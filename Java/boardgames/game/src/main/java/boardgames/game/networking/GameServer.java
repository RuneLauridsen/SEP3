package boardgames.game.networking;

public interface GameServer extends Runnable {
    public void run();
    public void close();
}
