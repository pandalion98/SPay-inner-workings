/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.esf;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.Attribute;
import org.bouncycastle.asn1.x509.AttributeCertificate;

public class SignerAttribute
extends ASN1Object {
    private Object[] values;

    /*
     * Enabled aggressive block sorting
     */
    private SignerAttribute(ASN1Sequence aSN1Sequence) {
        this.values = new Object[aSN1Sequence.size()];
        Enumeration enumeration = aSN1Sequence.getObjects();
        int n2 = 0;
        while (enumeration.hasMoreElements()) {
            ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(enumeration.nextElement());
            if (aSN1TaggedObject.getTagNo() == 0) {
                ASN1Sequence aSN1Sequence2 = ASN1Sequence.getInstance(aSN1TaggedObject, true);
                Attribute[] arrattribute = new Attribute[aSN1Sequence2.size()];
                for (int i2 = 0; i2 != arrattribute.length; ++i2) {
                    arrattribute[i2] = Attribute.getInstance(aSN1Sequence2.getObjectAt(i2));
                }
                this.values[n2] = arrattribute;
            } else {
                if (aSN1TaggedObject.getTagNo() != 1) {
                    throw new IllegalArgumentException("illegal tag: " + aSN1TaggedObject.getTagNo());
                }
                this.values[n2] = AttributeCertificate.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, true));
            }
            ++n2;
        }
        return;
    }

    public SignerAttribute(AttributeCertificate attributeCertificate) {
        this.values = new Object[1];
        this.values[0] = attributeCertificate;
    }

    public SignerAttribute(Attribute[] arrattribute) {
        this.values = new Object[1];
        this.values[0] = arrattribute;
    }

    public static SignerAttribute getInstance(Object object) {
        if (object instanceof SignerAttribute) {
            return (SignerAttribute)object;
        }
        if (object != null) {
            return new SignerAttribute(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public Object[] getValues() {
        return this.values;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        int n2 = 0;
        while (n2 != this.values.length) {
            if (this.values[n2] instanceof Attribute[]) {
                aSN1EncodableVector.add(new DERTaggedObject(0, new DERSequence((Attribute[])this.values[n2])));
            } else {
                aSN1EncodableVector.add(new DERTaggedObject(1, (AttributeCertificate)this.values[n2]));
            }
            ++n2;
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

