package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.sdkmodulelib.model.TagInfo;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TLVHelper;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;

public class TokenMetaInfoParser extends DataGroup {
    private String applicationIndentifier;
    private String deviceId;
    private int tokenDataMajorVersion;
    private String tokenDataMinorVersion;
    private String tokenStatus;
    private String walletCode;

    public void init(String str) {
        super.init(str, Constants.TOKEN_META_START_TAG, Constants.TOKEN_META_END_TAG);
    }

    public void parseDataGroup() {
        if (!isDataGroupMalformed()) {
            for (TagInfo tagInfo : new TLVHelper().parseTLV(getParsedData())) {
                String tagValue;
                if (tagInfo.getTagName().equalsIgnoreCase(Constants.TOKEN_META_VERSION_TAG)) {
                    try {
                        tagValue = tagInfo.getTagValue();
                        String substring = tagValue.substring(0, 2);
                        tagValue = tagValue.substring(2, 4);
                        this.tokenDataMajorVersion = Integer.parseInt(substring);
                        this.tokenDataMinorVersion = Integer.parseInt(tagValue) < 10 ? TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE + Integer.parseInt(tagValue) : Integer.parseInt(tagValue) + BuildConfig.FLAVOR;
                    } catch (Exception e) {
                        try {
                            this.tokenDataMajorVersion = -1;
                            this.tokenDataMinorVersion = TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE;
                            setTokenDataErrorStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
                        } catch (Exception e2) {
                            setTokenDataErrorStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
                            return;
                        }
                    }
                } else if (tagInfo.getTagName().equalsIgnoreCase(Constants.TOKEN_META_AID_TAG)) {
                    this.applicationIndentifier = tagInfo.getTagValue();
                } else if (tagInfo.getTagName().equalsIgnoreCase(Constants.TOKEN_META_WALLETID_TAG)) {
                    this.walletCode = Util.getSafePrintChars(Util.fromHexString(tagInfo.getTagValue()));
                } else if (tagInfo.getTagName().equalsIgnoreCase(Constants.TOKEN_META_DEVICEID_TAG)) {
                    this.deviceId = tagInfo.getTagValue();
                } else if (tagInfo.getTagName().equalsIgnoreCase(Constants.TOKEN_META_SUPPRESS_EMV_TAG)) {
                    tagValue = tagInfo.getTagValue();
                    if (tagValue.equalsIgnoreCase(HCEClientConstants.HEX_ZERO_BYTE)) {
                        this.isTokenDataContainsEMV = true;
                    } else if (tagValue.equalsIgnoreCase(HCEClientConstants.API_INDEX_TOKEN_OPEN)) {
                        this.isTokenDataContainsEMV = false;
                    }
                } else if (tagInfo.getTagName().equalsIgnoreCase(Constants.TOKEN_META_TOKEN_STATUS_TAG)) {
                    this.tokenStatus = tagInfo.getTagValue();
                }
            }
            TokenDataParser.throwExceptionIfEmpty(this.applicationIndentifier, this.walletCode, this.deviceId, Boolean.valueOf(this.isTokenDataContainsEMV).toString());
        }
    }

    public int getTokenDataMajorVersion() {
        return this.tokenDataMajorVersion;
    }

    public void setTokenDataMajorVersion(int i) {
        this.tokenDataMajorVersion = i;
    }

    public String getTokenDataMinorVersion() {
        return this.tokenDataMinorVersion;
    }

    public void setTokenDataMinorVersion(String str) {
        this.tokenDataMinorVersion = str;
    }

    public String getApplicationIndentifier() {
        return this.applicationIndentifier;
    }

    public void setApplicationIndentifier(String str) {
        this.applicationIndentifier = str;
    }

    public String getWalletCode() {
        return this.walletCode;
    }

    public void setWalletCode(String str) {
        this.walletCode = str;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String str) {
        this.deviceId = str;
    }

    public String getTokenDataVersion() {
        return this.tokenDataMajorVersion + "." + this.tokenDataMinorVersion;
    }

    public String getTokenStatus() {
        return this.tokenStatus;
    }

    public void setTokenStatus(String str) {
        this.tokenStatus = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append("\ntokenDataMajorVersion=").append(this.tokenDataMajorVersion);
        stringBuilder.append("\ntokenDataMinorVersion=").append(this.tokenDataMinorVersion);
        stringBuilder.append("\ntokenStatus=").append(this.tokenStatus);
        stringBuilder.append("\nisTokenDataContainsEMV=").append(this.isTokenDataContainsEMV);
        stringBuilder.append("\napplicationIndentifier=").append(this.applicationIndentifier);
        stringBuilder.append("\nwalletCode=").append(this.walletCode);
        stringBuilder.append("\ndeviceId=").append(this.deviceId);
        return stringBuilder.toString();
    }
}
