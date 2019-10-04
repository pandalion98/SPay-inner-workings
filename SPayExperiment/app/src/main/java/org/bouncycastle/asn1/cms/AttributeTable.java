/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Enumeration
 *  java.util.Hashtable
 *  java.util.Vector
 */
package org.bouncycastle.asn1.cms;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.Attributes;

public class AttributeTable {
    private Hashtable attributes = new Hashtable();

    public AttributeTable(Hashtable hashtable) {
        this.attributes = this.copyTable(hashtable);
    }

    public AttributeTable(ASN1EncodableVector aSN1EncodableVector) {
        for (int i2 = 0; i2 != aSN1EncodableVector.size(); ++i2) {
            Attribute attribute = Attribute.getInstance(aSN1EncodableVector.get(i2));
            this.addAttribute(attribute.getAttrType(), attribute);
        }
    }

    public AttributeTable(ASN1Set aSN1Set) {
        for (int i2 = 0; i2 != aSN1Set.size(); ++i2) {
            Attribute attribute = Attribute.getInstance(aSN1Set.getObjectAt(i2));
            this.addAttribute(attribute.getAttrType(), attribute);
        }
    }

    public AttributeTable(Attribute attribute) {
        this.addAttribute(attribute.getAttrType(), attribute);
    }

    public AttributeTable(Attributes attributes) {
        this(ASN1Set.getInstance(attributes.toASN1Primitive()));
    }

    /*
     * Enabled aggressive block sorting
     */
    private void addAttribute(ASN1ObjectIdentifier aSN1ObjectIdentifier, Attribute attribute) {
        Vector vector;
        Object object = this.attributes.get((Object)aSN1ObjectIdentifier);
        if (object == null) {
            this.attributes.put((Object)aSN1ObjectIdentifier, (Object)attribute);
            return;
        }
        if (object instanceof Attribute) {
            Vector vector2 = new Vector();
            vector2.addElement(object);
            vector2.addElement((Object)attribute);
            vector = vector2;
        } else {
            vector = (Vector)object;
            vector.addElement((Object)attribute);
        }
        this.attributes.put((Object)aSN1ObjectIdentifier, (Object)vector);
    }

    private Hashtable copyTable(Hashtable hashtable) {
        Hashtable hashtable2 = new Hashtable();
        Enumeration enumeration = hashtable.keys();
        while (enumeration.hasMoreElements()) {
            Object object = enumeration.nextElement();
            hashtable2.put(object, hashtable.get(object));
        }
        return hashtable2;
    }

    public AttributeTable add(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Encodable aSN1Encodable) {
        AttributeTable attributeTable = new AttributeTable(this.attributes);
        attributeTable.addAttribute(aSN1ObjectIdentifier, new Attribute(aSN1ObjectIdentifier, new DERSet(aSN1Encodable)));
        return attributeTable;
    }

    public Attribute get(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        Object object = this.attributes.get((Object)aSN1ObjectIdentifier);
        if (object instanceof Vector) {
            return (Attribute)((Vector)object).elementAt(0);
        }
        return (Attribute)object;
    }

    public ASN1EncodableVector getAll(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        Object object = this.attributes.get((Object)aSN1ObjectIdentifier);
        if (object instanceof Vector) {
            Enumeration enumeration = ((Vector)object).elements();
            while (enumeration.hasMoreElements()) {
                aSN1EncodableVector.add((Attribute)enumeration.nextElement());
            }
        } else if (object != null) {
            aSN1EncodableVector.add((Attribute)object);
        }
        return aSN1EncodableVector;
    }

    public AttributeTable remove(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        AttributeTable attributeTable = new AttributeTable(this.attributes);
        attributeTable.attributes.remove((Object)aSN1ObjectIdentifier);
        return attributeTable;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int size() {
        Enumeration enumeration = this.attributes.elements();
        int n2 = 0;
        while (enumeration.hasMoreElements()) {
            Object object = enumeration.nextElement();
            int n3 = object instanceof Vector ? n2 + ((Vector)object).size() : n2 + 1;
            n2 = n3;
        }
        return n2;
    }

    public ASN1EncodableVector toASN1EncodableVector() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        Enumeration enumeration = this.attributes.elements();
        while (enumeration.hasMoreElements()) {
            Object object = enumeration.nextElement();
            if (object instanceof Vector) {
                Enumeration enumeration2 = ((Vector)object).elements();
                while (enumeration2.hasMoreElements()) {
                    aSN1EncodableVector.add(Attribute.getInstance(enumeration2.nextElement()));
                }
                continue;
            }
            aSN1EncodableVector.add(Attribute.getInstance(object));
        }
        return aSN1EncodableVector;
    }

    public Attributes toASN1Structure() {
        return new Attributes(this.toASN1EncodableVector());
    }

    public Hashtable toHashtable() {
        return this.copyTable(this.attributes);
    }
}

