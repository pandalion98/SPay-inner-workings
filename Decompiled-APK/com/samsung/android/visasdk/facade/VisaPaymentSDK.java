package com.samsung.android.visasdk.facade;

import android.os.Bundle;
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
import com.samsung.android.visasdk.facade.data.TransactionStatus;
import com.samsung.android.visasdk.facade.data.UpdateReason;
import com.samsung.android.visasdk.paywave.data.CardMetadataUpdateResponse;
import com.samsung.android.visasdk.paywave.data.EnrollDeviceCerts;
import com.samsung.android.visasdk.paywave.data.TVL;
import com.samsung.android.visasdk.paywave.model.LcmParams;
import com.samsung.android.visasdk.paywave.model.LcmTokenRequest;
import com.samsung.android.visasdk.paywave.model.TokenInfo;
import java.util.List;

public interface VisaPaymentSDK {
    EnrollPanRequest constructEnrollRequest(byte[] bArr);

    LcmTokenRequest constructLcmRequest(LcmParams lcmParams);

    PaymentDataRequest constructPaymentDataRequest(String str, TokenKey tokenKey, String str2, String str3);

    ProvisionAckRequest constructProvisionAck(TokenKey tokenKey);

    ProvisionRequestWithEnrollId constructProvisionRequest(byte[] bArr);

    ProvisionRequestWithPanData constructProvisionRequest(byte[] bArr, byte[] bArr2);

    ReplenishAckRequest constructReplenishAcknowledgementRequest(TokenKey tokenKey);

    ReplenishRequest constructReplenishRequest(TokenKey tokenKey);

    void deleteAllTokensLocally();

    void deleteToken(TokenKey tokenKey);

    void enableThm(boolean z);

    List<TokenData> getAllTokenData();

    EnrollDeviceCerts getCerts();

    CvmMode getCvmVerificationMode();

    DeviceData getDeviceData();

    String getEnrollPANTemplate();

    int getMaxTvlRecords();

    TokenKey getSelectedCard();

    TokenData getTokenData(TokenKey tokenKey);

    TokenKey getTokenKeyForProvisionedToken(String str);

    Bundle getTokenMetaData(TokenKey tokenKey);

    String getTokenStatus(TokenKey tokenKey);

    List<TVL> getTvlLog(TokenKey tokenKey);

    boolean isCvmVerified();

    boolean isMstSupported(TokenKey tokenKey);

    boolean isThmEnabled();

    boolean prepareMstData();

    ApduResponse processCommandApdu(byte[] bArr, Bundle bundle, boolean z);

    void processInAppTransactionComplete(TokenKey tokenKey, String str, boolean z);

    boolean processReplenishmentResponse(TokenKey tokenKey, TokenInfo tokenInfo);

    TransactionStatus processTransactionComplete(TokenKey tokenKey);

    void resumeToken(TokenKey tokenKey);

    void selectCard(TokenKey tokenKey);

    void setCvmVerificationMode(CvmMode cvmMode);

    void setCvmVerified(boolean z);

    void setMaxTvlRecords(int i);

    void setPasscode(String str);

    boolean shouldTapAndGo(TokenKey tokenKey);

    TokenKey storeProvisionedToken(ProvisionResponse provisionResponse, String str);

    void suspendToken(TokenKey tokenKey, UpdateReason updateReason);

    boolean tokensExist();

    void transactionComplete(boolean z);

    boolean updateCardMetaData(CardMetadataUpdateResponse cardMetadataUpdateResponse);

    boolean updateTokenStatus(TokenKey tokenKey, TokenStatus tokenStatus);

    boolean verifyPasscode(String str);
}
