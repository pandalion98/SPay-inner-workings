/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.squareup.okhttp;

import com.squareup.okhttp.Connection;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public interface Interceptor {
    public Response intercept(Chain var1);

    public static interface Chain {
        public Connection connection();

        public Response proceed(Request var1);

        public Request request();
    }

}

