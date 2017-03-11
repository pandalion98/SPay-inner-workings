package org.bouncycastle.crypto.tls;

import org.bouncycastle.util.Arrays;

public class SecurityParameters {
    int cipherSuite;
    byte[] clientRandom;
    short compressionAlgorithm;
    boolean encryptThenMAC;
    int entity;
    boolean extendedMasterSecret;
    byte[] masterSecret;
    short maxFragmentLength;
    int prfAlgorithm;
    byte[] pskIdentity;
    byte[] serverRandom;
    byte[] sessionHash;
    byte[] srpIdentity;
    boolean truncatedHMac;
    int verifyDataLength;

    public SecurityParameters() {
        this.entity = -1;
        this.cipherSuite = -1;
        this.compressionAlgorithm = (short) 0;
        this.prfAlgorithm = -1;
        this.verifyDataLength = -1;
        this.masterSecret = null;
        this.clientRandom = null;
        this.serverRandom = null;
        this.sessionHash = null;
        this.pskIdentity = null;
        this.srpIdentity = null;
        this.maxFragmentLength = (short) -1;
        this.truncatedHMac = false;
        this.encryptThenMAC = false;
        this.extendedMasterSecret = false;
    }

    void clear() {
        if (this.masterSecret != null) {
            Arrays.fill(this.masterSecret, (byte) 0);
            this.masterSecret = null;
        }
    }

    public int getCipherSuite() {
        return this.cipherSuite;
    }

    public byte[] getClientRandom() {
        return this.clientRandom;
    }

    public short getCompressionAlgorithm() {
        return this.compressionAlgorithm;
    }

    public int getEntity() {
        return this.entity;
    }

    public byte[] getMasterSecret() {
        return this.masterSecret;
    }

    public byte[] getPSKIdentity() {
        return this.pskIdentity;
    }

    public int getPrfAlgorithm() {
        return this.prfAlgorithm;
    }

    public byte[] getPskIdentity() {
        return this.pskIdentity;
    }

    public byte[] getSRPIdentity() {
        return this.srpIdentity;
    }

    public byte[] getServerRandom() {
        return this.serverRandom;
    }

    public byte[] getSessionHash() {
        return this.sessionHash;
    }

    public int getVerifyDataLength() {
        return this.verifyDataLength;
    }
}
