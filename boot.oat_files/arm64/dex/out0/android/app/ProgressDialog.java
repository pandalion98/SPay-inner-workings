package android.app;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.ProxyInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.internal.R;
import java.text.NumberFormat;

public class ProgressDialog extends AlertDialog {
    public static final int SAMSUNG_TW_STYLE_CIRCLE = 1000;
    public static final int STYLE_HORIZONTAL = 1;
    public static final int STYLE_SPINNER = 0;
    private static boolean mIsThemeDeviceDefaultFamily;
    private boolean mHasStarted;
    private int mIncrementBy;
    private int mIncrementSecondaryBy;
    private boolean mIndeterminate;
    private Drawable mIndeterminateDrawable;
    private int mMax;
    private CharSequence mMessage;
    private TextView mMessageView;
    private ProgressBar mProgress;
    private Drawable mProgressDrawable;
    private TextView mProgressNumber;
    private String mProgressNumberFormat;
    private TextView mProgressPercent;
    private NumberFormat mProgressPercentFormat;
    private int mProgressStyle = 0;
    private int mProgressVal;
    private int mSecondaryProgressVal;
    private Handler mViewUpdateHandler;

    public ProgressDialog(Context context) {
        boolean z = true;
        super(context);
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(18219197, outValue, true);
        if (outValue.data == 0) {
            z = false;
        }
        mIsThemeDeviceDefaultFamily = z;
        initFormats();
    }

    public ProgressDialog(Context context, int theme) {
        boolean z = true;
        super(context, theme);
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(18219197, outValue, true);
        if (outValue.data == 0) {
            z = false;
        }
        mIsThemeDeviceDefaultFamily = z;
        initFormats();
    }

    private void initFormats() {
        if (mIsThemeDeviceDefaultFamily) {
            this.mProgressNumberFormat = "%1d/%1d";
        } else {
            this.mProgressNumberFormat = "%1d/%2d";
        }
        this.mProgressPercentFormat = NumberFormat.getPercentInstance();
        this.mProgressPercentFormat.setMaximumFractionDigits(0);
    }

    public static ProgressDialog show(Context context, CharSequence title, CharSequence message) {
        return show(context, title, message, false);
    }

