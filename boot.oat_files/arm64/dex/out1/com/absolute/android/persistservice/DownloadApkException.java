package com.absolute.android.persistservice;

public class DownloadApkException extends RuntimeException {
    private static final long serialVersionUID = 1;
    private boolean m_isConnectivityFailure = false;
    private boolean m_isFatal = false;

    public DownloadApkException(String str, boolean z) {
        super(str);
        this.m_isFatal = z;
    }

    public DownloadApkException(String str, Throwable th, boolean z) {
        super(str, th);
        this.m_isFatal = z;
    }

    public boolean a() {
        return this.m_isFatal;
    }

    public boolean b() {
        return this.m_isConnectivityFailure;
    }

    public void a(boolean z) {
        this.m_isConnectivityFailure = z;
    }
}
