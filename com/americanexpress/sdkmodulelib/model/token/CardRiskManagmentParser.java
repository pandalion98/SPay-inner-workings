package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.model.TagInfo;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TLVHelper;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;

public abstract class CardRiskManagmentParser extends DataGroup {
    private String serviceCode;

    public abstract void init(String str);

    public void parseDataGroup() {
        if (!isDataGroupMalformed()) {
            try {
                for (TagInfo tagInfo : new TLVHelper().parseTLV(getParsedData())) {
                    if (tagInfo.getTagName().equalsIgnoreCase(Constants.CARD_VERF_METHOD_LIST_SERVICE_CODE_TAG)) {
                        this.serviceCode = tagInfo.getTagValue();
                    }
                }
                TokenDataParser.throwExceptionIfEmpty(this.serviceCode);
            } catch (Exception e) {
                setTokenDataErrorStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
            }
        }
    }

    public String getServiceCode() {
        return this.serviceCode;
    }

    public void setServiceCode(String str) {
        this.serviceCode = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        return stringBuilder.toString();
    }
}
