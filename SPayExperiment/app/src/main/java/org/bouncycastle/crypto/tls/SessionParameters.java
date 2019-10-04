/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.OutputStream
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Hashtable
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.TlsProtocol;
import org.bouncycastle.util.Arrays;

public final class SessionParameters {
    private int cipherSuite;
    private short compressionAlgorithm;
    private byte[] encodedServerExtensions;
    private byte[] masterSecret;
    private Certificate peerCertificate;
    private byte[] pskIdentity = null;
    private byte[] srpIdentity = null;

    private SessionParameters(int n2, short s2, byte[] arrby, Certificate certificate, byte[] arrby2, byte[] arrby3, byte[] arrby4) {
        this.cipherSuite = n2;
        this.compressionAlgorithm = s2;
        this.masterSecret = Arrays.clone((byte[])arrby);
        this.peerCertificate = certificate;
        this.pskIdentity = Arrays.clone((byte[])arrby2);
        this.srpIdentity = Arrays.clone((byte[])arrby3);
        this.encodedServerExtensions = arrby4;
    }

    public void clear() {
        if (this.masterSecret != null) {
            Arrays.fill((byte[])this.masterSecret, (byte)0);
        }
    }

    public SessionParameters copy() {
        return new SessionParameters(this.cipherSuite, this.compressionAlgorithm, this.masterSecret, this.peerCertificate, this.pskIdentity, this.srpIdentity, this.encodedServerExtensions);
    }

    public int getCipherSuite() {
        return this.cipherSuite;
    }

    public short getCompressionAlgorithm() {
        return this.compressionAlgorithm;
    }

    public byte[] getMasterSecret() {
        return this.masterSecret;
    }

    public byte[] getPSKIdentity() {
        return this.pskIdentity;
    }

    public Certificate getPeerCertificate() {
        return this.peerCertificate;
    }

    public byte[] getPskIdentity() {
        return this.pskIdentity;
    }

    public byte[] getSRPIdentity() {
        return this.srpIdentity;
    }

    public Hashtable readServerExtensions() {
        if (this.encodedServerExtensions == null) {
            return null;
        }
        return TlsProtocol.readExtensions(new ByteArrayInputStream(this.encodedServerExtensions));
    }

    public static final class Builder {
        private int cipherSuite = -1;
        private short compressionAlgorithm = (short)-1;
        private byte[] encodedServerExtensions = null;
        private byte[] masterSecret = null;
        private Certificate peerCertificate = null;
        private byte[] pskIdentity = null;
        private byte[] srpIdentity = null;

        private void validate(boolean bl, String string) {
            if (!bl) {
                throw new IllegalStateException("Required session parameter '" + string + "' not configured");
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        public SessionParameters build() {
            boolean bl = true;
            boolean bl2 = this.cipherSuite >= 0 ? bl : false;
            this.validate(bl2, "cipherSuite");
            boolean bl3 = this.compressionAlgorithm >= 0 ? bl : false;
            this.validate(bl3, "compressionAlgorithm");
            if (this.masterSecret == null) {
                bl = false;
            }
            this.validate(bl, "masterSecret");
            return new SessionParameters(this.cipherSuite, this.compressionAlgorithm, this.masterSecret, this.peerCertificate, this.pskIdentity, this.srpIdentity, this.encodedServerExtensions);
        }

        public Builder setCipherSuite(int n2) {
            this.cipherSuite = n2;
            return this;
        }

        public Builder setCompressionAlgorithm(short s2) {
            this.compressionAlgorithm = s2;
            return this;
        }

        public Builder setMasterSecret(byte[] arrby) {
            this.masterSecret = arrby;
            return this;
        }

        public Builder setPSKIdentity(byte[] arrby) {
            this.pskIdentity = arrby;
            return this;
        }

        public Builder setPeerCertificate(Certificate certificate) {
            this.peerCertificate = certificate;
            return this;
        }

        public Builder setPskIdentity(byte[] arrby) {
            this.pskIdentity = arrby;
            return this;
        }

        public Builder setSRPIdentity(byte[] arrby) {
            this.srpIdentity = arrby;
            return this;
        }

        public Builder setServerExtensions(Hashtable hashtable) {
            if (hashtable == null) {
                this.encodedServerExtensions = null;
                return this;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            TlsProtocol.writeExtensions((OutputStream)byteArrayOutputStream, hashtable);
            this.encodedServerExtensions = byteArrayOutputStream.toByteArray();
            return this;
        }
    }

}

