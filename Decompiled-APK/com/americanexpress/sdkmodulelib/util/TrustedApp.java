package com.americanexpress.sdkmodulelib.util;

import android.content.Context;
import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import java.util.HashMap;

public interface TrustedApp {
    String activateToken(String str);

    void clearLUPC();

    String decryptTokenData(String str);

    TokenDataRecord generateInAppPaymentPayload(Object obj, String str, TokenDataRecord tokenDataRecord);

    Context getApplicationContext();

    String getNFCCryptogram(int i, int i2, HashMap<String, String> hashMap);

    TokenDataRecord processTransaction(int i, TokenDataRecord tokenDataRecord);

    String resumeToken(String str);

    String suspendToken(String str);
}
