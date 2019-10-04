/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
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
    public EnrollPanRequest constructEnrollRequest(byte[] var1);

    public LcmTokenRequest constructLcmRequest(LcmParams var1);

    public PaymentDataRequest constructPaymentDataRequest(String var1, TokenKey var2, String var3, String var4);

    public ProvisionAckRequest constructProvisionAck(TokenKey var1);

    public ProvisionRequestWithEnrollId constructProvisionRequest(byte[] var1);

    public ProvisionRequestWithPanData constructProvisionRequest(byte[] var1, byte[] var2);

    public ReplenishAckRequest constructReplenishAcknowledgementRequest(TokenKey var1);

    public ReplenishRequest constructReplenishRequest(TokenKey var1);

    public void deleteAllTokensLocally();

    public void deleteToken(TokenKey var1);

    public void enableThm(boolean var1);

    public List<TokenData> getAllTokenData();

    public EnrollDeviceCerts getCerts();

    public CvmMode getCvmVerificationMode();

    public DeviceData getDeviceData();

    public String getEnrollPANTemplate();

    public int getMaxTvlRecords();

    public TokenKey getSelectedCard();

    public TokenData getTokenData(TokenKey var1);

    public TokenKey getTokenKeyForProvisionedToken(String var1);

    public Bundle getTokenMetaData(TokenKey var1);

    public String getTokenStatus(TokenKey var1);

    public List<TVL> getTvlLog(TokenKey var1);

    public boolean isCvmVerified();

    public boolean isMstSupported(TokenKey var1);

    public boolean isThmEnabled();

    public boolean prepareMstData();

    public ApduResponse processCommandApdu(byte[] var1, Bundle var2, boolean var3);

    public void processInAppTransactionComplete(TokenKey var1, String var2, boolean var3);

    public boolean processReplenishmentResponse(TokenKey var1, TokenInfo var2);

    public TransactionStatus processTransactionComplete(TokenKey var1);

    public void resumeToken(TokenKey var1);

    public void selectCard(TokenKey var1);

    public void setCvmVerificationMode(CvmMode var1);

    public void setCvmVerified(boolean var1);

    public void setMaxTvlRecords(int var1);

    public void setPasscode(String var1);

    public boolean shouldTapAndGo(TokenKey var1);

    public TokenKey storeProvisionedToken(ProvisionResponse var1, String var2);

    public void suspendToken(TokenKey var1, UpdateReason var2);

    public boolean tokensExist();

    public void transactionComplete(boolean var1);

    public boolean updateCardMetaData(CardMetadataUpdateResponse var1);

    public boolean updateTokenStatus(TokenKey var1, TokenStatus var2);

    public boolean verifyPasscode(String var1);
}

