/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.payment;

import com.americanexpress.sdkmodulelib.model.TokenDataResponse;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.model.TokenDataVersionResponse;
import com.americanexpress.sdkmodulelib.model.TokenStatusResponse;

public interface TokenDataManager {
    public TokenDataStatus activateToken(String var1);

    public TokenDataStatus deleteToken(String var1);

    public String getClientVersion();

    public TokenDataResponse getTokenData(String var1);

    public TokenDataVersionResponse getTokenDataVersion(String var1);

    public TokenStatusResponse getTokenStatus(String var1);

    public TokenDataStatus resumeToken(String var1);

    public TokenDataStatus suspendToken(String var1);

    public TokenDataStatus updateTokenData(String var1, String var2, String var3, String var4, String var5, String var6);
}

