package org.bouncycastle.asn1.x509;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Boolean;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;

public class X509Extensions extends ASN1Object {
    public static final ASN1ObjectIdentifier AuditIdentity;
    public static final ASN1ObjectIdentifier AuthorityInfoAccess;
    public static final ASN1ObjectIdentifier AuthorityKeyIdentifier;
    public static final ASN1ObjectIdentifier BasicConstraints;
    public static final ASN1ObjectIdentifier BiometricInfo;
    public static final ASN1ObjectIdentifier CRLDistributionPoints;
    public static final ASN1ObjectIdentifier CRLNumber;
    public static final ASN1ObjectIdentifier CertificateIssuer;
    public static final ASN1ObjectIdentifier CertificatePolicies;
    public static final ASN1ObjectIdentifier DeltaCRLIndicator;
    public static final ASN1ObjectIdentifier ExtendedKeyUsage;
    public static final ASN1ObjectIdentifier FreshestCRL;
    public static final ASN1ObjectIdentifier InhibitAnyPolicy;
    public static final ASN1ObjectIdentifier InstructionCode;
    public static final ASN1ObjectIdentifier InvalidityDate;
    public static final ASN1ObjectIdentifier IssuerAlternativeName;
    public static final ASN1ObjectIdentifier IssuingDistributionPoint;
    public static final ASN1ObjectIdentifier KeyUsage;
    public static final ASN1ObjectIdentifier LogoType;
    public static final ASN1ObjectIdentifier NameConstraints;
    public static final ASN1ObjectIdentifier NoRevAvail;
    public static final ASN1ObjectIdentifier PolicyConstraints;
    public static final ASN1ObjectIdentifier PolicyMappings;
    public static final ASN1ObjectIdentifier PrivateKeyUsagePeriod;
    public static final ASN1ObjectIdentifier QCStatements;
    public static final ASN1ObjectIdentifier ReasonCode;
    public static final ASN1ObjectIdentifier SubjectAlternativeName;
    public static final ASN1ObjectIdentifier SubjectDirectoryAttributes;
    public static final ASN1ObjectIdentifier SubjectInfoAccess;
    public static final ASN1ObjectIdentifier SubjectKeyIdentifier;
    public static final ASN1ObjectIdentifier TargetInformation;
    private Hashtable extensions;
    private Vector ordering;

    static {
        SubjectDirectoryAttributes = new ASN1ObjectIdentifier("2.5.29.9");
        SubjectKeyIdentifier = new ASN1ObjectIdentifier("2.5.29.14");
        KeyUsage = new ASN1ObjectIdentifier("2.5.29.15");
        PrivateKeyUsagePeriod = new ASN1ObjectIdentifier("2.5.29.16");
        SubjectAlternativeName = new ASN1ObjectIdentifier("2.5.29.17");
        IssuerAlternativeName = new ASN1ObjectIdentifier("2.5.29.18");
        BasicConstraints = new ASN1ObjectIdentifier("2.5.29.19");
        CRLNumber = new ASN1ObjectIdentifier("2.5.29.20");
        ReasonCode = new ASN1ObjectIdentifier("2.5.29.21");
        InstructionCode = new ASN1ObjectIdentifier("2.5.29.23");
        InvalidityDate = new ASN1ObjectIdentifier("2.5.29.24");
        DeltaCRLIndicator = new ASN1ObjectIdentifier("2.5.29.27");
        IssuingDistributionPoint = new ASN1ObjectIdentifier("2.5.29.28");
        CertificateIssuer = new ASN1ObjectIdentifier("2.5.29.29");
        NameConstraints = new ASN1ObjectIdentifier("2.5.29.30");
        CRLDistributionPoints = new ASN1ObjectIdentifier("2.5.29.31");
        CertificatePolicies = new ASN1ObjectIdentifier("2.5.29.32");
        PolicyMappings = new ASN1ObjectIdentifier("2.5.29.33");
        AuthorityKeyIdentifier = new ASN1ObjectIdentifier("2.5.29.35");
        PolicyConstraints = new ASN1ObjectIdentifier("2.5.29.36");
        ExtendedKeyUsage = new ASN1ObjectIdentifier("2.5.29.37");
        FreshestCRL = new ASN1ObjectIdentifier("2.5.29.46");
        InhibitAnyPolicy = new ASN1ObjectIdentifier("2.5.29.54");
        AuthorityInfoAccess = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.1");
        SubjectInfoAccess = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.11");
        LogoType = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.12");
        BiometricInfo = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.2");
        QCStatements = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.3");
        AuditIdentity = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.4");
        NoRevAvail = new ASN1ObjectIdentifier("2.5.29.56");
        TargetInformation = new ASN1ObjectIdentifier("2.5.29.55");
    }

    public X509Extensions(Hashtable hashtable) {
        this(null, hashtable);
    }

