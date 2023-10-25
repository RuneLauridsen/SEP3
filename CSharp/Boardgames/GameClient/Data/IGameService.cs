namespace GameClient.Data;

public interface IGameService
{
    public Task<TicTacToeState> PlayAsync(TicTacToeState state);
}
