/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1SequenceParser;
import org.bouncycastle.asn1.ASN1SetParser;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1TaggedObjectParser;
import org.bouncycastle.asn1.cms.ContentInfoParser;
import org.bouncycastle.asn1.cms.OriginatorInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class AuthenticatedDataParser {
    private ASN1Encodable nextObject;
    private boolean originatorInfoCalled;
    private ASN1SequenceParser seq;
    private ASN1Integer version;

    public AuthenticatedDataParser(ASN1SequenceParser aSN1SequenceParser) {
        this.seq = aSN1SequenceParser;
        this.version = ASN1Integer.getInstance(aSN1SequenceParser.readObject());
    }

    public ASN1SetParser getAuthAttrs() {
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        if (this.nextObject instanceof ASN1TaggedObjectParser) {
            ASN1Encodable aSN1Encodable = this.nextObject;
            this.nextObject = null;
            return (ASN1SetParser)((ASN1TaggedObjectParser)aSN1Encodable).getObjectParser(17, false);
        }
        return null;
    }

    public AlgorithmIdentifier getDigestAlgorithm() {
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        if (this.nextObject instanceof ASN1TaggedObjectParser) {
            AlgorithmIdentifier algorithmIdentifier = AlgorithmIdentifier.getInstance((ASN1TaggedObject)this.nextObject.toASN1Primitive(), false);
            this.nextObject = null;
            return algorithmIdentifier;
        }
        return null;
    }

    public ContentInfoParser getEnapsulatedContentInfo() {
        return this.getEncapsulatedContentInfo();
    }

    public ContentInfoParser getEncapsulatedContentInfo() {
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        if (this.nextObject != null) {
            ASN1SequenceParser aSN1SequenceParser = (ASN1SequenceParser)this.nextObject;
            this.nextObject = null;
            return new ContentInfoParser(aSN1SequenceParser);
        }
        return null;
    }

    public ASN1OctetString getMac() {
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        ASN1Encodable aSN1Encodable = this.nextObject;
        this.nextObject = null;
        return ASN1OctetString.getInstance(aSN1Encodable.toASN1Primitive());
    }

    public AlgorithmIdentifier getMacAlgorithm() {
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        if (this.nextObject != null) {
            ASN1SequenceParser aSN1SequenceParser = (ASN1SequenceParser)this.nextObject;
            this.nextObject = null;
            return AlgorithmIdentifier.getInstance(aSN1SequenceParser.toASN1Primitive());
        }
        return null;
    }

    public OriginatorInfo getOriginatorInfo() {
        this.originatorInfoCalled = true;
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        if (this.nextObject instanceof ASN1TaggedObjectParser && ((ASN1TaggedObjectParser)this.nextObject).getTagNo() == 0) {
            ASN1SequenceParser aSN1SequenceParser = (ASN1SequenceParser)((ASN1TaggedObjectParser)this.nextObject).getObjectParser(16, false);
            this.nextObject = null;
            return OriginatorInfo.getInstance(aSN1SequenceParser.toASN1Primitive());
        }
        return null;
    }

    public ASN1SetParser getRecipientInfos() {
        if (!this.originatorInfoCalled) {
            this.getOriginatorInfo();
        }
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        ASN1SetParser aSN1SetParser = (ASN1SetParser)this.nextObject;
        this.nextObject = null;
        return aSN1SetParser;
    }

    public ASN1SetParser getUnauthAttrs() {
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        if (this.nextObject != null) {
            ASN1Encodable aSN1Encodable = this.nextObject;
            this.nextObject = null;
            return (ASN1SetParser)((ASN1TaggedObjectParser)aSN1Encodable).getObjectParser(17, false);
        }
        return null;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }
}

