/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ocsp.RevokedInfo;

public class CertStatus
extends ASN1Object
implements ASN1Choice {
    private int tagNo;
    private ASN1Encodable value;

    public CertStatus() {
        this.tagNo = 0;
        this.value = DERNull.INSTANCE;
    }

    public CertStatus(int n2, ASN1Encodable aSN1Encodable) {
        this.tagNo = n2;
        this.value = aSN1Encodable;
    }

    public CertStatus(ASN1TaggedObject aSN1TaggedObject) {
        this.tagNo = aSN1TaggedObject.getTagNo();
        switch (aSN1TaggedObject.getTagNo()) {
            default: {
                return;
            }
            case 0: {
                this.value = DERNull.INSTANCE;
                return;
            }
            case 1: {
                this.value = RevokedInfo.getInstance(aSN1TaggedObject, false);
                return;
            }
            case 2: 
        }
        this.value = DERNull.INSTANCE;
    }

    public CertStatus(RevokedInfo revokedInfo) {
        this.tagNo = 1;
        this.value = revokedInfo;
    }

    public static CertStatus getInstance(Object object) {
        if (object == null || object instanceof CertStatus) {
            return (CertStatus)object;
        }
        if (object instanceof ASN1TaggedObject) {
            return new CertStatus((ASN1TaggedObject)object);
        }
        throw new IllegalArgumentException("unknown object in factory: " + object.getClass().getName());
    }

    public static CertStatus getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return CertStatus.getInstance(aSN1TaggedObject.getObject());
    }

    public ASN1Encodable getStatus() {
        return this.value;
    }

    public int getTagNo() {
        return this.tagNo;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERTaggedObject(false, this.tagNo, this.value);
    }
}

