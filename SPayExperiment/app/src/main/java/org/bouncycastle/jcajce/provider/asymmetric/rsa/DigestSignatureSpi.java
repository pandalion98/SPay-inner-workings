/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArrayIndexOutOfBoundsException
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.UnsupportedOperationException
 *  java.security.AlgorithmParameters
 *  java.security.InvalidKeyException
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SignatureException
 *  java.security.SignatureSpi
 *  java.security.interfaces.RSAPrivateKey
 *  java.security.interfaces.RSAPublicKey
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.rsa;

import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD2Digest;
import org.bouncycastle.crypto.digests.MD4Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.NullDigest;
import org.bouncycastle.crypto.digests.RIPEMD128Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.RIPEMD256Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSABlindedEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import org.bouncycastle.util.Arrays;

public class DigestSignatureSpi
extends SignatureSpi {
    private AlgorithmIdentifier algId;
    private AsymmetricBlockCipher cipher;
    private Digest digest;

    protected DigestSignatureSpi(ASN1ObjectIdentifier aSN1ObjectIdentifier, Digest digest, AsymmetricBlockCipher asymmetricBlockCipher) {
        this.digest = digest;
        this.cipher = asymmetricBlockCipher;
        this.algId = new AlgorithmIdentifier(aSN1ObjectIdentifier, DERNull.INSTANCE);
    }

    protected DigestSignatureSpi(Digest digest, AsymmetricBlockCipher asymmetricBlockCipher) {
        this.digest = digest;
        this.cipher = asymmetricBlockCipher;
        this.algId = null;
    }

    private byte[] derEncode(byte[] arrby) {
        if (this.algId == null) {
            return arrby;
        }
        return new DigestInfo(this.algId, arrby).getEncoded("DER");
    }

    private String getType(Object object) {
        if (object == null) {
            return null;
        }
        return object.getClass().getName();
    }

    protected Object engineGetParameter(String string) {
        return null;
    }

    protected AlgorithmParameters engineGetParameters() {
        return null;
    }

    protected void engineInitSign(PrivateKey privateKey) {
        if (!(privateKey instanceof RSAPrivateKey)) {
            throw new InvalidKeyException("Supplied key (" + this.getType((Object)privateKey) + ") is not a RSAPrivateKey instance");
        }
        RSAKeyParameters rSAKeyParameters = RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)privateKey);
        this.digest.reset();
        this.cipher.init(true, rSAKeyParameters);
    }

    protected void engineInitVerify(PublicKey publicKey) {
        if (!(publicKey instanceof RSAPublicKey)) {
            throw new InvalidKeyException("Supplied key (" + this.getType((Object)publicKey) + ") is not a RSAPublicKey instance");
        }
        RSAKeyParameters rSAKeyParameters = RSAUtil.generatePublicKeyParameter((RSAPublicKey)publicKey);
        this.digest.reset();
        this.cipher.init(false, rSAKeyParameters);
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
            byte[] arrby2 = this.derEncode(arrby);
            byte[] arrby3 = this.cipher.processBlock(arrby2, 0, arrby2.length);
            return arrby3;
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new SignatureException("key too small for signature type");
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

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected boolean engineVerify(byte[] arrby) {
        byte[] arrby2;
        byte[] arrby3;
        byte[] arrby4 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby4, 0);
        try {
            arrby3 = this.cipher.processBlock(arrby, 0, arrby.length);
            arrby2 = this.derEncode(arrby4);
        }
        catch (Exception exception) {
            return false;
        }
        if (arrby3.length == arrby2.length) {
            return Arrays.constantTimeAreEqual(arrby3, arrby2);
        }
        if (arrby3.length == -2 + arrby2.length) {
            int n = -2 + (arrby3.length - arrby4.length);
            int n2 = -2 + (arrby2.length - arrby4.length);
            arrby2[1] = (byte)(-2 + arrby2[1]);
            arrby2[3] = (byte)(-2 + arrby2[3]);
            int n3 = 0;
            for (int i = 0; i < arrby4.length; ++i) {
                n3 |= arrby3[n + i] ^ arrby2[n2 + i];
            }
            for (int i = 0; i < n; ++i) {
                n3 |= arrby3[i] ^ arrby2[i];
            }
            boolean bl = false;
            if (n3 != 0) return bl;
            return true;
        }
        Arrays.constantTimeAreEqual(arrby2, arrby2);
        return false;
    }

    public static class MD2
    extends DigestSignatureSpi {
        public MD2() {
            super(PKCSObjectIdentifiers.md2, new MD2Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }

    public static class MD4
    extends DigestSignatureSpi {
        public MD4() {
            super(PKCSObjectIdentifiers.md4, new MD4Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }

    public static class MD5
    extends DigestSignatureSpi {
        public MD5() {
            super(PKCSObjectIdentifiers.md5, new MD5Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }

    public static class RIPEMD128
    extends DigestSignatureSpi {
        public RIPEMD128() {
            super(TeleTrusTObjectIdentifiers.ripemd128, new RIPEMD128Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }

    public static class RIPEMD160
    extends DigestSignatureSpi {
        public RIPEMD160() {
            super(TeleTrusTObjectIdentifiers.ripemd160, new RIPEMD160Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }

    public static class RIPEMD256
    extends DigestSignatureSpi {
        public RIPEMD256() {
            super(TeleTrusTObjectIdentifiers.ripemd256, new RIPEMD256Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }

    public static class SHA1
    extends DigestSignatureSpi {
        public SHA1() {
            super(OIWObjectIdentifiers.idSHA1, new SHA1Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }

    public static class SHA224
    extends DigestSignatureSpi {
        public SHA224() {
            super(NISTObjectIdentifiers.id_sha224, new SHA224Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }

    public static class SHA256
    extends DigestSignatureSpi {
        public SHA256() {
            super(NISTObjectIdentifiers.id_sha256, new SHA256Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }

    public static class SHA384
    extends DigestSignatureSpi {
        public SHA384() {
            super(NISTObjectIdentifiers.id_sha384, new SHA384Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }

    public static class SHA512
    extends DigestSignatureSpi {
        public SHA512() {
            super(NISTObjectIdentifiers.id_sha512, new SHA512Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }

    public static class noneRSA
    extends DigestSignatureSpi {
        public noneRSA() {
            super(new NullDigest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }

}

