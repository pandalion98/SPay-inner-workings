/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.jce.interfaces;

import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;

public interface GOST3410Params {
    public String getDigestParamSetOID();

    public String getEncryptionParamSetOID();

    public String getPublicKeyParamSetOID();

    public GOST3410PublicKeyParameterSetSpec getPublicKeyParameters();
}

