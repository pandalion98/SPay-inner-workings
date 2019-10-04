/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.TlsUtils;

public class UseSRTPData {
    protected byte[] mki;
    protected int[] protectionProfiles;

    /*
     * Enabled aggressive block sorting
     */
    public UseSRTPData(int[] arrn, byte[] arrby) {
        if (arrn == null || arrn.length < 1 || arrn.length >= 32768) {
            throw new IllegalArgumentException("'protectionProfiles' must have length from 1 to (2^15 - 1)");
        }
        if (arrby == null) {
            arrby = TlsUtils.EMPTY_BYTES;
        } else if (arrby.length > 255) {
            throw new IllegalArgumentException("'mki' cannot be longer than 255 bytes");
        }
        this.protectionProfiles = arrn;
        this.mki = arrby;
    }

    public byte[] getMki() {
        return this.mki;
    }

    public int[] getProtectionProfiles() {
        return this.protectionProfiles;
    }
}

