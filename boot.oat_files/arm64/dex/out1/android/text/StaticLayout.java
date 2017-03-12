package android.text;

import android.graphics.Bitmap;
import android.graphics.Paint.FontMetricsInt;
import android.text.Layout.Alignment;
import android.text.Layout.Directions;
import android.text.TextUtils.TruncateAt;
import android.text.style.LeadingMarginSpan;
import android.text.style.LeadingMarginSpan.LeadingMarginSpan2;
import android.text.style.LineHeightSpan;
import android.text.style.LineHeightSpan.WithDensity;
import android.text.style.MetricAffectingSpan;
import android.text.style.TabStopSpan;
import android.util.Log;
import android.util.Pools.SynchronizedPool;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Locale;

public class StaticLayout extends Layout {
    private static final char CHAR_FIRST_CJK = '⺀';
    private static final int CHAR_FIRST_HIGH_SURROGATE = 55296;
    private static final char CHAR_HYPHEN = '-';
    private static final int CHAR_LAST_LOW_SURROGATE = 57343;
    private static final char CHAR_NEW_LINE = '\n';
    private static final char CHAR_SLASH = '/';
    private static final char CHAR_SPACE = ' ';
    private static final char CHAR_TAB = '\t';
    private static final char CHAR_ZWS = '​';
    private static final int CHN_LineBreak = 3;
    private static final int COLUMNS_ELLIPSIZE = 6;
    private static final int COLUMNS_NORMAL = 4;
    private static final int DESCENT = 2;
    private static final int DIR = 0;
    private static final int DIR_SHIFT = 30;
    private static final int ELLIPSIS_COUNT = 5;
    private static final int ELLIPSIS_START = 4;
    private static final double EXTRA_ROUNDING = 0.5d;
    private static final int HYPHEN = 3;
    private static final int JPN_LineBreak = 2;
    private static final int KOR_LineBreak = 1;
    private static final int MYM_LineBreak = 4;
    private static final int START = 0;
    private static final int START_MASK = 536870911;
    private static final int TAB = 0;
    private static final int TAB_INCREMENT = 20;
    private static final int TAB_MASK = 536870912;
    static final String TAG = "StaticLayout";
    private static final int TOP = 1;
    private int mBottomPadding;
    private int mCJKLineBreak;
    private int mColumns;
    private int mEllipsizedWidth;
    private int[] mLeftIndents;
    private int mLineCount;
    private Directions[] mLineDirections;
    private int[] mLines;
    private int mMaximumVisibleLineCount;
    private int[] mRightIndents;
    private int mTopPadding;

    public static final class Builder {
        private static final SynchronizedPool<Builder> sPool = new SynchronizedPool(3);
        Alignment mAlignment;
        int mBreakStrategy;
        TruncateAt mEllipsize;
        int mEllipsizedWidth;
        int mEnd;
        FontMetricsInt mFontMetricsInt = new FontMetricsInt();
        int mHyphenationFrequency;
        boolean mIncludePad;
        int[] mLeftIndents;
        Locale mLocale;
        int mMaxLines;
        MeasuredText mMeasuredText;
        long mNativePtr = StaticLayout.nNewBuilder();
        TextPaint mPaint;
        int[] mRightIndents;
        float mSpacingAdd;
        float mSpacingMult;
        int mStart;
        CharSequence mText;
        TextDirectionHeuristic mTextDir;
        int mWidth;

        private Builder() {
        }

        public static Builder obtain(CharSequence source, int start, int end, TextPaint paint, int width) {
            Builder b = (Builder) sPool.acquire();
            if (b == null) {
                b = new Builder();
            }
            b.mText = source;
            b.mStart = start;
            b.mEnd = end;
            b.mPaint = paint;
            b.mWidth = width;
            b.mAlignment = Alignment.ALIGN_NORMAL;
            b.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
            b.mSpacingMult = 1.0f;
            b.mSpacingAdd = 0.0f;
            b.mIncludePad = true;
            b.mEllipsizedWidth = width;
            b.mEllipsize = null;
            b.mMaxLines = Integer.MAX_VALUE;
            b.mBreakStrategy = 0;
            b.mHyphenationFrequency = 0;
            b.mMeasuredText = MeasuredText.obtain();
            return b;
        }

        private static void recycle(Builder b) {
            b.mPaint = null;
            b.mText = null;
            MeasuredText.recycle(b.mMeasuredText);
            b.mMeasuredText = null;
            b.mLeftIndents = null;
            b.mRightIndents = null;
            StaticLayout.nFinishBuilder(b.mNativePtr);
            sPool.release(b);
        }

        void finish() {
            StaticLayout.nFinishBuilder(this.mNativePtr);
            this.mText = null;
            this.mPaint = null;
            this.mLeftIndents = null;
            this.mRightIndents = null;
            this.mMeasuredText.finish();
        }

        public Builder setText(CharSequence source) {
            return setText(source, 0, source.length());
        }

        public Builder setText(CharSequence source, int start, int end) {
            this.mText = source;
            this.mStart = start;
            this.mEnd = end;
            return this;
        }

        public Builder setPaint(TextPaint paint) {
            this.mPaint = paint;
            return this;
        }

        public Builder setWidth(int width) {
            this.mWidth = width;
            if (this.mEllipsize == null) {
                this.mEllipsizedWidth = width;
            }
            return this;
        }

        public Builder setAlignment(Alignment alignment) {
            this.mAlignment = alignment;
            return this;
        }

        public Builder setTextDirection(TextDirectionHeuristic textDir) {
            this.mTextDir = textDir;
            return this;
        }

        public Builder setLineSpacing(float spacingAdd, float spacingMult) {
            this.mSpacingAdd = spacingAdd;
            this.mSpacingMult = spacingMult;
            return this;
        }

        public Builder setIncludePad(boolean includePad) {
            this.mIncludePad = includePad;
            return this;
        }

        public Builder setEllipsizedWidth(int ellipsizedWidth) {
            this.mEllipsizedWidth = ellipsizedWidth;
            return this;
        }

        public Builder setEllipsize(TruncateAt ellipsize) {
            this.mEllipsize = ellipsize;
            return this;
        }

        public Builder setMaxLines(int maxLines) {
            this.mMaxLines = maxLines;
            return this;
        }

        public Builder setBreakStrategy(int breakStrategy) {
            this.mBreakStrategy = breakStrategy;
            return this;
        }

        public Builder setHyphenationFrequency(int hyphenationFrequency) {
            this.mHyphenationFrequency = hyphenationFrequency;
            return this;
        }

        public Builder setIndents(int[] leftIndents, int[] rightIndents) {
            this.mLeftIndents = leftIndents;
            this.mRightIndents = rightIndents;
            int leftLen = leftIndents == null ? 0 : leftIndents.length;
            int rightLen = rightIndents == null ? 0 : rightIndents.length;
            int[] indents = new int[Math.max(leftLen, rightLen)];
            for (int i = 0; i < indents.length; i++) {
                int leftMargin;
                int rightMargin;
                if (i < leftLen) {
                    leftMargin = leftIndents[i];
                } else {
                    leftMargin = 0;
                }
                if (i < rightLen) {
                    rightMargin = rightIndents[i];
                } else {
                    rightMargin = 0;
                }
                indents[i] = leftMargin + rightMargin;
            }
            StaticLayout.nSetIndents(this.mNativePtr, indents);
            return this;
        }

