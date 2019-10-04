/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.security.PublicKey
 */
package org.bouncycastle.jce.interfaces;

import java.math.BigInteger;
import java.security.PublicKey;
import org.bouncycastle.jce.interfaces.GOST3410Key;

public interface GOST3410PublicKey
extends PublicKey,
GOST3410Key {
    public BigInteger getY();
}

