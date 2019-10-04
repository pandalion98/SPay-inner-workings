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

public abstract class IssuerApplicationParser
extends DataGroup {
    private String cardVerificationResult = null;
    private String issuerApplicationData = null;

    public String getCardVerificationResult() {
        return this.cardVerificationResult;
    }

    public String getIssuerApplicationData() {
        return this.issuerApplicationData;
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
                    if (!tagInfo.getTagName().equalsIgnoreCase("9F10")) continue;
                    String string = tagInfo.getTagValue();
                    this.setIssuerApplicationData(string);
                    if (string == null || string.length() <= 0) continue;
                    this.setCardVerificationResult(string.substring(6, string.length()));
                }
                String[] arrstring = new String[]{this.issuerApplicationData, this.cardVerificationResult};
                TokenDataParser.throwExceptionIfEmpty(arrstring);
                return;
            }
            catch (Exception exception) {
                this.setTokenDataErrorStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
            }
        }
    }

    public void setCardVerificationResult(String string) {
        this.cardVerificationResult = string;
    }

    public void setIssuerApplicationData(String string) {
        this.issuerApplicationData = string;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append("\ncardVerificationResult=").append(this.cardVerificationResult);
        return stringBuilder.toString();
    }
}

