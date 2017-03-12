package android.text;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.text.Layout.Directions;
import android.text.style.CharacterStyle;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;
import com.android.internal.util.ArrayUtils;

class TextLine {
    private static final boolean DEBUG = false;
    private static final int TAB_INCREMENT = 20;
    private static final TextLine[] sCached = new TextLine[3];
    private final SpanSet<CharacterStyle> mCharacterStyleSpanSet = new SpanSet(CharacterStyle.class);
    private char[] mChars;
    private boolean mCharsValid;
    private int mDir;
    private Directions mDirections;
    private boolean mHasTabs;
    private int mLen;
    private final SpanSet<MetricAffectingSpan> mMetricAffectingSpanSpanSet = new SpanSet(MetricAffectingSpan.class);
    private TextPaint mPaint;
    private final SpanSet<ReplacementSpan> mReplacementSpanSpanSet = new SpanSet(ReplacementSpan.class);
    private Spanned mSpanned;
    private int mStart;
    private TabStops mTabs;
    private CharSequence mText;
    private final TextPaint mWorkPaint = new TextPaint();

    TextLine() {
    }

    static TextLine obtain() {
        synchronized (sCached) {
            int i = sCached.length;
            do {
                i--;
                if (i < 0) {
                    return new TextLine();
                }
            } while (sCached[i] == null);
            TextLine tl = sCached[i];
            sCached[i] = null;
            return tl;
        }
    }

    static TextLine recycle(TextLine tl) {
        tl.mText = null;
        tl.mPaint = null;
        tl.mDirections = null;
        tl.mSpanned = null;
        tl.mTabs = null;
        tl.mChars = null;
        tl.mMetricAffectingSpanSpanSet.recycle();
        tl.mCharacterStyleSpanSet.recycle();
        tl.mReplacementSpanSpanSet.recycle();
        synchronized (sCached) {
            for (int i = 0; i < sCached.length; i++) {
                if (sCached[i] == null) {
                    sCached[i] = tl;
                    break;
                }
            }
        }
        return null;
    }

    void set(TextPaint paint, CharSequence text, int start, int limit, int dir, Directions directions, boolean hasTabs, TabStops tabStops) {
        this.mPaint = paint;
        this.mText = text;
        this.mStart = start;
        this.mLen = limit - start;
        this.mDir = dir;
        this.mDirections = directions;
        if (this.mDirections == null) {
            throw new IllegalArgumentException("Directions cannot be null");
        }
        this.mHasTabs = hasTabs;
        this.mSpanned = null;
        boolean hasReplacement = false;
        if (text instanceof Spanned) {
            this.mSpanned = (Spanned) text;
            this.mReplacementSpanSpanSet.init(this.mSpanned, start, limit);
            hasReplacement = this.mReplacementSpanSpanSet.numberOfSpans > 0;
        }
        boolean z = hasReplacement || hasTabs || directions != Layout.DIRS_ALL_LEFT_TO_RIGHT;
        this.mCharsValid = z;
        if (this.mCharsValid) {
            if (this.mChars == null || this.mChars.length < this.mLen) {
                this.mChars = ArrayUtils.newUnpaddedCharArray(this.mLen);
            }
            TextUtils.getChars(text, start, limit, this.mChars, 0);
            if (hasReplacement) {
                char[] chars = this.mChars;
                int i = start;
                while (i < limit) {
                    int inext = this.mReplacementSpanSpanSet.getNextTransition(i, limit);
                    if (this.mReplacementSpanSpanSet.hasSpansIntersecting(i, inext)) {
                        chars[i - start] = '￼';
                        int e = inext - start;
                        for (int j = (i - start) + 1; j < e; j++) {
                            chars[j] = '﻿';
                        }
                    }
                    i = inext;
                }
            }
        }
        this.mTabs = tabStops;
    }

