/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.engines.RFC3394WrapEngine;

public class AESWrapEngine
extends RFC3394WrapEngine {
    public AESWrapEngine() {
        super(new AESEngine());
    }

    public AESWrapEngine(boolean bl) {
        super(new AESEngine(), bl);
    }
}

