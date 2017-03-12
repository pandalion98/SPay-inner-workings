package android.media;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.SubtitleTrack.RenderingWidget;
import android.media.SubtitleTrack.RenderingWidget.OnChangedListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.accessibility.CaptioningManager;
import android.view.accessibility.CaptioningManager.CaptionStyle;
import android.view.accessibility.CaptioningManager.CaptioningChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

/* compiled from: ClosedCaptionRenderer */
class ClosedCaptionWidget extends ViewGroup implements RenderingWidget, DisplayListener {
    private static final CaptionStyle DEFAULT_CAPTION_STYLE = CaptionStyle.DEFAULT;
    private static final String TAG = "ClosedCaptionWidget";
    private static final String mDummyText = "1234567890123456789012345678901234";
    private static final Rect mTextBounds = new Rect();
    private CaptionStyle mCaptionStyle;
    private final CaptioningChangeListener mCaptioningListener;
    private CCLayout mClosedCaptionLayout;
    private boolean mHasChangeListener;
    private OnChangedListener mListener;
    private final CaptioningManager mManager;

    /* compiled from: ClosedCaptionRenderer */
    private static class CCLayout extends LinearLayout {
        private static final int MAX_ROWS = 15;
        private static final float SAFE_AREA_RATIO = 0.9f;
        private final CCLineBox[] mLineBoxes = new CCLineBox[15];

        CCLayout(Context context) {
            super(context);
            setGravity(8388611);
            setOrientation(1);
            for (int i = 0; i < 15; i++) {
                this.mLineBoxes[i] = new CCLineBox(getContext());
                addView(this.mLineBoxes[i], -2, -2);
            }
        }

        void setCaptionStyle(CaptionStyle captionStyle) {
            for (int i = 0; i < 15; i++) {
                this.mLineBoxes[i].setCaptionStyle(captionStyle);
            }
        }

