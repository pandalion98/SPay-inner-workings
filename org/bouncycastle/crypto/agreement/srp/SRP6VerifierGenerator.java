package org.bouncycastle.crypto.agreement.srp;

import java.math.BigInteger;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.SRP6GroupParameters;

public class SRP6VerifierGenerator {
    protected BigInteger f105N;
    protected Digest digest;
    protected BigInteger f106g;

    public BigInteger generateVerifier(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return this.f106g.modPow(SRP6Util.calculateX(this.digest, this.f105N, bArr, bArr2, bArr3), this.f105N);
    }

    public void init(BigInteger bigInteger, BigInteger bigInteger2, Digest digest) {
        this.f105N = bigInteger;
        this.f106g = bigInteger2;
        this.digest = digest;
    }

    public void init(SRP6GroupParameters sRP6GroupParameters, Digest digest) {
        this.f105N = sRP6GroupParameters.getN();
        this.f106g = sRP6GroupParameters.getG();
        this.digest = digest;
    }
}
