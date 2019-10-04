/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.x509;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1UTCTime;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.Time;

public class TBSCertList
extends ASN1Object {
    Extensions crlExtensions;
    X500Name issuer;
    Time nextUpdate;
    ASN1Sequence revokedCertificates;
    AlgorithmIdentifier signature;
    Time thisUpdate;
    ASN1Integer version;

    /*
     * Enabled aggressive block sorting
     */
    public TBSCertList(ASN1Sequence aSN1Sequence) {
        int n2;
        if (aSN1Sequence.size() < 3 || aSN1Sequence.size() > 7) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        if (aSN1Sequence.getObjectAt(0) instanceof ASN1Integer) {
            this.version = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(0));
            n2 = 1;
        } else {
            this.version = null;
            n2 = 0;
        }
        int n3 = n2 + 1;
        this.signature = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(n2));
        int n4 = n3 + 1;
        this.issuer = X500Name.getInstance(aSN1Sequence.getObjectAt(n3));
        int n5 = n4 + 1;
        this.thisUpdate = Time.getInstance(aSN1Sequence.getObjectAt(n4));
        if (n5 < aSN1Sequence.size() && (aSN1Sequence.getObjectAt(n5) instanceof ASN1UTCTime || aSN1Sequence.getObjectAt(n5) instanceof ASN1GeneralizedTime || aSN1Sequence.getObjectAt(n5) instanceof Time)) {
            int n6 = n5 + 1;
            this.nextUpdate = Time.getInstance(aSN1Sequence.getObjectAt(n5));
            n5 = n6;
        }
        if (n5 < aSN1Sequence.size() && !(aSN1Sequence.getObjectAt(n5) instanceof DERTaggedObject)) {
            int n7 = n5 + 1;
            this.revokedCertificates = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(n5));
            n5 = n7;
        }
        if (n5 < aSN1Sequence.size() && aSN1Sequence.getObjectAt(n5) instanceof DERTaggedObject) {
            this.crlExtensions = Extensions.getInstance(ASN1Sequence.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(n5), true));
        }
    }

    public static TBSCertList getInstance(Object object) {
        if (object instanceof TBSCertList) {
            return (TBSCertList)object;
        }
        if (object != null) {
            return new TBSCertList(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static TBSCertList getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return TBSCertList.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public Extensions getExtensions() {
        return this.crlExtensions;
    }

    public X500Name getIssuer() {
        return this.issuer;
    }

    public Time getNextUpdate() {
        return this.nextUpdate;
    }

    public Enumeration getRevokedCertificateEnumeration() {
        if (this.revokedCertificates == null) {
            return new EmptyEnumeration();
        }
        return new RevokedCertificatesEnumeration(this.revokedCertificates.getObjects());
    }

    public CRLEntry[] getRevokedCertificates() {
        if (this.revokedCertificates == null) {
            return new CRLEntry[0];
        }
        CRLEntry[] arrcRLEntry = new CRLEntry[this.revokedCertificates.size()];
        for (int i2 = 0; i2 < arrcRLEntry.length; ++i2) {
            arrcRLEntry[i2] = CRLEntry.getInstance(this.revokedCertificates.getObjectAt(i2));
        }
        return arrcRLEntry;
    }

    public AlgorithmIdentifier getSignature() {
        return this.signature;
    }

    public Time getThisUpdate() {
        return this.thisUpdate;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    public int getVersionNumber() {
        if (this.version == null) {
            return 1;
        }
        return 1 + this.version.getValue().intValue();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.version != null) {
            aSN1EncodableVector.add(this.version);
        }
        aSN1EncodableVector.add(this.signature);
        aSN1EncodableVector.add(this.issuer);
        aSN1EncodableVector.add(this.thisUpdate);
        if (this.nextUpdate != null) {
            aSN1EncodableVector.add(this.nextUpdate);
        }
        if (this.revokedCertificates != null) {
            aSN1EncodableVector.add(this.revokedCertificates);
        }
        if (this.crlExtensions != null) {
            aSN1EncodableVector.add(new DERTaggedObject(0, this.crlExtensions));
        }
        return new DERSequence(aSN1EncodableVector);
    }

    public static class CRLEntry
    extends ASN1Object {
        Extensions crlEntryExtensions;
        ASN1Sequence seq;

        private CRLEntry(ASN1Sequence aSN1Sequence) {
            if (aSN1Sequence.size() < 2 || aSN1Sequence.size() > 3) {
                throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
            }
            this.seq = aSN1Sequence;
        }

        public static CRLEntry getInstance(Object object) {
            if (object instanceof CRLEntry) {
                return (CRLEntry)object;
            }
            if (object != null) {
                return new CRLEntry(ASN1Sequence.getInstance(object));
            }
            return null;
        }

        public Extensions getExtensions() {
            if (this.crlEntryExtensions == null && this.seq.size() == 3) {
                this.crlEntryExtensions = Extensions.getInstance(this.seq.getObjectAt(2));
            }
            return this.crlEntryExtensions;
        }

        public Time getRevocationDate() {
            return Time.getInstance(this.seq.getObjectAt(1));
        }

        public ASN1Integer getUserCertificate() {
            return ASN1Integer.getInstance(this.seq.getObjectAt(0));
        }

        public boolean hasExtensions() {
            return this.seq.size() == 3;
        }

        @Override
        public ASN1Primitive toASN1Primitive() {
            return this.seq;
        }
    }

    private class EmptyEnumeration
    implements Enumeration {
        private EmptyEnumeration() {
        }

        public boolean hasMoreElements() {
            return false;
        }

        public Object nextElement() {
            return null;
        }
    }

    private class RevokedCertificatesEnumeration
    implements Enumeration {
        private final Enumeration en;

        RevokedCertificatesEnumeration(Enumeration enumeration) {
            this.en = enumeration;
        }

        public boolean hasMoreElements() {
            return this.en.hasMoreElements();
        }

        public Object nextElement() {
            return CRLEntry.getInstance(this.en.nextElement());
        }
    }

}

