/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
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
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.OriginatorInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class AuthenticatedData
extends ASN1Object {
    private ASN1Set authAttrs;
    private AlgorithmIdentifier digestAlgorithm;
    private ContentInfo encapsulatedContentInfo;
    private ASN1OctetString mac;
    private AlgorithmIdentifier macAlgorithm;
    private OriginatorInfo originatorInfo;
    private ASN1Set recipientInfos;
    private ASN1Set unauthAttrs;
    private ASN1Integer version;

    /*
     * Enabled aggressive block sorting
     */
    public AuthenticatedData(ASN1Sequence aSN1Sequence) {
        int n2;
        int n3;
        int n4;
        this.version = (ASN1Integer)aSN1Sequence.getObjectAt(0);
        ASN1Encodable aSN1Encodable = aSN1Sequence.getObjectAt(1);
        if (aSN1Encodable instanceof ASN1TaggedObject) {
            this.originatorInfo = OriginatorInfo.getInstance((ASN1TaggedObject)aSN1Encodable, false);
            n4 = 3;
            aSN1Encodable = aSN1Sequence.getObjectAt(2);
        } else {
            n4 = 2;
        }
        this.recipientInfos = ASN1Set.getInstance(aSN1Encodable);
        int n5 = n4 + 1;
        this.macAlgorithm = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(n4));
        int n6 = n5 + 1;
        ASN1Encodable aSN1Encodable2 = aSN1Sequence.getObjectAt(n5);
        if (aSN1Encodable2 instanceof ASN1TaggedObject) {
            this.digestAlgorithm = AlgorithmIdentifier.getInstance((ASN1TaggedObject)aSN1Encodable2, false);
            n2 = n6 + 1;
            aSN1Encodable2 = aSN1Sequence.getObjectAt(n6);
        } else {
            n2 = n6;
        }
        this.encapsulatedContentInfo = ContentInfo.getInstance(aSN1Encodable2);
        int n7 = n2 + 1;
        ASN1Encodable aSN1Encodable3 = aSN1Sequence.getObjectAt(n2);
        if (aSN1Encodable3 instanceof ASN1TaggedObject) {
            this.authAttrs = ASN1Set.getInstance((ASN1TaggedObject)aSN1Encodable3, false);
            n3 = n7 + 1;
            aSN1Encodable3 = aSN1Sequence.getObjectAt(n7);
        } else {
            n3 = n7;
        }
        this.mac = ASN1OctetString.getInstance(aSN1Encodable3);
        if (aSN1Sequence.size() > n3) {
            this.unauthAttrs = ASN1Set.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(n3), false);
        }
    }

    public AuthenticatedData(OriginatorInfo originatorInfo, ASN1Set aSN1Set, AlgorithmIdentifier algorithmIdentifier, AlgorithmIdentifier algorithmIdentifier2, ContentInfo contentInfo, ASN1Set aSN1Set2, ASN1OctetString aSN1OctetString, ASN1Set aSN1Set3) {
        if (!(algorithmIdentifier2 == null && aSN1Set2 == null || algorithmIdentifier2 != null && aSN1Set2 != null)) {
            throw new IllegalArgumentException("digestAlgorithm and authAttrs must be set together");
        }
        this.version = new ASN1Integer(AuthenticatedData.calculateVersion(originatorInfo));
        this.originatorInfo = originatorInfo;
        this.macAlgorithm = algorithmIdentifier;
        this.digestAlgorithm = algorithmIdentifier2;
        this.recipientInfos = aSN1Set;
        this.encapsulatedContentInfo = contentInfo;
        this.authAttrs = aSN1Set2;
        this.mac = aSN1OctetString;
        this.unauthAttrs = aSN1Set3;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static int calculateVersion(OriginatorInfo var0) {
        if (var0 == null) {
            return 0;
        }
        var1_1 = var0.getCertificates().getObjects();
        var2_2 = 0;
        while (var1_1.hasMoreElements()) {
            var5_3 = var1_1.nextElement();
            if (!(var5_3 instanceof ASN1TaggedObject)) ** GOTO lbl15
            var7_5 = (ASN1TaggedObject)var5_3;
            if (var7_5.getTagNo() == 2) {
                var6_4 = 1;
            } else {
                if (var7_5.getTagNo() == 3) {
                    var2_2 = 3;
                    break;
                }
lbl15: // 3 sources:
                var6_4 = var2_2;
            }
            var2_2 = var6_4;
        }
        if (var0.getCRLs() == null) return var2_2;
        var3_6 = var0.getCRLs().getObjects();
        do {
            if (var3_6.hasMoreElements() == false) return var2_2;
        } while (!((var4_7 = var3_6.nextElement()) instanceof ASN1TaggedObject) || ((ASN1TaggedObject)var4_7).getTagNo() != 1);
        return 3;
    }

    public static AuthenticatedData getInstance(Object object) {
        if (object == null || object instanceof AuthenticatedData) {
            return (AuthenticatedData)object;
        }
        if (object instanceof ASN1Sequence) {
            return new AuthenticatedData((ASN1Sequence)object);
        }
        throw new IllegalArgumentException("Invalid AuthenticatedData: " + object.getClass().getName());
    }

    public static AuthenticatedData getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return AuthenticatedData.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public ASN1Set getAuthAttrs() {
        return this.authAttrs;
    }

    public AlgorithmIdentifier getDigestAlgorithm() {
        return this.digestAlgorithm;
    }

    public ContentInfo getEncapsulatedContentInfo() {
        return this.encapsulatedContentInfo;
    }

    public ASN1OctetString getMac() {
        return this.mac;
    }

    public AlgorithmIdentifier getMacAlgorithm() {
        return this.macAlgorithm;
    }

    public OriginatorInfo getOriginatorInfo() {
        return this.originatorInfo;
    }

    public ASN1Set getRecipientInfos() {
        return this.recipientInfos;
    }

    public ASN1Set getUnauthAttrs() {
        return this.unauthAttrs;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.version);
        if (this.originatorInfo != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.originatorInfo));
        }
        aSN1EncodableVector.add(this.recipientInfos);
        aSN1EncodableVector.add(this.macAlgorithm);
        if (this.digestAlgorithm != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.digestAlgorithm));
        }
        aSN1EncodableVector.add(this.encapsulatedContentInfo);
        if (this.authAttrs != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 2, this.authAttrs));
        }
        aSN1EncodableVector.add(this.mac);
        if (this.unauthAttrs != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 3, this.unauthAttrs));
        }
        return new BERSequence(aSN1EncodableVector);
    }
}

