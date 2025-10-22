using System.ComponentModel.DataAnnotations;

namespace Animerah.Server.Models;

public class BigTalkMessage
{
    public int Id { get; set; }
    
    [Required]
    public string Content { get; set; } = string.Empty;
    
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    
    // Foreign keys
    [Required]
    public int BigTalkId { get; set; }
    
    [Required]
    public string UserId { get; set; } = string.Empty;
    
    // Navigation properties
    public virtual BigTalk BigTalk { get; set; } = null!;
    public virtual User User { get; set; } = null!;
}