/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.x509.qualified;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.GeneralName;

public class SemanticsInformation
extends ASN1Object {
    private GeneralName[] nameRegistrationAuthorities;
    private ASN1ObjectIdentifier semanticsIdentifier;

    public SemanticsInformation(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this.semanticsIdentifier = aSN1ObjectIdentifier;
        this.nameRegistrationAuthorities = null;
    }

    public SemanticsInformation(ASN1ObjectIdentifier aSN1ObjectIdentifier, GeneralName[] arrgeneralName) {
        this.semanticsIdentifier = aSN1ObjectIdentifier;
        this.nameRegistrationAuthorities = arrgeneralName;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private SemanticsInformation(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        if (aSN1Sequence.size() < 1) {
            throw new IllegalArgumentException("no objects in SemanticsInformation");
        }
        Object object = enumeration.nextElement();
        if (object instanceof ASN1ObjectIdentifier) {
            this.semanticsIdentifier = ASN1ObjectIdentifier.getInstance(object);
            if (!enumeration.hasMoreElements()) return;
            object = enumeration.nextElement();
        }
        if (object == null) return;
        ASN1Sequence aSN1Sequence2 = ASN1Sequence.getInstance(object);
        this.nameRegistrationAuthorities = new GeneralName[aSN1Sequence2.size()];
        int n2 = 0;
        while (n2 < aSN1Sequence2.size()) {
            this.nameRegistrationAuthorities[n2] = GeneralName.getInstance(aSN1Sequence2.getObjectAt(n2));
            ++n2;
        }
    }

    public SemanticsInformation(GeneralName[] arrgeneralName) {
        this.semanticsIdentifier = null;
        this.nameRegistrationAuthorities = arrgeneralName;
    }

    public static SemanticsInformation getInstance(Object object) {
        if (object instanceof SemanticsInformation) {
            return (SemanticsInformation)object;
        }
        if (object != null) {
            return new SemanticsInformation(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public GeneralName[] getNameRegistrationAuthorities() {
        return this.nameRegistrationAuthorities;
    }

    public ASN1ObjectIdentifier getSemanticsIdentifier() {
        return this.semanticsIdentifier;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.semanticsIdentifier != null) {
            aSN1EncodableVector.add(this.semanticsIdentifier);
        }
        if (this.nameRegistrationAuthorities != null) {
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            for (int i2 = 0; i2 < this.nameRegistrationAuthorities.length; ++i2) {
                aSN1EncodableVector2.add(this.nameRegistrationAuthorities[i2]);
            }
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

