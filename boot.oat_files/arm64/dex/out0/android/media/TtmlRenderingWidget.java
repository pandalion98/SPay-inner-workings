package android.media;

import android.content.Context;
import android.graphics.Color;
import android.media.SubtitleTrack.Cue;
import android.media.SubtitleTrack.RenderingWidget;
import android.media.SubtitleTrack.RenderingWidget.OnChangedListener;
import android.net.ProxyInfo;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.accessibility.CaptioningManager;
import android.view.accessibility.CaptioningManager.CaptionStyle;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.LinkedList;
import java.util.Vector;

/* compiled from: TtmlRenderer */
class TtmlRenderingWidget extends LinearLayout implements RenderingWidget {
    private static final float LINE_HEIGHT_RATIO = 0.0533f;
    private CaptioningManager mCaptionManager;
    private CaptionStyle mCaptionStyle;
    private OnChangedListener mListener;
    private LinkedList<CustomTextView> mTextViewSet;

    /* compiled from: TtmlRenderer */
    private static class CustomTextView extends TextView {
        private String TAG = "CustomTextView";
        private TtmlCue mTtmlCue;

        public CustomTextView(Context context) {
            super(context);
        }

        public void setTtmlCue(TtmlCue cue) {
            this.mTtmlCue = cue;
        }

        public void setSize(int width, int height) {
            measure(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
            layout(0, 0, width, height);
        }
    }

    public TtmlRenderingWidget(Context context) {
        this(context, null);
    }

    public TtmlRenderingWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TtmlRenderingWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TtmlRenderingWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mTextViewSet = new LinkedList();
        setLayerType(1, null);
        this.mCaptionManager = (CaptioningManager) context.getSystemService(Context.CAPTIONING_SERVICE);
        this.mCaptionStyle = this.mCaptionManager.getUserStyle();
        CustomTextView customTextView = new CustomTextView(context);
        customTextView.setGravity(81);
        this.mTextViewSet.addLast(customTextView);
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
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int viewportWidth = r - l;
        int viewportHeight = b - t;
    }

    public int applyOpacity(int color, int opacity) {
        return Color.argb((opacity * 255) / 100, Color.red(color), Color.green(color), Color.blue(color));
    }

    public void setActiveCues(Vector<Cue> activeCues) {
        int i;
        removeAllViews();
        int viewCount = this.mTextViewSet.size();
        for (i = 0; i < viewCount - 1; i++) {
            this.mTextViewSet.removeLast();
        }
        int count = activeCues.size();
        String subtitleText = ProxyInfo.LOCAL_EXCL_LIST;
        for (i = 0; i < count; i++) {
            TtmlCue cue = (TtmlCue) activeCues.get(i);
            if (i > 0) {
                subtitleText = subtitleText + "\n";
            }
            subtitleText = (subtitleText + TtmlUtils.applySpacePolicy(cue.mText, true)) + "\n";
            Spannable textSpan = new SpannableString(subtitleText);
            float fontSize = (this.mCaptionManager.getFontScale() * ((float) getHeight())) * LINE_HEIGHT_RATIO;
            textSpan.setSpan(new BackgroundColorSpan(this.mCaptionStyle.backgroundColor), 0, subtitleText.length(), 33);
            ((CustomTextView) this.mTextViewSet.get(0)).setGravity(81);
            ((CustomTextView) this.mTextViewSet.get(0)).setText(textSpan);
            ((CustomTextView) this.mTextViewSet.get(0)).setTextSize(fontSize);
            ((CustomTextView) this.mTextViewSet.get(0)).setTextSize((fontSize * fontSize) / ((CustomTextView) this.mTextViewSet.get(0)).getTextSize());
            ((CustomTextView) this.mTextViewSet.get(0)).setTextColor(this.mCaptionStyle.foregroundColor);
            addView((View) this.mTextViewSet.get(0), -1, -1);
        }
        setSize(getWidth(), getHeight());
        if (this.mListener != null) {
            this.mListener.onChanged(this);
        }
    }
}
