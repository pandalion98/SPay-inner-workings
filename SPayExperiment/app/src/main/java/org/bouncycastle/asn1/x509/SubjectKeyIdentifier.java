/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;

public class SubjectKeyIdentifier
extends ASN1Object {
    private byte[] keyidentifier;

    protected SubjectKeyIdentifier(ASN1OctetString aSN1OctetString) {
        this.keyidentifier = aSN1OctetString.getOctets();
    }

    public SubjectKeyIdentifier(byte[] arrby) {
        this.keyidentifier = arrby;
    }

    public static SubjectKeyIdentifier fromExtensions(Extensions extensions) {
        return SubjectKeyIdentifier.getInstance(extensions.getExtensionParsedValue(Extension.subjectKeyIdentifier));
    }

    public static SubjectKeyIdentifier getInstance(Object object) {
        if (object instanceof SubjectKeyIdentifier) {
            return (SubjectKeyIdentifier)object;
        }
        if (object != null) {
            return new SubjectKeyIdentifier(ASN1OctetString.getInstance(object));
        }
        return null;
    }

    public static SubjectKeyIdentifier getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return SubjectKeyIdentifier.getInstance(ASN1OctetString.getInstance(aSN1TaggedObject, bl));
    }

    public byte[] getKeyIdentifier() {
        return this.keyidentifier;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DEROctetString(this.keyidentifier);
    }
}

