package org.bouncycastle.x509.extension;

import java.io.IOException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.util.Integers;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class X509ExtensionUtil {
    public static ASN1Primitive fromExtensionValue(byte[] bArr) {
        return ASN1Primitive.fromByteArray(((ASN1OctetString) ASN1Primitive.fromByteArray(bArr)).getOctets());
    }

    private static Collection getAlternativeNames(byte[] bArr) {
        if (bArr == null) {
            return Collections.EMPTY_LIST;
        }
        try {
            Collection arrayList = new ArrayList();
            Enumeration objects = ASN1Sequence.getInstance(fromExtensionValue(bArr)).getObjects();
            while (objects.hasMoreElements()) {
                GeneralName instance = GeneralName.getInstance(objects.nextElement());
                List arrayList2 = new ArrayList();
                arrayList2.add(Integers.valueOf(instance.getTagNo()));
                switch (instance.getTagNo()) {
                    case ECCurve.COORD_AFFINE /*0*/:
                    case F2m.PPB /*3*/:
                    case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                        arrayList2.add(instance.getName().toASN1Primitive());
                        break;
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    case CipherSpiExt.DECRYPT_MODE /*2*/:
                    case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                        arrayList2.add(((ASN1String) instance.getName()).getString());
                        break;
                    case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                        arrayList2.add(X500Name.getInstance(instance.getName()).toString());
                        break;
                    case ECCurve.COORD_SKEWED /*7*/:
                        arrayList2.add(ASN1OctetString.getInstance(instance.getName()).getOctets());
                        break;
                    case X509KeyUsage.keyAgreement /*8*/:
                        arrayList2.add(ASN1ObjectIdentifier.getInstance(instance.getName()).getId());
                        break;
                    default:
                        throw new IOException("Bad tag number: " + instance.getTagNo());
                }
                arrayList.add(arrayList2);
            }
            return Collections.unmodifiableCollection(arrayList);
        } catch (Exception e) {
            throw new CertificateParsingException(e.getMessage());
        }
    }

    public static Collection getIssuerAlternativeNames(X509Certificate x509Certificate) {
        return getAlternativeNames(x509Certificate.getExtensionValue(X509Extension.issuerAlternativeName.getId()));
    }

    public static Collection getSubjectAlternativeNames(X509Certificate x509Certificate) {
        return getAlternativeNames(x509Certificate.getExtensionValue(X509Extension.subjectAlternativeName.getId()));
    }
}