    public X509Extensions(Vector vector, Hashtable hashtable) {
        this.extensions = new Hashtable();
        this.ordering = new Vector();
        Enumeration keys = vector == null ? hashtable.keys() : vector.elements();
        while (keys.hasMoreElements()) {
            this.ordering.addElement(ASN1ObjectIdentifier.getInstance(keys.nextElement()));
        }
        Enumeration elements = this.ordering.elements();
        while (elements.hasMoreElements()) {
            ASN1ObjectIdentifier instance = ASN1ObjectIdentifier.getInstance(elements.nextElement());
            this.extensions.put(instance, (X509Extension) hashtable.get(instance));
        }
    }

    public X509Extensions(Vector vector, Vector vector2) {
        this.extensions = new Hashtable();
        this.ordering = new Vector();
        Enumeration elements = vector.elements();
        while (elements.hasMoreElements()) {
            this.ordering.addElement(elements.nextElement());
        }
        Enumeration elements2 = this.ordering.elements();
        int i = 0;
        while (elements2.hasMoreElements()) {
            this.extensions.put((ASN1ObjectIdentifier) elements2.nextElement(), (X509Extension) vector2.elementAt(i));
            i++;
        }
    }

    public X509Extensions(ASN1Sequence aSN1Sequence) {
        this.extensions = new Hashtable();
        this.ordering = new Vector();
        Enumeration objects = aSN1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            ASN1Sequence instance = ASN1Sequence.getInstance(objects.nextElement());
            if (instance.size() == 3) {
                this.extensions.put(instance.getObjectAt(0), new X509Extension(ASN1Boolean.getInstance(instance.getObjectAt(1)), ASN1OctetString.getInstance(instance.getObjectAt(2))));
            } else if (instance.size() == 2) {
                this.extensions.put(instance.getObjectAt(0), new X509Extension(false, ASN1OctetString.getInstance(instance.getObjectAt(1))));
            } else {
                throw new IllegalArgumentException("Bad sequence size: " + instance.size());
            }
            this.ordering.addElement(instance.getObjectAt(0));
        }
    }

    private ASN1ObjectIdentifier[] getExtensionOIDs(boolean z) {
        Vector vector = new Vector();
        for (int i = 0; i != this.ordering.size(); i++) {
            Object elementAt = this.ordering.elementAt(i);
            if (((X509Extension) this.extensions.get(elementAt)).isCritical() == z) {
                vector.addElement(elementAt);
            }
        }
        return toOidArray(vector);
    }

    public static X509Extensions getInstance(Object obj) {
        if (obj == null || (obj instanceof X509Extensions)) {
            return (X509Extensions) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new X509Extensions((ASN1Sequence) obj);
        }
        if (obj instanceof Extensions) {
            return new X509Extensions((ASN1Sequence) ((Extensions) obj).toASN1Primitive());
        }
        if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject) obj).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static X509Extensions getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    private ASN1ObjectIdentifier[] toOidArray(Vector vector) {
        ASN1ObjectIdentifier[] aSN1ObjectIdentifierArr = new ASN1ObjectIdentifier[vector.size()];
        for (int i = 0; i != aSN1ObjectIdentifierArr.length; i++) {
            aSN1ObjectIdentifierArr[i] = (ASN1ObjectIdentifier) vector.elementAt(i);
        }
        return aSN1ObjectIdentifierArr;
    }

    public boolean equivalent(X509Extensions x509Extensions) {
        if (this.extensions.size() != x509Extensions.extensions.size()) {
            return false;
        }
        Enumeration keys = this.extensions.keys();
        while (keys.hasMoreElements()) {
            Object nextElement = keys.nextElement();
            if (!this.extensions.get(nextElement).equals(x509Extensions.extensions.get(nextElement))) {
                return false;
            }
        }
        return true;
    }

    public ASN1ObjectIdentifier[] getCriticalExtensionOIDs() {
        return getExtensionOIDs(true);
    }

    public X509Extension getExtension(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return (X509Extension) this.extensions.get(aSN1ObjectIdentifier);
    }

    public ASN1ObjectIdentifier[] getExtensionOIDs() {
        return toOidArray(this.ordering);
    }

    public ASN1ObjectIdentifier[] getNonCriticalExtensionOIDs() {
        return getExtensionOIDs(false);
    }

    public Enumeration oids() {
        return this.ordering.elements();
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        Enumeration elements = this.ordering.elements();
        while (elements.hasMoreElements()) {
            ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) elements.nextElement();
            X509Extension x509Extension = (X509Extension) this.extensions.get(aSN1ObjectIdentifier);
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            aSN1EncodableVector2.add(aSN1ObjectIdentifier);
            if (x509Extension.isCritical()) {
                aSN1EncodableVector2.add(ASN1Boolean.TRUE);
            }
            aSN1EncodableVector2.add(x509Extension.getValue());
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
