/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.HashMap
 *  java.util.Set
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.payprovider.discover.payment.data.c;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.a;
import java.util.HashMap;
import java.util.Set;

public class f {
    private HashMap<ByteBuffer, ByteBuffer> wb = new HashMap();

    public f(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, ByteBuffer byteBuffer4, ByteBuffer byteBuffer5, ByteBuffer byteBuffer6, ByteBuffer byteBuffer7, ByteBuffer byteBuffer8, ByteBuffer byteBuffer9, ByteBuffer byteBuffer10, ByteBuffer byteBuffer11, ByteBuffer byteBuffer12, ByteBuffer byteBuffer13, ByteBuffer byteBuffer14) {
        this.wb.put((Object)c.vC, (Object)byteBuffer);
        this.wb.put((Object)c.vD, (Object)byteBuffer2);
        this.wb.put((Object)c.vE, (Object)byteBuffer3);
        this.wb.put((Object)c.vn, (Object)byteBuffer4);
        this.wb.put((Object)c.vF, (Object)byteBuffer5);
        this.wb.put((Object)c.vG, (Object)byteBuffer6);
        this.wb.put((Object)c.vH, (Object)byteBuffer7);
        this.wb.put((Object)c.vI, (Object)byteBuffer8);
        this.wb.put((Object)c.vJ, (Object)byteBuffer9);
        this.wb.put((Object)c.vK, (Object)byteBuffer10);
        this.wb.put((Object)c.vL, (Object)byteBuffer11);
        this.wb.put((Object)c.uC, (Object)byteBuffer12);
        this.wb.put((Object)c.uD, (Object)byteBuffer13);
        this.wb.put((Object)c.vv, (Object)byteBuffer14);
    }

    public ByteBuffer dW() {
        ByteBuffer byteBuffer = new ByteBuffer(0);
        for (ByteBuffer byteBuffer2 : this.wb.keySet()) {
            if (this.wb.get((Object)byteBuffer2) == null) continue;
            byteBuffer.append(a.c(byteBuffer2, (ByteBuffer)this.wb.get((Object)byteBuffer2)));
        }
        return byteBuffer;
    }
}