        void update(SpannableStringBuilder[] textBuffer) {
            for (int i = 0; i < 15; i++) {
                if (textBuffer[i] != null) {
                    this.mLineBoxes[i].setText(textBuffer[i], BufferType.SPANNABLE);
                    this.mLineBoxes[i].setVisibility(0);
                } else {
                    this.mLineBoxes[i].setVisibility(4);
                }
            }
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int safeWidth = getMeasuredWidth();
            int safeHeight = getMeasuredHeight();
            if (safeWidth * 3 >= safeHeight * 4) {
                safeWidth = (safeHeight * 4) / 3;
            } else {
                safeHeight = (safeWidth * 3) / 4;
            }
            safeWidth = (int) (((float) safeWidth) * SAFE_AREA_RATIO);
            int lineHeightMeasureSpec = MeasureSpec.makeMeasureSpec(((int) (((float) safeHeight) * SAFE_AREA_RATIO)) / 15, 1073741824);
            int lineWidthMeasureSpec = MeasureSpec.makeMeasureSpec(safeWidth, 1073741824);
            for (int i = 0; i < 15; i++) {
                this.mLineBoxes[i].measure(lineWidthMeasureSpec, lineHeightMeasureSpec);
            }
        }

        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int safeWidth;
            int safeHeight;
            int viewPortWidth = r - l;
            int viewPortHeight = b - t;
            if (viewPortWidth * 3 >= viewPortHeight * 4) {
                safeWidth = (viewPortHeight * 4) / 3;
                safeHeight = viewPortHeight;
            } else {
                safeWidth = viewPortWidth;
                safeHeight = (viewPortWidth * 3) / 4;
            }
            safeWidth = (int) (((float) safeWidth) * SAFE_AREA_RATIO);
            safeHeight = (int) (((float) safeHeight) * SAFE_AREA_RATIO);
            int left = (viewPortWidth - safeWidth) / 2;
            int top = (viewPortHeight - safeHeight) / 2;
            for (int i = 0; i < 15; i++) {
                this.mLineBoxes[i].layout(left, ((safeHeight * i) / 15) + top, left + safeWidth, (((i + 1) * safeHeight) / 15) + top);
            }
        }
    }

    /* compiled from: ClosedCaptionRenderer */
    private static class CCLineBox extends TextView {
        private static final float EDGE_OUTLINE_RATIO = 0.1f;
        private static final float EDGE_SHADOW_RATIO = 0.05f;
        private static final float FONT_PADDING_RATIO = 0.75f;
        private int mBgColor = -16777216;
        private int mEdgeColor = 0;
        private int mEdgeType = 0;
        private float mOutlineWidth;
        private float mShadowOffset;
        private float mShadowRadius;
        private int mTextColor = -1;

        CCLineBox(Context context) {
            super(context);
            setGravity(17);
            setBackgroundColor(0);
            setTextColor(-1);
            setTypeface(Typeface.MONOSPACE);
            setVisibility(4);
            Resources res = getContext().getResources();
            this.mOutlineWidth = (float) res.getDimensionPixelSize(17105030);
            this.mShadowRadius = (float) res.getDimensionPixelSize(17105028);
            this.mShadowOffset = (float) res.getDimensionPixelSize(17105029);
        }

        void setCaptionStyle(CaptionStyle captionStyle) {
            this.mTextColor = captionStyle.foregroundColor;
            this.mBgColor = captionStyle.backgroundColor;
            this.mEdgeType = captionStyle.edgeType;
            this.mEdgeColor = captionStyle.edgeColor;
            setTextColor(this.mTextColor);
            if (this.mEdgeType == 2) {
                setShadowLayer(this.mShadowRadius, this.mShadowOffset, this.mShadowOffset, this.mEdgeColor);
            } else {
                setShadowLayer(0.0f, 0.0f, 0.0f, 0);
            }
            invalidate();
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            float fontSize = ((float) MeasureSpec.getSize(heightMeasureSpec)) * FONT_PADDING_RATIO;
            setTextSize(0, fontSize);
            this.mOutlineWidth = (EDGE_OUTLINE_RATIO * fontSize) + 1.0f;
            this.mShadowRadius = (EDGE_SHADOW_RATIO * fontSize) + 1.0f;
            this.mShadowOffset = this.mShadowRadius;
            setScaleX(1.0f);
            getPaint().getTextBounds(ClosedCaptionWidget.mDummyText, 0, ClosedCaptionWidget.mDummyText.length(), ClosedCaptionWidget.mTextBounds);
            setScaleX(((float) MeasureSpec.getSize(widthMeasureSpec)) / ((float) ClosedCaptionWidget.mTextBounds.width()));
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        protected void onDraw(Canvas c) {
            if (this.mEdgeType == -1 || this.mEdgeType == 0 || this.mEdgeType == 2) {
                super.onDraw(c);
            } else if (this.mEdgeType == 1) {
                drawEdgeOutline(c);
            } else {
                drawEdgeRaisedOrDepressed(c);
            }
        }

        private void drawEdgeOutline(Canvas c) {
            TextPaint textPaint = getPaint();
            Style previousStyle = textPaint.getStyle();
            Join previousJoin = textPaint.getStrokeJoin();
            float previousWidth = textPaint.getStrokeWidth();
            setTextColor(this.mEdgeColor);
            textPaint.setStyle(Style.FILL_AND_STROKE);
            textPaint.setStrokeJoin(Join.ROUND);
            textPaint.setStrokeWidth(this.mOutlineWidth);
            super.onDraw(c);
            setTextColor(this.mTextColor);
            textPaint.setStyle(previousStyle);
            textPaint.setStrokeJoin(previousJoin);
            textPaint.setStrokeWidth(previousWidth);
            setBackgroundSpans(0);
            super.onDraw(c);
            setBackgroundSpans(this.mBgColor);
        }

        private void drawEdgeRaisedOrDepressed(Canvas c) {
            boolean raised;
            int colorDown = -1;
            TextPaint textPaint = getPaint();
            Style previousStyle = textPaint.getStyle();
            textPaint.setStyle(Style.FILL);
            if (this.mEdgeType == 3) {
                raised = true;
            } else {
                raised = false;
            }
            int colorUp = raised ? -1 : this.mEdgeColor;
            if (raised) {
                colorDown = this.mEdgeColor;
            }
            float offset = this.mShadowRadius / 2.0f;
            setShadowLayer(this.mShadowRadius, -offset, -offset, colorUp);
            super.onDraw(c);
            setBackgroundSpans(0);
            setShadowLayer(this.mShadowRadius, offset, offset, colorDown);
            super.onDraw(c);
            textPaint.setStyle(previousStyle);
            setBackgroundSpans(this.mBgColor);
        }

        private void setBackgroundSpans(int color) {
            CharSequence text = getText();
            if (text instanceof Spannable) {
                Spannable spannable = (Spannable) text;
                MutableBackgroundColorSpan[] bgSpans = (MutableBackgroundColorSpan[]) spannable.getSpans(0, spannable.length(), MutableBackgroundColorSpan.class);
                for (MutableBackgroundColorSpan backgroundColor : bgSpans) {
                    backgroundColor.setBackgroundColor(color);
                }
            }
        }
    }

    public ClosedCaptionWidget(Context context) {
        this(context, null);
    }

    public ClosedCaptionWidget(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public ClosedCaptionWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCaptioningListener = new CaptioningChangeListener() {
            public void onUserStyleChanged(CaptionStyle userStyle) {
                ClosedCaptionWidget.this.mCaptionStyle = ClosedCaptionWidget.DEFAULT_CAPTION_STYLE.applyStyle(userStyle);
                ClosedCaptionWidget.this.mClosedCaptionLayout.setCaptionStyle(ClosedCaptionWidget.this.mCaptionStyle);
            }
        };
        setLayerType(1, null);
        this.mManager = (CaptioningManager) context.getSystemService(Context.CAPTIONING_SERVICE);
        this.mCaptionStyle = DEFAULT_CAPTION_STYLE.applyStyle(this.mManager.getUserStyle());
        this.mClosedCaptionLayout = new CCLayout(context);
        this.mClosedCaptionLayout.setCaptionStyle(this.mCaptionStyle);
        addView(this.mClosedCaptionLayout, -1, -1);
        requestLayout();
    }

    public void setOnChangedListener(OnChangedListener listener) {
        this.mListener = listener;
    }

    public void setSize(int width, int height) {
        measure(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
        layout(0, 0, width, height);
    }

    public void setVisible(boolean visible) {
        if (visible) {
            setVisibility(0);
        } else {
            setVisibility(8);
        }
        manageChangeListener();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        manageChangeListener();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        manageChangeListener();
    }

    public void onDisplayChanged(SpannableStringBuilder[] styledTexts) {
        this.mClosedCaptionLayout.update(styledTexts);
        if (this.mListener != null) {
            this.mListener.onChanged(this);
        }
    }

    public CaptionStyle getCaptionStyle() {
        return this.mCaptionStyle;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mClosedCaptionLayout.measure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.mClosedCaptionLayout.layout(l, t, r, b);
    }

    private void manageChangeListener() {
        boolean needsListener = isAttachedToWindow() && getVisibility() == 0;
        if (this.mHasChangeListener != needsListener) {
            this.mHasChangeListener = needsListener;
            if (needsListener) {
                this.mManager.addCaptioningChangeListener(this.mCaptioningListener);
            } else {
                this.mManager.removeCaptioningChangeListener(this.mCaptioningListener);
            }
        }
    }
}
