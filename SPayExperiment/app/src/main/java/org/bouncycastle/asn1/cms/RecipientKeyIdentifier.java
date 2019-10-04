/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
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

public class RecipientKeyIdentifier
extends ASN1Object {
    private ASN1GeneralizedTime date;
    private OtherKeyAttribute other;
    private ASN1OctetString subjectKeyIdentifier;

    public RecipientKeyIdentifier(ASN1OctetString aSN1OctetString, ASN1GeneralizedTime aSN1GeneralizedTime, OtherKeyAttribute otherKeyAttribute) {
        this.subjectKeyIdentifier = aSN1OctetString;
        this.date = aSN1GeneralizedTime;
        this.other = otherKeyAttribute;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public RecipientKeyIdentifier(ASN1Sequence var1_1) {
        super();
        this.subjectKeyIdentifier = ASN1OctetString.getInstance(var1_1.getObjectAt(0));
        switch (var1_1.size()) {
            default: {
                throw new IllegalArgumentException("Invalid RecipientKeyIdentifier");
            }
            case 2: {
                if (!(var1_1.getObjectAt(1) instanceof ASN1GeneralizedTime)) ** GOTO lbl11
                this.date = ASN1GeneralizedTime.getInstance(var1_1.getObjectAt(1));
            }
            case 1: {
                return;
            }
lbl11: // 1 sources:
            this.other = OtherKeyAttribute.getInstance(var1_1.getObjectAt(2));
            return;
            case 3: 
        }
        this.date = ASN1GeneralizedTime.getInstance(var1_1.getObjectAt(1));
        this.other = OtherKeyAttribute.getInstance(var1_1.getObjectAt(2));
    }

    public RecipientKeyIdentifier(byte[] arrby) {
        this(arrby, null, null);
    }

    public RecipientKeyIdentifier(byte[] arrby, ASN1GeneralizedTime aSN1GeneralizedTime, OtherKeyAttribute otherKeyAttribute) {
        this.subjectKeyIdentifier = new DEROctetString(arrby);
        this.date = aSN1GeneralizedTime;
        this.other = otherKeyAttribute;
    }

    public static RecipientKeyIdentifier getInstance(Object object) {
        if (object instanceof RecipientKeyIdentifier) {
            return (RecipientKeyIdentifier)object;
        }
        if (object != null) {
            return new RecipientKeyIdentifier(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static RecipientKeyIdentifier getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return RecipientKeyIdentifier.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public ASN1GeneralizedTime getDate() {
        return this.date;
    }

    public OtherKeyAttribute getOtherKeyAttribute() {
        return this.other;
    }

    public ASN1OctetString getSubjectKeyIdentifier() {
        return this.subjectKeyIdentifier;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.subjectKeyIdentifier);
        if (this.date != null) {
            aSN1EncodableVector.add(this.date);
        }
        if (this.other != null) {
            aSN1EncodableVector.add(this.other);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

