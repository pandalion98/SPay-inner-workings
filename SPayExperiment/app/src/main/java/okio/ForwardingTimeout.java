/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 *  java.util.concurrent.TimeUnit
 */
package okio;

import java.util.concurrent.TimeUnit;
import okio.Timeout;

public class ForwardingTimeout
extends Timeout {
    private Timeout delegate;

    public ForwardingTimeout(Timeout timeout) {
        if (timeout == null) {
            throw new IllegalArgumentException("delegate == null");
        }
        this.delegate = timeout;
    }

    @Override
    public Timeout clearDeadline() {
        return this.delegate.clearDeadline();
    }

    @Override
    public Timeout clearTimeout() {
        return this.delegate.clearTimeout();
    }

    @Override
    public long deadlineNanoTime() {
        return this.delegate.deadlineNanoTime();
    }

    @Override
    public Timeout deadlineNanoTime(long l2) {
        return this.delegate.deadlineNanoTime(l2);
    }

    public final Timeout delegate() {
        return this.delegate;
    }

    @Override
    public boolean hasDeadline() {
        return this.delegate.hasDeadline();
    }

    public final ForwardingTimeout setDelegate(Timeout timeout) {
        if (timeout == null) {
            throw new IllegalArgumentException("delegate == null");
        }
        this.delegate = timeout;
        return this;
    }

    @Override
    public void throwIfReached() {
        this.delegate.throwIfReached();
    }

    @Override
    public Timeout timeout(long l2, TimeUnit timeUnit) {
        return this.delegate.timeout(l2, timeUnit);
    }

    @Override
    public long timeoutNanos() {
        return this.delegate.timeoutNanos();
    }
}

