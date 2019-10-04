/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.HashMap
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.payprovider.discover.payment.data.c;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.util.HashMap;

public class d {
    private ByteBuffer[] wa;
    private HashMap<ByteBuffer, ByteBuffer> wb;

    public d(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, ByteBuffer byteBuffer4, ByteBuffer byteBuffer5, ByteBuffer byteBuffer6, ByteBuffer byteBuffer7, ByteBuffer byteBuffer8, ByteBuffer byteBuffer9, ByteBuffer byteBuffer10, ByteBuffer byteBuffer11, ByteBuffer byteBuffer12, ByteBuffer byteBuffer13, ByteBuffer byteBuffer14) {
        ByteBuffer[] arrbyteBuffer = new ByteBuffer[]{c.ux, c.uy, c.vv, c.vw, c.vn, c.vx, c.vy, c.vz, c.vk, c.vA, c.vB, c.uC, c.vW, c.vX};
        this.wa = arrbyteBuffer;
        this.wb = new HashMap();
        this.wb.put((Object)c.ux, (Object)byteBuffer);
        this.wb.put((Object)c.uy, (Object)byteBuffer2);
        this.wb.put((Object)c.vv, (Object)byteBuffer3);
        this.wb.put((Object)c.vw, (Object)byteBuffer4);
        this.wb.put((Object)c.vn, (Object)byteBuffer5);
        this.wb.put((Object)c.vx, (Object)byteBuffer6);
        this.wb.put((Object)c.vy, (Object)byteBuffer7);
        this.wb.put((Object)c.vz, (Object)byteBuffer8);
        this.wb.put((Object)c.vk, (Object)byteBuffer9);
        this.wb.put((Object)c.vA, (Object)byteBuffer10);
        this.wb.put((Object)c.vB, (Object)byteBuffer11);
        this.wb.put((Object)c.uC, (Object)byteBuffer12);
        this.wb.put((Object)c.vW, (Object)byteBuffer13);
        this.wb.put((Object)c.vX, (Object)byteBuffer14);
    }

    public ByteBuffer dW() {
        ByteBuffer byteBuffer = new ByteBuffer(0);
        for (ByteBuffer byteBuffer2 : this.wa) {
            if (this.wb.get((Object)byteBuffer2) == null) continue;
            byteBuffer.append(com.samsung.android.spayfw.payprovider.discover.payment.utils.a.c(byteBuffer2, (ByteBuffer)this.wb.get((Object)byteBuffer2)));
        }
        return byteBuffer;
    }

    public static class a
    extends d {
        public a(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, ByteBuffer byteBuffer4, ByteBuffer byteBuffer5, ByteBuffer byteBuffer6, ByteBuffer byteBuffer7) {
            super(byteBuffer, byteBuffer2, byteBuffer3, null, byteBuffer4, byteBuffer5, byteBuffer6, null, null, null, null, byteBuffer7, null, null);
        }

        public a(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, ByteBuffer byteBuffer4, ByteBuffer byteBuffer5, ByteBuffer byteBuffer6, ByteBuffer byteBuffer7, ByteBuffer byteBuffer8, ByteBuffer byteBuffer9, ByteBuffer byteBuffer10, ByteBuffer byteBuffer11, ByteBuffer byteBuffer12) {
            super(byteBuffer, null, byteBuffer2, null, byteBuffer3, byteBuffer4, byteBuffer5, byteBuffer6, byteBuffer7, byteBuffer8, byteBuffer9, byteBuffer10, byteBuffer11, byteBuffer12);
        }
    }

    public static class b
    extends d {
        public b(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, ByteBuffer byteBuffer4, ByteBuffer byteBuffer5, ByteBuffer byteBuffer6, ByteBuffer byteBuffer7) {
            super(byteBuffer, byteBuffer2, null, byteBuffer3, byteBuffer4, byteBuffer5, byteBuffer6, null, null, null, null, byteBuffer7, null, null);
        }
    }

}

