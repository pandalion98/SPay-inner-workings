package com.samsung.android.spayfw.remoteservice;

/* renamed from: com.samsung.android.spayfw.remoteservice.d */
public class ServerResponse extends Response<String> {
    private String AI;

    protected ServerResponse(String str, String str2, int i) {
        super(null, str2, i);
        this.AI = str;
    }

    public String getFilePath() {
        return this.AI;
    }
}
