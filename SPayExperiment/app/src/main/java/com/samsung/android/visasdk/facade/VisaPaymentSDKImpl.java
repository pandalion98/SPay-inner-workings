/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Thread
 *  java.lang.Thread$UncaughtExceptionHandler
 *  java.util.Arrays
 *  java.util.List
 */
package com.samsung.android.visasdk.facade;

import android.content.Context;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.visasdk.a.c;
import com.samsung.android.visasdk.c.a;
import com.samsung.android.visasdk.facade.VisaPaymentSDK;
import com.samsung.android.visasdk.facade.data.ApduResponse;
import com.samsung.android.visasdk.facade.data.CvmMode;
import com.samsung.android.visasdk.facade.data.DeviceData;
import com.samsung.android.visasdk.facade.data.EnrollPanRequest;
import com.samsung.android.visasdk.facade.data.PaymentDataRequest;
import com.samsung.android.visasdk.facade.data.ProvisionAckRequest;
import com.samsung.android.visasdk.facade.data.ProvisionRequestWithEnrollId;
import com.samsung.android.visasdk.facade.data.ProvisionRequestWithPanData;
import com.samsung.android.visasdk.facade.data.ProvisionResponse;
import com.samsung.android.visasdk.facade.data.ReplenishAckRequest;
import com.samsung.android.visasdk.facade.data.ReplenishRequest;
import com.samsung.android.visasdk.facade.data.TokenData;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.data.TokenStatus;
import com.samsung.android.visasdk.facade.data.TransactionError;
import com.samsung.android.visasdk.facade.data.TransactionStatus;
import com.samsung.android.visasdk.facade.data.UpdateReason;
import com.samsung.android.visasdk.facade.exception.InitializationException;
import com.samsung.android.visasdk.facade.exception.PaywaveUncaughtExceptionHandler;
import com.samsung.android.visasdk.facade.exception.TokenInvalidException;
import com.samsung.android.visasdk.facade.exception.TokenKeyInvalidException;
import com.samsung.android.visasdk.paywave.b;
import com.samsung.android.visasdk.paywave.data.CardMetadataUpdateResponse;
import com.samsung.android.visasdk.paywave.data.EnrollDeviceCerts;
import com.samsung.android.visasdk.paywave.data.TVL;
import com.samsung.android.visasdk.paywave.model.LcmParams;
import com.samsung.android.visasdk.paywave.model.LcmTokenRequest;
import com.samsung.android.visasdk.paywave.model.TokenInfo;
import com.visa.tainterface.VisaTAController;
import java.util.Arrays;
import java.util.List;

