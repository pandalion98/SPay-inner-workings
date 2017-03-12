package org.bouncycastle.asn1.x509;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Boolean;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;

public class Extension extends ASN1Object {
    public static final ASN1ObjectIdentifier auditIdentity;
    public static final ASN1ObjectIdentifier authorityInfoAccess;
    public static final ASN1ObjectIdentifier authorityKeyIdentifier;
    public static final ASN1ObjectIdentifier basicConstraints;
    public static final ASN1ObjectIdentifier biometricInfo;
    public static final ASN1ObjectIdentifier cRLDistributionPoints;
    public static final ASN1ObjectIdentifier cRLNumber;
    public static final ASN1ObjectIdentifier certificateIssuer;
    public static final ASN1ObjectIdentifier certificatePolicies;
    public static final ASN1ObjectIdentifier deltaCRLIndicator;
    public static final ASN1ObjectIdentifier extendedKeyUsage;
    public static final ASN1ObjectIdentifier freshestCRL;
    public static final ASN1ObjectIdentifier inhibitAnyPolicy;
    public static final ASN1ObjectIdentifier instructionCode;
    public static final ASN1ObjectIdentifier invalidityDate;
    public static final ASN1ObjectIdentifier issuerAlternativeName;
    public static final ASN1ObjectIdentifier issuingDistributionPoint;
    public static final ASN1ObjectIdentifier keyUsage;
    public static final ASN1ObjectIdentifier logoType;
    public static final ASN1ObjectIdentifier nameConstraints;
    public static final ASN1ObjectIdentifier noRevAvail;
    public static final ASN1ObjectIdentifier policyConstraints;
    public static final ASN1ObjectIdentifier policyMappings;
    public static final ASN1ObjectIdentifier privateKeyUsagePeriod;
    public static final ASN1ObjectIdentifier qCStatements;
    public static final ASN1ObjectIdentifier reasonCode;
    public static final ASN1ObjectIdentifier subjectAlternativeName;
    public static final ASN1ObjectIdentifier subjectDirectoryAttributes;
    public static final ASN1ObjectIdentifier subjectInfoAccess;
    public static final ASN1ObjectIdentifier subjectKeyIdentifier;
    public static final ASN1ObjectIdentifier targetInformation;
    private boolean critical;
    private ASN1ObjectIdentifier extnId;
    private ASN1OctetString value;

    static {
        subjectDirectoryAttributes = new ASN1ObjectIdentifier("2.5.29.9");
        subjectKeyIdentifier = new ASN1ObjectIdentifier("2.5.29.14");
        keyUsage = new ASN1ObjectIdentifier("2.5.29.15");
        privateKeyUsagePeriod = new ASN1ObjectIdentifier("2.5.29.16");
        subjectAlternativeName = new ASN1ObjectIdentifier("2.5.29.17");
        issuerAlternativeName = new ASN1ObjectIdentifier("2.5.29.18");
        basicConstraints = new ASN1ObjectIdentifier("2.5.29.19");
        cRLNumber = new ASN1ObjectIdentifier("2.5.29.20");
        reasonCode = new ASN1ObjectIdentifier("2.5.29.21");
        instructionCode = new ASN1ObjectIdentifier("2.5.29.23");
        invalidityDate = new ASN1ObjectIdentifier("2.5.29.24");
        deltaCRLIndicator = new ASN1ObjectIdentifier("2.5.29.27");
        issuingDistributionPoint = new ASN1ObjectIdentifier("2.5.29.28");
        certificateIssuer = new ASN1ObjectIdentifier("2.5.29.29");
        nameConstraints = new ASN1ObjectIdentifier("2.5.29.30");
        cRLDistributionPoints = new ASN1ObjectIdentifier("2.5.29.31");
        certificatePolicies = new ASN1ObjectIdentifier("2.5.29.32");
        policyMappings = new ASN1ObjectIdentifier("2.5.29.33");
        authorityKeyIdentifier = new ASN1ObjectIdentifier("2.5.29.35");
        policyConstraints = new ASN1ObjectIdentifier("2.5.29.36");
        extendedKeyUsage = new ASN1ObjectIdentifier("2.5.29.37");
        freshestCRL = new ASN1ObjectIdentifier("2.5.29.46");
        inhibitAnyPolicy = new ASN1ObjectIdentifier("2.5.29.54");
        authorityInfoAccess = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.1");
        subjectInfoAccess = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.11");
        logoType = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.12");
        biometricInfo = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.2");
        qCStatements = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.3");
        auditIdentity = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.4");
        noRevAvail = new ASN1ObjectIdentifier("2.5.29.56");
        targetInformation = new ASN1ObjectIdentifier("2.5.29.55");
    }

    public Extension(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Boolean aSN1Boolean, ASN1OctetString aSN1OctetString) {
        this(aSN1ObjectIdentifier, aSN1Boolean.isTrue(), aSN1OctetString);
    }

    public Extension(ASN1ObjectIdentifier aSN1ObjectIdentifier, boolean z, ASN1OctetString aSN1OctetString) {
        this.extnId = aSN1ObjectIdentifier;
        this.critical = z;
        this.value = aSN1OctetString;
    }

    public Extension(ASN1ObjectIdentifier aSN1ObjectIdentifier, boolean z, byte[] bArr) {
        this(aSN1ObjectIdentifier, z, new DEROctetString(bArr));
    }

    private Extension(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() == 2) {
            this.extnId = ASN1ObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
            this.critical = false;
            this.value = ASN1OctetString.getInstance(aSN1Sequence.getObjectAt(1));
        } else if (aSN1Sequence.size() == 3) {
            this.extnId = ASN1ObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
            this.critical = ASN1Boolean.getInstance(aSN1Sequence.getObjectAt(1)).isTrue();
            this.value = ASN1OctetString.getInstance(aSN1Sequence.getObjectAt(2));
        } else {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
    }

    private static ASN1Primitive convertValueToObject(Extension extension) {
        try {
            return ASN1Primitive.fromByteArray(extension.getExtnValue().getOctets());
        } catch (IOException e) {
            throw new IllegalArgumentException("can't convert extension: " + e);
        }
    }

    public static Extension getInstance(Object obj) {
        return obj instanceof Extension ? (Extension) obj : obj != null ? new Extension(ASN1Sequence.getInstance(obj)) : null;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Extension)) {
            return false;
        }
        Extension extension = (Extension) obj;
        return extension.getExtnId().equals(getExtnId()) && extension.getExtnValue().equals(getExtnValue()) && extension.isCritical() == isCritical();
    }

    public ASN1ObjectIdentifier getExtnId() {
        return this.extnId;
    }

    public ASN1OctetString getExtnValue() {
        return this.value;
    }

    public ASN1Encodable getParsedValue() {
        return convertValueToObject(this);
    }

    public int hashCode() {
        return isCritical() ? getExtnValue().hashCode() ^ getExtnId().hashCode() : (getExtnValue().hashCode() ^ getExtnId().hashCode()) ^ -1;
    }

    public boolean isCritical() {
        return this.critical;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.extnId);
        if (this.critical) {
            aSN1EncodableVector.add(ASN1Boolean.getInstance(true));
        }
        aSN1EncodableVector.add(this.value);
        return new DERSequence(aSN1EncodableVector);
    }
}
