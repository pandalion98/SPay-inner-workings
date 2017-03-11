package com.samsung.android.spayfw.payprovider.mastercard.tds.network;

import android.text.TextUtils;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder.TdsRequest;
import com.samsung.android.spayfw.utils.AsyncNetworkHttpClient;
import javax.net.ssl.SSLSocketFactory;

public class McTdsRequestor {
    private static final int DEFAULT_CONNECTION_TIMEOUT = 10000;
    private static final int DEFAULT_RESPONSE_TIMEOUT = 10000;
    private static final String TAG = "McTdsRequestor";

    public static boolean sendRequest(TdsRequest tdsRequest) {
        try {
            validate(tdsRequest);
            String url = tdsRequest.getUrl();
            byte[] dataBytes = tdsRequest.getDataBytes();
            Log.m285d(TAG, "request: url = " + url);
            Log.m285d(TAG, "request: Data = " + new String(dataBytes));
            AsyncNetworkHttpClient asyncNetworkHttpClient = new AsyncNetworkHttpClient();
            SSLSocketFactory instance = McTdsSniSocketfactory.getInstance();
            if (instance == null) {
                return false;
            }
            asyncNetworkHttpClient.setSSLSocketFactory(instance);
            asyncNetworkHttpClient.addHeader("Accept", "application/json");
            asyncNetworkHttpClient.addHeader("Content-Type", "application/json");
            asyncNetworkHttpClient.m1264d(DEFAULT_RESPONSE_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT);
            asyncNetworkHttpClient.m1263a(url, dataBytes, "application/json", tdsRequest.getCallbck());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.m286e(TAG, "Exception : " + e.getMessage());
            return false;
        }
    }

    private static void validate(TdsRequest tdsRequest) {
        if (tdsRequest == null) {
            throw new Exception("request obj null");
        } else if (TextUtils.isEmpty(tdsRequest.getUrl())) {
            throw new Exception("Url null");
        } else if (tdsRequest.getDataBytes() == null) {
            throw new Exception("request obj bytes null");
        } else if (tdsRequest.getCallbck() == null) {
            throw new Exception("callback null");
        }
    }
}
