package com.samsung.android.spayfw.payprovider.discover.payment.p017a;

import com.mastercard.mobile_api.utils.apdu.emv.SetResetParamApdu;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.a.e */
public class ResponseApdu {
    protected ByteBuffer tC;

    public void m926r(ByteBuffer byteBuffer) {
        if (byteBuffer != null) {
            this.tC = byteBuffer.clone();
            this.tC.append(new ByteBuffer(new byte[]{SetResetParamApdu.CLA, (byte) 0}));
        }
    }

    public ByteBuffer dj() {
        return this.tC;
    }
}
