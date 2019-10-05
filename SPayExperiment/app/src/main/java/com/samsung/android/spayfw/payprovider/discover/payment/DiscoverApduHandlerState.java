/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover.payment;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.data.e;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public abstract class DiscoverApduHandlerState
extends Enum<DiscoverApduHandlerState> {
    public static final /* enum */ DiscoverApduHandlerState sV = new DiscoverApduHandlerState(){

        @Override
        public List<DiscoverApduHandlerState> cP() {
            DiscoverApduHandlerState[] arrdiscoverApduHandlerState = new DiscoverApduHandlerState[]{sV, sY, ta};
            return Arrays.asList((Object[])arrdiscoverApduHandlerState);
        }

        @Override
        public com.samsung.android.spayfw.payprovider.discover.payment.data.a g(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
            return this.cO();
        }
    };
    public static final /* enum */ DiscoverApduHandlerState sW = new DiscoverApduHandlerState(){

        @Override
        public List<DiscoverApduHandlerState> cP() {
            DiscoverApduHandlerState[] arrdiscoverApduHandlerState = new DiscoverApduHandlerState[]{sV, sY, sX, sW, ta};
            return Arrays.asList((Object[])arrdiscoverApduHandlerState);
        }

        @Override
        public com.samsung.android.spayfw.payprovider.discover.payment.data.a g(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
            return this.cO();
        }
    };
    public static final /* enum */ DiscoverApduHandlerState sX = new DiscoverApduHandlerState(){

        @Override
        public List<DiscoverApduHandlerState> cP() {
            DiscoverApduHandlerState[] arrdiscoverApduHandlerState = new DiscoverApduHandlerState[]{sV, sY, sX, sW, ta};
            return Arrays.asList((Object[])arrdiscoverApduHandlerState);
        }

        @Override
        public com.samsung.android.spayfw.payprovider.discover.payment.data.a h(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
            return this.cO();
        }
    };
    public static final /* enum */ DiscoverApduHandlerState sY = new DiscoverApduHandlerState(){

        @Override
        public List<DiscoverApduHandlerState> cP() {
            DiscoverApduHandlerState[] arrdiscoverApduHandlerState = new DiscoverApduHandlerState[]{sV, sX, sZ, ta};
            return Arrays.asList((Object[])arrdiscoverApduHandlerState);
        }

        @Override
        public com.samsung.android.spayfw.payprovider.discover.payment.data.a g(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
            return this.cO();
        }

        @Override
        public com.samsung.android.spayfw.payprovider.discover.payment.data.a h(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
            return this.cO();
        }
    };
    public static final /* enum */ DiscoverApduHandlerState sZ = new DiscoverApduHandlerState(){

        @Override
        public List<DiscoverApduHandlerState> cP() {
            DiscoverApduHandlerState[] arrdiscoverApduHandlerState = new DiscoverApduHandlerState[]{sY, sZ, ta};
            return Arrays.asList((Object[])arrdiscoverApduHandlerState);
        }

        @Override
        public com.samsung.android.spayfw.payprovider.discover.payment.data.a f(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
            return this.cO();
        }

        @Override
        public com.samsung.android.spayfw.payprovider.discover.payment.data.a g(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
            return this.cO();
        }

        @Override
        public com.samsung.android.spayfw.payprovider.discover.payment.data.a h(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
            return this.cO();
        }
    };
    public static final /* enum */ DiscoverApduHandlerState ta = new DiscoverApduHandlerState(){

        @Override
        public List<DiscoverApduHandlerState> cP() {
            DiscoverApduHandlerState[] arrdiscoverApduHandlerState = new DiscoverApduHandlerState[]{sY, ta, sX, sW};
            return Arrays.asList((Object[])arrdiscoverApduHandlerState);
        }

        @Override
        public com.samsung.android.spayfw.payprovider.discover.payment.data.a g(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
            return this.cO();
        }

        @Override
        public com.samsung.android.spayfw.payprovider.discover.payment.data.a h(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
            return this.cO();
        }

        @Override
        public com.samsung.android.spayfw.payprovider.discover.payment.data.a i(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
            return this.cO();
        }
    };
    private static final /* synthetic */ DiscoverApduHandlerState[] tb;

    static {
        DiscoverApduHandlerState[] arrdiscoverApduHandlerState = new DiscoverApduHandlerState[]{sV, sW, sX, sY, sZ, ta};
        tb = arrdiscoverApduHandlerState;
    }

    private DiscoverApduHandlerState() {
    }

    private com.samsung.android.spayfw.payprovider.discover.payment.data.a a(a a2) {
        com.samsung.android.spayfw.payprovider.discover.payment.data.a a3 = a2.cK();
        this.a(a2, a3);
        return a3;
    }

    public static DiscoverApduHandlerState valueOf(String string) {
        return (DiscoverApduHandlerState)Enum.valueOf(DiscoverApduHandlerState.class, (String)string);
    }

    public static DiscoverApduHandlerState[] values() {
        return (DiscoverApduHandlerState[])tb.clone();
    }

    public void a(a a2, com.samsung.android.spayfw.payprovider.discover.payment.data.a a3) {
        DiscoverApduHandlerState discoverApduHandlerState = a3.dA();
        if (discoverApduHandlerState == null) {
            discoverApduHandlerState = this;
        }
        if (this.cP().contains((Object)discoverApduHandlerState)) {
            a3.a(discoverApduHandlerState);
            return;
        }
        Log.e("DCSDK_DiscoverApduHandlerState", "processApduResultWithHandler, wrong state requested: " + discoverApduHandlerState.name() + ", current state: " + this.name());
        this.cO();
    }

    public com.samsung.android.spayfw.payprovider.discover.payment.data.a cO() {
        return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27013);
    }

    public abstract List<DiscoverApduHandlerState> cP();

    public com.samsung.android.spayfw.payprovider.discover.payment.data.a e(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
        Log.i("DCSDK_DiscoverApduHandlerState", "state: " + this.name());
        if (byteBuffer == null) {
            Log.e("DCSDK_DiscoverApduHandlerState", "process: apdu is null");
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27264);
        }
        if (e2 == null) {
            Log.e("DCSDK_DiscoverApduHandlerState", "process: context is null, not  ready for payment.");
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27264);
        }
        if (discoverPaymentCard == null) {
            Log.e("DCSDK_DiscoverApduHandlerState", "process: profile is null, not  ready for payment.");
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27264);
        }
        byte by = byteBuffer.getByte(1);
        Log.i("DCSDK_DiscoverApduHandlerState", "handleApdu, APDU INS: " + (by & 255));
        switch (by) {
            default: {
                return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27904);
            }
            case -92: {
                Log.i("DCSDK_DiscoverApduHandlerState", "handleApdu, APDU INS: SELECT APDU received");
                return this.f(byteBuffer, e2, discoverPaymentCard);
            }
            case -88: {
                Log.i("DCSDK_DiscoverApduHandlerState", "handleApdu, APDU INS: GPO APDU received");
                return this.g(byteBuffer, e2, discoverPaymentCard);
            }
            case -78: {
                Log.i("DCSDK_DiscoverApduHandlerState", "handleApdu, APDU INS: READ RECORD APDU received");
                return this.h(byteBuffer, e2, discoverPaymentCard);
            }
            case -54: 
        }
        Log.i("DCSDK_DiscoverApduHandlerState", "handleApdu, APDU INS: GET DATA APDU received");
        return this.i(byteBuffer, e2, discoverPaymentCard);
    }

    public com.samsung.android.spayfw.payprovider.discover.payment.data.a f(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
        return this.a(a.a(byteBuffer, e2, discoverPaymentCard));
    }

    public com.samsung.android.spayfw.payprovider.discover.payment.data.a g(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
        return this.a(a.b(byteBuffer, e2, discoverPaymentCard));
    }

    public com.samsung.android.spayfw.payprovider.discover.payment.data.a h(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
        return this.a(a.c(byteBuffer, e2, discoverPaymentCard));
    }

    public com.samsung.android.spayfw.payprovider.discover.payment.data.a i(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
        return this.a(a.d(byteBuffer, e2, discoverPaymentCard));
    }

}