        private void setLocale(Locale locale) {
            if (!locale.equals(this.mLocale)) {
                StaticLayout.nSetLocale(this.mNativePtr, locale.toLanguageTag(), Hyphenator.get(locale).getNativePtr());
                this.mLocale = locale;
            }
        }

        float addStyleRun(TextPaint paint, int start, int end, boolean isRtl) {
            return StaticLayout.nAddStyleRun(this.mNativePtr, paint.getNativeInstance(), paint.mNativeTypeface, start, end, isRtl);
        }

        void addMeasuredRun(int start, int end, float[] widths) {
            StaticLayout.nAddMeasuredRun(this.mNativePtr, start, end, widths);
        }

        void addReplacementRun(int start, int end, float width) {
            StaticLayout.nAddReplacementRun(this.mNativePtr, start, end, width);
        }

        public StaticLayout build() {
            StaticLayout result = new StaticLayout();
            recycle(this);
            return result;
        }

        protected void finalize() throws Throwable {
            try {
                StaticLayout.nFreeBuilder(this.mNativePtr);
            } finally {
                super.finalize();
            }
        }
    }

    static class LineBreaks {
        private static final int INITIAL_SIZE = 16;
        public int[] breaks = new int[16];
        public int[] flags = new int[16];
        public float[] widths = new float[16];

        LineBreaks() {
        }
    }

    private static native void nAddMeasuredRun(long j, int i, int i2, float[] fArr);

    private static native void nAddReplacementRun(long j, int i, int i2, float f);

    private static native float nAddStyleRun(long j, long j2, long j3, int i, int i2, boolean z);

    private static native int nComputeLineBreaks(long j, LineBreaks lineBreaks, int[] iArr, float[] fArr, int[] iArr2, int i);

    private static native void nFinishBuilder(long j);

    private static native void nFreeBuilder(long j);

    private static native void nGetWidths(long j, float[] fArr);

    static native long nLoadHyphenator(ByteBuffer byteBuffer, int i);

    private static native long nNewBuilder();

    private static native void nSetIndents(long j, int[] iArr);

    private static native void nSetLocale(long j, String str, long j2);

    private static native void nSetupParagraph(long j, char[] cArr, int i, float f, int i2, float f2, int[] iArr, int i3, int i4, int i5);

    public StaticLayout(CharSequence source, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(source, 0, source.length(), paint, width, align, spacingmult, spacingadd, includepad);
    }

    public StaticLayout(CharSequence source, TextPaint paint, int width, Alignment align, TextDirectionHeuristic textDir, float spacingmult, float spacingadd, boolean includepad) {
        this(source, 0, source.length(), paint, width, align, textDir, spacingmult, spacingadd, includepad);
    }

    public StaticLayout(CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(source, bufstart, bufend, paint, outerwidth, align, spacingmult, spacingadd, includepad, null, 0);
    }

    public StaticLayout(CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Alignment align, TextDirectionHeuristic textDir, float spacingmult, float spacingadd, boolean includepad) {
        this(source, bufstart, bufend, paint, outerwidth, align, textDir, spacingmult, spacingadd, includepad, null, 0, Integer.MAX_VALUE);
    }

    public StaticLayout(CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Alignment align, float spacingmult, float spacingadd, boolean includepad, TruncateAt ellipsize, int ellipsizedWidth) {
        this(source, bufstart, bufend, paint, outerwidth, align, TextDirectionHeuristics.FIRSTSTRONG_LTR, spacingmult, spacingadd, includepad, ellipsize, ellipsizedWidth, Integer.MAX_VALUE);
    }

    public StaticLayout(CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Alignment align, TextDirectionHeuristic textDir, float spacingmult, float spacingadd, boolean includepad, TruncateAt ellipsize, int ellipsizedWidth, int maxLines) {
        CharSequence spannedEllipsizer = ellipsize == null ? source : source instanceof Spanned ? new SpannedEllipsizer(source) : new Ellipsizer(source);
        super(spannedEllipsizer, paint, outerwidth, align, textDir, spacingmult, spacingadd);
        this.mCJKLineBreak = 0;
        this.mMaximumVisibleLineCount = Integer.MAX_VALUE;
        Builder b = Builder.obtain(source, bufstart, bufend, paint, outerwidth).setAlignment(align).setTextDirection(textDir).setLineSpacing(spacingadd, spacingmult).setIncludePad(includepad).setEllipsizedWidth(ellipsizedWidth).setEllipsize(ellipsize).setMaxLines(maxLines);
        if (ellipsize != null) {
            Ellipsizer e = (Ellipsizer) getText();
            e.mLayout = this;
            e.mWidth = ellipsizedWidth;
            e.mMethod = ellipsize;
            this.mEllipsizedWidth = ellipsizedWidth;
            this.mColumns = 6;
        } else {
            this.mColumns = 4;
            this.mEllipsizedWidth = outerwidth;
        }
        this.mLineDirections = (Directions[]) ArrayUtils.newUnpaddedArray(Directions.class, this.mColumns * 2);
        this.mLines = new int[this.mLineDirections.length];
        this.mMaximumVisibleLineCount = maxLines;
        generate(b, b.mIncludePad, b.mIncludePad);
        Builder.recycle(b);
    }

    StaticLayout(CharSequence text) {
        super(text, null, 0, null, 0.0f, 0.0f);
        this.mCJKLineBreak = 0;
        this.mMaximumVisibleLineCount = Integer.MAX_VALUE;
        this.mColumns = 6;
        this.mLineDirections = (Directions[]) ArrayUtils.newUnpaddedArray(Directions.class, this.mColumns * 2);
        this.mLines = new int[this.mLineDirections.length];
    }

    private StaticLayout(Builder b) {
        CharSequence spannedEllipsizer = b.mEllipsize == null ? b.mText : b.mText instanceof Spanned ? new SpannedEllipsizer(b.mText) : new Ellipsizer(b.mText);
        super(spannedEllipsizer, b.mPaint, b.mWidth, b.mAlignment, b.mSpacingMult, b.mSpacingAdd);
        this.mCJKLineBreak = 0;
        this.mMaximumVisibleLineCount = Integer.MAX_VALUE;
        if (b.mEllipsize != null) {
            Ellipsizer e = (Ellipsizer) getText();
            e.mLayout = this;
            e.mWidth = b.mEllipsizedWidth;
            e.mMethod = b.mEllipsize;
            this.mEllipsizedWidth = b.mEllipsizedWidth;
            this.mColumns = 6;
        } else {
            this.mColumns = 4;
            this.mEllipsizedWidth = b.mWidth;
        }
        this.mLineDirections = (Directions[]) ArrayUtils.newUnpaddedArray(Directions.class, this.mColumns * 2);
        this.mLines = new int[this.mLineDirections.length];
        this.mMaximumVisibleLineCount = b.mMaxLines;
        this.mLeftIndents = b.mLeftIndents;
        this.mRightIndents = b.mRightIndents;
        generate(b, b.mIncludePad, b.mIncludePad);
    }

