package org.bouncycastle.crypto.tls;

import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.prng.DigestRandomGenerator;
import org.bouncycastle.crypto.prng.RandomGenerator;
import org.bouncycastle.util.Times;

abstract class AbstractTlsContext implements TlsContext {
    private static long counter;
    private ProtocolVersion clientVersion;
    private RandomGenerator nonceRandom;
    private SecureRandom secureRandom;
    private SecurityParameters securityParameters;
    private ProtocolVersion serverVersion;
    private TlsSession session;
    private Object userObject;

    static {
        counter = Times.nanoTime();
    }

    AbstractTlsContext(SecureRandom secureRandom, SecurityParameters securityParameters) {
        this.clientVersion = null;
        this.serverVersion = null;
        this.session = null;
        this.userObject = null;
        Digest createHash = TlsUtils.createHash((short) 4);
        byte[] bArr = new byte[createHash.getDigestSize()];
        secureRandom.nextBytes(bArr);
        this.nonceRandom = new DigestRandomGenerator(createHash);
        this.nonceRandom.addSeedMaterial(nextCounterValue());
        this.nonceRandom.addSeedMaterial(Times.nanoTime());
        this.nonceRandom.addSeedMaterial(bArr);
        this.secureRandom = secureRandom;
        this.securityParameters = securityParameters;
    }

    private static synchronized long nextCounterValue() {
        long j;
        synchronized (AbstractTlsContext.class) {
            j = counter + 1;
            counter = j;
        }
        return j;
    }

    public byte[] exportKeyingMaterial(String str, byte[] bArr, int i) {
        if (bArr == null || TlsUtils.isValidUint16(bArr.length)) {
            SecurityParameters securityParameters = getSecurityParameters();
            Object clientRandom = securityParameters.getClientRandom();
            Object serverRandom = securityParameters.getServerRandom();
            int length = clientRandom.length + serverRandom.length;
            if (bArr != null) {
                length += bArr.length + 2;
            }
            Object obj = new byte[length];
            System.arraycopy(clientRandom, 0, obj, 0, clientRandom.length);
            int length2 = clientRandom.length + 0;
            System.arraycopy(serverRandom, 0, obj, length2, serverRandom.length);
            length2 += serverRandom.length;
            if (bArr != null) {
                TlsUtils.writeUint16(bArr.length, obj, length2);
                length2 += 2;
                System.arraycopy(bArr, 0, obj, length2, bArr.length);
                length2 += bArr.length;
            }
            if (length2 == length) {
                return TlsUtils.PRF(this, securityParameters.getMasterSecret(), str, obj, i);
            }
            throw new IllegalStateException("error in calculation of seed for export");
        }
        throw new IllegalArgumentException("'context_value' must have length less than 2^16 (or be null)");
    }

    public ProtocolVersion getClientVersion() {
        return this.clientVersion;
    }

    public RandomGenerator getNonceRandomGenerator() {
        return this.nonceRandom;
    }

    public TlsSession getResumableSession() {
        return this.session;
    }

    public SecureRandom getSecureRandom() {
        return this.secureRandom;
    }

    public SecurityParameters getSecurityParameters() {
        return this.securityParameters;
    }

    public ProtocolVersion getServerVersion() {
        return this.serverVersion;
    }

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

    public void setUserObject(Object obj) {
        this.userObject = obj;
    }
}
