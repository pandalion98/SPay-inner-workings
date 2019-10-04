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

public abstract class CardRiskManagmentParser
extends DataGroup {
    private String serviceCode;

    public String getServiceCode() {
        return this.serviceCode;
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
                    if (!tagInfo.getTagName().equalsIgnoreCase("5F30")) continue;
                    this.serviceCode = tagInfo.getTagValue();
                }
                String[] arrstring = new String[]{this.serviceCode};
                TokenDataParser.throwExceptionIfEmpty(arrstring);
                return;
            }
            catch (Exception exception) {
                this.setTokenDataErrorStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
            }
        }
    }

    public void setServiceCode(String string) {
        this.serviceCode = string;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        return stringBuilder.toString();
    }
}

