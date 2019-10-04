/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.PrivateKey
 */
package org.bouncycastle.pqc.jcajce.provider.mceliece;

import java.io.IOException;
import java.security.PrivateKey;
import org.bouncycastle.asn1.ASN1Encodable;
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

public class BCMcEliecePrivateKey
implements PrivateKey,
CipherParameters {
    private static final long serialVersionUID = 1L;
    private GF2mField field;
    private PolynomialGF2mSmallM goppaPoly;
    private GF2Matrix h;
    private int k;
    private McElieceParameters mcElieceParams;
    private int n;
    private String oid;
    private Permutation p1;
    private Permutation p2;
    private PolynomialGF2mSmallM[] qInv;
    private GF2Matrix sInv;

    public BCMcEliecePrivateKey(String string, int n, int n2, GF2mField gF2mField, PolynomialGF2mSmallM polynomialGF2mSmallM, GF2Matrix gF2Matrix, Permutation permutation, Permutation permutation2, GF2Matrix gF2Matrix2, PolynomialGF2mSmallM[] arrpolynomialGF2mSmallM) {
        this.oid = string;
        this.n = n;
        this.k = n2;
        this.field = gF2mField;
        this.goppaPoly = polynomialGF2mSmallM;
        this.sInv = gF2Matrix;
        this.p1 = permutation;
        this.p2 = permutation2;
        this.h = gF2Matrix2;
        this.qInv = arrpolynomialGF2mSmallM;
    }

    public BCMcEliecePrivateKey(McEliecePrivateKeyParameters mcEliecePrivateKeyParameters) {
        this(mcEliecePrivateKeyParameters.getOIDString(), mcEliecePrivateKeyParameters.getN(), mcEliecePrivateKeyParameters.getK(), mcEliecePrivateKeyParameters.getField(), mcEliecePrivateKeyParameters.getGoppaPoly(), mcEliecePrivateKeyParameters.getSInv(), mcEliecePrivateKeyParameters.getP1(), mcEliecePrivateKeyParameters.getP2(), mcEliecePrivateKeyParameters.getH(), mcEliecePrivateKeyParameters.getQInv());
        this.mcElieceParams = mcEliecePrivateKeyParameters.getParameters();
    }

    public BCMcEliecePrivateKey(McEliecePrivateKeySpec mcEliecePrivateKeySpec) {
        this(mcEliecePrivateKeySpec.getOIDString(), mcEliecePrivateKeySpec.getN(), mcEliecePrivateKeySpec.getK(), mcEliecePrivateKeySpec.getField(), mcEliecePrivateKeySpec.getGoppaPoly(), mcEliecePrivateKeySpec.getSInv(), mcEliecePrivateKeySpec.getP1(), mcEliecePrivateKeySpec.getP2(), mcEliecePrivateKeySpec.getH(), mcEliecePrivateKeySpec.getQInv());
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (!(object instanceof BCMcEliecePrivateKey)) break block2;
                BCMcEliecePrivateKey bCMcEliecePrivateKey = (BCMcEliecePrivateKey)object;
                if (this.n == bCMcEliecePrivateKey.n && this.k == bCMcEliecePrivateKey.k && this.field.equals(bCMcEliecePrivateKey.field) && this.goppaPoly.equals(bCMcEliecePrivateKey.goppaPoly) && this.sInv.equals(bCMcEliecePrivateKey.sInv) && this.p1.equals(bCMcEliecePrivateKey.p1) && this.p2.equals(bCMcEliecePrivateKey.p2) && this.h.equals(bCMcEliecePrivateKey.h)) break block3;
            }
            return false;
        }
        return true;
    }

    protected ASN1Primitive getAlgParams() {
        return null;
    }

    public String getAlgorithm() {
        return "McEliece";
    }

    public byte[] getEncoded() {
        PrivateKeyInfo privateKeyInfo;
        McEliecePrivateKey mcEliecePrivateKey = new McEliecePrivateKey(new ASN1ObjectIdentifier(this.oid), this.n, this.k, this.field, this.goppaPoly, this.sInv, this.p1, this.p2, this.h, this.qInv);
        try {
            privateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(this.getOID(), DERNull.INSTANCE), mcEliecePrivateKey);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return null;
        }
        try {
            byte[] arrby = privateKeyInfo.getEncoded();
            return arrby;
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
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
        return this.h;
    }

    public int getK() {
        return this.k;
    }

    public McElieceParameters getMcElieceParameters() {
        return this.mcElieceParams;
    }

    public int getN() {
        return this.n;
    }

    protected ASN1ObjectIdentifier getOID() {
        return new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.4.1");
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
        return this.k + this.n + this.field.hashCode() + this.goppaPoly.hashCode() + this.sInv.hashCode() + this.p1.hashCode() + this.p2.hashCode() + this.h.hashCode();
    }

    public String toString() {
        String string = " length of the code          : " + this.n + "\n";
        String string2 = string + " dimension of the code       : " + this.k + "\n";
        String string3 = string2 + " irreducible Goppa polynomial: " + this.goppaPoly + "\n";
        String string4 = string3 + " (k x k)-matrix S^-1         : " + this.sInv + "\n";
        String string5 = string4 + " permutation P1              : " + this.p1 + "\n";
        return string5 + " permutation P2              : " + this.p2;
    }
}

