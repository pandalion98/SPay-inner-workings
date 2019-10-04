/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.DSAKCalculator;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.crypto.tls.TlsDSASigner;
import org.bouncycastle.crypto.tls.TlsUtils;

public class TlsECDSASigner
extends TlsDSASigner {
    @Override
    protected DSA createDSAImpl(short s2) {
        return new ECDSASigner(new HMacDSAKCalculator(TlsUtils.createHash(s2)));
    }

    @Override
    protected short getSignatureAlgorithm() {
        return 3;
    }

    @Override
    public boolean isValidPublicKey(AsymmetricKeyParameter asymmetricKeyParameter) {
        return asymmetricKeyParameter instanceof ECPublicKeyParameters;
    }
}

