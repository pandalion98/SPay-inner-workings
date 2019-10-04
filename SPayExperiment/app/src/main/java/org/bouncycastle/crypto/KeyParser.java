/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.lang.Object
 */
package org.bouncycastle.crypto;

import java.io.InputStream;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public interface KeyParser {
    public AsymmetricKeyParameter readKey(InputStream var1);
}

