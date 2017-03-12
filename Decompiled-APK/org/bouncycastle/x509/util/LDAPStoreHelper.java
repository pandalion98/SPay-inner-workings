package org.bouncycastle.x509.util;

import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.CertificatePair;
import org.bouncycastle.jce.X509LDAPCertStoreParameters;
import org.bouncycastle.jce.provider.X509AttrCertParser;
import org.bouncycastle.jce.provider.X509CRLParser;
import org.bouncycastle.jce.provider.X509CertPairParser;
import org.bouncycastle.jce.provider.X509CertParser;
import org.bouncycastle.util.StoreException;
import org.bouncycastle.x509.X509AttributeCertStoreSelector;
import org.bouncycastle.x509.X509AttributeCertificate;
import org.bouncycastle.x509.X509CRLStoreSelector;
import org.bouncycastle.x509.X509CertPairStoreSelector;
import org.bouncycastle.x509.X509CertStoreSelector;
import org.bouncycastle.x509.X509CertificatePair;

public class LDAPStoreHelper {
    private static String LDAP_PROVIDER = null;
    private static String REFERRALS_IGNORE = null;
    private static final String SEARCH_SECURITY_LEVEL = "none";
    private static final String URL_CONTEXT_PREFIX = "com.sun.jndi.url";
    private static int cacheSize;
    private static long lifeTime;
    private Map cacheMap;
    private X509LDAPCertStoreParameters params;

    static {
        LDAP_PROVIDER = "com.sun.jndi.ldap.LdapCtxFactory";
        REFERRALS_IGNORE = "ignore";
        cacheSize = 32;
        lifeTime = 60000;
    }

    public LDAPStoreHelper(X509LDAPCertStoreParameters x509LDAPCertStoreParameters) {
        this.cacheMap = new HashMap(cacheSize);
        this.params = x509LDAPCertStoreParameters;
    }

    private synchronized void addToCache(String str, List list) {
        Date date = new Date(System.currentTimeMillis());
        List arrayList = new ArrayList();
        arrayList.add(date);
        arrayList.add(list);
        if (this.cacheMap.containsKey(str)) {
            this.cacheMap.put(str, arrayList);
        } else {
            if (this.cacheMap.size() >= cacheSize) {
                long time = date.getTime();
                Object obj = null;
                for (Entry entry : this.cacheMap.entrySet()) {
                    Object key;
                    long j;
                    long time2 = ((Date) ((List) entry.getValue()).get(0)).getTime();
                    if (time2 < time) {
                        key = entry.getKey();
                        j = time2;
                    } else {
                        key = obj;
                        j = time;
                    }
                    time = j;
                    obj = key;
                }
                this.cacheMap.remove(obj);
            }
            this.cacheMap.put(str, arrayList);
        }
    }

