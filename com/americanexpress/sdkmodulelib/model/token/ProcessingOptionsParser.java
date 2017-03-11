package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.model.TagInfo;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TLVHelper;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;

public abstract class ProcessingOptionsParser extends DataGroup {
    private String applicationFileLocator;
    private String applicationInterchangeProfile;

    public abstract void init(String str);

    public void parseDataGroup() {
        if (!isDataGroupMalformed()) {
            try {
                for (TagInfo tagInfo : new TLVHelper().parseTLV(getParsedData())) {
                    if (tagInfo.getTagName().equalsIgnoreCase(Constants.APPLICATION_FILE_LOCATOR_AIP_TAG)) {
                        setApplicationInterchangeProfile(tagInfo.getTagValue());
                    } else if (tagInfo.getTagName().equalsIgnoreCase(Constants.APPLICATION_FILE_LOCATOR_AFL_TAG)) {
                        setApplicationFileLocator(tagInfo.getTagValue());
                    }
                }
                TokenDataParser.throwExceptionIfEmpty(this.applicationInterchangeProfile, this.applicationFileLocator);
            } catch (Exception e) {
                setTokenDataErrorStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
            }
        }
    }

    public String getApplicationInterchangeProfile() {
        return this.applicationInterchangeProfile;
    }

    public void setApplicationInterchangeProfile(String str) {
        this.applicationInterchangeProfile = str;
    }

    public String getApplicationFileLocator() {
        return this.applicationFileLocator;
    }

    public void setApplicationFileLocator(String str) {
        this.applicationFileLocator = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append("\napplicationInterchangeProfile=").append(this.applicationInterchangeProfile);
        stringBuilder.append("\napplicationFileLocator=").append(this.applicationFileLocator);
        return stringBuilder.toString();
    }
}
