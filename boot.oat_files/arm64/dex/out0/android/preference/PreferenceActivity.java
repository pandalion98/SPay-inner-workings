package android.preference;

import android.R;
import android.app.Fragment;
import android.app.FragmentBreadCrumbs;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.im.InjectionManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.preference.PreferenceFragment.OnPreferenceStartFragmentCallback;
import android.preference.PreferenceManager.OnPreferenceTreeClickListener;
import android.provider.Downloads.Impl.RequestHeaders;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.GeneralUtil;
import android.util.TypedValue;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

public abstract class PreferenceActivity extends ListActivity implements OnPreferenceTreeClickListener, OnPreferenceStartFragmentCallback {
    private static final String BACK_STACK_PREFS = ":android:prefs";
    private static final String CUR_HEADER_TAG = ":android:cur_header";
    public static final String EXTRA_NO_HEADERS = ":android:no_headers";
    private static final String EXTRA_PREFS_SET_BACK_TEXT = "extra_prefs_set_back_text";
    private static final String EXTRA_PREFS_SET_NEXT_TEXT = "extra_prefs_set_next_text";
    private static final String EXTRA_PREFS_SHOW_BUTTON_BAR = "extra_prefs_show_button_bar";
    private static final String EXTRA_PREFS_SHOW_SKIP = "extra_prefs_show_skip";
    public static final String EXTRA_SHOW_FRAGMENT = ":android:show_fragment";
    public static final String EXTRA_SHOW_FRAGMENT_ARGUMENTS = ":android:show_fragment_args";
    public static final String EXTRA_SHOW_FRAGMENT_SHORT_TITLE = ":android:show_fragment_short_title";
    public static final String EXTRA_SHOW_FRAGMENT_TITLE = ":android:show_fragment_title";
    private static final int FIRST_REQUEST_CODE = 100;
    private static final String HEADERS_TAG = ":android:headers";
    public static final long HEADER_ID_UNDEFINED = -1;
    private static final boolean L_MOCHA = "mocha".equals(SystemProperties.get("ro.build.scafe"));
    private static final int MSG_BIND_PREFERENCES = 1;
    private static final int MSG_BUILD_HEADERS = 2;
    private static final String PREFERENCES_TAG = ":android:preferences";
    private static final float SPLIT_BAR_MOVEABLE_AREA_MAX = 0.66f;
    private static final float SPLIT_BAR_MOVEABLE_AREA_MIN = 0.2f;
    private static final float SPLIT_BAR_SPLIT_X_IN_FULLVIEW = 20.0f;
    private static final String TAG = "PreferenceActivity";
    private static float mSplitBarMovedLeftWeight = -1.0f;
    private static boolean mUserUpdateSplit = false;
    private final boolean isElasticEnabled = true;
    private Header mCurHeader;
    private boolean mEnableSplitBar = true;
    private FragmentBreadCrumbs mFragmentBreadCrumbs;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    PreferenceActivity.this.bindPreferences();
                    return;
                case 2:
                    ArrayList<Header> oldHeaders = new ArrayList(PreferenceActivity.this.mHeaders);
                    PreferenceActivity.this.mHeaders.clear();
                    PreferenceActivity.this.onBuildHeaders(PreferenceActivity.this.mHeaders);
                    if (InjectionManager.getInstance() != null) {
                        InjectionManager.getInstance().dispatchBuildHeader(PreferenceActivity.this, PreferenceActivity.this.mHeaders);
                    }
                    if (PreferenceActivity.this.mAdapter instanceof BaseAdapter) {
                        ((BaseAdapter) PreferenceActivity.this.mAdapter).notifyDataSetChanged();
                    }
                    Header header = PreferenceActivity.this.onGetNewHeader();
                    Header mappedHeader;
                    if (header != null && header.fragment != null) {
                        mappedHeader = PreferenceActivity.this.findBestMatchingHeader(header, oldHeaders);
                        if (mappedHeader == null || PreferenceActivity.this.mCurHeader != mappedHeader) {
                            PreferenceActivity.this.switchToHeader(header);
                            return;
                        }
                        return;
                    } else if (PreferenceActivity.this.mCurHeader != null) {
                        mappedHeader = PreferenceActivity.this.findBestMatchingHeader(PreferenceActivity.this.mCurHeader, PreferenceActivity.this.mHeaders);
                        if (mappedHeader != null) {
                            PreferenceActivity.this.setSelectedHeader(mappedHeader);
                            return;
                        }
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private LinearLayout mHeaderLayout;
    private final ArrayList<Header> mHeaders = new ArrayList();
    private boolean mInsideOnCreate = false;
    private boolean mIsDeviceDefault;
    private boolean mIsMultiPane = false;
    private boolean mIsRTL = false;
    private FrameLayout mListFooter;
    private Button mNextButton;
    private int mPreferenceHeaderItemResId = 0;
    private boolean mPreferenceHeaderRemoveEmptyIcon = false;
    private PreferenceManager mPreferenceManager;
    private ViewGroup mPrefsContainer;
    private Bundle mSavedInstanceState;
    private boolean mSinglePane;
    private OnLayoutChangeListener mSplitBarLayoutChangeListner = null;
    private View mSplitBarView = null;
    private boolean mUpdateLayoutBySplitChange = false;

    public static final class Header implements Parcelable {
        public static final Creator<Header> CREATOR = new Creator<Header>() {
            public Header createFromParcel(Parcel source) {
                return new Header(source);
            }

            public Header[] newArray(int size) {
                return new Header[size];
            }
        };
        public CharSequence breadCrumbShortTitle;
        public int breadCrumbShortTitleRes;
        public CharSequence breadCrumbTitle;
        public int breadCrumbTitleRes;
        public Bundle extras;
        public String fragment;
        public Bundle fragmentArguments;
        public int iconRes;
        public long id = -1;
        public Intent intent;
        public CharSequence summary;
        public CharSequence summaryDescription;
        public int summaryDescriptionRes;
        public int summaryRes;
        public CharSequence title;
        public CharSequence titleDescription;
        public int titleDescriptionRes;
        public int titleRes;

        public CharSequence getTitle(Resources res) {
            if (this.titleRes != 0) {
                return res.getText(this.titleRes);
            }
            return this.title;
        }

        public CharSequence getTitleDescription(Resources res) {
            if (this.titleDescriptionRes != 0) {
                return res.getText(this.titleDescriptionRes);
            }
            return this.titleDescription;
        }

        public CharSequence getSummary(Resources res) {
            if (this.summaryRes != 0) {
                return res.getText(this.summaryRes);
            }
            return this.summary;
        }

        public CharSequence getSummaryDescription(Resources res) {
            if (this.summaryDescriptionRes != 0) {
                return res.getText(this.summaryDescriptionRes);
            }
            return this.summaryDescription;
        }

        public CharSequence getBreadCrumbTitle(Resources res) {
            if (this.breadCrumbTitleRes != 0) {
                return res.getText(this.breadCrumbTitleRes);
            }
            return this.breadCrumbTitle;
        }

        public CharSequence getBreadCrumbShortTitle(Resources res) {
            if (this.breadCrumbShortTitleRes != 0) {
                return res.getText(this.breadCrumbShortTitleRes);
            }
            return this.breadCrumbShortTitle;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.id);
            dest.writeInt(this.titleRes);
            TextUtils.writeToParcel(this.title, dest, flags);
            dest.writeInt(this.summaryRes);
            TextUtils.writeToParcel(this.summary, dest, flags);
            dest.writeInt(this.breadCrumbTitleRes);
            TextUtils.writeToParcel(this.breadCrumbTitle, dest, flags);
            dest.writeInt(this.breadCrumbShortTitleRes);
            TextUtils.writeToParcel(this.breadCrumbShortTitle, dest, flags);
            dest.writeInt(this.titleDescriptionRes);
            TextUtils.writeToParcel(this.titleDescription, dest, flags);
            dest.writeInt(this.summaryDescriptionRes);
            TextUtils.writeToParcel(this.summaryDescription, dest, flags);
            dest.writeInt(this.iconRes);
            dest.writeString(this.fragment);
            dest.writeBundle(this.fragmentArguments);
            if (this.intent != null) {
                dest.writeInt(1);
                this.intent.writeToParcel(dest, flags);
            } else {
                dest.writeInt(0);
            }
            dest.writeBundle(this.extras);
        }

        public void readFromParcel(Parcel in) {
            this.id = in.readLong();
            this.titleRes = in.readInt();
            this.title = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.summaryRes = in.readInt();
            this.summary = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.breadCrumbTitleRes = in.readInt();
            this.breadCrumbTitle = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.breadCrumbShortTitleRes = in.readInt();
            this.breadCrumbShortTitle = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.titleDescriptionRes = in.readInt();
            this.titleDescription = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.summaryDescriptionRes = in.readInt();
            this.summaryDescription = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.iconRes = in.readInt();
            this.fragment = in.readString();
            this.fragmentArguments = in.readBundle();
            if (in.readInt() != 0) {
                this.intent = (Intent) Intent.CREATOR.createFromParcel(in);
            }
            this.extras = in.readBundle();
        }

        Header(Parcel in) {
            readFromParcel(in);
        }
    }