    private List attrCertSubjectSerialSearch(X509AttributeCertStoreSelector x509AttributeCertStoreSelector, String[] strArr, String[] strArr2, String[] strArr3) {
        Principal[] entityNames;
        String str = null;
        List arrayList = new ArrayList();
        Collection<String> hashSet = new HashSet();
        if (x509AttributeCertStoreSelector.getHolder() != null) {
            if (x509AttributeCertStoreSelector.getHolder().getSerialNumber() != null) {
                hashSet.add(x509AttributeCertStoreSelector.getHolder().getSerialNumber().toString());
            }
            if (x509AttributeCertStoreSelector.getHolder().getEntityNames() != null) {
                entityNames = x509AttributeCertStoreSelector.getHolder().getEntityNames();
                if (x509AttributeCertStoreSelector.getAttributeCert() != null) {
                    if (x509AttributeCertStoreSelector.getAttributeCert().getHolder().getEntityNames() != null) {
                        entityNames = x509AttributeCertStoreSelector.getAttributeCert().getHolder().getEntityNames();
                    }
                    hashSet.add(x509AttributeCertStoreSelector.getAttributeCert().getSerialNumber().toString());
                }
                if (entityNames != null) {
                    str = entityNames[0] instanceof X500Principal ? ((X500Principal) entityNames[0]).getName("RFC1779") : entityNames[0].getName();
                }
                if (x509AttributeCertStoreSelector.getSerialNumber() != null) {
                    hashSet.add(x509AttributeCertStoreSelector.getSerialNumber().toString());
                }
                if (str != null) {
                    for (String parseDN : strArr3) {
                        arrayList.addAll(search(strArr2, "*" + parseDN(str, parseDN) + "*", strArr));
                    }
                }
                if (hashSet.size() > 0 && this.params.getSearchForSerialNumberIn() != null) {
                    for (String search : hashSet) {
                        arrayList.addAll(search(splitString(this.params.getSearchForSerialNumberIn()), search, strArr));
                    }
                }
                if (hashSet.size() == 0 && str == null) {
                    arrayList.addAll(search(strArr2, "*", strArr));
                }
                return arrayList;
            }
        }
        entityNames = null;
        if (x509AttributeCertStoreSelector.getAttributeCert() != null) {
            if (x509AttributeCertStoreSelector.getAttributeCert().getHolder().getEntityNames() != null) {
                entityNames = x509AttributeCertStoreSelector.getAttributeCert().getHolder().getEntityNames();
            }
            hashSet.add(x509AttributeCertStoreSelector.getAttributeCert().getSerialNumber().toString());
        }
        if (entityNames != null) {
            if (entityNames[0] instanceof X500Principal) {
            }
        }
        if (x509AttributeCertStoreSelector.getSerialNumber() != null) {
            hashSet.add(x509AttributeCertStoreSelector.getSerialNumber().toString());
        }
        if (str != null) {
            while (r0 < strArr3.length) {
                arrayList.addAll(search(strArr2, "*" + parseDN(str, parseDN) + "*", strArr));
            }
        }
        while (r2.hasNext()) {
            arrayList.addAll(search(splitString(this.params.getSearchForSerialNumberIn()), search, strArr));
        }
        arrayList.addAll(search(strArr2, "*", strArr));
        return arrayList;
    }

    private List cRLIssuerSearch(X509CRLStoreSelector x509CRLStoreSelector, String[] strArr, String[] strArr2, String[] strArr3) {
        int i;
        List arrayList = new ArrayList();
        Collection<X500Principal> hashSet = new HashSet();
        if (x509CRLStoreSelector.getIssuers() != null) {
            hashSet.addAll(x509CRLStoreSelector.getIssuers());
        }
        if (x509CRLStoreSelector.getCertificateChecking() != null) {
            hashSet.add(getCertificateIssuer(x509CRLStoreSelector.getCertificateChecking()));
        }
        if (x509CRLStoreSelector.getAttrCertificateChecking() != null) {
            Principal[] principals = x509CRLStoreSelector.getAttrCertificateChecking().getIssuer().getPrincipals();
            for (i = 0; i < principals.length; i++) {
                if (principals[i] instanceof X500Principal) {
                    hashSet.add(principals[i]);
                }
            }
        }
        String str = null;
        for (X500Principal name : hashSet) {
            String name2 = name.getName("RFC1779");
            for (String parseDN : strArr3) {
                arrayList.addAll(search(strArr2, "*" + parseDN(name2, parseDN) + "*", strArr));
            }
            str = name2;
        }
        if (str == null) {
            arrayList.addAll(search(strArr2, "*", strArr));
        }
        return arrayList;
    }

