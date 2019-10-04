/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.BufferedInputStream
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.io.OutputStreamWriter
 *  java.io.Writer
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.security.NoSuchProviderException
 *  java.security.cert.CertPath
 *  java.security.cert.Certificate
 *  java.security.cert.CertificateEncodingException
 *  java.security.cert.CertificateException
 *  java.security.cert.CertificateFactory
 *  java.security.cert.X509Certificate
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.Enumeration
 *  java.util.Iterator
 *  java.util.List
 *  java.util.ListIterator
 *  javax.security.auth.x500.X500Principal
 */
package org.bouncycastle.jcajce.provider.asymmetric.x509;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.NoSuchProviderException;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.pkcs.ContentInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.SignedData;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemObjectGenerator;
import org.bouncycastle.util.io.pem.PemWriter;

public class PKIXCertPath
extends CertPath {
    static final List certPathEncodings;
    private List certificates;
    private final JcaJceHelper helper = new BCJcaJceHelper();

    static {
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)"PkiPath");
        arrayList.add((Object)"PEM");
        arrayList.add((Object)"PKCS7");
        certPathEncodings = Collections.unmodifiableList((List)arrayList);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    PKIXCertPath(InputStream inputStream, String string) {
        super("X.509");
        try {
            if (string.equalsIgnoreCase("PkiPath")) {
                ASN1Primitive aSN1Primitive = new ASN1InputStream(inputStream).readObject();
                if (!(aSN1Primitive instanceof ASN1Sequence)) {
                    throw new CertificateException("input stream does not contain a ASN1 SEQUENCE while reading PkiPath encoded data to load CertPath");
                }
                Enumeration enumeration = ((ASN1Sequence)aSN1Primitive).getObjects();
                this.certificates = new ArrayList();
                CertificateFactory certificateFactory = this.helper.createCertificateFactory("X.509");
                while (enumeration.hasMoreElements()) {
                    byte[] arrby = ((ASN1Encodable)enumeration.nextElement()).toASN1Primitive().getEncoded("DER");
                    this.certificates.add(0, (Object)certificateFactory.generateCertificate((InputStream)new ByteArrayInputStream(arrby)));
                }
            } else {
                Certificate certificate;
                if (!string.equalsIgnoreCase("PKCS7") && !string.equalsIgnoreCase("PEM")) {
                    throw new CertificateException("unsupported encoding: " + string);
                }
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                this.certificates = new ArrayList();
                CertificateFactory certificateFactory = this.helper.createCertificateFactory("X.509");
                while ((certificate = certificateFactory.generateCertificate((InputStream)bufferedInputStream)) != null) {
                    this.certificates.add((Object)certificate);
                }
            }
        }
        catch (IOException iOException) {
            throw new CertificateException("IOException throw while decoding CertPath:\n" + iOException.toString());
        }
        catch (NoSuchProviderException noSuchProviderException) {
            throw new CertificateException("BouncyCastle provider not found while trying to get a CertificateFactory:\n" + noSuchProviderException.toString());
        }
        this.certificates = this.sortCerts(this.certificates);
    }

    PKIXCertPath(List list) {
        super("X.509");
        this.certificates = this.sortCerts((List)new ArrayList((Collection)list));
    }

    /*
     * Enabled aggressive block sorting
     */
    private List sortCerts(List list) {
        block24 : {
            block23 : {
                boolean bl;
                block21 : {
                    if (list.size() < 2) break block23;
                    X500Principal x500Principal = ((X509Certificate)list.get(0)).getIssuerX500Principal();
                    X500Principal x500Principal2 = x500Principal;
                    for (int i = 1; i != list.size(); ++i) {
                        if (x500Principal2.equals((Object)((X509Certificate)list.get(i)).getSubjectX500Principal())) {
                            x500Principal2 = ((X509Certificate)list.get(i)).getIssuerX500Principal();
                            continue;
                        }
                        bl = false;
                        break block21;
                    }
                    bl = true;
                }
                if (!bl) break block24;
            }
            return list;
        }
        ArrayList arrayList = new ArrayList(list.size());
        ArrayList arrayList2 = new ArrayList((Collection)list);
        int n = 0;
        do {
            X509Certificate x509Certificate;
            boolean bl;
            block22 : {
                if (n >= list.size()) {
                    if (arrayList.size() > 1) {
                        return arrayList2;
                    }
                    break;
                }
                x509Certificate = (X509Certificate)list.get(n);
                X500Principal x500Principal = x509Certificate.getSubjectX500Principal();
                for (int i = 0; i != list.size(); ++i) {
                    if (!((X509Certificate)list.get(i)).getIssuerX500Principal().equals((Object)x500Principal)) continue;
                    bl = true;
                    break block22;
                }
                bl = false;
            }
            if (!bl) {
                arrayList.add((Object)x509Certificate);
                list.remove(n);
            }
            ++n;
        } while (true);
        int n2 = 0;
        block3 : do {
            if (n2 == arrayList.size()) {
                if (list.size() > 0) {
                    return arrayList2;
                }
                return arrayList;
            }
            X500Principal x500Principal = ((X509Certificate)arrayList.get(n2)).getIssuerX500Principal();
            int n3 = 0;
            do {
                block26 : {
                    block25 : {
                        if (n3 >= list.size()) break block25;
                        X509Certificate x509Certificate = (X509Certificate)list.get(n3);
                        if (!x500Principal.equals((Object)x509Certificate.getSubjectX500Principal())) break block26;
                        arrayList.add((Object)x509Certificate);
                        list.remove(n3);
                    }
                    ++n2;
                    continue block3;
                }
                ++n3;
            } while (true);
            break;
        } while (true);
    }

    private ASN1Primitive toASN1Object(X509Certificate x509Certificate) {
        try {
            ASN1Primitive aSN1Primitive = new ASN1InputStream(x509Certificate.getEncoded()).readObject();
            return aSN1Primitive;
        }
        catch (Exception exception) {
            throw new CertificateEncodingException("Exception while encoding certificate: " + exception.toString());
        }
    }

    private byte[] toDEREncoded(ASN1Encodable aSN1Encodable) {
        try {
            byte[] arrby = aSN1Encodable.toASN1Primitive().getEncoded("DER");
            return arrby;
        }
        catch (IOException iOException) {
            throw new CertificateEncodingException("Exception thrown: " + (Object)((Object)iOException));
        }
    }

    public List getCertificates() {
        return Collections.unmodifiableList((List)new ArrayList((Collection)this.certificates));
    }

    public byte[] getEncoded() {
        Object object;
        Iterator iterator = this.getEncodings();
        if (iterator.hasNext() && (object = iterator.next()) instanceof String) {
            return this.getEncoded((String)object);
        }
        return null;
    }

    public byte[] getEncoded(String string) {
        if (string.equalsIgnoreCase("PkiPath")) {
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            ListIterator listIterator = this.certificates.listIterator(this.certificates.size());
            while (listIterator.hasPrevious()) {
                aSN1EncodableVector.add(this.toASN1Object((X509Certificate)listIterator.previous()));
            }
            return this.toDEREncoded(new DERSequence(aSN1EncodableVector));
        }
        if (string.equalsIgnoreCase("PKCS7")) {
            ContentInfo contentInfo = new ContentInfo(PKCSObjectIdentifiers.data, null);
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            for (int i = 0; i != this.certificates.size(); ++i) {
                aSN1EncodableVector.add(this.toASN1Object((X509Certificate)this.certificates.get(i)));
            }
            SignedData signedData = new SignedData(new ASN1Integer(1L), new DERSet(), contentInfo, new DERSet(aSN1EncodableVector), null, new DERSet());
            return this.toDEREncoded(new ContentInfo(PKCSObjectIdentifiers.signedData, signedData));
        }
        if (string.equalsIgnoreCase("PEM")) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PemWriter pemWriter = new PemWriter((Writer)new OutputStreamWriter((OutputStream)byteArrayOutputStream));
            int n = 0;
            do {
                if (n == this.certificates.size()) break;
                pemWriter.writeObject(new PemObject("CERTIFICATE", ((X509Certificate)this.certificates.get(n)).getEncoded()));
                ++n;
            } while (true);
            try {
                pemWriter.close();
            }
            catch (Exception exception) {
                throw new CertificateEncodingException("can't encode certificate for PEM encoded path");
            }
            return byteArrayOutputStream.toByteArray();
        }
        throw new CertificateEncodingException("unsupported encoding: " + string);
    }

    public Iterator getEncodings() {
        return certPathEncodings.iterator();
    }
}

