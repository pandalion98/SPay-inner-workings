/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.x509.qualified;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;

public class TypeOfBiometricData
extends ASN1Object
implements ASN1Choice {
    public static final int HANDWRITTEN_SIGNATURE = 1;
    public static final int PICTURE;
    ASN1Encodable obj;

    public TypeOfBiometricData(int n2) {
        if (n2 == 0 || n2 == 1) {
            this.obj = new ASN1Integer(n2);
            return;
        }
        throw new IllegalArgumentException("unknow PredefinedBiometricType : " + n2);
    }

    public TypeOfBiometricData(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this.obj = aSN1ObjectIdentifier;
    }

    public static TypeOfBiometricData getInstance(Object object) {
        if (object == null || object instanceof TypeOfBiometricData) {
            return (TypeOfBiometricData)object;
        }
        if (object instanceof ASN1Integer) {
            return new TypeOfBiometricData(ASN1Integer.getInstance(object).getValue().intValue());
        }
        if (object instanceof ASN1ObjectIdentifier) {
            return new TypeOfBiometricData(ASN1ObjectIdentifier.getInstance(object));
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }

    public ASN1ObjectIdentifier getBiometricDataOid() {
        return (ASN1ObjectIdentifier)this.obj;
    }

    public int getPredefinedBiometricType() {
        return ((ASN1Integer)this.obj).getValue().intValue();
    }

    public boolean isPredefined() {
        return this.obj instanceof ASN1Integer;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.obj.toASN1Primitive();
    }
}

