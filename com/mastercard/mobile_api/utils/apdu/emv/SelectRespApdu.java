package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.TLV;
import com.mastercard.mobile_api.utils.apdu.RespApdu;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;

public class SelectRespApdu extends RespApdu {
    public SelectRespApdu(ByteArray byteArray, ByteArray byteArray2) {
        ByteArray create = TLV.create((byte) PinChangeUnblockApdu.CLA, byteArray);
        create.append(byteArray2);
        setValueAndSuccess(TLV.create((byte) MCFCITemplate.TAG_FCI_TEMPLATE, create));
    }
}
