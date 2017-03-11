package com.samsung.android.spayfw.payprovider.mastercard.tds.network.callback;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager;
import com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsRegistrationCodeResponse;
import com.samsung.android.spayfw.utils.AsyncNetworkHttpClient.AsyncNetworkHttpClient;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.x509.DisplayText;

public class McTdsRegistrationCode1Callbck implements AsyncNetworkHttpClient {
    private static final String TAG = "McTdsRegistrationCode1Callbck";
    private static final String TDS_TAG_ERROR = "e_McTdsRegistrationCode1Callbck";
    private static final String TDS_TAG_INFO = "i_McTdsRegistrationCode1Callbck";
    private final long mCardMasterId;

    public McTdsRegistrationCode1Callbck(long j) {
        this.mCardMasterId = j;
    }

    public void onComplete(int i, Map<String, List<String>> map, byte[] bArr) {
        int i2;
        McTdsRegistrationCodeResponse mcTdsRegistrationCodeResponse;
        if (McProvider.getContext() == null) {
            Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsRegistrationCode1Callbck: " + "Err. Context missing. Cannot store registrationCode1 in db");
            return;
        }
        McTdsManager instance = McTdsManager.getInstance(this.mCardMasterId);
        try {
            Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " McTdsRegistrationCode1Callbck: RegistrationCode1Callbck statusCode : " + i);
            switch (i) {
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    i2 = 0;
                    break;
                default:
                    i2 = -36;
                    break;
            }
            if (bArr == null) {
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsRegistrationCode1Callbck responseData empty received");
                return;
            }
            mcTdsRegistrationCodeResponse = null;
            McTdsRegistrationCodeResponse mcTdsRegistrationCodeResponse2 = (McTdsRegistrationCodeResponse) new Gson().fromJson(new String(bArr, StandardCharsets.UTF_8), McTdsRegistrationCodeResponse.class);
            if (mcTdsRegistrationCodeResponse2 == null) {
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsRegistrationCode1Callbck: Error:  Empty payload");
            } else if (!TextUtils.isEmpty(mcTdsRegistrationCodeResponse2.getErrorCode()) || i2 != 0) {
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsRegistrationCode1Callbck: ErrorCode: " + mcTdsRegistrationCodeResponse2.getErrorCode() + " resultCode:" + i2);
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " registrationCode1 : FAILED making re-try");
                McTdsManager.setRegistrationState(this.mCardMasterId, RegistrationState.TDS_NOT_REGISTERED);
                McTdsManager.addTokenToRegPendingList(Long.valueOf(this.mCardMasterId));
                McTdsTimerUtil.startTdsTimer();
            } else if (TextUtils.isEmpty(mcTdsRegistrationCodeResponse2.getRegistrationCode1())) {
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsRegistrationCode1Callbck:  RegistrationCode1 missing in response ");
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " registrationCode1 : FAILED making re-try");
                McTdsManager.setRegistrationState(this.mCardMasterId, RegistrationState.TDS_NOT_REGISTERED);
                McTdsManager.addTokenToRegPendingList(Long.valueOf(this.mCardMasterId));
                McTdsTimerUtil.startTdsTimer();
            } else {
                String registrationCode1 = mcTdsRegistrationCodeResponse2.getRegistrationCode1();
                Log.m285d(TAG, "tokenId: " + this.mCardMasterId + " McTdsRegistrationCode1Callbck: RegistrationCode1 Received: " + registrationCode1);
                String responseHost = mcTdsRegistrationCodeResponse2.getResponseHost();
                if (!TextUtils.isEmpty(responseHost)) {
                    Log.m285d(TAG, "tokenId: " + this.mCardMasterId + " McTdsRegistrationCode1Callbck: Updated responseHost Received: " + responseHost);
                }
                instance.onRegisterCode1(responseHost, registrationCode1);
                Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " registrationCode1: SUCESS");
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " JsonSyntaxException McTdsRegistrationCode1Callbck : ");
            if (mcTdsRegistrationCodeResponse == null) {
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsRegistrationCode1Callbck: Error:  Empty payload");
            } else if (!TextUtils.isEmpty(mcTdsRegistrationCodeResponse.getErrorCode()) || i2 != 0) {
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsRegistrationCode1Callbck: ErrorCode: " + mcTdsRegistrationCodeResponse.getErrorCode() + " resultCode:" + i2);
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " registrationCode1 : FAILED making re-try");
                McTdsManager.setRegistrationState(this.mCardMasterId, RegistrationState.TDS_NOT_REGISTERED);
                McTdsManager.addTokenToRegPendingList(Long.valueOf(this.mCardMasterId));
                McTdsTimerUtil.startTdsTimer();
            } else if (TextUtils.isEmpty(mcTdsRegistrationCodeResponse.getRegistrationCode1())) {
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsRegistrationCode1Callbck:  RegistrationCode1 missing in response ");
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " registrationCode1 : FAILED making re-try");
                McTdsManager.setRegistrationState(this.mCardMasterId, RegistrationState.TDS_NOT_REGISTERED);
                McTdsManager.addTokenToRegPendingList(Long.valueOf(this.mCardMasterId));
                McTdsTimerUtil.startTdsTimer();
            } else {
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " registrationCode1 : FAILED making re-try");
                McTdsManager.setRegistrationState(this.mCardMasterId, RegistrationState.TDS_NOT_REGISTERED);
                McTdsManager.addTokenToRegPendingList(Long.valueOf(this.mCardMasterId));
                McTdsTimerUtil.startTdsTimer();
            }
        } catch (Throwable th) {
            if (mcTdsRegistrationCodeResponse == null) {
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsRegistrationCode1Callbck: Error:  Empty payload");
            } else if (!TextUtils.isEmpty(mcTdsRegistrationCodeResponse.getErrorCode()) || i2 != 0) {
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsRegistrationCode1Callbck: ErrorCode: " + mcTdsRegistrationCodeResponse.getErrorCode() + " resultCode:" + i2);
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " registrationCode1 : FAILED making re-try");
                McTdsManager.setRegistrationState(this.mCardMasterId, RegistrationState.TDS_NOT_REGISTERED);
                McTdsManager.addTokenToRegPendingList(Long.valueOf(this.mCardMasterId));
                McTdsTimerUtil.startTdsTimer();
            } else if (TextUtils.isEmpty(mcTdsRegistrationCodeResponse.getRegistrationCode1())) {
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsRegistrationCode1Callbck:  RegistrationCode1 missing in response ");
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " registrationCode1 : FAILED making re-try");
                McTdsManager.setRegistrationState(this.mCardMasterId, RegistrationState.TDS_NOT_REGISTERED);
                McTdsManager.addTokenToRegPendingList(Long.valueOf(this.mCardMasterId));
                McTdsTimerUtil.startTdsTimer();
            }
        } finally {
            Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " registrationCode1 : FAILED making re-try");
            McTdsManager.setRegistrationState(this.mCardMasterId, RegistrationState.TDS_NOT_REGISTERED);
            McTdsManager.addTokenToRegPendingList(Long.valueOf(this.mCardMasterId));
            McTdsTimerUtil.startTdsTimer();
        }
    }
}
