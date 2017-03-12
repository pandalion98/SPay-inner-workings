package android.widget;

import android.content.Context;
import android.graphics.Rect;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.os.CustomFrequencyManager;
import android.os.RemoteException;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView.BufferType;
import com.android.internal.R;
import com.samsung.android.smartface.SmartFaceManager;
import java.util.HashSet;

public class EditText extends TextView {
    private static String TAG = EditText.class.getSimpleName();
    private static HashSet<String> sKnownAppList = new HashSet();
    private CustomFrequencyManager mCfmsService;
    private boolean mFocused;
    private boolean mKnownAppSipScenario;
    private Runnable mSetSPenIconCursorRunnable;
    private int mSipTextLen;
    private TextWatcher mSipTextWatcher;

    public EditText(Context context) {
        this(context, null);
    }

    public EditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mSipTextLen = 0;
        this.mFocused = false;
        this.mSipTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable arg0) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (arg0 != null) {
                    EditText.this.mSipTextLen = arg0.length();
                    EditText.this.updateSipFocusStatus();
                }
            }
        };
        this.mKnownAppSipScenario = false;
    }

    protected boolean getDefaultEditable() {
        return true;
    }

    protected MovementMethod getDefaultMovementMethod() {
        return ArrowKeyMovementMethod.getInstance();
    }

    public Editable getText() {
        return (Editable) super.getText();
    }

    public void setText(CharSequence text, BufferType type) {
        super.setText(text, BufferType.EDITABLE);
    }

    public void setSelection(int start, int stop) {
        Selection.setSelection(getText(), start, stop);
    }

    public void setSelection(int index) {
        Selection.setSelection(getText(), index);
    }

    public void selectAll() {
        Selection.selectAll(getText());
    }

    public void extendSelection(int index) {
        Selection.extendSelection(getText(), index);
    }

    public void setEllipsize(TruncateAt ellipsis) {
        if (ellipsis == TruncateAt.MARQUEE) {
            throw new IllegalArgumentException("EditText cannot use the ellipsize mode TextUtils.TruncateAt.MARQUEE");
        }
        super.setEllipsize(ellipsis);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(EditText.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(EditText.class.getName());
    }

    public CharSequence getAccessibilityClassName() {
        return EditText.class.getName();
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        switch (action) {
            case 2097152:
                CharSequence text = arguments != null ? arguments.getCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE) : null;
                setText(text);
                if (text != null && text.length() > 0) {
                    setSelection(text.length());
                }
                return true;
            default:
                return super.performAccessibilityActionInternal(action, arguments);
        }
    }

    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused && getSecClipboardEnabled() && !(this instanceof ExtractEditText) && getClipboardExManager().isShowing() && hasWindowFocus()) {
            getClipboardExManager().updateFilter(2, this.mPasteEventListener);
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (sKnownAppList.contains(getContext().getPackageName())) {
            if (focused) {
                addTextChangedListener(this.mSipTextWatcher);
            } else {
                removeTextChangedListener(this.mSipTextWatcher);
            }
            this.mFocused = focused;
            updateSipFocusStatus();
        }
    }

    public void dispatchWindowFocusChanged(boolean hasFocus) {
        super.dispatchWindowFocusChanged(hasFocus);
        if (isFocused() && hasFocus && getSecClipboardEnabled() && !(this instanceof ExtractEditText) && getClipboardExManager().isShowing()) {
            getClipboardExManager().updateFilter(2, this.mPasteEventListener);
        }
    }

    public void setCursorColor(int color) {
        super.setCursorColor(color);
    }

    public void hideCursorControllers() {
        super.hideCursorControllers();
    }

    static {
        sKnownAppList.add("com.google.android.talk");
        sKnownAppList.add("com.facebook.katana");
    }

    private void updateSipFocusStatus() {
        boolean current = this.mFocused && this.mSipTextLen > 0;
        if (this.mKnownAppSipScenario != current) {
            this.mKnownAppSipScenario = current;
            if (this.mCfmsService == null) {
                this.mCfmsService = (CustomFrequencyManager) getContext().getSystemService("CustomFrequencyManagerService");
            }
            this.mCfmsService.sendCommandToSSRM("KNOWN_APP_SIP", this.mKnownAppSipScenario ? SmartFaceManager.PAGE_BOTTOM : SmartFaceManager.PAGE_MIDDLE);
        }
    }

    public boolean onHoverEvent(MotionEvent event) {
        if (isHoveringUIEnabled()) {
            try {
                if (event.getAction() == 9 && isTextEditable()) {
                    this.mSetSPenIconCursorRunnable = new Runnable() {
                        public void run() {
                            try {
                                PointerIcon.setHoveringSpenIcon(2, -1);
                            } catch (RemoteException e) {
                                Log.e("TextView", "Hover icon is failed:" + e.toString());
                            }
                        }
                    };
                    post(this.mSetSPenIconCursorRunnable);
                } else if (event.getAction() == 10) {
                    if (this.mSetSPenIconCursorRunnable != null) {
                        removeCallbacks(this.mSetSPenIconCursorRunnable);
                        this.mSetSPenIconCursorRunnable = null;
                    }
                    PointerIcon.setHoveringSpenIcon(1, -1);
                }
            } catch (RemoteException e) {
                Log.e("TextView", "Hover icon is failed:" + e.toString());
            }
        }
        return super.onHoverEvent(event);
    }
}
