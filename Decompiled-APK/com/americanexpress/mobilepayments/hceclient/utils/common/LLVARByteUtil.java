package com.americanexpress.mobilepayments.hceclient.utils.common;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bouncycastle.asn1.eac.EACTags;

public class LLVARByteUtil {
    private static final int DECIMAL_RADIX = 10;
    private static final char HEX = '2';
    private static final char PLAIN_TEXT = '1';
    private static final char ZERO = '0';

    public static byte[] bytesToLLVar(byte[][] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] byteArrayToLLVar = byteArrayToLLVar(intToBytes(bArr.length));
        byteArrayOutputStream.write(byteArrayToLLVar, 0, byteArrayToLLVar.length);
        for (byte[] bArr2 : bArr) {
            byte[] bArr22;
            if (bArr22 == null || bArr22.length == 0 || ((char) bArr22[0]) == ZERO) {
                bArr22 = intToBytes(EACTags.APPLICATION_RELATED_DATA);
                byteArrayOutputStream.write(bArr22, 0, bArr22.length);
            } else {
                char c = (char) bArr22[0];
                if (c == PLAIN_TEXT || c == HEX) {
                    bArr22 = byteArrayToLLVar(bArr22);
                    byteArrayOutputStream.write(bArr22, 0, bArr22.length);
                } else {
                    throw new IllegalArgumentException("Not correct format");
                }
            }
        }
        byteArrayToLLVar = intToBytes(DECIMAL_RADIX);
        byteArrayOutputStream.write(byteArrayToLLVar, 0, byteArrayToLLVar.length);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[][] llVarToBytes(byte[] bArr) {
        char[] charArrayFromByteArray = getCharArrayFromByteArray(bArr);
        List arrayList = new ArrayList();
        int i = 0;
        while (i < charArrayFromByteArray.length) {
            int i2;
            if (Character.isDigit(charArrayFromByteArray[i])) {
                int i3;
                int i4;
                int numericValue = Character.getNumericValue(charArrayFromByteArray[i]);
                if (charArrayFromByteArray.length >= numericValue) {
                    i3 = 0;
                    i2 = i;
                    i = 0;
                    while (i3 < numericValue) {
                        i4 = i2 + 1;
                        i3++;
                        i = Character.digit(charArrayFromByteArray[i4], DECIMAL_RADIX) + (i * DECIMAL_RADIX);
                        i2 = i4;
                    }
                } else {
                    i2 = i;
                    i = 0;
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                if (charArrayFromByteArray.length >= i) {
                    i3 = 0;
                    while (i3 < i) {
                        i4 = i2 + 1;
                        byteArrayOutputStream.write(charArrayFromByteArray[i4]);
                        i3++;
                        i2 = i4;
                    }
                }
                Object toByteArray = byteArrayOutputStream.toByteArray();
                if (arrayList.size() > 0) {
                    if (getIntFromByteArray((byte[]) arrayList.get(0)) >= arrayList.size()) {
                        arrayList.add(getDecBytesFromHexBytes(toByteArray));
                    }
                } else if (arrayList.size() == 0) {
                    arrayList.add(toByteArray);
                }
            } else {
                i2 = i;
            }
            i = i2 + 1;
        }
        if (getIntFromByteArray((byte[]) arrayList.remove(0)) != arrayList.size()) {
            return (byte[][]) null;
        }
        return (byte[][]) arrayList.toArray(new byte[arrayList.size()][]);
    }

    private static char[] getCharArrayFromByteArray(byte[] bArr) {
        int i = 0;
        char[] cArr = new char[bArr.length];
        int length = bArr.length;
        int i2 = 0;
        while (i < length) {
            int i3 = i2 + 1;
            cArr[i2] = (char) bArr[i];
            i++;
            i2 = i3;
        }
        return cArr;
    }

    private static int getIntFromByteArray(byte[] bArr) {
        int i = 0;
        int i2 = 0;
        while (i < bArr.length) {
            i2 = (i2 * DECIMAL_RADIX) + Character.digit(bArr[i], DECIMAL_RADIX);
            i++;
        }
        return i2;
    }

    private static byte[] byteArrayToLLVar(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] intToBytes = intToBytes(bArr.length);
        byte[] intToBytes2 = intToBytes(intToBytes.length);
        byteArrayOutputStream.write(intToBytes2, 0, intToBytes2.length);
        byteArrayOutputStream.write(intToBytes, 0, intToBytes.length);
        byteArrayOutputStream.write(bArr, 0, bArr.length);
        return byteArrayOutputStream.toByteArray();
    }

