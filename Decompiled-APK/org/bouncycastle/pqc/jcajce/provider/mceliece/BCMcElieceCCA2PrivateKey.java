package org.bouncycastle.pqc.jcajce.provider.mceliece;

import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.IOException;
import java.security.PrivateKey;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.pqc.asn1.McElieceCCA2PrivateKey;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2Parameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2PrivateKeyParameters;
import org.bouncycastle.pqc.jcajce.spec.McElieceCCA2PrivateKeySpec;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;

public class BCMcElieceCCA2PrivateKey implements PrivateKey, CipherParameters {
    private static final long serialVersionUID = 1;
    private GF2mField field;
    private PolynomialGF2mSmallM goppaPoly;
    private GF2Matrix f443h;
    private int f444k;
    private McElieceCCA2Parameters mcElieceCCA2Params;
    private int f445n;
    private String oid;
    private Permutation f446p;
    private PolynomialGF2mSmallM[] qInv;

    public BCMcElieceCCA2PrivateKey(String str, int i, int i2, GF2mField gF2mField, PolynomialGF2mSmallM polynomialGF2mSmallM, Permutation permutation, GF2Matrix gF2Matrix, PolynomialGF2mSmallM[] polynomialGF2mSmallMArr) {
        this.oid = str;
        this.f445n = i;
        this.f444k = i2;
        this.field = gF2mField;
        this.goppaPoly = polynomialGF2mSmallM;
        this.f446p = permutation;
        this.f443h = gF2Matrix;
        this.qInv = polynomialGF2mSmallMArr;
    }

    public BCMcElieceCCA2PrivateKey(McElieceCCA2PrivateKeyParameters mcElieceCCA2PrivateKeyParameters) {
        this(mcElieceCCA2PrivateKeyParameters.getOIDString(), mcElieceCCA2PrivateKeyParameters.getN(), mcElieceCCA2PrivateKeyParameters.getK(), mcElieceCCA2PrivateKeyParameters.getField(), mcElieceCCA2PrivateKeyParameters.getGoppaPoly(), mcElieceCCA2PrivateKeyParameters.getP(), mcElieceCCA2PrivateKeyParameters.getH(), mcElieceCCA2PrivateKeyParameters.getQInv());
        this.mcElieceCCA2Params = mcElieceCCA2PrivateKeyParameters.getParameters();
    }

    public BCMcElieceCCA2PrivateKey(McElieceCCA2PrivateKeySpec mcElieceCCA2PrivateKeySpec) {
        this(mcElieceCCA2PrivateKeySpec.getOIDString(), mcElieceCCA2PrivateKeySpec.getN(), mcElieceCCA2PrivateKeySpec.getK(), mcElieceCCA2PrivateKeySpec.getField(), mcElieceCCA2PrivateKeySpec.getGoppaPoly(), mcElieceCCA2PrivateKeySpec.getP(), mcElieceCCA2PrivateKeySpec.getH(), mcElieceCCA2PrivateKeySpec.getQInv());
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BCMcElieceCCA2PrivateKey)) {
            return false;
        }
        BCMcElieceCCA2PrivateKey bCMcElieceCCA2PrivateKey = (BCMcElieceCCA2PrivateKey) obj;
        return this.f445n == bCMcElieceCCA2PrivateKey.f445n && this.f444k == bCMcElieceCCA2PrivateKey.f444k && this.field.equals(bCMcElieceCCA2PrivateKey.field) && this.goppaPoly.equals(bCMcElieceCCA2PrivateKey.goppaPoly) && this.f446p.equals(bCMcElieceCCA2PrivateKey.f446p) && this.f443h.equals(bCMcElieceCCA2PrivateKey.f443h);
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
                return new PrivateKeyInfo(new AlgorithmIdentifier(getOID(), DERNull.INSTANCE), new McElieceCCA2PrivateKey(new ASN1ObjectIdentifier(this.oid), this.f445n, this.f444k, this.field, this.goppaPoly, this.f446p, this.f443h, this.qInv)).getEncoded();
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
        return this.f443h;
    }

    public int getK() {
        return this.f444k;
    }

    public McElieceCCA2Parameters getMcElieceCCA2Parameters() {
        return this.mcElieceCCA2Params;
    }

    public int getN() {
        return this.f445n;
    }

    protected ASN1ObjectIdentifier getOID() {
        return new ASN1ObjectIdentifier(McElieceCCA2KeyFactorySpi.OID);
    }

    public String getOIDString() {
        return this.oid;
    }

    public Permutation getP() {
        return this.f446p;
    }

    public PolynomialGF2mSmallM[] getQInv() {
        return this.qInv;
    }

    public int getT() {
        return this.goppaPoly.getDegree();
    }

    public int hashCode() {
        return ((((this.f444k + this.f445n) + this.field.hashCode()) + this.goppaPoly.hashCode()) + this.f446p.hashCode()) + this.f443h.hashCode();
    }

    public String toString() {
        return ((BuildConfig.FLAVOR + " extension degree of the field      : " + this.f445n + "\n") + " dimension of the code              : " + this.f444k + "\n") + " irreducible Goppa polynomial       : " + this.goppaPoly + "\n";
    }
}
