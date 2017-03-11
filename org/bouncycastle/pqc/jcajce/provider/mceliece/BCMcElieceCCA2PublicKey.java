package org.bouncycastle.pqc.jcajce.provider.mceliece;

import java.io.IOException;
import java.security.PublicKey;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.pqc.asn1.McElieceCCA2PublicKey;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2Parameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2PublicKeyParameters;
import org.bouncycastle.pqc.jcajce.spec.McElieceCCA2PublicKeySpec;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;

public class BCMcElieceCCA2PublicKey implements PublicKey, CipherParameters {
    private static final long serialVersionUID = 1;
    private McElieceCCA2Parameters McElieceCCA2Params;
    private GF2Matrix f447g;
    private int f448n;
    private String oid;
    private int f449t;

    public BCMcElieceCCA2PublicKey(String str, int i, int i2, GF2Matrix gF2Matrix) {
        this.oid = str;
        this.f448n = i;
        this.f449t = i2;
        this.f447g = gF2Matrix;
    }

    public BCMcElieceCCA2PublicKey(McElieceCCA2PublicKeyParameters mcElieceCCA2PublicKeyParameters) {
        this(mcElieceCCA2PublicKeyParameters.getOIDString(), mcElieceCCA2PublicKeyParameters.getN(), mcElieceCCA2PublicKeyParameters.getT(), mcElieceCCA2PublicKeyParameters.getMatrixG());
        this.McElieceCCA2Params = mcElieceCCA2PublicKeyParameters.getParameters();
    }

    public BCMcElieceCCA2PublicKey(McElieceCCA2PublicKeySpec mcElieceCCA2PublicKeySpec) {
        this(mcElieceCCA2PublicKeySpec.getOIDString(), mcElieceCCA2PublicKeySpec.getN(), mcElieceCCA2PublicKeySpec.getT(), mcElieceCCA2PublicKeySpec.getMatrixG());
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BCMcElieceCCA2PublicKey)) {
            return false;
        }
        BCMcElieceCCA2PublicKey bCMcElieceCCA2PublicKey = (BCMcElieceCCA2PublicKey) obj;
        return this.f448n == bCMcElieceCCA2PublicKey.f448n && this.f449t == bCMcElieceCCA2PublicKey.f449t && this.f447g.equals(bCMcElieceCCA2PublicKey.f447g);
    }

    protected ASN1Primitive getAlgParams() {
        return null;
    }

    public String getAlgorithm() {
        return "McEliece";
    }

    public byte[] getEncoded() {
        try {
            return new SubjectPublicKeyInfo(new AlgorithmIdentifier(getOID(), DERNull.INSTANCE), new McElieceCCA2PublicKey(new ASN1ObjectIdentifier(this.oid), this.f448n, this.f449t, this.f447g)).getEncoded();
        } catch (IOException e) {
            return null;
        }
    }

    public String getFormat() {
        return null;
    }

    public GF2Matrix getG() {
        return this.f447g;
    }

    public int getK() {
        return this.f447g.getNumRows();
    }

    public McElieceCCA2Parameters getMcElieceCCA2Parameters() {
        return this.McElieceCCA2Params;
    }

    public int getN() {
        return this.f448n;
    }

    protected ASN1ObjectIdentifier getOID() {
        return new ASN1ObjectIdentifier(McElieceCCA2KeyFactorySpi.OID);
    }

    public String getOIDString() {
        return this.oid;
    }

    public int getT() {
        return this.f449t;
    }

    public int hashCode() {
        return (this.f448n + this.f449t) + this.f447g.hashCode();
    }

    public String toString() {
        return (("McEliecePublicKey:\n" + " length of the code         : " + this.f448n + "\n") + " error correction capability: " + this.f449t + "\n") + " generator matrix           : " + this.f447g.toString();
    }
}
