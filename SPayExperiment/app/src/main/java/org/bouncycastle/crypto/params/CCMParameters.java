/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;

public class CCMParameters
extends AEADParameters {
    public CCMParameters(KeyParameter keyParameter, int n2, byte[] arrby, byte[] arrby2) {
        super(keyParameter, n2, arrby, arrby2);
    }
}

