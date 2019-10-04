/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.db;

public class DcDbException
extends Exception {
    private int mErrorCode;

    public DcDbException(String string, int n2) {
        super(string);
        this.mErrorCode = n2;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }
}

