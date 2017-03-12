package com.samsung.android.spayfw.appinterface;

public abstract class ConnectionCallback implements PaymentFrameworkConnection {
    public abstract void onError(String str, int i);
}
