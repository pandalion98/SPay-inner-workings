/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.utils.tlv.framework;

import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.NFCLUPCTagValue;

public class MSTLUPCTagValue
extends NFCLUPCTagValue {
    private String mstDynamicData;

    public MSTLUPCTagValue() {
    }

    public MSTLUPCTagValue(String string) {
        String[] arrstring = string.split("-");
        if (arrstring.length != 4) {
            throw new HCEClientException(OperationStatus.INVALID_DATA);
        }
        this.atc = arrstring[0];
        this.startDate = arrstring[2];
        this.endDate = arrstring[3];
    }

    public String getMstDynamicData() {
        String string = this.mstDynamicData;
        this.mstDynamicData = "";
        return string;
    }

    public void setMstDynamicData(String string) {
        this.mstDynamicData = string;
    }

    @Override
    public String toString() {
        return this.atc + "-" + this.mstDynamicData + "-" + this.startDate + "-" + this.endDate;
    }
}

