using System.ComponentModel.DataAnnotations;

namespace Animerah.Server.DTOs;

public class CreateCommentDto
{
    [Required]
    public string Content { get; set; } = string.Empty;
    
    [Required]
    public int ArticleId { get; set; }
}

public class CommentDto
{
    public int Id { get; set; }
    public string Content { get; set; } = string.Empty;
    public DateTime CreatedAt { get; set; }
    public DateTime UpdatedAt { get; set; }
    public string UserName { get; set; } = string.Empty;
    public string UserId { get; set; } = string.Empty;
    public int ArticleId { get; set; }
}

public class CreateBigTalkRequestDto
{
    [Required]
    [StringLength(200)]
    public string Title { get; set; } = string.Empty;
    
    [Required]
    public string Reason { get; set; } = string.Empty;
    
    [Required]
    public int ArticleId { get; set; }
}

public class BigTalkRequestDto
{
    public int Id { get; set; }
    public string Title { get; set; } = string.Empty;
    public string Reason { get; set; } = string.Empty;
    public DateTime CreatedAt { get; set; }
    public string Status { get; set; } = string.Empty;
    public string? AdminResponse { get; set; }
    public DateTime? ReviewedAt { get; set; }
    public string RequesterName { get; set; } = string.Empty;
    public string RequesterId { get; set; } = string.Empty;
    public int ArticleId { get; set; }
    public string ArticleTitle { get; set; } = string.Empty;
    public string? ReviewedByName { get; set; }
}

public class ReviewBigTalkRequestDto
{
    [Required]
    public bool Approve { get; set; }
    
    public string? AdminResponse { get; set; }
}