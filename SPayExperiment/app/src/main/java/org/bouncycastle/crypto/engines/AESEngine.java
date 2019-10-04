/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Array
 */
package org.bouncycastle.crypto.engines;

import java.lang.reflect.Array;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

public class AESEngine
implements BlockCipher {
    private static final int BLOCK_SIZE = 16;
    private static final byte[] S = new byte[]{99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118, -54, -126, -55, 125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64, -73, -3, -109, 38, 54, 63, -9, -52, 52, -91, -27, -15, 113, -40, 49, 21, 4, -57, 35, -61, 24, -106, 5, -102, 7, 18, -128, -30, -21, 39, -78, 117, 9, -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 83, -47, 0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49, -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88, 81, -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1, -13, -46, -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, 61, 100, 93, 25, 115, 96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, -37, -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118, 112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, -31, -8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33, -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22};
    private static final byte[] Si = new byte[]{82, 9, 106, -43, 48, 54, -91, 56, -65, 64, -93, -98, -127, -13, -41, -5, 124, -29, 57, -126, -101, 47, -1, -121, 52, -114, 67, 68, -60, -34, -23, -53, 84, 123, -108, 50, -90, -62, 35, 61, -18, 76, -107, 11, 66, -6, -61, 78, 8, 46, -95, 102, 40, -39, 36, -78, 118, 91, -94, 73, 109, -117, -47, 37, 114, -8, -10, 100, -122, 104, -104, 22, -44, -92, 92, -52, 93, 101, -74, -110, 108, 112, 72, 80, -3, -19, -71, -38, 94, 21, 70, 87, -89, -115, -99, -124, -112, -40, -85, 0, -116, -68, -45, 10, -9, -28, 88, 5, -72, -77, 69, 6, -48, 44, 30, -113, -54, 63, 15, 2, -63, -81, -67, 3, 1, 19, -118, 107, 58, -111, 17, 65, 79, 103, -36, -22, -105, -14, -49, -50, -16, -76, -26, 115, -106, -84, 116, 34, -25, -83, 53, -123, -30, -7, 55, -24, 28, 117, -33, 110, 71, -15, 26, 113, 29, 41, -59, -119, 111, -73, 98, 14, -86, 24, -66, 27, -4, 86, 62, 75, -58, -46, 121, 32, -102, -37, -64, -2, 120, -51, 90, -12, 31, -35, -88, 51, -120, 7, -57, 49, -79, 18, 16, 89, 39, -128, -20, 95, 96, 81, 127, -87, 25, -75, 74, 13, 45, -27, 122, -97, -109, -55, -100, -17, -96, -32, 59, 77, -82, 42, -11, -80, -56, -21, -69, 60, -125, 83, -103, 97, 23, 43, 4, 126, -70, 119, -42, 38, -31, 105, 20, 99, 85, 33, 12, 125};
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
    private int[][] WorkingKey = null;
    private boolean forEncryption;

    static {
        rcon = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212, 179, 125, 250, 239, 197, 145};
        T0 = new int[]{-1520213050, -2072216328, -1720223762, -1921287178, 234025727, -1117033514, -1318096930, 1422247313, 1345335392, 50397442, -1452841010, 2099981142, 436141799, 1658312629, -424957107, -1703512340, 1170918031, -1652391393, 1086966153, -2021818886, 368769775, -346465870, -918075506, 200339707, -324162239, 1742001331, -39673249, -357585083, -1080255453, -140204973, -1770884380, 1539358875, -1028147339, 486407649, -1366060227, 1780885068, 1513502316, 1094664062, 49805301, 1338821763, 1546925160, -190470831, 887481809, 150073849, -1821281822, 1943591083, 1395732834, 1058346282, 201589768, 1388824469, 1696801606, 1589887901, 672667696, -1583966665, 251987210, -1248159185, 151455502, 907153956, -1686077413, 1038279391, 652995533, 1764173646, -843926913, -1619692054, 453576978, -1635548387, 1949051992, 773462580, 756751158, -1301385508, -296068428, -73359269, -162377052, 1295727478, 1641469623, -827083907, 2066295122, 1055122397, 1898917726, -1752923117, -179088474, 1758581177, 0, 753790401, 1612718144, 536673507, -927878791, -312779850, -1100322092, 1187761037, -641810841, 1262041458, -565556588, -733197160, -396863312, 1255133061, 1808847035, 720367557, -441800113, 385612781, -985447546, -682799718, 1429418854, -1803188975, -817543798, 284817897, 100794884, -2122350594, -263171936, 1144798328, -1163944155, -475486133, -212774494, -22830243, -1069531008, -1970303227, -1382903233, -1130521311, 1211644016, 83228145, -541279133, -1044990345, 1977277103, 1663115586, 806359072, 452984805, 250868733, 1842533055, 1288555905, 336333848, 890442534, 804056259, -513843266, -1567123659, -867941240, 957814574, 1472513171, -223893675, -2105639172, 1195195770, -1402706744, -413311558, 723065138, -1787595802, -1604296512, -1736343271, -783331426, 2145180835, 1713513028, 2116692564, -1416589253, -2088204277, -901364084, 703524551, -742868885, 1007948840, 2044649127, -497131844, 487262998, 1994120109, 1004593371, 1446130276, 1312438900, 503974420, -615954030, 168166924, 1814307912, -463709000, 1573044895, 1859376061, -273896381, -1503501628, -1466855111, -1533700815, 937747667, -1954973198, 854058965, 1137232011, 1496790894, -1217565222, -1936880383, 1691735473, -766620004, -525751991, -1267962664, -95005012, 133494003, 636152527, -1352309302, -1904575756, -374428089, 403179536, -709182865, -2005370640, 1864705354, 1915629148, 605822008, -240736681, -944458637, 1371981463, 602466507, 2094914977, -1670089496, 555687742, -582268010, -591544991, -2037675251, -2054518257, -1871679264, 1111375484, -994724495, -1436129588, -666351472, 84083462, 32962295, 302911004, -1553899070, 1597322602, -111716434, -793134743, -1853454825, 1489093017, 656219450, -1180787161, 954327513, 335083755, -1281845205, 856756514, -1150719534, 1893325225, -1987146233, -1483434957, -1231316179, 572399164, -1836611819, 552200649, 1238290055, -11184726, 2015897680, 2061492133, -1886614525, -123625127, -2138470135, 386731290, -624967835, 837215959, -968736124, -1201116976, -1019133566, -1332111063, 1999449434, 286199582, -877612933, -61582168, -692339859, 974525996};
        Tinv0 = new int[]{1353184337, 1399144830, -1012656358, -1772214470, -882136261, -247096033, -1420232020, -1828461749, 1442459680, -160598355, -1854485368, 625738485, -52959921, -674551099, -2143013594, -1885117771, 1230680542, 1729870373, -1743852987, -507445667, 41234371, 317738113, -1550367091, -956705941, -413167869, -1784901099, -344298049, -631680363, 763608788, -752782248, 694804553, 1154009486, 1787413109, 2021232372, 1799248025, -579749593, -1236278850, 397248752, 1722556617, -1271214467, 407560035, -2110711067, 1613975959, 1165972322, -529046351, -2068943941, 480281086, -1809118983, 1483229296, 436028815, -2022908268, -1208452270, 601060267, -503166094, 1468997603, 715871590, 120122290, 63092015, -1703164538, -1526188077, -226023376, -1297760477, -1167457534, 1552029421, 723308426, -1833666137, -252573709, -1578997426, -839591323, -708967162, 526529745, -1963022652, -1655493068, -1604979806, 853641733, 1978398372, 971801355, -1427152832, 111112542, 1360031421, -108388034, 1023860118, -1375387939, 1186850381, -1249028975, 90031217, 1876166148, -15380384, 620468249, -1746289194, -868007799, 2006899047, -1119688528, -2004121337, 945494503, -605108103, 1191869601, -384875908, -920746760, 0, -2088337399, 1223502642, -1401941730, 1316117100, -67170563, 1446544655, 517320253, 658058550, 1691946762, 564550760, -783000677, 976107044, -1318647284, 266819475, -761860428, -1634624741, 1338359936, -1574904735, 1766553434, 370807324, 179999714, -450191168, 1138762300, 488053522, 185403662, -1379431438, -1180125651, -928440812, -2061897385, 1275557295, -1143105042, -44007517, -1624899081, -1124765092, -985962940, 880737115, 1982415755, -590994485, 1761406390, 1676797112, -891538985, 277177154, 1076008723, 538035844, 2099530373, -130171950, 288553390, 1839278535, 1261411869, -214912292, -330136051, -790380169, 1813426987, -1715900247, -95906799, 577038663, -997393240, 440397984, -668172970, -275762398, -951170681, -1043253031, -22885748, 906744984, -813566554, 685669029, 646887386, -1530942145, -459458004, 227702864, -1681105046, 1648787028, -1038905866, -390539120, 1593260334, -173030526, -1098883681, 2090061929, -1456614033, -1290656305, 999926984, -1484974064, 1852021992, 2075868123, 158869197, -199730834, 28809964, -1466282109, 1701746150, 2129067946, 147831841, -420997649, -644094022, -835293366, -737566742, -696471511, -1347247055, 824393514, 815048134, -1067015627, 935087732, -1496677636, -1328508704, 366520115, 1251476721, -136647615, 240176511, 804688151, -1915335306, 1303441219, 1414376140, -553347356, -474623586, 461924940, -1205916479, 2136040774, 82468509, 1563790337, 1937016826, 776014843, 1511876531, 1389550482, 861278441, 323475053, -1939744870, 2047648055, -1911228327, -1992551445, -299390514, 902390199, -303751967, 1018251130, 1507840668, 1064563285, 2043548696, -1086863501, -355600557, 1537932639, 342834655, -2032450440, -2114736182, 1053059257, 741614648, 1598071746, 1925389590, 203809468, -1958134744, 1100287487, 1895934009, -558691320, -1662733096, -1866377628, 1636092795, 1890988757, 1952214088, 1113045200};
    }

    private static int FFmulX(int n2) {
        return (2139062143 & n2) << 1 ^ 27 * ((-2139062144 & n2) >>> 7);
    }

    private void decryptBlock(int[][] arrn) {
        int n2 = this.C0 ^ arrn[this.ROUNDS][0];
        int n3 = this.C1 ^ arrn[this.ROUNDS][1];
        int n4 = this.C2 ^ arrn[this.ROUNDS][2];
        int n5 = -1 + this.ROUNDS;
        int n6 = this.C3 ^ arrn[this.ROUNDS][3];
        while (n5 > 1) {
            int n7 = Tinv0[n2 & 255] ^ AESEngine.shift(Tinv0[255 & n6 >> 8], 24) ^ AESEngine.shift(Tinv0[255 & n4 >> 16], 16) ^ AESEngine.shift(Tinv0[255 & n3 >> 24], 8) ^ arrn[n5][0];
            int n8 = Tinv0[n3 & 255] ^ AESEngine.shift(Tinv0[255 & n2 >> 8], 24) ^ AESEngine.shift(Tinv0[255 & n6 >> 16], 16) ^ AESEngine.shift(Tinv0[255 & n4 >> 24], 8) ^ arrn[n5][1];
            int n9 = Tinv0[n4 & 255] ^ AESEngine.shift(Tinv0[255 & n3 >> 8], 24) ^ AESEngine.shift(Tinv0[255 & n2 >> 16], 16) ^ AESEngine.shift(Tinv0[255 & n6 >> 24], 8) ^ arrn[n5][2];
            int n10 = Tinv0[n6 & 255] ^ AESEngine.shift(Tinv0[255 & n4 >> 8], 24) ^ AESEngine.shift(Tinv0[255 & n3 >> 16], 16) ^ AESEngine.shift(Tinv0[255 & n2 >> 24], 8);
            int n11 = n5 - 1;
            int n12 = n10 ^ arrn[n5][3];
            n2 = Tinv0[n7 & 255] ^ AESEngine.shift(Tinv0[255 & n12 >> 8], 24) ^ AESEngine.shift(Tinv0[255 & n9 >> 16], 16) ^ AESEngine.shift(Tinv0[255 & n8 >> 24], 8) ^ arrn[n11][0];
            n3 = Tinv0[n8 & 255] ^ AESEngine.shift(Tinv0[255 & n7 >> 8], 24) ^ AESEngine.shift(Tinv0[255 & n12 >> 16], 16) ^ AESEngine.shift(Tinv0[255 & n9 >> 24], 8) ^ arrn[n11][1];
            n4 = Tinv0[n9 & 255] ^ AESEngine.shift(Tinv0[255 & n8 >> 8], 24) ^ AESEngine.shift(Tinv0[255 & n7 >> 16], 16) ^ AESEngine.shift(Tinv0[255 & n12 >> 24], 8) ^ arrn[n11][2];
            int n13 = Tinv0[n12 & 255] ^ AESEngine.shift(Tinv0[255 & n9 >> 8], 24) ^ AESEngine.shift(Tinv0[255 & n8 >> 16], 16) ^ AESEngine.shift(Tinv0[255 & n7 >> 24], 8);
            n5 = n11 - 1;
            n6 = n13 ^ arrn[n11][3];
        }
        int n14 = Tinv0[n2 & 255] ^ AESEngine.shift(Tinv0[255 & n6 >> 8], 24) ^ AESEngine.shift(Tinv0[255 & n4 >> 16], 16) ^ AESEngine.shift(Tinv0[255 & n3 >> 24], 8) ^ arrn[n5][0];
        int n15 = Tinv0[n3 & 255] ^ AESEngine.shift(Tinv0[255 & n2 >> 8], 24) ^ AESEngine.shift(Tinv0[255 & n6 >> 16], 16) ^ AESEngine.shift(Tinv0[255 & n4 >> 24], 8) ^ arrn[n5][1];
        int n16 = Tinv0[n4 & 255] ^ AESEngine.shift(Tinv0[255 & n3 >> 8], 24) ^ AESEngine.shift(Tinv0[255 & n2 >> 16], 16) ^ AESEngine.shift(Tinv0[255 & n6 >> 24], 8) ^ arrn[n5][2];
        int n17 = Tinv0[n6 & 255] ^ AESEngine.shift(Tinv0[255 & n4 >> 8], 24) ^ AESEngine.shift(Tinv0[255 & n3 >> 16], 16) ^ AESEngine.shift(Tinv0[255 & n2 >> 24], 8) ^ arrn[n5][3];
        this.C0 = 255 & Si[n14 & 255] ^ (255 & Si[255 & n17 >> 8]) << 8 ^ (255 & Si[255 & n16 >> 16]) << 16 ^ Si[255 & n15 >> 24] << 24 ^ arrn[0][0];
        this.C1 = 255 & Si[n15 & 255] ^ (255 & Si[255 & n14 >> 8]) << 8 ^ (255 & Si[255 & n17 >> 16]) << 16 ^ Si[255 & n16 >> 24] << 24 ^ arrn[0][1];
        this.C2 = 255 & Si[n16 & 255] ^ (255 & Si[255 & n15 >> 8]) << 8 ^ (255 & Si[255 & n14 >> 16]) << 16 ^ Si[255 & n17 >> 24] << 24 ^ arrn[0][2];
        this.C3 = 255 & Si[n17 & 255] ^ (255 & Si[255 & n16 >> 8]) << 8 ^ (255 & Si[255 & n15 >> 16]) << 16 ^ Si[255 & n14 >> 24] << 24 ^ arrn[0][3];
    }

    private void encryptBlock(int[][] arrn) {
        int n2 = this.C0 ^ arrn[0][0];
        int n3 = this.C1 ^ arrn[0][1];
        int n4 = this.C2 ^ arrn[0][2];
        int n5 = 1;
        int n6 = this.C3 ^ arrn[0][3];
        while (n5 < -1 + this.ROUNDS) {
            int n7 = T0[n2 & 255] ^ AESEngine.shift(T0[255 & n3 >> 8], 24) ^ AESEngine.shift(T0[255 & n4 >> 16], 16) ^ AESEngine.shift(T0[255 & n6 >> 24], 8) ^ arrn[n5][0];
            int n8 = T0[n3 & 255] ^ AESEngine.shift(T0[255 & n4 >> 8], 24) ^ AESEngine.shift(T0[255 & n6 >> 16], 16) ^ AESEngine.shift(T0[255 & n2 >> 24], 8) ^ arrn[n5][1];
            int n9 = T0[n4 & 255] ^ AESEngine.shift(T0[255 & n6 >> 8], 24) ^ AESEngine.shift(T0[255 & n2 >> 16], 16) ^ AESEngine.shift(T0[255 & n3 >> 24], 8) ^ arrn[n5][2];
            int n10 = T0[n6 & 255] ^ AESEngine.shift(T0[255 & n2 >> 8], 24) ^ AESEngine.shift(T0[255 & n3 >> 16], 16) ^ AESEngine.shift(T0[255 & n4 >> 24], 8);
            int n11 = n5 + 1;
            int n12 = n10 ^ arrn[n5][3];
            n2 = T0[n7 & 255] ^ AESEngine.shift(T0[255 & n8 >> 8], 24) ^ AESEngine.shift(T0[255 & n9 >> 16], 16) ^ AESEngine.shift(T0[255 & n12 >> 24], 8) ^ arrn[n11][0];
            n3 = T0[n8 & 255] ^ AESEngine.shift(T0[255 & n9 >> 8], 24) ^ AESEngine.shift(T0[255 & n12 >> 16], 16) ^ AESEngine.shift(T0[255 & n7 >> 24], 8) ^ arrn[n11][1];
            n4 = T0[n9 & 255] ^ AESEngine.shift(T0[255 & n12 >> 8], 24) ^ AESEngine.shift(T0[255 & n7 >> 16], 16) ^ AESEngine.shift(T0[255 & n8 >> 24], 8) ^ arrn[n11][2];
            int n13 = T0[n12 & 255] ^ AESEngine.shift(T0[255 & n7 >> 8], 24) ^ AESEngine.shift(T0[255 & n8 >> 16], 16) ^ AESEngine.shift(T0[255 & n9 >> 24], 8);
            n5 = n11 + 1;
            n6 = n13 ^ arrn[n11][3];
        }
        int n14 = T0[n2 & 255] ^ AESEngine.shift(T0[255 & n3 >> 8], 24) ^ AESEngine.shift(T0[255 & n4 >> 16], 16) ^ AESEngine.shift(T0[255 & n6 >> 24], 8) ^ arrn[n5][0];
        int n15 = T0[n3 & 255] ^ AESEngine.shift(T0[255 & n4 >> 8], 24) ^ AESEngine.shift(T0[255 & n6 >> 16], 16) ^ AESEngine.shift(T0[255 & n2 >> 24], 8) ^ arrn[n5][1];
        int n16 = T0[n4 & 255] ^ AESEngine.shift(T0[255 & n6 >> 8], 24) ^ AESEngine.shift(T0[255 & n2 >> 16], 16) ^ AESEngine.shift(T0[255 & n3 >> 24], 8) ^ arrn[n5][2];
        int n17 = T0[n6 & 255] ^ AESEngine.shift(T0[255 & n2 >> 8], 24) ^ AESEngine.shift(T0[255 & n3 >> 16], 16) ^ AESEngine.shift(T0[255 & n4 >> 24], 8);
        int n18 = n5 + 1;
        int n19 = n17 ^ arrn[n5][3];
        this.C0 = 255 & S[n14 & 255] ^ (255 & S[255 & n15 >> 8]) << 8 ^ (255 & S[255 & n16 >> 16]) << 16 ^ S[255 & n19 >> 24] << 24 ^ arrn[n18][0];
        this.C1 = 255 & S[n15 & 255] ^ (255 & S[255 & n16 >> 8]) << 8 ^ (255 & S[255 & n19 >> 16]) << 16 ^ S[255 & n14 >> 24] << 24 ^ arrn[n18][1];
        this.C2 = 255 & S[n16 & 255] ^ (255 & S[255 & n19 >> 8]) << 8 ^ (255 & S[255 & n14 >> 16]) << 16 ^ S[255 & n15 >> 24] << 24 ^ arrn[n18][2];
        this.C3 = 255 & S[n19 & 255] ^ (255 & S[255 & n14 >> 8]) << 8 ^ (255 & S[255 & n15 >> 16]) << 16 ^ S[255 & n16 >> 24] << 24 ^ arrn[n18][3];
    }

    /*
     * Enabled aggressive block sorting
     */
    private int[][] generateWorkingKey(byte[] arrby, boolean bl) {
        int n2 = arrby.length / 4;
        if (n2 != 4 && n2 != 6 && n2 != 8 || n2 * 4 != arrby.length) {
            throw new IllegalArgumentException("Key length not 128/192/256 bits.");
        }
        this.ROUNDS = n2 + 6;
        int[] arrn = new int[]{1 + this.ROUNDS, 4};
        int[][] arrn2 = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn);
        int n3 = 0;
        for (int i2 = 0; i2 < arrby.length; i2 += 4, ++n3) {
            arrn2[n3 >> 2][n3 & 3] = 255 & arrby[i2] | (255 & arrby[i2 + 1]) << 8 | (255 & arrby[i2 + 2]) << 16 | arrby[i2 + 3] << 24;
        }
        int n4 = 1 + this.ROUNDS << 2;
        for (int i3 = n2; i3 < n4; ++i3) {
            int n5 = arrn2[i3 - 1 >> 2][3 & i3 - 1];
            if (i3 % n2 == 0) {
                n5 = AESEngine.subWord(AESEngine.shift(n5, 8)) ^ rcon[-1 + i3 / n2];
            } else if (n2 > 6 && i3 % n2 == 4) {
                n5 = AESEngine.subWord(n5);
            }
            arrn2[i3 >> 2][i3 & 3] = n5 ^ arrn2[i3 - n2 >> 2][3 & i3 - n2];
        }
        if (!bl) {
            for (int i4 = 1; i4 < this.ROUNDS; ++i4) {
                for (int i5 = 0; i5 < 4; ++i5) {
                    arrn2[i4][i5] = AESEngine.inv_mcol(arrn2[i4][i5]);
                }
            }
        }
        return arrn2;
    }

    private static int inv_mcol(int n2) {
        int n3 = AESEngine.FFmulX(n2);
        int n4 = AESEngine.FFmulX(n3);
        int n5 = AESEngine.FFmulX(n4);
        int n6 = n2 ^ n5;
        return n5 ^ (n3 ^ n4) ^ AESEngine.shift(n3 ^ n6, 8) ^ AESEngine.shift(n4 ^ n6, 16) ^ AESEngine.shift(n6, 24);
    }

    private void packBlock(byte[] arrby, int n2) {
        int n3 = n2 + 1;
        arrby[n2] = (byte)this.C0;
        int n4 = n3 + 1;
        arrby[n3] = (byte)(this.C0 >> 8);
        int n5 = n4 + 1;
        arrby[n4] = (byte)(this.C0 >> 16);
        int n6 = n5 + 1;
        arrby[n5] = (byte)(this.C0 >> 24);
        int n7 = n6 + 1;
        arrby[n6] = (byte)this.C1;
        int n8 = n7 + 1;
        arrby[n7] = (byte)(this.C1 >> 8);
        int n9 = n8 + 1;
        arrby[n8] = (byte)(this.C1 >> 16);
        int n10 = n9 + 1;
        arrby[n9] = (byte)(this.C1 >> 24);
        int n11 = n10 + 1;
        arrby[n10] = (byte)this.C2;
        int n12 = n11 + 1;
        arrby[n11] = (byte)(this.C2 >> 8);
        int n13 = n12 + 1;
        arrby[n12] = (byte)(this.C2 >> 16);
        int n14 = n13 + 1;
        arrby[n13] = (byte)(this.C2 >> 24);
        int n15 = n14 + 1;
        arrby[n14] = (byte)this.C3;
        int n16 = n15 + 1;
        arrby[n15] = (byte)(this.C3 >> 8);
        int n17 = n16 + 1;
        arrby[n16] = (byte)(this.C3 >> 16);
        n17 + 1;
        arrby[n17] = (byte)(this.C3 >> 24);
    }

    private static int shift(int n2, int n3) {
        return n2 >>> n3 | n2 << -n3;
    }

    private static int subWord(int n2) {
        return 255 & S[n2 & 255] | (255 & S[255 & n2 >> 8]) << 8 | (255 & S[255 & n2 >> 16]) << 16 | S[255 & n2 >> 24] << 24;
    }

    private void unpackBlock(byte[] arrby, int n2) {
        int n3 = n2 + 1;
        int n4 = this.C0 = 255 & arrby[n2];
        int n5 = n3 + 1;
        int n6 = this.C0 = n4 | (255 & arrby[n3]) << 8;
        int n7 = n5 + 1;
        int n8 = this.C0 = n6 | (255 & arrby[n5]) << 16;
        int n9 = n7 + 1;
        this.C0 = n8 | arrby[n7] << 24;
        int n10 = n9 + 1;
        int n11 = this.C1 = 255 & arrby[n9];
        int n12 = n10 + 1;
        int n13 = this.C1 = n11 | (255 & arrby[n10]) << 8;
        int n14 = n12 + 1;
        int n15 = this.C1 = n13 | (255 & arrby[n12]) << 16;
        int n16 = n14 + 1;
        this.C1 = n15 | arrby[n14] << 24;
        int n17 = n16 + 1;
        int n18 = this.C2 = 255 & arrby[n16];
        int n19 = n17 + 1;
        int n20 = this.C2 = n18 | (255 & arrby[n17]) << 8;
        int n21 = n19 + 1;
        int n22 = this.C2 = n20 | (255 & arrby[n19]) << 16;
        int n23 = n21 + 1;
        this.C2 = n22 | arrby[n21] << 24;
        int n24 = n23 + 1;
        int n25 = this.C3 = 255 & arrby[n23];
        int n26 = n24 + 1;
        int n27 = this.C3 = n25 | (255 & arrby[n24]) << 8;
        int n28 = n26 + 1;
        int n29 = this.C3 = n27 | (255 & arrby[n26]) << 16;
        n28 + 1;
        this.C3 = n29 | arrby[n28] << 24;
    }

    @Override
    public String getAlgorithmName() {
        return "AES";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.WorkingKey = this.generateWorkingKey(((KeyParameter)cipherParameters).getKey(), bl);
            this.forEncryption = bl;
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to AES init - " + cipherParameters.getClass().getName());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (this.WorkingKey == null) {
            throw new IllegalStateException("AES engine not initialised");
        }
        if (n2 + 16 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + 16 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.forEncryption) {
            this.unpackBlock(arrby, n2);
            this.encryptBlock(this.WorkingKey);
            this.packBlock(arrby2, n3);
            do {
                return 16;
                break;
            } while (true);
        }
        this.unpackBlock(arrby, n2);
        this.decryptBlock(this.WorkingKey);
        this.packBlock(arrby2, n3);
        return 16;
    }

    @Override
    public void reset() {
    }
}

