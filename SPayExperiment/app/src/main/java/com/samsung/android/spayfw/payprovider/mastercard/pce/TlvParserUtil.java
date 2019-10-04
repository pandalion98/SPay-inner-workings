/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  com.google.gson.Gson
 *  java.lang.ArrayIndexOutOfBoundsException
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.IllegalArgumentException
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.mastercard.mobile_api.utils.tlv.ParsingException;
import com.mastercard.mobile_api.utils.tlv.TLVHandler;
import com.mastercard.mobile_api.utils.tlv.TLVParser;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TlvParserUtil {
    private static final String TAG = "mcpce_TlvParserUtil";

    static /* synthetic */ Map access$000(Map map, String string, String string2) {
        return TlvParserUtil.appendIfNotPresent((Map<String, List<String>>)map, string, string2);
    }

    private static Map<String, List<String>> appendIfNotPresent(Map<String, List<String>> map, String string, String string2) {
        if (map == null || TextUtils.isEmpty((CharSequence)string) || TextUtils.isEmpty((CharSequence)string2)) {
            return null;
        }
        return TlvParserUtil.appendIfNotPresent(map, string, (List<String>)new ArrayList((Collection)Arrays.asList((Object[])new String[]{string2})));
    }

    private static Map<String, List<String>> appendIfNotPresent(Map<String, List<String>> map, String string, List<String> list) {
        List list2;
        if (map == null || TextUtils.isEmpty((CharSequence)string) || list == null || list.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (map.containsKey((Object)string) && (list2 = (List)map.remove((Object)string)) != null && !list2.isEmpty()) {
            arrayList.addAll((Collection)list2);
        }
        arrayList.addAll(list);
        map.put((Object)string, (Object)arrayList);
        return map;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Map<String, List<String>> extractAllTags(byte[] arrby) {
        if (arrby == null) {
            return null;
        }
        Map<String, List<String>> map = TlvParserUtil.getAllTags(McUtils.byteArrayToHex(arrby));
        if (map == null) return map;
        c.d(TAG, "mapJson : " + new Gson().toJson(map));
        return map;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static List<String> extractTagData(byte[] arrby, Mctags mctags) {
        List list;
        block3 : {
            block2 : {
                list = null;
                if (arrby == null) break block2;
                list = null;
                if (mctags == null) break block2;
                Map<String, List<String>> map = TlvParserUtil.extractAllTags(arrby);
                list = null;
                if (map == null) break block2;
                boolean bl = map.isEmpty();
                list = null;
                if (bl) break block2;
                c.d(TAG, "mapJson : " + new Gson().toJson(map));
                boolean bl2 = map.containsKey((Object)mctags.getTag());
                list = null;
                if (bl2 && (list = (List)map.get((Object)mctags.getTag())) != null && !list.isEmpty()) break block3;
            }
            return list;
        }
        c.d(TAG, "key Found : " + mctags.getTag() + " value: " + list.toString());
        return list;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private static Map<String, List<String>> getAllTags(String var0) {
        var18_1 = TlvParserUtil.class;
        // MONITORENTER : com.samsung.android.spayfw.payprovider.mastercard.pce.TlvParserUtil.class
        var2_2 = TextUtils.isEmpty((CharSequence)var0);
        if (var2_2) {
            var7_3 = null;
            // MONITOREXIT : var18_1
            return var7_3;
        }
        var3_4 = new ArrayList();
        var4_5 = new HashMap();
        var5_6 = new SimpleTlvHandler();
        var3_4.add((Object)var0);
        do {
            block11 : {
                if (var3_4.isEmpty()) {
                    return var4_5;
                }
                var8_7 = (String)var3_4.remove(0);
                if (TextUtils.isEmpty((CharSequence)var8_7)) continue;
                var9_8 = McUtils.convertStirngToByteArray(var8_7);
                try {
                    var5_6.reset();
                    TLVParser.parseTLV(var9_8, 0, var9_8.length, var5_6);
                }
                catch (ParsingException var10_9) {
                    c.b("mcpce_TlvParserUtil", "ParsingException !!!" + var8_7, (Throwable)var10_9);
                }
                var11_10 = var5_6.getParsedTlvNodes();
                if (var11_10.isEmpty()) continue;
                break block11;
                continue;
            }
            var12_11 = var11_10.entrySet().iterator();
            do {
                if (!var12_11.hasNext()) ** break;
                var13_12 = (Map.Entry)var12_11.next();
                var14_13 = (String)var13_12.getKey();
                var15_14 = (List)var13_12.getValue();
                if (TextUtils.isEmpty((CharSequence)var14_13) || var15_14 == null || var15_14.isEmpty()) continue;
                c.d("mcpce_TlvParserUtil", "Key : " + (String)var13_12.getKey() + " Values : " + var15_14.toString());
                if (TlvParserUtil.isPrimitiveTag(var14_13)) {
                    c.d("mcpce_TlvParserUtil", "Key : Primitive " + var14_13);
                    TlvParserUtil.appendIfNotPresent((Map<String, List<String>>)var4_5, var14_13, (List<String>)var15_14);
                    continue;
                }
                c.d("mcpce_TlvParserUtil", "Key : Non Primitive " + var14_13);
                var3_4.addAll((Collection)var15_14);
            } while (true);
            break;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean isPrimitiveTag(String string) {
        return TextUtils.isEmpty((CharSequence)string) || (string.charAt(0) >> 1) % 2 == 0;
    }

    public static final class Mctags
    extends Enum<Mctags> {
        private static final /* synthetic */ Mctags[] $VALUES;
        public static final /* enum */ Mctags APPLICATION_LABEL;
        public static final /* enum */ Mctags APPLICATION_USAGE_CONTROL;
        public static final /* enum */ Mctags APP_PRIORITY_INDICATOR;
        public static final /* enum */ Mctags AVN;
        public static final /* enum */ Mctags CDOL1;
        public static final /* enum */ Mctags CDOL2;
        public static final /* enum */ Mctags CURRENCY_CODE;
        public static final /* enum */ Mctags CVM_LIST;
        public static final /* enum */ Mctags DF_ADF_NAME;
        public static final /* enum */ Mctags EFFECTIVE_DATE;
        public static final /* enum */ Mctags EXPIRY_DATE;
        public static final /* enum */ Mctags IAC_DEFAULT;
        public static final /* enum */ Mctags IAC_DENIAL;
        public static final /* enum */ Mctags IAC_ONLINE;
        public static final /* enum */ Mctags ICC_PUB_KEY_CERT;
        public static final /* enum */ Mctags ICC_PUB_KEY_EXP;
        public static final /* enum */ Mctags ICC_PUB_KEY_REM;
        public static final /* enum */ Mctags ISSUER_COUNTRY_CODE;
        public static final /* enum */ Mctags ISSUER_PUB_KEY_CERT;
        public static final /* enum */ Mctags ISSUER_PUB_KEY_EXP;
        public static final /* enum */ Mctags ISSUER_PUB_KEY_REM;
        public static final /* enum */ Mctags KEY_INDEX;
        public static final /* enum */ Mctags LANG_PREF;
        public static final /* enum */ Mctags MAGSTRIPE_AVN;
        public static final /* enum */ Mctags NATC_TRACK1;
        public static final /* enum */ Mctags NATC_TRACK2;
        public static final /* enum */ Mctags PAN;
        public static final /* enum */ Mctags PAN_SEQUENCE;
        public static final /* enum */ Mctags PAR;
        public static final /* enum */ Mctags PCVC3_TRACK1;
        public static final /* enum */ Mctags PCVC3_TRACK2;
        public static final /* enum */ Mctags PDOL;
        public static final /* enum */ Mctags PPSE_AID;
        public static final /* enum */ Mctags PUNATC_TRACK1;
        public static final /* enum */ Mctags PUNATC_TRACK2;
        public static final /* enum */ Mctags SDA;
        public static final /* enum */ Mctags THIRD_PARTY_DATA;
        public static final /* enum */ Mctags TRACK1_DATA;
        public static final /* enum */ Mctags TRACK2;
        public static final /* enum */ Mctags TRACK2_DATA;
        public static final /* enum */ Mctags UDOL;
        private final String mDesc;
        private final String mTag;

        static {
            TRACK2 = new Mctags("57", "Track2 Data");
            PAN = new Mctags("5A", "Application PAN");
            EXPIRY_DATE = new Mctags("5F24", "EXPIRY_DATE");
            EFFECTIVE_DATE = new Mctags("5F25", "EFFECTIVE_DATE");
            ISSUER_COUNTRY_CODE = new Mctags("5F28", "ISSUR COUNTRY CODE");
            PAN_SEQUENCE = new Mctags("5F34", "PAN_SEQUENCE");
            CDOL1 = new Mctags("8C", "CDOL1");
            CDOL2 = new Mctags("8D", "CDOL2");
            CVM_LIST = new Mctags("8E", "CVM_LIST");
            APPLICATION_USAGE_CONTROL = new Mctags("9F07", "APPLICATION_USAGE_CONTROL");
            AVN = new Mctags("9F08", "Application Version Number");
            IAC_DEFAULT = new Mctags("9F0D", "IAC_DEFAULT");
            IAC_DENIAL = new Mctags("9F0E", "IAC_DENIAL");
            IAC_ONLINE = new Mctags("9F0F", "IAC_ONLINE");
            CURRENCY_CODE = new Mctags("9F42", "CURRENCY_CODE");
            SDA = new Mctags("9F4A", "SDA TAG LIST");
            PAR = new Mctags("9F24", "Payment Account Reference");
            MAGSTRIPE_AVN = new Mctags("9F6C", "MAGSTRIPE_AVN");
            PCVC3_TRACK1 = new Mctags("9F62", "PCVC3_TRACK1");
            PUNATC_TRACK1 = new Mctags("9F63", "PUNATC TRACK1");
            NATC_TRACK1 = new Mctags("9F64", "NATC_TRACK1");
            TRACK1_DATA = new Mctags("56", "TRACK1_DATA");
            PCVC3_TRACK2 = new Mctags("9F65", "PCVC3_TRACK2");
            PUNATC_TRACK2 = new Mctags("9F66", "PUNATC TRACK2");
            NATC_TRACK2 = new Mctags("9F67", "NATC_TRACK2");
            TRACK2_DATA = new Mctags("9F6B", "TRACK2_DATA");
            UDOL = new Mctags("9F69", "udol");
            KEY_INDEX = new Mctags("8F", "KEY_INDEX");
            ISSUER_PUB_KEY_EXP = new Mctags("9F32", "PUB_KEY_EXP");
            ISSUER_PUB_KEY_REM = new Mctags("92", "PUB_KEY_REM");
            ISSUER_PUB_KEY_CERT = new Mctags("90", "PUB_KEY_CERT");
            ICC_PUB_KEY_EXP = new Mctags("9F47", "PUB_KEY_EXP");
            ICC_PUB_KEY_REM = new Mctags("9F48", "PUB_KEY_REM");
            ICC_PUB_KEY_CERT = new Mctags("9F46", "PUB_KEY_CERT");
            PPSE_AID = new Mctags("4F", "PPSE_AID");
            APP_PRIORITY_INDICATOR = new Mctags("87", "Application Priority Indicator");
            DF_ADF_NAME = new Mctags("84", "DF_ADF_NAME");
            APPLICATION_LABEL = new Mctags("50", "APPLICATION_LABEL");
            PDOL = new Mctags("9F38", "PDOL");
            LANG_PREF = new Mctags("5F2D", "Language Preference");
            THIRD_PARTY_DATA = new Mctags("9F6E", "Third Paty Data");
            Mctags[] arrmctags = new Mctags[]{TRACK2, PAN, EXPIRY_DATE, EFFECTIVE_DATE, ISSUER_COUNTRY_CODE, PAN_SEQUENCE, CDOL1, CDOL2, CVM_LIST, APPLICATION_USAGE_CONTROL, AVN, IAC_DEFAULT, IAC_DENIAL, IAC_ONLINE, CURRENCY_CODE, SDA, PAR, MAGSTRIPE_AVN, PCVC3_TRACK1, PUNATC_TRACK1, NATC_TRACK1, TRACK1_DATA, PCVC3_TRACK2, PUNATC_TRACK2, NATC_TRACK2, TRACK2_DATA, UDOL, KEY_INDEX, ISSUER_PUB_KEY_EXP, ISSUER_PUB_KEY_REM, ISSUER_PUB_KEY_CERT, ICC_PUB_KEY_EXP, ICC_PUB_KEY_REM, ICC_PUB_KEY_CERT, PPSE_AID, APP_PRIORITY_INDICATOR, DF_ADF_NAME, APPLICATION_LABEL, PDOL, LANG_PREF, THIRD_PARTY_DATA};
            $VALUES = arrmctags;
        }

        private Mctags(String string2, String string3) {
            this.mTag = string2;
            this.mDesc = string3;
        }

        public static List<String> getAllTags() {
            ArrayList arrayList = new ArrayList();
            Mctags[] arrmctags = Mctags.values();
            int n2 = arrmctags.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                arrayList.add((Object)arrmctags[i2].getTag());
            }
            return arrayList;
        }

        public static Mctags valueOf(String string) {
            return (Mctags)Enum.valueOf(Mctags.class, (String)string);
        }

        public static Mctags[] values() {
            return (Mctags[])$VALUES.clone();
        }

        public String getDesc() {
            return this.mDesc;
        }

        public String getTag() {
            return this.mTag;
        }
    }

    private static final class SimpleTlvHandler
    extends TLVHandler {
        private final Map<String, List<String>> mTagsFound = new HashMap();

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private void parseTagInternal(String var1_1, int var2_2, byte[] var3_3, int var4_4) {
            if (TextUtils.isEmpty((CharSequence)var1_1) || var3_3 == null || var3_3.length == 0 || var2_2 <= 0 || var4_4 < 0 || var4_4 > var3_3.length || var2_2 > var3_3.length || var4_4 + var2_2 > var3_3.length) {
                c.e("mcpce_TlvParserUtil", "Parsing Error!!! mTag :" + var1_1 + " length : " + var2_2 + " offset : " + var4_4);
                if (var3_3 == null) return;
                c.e("mcpce_TlvParserUtil", "data :" + McUtils.byteArrayToHex(var3_3));
                return;
            }
            var5_5 = var4_4 + var2_2;
            try {
                var7_6 = Arrays.copyOfRange((byte[])var3_3, (int)var4_4, (int)var5_5);
                TlvParserUtil.access$000(this.mTagsFound, var1_1.toUpperCase(), McUtils.byteArrayToHex(var7_6));
                return;
            }
            catch (IllegalArgumentException var6_7) {}
            ** GOTO lbl-1000
            catch (NullPointerException var6_9) {
                ** GOTO lbl-1000
            }
            catch (ArrayIndexOutOfBoundsException var6_10) {}
lbl-1000: // 3 sources:
            {
                c.a("mcpce_TlvParserUtil", "Parsing Exception!!! ", (Throwable)var6_8);
                return;
            }
        }

        public Map<String, List<String>> getParsedTlvNodes() {
            HashMap hashMap = new HashMap();
            hashMap.putAll(this.mTagsFound);
            return hashMap;
        }

        @Override
        public void parseTag(byte by, int n2, byte[] arrby, int n3) {
            if (by != 0) {
                this.parseTagInternal(McUtils.byteToHex(by), n2, arrby, n3);
            }
        }

        @Override
        public void parseTag(short s2, int n2, byte[] arrby, int n3) {
            if (s2 != 0) {
                this.parseTagInternal(McUtils.shortToHex(s2), n2, arrby, n3);
            }
        }

        public void reset() {
            this.mTagsFound.clear();
        }
    }

}

