/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 *  java.util.Vector
 */
package org.bouncycastle.asn1.x509;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.x509.GeneralNames;

public class IetfAttrSyntax
extends ASN1Object {
    public static final int VALUE_OCTETS = 1;
    public static final int VALUE_OID = 2;
    public static final int VALUE_UTF8 = 3;
    GeneralNames policyAuthority = null;
    int valueChoice = -1;
    Vector values = new Vector();

    /*
     * Enabled aggressive block sorting
     */
    private IetfAttrSyntax(ASN1Sequence aSN1Sequence) {
        int n2;
        if (aSN1Sequence.getObjectAt(0) instanceof ASN1TaggedObject) {
            this.policyAuthority = GeneralNames.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(0), false);
            n2 = 1;
        } else if (aSN1Sequence.size() == 2) {
            this.policyAuthority = GeneralNames.getInstance(aSN1Sequence.getObjectAt(0));
            n2 = 1;
        } else {
            n2 = 0;
        }
        if (!(aSN1Sequence.getObjectAt(n2) instanceof ASN1Sequence)) {
            throw new IllegalArgumentException("Non-IetfAttrSyntax encoding");
        }
        Enumeration enumeration = ((ASN1Sequence)aSN1Sequence.getObjectAt(n2)).getObjects();
        while (enumeration.hasMoreElements()) {
            int n3;
            ASN1Primitive aSN1Primitive = (ASN1Primitive)enumeration.nextElement();
            if (aSN1Primitive instanceof ASN1ObjectIdentifier) {
                n3 = 2;
            } else if (aSN1Primitive instanceof DERUTF8String) {
                n3 = 3;
            } else {
                if (!(aSN1Primitive instanceof DEROctetString)) {
                    throw new IllegalArgumentException("Bad value type encoding IetfAttrSyntax");
                }
                n3 = 1;
            }
            if (this.valueChoice < 0) {
                this.valueChoice = n3;
            }
            if (n3 != this.valueChoice) {
                throw new IllegalArgumentException("Mix of value types in IetfAttrSyntax");
            }
            this.values.addElement((Object)aSN1Primitive);
        }
        return;
    }

    public static IetfAttrSyntax getInstance(Object object) {
        if (object instanceof IetfAttrSyntax) {
            return (IetfAttrSyntax)object;
        }
        if (object != null) {
            return new IetfAttrSyntax(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public GeneralNames getPolicyAuthority() {
        return this.policyAuthority;
    }

    public int getValueType() {
        return this.valueChoice;
    }

    public Object[] getValues() {
        if (this.getValueType() == 1) {
            Object[] arrobject = new ASN1OctetString[this.values.size()];
            for (int i2 = 0; i2 != arrobject.length; ++i2) {
                arrobject[i2] = (ASN1OctetString)this.values.elementAt(i2);
            }
            return arrobject;
        }
        if (this.getValueType() == 2) {
            Object[] arrobject = new ASN1ObjectIdentifier[this.values.size()];
            for (int i3 = 0; i3 != arrobject.length; ++i3) {
                arrobject[i3] = (ASN1ObjectIdentifier)this.values.elementAt(i3);
            }
            return arrobject;
        }
        Object[] arrobject = new DERUTF8String[this.values.size()];
        for (int i4 = 0; i4 != arrobject.length; ++i4) {
            arrobject[i4] = (DERUTF8String)this.values.elementAt(i4);
        }
        return arrobject;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.policyAuthority != null) {
            aSN1EncodableVector.add(new DERTaggedObject(0, this.policyAuthority));
        }
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        Enumeration enumeration = this.values.elements();
        while (enumeration.hasMoreElements()) {
            aSN1EncodableVector2.add((ASN1Encodable)enumeration.nextElement());
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        return new DERSequence(aSN1EncodableVector);
    }
}

