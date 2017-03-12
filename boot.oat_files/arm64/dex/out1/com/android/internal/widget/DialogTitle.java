package com.android.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.GeneralUtil;
import android.widget.TextView;
import com.android.internal.R;

public class DialogTitle extends TextView {
    private Context mContext;
    private int textSize;

    public DialogTitle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
    }

    public DialogTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public DialogTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public DialogTitle(Context context) {
        super(context);
        this.mContext = context;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        if (GeneralUtil.isDeviceDefault(this.mContext)) {
            this.textSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.tw_dialog_title_two_line_textsize);
            return;
        }
        TypedArray a = this.mContext.obtainStyledAttributes(null, android.R.styleable.TextAppearance, R.attr.textAppearanceMedium, R.style.TextAppearance_Medium);
        this.textSize = a.getDimensionPixelSize(0, 0);
        a.recycle();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Layout layout = getLayout();
        if (layout != null) {
            int lineCount = layout.getLineCount();
            if (lineCount > 0 && layout.getEllipsisCount(lineCount - 1) > 0) {
                setSingleLine(false);
                setMaxLines(2);
                if (this.textSize != 0) {
                    float currentFontScale = this.mContext.getResources().getConfiguration().fontScale;
                    if (!GeneralUtil.isDeviceDefault(this.mContext) || currentFontScale <= 1.2f) {
                        setTextSize(0, (float) this.textSize);
                    } else {
                        setTextSize(0, (this.mContext.getResources().getDimension(R.dimen.tw_dialog_title_text_size) / currentFontScale) * 1.2f);
                    }
                }
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }
}
