/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.Byte
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.reflect.Array
 *  java.security.SecureRandom
 *  java.util.Vector
 */
package org.bouncycastle.pqc.crypto.gmss;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.util.Vector;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.pqc.crypto.gmss.GMSSDigestProvider;
import org.bouncycastle.pqc.crypto.gmss.GMSSKeyGenerationParameters;
import org.bouncycastle.pqc.crypto.gmss.GMSSParameters;
import org.bouncycastle.pqc.crypto.gmss.GMSSPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.gmss.GMSSPublicKeyParameters;
import org.bouncycastle.pqc.crypto.gmss.GMSSRootCalc;
import org.bouncycastle.pqc.crypto.gmss.Treehash;
import org.bouncycastle.pqc.crypto.gmss.util.GMSSRandom;
import org.bouncycastle.pqc.crypto.gmss.util.WinternitzOTSVerify;
import org.bouncycastle.pqc.crypto.gmss.util.WinternitzOTSignature;

public class GMSSKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.3";
    private int[] K;
    private byte[][] currentRootSigs;
    private byte[][] currentSeeds;
    private GMSSDigestProvider digestProvider;
    private GMSSParameters gmssPS;
    private GMSSKeyGenerationParameters gmssParams;
    private GMSSRandom gmssRandom;
    private int[] heightOfTrees;
    private boolean initialized = false;
    private int mdLength;
    private Digest messDigestTree;
    private byte[][] nextNextSeeds;
    private int numLayer;
    private int[] otsIndex;

    public GMSSKeyPairGenerator(GMSSDigestProvider gMSSDigestProvider) {
        this.digestProvider = gMSSDigestProvider;
        this.messDigestTree = gMSSDigestProvider.get();
        this.mdLength = this.messDigestTree.getDigestSize();
        this.gmssRandom = new GMSSRandom(this.messDigestTree);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private AsymmetricCipherKeyPair genKeyPair() {
        if (!this.initialized) {
            this.initializeDefault();
        }
        byte[][][] arrarrby = new byte[this.numLayer][][];
        byte[][][] arrarrby2 = new byte[-1 + this.numLayer][][];
        Treehash[][] arrtreehash = new Treehash[this.numLayer][];
        Treehash[][] arrtreehash2 = new Treehash[-1 + this.numLayer][];
        Vector[] arrvector = new Vector[this.numLayer];
        Vector[] arrvector2 = new Vector[-1 + this.numLayer];
        Vector[][] arrarrvector = new Vector[this.numLayer][];
        Vector[][] arrarrvector2 = new Vector[-1 + this.numLayer][];
        for (int i = 0; i < this.numLayer; ++i) {
            int[] arrn = new int[]{this.heightOfTrees[i], this.mdLength};
            arrarrby[i] = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
            arrtreehash[i] = new Treehash[this.heightOfTrees[i] - this.K[i]];
            if (i > 0) {
                int n = i - 1;
                int[] arrn2 = new int[]{this.heightOfTrees[i], this.mdLength};
                arrarrby2[n] = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn2);
                arrtreehash2[i - 1] = new Treehash[this.heightOfTrees[i] - this.K[i]];
            }
            arrvector[i] = new Vector();
            if (i <= 0) continue;
            arrvector2[i - 1] = new Vector();
        }
        int[] arrn = new int[]{this.numLayer, this.mdLength};
        byte[][] arrby = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
        int[] arrn3 = new int[]{-1 + this.numLayer, this.mdLength};
        byte[][] arrby2 = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn3);
        int[] arrn4 = new int[]{this.numLayer, this.mdLength};
        byte[][] arrby3 = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn4);
        for (int i = 0; i < this.numLayer; ++i) {
            System.arraycopy((Object)this.currentSeeds[i], (int)0, (Object)arrby3[i], (int)0, (int)this.mdLength);
        }
        int[] arrn5 = new int[]{-1 + this.numLayer, this.mdLength};
        this.currentRootSigs = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn5);
        for (int i = -1 + this.numLayer; i >= 0; --i) {
            int n;
            GMSSRootCalc gMSSRootCalc;
            block12 : {
                GMSSRootCalc gMSSRootCalc2;
                gMSSRootCalc = new GMSSRootCalc(this.heightOfTrees[i], this.K[i], this.digestProvider);
                int n2 = -1 + this.numLayer;
                if (i != n2) break block12;
                gMSSRootCalc = gMSSRootCalc2 = this.generateCurrentAuthpathAndRoot(null, arrvector[i], arrby3[i], i);
            }
            int n3 = i + 1;
            try {
                GMSSRootCalc gMSSRootCalc3;
                gMSSRootCalc = gMSSRootCalc3 = this.generateCurrentAuthpathAndRoot(arrby[n3], arrvector[i], arrby3[i], i);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            for (int j = 0; j < (n = this.heightOfTrees[i]); ++j) {
                System.arraycopy((Object)gMSSRootCalc.getAuthPath()[j], (int)0, (Object)arrarrby[i][j], (int)0, (int)this.mdLength);
            }
            arrarrvector[i] = gMSSRootCalc.getRetain();
            arrtreehash[i] = gMSSRootCalc.getTreehash();
            System.arraycopy((Object)gMSSRootCalc.getRoot(), (int)0, (Object)arrby[i], (int)0, (int)this.mdLength);
        }
        int n = -2 + this.numLayer;
        do {
            if (n < 0) {
                GMSSPublicKeyParameters gMSSPublicKeyParameters = new GMSSPublicKeyParameters(arrby[0], this.gmssPS);
                return new AsymmetricCipherKeyPair(gMSSPublicKeyParameters, new GMSSPrivateKeyParameters(this.currentSeeds, this.nextNextSeeds, arrarrby, arrarrby2, arrtreehash, arrtreehash2, arrvector, arrvector2, arrarrvector, arrarrvector2, arrby2, this.currentRootSigs, this.gmssPS, this.digestProvider));
            }
            GMSSRootCalc gMSSRootCalc = this.generateNextAuthpathAndRoot(arrvector2[n], arrby3[n + 1], n + 1);
            for (int i = 0; i < this.heightOfTrees[n + 1]; ++i) {
                System.arraycopy((Object)gMSSRootCalc.getAuthPath()[i], (int)0, (Object)arrarrby2[n][i], (int)0, (int)this.mdLength);
            }
            arrarrvector2[n] = gMSSRootCalc.getRetain();
            arrtreehash2[n] = gMSSRootCalc.getTreehash();
            System.arraycopy((Object)gMSSRootCalc.getRoot(), (int)0, (Object)arrby2[n], (int)0, (int)this.mdLength);
            System.arraycopy((Object)arrby3[n + 1], (int)0, (Object)this.nextNextSeeds[n], (int)0, (int)this.mdLength);
            --n;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private GMSSRootCalc generateCurrentAuthpathAndRoot(byte[] arrby, Vector vector, byte[] arrby2, int n) {
        byte[] arrby3;
        new byte[this.mdLength];
        new byte[this.mdLength];
        byte[] arrby4 = this.gmssRandom.nextSeed(arrby2);
        GMSSRootCalc gMSSRootCalc = new GMSSRootCalc(this.heightOfTrees[n], this.K[n], this.digestProvider);
        gMSSRootCalc.initialize(vector);
        if (n == -1 + this.numLayer) {
            arrby3 = new WinternitzOTSignature(arrby4, this.digestProvider.get(), this.otsIndex[n]).getPublicKey();
        } else {
            WinternitzOTSignature winternitzOTSignature = new WinternitzOTSignature(arrby4, this.digestProvider.get(), this.otsIndex[n]);
            this.currentRootSigs[n] = winternitzOTSignature.getSignature(arrby);
            arrby3 = new WinternitzOTSVerify(this.digestProvider.get(), this.otsIndex[n]).Verify(arrby, this.currentRootSigs[n]);
        }
        gMSSRootCalc.update(arrby3);
        int n2 = 3;
        int n3 = 0;
        for (int i = 1; i < 1 << this.heightOfTrees[n]; ++i) {
            if (i == n2 && n3 < this.heightOfTrees[n] - this.K[n]) {
                gMSSRootCalc.initializeTreehashSeed(arrby2, n3);
                n2 *= 2;
                ++n3;
            }
            gMSSRootCalc.update(new WinternitzOTSignature(this.gmssRandom.nextSeed(arrby2), this.digestProvider.get(), this.otsIndex[n]).getPublicKey());
        }
        if (gMSSRootCalc.wasFinished()) {
            return gMSSRootCalc;
        }
        System.err.println("Baum noch nicht fertig konstruiert!!!");
        return null;
    }

    private GMSSRootCalc generateNextAuthpathAndRoot(Vector vector, byte[] arrby, int n) {
        new byte[this.numLayer];
        GMSSRootCalc gMSSRootCalc = new GMSSRootCalc(this.heightOfTrees[n], this.K[n], this.digestProvider);
        gMSSRootCalc.initialize(vector);
        int n2 = 3;
        int n3 = 0;
        for (int i = 0; i < 1 << this.heightOfTrees[n]; ++i) {
            if (i == n2 && n3 < this.heightOfTrees[n] - this.K[n]) {
                gMSSRootCalc.initializeTreehashSeed(arrby, n3);
                n2 *= 2;
                ++n3;
            }
            gMSSRootCalc.update(new WinternitzOTSignature(this.gmssRandom.nextSeed(arrby), this.digestProvider.get(), this.otsIndex[n]).getPublicKey());
        }
        if (gMSSRootCalc.wasFinished()) {
            return gMSSRootCalc;
        }
        System.err.println("N\ufffdchster Baum noch nicht fertig konstruiert!!!");
        return null;
    }

    private void initializeDefault() {
        int[] arrn = new int[]{10, 10, 10, 10};
        int[] arrn2 = new int[]{3, 3, 3, 3};
        int[] arrn3 = new int[]{2, 2, 2, 2};
        this.initialize(new GMSSKeyGenerationParameters(new SecureRandom(), new GMSSParameters(arrn.length, arrn, arrn2, arrn3)));
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        return this.genKeyPair();
    }

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.initialize(keyGenerationParameters);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void initialize(int n, SecureRandom secureRandom) {
        GMSSKeyGenerationParameters gMSSKeyGenerationParameters;
        if (n <= 10) {
            int[] arrn = new int[]{10};
            int[] arrn2 = new int[]{3};
            int[] arrn3 = new int[]{2};
            gMSSKeyGenerationParameters = new GMSSKeyGenerationParameters(secureRandom, new GMSSParameters(arrn.length, arrn, arrn2, arrn3));
        } else if (n <= 20) {
            int[] arrn = new int[]{10, 10};
            int[] arrn4 = new int[]{5, 4};
            int[] arrn5 = new int[]{2, 2};
            gMSSKeyGenerationParameters = new GMSSKeyGenerationParameters(secureRandom, new GMSSParameters(arrn.length, arrn, arrn4, arrn5));
        } else {
            int[] arrn = new int[]{10, 10, 10, 10};
            int[] arrn6 = new int[]{9, 9, 9, 3};
            int[] arrn7 = new int[]{2, 2, 2, 2};
            gMSSKeyGenerationParameters = new GMSSKeyGenerationParameters(secureRandom, new GMSSParameters(arrn.length, arrn, arrn6, arrn7));
        }
        this.initialize(gMSSKeyGenerationParameters);
    }

    public void initialize(KeyGenerationParameters keyGenerationParameters) {
        this.gmssParams = (GMSSKeyGenerationParameters)keyGenerationParameters;
        this.gmssPS = new GMSSParameters(this.gmssParams.getParameters().getNumOfLayers(), this.gmssParams.getParameters().getHeightOfTrees(), this.gmssParams.getParameters().getWinternitzParameter(), this.gmssParams.getParameters().getK());
        this.numLayer = this.gmssPS.getNumOfLayers();
        this.heightOfTrees = this.gmssPS.getHeightOfTrees();
        this.otsIndex = this.gmssPS.getWinternitzParameter();
        this.K = this.gmssPS.getK();
        int[] arrn = new int[]{this.numLayer, this.mdLength};
        this.currentSeeds = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
        int[] arrn2 = new int[]{-1 + this.numLayer, this.mdLength};
        this.nextNextSeeds = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn2);
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < this.numLayer; ++i) {
            secureRandom.nextBytes(this.currentSeeds[i]);
            this.gmssRandom.nextSeed(this.currentSeeds[i]);
        }
        this.initialized = true;
    }
}

