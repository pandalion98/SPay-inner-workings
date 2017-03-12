package android.app;

import android.R;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.SystemProperties;
import android.provider.Settings.System;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class GlobalActionsSViewCoverDialog extends Dialog {
    private static ImageView mBackgroundView;
    private static int mCoverColor = 0;
    private static final String mScafe = SystemProperties.get("ro.build.scafe");
    private static TextView messageView;
    private static ScrollView messageViewContainer;
    private LayoutParams messageView_attrs = null;

    public static class Builder {
        int backgroundColor = 0;
        private View contentView;
        private Context context;
        private int mSViewCoverHeight = 0;
        private int mSViewCoverWidth = 0;
        private int mStatusHeight = 0;
        private String message;
        private OnClickListener negativeButtonClickListener;
        private String negativeButtonText;
        private OnClickListener positiveButtonClickListener;
        private String positiveButtonText;
        private String title;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) this.context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) this.context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) this.context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String) this.context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public GlobalActionsSViewCoverDialog create() {
            View layout;
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final GlobalActionsSViewCoverDialog dialog = new GlobalActionsSViewCoverDialog(this.context, 16975075);
            if (this.title == null) {
                layout = inflater.inflate(17367152, null);
            } else {
                layout = inflater.inflate(17367151, null);
                ((TextView) layout.findViewById(16909152)).setText(this.title);
            }
            dialog.addContentView(layout, new ViewGroup.LayoutParams(-1, -2));
            GlobalActionsSViewCoverDialog.mBackgroundView = (ImageView) layout.findViewById(16909149);
            if (this.positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Builder.this.positiveButtonClickListener.onClick(dialog, -1);
                    }
                });
                ((Button) layout.findViewById(R.id.button2)).setText(this.positiveButtonText);
            } else {
                layout.findViewById(R.id.button1).setVisibility(8);
            }
            if (this.negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Builder.this.negativeButtonClickListener.onClick(dialog, -2);
                    }
                });
                ((Button) layout.findViewById(R.id.button1)).setText(this.negativeButtonText);
            } else {
                layout.findViewById(R.id.button2).setVisibility(8);
            }
            if (this.message != null) {
                GlobalActionsSViewCoverDialog.messageView = (TextView) layout.findViewById(R.id.message);
                GlobalActionsSViewCoverDialog.messageView.setText(this.message);
                GlobalActionsSViewCoverDialog.messageViewContainer = (ScrollView) layout.findViewById(16909237);
            } else if (this.contentView != null) {
                ((LinearLayout) layout.findViewById(16909157)).removeAllViews();
                ((LinearLayout) layout.findViewById(16909157)).addView(this.contentView, new ViewGroup.LayoutParams(-2, -2));
            }
            dialog.setContentView(layout);
            WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
            attrs.gravity = 48;
            dialog.getWindow().setAttributes(attrs);
            dialog.getWindow().clearFlags(2);
            this.mSViewCoverWidth = this.context.getResources().getDimensionPixelSize(17105355);
            this.mSViewCoverHeight = this.context.getResources().getDimensionPixelSize(17105356);
            this.mStatusHeight = this.context.getResources().getDimensionPixelSize(17104919);
            dialog.getWindow().setLayout(this.mSViewCoverWidth, this.mSViewCoverHeight - this.mStatusHeight);
            return dialog;
        }

        private int getBackgroundColor() {
            this.backgroundColor = Color.rgb(0, 0, 0);
            if (System.getInt(this.context.getContentResolver(), "sview_color_use_all", 1) == 1) {
                switch (System.getInt(this.context.getContentResolver(), "sview_color_random", 0)) {
                    case 0:
                        this.backgroundColor = System.getInt(this.context.getContentResolver(), "s_vew_cover_background_color", Color.rgb(8, 107, 119));
                        break;
                    case 1:
                        this.backgroundColor = System.getInt(this.context.getContentResolver(), "sview_bg_display_random", Color.rgb(8, 107, 119));
                        break;
                    default:
                        this.backgroundColor = Color.rgb(0, 0, 0);
                        break;
                }
            }
            return this.backgroundColor;
        }
    }

    public GlobalActionsSViewCoverDialog(Context context, int theme) {
        super(context, theme);
    }

    public GlobalActionsSViewCoverDialog(Context context) {
        super(context);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onAttachedToWindow();
        this.messageView_attrs = (LayoutParams) messageView.getLayoutParams();
        if (messageViewContainer.getMeasuredHeight() >= messageView.getMeasuredHeight()) {
            this.messageView_attrs.gravity = 16;
        } else {
            this.messageView_attrs.gravity = 48;
        }
        messageView.setLayoutParams(this.messageView_attrs);
    }
}
