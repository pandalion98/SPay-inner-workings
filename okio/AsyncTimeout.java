package okio;

import java.io.IOException;
import java.io.InterruptedIOException;

public class AsyncTimeout extends Timeout {
    private static AsyncTimeout head;
    private boolean inQueue;
    private AsyncTimeout next;
    private long timeoutAt;

    /* renamed from: okio.AsyncTimeout.1 */
    class C06951 implements Sink {
        final /* synthetic */ Sink val$sink;

        C06951(Sink sink) {
            this.val$sink = sink;
        }

        public void write(Buffer buffer, long j) {
            AsyncTimeout.this.enter();
            try {
                this.val$sink.write(buffer, j);
                AsyncTimeout.this.exit(true);
            } catch (IOException e) {
                throw AsyncTimeout.this.exit(e);
            } catch (Throwable th) {
                AsyncTimeout.this.exit(false);
            }
        }

        public void flush() {
            AsyncTimeout.this.enter();
            try {
                this.val$sink.flush();
                AsyncTimeout.this.exit(true);
            } catch (IOException e) {
                throw AsyncTimeout.this.exit(e);
            } catch (Throwable th) {
                AsyncTimeout.this.exit(false);
            }
        }

        public void close() {
            AsyncTimeout.this.enter();
            try {
                this.val$sink.close();
                AsyncTimeout.this.exit(true);
            } catch (IOException e) {
                throw AsyncTimeout.this.exit(e);
            } catch (Throwable th) {
                AsyncTimeout.this.exit(false);
            }
        }

        public Timeout timeout() {
            return AsyncTimeout.this;
        }

        public String toString() {
            return "AsyncTimeout.sink(" + this.val$sink + ")";
        }
    }

    /* renamed from: okio.AsyncTimeout.2 */
    class C06962 implements Source {
        final /* synthetic */ Source val$source;

        C06962(Source source) {
            this.val$source = source;
        }

        public long read(Buffer buffer, long j) {
            AsyncTimeout.this.enter();
            try {
                long read = this.val$source.read(buffer, j);
                AsyncTimeout.this.exit(true);
                return read;
            } catch (IOException e) {
                throw AsyncTimeout.this.exit(e);
            } catch (Throwable th) {
                AsyncTimeout.this.exit(false);
            }
        }

        public void close() {
            try {
                this.val$source.close();
                AsyncTimeout.this.exit(true);
            } catch (IOException e) {
                throw AsyncTimeout.this.exit(e);
            } catch (Throwable th) {
                AsyncTimeout.this.exit(false);
            }
        }

        public Timeout timeout() {
            return AsyncTimeout.this;
        }

        public String toString() {
            return "AsyncTimeout.source(" + this.val$source + ")";
        }
    }

    private static final class Watchdog extends Thread {
        public Watchdog() {
            super("Okio Watchdog");
            setDaemon(true);
        }

        public void run() {
            while (true) {
                try {
                    AsyncTimeout access$000 = AsyncTimeout.awaitTimeout();
                    if (access$000 != null) {
                        access$000.timedOut();
                    }
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public final void enter() {
        if (this.inQueue) {
            throw new IllegalStateException("Unbalanced enter/exit");
        }
        long timeoutNanos = timeoutNanos();
        boolean hasDeadline = hasDeadline();
        if (timeoutNanos != 0 || hasDeadline) {
            this.inQueue = true;
            scheduleTimeout(this, timeoutNanos, hasDeadline);
        }
    }

    private static synchronized void scheduleTimeout(AsyncTimeout asyncTimeout, long j, boolean z) {
        synchronized (AsyncTimeout.class) {
            if (head == null) {
                head = new AsyncTimeout();
                new Watchdog().start();
            }
            long nanoTime = System.nanoTime();
            if (j != 0 && z) {
                asyncTimeout.timeoutAt = Math.min(j, asyncTimeout.deadlineNanoTime() - nanoTime) + nanoTime;
            } else if (j != 0) {
                asyncTimeout.timeoutAt = nanoTime + j;
            } else if (z) {
                asyncTimeout.timeoutAt = asyncTimeout.deadlineNanoTime();
            } else {
                throw new AssertionError();
            }
            long remainingNanos = asyncTimeout.remainingNanos(nanoTime);
            AsyncTimeout asyncTimeout2 = head;
            while (asyncTimeout2.next != null && remainingNanos >= asyncTimeout2.next.remainingNanos(nanoTime)) {
                asyncTimeout2 = asyncTimeout2.next;
            }
            asyncTimeout.next = asyncTimeout2.next;
            asyncTimeout2.next = asyncTimeout;
            if (asyncTimeout2 == head) {
                AsyncTimeout.class.notify();
            }
        }
    }

    public final boolean exit() {
        if (!this.inQueue) {
            return false;
        }
        this.inQueue = false;
        return cancelScheduledTimeout(this);
    }

    private static synchronized boolean cancelScheduledTimeout(AsyncTimeout asyncTimeout) {
        boolean z;
        synchronized (AsyncTimeout.class) {
            for (AsyncTimeout asyncTimeout2 = head; asyncTimeout2 != null; asyncTimeout2 = asyncTimeout2.next) {
                if (asyncTimeout2.next == asyncTimeout) {
                    asyncTimeout2.next = asyncTimeout.next;
                    asyncTimeout.next = null;
                    z = false;
                    break;
                }
            }
            z = true;
        }
        return z;
    }

    private long remainingNanos(long j) {
        return this.timeoutAt - j;
    }

    protected void timedOut() {
    }

    public final Sink sink(Sink sink) {
        return new C06951(sink);
    }

    public final Source source(Source source) {
        return new C06962(source);
    }

    final void exit(boolean z) {
        if (exit() && z) {
            throw new InterruptedIOException("timeout");
        }
    }

    final IOException exit(IOException iOException) {
        if (!exit()) {
            return iOException;
        }
        IOException interruptedIOException = new InterruptedIOException("timeout");
        interruptedIOException.initCause(iOException);
        return interruptedIOException;
    }

    private static synchronized AsyncTimeout awaitTimeout() {
        AsyncTimeout asyncTimeout = null;
        synchronized (AsyncTimeout.class) {
            AsyncTimeout asyncTimeout2 = head.next;
            if (asyncTimeout2 == null) {
                AsyncTimeout.class.wait();
            } else {
                long remainingNanos = asyncTimeout2.remainingNanos(System.nanoTime());
                if (remainingNanos > 0) {
                    long j = remainingNanos / 1000000;
                    AsyncTimeout.class.wait(j, (int) (remainingNanos - (1000000 * j)));
                } else {
                    head.next = asyncTimeout2.next;
                    asyncTimeout2.next = null;
                    asyncTimeout = asyncTimeout2;
                }
            }
        }
        return asyncTimeout;
    }
}
