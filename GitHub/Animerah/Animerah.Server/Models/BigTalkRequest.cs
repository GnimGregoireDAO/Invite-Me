using System.ComponentModel.DataAnnotations;

namespace Animerah.Server.Models;

public enum BigTalkStatus
{
    Pending,
    Approved,
    Rejected
}

public class BigTalkRequest
{
    public int Id { get; set; }
    
    [Required]
    [StringLength(200)]
    public string Title { get; set; } = string.Empty;
    
    [Required]
    public string Reason { get; set; } = string.Empty;
    
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    
    public BigTalkStatus Status { get; set; } = BigTalkStatus.Pending;
    
    public string? AdminResponse { get; set; }
    
    public DateTime? ReviewedAt { get; set; }
    
    // Foreign keys
    [Required]
    public string RequesterId { get; set; } = string.Empty;
    
    [Required]
    public int ArticleId { get; set; }
    
    public string? ReviewedById { get; set; }
    
    // Navigation properties
    public virtual User Requester { get; set; } = null!;
    public virtual AnimeArticle Article { get; set; } = null!;
    public virtual User? ReviewedBy { get; set; }
    public virtual BigTalk? BigTalk { get; set; }
}