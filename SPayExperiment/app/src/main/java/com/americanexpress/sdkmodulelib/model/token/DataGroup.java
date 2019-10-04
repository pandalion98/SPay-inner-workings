/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.exception.TokenDataParseException;
import com.americanexpress.sdkmodulelib.model.TagInfo;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TLVHelper;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;

public abstract class DataGroup {
    private String endTag;
    protected boolean isTokenDataContainsEMV = false;
    private String parsedData;
    private String startTag;
    private String tokenDataBlob;
    private TokenDataStatus tokenDataErrorStatus;

    protected String getDataGroup(String string, String string2) {
        TagInfo tagInfo;
        int n2 = this.tokenDataBlob.indexOf(string);
        int n3 = this.tokenDataBlob.indexOf(string2);
        if (n2 == -1 || n3 == -1) {
            throw new TokenDataParseException(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
        }
        String string3 = this.tokenDataBlob.substring(n2 + string.length(), n3);
        TLVHelper tLVHelper = new TLVHelper();
        try {
            tagInfo = tLVHelper.getFirstTag("f1" + string3);
        }
        catch (Exception exception) {
            throw new TokenDataParseException(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
        }
        return tagInfo.getTagValue();
    }

    public String getParsedData() {
        return this.parsedData;
    }

    public String getStartTag() {
        return this.startTag;
    }

    public String getTokenDataBlob() {
        return this.tokenDataBlob;
    }

    public TokenDataStatus getTokenDataErrorStatus() {
        return this.tokenDataErrorStatus;
    }

    public abstract void init(String var1);

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void init(String string, String string2, String string3) {
        this.tokenDataBlob = string.toUpperCase();
        this.startTag = string2;
        this.endTag = string3;
        try {
            this.parsedData = this.getDataGroup(string2, string3);
        }
        catch (TokenDataParseException tokenDataParseException) {
            this.setTokenDataErrorStatus(tokenDataParseException.getTokenDataStatus());
        }
        this.parseDataGroup();
    }

    public boolean isDataGroupMalformed() {
        return this.getTokenDataErrorStatus() != null;
    }

    public boolean isTokenDataContainsEMV() {
        return this.isTokenDataContainsEMV;
    }

    public abstract void parseDataGroup();

    public void setIsTokenDataContainsEMV(boolean bl) {
        this.isTokenDataContainsEMV = bl;
    }

    public void setParsedData(String string) {
        this.parsedData = string;
    }

    public void setTokenDataBlob(String string) {
        this.tokenDataBlob = string;
    }

    public void setTokenDataErrorStatus(TokenDataStatus tokenDataStatus) {
        this.tokenDataErrorStatus = tokenDataStatus;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n***************").append(this.getClass().getSimpleName()).append("**********************\n");
        stringBuilder.append("ParsedData=").append(this.getParsedData()).append("\n");
        return stringBuilder.toString();
    }
}

