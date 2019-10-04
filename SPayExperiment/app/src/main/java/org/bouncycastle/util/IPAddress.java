/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.util;

public class IPAddress {
    private static boolean isMaskValue(String string, int n) {
        try {
            int n2 = Integer.parseInt((String)string);
            boolean bl = false;
            if (n2 >= 0) {
                bl = false;
                if (n2 <= n) {
                    bl = true;
                }
            }
            return bl;
        }
        catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public static boolean isValid(String string) {
        return IPAddress.isValidIPv4(string) || IPAddress.isValidIPv6(string);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static boolean isValidIPv4(String string) {
        int n;
        if (string.length() == 0) {
            return false;
        }
        String string2 = string + ".";
        int n2 = 0;
        int n3 = 0;
        while (n2 < string2.length() && (n = string2.indexOf(46, n2)) > n2) {
            if (n3 == 4) return false;
            int n4 = Integer.parseInt((String)string2.substring(n2, n));
            if (n4 < 0) return false;
            if (n4 > 255) return false;
            n2 = n + 1;
            ++n3;
        }
        if (n3 != 4) return false;
        return true;
        catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public static boolean isValidIPv4WithNetmask(String string) {
        boolean bl;
        block2 : {
            block3 : {
                int n = string.indexOf("/");
                String string2 = string.substring(n + 1);
                bl = false;
                if (n <= 0) break block2;
                boolean bl2 = IPAddress.isValidIPv4(string.substring(0, n));
                bl = false;
                if (!bl2) break block2;
                if (IPAddress.isValidIPv4(string2)) break block3;
                boolean bl3 = IPAddress.isMaskValue(string2, 32);
                bl = false;
                if (!bl3) break block2;
            }
            bl = true;
        }
        return bl;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static boolean isValidIPv6(String var0) {
        if (var0.length() == 0) {
            return false;
        }
        var1_1 = var0 + ":";
        var2_2 = 0;
        var3_3 = false;
        var4_4 = 0;
        while (var2_2 < var1_1.length() && (var5_5 = var1_1.indexOf(58, var2_2)) >= var2_2) {
            block8 : {
                if (var4_4 == 8) return false;
                if (var2_2 == var5_5) ** GOTO lbl21
                var6_6 = var1_1.substring(var2_2, var5_5);
                if (var5_5 == -1 + var1_1.length() && var6_6.indexOf(46) > 0) {
                    if (IPAddress.isValidIPv4(var6_6) == false) return false;
                    ++var4_4;
                } else {
                    var8_7 = Integer.parseInt((String)var1_1.substring(var2_2, var5_5), (int)16);
                    if (var8_7 < 0) return false;
                    if (var8_7 > 65535) {
                        return false;
                    }
                    break block8;
lbl21: // 1 sources:
                    if (var5_5 != 1 && var5_5 != -1 + var1_1.length()) {
                        if (var3_3 != false) return false;
                    }
                    var3_3 = true;
                }
            }
            var2_2 = var5_5 + 1;
            ++var4_4;
        }
        if (var4_4 == 8) return true;
        if (var3_3 == false) return false;
        return true;
        catch (NumberFormatException var7_8) {
            return false;
        }
    }

    public static boolean isValidIPv6WithNetmask(String string) {
        boolean bl;
        block2 : {
            block3 : {
                int n = string.indexOf("/");
                String string2 = string.substring(n + 1);
                bl = false;
                if (n <= 0) break block2;
                boolean bl2 = IPAddress.isValidIPv6(string.substring(0, n));
                bl = false;
                if (!bl2) break block2;
                if (IPAddress.isValidIPv6(string2)) break block3;
                boolean bl3 = IPAddress.isMaskValue(string2, 128);
                bl = false;
                if (!bl3) break block2;
            }
            bl = true;
        }
        return bl;
    }

    public static boolean isValidWithNetMask(String string) {
        return IPAddress.isValidIPv4WithNetmask(string) || IPAddress.isValidIPv6WithNetmask(string);
    }
}

