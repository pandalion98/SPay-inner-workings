/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.security.Principal
 *  java.security.cert.CertificateParsingException
 *  java.security.cert.X509CRL
 *  java.security.cert.X509Certificate
 *  java.sql.Date
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.HashSet
 *  java.util.Hashtable
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Properties
 *  java.util.Set
 *  javax.naming.directory.DirContext
 *  javax.naming.directory.InitialDirContext
 *  javax.security.auth.x500.X500Principal
 */
package org.bouncycastle.x509.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.CertificatePair;
import org.bouncycastle.jce.X509LDAPCertStoreParameters;
import org.bouncycastle.jce.provider.X509AttrCertParser;
import org.bouncycastle.jce.provider.X509CRLParser;
import org.bouncycastle.jce.provider.X509CertPairParser;
import org.bouncycastle.jce.provider.X509CertParser;
import org.bouncycastle.util.StoreException;
import org.bouncycastle.x509.AttributeCertificateHolder;
import org.bouncycastle.x509.AttributeCertificateIssuer;
import org.bouncycastle.x509.X509AttributeCertStoreSelector;
import org.bouncycastle.x509.X509AttributeCertificate;
import org.bouncycastle.x509.X509CRLStoreSelector;
import org.bouncycastle.x509.X509CertPairStoreSelector;
import org.bouncycastle.x509.X509CertStoreSelector;
import org.bouncycastle.x509.X509CertificatePair;
import org.bouncycastle.x509.util.StreamParsingException;

public class LDAPStoreHelper {
    private static String LDAP_PROVIDER = "com.sun.jndi.ldap.LdapCtxFactory";
    private static String REFERRALS_IGNORE = "ignore";
    private static final String SEARCH_SECURITY_LEVEL = "none";
    private static final String URL_CONTEXT_PREFIX = "com.sun.jndi.url";
    private static int cacheSize = 32;
    private static long lifeTime = 60000L;
    private Map cacheMap = new HashMap(cacheSize);
    private X509LDAPCertStoreParameters params;

