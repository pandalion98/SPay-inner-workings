package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.BERTLV;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.util.HashMap;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.data.f */
public class DiscoverTransactionLogData {
    private HashMap<ByteBuffer, ByteBuffer> wb;

    public DiscoverTransactionLogData(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, ByteBuffer byteBuffer4, ByteBuffer byteBuffer5, ByteBuffer byteBuffer6, ByteBuffer byteBuffer7, ByteBuffer byteBuffer8, ByteBuffer byteBuffer9, ByteBuffer byteBuffer10, ByteBuffer byteBuffer11, ByteBuffer byteBuffer12, ByteBuffer byteBuffer13, ByteBuffer byteBuffer14) {
        this.wb = new HashMap();
        this.wb.put(DiscoverDataTags.vC, byteBuffer);
        this.wb.put(DiscoverDataTags.vD, byteBuffer2);
        this.wb.put(DiscoverDataTags.vE, byteBuffer3);
        this.wb.put(DiscoverDataTags.vn, byteBuffer4);
        this.wb.put(DiscoverDataTags.vF, byteBuffer5);
        this.wb.put(DiscoverDataTags.vG, byteBuffer6);
        this.wb.put(DiscoverDataTags.vH, byteBuffer7);
        this.wb.put(DiscoverDataTags.vI, byteBuffer8);
        this.wb.put(DiscoverDataTags.vJ, byteBuffer9);
        this.wb.put(DiscoverDataTags.vK, byteBuffer10);
        this.wb.put(DiscoverDataTags.vL, byteBuffer11);
        this.wb.put(DiscoverDataTags.uC, byteBuffer12);
        this.wb.put(DiscoverDataTags.uD, byteBuffer13);
        this.wb.put(DiscoverDataTags.vv, byteBuffer14);
    }

    public ByteBuffer dW() {
        ByteBuffer byteBuffer = new ByteBuffer(0);
        for (ByteBuffer byteBuffer2 : this.wb.keySet()) {
            if (this.wb.get(byteBuffer2) != null) {
                byteBuffer.append(BERTLV.m1004c(byteBuffer2, (ByteBuffer) this.wb.get(byteBuffer2)));
            }
        }
        return byteBuffer;
    }
}
