package com.samsung.android.spaytzsvc.api;

public class KeyValPair extends TAStruct {
    private static final String TAG = "KeyValPair";
    private Blob key;
    private Blob value;

    public KeyValPair(int i, int i2) {
        this.key = (Blob) inner(new Blob(i));
        this.value = (Blob) inner(new Blob(i2));
    }

    public void setData(byte[] bArr, byte[] bArr2) {
        if (bArr != null && bArr.length != 0 && bArr2 != null) {
            this.key.setData(bArr);
            this.value.setData(bArr2);
        }
    }

    public byte[] getKey() {
        return this.key.getData();
    }

    public byte[] getValue() {
        return this.value.getData();
    }
}
