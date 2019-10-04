/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.List
 */
package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.model.TagInfo;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.model.token.DataGroup;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TLVHelper;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import java.util.List;

public abstract class Track2Info
extends DataGroup {
    private String accountRefNumber;
    private String track2EquivalentData;

    private String parseAccountNumber(String string) {
        if (string != null && string.length() != 0 && string.indexOf("D") != -1) {
            return string.substring(0, string.indexOf("D"));
        }
        return null;
    }

    public String getAccountRefNumber() {
        return this.accountRefNumber;
    }

    public String getTrack2EquivalentData() {
        return this.track2EquivalentData;
    }

    @Override
    public abstract void init(String var1);

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void parseDataGroup() {
        if (!this.isDataGroupMalformed()) {
            TLVHelper tLVHelper = new TLVHelper();
            try {
                for (TagInfo tagInfo : tLVHelper.parseTLV(this.getParsedData())) {
                    if (!tagInfo.getTagName().equalsIgnoreCase("57")) continue;
                    this.track2EquivalentData = tagInfo.getTagValue();
                    this.setAccountRefNumber(this.parseAccountNumber(tagInfo.getTagValue()));
                }
                String[] arrstring = new String[]{this.accountRefNumber};
                TokenDataParser.throwExceptionIfEmpty(arrstring);
                return;
            }
            catch (Exception exception) {
                this.setTokenDataErrorStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
            }
        }
    }

    public void setAccountRefNumber(String string) {
        this.accountRefNumber = string;
    }

    public void setTrack2EquivalentData(String string) {
        this.track2EquivalentData = string;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append("\naccountRefNumber=").append(this.accountRefNumber);
        return stringBuilder.toString();
    }
}

