/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.security.cert.CertificateParsingException
 *  java.security.cert.X509Certificate
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.Enumeration
 *  java.util.List
 */
package org.bouncycastle.x509.extension;

import java.io.IOException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.util.Integers;

public class X509ExtensionUtil {
    public static ASN1Primitive fromExtensionValue(byte[] arrby) {
        return ASN1Primitive.fromByteArray(((ASN1OctetString)ASN1Primitive.fromByteArray(arrby)).getOctets());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Collection getAlternativeNames(byte[] arrby) {
        if (arrby == null) {
            return Collections.EMPTY_LIST;
        }
        try {
            ArrayList arrayList = new ArrayList();
            Enumeration enumeration = DERSequence.getInstance(X509ExtensionUtil.fromExtensionValue(arrby)).getObjects();
            while (enumeration.hasMoreElements()) {
                GeneralName generalName = GeneralName.getInstance(enumeration.nextElement());
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add((Object)Integers.valueOf(generalName.getTagNo()));
                switch (generalName.getTagNo()) {
                    default: {
                        throw new IOException("Bad tag number: " + generalName.getTagNo());
                    }
                    case 0: 
                    case 3: 
                    case 5: {
                        arrayList2.add((Object)generalName.getName().toASN1Primitive());
                        break;
                    }
                    case 4: {
                        arrayList2.add((Object)X500Name.getInstance(generalName.getName()).toString());
                        break;
                    }
                    case 1: 
                    case 2: 
                    case 6: {
                        arrayList2.add((Object)((ASN1String)((Object)generalName.getName())).getString());
                        break;
                    }
                    case 8: {
                        arrayList2.add((Object)ASN1ObjectIdentifier.getInstance(generalName.getName()).getId());
                        break;
                    }
                    case 7: {
                        arrayList2.add((Object)DEROctetString.getInstance(generalName.getName()).getOctets());
                    }
                }
                arrayList.add((Object)arrayList2);
            }
            return Collections.unmodifiableCollection((Collection)arrayList);
        }
        catch (Exception exception) {
            throw new CertificateParsingException(exception.getMessage());
        }
    }

    public static Collection getIssuerAlternativeNames(X509Certificate x509Certificate) {
        return X509ExtensionUtil.getAlternativeNames(x509Certificate.getExtensionValue(X509Extension.issuerAlternativeName.getId()));
    }

    public static Collection getSubjectAlternativeNames(X509Certificate x509Certificate) {
        return X509ExtensionUtil.getAlternativeNames(x509Certificate.getExtensionValue(X509Extension.subjectAlternativeName.getId()));
    }
}

