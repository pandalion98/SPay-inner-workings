/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.params;

public class GOST3410ValidationParameters {
    private int c;
    private long cL;
    private int x0;
    private long x0L;

    public GOST3410ValidationParameters(int n2, int n3) {
        this.x0 = n2;
        this.c = n3;
    }

    public GOST3410ValidationParameters(long l2, long l3) {
        this.x0L = l2;
        this.cL = l3;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (!(object instanceof GOST3410ValidationParameters)) break block2;
                GOST3410ValidationParameters gOST3410ValidationParameters = (GOST3410ValidationParameters)object;
                if (gOST3410ValidationParameters.c == this.c && gOST3410ValidationParameters.x0 == this.x0 && gOST3410ValidationParameters.cL == this.cL && gOST3410ValidationParameters.x0L == this.x0L) break block3;
            }
            return false;
        }
        return true;
    }

    public int getC() {
        return this.c;
    }

    public long getCL() {
        return this.cL;
    }

    public int getX0() {
        return this.x0;
    }

    public long getX0L() {
        return this.x0L;
    }

    public int hashCode() {
        return this.x0 ^ this.c ^ (int)this.x0L ^ (int)(this.x0L >> 32) ^ (int)this.cL ^ (int)(this.cL >> 32);
    }
}

