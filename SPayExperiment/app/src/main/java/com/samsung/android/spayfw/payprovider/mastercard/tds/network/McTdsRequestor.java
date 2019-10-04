/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  javax.net.ssl.SSLSocketFactory
 */
package com.samsung.android.spayfw.payprovider.mastercard.tds.network;

import android.text.TextUtils;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsSniSocketfactory;
import com.samsung.android.spayfw.utils.a;
import javax.net.ssl.SSLSocketFactory;

public class McTdsRequestor {
    private static final int DEFAULT_CONNECTION_TIMEOUT = 10000;
    private static final int DEFAULT_RESPONSE_TIMEOUT = 10000;
    private static final String TAG = "McTdsRequestor";

    public static boolean sendRequest(McTdsRequestBuilder.TdsRequest tdsRequest) {
        try {
            McTdsRequestor.validate(tdsRequest);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            c.e(TAG, "Exception : " + exception.getMessage());
            return false;
        }
        String string = tdsRequest.getUrl();
        byte[] arrby = tdsRequest.getDataBytes();
        c.d(TAG, "request: url = " + string);
        c.d(TAG, "request: Data = " + new String(arrby));
        a a2 = new a();
        McTdsSniSocketfactory mcTdsSniSocketfactory = McTdsSniSocketfactory.getInstance();
        if (mcTdsSniSocketfactory == null) {
            return false;
        }
        a2.setSSLSocketFactory(mcTdsSniSocketfactory);
        a2.addHeader("Accept", "application/json");
        a2.addHeader("Content-Type", "application/json");
        a2.d(10000, 10000);
        a2.a(string, arrby, "application/json", tdsRequest.getCallbck());
        return true;
    }

    private static void validate(McTdsRequestBuilder.TdsRequest tdsRequest) {
        if (tdsRequest == null) {
            throw new Exception("request obj null");
        }
        if (TextUtils.isEmpty((CharSequence)tdsRequest.getUrl())) {
            throw new Exception("Url null");
        }
        if (tdsRequest.getDataBytes() == null) {
            throw new Exception("request obj bytes null");
        }
        if (tdsRequest.getCallbck() == null) {
            throw new Exception("callback null");
        }
    }
}

