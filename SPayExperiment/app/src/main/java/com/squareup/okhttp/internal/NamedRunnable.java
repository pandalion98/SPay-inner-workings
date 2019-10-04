/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.Thread
 */
package com.squareup.okhttp.internal;

public abstract class NamedRunnable
implements Runnable {
    protected final String name;

    public /* varargs */ NamedRunnable(String string, Object ... arrobject) {
        this.name = String.format((String)string, (Object[])arrobject);
    }

    protected abstract void execute();

    public final void run() {
        String string = Thread.currentThread().getName();
        Thread.currentThread().setName(this.name);
        try {
            this.execute();
            return;
        }
        finally {
            Thread.currentThread().setName(string);
        }
    }
}

