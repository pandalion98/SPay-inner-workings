package com.samsung.android.spayfw.payprovider.globalmembership.tzsvc;

import com.samsung.android.spaytui.SPayTUIException;

public class GlobalMembershipTAException extends Exception {
    private static final long serialVersionUID = 1;
    private int errorCode;

    public GlobalMembershipTAException(String str, int i) {
        super(str);
        this.errorCode = SPayTUIException.ERR_UNKNOWN;
        this.errorCode = i;
    }
}
