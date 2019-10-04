/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.KeyFactory
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.Signature
 *  java.security.SignatureException
 *  java.security.spec.InvalidKeySpecException
 *  java.security.spec.KeySpec
 *  java.security.spec.X509EncodedKeySpec
 */
package org.bouncycastle.jce.netscape;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

public class NetscapeCertRequest
extends ASN1Object {
    String challenge;
    DERBitString content;
    AlgorithmIdentifier keyAlg;
    PublicKey pubkey;
    AlgorithmIdentifier sigAlg;
    byte[] sigBits;

    public NetscapeCertRequest(String string, AlgorithmIdentifier algorithmIdentifier, PublicKey publicKey) {
        this.challenge = string;
        this.sigAlg = algorithmIdentifier;
        this.pubkey = publicKey;
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.getKeySpec());
        aSN1EncodableVector.add(new DERIA5String(string));
        try {
            this.content = new DERBitString(new DERSequence(aSN1EncodableVector));
            return;
        }
        catch (IOException iOException) {
            throw new InvalidKeySpecException("exception encoding key: " + iOException.toString());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public NetscapeCertRequest(ASN1Sequence aSN1Sequence) {
        ASN1Sequence aSN1Sequence2;
        try {
            if (aSN1Sequence.size() != 3) {
                throw new IllegalArgumentException("invalid SPKAC (size):" + aSN1Sequence.size());
            }
            this.sigAlg = new AlgorithmIdentifier((ASN1Sequence)aSN1Sequence.getObjectAt(1));
            this.sigBits = ((DERBitString)aSN1Sequence.getObjectAt(2)).getBytes();
            aSN1Sequence2 = (ASN1Sequence)aSN1Sequence.getObjectAt(0);
            if (aSN1Sequence2.size() != 2) {
                throw new IllegalArgumentException("invalid PKAC (len): " + aSN1Sequence2.size());
            }
        }
        catch (Exception exception) {
            throw new IllegalArgumentException(exception.toString());
        }
        this.challenge = ((DERIA5String)aSN1Sequence2.getObjectAt(1)).getString();
        this.content = new DERBitString(aSN1Sequence2);
        SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo((ASN1Sequence)aSN1Sequence2.getObjectAt(0));
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(new DERBitString(subjectPublicKeyInfo).getBytes());
        this.keyAlg = subjectPublicKeyInfo.getAlgorithmId();
        this.pubkey = KeyFactory.getInstance((String)this.keyAlg.getObjectId().getId(), (String)"BC").generatePublic((KeySpec)x509EncodedKeySpec);
    }

    public NetscapeCertRequest(byte[] arrby) {
        this(NetscapeCertRequest.getReq(arrby));
    }

    private ASN1Primitive getKeySpec() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(this.pubkey.getEncoded());
            byteArrayOutputStream.close();
            ASN1Primitive aSN1Primitive = new ASN1InputStream((InputStream)new ByteArrayInputStream(byteArrayOutputStream.toByteArray())).readObject();
            return aSN1Primitive;
        }
        catch (IOException iOException) {
            throw new InvalidKeySpecException(iOException.getMessage());
        }
    }

    private static ASN1Sequence getReq(byte[] arrby) {
        return ASN1Sequence.getInstance(new ASN1InputStream((InputStream)new ByteArrayInputStream(arrby)).readObject());
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

    public void setChallenge(String string) {
        this.challenge = string;
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
        this.sign(privateKey, null);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void sign(PrivateKey privateKey, SecureRandom secureRandom) {
        Signature signature = Signature.getInstance((String)this.sigAlg.getAlgorithm().getId(), (String)"BC");
        if (secureRandom != null) {
            signature.initSign(privateKey, secureRandom);
        } else {
            signature.initSign(privateKey);
        }
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.getKeySpec());
        aSN1EncodableVector.add(new DERIA5String(this.challenge));
        try {
            signature.update(new DERSequence(aSN1EncodableVector).getEncoded("DER"));
        }
        catch (IOException iOException) {
            throw new SignatureException(iOException.getMessage());
        }
        this.sigBits = signature.sign();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector;
        ASN1EncodableVector aSN1EncodableVector2;
        aSN1EncodableVector2 = new ASN1EncodableVector();
        aSN1EncodableVector = new ASN1EncodableVector();
        try {
            aSN1EncodableVector.add(this.getKeySpec());
        }
        catch (Exception exception) {}
        aSN1EncodableVector.add(new DERIA5String(this.challenge));
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector));
        aSN1EncodableVector2.add(this.sigAlg);
        aSN1EncodableVector2.add(new DERBitString(this.sigBits));
        return new DERSequence(aSN1EncodableVector2);
    }

    public boolean verify(String string) {
        if (!string.equals((Object)this.challenge)) {
            return false;
        }
        Signature signature = Signature.getInstance((String)this.sigAlg.getObjectId().getId(), (String)"BC");
        signature.initVerify(this.pubkey);
        signature.update(this.content.getBytes());
        return signature.verify(this.sigBits);
    }
}

