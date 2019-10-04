/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.pqc.crypto.gmss;

import org.bouncycastle.util.Arrays;

public class GMSSParameters {
    private int[] K;
    private int[] heightOfTrees;
    private int numOfLayers;
    private int[] winternitzParameter;

    public GMSSParameters(int n) {
        if (n <= 10) {
            int[] arrn = new int[]{10};
            int[] arrn2 = new int[]{3};
            int[] arrn3 = new int[]{2};
            this.init(arrn.length, arrn, arrn2, arrn3);
            return;
        }
        if (n <= 20) {
            int[] arrn = new int[]{10, 10};
            int[] arrn4 = new int[]{5, 4};
            int[] arrn5 = new int[]{2, 2};
            this.init(arrn.length, arrn, arrn4, arrn5);
            return;
        }
        int[] arrn = new int[]{10, 10, 10, 10};
        int[] arrn6 = new int[]{9, 9, 9, 3};
        int[] arrn7 = new int[]{2, 2, 2, 2};
        this.init(arrn.length, arrn, arrn6, arrn7);
    }

    public GMSSParameters(int n, int[] arrn, int[] arrn2, int[] arrn3) {
        this.init(n, arrn, arrn2, arrn3);
    }

    private void init(int n, int[] arrn, int[] arrn2, int[] arrn3) {
        boolean bl = true;
        String string = "";
        this.numOfLayers = n;
        if (this.numOfLayers != arrn2.length || this.numOfLayers != arrn.length || this.numOfLayers != arrn3.length) {
            string = "Unexpected parameterset format";
            bl = false;
        }
        boolean bl2 = bl;
        String string2 = string;
        for (int i = 0; i < this.numOfLayers; ++i) {
            if (arrn3[i] < 2 || (arrn[i] - arrn3[i]) % 2 != 0) {
                string2 = "Wrong parameter K (K >= 2 and H-K even required)!";
                bl2 = false;
            }
            if (arrn[i] >= 4 && arrn2[i] >= 2) continue;
            string2 = "Wrong parameter H or w (H > 3 and w > 1 required)!";
            bl2 = false;
        }
        if (bl2) {
            this.heightOfTrees = Arrays.clone(arrn);
            this.winternitzParameter = Arrays.clone(arrn2);
            this.K = Arrays.clone(arrn3);
            return;
        }
        throw new IllegalArgumentException(string2);
    }

    public int[] getHeightOfTrees() {
        return Arrays.clone(this.heightOfTrees);
    }

    public int[] getK() {
        return Arrays.clone(this.K);
    }

    public int getNumOfLayers() {
        return this.numOfLayers;
    }

    public int[] getWinternitzParameter() {
        return Arrays.clone(this.winternitzParameter);
    }
}

