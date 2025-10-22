using System.ComponentModel.DataAnnotations;

namespace Animerah.Server.Models;

public class BigTalk
{
    public int Id { get; set; }
    
    [Required]
    [StringLength(200)]
    public string Title { get; set; } = string.Empty;
    
    [Required]
    public string Description { get; set; } = string.Empty;
    
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    
    public bool IsActive { get; set; } = true;
    
    // Foreign keys
    [Required]
    public int BigTalkRequestId { get; set; }
    
    [Required]
    public string CreatedById { get; set; } = string.Empty;
    
    // Navigation properties
    public virtual BigTalkRequest BigTalkRequest { get; set; } = null!;
    public virtual User CreatedBy { get; set; } = null!;
    public virtual ICollection<BigTalkMessage> Messages { get; set; } = new List<BigTalkMessage>();
}