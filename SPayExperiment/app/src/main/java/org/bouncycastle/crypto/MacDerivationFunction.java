/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.Mac;

public interface MacDerivationFunction
extends DerivationFunction {
    public Mac getMac();
}

