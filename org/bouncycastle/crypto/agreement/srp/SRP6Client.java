package org.bouncycastle.crypto.agreement.srp;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.SRP6GroupParameters;

public class SRP6Client {
    protected BigInteger f89A;
    protected BigInteger f90B;
    protected BigInteger Key;
    protected BigInteger M1;
    protected BigInteger M2;
    protected BigInteger f91N;
    protected BigInteger f92S;
    protected BigInteger f93a;
    protected Digest digest;
    protected BigInteger f94g;
    protected SecureRandom random;
    protected BigInteger f95u;
    protected BigInteger f96x;

    private BigInteger calculateS() {
        BigInteger calculateK = SRP6Util.calculateK(this.digest, this.f91N, this.f94g);
        return this.f90B.subtract(this.f94g.modPow(this.f96x, this.f91N).multiply(calculateK).mod(this.f91N)).mod(this.f91N).modPow(this.f95u.multiply(this.f96x).add(this.f93a), this.f91N);
    }

    public BigInteger calculateClientEvidenceMessage() {
        if (this.f89A == null || this.f90B == null || this.f92S == null) {
            throw new CryptoException("Impossible to compute M1: some data are missing from the previous operations (A,B,S)");
        }
        this.M1 = SRP6Util.calculateM1(this.digest, this.f91N, this.f89A, this.f90B, this.f92S);
        return this.M1;
    }

    public BigInteger calculateSecret(BigInteger bigInteger) {
        this.f90B = SRP6Util.validatePublicValue(this.f91N, bigInteger);
        this.f95u = SRP6Util.calculateU(this.digest, this.f91N, this.f89A, this.f90B);
        this.f92S = calculateS();
        return this.f92S;
    }

    public BigInteger calculateSessionKey() {
        if (this.f92S == null || this.M1 == null || this.M2 == null) {
            throw new CryptoException("Impossible to compute Key: some data are missing from the previous operations (S,M1,M2)");
        }
        this.Key = SRP6Util.calculateKey(this.digest, this.f91N, this.f92S);
        return this.Key;
    }

    public BigInteger generateClientCredentials(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        this.f96x = SRP6Util.calculateX(this.digest, this.f91N, bArr, bArr2, bArr3);
        this.f93a = selectPrivateValue();
        this.f89A = this.f94g.modPow(this.f93a, this.f91N);
        return this.f89A;
    }

    public void init(BigInteger bigInteger, BigInteger bigInteger2, Digest digest, SecureRandom secureRandom) {
        this.f91N = bigInteger;
        this.f94g = bigInteger2;
        this.digest = digest;
        this.random = secureRandom;
    }

    public void init(SRP6GroupParameters sRP6GroupParameters, Digest digest, SecureRandom secureRandom) {
        init(sRP6GroupParameters.getN(), sRP6GroupParameters.getG(), digest, secureRandom);
    }

    protected BigInteger selectPrivateValue() {
        return SRP6Util.generatePrivateValue(this.digest, this.f91N, this.f94g, this.random);
    }

    public boolean verifyServerEvidenceMessage(BigInteger bigInteger) {
        if (this.f89A == null || this.M1 == null || this.f92S == null) {
            throw new CryptoException("Impossible to compute and verify M2: some data are missing from the previous operations (A,M1,S)");
        } else if (!SRP6Util.calculateM2(this.digest, this.f91N, this.f89A, this.M1, this.f92S).equals(bigInteger)) {
            return false;
        } else {
            this.M2 = bigInteger;
            return true;
        }
    }
}
