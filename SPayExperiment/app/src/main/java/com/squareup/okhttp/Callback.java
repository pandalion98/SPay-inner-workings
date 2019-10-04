/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 */
package com.squareup.okhttp;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

public interface Callback {
    public void onFailure(Request var1, IOException var2);

    public void onResponse(Response var1);
}

