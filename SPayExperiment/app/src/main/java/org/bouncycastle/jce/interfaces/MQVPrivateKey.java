/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.PrivateKey
 *  java.security.PublicKey
 */
package org.bouncycastle.jce.interfaces;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface MQVPrivateKey
extends PrivateKey {
    public PrivateKey getEphemeralPrivateKey();

    public PublicKey getEphemeralPublicKey();

    public PrivateKey getStaticPrivateKey();
}

