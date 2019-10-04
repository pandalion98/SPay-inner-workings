/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.cert.CRL
 *  java.security.cert.CRLSelector
 *  java.security.cert.CertSelector
 *  java.security.cert.CertStoreException
 *  java.security.cert.CertStoreParameters
 *  java.security.cert.CertStoreSpi
 *  java.security.cert.Certificate
 *  java.security.cert.CertificateFactory
 *  java.security.cert.X509CRLSelector
 *  java.security.cert.X509CertSelector
 *  java.security.cert.X509Certificate
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.HashSet
 *  java.util.Hashtable
 *  java.util.Iterator
 *  java.util.Properties
 *  java.util.Set
 *  javax.naming.NamingEnumeration
 *  javax.naming.directory.Attribute
 *  javax.naming.directory.Attributes
 *  javax.naming.directory.DirContext
 *  javax.naming.directory.InitialDirContext
 *  javax.naming.directory.SearchControls
 *  javax.naming.directory.SearchResult
 *  javax.security.auth.x500.X500Principal
 */
package org.bouncycastle.jce.provider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CRL;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.CertStoreException;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStoreSpi;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.CertificatePair;
import org.bouncycastle.jce.X509LDAPCertStoreParameters;

public class X509LDAPCertStoreSpi
extends CertStoreSpi {
    private static String LDAP_PROVIDER = "com.sun.jndi.ldap.LdapCtxFactory";
    private static String REFERRALS_IGNORE = "ignore";
    private static final String SEARCH_SECURITY_LEVEL = "none";
    private static final String URL_CONTEXT_PREFIX = "com.sun.jndi.url";
    private X509LDAPCertStoreParameters params;

    public X509LDAPCertStoreSpi(CertStoreParameters certStoreParameters) {
        super(certStoreParameters);
        if (!(certStoreParameters instanceof X509LDAPCertStoreParameters)) {
            throw new InvalidAlgorithmParameterException(X509LDAPCertStoreSpi.class.getName() + ": parameter must be a " + X509LDAPCertStoreParameters.class.getName() + " object\n" + certStoreParameters.toString());
        }
        this.params = (X509LDAPCertStoreParameters)certStoreParameters;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Set certSubjectSerialSearch(X509CertSelector x509CertSelector, String[] arrstring, String string, String string2) {
        HashSet hashSet = new HashSet();
        try {
            String string3;
            String string4;
            if (x509CertSelector.getSubjectAsBytes() == null && x509CertSelector.getSubjectAsString() == null && x509CertSelector.getCertificate() == null) {
                hashSet.addAll((Collection)this.search(string, "*", arrstring));
                return hashSet;
            }
            if (x509CertSelector.getCertificate() != null) {
                string3 = x509CertSelector.getCertificate().getSubjectX500Principal().getName("RFC1779");
                string4 = x509CertSelector.getCertificate().getSerialNumber().toString();
            } else if (x509CertSelector.getSubjectAsBytes() != null) {
                string3 = new X500Principal(x509CertSelector.getSubjectAsBytes()).getName("RFC1779");
                string4 = null;
            } else {
                string3 = x509CertSelector.getSubjectAsString();
                string4 = null;
            }
            String string5 = this.parseDN(string3, string2);
            hashSet.addAll((Collection)this.search(string, "*" + string5 + "*", arrstring));
            if (string4 == null || this.params.getSearchForSerialNumberIn() == null) return hashSet;
            {
                hashSet.addAll((Collection)this.search(this.params.getSearchForSerialNumberIn(), "*" + string4 + "*", arrstring));
                return hashSet;
            }
        }
        catch (IOException iOException) {
            throw new CertStoreException("exception processing selector: " + (Object)((Object)iOException));
        }
    }

    private DirContext connectLDAP() {
        Properties properties = new Properties();
        properties.setProperty("java.naming.factory.initial", LDAP_PROVIDER);
        properties.setProperty("java.naming.batchsize", "0");
        properties.setProperty("java.naming.provider.url", this.params.getLdapURL());
        properties.setProperty("java.naming.factory.url.pkgs", URL_CONTEXT_PREFIX);
        properties.setProperty("java.naming.referral", REFERRALS_IGNORE);
        properties.setProperty("java.naming.security.authentication", SEARCH_SECURITY_LEVEL);
        return new InitialDirContext((Hashtable)properties);
    }

    private Set getCACertificates(X509CertSelector x509CertSelector) {
        String[] arrstring = new String[]{this.params.getCACertificateAttribute()};
        Set set = this.certSubjectSerialSearch(x509CertSelector, arrstring, this.params.getLdapCACertificateAttributeName(), this.params.getCACertificateSubjectAttributeName());
        if (set.isEmpty()) {
            set.addAll((Collection)this.search(null, "*", arrstring));
        }
        return set;
    }

    private Set getCrossCertificates(X509CertSelector x509CertSelector) {
        String[] arrstring = new String[]{this.params.getCrossCertificateAttribute()};
        Set set = this.certSubjectSerialSearch(x509CertSelector, arrstring, this.params.getLdapCrossCertificateAttributeName(), this.params.getCrossCertificateSubjectAttributeName());
        if (set.isEmpty()) {
            set.addAll((Collection)this.search(null, "*", arrstring));
        }
        return set;
    }

    private Set getEndCertificates(X509CertSelector x509CertSelector) {
        String[] arrstring = new String[]{this.params.getUserCertificateAttribute()};
        return this.certSubjectSerialSearch(x509CertSelector, arrstring, this.params.getLdapUserCertificateAttributeName(), this.params.getUserCertificateSubjectAttributeName());
    }

    private String parseDN(String string, String string2) {
        String string3 = string.substring(string.toLowerCase().indexOf(string2.toLowerCase()) + string2.length());
        int n = string3.indexOf(44);
        if (n == -1) {
            n = string3.length();
        }
        while (string3.charAt(n - 1) == '\\') {
            if ((n = string3.indexOf(44, n + 1)) != -1) continue;
            n = string3.length();
        }
        String string4 = string3.substring(0, n);
        String string5 = string4.substring(1 + string4.indexOf(61));
        if (string5.charAt(0) == ' ') {
            string5 = string5.substring(1);
        }
        if (string5.startsWith("\"")) {
            string5 = string5.substring(1);
        }
        if (string5.endsWith("\"")) {
            string5 = string5.substring(0, -1 + string5.length());
        }
        return string5;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private Set search(String var1_1, String var2_2, String[] var3_3) {
        var4_4 = 0;
        var5_5 = var1_1 + "=" + var2_2;
        if (var1_1 == null) {
            var5_5 = null;
        }
        var6_6 = new HashSet();
        var11_8 = var14_7 = this.connectLDAP();
        try {
            var15_9 = new SearchControls();
            var15_9.setSearchScope(2);
            var15_9.setCountLimit(0L);
        }
        catch (Exception var8_14) {
            block17 : {
                var9_16 = var11_8;
                break block17;
                catch (Throwable var13_22) {
                    var10_18 = var13_22;
                    var11_8 = null;
                    ** GOTO lbl30
                }
                catch (Throwable var10_19) {
                    ** GOTO lbl30
                }
                catch (Exception var7_23) {
                    var8_15 = var7_23;
                    var9_16 = null;
                }
            }
            try {
                throw new CertStoreException("Error getting results from LDAP directory " + (Object)var8_15);
            }
            catch (Throwable var10_17) {
                var11_8 = var9_16;
lbl30: // 3 sources:
                if (var11_8 == null) throw var10_18;
                try {
                    var11_8.close();
                }
                catch (Exception var12_21) {
                    throw var10_18;
                }
                throw var10_18;
            }
        }
        while (var4_4 < var3_3.length) {
            var17_10 = new String[]{var3_3[var4_4]};
            var15_9.setReturningAttributes(var17_10);
            var18_11 = "(&(" + var5_5 + ")(" + var17_10[0] + "=*))";
            if (var5_5 == null) {
                var18_11 = "(" + var17_10[0] + "=*)";
            }
            var19_12 = var11_8.search(this.params.getBaseDN(), var18_11, var15_9);
            while (var19_12.hasMoreElements()) {
                var20_13 = ((Attribute)((SearchResult)var19_12.next()).getAttributes().getAll().next()).getAll();
                while (var20_13.hasMore()) {
                    var6_6.add(var20_13.next());
                }
            }
            ++var4_4;
        }
        if (var11_8 == null) return var6_6;
        try {
            var11_8.close();
            return var6_6;
        }
        catch (Exception var16_20) {
            return var6_6;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Collection engineGetCRLs(CRLSelector cRLSelector) {
        String[] arrstring = new String[]{this.params.getCertificateRevocationListAttribute()};
        if (!(cRLSelector instanceof X509CRLSelector)) {
            throw new CertStoreException("selector is not a X509CRLSelector");
        }
        X509CRLSelector x509CRLSelector = (X509CRLSelector)cRLSelector;
        HashSet hashSet = new HashSet();
        String string = this.params.getLdapCertificateRevocationListAttributeName();
        HashSet hashSet2 = new HashSet();
        if (x509CRLSelector.getIssuerNames() == null) {
            hashSet2.addAll((Collection)this.search(string, "*", arrstring));
        } else {
            for (Object object : x509CRLSelector.getIssuerNames()) {
                String string2;
                if (object instanceof String) {
                    String string3 = this.params.getCertificateRevocationListIssuerAttributeName();
                    string2 = this.parseDN((String)object, string3);
                } else {
                    String string4 = this.params.getCertificateRevocationListIssuerAttributeName();
                    string2 = this.parseDN(new X500Principal((byte[])object).getName("RFC1779"), string4);
                }
                hashSet2.addAll((Collection)this.search(string, "*" + string2 + "*", arrstring));
            }
        }
        hashSet2.addAll((Collection)this.search(null, "*", arrstring));
        Iterator iterator = hashSet2.iterator();
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance((String)"X.509", (String)"BC");
            while (iterator.hasNext()) {
                CRL cRL = certificateFactory.generateCRL((InputStream)new ByteArrayInputStream((byte[])iterator.next()));
                if (!x509CRLSelector.match(cRL)) continue;
                hashSet.add((Object)cRL);
            }
            return hashSet;
        }
        catch (Exception exception) {
            throw new CertStoreException("CRL cannot be constructed from LDAP result " + (Object)((Object)exception));
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public Collection engineGetCertificates(CertSelector var1_1) {
        block12 : {
            if (!(var1_1 instanceof X509CertSelector)) {
                throw new CertStoreException("selector is not a X509CertSelector");
            }
            var2_2 = (X509CertSelector)var1_1;
            var3_3 = new HashSet();
            var4_4 = this.getEndCertificates(var2_2);
            var4_4.addAll((Collection)this.getCACertificates(var2_2));
            var4_4.addAll((Collection)this.getCrossCertificates(var2_2));
            var7_5 = var4_4.iterator();
            try {
                var9_6 = CertificateFactory.getInstance((String)"X.509", (String)"BC");
                do lbl-1000: // 2 sources:
                {
                    if (var7_5.hasNext() == false) return var3_3;
                    var10_7 = (byte[])var7_5.next();
                    if (var10_7 == null || var10_7.length == 0) ** GOTO lbl-1000
                    var11_8 = new ArrayList();
                    var11_8.add((Object)var10_7);
                    var20_15 = CertificatePair.getInstance(new ASN1InputStream(var10_7).readObject());
                    var11_8.clear();
                    if (var20_15.getForward() != null) {
                        var11_8.add((Object)var20_15.getForward().getEncoded());
                    }
                    if (var20_15.getReverse() != null) {
                        var11_8.add((Object)var20_15.getReverse().getEncoded());
                    }
                    break block12;
                    break;
                } while (true);
            }
            catch (Exception var8_16) {
                throw new CertStoreException("certificate cannot be constructed from LDAP result: " + (Object)var8_16);
            }
            catch (IllegalArgumentException var19_14) {
                break block12;
            }
            catch (IOException var13_9) {}
        }
        var14_10 = var11_8.iterator();
        do {
            if (!var14_10.hasNext()) ** continue;
            var15_11 = new ByteArrayInputStream((byte[])var14_10.next());
            try {
                var17_13 = var9_6.generateCertificate((InputStream)var15_11);
                if (!var2_2.match(var17_13)) ** continue;
                var3_3.add((Object)var17_13);
            }
            catch (Exception var16_12) {
            }
        } while (true);
    }
}

