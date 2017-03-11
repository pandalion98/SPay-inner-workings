package com.squareup.okhttp;

import com.squareup.okhttp.internal.Platform;
import com.squareup.okhttp.internal.Util;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ConnectionPool {
    private static final long DEFAULT_KEEP_ALIVE_DURATION_MS = 300000;
    private static final ConnectionPool systemDefault;
    private final LinkedList<Connection> connections;
    private final Runnable connectionsCleanupRunnable;
    private Executor executor;
    private final long keepAliveDurationNs;
    private final int maxIdleConnections;

    /* renamed from: com.squareup.okhttp.ConnectionPool.1 */
    class C06281 implements Runnable {
        C06281() {
        }

        public void run() {
            ConnectionPool.this.runCleanupUntilPoolIsEmpty();
        }
    }

    static {
        String property = System.getProperty("http.keepAlive");
        String property2 = System.getProperty("http.keepAliveDuration");
        String property3 = System.getProperty("http.maxConnections");
        long parseLong = property2 != null ? Long.parseLong(property2) : DEFAULT_KEEP_ALIVE_DURATION_MS;
        if (property != null && !Boolean.parseBoolean(property)) {
            systemDefault = new ConnectionPool(0, parseLong);
        } else if (property3 != null) {
            systemDefault = new ConnectionPool(Integer.parseInt(property3), parseLong);
        } else {
            systemDefault = new ConnectionPool(5, parseLong);
        }
    }

    public ConnectionPool(int i, long j) {
        this.connections = new LinkedList();
        this.executor = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory("OkHttp ConnectionPool", true));
        this.connectionsCleanupRunnable = new C06281();
        this.maxIdleConnections = i;
        this.keepAliveDurationNs = (j * 1000) * 1000;
    }

    public static ConnectionPool getDefault() {
        return systemDefault;
    }

    public synchronized int getConnectionCount() {
        return this.connections.size();
    }

    @Deprecated
    public synchronized int getSpdyConnectionCount() {
        return getMultiplexedConnectionCount();
    }

    public synchronized int getMultiplexedConnectionCount() {
        int i;
        i = 0;
        Iterator it = this.connections.iterator();
        while (it.hasNext()) {
            int i2;
            if (((Connection) it.next()).isSpdy()) {
                i2 = i + 1;
            } else {
                i2 = i;
            }
            i = i2;
        }
        return i;
    }

    public synchronized int getHttpConnectionCount() {
        return this.connections.size() - getMultiplexedConnectionCount();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized com.squareup.okhttp.Connection get(com.squareup.okhttp.Address r9) {
        /*
        r8 = this;
        monitor-enter(r8);
        r2 = 0;
        r0 = r8.connections;	 Catch:{ all -> 0x0083 }
        r1 = r8.connections;	 Catch:{ all -> 0x0083 }
        r1 = r1.size();	 Catch:{ all -> 0x0083 }
        r3 = r0.listIterator(r1);	 Catch:{ all -> 0x0083 }
    L_0x000e:
        r0 = r3.hasPrevious();	 Catch:{ all -> 0x0083 }
        if (r0 == 0) goto L_0x0086;
    L_0x0014:
        r0 = r3.previous();	 Catch:{ all -> 0x0083 }
        r0 = (com.squareup.okhttp.Connection) r0;	 Catch:{ all -> 0x0083 }
        r1 = r0.getRoute();	 Catch:{ all -> 0x0083 }
        r1 = r1.getAddress();	 Catch:{ all -> 0x0083 }
        r1 = r1.equals(r9);	 Catch:{ all -> 0x0083 }
        if (r1 == 0) goto L_0x000e;
    L_0x0028:
        r1 = r0.isAlive();	 Catch:{ all -> 0x0083 }
        if (r1 == 0) goto L_0x000e;
    L_0x002e:
        r4 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0083 }
        r6 = r0.getIdleStartTimeNs();	 Catch:{ all -> 0x0083 }
        r4 = r4 - r6;
        r6 = r8.keepAliveDurationNs;	 Catch:{ all -> 0x0083 }
        r1 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r1 >= 0) goto L_0x000e;
    L_0x003d:
        r3.remove();	 Catch:{ all -> 0x0083 }
        r1 = r0.isSpdy();	 Catch:{ all -> 0x0083 }
        if (r1 != 0) goto L_0x0051;
    L_0x0046:
        r1 = com.squareup.okhttp.internal.Platform.get();	 Catch:{ SocketException -> 0x0060 }
        r4 = r0.getSocket();	 Catch:{ SocketException -> 0x0060 }
        r1.tagSocket(r4);	 Catch:{ SocketException -> 0x0060 }
    L_0x0051:
        if (r0 == 0) goto L_0x005e;
    L_0x0053:
        r1 = r0.isSpdy();	 Catch:{ all -> 0x0083 }
        if (r1 == 0) goto L_0x005e;
    L_0x0059:
        r1 = r8.connections;	 Catch:{ all -> 0x0083 }
        r1.addFirst(r0);	 Catch:{ all -> 0x0083 }
    L_0x005e:
        monitor-exit(r8);
        return r0;
    L_0x0060:
        r1 = move-exception;
        r0 = r0.getSocket();	 Catch:{ all -> 0x0083 }
        com.squareup.okhttp.internal.Util.closeQuietly(r0);	 Catch:{ all -> 0x0083 }
        r0 = com.squareup.okhttp.internal.Platform.get();	 Catch:{ all -> 0x0083 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0083 }
        r4.<init>();	 Catch:{ all -> 0x0083 }
        r5 = "Unable to tagSocket(): ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0083 }
        r1 = r4.append(r1);	 Catch:{ all -> 0x0083 }
        r1 = r1.toString();	 Catch:{ all -> 0x0083 }
        r0.logW(r1);	 Catch:{ all -> 0x0083 }
        goto L_0x000e;
    L_0x0083:
        r0 = move-exception;
        monitor-exit(r8);
        throw r0;
    L_0x0086:
        r0 = r2;
        goto L_0x0051;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.ConnectionPool.get(com.squareup.okhttp.Address):com.squareup.okhttp.Connection");
    }

    void recycle(Connection connection) {
        if (connection.isSpdy() || !connection.clearOwner()) {
            return;
        }
        if (connection.isAlive()) {
            try {
                Platform.get().untagSocket(connection.getSocket());
                synchronized (this) {
                    addConnection(connection);
                    connection.incrementRecycleCount();
                    connection.resetIdleStartTime();
                }
                return;
            } catch (SocketException e) {
                Platform.get().logW("Unable to untagSocket(): " + e);
                Util.closeQuietly(connection.getSocket());
                return;
            }
        }
        Util.closeQuietly(connection.getSocket());
    }

    private void addConnection(Connection connection) {
        boolean isEmpty = this.connections.isEmpty();
        this.connections.addFirst(connection);
        if (isEmpty) {
            this.executor.execute(this.connectionsCleanupRunnable);
        } else {
            notifyAll();
        }
    }

    void share(Connection connection) {
        if (!connection.isSpdy()) {
            throw new IllegalArgumentException();
        } else if (connection.isAlive()) {
            synchronized (this) {
                addConnection(connection);
            }
        }
    }

    public void evictAll() {
        List arrayList;
        synchronized (this) {
            arrayList = new ArrayList(this.connections);
            this.connections.clear();
            notifyAll();
        }
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            Util.closeQuietly(((Connection) arrayList.get(i)).getSocket());
        }
    }

    private void runCleanupUntilPoolIsEmpty() {
        do {
        } while (performCleanup());
    }

    boolean performCleanup() {
        synchronized (this) {
            if (this.connections.isEmpty()) {
                return false;
            }
            int i;
            List arrayList = new ArrayList();
            int i2 = 0;
            long nanoTime = System.nanoTime();
            long j = this.keepAliveDurationNs;
            ListIterator listIterator = this.connections.listIterator(this.connections.size());
            while (listIterator.hasPrevious()) {
                long j2;
                Connection connection = (Connection) listIterator.previous();
                long idleStartTimeNs = (connection.getIdleStartTimeNs() + this.keepAliveDurationNs) - nanoTime;
                long j3;
                if (idleStartTimeNs <= 0 || !connection.isAlive()) {
                    listIterator.remove();
                    arrayList.add(connection);
                    j3 = j;
                    i = i2;
                    j2 = j3;
                } else if (connection.isIdle()) {
                    int i3 = i2 + 1;
                    j2 = Math.min(j, idleStartTimeNs);
                    i = i3;
                } else {
                    j3 = j;
                    i = i2;
                    j2 = j3;
                }
                i2 = i;
                j = j2;
            }
            ListIterator listIterator2 = this.connections.listIterator(this.connections.size());
            while (listIterator2.hasPrevious() && i2 > this.maxIdleConnections) {
                int i4;
                connection = (Connection) listIterator2.previous();
                if (connection.isIdle()) {
                    arrayList.add(connection);
                    listIterator2.remove();
                    i4 = i2 - 1;
                } else {
                    i4 = i2;
                }
                i2 = i4;
            }
            if (arrayList.isEmpty()) {
                try {
                    j2 = j / 1000000;
                    wait(j2, (int) (j - (1000000 * j2)));
                    return true;
                } catch (InterruptedException e) {
                }
            }
            i = arrayList.size();
            for (i2 = 0; i2 < i; i2++) {
                Util.closeQuietly(((Connection) arrayList.get(i2)).getSocket());
            }
            return true;
        }
    }

    void replaceCleanupExecutorForTests(Executor executor) {
        this.executor = executor;
    }

    synchronized List<Connection> getConnections() {
        return new ArrayList(this.connections);
    }
}
