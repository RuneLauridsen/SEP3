package boardgames.shared.util;

import java.util.ArrayList;
import java.util.function.Supplier;

public class Timer {
    private int index = 0;
    private final ArrayList<String> names = new ArrayList<>();
    private final ArrayList<Long> beginNanos = new ArrayList<>();
    private static ThreadLocal<Timer> threadLocal = ThreadLocal.withInitial(Timer::new);

    // NOTE(rune): https://stackoverflow.com/a/68674306.
    private static String getCallerName(int depth) {
        StackWalker.StackFrame frame = StackWalker
            .getInstance()
            .walk(stream -> stream
                .filter(x -> x.getClassName().startsWith("boardgames"))
                .filter(x -> !x.getClassName().endsWith("Timer"))
                .skip(depth)
                .findFirst()
                .get()
            );

        return frame.getClassName() + "." + frame.getMethodName();
    }

    public static void begin() {
        begin(0);
    }

    public static void begin(int depth) {
        begin(getCallerName(depth));
    }

    public static void begin(String name) {
        Timer p = threadLocal.get();
        int i = p.index++;
        p.names.add(name);

        // NOTE(rune): Vi måler sådan set også tiden det tager for ArrayList.add,
        // men med størrelseordren vi er interesseret i (millisekunder), betyder
        // det ikke noget.
        p.beginNanos.add(System.nanoTime());
    }

    public static ProfileItem end() {
        long endNano = System.nanoTime();

        Timer p = threadLocal.get();
        String name = p.names.remove(p.names.size() - 1);
        long beginNano = p.beginNanos.remove(p.beginNanos.size() - 1);
        long nanoDiff = endNano - beginNano;
        double elapsedMillis = (double) nanoDiff / (1000.0 * 1000.0);

        return new ProfileItem(name, beginNano, endNano, elapsedMillis);
    }

    public static void endAndPrint() {
        ProfileItem item = end();
        System.out.println(item.name() + " took " + item.elapsedMillis() + " ms");
    }

    public static void scope(Runnable scope) {
        try {
            Timer.begin();
            scope.run();
        } finally {
            Timer.endAndPrint();
        }
    }

    public static <T> T scope(Supplier<T> scope) {
        try {
            Timer.begin();
            T ret = scope.get();
            return ret;
        } finally {
            Timer.endAndPrint();
        }
    }
}
