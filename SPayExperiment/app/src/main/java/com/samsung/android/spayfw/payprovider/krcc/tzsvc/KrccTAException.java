/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.krcc.tzsvc;

public class KrccTAException
extends Exception {
    public static final int ERR_INVALID_INPUT = 1004;
    public static final int ERR_TZ_ACCESS_DENIED = 1002;
    public static final int ERR_TZ_COM_ERR = 1001;
    public static final int ERR_TZ_TA_NOT_AVAILABLE = 1003;
    public static final int ERR_UNKNOWN = 9001;
    private static final long serialVersionUID = 1L;
    private int errorCode = 9001;

    public KrccTAException(String string, int n2) {
        super(string);
        this.errorCode = n2;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}

