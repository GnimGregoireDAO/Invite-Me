using System.ComponentModel.DataAnnotations;

namespace Animerah.Server.Models;

public class Comment
{
    public int Id { get; set; }
    
    [Required]
    public string Content { get; set; } = string.Empty;
    
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    
    public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;
    
    // Foreign keys
    [Required]
    public string UserId { get; set; } = string.Empty;
    
    [Required]
    public int ArticleId { get; set; }
    
    // Navigation properties
    public virtual User User { get; set; } = null!;
    public virtual AnimeArticle Article { get; set; } = null!;
}