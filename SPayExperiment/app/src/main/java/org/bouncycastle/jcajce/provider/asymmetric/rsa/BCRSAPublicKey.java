/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.EOFException
 *  java.io.IOException
 *  java.io.ObjectInputStream
 *  java.io.ObjectOutputStream
 *  java.io.OptionalDataException
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.interfaces.RSAPublicKey
 *  java.security.spec.RSAPublicKeySpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.rsa;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.math.BigInteger;
import java.security.spec.RSAPublicKeySpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil;

public class BCRSAPublicKey
implements java.security.interfaces.RSAPublicKey {
    private static final AlgorithmIdentifier DEFAULT_ALGORITHM_IDENTIFIER = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
    static final long serialVersionUID = 2675817738516720772L;
    private transient AlgorithmIdentifier algorithmIdentifier;
    private BigInteger modulus;
    private BigInteger publicExponent;

    BCRSAPublicKey(java.security.interfaces.RSAPublicKey rSAPublicKey) {
        this.algorithmIdentifier = DEFAULT_ALGORITHM_IDENTIFIER;
        this.modulus = rSAPublicKey.getModulus();
        this.publicExponent = rSAPublicKey.getPublicExponent();
    }

    BCRSAPublicKey(RSAPublicKeySpec rSAPublicKeySpec) {
        this.algorithmIdentifier = DEFAULT_ALGORITHM_IDENTIFIER;
        this.modulus = rSAPublicKeySpec.getModulus();
        this.publicExponent = rSAPublicKeySpec.getPublicExponent();
    }

    BCRSAPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.populateFromPublicKeyInfo(subjectPublicKeyInfo);
    }

    BCRSAPublicKey(RSAKeyParameters rSAKeyParameters) {
        this.algorithmIdentifier = DEFAULT_ALGORITHM_IDENTIFIER;
        this.modulus = rSAKeyParameters.getModulus();
        this.publicExponent = rSAKeyParameters.getExponent();
    }

    private void populateFromPublicKeyInfo(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        try {
            RSAPublicKey rSAPublicKey = RSAPublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey());
            this.algorithmIdentifier = subjectPublicKeyInfo.getAlgorithm();
            this.modulus = rSAPublicKey.getModulus();
            this.publicExponent = rSAPublicKey.getPublicExponent();
            return;
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("invalid info structure in RSA public key");
        }
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        try {
            this.algorithmIdentifier = AlgorithmIdentifier.getInstance(objectInputStream.readObject());
            return;
        }
        catch (OptionalDataException optionalDataException) {
            this.algorithmIdentifier = DEFAULT_ALGORITHM_IDENTIFIER;
            return;
        }
        catch (EOFException eOFException) {
            this.algorithmIdentifier = DEFAULT_ALGORITHM_IDENTIFIER;
            return;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        if (!this.algorithmIdentifier.equals(DEFAULT_ALGORITHM_IDENTIFIER)) {
            objectOutputStream.writeObject((Object)this.algorithmIdentifier.getEncoded());
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block5 : {
            block4 : {
                if (object == this) break block4;
                if (!(object instanceof java.security.interfaces.RSAPublicKey)) {
                    return false;
                }
                java.security.interfaces.RSAPublicKey rSAPublicKey = (java.security.interfaces.RSAPublicKey)object;
                if (!this.getModulus().equals((Object)rSAPublicKey.getModulus()) || !this.getPublicExponent().equals((Object)rSAPublicKey.getPublicExponent())) break block5;
            }
            return true;
        }
        return false;
    }

    public String getAlgorithm() {
        return "RSA";
    }

    public byte[] getEncoded() {
        return KeyUtil.getEncodedSubjectPublicKeyInfo(this.algorithmIdentifier, new RSAPublicKey(this.getModulus(), this.getPublicExponent()));
    }

    public String getFormat() {
        return "X.509";
    }

    public BigInteger getModulus() {
        return this.modulus;
    }

    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }

    public int hashCode() {
        return this.getModulus().hashCode() ^ this.getPublicExponent().hashCode();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        String string = System.getProperty((String)"line.separator");
        stringBuffer.append("RSA Public Key").append(string);
        stringBuffer.append("            modulus: ").append(this.getModulus().toString(16)).append(string);
        stringBuffer.append("    public exponent: ").append(this.getPublicExponent().toString(16)).append(string);
        return stringBuffer.toString();
    }
}

