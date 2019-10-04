/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.Deprecated
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.InterruptedException
 *  java.lang.Long
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.System
 *  java.net.Socket
 *  java.net.SocketException
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.LinkedList
 *  java.util.List
 *  java.util.ListIterator
 *  java.util.concurrent.BlockingQueue
 *  java.util.concurrent.Executor
 *  java.util.concurrent.LinkedBlockingQueue
 *  java.util.concurrent.ThreadFactory
 *  java.util.concurrent.ThreadPoolExecutor
 *  java.util.concurrent.TimeUnit
 */
package com.squareup.okhttp;

import com.squareup.okhttp.Address;
import com.squareup.okhttp.Connection;
import com.squareup.okhttp.Route;
import com.squareup.okhttp.internal.Platform;
import com.squareup.okhttp.internal.Util;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ConnectionPool {
    private static final long DEFAULT_KEEP_ALIVE_DURATION_MS = 300000L;
    private static final ConnectionPool systemDefault;
    private final LinkedList<Connection> connections = new LinkedList();
    private final Runnable connectionsCleanupRunnable = new Runnable(){

        public void run() {
            ConnectionPool.this.runCleanupUntilPoolIsEmpty();
        }
    };
    private Executor executor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, (BlockingQueue)new LinkedBlockingQueue(), Util.threadFactory("OkHttp ConnectionPool", true));
    private final long keepAliveDurationNs;
    private final int maxIdleConnections;

    /*
     * Enabled aggressive block sorting
     */
    static {
        String string = System.getProperty((String)"http.keepAlive");
        String string2 = System.getProperty((String)"http.keepAliveDuration");
        String string3 = System.getProperty((String)"http.maxConnections");
        long l2 = string2 != null ? Long.parseLong((String)string2) : 300000L;
        if (string != null && !Boolean.parseBoolean((String)string)) {
            systemDefault = new ConnectionPool(0, l2);
            return;
        }
        if (string3 != null) {
            systemDefault = new ConnectionPool(Integer.parseInt((String)string3), l2);
            return;
        }
        systemDefault = new ConnectionPool(5, l2);
    }

    public ConnectionPool(int n2, long l2) {
        this.maxIdleConnections = n2;
        this.keepAliveDurationNs = 1000L * (l2 * 1000L);
    }

    private void addConnection(Connection connection) {
        boolean bl = this.connections.isEmpty();
        this.connections.addFirst((Object)connection);
        if (bl) {
            this.executor.execute(this.connectionsCleanupRunnable);
            return;
        }
        this.notifyAll();
    }

    public static ConnectionPool getDefault() {
        return systemDefault;
    }

    private void runCleanupUntilPoolIsEmpty() {
        while (this.performCleanup()) {
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void evictAll() {
        ArrayList arrayList;
        ConnectionPool connectionPool = this;
        synchronized (connectionPool) {
            arrayList = new ArrayList(this.connections);
            this.connections.clear();
            this.notifyAll();
        }
        int n2 = arrayList.size();
        int n3 = 0;
        while (n3 < n2) {
            Util.closeQuietly(((Connection)arrayList.get(n3)).getSocket());
            ++n3;
        }
        return;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Connection get(Address address) {
        ConnectionPool connectionPool = this;
        synchronized (connectionPool) {
            Connection connection;
            block5 : {
                ListIterator listIterator = this.connections.listIterator(this.connections.size());
                while (listIterator.hasPrevious()) {
                    connection = (Connection)listIterator.previous();
                    if (!connection.getRoute().getAddress().equals(address) || !connection.isAlive() || System.nanoTime() - connection.getIdleStartTimeNs() >= this.keepAliveDurationNs) continue;
                    listIterator.remove();
                    boolean bl = connection.isSpdy();
                    if (bl) break block5;
                    try {
                        Platform.get().tagSocket(connection.getSocket());
                        break block5;
                    }
                    catch (SocketException socketException) {
                        Util.closeQuietly(connection.getSocket());
                        Platform.get().logW("Unable to tagSocket(): " + (Object)((Object)socketException));
                    }
                }
                return null;
            }
            if (connection == null) return connection;
            if (!connection.isSpdy()) return connection;
            this.connections.addFirst((Object)connection);
            return connection;
        }
    }

    public int getConnectionCount() {
        ConnectionPool connectionPool = this;
        synchronized (connectionPool) {
            int n2 = this.connections.size();
            return n2;
        }
    }

    List<Connection> getConnections() {
        ConnectionPool connectionPool = this;
        synchronized (connectionPool) {
            ArrayList arrayList = new ArrayList(this.connections);
            return arrayList;
        }
    }

    public int getHttpConnectionCount() {
        ConnectionPool connectionPool = this;
        synchronized (connectionPool) {
            int n2 = this.connections.size();
            int n3 = this.getMultiplexedConnectionCount();
            int n4 = n2 - n3;
            return n4;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getMultiplexedConnectionCount() {
        ConnectionPool connectionPool = this;
        synchronized (connectionPool) {
            int n2 = 0;
            Iterator iterator = this.connections.iterator();
            while (iterator.hasNext()) {
                boolean bl = ((Connection)iterator.next()).isSpdy();
                int n3 = bl ? n2 + 1 : n2;
                n2 = n3;
            }
            return n2;
        }
    }

    @Deprecated
    public int getSpdyConnectionCount() {
        ConnectionPool connectionPool = this;
        synchronized (connectionPool) {
            int n2 = this.getMultiplexedConnectionCount();
            return n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    boolean performCleanup() {
        ConnectionPool connectionPool = this;
        synchronized (connectionPool) {
            if (this.connections.isEmpty()) {
                return false;
            }
            ArrayList arrayList = new ArrayList();
            int n2 = 0;
            long l2 = System.nanoTime();
            long l3 = this.keepAliveDurationNs;
            ListIterator listIterator = this.connections.listIterator(this.connections.size());
            do {
                long l4;
                int n3;
                if (!listIterator.hasPrevious()) break;
                Connection connection = (Connection)listIterator.previous();
                long l5 = connection.getIdleStartTimeNs() + this.keepAliveDurationNs - l2;
                if (l5 <= 0L || !connection.isAlive()) {
                    listIterator.remove();
                    arrayList.add((Object)connection);
                    long l6 = l3;
                    n3 = n2;
                    l4 = l6;
                } else if (connection.isIdle()) {
                    int n4 = n2 + 1;
                    l4 = Math.min((long)l3, (long)l5);
                    n3 = n4;
                } else {
                    long l7 = l3;
                    n3 = n2;
                    l4 = l7;
                }
                long l8 = l4;
                n2 = n3;
                l3 = l8;
            } while (true);
            ListIterator listIterator2 = this.connections.listIterator(this.connections.size());
            do {
                int n5;
                block18 : {
                    block17 : {
                        block16 : {
                            if (!listIterator2.hasPrevious() || n2 <= this.maxIdleConnections) break block16;
                            Connection connection = (Connection)listIterator2.previous();
                            if (!connection.isIdle()) break block17;
                            arrayList.add((Object)connection);
                            listIterator2.remove();
                            n5 = n2 - 1;
                            break block18;
                        }
                        boolean bl = arrayList.isEmpty();
                        if (bl) {
                            try {
                                long l9 = l3 / 1000000L;
                                this.wait(l9, (int)(l3 - 1000000L * l9));
                                return true;
                            }
                            catch (InterruptedException interruptedException) {
                                // empty catch block
                            }
                        }
                        // MONITOREXIT [4, 7] lbl52 : w: MONITOREXIT : var33_1
                        int n6 = arrayList.size();
                        int n7 = 0;
                        do {
                            if (n7 >= n6) {
                                return true;
                            }
                            Util.closeQuietly(((Connection)arrayList.get(n7)).getSocket());
                            ++n7;
                        } while (true);
                    }
                    n5 = n2;
                }
                n2 = n5;
            } while (true);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void recycle(Connection connection) {
        ConnectionPool connectionPool;
        if (connection.isSpdy() || !connection.clearOwner()) {
            return;
        }
        if (!connection.isAlive()) {
            Util.closeQuietly(connection.getSocket());
            return;
        }
        try {
            Platform.get().untagSocket(connection.getSocket());
            connectionPool = this;
        }
        catch (SocketException socketException) {
            Platform.get().logW("Unable to untagSocket(): " + (Object)((Object)socketException));
            Util.closeQuietly(connection.getSocket());
            return;
        }
        synchronized (connectionPool) {
            this.addConnection(connection);
            connection.incrementRecycleCount();
            connection.resetIdleStartTime();
            return;
        }
    }

    void replaceCleanupExecutorForTests(Executor executor) {
        this.executor = executor;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void share(Connection connection) {
        if (!connection.isSpdy()) {
            throw new IllegalArgumentException();
        }
        if (!connection.isAlive()) {
            return;
        }
        ConnectionPool connectionPool = this;
        synchronized (connectionPool) {
            this.addConnection(connection);
            return;
        }
    }

}

