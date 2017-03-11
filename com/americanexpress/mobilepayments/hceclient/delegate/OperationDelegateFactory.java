package com.americanexpress.mobilepayments.hceclient.delegate;

import com.americanexpress.mobilepayments.hceclient.session.OperationMode;

public class OperationDelegateFactory {
    public static OperationDelegate getOperationDelegate(OperationMode operationMode) {
        if (operationMode.equals(OperationMode.PROVISION)) {
            return new TokenOpenProvisionChildDelegate();
        }
        if (operationMode.equals(OperationMode.REFRESH)) {
            return new TokenOpenUpdateChildDelegate();
        }
        if (operationMode.equals(OperationMode.PAYMENT) || operationMode.equals(OperationMode.TAP_PAYMENT)) {
            return new TokenOpenPaymentChildDelegate();
        }
        if (operationMode.equals(OperationMode.LCM)) {
            return new TokenOpenLCMChildDelegate();
        }
        return null;
    }

    public static OperationDelegate getTokenCloseDelegate() {
        return new TokenCloseChildDelegate();
    }

    public static OperationDelegate getTokenPersoDelegate() {
        return new TokenPersoChildDelegate();
    }
}
