/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.engines.CamelliaEngine;
import org.bouncycastle.crypto.engines.RFC3394WrapEngine;

public class CamelliaWrapEngine
extends RFC3394WrapEngine {
    public CamelliaWrapEngine() {
        super(new CamelliaEngine());
    }
}

