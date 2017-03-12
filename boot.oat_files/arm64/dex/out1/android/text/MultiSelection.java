package android.text;

import android.graphics.Paint;

public class MultiSelection {
    public static final Object CURRENT_SELECTION_END = new END();
    public static final Object CURRENT_SELECTION_START = new START();
    private static int mHoveredIcon = -1;
    private static boolean mIsSelecting = false;
    private static boolean mIsTextViewHovered = false;
    private static boolean mNeedToScroll = false;

    private static final class END implements NoCopySpan {
        private END() {
        }
    }

    private static final class START implements NoCopySpan {
        private START() {
        }
    }

    private MultiSelection() {
    }

    public static final int getSelectionStart(CharSequence text) {
        if (text instanceof Spanned) {
            return ((Spanned) text).getSpanStart(CURRENT_SELECTION_START);
        }
        return -1;
    }

    public static final int getSelectionEnd(CharSequence text) {
        if (text instanceof Spanned) {
            return ((Spanned) text).getSpanStart(CURRENT_SELECTION_END);
        }
        return -1;
    }

    public static void setSelection(Spannable text, int start, int stop) {
        if (start != stop && start >= 0 && stop >= 0) {
            int ostart = getSelectionStart(text);
            int oend = getSelectionEnd(text);
            int len = text.length();
            if ((start > 0 && start < len) || (stop > 0 && stop < len)) {
                boolean needCheckPosition = false;
                if (start > 0 && start < len) {
                    char startChar = text.charAt(start);
                    if (Character.isLowSurrogate(startChar)) {
                        start++;
                    } else if (TextUtils.isIndianChar(startChar) || TextUtils.isThaiChar(startChar) || TextUtils.isKhmerChar(startChar) || TextUtils.isMyanmarChar(startChar) || TextUtils.isLaoChar(startChar)) {
                        needCheckPosition = true;
                    }
                }
                if (stop > 0 && stop < len) {
                    char stopChar = text.charAt(stop);
                    if (Character.isLowSurrogate(stopChar)) {
                        stop++;
                    } else if (!needCheckPosition && (TextUtils.isIndianChar(stopChar) || TextUtils.isThaiChar(stopChar) || TextUtils.isKhmerChar(stopChar) || TextUtils.isMyanmarChar(stopChar) || TextUtils.isLaoChar(stopChar))) {
                        needCheckPosition = true;
                    }
                }
                if (needCheckPosition) {
                    float[] widths = new float[len];
                    char[] chars = new char[len];
                    Paint p = new Paint(1);
                    TextUtils.getChars(text, 0, len, chars, 0);
                    p.getTextRunAdvances(chars, 0, len, 0, len, false, widths, 0);
                    while (start < len && widths[start] == 0.0f && chars[start] != '\n') {
                        start++;
                    }
                    while (stop < len && widths[stop] == 0.0f && chars[stop] != '\n') {
                        stop++;
                    }
                }
            }
            if (ostart != start || oend != stop) {
                START[] startSpans = (START[]) text.getSpans(0, text.length(), START.class);
                END[] endSpans = (END[]) text.getSpans(0, text.length(), END.class);
                for (int i = 0; i < startSpans.length; i++) {
                    int starts = text.getSpanStart(startSpans[i]);
                    int ends = text.getSpanStart(endSpans[i]);
                    if ((starts <= start && start < ends) || (starts < stop && stop <= ends)) {
                        text.removeSpan(startSpans[i]);
                        text.removeSpan(endSpans[i]);
                    }
                }
                text.setSpan(CURRENT_SELECTION_START, start, start, 546);
                text.setSpan(CURRENT_SELECTION_END, stop, stop, 34);
            }
        }
    }

    public static final void removeCurSelection(Spannable text) {
        text.removeSpan(CURRENT_SELECTION_START);
        text.removeSpan(CURRENT_SELECTION_END);
    }

    public static final void selectAll(Spannable text) {
        setSelection(text, 0, text.length());
    }

    public static final void addMultiSelection(Spannable text, int start, int stop) {
        if (start >= 0 && stop >= 0) {
            START penStart = new START();
            END penEnd = new END();
            text.setSpan(penStart, start, start, 546);
            text.setSpan(penEnd, stop, stop, 34);
        }
    }

    public static final boolean removeMultiSelection(Spannable text, int start, int stop) {
        START[] spansStarts = (START[]) text.getSpans(start, start, START.class);
        END[] spansEnds = (END[]) text.getSpans(stop, stop, END.class);
        boolean ret = true;
        if (spansStarts.length == 1) {
            text.removeSpan(spansStarts[0]);
        } else {
            ret = false;
        }
        if (spansEnds.length != 1) {
            return false;
        }
        text.removeSpan(spansEnds[0]);
        return ret;
    }

    public static final void clearMultiSelection(Spannable text) {
        START[] spansStarts = (START[]) text.getSpans(0, text.length(), START.class);
        END[] spansEnds = (END[]) text.getSpans(0, text.length(), END.class);
        for (int i = 0; i < spansStarts.length; i++) {
            text.removeSpan(spansStarts[i]);
            text.removeSpan(spansEnds[i]);
        }
    }

    public static final int[] getMultiSelectionStart(Spannable text) {
        START[] spans = (START[]) text.getSpans(0, text.length(), START.class);
        int[] starts = new int[spans.length];
        for (int i = 0; i < spans.length; i++) {
            starts[i] = text.getSpanStart(spans[i]);
        }
        return starts;
    }

    public static final int[] getMultiSelectionEnd(Spannable text) {
        END[] spans = (END[]) text.getSpans(0, text.length(), END.class);
        int[] ends = new int[spans.length];
        for (int i = 0; i < spans.length; i++) {
            ends[i] = text.getSpanStart(spans[i]);
        }
        return ends;
    }

    public static final int getMultiSelectionCount(Spannable text) {
        return ((START[]) text.getSpans(0, text.length(), START.class)).length;
    }

    public static final void setIsMultiSelectingText(boolean bSelecting) {
        mIsSelecting = bSelecting;
    }

    public static final boolean getIsMultiSelectingText() {
        return mIsSelecting;
    }

    public static final void setTextViewHovered(boolean bSelecting) {
        setTextViewHovered(bSelecting, -1);
    }

    public static final void setTextViewHovered(boolean bSelecting, int type) {
        mIsTextViewHovered = bSelecting;
        mHoveredIcon = type;
    }

    public static final boolean isTextViewHovered() {
        return mIsTextViewHovered;
    }

    public static final int getHoveredIcon() {
        return mHoveredIcon;
    }

    public static final void setNeedToScroll(boolean bflag) {
        mNeedToScroll = bflag;
    }

    public static final boolean isNeedToScroll() {
        return mNeedToScroll;
    }
}
