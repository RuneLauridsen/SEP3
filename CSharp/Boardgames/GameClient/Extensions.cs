using System.Security.Claims;
using Microsoft.AspNetCore.Components;

namespace GameClient;

public static class Extensions {
    // NOTE(rune): https://stackoverflow.com/a/75747008
    public static void ReloadCurrentPage(this NavigationManager manager) {
        manager.NavigateTo(manager.Uri, true);
    }

    public static string GetOrDefault(this ClaimsPrincipal claimsPrincipal, string type, string defaultValue) {
        var claim = claimsPrincipal.Claims.FirstOrDefault(x => x.Type == type);
        return claim?.Value ?? defaultValue;
    }
}
