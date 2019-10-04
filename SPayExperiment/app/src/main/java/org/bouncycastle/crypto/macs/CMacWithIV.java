/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.macs.CMac;

public class CMacWithIV
extends CMac {
    public CMacWithIV(BlockCipher blockCipher) {
        super(blockCipher);
    }

    public CMacWithIV(BlockCipher blockCipher, int n2) {
        super(blockCipher, n2);
    }

    @Override
    void validate(CipherParameters cipherParameters) {
    }
}

