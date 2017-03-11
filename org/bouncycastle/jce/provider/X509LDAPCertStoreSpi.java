package org.bouncycastle.jce.provider;

import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CRL;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.CertStoreException;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStoreSpi;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.CertificatePair;
import org.bouncycastle.jce.X509LDAPCertStoreParameters;

public class X509LDAPCertStoreSpi extends CertStoreSpi {
    private static String LDAP_PROVIDER = null;
    private static String REFERRALS_IGNORE = null;
    private static final String SEARCH_SECURITY_LEVEL = "none";
    private static final String URL_CONTEXT_PREFIX = "com.sun.jndi.url";
    private X509LDAPCertStoreParameters params;

    static {
        LDAP_PROVIDER = "com.sun.jndi.ldap.LdapCtxFactory";
        REFERRALS_IGNORE = "ignore";
    }

    public X509LDAPCertStoreSpi(CertStoreParameters certStoreParameters) {
        super(certStoreParameters);
        if (certStoreParameters instanceof X509LDAPCertStoreParameters) {
            this.params = (X509LDAPCertStoreParameters) certStoreParameters;
            return;
        }
        throw new InvalidAlgorithmParameterException(X509LDAPCertStoreSpi.class.getName() + ": parameter must be a " + X509LDAPCertStoreParameters.class.getName() + " object\n" + certStoreParameters.toString());
    }

    private Set certSubjectSerialSearch(X509CertSelector x509CertSelector, String[] strArr, String str, String str2) {
        Set hashSet = new HashSet();
        try {
            if (x509CertSelector.getSubjectAsBytes() == null && x509CertSelector.getSubjectAsString() == null && x509CertSelector.getCertificate() == null) {
                hashSet.addAll(search(str, "*", strArr));
            } else {
                String name;
                String str3 = null;
                if (x509CertSelector.getCertificate() != null) {
                    name = x509CertSelector.getCertificate().getSubjectX500Principal().getName("RFC1779");
                    str3 = x509CertSelector.getCertificate().getSerialNumber().toString();
                } else {
                    name = x509CertSelector.getSubjectAsBytes() != null ? new X500Principal(x509CertSelector.getSubjectAsBytes()).getName("RFC1779") : x509CertSelector.getSubjectAsString();
                }
                hashSet.addAll(search(str, "*" + parseDN(name, str2) + "*", strArr));
                if (!(str3 == null || this.params.getSearchForSerialNumberIn() == null)) {
                    hashSet.addAll(search(this.params.getSearchForSerialNumberIn(), "*" + str3 + "*", strArr));
                }
            }
            return hashSet;
        } catch (IOException e) {
            throw new CertStoreException("exception processing selector: " + e);
        }
    }

    private DirContext connectLDAP() {
        Hashtable properties = new Properties();
        properties.setProperty("java.naming.factory.initial", LDAP_PROVIDER);
        properties.setProperty("java.naming.batchsize", TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE);
        properties.setProperty("java.naming.provider.url", this.params.getLdapURL());
        properties.setProperty("java.naming.factory.url.pkgs", URL_CONTEXT_PREFIX);
        properties.setProperty("java.naming.referral", REFERRALS_IGNORE);
        properties.setProperty("java.naming.security.authentication", SEARCH_SECURITY_LEVEL);
        return new InitialDirContext(properties);
    }

    private Set getCACertificates(X509CertSelector x509CertSelector) {
        String[] strArr = new String[]{this.params.getCACertificateAttribute()};
        Set certSubjectSerialSearch = certSubjectSerialSearch(x509CertSelector, strArr, this.params.getLdapCACertificateAttributeName(), this.params.getCACertificateSubjectAttributeName());
        if (certSubjectSerialSearch.isEmpty()) {
            certSubjectSerialSearch.addAll(search(null, "*", strArr));
        }
        return certSubjectSerialSearch;
    }

    private Set getCrossCertificates(X509CertSelector x509CertSelector) {
        String[] strArr = new String[]{this.params.getCrossCertificateAttribute()};
        Set certSubjectSerialSearch = certSubjectSerialSearch(x509CertSelector, strArr, this.params.getLdapCrossCertificateAttributeName(), this.params.getCrossCertificateSubjectAttributeName());
        if (certSubjectSerialSearch.isEmpty()) {
            certSubjectSerialSearch.addAll(search(null, "*", strArr));
        }
        return certSubjectSerialSearch;
    }

    private Set getEndCertificates(X509CertSelector x509CertSelector) {
        return certSubjectSerialSearch(x509CertSelector, new String[]{this.params.getUserCertificateAttribute()}, this.params.getLdapUserCertificateAttributeName(), this.params.getUserCertificateSubjectAttributeName());
    }

    private String parseDN(String str, String str2) {
        String substring = str.substring(str.toLowerCase().indexOf(str2.toLowerCase()) + str2.length());
        int indexOf = substring.indexOf(44);
        if (indexOf == -1) {
            indexOf = substring.length();
        }
        while (substring.charAt(indexOf - 1) == '\\') {
            indexOf = substring.indexOf(44, indexOf + 1);
            if (indexOf == -1) {
                indexOf = substring.length();
            }
        }
        String substring2 = substring.substring(0, indexOf);
        substring2 = substring2.substring(substring2.indexOf(61) + 1);
        if (substring2.charAt(0) == ' ') {
            substring2 = substring2.substring(1);
        }
        if (substring2.startsWith("\"")) {
            substring2 = substring2.substring(1);
        }
        return substring2.endsWith("\"") ? substring2.substring(0, substring2.length() - 1) : substring2;
    }

