/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import com.americanexpress.mobilepayments.hceclient.delegate.OperationDelegate;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenCloseChildDelegate;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenOpenLCMChildDelegate;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenOpenPaymentChildDelegate;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenOpenProvisionChildDelegate;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenOpenUpdateChildDelegate;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenPersoChildDelegate;
import com.americanexpress.mobilepayments.hceclient.session.OperationMode;

public class OperationDelegateFactory {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static OperationDelegate getOperationDelegate(OperationMode operationMode) {
        if (operationMode.equals((Object)OperationMode.PROVISION)) {
            return new TokenOpenProvisionChildDelegate();
        }
        if (operationMode.equals((Object)OperationMode.REFRESH)) {
            return new TokenOpenUpdateChildDelegate();
        }
        if (operationMode.equals((Object)OperationMode.PAYMENT)) return new TokenOpenPaymentChildDelegate();
        if (operationMode.equals((Object)OperationMode.TAP_PAYMENT)) {
            return new TokenOpenPaymentChildDelegate();
        }
        boolean bl = operationMode.equals((Object)OperationMode.LCM);
        TokenOpenProvisionChildDelegate tokenOpenProvisionChildDelegate = null;
        if (!bl) return tokenOpenProvisionChildDelegate;
        return new TokenOpenLCMChildDelegate();
    }

    public static OperationDelegate getTokenCloseDelegate() {
        return new TokenCloseChildDelegate();
    }

    public static OperationDelegate getTokenPersoDelegate() {
        return new TokenPersoChildDelegate();
    }
}

