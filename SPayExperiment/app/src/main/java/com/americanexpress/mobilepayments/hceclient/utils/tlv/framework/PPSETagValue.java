/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.utils.tlv.framework;

import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;

public class PPSETagValue
extends TagValue {
    private String sAID = null;
    private String sDirectoryEntry = "";

    public static PPSETagValue fromString(String string) {
        String[] arrstring = string.split("-");
        if (arrstring.length != 2) {
            return null;
        }
        PPSETagValue pPSETagValue = new PPSETagValue();
        pPSETagValue.setsAID(arrstring[0]);
        pPSETagValue.setsDirectoryEntry(arrstring[1]);
        return pPSETagValue;
    }

    public String getsAID() {
        return this.sAID;
    }

    public String getsDirectoryEntry() {
        return this.sDirectoryEntry;
    }

    public void setsAID(String string) {
        this.sAID = string;
    }

    public void setsDirectoryEntry(String string) {
        this.sDirectoryEntry = string;
    }

    @Override
    public String toString() {
        return this.sAID + "-" + this.sDirectoryEntry;
    }
}

