/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.MessageDigest
 *  java.security.Principal
 *  java.security.PublicKey
 *  java.security.cert.CertSelector
 *  java.security.cert.Certificate
 *  java.security.cert.CertificateEncodingException
 *  java.security.cert.CertificateParsingException
 *  java.security.cert.X509Certificate
 *  java.util.ArrayList
 *  javax.security.auth.x500.X500Principal
 */
package org.bouncycastle.x509;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.CertSelector;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.Holder;
import org.bouncycastle.asn1.x509.IssuerSerial;
import org.bouncycastle.asn1.x509.ObjectDigestInfo;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Selector;
import org.bouncycastle.x509.X509Util;

public class AttributeCertificateHolder
implements CertSelector,
Selector {
    final Holder holder;

    public AttributeCertificateHolder(int n, String string, String string2, byte[] arrby) {
        this.holder = new Holder(new ObjectDigestInfo(n, new ASN1ObjectIdentifier(string2), new AlgorithmIdentifier(string), Arrays.clone(arrby)));
    }

    public AttributeCertificateHolder(X509Certificate x509Certificate) {
        X509Principal x509Principal;
        try {
            x509Principal = PrincipalUtil.getIssuerX509Principal(x509Certificate);
        }
        catch (Exception exception) {
            throw new CertificateParsingException(exception.getMessage());
        }
        this.holder = new Holder(new IssuerSerial(this.generateGeneralNames(x509Principal), new ASN1Integer(x509Certificate.getSerialNumber())));
    }

    public AttributeCertificateHolder(X500Principal x500Principal) {
        this(X509Util.convertPrincipal(x500Principal));
    }

    public AttributeCertificateHolder(X500Principal x500Principal, BigInteger bigInteger) {
        this(X509Util.convertPrincipal(x500Principal), bigInteger);
    }

    AttributeCertificateHolder(ASN1Sequence aSN1Sequence) {
        this.holder = Holder.getInstance(aSN1Sequence);
    }

    public AttributeCertificateHolder(X509Principal x509Principal) {
        this.holder = new Holder(this.generateGeneralNames(x509Principal));
    }

    public AttributeCertificateHolder(X509Principal x509Principal, BigInteger bigInteger) {
        this.holder = new Holder(new IssuerSerial(GeneralNames.getInstance(new DERSequence(new GeneralName(x509Principal))), new ASN1Integer(bigInteger)));
    }

    private GeneralNames generateGeneralNames(X509Principal x509Principal) {
        return GeneralNames.getInstance(new DERSequence(new GeneralName(x509Principal)));
    }

    private Object[] getNames(GeneralName[] arrgeneralName) {
        ArrayList arrayList = new ArrayList(arrgeneralName.length);
        for (int i = 0; i != arrgeneralName.length; ++i) {
            if (arrgeneralName[i].getTagNo() != 4) continue;
            try {
                arrayList.add((Object)new X500Principal(arrgeneralName[i].getName().toASN1Primitive().getEncoded()));
            }
            catch (IOException iOException) {
                throw new RuntimeException("badly formed Name object");
            }
        }
        return arrayList.toArray(new Object[arrayList.size()]);
    }

    private Principal[] getPrincipals(GeneralNames generalNames) {
        Object[] arrobject = this.getNames(generalNames.getNames());
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i != arrobject.length; ++i) {
            if (!(arrobject[i] instanceof Principal)) continue;
            arrayList.add(arrobject[i]);
        }
        return (Principal[])arrayList.toArray((Object[])new Principal[arrayList.size()]);
    }

    private boolean matchesDN(X509Principal x509Principal, GeneralNames generalNames) {
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
                        boolean bl2 = new X509Principal(generalName.getName().toASN1Primitive().getEncoded()).equals(x509Principal);
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
        return new AttributeCertificateHolder((ASN1Sequence)this.holder.toASN1Object());
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof AttributeCertificateHolder)) {
            return false;
        }
        AttributeCertificateHolder attributeCertificateHolder = (AttributeCertificateHolder)object;
        return this.holder.equals(attributeCertificateHolder.holder);
    }

    public String getDigestAlgorithm() {
        if (this.holder.getObjectDigestInfo() != null) {
            return this.holder.getObjectDigestInfo().getDigestAlgorithm().getObjectId().getId();
        }
        return null;
    }

    public int getDigestedObjectType() {
        if (this.holder.getObjectDigestInfo() != null) {
            return this.holder.getObjectDigestInfo().getDigestedObjectType().getValue().intValue();
        }
        return -1;
    }

    public Principal[] getEntityNames() {
        if (this.holder.getEntityName() != null) {
            return this.getPrincipals(this.holder.getEntityName());
        }
        return null;
    }

    public Principal[] getIssuer() {
        if (this.holder.getBaseCertificateID() != null) {
            return this.getPrincipals(this.holder.getBaseCertificateID().getIssuer());
        }
        return null;
    }

    public byte[] getObjectDigest() {
        if (this.holder.getObjectDigestInfo() != null) {
            return this.holder.getObjectDigestInfo().getObjectDigest().getBytes();
        }
        return null;
    }

    public String getOtherObjectTypeID() {
        if (this.holder.getObjectDigestInfo() != null) {
            this.holder.getObjectDigestInfo().getOtherObjectTypeID().getId();
        }
        return null;
    }

    public BigInteger getSerialNumber() {
        if (this.holder.getBaseCertificateID() != null) {
            return this.holder.getBaseCertificateID().getSerial().getValue();
        }
        return null;
    }

    public int hashCode() {
        return this.holder.hashCode();
    }

    public boolean match(Object object) {
        if (!(object instanceof X509Certificate)) {
            return false;
        }
        return this.match((Certificate)object);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean match(Certificate certificate) {
        if (!(certificate instanceof X509Certificate)) {
            return false;
        }
        X509Certificate x509Certificate = (X509Certificate)certificate;
        try {
            MessageDigest messageDigest;
            if (this.holder.getBaseCertificateID() != null) {
                if (!this.holder.getBaseCertificateID().getSerial().getValue().equals((Object)x509Certificate.getSerialNumber())) return false;
                if (!this.matchesDN(PrincipalUtil.getIssuerX509Principal(x509Certificate), this.holder.getBaseCertificateID().getIssuer())) return false;
                return true;
            }
            if (this.holder.getEntityName() != null && this.matchesDN(PrincipalUtil.getSubjectX509Principal(x509Certificate), this.holder.getEntityName())) {
                return true;
            }
            ObjectDigestInfo objectDigestInfo = this.holder.getObjectDigestInfo();
            if (objectDigestInfo == null) return false;
            try {
                messageDigest = MessageDigest.getInstance((String)this.getDigestAlgorithm(), (String)"BC");
            }
            catch (Exception exception) {
                return false;
            }
            switch (this.getDigestedObjectType()) {
                case 0: {
                    messageDigest.update(certificate.getPublicKey().getEncoded());
                }
                default: {
                    break;
                }
                case 1: {
                    messageDigest.update(certificate.getEncoded());
                }
            }
            if (Arrays.areEqual(messageDigest.digest(), this.getObjectDigest())) return false;
            return false;
        }
        catch (CertificateEncodingException certificateEncodingException) {
            return false;
        }
    }
}

