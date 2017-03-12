package com.samsung.contextservice.server.models;

public class WalletInfo extends Id {
    private int since;

    public WalletInfo(String str) {
        super(str);
    }

    void setSince(int i) {
        this.since = i;
    }

    public String toString() {
        return getId();
    }
}
