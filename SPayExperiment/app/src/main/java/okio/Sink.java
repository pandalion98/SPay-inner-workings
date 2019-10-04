/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.Closeable
 *  java.io.Flushable
 *  java.lang.Object
 */
package okio;

import java.io.Closeable;
import java.io.Flushable;
import okio.Buffer;
import okio.Timeout;

public interface Sink
extends Closeable,
Flushable {
    public void close();

    public void flush();

    public Timeout timeout();

    public void write(Buffer var1, long var2);
}

