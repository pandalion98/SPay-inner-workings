/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.ObjectInputStream
 *  java.io.ObjectOutputStream
 *  java.io.OutputStream
 *  java.lang.Object
 *  java.util.Enumeration
 *  java.util.Hashtable
 *  java.util.Vector
 */
package org.bouncycastle.jcajce.provider.asymmetric.util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;

public class PKCS12BagAttributeCarrierImpl
implements PKCS12BagAttributeCarrier {
    private Hashtable pkcs12Attributes;
    private Vector pkcs12Ordering;

    public PKCS12BagAttributeCarrierImpl() {
        this(new Hashtable(), new Vector());
    }

    PKCS12BagAttributeCarrierImpl(Hashtable hashtable, Vector vector) {
        this.pkcs12Attributes = hashtable;
        this.pkcs12Ordering = vector;
    }

    Hashtable getAttributes() {
        return this.pkcs12Attributes;
    }

    @Override
    public ASN1Encodable getBagAttribute(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return (ASN1Encodable)this.pkcs12Attributes.get((Object)aSN1ObjectIdentifier);
    }

    @Override
    public Enumeration getBagAttributeKeys() {
        return this.pkcs12Ordering.elements();
    }

    Vector getOrdering() {
        return this.pkcs12Ordering;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void readObject(ObjectInputStream objectInputStream) {
        Object object = objectInputStream.readObject();
        if (object instanceof Hashtable) {
            this.pkcs12Attributes = (Hashtable)object;
            this.pkcs12Ordering = (Vector)objectInputStream.readObject();
            return;
        } else {
            ASN1ObjectIdentifier aSN1ObjectIdentifier;
            ASN1InputStream aSN1InputStream = new ASN1InputStream((byte[])object);
            while ((aSN1ObjectIdentifier = (ASN1ObjectIdentifier)aSN1InputStream.readObject()) != null) {
                this.setBagAttribute(aSN1ObjectIdentifier, aSN1InputStream.readObject());
            }
        }
    }

    @Override
    public void setBagAttribute(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Encodable aSN1Encodable) {
        if (this.pkcs12Attributes.containsKey((Object)aSN1ObjectIdentifier)) {
            this.pkcs12Attributes.put((Object)aSN1ObjectIdentifier, (Object)aSN1Encodable);
            return;
        }
        this.pkcs12Attributes.put((Object)aSN1ObjectIdentifier, (Object)aSN1Encodable);
        this.pkcs12Ordering.addElement((Object)aSN1ObjectIdentifier);
    }

    int size() {
        return this.pkcs12Ordering.size();
    }

    public void writeObject(ObjectOutputStream objectOutputStream) {
        if (this.pkcs12Ordering.size() == 0) {
            objectOutputStream.writeObject((Object)new Hashtable());
            objectOutputStream.writeObject((Object)new Vector());
            return;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ASN1OutputStream aSN1OutputStream = new ASN1OutputStream((OutputStream)byteArrayOutputStream);
        Enumeration enumeration = this.getBagAttributeKeys();
        while (enumeration.hasMoreElements()) {
            ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)enumeration.nextElement();
            aSN1OutputStream.writeObject(aSN1ObjectIdentifier);
            aSN1OutputStream.writeObject((ASN1Encodable)this.pkcs12Attributes.get((Object)aSN1ObjectIdentifier));
        }
        objectOutputStream.writeObject((Object)byteArrayOutputStream.toByteArray());
    }
}

