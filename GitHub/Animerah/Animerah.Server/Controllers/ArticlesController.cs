using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Animerah.Server.Data;
using Animerah.Server.DTOs;
using Animerah.Server.Models;

namespace Animerah.Server.Controllers;

[ApiController]
[Route("api/[controller]")]
public class ArticlesController : ControllerBase
{
    private readonly ApplicationDbContext _context;
    
    public ArticlesController(ApplicationDbContext context)
    {
        _context = context;
    }
    
    [HttpGet]
    public async Task<ActionResult<IEnumerable<ArticleDto>>> GetArticles(
        [FromQuery] int page = 1,
        [FromQuery] int pageSize = 10,
        [FromQuery] string? search = null,
        [FromQuery] string? genre = null)
    {
        var query = _context.AnimeArticles
            .Include(a => a.Author)
            .Include(a => a.Comments)
            .Where(a => a.IsPublished)
            .AsQueryable();
            
        if (!string.IsNullOrEmpty(search))
        {
            query = query.Where(a => a.Title.Contains(search) || 
                                   a.AnimeName.Contains(search) || 
                                   a.Content.Contains(search));
        }
        
        if (!string.IsNullOrEmpty(genre))
        {
            query = query.Where(a => a.AnimeGenre.Contains(genre));
        }
        
        var articles = await query
            .OrderByDescending(a => a.CreatedAt)
            .Skip((page - 1) * pageSize)
            .Take(pageSize)
            .Select(a => new ArticleDto
            {
                Id = a.Id,
                Title = a.Title,
                Content = a.Content,
                Summary = a.Summary,
                ImageUrl = a.ImageUrl,
                AnimeName = a.AnimeName,
                AnimeGenre = a.AnimeGenre,
                CreatedAt = a.CreatedAt,
                UpdatedAt = a.UpdatedAt,
                IsPublished = a.IsPublished,
                AuthorName = a.Author.DisplayName,
                AuthorId = a.AuthorId,
                CommentsCount = a.Comments.Count
            })
            .ToListAsync();
            
        return Ok(articles);
    }
    
    [HttpGet("{id}")]
    public async Task<ActionResult<ArticleDto>> GetArticle(int id)
    {
        var article = await _context.AnimeArticles
            .Include(a => a.Author)
            .Include(a => a.Comments)
            .FirstOrDefaultAsync(a => a.Id == id);
            
        if (article == null)
            return NotFound();
            
        if (!article.IsPublished)
        {
            var userId = User.FindFirst(System.Security.Claims.ClaimTypes.NameIdentifier)?.Value;
            if (userId != article.AuthorId && !User.HasClaim("IsAdmin", "True"))
                return NotFound();
        }
        
        return Ok(new ArticleDto
        {
            Id = article.Id,
            Title = article.Title,
            Content = article.Content,
            Summary = article.Summary,
            ImageUrl = article.ImageUrl,
            AnimeName = article.AnimeName,
            AnimeGenre = article.AnimeGenre,
            CreatedAt = article.CreatedAt,
            UpdatedAt = article.UpdatedAt,
            IsPublished = article.IsPublished,
            AuthorName = article.Author.DisplayName,
            AuthorId = article.AuthorId,
            CommentsCount = article.Comments.Count
        });
    }
    
