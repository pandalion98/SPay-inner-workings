/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.InterruptedIOException
 *  java.lang.AssertionError
 *  java.lang.Class
 *  java.lang.IllegalStateException
 *  java.lang.InterruptedException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Throwable
 */
package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import okio.Buffer;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public class AsyncTimeout
extends Timeout {
    private static AsyncTimeout head;
    private boolean inQueue;
    private AsyncTimeout next;
    private long timeoutAt;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static AsyncTimeout awaitTimeout() {
        AsyncTimeout asyncTimeout = null;
        Class<AsyncTimeout> class_ = AsyncTimeout.class;
        synchronized (AsyncTimeout.class) {
            AsyncTimeout asyncTimeout2 = AsyncTimeout.head.next;
            if (asyncTimeout2 != null) {
                long l2 = asyncTimeout2.remainingNanos(System.nanoTime());
                if (l2 > 0L) {
                    long l3 = l2 / 1000000L;
                    AsyncTimeout.class.wait(l3, (int)(l2 - 1000000L * l3));
                    return null;
                }
                AsyncTimeout.head.next = asyncTimeout2.next;
                asyncTimeout2.next = null;
                return asyncTimeout2;
            }
            AsyncTimeout.class.wait();
            // ** MonitorExit[var7_1] (shouldn't be in output)
            return asyncTimeout;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean cancelScheduledTimeout(AsyncTimeout asyncTimeout) {
        Class<AsyncTimeout> class_ = AsyncTimeout.class;
        synchronized (AsyncTimeout.class) {
            AsyncTimeout asyncTimeout2 = head;
            while (asyncTimeout2 != null) {
                block6 : {
                    if (asyncTimeout2.next != asyncTimeout) break block6;
                    asyncTimeout2.next = asyncTimeout.next;
                    asyncTimeout.next = null;
                    return false;
                }
                asyncTimeout2 = asyncTimeout2.next;
            }
            return true;
        }
    }

    private long remainingNanos(long l2) {
        return this.timeoutAt - l2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void scheduleTimeout(AsyncTimeout asyncTimeout, long l2, boolean bl) {
        Class<AsyncTimeout> class_ = AsyncTimeout.class;
        synchronized (AsyncTimeout.class) {
            if (head == null) {
                head = new AsyncTimeout();
                new Watchdog().start();
            }
            long l3 = System.nanoTime();
            if (l2 != 0L && bl) {
                asyncTimeout.timeoutAt = l3 + Math.min((long)l2, (long)(asyncTimeout.deadlineNanoTime() - l3));
            } else if (l2 != 0L) {
                long l4;
                asyncTimeout.timeoutAt = l4 = l3 + l2;
            } else {
                if (!bl) {
                    throw new AssertionError();
                }
                asyncTimeout.timeoutAt = asyncTimeout.deadlineNanoTime();
            }
            long l5 = asyncTimeout.remainingNanos(l3);
            AsyncTimeout asyncTimeout2 = head;
            do {
                if (asyncTimeout2.next == null || l5 < asyncTimeout2.next.remainingNanos(l3)) {
                    asyncTimeout.next = asyncTimeout2.next;
                    asyncTimeout2.next = asyncTimeout;
                    if (asyncTimeout2 == head) {
                        AsyncTimeout.class.notify();
                    }
                    // ** MonitorExit[var12_3] (shouldn't be in output)
                    return;
                }
                asyncTimeout2 = asyncTimeout2.next;
            } while (true);
        }
    }

    public final void enter() {
        if (this.inQueue) {
            throw new IllegalStateException("Unbalanced enter/exit");
        }
        long l2 = this.timeoutNanos();
        boolean bl = this.hasDeadline();
        if (l2 == 0L && !bl) {
            return;
        }
        this.inQueue = true;
        AsyncTimeout.scheduleTimeout(this, l2, bl);
    }

    final IOException exit(IOException iOException) {
        if (!this.exit()) {
            return iOException;
        }
        InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
        interruptedIOException.initCause((Throwable)iOException);
        return interruptedIOException;
    }

    final void exit(boolean bl) {
        if (this.exit() && bl) {
            throw new InterruptedIOException("timeout");
        }
    }

    public final boolean exit() {
        if (!this.inQueue) {
            return false;
        }
        this.inQueue = false;
        return AsyncTimeout.cancelScheduledTimeout(this);
    }

    public final Sink sink(final Sink sink) {
        return new Sink(){

            @Override
            public void close() {
                AsyncTimeout.this.enter();
                try {
                    sink.close();
                }
                catch (IOException iOException) {
                    try {
                        throw AsyncTimeout.this.exit(iOException);
                    }
                    catch (Throwable throwable) {
                        AsyncTimeout.this.exit(false);
                        throw throwable;
                    }
                }
                AsyncTimeout.this.exit(true);
                return;
            }

            @Override
            public void flush() {
                AsyncTimeout.this.enter();
                try {
                    sink.flush();
                }
                catch (IOException iOException) {
                    try {
                        throw AsyncTimeout.this.exit(iOException);
                    }
                    catch (Throwable throwable) {
                        AsyncTimeout.this.exit(false);
                        throw throwable;
                    }
                }
                AsyncTimeout.this.exit(true);
                return;
            }

            @Override
            public Timeout timeout() {
                return AsyncTimeout.this;
            }

            public String toString() {
                return "AsyncTimeout.sink(" + sink + ")";
            }

            @Override
            public void write(Buffer buffer, long l2) {
                AsyncTimeout.this.enter();
                try {
                    sink.write(buffer, l2);
                }
                catch (IOException iOException) {
                    try {
                        throw AsyncTimeout.this.exit(iOException);
                    }
                    catch (Throwable throwable) {
                        AsyncTimeout.this.exit(false);
                        throw throwable;
                    }
                }
                AsyncTimeout.this.exit(true);
                return;
            }
        };
    }

    public final Source source(final Source source) {
        return new Source(){

            @Override
            public void close() {
                try {
                    source.close();
                }
                catch (IOException iOException) {
                    try {
                        throw AsyncTimeout.this.exit(iOException);
                    }
                    catch (Throwable throwable) {
                        AsyncTimeout.this.exit(false);
                        throw throwable;
                    }
                }
                AsyncTimeout.this.exit(true);
                return;
            }

            @Override
            public long read(Buffer buffer, long l2) {
                long l3;
                AsyncTimeout.this.enter();
                try {
                    l3 = source.read(buffer, l2);
                }
                catch (IOException iOException) {
                    try {
                        throw AsyncTimeout.this.exit(iOException);
                    }
                    catch (Throwable throwable) {
                        AsyncTimeout.this.exit(false);
                        throw throwable;
                    }
                }
                AsyncTimeout.this.exit(true);
                return l3;
            }

            @Override
            public Timeout timeout() {
                return AsyncTimeout.this;
            }

            public String toString() {
                return "AsyncTimeout.source(" + source + ")";
            }
        };
    }

    protected void timedOut() {
    }

    private static final class Watchdog
    extends Thread {
        public Watchdog() {
            super("Okio Watchdog");
            this.setDaemon(true);
        }

        public void run() {
            do {
                AsyncTimeout asyncTimeout = AsyncTimeout.awaitTimeout();
                if (asyncTimeout == null) continue;
                try {
                    asyncTimeout.timedOut();
                }
                catch (InterruptedException interruptedException) {
                }
            } while (true);
        }
    }

}

