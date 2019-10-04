/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.PublicKey
 */
package org.bouncycastle.jce.interfaces;

import java.security.PublicKey;

public interface MQVPublicKey
extends PublicKey {
    public PublicKey getEphemeralKey();

    public PublicKey getStaticKey();
}

