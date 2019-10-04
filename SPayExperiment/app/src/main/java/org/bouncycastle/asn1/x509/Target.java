/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.GeneralName;

public class Target
extends ASN1Object
implements ASN1Choice {
    public static final int targetGroup = 1;
    public static final int targetName;
    private GeneralName targGroup;
    private GeneralName targName;

    public Target(int n2, GeneralName generalName) {
        this(new DERTaggedObject(n2, generalName));
    }

    private Target(ASN1TaggedObject aSN1TaggedObject) {
        switch (aSN1TaggedObject.getTagNo()) {
            default: {
                throw new IllegalArgumentException("unknown tag: " + aSN1TaggedObject.getTagNo());
            }
            case 0: {
                this.targName = GeneralName.getInstance(aSN1TaggedObject, true);
                return;
            }
            case 1: 
        }
        this.targGroup = GeneralName.getInstance(aSN1TaggedObject, true);
    }

    public static Target getInstance(Object object) {
        if (object == null || object instanceof Target) {
            return (Target)object;
        }
        if (object instanceof ASN1TaggedObject) {
            return new Target((ASN1TaggedObject)object);
        }
        throw new IllegalArgumentException("unknown object in factory: " + (Object)object.getClass());
    }

    public GeneralName getTargetGroup() {
        return this.targGroup;
    }

    public GeneralName getTargetName() {
        return this.targName;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.targName != null) {
            return new DERTaggedObject(true, 0, this.targName);
        }
        return new DERTaggedObject(true, 1, this.targGroup);
    }
}

