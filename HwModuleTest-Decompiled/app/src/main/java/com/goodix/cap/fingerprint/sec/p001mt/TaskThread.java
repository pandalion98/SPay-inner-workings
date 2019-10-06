package com.goodix.cap.fingerprint.sec.p001mt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* renamed from: com.goodix.cap.fingerprint.sec.mt.TaskThread */
public class TaskThread {
    private static final int MAX_THREAD = 8;
    private static final String TAG = "TaskThread";
    private ExecutorService mThreadPool = Executors.newSingleThreadExecutor();

    public void runTestThread(final TaskCommon commonTask) {
        this.mThreadPool.execute(new Runnable() {
            public void run() {
                Common.LOG_D(TaskThread.TAG, "runTestThread.");
                commonTask.onStart();
                commonTask.onDone();
            }
        });
    }

    public void resumeThreadPool() {
        this.mThreadPool = Executors.newSingleThreadExecutor();
    }

    public boolean isShutdown() {
        return this.mThreadPool.isShutdown();
    }

    public void stopTask() {
        this.mThreadPool.shutdown();
    }
}
