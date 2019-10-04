/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.cmp;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.cmp.PKIFreeText;
import org.bouncycastle.asn1.cmp.PKIStatusInfo;

public class ErrorMsgContent
extends ASN1Object {
    private ASN1Integer errorCode;
    private PKIFreeText errorDetails;
    private PKIStatusInfo pkiStatusInfo;

    private ErrorMsgContent(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.pkiStatusInfo = PKIStatusInfo.getInstance(enumeration.nextElement());
        while (enumeration.hasMoreElements()) {
            Object object = enumeration.nextElement();
            if (object instanceof ASN1Integer) {
                this.errorCode = ASN1Integer.getInstance(object);
                continue;
            }
            this.errorDetails = PKIFreeText.getInstance(object);
        }
    }

    public ErrorMsgContent(PKIStatusInfo pKIStatusInfo) {
        this(pKIStatusInfo, null, null);
    }

    public ErrorMsgContent(PKIStatusInfo pKIStatusInfo, ASN1Integer aSN1Integer, PKIFreeText pKIFreeText) {
        if (pKIStatusInfo == null) {
            throw new IllegalArgumentException("'pkiStatusInfo' cannot be null");
        }
        this.pkiStatusInfo = pKIStatusInfo;
        this.errorCode = aSN1Integer;
        this.errorDetails = pKIFreeText;
    }

    private void addOptional(ASN1EncodableVector aSN1EncodableVector, ASN1Encodable aSN1Encodable) {
        if (aSN1Encodable != null) {
            aSN1EncodableVector.add(aSN1Encodable);
        }
    }

    public static ErrorMsgContent getInstance(Object object) {
        if (object instanceof ErrorMsgContent) {
            return (ErrorMsgContent)object;
        }
        if (object != null) {
            return new ErrorMsgContent(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public ASN1Integer getErrorCode() {
        return this.errorCode;
    }

    public PKIFreeText getErrorDetails() {
        return this.errorDetails;
    }

    public PKIStatusInfo getPKIStatusInfo() {
        return this.pkiStatusInfo;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.pkiStatusInfo);
        this.addOptional(aSN1EncodableVector, this.errorCode);
        this.addOptional(aSN1EncodableVector, this.errorDetails);
        return new DERSequence(aSN1EncodableVector);
    }
}

