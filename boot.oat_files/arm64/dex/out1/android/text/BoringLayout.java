package android.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Path;
import android.text.Layout.Alignment;
import android.text.Layout.Directions;
import android.text.TextUtils.EllipsizeCallback;
import android.text.TextUtils.TruncateAt;
import android.text.style.ParagraphStyle;

public class BoringLayout extends Layout implements EllipsizeCallback {
    private static final char FIRST_RIGHT_TO_LEFT = '֐';
    private static final TextPaint sTemp = new TextPaint();
    int mBottom;
    private int mBottomPadding;
    int mDesc;
    private String mDirect;
    private int mEllipsizedCount;
    private int mEllipsizedStart;
    private int mEllipsizedWidth;
    private float mMax;
    private Paint mPaint;
    private int mTopPadding;

    public static class Metrics extends FontMetricsInt {
        public int width;

        public String toString() {
            return super.toString() + " width=" + this.width;
        }
    }

    public static BoringLayout make(CharSequence source, TextPaint paint, int outerwidth, Alignment align, float spacingmult, float spacingadd, Metrics metrics, boolean includepad) {
        return new BoringLayout(source, paint, outerwidth, align, spacingmult, spacingadd, metrics, includepad);
    }

    public static BoringLayout make(CharSequence source, TextPaint paint, int outerwidth, Alignment align, float spacingmult, float spacingadd, Metrics metrics, boolean includepad, TruncateAt ellipsize, int ellipsizedWidth) {
        return new BoringLayout(source, paint, outerwidth, align, spacingmult, spacingadd, metrics, includepad, ellipsize, ellipsizedWidth);
    }

    public BoringLayout replaceOrMake(CharSequence source, TextPaint paint, int outerwidth, Alignment align, float spacingmult, float spacingadd, Metrics metrics, boolean includepad) {
        replaceWith(source, paint, outerwidth, align, spacingmult, spacingadd);
        this.mEllipsizedWidth = outerwidth;
        this.mEllipsizedStart = 0;
        this.mEllipsizedCount = 0;
        init(source, paint, outerwidth, align, spacingmult, spacingadd, metrics, includepad, true);
        return this;
    }

    public BoringLayout replaceOrMake(CharSequence source, TextPaint paint, int outerwidth, Alignment align, float spacingmult, float spacingadd, Metrics metrics, boolean includepad, TruncateAt ellipsize, int ellipsizedWidth) {
        boolean trust;
        if (ellipsize == null || ellipsize == TruncateAt.MARQUEE) {
            replaceWith(source, paint, outerwidth, align, spacingmult, spacingadd);
            this.mEllipsizedWidth = outerwidth;
            this.mEllipsizedStart = 0;
            this.mEllipsizedCount = 0;
            trust = true;
        } else {
            replaceWith(TextUtils.ellipsize(source, paint, (float) ellipsizedWidth, ellipsize, true, this), paint, outerwidth, align, spacingmult, spacingadd);
            this.mEllipsizedWidth = ellipsizedWidth;
            trust = false;
        }
        init(getText(), paint, outerwidth, align, spacingmult, spacingadd, metrics, includepad, trust);
        return this;
    }

    public BoringLayout(CharSequence source, TextPaint paint, int outerwidth, Alignment align, float spacingmult, float spacingadd, Metrics metrics, boolean includepad) {
        super(source, paint, outerwidth, align, spacingmult, spacingadd);
        this.mEllipsizedWidth = outerwidth;
        this.mEllipsizedStart = 0;
        this.mEllipsizedCount = 0;
        init(source, paint, outerwidth, align, spacingmult, spacingadd, metrics, includepad, true);
    }

    public BoringLayout(CharSequence source, TextPaint paint, int outerwidth, Alignment align, float spacingmult, float spacingadd, Metrics metrics, boolean includepad, TruncateAt ellipsize, int ellipsizedWidth) {
        boolean trust;
        super(source, paint, outerwidth, align, spacingmult, spacingadd);
        if (ellipsize == null || ellipsize == TruncateAt.MARQUEE) {
            this.mEllipsizedWidth = outerwidth;
            this.mEllipsizedStart = 0;
            this.mEllipsizedCount = 0;
            trust = true;
        } else {
            replaceWith(TextUtils.ellipsize(source, paint, (float) ellipsizedWidth, ellipsize, true, this), paint, outerwidth, align, spacingmult, spacingadd);
            this.mEllipsizedWidth = ellipsizedWidth;
            trust = false;
        }
        init(getText(), paint, outerwidth, align, spacingmult, spacingadd, metrics, includepad, trust);
    }