    void draw(Canvas c, float x, int top, int y, int bottom) {
        if (!this.mHasTabs) {
            if (this.mDirections == Layout.DIRS_ALL_LEFT_TO_RIGHT) {
                drawRun(c, 0, this.mLen, false, x, top, y, bottom, false);
                return;
            } else if (this.mDirections == Layout.DIRS_ALL_RIGHT_TO_LEFT) {
                drawRun(c, 0, this.mLen, true, x, top, y, bottom, false);
                return;
            }
        }
        float h = 0.0f;
        int[] runs = this.mDirections.mDirections;
        RectF emojiRect = null;
        int lastRunIndex = runs.length - 2;
        int i = 0;
        while (i < runs.length) {
            int runStart = runs[i];
            int runLimit = runStart + (runs[i + 1] & 67108863);
            if (runLimit > this.mLen) {
                runLimit = this.mLen;
            }
            boolean runIsRtl = (runs[i + 1] & 67108864) != 0;
            int segstart = runStart;
            int j = this.mHasTabs ? runStart : runLimit;
            while (j <= runLimit) {
                int codept = 0;
                Bitmap bm = null;
                if (this.mHasTabs && j < runLimit) {
                    codept = this.mChars[j];
                    if (codept >= 55296 && codept < 56320 && j + 1 < runLimit) {
                        codept = Character.codePointAt(this.mChars, j);
                        if (codept >= Layout.MIN_EMOJI && codept <= Layout.MAX_EMOJI) {
                            bm = Layout.EMOJI_FACTORY.getBitmapFromAndroidPua(codept);
                        } else if (codept > 65535) {
                            j++;
                            j++;
                        }
                    }
                }
                if (j == runLimit || codept == 9 || bm != null) {
                    float f = x + h;
                    boolean z = (i == lastRunIndex && j == this.mLen) ? false : true;
                    h += drawRun(c, segstart, j, runIsRtl, f, top, y, bottom, z);
                    if (codept == 9) {
                        h = ((float) this.mDir) * nextTab(((float) this.mDir) * h);
                    } else if (bm != null) {
                        float bmAscent = ascent(j);
                        float width = ((float) bm.getWidth()) * ((-bmAscent) / ((float) bm.getHeight()));
                        if (emojiRect == null) {
                            emojiRect = new RectF();
                        }
                        emojiRect.set(x + h, ((float) y) + bmAscent, (x + h) + width, (float) y);
                        c.drawBitmap(bm, null, emojiRect, this.mPaint);
                        h += width;
                        j++;
                    }
                    segstart = j + 1;
                    j++;
                } else {
                    j++;
                }
            }
            i += 2;
        }
    }

    float metrics(FontMetricsInt fmi) {
        return measure(this.mLen, false, fmi);
    }

    float measure(int offset, boolean trailing, FontMetricsInt fmi) {
        int target;
        if (trailing) {
            target = offset - 1;
        } else {
            target = offset;
        }
        if (target < 0) {
            return 0.0f;
        }
        float h = 0.0f;
        if (!this.mHasTabs) {
            if (this.mDirections == Layout.DIRS_ALL_LEFT_TO_RIGHT) {
                return measureRun(0, offset, this.mLen, false, fmi);
            } else if (this.mDirections == Layout.DIRS_ALL_RIGHT_TO_LEFT) {
                return measureRun(0, offset, this.mLen, true, fmi);
            }
        }
        char[] chars = this.mChars;
        int[] runs = this.mDirections.mDirections;
        for (int i = 0; i < runs.length; i += 2) {
            int runStart = runs[i];
            int runLimit = runStart + (runs[i + 1] & 67108863);
            if (runLimit > this.mLen) {
                runLimit = this.mLen;
            }
            boolean runIsRtl = (runs[i + 1] & 67108864) != 0;
            int segstart = runStart;
            int j = this.mHasTabs ? runStart : runLimit;
            while (j <= runLimit) {
                int codept = 0;
                Bitmap bm = null;
                if (this.mHasTabs && j < runLimit) {
                    codept = chars[j];
                    if (codept >= 55296 && codept < 56320 && j + 1 < runLimit) {
                        codept = Character.codePointAt(chars, j);
                        if (codept >= Layout.MIN_EMOJI && codept <= Layout.MAX_EMOJI) {
                            bm = Layout.EMOJI_FACTORY.getBitmapFromAndroidPua(codept);
                        } else if (codept > 65535) {
                            j++;
                            j++;
                        }
                    }
                }
                if (j == runLimit || codept == 9 || bm != null) {
                    boolean inSegment = target >= segstart && target < j;
                    boolean advance = (this.mDir == -1) == runIsRtl;
                    if (inSegment && advance) {
                        return h + measureRun(segstart, offset, j, runIsRtl, fmi);
                    }
                    float w = measureRun(segstart, j, j, runIsRtl, fmi);
                    if (!advance) {
                        w = -w;
                    }
                    h += w;
                    if (inSegment) {
                        return h + measureRun(segstart, offset, j, runIsRtl, null);
                    }
                    if (codept == 9) {
                        if (offset == j) {
                            return h;
                        }
                        h = ((float) this.mDir) * nextTab(((float) this.mDir) * h);
                        if (target == j) {
                            return h;
                        }
                    }
                    if (bm != null) {
                        h += ((float) this.mDir) * ((((float) bm.getWidth()) * (-ascent(j))) / ((float) bm.getHeight()));
                        j++;
                    }
                    segstart = j + 1;
                    j++;
                } else {
                    j++;
                }
            }
        }
        return h;
    }

