/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.System
 *  java.util.concurrent.CountDownLatch
 *  java.util.concurrent.TimeUnit
 */
package com.squareup.okhttp.internal.spdy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class Ping {
    private final CountDownLatch latch = new CountDownLatch(1);
    private long received = -1L;
    private long sent = -1L;

    Ping() {
    }

    void cancel() {
        if (this.received != -1L || this.sent == -1L) {
            throw new IllegalStateException();
        }
        this.received = this.sent - 1L;
        this.latch.countDown();
    }

    void receive() {
        if (this.received != -1L || this.sent == -1L) {
            throw new IllegalStateException();
        }
        this.received = System.nanoTime();
        this.latch.countDown();
    }

    public long roundTripTime() {
        this.latch.await();
        return this.received - this.sent;
    }

    public long roundTripTime(long l2, TimeUnit timeUnit) {
        if (this.latch.await(l2, timeUnit)) {
            return this.received - this.sent;
        }
        return -2L;
    }

    void send() {
        if (this.sent != -1L) {
            throw new IllegalStateException();
        }
        this.sent = System.nanoTime();
    }
}

