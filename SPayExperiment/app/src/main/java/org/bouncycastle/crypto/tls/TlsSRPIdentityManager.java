/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.TlsSRPLoginParameters;

public interface TlsSRPIdentityManager {
    public TlsSRPLoginParameters getLoginParameters(byte[] var1);
}