    private Set search(String str, String str2, String[] strArr) {
        DirContext connectLDAP;
        Object e;
        DirContext dirContext;
        Throwable th;
        String str3 = str + "=" + str2;
        if (str == null) {
            str3 = null;
        }
        Set hashSet = new HashSet();
        try {
            connectLDAP = connectLDAP();
            try {
                SearchControls searchControls = new SearchControls();
                searchControls.setSearchScope(2);
                searchControls.setCountLimit(0);
                for (int i = 0; i < strArr.length; i++) {
                    String[] strArr2 = new String[]{strArr[i]};
                    searchControls.setReturningAttributes(strArr2);
                    String str4 = "(&(" + str3 + ")(" + strArr2[0] + "=*))";
                    if (str3 == null) {
                        str4 = "(" + strArr2[0] + "=*)";
                    }
                    NamingEnumeration search = connectLDAP.search(this.params.getBaseDN(), str4, searchControls);
                    while (search.hasMoreElements()) {
                        NamingEnumeration all = ((Attribute) ((SearchResult) search.next()).getAttributes().getAll().next()).getAll();
                        while (all.hasMore()) {
                            hashSet.add(all.next());
                        }
                    }
                }
                if (connectLDAP != null) {
                    try {
                        connectLDAP.close();
                    } catch (Exception e2) {
                    }
                }
                return hashSet;
            } catch (Exception e3) {
                e = e3;
                dirContext = connectLDAP;
                try {
                    throw new CertStoreException("Error getting results from LDAP directory " + e);
                } catch (Throwable th2) {
                    th = th2;
                    connectLDAP = dirContext;
                    if (connectLDAP != null) {
                        try {
                            connectLDAP.close();
                        } catch (Exception e4) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (connectLDAP != null) {
                    connectLDAP.close();
                }
                throw th;
            }
        } catch (Exception e5) {
            Exception exception = e5;
            dirContext = null;
            Exception exception2 = exception;
            throw new CertStoreException("Error getting results from LDAP directory " + e);
        } catch (Throwable th4) {
            connectLDAP = null;
            th = th4;
            if (connectLDAP != null) {
                connectLDAP.close();
            }
            throw th;
        }
    }

    public Collection engineGetCRLs(CRLSelector cRLSelector) {
        String[] strArr = new String[]{this.params.getCertificateRevocationListAttribute()};
        if (cRLSelector instanceof X509CRLSelector) {
            X509CRLSelector x509CRLSelector = (X509CRLSelector) cRLSelector;
            Collection hashSet = new HashSet();
            String ldapCertificateRevocationListAttributeName = this.params.getLdapCertificateRevocationListAttributeName();
            Set<byte[]> hashSet2 = new HashSet();
            if (x509CRLSelector.getIssuerNames() != null) {
                for (Object next : x509CRLSelector.getIssuerNames()) {
                    String parseDN;
                    if (next instanceof String) {
                        parseDN = parseDN((String) next, this.params.getCertificateRevocationListIssuerAttributeName());
                    } else {
                        parseDN = parseDN(new X500Principal((byte[]) next).getName("RFC1779"), this.params.getCertificateRevocationListIssuerAttributeName());
                    }
                    hashSet2.addAll(search(ldapCertificateRevocationListAttributeName, "*" + parseDN + "*", strArr));
                }
            } else {
                hashSet2.addAll(search(ldapCertificateRevocationListAttributeName, "*", strArr));
            }
            hashSet2.addAll(search(null, "*", strArr));
            try {
                CertificateFactory instance = CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME);
                for (byte[] byteArrayInputStream : hashSet2) {
                    CRL generateCRL = instance.generateCRL(new ByteArrayInputStream(byteArrayInputStream));
                    if (x509CRLSelector.match(generateCRL)) {
                        hashSet.add(generateCRL);
                    }
                }
                return hashSet;
            } catch (Exception e) {
                throw new CertStoreException("CRL cannot be constructed from LDAP result " + e);
            }
        }
        throw new CertStoreException("selector is not a X509CRLSelector");
    }

    public Collection engineGetCertificates(CertSelector certSelector) {
        if (certSelector instanceof X509CertSelector) {
            X509CertSelector x509CertSelector = (X509CertSelector) certSelector;
            Collection hashSet = new HashSet();
            Set<byte[]> endCertificates = getEndCertificates(x509CertSelector);
            endCertificates.addAll(getCACertificates(x509CertSelector));
            endCertificates.addAll(getCrossCertificates(x509CertSelector));
            try {
                CertificateFactory instance = CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME);
                for (byte[] bArr : endCertificates) {
                    if (!(bArr == null || bArr.length == 0)) {
                        List<byte[]> arrayList = new ArrayList();
                        arrayList.add(bArr);
                        try {
                            CertificatePair instance2 = CertificatePair.getInstance(new ASN1InputStream(bArr).readObject());
                            arrayList.clear();
                            if (instance2.getForward() != null) {
                                arrayList.add(instance2.getForward().getEncoded());
                            }
                            if (instance2.getReverse() != null) {
                                arrayList.add(instance2.getReverse().getEncoded());
                            }
                        } catch (IOException e) {
                        } catch (IllegalArgumentException e2) {
                        }
                        for (byte[] bArr2 : arrayList) {
                            try {
                                Certificate generateCertificate = instance.generateCertificate(new ByteArrayInputStream(bArr2));
                                if (x509CertSelector.match(generateCertificate)) {
                                    hashSet.add(generateCertificate);
                                }
                            } catch (Exception e3) {
                            }
                        }
                    }
                }
                return hashSet;
            } catch (Exception e4) {
                throw new CertStoreException("certificate cannot be constructed from LDAP result: " + e4);
            }
        }
        throw new CertStoreException("selector is not a X509CertSelector");
    }
}
