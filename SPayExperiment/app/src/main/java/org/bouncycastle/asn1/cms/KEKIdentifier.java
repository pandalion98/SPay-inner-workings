/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.cms.OtherKeyAttribute;

public class KEKIdentifier
extends ASN1Object {
    private ASN1GeneralizedTime date;
    private ASN1OctetString keyIdentifier;
    private OtherKeyAttribute other;

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private KEKIdentifier(ASN1Sequence var1_1) {
        super();
        this.keyIdentifier = (ASN1OctetString)var1_1.getObjectAt(0);
        switch (var1_1.size()) {
            default: {
                throw new IllegalArgumentException("Invalid KEKIdentifier");
            }
            case 2: {
                if (!(var1_1.getObjectAt(1) instanceof ASN1GeneralizedTime)) ** GOTO lbl11
                this.date = (ASN1GeneralizedTime)var1_1.getObjectAt(1);
            }
            case 1: {
                return;
            }
lbl11: // 1 sources:
            this.other = OtherKeyAttribute.getInstance(var1_1.getObjectAt(1));
            return;
            case 3: 
        }
        this.date = (ASN1GeneralizedTime)var1_1.getObjectAt(1);
        this.other = OtherKeyAttribute.getInstance(var1_1.getObjectAt(2));
    }

    public KEKIdentifier(byte[] arrby, ASN1GeneralizedTime aSN1GeneralizedTime, OtherKeyAttribute otherKeyAttribute) {
        this.keyIdentifier = new DEROctetString(arrby);
        this.date = aSN1GeneralizedTime;
        this.other = otherKeyAttribute;
    }

    public static KEKIdentifier getInstance(Object object) {
        if (object == null || object instanceof KEKIdentifier) {
            return (KEKIdentifier)object;
        }
        if (object instanceof ASN1Sequence) {
            return new KEKIdentifier((ASN1Sequence)object);
        }
        throw new IllegalArgumentException("Invalid KEKIdentifier: " + object.getClass().getName());
    }

    public static KEKIdentifier getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return KEKIdentifier.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public ASN1GeneralizedTime getDate() {
        return this.date;
    }

    public ASN1OctetString getKeyIdentifier() {
        return this.keyIdentifier;
    }

    public OtherKeyAttribute getOther() {
        return this.other;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.keyIdentifier);
        if (this.date != null) {
            aSN1EncodableVector.add(this.date);
        }
        if (this.other != null) {
            aSN1EncodableVector.add(this.other);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

