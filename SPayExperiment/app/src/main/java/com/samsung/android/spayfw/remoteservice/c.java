/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice;

import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;

public class c<T> {
    private ErrorResponseData AG;
    private String AH;
    private T result;
    private int statusCode;

    public c(ErrorResponseData errorResponseData, T t2, int n2) {
        this.AG = errorResponseData;
        this.result = t2;
        this.statusCode = n2;
    }

    public void bh(String string) {
        this.AH = string;
    }

    public ErrorResponseData fa() {
        return this.AG;
    }

    public T getResult() {
        return this.result;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}

