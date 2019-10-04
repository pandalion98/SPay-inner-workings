/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Enumeration
 */
package org.bouncycastle.jce;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

public class ECGOST3410NamedCurveTable {
    public static Enumeration getNames() {
        return ECGOST3410NamedCurves.getNames();
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static ECNamedCurveParameterSpec getParameterSpec(String string) {
        ECDomainParameters eCDomainParameters = ECGOST3410NamedCurves.getByName(string);
        if (eCDomainParameters == null) {
            ECDomainParameters eCDomainParameters2;
            eCDomainParameters = eCDomainParameters2 = ECGOST3410NamedCurves.getByOID(new ASN1ObjectIdentifier(string));
        }
        if (eCDomainParameters != null) return new ECNamedCurveParameterSpec(string, eCDomainParameters.getCurve(), eCDomainParameters.getG(), eCDomainParameters.getN(), eCDomainParameters.getH(), eCDomainParameters.getSeed());
        return null;
        catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }
}

