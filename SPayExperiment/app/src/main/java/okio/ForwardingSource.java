/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package okio;

import okio.Buffer;
import okio.Source;
import okio.Timeout;

public abstract class ForwardingSource
implements Source {
    private final Source delegate;

    public ForwardingSource(Source source) {
        if (source == null) {
            throw new IllegalArgumentException("delegate == null");
        }
        this.delegate = source;
    }

    @Override
    public void close() {
        this.delegate.close();
    }

    public final Source delegate() {
        return this.delegate;
    }

    @Override
    public long read(Buffer buffer, long l2) {
        return this.delegate.read(buffer, l2);
    }

    @Override
    public Timeout timeout() {
        return this.delegate.timeout();
    }

    public String toString() {
        return this.getClass().getSimpleName() + "(" + this.delegate.toString() + ")";
    }
}

