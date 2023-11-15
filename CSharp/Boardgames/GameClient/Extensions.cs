using Microsoft.AspNetCore.Components;

namespace GameClient;

public static class Extensions {
    // NOTE(rune): https://stackoverflow.com/a/75747008
    public static void ReloadCurrentPage(this NavigationManager manager) {
        manager.NavigateTo(manager.Uri, true);
    }
}
