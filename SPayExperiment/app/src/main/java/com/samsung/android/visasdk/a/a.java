/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.a;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class a {
    public String toJson() {
        return new GsonBuilder().excludeFieldsWithModifiers(new int[]{128}).create().toJson((Object)this);
    }

    public String toString() {
        return this.toJson();
    }
}

