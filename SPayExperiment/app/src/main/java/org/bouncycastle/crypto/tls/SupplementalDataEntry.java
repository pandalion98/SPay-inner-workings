/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

public class SupplementalDataEntry {
    protected byte[] data;
    protected int dataType;

    public SupplementalDataEntry(int n2, byte[] arrby) {
        this.dataType = n2;
        this.data = arrby;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getDataType() {
        return this.dataType;
    }
}

