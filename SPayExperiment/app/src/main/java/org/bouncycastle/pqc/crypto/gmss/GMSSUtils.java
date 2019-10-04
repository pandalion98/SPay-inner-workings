/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 *  java.util.Enumeration
 *  java.util.Vector
 */
package org.bouncycastle.pqc.crypto.gmss;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.pqc.crypto.gmss.GMSSLeaf;
import org.bouncycastle.pqc.crypto.gmss.GMSSRootCalc;
import org.bouncycastle.pqc.crypto.gmss.GMSSRootSig;
import org.bouncycastle.pqc.crypto.gmss.Treehash;
import org.bouncycastle.util.Arrays;

class GMSSUtils {
    GMSSUtils() {
    }

    static Vector[] clone(Vector[] arrvector) {
        if (arrvector == null) {
            return null;
        }
        Vector[] arrvector2 = new Vector[arrvector.length];
        for (int i = 0; i != arrvector.length; ++i) {
            arrvector2[i] = new Vector();
            Enumeration enumeration = arrvector[i].elements();
            while (enumeration.hasMoreElements()) {
                arrvector2[i].addElement(enumeration.nextElement());
            }
        }
        return arrvector2;
    }

    static GMSSLeaf[] clone(GMSSLeaf[] arrgMSSLeaf) {
        if (arrgMSSLeaf == null) {
            return null;
        }
        GMSSLeaf[] arrgMSSLeaf2 = new GMSSLeaf[arrgMSSLeaf.length];
        System.arraycopy((Object)arrgMSSLeaf, (int)0, (Object)arrgMSSLeaf2, (int)0, (int)arrgMSSLeaf.length);
        return arrgMSSLeaf2;
    }

    static GMSSRootCalc[] clone(GMSSRootCalc[] arrgMSSRootCalc) {
        if (arrgMSSRootCalc == null) {
            return null;
        }
        GMSSRootCalc[] arrgMSSRootCalc2 = new GMSSRootCalc[arrgMSSRootCalc.length];
        System.arraycopy((Object)arrgMSSRootCalc, (int)0, (Object)arrgMSSRootCalc2, (int)0, (int)arrgMSSRootCalc.length);
        return arrgMSSRootCalc2;
    }

    static GMSSRootSig[] clone(GMSSRootSig[] arrgMSSRootSig) {
        if (arrgMSSRootSig == null) {
            return null;
        }
        GMSSRootSig[] arrgMSSRootSig2 = new GMSSRootSig[arrgMSSRootSig.length];
        System.arraycopy((Object)arrgMSSRootSig, (int)0, (Object)arrgMSSRootSig2, (int)0, (int)arrgMSSRootSig.length);
        return arrgMSSRootSig2;
    }

    static Treehash[] clone(Treehash[] arrtreehash) {
        if (arrtreehash == null) {
            return null;
        }
        Treehash[] arrtreehash2 = new Treehash[arrtreehash.length];
        System.arraycopy((Object)arrtreehash, (int)0, (Object)arrtreehash2, (int)0, (int)arrtreehash.length);
        return arrtreehash2;
    }

    static byte[][] clone(byte[][] arrby) {
        if (arrby == null) {
            return null;
        }
        byte[][] arrarrby = new byte[arrby.length][];
        for (int i = 0; i != arrby.length; ++i) {
            arrarrby[i] = Arrays.clone(arrby[i]);
        }
        return arrarrby;
    }

    static Vector[][] clone(Vector[][] arrvector) {
        if (arrvector == null) {
            return null;
        }
        Vector[][] arrarrvector = new Vector[arrvector.length][];
        for (int i = 0; i != arrvector.length; ++i) {
            arrarrvector[i] = GMSSUtils.clone(arrvector[i]);
        }
        return arrarrvector;
    }

    static Treehash[][] clone(Treehash[][] arrtreehash) {
        if (arrtreehash == null) {
            return null;
        }
        Treehash[][] arrtreehash2 = new Treehash[arrtreehash.length][];
        for (int i = 0; i != arrtreehash.length; ++i) {
            arrtreehash2[i] = GMSSUtils.clone(arrtreehash[i]);
        }
        return arrtreehash2;
    }

    static byte[][][] clone(byte[][][] arrby) {
        if (arrby == null) {
            return null;
        }
        byte[][][] arrarrby = new byte[arrby.length][][];
        for (int i = 0; i != arrby.length; ++i) {
            arrarrby[i] = GMSSUtils.clone(arrby[i]);
        }
        return arrarrby;
    }
}

