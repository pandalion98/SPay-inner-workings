/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.Short
 *  java.lang.String
 *  java.lang.reflect.Array
 */
package org.bouncycastle.pqc.crypto.rainbow.util;

import java.lang.reflect.Array;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class ComputeInField {
    private short[][] A;
    short[] x;

    private void computeZerosAbove() {
        for (int i = -1 + this.A.length; i > 0; --i) {
            for (int j = i - 1; j >= 0; --j) {
                short s = this.A[j][i];
                short s2 = GF2Field.invElem(this.A[i][i]);
                if (s2 == 0) {
                    throw new RuntimeException("The matrix is not invertible");
                }
                for (int k = i; k < 2 * this.A.length; ++k) {
                    short s3 = GF2Field.multElem(s, GF2Field.multElem(this.A[i][k], s2));
                    this.A[j][k] = GF2Field.addElem(this.A[j][k], s3);
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void computeZerosUnder(boolean bl) {
        int n = bl ? 2 * this.A.length : 1 + this.A.length;
        int n2 = 0;
        while (n2 < -1 + this.A.length) {
            for (int i = n2 + 1; i < this.A.length; ++i) {
                short s = this.A[i][n2];
                short s2 = GF2Field.invElem(this.A[n2][n2]);
                if (s2 == 0) {
                    throw new RuntimeException("Matrix not invertible! We have to choose another one!");
                }
                for (int j = n2; j < n; ++j) {
                    short s3 = GF2Field.multElem(s, GF2Field.multElem(this.A[n2][j], s2));
                    this.A[i][j] = GF2Field.addElem(this.A[i][j], s3);
                }
            }
            ++n2;
        }
        return;
    }

    private void substitute() {
        short s = GF2Field.invElem(this.A[-1 + this.A.length][-1 + this.A.length]);
        if (s == 0) {
            throw new RuntimeException("The equation system is not solvable");
        }
        this.x[-1 + this.A.length] = GF2Field.multElem(this.A[-1 + this.A.length][this.A.length], s);
        for (int i = -2 + this.A.length; i >= 0; --i) {
            short s2 = this.A[i][this.A.length];
            for (int j = -1 + this.A.length; j > i; --j) {
                s2 = GF2Field.addElem(s2, GF2Field.multElem(this.A[i][j], this.x[j]));
            }
            short s3 = GF2Field.invElem(this.A[i][i]);
            if (s3 == 0) {
                throw new RuntimeException("Not solvable equation system");
            }
            this.x[i] = GF2Field.multElem(s2, s3);
        }
    }

    public short[][] addSquareMatrix(short[][] arrs, short[][] arrs2) {
        if (arrs.length != arrs2.length || arrs[0].length != arrs2[0].length) {
            throw new RuntimeException("Addition is not possible!");
        }
        int[] arrn = new int[]{arrs.length, arrs.length};
        short[][] arrs3 = (short[][])Array.newInstance((Class)Short.TYPE, (int[])arrn);
        for (int i = 0; i < arrs.length; ++i) {
            for (int j = 0; j < arrs2.length; ++j) {
                arrs3[i][j] = GF2Field.addElem(arrs[i][j], arrs2[i][j]);
            }
        }
        return arrs3;
    }

    public short[] addVect(short[] arrs, short[] arrs2) {
        if (arrs.length != arrs2.length) {
            throw new RuntimeException("Multiplication is not possible!");
        }
        short[] arrs3 = new short[arrs.length];
        for (int i = 0; i < arrs3.length; ++i) {
            arrs3[i] = GF2Field.addElem(arrs[i], arrs2[i]);
        }
        return arrs3;
    }

    /*
     * Exception decompiling
     */
    public short[][] inverse(short[][] var1_1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[TRYBLOCK]], but top level block is 8[FORLOOP]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    public short[][] multMatrix(short s, short[][] arrs) {
        int[] arrn = new int[]{arrs.length, arrs[0].length};
        short[][] arrs2 = (short[][])Array.newInstance((Class)Short.TYPE, (int[])arrn);
        for (int i = 0; i < arrs.length; ++i) {
            for (int j = 0; j < arrs[0].length; ++j) {
                arrs2[i][j] = GF2Field.multElem(s, arrs[i][j]);
            }
        }
        return arrs2;
    }

    public short[] multVect(short s, short[] arrs) {
        short[] arrs2 = new short[arrs.length];
        for (int i = 0; i < arrs2.length; ++i) {
            arrs2[i] = GF2Field.multElem(s, arrs[i]);
        }
        return arrs2;
    }

    public short[][] multVects(short[] arrs, short[] arrs2) {
        if (arrs.length != arrs2.length) {
            throw new RuntimeException("Multiplication is not possible!");
        }
        int[] arrn = new int[]{arrs.length, arrs2.length};
        short[][] arrs3 = (short[][])Array.newInstance((Class)Short.TYPE, (int[])arrn);
        for (int i = 0; i < arrs.length; ++i) {
            for (int j = 0; j < arrs2.length; ++j) {
                arrs3[i][j] = GF2Field.multElem(arrs[i], arrs2[j]);
            }
        }
        return arrs3;
    }

    public short[] multiplyMatrix(short[][] arrs, short[] arrs2) {
        if (arrs[0].length != arrs2.length) {
            throw new RuntimeException("Multiplication is not possible!");
        }
        short[] arrs3 = new short[arrs.length];
        for (int i = 0; i < arrs.length; ++i) {
            for (int j = 0; j < arrs2.length; ++j) {
                short s = GF2Field.multElem(arrs[i][j], arrs2[j]);
                arrs3[i] = GF2Field.addElem(arrs3[i], s);
            }
        }
        return arrs3;
    }

    public short[][] multiplyMatrix(short[][] arrs, short[][] arrs2) {
        if (arrs[0].length != arrs2.length) {
            throw new RuntimeException("Multiplication is not possible!");
        }
        int[] arrn = new int[]{arrs.length, arrs2[0].length};
        this.A = (short[][])Array.newInstance((Class)Short.TYPE, (int[])arrn);
        for (int i = 0; i < arrs.length; ++i) {
            for (int j = 0; j < arrs2.length; ++j) {
                for (int k = 0; k < arrs2[0].length; ++k) {
                    short s = GF2Field.multElem(arrs[i][j], arrs2[j][k]);
                    this.A[i][k] = GF2Field.addElem(this.A[i][k], s);
                }
            }
        }
        return this.A;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public short[] solveEquation(short[][] arrs, short[] arrs2) {
        try {
            if (arrs.length != arrs2.length) {
                throw new RuntimeException("The equation system is not solvable");
            }
            int[] arrn = new int[]{arrs.length, 1 + arrs.length};
            this.A = (short[][])Array.newInstance((Class)Short.TYPE, (int[])arrn);
            this.x = new short[arrs.length];
            int n = 0;
            do {
                if (n >= arrs.length) break;
                for (int i = 0; i < arrs[0].length; ++i) {
                    this.A[n][i] = arrs[n][i];
                }
                ++n;
            } while (true);
            for (int i = 0; i < arrs2.length; ++i) {
                this.A[i][arrs2.length] = GF2Field.addElem(arrs2[i], this.A[i][arrs2.length]);
            }
        }
        catch (RuntimeException runtimeException) {
            return null;
        }
        this.computeZerosUnder(false);
        this.substitute();
        return this.x;
    }
}

