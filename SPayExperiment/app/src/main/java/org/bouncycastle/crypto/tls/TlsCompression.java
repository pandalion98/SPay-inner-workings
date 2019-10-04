/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import java.io.OutputStream;

public interface TlsCompression {
    public OutputStream compress(OutputStream var1);

    public OutputStream decompress(OutputStream var1);
}

