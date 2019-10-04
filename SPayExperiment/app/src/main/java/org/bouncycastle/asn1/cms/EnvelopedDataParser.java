/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1SequenceParser;
import org.bouncycastle.asn1.ASN1SetParser;
import org.bouncycastle.asn1.ASN1TaggedObjectParser;
import org.bouncycastle.asn1.cms.EncryptedContentInfoParser;
import org.bouncycastle.asn1.cms.OriginatorInfo;

public class EnvelopedDataParser {
    private ASN1Encodable _nextObject;
    private boolean _originatorInfoCalled;
    private ASN1SequenceParser _seq;
    private ASN1Integer _version;

    public EnvelopedDataParser(ASN1SequenceParser aSN1SequenceParser) {
        this._seq = aSN1SequenceParser;
        this._version = ASN1Integer.getInstance(aSN1SequenceParser.readObject());
    }

    public EncryptedContentInfoParser getEncryptedContentInfo() {
        if (this._nextObject == null) {
            this._nextObject = this._seq.readObject();
        }
        if (this._nextObject != null) {
            ASN1SequenceParser aSN1SequenceParser = (ASN1SequenceParser)this._nextObject;
            this._nextObject = null;
            return new EncryptedContentInfoParser(aSN1SequenceParser);
        }
        return null;
    }

    public OriginatorInfo getOriginatorInfo() {
        this._originatorInfoCalled = true;
        if (this._nextObject == null) {
            this._nextObject = this._seq.readObject();
        }
        if (this._nextObject instanceof ASN1TaggedObjectParser && ((ASN1TaggedObjectParser)this._nextObject).getTagNo() == 0) {
            ASN1SequenceParser aSN1SequenceParser = (ASN1SequenceParser)((ASN1TaggedObjectParser)this._nextObject).getObjectParser(16, false);
            this._nextObject = null;
            return OriginatorInfo.getInstance(aSN1SequenceParser.toASN1Primitive());
        }
        return null;
    }

    public ASN1SetParser getRecipientInfos() {
        if (!this._originatorInfoCalled) {
            this.getOriginatorInfo();
        }
        if (this._nextObject == null) {
            this._nextObject = this._seq.readObject();
        }
        ASN1SetParser aSN1SetParser = (ASN1SetParser)this._nextObject;
        this._nextObject = null;
        return aSN1SetParser;
    }

    public ASN1SetParser getUnprotectedAttrs() {
        if (this._nextObject == null) {
            this._nextObject = this._seq.readObject();
        }
        if (this._nextObject != null) {
            ASN1Encodable aSN1Encodable = this._nextObject;
            this._nextObject = null;
            return (ASN1SetParser)((ASN1TaggedObjectParser)aSN1Encodable).getObjectParser(17, false);
        }
        return null;
    }

    public ASN1Integer getVersion() {
        return this._version;
    }
}

