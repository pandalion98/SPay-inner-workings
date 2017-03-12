package android.telephony;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.location.CountryDetector;
import android.net.Uri;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings$System;
import android.telecom.PhoneAccount;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.TtsSpan;
import android.text.style.TtsSpan.TelephoneBuilder;
import android.util.SparseIntArray;
import com.android.i18n.phonenumbers.NumberParseException;
import com.android.i18n.phonenumbers.PhoneNumberUtil;
import com.android.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.android.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.android.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;
import com.android.internal.R;
import com.android.internal.content.NativeLibraryHelper;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephony.Stub;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.SmsConstants;
import com.android.internal.telephony.TelephonyProperties;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.smartface.SmartFaceManager;
import com.sec.android.app.CscFeature;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberUtils {
    private static final int CCC_LENGTH = COUNTRY_CALLING_CALL.length;
    private static final String CLIDIGITS_KEY = "Clidigits";
    private static final String CLIDIGITS_PREFERENCES_NAME = "clidigits.preferences_name";
    private static final String CLIR_OFF = "#31#";
    private static final String CLIR_ON = "*31#";
    private static final boolean[] COUNTRY_CALLING_CALL = new boolean[]{true, true, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, true, true, false, true, true, true, true, true, false, true, false, false, true, true, false, false, true, true, true, true, true, true, true, false, true, true, true, true, true, true, true, true, false, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, true, true, false, true, false, false, true, true, true, true, true, true, true, false, false, true, false};
    private static final boolean DBG = false;
    private static final int ECC_CATEGORY_Ambulance = 2;
    private static final int ECC_CATEGORY_Cyber_Terror = 19;
    private static final int ECC_CATEGORY_Default_Emergency_Center = 0;
    private static final int ECC_CATEGORY_Drug_Report = 17;
    private static final int ECC_CATEGORY_Fire_Brigade = 4;
    private static final int ECC_CATEGORY_Marine_Guard = 8;
    private static final int ECC_CATEGORY_Mountain_Rescue = 16;
    private static final int ECC_CATEGORY_National_Intelligence_Service_KT = 7;
    private static final int ECC_CATEGORY_National_Intelligence_Service_SKT = 6;
    private static final int ECC_CATEGORY_Normal_Call = 255;
    private static final int ECC_CATEGORY_Police = 1;
    private static final int ECC_CATEGORY_School_Violence = 18;
    private static final int ECC_CATEGORY_Smuggling_Report = 9;
    private static final int ECC_CATEGORY_Spy_Report = 3;
    public static final int FORMAT_JAPAN = 2;
    public static final int FORMAT_KOREA = 82;
    public static final int FORMAT_NANP = 1;
    public static final int FORMAT_UNKNOWN = 0;
    private static final Pattern GLOBAL_PHONE_NUMBER_PATTERN = Pattern.compile("[\\+]?[0-9.-]+");
    private static final String IP_CALL_PREFIX = "ip_call_prefix_sub";
    private static final SparseIntArray KEYPAD_MAP = new SparseIntArray();
    private static final String KOREA_ISO_COUNTRY_CODE = "KR";
    private static final int KRNP_STATE_0505_START = 14;
    private static final int KRNP_STATE_AREA_SEOUL = 6;
    private static final int KRNP_STATE_EXCEPT_CASE_1 = 10;
    private static final int KRNP_STATE_EXCEPT_CASE_2 = 11;
    private static final int KRNP_STATE_NORMAL = 5;
    private static final int KRNP_STATE_PLUS = 9;
    private static final int KRNP_STATE_SHARP = 13;
    private static final int KRNP_STATE_SHARP_NINE = 8;
    private static final int KRNP_STATE_STAR = 12;
    private static final int KRNP_STATE_ZERO_START = 7;
    static final String LOG_TAG = "PhoneNumberUtils";
    private static final Uri MCC_OTA_URI = Uri.parse("content://assisteddialing/mcc_otalookup");
    static final int MIN_MATCH = 7;
    static final int MIN_MATCH_CHINA = 11;
    static final int MIN_MATCH_HK = 8;
    static final int MIN_MATCH_TW = 9;
    private static final String[] NANP_COUNTRIES = new String[]{"US", "CA", "AS", "AI", "AG", "BS", "BB", "BM", "VG", "KY", "DM", "DO", "GD", "GU", "JM", "PR", "MS", "MP", "KN", "LC", "VC", "TT", "TC", "VI"};
    private static final String NANP_IDP_STRING = "011";
    private static final String NANP_IDP_STRING_00 = "00";
    private static final String NANP_IDP_STRING_001 = "001";
    private static final String NANP_IDP_STRING_005 = "005";
    private static final String NANP_IDP_STRING_00700 = "00700";
    private static final String NANP_IDP_STRING_010 = "010";
    private static final String NANP_IDP_STRING_011 = "011";
    private static final int NANP_LENGTH = 10;
    private static final int NANP_STATE_DASH = 4;
    private static final int NANP_STATE_DIGIT = 1;
    private static final int NANP_STATE_ONE = 3;
    private static final int NANP_STATE_PLUS = 2;
    public static final String OTA_COUNTRY_MCC_KEY = "otaCountryMccKey";
    private static final Uri OTA_COUNTRY_URI = Uri.parse("content://assisteddialing/ota_country");
    public static final char PAUSE = ',';
    private static final char PLUS_SIGN_CHAR = '+';
    private static final String PLUS_SIGN_STRING = "+";
    private static final Uri REF_COUNTRY_SHARED_PREF = Uri.parse("content://assisteddialing/refcountry");
    public static final int TOA_International = 145;
    public static final int TOA_Unknown = 129;
    public static final char WAIT = ';';
    public static final char WILD = 'N';
    public static boolean isAssistedDialingNumber = false;
    private static boolean isCDMARegistered = false;
    private static boolean isGSMRegistered = false;
    private static boolean isNANPCountry = false;
    private static boolean isNBPCDSupported = false;
    private static boolean isNetRoaming = false;
    private static boolean isOTANANPCountry = false;
    private static boolean isOTANBPCDSupported = false;
    private static String mConfigNetworkTypeCapability = CscFeature.getInstance().getString("CscFeature_RIL_ConfigNetworkTypeCapability");
    private static Cursor mCursor;
    private static Cursor mCursorContry;
    private static int numberLength = 0;
    private static String otaCountryCountryCode = "";
    private static String otaCountryIDDPrefix = "";
    private static String otaCountryMCC = "";
    private static String otaCountryNDDPrefix = "";
    private static String otaCountryName = "";
    private static int phoneType = 0;
    private static String refCountryAreaCode = "";
    private static String refCountryCountryCode = "";
    private static String refCountryIDDPrefix = "";
    private static String refCountryMCC = "";
    private static String refCountryNDDPrefix = "";
    private static String refCountryName = "";
    private static Integer refCountryNationalNumberLength = Integer.valueOf(0);

    private static class CountryCallingCodeAndNewIndex {
        public final int countryCallingCode;
        public final int newIndex;

        public CountryCallingCodeAndNewIndex(int countryCode, int newIndex) {
            this.countryCallingCode = countryCode;
            this.newIndex = newIndex;
        }
    }

    static {
        KEYPAD_MAP.put(97, 50);
        KEYPAD_MAP.put(98, 50);
        KEYPAD_MAP.put(99, 50);
        KEYPAD_MAP.put(65, 50);
        KEYPAD_MAP.put(66, 50);
        KEYPAD_MAP.put(67, 50);
        KEYPAD_MAP.put(100, 51);
        KEYPAD_MAP.put(101, 51);
        KEYPAD_MAP.put(102, 51);
        KEYPAD_MAP.put(68, 51);
        KEYPAD_MAP.put(69, 51);
        KEYPAD_MAP.put(70, 51);
        KEYPAD_MAP.put(103, 52);
        KEYPAD_MAP.put(104, 52);
        KEYPAD_MAP.put(105, 52);
        KEYPAD_MAP.put(71, 52);
        KEYPAD_MAP.put(72, 52);
        KEYPAD_MAP.put(73, 52);
        KEYPAD_MAP.put(106, 53);
        KEYPAD_MAP.put(107, 53);
        KEYPAD_MAP.put(108, 53);
        KEYPAD_MAP.put(74, 53);
        KEYPAD_MAP.put(75, 53);
        KEYPAD_MAP.put(76, 53);
        KEYPAD_MAP.put(109, 54);
        KEYPAD_MAP.put(110, 54);
        KEYPAD_MAP.put(111, 54);
        KEYPAD_MAP.put(77, 54);
        KEYPAD_MAP.put(78, 54);
        KEYPAD_MAP.put(79, 54);
        KEYPAD_MAP.put(112, 55);
        KEYPAD_MAP.put(113, 55);
        KEYPAD_MAP.put(114, 55);
        KEYPAD_MAP.put(115, 55);
        KEYPAD_MAP.put(80, 55);
        KEYPAD_MAP.put(81, 55);
        KEYPAD_MAP.put(82, 55);
        KEYPAD_MAP.put(83, 55);
        KEYPAD_MAP.put(116, 56);
        KEYPAD_MAP.put(117, 56);
        KEYPAD_MAP.put(118, 56);
        KEYPAD_MAP.put(84, 56);
        KEYPAD_MAP.put(85, 56);
        KEYPAD_MAP.put(86, 56);
        KEYPAD_MAP.put(119, 57);
        KEYPAD_MAP.put(120, 57);
        KEYPAD_MAP.put(121, 57);
        KEYPAD_MAP.put(122, 57);
        KEYPAD_MAP.put(87, 57);
        KEYPAD_MAP.put(88, 57);
        KEYPAD_MAP.put(89, 57);
        KEYPAD_MAP.put(90, 57);
    }

    public static boolean isISODigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static final boolean is12Key(char c) {
        return (c >= '0' && c <= '9') || c == '*' || c == '#';
    }

    public static final boolean isDialable(char c) {
        return (c >= '0' && c <= '9') || c == '*' || c == '#' || c == PLUS_SIGN_CHAR || c == WILD;
    }

    public static final boolean isReallyDialable(char c) {
        return (c >= '0' && c <= '9') || c == '*' || c == '#' || c == PLUS_SIGN_CHAR;
    }

    public static final boolean isNonSeparator(char c) {
        return (c >= '0' && c <= '9') || c == '*' || c == '#' || c == PLUS_SIGN_CHAR || c == WILD || c == ';' || c == ',';
    }

    public static final boolean isStartsPostDial(char c) {
        return c == ',' || c == ';';
    }

    private static boolean isPause(char c) {
        return c == 'p' || c == 'P';
    }

    private static boolean isToneWait(char c) {
        return c == 'w' || c == 'W';
    }

    private static boolean isSeparator(char ch) {
        return !isDialable(ch) && ((DateFormat.AM_PM > ch || ch > DateFormat.TIME_ZONE) && (DateFormat.CAPITAL_AM_PM > ch || ch > 'Z'));
    }

    public static String getNumberFromIntent(Intent intent, Context context) {
        String number = null;
        Uri uri = intent.getData();
        if (uri == null) {
            return null;
        }
        String scheme = uri.getScheme();
        if (scheme.equals(PhoneAccount.SCHEME_TEL) || scheme.equals(PhoneAccount.SCHEME_SIP)) {
            return uri.getSchemeSpecificPart();
        }
        if (context == null) {
            return null;
        }
        String type = intent.resolveType(context);
        String phoneColumn = null;
        String authority = uri.getAuthority();
        if ("contacts".equals(authority)) {
            phoneColumn = "number";
        } else if ("com.android.contacts".equals(authority)) {
            phoneColumn = "data1";
        }
        Cursor c = null;
        try {
            c = context.getContentResolver().query(uri, new String[]{phoneColumn}, null, null, null);
            if (c != null && c.moveToFirst()) {
                number = c.getString(c.getColumnIndex(phoneColumn));
            }
            if (c != null) {
                c.close();
            }
        } catch (RuntimeException e) {
            Rlog.e(LOG_TAG, "Error getting phone number.", e);
            if (c != null) {
                c.close();
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
        return number;
    }

    public static String extractNetworkPortion(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        int len = phoneNumber.length();
        StringBuilder ret = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = phoneNumber.charAt(i);
            int digit = Character.digit(c, 10);
            if (digit != -1) {
                ret.append(digit);
            } else if (c == PLUS_SIGN_CHAR) {
                String prefix = ret.toString();
                if (prefix.length() == 0 || prefix.equals(CLIR_ON) || prefix.equals(CLIR_OFF)) {
                    ret.append(c);
                }
            } else if (isDialable(c)) {
                ret.append(c);
            } else if (isStartsPostDial(c)) {
                break;
            }
        }
        return ret.toString();
    }

    public static String extractNetworkPortionAlt(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        int len = phoneNumber.length();
        StringBuilder ret = new StringBuilder(len);
        boolean haveSeenPlus = false;
        for (int i = 0; i < len; i++) {
            char c = phoneNumber.charAt(i);
            if (c == PLUS_SIGN_CHAR) {
                if (haveSeenPlus) {
                    continue;
                } else {
                    haveSeenPlus = true;
                }
            }
            if (isDialable(c)) {
                ret.append(c);
            } else if (isStartsPostDial(c)) {
                break;
            }
        }
        return ret.toString();
    }

    public static String stripSeparators(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        int len = phoneNumber.length();
        StringBuilder ret = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = phoneNumber.charAt(i);
            int digit = Character.digit(c, 10);
            if (digit != -1) {
                ret.append(digit);
            } else if (isNonSeparator(c)) {
                ret.append(c);
            }
        }
        return ret.toString();
    }

    public static String convertAndStrip(String phoneNumber) {
        return stripSeparators(convertKeypadLettersToDigits(phoneNumber));
    }

    public static String convertPreDial(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        int len = phoneNumber.length();
        StringBuilder ret = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = phoneNumber.charAt(i);
            if (isPause(c)) {
                c = ',';
            } else if (isToneWait(c)) {
                c = ';';
            }
            ret.append(c);
        }
        return ret.toString();
    }

    private static int minPositive(int a, int b) {
        if (a >= 0 && b >= 0) {
            return a < b ? a : b;
        } else {
            if (a >= 0) {
                return a;
            }
            if (b >= 0) {
                return b;
            }
            return -1;
        }
    }

    private static void log(String msg) {
        Rlog.d(LOG_TAG, msg);
    }

    private static int indexOfLastNetworkChar(String a) {
        int origLength = a.length();
        int trimIndex = minPositive(a.indexOf(44), a.indexOf(59));
        if (trimIndex < 0) {
            return origLength - 1;
        }
        return trimIndex - 1;
    }

    public static String extractPostDialPortion(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        StringBuilder ret = new StringBuilder();
        int s = phoneNumber.length();
        for (int i = indexOfLastNetworkChar(phoneNumber) + 1; i < s; i++) {
            char c = phoneNumber.charAt(i);
            if (isNonSeparator(c)) {
                ret.append(c);
            }
        }
        return ret.toString();
    }

    public static boolean compare(String a, String b) {
        return compare(a, b, false);
    }

    public static boolean compare(Context context, String a, String b) {
        return compare(a, b, context.getResources().getBoolean(R.bool.config_use_strict_phone_number_comparation));
    }

    public static boolean compare(String a, String b, boolean useStrictComparation) {
        return useStrictComparation ? compareStrictly(a, b) : compareLoosely(a, b);
    }

    public static boolean compareLoosely(String a, String b) {
        int numNonDialableCharsInA = 0;
        int numNonDialableCharsInB = 0;
        if (a == null || b == null) {
            if (a == b) {
                return true;
            }
            return false;
        } else if (a.length() == 0 || b.length() == 0) {
            return false;
        } else {
            int ia = indexOfLastNetworkChar(a);
            int ib = indexOfLastNetworkChar(b);
            int matched = 0;
            while (ia >= 0 && ib >= 0) {
                boolean skipCmp = false;
                char ca = a.charAt(ia);
                if (!isDialable(ca)) {
                    ia--;
                    skipCmp = true;
                    numNonDialableCharsInA++;
                }
                char cb = b.charAt(ib);
                if (!isDialable(cb)) {
                    ib--;
                    skipCmp = true;
                    numNonDialableCharsInB++;
                }
                if (!skipCmp) {
                    if (cb != ca && ca != WILD && cb != WILD) {
                        break;
                    }
                    ia--;
                    ib--;
                    matched++;
                }
            }
            String salesCode = SystemProperties.get("ro.csc.sales_code");
            int effectiveALen;
            if ("CHN".equals(salesCode) || "CHU".equals(salesCode) || "CHM".equals(salesCode) || "CTC".equals(salesCode) || "CHC".equals(salesCode)) {
                if (matched < 11) {
                    effectiveALen = a.length() - numNonDialableCharsInA;
                    if (effectiveALen == b.length() - numNonDialableCharsInB && effectiveALen == matched) {
                        return true;
                    }
                    return false;
                } else if (matched >= 11 && (ia < 0 || ib < 0)) {
                    return true;
                }
            } else if ("FET".equals(salesCode) || "TWM".equals(salesCode) || "CWT".equals(salesCode) || "BRI".equals(salesCode)) {
                if (matched < 9) {
                    effectiveALen = a.length() - numNonDialableCharsInA;
                    if (effectiveALen == b.length() - numNonDialableCharsInB && effectiveALen == matched) {
                        return true;
                    }
                    return false;
                } else if (matched >= 9 && (ia < 0 || ib < 0)) {
                    return true;
                }
            } else if ("TGY".equals(salesCode)) {
                if (matched < 8) {
                    effectiveALen = a.length() - numNonDialableCharsInA;
                    if (effectiveALen == b.length() - numNonDialableCharsInB && effectiveALen == matched) {
                        return true;
                    }
                    return false;
                } else if (matched >= 8 && (ia < 0 || ib < 0)) {
                    return true;
                }
            } else if (matched < getMinMatch()) {
                effectiveALen = a.length() - numNonDialableCharsInA;
                if (effectiveALen == b.length() - numNonDialableCharsInB && effectiveALen == matched) {
                    return true;
                }
                return false;
            } else if (matched >= getMinMatch() && (ia < 0 || ib < 0)) {
                return true;
            }
            if (matchIntlPrefix(a, ia + 1) && matchIntlPrefix(b, ib + 1)) {
                return true;
            }
            if (matchTrunkPrefix(a, ia + 1) && matchIntlPrefixAndCC(b, ib + 1)) {
                return true;
            }
            if (matchTrunkPrefix(b, ib + 1) && matchIntlPrefixAndCC(a, ia + 1)) {
                return true;
            }
            boolean aPlusFirst = a.startsWith(PLUS_SIGN_STRING);
            boolean bPlusFirst = b.startsWith(PLUS_SIGN_STRING);
            if (ia >= 4 || ib >= 4 || ((!aPlusFirst && !bPlusFirst) || (aPlusFirst && bPlusFirst))) {
                return false;
            }
            return true;
        }
    }

    public static boolean compareStrictly(String a, String b) {
        return compareStrictly(a, b, true);
    }

    public static boolean compareStrictly(String a, String b, boolean acceptInvalidCCCPrefix) {
        if (a == null || b == null) {
            if (a == b) {
                return true;
            }
            return false;
        } else if (a.length() == 0 && b.length() == 0) {
            return false;
        } else {
            char chA;
            char chB;
            int forwardIndexA = 0;
            int forwardIndexB = 0;
            CountryCallingCodeAndNewIndex cccA = tryGetCountryCallingCodeAndNewIndex(a, acceptInvalidCCCPrefix);
            CountryCallingCodeAndNewIndex cccB = tryGetCountryCallingCodeAndNewIndex(b, acceptInvalidCCCPrefix);
            boolean bothHasCountryCallingCode = false;
            boolean okToIgnorePrefix = true;
            boolean trunkPrefixIsOmittedA = false;
            boolean trunkPrefixIsOmittedB = false;
            if (cccA != null && cccB != null) {
                if (cccA.countryCallingCode != cccB.countryCallingCode) {
                    return false;
                }
                okToIgnorePrefix = false;
                bothHasCountryCallingCode = true;
                forwardIndexA = cccA.newIndex;
                forwardIndexB = cccB.newIndex;
            } else if (cccA == null && cccB == null) {
                okToIgnorePrefix = false;
            } else {
                int tmp;
                if (cccA != null) {
                    forwardIndexA = cccA.newIndex;
                } else {
                    tmp = tryGetTrunkPrefixOmittedIndex(b, 0);
                    if (tmp >= 0) {
                        forwardIndexA = tmp;
                        trunkPrefixIsOmittedA = true;
                    }
                }
                if (cccB != null) {
                    forwardIndexB = cccB.newIndex;
                } else {
                    tmp = tryGetTrunkPrefixOmittedIndex(b, 0);
                    if (tmp >= 0) {
                        forwardIndexB = tmp;
                        trunkPrefixIsOmittedB = true;
                    }
                }
            }
            int backwardIndexA = a.length() - 1;
            int backwardIndexB = b.length() - 1;
            while (backwardIndexA >= forwardIndexA && backwardIndexB >= forwardIndexB) {
                boolean skip_compare = false;
                chA = a.charAt(backwardIndexA);
                chB = b.charAt(backwardIndexB);
                if (isSeparator(chA)) {
                    backwardIndexA--;
                    skip_compare = true;
                }
                if (isSeparator(chB)) {
                    backwardIndexB--;
                    skip_compare = true;
                }
                if (!skip_compare) {
                    if (chA != chB) {
                        return false;
                    }
                    backwardIndexA--;
                    backwardIndexB--;
                }
            }
            if (!okToIgnorePrefix) {
                boolean maybeNamp = !bothHasCountryCallingCode;
                while (backwardIndexA >= forwardIndexA) {
                    chA = a.charAt(backwardIndexA);
                    if (isDialable(chA)) {
                        if (!maybeNamp || tryGetISODigit(chA) != 1) {
                            return false;
                        }
                        maybeNamp = false;
                    }
                    backwardIndexA--;
                }
                while (backwardIndexB >= forwardIndexB) {
                    chB = b.charAt(backwardIndexB);
                    if (isDialable(chB)) {
                        if (!maybeNamp || tryGetISODigit(chB) != 1) {
                            return false;
                        }
                        maybeNamp = false;
                    }
                    backwardIndexB--;
                }
            } else if ((!trunkPrefixIsOmittedA || forwardIndexA > backwardIndexA) && checkPrefixIsIgnorable(a, forwardIndexA, backwardIndexA)) {
                if ((trunkPrefixIsOmittedB && forwardIndexB <= backwardIndexB) || !checkPrefixIsIgnorable(b, forwardIndexA, backwardIndexB)) {
                    if (acceptInvalidCCCPrefix) {
                        return compare(a, b, false);
                    }
                    return false;
                }
            } else if (acceptInvalidCCCPrefix) {
                return compare(a, b, false);
            } else {
                return false;
            }
            return true;
        }
    }

    public static String toCallerIDMinMatch(String phoneNumber) {
        String np = extractNetworkPortionAlt(phoneNumber);
        String salesCode = SystemProperties.get("ro.csc.sales_code");
        if ("CHN".equals(salesCode) || "CHU".equals(salesCode) || "CHM".equals(salesCode) || "CTC".equals(salesCode) || "CHC".equals(salesCode)) {
            return internalGetStrippedReversed(np, 11);
        }
        if ("FET".equals(salesCode) || "TWM".equals(salesCode) || "CWT".equals(salesCode) || "BRI".equals(salesCode)) {
            return internalGetStrippedReversed(np, 9);
        }
        if ("TGY".equals(salesCode)) {
            return internalGetStrippedReversed(np, 8);
        }
        return internalGetStrippedReversed(np, getMinMatch());
    }

    public static String getStrippedReversed(String phoneNumber) {
        String np = extractNetworkPortionAlt(phoneNumber);
        if (np == null) {
            return null;
        }
        return internalGetStrippedReversed(np, np.length());
    }

    private static String internalGetStrippedReversed(String np, int numDigits) {
        if (np == null) {
            return null;
        }
        StringBuilder ret = new StringBuilder(numDigits);
        int length = np.length();
        int i = length - 1;
        int s = length;
        while (i >= 0 && s - i <= numDigits) {
            ret.append(np.charAt(i));
            i--;
        }
        return ret.toString();
    }

    public static String stringFromStringAndTOA(String s, int TOA) {
        if (s == null) {
            return null;
        }
        if (TOA != 145 || s.length() <= 0 || s.charAt(0) == PLUS_SIGN_CHAR) {
            return s;
        }
        return PLUS_SIGN_STRING + s;
    }

    public static int toaFromString(String s) {
        if (s == null || s.length() <= 0 || s.charAt(0) != PLUS_SIGN_CHAR) {
            return 129;
        }
        return 145;
    }

    public static String calledPartyBCDToString(byte[] bytes, int offset, int length) {
        boolean prependPlus = false;
        StringBuilder ret = new StringBuilder((length * 2) + 1);
        if (length < 2) {
            return "";
        }
        if ((bytes[offset] & 240) == 144) {
            prependPlus = true;
        }
        internalCalledPartyBCDFragmentToString(ret, bytes, offset + 1, length - 1);
        if (prependPlus && ret.length() == 0) {
            return "";
        }
        if (prependPlus) {
            String retString = ret.toString();
            Matcher m = Pattern.compile("(^[#*])(.*)([#*])(.*)(#)$").matcher(retString);
            if (!m.matches()) {
                m = Pattern.compile("(^[#*])(.*)([#*])(.*)").matcher(retString);
                if (m.matches()) {
                    ret = new StringBuilder();
                    ret.append(m.group(1));
                    ret.append(m.group(2));
                    ret.append(m.group(3));
                    ret.append(PLUS_SIGN_STRING);
                    ret.append(m.group(4));
                } else {
                    ret = new StringBuilder();
                    ret.append(PLUS_SIGN_CHAR);
                    ret.append(retString);
                }
            } else if ("".equals(m.group(2))) {
                ret = new StringBuilder();
                ret.append(m.group(1));
                ret.append(m.group(3));
                ret.append(m.group(4));
                ret.append(m.group(5));
                ret.append(PLUS_SIGN_STRING);
            } else {
                ret = new StringBuilder();
                ret.append(m.group(1));
                ret.append(m.group(2));
                ret.append(m.group(3));
                ret.append(PLUS_SIGN_STRING);
                ret.append(m.group(4));
                ret.append(m.group(5));
            }
        }
        return ret.toString();
    }

    private static void internalCalledPartyBCDFragmentToString(StringBuilder sb, byte[] bytes, int offset, int length) {
        int i = offset;
        while (i < length + offset) {
            char c;
            if ("SKT".equals("EUR") || "KTT".equals("EUR") || "LGT".equals("EUR") || "KOO".equals("EUR")) {
                c = KorMsgbcdToChar((byte) (bytes[i] & 15));
            } else {
                c = bcdToChar((byte) (bytes[i] & 15));
            }
            if (c != '\u0000') {
                sb.append(c);
                byte b = (byte) ((bytes[i] >> 4) & 15);
                if (b != (byte) 15 || i + 1 != length + offset) {
                    if ("SKT".equals("EUR") || "KTT".equals("EUR") || "LGT".equals("EUR") || "KOO".equals("EUR")) {
                        c = KorMsgbcdToChar(b);
                    } else {
                        c = bcdToChar(b);
                    }
                    if (c != '\u0000') {
                        sb.append(c);
                        i++;
                    } else {
                        return;
                    }
                }
                return;
            }
            return;
        }
    }

    public static String calledPartyBCDFragmentToString(byte[] bytes, int offset, int length) {
        StringBuilder ret = new StringBuilder(length * 2);
        internalCalledPartyBCDFragmentToString(ret, bytes, offset, length);
        return ret.toString();
    }

    private static char bcdToChar(byte b) {
        if (b < (byte) 10) {
            return (char) (b + 48);
        }
        switch (b) {
            case (byte) 10:
                return '*';
            case (byte) 11:
                return '#';
            case (byte) 12:
                return ',';
            case (byte) 13:
                return WILD;
            default:
                return '\u0000';
        }
    }

    private static int charToBCD(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c == '*') {
            return 10;
        }
        if (c == '#') {
            return 11;
        }
        if (c == ',') {
            return 12;
        }
        if (c == WILD) {
            return 13;
        }
        if (c == ';') {
            return 14;
        }
        throw new RuntimeException("invalid char for BCD " + c);
    }

    private static char KorMsgbcdToChar(byte b) {
        if (b < (byte) 10) {
            return (char) (b + 48);
        }
        switch (b) {
            case (byte) 10:
                return '*';
            case (byte) 11:
                return '#';
            case (byte) 12:
                return DateFormat.AM_PM;
            case (byte) 13:
                return 'b';
            case (byte) 14:
                return 'c';
            default:
                return '\u0000';
        }
    }

    public static boolean isWellFormedSmsAddress(String address) {
        String networkPortion = extractNetworkPortion(address);
        return (networkPortion.equals(PLUS_SIGN_STRING) || TextUtils.isEmpty(networkPortion) || !isDialable(networkPortion)) ? false : true;
    }

    public static boolean isGlobalPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        }
        return GLOBAL_PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

    private static boolean isDialable(String address) {
        int count = address.length();
        for (int i = 0; i < count; i++) {
            if (!isDialable(address.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNonSeparator(String address) {
        int count = address.length();
        for (int i = 0; i < count; i++) {
            if (!isNonSeparator(address.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static byte[] networkPortionToCalledPartyBCD(String s) {
        return numberToCalledPartyBCDHelper(extractNetworkPortion(s), false);
    }

    public static byte[] DocomoNetworkPortionToCalledPartyBCD(String s) {
        return DocomoNumberToCalledPartyBCDHelper(extractNetworkPortion(s), false);
    }

    public static byte[] networkPortionToCalledPartyBCDWithLength(String s) {
        return numberToCalledPartyBCDHelper(extractNetworkPortion(s), true);
    }

    public static byte[] numberToCalledPartyBCD(String number) {
        return numberToCalledPartyBCDHelper(number, false);
    }

    private static byte[] numberToCalledPartyBCDHelper(String number, boolean includeLength) {
        int numberLenReal = number.length();
        int numberLenEffective = numberLenReal;
        boolean hasPlus = number.indexOf(43) != -1;
        if (hasPlus) {
            numberLenEffective--;
        }
        if (numberLenEffective == 0) {
            return null;
        }
        int resultLen = (numberLenEffective + 1) / 2;
        int extraBytes = 1;
        if (includeLength) {
            extraBytes = 1 + 1;
        }
        resultLen += extraBytes;
        byte[] result = new byte[resultLen];
        int digitCount = 0;
        for (int i = 0; i < numberLenReal; i++) {
            char c = number.charAt(i);
            if (c != PLUS_SIGN_CHAR) {
                int i2 = (digitCount >> 1) + extraBytes;
                result[i2] = (byte) (result[i2] | ((byte) ((charToBCD(c) & 15) << ((digitCount & 1) == 1 ? 4 : 0))));
                digitCount++;
            }
        }
        if ((digitCount & 1) == 1) {
            i2 = (digitCount >> 1) + extraBytes;
            result[i2] = (byte) (result[i2] | 240);
        }
        int offset = 0;
        if (includeLength) {
            int offset2 = 0 + 1;
            result[0] = (byte) (resultLen - 1);
            offset = offset2;
        }
        result[offset] = (byte) (hasPlus ? 145 : 129);
        return result;
    }

    private static byte[] DocomoNumberToCalledPartyBCDHelper(String number, boolean includeLength) {
        int numberLenReal = number.length();
        int numberLenEffective = numberLenReal;
        boolean hasPlus = number.indexOf(43) != -1;
        boolean hasSharp = number.indexOf(35) != -1;
        boolean hasStar = number.indexOf(42) != -1;
        if (hasPlus) {
            numberLenEffective--;
        }
        if (numberLenEffective == 0) {
            return null;
        }
        int resultLen = (numberLenEffective + 1) / 2;
        int extraBytes = 1;
        if (includeLength) {
            extraBytes = 1 + 1;
        }
        resultLen += extraBytes;
        byte[] result = new byte[resultLen];
        int digitCount = 0;
        for (int i = 0; i < numberLenReal; i++) {
            char c = number.charAt(i);
            if (c != PLUS_SIGN_CHAR) {
                int i2 = (digitCount >> 1) + extraBytes;
                result[i2] = (byte) (result[i2] | ((byte) ((charToBCD(c) & 15) << ((digitCount & 1) == 1 ? 4 : 0))));
                digitCount++;
            }
        }
        if ((digitCount & 1) == 1) {
            i2 = (digitCount >> 1) + extraBytes;
            result[i2] = (byte) (result[i2] | 240);
        }
        int offset = 0;
        if (includeLength) {
            int offset2 = 0 + 1;
            result[0] = (byte) (resultLen - 1);
            offset = offset2;
        }
        result[offset] = (byte) (hasPlus ? 145 : 129);
        if (hasSharp || hasStar) {
            result[offset] = (byte) (result[offset] & 240);
        }
        log("TOA: " + result[offset]);
        return result;
    }

    @Deprecated
    public static String formatNumber(String source) {
        Editable text = new SpannableStringBuilder(source);
        formatNumber(text, getFormatTypeForLocale(Locale.getDefault()));
        return text.toString();
    }

    @Deprecated
    public static String formatNumber(String source, int defaultFormattingType) {
        Editable text = new SpannableStringBuilder(source);
        formatNumber(text, defaultFormattingType);
        return text.toString();
    }

    @Deprecated
    public static int getFormatTypeForLocale(Locale locale) {
        return getFormatTypeFromCountryCode(locale.getCountry());
    }

    @Deprecated
    public static void formatNumber(Editable text, int defaultFormattingType) {
        int formatType = defaultFormattingType;
        if (text.length() > 2 && text.charAt(0) == PLUS_SIGN_CHAR) {
            formatType = text.charAt(1) == '1' ? 1 : (text.length() >= 3 && text.charAt(1) == '8' && text.charAt(2) == '1') ? 2 : (("SKT".equals("EUR") || "KTT".equals("EUR") || "LGT".equals("EUR") || "KOO".equals("EUR")) && text.length() >= 3 && text.charAt(1) == '8' && text.charAt(2) == '2') ? 82 : 0;
        }
        switch (formatType) {
            case 0:
                removeDashes(text);
                return;
            case 1:
                formatNanpNumber(text);
                return;
            case 2:
                formatJapaneseNumber(text);
                return;
            case 82:
                if ("SKT".equals("EUR") || "KTT".equals("EUR") || "LGT".equals("EUR") || "KOO".equals("EUR")) {
                    formatKRnpNumber(text);
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @java.lang.Deprecated
    public static void formatNanpNumber(android.text.Editable r15) {
        /*
        r14 = 2;
        r13 = 3;
        r12 = 0;
        r4 = r15.length();
        r11 = "+1-nnn-nnn-nnnn";
        r11 = r11.length();
        if (r4 <= r11) goto L_0x0010;
    L_0x000f:
        return;
    L_0x0010:
        r11 = 5;
        if (r4 <= r11) goto L_0x000f;
    L_0x0013:
        r9 = r15.subSequence(r12, r4);
        removeDashes(r15);
        r4 = r15.length();
        r1 = new int[r13];
        r5 = 0;
        r10 = 1;
        r7 = 0;
        r2 = 0;
        r6 = r5;
    L_0x0025:
        if (r2 >= r4) goto L_0x0061;
    L_0x0027:
        r0 = r15.charAt(r2);
        switch(r0) {
            case 43: goto L_0x005c;
            case 44: goto L_0x002e;
            case 45: goto L_0x0059;
            case 46: goto L_0x002e;
            case 47: goto L_0x002e;
            case 48: goto L_0x003c;
            case 49: goto L_0x0032;
            case 50: goto L_0x003c;
            case 51: goto L_0x003c;
            case 52: goto L_0x003c;
            case 53: goto L_0x003c;
            case 54: goto L_0x003c;
            case 55: goto L_0x003c;
            case 56: goto L_0x003c;
            case 57: goto L_0x003c;
            default: goto L_0x002e;
        };
    L_0x002e:
        r15.replace(r12, r4, r9);
        goto L_0x000f;
    L_0x0032:
        if (r7 == 0) goto L_0x0036;
    L_0x0034:
        if (r10 != r14) goto L_0x003c;
    L_0x0036:
        r10 = 3;
        r5 = r6;
    L_0x0038:
        r2 = r2 + 1;
        r6 = r5;
        goto L_0x0025;
    L_0x003c:
        if (r10 != r14) goto L_0x0042;
    L_0x003e:
        r15.replace(r12, r4, r9);
        goto L_0x000f;
    L_0x0042:
        if (r10 != r13) goto L_0x004c;
    L_0x0044:
        r5 = r6 + 1;
        r1[r6] = r2;
    L_0x0048:
        r10 = 1;
        r7 = r7 + 1;
        goto L_0x0038;
    L_0x004c:
        r11 = 4;
        if (r10 == r11) goto L_0x0091;
    L_0x004f:
        if (r7 == r13) goto L_0x0054;
    L_0x0051:
        r11 = 6;
        if (r7 != r11) goto L_0x0091;
    L_0x0054:
        r5 = r6 + 1;
        r1[r6] = r2;
        goto L_0x0048;
    L_0x0059:
        r10 = 4;
        r5 = r6;
        goto L_0x0038;
    L_0x005c:
        if (r2 != 0) goto L_0x002e;
    L_0x005e:
        r10 = 2;
        r5 = r6;
        goto L_0x0038;
    L_0x0061:
        r11 = 7;
        if (r7 != r11) goto L_0x008f;
    L_0x0064:
        r5 = r6 + -1;
    L_0x0066:
        r2 = 0;
    L_0x0067:
        if (r2 >= r5) goto L_0x0077;
    L_0x0069:
        r8 = r1[r2];
        r11 = r8 + r2;
        r12 = r8 + r2;
        r13 = "-";
        r15.replace(r11, r12, r13);
        r2 = r2 + 1;
        goto L_0x0067;
    L_0x0077:
        r3 = r15.length();
    L_0x007b:
        if (r3 <= 0) goto L_0x000f;
    L_0x007d:
        r11 = r3 + -1;
        r11 = r15.charAt(r11);
        r12 = 45;
        if (r11 != r12) goto L_0x000f;
    L_0x0087:
        r11 = r3 + -1;
        r15.delete(r11, r3);
        r3 = r3 + -1;
        goto L_0x007b;
    L_0x008f:
        r5 = r6;
        goto L_0x0066;
    L_0x0091:
        r5 = r6;
        goto L_0x0048;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.telephony.PhoneNumberUtils.formatNanpNumber(android.text.Editable):void");
    }

    @Deprecated
    public static void formatJapaneseNumber(Editable text) {
        JapanesePhoneNumberFormatter.format(text);
    }

    public static void formatKRnpNumber(Editable text) {
        int length = text.length();
        if (text.toString().replace(NativeLibraryHelper.CLEAR_ABI_OVERRIDE, "").length() > 12) {
            removeDashes(text);
        } else if (length >= 2) {
            String Digits = text.toString();
            int p;
            if (length >= 6 || !Digits.endsWith(NativeLibraryHelper.CLEAR_ABI_OVERRIDE)) {
                p = 0;
                while (p < text.length()) {
                    if (text.charAt(p) == ' ' || text.charAt(p) == '/') {
                        text.delete(p, p + 1);
                    } else {
                        p++;
                    }
                }
                if (length != text.length()) {
                    length = text.length();
                }
                if (length >= 1) {
                    int state;
                    if (text.charAt(0) == '0') {
                        if (length < 2) {
                            return;
                        }
                        if (text.charAt(1) == '2') {
                            state = 6;
                        } else if (length < 3) {
                            return;
                        } else {
                            if (Digits.startsWith("0505") || Digits.startsWith("050-5")) {
                                state = 14;
                            } else {
                                state = 7;
                            }
                        }
                    } else if (text.charAt(0) == '*') {
                        if (length < 4) {
                            return;
                        }
                        if (Digits.startsWith("*23#") || Digits.startsWith("*22#") || Digits.startsWith(CLIR_ON)) {
                            if (length <= 5) {
                                state = 10;
                            } else {
                                return;
                            }
                        } else if (Digits.startsWith("*230#") && length <= 6) {
                            state = 11;
                        } else {
                            return;
                        }
                    } else if (text.charAt(0) == '#') {
                        if (length < 2) {
                            return;
                        }
                        if (text.charAt(1) == '9') {
                            if (length <= 3) {
                                state = 8;
                            } else {
                                return;
                            }
                        } else if (Digits.startsWith(CLIR_OFF) && length <= 5) {
                            state = 10;
                        } else {
                            return;
                        }
                    } else if (text.charAt(0) == PLUS_SIGN_CHAR) {
                        if (length >= 6 && length <= 14) {
                            state = 9;
                        } else {
                            return;
                        }
                    } else if (length >= 5 && length <= 14) {
                        state = 5;
                    } else {
                        return;
                    }
                    CharSequence saved = text.subSequence(0, length);
                    p = 0;
                    while (p < text.length()) {
                        if (text.charAt(p) == '-') {
                            text.delete(p, p + 1);
                        } else {
                            p++;
                        }
                    }
                    length = text.length();
                    if (text.toString().equals("3003003000")) {
                        text.replace(0, length, "300-300-3000");
                        return;
                    }
                    int[] dashPositions = new int[2];
                    int numDashes = 0;
                    switch (state) {
                        case 5:
                            if (length > 3) {
                                if (length > 7) {
                                    if (length > 7) {
                                        dashPositions[0] = 4;
                                        numDashes = 1;
                                        break;
                                    }
                                }
                                dashPositions[0] = 3;
                                numDashes = 1;
                                break;
                            }
                            numDashes = 0;
                            break;
                            break;
                        case 6:
                            if (length > 2) {
                                if (length > 6) {
                                    if (length <= 6 || length > 9) {
                                        if (length > 9) {
                                            dashPositions[0] = 2;
                                            dashPositions[1] = 6;
                                            numDashes = 2;
                                            break;
                                        }
                                    }
                                    dashPositions[0] = 2;
                                    dashPositions[1] = length - 4;
                                    numDashes = 2;
                                    break;
                                }
                                dashPositions[0] = 2;
                                numDashes = 1;
                                break;
                            }
                            numDashes = 0;
                            break;
                            break;
                        case 7:
                            if (length > 3) {
                                if (length > 7) {
                                    if (length <= 7 || length > 10) {
                                        if (length > 10) {
                                            dashPositions[0] = 3;
                                            dashPositions[1] = 7;
                                            numDashes = 2;
                                            break;
                                        }
                                    }
                                    dashPositions[0] = 3;
                                    dashPositions[1] = length - 4;
                                    numDashes = 2;
                                    break;
                                }
                                dashPositions[0] = 3;
                                numDashes = 1;
                                break;
                            }
                            numDashes = 0;
                            break;
                            break;
                        case 8:
                            if (length > 2) {
                                dashPositions[0] = 2;
                                numDashes = 1;
                                break;
                            }
                            numDashes = 0;
                            break;
                        case 9:
                            if (length > 8) {
                                if (length > 8) {
                                    dashPositions[0] = 4;
                                    numDashes = 1;
                                    break;
                                }
                            }
                            dashPositions[0] = length - 4;
                            numDashes = 1;
                            break;
                            break;
                        case 10:
                            if (length > 4) {
                                dashPositions[0] = 4;
                                numDashes = 1;
                                break;
                            }
                            numDashes = 0;
                            break;
                        case 11:
                            if (length > 5) {
                                dashPositions[0] = 5;
                                numDashes = 1;
                                break;
                            }
                            numDashes = 0;
                            break;
                        case 14:
                            if (length > 4) {
                                if (length > 8) {
                                    if (length <= 8 || length > 11) {
                                        if (length > 11) {
                                            dashPositions[0] = 4;
                                            dashPositions[1] = 8;
                                            numDashes = 2;
                                            break;
                                        }
                                    }
                                    dashPositions[0] = 4;
                                    dashPositions[1] = length - 4;
                                    numDashes = 2;
                                    break;
                                }
                                dashPositions[0] = 4;
                                numDashes = 1;
                                break;
                            }
                            numDashes = 0;
                            break;
                            break;
                        default:
                            text.replace(0, length, saved);
                            return;
                    }
                    if (numDashes != 0) {
                        int i = 0;
                        while (i < numDashes) {
                            int pos = dashPositions[i];
                            if (pos + i >= 0 && pos + i <= length) {
                                text.replace(pos + i, pos + i, NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                            }
                            i++;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            p = 0;
            while (p < text.length()) {
                if (text.charAt(p) == '-') {
                    text.delete(p, p + 1);
                } else {
                    p++;
                }
            }
        }
    }

    private static void removeDashes(Editable text) {
        int p = 0;
        while (p < text.length()) {
            if (text.charAt(p) == '-') {
                text.delete(p, p + 1);
            } else {
                p++;
            }
        }
    }

    public static String formatNumberToE164(String phoneNumber, String defaultCountryIso) {
        return formatNumberInternal(phoneNumber, defaultCountryIso, PhoneNumberFormat.E164);
    }

    public static String formatNumberToRFC3966(String phoneNumber, String defaultCountryIso) {
        return formatNumberInternal(phoneNumber, defaultCountryIso, PhoneNumberFormat.RFC3966);
    }

    private static String formatNumberInternal(String rawPhoneNumber, String defaultCountryIso, PhoneNumberFormat formatIdentifier) {
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber phoneNumber = util.parse(rawPhoneNumber, defaultCountryIso);
            if (util.isValidNumber(phoneNumber)) {
                return util.format(phoneNumber, formatIdentifier);
            }
        } catch (NumberParseException e) {
        }
        return null;
    }

    public static String formatNumber(String phoneNumber, String defaultCountryIso) {
        if (phoneNumber.startsWith("#") || phoneNumber.startsWith(PhoneConstants.APN_TYPE_ALL)) {
            return phoneNumber;
        }
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        PhoneNumber pn;
        if (!"SKT".equals("EUR") && !"KTT".equals("EUR") && !"LGT".equals("EUR") && !"KOO".equals("EUR")) {
            try {
                pn = util.parseAndKeepRawInput(phoneNumber, defaultCountryIso);
                if (mConfigNetworkTypeCapability != null && ((mConfigNetworkTypeCapability.indexOf("-GLB-USA") >= 0 || mConfigNetworkTypeCapability.indexOf("-CDM-USA") >= 0) && !"US".equals(defaultCountryIso))) {
                    log("formatNumber - ISO: " + defaultCountryIso);
                }
                return util.formatInOriginalFormat(pn, defaultCountryIso);
            } catch (NumberParseException e) {
                return null;
            }
        } else if (!phoneNumber.startsWith(PLUS_SIGN_STRING)) {
            return formatNumber(phoneNumber, getFormatTypeForLocale(Locale.getDefault()));
        } else {
            try {
                pn = util.parseAndKeepRawInput(phoneNumber, defaultCountryIso);
                if (KOREA_ISO_COUNTRY_CODE.equals(defaultCountryIso) && pn.getCountryCode() == util.getCountryCodeForRegion(KOREA_ISO_COUNTRY_CODE) && pn.getCountryCodeSource() == CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN) {
                    return util.format(pn, PhoneNumberFormat.NATIONAL);
                }
                return util.formatInOriginalFormat(pn, defaultCountryIso);
            } catch (NumberParseException e2) {
                return null;
            }
        }
    }

    public static String formatNumber(String phoneNumber, String phoneNumberE164, String defaultCountryIso) {
        int len = phoneNumber.length();
        for (int i = 0; i < len; i++) {
            if (!isDialable(phoneNumber.charAt(i))) {
                return phoneNumber;
            }
        }
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        if (phoneNumberE164 != null && phoneNumberE164.length() >= 2 && phoneNumberE164.charAt(0) == PLUS_SIGN_CHAR) {
            try {
                String regionCode = util.getRegionCodeForNumber(util.parse(phoneNumberE164, "ZZ"));
                if (!TextUtils.isEmpty(regionCode) && normalizeNumber(phoneNumber).indexOf(phoneNumberE164.substring(1)) <= 0) {
                    defaultCountryIso = regionCode;
                }
            } catch (NumberParseException e) {
            }
        }
        String result = formatNumber(phoneNumber, defaultCountryIso);
        if (result == null) {
            result = phoneNumber;
        }
        return result;
    }

    public static String normalizeNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int len = phoneNumber.length();
        for (int i = 0; i < len; i++) {
            char c = phoneNumber.charAt(i);
            int digit = Character.digit(c, 10);
            if (digit != -1) {
                sb.append(digit);
            } else if ((sb.length() == 0 && c == PLUS_SIGN_CHAR) || c == '*' || c == '#') {
                sb.append(c);
            } else if ((c >= DateFormat.AM_PM && c <= DateFormat.TIME_ZONE) || (c >= DateFormat.CAPITAL_AM_PM && c <= 'Z')) {
                return normalizeNumber(convertKeypadLettersToDigits(phoneNumber));
            }
        }
        return sb.toString();
    }

    public static String replaceUnicodeDigits(String number) {
        StringBuilder normalizedDigits = new StringBuilder(number.length());
        for (char c : number.toCharArray()) {
            int digit = Character.digit(c, 10);
            if (digit != -1) {
                normalizedDigits.append(digit);
            } else {
                normalizedDigits.append(c);
            }
        }
        return normalizedDigits.toString();
    }

    public static boolean isEmergencyNumber(String number) {
        return isEmergencyNumber(getDefaultVoiceSubId(), number);
    }

    public static boolean isEmergencyNumber(int subId, String number) {
        return isEmergencyNumberInternal(subId, number, true);
    }

    public static boolean isPotentialEmergencyNumber(String number) {
        return isPotentialEmergencyNumber(getDefaultVoiceSubId(), number);
    }

    public static boolean isPotentialEmergencyNumber(int subId, String number) {
        return isEmergencyNumberInternal(subId, number, false);
    }

    private static boolean isEmergencyNumberInternal(String number, boolean useExactMatch) {
        return isEmergencyNumberInternal(getDefaultVoiceSubId(), number, useExactMatch);
    }

    private static boolean isEmergencyNumberInternal(int subId, String number, boolean useExactMatch) {
        return isEmergencyNumberInternal(subId, number, null, useExactMatch);
    }

    public static boolean isEmergencyNumber(String number, String defaultCountryIso) {
        return isEmergencyNumber(getDefaultVoiceSubId(), number, defaultCountryIso);
    }

    public static boolean isEmergencyNumber(int subId, String number, String defaultCountryIso) {
        return isEmergencyNumberInternal(subId, number, defaultCountryIso, true);
    }

    public static boolean isPotentialEmergencyNumber(String number, String defaultCountryIso) {
        return isPotentialEmergencyNumber(getDefaultVoiceSubId(), number, defaultCountryIso);
    }

    public static boolean isPotentialEmergencyNumber(int subId, String number, String defaultCountryIso) {
        return isEmergencyNumberInternal(subId, number, defaultCountryIso, false);
    }

    private static boolean isEmergencyNumberInternal(String number, String defaultCountryIso, boolean useExactMatch) {
        return isEmergencyNumberInternal(getDefaultVoiceSubId(), number, defaultCountryIso, useExactMatch);
    }

    private static boolean isEmergencyNumberInternal(int subId, String number, String defaultCountryIso, boolean useExactMatch) {
        if (number == null || isUriNumber(number)) {
            return false;
        }
        number = extractNetworkPortionAlt(number);
        String str = LOG_TAG;
        StringBuilder append = new StringBuilder().append("subId:").append(subId).append(", defaultCountryIso:");
        if (defaultCountryIso == null) {
            defaultCountryIso = "NULL";
        }
        Rlog.d(str, append.append(defaultCountryIso).toString());
        String cat;
        if ("SKT".equals("EUR") || "KTT".equals("EUR") || "LGT".equals("EUR") || "KOO".equals("EUR")) {
            cat = getEmergencyServiceCategoryforkor(subId, number);
            log("isEmergencyNumber cat = " + cat);
            if (cat != null) {
                return true;
            }
            return false;
        }
        cat = getEmergencyServiceCategory(subId, number);
        log("isEmergencyNumber cat = " + cat);
        if (cat != null) {
            return true;
        }
        return false;
    }

    public static boolean isLocalEmergencyNumber(Context context, String number) {
        return isLocalEmergencyNumber(context, getDefaultVoiceSubId(), number);
    }

    public static boolean isLocalEmergencyNumber(Context context, int subId, String number) {
        return isLocalEmergencyNumberInternal(subId, number, context, true);
    }

    public static boolean isPotentialLocalEmergencyNumber(Context context, String number) {
        return isPotentialLocalEmergencyNumber(context, getDefaultVoiceSubId(), number);
    }

    public static boolean isPotentialLocalEmergencyNumber(Context context, int subId, String number) {
        return isLocalEmergencyNumberInternal(subId, number, context, false);
    }

    private static boolean isLocalEmergencyNumberInternal(String number, Context context, boolean useExactMatch) {
        return isLocalEmergencyNumberInternal(getDefaultVoiceSubId(), number, context, useExactMatch);
    }

    private static boolean isLocalEmergencyNumberInternal(int subId, String number, Context context, boolean useExactMatch) {
        String countryIso;
        CountryDetector detector = (CountryDetector) context.getSystemService("country_detector");
        if (detector == null || detector.detectCountry() == null) {
            countryIso = context.getResources().getConfiguration().locale.getCountry();
            Rlog.w(LOG_TAG, "No CountryDetector; falling back to countryIso based on locale: " + countryIso);
        } else {
            countryIso = detector.detectCountry().getCountryIso();
        }
        return isEmergencyNumberInternal(subId, number, countryIso, useExactMatch);
    }

    public static boolean isVoiceMailNumber(String number) {
        return isVoiceMailNumber(SubscriptionManager.getDefaultSubId(), number);
    }

    public static boolean isVoiceMailNumber(int subId, String number) {
        return isVoiceMailNumber(null, subId, number);
    }

    public static boolean isVoiceMailNumber(Context context, int subId, String number) {
        TelephonyManager tm;
        if (context == null) {
            try {
                tm = TelephonyManager.getDefault();
            } catch (SecurityException e) {
                return false;
            }
        }
        tm = TelephonyManager.from(context);
        String vmNumber = tm.getVoiceMailNumber(subId);
        if (mConfigNetworkTypeCapability != null && mConfigNetworkTypeCapability.indexOf("-GLB-USA") >= 0 && mConfigNetworkTypeCapability.startsWith("VZW-") && "*86".equals(number)) {
            return true;
        }
        number = extractNetworkPortionAlt(number);
        if (TextUtils.isEmpty(number) || !compare(number, vmNumber)) {
            return false;
        }
        return true;
    }

    public static String convertKeypadLettersToDigits(String input) {
        if (input == null) {
            return input;
        }
        int len = input.length();
        if (len == 0) {
            return input;
        }
        char[] out = input.toCharArray();
        for (int i = 0; i < len; i++) {
            char c = out[i];
            out[i] = (char) KEYPAD_MAP.get(c, c);
        }
        return new String(out);
    }

    public static String cdmaCheckAndProcessPlusCode(String dialStr) {
        if (TextUtils.isEmpty(dialStr) || !isReallyDialable(dialStr.charAt(0)) || !isNonSeparator(dialStr)) {
            return dialStr;
        }
        String currIso = TelephonyManager.getDefault().getNetworkCountryIso();
        String defaultIso = TelephonyManager.getDefault().getSimCountryIso();
        if (mConfigNetworkTypeCapability != null && mConfigNetworkTypeCapability.startsWith("VZW-")) {
            String assistedDialingTest = SystemProperties.get(TelephonyProperties.PROPERTY_TEST_ASSISTEDDIAL, SmartFaceManager.FALSE);
            if (!SmartFaceManager.FALSE.equals(assistedDialingTest)) {
                String iso = "";
                String[] assistedDialingTestParam = assistedDialingTest.split(":");
                if (assistedDialingTestParam.length > 2) {
                    iso = "us";
                    if ("234".equals(assistedDialingTestParam[2])) {
                        iso = "gb";
                    } else if ("460".equals(assistedDialingTestParam[2])) {
                        iso = "cn";
                    }
                }
                Rlog.d(LOG_TAG, "Assited Dialing Testmode - currIso: " + currIso + " -> " + iso + ", defaultIso: " + defaultIso + " -> " + iso);
                currIso = iso;
                defaultIso = iso;
            }
        }
        if (TextUtils.isEmpty(currIso) || TextUtils.isEmpty(defaultIso)) {
            return dialStr;
        }
        return cdmaCheckAndProcessPlusCodeByNumberFormat(dialStr, getFormatTypeFromCountryCode(currIso), getFormatTypeFromCountryCode(defaultIso));
    }

    public static String cdmaCheckAndProcessPlusCode(String dialStr, Context context) {
        if (TextUtils.isEmpty(dialStr) || !isReallyDialable(dialStr.charAt(0)) || !isNonSeparator(dialStr)) {
            return dialStr;
        }
        String currIso = TelephonyManager.getDefault().getNetworkCountryIso();
        String defaultIso = TelephonyManager.getDefault().getSimCountryIso();
        if (TextUtils.isEmpty(currIso) || TextUtils.isEmpty(defaultIso)) {
            return dialStr;
        }
        return cdmaCheckAndProcessPlusCodeByNumberFormat(dialStr, getFormatTypeFromCountryCode(currIso), getFormatTypeFromCountryCode(defaultIso), context);
    }

    public static String cdmaCheckAndProcessPlusCodeForSms(String dialStr) {
        if (TextUtils.isEmpty(dialStr) || !isReallyDialable(dialStr.charAt(0)) || !isNonSeparator(dialStr)) {
            return dialStr;
        }
        String defaultIso = TelephonyManager.getDefault().getSimCountryIso();
        if (TextUtils.isEmpty(defaultIso)) {
            return dialStr;
        }
        int format = getFormatTypeFromCountryCode(defaultIso);
        return cdmaCheckAndProcessPlusCodeByNumberFormat(dialStr, format, format);
    }

    public static String cdmaCheckAndProcessPlusCodeByNumberFormat(String dialStr, int currFormat, int defaultFormat) {
        String retStr = dialStr;
        boolean useNanp = currFormat == defaultFormat && currFormat == 1;
        if (dialStr != null && dialStr.lastIndexOf(PLUS_SIGN_STRING) != -1) {
            String tempDialStr = dialStr;
            retStr = null;
            do {
                String networkDialStr;
                if (useNanp) {
                    networkDialStr = extractNetworkPortion(tempDialStr);
                } else {
                    networkDialStr = extractNetworkPortionAlt(tempDialStr);
                }
                String salesCode = SystemProperties.get("ro.csc.sales_code");
                if (!("CTC".equals(salesCode) || "KDI".equals(salesCode) || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportAllRat") || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_Support75Mode") || ("LGT".equals(salesCode) && "domestic".equals(SystemProperties.get("ril.currentplmn", "domestic")) && networkDialStr.startsWith("+82"))) || useNanp) {
                    networkDialStr = processPlusCode(networkDialStr, useNanp);
                }
                if (!TextUtils.isEmpty(networkDialStr)) {
                    if (retStr == null) {
                        retStr = networkDialStr;
                    } else {
                        retStr = retStr.concat(networkDialStr);
                    }
                    String postDialStr = extractPostDialPortion(tempDialStr);
                    if (!TextUtils.isEmpty(postDialStr)) {
                        int dialableIndex = findDialableIndexFromPostDialStr(postDialStr);
                        if (dialableIndex >= 1) {
                            retStr = appendPwCharBackToOrigDialStr(dialableIndex, retStr, postDialStr);
                            tempDialStr = postDialStr.substring(dialableIndex);
                        } else {
                            if (dialableIndex < 0) {
                                postDialStr = "";
                            }
                            Rlog.e("wrong postDialStr=", postDialStr);
                        }
                    }
                    if (TextUtils.isEmpty(postDialStr)) {
                        break;
                    }
                } else {
                    Rlog.e("checkAndProcessPlusCode: null newDialStr", networkDialStr);
                    return dialStr;
                }
            } while (!TextUtils.isEmpty(tempDialStr));
        }
        return retStr;
    }

    public static CharSequence createTtsSpannable(CharSequence phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        CharSequence spannable = Factory.getInstance().newSpannable(phoneNumber);
        addTtsSpan(spannable, 0, spannable.length());
        return spannable;
    }

    public static void addTtsSpan(Spannable s, int start, int endExclusive) {
        s.setSpan(createTtsSpan(s.subSequence(start, endExclusive).toString()), start, endExclusive, 33);
    }

    @Deprecated
    public static CharSequence ttsSpanAsPhoneNumber(CharSequence phoneNumber) {
        return createTtsSpannable(phoneNumber);
    }

    @Deprecated
    public static void ttsSpanAsPhoneNumber(Spannable s, int start, int end) {
        addTtsSpan(s, start, end);
    }

    public static TtsSpan createTtsSpan(String phoneNumberString) {
        if (phoneNumberString == null) {
            return null;
        }
        PhoneNumber phoneNumber = null;
        try {
            phoneNumber = PhoneNumberUtil.getInstance().parse(phoneNumberString, null);
        } catch (NumberParseException e) {
        }
        TelephoneBuilder builder = new TelephoneBuilder();
        if (phoneNumber == null) {
            builder.setNumberParts(splitAtNonNumerics(phoneNumberString));
        } else {
            if (phoneNumber.hasCountryCode()) {
                builder.setCountryCode(Integer.toString(phoneNumber.getCountryCode()));
            }
            builder.setNumberParts(Long.toString(phoneNumber.getNationalNumber()));
        }
        return builder.build();
    }

    private static String splitAtNonNumerics(CharSequence number) {
        StringBuilder sb = new StringBuilder(number.length());
        for (int i = 0; i < number.length(); i++) {
            sb.append(isISODigit(number.charAt(i)) ? Character.valueOf(number.charAt(i)) : " ");
        }
        return sb.toString().replaceAll(" +", " ").trim();
    }

    public static String cdmaCheckAndProcessPlusCodeByNumberFormat(String dialStr, int currFormat, int defaultFormat, Context context) {
        boolean useNanp;
        int i = 0;
        String retStr = dialStr;
        if (currFormat == defaultFormat && currFormat == 1) {
            useNanp = true;
        } else {
            useNanp = false;
        }
        if (mConfigNetworkTypeCapability != null && mConfigNetworkTypeCapability.startsWith("SPR-")) {
            i = 1;
        }
        useNanp |= i;
        if (dialStr != null && dialStr.lastIndexOf(PLUS_SIGN_STRING) != -1) {
            String tempDialStr = dialStr;
            retStr = null;
            do {
                String networkDialStr;
                if (useNanp) {
                    networkDialStr = extractNetworkPortion(tempDialStr);
                } else {
                    networkDialStr = extractNetworkPortionAlt(tempDialStr);
                }
                String salesCode = SystemProperties.get("ro.csc.sales_code");
                if (!("CTC".equals(salesCode) || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportAllRat") || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_Support75Mode") || ("LGT".equals(salesCode) && "domestic".equals(SystemProperties.get("ril.currentplmn", "domestic")) && networkDialStr.startsWith("+82"))) || useNanp) {
                    networkDialStr = processPlusCode(networkDialStr, useNanp, context);
                }
                if (!TextUtils.isEmpty(networkDialStr)) {
                    if (retStr == null) {
                        retStr = networkDialStr;
                    } else {
                        retStr = retStr.concat(networkDialStr);
                    }
                    String postDialStr = extractPostDialPortion(tempDialStr);
                    if (!TextUtils.isEmpty(postDialStr)) {
                        int dialableIndex = findDialableIndexFromPostDialStr(postDialStr);
                        if (dialableIndex >= 1) {
                            retStr = appendPwCharBackToOrigDialStr(dialableIndex, retStr, postDialStr);
                            tempDialStr = postDialStr.substring(dialableIndex);
                        } else {
                            if (dialableIndex < 0) {
                                postDialStr = "";
                            }
                            Rlog.e("wrong postDialStr=", postDialStr);
                        }
                    }
                    if (TextUtils.isEmpty(postDialStr)) {
                        break;
                    }
                } else {
                    Rlog.e("checkAndProcessPlusCode: null newDialStr", networkDialStr);
                    return dialStr;
                }
            } while (!TextUtils.isEmpty(tempDialStr));
        }
        return retStr;
    }

    private static String getCurrentIdp(boolean useNanp) {
        if (useNanp) {
            return "011";
        }
        return SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_IDP_STRING, PLUS_SIGN_STRING);
    }

    private static boolean isTwoToNine(char c) {
        if (c < '2' || c > '9') {
            return false;
        }
        return true;
    }

    private static int getFormatTypeFromCountryCode(String country) {
        if (("SKT".equals("EUR") || "KTT".equals("EUR") || "LGT".equals("EUR") || "KOO".equals("EUR")) && KOREA_ISO_COUNTRY_CODE.compareToIgnoreCase(country) == 0) {
            return 82;
        }
        for (String compareToIgnoreCase : NANP_COUNTRIES) {
            if (compareToIgnoreCase.compareToIgnoreCase(country) == 0) {
                return 1;
            }
        }
        if ("jp".compareToIgnoreCase(country) == 0) {
            return 2;
        }
        return 0;
    }

    public static boolean isNanpCountry(String country) {
        for (String compareToIgnoreCase : NANP_COUNTRIES) {
            if (compareToIgnoreCase.compareToIgnoreCase(country) == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNanp(String dialStr) {
        if (dialStr == null) {
            Rlog.e("isNanp: null dialStr passed in", dialStr);
            return false;
        } else if (dialStr.length() != 10 || !isTwoToNine(dialStr.charAt(0)) || !isTwoToNine(dialStr.charAt(3))) {
            return false;
        } else {
            for (int i = 1; i < 10; i++) {
                if (!isISODigit(dialStr.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    private static boolean isOneNanp(String dialStr) {
        if (dialStr != null) {
            String newDialStr = dialStr.substring(1);
            if (dialStr.charAt(0) == '1' && isNanp(newDialStr)) {
                return true;
            }
            return false;
        }
        Rlog.e("isOneNanp: null dialStr passed in", dialStr);
        return false;
    }

    public static boolean isUriNumber(String number) {
        return number != null && (number.contains("@") || number.contains("%40"));
    }

    public static String getUsernameFromUriNumber(String number) {
        int delimiterIndex = number.indexOf(64);
        if (delimiterIndex < 0) {
            delimiterIndex = number.indexOf("%40");
        }
        if (delimiterIndex < 0) {
            Rlog.w(LOG_TAG, "getUsernameFromUriNumber: no delimiter found in SIP addr '" + number + "'");
            delimiterIndex = number.length();
        }
        return number.substring(0, delimiterIndex);
    }

    private static String processPlusCode(String networkDialStr, boolean useNanp) {
        String retStr = networkDialStr;
        if (networkDialStr == null || networkDialStr.charAt(0) != PLUS_SIGN_CHAR || networkDialStr.length() <= 1) {
            return retStr;
        }
        String newStr = networkDialStr.substring(1);
        if (useNanp && isOneNanp(newStr)) {
            retStr = newStr;
            log("processPlusCode - Remove the leading plus sign");
            return retStr;
        }
        retStr = networkDialStr.replaceFirst("[+]", getCurrentIdp(useNanp));
        log("processPlusCode - Replaces the plus sign with the default IDP (useNanp: " + useNanp + ", current IDP: " + getCurrentIdp(useNanp) + ")");
        return retStr;
    }

    private static String processPlusCode(String networkDialStr, boolean useNanp, Context context) {
        String retStr = networkDialStr;
        if (networkDialStr == null || networkDialStr.charAt(0) != PLUS_SIGN_CHAR || networkDialStr.length() <= 1) {
            return retStr;
        }
        String newStr = networkDialStr.substring(1);
        if (mConfigNetworkTypeCapability != null && mConfigNetworkTypeCapability.startsWith("SPR-")) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            boolean isUSDialingValue = sp.getBoolean("toggle_country_name", true);
            if (isOneNanp(newStr) && isUSDialingValue) {
                String iso = "";
                if (mConfigNetworkTypeCapability != null && mConfigNetworkTypeCapability.startsWith("SPR-")) {
                    iso = SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_ISO_COUNTRY_REAL, "");
                }
                if (TextUtils.isEmpty(iso)) {
                    iso = TelephonyManager.getDefault().getNetworkCountryIso();
                }
                String simIso = TelephonyManager.getDefault().getSimCountryIso();
                Rlog.d(LOG_TAG, "ISO: " + iso + ", SIM ISO: " + simIso);
                if (networkDialStr.length() <= 2 || getFormatTypeFromCountryCode(iso) != 1 || getFormatTypeFromCountryCode(simIso) != 1) {
                    return retStr;
                }
                retStr = networkDialStr.substring(2);
                log("processPlusCode - Remove the leading plus sign and 1");
                return retStr;
            }
            String nanpIDPString = sp.getString(Settings$System.COUNTRY_CODE, "011");
            retStr = networkDialStr.replaceFirst("[+]", nanpIDPString);
            log("processPlusCode - Replaces the plus sign with the NANP IDP (NANP IDP: " + nanpIDPString + ")");
            return retStr;
        } else if (useNanp && isOneNanp(newStr)) {
            retStr = newStr;
            log("processPlusCode - Remove the leading plus sign");
            return retStr;
        } else {
            retStr = networkDialStr.replaceFirst("[+]", getCurrentIdp(useNanp));
            log("processPlusCode - Replaces the plus sign with the default IDP (useNanp: " + useNanp + ", current IDP: " + getCurrentIdp(useNanp) + ")");
            return retStr;
        }
    }

    private static int findDialableIndexFromPostDialStr(String postDialStr) {
        for (int index = 0; index < postDialStr.length(); index++) {
            if (isReallyDialable(postDialStr.charAt(index))) {
                return index;
            }
        }
        return -1;
    }

    private static String appendPwCharBackToOrigDialStr(int dialableIndex, String origStr, String dialStr) {
        if (dialableIndex == 1) {
            return dialStr.charAt(0);
        }
        return origStr.concat(dialStr.substring(0, dialableIndex));
    }

    private static boolean matchIntlPrefix(String a, int len) {
        int state = 0;
        for (int i = 0; i < len; i++) {
            char c = a.charAt(i);
            switch (state) {
                case 0:
                    if (c != PLUS_SIGN_CHAR) {
                        if (c != '0') {
                            if (!isNonSeparator(c)) {
                                break;
                            }
                            return false;
                        }
                        state = 2;
                        break;
                    }
                    state = 1;
                    break;
                case 2:
                    if (c != '0') {
                        if (c != '1') {
                            if (!isNonSeparator(c)) {
                                break;
                            }
                            return false;
                        }
                        state = 4;
                        break;
                    }
                    state = 3;
                    break;
                case 4:
                    if (c != '1') {
                        if (!isNonSeparator(c)) {
                            break;
                        }
                        return false;
                    }
                    state = 5;
                    break;
                default:
                    if (!isNonSeparator(c)) {
                        break;
                    }
                    return false;
            }
        }
        if (state == 1 || state == 3 || state == 5) {
            return true;
        }
        return false;
    }

    private static boolean matchIntlPrefixAndCC(String a, int len) {
        int state = 0;
        for (int i = 0; i < len; i++) {
            char c = a.charAt(i);
            switch (state) {
                case 0:
                    if (c != PLUS_SIGN_CHAR) {
                        if (c != '0') {
                            if (!isNonSeparator(c)) {
                                break;
                            }
                            return false;
                        }
                        state = 2;
                        break;
                    }
                    state = 1;
                    break;
                case 1:
                case 3:
                case 5:
                    if (!isISODigit(c)) {
                        if (!isNonSeparator(c)) {
                            break;
                        }
                        return false;
                    }
                    state = 6;
                    break;
                case 2:
                    if (c != '0') {
                        if (c != '1') {
                            if (!isNonSeparator(c)) {
                                break;
                            }
                            return false;
                        }
                        state = 4;
                        break;
                    }
                    state = 3;
                    break;
                case 4:
                    if (c != '1') {
                        if (!isNonSeparator(c)) {
                            break;
                        }
                        return false;
                    }
                    state = 5;
                    break;
                case 6:
                case 7:
                    if (!isISODigit(c)) {
                        if (!isNonSeparator(c)) {
                            break;
                        }
                        return false;
                    }
                    state++;
                    break;
                default:
                    if (!isNonSeparator(c)) {
                        break;
                    }
                    return false;
            }
        }
        if (state == 6 || state == 7 || state == 8) {
            return true;
        }
        return false;
    }

    private static boolean matchTrunkPrefix(String a, int len) {
        boolean found = false;
        for (int i = 0; i < len; i++) {
            char c = a.charAt(i);
            if (c == '0' && !found) {
                found = true;
            } else if (isNonSeparator(c)) {
                return false;
            }
        }
        return found;
    }

    private static boolean isCountryCallingCode(int countryCallingCodeCandidate) {
        return countryCallingCodeCandidate > 0 && countryCallingCodeCandidate < CCC_LENGTH && COUNTRY_CALLING_CALL[countryCallingCodeCandidate];
    }

    private static int tryGetISODigit(char ch) {
        if ('0' > ch || ch > '9') {
            return -1;
        }
        return ch - 48;
    }

    private static CountryCallingCodeAndNewIndex tryGetCountryCallingCodeAndNewIndex(String str, boolean acceptThailandCase) {
        int state = 0;
        int ccc = 0;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char ch = str.charAt(i);
            switch (state) {
                case 0:
                    if (ch != PLUS_SIGN_CHAR) {
                        if (ch != '0') {
                            if (ch != '1') {
                                if (!isDialable(ch)) {
                                    break;
                                }
                                return null;
                            } else if (acceptThailandCase) {
                                state = 8;
                                break;
                            } else {
                                return null;
                            }
                        }
                        state = 2;
                        break;
                    }
                    state = 1;
                    break;
                case 1:
                case 3:
                case 5:
                case 6:
                case 7:
                    int ret = tryGetISODigit(ch);
                    if (ret <= 0 && (ret != 0 || ccc >= 10)) {
                        if (!isDialable(ch)) {
                            break;
                        }
                        return null;
                    }
                    ccc = (ccc * 10) + ret;
                    if (ccc < 100 && !isCountryCallingCode(ccc)) {
                        if (state != 1 && state != 3 && state != 5) {
                            state++;
                            break;
                        }
                        state = 6;
                        break;
                    }
                    return new CountryCallingCodeAndNewIndex(ccc, i + 1);
                    break;
                case 2:
                    if (ch != '0') {
                        if (ch != '1') {
                            if (!isDialable(ch)) {
                                break;
                            }
                            return null;
                        }
                        state = 4;
                        break;
                    }
                    state = 3;
                    break;
                case 4:
                    if (ch != '1') {
                        if (!isDialable(ch)) {
                            break;
                        }
                        return null;
                    }
                    state = 5;
                    break;
                case 8:
                    if (ch != '6') {
                        if (!isDialable(ch)) {
                            break;
                        }
                        return null;
                    }
                    state = 9;
                    break;
                case 9:
                    if (ch == '6') {
                        return new CountryCallingCodeAndNewIndex(66, i + 1);
                    }
                    return null;
                default:
                    return null;
            }
        }
        return null;
    }

    private static int tryGetTrunkPrefixOmittedIndex(String str, int currentIndex) {
        int length = str.length();
        for (int i = currentIndex; i < length; i++) {
            char ch = str.charAt(i);
            if (tryGetISODigit(ch) >= 0) {
                return i + 1;
            }
            if (isDialable(ch)) {
                return -1;
            }
        }
        return -1;
    }

    private static boolean checkPrefixIsIgnorable(String str, int forwardIndex, int backwardIndex) {
        boolean trunk_prefix_was_read = false;
        while (backwardIndex >= forwardIndex) {
            if (tryGetISODigit(str.charAt(backwardIndex)) >= 0) {
                if (trunk_prefix_was_read) {
                    return false;
                }
                trunk_prefix_was_read = true;
            } else if (isDialable(str.charAt(backwardIndex))) {
                return false;
            }
            backwardIndex--;
        }
        return true;
    }

    private static int getDefaultVoiceSubId() {
        return SubscriptionManager.getDefaultVoiceSubId();
    }

    public static int getLTNContactsMatchLength(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CLIDIGITS_PREFERENCES_NAME, 3);
        int Clidigits = sp.getInt(CLIDIGITS_KEY, 7);
        if (TelephonyManager.getDefault().getSimState() == 1 || TelephonyManager.getDefault().getSimState() == 0) {
            return Clidigits;
        }
        String SimOperator = TelephonyManager.getDefault().getSimOperator();
        if (SimOperator == null || SimOperator.length() <= 0) {
            return Clidigits;
        }
        int length;
        int mcc = Integer.parseInt(SimOperator.substring(0, 3));
        int mnc = Integer.parseInt(SimOperator.substring(3, 5));
        switch (mcc) {
            case 214:
            case 338:
            case 363:
            case 374:
            case 708:
            case 714:
            case 716:
            case 722:
            case 744:
                length = 7;
                break;
            case R.styleable.Theme_fragmentBreadCrumbItemLayout /*330*/:
            case 370:
            case 732:
                length = 10;
                break;
            case 334:
                if (mnc != 50 && mnc != 5 && mnc != 30 && mnc != 3 && mnc != 9) {
                    length = 8;
                    break;
                }
                length = 7;
                break;
                break;
            case 368:
            case 454:
            case 704:
            case 706:
            case 710:
            case 712:
            case 724:
            case 730:
            case 736:
            case 740:
                length = 8;
                break;
            case 455:
            case 466:
                length = 9;
                break;
            case 460:
                length = 11;
                break;
            case 734:
                if (mnc != 1 && mnc != 2 && mnc != 3) {
                    length = 10;
                    break;
                }
                length = 7;
                break;
                break;
            case 748:
                if (mnc != 1) {
                    length = 7;
                    break;
                }
                length = 8;
                break;
            default:
                length = 7;
                break;
        }
        if (Clidigits != length) {
            Editor editor = sp.edit();
            editor.putInt(CLIDIGITS_KEY, length);
            editor.commit();
        }
        return length;
    }

    public static boolean isLTNSpecialNumber(String strNumber) {
        boolean bIsDigit = false;
        if (strNumber == null) {
            return false;
        }
        for (int i = 0; i < strNumber.length(); i++) {
            if (Character.isDigit(strNumber.charAt(i))) {
                bIsDigit = true;
                break;
            }
        }
        if (bIsDigit) {
            return false;
        }
        return true;
    }

    public static int getMinMatch() {
        if (CscFeature.getInstance().getInteger("CscFeature_RIL_CallerIdMatchingDigit") == -1) {
            return 7;
        }
        return CscFeature.getInstance().getInteger("CscFeature_RIL_CallerIdMatchingDigit");
    }

    public static String getEmergencyServiceCategory(String number) {
        return getEmergencyServiceCategory(getDefaultVoiceSubId(), number);
    }

    public static String getEmergencyServiceCategory(int subId, String number) {
        String numbers = "";
        if (number == null) {
            return null;
        }
        if (number.isEmpty()) {
            return null;
        }
        if (number.charAt(0) == '/') {
            return null;
        }
        String ecclistNetProp;
        int slotId = SubscriptionManager.getSlotId(subId);
        if (slotId <= 0) {
            ecclistNetProp = "ril.ecclist_net0";
        } else {
            ecclistNetProp = "ril.ecclist_net" + slotId;
        }
        String salesCode = SystemProperties.get("ro.csc.sales_code");
        if (slotId <= 0) {
            slotId = 0;
        }
        log("getEmergencyServiceCategory slodId = " + slotId);
        phoneType = TelephonyManager.getDefault().getCurrentPhoneType(subId);
        String simOperator = SystemProperties.get(TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC);
        if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_ConvertPlusPrefixNumberToMnoCode") && (number.startsWith("184") || number.startsWith("186"))) {
            number = number.substring(3);
        }
        int i;
        String key;
        String n;
        if (mConfigNetworkTypeCapability == null || mConfigNetworkTypeCapability.indexOf("-GLB-USA") < 0) {
            if ((!"CTC".equals(salesCode) && !CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportAllRat") && !CscFeature.getInstance().getEnableStatus("CscFeature_RIL_Support75Mode")) || phoneType != 2) {
                if (phoneType != 2) {
                    i = 0;
                    while (true) {
                        key = "ril.ecclist" + slotId + Integer.toString(i);
                        n = SystemProperties.get(key);
                        if (n.length() == 0) {
                            break;
                        }
                        log(key + " " + n);
                        numbers = numbers + n;
                        i++;
                    }
                } else {
                    numbers = SystemProperties.get("ro.ril.ecclist");
                }
            } else if (phoneType != 2) {
                i = 0;
                while (true) {
                    key = "ril.ecclist" + slotId + Integer.toString(i);
                    n = SystemProperties.get(key);
                    if (n.length() == 0) {
                        break;
                    }
                    log(":: ecclist for SIM" + slotId + " ::" + key + " " + n);
                    numbers = numbers + n;
                    i++;
                }
            } else {
                String ecclist_cdma;
                ecclist_cdma = SystemProperties.get("ro.ril.ecclist");
                log(":: ecclist for UIM" + slotId + " :: " + ecclist_cdma);
                if (ecclist_cdma.length() > 0) {
                    numbers = numbers + FingerprintManager.FINGER_PERMISSION_DELIMITER + ecclist_cdma;
                }
                if ("110".equals(number) || "112".equals(number) || "119".equals(number) || "120".equals(number) || "911".equals(number) || "999".equals(number) || "122".equals(number) || "000".equals(number) || "118".equals(number) || "08".equals(number)) {
                    return "";
                }
            }
        }
        ecclist_cdma = SystemProperties.get("ro.ril.ecclist");
        i = 0;
        while (true) {
            key = "ril.ecclist" + slotId + Integer.toString(i);
            n = SystemProperties.get(key);
            if (n.length() == 0) {
                break;
            }
            log(key + " " + n);
            numbers = numbers + n;
            i++;
        }
        if (ecclist_cdma.length() > 0) {
            numbers = numbers + FingerprintManager.FINGER_PERMISSION_DELIMITER + ecclist_cdma;
        }
        String ecclist_net = SystemProperties.get(ecclistNetProp);
        if (ecclist_net.length() > 0) {
            if ("DTM".equals(salesCode)) {
                numbers = ecclist_net + FingerprintManager.FINGER_PERMISSION_DELIMITER + numbers;
            } else {
                numbers = numbers + FingerprintManager.FINGER_PERMISSION_DELIMITER + ecclist_net;
            }
        }
        String ecclist_test = SystemProperties.get("persist.radio.test_emer_num");
        if (ecclist_test.length() > 0) {
            numbers = numbers + FingerprintManager.FINGER_PERMISSION_DELIMITER + ecclist_test;
        }
        if (mConfigNetworkTypeCapability != null && (mConfigNetworkTypeCapability.indexOf("-GLB-USA") >= 0 || mConfigNetworkTypeCapability.indexOf("-CDM-USA") >= 0)) {
            String ecclist_pbm = SystemProperties.get("ril.ecclist" + slotId);
            if (ecclist_pbm.length() > 0) {
                numbers = numbers + FingerprintManager.FINGER_PERMISSION_DELIMITER + ecclist_pbm;
            }
            log("ECC List: " + numbers);
        }
        if (!TextUtils.isEmpty(numbers)) {
            String[] arr$ = numbers.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                String[] splitStr = arr$[i$].split("/");
                String eccNum = splitStr[0];
                String str = splitStr.length > 1 ? splitStr[1] : "";
                if (!eccNum.equals(number)) {
                    i$++;
                } else if (!"46605".equals(simOperator)) {
                    return str;
                } else {
                    if ("119".equals(number)) {
                        return Integer.toString(2);
                    }
                    if ("110".equals(number) || "112".equals(number)) {
                        return Integer.toString(1);
                    }
                    return str;
                }
            }
            return null;
        } else if (number.equals("112") || number.equals("911")) {
            return "";
        } else {
            return null;
        }
    }

    public static String getEmergencyServiceCategoryforkor(String number) {
        return getEmergencyServiceCategoryforkor(getDefaultVoiceSubId(), number);
    }

    public static String getEmergencyServiceCategoryforkor(int subId, String number) {
        String korDefaultECCList = "112,911,999,000,08,110,118,119,122,113,125,111,117";
        String salesCode = "EUR";
        int slotId = SubscriptionManager.getSlotId(subId);
        if (number == null) {
            return null;
        }
        String currIso;
        String dialNumber = extractNetworkPortionAlt(number);
        String numbers = "";
        int i = 0;
        while (true) {
            String key = "ril.ecclist" + slotId + Integer.toString(i);
            String n = SystemProperties.get(key);
            if (n.length() == 0) {
                break;
            }
            log(key + " " + n);
            numbers = numbers + n;
            i++;
        }
        if (TextUtils.isEmpty(numbers)) {
            numbers = korDefaultECCList;
        } else {
            numbers = numbers + FingerprintManager.FINGER_PERMISSION_DELIMITER + korDefaultECCList;
        }
        if (!TextUtils.isEmpty(numbers)) {
            String currPlmn = SystemProperties.get("ril.currentplmn", "domestic");
            boolean isDomestic = "domestic".equals(currPlmn) || SmsConstants.FORMAT_UNKNOWN.equals(currPlmn) || TextUtils.isEmpty(currPlmn);
            String[] arr$;
            int len$;
            int i$;
            String[] splitStr;
            String eccNum;
            String eccCat;
            String ecc119;
            if ("SKT".equals(salesCode)) {
                arr$ = numbers.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                len$ = arr$.length;
                i$ = 0;
                while (i$ < len$) {
                    splitStr = arr$[i$].split("/");
                    eccNum = splitStr[0];
                    eccCat = splitStr.length > 1 ? splitStr[1] : "";
                    if (!eccNum.equals(dialNumber)) {
                        i$++;
                    } else if ("112".equals(dialNumber)) {
                        return Integer.toString(1);
                    } else {
                        if ("119".equals(dialNumber)) {
                            ecc119 = SystemProperties.get("ril.skt119Cat");
                            if (ecc119.length() > 0) {
                                return ecc119;
                            }
                            if (eccCat.length() > 0) {
                                return eccCat;
                            }
                            return Integer.toString(4);
                        } else if ("911".equals(dialNumber)) {
                            return Integer.toString(4);
                        } else {
                            if ("122".equals(dialNumber)) {
                                return Integer.toString(8);
                            }
                            if ("113".equals(dialNumber)) {
                                return isDomestic ? Integer.toString(3) : null;
                            } else {
                                if ("125".equals(dialNumber)) {
                                    return isDomestic ? Integer.toString(9) : null;
                                } else {
                                    if ("117".equals(dialNumber)) {
                                        return isDomestic ? Integer.toString(18) : null;
                                    } else {
                                        if ("118".equals(dialNumber)) {
                                            return isDomestic ? Integer.toString(19) : Integer.toString(4);
                                        } else {
                                            if ("111".equals(dialNumber)) {
                                                return isDomestic ? Integer.toString(6) : null;
                                            } else {
                                                if ("127".equals(dialNumber)) {
                                                    return null;
                                                }
                                                if ("000".equals(dialNumber) || "08".equals(dialNumber) || "110".equals(dialNumber) || "999".equals(dialNumber)) {
                                                    return !isDomestic ? Integer.toString(4) : null;
                                                } else {
                                                    return eccCat;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if ("KTT".equals(salesCode)) {
                for (String emergencyNum : numbers.split(FingerprintManager.FINGER_PERMISSION_DELIMITER)) {
                    splitStr = emergencyNum.split("/");
                    eccNum = splitStr[0];
                    eccCat = splitStr.length > 1 ? splitStr[1] : "";
                    if ("112".equals(dialNumber)) {
                        return Integer.toString(1);
                    }
                    if ("119".equals(dialNumber)) {
                        return Integer.toString(4);
                    }
                    if ("911".equals(dialNumber)) {
                        return Integer.toString(4);
                    }
                    if ("122".equals(dialNumber)) {
                        return Integer.toString(8);
                    }
                    if ("113".equals(dialNumber)) {
                        return Integer.toString(3);
                    }
                    if ("125".equals(dialNumber)) {
                        return Integer.toString(9);
                    }
                    if ("117".equals(dialNumber)) {
                        return Integer.toString(18);
                    }
                    if ("118".equals(dialNumber)) {
                        return Integer.toString(19);
                    }
                    if ("111".equals(dialNumber)) {
                        return Integer.toString(7);
                    }
                    if ("127".equals(dialNumber)) {
                        return null;
                    }
                    if (eccNum.equals(dialNumber) && !TextUtils.isEmpty(eccCat)) {
                        return eccCat;
                    }
                    if (eccNum.equals(dialNumber) && TextUtils.isEmpty(eccCat)) {
                        return Integer.toString(0);
                    }
                    if ("000".equals(dialNumber) || "08".equals(dialNumber) || "110".equals(dialNumber) || "999".equals(dialNumber)) {
                        return null;
                    }
                }
            } else if ("LGT".equals(salesCode)) {
                currIso = SystemProperties.get("ril.currentplmn", "domestic");
                String simstate = SystemProperties.get(TelephonyProperties.PROPERTY_SIM_STATE, "");
                String network_type = SystemProperties.get(TelephonyProperties.PROPERTY_DATA_NETWORK_TYPE, "");
                String simtype = SystemProperties.get("ril.simtype", "");
                boolean isCdma = false;
                boolean isLteOnly = false;
                int serviceState = 0;
                try {
                    ITelephony phone = Stub.asInterface(ServiceManager.checkService(PhoneConstants.PHONE_KEY));
                    if (phone != null) {
                        isCdma = phone.getActivePhoneType() == 2;
                        serviceState = 1;
                    }
                } catch (RemoteException e) {
                    Rlog.w(LOG_TAG, "phone.getActivePhoneType() failed", e);
                }
                if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_CallLteSinglemodeSupport")) {
                    log("isLteOnly is serviceState : " + serviceState);
                    if (serviceState != 1) {
                        isLteOnly = true;
                    }
                }
                log("isCdma = " + isCdma);
                if (isCdma && "domestic".equals(currIso) && ("3".equals(simtype) || "18".equals(simtype))) {
                    if ("112".equals(dialNumber) || "119".equals(dialNumber) || "122".equals(dialNumber) || "113".equals(dialNumber) || "125".equals(dialNumber) || "117".equals(dialNumber) || "111".equals(dialNumber) || "114".equals(dialNumber) || "118".equals(dialNumber)) {
                        return "";
                    }
                    return null;
                } else if (isLteOnly && "domestic".equals(currIso) && ("3".equals(simtype) || "18".equals(simtype))) {
                    log("isLteOnly emergency in domestic, dialNumber = " + dialNumber);
                    if ("112".equals(dialNumber)) {
                        return Integer.toString(1);
                    }
                    if ("119".equals(dialNumber)) {
                        return Integer.toString(4);
                    }
                    if ("911".equals(dialNumber)) {
                        return Integer.toString(4);
                    }
                    if ("122".equals(dialNumber)) {
                        return Integer.toString(8);
                    }
                    if ("113".equals(dialNumber)) {
                        return Integer.toString(3);
                    }
                    if ("125".equals(dialNumber)) {
                        return Integer.toString(9);
                    }
                    if ("117".equals(dialNumber)) {
                        return Integer.toString(18);
                    }
                    if ("111".equals(dialNumber)) {
                        return Integer.toString(6);
                    }
                    if ("118".equals(dialNumber)) {
                        return Integer.toString(19);
                    }
                    return null;
                } else if ("oversea".equals(currIso)) {
                    if ("112".equals(dialNumber)) {
                        return Integer.toString(1);
                    }
                    if ("122".equals(dialNumber)) {
                        return Integer.toString(8);
                    }
                    if ("911".equals(dialNumber)) {
                        return Integer.toString(4);
                    }
                    if (!"119".equals(dialNumber)) {
                        return null;
                    }
                    ecc119 = SystemProperties.get("ril.skt119Cat");
                    if (ecc119.length() > 0) {
                        return ecc119;
                    }
                    return Integer.toString(2);
                } else if ("112".equals(dialNumber)) {
                    return Integer.toString(1);
                } else {
                    if ("119".equals(dialNumber)) {
                        return Integer.toString(4);
                    }
                    if ("911".equals(dialNumber)) {
                        return Integer.toString(4);
                    }
                    if ("122".equals(dialNumber)) {
                        return Integer.toString(8);
                    }
                    if ("113".equals(dialNumber)) {
                        return Integer.toString(3);
                    }
                    if ("125".equals(dialNumber)) {
                        return Integer.toString(9);
                    }
                    if ("117".equals(dialNumber)) {
                        return Integer.toString(18);
                    }
                    if ("118".equals(dialNumber)) {
                        return Integer.toString(19);
                    }
                    if ("111".equals(dialNumber)) {
                        return Integer.toString(6);
                    }
                    if (isCdma && "114".equals(dialNumber)) {
                        return "";
                    }
                    return null;
                }
            } else if ("ANY".equals(salesCode) || "OPEN".equals(salesCode) || "KOO".equals(salesCode)) {
                arr$ = numbers.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                len$ = arr$.length;
                i$ = 0;
                while (i$ < len$) {
                    splitStr = arr$[i$].split("/");
                    eccNum = splitStr[0];
                    eccCat = splitStr.length > 1 ? splitStr[1] : "";
                    if (!eccNum.equals(dialNumber)) {
                        i$++;
                    } else if ("112".equals(dialNumber)) {
                        return Integer.toString(1);
                    } else {
                        if ("119".equals(dialNumber)) {
                            ecc119 = SystemProperties.get("ril.skt119Cat");
                            if (ecc119.length() > 0) {
                                return ecc119;
                            }
                            if (eccCat.length() > 0) {
                                return eccCat;
                            }
                            return Integer.toString(4);
                        } else if ("911".equals(dialNumber)) {
                            return Integer.toString(4);
                        } else {
                            if ("122".equals(dialNumber)) {
                                return Integer.toString(8);
                            }
                            if ("113".equals(dialNumber)) {
                                return isDomestic ? Integer.toString(3) : null;
                            } else {
                                if ("125".equals(dialNumber)) {
                                    return isDomestic ? Integer.toString(9) : null;
                                } else {
                                    if ("127".equals(dialNumber)) {
                                        return null;
                                    }
                                    if ("111".equals(dialNumber)) {
                                        return isDomestic ? Integer.toString(6) : null;
                                    } else {
                                        if ("117".equals(dialNumber)) {
                                            return isDomestic ? Integer.toString(18) : null;
                                        } else {
                                            if ("118".equals(dialNumber)) {
                                                return isDomestic ? Integer.toString(19) : Integer.toString(4);
                                            } else {
                                                if ("000".equals(dialNumber) || "08".equals(dialNumber) || "110".equals(dialNumber) || "999".equals(dialNumber)) {
                                                    return !isDomestic ? Integer.toString(4) : null;
                                                } else {
                                                    return eccCat;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        currIso = SystemProperties.get("ril.currentplmn", "domestic");
        if ("domestic".equals(currIso) || SmsConstants.FORMAT_UNKNOWN.equals(currIso) || TextUtils.isEmpty(currIso)) {
            if ("112".equals(dialNumber) || "119".equals(dialNumber) || "122".equals(dialNumber) || "113".equals(dialNumber) || "125".equals(dialNumber) || "117".equals(dialNumber) || "111".equals(dialNumber) || "118".equals(dialNumber)) {
                return "";
            }
            return null;
        } else if ("000".equals(dialNumber) || "08".equals(dialNumber) || "110".equals(dialNumber) || "999".equals(dialNumber) || "118".equals(dialNumber) || "112".equals(dialNumber) || "911".equals(dialNumber) || "119".equals(dialNumber) || "122".equals(dialNumber)) {
            return "";
        } else {
            return null;
        }
    }

    public static String getCurrentOtaCountryIdd(Context context) {
        boolean z = true;
        String idd = "";
        if (Settings$System.getInt(context.getContentResolver(), Settings$System.ASSISTED_DIALING, 1) != 1) {
            z = false;
        }
        Cursor c = getOtaCountry(context, z);
        if (c != null && c.moveToFirst()) {
            idd = c.getString(3);
        }
        if (c != null) {
            c.close();
        }
        return idd;
    }

    public static String getCurrentOtaCountryNanp(Context context) {
        boolean z = true;
        String nanp = "";
        if (Settings$System.getInt(context.getContentResolver(), Settings$System.ASSISTED_DIALING, 1) != 1) {
            z = false;
        }
        Cursor c = getOtaCountry(context, z);
        if (c != null && c.moveToFirst()) {
            nanp = c.getString(5);
        }
        if (c != null) {
            c.close();
        }
        return nanp;
    }

    public static String assistedDialFromContactList(String phoneNumber, Context context) {
        try {
            Rlog.d(LOG_TAG, "Called assistedDialFromContactList : ");
            if (phoneNumber == null || isUriNumber(phoneNumber)) {
                isAssistedDialingNumber = false;
                return phoneNumber;
            }
            phoneNumber = stripSeparators(convertKeypadLettersToDigits(phoneNumber));
            char ch = phoneNumber.charAt(0);
            if (isISODigit(ch) || PLUS_SIGN_CHAR == ch) {
                Rlog.d(LOG_TAG, "Assisted Dialing PhoneNumber is OK: " + ch);
                if (retrieveAssistedParams(phoneNumber, context)) {
                    String temp;
                    StringBuilder stringBuilder = new StringBuilder(128);
                    boolean numberBeginsWithRefCountryNDDPrefix = phoneNumber.startsWith(refCountryNDDPrefix);
                    boolean numberBeginsWithRefCountryIDDPrefix = phoneNumber.startsWith(refCountryIDDPrefix);
                    boolean numberBeginsWithNonUSIDDPrefix = !phoneNumber.startsWith("011");
                    boolean numberBeginsWithOTAIDDPrefix = phoneNumber.startsWith(otaCountryIDDPrefix);
                    boolean numberBeginsWithOTANDDPrefix = phoneNumber.startsWith(otaCountryNDDPrefix);
                    char c = phoneNumber.charAt(0);
                    checkAssistedDialingTestmode(context);
                    if (isCDMARegistered) {
                        boolean match;
                        if (isISODigit(c)) {
                            if (isNetRoaming && !isOTANANPCountry) {
                                if (numberLength <= refCountryNationalNumberLength.intValue()) {
                                    if (numberLength == refCountryNationalNumberLength.intValue()) {
                                        if (refCountryCountryCode.equals(otaCountryCountryCode)) {
                                            Rlog.d(LOG_TAG, "[AssistDialing2-3] ");
                                            isAssistedDialingNumber = false;
                                            return phoneNumber;
                                        } else if (isNBPCDSupported) {
                                            stringBuilder.append(PLUS_SIGN_STRING);
                                            stringBuilder.append(refCountryCountryCode);
                                            stringBuilder.append(phoneNumber);
                                            Rlog.d(LOG_TAG, "[AssistDialing2-1] ");
                                            isAssistedDialingNumber = true;
                                            return stringBuilder.toString();
                                        } else {
                                            stringBuilder.append(otaCountryIDDPrefix);
                                            stringBuilder.append(refCountryCountryCode);
                                            stringBuilder.append(phoneNumber);
                                            Rlog.d(LOG_TAG, "[AssistDialing2-2] ");
                                            isAssistedDialingNumber = true;
                                            return stringBuilder.toString();
                                        }
                                    } else if (numberLength == refCountryNationalNumberLength.intValue() - refCountryAreaCode.length()) {
                                        if (refCountryCountryCode.equals(otaCountryCountryCode)) {
                                            Rlog.d(LOG_TAG, "[AssistDialing3-3] ");
                                            isAssistedDialingNumber = false;
                                            return phoneNumber;
                                        } else if (isNBPCDSupported) {
                                            stringBuilder.append(PLUS_SIGN_STRING);
                                            stringBuilder.append(refCountryCountryCode);
                                            stringBuilder.append(refCountryAreaCode);
                                            stringBuilder.append(phoneNumber);
                                            Rlog.d(LOG_TAG, "[AssistDialing3-1] ");
                                            isAssistedDialingNumber = true;
                                            return stringBuilder.toString();
                                        } else {
                                            stringBuilder.append(otaCountryIDDPrefix);
                                            stringBuilder.append(refCountryCountryCode);
                                            stringBuilder.append(refCountryAreaCode);
                                            stringBuilder.append(phoneNumber);
                                            Rlog.d(LOG_TAG, "[AssistDialing3-2] ");
                                            isAssistedDialingNumber = true;
                                            return stringBuilder.toString();
                                        }
                                    }
                                }
                                if (numberBeginsWithRefCountryIDDPrefix || numberBeginsWithOTAIDDPrefix) {
                                    String iddPrefix;
                                    String numberAfterIDDPrefix;
                                    if (true == numberBeginsWithRefCountryIDDPrefix) {
                                        iddPrefix = refCountryIDDPrefix;
                                        numberAfterIDDPrefix = phoneNumber.substring(refCountryIDDPrefix.length(), phoneNumber.length());
                                    } else {
                                        iddPrefix = otaCountryIDDPrefix;
                                        numberAfterIDDPrefix = phoneNumber.substring(otaCountryIDDPrefix.length(), phoneNumber.length());
                                    }
                                    if (numberAfterIDDPrefix.startsWith(otaCountryCountryCode)) {
                                        if (isNBPCDSupported) {
                                            stringBuilder.append(phoneNumber);
                                            stringBuilder.replace(0, iddPrefix.length() + otaCountryCountryCode.length(), PLUS_SIGN_STRING);
                                            Rlog.d(LOG_TAG, "[AssistDialing6-1] ");
                                            isAssistedDialingNumber = true;
                                            return stringBuilder.toString();
                                        }
                                        stringBuilder.append(phoneNumber);
                                        stringBuilder.replace(0, iddPrefix.length() + otaCountryCountryCode.length(), otaCountryNDDPrefix);
                                        Rlog.d(LOG_TAG, "[AssistDialing6-2] ");
                                        isAssistedDialingNumber = true;
                                        return stringBuilder.toString();
                                    } else if (isNBPCDSupported) {
                                        stringBuilder.append(phoneNumber);
                                        stringBuilder.replace(0, iddPrefix.length(), PLUS_SIGN_STRING);
                                        Rlog.d(LOG_TAG, "[AssistDialing6-3] ");
                                        isAssistedDialingNumber = true;
                                        return stringBuilder.toString();
                                    } else if (numberBeginsWithRefCountryIDDPrefix) {
                                        stringBuilder.append(phoneNumber);
                                        stringBuilder.replace(0, refCountryIDDPrefix.length(), otaCountryIDDPrefix);
                                        Rlog.d(LOG_TAG, "[AssistDialing6-4] ");
                                        isAssistedDialingNumber = true;
                                        return stringBuilder.toString();
                                    } else if (numberBeginsWithOTAIDDPrefix) {
                                        Rlog.d(LOG_TAG, "[AssistDialing6-5] ");
                                        isAssistedDialingNumber = false;
                                        return phoneNumber;
                                    }
                                } else if ((numberBeginsWithRefCountryNDDPrefix || numberBeginsWithOTANDDPrefix) && numberLength == refCountryNationalNumberLength.intValue() + refCountryNDDPrefix.length()) {
                                    if (refCountryCountryCode.equals(otaCountryCountryCode)) {
                                        Rlog.d(LOG_TAG, "[AssistDialing7-3] ");
                                        isAssistedDialingNumber = false;
                                        return phoneNumber;
                                    }
                                    String nddPrefix;
                                    if (true == numberBeginsWithRefCountryNDDPrefix) {
                                        nddPrefix = refCountryNDDPrefix;
                                    } else {
                                        nddPrefix = otaCountryNDDPrefix;
                                    }
                                    if (isNBPCDSupported) {
                                        stringBuilder.append(phoneNumber);
                                        stringBuilder.replace(0, nddPrefix.length(), PLUS_SIGN_STRING);
                                        Rlog.d(LOG_TAG, "[AssistDialing7-1] ");
                                        isAssistedDialingNumber = true;
                                        return stringBuilder.toString();
                                    }
                                    temp = new StringBuffer().append(otaCountryIDDPrefix).append(refCountryCountryCode).toString();
                                    stringBuilder.append(phoneNumber);
                                    stringBuilder.replace(0, nddPrefix.length(), temp);
                                    Rlog.d(LOG_TAG, "[AssistDialing7-2] ");
                                    isAssistedDialingNumber = true;
                                    return stringBuilder.toString();
                                }
                            } else if (!numberBeginsWithNonUSIDDPrefix || numberLength < 11 || '1' == c) {
                                if (numberLength == refCountryNationalNumberLength.intValue() + refCountryNDDPrefix.length()) {
                                    if (numberBeginsWithRefCountryNDDPrefix) {
                                        if (isNBPCDSupported) {
                                            stringBuilder.append(phoneNumber);
                                            stringBuilder.replace(0, refCountryNDDPrefix.length(), PLUS_SIGN_STRING);
                                            Rlog.d(LOG_TAG, "[AssistDialing4-1] ");
                                            isAssistedDialingNumber = true;
                                            return stringBuilder.toString();
                                        }
                                        Rlog.d(LOG_TAG, "[AssistDialing4-2] ");
                                        isAssistedDialingNumber = false;
                                        return phoneNumber;
                                    } else if (numberBeginsWithOTANDDPrefix) {
                                        if (isNBPCDSupported) {
                                            stringBuilder.append(phoneNumber);
                                            stringBuilder.replace(0, otaCountryNDDPrefix.length(), PLUS_SIGN_STRING);
                                            Rlog.d(LOG_TAG, "[AssistDialing4-3] ");
                                            isAssistedDialingNumber = true;
                                            return stringBuilder.toString();
                                        }
                                        Rlog.d(LOG_TAG, "[AssistDialing4-4] ");
                                        isAssistedDialingNumber = false;
                                        return phoneNumber;
                                    }
                                }
                                if (numberBeginsWithRefCountryIDDPrefix && refCountryCountryCode.equals(SmartFaceManager.PAGE_BOTTOM) && refCountryIDDPrefix == "011") {
                                    Rlog.d(LOG_TAG, "[AssistDialing5-1] ");
                                    isAssistedDialingNumber = false;
                                    return phoneNumber;
                                }
                            } else {
                                match = false;
                                int findIDDLen = 0;
                                mCursor = context.getContentResolver().query(MCC_OTA_URI, null, null, null, null);
                                mCursor.moveToFirst();
                                mCursorContry = context.getContentResolver().query(MCC_OTA_URI, null, null, null, null);
                                while (mCursor != null && !mCursor.isAfterLast()) {
                                    mCursorContry.moveToFirst();
                                    while (mCursorContry != null && !mCursorContry.isAfterLast()) {
                                        if (phoneNumber.startsWith(mCursor.getString(3) + mCursorContry.getString(6))) {
                                            findIDDLen = mCursor.getString(3).length();
                                            match = true;
                                            break;
                                        }
                                        mCursorContry.moveToNext();
                                    }
                                    if (match) {
                                        break;
                                    }
                                    mCursor.moveToNext();
                                }
                                if (mCursorContry != null) {
                                    mCursorContry.close();
                                }
                                if (mCursor != null) {
                                    mCursor.close();
                                }
                                if (match) {
                                    stringBuilder.append(phoneNumber);
                                    stringBuilder.replace(0, findIDDLen, "011");
                                    Rlog.d(LOG_TAG, "[AssistDialing1-2] ");
                                    isAssistedDialingNumber = true;
                                    return stringBuilder.toString();
                                }
                                stringBuilder.append("011");
                                stringBuilder.append(phoneNumber);
                                Rlog.d(LOG_TAG, "[AssistDialing1-1] ");
                                isAssistedDialingNumber = true;
                                return stringBuilder.toString();
                            }
                        }
                        if (PLUS_SIGN_CHAR == c && !isNBPCDSupported) {
                            String numberAfterPlus = phoneNumber.substring(1, phoneNumber.length());
                            boolean numberAfterPlusContainsOTACountryCode = numberAfterPlus.startsWith(otaCountryCountryCode);
                            if (!isNetRoaming || isOTANANPCountry) {
                                if (numberAfterPlus.length() >= 11) {
                                    if (11 == numberAfterPlus.length() && '1' == phoneNumber.charAt(1)) {
                                        Rlog.d(LOG_TAG, "[AssistDialing9-1] ");
                                        isAssistedDialingNumber = false;
                                        return phoneNumber;
                                    }
                                    match = false;
                                    mCursor = context.getContentResolver().query(MCC_OTA_URI, null, null, null, null);
                                    mCursor.moveToFirst();
                                    while (mCursor != null && !mCursor.isAfterLast()) {
                                        if (numberAfterPlus.startsWith(mCursor.getString(6))) {
                                            match = true;
                                            break;
                                        }
                                        mCursor.moveToNext();
                                    }
                                    if (mCursor == null || !match) {
                                        Rlog.d(LOG_TAG, "[AssistDialing9-2] ");
                                        isAssistedDialingNumber = false;
                                        return phoneNumber;
                                    } else if (numberAfterPlus.length() - mCursor.getString(6).length() == refCountryNationalNumberLength.intValue()) {
                                        mCursor.close();
                                        stringBuilder.append(phoneNumber);
                                        stringBuilder.replace(0, 1, refCountryIDDPrefix);
                                        Rlog.d(LOG_TAG, "[AssistDialing9-3] ");
                                        isAssistedDialingNumber = false;
                                        return stringBuilder.toString();
                                    }
                                }
                            } else if (numberAfterPlusContainsOTACountryCode) {
                                stringBuilder.append(phoneNumber);
                                stringBuilder.replace(0, otaCountryCountryCode.length() + 1, otaCountryNDDPrefix);
                                Rlog.d(LOG_TAG, "[AssistDialing8-1] ");
                                isAssistedDialingNumber = false;
                                return stringBuilder.toString();
                            } else {
                                stringBuilder.append(phoneNumber);
                                stringBuilder.replace(0, 1, otaCountryIDDPrefix);
                                Rlog.d(LOG_TAG, "[AssistDialing8-2] ");
                                isAssistedDialingNumber = false;
                                return stringBuilder.toString();
                            }
                        }
                    }
                    if (isGSMRegistered) {
                        if (!isISODigit(c)) {
                            if (phoneNumber.startsWith("+011")) {
                                stringBuilder.append(PLUS_SIGN_STRING);
                                stringBuilder.append(phoneNumber.substring(4));
                                isAssistedDialingNumber = true;
                                return stringBuilder.toString();
                            }
                        } else if (numberLength <= refCountryNationalNumberLength.intValue()) {
                            if (numberLength == refCountryNationalNumberLength.intValue()) {
                                stringBuilder.append(PLUS_SIGN_STRING);
                                stringBuilder.append(refCountryCountryCode);
                                stringBuilder.append(phoneNumber);
                                Rlog.d(LOG_TAG, "[AssistDialing10-1] ");
                                isAssistedDialingNumber = true;
                                return stringBuilder.toString();
                            } else if (numberLength == refCountryNationalNumberLength.intValue() - refCountryAreaCode.length()) {
                                stringBuilder.append(PLUS_SIGN_STRING);
                                stringBuilder.append(refCountryCountryCode);
                                stringBuilder.append(refCountryAreaCode);
                                stringBuilder.append(phoneNumber);
                                Rlog.d(LOG_TAG, "[AssistDialing10-2] ");
                                isAssistedDialingNumber = true;
                                return stringBuilder.toString();
                            }
                        } else if (numberBeginsWithRefCountryIDDPrefix) {
                            stringBuilder.append(phoneNumber);
                            stringBuilder.replace(0, refCountryIDDPrefix.length(), PLUS_SIGN_STRING);
                            Rlog.d(LOG_TAG, "[AssistDialing11-1] ");
                            isAssistedDialingNumber = true;
                            return stringBuilder.toString();
                        } else if (numberBeginsWithRefCountryNDDPrefix && numberLength == refCountryNationalNumberLength.intValue() + refCountryNDDPrefix.length()) {
                            temp = new StringBuffer().append(PLUS_SIGN_STRING).append(refCountryCountryCode).toString();
                            stringBuilder.append(phoneNumber);
                            stringBuilder.replace(0, refCountryNDDPrefix.length(), temp);
                            Rlog.d(LOG_TAG, "[AssistDialing11-2] ");
                            isAssistedDialingNumber = true;
                            return stringBuilder.toString();
                        }
                    }
                    Rlog.d(LOG_TAG, "[AssistDialing13-1] ");
                    isAssistedDialingNumber = false;
                    return phoneNumber;
                }
                isAssistedDialingNumber = false;
                return phoneNumber;
            }
            Rlog.d(LOG_TAG, "Assisted Dialing PhoneNumber is FAILED: " + ch);
            isAssistedDialingNumber = false;
            return phoneNumber;
        } catch (Exception e) {
            Rlog.d(LOG_TAG, "Cannot assist: " + e);
            isAssistedDialingNumber = false;
            return phoneNumber;
        }
    }

    private static boolean retrieveAssistedParams(String phoneNumber, Context context) {
        phoneType = SystemProperties.getInt(TelephonyProperties.CURRENT_ACTIVE_PHONE, 2);
        numberLength = extractNetworkPortionAlt(phoneNumber).length();
        isNetRoaming = ((TelephonyManager) context.getSystemService(PhoneConstants.PHONE_KEY)).isNetworkRoaming();
        String mdn = ((TelephonyManager) context.getSystemService(PhoneConstants.PHONE_KEY)).getLine1Number();
        if (mdn != null && (TextUtils.isEmpty(mdn) || mdn.length() < 3)) {
            return false;
        }
        if ("LRA".equals(SystemProperties.get("ro.csc.sales_code"))) {
            Rlog.d(LOG_TAG, "Assisted Dial not supported in LRA. Return original number");
            return false;
        }
        mCursor = context.getContentResolver().query(REF_COUNTRY_SHARED_PREF, null, null, null, null);
        if (mCursor == null) {
            return false;
        }
        boolean z;
        mCursor.moveToFirst();
        refCountryName = mCursor.getString(1);
        String refmcc = mCursor.getString(2);
        if (refmcc.equals("310 to 316")) {
            refmcc = "310";
        } else if (refmcc.equals("430 to 431")) {
            refmcc = "430";
        }
        refCountryMCC = refmcc;
        refCountryIDDPrefix = mCursor.getString(3);
        refCountryNDDPrefix = mCursor.getString(4);
        if (mCursor.getString(5).equals("NANP")) {
            z = true;
        } else {
            z = false;
        }
        isNANPCountry = z;
        refCountryCountryCode = mCursor.getString(6);
        isNBPCDSupported = Boolean.parseBoolean(mCursor.getString(7));
        refCountryAreaCode = mCursor.getString(8);
        refCountryNationalNumberLength = Integer.getInteger(mCursor.getString(9));
        if (refCountryAreaCode == null) {
            if (mdn == null || mdn.length() < 3) {
                refCountryAreaCode = "123";
            } else {
                refCountryAreaCode = mdn.substring(0, 3);
            }
        }
        if (refCountryNationalNumberLength == null) {
            if (mdn == null || mdn.length() < 3) {
                refCountryNationalNumberLength = Integer.valueOf(10);
            } else {
                refCountryNationalNumberLength = Integer.valueOf(mdn.length());
            }
        }
        Rlog.d(LOG_TAG, "refCountryMCC: " + refCountryMCC);
        mCursor.close();
        if (phoneType == 1) {
            z = true;
        } else {
            z = false;
        }
        isGSMRegistered = z;
        if (phoneType == 2) {
            z = true;
        } else {
            z = false;
        }
        isCDMARegistered = z;
        mCursor = getOtaCountry(context, true);
        otaCountryMCC = null;
        if (mCursor != null && mCursor.moveToFirst()) {
            otaCountryName = mCursor.getString(1);
            otaCountryMCC = mCursor.getString(2);
            otaCountryIDDPrefix = mCursor.getString(3);
            otaCountryNDDPrefix = mCursor.getString(4);
            if (otaCountryNDDPrefix == null) {
                otaCountryNDDPrefix = "";
            }
            isOTANANPCountry = mCursor.getString(5).equals("NANP");
            otaCountryCountryCode = mCursor.getString(6);
            isOTANBPCDSupported = Boolean.parseBoolean(mCursor.getString(7));
            String str = otaCountryMCC.equals("310 to 316") ? "310" : otaCountryMCC.equals("430 to 431") ? "430" : otaCountryMCC;
            otaCountryMCC = str;
        }
        if (mCursor != null) {
            mCursor.close();
        }
        if (otaCountryMCC == null) {
            Rlog.e(LOG_TAG, "OTA country not found");
            return false;
        }
        displayAssistedParams();
        return true;
    }

    private static void displayAssistedParams() {
        Rlog.d(LOG_TAG, " refCountryName: " + refCountryName + " refCountryMCC: " + refCountryMCC + " refCountryIDDPrefix: " + refCountryIDDPrefix + " refCountryNDDPrefix: " + refCountryNDDPrefix + " refCountryAreaCode: " + refCountryAreaCode + " refCountryNationalNumberLength: " + refCountryNationalNumberLength + " isNANPCountry: " + isNANPCountry + " refCountryCountryCode: " + refCountryCountryCode + " isNBPCDSupported: " + isNBPCDSupported + " isGSMRegistered: " + isGSMRegistered + " isCDMARegistered: " + isCDMARegistered);
        Rlog.d(LOG_TAG, " isNetRoaming: " + isNetRoaming + " numberLength: " + numberLength + " otaCountryName: " + otaCountryName + " otaCountryMCC: " + otaCountryMCC + " otaCountryIDDPrefix: " + otaCountryIDDPrefix + " otaCountryNDDPrefix: " + otaCountryNDDPrefix + " isOTANANPCountry: " + isOTANANPCountry + " otaCountryCountryCode: " + otaCountryCountryCode + " isOTANBPCDSupported: " + isOTANBPCDSupported);
    }

    private static void checkAssistedDialingTestmode(Context context) {
        boolean z = true;
        String assistedDialingTest = SystemProperties.get(TelephonyProperties.PROPERTY_TEST_ASSISTEDDIAL, SmartFaceManager.FALSE);
        if (!SmartFaceManager.FALSE.equals(assistedDialingTest)) {
            String[] assistedDialingTestParam = assistedDialingTest.split(":");
            for (int i = 0; i < assistedDialingTestParam.length; i++) {
                Rlog.d(LOG_TAG, "Assisted Dialing Testmode Parameter[" + i + "] : " + assistedDialingTestParam[i]);
            }
            if (assistedDialingTestParam.length > 0) {
                boolean z2;
                if ("gsm".equals(assistedDialingTestParam[0])) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                isGSMRegistered = z2;
                if (isGSMRegistered) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                isCDMARegistered = z2;
                if (assistedDialingTestParam.length > 1) {
                    if ("roam".equals(assistedDialingTestParam[1])) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    isNetRoaming = z2;
                    if (assistedDialingTestParam.length > 2) {
                        Cursor cursor;
                        ContentResolver otacr = context.getContentResolver();
                        if ("310".equals(assistedDialingTestParam[2])) {
                            cursor = otacr.query(MCC_OTA_URI, null, "mcc=?", new String[]{"310 to 316"}, null);
                        } else {
                            cursor = otacr.query(MCC_OTA_URI, null, "mcc=?", new String[]{assistedDialingTestParam[2]}, null);
                        }
                        otaCountryMCC = null;
                        if (cursor == null) {
                            Rlog.d(LOG_TAG, "Assisted Dialing Testmode - cursor is null");
                        } else if (cursor.moveToFirst()) {
                            Rlog.d(LOG_TAG, "Assisted Dialing Testmode - find cursor");
                            otaCountryName = cursor.getString(1);
                            otaCountryMCC = cursor.getString(2);
                            otaCountryIDDPrefix = cursor.getString(3);
                            otaCountryNDDPrefix = cursor.getString(4);
                            if (otaCountryNDDPrefix == null) {
                                otaCountryNDDPrefix = "";
                            }
                            if (!cursor.getString(5).equals("NANP")) {
                                z = false;
                            }
                            isOTANANPCountry = z;
                            otaCountryCountryCode = cursor.getString(6);
                            isOTANBPCDSupported = Boolean.parseBoolean(cursor.getString(7));
                            String str = otaCountryMCC.equals("310 to 316") ? "310" : otaCountryMCC.equals("430 to 431") ? "430" : otaCountryMCC;
                            otaCountryMCC = str;
                        } else {
                            Rlog.d(LOG_TAG, "Assisted Dialing Testmode - cursor is empty");
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                }
                Rlog.d(LOG_TAG, "========== Assisted Dialing Parameter for Testmode ==========");
                displayAssistedParams();
            }
        }
    }

    public static String assistedDialFromDialPad(String phoneNumber, Context context) {
        try {
            Rlog.d(LOG_TAG, "Called assistedDialFromDialPad : ");
            if (phoneNumber == null || isUriNumber(phoneNumber)) {
                isAssistedDialingNumber = false;
                return phoneNumber;
            }
            phoneNumber = stripSeparators(convertKeypadLettersToDigits(phoneNumber));
            char ch = phoneNumber.charAt(0);
            if (isISODigit(ch) || PLUS_SIGN_CHAR == ch) {
                Rlog.d(LOG_TAG, "Assisted Dialing PhoneNumber is OK: " + ch);
                if (retrieveAssistedParams(phoneNumber, context)) {
                    StringBuilder newPhoneNumber = new StringBuilder(128);
                    boolean enableAssistedDialing = Settings$System.getInt(context.getContentResolver(), Settings$System.ASSISTED_DIALING, 0) == 1;
                    boolean numberBeginsWithRefCountryIDDPrefixWithAdEnabled = enableAssistedDialing && phoneNumber.startsWith(refCountryIDDPrefix);
                    boolean numberBeginsWithOTAIDDPrefix = phoneNumber.startsWith(otaCountryIDDPrefix);
                    Rlog.d(LOG_TAG, "enableAssistedDialing: " + enableAssistedDialing + ", numberBeginsWithRefCountryIDDPrefixWithAdEnabled: " + numberBeginsWithRefCountryIDDPrefixWithAdEnabled + ", numberBeginsWithOTAIDDPrefix: " + numberBeginsWithOTAIDDPrefix);
                    checkAssistedDialingTestmode(context);
                    if (isCDMARegistered) {
                        char c;
                        if (!isNetRoaming || isOTANANPCountry) {
                            boolean numberBeginsWithNonUSIDDPrefix = !phoneNumber.startsWith("011");
                            c = phoneNumber.charAt(0);
                            if (isISODigit(c) && numberBeginsWithNonUSIDDPrefix && numberLength >= 11 && '1' != c) {
                                newPhoneNumber.append("011");
                                newPhoneNumber.append(phoneNumber);
                                Rlog.d(LOG_TAG, "[AssistDialingA-1] ");
                                isAssistedDialingNumber = true;
                                return newPhoneNumber.toString();
                            }
                        }
                        c = phoneNumber.charAt(0);
                        if ((isISODigit(c) || PLUS_SIGN_CHAR == c) && numberLength >= 11 && !(numberLength == 11 && '1' == c)) {
                            String iddPrefix = null;
                            String numberAfterIDDPrefix = null;
                            if (numberBeginsWithRefCountryIDDPrefixWithAdEnabled || numberBeginsWithOTAIDDPrefix) {
                                if (true == numberBeginsWithRefCountryIDDPrefixWithAdEnabled) {
                                    iddPrefix = refCountryIDDPrefix;
                                    numberAfterIDDPrefix = phoneNumber.substring(refCountryIDDPrefix.length(), phoneNumber.length());
                                } else {
                                    iddPrefix = otaCountryIDDPrefix;
                                    numberAfterIDDPrefix = phoneNumber.substring(otaCountryIDDPrefix.length(), phoneNumber.length());
                                }
                            }
                            if (PLUS_SIGN_CHAR != c || isNBPCDSupported) {
                                if (numberBeginsWithRefCountryIDDPrefixWithAdEnabled || numberBeginsWithOTAIDDPrefix) {
                                    if (numberAfterIDDPrefix.startsWith(otaCountryCountryCode)) {
                                        if (isNBPCDSupported) {
                                            newPhoneNumber.append(phoneNumber);
                                            newPhoneNumber.replace(0, iddPrefix.length() + otaCountryCountryCode.length(), PLUS_SIGN_STRING);
                                            Rlog.d(LOG_TAG, "[AssistDialingC-1] ");
                                            if (numberBeginsWithOTAIDDPrefix) {
                                                isAssistedDialingNumber = false;
                                            } else {
                                                isAssistedDialingNumber = true;
                                            }
                                            return newPhoneNumber.toString();
                                        }
                                        newPhoneNumber.append(phoneNumber);
                                        newPhoneNumber.replace(0, iddPrefix.length() + otaCountryCountryCode.length(), otaCountryNDDPrefix);
                                        Rlog.d(LOG_TAG, "[AssistDialingC-2] ");
                                        if (numberBeginsWithOTAIDDPrefix) {
                                            isAssistedDialingNumber = false;
                                        } else {
                                            isAssistedDialingNumber = true;
                                        }
                                        return newPhoneNumber.toString();
                                    } else if (numberBeginsWithRefCountryIDDPrefixWithAdEnabled) {
                                        newPhoneNumber.append(otaCountryIDDPrefix);
                                        newPhoneNumber.append(numberAfterIDDPrefix);
                                        Rlog.d(LOG_TAG, "[AssistDialingC-3] ");
                                        isAssistedDialingNumber = true;
                                        return newPhoneNumber.toString();
                                    }
                                }
                            } else if (phoneNumber.substring(1, phoneNumber.length()).startsWith(otaCountryCountryCode)) {
                                newPhoneNumber.append(phoneNumber);
                                newPhoneNumber.replace(0, otaCountryCountryCode.length() + 1, otaCountryNDDPrefix);
                                Rlog.d(LOG_TAG, "[AssistDialingB-2] ");
                                isAssistedDialingNumber = false;
                                return newPhoneNumber.toString();
                            } else {
                                newPhoneNumber.append(phoneNumber);
                                newPhoneNumber.replace(0, 1, otaCountryIDDPrefix);
                                Rlog.d(LOG_TAG, "[AssistDialingB-3] ");
                                isAssistedDialingNumber = false;
                                return newPhoneNumber.toString();
                            }
                        }
                    }
                    if (isGSMRegistered) {
                        if (numberBeginsWithRefCountryIDDPrefixWithAdEnabled) {
                            newPhoneNumber.append(phoneNumber);
                            newPhoneNumber.replace(0, refCountryIDDPrefix.length(), PLUS_SIGN_STRING);
                            isAssistedDialingNumber = true;
                            return newPhoneNumber.toString();
                        } else if (enableAssistedDialing && phoneNumber.startsWith("+011")) {
                            newPhoneNumber.append(PLUS_SIGN_STRING);
                            newPhoneNumber.append(phoneNumber.substring(4));
                            isAssistedDialingNumber = true;
                            return newPhoneNumber.toString();
                        }
                    }
                    Rlog.d(LOG_TAG, "[AssistDialingD-1] ");
                    isAssistedDialingNumber = false;
                    return phoneNumber;
                }
                Rlog.d(LOG_TAG, "Problem in retrieving Assisted db params, Returning original number");
                isAssistedDialingNumber = false;
                return phoneNumber;
            }
            Rlog.d(LOG_TAG, "Assisted Dialing PhoneNumber is FAILED: " + ch);
            isAssistedDialingNumber = false;
            return phoneNumber;
        } catch (Exception e) {
            Rlog.d(LOG_TAG, "Cannot assist: " + e);
            isAssistedDialingNumber = false;
            return phoneNumber;
        }
    }

    private static Cursor getOtaCountry(Context context, boolean useSharedPreference) {
        String spOtaCountryMcc = PreferenceManager.getDefaultSharedPreferences(context).getString(OTA_COUNTRY_MCC_KEY, null);
        ContentResolver otacr = context.getContentResolver();
        if (!useSharedPreference || spOtaCountryMcc == null) {
            return otacr.query(OTA_COUNTRY_URI, null, null, null, null);
        }
        return otacr.query(MCC_OTA_URI, null, "mcc=?", new String[]{spOtaCountryMcc}, null);
    }

    private static boolean startWithCountryCode(String number, Context context) {
        if (number.length() == 12 && (number.startsWith("7") || number.startsWith("20") || number.startsWith("65") || number.startsWith("90"))) {
            Rlog.d(LOG_TAG, "length 12 - 7,20,65,90 is detected");
            return false;
        }
        if (number.length() >= 11) {
            if (number.startsWith(SmartFaceManager.PAGE_BOTTOM)) {
                Rlog.d(LOG_TAG, "US country code is detected with more than 11 digits");
                return false;
            }
            mCursorContry = context.getContentResolver().query(MCC_OTA_URI, null, null, null, null);
            mCursorContry.moveToFirst();
            while (mCursorContry != null && !mCursorContry.isAfterLast()) {
                if (number.startsWith(mCursorContry.getString(6))) {
                    Rlog.d(LOG_TAG, "contry code is detected");
                    mCursorContry.close();
                    return true;
                }
                mCursorContry.moveToNext();
            }
            if (mCursorContry != null) {
                mCursorContry.close();
            }
        }
        return false;
    }

    public static String convertSMSDestinationAddress(String number, Context context, int fixedNetwork) {
        String phoneNumber = extractNetworkPortion(number);
        int numberLength = phoneNumber.length();
        char c = phoneNumber.charAt(0);
        if (isISODigit(c) || '+' == c) {
            Rlog.d(LOG_TAG, "SMS Destination Number is OK " + c);
            try {
                if (!retrieveAssistedParams(phoneNumber, context)) {
                    return phoneNumber;
                }
                String nanpStr;
                String numberAfterIDDPrefix;
                String iddPrefix;
                int findIDDLen;
                String newStr;
                StringBuilder newPhoneNumber = new StringBuilder(128);
                boolean numberBeginsWithOTAIDDPrefix = phoneNumber.startsWith(otaCountryIDDPrefix);
                boolean numberBeginsWithNonUSIDDPrefix = !phoneNumber.startsWith("011");
                Rlog.d(LOG_TAG, "SMS Destination numberLength: " + numberLength + " numberBeginsWithOTAIDDPrefix: " + numberBeginsWithOTAIDDPrefix + " numberBeginsWithNonUSIDDPrefix: " + numberBeginsWithNonUSIDDPrefix + " otaCountryIDDPrefix: " + otaCountryIDDPrefix + " number : " + phoneNumber.substring(0, 5) + "**********");
                switch (fixedNetwork) {
                    case 1:
                        isCDMARegistered = true;
                        isGSMRegistered = false;
                        isNetRoaming = false;
                        break;
                    case 2:
                        isCDMARegistered = true;
                        isGSMRegistered = false;
                        isNetRoaming = true;
                        break;
                    case 3:
                        isCDMARegistered = false;
                        isGSMRegistered = true;
                        break;
                }
                if (isCDMARegistered) {
                    if (isNetRoaming) {
                        Rlog.d(LOG_TAG, "Address Rule in CDMA Internatinal Roaming");
                        if ((isISODigit(c) || '+' == c) && numberLength >= 11 && !(numberLength == 11 && '1' == c)) {
                            nanpStr = phoneNumber.substring(numberLength - 11);
                            numberAfterIDDPrefix = null;
                            if (numberBeginsWithOTAIDDPrefix) {
                                iddPrefix = otaCountryIDDPrefix;
                                numberAfterIDDPrefix = phoneNumber.substring(otaCountryIDDPrefix.length(), phoneNumber.length());
                            }
                            if (numberBeginsWithOTAIDDPrefix) {
                                findIDDLen = otaCountryIDDPrefix.length();
                                newStr = phoneNumber.substring(findIDDLen);
                                if (isOneNanp(nanpStr) && phoneNumber.length() == findIDDLen + 11) {
                                    newPhoneNumber.append(newStr);
                                } else if (startWithCountryCode(numberAfterIDDPrefix, context)) {
                                    Rlog.d(LOG_TAG, "Found Country Code after IDD");
                                    newPhoneNumber.append(phoneNumber);
                                    newPhoneNumber.replace(0, findIDDLen, "011");
                                } else {
                                    Rlog.d(LOG_TAG, "No Condition");
                                    newPhoneNumber.append(phoneNumber);
                                }
                                return newPhoneNumber.toString();
                            } else if ('+' == c && !isNBPCDSupported) {
                                newStr = phoneNumber.substring(1);
                                if (isOneNanp(newStr) && phoneNumber.length() == 12) {
                                    newPhoneNumber.append(newStr);
                                } else if (startWithCountryCode(newStr, context)) {
                                    newPhoneNumber.append("011");
                                    newPhoneNumber.append(newStr);
                                } else {
                                    Rlog.d(LOG_TAG, "1NANP is not matched");
                                    newPhoneNumber.append(phoneNumber);
                                }
                                return newPhoneNumber.toString();
                            } else if (startWithCountryCode(phoneNumber, context)) {
                                newPhoneNumber.append("011");
                                newPhoneNumber.append(phoneNumber);
                                return newPhoneNumber.toString();
                            }
                        }
                    }
                    Rlog.d(LOG_TAG, "Address Rule in VZW Network");
                    if (!isISODigit(c) || !numberBeginsWithNonUSIDDPrefix || numberLength < 11 || ((numberLength == 11 && '1' == c) || !startWithCountryCode(phoneNumber, context))) {
                        return cdmaCheckAndProcessPlusCodeByNumberFormat(phoneNumber, 1, 1);
                    }
                    newPhoneNumber.append("011");
                    newPhoneNumber.append(phoneNumber);
                    return newPhoneNumber.toString();
                }
                if (isGSMRegistered) {
                    Rlog.d(LOG_TAG, "Address Rule in GSM/UMTS");
                    if ((isISODigit(c) || '+' == c) && numberLength >= 11 && !(numberLength == 11 && '1' == c)) {
                        nanpStr = phoneNumber.substring(numberLength - 11);
                        numberAfterIDDPrefix = null;
                        if (numberBeginsWithOTAIDDPrefix) {
                            iddPrefix = otaCountryIDDPrefix;
                            numberAfterIDDPrefix = phoneNumber.substring(otaCountryIDDPrefix.length(), phoneNumber.length());
                        }
                        if (numberBeginsWithOTAIDDPrefix) {
                            findIDDLen = otaCountryIDDPrefix.length();
                            if (isOneNanp(phoneNumber.substring(findIDDLen)) && phoneNumber.length() == findIDDLen + 11) {
                                newPhoneNumber.append(phoneNumber);
                                newPhoneNumber.replace(0, otaCountryIDDPrefix.length(), PLUS_SIGN_STRING);
                                return newPhoneNumber.toString();
                            } else if (startWithCountryCode(numberAfterIDDPrefix, context)) {
                                newPhoneNumber.append(phoneNumber);
                                newPhoneNumber.replace(0, otaCountryIDDPrefix.length(), "011");
                                return newPhoneNumber.toString();
                            } else {
                                Rlog.d(LOG_TAG, "No condition is matched in IDD");
                                newPhoneNumber.append(phoneNumber);
                                return newPhoneNumber.toString();
                            }
                        } else if ('+' == c) {
                            String iddStr = phoneNumber.substring(1);
                            if (isOneNanp(nanpStr) && phoneNumber.length() == 12) {
                                newPhoneNumber.append(phoneNumber);
                            } else if (startWithCountryCode(iddStr, context)) {
                                newStr = phoneNumber.substring(1);
                                newPhoneNumber.append("011");
                                newPhoneNumber.append(newStr);
                            } else if (iddStr.startsWith("011")) {
                                newPhoneNumber.append(iddStr);
                            } else {
                                Rlog.d(LOG_TAG, "No condition is matched in '+'");
                                newPhoneNumber.append(phoneNumber);
                            }
                            return newPhoneNumber.toString();
                        } else if (startWithCountryCode(phoneNumber, context)) {
                            newPhoneNumber.append("011");
                            newPhoneNumber.append(phoneNumber);
                            return newPhoneNumber.toString();
                        }
                    }
                }
                Rlog.d(LOG_TAG, "Can't find any match in this number");
                return phoneNumber;
            } catch (Exception e) {
                Rlog.d(LOG_TAG, "Cannot convert: " + e);
                return phoneNumber;
            }
        }
        Rlog.d(LOG_TAG, "SMS Destination Number might be email address" + c);
        return phoneNumber;
    }

    public static String extractNetworkPortionchangePlusCode(Context context, String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        int len = phoneNumber.length();
        StringBuilder ret = new StringBuilder(len);
        boolean firstCharPause = false;
        for (int i = 0; i < len; i++) {
            char c = phoneNumber.charAt(i);
            if (isDialable(c)) {
                ret.append(c);
            } else if (isStartsPostDial(c)) {
                if (i == 0) {
                    firstCharPause = true;
                }
                if (firstCharPause) {
                    return processPlusCodeWithinNanp(context, ret.toString());
                }
                return phoneNumber;
            }
        }
        if (firstCharPause) {
            return phoneNumber;
        }
        return processPlusCodeWithinNanp(context, ret.toString());
    }

    private static String processPlusCodeWithinNanp(Context context, String networkDialStr) {
        String retStr = null;
        String newStr = null;
        if (networkDialStr != null) {
            retStr = networkDialStr;
            newStr = networkDialStr.substring(1);
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sp.getString(Settings$System.COUNTRY_CODE, "011");
        boolean isChecked = sp.getBoolean("toggle_country_name", true);
        String sCountry = SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_ISO_COUNTRY);
        if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_ConvertPlusPrefixNumberToMnoCode")) {
            if ("cn".equals(sCountry) || "nz".equals(sCountry) || "mx".equals(sCountry) || "mo".equals(sCountry) || "il".equals(sCountry) || "in".equals(sCountry) || "pe".equals(sCountry) || "vn".equals(sCountry) || "ve".equals(sCountry) || "bd".equals(sCountry)) {
                value = NANP_IDP_STRING_00;
            } else if ("th".equals(sCountry) || "hk".equals(sCountry) || "id".equals(sCountry)) {
                value = NANP_IDP_STRING_001;
            } else if ("mp".equals(sCountry) || "bm".equals(sCountry) || "us".equals(sCountry) || "bs".equals(sCountry)) {
                value = "011";
            } else if ("tw".equals(sCountry)) {
                value = NANP_IDP_STRING_005;
            } else if ("kr".equals(sCountry)) {
                value = NANP_IDP_STRING_00700;
            } else if ("jp".equals(sCountry)) {
                value = NANP_IDP_STRING_010;
            }
        }
        if (networkDialStr == null || networkDialStr.charAt(0) != PLUS_SIGN_CHAR || networkDialStr.length() <= 1) {
            return retStr;
        }
        if (isOneNanp(newStr) && isDefaultPlusCode(value) && isChecked) {
            return newStr;
        }
        if ("jp".equals(sCountry) && networkDialStr.charAt(1) == '8' && networkDialStr.charAt(2) == '1') {
            return networkDialStr.replaceFirst("[+]81", SmartFaceManager.PAGE_MIDDLE);
        }
        if ("jp".equals(sCountry)) {
            return networkDialStr.replaceFirst("[+]", value);
        }
        return retStr;
    }

    private static boolean isDefaultPlusCode(String dialStr) {
        if (dialStr.length() == 3 && dialStr.charAt(0) == '0' && dialStr.charAt(1) == '1' && dialStr.charAt(2) == '1') {
            return true;
        }
        return false;
    }

    public static String processCLIRDigitsWithinNetworkDial(Context context, String sDialNumber) {
        boolean isCheck;
        String sNetworkDial = extractNetworkPortionchangePlusCode(context, sDialNumber);
        boolean isEmergencyCall = isEmergencyNumber(sDialNumber);
        if (Settings$System.getInt(context.getContentResolver(), "button_clir_key", 1) == 1) {
            isCheck = true;
        } else {
            isCheck = false;
        }
        String sCountry = SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_ISO_COUNTRY);
        if (isCheck || isEmergencyCall || !"jp".equals(sCountry)) {
            return sNetworkDial;
        }
        if (sNetworkDial.charAt(0) == '1' && sNetworkDial.charAt(1) == '8' && sNetworkDial.charAt(2) == '4') {
            return sNetworkDial;
        }
        if (sNetworkDial.charAt(0) == '1' && sNetworkDial.charAt(1) == '8' && sNetworkDial.charAt(2) == '6') {
            return sNetworkDial;
        }
        sNetworkDial = "184" + sNetworkDial;
        Rlog.d(LOG_TAG, "Add 184 to dialnumber");
        return sNetworkDial;
    }
}
