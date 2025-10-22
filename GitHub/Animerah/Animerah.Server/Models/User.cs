using Microsoft.AspNetCore.Identity;
using System.ComponentModel.DataAnnotations;

namespace Animerah.Server.Models;

public class User : IdentityUser
{
    [Required]
    [StringLength(100)]
    public string DisplayName { get; set; } = string.Empty;
    
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    
    public DateTime LastLoginAt { get; set; }
    
    public bool IsAdmin { get; set; } = false;
    
    // Navigation properties
    public virtual ICollection<AnimeArticle> Articles { get; set; } = new List<AnimeArticle>();
    public virtual ICollection<Comment> Comments { get; set; } = new List<Comment>();
    public virtual ICollection<BigTalkRequest> BigTalkRequests { get; set; } = new List<BigTalkRequest>();
    public virtual ICollection<BigTalk> CreatedBigTalks { get; set; } = new List<BigTalk>();
}