    private static class HeaderAdapter extends ArrayAdapter<Header> {
        private LayoutInflater mInflater;
        private boolean mIsSinglePane;
        private int mLayoutResId;
        private boolean mRemoveIconIfEmpty;
        private int mSelectedId;

        private static class HeaderViewHolder {
            ImageView icon;
            TextView summary;
            TextView title;

            private HeaderViewHolder() {
            }
        }

        public HeaderAdapter(Context context, List<Header> objects, int layoutResId, boolean removeIconBehavior) {
            super(context, 0, objects);
            this.mIsSinglePane = true;
            this.mSelectedId = -1;
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mLayoutResId = layoutResId;
            this.mRemoveIconIfEmpty = removeIconBehavior;
            this.mSelectedId = -1;
        }

        public HeaderAdapter(Context context, List<Header> objects, int layoutResId) {
            this(context, objects, layoutResId, true);
        }

        public HeaderAdapter(Context context, List<Header> objects, int layoutResId, boolean removeIconBehavior, boolean singlePane) {
            this(context, objects, layoutResId, removeIconBehavior);
            this.mIsSinglePane = singlePane;
        }

        public void setSelectedId(int nSelId) {
            this.mSelectedId = nSelId;
        }

        public int getSelectedId() {
            return this.mSelectedId;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            HeaderViewHolder holder;
            if (convertView == null) {
                view = this.mInflater.inflate(this.mLayoutResId, parent, false);
                holder = new HeaderViewHolder();
                holder.icon = (ImageView) view.findViewById(R.id.icon);
                holder.title = (TextView) view.findViewById(R.id.title);
                holder.summary = (TextView) view.findViewById(R.id.summary);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (HeaderViewHolder) view.getTag();
            }
            Header header = (Header) getItem(position);
            if (!this.mRemoveIconIfEmpty) {
                holder.icon.setImageResource(header.iconRes);
            } else if (header.iconRes == 0) {
                holder.icon.setVisibility(8);
            } else {
                holder.icon.setVisibility(0);
                holder.icon.setImageResource(header.iconRes);
            }
            holder.title.setText(header.getTitle(getContext().getResources()));
            CharSequence summary = header.getSummary(getContext().getResources());
            if (TextUtils.isEmpty(summary)) {
                holder.summary.setVisibility(8);
            } else {
                holder.summary.setVisibility(0);
                holder.summary.setText(summary);
            }
            CharSequence titleDescription = header.getTitleDescription(getContext().getResources());
            if (!TextUtils.isEmpty(titleDescription)) {
                holder.title.setContentDescription(titleDescription);
            }
            CharSequence summaryDescription = header.getSummaryDescription(getContext().getResources());
            if (!TextUtils.isEmpty(summaryDescription)) {
                holder.summary.setContentDescription(summaryDescription);
            }
            if (header.iconRes == 0) {
                holder.icon.setVisibility(8);
            } else {
                holder.icon.setVisibility(0);
            }
            View dynamicDivider = view.findViewById(16909503);
            if (dynamicDivider != null && header.intent == null && TextUtils.isEmpty(header.fragment)) {
                dynamicDivider.setVisibility(8);
            }
            return view;
        }
    }