    void generate(Builder b, boolean includepad, boolean trackpad) {
        CharSequence source = b.mText;
        int bufStart = b.mStart;
        int bufEnd = b.mEnd;
        TextPaint paint = b.mPaint;
        int outerWidth = b.mWidth;
        TextDirectionHeuristic textDir = b.mTextDir;
        float spacingmult = b.mSpacingMult;
        float spacingadd = b.mSpacingAdd;
        float ellipsizedWidth = (float) b.mEllipsizedWidth;
        TruncateAt ellipsize = b.mEllipsize;
        LineBreaks lineBreaks = new LineBreaks();
        int[] spanEndCache = new int[4];
        int[] fmCache = new int[16];
        b.setLocale(paint.getTextLocale());
        this.mCJKLineBreak = 0;
        if (b.mLocale != null) {
            if ("ko".equals(b.mLocale.getLanguage())) {
                this.mCJKLineBreak = 1;
            } else if ("ja".equals(b.mLocale.getLanguage())) {
                this.mCJKLineBreak = 2;
            } else if ("my".equals(b.mLocale.getLanguage()) || "ZG".equals(b.mLocale.getCountry())) {
                this.mCJKLineBreak = 4;
            }
        }
        if (this.mCJKLineBreak > 0) {
            generateForCJK(b, includepad, trackpad);
            return;
        }
        this.mLineCount = 0;
        int v = 0;
        boolean needMultiply = (spacingmult == 1.0f && spacingadd == 0.0f) ? false : true;
        FontMetricsInt fm = b.mFontMetricsInt;
        int[] chooseHtv = null;
        MeasuredText measured = b.mMeasuredText;
        Spanned spanned = null;
        if (source instanceof Spanned) {
            spanned = (Spanned) source;
        }
        int paraStart = bufStart;
        while (paraStart <= bufEnd) {
            int i;
            int spanEnd;
            int paraEnd = TextUtils.indexOf(source, (char) CHAR_NEW_LINE, paraStart, bufEnd);
            if (paraEnd < 0) {
                paraEnd = bufEnd;
            } else {
                paraEnd++;
            }
            int firstWidthLineCount = 1;
            int firstWidth = outerWidth;
            int restWidth = outerWidth;
            LineHeightSpan[] chooseHt = null;
            if (spanned != null) {
                LeadingMarginSpan[] sp = (LeadingMarginSpan[]) Layout.getParagraphSpans(spanned, paraStart, paraEnd, LeadingMarginSpan.class);
                for (i = 0; i < sp.length; i++) {
                    LeadingMarginSpan lms = sp[i];
                    firstWidth -= sp[i].getLeadingMargin(true);
                    restWidth -= sp[i].getLeadingMargin(false);
                    if (lms instanceof LeadingMarginSpan2) {
                        firstWidthLineCount = Math.max(firstWidthLineCount, ((LeadingMarginSpan2) lms).getLeadingMarginLineCount());
                    }
                }
                chooseHt = (LineHeightSpan[]) Layout.getParagraphSpans(spanned, paraStart, paraEnd, LineHeightSpan.class);
                if (chooseHt.length == 0) {
                    chooseHt = null;
                } else {
                    if (chooseHtv == null || chooseHtv.length < chooseHt.length) {
                        chooseHtv = ArrayUtils.newUnpaddedIntArray(chooseHt.length);
                    }
                    for (i = 0; i < chooseHt.length; i++) {
                        int o = spanned.getSpanStart(chooseHt[i]);
                        if (o < paraStart) {
                            chooseHtv[i] = getLineTop(getLineForOffset(o));
                        } else {
                            chooseHtv[i] = v;
                        }
                    }
                }
            }
            measured.setPara(source, paraStart, paraEnd, textDir, b);
            char[] chs = measured.mChars;
            float[] widths = measured.mWidths;
            byte[] chdirs = measured.mLevels;
            int dir = measured.mDir;
            boolean easy = measured.mEasy;
            int[] variableTabStops = null;
            if (spanned != null) {
                TabStopSpan[] spans = (TabStopSpan[]) Layout.getParagraphSpans(spanned, paraStart, paraEnd, TabStopSpan.class);
                if (spans.length > 0) {
                    int[] stops = new int[spans.length];
                    for (i = 0; i < spans.length; i++) {
                        stops[i] = spans[i].getTabStop();
                    }
                    Arrays.sort(stops, 0, stops.length);
                    variableTabStops = stops;
                }
            }
            nSetupParagraph(b.mNativePtr, chs, paraEnd - paraStart, (float) firstWidth, firstWidthLineCount, (float) restWidth, variableTabStops, 20, b.mBreakStrategy, b.mHyphenationFrequency);
            if (!(this.mLeftIndents == null && this.mRightIndents == null)) {
                int leftLen = this.mLeftIndents == null ? 0 : this.mLeftIndents.length;
                int rightLen = this.mRightIndents == null ? 0 : this.mRightIndents.length;
                int indentsLen = Math.max(1, Math.min(leftLen, rightLen) - this.mLineCount);
                int[] indents = new int[indentsLen];
                for (i = 0; i < indentsLen; i++) {
                    indents[i] = (this.mLeftIndents == null ? 0 : this.mLeftIndents[Math.min(this.mLineCount + i, leftLen - 1)]) + (this.mRightIndents == null ? 0 : this.mRightIndents[Math.min(this.mLineCount + i, rightLen - 1)]);
                }
                nSetIndents(b.mNativePtr, indents);
            }
            int fmCacheCount = 0;
            int spanEndCacheCount = 0;
            int spanStart = paraStart;
            while (spanStart < paraEnd) {
                if (fmCacheCount * 4 >= fmCache.length) {
                    int[] grow = new int[((fmCacheCount * 4) * 2)];
                    System.arraycopy(fmCache, 0, grow, 0, fmCacheCount * 4);
                    fmCache = grow;
                }
                if (spanEndCacheCount >= spanEndCache.length) {
                    grow = new int[(spanEndCacheCount * 2)];
                    System.arraycopy(spanEndCache, 0, grow, 0, spanEndCacheCount);
                    spanEndCache = grow;
                }
                if (spanned == null) {
                    spanEnd = paraEnd;
                    measured.addStyleRun(paint, spanEnd - spanStart, fm);
                } else {
                    spanEnd = spanned.nextSpanTransition(spanStart, paraEnd, MetricAffectingSpan.class);
                    measured.addStyleRun(paint, (MetricAffectingSpan[]) TextUtils.removeEmptySpans((MetricAffectingSpan[]) spanned.getSpans(spanStart, spanEnd, MetricAffectingSpan.class), spanned, MetricAffectingSpan.class), spanEnd - spanStart, fm);
                }
                fmCache[(fmCacheCount * 4) + 0] = fm.top;
                fmCache[(fmCacheCount * 4) + 1] = fm.bottom;
                fmCache[(fmCacheCount * 4) + 2] = fm.ascent;
                fmCache[(fmCacheCount * 4) + 3] = fm.descent;
                fmCacheCount++;
                spanEndCache[spanEndCacheCount] = spanEnd;
                spanEndCacheCount++;
                spanStart = spanEnd;
            }
            nGetWidths(b.mNativePtr, widths);
            int breakCount = nComputeLineBreaks(b.mNativePtr, lineBreaks, lineBreaks.breaks, lineBreaks.widths, lineBreaks.flags, lineBreaks.breaks.length);
            int[] breaks = lineBreaks.breaks;
            float[] lineWidths = lineBreaks.widths;
            int[] flags = lineBreaks.flags;
            int remainingLineCount = this.mMaximumVisibleLineCount - this.mLineCount;
            boolean ellipsisMayBeApplied = ellipsize != null && (ellipsize == TruncateAt.END || (this.mMaximumVisibleLineCount == 1 && ellipsize != TruncateAt.MARQUEE));
            if (remainingLineCount > 0 && remainingLineCount < breakCount && ellipsisMayBeApplied) {
                float width = 0.0f;
                int flag = 0;
                i = remainingLineCount - 1;
                while (i < breakCount) {
                    width += lineWidths[i];
                    if (chs[breaks[i] - 1] == CHAR_SPACE && i < breakCount - 1) {
                        width += widths[breaks[i] - 1];
                    }
                    flag |= flags[i] & 536870912;
                    i++;
                }
                lineWidths[remainingLineCount - 1] = width;
                flags[remainingLineCount - 1] = flag;
                breaks[remainingLineCount - 1] = breaks[breakCount - 1];
                breakCount = remainingLineCount;
            }
            int here = paraStart;
            int fmTop = 0;
            int fmBottom = 0;
            int fmAscent = 0;
            int fmDescent = 0;
            int fmCacheIndex = 0;
            int breakIndex = 0;
            spanStart = paraStart;
            int spanEndCacheIndex = 0;
            while (spanStart < paraEnd && spanEndCacheIndex < spanEndCache.length) {
                int spanEndCacheIndex2 = spanEndCacheIndex + 1;
                spanEnd = spanEndCache[spanEndCacheIndex];
                fm.top = fmCache[(fmCacheIndex * 4) + 0];
                fm.bottom = fmCache[(fmCacheIndex * 4) + 1];
                fm.ascent = fmCache[(fmCacheIndex * 4) + 2];
                fm.descent = fmCache[(fmCacheIndex * 4) + 3];
                fmCacheIndex++;
                if (fm.top < fmTop) {
                    fmTop = fm.top;
                }
                if (fm.ascent < fmAscent) {
                    fmAscent = fm.ascent;
                }
                if (fm.descent > fmDescent) {
                    fmDescent = fm.descent;
                }
                if (fm.bottom > fmBottom) {
                    fmBottom = fm.bottom;
                }
                while (breakIndex < breakCount && breaks[breakIndex] + paraStart < spanStart) {
                    breakIndex++;
                }
                while (breakIndex < breakCount && breaks[breakIndex] + paraStart <= spanEnd) {
                    int endPos = paraStart + breaks[breakIndex];
                    boolean moreChars = endPos < bufEnd;
                    float elipsizeWidth_span = ellipsizedWidth;
                    if (breakIndex == 0 && firstWidth != outerWidth) {
                        elipsizeWidth_span = ellipsizedWidth - ((float) (outerWidth - firstWidth));
                    } else if (!(breakIndex == 0 || restWidth == outerWidth)) {
                        elipsizeWidth_span = ellipsizedWidth - ((float) (outerWidth - restWidth));
                    }
                    if (elipsizeWidth_span < 0.0f) {
                        elipsizeWidth_span = ellipsizedWidth;
                    }
                    v = out(source, here, endPos, fmAscent, fmDescent, fmTop, fmBottom, v, spacingmult, spacingadd, chooseHt, chooseHtv, fm, flags[breakIndex], needMultiply, chdirs, dir, easy, bufEnd, includepad, trackpad, chs, widths, paraStart, ellipsize, elipsizeWidth_span, lineWidths[breakIndex], paint, moreChars);
                    if (endPos < spanEnd) {
                        fmTop = fm.top;
                        fmBottom = fm.bottom;
                        fmAscent = fm.ascent;
                        fmDescent = fm.descent;
                    } else {
                        fmDescent = 0;
                        fmAscent = 0;
                        fmBottom = 0;
                        fmTop = 0;
                    }
                    here = endPos;
                    breakIndex++;
                    if (this.mLineCount >= this.mMaximumVisibleLineCount) {
                        return;
                    }
                }
                spanStart = spanEnd;
                spanEndCacheIndex = spanEndCacheIndex2;
            }
            if (paraEnd == bufEnd) {
                break;
            }
            paraStart = paraEnd;
        }
        if ((bufEnd == bufStart || source.charAt(bufEnd - 1) == CHAR_NEW_LINE) && this.mLineCount < this.mMaximumVisibleLineCount) {
            measured.setPara(source, bufEnd, bufEnd, textDir, b);
            paint.getFontMetricsInt(fm);
            v = out(source, bufEnd, bufEnd, fm.ascent, fm.descent, fm.top, fm.bottom, v, spacingmult, spacingadd, null, null, fm, 0, needMultiply, measured.mLevels, measured.mDir, measured.mEasy, bufEnd, includepad, trackpad, null, null, bufStart, ellipsize, ellipsizedWidth, 0.0f, paint, false);
        }
    }

