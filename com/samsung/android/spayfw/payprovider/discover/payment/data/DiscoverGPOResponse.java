package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.BERTLV;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.util.HashMap;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.data.d */
public class DiscoverGPOResponse {
    private ByteBuffer[] wa;
    private HashMap<ByteBuffer, ByteBuffer> wb;

    /* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.data.d.a */
    public static class DiscoverGPOResponse extends DiscoverGPOResponse {
        public DiscoverGPOResponse(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, ByteBuffer byteBuffer4, ByteBuffer byteBuffer5, ByteBuffer byteBuffer6, ByteBuffer byteBuffer7, ByteBuffer byteBuffer8, ByteBuffer byteBuffer9, ByteBuffer byteBuffer10, ByteBuffer byteBuffer11, ByteBuffer byteBuffer12) {
            super(byteBuffer, null, byteBuffer2, null, byteBuffer3, byteBuffer4, byteBuffer5, byteBuffer6, byteBuffer7, byteBuffer8, byteBuffer9, byteBuffer10, byteBuffer11, byteBuffer12);
        }

        public DiscoverGPOResponse(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, ByteBuffer byteBuffer4, ByteBuffer byteBuffer5, ByteBuffer byteBuffer6, ByteBuffer byteBuffer7) {
            super(byteBuffer, byteBuffer2, byteBuffer3, null, byteBuffer4, byteBuffer5, byteBuffer6, null, null, null, null, byteBuffer7, null, null);
        }
    }

    /* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.data.d.b */
    public static class DiscoverGPOResponse extends DiscoverGPOResponse {
        public DiscoverGPOResponse(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, ByteBuffer byteBuffer4, ByteBuffer byteBuffer5, ByteBuffer byteBuffer6, ByteBuffer byteBuffer7) {
            super(byteBuffer, byteBuffer2, null, byteBuffer3, byteBuffer4, byteBuffer5, byteBuffer6, null, null, null, null, byteBuffer7, null, null);
        }
    }

    public DiscoverGPOResponse(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, ByteBuffer byteBuffer4, ByteBuffer byteBuffer5, ByteBuffer byteBuffer6, ByteBuffer byteBuffer7, ByteBuffer byteBuffer8, ByteBuffer byteBuffer9, ByteBuffer byteBuffer10, ByteBuffer byteBuffer11, ByteBuffer byteBuffer12, ByteBuffer byteBuffer13, ByteBuffer byteBuffer14) {
        this.wa = new ByteBuffer[]{DiscoverDataTags.ux, DiscoverDataTags.uy, DiscoverDataTags.vv, DiscoverDataTags.vw, DiscoverDataTags.vn, DiscoverDataTags.vx, DiscoverDataTags.vy, DiscoverDataTags.vz, DiscoverDataTags.vk, DiscoverDataTags.vA, DiscoverDataTags.vB, DiscoverDataTags.uC, DiscoverDataTags.vW, DiscoverDataTags.vX};
        this.wb = new HashMap();
        this.wb.put(DiscoverDataTags.ux, byteBuffer);
        this.wb.put(DiscoverDataTags.uy, byteBuffer2);
        this.wb.put(DiscoverDataTags.vv, byteBuffer3);
        this.wb.put(DiscoverDataTags.vw, byteBuffer4);
        this.wb.put(DiscoverDataTags.vn, byteBuffer5);
        this.wb.put(DiscoverDataTags.vx, byteBuffer6);
        this.wb.put(DiscoverDataTags.vy, byteBuffer7);
        this.wb.put(DiscoverDataTags.vz, byteBuffer8);
        this.wb.put(DiscoverDataTags.vk, byteBuffer9);
        this.wb.put(DiscoverDataTags.vA, byteBuffer10);
        this.wb.put(DiscoverDataTags.vB, byteBuffer11);
        this.wb.put(DiscoverDataTags.uC, byteBuffer12);
        this.wb.put(DiscoverDataTags.vW, byteBuffer13);
        this.wb.put(DiscoverDataTags.vX, byteBuffer14);
    }

    public ByteBuffer dW() {
        ByteBuffer byteBuffer = new ByteBuffer(0);
        for (ByteBuffer byteBuffer2 : this.wa) {
            if (this.wb.get(byteBuffer2) != null) {
                byteBuffer.append(BERTLV.m1004c(byteBuffer2, (ByteBuffer) this.wb.get(byteBuffer2)));
            }
        }
        return byteBuffer;
    }
}
