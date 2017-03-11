package org.bouncycastle.pqc.jcajce.provider.mceliece;

import java.io.IOException;
import java.security.PrivateKey;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.pqc.asn1.McEliecePrivateKey;
import org.bouncycastle.pqc.crypto.mceliece.McElieceParameters;
import org.bouncycastle.pqc.crypto.mceliece.McEliecePrivateKeyParameters;
import org.bouncycastle.pqc.jcajce.spec.McEliecePrivateKeySpec;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;

public class BCMcEliecePrivateKey implements PrivateKey, CipherParameters {
    private static final long serialVersionUID = 1;
    private GF2mField field;
    private PolynomialGF2mSmallM goppaPoly;
    private GF2Matrix f450h;
    private int f451k;
    private McElieceParameters mcElieceParams;
    private int f452n;
    private String oid;
    private Permutation p1;
    private Permutation p2;
    private PolynomialGF2mSmallM[] qInv;
    private GF2Matrix sInv;

    public BCMcEliecePrivateKey(String str, int i, int i2, GF2mField gF2mField, PolynomialGF2mSmallM polynomialGF2mSmallM, GF2Matrix gF2Matrix, Permutation permutation, Permutation permutation2, GF2Matrix gF2Matrix2, PolynomialGF2mSmallM[] polynomialGF2mSmallMArr) {
        this.oid = str;
        this.f452n = i;
        this.f451k = i2;
        this.field = gF2mField;
        this.goppaPoly = polynomialGF2mSmallM;
        this.sInv = gF2Matrix;
        this.p1 = permutation;
        this.p2 = permutation2;
        this.f450h = gF2Matrix2;
        this.qInv = polynomialGF2mSmallMArr;
    }

    public BCMcEliecePrivateKey(McEliecePrivateKeyParameters mcEliecePrivateKeyParameters) {
        this(mcEliecePrivateKeyParameters.getOIDString(), mcEliecePrivateKeyParameters.getN(), mcEliecePrivateKeyParameters.getK(), mcEliecePrivateKeyParameters.getField(), mcEliecePrivateKeyParameters.getGoppaPoly(), mcEliecePrivateKeyParameters.getSInv(), mcEliecePrivateKeyParameters.getP1(), mcEliecePrivateKeyParameters.getP2(), mcEliecePrivateKeyParameters.getH(), mcEliecePrivateKeyParameters.getQInv());
        this.mcElieceParams = mcEliecePrivateKeyParameters.getParameters();
    }

    public BCMcEliecePrivateKey(McEliecePrivateKeySpec mcEliecePrivateKeySpec) {
        this(mcEliecePrivateKeySpec.getOIDString(), mcEliecePrivateKeySpec.getN(), mcEliecePrivateKeySpec.getK(), mcEliecePrivateKeySpec.getField(), mcEliecePrivateKeySpec.getGoppaPoly(), mcEliecePrivateKeySpec.getSInv(), mcEliecePrivateKeySpec.getP1(), mcEliecePrivateKeySpec.getP2(), mcEliecePrivateKeySpec.getH(), mcEliecePrivateKeySpec.getQInv());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BCMcEliecePrivateKey)) {
            return false;
        }
        BCMcEliecePrivateKey bCMcEliecePrivateKey = (BCMcEliecePrivateKey) obj;
        return this.f452n == bCMcEliecePrivateKey.f452n && this.f451k == bCMcEliecePrivateKey.f451k && this.field.equals(bCMcEliecePrivateKey.field) && this.goppaPoly.equals(bCMcEliecePrivateKey.goppaPoly) && this.sInv.equals(bCMcEliecePrivateKey.sInv) && this.p1.equals(bCMcEliecePrivateKey.p1) && this.p2.equals(bCMcEliecePrivateKey.p2) && this.f450h.equals(bCMcEliecePrivateKey.f450h);
    }

    protected ASN1Primitive getAlgParams() {
        return null;
    }

    public String getAlgorithm() {
        return "McEliece";
    }

    public byte[] getEncoded() {
        try {
            try {
                return new PrivateKeyInfo(new AlgorithmIdentifier(getOID(), DERNull.INSTANCE), new McEliecePrivateKey(new ASN1ObjectIdentifier(this.oid), this.f452n, this.f451k, this.field, this.goppaPoly, this.sInv, this.p1, this.p2, this.f450h, this.qInv)).getEncoded();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public GF2mField getField() {
        return this.field;
    }

    public String getFormat() {
        return null;
    }

    public PolynomialGF2mSmallM getGoppaPoly() {
        return this.goppaPoly;
    }

    public GF2Matrix getH() {
        return this.f450h;
    }

    public int getK() {
        return this.f451k;
    }

    public McElieceParameters getMcElieceParameters() {
        return this.mcElieceParams;
    }

    public int getN() {
        return this.f452n;
    }

    protected ASN1ObjectIdentifier getOID() {
        return new ASN1ObjectIdentifier(McElieceKeyFactorySpi.OID);
    }

    public String getOIDString() {
        return this.oid;
    }

    public Permutation getP1() {
        return this.p1;
    }

    public Permutation getP2() {
        return this.p2;
    }

    public PolynomialGF2mSmallM[] getQInv() {
        return this.qInv;
    }

    public GF2Matrix getSInv() {
        return this.sInv;
    }

    public int hashCode() {
        return ((((((this.f451k + this.f452n) + this.field.hashCode()) + this.goppaPoly.hashCode()) + this.sInv.hashCode()) + this.p1.hashCode()) + this.p2.hashCode()) + this.f450h.hashCode();
    }

    public String toString() {
        return (((((" length of the code          : " + this.f452n + "\n") + " dimension of the code       : " + this.f451k + "\n") + " irreducible Goppa polynomial: " + this.goppaPoly + "\n") + " (k x k)-matrix S^-1         : " + this.sInv + "\n") + " permutation P1              : " + this.p1 + "\n") + " permutation P2              : " + this.p2;
    }
}