    void init(CharSequence source, TextPaint paint, int outerwidth, Alignment align, float spacingmult, float spacingadd, Metrics metrics, boolean includepad, boolean trustWidth) {
        int spacing;
        if ((source instanceof String) && align == Alignment.ALIGN_NORMAL) {
            this.mDirect = source.toString();
        } else {
            this.mDirect = null;
        }
        this.mPaint = paint;
        if (includepad) {
            spacing = metrics.bottom - metrics.top;
        } else {
            spacing = metrics.descent - metrics.ascent;
        }
        this.mBottom = spacing;
        if (includepad) {
            this.mDesc = metrics.top + spacing;
        } else {
            this.mDesc = metrics.ascent + spacing;
        }
        if (trustWidth) {
            this.mMax = (float) metrics.width;
        } else {
            TextLine line = TextLine.obtain();
            line.set(paint, source, 0, source.length(), 1, Layout.DIRS_ALL_LEFT_TO_RIGHT, false, null);
            this.mMax = (float) ((int) Math.ceil((double) line.metrics(null)));
            TextLine.recycle(line);
        }
        if (includepad) {
            this.mTopPadding = metrics.top - metrics.ascent;
            this.mBottomPadding = metrics.bottom - metrics.descent;
        }
    }

    public static Metrics isBoring(CharSequence text, TextPaint paint) {
        return isBoring(text, paint, TextDirectionHeuristics.FIRSTSTRONG_LTR, null);
    }

    public static Metrics isBoring(CharSequence text, TextPaint paint, TextDirectionHeuristic textDir) {
        return isBoring(text, paint, textDir, null);
    }

    public static Metrics isBoring(CharSequence text, TextPaint paint, Metrics metrics) {
        return isBoring(text, paint, TextDirectionHeuristics.FIRSTSTRONG_LTR, metrics);
    }

    public static Metrics isBoring(CharSequence text, TextPaint paint, TextDirectionHeuristic textDir, Metrics metrics) {
        char[] temp = TextUtils.obtain(500);
        int length = text.length();
        boolean boring = true;
        loop0:
        for (int i = 0; i < length; i += 500) {
            int j = i + 500;
            if (j > length) {
                j = length;
            }
            TextUtils.getChars(text, i, j, temp, 0);
            int n = j - i;
            for (int a = 0; a < n; a++) {
                char c = temp[a];
                if (c == '\n' || c == '\t' || ((c >= FIRST_RIGHT_TO_LEFT && c <= 'ࣿ') || c == '‏' || ((c >= '‪' && c <= '‮') || ((c >= '⁦' && c <= '⁩') || ((c >= '?' && c <= '?') || ((c >= 'יִ' && c <= '﷿') || (c >= 'ﹰ' && c <= '﻾'))))))) {
                    boring = false;
                    break loop0;
                }
            }
            if (textDir != null && textDir.isRtl(temp, 0, n)) {
                boring = false;
                break;
            }
        }
        TextUtils.recycle(temp);
        if (boring && (text instanceof Spanned)) {
            if (((Spanned) text).getSpans(0, length, ParagraphStyle.class).length > 0) {
                boring = false;
            }
        }
        if (!boring) {
            return null;
        }
        FontMetricsInt fm = metrics;
        if (fm == null) {
            fm = new Metrics();
        }
        paint.set(paint);
        TextLine line = TextLine.obtain();
        line.set(paint, text, 0, length, 1, Layout.DIRS_ALL_LEFT_TO_RIGHT, false, null);
        fm.width = (int) Math.ceil((double) line.metrics(fm));
        TextLine.recycle(line);
        return fm;
    }

    public int getHeight() {
        return this.mBottom;
    }

    public int getLineCount() {
        return 1;
    }

    public int getLineTop(int line) {
        if (line == 0) {
            return 0;
        }
        return this.mBottom;
    }

    public int getLineDescent(int line) {
        return this.mDesc;
    }

    public int getLineStart(int line) {
        if (line == 0) {
            return 0;
        }
        return getText().length();
    }

    public int getParagraphDirection(int line) {
        return 1;
    }

    public boolean getLineContainsTab(int line) {
        return false;
    }

    public float getLineMax(int line) {
        return this.mMax;
    }

    public final Directions getLineDirections(int line) {
        return Layout.DIRS_ALL_LEFT_TO_RIGHT;
    }

    public int getTopPadding() {
        return this.mTopPadding;
    }

    public int getBottomPadding() {
        return this.mBottomPadding;
    }

    public int getEllipsisCount(int line) {
        return this.mEllipsizedCount;
    }

    public int getEllipsisStart(int line) {
        return this.mEllipsizedStart;
    }

    public int getEllipsizedWidth() {
        return this.mEllipsizedWidth;
    }

    public void draw(Canvas c, Path highlight, Paint highlightpaint, int cursorOffset) {
        if (this.mDirect == null || highlight != null) {
            super.draw(c, highlight, highlightpaint, cursorOffset);
        } else {
            c.drawText(this.mDirect, 0.0f, (float) (this.mBottom - this.mDesc), this.mPaint);
        }
    }

    public void ellipsized(int start, int end) {
        this.mEllipsizedStart = start;
        this.mEllipsizedCount = end - start;
    }
}
