package boardgames.logic.networking;

public interface GameServer extends Runnable {
    public void run();
    public void close();
}
