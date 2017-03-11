package org.bouncycastle.jcajce.provider.asymmetric.dstu;

import com.mastercard.mobile_api.utils.apdu.emv.PutTemplateApdu;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.GOST3411Digest;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.DSTU4145Signer;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.jce.interfaces.ECKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SignatureSpi extends java.security.SignatureSpi implements PKCSObjectIdentifiers, X509ObjectIdentifiers {
    private static byte[] DEFAULT_SBOX;
    private Digest digest;
    private DSA signer;

    static {
        DEFAULT_SBOX = new byte[]{(byte) 10, (byte) 9, (byte) 13, (byte) 6, (byte) 14, (byte) 11, (byte) 4, (byte) 5, (byte) 15, (byte) 1, (byte) 3, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 7, (byte) 0, (byte) 8, (byte) 2, (byte) 8, (byte) 0, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 4, (byte) 9, (byte) 6, (byte) 7, (byte) 11, (byte) 2, (byte) 3, (byte) 1, (byte) 15, (byte) 5, (byte) 14, (byte) 10, (byte) 13, (byte) 15, (byte) 6, (byte) 5, (byte) 8, (byte) 14, (byte) 11, (byte) 10, (byte) 4, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 0, (byte) 3, (byte) 7, (byte) 2, (byte) 9, (byte) 1, (byte) 13, (byte) 3, (byte) 8, (byte) 13, (byte) 9, (byte) 6, (byte) 11, (byte) 15, (byte) 0, (byte) 2, (byte) 5, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 10, (byte) 4, (byte) 14, (byte) 1, (byte) 7, (byte) 15, (byte) 8, (byte) 14, (byte) 9, (byte) 7, (byte) 2, (byte) 0, (byte) 13, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 6, (byte) 1, (byte) 5, (byte) 11, (byte) 4, (byte) 3, (byte) 10, (byte) 2, (byte) 8, (byte) 9, (byte) 7, (byte) 5, (byte) 15, (byte) 0, (byte) 11, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 1, (byte) 13, (byte) 14, (byte) 10, (byte) 3, (byte) 6, (byte) 4, (byte) 3, (byte) 8, (byte) 11, (byte) 5, (byte) 6, (byte) 4, (byte) 14, (byte) 10, (byte) 2, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 1, (byte) 7, (byte) 9, (byte) 15, (byte) 13, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 14, (byte) 6, (byte) 13, (byte) 11, (byte) 8, (byte) 15, (byte) 10, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 5, (byte) 7, (byte) 9, (byte) 0, (byte) 4};
    }

    public SignatureSpi() {
        this.signer = new DSTU4145Signer();
    }

    protected Object engineGetParameter(String str) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    protected void engineInitSign(PrivateKey privateKey) {
        CipherParameters cipherParameters = null;
        if (privateKey instanceof ECKey) {
            cipherParameters = ECUtil.generatePrivateKeyParameter(privateKey);
        }
        this.digest = new GOST3411Digest(DEFAULT_SBOX);
        if (this.appRandom != null) {
            this.signer.init(true, new ParametersWithRandom(cipherParameters, this.appRandom));
        } else {
            this.signer.init(true, cipherParameters);
        }
    }

    protected void engineInitVerify(PublicKey publicKey) {
        CipherParameters generatePublicKeyParameter;
        PublicKey publicKey2;
        if (publicKey instanceof ECPublicKey) {
            generatePublicKeyParameter = ECUtil.generatePublicKeyParameter(publicKey);
            publicKey2 = publicKey;
        } else {
            try {
                publicKey = BouncyCastleProvider.getPublicKey(SubjectPublicKeyInfo.getInstance(publicKey.getEncoded()));
                if (publicKey instanceof ECPublicKey) {
                    Object generatePublicKeyParameter2 = ECUtil.generatePublicKeyParameter(publicKey);
                    publicKey2 = publicKey;
                } else {
                    throw new InvalidKeyException("can't recognise key type in DSA based signer");
                }
            } catch (Exception e) {
                throw new InvalidKeyException("can't recognise key type in DSA based signer");
            }
        }
        this.digest = new GOST3411Digest(expandSbox(((BCDSTU4145PublicKey) publicKey2).getSbox()));
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
            Object toByteArray = generateSignature[0].toByteArray();
            Object toByteArray2 = generateSignature[1].toByteArray();
            bArr = new byte[(toByteArray.length > toByteArray2.length ? toByteArray.length * 2 : toByteArray2.length * 2)];
            System.arraycopy(toByteArray2, 0, bArr, (bArr.length / 2) - toByteArray2.length, toByteArray2.length);
            System.arraycopy(toByteArray, 0, bArr, bArr.length - toByteArray.length, toByteArray.length);
            return new DEROctetString(bArr).getEncoded();
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
            Object octets = ((ASN1OctetString) ASN1Primitive.fromByteArray(bArr)).getOctets();
            Object obj = new byte[(octets.length / 2)];
            System.arraycopy(octets, 0, new byte[(octets.length / 2)], 0, octets.length / 2);
            System.arraycopy(octets, octets.length / 2, obj, 0, octets.length / 2);
            BigInteger[] bigIntegerArr = new BigInteger[]{new BigInteger(1, obj), new BigInteger(1, r3)};
            return this.signer.verifySignature(bArr2, bigIntegerArr[0], bigIntegerArr[1]);
        } catch (Exception e) {
            throw new SignatureException("error decoding signature bytes.");
        }
    }

    byte[] expandSbox(byte[] bArr) {
        byte[] bArr2 = new byte[X509KeyUsage.digitalSignature];
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i * 2] = (byte) ((bArr[i] >> 4) & 15);
            bArr2[(i * 2) + 1] = (byte) (bArr[i] & 15);
        }
        return bArr2;
    }
}
