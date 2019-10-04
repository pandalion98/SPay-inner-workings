/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.util.Arrays;

public class HKDFParameters
implements DerivationParameters {
    private final byte[] ikm;
    private final byte[] info;
    private final byte[] salt;
    private final boolean skipExpand;

    /*
     * Enabled aggressive block sorting
     */
    private HKDFParameters(byte[] arrby, boolean bl, byte[] arrby2, byte[] arrby3) {
        if (arrby == null) {
            throw new IllegalArgumentException("IKM (input keying material) should not be null");
        }
        this.ikm = Arrays.clone((byte[])arrby);
        this.skipExpand = bl;
        this.salt = arrby2 == null || arrby2.length == 0 ? null : Arrays.clone((byte[])arrby2);
        if (arrby3 == null) {
            this.info = new byte[0];
            return;
        }
        this.info = Arrays.clone((byte[])arrby3);
    }

    public HKDFParameters(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        this(arrby, false, arrby2, arrby3);
    }

    public static HKDFParameters defaultParameters(byte[] arrby) {
        return new HKDFParameters(arrby, false, null, null);
    }

    public static HKDFParameters skipExtractParameters(byte[] arrby, byte[] arrby2) {
        return new HKDFParameters(arrby, true, null, arrby2);
    }

    public byte[] getIKM() {
        return Arrays.clone((byte[])this.ikm);
    }

    public byte[] getInfo() {
        return Arrays.clone((byte[])this.info);
    }

    public byte[] getSalt() {
        return Arrays.clone((byte[])this.salt);
    }

    public boolean skipExtract() {
        return this.skipExpand;
    }
}

