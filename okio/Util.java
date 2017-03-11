package okio;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import java.nio.charset.Charset;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

final class Util {
    public static final Charset UTF_8;

    static {
        UTF_8 = Charset.forName("UTF-8");
    }

    private Util() {
    }

    public static void checkOffsetAndCount(long j, long j2, long j3) {
        if ((j2 | j3) < 0 || j2 > j || j - j2 < j3) {
            throw new ArrayIndexOutOfBoundsException(String.format("size=%s offset=%s byteCount=%s", new Object[]{Long.valueOf(j), Long.valueOf(j2), Long.valueOf(j3)}));
        }
    }

    public static short reverseBytesShort(short s) {
        int i = HCEClientConstants.HIGHEST_ATC_DEC_VALUE & s;
        return (short) (((i & GF2Field.MASK) << 8) | ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i) >>> 8));
    }

    public static int reverseBytesInt(int i) {
        return ((((ViewCompat.MEASURED_STATE_MASK & i) >>> 24) | ((16711680 & i) >>> 8)) | ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i) << 8)) | ((i & GF2Field.MASK) << 24);
    }

    public static long reverseBytesLong(long j) {
        return ((((((((-72057594037927936L & j) >>> 56) | ((71776119061217280L & j) >>> 40)) | ((280375465082880L & j) >>> 24)) | ((1095216660480L & j) >>> 8)) | ((4278190080L & j) << 8)) | ((16711680 & j) << 24)) | ((65280 & j) << 40)) | ((255 & j) << 56);
    }

    public static void sneakyRethrow(Throwable th) {
        sneakyThrow2(th);
    }

    private static <T extends Throwable> void sneakyThrow2(Throwable th) {
        throw th;
    }

    public static boolean arrayRangeEquals(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            if (bArr[i4 + i] != bArr2[i4 + i2]) {
                return false;
            }
        }
        return true;
    }
}
