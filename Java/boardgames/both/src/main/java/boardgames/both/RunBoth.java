package boardgames.both;

import boardgames.logic.RunLogicServer;
import boardgames.logic.games.tictactoe.TicTacToeMove;
import boardgames.persistence.RunPersistence;
import boardgames.shared.util.JsonUtil;

import java.util.ArrayList;

// NOTE(rune): Kun til debug. Starter både "game" og "persistence" serverne,
// så man ikke skal rundt i nær så mange IntelliJ menuer.
public class RunBoth {
    public static void main(String[] args) throws InterruptedException {
        // NOTE(rune): Første argument er persistence server url til logic,
        // resten er til Spring.
        final int NUM_LOGIC_ARGS = 1;

        ArrayList<String> logicArgs = new ArrayList<>();
        ArrayList<String> persistenceArgs = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            if (i < NUM_LOGIC_ARGS) {
                logicArgs.add(args[i]);
            } else {
                persistenceArgs.add(args[i]);
            }
        }

        Thread thread0 = new Thread(() -> RunPersistence.main(logicArgs.toArray(new String[0])));
        Thread thread1 = new Thread(() -> RunLogicServer.main(persistenceArgs.toArray(new String[0])));

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
