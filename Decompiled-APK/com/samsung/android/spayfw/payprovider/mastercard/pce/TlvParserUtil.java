package com.samsung.android.spayfw.payprovider.mastercard.pce;

import android.text.TextUtils;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.EMVConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.google.gson.Gson;
import com.mastercard.mobile_api.utils.tlv.TLVHandler;
import com.mastercard.mobile_api.utils.tlv.TLVParser;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TlvParserUtil {
    private static final String TAG = "mcpce_TlvParserUtil";

    public enum Mctags {
        TRACK2(Constants.TRACK2_EQUIVALENT_DATA, "Track2 Data"),
        PAN("5A", "Application PAN"),
        EXPIRY_DATE("5F24", "EXPIRY_DATE"),
        EFFECTIVE_DATE("5F25", "EFFECTIVE_DATE"),
        ISSUER_COUNTRY_CODE("5F28", "ISSUR COUNTRY CODE"),
        PAN_SEQUENCE("5F34", "PAN_SEQUENCE"),
        CDOL1("8C", "CDOL1"),
        CDOL2("8D", "CDOL2"),
        CVM_LIST("8E", "CVM_LIST"),
        APPLICATION_USAGE_CONTROL("9F07", "APPLICATION_USAGE_CONTROL"),
        AVN("9F08", "Application Version Number"),
        IAC_DEFAULT("9F0D", "IAC_DEFAULT"),
        IAC_DENIAL("9F0E", "IAC_DENIAL"),
        IAC_ONLINE("9F0F", "IAC_ONLINE"),
        CURRENCY_CODE("9F42", "CURRENCY_CODE"),
        SDA("9F4A", "SDA TAG LIST"),
        PAR("9F24", "Payment Account Reference"),
        MAGSTRIPE_AVN("9F6C", "MAGSTRIPE_AVN"),
        PCVC3_TRACK1("9F62", "PCVC3_TRACK1"),
        PUNATC_TRACK1("9F63", "PUNATC TRACK1"),
        NATC_TRACK1("9F64", "NATC_TRACK1"),
        TRACK1_DATA("56", "TRACK1_DATA"),
        PCVC3_TRACK2("9F65", "PCVC3_TRACK2"),
        PUNATC_TRACK2("9F66", "PUNATC TRACK2"),
        NATC_TRACK2("9F67", "NATC_TRACK2"),
        TRACK2_DATA("9F6B", "TRACK2_DATA"),
        UDOL("9F69", "udol"),
        KEY_INDEX("8F", "KEY_INDEX"),
        ISSUER_PUB_KEY_EXP("9F32", "PUB_KEY_EXP"),
        ISSUER_PUB_KEY_REM("92", "PUB_KEY_REM"),
        ISSUER_PUB_KEY_CERT("90", "PUB_KEY_CERT"),
        ICC_PUB_KEY_EXP("9F47", "PUB_KEY_EXP"),
        ICC_PUB_KEY_REM("9F48", "PUB_KEY_REM"),
        ICC_PUB_KEY_CERT("9F46", "PUB_KEY_CERT"),
        PPSE_AID(EMVConstants.APPLICATION_IDENTIFER_TAG, "PPSE_AID"),
        APP_PRIORITY_INDICATOR(EMVConstants.PRIORITY_INDICATOR_TAG, "Application Priority Indicator"),
        DF_ADF_NAME(APDUConstants.SELECT_RESPONSE_DF_NAME, "DF_ADF_NAME"),
        APPLICATION_LABEL(EMVConstants.APPLICATION_LABEL_TAG, CPDLConfig.APPLICATION_LABEL),
        PDOL("9F38", "PDOL"),
        LANG_PREF("5F2D", "Language Preference"),
        THIRD_PARTY_DATA(EMVConstants.TERMINAL_CAPABILITY, "Third Paty Data");
        
        private final String mDesc;
        private final String mTag;

        private Mctags(String str, String str2) {
            this.mTag = str;
            this.mDesc = str2;
        }

        public String getDesc() {
            return this.mDesc;
        }

        public String getTag() {
            return this.mTag;
        }

        public static List<String> getAllTags() {
            List<String> arrayList = new ArrayList();
            for (Mctags tag : values()) {
                arrayList.add(tag.getTag());
            }
            return arrayList;
        }
    }

    private static final class SimpleTlvHandler extends TLVHandler {
        private final Map<String, List<String>> mTagsFound;

        public SimpleTlvHandler() {
            this.mTagsFound = new HashMap();
        }

        public void parseTag(byte b, int i, byte[] bArr, int i2) {
            if (b != null) {
                parseTagInternal(McUtils.byteToHex(b), i, bArr, i2);
            }
        }

        public void parseTag(short s, int i, byte[] bArr, int i2) {
            if (s != (short) 0) {
                parseTagInternal(McUtils.shortToHex(s), i, bArr, i2);
            }
        }

        private void parseTagInternal(String str, int i, byte[] bArr, int i2) {
            Throwable e;
            if (TextUtils.isEmpty(str) || bArr == null || bArr.length == 0 || i <= 0 || i2 < 0 || i2 > bArr.length || i > bArr.length || i2 + i > bArr.length) {
                Log.m286e(TlvParserUtil.TAG, "Parsing Error!!! mTag :" + str + " length : " + i + " offset : " + i2);
                if (bArr != null) {
                    Log.m286e(TlvParserUtil.TAG, "data :" + McUtils.byteArrayToHex(bArr));
                    return;
                }
                return;
            }
            try {
                TlvParserUtil.appendIfNotPresent(this.mTagsFound, str.toUpperCase(), McUtils.byteArrayToHex(Arrays.copyOfRange(bArr, i2, i2 + i)));
            } catch (IllegalArgumentException e2) {
                e = e2;
                Log.m281a(TlvParserUtil.TAG, "Parsing Exception!!! ", e);
            } catch (NullPointerException e3) {
                e = e3;
                Log.m281a(TlvParserUtil.TAG, "Parsing Exception!!! ", e);
            } catch (ArrayIndexOutOfBoundsException e4) {
                e = e4;
                Log.m281a(TlvParserUtil.TAG, "Parsing Exception!!! ", e);
            }
        }

        public Map<String, List<String>> getParsedTlvNodes() {
            Map<String, List<String>> hashMap = new HashMap();
            hashMap.putAll(this.mTagsFound);
            return hashMap;
        }

        public void reset() {
            this.mTagsFound.clear();
        }
    }

    public static List<String> extractTagData(byte[] bArr, Mctags mctags) {
        List<String> list = null;
        if (!(bArr == null || mctags == null)) {
            Object extractAllTags = extractAllTags(bArr);
            if (!(extractAllTags == null || extractAllTags.isEmpty())) {
                Log.m285d(TAG, "mapJson : " + new Gson().toJson(extractAllTags));
                if (extractAllTags.containsKey(mctags.getTag())) {
                    list = (List) extractAllTags.get(mctags.getTag());
                    if (!(list == null || list.isEmpty())) {
                        Log.m285d(TAG, "key Found : " + mctags.getTag() + " value: " + list.toString());
                    }
                }
            }
        }
        return list;
    }

    public static Map<String, List<String>> extractAllTags(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        Object allTags = getAllTags(McUtils.byteArrayToHex(bArr));
        if (allTags == null) {
            return allTags;
        }
        Log.m285d(TAG, "mapJson : " + new Gson().toJson(allTags));
        return allTags;
    }

    private static boolean isPrimitiveTag(String str) {
        if (TextUtils.isEmpty(str) || (str.charAt(0) >> 1) % 2 == 0) {
            return true;
        }
        return false;
    }

    private static synchronized Map<String, List<String>> getAllTags(String str) {
        Map<String, List<String>> map;
        synchronized (TlvParserUtil.class) {
            if (TextUtils.isEmpty(str)) {
                map = null;
            } else {
                List arrayList = new ArrayList();
                Map<String, List<String>> hashMap = new HashMap();
                TLVHandler simpleTlvHandler = new SimpleTlvHandler();
                arrayList.add(str);
                while (!arrayList.isEmpty()) {
                    String str2 = (String) arrayList.remove(0);
                    if (!TextUtils.isEmpty(str2)) {
                        byte[] convertStirngToByteArray = McUtils.convertStirngToByteArray(str2);
                        try {
                            simpleTlvHandler.reset();
                            TLVParser.parseTLV(convertStirngToByteArray, 0, convertStirngToByteArray.length, simpleTlvHandler);
                            Map parsedTlvNodes = simpleTlvHandler.getParsedTlvNodes();
                            if (!parsedTlvNodes.isEmpty()) {
                                for (Entry entry : parsedTlvNodes.entrySet()) {
                                    String str3 = (String) entry.getKey();
                                    List list = (List) entry.getValue();
                                    if (!(TextUtils.isEmpty(str3) || list == null || list.isEmpty())) {
                                        Log.m285d(TAG, "Key : " + ((String) entry.getKey()) + " Values : " + list.toString());
                                        if (isPrimitiveTag(str3)) {
                                            Log.m285d(TAG, "Key : Primitive " + str3);
                                            appendIfNotPresent((Map) hashMap, str3, list);
                                        } else {
                                            Log.m285d(TAG, "Key : Non Primitive " + str3);
                                            arrayList.addAll(list);
                                        }
                                    }
                                }
                            }
                        } catch (Throwable e) {
                            Log.m283b(TAG, "ParsingException !!!" + str2, e);
                        }
                    }
                }
                map = hashMap;
            }
        }
        return map;
    }

    private static Map<String, List<String>> appendIfNotPresent(Map<String, List<String>> map, String str, String str2) {
        if (map == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        return appendIfNotPresent((Map) map, str, new ArrayList(Arrays.asList(new String[]{str2})));
    }

    private static Map<String, List<String>> appendIfNotPresent(Map<String, List<String>> map, String str, List<String> list) {
        if (map == null || TextUtils.isEmpty(str) || list == null || list.isEmpty()) {
            return null;
        }
        List arrayList = new ArrayList();
        if (map.containsKey(str)) {
            List list2 = (List) map.remove(str);
            if (!(list2 == null || list2.isEmpty())) {
                arrayList.addAll(list2);
            }
        }
        arrayList.addAll(list);
        map.put(str, arrayList);
        return map;
    }
}
