package com.americanexpress.sdkmodulelib.payment;

import com.americanexpress.sdkmodulelib.model.TokenDataResponse;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.model.TokenDataVersionResponse;
import com.americanexpress.sdkmodulelib.model.TokenStatusResponse;

public interface TokenDataManager {
    TokenDataStatus activateToken(String str);

    TokenDataStatus deleteToken(String str);

    String getClientVersion();

    TokenDataResponse getTokenData(String str);

    TokenDataVersionResponse getTokenDataVersion(String str);

    TokenStatusResponse getTokenStatus(String str);

    TokenDataStatus resumeToken(String str);

    TokenDataStatus suspendToken(String str);

    TokenDataStatus updateTokenData(String str, String str2, String str3, String str4, String str5, String str6);
}
