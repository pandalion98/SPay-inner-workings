/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.engines;

import java.security.SecureRandom;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;

public class RFC3211WrapEngine
implements Wrapper {
    private CBCBlockCipher engine;
    private boolean forWrapping;
    private ParametersWithIV param;
    private SecureRandom rand;

    public RFC3211WrapEngine(BlockCipher blockCipher) {
        this.engine = new CBCBlockCipher(blockCipher);
    }

    @Override
    public String getAlgorithmName() {
        return this.engine.getUnderlyingCipher().getAlgorithmName() + "/RFC3211Wrap";
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forWrapping = bl;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.rand = parametersWithRandom.getRandom();
            this.param = (ParametersWithIV)parametersWithRandom.getParameters();
            return;
        }
        if (bl) {
            this.rand = new SecureRandom();
        }
        this.param = (ParametersWithIV)cipherParameters;
    }

    @Override
    public byte[] unwrap(byte[] arrby, int n2, int n3) {
        int n4 = 0;
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        int n5 = this.engine.getBlockSize();
        if (n3 < n5 * 2) {
            throw new InvalidCipherTextException("input too short");
        }
        byte[] arrby2 = new byte[n3];
        byte[] arrby3 = new byte[n5];
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)0, (int)n3);
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby3, (int)0, (int)arrby3.length);
        this.engine.init(false, new ParametersWithIV(this.param.getParameters(), arrby3));
        for (int i2 = n5; i2 < arrby2.length; i2 += n5) {
            this.engine.processBlock(arrby2, i2, arrby2, i2);
        }
        System.arraycopy((Object)arrby2, (int)(arrby2.length - arrby3.length), (Object)arrby3, (int)0, (int)arrby3.length);
        this.engine.init(false, new ParametersWithIV(this.param.getParameters(), arrby3));
        this.engine.processBlock(arrby2, 0, arrby2, 0);
        this.engine.init(false, this.param);
        for (int i3 = 0; i3 < arrby2.length; i3 += n5) {
            this.engine.processBlock(arrby2, i3, arrby2, i3);
        }
        if ((255 & arrby2[0]) > -4 + arrby2.length) {
            throw new InvalidCipherTextException("wrapped key corrupted");
        }
        byte[] arrby4 = new byte[255 & arrby2[0]];
        System.arraycopy((Object)arrby2, (int)4, (Object)arrby4, (int)0, (int)arrby2[0]);
        int n6 = 0;
        while (n4 != 3) {
            n6 |= (byte)(-1 ^ arrby2[n4 + 1]) ^ arrby4[n4];
            ++n4;
        }
        if (n6 != 0) {
            throw new InvalidCipherTextException("wrapped key fails checksum");
        }
        return arrby4;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public byte[] wrap(byte[] arrby, int n2, int n3) {
        byte[] arrby2;
        int n4;
        if (!this.forWrapping) {
            throw new IllegalStateException("not set for wrapping");
        }
        this.engine.init(true, this.param);
        int n5 = this.engine.getBlockSize();
        if (n3 + 4 < n5 * 2) {
            arrby2 = new byte[n5 * 2];
        } else {
            int n6 = (n3 + 4) % n5 == 0 ? n3 + 4 : n5 * (1 + (n3 + 4) / n5);
            arrby2 = new byte[n6];
        }
        arrby2[0] = (byte)n3;
        arrby2[1] = (byte)(-1 ^ arrby[n2]);
        arrby2[2] = (byte)(-1 ^ arrby[n2 + 1]);
        arrby2[3] = (byte)(-1 ^ arrby[n2 + 2]);
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)4, (int)n3);
        for (int i2 = n3 + 4; i2 < arrby2.length; ++i2) {
            arrby2[i2] = (byte)this.rand.nextInt();
        }
        int n7 = 0;
        do {
            int n8 = arrby2.length;
            n4 = 0;
            if (n7 >= n8) break;
            this.engine.processBlock(arrby2, n7, arrby2, n7);
            n7 += n5;
        } while (true);
        while (n4 < arrby2.length) {
            this.engine.processBlock(arrby2, n4, arrby2, n4);
            n4 += n5;
        }
        return arrby2;
    }
}

