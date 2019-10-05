/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.DiscoverApduHandlerState;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

public class a {
    private DiscoverApduHandlerState tD = null;
    private ByteBuffer tE;
    private boolean tF = false;
    private boolean tG = false;

    public a(ByteBuffer byteBuffer) {
        this.tE = byteBuffer;
    }

    public a(ByteBuffer byteBuffer, DiscoverApduHandlerState discoverApduHandlerState) {
        this.tE = byteBuffer;
        this.tD = discoverApduHandlerState;
    }

    public a(short s2) {
        byte[] arrby = new byte[]{(byte)(255 & s2 >> 8), (byte)(s2 & 255)};
        this.tE = new ByteBuffer(arrby);
        Log.d("DCSDK_", "DiscoverApduProcessingResult: " + this.tE.toHexString());
    }

    public void a(DiscoverApduHandlerState discoverApduHandlerState) {
        this.tD = discoverApduHandlerState;
    }

    public DiscoverApduHandlerState dA() {
        return this.tD;
    }

    public ByteBuffer dB() {
        return this.tE;
    }

    public boolean dC() {
        return this.tG;
    }

    public void dD() {
        this.tG = true;
    }
}

