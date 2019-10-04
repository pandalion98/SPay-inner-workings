/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 *  java.util.Vector
 */
package org.bouncycastle.asn1.x9;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle.asn1.x9.X962NamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;

public class ECNamedCurveTable {
    private static void addEnumeration(Vector vector, Enumeration enumeration) {
        while (enumeration.hasMoreElements()) {
            vector.addElement(enumeration.nextElement());
        }
    }

    public static X9ECParameters getByName(String string) {
        X9ECParameters x9ECParameters = X962NamedCurves.getByName(string);
        if (x9ECParameters == null) {
            x9ECParameters = SECNamedCurves.getByName(string);
        }
        if (x9ECParameters == null) {
            x9ECParameters = TeleTrusTNamedCurves.getByName(string);
        }
        if (x9ECParameters == null) {
            x9ECParameters = NISTNamedCurves.getByName(string);
        }
        return x9ECParameters;
    }

    public static X9ECParameters getByOID(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        X9ECParameters x9ECParameters = X962NamedCurves.getByOID(aSN1ObjectIdentifier);
        if (x9ECParameters == null) {
            x9ECParameters = SECNamedCurves.getByOID(aSN1ObjectIdentifier);
        }
        if (x9ECParameters == null) {
            x9ECParameters = TeleTrusTNamedCurves.getByOID(aSN1ObjectIdentifier);
        }
        return x9ECParameters;
    }

    public static Enumeration getNames() {
        Vector vector = new Vector();
        ECNamedCurveTable.addEnumeration(vector, X962NamedCurves.getNames());
        ECNamedCurveTable.addEnumeration(vector, SECNamedCurves.getNames());
        ECNamedCurveTable.addEnumeration(vector, NISTNamedCurves.getNames());
        ECNamedCurveTable.addEnumeration(vector, TeleTrusTNamedCurves.getNames());
        return vector.elements();
    }

    public static ASN1ObjectIdentifier getOID(String string) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = X962NamedCurves.getOID(string);
        if (aSN1ObjectIdentifier == null) {
            aSN1ObjectIdentifier = SECNamedCurves.getOID(string);
        }
        if (aSN1ObjectIdentifier == null) {
            aSN1ObjectIdentifier = TeleTrusTNamedCurves.getOID(string);
        }
        if (aSN1ObjectIdentifier == null) {
            aSN1ObjectIdentifier = NISTNamedCurves.getOID(string);
        }
        return aSN1ObjectIdentifier;
    }
}

