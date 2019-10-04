/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.PublicKey
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.jce.interfaces;

import java.security.PublicKey;
import org.bouncycastle.jce.interfaces.ECKey;
import org.bouncycastle.math.ec.ECPoint;

public interface ECPublicKey
extends PublicKey,
ECKey {
    public ECPoint getQ();
}

