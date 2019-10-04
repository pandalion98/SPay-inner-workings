/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.Key
 *  java.security.PrivateKey
 *  java.security.PublicKey
 */
package org.bouncycastle.jce.interfaces;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface IESKey
extends Key {
    public PrivateKey getPrivate();

    public PublicKey getPublic();
}

