package com.americanexpress.mobilepayments.hceclient.service;

import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenChannelInitializeResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenChannelUpdateResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenCloseResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenInAppResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;
import com.americanexpress.mobilepayments.hceclient.model.TokenPersoResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenRefreshStatusResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenUpdateResponse;
import com.americanexpress.mobilepayments.hceclient.session.OperationMode;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;

public interface AmexPay {
    TokenAPDUResponse tokenAPDU(byte[] bArr);

    TokenChannelInitializeResponse tokenChannelInitialize(byte[] bArr);

    TokenChannelUpdateResponse tokenChannelUpdate(byte[] bArr);

    TokenCloseResponse tokenClose();

    TokenInAppResponse tokenInApp(String str, String str2);

    TokenOperationStatus tokenLCM(StateMode stateMode);

    TokenOperationStatus tokenOpen(OperationMode operationMode, String str);

    TokenPersoResponse tokenPerso(String str);

    TokenRefreshStatusResponse tokenRefreshStatus(long j, String str);

    TokenOperationStatus tokenSetCDCVM(byte[] bArr);

    TokenUpdateResponse tokenUpdate(String str);
}
