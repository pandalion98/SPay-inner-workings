/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public interface KeyEncoder {
    public byte[] getEncoded(AsymmetricKeyParameter var1);
}

