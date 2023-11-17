package boardgames.both;

import boardgames.logic.RunLogicServer;
import boardgames.logic.games.tictactoe.TicTacToeMove;
import boardgames.persistence.RunPersistence;
import boardgames.shared.util.JsonUtil;

// NOTE(rune): Kun til debug. Starter både "game" og "persistence" serverne,
// så man ikke skal rundt i nær så mange IntelliJ menuer.
public class RunBoth {
    public static void main(String[] args) throws InterruptedException {
        String s = "{\"placeOnIndex\":9,\"takeFromIndex\":0}";
        TicTacToeMove m = JsonUtil.fromJson(s, TicTacToeMove.class);

        Thread thread0 = new Thread(() -> RunPersistence.main(args));
        Thread thread1 = new Thread(() -> RunLogicServer.main(args));

        thread0.start();
        Thread.sleep(10000); // NOTE(rune): Spring startup tid med  margin. Hvad mon Spring bruger 5000 ms på?
        thread1.start();

        System.out.println("""
              ____   ___ _____ _   _    ____ _____  _    ____ _____ _____ ____
             | __ ) / _ \\_   _| | | |  / ___|_   _|/ \\  |  _ \\_   _| ____|  _ \\
             |  _ \\| | | || | | |_| |  \\___ \\ | | / _ \\ | |_) || | |  _| | | | |
             | |_) | |_| || | |  _  |   ___) || |/ ___ \\|  _ < | | | |___| |_| |
             |____/ \\___/ |_| |_| |_|  |____/ |_/_/   \\_\\_| \\_\\|_| |_____|____/
             
            """);

        thread0.join();
        thread1.join();

    }
}
