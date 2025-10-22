using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Animerah.Server.DTOs;
using Animerah.Server.Models;
using Animerah.Server.Services;

namespace Animerah.Server.Controllers;

[ApiController]
[Route("api/[controller]")]
public class AuthController : ControllerBase
{
    private readonly UserManager<User> _userManager;
    private readonly SignInManager<User> _signInManager;
    private readonly IJwtService _jwtService;
    
    public AuthController(
        UserManager<User> userManager,
        SignInManager<User> signInManager,
        IJwtService jwtService)
    {
        _userManager = userManager;
        _signInManager = signInManager;
        _jwtService = jwtService;
    }
    
    [HttpPost("register")]
    public async Task<ActionResult<AuthResponseDto>> Register(RegisterDto request)
    {
        if (!ModelState.IsValid)
            return BadRequest(ModelState);
            
        var user = new User
        {
            Email = request.Email,
            UserName = request.Email,
            DisplayName = request.DisplayName,
            CreatedAt = DateTime.UtcNow,
            LastLoginAt = DateTime.UtcNow
        };
        
        var result = await _userManager.CreateAsync(user, request.Password);
        
        if (!result.Succeeded)
        {
            foreach (var error in result.Errors)
            {
                ModelState.AddModelError(string.Empty, error.Description);
            }
            return BadRequest(ModelState);
        }
        
        var token = _jwtService.GenerateToken(user);
        
        return Ok(new AuthResponseDto
        {
            Token = token,
            UserId = user.Id,
            Email = user.Email!,
            DisplayName = user.DisplayName,
            IsAdmin = user.IsAdmin
        });
    }
    
    [HttpPost("login")]
    public async Task<ActionResult<AuthResponseDto>> Login(LoginDto request)
    {
        if (!ModelState.IsValid)
            return BadRequest(ModelState);
            
        var user = await _userManager.FindByEmailAsync(request.Email);
        if (user == null)
            return Unauthorized("Email ou mot de passe invalide");
            
        var result = await _signInManager.CheckPasswordSignInAsync(user, request.Password, false);
        
        if (!result.Succeeded)
            return Unauthorized("Email ou mot de passe invalide");
            
        user.LastLoginAt = DateTime.UtcNow;
        await _userManager.UpdateAsync(user);
        
        var token = _jwtService.GenerateToken(user);
        
        return Ok(new AuthResponseDto
        {
            Token = token,
            UserId = user.Id,
            Email = user.Email!,
            DisplayName = user.DisplayName,
            IsAdmin = user.IsAdmin
        });
    }
    
    [HttpGet("me")]
    [Authorize]
    public async Task<ActionResult<AuthResponseDto>> GetCurrentUser()
    {
        var userId = User.FindFirst(System.Security.Claims.ClaimTypes.NameIdentifier)?.Value;
        if (userId == null)
            return Unauthorized();
            
        var user = await _userManager.FindByIdAsync(userId);
        if (user == null)
            return NotFound();
            
        return Ok(new AuthResponseDto
        {
            Token = string.Empty, // Don't send token back
            UserId = user.Id,
            Email = user.Email!,
            DisplayName = user.DisplayName,
            IsAdmin = user.IsAdmin
        });
    }
}