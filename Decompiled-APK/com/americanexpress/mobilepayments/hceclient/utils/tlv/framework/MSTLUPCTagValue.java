package com.americanexpress.mobilepayments.hceclient.utils.tlv.framework;

import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;

public class MSTLUPCTagValue extends NFCLUPCTagValue {
    private String mstDynamicData;

    public MSTLUPCTagValue(String str) {
        String[] split = str.split(HCEClientConstants.TAG_KEY_SEPARATOR);
        if (split.length != 4) {
            throw new HCEClientException(OperationStatus.INVALID_DATA);
        }
        this.atc = split[0];
        this.startDate = split[2];
        this.endDate = split[3];
    }

    public String getMstDynamicData() {
        String str = this.mstDynamicData;
        this.mstDynamicData = BuildConfig.FLAVOR;
        return str;
    }

    public void setMstDynamicData(String str) {
        this.mstDynamicData = str;
    }

    public String toString() {
        return this.atc + HCEClientConstants.TAG_KEY_SEPARATOR + this.mstDynamicData + HCEClientConstants.TAG_KEY_SEPARATOR + this.startDate + HCEClientConstants.TAG_KEY_SEPARATOR + this.endDate;
    }
}
