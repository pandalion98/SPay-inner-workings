package com.android.internal.telephony;

import android.content.res.Resources;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.SparseIntArray;
import android.util.secutil.Log;
import com.android.ims.ImsReasonInfo;
import com.android.internal.R;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class GsmAlphabet {
    public static final byte GSM_EXTENDED_ESCAPE = (byte) 27;
    private static final String TAG = "GSM";
    public static final int UDH_SEPTET_COST_CONCATENATED_MESSAGE = 6;
    public static final int UDH_SEPTET_COST_LENGTH = 1;
    public static final int UDH_SEPTET_COST_ONE_SHIFT_TABLE = 4;
    public static final int UDH_SEPTET_COST_TWO_SHIFT_TABLES = 7;
    private static final SparseIntArray charToGsm = new SparseIntArray();
    private static final SparseIntArray charToGsmExtended = new SparseIntArray();
    private static final SparseIntArray gsmExtendedToChar = new SparseIntArray();
    private static final SparseIntArray gsmToChar = new SparseIntArray();
    private static final SparseIntArray[] sCharsToGsmTables;
    private static final SparseIntArray[] sCharsToShiftTables;
    private static boolean sDisableCountryEncodingCheck = false;
    private static boolean sEnableIgnoreSpecialChar = false;
    private static int[] sEnabledLockingShiftTables;
    private static int[] sEnabledSingleShiftTables;
    private static int sGsmSpaceChar = charToGsm.get(32);
    private static int sHighestEnabledSingleShiftCode;
    private static final String[] sLanguageShiftTables = new String[]{"          \f         ^                   {}     \\            [~] |                                    €                          ", "          \f         ^                   {}     \\            [~] |      Ğ İ         Ş               ç € ğ ı         ş            ", "         ç\f         ^                   {}     \\            [~] |Á       Í     Ó     Ú           á   €   í     ó     ú          ", "     ê   ç\fÔô Áá  ΦΓ^ΩΠΨΣΘ     Ê        {}     \\            [~] |À       Í     Ó     Ú     ÃÕ    Â   €   í     ó     ú     ãõ  â", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*০১ ২৩৪৫৬৭৮৯য়ৠৡৢ{}ৣ৲৳৴৵\\৶৷৸৹৺       [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ૦૧૨૩૪૫૬૭૮૯  {}     \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ०१२३४५६७८९॒॑{}॓॔क़ख़ग़\\ज़ड़ढ़फ़य़ॠॡॢॣ॰ॱ [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ೦೧೨೩೪೫೬೭೮೯ೞೱ{}ೲ    \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ൦൧൨൩൪൫൬൭൮൯൰൱{}൲൳൴൵ൺ\\ൻർൽൾൿ       [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ୦୧୨୩୪୫୬୭୮୯ଡ଼ଢ଼{}ୟ୰ୱ  \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ੦੧੨੩੪੫੬੭੮੯ਖ਼ਗ਼{}ਜ਼ੜਫ਼ੵ \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ௦௧௨௩௪௫௬௭௮௯௳௴{}௵௶௷௸௺\\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*   ౦౧౨౩౪౫౬౭౮౯ౘౙ{}౸౹౺౻౼\\౽౾౿         [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*؀؁ ۰۱۲۳۴۵۶۷۸۹،؍{}؎؏ؐؑؒ\\ؓؔ؛؟ـْ٘٫٬ٲٳۍ[~]۔|ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          "};
    private static final String[] sLanguageTables = new String[]{"@£$¥èéùìòÇ\nØø\rÅåΔ_ΦΓΛΩΠΨΣΘΞ￿ÆæßÉ !\"#¤%&'()*+,-./0123456789:;<=>?¡ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§¿abcdefghijklmnopqrstuvwxyzäöñüà", "@£$¥€éùıòÇ\nĞğ\rÅåΔ_ΦΓΛΩΠΨΣΘΞ￿ŞşßÉ !\"#¤%&'()*+,-./0123456789:;<=>?İABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§çabcdefghijklmnopqrstuvwxyzäöñüà", "", "@£$¥êéúíóç\nÔô\rÁáΔ_ªÇÀ∞^\\€Ó|￿ÂâÊÉ !\"#º%&'()*+,-./0123456789:;<=>?ÍABCDEFGHIJKLMNOPQRSTUVWXYZÃÕÚÜ§~abcdefghijklmnopqrstuvwxyzãõ`üà", "ঁংঃঅআইঈউঊঋ\nঌ \r এঐ  ওঔকখগঘঙচ￿ছজঝঞ !টঠডঢণত)(থদ,ধ.ন0123456789:; পফ?বভমযর ল   শষসহ়ঽািীুূৃৄ  েৈ  োৌ্ৎabcdefghijklmnopqrstuvwxyzৗড়ঢ়ৰৱ", "ઁંઃઅઆઇઈઉઊઋ\nઌઍ\r એઐઑ ઓઔકખગઘઙચ￿છજઝઞ !ટઠડઢણત)(થદ,ધ.ન0123456789:; પફ?બભમયર લળ વશષસહ઼ઽાિીુૂૃૄૅ ેૈૉ ોૌ્ૐabcdefghijklmnopqrstuvwxyzૠૡૢૣ૱", "ँंःअआइईउऊऋ\nऌऍ\rऎएऐऑऒओऔकखगघङच￿छजझञ !टठडढणत)(थद,ध.न0123456789:;ऩपफ?बभमयरऱलळऴवशषसह़ऽािीुूृॄॅॆेैॉॊोौ्ॐabcdefghijklmnopqrstuvwxyzॲॻॼॾॿ", " ಂಃಅಆಇಈಉಊಋ\nಌ \rಎಏಐ ಒಓಔಕಖಗಘಙಚ￿ಛಜಝಞ !ಟಠಡಢಣತ)(ಥದ,ಧ.ನ0123456789:; ಪಫ?ಬಭಮಯರಱಲಳ ವಶಷಸಹ಼ಽಾಿೀುೂೃೄ ೆೇೈ ೊೋೌ್ೕabcdefghijklmnopqrstuvwxyzೖೠೡೢೣ", " ംഃഅആഇഈഉഊഋ\nഌ \rഎഏഐ ഒഓഔകഖഗഘങച￿ഛജഝഞ !ടഠഡഢണത)(ഥദ,ധ.ന0123456789:; പഫ?ബഭമയരറലളഴവശഷസഹ ഽാിീുൂൃൄ െേൈ ൊോൌ്ൗabcdefghijklmnopqrstuvwxyzൠൡൢൣ൹", "ଁଂଃଅଆଇଈଉଊଋ\nଌ \r ଏଐ  ଓଔକଖଗଘଙଚ￿ଛଜଝଞ !ଟଠଡଢଣତ)(ଥଦ,ଧ.ନ0123456789:; ପଫ?ବଭମଯର ଲଳ ଵଶଷସହ଼ଽାିୀୁୂୃୄ  େୈ  ୋୌ୍ୖabcdefghijklmnopqrstuvwxyzୗୠୡୢୣ", "ਁਂਃਅਆਇਈਉਊ \n  \r ਏਐ  ਓਔਕਖਗਘਙਚ￿ਛਜਝਞ !ਟਠਡਢਣਤ)(ਥਦ,ਧ.ਨ0123456789:; ਪਫ?ਬਭਮਯਰ ਲਲ਼ ਵਸ਼ ਸਹ਼ ਾਿੀੁੂ    ੇੈ  ੋੌ੍ੑabcdefghijklmnopqrstuvwxyzੰੱੲੳੴ", " ஂஃஅஆஇஈஉஊ \n  \rஎஏஐ ஒஓஔக   ஙச￿ ஜ ஞ !ட   ணத)(  , .ந0123456789:;னப ?  மயரறலளழவஶஷஸஹ  ாிீுூ   ெேை ொோௌ்ௐabcdefghijklmnopqrstuvwxyzௗ௰௱௲௹", "ఁంఃఅఆఇఈఉఊఋ\nఌ \rఎఏఐ ఒఓఔకఖగఘఙచ￿ఛజఝఞ !టఠడఢణత)(థద,ధ.న0123456789:; పఫ?బభమయరఱలళ వశషసహ ఽాిీుూృౄ ెేై ొోౌ్ౕabcdefghijklmnopqrstuvwxyzౖౠౡౢౣ", "اآبٻڀپڦتۂٿ\nٹٽ\rٺټثجځڄڃڅچڇحخد￿ڌڈډڊ !ڏڍذرڑړ)(ڙز,ږ.ژ0123456789:;ښسش?صضطظعفقکڪګگڳڱلمنںڻڼوۄەہھءیېےٍُِٗٔabcdefghijklmnopqrstuvwxyzّٰٕٖٓ"};

    private static class LanguagePairCount {
        final int languageCode;
        final int[] septetCounts;
        final int[] unencodableCounts;

        LanguagePairCount(int code) {
            this.languageCode = code;
            int maxSingleShiftCode = GsmAlphabet.sHighestEnabledSingleShiftCode;
            this.septetCounts = new int[(maxSingleShiftCode + 1)];
            this.unencodableCounts = new int[(maxSingleShiftCode + 1)];
            int tableOffset = 0;
            for (int i = 1; i <= maxSingleShiftCode; i++) {
                if (GsmAlphabet.sEnabledSingleShiftTables[tableOffset] == i) {
                    tableOffset++;
                } else {
                    this.septetCounts[i] = -1;
                }
            }
            if (code == 1 && maxSingleShiftCode >= 1) {
                this.septetCounts[1] = -1;
            } else if (code == 3 && maxSingleShiftCode >= 2) {
                this.septetCounts[2] = -1;
            }
        }
    }

    public static class TextEncodingDetails {
        public int codeUnitCount;
        public int codeUnitSize;
        public int codeUnitsRemaining;
        public int languageShiftTable;
        public int languageTable;
        public int msgCount;

        public String toString() {
            return "TextEncodingDetails { msgCount=" + this.msgCount + ", codeUnitCount=" + this.codeUnitCount + ", codeUnitsRemaining=" + this.codeUnitsRemaining + ", codeUnitSize=" + this.codeUnitSize + ", languageTable=" + this.languageTable + ", languageShiftTable=" + this.languageShiftTable + " }";
        }
    }

    private GsmAlphabet() {
    }

    public static int charToGsm(char c) {
        try {
            return charToGsm(c, false);
        } catch (EncodeException e) {
            return sCharsToGsmTables[0].get(32, 32);
        }
    }

    public static int charToGsm(char c, boolean throwException) throws EncodeException {
        int ret = sCharsToGsmTables[0].get(c, -1);
        if (ret != -1) {
            return ret;
        }
        if (sCharsToShiftTables[0].get(c, -1) != -1) {
            return 27;
        }
        if (!throwException) {
            return sCharsToGsmTables[0].get(32, 32);
        }
        throw new EncodeException(c);
    }

    public static int charToGsmExtended(char c) {
        int ret = sCharsToShiftTables[0].get(c, -1);
        if (ret == -1) {
            return sCharsToGsmTables[0].get(32, 32);
        }
        return ret;
    }

    public static char gsmToChar(int gsmChar) {
        if (gsmChar < 0 || gsmChar >= 128) {
            return ' ';
        }
        return sLanguageTables[0].charAt(gsmChar);
    }

    public static char gsmExtendedToChar(int gsmChar) {
        if (gsmChar == 27) {
            return ' ';
        }
        if (gsmChar < 0 || gsmChar >= 128) {
            return ' ';
        }
        char c = sLanguageShiftTables[0].charAt(gsmChar);
        if (c == ' ') {
            return sLanguageTables[0].charAt(gsmChar);
        }
        return c;
    }

    public static byte[] stringToGsm7BitPackedWithHeader(String data, byte[] header) throws EncodeException {
        return stringToGsm7BitPackedWithHeader(data, header, 0, 0);
    }

    public static byte[] stringToGsm7BitPackedWithHeader(String data, byte[] header, int languageTable, int languageShiftTable) throws EncodeException {
        if (header == null || header.length == 0) {
            return stringToGsm7BitPacked(data, languageTable, languageShiftTable);
        }
        byte[] ret = stringToGsm7BitPacked(data, (((header.length + 1) * 8) + 6) / 7, true, languageTable, languageShiftTable);
        ret[1] = (byte) header.length;
        System.arraycopy(header, 0, ret, 2, header.length);
        return ret;
    }

    public static byte[] stringToGsm7BitPacked(String data) throws EncodeException {
        return stringToGsm7BitPacked(data, 0, true, 0, 0);
    }

    public static byte[] stringToGsm7BitPacked(String data, int languageTable, int languageShiftTable) throws EncodeException {
        return stringToGsm7BitPacked(data, 0, true, languageTable, languageShiftTable);
    }

    public static byte[] stringToGsm7BitPacked(String data, int startingSeptetOffset, boolean throwException, int languageTable, int languageShiftTable) throws EncodeException {
        int dataLen = data.length();
        int septetCount = countGsmSeptetsUsingTables(data, !throwException, languageTable, languageShiftTable);
        if (septetCount == -1) {
            throw new EncodeException("countGsmSeptetsUsingTables(): unencodable char");
        }
        septetCount += startingSeptetOffset;
        if (septetCount > 255) {
            throw new EncodeException("Payload cannot exceed 255 septets");
        }
        byte[] ret = new byte[((((septetCount * 7) + 7) / 8) + 1)];
        SparseIntArray charToLanguageTable = sCharsToGsmTables[languageTable];
        SparseIntArray charToShiftTable = sCharsToShiftTables[languageShiftTable];
        int i = 0;
        int septets = startingSeptetOffset;
        int bitOffset = startingSeptetOffset * 7;
        while (i < dataLen && septets < septetCount) {
            char c = data.charAt(i);
            int v = charToLanguageTable.get(c, -1);
            if (v == -1) {
                v = charToShiftTable.get(c, -1);
                if (v != -1) {
                    packSmsChar(ret, bitOffset, 27);
                    bitOffset += 7;
                    septets++;
                } else if (throwException) {
                    throw new EncodeException("stringToGsm7BitPacked(): unencodable char");
                } else {
                    v = charToLanguageTable.get(32, 32);
                }
            }
            packSmsChar(ret, bitOffset, v);
            septets++;
            i++;
            bitOffset += 7;
        }
        ret[0] = (byte) septetCount;
        return ret;
    }

    private static void packSmsChar(byte[] packedChars, int bitOffset, int value) {
        int shift = bitOffset % 8;
        int byteOffset = (bitOffset / 8) + 1;
        packedChars[byteOffset] = (byte) (packedChars[byteOffset] | (value << shift));
        if (shift > 1) {
            packedChars[byteOffset + 1] = (byte) (value >> (8 - shift));
        }
    }

    public static String gsm7BitPackedToString(byte[] pdu, int offset, int lengthSeptets) {
        return gsm7BitPackedToString(pdu, offset, lengthSeptets, 0, 0, 0);
    }

    public static String gsm7BitPackedToString(byte[] pdu, int offset, int lengthSeptets, int numPaddingBits, int languageTable, int shiftTable) {
        StringBuilder ret = new StringBuilder(lengthSeptets);
        if (languageTable < 0 || languageTable > sLanguageTables.length) {
            Rlog.w(TAG, "unknown language table " + languageTable + ", using default");
            languageTable = 0;
        }
        if (shiftTable < 0 || shiftTable > sLanguageShiftTables.length) {
            Rlog.w(TAG, "unknown single shift table " + shiftTable + ", using default");
            shiftTable = 0;
        }
        boolean prevCharWasEscape = false;
        try {
            String languageTableToChar = sLanguageTables[languageTable];
            String shiftTableToChar = sLanguageShiftTables[shiftTable];
            if (languageTableToChar.isEmpty()) {
                Rlog.w(TAG, "no language table for code " + languageTable + ", using default");
                languageTableToChar = sLanguageTables[0];
            }
            if (shiftTableToChar.isEmpty()) {
                Rlog.w(TAG, "no single shift table for code " + shiftTable + ", using default");
                shiftTableToChar = sLanguageShiftTables[0];
            }
            for (int i = 0; i < lengthSeptets; i++) {
                int bitOffset = (i * 7) + numPaddingBits;
                int byteOffset = bitOffset / 8;
                int shift = bitOffset % 8;
                int gsmVal = (pdu[offset + byteOffset] >> shift) & 127;
                if (shift > 1) {
                    gsmVal = (gsmVal & (127 >> (shift - 1))) | ((pdu[(offset + byteOffset) + 1] << (8 - shift)) & 127);
                }
                if (prevCharWasEscape) {
                    if (gsmVal == 27) {
                        ret.append(' ');
                    } else {
                        char c = shiftTableToChar.charAt(gsmVal);
                        if (c == ' ') {
                            ret.append(languageTableToChar.charAt(gsmVal));
                        } else {
                            ret.append(c);
                        }
                    }
                    prevCharWasEscape = false;
                } else if (gsmVal == 27) {
                    prevCharWasEscape = true;
                } else {
                    ret.append(languageTableToChar.charAt(gsmVal));
                }
            }
            return ret.toString();
        } catch (RuntimeException ex) {
            Rlog.e(TAG, "Error GSM 7 bit packed: ", ex);
            return null;
        }
    }

    public static String gsm8BitUnpackedToString(byte[] data, int offset, int length) {
        return gsm8BitUnpackedToString(data, offset, length, "");
    }

    public static String gsm8BitUnpackedToString(byte[] data, int offset, int length, String characterset) {
        boolean isMbcs = false;
        Charset charset = null;
        ByteBuffer mbcsBuffer = null;
        if (!TextUtils.isEmpty(characterset)) {
            if (!characterset.equalsIgnoreCase("us-ascii") && Charset.isSupported(characterset)) {
                isMbcs = true;
                charset = Charset.forName(characterset);
                mbcsBuffer = ByteBuffer.allocate(2);
            }
        }
        String languageTableToChar = sLanguageTables[0];
        String shiftTableToChar = sLanguageShiftTables[0];
        StringBuilder ret = new StringBuilder(length);
        boolean prevWasEscape = false;
        int i = offset;
        while (i < offset + length) {
            int c = data[i] & 255;
            if (c == 255) {
                break;
            }
            int i2;
            if (c != 27) {
                if (prevWasEscape) {
                    char shiftChar = c < shiftTableToChar.length() ? shiftTableToChar.charAt(c) : ' ';
                    if (shiftChar != ' ') {
                        ret.append(shiftChar);
                    } else if (c < languageTableToChar.length()) {
                        ret.append(languageTableToChar.charAt(c));
                    } else {
                        ret.append(' ');
                    }
                    i2 = i;
                } else if (isMbcs && c >= 128 && i + 1 < offset + length) {
                    mbcsBuffer.clear();
                    i2 = i + 1;
                    mbcsBuffer.put(data, i, 2);
                    mbcsBuffer.flip();
                    ret.append(charset.decode(mbcsBuffer).toString());
                } else if (c < languageTableToChar.length()) {
                    ret.append(languageTableToChar.charAt(c));
                    i2 = i;
                } else {
                    ret.append(' ');
                    i2 = i;
                }
                prevWasEscape = false;
            } else if (prevWasEscape) {
                ret.append(' ');
                prevWasEscape = false;
                i2 = i;
            } else {
                prevWasEscape = true;
                i2 = i;
            }
            i = i2 + 1;
        }
        return ret.toString();
    }

    public static byte[] stringToGsm8BitPacked(String s) {
        byte[] ret = new byte[countGsmSeptetsUsingTables(s, true, 0, 0)];
        stringToGsm8BitUnpackedField(s, ret, 0, ret.length);
        return ret;
    }

    public static void stringToGsm8BitUnpackedField(String s, byte[] dest, int offset, int length) {
        int outByteIndex = offset;
        SparseIntArray charToLanguageTable = sCharsToGsmTables[0];
        SparseIntArray charToShiftTable = sCharsToShiftTables[0];
        int sz = s.length();
        int outByteIndex2 = outByteIndex;
        for (int i = 0; i < sz && outByteIndex2 - offset < length; i++) {
            char c = s.charAt(i);
            int v = charToLanguageTable.get(c, -1);
            if (v == -1) {
                v = charToShiftTable.get(c, -1);
                if (v == -1) {
                    v = charToLanguageTable.get(32, 32);
                    outByteIndex = outByteIndex2;
                } else if ((outByteIndex2 + 1) - offset >= length) {
                    break;
                } else {
                    outByteIndex = outByteIndex2 + 1;
                    dest[outByteIndex2] = (byte) 27;
                }
            } else {
                outByteIndex = outByteIndex2;
            }
            outByteIndex2 = outByteIndex + 1;
            dest[outByteIndex] = (byte) v;
        }
        while (outByteIndex2 - offset < length) {
            outByteIndex = outByteIndex2 + 1;
            dest[outByteIndex2] = (byte) -1;
            outByteIndex2 = outByteIndex;
        }
    }

    public static int countGsmSeptets(char c) {
        int i = 0;
        try {
            i = countGsmSeptets(c, false);
        } catch (EncodeException e) {
        }
        return i;
    }

    public static int countGsmSeptets(char c, boolean throwsException) throws EncodeException {
        if (sCharsToGsmTables[0].get(c, -1) != -1) {
            return 1;
        }
        if (sCharsToShiftTables[0].get(c, -1) != -1) {
            return 2;
        }
        if (!throwsException) {
            return 1;
        }
        throw new EncodeException(c);
    }

    public static boolean isGsmSeptets(char c) {
        if (sCharsToGsmTables[0].get(c, -1) == -1 && sCharsToShiftTables[0].get(c, -1) == -1) {
            return false;
        }
        return true;
    }

    public static int countGsmSeptetsUsingTables(CharSequence s, boolean use7bitOnly, int languageTable, int languageShiftTable) {
        int count = 0;
        int sz = s.length();
        SparseIntArray charToLanguageTable = sCharsToGsmTables[languageTable];
        SparseIntArray charToShiftTable = sCharsToShiftTables[languageShiftTable];
        for (int i = 0; i < sz; i++) {
            char c = s.charAt(i);
            if (c == '\u001b') {
                Rlog.w(TAG, "countGsmSeptets() string contains Escape character, skipping.");
            } else {
                if (charToLanguageTable.get(c, -1) != -1) {
                    count++;
                } else if (charToShiftTable.get(c, -1) != -1) {
                    count += 2;
                } else if (!use7bitOnly) {
                    return -1;
                } else {
                    count++;
                }
                if (sEnableIgnoreSpecialChar && (c == '¥' || c == '£' || c == '€')) {
                    return -1;
                }
            }
        }
        return count;
    }

    public static TextEncodingDetails countGsmSeptets(CharSequence s, boolean use7bitOnly) {
        if (!sDisableCountryEncodingCheck) {
            enableCountrySpecificEncodings();
        }
        TextEncodingDetails ted;
        int septets;
        if (sEnabledSingleShiftTables.length + sEnabledLockingShiftTables.length == 0) {
            ted = new TextEncodingDetails();
            septets = countGsmSeptetsUsingTables(s, use7bitOnly, 0, 0);
            if (septets == -1) {
                return null;
            }
            ted.codeUnitSize = 1;
            ted.codeUnitCount = septets;
            if (septets > 160) {
                ted.msgCount = (septets + 152) / 153;
                ted.codeUnitsRemaining = (ted.msgCount * 153) - septets;
            } else {
                ted.msgCount = 1;
                ted.codeUnitsRemaining = 160 - septets;
            }
            ted.codeUnitSize = 1;
            return ted;
        }
        int maxSingleShiftCode = sHighestEnabledSingleShiftCode;
        List<LanguagePairCount> lpcList = new ArrayList(sEnabledLockingShiftTables.length + 1);
        lpcList.add(new LanguagePairCount(0));
        for (int i : sEnabledLockingShiftTables) {
            int i2;
            if (!(i2 == 0 || sLanguageTables[i2].isEmpty())) {
                lpcList.add(new LanguagePairCount(i2));
            }
        }
        int sz = s.length();
        for (i2 = 0; i2 < sz && !lpcList.isEmpty(); i2++) {
            char c = s.charAt(i2);
            if (c == '\u001b') {
                Rlog.w(TAG, "countGsmSeptets() string contains Escape character, ignoring!");
            } else {
                for (LanguagePairCount lpc : lpcList) {
                    int table;
                    int[] iArr;
                    if (sCharsToGsmTables[lpc.languageCode].get(c, -1) == -1) {
                        for (table = 0; table <= maxSingleShiftCode; table++) {
                            if (lpc.septetCounts[table] != -1) {
                                if (sCharsToShiftTables[table].get(c, -1) != -1) {
                                    iArr = lpc.septetCounts;
                                    iArr[table] = iArr[table] + 2;
                                } else if (use7bitOnly) {
                                    iArr = lpc.septetCounts;
                                    iArr[table] = iArr[table] + 1;
                                    iArr = lpc.unencodableCounts;
                                    iArr[table] = iArr[table] + 1;
                                } else {
                                    lpc.septetCounts[table] = -1;
                                }
                            }
                        }
                    } else {
                        for (table = 0; table <= maxSingleShiftCode; table++) {
                            if (lpc.septetCounts[table] != -1) {
                                iArr = lpc.septetCounts;
                                iArr[table] = iArr[table] + 1;
                            }
                        }
                    }
                }
            }
        }
        ted = new TextEncodingDetails();
        ted.msgCount = Integer.MAX_VALUE;
        ted.codeUnitSize = 1;
        int minUnencodableCount = Integer.MAX_VALUE;
        for (LanguagePairCount lpc2 : lpcList) {
            int shiftTable = 0;
            while (shiftTable <= maxSingleShiftCode) {
                septets = lpc2.septetCounts[shiftTable];
                if (septets != -1) {
                    int udhLength;
                    int msgCount;
                    int septetsRemaining;
                    if (lpc2.languageCode != 0 && shiftTable != 0) {
                        udhLength = 8;
                    } else if (lpc2.languageCode == 0 && shiftTable == 0) {
                        udhLength = 0;
                    } else {
                        udhLength = 5;
                    }
                    if (septets + udhLength > 160) {
                        if (udhLength == 0) {
                            udhLength = 1;
                        }
                        int septetsPerMessage = 160 - (udhLength + 6);
                        msgCount = ((septets + septetsPerMessage) - 1) / septetsPerMessage;
                        septetsRemaining = (msgCount * septetsPerMessage) - septets;
                    } else {
                        msgCount = 1;
                        septetsRemaining = (160 - udhLength) - septets;
                    }
                    int unencodableCount = lpc2.unencodableCounts[shiftTable];
                    if ((!use7bitOnly || unencodableCount <= minUnencodableCount) && ((use7bitOnly && unencodableCount < minUnencodableCount) || msgCount < ted.msgCount || (msgCount == ted.msgCount && septetsRemaining > ted.codeUnitsRemaining))) {
                        minUnencodableCount = unencodableCount;
                        ted.msgCount = msgCount;
                        ted.codeUnitCount = septets;
                        ted.codeUnitsRemaining = septetsRemaining;
                        ted.languageTable = lpc2.languageCode;
                        ted.languageShiftTable = shiftTable;
                    }
                }
                shiftTable++;
            }
        }
        if (ted.msgCount == Integer.MAX_VALUE) {
            return null;
        }
        return ted;
    }

    public static TextEncodingDetails countGsmSeptets(CharSequence s, boolean use7bitOnly, boolean ignoreSpecialChar) {
        sEnableIgnoreSpecialChar = ignoreSpecialChar;
        TextEncodingDetails ted = countGsmSeptets(s, use7bitOnly);
        sEnableIgnoreSpecialChar = false;
        return ted;
    }

    public static int findGsmSeptetLimitIndex(String s, int start, int limit, int langTable, int langShiftTable) {
        int accumulator = 0;
        int size = s.length();
        SparseIntArray charToLangTable = sCharsToGsmTables[langTable];
        SparseIntArray charToLangShiftTable = sCharsToShiftTables[langShiftTable];
        for (int i = start; i < size; i++) {
            if (charToLangTable.get(s.charAt(i), -1) != -1) {
                accumulator++;
            } else if (charToLangShiftTable.get(s.charAt(i), -1) == -1) {
                accumulator++;
            } else {
                accumulator += 2;
            }
            if (accumulator > limit) {
                return i;
            }
        }
        return size;
    }

    static synchronized void setEnabledSingleShiftTables(int[] tables) {
        synchronized (GsmAlphabet.class) {
            sEnabledSingleShiftTables = tables;
            sDisableCountryEncodingCheck = true;
            if (tables.length > 0) {
                sHighestEnabledSingleShiftCode = tables[tables.length - 1];
            } else {
                sHighestEnabledSingleShiftCode = 0;
            }
        }
    }

    static synchronized void setEnabledLockingShiftTables(int[] tables) {
        synchronized (GsmAlphabet.class) {
            sEnabledLockingShiftTables = tables;
            sDisableCountryEncodingCheck = true;
        }
    }

    public static synchronized int[] getEnabledSingleShiftTables() {
        int[] iArr;
        synchronized (GsmAlphabet.class) {
            iArr = sEnabledSingleShiftTables;
        }
        return iArr;
    }

    public static synchronized int[] getEnabledLockingShiftTables() {
        int[] iArr;
        synchronized (GsmAlphabet.class) {
            iArr = sEnabledLockingShiftTables;
        }
        return iArr;
    }

    private static void enableCountrySpecificEncodings() {
        Resources r = Resources.getSystem();
        sEnabledSingleShiftTables = r.getIntArray(R.array.config_sms_enabled_single_shift_tables);
        sEnabledLockingShiftTables = r.getIntArray(R.array.config_sms_enabled_locking_shift_tables);
        if (sEnabledSingleShiftTables.length > 0) {
            sHighestEnabledSingleShiftCode = sEnabledSingleShiftTables[sEnabledSingleShiftTables.length - 1];
        } else {
            sHighestEnabledSingleShiftCode = 0;
        }
    }

    static {
        int i;
        int j;
        enableCountrySpecificEncodings();
        int numTables = sLanguageTables.length;
        int numShiftTables = sLanguageShiftTables.length;
        if (numTables != numShiftTables) {
            Rlog.e(TAG, "Error: language tables array length " + numTables + " != shift tables array length " + numShiftTables);
        }
        sCharsToGsmTables = new SparseIntArray[numTables];
        for (i = 0; i < numTables; i++) {
            String table = sLanguageTables[i];
            int tableLen = table.length();
            if (!(tableLen == 0 || tableLen == 128)) {
                Rlog.e(TAG, "Error: language tables index " + i + " length " + tableLen + " (expected 128 or 0)");
            }
            SparseIntArray charToGsmTable = new SparseIntArray(tableLen);
            sCharsToGsmTables[i] = charToGsmTable;
            for (j = 0; j < tableLen; j++) {
                charToGsmTable.put(table.charAt(j), j);
            }
        }
        sCharsToShiftTables = new SparseIntArray[numTables];
        for (i = 0; i < numShiftTables; i++) {
            String shiftTable = sLanguageShiftTables[i];
            int shiftTableLen = shiftTable.length();
            if (!(shiftTableLen == 0 || shiftTableLen == 128)) {
                Rlog.e(TAG, "Error: language shift tables index " + i + " length " + shiftTableLen + " (expected 128 or 0)");
            }
            SparseIntArray charToShiftTable = new SparseIntArray(shiftTableLen);
            sCharsToShiftTables[i] = charToShiftTable;
            for (j = 0; j < shiftTableLen; j++) {
                char c = shiftTable.charAt(j);
                if (c != ' ') {
                    charToShiftTable.put(c, j);
                }
            }
        }
        int i2 = 0 + 1;
        charToGsm.put(64, 0);
        i = i2 + 1;
        charToGsm.put(163, i2);
        i2 = i + 1;
        charToGsm.put(36, i);
        i = i2 + 1;
        charToGsm.put(165, i2);
        i2 = i + 1;
        charToGsm.put(232, i);
        i = i2 + 1;
        charToGsm.put(233, i2);
        i2 = i + 1;
        charToGsm.put(249, i);
        i = i2 + 1;
        charToGsm.put(236, i2);
        i2 = i + 1;
        charToGsm.put(242, i);
        i = i2 + 1;
        charToGsm.put(199, i2);
        i2 = i + 1;
        charToGsm.put(10, i);
        i = i2 + 1;
        charToGsm.put(216, i2);
        i2 = i + 1;
        charToGsm.put(248, i);
        i = i2 + 1;
        charToGsm.put(13, i2);
        i2 = i + 1;
        charToGsm.put(197, i);
        i = i2 + 1;
        charToGsm.put(229, i2);
        i2 = i + 1;
        charToGsm.put(916, i);
        i = i2 + 1;
        charToGsm.put(95, i2);
        i2 = i + 1;
        charToGsm.put(934, i);
        i = i2 + 1;
        charToGsm.put(915, i2);
        i2 = i + 1;
        charToGsm.put(923, i);
        i = i2 + 1;
        charToGsm.put(937, i2);
        i2 = i + 1;
        charToGsm.put(928, i);
        i = i2 + 1;
        charToGsm.put(936, i2);
        i2 = i + 1;
        charToGsm.put(931, i);
        i = i2 + 1;
        charToGsm.put(920, i2);
        i2 = i + 1;
        charToGsm.put(926, i);
        i = i2 + 1;
        charToGsm.put(65535, i2);
        i2 = i + 1;
        charToGsm.put(198, i);
        i = i2 + 1;
        charToGsm.put(230, i2);
        i2 = i + 1;
        charToGsm.put(223, i);
        i = i2 + 1;
        charToGsm.put(201, i2);
        i2 = i + 1;
        charToGsm.put(32, i);
        i = i2 + 1;
        charToGsm.put(33, i2);
        i2 = i + 1;
        charToGsm.put(34, i);
        i = i2 + 1;
        charToGsm.put(35, i2);
        i2 = i + 1;
        charToGsm.put(164, i);
        i = i2 + 1;
        charToGsm.put(37, i2);
        i2 = i + 1;
        charToGsm.put(38, i);
        i = i2 + 1;
        charToGsm.put(39, i2);
        i2 = i + 1;
        charToGsm.put(40, i);
        i = i2 + 1;
        charToGsm.put(41, i2);
        i2 = i + 1;
        charToGsm.put(42, i);
        i = i2 + 1;
        charToGsm.put(43, i2);
        i2 = i + 1;
        charToGsm.put(44, i);
        i = i2 + 1;
        charToGsm.put(45, i2);
        i2 = i + 1;
        charToGsm.put(46, i);
        i = i2 + 1;
        charToGsm.put(47, i2);
        i2 = i + 1;
        charToGsm.put(48, i);
        i = i2 + 1;
        charToGsm.put(49, i2);
        i2 = i + 1;
        charToGsm.put(50, i);
        i = i2 + 1;
        charToGsm.put(51, i2);
        i2 = i + 1;
        charToGsm.put(52, i);
        i = i2 + 1;
        charToGsm.put(53, i2);
        i2 = i + 1;
        charToGsm.put(54, i);
        i = i2 + 1;
        charToGsm.put(55, i2);
        i2 = i + 1;
        charToGsm.put(56, i);
        i = i2 + 1;
        charToGsm.put(57, i2);
        i2 = i + 1;
        charToGsm.put(58, i);
        i = i2 + 1;
        charToGsm.put(59, i2);
        i2 = i + 1;
        charToGsm.put(60, i);
        i = i2 + 1;
        charToGsm.put(61, i2);
        i2 = i + 1;
        charToGsm.put(62, i);
        i = i2 + 1;
        charToGsm.put(63, i2);
        i2 = i + 1;
        charToGsm.put(161, i);
        i = i2 + 1;
        charToGsm.put(65, i2);
        i2 = i + 1;
        charToGsm.put(66, i);
        i = i2 + 1;
        charToGsm.put(67, i2);
        i2 = i + 1;
        charToGsm.put(68, i);
        i = i2 + 1;
        charToGsm.put(69, i2);
        i2 = i + 1;
        charToGsm.put(70, i);
        i = i2 + 1;
        charToGsm.put(71, i2);
        i2 = i + 1;
        charToGsm.put(72, i);
        i = i2 + 1;
        charToGsm.put(73, i2);
        i2 = i + 1;
        charToGsm.put(74, i);
        i = i2 + 1;
        charToGsm.put(75, i2);
        i2 = i + 1;
        charToGsm.put(76, i);
        i = i2 + 1;
        charToGsm.put(77, i2);
        i2 = i + 1;
        charToGsm.put(78, i);
        i = i2 + 1;
        charToGsm.put(79, i2);
        i2 = i + 1;
        charToGsm.put(80, i);
        i = i2 + 1;
        charToGsm.put(81, i2);
        i2 = i + 1;
        charToGsm.put(82, i);
        i = i2 + 1;
        charToGsm.put(83, i2);
        i2 = i + 1;
        charToGsm.put(84, i);
        i = i2 + 1;
        charToGsm.put(85, i2);
        i2 = i + 1;
        charToGsm.put(86, i);
        i = i2 + 1;
        charToGsm.put(87, i2);
        i2 = i + 1;
        charToGsm.put(88, i);
        i = i2 + 1;
        charToGsm.put(89, i2);
        i2 = i + 1;
        charToGsm.put(90, i);
        i = i2 + 1;
        charToGsm.put(196, i2);
        i2 = i + 1;
        charToGsm.put(214, i);
        i = i2 + 1;
        charToGsm.put(209, i2);
        i2 = i + 1;
        charToGsm.put(220, i);
        i = i2 + 1;
        charToGsm.put(167, i2);
        i2 = i + 1;
        charToGsm.put(191, i);
        i = i2 + 1;
        charToGsm.put(97, i2);
        i2 = i + 1;
        charToGsm.put(98, i);
        i = i2 + 1;
        charToGsm.put(99, i2);
        i2 = i + 1;
        charToGsm.put(100, i);
        i = i2 + 1;
        charToGsm.put(101, i2);
        i2 = i + 1;
        charToGsm.put(102, i);
        i = i2 + 1;
        charToGsm.put(103, i2);
        i2 = i + 1;
        charToGsm.put(104, i);
        i = i2 + 1;
        charToGsm.put(105, i2);
        i2 = i + 1;
        charToGsm.put(106, i);
        i = i2 + 1;
        charToGsm.put(107, i2);
        i2 = i + 1;
        charToGsm.put(108, i);
        i = i2 + 1;
        charToGsm.put(109, i2);
        i2 = i + 1;
        charToGsm.put(110, i);
        i = i2 + 1;
        charToGsm.put(111, i2);
        i2 = i + 1;
        charToGsm.put(112, i);
        i = i2 + 1;
        charToGsm.put(113, i2);
        i2 = i + 1;
        charToGsm.put(114, i);
        i = i2 + 1;
        charToGsm.put(115, i2);
        i2 = i + 1;
        charToGsm.put(116, i);
        i = i2 + 1;
        charToGsm.put(117, i2);
        i2 = i + 1;
        charToGsm.put(118, i);
        i = i2 + 1;
        charToGsm.put(119, i2);
        i2 = i + 1;
        charToGsm.put(120, i);
        i = i2 + 1;
        charToGsm.put(121, i2);
        i2 = i + 1;
        charToGsm.put(122, i);
        i = i2 + 1;
        charToGsm.put(228, i2);
        i2 = i + 1;
        charToGsm.put(246, i);
        i = i2 + 1;
        charToGsm.put(241, i2);
        i2 = i + 1;
        charToGsm.put(252, i);
        i = i2 + 1;
        charToGsm.put(224, i2);
        charToGsmExtended.put(12, 10);
        charToGsmExtended.put(94, 20);
        charToGsmExtended.put(123, 40);
        charToGsmExtended.put(125, 41);
        charToGsmExtended.put(92, 47);
        charToGsmExtended.put(91, 60);
        charToGsmExtended.put(126, 61);
        charToGsmExtended.put(93, 62);
        charToGsmExtended.put(124, 64);
        charToGsmExtended.put(8364, 101);
        int size = charToGsm.size();
        for (j = 0; j < size; j++) {
            gsmToChar.put(charToGsm.valueAt(j), charToGsm.keyAt(j));
        }
        size = charToGsmExtended.size();
        for (j = 0; j < size; j++) {
            gsmExtendedToChar.put(charToGsmExtended.valueAt(j), charToGsmExtended.keyAt(j));
        }
    }

    public static String gsm7BitPackedToString(byte[] pdu, int offset, int lengthSeptets, int numPaddingBits) {
        StringBuilder ret = new StringBuilder(lengthSeptets);
        boolean prevCharWasEscape = false;
        int i = 0;
        while (i < lengthSeptets) {
            int bitOffset = (i * 7) + numPaddingBits;
            try {
                int byteOffset = bitOffset / 8;
                int shift = bitOffset % 8;
                int gsmVal = (pdu[offset + byteOffset] >> shift) & 127;
                if (shift > 1) {
                    gsmVal = (gsmVal & (127 >> (shift - 1))) | ((pdu[(offset + byteOffset) + 1] << (8 - shift)) & 127);
                }
                if (prevCharWasEscape) {
                    ret.append(gsmExtendedToChar(gsmVal));
                    prevCharWasEscape = false;
                } else if (gsmVal == 27) {
                    prevCharWasEscape = true;
                } else {
                    ret.append(gsmToChar(gsmVal));
                }
                i++;
            } catch (RuntimeException ex) {
                Rlog.e(TAG, "Error GSM 7 bit packed: ", ex);
                return null;
            }
        }
        return ret.toString();
    }

    public static char convertEachCharacter(char c) {
        char ret = c;
        if (sEnabledSingleShiftTables.length + sEnabledLockingShiftTables.length != 0) {
            return ret;
        }
        if (charToGsm.get(c, -1) != -1) {
            return c;
        }
        if (charToGsmExtended.get(c, -1) != -1) {
            return c;
        }
        return convertNonGSMCharacter(c);
    }

    private static char convertNonGSMCharacter(char c) {
        char temp = c;
        System.out.println("temp char :" + c);
        switch (temp) {
            case 'À':
                temp = DateFormat.CAPITAL_AM_PM;
                break;
            case 'Á':
                temp = DateFormat.CAPITAL_AM_PM;
                break;
            case 'Â':
                temp = DateFormat.CAPITAL_AM_PM;
                break;
            case 'Ã':
                temp = DateFormat.CAPITAL_AM_PM;
                break;
            case 'È':
                temp = DateFormat.DAY;
                break;
            case 'Ê':
                temp = DateFormat.DAY;
                break;
            case 'Ë':
                temp = DateFormat.DAY;
                break;
            case 'Ì':
                temp = 'I';
                break;
            case 'Í':
                temp = 'I';
                break;
            case 'Î':
                temp = 'I';
                break;
            case 'Ï':
                temp = 'I';
                break;
            case 'Ò':
                temp = 'O';
                break;
            case 'Ó':
                temp = 'O';
                break;
            case 'Ô':
                temp = 'O';
                break;
            case 'Õ':
                temp = 'O';
                break;
            case 'Ù':
                temp = 'U';
                break;
            case 'Ú':
                temp = 'U';
                break;
            case 'Û':
                temp = 'U';
                break;
            case 'Ý':
                temp = 'Y';
                break;
            case 'á':
                temp = DateFormat.AM_PM;
                break;
            case 'â':
                temp = DateFormat.AM_PM;
                break;
            case 'ã':
                temp = DateFormat.AM_PM;
                break;
            case 'ç':
                temp = 'c';
                break;
            case 'é':
                temp = 'e';
                break;
            case 'ê':
                temp = 'e';
                break;
            case 'ë':
                temp = 'e';
                break;
            case 'í':
                temp = 'i';
                break;
            case 'î':
                temp = 'i';
                break;
            case 'ï':
                temp = 'i';
                break;
            case 'ó':
                temp = 'o';
                break;
            case 'ô':
                temp = 'o';
                break;
            case 'õ':
                temp = 'o';
                break;
            case 'ö':
                temp = 'o';
                break;
            case 'ú':
                temp = 'u';
                break;
            case 'û':
                temp = 'u';
                break;
            case 'ü':
                temp = 'u';
                break;
            case 'ý':
                temp = 'y';
                break;
            case 'ÿ':
                temp = 'y';
                break;
            case 'Ā':
                temp = DateFormat.CAPITAL_AM_PM;
                break;
            case 'ā':
                temp = DateFormat.AM_PM;
                break;
            case 'Ą':
                temp = DateFormat.CAPITAL_AM_PM;
                break;
            case 'ą':
                temp = DateFormat.AM_PM;
                break;
            case 'Ć':
                temp = 'C';
                break;
            case 'ć':
                temp = 'c';
                break;
            case R.styleable.Theme_textColorSearchUrl /*268*/:
                temp = 'C';
                break;
            case R.styleable.Theme_searchWidgetCorpusItemBackground /*269*/:
                temp = 'c';
                break;
            case R.styleable.Theme_textAppearanceEasyCorrectSuggestion /*270*/:
                temp = 'D';
                break;
            case R.styleable.Theme_textAppearanceMisspelledSuggestion /*271*/:
                temp = DateFormat.DATE;
                break;
            case 'Ē':
                temp = DateFormat.DAY;
                break;
            case 'ē':
                temp = 'e';
                break;
            case 'Ę':
                temp = DateFormat.DAY;
                break;
            case R.styleable.Theme_floatingToolbarCloseDrawable /*281*/:
                temp = 'e';
                break;
            case R.styleable.Theme_floatingToolbarForegroundColor /*282*/:
                temp = DateFormat.DAY;
                break;
            case R.styleable.Theme_floatingToolbarItemBackgroundBorderlessDrawable /*283*/:
                temp = 'e';
                break;
            case R.styleable.Theme_floatingToolbarPopupBackgroundDrawable /*286*/:
                temp = 'G';
                break;
            case R.styleable.Theme_alertDialogButtonGroupStyle /*287*/:
                temp = 'g';
                break;
            case R.styleable.Theme_preferenceActivityStyle /*298*/:
                temp = 'I';
                break;
            case R.styleable.Theme_preferenceFragmentStyle /*299*/:
                temp = 'i';
                break;
            case 'İ':
                temp = 'I';
                break;
            case 'ı':
                temp = 'i';
                break;
            case R.styleable.Theme_pointerStyle /*313*/:
                temp = DateFormat.STANDALONE_MONTH;
                break;
            case R.styleable.Theme_accessibilityFocusedDrawable /*314*/:
                temp = 'l';
                break;
            case R.styleable.Theme_colorSwitchThumbNormal /*317*/:
                temp = DateFormat.STANDALONE_MONTH;
                break;
            case R.styleable.Theme_lightY /*318*/:
                temp = 'l';
                break;
            case 'Ł':
                temp = DateFormat.STANDALONE_MONTH;
                break;
            case R.styleable.Theme_state_finger_hovered /*322*/:
                temp = 'l';
                break;
            case R.styleable.Theme_parentIsDeviceDefault /*323*/:
                temp = PhoneNumberUtils.WILD;
                break;
            case R.styleable.Theme_parentIsThemeHoloDark /*324*/:
                temp = 'n';
                break;
            case R.styleable.Theme_summaryContentDescription /*327*/:
                temp = PhoneNumberUtils.WILD;
                break;
            case R.styleable.Theme_preferenceActivityLayout /*328*/:
                temp = 'n';
                break;
            case 'Ō':
                temp = 'O';
                break;
            case 'ō':
                temp = 'o';
                break;
            case 'Ő':
                temp = 'O';
                break;
            case 'ő':
                temp = 'o';
                break;
            case 'Œ':
                temp = 'O';
                break;
            case 'œ':
                temp = 'o';
                break;
            case 'Ŕ':
                temp = 'R';
                break;
            case 'ŕ':
                temp = 'r';
                break;
            case R.styleable.Theme_twDragBlockImage /*344*/:
                temp = 'R';
                break;
            case R.styleable.Theme_twDragBlockImageBorderless /*345*/:
                temp = 'r';
                break;
            case R.styleable.Theme_toastTextColor /*346*/:
                temp = 'S';
                break;
            case R.styleable.Theme_twListTextColorPrimary /*347*/:
                temp = DateFormat.SECONDS;
                break;
            case 'Ş':
                temp = 'S';
                break;
            case 'ş':
                temp = DateFormat.SECONDS;
                break;
            case 'Š':
                temp = 'S';
                break;
            case 'š':
                temp = DateFormat.SECONDS;
                break;
            case R.styleable.Theme_hoverPopupPickerSpaceColor /*356*/:
                temp = 'T';
                break;
            case R.styleable.Theme_hoverPopupBottomBgColor /*357*/:
                temp = 't';
                break;
            case ImsReasonInfo.CODE_SIP_GLOBAL_ERROR /*362*/:
                temp = 'U';
                break;
            case 'ū':
                temp = 'u';
                break;
            case 'Ů':
                temp = 'U';
                break;
            case 'ů':
                temp = 'u';
                break;
            case 'Ű':
                temp = 'U';
                break;
            case 'ű':
                temp = 'u';
                break;
            case 'Ÿ':
                temp = 'Y';
                break;
            case 'Ź':
                temp = 'Z';
                break;
            case 'ź':
                temp = DateFormat.TIME_ZONE;
                break;
            case 'Ż':
                temp = 'Z';
                break;
            case 'ż':
                temp = DateFormat.TIME_ZONE;
                break;
            case 'Ž':
                temp = 'Z';
                break;
            case 'ž':
                temp = DateFormat.TIME_ZONE;
                break;
            case 'Α':
                temp = DateFormat.CAPITAL_AM_PM;
                break;
            case 'Β':
                temp = 'B';
                break;
            case 'Ε':
                temp = DateFormat.DAY;
                break;
            case 'Ζ':
                temp = 'Z';
                break;
            case 'Η':
                temp = 'H';
                break;
            case 'Ι':
                temp = 'I';
                break;
            case 'Κ':
                temp = 'K';
                break;
            case 'Μ':
                temp = DateFormat.MONTH;
                break;
            case 'Ν':
                temp = PhoneNumberUtils.WILD;
                break;
            case 'Ο':
                temp = 'O';
                break;
            case 'Ρ':
                temp = 'P';
                break;
            case 'Τ':
                temp = 'T';
                break;
            case 'Υ':
                temp = 'Y';
                break;
            case 'Χ':
                temp = 'X';
                break;
            default:
                if (temp > '' || temp == '`') {
                    if (temp != '') {
                        temp = '﻿';
                        break;
                    }
                    temp = ' ';
                    break;
                }
        }
        System.out.println("temp char :" + temp);
        return temp;
    }

    public static TextEncodingDetails CountGsmSeptetsWithEmail(CharSequence s, boolean use7bitOnly, int maxEmailLen) {
        Log.secV(TAG, "sEnabledSingleShiftTables.length + sEnabledLockingShiftTables.length == 0: " + (sEnabledSingleShiftTables.length + sEnabledLockingShiftTables.length == 0));
        TextEncodingDetails ted;
        int septets;
        if (sEnabledSingleShiftTables.length + sEnabledLockingShiftTables.length == 0) {
            ted = new TextEncodingDetails();
            septets = countGsmSeptetsUsingTables(s, use7bitOnly, 0, 0);
            if (septets == -1) {
                return null;
            }
            int maxLenPerSMS = maxEmailLen > 0 ? (160 - maxEmailLen) - 1 : 160;
            int maxLenPerSMSWithHeader = maxEmailLen > 0 ? (153 - maxEmailLen) - 1 : 153;
            if (septets != -1 && septets <= maxLenPerSMS) {
                ted.msgCount = 1;
                ted.codeUnitCount = septets;
                ted.codeUnitsRemaining = maxLenPerSMS - septets;
                ted.codeUnitSize = 1;
                return ted;
            } else if (septets == -1) {
                return ted;
            } else {
                ted.codeUnitCount = septets;
                if (septets > maxLenPerSMS) {
                    ted.msgCount = ((maxLenPerSMSWithHeader - 1) + septets) / maxLenPerSMSWithHeader;
                    if (septets % maxLenPerSMSWithHeader > 0) {
                        ted.codeUnitsRemaining = maxLenPerSMSWithHeader - (septets % maxLenPerSMSWithHeader);
                    } else {
                        ted.codeUnitsRemaining = 0;
                    }
                } else {
                    ted.msgCount = 1;
                    ted.codeUnitsRemaining = maxLenPerSMS - septets;
                }
                ted.codeUnitSize = 1;
                return ted;
            }
        }
        int i;
        int maxSingleShiftCode = sHighestEnabledSingleShiftCode;
        List<LanguagePairCount> lpcList = new ArrayList(sEnabledLockingShiftTables.length + 1);
        lpcList.add(new LanguagePairCount(0));
        for (int i2 : sEnabledLockingShiftTables) {
            if (!(i2 == 0 || sLanguageTables[i2].isEmpty())) {
                lpcList.add(new LanguagePairCount(i2));
            }
        }
        int sz = s.length();
        for (i2 = 0; i2 < sz && !lpcList.isEmpty(); i2++) {
            char c = s.charAt(i2);
            if (c == '\u001b') {
                Log.secW(TAG, "countGsmSeptets() string contains Escape character, ignoring!");
            } else {
                for (LanguagePairCount lpc : lpcList) {
                    int table;
                    int[] iArr;
                    if (sCharsToGsmTables[lpc.languageCode].get(c, -1) == -1) {
                        for (table = 0; table <= maxSingleShiftCode; table++) {
                            if (lpc.septetCounts[table] != -1) {
                                if (sCharsToShiftTables[table].get(c, -1) != -1) {
                                    iArr = lpc.septetCounts;
                                    iArr[table] = iArr[table] + 2;
                                } else if (use7bitOnly) {
                                    iArr = lpc.septetCounts;
                                    iArr[table] = iArr[table] + 1;
                                    iArr = lpc.unencodableCounts;
                                    iArr[table] = iArr[table] + 1;
                                } else {
                                    lpc.septetCounts[table] = -1;
                                }
                            }
                        }
                    } else {
                        for (table = 0; table <= maxSingleShiftCode; table++) {
                            if (lpc.septetCounts[table] != -1) {
                                iArr = lpc.septetCounts;
                                iArr[table] = iArr[table] + 1;
                            }
                        }
                    }
                }
            }
        }
        ted = new TextEncodingDetails();
        ted.msgCount = Integer.MAX_VALUE;
        ted.codeUnitSize = 1;
        int minUnencodableCount = Integer.MAX_VALUE;
        for (LanguagePairCount lpc2 : lpcList) {
            int shiftTable = 0;
            while (shiftTable <= maxSingleShiftCode) {
                septets = lpc2.septetCounts[shiftTable];
                if (septets != -1) {
                    int udhLength;
                    int msgCount;
                    int septetsRemaining;
                    if (lpc2.languageCode != 0 && shiftTable != 0) {
                        udhLength = 8;
                    } else if (lpc2.languageCode == 0 && shiftTable == 0) {
                        udhLength = 0;
                    } else {
                        udhLength = 5;
                    }
                    if (septets + udhLength > 160) {
                        if (udhLength == 0) {
                            udhLength = 1;
                        }
                        int septetsPerMessage = 160 - (udhLength + 6);
                        msgCount = ((septets + septetsPerMessage) - 1) / septetsPerMessage;
                        septetsRemaining = (msgCount * septetsPerMessage) - septets;
                    } else {
                        msgCount = 1;
                        septetsRemaining = (160 - udhLength) - septets;
                    }
                    int unencodableCount = lpc2.unencodableCounts[shiftTable];
                    if ((!use7bitOnly || unencodableCount <= minUnencodableCount) && ((use7bitOnly && unencodableCount < minUnencodableCount) || msgCount < ted.msgCount || (msgCount == ted.msgCount && septetsRemaining > ted.codeUnitsRemaining))) {
                        minUnencodableCount = unencodableCount;
                        ted.msgCount = msgCount;
                        ted.codeUnitCount = septets;
                        ted.codeUnitsRemaining = septetsRemaining;
                        ted.languageTable = lpc2.languageCode;
                        ted.languageShiftTable = shiftTable;
                    }
                }
                shiftTable++;
            }
        }
        if (ted.msgCount == Integer.MAX_VALUE) {
            return null;
        }
        return ted;
    }
}
