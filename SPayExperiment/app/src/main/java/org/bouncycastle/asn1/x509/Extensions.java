/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Enumeration
 *  java.util.Hashtable
 *  java.util.Vector
 */
package org.bouncycastle.asn1.x509;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.Extension;

public class Extensions
extends ASN1Object {
    private Hashtable extensions = new Hashtable();
    private Vector ordering = new Vector();

    private Extensions(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        while (enumeration.hasMoreElements()) {
            Extension extension = Extension.getInstance(enumeration.nextElement());
            this.extensions.put((Object)extension.getExtnId(), (Object)extension);
            this.ordering.addElement((Object)extension.getExtnId());
        }
    }

    public Extensions(Extension extension) {
        this.ordering.addElement((Object)extension.getExtnId());
        this.extensions.put((Object)extension.getExtnId(), (Object)extension);
    }

    public Extensions(Extension[] arrextension) {
        for (int i2 = 0; i2 != arrextension.length; ++i2) {
            Extension extension = arrextension[i2];
            this.ordering.addElement((Object)extension.getExtnId());
            this.extensions.put((Object)extension.getExtnId(), (Object)extension);
        }
    }

    private ASN1ObjectIdentifier[] getExtensionOIDs(boolean bl) {
        Vector vector = new Vector();
        for (int i2 = 0; i2 != this.ordering.size(); ++i2) {
            Object object = this.ordering.elementAt(i2);
            if (((Extension)this.extensions.get(object)).isCritical() != bl) continue;
            vector.addElement(object);
        }
        return this.toOidArray(vector);
    }

    public static Extensions getInstance(Object object) {
        if (object instanceof Extensions) {
            return (Extensions)object;
        }
        if (object != null) {
            return new Extensions(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static Extensions getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return Extensions.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    private ASN1ObjectIdentifier[] toOidArray(Vector vector) {
        ASN1ObjectIdentifier[] arraSN1ObjectIdentifier = new ASN1ObjectIdentifier[vector.size()];
        for (int i2 = 0; i2 != arraSN1ObjectIdentifier.length; ++i2) {
            arraSN1ObjectIdentifier[i2] = (ASN1ObjectIdentifier)vector.elementAt(i2);
        }
        return arraSN1ObjectIdentifier;
    }

    public boolean equivalent(Extensions extensions) {
        if (this.extensions.size() != extensions.extensions.size()) {
            return false;
        }
        Enumeration enumeration = this.extensions.keys();
        while (enumeration.hasMoreElements()) {
            Object object = enumeration.nextElement();
            if (this.extensions.get(object).equals(extensions.extensions.get(object))) continue;
            return false;
        }
        return true;
    }

    public ASN1ObjectIdentifier[] getCriticalExtensionOIDs() {
        return this.getExtensionOIDs(true);
    }

    public Extension getExtension(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return (Extension)this.extensions.get((Object)aSN1ObjectIdentifier);
    }

    public ASN1ObjectIdentifier[] getExtensionOIDs() {
        return this.toOidArray(this.ordering);
    }

    public ASN1Encodable getExtensionParsedValue(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        Extension extension = this.getExtension(aSN1ObjectIdentifier);
        if (extension != null) {
            return extension.getParsedValue();
        }
        return null;
    }

    public ASN1ObjectIdentifier[] getNonCriticalExtensionOIDs() {
        return this.getExtensionOIDs(false);
    }

    public Enumeration oids() {
        return this.ordering.elements();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        Enumeration enumeration = this.ordering.elements();
        while (enumeration.hasMoreElements()) {
            ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)enumeration.nextElement();
            aSN1EncodableVector.add((Extension)this.extensions.get((Object)aSN1ObjectIdentifier));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

