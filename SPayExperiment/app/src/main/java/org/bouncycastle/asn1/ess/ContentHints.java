/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.ess;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERUTF8String;

public class ContentHints
extends ASN1Object {
    private DERUTF8String contentDescription;
    private ASN1ObjectIdentifier contentType;

    public ContentHints(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this.contentType = aSN1ObjectIdentifier;
        this.contentDescription = null;
    }

    public ContentHints(ASN1ObjectIdentifier aSN1ObjectIdentifier, DERUTF8String dERUTF8String) {
        this.contentType = aSN1ObjectIdentifier;
        this.contentDescription = dERUTF8String;
    }

    private ContentHints(ASN1Sequence aSN1Sequence) {
        ASN1Encodable aSN1Encodable = aSN1Sequence.getObjectAt(0);
        if (aSN1Encodable.toASN1Primitive() instanceof DERUTF8String) {
            this.contentDescription = DERUTF8String.getInstance(aSN1Encodable);
            this.contentType = ASN1ObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(1));
            return;
        }
        this.contentType = ASN1ObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
    }

    public static ContentHints getInstance(Object object) {
        if (object instanceof ContentHints) {
            return (ContentHints)object;
        }
        if (object != null) {
            return new ContentHints(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public DERUTF8String getContentDescription() {
        return this.contentDescription;
    }

    public ASN1ObjectIdentifier getContentType() {
        return this.contentType;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.contentDescription != null) {
            aSN1EncodableVector.add(this.contentDescription);
        }
        aSN1EncodableVector.add(this.contentType);
        return new DERSequence(aSN1EncodableVector);
    }
}

