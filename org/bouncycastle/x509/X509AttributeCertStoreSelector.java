package org.bouncycastle.x509;

import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.Target;
import org.bouncycastle.asn1.x509.TargetInformation;
import org.bouncycastle.asn1.x509.Targets;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.util.Selector;

public class X509AttributeCertStoreSelector implements Selector {
    private X509AttributeCertificate attributeCert;
    private Date attributeCertificateValid;
    private AttributeCertificateHolder holder;
    private AttributeCertificateIssuer issuer;
    private BigInteger serialNumber;
    private Collection targetGroups;
    private Collection targetNames;

    public X509AttributeCertStoreSelector() {
        this.targetNames = new HashSet();
        this.targetGroups = new HashSet();
    }

    private Set extractGeneralNames(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            return new HashSet();
        }
        Set hashSet = new HashSet();
        for (Object next : collection) {
            if (next instanceof GeneralName) {
                hashSet.add(next);
            } else {
                hashSet.add(GeneralName.getInstance(ASN1Primitive.fromByteArray((byte[]) next)));
            }
        }
        return hashSet;
    }

    public void addTargetGroup(GeneralName generalName) {
        this.targetGroups.add(generalName);
    }

    public void addTargetGroup(byte[] bArr) {
        addTargetGroup(GeneralName.getInstance(ASN1Primitive.fromByteArray(bArr)));
    }

    public void addTargetName(GeneralName generalName) {
        this.targetNames.add(generalName);
    }

    public void addTargetName(byte[] bArr) {
        addTargetName(GeneralName.getInstance(ASN1Primitive.fromByteArray(bArr)));
    }

    public Object clone() {
        X509AttributeCertStoreSelector x509AttributeCertStoreSelector = new X509AttributeCertStoreSelector();
        x509AttributeCertStoreSelector.attributeCert = this.attributeCert;
        x509AttributeCertStoreSelector.attributeCertificateValid = getAttributeCertificateValid();
        x509AttributeCertStoreSelector.holder = this.holder;
        x509AttributeCertStoreSelector.issuer = this.issuer;
        x509AttributeCertStoreSelector.serialNumber = this.serialNumber;
        x509AttributeCertStoreSelector.targetGroups = getTargetGroups();
        x509AttributeCertStoreSelector.targetNames = getTargetNames();
        return x509AttributeCertStoreSelector;
    }

    public X509AttributeCertificate getAttributeCert() {
        return this.attributeCert;
    }

    public Date getAttributeCertificateValid() {
        return this.attributeCertificateValid != null ? new Date(this.attributeCertificateValid.getTime()) : null;
    }

    public AttributeCertificateHolder getHolder() {
        return this.holder;
    }

    public AttributeCertificateIssuer getIssuer() {
        return this.issuer;
    }

    public BigInteger getSerialNumber() {
        return this.serialNumber;
    }

    public Collection getTargetGroups() {
        return Collections.unmodifiableCollection(this.targetGroups);
    }

    public Collection getTargetNames() {
        return Collections.unmodifiableCollection(this.targetNames);
    }

    public boolean match(Object obj) {
        if (!(obj instanceof X509AttributeCertificate)) {
            return false;
        }
        X509AttributeCertificate x509AttributeCertificate = (X509AttributeCertificate) obj;
        if (this.attributeCert != null && !this.attributeCert.equals(x509AttributeCertificate)) {
            return false;
        }
        if (this.serialNumber != null && !x509AttributeCertificate.getSerialNumber().equals(this.serialNumber)) {
            return false;
        }
        if (this.holder != null && !x509AttributeCertificate.getHolder().equals(this.holder)) {
            return false;
        }
        if (this.issuer != null && !x509AttributeCertificate.getIssuer().equals(this.issuer)) {
            return false;
        }
        if (this.attributeCertificateValid != null) {
            try {
                x509AttributeCertificate.checkValidity(this.attributeCertificateValid);
            } catch (CertificateExpiredException e) {
                return false;
            } catch (CertificateNotYetValidException e2) {
                return false;
            }
        }
        if (!(this.targetNames.isEmpty() && this.targetGroups.isEmpty())) {
            byte[] extensionValue = x509AttributeCertificate.getExtensionValue(X509Extensions.TargetInformation.getId());
            if (extensionValue != null) {
                try {
                    boolean z;
                    Target[] targets;
                    Targets[] targetsObjects = TargetInformation.getInstance(new ASN1InputStream(((DEROctetString) ASN1Primitive.fromByteArray(extensionValue)).getOctets()).readObject()).getTargetsObjects();
                    if (!this.targetNames.isEmpty()) {
                        z = false;
                        for (Targets targets2 : targetsObjects) {
                            targets = targets2.getTargets();
                            for (Target targetName : targets) {
                                if (this.targetNames.contains(GeneralName.getInstance(targetName.getTargetName()))) {
                                    z = true;
                                    break;
                                }
                            }
                        }
                        if (!z) {
                            return false;
                        }
                    }
                    if (!this.targetGroups.isEmpty()) {
                        z = false;
                        for (Targets targets22 : targetsObjects) {
                            targets = targets22.getTargets();
                            for (Target targetName2 : targets) {
                                if (this.targetGroups.contains(GeneralName.getInstance(targetName2.getTargetGroup()))) {
                                    z = true;
                                    break;
                                }
                            }
                        }
                        if (!z) {
                            return false;
                        }
                    }
                } catch (IOException e3) {
                    return false;
                } catch (IllegalArgumentException e4) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setAttributeCert(X509AttributeCertificate x509AttributeCertificate) {
        this.attributeCert = x509AttributeCertificate;
    }

    public void setAttributeCertificateValid(Date date) {
        if (date != null) {
            this.attributeCertificateValid = new Date(date.getTime());
        } else {
            this.attributeCertificateValid = null;
        }
    }

    public void setHolder(AttributeCertificateHolder attributeCertificateHolder) {
        this.holder = attributeCertificateHolder;
    }

    public void setIssuer(AttributeCertificateIssuer attributeCertificateIssuer) {
        this.issuer = attributeCertificateIssuer;
    }

    public void setSerialNumber(BigInteger bigInteger) {
        this.serialNumber = bigInteger;
    }

    public void setTargetGroups(Collection collection) {
        this.targetGroups = extractGeneralNames(collection);
    }

    public void setTargetNames(Collection collection) {
        this.targetNames = extractGeneralNames(collection);
    }
}
