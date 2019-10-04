/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.generators.BaseKDFBytesGenerator;

public class KDF1BytesGenerator
extends BaseKDFBytesGenerator {
    public KDF1BytesGenerator(Digest digest) {
        super(0, digest);
    }
}

