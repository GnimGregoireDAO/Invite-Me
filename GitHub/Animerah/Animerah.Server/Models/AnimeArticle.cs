using System.ComponentModel.DataAnnotations;

namespace Animerah.Server.Models;

public class AnimeArticle
{
    public int Id { get; set; }
    
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
    
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    
    public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;
    
    public bool IsPublished { get; set; } = false;
    
    // Foreign key
    [Required]
    public string AuthorId { get; set; } = string.Empty;
    
    // Navigation properties
    public virtual User Author { get; set; } = null!;
    public virtual ICollection<Comment> Comments { get; set; } = new List<Comment>();
    public virtual ICollection<BigTalkRequest> BigTalkRequests { get; set; } = new List<BigTalkRequest>();
}