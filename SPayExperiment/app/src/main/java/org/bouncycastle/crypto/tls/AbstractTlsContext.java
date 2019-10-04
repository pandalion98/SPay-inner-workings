/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.security.SecureRandom
 *  org.bouncycastle.util.Times
 */
package org.bouncycastle.crypto.tls;

import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.prng.DigestRandomGenerator;
import org.bouncycastle.crypto.prng.RandomGenerator;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsSession;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Times;

abstract class AbstractTlsContext
implements TlsContext {
    private static long counter = Times.nanoTime();
    private ProtocolVersion clientVersion = null;
    private RandomGenerator nonceRandom;
    private SecureRandom secureRandom;
    private SecurityParameters securityParameters;
    private ProtocolVersion serverVersion = null;
    private TlsSession session = null;
    private Object userObject = null;

    AbstractTlsContext(SecureRandom secureRandom, SecurityParameters securityParameters) {
        Digest digest = TlsUtils.createHash((short)4);
        byte[] arrby = new byte[digest.getDigestSize()];
        secureRandom.nextBytes(arrby);
        this.nonceRandom = new DigestRandomGenerator(digest);
        this.nonceRandom.addSeedMaterial(AbstractTlsContext.nextCounterValue());
        this.nonceRandom.addSeedMaterial(Times.nanoTime());
        this.nonceRandom.addSeedMaterial(arrby);
        this.secureRandom = secureRandom;
        this.securityParameters = securityParameters;
    }

    private static long nextCounterValue() {
        Class<AbstractTlsContext> class_ = AbstractTlsContext.class;
        synchronized (AbstractTlsContext.class) {
            long l2;
            counter = l2 = 1L + counter;
            // ** MonitorExit[var3] (shouldn't be in output)
            return l2;
        }
    }

    @Override
    public byte[] exportKeyingMaterial(String string, byte[] arrby, int n2) {
        if (arrby != null && !TlsUtils.isValidUint16(arrby.length)) {
            throw new IllegalArgumentException("'context_value' must have length less than 2^16 (or be null)");
        }
        SecurityParameters securityParameters = this.getSecurityParameters();
        byte[] arrby2 = securityParameters.getClientRandom();
        byte[] arrby3 = securityParameters.getServerRandom();
        int n3 = arrby2.length + arrby3.length;
        if (arrby != null) {
            n3 += 2 + arrby.length;
        }
        byte[] arrby4 = new byte[n3];
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby4, (int)0, (int)arrby2.length);
        int n4 = 0 + arrby2.length;
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby4, (int)n4, (int)arrby3.length);
        int n5 = n4 + arrby3.length;
        if (arrby != null) {
            TlsUtils.writeUint16(arrby.length, arrby4, n5);
            int n6 = n5 + 2;
            System.arraycopy((Object)arrby, (int)0, (Object)arrby4, (int)n6, (int)arrby.length);
            n5 = n6 + arrby.length;
        }
        if (n5 != n3) {
            throw new IllegalStateException("error in calculation of seed for export");
        }
        return TlsUtils.PRF(this, securityParameters.getMasterSecret(), string, arrby4, n2);
    }

    @Override
    public ProtocolVersion getClientVersion() {
        return this.clientVersion;
    }

    @Override
    public RandomGenerator getNonceRandomGenerator() {
        return this.nonceRandom;
    }

    @Override
    public TlsSession getResumableSession() {
        return this.session;
    }

    @Override
    public SecureRandom getSecureRandom() {
        return this.secureRandom;
    }

    @Override
    public SecurityParameters getSecurityParameters() {
        return this.securityParameters;
    }

    @Override
    public ProtocolVersion getServerVersion() {
        return this.serverVersion;
    }

    @Override
    public Object getUserObject() {
        return this.userObject;
    }

    void setClientVersion(ProtocolVersion protocolVersion) {
        this.clientVersion = protocolVersion;
    }

    void setResumableSession(TlsSession tlsSession) {
        this.session = tlsSession;
    }

    void setServerVersion(ProtocolVersion protocolVersion) {
        this.serverVersion = protocolVersion;
    }

    @Override
    public void setUserObject(Object object) {
        this.userObject = object;
    }
}

