package com.samsung.android.visasdk.facade;

import android.content.Context;
import android.os.Bundle;
import com.google.gson.GsonBuilder;
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
import com.samsung.android.visasdk.p023a.Version;
import com.samsung.android.visasdk.p025c.Log;
import com.samsung.android.visasdk.p026d.TokenProcessor;
import com.samsung.android.visasdk.paywave.VcpcsProcessor;
import com.samsung.android.visasdk.paywave.data.CardMetadataUpdateResponse;
import com.samsung.android.visasdk.paywave.data.EnrollDeviceCerts;
import com.samsung.android.visasdk.paywave.data.TVL;
import com.samsung.android.visasdk.paywave.model.LcmParams;
import com.samsung.android.visasdk.paywave.model.LcmTokenRequest;
import com.samsung.android.visasdk.paywave.model.TokenInfo;
import com.visa.tainterface.VisaTAController;
import java.util.Arrays;
import java.util.List;

public class VisaPaymentSDKImpl implements VisaPaymentSDK {
    public static final String CONSOLE_LOGGER = "Console-Logger";
    private static final boolean DEBUG;
    public static final String FILE_LOGGER = "File-Logger";
    private static final String TAG = "VisaPaymentSDKImpl";
    private static byte[] dbKey;
    private static Context mContext;
    private static VisaPaymentSDK mVisaPaymentSdk;
    private TokenProcessor mTokenProcessor;
    private VcpcsProcessor mVcpcsProcessor;

    static {
        DEBUG = Version.LOG_DEBUG;
        mVisaPaymentSdk = null;
        dbKey = null;
    }

    public static void initialize(Context context, byte[] bArr) {
        mContext = context;
        dbKey = bArr;
    }

    public static synchronized VisaPaymentSDK getInstance() {
        VisaPaymentSDK instance;
        synchronized (VisaPaymentSDKImpl.class) {
            instance = getInstance(null);
        }
        return instance;
    }

    public static synchronized VisaPaymentSDK getInstance(Bundle bundle) {
        VisaPaymentSDK visaPaymentSDK;
        synchronized (VisaPaymentSDKImpl.class) {
            if (mVisaPaymentSdk == null) {
                if (mContext == null) {
                    throw new InitializationException("context is null");
                }
                mVisaPaymentSdk = new VisaPaymentSDKImpl(mContext, bundle);
                if (mVisaPaymentSdk == null) {
                    throw new InitializationException("cannot initialize VisaPaymentSdk");
                }
            }
            visaPaymentSDK = mVisaPaymentSdk;
        }
        return visaPaymentSDK;
    }

    private VisaPaymentSDKImpl(Context context, Bundle bundle) {
        this.mVcpcsProcessor = null;
        this.mTokenProcessor = null;
        if (context == null) {
            throw new InitializationException("context is null");
        }
        Thread.setDefaultUncaughtExceptionHandler(new PaywaveUncaughtExceptionHandler());
        VisaTAController.bv(context);
        this.mVcpcsProcessor = VcpcsProcessor.m1346a(context, bundle);
        if (this.mVcpcsProcessor == null) {
            throw new InitializationException("cannot initialize vcpcs processor");
        }
        this.mTokenProcessor = TokenProcessor.at(context);
        if (this.mTokenProcessor == null) {
            throw new InitializationException("cannot initialize token processor");
        }
    }

    public static byte[] getDbPassword() {
        return dbKey;
    }

    public static void resetDbPassword() {
        Arrays.fill(dbKey, (byte) 0);
        dbKey = null;
    }

    public TokenKey getSelectedCard() {
        return null;
    }

    public void selectCard(TokenKey tokenKey) {
        if (this.mVcpcsProcessor == null) {
            Log.m1301e(TAG, "card is not selected, processor is null");
            return;
        }
        this.mVcpcsProcessor.m1350c(tokenKey);
        Log.m1300d(TAG, "tokenStatus=" + getTokenStatus(tokenKey));
    }

    public ApduResponse processCommandApdu(byte[] bArr, Bundle bundle, boolean z) {
        if (this.mVcpcsProcessor == null) {
            return new ApduResponse(-9);
        }
        return this.mVcpcsProcessor.m1349c(bArr, z);
    }

    public TransactionStatus processTransactionComplete(TokenKey tokenKey) {
        if (this.mVcpcsProcessor == null) {
            return new TransactionStatus(TransactionError.OTHER_ERROR, DEBUG);
        }
        return this.mVcpcsProcessor.processTransactionComplete(tokenKey);
    }

    public void setCvmVerified(boolean z) {
        this.mVcpcsProcessor.setCvmVerified(z);
    }

    public boolean isCvmVerified() {
        return this.mVcpcsProcessor.isCvmVerified();
    }

    public void setCvmVerificationMode(CvmMode cvmMode) {
        if (this.mVcpcsProcessor != null) {
            this.mVcpcsProcessor.setCvmVerificationMode(cvmMode);
        }
    }

    public void setPasscode(String str) {
    }

    public boolean verifyPasscode(String str) {
        return DEBUG;
    }

    public CvmMode getCvmVerificationMode() {
        return null;
    }

    public void setMaxTvlRecords(int i) {
    }

    public int getMaxTvlRecords() {
        return 0;
    }

    public List<TVL> getTvlLog(TokenKey tokenKey) {
        return null;
    }

    public DeviceData getDeviceData() {
        return null;
    }

    public void enableThm(boolean z) {
    }

    public boolean isThmEnabled() {
        return DEBUG;
    }

