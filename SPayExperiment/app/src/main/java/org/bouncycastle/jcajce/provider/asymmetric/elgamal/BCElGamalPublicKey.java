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
 *  java.math.BigInteger
 *  javax.crypto.interfaces.DHPublicKey
 *  javax.crypto.spec.DHParameterSpec
 *  javax.crypto.spec.DHPublicKeySpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.elgamal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.oiw.ElGamalParameter;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.jce.interfaces.ElGamalPublicKey;
import org.bouncycastle.jce.spec.ElGamalParameterSpec;
import org.bouncycastle.jce.spec.ElGamalPublicKeySpec;

public class BCElGamalPublicKey
implements DHPublicKey,
ElGamalPublicKey {
    static final long serialVersionUID = 8712728417091216948L;
    private transient ElGamalParameterSpec elSpec;
    private BigInteger y;

    BCElGamalPublicKey(BigInteger bigInteger, ElGamalParameterSpec elGamalParameterSpec) {
        this.y = bigInteger;
        this.elSpec = elGamalParameterSpec;
    }

    BCElGamalPublicKey(DHPublicKey dHPublicKey) {
        this.y = dHPublicKey.getY();
        this.elSpec = new ElGamalParameterSpec(dHPublicKey.getParams().getP(), dHPublicKey.getParams().getG());
    }

    BCElGamalPublicKey(DHPublicKeySpec dHPublicKeySpec) {
        this.y = dHPublicKeySpec.getY();
        this.elSpec = new ElGamalParameterSpec(dHPublicKeySpec.getP(), dHPublicKeySpec.getG());
    }

    BCElGamalPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        ASN1Integer aSN1Integer;
        ElGamalParameter elGamalParameter = ElGamalParameter.getInstance(subjectPublicKeyInfo.getAlgorithm().getParameters());
        try {
            aSN1Integer = (ASN1Integer)subjectPublicKeyInfo.parsePublicKey();
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("invalid info structure in DSA public key");
        }
        this.y = aSN1Integer.getValue();
        this.elSpec = new ElGamalParameterSpec(elGamalParameter.getP(), elGamalParameter.getG());
    }

    BCElGamalPublicKey(ElGamalPublicKeyParameters elGamalPublicKeyParameters) {
        this.y = elGamalPublicKeyParameters.getY();
        this.elSpec = new ElGamalParameterSpec(elGamalPublicKeyParameters.getParameters().getP(), elGamalPublicKeyParameters.getParameters().getG());
    }

    BCElGamalPublicKey(ElGamalPublicKey elGamalPublicKey) {
        this.y = elGamalPublicKey.getY();
        this.elSpec = elGamalPublicKey.getParameters();
    }

    BCElGamalPublicKey(ElGamalPublicKeySpec elGamalPublicKeySpec) {
        this.y = elGamalPublicKeySpec.getY();
        this.elSpec = new ElGamalParameterSpec(elGamalPublicKeySpec.getParams().getP(), elGamalPublicKeySpec.getParams().getG());
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        this.elSpec = new ElGamalParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject((Object)this.elSpec.getP());
        objectOutputStream.writeObject((Object)this.elSpec.getG());
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (!(object instanceof DHPublicKey)) break block2;
                DHPublicKey dHPublicKey = (DHPublicKey)object;
                if (this.getY().equals((Object)dHPublicKey.getY()) && this.getParams().getG().equals((Object)dHPublicKey.getParams().getG()) && this.getParams().getP().equals((Object)dHPublicKey.getParams().getP()) && this.getParams().getL() == dHPublicKey.getParams().getL()) break block3;
            }
            return false;
        }
        return true;
    }

    public String getAlgorithm() {
        return "ElGamal";
    }

    public byte[] getEncoded() {
        try {
            byte[] arrby = new SubjectPublicKeyInfo(new AlgorithmIdentifier(OIWObjectIdentifiers.elGamalAlgorithm, new ElGamalParameter(this.elSpec.getP(), this.elSpec.getG())), new ASN1Integer(this.y)).getEncoded("DER");
            return arrby;
        }
        catch (IOException iOException) {
            return null;
        }
    }

    public String getFormat() {
        return "X.509";
    }

    @Override
    public ElGamalParameterSpec getParameters() {
        return this.elSpec;
    }

    public DHParameterSpec getParams() {
        return new DHParameterSpec(this.elSpec.getP(), this.elSpec.getG());
    }

    @Override
    public BigInteger getY() {
        return this.y;
    }

    public int hashCode() {
        return this.getY().hashCode() ^ this.getParams().getG().hashCode() ^ this.getParams().getP().hashCode() ^ this.getParams().getL();
    }
}