    private static byte[] intToBytes(int i) {
        if (i > 0) {
            int length;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            char[] cArr = new char[((int) (Math.log10((double) i) + 1.0d))];
            for (length = cArr.length - 1; length >= 0; length--) {
                cArr[length] = Character.forDigit(i % DECIMAL_RADIX, DECIMAL_RADIX);
                i /= DECIMAL_RADIX;
            }
            for (char write : cArr) {
                byteArrayOutputStream.write(write);
            }
            return byteArrayOutputStream.toByteArray();
        } else if (i != 0) {
            return null;
        } else {
            return new byte[]{(byte) 48};
        }
    }

    public static byte[] zeroByteArray(int i) {
        if (i <= 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] intToBytes = intToBytes(EACTags.FCI_TEMPLATE);
        byteArrayOutputStream.write(intToBytes, 0, intToBytes.length);
        intToBytes = intToBytes((int) (Math.log10((double) i) + 1.0d));
        byteArrayOutputStream.write(intToBytes, 0, intToBytes.length);
        intToBytes = intToBytes(i);
        byteArrayOutputStream.write(intToBytes, 0, intToBytes.length);
        for (int i2 = 0; i2 < i; i2++) {
            byteArrayOutputStream.write(48);
        }
        intToBytes = intToBytes(DECIMAL_RADIX);
        byteArrayOutputStream.write(intToBytes, 0, intToBytes.length);
        return byteArrayOutputStream.toByteArray();
    }

    public static int getBufferLength(byte[] bArr) {
        int i = 0;
        char[] charArrayFromByteArray = getCharArrayFromByteArray(bArr);
        if (Character.isDigit(charArrayFromByteArray[3])) {
            int numericValue = Character.getNumericValue(charArrayFromByteArray[3]);
            if (charArrayFromByteArray.length >= numericValue) {
                int i2 = 0;
                int i3 = 0;
                while (i2 < numericValue) {
                    i3++;
                    i2++;
                    i = Character.digit(charArrayFromByteArray[i3 + 3], DECIMAL_RADIX) + (i * DECIMAL_RADIX);
                }
            }
        }
        return i;
    }

    private static byte[] getDecBytesFromHexBytes(byte[] bArr) {
        int i = 1;
        if (bArr[0] != 50) {
            return Arrays.copyOfRange(bArr, 1, bArr.length);
        }
        if (bArr == null) {
            throw new IllegalArgumentException("Empty Or Null Input: " + bArr);
        }
        byte[] bArr2 = new byte[(bArr.length / 2)];
        while (i < bArr.length) {
            StringBuilder stringBuilder = new StringBuilder(2);
            stringBuilder.append((char) bArr[i]).append((char) bArr[i + 1]);
            bArr2[i / 2] = (byte) Integer.parseInt(stringBuilder.toString(), 16);
            i += 2;
        }
        return bArr2;
    }

    public static void main(String[] strArr) {
        int i = 0;
        byte[] bytesToLLVar = bytesToLLVar(new byte[][]{"1STRING".getBytes(), "268656c6c6f".getBytes(), "1FLOAT".getBytes(), "1LONG".getBytes(), "1DECIMAL".getBytes()});
        System.out.println(new String(bytesToLLVar));
        byte[][] llVarToBytes = llVarToBytes(bytesToLLVar);
        int length = llVarToBytes.length;
        while (i < length) {
            byte[] bArr = llVarToBytes[i];
            System.out.println("byteArray ::String---" + new String(bArr));
            System.out.println("byteArray ::Content---" + Arrays.toString(bArr));
            i++;
        }
    }
}
