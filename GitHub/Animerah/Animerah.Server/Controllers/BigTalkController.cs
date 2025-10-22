using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Animerah.Server.Data;
using Animerah.Server.DTOs;
using Animerah.Server.Models;

namespace Animerah.Server.Controllers;

[ApiController]
[Route("api/[controller]")]
public class BigTalkController : ControllerBase
{
    private readonly ApplicationDbContext _context;
    
    public BigTalkController(ApplicationDbContext context)
    {
        _context = context;
    }
    
    [HttpPost("request")]
    [Authorize]
    public async Task<ActionResult<BigTalkRequestDto>> CreateBigTalkRequest(CreateBigTalkRequestDto request)
    {
        if (!ModelState.IsValid)
            return BadRequest(ModelState);
            
        var userId = User.FindFirst(System.Security.Claims.ClaimTypes.NameIdentifier)?.Value;
        if (userId == null)
            return Unauthorized();
            
        // Check if article exists
        var articleExists = await _context.AnimeArticles.AnyAsync(a => a.Id == request.ArticleId);
        if (!articleExists)
            return BadRequest("Article non trouvé");
            
        // Check if user already has a pending request for this article
        var existingRequest = await _context.BigTalkRequests
            .AnyAsync(r => r.ArticleId == request.ArticleId && 
                          r.RequesterId == userId && 
                          r.Status == BigTalkStatus.Pending);
                          
        if (existingRequest)
            return BadRequest("Vous avez déjà une demande en attente pour cet article");
            
        var bigTalkRequest = new BigTalkRequest
        {
            Title = request.Title,
            Reason = request.Reason,
            ArticleId = request.ArticleId,
            RequesterId = userId,
            CreatedAt = DateTime.UtcNow,
            Status = BigTalkStatus.Pending
        };
        
        _context.BigTalkRequests.Add(bigTalkRequest);
        await _context.SaveChangesAsync();
        
        // Load the request with related data
        var createdRequest = await _context.BigTalkRequests
            .Include(r => r.Requester)
            .Include(r => r.Article)
            .FirstAsync(r => r.Id == bigTalkRequest.Id);
            
        return CreatedAtAction(nameof(GetBigTalkRequest), 
            new { id = bigTalkRequest.Id }, 
            new BigTalkRequestDto
            {
                Id = createdRequest.Id,
                Title = createdRequest.Title,
                Reason = createdRequest.Reason,
                CreatedAt = createdRequest.CreatedAt,
                Status = createdRequest.Status.ToString(),
                AdminResponse = createdRequest.AdminResponse,
                ReviewedAt = createdRequest.ReviewedAt,
                RequesterName = createdRequest.Requester.DisplayName,
                RequesterId = createdRequest.RequesterId,
                ArticleId = createdRequest.ArticleId,
                ArticleTitle = createdRequest.Article.Title,
                ReviewedByName = null
            });
    }
    
    [HttpGet("requests")]
    [Authorize]
    public async Task<ActionResult<IEnumerable<BigTalkRequestDto>>> GetBigTalkRequests(
        [FromQuery] string? status = null)
    {
        var userId = User.FindFirst(System.Security.Claims.ClaimTypes.NameIdentifier)?.Value;
        var isAdmin = User.HasClaim("IsAdmin", "True");
        
        var query = _context.BigTalkRequests
            .Include(r => r.Requester)
            .Include(r => r.Article)
            .Include(r => r.ReviewedBy)
            .AsQueryable();
            
        // Non-admins can only see their own requests
        if (!isAdmin)
        {
            query = query.Where(r => r.RequesterId == userId);
        }
        
        if (!string.IsNullOrEmpty(status) && Enum.TryParse<BigTalkStatus>(status, true, out var statusEnum))
        {
            query = query.Where(r => r.Status == statusEnum);
        }
        
        var requests = await query
            .OrderByDescending(r => r.CreatedAt)
            .Select(r => new BigTalkRequestDto
            {
                Id = r.Id,
                Title = r.Title,
                Reason = r.Reason,
                CreatedAt = r.CreatedAt,
                Status = r.Status.ToString(),
                AdminResponse = r.AdminResponse,
                ReviewedAt = r.ReviewedAt,
                RequesterName = r.Requester.DisplayName,
                RequesterId = r.RequesterId,
                ArticleId = r.ArticleId,
                ArticleTitle = r.Article.Title,
                ReviewedByName = r.ReviewedBy != null ? r.ReviewedBy.DisplayName : null
            })
            .ToListAsync();
            
        return Ok(requests);
    }
    
