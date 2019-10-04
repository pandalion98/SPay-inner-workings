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
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

public class ECNamedCurveTable {
    public static Enumeration getNames() {
        return org.bouncycastle.asn1.x9.ECNamedCurveTable.getNames();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ECNamedCurveParameterSpec getParameterSpec(String string) {
        X9ECParameters x9ECParameters;
        block6 : {
            X9ECParameters x9ECParameters2;
            block7 : {
                x9ECParameters2 = CustomNamedCurves.getByName(string);
                if (x9ECParameters2 != null) break block7;
                try {
                    X9ECParameters x9ECParameters3;
                    x9ECParameters2 = x9ECParameters3 = CustomNamedCurves.getByOID(new ASN1ObjectIdentifier(string));
                }
                catch (IllegalArgumentException illegalArgumentException) {}
                {
                }
                while (x9ECParameters2 == null && (x9ECParameters2 = org.bouncycastle.asn1.x9.ECNamedCurveTable.getByName(string)) == null) {
                    try {
                        X9ECParameters x9ECParameters4;
                        x9ECParameters = x9ECParameters4 = org.bouncycastle.asn1.x9.ECNamedCurveTable.getByOID(new ASN1ObjectIdentifier(string));
                    }
                    catch (IllegalArgumentException illegalArgumentException) {
                        x9ECParameters = x9ECParameters2;
                    }
                    break block6;
                }
            }
            x9ECParameters = x9ECParameters2;
        }
        if (x9ECParameters == null) {
            return null;
        }
        return new ECNamedCurveParameterSpec(string, x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH(), x9ECParameters.getSeed());
    }
}

