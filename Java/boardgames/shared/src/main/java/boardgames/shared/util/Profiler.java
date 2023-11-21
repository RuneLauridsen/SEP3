package boardgames.shared.util;

import java.util.ArrayList;

public class Profiler {
    private int index = 0;
    private final ArrayList<String> names = new ArrayList<>();
    private final ArrayList<Long> beginNanos = new ArrayList<>();
    private static ThreadLocal<Profiler> threadLocal = ThreadLocal.withInitial(Profiler::new);

    public static void begin(String name) {
        Profiler p = threadLocal.get();
        int i = p.index++;
        p.names.add(name);

        // NOTE(rune): Vi måler sådan set også tiden det tager for ArrayList::add,
        // men måler ikke i en størrelseordren, hvor det betyder noget.
        p.beginNanos.add(System.nanoTime());
    }

    public static ProfileItem end() {
        long endNano = System.nanoTime();

        Profiler p = threadLocal.get();
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
}
