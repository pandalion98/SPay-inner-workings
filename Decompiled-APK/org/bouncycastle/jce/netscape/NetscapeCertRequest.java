package org.bouncycastle.jce.netscape;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class NetscapeCertRequest extends ASN1Object {
    String challenge;
    DERBitString content;
    AlgorithmIdentifier keyAlg;
    PublicKey pubkey;
    AlgorithmIdentifier sigAlg;
    byte[] sigBits;

    public NetscapeCertRequest(String str, AlgorithmIdentifier algorithmIdentifier, PublicKey publicKey) {
        this.challenge = str;
        this.sigAlg = algorithmIdentifier;
        this.pubkey = publicKey;
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(getKeySpec());
        aSN1EncodableVector.add(new DERIA5String(str));
        try {
            this.content = new DERBitString(new DERSequence(aSN1EncodableVector));
        } catch (IOException e) {
            throw new InvalidKeySpecException("exception encoding key: " + e.toString());
        }
    }

    public NetscapeCertRequest(ASN1Sequence aSN1Sequence) {
        try {
            if (aSN1Sequence.size() != 3) {
                throw new IllegalArgumentException("invalid SPKAC (size):" + aSN1Sequence.size());
            }
            this.sigAlg = new AlgorithmIdentifier((ASN1Sequence) aSN1Sequence.getObjectAt(1));
            this.sigBits = ((DERBitString) aSN1Sequence.getObjectAt(2)).getBytes();
            ASN1Encodable aSN1Encodable = (ASN1Sequence) aSN1Sequence.getObjectAt(0);
            if (aSN1Encodable.size() != 2) {
                throw new IllegalArgumentException("invalid PKAC (len): " + aSN1Encodable.size());
            }
            this.challenge = ((DERIA5String) aSN1Encodable.getObjectAt(1)).getString();
            this.content = new DERBitString(aSN1Encodable);
            ASN1Encodable subjectPublicKeyInfo = new SubjectPublicKeyInfo((ASN1Sequence) aSN1Encodable.getObjectAt(0));
            KeySpec x509EncodedKeySpec = new X509EncodedKeySpec(new DERBitString(subjectPublicKeyInfo).getBytes());
            this.keyAlg = subjectPublicKeyInfo.getAlgorithmId();
            this.pubkey = KeyFactory.getInstance(this.keyAlg.getObjectId().getId(), BouncyCastleProvider.PROVIDER_NAME).generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    public NetscapeCertRequest(byte[] bArr) {
        this(getReq(bArr));
    }

    private ASN1Primitive getKeySpec() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(this.pubkey.getEncoded());
            byteArrayOutputStream.close();
            return new ASN1InputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())).readObject();
        } catch (IOException e) {
            throw new InvalidKeySpecException(e.getMessage());
        }
    }

    private static ASN1Sequence getReq(byte[] bArr) {
        return ASN1Sequence.getInstance(new ASN1InputStream(new ByteArrayInputStream(bArr)).readObject());
    }

    public String getChallenge() {
        return this.challenge;
    }

    public AlgorithmIdentifier getKeyAlgorithm() {
        return this.keyAlg;
    }

    public PublicKey getPublicKey() {
        return this.pubkey;
    }

    public AlgorithmIdentifier getSigningAlgorithm() {
        return this.sigAlg;
    }

    public void setChallenge(String str) {
        this.challenge = str;
    }

    public void setKeyAlgorithm(AlgorithmIdentifier algorithmIdentifier) {
        this.keyAlg = algorithmIdentifier;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.pubkey = publicKey;
    }

    public void setSigningAlgorithm(AlgorithmIdentifier algorithmIdentifier) {
        this.sigAlg = algorithmIdentifier;
    }

    public void sign(PrivateKey privateKey) {
        sign(privateKey, null);
    }

    public void sign(PrivateKey privateKey, SecureRandom secureRandom) {
        Signature instance = Signature.getInstance(this.sigAlg.getAlgorithm().getId(), BouncyCastleProvider.PROVIDER_NAME);
        if (secureRandom != null) {
            instance.initSign(privateKey, secureRandom);
        } else {
            instance.initSign(privateKey);
        }
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(getKeySpec());
        aSN1EncodableVector.add(new DERIA5String(this.challenge));
        try {
            instance.update(new DERSequence(aSN1EncodableVector).getEncoded(ASN1Encoding.DER));
            this.sigBits = instance.sign();
        } catch (IOException e) {
            throw new SignatureException(e.getMessage());
        }
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        try {
            aSN1EncodableVector2.add(getKeySpec());
        } catch (Exception e) {
        }
        aSN1EncodableVector2.add(new DERIA5String(this.challenge));
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        aSN1EncodableVector.add(this.sigAlg);
        aSN1EncodableVector.add(new DERBitString(this.sigBits));
        return new DERSequence(aSN1EncodableVector);
    }

    public boolean verify(String str) {
        if (!str.equals(this.challenge)) {
            return false;
        }
        Signature instance = Signature.getInstance(this.sigAlg.getObjectId().getId(), BouncyCastleProvider.PROVIDER_NAME);
        instance.initVerify(this.pubkey);
        instance.update(this.content.getBytes());
        return instance.verify(this.sigBits);
    }
}
