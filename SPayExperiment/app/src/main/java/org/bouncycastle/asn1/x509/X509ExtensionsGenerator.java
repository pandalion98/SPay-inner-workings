/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Hashtable
 *  java.util.Vector
 */
package org.bouncycastle.asn1.x509;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;

public class X509ExtensionsGenerator {
    private Vector extOrdering = new Vector();
    private Hashtable extensions = new Hashtable();

    public void addExtension(ASN1ObjectIdentifier aSN1ObjectIdentifier, boolean bl, ASN1Encodable aSN1Encodable) {
        try {
            this.addExtension(aSN1ObjectIdentifier, bl, aSN1Encodable.toASN1Primitive().getEncoded("DER"));
            return;
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("error encoding value: " + (Object)((Object)iOException));
        }
    }

    public void addExtension(ASN1ObjectIdentifier aSN1ObjectIdentifier, boolean bl, byte[] arrby) {
        if (this.extensions.containsKey((Object)aSN1ObjectIdentifier)) {
            throw new IllegalArgumentException("extension " + aSN1ObjectIdentifier + " already added");
        }
        this.extOrdering.addElement((Object)aSN1ObjectIdentifier);
        this.extensions.put((Object)aSN1ObjectIdentifier, (Object)new X509Extension(bl, (ASN1OctetString)new DEROctetString(arrby)));
    }

    public X509Extensions generate() {
        return new X509Extensions(this.extOrdering, this.extensions);
    }

    public boolean isEmpty() {
        return this.extOrdering.isEmpty();
    }

    public void reset() {
        this.extensions = new Hashtable();
        this.extOrdering = new Vector();
    }
}

