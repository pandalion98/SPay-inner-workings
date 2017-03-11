package com.americanexpress.sdkmodulelib.util;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.model.token.DataGroup;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.model.token.TokenMetaInfoParser;
import com.americanexpress.sdkmodulelib.payment.SessionManager;
import com.americanexpress.sdkmodulelib.storage.StorageFactory;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class TokenDataParser {
    public static ParsedTokenRecord parseToken(String str) {
        ParsedTokenRecord parsedTokenRecord = new ParsedTokenRecord();
        HashMap hashMap = new HashMap();
        TokenMetaInfoParser tokenMetaInfoParser = new TokenMetaInfoParser();
        tokenMetaInfoParser.init(str);
        parsedTokenRecord.setIsTokenDataContainsEMV(tokenMetaInfoParser.isTokenDataContainsEMV());
        hashMap.put(TokenMetaInfoParser.class, tokenMetaInfoParser);
        List asList;
        if (tokenMetaInfoParser.isTokenDataContainsEMV()) {
            asList = Arrays.asList(Constants.DGI_GROUP_PARSERS_MAG_EMV);
        } else {
            asList = Arrays.asList(Constants.DGI_GROUP_PARSERS_MAG);
        }
        int i = 0;
        for (Class cls : r2) {
            DataGroup dataGroup;
            int i2;
            if (hashMap.containsKey(cls)) {
                dataGroup = (DataGroup) hashMap.get(cls);
            } else {
                dataGroup = (DataGroup) cls.newInstance();
                dataGroup.init(str);
                hashMap.put(cls, dataGroup);
            }
            if (dataGroup.isDataGroupMalformed()) {
                i2 = i + 1;
            } else {
                i2 = i;
            }
            dataGroup.setTokenDataBlob(null);
            i = i2;
        }
        parsedTokenRecord.setDataGroups(hashMap);
        if (!(i == 0 || r2.size() == i)) {
            parsedTokenRecord.setTokenPartiallyMalformed(true);
        }
        if (r2.size() == i) {
            parsedTokenRecord.setTokenMalformed(true);
        }
        parsedTokenRecord.setTokenDataBlob(str);
        return parsedTokenRecord;
    }

    public static TokenDataStatus buildTokenDataStatus(String str, String str2, String str3) {
        TokenDataStatus tokenDataStatus = new TokenDataStatus();
        tokenDataStatus.setReasonCode(str);
        tokenDataStatus.setDetailCode(str2);
        tokenDataStatus.setDetailMessage(str3);
        return tokenDataStatus;
    }

    public static TokenDataStatus buildTokenDataStatus(String[] strArr) {
        return buildTokenDataStatus(strArr[0], strArr[1], strArr[2]);
    }

    public static TokenDataStatus buildTokenDataStatus(String[] strArr, int i) {
        String str = strArr[0];
        String str2 = strArr[1];
        String str3 = strArr[2];
        if (i != 0) {
            str3 = str3 + i;
        }
        return buildTokenDataStatus(str, str2, str3);
    }

    public static boolean isClientVersionUpdateRequired(ParsedTokenRecord parsedTokenRecord) {
        if (1 < ((TokenMetaInfoParser) parsedTokenRecord.getDataGroups().get(TokenMetaInfoParser.class)).getTokenDataMajorVersion()) {
            return true;
        }
        return false;
    }

    public static boolean isUpdateTokenData(ParsedTokenRecord parsedTokenRecord) {
        if (1 > ((TokenMetaInfoParser) parsedTokenRecord.getDataGroups().get(TokenMetaInfoParser.class)).getTokenDataMajorVersion()) {
            return true;
        }
        return false;
    }

    public static boolean isExpired(long j) {
        if (Calendar.getInstance().getTimeInMillis() > 1000 * j) {
            return true;
        }
        return false;
    }

    public static boolean isExpired(String str) {
        try {
            return isExpired(Long.parseLong(str));
        } catch (NumberFormatException e) {
            return true;
        }
    }

    public static void throwExceptionIfEmpty(String... strArr) {
        for (String str : strArr) {
            if (str == null || str.length() == 0) {
                throw new IllegalStateException();
            }
        }
    }

    public static TokenDataRecord loadFromStorage(String str) {
        try {
            TokenDataRecord fetch = StorageFactory.getStorageManager().fetch(str);
            SessionManager.createSession().setTokenDataRecord(fetch);
            return fetch;
        } catch (Exception e) {
            throw new Exception("Failed to fetch data from storage");
        }
    }

    public static boolean isEMV(String str) {
        BitSet byteArray2BitSet = Util.byteArray2BitSet(Util.fromHexString(str));
        boolean z = byteArray2BitSet.get(7);
        boolean z2 = byteArray2BitSet.get(6);
        boolean z3 = byteArray2BitSet.get(3);
        if ((!z || !z2 || !z3) && ((!z || !z2 || z3) && ((!z || z2 || !z3) && (!z || z2 || z3)))) {
            return false;
        }
        return true;
    }

    public static boolean isTerminalTypeOffline(String str) {
        BitSet byteArray2BitSet = Util.byteArray2BitSet(Util.fromHexString(str));
        boolean z = byteArray2BitSet.get(0);
        boolean z2 = byteArray2BitSet.get(1);
        boolean z3 = byteArray2BitSet.get(2);
        if (z && z2 && !z3) {
            return true;
        }
        if (!z && z2 && z3) {
            return true;
        }
        return false;
    }

    public static boolean isTerminalGenACRequestConnectivityOffline(String str) {
        if (HCEClientConstants.HEX_ZERO_BYTE.equals(str.substring(4, 6))) {
            return true;
        }
        return false;
    }

    public static String constructCVRBasedOnTerminalConnectivity(String str, boolean z) {
        AxpeLogUtils.log("constructCVRBasedOnTerminalConnectivity --> currentCvr=" + str);
        char[] toCharArray = str.toCharArray();
        String[] strArr = new String[]{toCharArray[0] + BuildConfig.FLAVOR + toCharArray[1], toCharArray[2] + BuildConfig.FLAVOR + toCharArray[3], toCharArray[4] + BuildConfig.FLAVOR + toCharArray[5], toCharArray[6] + BuildConfig.FLAVOR + toCharArray[7]};
        AxpeLogUtils.log("cvrArray len=" + strArr.length + " cvrArray=" + Arrays.toString(strArr));
        BitSet byteArray2BitSet = Util.byteArray2BitSet(Util.fromHexString(strArr[1]));
        if (z) {
            byteArray2BitSet.set(5, true);
            byteArray2BitSet.set(4, false);
            strArr[1] = Util.byteArrayToHexString(byteArray2BitSet.toByteArray());
        } else {
            byteArray2BitSet.set(5, false);
            byteArray2BitSet.set(4, false);
            strArr[1] = Util.byteArrayToHexString(byteArray2BitSet.toByteArray());
        }
        return concatStringArray(strArr).toUpperCase();
    }

    public static String constructCVRBasedOnAuthenticationType(String str, boolean z) {
        AxpeLogUtils.log("currentCvr=" + str);
        char[] toCharArray = str.trim().toCharArray();
        String[] strArr = new String[]{toCharArray[0] + BuildConfig.FLAVOR + toCharArray[1], toCharArray[2] + BuildConfig.FLAVOR + toCharArray[3], toCharArray[4] + BuildConfig.FLAVOR + toCharArray[5], toCharArray[6] + BuildConfig.FLAVOR + toCharArray[7]};
        AxpeLogUtils.log("cvrArray len=" + strArr.length + " cvrArray=" + Arrays.toString(strArr));
        BitSet byteArray2BitSet = Util.byteArray2BitSet(Util.fromHexString(strArr[1]));
        if (z) {
            byteArray2BitSet.set(2, false);
            byteArray2BitSet.set(1, true);
            strArr[1] = Util.byteArrayToHexString(byteArray2BitSet.toByteArray());
        } else {
            byteArray2BitSet.set(2, false);
            byteArray2BitSet.set(1, false);
            strArr[1] = Util.byteArrayToHexString(byteArray2BitSet.toByteArray());
        }
        byteArray2BitSet = Util.byteArray2BitSet(Util.fromHexString(strArr[2]));
        byteArray2BitSet.set(0, true);
        strArr[2] = Util.byteArrayToHexString(byteArray2BitSet.toByteArray());
        AxpeLogUtils.log("update cvrArray = " + Arrays.toString(strArr));
        return concatStringArray(strArr).toUpperCase();
    }

    public static int getExpressPayVersion(String str) {
        BitSet byteArray2BitSet = Util.byteArray2BitSet(Util.fromHexString(str));
        boolean z = byteArray2BitSet.get(7);
        boolean z2 = byteArray2BitSet.get(6);
        boolean z3 = byteArray2BitSet.get(3);
        if ((!z && !z2 && !z3) || (!z && !z2 && z3)) {
            return 1;
        }
        if ((z && !z2 && !z3) || (z && !z2 && z3)) {
            return 2;
        }
        if (!z && z2 && !z3) {
            return 3;
        }
        if (!z && z2 && z3) {
            return 3;
        }
        if (z && z2 && !z3) {
            return 3;
        }
        if (z && z2 && z3) {
            return 3;
        }
        return 0;
    }

    public static boolean isCVMRequired(String str) {
        BitSet byteArray2BitSet = Util.byteArray2BitSet(Util.fromHexString(str));
        boolean z = byteArray2BitSet.get(7);
        boolean z2 = byteArray2BitSet.get(6);
        boolean z3 = byteArray2BitSet.get(3);
        if ((!z && z2 && z3) || (z && z2 && z3)) {
            return true;
        }
        return false;
    }

    public static String calculateCVMResult(boolean z, boolean z2, boolean z3) {
        String[] strArr = new String[]{HCEClientConstants.HEX_ZERO_BYTE, HCEClientConstants.HEX_ZERO_BYTE, HCEClientConstants.HEX_ZERO_BYTE};
        BitSet byteArray2BitSet = Util.byteArray2BitSet(Util.fromHexString(strArr[0]));
        if (z) {
            byteArray2BitSet.set(0, true);
            strArr[0] = Util.byteArrayToHexString(byteArray2BitSet.toByteArray());
        } else {
            byteArray2BitSet.set(5, true);
            byteArray2BitSet.set(4, true);
            byteArray2BitSet.set(3, true);
            byteArray2BitSet.set(2, true);
            byteArray2BitSet.set(1, true);
            byteArray2BitSet.set(0, true);
            strArr[0] = Util.byteArrayToHexString(byteArray2BitSet.toByteArray());
        }
        byteArray2BitSet = Util.byteArray2BitSet(Util.fromHexString(strArr[1]));
        if (z2) {
            byteArray2BitSet.set(1, true);
            byteArray2BitSet.set(0, true);
            strArr[1] = Util.byteArrayToHexString(byteArray2BitSet.toByteArray());
        }
        byteArray2BitSet = Util.byteArray2BitSet(Util.fromHexString(strArr[2]));
        if (z3) {
            byteArray2BitSet.set(1, true);
            strArr[2] = Util.byteArrayToHexString(byteArray2BitSet.toByteArray());
        } else {
            byteArray2BitSet.set(0, true);
            strArr[2] = Util.byteArrayToHexString(byteArray2BitSet.toByteArray());
        }
        return concatStringArray(strArr);
    }

    public static String updateCVR(String str, String str2, String str3) {
        return str.replace(str2, str3);
    }

    private static String concatStringArray(String[] strArr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String append : strArr) {
            stringBuilder.append(append);
        }
        return stringBuilder.toString();
    }
}