    private void generateForCJK(Builder b, boolean includepad, boolean trackpad) {
        CharSequence source = b.mText;
        int bufStart = b.mStart;
        int bufEnd = b.mEnd;
        TextPaint paint = b.mPaint;
        int outerWidth = b.mWidth;
        TextDirectionHeuristic textDir = b.mTextDir;
        float spacingmult = b.mSpacingMult;
        float spacingadd = b.mSpacingAdd;
        float ellipsizedWidth = (float) b.mEllipsizedWidth;
        TruncateAt ellipsize = b.mEllipsize;
        int[] spanEndCache = new int[4];
        int[] fmCache = new int[16];
        this.mLineCount = 0;
        int v = 0;
        boolean needMultiply = (spacingmult == 1.0f && spacingadd == 0.0f) ? false : true;
        FontMetricsInt fm = b.mFontMetricsInt;
        int[] chooseHtv = null;
        MeasuredText measured = b.mMeasuredText;
        Spanned spanned = null;
        if (source instanceof Spanned) {
            spanned = (Spanned) source;
        }
        int paraStart = bufStart;
        while (paraStart <= bufEnd) {
            int i;
            TabStopSpan[] spans;
            int spanEnd;
            int paraEnd = TextUtils.indexOf(source, (char) CHAR_NEW_LINE, paraStart, bufEnd);
            if (paraEnd < 0) {
                paraEnd = bufEnd;
            } else {
                paraEnd++;
            }
            int i2 = this.mLineCount + 1;
            int firstWidthLineCount = 1;
            int firstWidth = outerWidth;
            int restWidth = outerWidth;
            LineHeightSpan[] chooseHt = null;
            if (spanned != null) {
                LeadingMarginSpan[] sp = (LeadingMarginSpan[]) Layout.getParagraphSpans(spanned, paraStart, paraEnd, LeadingMarginSpan.class);
                for (i = 0; i < sp.length; i++) {
                    LeadingMarginSpan lms = sp[i];
                    firstWidth -= sp[i].getLeadingMargin(true);
                    restWidth -= sp[i].getLeadingMargin(false);
                    if (lms instanceof LeadingMarginSpan2) {
                        LeadingMarginSpan2 lms2 = (LeadingMarginSpan2) lms;
                        i2 = Math.max(i2, lms2.getLeadingMarginLineCount() + getLineForOffset(spanned.getSpanStart(lms2)));
                        firstWidthLineCount = Math.max(firstWidthLineCount, lms2.getLeadingMarginLineCount());
                    }
                }
                chooseHt = (LineHeightSpan[]) Layout.getParagraphSpans(spanned, paraStart, paraEnd, LineHeightSpan.class);
                if (chooseHt.length != 0) {
                    if (chooseHtv == null || chooseHtv.length < chooseHt.length) {
                        chooseHtv = ArrayUtils.newUnpaddedIntArray(chooseHt.length);
                    }
                    for (i = 0; i < chooseHt.length; i++) {
                        int o = spanned.getSpanStart(chooseHt[i]);
                        if (o < paraStart) {
                            chooseHtv[i] = getLineTop(getLineForOffset(o));
                        } else {
                            chooseHtv[i] = v;
                        }
                    }
                }
            }
            measured.setPara(source, paraStart, paraEnd, textDir, null);
            char[] chs = measured.mChars;
            float[] widths = measured.mWidths;
            byte[] chdirs = measured.mLevels;
            int dir = measured.mDir;
            boolean easy = measured.mEasy;
            int[] variableTabStops = null;
            if (spanned != null) {
                spans = (TabStopSpan[]) Layout.getParagraphSpans(spanned, paraStart, paraEnd, TabStopSpan.class);
                if (spans.length > 0) {
                    int[] stops = new int[spans.length];
                    for (i = 0; i < spans.length; i++) {
                        stops[i] = spans[i].getTabStop();
                    }
                    Arrays.sort(stops, 0, stops.length);
                    variableTabStops = stops;
                }
            }
            nSetupParagraph(b.mNativePtr, chs, paraEnd - paraStart, (float) firstWidth, firstWidthLineCount, (float) restWidth, variableTabStops, 20, b.mBreakStrategy, b.mHyphenationFrequency);
            if (!(this.mLeftIndents == null && this.mRightIndents == null)) {
                int leftLen = this.mLeftIndents == null ? 0 : this.mLeftIndents.length;
                int rightLen = this.mRightIndents == null ? 0 : this.mRightIndents.length;
                int indentsLen = Math.max(1, Math.min(leftLen, rightLen) - this.mLineCount);
                int[] indents = new int[indentsLen];
                for (i = 0; i < indentsLen; i++) {
                    indents[i] = (this.mLeftIndents == null ? 0 : this.mLeftIndents[Math.min(this.mLineCount + i, leftLen - 1)]) + (this.mRightIndents == null ? 0 : this.mRightIndents[Math.min(this.mLineCount + i, rightLen - 1)]);
                }
                nSetIndents(b.mNativePtr, indents);
            }
            int fmCacheCount = 0;
            int spanEndCacheCount = 0;
            int spanStart = paraStart;
            while (spanStart < paraEnd) {
                if (fmCacheCount * 4 >= fmCache.length) {
                    int[] grow = new int[((fmCacheCount * 4) * 2)];
                    System.arraycopy(fmCache, 0, grow, 0, fmCacheCount * 4);
                    fmCache = grow;
                }
                if (spanEndCacheCount >= spanEndCache.length) {
                    grow = new int[(spanEndCacheCount * 2)];
                    System.arraycopy(spanEndCache, 0, grow, 0, spanEndCacheCount);
                    spanEndCache = grow;
                }
                if (spanned == null) {
                    spanEnd = paraEnd;
                    measured.addStyleRun(paint, spanEnd - spanStart, fm);
                } else {
                    spanEnd = spanned.nextSpanTransition(spanStart, paraEnd, MetricAffectingSpan.class);
                    measured.addStyleRun(paint, (MetricAffectingSpan[]) TextUtils.removeEmptySpans((MetricAffectingSpan[]) spanned.getSpans(spanStart, spanEnd, MetricAffectingSpan.class), spanned, MetricAffectingSpan.class), spanEnd - spanStart, fm);
                }
                fmCache[(fmCacheCount * 4) + 0] = fm.top;
                fmCache[(fmCacheCount * 4) + 1] = fm.bottom;
                fmCache[(fmCacheCount * 4) + 2] = fm.ascent;
                fmCache[(fmCacheCount * 4) + 3] = fm.descent;
                fmCacheCount++;
                spanEndCache[spanEndCacheCount] = spanEnd;
                spanEndCacheCount++;
                spanStart = spanEnd;
            }
            int width = firstWidth;
            float w = 0.0f;
            int here = paraStart;
            int ok = paraStart;
            float okWidth = 0.0f;
            int okAscent = 0;
            int okDescent = 0;
            int okTop = 0;
            int okBottom = 0;
            int fit = paraStart;
            float fitWidth = 0.0f;
            int fitAscent = 0;
            int fitDescent = 0;
            int fitTop = 0;
            int fitBottom = 0;
            float fitWidthGraphing = 0.0f;
            int hasTabOrEmoji = 0;
            TabStops tabStops = null;
            int fmTop = 0;
            int fmBottom = 0;
            int fmAscent = 0;
            int fmDescent = 0;
            int fmCacheIndex = 0;
            spanStart = paraStart;
            int spanEndCacheIndex = 0;
            while (spanStart < paraEnd && spanEndCacheIndex < spanEndCache.length) {
                int spanEndCacheIndex2 = spanEndCacheIndex + 1;
                spanEnd = spanEndCache[spanEndCacheIndex];
                fm.top = fmCache[(fmCacheIndex * 4) + 0];
                fm.bottom = fmCache[(fmCacheIndex * 4) + 1];
                fm.ascent = fmCache[(fmCacheIndex * 4) + 2];
                fm.descent = fmCache[(fmCacheIndex * 4) + 3];
                fmCacheIndex++;
                if (fm.top < fmTop) {
                    fmTop = fm.top;
                }
                if (fm.ascent < fmAscent) {
                    fmAscent = fm.ascent;
                }
                if (fm.descent > fmDescent) {
                    fmDescent = fm.descent;
                }
                if (fm.bottom > fmBottom) {
                    fmBottom = fm.bottom;
                }
                boolean bZerowithspace = false;
                int j = spanStart;
                while (j < spanEnd) {
                    char c = chs[j - paraStart];
                    if (c != '\n') {
                        if (c == '\t') {
                            if ((536870912 & hasTabOrEmoji) == 0) {
                                hasTabOrEmoji |= 536870912;
                                if (spanned != null) {
                                    spans = (TabStopSpan[]) Layout.getParagraphSpans(spanned, paraStart, paraEnd, TabStopSpan.class);
                                    if (spans.length > 0) {
                                        TabStops tabStops2 = new TabStops(20, spans);
                                    }
                                }
                            }
                            if (tabStops != null) {
                                w = tabStops.nextTab(w);
                            } else {
                                w = TabStops.nextDefaultStop(w, 20);
                            }
                        } else if (c < CHAR_FIRST_HIGH_SURROGATE || c > CHAR_LAST_LOW_SURROGATE || j + 1 >= spanEnd) {
                            w += widths[j - paraStart];
                        } else {
                            int emoji = Character.codePointAt(chs, j - paraStart);
                            if (emoji < MIN_EMOJI || emoji > MAX_EMOJI) {
                                w += widths[j - paraStart];
                            } else {
                                Bitmap bm = EMOJI_FACTORY.getBitmapFromAndroidPua(emoji);
                                if (bm != null) {
                                    w += (((float) bm.getWidth()) * (-paint.ascent())) / ((float) bm.getHeight());
                                    hasTabOrEmoji |= 1;
                                    j++;
                                } else {
                                    w += widths[j - paraStart];
                                }
                            }
                        }
                    }
                    boolean isSpaceOrTab = c == ' ' || c == '\t' || c == '​';
                    if (w <= ((float) width) || isSpaceOrTab) {
                        boolean isLineBreak;
                        fitWidth = w;
                        if (!isSpaceOrTab) {
                            fitWidthGraphing = w;
                        }
                        fit = j + 1;
                        if (fmTop < fitTop) {
                            fitTop = fmTop;
                        }
                        if (fmAscent < fitAscent) {
                            fitAscent = fmAscent;
                        }
                        if (fmDescent > fitDescent) {
                            fitDescent = fmDescent;
                        }
                        if (fmBottom > fitBottom) {
                            fitBottom = fmBottom;
                        }
                        if (c == '​') {
                            bZerowithspace = true;
                        }
                        if (this.mCJKLineBreak != 2) {
                            isLineBreak = isSpaceOrTab || (((c == '/' || c == '-') && (j + 1 >= spanEnd || !Character.isDigit(chs[(j + 1) - paraStart]))) || (c >= '⺀' && isIdeographic(c, true) && j + 1 < spanEnd && isIdeographic(chs[(j + 1) - paraStart], false)));
                        } else if (isSpaceOrTab || (((c == '/' || c == '-') && (j + 1 >= spanEnd || !Character.isDigit(chs[(j + 1) - paraStart]))) || ((!bZerowithspace && c >= '⺀' && isIdeographic(c, true) && j + 1 < spanEnd && isIdeographic(chs[(j + 1) - paraStart], false)) || (!bZerowithspace && c >= '⺀' && isIdeographic(c, true) && j + 1 < spanEnd && !isIdeographic(chs[(j + 1) - paraStart], true))))) {
                            isLineBreak = true;
                        } else {
                            isLineBreak = false;
                        }
                        if (isLineBreak) {
                            okWidth = fitWidthGraphing;
                            ok = j + 1;
                            if (fitTop < okTop) {
                                okTop = fitTop;
                            }
                            if (fitAscent < okAscent) {
                                okAscent = fitAscent;
                            }
                            if (fitDescent > okDescent) {
                                okDescent = fitDescent;
                            }
                            if (fitBottom > okBottom) {
                                okBottom = fitBottom;
                            }
                        }
                    } else {
                        int endPos;
                        int above;
                        int below;
                        int top;
                        int bottom;
                        float currentTextWidth;
                        if (ok != here) {
                            endPos = ok;
                            above = okAscent;
                            below = okDescent;
                            top = okTop;
                            bottom = okBottom;
                            currentTextWidth = okWidth;
                        } else if (fit != here) {
                            endPos = fit;
                            above = fitAscent;
                            below = fitDescent;
                            top = fitTop;
                            bottom = fitBottom;
                            currentTextWidth = fitWidth;
                        } else {
                            endPos = here + 1;
                            while (endPos < spanEnd && widths[endPos - paraStart] == 0.0f) {
                                endPos++;
                            }
                            above = fmAscent;
                            below = fmDescent;
                            top = fmTop;
                            bottom = fmBottom;
                            currentTextWidth = widths[here - paraStart];
                        }
                        if (c == '\n') {
                            endPos++;
                        }
                        int ellipseEnd = endPos;
                        if (this.mMaximumVisibleLineCount == 1 && ellipsize == TruncateAt.MIDDLE) {
                            ellipseEnd = paraEnd;
                        }
                        v = out(source, here, ellipseEnd, above, below, top, bottom, v, spacingmult, spacingadd, chooseHt, chooseHtv, fm, hasTabOrEmoji, needMultiply, chdirs, dir, easy, bufEnd, includepad, trackpad, chs, widths, paraStart, ellipsize, ellipsizedWidth, currentTextWidth, paint, true);
                        if (endPos < spanEnd) {
                            fmTop = fm.top;
                            fmBottom = fm.bottom;
                            fmAscent = fm.ascent;
                            fmDescent = fm.descent;
                        } else {
                            fmDescent = 0;
                            fmAscent = 0;
                            fmBottom = 0;
                            fmTop = 0;
                        }
                        here = endPos;
                        j = here - 1;
                        fit = here;
                        ok = here;
                        w = 0.0f;
                        fitWidthGraphing = 0.0f;
                        fitBottom = 0;
                        fitTop = 0;
                        fitDescent = 0;
                        fitAscent = 0;
                        okBottom = 0;
                        okTop = 0;
                        okDescent = 0;
                        okAscent = 0;
                        hasTabOrEmoji = 0;
                        i2--;
                        if (i2 <= 0) {
                            width = restWidth;
                        }
                        if (here < spanStart) {
                            spanEnd = here;
                            spanEndCacheIndex2--;
                            fmCacheIndex--;
                            break;
                        } else if (this.mLineCount >= this.mMaximumVisibleLineCount) {
                            return;
                        }
                    }
                    j++;
                }
                spanStart = spanEnd;
                spanEndCacheIndex = spanEndCacheIndex2;
            }
            if (paraEnd != here && this.mLineCount < this.mMaximumVisibleLineCount) {
                boolean z;
                if ((((fitTop | fitBottom) | fitDescent) | fitAscent) == 0) {
                    paint.getFontMetricsInt(fm);
                    fitTop = fm.top;
                    fitBottom = fm.bottom;
                    fitAscent = fm.ascent;
                    fitDescent = fm.descent;
                }
                if (paraEnd != bufEnd) {
                    z = true;
                } else {
                    z = false;
                }
                v = out(source, here, paraEnd, fitAscent, fitDescent, fitTop, fitBottom, v, spacingmult, spacingadd, chooseHt, chooseHtv, fm, hasTabOrEmoji, needMultiply, chdirs, dir, easy, bufEnd, includepad, trackpad, chs, widths, paraStart, ellipsize, ellipsizedWidth, w, paint, z);
            }
            paraStart = paraEnd;
            if (paraEnd == bufEnd || this.mLineCount >= this.mMaximumVisibleLineCount) {
                break;
            }
            paraStart = paraEnd;
        }
        if ((bufEnd == bufStart || source.charAt(bufEnd - 1) == CHAR_NEW_LINE) && this.mLineCount < this.mMaximumVisibleLineCount) {
            measured.setPara(source, bufStart, bufEnd, textDir, null);
            paint.getFontMetricsInt(fm);
            v = out(source, bufEnd, bufEnd, fm.ascent, fm.descent, fm.top, fm.bottom, v, spacingmult, spacingadd, null, null, fm, 0, needMultiply, measured.mLevels, measured.mDir, measured.mEasy, bufEnd, includepad, trackpad, null, null, bufStart, ellipsize, ellipsizedWidth, 0.0f, paint, false);
        }
    }

