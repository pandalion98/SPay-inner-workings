/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.params.KeyParameter;

public class DESedeEngine
extends DESEngine {
    protected static final int BLOCK_SIZE = 8;
    private boolean forEncryption;
    private int[] workingKey1 = null;
    private int[] workingKey2 = null;
    private int[] workingKey3 = null;

    @Override
    public String getAlgorithmName() {
        return "DESede";
    }

    @Override
    public int getBlockSize() {
        return 8;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to DESede init - " + cipherParameters.getClass().getName());
        }
        byte[] arrby = ((KeyParameter)cipherParameters).getKey();
        if (arrby.length != 24 && arrby.length != 16) {
            throw new IllegalArgumentException("key size must be 16 or 24 bytes.");
        }
        this.forEncryption = bl;
        byte[] arrby2 = new byte[8];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby2.length);
        this.workingKey1 = this.generateWorkingKey(bl, arrby2);
        byte[] arrby3 = new byte[8];
        System.arraycopy((Object)arrby, (int)8, (Object)arrby3, (int)0, (int)arrby3.length);
        boolean bl2 = !bl;
        this.workingKey2 = this.generateWorkingKey(bl2, arrby3);
        if (arrby.length == 24) {
            byte[] arrby4 = new byte[8];
            System.arraycopy((Object)arrby, (int)16, (Object)arrby4, (int)0, (int)arrby4.length);
            this.workingKey3 = this.generateWorkingKey(bl, arrby4);
            return;
        }
        this.workingKey3 = this.workingKey1;
    }

    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (this.workingKey1 == null) {
            throw new IllegalStateException("DESede engine not initialised");
        }
        if (n2 + 8 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + 8 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        byte[] arrby3 = new byte[8];
        if (this.forEncryption) {
            this.desFunc(this.workingKey1, arrby, n2, arrby3, 0);
            this.desFunc(this.workingKey2, arrby3, 0, arrby3, 0);
            this.desFunc(this.workingKey3, arrby3, 0, arrby2, n3);
            return 8;
        }
        this.desFunc(this.workingKey3, arrby, n2, arrby3, 0);
        this.desFunc(this.workingKey2, arrby3, 0, arrby3, 0);
        this.desFunc(this.workingKey1, arrby3, 0, arrby2, n3);
        return 8;
    }

    @Override
    public void reset() {
    }
}

