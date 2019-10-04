/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
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
    public TokenAPDUResponse tokenAPDU(byte[] var1);

    public TokenChannelInitializeResponse tokenChannelInitialize(byte[] var1);

    public TokenChannelUpdateResponse tokenChannelUpdate(byte[] var1);

    public TokenCloseResponse tokenClose();

    public TokenInAppResponse tokenInApp(String var1, String var2);

    public TokenOperationStatus tokenLCM(StateMode var1);

    public TokenOperationStatus tokenOpen(OperationMode var1, String var2);

    public TokenPersoResponse tokenPerso(String var1);

    public TokenRefreshStatusResponse tokenRefreshStatus(long var1, String var3);

    public TokenOperationStatus tokenSetCDCVM(byte[] var1);

    public TokenUpdateResponse tokenUpdate(String var1);
}