    [HttpGet("requests/{id}")]
    [Authorize]
    public async Task<ActionResult<BigTalkRequestDto>> GetBigTalkRequest(int id)
    {
        var userId = User.FindFirst(System.Security.Claims.ClaimTypes.NameIdentifier)?.Value;
        var isAdmin = User.HasClaim("IsAdmin", "True");
        
        var request = await _context.BigTalkRequests
            .Include(r => r.Requester)
            .Include(r => r.Article)
            .Include(r => r.ReviewedBy)
            .FirstOrDefaultAsync(r => r.Id == id);
            
        if (request == null)
            return NotFound();
            
        // Non-admins can only see their own requests
        if (!isAdmin && request.RequesterId != userId)
            return Forbid();
            
        return Ok(new BigTalkRequestDto
        {
            Id = request.Id,
            Title = request.Title,
            Reason = request.Reason,
            CreatedAt = request.CreatedAt,
            Status = request.Status.ToString(),
            AdminResponse = request.AdminResponse,
            ReviewedAt = request.ReviewedAt,
            RequesterName = request.Requester.DisplayName,
            RequesterId = request.RequesterId,
            ArticleId = request.ArticleId,
            ArticleTitle = request.Article.Title,
            ReviewedByName = request.ReviewedBy?.DisplayName
        });
    }
    
    [HttpPost("requests/{id}/review")]
    [Authorize]
    public async Task<ActionResult<BigTalkRequestDto>> ReviewBigTalkRequest(int id, ReviewBigTalkRequestDto review)
    {
        if (!User.HasClaim("IsAdmin", "True"))
            return Forbid("Seuls les administrateurs peuvent réviser les demandes");
            
        var userId = User.FindFirst(System.Security.Claims.ClaimTypes.NameIdentifier)?.Value;
        if (userId == null)
            return Unauthorized();
            
        var request = await _context.BigTalkRequests
            .Include(r => r.Requester)
            .Include(r => r.Article)
            .FirstOrDefaultAsync(r => r.Id == id);
            
        if (request == null)
            return NotFound();
            
        if (request.Status != BigTalkStatus.Pending)
            return BadRequest("Cette demande a déjà été révisée");
            
        request.Status = review.Approve ? BigTalkStatus.Approved : BigTalkStatus.Rejected;
        request.AdminResponse = review.AdminResponse;
        request.ReviewedAt = DateTime.UtcNow;
        request.ReviewedById = userId;
        
        // If approved, create the BigTalk
        if (review.Approve)
        {
            var bigTalk = new BigTalk
            {
                Title = request.Title,
                Description = request.Reason,
                BigTalkRequestId = request.Id,
                CreatedById = userId,
                CreatedAt = DateTime.UtcNow,
                IsActive = true
            };
            
            _context.BigTalks.Add(bigTalk);
        }
        
        await _context.SaveChangesAsync();
        
        // Reload with reviewer info
        var updatedRequest = await _context.BigTalkRequests
            .Include(r => r.Requester)
            .Include(r => r.Article)
            .Include(r => r.ReviewedBy)
            .FirstAsync(r => r.Id == id);
            
        return Ok(new BigTalkRequestDto
        {
            Id = updatedRequest.Id,
            Title = updatedRequest.Title,
            Reason = updatedRequest.Reason,
            CreatedAt = updatedRequest.CreatedAt,
            Status = updatedRequest.Status.ToString(),
            AdminResponse = updatedRequest.AdminResponse,
            ReviewedAt = updatedRequest.ReviewedAt,
            RequesterName = updatedRequest.Requester.DisplayName,
            RequesterId = updatedRequest.RequesterId,
            ArticleId = updatedRequest.ArticleId,
            ArticleTitle = updatedRequest.Article.Title,
            ReviewedByName = updatedRequest.ReviewedBy?.DisplayName
        });
    }
    
    [HttpGet("talks")]
    public async Task<ActionResult<IEnumerable<object>>> GetActiveBigTalks()
    {
        var bigTalks = await _context.BigTalks
            .Include(bt => bt.BigTalkRequest)
            .ThenInclude(r => r.Article)
            .Include(bt => bt.CreatedBy)
            .Include(bt => bt.Messages)
            .ThenInclude(m => m.User)
            .Where(bt => bt.IsActive)
            .OrderByDescending(bt => bt.CreatedAt)
            .Select(bt => new
            {
                bt.Id,
                bt.Title,
                bt.Description,
                bt.CreatedAt,
                bt.IsActive,
                Article = new
                {
                    bt.BigTalkRequest.Article.Id,
                    bt.BigTalkRequest.Article.Title
                },
                CreatedBy = bt.CreatedBy.DisplayName,
                MessagesCount = bt.Messages.Count,
                LastMessageAt = bt.Messages.Any() ? 
                    bt.Messages.Max(m => m.CreatedAt) : 
                    (DateTime?)null
            })
            .ToListAsync();
            
        return Ok(bigTalks);
    }
}