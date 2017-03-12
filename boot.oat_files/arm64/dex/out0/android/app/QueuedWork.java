package android.app;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QueuedWork {
    private static final ConcurrentLinkedQueue<Runnable> sPendingWorkFinishers = new ConcurrentLinkedQueue();
    private static ExecutorService sSingleThreadExecutor = null;

    public static ExecutorService singleThreadExecutor() {
        ExecutorService executorService;
        synchronized (QueuedWork.class) {
            if (sSingleThreadExecutor == null) {
                sSingleThreadExecutor = Executors.newSingleThreadExecutor();
            }
            executorService = sSingleThreadExecutor;
        }
        return executorService;
    }

    public static void add(Runnable finisher) {
        sPendingWorkFinishers.add(finisher);
    }

    public static void remove(Runnable finisher) {
        sPendingWorkFinishers.remove(finisher);
    }

    public static void waitToFinish() {
        while (true) {
            Runnable toFinish = (Runnable) sPendingWorkFinishers.poll();
            if (toFinish != null) {
                toFinish.run();
            } else {
                return;
            }
        }
    }

    public static boolean hasPendingWork() {
        return !sPendingWorkFinishers.isEmpty();
    }
}
