/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.dvcs;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;

public class ServiceType
extends ASN1Object {
    public static final ServiceType CCPD;
    public static final ServiceType CPD;
    public static final ServiceType VPKC;
    public static final ServiceType VSD;
    private ASN1Enumerated value;

    static {
        CPD = new ServiceType(1);
        VSD = new ServiceType(2);
        VPKC = new ServiceType(3);
        CCPD = new ServiceType(4);
    }

    public ServiceType(int n2) {
        this.value = new ASN1Enumerated(n2);
    }

    private ServiceType(ASN1Enumerated aSN1Enumerated) {
        this.value = aSN1Enumerated;
    }

    public static ServiceType getInstance(Object object) {
        if (object instanceof ServiceType) {
            return (ServiceType)object;
        }
        if (object != null) {
            return new ServiceType(ASN1Enumerated.getInstance(object));
        }
        return null;
    }

    public static ServiceType getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return ServiceType.getInstance(ASN1Enumerated.getInstance(aSN1TaggedObject, bl));
    }

    public BigInteger getValue() {
        return this.value.getValue();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.value;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String toString() {
        String string;
        int n2 = this.value.getValue().intValue();
        StringBuilder stringBuilder = new StringBuilder().append("").append(n2);
        if (n2 == CPD.getValue().intValue()) {
            string = "(CPD)";
            do {
                return stringBuilder.append(string).toString();
                break;
            } while (true);
        }
        if (n2 == VSD.getValue().intValue()) {
            string = "(VSD)";
            return stringBuilder.append(string).toString();
        }
        if (n2 == VPKC.getValue().intValue()) {
            string = "(VPKC)";
            return stringBuilder.append(string).toString();
        }
        if (n2 == CCPD.getValue().intValue()) {
            string = "(CCPD)";
            return stringBuilder.append(string).toString();
        }
        string = "?";
        return stringBuilder.append(string).toString();
    }
}

