package android.net.wifi.hs20.cert;

import android.net.ProxyInfo;
import android.net.wifi.hs20.WifiHs20Manager;
import android.util.Log;
import com.android.org.bouncycastle.asn1.ASN1Encodable;
import com.android.org.bouncycastle.asn1.ASN1InputStream;
import com.android.org.bouncycastle.asn1.ASN1Object;
import com.android.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.android.org.bouncycastle.asn1.ASN1Primitive;
import com.android.org.bouncycastle.asn1.ASN1Sequence;
import com.android.org.bouncycastle.asn1.DERIA5String;
import com.android.org.bouncycastle.asn1.DERObjectIdentifier;
import com.android.org.bouncycastle.asn1.DEROctetString;
import com.android.org.bouncycastle.asn1.DERSequence;
import com.android.org.bouncycastle.asn1.DERTaggedObject;
import com.android.org.bouncycastle.asn1.DERUTF8String;
import com.android.org.bouncycastle.asn1.util.ASN1Dump;
import com.android.org.bouncycastle.asn1.x500.RDN;
import com.android.org.bouncycastle.asn1.x500.X500Name;
import com.android.org.bouncycastle.asn1.x500.style.BCStyle;
import com.android.org.bouncycastle.asn1.x500.style.IETFUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.security.auth.x500.X500Principal;

public class HotspotDMCertificateValidation {
    private static final String CLIENT_AUTH = "1.3.6.1.5.5.7.3.2";
    private static final String GEN_NAMES_TAG = "2";
    private static final String LOGO_TYPE_EXTN = "1.3.6.1.5.5.7.1.12";
    private static final String TAG = "HotspotDMCertificateValidation";
    private String HOTSPOT_2_0_FREINDLY_NAME_OID = "1.3.6.1.4.1.40808.1.1.1";
    private String OSU_SERVER = "OSU";
    private int OTHER_NAME_TAG = 0;
    private String iconHash;
    private X509Certificate[] mServerCertificate;
    private HotspotDMValidationParameters mvalidationParameters;

    public HotspotDMCertificateValidation(X509Certificate[] serverCertificate, HotspotDMValidationParameters validationParameters) {
        this.mServerCertificate = serverCertificate;
        validationParameters.fqdnName = validationParameters.serverUri;
        this.mvalidationParameters = validationParameters;
    }

    private void printCertProp() throws Exception {
        Log.i(TAG, "printCertProp:length of the certs :" + this.mServerCertificate.length);
        for (X509Certificate issuerDN : this.mServerCertificate) {
            Log.i(TAG, "printCertProp:cert issuer :" + issuerDN.getIssuerDN().toString());
        }
    }