    private int out(CharSequence text, int start, int end, int above, int below, int top, int bottom, int v, float spacingmult, float spacingadd, LineHeightSpan[] chooseHt, int[] chooseHtv, FontMetricsInt fm, int flags, boolean needMultiply, byte[] chdirs, int dir, boolean easy, int bufEnd, boolean includePad, boolean trackPad, char[] chs, float[] widths, int widthStart, TruncateAt ellipsize, float ellipsisWidth, float textWidth, TextPaint paint, boolean moreChars) {
        int extra;
        int j = this.mLineCount;
        int off = j * this.mColumns;
        int want = (this.mColumns + off) + 1;
        int[] lines = this.mLines;
        if (want >= lines.length) {
            Object grow2 = (Directions[]) ArrayUtils.newUnpaddedArray(Directions.class, GrowingArrayUtils.growSize(want));
            System.arraycopy(this.mLineDirections, 0, grow2, 0, this.mLineDirections.length);
            this.mLineDirections = grow2;
            int[] grow = new int[grow2.length];
            System.arraycopy(lines, 0, grow, 0, lines.length);
            this.mLines = grow;
            lines = grow;
        }
        if (chooseHt != null) {
            fm.ascent = above;
            fm.descent = below;
            fm.top = top;
            fm.bottom = bottom;
            for (int i = 0; i < chooseHt.length; i++) {
                if (chooseHt[i] instanceof WithDensity) {
                    ((WithDensity) chooseHt[i]).chooseHeight(text, start, end, chooseHtv[i], v, fm, paint);
                } else {
                    chooseHt[i].chooseHeight(text, start, end, chooseHtv[i], v, fm);
                }
            }
            above = fm.ascent;
            below = fm.descent;
            top = fm.top;
            bottom = fm.bottom;
        }
        boolean firstLine = j == 0;
        boolean currentLineIsTheLastVisibleOne = j + 1 == this.mMaximumVisibleLineCount;
        boolean lastLine = currentLineIsTheLastVisibleOne || end == bufEnd;
        if (firstLine) {
            if (trackPad) {
                this.mTopPadding = top - above;
            }
            if (includePad) {
                above = top;
            }
        }
        if (lastLine) {
            if (trackPad) {
                this.mBottomPadding = bottom - below;
            }
            if (includePad) {
                below = bottom;
            }
        }
        if (!needMultiply || lastLine) {
            extra = 0;
        } else {
            double ex = (double) ((((float) (below - above)) * (spacingmult - 1.0f)) + spacingadd);
            if (ex >= 0.0d) {
                extra = (int) (EXTRA_ROUNDING + ex);
            } else {
                extra = -((int) ((-ex) + EXTRA_ROUNDING));
            }
        }
        lines[off + 0] = start;
        lines[off + 1] = v;
        lines[off + 2] = below + extra;
        v += (below - above) + extra;
        lines[(this.mColumns + off) + 0] = end;
        lines[(this.mColumns + off) + 1] = v;
        int i2 = off + 0;
        lines[i2] = lines[i2] | (536870912 & flags);
        lines[off + 3] = flags;
        i2 = off + 0;
        lines[i2] = lines[i2] | (dir << 30);
        Directions linedirs = DIRS_ALL_LEFT_TO_RIGHT;
        if (easy) {
            this.mLineDirections[j] = linedirs;
        } else {
            this.mLineDirections[j] = AndroidBidi.directions(dir, chdirs, start - widthStart, chs, start - widthStart, end - start);
        }
        if (ellipsize != null) {
            boolean forceEllipsis = moreChars && this.mLineCount + 1 == this.mMaximumVisibleLineCount;
            boolean doEllipsis = (((this.mMaximumVisibleLineCount == 1 && moreChars) || (firstLine && !moreChars)) && ellipsize != TruncateAt.MARQUEE) || (!firstLine && ((currentLineIsTheLastVisibleOne || !moreChars) && ellipsize == TruncateAt.END));
            if (doEllipsis) {
                calculateEllipsis(start, end, widths, widthStart, ellipsisWidth, ellipsize, j, textWidth, paint, forceEllipsis, chs);
            }
        }
        this.mLineCount++;
        return v;
    }

