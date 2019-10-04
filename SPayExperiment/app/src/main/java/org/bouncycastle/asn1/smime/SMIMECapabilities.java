/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 *  java.util.Vector
 */
package org.bouncycastle.asn1.smime;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.smime.SMIMECapability;

public class SMIMECapabilities
extends ASN1Object {
    public static final ASN1ObjectIdentifier canNotDecryptAny;
    public static final ASN1ObjectIdentifier dES_CBC;
    public static final ASN1ObjectIdentifier dES_EDE3_CBC;
    public static final ASN1ObjectIdentifier preferSignedData;
    public static final ASN1ObjectIdentifier rC2_CBC;
    public static final ASN1ObjectIdentifier sMIMECapabilitesVersions;
    private ASN1Sequence capabilities;

    static {
        preferSignedData = PKCSObjectIdentifiers.preferSignedData;
        canNotDecryptAny = PKCSObjectIdentifiers.canNotDecryptAny;
        sMIMECapabilitesVersions = PKCSObjectIdentifiers.sMIMECapabilitiesVersions;
        dES_CBC = new ASN1ObjectIdentifier("1.3.14.3.2.7");
        dES_EDE3_CBC = PKCSObjectIdentifiers.des_EDE3_CBC;
        rC2_CBC = PKCSObjectIdentifiers.RC2_CBC;
    }

    public SMIMECapabilities(ASN1Sequence aSN1Sequence) {
        this.capabilities = aSN1Sequence;
    }

    public static SMIMECapabilities getInstance(Object object) {
        if (object == null || object instanceof SMIMECapabilities) {
            return (SMIMECapabilities)object;
        }
        if (object instanceof ASN1Sequence) {
            return new SMIMECapabilities((ASN1Sequence)object);
        }
        if (object instanceof Attribute) {
            return new SMIMECapabilities((ASN1Sequence)((Attribute)object).getAttrValues().getObjectAt(0));
        }
        throw new IllegalArgumentException("unknown object in factory: " + object.getClass().getName());
    }

    public Vector getCapabilities(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        Vector vector;
        Enumeration enumeration = this.capabilities.getObjects();
        vector = new Vector();
        if (aSN1ObjectIdentifier == null) {
            while (enumeration.hasMoreElements()) {
                vector.addElement((Object)SMIMECapability.getInstance(enumeration.nextElement()));
            }
        } else {
            while (enumeration.hasMoreElements()) {
                SMIMECapability sMIMECapability = SMIMECapability.getInstance(enumeration.nextElement());
                if (!aSN1ObjectIdentifier.equals(sMIMECapability.getCapabilityID())) continue;
                vector.addElement((Object)sMIMECapability);
            }
        }
        return vector;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.capabilities;
    }
}

