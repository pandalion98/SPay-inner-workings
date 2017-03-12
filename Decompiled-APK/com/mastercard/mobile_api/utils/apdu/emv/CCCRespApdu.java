package com.mastercard.mobile_api.utils.apdu.emv;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.TLV;
import com.mastercard.mobile_api.utils.apdu.RespApdu;

public class CCCRespApdu extends RespApdu {
    public CCCRespApdu(ByteArray byteArray) {
        setValueAndSuccess(TLV.create((byte) ApplicationInfoManager.TERM_XP2, byteArray));
    }
}
