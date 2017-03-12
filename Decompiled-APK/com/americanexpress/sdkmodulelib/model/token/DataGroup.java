package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.exception.TokenDataParseException;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TLVHelper;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;

public abstract class DataGroup {
    private String endTag;
    protected boolean isTokenDataContainsEMV;
    private String parsedData;
    private String startTag;
    private String tokenDataBlob;
    private TokenDataStatus tokenDataErrorStatus;

    public abstract void init(String str);

    public abstract void parseDataGroup();

    public DataGroup() {
        this.isTokenDataContainsEMV = false;
    }

    protected void init(String str, String str2, String str3) {
        this.tokenDataBlob = str.toUpperCase();
        this.startTag = str2;
        this.endTag = str3;
        try {
            this.parsedData = getDataGroup(str2, str3);
        } catch (TokenDataParseException e) {
            setTokenDataErrorStatus(e.getTokenDataStatus());
        }
        parseDataGroup();
    }

    protected String getDataGroup(String str, String str2) {
        int indexOf = this.tokenDataBlob.indexOf(str);
        int indexOf2 = this.tokenDataBlob.indexOf(str2);
        if (indexOf == -1 || indexOf2 == -1) {
            throw new TokenDataParseException(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
        }
        String substring = this.tokenDataBlob.substring(indexOf + str.length(), indexOf2);
        try {
            return new TLVHelper().getFirstTag("f1" + substring).getTagValue();
        } catch (Exception e) {
            throw new TokenDataParseException(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
        }
    }

    public boolean isDataGroupMalformed() {
        return getTokenDataErrorStatus() != null;
    }

    public TokenDataStatus getTokenDataErrorStatus() {
        return this.tokenDataErrorStatus;
    }

    public void setTokenDataErrorStatus(TokenDataStatus tokenDataStatus) {
        this.tokenDataErrorStatus = tokenDataStatus;
    }

    public String getParsedData() {
        return this.parsedData;
    }

    public void setParsedData(String str) {
        this.parsedData = str;
    }

    public String getTokenDataBlob() {
        return this.tokenDataBlob;
    }

    public void setTokenDataBlob(String str) {
        this.tokenDataBlob = str;
    }

    public String getStartTag() {
        return this.startTag;
    }

    public boolean isTokenDataContainsEMV() {
        return this.isTokenDataContainsEMV;
    }

    public void setIsTokenDataContainsEMV(boolean z) {
        this.isTokenDataContainsEMV = z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n***************").append(getClass().getSimpleName()).append("**********************\n");
        stringBuilder.append("ParsedData=").append(getParsedData()).append("\n");
        return stringBuilder.toString();
    }
}
