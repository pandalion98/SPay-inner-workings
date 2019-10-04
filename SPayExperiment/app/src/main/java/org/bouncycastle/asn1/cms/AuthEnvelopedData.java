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
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cms.EncryptedContentInfo;
import org.bouncycastle.asn1.cms.OriginatorInfo;

public class AuthEnvelopedData
extends ASN1Object {
    private ASN1Set authAttrs;
    private EncryptedContentInfo authEncryptedContentInfo;
    private ASN1OctetString mac;
    private OriginatorInfo originatorInfo;
    private ASN1Set recipientInfos;
    private ASN1Set unauthAttrs;
    private ASN1Integer version;

    /*
     * Enabled aggressive block sorting
     */
    public AuthEnvelopedData(ASN1Sequence aSN1Sequence) {
        int n2;
        int n3;
        this.version = (ASN1Integer)aSN1Sequence.getObjectAt(0).toASN1Primitive();
        ASN1Primitive aSN1Primitive = aSN1Sequence.getObjectAt(1).toASN1Primitive();
        if (aSN1Primitive instanceof ASN1TaggedObject) {
            this.originatorInfo = OriginatorInfo.getInstance((ASN1TaggedObject)aSN1Primitive, false);
            n3 = 3;
            aSN1Primitive = aSN1Sequence.getObjectAt(2).toASN1Primitive();
        } else {
            n3 = 2;
        }
        this.recipientInfos = ASN1Set.getInstance(aSN1Primitive);
        int n4 = n3 + 1;
        this.authEncryptedContentInfo = EncryptedContentInfo.getInstance(aSN1Sequence.getObjectAt(n3).toASN1Primitive());
        int n5 = n4 + 1;
        ASN1Primitive aSN1Primitive2 = aSN1Sequence.getObjectAt(n4).toASN1Primitive();
        if (aSN1Primitive2 instanceof ASN1TaggedObject) {
            this.authAttrs = ASN1Set.getInstance((ASN1TaggedObject)aSN1Primitive2, false);
            n2 = n5 + 1;
            aSN1Primitive2 = aSN1Sequence.getObjectAt(n5).toASN1Primitive();
        } else {
            n2 = n5;
        }
        this.mac = ASN1OctetString.getInstance(aSN1Primitive2);
        if (aSN1Sequence.size() > n2) {
            n2 + 1;
            this.unauthAttrs = ASN1Set.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(n2).toASN1Primitive(), false);
        }
    }

    public AuthEnvelopedData(OriginatorInfo originatorInfo, ASN1Set aSN1Set, EncryptedContentInfo encryptedContentInfo, ASN1Set aSN1Set2, ASN1OctetString aSN1OctetString, ASN1Set aSN1Set3) {
        this.version = new ASN1Integer(0L);
        this.originatorInfo = originatorInfo;
        this.recipientInfos = aSN1Set;
        this.authEncryptedContentInfo = encryptedContentInfo;
        this.authAttrs = aSN1Set2;
        this.mac = aSN1OctetString;
        this.unauthAttrs = aSN1Set3;
    }

    public static AuthEnvelopedData getInstance(Object object) {
        if (object == null || object instanceof AuthEnvelopedData) {
            return (AuthEnvelopedData)object;
        }
        if (object instanceof ASN1Sequence) {
            return new AuthEnvelopedData((ASN1Sequence)object);
        }
        throw new IllegalArgumentException("Invalid AuthEnvelopedData: " + object.getClass().getName());
    }

    public static AuthEnvelopedData getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return AuthEnvelopedData.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public ASN1Set getAuthAttrs() {
        return this.authAttrs;
    }

    public EncryptedContentInfo getAuthEncryptedContentInfo() {
        return this.authEncryptedContentInfo;
    }

    public ASN1OctetString getMac() {
        return this.mac;
    }

    public OriginatorInfo getOriginatorInfo() {
        return this.originatorInfo;
    }

    public ASN1Set getRecipientInfos() {
        return this.recipientInfos;
    }

    public ASN1Set getUnauthAttrs() {
        return this.unauthAttrs;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.version);
        if (this.originatorInfo != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.originatorInfo));
        }
        aSN1EncodableVector.add(this.recipientInfos);
        aSN1EncodableVector.add(this.authEncryptedContentInfo);
        if (this.authAttrs != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.authAttrs));
        }
        aSN1EncodableVector.add(this.mac);
        if (this.unauthAttrs != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 2, this.unauthAttrs));
        }
        return new BERSequence(aSN1EncodableVector);
    }
}

