using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Animerah.Server.Models;

namespace Animerah.Server.Data;

public class ApplicationDbContext : IdentityDbContext<User>
{
    public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
        : base(options)
    {
    }
    
    public DbSet<AnimeArticle> AnimeArticles { get; set; }
    public DbSet<Comment> Comments { get; set; }
    public DbSet<BigTalkRequest> BigTalkRequests { get; set; }
    public DbSet<BigTalk> BigTalks { get; set; }
    public DbSet<BigTalkMessage> BigTalkMessages { get; set; }
    
    protected override void OnModelCreating(ModelBuilder builder)
    {
        base.OnModelCreating(builder);
        
        // Configure AnimeArticle relationships
        builder.Entity<AnimeArticle>()
            .HasOne(a => a.Author)
            .WithMany(u => u.Articles)
            .HasForeignKey(a => a.AuthorId)
            .OnDelete(DeleteBehavior.Restrict); // Changé de Cascade à Restrict
            
        // Configure Comment relationships
        builder.Entity<Comment>()
            .HasOne(c => c.User)
            .WithMany(u => u.Comments)
            .HasForeignKey(c => c.UserId)
            .OnDelete(DeleteBehavior.Restrict); // Changé de Cascade à Restrict
            
        builder.Entity<Comment>()
            .HasOne(c => c.Article)
            .WithMany(a => a.Comments)
            .HasForeignKey(c => c.ArticleId)
            .OnDelete(DeleteBehavior.Cascade);
            
        // Configure BigTalkRequest relationships
        builder.Entity<BigTalkRequest>()
            .HasOne(r => r.Requester)
            .WithMany(u => u.BigTalkRequests)
            .HasForeignKey(r => r.RequesterId)
            .OnDelete(DeleteBehavior.Restrict); // Changé de Cascade à Restrict
            
        builder.Entity<BigTalkRequest>()
            .HasOne(r => r.Article)
            .WithMany(a => a.BigTalkRequests)
            .HasForeignKey(r => r.ArticleId)
            .OnDelete(DeleteBehavior.Cascade);
            
        builder.Entity<BigTalkRequest>()
            .HasOne(r => r.ReviewedBy)
            .WithMany()
            .HasForeignKey(r => r.ReviewedById)
            .OnDelete(DeleteBehavior.SetNull);
            
        // Configure BigTalk relationships
        builder.Entity<BigTalk>()
            .HasOne(bt => bt.BigTalkRequest)
            .WithOne(r => r.BigTalk)
            .HasForeignKey<BigTalk>(bt => bt.BigTalkRequestId)
            .OnDelete(DeleteBehavior.Cascade);
            
        builder.Entity<BigTalk>()
            .HasOne(bt => bt.CreatedBy)
            .WithMany(u => u.CreatedBigTalks)
            .HasForeignKey(bt => bt.CreatedById)
            .OnDelete(DeleteBehavior.Restrict); // Changé de Cascade à Restrict
            
        // Configure BigTalkMessage relationships
        builder.Entity<BigTalkMessage>()
            .HasOne(m => m.BigTalk)
            .WithMany(bt => bt.Messages)
            .HasForeignKey(m => m.BigTalkId)
            .OnDelete(DeleteBehavior.Cascade);
            
        builder.Entity<BigTalkMessage>()
            .HasOne(m => m.User)
            .WithMany()
            .HasForeignKey(m => m.UserId)
            .OnDelete(DeleteBehavior.Restrict); // Changé de Cascade à Restrict
    }
}