/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.FilterInputStream
 *  java.io.InputStream
 */
package org.bouncycastle.crypto.io;

import java.io.FilterInputStream;
import java.io.InputStream;
import org.bouncycastle.crypto.Mac;

public class MacInputStream
extends FilterInputStream {
    protected Mac mac;

    public MacInputStream(InputStream inputStream, Mac mac) {
        super(inputStream);
        this.mac = mac;
    }

    public Mac getMac() {
        return this.mac;
    }

    public int read() {
        int n2 = this.in.read();
        if (n2 >= 0) {
            this.mac.update((byte)n2);
        }
        return n2;
    }

    public int read(byte[] arrby, int n2, int n3) {
        int n4 = this.in.read(arrby, n2, n3);
        if (n4 >= 0) {
            this.mac.update(arrby, n2, n4);
        }
        return n4;
    }
}

