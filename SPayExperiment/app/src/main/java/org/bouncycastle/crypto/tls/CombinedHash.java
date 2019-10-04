/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.tls.SSL3Mac;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsHandshakeHash;
import org.bouncycastle.crypto.tls.TlsUtils;

class CombinedHash
implements TlsHandshakeHash {
    protected TlsContext context;
    protected Digest md5;
    protected Digest sha1;

    CombinedHash() {
        this.md5 = TlsUtils.createHash((short)1);
        this.sha1 = TlsUtils.createHash((short)2);
    }

    CombinedHash(CombinedHash combinedHash) {
        this.context = combinedHash.context;
        this.md5 = TlsUtils.cloneHash((short)1, combinedHash.md5);
        this.sha1 = TlsUtils.cloneHash((short)2, combinedHash.sha1);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        if (this.context != null && TlsUtils.isSSL(this.context)) {
            this.ssl3Complete(this.md5, SSL3Mac.IPAD, SSL3Mac.OPAD, 48);
            this.ssl3Complete(this.sha1, SSL3Mac.IPAD, SSL3Mac.OPAD, 40);
        }
        int n3 = this.md5.doFinal(arrby, n2);
        return n3 + this.sha1.doFinal(arrby, n2 + n3);
    }

    @Override
    public Digest forkPRFHash() {
        return new CombinedHash(this);
    }

    @Override
    public String getAlgorithmName() {
        return this.md5.getAlgorithmName() + " and " + this.sha1.getAlgorithmName();
    }

    @Override
    public int getDigestSize() {
        return this.md5.getDigestSize() + this.sha1.getDigestSize();
    }

    @Override
    public byte[] getFinalHash(short s2) {
        throw new IllegalStateException("CombinedHash doesn't support multiple hashes");
    }

    @Override
    public void init(TlsContext tlsContext) {
        this.context = tlsContext;
    }

    @Override
    public TlsHandshakeHash notifyPRFDetermined() {
        return this;
    }

    @Override
    public void reset() {
        this.md5.reset();
        this.sha1.reset();
    }

    @Override
    public void sealHashAlgorithms() {
    }

    protected void ssl3Complete(Digest digest, byte[] arrby, byte[] arrby2, int n2) {
        byte[] arrby3 = this.context.getSecurityParameters().masterSecret;
        digest.update(arrby3, 0, arrby3.length);
        digest.update(arrby, 0, n2);
        byte[] arrby4 = new byte[digest.getDigestSize()];
        digest.doFinal(arrby4, 0);
        digest.update(arrby3, 0, arrby3.length);
        digest.update(arrby2, 0, n2);
        digest.update(arrby4, 0, arrby4.length);
    }

    @Override
    public TlsHandshakeHash stopTracking() {
        return new CombinedHash(this);
    }

    @Override
    public void trackHashAlgorithm(short s2) {
        throw new IllegalStateException("CombinedHash only supports calculating the legacy PRF for handshake hash");
    }

    @Override
    public void update(byte by) {
        this.md5.update(by);
        this.sha1.update(by);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        this.md5.update(arrby, n2, n3);
        this.sha1.update(arrby, n2, n3);
    }
}

