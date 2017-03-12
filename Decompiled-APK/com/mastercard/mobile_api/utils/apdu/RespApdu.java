package com.mastercard.mobile_api.utils.apdu;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.apdu.emv.SetResetParamApdu;

public class RespApdu {
    private ByteArray val;

    public RespApdu(ByteArray byteArray, ByteArray byteArray2) {
        setValue(byteArray, byteArray2);
    }

    public RespApdu(byte[] bArr, int i) {
        this.val = ByteArrayFactory.getInstance().getByteArray(bArr, i);
    }

    public RespApdu(ByteArray byteArray) {
        this.val = byteArray;
    }

    public RespApdu(int i, int i2) {
        this.val = ByteArrayFactory.getInstance().getFromWord(i);
    }

    public void setValue(ByteArray byteArray, ByteArray byteArray2) {
        this.val = byteArray;
        this.val.append(byteArray2);
    }

    public void setValueAndSuccess(ByteArray byteArray) {
        this.val = byteArray;
        ByteArray byteArray2 = ByteArrayFactory.getInstance().getByteArray(2);
        byteArray2.setByte(0, SetResetParamApdu.CLA);
        byteArray2.setByte(1, (byte) 0);
        this.val.append(byteArray2);
    }

    public byte[] getBytes() {
        return this.val.getBytes();
    }

    public ByteArray getByteArray() {
        return this.val;
    }
}
