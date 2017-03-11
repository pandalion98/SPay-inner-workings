package org.bouncycastle.pqc.jcajce.provider.mceliece;

import java.io.IOException;
import java.security.PublicKey;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.pqc.asn1.McEliecePublicKey;
import org.bouncycastle.pqc.crypto.mceliece.McElieceParameters;
import org.bouncycastle.pqc.crypto.mceliece.McEliecePublicKeyParameters;
import org.bouncycastle.pqc.jcajce.spec.McEliecePublicKeySpec;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;

public class BCMcEliecePublicKey implements PublicKey, CipherParameters {
    private static final long serialVersionUID = 1;
    private McElieceParameters McElieceParams;
    private GF2Matrix f453g;
    private int f454n;
    private String oid;
    private int f455t;

    public BCMcEliecePublicKey(String str, int i, int i2, GF2Matrix gF2Matrix) {
        this.oid = str;
        this.f454n = i;
        this.f455t = i2;
        this.f453g = gF2Matrix;
    }

    public BCMcEliecePublicKey(McEliecePublicKeyParameters mcEliecePublicKeyParameters) {
        this(mcEliecePublicKeyParameters.getOIDString(), mcEliecePublicKeyParameters.getN(), mcEliecePublicKeyParameters.getT(), mcEliecePublicKeyParameters.getG());
        this.McElieceParams = mcEliecePublicKeyParameters.getParameters();
    }

    public BCMcEliecePublicKey(McEliecePublicKeySpec mcEliecePublicKeySpec) {
        this(mcEliecePublicKeySpec.getOIDString(), mcEliecePublicKeySpec.getN(), mcEliecePublicKeySpec.getT(), mcEliecePublicKeySpec.getG());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BCMcEliecePublicKey)) {
            return false;
        }
        BCMcEliecePublicKey bCMcEliecePublicKey = (BCMcEliecePublicKey) obj;
        return this.f454n == bCMcEliecePublicKey.f454n && this.f455t == bCMcEliecePublicKey.f455t && this.f453g.equals(bCMcEliecePublicKey.f453g);
    }

    protected ASN1Primitive getAlgParams() {
        return null;
    }

    public String getAlgorithm() {
        return "McEliece";
    }

    public byte[] getEncoded() {
        try {
            return new SubjectPublicKeyInfo(new AlgorithmIdentifier(getOID(), DERNull.INSTANCE), new McEliecePublicKey(new ASN1ObjectIdentifier(this.oid), this.f454n, this.f455t, this.f453g)).getEncoded();
        } catch (IOException e) {
            return null;
        }
    }

    public String getFormat() {
        return null;
    }

    public GF2Matrix getG() {
        return this.f453g;
    }

    public int getK() {
        return this.f453g.getNumRows();
    }

    public McElieceParameters getMcElieceParameters() {
        return this.McElieceParams;
    }

    public int getN() {
        return this.f454n;
    }

    protected ASN1ObjectIdentifier getOID() {
        return new ASN1ObjectIdentifier(McElieceKeyFactorySpi.OID);
    }

    public String getOIDString() {
        return this.oid;
    }

    public int getT() {
        return this.f455t;
    }

    public int hashCode() {
        return (this.f454n + this.f455t) + this.f453g.hashCode();
    }

    public String toString() {
        return (("McEliecePublicKey:\n" + " length of the code         : " + this.f454n + "\n") + " error correction capability: " + this.f455t + "\n") + " generator matrix           : " + this.f453g.toString();
    }
}