    public static ProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static ProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    public static ProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable, OnCancelListener cancelListener) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.show();
        return dialog;
    }

    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(this.mContext);
        TypedArray a = this.mContext.obtainStyledAttributes(null, R.styleable.AlertDialog, android.R.attr.alertDialogStyle, 0);
        View view;
        if (this.mProgressStyle == 1) {
            this.mViewUpdateHandler = new Handler() {
                public void handleMessage(Message msg) {
                    int i = 1;
                    super.handleMessage(msg);
                    int progress = ProgressDialog.this.mProgress.getProgress();
                    int max = ProgressDialog.this.mProgress.getMax();
                    if (ProgressDialog.this.mProgressNumberFormat != null) {
                        String format = ProgressDialog.this.mProgressNumberFormat;
                        if (ProgressDialog.this.mProgressNumber.isLayoutRtl()) {
                            ProgressDialog.this.mProgressNumber.setText(String.format(format, new Object[]{Integer.valueOf(max), Integer.valueOf(progress)}));
                        } else {
                            ProgressDialog.this.mProgressNumber.setText(String.format(format, new Object[]{Integer.valueOf(progress), Integer.valueOf(max)}));
                        }
                    } else {
                        ProgressDialog.this.mProgressNumber.setText(ProxyInfo.LOCAL_EXCL_LIST);
                    }
                    if (ProgressDialog.this.mProgressPercentFormat != null) {
                        SpannableString tmp = new SpannableString(ProgressDialog.this.mProgressPercentFormat.format(((double) progress) / ((double) max)));
                        if (ProgressDialog.mIsThemeDeviceDefaultFamily) {
                            i = 0;
                        }
                        tmp.setSpan(new StyleSpan(i), 0, tmp.length(), 33);
                        ProgressDialog.this.mProgressPercent.setText(tmp);
                        return;
                    }
                    ProgressDialog.this.mProgressPercent.setText(ProxyInfo.LOCAL_EXCL_LIST);
                }
            };
            view = inflater.inflate(a.getResourceId(17, 17367085), null);
            this.mProgress = (ProgressBar) view.findViewById(android.R.id.progress);
            this.mProgressNumber = (TextView) view.findViewById(16909165);
            this.mProgressPercent = (TextView) view.findViewById(16909164);
            if (mIsThemeDeviceDefaultFamily) {
                this.mMessageView = (TextView) view.findViewById(android.R.id.message);
            }
            setView(view);
        } else if (this.mProgressStyle == 1000 && mIsThemeDeviceDefaultFamily) {
            setTitle(null);
            getWindow().setBackgroundDrawableResource(17303840);
            view = inflater.inflate(17367362, null);
            this.mProgress = (ProgressBar) view.findViewById(android.R.id.progress);
            this.mMessageView = (TextView) view.findViewById(android.R.id.message);
            setView(view);
        } else {
            view = inflater.inflate(a.getResourceId(16, 17367254), null);
            this.mProgress = (ProgressBar) view.findViewById(android.R.id.progress);
            this.mMessageView = (TextView) view.findViewById(android.R.id.message);
            setView(view);
        }
        a.recycle();
        if (this.mMax > 0) {
            setMax(this.mMax);
        }
        if (this.mProgressVal > 0) {
            setProgress(this.mProgressVal);
        }
        if (this.mSecondaryProgressVal > 0) {
            setSecondaryProgress(this.mSecondaryProgressVal);
        }
        if (this.mIncrementBy > 0) {
            incrementProgressBy(this.mIncrementBy);
        }
        if (this.mIncrementSecondaryBy > 0) {
            incrementSecondaryProgressBy(this.mIncrementSecondaryBy);
        }
        if (this.mProgressDrawable != null) {
            setProgressDrawable(this.mProgressDrawable);
        }
        if (this.mIndeterminateDrawable != null) {
            setIndeterminateDrawable(this.mIndeterminateDrawable);
        }
        if (this.mMessage != null) {
            setMessage(this.mMessage);
        }
        setIndeterminate(this.mIndeterminate);
        onProgressChanged();
        super.onCreate(savedInstanceState);
    }

    public void onStart() {
        super.onStart();
        this.mHasStarted = true;
    }

    protected void onStop() {
        super.onStop();
        this.mHasStarted = false;
    }

    public void setProgress(int value) {
        if (this.mHasStarted) {
            this.mProgress.setProgress(value);
            onProgressChanged();
            return;
        }
        this.mProgressVal = value;
    }

    public void setSecondaryProgress(int secondaryProgress) {
        if (this.mProgress != null) {
            this.mProgress.setSecondaryProgress(secondaryProgress);
            onProgressChanged();
            return;
        }
        this.mSecondaryProgressVal = secondaryProgress;
    }

    public int getProgress() {
        if (this.mProgress != null) {
            return this.mProgress.getProgress();
        }
        return this.mProgressVal;
    }

    public int getSecondaryProgress() {
        if (this.mProgress != null) {
            return this.mProgress.getSecondaryProgress();
        }
        return this.mSecondaryProgressVal;
    }

    public int getMax() {
        if (this.mProgress != null) {
            return this.mProgress.getMax();
        }
        return this.mMax;
    }

    public void setMax(int max) {
        if (this.mProgress != null) {
            this.mProgress.setMax(max);
            onProgressChanged();
            return;
        }
        this.mMax = max;
    }

    public void incrementProgressBy(int diff) {
        if (this.mProgress != null) {
            this.mProgress.incrementProgressBy(diff);
            onProgressChanged();
            return;
        }
        this.mIncrementBy += diff;
    }

    public void incrementSecondaryProgressBy(int diff) {
        if (this.mProgress != null) {
            this.mProgress.incrementSecondaryProgressBy(diff);
            onProgressChanged();
            return;
        }
        this.mIncrementSecondaryBy += diff;
    }

    public void setProgressDrawable(Drawable d) {
        if (this.mProgress != null) {
            this.mProgress.setProgressDrawable(d);
        } else {
            this.mProgressDrawable = d;
        }
    }

    public void setIndeterminateDrawable(Drawable d) {
        if (this.mProgress != null) {
            this.mProgress.setIndeterminateDrawable(d);
        } else {
            this.mIndeterminateDrawable = d;
        }
    }

    public void setIndeterminate(boolean indeterminate) {
        if (this.mProgress != null) {
            this.mProgress.setIndeterminate(indeterminate);
        } else {
            this.mIndeterminate = indeterminate;
        }
    }

    public boolean isIndeterminate() {
        if (this.mProgress != null) {
            return this.mProgress.isIndeterminate();
        }
        return this.mIndeterminate;
    }

    public void setMessage(CharSequence message) {
        int i = 8;
        if (this.mProgress == null) {
            this.mMessage = message;
        } else if (this.mProgressStyle == 1) {
            if (!mIsThemeDeviceDefaultFamily || this.mMessageView == null) {
                super.setMessage(message);
                return;
            }
            this.mMessageView.setText(message);
            r2 = this.mMessageView;
            if (!message.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
                i = 0;
            }
            r2.setVisibility(i);
        } else if (mIsThemeDeviceDefaultFamily && this.mMessageView != null && this.mProgressStyle == 1000) {
            this.mMessageView.setText(message);
            r2 = this.mMessageView;
            if (!message.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
                i = 0;
            }
            r2.setVisibility(i);
        } else if (this.mMessageView != null) {
            this.mMessageView.setText(message);
        }
    }

    public void setProgressStyle(int style) {
        this.mProgressStyle = style;
    }

    public void setProgressNumberFormat(String format) {
        this.mProgressNumberFormat = format;
        onProgressChanged();
    }

    public void setProgressPercentFormat(NumberFormat format) {
        this.mProgressPercentFormat = format;
        onProgressChanged();
    }

    private void onProgressChanged() {
        if (this.mProgressStyle == 1 && this.mViewUpdateHandler != null && !this.mViewUpdateHandler.hasMessages(0)) {
            this.mViewUpdateHandler.sendEmptyMessage(0);
        }
    }

    public int getCurrentProgressStyle() {
        return this.mProgressStyle;
    }
}