    private List certSubjectSerialSearch(X509CertStoreSelector x509CertStoreSelector, String[] strArr, String[] strArr2, String[] strArr3) {
        String name;
        List arrayList = new ArrayList();
        String str = null;
        String subjectAsString = getSubjectAsString(x509CertStoreSelector);
        if (x509CertStoreSelector.getSerialNumber() != null) {
            str = x509CertStoreSelector.getSerialNumber().toString();
        }
        if (x509CertStoreSelector.getCertificate() != null) {
            name = x509CertStoreSelector.getCertificate().getSubjectX500Principal().getName("RFC1779");
            subjectAsString = x509CertStoreSelector.getCertificate().getSerialNumber().toString();
        } else {
            name = subjectAsString;
            subjectAsString = str;
        }
        if (name != null) {
            for (String parseDN : strArr3) {
                arrayList.addAll(search(strArr2, "*" + parseDN(name, parseDN) + "*", strArr));
            }
        }
        if (!(subjectAsString == null || this.params.getSearchForSerialNumberIn() == null)) {
            arrayList.addAll(search(splitString(this.params.getSearchForSerialNumberIn()), subjectAsString, strArr));
        }
        if (subjectAsString == null && name == null) {
            arrayList.addAll(search(strArr2, "*", strArr));
        }
        return arrayList;
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

    private Set createAttributeCertificates(List list, X509AttributeCertStoreSelector x509AttributeCertStoreSelector) {
        Set hashSet = new HashSet();
        X509AttrCertParser x509AttrCertParser = new X509AttrCertParser();
        for (byte[] byteArrayInputStream : list) {
            try {
                x509AttrCertParser.engineInit(new ByteArrayInputStream(byteArrayInputStream));
                X509AttributeCertificate x509AttributeCertificate = (X509AttributeCertificate) x509AttrCertParser.engineRead();
                if (x509AttributeCertStoreSelector.match(x509AttributeCertificate)) {
                    hashSet.add(x509AttributeCertificate);
                }
            } catch (StreamParsingException e) {
            }
        }
        return hashSet;
    }

    private Set createCRLs(List list, X509CRLStoreSelector x509CRLStoreSelector) {
        Set hashSet = new HashSet();
        X509CRLParser x509CRLParser = new X509CRLParser();
        for (byte[] byteArrayInputStream : list) {
            try {
                x509CRLParser.engineInit(new ByteArrayInputStream(byteArrayInputStream));
                Object obj = (X509CRL) x509CRLParser.engineRead();
                if (x509CRLStoreSelector.match(obj)) {
                    hashSet.add(obj);
                }
            } catch (StreamParsingException e) {
            }
        }
        return hashSet;
    }

    private Set createCerts(List list, X509CertStoreSelector x509CertStoreSelector) {
        Set hashSet = new HashSet();
        X509CertParser x509CertParser = new X509CertParser();
        for (byte[] byteArrayInputStream : list) {
            try {
                x509CertParser.engineInit(new ByteArrayInputStream(byteArrayInputStream));
                Object obj = (X509Certificate) x509CertParser.engineRead();
                if (x509CertStoreSelector.match(obj)) {
                    hashSet.add(obj);
                }
            } catch (Exception e) {
            }
        }
        return hashSet;
    }

    private Set createCrossCertificatePairs(List list, X509CertPairStoreSelector x509CertPairStoreSelector) {
        Set hashSet = new HashSet();
        int i;
        for (int i2 = 0; i2 < list.size(); i2 = i + 1) {
            try {
                X509CertPairParser x509CertPairParser = new X509CertPairParser();
                x509CertPairParser.engineInit(new ByteArrayInputStream((byte[]) list.get(i2)));
                Object obj = (X509CertificatePair) x509CertPairParser.engineRead();
                i = i2;
            } catch (StreamParsingException e) {
                try {
                    i = i2 + 1;
                    X509CertificatePair x509CertificatePair = new X509CertificatePair(new CertificatePair(Certificate.getInstance(new ASN1InputStream((byte[]) list.get(i2)).readObject()), Certificate.getInstance(new ASN1InputStream((byte[]) list.get(i2 + 1)).readObject())));
                } catch (CertificateParsingException e2) {
                    i = i2;
                } catch (IOException e3) {
                    i = i2;
                }
            }
            try {
                if (x509CertPairStoreSelector.match(obj)) {
                    hashSet.add(obj);
                }
            } catch (CertificateParsingException e4) {
            } catch (IOException e5) {
            }
        }
        return hashSet;
    }

    private List crossCertificatePairSubjectSearch(X509CertPairStoreSelector x509CertPairStoreSelector, String[] strArr, String[] strArr2, String[] strArr3) {
        List arrayList = new ArrayList();
        String str = null;
        if (x509CertPairStoreSelector.getForwardSelector() != null) {
            str = getSubjectAsString(x509CertPairStoreSelector.getForwardSelector());
        }
        String name = (x509CertPairStoreSelector.getCertPair() == null || x509CertPairStoreSelector.getCertPair().getForward() == null) ? str : x509CertPairStoreSelector.getCertPair().getForward().getSubjectX500Principal().getName("RFC1779");
        if (name != null) {
            for (String parseDN : strArr3) {
                arrayList.addAll(search(strArr2, "*" + parseDN(name, parseDN) + "*", strArr));
            }
        }
        if (name == null) {
            arrayList.addAll(search(strArr2, "*", strArr));
        }
        return arrayList;
    }

    private X500Principal getCertificateIssuer(X509Certificate x509Certificate) {
        return x509Certificate.getIssuerX500Principal();
    }

    private List getFromCache(String str) {
        List list = (List) this.cacheMap.get(str);
        return list != null ? ((Date) list.get(0)).getTime() < System.currentTimeMillis() - lifeTime ? null : (List) list.get(1) : null;
    }

    private String getSubjectAsString(X509CertStoreSelector x509CertStoreSelector) {
        try {
            byte[] subjectAsBytes = x509CertStoreSelector.getSubjectAsBytes();
            return subjectAsBytes != null ? new X500Principal(subjectAsBytes).getName("RFC1779") : null;
        } catch (Throwable e) {
            throw new StoreException("exception processing name: " + e.getMessage(), e);
        }
    }

    private String parseDN(String str, String str2) {
        int indexOf = str.toLowerCase().indexOf(str2.toLowerCase() + "=");
        if (indexOf == -1) {
            return BuildConfig.FLAVOR;
        }
        String substring = str.substring(indexOf + str2.length());
        indexOf = substring.indexOf(44);
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

    private List search(String[] strArr, String str, String[] strArr2) {
        String str2;
        String str3;
        DirContext dirContext = null;
        int i = 0;
        if (strArr == null) {
            str2 = null;
        } else {
            str2 = BuildConfig.FLAVOR;
            if (str.equals("**")) {
                str = "*";
            }
            str3 = str2;
            for (String str4 : strArr) {
                str3 = str3 + "(" + str4 + "=" + str + ")";
            }
            str2 = "(|" + str3 + ")";
        }
        str3 = BuildConfig.FLAVOR;
        while (i < strArr2.length) {
            str3 = str3 + "(" + strArr2[i] + "=*)";
            i++;
        }
        String str5 = "(|" + str3 + ")";
        str3 = "(&" + str2 + BuildConfig.FLAVOR + str5 + ")";
        if (str2 != null) {
            str5 = str3;
        }
        List fromCache = getFromCache(str5);
        if (fromCache != null) {
            return fromCache;
        }
        List arrayList = new ArrayList();
        try {
            dirContext = connectLDAP();
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(2);
            searchControls.setCountLimit(0);
            searchControls.setReturningAttributes(strArr2);
            NamingEnumeration search = dirContext.search(this.params.getBaseDN(), str5, searchControls);
            while (search.hasMoreElements()) {
                NamingEnumeration all = ((Attribute) ((SearchResult) search.next()).getAttributes().getAll().next()).getAll();
                while (all.hasMore()) {
                    arrayList.add(all.next());
                }
            }
            addToCache(str5, arrayList);
            if (dirContext != null) {
                try {
                    dirContext.close();
                } catch (Exception e) {
                }
            }
        } catch (NamingException e2) {
            if (dirContext != null) {
                try {
                    dirContext.close();
                } catch (Exception e3) {
                }
            }
        } catch (Throwable th) {
            if (dirContext != null) {
                try {
                    dirContext.close();
                } catch (Exception e4) {
                }
            }
        }
        return arrayList;
    }

    private String[] splitString(String str) {
        return str.split("\\s+");
    }

    public Collection getAACertificates(X509AttributeCertStoreSelector x509AttributeCertStoreSelector) {
        String[] splitString = splitString(this.params.getAACertificateAttribute());
        String[] splitString2 = splitString(this.params.getLdapAACertificateAttributeName());
        String[] splitString3 = splitString(this.params.getAACertificateSubjectAttributeName());
        Collection createAttributeCertificates = createAttributeCertificates(attrCertSubjectSerialSearch(x509AttributeCertStoreSelector, splitString, splitString2, splitString3), x509AttributeCertStoreSelector);
        if (createAttributeCertificates.size() == 0) {
            createAttributeCertificates.addAll(createAttributeCertificates(attrCertSubjectSerialSearch(new X509AttributeCertStoreSelector(), splitString, splitString2, splitString3), x509AttributeCertStoreSelector));
        }
        return createAttributeCertificates;
    }

    public Collection getAttributeAuthorityRevocationLists(X509CRLStoreSelector x509CRLStoreSelector) {
        String[] splitString = splitString(this.params.getAttributeAuthorityRevocationListAttribute());
        String[] splitString2 = splitString(this.params.getLdapAttributeAuthorityRevocationListAttributeName());
        String[] splitString3 = splitString(this.params.getAttributeAuthorityRevocationListIssuerAttributeName());
        Collection createCRLs = createCRLs(cRLIssuerSearch(x509CRLStoreSelector, splitString, splitString2, splitString3), x509CRLStoreSelector);
        if (createCRLs.size() == 0) {
            createCRLs.addAll(createCRLs(cRLIssuerSearch(new X509CRLStoreSelector(), splitString, splitString2, splitString3), x509CRLStoreSelector));
        }
        return createCRLs;
    }

    public Collection getAttributeCertificateAttributes(X509AttributeCertStoreSelector x509AttributeCertStoreSelector) {
        String[] splitString = splitString(this.params.getAttributeCertificateAttributeAttribute());
        String[] splitString2 = splitString(this.params.getLdapAttributeCertificateAttributeAttributeName());
        String[] splitString3 = splitString(this.params.getAttributeCertificateAttributeSubjectAttributeName());
        Collection createAttributeCertificates = createAttributeCertificates(attrCertSubjectSerialSearch(x509AttributeCertStoreSelector, splitString, splitString2, splitString3), x509AttributeCertStoreSelector);
        if (createAttributeCertificates.size() == 0) {
            createAttributeCertificates.addAll(createAttributeCertificates(attrCertSubjectSerialSearch(new X509AttributeCertStoreSelector(), splitString, splitString2, splitString3), x509AttributeCertStoreSelector));
        }
        return createAttributeCertificates;
    }

    public Collection getAttributeCertificateRevocationLists(X509CRLStoreSelector x509CRLStoreSelector) {
        String[] splitString = splitString(this.params.getAttributeCertificateRevocationListAttribute());
        String[] splitString2 = splitString(this.params.getLdapAttributeCertificateRevocationListAttributeName());
        String[] splitString3 = splitString(this.params.getAttributeCertificateRevocationListIssuerAttributeName());
        Collection createCRLs = createCRLs(cRLIssuerSearch(x509CRLStoreSelector, splitString, splitString2, splitString3), x509CRLStoreSelector);
        if (createCRLs.size() == 0) {
            createCRLs.addAll(createCRLs(cRLIssuerSearch(new X509CRLStoreSelector(), splitString, splitString2, splitString3), x509CRLStoreSelector));
        }
        return createCRLs;
    }

    public Collection getAttributeDescriptorCertificates(X509AttributeCertStoreSelector x509AttributeCertStoreSelector) {
        String[] splitString = splitString(this.params.getAttributeDescriptorCertificateAttribute());
        String[] splitString2 = splitString(this.params.getLdapAttributeDescriptorCertificateAttributeName());
        String[] splitString3 = splitString(this.params.getAttributeDescriptorCertificateSubjectAttributeName());
        Collection createAttributeCertificates = createAttributeCertificates(attrCertSubjectSerialSearch(x509AttributeCertStoreSelector, splitString, splitString2, splitString3), x509AttributeCertStoreSelector);
        if (createAttributeCertificates.size() == 0) {
            createAttributeCertificates.addAll(createAttributeCertificates(attrCertSubjectSerialSearch(new X509AttributeCertStoreSelector(), splitString, splitString2, splitString3), x509AttributeCertStoreSelector));
        }
        return createAttributeCertificates;
    }

    public Collection getAuthorityRevocationLists(X509CRLStoreSelector x509CRLStoreSelector) {
        String[] splitString = splitString(this.params.getAuthorityRevocationListAttribute());
        String[] splitString2 = splitString(this.params.getLdapAuthorityRevocationListAttributeName());
        String[] splitString3 = splitString(this.params.getAuthorityRevocationListIssuerAttributeName());
        Collection createCRLs = createCRLs(cRLIssuerSearch(x509CRLStoreSelector, splitString, splitString2, splitString3), x509CRLStoreSelector);
        if (createCRLs.size() == 0) {
            createCRLs.addAll(createCRLs(cRLIssuerSearch(new X509CRLStoreSelector(), splitString, splitString2, splitString3), x509CRLStoreSelector));
        }
        return createCRLs;
    }

    public Collection getCACertificates(X509CertStoreSelector x509CertStoreSelector) {
        String[] splitString = splitString(this.params.getCACertificateAttribute());
        String[] splitString2 = splitString(this.params.getLdapCACertificateAttributeName());
        String[] splitString3 = splitString(this.params.getCACertificateSubjectAttributeName());
        Collection createCerts = createCerts(certSubjectSerialSearch(x509CertStoreSelector, splitString, splitString2, splitString3), x509CertStoreSelector);
        if (createCerts.size() == 0) {
            createCerts.addAll(createCerts(certSubjectSerialSearch(new X509CertStoreSelector(), splitString, splitString2, splitString3), x509CertStoreSelector));
        }
        return createCerts;
    }

    public Collection getCertificateRevocationLists(X509CRLStoreSelector x509CRLStoreSelector) {
        String[] splitString = splitString(this.params.getCertificateRevocationListAttribute());
        String[] splitString2 = splitString(this.params.getLdapCertificateRevocationListAttributeName());
        String[] splitString3 = splitString(this.params.getCertificateRevocationListIssuerAttributeName());
        Collection createCRLs = createCRLs(cRLIssuerSearch(x509CRLStoreSelector, splitString, splitString2, splitString3), x509CRLStoreSelector);
        if (createCRLs.size() == 0) {
            createCRLs.addAll(createCRLs(cRLIssuerSearch(new X509CRLStoreSelector(), splitString, splitString2, splitString3), x509CRLStoreSelector));
        }
        return createCRLs;
    }

    public Collection getCrossCertificatePairs(X509CertPairStoreSelector x509CertPairStoreSelector) {
        String[] splitString = splitString(this.params.getCrossCertificateAttribute());
        String[] splitString2 = splitString(this.params.getLdapCrossCertificateAttributeName());
        String[] splitString3 = splitString(this.params.getCrossCertificateSubjectAttributeName());
        Collection createCrossCertificatePairs = createCrossCertificatePairs(crossCertificatePairSubjectSearch(x509CertPairStoreSelector, splitString, splitString2, splitString3), x509CertPairStoreSelector);
        if (createCrossCertificatePairs.size() == 0) {
            X509CertStoreSelector x509CertStoreSelector = new X509CertStoreSelector();
            X509CertPairStoreSelector x509CertPairStoreSelector2 = new X509CertPairStoreSelector();
            x509CertPairStoreSelector2.setForwardSelector(x509CertStoreSelector);
            x509CertPairStoreSelector2.setReverseSelector(x509CertStoreSelector);
            createCrossCertificatePairs.addAll(createCrossCertificatePairs(crossCertificatePairSubjectSearch(x509CertPairStoreSelector2, splitString, splitString2, splitString3), x509CertPairStoreSelector));
        }
        return createCrossCertificatePairs;
    }

    public Collection getDeltaCertificateRevocationLists(X509CRLStoreSelector x509CRLStoreSelector) {
        String[] splitString = splitString(this.params.getDeltaRevocationListAttribute());
        String[] splitString2 = splitString(this.params.getLdapDeltaRevocationListAttributeName());
        String[] splitString3 = splitString(this.params.getDeltaRevocationListIssuerAttributeName());
        Collection createCRLs = createCRLs(cRLIssuerSearch(x509CRLStoreSelector, splitString, splitString2, splitString3), x509CRLStoreSelector);
        if (createCRLs.size() == 0) {
            createCRLs.addAll(createCRLs(cRLIssuerSearch(new X509CRLStoreSelector(), splitString, splitString2, splitString3), x509CRLStoreSelector));
        }
        return createCRLs;
    }

    public Collection getUserCertificates(X509CertStoreSelector x509CertStoreSelector) {
        String[] splitString = splitString(this.params.getUserCertificateAttribute());
        String[] splitString2 = splitString(this.params.getLdapUserCertificateAttributeName());
        String[] splitString3 = splitString(this.params.getUserCertificateSubjectAttributeName());
        Collection createCerts = createCerts(certSubjectSerialSearch(x509CertStoreSelector, splitString, splitString2, splitString3), x509CertStoreSelector);
        if (createCerts.size() == 0) {
            createCerts.addAll(createCerts(certSubjectSerialSearch(new X509CertStoreSelector(), splitString, splitString2, splitString3), x509CertStoreSelector));
        }
        return createCerts;
    }
}
