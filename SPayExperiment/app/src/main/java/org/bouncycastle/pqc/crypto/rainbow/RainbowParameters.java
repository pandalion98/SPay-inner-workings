/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.pqc.crypto.rainbow;

import org.bouncycastle.crypto.CipherParameters;

public class RainbowParameters
implements CipherParameters {
    private final int[] DEFAULT_VI = new int[]{6, 12, 17, 22, 33};
    private int[] vi;

    public RainbowParameters() {
        this.vi = this.DEFAULT_VI;
    }

    public RainbowParameters(int[] arrn) {
        this.vi = arrn;
        try {
            this.checkParams();
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    private void checkParams() {
        if (this.vi == null) {
            throw new Exception("no layers defined.");
        }
        if (this.vi.length > 1) {
            for (int i = 0; i < -1 + this.vi.length; ++i) {
                if (this.vi[i] < this.vi[i + 1]) continue;
                throw new Exception("v[i] has to be smaller than v[i+1]");
            }
        } else {
            throw new Exception("Rainbow needs at least 1 layer, such that v1 < v2.");
        }
    }

    public int getDocLength() {
        return this.vi[-1 + this.vi.length] - this.vi[0];
    }

    public int getNumOfLayers() {
        return -1 + this.vi.length;
    }

    public int[] getVi() {
        return this.vi;
    }
}

