/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalStateException
 *  java.lang.Long
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.Arrays
 *  java.util.BitSet
 *  java.util.Calendar
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 */
package com.americanexpress.sdkmodulelib.util;

import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.model.token.DataGroup;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.model.token.TokenMetaInfoParser;
import com.americanexpress.sdkmodulelib.payment.SessionManager;
import com.americanexpress.sdkmodulelib.storage.StorageFactory;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.americanexpress.sdkmodulelib.util.AxpeLogUtils;
import com.americanexpress.sdkmodulelib.util.Constants;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TokenDataParser {
    public static TokenDataStatus buildTokenDataStatus(String string, String string2, String string3) {
        TokenDataStatus tokenDataStatus = new TokenDataStatus();
        tokenDataStatus.setReasonCode(string);
        tokenDataStatus.setDetailCode(string2);
        tokenDataStatus.setDetailMessage(string3);
        return tokenDataStatus;
    }

    public static TokenDataStatus buildTokenDataStatus(String[] arrstring) {
        return TokenDataParser.buildTokenDataStatus(arrstring[0], arrstring[1], arrstring[2]);
    }

    public static TokenDataStatus buildTokenDataStatus(String[] arrstring, int n2) {
        String string = arrstring[0];
        String string2 = arrstring[1];
        String string3 = arrstring[2];
        if (n2 != 0) {
            string3 = string3 + n2;
        }
        return TokenDataParser.buildTokenDataStatus(string, string2, string3);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String calculateCVMResult(boolean bl, boolean bl2, boolean bl3) {
        String[] arrstring = new String[]{"00", "00", "00"};
        BitSet bitSet = Util.byteArray2BitSet(Util.fromHexString(arrstring[0]));
        if (bl) {
            bitSet.set(0, true);
            arrstring[0] = Util.byteArrayToHexString(bitSet.toByteArray());
        } else {
            bitSet.set(5, true);
            bitSet.set(4, true);
            bitSet.set(3, true);
            bitSet.set(2, true);
            bitSet.set(1, true);
            bitSet.set(0, true);
            arrstring[0] = Util.byteArrayToHexString(bitSet.toByteArray());
        }
        BitSet bitSet2 = Util.byteArray2BitSet(Util.fromHexString(arrstring[1]));
        if (bl2) {
            bitSet2.set(1, true);
            bitSet2.set(0, true);
            arrstring[1] = Util.byteArrayToHexString(bitSet2.toByteArray());
        }
        BitSet bitSet3 = Util.byteArray2BitSet(Util.fromHexString(arrstring[2]));
        if (bl3) {
            bitSet3.set(1, true);
            arrstring[2] = Util.byteArrayToHexString(bitSet3.toByteArray());
            return TokenDataParser.concatStringArray(arrstring);
        }
        bitSet3.set(0, true);
        arrstring[2] = Util.byteArrayToHexString(bitSet3.toByteArray());
        return TokenDataParser.concatStringArray(arrstring);
    }

    private static String concatStringArray(String[] arrstring) {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = arrstring.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            stringBuilder.append(arrstring[i2]);
        }
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String constructCVRBasedOnAuthenticationType(String string, boolean bl) {
        AxpeLogUtils.log("currentCvr=" + string);
        char[] arrc = string.trim().toCharArray();
        Object[] arrobject = new String[]{arrc[0] + "" + arrc[1], arrc[2] + "" + arrc[3], arrc[4] + "" + arrc[5], arrc[6] + "" + arrc[7]};
        AxpeLogUtils.log("cvrArray len=" + arrobject.length + " cvrArray=" + Arrays.toString((Object[])arrobject));
        BitSet bitSet = Util.byteArray2BitSet(Util.fromHexString((String)arrobject[1]));
        if (bl) {
            bitSet.set(2, false);
            bitSet.set(1, true);
            arrobject[1] = Util.byteArrayToHexString(bitSet.toByteArray());
        } else {
            bitSet.set(2, false);
            bitSet.set(1, false);
            arrobject[1] = Util.byteArrayToHexString(bitSet.toByteArray());
        }
        BitSet bitSet2 = Util.byteArray2BitSet(Util.fromHexString((String)arrobject[2]));
        bitSet2.set(0, true);
        arrobject[2] = Util.byteArrayToHexString(bitSet2.toByteArray());
        AxpeLogUtils.log("update cvrArray = " + Arrays.toString((Object[])arrobject));
        return TokenDataParser.concatStringArray((String[])arrobject).toUpperCase();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String constructCVRBasedOnTerminalConnectivity(String string, boolean bl) {
        AxpeLogUtils.log("constructCVRBasedOnTerminalConnectivity --> currentCvr=" + string);
        char[] arrc = string.toCharArray();
        Object[] arrobject = new String[]{arrc[0] + "" + arrc[1], arrc[2] + "" + arrc[3], arrc[4] + "" + arrc[5], arrc[6] + "" + arrc[7]};
        AxpeLogUtils.log("cvrArray len=" + arrobject.length + " cvrArray=" + Arrays.toString((Object[])arrobject));
        BitSet bitSet = Util.byteArray2BitSet(Util.fromHexString((String)arrobject[1]));
        if (bl) {
            bitSet.set(5, true);
            bitSet.set(4, false);
            arrobject[1] = Util.byteArrayToHexString(bitSet.toByteArray());
            do {
                return TokenDataParser.concatStringArray((String[])arrobject).toUpperCase();
                break;
            } while (true);
        }
        bitSet.set(5, false);
        bitSet.set(4, false);
        arrobject[1] = Util.byteArrayToHexString(bitSet.toByteArray());
        return TokenDataParser.concatStringArray((String[])arrobject).toUpperCase();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int getExpressPayVersion(String string) {
        int n2 = 3;
        BitSet bitSet = Util.byteArray2BitSet(Util.fromHexString(string));
        boolean bl = bitSet.get(7);
        boolean bl2 = bitSet.get(6);
        boolean bl3 = bitSet.get(n2);
        if (!bl && !bl2) {
            if (!bl3) return 1;
        }
        if (!bl && !bl2 && bl3) {
            return 1;
        }
        if (bl && !bl2) {
            if (!bl3) return 2;
        }
        if (bl && !bl2 && bl3) {
            return 2;
        }
        if (!bl && bl2) {
            if (!bl3) return n2;
        }
        if (!bl && bl2) {
            if (bl3) return n2;
        }
        if (bl && bl2) {
            if (!bl3) return n2;
        }
        if (!bl) return 0;
        if (!bl2) return 0;
        if (bl3) return n2;
        return 0;
    }

    public static boolean isCVMRequired(String string) {
        boolean bl;
        block3 : {
            block2 : {
                BitSet bitSet = Util.byteArray2BitSet(Util.fromHexString(string));
                boolean bl2 = bitSet.get(7);
                boolean bl3 = bitSet.get(6);
                boolean bl4 = bitSet.get(3);
                if (!bl2 && bl3 && bl4) break block2;
                bl = false;
                if (!bl2) break block3;
                bl = false;
                if (!bl3) break block3;
                bl = false;
                if (!bl4) break block3;
            }
            bl = true;
        }
        return bl;
    }

    public static boolean isClientVersionUpdateRequired(ParsedTokenRecord parsedTokenRecord) {
        return 1 < ((TokenMetaInfoParser)parsedTokenRecord.getDataGroups().get(TokenMetaInfoParser.class)).getTokenDataMajorVersion();
    }

    public static boolean isEMV(String string) {
        boolean bl;
        block3 : {
            block2 : {
                BitSet bitSet = Util.byteArray2BitSet(Util.fromHexString(string));
                boolean bl2 = bitSet.get(7);
                boolean bl3 = bitSet.get(6);
                boolean bl4 = bitSet.get(3);
                if (bl2 && bl3 && bl4 || bl2 && bl3 && !bl4 || bl2 && !bl3 && bl4) break block2;
                bl = false;
                if (!bl2) break block3;
                bl = false;
                if (bl3) break block3;
                bl = false;
                if (bl4) break block3;
            }
            bl = true;
        }
        return bl;
    }

    public static boolean isExpired(long l2) {
        long l3 = 1000L * l2;
        return Calendar.getInstance().getTimeInMillis() > l3;
    }

    public static boolean isExpired(String string) {
        long l2;
        try {
            l2 = Long.parseLong((String)string);
        }
        catch (NumberFormatException numberFormatException) {
            return true;
        }
        return TokenDataParser.isExpired(l2);
    }

    public static boolean isTerminalGenACRequestConnectivityOffline(String string) {
        boolean bl = "00".equals((Object)string.substring(4, 6));
        boolean bl2 = false;
        if (bl) {
            bl2 = true;
        }
        return bl2;
    }

    public static boolean isTerminalTypeOffline(String string) {
        BitSet bitSet = Util.byteArray2BitSet(Util.fromHexString(string));
        boolean bl = bitSet.get(0);
        boolean bl2 = bitSet.get(1);
        boolean bl3 = bitSet.get(2);
        return bl && bl2 && !bl3 || !bl && bl2 && bl3;
    }

    public static boolean isUpdateTokenData(ParsedTokenRecord parsedTokenRecord) {
        return 1 > ((TokenMetaInfoParser)parsedTokenRecord.getDataGroups().get(TokenMetaInfoParser.class)).getTokenDataMajorVersion();
    }

    public static TokenDataRecord loadFromStorage(String string) {
        try {
            TokenDataRecord tokenDataRecord = StorageFactory.getStorageManager().fetch(string);
            SessionManager.createSession().setTokenDataRecord(tokenDataRecord);
            return tokenDataRecord;
        }
        catch (Exception exception) {
            throw new Exception("Failed to fetch data from storage");
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static ParsedTokenRecord parseToken(String string) {
        ParsedTokenRecord parsedTokenRecord = new ParsedTokenRecord();
        HashMap hashMap = new HashMap();
        TokenMetaInfoParser tokenMetaInfoParser = new TokenMetaInfoParser();
        tokenMetaInfoParser.init(string);
        parsedTokenRecord.setIsTokenDataContainsEMV(tokenMetaInfoParser.isTokenDataContainsEMV());
        hashMap.put(TokenMetaInfoParser.class, (Object)tokenMetaInfoParser);
        List list = tokenMetaInfoParser.isTokenDataContainsEMV() ? Arrays.asList((Object[])Constants.DGI_GROUP_PARSERS_MAG_EMV) : Arrays.asList((Object[])Constants.DGI_GROUP_PARSERS_MAG);
        Iterator iterator = list.iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            DataGroup dataGroup;
            Class class_ = (Class)iterator.next();
            if (!hashMap.containsKey((Object)class_)) {
                dataGroup = (DataGroup)class_.newInstance();
                dataGroup.init(string);
                hashMap.put((Object)class_, (Object)dataGroup);
            } else {
                dataGroup = (DataGroup)hashMap.get((Object)class_);
            }
            int n3 = dataGroup.isDataGroupMalformed() ? n2 + 1 : n2;
            dataGroup.setTokenDataBlob(null);
            n2 = n3;
        }
        parsedTokenRecord.setDataGroups((HashMap<Class, DataGroup>)hashMap);
        if (n2 != 0 && list.size() != n2) {
            parsedTokenRecord.setTokenPartiallyMalformed(true);
        }
        if (list.size() == n2) {
            parsedTokenRecord.setTokenMalformed(true);
        }
        parsedTokenRecord.setTokenDataBlob(string);
        return parsedTokenRecord;
    }

    public static /* varargs */ void throwExceptionIfEmpty(String ... arrstring) {
        for (String string : arrstring) {
            if (string != null && string.length() != 0) continue;
            throw new IllegalStateException();
        }
    }

    public static String updateCVR(String string, String string2, String string3) {
        return string.replace((CharSequence)string2, (CharSequence)string3);
    }
}

