package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class NetworkInfo extends Name {
    private NetworkInfo current;
    private String mcc;
    private String mnc;
    private String type;

    public NetworkInfo(String str, String str2, String str3, String str4) {
        super(str2);
        this.type = str;
        this.mcc = str3;
        this.mnc = str4;
    }

    public void setCurrent(NetworkInfo networkInfo) {
        this.current = networkInfo;
    }
}
