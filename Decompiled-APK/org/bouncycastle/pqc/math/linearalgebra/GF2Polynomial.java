package org.bouncycastle.pqc.math.linearalgebra;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.google.android.gms.location.places.Place;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.cncc.CNCCCommands;
import com.samsung.android.spaytui.SpayTuiTAController;
import java.math.BigInteger;
import java.util.Random;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class GF2Polynomial {
    private static final int[] bitMask;
    private static final boolean[] parity;
    private static Random rand;
    private static final int[] reverseRightMask;
    private static final short[] squaringTable;
    private int blocks;
    private int len;
    private int[] value;

    static {
        rand = new Random();
        parity = new boolean[]{false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false};
        squaringTable = new short[]{(short) 0, (short) 1, (short) 4, (short) 5, (short) 16, (short) 17, (short) 20, (short) 21, (short) 64, (short) 65, (short) 68, (short) 69, (short) 80, (short) 81, (short) 84, (short) 85, (short) 256, (short) 257, (short) 260, (short) 261, (short) 272, (short) 273, (short) 276, (short) 277, (short) 320, (short) 321, (short) 324, (short) 325, (short) 336, (short) 337, (short) 340, (short) 341, (short) 1024, (short) 1025, (short) 1028, (short) 1029, (short) 1040, (short) 1041, (short) 1044, (short) 1045, (short) 1088, (short) 1089, (short) 1092, (short) 1093, (short) 1104, (short) 1105, (short) 1108, (short) 1109, (short) 1280, (short) 1281, (short) 1284, (short) 1285, (short) 1296, (short) 1297, (short) 1300, (short) 1301, (short) 1344, (short) 1345, (short) 1348, (short) 1349, (short) 1360, (short) 1361, (short) 1364, (short) 1365, (short) 4096, (short) 4097, (short) 4100, (short) 4101, (short) 4112, (short) 4113, (short) 4116, (short) 4117, (short) 4160, (short) 4161, (short) 4164, (short) 4165, (short) 4176, (short) 4177, (short) 4180, (short) 4181, (short) 4352, (short) 4353, (short) 4356, (short) 4357, (short) 4368, (short) 4369, (short) 4372, (short) 4373, (short) 4416, (short) 4417, (short) 4420, (short) 4421, (short) 4432, (short) 4433, (short) 4436, (short) 4437, (short) 5120, (short) 5121, (short) 5124, (short) 5125, (short) 5136, (short) 5137, (short) 5140, (short) 5141, (short) 5184, (short) 5185, (short) 5188, (short) 5189, (short) 5200, (short) 5201, (short) 5204, (short) 5205, (short) 5376, (short) 5377, (short) 5380, (short) 5381, (short) 5392, (short) 5393, (short) 5396, (short) 5397, (short) 5440, (short) 5441, (short) 5444, (short) 5445, (short) 5456, (short) 5457, (short) 5460, (short) 5461, (short) 16384, (short) 16385, (short) 16388, (short) 16389, (short) 16400, (short) 16401, (short) 16404, (short) 16405, (short) 16448, (short) 16449, (short) 16452, (short) 16453, (short) 16464, (short) 16465, (short) 16468, (short) 16469, (short) 16640, (short) 16641, (short) 16644, (short) 16645, (short) 16656, (short) 16657, (short) 16660, (short) 16661, (short) 16704, (short) 16705, (short) 16708, (short) 16709, (short) 16720, (short) 16721, (short) 16724, (short) 16725, (short) 17408, (short) 17409, (short) 17412, (short) 17413, (short) 17424, (short) 17425, (short) 17428, (short) 17429, (short) 17472, (short) 17473, (short) 17476, (short) 17477, (short) 17488, (short) 17489, (short) 17492, (short) 17493, (short) 17664, (short) 17665, (short) 17668, (short) 17669, (short) 17680, (short) 17681, (short) 17684, (short) 17685, (short) 17728, (short) 17729, (short) 17732, (short) 17733, (short) 17744, (short) 17745, (short) 17748, (short) 17749, (short) 20480, (short) 20481, (short) 20484, (short) 20485, (short) 20496, (short) 20497, (short) 20500, (short) 20501, (short) 20544, (short) 20545, (short) 20548, (short) 20549, (short) 20560, (short) 20561, (short) 20564, (short) 20565, (short) 20736, (short) 20737, (short) 20740, (short) 20741, (short) 20752, (short) 20753, (short) 20756, (short) 20757, (short) 20800, (short) 20801, (short) 20804, (short) 20805, (short) 20816, (short) 20817, (short) 20820, (short) 20821, (short) 21504, (short) 21505, (short) 21508, (short) 21509, (short) 21520, (short) 21521, (short) 21524, (short) 21525, (short) 21568, (short) 21569, (short) 21572, (short) 21573, (short) 21584, (short) 21585, (short) 21588, (short) 21589, (short) 21760, (short) 21761, (short) 21764, (short) 21765, (short) 21776, (short) 21777, (short) 21780, (short) 21781, (short) 21824, (short) 21825, (short) 21828, (short) 21829, (short) 21840, (short) 21841, (short) 21844, (short) 21845};
        bitMask = new int[]{1, 2, 4, 8, 16, 32, 64, X509KeyUsage.digitalSignature, SkeinMac.SKEIN_256, SkeinMac.SKEIN_512, SkeinMac.SKEIN_1024, PKIFailureInfo.wrongIntegrity, PKIFailureInfo.certConfirmed, PKIFailureInfo.certRevoked, PKIFailureInfo.badPOP, X509KeyUsage.decipherOnly, PKIFailureInfo.notAuthorized, PKIFailureInfo.unsupportedVersion, PKIFailureInfo.transactionIdInUse, PKIFailureInfo.signerNotTrusted, PKIFailureInfo.badCertTemplate, PKIFailureInfo.badSenderNonce, PKIFailureInfo.addInfoNotAvailable, PKIFailureInfo.unacceptedExtension, ViewCompat.MEASURED_STATE_TOO_SMALL, 33554432, 67108864, 134217728, 268435456, PKIFailureInfo.duplicateCertReq, PKIFailureInfo.systemFailure, PKIFailureInfo.systemUnavail, 0};
        reverseRightMask = new int[]{0, 1, 3, 7, 15, 31, 63, CertificateBody.profileType, GF2Field.MASK, 511, Place.TYPE_SUBLOCALITY_LEVEL_1, 2047, 4095, SpayTuiTAController.SPAY_AUTH_FAIL, 16383, 32767, HCEClientConstants.HIGHEST_ATC_DEC_VALUE, 131071, 262143, 524287, 1048575, 2097151, 4194303, 8388607, ViewCompat.MEASURED_SIZE_MASK, 33554431, 67108863, 134217727, 268435455, 536870911, 1073741823, CNCCCommands.CMD_CNCC_CMD_UNKNOWN, -1};
    }

    public GF2Polynomial(int i) {
        if (i < 1) {
            i = 1;
        }
        this.blocks = ((i - 1) >> 5) + 1;
        this.value = new int[this.blocks];
        this.len = i;
    }

    public GF2Polynomial(int i, String str) {
        if (i < 1) {
            i = 1;
        }
        this.blocks = ((i - 1) >> 5) + 1;
        this.value = new int[this.blocks];
        this.len = i;
        if (str.equalsIgnoreCase("ZERO")) {
            assignZero();
        } else if (str.equalsIgnoreCase("ONE")) {
            assignOne();
        } else if (str.equalsIgnoreCase("RANDOM")) {
            randomize();
        } else if (str.equalsIgnoreCase("X")) {
            assignX();
        } else if (str.equalsIgnoreCase(EnrollCardInfo.CARD_PRESENTATION_MODE_ALL)) {
            assignAll();
        } else {
            throw new IllegalArgumentException("Error: GF2Polynomial was called using " + str + " as value!");
        }
    }

    public GF2Polynomial(int i, BigInteger bigInteger) {
        Object obj;
        int i2;
        if (i < 1) {
            i = 1;
        }
        this.blocks = ((i - 1) >> 5) + 1;
        this.value = new int[this.blocks];
        this.len = i;
        Object toByteArray = bigInteger.toByteArray();
        if (toByteArray[0] == null) {
            Object obj2 = new byte[(toByteArray.length - 1)];
            System.arraycopy(toByteArray, 1, obj2, 0, obj2.length);
            obj = obj2;
        } else {
            obj = toByteArray;
        }
        int length = obj.length & 3;
        int length2 = ((obj.length - 1) >> 2) + 1;
        for (i2 = 0; i2 < length; i2++) {
            int[] iArr = this.value;
            int i3 = length2 - 1;
            iArr[i3] = iArr[i3] | ((obj[i2] & GF2Field.MASK) << (((length - 1) - i2) << 3));
        }
        for (i2 = 0; i2 <= ((obj.length - 4) >> 2); i2++) {
            length = (obj.length - 1) - (i2 << 2);
            this.value[i2] = obj[length] & GF2Field.MASK;
            int[] iArr2 = this.value;
            iArr2[i2] = iArr2[i2] | ((obj[length - 1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK);
            iArr2 = this.value;
            iArr2[i2] = iArr2[i2] | ((obj[length - 2] << 16) & 16711680);
            iArr2 = this.value;
            iArr2[i2] = ((obj[length - 3] << 24) & ViewCompat.MEASURED_STATE_MASK) | iArr2[i2];
        }
        if ((this.len & 31) != 0) {
            int[] iArr3 = this.value;
            i2 = this.blocks - 1;
            iArr3[i2] = iArr3[i2] & reverseRightMask[this.len & 31];
        }
        reduceN();
    }

    public GF2Polynomial(int i, Random random) {
        if (i < 1) {
            i = 1;
        }
        this.blocks = ((i - 1) >> 5) + 1;
        this.value = new int[this.blocks];
        this.len = i;
        randomize(random);
    }

    public GF2Polynomial(int i, byte[] bArr) {
        int i2;
        if (i < 1) {
            i = 1;
        }
        this.blocks = ((i - 1) >> 5) + 1;
        this.value = new int[this.blocks];
        this.len = i;
        int min = Math.min(((bArr.length - 1) >> 2) + 1, this.blocks);
        for (i2 = 0; i2 < min - 1; i2++) {
            int length = (bArr.length - (i2 << 2)) - 1;
            this.value[i2] = bArr[length] & GF2Field.MASK;
            int[] iArr = this.value;
            iArr[i2] = iArr[i2] | ((bArr[length - 1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK);
            iArr = this.value;
            iArr[i2] = iArr[i2] | ((bArr[length - 2] << 16) & 16711680);
            iArr = this.value;
            iArr[i2] = ((bArr[length - 3] << 24) & ViewCompat.MEASURED_STATE_MASK) | iArr[i2];
        }
        i2 = min - 1;
        min = (bArr.length - (i2 << 2)) - 1;
        this.value[i2] = bArr[min] & GF2Field.MASK;
        if (min > 0) {
            int[] iArr2 = this.value;
            iArr2[i2] = iArr2[i2] | ((bArr[min - 1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK);
        }
        if (min > 1) {
            int[] iArr3 = this.value;
            iArr3[i2] = iArr3[i2] | ((bArr[min - 2] << 16) & 16711680);
        }
        if (min > 2) {
            iArr3 = this.value;
            iArr3[i2] = ((bArr[min - 3] << 24) & ViewCompat.MEASURED_STATE_MASK) | iArr3[i2];
        }
        zeroUnusedBits();
        reduceN();
    }

    public GF2Polynomial(int i, int[] iArr) {
        if (i < 1) {
            i = 1;
        }
        this.blocks = ((i - 1) >> 5) + 1;
        this.value = new int[this.blocks];
        this.len = i;
        System.arraycopy(iArr, 0, this.value, 0, Math.min(this.blocks, iArr.length));
        zeroUnusedBits();
    }

    public GF2Polynomial(GF2Polynomial gF2Polynomial) {
        this.len = gF2Polynomial.len;
        this.blocks = gF2Polynomial.blocks;
        this.value = IntUtils.clone(gF2Polynomial.value);
    }

    private void doShiftBlocksLeft(int i) {
        if (this.blocks <= this.value.length) {
            int i2;
            for (i2 = this.blocks - 1; i2 >= i; i2--) {
                this.value[i2] = this.value[i2 - i];
            }
            for (i2 = 0; i2 < i; i2++) {
                this.value[i2] = 0;
            }
            return;
        }
        Object obj = new int[this.blocks];
        System.arraycopy(this.value, 0, obj, i, this.blocks - i);
        this.value = null;
        this.value = obj;
    }

    private GF2Polynomial karaMult(GF2Polynomial gF2Polynomial) {
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this.len << 1);
        if (this.len <= 32) {
            gF2Polynomial2.value = mult32(this.value[0], gF2Polynomial.value[0]);
        } else if (this.len <= 64) {
            gF2Polynomial2.value = mult64(this.value, gF2Polynomial.value);
        } else if (this.len <= X509KeyUsage.digitalSignature) {
            gF2Polynomial2.value = mult128(this.value, gF2Polynomial.value);
        } else if (this.len <= SkeinMac.SKEIN_256) {
            gF2Polynomial2.value = mult256(this.value, gF2Polynomial.value);
        } else if (this.len <= SkeinMac.SKEIN_512) {
            gF2Polynomial2.value = mult512(this.value, gF2Polynomial.value);
        } else {
            int i = bitMask[IntegerFunctions.floorLog(this.len - 1)];
            GF2Polynomial lower = lower(((i - 1) >> 5) + 1);
            GF2Polynomial upper = upper(((i - 1) >> 5) + 1);
            GF2Polynomial lower2 = gF2Polynomial.lower(((i - 1) >> 5) + 1);
            GF2Polynomial upper2 = gF2Polynomial.upper(((i - 1) >> 5) + 1);
            GF2Polynomial karaMult = upper.karaMult(upper2);
            GF2Polynomial karaMult2 = lower.karaMult(lower2);
            lower.addToThis(upper);
            lower2.addToThis(upper2);
            lower = lower.karaMult(lower2);
            gF2Polynomial2.shiftLeftAddThis(karaMult, i << 1);
            gF2Polynomial2.shiftLeftAddThis(karaMult, i);
            gF2Polynomial2.shiftLeftAddThis(lower, i);
            gF2Polynomial2.shiftLeftAddThis(karaMult2, i);
            gF2Polynomial2.addToThis(karaMult2);
        }
        return gF2Polynomial2;
    }

    private GF2Polynomial lower(int i) {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(i << 5);
        System.arraycopy(this.value, 0, gF2Polynomial.value, 0, Math.min(i, this.blocks));
        return gF2Polynomial;
    }

    private static int[] mult128(int[] iArr, int[] iArr2) {
        int[] iArr3 = new int[8];
        Object obj = new int[2];
        System.arraycopy(iArr, 0, obj, 0, Math.min(2, iArr.length));
        Object obj2 = new int[2];
        if (iArr.length > 2) {
            System.arraycopy(iArr, 2, obj2, 0, Math.min(2, iArr.length - 2));
        }
        Object obj3 = new int[2];
        System.arraycopy(iArr2, 0, obj3, 0, Math.min(2, iArr2.length));
        Object obj4 = new int[2];
        if (iArr2.length > 2) {
            System.arraycopy(iArr2, 2, obj4, 0, Math.min(2, iArr2.length - 2));
        }
        int[] mult64;
        if (obj2[1] != null || obj4[1] != null) {
            mult64 = mult64(obj2, obj4);
            iArr3[7] = iArr3[7] ^ mult64[3];
            iArr3[6] = iArr3[6] ^ mult64[2];
            iArr3[5] = iArr3[5] ^ (mult64[1] ^ mult64[3]);
            iArr3[4] = iArr3[4] ^ (mult64[0] ^ mult64[2]);
            iArr3[3] = iArr3[3] ^ mult64[1];
            iArr3[2] = mult64[0] ^ iArr3[2];
        } else if (!(obj2[0] == null && obj4[0] == null)) {
            mult64 = mult32(obj2[0], obj4[0]);
            iArr3[5] = iArr3[5] ^ mult64[1];
            iArr3[4] = iArr3[4] ^ mult64[0];
            iArr3[3] = iArr3[3] ^ mult64[1];
            iArr3[2] = mult64[0] ^ iArr3[2];
        }
        obj2[0] = obj2[0] ^ obj[0];
        obj2[1] = obj2[1] ^ obj[1];
        obj4[0] = obj4[0] ^ obj3[0];
        obj4[1] = obj4[1] ^ obj3[1];
        int[] mult32;
        if (obj2[1] == null && obj4[1] == null) {
            mult32 = mult32(obj2[0], obj4[0]);
            iArr3[3] = iArr3[3] ^ mult32[1];
            iArr3[2] = mult32[0] ^ iArr3[2];
        } else {
            mult32 = mult64(obj2, obj4);
            iArr3[5] = iArr3[5] ^ mult32[3];
            iArr3[4] = iArr3[4] ^ mult32[2];
            iArr3[3] = iArr3[3] ^ mult32[1];
            iArr3[2] = mult32[0] ^ iArr3[2];
        }
        int[] mult322;
        if (obj[1] == null && obj3[1] == null) {
            mult322 = mult32(obj[0], obj3[0]);
            iArr3[3] = iArr3[3] ^ mult322[1];
            iArr3[2] = iArr3[2] ^ mult322[0];
            iArr3[1] = iArr3[1] ^ mult322[1];
            iArr3[0] = mult322[0] ^ iArr3[0];
        } else {
            mult322 = mult64(obj, obj3);
            iArr3[5] = iArr3[5] ^ mult322[3];
            iArr3[4] = iArr3[4] ^ mult322[2];
            iArr3[3] = iArr3[3] ^ (mult322[1] ^ mult322[3]);
            iArr3[2] = iArr3[2] ^ (mult322[0] ^ mult322[2]);
            iArr3[1] = iArr3[1] ^ mult322[1];
            iArr3[0] = mult322[0] ^ iArr3[0];
        }
        return iArr3;
    }

    private static int[] mult256(int[] iArr, int[] iArr2) {
        int[] iArr3 = new int[16];
        Object obj = new int[4];
        System.arraycopy(iArr, 0, obj, 0, Math.min(4, iArr.length));
        Object obj2 = new int[4];
        if (iArr.length > 4) {
            System.arraycopy(iArr, 4, obj2, 0, Math.min(4, iArr.length - 4));
        }
        Object obj3 = new int[4];
        System.arraycopy(iArr2, 0, obj3, 0, Math.min(4, iArr2.length));
        Object obj4 = new int[4];
        if (iArr2.length > 4) {
            System.arraycopy(iArr2, 4, obj4, 0, Math.min(4, iArr2.length - 4));
        }
        int[] mult128;
        if (obj2[3] != null || obj2[2] != null || obj4[3] != null || obj4[2] != null) {
            mult128 = mult128(obj2, obj4);
            iArr3[15] = iArr3[15] ^ mult128[7];
            iArr3[14] = iArr3[14] ^ mult128[6];
            iArr3[13] = iArr3[13] ^ mult128[5];
            iArr3[12] = iArr3[12] ^ mult128[4];
            iArr3[11] = iArr3[11] ^ (mult128[3] ^ mult128[7]);
            iArr3[10] = iArr3[10] ^ (mult128[2] ^ mult128[6]);
            iArr3[9] = iArr3[9] ^ (mult128[1] ^ mult128[5]);
            iArr3[8] = iArr3[8] ^ (mult128[0] ^ mult128[4]);
            iArr3[7] = iArr3[7] ^ mult128[3];
            iArr3[6] = iArr3[6] ^ mult128[2];
            iArr3[5] = iArr3[5] ^ mult128[1];
            iArr3[4] = mult128[0] ^ iArr3[4];
        } else if (obj2[1] != null || obj4[1] != null) {
            mult128 = mult64(obj2, obj4);
            iArr3[11] = iArr3[11] ^ mult128[3];
            iArr3[10] = iArr3[10] ^ mult128[2];
            iArr3[9] = iArr3[9] ^ mult128[1];
            iArr3[8] = iArr3[8] ^ mult128[0];
            iArr3[7] = iArr3[7] ^ mult128[3];
            iArr3[6] = iArr3[6] ^ mult128[2];
            iArr3[5] = iArr3[5] ^ mult128[1];
            iArr3[4] = mult128[0] ^ iArr3[4];
        } else if (!(obj2[0] == null && obj4[0] == null)) {
            mult128 = mult32(obj2[0], obj4[0]);
            iArr3[9] = iArr3[9] ^ mult128[1];
            iArr3[8] = iArr3[8] ^ mult128[0];
            iArr3[5] = iArr3[5] ^ mult128[1];
            iArr3[4] = mult128[0] ^ iArr3[4];
        }
        obj2[0] = obj2[0] ^ obj[0];
        obj2[1] = obj2[1] ^ obj[1];
        obj2[2] = obj2[2] ^ obj[2];
        obj2[3] = obj2[3] ^ obj[3];
        obj4[0] = obj4[0] ^ obj3[0];
        obj4[1] = obj4[1] ^ obj3[1];
        obj4[2] = obj4[2] ^ obj3[2];
        obj4[3] = obj4[3] ^ obj3[3];
        int[] mult1282 = mult128(obj2, obj4);
        iArr3[11] = iArr3[11] ^ mult1282[7];
        iArr3[10] = iArr3[10] ^ mult1282[6];
        iArr3[9] = iArr3[9] ^ mult1282[5];
        iArr3[8] = iArr3[8] ^ mult1282[4];
        iArr3[7] = iArr3[7] ^ mult1282[3];
        iArr3[6] = iArr3[6] ^ mult1282[2];
        iArr3[5] = iArr3[5] ^ mult1282[1];
        iArr3[4] = mult1282[0] ^ iArr3[4];
        int[] mult1283 = mult128(obj, obj3);
        iArr3[11] = iArr3[11] ^ mult1283[7];
        iArr3[10] = iArr3[10] ^ mult1283[6];
        iArr3[9] = iArr3[9] ^ mult1283[5];
        iArr3[8] = iArr3[8] ^ mult1283[4];
        iArr3[7] = iArr3[7] ^ (mult1283[3] ^ mult1283[7]);
        iArr3[6] = iArr3[6] ^ (mult1283[2] ^ mult1283[6]);
        iArr3[5] = iArr3[5] ^ (mult1283[1] ^ mult1283[5]);
        iArr3[4] = iArr3[4] ^ (mult1283[0] ^ mult1283[4]);
        iArr3[3] = iArr3[3] ^ mult1283[3];
        iArr3[2] = iArr3[2] ^ mult1283[2];
        iArr3[1] = iArr3[1] ^ mult1283[1];
        iArr3[0] = mult1283[0] ^ iArr3[0];
        return iArr3;
    }

    private static int[] mult32(int i, int i2) {
        int[] iArr = new int[2];
        if (i == 0 || i2 == 0) {
            return iArr;
        }
        long j = ((long) i2) & 4294967295L;
        long j2 = 0;
        for (int i3 = 1; i3 <= 32; i3++) {
            if ((bitMask[i3 - 1] & i) != 0) {
                j2 ^= j;
            }
            j <<= 1;
        }
        iArr[1] = (int) (j2 >>> 32);
        iArr[0] = (int) (j2 & 4294967295L);
        return iArr;
    }

    private static int[] mult512(int[] iArr, int[] iArr2) {
        int[] iArr3 = new int[32];
        Object obj = new int[8];
        System.arraycopy(iArr, 0, obj, 0, Math.min(8, iArr.length));
        Object obj2 = new int[8];
        if (iArr.length > 8) {
            System.arraycopy(iArr, 8, obj2, 0, Math.min(8, iArr.length - 8));
        }
        Object obj3 = new int[8];
        System.arraycopy(iArr2, 0, obj3, 0, Math.min(8, iArr2.length));
        Object obj4 = new int[8];
        if (iArr2.length > 8) {
            System.arraycopy(iArr2, 8, obj4, 0, Math.min(8, iArr2.length - 8));
        }
        int[] mult256 = mult256(obj2, obj4);
        iArr3[31] = iArr3[31] ^ mult256[15];
        iArr3[30] = iArr3[30] ^ mult256[14];
        iArr3[29] = iArr3[29] ^ mult256[13];
        iArr3[28] = iArr3[28] ^ mult256[12];
        iArr3[27] = iArr3[27] ^ mult256[11];
        iArr3[26] = iArr3[26] ^ mult256[10];
        iArr3[25] = iArr3[25] ^ mult256[9];
        iArr3[24] = iArr3[24] ^ mult256[8];
        iArr3[23] = iArr3[23] ^ (mult256[7] ^ mult256[15]);
        iArr3[22] = iArr3[22] ^ (mult256[6] ^ mult256[14]);
        iArr3[21] = iArr3[21] ^ (mult256[5] ^ mult256[13]);
        iArr3[20] = iArr3[20] ^ (mult256[4] ^ mult256[12]);
        iArr3[19] = iArr3[19] ^ (mult256[3] ^ mult256[11]);
        iArr3[18] = iArr3[18] ^ (mult256[2] ^ mult256[10]);
        iArr3[17] = iArr3[17] ^ (mult256[1] ^ mult256[9]);
        iArr3[16] = iArr3[16] ^ (mult256[0] ^ mult256[8]);
        iArr3[15] = iArr3[15] ^ mult256[7];
        iArr3[14] = iArr3[14] ^ mult256[6];
        iArr3[13] = iArr3[13] ^ mult256[5];
        iArr3[12] = iArr3[12] ^ mult256[4];
        iArr3[11] = iArr3[11] ^ mult256[3];
        iArr3[10] = iArr3[10] ^ mult256[2];
        iArr3[9] = iArr3[9] ^ mult256[1];
        iArr3[8] = mult256[0] ^ iArr3[8];
        obj2[0] = obj2[0] ^ obj[0];
        obj2[1] = obj2[1] ^ obj[1];
        obj2[2] = obj2[2] ^ obj[2];
        obj2[3] = obj2[3] ^ obj[3];
        obj2[4] = obj2[4] ^ obj[4];
        obj2[5] = obj2[5] ^ obj[5];
        obj2[6] = obj2[6] ^ obj[6];
        obj2[7] = obj2[7] ^ obj[7];
        obj4[0] = obj4[0] ^ obj3[0];
        obj4[1] = obj4[1] ^ obj3[1];
        obj4[2] = obj4[2] ^ obj3[2];
        obj4[3] = obj4[3] ^ obj3[3];
        obj4[4] = obj4[4] ^ obj3[4];
        obj4[5] = obj4[5] ^ obj3[5];
        obj4[6] = obj4[6] ^ obj3[6];
        obj4[7] = obj4[7] ^ obj3[7];
        int[] mult2562 = mult256(obj2, obj4);
        iArr3[23] = iArr3[23] ^ mult2562[15];
        iArr3[22] = iArr3[22] ^ mult2562[14];
        iArr3[21] = iArr3[21] ^ mult2562[13];
        iArr3[20] = iArr3[20] ^ mult2562[12];
        iArr3[19] = iArr3[19] ^ mult2562[11];
        iArr3[18] = iArr3[18] ^ mult2562[10];
        iArr3[17] = iArr3[17] ^ mult2562[9];
        iArr3[16] = iArr3[16] ^ mult2562[8];
        iArr3[15] = iArr3[15] ^ mult2562[7];
        iArr3[14] = iArr3[14] ^ mult2562[6];
        iArr3[13] = iArr3[13] ^ mult2562[5];
        iArr3[12] = iArr3[12] ^ mult2562[4];
        iArr3[11] = iArr3[11] ^ mult2562[3];
        iArr3[10] = iArr3[10] ^ mult2562[2];
        iArr3[9] = iArr3[9] ^ mult2562[1];
        iArr3[8] = mult2562[0] ^ iArr3[8];
        int[] mult2563 = mult256(obj, obj3);
        iArr3[23] = iArr3[23] ^ mult2563[15];
        iArr3[22] = iArr3[22] ^ mult2563[14];
        iArr3[21] = iArr3[21] ^ mult2563[13];
        iArr3[20] = iArr3[20] ^ mult2563[12];
        iArr3[19] = iArr3[19] ^ mult2563[11];
        iArr3[18] = iArr3[18] ^ mult2563[10];
        iArr3[17] = iArr3[17] ^ mult2563[9];
        iArr3[16] = iArr3[16] ^ mult2563[8];
        iArr3[15] = iArr3[15] ^ (mult2563[7] ^ mult2563[15]);
        iArr3[14] = iArr3[14] ^ (mult2563[6] ^ mult2563[14]);
        iArr3[13] = iArr3[13] ^ (mult2563[5] ^ mult2563[13]);
        iArr3[12] = iArr3[12] ^ (mult2563[4] ^ mult2563[12]);
        iArr3[11] = iArr3[11] ^ (mult2563[3] ^ mult2563[11]);
        iArr3[10] = iArr3[10] ^ (mult2563[2] ^ mult2563[10]);
        iArr3[9] = iArr3[9] ^ (mult2563[1] ^ mult2563[9]);
        iArr3[8] = iArr3[8] ^ (mult2563[0] ^ mult2563[8]);
        iArr3[7] = iArr3[7] ^ mult2563[7];
        iArr3[6] = iArr3[6] ^ mult2563[6];
        iArr3[5] = iArr3[5] ^ mult2563[5];
        iArr3[4] = iArr3[4] ^ mult2563[4];
        iArr3[3] = iArr3[3] ^ mult2563[3];
        iArr3[2] = iArr3[2] ^ mult2563[2];
        iArr3[1] = iArr3[1] ^ mult2563[1];
        iArr3[0] = mult2563[0] ^ iArr3[0];
        return iArr3;
    }

    private static int[] mult64(int[] iArr, int[] iArr2) {
        int[] iArr3 = new int[4];
        int i = iArr[0];
        int i2 = iArr.length > 1 ? iArr[1] : 0;
        int i3 = iArr2[0];
        int i4 = iArr2.length > 1 ? iArr2[1] : 0;
        if (!(i2 == 0 && i4 == 0)) {
            int[] mult32 = mult32(i2, i4);
            iArr3[3] = iArr3[3] ^ mult32[1];
            iArr3[2] = iArr3[2] ^ (mult32[0] ^ mult32[1]);
            iArr3[1] = mult32[0] ^ iArr3[1];
        }
        int[] mult322 = mult32(i2 ^ i, i4 ^ i3);
        iArr3[2] = iArr3[2] ^ mult322[1];
        iArr3[1] = mult322[0] ^ iArr3[1];
        mult322 = mult32(i, i3);
        iArr3[2] = iArr3[2] ^ mult322[1];
        iArr3[1] = iArr3[1] ^ (mult322[0] ^ mult322[1]);
        iArr3[0] = mult322[0] ^ iArr3[0];
        return iArr3;
    }

    private GF2Polynomial upper(int i) {
        int min = Math.min(i, this.blocks - i);
        GF2Polynomial gF2Polynomial = new GF2Polynomial(min << 5);
        if (this.blocks >= i) {
            System.arraycopy(this.value, i, gF2Polynomial.value, 0, min);
        }
        return gF2Polynomial;
    }

    private void zeroUnusedBits() {
        if ((this.len & 31) != 0) {
            int[] iArr = this.value;
            int i = this.blocks - 1;
            iArr[i] = iArr[i] & reverseRightMask[this.len & 31];
        }
    }

    public GF2Polynomial add(GF2Polynomial gF2Polynomial) {
        return xor(gF2Polynomial);
    }

    public void addToThis(GF2Polynomial gF2Polynomial) {
        expandN(gF2Polynomial.len);
        xorThisBy(gF2Polynomial);
    }

    public void assignAll() {
        for (int i = 0; i < this.blocks; i++) {
            this.value[i] = -1;
        }
        zeroUnusedBits();
    }

    public void assignOne() {
        for (int i = 1; i < this.blocks; i++) {
            this.value[i] = 0;
        }
        this.value[0] = 1;
    }

    public void assignX() {
        for (int i = 1; i < this.blocks; i++) {
            this.value[i] = 0;
        }
        this.value[0] = 2;
    }

    public void assignZero() {
        for (int i = 0; i < this.blocks; i++) {
            this.value[i] = 0;
        }
    }

    public Object clone() {
        return new GF2Polynomial(this);
    }

    public GF2Polynomial[] divide(GF2Polynomial gF2Polynomial) {
        GF2Polynomial[] gF2PolynomialArr = new GF2Polynomial[2];
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this.len);
        GF2Polynomial gF2Polynomial3 = new GF2Polynomial(this);
        GF2Polynomial gF2Polynomial4 = new GF2Polynomial(gF2Polynomial);
        if (gF2Polynomial4.isZero()) {
            throw new RuntimeException();
        }
        gF2Polynomial3.reduceN();
        gF2Polynomial4.reduceN();
        if (gF2Polynomial3.len < gF2Polynomial4.len) {
            gF2PolynomialArr[0] = new GF2Polynomial(0);
            gF2PolynomialArr[1] = gF2Polynomial3;
        } else {
            int i = gF2Polynomial3.len - gF2Polynomial4.len;
            gF2Polynomial2.expandN(i + 1);
            while (i >= 0) {
                gF2Polynomial3.subtractFromThis(gF2Polynomial4.shiftLeft(i));
                gF2Polynomial3.reduceN();
                gF2Polynomial2.xorBit(i);
                i = gF2Polynomial3.len - gF2Polynomial4.len;
            }
            gF2PolynomialArr[0] = gF2Polynomial2;
            gF2PolynomialArr[1] = gF2Polynomial3;
        }
        return gF2PolynomialArr;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof GF2Polynomial)) {
            return false;
        }
        GF2Polynomial gF2Polynomial = (GF2Polynomial) obj;
        if (this.len != gF2Polynomial.len) {
            return false;
        }
        for (int i = 0; i < this.blocks; i++) {
            if (this.value[i] != gF2Polynomial.value[i]) {
                return false;
            }
        }
        return true;
    }

    public void expandN(int i) {
        if (this.len < i) {
            this.len = i;
            int i2 = ((i - 1) >>> 5) + 1;
            if (this.blocks >= i2) {
                return;
            }
            if (this.value.length >= i2) {
                for (int i3 = this.blocks; i3 < i2; i3++) {
                    this.value[i3] = 0;
                }
                this.blocks = i2;
                return;
            }
            Object obj = new int[i2];
            System.arraycopy(this.value, 0, obj, 0, this.blocks);
            this.blocks = i2;
            this.value = null;
            this.value = obj;
        }
    }

    public GF2Polynomial gcd(GF2Polynomial gF2Polynomial) {
        if (isZero() && gF2Polynomial.isZero()) {
            throw new ArithmeticException("Both operands of gcd equal zero.");
        } else if (isZero()) {
            return new GF2Polynomial(gF2Polynomial);
        } else {
            if (gF2Polynomial.isZero()) {
                return new GF2Polynomial(this);
            }
            GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this);
            GF2Polynomial gF2Polynomial3 = new GF2Polynomial(gF2Polynomial);
            while (!gF2Polynomial3.isZero()) {
                GF2Polynomial remainder = gF2Polynomial2.remainder(gF2Polynomial3);
                gF2Polynomial2 = gF2Polynomial3;
                gF2Polynomial3 = remainder;
            }
            return gF2Polynomial2;
        }
    }

    public int getBit(int i) {
        if (i >= 0) {
            return (i <= this.len + -1 && (this.value[i >>> 5] & bitMask[i & 31]) != 0) ? 1 : 0;
        } else {
            throw new RuntimeException();
        }
    }

    public int getLength() {
        return this.len;
    }

    public int hashCode() {
        return this.len + this.value.hashCode();
    }

    public GF2Polynomial increase() {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this);
        gF2Polynomial.increaseThis();
        return gF2Polynomial;
    }

    public void increaseThis() {
        xorBit(0);
    }

    public boolean isIrreducible() {
        if (isZero()) {
            return false;
        }
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this);
        gF2Polynomial.reduceN();
        int i = gF2Polynomial.len - 1;
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(gF2Polynomial.len, "X");
        for (int i2 = 1; i2 <= (i >> 1); i2++) {
            gF2Polynomial2.squareThisPreCalc();
            gF2Polynomial2 = gF2Polynomial2.remainder(gF2Polynomial);
            GF2Polynomial add = gF2Polynomial2.add(new GF2Polynomial(32, "X"));
            if (add.isZero() || !gF2Polynomial.gcd(add).isOne()) {
                return false;
            }
        }
        return true;
    }

    public boolean isOne() {
        for (int i = 1; i < this.blocks; i++) {
            if (this.value[i] != 0) {
                return false;
            }
        }
        return this.value[0] == 1;
    }

    public boolean isZero() {
        if (this.len == 0) {
            return true;
        }
        for (int i = 0; i < this.blocks; i++) {
            if (this.value[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public GF2Polynomial multiply(GF2Polynomial gF2Polynomial) {
        int max = Math.max(this.len, gF2Polynomial.len);
        expandN(max);
        gF2Polynomial.expandN(max);
        return karaMult(gF2Polynomial);
    }

    public GF2Polynomial multiplyClassic(GF2Polynomial gF2Polynomial) {
        int i;
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(Math.max(this.len, gF2Polynomial.len) << 1);
        GF2Polynomial[] gF2PolynomialArr = new GF2Polynomial[32];
        gF2PolynomialArr[0] = new GF2Polynomial(this);
        for (i = 1; i <= 31; i++) {
            gF2PolynomialArr[i] = gF2PolynomialArr[i - 1].shiftLeft();
        }
        for (i = 0; i < gF2Polynomial.blocks; i++) {
            int i2;
            for (i2 = 0; i2 <= 31; i2++) {
                if ((gF2Polynomial.value[i] & bitMask[i2]) != 0) {
                    gF2Polynomial2.xorThisBy(gF2PolynomialArr[i2]);
                }
            }
            for (i2 = 0; i2 <= 31; i2++) {
                gF2PolynomialArr[i2].shiftBlocksLeft();
            }
        }
        return gF2Polynomial2;
    }

    public GF2Polynomial quotient(GF2Polynomial gF2Polynomial) {
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this.len);
        GF2Polynomial gF2Polynomial3 = new GF2Polynomial(this);
        GF2Polynomial gF2Polynomial4 = new GF2Polynomial(gF2Polynomial);
        if (gF2Polynomial4.isZero()) {
            throw new RuntimeException();
        }
        gF2Polynomial3.reduceN();
        gF2Polynomial4.reduceN();
        if (gF2Polynomial3.len < gF2Polynomial4.len) {
            return new GF2Polynomial(0);
        }
        int i = gF2Polynomial3.len - gF2Polynomial4.len;
        gF2Polynomial2.expandN(i + 1);
        while (i >= 0) {
            gF2Polynomial3.subtractFromThis(gF2Polynomial4.shiftLeft(i));
            gF2Polynomial3.reduceN();
            gF2Polynomial2.xorBit(i);
            i = gF2Polynomial3.len - gF2Polynomial4.len;
        }
        return gF2Polynomial2;
    }

    public void randomize() {
        for (int i = 0; i < this.blocks; i++) {
            this.value[i] = rand.nextInt();
        }
        zeroUnusedBits();
    }

    public void randomize(Random random) {
        for (int i = 0; i < this.blocks; i++) {
            this.value[i] = random.nextInt();
        }
        zeroUnusedBits();
    }

    public void reduceN() {
        int i = this.blocks - 1;
        while (this.value[i] == 0 && i > 0) {
            i--;
        }
        int i2 = this.value[i];
        int i3 = 0;
        while (i2 != 0) {
            i2 >>>= 1;
            i3++;
        }
        this.len = (i << 5) + i3;
        this.blocks = i + 1;
    }

    void reducePentanomial(int i, int[] iArr) {
        long j;
        int i2 = i >>> 5;
        int i3 = 32 - (i & 31);
        int i4 = (i - iArr[0]) >>> 5;
        int i5 = 32 - ((i - iArr[0]) & 31);
        int i6 = (i - iArr[1]) >>> 5;
        int i7 = 32 - ((i - iArr[1]) & 31);
        int i8 = (i - iArr[2]) >>> 5;
        int i9 = 32 - ((i - iArr[2]) & 31);
        for (int i10 = ((i << 1) - 2) >>> 5; i10 > i2; i10--) {
            j = ((long) this.value[i10]) & 4294967295L;
            int[] iArr2 = this.value;
            int i11 = (i10 - i2) - 1;
            iArr2[i11] = iArr2[i11] ^ ((int) (j << i3));
            iArr2 = this.value;
            i11 = i10 - i2;
            iArr2[i11] = (int) (((long) iArr2[i11]) ^ (j >>> (32 - i3)));
            iArr2 = this.value;
            i11 = (i10 - i4) - 1;
            iArr2[i11] = iArr2[i11] ^ ((int) (j << i5));
            iArr2 = this.value;
            i11 = i10 - i4;
            iArr2[i11] = (int) (((long) iArr2[i11]) ^ (j >>> (32 - i5)));
            iArr2 = this.value;
            i11 = (i10 - i6) - 1;
            iArr2[i11] = iArr2[i11] ^ ((int) (j << i7));
            iArr2 = this.value;
            i11 = i10 - i6;
            iArr2[i11] = (int) (((long) iArr2[i11]) ^ (j >>> (32 - i7)));
            iArr2 = this.value;
            i11 = (i10 - i8) - 1;
            iArr2[i11] = iArr2[i11] ^ ((int) (j << i9));
            iArr2 = this.value;
            i11 = i10 - i8;
            j >>>= 32 - i9;
            iArr2[i11] = (int) (j ^ ((long) iArr2[i11]));
            this.value[i10] = 0;
        }
        j = (((long) this.value[i2]) & 4294967295L) & (4294967295L << (i & 31));
        int[] iArr3 = this.value;
        iArr3[0] = (int) (((long) iArr3[0]) ^ (j >>> (32 - i3)));
        if ((i2 - i4) - 1 >= 0) {
            iArr3 = this.value;
            i3 = (i2 - i4) - 1;
            iArr3[i3] = iArr3[i3] ^ ((int) (j << i5));
        }
        iArr3 = this.value;
        i3 = i2 - i4;
        iArr3[i3] = (int) (((long) iArr3[i3]) ^ (j >>> (32 - i5)));
        if ((i2 - i6) - 1 >= 0) {
            iArr3 = this.value;
            i3 = (i2 - i6) - 1;
            iArr3[i3] = iArr3[i3] ^ ((int) (j << i7));
        }
        iArr3 = this.value;
        i3 = i2 - i6;
        iArr3[i3] = (int) (((long) iArr3[i3]) ^ (j >>> (32 - i7)));
        if ((i2 - i8) - 1 >= 0) {
            iArr3 = this.value;
            i3 = (i2 - i8) - 1;
            iArr3[i3] = iArr3[i3] ^ ((int) (j << i9));
        }
        iArr3 = this.value;
        i3 = i2 - i8;
        iArr3[i3] = (int) (((long) iArr3[i3]) ^ (j >>> (32 - i9)));
        iArr3 = this.value;
        iArr3[i2] = iArr3[i2] & reverseRightMask[i & 31];
        this.blocks = ((i - 1) >>> 5) + 1;
        this.len = i;
    }

    void reduceTrinomial(int i, int i2) {
        long j;
        int i3 = i >>> 5;
        int i4 = 32 - (i & 31);
        int i5 = (i - i2) >>> 5;
        int i6 = 32 - ((i - i2) & 31);
        for (int i7 = ((i << 1) - 2) >>> 5; i7 > i3; i7--) {
            j = ((long) this.value[i7]) & 4294967295L;
            int[] iArr = this.value;
            int i8 = (i7 - i3) - 1;
            iArr[i8] = iArr[i8] ^ ((int) (j << i4));
            iArr = this.value;
            i8 = i7 - i3;
            iArr[i8] = (int) (((long) iArr[i8]) ^ (j >>> (32 - i4)));
            iArr = this.value;
            i8 = (i7 - i5) - 1;
            iArr[i8] = iArr[i8] ^ ((int) (j << i6));
            iArr = this.value;
            i8 = i7 - i5;
            iArr[i8] = (int) ((j >>> (32 - i6)) ^ ((long) iArr[i8]));
            this.value[i7] = 0;
        }
        j = (((long) this.value[i3]) & 4294967295L) & (4294967295L << (i & 31));
        int[] iArr2 = this.value;
        iArr2[0] = (int) (((long) iArr2[0]) ^ (j >>> (32 - i4)));
        if ((i3 - i5) - 1 >= 0) {
            iArr2 = this.value;
            i4 = (i3 - i5) - 1;
            iArr2[i4] = iArr2[i4] ^ ((int) (j << i6));
        }
        iArr2 = this.value;
        i4 = i3 - i5;
        iArr2[i4] = (int) ((j >>> (32 - i6)) ^ ((long) iArr2[i4]));
        iArr2 = this.value;
        iArr2[i3] = iArr2[i3] & reverseRightMask[i & 31];
        this.blocks = ((i - 1) >>> 5) + 1;
        this.len = i;
    }

    public GF2Polynomial remainder(GF2Polynomial gF2Polynomial) {
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this);
        GF2Polynomial gF2Polynomial3 = new GF2Polynomial(gF2Polynomial);
        if (gF2Polynomial3.isZero()) {
            throw new RuntimeException();
        }
        gF2Polynomial2.reduceN();
        gF2Polynomial3.reduceN();
        if (gF2Polynomial2.len >= gF2Polynomial3.len) {
            for (int i = gF2Polynomial2.len - gF2Polynomial3.len; i >= 0; i = gF2Polynomial2.len - gF2Polynomial3.len) {
                gF2Polynomial2.subtractFromThis(gF2Polynomial3.shiftLeft(i));
                gF2Polynomial2.reduceN();
            }
        }
        return gF2Polynomial2;
    }

    public void resetBit(int i) {
        if (i < 0) {
            throw new RuntimeException();
        } else if (i <= this.len - 1) {
            int[] iArr = this.value;
            int i2 = i >>> 5;
            iArr[i2] = iArr[i2] & (bitMask[i & 31] ^ -1);
        }
    }

    public void setBit(int i) {
        if (i < 0 || i > this.len - 1) {
            throw new RuntimeException();
        }
        int[] iArr = this.value;
        int i2 = i >>> 5;
        iArr[i2] = iArr[i2] | bitMask[i & 31];
    }

    void shiftBlocksLeft() {
        this.blocks++;
        this.len += 32;
        if (this.blocks <= this.value.length) {
            for (int i = this.blocks - 1; i >= 1; i--) {
                this.value[i] = this.value[i - 1];
            }
            this.value[0] = 0;
            return;
        }
        Object obj = new int[this.blocks];
        System.arraycopy(this.value, 0, obj, 1, this.blocks - 1);
        this.value = null;
        this.value = obj;
    }

    public GF2Polynomial shiftLeft() {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this.len + 1, this.value);
        for (int i = gF2Polynomial.blocks - 1; i >= 1; i--) {
            int[] iArr = gF2Polynomial.value;
            iArr[i] = iArr[i] << 1;
            iArr = gF2Polynomial.value;
            iArr[i] = iArr[i] | (gF2Polynomial.value[i - 1] >>> 31);
        }
        int[] iArr2 = gF2Polynomial.value;
        iArr2[0] = iArr2[0] << 1;
        return gF2Polynomial;
    }

    public GF2Polynomial shiftLeft(int i) {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this.len + i, this.value);
        if (i >= 32) {
            gF2Polynomial.doShiftBlocksLeft(i >>> 5);
        }
        int i2 = i & 31;
        if (i2 != 0) {
            for (int i3 = gF2Polynomial.blocks - 1; i3 >= 1; i3--) {
                int[] iArr = gF2Polynomial.value;
                iArr[i3] = iArr[i3] << i2;
                iArr = gF2Polynomial.value;
                iArr[i3] = iArr[i3] | (gF2Polynomial.value[i3 - 1] >>> (32 - i2));
            }
            int[] iArr2 = gF2Polynomial.value;
            iArr2[0] = iArr2[0] << i2;
        }
        return gF2Polynomial;
    }

    public void shiftLeftAddThis(GF2Polynomial gF2Polynomial, int i) {
        if (i == 0) {
            addToThis(gF2Polynomial);
            return;
        }
        expandN(gF2Polynomial.len + i);
        int i2 = i >>> 5;
        for (int i3 = gF2Polynomial.blocks - 1; i3 >= 0; i3--) {
            int[] iArr;
            int i4;
            if ((i3 + i2) + 1 < this.blocks && (i & 31) != 0) {
                iArr = this.value;
                i4 = (i3 + i2) + 1;
                iArr[i4] = iArr[i4] ^ (gF2Polynomial.value[i3] >>> (32 - (i & 31)));
            }
            iArr = this.value;
            i4 = i3 + i2;
            iArr[i4] = iArr[i4] ^ (gF2Polynomial.value[i3] << (i & 31));
        }
    }

    public void shiftLeftThis() {
        int i;
        if ((this.len & 31) == 0) {
            this.len++;
            this.blocks++;
            if (this.blocks > this.value.length) {
                Object obj = new int[this.blocks];
                System.arraycopy(this.value, 0, obj, 0, this.value.length);
                this.value = null;
                this.value = obj;
            }
            for (i = this.blocks - 1; i >= 1; i--) {
                int[] iArr = this.value;
                iArr[i] = iArr[i] | (this.value[i - 1] >>> 31);
                iArr = this.value;
                int i2 = i - 1;
                iArr[i2] = iArr[i2] << 1;
            }
            return;
        }
        this.len++;
        for (i = this.blocks - 1; i >= 1; i--) {
            iArr = this.value;
            iArr[i] = iArr[i] << 1;
            iArr = this.value;
            iArr[i] = iArr[i] | (this.value[i - 1] >>> 31);
        }
        int[] iArr2 = this.value;
        iArr2[0] = iArr2[0] << 1;
    }

    public GF2Polynomial shiftRight() {
        int i = 0;
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this.len - 1);
        System.arraycopy(this.value, 0, gF2Polynomial.value, 0, gF2Polynomial.blocks);
        while (i <= gF2Polynomial.blocks - 2) {
            int[] iArr = gF2Polynomial.value;
            iArr[i] = iArr[i] >>> 1;
            iArr = gF2Polynomial.value;
            iArr[i] = iArr[i] | (gF2Polynomial.value[i + 1] << 31);
            i++;
        }
        int[] iArr2 = gF2Polynomial.value;
        int i2 = gF2Polynomial.blocks - 1;
        iArr2[i2] = iArr2[i2] >>> 1;
        if (gF2Polynomial.blocks < this.blocks) {
            iArr2 = gF2Polynomial.value;
            i2 = gF2Polynomial.blocks - 1;
            iArr2[i2] = iArr2[i2] | (this.value[gF2Polynomial.blocks] << 31);
        }
        return gF2Polynomial;
    }

    public void shiftRightThis() {
        this.len--;
        this.blocks = ((this.len - 1) >>> 5) + 1;
        for (int i = 0; i <= this.blocks - 2; i++) {
            int[] iArr = this.value;
            iArr[i] = iArr[i] >>> 1;
            iArr = this.value;
            iArr[i] = iArr[i] | (this.value[i + 1] << 31);
        }
        int[] iArr2 = this.value;
        int i2 = this.blocks - 1;
        iArr2[i2] = iArr2[i2] >>> 1;
        if ((this.len & 31) == 0) {
            iArr2 = this.value;
            i2 = this.blocks - 1;
            iArr2[i2] = iArr2[i2] | (this.value[this.blocks] << 31);
        }
    }

    public void squareThisBitwise() {
        if (!isZero()) {
            int[] iArr = new int[(this.blocks << 1)];
            for (int i = this.blocks - 1; i >= 0; i--) {
                int i2 = this.value[i];
                int i3 = 1;
                for (int i4 = 0; i4 < 16; i4++) {
                    int i5;
                    if ((i2 & 1) != 0) {
                        i5 = i << 1;
                        iArr[i5] = iArr[i5] | i3;
                    }
                    if ((PKIFailureInfo.notAuthorized & i2) != 0) {
                        i5 = (i << 1) + 1;
                        iArr[i5] = iArr[i5] | i3;
                    }
                    i3 <<= 2;
                    i2 >>>= 1;
                }
            }
            this.value = null;
            this.value = iArr;
            this.blocks = iArr.length;
            this.len = (this.len << 1) - 1;
        }
    }

    public void squareThisPreCalc() {
        if (!isZero()) {
            int i;
            if (this.value.length >= (this.blocks << 1)) {
                for (i = this.blocks - 1; i >= 0; i--) {
                    this.value[(i << 1) + 1] = squaringTable[(this.value[i] & 16711680) >>> 16] | (squaringTable[(this.value[i] & ViewCompat.MEASURED_STATE_MASK) >>> 24] << 16);
                    this.value[i << 1] = squaringTable[this.value[i] & GF2Field.MASK] | (squaringTable[(this.value[i] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >>> 8] << 16);
                }
                this.blocks <<= 1;
                this.len = (this.len << 1) - 1;
                return;
            }
            int[] iArr = new int[(this.blocks << 1)];
            for (i = 0; i < this.blocks; i++) {
                iArr[i << 1] = squaringTable[this.value[i] & GF2Field.MASK] | (squaringTable[(this.value[i] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >>> 8] << 16);
                iArr[(i << 1) + 1] = squaringTable[(this.value[i] & 16711680) >>> 16] | (squaringTable[(this.value[i] & ViewCompat.MEASURED_STATE_MASK) >>> 24] << 16);
            }
            this.value = null;
            this.value = iArr;
            this.blocks <<= 1;
            this.len = (this.len << 1) - 1;
        }
    }

    public GF2Polynomial subtract(GF2Polynomial gF2Polynomial) {
        return xor(gF2Polynomial);
    }

    public void subtractFromThis(GF2Polynomial gF2Polynomial) {
        expandN(gF2Polynomial.len);
        xorThisBy(gF2Polynomial);
    }

    public boolean testBit(int i) {
        if (i >= 0) {
            return i <= this.len + -1 && (this.value[i >>> 5] & bitMask[i & 31]) != 0;
        } else {
            throw new RuntimeException();
        }
    }

    public byte[] toByteArray() {
        int i;
        int i2 = 0;
        int i3 = ((this.len - 1) >> 3) + 1;
        int i4 = i3 & 3;
        byte[] bArr = new byte[i3];
        for (i = 0; i < (i3 >> 2); i++) {
            int i5 = (i3 - (i << 2)) - 1;
            bArr[i5] = (byte) (this.value[i] & GF2Field.MASK);
            bArr[i5 - 1] = (byte) ((this.value[i] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >>> 8);
            bArr[i5 - 2] = (byte) ((this.value[i] & 16711680) >>> 16);
            bArr[i5 - 3] = (byte) ((this.value[i] & ViewCompat.MEASURED_STATE_MASK) >>> 24);
        }
        while (i2 < i4) {
            i = ((i4 - i2) - 1) << 3;
            bArr[i2] = (byte) ((this.value[this.blocks - 1] & (GF2Field.MASK << i)) >>> i);
            i2++;
        }
        return bArr;
    }

    public BigInteger toFlexiBigInt() {
        return (this.len == 0 || isZero()) ? new BigInteger(0, new byte[0]) : new BigInteger(1, toByteArray());
    }

    public int[] toIntegerArray() {
        Object obj = new int[this.blocks];
        System.arraycopy(this.value, 0, obj, 0, this.blocks);
        return obj;
    }

    public String toString(int i) {
        String str;
        char[] cArr = new char[]{LLVARUtil.EMPTY_STRING, LLVARUtil.PLAIN_TEXT, LLVARUtil.HEX_STRING, '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        String[] strArr = new String[]{"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
        String str2 = new String();
        int i2;
        if (i == 16) {
            str = str2;
            i2 = this.blocks - 1;
            while (i2 >= 0) {
                i2--;
                str = ((((((((str + cArr[(this.value[i2] >>> 28) & 15]) + cArr[(this.value[i2] >>> 24) & 15]) + cArr[(this.value[i2] >>> 20) & 15]) + cArr[(this.value[i2] >>> 16) & 15]) + cArr[(this.value[i2] >>> 12) & 15]) + cArr[(this.value[i2] >>> 8) & 15]) + cArr[(this.value[i2] >>> 4) & 15]) + cArr[this.value[i2] & 15]) + " ";
            }
        } else {
            str = str2;
            i2 = this.blocks - 1;
            while (i2 >= 0) {
                i2--;
                str = ((((((((str + strArr[(this.value[i2] >>> 28) & 15]) + strArr[(this.value[i2] >>> 24) & 15]) + strArr[(this.value[i2] >>> 20) & 15]) + strArr[(this.value[i2] >>> 16) & 15]) + strArr[(this.value[i2] >>> 12) & 15]) + strArr[(this.value[i2] >>> 8) & 15]) + strArr[(this.value[i2] >>> 4) & 15]) + strArr[this.value[i2] & 15]) + " ";
            }
        }
        return str;
    }

    public boolean vectorMult(GF2Polynomial gF2Polynomial) {
        boolean z = false;
        if (this.len != gF2Polynomial.len) {
            throw new RuntimeException();
        }
        for (int i = 0; i < this.blocks; i++) {
            int i2 = this.value[i] & gF2Polynomial.value[i];
            z = (((z ^ parity[i2 & GF2Field.MASK]) ^ parity[(i2 >>> 8) & GF2Field.MASK]) ^ parity[(i2 >>> 16) & GF2Field.MASK]) ^ parity[(i2 >>> 24) & GF2Field.MASK];
        }
        return z;
    }

    public GF2Polynomial xor(GF2Polynomial gF2Polynomial) {
        GF2Polynomial gF2Polynomial2;
        int i = 0;
        int min = Math.min(this.blocks, gF2Polynomial.blocks);
        int[] iArr;
        if (this.len >= gF2Polynomial.len) {
            gF2Polynomial2 = new GF2Polynomial(this);
            while (i < min) {
                iArr = gF2Polynomial2.value;
                iArr[i] = iArr[i] ^ gF2Polynomial.value[i];
                i++;
            }
        } else {
            gF2Polynomial2 = new GF2Polynomial(gF2Polynomial);
            while (i < min) {
                iArr = gF2Polynomial2.value;
                iArr[i] = iArr[i] ^ this.value[i];
                i++;
            }
        }
        gF2Polynomial2.zeroUnusedBits();
        return gF2Polynomial2;
    }

    public void xorBit(int i) {
        if (i < 0 || i > this.len - 1) {
            throw new RuntimeException();
        }
        int[] iArr = this.value;
        int i2 = i >>> 5;
        iArr[i2] = iArr[i2] ^ bitMask[i & 31];
    }

    public void xorThisBy(GF2Polynomial gF2Polynomial) {
        for (int i = 0; i < Math.min(this.blocks, gF2Polynomial.blocks); i++) {
            int[] iArr = this.value;
            iArr[i] = iArr[i] ^ gF2Polynomial.value[i];
        }
        zeroUnusedBits();
    }
}
