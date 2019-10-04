/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.ObjectInputStream
 *  java.io.ObjectOutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.interfaces.DSAParams
 *  java.security.interfaces.DSAPublicKey
 *  java.security.spec.DSAParameterSpec
 *  java.security.spec.DSAPublicKeySpec
 */
package org.bouncycastle.jce.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAParameterSpec;
import java.security.spec.DSAPublicKeySpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DSAParameter;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;

public class JDKDSAPublicKey
implements DSAPublicKey {
    private static final long serialVersionUID = 1752452449903495175L;
    private DSAParams dsaSpec;
    private BigInteger y;

    JDKDSAPublicKey(BigInteger bigInteger, DSAParameterSpec dSAParameterSpec) {
        this.y = bigInteger;
        this.dsaSpec = dSAParameterSpec;
    }

    JDKDSAPublicKey(DSAPublicKey dSAPublicKey) {
        this.y = dSAPublicKey.getY();
        this.dsaSpec = dSAPublicKey.getParams();
    }

    JDKDSAPublicKey(DSAPublicKeySpec dSAPublicKeySpec) {
        this.y = dSAPublicKeySpec.getY();
        this.dsaSpec = new DSAParameterSpec(dSAPublicKeySpec.getP(), dSAPublicKeySpec.getQ(), dSAPublicKeySpec.getG());
    }

    JDKDSAPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        ASN1Integer aSN1Integer;
        try {
            aSN1Integer = (ASN1Integer)subjectPublicKeyInfo.parsePublicKey();
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("invalid info structure in DSA public key");
        }
        this.y = aSN1Integer.getValue();
        if (this.isNotNull(subjectPublicKeyInfo.getAlgorithm().getParameters())) {
            DSAParameter dSAParameter = DSAParameter.getInstance(subjectPublicKeyInfo.getAlgorithm().getParameters());
            this.dsaSpec = new DSAParameterSpec(dSAParameter.getP(), dSAParameter.getQ(), dSAParameter.getG());
        }
    }

    JDKDSAPublicKey(DSAPublicKeyParameters dSAPublicKeyParameters) {
        this.y = dSAPublicKeyParameters.getY();
        this.dsaSpec = new DSAParameterSpec(dSAPublicKeyParameters.getParameters().getP(), dSAPublicKeyParameters.getParameters().getQ(), dSAPublicKeyParameters.getParameters().getG());
    }

    private boolean isNotNull(ASN1Encodable aSN1Encodable) {
        return aSN1Encodable != null && !DERNull.INSTANCE.equals(aSN1Encodable);
    }

    private void readObject(ObjectInputStream objectInputStream) {
        this.y = (BigInteger)objectInputStream.readObject();
        this.dsaSpec = new DSAParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.writeObject((Object)this.y);
        objectOutputStream.writeObject((Object)this.dsaSpec.getP());
        objectOutputStream.writeObject((Object)this.dsaSpec.getQ());
        objectOutputStream.writeObject((Object)this.dsaSpec.getG());
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (!(object instanceof DSAPublicKey)) break block2;
                DSAPublicKey dSAPublicKey = (DSAPublicKey)object;
                if (this.getY().equals((Object)dSAPublicKey.getY()) && this.getParams().getG().equals((Object)dSAPublicKey.getParams().getG()) && this.getParams().getP().equals((Object)dSAPublicKey.getParams().getP()) && this.getParams().getQ().equals((Object)dSAPublicKey.getParams().getQ())) break block3;
            }
            return false;
        }
        return true;
    }

    public String getAlgorithm() {
        return "DSA";
    }

    public byte[] getEncoded() {
        try {
            if (this.dsaSpec == null) {
                return new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa), new ASN1Integer(this.y)).getEncoded("DER");
            }
            byte[] arrby = new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa, new DSAParameter(this.dsaSpec.getP(), this.dsaSpec.getQ(), this.dsaSpec.getG())), new ASN1Integer(this.y)).getEncoded("DER");
            return arrby;
        }
        catch (IOException iOException) {
            return null;
        }
    }

    public String getFormat() {
        return "X.509";
    }

    public DSAParams getParams() {
        return this.dsaSpec;
    }

    public BigInteger getY() {
        return this.y;
    }

    public int hashCode() {
        return this.getY().hashCode() ^ this.getParams().getG().hashCode() ^ this.getParams().getP().hashCode() ^ this.getParams().getQ().hashCode();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        String string = System.getProperty((String)"line.separator");
        stringBuffer.append("DSA Public Key").append(string);
        stringBuffer.append("            y: ").append(this.getY().toString(16)).append(string);
        return stringBuffer.toString();
    }
}

