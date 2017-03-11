package org.bouncycastle.crypto.modes.gcm;

public class BasicGCMMultiplier implements GCMMultiplier {
    private int[] f200H;

    public void init(byte[] bArr) {
        this.f200H = GCMUtil.asInts(bArr);
    }

    public void multiplyH(byte[] bArr) {
        int[] asInts = GCMUtil.asInts(bArr);
        GCMUtil.multiply(asInts, this.f200H);
        GCMUtil.asBytes(asInts, bArr);
    }
}
