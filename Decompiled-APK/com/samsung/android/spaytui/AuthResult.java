package com.samsung.android.spaytui;

public class AuthResult {
    private String authType;
    private byte[] secObjData;

    AuthResult(byte[] bArr, String str) {
        this.secObjData = bArr;
        this.authType = str;
    }

    public byte[] getSecObjData() {
        return this.secObjData;
    }

    public void setSecObjData(byte[] bArr) {
        this.secObjData = bArr;
    }

    public String getAuthType() {
        return this.authType;
    }

    public void setAuthType(String str) {
        this.authType = str;
    }
}
