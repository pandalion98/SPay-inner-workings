/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Hashtable
 *  java.util.Vector
 */
package org.bouncycastle.asn1.x509;

import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;

public class ExtensionsGenerator {
    private Vector extOrdering = new Vector();
    private Hashtable extensions = new Hashtable();

    public void addExtension(ASN1ObjectIdentifier aSN1ObjectIdentifier, boolean bl, ASN1Encodable aSN1Encodable) {
        this.addExtension(aSN1ObjectIdentifier, bl, aSN1Encodable.toASN1Primitive().getEncoded("DER"));
    }

    public void addExtension(ASN1ObjectIdentifier aSN1ObjectIdentifier, boolean bl, byte[] arrby) {
        if (this.extensions.containsKey((Object)aSN1ObjectIdentifier)) {
            throw new IllegalArgumentException("extension " + aSN1ObjectIdentifier + " already added");
        }
        this.extOrdering.addElement((Object)aSN1ObjectIdentifier);
        this.extensions.put((Object)aSN1ObjectIdentifier, (Object)new Extension(aSN1ObjectIdentifier, bl, (ASN1OctetString)new DEROctetString(arrby)));
    }

    public Extensions generate() {
        Extension[] arrextension = new Extension[this.extOrdering.size()];
        for (int i2 = 0; i2 != this.extOrdering.size(); ++i2) {
            arrextension[i2] = (Extension)this.extensions.get(this.extOrdering.elementAt(i2));
        }
        return new Extensions(arrextension);
    }

    public boolean isEmpty() {
        return this.extOrdering.isEmpty();
    }

    public void reset() {
        this.extensions = new Hashtable();
        this.extOrdering = new Vector();
    }
}

