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
import org.bouncycastle.asn1.ASN1Encoding;
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
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;

public class DSASigner extends SignatureSpi implements PKCSObjectIdentifiers, X509ObjectIdentifiers {
    private Digest digest;
    private SecureRandom random;
    private DSA signer;

    public static class detDSA224 extends DSASigner {
        public detDSA224() {
            super(new SHA224Digest(), new org.bouncycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(new SHA224Digest())));
        }
    }

    public static class detDSA256 extends DSASigner {
        public detDSA256() {
            super(new SHA256Digest(), new org.bouncycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(new SHA256Digest())));
        }
    }

    public static class detDSA384 extends DSASigner {
        public detDSA384() {
            super(new SHA384Digest(), new org.bouncycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(new SHA384Digest())));
        }
    }

    public static class detDSA512 extends DSASigner {
        public detDSA512() {
            super(new SHA512Digest(), new org.bouncycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(new SHA512Digest())));
        }
    }

    public static class detDSA extends DSASigner {
        public detDSA() {
            super(new SHA1Digest(), new org.bouncycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(new SHA1Digest())));
        }
    }

    public static class dsa224 extends DSASigner {
        public dsa224() {
            super(new SHA224Digest(), new org.bouncycastle.crypto.signers.DSASigner());
        }
    }

    public static class dsa256 extends DSASigner {
        public dsa256() {
            super(new SHA256Digest(), new org.bouncycastle.crypto.signers.DSASigner());
        }
    }

    public static class dsa384 extends DSASigner {
        public dsa384() {
            super(new SHA384Digest(), new org.bouncycastle.crypto.signers.DSASigner());
        }
    }

    public static class dsa512 extends DSASigner {
        public dsa512() {
            super(new SHA512Digest(), new org.bouncycastle.crypto.signers.DSASigner());
        }
    }

    public static class noneDSA extends DSASigner {
        public noneDSA() {
            super(new NullDigest(), new org.bouncycastle.crypto.signers.DSASigner());
        }
    }

    public static class stdDSA extends DSASigner {
        public stdDSA() {
            super(new SHA1Digest(), new org.bouncycastle.crypto.signers.DSASigner());
        }
    }

    protected DSASigner(Digest digest, DSA dsa) {
        this.digest = digest;
        this.signer = dsa;
    }

    private BigInteger[] derDecode(byte[] bArr) {
        ASN1Sequence aSN1Sequence = (ASN1Sequence) ASN1Primitive.fromByteArray(bArr);
        return new BigInteger[]{((ASN1Integer) aSN1Sequence.getObjectAt(0)).getValue(), ((ASN1Integer) aSN1Sequence.getObjectAt(1)).getValue()};
    }

    private byte[] derEncode(BigInteger bigInteger, BigInteger bigInteger2) {
        return new DERSequence(new ASN1Integer[]{new ASN1Integer(bigInteger), new ASN1Integer(bigInteger2)}).getEncoded(ASN1Encoding.DER);
    }

    protected Object engineGetParameter(String str) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    protected void engineInitSign(PrivateKey privateKey) {
        CipherParameters generatePrivateKeyParameter = DSAUtil.generatePrivateKeyParameter(privateKey);
        CipherParameters parametersWithRandom = this.random != null ? new ParametersWithRandom(generatePrivateKeyParameter, this.random) : generatePrivateKeyParameter;
        this.digest.reset();
        this.signer.init(true, parametersWithRandom);
    }

    protected void engineInitSign(PrivateKey privateKey, SecureRandom secureRandom) {
        this.random = secureRandom;
        engineInitSign(privateKey);
    }

    protected void engineInitVerify(PublicKey publicKey) {
        CipherParameters generatePublicKeyParameter;
        if (publicKey instanceof DSAKey) {
            generatePublicKeyParameter = DSAUtil.generatePublicKeyParameter(publicKey);
        } else {
            try {
                PublicKey bCDSAPublicKey = new BCDSAPublicKey(SubjectPublicKeyInfo.getInstance(publicKey.getEncoded()));
                if (bCDSAPublicKey instanceof DSAKey) {
                    generatePublicKeyParameter = DSAUtil.generatePublicKeyParameter(bCDSAPublicKey);
                } else {
                    throw new InvalidKeyException("can't recognise key type in DSA based signer");
                }
            } catch (Exception e) {
                throw new InvalidKeyException("can't recognise key type in DSA based signer");
            }
        }
        this.digest.reset();
        this.signer.init(false, generatePublicKeyParameter);
    }

    protected void engineSetParameter(String str, Object obj) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    protected byte[] engineSign() {
        byte[] bArr = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(bArr, 0);
        try {
            BigInteger[] generateSignature = this.signer.generateSignature(bArr);
            return derEncode(generateSignature[0], generateSignature[1]);
        } catch (Exception e) {
            throw new SignatureException(e.toString());
        }
    }

    protected void engineUpdate(byte b) {
        this.digest.update(b);
    }

    protected void engineUpdate(byte[] bArr, int i, int i2) {
        this.digest.update(bArr, i, i2);
    }

    protected boolean engineVerify(byte[] bArr) {
        byte[] bArr2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(bArr2, 0);
        try {
            BigInteger[] derDecode = derDecode(bArr);
            return this.signer.verifySignature(bArr2, derDecode[0], derDecode[1]);
        } catch (Exception e) {
            throw new SignatureException("error decoding signature bytes.");
        }
    }
}
