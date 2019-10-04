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
 */
package org.bouncycastle.jcajce.provider.asymmetric.gost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.GOST3410PublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil;
import org.bouncycastle.jce.interfaces.GOST3410Params;
import org.bouncycastle.jce.interfaces.GOST3410PublicKey;
import org.bouncycastle.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;
import org.bouncycastle.jce.spec.GOST3410PublicKeySpec;

public class BCGOST3410PublicKey
implements GOST3410PublicKey {
    static final long serialVersionUID = -6251023343619275990L;
    private transient GOST3410Params gost3410Spec;
    private BigInteger y;

    BCGOST3410PublicKey(BigInteger bigInteger, GOST3410ParameterSpec gOST3410ParameterSpec) {
        this.y = bigInteger;
        this.gost3410Spec = gOST3410ParameterSpec;
    }

    BCGOST3410PublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        GOST3410PublicKeyAlgParameters gOST3410PublicKeyAlgParameters = new GOST3410PublicKeyAlgParameters((ASN1Sequence)subjectPublicKeyInfo.getAlgorithmId().getParameters());
        byte[] arrby = ((DEROctetString)subjectPublicKeyInfo.parsePublicKey()).getOctets();
        byte[] arrby2 = new byte[arrby.length];
        int n = 0;
        do {
            if (n == arrby.length) break;
            arrby2[n] = arrby[-1 + arrby.length - n];
            ++n;
        } while (true);
        try {
            this.y = new BigInteger(1, arrby2);
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("invalid info structure in GOST3410 public key");
        }
        this.gost3410Spec = GOST3410ParameterSpec.fromPublicKeyAlg(gOST3410PublicKeyAlgParameters);
    }

    BCGOST3410PublicKey(GOST3410PublicKeyParameters gOST3410PublicKeyParameters, GOST3410ParameterSpec gOST3410ParameterSpec) {
        this.y = gOST3410PublicKeyParameters.getY();
        this.gost3410Spec = gOST3410ParameterSpec;
    }

    BCGOST3410PublicKey(GOST3410PublicKey gOST3410PublicKey) {
        this.y = gOST3410PublicKey.getY();
        this.gost3410Spec = gOST3410PublicKey.getParameters();
    }

    BCGOST3410PublicKey(GOST3410PublicKeySpec gOST3410PublicKeySpec) {
        this.y = gOST3410PublicKeySpec.getY();
        this.gost3410Spec = new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec(gOST3410PublicKeySpec.getP(), gOST3410PublicKeySpec.getQ(), gOST3410PublicKeySpec.getA()));
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        String string = (String)objectInputStream.readObject();
        if (string != null) {
            this.gost3410Spec = new GOST3410ParameterSpec(string, (String)objectInputStream.readObject(), (String)objectInputStream.readObject());
            return;
        }
        this.gost3410Spec = new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject()));
        objectInputStream.readObject();
        objectInputStream.readObject();
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

    public boolean equals(Object object) {
        boolean bl = object instanceof BCGOST3410PublicKey;
        boolean bl2 = false;
        if (bl) {
            BCGOST3410PublicKey bCGOST3410PublicKey = (BCGOST3410PublicKey)object;
            boolean bl3 = this.y.equals((Object)bCGOST3410PublicKey.y);
            bl2 = false;
            if (bl3) {
                boolean bl4 = this.gost3410Spec.equals((Object)bCGOST3410PublicKey.gost3410Spec);
                bl2 = false;
                if (bl4) {
                    bl2 = true;
                }
            }
        }
        return bl2;
    }

    public String getAlgorithm() {
        return "GOST3410";
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getEncoded() {
        byte[] arrby;
        int n = 0;
        byte[] arrby2 = this.getY().toByteArray();
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
            SubjectPublicKeyInfo subjectPublicKeyInfo;
            if (!(this.gost3410Spec instanceof GOST3410ParameterSpec)) {
                SubjectPublicKeyInfo subjectPublicKeyInfo2;
                subjectPublicKeyInfo = subjectPublicKeyInfo2 = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94), new DEROctetString(arrby));
                return KeyUtil.getEncodedSubjectPublicKeyInfo(subjectPublicKeyInfo);
            }
            if (this.gost3410Spec.getEncryptionParamSetOID() != null) {
                subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94, new GOST3410PublicKeyAlgParameters(new ASN1ObjectIdentifier(this.gost3410Spec.getPublicKeyParamSetOID()), new ASN1ObjectIdentifier(this.gost3410Spec.getDigestParamSetOID()), new ASN1ObjectIdentifier(this.gost3410Spec.getEncryptionParamSetOID()))), new DEROctetString(arrby));
                return KeyUtil.getEncodedSubjectPublicKeyInfo(subjectPublicKeyInfo);
            }
            subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94, new GOST3410PublicKeyAlgParameters(new ASN1ObjectIdentifier(this.gost3410Spec.getPublicKeyParamSetOID()), new ASN1ObjectIdentifier(this.gost3410Spec.getDigestParamSetOID()))), new DEROctetString(arrby));
            return KeyUtil.getEncodedSubjectPublicKeyInfo(subjectPublicKeyInfo);
        }
        catch (IOException iOException) {
            return null;
        }
    }

    public String getFormat() {
        return "X.509";
    }

    @Override
    public GOST3410Params getParameters() {
        return this.gost3410Spec;
    }

    @Override
    public BigInteger getY() {
        return this.y;
    }

    public int hashCode() {
        return this.y.hashCode() ^ this.gost3410Spec.hashCode();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        String string = System.getProperty((String)"line.separator");
        stringBuffer.append("GOST3410 Public Key").append(string);
        stringBuffer.append("            y: ").append(this.getY().toString(16)).append(string);
        return stringBuffer.toString();
    }
}

