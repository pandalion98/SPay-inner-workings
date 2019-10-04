/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.payments.nfc;

import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.PPSETagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.Map;

public class PPSEResponse {
    private static final byte[] _baPPSE_AID = new byte[]{50, 80, 65, 89, 46, 83, 89, 83, 46, 68, 68, 70, 48, 49};
    private String availableAIDs = "";
    private String currentAID = "";
    private String sPPSEAIDEntry = "";
    private String sPPSERes = "";

    public void buildPPSEResponse(boolean bl) {
        DataContext dataContext = DataContext.getSessionInstance();
        PPSETagValue pPSETagValue = (PPSETagValue)TagsMapUtil.getTagValue(dataContext.getTagMap(), CPDLConfig.getDGI_TAG("PAYMENT_PARAMETERS_DGI"), CPDLConfig.getDGI_TAG("PPSE_TAG_V1"), true);
        PPSETagValue pPSETagValue2 = (PPSETagValue)TagsMapUtil.getTagValue(dataContext.getTagMap(), CPDLConfig.getDGI_TAG("PAYMENT_PARAMETERS_DGI"), CPDLConfig.getDGI_TAG("PPSE_TAG_V2"), false);
        String string = "";
        if (pPSETagValue != null) {
            string = pPSETagValue.getsDirectoryEntry();
            this.availableAIDs = "00" + pPSETagValue.getsAID();
        }
        String string2 = string;
        if (bl && pPSETagValue2 != null) {
            string2 = string2 + pPSETagValue2.getsDirectoryEntry();
            this.availableAIDs = this.availableAIDs + "=01" + pPSETagValue2.getsAID();
        }
        if (string2.compareToIgnoreCase("") != 0) {
            string2 = "A5" + Utility.constructLV(new StringBuilder().append("BF0C").append(Utility.constructLV(string2)).toString());
            this.sPPSEAIDEntry = "BF0C" + Utility.constructLV(string2);
        }
        String string3 = "84" + Utility.constructLV(HexUtils.byteArrayToHexString(_baPPSE_AID));
        this.sPPSERes = "6F" + Utility.constructLV(new StringBuilder().append(string3).append(string2).toString());
    }

    public String getCurrentAID() {
        return this.currentAID;
    }

    public byte[] getPPSERes() {
        return HexUtils.hexStringToByteArray(this.sPPSERes);
    }

    public String getsPPSEAIDEntry() {
        return this.sPPSEAIDEntry;
    }

    public byte isAIDPresent(byte[] arrby) {
        String[] arrstring = this.availableAIDs.split("=");
        int n2 = 0;
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            if (arrstring[i2].compareToIgnoreCase("") == 0 || HexUtils.secureCompare(arrby, (short)0, HexUtils.hexStringToByteArray(arrstring[i2].substring(2)), (short)0, (short)arrby.length) != Constants.MAGIC_TRUE) continue;
            n2 = 1;
            if (arrstring[i2].startsWith("00")) {
                n2 = -127;
            }
            this.currentAID = arrstring[i2].substring(2);
        }
        return (byte)n2;
    }
}

