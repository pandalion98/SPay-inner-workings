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
package org.bouncycastle.jce.provider;

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

public class JCEDHPublicKey
implements DHPublicKey {
    static final long serialVersionUID = -216691575254424324L;
    private DHParameterSpec dhSpec;
    private SubjectPublicKeyInfo info;
    private BigInteger y;

    JCEDHPublicKey(BigInteger bigInteger, DHParameterSpec dHParameterSpec) {
        this.y = bigInteger;
        this.dhSpec = dHParameterSpec;
    }

    JCEDHPublicKey(DHPublicKey dHPublicKey) {
        this.y = dHPublicKey.getY();
        this.dhSpec = dHPublicKey.getParams();
    }

    JCEDHPublicKey(DHPublicKeySpec dHPublicKeySpec) {
        this.y = dHPublicKeySpec.getY();
        this.dhSpec = new DHParameterSpec(dHPublicKeySpec.getP(), dHPublicKeySpec.getG());
    }

    JCEDHPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
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
            aSN1Sequence = ASN1Sequence.getInstance(subjectPublicKeyInfo.getAlgorithmId().getParameters());
            aSN1ObjectIdentifier = subjectPublicKeyInfo.getAlgorithmId().getAlgorithm();
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

    JCEDHPublicKey(DHPublicKeyParameters dHPublicKeyParameters) {
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
        this.y = (BigInteger)objectInputStream.readObject();
        this.dhSpec = new DHParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), objectInputStream.readInt());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.writeObject((Object)this.getY());
        objectOutputStream.writeObject((Object)this.dhSpec.getP());
        objectOutputStream.writeObject((Object)this.dhSpec.getG());
        objectOutputStream.writeInt(this.dhSpec.getL());
    }

    public String getAlgorithm() {
        return "DH";
    }

    public byte[] getEncoded() {
        if (this.info != null) {
            return KeyUtil.getEncodedSubjectPublicKeyInfo(this.info);
        }
        return KeyUtil.getEncodedSubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.dhKeyAgreement, new DHParameter(this.dhSpec.getP(), this.dhSpec.getG(), this.dhSpec.getL())), new ASN1Integer(this.y));
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
}

