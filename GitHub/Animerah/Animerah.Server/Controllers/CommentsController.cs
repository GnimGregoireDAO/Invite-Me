using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Animerah.Server.Data;
using Animerah.Server.DTOs;
using Animerah.Server.Models;

namespace Animerah.Server.Controllers;

[ApiController]
[Route("api/[controller]")]
public class CommentsController : ControllerBase
{
    private readonly ApplicationDbContext _context;
    
    public CommentsController(ApplicationDbContext context)
    {
        _context = context;
    }
    
    [HttpGet("article/{articleId}")]
    public async Task<ActionResult<IEnumerable<CommentDto>>> GetCommentsByArticle(int articleId)
    {
        var comments = await _context.Comments
            .Include(c => c.User)
            .Where(c => c.ArticleId == articleId)
            .OrderBy(c => c.CreatedAt)
            .Select(c => new CommentDto
            {
                Id = c.Id,
                Content = c.Content,
                CreatedAt = c.CreatedAt,
                UpdatedAt = c.UpdatedAt,
                UserName = c.User.DisplayName,
                UserId = c.UserId,
                ArticleId = c.ArticleId
            })
            .ToListAsync();
            
        return Ok(comments);
    }
    
    [HttpPost]
    [Authorize]
    public async Task<ActionResult<CommentDto>> CreateComment(CreateCommentDto request)
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
            
        var comment = new Comment
        {
            Content = request.Content,
            ArticleId = request.ArticleId,
            UserId = userId,
            CreatedAt = DateTime.UtcNow,
            UpdatedAt = DateTime.UtcNow
        };
        
        _context.Comments.Add(comment);
        await _context.SaveChangesAsync();
        
        // Load the comment with user information
        var createdComment = await _context.Comments
            .Include(c => c.User)
            .FirstAsync(c => c.Id == comment.Id);
            
        return CreatedAtAction(nameof(GetCommentsByArticle), 
            new { articleId = request.ArticleId }, 
            new CommentDto
            {
                Id = createdComment.Id,
                Content = createdComment.Content,
                CreatedAt = createdComment.CreatedAt,
                UpdatedAt = createdComment.UpdatedAt,
                UserName = createdComment.User.DisplayName,
                UserId = createdComment.UserId,
                ArticleId = createdComment.ArticleId
            });
    }
    
    [HttpPut("{id}")]
    [Authorize]
    public async Task<ActionResult<CommentDto>> UpdateComment(int id, [FromBody] string content)
    {
        if (string.IsNullOrWhiteSpace(content))
            return BadRequest("Le contenu ne peut pas être vide");
            
        var comment = await _context.Comments
            .Include(c => c.User)
            .FirstOrDefaultAsync(c => c.Id == id);
            
        if (comment == null)
            return NotFound();
            
        var userId = User.FindFirst(System.Security.Claims.ClaimTypes.NameIdentifier)?.Value;
        
        // Only comment author or admin can update
        if (userId != comment.UserId && !User.HasClaim("IsAdmin", "True"))
            return Forbid();
            
        comment.Content = content;
        comment.UpdatedAt = DateTime.UtcNow;
        
        await _context.SaveChangesAsync();
        
        return Ok(new CommentDto
        {
            Id = comment.Id,
            Content = comment.Content,
            CreatedAt = comment.CreatedAt,
            UpdatedAt = comment.UpdatedAt,
            UserName = comment.User.DisplayName,
            UserId = comment.UserId,
            ArticleId = comment.ArticleId
        });
    }
    
    [HttpDelete("{id}")]
    [Authorize]
    public async Task<IActionResult> DeleteComment(int id)
    {
        var comment = await _context.Comments.FindAsync(id);
        if (comment == null)
            return NotFound();
            
        var userId = User.FindFirst(System.Security.Claims.ClaimTypes.NameIdentifier)?.Value;
        
        // Only comment author or admin can delete
        if (userId != comment.UserId && !User.HasClaim("IsAdmin", "True"))
            return Forbid();
            
        _context.Comments.Remove(comment);
        await _context.SaveChangesAsync();
        
        return NoContent();
    }
}