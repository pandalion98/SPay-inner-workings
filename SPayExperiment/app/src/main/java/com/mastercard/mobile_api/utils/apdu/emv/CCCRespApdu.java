/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.TLV;
import com.mastercard.mobile_api.utils.apdu.RespApdu;

public class CCCRespApdu
extends RespApdu {
    public CCCRespApdu(ByteArray byteArray) {
        this.setValueAndSuccess(TLV.create((byte)119, byteArray));
    }
}

