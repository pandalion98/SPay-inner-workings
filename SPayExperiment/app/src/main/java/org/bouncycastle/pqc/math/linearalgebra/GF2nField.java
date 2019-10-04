/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Vector
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.math.BigInteger;
import java.util.Vector;
import org.bouncycastle.pqc.math.linearalgebra.GF2Polynomial;
import org.bouncycastle.pqc.math.linearalgebra.GF2nElement;
import org.bouncycastle.pqc.math.linearalgebra.GF2nONBElement;
import org.bouncycastle.pqc.math.linearalgebra.GF2nONBField;
import org.bouncycastle.pqc.math.linearalgebra.GF2nPolynomialElement;
import org.bouncycastle.pqc.math.linearalgebra.GF2nPolynomialField;

public abstract class GF2nField {
    protected GF2Polynomial fieldPolynomial;
    protected Vector fields;
    protected int mDegree;
    protected Vector matrices;

    protected abstract void computeCOBMatrix(GF2nField var1);

    protected abstract void computeFieldPolynomial();

    public final GF2nElement convert(GF2nElement gF2nElement, GF2nField gF2nField) {
        if (gF2nField == this) {
            return (GF2nElement)gF2nElement.clone();
        }
        if (this.fieldPolynomial.equals(gF2nField.fieldPolynomial)) {
            return (GF2nElement)gF2nElement.clone();
        }
        if (this.mDegree != gF2nField.mDegree) {
            throw new RuntimeException("GF2nField.convert: B1 has a different degree and thus cannot be coverted to!");
        }
        int n = this.fields.indexOf((Object)gF2nField);
        if (n == -1) {
            this.computeCOBMatrix(gF2nField);
            n = this.fields.indexOf((Object)gF2nField);
        }
        GF2Polynomial[] arrgF2Polynomial = (GF2Polynomial[])this.matrices.elementAt(n);
        GF2nElement gF2nElement2 = (GF2nElement)gF2nElement.clone();
        if (gF2nElement2 instanceof GF2nONBElement) {
            ((GF2nONBElement)gF2nElement2).reverseOrder();
        }
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this.mDegree, gF2nElement2.toFlexiBigInt());
        gF2Polynomial.expandN(this.mDegree);
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this.mDegree);
        for (int i = 0; i < this.mDegree; ++i) {
            if (!gF2Polynomial.vectorMult(arrgF2Polynomial[i])) continue;
            gF2Polynomial2.setBit(-1 + this.mDegree - i);
        }
        if (gF2nField instanceof GF2nPolynomialField) {
            return new GF2nPolynomialElement((GF2nPolynomialField)gF2nField, gF2Polynomial2);
        }
        if (gF2nField instanceof GF2nONBField) {
            GF2nONBElement gF2nONBElement = new GF2nONBElement((GF2nONBField)gF2nField, gF2Polynomial2.toFlexiBigInt());
            gF2nONBElement.reverseOrder();
            return gF2nONBElement;
        }
        throw new RuntimeException("GF2nField.convert: B1 must be an instance of GF2nPolynomialField or GF2nONBField!");
    }

    /*
     * Enabled aggressive block sorting
     */
    public final boolean equals(Object object) {
        block3 : {
            block2 : {
                if (object == null || !(object instanceof GF2nField)) break block2;
                GF2nField gF2nField = (GF2nField)object;
                if (gF2nField.mDegree == this.mDegree && this.fieldPolynomial.equals(gF2nField.fieldPolynomial) && (!(this instanceof GF2nPolynomialField) || gF2nField instanceof GF2nPolynomialField) && (!(this instanceof GF2nONBField) || gF2nField instanceof GF2nONBField)) break block3;
            }
            return false;
        }
        return true;
    }

    public final int getDegree() {
        return this.mDegree;
    }

    public final GF2Polynomial getFieldPolynomial() {
        if (this.fieldPolynomial == null) {
            this.computeFieldPolynomial();
        }
        return new GF2Polynomial(this.fieldPolynomial);
    }

    protected abstract GF2nElement getRandomRoot(GF2Polynomial var1);

    public int hashCode() {
        return this.mDegree + this.fieldPolynomial.hashCode();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected final GF2Polynomial[] invertMatrix(GF2Polynomial[] var1_1) {
        var2_2 = new GF2Polynomial[var1_1.length];
        var3_3 = new GF2Polynomial[var1_1.length];
        var4_4 = 0;
        do {
            var5_5 = this.mDegree;
            var6_6 = 0;
            if (var4_4 >= var5_5) ** GOTO lbl18
            try {
                var2_2[var4_4] = new GF2Polynomial(var1_1[var4_4]);
                var3_3[var4_4] = new GF2Polynomial(this.mDegree);
                var3_3[var4_4].setBit(-1 + this.mDegree - var4_4);
            }
            catch (RuntimeException var13_7) {
                var13_7.printStackTrace();
            }
            ++var4_4;
        } while (true);
        {
            ++var6_6;
lbl18: // 2 sources:
            if (var6_6 >= -1 + this.mDegree) break;
            for (var9_8 = var6_6; var9_8 < this.mDegree && !var2_2[var9_8].testBit(-1 + this.mDegree - var6_6); ++var9_8) {
            }
            if (var9_8 >= this.mDegree) {
                throw new RuntimeException("GF2nField.invertMatrix: Matrix cannot be inverted!");
            }
            if (var6_6 != var9_8) {
                var11_10 = var2_2[var6_6];
                var2_2[var6_6] = var2_2[var9_8];
                var2_2[var9_8] = var11_10;
                var12_11 = var3_3[var6_6];
                var3_3[var6_6] = var3_3[var9_8];
                var3_3[var9_8] = var12_11;
            }
            var10_9 = var6_6 + 1;
            do {
                if (var10_9 >= this.mDegree) continue block3;
                if (var2_2[var10_9].testBit(-1 + this.mDegree - var6_6)) {
                    var2_2[var10_9].addToThis(var2_2[var6_6]);
                    var3_3[var10_9].addToThis(var3_3[var6_6]);
                }
                ++var10_9;
            } while (true);
        }
        var7_12 = -1 + this.mDegree;
        while (var7_12 > 0) {
            for (var8_13 = var7_12 - 1; var8_13 >= 0; --var8_13) {
                if (!var2_2[var8_13].testBit(-1 + this.mDegree - var7_12)) continue;
                var2_2[var8_13].addToThis(var2_2[var7_12]);
                var3_3[var8_13].addToThis(var3_3[var7_12]);
            }
            --var7_12;
        }
        return var3_3;
    }
}

