package com.americanexpress.sdkmodulelib.tlv;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Date;
import java.util.StringTokenizer;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class Util {

    /* renamed from: com.americanexpress.sdkmodulelib.tlv.Util.1 */
    static class C00761 extends SecurityManager {
        C00761() {
        }

        public Class[] getClassContext() {
            return super.getClassContext();
        }
    }

    private Util() {
        throw new UnsupportedOperationException("Not allowed to instantiate");
    }

    public static String getSpaces(int i) {
        StringBuilder stringBuilder = new StringBuilder(i);
        for (int i2 = 0; i2 < i; i2++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public static String prettyPrintHex(String str, int i, boolean z) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < str.length(); i2++) {
            stringBuilder.append(str.charAt(i2));
            int i3 = i2 + 1;
            if (z && i3 % 32 == 0 && i3 != str.length()) {
                stringBuilder.append("\n").append(getSpaces(i));
            } else if (i3 % 2 == 0 && i3 != str.length()) {
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    public static String prettyPrintHex(String str, int i) {
        return prettyPrintHex(str, i, true);
    }

    public static String prettyPrintHex(byte[] bArr, int i) {
        return prettyPrintHex(byteArrayToHexString(bArr), i, true);
    }

    public static String prettyPrintHex(byte[] bArr) {
        return prettyPrintHex(byteArrayToHexString(bArr), 0, true);
    }

    public static String prettyPrintHex(byte[] bArr, int i, int i2) {
        return prettyPrintHex(byteArrayToHexString(bArr, i, i2), 0, true);
    }

    public static String prettyPrintHexNoWrap(byte[] bArr) {
        return prettyPrintHex(byteArrayToHexString(bArr), 0, false);
    }

    public static String prettyPrintHexNoWrap(byte[] bArr, int i, int i2) {
        return prettyPrintHex(byteArrayToHexString(bArr, i, i2), 0, false);
    }

    public static String prettyPrintHexNoWrap(String str) {
        return prettyPrintHex(str, 0, false);
    }

    public static String prettyPrintHex(String str) {
        return prettyPrintHex(str, 0, true);
    }

    public static String prettyPrintHex(BigInteger bigInteger) {
        byte[] bArr;
        Object toByteArray = bigInteger.toByteArray();
        if (toByteArray[0] == null) {
            bArr = new byte[(toByteArray.length - 1)];
            System.arraycopy(toByteArray, 1, bArr, 0, toByteArray.length - 1);
        } else {
            Object obj = toByteArray;
        }
        return prettyPrintHex(bArr);
    }

    public static byte[] performRSA(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        int length = bArr.length;
        if (bArr2[0] >= VerifyPINApdu.P2_PLAINTEXT) {
            Object obj = new byte[(bArr2.length + 1)];
            obj[0] = null;
            System.arraycopy(bArr2, 0, obj, 1, bArr2.length);
            bArr2 = obj;
        }
        if (bArr3[0] >= VerifyPINApdu.P2_PLAINTEXT) {
            obj = new byte[(bArr3.length + 1)];
            obj[0] = null;
            System.arraycopy(bArr3, 0, obj, 1, bArr3.length);
            bArr3 = obj;
        }
        if (bArr[0] >= VerifyPINApdu.P2_PLAINTEXT) {
            obj = new byte[(bArr.length + 1)];
            obj[0] = null;
            System.arraycopy(bArr, 0, obj, 1, bArr.length);
            bArr = obj;
        }
        Object toByteArray = new BigInteger(bArr).modPow(new BigInteger(bArr2), new BigInteger(bArr3)).toByteArray();
        if (toByteArray.length != length + 1 || toByteArray[0] != null) {
            return toByteArray;
        }
        obj = new byte[length];
        System.arraycopy(toByteArray, 1, obj, 0, length);
        return obj;
    }

    public static byte[] calculateSHA1(byte[] bArr) {
        return MessageDigest.getInstance("SHA-1").digest(bArr);
    }

    public static String byte2Hex(byte b) {
        String[] strArr = new String[]{TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE, TransactionInfo.VISA_TRANSACTIONTYPE_REFUND, TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED, TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED, DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR, "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        int i = b & GF2Field.MASK;
        return strArr[(i >>> 4) & 15] + strArr[i & 15];
    }

    public static String short2Hex(short s) {
        return byte2Hex((byte) (s >>> 8)) + byte2Hex((byte) (s & GF2Field.MASK));
    }

    public static int byteToInt(byte b) {
        return b & GF2Field.MASK;
    }

    public static int byteToInt(byte b, byte b2) {
        return ((b & GF2Field.MASK) << 8) + (b2 & GF2Field.MASK);
    }

    public static short byte2Short(byte b, byte b2) {
        return (short) ((b << 8) | (b2 & GF2Field.MASK));
    }

    public static String getFormattedNanoTime(long j) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((int) (j / 1000000));
        stringBuilder.append("ms ");
        stringBuilder.append(j % 1000000);
        stringBuilder.append("ns");
        return stringBuilder.toString();
    }

    public static byte[] getCurrentDateAsNumericEncodedByteArray() {
        return fromHexString(new SimpleDateFormat("yyMMdd").format(new Date()));
    }

    public static String getSafePrintChars(byte[] bArr) {
        if (bArr == null) {
            return BuildConfig.FLAVOR;
        }
        return getSafePrintChars(bArr, 0, bArr.length);
    }

    public static String getSafePrintChars(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return BuildConfig.FLAVOR;
        }
        if (bArr.length < i + i2) {
            throw new IllegalArgumentException("startPos(" + i + ")+length(" + i2 + ") > byteArray.length(" + bArr.length + ")");
        }
        StringBuilder stringBuilder = new StringBuilder();
        int i3 = i;
        while (i3 < i + i2) {
            if (bArr[i3] < 32 || bArr[i3] >= 127) {
                stringBuilder.append(".");
            } else {
                stringBuilder.append((char) bArr[i3]);
            }
            i3++;
        }
        return stringBuilder.toString();
    }

    public static String byteArrayToHexString(byte[] bArr) {
        if (bArr == null) {
            return BuildConfig.FLAVOR;
        }
        return byteArrayToHexString(bArr, 0, bArr.length);
    }

    public static String byteArrayToHexString(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return BuildConfig.FLAVOR;
        }
        if (bArr.length < i + i2) {
            throw new IllegalArgumentException("startPos(" + i + ")+length(" + i2 + ") > byteArray.length(" + bArr.length + ")");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i3 = 0; i3 < i2; i3++) {
            stringBuilder.append(Integer.toHexString((bArr[i + i3] & GF2Field.MASK) | -256).substring(6));
        }
        return stringBuilder.toString();
    }

    public static String int2Hex(int i) {
        String toHexString = Integer.toHexString(i);
        if (toHexString.length() % 2 != 0) {
            return TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE + toHexString;
        }
        return toHexString;
    }

    public static String int2HexZeroPad(int i) {
        String int2Hex = int2Hex(i);
        if (int2Hex.length() % 2 != 0) {
            return TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE + int2Hex;
        }
        return int2Hex;
    }

    public static byte[] intToByteArray(int i) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte b = (byte) (i >>> 24);
        byte b2 = (byte) (i >>> 16);
        byte b3 = (byte) (i >>> 8);
        byte b4 = (byte) i;
        Object obj = null;
        if (b > null) {
            byteArrayOutputStream.write(b);
            obj = 1;
        }
        if (obj != null || b2 > null) {
            byteArrayOutputStream.write(b2);
            obj = 1;
        }
        if (obj != null || b3 > null) {
            byteArrayOutputStream.write(b3);
        }
        byteArrayOutputStream.write(b4);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] intToByteArray4(int i) {
        return new byte[]{(byte) (i >>> 24), (byte) (i >>> 16), (byte) (i >>> 8), (byte) i};
    }

    public static int byteArrayToInt(byte[] bArr) {
        return byteArrayToInt(bArr, 0, bArr.length);
    }

    public static int byteArrayToInt(byte[] bArr, int i, int i2) {
        int i3 = 0;
        if (bArr == null) {
            throw new IllegalArgumentException("Parameter 'byteArray' cannot be null");
        } else if (i2 <= 0 || i2 > 4) {
            throw new IllegalArgumentException("Length must be between 1 and 4. Length = " + i2);
        } else if (i2 == 4 && isBitSet(bArr[i], 8)) {
            throw new IllegalArgumentException("Signed bit is set (leftmost bit): " + byte2Hex(bArr[i]));
        } else {
            int i4 = 0;
            while (i3 < i2) {
                i4 += (bArr[i + i3] & GF2Field.MASK) << (((i2 - i3) - 1) * 8);
                i3++;
            }
            return i4;
        }
    }

    public static long byteArrayToLong(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            throw new IllegalArgumentException("Parameter 'byteArray' cannot be null");
        } else if (i2 <= 0 || i2 > 8) {
            throw new IllegalArgumentException("Length must be between 1 and 4. Length = " + i2);
        } else if (i2 == 8 && isBitSet(bArr[i], 8)) {
            throw new IllegalArgumentException("Signed bit is set (leftmost bit): " + byte2Hex(bArr[i]));
        } else {
            long j = 0;
            for (int i3 = 0; i3 < i2; i3++) {
                j += (((long) bArr[i + i3]) & 255) << (((i2 - i3) - 1) * 8);
            }
            return j;
        }
    }

    public static byte[] fromHexString(String str) {
        int i = 0;
        String removeSpaces = removeSpaces(str);
        if (removeSpaces.length() == 0) {
            return new byte[0];
        }
        if (removeSpaces.length() % 2 != 0) {
            throw new IllegalArgumentException("Input string must contain an even number of characters: " + removeSpaces);
        }
        byte[] bArr = new byte[(removeSpaces.length() / 2)];
        char[] toCharArray = removeSpaces.toCharArray();
        while (i < toCharArray.length) {
            StringBuilder stringBuilder = new StringBuilder(2);
            stringBuilder.append(toCharArray[i]).append(toCharArray[i + 1]);
            bArr[i / 2] = (byte) Integer.parseInt(stringBuilder.toString(), 16);
            i += 2;
        }
        return bArr;
    }

    public static String removeCRLFTab(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, "\r\n\t", false);
        StringBuilder stringBuilder = new StringBuilder();
        while (stringTokenizer.hasMoreElements()) {
            stringBuilder.append(stringTokenizer.nextElement());
        }
        return stringBuilder.toString();
    }

    public static String removeSpaces(String str) {
        return str.replaceAll(" ", BuildConfig.FLAVOR);
    }

    public static byte[] intToBinaryEncodedDecimalByteArray(int i) {
        String valueOf = String.valueOf(i);
        if (valueOf.length() % 2 != 0) {
            valueOf = TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE + valueOf;
        }
        return fromHexString(valueOf);
    }

    public static int binaryCodedDecimalToInt(byte b) {
        try {
            return Integer.parseInt(byte2Hex(b));
        } catch (Throwable e) {
            throw new IllegalArgumentException("The hex representation of argument b must be digits only, and integer", e);
        }
    }

    public static int binaryHexCodedDecimalToInt(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Param hex cannot be null");
        }
        String removeSpaces = removeSpaces(str);
        if (removeSpaces.length() > 10) {
            throw new IllegalArgumentException("There must be a maximum of 5 hex octets. hex=" + removeSpaces);
        }
        try {
            return Integer.parseInt(removeSpaces);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Argument hex must be all digits. hex=" + removeSpaces, e);
        }
    }

    public static int binaryHexCodedDecimalToInt(byte[] bArr) {
        if (bArr != null) {
            return binaryHexCodedDecimalToInt(byteArrayToHexString(bArr));
        }
        throw new IllegalArgumentException("Param bcdArray cannot be null");
    }

    public static String byte2BinaryLiteral(byte b) {
        String toBinaryString = Integer.toBinaryString(byteToInt(b));
        if (toBinaryString.length() >= 8) {
            return toBinaryString;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8 - toBinaryString.length(); i++) {
            stringBuilder.append(LLVARUtil.EMPTY_STRING);
        }
        stringBuilder.append(toBinaryString);
        return stringBuilder.toString();
    }

    public static BitSet byteArray2BitSet(byte[] bArr) {
        BitSet bitSet = new BitSet();
        for (int i = 0; i < bArr.length * 8; i++) {
            if ((bArr[(bArr.length - (i / 8)) - 1] & (1 << (i % 8))) > 0) {
                bitSet.set(i);
            }
        }
        return bitSet;
    }

    public static byte[] bitSet2ByteArray(BitSet bitSet) {
        byte[] bArr = new byte[((bitSet.length() / 8) + 1)];
        for (int i = 0; i < bitSet.length(); i++) {
            if (bitSet.get(i)) {
                int length = (bArr.length - (i / 8)) - 1;
                bArr[length] = (byte) (bArr[length] | (1 << (i % 8)));
            }
        }
        return bArr;
    }

    public static boolean isBitSet(byte b, int i) {
        if (i < 1 || i > 8) {
            throw new IllegalArgumentException("parameter 'bitPos' must be between 1 and 8. bitPos=" + i);
        } else if (((b >>> (i - 1)) & 1) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static byte setBit(byte b, int i, boolean z) {
        if (i < 1 || i > 8) {
            throw new IllegalArgumentException("parameter 'bitPos' must be between 1 and 8. bitPos=" + i);
        } else if (z) {
            return (byte) ((1 << (i - 1)) | b);
        } else {
            return (byte) (((1 << (i - 1)) ^ -1) & b);
        }
    }

    public static byte[] generateRandomBytes(int i) {
        byte[] bArr = new byte[i];
        new SecureRandom().nextBytes(bArr);
        return bArr;
    }

    public static InputStream loadResource(Class cls, String str) {
        return cls.getResourceAsStream(str);
    }

    public static byte[] resizeArray(byte[] bArr, int i) {
        int i2 = 0;
        if (bArr == null) {
            throw new IllegalArgumentException("byte array cannot be null");
        } else if (i < 0) {
            throw new IllegalArgumentException("Illegal new length: " + i + ". Must be >= 0");
        } else if (i == 0) {
            return new byte[0];
        } else {
            Object obj = new byte[i];
            int length = obj.length > bArr.length ? 0 : bArr.length - obj.length;
            if (obj.length > bArr.length) {
                i2 = obj.length - bArr.length;
            }
            System.arraycopy(bArr, length, obj, i2, obj.length > bArr.length ? bArr.length : obj.length);
            return obj;
        }
    }

    public static byte[] copyByteArray(byte[] bArr) {
        if (bArr != null) {
            return copyByteArray(bArr, 0, bArr.length);
        }
        throw new IllegalArgumentException("Argument 'array2Copy' cannot be null");
    }

    public static byte[] copyByteArray(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            throw new IllegalArgumentException("Argument 'array2Copy' cannot be null");
        } else if (bArr.length < i + i2) {
            throw new IllegalArgumentException("startPos(" + i + ")+length(" + i2 + ") > byteArray.length(" + bArr.length + ")");
        } else {
            Object obj = new byte[bArr.length];
            System.arraycopy(bArr, i, obj, 0, i2);
            return obj;
        }
    }

    public static String getStackTrace(Throwable th) {
        Writer stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static String readInputStreamToString(InputStream inputStream, String str) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, str);
        char[] cArr = new char[5000];
        StringBuilder stringBuilder = new StringBuilder(5000);
        for (int read = inputStreamReader.read(cArr, 0, cArr.length); read != -1; read = inputStreamReader.read(cArr, 0, cArr.length)) {
            stringBuilder.append(cArr, 0, read);
        }
        return stringBuilder.toString();
    }

    public static void writeStringToFile(String str, String str2, boolean z) {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(str2, z));
        bufferedWriter.write(str);
        bufferedWriter.close();
    }

    public static Class getCallerClass(int i) {
        int i2 = 0;
        Class[] classContext = new C00761().getClassContext();
        if (classContext != null) {
            while (i2 < classContext.length) {
                if (classContext[i2] == Util.class) {
                    return classContext[i2 + i];
                }
                i2++;
            }
        } else {
            try {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                while (i2 < stackTrace.length) {
                    if (Class.forName(stackTrace[i2].getClassName()) == Util.class) {
                        return Class.forName(stackTrace[i2 + i].getClassName());
                    }
                    i2++;
                }
            } catch (ClassNotFoundException e) {
            }
        }
        return null;
    }

    public static void main(String[] strArr) {
        System.out.println(prettyPrintHexNoWrap(resizeArray(new byte[]{(byte) 1}, 0)));
        System.out.println(prettyPrintHexNoWrap(resizeArray(new byte[]{(byte) 1}, 1)));
        System.out.println(prettyPrintHexNoWrap(resizeArray(new byte[]{(byte) 1}, 2)));
        System.out.println(prettyPrintHexNoWrap(resizeArray(new byte[]{(byte) 1, (byte) 2}, 1)));
        System.out.println(prettyPrintHexNoWrap(resizeArray(new byte[]{(byte) 1, (byte) 2}, 4)));
    }
}
