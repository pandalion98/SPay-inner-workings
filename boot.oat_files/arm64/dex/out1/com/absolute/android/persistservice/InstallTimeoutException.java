package com.absolute.android.persistservice;

public class InstallTimeoutException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public InstallTimeoutException(String str) {
        super(str);
    }

    public InstallTimeoutException(Throwable th) {
        super(th);
    }
}
