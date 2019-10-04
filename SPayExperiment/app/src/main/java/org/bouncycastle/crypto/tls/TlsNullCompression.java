/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import java.io.OutputStream;
import org.bouncycastle.crypto.tls.TlsCompression;

public class TlsNullCompression
implements TlsCompression {
    @Override
    public OutputStream compress(OutputStream outputStream) {
        return outputStream;
    }

    @Override
    public OutputStream decompress(OutputStream outputStream) {
        return outputStream;
    }
}

