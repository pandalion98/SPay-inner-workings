/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 *  javax.crypto.Mac
 */
package org.bouncycastle.jcajce.io;

import java.io.OutputStream;
import javax.crypto.Mac;

public class MacOutputStream
extends OutputStream {
    protected Mac mac;

    public MacOutputStream(Mac mac) {
        this.mac = mac;
    }

    public byte[] getMac() {
        return this.mac.doFinal();
    }

    public void write(int n2) {
        this.mac.update((byte)n2);
    }

    public void write(byte[] arrby, int n2, int n3) {
        this.mac.update(arrby, n2, n3);
    }
}

