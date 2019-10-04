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
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x500.DirectoryString;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.IssuerSerial;

public class ProcurationSyntax
extends ASN1Object {
    private IssuerSerial certRef;
    private String country;
    private GeneralName thirdPerson;
    private DirectoryString typeOfSubstitution;

    public ProcurationSyntax(String string, DirectoryString directoryString, GeneralName generalName) {
        this.country = string;
        this.typeOfSubstitution = directoryString;
        this.thirdPerson = generalName;
        this.certRef = null;
    }

    public ProcurationSyntax(String string, DirectoryString directoryString, IssuerSerial issuerSerial) {
        this.country = string;
        this.typeOfSubstitution = directoryString;
        this.thirdPerson = null;
        this.certRef = issuerSerial;
    }

    private ProcurationSyntax(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() < 1 || aSN1Sequence.size() > 3) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        Enumeration enumeration = aSN1Sequence.getObjects();
        block5 : while (enumeration.hasMoreElements()) {
            ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(enumeration.nextElement());
            switch (aSN1TaggedObject.getTagNo()) {
                default: {
                    throw new IllegalArgumentException("Bad tag number: " + aSN1TaggedObject.getTagNo());
                }
                case 1: {
                    this.country = DERPrintableString.getInstance(aSN1TaggedObject, true).getString();
                    continue block5;
                }
                case 2: {
                    this.typeOfSubstitution = DirectoryString.getInstance(aSN1TaggedObject, true);
                    continue block5;
                }
                case 3: 
            }
            ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
            if (aSN1Primitive instanceof ASN1TaggedObject) {
                this.thirdPerson = GeneralName.getInstance(aSN1Primitive);
                continue;
            }
            this.certRef = IssuerSerial.getInstance(aSN1Primitive);
        }
    }

    public static ProcurationSyntax getInstance(Object object) {
        if (object == null || object instanceof ProcurationSyntax) {
            return (ProcurationSyntax)object;
        }
        if (object instanceof ASN1Sequence) {
            return new ProcurationSyntax((ASN1Sequence)object);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public IssuerSerial getCertRef() {
        return this.certRef;
    }

    public String getCountry() {
        return this.country;
    }

    public GeneralName getThirdPerson() {
        return this.thirdPerson;
    }

    public DirectoryString getTypeOfSubstitution() {
        return this.typeOfSubstitution;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.country != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 1, new DERPrintableString(this.country, true)));
        }
        if (this.typeOfSubstitution != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 2, this.typeOfSubstitution));
        }
        if (this.thirdPerson != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 3, this.thirdPerson));
            do {
                return new DERSequence(aSN1EncodableVector);
                break;
            } while (true);
        }
        aSN1EncodableVector.add(new DERTaggedObject(true, 3, this.certRef));
        return new DERSequence(aSN1EncodableVector);
    }
}

