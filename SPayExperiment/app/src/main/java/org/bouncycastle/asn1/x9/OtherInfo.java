/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.x9;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x9.KeySpecificInfo;

public class OtherInfo
extends ASN1Object {
    private KeySpecificInfo keyInfo;
    private ASN1OctetString partyAInfo;
    private ASN1OctetString suppPubInfo;

    public OtherInfo(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.keyInfo = new KeySpecificInfo((ASN1Sequence)enumeration.nextElement());
        while (enumeration.hasMoreElements()) {
            DERTaggedObject dERTaggedObject = (DERTaggedObject)enumeration.nextElement();
            if (dERTaggedObject.getTagNo() == 0) {
                this.partyAInfo = (ASN1OctetString)dERTaggedObject.getObject();
                continue;
            }
            if (dERTaggedObject.getTagNo() != 2) continue;
            this.suppPubInfo = (ASN1OctetString)dERTaggedObject.getObject();
        }
    }

    public OtherInfo(KeySpecificInfo keySpecificInfo, ASN1OctetString aSN1OctetString, ASN1OctetString aSN1OctetString2) {
        this.keyInfo = keySpecificInfo;
        this.partyAInfo = aSN1OctetString;
        this.suppPubInfo = aSN1OctetString2;
    }

    public KeySpecificInfo getKeyInfo() {
        return this.keyInfo;
    }

    public ASN1OctetString getPartyAInfo() {
        return this.partyAInfo;
    }

    public ASN1OctetString getSuppPubInfo() {
        return this.suppPubInfo;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.keyInfo);
        if (this.partyAInfo != null) {
            aSN1EncodableVector.add(new DERTaggedObject(0, this.partyAInfo));
        }
        aSN1EncodableVector.add(new DERTaggedObject(2, this.suppPubInfo));
        return new DERSequence(aSN1EncodableVector);
    }
}

