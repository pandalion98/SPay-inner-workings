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
 *  java.util.Enumeration
 */
package org.bouncycastle.jcajce.provider.asymmetric.gost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.params.GOST3410PrivateKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.PKCS12BagAttributeCarrierImpl;
import org.bouncycastle.jce.interfaces.GOST3410Params;
import org.bouncycastle.jce.interfaces.GOST3410PrivateKey;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle.jce.spec.GOST3410PrivateKeySpec;
import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;

public class BCGOST3410PrivateKey
implements GOST3410PrivateKey,
PKCS12BagAttributeCarrier {
    static final long serialVersionUID = 8581661527592305464L;
    private transient PKCS12BagAttributeCarrier attrCarrier = new PKCS12BagAttributeCarrierImpl();
    private transient GOST3410Params gost3410Spec;
    private BigInteger x;

    protected BCGOST3410PrivateKey() {
    }

    BCGOST3410PrivateKey(PrivateKeyInfo privateKeyInfo) {
        GOST3410PublicKeyAlgParameters gOST3410PublicKeyAlgParameters = new GOST3410PublicKeyAlgParameters((ASN1Sequence)privateKeyInfo.getAlgorithmId().getParameters());
        byte[] arrby = ASN1OctetString.getInstance(privateKeyInfo.parsePrivateKey()).getOctets();
        byte[] arrby2 = new byte[arrby.length];
        for (int i = 0; i != arrby.length; ++i) {
            arrby2[i] = arrby[-1 + arrby.length - i];
        }
        this.x = new BigInteger(1, arrby2);
        this.gost3410Spec = GOST3410ParameterSpec.fromPublicKeyAlg(gOST3410PublicKeyAlgParameters);
    }

    BCGOST3410PrivateKey(GOST3410PrivateKeyParameters gOST3410PrivateKeyParameters, GOST3410ParameterSpec gOST3410ParameterSpec) {
        this.x = gOST3410PrivateKeyParameters.getX();
        this.gost3410Spec = gOST3410ParameterSpec;
        if (gOST3410ParameterSpec == null) {
            throw new IllegalArgumentException("spec is null");
        }
    }

    BCGOST3410PrivateKey(GOST3410PrivateKey gOST3410PrivateKey) {
        this.x = gOST3410PrivateKey.getX();
        this.gost3410Spec = gOST3410PrivateKey.getParameters();
    }

    BCGOST3410PrivateKey(GOST3410PrivateKeySpec gOST3410PrivateKeySpec) {
        this.x = gOST3410PrivateKeySpec.getX();
        this.gost3410Spec = new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec(gOST3410PrivateKeySpec.getP(), gOST3410PrivateKeySpec.getQ(), gOST3410PrivateKeySpec.getA()));
    }

    private boolean compareObj(Object object, Object object2) {
        if (object == object2) {
            return true;
        }
        if (object == null) {
            return false;
        }
        return object.equals(object2);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        String string = (String)objectInputStream.readObject();
        if (string != null) {
            this.gost3410Spec = new GOST3410ParameterSpec(string, (String)objectInputStream.readObject(), (String)objectInputStream.readObject());
        } else {
            this.gost3410Spec = new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject()));
            objectInputStream.readObject();
            objectInputStream.readObject();
        }
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        if (this.gost3410Spec.getPublicKeyParamSetOID() != null) {
            objectOutputStream.writeObject((Object)this.gost3410Spec.getPublicKeyParamSetOID());
            objectOutputStream.writeObject((Object)this.gost3410Spec.getDigestParamSetOID());
            objectOutputStream.writeObject((Object)this.gost3410Spec.getEncryptionParamSetOID());
            return;
        }
        objectOutputStream.writeObject(null);
        objectOutputStream.writeObject((Object)this.gost3410Spec.getPublicKeyParameters().getP());
        objectOutputStream.writeObject((Object)this.gost3410Spec.getPublicKeyParameters().getQ());
        objectOutputStream.writeObject((Object)this.gost3410Spec.getPublicKeyParameters().getA());
        objectOutputStream.writeObject((Object)this.gost3410Spec.getDigestParamSetOID());
        objectOutputStream.writeObject((Object)this.gost3410Spec.getEncryptionParamSetOID());
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (!(object instanceof GOST3410PrivateKey)) break block2;
                GOST3410PrivateKey gOST3410PrivateKey = (GOST3410PrivateKey)object;
                if (this.getX().equals((Object)gOST3410PrivateKey.getX()) && this.getParameters().getPublicKeyParameters().equals(gOST3410PrivateKey.getParameters().getPublicKeyParameters()) && this.getParameters().getDigestParamSetOID().equals((Object)gOST3410PrivateKey.getParameters().getDigestParamSetOID()) && this.compareObj(this.getParameters().getEncryptionParamSetOID(), gOST3410PrivateKey.getParameters().getEncryptionParamSetOID())) break block3;
            }
            return false;
        }
        return true;
    }

    public String getAlgorithm() {
        return "GOST3410";
    }

    @Override
    public ASN1Encodable getBagAttribute(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return this.attrCarrier.getBagAttribute(aSN1ObjectIdentifier);
    }

    @Override
    public Enumeration getBagAttributeKeys() {
        return this.attrCarrier.getBagAttributeKeys();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getEncoded() {
        byte[] arrby;
        int n = 0;
        byte[] arrby2 = this.getX().toByteArray();
        if (arrby2[0] == 0) {
            arrby = new byte[-1 + arrby2.length];
        } else {
            arrby = new byte[arrby2.length];
            n = 0;
        }
        while (n != arrby.length) {
            arrby[n] = arrby2[-1 + arrby2.length - n];
            ++n;
        }
        try {
            PrivateKeyInfo privateKeyInfo;
            PrivateKeyInfo privateKeyInfo2;
            if (this.gost3410Spec instanceof GOST3410ParameterSpec) {
                privateKeyInfo2 = new PrivateKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94, new GOST3410PublicKeyAlgParameters(new ASN1ObjectIdentifier(this.gost3410Spec.getPublicKeyParamSetOID()), new ASN1ObjectIdentifier(this.gost3410Spec.getDigestParamSetOID()))), new DEROctetString(arrby));
                return privateKeyInfo2.getEncoded("DER");
            }
            privateKeyInfo2 = privateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94), new DEROctetString(arrby));
            return privateKeyInfo2.getEncoded("DER");
        }
        catch (IOException iOException) {
            return null;
        }
    }

    public String getFormat() {
        return "PKCS#8";
    }

    @Override
    public GOST3410Params getParameters() {
        return this.gost3410Spec;
    }

    @Override
    public BigInteger getX() {
        return this.x;
    }

    public int hashCode() {
        return this.getX().hashCode() ^ this.gost3410Spec.hashCode();
    }

    @Override
    public void setBagAttribute(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Encodable aSN1Encodable) {
        this.attrCarrier.setBagAttribute(aSN1ObjectIdentifier, aSN1Encodable);
    }
}

