/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 */
package com.americanexpress.sdkmodulelib.util;

import android.content.Context;
import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import java.util.HashMap;

public interface TrustedApp {
    public String activateToken(String var1);

    public void clearLUPC();

    public String decryptTokenData(String var1);

    public TokenDataRecord generateInAppPaymentPayload(Object var1, String var2, TokenDataRecord var3);

    public Context getApplicationContext();

    public String getNFCCryptogram(int var1, int var2, HashMap<String, String> var3);

    public TokenDataRecord processTransaction(int var1, TokenDataRecord var2);

    public String resumeToken(String var1);

    public String suspendToken(String var1);
}

