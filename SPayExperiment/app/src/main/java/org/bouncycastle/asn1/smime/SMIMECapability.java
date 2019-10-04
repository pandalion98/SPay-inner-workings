/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.smime;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

public class SMIMECapability
extends ASN1Object {
    public static final ASN1ObjectIdentifier aES128_CBC;
    public static final ASN1ObjectIdentifier aES192_CBC;
    public static final ASN1ObjectIdentifier aES256_CBC;
    public static final ASN1ObjectIdentifier canNotDecryptAny;
    public static final ASN1ObjectIdentifier dES_CBC;
    public static final ASN1ObjectIdentifier dES_EDE3_CBC;
    public static final ASN1ObjectIdentifier preferSignedData;
    public static final ASN1ObjectIdentifier rC2_CBC;
    public static final ASN1ObjectIdentifier sMIMECapabilitiesVersions;
    private ASN1ObjectIdentifier capabilityID;
    private ASN1Encodable parameters;

    static {
        preferSignedData = PKCSObjectIdentifiers.preferSignedData;
        canNotDecryptAny = PKCSObjectIdentifiers.canNotDecryptAny;
        sMIMECapabilitiesVersions = PKCSObjectIdentifiers.sMIMECapabilitiesVersions;
        dES_CBC = new ASN1ObjectIdentifier("1.3.14.3.2.7");
        dES_EDE3_CBC = PKCSObjectIdentifiers.des_EDE3_CBC;
        rC2_CBC = PKCSObjectIdentifiers.RC2_CBC;
        aES128_CBC = NISTObjectIdentifiers.id_aes128_CBC;
        aES192_CBC = NISTObjectIdentifiers.id_aes192_CBC;
        aES256_CBC = NISTObjectIdentifiers.id_aes256_CBC;
    }

    public SMIMECapability(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Encodable aSN1Encodable) {
        this.capabilityID = aSN1ObjectIdentifier;
        this.parameters = aSN1Encodable;
    }

    public SMIMECapability(ASN1Sequence aSN1Sequence) {
        this.capabilityID = (ASN1ObjectIdentifier)aSN1Sequence.getObjectAt(0);
        if (aSN1Sequence.size() > 1) {
            this.parameters = (ASN1Primitive)aSN1Sequence.getObjectAt(1);
        }
    }

    public static SMIMECapability getInstance(Object object) {
        if (object == null || object instanceof SMIMECapability) {
            return (SMIMECapability)object;
        }
        if (object instanceof ASN1Sequence) {
            return new SMIMECapability((ASN1Sequence)object);
        }
        throw new IllegalArgumentException("Invalid SMIMECapability");
    }

    public ASN1ObjectIdentifier getCapabilityID() {
        return this.capabilityID;
    }

    public ASN1Encodable getParameters() {
        return this.parameters;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.capabilityID);
        if (this.parameters != null) {
            aSN1EncodableVector.add(this.parameters);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

