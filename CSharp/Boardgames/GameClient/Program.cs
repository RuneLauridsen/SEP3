using GameClient;
using GameClient.DTO;
using GameClient.Services;
using Shared;
using Shared.AuthService;
using Shared.AuthState;
using Shared.Data;



var builder = WebApplication.CreateBuilder(args);














// Add services to the container.
builder.Services.AddRazorPages();
builder.Services.AddServerSideBlazor();
// Add our Config object so it can be injected
builder.Services.AddSingleton<Config>(new Config {
    LogicAddress = "localhost",
    LogicPort = 1234
});
builder.Services.AddScoped<IAuthState, AuthStateSessionStorage>();
builder.Services.AddTransient<IAuthService, JwtAuthService>();
builder.Services.AddTransient<IGameService, GameService>();
builder.Services.AddScoped<ILiveService, LiveService>();



var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment()) {
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
