/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.InvalidKeyException
 *  java.security.Key
 *  java.security.KeyFactorySpi
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.spec.InvalidKeySpecException
 *  java.security.spec.KeySpec
 *  java.security.spec.PKCS8EncodedKeySpec
 *  java.security.spec.X509EncodedKeySpec
 */
package org.bouncycastle.pqc.jcajce.provider.mceliece;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.pqc.asn1.McElieceCCA2PrivateKey;
import org.bouncycastle.pqc.asn1.McElieceCCA2PublicKey;
import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcElieceCCA2PrivateKey;
import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcElieceCCA2PublicKey;
import org.bouncycastle.pqc.jcajce.spec.McElieceCCA2PrivateKeySpec;
import org.bouncycastle.pqc.jcajce.spec.McElieceCCA2PublicKeySpec;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;

public class McElieceCCA2KeyFactorySpi
extends KeyFactorySpi {
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.2";

    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) {
        return null;
    }

    protected PublicKey engineGeneratePublic(KeySpec keySpec) {
        return null;
    }

    protected KeySpec engineGetKeySpec(Key key, Class class_) {
        return null;
    }

    protected Key engineTranslateKey(Key key) {
        return null;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public PrivateKey generatePrivate(KeySpec keySpec) {
        i = 0;
        if (keySpec instanceof McElieceCCA2PrivateKeySpec) {
            return new BCMcElieceCCA2PrivateKey((McElieceCCA2PrivateKeySpec)keySpec);
        }
        if (keySpec instanceof PKCS8EncodedKeySpec == false) throw new InvalidKeySpecException("Unsupported key specification: " + (Object)keySpec.getClass() + ".");
        arrby = ((PKCS8EncodedKeySpec)keySpec).getEncoded();
        privateKeyInfo = PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(arrby));
        {
            catch (IOException iOException) {
                throw new InvalidKeySpecException("Unable to decode PKCS8EncodedKeySpec: " + (Object)iOException);
            }
        }
        ** try [egrp 1[TRYBLOCK] [1 : 45->211)] { 
        {
lbl12: // 1 sources:
            aSN1Sequence = (ASN1Sequence)privateKeyInfo.parsePrivateKey().toASN1Primitive();
            ((ASN1ObjectIdentifier)aSN1Sequence.getObjectAt(0)).toString();
            n = ((ASN1Integer)aSN1Sequence.getObjectAt(1)).getValue().intValue();
            n2 = ((ASN1Integer)aSN1Sequence.getObjectAt(2)).getValue().intValue();
            arrby2 = ((ASN1OctetString)aSN1Sequence.getObjectAt(3)).getOctets();
            arrby3 = ((ASN1OctetString)aSN1Sequence.getObjectAt(4)).getOctets();
            arrby4 = ((ASN1OctetString)aSN1Sequence.getObjectAt(5)).getOctets();
            arrby5 = ((ASN1OctetString)aSN1Sequence.getObjectAt(6)).getOctets();
            aSN1Sequence2 = (ASN1Sequence)aSN1Sequence.getObjectAt(7);
            arrarrby = new byte[aSN1Sequence2.size()][];
            while (i < aSN1Sequence2.size()) {
                arrarrby[i] = ((ASN1OctetString)aSN1Sequence2.getObjectAt(i)).getOctets();
                ++i;
            }
            return new BCMcElieceCCA2PrivateKey(new McElieceCCA2PrivateKeySpec("1.3.6.1.4.1.8301.3.1.3.4.2", n, n2, arrby2, arrby3, arrby4, arrby5, arrarrby));
        }
lbl27: // 1 sources:
        catch (IOException iOException) {
            throw new InvalidKeySpecException("Unable to decode PKCS8EncodedKeySpec.");
        }
    }

    public PrivateKey generatePrivate(PrivateKeyInfo privateKeyInfo) {
        try {
            McElieceCCA2PrivateKey mcElieceCCA2PrivateKey = McElieceCCA2PrivateKey.getInstance(privateKeyInfo.parsePrivateKey().toASN1Primitive());
            BCMcElieceCCA2PrivateKey bCMcElieceCCA2PrivateKey = new BCMcElieceCCA2PrivateKey(mcElieceCCA2PrivateKey.getOID().getId(), mcElieceCCA2PrivateKey.getN(), mcElieceCCA2PrivateKey.getK(), mcElieceCCA2PrivateKey.getField(), mcElieceCCA2PrivateKey.getGoppaPoly(), mcElieceCCA2PrivateKey.getP(), mcElieceCCA2PrivateKey.getH(), mcElieceCCA2PrivateKey.getQInv());
            return bCMcElieceCCA2PrivateKey;
        }
        catch (IOException iOException) {
            throw new InvalidKeySpecException("Unable to decode PKCS8EncodedKeySpec");
        }
    }

    public PublicKey generatePublic(KeySpec keySpec) {
        if (keySpec instanceof McElieceCCA2PublicKeySpec) {
            return new BCMcElieceCCA2PublicKey((McElieceCCA2PublicKeySpec)keySpec);
        }
        if (keySpec instanceof X509EncodedKeySpec) {
            SubjectPublicKeyInfo subjectPublicKeyInfo;
            byte[] arrby = ((X509EncodedKeySpec)keySpec).getEncoded();
            try {
                subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(arrby));
            }
            catch (IOException iOException) {
                throw new InvalidKeySpecException(iOException.toString());
            }
            try {
                ASN1Sequence aSN1Sequence = (ASN1Sequence)subjectPublicKeyInfo.parsePublicKey();
                ((ASN1ObjectIdentifier)aSN1Sequence.getObjectAt(0)).toString();
                BCMcElieceCCA2PublicKey bCMcElieceCCA2PublicKey = new BCMcElieceCCA2PublicKey(new McElieceCCA2PublicKeySpec(OID, ((ASN1Integer)aSN1Sequence.getObjectAt(1)).getValue().intValue(), ((ASN1Integer)aSN1Sequence.getObjectAt(2)).getValue().intValue(), ((ASN1OctetString)aSN1Sequence.getObjectAt(3)).getOctets()));
                return bCMcElieceCCA2PublicKey;
            }
            catch (IOException iOException) {
                throw new InvalidKeySpecException("Unable to decode X509EncodedKeySpec: " + iOException.getMessage());
            }
        }
        throw new InvalidKeySpecException("Unsupported key specification: " + (Object)keySpec.getClass() + ".");
    }

    public PublicKey generatePublic(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        try {
            McElieceCCA2PublicKey mcElieceCCA2PublicKey = McElieceCCA2PublicKey.getInstance((ASN1Sequence)subjectPublicKeyInfo.parsePublicKey());
            BCMcElieceCCA2PublicKey bCMcElieceCCA2PublicKey = new BCMcElieceCCA2PublicKey(mcElieceCCA2PublicKey.getOID().getId(), mcElieceCCA2PublicKey.getN(), mcElieceCCA2PublicKey.getT(), mcElieceCCA2PublicKey.getG());
            return bCMcElieceCCA2PublicKey;
        }
        catch (IOException iOException) {
            throw new InvalidKeySpecException("Unable to decode X509EncodedKeySpec");
        }
    }

    public KeySpec getKeySpec(Key key, Class class_) {
        if (key instanceof BCMcElieceCCA2PrivateKey) {
            if (PKCS8EncodedKeySpec.class.isAssignableFrom(class_)) {
                return new PKCS8EncodedKeySpec(key.getEncoded());
            }
            if (McElieceCCA2PrivateKeySpec.class.isAssignableFrom(class_)) {
                BCMcElieceCCA2PrivateKey bCMcElieceCCA2PrivateKey = (BCMcElieceCCA2PrivateKey)key;
                return new McElieceCCA2PrivateKeySpec(OID, bCMcElieceCCA2PrivateKey.getN(), bCMcElieceCCA2PrivateKey.getK(), bCMcElieceCCA2PrivateKey.getField(), bCMcElieceCCA2PrivateKey.getGoppaPoly(), bCMcElieceCCA2PrivateKey.getP(), bCMcElieceCCA2PrivateKey.getH(), bCMcElieceCCA2PrivateKey.getQInv());
            }
        } else if (key instanceof BCMcElieceCCA2PublicKey) {
            if (X509EncodedKeySpec.class.isAssignableFrom(class_)) {
                return new X509EncodedKeySpec(key.getEncoded());
            }
            if (McElieceCCA2PublicKeySpec.class.isAssignableFrom(class_)) {
                BCMcElieceCCA2PublicKey bCMcElieceCCA2PublicKey = (BCMcElieceCCA2PublicKey)key;
                return new McElieceCCA2PublicKeySpec(OID, bCMcElieceCCA2PublicKey.getN(), bCMcElieceCCA2PublicKey.getT(), bCMcElieceCCA2PublicKey.getG());
            }
        } else {
            throw new InvalidKeySpecException("Unsupported key type: " + (Object)key.getClass() + ".");
        }
        throw new InvalidKeySpecException("Unknown key specification: " + (Object)class_ + ".");
    }

    public Key translateKey(Key key) {
        if (key instanceof BCMcElieceCCA2PrivateKey || key instanceof BCMcElieceCCA2PublicKey) {
            return key;
        }
        throw new InvalidKeyException("Unsupported key type.");
    }
}

