package android.preference;

import android.R;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.preference.Preference.BaseSavedState;
import android.provider.Settings.Global;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;

public final class PreferenceScreen extends PreferenceGroup implements OnItemClickListener, OnDismissListener {
    private static final int HUGE_FONT_FIRST = 7;
    private static final int HUGE_FONT_FOURTH = 10;
    private static final int HUGE_FONT_SECOND = 8;
    private static final int HUGE_FONT_THIRD = 9;
    private static final String INTENT_EXTRA_HUGE_FONT = "large_font";
    private static final String INTENT_FONT_SIZE_CHANGED = "com.samsung.settings.FONT_SIZE_CHANGED";
    private String TAG = "PreferenceScreen";
    private Dialog mDialog;
    private boolean mIsChangedPressedItem = false;
    private boolean mIsCheckedSwitch = false;
    private boolean mIsNeedClick = false;
    private boolean mIsPassedActionMove = false;
    private boolean mIsRTL = false;
    private boolean mIsUsingTouchListener = false;
    private ListView mListView;
    private ListView mListViewForTouch;
    private float mLocationTouchDownX = -1.0f;
    private float mMovedDeltaX = -1.0f;
    private BroadcastReceiver mReceiver;
    private ListAdapter mRootAdapter;
    private int mScaledTouchSlop = 64;
    private final TouchListener mTouchListener = new TouchListener();
    private int pressedPostion = -1;

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        Bundle dialogBundle;
        boolean isDialogShowing;

