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
import org.bouncycastle.pqc.asn1.McEliecePublicKey;
import org.bouncycastle.pqc.crypto.mceliece.McElieceParameters;
import org.bouncycastle.pqc.crypto.mceliece.McEliecePublicKeyParameters;
import org.bouncycastle.pqc.jcajce.spec.McEliecePublicKeySpec;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;

public class BCMcEliecePublicKey
implements PublicKey,
CipherParameters {
    private static final long serialVersionUID = 1L;
    private McElieceParameters McElieceParams;
    private GF2Matrix g;
    private int n;
    private String oid;
    private int t;

    public BCMcEliecePublicKey(String string, int n, int n2, GF2Matrix gF2Matrix) {
        this.oid = string;
        this.n = n;
        this.t = n2;
        this.g = gF2Matrix;
    }

    public BCMcEliecePublicKey(McEliecePublicKeyParameters mcEliecePublicKeyParameters) {
        this(mcEliecePublicKeyParameters.getOIDString(), mcEliecePublicKeyParameters.getN(), mcEliecePublicKeyParameters.getT(), mcEliecePublicKeyParameters.getG());
        this.McElieceParams = mcEliecePublicKeyParameters.getParameters();
    }

    public BCMcEliecePublicKey(McEliecePublicKeySpec mcEliecePublicKeySpec) {
        this(mcEliecePublicKeySpec.getOIDString(), mcEliecePublicKeySpec.getN(), mcEliecePublicKeySpec.getT(), mcEliecePublicKeySpec.getG());
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (!(object instanceof BCMcEliecePublicKey)) break block2;
                BCMcEliecePublicKey bCMcEliecePublicKey = (BCMcEliecePublicKey)object;
                if (this.n == bCMcEliecePublicKey.n && this.t == bCMcEliecePublicKey.t && this.g.equals(bCMcEliecePublicKey.g)) break block3;
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
        McEliecePublicKey mcEliecePublicKey = new McEliecePublicKey(new ASN1ObjectIdentifier(this.oid), this.n, this.t, this.g);
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(this.getOID(), DERNull.INSTANCE);
        try {
            byte[] arrby = new SubjectPublicKeyInfo(algorithmIdentifier, mcEliecePublicKey).getEncoded();
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

    public McElieceParameters getMcElieceParameters() {
        return this.McElieceParams;
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

