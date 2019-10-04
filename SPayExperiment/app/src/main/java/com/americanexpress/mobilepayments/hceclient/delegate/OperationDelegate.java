/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;

public abstract class OperationDelegate {
    protected void checkSCStatus(int n2) {
        if (n2 < 0) {
            throw new HCEClientException("Secure Component Status::" + n2);
        }
    }

    protected int computeDestBufferSize(int n2) {
        return 1024 + n2 * 2;
    }

    public abstract void doOperation();
}

