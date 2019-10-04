/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 *  java.math.BigInteger
 *  org.bouncycastle.util.BigIntegers
 */
package org.bouncycastle.crypto.tls;

import java.math.BigInteger;
import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.agreement.DHBasicAgreement;
import org.bouncycastle.crypto.agreement.ECDHBasicAgreement;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.tls.AbstractTlsAgreementCredentials;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.util.BigIntegers;

public class DefaultTlsAgreementCredentials
extends AbstractTlsAgreementCredentials {
    protected BasicAgreement basicAgreement;
    protected Certificate certificate;
    protected AsymmetricKeyParameter privateKey;
    protected boolean truncateAgreement;

    /*
     * Enabled aggressive block sorting
     */
    public DefaultTlsAgreementCredentials(Certificate certificate, AsymmetricKeyParameter asymmetricKeyParameter) {
        if (certificate == null) {
            throw new IllegalArgumentException("'certificate' cannot be null");
        }
        if (certificate.isEmpty()) {
            throw new IllegalArgumentException("'certificate' cannot be empty");
        }
        if (asymmetricKeyParameter == null) {
            throw new IllegalArgumentException("'privateKey' cannot be null");
        }
        if (!asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("'privateKey' must be private");
        }
        if (asymmetricKeyParameter instanceof DHPrivateKeyParameters) {
            this.basicAgreement = new DHBasicAgreement();
            this.truncateAgreement = true;
        } else {
            if (!(asymmetricKeyParameter instanceof ECPrivateKeyParameters)) {
                throw new IllegalArgumentException("'privateKey' type not supported: " + asymmetricKeyParameter.getClass().getName());
            }
            this.basicAgreement = new ECDHBasicAgreement();
            this.truncateAgreement = false;
        }
        this.certificate = certificate;
        this.privateKey = asymmetricKeyParameter;
    }

    @Override
    public byte[] generateAgreement(AsymmetricKeyParameter asymmetricKeyParameter) {
        this.basicAgreement.init(this.privateKey);
        BigInteger bigInteger = this.basicAgreement.calculateAgreement(asymmetricKeyParameter);
        if (this.truncateAgreement) {
            return BigIntegers.asUnsignedByteArray((BigInteger)bigInteger);
        }
        return BigIntegers.asUnsignedByteArray((int)this.basicAgreement.getFieldSize(), (BigInteger)bigInteger);
    }

    @Override
    public Certificate getCertificate() {
        return this.certificate;
    }
}

