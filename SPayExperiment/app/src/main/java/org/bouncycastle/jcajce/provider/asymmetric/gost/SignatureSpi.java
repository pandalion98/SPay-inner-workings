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
 */
package org.bouncycastle.jcajce.provider.asymmetric.gost;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.GOST3411Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.GOST3410Signer;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jcajce.provider.asymmetric.util.GOST3410Util;
import org.bouncycastle.jce.interfaces.ECKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.interfaces.GOST3410Key;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SignatureSpi
extends java.security.SignatureSpi
implements PKCSObjectIdentifiers,
X509ObjectIdentifiers {
    private Digest digest = new GOST3411Digest();
    private SecureRandom random;
    private DSA signer = new GOST3410Signer();

    protected Object engineGetParameter(String string) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void engineInitSign(PrivateKey privateKey) {
        AsymmetricKeyParameter asymmetricKeyParameter = privateKey instanceof ECKey ? ECUtil.generatePrivateKeyParameter(privateKey) : GOST3410Util.generatePrivateKeyParameter(privateKey);
        this.digest.reset();
        if (this.random != null) {
            this.signer.init(true, new ParametersWithRandom(asymmetricKeyParameter, this.random));
            return;
        }
        this.signer.init(true, asymmetricKeyParameter);
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
        if (publicKey instanceof ECPublicKey) {
            asymmetricKeyParameter = ECUtil.generatePublicKeyParameter(publicKey);
        } else if (publicKey instanceof GOST3410Key) {
            asymmetricKeyParameter = GOST3410Util.generatePublicKeyParameter(publicKey);
        } else {
            PublicKey publicKey2 = BouncyCastleProvider.getPublicKey(SubjectPublicKeyInfo.getInstance(publicKey.getEncoded()));
            if (!(publicKey2 instanceof ECPublicKey)) throw new InvalidKeyException("can't recognise key type in DSA based signer");
            asymmetricKeyParameter = ECUtil.generatePublicKeyParameter(publicKey2);
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected byte[] engineSign() {
        byte[] arrby = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby, 0);
        try {
            byte[] arrby2 = new byte[64];
            BigInteger[] arrbigInteger = this.signer.generateSignature(arrby);
            byte[] arrby3 = arrbigInteger[0].toByteArray();
            byte[] arrby4 = arrbigInteger[1].toByteArray();
            if (arrby4[0] != 0) {
                System.arraycopy((Object)arrby4, (int)0, (Object)arrby2, (int)(32 - arrby4.length), (int)arrby4.length);
            } else {
                System.arraycopy((Object)arrby4, (int)1, (Object)arrby2, (int)(32 - (-1 + arrby4.length)), (int)(-1 + arrby4.length));
            }
            if (arrby3[0] != 0) {
                System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)(64 - arrby3.length), (int)arrby3.length);
                return arrby2;
            }
            System.arraycopy((Object)arrby3, (int)1, (Object)arrby2, (int)(64 - (-1 + arrby3.length)), (int)(-1 + arrby3.length));
            return arrby2;
        }
        catch (Exception exception) {
            throw new SignatureException(exception.toString());
        }
    }

    protected void engineUpdate(byte by) {
        this.digest.update(by);
    }

    protected void engineUpdate(byte[] arrby, int n, int n2) {
        this.digest.update(arrby, n, n2);
    }

    protected boolean engineVerify(byte[] arrby) {
        BigInteger[] arrbigInteger;
        byte[] arrby2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby2, 0);
        try {
            byte[] arrby3 = new byte[32];
            byte[] arrby4 = new byte[32];
            System.arraycopy((Object)arrby, (int)0, (Object)arrby4, (int)0, (int)32);
            System.arraycopy((Object)arrby, (int)32, (Object)arrby3, (int)0, (int)32);
            arrbigInteger = new BigInteger[]{new BigInteger(1, arrby3), new BigInteger(1, arrby4)};
        }
        catch (Exception exception) {
            throw new SignatureException("error decoding signature bytes.");
        }
        return this.signer.verifySignature(arrby2, arrbigInteger[0], arrbigInteger[1]);
    }
}

