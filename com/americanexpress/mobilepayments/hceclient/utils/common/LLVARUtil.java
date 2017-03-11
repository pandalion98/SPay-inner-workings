package com.americanexpress.mobilepayments.hceclient.utils.common;

import com.americanexpress.sdkmodulelib.util.Constants;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.ArrayList;
import java.util.List;

public class LLVARUtil {
    public static final char EMPTY_STRING = '0';
    public static final char HEX_STRING = '2';
    public static final char PLAIN_TEXT = '1';

    public static byte[] objectsToLLVar(Object... objArr) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(toLLVar(String.valueOf(objArr.length)));
        for (Object obj : objArr) {
            String str = (String) obj;
            if (str == null || str.length() == 0 || str.charAt(0) == EMPTY_STRING) {
                stringBuilder.append("110");
            } else {
                char charAt = str.charAt(0);
                if (charAt == PLAIN_TEXT || charAt == HEX_STRING) {
                    stringBuilder.append(toLLVar(str));
                } else {
                    throw new IllegalArgumentException("Not correct format");
                }
            }
        }
        stringBuilder.append(Constants.CLIENT_MINOR_VERSION);
        return stringBuilder.toString().getBytes();
    }

    public static Object[] llVarToObjects(byte[] bArr) {
        int i = 0;
        char[] toCharArray = new String(bArr).toCharArray();
        List arrayList = new ArrayList();
        int i2 = 0;
        while (i2 < toCharArray.length) {
            int i3;
            if (Character.isDigit(toCharArray[i2])) {
                int i4;
                String str;
                StringBuilder append;
                int i5;
                int numericValue = Character.getNumericValue(toCharArray[i2]);
                String str2 = BuildConfig.FLAVOR;
                String str3;
                if (toCharArray.length >= numericValue) {
                    i4 = 0;
                    str3 = str2;
                    i3 = i2;
                    str = str3;
                    while (i4 < numericValue) {
                        append = new StringBuilder().append(str);
                        i5 = i3 + 1;
                        i4++;
                        str = append.append(toCharArray[i5]).toString();
                        i3 = i5;
                    }
                } else {
                    str3 = str2;
                    i3 = i2;
                    str = str3;
                }
                numericValue = Integer.parseInt(str);
                Object obj = BuildConfig.FLAVOR;
                if (toCharArray.length >= numericValue) {
                    i4 = 0;
                    while (i4 < numericValue) {
                        append = new StringBuilder().append(obj);
                        i5 = i3 + 1;
                        i4++;
                        str = append.append(toCharArray[i5]).toString();
                        i3 = i5;
                    }
                }
                arrayList.add(obj);
            } else {
                i3 = i2;
            }
            i2 = i3 + 1;
        }
        arrayList.remove(arrayList.size() - 1);
        if (Integer.parseInt((String) arrayList.remove(0)) != arrayList.size()) {
            return null;
        }
        while (i < arrayList.size()) {
            arrayList.set(i, getCharBytesFromHexBytes((String) arrayList.get(i)));
            i++;
        }
        return arrayList.toArray();
    }

    public static String toLLVar(String str) {
        StringBuilder stringBuilder = null;
        if (str != null) {
            if (str.length() != 0) {
                str.trim();
                String valueOf = String.valueOf(str.length());
                stringBuilder = new StringBuilder(String.valueOf(valueOf.length()));
                stringBuilder.append(valueOf);
                stringBuilder.append(str);
            } else {
                stringBuilder = new StringBuilder(Constants.CLIENT_MINOR_VERSION);
            }
        }
        return stringBuilder.toString();
    }

    public static String byteArrayToString(byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        String str = new String(bArr);
        return str.substring(str.length() - Integer.parseInt(str.substring(1, Integer.parseInt(String.valueOf(str.charAt(0))) + 1)));
    }

    public static byte[] stringToByteArray(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        str.trim();
        String valueOf = String.valueOf(str.length());
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(valueOf.length()));
        stringBuilder.append(valueOf);
        stringBuilder.append(str);
        return stringBuilder.toString().getBytes();
    }

    public static byte[] emptyByteArray() {
        return Constants.CLIENT_MINOR_VERSION.getBytes();
    }

    public static Object llVarToObject(byte[] bArr, int i) {
        Object[] llVarToObjects = llVarToObjects(bArr);
        if (llVarToObjects.length <= 0 || llVarToObjects[i] == null) {
            return null;
        }
        return llVarToObjects[i];
    }

    public static byte[] zeroByteArray(int i) {
        StringBuilder append = new StringBuilder("111").append(String.valueOf(String.valueOf(i).length())).append(i);
        for (int i2 = 0; i2 < i; i2++) {
            append.append(EMPTY_STRING);
        }
        append.append(Constants.CLIENT_MINOR_VERSION);
        return append.toString().getBytes();
    }

    public static int getBufferLength(String str) {
        int i = 0;
        char[] toCharArray = str.toCharArray();
        String str2 = BuildConfig.FLAVOR;
        if (Character.isDigit(toCharArray[3])) {
            int numericValue = Character.getNumericValue(toCharArray[3]);
            if (toCharArray.length >= numericValue) {
                int i2 = 0;
                while (i < numericValue) {
                    i2++;
                    i++;
                    str2 = str2 + toCharArray[i2 + 3];
                }
            }
        }
        return Integer.parseInt(str2);
    }

    private static String getCharBytesFromHexBytes(String str) {
        int i = 1;
        if (str == null) {
            throw new IllegalArgumentException("Empty Or Null Input: " + str);
        } else if (str.charAt(0) != HEX_STRING) {
            return str.substring(1);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            while (i < str.length()) {
                StringBuilder stringBuilder2 = new StringBuilder(2);
                stringBuilder2.append(str.charAt(i)).append(str.charAt(i + 1));
                stringBuilder.append((char) Integer.parseInt(stringBuilder2.toString(), 16));
                i += 2;
            }
            return stringBuilder.toString();
        }
    }
}
