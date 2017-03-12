package com.samsung.android.contextaware.utilbundle;

import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class CaConvertUtil {
    public static int strToInt(String value) {
        if (value == null) {
            return 0;
        }
        try {
            if (value.toUpperCase().startsWith("0X")) {
                return Integer.parseInt(value.substring(2), 16);
            }
            if (value.endsWith("B")) {
                return Integer.parseInt(value.substring(0, value.length() - 1), 2);
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            CaLogger.exception(e);
            return 0;
        } catch (Exception e2) {
            CaLogger.exception(e2);
            return 0;
        }
    }

    public static float strToFloat(String value) {
        if (value == null) {
            return 0.0f;
        }
        try {
            if (!value.contains("/")) {
                return Float.parseFloat(value);
            }
            int index = value.indexOf(47);
            return ((float) strToInt(value.substring(0, index))) / ((float) strToInt(value.substring(index + 1)));
        } catch (NumberFormatException e) {
            CaLogger.exception(e);
            return 0.0f;
        } catch (Exception e2) {
            CaLogger.exception(e2);
            return 0.0f;
        }
    }

    public static long strToLong(String value) {
        if (value == null) {
            return 0;
        }
        try {
            if (value.toUpperCase().startsWith("0X")) {
                return Long.parseLong(value.substring(2), 16);
            }
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            CaLogger.exception(e);
            return 0;
        } catch (Exception e2) {
            CaLogger.exception(e2);
            return 0;
        }
    }

    public static double strToDouble(String value) {
        if (value == null) {
            return 0.0d;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            CaLogger.exception(e);
            return 0.0d;
        } catch (Exception e2) {
            CaLogger.exception(e2);
            return 0.0d;
        }
    }

    public static byte[] intToByteArr(int value, int size) {
        if (size > 4) {
            return null;
        }
        int i;
        byte[] bytes = new byte[size];
        for (i = 0; i < size; i++) {
            bytes[i] = (byte) ((value >> (i * 8)) & 255);
        }
        byte[] result = new byte[size];
        int len = bytes.length;
        for (i = 0; i < bytes.length; i++) {
            len--;
            result[i] = bytes[len];
        }
        return result;
    }

    public static String byteArrToString(byte[] data) {
        if (data == null || data.length <= 0) {
            CaLogger.error("Data is null");
            return null;
        }
        StringBuffer str = new StringBuffer();
        for (byte k : data) {
            str.append(k + ", ");
        }
        if (str.length() > 1) {
            str.delete(str.length() - 1, str.length());
        }
        return str.toString();
    }

    public static byte[] stringToByteArray(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        int len = 0;
        int endIdx = str.indexOf(44, 0);
        while (endIdx <= str.length()) {
            len++;
            endIdx = str.indexOf(44, endIdx + 1);
            if (endIdx < 0) {
                break;
            }
        }
        byte[] arr = new byte[len];
        int startIdx = 0;
        int i = 0;
        int size = 0;
        while (i < len) {
            endIdx = str.indexOf(44, startIdx);
            int size2 = size + 1;
            System.arraycopy(intToByteArr(strToInt(str.substring(startIdx, endIdx)), 1), 0, arr, size, 1);
            startIdx = endIdx + 2;
            i++;
            size = size2;
        }
        return arr;
    }

    public static int getCompleteOfTwo(int value) {
        int complete = value;
        if (value < 0) {
            return (value + 255) + 1;
        }
        return complete;
    }
}
