using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Web;
using AdminClient.Services;
using Shared;
using Shared.AuthService;
using Shared.AuthState;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddRazorPages();
builder.Services.AddServerSideBlazor();
builder.Services.AddSingleton<Config>(new Config {
    LogicAddress = "localhost",
    LogicPort = 1234
});
builder.Services.AddScoped<IAuthState, AuthStateSessionStorage>();
builder.Services.AddScoped<IAuthService, JwtAuthService>();
builder.Services.AddScoped<IAdminService, AdminService>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error");
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseHttpsRedirection();

app.UseStaticFiles();

app.UseRouting();

app.MapBlazorHub();
app.MapFallbackToPage("/_Host");

app.Run();