    public boolean validate() {
        boolean validationFlag = false;
        Log.i(TAG, "validate:");
        try {
            printCertProp();
            if (!extendedValidation(this.mServerCertificate)) {
                Log.e(TAG, "validate: rfc5280Validation failed");
            } else if (attributeValidation(this.mServerCertificate)) {
                validationFlag = true;
            } else {
                Log.e(TAG, "validate: attributeValidation failed");
            }
            return validationFlag;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean extendedValidation(X509Certificate[] mServerCertificate) throws Exception {
        boolean extendedValidationFlag = true;
        try {
            X509Certificate ServerRootCertificate = mServerCertificate[0];
            ServerRootCertificate.getExtendedKeyUsage();
            ServerRootCertificate.getIssuerAlternativeNames();
            ServerRootCertificate.getIssuerDN();
            boolean[] keyUsage = ServerRootCertificate.getKeyUsage();
            Set<String> noncritical_Extensions = ServerRootCertificate.getNonCriticalExtensionOIDs();
            keyUsageCheck(keyUsage);
            if (ServerRootCertificate.getSubjectAlternativeNames() != null) {
                Log.i(TAG, "rfc5280Validation:Subject-alt-name: " + ServerRootCertificate.getSubjectAlternativeNames().toString());
                extendedValidationFlag = subAltNameMatch(ServerRootCertificate.getSubjectAlternativeNames());
                if (!this.mvalidationParameters.sim && this.OSU_SERVER.equals(this.mvalidationParameters.serverType) && extendedValidationFlag) {
                    extendedValidationFlag = isLanguageAndNamesMatched(ServerRootCertificate);
                }
            }
            Log.i(TAG, "rfc5280Validation:mvalidationParameters.serverType: " + this.mvalidationParameters.serverType + " SubjDnsType: " + false + " rfc5280ValidationFlag: " + extendedValidationFlag);
            if (noncritical_Extensions != null && extendedValidationFlag) {
                Log.i(TAG, "rfc5280Validation:noncritical_extensions: " + noncritical_Extensions);
                if (!this.mvalidationParameters.sim && this.OSU_SERVER.equals(this.mvalidationParameters.serverType)) {
                    if (noncritical_Extensions.contains(LOGO_TYPE_EXTN) && extendedValidationFlag) {
                        extendedValidationFlag = isLogoTypeExtensionMatched(ServerRootCertificate);
                        Log.i(TAG, "rfc5280Validation:logoMatch: " + extendedValidationFlag);
                    } else {
                        extendedValidationFlag = false;
                        Log.e(TAG, "rfc5280Validation:Cert doesnt contain Logotype  extension");
                    }
                }
            }
            Log.i(TAG, "certificate_version = " + ServerRootCertificate.getVersion());
            if (ServerRootCertificate.getVersion() != 3 && extendedValidationFlag) {
                extendedValidationFlag = false;
                Log.e(TAG, "rfc5280Validation:ServerRootCertificate version incorrect");
            }
        } catch (Exception e) {
            Log.e(TAG, "rfc5280Validation:Certificate validation failed ");
            e.printStackTrace();
        }
        Log.i(TAG, "rfc5280Validation:final rfc5280ValidationFlag" + extendedValidationFlag);
        return extendedValidationFlag;
    }

    public boolean attributeValidation(X509Certificate[] mServerCertificate) throws CertificateException, IOException {
        List<String> extendedKeys = mServerCertificate[0].getExtendedKeyUsage();
        Log.e(TAG, "attributeValidation:extendedKeys:" + extendedKeys);
        if (extendedKeys == null) {
            return false;
        }
        if (!extendedKeys.contains(CLIENT_AUTH)) {
            return true;
        }
        Log.e(TAG, "attributeValidation:It contains Clientauth attribute ABORT_OSU");
        return false;
    }

    public boolean subAltNameMatch(Collection<List<?>> SubaltList) throws Exception {
        Log.i(TAG, "subAltNameMatch:");
        boolean subAltMatchFlag = true;
        if (this.mvalidationParameters.serverUri != null) {
            URI mserverUri = new URI(this.mvalidationParameters.serverUri);
            boolean match = false;
            for (List it : SubaltList) {
                Iterator<?> OidIterator = it.iterator();
                while (OidIterator.hasNext()) {
                    String genNames = OidIterator.next().toString();
                    Log.i(TAG, "subAltNameMatch:genNames " + genNames);
                    if (GEN_NAMES_TAG.equals(genNames) && !match) {
                        String dnsName = OidIterator.next().toString();
                        Log.i(TAG, "subAltNameMatch:dnsName from certificate: " + dnsName);
                        if (dnsName.equals(mserverUri.getHost())) {
                            match = true;
                            subAltMatchFlag = true;
                            Log.i(TAG, "subAltNameMatch:Domain Name or UrlWnmframe is suffix match for subject at name");
                        } else {
                            subAltMatchFlag = false;
                            Log.i(TAG, "subAltNameMatch:dnsName " + dnsName);
                            Log.e(TAG, "subAltNameMatch:mserverUri.getHost() " + mserverUri.getHost());
                            Log.e(TAG, "subAltNameMatch:Domain Name or UrlWnmframe is not suffix match for subject at name");
                        }
                    }
                }
            }
            Log.i(TAG, "subAltNameMatch:rfcflag:" + subAltMatchFlag);
            return subAltMatchFlag;
        }
        Log.i(TAG, "subAltNameMatch:server uri is null");
        return false;
    }

    public boolean keyUsageCheck(boolean[] keyUsage) {
        if (keyUsage != null && keyUsage[0] && keyUsage[7]) {
            Log.i(TAG, "keyUsageCheck;have both encipherment and signing");
            return true;
        }
        Log.i(TAG, "keyUsageCheck:either of encipherment and signing is missing");
        return false;
    }

    public boolean subCnMatch(X509Certificate ServerRootCertificate) {
        X500Principal principal = (X500Principal) ServerRootCertificate.getSubjectDN();
        String CN = null;
        if (principal == null) {
            return true;
        }
        RDN cn = new X500Name(principal.getName()).getRDNs(BCStyle.CN)[0];
        if (cn != null) {
            CN = IETFUtils.valueToString(cn.getFirst().getValue());
        }
        if (CN == null || !CN.endsWith(this.mvalidationParameters.fqdnName) || this.mvalidationParameters.fqdnName == null) {
            Log.i(TAG, "subCnMatch:Comman name:" + CN);
            Log.i(TAG, "subCnMatch:mvalidationParameters.fqdnName:" + this.mvalidationParameters.fqdnName);
            Log.i(TAG, "subCnMatch:mvalidationParameters.fqdnName is Not suffix  match for subject names");
            return false;
        }
        Log.i(TAG, "subCnMatch:fqdnName is suffix match for  CN portion subject names");
        return true;
    }

    public String getIconHash(String algoName) throws Exception {
        Exception e;
        File file;
        Throwable th;
        byte[] IconBytes = new byte[1024];
        String hexString = null;
        FileInputStream icon_stream = null;
        try {
            if (this.mvalidationParameters.iconPath != null) {
                File icon_file = new File(WifiHs20Manager.HS20_ICON_STORE_PATH, this.mvalidationParameters.iconPath);
                try {
                    if (icon_file.exists()) {
                        MessageDigest iconDigest = MessageDigest.getInstance(algoName);
                        ByteArrayOutputStream IconByteOutStream = new ByteArrayOutputStream();
                        FileInputStream icon_stream2 = new FileInputStream(icon_file);
                        while (true) {
                            try {
                                int bytesRead = icon_stream2.read(IconBytes);
                                if (bytesRead == -1) {
                                    break;
                                }
                                IconByteOutStream.write(IconBytes, 0, bytesRead);
                            } catch (Exception e2) {
                                e = e2;
                                icon_stream = icon_stream2;
                                file = icon_file;
                            } catch (Throwable th2) {
                                th = th2;
                                icon_stream = icon_stream2;
                                file = icon_file;
                            }
                        }
                        icon_stream2.close();
                        iconDigest.update(IconByteOutStream.toByteArray());
                        IconByteOutStream.close();
                        hexString = hexify(iconDigest.digest());
                        icon_stream = icon_stream2;
                        file = icon_file;
                    } else {
                        if (icon_stream != null) {
                            try {
                                icon_stream.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        file = icon_file;
                        return null;
                    }
                } catch (Exception e4) {
                    e = e4;
                    file = icon_file;
                    try {
                        Log.i(TAG, "getIconHash:exception occured:" + e);
                        e.printStackTrace();
                        if (icon_stream != null) {
                            try {
                                icon_stream.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                            }
                        }
                        return hexString;
                    } catch (Throwable th3) {
                        th = th3;
                        if (icon_stream != null) {
                            try {
                                icon_stream.close();
                            } catch (IOException e322) {
                                e322.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    file = icon_file;
                    if (icon_stream != null) {
                        icon_stream.close();
                    }
                    throw th;
                }
            }
            if (icon_stream != null) {
                try {
                    icon_stream.close();
                } catch (IOException e3222) {
                    e3222.printStackTrace();
                }
            }
        } catch (Exception e5) {
            e = e5;
            Log.i(TAG, "getIconHash:exception occured:" + e);
            e.printStackTrace();
            if (icon_stream != null) {
                icon_stream.close();
            }
            return hexString;
        }
        return hexString;
    }

    public static String hexify(byte[] bytes) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            buf.append(hexDigits[(bytes[i] & 240) >> 4]);
            buf.append(hexDigits[bytes[i] & 15]);
        }
        return buf.toString();
    }

    private boolean isLogoTypeExtensionMatched(X509Certificate x509Cert) throws Exception {
        this.iconHash = getIconHash("SHA256");
        if (this.iconHash == null) {
            return true;
        }
        boolean result = true;
        try {
            byte[] logoType = x509Cert.getExtensionValue(LOGO_TYPE_EXTN);
            if (logoType != null) {
                ASN1InputStream asn1_is = new ASN1InputStream((ByteArrayInputStream) ((DEROctetString) new ASN1InputStream(new ByteArrayInputStream(logoType)).readObject()).getOctetStream());
                ASN1Sequence logoTypeExt = (ASN1Sequence) asn1_is.readObject();
                asn1_is.close();
                Log.d(TAG, "LogotypeExtn:" + logoTypeExt.toString());
                Enumeration LogotypeExtnSequence = logoTypeExt.getObjects();
                while (LogotypeExtnSequence.hasMoreElements()) {
                    DERTaggedObject LogotypeExtnTaggedObj = (DERTaggedObject) ((ASN1Encodable) LogotypeExtnSequence.nextElement()).toASN1Primitive();
                    Log.d(TAG, "LogotypeExtnTaggedObj:" + LogotypeExtnTaggedObj.toString());
                    Log.d(TAG, "LogotypeExtnTaggedObj CHOICE: " + LogotypeExtnTaggedObj.getTagNo());
                    if (LogotypeExtnTaggedObj.getTagNo() == 0) {
                        Log.d(TAG, ProxyInfo.LOCAL_EXCL_LIST);
                        DERSequence CommunityLogos = (DERSequence) LogotypeExtnTaggedObj.getObject();
                        Log.d(TAG, "communityLogos:" + CommunityLogos.toString());
                        Enumeration CommunityLogosEnu = CommunityLogos.getObjects();
                        while (CommunityLogosEnu.hasMoreElements()) {
                            result = true;
                            DERTaggedObject CommunityLogosTaggedObj = (DERTaggedObject) ((ASN1Encodable) CommunityLogosEnu.nextElement()).toASN1Primitive();
                            Log.d(TAG, "CommunityLogosTaggedObj CHOICE: " + CommunityLogosTaggedObj.getTagNo());
                            if (CommunityLogosTaggedObj.getTagNo() == 0) {
                                DERSequence LogotypeData = (DERSequence) CommunityLogosTaggedObj.getObject();
                                Log.d(TAG, "LogotypeImage:" + LogotypeData.toString());
                                Enumeration LogotypeDataEnu = LogotypeData.getObjects();
                                while (LogotypeDataEnu.hasMoreElements()) {
                                    DERSequence LogotypeImage = (DERSequence) ((ASN1Encodable) LogotypeDataEnu.nextElement()).toASN1Primitive();
                                    Log.d(TAG, "LogotypeImage:" + LogotypeImage.toString());
                                    Enumeration LogotypeImageEnu = LogotypeImage.getObjects();
                                    while (LogotypeImageEnu.hasMoreElements()) {
                                        DERSequence imageDetails = (DERSequence) ((ASN1Encodable) LogotypeImageEnu.nextElement()).toASN1Primitive();
                                        printImageInfo((DERSequence) ((ASN1Encodable) LogotypeImageEnu.nextElement()).toASN1Primitive());
                                        DEROctetString hashValue = getHashFromSeq(getHashSeqFromLogoTypeDetails(imageDetails).getObjects());
                                        byte[] hashValueOctetString = hashValue.getOctets();
                                        String certIconHash = hashValue.toString().substring(1);
                                        Log.d(TAG, "hashValue String:" + certIconHash);
                                        if (this.iconHash.equals(certIconHash)) {
                                            Log.d(TAG, "Icon hash match");
                                            return true;
                                        }
                                        Log.d(TAG, "Icon hash not match");
                                        result = false;
                                    }
                                }
                                continue;
                            }
                        }
                        continue;
                    }
                }
                Log.d(TAG, "LogotypeExtn parsing done");
                return result;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return false;
    }

    private DEROctetString getHashFromSeq(Enumeration enu) {
        DERSequence HashAlgAndValue = (DERSequence) ((ASN1Encodable) enu.nextElement()).toASN1Primitive();
        Log.d(TAG, "HashAlgAndValue:" + HashAlgAndValue.toString());
        enu = HashAlgAndValue.getObjects();
        DERSequence hashAlg = (DERSequence) ((ASN1Encodable) enu.nextElement()).toASN1Primitive();
        Log.d(TAG, "hashAlg:" + hashAlg.toString());
        DEROctetString hashValue = (DEROctetString) ((ASN1Encodable) enu.nextElement()).toASN1Primitive();
        Log.d(TAG, "hashValue:" + hashValue.toString());
        Log.d(TAG, "AlgorithmIdentifier:" + ((DERObjectIdentifier) ((ASN1Encodable) hashAlg.getObjects().nextElement()).toASN1Primitive()).toString());
        return hashValue;
    }

    private DERSequence getHashSeqFromLogoTypeDetails(DERSequence imageDetails) {
        Log.d(TAG, "imageDetails:" + imageDetails.toString());
        Enumeration enu = imageDetails.getObjects();
        Log.d(TAG, "mediaType:" + ((DERIA5String) ((ASN1Encodable) enu.nextElement()).toASN1Primitive()).toString());
        DERSequence logotypeHash = (DERSequence) ((ASN1Encodable) enu.nextElement()).toASN1Primitive();
        Log.d(TAG, "logotypeHash:" + logotypeHash.toString());
        DERSequence logotypeURI = (DERSequence) ((ASN1Encodable) enu.nextElement()).toASN1Primitive();
        Log.d(TAG, "logotypeURI:" + logotypeURI.toString());
        Log.d(TAG, "logotypeURIStr:" + ((DERIA5String) ((ASN1Encodable) logotypeURI.getObjects().nextElement()).toASN1Primitive()).toString());
        Log.d(TAG, "filename : (" + filenameFromURI(logotypeURI.toString()) + ")");
        return logotypeHash;
    }

    private void printImageInfo(DERSequence imageInfo) {
        Log.d(TAG, "imageInfo:" + imageInfo.toString());
        Enumeration enu = imageInfo.getObjects();
        while (enu.hasMoreElements()) {
            ASN1Object info = ((ASN1Encodable) enu.nextElement()).toASN1Primitive();
            Log.d(TAG, "object:" + info.toString());
            printLanGuageCode(info);
        }
    }

    private void printLanGuageCode(ASN1Object info) {
        if ((info instanceof DERTaggedObject) && ((DERTaggedObject) info).getTagNo() == 4) {
            String languageCode = null;
            try {
                languageCode = new String(((DEROctetString) ((DERTaggedObject) info).getObject()).getEncoded()).substring(2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "imageInfo language code:" + languageCode);
        }
    }

    private String filenameFromURI(String uri) {
        return uri.substring(uri.lastIndexOf("/") + 1);
    }

    private boolean isLanguageAndNamesMatched(X509Certificate x509Cert) {
        if (this.mvalidationParameters.friendlyNames == null) {
            return false;
        }
        try {
            Collection<List> c = x509Cert.getSubjectAlternativeNames();
            if (c != null) {
                for (List gn : c) {
                    if (((Integer) gn.get(this.OTHER_NAME_TAG)).intValue() == 0) {
                        Log.d(TAG, "SubjectAltName OtherName:" + gn.get(1).toString());
                        ASN1InputStream asn1_is = new ASN1InputStream(new ByteArrayInputStream((byte[]) gn.get(1)));
                        ASN1Primitive asn1Obj = asn1_is.readObject();
                        Log.d(TAG, ASN1Dump.dumpAsString(asn1Obj, true));
                        Enumeration enu = ((ASN1Sequence) ((DERTaggedObject) asn1Obj).getObject()).getObjects();
                        ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) ((ASN1Encodable) enu.nextElement()).toASN1Primitive();
                        Log.d(TAG, "    OID:" + oid.toString());
                        if (this.HOTSPOT_2_0_FREINDLY_NAME_OID.equals(oid.toString())) {
                            String spLangFriendlyName;
                            DERTaggedObject dertagObj = (DERTaggedObject) ((ASN1Encodable) enu.nextElement()).toASN1Primitive();
                            if (dertagObj.getObject() instanceof DERUTF8String) {
                                spLangFriendlyName = ((DERUTF8String) dertagObj.getObject()).toString();
                            } else {
                                spLangFriendlyName = ((DEROctetString) dertagObj.getObject()).toString();
                            }
                            Log.d(TAG, "language code and friendly name:" + spLangFriendlyName.toString());
                            String friendlyName = (String) this.mvalidationParameters.friendlyNames.get(spLangFriendlyName.substring(0, 3));
                            if (friendlyName != null) {
                                Log.d(TAG, "Language code match");
                                Log.d(TAG, "mvalidationParameters.friendlyName = " + friendlyName);
                                if (spLangFriendlyName.substring(3).equals(friendlyName)) {
                                    Log.d(TAG, "OSU friendly name match");
                                    asn1_is.close();
                                    return true;
                                }
                                Log.d(TAG, "OSU friendly name not match");
                            } else {
                                Log.d(TAG, "Language code not match");
                            }
                        } else {
                            continue;
                        }
                    }
                }
                Log.d(TAG, "Subject Alternative Names:" + c.toString());
            }
        } catch (CertificateParsingException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return false;
    }
}
