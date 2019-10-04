/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.regex.Matcher
 *  java.util.regex.Pattern
 */
package com.samsung.android.spayfw.payprovider.plcc.util;

import android.text.TextUtils;
import com.samsung.android.spayfw.appinterface.MstPayConfig;
import com.samsung.android.spayfw.appinterface.MstPayConfigEntry;
import com.samsung.android.spayfw.appinterface.MstPayConfigEntryItem;
import com.samsung.android.spayfw.payprovider.plcc.exception.PlccException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SequenceUtils {
    /*
     * Enabled aggressive block sorting
     */
    private static MstPayConfigEntryItem buildSegment(String string) {
        int n2 = 1;
        int n3 = string.indexOf("R") != -1 ? n2 : 0;
        String string2 = string.toLowerCase();
        if (string2.indexOf("t") == -1) {
            throw new PlccException("Invalid Sequence FormatNo Track Index");
        }
        int n4 = SequenceUtils.extractNumber(string2.substring(1 + string2.indexOf("t")));
        if (n4 < n2 || n4 > 2) {
            throw new PlccException("Invalid Sequence Formattrack must be 1 or 2");
        }
        if (string2.indexOf("lz") == -1) {
            throw new PlccException("Invalid Sequence Format No Leading Zeros");
        }
        int n5 = SequenceUtils.extractNumber(string2.substring(2 + string2.indexOf("lz")));
        if (n5 < 0 || n5 > 1000) {
            throw new PlccException("Invalid Sequence Format Leading Zeros should be between [0,1000]");
        }
        if (string2.indexOf("tz") == -1) {
            throw new PlccException("Invalid Sequence Format No Trailing Zeros");
        }
        int n6 = SequenceUtils.extractNumber(string2.substring(2 + string2.indexOf("tz")));
        if (n6 < 0 || n6 > 1000) {
            throw new PlccException("Invalid Sequence Format Trailing Zeros should be between [0,1000]");
        }
        MstPayConfigEntryItem mstPayConfigEntryItem = new MstPayConfigEntryItem();
        mstPayConfigEntryItem.setTrackIndex(n4);
        mstPayConfigEntryItem.setLeadingZeros(n5);
        mstPayConfigEntryItem.setTrailingZeros(n6);
        if (n3 == 0) {
            n2 = 0;
        }
        mstPayConfigEntryItem.setDirection(n2);
        return mstPayConfigEntryItem;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static MstPayConfig buildSequenceFromString(String var0) {
        block7 : {
            block8 : {
                var1_1 = 0;
                var2_2 = SequenceUtils.parseBrackets(var0);
                var3_3 = new MstPayConfig();
                var4_4 = new ArrayList();
                if (var2_2.size() != 1) break block8;
                var5_5 = new ArrayList();
                var6_6 = (String)var2_2.get(0);
                var7_7 = new MstPayConfigEntry();
                var7_7.setBaudRate(SequenceUtils.getbaudRate(var6_6));
                var7_7.setDelayBetweenRepeat(SequenceUtils.getDelay(var6_6));
                var5_5.add((Object)SequenceUtils.buildSegment(var6_6));
                var7_7.setMstPayConfigEntry((List<MstPayConfigEntryItem>)var5_5);
                var4_4.add((Object)var7_7);
                ** GOTO lbl20
            }
            if (var2_2.size() <= 1) ** GOTO lbl20
            var10_8 = 0;
            block0 : do {
                block10 : {
                    block9 : {
                        if (var1_1 <= -1 + var2_2.size()) break block9;
lbl20: // 3 sources:
                        var3_3.setMstPayConfigEntry((List<MstPayConfigEntry>)var4_4);
                        return var3_3;
                    }
                    if (var1_1 != -1 + var2_2.size()) break block10;
                    var11_9 = var1_1 + 1;
                    ** GOTO lbl-1000
                }
                var11_9 = var1_1;
lbl28: // 2 sources:
                while (var11_9 >= var2_2.size()) lbl-1000: // 3 sources:
                {
                    do {
                        var12_10 = new MstPayConfigEntry();
                        var12_10.setBaudRate(SequenceUtils.getbaudRate((String)var2_2.get(var11_9 - 1)));
                        var12_10.setDelayBetweenRepeat(SequenceUtils.getDelay((String)var2_2.get(var11_9 - 1)));
                        var13_11 = new ArrayList();
                        while (var10_8 < var11_9) {
                            var13_11.add((Object)SequenceUtils.buildSegment((String)var2_2.get(var10_8)));
                            ++var10_8;
                        }
                        var12_10.setMstPayConfigEntry((List<MstPayConfigEntryItem>)var13_11);
                        var4_4.add((Object)var12_10);
                        var1_1 = var11_9;
                        var10_8 = var11_9;
                        continue block0;
                        break;
                    } while (true);
                    continue block0;
                }
                break block7;
                break;
            } while (true);
lbl45: // 1 sources:
            do {
                ++var11_9;
                ** GOTO lbl28
                break;
            } while (true);
        }
        ** while (!SequenceUtils.isLumpEnd((String)((String)var2_2.get((int)var11_9))))
lbl50: // 1 sources:
        ++var11_9;
        ** while (true)
    }

    /*
     * Enabled aggressive block sorting
     */
    private static int extractNumber(String string) {
        String string2;
        block4 : {
            block3 : {
                if (string == null) break block3;
                string2 = "";
                for (int i2 = 0; i2 <= -1 + string.length() && TextUtils.isDigitsOnly((CharSequence)String.valueOf((char)string.charAt(i2))); ++i2) {
                    string2 = string2 + String.valueOf((char)string.charAt(i2));
                }
                if (!string2.equals((Object)"")) break block4;
            }
            return 0;
        }
        return Integer.valueOf((String)string2);
    }

    private static int getDelay(String string) {
        String string2 = string.toLowerCase();
        int n2 = SequenceUtils.extractNumber(string2.substring(1 + string2.indexOf("d")));
        if (n2 < 0 || n2 > 5000) {
            throw new PlccException("Invalid Sequence Format Delay should be between [0,5000]");
        }
        return n2;
    }

    private static int getbaudRate(String string) {
        int n2;
        int n3 = string.indexOf("r");
        if (n3 != -1) {
            n2 = SequenceUtils.extractNumber(string.substring(n3 + 1));
            if (n2 < 20 || n2 > 2000) {
                throw new PlccException("Invalid Sequence Format: Baud rate should be between [20,2000]");
            }
        } else {
            throw new PlccException("Invalid Sequence Format No baud rate found for lump");
        }
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean isLumpEnd(String string) {
        int n2;
        String string2;
        return string != null && (n2 = (string2 = string.toLowerCase()).indexOf("d")) != -1 && SequenceUtils.extractNumber(string2.substring(n2 + 1)) != 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static List<String> parseBrackets(String string) {
        ArrayList arrayList = new ArrayList();
        if (string != null) {
            Matcher matcher = Pattern.compile((String)"\\((.*?)\\)").matcher((CharSequence)string);
            while (matcher.find()) {
                arrayList.add((Object)matcher.group());
            }
        }
        return arrayList;
    }
}

