/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.signers.DSAKCalculator;
import org.bouncycastle.crypto.signers.DSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.crypto.tls.TlsDSASigner;
import org.bouncycastle.crypto.tls.TlsUtils;

public class TlsDSSSigner
extends TlsDSASigner {
    @Override
    protected DSA createDSAImpl(short s2) {
        return new DSASigner(new HMacDSAKCalculator(TlsUtils.createHash(s2)));
    }

    @Override
    protected short getSignatureAlgorithm() {
        return 2;
    }

    @Override
    public boolean isValidPublicKey(AsymmetricKeyParameter asymmetricKeyParameter) {
        return asymmetricKeyParameter instanceof DSAPublicKeyParameters;
    }
}

