package org.bouncycastle.crypto.agreement.srp;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.SRP6GroupParameters;

public class SRP6Server {
    protected BigInteger f97A;
    protected BigInteger f98B;
    protected BigInteger Key;
    protected BigInteger M1;
    protected BigInteger M2;
    protected BigInteger f99N;
    protected BigInteger f100S;
    protected BigInteger f101b;
    protected Digest digest;
    protected BigInteger f102g;
    protected SecureRandom random;
    protected BigInteger f103u;
    protected BigInteger f104v;

    private BigInteger calculateS() {
        return this.f104v.modPow(this.f103u, this.f99N).multiply(this.f97A).mod(this.f99N).modPow(this.f101b, this.f99N);
    }

    public BigInteger calculateSecret(BigInteger bigInteger) {
        this.f97A = SRP6Util.validatePublicValue(this.f99N, bigInteger);
        this.f103u = SRP6Util.calculateU(this.digest, this.f99N, this.f97A, this.f98B);
        this.f100S = calculateS();
        return this.f100S;
    }

    public BigInteger calculateServerEvidenceMessage() {
        if (this.f97A == null || this.M1 == null || this.f100S == null) {
            throw new CryptoException("Impossible to compute M2: some data are missing from the previous operations (A,M1,S)");
        }
        this.M2 = SRP6Util.calculateM2(this.digest, this.f99N, this.f97A, this.M1, this.f100S);
        return this.M2;
    }

    public BigInteger calculateSessionKey() {
        if (this.f100S == null || this.M1 == null || this.M2 == null) {
            throw new CryptoException("Impossible to compute Key: some data are missing from the previous operations (S,M1,M2)");
        }
        this.Key = SRP6Util.calculateKey(this.digest, this.f99N, this.f100S);
        return this.Key;
    }

    public BigInteger generateServerCredentials() {
        BigInteger calculateK = SRP6Util.calculateK(this.digest, this.f99N, this.f102g);
        this.f101b = selectPrivateValue();
        this.f98B = calculateK.multiply(this.f104v).mod(this.f99N).add(this.f102g.modPow(this.f101b, this.f99N)).mod(this.f99N);
        return this.f98B;
    }

    public void init(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, Digest digest, SecureRandom secureRandom) {
        this.f99N = bigInteger;
        this.f102g = bigInteger2;
        this.f104v = bigInteger3;
        this.random = secureRandom;
        this.digest = digest;
    }

    public void init(SRP6GroupParameters sRP6GroupParameters, BigInteger bigInteger, Digest digest, SecureRandom secureRandom) {
        init(sRP6GroupParameters.getN(), sRP6GroupParameters.getG(), bigInteger, digest, secureRandom);
    }

    protected BigInteger selectPrivateValue() {
        return SRP6Util.generatePrivateValue(this.digest, this.f99N, this.f102g, this.random);
    }

    public boolean verifyClientEvidenceMessage(BigInteger bigInteger) {
        if (this.f97A == null || this.f98B == null || this.f100S == null) {
            throw new CryptoException("Impossible to compute and verify M1: some data are missing from the previous operations (A,B,S)");
        } else if (!SRP6Util.calculateM1(this.digest, this.f99N, this.f97A, this.f98B, this.f100S).equals(bigInteger)) {
            return false;
        } else {
            this.M1 = bigInteger;
            return true;
        }
    }
}
