/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.HashMap
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.Map$Entry
 *  java.util.Set
 */
package org.bouncycastle.jce.provider;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralSubtree;
import org.bouncycastle.jce.provider.PKIXNameConstraintValidatorException;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Strings;

public class PKIXNameConstraintValidator {
    private Set excludedSubtreesDN = new HashSet();
    private Set excludedSubtreesDNS = new HashSet();
    private Set excludedSubtreesEmail = new HashSet();
    private Set excludedSubtreesIP = new HashSet();
    private Set excludedSubtreesURI = new HashSet();
    private Set permittedSubtreesDN;
    private Set permittedSubtreesDNS;
    private Set permittedSubtreesEmail;
    private Set permittedSubtreesIP;
    private Set permittedSubtreesURI;

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void checkExcludedDN(Set set, ASN1Sequence aSN1Sequence) {
        if (set.isEmpty()) {
            return;
        }
        Iterator iterator = set.iterator();
        do {
            if (!iterator.hasNext()) return;
        } while (!PKIXNameConstraintValidator.withinDNSubtree(aSN1Sequence, (ASN1Sequence)iterator.next()));
        throw new PKIXNameConstraintValidatorException("Subject distinguished name is from an excluded subtree");
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void checkExcludedDNS(Set set, String string) {
        String string2;
        if (set.isEmpty()) {
            return;
        }
        Iterator iterator = set.iterator();
        do {
            if (!iterator.hasNext()) return;
            string2 = (String)iterator.next();
            if (this.withinDomain(string, string2)) throw new PKIXNameConstraintValidatorException("DNS is from an excluded subtree.");
        } while (!string.equalsIgnoreCase(string2));
        throw new PKIXNameConstraintValidatorException("DNS is from an excluded subtree.");
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void checkExcludedEmail(Set set, String string) {
        if (set.isEmpty()) {
            return;
        }
        Iterator iterator = set.iterator();
        do {
            if (!iterator.hasNext()) return;
        } while (!this.emailIsConstrained(string, (String)iterator.next()));
        throw new PKIXNameConstraintValidatorException("Email address is from an excluded subtree.");
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void checkExcludedIP(Set set, byte[] arrby) {
        if (set.isEmpty()) {
            return;
        }
        Iterator iterator = set.iterator();
        do {
            if (!iterator.hasNext()) return;
        } while (!this.isIPConstrained(arrby, (byte[])iterator.next()));
        throw new PKIXNameConstraintValidatorException("IP is from an excluded subtree.");
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void checkExcludedURI(Set set, String string) {
        if (set.isEmpty()) {
            return;
        }
        Iterator iterator = set.iterator();
        do {
            if (!iterator.hasNext()) return;
        } while (!this.isUriConstrained(string, (String)iterator.next()));
        throw new PKIXNameConstraintValidatorException("URI is from an excluded subtree.");
    }

    /*
     * Enabled aggressive block sorting
     */
    private void checkPermittedDN(Set set, ASN1Sequence aSN1Sequence) {
        if (set == null || set.isEmpty() && aSN1Sequence.size() == 0) {
            return;
        }
        Iterator iterator = set.iterator();
        do {
            if (iterator.hasNext()) continue;
            throw new PKIXNameConstraintValidatorException("Subject distinguished name is not from a permitted subtree");
        } while (!PKIXNameConstraintValidator.withinDNSubtree(aSN1Sequence, (ASN1Sequence)iterator.next()));
    }

    /*
     * Enabled aggressive block sorting
     */
    private void checkPermittedDNS(Set set, String string) {
        if (set == null) return;
        for (String string2 : set) {
            if (this.withinDomain(string, string2)) return;
            {
                if (!string.equalsIgnoreCase(string2)) continue;
                return;
            }
        }
        if (string.length() != 0 || set.size() != 0) throw new PKIXNameConstraintValidatorException("DNS is not from a permitted subtree.");
    }

    /*
     * Enabled aggressive block sorting
     */
    private void checkPermittedEmail(Set set, String string) {
        block4 : {
            block3 : {
                if (set == null) break block3;
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    if (!this.emailIsConstrained(string, (String)iterator.next())) continue;
                    return;
                }
                if (string.length() != 0 || set.size() != 0) break block4;
            }
            return;
        }
        throw new PKIXNameConstraintValidatorException("Subject email address is not from a permitted subtree.");
    }

    /*
     * Enabled aggressive block sorting
     */
    private void checkPermittedIP(Set set, byte[] arrby) {
        block4 : {
            block3 : {
                if (set == null) break block3;
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    if (!this.isIPConstrained(arrby, (byte[])iterator.next())) continue;
                    return;
                }
                if (arrby.length != 0 || set.size() != 0) break block4;
            }
            return;
        }
        throw new PKIXNameConstraintValidatorException("IP is not from a permitted subtree.");
    }

    /*
     * Enabled aggressive block sorting
     */
    private void checkPermittedURI(Set set, String string) {
        block4 : {
            block3 : {
                if (set == null) break block3;
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    if (!this.isUriConstrained(string, (String)iterator.next())) continue;
                    return;
                }
                if (string.length() != 0 || set.size() != 0) break block4;
            }
            return;
        }
        throw new PKIXNameConstraintValidatorException("URI is not from a permitted subtree.");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean collectionsAreEqual(Collection collection, Collection collection2) {
        boolean bl;
        if (collection == collection2) {
            return true;
        }
        if (collection == null) return false;
        if (collection2 == null) {
            return false;
        }
        if (collection.size() != collection2.size()) {
            return false;
        }
        Iterator iterator = collection.iterator();
        do {
            if (!iterator.hasNext()) return true;
            Object object = iterator.next();
            Iterator iterator2 = collection2.iterator();
            do {
                if (!iterator2.hasNext()) return false;
            } while (!this.equals(object, iterator2.next()));
            bl = true;
        } while (bl);
        return false;
    }

    private static int compareTo(byte[] arrby, byte[] arrby2) {
        if (Arrays.areEqual(arrby, arrby2)) {
            return 0;
        }
        if (Arrays.areEqual(PKIXNameConstraintValidator.max(arrby, arrby2), arrby)) {
            return 1;
        }
        return -1;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean emailIsConstrained(String string, String string2) {
        String string3 = string.substring(1 + string.indexOf(64));
        if (string2.indexOf(64) != -1) {
            if (!string.equalsIgnoreCase(string2) && !string3.equalsIgnoreCase(string2.substring(1))) return false;
            return true;
        }
        if (string2.charAt(0) != '.') {
            if (!string3.equalsIgnoreCase(string2)) return false;
            return true;
        }
        if (this.withinDomain(string3, string2)) return true;
        return false;
    }

    private boolean equals(Object object, Object object2) {
        if (object == object2) {
            return true;
        }
        if (object == null || object2 == null) {
            return false;
        }
        if (object instanceof byte[] && object2 instanceof byte[]) {
            return Arrays.areEqual((byte[])object, (byte[])object2);
        }
        return object.equals(object2);
    }

    private static String extractHostFromURL(String string) {
        String string2;
        String string3;
        String string4 = string.substring(1 + string.indexOf(58));
        if (string4.indexOf("//") != -1) {
            string4 = string4.substring(2 + string4.indexOf("//"));
        }
        if (string4.lastIndexOf(58) != -1) {
            string4 = string4.substring(0, string4.lastIndexOf(58));
        }
        if ((string3 = (string2 = string4.substring(1 + string4.indexOf(58))).substring(1 + string2.indexOf(64))).indexOf(47) != -1) {
            string3 = string3.substring(0, string3.indexOf(47));
        }
        return string3;
    }

    private byte[][] extractIPsAndSubnetMasks(byte[] arrby, byte[] arrby2) {
        int n = arrby.length / 2;
        byte[] arrby3 = new byte[n];
        byte[] arrby4 = new byte[n];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)0, (int)n);
        System.arraycopy((Object)arrby, (int)n, (Object)arrby4, (int)0, (int)n);
        byte[] arrby5 = new byte[n];
        byte[] arrby6 = new byte[n];
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby5, (int)0, (int)n);
        System.arraycopy((Object)arrby2, (int)n, (Object)arrby6, (int)0, (int)n);
        return new byte[][]{arrby3, arrby4, arrby5, arrby6};
    }

