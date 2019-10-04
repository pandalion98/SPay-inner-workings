/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.cmp;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cmp.CMPCertificate;
import org.bouncycastle.asn1.cmp.PKIBody;
import org.bouncycastle.asn1.cmp.PKIHeader;

public class PKIMessage
extends ASN1Object {
    private PKIBody body;
    private ASN1Sequence extraCerts;
    private PKIHeader header;
    private DERBitString protection;

    private PKIMessage(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.header = PKIHeader.getInstance(enumeration.nextElement());
        this.body = PKIBody.getInstance(enumeration.nextElement());
        while (enumeration.hasMoreElements()) {
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject)enumeration.nextElement();
            if (aSN1TaggedObject.getTagNo() == 0) {
                this.protection = DERBitString.getInstance(aSN1TaggedObject, true);
                continue;
            }
            this.extraCerts = ASN1Sequence.getInstance(aSN1TaggedObject, true);
        }
    }

    public PKIMessage(PKIHeader pKIHeader, PKIBody pKIBody) {
        this(pKIHeader, pKIBody, null, null);
    }

    public PKIMessage(PKIHeader pKIHeader, PKIBody pKIBody, DERBitString dERBitString) {
        this(pKIHeader, pKIBody, dERBitString, null);
    }

    public PKIMessage(PKIHeader pKIHeader, PKIBody pKIBody, DERBitString dERBitString, CMPCertificate[] arrcMPCertificate) {
        this.header = pKIHeader;
        this.body = pKIBody;
        this.protection = dERBitString;
        if (arrcMPCertificate != null) {
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            for (int i2 = 0; i2 < arrcMPCertificate.length; ++i2) {
                aSN1EncodableVector.add(arrcMPCertificate[i2]);
            }
            this.extraCerts = new DERSequence(aSN1EncodableVector);
        }
    }

    private void addOptional(ASN1EncodableVector aSN1EncodableVector, int n2, ASN1Encodable aSN1Encodable) {
        if (aSN1Encodable != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, n2, aSN1Encodable));
        }
    }

    public static PKIMessage getInstance(Object object) {
        if (object instanceof PKIMessage) {
            return (PKIMessage)object;
        }
        if (object != null) {
            return new PKIMessage(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public PKIBody getBody() {
        return this.body;
    }

    public CMPCertificate[] getExtraCerts() {
        if (this.extraCerts == null) {
            return null;
        }
        CMPCertificate[] arrcMPCertificate = new CMPCertificate[this.extraCerts.size()];
        for (int i2 = 0; i2 < arrcMPCertificate.length; ++i2) {
            arrcMPCertificate[i2] = CMPCertificate.getInstance(this.extraCerts.getObjectAt(i2));
        }
        return arrcMPCertificate;
    }

    public PKIHeader getHeader() {
        return this.header;
    }

    public DERBitString getProtection() {
        return this.protection;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.header);
        aSN1EncodableVector.add(this.body);
        this.addOptional(aSN1EncodableVector, 0, this.protection);
        this.addOptional(aSN1EncodableVector, 1, this.extraCerts);
        return new DERSequence(aSN1EncodableVector);
    }
}

