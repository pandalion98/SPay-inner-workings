/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.pqc.jcajce.spec;

import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.util.Arrays;

public class RainbowParameterSpec
implements AlgorithmParameterSpec {
    private static final int[] DEFAULT_VI = new int[]{6, 12, 17, 22, 33};
    private int[] vi;

    public RainbowParameterSpec() {
        this.vi = DEFAULT_VI;
    }

    public RainbowParameterSpec(int[] arrn) {
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
            throw new IllegalArgumentException("no layers defined.");
        }
        if (this.vi.length > 1) {
            for (int i = 0; i < -1 + this.vi.length; ++i) {
                if (this.vi[i] < this.vi[i + 1]) continue;
                throw new IllegalArgumentException("v[i] has to be smaller than v[i+1]");
            }
        } else {
            throw new IllegalArgumentException("Rainbow needs at least 1 layer, such that v1 < v2.");
        }
    }

    public int getDocumentLength() {
        return this.vi[-1 + this.vi.length] - this.vi[0];
    }

    public int getNumOfLayers() {
        return -1 + this.vi.length;
    }

    public int[] getVi() {
        return Arrays.clone(this.vi);
    }
}