    [HttpPost]
    [Authorize]
    public async Task<ActionResult<ArticleDto>> CreateArticle(CreateArticleDto request)
    {
        if (!ModelState.IsValid)
            return BadRequest(ModelState);
            
        var userId = User.FindFirst(System.Security.Claims.ClaimTypes.NameIdentifier)?.Value;
        if (userId == null)
            return Unauthorized();
            
        // Only admins can create articles
        if (!User.HasClaim("IsAdmin", "True"))
            return Forbid("Seuls les administrateurs peuvent créer des articles");
            
        var article = new AnimeArticle
        {
            Title = request.Title,
            Content = request.Content,
            Summary = request.Summary,
            ImageUrl = request.ImageUrl,
            AnimeName = request.AnimeName,
            AnimeGenre = request.AnimeGenre,
            IsPublished = request.IsPublished,
            AuthorId = userId,
            CreatedAt = DateTime.UtcNow,
            UpdatedAt = DateTime.UtcNow
        };
        
        _context.AnimeArticles.Add(article);
        await _context.SaveChangesAsync();
        
        // Load the article with author information
        var createdArticle = await _context.AnimeArticles
            .Include(a => a.Author)
            .Include(a => a.Comments)
            .FirstAsync(a => a.Id == article.Id);
            
        return CreatedAtAction(nameof(GetArticle), new { id = article.Id }, new ArticleDto
        {
            Id = createdArticle.Id,
            Title = createdArticle.Title,
            Content = createdArticle.Content,
            Summary = createdArticle.Summary,
            ImageUrl = createdArticle.ImageUrl,
            AnimeName = createdArticle.AnimeName,
            AnimeGenre = createdArticle.AnimeGenre,
            CreatedAt = createdArticle.CreatedAt,
            UpdatedAt = createdArticle.UpdatedAt,
            IsPublished = createdArticle.IsPublished,
            AuthorName = createdArticle.Author.DisplayName,
            AuthorId = createdArticle.AuthorId,
            CommentsCount = createdArticle.Comments.Count
        });
    }
    
    [HttpPut("{id}")]
    [Authorize]
    public async Task<ActionResult<ArticleDto>> UpdateArticle(int id, UpdateArticleDto request)
    {
        var article = await _context.AnimeArticles
            .Include(a => a.Author)
            .Include(a => a.Comments)
            .FirstOrDefaultAsync(a => a.Id == id);
            
        if (article == null)
            return NotFound();
            
        var userId = User.FindFirst(System.Security.Claims.ClaimTypes.NameIdentifier)?.Value;
        
        // Only author or admin can update
        if (userId != article.AuthorId && !User.HasClaim("IsAdmin", "True"))
            return Forbid();
            
        if (!string.IsNullOrEmpty(request.Title))
            article.Title = request.Title;
        if (!string.IsNullOrEmpty(request.Content))
            article.Content = request.Content;
        if (request.Summary != null)
            article.Summary = request.Summary;
        if (request.ImageUrl != null)
            article.ImageUrl = request.ImageUrl;
        if (request.AnimeName != null)
            article.AnimeName = request.AnimeName;
        if (request.AnimeGenre != null)
            article.AnimeGenre = request.AnimeGenre;
        if (request.IsPublished.HasValue)
            article.IsPublished = request.IsPublished.Value;
            
        article.UpdatedAt = DateTime.UtcNow;
        
        await _context.SaveChangesAsync();
        
        return Ok(new ArticleDto
        {
            Id = article.Id,
            Title = article.Title,
            Content = article.Content,
            Summary = article.Summary,
            ImageUrl = article.ImageUrl,
            AnimeName = article.AnimeName,
            AnimeGenre = article.AnimeGenre,
            CreatedAt = article.CreatedAt,
            UpdatedAt = article.UpdatedAt,
            IsPublished = article.IsPublished,
            AuthorName = article.Author.DisplayName,
            AuthorId = article.AuthorId,
            CommentsCount = article.Comments.Count
        });
    }
    
    [HttpDelete("{id}")]
    [Authorize]
    public async Task<IActionResult> DeleteArticle(int id)
    {
        var article = await _context.AnimeArticles.FindAsync(id);
        if (article == null)
            return NotFound();
            
        var userId = User.FindFirst(System.Security.Claims.ClaimTypes.NameIdentifier)?.Value;
        
        // Only author or admin can delete
        if (userId != article.AuthorId && !User.HasClaim("IsAdmin", "True"))
            return Forbid();
            
        _context.AnimeArticles.Remove(article);
        await _context.SaveChangesAsync();
        
        return NoContent();
    }
}