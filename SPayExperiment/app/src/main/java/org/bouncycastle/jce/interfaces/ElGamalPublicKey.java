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
import org.bouncycastle.jce.interfaces.ElGamalKey;

public interface ElGamalPublicKey
extends PublicKey,
ElGamalKey {
    public BigInteger getY();
}

