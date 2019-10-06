package com.sec.xmldata.support;

public class NVAccessor {
    private static final String CLASS_NAME = "NVAccessor";
    public static final byte NV_VALUE_ENTER = 69;
    public static final byte NV_VALUE_FAIL = 70;
    public static final byte NV_VALUE_NOTEST = 78;
    public static final byte NV_VALUE_PASS = 80;
    private static NVAccessor mNVAccessor = new NVAccessor();
    private static boolean mSuccessLibLoad;

    /* access modifiers changed from: 0000 */
    public native String nativeGetFullTestNV();

    /* access modifiers changed from: 0000 */
    public native String nativeGetNV(int i);

    /* access modifiers changed from: 0000 */
    public native String nativeGetNVHistory();

    /* access modifiers changed from: 0000 */
    public native int nativeSetFullTestNV(String str);

    /* access modifiers changed from: 0000 */
    public native int nativeSetNV(int i, String str);

    static {
        mSuccessLibLoad = true;
        try {
            System.loadLibrary("nvaccessor_fb");
        } catch (UnsatisfiedLinkError e) {
            XmlUtil.log_e(CLASS_NAME, "WARNING: Could not load libnvaccessor");
            mSuccessLibLoad = false;
        }
    }

    private NVAccessor() {
    }

    public static byte getNV(int key) {
        if (key > 0 && mSuccessLibLoad) {
            try {
                String value = mNVAccessor.nativeGetNV(key);
                if (value != null) {
                    return (byte) value.charAt(0);
                }
                XmlUtil.log_e(CLASS_NAME, "Can not access NV, please check the libomission_avoidance.so");
            } catch (Exception e) {
                XmlUtil.log_e(e);
            }
        }
        return NV_VALUE_NOTEST;
    }

    public static String getFullTestNV() {
        if (mSuccessLibLoad) {
            try {
                return mNVAccessor.nativeGetFullTestNV();
            } catch (Exception e) {
                XmlUtil.log_e(CLASS_NAME, "Can not access NV, please check the libomission_avoidance.so");
                XmlUtil.log_e(e);
            }
        }
        return null;
    }

    public static int setNV(int key, byte value) {
        if (!mSuccessLibLoad) {
            return -1;
        }
        if (mNVAccessor.nativeSetNV(key, Character.toString((char) value)) >= 0) {
            return 0;
        }
        XmlUtil.log_e(CLASS_NAME, "Can not access NV, please check the libomission_avoidance.so");
        return -1;
    }

    public static int setFullTestNV(String value) {
        if (!mSuccessLibLoad) {
            return -1;
        }
        if (mNVAccessor.nativeSetFullTestNV(value) >= 0) {
            return 0;
        }
        XmlUtil.log_e(CLASS_NAME, "Can not access NV, please check the libomission_avoidance.so");
        return -1;
    }

    public static String getNVHistory() {
        if (!mSuccessLibLoad) {
            return null;
        }
        String value = mNVAccessor.nativeGetNVHistory();
        if (value == null) {
            XmlUtil.log_e(CLASS_NAME, "Can not access NV, please check the libomission_avoidance.so");
        }
        return value;
    }

    public static String setLocalTestNV(String source, int key, byte value) {
        if (source == null || source.isEmpty() || source.length() != 2000) {
            StringBuilder sb = new StringBuilder();
            sb.append("invalid source = ");
            sb.append(source);
            XmlUtil.log_e(CLASS_NAME, "setLocalTestNV", sb.toString());
            return null;
        } else if (key <= 0 || key > 500) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("invalid key = ");
            sb2.append(key);
            XmlUtil.log_e(CLASS_NAME, "setLocalTestNV", sb2.toString());
            return null;
        } else if (value == 78 || value == 80 || value == 69 || value == 70) {
            String front = source.substring(0, ((key - 1) * 4) + 3);
            String rear = source.substring(4 * key, source.length());
            StringBuilder sb3 = new StringBuilder();
            sb3.append(front);
            sb3.append(String.valueOf((char) value));
            sb3.append(rear);
            return sb3.toString();
        } else {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("invalid value = ");
            sb4.append(value);
            XmlUtil.log_e(CLASS_NAME, "setLocalTestNV", sb4.toString());
            return null;
        }
    }

    public static byte getLocalTestNV(String source, int key) {
        byte b = NV_VALUE_FAIL;
        if (source == null || source.isEmpty() || source.length() != 2000) {
            StringBuilder sb = new StringBuilder();
            sb.append("invalid source = ");
            sb.append(source);
            XmlUtil.log_e(CLASS_NAME, "getLocalTestNV", sb.toString());
            return NV_VALUE_FAIL;
        } else if (key <= 0 || key >= 500) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("invalid key = ");
            sb2.append(key);
            XmlUtil.log_e(CLASS_NAME, "getLocalTestNV", sb2.toString());
            return NV_VALUE_FAIL;
        } else {
            String value = source.substring(((key - 1) * 4) + 3, ((key - 1) * 4) + 4);
            if (value != null) {
                try {
                    if (value.length() > 0) {
                        b = (byte) value.charAt(0);
                    }
                } catch (NumberFormatException ne) {
                    XmlUtil.log_e(ne);
                    return NV_VALUE_FAIL;
                }
            }
            return b;
        }
    }
}
