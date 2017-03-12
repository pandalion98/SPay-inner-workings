package com.samsung.android.spayfw.payprovider.discover.payment.p017a;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.a.f */
public class SelectRApdu extends ResponseApdu {
    public SelectRApdu(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        m926r(byteBuffer2);
    }
}
