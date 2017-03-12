package android.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.ProxyInfo;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.R;

public class SwitchPreferenceScreen extends SwitchPreference {
    public SwitchPreferenceScreen(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Preference, defStyleAttr, defStyleRes);
        String fragment = a.getString(13);
        if (fragment == null || fragment.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
            Log.w("SwitchPreferenceScreen", "SwitchPreferenceScreen should get fragment property. Fragment property does not exsit in SwitchPreferenceScreen");
        }
        a.recycle();
    }

    public SwitchPreferenceScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SwitchPreferenceScreen(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.switchPreferenceStyle);
    }

    public SwitchPreferenceScreen(Context context) {
        this(context, null);
    }

    protected void onClick() {
    }

    public void performClick() {
        super.onClick();
    }
}
