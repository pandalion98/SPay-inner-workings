/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Enumeration
 *  java.util.Vector
 */
package org.bouncycastle.asn1.x509;

import java.math.BigInteger;
import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.DisplayText;

public class NoticeReference
extends ASN1Object {
    private ASN1Sequence noticeNumbers;
    private DisplayText organization;

    public NoticeReference(String string, Vector vector) {
        this(string, NoticeReference.convertVector(vector));
    }

    public NoticeReference(String string, ASN1EncodableVector aSN1EncodableVector) {
        this(new DisplayText(string), aSN1EncodableVector);
    }

    private NoticeReference(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        this.organization = DisplayText.getInstance(aSN1Sequence.getObjectAt(0));
        this.noticeNumbers = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(1));
    }

    public NoticeReference(DisplayText displayText, ASN1EncodableVector aSN1EncodableVector) {
        this.organization = displayText;
        this.noticeNumbers = new DERSequence(aSN1EncodableVector);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static ASN1EncodableVector convertVector(Vector vector) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        Enumeration enumeration = vector.elements();
        while (enumeration.hasMoreElements()) {
            ASN1Integer aSN1Integer;
            Object object = enumeration.nextElement();
            if (object instanceof BigInteger) {
                aSN1Integer = new ASN1Integer((BigInteger)object);
            } else {
                if (!(object instanceof Integer)) {
                    throw new IllegalArgumentException();
                }
                aSN1Integer = new ASN1Integer(((Integer)object).intValue());
            }
            aSN1EncodableVector.add(aSN1Integer);
        }
        return aSN1EncodableVector;
    }

    public static NoticeReference getInstance(Object object) {
        if (object instanceof NoticeReference) {
            return (NoticeReference)object;
        }
        if (object != null) {
            return new NoticeReference(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public ASN1Integer[] getNoticeNumbers() {
        ASN1Integer[] arraSN1Integer = new ASN1Integer[this.noticeNumbers.size()];
        for (int i2 = 0; i2 != this.noticeNumbers.size(); ++i2) {
            arraSN1Integer[i2] = ASN1Integer.getInstance(this.noticeNumbers.getObjectAt(i2));
        }
        return arraSN1Integer;
    }

    public DisplayText getOrganization() {
        return this.organization;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.organization);
        aSN1EncodableVector.add(this.noticeNumbers);
        return new DERSequence(aSN1EncodableVector);
    }
}

