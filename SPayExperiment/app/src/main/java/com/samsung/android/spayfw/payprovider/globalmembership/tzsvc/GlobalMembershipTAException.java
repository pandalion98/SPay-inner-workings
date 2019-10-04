/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.globalmembership.tzsvc;

public class GlobalMembershipTAException
extends Exception {
    private static final long serialVersionUID = 1L;
    private int errorCode = 9001;

    public GlobalMembershipTAException(String string, int n2) {
        super(string);
        this.errorCode = n2;
    }
}