    @Deprecated
    protected void setActionBarShadow(boolean use) {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mInsideOnCreate = true;
        TypedArray sa = obtainStyledAttributes(null, com.android.internal.R.styleable.PreferenceActivity, 18219041, 0);
        int layoutResId = sa.getResourceId(0, 17367243);
        this.mPreferenceHeaderItemResId = sa.getResourceId(1, 17367237);
        this.mPreferenceHeaderRemoveEmptyIcon = sa.getBoolean(2, false);
        this.mIsDeviceDefault = GeneralUtil.isDeviceDefault(this);
        sa.recycle();
        setContentView(layoutResId);
        this.mHeaderLayout = (LinearLayout) findViewById(16909391);
        this.mListFooter = (FrameLayout) findViewById(16909392);
        this.mPrefsContainer = (ViewGroup) findViewById(16909393);
        boolean z = onIsHidingHeaders() || !onIsMultiPane();
        this.mSinglePane = z;
        String initialFragment = getIntent().getStringExtra(EXTRA_SHOW_FRAGMENT);
        Bundle initialArguments = getIntent().getBundleExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS);
        int initialTitle = getIntent().getIntExtra(EXTRA_SHOW_FRAGMENT_TITLE, 0);
        int initialShortTitle = getIntent().getIntExtra(EXTRA_SHOW_FRAGMENT_SHORT_TITLE, 0);
        if (!this.mIsDeviceDefault || this.mSinglePane) {
            this.mSplitBarView = findViewById(16909504);
            if (this.mSplitBarView != null) {
                this.mSplitBarView.setVisibility(8);
                this.mSplitBarView = null;
            }
        } else {
            this.mSplitBarView = findViewById(16909504);
            if (this.mSplitBarView != null) {
                LinearLayout headerLayout = (LinearLayout) findViewById(16909391);
                LayoutParams llp = (LinearLayout.LayoutParams) headerLayout.getLayoutParams();
                LayoutParams rlp = (LinearLayout.LayoutParams) this.mPrefsContainer.getLayoutParams();
                float weightSum = llp.weight + rlp.weight;
                if (mSplitBarMovedLeftWeight > 0.0f) {
                    llp.weight = mSplitBarMovedLeftWeight;
                    rlp.weight = weightSum - mSplitBarMovedLeftWeight;
                    headerLayout.setLayoutParams(llp);
                    this.mPrefsContainer.setLayoutParams(rlp);
                }
            }
        }
        if (savedInstanceState != null) {
            ArrayList<Header> headers = savedInstanceState.getParcelableArrayList(HEADERS_TAG);
            if (headers != null) {
                this.mHeaders.addAll(headers);
                int curHeader = savedInstanceState.getInt(CUR_HEADER_TAG, -1);
                if (curHeader >= 0 && curHeader < this.mHeaders.size()) {
                    setSelectedHeader((Header) this.mHeaders.get(curHeader));
                }
            }
        } else if (initialFragment == null || !this.mSinglePane) {
            onBuildHeaders(this.mHeaders);
            if (InjectionManager.getInstance() != null) {
                InjectionManager.getInstance().dispatchBuildHeader(this, this.mHeaders);
            }
            if (this.mHeaders.size() > 0 && !this.mSinglePane) {
                if (initialFragment == null) {
                    switchToHeader(onGetInitialHeader());
                } else {
                    switchToHeader(initialFragment, initialArguments);
                }
            }
        } else {
            switchToHeader(initialFragment, initialArguments);
            if (initialTitle != 0) {
                showBreadCrumbs(getText(initialTitle), initialShortTitle != 0 ? getText(initialShortTitle) : null);
            }
        }
        if (initialFragment != null && this.mSinglePane) {
            findViewById(16909391).setVisibility(8);
            this.mPrefsContainer.setVisibility(0);
            ViewGroup crumbsLayout = (ViewGroup) this.mPrefsContainer.getChildAt(0);
            crumbsLayout.removeAllViews();
            crumbsLayout.setVisibility(8);
            this.mFragmentBreadCrumbs = null;
            if (initialTitle != 0) {
                showBreadCrumbs(getText(initialTitle), initialShortTitle != 0 ? getText(initialShortTitle) : null);
            }
        } else if (this.mHeaders.size() > 0) {
            setListAdapter(new HeaderAdapter(this, this.mHeaders, this.mPreferenceHeaderItemResId, this.mPreferenceHeaderRemoveEmptyIcon, this.mSinglePane));
            if (!this.mSinglePane) {
                getListView().setChoiceMode(1);
                if (this.mCurHeader != null) {
                    setSelectedHeader(this.mCurHeader);
                }
                if (L_MOCHA && getListView().getBackground() != null) {
                    getListView().getBackground().setAutoMirrored(true);
                }
                this.mPrefsContainer.setVisibility(0);
            }
        } else {
            setContentView(this.mIsDeviceDefault ? 17367357 : 17367245);
            this.mListFooter = (FrameLayout) findViewById(16909392);
            this.mPrefsContainer = (ViewGroup) findViewById(16909394);
            this.mPreferenceManager = new PreferenceManager(this, 100);
            this.mPreferenceManager.setOnPreferenceTreeClickListener(this);
        }
        Intent intent = getIntent();
        if (intent.getBooleanExtra(EXTRA_PREFS_SHOW_BUTTON_BAR, false)) {
            String buttonText;
            findViewById(16909395).setVisibility(0);
            Button backButton = (Button) findViewById(16909396);
            backButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    PreferenceActivity.this.setResult(0);
                    PreferenceActivity.this.finish();
                }
            });
            Button skipButton = (Button) findViewById(16909397);
            skipButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    PreferenceActivity.this.setResult(-1);
                    PreferenceActivity.this.finish();
                }
            });
            this.mNextButton = (Button) findViewById(16909398);
            this.mNextButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    PreferenceActivity.this.setResult(-1);
                    PreferenceActivity.this.finish();
                }
            });
            if (intent.hasExtra(EXTRA_PREFS_SET_NEXT_TEXT)) {
                buttonText = intent.getStringExtra(EXTRA_PREFS_SET_NEXT_TEXT);
                if (TextUtils.isEmpty(buttonText)) {
                    this.mNextButton.setVisibility(8);
                } else {
                    this.mNextButton.setText(buttonText);
                }
            }
            if (intent.hasExtra(EXTRA_PREFS_SET_BACK_TEXT)) {
                buttonText = intent.getStringExtra(EXTRA_PREFS_SET_BACK_TEXT);
                if (TextUtils.isEmpty(buttonText)) {
                    backButton.setVisibility(8);
                } else {
                    backButton.setText(buttonText);
                }
            }
            if (intent.getBooleanExtra(EXTRA_PREFS_SHOW_SKIP, false)) {
                skipButton.setVisibility(0);
            }
        }
        Preference preference = new Preference(this);
        z = preference.isRTL() && preference.hasRTL();
        this.mIsRTL = z;
        if (!(!this.mIsDeviceDefault || this.mSinglePane || this.mSplitBarView == null)) {
            if (this.mSplitBarLayoutChangeListner == null) {
                this.mSplitBarLayoutChangeListner = new OnLayoutChangeListener() {
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (PreferenceActivity.this.mEnableSplitBar) {
                            float mRightLayoutStartPosition;
                            if (PreferenceActivity.this.mIsRTL) {
                                mRightLayoutStartPosition = PreferenceActivity.this.mHeaderLayout.getX();
                            } else {
                                mRightLayoutStartPosition = PreferenceActivity.this.mPrefsContainer.getX();
                            }
                            if (PreferenceActivity.this.mIsDeviceDefault && PreferenceActivity.this.mSplitBarView != null) {
                                float x = mRightLayoutStartPosition - ((float) (PreferenceActivity.this.mSplitBarView.getWidth() / 2));
                                if (x < 0.0f) {
                                    x = 0.0f;
                                }
                                if (PreferenceActivity.this.mSplitBarView.getX() != x) {
                                    PreferenceActivity.this.mSplitBarView.setX(x);
                                }
                            }
                        }
                    }
                };
            }
            this.mSplitBarView.addOnLayoutChangeListener(this.mSplitBarLayoutChangeListner);
            this.mSplitBarView.setOnHoverListener(new OnHoverListener() {
                public boolean onHover(View v, MotionEvent event) {
                    if (!PreferenceActivity.this.mEnableSplitBar || event.getToolType(0) != 2) {
                        return false;
                    }
                    int action = event.getAction();
                    if (action == 9) {
                        try {
                            PointerIcon.setHoveringSpenIcon(4, -1);
                        } catch (RemoteException e) {
                        }
                    } else if (action == 10) {
                        PointerIcon.setHoveringSpenIcon(1, -1);
                    }
                    return true;
                }
            });
            this.mSplitBarView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (!PreferenceActivity.this.mEnableSplitBar) {
                        return false;
                    }
                    int action = event.getAction();
                    View splitBar = null;
                    if (v instanceof ViewGroup) {
                        splitBar = ((ViewGroup) v).getChildAt(0);
                    }
                    if (splitBar == null) {
                        return false;
                    }
                    if (action == 0) {
                        splitBar.setVisibility(0);
                        PreferenceActivity.this.mUpdateLayoutBySplitChange = false;
                    } else if (action == 2) {
                        int splitBarwidth = PreferenceActivity.this.mSplitBarView.getWidth();
                        int parentLayoutWidth = ((View) PreferenceActivity.this.mSplitBarView.getParent()).getWidth();
                        float touchX = event.getX();
                        float newSplitBarPosX = PreferenceActivity.this.mSplitBarView.getX();
                        float newSplitBarCenterPosX = newSplitBarPosX + ((float) (splitBarwidth / 2));
                        float touchXInParentRect = newSplitBarPosX + touchX;
                        float splitRatio;
                        if (PreferenceActivity.this.mIsDeviceDefault && PreferenceActivity.this.mIsRTL) {
                            float f;
                            if (touchX > ((float) splitBarwidth) && touchXInParentRect <= ((float) parentLayoutWidth)) {
                                newSplitBarCenterPosX += touchX - ((float) splitBarwidth);
                                if (newSplitBarCenterPosX / ((float) parentLayoutWidth) > 0.8f) {
                                    f = (float) parentLayoutWidth;
                                    newSplitBarCenterPosX = f - TypedValue.applyDimension(1, PreferenceActivity.SPLIT_BAR_SPLIT_X_IN_FULLVIEW, PreferenceActivity.this.getResources().getDisplayMetrics());
                                }
                                PreferenceActivity.this.mSplitBarView.setX(newSplitBarCenterPosX - ((float) (splitBarwidth / 2)));
                                PreferenceActivity.this.mUpdateLayoutBySplitChange = true;
                            } else if (touchX < 0.0f && touchXInParentRect >= 0.0f) {
                                newSplitBarCenterPosX += touchX;
                                splitRatio = newSplitBarCenterPosX / ((float) parentLayoutWidth);
                                if (splitRatio < 0.33999997f) {
                                    newSplitBarCenterPosX = ((float) parentLayoutWidth) * 0.33999997f;
                                } else if (splitRatio > 0.8f) {
                                    f = (float) parentLayoutWidth;
                                    newSplitBarCenterPosX = f - TypedValue.applyDimension(1, PreferenceActivity.SPLIT_BAR_SPLIT_X_IN_FULLVIEW, PreferenceActivity.this.getResources().getDisplayMetrics());
                                }
                                PreferenceActivity.this.mSplitBarView.setX(newSplitBarCenterPosX - ((float) (splitBarwidth / 2)));
                                PreferenceActivity.this.mUpdateLayoutBySplitChange = true;
                            }
                        } else if (touchX > ((float) splitBarwidth) && touchXInParentRect <= ((float) parentLayoutWidth)) {
                            newSplitBarCenterPosX += touchX - ((float) splitBarwidth);
                            splitRatio = newSplitBarCenterPosX / ((float) parentLayoutWidth);
                            if (splitRatio > PreferenceActivity.SPLIT_BAR_MOVEABLE_AREA_MAX) {
                                newSplitBarCenterPosX = ((float) parentLayoutWidth) * PreferenceActivity.SPLIT_BAR_MOVEABLE_AREA_MAX;
                            } else if (splitRatio < PreferenceActivity.SPLIT_BAR_MOVEABLE_AREA_MIN) {
                                newSplitBarCenterPosX = TypedValue.applyDimension(1, PreferenceActivity.SPLIT_BAR_SPLIT_X_IN_FULLVIEW, PreferenceActivity.this.getResources().getDisplayMetrics());
                            }
                            PreferenceActivity.this.mSplitBarView.setX(newSplitBarCenterPosX - ((float) (splitBarwidth / 2)));
                            PreferenceActivity.this.mUpdateLayoutBySplitChange = true;
                        } else if (touchX < 0.0f && touchXInParentRect >= 0.0f) {
                            newSplitBarCenterPosX += touchX;
                            if (newSplitBarCenterPosX / ((float) parentLayoutWidth) < PreferenceActivity.SPLIT_BAR_MOVEABLE_AREA_MIN) {
                                newSplitBarCenterPosX = TypedValue.applyDimension(1, PreferenceActivity.SPLIT_BAR_SPLIT_X_IN_FULLVIEW, PreferenceActivity.this.getResources().getDisplayMetrics());
                            }
                            PreferenceActivity.this.mSplitBarView.setX(newSplitBarCenterPosX - ((float) (splitBarwidth / 2)));
                            PreferenceActivity.this.mUpdateLayoutBySplitChange = true;
                        }
                        if (PreferenceActivity.this.mUpdateLayoutBySplitChange) {
                            PreferenceActivity.mUserUpdateSplit = true;
                            LinearLayout headerLayout = (LinearLayout) PreferenceActivity.this.findViewById(16909391);
                            llp = (LinearLayout.LayoutParams) headerLayout.getLayoutParams();
                            LayoutParams rlp = (LinearLayout.LayoutParams) PreferenceActivity.this.mPrefsContainer.getLayoutParams();
                            float weightSum = llp.weight + rlp.weight;
                            float newLeftPanelWeight = weightSum * (newSplitBarCenterPosX / ((float) parentLayoutWidth));
                            float newRightPanelWeight = weightSum - newLeftPanelWeight;
                            llp.weight = newLeftPanelWeight;
                            rlp.weight = newRightPanelWeight;
                            if (PreferenceActivity.this.mIsDeviceDefault) {
                                if (PreferenceActivity.this.mIsRTL) {
                                    headerLayout.setLayoutParams(rlp);
                                    PreferenceActivity.this.mPrefsContainer.setLayoutParams(llp);
                                } else {
                                    headerLayout.setLayoutParams(llp);
                                    PreferenceActivity.this.mPrefsContainer.setLayoutParams(rlp);
                                }
                            }
                        }
                        PreferenceActivity.this.mUpdateLayoutBySplitChange = false;
                    } else if (action == 1) {
                        llp = (LinearLayout.LayoutParams) ((LinearLayout) PreferenceActivity.this.findViewById(16909391)).getLayoutParams();
                        if (PreferenceActivity.mSplitBarMovedLeftWeight != llp.weight) {
                            PreferenceActivity.mSplitBarMovedLeftWeight = llp.weight;
                        }
                        splitBar.setVisibility(4);
                        splitBar.requestLayout();
                    } else {
                        float x = PreferenceActivity.this.mPrefsContainer.getX() - ((float) (PreferenceActivity.this.mSplitBarView.getWidth() / 2));
                        if (x < 0.0f) {
                            x = 0.0f;
                        }
                        if (!(action == 3 && PreferenceActivity.this.mIsDeviceDefault)) {
                            PreferenceActivity.this.mSplitBarView.setX(x);
                        }
                        PreferenceActivity.this.mUpdateLayoutBySplitChange = false;
                        splitBar.setVisibility(4);
                    }
                    return true;
                }
            });
        }
        this.mInsideOnCreate = false;
    }

    public boolean hasHeaders() {
        return getListView().getVisibility() == 0 && this.mPreferenceManager == null;
    }

    public List<Header> getHeaders() {
        return this.mHeaders;
    }

    public boolean isMultiPane() {
        return hasHeaders() && this.mPrefsContainer.getVisibility() == 0;
    }

    public boolean onIsMultiPane() {
        return getResources().getBoolean(17956873) || this.mIsMultiPane;
    }

    protected void setMultiPane(boolean isMultiPane) {
        this.mIsMultiPane = isMultiPane;
    }

    public void setEnableSplitBar(boolean enable) {
        this.mEnableSplitBar = enable;
    }

    public boolean onIsHidingHeaders() {
        return getIntent().getBooleanExtra(EXTRA_NO_HEADERS, false);
    }

    public Header onGetInitialHeader() {
        for (int i = 0; i < this.mHeaders.size(); i++) {
            Header h = (Header) this.mHeaders.get(i);
            if (h.fragment != null) {
                return h;
            }
        }
        throw new IllegalStateException("Must have at least one header with a fragment");
    }

    public Header onGetNewHeader() {
        return null;
    }

    public void onBuildHeaders(List<Header> list) {
    }

    public void invalidateHeaders() {
        if (!this.mHandler.hasMessages(2)) {
            this.mHandler.sendEmptyMessage(2);
        }
    }

    public void loadHeadersFromResource(int resid, List<Header> target) {
        XmlResourceParser parser = null;
        try {
            int type;
            parser = getResources().getXml(resid);
            AttributeSet attrs = Xml.asAttributeSet(parser);
            do {
                type = parser.next();
                if (type == 1) {
                    break;
                }
            } while (type != 2);
            String nodeName = parser.getName();
            if ("preference-headers".equals(nodeName)) {
                Bundle curBundle = null;
                int outerDepth = parser.getDepth();
                while (true) {
                    type = parser.next();
                    if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                        if (parser != null) {
                            parser.close();
                            return;
                        }
                        return;
                    } else if (!(type == 3 || type == 4)) {
                        if (RequestHeaders.COLUMN_HEADER.equals(parser.getName())) {
                            Header header = new Header();
                            TypedArray sa = obtainStyledAttributes(attrs, com.android.internal.R.styleable.PreferenceHeader);
                            header.id = (long) sa.getResourceId(1, -1);
                            TypedValue tv = sa.peekValue(2);
                            if (tv != null && tv.type == 3) {
                                if (tv.resourceId != 0) {
                                    header.titleRes = tv.resourceId;
                                } else {
                                    header.title = tv.string;
                                }
                            }
                            tv = sa.peekValue(3);
                            if (tv != null && tv.type == 3) {
                                if (tv.resourceId != 0) {
                                    header.summaryRes = tv.resourceId;
                                } else {
                                    header.summary = tv.string;
                                }
                            }
                            tv = sa.peekValue(5);
                            if (tv != null && tv.type == 3) {
                                if (tv.resourceId != 0) {
                                    header.breadCrumbTitleRes = tv.resourceId;
                                } else {
                                    header.breadCrumbTitle = tv.string;
                                }
                            }
                            tv = sa.peekValue(6);
                            if (tv != null && tv.type == 3) {
                                if (tv.resourceId != 0) {
                                    header.breadCrumbShortTitleRes = tv.resourceId;
                                } else {
                                    header.breadCrumbShortTitle = tv.string;
                                }
                            }
                            tv = sa.peekValue(7);
                            if (tv != null && tv.type == 3) {
                                if (tv.resourceId != 0) {
                                    header.titleDescriptionRes = tv.resourceId;
                                } else {
                                    header.titleDescription = tv.string;
                                }
                            }
                            tv = sa.peekValue(8);
                            if (tv != null && tv.type == 3) {
                                if (tv.resourceId != 0) {
                                    header.summaryDescriptionRes = tv.resourceId;
                                } else {
                                    header.summaryDescription = tv.string;
                                }
                            }
                            header.iconRes = sa.getResourceId(0, 0);
                            header.fragment = sa.getString(4);
                            sa.recycle();
                            if (curBundle == null) {
                                curBundle = new Bundle();
                            }
                            int innerDepth = parser.getDepth();
                            while (true) {
                                type = parser.next();
                                if (type != 1 && (type != 3 || parser.getDepth() > innerDepth)) {
                                    if (!(type == 3 || type == 4)) {
                                        String innerNodeName = parser.getName();
                                        if (innerNodeName.equals("extra")) {
                                            getResources().parseBundleExtra("extra", attrs, curBundle);
                                            XmlUtils.skipCurrentTag(parser);
                                        } else if (innerNodeName.equals("intent")) {
                                            header.intent = Intent.parseIntent(getResources(), parser, attrs);
                                        } else {
                                            XmlUtils.skipCurrentTag(parser);
                                        }
                                    }
                                }
                            }
                            if (curBundle.size() > 0) {
                                header.fragmentArguments = curBundle;
                                curBundle = null;
                            }
                            target.add(header);
                        } else {
                            XmlUtils.skipCurrentTag(parser);
                        }
                    }
                }
                if (parser != null) {
                    parser.close();
                    return;
                }
                return;
            }
            throw new RuntimeException("XML document must start with <preference-headers> tag; found" + nodeName + " at " + parser.getPositionDescription());
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Error parsing headers", e);
        } catch (IOException e2) {
            throw new RuntimeException("Error parsing headers", e2);
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
        }
    }

    protected boolean isValidFragment(String fragmentName) {
        if (getApplicationInfo().targetSdkVersion < 19) {
            return true;
        }
        throw new RuntimeException("Subclasses of PreferenceActivity must override isValidFragment(String) to verify that the Fragment class is valid! " + getClass().getName() + " has not checked if fragment " + fragmentName + " is valid.");
    }

    public void setListFooter(View view) {
        this.mListFooter.removeAllViews();
        this.mListFooter.addView(view, new FrameLayout.LayoutParams(-1, -2));
    }

    protected void onStop() {
        super.onStop();
        if (this.mPreferenceManager != null) {
            this.mPreferenceManager.dispatchActivityStop();
        }
    }

    protected void onDestroy() {
        if (!(!this.mIsDeviceDefault || this.mSinglePane || this.mSplitBarView == null || this.mSplitBarLayoutChangeListner == null)) {
            this.mSplitBarView.removeOnLayoutChangeListener(this.mSplitBarLayoutChangeListner);
            this.mSplitBarLayoutChangeListner = null;
        }
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        super.onDestroy();
        if (this.mPreferenceManager != null) {
            this.mPreferenceManager.dispatchActivityDestroy();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.mHeaders.size() > 0) {
            outState.putParcelableArrayList(HEADERS_TAG, this.mHeaders);
            if (this.mCurHeader != null) {
                int index = this.mHeaders.indexOf(this.mCurHeader);
                if (index >= 0) {
                    outState.putInt(CUR_HEADER_TAG, index);
                }
            }
        }
        if (this.mPreferenceManager != null) {
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            if (preferenceScreen != null) {
                Bundle container = new Bundle();
                preferenceScreen.saveHierarchyState(container);
                outState.putBundle(PREFERENCES_TAG, container);
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int mLeftPaneWt = getResources().getInteger(17694968);
        int mRightPaneWt = getResources().getInteger(17694969);
        if (this.mSplitBarView != null && this.mIsDeviceDefault && !this.mSinglePane && !mUserUpdateSplit && this.mPrefsContainer != null) {
            LinearLayout headerLayout = (LinearLayout) findViewById(16909391);
            LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) headerLayout.getLayoutParams();
            LinearLayout.LayoutParams rlp = (LinearLayout.LayoutParams) this.mPrefsContainer.getLayoutParams();
            llp.weight = (float) mLeftPaneWt;
            rlp.weight = (float) mRightPaneWt;
            headerLayout.setLayoutParams(llp);
            this.mPrefsContainer.setLayoutParams(rlp);
        }
    }

    protected void onRestoreInstanceState(Bundle state) {
        if (this.mPreferenceManager != null) {
            Bundle container = state.getBundle(PREFERENCES_TAG);
            if (container != null) {
                PreferenceScreen preferenceScreen = getPreferenceScreen();
                if (preferenceScreen != null) {
                    preferenceScreen.restoreHierarchyState(container);
                    this.mSavedInstanceState = state;
                    return;
                }
            }
        }
        super.onRestoreInstanceState(state);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.mPreferenceManager != null) {
            this.mPreferenceManager.dispatchActivityResult(requestCode, resultCode, data);
        }
    }

    public void onContentChanged() {
        super.onContentChanged();
        if (this.mPreferenceManager != null) {
            postBindPreferences();
        }
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (isResumed()) {
            super.onListItemClick(l, v, position, id);
            if (this.mAdapter != null) {
                Object item = this.mAdapter.getItem(position);
                if (item instanceof Header) {
                    onHeaderClick((Header) item, position);
                }
            }
        }
    }

    public void onHeaderClick(Header header, int position) {
        if (header.fragment != null) {
            if (this.mSinglePane) {
                int titleRes = header.breadCrumbTitleRes;
                int shortTitleRes = header.breadCrumbShortTitleRes;
                if (titleRes == 0) {
                    titleRes = header.titleRes;
                    shortTitleRes = 0;
                }
                startWithFragment(header.fragment, header.fragmentArguments, null, 0, titleRes, shortTitleRes);
                return;
            }
            switchToHeader(header);
        } else if (header.intent != null) {
            startActivity(header.intent);
        }
    }

    public Intent onBuildStartFragmentIntent(String fragmentName, Bundle args, int titleRes, int shortTitleRes) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClass(this, getClass());
        intent.putExtra(EXTRA_SHOW_FRAGMENT, fragmentName);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, args);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_TITLE, titleRes);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_SHORT_TITLE, shortTitleRes);
        intent.putExtra(EXTRA_NO_HEADERS, true);
        return intent;
    }

    public void startWithFragment(String fragmentName, Bundle args, Fragment resultTo, int resultRequestCode) {
        startWithFragment(fragmentName, args, resultTo, resultRequestCode, 0, 0);
    }

    public void startWithFragment(String fragmentName, Bundle args, Fragment resultTo, int resultRequestCode, int titleRes, int shortTitleRes) {
        Intent intent = onBuildStartFragmentIntent(fragmentName, args, titleRes, shortTitleRes);
        if (resultTo == null) {
            startActivity(intent);
        } else {
            resultTo.startActivityForResult(intent, resultRequestCode);
        }
    }

    public void showBreadCrumbs(CharSequence title, CharSequence shortTitle) {
        if (this.mFragmentBreadCrumbs == null) {
            try {
                this.mFragmentBreadCrumbs = (FragmentBreadCrumbs) findViewById(R.id.title);
                if (this.mFragmentBreadCrumbs != null) {
                    if (this.mSinglePane) {
                        this.mFragmentBreadCrumbs.setVisibility(8);
                        View bcSection = findViewById(16909180);
                        if (bcSection != null) {
                            bcSection.setVisibility(8);
                        }
                        setTitle(title);
                    }
                    this.mFragmentBreadCrumbs.setMaxVisible(2);
                    this.mFragmentBreadCrumbs.setActivity(this);
                } else if (title != null) {
                    setTitle(title);
                    return;
                } else {
                    return;
                }
            } catch (ClassCastException e) {
                setTitle(title);
                return;
            }
        }
        if (this.mFragmentBreadCrumbs.getVisibility() != 0) {
            setTitle(title);
            return;
        }
        this.mFragmentBreadCrumbs.setTitle(title, shortTitle);
        this.mFragmentBreadCrumbs.setParentTitle(null, null, null);
    }

    public void setParentTitle(CharSequence title, CharSequence shortTitle, OnClickListener listener) {
        if (this.mFragmentBreadCrumbs != null) {
            this.mFragmentBreadCrumbs.setParentTitle(title, shortTitle, listener);
        }
    }

    void setSelectedHeader(Header header) {
        this.mCurHeader = header;
        int index = this.mHeaders.indexOf(header);
        if (index >= 0) {
            getListView().setItemChecked(index, true);
            if (this.mAdapter != null && (this.mAdapter instanceof HeaderAdapter)) {
                ((HeaderAdapter) this.mAdapter).setSelectedId(index);
            }
        } else {
            getListView().clearChoices();
        }
        showBreadCrumbs(header);
    }

    void showBreadCrumbs(Header header) {
        if (header != null) {
            CharSequence title = header.getBreadCrumbTitle(getResources());
            if (title == null) {
                title = header.getTitle(getResources());
            }
            if (title == null) {
                title = getTitle();
            }
            showBreadCrumbs(title, header.getBreadCrumbShortTitle(getResources()));
            return;
        }
        showBreadCrumbs(getTitle(), null);
    }

    private void switchToHeaderInner(String fragmentName, Bundle args) {
        getFragmentManager().popBackStack(BACK_STACK_PREFS, 1);
        if (isValidFragment(fragmentName)) {
            Fragment f = Fragment.instantiate(this, fragmentName, args);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!this.mInsideOnCreate) {
                transaction.setTransition(4099);
            }
            transaction.replace(16909394, f);
            transaction.commitAllowingStateLoss();
            return;
        }
        throw new IllegalArgumentException("Invalid fragment for this activity: " + fragmentName);
    }

    public void switchToHeader(String fragmentName, Bundle args) {
        Header selectedHeader = null;
        for (int i = 0; i < this.mHeaders.size(); i++) {
            if (fragmentName.equals(((Header) this.mHeaders.get(i)).fragment)) {
                selectedHeader = (Header) this.mHeaders.get(i);
                break;
            }
        }
        setSelectedHeader(selectedHeader);
        switchToHeaderInner(fragmentName, args);
    }

    public void switchToHeader(Header header) {
        if (this.mCurHeader == header) {
            getFragmentManager().popBackStack(BACK_STACK_PREFS, 1);
        } else if (header.fragment == null) {
            throw new IllegalStateException("can't switch to header that has no fragment");
        } else {
            switchToHeaderInner(header.fragment, header.fragmentArguments);
            setSelectedHeader(header);
        }
    }

    Header findBestMatchingHeader(Header cur, ArrayList<Header> from) {
        int j;
        ArrayList<Header> matches = new ArrayList();
        for (j = 0; j < from.size(); j++) {
            Header oh = (Header) from.get(j);
            if (cur == oh || (cur.id != -1 && cur.id == oh.id)) {
                matches.clear();
                matches.add(oh);
                break;
            }
            if (cur.fragment != null) {
                if (cur.fragment.equals(oh.fragment)) {
                    matches.add(oh);
                }
            } else if (cur.intent != null) {
                if (cur.intent.equals(oh.intent)) {
                    matches.add(oh);
                }
            } else if (cur.title != null && cur.title.equals(oh.title)) {
                matches.add(oh);
            }
        }
        int NM = matches.size();
        if (NM == 1) {
            return (Header) matches.get(0);
        }
        if (NM > 1) {
            for (j = 0; j < NM; j++) {
                oh = (Header) matches.get(j);
                if (cur.fragmentArguments != null && cur.fragmentArguments.equals(oh.fragmentArguments)) {
                    return oh;
                }
                if (cur.extras != null && cur.extras.equals(oh.extras)) {
                    return oh;
                }
                if (cur.title != null && cur.title.equals(oh.title)) {
                    return oh;
                }
            }
        }
        return null;
    }

    public void startPreferenceFragment(Fragment fragment, boolean push) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(16909394, fragment);
        if (push) {
            transaction.setTransition(4097);
            transaction.addToBackStack(BACK_STACK_PREFS);
        } else {
            transaction.setTransition(4099);
        }
        transaction.commitAllowingStateLoss();
    }

    public void startPreferencePanel(String fragmentClass, Bundle args, int titleRes, CharSequence titleText, Fragment resultTo, int resultRequestCode) {
        if (this.mSinglePane) {
            startWithFragment(fragmentClass, args, resultTo, resultRequestCode, titleRes, 0);
            return;
        }
        Fragment f = Fragment.instantiate(this, fragmentClass, args);
        if (resultTo != null) {
            f.setTargetFragment(resultTo, resultRequestCode);
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(16909394, f);
        if (titleRes != 0) {
            transaction.setBreadCrumbTitle(titleRes);
        } else if (titleText != null) {
            transaction.setBreadCrumbTitle(titleText);
        }
        transaction.setTransition(4097);
        transaction.addToBackStack(BACK_STACK_PREFS);
        transaction.commitAllowingStateLoss();
    }

    public void finishPreferencePanel(Fragment caller, int resultCode, Intent resultData) {
        if (this.mSinglePane) {
            setResult(resultCode, resultData);
            finish();
            return;
        }
        onBackPressed();
        if (caller != null && caller.getTargetFragment() != null) {
            caller.getTargetFragment().onActivityResult(caller.getTargetRequestCode(), resultCode, resultData);
        }
    }

    public boolean onPreferenceStartFragment(PreferenceFragment caller, Preference pref) {
        startPreferencePanel(pref.getFragment(), pref.getExtras(), pref.getTitleRes(), pref.getTitle(), null, 0);
        return true;
    }

    private void postBindPreferences() {
        if (!this.mHandler.hasMessages(1)) {
            this.mHandler.obtainMessage(1).sendToTarget();
        }
    }

    private void bindPreferences() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.bind(getListView());
            if (this.mSavedInstanceState != null) {
                super.onRestoreInstanceState(this.mSavedInstanceState);
                this.mSavedInstanceState = null;
            }
        }
    }

    @Deprecated
    public PreferenceManager getPreferenceManager() {
        return this.mPreferenceManager;
    }

    private void requirePreferenceManager() {
        if (this.mPreferenceManager != null) {
            return;
        }
        if (this.mAdapter == null) {
            throw new RuntimeException("This should be called after super.onCreate.");
        }
        throw new RuntimeException("Modern two-pane PreferenceActivity requires use of a PreferenceFragment");
    }

    @Deprecated
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        requirePreferenceManager();
        if (this.mPreferenceManager.setPreferences(preferenceScreen) && preferenceScreen != null) {
            postBindPreferences();
            CharSequence title = getPreferenceScreen().getTitle();
            if (title != null) {
                setTitle(title);
            }
        }
    }

    @Deprecated
    public PreferenceScreen getPreferenceScreen() {
        if (this.mPreferenceManager != null) {
            return this.mPreferenceManager.getPreferenceScreen();
        }
        return null;
    }

    @Deprecated
    public void addPreferencesFromIntent(Intent intent) {
        requirePreferenceManager();
        setPreferenceScreen(this.mPreferenceManager.inflateFromIntent(intent, getPreferenceScreen()));
    }

    @Deprecated
    public void addPreferencesFromResource(int preferencesResId) {
        requirePreferenceManager();
        setPreferenceScreen(this.mPreferenceManager.inflateFromResource(this, preferencesResId, getPreferenceScreen()));
    }

    @Deprecated
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return false;
    }

    @Deprecated
    public Preference findPreference(CharSequence key) {
        if (this.mPreferenceManager == null) {
            return null;
        }
        return this.mPreferenceManager.findPreference(key);
    }

    protected void onNewIntent(Intent intent) {
        if (this.mPreferenceManager != null) {
            this.mPreferenceManager.dispatchNewIntent(intent);
        }
    }

    protected boolean hasNextButton() {
        return this.mNextButton != null;
    }

    protected Button getNextButton() {
        return this.mNextButton;
    }
}
