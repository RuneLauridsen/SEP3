using System.Runtime.InteropServices.JavaScript;

namespace GameClient.Data;

public class GameService : IGameService
{
    private static int i = 0;

    public Task<TicTacToeState> PlayAsync(TicTacToeState state)
    {
        TicTacToeState ret = new();

        if (i % 2 == 0)
        {
            ret.Error = "";
            ret.Pieces = "AAA";
        }
        else
        {
            ret.Error = "You made an error";
            ret.Pieces = "Gibberish";
        }

        i++;

        return Task.FromResult(ret);
    }
}
