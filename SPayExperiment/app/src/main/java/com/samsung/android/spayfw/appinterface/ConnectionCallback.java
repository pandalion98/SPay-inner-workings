/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;

public abstract class ConnectionCallback
implements PaymentFrameworkConnection {
    public abstract void onError(String var1, int var2);
}

