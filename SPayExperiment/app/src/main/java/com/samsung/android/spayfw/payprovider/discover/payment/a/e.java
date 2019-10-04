/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.discover.payment.a;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

public class e {
    protected ByteBuffer tC;

    public ByteBuffer dj() {
        return this.tC;
    }

    public void r(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return;
        }
        this.tC = byteBuffer.clone();
        ByteBuffer byteBuffer2 = new ByteBuffer(new byte[]{-112, 0});
        this.tC.append(byteBuffer2);
    }
}

