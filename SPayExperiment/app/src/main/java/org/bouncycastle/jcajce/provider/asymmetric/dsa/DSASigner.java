/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.UnsupportedOperationException
 *  java.math.BigInteger
 *  java.security.InvalidKeyException
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.SignatureException
 *  java.security.SignatureSpi
 *  java.security.interfaces.DSAKey
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.dsa;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.interfaces.DSAKey;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.NullDigest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.DSAKCalculator;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.jcajce.provider.asymmetric.dsa.BCDSAPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.dsa.DSAUtil;

public class DSASigner
extends SignatureSpi
implements PKCSObjectIdentifiers,
X509ObjectIdentifiers {
    private Digest digest;
    private SecureRandom random;
    private DSA signer;

    protected DSASigner(Digest digest, DSA dSA) {
        this.digest = digest;
        this.signer = dSA;
    }

    private BigInteger[] derDecode(byte[] arrby) {
        ASN1Sequence aSN1Sequence = (ASN1Sequence)ASN1Primitive.fromByteArray(arrby);
        BigInteger[] arrbigInteger = new BigInteger[]{((ASN1Integer)aSN1Sequence.getObjectAt(0)).getValue(), ((ASN1Integer)aSN1Sequence.getObjectAt(1)).getValue()};
        return arrbigInteger;
    }

    private byte[] derEncode(BigInteger bigInteger, BigInteger bigInteger2) {
        ASN1Encodable[] arraSN1Encodable = new ASN1Integer[]{new ASN1Integer(bigInteger), new ASN1Integer(bigInteger2)};
        return new DERSequence(arraSN1Encodable).getEncoded("DER");
    }

    protected Object engineGetParameter(String string) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void engineInitSign(PrivateKey privateKey) {
        void var3_4;
        AsymmetricKeyParameter asymmetricKeyParameter = DSAUtil.generatePrivateKeyParameter(privateKey);
        if (this.random != null) {
            ParametersWithRandom parametersWithRandom = new ParametersWithRandom(asymmetricKeyParameter, this.random);
        } else {
            AsymmetricKeyParameter asymmetricKeyParameter2 = asymmetricKeyParameter;
        }
        this.digest.reset();
        this.signer.init(true, (CipherParameters)var3_4);
    }

    protected void engineInitSign(PrivateKey privateKey, SecureRandom secureRandom) {
        this.random = secureRandom;
        this.engineInitSign(privateKey);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected void engineInitVerify(PublicKey publicKey) {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (publicKey instanceof DSAKey) {
            asymmetricKeyParameter = DSAUtil.generatePublicKeyParameter(publicKey);
        } else {
            BCDSAPublicKey bCDSAPublicKey = new BCDSAPublicKey(SubjectPublicKeyInfo.getInstance(publicKey.getEncoded()));
            if (!(bCDSAPublicKey instanceof DSAKey)) throw new InvalidKeyException("can't recognise key type in DSA based signer");
            asymmetricKeyParameter = DSAUtil.generatePublicKeyParameter((PublicKey)bCDSAPublicKey);
        }
        this.digest.reset();
        this.signer.init(false, asymmetricKeyParameter);
        return;
        catch (Exception exception) {
            throw new InvalidKeyException("can't recognise key type in DSA based signer");
        }
    }

    protected void engineSetParameter(String string, Object object) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    protected byte[] engineSign() {
        byte[] arrby = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby, 0);
        try {
            BigInteger[] arrbigInteger = this.signer.generateSignature(arrby);
            byte[] arrby2 = this.derEncode(arrbigInteger[0], arrbigInteger[1]);
            return arrby2;
        }
        catch (Exception exception) {
            throw new SignatureException(exception.toString());
        }
    }

    protected void engineUpdate(byte by) {
        this.digest.update(by);
    }

    protected void engineUpdate(byte[] arrby, int n2, int n3) {
        this.digest.update(arrby, n2, n3);
    }

    protected boolean engineVerify(byte[] arrby) {
        BigInteger[] arrbigInteger;
        byte[] arrby2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby2, 0);
        try {
            arrbigInteger = this.derDecode(arrby);
        }
        catch (Exception exception) {
            throw new SignatureException("error decoding signature bytes.");
        }
        return this.signer.verifySignature(arrby2, arrbigInteger[0], arrbigInteger[1]);
    }

    public static class detDSA
    extends DSASigner {
        public detDSA() {
            super(new SHA1Digest(), new org.bouncycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(new SHA1Digest())));
        }
    }

    public static class detDSA224
    extends DSASigner {
        public detDSA224() {
            super(new SHA224Digest(), new org.bouncycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(new SHA224Digest())));
        }
    }

    public static class detDSA256
    extends DSASigner {
        public detDSA256() {
            super(new SHA256Digest(), new org.bouncycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(new SHA256Digest())));
        }
    }

    public static class detDSA384
    extends DSASigner {
        public detDSA384() {
            super(new SHA384Digest(), new org.bouncycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(new SHA384Digest())));
        }
    }

    public static class detDSA512
    extends DSASigner {
        public detDSA512() {
            super(new SHA512Digest(), new org.bouncycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(new SHA512Digest())));
        }
    }

    public static class dsa224
    extends DSASigner {
        public dsa224() {
            super(new SHA224Digest(), new org.bouncycastle.crypto.signers.DSASigner());
        }
    }

    public static class dsa256
    extends DSASigner {
        public dsa256() {
            super(new SHA256Digest(), new org.bouncycastle.crypto.signers.DSASigner());
        }
    }

    public static class dsa384
    extends DSASigner {
        public dsa384() {
            super(new SHA384Digest(), new org.bouncycastle.crypto.signers.DSASigner());
        }
    }

    public static class dsa512
    extends DSASigner {
        public dsa512() {
            super(new SHA512Digest(), new org.bouncycastle.crypto.signers.DSASigner());
        }
    }

    public static class noneDSA
    extends DSASigner {
        public noneDSA() {
            super(new NullDigest(), new org.bouncycastle.crypto.signers.DSASigner());
        }
    }

    public static class stdDSA
    extends DSASigner {
        public stdDSA() {
            super(new SHA1Digest(), new org.bouncycastle.crypto.signers.DSASigner());
        }
    }

}

