package com.samsung.android.spayfw.payprovider.discover.payment.p017a;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.a.d */
public class ReadRecordCApdu extends CommandApdu {
    public ReadRecordCApdu(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    public byte getRecordNumber() {
        return getP1();
    }

    public byte getSfiNumber() {
        return (byte) (getP2() >>> 3);
    }

    public byte dl() {
        return dj().getByte(dj().getSize() - 1);
    }
}
