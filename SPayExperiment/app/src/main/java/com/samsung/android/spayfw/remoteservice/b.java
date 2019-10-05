/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Set
 */
package com.samsung.android.spayfw.remoteservice;

import android.os.Bundle;

import com.samsung.android.spayfw.b.Log;

public class b
extends Request<String, String, d, b> {
    private Bundle headers;
    private String url;

    protected b(a a2, int n2, String string, Bundle bundle, String string2) {
        super(a2, b.S(n2), string2);
        this.url = string;
        this.headers = bundle;
    }

    private static Client.HttpRequest.RequestMethod S(int n2) {
        switch (n2) {
            default: {
                return Client.HttpRequest.RequestMethod.Ag;
            }
            case 0: {
                return Client.HttpRequest.RequestMethod.Ag;
            }
            case 1: {
                return Client.HttpRequest.RequestMethod.Ah;
            }
            case 2: 
        }
        return Client.HttpRequest.RequestMethod.Ai;
    }

    @Override
    protected /* synthetic */ c b(int n2, String string) {
        return this.c(n2, string);
    }

    protected d c(int n2, String string) {
        String string2 = null;
        if (string != null) {
            Log.d("GenericServerRequest", "Response Bytes = " + string.getBytes());
            Log.d("GenericServerRequest", "Response Bytes Length= " + string.getBytes().length);
            int n3 = string.getBytes().length;
            string2 = null;
            if (n3 > 200000) {
                string2 = this.aT(string);
            }
        }
        return new d(string2, string, n2);
    }

    @Override
    protected String cG() {
        return this.url;
    }

    protected d d(int n2, String string) {
        return new d(null, string, n2);
    }

    @Override
    protected /* synthetic */ c e(int n2, String string) {
        return this.d(n2, string);
    }

    @Override
    protected String getRequestType() {
        return "GenericServerRequest";
    }

    @Override
    protected void init() {
        if (this.headers != null) {
            for (String string : this.headers.keySet()) {
                Object object = this.headers.get(string);
                if (object != null && object instanceof String) {
                    Log.d("GenericServerRequest", "add header : " + string + ", value: " + (String)object);
                    this.addHeader(string, (String)object);
                    continue;
                }
                Log.w("GenericServerRequest", "Cannot add header : " + string);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected String j(Object object) {
        String string;
        Log.d("GenericServerRequest", "Entered getRequestDataString: ");
        try {
            string = (String)object;
        }
        catch (Exception exception) {
            Log.c("GenericServerRequest", exception.getMessage(), exception);
            string = null;
        }
        Log.d("GenericServerRequest", "requestDataString = " + string);
        return string;
    }
}