        public SavedState(Parcel source) {
            boolean z = true;
            super(source);
            if (source.readInt() != 1) {
                z = false;
            }
            this.isDialogShowing = z;
            this.dialogBundle = source.readBundle();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.isDialogShowing ? 1 : 0);
            dest.writeBundle(this.dialogBundle);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }

    private class TouchListener implements OnTouchListener {
        private TouchListener() {
        }

        public boolean onTouch(View v, MotionEvent ev) {
            int action = ev.getActionMasked();
            PreferenceScreen.this.mIsUsingTouchListener = true;
            if (PreferenceScreen.this.mListViewForTouch != null) {
                switch (action) {
                    case 0:
                        PreferenceScreen.this.mIsPassedActionMove = false;
                        PreferenceScreen.this.mLocationTouchDownX = ev.getX();
                        break;
                    case 1:
                        PreferenceScreen.this.pressedPostion = -1;
                        if (!PreferenceScreen.this.mIsPassedActionMove) {
                            PreferenceScreen.this.mIsNeedClick = true;
                            break;
                        }
                        break;
                    case 2:
                        PreferenceScreen.this.mIsPassedActionMove = true;
                        PreferenceScreen.this.mMovedDeltaX = ev.getX() - PreferenceScreen.this.mLocationTouchDownX;
                        if (Math.abs(PreferenceScreen.this.mMovedDeltaX) >= ((float) PreferenceScreen.this.mScaledTouchSlop)) {
                            if (PreferenceScreen.this.mIsRTL) {
                                boolean z;
                                PreferenceScreen preferenceScreen = PreferenceScreen.this;
                                if (PreferenceScreen.this.mMovedDeltaX < 0.0f) {
                                    z = true;
                                } else {
                                    z = false;
                                }
                                preferenceScreen.mIsCheckedSwitch = z;
                            } else {
                                PreferenceScreen.this.mIsCheckedSwitch = PreferenceScreen.this.mMovedDeltaX > 0.0f;
                            }
                            PreferenceScreen.this.mIsNeedClick = false;
                            int lastVisibleListItem = PreferenceScreen.this.mListViewForTouch.getLastVisiblePosition();
                            int i = PreferenceScreen.this.mListViewForTouch.getFirstVisiblePosition();
                            while (i <= lastVisibleListItem) {
                                if (PreferenceScreen.this.mListViewForTouch.getAdapter().getItem(i) instanceof SwitchPreference) {
                                    View tmpView = PreferenceScreen.this.mListViewForTouch.getChildAt(i);
                                    if (tmpView != null && tmpView.isPressed()) {
                                        if (!(PreferenceScreen.this.pressedPostion == -1 || PreferenceScreen.this.pressedPostion == i)) {
                                            PreferenceScreen.this.mIsChangedPressedItem = true;
                                        }
                                        PreferenceScreen.this.pressedPostion = i;
                                        break;
                                    }
                                }
                                i++;
                            }
                            break;
                        }
                        PreferenceScreen.this.mIsNeedClick = true;
                        if (!PreferenceScreen.this.mListViewForTouch.hasFocus()) {
                            PreferenceScreen.this.mListViewForTouch.requestFocus();
                            break;
                        }
                        break;
                    case 3:
                        PreferenceScreen.this.pressedPostion = -1;
                        break;
                    default:
                        break;
                }
            }
            return false;
        }
    }

    public PreferenceScreen(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.preferenceScreenStyle);
        if (this.mIsDeviceDefault && View.TW_SCAFE_2016A) {
            setTitleFontSize();
        }
    }

    public ListAdapter getRootAdapter() {
        if (this.mRootAdapter == null) {
            this.mRootAdapter = onCreateRootAdapter();
        }
        return this.mRootAdapter;
    }

    protected ListAdapter onCreateRootAdapter() {
        return new PreferenceGroupAdapter(this);
    }

    public void bind(ListView listView) {
        listView.setOnItemClickListener(this);
        listView.setAdapter(getRootAdapter());
        Context context = getContext();
        if (this.mIsDeviceDefault) {
            listView.setOnTouchListener(this.mTouchListener);
            boolean z = isRTL() && hasRTL();
            this.mIsRTL = z;
            if (View.TW_SCAFE_2016A && this.mReceiver == null) {
                this.mReceiver = new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        if (PreferenceScreen.INTENT_FONT_SIZE_CHANGED.equals(intent.getAction())) {
                            PreferenceScreen.this.setTitleFontSize();
                            PreferenceScreen.this.notifyHierarchyChanged();
                        }
                    }
                };
                IntentFilter theFilter = new IntentFilter();
                theFilter.addAction(INTENT_FONT_SIZE_CHANGED);
                context.registerReceiver(this.mReceiver, theFilter);
            }
        }
        if (context != null) {
            this.mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        }
        if (this.mRootAdapter instanceof PreferenceGroupAdapter) {
            PreferenceGroupAdapter adapter = this.mRootAdapter;
            if (adapter.mTwNeedPaddingTop > listView.getPaddingTop()) {
                if (listView.isPaddingRelative()) {
                    listView.setPaddingRelative(listView.getPaddingStart(), adapter.mTwNeedPaddingTop, listView.getPaddingEnd(), listView.getPaddingBottom());
                } else {
                    listView.setPadding(listView.getPaddingLeft(), adapter.mTwNeedPaddingTop, listView.getPaddingRight(), listView.getPaddingBottom());
                }
            }
        }
        this.mListViewForTouch = listView;
        onAttachedToActivity();
    }

    protected void onClick() {
        if (getIntent() == null && getFragment() == null && getPreferenceCount() != 0) {
            showDialog(null);
        }
    }

    private void showDialog(Bundle state) {
        Context context = getContext();
        if (this.mListView != null) {
            this.mListView.setAdapter(null);
        }
        View childPrefScreen = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(17367246, null);
        this.mListView = (ListView) childPrefScreen.findViewById(R.id.list);
        bind(this.mListView);
        CharSequence title = getTitle();
        Dialog dialog = new Dialog(context, context.getThemeResId());
        this.mDialog = dialog;
        if (TextUtils.isEmpty(title)) {
            dialog.getWindow().requestFeature(1);
        } else {
            dialog.setTitle(title);
        }
        dialog.setContentView(childPrefScreen);
        dialog.setOnDismissListener(this);
        if (state != null) {
            dialog.onRestoreInstanceState(state);
        }
        getPreferenceManager().addPreferencesScreen(dialog);
        dialog.show();
    }

    public void onDismiss(DialogInterface dialog) {
        this.mDialog = null;
        getPreferenceManager().removePreferencesScreen(dialog);
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    public void onItemClick(AdapterView parent, View view, int position, long id) {
        if (parent instanceof ListView) {
            position -= ((ListView) parent).getHeaderViewsCount();
        }
        Preference item = getRootAdapter().getItem(position);
        if (item instanceof Preference) {
            Preference preference = item;
            Switch tmpSwitch = null;
            if (preference instanceof SwitchPreference) {
                tmpSwitch = ((SwitchPreference) preference).mSwitch;
            }
            if (this.mIsUsingTouchListener && (tmpSwitch instanceof Switch) && ((this.mIsCheckedSwitch == tmpSwitch.isChecked() && !this.mIsNeedClick) || this.mIsChangedPressedItem)) {
                this.mIsUsingTouchListener = false;
                this.mIsChangedPressedItem = false;
                return;
            }
            this.mIsUsingTouchListener = false;
            preference.performClick(this);
        }
    }

    protected boolean isOnSameScreenAsChildren() {
        return false;
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            return superState;
        }
        Parcelable myState = new SavedState(superState);
        myState.isDialogShowing = true;
        myState.dialogBundle = dialog.onSaveInstanceState();
        return myState;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        if (myState.isDialogShowing) {
            showDialog(myState.dialogBundle);
        }
    }

    void unregisterReceiver() {
        if (this.mReceiver != null) {
            getContext().unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
        }
    }

    private void setTitleFontSize() {
        Context context = getContext();
        this.mLargerFontLevel = Global.getInt(context.getContentResolver(), Global.FONT_SIZE, 0);
        switch (this.mLargerFontLevel) {
            case 7:
                this.mTitleLargerTextSize = context.getResources().getDimensionPixelSize(17105465);
                return;
            case 8:
                this.mTitleLargerTextSize = context.getResources().getDimensionPixelSize(17105466);
                return;
            case 9:
                this.mTitleLargerTextSize = context.getResources().getDimensionPixelSize(17105467);
                return;
            case 10:
                this.mTitleLargerTextSize = context.getResources().getDimensionPixelSize(17105468);
                return;
            default:
                this.mTitleLargerTextSize = context.getResources().getDimensionPixelSize(17105469);
                return;
        }
    }
}
