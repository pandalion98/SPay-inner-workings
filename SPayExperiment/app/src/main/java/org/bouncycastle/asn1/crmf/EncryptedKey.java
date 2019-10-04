/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cms.EnvelopedData;
import org.bouncycastle.asn1.crmf.EncryptedValue;

public class EncryptedKey
extends ASN1Object
implements ASN1Choice {
    private EncryptedValue encryptedValue;
    private EnvelopedData envelopedData;

    public EncryptedKey(EnvelopedData envelopedData) {
        this.envelopedData = envelopedData;
    }

    public EncryptedKey(EncryptedValue encryptedValue) {
        this.encryptedValue = encryptedValue;
    }

    public static EncryptedKey getInstance(Object object) {
        if (object instanceof EncryptedKey) {
            return (EncryptedKey)object;
        }
        if (object instanceof ASN1TaggedObject) {
            return new EncryptedKey(EnvelopedData.getInstance((ASN1TaggedObject)object, false));
        }
        if (object instanceof EncryptedValue) {
            return new EncryptedKey((EncryptedValue)object);
        }
        return new EncryptedKey(EncryptedValue.getInstance(object));
    }

    public ASN1Encodable getValue() {
        if (this.encryptedValue != null) {
            return this.encryptedValue;
        }
        return this.envelopedData;
    }

    public boolean isEncryptedValue() {
        return this.encryptedValue != null;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.encryptedValue != null) {
            return this.encryptedValue.toASN1Primitive();
        }
        return new DERTaggedObject(false, 0, this.envelopedData);
    }
}

