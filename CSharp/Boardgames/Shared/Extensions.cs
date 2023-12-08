using System.Security.Claims;
using Microsoft.AspNetCore.Components;

namespace Shared;

public static class Extensions {
    public static string GetOrDefault(this ClaimsPrincipal claimsPrincipal, string type, string defaultValue) {
        var claim = claimsPrincipal.Claims.FirstOrDefault(x => x.Type == type);
        return claim?.Value ?? defaultValue;
    }
}
