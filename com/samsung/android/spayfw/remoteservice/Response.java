package com.samsung.android.spayfw.remoteservice;

import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;

/* renamed from: com.samsung.android.spayfw.remoteservice.c */
public class Response<T> {
    private ErrorResponseData AG;
    private String AH;
    private T result;
    private int statusCode;

    public Response(ErrorResponseData errorResponseData, T t, int i) {
        this.AG = errorResponseData;
        this.result = t;
        this.statusCode = i;
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

    public void bh(String str) {
        this.AH = str;
    }
}
