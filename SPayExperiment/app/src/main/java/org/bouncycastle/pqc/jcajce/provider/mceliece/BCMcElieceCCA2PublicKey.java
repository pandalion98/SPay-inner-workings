/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.PublicKey
 */
package org.bouncycastle.pqc.jcajce.provider.mceliece;

import java.io.IOException;
import java.security.PublicKey;
import org.bouncycastle.asn1.ASN1Encodable;
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

public class BCMcElieceCCA2PublicKey
implements PublicKey,
CipherParameters {
    private static final long serialVersionUID = 1L;
    private McElieceCCA2Parameters McElieceCCA2Params;
    private GF2Matrix g;
    private int n;
    private String oid;
    private int t;

    public BCMcElieceCCA2PublicKey(String string, int n, int n2, GF2Matrix gF2Matrix) {
        this.oid = string;
        this.n = n;
        this.t = n2;
        this.g = gF2Matrix;
    }

    public BCMcElieceCCA2PublicKey(McElieceCCA2PublicKeyParameters mcElieceCCA2PublicKeyParameters) {
        this(mcElieceCCA2PublicKeyParameters.getOIDString(), mcElieceCCA2PublicKeyParameters.getN(), mcElieceCCA2PublicKeyParameters.getT(), mcElieceCCA2PublicKeyParameters.getMatrixG());
        this.McElieceCCA2Params = mcElieceCCA2PublicKeyParameters.getParameters();
    }

    public BCMcElieceCCA2PublicKey(McElieceCCA2PublicKeySpec mcElieceCCA2PublicKeySpec) {
        this(mcElieceCCA2PublicKeySpec.getOIDString(), mcElieceCCA2PublicKeySpec.getN(), mcElieceCCA2PublicKeySpec.getT(), mcElieceCCA2PublicKeySpec.getMatrixG());
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (object == null || !(object instanceof BCMcElieceCCA2PublicKey)) break block2;
                BCMcElieceCCA2PublicKey bCMcElieceCCA2PublicKey = (BCMcElieceCCA2PublicKey)object;
                if (this.n == bCMcElieceCCA2PublicKey.n && this.t == bCMcElieceCCA2PublicKey.t && this.g.equals(bCMcElieceCCA2PublicKey.g)) break block3;
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
        McElieceCCA2PublicKey mcElieceCCA2PublicKey = new McElieceCCA2PublicKey(new ASN1ObjectIdentifier(this.oid), this.n, this.t, this.g);
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(this.getOID(), DERNull.INSTANCE);
        try {
            byte[] arrby = new SubjectPublicKeyInfo(algorithmIdentifier, mcElieceCCA2PublicKey).getEncoded();
            return arrby;
        }
        catch (IOException iOException) {
            return null;
        }
    }

    public String getFormat() {
        return null;
    }

    public GF2Matrix getG() {
        return this.g;
    }

    public int getK() {
        return this.g.getNumRows();
    }

    public McElieceCCA2Parameters getMcElieceCCA2Parameters() {
        return this.McElieceCCA2Params;
    }

    public int getN() {
        return this.n;
    }

    protected ASN1ObjectIdentifier getOID() {
        return new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.4.2");
    }

    public String getOIDString() {
        return this.oid;
    }

    public int getT() {
        return this.t;
    }

    public int hashCode() {
        return this.n + this.t + this.g.hashCode();
    }

    public String toString() {
        String string = "McEliecePublicKey:\n" + " length of the code         : " + this.n + "\n";
        String string2 = string + " error correction capability: " + this.t + "\n";
        return string2 + " generator matrix           : " + this.g.toString();
    }
}

