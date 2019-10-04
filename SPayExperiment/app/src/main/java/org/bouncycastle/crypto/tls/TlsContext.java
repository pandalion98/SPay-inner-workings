/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.tls;

import java.security.SecureRandom;
import org.bouncycastle.crypto.prng.RandomGenerator;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.TlsSession;

public interface TlsContext {
    public byte[] exportKeyingMaterial(String var1, byte[] var2, int var3);

    public ProtocolVersion getClientVersion();

    public RandomGenerator getNonceRandomGenerator();

    public TlsSession getResumableSession();

    public SecureRandom getSecureRandom();

    public SecurityParameters getSecurityParameters();

    public ProtocolVersion getServerVersion();

    public Object getUserObject();

    public boolean isServer();

    public void setUserObject(Object var1);
}

