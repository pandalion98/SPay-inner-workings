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
 *  org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil
 */
package org.bouncycastle.jcajce.provider.asymmetric.dh;

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
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.DHParameter;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.DHDomainParameters;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil;

public class BCDHPublicKey
implements DHPublicKey {
    static final long serialVersionUID = -216691575254424324L;
    private transient DHParameterSpec dhSpec;
    private transient SubjectPublicKeyInfo info;
    private BigInteger y;

    BCDHPublicKey(BigInteger bigInteger, DHParameterSpec dHParameterSpec) {
        this.y = bigInteger;
        this.dhSpec = dHParameterSpec;
    }

    BCDHPublicKey(DHPublicKey dHPublicKey) {
        this.y = dHPublicKey.getY();
        this.dhSpec = dHPublicKey.getParams();
    }

    BCDHPublicKey(DHPublicKeySpec dHPublicKeySpec) {
        this.y = dHPublicKeySpec.getY();
        this.dhSpec = new DHParameterSpec(dHPublicKeySpec.getP(), dHPublicKeySpec.getG());
    }

    public BCDHPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        ASN1Sequence aSN1Sequence;
        ASN1ObjectIdentifier aSN1ObjectIdentifier;
        block4 : {
            ASN1Integer aSN1Integer;
            this.info = subjectPublicKeyInfo;
            try {
                aSN1Integer = (ASN1Integer)subjectPublicKeyInfo.parsePublicKey();
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("invalid info structure in DH public key");
            }
            this.y = aSN1Integer.getValue();
            aSN1Sequence = ASN1Sequence.getInstance(subjectPublicKeyInfo.getAlgorithm().getParameters());
            aSN1ObjectIdentifier = subjectPublicKeyInfo.getAlgorithm().getAlgorithm();
            if (!aSN1ObjectIdentifier.equals(PKCSObjectIdentifiers.dhKeyAgreement) && !this.isPKCSParam(aSN1Sequence)) break block4;
            DHParameter dHParameter = DHParameter.getInstance(aSN1Sequence);
            if (dHParameter.getL() != null) {
                this.dhSpec = new DHParameterSpec(dHParameter.getP(), dHParameter.getG(), dHParameter.getL().intValue());
                return;
            }
            this.dhSpec = new DHParameterSpec(dHParameter.getP(), dHParameter.getG());
            return;
        }
        if (aSN1ObjectIdentifier.equals(X9ObjectIdentifiers.dhpublicnumber)) {
            DHDomainParameters dHDomainParameters = DHDomainParameters.getInstance(aSN1Sequence);
            this.dhSpec = new DHParameterSpec(dHDomainParameters.getP().getValue(), dHDomainParameters.getG().getValue());
            return;
        }
        throw new IllegalArgumentException("unknown algorithm type: " + aSN1ObjectIdentifier);
    }

    BCDHPublicKey(DHPublicKeyParameters dHPublicKeyParameters) {
        this.y = dHPublicKeyParameters.getY();
        this.dhSpec = new DHParameterSpec(dHPublicKeyParameters.getParameters().getP(), dHPublicKeyParameters.getParameters().getG(), dHPublicKeyParameters.getParameters().getL());
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean isPKCSParam(ASN1Sequence aSN1Sequence) {
        block5 : {
            block4 : {
                if (aSN1Sequence.size() == 2) break block4;
                if (aSN1Sequence.size() > 3) {
                    return false;
                }
                ASN1Integer aSN1Integer = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(2));
                ASN1Integer aSN1Integer2 = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(0));
                if (aSN1Integer.getValue().compareTo(BigInteger.valueOf((long)aSN1Integer2.getValue().bitLength())) > 0) break block5;
            }
            return true;
        }
        return false;
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        this.dhSpec = new DHParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), objectInputStream.readInt());
        this.info = null;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject((Object)this.dhSpec.getP());
        objectOutputStream.writeObject((Object)this.dhSpec.getG());
        objectOutputStream.writeInt(this.dhSpec.getL());
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
        return "DH";
    }

    public byte[] getEncoded() {
        if (this.info != null) {
            return KeyUtil.getEncodedSubjectPublicKeyInfo((SubjectPublicKeyInfo)this.info);
        }
        return KeyUtil.getEncodedSubjectPublicKeyInfo((AlgorithmIdentifier)new AlgorithmIdentifier(PKCSObjectIdentifiers.dhKeyAgreement, new DHParameter(this.dhSpec.getP(), this.dhSpec.getG(), this.dhSpec.getL()).toASN1Primitive()), (ASN1Encodable)new ASN1Integer(this.y));
    }

    public String getFormat() {
        return "X.509";
    }

    public DHParameterSpec getParams() {
        return this.dhSpec;
    }

    public BigInteger getY() {
        return this.y;
    }

    public int hashCode() {
        return this.getY().hashCode() ^ this.getParams().getG().hashCode() ^ this.getParams().getP().hashCode() ^ this.getParams().getL();
    }
}

