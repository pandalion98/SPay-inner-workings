/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.Principal
 *  java.security.cert.CertSelector
 *  java.security.cert.Certificate
 *  java.security.cert.X509Certificate
 *  java.util.ArrayList
 *  javax.security.auth.x500.X500Principal
 */
package org.bouncycastle.x509;

import java.io.IOException;
import java.math.BigInteger;
import java.security.Principal;
import java.security.cert.CertSelector;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AttCertIssuer;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.IssuerSerial;
import org.bouncycastle.asn1.x509.V2Form;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.util.Selector;

public class AttributeCertificateIssuer
implements CertSelector,
Selector {
    final ASN1Encodable form;

    public AttributeCertificateIssuer(X500Principal x500Principal) {
        this(new X509Principal(x500Principal.getEncoded()));
    }

    public AttributeCertificateIssuer(AttCertIssuer attCertIssuer) {
        this.form = attCertIssuer.getIssuer();
    }

    public AttributeCertificateIssuer(X509Principal x509Principal) {
        this.form = new V2Form(GeneralNames.getInstance(new DERSequence(new GeneralName(x509Principal))));
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private Object[] getNames() {
        GeneralNames generalNames = this.form instanceof V2Form ? ((V2Form)this.form).getIssuerName() : (GeneralNames)this.form;
        GeneralName[] arrgeneralName = generalNames.getNames();
        ArrayList arrayList = new ArrayList(arrgeneralName.length);
        int n = 0;
        while (n != arrgeneralName.length) {
            if (arrgeneralName[n].getTagNo() == 4) {
                arrayList.add((Object)new X500Principal(arrgeneralName[n].getName().toASN1Primitive().getEncoded()));
            }
            ++n;
        }
        return arrayList.toArray(new Object[arrayList.size()]);
        catch (IOException iOException) {
            throw new RuntimeException("badly formed Name object");
        }
    }

    private boolean matchesDN(X500Principal x500Principal, GeneralNames generalNames) {
        GeneralName[] arrgeneralName = generalNames.getNames();
        int n = 0;
        do {
            block5 : {
                boolean bl;
                block6 : {
                    int n2 = arrgeneralName.length;
                    bl = false;
                    if (n == n2) break block6;
                    GeneralName generalName = arrgeneralName[n];
                    if (generalName.getTagNo() != 4) break block5;
                    try {
                        boolean bl2 = new X500Principal(generalName.getName().toASN1Primitive().getEncoded()).equals((Object)x500Principal);
                        if (!bl2) break block5;
                        bl = true;
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                return bl;
            }
            ++n;
        } while (true);
    }

    @Override
    public Object clone() {
        return new AttributeCertificateIssuer(AttCertIssuer.getInstance(this.form));
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof AttributeCertificateIssuer)) {
            return false;
        }
        AttributeCertificateIssuer attributeCertificateIssuer = (AttributeCertificateIssuer)object;
        return this.form.equals((Object)attributeCertificateIssuer.form);
    }

    public Principal[] getPrincipals() {
        Object[] arrobject = this.getNames();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i != arrobject.length; ++i) {
            if (!(arrobject[i] instanceof Principal)) continue;
            arrayList.add(arrobject[i]);
        }
        return (Principal[])arrayList.toArray((Object[])new Principal[arrayList.size()]);
    }

    public int hashCode() {
        return this.form.hashCode();
    }

    public boolean match(Object object) {
        if (!(object instanceof X509Certificate)) {
            return false;
        }
        return this.match((Certificate)object);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean match(Certificate certificate) {
        if (!(certificate instanceof X509Certificate)) {
            return false;
        }
        X509Certificate x509Certificate = (X509Certificate)certificate;
        if (this.form instanceof V2Form) {
            V2Form v2Form = (V2Form)this.form;
            if (v2Form.getBaseCertificateID() != null) {
                if (!v2Form.getBaseCertificateID().getSerial().getValue().equals((Object)x509Certificate.getSerialNumber())) return false;
                if (!this.matchesDN(x509Certificate.getIssuerX500Principal(), v2Form.getBaseCertificateID().getIssuer())) return false;
                return true;
            }
            GeneralNames generalNames = v2Form.getIssuerName();
            if (!this.matchesDN(x509Certificate.getSubjectX500Principal(), generalNames)) return false;
            return true;
        }
        GeneralNames generalNames = (GeneralNames)this.form;
        if (!this.matchesDN(x509Certificate.getSubjectX500Principal(), generalNames)) return false;
        return true;
    }
}

