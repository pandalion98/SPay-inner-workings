/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.interfaces.RSAPublicKey
 *  java.security.spec.RSAPublicKeySpec
 */
package org.bouncycastle.jce.provider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil;

public class JCERSAPublicKey
implements RSAPublicKey {
    static final long serialVersionUID = 2675817738516720772L;
    private BigInteger modulus;
    private BigInteger publicExponent;

    JCERSAPublicKey(RSAPublicKey rSAPublicKey) {
        this.modulus = rSAPublicKey.getModulus();
        this.publicExponent = rSAPublicKey.getPublicExponent();
    }

    JCERSAPublicKey(RSAPublicKeySpec rSAPublicKeySpec) {
        this.modulus = rSAPublicKeySpec.getModulus();
        this.publicExponent = rSAPublicKeySpec.getPublicExponent();
    }

    JCERSAPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        try {
            RSAPublicKeyStructure rSAPublicKeyStructure = new RSAPublicKeyStructure((ASN1Sequence)subjectPublicKeyInfo.parsePublicKey());
            this.modulus = rSAPublicKeyStructure.getModulus();
            this.publicExponent = rSAPublicKeyStructure.getPublicExponent();
            return;
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("invalid info structure in RSA public key");
        }
    }

    JCERSAPublicKey(RSAKeyParameters rSAKeyParameters) {
        this.modulus = rSAKeyParameters.getModulus();
        this.publicExponent = rSAKeyParameters.getExponent();
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block5 : {
            block4 : {
                if (object == this) break block4;
                if (!(object instanceof RSAPublicKey)) {
                    return false;
                }
                RSAPublicKey rSAPublicKey = (RSAPublicKey)object;
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
        return KeyUtil.getEncodedSubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE), new RSAPublicKeyStructure(this.getModulus(), this.getPublicExponent()));
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

