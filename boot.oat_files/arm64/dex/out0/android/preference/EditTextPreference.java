package android.preference;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.preference.Preference.BaseSavedState;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditTextPreference extends DialogPreference {
    private Context mContext;
    private EditText mEditText;
    private boolean mIsDeviceDefault;
    private String mText;
    private int mTwStartPadding;

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        String text;

        public SavedState(Parcel source) {
            super(source);
            this.text = source.readString();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.text);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }

    public EditTextPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        boolean z = true;
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mIsDeviceDefault = false;
        this.mContext = context;
        this.mEditText = new EditText(context, attrs);
        this.mEditText.setId(R.id.edit);
        this.mEditText.setPrivateImeOptions("inputType=PredictionOff");
        this.mEditText.setEnabled(true);
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(18219197, outValue, true);
        if (outValue.data == 0) {
            z = false;
        }
        this.mIsDeviceDefault = z;
    }

    public EditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EditTextPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextPreferenceStyle);
    }

    public EditTextPreference(Context context) {
        this(context, null);
    }

    public void setText(String text) {
        boolean wasBlocking = shouldDisableDependents();
        this.mText = text;
        persistString(text);
        boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }
    }

    public String getText() {
        return this.mText;
    }

    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        EditText editText = this.mEditText;
        editText.setText(getText());
        if (editText.length() != 0) {
            editText.setSelection(editText.length());
        }
        View oldParent = editText.getParent();
        if (oldParent != view) {
            if (oldParent != null) {
                ((ViewGroup) oldParent).removeView(editText);
            }
            onAddEditTextToDialogView(view, editText);
        }
        editText.requestFocus();
    }

    protected void onAddEditTextToDialogView(View dialogView, EditText editText) {
        ViewGroup container = (ViewGroup) dialogView.findViewById(16909389);
        if (container != null) {
            if (this.mIsDeviceDefault) {
                this.mTwStartPadding = container.getPaddingStart() - ((int) (6.0f * this.mContext.getResources().getDisplayMetrics().density));
                container.setPadding(this.mTwStartPadding, container.getPaddingTop(), container.getPaddingEnd(), container.getPaddingBottom());
            }
            container.addView(editText, -1, -2);
        }
    }

    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            String value = this.mEditText.getText().toString();
            if (callChangeListener(value)) {
                setText(value);
            }
        }
    }

    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        if (restoreValue) {
            defaultValue = getPersistedString(this.mText);
        } else {
            String defaultValue2 = (String) defaultValue;
        }
        setText(defaultValue);
    }

    public boolean shouldDisableDependents() {
        return TextUtils.isEmpty(this.mText) || super.shouldDisableDependents();
    }

    public EditText getEditText() {
        return this.mEditText;
    }

    protected boolean needInputMethod() {
        return true;
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }
        SavedState myState = new SavedState(superState);
        myState.text = getText();
        return myState;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        setText(myState.text);
    }
}