public class VisaPaymentSDKImpl
implements VisaPaymentSDK {
    public static final String CONSOLE_LOGGER = "Console-Logger";
    private static final boolean DEBUG = false;
    public static final String FILE_LOGGER = "File-Logger";
    private static final String TAG = "VisaPaymentSDKImpl";
    private static byte[] dbKey;
    private static Context mContext;
    private static VisaPaymentSDK mVisaPaymentSdk;
    private com.samsung.android.visasdk.d.a mTokenProcessor = null;
    private b mVcpcsProcessor = null;

    static {
        DEBUG = c.LOG_DEBUG;
        mVisaPaymentSdk = null;
        dbKey = null;
    }

    private VisaPaymentSDKImpl(Context context, Bundle bundle) {
        if (context == null) {
            throw new InitializationException("context is null");
        }
        Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new PaywaveUncaughtExceptionHandler());
        VisaTAController.bv(context);
        this.mVcpcsProcessor = b.a(context, bundle);
        if (this.mVcpcsProcessor == null) {
            throw new InitializationException("cannot initialize vcpcs processor");
        }
        this.mTokenProcessor = com.samsung.android.visasdk.d.a.at(context);
        if (this.mTokenProcessor == null) {
            throw new InitializationException("cannot initialize token processor");
        }
    }

    public static byte[] getDbPassword() {
        return dbKey;
    }

    public static VisaPaymentSDK getInstance() {
        Class<VisaPaymentSDKImpl> class_ = VisaPaymentSDKImpl.class;
        synchronized (VisaPaymentSDKImpl.class) {
            VisaPaymentSDK visaPaymentSDK = VisaPaymentSDKImpl.getInstance(null);
            // ** MonitorExit[var2] (shouldn't be in output)
            return visaPaymentSDK;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static VisaPaymentSDK getInstance(Bundle bundle) {
        Class<VisaPaymentSDKImpl> class_ = VisaPaymentSDKImpl.class;
        synchronized (VisaPaymentSDKImpl.class) {
            if (mVisaPaymentSdk != null) return mVisaPaymentSdk;
            if (mContext == null) {
                throw new InitializationException("context is null");
            }
            mVisaPaymentSdk = new VisaPaymentSDKImpl(mContext, bundle);
            if (mVisaPaymentSdk != null) return mVisaPaymentSdk;
            throw new InitializationException("cannot initialize VisaPaymentSdk");
        }
    }

    public static void initialize(Context context, byte[] arrby) {
        mContext = context;
        dbKey = arrby;
    }

    public static void resetDbPassword() {
        Arrays.fill((byte[])dbKey, (byte)0);
        dbKey = null;
    }

    @Override
    public EnrollPanRequest constructEnrollRequest(byte[] arrby) {
        if (arrby == null || arrby.length <= 0) {
            a.d(TAG, "enroll request data is null");
            return null;
        }
        return this.mTokenProcessor.constructEnrollRequest(arrby);
    }

    @Override
    public LcmTokenRequest constructLcmRequest(LcmParams lcmParams) {
        return null;
    }

    @Override
    public PaymentDataRequest constructPaymentDataRequest(String string, TokenKey tokenKey, String string2, String string3) {
        return this.mVcpcsProcessor.constructPaymentDataRequest(string, tokenKey, string2, string3);
    }

    @Override
    public ProvisionAckRequest constructProvisionAck(TokenKey tokenKey) {
        if (tokenKey == null) {
            throw new TokenInvalidException("token key is null");
        }
        return this.mTokenProcessor.constructProvisionAck(tokenKey);
    }

    @Override
    public ProvisionRequestWithEnrollId constructProvisionRequest(byte[] arrby) {
        return null;
    }

    @Override
    public ProvisionRequestWithPanData constructProvisionRequest(byte[] arrby, byte[] arrby2) {
        return null;
    }

    @Override
    public ReplenishAckRequest constructReplenishAcknowledgementRequest(TokenKey tokenKey) {
        if (tokenKey != null && tokenKey.getTokenId() > 0L) {
            return this.mTokenProcessor.constructReplenishAcknowledgementRequest(tokenKey);
        }
        throw new TokenKeyInvalidException("cannot find the token by token key");
    }

    @Override
    public ReplenishRequest constructReplenishRequest(TokenKey tokenKey) {
        if (tokenKey != null && tokenKey.getTokenId() > 0L) {
            return this.mTokenProcessor.constructReplenishRequest(tokenKey);
        }
        throw new TokenKeyInvalidException("cannot find the token by token key");
    }

    @Override
    public void deleteAllTokensLocally() {
    }

    @Override
    public void deleteToken(TokenKey tokenKey) {
        if (tokenKey == null) {
            a.e(TAG, "token key is null, cannot delete token");
            return;
        }
        this.mTokenProcessor.deleteToken(tokenKey);
    }

    @Override
    public void enableThm(boolean bl) {
    }

    @Override
    public List<TokenData> getAllTokenData() {
        return null;
    }

    @Override
    public EnrollDeviceCerts getCerts() {
        return null;
    }

    @Override
    public CvmMode getCvmVerificationMode() {
        return null;
    }

    @Override
    public DeviceData getDeviceData() {
        return null;
    }

    @Override
    public String getEnrollPANTemplate() {
        return null;
    }

    @Override
    public int getMaxTvlRecords() {
        return 0;
    }

    @Override
    public TokenKey getSelectedCard() {
        return null;
    }

    @Override
    public TokenData getTokenData(TokenKey tokenKey) {
        return null;
    }

    @Override
    public TokenKey getTokenKeyForProvisionedToken(String string) {
        return null;
    }

    @Override
    public Bundle getTokenMetaData(TokenKey tokenKey) {
        return this.mTokenProcessor.getTokenMetaData(tokenKey);
    }

    @Override
    public String getTokenStatus(TokenKey tokenKey) {
        if (tokenKey == null) {
            return TokenStatus.NOT_FOUND.getStatus();
        }
        return this.mTokenProcessor.getTokenStatus(tokenKey);
    }

    @Override
    public List<TVL> getTvlLog(TokenKey tokenKey) {
        return null;
    }

    @Override
    public boolean isCvmVerified() {
        return this.mVcpcsProcessor.isCvmVerified();
    }

    @Override
    public boolean isMstSupported(TokenKey tokenKey) {
        return this.mTokenProcessor.isMstSupported(tokenKey);
    }

    @Override
    public boolean isThmEnabled() {
        return false;
    }

    @Override
    public boolean prepareMstData() {
        return this.mVcpcsProcessor.prepareMstData();
    }

    @Override
    public ApduResponse processCommandApdu(byte[] arrby, Bundle bundle, boolean bl) {
        if (this.mVcpcsProcessor == null) {
            return new ApduResponse(-9);
        }
        return this.mVcpcsProcessor.c(arrby, bl);
    }

    @Override
    public void processInAppTransactionComplete(TokenKey tokenKey, String string, boolean bl) {
        this.mVcpcsProcessor.processInAppTransactionComplete(tokenKey, string, bl);
    }

    @Override
    public boolean processReplenishmentResponse(TokenKey tokenKey, TokenInfo tokenInfo) {
        if (tokenKey != null && tokenKey.getTokenId() > 0L && tokenInfo != null) {
            return this.mTokenProcessor.processReplenishmentResponse(tokenKey, tokenInfo);
        }
        throw new TokenKeyInvalidException("cannot find the token by token key");
    }

    @Override
    public TransactionStatus processTransactionComplete(TokenKey tokenKey) {
        if (this.mVcpcsProcessor == null) {
            return new TransactionStatus(TransactionError.OTHER_ERROR, false);
        }
        return this.mVcpcsProcessor.processTransactionComplete(tokenKey);
    }

    @Override
    public void resumeToken(TokenKey tokenKey) {
        if (tokenKey == null) {
            return;
        }
        this.mTokenProcessor.updateTokenStatus(tokenKey, TokenStatus.ACTIVE);
    }

    @Override
    public void selectCard(TokenKey tokenKey) {
        if (this.mVcpcsProcessor == null) {
            a.e(TAG, "card is not selected, processor is null");
            return;
        }
        this.mVcpcsProcessor.c(tokenKey);
        a.d(TAG, "tokenStatus=" + this.getTokenStatus(tokenKey));
    }

    @Override
    public void setCvmVerificationMode(CvmMode cvmMode) {
        if (this.mVcpcsProcessor == null) {
            return;
        }
        this.mVcpcsProcessor.setCvmVerificationMode(cvmMode);
    }

    @Override
    public void setCvmVerified(boolean bl) {
        this.mVcpcsProcessor.setCvmVerified(bl);
    }

    @Override
    public void setMaxTvlRecords(int n2) {
    }

    @Override
    public void setPasscode(String string) {
    }

    @Override
    public boolean shouldTapAndGo(TokenKey tokenKey) {
        return this.mVcpcsProcessor.shouldTapAndGo(tokenKey);
    }

    @Override
    public TokenKey storeProvisionedToken(ProvisionResponse provisionResponse, String string) {
        if (provisionResponse == null) {
            a.e(TAG, "enroll request data is null");
            return null;
        }
        if (DEBUG) {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            a.d(TAG, "VTS provisioned data= " + gson.toJson((Object)provisionResponse));
        }
        return this.mTokenProcessor.storeProvisionedToken(provisionResponse, string);
    }

    @Override
    public void suspendToken(TokenKey tokenKey, UpdateReason updateReason) {
        if (tokenKey == null) {
            a.e(TAG, "token key is null, cannot suspend token");
            return;
        }
        this.mTokenProcessor.updateTokenStatus(tokenKey, TokenStatus.SUSPENDED);
    }

    @Override
    public boolean tokensExist() {
        return false;
    }

    @Override
    public void transactionComplete(boolean bl) {
        this.mVcpcsProcessor.l(bl);
    }

    @Override
    public boolean updateCardMetaData(CardMetadataUpdateResponse cardMetadataUpdateResponse) {
        return false;
    }

    @Override
    public boolean updateTokenStatus(TokenKey tokenKey, TokenStatus tokenStatus) {
        if (tokenKey == null || tokenStatus == null) {
            throw new TokenKeyInvalidException("cannot find token key");
        }
        return this.mTokenProcessor.updateTokenStatus(tokenKey, tokenStatus);
    }

    @Override
    public boolean verifyPasscode(String string) {
        return false;
    }
}