    public EnrollDeviceCerts getCerts() {
        return null;
    }

    public String getEnrollPANTemplate() {
        return null;
    }

    public LcmTokenRequest constructLcmRequest(LcmParams lcmParams) {
        return null;
    }

    public EnrollPanRequest constructEnrollRequest(byte[] bArr) {
        if (bArr != null && bArr.length > 0) {
            return this.mTokenProcessor.constructEnrollRequest(bArr);
        }
        Log.m1300d(TAG, "enroll request data is null");
        return null;
    }

    public ProvisionRequestWithPanData constructProvisionRequest(byte[] bArr, byte[] bArr2) {
        return null;
    }

    public ProvisionRequestWithEnrollId constructProvisionRequest(byte[] bArr) {
        return null;
    }

    public TokenKey storeProvisionedToken(ProvisionResponse provisionResponse, String str) {
        if (provisionResponse == null) {
            Log.m1301e(TAG, "enroll request data is null");
            return null;
        }
        if (DEBUG) {
            Log.m1300d(TAG, "VTS provisioned data= " + new GsonBuilder().disableHtmlEscaping().create().toJson((Object) provisionResponse));
        }
        return this.mTokenProcessor.storeProvisionedToken(provisionResponse, str);
    }

    public ProvisionAckRequest constructProvisionAck(TokenKey tokenKey) {
        if (tokenKey != null) {
            return this.mTokenProcessor.constructProvisionAck(tokenKey);
        }
        throw new TokenInvalidException("token key is null");
    }

    public ReplenishRequest constructReplenishRequest(TokenKey tokenKey) {
        if (tokenKey != null && tokenKey.getTokenId() > 0) {
            return this.mTokenProcessor.constructReplenishRequest(tokenKey);
        }
        throw new TokenKeyInvalidException("cannot find the token by token key");
    }

    public ReplenishAckRequest constructReplenishAcknowledgementRequest(TokenKey tokenKey) {
        if (tokenKey != null && tokenKey.getTokenId() > 0) {
            return this.mTokenProcessor.constructReplenishAcknowledgementRequest(tokenKey);
        }
        throw new TokenKeyInvalidException("cannot find the token by token key");
    }

    public boolean processReplenishmentResponse(TokenKey tokenKey, TokenInfo tokenInfo) {
        if (tokenKey != null && tokenKey.getTokenId() > 0 && tokenInfo != null) {
            return this.mTokenProcessor.processReplenishmentResponse(tokenKey, tokenInfo);
        }
        throw new TokenKeyInvalidException("cannot find the token by token key");
    }

    public String getTokenStatus(TokenKey tokenKey) {
        if (tokenKey == null) {
            return TokenStatus.NOT_FOUND.getStatus();
        }
        return this.mTokenProcessor.getTokenStatus(tokenKey);
    }

    public boolean updateTokenStatus(TokenKey tokenKey, TokenStatus tokenStatus) {
        if (tokenKey != null && tokenStatus != null) {
            return this.mTokenProcessor.updateTokenStatus(tokenKey, tokenStatus);
        }
        throw new TokenKeyInvalidException("cannot find token key");
    }

    public void suspendToken(TokenKey tokenKey, UpdateReason updateReason) {
        if (tokenKey == null) {
            Log.m1301e(TAG, "token key is null, cannot suspend token");
        } else {
            this.mTokenProcessor.updateTokenStatus(tokenKey, TokenStatus.SUSPENDED);
        }
    }

    public void deleteToken(TokenKey tokenKey) {
        if (tokenKey == null) {
            Log.m1301e(TAG, "token key is null, cannot delete token");
        } else {
            this.mTokenProcessor.deleteToken(tokenKey);
        }
    }

    public void resumeToken(TokenKey tokenKey) {
        if (tokenKey != null) {
            this.mTokenProcessor.updateTokenStatus(tokenKey, TokenStatus.ACTIVE);
        }
    }

    public boolean tokensExist() {
        return DEBUG;
    }

    public void deleteAllTokensLocally() {
    }

    public TokenData getTokenData(TokenKey tokenKey) {
        return null;
    }

    public List<TokenData> getAllTokenData() {
        return null;
    }

    public boolean updateCardMetaData(CardMetadataUpdateResponse cardMetadataUpdateResponse) {
        return DEBUG;
    }

    public TokenKey getTokenKeyForProvisionedToken(String str) {
        return null;
    }

    public boolean prepareMstData() {
        return this.mVcpcsProcessor.prepareMstData();
    }

    public void transactionComplete(boolean z) {
        this.mVcpcsProcessor.m1351l(z);
    }

    public PaymentDataRequest constructPaymentDataRequest(String str, TokenKey tokenKey, String str2, String str3) {
        return this.mVcpcsProcessor.constructPaymentDataRequest(str, tokenKey, str2, str3);
    }

    public void processInAppTransactionComplete(TokenKey tokenKey, String str, boolean z) {
        this.mVcpcsProcessor.processInAppTransactionComplete(tokenKey, str, z);
    }

    public boolean isMstSupported(TokenKey tokenKey) {
        return this.mTokenProcessor.isMstSupported(tokenKey);
    }

    public boolean shouldTapAndGo(TokenKey tokenKey) {
        return this.mVcpcsProcessor.shouldTapAndGo(tokenKey);
    }

    public Bundle getTokenMetaData(TokenKey tokenKey) {
        return this.mTokenProcessor.getTokenMetaData(tokenKey);
    }
}
