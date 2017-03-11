package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.DiscoverApduHandlerState;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.data.a */
public class DiscoverApduProcessingResult {
    private DiscoverApduHandlerState tD;
    private ByteBuffer tE;
    private boolean tF;
    private boolean tG;

    public DiscoverApduProcessingResult(ByteBuffer byteBuffer) {
        this.tD = null;
        this.tF = false;
        this.tG = false;
        this.tE = byteBuffer;
    }

    public DiscoverApduProcessingResult(ByteBuffer byteBuffer, DiscoverApduHandlerState discoverApduHandlerState) {
        this.tD = null;
        this.tF = false;
        this.tG = false;
        this.tE = byteBuffer;
        this.tD = discoverApduHandlerState;
    }

    public DiscoverApduProcessingResult(short s) {
        this.tD = null;
        this.tF = false;
        this.tG = false;
        this.tE = new ByteBuffer(new byte[]{(byte) ((s >> 8) & GF2Field.MASK), (byte) (s & GF2Field.MASK)});
        Log.m285d("DCSDK_", "DiscoverApduProcessingResult: " + this.tE.toHexString());
    }

    public DiscoverApduHandlerState dA() {
        return this.tD;
    }

    public void m962a(DiscoverApduHandlerState discoverApduHandlerState) {
        this.tD = discoverApduHandlerState;
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