    private void calculateEllipsis(int lineStart, int lineEnd, float[] widths, int widthStart, float avail, TruncateAt where, int line, float textWidth, TextPaint paint, boolean forceEllipsis, char[] chs) {
        if (textWidth > avail || forceEllipsis) {
            float ellipsisWidth = paint.measureText(where == TruncateAt.END_SMALL ? TextUtils.ELLIPSIS_TWO_DOTS : TextUtils.ELLIPSIS_NORMAL, 0, 1);
            int ellipsisStart = 0;
            int ellipsisCount = 0;
            int len = lineEnd - lineStart;
            float sum;
            int rightForZeroWidth;
            int i;
            float w;
            if (where == TruncateAt.START) {
                if (this.mMaximumVisibleLineCount == 1) {
                    sum = 0.0f;
                    rightForZeroWidth = len;
                    i = len;
                    while (i >= 0 && (i != 0 || lineStart != widthStart)) {
                        w = widths[((i - 1) + lineStart) - widthStart];
                        if ((w + sum) + ellipsisWidth > avail) {
                            break;
                        }
                        if (w != 0.0f) {
                            rightForZeroWidth = i - 1;
                        }
                        sum += w;
                        i--;
                    }
                    if (i != 0) {
                        i = rightForZeroWidth;
                    }
                    ellipsisStart = 0;
                    ellipsisCount = i;
                } else if (Log.isLoggable(TAG, 5)) {
                    Log.w(TAG, "Start Ellipsis only supported with one line");
                }
            } else if (where == TruncateAt.END || where == TruncateAt.MARQUEE || where == TruncateAt.END_SMALL) {
                sum = 0.0f;
                i = 0;
                while (i < len) {
                    w = widths[(i + lineStart) - widthStart];
                    if ((w + sum) + ellipsisWidth > avail) {
                        break;
                    }
                    sum += w;
                    i++;
                }
                while (chs != null && i > 0 && i < len && TextUtils.isArabicChar(chs[i])) {
                    if (paint.measureText(new String(chs, 0, i)) + ellipsisWidth <= avail) {
                        break;
                    }
                    i--;
                }
                ellipsisStart = i;
                ellipsisCount = len - i;
                if (forceEllipsis && ellipsisCount == 0 && len > 0) {
                    ellipsisStart = len - 1;
                    while (ellipsisStart > 0 && widths[(ellipsisStart + lineStart) - widthStart] == 0.0f && chs[(ellipsisStart + lineStart) - widthStart] != '\n') {
                        ellipsisStart--;
                    }
                    ellipsisCount = len - ellipsisStart;
                }
            } else if (this.mMaximumVisibleLineCount == 1) {
                float lsum = 0.0f;
                float rsum = 0.0f;
                rightForZeroWidth = len;
                float ravail = (avail - ellipsisWidth) / 2.0f;
                int right = len;
                while (right > 0) {
                    w = widths[((right - 1) + lineStart) - widthStart];
                    if (w + rsum > ravail) {
                        break;
                    }
                    if (w != 0.0f) {
                        rightForZeroWidth = right - 1;
                    }
                    rsum += w;
                    right--;
                }
                if (right != 0) {
                    right = rightForZeroWidth;
                }
                float lavail = (avail - ellipsisWidth) - rsum;
                int left = 0;
                while (left < right) {
                    w = widths[(left + lineStart) - widthStart];
                    if (w + lsum > lavail) {
                        break;
                    }
                    lsum += w;
                    left++;
                }
                ellipsisStart = left;
                ellipsisCount = right - left;
            } else if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Middle Ellipsis only supported with one line");
            }
            this.mLines[(this.mColumns * line) + 4] = ellipsisStart;
            this.mLines[(this.mColumns * line) + 5] = ellipsisCount;
            return;
        }
        this.mLines[(this.mColumns * line) + 4] = 0;
        this.mLines[(this.mColumns * line) + 5] = 0;
    }

    public int getLineForVertical(int vertical) {
        int high = this.mLineCount;
        int low = -1;
        int[] lines = this.mLines;
        while (high - low > 1) {
            int guess = (high + low) >> 1;
            if (lines[(this.mColumns * guess) + 1] > vertical) {
                high = guess;
            } else {
                low = guess;
            }
        }
        if (low < 0) {
            return 0;
        }
        return low;
    }

    public int getLineCount() {
        return this.mLineCount;
    }

    public int getLineTop(int line) {
        int top = this.mLines[(this.mColumns * line) + 1];
        if (this.mMaximumVisibleLineCount <= 0 || line < this.mMaximumVisibleLineCount || line == this.mLineCount) {
            return top;
        }
        return top + getBottomPadding();
    }

    public int getLineDescent(int line) {
        int descent = this.mLines[(this.mColumns * line) + 2];
        if (this.mMaximumVisibleLineCount <= 0 || line < this.mMaximumVisibleLineCount - 1 || line == this.mLineCount) {
            return descent;
        }
        return descent + getBottomPadding();
    }

    public int getLineStart(int line) {
        return this.mLines[(this.mColumns * line) + 0] & START_MASK;
    }

    public int getParagraphDirection(int line) {
        return this.mLines[(this.mColumns * line) + 0] >> 30;
    }

    public boolean getLineContainsTab(int line) {
        return (this.mLines[(this.mColumns * line) + 0] & 536870912) != 0;
    }

    public final Directions getLineDirections(int line) {
        return this.mLineDirections[line];
    }

    public int getTopPadding() {
        return this.mTopPadding;
    }

    public int getBottomPadding() {
        return this.mBottomPadding;
    }

    public int getHyphen(int line) {
        return this.mLines[(this.mColumns * line) + 3] & 255;
    }

    public int getIndentAdjust(int line, Alignment align) {
        if (align == Alignment.ALIGN_LEFT) {
            if (this.mLeftIndents == null) {
                return 0;
            }
            return this.mLeftIndents[Math.min(line, this.mLeftIndents.length - 1)];
        } else if (align == Alignment.ALIGN_RIGHT) {
            if (this.mRightIndents != null) {
                return -this.mRightIndents[Math.min(line, this.mRightIndents.length - 1)];
            }
            return 0;
        } else if (align == Alignment.ALIGN_CENTER) {
            int left = 0;
            if (this.mLeftIndents != null) {
                left = this.mLeftIndents[Math.min(line, this.mLeftIndents.length - 1)];
            }
            int right = 0;
            if (this.mRightIndents != null) {
                right = this.mRightIndents[Math.min(line, this.mRightIndents.length - 1)];
            }
            return (left - right) >> 1;
        } else {
            throw new AssertionError("unhandled alignment " + align);
        }
    }

    public int getEllipsisCount(int line) {
        if (this.mColumns < 6) {
            return 0;
        }
        return this.mLines[(this.mColumns * line) + 5];
    }

    public int getEllipsisStart(int line) {
        if (this.mColumns < 6) {
            return 0;
        }
        return this.mLines[(this.mColumns * line) + 4];
    }

    public int getEllipsizedWidth() {
        return this.mEllipsizedWidth;
    }

    private final boolean isIdeographic(char c, boolean includeNonStarters) {
        if (c >= CHAR_FIRST_CJK && c <= '⿿') {
            return true;
        }
        if (c == '　') {
            return true;
        }
        if (c >= '぀' && c <= 'ゟ') {
            if (!includeNonStarters) {
                switch (c) {
                    case 'ぁ':
                    case 'ぃ':
                    case 'ぅ':
                    case 'ぇ':
                    case 'ぉ':
                    case 'っ':
                    case 'ゃ':
                    case 'ゅ':
                    case 'ょ':
                    case 'ゎ':
                    case 'ゕ':
                    case 'ゖ':
                    case '゛':
                    case '゜':
                    case 'ゝ':
                    case 'ゞ':
                        return false;
                }
            }
            return true;
        } else if (c >= '゠' && c <= 'ヿ') {
            if (!includeNonStarters) {
                switch (c) {
                    case '゠':
                    case 'ァ':
                    case 'ィ':
                    case 'ゥ':
                    case 'ェ':
                    case 'ォ':
                    case 'ッ':
                    case 'ャ':
                    case 'ュ':
                    case 'ョ':
                    case 'ヮ':
                    case 'ヵ':
                    case 'ヶ':
                    case '・':
                    case 'ー':
                    case 'ヽ':
                    case 'ヾ':
                        return false;
                }
            }
            return true;
        } else if (c >= '㐀' && c <= '䶵') {
            return true;
        } else {
            if (c >= '一' && c <= '龻') {
                return true;
            }
            if (c >= '豈' && c <= '龎') {
                return true;
            }
            if (c >= 'ꀀ' && c <= '꒏') {
                return true;
            }
            if (c >= '꒐' && c <= '꓏') {
                return true;
            }
            if (c >= '﹢' && c <= '﹦') {
                return true;
            }
            if (c >= '０' && c <= '９') {
                return true;
            }
            if (this.mCJKLineBreak == 2) {
                if (c >= '、' && c <= '〟') {
                    if (!includeNonStarters) {
                        switch (c) {
                            case '、':
                            case '。':
                            case '々':
                            case '〉':
                            case '》':
                            case '」':
                            case '』':
                            case '】':
                            case '〕':
                                return false;
                        }
                    }
                    switch (c) {
                        case '〈':
                        case '《':
                        case '「':
                        case '『':
                        case '【':
                        case '〔':
                            return false;
                        default:
                            return true;
                    }
                } else if (c >= '！' && c <= 'ﾟ') {
                    if (!includeNonStarters) {
                        switch (c) {
                            case '！':
                            case '）':
                            case '，':
                            case '．':
                            case '：':
                            case '；':
                            case '？':
                            case '］':
                            case '｝':
                            case '｡':
                            case '｣':
                            case '､':
                            case '･':
                            case 'ｰ':
                            case 'ﾞ':
                            case 'ﾟ':
                                return false;
                        }
                    }
                    switch (c) {
                        case '（':
                        case '［':
                        case '｛':
                        case '｢':
                            return false;
                        default:
                            return true;
                    }
                } else if (c >= '!' && c <= '?') {
                    if (!includeNonStarters) {
                        switch (c) {
                            case '!':
                            case ')':
                            case ',':
                            case '.':
                            case ':':
                            case ';':
                            case '?':
                                return false;
                        }
                    }
                    return true;
                } else if (c == ']' || c == '}' || c == '' || c == '°' || c == '’' || c == '”' || c == '′' || c == '″' || c == '゛' || c == '゜' || c == '・') {
                    return includeNonStarters;
                }
            }
            return false;
        }
    }
}
