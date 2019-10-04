/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  javax.crypto.spec.PBEKeySpec
 */
package org.bouncycastle.jcajce.spec;

import javax.crypto.spec.PBEKeySpec;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class PBKDF2KeySpec
extends PBEKeySpec {
    private AlgorithmIdentifier prf;

    public PBKDF2KeySpec(char[] arrc, byte[] arrby, int n, int n2, AlgorithmIdentifier algorithmIdentifier) {
        super(arrc, arrby, n, n2);
        this.prf = algorithmIdentifier;
    }

    public AlgorithmIdentifier getPrf() {
        return this.prf;
    }
}

