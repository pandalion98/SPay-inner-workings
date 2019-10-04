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

public abstract class ProcessingOptionsParser
extends DataGroup {
    private String applicationFileLocator;
    private String applicationInterchangeProfile;

    public String getApplicationFileLocator() {
        return this.applicationFileLocator;
    }

    public String getApplicationInterchangeProfile() {
        return this.applicationInterchangeProfile;
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
        block6 : {
            if (!this.isDataGroupMalformed()) {
                TLVHelper tLVHelper = new TLVHelper();
                try {
                    for (TagInfo tagInfo : tLVHelper.parseTLV(this.getParsedData())) {
                        if (tagInfo.getTagName().equalsIgnoreCase("82")) {
                            this.setApplicationInterchangeProfile(tagInfo.getTagValue());
                            continue;
                        }
                        if (!tagInfo.getTagName().equalsIgnoreCase("94")) continue;
                        this.setApplicationFileLocator(tagInfo.getTagValue());
                    }
                    break block6;
                }
                catch (Exception exception) {
                    this.setTokenDataErrorStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
                }
            }
            return;
        }
        String[] arrstring = new String[]{this.applicationInterchangeProfile, this.applicationFileLocator};
        TokenDataParser.throwExceptionIfEmpty(arrstring);
    }

    public void setApplicationFileLocator(String string) {
        this.applicationFileLocator = string;
    }

    public void setApplicationInterchangeProfile(String string) {
        this.applicationInterchangeProfile = string;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append("\napplicationInterchangeProfile=").append(this.applicationInterchangeProfile);
        stringBuilder.append("\napplicationFileLocator=").append(this.applicationFileLocator);
        return stringBuilder.toString();
    }
}

