package android.text;

import android.emoji.EmojiFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.text.TextUtils.TruncateAt;
import android.text.method.MetaKeyKeyListener;
import android.text.style.AlignmentSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LeadingMarginSpan.LeadingMarginSpan2;
import android.text.style.LineBackgroundSpan;
import android.text.style.ParagraphStyle;
import android.text.style.ReplacementSpan;
import android.text.style.TabStopSpan;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import com.android.internal.widget.AutoScrollHelper;
import java.util.Arrays;

public abstract class Layout {
    public static final int BREAK_STRATEGY_BALANCED = 2;
    public static final int BREAK_STRATEGY_HIGH_QUALITY = 1;
    public static final int BREAK_STRATEGY_SIMPLE = 0;
    static final Directions DIRS_ALL_LEFT_TO_RIGHT = new Directions(new int[]{0, RUN_LENGTH_MASK});
    static final Directions DIRS_ALL_RIGHT_TO_LEFT = new Directions(new int[]{0, 134217727});
    public static final int DIR_LEFT_TO_RIGHT = 1;
    static final int DIR_REQUEST_DEFAULT_LTR = 2;
    static final int DIR_REQUEST_DEFAULT_RTL = -2;
    static final int DIR_REQUEST_LTR = 1;
    static final int DIR_REQUEST_RTL = -1;
    public static final int DIR_RIGHT_TO_LEFT = -1;
    static final EmojiFactory EMOJI_FACTORY = EmojiFactory.newAvailableInstance();
    public static final int HYPHENATION_FREQUENCY_FULL = 2;
    public static final int HYPHENATION_FREQUENCY_NONE = 0;
    public static final int HYPHENATION_FREQUENCY_NORMAL = 1;
    static final int MAX_EMOJI;
    static final int MIN_EMOJI;
    private static final ParagraphStyle[] NO_PARA_SPANS = ((ParagraphStyle[]) ArrayUtils.emptyArray(ParagraphStyle.class));
    static final int RUN_LENGTH_MASK = 67108863;
    static final int RUN_LEVEL_MASK = 63;
    static final int RUN_LEVEL_SHIFT = 26;
    static final int RUN_RTL_FLAG = 67108864;
    private static final int TAB_INCREMENT = 20;
    private static final Rect sTempRect = new Rect();
    private Alignment mAlignment;
    private boolean mHighContrastTextMode;
    private SpanSet<LineBackgroundSpan> mLineBackgroundSpans;
    private TextPaint mPaint;
    private float mSpacingAdd;
    private float mSpacingMult;
    private boolean mSpannedText;
    private CharSequence mText;
    private TextDirectionHeuristic mTextDir;
    private int mWidth;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$text$Layout$Alignment = new int[Alignment.values().length];

