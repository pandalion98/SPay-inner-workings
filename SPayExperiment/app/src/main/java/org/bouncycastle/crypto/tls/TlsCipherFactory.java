/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsContext;

public interface TlsCipherFactory {
    public TlsCipher createCipher(TlsContext var1, int var2, int var3);
}