    private float drawRun(Canvas c, int start, int limit, boolean runIsRtl, float x, int top, int y, int bottom, boolean needWidth) {
        if ((this.mDir == 1) != runIsRtl) {
            return handleRun(start, limit, limit, runIsRtl, c, x, top, y, bottom, null, needWidth);
        }
        float w = -measureRun(start, limit, limit, runIsRtl, null);
        handleRun(start, limit, limit, runIsRtl, c, x + w, top, y, bottom, null, false);
        return w;
    }

    private float measureRun(int start, int offset, int limit, boolean runIsRtl, FontMetricsInt fmi) {
        return handleRun(start, offset, limit, runIsRtl, null, 0.0f, 0, 0, 0, fmi, true);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    int getOffsetToLeftRightOf(int r31, boolean r32) {
        /*
        r30 = this;
        r17 = 0;
        r0 = r30;
        r0 = r0.mLen;
        r16 = r0;
        r0 = r30;
        r2 = r0.mDir;
        r7 = -1;
        if (r2 != r7) goto L_0x007c;
    L_0x000f:
        r21 = 1;
    L_0x0011:
        r0 = r30;
        r2 = r0.mDirections;
        r0 = r2.mDirections;
        r28 = r0;
        r27 = 0;
        r4 = r17;
        r5 = r16;
        r18 = -1;
        r29 = 0;
        r0 = r31;
        r1 = r17;
        if (r0 != r1) goto L_0x007f;
    L_0x0029:
        r3 = -2;
    L_0x002a:
        r0 = r32;
        r1 = r21;
        if (r0 != r1) goto L_0x0137;
    L_0x0030:
        r8 = 1;
    L_0x0031:
        if (r8 == 0) goto L_0x013a;
    L_0x0033:
        r2 = 2;
    L_0x0034:
        r10 = r3 + r2;
        if (r10 < 0) goto L_0x0159;
    L_0x0038:
        r0 = r28;
        r2 = r0.length;
        if (r10 >= r2) goto L_0x0159;
    L_0x003d:
        r2 = r28[r10];
        r11 = r17 + r2;
        r2 = r10 + 1;
        r2 = r28[r2];
        r7 = 67108863; // 0x3ffffff float:1.5046327E-36 double:3.31561837E-316;
        r2 = r2 & r7;
        r12 = r11 + r2;
        r0 = r16;
        if (r12 <= r0) goto L_0x0051;
    L_0x004f:
        r12 = r16;
    L_0x0051:
        r2 = r10 + 1;
        r2 = r28[r2];
        r2 = r2 >>> 26;
        r20 = r2 & 63;
        r2 = r20 & 1;
        if (r2 == 0) goto L_0x013d;
    L_0x005d:
        r13 = 1;
    L_0x005e:
        r0 = r32;
        if (r0 != r13) goto L_0x0140;
    L_0x0062:
        r8 = 1;
    L_0x0063:
        r2 = -1;
        r0 = r18;
        if (r0 != r2) goto L_0x0149;
    L_0x0068:
        if (r8 == 0) goto L_0x0143;
    L_0x006a:
        r14 = r11;
    L_0x006b:
        r9 = r30;
        r15 = r8;
        r18 = r9.getOffsetBeforeAfter(r10, r11, r12, r13, r14, r15);
        if (r8 == 0) goto L_0x0146;
    L_0x0074:
        r0 = r18;
        if (r0 != r12) goto L_0x0153;
    L_0x0078:
        r3 = r10;
        r27 = r20;
        goto L_0x002a;
    L_0x007c:
        r21 = 0;
        goto L_0x0011;
    L_0x007f:
        r0 = r31;
        r1 = r16;
        if (r0 != r1) goto L_0x0089;
    L_0x0085:
        r0 = r28;
        r3 = r0.length;
        goto L_0x002a;
    L_0x0089:
        r3 = 0;
    L_0x008a:
        r0 = r28;
        r2 = r0.length;
        if (r3 >= r2) goto L_0x00fc;
    L_0x008f:
        r2 = r28[r3];
        r4 = r17 + r2;
        r0 = r31;
        if (r0 < r4) goto L_0x012b;
    L_0x0097:
        r2 = r3 + 1;
        r2 = r28[r2];
        r7 = 67108863; // 0x3ffffff float:1.5046327E-36 double:3.31561837E-316;
        r2 = r2 & r7;
        r5 = r4 + r2;
        r0 = r16;
        if (r5 <= r0) goto L_0x00a7;
    L_0x00a5:
        r5 = r16;
    L_0x00a7:
        r0 = r31;
        if (r0 >= r5) goto L_0x012b;
    L_0x00ab:
        r2 = r3 + 1;
        r2 = r28[r2];
        r2 = r2 >>> 26;
        r27 = r2 & 63;
        r0 = r31;
        if (r0 != r4) goto L_0x00fc;
    L_0x00b7:
        r22 = r31 + -1;
        r23 = 0;
    L_0x00bb:
        r0 = r28;
        r2 = r0.length;
        r0 = r23;
        if (r0 >= r2) goto L_0x00fc;
    L_0x00c2:
        r2 = r28[r23];
        r26 = r17 + r2;
        r0 = r22;
        r1 = r26;
        if (r0 < r1) goto L_0x0128;
    L_0x00cc:
        r2 = r23 + 1;
        r2 = r28[r2];
        r7 = 67108863; // 0x3ffffff float:1.5046327E-36 double:3.31561837E-316;
        r2 = r2 & r7;
        r25 = r26 + r2;
        r0 = r25;
        r1 = r16;
        if (r0 <= r1) goto L_0x00de;
    L_0x00dc:
        r25 = r16;
    L_0x00de:
        r0 = r22;
        r1 = r25;
        if (r0 >= r1) goto L_0x0128;
    L_0x00e4:
        r2 = r23 + 1;
        r2 = r28[r2];
        r2 = r2 >>> 26;
        r24 = r2 & 63;
        r0 = r24;
        r1 = r27;
        if (r0 >= r1) goto L_0x0128;
    L_0x00f2:
        r3 = r23;
        r27 = r24;
        r4 = r26;
        r5 = r25;
        r29 = 1;
    L_0x00fc:
        r0 = r28;
        r2 = r0.length;
        if (r3 == r2) goto L_0x002a;
    L_0x0101:
        r2 = r27 & 1;
        if (r2 == 0) goto L_0x012f;
    L_0x0105:
        r6 = 1;
    L_0x0106:
        r0 = r32;
        if (r0 != r6) goto L_0x0131;
    L_0x010a:
        r8 = 1;
    L_0x010b:
        if (r8 == 0) goto L_0x0133;
    L_0x010d:
        r2 = r5;
    L_0x010e:
        r0 = r31;
        if (r0 != r2) goto L_0x0116;
    L_0x0112:
        r0 = r29;
        if (r8 == r0) goto L_0x002a;
    L_0x0116:
        r2 = r30;
        r7 = r31;
        r18 = r2.getOffsetBeforeAfter(r3, r4, r5, r6, r7, r8);
        if (r8 == 0) goto L_0x0135;
    L_0x0120:
        r2 = r5;
    L_0x0121:
        r0 = r18;
        if (r0 == r2) goto L_0x002a;
    L_0x0125:
        r19 = r18;
    L_0x0127:
        return r19;
    L_0x0128:
        r23 = r23 + 2;
        goto L_0x00bb;
    L_0x012b:
        r3 = r3 + 2;
        goto L_0x008a;
    L_0x012f:
        r6 = 0;
        goto L_0x0106;
    L_0x0131:
        r8 = 0;
        goto L_0x010b;
    L_0x0133:
        r2 = r4;
        goto L_0x010e;
    L_0x0135:
        r2 = r4;
        goto L_0x0121;
    L_0x0137:
        r8 = 0;
        goto L_0x0031;
    L_0x013a:
        r2 = -2;
        goto L_0x0034;
    L_0x013d:
        r13 = 0;
        goto L_0x005e;
    L_0x0140:
        r8 = 0;
        goto L_0x0063;
    L_0x0143:
        r14 = r12;
        goto L_0x006b;
    L_0x0146:
        r12 = r11;
        goto L_0x0074;
    L_0x0149:
        r0 = r20;
        r1 = r27;
        if (r0 >= r1) goto L_0x0153;
    L_0x014f:
        if (r8 == 0) goto L_0x0156;
    L_0x0151:
        r18 = r11;
    L_0x0153:
        r19 = r18;
        goto L_0x0127;
    L_0x0156:
        r18 = r12;
        goto L_0x0153;
    L_0x0159:
        r2 = -1;
        r0 = r18;
        if (r0 != r2) goto L_0x016a;
    L_0x015e:
        if (r8 == 0) goto L_0x0167;
    L_0x0160:
        r0 = r30;
        r2 = r0.mLen;
        r18 = r2 + 1;
    L_0x0166:
        goto L_0x0153;
    L_0x0167:
        r18 = -1;
        goto L_0x0166;
    L_0x016a:
        r0 = r18;
        r1 = r16;
        if (r0 > r1) goto L_0x0153;
    L_0x0170:
        if (r8 == 0) goto L_0x0175;
    L_0x0172:
        r18 = r16;
    L_0x0174:
        goto L_0x0153;
    L_0x0175:
        r18 = r17;
        goto L_0x0174;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.TextLine.getOffsetToLeftRightOf(int, boolean):int");
    }

    private int getOffsetBeforeAfter(int runIndex, int runStart, int runLimit, boolean runIsRtl, int offset, boolean after) {
        if (runIndex >= 0) {
            if (offset != (after ? this.mLen : 0)) {
                int spanLimit;
                TextPaint wp = this.mWorkPaint;
                wp.set(this.mPaint);
                int spanStart = runStart;
                if (this.mSpanned == null) {
                    spanLimit = runLimit;
                } else {
                    int target;
                    if (after) {
                        target = offset + 1;
                    } else {
                        target = offset;
                    }
                    int limit = this.mStart + runLimit;
                    while (true) {
                        spanLimit = this.mSpanned.nextSpanTransition(this.mStart + spanStart, limit, MetricAffectingSpan.class) - this.mStart;
                        if (spanLimit >= target) {
                            break;
                        }
                        spanStart = spanLimit;
                    }
                    MetricAffectingSpan[] spans = (MetricAffectingSpan[]) TextUtils.removeEmptySpans((MetricAffectingSpan[]) this.mSpanned.getSpans(this.mStart + spanStart, this.mStart + spanLimit, MetricAffectingSpan.class), this.mSpanned, MetricAffectingSpan.class);
                    if (spans.length > 0) {
                        ReplacementSpan replacement = null;
                        for (MetricAffectingSpan span : spans) {
                            if (span instanceof ReplacementSpan) {
                                replacement = (ReplacementSpan) span;
                            } else {
                                span.updateMeasureState(wp);
                            }
                        }
                        if (replacement != null) {
                            if (after) {
                                return spanLimit;
                            }
                            return spanStart;
                        }
                    }
                }
                int dir = runIsRtl ? 1 : 0;
                int cursorOpt = after ? 0 : 2;
                if (this.mCharsValid) {
                    return wp.getTextRunCursor(this.mChars, spanStart, spanLimit - spanStart, dir, offset, cursorOpt);
                }
                return wp.getTextRunCursor(this.mText, this.mStart + spanStart, this.mStart + spanLimit, dir, this.mStart + offset, cursorOpt) - this.mStart;
            }
        }
        if (after) {
            return TextUtils.getOffsetAfter(this.mText, this.mStart + offset) - this.mStart;
        }
        return TextUtils.getOffsetBefore(this.mText, this.mStart + offset) - this.mStart;
    }

    private static void expandMetricsFromPaint(FontMetricsInt fmi, TextPaint wp) {
        int previousTop = fmi.top;
        int previousAscent = fmi.ascent;
        int previousDescent = fmi.descent;
        int previousBottom = fmi.bottom;
        int previousLeading = fmi.leading;
        wp.getFontMetricsInt(fmi);
        updateMetrics(fmi, previousTop, previousAscent, previousDescent, previousBottom, previousLeading);
    }

    static void updateMetrics(FontMetricsInt fmi, int previousTop, int previousAscent, int previousDescent, int previousBottom, int previousLeading) {
        fmi.top = Math.min(fmi.top, previousTop);
        fmi.ascent = Math.min(fmi.ascent, previousAscent);
        fmi.descent = Math.max(fmi.descent, previousDescent);
        fmi.bottom = Math.max(fmi.bottom, previousBottom);
        fmi.leading = Math.max(fmi.leading, previousLeading);
    }

    private float handleText(TextPaint wp, int start, int end, int contextStart, int contextEnd, boolean runIsRtl, Canvas c, float x, int top, int y, int bottom, FontMetricsInt fmi, boolean needWidth) {
        if (fmi != null) {
            expandMetricsFromPaint(fmi, wp);
        }
        if (end - start == 0) {
            return 0.0f;
        }
        float ret = 0.0f;
        if (needWidth || !(c == null || (wp.bgColor == 0 && wp.underlineColor == 0 && !runIsRtl))) {
            if (this.mCharsValid) {
                ret = wp.getRunAdvance(this.mChars, start, end, contextStart, contextEnd, runIsRtl, end);
            } else {
                int delta = this.mStart;
                ret = wp.getRunAdvance(this.mText, delta + start, delta + end, delta + contextStart, delta + contextEnd, runIsRtl, delta + end);
            }
        }
        if (this.mText.length() == end && TextUtils.isRegionalCharHandling(this.mText.charAt(end - 1))) {
            ret += wp.measureText(" ");
        }
        if (c != null) {
            int previousColor;
            Style previousStyle;
            if (runIsRtl) {
                x -= ret;
            }
            if (wp.bgColor != 0) {
                previousColor = wp.getColor();
                previousStyle = wp.getStyle();
                wp.setColor(wp.bgColor);
                wp.setStyle(Style.FILL);
                c.drawRect(x, (float) top, x + ret, (float) bottom, wp);
                wp.setStyle(previousStyle);
                wp.setColor(previousColor);
            }
            if (wp.underlineColor != 0) {
                float underlineTop = ((float) (wp.baselineShift + y)) + (0.11111111f * wp.getTextSize());
                previousColor = wp.getColor();
                previousStyle = wp.getStyle();
                boolean previousAntiAlias = wp.isAntiAlias();
                wp.setStyle(Style.FILL);
                wp.setAntiAlias(true);
                wp.setColor(wp.underlineColor);
                c.drawRect(x, underlineTop, x + ret, underlineTop + wp.underlineThickness, wp);
                wp.setStyle(previousStyle);
                wp.setColor(previousColor);
                wp.setAntiAlias(previousAntiAlias);
            }
            drawTextRun(c, wp, start, end, contextStart, contextEnd, runIsRtl, x, y + wp.baselineShift);
        }
        if (runIsRtl) {
            return -ret;
        }
        return ret;
    }

    private float handleReplacement(ReplacementSpan replacement, TextPaint wp, int start, int limit, boolean runIsRtl, Canvas c, float x, int top, int y, int bottom, FontMetricsInt fmi, boolean needWidth) {
        float ret = 0.0f;
        int textStart = this.mStart + start;
        int textLimit = this.mStart + limit;
        if (needWidth || (c != null && runIsRtl)) {
            int previousTop = 0;
            int previousAscent = 0;
            int previousDescent = 0;
            int previousBottom = 0;
            int previousLeading = 0;
            boolean needUpdateMetrics = fmi != null;
            if (needUpdateMetrics) {
                previousTop = fmi.top;
                previousAscent = fmi.ascent;
                previousDescent = fmi.descent;
                previousBottom = fmi.bottom;
                previousLeading = fmi.leading;
            }
            ret = (float) replacement.getSize(wp, this.mText, textStart, textLimit, fmi);
            if (needUpdateMetrics) {
                updateMetrics(fmi, previousTop, previousAscent, previousDescent, previousBottom, previousLeading);
            }
        }
        if (c != null) {
            if (runIsRtl) {
                x -= ret;
            }
            replacement.draw(c, this.mText, textStart, textLimit, x, top, y, bottom, wp);
        }
        return runIsRtl ? -ret : ret;
    }

    private float handleRun(int start, int measureLimit, int limit, boolean runIsRtl, Canvas c, float x, int top, int y, int bottom, FontMetricsInt fmi, boolean needWidth) {
        TextPaint wp;
        if (start == measureLimit) {
            wp = this.mWorkPaint;
            wp.set(this.mPaint);
            if (fmi != null) {
                expandMetricsFromPaint(fmi, wp);
            }
            return 0.0f;
        } else if (this.mSpanned == null) {
            wp = this.mWorkPaint;
            wp.set(this.mPaint);
            mlimit = measureLimit;
            boolean z = needWidth || mlimit < measureLimit;
            return handleText(wp, start, mlimit, start, limit, runIsRtl, c, x, top, y, bottom, fmi, z);
        } else {
            this.mMetricAffectingSpanSpanSet.init(this.mSpanned, this.mStart + start, this.mStart + limit);
            this.mCharacterStyleSpanSet.init(this.mSpanned, this.mStart + start, this.mStart + limit);
            float originalX = x;
            int i = start;
            while (i < measureLimit) {
                wp = this.mWorkPaint;
                wp.set(this.mPaint);
                int inext = this.mMetricAffectingSpanSpanSet.getNextTransition(this.mStart + i, this.mStart + limit) - this.mStart;
                mlimit = Math.min(inext, measureLimit);
                ReplacementSpan replacement = null;
                int j = 0;
                while (j < this.mMetricAffectingSpanSpanSet.numberOfSpans) {
                    if (this.mMetricAffectingSpanSpanSet.spanStarts[j] < this.mStart + mlimit && this.mMetricAffectingSpanSpanSet.spanEnds[j] > this.mStart + i) {
                        MetricAffectingSpan span = ((MetricAffectingSpan[]) this.mMetricAffectingSpanSpanSet.spans)[j];
                        if (span instanceof ReplacementSpan) {
                            replacement = (ReplacementSpan) span;
                        } else {
                            span.updateDrawState(wp);
                        }
                    }
                    j++;
                }
                int i2;
                if (replacement != null) {
                    boolean z2;
                    if (needWidth || mlimit < measureLimit) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    x += handleReplacement(replacement, wp, i, mlimit, runIsRtl, c, x, top, y, bottom, fmi, z2);
                    i2 = j;
                } else {
                    i2 = i;
                    while (i2 < mlimit) {
                        boolean z3;
                        int jnext = this.mCharacterStyleSpanSet.getNextTransition(this.mStart + i2, this.mStart + mlimit) - this.mStart;
                        wp.set(this.mPaint);
                        int k = 0;
                        while (k < this.mCharacterStyleSpanSet.numberOfSpans) {
                            if (this.mCharacterStyleSpanSet.spanStarts[k] < this.mStart + jnext && this.mCharacterStyleSpanSet.spanEnds[k] > this.mStart + i2) {
                                ((CharacterStyle[]) this.mCharacterStyleSpanSet.spans)[k].updateDrawState(wp);
                            }
                            k++;
                        }
                        if (jnext < this.mLen) {
                            wp.setHyphenEdit(0);
                        }
                        if (needWidth || jnext < measureLimit) {
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                        x += handleText(wp, i2, jnext, i, inext, runIsRtl, c, x, top, y, bottom, fmi, z3);
                        i2 = jnext;
                    }
                }
                i = inext;
            }
            return x - originalX;
        }
    }

    private void drawTextRun(Canvas c, TextPaint wp, int start, int end, int contextStart, int contextEnd, boolean runIsRtl, float x, int y) {
        if (this.mCharsValid) {
            Canvas canvas = c;
            int i = start;
            int i2 = contextStart;
            canvas.drawTextRun(this.mChars, i, end - start, i2, contextEnd - contextStart, x, (float) y, runIsRtl, wp);
            return;
        }
        int delta = this.mStart;
        c.drawTextRun(this.mText, delta + start, delta + end, delta + contextStart, delta + contextEnd, x, (float) y, runIsRtl, wp);
    }

    float ascent(int pos) {
        if (this.mSpanned == null) {
            return this.mPaint.ascent();
        }
        pos += this.mStart;
        MetricAffectingSpan[] spans = (MetricAffectingSpan[]) this.mSpanned.getSpans(pos, pos + 1, MetricAffectingSpan.class);
        if (spans.length == 0) {
            return this.mPaint.ascent();
        }
        TextPaint wp = this.mWorkPaint;
        wp.set(this.mPaint);
        for (MetricAffectingSpan span : spans) {
            span.updateMeasureState(wp);
        }
        return wp.ascent();
    }

    float nextTab(float h) {
        if (this.mTabs != null) {
            return this.mTabs.nextTab(h);
        }
        return TabStops.nextDefaultStop(h, 20);
    }
}
