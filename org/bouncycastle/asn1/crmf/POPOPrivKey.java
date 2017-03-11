package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cms.EnvelopedData;
import org.bouncycastle.math.ec.ECCurve;

public class POPOPrivKey extends ASN1Object implements ASN1Choice {
    public static final int agreeMAC = 3;
    public static final int dhMAC = 2;
    public static final int encryptedKey = 4;
    public static final int subsequentMessage = 1;
    public static final int thisMessage = 0;
    private ASN1Encodable obj;
    private int tagNo;

    private POPOPrivKey(ASN1TaggedObject aSN1TaggedObject) {
        this.tagNo = aSN1TaggedObject.getTagNo();
        switch (this.tagNo) {
            case ECCurve.COORD_AFFINE /*0*/:
                this.obj = DERBitString.getInstance(aSN1TaggedObject, false);
            case subsequentMessage /*1*/:
                this.obj = SubsequentMessage.valueOf(ASN1Integer.getInstance(aSN1TaggedObject, false).getValue().intValue());
            case dhMAC /*2*/:
                this.obj = DERBitString.getInstance(aSN1TaggedObject, false);
            case agreeMAC /*3*/:
                this.obj = PKMACValue.getInstance(aSN1TaggedObject, false);
            case encryptedKey /*4*/:
                this.obj = EnvelopedData.getInstance(aSN1TaggedObject, false);
            default:
                throw new IllegalArgumentException("unknown tag in POPOPrivKey");
        }
    }

    public POPOPrivKey(SubsequentMessage subsequentMessage) {
        this.tagNo = subsequentMessage;
        this.obj = subsequentMessage;
    }

    public static POPOPrivKey getInstance(Object obj) {
        return obj instanceof POPOPrivKey ? (POPOPrivKey) obj : obj != null ? new POPOPrivKey(ASN1TaggedObject.getInstance(obj)) : null;
    }

    public static POPOPrivKey getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1TaggedObject.getInstance(aSN1TaggedObject, z));
    }

    public int getType() {
        return this.tagNo;
    }

    public ASN1Encodable getValue() {
        return this.obj;
    }

    public ASN1Primitive toASN1Primitive() {
        return new DERTaggedObject(false, this.tagNo, this.obj);
    }
}
