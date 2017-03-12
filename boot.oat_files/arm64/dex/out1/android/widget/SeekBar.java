package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.android.internal.R;

public class SeekBar extends AbsSeekBar {
    private OnSeekBarChangeListener mOnSeekBarChangeListener;
    private OnSeekBarHoverListener mOnSeekBarHoverListener;

    public interface OnSeekBarChangeListener {
        void onProgressChanged(SeekBar seekBar, int i, boolean z);

        void onStartTrackingTouch(SeekBar seekBar);

        void onStopTrackingTouch(SeekBar seekBar);
    }

    public interface OnSeekBarHoverListener {
        void onHoverChanged(SeekBar seekBar, int i, boolean z);

        void onStartTrackingHover(SeekBar seekBar, int i);

        void onStopTrackingHover(SeekBar seekBar);
    }

    public SeekBar(Context context) {
        this(context, null);
    }

    public SeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarStyle);
    }

    public SeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    void onProgressRefresh(float scale, boolean fromUser, int progress) {
        super.onProgressRefresh(scale, fromUser, progress);
        if (this.mOnSeekBarChangeListener != null) {
            this.mOnSeekBarChangeListener.onProgressChanged(this, progress, fromUser);
        }
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        this.mOnSeekBarChangeListener = l;
    }

    void onStartTrackingTouch() {
        super.onStartTrackingTouch();
        if (this.mOnSeekBarChangeListener != null) {
            this.mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    void onStopTrackingTouch() {
        super.onStopTrackingTouch();
        if (this.mOnSeekBarChangeListener != null) {
            this.mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }

    public CharSequence getAccessibilityClassName() {
        return SeekBar.class.getName();
    }

    public void setOnSeekBarHoverListener(OnSeekBarHoverListener l) {
        this.mOnSeekBarHoverListener = l;
    }

    void onStartTrackingHover(int hoverLevel, int posX, int posY) {
        if (this.mOnSeekBarHoverListener != null) {
            this.mOnSeekBarHoverListener.onStartTrackingHover(this, hoverLevel);
        }
        super.onStartTrackingHover(hoverLevel, posX, posY);
    }

    void onStopTrackingHover() {
        if (this.mOnSeekBarHoverListener != null) {
            this.mOnSeekBarHoverListener.onStopTrackingHover(this);
        }
        super.onStopTrackingHover();
    }

    void onHoverChanged(int hoverLevel, int posX, int posY) {
        if (this.mOnSeekBarHoverListener != null) {
            this.mOnSeekBarHoverListener.onHoverChanged(this, hoverLevel, true);
        }
        super.onHoverChanged(hoverLevel, posX, posY);
    }
}
