/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.isismtt.x509;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.isismtt.x509.Admissions;
import org.bouncycastle.asn1.x509.GeneralName;

public class AdmissionSyntax
extends ASN1Object {
    private GeneralName admissionAuthority;
    private ASN1Sequence contentsOfAdmissions;

    private AdmissionSyntax(ASN1Sequence aSN1Sequence) {
        switch (aSN1Sequence.size()) {
            default: {
                throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
            }
            case 1: {
                this.contentsOfAdmissions = DERSequence.getInstance(aSN1Sequence.getObjectAt(0));
                return;
            }
            case 2: 
        }
        this.admissionAuthority = GeneralName.getInstance(aSN1Sequence.getObjectAt(0));
        this.contentsOfAdmissions = DERSequence.getInstance(aSN1Sequence.getObjectAt(1));
    }

    public AdmissionSyntax(GeneralName generalName, ASN1Sequence aSN1Sequence) {
        this.admissionAuthority = generalName;
        this.contentsOfAdmissions = aSN1Sequence;
    }

    public static AdmissionSyntax getInstance(Object object) {
        if (object == null || object instanceof AdmissionSyntax) {
            return (AdmissionSyntax)object;
        }
        if (object instanceof ASN1Sequence) {
            return new AdmissionSyntax((ASN1Sequence)object);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public GeneralName getAdmissionAuthority() {
        return this.admissionAuthority;
    }

    public Admissions[] getContentsOfAdmissions() {
        Admissions[] arradmissions = new Admissions[this.contentsOfAdmissions.size()];
        int n2 = 0;
        Enumeration enumeration = this.contentsOfAdmissions.getObjects();
        while (enumeration.hasMoreElements()) {
            int n3 = n2 + 1;
            arradmissions[n2] = Admissions.getInstance(enumeration.nextElement());
            n2 = n3;
        }
        return arradmissions;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.admissionAuthority != null) {
            aSN1EncodableVector.add(this.admissionAuthority);
        }
        aSN1EncodableVector.add(this.contentsOfAdmissions);
        return new DERSequence(aSN1EncodableVector);
    }
}

