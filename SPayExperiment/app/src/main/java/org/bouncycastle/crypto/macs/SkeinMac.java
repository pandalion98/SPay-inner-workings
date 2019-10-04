/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.digests.SkeinEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.SkeinParameters;

public class SkeinMac
implements Mac {
    public static final int SKEIN_1024 = 1024;
    public static final int SKEIN_256 = 256;
    public static final int SKEIN_512 = 512;
    private SkeinEngine engine;

    public SkeinMac(int n2, int n3) {
        this.engine = new SkeinEngine(n2, n3);
    }

    public SkeinMac(SkeinMac skeinMac) {
        this.engine = new SkeinEngine(skeinMac.engine);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        return this.engine.doFinal(arrby, n2);
    }

    @Override
    public String getAlgorithmName() {
        return "Skein-MAC-" + 8 * this.engine.getBlockSize() + "-" + 8 * this.engine.getOutputSize();
    }

    @Override
    public int getMacSize() {
        return this.engine.getOutputSize();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(CipherParameters cipherParameters) {
        SkeinParameters skeinParameters;
        if (cipherParameters instanceof SkeinParameters) {
            skeinParameters = (SkeinParameters)cipherParameters;
        } else {
            if (!(cipherParameters instanceof KeyParameter)) {
                throw new IllegalArgumentException("Invalid parameter passed to Skein MAC init - " + cipherParameters.getClass().getName());
            }
            skeinParameters = new SkeinParameters.Builder().setKey(((KeyParameter)cipherParameters).getKey()).build();
        }
        if (skeinParameters.getKey() == null) {
            throw new IllegalArgumentException("Skein MAC requires a key parameter.");
        }
        this.engine.init(skeinParameters);
    }

    @Override
    public void reset() {
        this.engine.reset();
    }

    @Override
    public void update(byte by) {
        this.engine.update(by);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        this.engine.update(arrby, n2, n3);
    }
}

