/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.jce.spec;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.cryptopro.GOST3410NamedParameters;
import org.bouncycastle.asn1.cryptopro.GOST3410ParamSetParameters;
import org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
import org.bouncycastle.jce.interfaces.GOST3410Params;
import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;

public class GOST3410ParameterSpec
implements AlgorithmParameterSpec,
GOST3410Params {
    private String digestParamSetOID;
    private String encryptionParamSetOID;
    private String keyParamSetOID;
    private GOST3410PublicKeyParameterSetSpec keyParameters;

    public GOST3410ParameterSpec(String string) {
        this(string, CryptoProObjectIdentifiers.gostR3411_94_CryptoProParamSet.getId(), null);
    }

    public GOST3410ParameterSpec(String string, String string2) {
        this(string, string2, null);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public GOST3410ParameterSpec(String string, String string2, String string3) {
        GOST3410ParamSetParameters gOST3410ParamSetParameters;
        block3 : {
            try {
                GOST3410ParamSetParameters gOST3410ParamSetParameters2;
                gOST3410ParamSetParameters = gOST3410ParamSetParameters2 = GOST3410NamedParameters.getByOID(new ASN1ObjectIdentifier(string));
            }
            catch (IllegalArgumentException illegalArgumentException) {
                ASN1ObjectIdentifier aSN1ObjectIdentifier = GOST3410NamedParameters.getOID(string);
                gOST3410ParamSetParameters = null;
                if (aSN1ObjectIdentifier == null) break block3;
                string = aSN1ObjectIdentifier.getId();
                gOST3410ParamSetParameters = GOST3410NamedParameters.getByOID(aSN1ObjectIdentifier);
            }
        }
        if (gOST3410ParamSetParameters == null) {
            throw new IllegalArgumentException("no key parameter set for passed in name/OID.");
        }
        this.keyParameters = new GOST3410PublicKeyParameterSetSpec(gOST3410ParamSetParameters.getP(), gOST3410ParamSetParameters.getQ(), gOST3410ParamSetParameters.getA());
        this.keyParamSetOID = string;
        this.digestParamSetOID = string2;
        this.encryptionParamSetOID = string3;
    }

    public GOST3410ParameterSpec(GOST3410PublicKeyParameterSetSpec gOST3410PublicKeyParameterSetSpec) {
        this.keyParameters = gOST3410PublicKeyParameterSetSpec;
        this.digestParamSetOID = CryptoProObjectIdentifiers.gostR3411_94_CryptoProParamSet.getId();
        this.encryptionParamSetOID = null;
    }

    public static GOST3410ParameterSpec fromPublicKeyAlg(GOST3410PublicKeyAlgParameters gOST3410PublicKeyAlgParameters) {
        if (gOST3410PublicKeyAlgParameters.getEncryptionParamSet() != null) {
            return new GOST3410ParameterSpec(gOST3410PublicKeyAlgParameters.getPublicKeyParamSet().getId(), gOST3410PublicKeyAlgParameters.getDigestParamSet().getId(), gOST3410PublicKeyAlgParameters.getEncryptionParamSet().getId());
        }
        return new GOST3410ParameterSpec(gOST3410PublicKeyAlgParameters.getPublicKeyParamSet().getId(), gOST3410PublicKeyAlgParameters.getDigestParamSet().getId());
    }

    public boolean equals(Object object) {
        boolean bl;
        block2 : {
            block3 : {
                boolean bl2 = object instanceof GOST3410ParameterSpec;
                bl = false;
                if (!bl2) break block2;
                GOST3410ParameterSpec gOST3410ParameterSpec = (GOST3410ParameterSpec)object;
                boolean bl3 = this.keyParameters.equals(gOST3410ParameterSpec.keyParameters);
                bl = false;
                if (!bl3) break block2;
                boolean bl4 = this.digestParamSetOID.equals((Object)gOST3410ParameterSpec.digestParamSetOID);
                bl = false;
                if (!bl4) break block2;
                if (this.encryptionParamSetOID == gOST3410ParameterSpec.encryptionParamSetOID) break block3;
                String string = this.encryptionParamSetOID;
                bl = false;
                if (string == null) break block2;
                boolean bl5 = this.encryptionParamSetOID.equals((Object)gOST3410ParameterSpec.encryptionParamSetOID);
                bl = false;
                if (!bl5) break block2;
            }
            bl = true;
        }
        return bl;
    }

    @Override
    public String getDigestParamSetOID() {
        return this.digestParamSetOID;
    }

    @Override
    public String getEncryptionParamSetOID() {
        return this.encryptionParamSetOID;
    }

    @Override
    public String getPublicKeyParamSetOID() {
        return this.keyParamSetOID;
    }

    @Override
    public GOST3410PublicKeyParameterSetSpec getPublicKeyParameters() {
        return this.keyParameters;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int hashCode() {
        int n;
        int n2 = this.keyParameters.hashCode() ^ this.digestParamSetOID.hashCode();
        if (this.encryptionParamSetOID != null) {
            n = this.encryptionParamSetOID.hashCode();
            do {
                return n ^ n2;
                break;
            } while (true);
        }
        n = 0;
        return n ^ n2;
    }
}

