namespace GameClient.Data;

public class TicTacToeState
{
    // NOTE(rune): String med 'X' og 'O'. 3 per. linje, seperaret med '\n'.
    // TODO(rune): QuickAndDirty
    public string Pieces { get; set; } = "";

    // NOTE(rune): Bruger-venlig besked, hvis bruger laver ugyldigt træk.
    // Tom string hvis ingen fejl.
    public string Error { get; set; } = "";
}
