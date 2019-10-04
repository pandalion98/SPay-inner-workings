/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.List
 */
package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.model.TagInfo;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.model.token.DataGroup;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TLVHelper;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import java.util.List;

public class TokenMetaInfoParser
extends DataGroup {
    private String applicationIndentifier;
    private String deviceId;
    private int tokenDataMajorVersion;
    private String tokenDataMinorVersion;
    private String tokenStatus;
    private String walletCode;

    public String getApplicationIndentifier() {
        return this.applicationIndentifier;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public int getTokenDataMajorVersion() {
        return this.tokenDataMajorVersion;
    }

    public String getTokenDataMinorVersion() {
        return this.tokenDataMinorVersion;
    }

    public String getTokenDataVersion() {
        return this.tokenDataMajorVersion + "." + this.tokenDataMinorVersion;
    }

    public String getTokenStatus() {
        return this.tokenStatus;
    }

    public String getWalletCode() {
        return this.walletCode;
    }

    @Override
    public void init(String string) {
        super.init(string, "#S9500#9500", "#E9500#");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void parseDataGroup() {
        if (this.isDataGroupMalformed()) return;
        for (TagInfo var4_2 : new TLVHelper().parseTLV(this.getParsedData())) {
            var5_3 = var4_2.getTagName().equalsIgnoreCase("41");
            if (!var5_3) ** GOTO lbl21
        }
        ** GOTO lbl42
        {
            try {
                var8_6 = var4_2.getTagValue();
                var9_7 = var8_6.substring(0, 2);
                var10_8 = var8_6.substring(2, 4);
                this.tokenDataMajorVersion = Integer.parseInt((String)var9_7);
                var12_10 = Integer.parseInt((String)var10_8) < 10 ? "0" + Integer.parseInt((String)var10_8) : (var11_9 = Integer.parseInt((String)var10_8) + "");
                this.tokenDataMinorVersion = var12_10;
                continue;
            }
            catch (Exception var7_5) {
                this.tokenDataMajorVersion = -1;
                this.tokenDataMinorVersion = "0";
                this.setTokenDataErrorStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
                continue;
lbl21: // 1 sources:
                try {
                    if (var4_2.getTagName().equalsIgnoreCase("45")) {
                        this.applicationIndentifier = var4_2.getTagValue();
                        continue;
                    }
                    if (var4_2.getTagName().equalsIgnoreCase("46")) {
                        this.walletCode = Util.getSafePrintChars(Util.fromHexString(var4_2.getTagValue()));
                        continue;
                    }
                    if (var4_2.getTagName().equalsIgnoreCase("47")) {
                        this.deviceId = var4_2.getTagValue();
                        continue;
                    }
                    if (var4_2.getTagName().equalsIgnoreCase("48")) {
                        var6_4 = var4_2.getTagValue();
                        if (var6_4.equalsIgnoreCase("00")) {
                            this.isTokenDataContainsEMV = true;
                            continue;
                        }
                        if (!var6_4.equalsIgnoreCase("01")) continue;
                        this.isTokenDataContainsEMV = false;
                        continue;
                    }
                    if (!var4_2.getTagName().equalsIgnoreCase("49")) continue;
                    this.tokenStatus = var4_2.getTagValue();
                    continue;
lbl42: // 1 sources:
                    var3_12 = new String[]{this.applicationIndentifier, this.walletCode, this.deviceId, Boolean.valueOf((boolean)this.isTokenDataContainsEMV).toString()};
                    TokenDataParser.throwExceptionIfEmpty(var3_12);
                    return;
                }
                catch (Exception var1_11) {
                    this.setTokenDataErrorStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.BUSINESS_DGI_DATA_PARSE_ERROR));
                    return;
                    break;
                }
            }
        }
    }

    public void setApplicationIndentifier(String string) {
        this.applicationIndentifier = string;
    }

    public void setDeviceId(String string) {
        this.deviceId = string;
    }

    public void setTokenDataMajorVersion(int n2) {
        this.tokenDataMajorVersion = n2;
    }

    public void setTokenDataMinorVersion(String string) {
        this.tokenDataMinorVersion = string;
    }

    public void setTokenStatus(String string) {
        this.tokenStatus = string;
    }

    public void setWalletCode(String string) {
        this.walletCode = string;
    }

    @Override
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

