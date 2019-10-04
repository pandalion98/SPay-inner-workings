/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InterruptedIOException
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.util.concurrent.TimeUnit
 */
package okio;

import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;

public class Timeout {
    public static final Timeout NONE = new Timeout(){

        @Override
        public Timeout deadlineNanoTime(long l2) {
            return this;
        }

        @Override
        public void throwIfReached() {
        }

        @Override
        public Timeout timeout(long l2, TimeUnit timeUnit) {
            return this;
        }
    };
    private long deadlineNanoTime;
    private boolean hasDeadline;
    private long timeoutNanos;

    public Timeout clearDeadline() {
        this.hasDeadline = false;
        return this;
    }

    public Timeout clearTimeout() {
        this.timeoutNanos = 0L;
        return this;
    }

    public final Timeout deadline(long l2, TimeUnit timeUnit) {
        if (l2 <= 0L) {
            throw new IllegalArgumentException("duration <= 0: " + l2);
        }
        if (timeUnit == null) {
            throw new IllegalArgumentException("unit == null");
        }
        return this.deadlineNanoTime(System.nanoTime() + timeUnit.toNanos(l2));
    }

    public long deadlineNanoTime() {
        if (!this.hasDeadline) {
            throw new IllegalStateException("No deadline");
        }
        return this.deadlineNanoTime;
    }

    public Timeout deadlineNanoTime(long l2) {
        this.hasDeadline = true;
        this.deadlineNanoTime = l2;
        return this;
    }

    public boolean hasDeadline() {
        return this.hasDeadline;
    }

    public void throwIfReached() {
        if (Thread.interrupted()) {
            throw new InterruptedIOException("thread interrupted");
        }
        if (this.hasDeadline && this.deadlineNanoTime - System.nanoTime() <= 0L) {
            throw new InterruptedIOException("deadline reached");
        }
    }

    public Timeout timeout(long l2, TimeUnit timeUnit) {
        if (l2 < 0L) {
            throw new IllegalArgumentException("timeout < 0: " + l2);
        }
        if (timeUnit == null) {
            throw new IllegalArgumentException("unit == null");
        }
        this.timeoutNanos = timeUnit.toNanos(l2);
        return this;
    }

    public long timeoutNanos() {
        return this.timeoutNanos;
    }

}

