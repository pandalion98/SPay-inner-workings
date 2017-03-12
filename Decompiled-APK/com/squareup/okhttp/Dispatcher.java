package com.squareup.okhttp;

import com.samsung.android.spayfw.cncc.CNCCCommands;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.HttpEngine;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class Dispatcher {
    private final Deque<Call> executedCalls;
    private ExecutorService executorService;
    private int maxRequests;
    private int maxRequestsPerHost;
    private final Deque<AsyncCall> readyCalls;
    private final Deque<AsyncCall> runningCalls;

    public Dispatcher(ExecutorService executorService) {
        this.maxRequests = 64;
        this.maxRequestsPerHost = 5;
        this.readyCalls = new ArrayDeque();
        this.runningCalls = new ArrayDeque();
        this.executedCalls = new ArrayDeque();
        this.executorService = executorService;
    }

    public Dispatcher() {
        this.maxRequests = 64;
        this.maxRequestsPerHost = 5;
        this.readyCalls = new ArrayDeque();
        this.runningCalls = new ArrayDeque();
        this.executedCalls = new ArrayDeque();
    }

    public synchronized ExecutorService getExecutorService() {
        if (this.executorService == null) {
            this.executorService = new ThreadPoolExecutor(0, CNCCCommands.CMD_CNCC_CMD_UNKNOWN, 60, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp Dispatcher", false));
        }
        return this.executorService;
    }

    public synchronized void setMaxRequests(int i) {
        if (i < 1) {
            throw new IllegalArgumentException("max < 1: " + i);
        }
        this.maxRequests = i;
        promoteCalls();
    }

    public synchronized int getMaxRequests() {
        return this.maxRequests;
    }

    public synchronized void setMaxRequestsPerHost(int i) {
        if (i < 1) {
            throw new IllegalArgumentException("max < 1: " + i);
        }
        this.maxRequestsPerHost = i;
        promoteCalls();
    }

    public synchronized int getMaxRequestsPerHost() {
        return this.maxRequestsPerHost;
    }

    synchronized void enqueue(AsyncCall asyncCall) {
        if (this.runningCalls.size() >= this.maxRequests || runningCallsForHost(asyncCall) >= this.maxRequestsPerHost) {
            this.readyCalls.add(asyncCall);
        } else {
            this.runningCalls.add(asyncCall);
            getExecutorService().execute(asyncCall);
        }
    }

    public synchronized void cancel(Object obj) {
        for (AsyncCall asyncCall : this.readyCalls) {
            if (Util.equal(obj, asyncCall.tag())) {
                asyncCall.cancel();
            }
        }
        for (AsyncCall asyncCall2 : this.runningCalls) {
            if (Util.equal(obj, asyncCall2.tag())) {
                asyncCall2.get().canceled = true;
                HttpEngine httpEngine = asyncCall2.get().engine;
                if (httpEngine != null) {
                    httpEngine.disconnect();
                }
            }
        }
        for (Call call : this.executedCalls) {
            if (Util.equal(obj, call.tag())) {
                call.cancel();
            }
        }
    }

    synchronized void finished(AsyncCall asyncCall) {
        if (this.runningCalls.remove(asyncCall)) {
            promoteCalls();
        } else {
            throw new AssertionError("AsyncCall wasn't running!");
        }
    }

    private void promoteCalls() {
        if (this.runningCalls.size() < this.maxRequests && !this.readyCalls.isEmpty()) {
            Iterator it = this.readyCalls.iterator();
            while (it.hasNext()) {
                AsyncCall asyncCall = (AsyncCall) it.next();
                if (runningCallsForHost(asyncCall) < this.maxRequestsPerHost) {
                    it.remove();
                    this.runningCalls.add(asyncCall);
                    getExecutorService().execute(asyncCall);
                }
                if (this.runningCalls.size() >= this.maxRequests) {
                    return;
                }
            }
        }
    }

    private int runningCallsForHost(AsyncCall asyncCall) {
        int i = 0;
        for (AsyncCall host : this.runningCalls) {
            int i2;
            if (host.host().equals(asyncCall.host())) {
                i2 = i + 1;
            } else {
                i2 = i;
            }
            i = i2;
        }
        return i;
    }

    synchronized void executed(Call call) {
        this.executedCalls.add(call);
    }

    synchronized void finished(Call call) {
        if (!this.executedCalls.remove(call)) {
            throw new AssertionError("Call wasn't in-flight!");
        }
    }

    public synchronized int getRunningCallCount() {
        return this.runningCalls.size();
    }

    public synchronized int getQueuedCallCount() {
        return this.readyCalls.size();
    }
}
