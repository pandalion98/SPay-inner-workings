package com.mastercard.mobile_api.utils.apdu.emv;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.TLV;
import com.mastercard.mobile_api.utils.apdu.RespApdu;

public class GenACRespApdu extends RespApdu {
    public GenACRespApdu(boolean z, ByteArray byteArray, ByteArray byteArray2, byte b, ByteArray byteArray3) {
        ByteArray byteArray4 = ByteArrayFactory.getInstance().getByteArray(new byte[]{(byte) -97, (byte) 39, (byte) 1, b, (byte) -97, (byte) 54, (byte) 2}, 7);
        byteArray4.append(byteArray2);
        if (z) {
            byteArray4.append(ByteArrayFactory.getInstance().getByteArray(new byte[]{(byte) -97, (byte) 75}, 2));
        } else {
            byteArray4.append(ByteArrayFactory.getInstance().getByteArray(new byte[]{(byte) -97, (byte) 38}, 2));
        }
        byteArray4.append(TLV.lengthBytes(byteArray));
        byteArray4.append(byteArray);
        byteArray4.append(TLV.create(ByteArrayFactory.getInstance().getByteArray(new byte[]{(byte) -97, Tnaf.POW_2_WIDTH}, 2), byteArray3));
        setValueAndSuccess(TLV.create((byte) ApplicationInfoManager.TERM_XP2, byteArray4));
    }

    public GenACRespApdu(boolean z, ByteArray byteArray, ByteArray byteArray2, byte b, ByteArray byteArray3, ByteArray byteArray4) {
        ByteArray byteArray5 = ByteArrayFactory.getInstance().getByteArray(new byte[]{(byte) -97, (byte) 39, (byte) 1, b, (byte) -97, (byte) 54, (byte) 2}, 7);
        byteArray5.append(byteArray2);
        if (z) {
            byteArray5.append(ByteArrayFactory.getInstance().getByteArray(new byte[]{(byte) -97, (byte) 75}, 2));
        } else {
            byteArray5.append(ByteArrayFactory.getInstance().getByteArray(new byte[]{(byte) -97, (byte) 38}, 2));
        }
        byteArray5.append(TLV.lengthBytes(byteArray));
        byteArray5.append(byteArray);
        byteArray5.append(TLV.create(ByteArrayFactory.getInstance().getByteArray(new byte[]{(byte) -97, Tnaf.POW_2_WIDTH}, 2), byteArray3));
        if (byteArray4 != null) {
            byteArray5.append(TLV.create(ByteArrayFactory.getInstance().getByteArray(new byte[]{(byte) -33, (byte) 75}, 2), byteArray4));
        }
        setValueAndSuccess(TLV.create((byte) ApplicationInfoManager.TERM_XP2, byteArray5));
    }
}
