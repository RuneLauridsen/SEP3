package boardgames.logic.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultiValueHashMap<K, V> {
    private Map<K, Set<V>> backing;
    private int count;

    public MultiValueHashMap() {
        backing = new HashMap<>();
    }

    public void put(K key, V value) {
        Set<V> set = backing.computeIfAbsent(key, k -> new HashSet<>());
        if (set.add(value)) {
            count++;
        }
    }

    public void remove(V value) {
        for (Set<V> set : backing.values()) {
            if (set.remove(value)) {
                count--;
            }
        }
    }

    public Collection<V> get(K key) {
        Set<V> set = backing.get(key);
        if (set == null) {
            return Collections.emptyList();
        } else {
            return set;
        }
    }

    public int count() {
        return count;
    }
}
