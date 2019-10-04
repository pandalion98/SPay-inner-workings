/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 */
package com.squareup.okhttp.internal;

import java.io.IOException;
import okio.Buffer;
import okio.ForwardingSink;
import okio.Sink;

class FaultHidingSink
extends ForwardingSink {
    private boolean hasErrors;

    public FaultHidingSink(Sink sink) {
        super(sink);
    }

    @Override
    public void close() {
        if (this.hasErrors) {
            return;
        }
        try {
            super.close();
            return;
        }
        catch (IOException iOException) {
            this.hasErrors = true;
            this.onException(iOException);
            return;
        }
    }

    @Override
    public void flush() {
        if (this.hasErrors) {
            return;
        }
        try {
            super.flush();
            return;
        }
        catch (IOException iOException) {
            this.hasErrors = true;
            this.onException(iOException);
            return;
        }
    }

    protected void onException(IOException iOException) {
    }

    @Override
    public void write(Buffer buffer, long l2) {
        if (this.hasErrors) {
            buffer.skip(l2);
            return;
        }
        try {
            super.write(buffer, l2);
            return;
        }
        catch (IOException iOException) {
            this.hasErrors = true;
            this.onException(iOException);
            return;
        }
    }
}

