package com.samsung.android.spayfw.payprovider;

/* renamed from: com.samsung.android.spayfw.payprovider.f */
public class ProviderTokenKey {
    private String mF;
    private String oZ;

    public ProviderTokenKey(long j) {
        this.oZ = String.valueOf(j);
    }

    public ProviderTokenKey(String str) {
        this.oZ = str;
    }

    public long cm() {
        return Long.parseLong(this.oZ);
    }

    public String cn() {
        if (this.oZ != null) {
            return String.valueOf(this.oZ);
        }
        return null;
    }

    public String getTrTokenId() {
        return this.mF;
    }

    public void setTrTokenId(String str) {
        this.mF = str;
    }
}
