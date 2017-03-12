package org.bouncycastle.jce.provider;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralSubtree;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Strings;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class PKIXNameConstraintValidator {
    private Set excludedSubtreesDN;
    private Set excludedSubtreesDNS;
    private Set excludedSubtreesEmail;
    private Set excludedSubtreesIP;
    private Set excludedSubtreesURI;
    private Set permittedSubtreesDN;
    private Set permittedSubtreesDNS;
    private Set permittedSubtreesEmail;
    private Set permittedSubtreesIP;
    private Set permittedSubtreesURI;

    public PKIXNameConstraintValidator() {
        this.excludedSubtreesDN = new HashSet();
        this.excludedSubtreesDNS = new HashSet();
        this.excludedSubtreesEmail = new HashSet();
        this.excludedSubtreesURI = new HashSet();
        this.excludedSubtreesIP = new HashSet();
    }

    private void checkExcludedDN(Set set, ASN1Sequence aSN1Sequence) {
        if (!set.isEmpty()) {
            for (ASN1Sequence withinDNSubtree : set) {
                if (withinDNSubtree(aSN1Sequence, withinDNSubtree)) {
                    throw new PKIXNameConstraintValidatorException("Subject distinguished name is from an excluded subtree");
                }
            }
        }
    }

    private void checkExcludedDNS(Set set, String str) {
        if (!set.isEmpty()) {
            for (String str2 : set) {
                if (!withinDomain(str, str2)) {
                    if (str.equalsIgnoreCase(str2)) {
                    }
                }
                throw new PKIXNameConstraintValidatorException("DNS is from an excluded subtree.");
            }
        }
    }

    private void checkExcludedEmail(Set set, String str) {
        if (!set.isEmpty()) {
            for (String emailIsConstrained : set) {
                if (emailIsConstrained(str, emailIsConstrained)) {
                    throw new PKIXNameConstraintValidatorException("Email address is from an excluded subtree.");
                }
            }
        }
    }

    private void checkExcludedIP(Set set, byte[] bArr) {
        if (!set.isEmpty()) {
            for (byte[] isIPConstrained : set) {
                if (isIPConstrained(bArr, isIPConstrained)) {
                    throw new PKIXNameConstraintValidatorException("IP is from an excluded subtree.");
                }
            }
        }
    }

    private void checkExcludedURI(Set set, String str) {
        if (!set.isEmpty()) {
            for (String isUriConstrained : set) {
                if (isUriConstrained(str, isUriConstrained)) {
                    throw new PKIXNameConstraintValidatorException("URI is from an excluded subtree.");
                }
            }
        }
    }

    private void checkPermittedDN(Set set, ASN1Sequence aSN1Sequence) {
        if (set != null) {
            if (!set.isEmpty() || aSN1Sequence.size() != 0) {
                for (ASN1Sequence withinDNSubtree : set) {
                    if (withinDNSubtree(aSN1Sequence, withinDNSubtree)) {
                        return;
                    }
                }
                throw new PKIXNameConstraintValidatorException("Subject distinguished name is not from a permitted subtree");
            }
        }
    }

    private void checkPermittedDNS(Set set, String str) {
        if (set != null) {
            for (String str2 : set) {
                if (!withinDomain(str, str2)) {
                    if (str.equalsIgnoreCase(str2)) {
                        return;
                    }
                }
                return;
            }
            if (str.length() != 0 || set.size() != 0) {
                throw new PKIXNameConstraintValidatorException("DNS is not from a permitted subtree.");
            }
        }
    }

    private void checkPermittedEmail(Set set, String str) {
        if (set != null) {
            for (String emailIsConstrained : set) {
                if (emailIsConstrained(str, emailIsConstrained)) {
                    return;
                }
            }
            if (str.length() != 0 || set.size() != 0) {
                throw new PKIXNameConstraintValidatorException("Subject email address is not from a permitted subtree.");
            }
        }
    }

    private void checkPermittedIP(Set set, byte[] bArr) {
        if (set != null) {
            for (byte[] isIPConstrained : set) {
                if (isIPConstrained(bArr, isIPConstrained)) {
                    return;
                }
            }
            if (bArr.length != 0 || set.size() != 0) {
                throw new PKIXNameConstraintValidatorException("IP is not from a permitted subtree.");
            }
        }
    }

    private void checkPermittedURI(Set set, String str) {
        if (set != null) {
            for (String isUriConstrained : set) {
                if (isUriConstrained(str, isUriConstrained)) {
                    return;
                }
            }
            if (str.length() != 0 || set.size() != 0) {
                throw new PKIXNameConstraintValidatorException("URI is not from a permitted subtree.");
            }
        }
    }

    private boolean collectionsAreEqual(Collection collection, Collection collection2) {
        if (collection == collection2) {
            return true;
        }
        if (collection == null || collection2 == null) {
            return false;
        }
        if (collection.size() != collection2.size()) {
            return false;
        }
        for (Object next : collection) {
            boolean z;
            for (Object equals : collection2) {
                if (equals(next, equals)) {
                    z = true;
                    break;
                    continue;
                }
            }
            z = false;
            continue;
            if (!z) {
                return false;
            }
        }
        return true;
    }

    private static int compareTo(byte[] bArr, byte[] bArr2) {
        return Arrays.areEqual(bArr, bArr2) ? 0 : Arrays.areEqual(max(bArr, bArr2), bArr) ? 1 : -1;
    }

    private boolean emailIsConstrained(String str, String str2) {
        String substring = str.substring(str.indexOf(64) + 1);
        if (str2.indexOf(64) != -1) {
            if (str.equalsIgnoreCase(str2) || substring.equalsIgnoreCase(str2.substring(1))) {
                return true;
            }
        } else if (str2.charAt(0) != '.') {
            if (substring.equalsIgnoreCase(str2)) {
                return true;
            }
        } else if (withinDomain(substring, str2)) {
            return true;
        }
        return false;
    }

    private boolean equals(Object obj, Object obj2) {
        return obj == obj2 ? true : (obj == null || obj2 == null) ? false : ((obj instanceof byte[]) && (obj2 instanceof byte[])) ? Arrays.areEqual((byte[]) obj, (byte[]) obj2) : obj.equals(obj2);
    }

    private static String extractHostFromURL(String str) {
        String substring = str.substring(str.indexOf(58) + 1);
        if (substring.indexOf("//") != -1) {
            substring = substring.substring(substring.indexOf("//") + 2);
        }
        if (substring.lastIndexOf(58) != -1) {
            substring = substring.substring(0, substring.lastIndexOf(58));
        }
        substring = substring.substring(substring.indexOf(58) + 1);
        substring = substring.substring(substring.indexOf(64) + 1);
        return substring.indexOf(47) != -1 ? substring.substring(0, substring.indexOf(47)) : substring;
    }

    private byte[][] extractIPsAndSubnetMasks(byte[] bArr, byte[] bArr2) {
        int length = bArr.length / 2;
        Object obj = new byte[length];
        System.arraycopy(bArr, 0, new byte[length], 0, length);
        System.arraycopy(bArr, length, obj, 0, length);
        Object obj2 = new byte[length];
        System.arraycopy(bArr2, 0, new byte[length], 0, length);
        System.arraycopy(bArr2, length, obj2, 0, length);
        return new byte[][]{r1, obj, r3, obj2};
    }

    private String extractNameAsString(GeneralName generalName) {
        return DERIA5String.getInstance(generalName.getName()).getString();
    }

    private int hashCollection(Collection collection) {
        if (collection == null) {
            return 0;
        }
        int i = 0;
        for (Object next : collection) {
            i = next instanceof byte[] ? Arrays.hashCode((byte[]) next) + i : next.hashCode() + i;
        }
        return i;
    }

    private Set intersectDN(Set set, Set set2) {
        Set hashSet = new HashSet();
        for (GeneralSubtree base : set2) {
            ASN1Sequence instance = ASN1Sequence.getInstance(base.getBase().getName().toASN1Primitive());
            if (set != null) {
                for (ASN1Sequence aSN1Sequence : set) {
                    if (withinDNSubtree(instance, aSN1Sequence)) {
                        hashSet.add(instance);
                    } else if (withinDNSubtree(aSN1Sequence, instance)) {
                        hashSet.add(aSN1Sequence);
                    }
                }
            } else if (instance != null) {
                hashSet.add(instance);
            }
        }
        return hashSet;
    }

    private Set intersectDNS(Set set, Set set2) {
        Set hashSet = new HashSet();
        for (GeneralSubtree base : set2) {
            String extractNameAsString = extractNameAsString(base.getBase());
            if (set != null) {
                for (String str : set) {
                    if (withinDomain(str, extractNameAsString)) {
                        hashSet.add(str);
                    } else if (withinDomain(extractNameAsString, str)) {
                        hashSet.add(extractNameAsString);
                    }
                }
            } else if (extractNameAsString != null) {
                hashSet.add(extractNameAsString);
            }
        }
        return hashSet;
    }

    private Set intersectEmail(Set set, Set set2) {
        Set hashSet = new HashSet();
        for (GeneralSubtree base : set2) {
            String extractNameAsString = extractNameAsString(base.getBase());
            if (set != null) {
                for (String intersectEmail : set) {
                    intersectEmail(extractNameAsString, intersectEmail, hashSet);
                }
            } else if (extractNameAsString != null) {
                hashSet.add(extractNameAsString);
            }
        }
        return hashSet;
    }

    private void intersectEmail(String str, String str2, Set set) {
        if (str.indexOf(64) != -1) {
            String substring = str.substring(str.indexOf(64) + 1);
            if (str2.indexOf(64) != -1) {
                if (str.equalsIgnoreCase(str2)) {
                    set.add(str);
                }
            } else if (str2.startsWith(".")) {
                if (withinDomain(substring, str2)) {
                    set.add(str);
                }
            } else if (substring.equalsIgnoreCase(str2)) {
                set.add(str);
            }
        } else if (str.startsWith(".")) {
            if (str2.indexOf(64) != -1) {
                if (withinDomain(str2.substring(str.indexOf(64) + 1), str)) {
                    set.add(str2);
                }
            } else if (str2.startsWith(".")) {
                if (withinDomain(str, str2) || str.equalsIgnoreCase(str2)) {
                    set.add(str);
                } else if (withinDomain(str2, str)) {
                    set.add(str2);
                }
            } else if (withinDomain(str2, str)) {
                set.add(str2);
            }
        } else if (str2.indexOf(64) != -1) {
            if (str2.substring(str2.indexOf(64) + 1).equalsIgnoreCase(str)) {
                set.add(str2);
            }
        } else if (str2.startsWith(".")) {
            if (withinDomain(str, str2)) {
                set.add(str);
            }
        } else if (str.equalsIgnoreCase(str2)) {
            set.add(str);
        }
    }

    private Set intersectIP(Set set, Set set2) {
        Set hashSet = new HashSet();
        for (GeneralSubtree base : set2) {
            Object octets = ASN1OctetString.getInstance(base.getBase().getName()).getOctets();
            if (set != null) {
                for (byte[] intersectIPRange : set) {
                    hashSet.addAll(intersectIPRange(intersectIPRange, octets));
                }
            } else if (octets != null) {
                hashSet.add(octets);
            }
        }
        return hashSet;
    }

    private Set intersectIPRange(byte[] bArr, byte[] bArr2) {
        if (bArr.length != bArr2.length) {
            return Collections.EMPTY_SET;
        }
        byte[][] extractIPsAndSubnetMasks = extractIPsAndSubnetMasks(bArr, bArr2);
        byte[] bArr3 = extractIPsAndSubnetMasks[0];
        byte[] bArr4 = extractIPsAndSubnetMasks[1];
        byte[] bArr5 = extractIPsAndSubnetMasks[2];
        byte[] bArr6 = extractIPsAndSubnetMasks[3];
        byte[][] minMaxIPs = minMaxIPs(bArr3, bArr4, bArr5, bArr6);
        return compareTo(max(minMaxIPs[0], minMaxIPs[2]), min(minMaxIPs[1], minMaxIPs[3])) == 1 ? Collections.EMPTY_SET : Collections.singleton(ipWithSubnetMask(or(minMaxIPs[0], minMaxIPs[2]), or(bArr4, bArr6)));
    }

    private Set intersectURI(Set set, Set set2) {
        Set hashSet = new HashSet();
        for (GeneralSubtree base : set2) {
            String extractNameAsString = extractNameAsString(base.getBase());
            if (set != null) {
                for (String intersectURI : set) {
                    intersectURI(intersectURI, extractNameAsString, hashSet);
                }
            } else if (extractNameAsString != null) {
                hashSet.add(extractNameAsString);
            }
        }
        return hashSet;
    }

    private void intersectURI(String str, String str2, Set set) {
        if (str.indexOf(64) != -1) {
            String substring = str.substring(str.indexOf(64) + 1);
            if (str2.indexOf(64) != -1) {
                if (str.equalsIgnoreCase(str2)) {
                    set.add(str);
                }
            } else if (str2.startsWith(".")) {
                if (withinDomain(substring, str2)) {
                    set.add(str);
                }
            } else if (substring.equalsIgnoreCase(str2)) {
                set.add(str);
            }
        } else if (str.startsWith(".")) {
            if (str2.indexOf(64) != -1) {
                if (withinDomain(str2.substring(str.indexOf(64) + 1), str)) {
                    set.add(str2);
                }
            } else if (str2.startsWith(".")) {
                if (withinDomain(str, str2) || str.equalsIgnoreCase(str2)) {
                    set.add(str);
                } else if (withinDomain(str2, str)) {
                    set.add(str2);
                }
            } else if (withinDomain(str2, str)) {
                set.add(str2);
            }
        } else if (str2.indexOf(64) != -1) {
            if (str2.substring(str2.indexOf(64) + 1).equalsIgnoreCase(str)) {
                set.add(str2);
            }
        } else if (str2.startsWith(".")) {
            if (withinDomain(str, str2)) {
                set.add(str);
            }
        } else if (str.equalsIgnoreCase(str2)) {
            set.add(str);
        }
    }

    private byte[] ipWithSubnetMask(byte[] bArr, byte[] bArr2) {
        int length = bArr.length;
        Object obj = new byte[(length * 2)];
        System.arraycopy(bArr, 0, obj, 0, length);
        System.arraycopy(bArr2, 0, obj, length, length);
        return obj;
    }

    private boolean isIPConstrained(byte[] bArr, byte[] bArr2) {
        int i = 0;
        int length = bArr.length;
        if (length != bArr2.length / 2) {
            return false;
        }
        Object obj = new byte[length];
        System.arraycopy(bArr2, length, obj, 0, length);
        byte[] bArr3 = new byte[length];
        byte[] bArr4 = new byte[length];
        while (i < length) {
            bArr3[i] = (byte) (bArr2[i] & obj[i]);
            bArr4[i] = (byte) (bArr[i] & obj[i]);
            i++;
        }
        return Arrays.areEqual(bArr3, bArr4);
    }

    private boolean isUriConstrained(String str, String str2) {
        String extractHostFromURL = extractHostFromURL(str);
        if (str2.startsWith(".")) {
            if (withinDomain(extractHostFromURL, str2)) {
                return true;
            }
        } else if (extractHostFromURL.equalsIgnoreCase(str2)) {
            return true;
        }
        return false;
    }

    private static byte[] max(byte[] bArr, byte[] bArr2) {
        for (int i = 0; i < bArr.length; i++) {
            if ((bArr[i] & HCEClientConstants.HIGHEST_ATC_DEC_VALUE) > (bArr2[i] & HCEClientConstants.HIGHEST_ATC_DEC_VALUE)) {
                return bArr;
            }
        }
        return bArr2;
    }

    private static byte[] min(byte[] bArr, byte[] bArr2) {
        for (int i = 0; i < bArr.length; i++) {
            if ((bArr[i] & HCEClientConstants.HIGHEST_ATC_DEC_VALUE) < (bArr2[i] & HCEClientConstants.HIGHEST_ATC_DEC_VALUE)) {
                return bArr;
            }
        }
        return bArr2;
    }

    private byte[][] minMaxIPs(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        int length = bArr.length;
        byte[] bArr5 = new byte[length];
        byte[] bArr6 = new byte[length];
        byte[] bArr7 = new byte[length];
        byte[] bArr8 = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr5[i] = (byte) (bArr[i] & bArr2[i]);
            bArr6[i] = (byte) ((bArr[i] & bArr2[i]) | (bArr2[i] ^ -1));
            bArr7[i] = (byte) (bArr3[i] & bArr4[i]);
            bArr8[i] = (byte) ((bArr3[i] & bArr4[i]) | (bArr4[i] ^ -1));
        }
        return new byte[][]{bArr5, bArr6, bArr7, bArr8};
    }

    private static byte[] or(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr3[i] = (byte) (bArr[i] | bArr2[i]);
        }
        return bArr3;
    }

    private String stringifyIP(byte[] bArr) {
        int i;
        String str = BuildConfig.FLAVOR;
        for (i = 0; i < bArr.length / 2; i++) {
            str = str + Integer.toString(bArr[i] & GF2Field.MASK) + ".";
        }
        str = str.substring(0, str.length() - 1) + "/";
        for (i = bArr.length / 2; i < bArr.length; i++) {
            str = str + Integer.toString(bArr[i] & GF2Field.MASK) + ".";
        }
        return str.substring(0, str.length() - 1);
    }

    private String stringifyIPCollection(Set set) {
        String str = BuildConfig.FLAVOR + "[";
        for (byte[] stringifyIP : set) {
            str = str + stringifyIP(stringifyIP) + ",";
        }
        if (str.length() > 1) {
            str = str.substring(0, str.length() - 1);
        }
        return str + "]";
    }

    private Set unionDN(Set set, ASN1Sequence aSN1Sequence) {
        if (!set.isEmpty()) {
            Set hashSet = new HashSet();
            for (ASN1Sequence aSN1Sequence2 : set) {
                if (withinDNSubtree(aSN1Sequence, aSN1Sequence2)) {
                    hashSet.add(aSN1Sequence2);
                } else if (withinDNSubtree(aSN1Sequence2, aSN1Sequence)) {
                    hashSet.add(aSN1Sequence);
                } else {
                    hashSet.add(aSN1Sequence2);
                    hashSet.add(aSN1Sequence);
                }
            }
            return hashSet;
        } else if (aSN1Sequence == null) {
            return set;
        } else {
            set.add(aSN1Sequence);
            return set;
        }
    }

    private Set unionEmail(Set set, String str) {
        if (!set.isEmpty()) {
            Set hashSet = new HashSet();
            for (String unionEmail : set) {
                unionEmail(unionEmail, str, hashSet);
            }
            return hashSet;
        } else if (str == null) {
            return set;
        } else {
            set.add(str);
            return set;
        }
    }

    private void unionEmail(String str, String str2, Set set) {
        if (str.indexOf(64) != -1) {
            String substring = str.substring(str.indexOf(64) + 1);
            if (str2.indexOf(64) != -1) {
                if (str.equalsIgnoreCase(str2)) {
                    set.add(str);
                    return;
                }
                set.add(str);
                set.add(str2);
            } else if (str2.startsWith(".")) {
                if (withinDomain(substring, str2)) {
                    set.add(str2);
                    return;
                }
                set.add(str);
                set.add(str2);
            } else if (substring.equalsIgnoreCase(str2)) {
                set.add(str2);
            } else {
                set.add(str);
                set.add(str2);
            }
        } else if (str.startsWith(".")) {
            if (str2.indexOf(64) != -1) {
                if (withinDomain(str2.substring(str.indexOf(64) + 1), str)) {
                    set.add(str);
                    return;
                }
                set.add(str);
                set.add(str2);
            } else if (str2.startsWith(".")) {
                if (withinDomain(str, str2) || str.equalsIgnoreCase(str2)) {
                    set.add(str2);
                } else if (withinDomain(str2, str)) {
                    set.add(str);
                } else {
                    set.add(str);
                    set.add(str2);
                }
            } else if (withinDomain(str2, str)) {
                set.add(str);
            } else {
                set.add(str);
                set.add(str2);
            }
        } else if (str2.indexOf(64) != -1) {
            if (str2.substring(str.indexOf(64) + 1).equalsIgnoreCase(str)) {
                set.add(str);
                return;
            }
            set.add(str);
            set.add(str2);
        } else if (str2.startsWith(".")) {
            if (withinDomain(str, str2)) {
                set.add(str2);
                return;
            }
            set.add(str);
            set.add(str2);
        } else if (str.equalsIgnoreCase(str2)) {
            set.add(str);
        } else {
            set.add(str);
            set.add(str2);
        }
    }

    private Set unionIP(Set set, byte[] bArr) {
        if (!set.isEmpty()) {
            Set hashSet = new HashSet();
            for (byte[] unionIPRange : set) {
                hashSet.addAll(unionIPRange(unionIPRange, bArr));
            }
            return hashSet;
        } else if (bArr == null) {
            return set;
        } else {
            set.add(bArr);
            return set;
        }
    }

    private Set unionIPRange(byte[] bArr, byte[] bArr2) {
        Set hashSet = new HashSet();
        if (Arrays.areEqual(bArr, bArr2)) {
            hashSet.add(bArr);
        } else {
            hashSet.add(bArr);
            hashSet.add(bArr2);
        }
        return hashSet;
    }

    private Set unionURI(Set set, String str) {
        if (!set.isEmpty()) {
            Set hashSet = new HashSet();
            for (String unionURI : set) {
                unionURI(unionURI, str, hashSet);
            }
            return hashSet;
        } else if (str == null) {
            return set;
        } else {
            set.add(str);
            return set;
        }
    }

    private void unionURI(String str, String str2, Set set) {
        if (str.indexOf(64) != -1) {
            String substring = str.substring(str.indexOf(64) + 1);
            if (str2.indexOf(64) != -1) {
                if (str.equalsIgnoreCase(str2)) {
                    set.add(str);
                    return;
                }
                set.add(str);
                set.add(str2);
            } else if (str2.startsWith(".")) {
                if (withinDomain(substring, str2)) {
                    set.add(str2);
                    return;
                }
                set.add(str);
                set.add(str2);
            } else if (substring.equalsIgnoreCase(str2)) {
                set.add(str2);
            } else {
                set.add(str);
                set.add(str2);
            }
        } else if (str.startsWith(".")) {
            if (str2.indexOf(64) != -1) {
                if (withinDomain(str2.substring(str.indexOf(64) + 1), str)) {
                    set.add(str);
                    return;
                }
                set.add(str);
                set.add(str2);
            } else if (str2.startsWith(".")) {
                if (withinDomain(str, str2) || str.equalsIgnoreCase(str2)) {
                    set.add(str2);
                } else if (withinDomain(str2, str)) {
                    set.add(str);
                } else {
                    set.add(str);
                    set.add(str2);
                }
            } else if (withinDomain(str2, str)) {
                set.add(str);
            } else {
                set.add(str);
                set.add(str2);
            }
        } else if (str2.indexOf(64) != -1) {
            if (str2.substring(str.indexOf(64) + 1).equalsIgnoreCase(str)) {
                set.add(str);
                return;
            }
            set.add(str);
            set.add(str2);
        } else if (str2.startsWith(".")) {
            if (withinDomain(str, str2)) {
                set.add(str2);
                return;
            }
            set.add(str);
            set.add(str2);
        } else if (str.equalsIgnoreCase(str2)) {
            set.add(str);
        } else {
            set.add(str);
            set.add(str2);
        }
    }

    private static boolean withinDNSubtree(ASN1Sequence aSN1Sequence, ASN1Sequence aSN1Sequence2) {
        if (aSN1Sequence2.size() < 1 || aSN1Sequence2.size() > aSN1Sequence.size()) {
            return false;
        }
        for (int size = aSN1Sequence2.size() - 1; size >= 0; size--) {
            if (!aSN1Sequence2.getObjectAt(size).equals(aSN1Sequence.getObjectAt(size))) {
                return false;
            }
        }
        return true;
    }

    private boolean withinDomain(String str, String str2) {
        if (str2.startsWith(".")) {
            str2 = str2.substring(1);
        }
        String[] split = Strings.split(str2, '.');
        String[] split2 = Strings.split(str, '.');
        if (split2.length <= split.length) {
            return false;
        }
        int length = split2.length - split.length;
        for (int i = -1; i < split.length; i++) {
            if (i == -1) {
                if (split2[i + length].equals(BuildConfig.FLAVOR)) {
                    return false;
                }
            } else if (!split[i].equalsIgnoreCase(split2[i + length])) {
                return false;
            }
        }
        return true;
    }

    public void addExcludedSubtree(GeneralSubtree generalSubtree) {
        GeneralName base = generalSubtree.getBase();
        switch (base.getTagNo()) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                this.excludedSubtreesEmail = unionEmail(this.excludedSubtreesEmail, extractNameAsString(base));
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                this.excludedSubtreesDNS = unionDNS(this.excludedSubtreesDNS, extractNameAsString(base));
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                this.excludedSubtreesDN = unionDN(this.excludedSubtreesDN, (ASN1Sequence) base.getName().toASN1Primitive());
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                this.excludedSubtreesURI = unionURI(this.excludedSubtreesURI, extractNameAsString(base));
            case ECCurve.COORD_SKEWED /*7*/:
                this.excludedSubtreesIP = unionIP(this.excludedSubtreesIP, ASN1OctetString.getInstance(base.getName()).getOctets());
            default:
        }
    }

    public void checkExcluded(GeneralName generalName) {
        switch (generalName.getTagNo()) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                checkExcludedEmail(this.excludedSubtreesEmail, extractNameAsString(generalName));
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                checkExcludedDNS(this.excludedSubtreesDNS, DERIA5String.getInstance(generalName.getName()).getString());
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                checkExcludedDN(ASN1Sequence.getInstance(generalName.getName().toASN1Primitive()));
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                checkExcludedURI(this.excludedSubtreesURI, DERIA5String.getInstance(generalName.getName()).getString());
            case ECCurve.COORD_SKEWED /*7*/:
                checkExcludedIP(this.excludedSubtreesIP, ASN1OctetString.getInstance(generalName.getName()).getOctets());
            default:
        }
    }

    public void checkExcludedDN(ASN1Sequence aSN1Sequence) {
        checkExcludedDN(this.excludedSubtreesDN, aSN1Sequence);
    }

    public void checkPermitted(GeneralName generalName) {
        switch (generalName.getTagNo()) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                checkPermittedEmail(this.permittedSubtreesEmail, extractNameAsString(generalName));
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                checkPermittedDNS(this.permittedSubtreesDNS, DERIA5String.getInstance(generalName.getName()).getString());
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                checkPermittedDN(ASN1Sequence.getInstance(generalName.getName().toASN1Primitive()));
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                checkPermittedURI(this.permittedSubtreesURI, DERIA5String.getInstance(generalName.getName()).getString());
            case ECCurve.COORD_SKEWED /*7*/:
                checkPermittedIP(this.permittedSubtreesIP, ASN1OctetString.getInstance(generalName.getName()).getOctets());
            default:
        }
    }

    public void checkPermittedDN(ASN1Sequence aSN1Sequence) {
        checkPermittedDN(this.permittedSubtreesDN, aSN1Sequence);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PKIXNameConstraintValidator)) {
            return false;
        }
        PKIXNameConstraintValidator pKIXNameConstraintValidator = (PKIXNameConstraintValidator) obj;
        return collectionsAreEqual(pKIXNameConstraintValidator.excludedSubtreesDN, this.excludedSubtreesDN) && collectionsAreEqual(pKIXNameConstraintValidator.excludedSubtreesDNS, this.excludedSubtreesDNS) && collectionsAreEqual(pKIXNameConstraintValidator.excludedSubtreesEmail, this.excludedSubtreesEmail) && collectionsAreEqual(pKIXNameConstraintValidator.excludedSubtreesIP, this.excludedSubtreesIP) && collectionsAreEqual(pKIXNameConstraintValidator.excludedSubtreesURI, this.excludedSubtreesURI) && collectionsAreEqual(pKIXNameConstraintValidator.permittedSubtreesDN, this.permittedSubtreesDN) && collectionsAreEqual(pKIXNameConstraintValidator.permittedSubtreesDNS, this.permittedSubtreesDNS) && collectionsAreEqual(pKIXNameConstraintValidator.permittedSubtreesEmail, this.permittedSubtreesEmail) && collectionsAreEqual(pKIXNameConstraintValidator.permittedSubtreesIP, this.permittedSubtreesIP) && collectionsAreEqual(pKIXNameConstraintValidator.permittedSubtreesURI, this.permittedSubtreesURI);
    }

    public int hashCode() {
        return ((((((((hashCollection(this.excludedSubtreesDN) + hashCollection(this.excludedSubtreesDNS)) + hashCollection(this.excludedSubtreesEmail)) + hashCollection(this.excludedSubtreesIP)) + hashCollection(this.excludedSubtreesURI)) + hashCollection(this.permittedSubtreesDN)) + hashCollection(this.permittedSubtreesDNS)) + hashCollection(this.permittedSubtreesEmail)) + hashCollection(this.permittedSubtreesIP)) + hashCollection(this.permittedSubtreesURI);
    }

    public void intersectEmptyPermittedSubtree(int i) {
        switch (i) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                this.permittedSubtreesEmail = new HashSet();
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                this.permittedSubtreesDNS = new HashSet();
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                this.permittedSubtreesDN = new HashSet();
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                this.permittedSubtreesURI = new HashSet();
            case ECCurve.COORD_SKEWED /*7*/:
                this.permittedSubtreesIP = new HashSet();
            default:
        }
    }

    public void intersectPermittedSubtree(GeneralSubtree generalSubtree) {
        intersectPermittedSubtree(new GeneralSubtree[]{generalSubtree});
    }

    public void intersectPermittedSubtree(GeneralSubtree[] generalSubtreeArr) {
        Map hashMap = new HashMap();
        for (int i = 0; i != generalSubtreeArr.length; i++) {
            GeneralSubtree generalSubtree = generalSubtreeArr[i];
            Integer valueOf = Integers.valueOf(generalSubtree.getBase().getTagNo());
            if (hashMap.get(valueOf) == null) {
                hashMap.put(valueOf, new HashSet());
            }
            ((Set) hashMap.get(valueOf)).add(generalSubtree);
        }
        for (Entry entry : hashMap.entrySet()) {
            switch (((Integer) entry.getKey()).intValue()) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    this.permittedSubtreesEmail = intersectEmail(this.permittedSubtreesEmail, (Set) entry.getValue());
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    this.permittedSubtreesDNS = intersectDNS(this.permittedSubtreesDNS, (Set) entry.getValue());
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    this.permittedSubtreesDN = intersectDN(this.permittedSubtreesDN, (Set) entry.getValue());
                    break;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    this.permittedSubtreesURI = intersectURI(this.permittedSubtreesURI, (Set) entry.getValue());
                    break;
                case ECCurve.COORD_SKEWED /*7*/:
                    this.permittedSubtreesIP = intersectIP(this.permittedSubtreesIP, (Set) entry.getValue());
                    break;
                default:
                    break;
            }
        }
    }

    public String toString() {
        String str = BuildConfig.FLAVOR + "permitted:\n";
        if (this.permittedSubtreesDN != null) {
            str = (str + "DN:\n") + this.permittedSubtreesDN.toString() + "\n";
        }
        if (this.permittedSubtreesDNS != null) {
            str = (str + "DNS:\n") + this.permittedSubtreesDNS.toString() + "\n";
        }
        if (this.permittedSubtreesEmail != null) {
            str = (str + "Email:\n") + this.permittedSubtreesEmail.toString() + "\n";
        }
        if (this.permittedSubtreesURI != null) {
            str = (str + "URI:\n") + this.permittedSubtreesURI.toString() + "\n";
        }
        if (this.permittedSubtreesIP != null) {
            str = (str + "IP:\n") + stringifyIPCollection(this.permittedSubtreesIP) + "\n";
        }
        str = str + "excluded:\n";
        if (!this.excludedSubtreesDN.isEmpty()) {
            str = (str + "DN:\n") + this.excludedSubtreesDN.toString() + "\n";
        }
        if (!this.excludedSubtreesDNS.isEmpty()) {
            str = (str + "DNS:\n") + this.excludedSubtreesDNS.toString() + "\n";
        }
        if (!this.excludedSubtreesEmail.isEmpty()) {
            str = (str + "Email:\n") + this.excludedSubtreesEmail.toString() + "\n";
        }
        if (!this.excludedSubtreesURI.isEmpty()) {
            str = (str + "URI:\n") + this.excludedSubtreesURI.toString() + "\n";
        }
        if (this.excludedSubtreesIP.isEmpty()) {
            return str;
        }
        return (str + "IP:\n") + stringifyIPCollection(this.excludedSubtreesIP) + "\n";
    }

    protected Set unionDNS(Set set, String str) {
        if (!set.isEmpty()) {
            Set hashSet = new HashSet();
            for (String str2 : set) {
                if (withinDomain(str2, str)) {
                    hashSet.add(str);
                } else if (withinDomain(str, str2)) {
                    hashSet.add(str2);
                } else {
                    hashSet.add(str2);
                    hashSet.add(str);
                }
            }
            return hashSet;
        } else if (str == null) {
            return set;
        } else {
            set.add(str);
            return set;
        }
    }
}
