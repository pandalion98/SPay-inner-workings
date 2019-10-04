/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.cms;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cms.Attributes;
import org.bouncycastle.asn1.cms.SignerIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class SignerInfo
extends ASN1Object {
    private ASN1Set authenticatedAttributes;
    private AlgorithmIdentifier digAlgorithm;
    private AlgorithmIdentifier digEncryptionAlgorithm;
    private ASN1OctetString encryptedDigest;
    private SignerIdentifier sid;
    private ASN1Set unauthenticatedAttributes;
    private ASN1Integer version;

    /*
     * Enabled aggressive block sorting
     */
    public SignerInfo(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.version = (ASN1Integer)enumeration.nextElement();
        this.sid = SignerIdentifier.getInstance(enumeration.nextElement());
        this.digAlgorithm = AlgorithmIdentifier.getInstance(enumeration.nextElement());
        Object object = enumeration.nextElement();
        if (object instanceof ASN1TaggedObject) {
            this.authenticatedAttributes = ASN1Set.getInstance((ASN1TaggedObject)object, false);
            this.digEncryptionAlgorithm = AlgorithmIdentifier.getInstance(enumeration.nextElement());
        } else {
            this.authenticatedAttributes = null;
            this.digEncryptionAlgorithm = AlgorithmIdentifier.getInstance(object);
        }
        this.encryptedDigest = DEROctetString.getInstance(enumeration.nextElement());
        if (enumeration.hasMoreElements()) {
            this.unauthenticatedAttributes = ASN1Set.getInstance((ASN1TaggedObject)enumeration.nextElement(), false);
            return;
        }
        this.unauthenticatedAttributes = null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public SignerInfo(SignerIdentifier signerIdentifier, AlgorithmIdentifier algorithmIdentifier, ASN1Set aSN1Set, AlgorithmIdentifier algorithmIdentifier2, ASN1OctetString aSN1OctetString, ASN1Set aSN1Set2) {
        this.version = signerIdentifier.isTagged() ? new ASN1Integer(3L) : new ASN1Integer(1L);
        this.sid = signerIdentifier;
        this.digAlgorithm = algorithmIdentifier;
        this.authenticatedAttributes = aSN1Set;
        this.digEncryptionAlgorithm = algorithmIdentifier2;
        this.encryptedDigest = aSN1OctetString;
        this.unauthenticatedAttributes = aSN1Set2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public SignerInfo(SignerIdentifier signerIdentifier, AlgorithmIdentifier algorithmIdentifier, Attributes attributes, AlgorithmIdentifier algorithmIdentifier2, ASN1OctetString aSN1OctetString, Attributes attributes2) {
        this.version = signerIdentifier.isTagged() ? new ASN1Integer(3L) : new ASN1Integer(1L);
        this.sid = signerIdentifier;
        this.digAlgorithm = algorithmIdentifier;
        this.authenticatedAttributes = ASN1Set.getInstance(attributes);
        this.digEncryptionAlgorithm = algorithmIdentifier2;
        this.encryptedDigest = aSN1OctetString;
        this.unauthenticatedAttributes = ASN1Set.getInstance(attributes2);
    }

    public static SignerInfo getInstance(Object object) {
        if (object instanceof SignerInfo) {
            return (SignerInfo)object;
        }
        if (object != null) {
            return new SignerInfo(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public ASN1Set getAuthenticatedAttributes() {
        return this.authenticatedAttributes;
    }

    public AlgorithmIdentifier getDigestAlgorithm() {
        return this.digAlgorithm;
    }

    public AlgorithmIdentifier getDigestEncryptionAlgorithm() {
        return this.digEncryptionAlgorithm;
    }

    public ASN1OctetString getEncryptedDigest() {
        return this.encryptedDigest;
    }

    public SignerIdentifier getSID() {
        return this.sid;
    }

    public ASN1Set getUnauthenticatedAttributes() {
        return this.unauthenticatedAttributes;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.version);
        aSN1EncodableVector.add(this.sid);
        aSN1EncodableVector.add(this.digAlgorithm);
        if (this.authenticatedAttributes != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.authenticatedAttributes));
        }
        aSN1EncodableVector.add(this.digEncryptionAlgorithm);
        aSN1EncodableVector.add(this.encryptedDigest);
        if (this.unauthenticatedAttributes != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.unauthenticatedAttributes));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

