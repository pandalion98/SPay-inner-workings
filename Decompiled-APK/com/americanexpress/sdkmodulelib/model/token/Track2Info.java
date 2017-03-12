package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.model.TagInfo;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TLVHelper;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;

public abstract class Track2Info extends DataGroup {
    private String accountRefNumber;
    private String track2EquivalentData;

    public abstract void init(String str);

    public void parseDataGroup() {
        if (!isDataGroupMalformed()) {
            try {
                for (TagInfo tagInfo : new TLVHelper().parseTLV(getParsedData())) {
                    if (tagInfo.getTagName().equalsIgnoreCase(Constants.TRACK2_EQUIVALENT_DATA)) {
                        this.track2EquivalentData = tagInfo.getTagValue();
                        setAccountRefNumber(parseAccountNumber(tagInfo.getTagValue()));
                    }
                }
                TokenDataParser.throwExceptionIfEmpty(this.accountRefNumber);
            } catch (Exception e) {
                setTokenDataErrorStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
            }
        }
    }

    private String parseAccountNumber(String str) {
        if (str == null || str.length() == 0 || str.indexOf("D") == -1) {
            return null;
        }
        return str.substring(0, str.indexOf("D"));
    }

    public String getAccountRefNumber() {
        return this.accountRefNumber;
    }

    public void setAccountRefNumber(String str) {
        this.accountRefNumber = str;
    }

    public String getTrack2EquivalentData() {
        return this.track2EquivalentData;
    }

    public void setTrack2EquivalentData(String str) {
        this.track2EquivalentData = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append("\naccountRefNumber=").append(this.accountRefNumber);
        return stringBuilder.toString();
    }
}
