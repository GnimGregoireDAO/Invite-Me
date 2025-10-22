using System.ComponentModel.DataAnnotations;

namespace Animerah.Server.DTOs;

public class CreateArticleDto
{
    [Required]
    [StringLength(200)]
    public string Title { get; set; } = string.Empty;
    
    [Required]
    public string Content { get; set; } = string.Empty;
    
    [StringLength(500)]
    public string Summary { get; set; } = string.Empty;
    
    [StringLength(200)]
    public string ImageUrl { get; set; } = string.Empty;
    
    [StringLength(100)]
    public string AnimeName { get; set; } = string.Empty;
    
    [StringLength(50)]
    public string AnimeGenre { get; set; } = string.Empty;
    
    public bool IsPublished { get; set; } = false;
}

public class UpdateArticleDto
{
    [StringLength(200)]
    public string? Title { get; set; }
    
    public string? Content { get; set; }
    
    [StringLength(500)]
    public string? Summary { get; set; }
    
    [StringLength(200)]
    public string? ImageUrl { get; set; }
    
    [StringLength(100)]
    public string? AnimeName { get; set; }
    
    [StringLength(50)]
    public string? AnimeGenre { get; set; }
    
    public bool? IsPublished { get; set; }
}

public class ArticleDto
{
    public int Id { get; set; }
    public string Title { get; set; } = string.Empty;
    public string Content { get; set; } = string.Empty;
    public string Summary { get; set; } = string.Empty;
    public string ImageUrl { get; set; } = string.Empty;
    public string AnimeName { get; set; } = string.Empty;
    public string AnimeGenre { get; set; } = string.Empty;
    public DateTime CreatedAt { get; set; }
    public DateTime UpdatedAt { get; set; }
    public bool IsPublished { get; set; }
    public string AuthorName { get; set; } = string.Empty;
    public string AuthorId { get; set; } = string.Empty;
    public int CommentsCount { get; set; }
}