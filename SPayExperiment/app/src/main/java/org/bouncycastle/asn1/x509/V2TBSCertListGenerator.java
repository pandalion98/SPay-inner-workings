/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.x509;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1UTCTime;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.TBSCertList;
import org.bouncycastle.asn1.x509.Time;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;

public class V2TBSCertListGenerator {
    private static final ASN1Sequence[] reasons = new ASN1Sequence[11];
    private ASN1EncodableVector crlentries = new ASN1EncodableVector();
    private Extensions extensions = null;
    private X500Name issuer;
    private Time nextUpdate = null;
    private AlgorithmIdentifier signature;
    private Time thisUpdate;
    private ASN1Integer version = new ASN1Integer(1L);

    static {
        V2TBSCertListGenerator.reasons[0] = V2TBSCertListGenerator.createReasonExtension(0);
        V2TBSCertListGenerator.reasons[1] = V2TBSCertListGenerator.createReasonExtension(1);
        V2TBSCertListGenerator.reasons[2] = V2TBSCertListGenerator.createReasonExtension(2);
        V2TBSCertListGenerator.reasons[3] = V2TBSCertListGenerator.createReasonExtension(3);
        V2TBSCertListGenerator.reasons[4] = V2TBSCertListGenerator.createReasonExtension(4);
        V2TBSCertListGenerator.reasons[5] = V2TBSCertListGenerator.createReasonExtension(5);
        V2TBSCertListGenerator.reasons[6] = V2TBSCertListGenerator.createReasonExtension(6);
        V2TBSCertListGenerator.reasons[7] = V2TBSCertListGenerator.createReasonExtension(7);
        V2TBSCertListGenerator.reasons[8] = V2TBSCertListGenerator.createReasonExtension(8);
        V2TBSCertListGenerator.reasons[9] = V2TBSCertListGenerator.createReasonExtension(9);
        V2TBSCertListGenerator.reasons[10] = V2TBSCertListGenerator.createReasonExtension(10);
    }

    private static ASN1Sequence createInvalidityDateExtension(ASN1GeneralizedTime aSN1GeneralizedTime) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        try {
            aSN1EncodableVector.add(Extension.invalidityDate);
            aSN1EncodableVector.add(new DEROctetString(aSN1GeneralizedTime.getEncoded()));
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("error encoding reason: " + (Object)((Object)iOException));
        }
        return new DERSequence(aSN1EncodableVector);
    }

    private static ASN1Sequence createReasonExtension(int n2) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        CRLReason cRLReason = CRLReason.lookup(n2);
        try {
            aSN1EncodableVector.add(Extension.reasonCode);
            aSN1EncodableVector.add(new DEROctetString(cRLReason.getEncoded()));
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("error encoding reason: " + (Object)((Object)iOException));
        }
        return new DERSequence(aSN1EncodableVector);
    }

    private void internalAddCRLEntry(ASN1Integer aSN1Integer, Time time, ASN1Sequence aSN1Sequence) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(aSN1Integer);
        aSN1EncodableVector.add(time);
        if (aSN1Sequence != null) {
            aSN1EncodableVector.add(aSN1Sequence);
        }
        this.addCRLEntry(new DERSequence(aSN1EncodableVector));
    }

    public void addCRLEntry(ASN1Integer aSN1Integer, ASN1UTCTime aSN1UTCTime, int n2) {
        this.addCRLEntry(aSN1Integer, new Time(aSN1UTCTime), n2);
    }

    public void addCRLEntry(ASN1Integer aSN1Integer, Time time, int n2) {
        this.addCRLEntry(aSN1Integer, time, n2, null);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void addCRLEntry(ASN1Integer aSN1Integer, Time time, int n2, ASN1GeneralizedTime aSN1GeneralizedTime) {
        if (n2 != 0) {
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            if (n2 < reasons.length) {
                if (n2 < 0) {
                    throw new IllegalArgumentException("invalid reason value: " + n2);
                }
                aSN1EncodableVector.add(reasons[n2]);
            } else {
                aSN1EncodableVector.add(V2TBSCertListGenerator.createReasonExtension(n2));
            }
            if (aSN1GeneralizedTime != null) {
                aSN1EncodableVector.add(V2TBSCertListGenerator.createInvalidityDateExtension(aSN1GeneralizedTime));
            }
            this.internalAddCRLEntry(aSN1Integer, time, new DERSequence(aSN1EncodableVector));
            return;
        }
        if (aSN1GeneralizedTime != null) {
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            aSN1EncodableVector.add(V2TBSCertListGenerator.createInvalidityDateExtension(aSN1GeneralizedTime));
            this.internalAddCRLEntry(aSN1Integer, time, new DERSequence(aSN1EncodableVector));
            return;
        }
        this.addCRLEntry(aSN1Integer, time, null);
    }

    public void addCRLEntry(ASN1Integer aSN1Integer, Time time, Extensions extensions) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(aSN1Integer);
        aSN1EncodableVector.add(time);
        if (extensions != null) {
            aSN1EncodableVector.add(extensions);
        }
        this.addCRLEntry(new DERSequence(aSN1EncodableVector));
    }

    public void addCRLEntry(ASN1Sequence aSN1Sequence) {
        this.crlentries.add(aSN1Sequence);
    }

    public TBSCertList generateTBSCertList() {
        if (this.signature == null || this.issuer == null || this.thisUpdate == null) {
            throw new IllegalStateException("Not all mandatory fields set in V2 TBSCertList generator.");
        }
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.version);
        aSN1EncodableVector.add(this.signature);
        aSN1EncodableVector.add(this.issuer);
        aSN1EncodableVector.add(this.thisUpdate);
        if (this.nextUpdate != null) {
            aSN1EncodableVector.add(this.nextUpdate);
        }
        if (this.crlentries.size() != 0) {
            aSN1EncodableVector.add(new DERSequence(this.crlentries));
        }
        if (this.extensions != null) {
            aSN1EncodableVector.add(new DERTaggedObject(0, this.extensions));
        }
        return new TBSCertList(new DERSequence(aSN1EncodableVector));
    }

    public void setExtensions(Extensions extensions) {
        this.extensions = extensions;
    }

    public void setExtensions(X509Extensions x509Extensions) {
        this.setExtensions(Extensions.getInstance(x509Extensions));
    }

    public void setIssuer(X500Name x500Name) {
        this.issuer = x500Name;
    }

    public void setIssuer(X509Name x509Name) {
        this.issuer = X500Name.getInstance(x509Name.toASN1Primitive());
    }

    public void setNextUpdate(ASN1UTCTime aSN1UTCTime) {
        this.nextUpdate = new Time(aSN1UTCTime);
    }

    public void setNextUpdate(Time time) {
        this.nextUpdate = time;
    }

    public void setSignature(AlgorithmIdentifier algorithmIdentifier) {
        this.signature = algorithmIdentifier;
    }

    public void setThisUpdate(ASN1UTCTime aSN1UTCTime) {
        this.thisUpdate = new Time(aSN1UTCTime);
    }

    public void setThisUpdate(Time time) {
        this.thisUpdate = time;
    }
}

