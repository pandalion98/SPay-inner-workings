package com.samsung.android.telephony;

import android.telephony.Rlog;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.GsmAlphabet.TextEncodingDetails;

public class GsmAlphabetCode {
    public static final byte GSM_EXTENDED_ESCAPE = (byte) 27;
    private static final String TAG = "GsmAlphabetCode";
    public static final int UDH_SEPTET_COST_CONCATENATED_MESSAGE = 6;
    public static final int UDH_SEPTET_COST_LENGTH = 1;
    public static final int UDH_SEPTET_COST_ONE_SHIFT_TABLE = 4;
    public static final int UDH_SEPTET_COST_TWO_SHIFT_TABLES = 7;
    public static GsmAlphabet mWrappedGsmAlphabet;

    public static class TextEncoding extends TextEncodingDetails {
    }

    private GsmAlphabetCode() {
    }

    private GsmAlphabetCode(GsmAlphabet ga) {
        mWrappedGsmAlphabet = ga;
    }

    static {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        gsmAlphabet = mWrappedGsmAlphabet;
        gsmAlphabet = mWrappedGsmAlphabet;
        gsmAlphabet = mWrappedGsmAlphabet;
        gsmAlphabet = mWrappedGsmAlphabet;
    }

    public static int charToGsm(char c) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.charToGsm(c);
    }

    public static int charToGsm(char c, boolean throwException) throws Exception {
        Rlog.d(TAG, "GsmAlphabetCode: charToGsm");
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.charToGsm(c, throwException);
    }

    public static int charToGsmExtended(char c) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.charToGsmExtended(c);
    }

    public static char gsmToChar(int gsmChar) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.gsmToChar(gsmChar);
    }

    public static char gsmExtendedToChar(int gsmChar) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.gsmExtendedToChar(gsmChar);
    }

    public static byte[] stringToGsm7BitPackedWithHeader(String data, byte[] header) throws EncodeException {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.stringToGsm7BitPackedWithHeader(data, header);
    }

    public static byte[] stringToGsm7BitPackedWithHeader(String data, byte[] header, int languageTable, int languageShiftTable) throws EncodeException {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.stringToGsm7BitPackedWithHeader(data, header, languageTable, languageShiftTable);
    }

    public static byte[] stringToGsm7BitPacked(String data) throws EncodeException {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.stringToGsm7BitPacked(data);
    }

    public static byte[] stringToGsm7BitPacked(String data, int languageTable, int languageShiftTable) throws EncodeException {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.stringToGsm7BitPacked(data, languageTable, languageShiftTable);
    }

    public static byte[] stringToGsm7BitPacked(String data, int startingSeptetOffset, boolean throwException, int languageTable, int languageShiftTable) throws EncodeException {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.stringToGsm7BitPacked(data, startingSeptetOffset, throwException, languageTable, languageShiftTable);
    }

    public static String gsm7BitPackedToString(byte[] pdu, int offset, int lengthSeptets) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.gsm7BitPackedToString(pdu, offset, lengthSeptets);
    }

    public static String gsm7BitPackedToString(byte[] pdu, int offset, int lengthSeptets, int numPaddingBits, int languageTable, int shiftTable) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.gsm7BitPackedToString(pdu, offset, lengthSeptets, numPaddingBits, languageTable, shiftTable);
    }

    public static String gsm8BitUnpackedToString(byte[] data, int offset, int length) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.gsm8BitUnpackedToString(data, offset, length);
    }

    public static String gsm8BitUnpackedToString(byte[] data, int offset, int length, String characterset) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.gsm8BitUnpackedToString(data, offset, length, characterset);
    }

    public static byte[] stringToGsm8BitPacked(String s) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.stringToGsm8BitPacked(s);
    }

    public static void stringToGsm8BitUnpackedField(String s, byte[] dest, int offset, int length) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        GsmAlphabet.stringToGsm8BitUnpackedField(s, dest, offset, length);
    }

    public static int countGsmSeptets(char c) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.countGsmSeptets(c);
    }

    public static int countGsmSeptets(char c, boolean throwsException) throws EncodeException {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.countGsmSeptets(c, throwsException);
    }

    public static boolean isGsmSeptets(char c) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.isGsmSeptets(c);
    }

    public static int countGsmSeptetsUsingTables(CharSequence s, boolean use7bitOnly, int languageTable, int languageShiftTable) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.countGsmSeptetsUsingTables(s, use7bitOnly, languageTable, languageShiftTable);
    }

    public static TextEncodingDetails countGsmSeptets(CharSequence s, boolean use7bitOnly) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return (TextEncoding) GsmAlphabet.countGsmSeptets(s, use7bitOnly);
    }

    public static int findGsmSeptetLimitIndex(String s, int start, int limit, int langTable, int langShiftTable) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.findGsmSeptetLimitIndex(s, start, limit, langTable, langShiftTable);
    }

    public static synchronized int[] getEnabledSingleShiftTables() {
        int[] enabledSingleShiftTables;
        synchronized (GsmAlphabetCode.class) {
            GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
            enabledSingleShiftTables = GsmAlphabet.getEnabledSingleShiftTables();
        }
        return enabledSingleShiftTables;
    }

    public static synchronized int[] getEnabledLockingShiftTables() {
        int[] enabledLockingShiftTables;
        synchronized (GsmAlphabetCode.class) {
            GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
            enabledLockingShiftTables = GsmAlphabet.getEnabledLockingShiftTables();
        }
        return enabledLockingShiftTables;
    }

    public static String gsm7BitPackedToString(byte[] pdu, int offset, int lengthSeptets, int numPaddingBits) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.gsm7BitPackedToString(pdu, offset, lengthSeptets, numPaddingBits);
    }

    public static char convertEachCharacter(char c) {
        Rlog.d(TAG, "GsmAlphabetCode: convertEachCharacter");
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return GsmAlphabet.convertEachCharacter(c);
    }

    public static TextEncodingDetails CountGsmSeptetsWithEmail(CharSequence s, boolean use7bitOnly, int maxEmailLen) {
        GsmAlphabet gsmAlphabet = mWrappedGsmAlphabet;
        return (TextEncoding) GsmAlphabet.CountGsmSeptetsWithEmail(s, use7bitOnly, maxEmailLen);
    }
}
