package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.model.TagInfo;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TLVHelper;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;

public abstract class IssuerApplicationParser extends DataGroup {
    private String cardVerificationResult;
    private String issuerApplicationData;

    public abstract void init(String str);

    public IssuerApplicationParser() {
        this.cardVerificationResult = null;
        this.issuerApplicationData = null;
    }

    public void parseDataGroup() {
        if (!isDataGroupMalformed()) {
            try {
                for (TagInfo tagInfo : new TLVHelper().parseTLV(getParsedData())) {
                    if (tagInfo.getTagName().equalsIgnoreCase(Constants.ISSUER_APP_DATA_TAG)) {
                        String tagValue = tagInfo.getTagValue();
                        setIssuerApplicationData(tagValue);
                        if (tagValue != null && tagValue.length() > 0) {
                            setCardVerificationResult(tagValue.substring(6, tagValue.length()));
                        }
                    }
                }
                TokenDataParser.throwExceptionIfEmpty(this.issuerApplicationData, this.cardVerificationResult);
            } catch (Exception e) {
                setTokenDataErrorStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
            }
        }
    }

    public String getCardVerificationResult() {
        return this.cardVerificationResult;
    }

    public void setCardVerificationResult(String str) {
        this.cardVerificationResult = str;
    }

    public String getIssuerApplicationData() {
        return this.issuerApplicationData;
    }

    public void setIssuerApplicationData(String str) {
        this.issuerApplicationData = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append("\ncardVerificationResult=").append(this.cardVerificationResult);
        return stringBuilder.toString();
    }
}
