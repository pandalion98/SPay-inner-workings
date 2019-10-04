/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.engines.RFC3394WrapEngine;
import org.bouncycastle.crypto.engines.SEEDEngine;

public class SEEDWrapEngine
extends RFC3394WrapEngine {
    public SEEDWrapEngine() {
        super(new SEEDEngine());
    }
}

