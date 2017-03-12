package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bouncycastle.asn1.eac.EACTags;

/* renamed from: com.samsung.android.spayfw.payprovider.amexv2.tzsvc.g */
public class LLVARByteUtil {
    public static byte[] m820d(String[] strArr) {
        byte[][] bArr = new byte[strArr.length][];
        for (int i = 0; i < strArr.length; i++) {
            bArr[i] = strArr[i].getBytes();
        }
        try {
            return LLVARByteUtil.bytesToLLVar(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] bytesToLLVar(byte[][] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(LLVARByteUtil.byteArrayToLLVar(LLVARByteUtil.intToBytes(bArr.length)));
        for (byte[] bArr2 : bArr) {
            if (bArr2 == null || bArr2.length == 0 || ((char) bArr2[0]) == LLVARUtil.EMPTY_STRING) {
                byteArrayOutputStream.write(LLVARByteUtil.intToBytes(EACTags.APPLICATION_RELATED_DATA));
            } else {
                char c = (char) bArr2[0];
                if (c == LLVARUtil.PLAIN_TEXT || c == LLVARUtil.HEX_STRING) {
                    byteArrayOutputStream.write(LLVARByteUtil.byteArrayToLLVar(bArr2));
                } else {
                    throw new IllegalArgumentException("Not correct format");
                }
            }
        }
        byteArrayOutputStream.write(LLVARByteUtil.intToBytes(10));
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[][] llVarToBytes(byte[] bArr) {
        char[] charArrayFromByteArray = LLVARByteUtil.getCharArrayFromByteArray(bArr);
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
                        i = Character.digit(charArrayFromByteArray[i4], 10) + (i * 10);
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
                    if (LLVARByteUtil.getIntFromByteArray((byte[]) arrayList.get(0)) >= arrayList.size()) {
                        arrayList.add(LLVARByteUtil.getDecBytesFromHexBytes(toByteArray));
                    }
                } else if (arrayList.size() == 0) {
                    arrayList.add(toByteArray);
                }
            } else {
                i2 = i;
            }
            i = i2 + 1;
        }
        if (LLVARByteUtil.getIntFromByteArray((byte[]) arrayList.remove(0)) != arrayList.size()) {
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
            i2 = (i2 * 10) + Character.digit(bArr[i], 10);
            i++;
        }
        return i2;
    }

    private static byte[] byteArrayToLLVar(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] intToBytes = LLVARByteUtil.intToBytes(bArr.length);
        byteArrayOutputStream.write(LLVARByteUtil.intToBytes(intToBytes.length));
        byteArrayOutputStream.write(intToBytes);
        byteArrayOutputStream.write(bArr);
        return byteArrayOutputStream.toByteArray();
    }

    private static byte[] intToBytes(int i) {
        if (i > 0) {
            int length;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            char[] cArr = new char[((int) (Math.log10((double) i) + 1.0d))];
            for (length = cArr.length - 1; length >= 0; length--) {
                cArr[length] = Character.forDigit(i % 10, 10);
                i /= 10;
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

    private static byte[] getDecBytesFromHexBytes(byte[] bArr) {
        int i = 1;
        if (bArr == null || bArr.length == 0) {
            throw new IllegalArgumentException("Empty Or Null Input");
        } else if (bArr[0] != 50) {
            return Arrays.copyOfRange(bArr, 1, bArr.length);
        } else {
            byte[] bArr2 = new byte[(bArr.length / 2)];
            while (i < bArr.length) {
                StringBuilder stringBuilder = new StringBuilder(2);
                stringBuilder.append((char) bArr[i]).append((char) bArr[i + 1]);
                bArr2[i / 2] = (byte) Integer.parseInt(stringBuilder.toString(), 16);
                i += 2;
            }
            return bArr2;
        }
    }
}
