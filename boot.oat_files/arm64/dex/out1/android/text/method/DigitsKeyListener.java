package android.text.method;

import android.text.SpannableStringBuilder;
import android.text.Spanned;

public class DigitsKeyListener extends NumberKeyListener {
    private static final char[][] CHARACTERS = new char[][]{new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}, new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '+'}, new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'}, new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '+', '.'}};
    private static final int DECIMAL = 2;
    private static final int SIGN = 1;
    private static DigitsKeyListener[] sInstance = new DigitsKeyListener[4];
    private char[] mAccepted;
    private boolean mDecimal;
    private boolean mSign;

    protected char[] getAcceptedChars() {
        return this.mAccepted;
    }

    private static boolean isSignChar(char c) {
        return c == '-' || c == '+';
    }

    private static boolean isDecimalPointChar(char c) {
        return c == '.';
    }

    public DigitsKeyListener() {
        this(false, false);
    }

    public DigitsKeyListener(boolean sign, boolean decimal) {
        int i;
        int i2 = 0;
        this.mSign = sign;
        this.mDecimal = decimal;
        if (sign) {
            i = 1;
        } else {
            i = 0;
        }
        if (decimal) {
            i2 = 2;
        }
        this.mAccepted = CHARACTERS[i | i2];
    }

    public static DigitsKeyListener getInstance() {
        return getInstance(false, false);
    }

    public static DigitsKeyListener getInstance(boolean sign, boolean decimal) {
        int i;
        int i2 = 0;
        if (sign) {
            i = 1;
        } else {
            i = 0;
        }
        if (decimal) {
            i2 = 2;
        }
        int kind = i | i2;
        if (sInstance[kind] != null) {
            return sInstance[kind];
        }
        sInstance[kind] = new DigitsKeyListener(sign, decimal);
        return sInstance[kind];
    }

    public static DigitsKeyListener getInstance(String accepted) {
        DigitsKeyListener dim = new DigitsKeyListener();
        dim.mAccepted = new char[accepted.length()];
        accepted.getChars(0, accepted.length(), dim.mAccepted, 0);
        return dim;
    }

    public int getInputType() {
        int contentType = 2;
        if (this.mSign) {
            contentType = 2 | 4096;
        }
        if (this.mDecimal) {
            return contentType | 8192;
        }
        return contentType;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        CharSequence out = super.filter(source, start, end, dest, dstart, dend);
        if (!this.mSign && !this.mDecimal) {
            return out;
        }
        int i;
        if (out != null) {
            source = out;
            start = 0;
            end = out.length();
        }
        int sign = -1;
        int decimal = -1;
        int dlen = dest.length();
        for (i = 0; i < dstart; i++) {
            char c = dest.charAt(i);
            if (isSignChar(c)) {
                sign = i;
            } else if (isDecimalPointChar(c)) {
                decimal = i;
            }
        }
        for (i = dend; i < dlen; i++) {
            c = dest.charAt(i);
            if (isSignChar(c)) {
                return "";
            }
            if (isDecimalPointChar(c)) {
                decimal = i;
            }
        }
        SpannableStringBuilder stripped = null;
        for (i = end - 1; i >= start; i--) {
            c = source.charAt(i);
            boolean strip = false;
            if (isSignChar(c)) {
                if (i != start || dstart != 0) {
                    strip = true;
                } else if (sign >= 0) {
                    strip = true;
                } else {
                    sign = i;
                }
            } else if (isDecimalPointChar(c)) {
                if (decimal >= 0) {
                    strip = true;
                } else {
                    decimal = i;
                }
            }
            if (strip) {
                if (end == start + 1) {
                    return "";
                }
                if (stripped == null) {
                    stripped = new SpannableStringBuilder(source, start, end);
                }
                stripped.delete(i - start, (i + 1) - start);
            }
        }
        if (stripped != null) {
            return stripped;
        }
        return out == null ? null : out;
    }
}
