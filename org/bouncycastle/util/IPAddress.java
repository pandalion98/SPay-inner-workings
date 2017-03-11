package org.bouncycastle.util;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class IPAddress {
    private static boolean isMaskValue(String str, int i) {
        try {
            int parseInt = Integer.parseInt(str);
            return parseInt >= 0 && parseInt <= i;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValid(String str) {
        return isValidIPv4(str) || isValidIPv6(str);
    }

    public static boolean isValidIPv4(String str) {
        if (str.length() == 0) {
            return false;
        }
        String str2 = str + ".";
        int i = 0;
        int i2 = 0;
        while (i < str2.length()) {
            int indexOf = str2.indexOf(46, i);
            if (indexOf <= i) {
                break;
            } else if (i2 == 4) {
                return false;
            } else {
                try {
                    i = Integer.parseInt(str2.substring(i, indexOf));
                    if (i < 0 || i > GF2Field.MASK) {
                        return false;
                    }
                    i = indexOf + 1;
                    i2++;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return i2 == 4;
    }

    public static boolean isValidIPv4WithNetmask(String str) {
        int indexOf = str.indexOf("/");
        String substring = str.substring(indexOf + 1);
        return (indexOf <= 0 || !isValidIPv4(str.substring(0, indexOf))) ? false : isValidIPv4(substring) || isMaskValue(substring, 32);
    }

    public static boolean isValidIPv6(String str) {
        if (str.length() == 0) {
            return false;
        }
        String str2 = str + ":";
        int i = 0;
        boolean z = false;
        int i2 = 0;
        while (i < str2.length()) {
            int indexOf = str2.indexOf(58, i);
            if (indexOf < i) {
                break;
            } else if (i2 == 8) {
                return false;
            } else {
                if (i != indexOf) {
                    String substring = str2.substring(i, indexOf);
                    if (indexOf != str2.length() - 1 || substring.indexOf(46) <= 0) {
                        try {
                            i = Integer.parseInt(str2.substring(i, indexOf), 16);
                            if (i < 0) {
                                return false;
                            }
                            if (i > HCEClientConstants.HIGHEST_ATC_DEC_VALUE) {
                                return false;
                            }
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    } else if (!isValidIPv4(substring)) {
                        return false;
                    } else {
                        i2++;
                    }
                } else if (indexOf != 1 && indexOf != str2.length() - 1 && z) {
                    return false;
                } else {
                    z = true;
                }
                i = indexOf + 1;
                i2++;
            }
        }
        return i2 == 8 || z;
    }

    public static boolean isValidIPv6WithNetmask(String str) {
        int indexOf = str.indexOf("/");
        String substring = str.substring(indexOf + 1);
        return (indexOf <= 0 || !isValidIPv6(str.substring(0, indexOf))) ? false : isValidIPv6(substring) || isMaskValue(substring, X509KeyUsage.digitalSignature);
    }

    public static boolean isValidWithNetMask(String str) {
        return isValidIPv4WithNetmask(str) || isValidIPv6WithNetmask(str);
    }
}
