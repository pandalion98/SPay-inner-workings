/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.Closeable
 *  java.lang.Object
 */
package okio;

import java.io.Closeable;
import okio.Buffer;
import okio.Timeout;

public interface Source
extends Closeable {
    public void close();

    public long read(Buffer var1, long var2);

    public Timeout timeout();
}

