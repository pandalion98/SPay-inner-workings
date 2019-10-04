/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.generators.BaseKDFBytesGenerator;

public class KDF2BytesGenerator
extends BaseKDFBytesGenerator {
    public KDF2BytesGenerator(Digest digest) {
        super(1, digest);
    }
}

