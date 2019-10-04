/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.Hashtable
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.signers;

import java.io.IOException;
import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSABlindedEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;

public class RSADigestSigner
implements Signer {
    private static final Hashtable oidMap = new Hashtable();
    private final AlgorithmIdentifier algId;
    private final Digest digest;
    private boolean forSigning;
    private final AsymmetricBlockCipher rsaEngine = new PKCS1Encoding(new RSABlindedEngine());

    static {
        oidMap.put((Object)"RIPEMD128", (Object)TeleTrusTObjectIdentifiers.ripemd128);
        oidMap.put((Object)"RIPEMD160", (Object)TeleTrusTObjectIdentifiers.ripemd160);
        oidMap.put((Object)"RIPEMD256", (Object)TeleTrusTObjectIdentifiers.ripemd256);
        oidMap.put((Object)"SHA-1", (Object)X509ObjectIdentifiers.id_SHA1);
        oidMap.put((Object)"SHA-224", (Object)NISTObjectIdentifiers.id_sha224);
        oidMap.put((Object)"SHA-256", (Object)NISTObjectIdentifiers.id_sha256);
        oidMap.put((Object)"SHA-384", (Object)NISTObjectIdentifiers.id_sha384);
        oidMap.put((Object)"SHA-512", (Object)NISTObjectIdentifiers.id_sha512);
        oidMap.put((Object)"SHA-512/224", (Object)NISTObjectIdentifiers.id_sha512_224);
        oidMap.put((Object)"SHA-512/256", (Object)NISTObjectIdentifiers.id_sha512_256);
        oidMap.put((Object)"MD2", (Object)PKCSObjectIdentifiers.md2);
        oidMap.put((Object)"MD4", (Object)PKCSObjectIdentifiers.md4);
        oidMap.put((Object)"MD5", (Object)PKCSObjectIdentifiers.md5);
    }

    public RSADigestSigner(Digest digest) {
        this(digest, (ASN1ObjectIdentifier)oidMap.get((Object)digest.getAlgorithmName()));
    }

    public RSADigestSigner(Digest digest, ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this.digest = digest;
        this.algId = new AlgorithmIdentifier(aSN1ObjectIdentifier, DERNull.INSTANCE);
    }

    private byte[] derEncode(byte[] arrby) {
        return new DigestInfo(this.algId, arrby).getEncoded("DER");
    }

    @Override
    public byte[] generateSignature() {
        if (!this.forSigning) {
            throw new IllegalStateException("RSADigestSigner not initialised for signature generation.");
        }
        byte[] arrby = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby, 0);
        try {
            byte[] arrby2 = this.derEncode(arrby);
            byte[] arrby3 = this.rsaEngine.processBlock(arrby2, 0, arrby2.length);
            return arrby3;
        }
        catch (IOException iOException) {
            throw new CryptoException("unable to encode signature: " + iOException.getMessage(), iOException);
        }
    }

    public String getAlgorithmName() {
        return this.digest.getAlgorithmName() + "withRSA";
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forSigning = bl;
        AsymmetricKeyParameter asymmetricKeyParameter = cipherParameters instanceof ParametersWithRandom ? (AsymmetricKeyParameter)((ParametersWithRandom)cipherParameters).getParameters() : (AsymmetricKeyParameter)cipherParameters;
        if (bl && !asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("signing requires private key");
        }
        if (!bl && asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("verification requires public key");
        }
        this.reset();
        this.rsaEngine.init(bl, cipherParameters);
    }

    @Override
    public void reset() {
        this.digest.reset();
    }

    @Override
    public void update(byte by) {
        this.digest.update(by);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        this.digest.update(arrby, n2, n3);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean verifySignature(byte[] arrby) {
        byte[] arrby2;
        byte[] arrby3;
        if (this.forSigning) {
            throw new IllegalStateException("RSADigestSigner not initialised for verification");
        }
        byte[] arrby4 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby4, 0);
        try {
            arrby3 = this.rsaEngine.processBlock(arrby, 0, arrby.length);
            arrby2 = this.derEncode(arrby4);
        }
        catch (Exception exception) {
            return false;
        }
        if (arrby3.length == arrby2.length) {
            return Arrays.constantTimeAreEqual((byte[])arrby3, (byte[])arrby2);
        }
        if (arrby3.length == -2 + arrby2.length) {
            int n2 = -2 + (arrby3.length - arrby4.length);
            int n3 = -2 + (arrby2.length - arrby4.length);
            arrby2[1] = (byte)(-2 + arrby2[1]);
            arrby2[3] = (byte)(-2 + arrby2[3]);
            int n4 = 0;
            for (int i2 = 0; i2 < arrby4.length; ++i2) {
                n4 |= arrby3[n2 + i2] ^ arrby2[n3 + i2];
            }
            for (int i3 = 0; i3 < n2; ++i3) {
                n4 |= arrby3[i3] ^ arrby2[i3];
            }
            boolean bl = false;
            if (n4 != 0) return bl;
            return true;
        }
        Arrays.constantTimeAreEqual((byte[])arrby2, (byte[])arrby2);
        return false;
    }
}