    private String extractNameAsString(GeneralName generalName) {
        return DERIA5String.getInstance(generalName.getName()).getString();
    }

    /*
     * Enabled aggressive block sorting
     */
    private int hashCollection(Collection collection) {
        if (collection == null) {
            return 0;
        }
        Iterator iterator = collection.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            Object object = iterator.next();
            int n2 = object instanceof byte[] ? n + Arrays.hashCode((byte[])object) : n + object.hashCode();
            n = n2;
        }
        return n;
    }

    private Set intersectDN(Set set, Set set2) {
        HashSet hashSet = new HashSet();
        Iterator iterator = set2.iterator();
        while (iterator.hasNext()) {
            ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(((GeneralSubtree)iterator.next()).getBase().getName().toASN1Primitive());
            if (set == null) {
                if (aSN1Sequence == null) continue;
                hashSet.add((Object)aSN1Sequence);
                continue;
            }
            for (ASN1Sequence aSN1Sequence2 : set) {
                if (PKIXNameConstraintValidator.withinDNSubtree(aSN1Sequence, aSN1Sequence2)) {
                    hashSet.add((Object)aSN1Sequence);
                    continue;
                }
                if (!PKIXNameConstraintValidator.withinDNSubtree(aSN1Sequence2, aSN1Sequence)) continue;
                hashSet.add((Object)aSN1Sequence2);
            }
        }
        return hashSet;
    }

    private Set intersectDNS(Set set, Set set2) {
        HashSet hashSet = new HashSet();
        Iterator iterator = set2.iterator();
        while (iterator.hasNext()) {
            String string = this.extractNameAsString(((GeneralSubtree)iterator.next()).getBase());
            if (set == null) {
                if (string == null) continue;
                hashSet.add((Object)string);
                continue;
            }
            for (String string2 : set) {
                if (this.withinDomain(string2, string)) {
                    hashSet.add((Object)string2);
                    continue;
                }
                if (!this.withinDomain(string, string2)) continue;
                hashSet.add((Object)string);
            }
        }
        return hashSet;
    }

    private Set intersectEmail(Set set, Set set2) {
        HashSet hashSet = new HashSet();
        Iterator iterator = set2.iterator();
        while (iterator.hasNext()) {
            String string = this.extractNameAsString(((GeneralSubtree)iterator.next()).getBase());
            if (set == null) {
                if (string == null) continue;
                hashSet.add((Object)string);
                continue;
            }
            Iterator iterator2 = set.iterator();
            while (iterator2.hasNext()) {
                this.intersectEmail(string, (String)iterator2.next(), (Set)hashSet);
            }
        }
        return hashSet;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void intersectEmail(String string, String string2, Set set) {
        if (string.indexOf(64) != -1) {
            String string3 = string.substring(1 + string.indexOf(64));
            if (string2.indexOf(64) != -1) {
                if (!string.equalsIgnoreCase(string2)) return;
                {
                    set.add((Object)string);
                    return;
                }
            } else if (string2.startsWith(".")) {
                if (!this.withinDomain(string3, string2)) return;
                {
                    set.add((Object)string);
                    return;
                }
            } else {
                if (!string3.equalsIgnoreCase(string2)) return;
                {
                    set.add((Object)string);
                    return;
                }
            }
        } else if (string.startsWith(".")) {
            if (string2.indexOf(64) != -1) {
                if (!this.withinDomain(string2.substring(1 + string.indexOf(64)), string)) return;
                {
                    set.add((Object)string2);
                    return;
                }
            } else if (string2.startsWith(".")) {
                if (this.withinDomain(string, string2) || string.equalsIgnoreCase(string2)) {
                    set.add((Object)string);
                    return;
                }
                if (!this.withinDomain(string2, string)) return;
                {
                    set.add((Object)string2);
                    return;
                }
            } else {
                if (!this.withinDomain(string2, string)) return;
                {
                    set.add((Object)string2);
                    return;
                }
            }
        } else if (string2.indexOf(64) != -1) {
            if (!string2.substring(1 + string2.indexOf(64)).equalsIgnoreCase(string)) return;
            {
                set.add((Object)string2);
                return;
            }
        } else if (string2.startsWith(".")) {
            if (!this.withinDomain(string, string2)) return;
            {
                set.add((Object)string);
                return;
            }
        } else {
            if (!string.equalsIgnoreCase(string2)) return;
            {
                set.add((Object)string);
                return;
            }
        }
    }

    private Set intersectIP(Set set, Set set2) {
        HashSet hashSet = new HashSet();
        Iterator iterator = set2.iterator();
        while (iterator.hasNext()) {
            byte[] arrby = ASN1OctetString.getInstance(((GeneralSubtree)iterator.next()).getBase().getName()).getOctets();
            if (set == null) {
                if (arrby == null) continue;
                hashSet.add((Object)arrby);
                continue;
            }
            Iterator iterator2 = set.iterator();
            while (iterator2.hasNext()) {
                hashSet.addAll((Collection)this.intersectIPRange((byte[])iterator2.next(), arrby));
            }
        }
        return hashSet;
    }

    private Set intersectIPRange(byte[] arrby, byte[] arrby2) {
        if (arrby.length != arrby2.length) {
            return Collections.EMPTY_SET;
        }
        byte[][] arrby3 = this.extractIPsAndSubnetMasks(arrby, arrby2);
        byte[] arrby4 = arrby3[0];
        byte[] arrby5 = arrby3[1];
        byte[] arrby6 = arrby3[2];
        byte[] arrby7 = arrby3[3];
        byte[][] arrby8 = this.minMaxIPs(arrby4, arrby5, arrby6, arrby7);
        byte[] arrby9 = PKIXNameConstraintValidator.min(arrby8[1], arrby8[3]);
        if (PKIXNameConstraintValidator.compareTo(PKIXNameConstraintValidator.max(arrby8[0], arrby8[2]), arrby9) == 1) {
            return Collections.EMPTY_SET;
        }
        return Collections.singleton((Object)this.ipWithSubnetMask(PKIXNameConstraintValidator.or(arrby8[0], arrby8[2]), PKIXNameConstraintValidator.or(arrby5, arrby7)));
    }

    private Set intersectURI(Set set, Set set2) {
        HashSet hashSet = new HashSet();
        Iterator iterator = set2.iterator();
        while (iterator.hasNext()) {
            String string = this.extractNameAsString(((GeneralSubtree)iterator.next()).getBase());
            if (set == null) {
                if (string == null) continue;
                hashSet.add((Object)string);
                continue;
            }
            Iterator iterator2 = set.iterator();
            while (iterator2.hasNext()) {
                this.intersectURI((String)iterator2.next(), string, (Set)hashSet);
            }
        }
        return hashSet;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void intersectURI(String string, String string2, Set set) {
        if (string.indexOf(64) != -1) {
            String string3 = string.substring(1 + string.indexOf(64));
            if (string2.indexOf(64) != -1) {
                if (!string.equalsIgnoreCase(string2)) return;
                {
                    set.add((Object)string);
                    return;
                }
            } else if (string2.startsWith(".")) {
                if (!this.withinDomain(string3, string2)) return;
                {
                    set.add((Object)string);
                    return;
                }
            } else {
                if (!string3.equalsIgnoreCase(string2)) return;
                {
                    set.add((Object)string);
                    return;
                }
            }
        } else if (string.startsWith(".")) {
            if (string2.indexOf(64) != -1) {
                if (!this.withinDomain(string2.substring(1 + string.indexOf(64)), string)) return;
                {
                    set.add((Object)string2);
                    return;
                }
            } else if (string2.startsWith(".")) {
                if (this.withinDomain(string, string2) || string.equalsIgnoreCase(string2)) {
                    set.add((Object)string);
                    return;
                }
                if (!this.withinDomain(string2, string)) return;
                {
                    set.add((Object)string2);
                    return;
                }
            } else {
                if (!this.withinDomain(string2, string)) return;
                {
                    set.add((Object)string2);
                    return;
                }
            }
        } else if (string2.indexOf(64) != -1) {
            if (!string2.substring(1 + string2.indexOf(64)).equalsIgnoreCase(string)) return;
            {
                set.add((Object)string2);
                return;
            }
        } else if (string2.startsWith(".")) {
            if (!this.withinDomain(string, string2)) return;
            {
                set.add((Object)string);
                return;
            }
        } else {
            if (!string.equalsIgnoreCase(string2)) return;
            {
                set.add((Object)string);
                return;
            }
        }
    }

    private byte[] ipWithSubnetMask(byte[] arrby, byte[] arrby2) {
        int n = arrby.length;
        byte[] arrby3 = new byte[n * 2];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)0, (int)n);
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)n, (int)n);
        return arrby3;
    }

    private boolean isIPConstrained(byte[] arrby, byte[] arrby2) {
        int n = arrby.length;
        if (n != arrby2.length / 2) {
            return false;
        }
        byte[] arrby3 = new byte[n];
        System.arraycopy((Object)arrby2, (int)n, (Object)arrby3, (int)0, (int)n);
        byte[] arrby4 = new byte[n];
        byte[] arrby5 = new byte[n];
        for (int i = 0; i < n; ++i) {
            arrby4[i] = (byte)(arrby2[i] & arrby3[i]);
            arrby5[i] = (byte)(arrby[i] & arrby3[i]);
        }
        return Arrays.areEqual(arrby4, arrby5);
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean isUriConstrained(String string, String string2) {
        String string3 = PKIXNameConstraintValidator.extractHostFromURL(string);
        return !string2.startsWith(".") ? string3.equalsIgnoreCase(string2) : this.withinDomain(string3, string2);
    }

    private static byte[] max(byte[] arrby, byte[] arrby2) {
        for (int i = 0; i < arrby.length; ++i) {
            if ((65535 & arrby[i]) <= (65535 & arrby2[i])) continue;
            return arrby;
        }
        return arrby2;
    }

    private static byte[] min(byte[] arrby, byte[] arrby2) {
        for (int i = 0; i < arrby.length; ++i) {
            if ((65535 & arrby[i]) >= (65535 & arrby2[i])) continue;
            return arrby;
        }
        return arrby2;
    }

    private byte[][] minMaxIPs(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4) {
        int n = arrby.length;
        byte[] arrby5 = new byte[n];
        byte[] arrby6 = new byte[n];
        byte[] arrby7 = new byte[n];
        byte[] arrby8 = new byte[n];
        for (int i = 0; i < n; ++i) {
            arrby5[i] = (byte)(arrby[i] & arrby2[i]);
            arrby6[i] = (byte)(arrby[i] & arrby2[i] | -1 ^ arrby2[i]);
            arrby7[i] = (byte)(arrby3[i] & arrby4[i]);
            arrby8[i] = (byte)(arrby3[i] & arrby4[i] | -1 ^ arrby4[i]);
        }
        return new byte[][]{arrby5, arrby6, arrby7, arrby8};
    }

    private static byte[] or(byte[] arrby, byte[] arrby2) {
        byte[] arrby3 = new byte[arrby.length];
        for (int i = 0; i < arrby.length; ++i) {
            arrby3[i] = (byte)(arrby[i] | arrby2[i]);
        }
        return arrby3;
    }

    private String stringifyIP(byte[] arrby) {
        String string = "";
        for (int i = 0; i < arrby.length / 2; ++i) {
            string = string + Integer.toString((int)(255 & arrby[i])) + ".";
        }
        String string2 = string.substring(0, -1 + string.length());
        String string3 = string2 + "/";
        for (int i = arrby.length / 2; i < arrby.length; ++i) {
            string3 = string3 + Integer.toString((int)(255 & arrby[i])) + ".";
        }
        return string3.substring(0, -1 + string3.length());
    }

    private String stringifyIPCollection(Set set) {
        String string = "" + "[";
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            string = string + this.stringifyIP((byte[])iterator.next()) + ",";
        }
        if (string.length() > 1) {
            string = string.substring(0, -1 + string.length());
        }
        return string + "]";
    }

    private Set unionDN(Set set, ASN1Sequence aSN1Sequence) {
        if (set.isEmpty()) {
            if (aSN1Sequence == null) {
                return set;
            }
            set.add((Object)aSN1Sequence);
            return set;
        }
        HashSet hashSet = new HashSet();
        for (ASN1Sequence aSN1Sequence2 : set) {
            if (PKIXNameConstraintValidator.withinDNSubtree(aSN1Sequence, aSN1Sequence2)) {
                hashSet.add((Object)aSN1Sequence2);
                continue;
            }
            if (PKIXNameConstraintValidator.withinDNSubtree(aSN1Sequence2, aSN1Sequence)) {
                hashSet.add((Object)aSN1Sequence);
                continue;
            }
            hashSet.add((Object)aSN1Sequence2);
            hashSet.add((Object)aSN1Sequence);
        }
        return hashSet;
    }

    private Set unionEmail(Set set, String string) {
        if (set.isEmpty()) {
            if (string == null) {
                return set;
            }
            set.add((Object)string);
            return set;
        }
        HashSet hashSet = new HashSet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            this.unionEmail((String)iterator.next(), string, (Set)hashSet);
        }
        return hashSet;
    }

    private void unionEmail(String string, String string2, Set set) {
        if (string.indexOf(64) != -1) {
            String string3 = string.substring(1 + string.indexOf(64));
            if (string2.indexOf(64) != -1) {
                if (string.equalsIgnoreCase(string2)) {
                    set.add((Object)string);
                    return;
                }
                set.add((Object)string);
                set.add((Object)string2);
                return;
            }
            if (string2.startsWith(".")) {
                if (this.withinDomain(string3, string2)) {
                    set.add((Object)string2);
                    return;
                }
                set.add((Object)string);
                set.add((Object)string2);
                return;
            }
            if (string3.equalsIgnoreCase(string2)) {
                set.add((Object)string2);
                return;
            }
            set.add((Object)string);
            set.add((Object)string2);
            return;
        }
        if (string.startsWith(".")) {
            if (string2.indexOf(64) != -1) {
                if (this.withinDomain(string2.substring(1 + string.indexOf(64)), string)) {
                    set.add((Object)string);
                    return;
                }
                set.add((Object)string);
                set.add((Object)string2);
                return;
            }
            if (string2.startsWith(".")) {
                if (this.withinDomain(string, string2) || string.equalsIgnoreCase(string2)) {
                    set.add((Object)string2);
                    return;
                }
                if (this.withinDomain(string2, string)) {
                    set.add((Object)string);
                    return;
                }
                set.add((Object)string);
                set.add((Object)string2);
                return;
            }
            if (this.withinDomain(string2, string)) {
                set.add((Object)string);
                return;
            }
            set.add((Object)string);
            set.add((Object)string2);
            return;
        }
        if (string2.indexOf(64) != -1) {
            if (string2.substring(1 + string.indexOf(64)).equalsIgnoreCase(string)) {
                set.add((Object)string);
                return;
            }
            set.add((Object)string);
            set.add((Object)string2);
            return;
        }
        if (string2.startsWith(".")) {
            if (this.withinDomain(string, string2)) {
                set.add((Object)string2);
                return;
            }
            set.add((Object)string);
            set.add((Object)string2);
            return;
        }
        if (string.equalsIgnoreCase(string2)) {
            set.add((Object)string);
            return;
        }
        set.add((Object)string);
        set.add((Object)string2);
    }

    private Set unionIP(Set set, byte[] arrby) {
        if (set.isEmpty()) {
            if (arrby == null) {
                return set;
            }
            set.add((Object)arrby);
            return set;
        }
        HashSet hashSet = new HashSet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            hashSet.addAll((Collection)this.unionIPRange((byte[])iterator.next(), arrby));
        }
        return hashSet;
    }

    private Set unionIPRange(byte[] arrby, byte[] arrby2) {
        HashSet hashSet = new HashSet();
        if (Arrays.areEqual(arrby, arrby2)) {
            hashSet.add((Object)arrby);
            return hashSet;
        }
        hashSet.add((Object)arrby);
        hashSet.add((Object)arrby2);
        return hashSet;
    }

    private Set unionURI(Set set, String string) {
        if (set.isEmpty()) {
            if (string == null) {
                return set;
            }
            set.add((Object)string);
            return set;
        }
        HashSet hashSet = new HashSet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            this.unionURI((String)iterator.next(), string, (Set)hashSet);
        }
        return hashSet;
    }

    private void unionURI(String string, String string2, Set set) {
        if (string.indexOf(64) != -1) {
            String string3 = string.substring(1 + string.indexOf(64));
            if (string2.indexOf(64) != -1) {
                if (string.equalsIgnoreCase(string2)) {
                    set.add((Object)string);
                    return;
                }
                set.add((Object)string);
                set.add((Object)string2);
                return;
            }
            if (string2.startsWith(".")) {
                if (this.withinDomain(string3, string2)) {
                    set.add((Object)string2);
                    return;
                }
                set.add((Object)string);
                set.add((Object)string2);
                return;
            }
            if (string3.equalsIgnoreCase(string2)) {
                set.add((Object)string2);
                return;
            }
            set.add((Object)string);
            set.add((Object)string2);
            return;
        }
        if (string.startsWith(".")) {
            if (string2.indexOf(64) != -1) {
                if (this.withinDomain(string2.substring(1 + string.indexOf(64)), string)) {
                    set.add((Object)string);
                    return;
                }
                set.add((Object)string);
                set.add((Object)string2);
                return;
            }
            if (string2.startsWith(".")) {
                if (this.withinDomain(string, string2) || string.equalsIgnoreCase(string2)) {
                    set.add((Object)string2);
                    return;
                }
                if (this.withinDomain(string2, string)) {
                    set.add((Object)string);
                    return;
                }
                set.add((Object)string);
                set.add((Object)string2);
                return;
            }
            if (this.withinDomain(string2, string)) {
                set.add((Object)string);
                return;
            }
            set.add((Object)string);
            set.add((Object)string2);
            return;
        }
        if (string2.indexOf(64) != -1) {
            if (string2.substring(1 + string.indexOf(64)).equalsIgnoreCase(string)) {
                set.add((Object)string);
                return;
            }
            set.add((Object)string);
            set.add((Object)string2);
            return;
        }
        if (string2.startsWith(".")) {
            if (this.withinDomain(string, string2)) {
                set.add((Object)string2);
                return;
            }
            set.add((Object)string);
            set.add((Object)string2);
            return;
        }
        if (string.equalsIgnoreCase(string2)) {
            set.add((Object)string);
            return;
        }
        set.add((Object)string);
        set.add((Object)string2);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean withinDNSubtree(ASN1Sequence aSN1Sequence, ASN1Sequence aSN1Sequence2) {
        if (aSN1Sequence2.size() >= 1 && aSN1Sequence2.size() <= aSN1Sequence.size()) {
            int n = -1 + aSN1Sequence2.size();
            do {
                if (n < 0) {
                    return true;
                }
                if (!aSN1Sequence2.getObjectAt(n).equals((Object)aSN1Sequence.getObjectAt(n))) break;
                --n;
            } while (true);
        }
        return false;
    }

    private boolean withinDomain(String string, String string2) {
        if (string2.startsWith(".")) {
            string2 = string2.substring(1);
        }
        String[] arrstring = Strings.split(string2, '.');
        String[] arrstring2 = Strings.split(string, '.');
        if (arrstring2.length <= arrstring.length) {
            return false;
        }
        int n = arrstring2.length - arrstring.length;
        for (int i = -1; i < arrstring.length; ++i) {
            if (!(i == -1 ? arrstring2[i + n].equals((Object)"") : !arrstring[i].equalsIgnoreCase(arrstring2[i + n]))) continue;
            return false;
        }
        return true;
    }

    public void addExcludedSubtree(GeneralSubtree generalSubtree) {
        GeneralName generalName = generalSubtree.getBase();
        switch (generalName.getTagNo()) {
            default: {
                return;
            }
            case 1: {
                this.excludedSubtreesEmail = this.unionEmail(this.excludedSubtreesEmail, this.extractNameAsString(generalName));
                return;
            }
            case 2: {
                this.excludedSubtreesDNS = this.unionDNS(this.excludedSubtreesDNS, this.extractNameAsString(generalName));
                return;
            }
            case 4: {
                this.excludedSubtreesDN = this.unionDN(this.excludedSubtreesDN, (ASN1Sequence)generalName.getName().toASN1Primitive());
                return;
            }
            case 6: {
                this.excludedSubtreesURI = this.unionURI(this.excludedSubtreesURI, this.extractNameAsString(generalName));
                return;
            }
            case 7: 
        }
        this.excludedSubtreesIP = this.unionIP(this.excludedSubtreesIP, ASN1OctetString.getInstance(generalName.getName()).getOctets());
    }

    public void checkExcluded(GeneralName generalName) {
        switch (generalName.getTagNo()) {
            default: {
                return;
            }
            case 1: {
                this.checkExcludedEmail(this.excludedSubtreesEmail, this.extractNameAsString(generalName));
                return;
            }
            case 2: {
                this.checkExcludedDNS(this.excludedSubtreesDNS, DERIA5String.getInstance(generalName.getName()).getString());
                return;
            }
            case 4: {
                this.checkExcludedDN(ASN1Sequence.getInstance(generalName.getName().toASN1Primitive()));
                return;
            }
            case 6: {
                this.checkExcludedURI(this.excludedSubtreesURI, DERIA5String.getInstance(generalName.getName()).getString());
                return;
            }
            case 7: 
        }
        byte[] arrby = ASN1OctetString.getInstance(generalName.getName()).getOctets();
        this.checkExcludedIP(this.excludedSubtreesIP, arrby);
    }

    public void checkExcludedDN(ASN1Sequence aSN1Sequence) {
        this.checkExcludedDN(this.excludedSubtreesDN, aSN1Sequence);
    }

    public void checkPermitted(GeneralName generalName) {
        switch (generalName.getTagNo()) {
            default: {
                return;
            }
            case 1: {
                this.checkPermittedEmail(this.permittedSubtreesEmail, this.extractNameAsString(generalName));
                return;
            }
            case 2: {
                this.checkPermittedDNS(this.permittedSubtreesDNS, DERIA5String.getInstance(generalName.getName()).getString());
                return;
            }
            case 4: {
                this.checkPermittedDN(ASN1Sequence.getInstance(generalName.getName().toASN1Primitive()));
                return;
            }
            case 6: {
                this.checkPermittedURI(this.permittedSubtreesURI, DERIA5String.getInstance(generalName.getName()).getString());
                return;
            }
            case 7: 
        }
        byte[] arrby = ASN1OctetString.getInstance(generalName.getName()).getOctets();
        this.checkPermittedIP(this.permittedSubtreesIP, arrby);
    }

    public void checkPermittedDN(ASN1Sequence aSN1Sequence) {
        this.checkPermittedDN(this.permittedSubtreesDN, aSN1Sequence);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (!(object instanceof PKIXNameConstraintValidator)) break block2;
                PKIXNameConstraintValidator pKIXNameConstraintValidator = (PKIXNameConstraintValidator)object;
                if (this.collectionsAreEqual((Collection)pKIXNameConstraintValidator.excludedSubtreesDN, (Collection)this.excludedSubtreesDN) && this.collectionsAreEqual((Collection)pKIXNameConstraintValidator.excludedSubtreesDNS, (Collection)this.excludedSubtreesDNS) && this.collectionsAreEqual((Collection)pKIXNameConstraintValidator.excludedSubtreesEmail, (Collection)this.excludedSubtreesEmail) && this.collectionsAreEqual((Collection)pKIXNameConstraintValidator.excludedSubtreesIP, (Collection)this.excludedSubtreesIP) && this.collectionsAreEqual((Collection)pKIXNameConstraintValidator.excludedSubtreesURI, (Collection)this.excludedSubtreesURI) && this.collectionsAreEqual((Collection)pKIXNameConstraintValidator.permittedSubtreesDN, (Collection)this.permittedSubtreesDN) && this.collectionsAreEqual((Collection)pKIXNameConstraintValidator.permittedSubtreesDNS, (Collection)this.permittedSubtreesDNS) && this.collectionsAreEqual((Collection)pKIXNameConstraintValidator.permittedSubtreesEmail, (Collection)this.permittedSubtreesEmail) && this.collectionsAreEqual((Collection)pKIXNameConstraintValidator.permittedSubtreesIP, (Collection)this.permittedSubtreesIP) && this.collectionsAreEqual((Collection)pKIXNameConstraintValidator.permittedSubtreesURI, (Collection)this.permittedSubtreesURI)) break block3;
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.hashCollection((Collection)this.excludedSubtreesDN) + this.hashCollection((Collection)this.excludedSubtreesDNS) + this.hashCollection((Collection)this.excludedSubtreesEmail) + this.hashCollection((Collection)this.excludedSubtreesIP) + this.hashCollection((Collection)this.excludedSubtreesURI) + this.hashCollection((Collection)this.permittedSubtreesDN) + this.hashCollection((Collection)this.permittedSubtreesDNS) + this.hashCollection((Collection)this.permittedSubtreesEmail) + this.hashCollection((Collection)this.permittedSubtreesIP) + this.hashCollection((Collection)this.permittedSubtreesURI);
    }

    public void intersectEmptyPermittedSubtree(int n) {
        switch (n) {
            default: {
                return;
            }
            case 1: {
                this.permittedSubtreesEmail = new HashSet();
                return;
            }
            case 2: {
                this.permittedSubtreesDNS = new HashSet();
                return;
            }
            case 4: {
                this.permittedSubtreesDN = new HashSet();
                return;
            }
            case 6: {
                this.permittedSubtreesURI = new HashSet();
                return;
            }
            case 7: 
        }
        this.permittedSubtreesIP = new HashSet();
    }

    public void intersectPermittedSubtree(GeneralSubtree generalSubtree) {
        this.intersectPermittedSubtree(new GeneralSubtree[]{generalSubtree});
    }

    public void intersectPermittedSubtree(GeneralSubtree[] arrgeneralSubtree) {
        HashMap hashMap = new HashMap();
        for (int i = 0; i != arrgeneralSubtree.length; ++i) {
            GeneralSubtree generalSubtree = arrgeneralSubtree[i];
            Integer n = Integers.valueOf(generalSubtree.getBase().getTagNo());
            if (hashMap.get((Object)n) == null) {
                hashMap.put((Object)n, (Object)new HashSet());
            }
            ((Set)hashMap.get((Object)n)).add((Object)generalSubtree);
        }
        block8 : for (Map.Entry entry : hashMap.entrySet()) {
            switch ((Integer)entry.getKey()) {
                default: {
                    continue block8;
                }
                case 1: {
                    this.permittedSubtreesEmail = this.intersectEmail(this.permittedSubtreesEmail, (Set)entry.getValue());
                    continue block8;
                }
                case 2: {
                    this.permittedSubtreesDNS = this.intersectDNS(this.permittedSubtreesDNS, (Set)entry.getValue());
                    continue block8;
                }
                case 4: {
                    this.permittedSubtreesDN = this.intersectDN(this.permittedSubtreesDN, (Set)entry.getValue());
                    continue block8;
                }
                case 6: {
                    this.permittedSubtreesURI = this.intersectURI(this.permittedSubtreesURI, (Set)entry.getValue());
                    continue block8;
                }
                case 7: 
            }
            this.permittedSubtreesIP = this.intersectIP(this.permittedSubtreesIP, (Set)entry.getValue());
        }
    }

    public String toString() {
        String string = "" + "permitted:\n";
        if (this.permittedSubtreesDN != null) {
            String string2 = string + "DN:\n";
            string = string2 + this.permittedSubtreesDN.toString() + "\n";
        }
        if (this.permittedSubtreesDNS != null) {
            String string3 = string + "DNS:\n";
            string = string3 + this.permittedSubtreesDNS.toString() + "\n";
        }
        if (this.permittedSubtreesEmail != null) {
            String string4 = string + "Email:\n";
            string = string4 + this.permittedSubtreesEmail.toString() + "\n";
        }
        if (this.permittedSubtreesURI != null) {
            String string5 = string + "URI:\n";
            string = string5 + this.permittedSubtreesURI.toString() + "\n";
        }
        if (this.permittedSubtreesIP != null) {
            String string6 = string + "IP:\n";
            string = string6 + this.stringifyIPCollection(this.permittedSubtreesIP) + "\n";
        }
        String string7 = string + "excluded:\n";
        if (!this.excludedSubtreesDN.isEmpty()) {
            String string8 = string7 + "DN:\n";
            string7 = string8 + this.excludedSubtreesDN.toString() + "\n";
        }
        if (!this.excludedSubtreesDNS.isEmpty()) {
            String string9 = string7 + "DNS:\n";
            string7 = string9 + this.excludedSubtreesDNS.toString() + "\n";
        }
        if (!this.excludedSubtreesEmail.isEmpty()) {
            String string10 = string7 + "Email:\n";
            string7 = string10 + this.excludedSubtreesEmail.toString() + "\n";
        }
        if (!this.excludedSubtreesURI.isEmpty()) {
            String string11 = string7 + "URI:\n";
            string7 = string11 + this.excludedSubtreesURI.toString() + "\n";
        }
        if (!this.excludedSubtreesIP.isEmpty()) {
            String string12 = string7 + "IP:\n";
            string7 = string12 + this.stringifyIPCollection(this.excludedSubtreesIP) + "\n";
        }
        return string7;
    }

    protected Set unionDNS(Set set, String string) {
        if (set.isEmpty()) {
            if (string == null) {
                return set;
            }
            set.add((Object)string);
            return set;
        }
        HashSet hashSet = new HashSet();
        for (String string2 : set) {
            if (this.withinDomain(string2, string)) {
                hashSet.add((Object)string);
                continue;
            }
            if (this.withinDomain(string, string2)) {
                hashSet.add((Object)string2);
                continue;
            }
            hashSet.add((Object)string2);
            hashSet.add((Object)string);
        }
        return hashSet;
    }
}

