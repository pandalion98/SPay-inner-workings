package com.americanexpress.mobilepayments.hceclient.utils.tlv.framework;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;

public class PPSETagValue extends TagValue {
    private String sAID;
    private String sDirectoryEntry;

    public PPSETagValue() {
        this.sDirectoryEntry = BuildConfig.FLAVOR;
        this.sAID = null;
    }

    public String getsDirectoryEntry() {
        return this.sDirectoryEntry;
    }

    public void setsDirectoryEntry(String str) {
        this.sDirectoryEntry = str;
    }

    public String getsAID() {
        return this.sAID;
    }

    public void setsAID(String str) {
        this.sAID = str;
    }

    public String toString() {
        return this.sAID + HCEClientConstants.TAG_KEY_SEPARATOR + this.sDirectoryEntry;
    }

    public static PPSETagValue fromString(String str) {
        String[] split = str.split(HCEClientConstants.TAG_KEY_SEPARATOR);
        if (split.length != 2) {
            return null;
        }
        PPSETagValue pPSETagValue = new PPSETagValue();
        pPSETagValue.setsAID(split[0]);
        pPSETagValue.setsDirectoryEntry(split[1]);
        return pPSETagValue;
    }
}
