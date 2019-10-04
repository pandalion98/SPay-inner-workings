/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.engines.RFC5649WrapEngine;

public class AESWrapPadEngine
extends RFC5649WrapEngine {
    public AESWrapPadEngine() {
        super(new AESEngine());
    }
}