        static {
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_NORMAL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public enum Alignment {
        ALIGN_NORMAL,
        ALIGN_OPPOSITE,
        ALIGN_CENTER,
        ALIGN_LEFT,
        ALIGN_RIGHT
    }

    public static class Directions {
        int[] mDirections;

        Directions(int[] dirs) {
            this.mDirections = dirs;
        }
    }

    static class Ellipsizer implements CharSequence, GetChars {
        Layout mLayout;
        TruncateAt mMethod;
        CharSequence mText;
        int mWidth;

        public Ellipsizer(CharSequence s) {
            this.mText = s;
        }

        public char charAt(int off) {
            char[] buf = TextUtils.obtain(1);
            getChars(off, off + 1, buf, 0);
            char ret = buf[0];
            TextUtils.recycle(buf);
            return ret;
        }

        public void getChars(int start, int end, char[] dest, int destoff) {
            int line1 = this.mLayout.getLineForOffset(start);
            int line2 = this.mLayout.getLineForOffset(end);
            TextUtils.getChars(this.mText, start, end, dest, destoff);
            for (int i = line1; i <= line2; i++) {
                this.mLayout.ellipsize(start, end, i, dest, destoff, this.mMethod);
            }
        }

        public int length() {
            return this.mText.length();
        }

        public CharSequence subSequence(int start, int end) {
            char[] s = new char[(end - start)];
            getChars(start, end, s, 0);
            return new String(s);
        }

        public String toString() {
            char[] s = new char[length()];
            getChars(0, length(), s, 0);
            return new String(s);
        }
    }

    static class SpannedEllipsizer extends Ellipsizer implements Spanned {
        private Spanned mSpanned;

        public SpannedEllipsizer(CharSequence display) {
            super(display);
            this.mSpanned = (Spanned) display;
        }

        public <T> T[] getSpans(int start, int end, Class<T> type) {
            return this.mSpanned.getSpans(start, end, type);
        }

        public int getSpanStart(Object tag) {
            return this.mSpanned.getSpanStart(tag);
        }

        public int getSpanEnd(Object tag) {
            return this.mSpanned.getSpanEnd(tag);
        }

        public int getSpanFlags(Object tag) {
            return this.mSpanned.getSpanFlags(tag);
        }

        public int nextSpanTransition(int start, int limit, Class type) {
            return this.mSpanned.nextSpanTransition(start, limit, type);
        }

        public CharSequence subSequence(int start, int end) {
            char[] s = new char[(end - start)];
            getChars(start, end, s, 0);
            SpannableString ss = new SpannableString(new String(s));
            TextUtils.copySpansFrom(this.mSpanned, start, end, Object.class, ss, 0);
            return ss;
        }
    }

    static class TabStops {
        private int mIncrement;
        private int mNumStops;
        private int[] mStops;

        TabStops(int increment, Object[] spans) {
            reset(increment, spans);
        }

        void reset(int increment, Object[] spans) {
            this.mIncrement = increment;
            int ns = 0;
            if (spans != null) {
                int[] stops = this.mStops;
                Object[] arr$ = spans;
                int len$ = arr$.length;
                int i$ = 0;
                int ns2 = 0;
                while (i$ < len$) {
                    Object o = arr$[i$];
                    if (o instanceof TabStopSpan) {
                        if (stops == null) {
                            stops = new int[10];
                        } else if (ns2 == stops.length) {
                            int[] nstops = new int[(ns2 * 2)];
                            for (int i = 0; i < ns2; i++) {
                                nstops[i] = stops[i];
                            }
                            stops = nstops;
                        }
                        ns = ns2 + 1;
                        stops[ns2] = ((TabStopSpan) o).getTabStop();
                    } else {
                        ns = ns2;
                    }
                    i$++;
                    ns2 = ns;
                }
                if (ns2 > 1) {
                    Arrays.sort(stops, 0, ns2);
                }
                if (stops != this.mStops) {
                    this.mStops = stops;
                }
                ns = ns2;
            }
            this.mNumStops = ns;
        }

        float nextTab(float h) {
            int ns = this.mNumStops;
            if (ns > 0) {
                int[] stops = this.mStops;
                for (int i = 0; i < ns; i++) {
                    int stop = stops[i];
                    if (((float) stop) > h) {
                        return (float) stop;
                    }
                }
            }
            return nextDefaultStop(h, this.mIncrement);
        }

        public static float nextDefaultStop(float h, int inc) {
            return (float) (((int) ((((float) inc) + h) / ((float) inc))) * inc);
        }
    }

    public abstract int getBottomPadding();

    public abstract int getEllipsisCount(int i);

    public abstract int getEllipsisStart(int i);

    public abstract boolean getLineContainsTab(int i);

    public abstract int getLineCount();

    public abstract int getLineDescent(int i);

    public abstract Directions getLineDirections(int i);

    public abstract int getLineStart(int i);

    public abstract int getLineTop(int i);

    public abstract int getParagraphDirection(int i);

    public abstract int getTopPadding();

    static {
        if (EMOJI_FACTORY != null) {
            MIN_EMOJI = EMOJI_FACTORY.getMinimumAndroidPua();
            MAX_EMOJI = EMOJI_FACTORY.getMaximumAndroidPua();
        } else {
            MIN_EMOJI = -1;
            MAX_EMOJI = -1;
        }
    }

    public static float getDesiredWidth(CharSequence source, TextPaint paint) {
        paint.set(paint);
        return getDesiredWidth(source, 0, source.length(), paint);
    }

    public static float getDesiredWidth(CharSequence source, int start, int end, TextPaint paint) {
        float need = 0.0f;
        int i = start;
        while (i <= end) {
            int next = TextUtils.indexOf(source, '\n', i, end);
            if (next < 0) {
                next = end;
            }
            float w = measurePara(paint, source, i, next);
            if (w > need) {
                need = w;
            }
            i = next + 1;
        }
        return need;
    }

    protected Layout(CharSequence text, TextPaint paint, int width, Alignment align, float spacingMult, float spacingAdd) {
        this(text, paint, width, align, TextDirectionHeuristics.FIRSTSTRONG_LTR, spacingMult, spacingAdd);
    }

    protected Layout(CharSequence text, TextPaint paint, int width, Alignment align, TextDirectionHeuristic textDir, float spacingMult, float spacingAdd) {
        this.mHighContrastTextMode = false;
        this.mAlignment = Alignment.ALIGN_NORMAL;
        if (width < 0) {
            throw new IllegalArgumentException("Layout: " + width + " < 0");
        }
        if (paint != null) {
            paint.bgColor = 0;
            paint.baselineShift = 0;
        }
        this.mText = text;
        this.mPaint = paint;
        this.mWidth = width;
        this.mAlignment = align;
        this.mSpacingMult = spacingMult;
        this.mSpacingAdd = spacingAdd;
        this.mSpannedText = text instanceof Spanned;
        this.mTextDir = textDir;
    }

    void replaceWith(CharSequence text, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd) {
        if (width < 0) {
            throw new IllegalArgumentException("Layout: " + width + " < 0");
        }
        this.mText = text;
        this.mPaint = paint;
        this.mWidth = width;
        this.mAlignment = align;
        this.mSpacingMult = spacingmult;
        this.mSpacingAdd = spacingadd;
        this.mSpannedText = text instanceof Spanned;
    }

    public void draw(Canvas c) {
        draw(c, null, null, 0);
    }

    public void draw(Canvas canvas, Path highlight, Paint highlightPaint, int cursorOffsetVertical) {
        long lineRange = getLineRangeForDraw(canvas);
        int firstLine = TextUtils.unpackRangeStartFromLong(lineRange);
        int lastLine = TextUtils.unpackRangeEndFromLong(lineRange);
        if (lastLine >= 0) {
            drawBackground(canvas, highlight, highlightPaint, cursorOffsetVertical, firstLine, lastLine);
            drawText(canvas, firstLine, lastLine);
        }
    }

    public void drawText(Canvas canvas, int firstLine, int lastLine) {
        int previousLineBottom = getLineTop(firstLine);
        int previousLineEnd = getLineStart(firstLine);
        ParagraphStyle[] spans = NO_PARA_SPANS;
        int spanEnd = 0;
        Paint paint = this.mPaint;
        CharSequence buf = this.mText;
        Alignment paraAlign = this.mAlignment;
        boolean tabStopsIsInitialized = false;
        TextLine tl = TextLine.obtain();
        int lineNum = firstLine;
        TabStops tabStops = null;
        while (lineNum <= lastLine) {
            TabStops tabStops2;
            int x;
            int start = previousLineEnd;
            previousLineEnd = getLineStart(lineNum + 1);
            int end = getLineVisibleEnd(lineNum, start, previousLineEnd);
            int ltop = previousLineBottom;
            int lbottom = getLineTop(lineNum + 1);
            previousLineBottom = lbottom;
            int lbaseline = lbottom - getLineDescent(lineNum);
            int dir = getParagraphDirection(lineNum);
            int left = 0;
            int right = this.mWidth;
            if (this.mSpannedText) {
                boolean isFirstParaLine;
                int n;
                Spanned sp = (Spanned) buf;
                int textLength = buf.length();
                if (start == 0 || buf.charAt(start - 1) == '\n') {
                    isFirstParaLine = true;
                } else {
                    isFirstParaLine = false;
                }
                if (start >= spanEnd && (lineNum == firstLine || isFirstParaLine)) {
                    spanEnd = sp.nextSpanTransition(start, textLength, ParagraphStyle.class);
                    spans = (ParagraphStyle[]) getParagraphSpans(sp, start, spanEnd, ParagraphStyle.class);
                    paraAlign = this.mAlignment;
                    for (n = spans.length - 1; n >= 0; n--) {
                        if (spans[n] instanceof AlignmentSpan) {
                            paraAlign = ((AlignmentSpan) spans[n]).getAlignment();
                            break;
                        }
                    }
                    tabStopsIsInitialized = false;
                }
                int length = spans.length;
                boolean useFirstLineMargin = isFirstParaLine;
                for (n = 0; n < length; n++) {
                    if (spans[n] instanceof LeadingMarginSpan2) {
                        if (lineNum < getLineForOffset(sp.getSpanStart(spans[n])) + ((LeadingMarginSpan2) spans[n]).getLeadingMarginLineCount()) {
                            useFirstLineMargin = true;
                            break;
                        }
                    }
                }
                for (n = 0; n < length; n++) {
                    if (spans[n] instanceof LeadingMarginSpan) {
                        LeadingMarginSpan margin = spans[n];
                        if (dir == -1) {
                            margin.drawLeadingMargin(canvas, paint, right, dir, ltop, lbaseline, lbottom, buf, start, end, isFirstParaLine, this);
                            right -= margin.getLeadingMargin(useFirstLineMargin);
                        } else {
                            margin.drawLeadingMargin(canvas, paint, left, dir, ltop, lbaseline, lbottom, buf, start, end, isFirstParaLine, this);
                            left += margin.getLeadingMargin(useFirstLineMargin);
                        }
                    }
                }
            }
            boolean hasTabOrEmoji = getLineContainsTab(lineNum);
            if (!hasTabOrEmoji || tabStopsIsInitialized) {
                tabStops2 = tabStops;
            } else {
                if (tabStops == null) {
                    TabStops tabStops3 = new TabStops(20, spans);
                } else {
                    tabStops.reset(20, spans);
                    tabStops2 = tabStops;
                }
                tabStopsIsInitialized = true;
            }
            Alignment align = paraAlign;
            if (align == Alignment.ALIGN_LEFT) {
                align = dir == 1 ? Alignment.ALIGN_NORMAL : Alignment.ALIGN_OPPOSITE;
            } else if (align == Alignment.ALIGN_RIGHT) {
                align = dir == 1 ? Alignment.ALIGN_OPPOSITE : Alignment.ALIGN_NORMAL;
            }
            if (align != Alignment.ALIGN_NORMAL) {
                int max = (int) getLineExtent(lineNum, tabStops2, false);
                if (align != Alignment.ALIGN_OPPOSITE) {
                    x = (((right + left) - (max & -2)) >> 1) + getIndentAdjust(lineNum, Alignment.ALIGN_CENTER);
                } else if (dir == 1) {
                    x = (right - max) + getIndentAdjust(lineNum, Alignment.ALIGN_RIGHT);
                } else {
                    x = (left - max) + getIndentAdjust(lineNum, Alignment.ALIGN_LEFT);
                }
            } else if (dir == 1) {
                x = left + getIndentAdjust(lineNum, Alignment.ALIGN_LEFT);
            } else {
                x = right + getIndentAdjust(lineNum, Alignment.ALIGN_RIGHT);
            }
            paint.setHyphenEdit(getHyphen(lineNum));
            Directions directions = getLineDirections(lineNum);
            if (directions != DIRS_ALL_LEFT_TO_RIGHT || this.mSpannedText || hasTabOrEmoji) {
                tl.set(paint, buf, start, end, dir, directions, hasTabOrEmoji, tabStops2);
                tl.draw(canvas, (float) x, ltop, lbaseline, lbottom);
            } else {
                canvas.drawText(buf, start, end, (float) x, (float) lbaseline, paint);
            }
            paint.setHyphenEdit(0);
            lineNum++;
            tabStops = tabStops2;
        }
        TextLine.recycle(tl);
    }

    public void drawBackground(Canvas canvas, Path highlight, Paint highlightPaint, int cursorOffsetVertical, int firstLine, int lastLine) {
        if (this.mSpannedText) {
            if (this.mLineBackgroundSpans == null) {
                this.mLineBackgroundSpans = new SpanSet(LineBackgroundSpan.class);
            }
            Spanned buffer = this.mText;
            int textLength = buffer.length();
            this.mLineBackgroundSpans.init(buffer, 0, textLength);
            if (this.mLineBackgroundSpans.numberOfSpans > 0) {
                int previousLineBottom = getLineTop(firstLine);
                int previousLineEnd = getLineStart(firstLine);
                ParagraphStyle[] spans = NO_PARA_SPANS;
                int spansLength = 0;
                TextPaint paint = this.mPaint;
                int spanEnd = 0;
                int width = this.mWidth;
                for (int i = firstLine; i <= lastLine; i++) {
                    int start = previousLineEnd;
                    int end = getLineStart(i + 1);
                    previousLineEnd = end;
                    int ltop = previousLineBottom;
                    int lbottom = getLineTop(i + 1);
                    previousLineBottom = lbottom;
                    int lbaseline = lbottom - getLineDescent(i);
                    if (start >= spanEnd) {
                        spanEnd = this.mLineBackgroundSpans.getNextTransition(start, textLength);
                        spansLength = 0;
                        if (start != end || start == 0) {
                            int j = 0;
                            while (j < this.mLineBackgroundSpans.numberOfSpans) {
                                if (this.mLineBackgroundSpans.spanStarts[j] < end && this.mLineBackgroundSpans.spanEnds[j] > start) {
                                    spans = (ParagraphStyle[]) GrowingArrayUtils.append((Object[]) spans, spansLength, ((LineBackgroundSpan[]) this.mLineBackgroundSpans.spans)[j]);
                                    spansLength++;
                                }
                                j++;
                            }
                        }
                    }
                    for (int n = 0; n < spansLength; n++) {
                        spans[n].drawBackground(canvas, paint, 0, width, ltop, lbaseline, lbottom, buffer, start, end, i);
                    }
                }
            }
            this.mLineBackgroundSpans.recycle();
        }
        if (highlight != null) {
            if (cursorOffsetVertical != 0) {
                canvas.translate(0.0f, (float) cursorOffsetVertical);
            }
            canvas.drawPath(highlight, highlightPaint);
            if (cursorOffsetVertical != 0) {
                canvas.translate(0.0f, (float) (-cursorOffsetVertical));
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long getLineRangeForDraw(android.graphics.Canvas r9) {
        /*
        r8 = this;
        r7 = -1;
        r5 = 0;
        r6 = sTempRect;
        monitor-enter(r6);
        r4 = sTempRect;	 Catch:{ all -> 0x0035 }
        r4 = r9.getClipBounds(r4);	 Catch:{ all -> 0x0035 }
        if (r4 != 0) goto L_0x0015;
    L_0x000d:
        r4 = 0;
        r5 = -1;
        r4 = android.text.TextUtils.packRangeInLong(r4, r5);	 Catch:{ all -> 0x0035 }
        monitor-exit(r6);	 Catch:{ all -> 0x0035 }
    L_0x0014:
        return r4;
    L_0x0015:
        r4 = sTempRect;	 Catch:{ all -> 0x0035 }
        r2 = r4.top;	 Catch:{ all -> 0x0035 }
        r4 = sTempRect;	 Catch:{ all -> 0x0035 }
        r1 = r4.bottom;	 Catch:{ all -> 0x0035 }
        monitor-exit(r6);	 Catch:{ all -> 0x0035 }
        r3 = java.lang.Math.max(r2, r5);
        r4 = r8.getLineCount();
        r4 = r8.getLineTop(r4);
        r0 = java.lang.Math.min(r4, r1);
        if (r3 < r0) goto L_0x0038;
    L_0x0030:
        r4 = android.text.TextUtils.packRangeInLong(r5, r7);
        goto L_0x0014;
    L_0x0035:
        r4 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0035 }
        throw r4;
    L_0x0038:
        r4 = r8.getLineForVertical(r3);
        r5 = r8.getLineForVertical(r0);
        r4 = android.text.TextUtils.packRangeInLong(r4, r5);
        goto L_0x0014;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.Layout.getLineRangeForDraw(android.graphics.Canvas):long");
    }

    private int getLineStartPos(int line, int left, int right) {
        Alignment align = getParagraphAlignment(line);
        int dir = getParagraphDirection(line);
        if (align == Alignment.ALIGN_LEFT) {
            align = dir == 1 ? Alignment.ALIGN_NORMAL : Alignment.ALIGN_OPPOSITE;
        } else if (align == Alignment.ALIGN_RIGHT) {
            align = dir == 1 ? Alignment.ALIGN_OPPOSITE : Alignment.ALIGN_NORMAL;
        }
        if (align != Alignment.ALIGN_NORMAL) {
            TabStops tabStops = null;
            if (this.mSpannedText && getLineContainsTab(line)) {
                Spanned spanned = this.mText;
                int start = getLineStart(line);
                TabStopSpan[] tabSpans = (TabStopSpan[]) getParagraphSpans(spanned, start, spanned.nextSpanTransition(start, spanned.length(), TabStopSpan.class), TabStopSpan.class);
                if (tabSpans.length > 0) {
                    tabStops = new TabStops(20, tabSpans);
                }
            }
            int max = (int) getLineExtent(line, tabStops, false);
            if (align != Alignment.ALIGN_OPPOSITE) {
                return ((left + right) - (max & -2)) >> (getIndentAdjust(line, Alignment.ALIGN_CENTER) + 1);
            } else if (dir == 1) {
                return (right - max) + getIndentAdjust(line, Alignment.ALIGN_RIGHT);
            } else {
                return (left - max) + getIndentAdjust(line, Alignment.ALIGN_LEFT);
            }
        } else if (dir == 1) {
            return left + getIndentAdjust(line, Alignment.ALIGN_LEFT);
        } else {
            return right + getIndentAdjust(line, Alignment.ALIGN_RIGHT);
        }
    }

    public final CharSequence getText() {
        return this.mText;
    }

    public final TextPaint getPaint() {
        return this.mPaint;
    }

    public final int getWidth() {
        return this.mWidth;
    }

    public int getEllipsizedWidth() {
        return this.mWidth;
    }

    public final void increaseWidthTo(int wid) {
        if (wid < this.mWidth) {
            throw new RuntimeException("attempted to reduce Layout width");
        }
        this.mWidth = wid;
    }

    public int getHeight() {
        return getLineTop(getLineCount());
    }

    public final Alignment getAlignment() {
        return this.mAlignment;
    }

    public final float getSpacingMultiplier() {
        return this.mSpacingMult;
    }

    public final float getSpacingAdd() {
        return this.mSpacingAdd;
    }

    public final TextDirectionHeuristic getTextDirectionHeuristic() {
        return this.mTextDir;
    }

    public int getLineBounds(int line, Rect bounds) {
        if (bounds != null) {
            bounds.left = 0;
            bounds.top = getLineTop(line);
            bounds.right = this.mWidth;
            bounds.bottom = getLineTop(line + 1);
        }
        return getLineBaseline(line);
    }

    public int getHyphen(int line) {
        return 0;
    }

    public int getIndentAdjust(int line, Alignment alignment) {
        return 0;
    }

    public boolean isLevelBoundary(int offset) {
        int line = getLineForOffset(offset);
        Directions dirs = getLineDirections(line);
        if (dirs == DIRS_ALL_LEFT_TO_RIGHT || dirs == DIRS_ALL_RIGHT_TO_LEFT) {
            return false;
        }
        int[] runs = dirs.mDirections;
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        if (offset == lineStart || offset == lineEnd) {
            int paraLevel;
            if (getParagraphDirection(line) == 1) {
                paraLevel = 0;
            } else {
                paraLevel = 1;
            }
            if (((runs[(offset == lineStart ? 0 : runs.length - 2) + 1] >>> 26) & 63) != paraLevel) {
                return true;
            }
            return false;
        }
        offset -= lineStart;
        for (int i = 0; i < runs.length; i += 2) {
            if (offset == runs[i]) {
                return true;
            }
        }
        return false;
    }

    public boolean isRtlCharAt(int offset) {
        boolean z = true;
        int line = getLineForOffset(offset);
        Directions dirs = getLineDirections(line);
        if (dirs == DIRS_ALL_LEFT_TO_RIGHT) {
            return false;
        }
        if (dirs == DIRS_ALL_RIGHT_TO_LEFT) {
            return true;
        }
        int[] runs = dirs.mDirections;
        int lineStart = getLineStart(line);
        int i = 0;
        while (i < runs.length) {
            int start = lineStart + runs[i];
            int limit = start + (runs[i + 1] & RUN_LENGTH_MASK);
            if (offset < start || offset >= limit) {
                i += 2;
            } else {
                if ((((runs[i + 1] >>> 26) & 63) & 1) == 0) {
                    z = false;
                }
                return z;
            }
        }
        return false;
    }

    private boolean primaryIsTrailingPrevious(int offset) {
        int levelBefore;
        boolean z = true;
        int line = getLineForOffset(offset);
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        int[] runs = getLineDirections(line).mDirections;
        int levelAt = -1;
        int i = 0;
        while (i < runs.length) {
            int start = lineStart + runs[i];
            int limit = start + (runs[i + 1] & RUN_LENGTH_MASK);
            if (limit > lineEnd) {
                limit = lineEnd;
            }
            if (offset < start || offset >= limit) {
                i += 2;
            } else if (offset > start) {
                return false;
            } else {
                levelAt = (runs[i + 1] >>> 26) & 63;
                if (levelAt == -1) {
                    if (getParagraphDirection(line) != 1) {
                        levelAt = 0;
                    } else {
                        levelAt = 1;
                    }
                }
                levelBefore = -1;
                if (offset == lineStart) {
                    offset--;
                    for (i = 0; i < runs.length; i += 2) {
                        start = lineStart + runs[i];
                        limit = start + (runs[i + 1] & RUN_LENGTH_MASK);
                        if (limit > lineEnd) {
                            limit = lineEnd;
                        }
                        if (offset < start && offset < limit) {
                            levelBefore = (runs[i + 1] >>> 26) & 63;
                            break;
                        }
                    }
                } else if (getParagraphDirection(line) != 1) {
                    levelBefore = 0;
                } else {
                    levelBefore = 1;
                }
                if (levelBefore >= levelAt) {
                    z = false;
                }
                return z;
            }
        }
        if (levelAt == -1) {
            if (getParagraphDirection(line) != 1) {
                levelAt = 1;
            } else {
                levelAt = 0;
            }
        }
        levelBefore = -1;
        if (offset == lineStart) {
            offset--;
            while (i < runs.length) {
                start = lineStart + runs[i];
                limit = start + (runs[i + 1] & RUN_LENGTH_MASK);
                if (limit > lineEnd) {
                    limit = lineEnd;
                }
                if (offset < start) {
                }
            }
        } else if (getParagraphDirection(line) != 1) {
            levelBefore = 1;
        } else {
            levelBefore = 0;
        }
        if (levelBefore >= levelAt) {
            z = false;
        }
        return z;
    }

    public float getPrimaryHorizontal(int offset) {
        return getPrimaryHorizontal(offset, false);
    }

    public float getPrimaryHorizontal(int offset, boolean clamped) {
        return getHorizontal(offset, primaryIsTrailingPrevious(offset), clamped);
    }

    public float getSecondaryHorizontal(int offset) {
        return getSecondaryHorizontal(offset, false);
    }

    public float getSecondaryHorizontal(int offset, boolean clamped) {
        return getHorizontal(offset, !primaryIsTrailingPrevious(offset), clamped);
    }

    private float getHorizontal(int offset, boolean trailing, boolean clamped) {
        return getHorizontal(offset, trailing, getLineForOffset(offset), clamped);
    }

    private float getHorizontal(int offset, boolean trailing, int line, boolean clamped) {
        int start = getLineStart(line);
        int end = getLineEnd(line);
        int dir = getParagraphDirection(line);
        boolean hasTabOrEmoji = getLineContainsTab(line);
        Directions directions = getLineDirections(line);
        TabStops tabStops = null;
        if (hasTabOrEmoji && (this.mText instanceof Spanned)) {
            TabStopSpan[] tabs = (TabStopSpan[]) getParagraphSpans((Spanned) this.mText, start, end, TabStopSpan.class);
            if (tabs.length > 0) {
                tabStops = new TabStops(20, tabs);
            }
        }
        TextLine tl = TextLine.obtain();
        tl.set(this.mPaint, this.mText, start, end, dir, directions, hasTabOrEmoji, tabStops);
        float wid = tl.measure(offset - start, trailing, null);
        TextLine.recycle(tl);
        if (clamped && wid > ((float) this.mWidth)) {
            wid = (float) this.mWidth;
        }
        return ((float) getLineStartPos(line, getParagraphLeft(line), getParagraphRight(line))) + wid;
    }

    public float getLineLeft(int line) {
        int dir = getParagraphDirection(line);
        Alignment align = getParagraphAlignment(line);
        if (align == Alignment.ALIGN_LEFT) {
            return 0.0f;
        }
        if (align == Alignment.ALIGN_NORMAL) {
            if (dir == -1) {
                return ((float) getParagraphRight(line)) - getLineMax(line);
            }
            return 0.0f;
        } else if (align == Alignment.ALIGN_RIGHT) {
            return ((float) this.mWidth) - getLineMax(line);
        } else {
            if (align != Alignment.ALIGN_OPPOSITE) {
                int left = getParagraphLeft(line);
                return (float) ((((getParagraphRight(line) - left) - (((int) getLineMax(line)) & -2)) / 2) + left);
            } else if (dir != -1) {
                return ((float) this.mWidth) - getLineMax(line);
            } else {
                return 0.0f;
            }
        }
    }

    public float getLineRight(int line) {
        int dir = getParagraphDirection(line);
        Alignment align = getParagraphAlignment(line);
        if (align == Alignment.ALIGN_LEFT) {
            return ((float) getParagraphLeft(line)) + getLineMax(line);
        }
        if (align == Alignment.ALIGN_NORMAL) {
            if (dir == -1) {
                return (float) this.mWidth;
            }
            return ((float) getParagraphLeft(line)) + getLineMax(line);
        } else if (align == Alignment.ALIGN_RIGHT) {
            return (float) this.mWidth;
        } else {
            if (align != Alignment.ALIGN_OPPOSITE) {
                int left = getParagraphLeft(line);
                int right = getParagraphRight(line);
                return (float) (right - (((right - left) - (((int) getLineMax(line)) & -2)) / 2));
            } else if (dir == -1) {
                return getLineMax(line);
            } else {
                return (float) this.mWidth;
            }
        }
    }

    public float getLineMax(int line) {
        float margin = (float) getParagraphLeadingMargin(line);
        float signedExtent = getLineExtent(line, false);
        if (signedExtent < 0.0f) {
            signedExtent = -signedExtent;
        }
        return margin + signedExtent;
    }

    public float getLineWidth(int line) {
        float margin = (float) getParagraphLeadingMargin(line);
        float signedExtent = getLineExtent(line, true);
        if (signedExtent < 0.0f) {
            signedExtent = -signedExtent;
        }
        return margin + signedExtent;
    }

    private float getLineExtent(int line, boolean full) {
        int start = getLineStart(line);
        int end = full ? getLineEnd(line) : getLineVisibleEnd(line);
        boolean hasTabsOrEmoji = getLineContainsTab(line);
        TabStops tabStops = null;
        if (hasTabsOrEmoji && (this.mText instanceof Spanned)) {
            TabStopSpan[] tabs = (TabStopSpan[]) getParagraphSpans((Spanned) this.mText, start, end, TabStopSpan.class);
            if (tabs.length > 0) {
                tabStops = new TabStops(20, tabs);
            }
        }
        Directions directions = getLineDirections(line);
        if (directions == null) {
            return 0.0f;
        }
        int dir = getParagraphDirection(line);
        TextLine tl = TextLine.obtain();
        tl.set(this.mPaint, this.mText, start, end, dir, directions, hasTabsOrEmoji, tabStops);
        float width = tl.metrics(null);
        TextLine.recycle(tl);
        return width;
    }

    private float getLineExtent(int line, TabStops tabStops, boolean full) {
        int start = getLineStart(line);
        int end = full ? getLineEnd(line) : getLineVisibleEnd(line);
        boolean hasTabsOrEmoji = getLineContainsTab(line);
        Directions directions = getLineDirections(line);
        int dir = getParagraphDirection(line);
        TextLine tl = TextLine.obtain();
        tl.set(this.mPaint, this.mText, start, end, dir, directions, hasTabsOrEmoji, tabStops);
        float width = tl.metrics(null);
        TextLine.recycle(tl);
        return width;
    }

    public int getLineForVertical(int vertical) {
        int high = getLineCount();
        int low = -1;
        while (high - low > 1) {
            int guess = (high + low) / 2;
            if (getLineTop(guess) > vertical) {
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

    public int getLineForOffset(int offset) {
        int high = getLineCount();
        int low = -1;
        while (high - low > 1) {
            int guess = (high + low) / 2;
            if (getLineStart(guess) > offset) {
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

    public int getOffsetForHorizontal(int line, float horiz) {
        float dist;
        int max = getLineEnd(line) - 1;
        int min = getLineStart(line);
        Directions dirs = getLineDirections(line);
        if (line == getLineCount() - 1) {
            max++;
        }
        int best = min;
        float bestdist = Math.abs(getPrimaryHorizontal(best) - horiz);
        for (int i = 0; i < dirs.mDirections.length; i += 2) {
            int here = min + dirs.mDirections[i];
            int there = here + (dirs.mDirections[i + 1] & RUN_LENGTH_MASK);
            int swap = (dirs.mDirections[i + 1] & 67108864) != 0 ? -1 : 1;
            if (there > max) {
                there = max;
            }
            int high = (there - 1) + 1;
            int low = (here + 1) - 1;
            while (high - low > 1) {
                int guess = (high + low) / 2;
                if (getPrimaryHorizontal(getOffsetAtStartOf(guess)) * ((float) swap) >= ((float) swap) * horiz) {
                    high = guess;
                } else {
                    low = guess;
                }
            }
            if (low < here + 1) {
                low = here + 1;
            }
            if (low < there) {
                low = getOffsetAtStartOf(low);
                dist = Math.abs(getPrimaryHorizontal(low) - horiz);
                int aft = TextUtils.getOffsetAfter(this.mText, low);
                if (aft < there) {
                    float other = Math.abs(getPrimaryHorizontal(aft) - horiz);
                    if (other < dist) {
                        dist = other;
                        low = aft;
                    }
                }
                if (dist < bestdist) {
                    bestdist = dist;
                    best = low;
                }
            }
            dist = Math.abs(getPrimaryHorizontal(here) - horiz);
            if (dist < bestdist) {
                bestdist = dist;
                best = here;
            }
        }
        dist = Math.abs(getPrimaryHorizontal(max) - horiz);
        if (dist > bestdist) {
            return best;
        }
        bestdist = dist;
        return max;
    }

    public final int getLineEnd(int line) {
        return getLineStart(line + 1);
    }

    public int getLineVisibleEnd(int line) {
        return getLineVisibleEnd(line, getLineStart(line), getLineStart(line + 1));
    }

    private int getLineVisibleEnd(int line, int start, int end) {
        CharSequence text = this.mText;
        if (line == getLineCount() - 1) {
            return end;
        }
        while (end > start) {
            char ch = text.charAt(end - 1);
            if (ch != '\n') {
                if (ch != ' ' && ch != '\t' && ch != ' ' && ((' ' > ch || ch > ' ' || ch == ' ') && ch != ' ' && ch != '　')) {
                    break;
                }
                end--;
            } else {
                return end - 1;
            }
        }
        return end;
    }

    public final int getLineBottom(int line) {
        return getLineTop(line + 1);
    }

    public final int getLineBaseline(int line) {
        return getLineTop(line + 1) - getLineDescent(line);
    }

    public final int getLineAscent(int line) {
        return getLineTop(line) - (getLineTop(line + 1) - getLineDescent(line));
    }

    public int getOffsetToLeftOf(int offset) {
        return getOffsetToLeftRightOf(offset, true);
    }

    public int getOffsetToRightOf(int offset) {
        return getOffsetToLeftRightOf(offset, false);
    }

    private int getOffsetToLeftRightOf(int caret, boolean toLeft) {
        int line = getLineForOffset(caret);
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        int lineDir = getParagraphDirection(line);
        boolean lineChanged = false;
        if (toLeft == (lineDir == -1)) {
            if (caret == lineEnd) {
                if (line >= getLineCount() - 1) {
                    return caret;
                }
                lineChanged = true;
                line++;
            }
        } else if (caret == lineStart) {
            if (line <= 0) {
                return caret;
            }
            lineChanged = true;
            line--;
        }
        if (lineChanged) {
            lineStart = getLineStart(line);
            lineEnd = getLineEnd(line);
            int newDir = getParagraphDirection(line);
            if (newDir != lineDir) {
                toLeft = !toLeft;
                lineDir = newDir;
            }
        }
        Directions directions = getLineDirections(line);
        TextLine tl = TextLine.obtain();
        tl.set(this.mPaint, this.mText, lineStart, lineEnd, lineDir, directions, false, null);
        caret = lineStart + tl.getOffsetToLeftRightOf(caret - lineStart, toLeft);
        tl = TextLine.recycle(tl);
        return caret;
    }

    private int getOffsetAtStartOf(int offset) {
        if (offset == 0) {
            return 0;
        }
        CharSequence text = this.mText;
        char c = text.charAt(offset);
        if (c >= '?' && c <= '?') {
            char c1 = text.charAt(offset - 1);
            if (c1 >= '?' && c1 <= '?') {
                offset--;
            }
        }
        if (this.mSpannedText) {
            ReplacementSpan[] spans = (ReplacementSpan[]) ((Spanned) text).getSpans(offset, offset, ReplacementSpan.class);
            for (int i = 0; i < spans.length; i++) {
                int start = ((Spanned) text).getSpanStart(spans[i]);
                int end = ((Spanned) text).getSpanEnd(spans[i]);
                if (start < offset && end > offset) {
                    offset = start;
                }
            }
        }
        return offset;
    }

    public boolean shouldClampCursor(int line) {
        switch (AnonymousClass1.$SwitchMap$android$text$Layout$Alignment[getParagraphAlignment(line).ordinal()]) {
            case 1:
                return true;
            case 2:
                if (getParagraphDirection(line) <= 0) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public void getCursorPath(int point, Path dest, CharSequence editingBuffer) {
        float h2;
        dest.reset();
        int line = getLineForOffset(point);
        int top = getLineTop(line);
        int bottom = getLineTop(line + 1);
        boolean clamped = shouldClampCursor(line);
        float h1 = getPrimaryHorizontal(point, clamped) - 0.5f;
        if (isLevelBoundary(point)) {
            h2 = getSecondaryHorizontal(point, clamped) - 0.5f;
        } else {
            h2 = h1;
        }
        int caps = MetaKeyKeyListener.getMetaState(editingBuffer, 1) | MetaKeyKeyListener.getMetaState(editingBuffer, 2048);
        int fn = MetaKeyKeyListener.getMetaState(editingBuffer, 2);
        int dist = 0;
        if (!(caps == 0 && fn == 0)) {
            dist = (bottom - top) >> 2;
            if (fn != 0) {
                top += dist;
            }
            if (caps != 0) {
                bottom -= dist;
            }
        }
        if (h1 < 0.5f) {
            h1 = 0.5f;
        }
        if (h2 < 0.5f) {
            h2 = 0.5f;
        }
        if (Float.compare(h1, h2) == 0) {
            dest.moveTo(h1, (float) top);
            dest.lineTo(h1, (float) bottom);
        } else {
            dest.moveTo(h1, (float) top);
            dest.lineTo(h1, (float) ((top + bottom) >> 1));
            dest.moveTo(h2, (float) ((top + bottom) >> 1));
            dest.lineTo(h2, (float) bottom);
        }
        if (caps == 2) {
            dest.moveTo(h2, (float) bottom);
            dest.lineTo(h2 - ((float) dist), (float) (bottom + dist));
            dest.lineTo(h2, (float) bottom);
            dest.lineTo(((float) dist) + h2, (float) (bottom + dist));
        } else if (caps == 1) {
            dest.moveTo(h2, (float) bottom);
            dest.lineTo(h2 - ((float) dist), (float) (bottom + dist));
            dest.moveTo(h2 - ((float) dist), ((float) (bottom + dist)) - 0.5f);
            dest.lineTo(((float) dist) + h2, ((float) (bottom + dist)) - 0.5f);
            dest.moveTo(((float) dist) + h2, (float) (bottom + dist));
            dest.lineTo(h2, (float) bottom);
        }
        if (fn == 2) {
            dest.moveTo(h1, (float) top);
            dest.lineTo(h1 - ((float) dist), (float) (top - dist));
            dest.lineTo(h1, (float) top);
            dest.lineTo(((float) dist) + h1, (float) (top - dist));
        } else if (fn == 1) {
            dest.moveTo(h1, (float) top);
            dest.lineTo(h1 - ((float) dist), (float) (top - dist));
            dest.moveTo(h1 - ((float) dist), ((float) (top - dist)) + 0.5f);
            dest.lineTo(((float) dist) + h1, ((float) (top - dist)) + 0.5f);
            dest.moveTo(((float) dist) + h1, (float) (top - dist));
            dest.lineTo(h1, (float) top);
        }
    }

    private void addSelection(int line, int start, int end, int top, int bottom, Path dest) {
        int linestart = getLineStart(line);
        int lineend = getLineEnd(line);
        Directions dirs = getLineDirections(line);
        if (lineend > linestart && this.mText.charAt(lineend - 1) == '\n') {
            lineend--;
        }
        for (int i = 0; i < dirs.mDirections.length; i += 2) {
            int here = linestart + dirs.mDirections[i];
            int there = here + (dirs.mDirections[i + 1] & RUN_LENGTH_MASK);
            if (there > lineend) {
                there = lineend;
            }
            if (start <= there && end >= here) {
                int st = Math.max(start, here);
                int en = Math.min(end, there);
                if (st != en) {
                    float h1 = getHorizontal(st, false, line, false);
                    float h2 = getHorizontal(en, true, line, false);
                    Path path = dest;
                    path.addRect(Math.min(h1, h2), (float) top, Math.max(h1, h2), (float) bottom, Direction.CW);
                }
            }
        }
    }

    public void getSelectionPath(int start, int end, Path dest) {
        dest.reset();
        if (start != end) {
            if (end < start) {
                int temp = end;
                end = start;
                start = temp;
            }
            int startline = getLineForOffset(start);
            int endline = getLineForOffset(end);
            int top = getLineTop(startline);
            int bottom = getLineBottom(endline);
            if (startline == endline) {
                addSelection(startline, start, end, top, bottom, dest);
                return;
            }
            float width = (float) this.mWidth;
            addSelection(startline, start, getLineEnd(startline), top, getLineBottom(startline), dest);
            if (getParagraphDirection(startline) == -1) {
                dest.addRect(getLineLeft(startline), (float) top, 0.0f, (float) getLineBottom(startline), Direction.CW);
            } else {
                dest.addRect(getLineRight(startline), (float) top, width, (float) getLineBottom(startline), Direction.CW);
            }
            for (int i = startline + 1; i < endline; i++) {
                Path path = dest;
                float f = width;
                path.addRect(0.0f, (float) getLineTop(i), f, (float) getLineBottom(i), Direction.CW);
            }
            top = getLineTop(endline);
            bottom = getLineBottom(endline);
            addSelection(endline, getLineStart(endline), end, top, bottom, dest);
            if (getParagraphDirection(endline) == -1) {
                dest.addRect(width, (float) top, getLineRight(endline), (float) bottom, Direction.CW);
                return;
            }
            dest.addRect(0.0f, (float) top, getLineLeft(endline), (float) bottom, Direction.CW);
        }
    }

    public void addSelectionPath(int start, int end, Path dest) {
        if (start != end) {
            if (end < start) {
                int temp = end;
                end = start;
                start = temp;
            }
            int startline = getLineForOffset(start);
            int endline = getLineForOffset(end);
            int top = getLineTop(startline) + 1;
            int bottom = getLineBottom(endline) - 1;
            if (startline == endline) {
                addSelection(startline, start, end, top, bottom, dest);
                return;
            }
            float width = (float) this.mWidth;
            addSelection(startline, start, getLineEnd(startline), top, getLineBottom(startline) - 1, dest);
            if (getParagraphDirection(startline) == -1) {
                dest.addRect(getLineLeft(startline), (float) top, 0.0f, (float) (getLineBottom(startline) - 1), Direction.CW);
            } else {
                dest.addRect(getLineRight(startline), (float) top, width, (float) (getLineBottom(startline) - 1), Direction.CW);
            }
            for (int i = startline + 1; i < endline; i++) {
                Path path = dest;
                float f = width;
                path.addRect(0.0f, (float) (getLineTop(i) + 1), f, (float) (getLineBottom(i) - 1), Direction.CW);
            }
            top = getLineTop(endline) + 1;
            bottom = getLineBottom(endline) - 1;
            addSelection(endline, getLineStart(endline), end, top, bottom, dest);
            if (getParagraphDirection(endline) == -1) {
                dest.addRect(width, (float) top, getLineRight(endline), (float) bottom, Direction.CW);
                return;
            }
            dest.addRect(0.0f, (float) top, getLineLeft(endline), (float) bottom, Direction.CW);
        }
    }

    public final Alignment getParagraphAlignment(int line) {
        Alignment align = this.mAlignment;
        if (!this.mSpannedText) {
            return align;
        }
        AlignmentSpan[] spans = (AlignmentSpan[]) getParagraphSpans(this.mText, getLineStart(line), getLineEnd(line), AlignmentSpan.class);
        int spanLength = spans.length;
        if (spanLength > 0) {
            return spans[spanLength - 1].getAlignment();
        }
        return align;
    }

    public final int getParagraphLeft(int line) {
        if (getParagraphDirection(line) == -1 || !this.mSpannedText) {
            return 0;
        }
        return getParagraphLeadingMargin(line);
    }

    public final int getParagraphRight(int line) {
        int right = this.mWidth;
        return (getParagraphDirection(line) == 1 || !this.mSpannedText) ? right : right - getParagraphLeadingMargin(line);
    }

    private int getParagraphLeadingMargin(int line) {
        if (!this.mSpannedText) {
            return 0;
        }
        Spanned spanned = this.mText;
        int lineStart = getLineStart(line);
        LeadingMarginSpan[] spans = (LeadingMarginSpan[]) getParagraphSpans(spanned, lineStart, spanned.nextSpanTransition(lineStart, getLineEnd(line), LeadingMarginSpan.class), LeadingMarginSpan.class);
        if (spans.length == 0) {
            return 0;
        }
        int margin = 0;
        boolean isFirstParaLine = lineStart == 0 || spanned.charAt(lineStart - 1) == '\n';
        boolean useFirstLineMargin = isFirstParaLine;
        for (int i = 0; i < spans.length; i++) {
            if (spans[i] instanceof LeadingMarginSpan2) {
                int i2;
                if (line < getLineForOffset(spanned.getSpanStart(spans[i])) + ((LeadingMarginSpan2) spans[i]).getLeadingMarginLineCount()) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                useFirstLineMargin |= i2;
            }
        }
        for (LeadingMarginSpan span : spans) {
            margin += span.getLeadingMargin(useFirstLineMargin);
        }
        return margin;
    }

    static float measurePara(TextPaint paint, CharSequence text, int start, int end) {
        MeasuredText mt = MeasuredText.obtain();
        TextLine tl = TextLine.obtain();
        try {
            Directions directions;
            int dir;
            float abs;
            mt.setPara(text, start, end, TextDirectionHeuristics.FIRSTSTRONG_LTR, null);
            if (mt.mEasy) {
                directions = DIRS_ALL_LEFT_TO_RIGHT;
                dir = 1;
            } else {
                directions = AndroidBidi.directions(mt.mDir, mt.mLevels, 0, mt.mChars, 0, mt.mLen);
                dir = mt.mDir;
            }
            char[] chars = mt.mChars;
            int len = mt.mLen;
            boolean hasTabs = false;
            TabStops tabStops = null;
            int margin = 0;
            if (text instanceof Spanned) {
                for (LeadingMarginSpan lms : (LeadingMarginSpan[]) getParagraphSpans((Spanned) text, start, end, LeadingMarginSpan.class)) {
                    margin += lms.getLeadingMargin(true);
                }
            }
            for (int i = 0; i < len; i++) {
                if (chars[i] == '\t') {
                    hasTabs = true;
                    if (text instanceof Spanned) {
                        Spanned spanned = (Spanned) text;
                        TabStopSpan[] spans = (TabStopSpan[]) getParagraphSpans(spanned, start, spanned.nextSpanTransition(start, end, TabStopSpan.class), TabStopSpan.class);
                        if (spans.length > 0) {
                            tabStops = new TabStops(20, spans);
                        }
                    }
                    tl.set(paint, text, start, end, dir, directions, hasTabs, tabStops);
                    abs = ((float) margin) + Math.abs(tl.metrics(null));
                    return abs;
                }
            }
            tl.set(paint, text, start, end, dir, directions, hasTabs, tabStops);
            abs = ((float) margin) + Math.abs(tl.metrics(null));
            return abs;
        } finally {
            TextLine.recycle(tl);
            MeasuredText.recycle(mt);
        }
    }

    static float nextTab(CharSequence text, int start, int end, float h, Object[] tabs) {
        float nh = AutoScrollHelper.NO_MAX;
        boolean alltabs = false;
        if (text instanceof Spanned) {
            if (tabs == null) {
                tabs = getParagraphSpans((Spanned) text, start, end, TabStopSpan.class);
                alltabs = true;
            }
            int i = 0;
            while (i < tabs.length) {
                if (alltabs || (tabs[i] instanceof TabStopSpan)) {
                    int where = ((TabStopSpan) tabs[i]).getTabStop();
                    if (((float) where) < nh && ((float) where) > h) {
                        nh = (float) where;
                    }
                }
                i++;
            }
            if (nh != AutoScrollHelper.NO_MAX) {
                return nh;
            }
        }
        return (float) (((int) ((h + 20.0f) / 20.0f)) * 20);
    }

    protected final boolean isSpanned() {
        return this.mSpannedText;
    }

    static <T> T[] getParagraphSpans(Spanned text, int start, int end, Class<T> type) {
        if (start != end || start <= 0) {
            return text.getSpans(start, end, type);
        }
        return ArrayUtils.emptyArray(type);
    }

    private char getEllipsisChar(TruncateAt method) {
        return method == TruncateAt.END_SMALL ? TextUtils.ELLIPSIS_TWO_DOTS[0] : TextUtils.ELLIPSIS_NORMAL[0];
    }

    private void ellipsize(int start, int end, int line, char[] dest, int destoff, TruncateAt method) {
        int ellipsisCount = getEllipsisCount(line);
        if (ellipsisCount != 0) {
            int ellipsisStart = getEllipsisStart(line);
            int linestart = getLineStart(line);
            for (int i = ellipsisStart; i < ellipsisStart + ellipsisCount; i++) {
                char c;
                if (i == ellipsisStart) {
                    c = getEllipsisChar(method);
                } else {
                    c = '﻿';
                }
                int a = i + linestart;
                if (a >= start && a < end) {
                    dest[(destoff + a) - start] = c;
                }
            }
        }
    }

    public boolean getHighContastTextMode() {
        return this.mHighContrastTextMode;
    }

    public void setHighContastTextMode(boolean hctMode) {
        this.mHighContrastTextMode = hctMode;
    }
}
