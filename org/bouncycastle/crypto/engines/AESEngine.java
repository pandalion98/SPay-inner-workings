package org.bouncycastle.crypto.engines;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.mastercard.mcbp.core.mpplite.MPPLiteInstruction;
import com.mastercard.mobile_api.payment.cld.CLD;
import com.mastercard.mobile_api.payment.cld.CardSide;
import com.mastercard.mobile_api.utils.apdu.ISO7816;
import com.mastercard.mobile_api.utils.apdu.emv.EMVGetResponse;
import com.mastercard.mobile_api.utils.apdu.emv.EMVGetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.EMVSetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GenerateACApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GetDataApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GetProcessingOptions;
import com.mastercard.mobile_api.utils.apdu.emv.GetTemplateApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PinChangeUnblockApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PutData80Apdu;
import com.mastercard.mobile_api.utils.apdu.emv.PutTemplateApdu;
import com.mastercard.mobile_api.utils.apdu.emv.ReadRecordApdu;
import com.mastercard.mobile_api.utils.apdu.emv.SetResetParamApdu;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.mastercard.mobile_api.utils.tlv.TLVParser;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import java.lang.reflect.Array;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class AESEngine implements BlockCipher {
    private static final int BLOCK_SIZE = 16;
    private static final byte[] f145S;
    private static final byte[] Si;
    private static final int[] T0;
    private static final int[] Tinv0;
    private static final int m1 = -2139062144;
    private static final int m2 = 2139062143;
    private static final int m3 = 27;
    private static final int[] rcon;
    private int C0;
    private int C1;
    private int C2;
    private int C3;
    private int ROUNDS;
    private int[][] WorkingKey;
    private boolean forEncryption;

    static {
        f145S = new byte[]{(byte) 99, (byte) 124, ApplicationInfoManager.TERM_XP2, (byte) 123, EMVGetStatusApdu.INS, (byte) 107, MCFCITemplate.TAG_FCI_TEMPLATE, (byte) -59, (byte) 48, (byte) 1, (byte) 103, (byte) 43, (byte) -2, (byte) -41, (byte) -85, (byte) 118, GetDataApdu.INS, EMVSetStatusApdu.RESET_LOWEST_PRIORITY, (byte) -55, (byte) 125, (byte) -6, (byte) 89, (byte) 71, EMVSetStatusApdu.INS, (byte) -83, GetTemplateApdu.INS, (byte) -94, (byte) -81, (byte) -100, ISO7816.INS_SELECT, (byte) 114, EMVGetResponse.INS, ApplicationInfoManager.EMV_ONLY, (byte) -3, (byte) -109, (byte) 38, (byte) 54, (byte) 63, (byte) -9, (byte) -52, (byte) 52, PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG, (byte) -27, (byte) -15, (byte) 113, (byte) -40, (byte) 49, CardSide.CARD_ELEMENTS_TAG, (byte) 4, (byte) -57, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE, (byte) -61, CardSide.NO_PIN_TEXT_TAG, (byte) -106, (byte) 5, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) 7, CLD.FORM_FACTOR_TAG, VerifyPINApdu.P2_PLAINTEXT, (byte) -30, (byte) -21, (byte) 39, ReadRecordApdu.INS, ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, (byte) 9, (byte) -125, (byte) 44, (byte) 26, (byte) 27, (byte) 110, (byte) 90, (byte) -96, (byte) 82, (byte) 59, (byte) -42, (byte) -77, (byte) 41, (byte) -29, (byte) 47, PinChangeUnblockApdu.CLA, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) -47, (byte) 0, (byte) -19, VerifyPINApdu.INS, (byte) -4, (byte) -79, (byte) 91, (byte) 106, (byte) -53, (byte) -66, ApplicationInfoManager.EMV_MS, (byte) 74, (byte) 76, ApplicationInfoManager.TERM_XP1, (byte) -49, (byte) -48, (byte) -17, (byte) -86, (byte) -5, (byte) 67, (byte) 77, ApplicationInfoManager.TERM_XP3, (byte) -123, (byte) 69, (byte) -7, (byte) 2, Byte.MAX_VALUE, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) 60, (byte) -97, GetProcessingOptions.INS, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, (byte) -93, EMVGetStatusApdu.P1, (byte) -113, (byte) -110, (byte) -99, (byte) 56, (byte) -11, PSSSigner.TRAILER_IMPLICIT, (byte) -74, PutData80Apdu.INS, (byte) 33, Tnaf.POW_2_WIDTH, (byte) -1, (byte) -13, PutTemplateApdu.INS, (byte) -51, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, CardSide.BACKGROUND_TAG, (byte) -20, MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) -105, (byte) 68, CardSide.PIN_TEXT_TAG, (byte) -60, (byte) -89, (byte) 126, (byte) 61, (byte) 100, (byte) 93, (byte) 25, (byte) 115, (byte) 96, TLVParser.BYTE_81, GetTemplateApdu.TAG_DF_NAME_4F, (byte) -36, (byte) 34, GenerateACApdu.INS, SetResetParamApdu.CLA, VerifyPINApdu.P2_CIPHERED, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) -18, (byte) -72, CardSide.PICTURE_TAG, (byte) -34, (byte) 94, (byte) 11, (byte) -37, (byte) -32, (byte) 50, (byte) 58, (byte) 10, (byte) 73, (byte) 6, PinChangeUnblockApdu.INS, (byte) 92, (byte) -62, ApplicationInfoManager.AMOUNT_STATUS_LOW, (byte) -84, (byte) 98, (byte) -111, (byte) -107, (byte) -28, (byte) 121, (byte) -25, (byte) -56, (byte) 55, (byte) 109, (byte) -115, (byte) -43, (byte) 78, (byte) -87, (byte) 108, (byte) 86, (byte) -12, MPPLiteInstruction.INS_RELAY_RESISTANCE, (byte) 101, (byte) 122, MPPLiteInstruction.INS_GENERATE_AC, (byte) 8, (byte) -70, ApplicationInfoManager.TERMINAL_MODE_NONE, (byte) 37, (byte) 46, (byte) 28, (byte) -90, (byte) -76, (byte) -58, (byte) -24, (byte) -35, (byte) 116, (byte) 31, (byte) 75, (byte) -67, (byte) -117, (byte) -118, (byte) 112, (byte) 62, (byte) -75, (byte) 102, (byte) 72, (byte) 3, (byte) -10, (byte) 14, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, (byte) 87, (byte) -71, (byte) -122, ApplicationInfoManager.MS_ONLY, (byte) 29, (byte) -98, (byte) -31, (byte) -8, ApplicationInfoManager.MOB_CVM_TYP_MPVV, CLD.VERSION_TAG, (byte) 105, (byte) -39, (byte) -114, (byte) -108, (byte) -101, (byte) 30, GetTemplateApdu.TAG_PRIORITY_INDICATOR_87, (byte) -23, (byte) -50, (byte) 85, (byte) 40, (byte) -33, (byte) -116, (byte) -95, (byte) -119, (byte) 13, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, (byte) -26, MCFCITemplate.TAG_FCI_ISSUER_IIN, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, (byte) 65, (byte) -103, SetResetParamApdu.INS, (byte) 15, (byte) -80, (byte) 84, (byte) -69, CardSide.ALWAYS_TEXT_TAG};
        Si = new byte[]{(byte) 82, (byte) 9, (byte) 106, (byte) -43, (byte) 48, (byte) 54, PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG, (byte) 56, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, EMVGetStatusApdu.P1, (byte) -93, (byte) -98, TLVParser.BYTE_81, (byte) -13, (byte) -41, (byte) -5, (byte) 124, (byte) -29, ApplicationInfoManager.EMV_MS, EMVSetStatusApdu.RESET_LOWEST_PRIORITY, (byte) -101, (byte) 47, (byte) -1, GetTemplateApdu.TAG_PRIORITY_INDICATOR_87, (byte) 52, (byte) -114, (byte) 67, (byte) 68, (byte) -60, (byte) -34, (byte) -23, (byte) -53, (byte) 84, (byte) 123, (byte) -108, (byte) 50, (byte) -90, (byte) -62, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE, (byte) 61, (byte) -18, (byte) 76, (byte) -107, (byte) 11, MCFCITemplate.TAG_FCI_ISSUER_IIN, (byte) -6, (byte) -61, (byte) 78, (byte) 8, (byte) 46, (byte) -95, (byte) 102, (byte) 40, (byte) -39, PinChangeUnblockApdu.INS, ReadRecordApdu.INS, (byte) 118, (byte) 91, (byte) -94, (byte) 73, (byte) 109, (byte) -117, (byte) -47, (byte) 37, (byte) 114, (byte) -8, (byte) -10, (byte) 100, (byte) -122, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, ApplicationInfoManager.MOB_CVM_TYP_MPVV, CardSide.ALWAYS_TEXT_TAG, GetTemplateApdu.INS, ISO7816.INS_SELECT, (byte) 92, (byte) -52, (byte) 93, (byte) 101, (byte) -74, (byte) -110, (byte) 108, (byte) 112, (byte) 72, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) -3, (byte) -19, (byte) -71, PutData80Apdu.INS, (byte) 94, CardSide.CARD_ELEMENTS_TAG, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) 87, (byte) -89, (byte) -115, (byte) -99, PinChangeUnblockApdu.CLA, SetResetParamApdu.CLA, (byte) -40, (byte) -85, (byte) 0, (byte) -116, PSSSigner.TRAILER_IMPLICIT, ApplicationInfoManager.AMOUNT_STATUS_LOW, (byte) 10, (byte) -9, (byte) -28, ApplicationInfoManager.TERM_XP1, (byte) 5, (byte) -72, (byte) -77, (byte) 69, (byte) 6, (byte) -48, (byte) 44, (byte) 30, (byte) -113, GetDataApdu.INS, (byte) 63, (byte) 15, (byte) 2, ApplicationInfoManager.MS_ONLY, (byte) -81, (byte) -67, (byte) 3, (byte) 1, CardSide.BACKGROUND_TAG, (byte) -118, (byte) 107, (byte) 58, (byte) -111, CLD.VERSION_TAG, (byte) 65, GetTemplateApdu.TAG_DF_NAME_4F, (byte) 103, (byte) -36, MPPLiteInstruction.INS_RELAY_RESISTANCE, (byte) -105, EMVGetStatusApdu.INS, (byte) -49, (byte) -50, EMVSetStatusApdu.INS, (byte) -76, (byte) -26, (byte) 115, (byte) -106, (byte) -84, (byte) 116, (byte) 34, (byte) -25, (byte) -83, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, (byte) -123, (byte) -30, (byte) -7, (byte) 55, (byte) -24, (byte) 28, ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, (byte) -33, (byte) 110, (byte) 71, (byte) -15, (byte) 26, (byte) 113, (byte) 29, (byte) 41, (byte) -59, (byte) -119, MCFCITemplate.TAG_FCI_TEMPLATE, ApplicationInfoManager.EMV_ONLY, (byte) 98, (byte) 14, (byte) -86, CardSide.NO_PIN_TEXT_TAG, (byte) -66, (byte) 27, (byte) -4, (byte) 86, (byte) 62, (byte) 75, (byte) -58, PutTemplateApdu.INS, (byte) 121, VerifyPINApdu.INS, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) -37, EMVGetResponse.INS, (byte) -2, ApplicationInfoManager.TERMINAL_MODE_NONE, (byte) -51, (byte) 90, (byte) -12, (byte) 31, (byte) -35, GetProcessingOptions.INS, ApplicationInfoManager.TERM_XP3, VerifyPINApdu.P2_CIPHERED, (byte) 7, (byte) -57, (byte) 49, (byte) -79, CLD.FORM_FACTOR_TAG, Tnaf.POW_2_WIDTH, (byte) 89, (byte) 39, VerifyPINApdu.P2_PLAINTEXT, (byte) -20, MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) 96, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, Byte.MAX_VALUE, (byte) -87, (byte) 25, (byte) -75, (byte) 74, (byte) 13, SetResetParamApdu.INS, (byte) -27, (byte) 122, (byte) -97, (byte) -109, (byte) -55, (byte) -100, (byte) -17, (byte) -96, (byte) -32, (byte) 59, (byte) 77, MPPLiteInstruction.INS_GENERATE_AC, GenerateACApdu.INS, (byte) -11, (byte) -80, (byte) -56, (byte) -21, (byte) -69, (byte) 60, (byte) -125, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) -103, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, CardSide.PIN_TEXT_TAG, (byte) 43, (byte) 4, (byte) 126, (byte) -70, ApplicationInfoManager.TERM_XP2, (byte) -42, (byte) 38, (byte) -31, (byte) 105, CardSide.PICTURE_TAG, (byte) 99, (byte) 85, (byte) 33, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 125};
        rcon = new int[]{1, 2, 4, 8, BLOCK_SIZE, 32, 64, X509KeyUsage.digitalSignature, m3, 54, CipherSuite.TLS_DH_anon_WITH_AES_128_CBC_SHA256, 216, CipherSuite.TLS_DHE_PSK_WITH_AES_256_GCM_SHA384, 77, CipherSuite.TLS_DHE_RSA_WITH_SEED_CBC_SHA, 47, 94, CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_CBC_SHA256, 99, 198, CipherSuite.TLS_DH_DSS_WITH_SEED_CBC_SHA, 53, CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA256, 212, CipherSuite.TLS_DHE_PSK_WITH_AES_256_CBC_SHA384, EACTags.SECURE_MESSAGING_TEMPLATE, 250, 239, CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256, CipherSuite.TLS_DHE_PSK_WITH_AES_256_CBC_SHA};
        T0 = new int[]{-1520213050, -2072216328, -1720223762, -1921287178, 234025727, -1117033514, -1318096930, 1422247313, 1345335392, 50397442, -1452841010, 2099981142, 436141799, 1658312629, -424957107, -1703512340, 1170918031, -1652391393, 1086966153, -2021818886, 368769775, -346465870, -918075506, 200339707, -324162239, 1742001331, -39673249, -357585083, -1080255453, -140204973, -1770884380, 1539358875, -1028147339, 486407649, -1366060227, 1780885068, 1513502316, 1094664062, 49805301, 1338821763, 1546925160, -190470831, 887481809, 150073849, -1821281822, 1943591083, 1395732834, 1058346282, 201589768, 1388824469, 1696801606, 1589887901, 672667696, -1583966665, 251987210, -1248159185, 151455502, 907153956, -1686077413, 1038279391, 652995533, 1764173646, -843926913, -1619692054, 453576978, -1635548387, 1949051992, 773462580, 756751158, -1301385508, -296068428, -73359269, -162377052, 1295727478, 1641469623, -827083907, 2066295122, 1055122397, 1898917726, -1752923117, -179088474, 1758581177, 0, 753790401, 1612718144, 536673507, -927878791, -312779850, -1100322092, 1187761037, -641810841, 1262041458, -565556588, -733197160, -396863312, 1255133061, 1808847035, 720367557, -441800113, 385612781, -985447546, -682799718, 1429418854, -1803188975, -817543798, 284817897, 100794884, -2122350594, -263171936, 1144798328, -1163944155, -475486133, -212774494, -22830243, -1069531008, -1970303227, -1382903233, -1130521311, 1211644016, 83228145, -541279133, -1044990345, 1977277103, 1663115586, 806359072, 452984805, 250868733, 1842533055, 1288555905, 336333848, 890442534, 804056259, -513843266, -1567123659, -867941240, 957814574, 1472513171, -223893675, -2105639172, 1195195770, -1402706744, -413311558, 723065138, -1787595802, -1604296512, -1736343271, -783331426, 2145180835, 1713513028, 2116692564, -1416589253, -2088204277, -901364084, 703524551, -742868885, 1007948840, 2044649127, -497131844, 487262998, 1994120109, 1004593371, 1446130276, 1312438900, 503974420, -615954030, 168166924, 1814307912, -463709000, 1573044895, 1859376061, -273896381, -1503501628, -1466855111, -1533700815, 937747667, -1954973198, 854058965, 1137232011, 1496790894, -1217565222, -1936880383, 1691735473, -766620004, -525751991, -1267962664, -95005012, 133494003, 636152527, -1352309302, -1904575756, -374428089, 403179536, -709182865, -2005370640, 1864705354, 1915629148, 605822008, -240736681, -944458637, 1371981463, 602466507, 2094914977, -1670089496, 555687742, -582268010, -591544991, -2037675251, -2054518257, -1871679264, 1111375484, -994724495, -1436129588, -666351472, 84083462, 32962295, 302911004, -1553899070, 1597322602, -111716434, -793134743, -1853454825, 1489093017, 656219450, -1180787161, 954327513, 335083755, -1281845205, 856756514, -1150719534, 1893325225, -1987146233, -1483434957, -1231316179, 572399164, -1836611819, 552200649, 1238290055, -11184726, 2015897680, 2061492133, -1886614525, -123625127, -2138470135, 386731290, -624967835, 837215959, -968736124, -1201116976, -1019133566, -1332111063, 1999449434, 286199582, -877612933, -61582168, -692339859, 974525996};
        Tinv0 = new int[]{1353184337, 1399144830, -1012656358, -1772214470, -882136261, -247096033, -1420232020, -1828461749, 1442459680, -160598355, -1854485368, 625738485, -52959921, -674551099, -2143013594, -1885117771, 1230680542, 1729870373, -1743852987, -507445667, 41234371, 317738113, -1550367091, -956705941, -413167869, -1784901099, -344298049, -631680363, 763608788, -752782248, 694804553, 1154009486, 1787413109, 2021232372, 1799248025, -579749593, -1236278850, 397248752, 1722556617, -1271214467, 407560035, -2110711067, 1613975959, 1165972322, -529046351, -2068943941, 480281086, -1809118983, 1483229296, 436028815, -2022908268, -1208452270, 601060267, -503166094, 1468997603, 715871590, 120122290, 63092015, -1703164538, -1526188077, -226023376, -1297760477, -1167457534, 1552029421, 723308426, -1833666137, -252573709, -1578997426, -839591323, -708967162, 526529745, -1963022652, -1655493068, -1604979806, 853641733, 1978398372, 971801355, -1427152832, 111112542, 1360031421, -108388034, 1023860118, -1375387939, 1186850381, -1249028975, 90031217, 1876166148, -15380384, 620468249, -1746289194, -868007799, 2006899047, -1119688528, -2004121337, 945494503, -605108103, 1191869601, -384875908, -920746760, 0, -2088337399, 1223502642, -1401941730, 1316117100, -67170563, 1446544655, 517320253, 658058550, 1691946762, 564550760, -783000677, 976107044, -1318647284, 266819475, -761860428, -1634624741, 1338359936, -1574904735, 1766553434, 370807324, 179999714, -450191168, 1138762300, 488053522, 185403662, -1379431438, -1180125651, -928440812, -2061897385, 1275557295, -1143105042, -44007517, -1624899081, -1124765092, -985962940, 880737115, 1982415755, -590994485, 1761406390, 1676797112, -891538985, 277177154, 1076008723, 538035844, 2099530373, -130171950, 288553390, 1839278535, 1261411869, -214912292, -330136051, -790380169, 1813426987, -1715900247, -95906799, 577038663, -997393240, 440397984, -668172970, -275762398, -951170681, -1043253031, -22885748, 906744984, -813566554, 685669029, 646887386, -1530942145, -459458004, 227702864, -1681105046, 1648787028, -1038905866, -390539120, 1593260334, -173030526, -1098883681, 2090061929, -1456614033, -1290656305, 999926984, -1484974064, 1852021992, 2075868123, 158869197, -199730834, 28809964, -1466282109, 1701746150, 2129067946, 147831841, -420997649, -644094022, -835293366, -737566742, -696471511, -1347247055, 824393514, 815048134, -1067015627, 935087732, -1496677636, -1328508704, 366520115, 1251476721, -136647615, 240176511, 804688151, -1915335306, 1303441219, 1414376140, -553347356, -474623586, 461924940, -1205916479, 2136040774, 82468509, 1563790337, 1937016826, 776014843, 1511876531, 1389550482, 861278441, 323475053, -1939744870, 2047648055, -1911228327, -1992551445, -299390514, 902390199, -303751967, 1018251130, 1507840668, 1064563285, 2043548696, -1086863501, -355600557, 1537932639, 342834655, -2032450440, -2114736182, 1053059257, 741614648, 1598071746, 1925389590, 203809468, -1958134744, 1100287487, 1895934009, -558691320, -1662733096, -1866377628, 1636092795, 1890988757, 1952214088, 1113045200};
    }

    public AESEngine() {
        this.WorkingKey = (int[][]) null;
    }

    private static int FFmulX(int i) {
        return ((m2 & i) << 1) ^ (((m1 & i) >>> 7) * m3);
    }

    private void decryptBlock(int[][] iArr) {
        int shift;
        int shift2;
        int shift3;
        int i = this.C0 ^ iArr[this.ROUNDS][0];
        int i2 = this.C1 ^ iArr[this.ROUNDS][1];
        int i3 = this.C2 ^ iArr[this.ROUNDS][2];
        int i4 = this.ROUNDS - 1;
        int i5 = this.C3 ^ iArr[this.ROUNDS][3];
        while (i4 > 1) {
            shift = (((Tinv0[i & GF2Field.MASK] ^ shift(Tinv0[(i5 >> 8) & GF2Field.MASK], 24)) ^ shift(Tinv0[(i3 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(Tinv0[(i2 >> 24) & GF2Field.MASK], 8)) ^ iArr[i4][0];
            shift2 = (((Tinv0[i2 & GF2Field.MASK] ^ shift(Tinv0[(i >> 8) & GF2Field.MASK], 24)) ^ shift(Tinv0[(i5 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(Tinv0[(i3 >> 24) & GF2Field.MASK], 8)) ^ iArr[i4][1];
            shift3 = (((Tinv0[i3 & GF2Field.MASK] ^ shift(Tinv0[(i2 >> 8) & GF2Field.MASK], 24)) ^ shift(Tinv0[(i >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(Tinv0[(i5 >> 24) & GF2Field.MASK], 8)) ^ iArr[i4][2];
            int i6 = i4 - 1;
            i5 = (((Tinv0[i5 & GF2Field.MASK] ^ shift(Tinv0[(i3 >> 8) & GF2Field.MASK], 24)) ^ shift(Tinv0[(i2 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(Tinv0[(i >> 24) & GF2Field.MASK], 8)) ^ iArr[i4][3];
            i = (((Tinv0[shift & GF2Field.MASK] ^ shift(Tinv0[(i5 >> 8) & GF2Field.MASK], 24)) ^ shift(Tinv0[(shift3 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(Tinv0[(shift2 >> 24) & GF2Field.MASK], 8)) ^ iArr[i6][0];
            i2 = (((Tinv0[shift2 & GF2Field.MASK] ^ shift(Tinv0[(shift >> 8) & GF2Field.MASK], 24)) ^ shift(Tinv0[(i5 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(Tinv0[(shift3 >> 24) & GF2Field.MASK], 8)) ^ iArr[i6][1];
            i3 = iArr[i6][2] ^ (((Tinv0[shift3 & GF2Field.MASK] ^ shift(Tinv0[(shift2 >> 8) & GF2Field.MASK], 24)) ^ shift(Tinv0[(shift >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(Tinv0[(i5 >> 24) & GF2Field.MASK], 8));
            i4 = i6 - 1;
            i5 = (((Tinv0[i5 & GF2Field.MASK] ^ shift(Tinv0[(shift3 >> 8) & GF2Field.MASK], 24)) ^ shift(Tinv0[(shift2 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(Tinv0[(shift >> 24) & GF2Field.MASK], 8)) ^ iArr[i6][3];
        }
        shift = (((Tinv0[i & GF2Field.MASK] ^ shift(Tinv0[(i5 >> 8) & GF2Field.MASK], 24)) ^ shift(Tinv0[(i3 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(Tinv0[(i2 >> 24) & GF2Field.MASK], 8)) ^ iArr[i4][0];
        shift2 = (((Tinv0[i2 & GF2Field.MASK] ^ shift(Tinv0[(i >> 8) & GF2Field.MASK], 24)) ^ shift(Tinv0[(i5 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(Tinv0[(i3 >> 24) & GF2Field.MASK], 8)) ^ iArr[i4][1];
        shift3 = (((Tinv0[i3 & GF2Field.MASK] ^ shift(Tinv0[(i2 >> 8) & GF2Field.MASK], 24)) ^ shift(Tinv0[(i >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(Tinv0[(i5 >> 24) & GF2Field.MASK], 8)) ^ iArr[i4][2];
        i5 = (((Tinv0[i5 & GF2Field.MASK] ^ shift(Tinv0[(i3 >> 8) & GF2Field.MASK], 24)) ^ shift(Tinv0[(i2 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(Tinv0[(i >> 24) & GF2Field.MASK], 8)) ^ iArr[i4][3];
        this.C0 = ((((Si[shift & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(i5 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(shift3 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(shift2 >> 24) & GF2Field.MASK] << 24)) ^ iArr[0][0];
        this.C1 = ((((Si[shift2 & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(shift >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(i5 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(shift3 >> 24) & GF2Field.MASK] << 24)) ^ iArr[0][1];
        this.C2 = ((((Si[shift3 & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(shift2 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(shift >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(i5 >> 24) & GF2Field.MASK] << 24)) ^ iArr[0][2];
        this.C3 = ((((Si[i5 & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(shift3 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(shift2 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(shift >> 24) & GF2Field.MASK] << 24)) ^ iArr[0][3];
    }

    private void encryptBlock(int[][] iArr) {
        int shift;
        int shift2;
        int shift3;
        int i = this.C0 ^ iArr[0][0];
        int i2 = this.C1 ^ iArr[0][1];
        int i3 = this.C2 ^ iArr[0][2];
        int i4 = 1;
        int i5 = this.C3 ^ iArr[0][3];
        while (i4 < this.ROUNDS - 1) {
            shift = (((T0[i & GF2Field.MASK] ^ shift(T0[(i2 >> 8) & GF2Field.MASK], 24)) ^ shift(T0[(i3 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(T0[(i5 >> 24) & GF2Field.MASK], 8)) ^ iArr[i4][0];
            shift2 = (((T0[i2 & GF2Field.MASK] ^ shift(T0[(i3 >> 8) & GF2Field.MASK], 24)) ^ shift(T0[(i5 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(T0[(i >> 24) & GF2Field.MASK], 8)) ^ iArr[i4][1];
            shift3 = (((T0[i3 & GF2Field.MASK] ^ shift(T0[(i5 >> 8) & GF2Field.MASK], 24)) ^ shift(T0[(i >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(T0[(i2 >> 24) & GF2Field.MASK], 8)) ^ iArr[i4][2];
            int i6 = i4 + 1;
            i5 = (((T0[i5 & GF2Field.MASK] ^ shift(T0[(i >> 8) & GF2Field.MASK], 24)) ^ shift(T0[(i2 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(T0[(i3 >> 24) & GF2Field.MASK], 8)) ^ iArr[i4][3];
            i = (((T0[shift & GF2Field.MASK] ^ shift(T0[(shift2 >> 8) & GF2Field.MASK], 24)) ^ shift(T0[(shift3 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(T0[(i5 >> 24) & GF2Field.MASK], 8)) ^ iArr[i6][0];
            i2 = (((T0[shift2 & GF2Field.MASK] ^ shift(T0[(shift3 >> 8) & GF2Field.MASK], 24)) ^ shift(T0[(i5 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(T0[(shift >> 24) & GF2Field.MASK], 8)) ^ iArr[i6][1];
            i3 = iArr[i6][2] ^ (((T0[shift3 & GF2Field.MASK] ^ shift(T0[(i5 >> 8) & GF2Field.MASK], 24)) ^ shift(T0[(shift >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(T0[(shift2 >> 24) & GF2Field.MASK], 8));
            i4 = i6 + 1;
            i5 = (((T0[i5 & GF2Field.MASK] ^ shift(T0[(shift >> 8) & GF2Field.MASK], 24)) ^ shift(T0[(shift2 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(T0[(shift3 >> 24) & GF2Field.MASK], 8)) ^ iArr[i6][3];
        }
        shift = (((T0[i & GF2Field.MASK] ^ shift(T0[(i2 >> 8) & GF2Field.MASK], 24)) ^ shift(T0[(i3 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(T0[(i5 >> 24) & GF2Field.MASK], 8)) ^ iArr[i4][0];
        shift2 = (((T0[i2 & GF2Field.MASK] ^ shift(T0[(i3 >> 8) & GF2Field.MASK], 24)) ^ shift(T0[(i5 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(T0[(i >> 24) & GF2Field.MASK], 8)) ^ iArr[i4][1];
        shift3 = (((T0[i3 & GF2Field.MASK] ^ shift(T0[(i5 >> 8) & GF2Field.MASK], 24)) ^ shift(T0[(i >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(T0[(i2 >> 24) & GF2Field.MASK], 8)) ^ iArr[i4][2];
        i5 = ((T0[i5 & GF2Field.MASK] ^ shift(T0[(i >> 8) & GF2Field.MASK], 24)) ^ shift(T0[(i2 >> BLOCK_SIZE) & GF2Field.MASK], BLOCK_SIZE)) ^ shift(T0[(i3 >> 24) & GF2Field.MASK], 8);
        i3 = i4 + 1;
        i5 ^= iArr[i4][3];
        this.C0 = ((((f145S[shift & GF2Field.MASK] & GF2Field.MASK) ^ ((f145S[(shift2 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f145S[(shift3 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f145S[(i5 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i3][0];
        this.C1 = ((((f145S[shift2 & GF2Field.MASK] & GF2Field.MASK) ^ ((f145S[(shift3 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f145S[(i5 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f145S[(shift >> 24) & GF2Field.MASK] << 24)) ^ iArr[i3][1];
        this.C2 = ((((f145S[shift3 & GF2Field.MASK] & GF2Field.MASK) ^ ((f145S[(i5 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f145S[(shift >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f145S[(shift2 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i3][2];
        this.C3 = ((((f145S[i5 & GF2Field.MASK] & GF2Field.MASK) ^ ((f145S[(shift >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f145S[(shift2 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f145S[(shift3 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i3][3];
    }

    private int[][] generateWorkingKey(byte[] bArr, boolean z) {
        int length = bArr.length / 4;
        if ((length == 4 || length == 6 || length == 8) && length * 4 == bArr.length) {
            this.ROUNDS = length + 6;
            int[][] iArr = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.ROUNDS + 1, 4});
            int i = 0;
            int i2 = 0;
            while (i < bArr.length) {
                iArr[i2 >> 2][i2 & 3] = (((bArr[i] & GF2Field.MASK) | ((bArr[i + 1] & GF2Field.MASK) << 8)) | ((bArr[i + 2] & GF2Field.MASK) << BLOCK_SIZE)) | (bArr[i + 3] << 24);
                i += 4;
                i2++;
            }
            int i3 = (this.ROUNDS + 1) << 2;
            i2 = length;
            while (i2 < i3) {
                i = iArr[(i2 - 1) >> 2][(i2 - 1) & 3];
                if (i2 % length == 0) {
                    i = subWord(shift(i, 8)) ^ rcon[(i2 / length) - 1];
                } else if (length > 6 && i2 % length == 4) {
                    i = subWord(i);
                }
                iArr[i2 >> 2][i2 & 3] = i ^ iArr[(i2 - length) >> 2][(i2 - length) & 3];
                i2++;
            }
            if (!z) {
                for (i = 1; i < this.ROUNDS; i++) {
                    for (i2 = 0; i2 < 4; i2++) {
                        iArr[i][i2] = inv_mcol(iArr[i][i2]);
                    }
                }
            }
            return iArr;
        }
        throw new IllegalArgumentException("Key length not 128/192/256 bits.");
    }

    private static int inv_mcol(int i) {
        int FFmulX = FFmulX(i);
        int FFmulX2 = FFmulX(FFmulX);
        int FFmulX3 = FFmulX(FFmulX2);
        int i2 = i ^ FFmulX3;
        return ((shift(FFmulX ^ i2, 8) ^ (FFmulX3 ^ (FFmulX ^ FFmulX2))) ^ shift(FFmulX2 ^ i2, BLOCK_SIZE)) ^ shift(i2, 24);
    }

    private void packBlock(byte[] bArr, int i) {
        int i2 = i + 1;
        bArr[i] = (byte) this.C0;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (this.C0 >> 8);
        i2 = i3 + 1;
        bArr[i3] = (byte) (this.C0 >> BLOCK_SIZE);
        i3 = i2 + 1;
        bArr[i2] = (byte) (this.C0 >> 24);
        i2 = i3 + 1;
        bArr[i3] = (byte) this.C1;
        i3 = i2 + 1;
        bArr[i2] = (byte) (this.C1 >> 8);
        i2 = i3 + 1;
        bArr[i3] = (byte) (this.C1 >> BLOCK_SIZE);
        i3 = i2 + 1;
        bArr[i2] = (byte) (this.C1 >> 24);
        i2 = i3 + 1;
        bArr[i3] = (byte) this.C2;
        i3 = i2 + 1;
        bArr[i2] = (byte) (this.C2 >> 8);
        i2 = i3 + 1;
        bArr[i3] = (byte) (this.C2 >> BLOCK_SIZE);
        i3 = i2 + 1;
        bArr[i2] = (byte) (this.C2 >> 24);
        i2 = i3 + 1;
        bArr[i3] = (byte) this.C3;
        i3 = i2 + 1;
        bArr[i2] = (byte) (this.C3 >> 8);
        i2 = i3 + 1;
        bArr[i3] = (byte) (this.C3 >> BLOCK_SIZE);
        i3 = i2 + 1;
        bArr[i2] = (byte) (this.C3 >> 24);
    }

    private static int shift(int i, int i2) {
        return (i >>> i2) | (i << (-i2));
    }

    private static int subWord(int i) {
        return (((f145S[i & GF2Field.MASK] & GF2Field.MASK) | ((f145S[(i >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) | ((f145S[(i >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) | (f145S[(i >> 24) & GF2Field.MASK] << 24);
    }

    private void unpackBlock(byte[] bArr, int i) {
        int i2 = i + 1;
        this.C0 = bArr[i] & GF2Field.MASK;
        int i3 = i2 + 1;
        this.C0 = ((bArr[i2] & GF2Field.MASK) << 8) | this.C0;
        int i4 = i3 + 1;
        this.C0 |= (bArr[i3] & GF2Field.MASK) << BLOCK_SIZE;
        i3 = i4 + 1;
        this.C0 |= bArr[i4] << 24;
        i2 = i3 + 1;
        this.C1 = bArr[i3] & GF2Field.MASK;
        i3 = i2 + 1;
        this.C1 = ((bArr[i2] & GF2Field.MASK) << 8) | this.C1;
        i4 = i3 + 1;
        this.C1 |= (bArr[i3] & GF2Field.MASK) << BLOCK_SIZE;
        i3 = i4 + 1;
        this.C1 |= bArr[i4] << 24;
        i2 = i3 + 1;
        this.C2 = bArr[i3] & GF2Field.MASK;
        i3 = i2 + 1;
        this.C2 = ((bArr[i2] & GF2Field.MASK) << 8) | this.C2;
        i4 = i3 + 1;
        this.C2 |= (bArr[i3] & GF2Field.MASK) << BLOCK_SIZE;
        i3 = i4 + 1;
        this.C2 |= bArr[i4] << 24;
        i2 = i3 + 1;
        this.C3 = bArr[i3] & GF2Field.MASK;
        i3 = i2 + 1;
        this.C3 = ((bArr[i2] & GF2Field.MASK) << 8) | this.C3;
        i4 = i3 + 1;
        this.C3 |= (bArr[i3] & GF2Field.MASK) << BLOCK_SIZE;
        i3 = i4 + 1;
        this.C3 |= bArr[i4] << 24;
    }

    public String getAlgorithmName() {
        return "AES";
    }

    public int getBlockSize() {
        return BLOCK_SIZE;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.WorkingKey = generateWorkingKey(((KeyParameter) cipherParameters).getKey(), z);
            this.forEncryption = z;
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to AES init - " + cipherParameters.getClass().getName());
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.WorkingKey == null) {
            throw new IllegalStateException("AES engine not initialised");
        } else if (i + BLOCK_SIZE > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i2 + BLOCK_SIZE > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            if (this.forEncryption) {
                unpackBlock(bArr, i);
                encryptBlock(this.WorkingKey);
                packBlock(bArr2, i2);
            } else {
                unpackBlock(bArr, i);
                decryptBlock(this.WorkingKey);
                packBlock(bArr2, i2);
            }
            return BLOCK_SIZE;
        }
    }

    public void reset() {
    }
}
