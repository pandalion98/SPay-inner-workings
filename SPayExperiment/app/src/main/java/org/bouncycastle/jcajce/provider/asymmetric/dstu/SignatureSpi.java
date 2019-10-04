/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.UnsupportedOperationException
 *  java.math.BigInteger
 *  java.security.InvalidKeyException
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.SignatureException
 *  java.security.SignatureSpi
 *  java.security.spec.AlgorithmParameterSpec
 *  org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil
 *  org.bouncycastle.jce.provider.BouncyCastleProvider
 */
package org.bouncycastle.jcajce.provider.asymmetric.dstu;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.GOST3411Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.DSTU4145Signer;
import org.bouncycastle.jcajce.provider.asymmetric.dstu.BCDSTU4145PublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.interfaces.ECKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SignatureSpi
extends java.security.SignatureSpi
implements PKCSObjectIdentifiers,
X509ObjectIdentifiers {
    private static byte[] DEFAULT_SBOX = new byte[]{10, 9, 13, 6, 14, 11, 4, 5, 15, 1, 3, 12, 7, 0, 8, 2, 8, 0, 12, 4, 9, 6, 7, 11, 2, 3, 1, 15, 5, 14, 10, 13, 15, 6, 5, 8, 14, 11, 10, 4, 12, 0, 3, 7, 2, 9, 1, 13, 3, 8, 13, 9, 6, 11, 15, 0, 2, 5, 12, 10, 4, 14, 1, 7, 15, 8, 14, 9, 7, 2, 0, 13, 12, 6, 1, 5, 11, 4, 3, 10, 2, 8, 9, 7, 5, 15, 0, 11, 12, 1, 13, 14, 10, 3, 6, 4, 3, 8, 11, 5, 6, 4, 14, 10, 2, 12, 1, 7, 9, 15, 13, 0, 1, 2, 3, 14, 6, 13, 11, 8, 15, 10, 12, 5, 7, 9, 0, 4};
    private Digest digest;
    private DSA signer = new DSTU4145Signer();

    protected Object engineGetParameter(String string) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    protected void engineInitSign(PrivateKey privateKey) {
        boolean bl = privateKey instanceof ECKey;
        AsymmetricKeyParameter asymmetricKeyParameter = null;
        if (bl) {
            asymmetricKeyParameter = ECUtil.generatePrivateKeyParameter((PrivateKey)privateKey);
        }
        this.digest = new GOST3411Digest(DEFAULT_SBOX);
        if (this.appRandom != null) {
            this.signer.init(true, new ParametersWithRandom(asymmetricKeyParameter, this.appRandom));
            return;
        }
        this.signer.init(true, asymmetricKeyParameter);
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
        PublicKey publicKey2;
        if (publicKey instanceof ECPublicKey) {
            asymmetricKeyParameter = ECUtil.generatePublicKeyParameter((PublicKey)publicKey);
            publicKey2 = publicKey;
        } else {
            PublicKey publicKey3 = BouncyCastleProvider.getPublicKey((SubjectPublicKeyInfo)SubjectPublicKeyInfo.getInstance(publicKey.getEncoded()));
            if (!(publicKey3 instanceof ECPublicKey)) throw new InvalidKeyException("can't recognise key type in DSA based signer");
            asymmetricKeyParameter = ECUtil.generatePublicKeyParameter((PublicKey)publicKey3);
            publicKey2 = publicKey3;
        }
        this.digest = new GOST3411Digest(this.expandSbox(((BCDSTU4145PublicKey)publicKey2).getSbox()));
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected byte[] engineSign() {
        byte[] arrby = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby, 0);
        try {
            int n2;
            BigInteger[] arrbigInteger = this.signer.generateSignature(arrby);
            byte[] arrby2 = arrbigInteger[0].toByteArray();
            byte[] arrby3 = arrbigInteger[1].toByteArray();
            if (arrby2.length > arrby3.length) {
                n2 = 2 * arrby2.length;
            } else {
                int n3 = arrby3.length;
                n2 = n3 * 2;
            }
            byte[] arrby4 = new byte[n2];
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby4, (int)(arrby4.length / 2 - arrby3.length), (int)arrby3.length);
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby4, (int)(arrby4.length - arrby2.length), (int)arrby2.length);
            return new DEROctetString(arrby4).getEncoded();
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
            byte[] arrby3 = ((ASN1OctetString)ASN1OctetString.fromByteArray(arrby)).getOctets();
            byte[] arrby4 = new byte[arrby3.length / 2];
            byte[] arrby5 = new byte[arrby3.length / 2];
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby5, (int)0, (int)(arrby3.length / 2));
            System.arraycopy((Object)arrby3, (int)(arrby3.length / 2), (Object)arrby4, (int)0, (int)(arrby3.length / 2));
            arrbigInteger = new BigInteger[]{new BigInteger(1, arrby4), new BigInteger(1, arrby5)};
        }
        catch (Exception exception) {
            throw new SignatureException("error decoding signature bytes.");
        }
        return this.signer.verifySignature(arrby2, arrbigInteger[0], arrbigInteger[1]);
    }

    byte[] expandSbox(byte[] arrby) {
        byte[] arrby2 = new byte[128];
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            arrby2[i2 * 2] = (byte)(15 & arrby[i2] >> 4);
            arrby2[1 + i2 * 2] = (byte)(15 & arrby[i2]);
        }
        return arrby2;
    }
}

