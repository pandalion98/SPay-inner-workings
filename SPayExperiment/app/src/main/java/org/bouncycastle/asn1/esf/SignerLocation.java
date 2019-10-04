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
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.x500.DirectoryString;

public class SignerLocation
extends ASN1Object {
    private DERUTF8String countryName;
    private DERUTF8String localityName;
    private ASN1Sequence postalAddress;

    /*
     * Enabled aggressive block sorting
     */
    private SignerLocation(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        block5 : do {
            if (!enumeration.hasMoreElements()) {
                return;
            }
            DERTaggedObject dERTaggedObject = (DERTaggedObject)enumeration.nextElement();
            switch (dERTaggedObject.getTagNo()) {
                default: {
                    throw new IllegalArgumentException("illegal tag");
                }
                case 0: {
                    this.countryName = new DERUTF8String(DirectoryString.getInstance(dERTaggedObject, true).getString());
                    continue block5;
                }
                case 1: {
                    this.localityName = new DERUTF8String(DirectoryString.getInstance(dERTaggedObject, true).getString());
                    continue block5;
                }
                case 2: 
            }
            this.postalAddress = dERTaggedObject.isExplicit() ? ASN1Sequence.getInstance(dERTaggedObject, true) : ASN1Sequence.getInstance(dERTaggedObject, false);
            if (this.postalAddress != null && this.postalAddress.size() > 6) break;
        } while (true);
        throw new IllegalArgumentException("postal address must contain less than 6 strings");
    }

    public SignerLocation(DERUTF8String dERUTF8String, DERUTF8String dERUTF8String2, ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence != null && aSN1Sequence.size() > 6) {
            throw new IllegalArgumentException("postal address must contain less than 6 strings");
        }
        if (dERUTF8String != null) {
            this.countryName = DERUTF8String.getInstance(dERUTF8String.toASN1Primitive());
        }
        if (dERUTF8String2 != null) {
            this.localityName = DERUTF8String.getInstance(dERUTF8String2.toASN1Primitive());
        }
        if (aSN1Sequence != null) {
            this.postalAddress = ASN1Sequence.getInstance(aSN1Sequence.toASN1Primitive());
        }
    }

    public static SignerLocation getInstance(Object object) {
        if (object == null || object instanceof SignerLocation) {
            return (SignerLocation)object;
        }
        return new SignerLocation(ASN1Sequence.getInstance(object));
    }

    public DERUTF8String getCountryName() {
        return this.countryName;
    }

    public DERUTF8String getLocalityName() {
        return this.localityName;
    }

    public ASN1Sequence getPostalAddress() {
        return this.postalAddress;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.countryName != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, this.countryName));
        }
        if (this.localityName != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 1, this.localityName));
        }
        if (this.postalAddress != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 2, this.postalAddress));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

