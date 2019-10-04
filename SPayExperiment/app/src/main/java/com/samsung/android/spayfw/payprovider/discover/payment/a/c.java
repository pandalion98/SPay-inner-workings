/*
 * Decompiled with CFR 0.0.
 */
package com.samsung.android.spayfw.payprovider.discover.payment.a;

import com.samsung.android.spayfw.payprovider.discover.payment.a.e;
import com.samsung.android.spayfw.payprovider.discover.payment.data.d;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.a;

public class c
extends e {
    public c(d d2) {
        this.r(a.a((byte)119, d2.dW()));
    }

    public c(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        ByteBuffer byteBuffer3 = a.a((byte)-126, byteBuffer);
        byteBuffer3.append(a.a((byte)-108, byteBuffer2));
        this.r(a.a((byte)119, byteBuffer3));
    }
}

