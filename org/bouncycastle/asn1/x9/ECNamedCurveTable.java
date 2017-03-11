package org.bouncycastle.asn1.x9;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;

public class ECNamedCurveTable {
    private static void addEnumeration(Vector vector, Enumeration enumeration) {
        while (enumeration.hasMoreElements()) {
            vector.addElement(enumeration.nextElement());
        }
    }

    public static X9ECParameters getByName(String str) {
        X9ECParameters byName = X962NamedCurves.getByName(str);
        if (byName == null) {
            byName = SECNamedCurves.getByName(str);
        }
        if (byName == null) {
            byName = TeleTrusTNamedCurves.getByName(str);
        }
        return byName == null ? NISTNamedCurves.getByName(str) : byName;
    }

    public static X9ECParameters getByOID(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        X9ECParameters byOID = X962NamedCurves.getByOID(aSN1ObjectIdentifier);
        if (byOID == null) {
            byOID = SECNamedCurves.getByOID(aSN1ObjectIdentifier);
        }
        return byOID == null ? TeleTrusTNamedCurves.getByOID(aSN1ObjectIdentifier) : byOID;
    }

    public static Enumeration getNames() {
        Vector vector = new Vector();
        addEnumeration(vector, X962NamedCurves.getNames());
        addEnumeration(vector, SECNamedCurves.getNames());
        addEnumeration(vector, NISTNamedCurves.getNames());
        addEnumeration(vector, TeleTrusTNamedCurves.getNames());
        return vector.elements();
    }

    public static ASN1ObjectIdentifier getOID(String str) {
        ASN1ObjectIdentifier oid = X962NamedCurves.getOID(str);
        if (oid == null) {
            oid = SECNamedCurves.getOID(str);
        }
        if (oid == null) {
            oid = TeleTrusTNamedCurves.getOID(str);
        }
        return oid == null ? NISTNamedCurves.getOID(str) : oid;
    }
}
