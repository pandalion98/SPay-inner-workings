/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.TLV;
import com.mastercard.mobile_api.utils.apdu.RespApdu;

public class SelectRespApdu
extends RespApdu {
    public SelectRespApdu(ByteArray byteArray, ByteArray byteArray2) {
        ByteArray byteArray3 = TLV.create((byte)-124, byteArray);
        byteArray3.append(byteArray2);
        this.setValueAndSuccess(TLV.create((byte)111, byteArray3));
    }
}

