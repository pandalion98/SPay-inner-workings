package com.americanexpress.mobilepayments.hceclient.utils.tlv.framework;

import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;

public class NFCLUPCTagValue extends TagValue {
    protected String atc;
    protected String dki;
    protected String endDate;
    protected String kcv;
    protected String lupc;
    protected String startDate;

    public NFCLUPCTagValue(String str) {
        String[] split = str.split(HCEClientConstants.TAG_KEY_SEPARATOR);
        if (split.length != 6) {
            throw new HCEClientException(OperationStatus.INVALID_DATA);
        }
        this.atc = split[0];
        this.dki = split[3];
        this.startDate = split[4];
        this.endDate = split[5];
    }

    public String getAtc() {
        return this.atc;
    }

    public void setAtc(String str) {
        this.atc = str;
    }

    public String getLupc() {
        String str = this.lupc;
        this.lupc = BuildConfig.FLAVOR;
        return str;
    }

    public void setLupc(String str) {
        this.lupc = str;
    }

    public String getKcv() {
        String str = this.kcv;
        this.kcv = BuildConfig.FLAVOR;
        return str;
    }

    public void setKcv(String str) {
        this.kcv = str;
    }

    public String getDki() {
        return this.dki;
    }

    public void setDki(String str) {
        this.dki = str;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String str) {
        this.startDate = str;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String str) {
        this.endDate = str;
    }

    public String toString() {
        return this.atc + HCEClientConstants.TAG_KEY_SEPARATOR + this.lupc + HCEClientConstants.TAG_KEY_SEPARATOR + this.kcv + HCEClientConstants.TAG_KEY_SEPARATOR + this.dki + HCEClientConstants.TAG_KEY_SEPARATOR + this.startDate + HCEClientConstants.TAG_KEY_SEPARATOR + this.endDate;
    }
}
