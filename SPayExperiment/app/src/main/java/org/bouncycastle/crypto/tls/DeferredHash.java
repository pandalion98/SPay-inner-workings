/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.Short
 *  java.lang.String
 *  java.util.Enumeration
 *  java.util.Hashtable
 *  org.bouncycastle.util.Shorts
 */
package org.bouncycastle.crypto.tls;

import java.util.Enumeration;
import java.util.Hashtable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.tls.CombinedHash;
import org.bouncycastle.crypto.tls.DigestInputBuffer;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsHandshakeHash;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Shorts;

class DeferredHash
implements TlsHandshakeHash {
    protected static final int BUFFERING_HASH_LIMIT = 4;
    private DigestInputBuffer buf;
    protected TlsContext context;
    private Hashtable hashes;
    private Short prfHashAlgorithm;

    DeferredHash() {
        this.buf = new DigestInputBuffer();
        this.hashes = new Hashtable();
        this.prfHashAlgorithm = null;
    }

    private DeferredHash(Short s2, Digest digest) {
        this.buf = null;
        this.hashes = new Hashtable();
        this.prfHashAlgorithm = s2;
        this.hashes.put((Object)s2, (Object)digest);
    }

    protected void checkStopBuffering() {
        if (this.buf != null && this.hashes.size() <= 4) {
            Enumeration enumeration = this.hashes.elements();
            while (enumeration.hasMoreElements()) {
                Digest digest = (Digest)enumeration.nextElement();
                this.buf.updateDigest(digest);
            }
            this.buf = null;
        }
    }

    protected void checkTrackingHash(Short s2) {
        if (!this.hashes.containsKey((Object)s2)) {
            Digest digest = TlsUtils.createHash(s2);
            this.hashes.put((Object)s2, (Object)digest);
        }
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        throw new IllegalStateException("Use fork() to get a definite Digest");
    }

    @Override
    public Digest forkPRFHash() {
        this.checkStopBuffering();
        if (this.buf != null) {
            Digest digest = TlsUtils.createHash(this.prfHashAlgorithm);
            this.buf.updateDigest(digest);
            return digest;
        }
        return TlsUtils.cloneHash(this.prfHashAlgorithm, (Digest)this.hashes.get((Object)this.prfHashAlgorithm));
    }

    @Override
    public String getAlgorithmName() {
        throw new IllegalStateException("Use fork() to get a definite Digest");
    }

    @Override
    public int getDigestSize() {
        throw new IllegalStateException("Use fork() to get a definite Digest");
    }

    @Override
    public byte[] getFinalHash(short s2) {
        Digest digest = (Digest)this.hashes.get((Object)Shorts.valueOf((short)s2));
        if (digest == null) {
            throw new IllegalStateException("HashAlgorithm " + s2 + " is not being tracked");
        }
        Digest digest2 = TlsUtils.cloneHash(s2, digest);
        if (this.buf != null) {
            this.buf.updateDigest(digest2);
        }
        byte[] arrby = new byte[digest2.getDigestSize()];
        digest2.doFinal(arrby, 0);
        return arrby;
    }

    @Override
    public void init(TlsContext tlsContext) {
        this.context = tlsContext;
    }

    @Override
    public TlsHandshakeHash notifyPRFDetermined() {
        int n2 = this.context.getSecurityParameters().getPrfAlgorithm();
        if (n2 == 0) {
            CombinedHash combinedHash = new CombinedHash();
            combinedHash.init(this.context);
            this.buf.updateDigest(combinedHash);
            return combinedHash.notifyPRFDetermined();
        }
        this.prfHashAlgorithm = Shorts.valueOf((short)TlsUtils.getHashAlgorithmForPRFAlgorithm(n2));
        this.checkTrackingHash(this.prfHashAlgorithm);
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void reset() {
        if (this.buf != null) {
            this.buf.reset();
            return;
        } else {
            Enumeration enumeration = this.hashes.elements();
            while (enumeration.hasMoreElements()) {
                ((Digest)enumeration.nextElement()).reset();
            }
        }
    }

    @Override
    public void sealHashAlgorithms() {
        this.checkStopBuffering();
    }

    @Override
    public TlsHandshakeHash stopTracking() {
        Digest digest = TlsUtils.cloneHash(this.prfHashAlgorithm, (Digest)this.hashes.get((Object)this.prfHashAlgorithm));
        if (this.buf != null) {
            this.buf.updateDigest(digest);
        }
        DeferredHash deferredHash = new DeferredHash(this.prfHashAlgorithm, digest);
        deferredHash.init(this.context);
        return deferredHash;
    }

    @Override
    public void trackHashAlgorithm(short s2) {
        if (this.buf == null) {
            throw new IllegalStateException("Too late to track more hash algorithms");
        }
        this.checkTrackingHash(Shorts.valueOf((short)s2));
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void update(byte by) {
        if (this.buf != null) {
            this.buf.write((int)by);
            return;
        } else {
            Enumeration enumeration = this.hashes.elements();
            while (enumeration.hasMoreElements()) {
                ((Digest)enumeration.nextElement()).update(by);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void update(byte[] arrby, int n2, int n3) {
        if (this.buf != null) {
            this.buf.write(arrby, n2, n3);
            return;
        } else {
            Enumeration enumeration = this.hashes.elements();
            while (enumeration.hasMoreElements()) {
                ((Digest)enumeration.nextElement()).update(arrby, n2, n3);
            }
        }
    }
}

