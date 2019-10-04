/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.utils.tlv.framework;

import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;

public class NFCLUPCTagValue
extends TagValue {
    protected String atc;
    protected String dki;
    protected String endDate;
    protected String kcv;
    protected String lupc;
    protected String startDate;

    public NFCLUPCTagValue() {
    }

    public NFCLUPCTagValue(String string) {
        String[] arrstring = string.split("-");
        if (arrstring.length != 6) {
            throw new HCEClientException(OperationStatus.INVALID_DATA);
        }
        this.atc = arrstring[0];
        this.dki = arrstring[3];
        this.startDate = arrstring[4];
        this.endDate = arrstring[5];
    }

    public String getAtc() {
        return this.atc;
    }

    public String getDki() {
        return this.dki;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public String getKcv() {
        String string = this.kcv;
        this.kcv = "";
        return string;
    }

    public String getLupc() {
        String string = this.lupc;
        this.lupc = "";
        return string;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setAtc(String string) {
        this.atc = string;
    }

    public void setDki(String string) {
        this.dki = string;
    }

    public void setEndDate(String string) {
        this.endDate = string;
    }

    public void setKcv(String string) {
        this.kcv = string;
    }

    public void setLupc(String string) {
        this.lupc = string;
    }

    public void setStartDate(String string) {
        this.startDate = string;
    }

    @Override
    public String toString() {
        return this.atc + "-" + this.lupc + "-" + this.kcv + "-" + this.dki + "-" + this.startDate + "-" + this.endDate;
    }
}

