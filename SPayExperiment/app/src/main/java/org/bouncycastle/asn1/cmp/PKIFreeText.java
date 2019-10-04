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
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERUTF8String;

public class PKIFreeText
extends ASN1Object {
    ASN1Sequence strings;

    public PKIFreeText(String string) {
        this(new DERUTF8String(string));
    }

    private PKIFreeText(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        while (enumeration.hasMoreElements()) {
            if (enumeration.nextElement() instanceof DERUTF8String) continue;
            throw new IllegalArgumentException("attempt to insert non UTF8 STRING into PKIFreeText");
        }
        this.strings = aSN1Sequence;
    }

    public PKIFreeText(DERUTF8String dERUTF8String) {
        this.strings = new DERSequence(dERUTF8String);
    }

    public PKIFreeText(String[] arrstring) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            aSN1EncodableVector.add(new DERUTF8String(arrstring[i2]));
        }
        this.strings = new DERSequence(aSN1EncodableVector);
    }

    public PKIFreeText(DERUTF8String[] arrdERUTF8String) {
        this.strings = new DERSequence(arrdERUTF8String);
    }

    public static PKIFreeText getInstance(Object object) {
        if (object instanceof PKIFreeText) {
            return (PKIFreeText)object;
        }
        if (object != null) {
            return new PKIFreeText(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static PKIFreeText getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return PKIFreeText.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public DERUTF8String getStringAt(int n2) {
        return (DERUTF8String)this.strings.getObjectAt(n2);
    }

    public int size() {
        return this.strings.size();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.strings;
    }
}

