package boardgames.logic.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// NOTE(rune):
// Mange ting vi laver i LogicServerModelImpl på Match'es er ikke atomiske,
// bla. fordi de kører mere end ét SQL statement og sender http request/resopnse
// over netværk. I stedet for at fixe problemet bruger vi IndexedMutexes
// til at låse per. matchId.

public class IndexedLocks {
    private final Map<Integer, Lock> map;

    public IndexedLocks() {
        map = new HashMap<>();
    }

    public synchronized boolean lockIfFree(int id) {
        if (map.containsKey(id)) {
            return false;
        } else {
            Lock lock = new ReentrantLock();
            map.put(id, lock);
            lock.lock();
            return true;
        }
    }

    public void lock(int id) {
        Lock lock = null;

        synchronized (this) {
            lock = map.get(id);
            if (lock == null) {
                lock = new ReentrantLock();
                map.put(id, lock);
            }
        }

        lock.lock();
    }

    public synchronized void unlock(int id) {
        Lock lock = map.remove(id);
        if (lock != null) {
            lock.unlock();
        }
    }

    public <T> T lockedScope(int id, Scope<T> scope) throws NotAuthorizedException {
        try {
            lock(id);
            return scope.run();
        } finally {
            unlock(id);
        }
    }

    public void lockedScope(int id, VoidScope scope) throws NotAuthorizedException {
        try {
            lock(id);
            scope.run();
        } finally {
            unlock(id);
        }
    }

    public static interface Scope<T> {
        public T run() throws NotAuthorizedException;
    }

    public static interface VoidScope {
        public void run() throws NotAuthorizedException;
    }
}
