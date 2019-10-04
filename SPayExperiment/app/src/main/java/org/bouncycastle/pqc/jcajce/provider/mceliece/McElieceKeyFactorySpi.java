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
import org.bouncycastle.pqc.asn1.McEliecePrivateKey;
import org.bouncycastle.pqc.asn1.McEliecePublicKey;
import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePrivateKey;
import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePublicKey;
import org.bouncycastle.pqc.jcajce.spec.McEliecePrivateKeySpec;
import org.bouncycastle.pqc.jcajce.spec.McEliecePublicKeySpec;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;

public class McElieceKeyFactorySpi
extends KeyFactorySpi {
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.1";

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
        if (keySpec instanceof McEliecePrivateKeySpec) {
            return new BCMcEliecePrivateKey((McEliecePrivateKeySpec)keySpec);
        }
        if (keySpec instanceof PKCS8EncodedKeySpec == false) throw new InvalidKeySpecException("Unsupported key specification: " + (Object)keySpec.getClass() + ".");
        arrby = ((PKCS8EncodedKeySpec)keySpec).getEncoded();
        privateKeyInfo = PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(arrby));
        {
            catch (IOException iOException) {
                throw new InvalidKeySpecException("Unable to decode PKCS8EncodedKeySpec: " + (Object)iOException);
            }
        }
        ** try [egrp 1[TRYBLOCK] [1 : 45->241)] { 
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
            arrby6 = ((ASN1OctetString)aSN1Sequence.getObjectAt(7)).getOctets();
            arrby7 = ((ASN1OctetString)aSN1Sequence.getObjectAt(8)).getOctets();
            aSN1Sequence2 = (ASN1Sequence)aSN1Sequence.getObjectAt(9);
            arrarrby = new byte[aSN1Sequence2.size()][];
            while (i < aSN1Sequence2.size()) {
                arrarrby[i] = ((ASN1OctetString)aSN1Sequence2.getObjectAt(i)).getOctets();
                ++i;
            }
            return new BCMcEliecePrivateKey(new McEliecePrivateKeySpec("1.3.6.1.4.1.8301.3.1.3.4.1", n, n2, arrby2, arrby3, arrby4, arrby5, arrby6, arrby7, arrarrby));
        }
lbl29: // 1 sources:
        catch (IOException iOException) {
            throw new InvalidKeySpecException("Unable to decode PKCS8EncodedKeySpec.");
        }
    }

    public PrivateKey generatePrivate(PrivateKeyInfo privateKeyInfo) {
        try {
            McEliecePrivateKey mcEliecePrivateKey = McEliecePrivateKey.getInstance(privateKeyInfo.parsePrivateKey().toASN1Primitive());
            BCMcEliecePrivateKey bCMcEliecePrivateKey = new BCMcEliecePrivateKey(mcEliecePrivateKey.getOID().getId(), mcEliecePrivateKey.getN(), mcEliecePrivateKey.getK(), mcEliecePrivateKey.getField(), mcEliecePrivateKey.getGoppaPoly(), mcEliecePrivateKey.getSInv(), mcEliecePrivateKey.getP1(), mcEliecePrivateKey.getP2(), mcEliecePrivateKey.getH(), mcEliecePrivateKey.getQInv());
            return bCMcEliecePrivateKey;
        }
        catch (IOException iOException) {
            throw new InvalidKeySpecException("Unable to decode PKCS8EncodedKeySpec");
        }
    }

    public PublicKey generatePublic(KeySpec keySpec) {
        if (keySpec instanceof McEliecePublicKeySpec) {
            return new BCMcEliecePublicKey((McEliecePublicKeySpec)keySpec);
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
                int n = ((ASN1Integer)aSN1Sequence.getObjectAt(1)).getValue().intValue();
                BCMcEliecePublicKey bCMcEliecePublicKey = new BCMcEliecePublicKey(new McEliecePublicKeySpec(OID, ((ASN1Integer)aSN1Sequence.getObjectAt(2)).getValue().intValue(), n, ((ASN1OctetString)aSN1Sequence.getObjectAt(3)).getOctets()));
                return bCMcEliecePublicKey;
            }
            catch (IOException iOException) {
                throw new InvalidKeySpecException("Unable to decode X509EncodedKeySpec: " + iOException.getMessage());
            }
        }
        throw new InvalidKeySpecException("Unsupported key specification: " + (Object)keySpec.getClass() + ".");
    }

    public PublicKey generatePublic(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        try {
            McEliecePublicKey mcEliecePublicKey = McEliecePublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey());
            BCMcEliecePublicKey bCMcEliecePublicKey = new BCMcEliecePublicKey(mcEliecePublicKey.getOID().getId(), mcEliecePublicKey.getN(), mcEliecePublicKey.getT(), mcEliecePublicKey.getG());
            return bCMcEliecePublicKey;
        }
        catch (IOException iOException) {
            throw new InvalidKeySpecException("Unable to decode X509EncodedKeySpec");
        }
    }

    public KeySpec getKeySpec(Key key, Class class_) {
        if (key instanceof BCMcEliecePrivateKey) {
            if (PKCS8EncodedKeySpec.class.isAssignableFrom(class_)) {
                return new PKCS8EncodedKeySpec(key.getEncoded());
            }
            if (McEliecePrivateKeySpec.class.isAssignableFrom(class_)) {
                BCMcEliecePrivateKey bCMcEliecePrivateKey = (BCMcEliecePrivateKey)key;
                return new McEliecePrivateKeySpec(OID, bCMcEliecePrivateKey.getN(), bCMcEliecePrivateKey.getK(), bCMcEliecePrivateKey.getField(), bCMcEliecePrivateKey.getGoppaPoly(), bCMcEliecePrivateKey.getSInv(), bCMcEliecePrivateKey.getP1(), bCMcEliecePrivateKey.getP2(), bCMcEliecePrivateKey.getH(), bCMcEliecePrivateKey.getQInv());
            }
        } else if (key instanceof BCMcEliecePublicKey) {
            if (X509EncodedKeySpec.class.isAssignableFrom(class_)) {
                return new X509EncodedKeySpec(key.getEncoded());
            }
            if (McEliecePublicKeySpec.class.isAssignableFrom(class_)) {
                BCMcEliecePublicKey bCMcEliecePublicKey = (BCMcEliecePublicKey)key;
                return new McEliecePublicKeySpec(OID, bCMcEliecePublicKey.getN(), bCMcEliecePublicKey.getT(), bCMcEliecePublicKey.getG());
            }
        } else {
            throw new InvalidKeySpecException("Unsupported key type: " + (Object)key.getClass() + ".");
        }
        throw new InvalidKeySpecException("Unknown key specification: " + (Object)class_ + ".");
    }

    public Key translateKey(Key key) {
        if (key instanceof BCMcEliecePrivateKey || key instanceof BCMcEliecePublicKey) {
            return key;
        }
        throw new InvalidKeyException("Unsupported key type.");
    }
}

