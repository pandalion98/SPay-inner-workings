/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.TLV;
import com.mastercard.mobile_api.utils.apdu.RespApdu;

public class GenACRespApdu
extends RespApdu {
    /*
     * Enabled aggressive block sorting
     */
    public GenACRespApdu(boolean bl, ByteArray byteArray, ByteArray byteArray2, byte by, ByteArray byteArray3) {
        byte[] arrby = new byte[]{-97, 39, 1, by, -97, 54, 2};
        ByteArray byteArray4 = ByteArrayFactory.getInstance().getByteArray(arrby, 7);
        byteArray4.append(byteArray2);
        if (bl) {
            byte[] arrby2 = new byte[]{-97, 75};
            byteArray4.append(ByteArrayFactory.getInstance().getByteArray(arrby2, 2));
        } else {
            byte[] arrby3 = new byte[]{-97, 38};
            byteArray4.append(ByteArrayFactory.getInstance().getByteArray(arrby3, 2));
        }
        byteArray4.append(TLV.lengthBytes(byteArray));
        byteArray4.append(byteArray);
        byte[] arrby4 = new byte[]{-97, 16};
        byteArray4.append(TLV.create(ByteArrayFactory.getInstance().getByteArray(arrby4, 2), byteArray3));
        this.setValueAndSuccess(TLV.create((byte)119, byteArray4));
    }

    /*
     * Enabled aggressive block sorting
     */
    public GenACRespApdu(boolean bl, ByteArray byteArray, ByteArray byteArray2, byte by, ByteArray byteArray3, ByteArray byteArray4) {
        byte[] arrby = new byte[]{-97, 39, 1, by, -97, 54, 2};
        ByteArray byteArray5 = ByteArrayFactory.getInstance().getByteArray(arrby, 7);
        byteArray5.append(byteArray2);
        if (bl) {
            byte[] arrby2 = new byte[]{-97, 75};
            byteArray5.append(ByteArrayFactory.getInstance().getByteArray(arrby2, 2));
        } else {
            byte[] arrby3 = new byte[]{-97, 38};
            byteArray5.append(ByteArrayFactory.getInstance().getByteArray(arrby3, 2));
        }
        byteArray5.append(TLV.lengthBytes(byteArray));
        byteArray5.append(byteArray);
        byte[] arrby4 = new byte[]{-97, 16};
        byteArray5.append(TLV.create(ByteArrayFactory.getInstance().getByteArray(arrby4, 2), byteArray3));
        if (byteArray4 != null) {
            byte[] arrby5 = new byte[]{-33, 75};
            byteArray5.append(TLV.create(ByteArrayFactory.getInstance().getByteArray(arrby5, 2), byteArray4));
        }
        this.setValueAndSuccess(TLV.create((byte)119, byteArray5));
    }
}

