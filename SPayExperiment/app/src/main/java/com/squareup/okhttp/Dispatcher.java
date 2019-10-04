/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.AssertionError
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.util.ArrayDeque
 *  java.util.Deque
 *  java.util.Iterator
 *  java.util.concurrent.BlockingQueue
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.SynchronousQueue
 *  java.util.concurrent.ThreadFactory
 *  java.util.concurrent.ThreadPoolExecutor
 *  java.util.concurrent.TimeUnit
 */
package com.squareup.okhttp;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.HttpEngine;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class Dispatcher {
    private final Deque<Call> executedCalls = new ArrayDeque();
    private ExecutorService executorService;
    private int maxRequests = 64;
    private int maxRequestsPerHost = 5;
    private final Deque<Call.AsyncCall> readyCalls = new ArrayDeque();
    private final Deque<Call.AsyncCall> runningCalls = new ArrayDeque();

    public Dispatcher() {
    }

    public Dispatcher(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void promoteCalls() {
        if (this.runningCalls.size() >= this.maxRequests) {
            return;
        }
        if (this.readyCalls.isEmpty()) return;
        Iterator iterator = this.readyCalls.iterator();
        do {
            if (!iterator.hasNext()) return;
            Call.AsyncCall asyncCall = (Call.AsyncCall)iterator.next();
            if (this.runningCallsForHost(asyncCall) >= this.maxRequestsPerHost) continue;
            iterator.remove();
            this.runningCalls.add((Object)asyncCall);
            this.getExecutorService().execute((Runnable)asyncCall);
        } while (this.runningCalls.size() < this.maxRequests);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int runningCallsForHost(Call.AsyncCall asyncCall) {
        Iterator iterator = this.runningCalls.iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            int n3 = ((Call.AsyncCall)iterator.next()).host().equals((Object)asyncCall.host()) ? n2 + 1 : n2;
            n2 = n3;
        }
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void cancel(Object object) {
        Dispatcher dispatcher = this;
        synchronized (dispatcher) {
            for (Call.AsyncCall asyncCall : this.readyCalls) {
                if (!Util.equal(object, asyncCall.tag())) continue;
                asyncCall.cancel();
            }
            for (Call.AsyncCall asyncCall : this.runningCalls) {
                if (!Util.equal(object, asyncCall.tag())) continue;
                asyncCall.get().canceled = true;
                HttpEngine httpEngine = asyncCall.get().engine;
                if (httpEngine == null) continue;
                httpEngine.disconnect();
            }
            Iterator iterator = this.executedCalls.iterator();
            while (iterator.hasNext()) {
                Call call = (Call)iterator.next();
                if (!Util.equal(object, call.tag())) continue;
                call.cancel();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void enqueue(Call.AsyncCall asyncCall) {
        Dispatcher dispatcher = this;
        synchronized (dispatcher) {
            if (this.runningCalls.size() < this.maxRequests && this.runningCallsForHost(asyncCall) < this.maxRequestsPerHost) {
                this.runningCalls.add((Object)asyncCall);
                this.getExecutorService().execute((Runnable)asyncCall);
            } else {
                this.readyCalls.add((Object)asyncCall);
            }
            return;
        }
    }

    void executed(Call call) {
        Dispatcher dispatcher = this;
        synchronized (dispatcher) {
            this.executedCalls.add((Object)call);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void finished(Call.AsyncCall asyncCall) {
        Dispatcher dispatcher = this;
        synchronized (dispatcher) {
            if (!this.runningCalls.remove((Object)asyncCall)) {
                throw new AssertionError((Object)"AsyncCall wasn't running!");
            }
            this.promoteCalls();
            return;
        }
    }

    void finished(Call call) {
        Dispatcher dispatcher = this;
        synchronized (dispatcher) {
            if (!this.executedCalls.remove((Object)call)) {
                throw new AssertionError((Object)"Call wasn't in-flight!");
            }
            return;
        }
    }

    public ExecutorService getExecutorService() {
        Dispatcher dispatcher = this;
        synchronized (dispatcher) {
            if (this.executorService == null) {
                this.executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, (BlockingQueue)new SynchronousQueue(), Util.threadFactory("OkHttp Dispatcher", false));
            }
            ExecutorService executorService = this.executorService;
            return executorService;
        }
    }

    public int getMaxRequests() {
        Dispatcher dispatcher = this;
        synchronized (dispatcher) {
            int n2 = this.maxRequests;
            return n2;
        }
    }

    public int getMaxRequestsPerHost() {
        Dispatcher dispatcher = this;
        synchronized (dispatcher) {
            int n2 = this.maxRequestsPerHost;
            return n2;
        }
    }

    public int getQueuedCallCount() {
        Dispatcher dispatcher = this;
        synchronized (dispatcher) {
            int n2 = this.readyCalls.size();
            return n2;
        }
    }

    public int getRunningCallCount() {
        Dispatcher dispatcher = this;
        synchronized (dispatcher) {
            int n2 = this.runningCalls.size();
            return n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setMaxRequests(int n2) {
        Dispatcher dispatcher = this;
        synchronized (dispatcher) {
            if (n2 < 1) {
                throw new IllegalArgumentException("max < 1: " + n2);
            }
            this.maxRequests = n2;
            this.promoteCalls();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setMaxRequestsPerHost(int n2) {
        Dispatcher dispatcher = this;
        synchronized (dispatcher) {
            if (n2 < 1) {
                throw new IllegalArgumentException("max < 1: " + n2);
            }
            this.maxRequestsPerHost = n2;
            this.promoteCalls();
            return;
        }
    }
}

