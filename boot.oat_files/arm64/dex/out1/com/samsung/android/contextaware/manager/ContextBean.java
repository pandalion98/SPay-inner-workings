package com.samsung.android.contextaware.manager;

import android.os.Bundle;

public class ContextBean {
    private Bundle mContextBundle;
    private Bundle mContextBundleForDisplay;

    protected ContextBean() {
        clearContextBean();
    }

    protected final Bundle getContextBundle() {
        return this.mContextBundle;
    }

    public final Bundle getContextBundleForDisplay() {
        return this.mContextBundleForDisplay;
    }

    public final void putContext(String key, boolean value) {
        this.mContextBundle.putBoolean(key, value);
        putContextForDisplay(key, Boolean.toString(value));
    }

    public final void putContext(String key, int value) {
        this.mContextBundle.putInt(key, value);
        putContextForDisplay(key, Integer.toString(value));
    }

    public final void putContext(String key, float value) {
        this.mContextBundle.putFloat(key, value);
        putContextForDisplay(key, Float.toString(value));
    }

    public final void putContext(String key, double value) {
        this.mContextBundle.putDouble(key, value);
        putContextForDisplay(key, Double.toString(value));
    }

    public final void putContext(String key, long value) {
        this.mContextBundle.putLong(key, value);
        putContextForDisplay(key, Long.toString(value));
    }

    public final void putContext(String key, short value) {
        this.mContextBundle.putShort(key, value);
        putContextForDisplay(key, Short.toString(value));
    }

    public final void putContext(String key, String value) {
        this.mContextBundle.putString(key, value);
        putContextForDisplay(key, value);
    }

    public final void putContext(String key, Bundle bundle) {
        this.mContextBundle.putBundle(key, bundle);
    }

    public final void putContext(String key, boolean[] value) {
        if (value != null && value.length > 0) {
            this.mContextBundle.putBooleanArray(key, value);
            String[] strArr = new String[value.length];
            for (int i = 0; i < value.length; i++) {
                strArr[i] = Boolean.toString(value[i]);
            }
            putContextForDisplay(key, strArr);
        }
    }

    public final void putContext(String key, int[] value) {
        if (value != null && value.length >= 0) {
            this.mContextBundle.putIntArray(key, value);
            String[] strArr = new String[value.length];
            for (int i = 0; i < value.length; i++) {
                strArr[i] = Integer.toString(value[i]);
            }
            putContextForDisplay(key, strArr);
        }
    }

    public final void putContext(String key, float[] value) {
        if (value != null && value.length > 0) {
            this.mContextBundle.putFloatArray(key, value);
            String[] strArr = new String[value.length];
            for (int i = 0; i < value.length; i++) {
                strArr[i] = Float.toString(value[i]);
            }
            putContextForDisplay(key, strArr);
        }
    }

    public final void putContext(String key, double[] value) {
        if (value != null && value.length >= 0) {
            this.mContextBundle.putDoubleArray(key, value);
            String[] strArr = new String[value.length];
            for (int i = 0; i < value.length; i++) {
                strArr[i] = Double.toString(value[i]);
            }
            putContextForDisplay(key, strArr);
        }
    }

    public final void putContext(String key, long[] value) {
        if (value != null && value.length >= 0) {
            this.mContextBundle.putLongArray(key, value);
            String[] strArr = new String[value.length];
            for (int i = 0; i < value.length; i++) {
                strArr[i] = Long.toString(value[i]);
            }
            putContextForDisplay(key, strArr);
        }
    }

    protected final void clearContextBean() {
        this.mContextBundle = new Bundle();
        this.mContextBundleForDisplay = new Bundle();
    }

    private void putContextForDisplay(String key, String[] value) {
        if (value != null && value.length > 0) {
            this.mContextBundleForDisplay.putStringArray(key, value);
        }
    }

    public final void putContextForDisplay(String key, boolean value) {
        putContextForDisplay(key, Boolean.toString(value));
    }

    public final void putContextForDisplay(String key, int value) {
        putContextForDisplay(key, Integer.toString(value));
    }

    public final void putContextForDisplay(String key, float value) {
        putContextForDisplay(key, Float.toString(value));
    }

    public final void putContextForDisplay(String key, double value) {
        putContextForDisplay(key, Double.toString(value));
    }

    public final void putContextForDisplay(String key, long value) {
        putContextForDisplay(key, Long.toString(value));
    }

    public final void putContextForDisplay(String key, short value) {
        putContextForDisplay(key, Short.toString(value));
    }

    private void putContextForDisplay(String key, String value) {
        if (value != null && !value.isEmpty()) {
            this.mContextBundleForDisplay.putString(key, value);
        }
    }

    public final void putContextForDisplay(String key, boolean[] value) {
        if (value != null && value.length > 0) {
            String[] strArr = new String[value.length];
            for (int i = 0; i < value.length; i++) {
                strArr[i] = Boolean.toString(value[i]);
            }
            putContextForDisplay(key, strArr);
        }
    }

    public final void putContextForDisplay(String key, int[] value) {
        if (value != null && value.length > 0) {
            String[] strArr = new String[value.length];
            for (int i = 0; i < value.length; i++) {
                strArr[i] = Integer.toString(value[i]);
            }
            putContextForDisplay(key, strArr);
        }
    }

    public final void putContextForDisplay(String key, float[] value) {
        if (value != null && value.length > 0) {
            String[] strArr = new String[value.length];
            for (int i = 0; i < value.length; i++) {
                strArr[i] = Float.toString(value[i]);
            }
            putContextForDisplay(key, strArr);
        }
    }

    public final void putContextForDisplay(String key, double[] value) {
        if (value != null && value.length > 0) {
            String[] strArr = new String[value.length];
            for (int i = 0; i < value.length; i++) {
                strArr[i] = Double.toString(value[i]);
            }
            putContextForDisplay(key, strArr);
        }
    }

    public final void putContextForDisplay(String key, long[] value) {
        if (value != null && value.length > 0) {
            String[] strArr = new String[value.length];
            for (int i = 0; i < value.length; i++) {
                strArr[i] = Long.toString(value[i]);
            }
            putContextForDisplay(key, strArr);
        }
    }
}
