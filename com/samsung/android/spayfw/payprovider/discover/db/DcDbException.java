package com.samsung.android.spayfw.payprovider.discover.db;

public class DcDbException extends Exception {
    private int mErrorCode;

    public DcDbException(String str, int i) {
        super(str);
        this.mErrorCode = i;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }
}
