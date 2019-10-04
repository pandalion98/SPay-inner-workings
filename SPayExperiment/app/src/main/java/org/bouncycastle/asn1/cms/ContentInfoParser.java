/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1SequenceParser;
import org.bouncycastle.asn1.ASN1TaggedObjectParser;

public class ContentInfoParser {
    private ASN1TaggedObjectParser content;
    private ASN1ObjectIdentifier contentType;

    public ContentInfoParser(ASN1SequenceParser aSN1SequenceParser) {
        this.contentType = (ASN1ObjectIdentifier)aSN1SequenceParser.readObject();
        this.content = (ASN1TaggedObjectParser)aSN1SequenceParser.readObject();
    }

    public ASN1Encodable getContent(int n2) {
        if (this.content != null) {
            return this.content.getObjectParser(n2, true);
        }
        return null;
    }

    public ASN1ObjectIdentifier getContentType() {
        return this.contentType;
    }
}