    public LDAPStoreHelper(X509LDAPCertStoreParameters x509LDAPCertStoreParameters) {
        this.params = x509LDAPCertStoreParameters;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private void addToCache(String var1_1, List var2_2) {
        var21_3 = this;
        // MONITORENTER : var21_3
        var3_4 = new Date(System.currentTimeMillis());
        var4_5 = new ArrayList();
        var4_5.add((Object)var3_4);
        var4_5.add((Object)var2_2);
        if (this.cacheMap.containsKey((Object)var1_1)) {
            this.cacheMap.put((Object)var1_1, (Object)var4_5);
            // MONITOREXIT : var21_3
            return;
        }
        if (this.cacheMap.size() < LDAPStoreHelper.cacheSize) ** GOTO lbl25
        var9_6 = this.cacheMap.entrySet().iterator();
        var10_7 = var3_4.getTime();
        var12_8 = null;
        do {
            block6 : {
                block5 : {
                    block4 : {
                        if (!var9_6.hasNext()) break block4;
                        var14_9 = (Map.Entry)var9_6.next();
                        var15_10 = ((Date)((List)var14_9.getValue()).get(0)).getTime();
                        if (var15_10 >= var10_7) break block5;
                        var17_11 = var14_9.getKey();
                        var18_12 = var15_10;
                        break block6;
                    }
                    this.cacheMap.remove(var12_8);
lbl25: // 2 sources:
                    this.cacheMap.put((Object)var1_1, (Object)var4_5);
                    return;
                }
                var17_11 = var12_8;
                var18_12 = var10_7;
            }
            var10_7 = var18_12;
            var12_8 = var17_11;
        } while (true);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private List attrCertSubjectSerialSearch(X509AttributeCertStoreSelector var1_1, String[] var2_2, String[] var3_3, String[] var4_4) {
        var5_5 = new ArrayList();
        var6_6 = new HashSet();
        if (var1_1.getHolder() == null) ** GOTO lbl-1000
        if (var1_1.getHolder().getSerialNumber() != null) {
            var6_6.add((Object)var1_1.getHolder().getSerialNumber().toString());
        }
        if (var1_1.getHolder().getEntityNames() != null) {
            var7_7 = var1_1.getHolder().getEntityNames();
        } else lbl-1000: // 2 sources:
        {
            var7_7 = null;
        }
        if (var1_1.getAttributeCert() != null) {
            if (var1_1.getAttributeCert().getHolder().getEntityNames() != null) {
                var7_7 = var1_1.getAttributeCert().getHolder().getEntityNames();
            }
            var6_6.add((Object)var1_1.getAttributeCert().getSerialNumber().toString());
        }
        var8_8 = null;
        if (var7_7 != null) {
            var8_8 = var7_7[0] instanceof X500Principal != false ? ((X500Principal)var7_7[0]).getName("RFC1779") : var7_7[0].getName();
        }
        if (var1_1.getSerialNumber() != null) {
            var6_6.add((Object)var1_1.getSerialNumber().toString());
        }
        if (var8_8 != null) {
            for (var13_9 = 0; var13_9 < var4_4.length; ++var13_9) {
                var14_10 = this.parseDN(var8_8, var4_4[var13_9]);
                var5_5.addAll((Collection)this.search(var3_3, "*" + var14_10 + "*", var2_2));
            }
        }
        if (var6_6.size() > 0 && this.params.getSearchForSerialNumberIn() != null) {
            for (String var11_12 : var6_6) {
                var5_5.addAll((Collection)this.search(this.splitString(this.params.getSearchForSerialNumberIn()), var11_12, var2_2));
            }
        }
        if (var6_6.size() != 0) return var5_5;
        if (var8_8 != null) return var5_5;
        var5_5.addAll((Collection)this.search(var3_3, "*", var2_2));
        return var5_5;
    }

    private List cRLIssuerSearch(X509CRLStoreSelector x509CRLStoreSelector, String[] arrstring, String[] arrstring2, String[] arrstring3) {
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        if (x509CRLStoreSelector.getIssuers() != null) {
            hashSet.addAll(x509CRLStoreSelector.getIssuers());
        }
        if (x509CRLStoreSelector.getCertificateChecking() != null) {
            hashSet.add((Object)this.getCertificateIssuer(x509CRLStoreSelector.getCertificateChecking()));
        }
        if (x509CRLStoreSelector.getAttrCertificateChecking() != null) {
            Principal[] arrprincipal = x509CRLStoreSelector.getAttrCertificateChecking().getIssuer().getPrincipals();
            for (int i = 0; i < arrprincipal.length; ++i) {
                if (!(arrprincipal[i] instanceof X500Principal)) continue;
                hashSet.add((Object)arrprincipal[i]);
            }
        }
        Iterator iterator = hashSet.iterator();
        String string = null;
        while (iterator.hasNext()) {
            String string2 = ((X500Principal)iterator.next()).getName("RFC1779");
            for (int i = 0; i < arrstring3.length; ++i) {
                String string3 = this.parseDN(string2, arrstring3[i]);
                arrayList.addAll((Collection)this.search(arrstring2, "*" + string3 + "*", arrstring));
            }
            string = string2;
        }
        if (string == null) {
            arrayList.addAll((Collection)this.search(arrstring2, "*", arrstring));
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     */
    private List certSubjectSerialSearch(X509CertStoreSelector x509CertStoreSelector, String[] arrstring, String[] arrstring2, String[] arrstring3) {
        String string;
        String string2;
        ArrayList arrayList = new ArrayList();
        String string3 = this.getSubjectAsString(x509CertStoreSelector);
        BigInteger bigInteger = x509CertStoreSelector.getSerialNumber();
        String string4 = null;
        if (bigInteger != null) {
            string4 = x509CertStoreSelector.getSerialNumber().toString();
        }
        if (x509CertStoreSelector.getCertificate() != null) {
            String string5 = x509CertStoreSelector.getCertificate().getSubjectX500Principal().getName("RFC1779");
            String string6 = x509CertStoreSelector.getCertificate().getSerialNumber().toString();
            string = string5;
            string2 = string6;
        } else {
            string = string3;
            string2 = string4;
        }
        if (string != null) {
            for (int i = 0; i < arrstring3.length; ++i) {
                String string7 = this.parseDN(string, arrstring3[i]);
                arrayList.addAll((Collection)this.search(arrstring2, "*" + string7 + "*", arrstring));
            }
        }
        if (string2 != null && this.params.getSearchForSerialNumberIn() != null) {
            arrayList.addAll((Collection)this.search(this.splitString(this.params.getSearchForSerialNumberIn()), string2, arrstring));
        }
        if (string2 == null && string == null) {
            arrayList.addAll((Collection)this.search(arrstring2, "*", arrstring));
        }
        return arrayList;
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

    private Set createAttributeCertificates(List list, X509AttributeCertStoreSelector x509AttributeCertStoreSelector) {
        HashSet hashSet = new HashSet();
        Iterator iterator = list.iterator();
        X509AttrCertParser x509AttrCertParser = new X509AttrCertParser();
        while (iterator.hasNext()) {
            try {
                x509AttrCertParser.engineInit((InputStream)new ByteArrayInputStream((byte[])iterator.next()));
                X509AttributeCertificate x509AttributeCertificate = (X509AttributeCertificate)x509AttrCertParser.engineRead();
                if (!x509AttributeCertStoreSelector.match(x509AttributeCertificate)) continue;
                hashSet.add((Object)x509AttributeCertificate);
            }
            catch (StreamParsingException streamParsingException) {}
        }
        return hashSet;
    }

    private Set createCRLs(List list, X509CRLStoreSelector x509CRLStoreSelector) {
        HashSet hashSet = new HashSet();
        X509CRLParser x509CRLParser = new X509CRLParser();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            try {
                x509CRLParser.engineInit((InputStream)new ByteArrayInputStream((byte[])iterator.next()));
                X509CRL x509CRL = (X509CRL)x509CRLParser.engineRead();
                if (!x509CRLStoreSelector.match((Object)x509CRL)) continue;
                hashSet.add((Object)x509CRL);
            }
            catch (StreamParsingException streamParsingException) {}
        }
        return hashSet;
    }

    private Set createCerts(List list, X509CertStoreSelector x509CertStoreSelector) {
        HashSet hashSet = new HashSet();
        Iterator iterator = list.iterator();
        X509CertParser x509CertParser = new X509CertParser();
        while (iterator.hasNext()) {
            try {
                x509CertParser.engineInit((InputStream)new ByteArrayInputStream((byte[])iterator.next()));
                X509Certificate x509Certificate = (X509Certificate)x509CertParser.engineRead();
                if (!x509CertStoreSelector.match((Object)x509Certificate)) continue;
                hashSet.add((Object)x509Certificate);
            }
            catch (Exception exception) {}
        }
        return hashSet;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private Set createCrossCertificatePairs(List var1_1, X509CertPairStoreSelector var2_2) {
        var3_3 = new HashSet();
        var4_4 = 0;
        while (var4_4 < var1_1.size()) {
            block10 : {
                try {
                    var5_5 = new X509CertPairParser();
                    var5_5.engineInit((InputStream)new ByteArrayInputStream((byte[])var1_1.get(var4_4)));
                    var13_13 = var17_16 = (X509CertificatePair)var5_5.engineRead();
                    var7_7 = var4_4;
                    ** GOTO lbl18
                }
                catch (StreamParsingException var9_9) {
                    var10_10 = (byte[])var1_1.get(var4_4);
                    var11_11 = (byte[])var1_1.get(var4_4 + 1);
                    var12_12 = new X509CertificatePair(new CertificatePair(Certificate.getInstance(new ASN1InputStream(var10_10).readObject()), Certificate.getInstance(new ASN1InputStream(var11_11).readObject())));
                    var7_7 = var4_4 + 1;
                    var13_13 = var12_12;
lbl18: // 3 sources:
                    if (var2_2.match(var13_13)) {
                        var3_3.add((Object)var13_13);
                    }
                    break block10;
                    catch (IOException var8_8) {
                        var7_7 = var4_4;
                        break block10;
                    }
                    catch (IOException var15_15) {
                        break block10;
                    }
                    catch (CertificateParsingException var6_6) {
                        var7_7 = var4_4;
                        break block10;
                    }
                }
                catch (CertificateParsingException var14_14) {}
            }
            var4_4 = var7_7 + 1;
        }
        return var3_3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private List crossCertificatePairSubjectSearch(X509CertPairStoreSelector x509CertPairStoreSelector, String[] arrstring, String[] arrstring2, String[] arrstring3) {
        String string;
        ArrayList arrayList = new ArrayList();
        X509CertStoreSelector x509CertStoreSelector = x509CertPairStoreSelector.getForwardSelector();
        String string2 = null;
        if (x509CertStoreSelector != null) {
            string2 = this.getSubjectAsString(x509CertPairStoreSelector.getForwardSelector());
        }
        if ((string = x509CertPairStoreSelector.getCertPair() != null && x509CertPairStoreSelector.getCertPair().getForward() != null ? x509CertPairStoreSelector.getCertPair().getForward().getSubjectX500Principal().getName("RFC1779") : string2) != null) {
            for (int i = 0; i < arrstring3.length; ++i) {
                String string3 = this.parseDN(string, arrstring3[i]);
                arrayList.addAll((Collection)this.search(arrstring2, "*" + string3 + "*", arrstring));
            }
        }
        if (string == null) {
            arrayList.addAll((Collection)this.search(arrstring2, "*", arrstring));
        }
        return arrayList;
    }

    private X500Principal getCertificateIssuer(X509Certificate x509Certificate) {
        return x509Certificate.getIssuerX500Principal();
    }

    private List getFromCache(String string) {
        List list = (List)this.cacheMap.get((Object)string);
        long l = System.currentTimeMillis();
        if (list != null) {
            if (((Date)list.get(0)).getTime() < l - lifeTime) {
                return null;
            }
            return (List)list.get(1);
        }
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private String getSubjectAsString(X509CertStoreSelector x509CertStoreSelector) {
        byte[] arrby;
        try {
            arrby = x509CertStoreSelector.getSubjectAsBytes();
            if (arrby == null) return null;
        }
        catch (IOException iOException) {
            throw new StoreException("exception processing name: " + iOException.getMessage(), iOException);
        }
        return new X500Principal(arrby).getName("RFC1779");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private String parseDN(String string, String string2) {
        int n = string.toLowerCase().indexOf(string2.toLowerCase() + "=");
        if (n == -1) {
            return "";
        }
        String string3 = string.substring(n + string2.length());
        int n2 = string3.indexOf(44);
        if (n2 == -1) {
            n2 = string3.length();
        }
        while (string3.charAt(n2 - 1) == '\\') {
            if ((n2 = string3.indexOf(44, n2 + 1)) != -1) continue;
            n2 = string3.length();
        }
        String string4 = string3.substring(0, n2);
        String string5 = string4.substring(1 + string4.indexOf(61));
        if (string5.charAt(0) == ' ') {
            string5 = string5.substring(1);
        }
        if (string5.startsWith("\"")) {
            string5 = string5.substring(1);
        }
        if (!string5.endsWith("\"")) return string5;
        return string5.substring(0, -1 + string5.length());
    }

    /*
     * Exception decompiling
     */
    private List search(String[] var1_1, String var2_2, String[] var3_3) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [5[CATCHBLOCK]], but top level block is 7[CATCHBLOCK]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    private String[] splitString(String string) {
        return string.split("\\s+");
    }

    public Collection getAACertificates(X509AttributeCertStoreSelector x509AttributeCertStoreSelector) {
        String[] arrstring;
        String[] arrstring2;
        String[] arrstring3 = this.splitString(this.params.getAACertificateAttribute());
        Set set = this.createAttributeCertificates(this.attrCertSubjectSerialSearch(x509AttributeCertStoreSelector, arrstring3, arrstring = this.splitString(this.params.getLdapAACertificateAttributeName()), arrstring2 = this.splitString(this.params.getAACertificateSubjectAttributeName())), x509AttributeCertStoreSelector);
        if (set.size() == 0) {
            set.addAll((Collection)this.createAttributeCertificates(this.attrCertSubjectSerialSearch(new X509AttributeCertStoreSelector(), arrstring3, arrstring, arrstring2), x509AttributeCertStoreSelector));
        }
        return set;
    }

    public Collection getAttributeAuthorityRevocationLists(X509CRLStoreSelector x509CRLStoreSelector) {
        String[] arrstring;
        String[] arrstring2;
        String[] arrstring3 = this.splitString(this.params.getAttributeAuthorityRevocationListAttribute());
        Set set = this.createCRLs(this.cRLIssuerSearch(x509CRLStoreSelector, arrstring3, arrstring = this.splitString(this.params.getLdapAttributeAuthorityRevocationListAttributeName()), arrstring2 = this.splitString(this.params.getAttributeAuthorityRevocationListIssuerAttributeName())), x509CRLStoreSelector);
        if (set.size() == 0) {
            set.addAll((Collection)this.createCRLs(this.cRLIssuerSearch(new X509CRLStoreSelector(), arrstring3, arrstring, arrstring2), x509CRLStoreSelector));
        }
        return set;
    }

    public Collection getAttributeCertificateAttributes(X509AttributeCertStoreSelector x509AttributeCertStoreSelector) {
        String[] arrstring;
        String[] arrstring2;
        String[] arrstring3 = this.splitString(this.params.getAttributeCertificateAttributeAttribute());
        Set set = this.createAttributeCertificates(this.attrCertSubjectSerialSearch(x509AttributeCertStoreSelector, arrstring3, arrstring = this.splitString(this.params.getLdapAttributeCertificateAttributeAttributeName()), arrstring2 = this.splitString(this.params.getAttributeCertificateAttributeSubjectAttributeName())), x509AttributeCertStoreSelector);
        if (set.size() == 0) {
            set.addAll((Collection)this.createAttributeCertificates(this.attrCertSubjectSerialSearch(new X509AttributeCertStoreSelector(), arrstring3, arrstring, arrstring2), x509AttributeCertStoreSelector));
        }
        return set;
    }

    public Collection getAttributeCertificateRevocationLists(X509CRLStoreSelector x509CRLStoreSelector) {
        String[] arrstring;
        String[] arrstring2;
        String[] arrstring3 = this.splitString(this.params.getAttributeCertificateRevocationListAttribute());
        Set set = this.createCRLs(this.cRLIssuerSearch(x509CRLStoreSelector, arrstring3, arrstring = this.splitString(this.params.getLdapAttributeCertificateRevocationListAttributeName()), arrstring2 = this.splitString(this.params.getAttributeCertificateRevocationListIssuerAttributeName())), x509CRLStoreSelector);
        if (set.size() == 0) {
            set.addAll((Collection)this.createCRLs(this.cRLIssuerSearch(new X509CRLStoreSelector(), arrstring3, arrstring, arrstring2), x509CRLStoreSelector));
        }
        return set;
    }

    public Collection getAttributeDescriptorCertificates(X509AttributeCertStoreSelector x509AttributeCertStoreSelector) {
        String[] arrstring;
        String[] arrstring2;
        String[] arrstring3 = this.splitString(this.params.getAttributeDescriptorCertificateAttribute());
        Set set = this.createAttributeCertificates(this.attrCertSubjectSerialSearch(x509AttributeCertStoreSelector, arrstring3, arrstring = this.splitString(this.params.getLdapAttributeDescriptorCertificateAttributeName()), arrstring2 = this.splitString(this.params.getAttributeDescriptorCertificateSubjectAttributeName())), x509AttributeCertStoreSelector);
        if (set.size() == 0) {
            set.addAll((Collection)this.createAttributeCertificates(this.attrCertSubjectSerialSearch(new X509AttributeCertStoreSelector(), arrstring3, arrstring, arrstring2), x509AttributeCertStoreSelector));
        }
        return set;
    }

    public Collection getAuthorityRevocationLists(X509CRLStoreSelector x509CRLStoreSelector) {
        String[] arrstring;
        String[] arrstring2;
        String[] arrstring3 = this.splitString(this.params.getAuthorityRevocationListAttribute());
        Set set = this.createCRLs(this.cRLIssuerSearch(x509CRLStoreSelector, arrstring3, arrstring = this.splitString(this.params.getLdapAuthorityRevocationListAttributeName()), arrstring2 = this.splitString(this.params.getAuthorityRevocationListIssuerAttributeName())), x509CRLStoreSelector);
        if (set.size() == 0) {
            set.addAll((Collection)this.createCRLs(this.cRLIssuerSearch(new X509CRLStoreSelector(), arrstring3, arrstring, arrstring2), x509CRLStoreSelector));
        }
        return set;
    }

    public Collection getCACertificates(X509CertStoreSelector x509CertStoreSelector) {
        String[] arrstring;
        String[] arrstring2;
        String[] arrstring3 = this.splitString(this.params.getCACertificateAttribute());
        Set set = this.createCerts(this.certSubjectSerialSearch(x509CertStoreSelector, arrstring3, arrstring = this.splitString(this.params.getLdapCACertificateAttributeName()), arrstring2 = this.splitString(this.params.getCACertificateSubjectAttributeName())), x509CertStoreSelector);
        if (set.size() == 0) {
            set.addAll((Collection)this.createCerts(this.certSubjectSerialSearch(new X509CertStoreSelector(), arrstring3, arrstring, arrstring2), x509CertStoreSelector));
        }
        return set;
    }

    public Collection getCertificateRevocationLists(X509CRLStoreSelector x509CRLStoreSelector) {
        String[] arrstring;
        String[] arrstring2;
        String[] arrstring3 = this.splitString(this.params.getCertificateRevocationListAttribute());
        Set set = this.createCRLs(this.cRLIssuerSearch(x509CRLStoreSelector, arrstring3, arrstring = this.splitString(this.params.getLdapCertificateRevocationListAttributeName()), arrstring2 = this.splitString(this.params.getCertificateRevocationListIssuerAttributeName())), x509CRLStoreSelector);
        if (set.size() == 0) {
            set.addAll((Collection)this.createCRLs(this.cRLIssuerSearch(new X509CRLStoreSelector(), arrstring3, arrstring, arrstring2), x509CRLStoreSelector));
        }
        return set;
    }

    public Collection getCrossCertificatePairs(X509CertPairStoreSelector x509CertPairStoreSelector) {
        String[] arrstring;
        String[] arrstring2;
        String[] arrstring3 = this.splitString(this.params.getCrossCertificateAttribute());
        Set set = this.createCrossCertificatePairs(this.crossCertificatePairSubjectSearch(x509CertPairStoreSelector, arrstring3, arrstring = this.splitString(this.params.getLdapCrossCertificateAttributeName()), arrstring2 = this.splitString(this.params.getCrossCertificateSubjectAttributeName())), x509CertPairStoreSelector);
        if (set.size() == 0) {
            X509CertStoreSelector x509CertStoreSelector = new X509CertStoreSelector();
            X509CertPairStoreSelector x509CertPairStoreSelector2 = new X509CertPairStoreSelector();
            x509CertPairStoreSelector2.setForwardSelector(x509CertStoreSelector);
            x509CertPairStoreSelector2.setReverseSelector(x509CertStoreSelector);
            set.addAll((Collection)this.createCrossCertificatePairs(this.crossCertificatePairSubjectSearch(x509CertPairStoreSelector2, arrstring3, arrstring, arrstring2), x509CertPairStoreSelector));
        }
        return set;
    }

    public Collection getDeltaCertificateRevocationLists(X509CRLStoreSelector x509CRLStoreSelector) {
        String[] arrstring;
        String[] arrstring2;
        String[] arrstring3 = this.splitString(this.params.getDeltaRevocationListAttribute());
        Set set = this.createCRLs(this.cRLIssuerSearch(x509CRLStoreSelector, arrstring3, arrstring = this.splitString(this.params.getLdapDeltaRevocationListAttributeName()), arrstring2 = this.splitString(this.params.getDeltaRevocationListIssuerAttributeName())), x509CRLStoreSelector);
        if (set.size() == 0) {
            set.addAll((Collection)this.createCRLs(this.cRLIssuerSearch(new X509CRLStoreSelector(), arrstring3, arrstring, arrstring2), x509CRLStoreSelector));
        }
        return set;
    }

    public Collection getUserCertificates(X509CertStoreSelector x509CertStoreSelector) {
        String[] arrstring;
        String[] arrstring2;
        String[] arrstring3 = this.splitString(this.params.getUserCertificateAttribute());
        Set set = this.createCerts(this.certSubjectSerialSearch(x509CertStoreSelector, arrstring3, arrstring = this.splitString(this.params.getLdapUserCertificateAttributeName()), arrstring2 = this.splitString(this.params.getUserCertificateSubjectAttributeName())), x509CertStoreSelector);
        if (set.size() == 0) {
            set.addAll((Collection)this.createCerts(this.certSubjectSerialSearch(new X509CertStoreSelector(), arrstring3, arrstring, arrstring2), x509CertStoreSelector));
        }
        return set;
    }
}

