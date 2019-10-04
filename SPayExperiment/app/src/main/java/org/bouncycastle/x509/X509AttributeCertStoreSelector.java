/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.cert.CertificateExpiredException
 *  java.security.cert.CertificateNotYetValidException
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.Date
 *  java.util.HashSet
 *  java.util.Set
 */
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
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.Target;
import org.bouncycastle.asn1.x509.TargetInformation;
import org.bouncycastle.asn1.x509.Targets;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.util.Selector;
import org.bouncycastle.x509.AttributeCertificateHolder;
import org.bouncycastle.x509.AttributeCertificateIssuer;
import org.bouncycastle.x509.X509AttributeCertificate;

public class X509AttributeCertStoreSelector
implements Selector {
    private X509AttributeCertificate attributeCert;
    private Date attributeCertificateValid;
    private AttributeCertificateHolder holder;
    private AttributeCertificateIssuer issuer;
    private BigInteger serialNumber;
    private Collection targetGroups = new HashSet();
    private Collection targetNames = new HashSet();

    private Set extractGeneralNames(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            return new HashSet();
        }
        HashSet hashSet = new HashSet();
        for (Object object : collection) {
            if (object instanceof GeneralName) {
                hashSet.add(object);
                continue;
            }
            hashSet.add((Object)GeneralName.getInstance(ASN1Primitive.fromByteArray((byte[])object)));
        }
        return hashSet;
    }

    public void addTargetGroup(GeneralName generalName) {
        this.targetGroups.add((Object)generalName);
    }

    public void addTargetGroup(byte[] arrby) {
        this.addTargetGroup(GeneralName.getInstance(ASN1Primitive.fromByteArray(arrby)));
    }

    public void addTargetName(GeneralName generalName) {
        this.targetNames.add((Object)generalName);
    }

    public void addTargetName(byte[] arrby) {
        this.addTargetName(GeneralName.getInstance(ASN1Primitive.fromByteArray(arrby)));
    }

    @Override
    public Object clone() {
        X509AttributeCertStoreSelector x509AttributeCertStoreSelector = new X509AttributeCertStoreSelector();
        x509AttributeCertStoreSelector.attributeCert = this.attributeCert;
        x509AttributeCertStoreSelector.attributeCertificateValid = this.getAttributeCertificateValid();
        x509AttributeCertStoreSelector.holder = this.holder;
        x509AttributeCertStoreSelector.issuer = this.issuer;
        x509AttributeCertStoreSelector.serialNumber = this.serialNumber;
        x509AttributeCertStoreSelector.targetGroups = this.getTargetGroups();
        x509AttributeCertStoreSelector.targetNames = this.getTargetNames();
        return x509AttributeCertStoreSelector;
    }

    public X509AttributeCertificate getAttributeCert() {
        return this.attributeCert;
    }

    public Date getAttributeCertificateValid() {
        if (this.attributeCertificateValid != null) {
            return new Date(this.attributeCertificateValid.getTime());
        }
        return null;
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
        return Collections.unmodifiableCollection((Collection)this.targetGroups);
    }

    public Collection getTargetNames() {
        return Collections.unmodifiableCollection((Collection)this.targetNames);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public boolean match(Object object) {
        Targets[] arrtargets;
        block25 : {
            block26 : {
                TargetInformation targetInformation;
                byte[] arrby;
                if (!(object instanceof X509AttributeCertificate)) {
                    return false;
                }
                X509AttributeCertificate x509AttributeCertificate = (X509AttributeCertificate)object;
                if (this.attributeCert != null) {
                    if (!this.attributeCert.equals((Object)x509AttributeCertificate)) return false;
                }
                if (this.serialNumber != null) {
                    if (!x509AttributeCertificate.getSerialNumber().equals((Object)this.serialNumber)) return false;
                }
                if (this.holder != null) {
                    if (!x509AttributeCertificate.getHolder().equals(this.holder)) return false;
                }
                if (this.issuer != null) {
                    if (!x509AttributeCertificate.getIssuer().equals(this.issuer)) return false;
                }
                if (this.attributeCertificateValid != null) {
                    x509AttributeCertificate.checkValidity(this.attributeCertificateValid);
                }
                if (this.targetNames.isEmpty()) {
                    if (this.targetGroups.isEmpty()) return true;
                }
                if ((arrby = x509AttributeCertificate.getExtensionValue(X509Extensions.TargetInformation.getId())) == null) return true;
                try {
                    targetInformation = TargetInformation.getInstance(new ASN1InputStream(((DEROctetString)DEROctetString.fromByteArray(arrby)).getOctets()).readObject());
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    return false;
                }
                catch (IOException iOException) {
                    return false;
                }
                arrtargets = targetInformation.getTargetsObjects();
                if (this.targetNames.isEmpty()) break block25;
                break block26;
                catch (CertificateNotYetValidException certificateNotYetValidException) {
                    return false;
                }
                catch (CertificateExpiredException certificateExpiredException) {
                    return false;
                }
            }
            int n = 0;
            boolean bl = false;
            block6 : do {
                if (n >= arrtargets.length) {
                    if (!bl) return false;
                    break;
                }
                Target[] arrtarget = arrtargets[n].getTargets();
                int n2 = 0;
                do {
                    block28 : {
                        block27 : {
                            if (n2 >= arrtarget.length) break block27;
                            if (!this.targetNames.contains((Object)GeneralName.getInstance(arrtarget[n2].getTargetName()))) break block28;
                            bl = true;
                        }
                        ++n;
                        continue block6;
                    }
                    ++n2;
                } while (true);
                break;
            } while (true);
        }
        if (this.targetGroups.isEmpty()) return true;
        int n = 0;
        boolean bl = false;
        block8 : do {
            if (n >= arrtargets.length) {
                if (!bl) return false;
                return true;
            }
            Target[] arrtarget = arrtargets[n].getTargets();
            int n3 = 0;
            do {
                block30 : {
                    block29 : {
                        if (n3 >= arrtarget.length) break block29;
                        if (!this.targetGroups.contains((Object)GeneralName.getInstance(arrtarget[n3].getTargetGroup()))) break block30;
                        bl = true;
                    }
                    ++n;
                    continue block8;
                }
                ++n3;
            } while (true);
            break;
        } while (true);
    }

    public void setAttributeCert(X509AttributeCertificate x509AttributeCertificate) {
        this.attributeCert = x509AttributeCertificate;
    }

    public void setAttributeCertificateValid(Date date) {
        if (date != null) {
            this.attributeCertificateValid = new Date(date.getTime());
            return;
        }
        this.attributeCertificateValid = null;
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
        this.targetGroups = this.extractGeneralNames(collection);
    }

    public void setTargetNames(Collection collection) {
        this.targetNames = this.extractGeneralNames(collection);
    }
}

