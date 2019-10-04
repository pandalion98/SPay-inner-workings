/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.security.SecureRandom
 */
package org.bouncycastle.util.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

public class FixedSecureRandom
extends SecureRandom {
    private byte[] _data;
    private int _index;
    private int _intPad;

    public FixedSecureRandom(boolean bl, byte[] arrby) {
        this(bl, new byte[][]{arrby});
    }

    public FixedSecureRandom(boolean bl, byte[][] arrby) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i != arrby.length; ++i) {
            try {
                byteArrayOutputStream.write(arrby[i]);
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("can't save value array.");
            }
        }
        this._data = byteArrayOutputStream.toByteArray();
        if (bl) {
            this._intPad = this._data.length % 4;
        }
    }

    public FixedSecureRandom(byte[] arrby) {
        this(false, new byte[][]{arrby});
    }

    public FixedSecureRandom(byte[][] arrby) {
        this(false, arrby);
    }

    private int nextValue() {
        byte[] arrby = this._data;
        int n = this._index;
        this._index = n + 1;
        return 255 & arrby[n];
    }

    public boolean isExhausted() {
        return this._index == this._data.length;
    }

    public void nextBytes(byte[] arrby) {
        System.arraycopy((Object)this._data, (int)this._index, (Object)arrby, (int)0, (int)arrby.length);
        this._index += arrby.length;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int nextInt() {
        int n = 0 | this.nextValue() << 24 | this.nextValue() << 16;
        if (this._intPad == 2) {
            this._intPad = -1 + this._intPad;
        } else {
            n |= this.nextValue() << 8;
        }
        if (this._intPad == 1) {
            this._intPad = -1 + this._intPad;
            return n;
        }
        return n | this.nextValue();
    }

    public long nextLong() {
        return 0L | (long)this.nextValue() << 56 | (long)this.nextValue() << 48 | (long)this.nextValue() << 40 | (long)this.nextValue() << 32 | (long)this.nextValue() << 24 | (long)this.nextValue() << 16 | (long)this.nextValue() << 8 | (long)this.nextValue();
    }
}

