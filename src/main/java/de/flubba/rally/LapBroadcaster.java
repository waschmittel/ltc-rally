package de.flubba.rally;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.flubba.rally.entity.Runner;

public final class LapBroadcaster {
    public interface LapBroadcastListener {
        void receiveBroadcast(Runner runner, long lapTime);
    }

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static final List<LapBroadcastListener> listeners = new CopyOnWriteArrayList<>();

    public static void broadcast(final Runner runner, final long lapTime) {
        for (final LapBroadcastListener listener : listeners) {
            executorService.execute(() -> listener.receiveBroadcast(runner, lapTime));
        }
    }

    public static void register(LapBroadcastListener listener) {
        listeners.add(listener);
    }

    public static void unregister(LapBroadcastListener listener) {
        listeners.remove(listener);
    }

    private LapBroadcaster() {
    }

}