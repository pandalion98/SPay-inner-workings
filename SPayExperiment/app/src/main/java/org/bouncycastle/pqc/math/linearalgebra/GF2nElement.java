/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.pqc.math.linearalgebra;

import org.bouncycastle.pqc.math.linearalgebra.GF2nField;
import org.bouncycastle.pqc.math.linearalgebra.GFElement;

public abstract class GF2nElement
implements GFElement {
    protected int mDegree;
    protected GF2nField mField;

    abstract void assignOne();

    abstract void assignZero();

    @Override
    public abstract Object clone();

    public final GF2nElement convert(GF2nField gF2nField) {
        return this.mField.convert(this, gF2nField);
    }

    public final GF2nField getField() {
        return this.mField;
    }

    public abstract GF2nElement increase();

    public abstract void increaseThis();

    public abstract GF2nElement solveQuadraticEquation();

    public abstract GF2nElement square();

    public abstract GF2nElement squareRoot();

    public abstract void squareRootThis();

    public abstract void squareThis();

    @Override
    public final GFElement subtract(GFElement gFElement) {
        return this.add(gFElement);
    }

    @Override
    public final void subtractFromThis(GFElement gFElement) {
        this.addToThis(gFElement);
    }

    abstract boolean testBit(int var1);

    public abstract boolean testRightmostBit();

    public abstract int trace();
}

