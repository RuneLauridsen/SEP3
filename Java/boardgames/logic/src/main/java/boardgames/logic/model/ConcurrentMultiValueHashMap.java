package boardgames.logic.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

// DOVEN IMPLEMENTATION
public class ConcurrentMultiValueHashMap<K, V> {
    private final MultiValueHashMap<K, V> backing;

    public ConcurrentMultiValueHashMap() {
        backing = new MultiValueHashMap<>();
    }

    public synchronized void put(K key, V value) {
        backing.put(key, value);
    }

    public synchronized void remove(V value) {
        backing.remove(value);
    }

    public synchronized Collection<V> get(K key) {
        return backing.get(key);
    }

    public synchronized int count() {
        return backing.count();
    }
}
