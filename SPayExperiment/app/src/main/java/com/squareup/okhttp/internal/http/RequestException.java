/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.Throwable
 */
package com.squareup.okhttp.internal.http;

import java.io.IOException;

public final class RequestException
extends Exception {
    public RequestException(IOException iOException) {
        super((Throwable)iOException);
    }

    public IOException getCause() {
        return (IOException)super.getCause();
    }
}

