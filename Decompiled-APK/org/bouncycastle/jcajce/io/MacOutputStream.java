package org.bouncycastle.jcajce.io;

import java.io.OutputStream;
import javax.crypto.Mac;

public class MacOutputStream extends OutputStream {
    protected Mac mac;

    public MacOutputStream(Mac mac) {
        this.mac = mac;
    }

    public byte[] getMac() {
        return this.mac.doFinal();
    }

    public void write(int i) {
        this.mac.update((byte) i);
    }

    public void write(byte[] bArr, int i, int i2) {
        this.mac.update(bArr, i, i2);
    }
}
