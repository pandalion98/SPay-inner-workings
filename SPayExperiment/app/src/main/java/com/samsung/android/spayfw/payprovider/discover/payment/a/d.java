/*
 * Decompiled with CFR 0.0.
 */
package com.samsung.android.spayfw.payprovider.discover.payment.a;

import com.samsung.android.spayfw.payprovider.discover.payment.a.a;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

public class d
extends a {
    public d(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    @Override
    public byte dl() {
        return this.dj().getByte(-1 + this.dj().getSize());
    }

    public byte getRecordNumber() {
        return this.getP1();
    }

    public byte getSfiNumber() {
        return (byte)(this.getP2() >>> 3);
    }
}

