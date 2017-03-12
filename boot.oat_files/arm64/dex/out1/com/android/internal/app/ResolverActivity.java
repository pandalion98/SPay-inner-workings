package com.android.internal.app;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.ActivityThread;
import android.app.AppGlobals;
import android.app.KeyguardManager;
import android.app.VoiceInteractor.PickOptionRequest;
import android.app.VoiceInteractor.PickOptionRequest.Option;
import android.app.VoiceInteractor.Prompt;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersonaManager;
import android.os.PersonaManager.KnoxContainerVersion;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings$System;
import android.provider.Settings.Secure;
import android.sec.enterprise.content.SecContentProviderURI;
import android.service.notification.ZenModeConfig;
import android.spay.TACommandRequest;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.ims.ImsConferenceState;
import com.android.internal.R;
import com.android.internal.content.PackageMonitor;
import com.android.internal.widget.PagerAdapter;
import com.android.internal.widget.ResolverDrawerLayout;
import com.android.internal.widget.ResolverDrawerLayout.OnDismissedListener;
import com.android.internal.widget.ViewPager;
import com.android.internal.widget.ViewPager.OnPageChangeListener;
import com.samsung.android.cocktailbar.AbsCocktailLoadablePanel;
import com.samsung.android.service.DeviceRootKeyService.DeviceRootKeyServiceManager;
import com.samsung.android.share.SShareCommon;
import com.samsung.android.share.SShareLogging;
import com.samsung.android.share.SShareMoreActions;
import com.samsung.android.share.SShareSimpleSharing;
import com.samsung.android.smartface.SmartFaceManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ResolverActivity extends Activity {
    private static final boolean DEBUG = false;
    public static final String DOCUMENTS_UI_POLICY = "DocumentsUIPolicy";
    public static final int DOCUMENTS_UI_POLICY_SEC = 1;
    private static final String GUIDE_ACTIVITY = "ResolverGuideActivity";
    private static final String INTERNAL_PACKAGE = "com.android.internal.app";
    private static final float MAX_FONT_SCALE = 1.2f;
    private static final int MAX_PAGE_LINE_NUM = 2;
    public static final String MIME_TYPE_MEMO = "application/vnd.samsung.android.memo";
    private static final String PAGER_KEY = "pagerkey_%1$d_%2$d";
    private static final String SHAREVIA_RECENT_ITEM = "sharevia_recent";
    private static final String SPLIT_STRING = ";";
    private static final String TAG = "ResolverActivity";
    public static final String THEME_CHOOSER = "theme";
    public static final int THEME_DEVICE_DEFAULT = 1;
    public static final int THEME_DEVICE_DEFAULT_LIGHT = 2;
    public static final int THEME_NONE = 0;
    private float defaultTextSize;
    private final boolean isElasticEnabled = true;
    private ResolveListAdapter mAdapter;
    private AbsListView mAdapterView;
    private Button mAlwaysButton;
    private boolean mAlwaysUseOption;
    private final int mAnimDuration = 300;
    private ViewGroup mBottomPanel;
    private int mCurrentPageIdx = 0;
    private ArrayList<ComponentName> mDropComponents = new ArrayList();
    private boolean mDropsDocumentsUI = false;
    private Animator mExitAnimator;
    protected List<Intent> mExtraIntentList = new ArrayList();
    private HorizontalListView mGridRecentHistory;
    private List<PageResolverListAdapter> mGridResolveAdapterList = new ArrayList();
    private int mIconDpi;
    private final ArrayList<Intent> mIntents = new ArrayList();
    private boolean mIsDeviceDefault = true;
    private boolean mIsManagedProfile;
    private int mLastSelected = -1;
    private String mLaunchedFromPackage;
    private int mLaunchedFromUid;
    private int mMaxColumns;
    private SShareMoreActions mMoreActions;
    private Button mOnceButton;
    private final PackageMonitor mPackageMonitor = new PackageMonitor() {
        public void onSomePackagesChanged() {
            ResolverActivity.this.mAdapter.handlePackagesChanged();
            if (ResolverActivity.this.mProfileView != null) {
                ResolverActivity.this.bindProfileView();
            }
        }
    };
    private int mPageItemNum;
    private ResolverPagerAdapter mPagerAdapter;
    private PickTargetOptionRequest mPickOptionRequest;
    private ArrayList<String> mPkgNamesArray = new ArrayList();
    private String mPkgNamesFromDB;
    private PackageManager mPm;
    private int mProfileSwitchMessageId = -1;
    private View mProfileView;
    private LinkedList<String> mRecentPkgList = new LinkedList();
    private String mRecentPkgNames;
    private boolean mRegistered;
    private ResolverComparator mResolverComparator;
    protected ResolverDrawerLayout mResolverDrawerLayout;
    private Map<String, Integer> mResolverListMap = new HashMap();
    private boolean mResolvingHome = false;
    private SShareCommon mSShareCommon;
    private SShareLogging mSShareLogging;
    private boolean mSafeForwardingMode;
    private boolean mShowExtended;
    private SShareSimpleSharing mSimpleSharing;
    private boolean mSquicleUX = false;
    private boolean mSupportButtons;
    private boolean mSupportGridResolver;
    private boolean mSupportLogging;
    private boolean mSupportMoreActions;
    private boolean mSupportPageMode;
    private boolean mSupportShowButtonShapes;
    private boolean mSupportSimpleSharing;
    private View mTitlePanelAFW;
    private View mTitlePanelDefault;
    private View mTopPanel;
    private int mTotalCount;
    private ViewPager mViewPager;
    private LinearLayout mViewPagerBottomSpacing;
    private LinearLayout mViewPagerNavi;
    private int mViewPagerNaviPrevPage;
    private View mVisibleArea;

    class ResolveListAdapter extends BaseAdapter {
        private final List<ResolveInfo> mBaseResolveList;
        List<DisplayResolveInfo> mDisplayList;
        private boolean mFilterLastUsed;
        private boolean mHasExtendedInfo;
        protected final LayoutInflater mInflater;
        private final Intent[] mInitialIntents;
        private final List<Intent> mIntents;
        private ResolveInfo mLastChosen;
        private int mLastChosenPosition;
        private final String mLaunchedFromPackage;
        private final int mLaunchedFromUid;
        List<ResolvedComponentInfo> mOrigResolveList;
        private DisplayResolveInfo mOtherProfile;

        public ResolveListAdapter(Context context, List<Intent> payloadIntents, Intent[] initialIntents, List<ResolveInfo> rList, int launchedFromUid, String launchedFromPackage, boolean filterLastUsed) {
            this.mLastChosenPosition = -1;
            this.mLaunchedFromPackage = launchedFromPackage;
            this.mIntents = payloadIntents;
            this.mInitialIntents = initialIntents;
            this.mBaseResolveList = rList;
            this.mLaunchedFromUid = launchedFromUid;
            this.mInflater = LayoutInflater.from(context);
            this.mDisplayList = new ArrayList();
            this.mFilterLastUsed = filterLastUsed;
            rebuildList();
        }

        public ResolveListAdapter(Context context, List<Intent> payloadIntents, Intent[] initialIntents, List<ResolveInfo> rList, int launchedFromUid, boolean filterLastUsed) {
            this.mLastChosenPosition = -1;
            this.mLaunchedFromPackage = null;
            this.mIntents = payloadIntents;
            this.mInitialIntents = initialIntents;
            this.mBaseResolveList = rList;
            this.mLaunchedFromUid = launchedFromUid;
            this.mInflater = LayoutInflater.from(context);
            this.mDisplayList = new ArrayList();
            this.mFilterLastUsed = filterLastUsed;
            rebuildList();
        }

        public void handlePackagesChanged() {
            int oldItemCount = getCount();
            rebuildList();
            notifyDataSetChanged();
            if (oldItemCount != getCount()) {
                ResolverActivity.this.updatePagerAdapter(getCount());
            }
            if (getCount() == 0) {
                ResolverActivity.this.finish();
            }
            if (ResolverActivity.this.mSupportGridResolver && (ResolverActivity.this.mAdapterView instanceof GridView)) {
                ((GridView) ResolverActivity.this.mAdapterView).setNumColumns(Math.min(ResolverActivity.this.mAdapter.getCount(), ResolverActivity.this.mMaxColumns));
            }
        }

        public DisplayResolveInfo getFilteredItem() {
            if (!this.mFilterLastUsed || this.mLastChosenPosition < 0) {
                return null;
            }
            return (DisplayResolveInfo) this.mDisplayList.get(this.mLastChosenPosition);
        }

        public DisplayResolveInfo getOtherProfile() {
            return this.mOtherProfile;
        }

        public int getFilteredPosition() {
            if (!this.mFilterLastUsed || this.mLastChosenPosition < 0) {
                return -1;
            }
            return this.mLastChosenPosition;
        }

        public boolean hasFilteredItem() {
            return this.mFilterLastUsed && this.mLastChosenPosition >= 0;
        }

        public float getScore(DisplayResolveInfo target) {
            return ResolverActivity.this.mResolverComparator.getScore(target.getResolvedComponentName());
        }

        private void rebuildList() {
            int N;
            int i;
            int j;
            ActivityInfo ai;
            List<ResolvedComponentInfo> currentResolveList = null;
            try {
                Intent primaryIntent = ResolverActivity.this.getTargetIntent();
                this.mLastChosen = AppGlobals.getPackageManager().getLastChosenActivity(primaryIntent, primaryIntent.resolveTypeIfNeeded(ResolverActivity.this.getContentResolver()), 65536);
            } catch (RemoteException re) {
                Log.d(ResolverActivity.TAG, "Error calling setLastChosenActivity\n" + re);
            }
            this.mOtherProfile = null;
            this.mDisplayList.clear();
            if (this.mBaseResolveList != null) {
                currentResolveList = new ArrayList();
                this.mOrigResolveList = currentResolveList;
                addResolveListDedupe(currentResolveList, ResolverActivity.this.getTargetIntent(), this.mBaseResolveList);
            } else {
                boolean shouldGetResolvedFilter = shouldGetResolvedFilter();
                boolean shouldGetActivityMetadata = ResolverActivity.this.shouldGetActivityMetadata();
                N = this.mIntents.size();
                for (i = 0; i < N; i++) {
                    int i2;
                    Intent intent = (Intent) this.mIntents.get(i);
                    try {
                        if ("android.intent.action.GET_CONTENT".equals(intent.getAction())) {
                            if (1 == intent.getIntExtra(ResolverActivity.DOCUMENTS_UI_POLICY, 0)) {
                                ResolverActivity.this.mDropsDocumentsUI = true;
                            }
                        }
                    } catch (Exception e) {
                        Log.d(ResolverActivity.TAG, "Exception occures while getting values from current intent !");
                    }
                    PackageManager access$300 = ResolverActivity.this.mPm;
                    int i3 = 65536 | (shouldGetResolvedFilter ? 64 : 0);
                    if (shouldGetActivityMetadata) {
                        i2 = 128;
                    } else {
                        i2 = 0;
                    }
                    List<ResolveInfo> infos = access$300.queryIntentActivities(intent, i2 | i3);
                    if (infos != null) {
                        if (currentResolveList == null) {
                            currentResolveList = new ArrayList();
                            this.mOrigResolveList = currentResolveList;
                        }
                        addResolveListDedupe(currentResolveList, intent, infos);
                        if (i == 0 && ResolverActivity.this.mDropComponents.size() > 0) {
                            j = 0;
                            while (j < ResolverActivity.this.mDropComponents.size()) {
                                int k = currentResolveList.size() - 1;
                                while (k >= 0) {
                                    ai = ((ResolvedComponentInfo) currentResolveList.get(k)).getResolveInfoAt(0).activityInfo;
                                    if (ai.packageName.equals(((ComponentName) ResolverActivity.this.mDropComponents.get(j)).getPackageName()) && ai.name.equals(((ComponentName) ResolverActivity.this.mDropComponents.get(j)).getClassName())) {
                                        if (this.mOrigResolveList == currentResolveList) {
                                            this.mOrigResolveList = new ArrayList(this.mOrigResolveList);
                                        }
                                        currentResolveList.remove(k);
                                        j++;
                                    } else {
                                        k--;
                                    }
                                }
                                j++;
                            }
                        }
                    }
                }
                if (currentResolveList != null) {
                    for (i = currentResolveList.size() - 1; i >= 0; i--) {
                        ai = ((ResolvedComponentInfo) currentResolveList.get(i)).getResolveInfoAt(0).activityInfo;
                        if (ActivityManager.checkComponentPermission(ResolverActivity.this.getTargetIntent(), ai.permission, this.mLaunchedFromPackage, this.mLaunchedFromUid, ai.applicationInfo.uid, ai.applicationInfo.packageName, ai.exported, false) != 0) {
                            if (this.mOrigResolveList == currentResolveList) {
                                this.mOrigResolveList = new ArrayList(this.mOrigResolveList);
                            }
                            currentResolveList.remove(i);
                        }
                    }
                }
            }
            if (currentResolveList != null) {
                N = currentResolveList.size();
                if (N > 0) {
                    ResolveInfo r0;
                    ResolveInfo ri;
                    int to = N;
                    int from = N;
                    int lastUserId = UserHandle.getUserId(((ResolvedComponentInfo) currentResolveList.get(N - 1)).getResolveInfoAt(0).activityInfo.applicationInfo.uid);
                    j = N - 1;
                    while (j >= 0) {
                        if (ResolverActivity.this.isForKnoxNFC()) {
                            int currentUserId = UserHandle.getUserId(((ResolvedComponentInfo) currentResolveList.get(j)).getResolveInfoAt(0).activityInfo.applicationInfo.uid);
                            if (lastUserId != currentUserId || j <= 0) {
                                to = from;
                                if (j != 0) {
                                    from = j + 1;
                                } else if (j != 0 || lastUserId == currentUserId) {
                                    from = j;
                                } else {
                                    j++;
                                    from = j;
                                }
                                lastUserId = currentUserId;
                                N = to;
                                r0 = ((ResolvedComponentInfo) currentResolveList.get(from)).getResolveInfoAt(0);
                                i = from + 1;
                                while (i < N) {
                                    ri = ((ResolvedComponentInfo) currentResolveList.get(i)).getResolveInfoAt(0);
                                    if (!ResolverActivity.this.isForKnoxNFC() && UserHandle.getUserId(ri.activityInfo.applicationInfo.uid) >= 100) {
                                        Log.d(ResolverActivity.TAG, "Bypassing default and priority check for NFC " + ri);
                                    } else if (r0.priority == ri.priority || r0.isDefault != ri.isDefault) {
                                        while (i < N) {
                                            if (this.mOrigResolveList == currentResolveList) {
                                                this.mOrigResolveList = new ArrayList(this.mOrigResolveList);
                                            }
                                            if (!ResolverActivity.this.mIsDeviceDefault && r0.activityInfo.name.toLowerCase().contains(".documentsui") && ResolverActivity.this.mDropsDocumentsUI) {
                                                currentResolveList.remove(0);
                                                this.mOrigResolveList.remove(0);
                                                N--;
                                                i = N;
                                                break;
                                            }
                                            currentResolveList.remove(i);
                                            N--;
                                        }
                                    }
                                    i++;
                                }
                            }
                        } else {
                            from = 0;
                            j = -1;
                            r0 = ((ResolvedComponentInfo) currentResolveList.get(from)).getResolveInfoAt(0);
                            i = from + 1;
                            while (i < N) {
                                ri = ((ResolvedComponentInfo) currentResolveList.get(i)).getResolveInfoAt(0);
                                if (!ResolverActivity.this.isForKnoxNFC()) {
                                }
                                if (r0.priority == ri.priority) {
                                }
                                while (i < N) {
                                    if (this.mOrigResolveList == currentResolveList) {
                                        this.mOrigResolveList = new ArrayList(this.mOrigResolveList);
                                    }
                                    if (!ResolverActivity.this.mIsDeviceDefault) {
                                    }
                                    currentResolveList.remove(i);
                                    N--;
                                }
                                i++;
                            }
                        }
                        j--;
                    }
                    if (ResolverActivity.this.isForKnoxNFC()) {
                        N = currentResolveList.size();
                    }
                    if (N > 1) {
                        try {
                            ResolverActivity.this.mResolverComparator.compute(currentResolveList);
                            Collections.sort(currentResolveList, ResolverActivity.this.mResolverComparator);
                        } catch (Exception e2) {
                            Log.e(ResolverActivity.TAG, "Exception occures during sorting app list !!!");
                        }
                    }
                    if (this.mInitialIntents != null) {
                        for (Intent ii : this.mInitialIntents) {
                            if (ii != null) {
                                ai = ii.resolveActivityInfo(ResolverActivity.this.getPackageManager(), 0);
                                if (ai == null) {
                                    Log.w(ResolverActivity.TAG, "No activity found for " + ii);
                                } else {
                                    ri = new ResolveInfo();
                                    ri.activityInfo = ai;
                                    UserManager userManager = (UserManager) ResolverActivity.this.getSystemService(ImsConferenceState.USER);
                                    if (ii instanceof LabeledIntent) {
                                        LabeledIntent li = (LabeledIntent) ii;
                                        ri.resolvePackageName = li.getSourcePackage();
                                        ri.labelRes = li.getLabelResource();
                                        ri.nonLocalizedLabel = li.getNonLocalizedLabel();
                                        ri.icon = li.getIconResource();
                                        ri.iconResourceId = ri.icon;
                                    }
                                    if (userManager.isManagedProfile()) {
                                        ri.noResourceId = true;
                                        ri.icon = 0;
                                    }
                                    addResolveInfo(new DisplayResolveInfo(ii, ri, ri.loadLabel(ResolverActivity.this.getPackageManager()), null, ii));
                                }
                            }
                        }
                    }
                    ResolvedComponentInfo rci0 = (ResolvedComponentInfo) currentResolveList.get(0);
                    r0 = rci0.getResolveInfoAt(0);
                    int start = 0;
                    CharSequence r0Label = r0.loadLabel(ResolverActivity.this.mPm);
                    this.mHasExtendedInfo = false;
                    ResolverActivity.this.mShowExtended = false;
                    for (i = 1; i < N; i++) {
                        if (r0Label == null) {
                            r0Label = r0.activityInfo.packageName;
                        }
                        ResolvedComponentInfo rci = (ResolvedComponentInfo) currentResolveList.get(i);
                        ri = rci.getResolveInfoAt(0);
                        CharSequence riLabel = ri.loadLabel(ResolverActivity.this.mPm);
                        if (riLabel == null) {
                            riLabel = ri.activityInfo.packageName;
                        }
                        if (riLabel.equals(r0Label)) {
                            if (ResolverActivity.this.isForKnoxNFC()) {
                                if (UserHandle.getUserId(r0.activityInfo.applicationInfo.uid) == UserHandle.getUserId(ri.activityInfo.applicationInfo.uid)) {
                                    Log.d(ResolverActivity.TAG, "Checking uid for NFC " + r0 + " and " + ri);
                                }
                            }
                        }
                        processGroup(currentResolveList, start, i - 1, rci0, r0Label);
                        rci0 = rci;
                        r0 = ri;
                        r0Label = riLabel;
                        start = i;
                    }
                    processGroup(currentResolveList, start, N - 1, rci0, r0Label);
                }
            }
            if (this.mOtherProfile != null && this.mLastChosenPosition >= 0) {
                this.mLastChosenPosition = -1;
                this.mFilterLastUsed = false;
            }
            onListRebuilt();
        }

        private void addResolveListDedupe(List<ResolvedComponentInfo> into, Intent intent, List<ResolveInfo> from) {
            int fromCount = from.size();
            int intoCount = into.size();
            for (int i = 0; i < fromCount; i++) {
                ResolveInfo newInfo = (ResolveInfo) from.get(i);
                boolean found = false;
                for (int j = 0; j < intoCount; j++) {
                    ResolvedComponentInfo rci = (ResolvedComponentInfo) into.get(i);
                    if (isSameResolvedComponent(newInfo, rci)) {
                        found = true;
                        rci.add(intent, newInfo);
                        break;
                    }
                }
                if (!found) {
                    into.add(new ResolvedComponentInfo(new ComponentName(newInfo.activityInfo.packageName, newInfo.activityInfo.name), intent, newInfo));
                }
            }
        }

        private boolean isSameResolvedComponent(ResolveInfo a, ResolvedComponentInfo b) {
            ActivityInfo ai = a.activityInfo;
            return ai.packageName.equals(b.name.getPackageName()) && ai.name.equals(b.name.getClassName());
        }

        public void onListRebuilt() {
        }

        public boolean shouldGetResolvedFilter() {
            return this.mFilterLastUsed;
        }

        private void processGroup(List<ResolvedComponentInfo> rList, int start, int end, ResolvedComponentInfo ro, CharSequence roLabel) {
            if ((end - start) + 1 == 1) {
                addResolveInfoWithAlternates(ro, null, roLabel);
                return;
            }
            this.mHasExtendedInfo = true;
            ResolverActivity.this.mShowExtended = true;
            boolean usePkg = false;
            CharSequence startApp = ro.getResolveInfoAt(0).activityInfo.applicationInfo.loadLabel(ResolverActivity.this.mPm);
            if (startApp == null) {
                usePkg = true;
            }
            if (!usePkg) {
                HashSet<CharSequence> duplicates = new HashSet();
                duplicates.add(startApp);
                int j = start + 1;
                while (j <= end) {
                    CharSequence jApp = ((ResolvedComponentInfo) rList.get(j)).getResolveInfoAt(0).activityInfo.applicationInfo.loadLabel(ResolverActivity.this.mPm);
                    if (jApp == null || duplicates.contains(jApp)) {
                        usePkg = true;
                        break;
                    } else {
                        duplicates.add(jApp);
                        j++;
                    }
                }
                duplicates.clear();
            }
            for (int k = start; k <= end; k++) {
                CharSequence extraInfo;
                ResolvedComponentInfo rci = (ResolvedComponentInfo) rList.get(k);
                ResolveInfo add = rci.getResolveInfoAt(0);
                if (usePkg) {
                    extraInfo = add.activityInfo.packageName;
                } else {
                    extraInfo = add.activityInfo.applicationInfo.loadLabel(ResolverActivity.this.mPm);
                }
                addResolveInfoWithAlternates(rci, extraInfo, roLabel);
            }
        }

        private void addResolveInfoWithAlternates(ResolvedComponentInfo rci, CharSequence extraInfo, CharSequence roLabel) {
            int count = rci.getCount();
            Intent intent = rci.getIntentAt(0);
            ResolveInfo add = rci.getResolveInfoAt(0);
            Intent replaceIntent = ResolverActivity.this.getReplacementIntent(add.activityInfo, intent);
            DisplayResolveInfo dri = new DisplayResolveInfo(intent, add, roLabel, extraInfo, replaceIntent);
            addResolveInfo(dri);
            if (replaceIntent == intent) {
                int N = count;
                for (int i = 1; i < N; i++) {
                    dri.addAlternateSourceIntent(rci.getIntentAt(i));
                }
            }
            updateLastChosenPosition(add);
        }

        private void updateLastChosenPosition(ResolveInfo info) {
            if (this.mLastChosen == null || !this.mLastChosen.activityInfo.packageName.equals(info.activityInfo.packageName) || !this.mLastChosen.activityInfo.name.equals(info.activityInfo.name)) {
                return;
            }
            if (!ResolverActivity.this.isForKnoxNFC()) {
                this.mLastChosenPosition = this.mDisplayList.size() - 1;
            } else if (UserHandle.getUserId(this.mLastChosen.activityInfo.applicationInfo.uid) == UserHandle.getUserId(info.activityInfo.applicationInfo.uid)) {
                this.mLastChosenPosition = this.mDisplayList.size() - 1;
                Log.d(ResolverActivity.TAG, "processGroup set last chosen to NFC " + this.mLastChosenPosition);
            }
        }

        private void addResolveInfo(DisplayResolveInfo dri) {
            if (dri.mResolveInfo.targetUserId == -2 || this.mOtherProfile != null) {
                this.mDisplayList.add(dri);
            } else {
                this.mOtherProfile = dri;
            }
        }

        public ResolveInfo resolveInfoForPosition(int position, boolean filtered) {
            if (ResolverActivity.this.mSupportGridResolver) {
                return ((DisplayResolveInfo) this.mDisplayList.get(position)).getResolveInfo();
            }
            return (filtered ? getItem(position) : (TargetInfo) this.mDisplayList.get(position)).getResolveInfo();
        }

        public TargetInfo targetInfoForPosition(int position, boolean filtered) {
            return filtered ? getItem(position) : (TargetInfo) this.mDisplayList.get(position);
        }

        public int getCount() {
            int result = this.mDisplayList.size();
            if (ResolverActivity.this.mSupportGridResolver || !this.mFilterLastUsed || this.mLastChosenPosition < 0) {
                return result;
            }
            return result - 1;
        }

        public int getUnfilteredCount() {
            return this.mDisplayList.size();
        }

        public int getDisplayInfoCount() {
            return this.mDisplayList.size();
        }

        public DisplayResolveInfo getDisplayInfoAt(int index) {
            return (DisplayResolveInfo) this.mDisplayList.get(index);
        }

        public TargetInfo getItem(int position) {
            if (!ResolverActivity.this.mSupportGridResolver && this.mFilterLastUsed && this.mLastChosenPosition >= 0 && position >= this.mLastChosenPosition) {
                position++;
            }
            return (TargetInfo) this.mDisplayList.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public boolean hasExtendedInfo() {
            return this.mHasExtendedInfo;
        }

        public boolean hasResolvedTarget(ResolveInfo info) {
            int N = this.mDisplayList.size();
            for (int i = 0; i < N; i++) {
                if (ResolverActivity.resolveInfoMatch(info, ((DisplayResolveInfo) this.mDisplayList.get(i)).getResolveInfo())) {
                    return true;
                }
            }
            return false;
        }

        protected int getDisplayResolveInfoCount() {
            return this.mDisplayList.size();
        }

        protected DisplayResolveInfo getDisplayResolveInfo(int index) {
            return (DisplayResolveInfo) this.mDisplayList.get(index);
        }

        public final View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = createView(parent);
            }
            onBindView(view, getItem(position));
            return view;
        }

        public final View createView(ViewGroup parent) {
            View view = onCreateView(parent);
            view.setTag(new ViewHolder(view));
            return view;
        }

        public View onCreateView(ViewGroup parent) {
            if (ResolverActivity.this.mIsDeviceDefault) {
                return this.mInflater.inflate((int) R.layout.tw_resolve_list_item, parent, false);
            }
            return this.mInflater.inflate((int) R.layout.resolve_list_item, parent, false);
        }

        public boolean showsExtendedInfo(TargetInfo info) {
            return !TextUtils.isEmpty(info.getExtendedInfo());
        }

        public final void bindView(int position, View view) {
            onBindView(view, getItem(position));
        }

        private void onBindView(View view, TargetInfo info) {
            ViewHolder holder = (ViewHolder) view.getTag();
            ResolverActivity.this.defaultTextSize = (float) ResolverActivity.this.getResources().getDimensionPixelSize(R.dimen.tw_resolver_item_text_size);
            if (!TextUtils.equals(holder.text.getText(), info.getDisplayLabel())) {
                holder.text.setTextSize(0, ResolverActivity.this.defaultTextSize * ResolverActivity.this.getFontScale());
                holder.text.setText(info.getDisplayLabel());
            }
            if (ResolverActivity.this.mShowExtended) {
                ResolverActivity.this.defaultTextSize = (float) ResolverActivity.this.getResources().getDimensionPixelSize(R.dimen.tw_resolver_item_text_size_secondary);
                holder.text2.setVisibility(0);
                holder.text2.setText(info.getExtendedInfo());
            } else {
                holder.text2.setVisibility(8);
            }
            if ((info instanceof DisplayResolveInfo) && !((DisplayResolveInfo) info).hasDisplayIcon()) {
                new LoadAdapterIconTask((DisplayResolveInfo) info).execute(new Void[0]);
            }
            holder.icon.setImageDrawable(info.getDisplayIcon());
            if (holder.badge != null) {
                Drawable badge = info.getBadgeIcon();
                if (badge != null) {
                    holder.badge.setImageDrawable(badge);
                    holder.badge.setContentDescription(info.getBadgeContentDescription());
                    holder.badge.setVisibility(0);
                    return;
                }
                holder.badge.setVisibility(8);
            }
        }
    }

    public interface TargetInfo {
        TargetInfo cloneFilledIn(Intent intent, int i);

        List<Intent> getAllSourceIntents();

        CharSequence getBadgeContentDescription();

        Drawable getBadgeIcon();

        Drawable getDisplayIcon();

        CharSequence getDisplayLabel();

        CharSequence getExtendedInfo();

        ResolveInfo getResolveInfo();

        ComponentName getResolvedComponentName();

        Intent getResolvedIntent();

        boolean start(Activity activity, Bundle bundle);

        boolean startAsCaller(Activity activity, Bundle bundle, int i);

        boolean startAsUser(Activity activity, Bundle bundle, UserHandle userHandle);
    }

    private enum ActionTitle {
        VIEW("android.intent.action.VIEW", R.string.whichViewApplication, R.string.whichViewApplicationNamed),
        EDIT("android.intent.action.EDIT", R.string.whichEditApplication, R.string.whichEditApplicationNamed),
        SEND("android.intent.action.SEND", R.string.whichApplication, R.string.whichApplicationNamed),
        SENDTO("android.intent.action.SENDTO", R.string.whichApplication, R.string.whichApplicationNamed),
        SEND_MULTIPLE("android.intent.action.SEND_MULTIPLE", R.string.whichApplication, R.string.whichApplicationNamed),
        DEFAULT(null, R.string.whichApplication, R.string.whichApplicationNamed),
        HOME("android.intent.action.MAIN", R.string.whichHomeApplication, R.string.whichHomeApplicationNamed);
        
        public final String action;
        public final int namedTitleRes;
        public final int titleRes;

        private ActionTitle(String action, int titleRes, int namedTitleRes) {
            this.action = action;
            this.titleRes = titleRes;
            this.namedTitleRes = namedTitleRes;
        }

        public static ActionTitle forAction(String action) {
            for (ActionTitle title : values()) {
                if (title != HOME && action != null && action.equals(title.action)) {
                    return title;
                }
            }
            return DEFAULT;
        }
    }

    final class DisplayResolveInfo implements TargetInfo {
        private Drawable mBadge;
        private Drawable mDisplayIcon;
        private final CharSequence mDisplayLabel;
        private final CharSequence mExtendedInfo;
        private final ResolveInfo mResolveInfo;
        private final Intent mResolvedIntent;
        private final List<Intent> mSourceIntents = new ArrayList();

        DisplayResolveInfo(Intent originalIntent, ResolveInfo pri, CharSequence pLabel, CharSequence pInfo, Intent pOrigIntent) {
            this.mSourceIntents.add(originalIntent);
            this.mResolveInfo = pri;
            this.mDisplayLabel = pLabel;
            this.mExtendedInfo = pInfo;
            if (pOrigIntent == null) {
                pOrigIntent = ResolverActivity.this.getReplacementIntent(pri.activityInfo, ResolverActivity.this.getTargetIntent());
            }
            Intent intent = new Intent(pOrigIntent);
            Intent selectorIntent = intent.getSelector();
            if (selectorIntent != null) {
                intent = selectorIntent;
            }
            intent.addFlags(View.SCROLLBARS_OUTSIDE_INSET);
            ActivityInfo ai = this.mResolveInfo.activityInfo;
            intent.setComponent(new ComponentName(ai.applicationInfo.packageName, ai.name));
            this.mResolvedIntent = intent;
        }

        private DisplayResolveInfo(DisplayResolveInfo other, Intent fillInIntent, int flags) {
            this.mSourceIntents.addAll(other.getAllSourceIntents());
            this.mResolveInfo = other.mResolveInfo;
            this.mDisplayLabel = other.mDisplayLabel;
            this.mDisplayIcon = other.mDisplayIcon;
            this.mExtendedInfo = other.mExtendedInfo;
            this.mResolvedIntent = new Intent(other.mResolvedIntent);
            this.mResolvedIntent.fillIn(fillInIntent, flags);
        }

        public ResolveInfo getResolveInfo() {
            return this.mResolveInfo;
        }

        public CharSequence getDisplayLabel() {
            return this.mDisplayLabel;
        }

        public Drawable getDisplayIcon() {
            return this.mDisplayIcon;
        }

        public Drawable getBadgeIcon() {
            if (TextUtils.isEmpty(getExtendedInfo())) {
                return null;
            }
            if (!(this.mBadge != null || this.mResolveInfo == null || this.mResolveInfo.activityInfo == null || this.mResolveInfo.activityInfo.applicationInfo == null)) {
                if (this.mResolveInfo.activityInfo.icon == 0 || this.mResolveInfo.activityInfo.icon == this.mResolveInfo.activityInfo.applicationInfo.icon) {
                    return null;
                }
                this.mBadge = this.mResolveInfo.activityInfo.applicationInfo.loadIcon(ResolverActivity.this.mPm);
            }
            return this.mBadge;
        }

        public CharSequence getBadgeContentDescription() {
            return null;
        }

        public TargetInfo cloneFilledIn(Intent fillInIntent, int flags) {
            return new DisplayResolveInfo(this, fillInIntent, flags);
        }

        public List<Intent> getAllSourceIntents() {
            return this.mSourceIntents;
        }

        public void addAlternateSourceIntent(Intent alt) {
            this.mSourceIntents.add(alt);
        }

        public void setDisplayIcon(Drawable icon) {
            this.mDisplayIcon = icon;
        }

        public boolean hasDisplayIcon() {
            return this.mDisplayIcon != null;
        }

        public CharSequence getExtendedInfo() {
            return this.mExtendedInfo;
        }

        public Intent getResolvedIntent() {
            return this.mResolvedIntent;
        }

        public ComponentName getResolvedComponentName() {
            return new ComponentName(this.mResolveInfo.activityInfo.packageName, this.mResolveInfo.activityInfo.name);
        }

        public boolean start(Activity activity, Bundle options) {
            if (ResolverActivity.this.mSShareCommon.getResolverGuideIntent() != null) {
                activity.startActivity(ResolverActivity.this.mSShareCommon.getResolverGuideIntent(), options);
            } else {
                activity.startActivity(this.mResolvedIntent, options);
            }
            return true;
        }

        public boolean startAsCaller(Activity activity, Bundle options, int userId) {
            if (ResolverActivity.this.mSShareCommon.getResolverGuideIntent() != null) {
                activity.startActivityAsCaller(ResolverActivity.this.mSShareCommon.getResolverGuideIntent(), options, false, userId);
            } else {
                activity.startActivityAsCaller(this.mResolvedIntent, options, false, userId);
            }
            return true;
        }

        public boolean startAsUser(Activity activity, Bundle options, UserHandle user) {
            activity.startActivityAsUser(this.mResolvedIntent, options, user);
            return false;
        }
    }

    class ItemClickListener implements OnItemClickListener, OnItemLongClickListener {
        ItemClickListener() {
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            boolean hasValidSelection;
            boolean z = false;
            if (!ResolverActivity.this.mSupportGridResolver) {
                ListView listView = parent instanceof ListView ? (ListView) parent : null;
                if (listView != null) {
                    position -= listView.getHeaderViewsCount();
                }
                if (position < 0) {
                    return;
                }
            }
            int checkedPos = ResolverActivity.this.mAdapterView.getCheckedItemPosition();
            if (checkedPos != -1) {
                hasValidSelection = true;
            } else {
                hasValidSelection = false;
            }
            if (!ResolverActivity.this.mAlwaysUseOption || ((hasValidSelection && ResolverActivity.this.mLastSelected == checkedPos) || !ResolverActivity.this.mSupportButtons)) {
                ResolverActivity resolverActivity = ResolverActivity.this;
                if (ResolverActivity.this.mAlwaysUseOption && !ResolverActivity.this.mSupportButtons) {
                    z = true;
                }
                resolverActivity.startSelected(position, z, true);
                return;
            }
            ResolverActivity.this.setAlwaysButtonEnabled(hasValidSelection, checkedPos, true);
            ResolverActivity.this.mOnceButton.setEnabled(hasValidSelection);
            if (hasValidSelection) {
                ResolverActivity.this.mAdapterView.smoothScrollToPosition(checkedPos);
            }
            ResolverActivity.this.mLastSelected = checkedPos;
        }

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (!ResolverActivity.this.mSupportGridResolver) {
                ListView listView = parent instanceof ListView ? (ListView) parent : null;
                if (listView != null) {
                    position -= listView.getHeaderViewsCount();
                }
                if (position < 0) {
                    return false;
                }
            }
            ResolverActivity.this.showAppDetails(ResolverActivity.this.mAdapter.resolveInfoForPosition(position, true));
            return true;
        }
    }

    abstract class LoadIconTask extends AsyncTask<Void, Void, Drawable> {
        protected final DisplayResolveInfo mDisplayResolveInfo;
        private final ResolveInfo mResolveInfo;

        public LoadIconTask(DisplayResolveInfo dri) {
            this.mDisplayResolveInfo = dri;
            this.mResolveInfo = dri.getResolveInfo();
        }

        protected Drawable doInBackground(Void... params) {
            Process.setThreadPriority(-4);
            return ResolverActivity.this.loadIconForResolveInfo(this.mResolveInfo);
        }

        protected void onPostExecute(Drawable d) {
            this.mDisplayResolveInfo.setDisplayIcon(d);
        }
    }

    class LoadAdapterIconTask extends LoadIconTask {
        public LoadAdapterIconTask(DisplayResolveInfo dri) {
            super(dri);
        }

        protected void onPostExecute(Drawable d) {
            super.onPostExecute(d);
            if (ResolverActivity.this.mProfileView != null && ResolverActivity.this.mAdapter.getOtherProfile() == this.mDisplayResolveInfo) {
                ResolverActivity.this.bindProfileView();
            }
            ResolverActivity.this.mAdapter.notifyDataSetChanged();
        }
    }

    class LoadIconIntoViewTask extends LoadIconTask {
        private final ImageView mTargetView;

        public LoadIconIntoViewTask(DisplayResolveInfo dri, ImageView target) {
            super(dri);
            this.mTargetView = target;
        }

        protected void onPostExecute(Drawable d) {
            super.onPostExecute(d);
            if (ResolverActivity.this.isForKnoxNFC()) {
                int userId = UserHandle.getUserId(this.mDisplayResolveInfo.getResolveInfo().activityInfo.applicationInfo.uid);
                if (PersonaManager.isKnoxId(userId)) {
                    Log.d(ResolverActivity.TAG, "Setting NFC badged icon for " + userId);
                    d = ResolverActivity.this.mPm.getUserBadgedIcon(this.mDisplayResolveInfo.getDisplayIcon(), new UserHandle(userId));
                }
            }
            this.mTargetView.setImageDrawable(d);
        }
    }

    class MoreActionsItemClickListener implements OnItemClickListener, OnItemLongClickListener {
        MoreActionsItemClickListener() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (ResolverActivity.this.mMoreActions != null) {
                ResolverActivity.this.mMoreActions.startMoreActions(position, (Intent) ResolverActivity.this.mIntents.get(0));
                if (ResolverActivity.this.mVisibleArea != null) {
                    ResolverActivity.this.mMoreActions.setSharePanelVisibleHeight(ResolverActivity.this.mVisibleArea.getHeight());
                } else {
                    Log.d(ResolverActivity.TAG, "mVisibleArea is null !");
                }
            }
            ResolverActivity.this.finish();
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            return true;
        }
    }

    static class PageItemViewHolder {
        public ImageView badge;
        public ImageView icon;
        public TextView text;
        public TextView text2;
        public TextView text3;

        public PageItemViewHolder(View view) {
            this.text = (TextView) view.findViewById(R.id.text1);
            this.text2 = (TextView) view.findViewById(R.id.text2);
            this.text3 = (TextView) view.findViewById(R.id.text3);
            this.icon = (ImageView) view.findViewById(R.id.icon);
            this.badge = (ImageView) view.findViewById(R.id.target_badge);
        }
    }

    class PageResolverListAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        List<String> mKey = new ArrayList();
        private ResolveInfo mLastChosen;
        List<TargetInfo> mList = new ArrayList();

        public PageResolverListAdapter(Context context) {
            this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        public int getCount() {
            return this.mList.size();
        }

        public TargetInfo getItem(int position) {
            return (TargetInfo) this.mList.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = this.mInflater.inflate((int) R.layout.tw_resolver_pagemode_page_list_item, parent, false);
                view.setTag(new PageItemViewHolder(view));
            } else {
                view = convertView;
            }
            bindView(view, getItem(position));
            return view;
        }

        private final void bindView(View view, TargetInfo info) {
            PageItemViewHolder holder = (PageItemViewHolder) view.getTag();
            float fontScale = ResolverActivity.this.getFontScale();
            if (!ResolverActivity.this.mSupportSimpleSharing) {
                holder.text.setLines(2);
            }
            if (info.getExtendedInfo() == null || ResolverActivity.this.isLandscapeMode()) {
                holder.text3.setVisibility(8);
                ResolverActivity.this.defaultTextSize = (float) ResolverActivity.this.getResources().getDimensionPixelSize(R.dimen.tw_resolver_item_text_size);
                holder.text.setVisibility(0);
                holder.text.setTextSize(0, ResolverActivity.this.defaultTextSize * fontScale);
                holder.text.setText(info.getDisplayLabel());
                holder.text2.setVisibility(8);
            } else {
                holder.text.setVisibility(8);
                ResolverActivity.this.defaultTextSize = (float) ResolverActivity.this.getResources().getDimensionPixelSize(R.dimen.tw_resolver_item_text_size);
                holder.text3.setVisibility(0);
                holder.text3.setTextSize(0, ResolverActivity.this.defaultTextSize * fontScale);
                holder.text3.setText(info.getDisplayLabel());
                ResolverActivity.this.defaultTextSize = (float) ResolverActivity.this.getResources().getDimensionPixelSize(R.dimen.tw_resolver_item_text_size_secondary);
                holder.text2.setVisibility(0);
                holder.text2.setTextSize(0, ResolverActivity.this.defaultTextSize * fontScale);
                holder.text2.setText(info.getExtendedInfo());
            }
            if (!(info instanceof DisplayResolveInfo) || ((DisplayResolveInfo) info).hasDisplayIcon()) {
                holder.icon.setImageDrawable(info.getDisplayIcon());
            } else {
                new LoadIconIntoViewTask((DisplayResolveInfo) info, holder.icon).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            }
            if (holder.badge != null) {
                Drawable badge = info.getBadgeIcon();
                if (badge != null) {
                    holder.badge.setImageDrawable(badge);
                    holder.badge.setContentDescription(info.getBadgeContentDescription());
                    holder.badge.setVisibility(0);
                    return;
                }
                holder.badge.setVisibility(8);
            }
        }
    }

    class PagerItemClickListener implements OnItemClickListener, OnItemLongClickListener {
        PagerItemClickListener() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            position = ResolverActivity.this.convertPageModePosition(position);
            ResolverActivity resolverActivity = ResolverActivity.this;
            boolean z = ResolverActivity.this.mAlwaysUseOption && !ResolverActivity.this.mSupportButtons;
            resolverActivity.startSelected(position, z, true);
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            ResolverActivity.this.showAppDetails(ResolverActivity.this.mAdapter.resolveInfoForPosition(ResolverActivity.this.convertPageModePosition(position), true));
            return true;
        }
    }

    static class PickTargetOptionRequest extends PickOptionRequest {
        public PickTargetOptionRequest(Prompt prompt, Option[] options, Bundle extras) {
            super(prompt, options, extras);
        }

        public void onCancel() {
            super.onCancel();
            ResolverActivity ra = (ResolverActivity) getActivity();
            if (ra != null) {
                ra.mPickOptionRequest = null;
                ra.finish();
            }
        }

        public void onPickOptionResult(boolean finished, Option[] selections, Bundle result) {
            super.onPickOptionResult(finished, selections, result);
            if (selections.length == 1) {
                ResolverActivity ra = (ResolverActivity) getActivity();
                if (ra != null && ra.onTargetSelected(ra.mAdapter.getItem(selections[0].getIndex()), false)) {
                    ra.mPickOptionRequest = null;
                    ra.finish();
                }
            }
        }
    }

    static final class ResolvedComponentInfo {
        private final List<Intent> mIntents = new ArrayList();
        private final List<ResolveInfo> mResolveInfos = new ArrayList();
        public final ComponentName name;

        public ResolvedComponentInfo(ComponentName name, Intent intent, ResolveInfo info) {
            this.name = name;
            add(intent, info);
        }

        public void add(Intent intent, ResolveInfo info) {
            this.mIntents.add(intent);
            this.mResolveInfos.add(info);
        }

        public int getCount() {
            return this.mIntents.size();
        }

        public Intent getIntentAt(int index) {
            return index >= 0 ? (Intent) this.mIntents.get(index) : null;
        }

        public ResolveInfo getResolveInfoAt(int index) {
            return index >= 0 ? (ResolveInfo) this.mResolveInfos.get(index) : null;
        }

        public int findIntent(Intent intent) {
            int N = this.mIntents.size();
            for (int i = 0; i < N; i++) {
                if (intent.equals(this.mIntents.get(i))) {
                    return i;
                }
            }
            return -1;
        }

        public int findResolveInfo(ResolveInfo info) {
            int N = this.mResolveInfos.size();
            for (int i = 0; i < N; i++) {
                if (info.equals(this.mResolveInfos.get(i))) {
                    return i;
                }
            }
            return -1;
        }
    }

    private class ResolverPageChangeListener implements OnPageChangeListener {
        private ResolverPageChangeListener() {
        }

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int position) {
            ResolverActivity.this.mCurrentPageIdx = position;
            ((ImageView) ResolverActivity.this.mViewPagerNavi.getChildAt(ResolverActivity.this.mViewPagerNaviPrevPage)).setImageResource(R.drawable.tw_resolver_navigation_dot_unfocused);
            ((ImageView) ResolverActivity.this.mViewPagerNavi.getChildAt(position)).setImageResource(R.drawable.tw_resolver_navigation_dot_focused);
            ResolverActivity.this.mViewPagerNaviPrevPage = position;
        }
    }

    class ResolverPagerAdapter extends PagerAdapter {
        private final LayoutInflater mInflater;

        public ResolverPagerAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        public int getItemPosition(Object object) {
            return -2;
        }

        public int getCount() {
            return ResolverActivity.this.mGridResolveAdapterList.size();
        }

        public Object instantiateItem(View pager, int position) {
            ListAdapter gridResolveListAdapter = (PageResolverListAdapter) ResolverActivity.this.mGridResolveAdapterList.get(position);
            View v = this.mInflater.inflate((int) R.layout.tw_resolver_pagemode_page, null);
            GridView gridView = (GridView) v.findViewById(R.id.tw_resolver_pagemode_page_list);
            gridView.setAdapter(gridResolveListAdapter);
            PagerItemClickListener listener = new PagerItemClickListener();
            gridView.setOnItemClickListener(listener);
            gridView.setOnItemLongClickListener(listener);
            int nCount = gridResolveListAdapter.getCount();
            if (position != 0 || ResolverActivity.this.mTotalCount > ResolverActivity.this.mMaxColumns) {
                gridView.setNumColumns(ResolverActivity.this.mMaxColumns);
                gridView.setGravity(1);
            } else {
                int paddingLeft = gridView.getPaddingLeft();
                int paddingRight = gridView.getPaddingRight();
                gridView.setNumColumns(nCount);
                gridView.setGravity(17);
                gridView.setPadding(paddingLeft, 0, paddingRight, 0);
                ((LinearLayout) v).setGravity(17);
            }
            ((ViewPager) pager).addView(v, 0);
            return v;
        }

        public void destroyItem(View pager, int position, Object view) {
            ((ViewPager) pager).removeView((View) view);
        }

        public boolean isViewFromObject(View pager, Object obj) {
            return pager == obj;
        }

        public Parcelable saveState() {
            return null;
        }

        public void startUpdate(View view) {
        }

        public void finishUpdate(View view) {
        }
    }

    class SShareItemClickListener implements OnItemClickListener, OnItemLongClickListener {
        SShareItemClickListener() {
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (parent == null) {
                return;
            }
            if (ResolverActivity.this.mSimpleSharing.isEasySignUpCertificated()) {
                ResolverActivity.this.mSimpleSharing.recentHistoryGridItemClick(position);
                ResolverActivity.this.finish();
            } else if (parent != null) {
                ResolverActivity.this.mSimpleSharing.recentHistoryDefaultGridItemClick(position);
            }
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            return true;
        }
    }

    static class ViewHolder {
        public ImageView badge;
        public ImageView icon;
        public TextView text;
        public TextView text2;

        public ViewHolder(View view) {
            this.text = (TextView) view.findViewById(R.id.text1);
            this.text2 = (TextView) view.findViewById(R.id.text2);
            this.icon = (ImageView) view.findViewById(R.id.icon);
            this.badge = (ImageView) view.findViewById(R.id.target_badge);
        }
    }

    private Intent makeMyIntent() {
        Intent intent = new Intent(getIntent());
        intent.setComponent(null);
        intent.setFlags(intent.getFlags() & -8388609);
        return intent;
    }

    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = makeMyIntent();
        Set<String> categories = intent.getCategories();
        if ("android.intent.action.MAIN".equals(intent.getAction()) && categories != null && categories.size() == 1 && categories.contains("android.intent.category.HOME")) {
            this.mResolvingHome = true;
        }
        setSafeForwardingMode(true);
        onCreate(savedInstanceState, intent, null, 0, null, null, true);
    }

    protected void onCreate(Bundle savedInstanceState, Intent intent, CharSequence title, Intent[] initialIntents, List<ResolveInfo> rList, boolean alwaysUseOption) {
        onCreate(savedInstanceState, intent, title, 0, initialIntents, rList, alwaysUseOption);
    }

    protected void onCreate(Bundle savedInstanceState, Intent intent, CharSequence title, int defaultTitleRes, Intent[] initialIntents, List<ResolveInfo> rList, boolean alwaysUseOption) {
        setTheme(R.style.Theme_DeviceDefault_Resolver);
        super.onCreate(savedInstanceState);
        setProfileSwitchMessageId(intent.getContentUserHint());
        this.mIsManagedProfile = ((UserManager) getSystemService(ImsConferenceState.USER)).isManagedProfile();
        LayoutParams lp = getWindow().getAttributes();
        lp.multiWindowFlags |= 256;
        getWindow().setAttributes(lp);
        boolean isShareListAllowed = getEnterprisePolicyEnabled(getBaseContext(), SecContentProviderURI.RESTRICTION3_URI, SecContentProviderURI.RESTRICTIONPOLICY_SHARELIST_METHOD, new String[]{SmartFaceManager.TRUE});
        Log.d(TAG, "onCreate(): isShareListAllowed(" + isShareListAllowed + ")");
        if (isShareListAllowed) {
            try {
                this.mLaunchedFromUid = ActivityManagerNative.getDefault().getLaunchedFromUid(getActivityToken());
            } catch (RemoteException e) {
                this.mLaunchedFromUid = -1;
            }
            if (this.mLaunchedFromUid < 0 || UserHandle.isIsolated(this.mLaunchedFromUid)) {
                finish();
                return;
            }
            this.mSquicleUX = checkSquicleUXRequired();
            try {
                this.mLaunchedFromPackage = ActivityManagerNative.getDefault().getLaunchedFromPackage(getActivityToken());
                Log.w(TAG, "mLaunchedFromPackage=" + this.mLaunchedFromPackage);
            } catch (RemoteException e2) {
                this.mLaunchedFromPackage = null;
                Log.w(TAG, "mLaunchedFromPackage=null");
            }
            this.mPm = getPackageManager();
            this.mPackageMonitor.register(this, getMainLooper(), false);
            this.mRegistered = true;
            this.mIconDpi = ((ActivityManager) getSystemService("activity")).getLauncherLargeIconDensity();
            this.mIntents.add(0, new Intent(intent));
            String referrerPackage = null;
            try {
                referrerPackage = getReferrerPackageName();
            } catch (Exception e3) {
                Log.e(TAG, "getReferrerPackageName error!!!" + e3);
            }
            this.mResolverComparator = new ResolverComparator(this, getTargetIntent(), referrerPackage);
            if (!configureContentView(this.mIntents, initialIntents, rList, alwaysUseOption)) {
                ResolverDrawerLayout rdl = (ResolverDrawerLayout) findViewById(R.id.contentPanel);
                if (rdl != null) {
                    rdl.setOnDismissedListener(new OnDismissedListener() {
                        public void onDismissed() {
                            ResolverActivity.this.finish();
                        }
                    });
                    if (isVoiceInteraction()) {
                        rdl.setCollapsed(false);
                    }
                    this.mResolverDrawerLayout = rdl;
                }
                overridePendingTransition(R.anim.options_panel_enter, 0);
                if (this.mSupportMoreActions) {
                    addMoreActionsView();
                }
                this.mVisibleArea = findViewById(R.id.visibleArea);
                this.mTitlePanelAFW = findViewById(R.id.titlePanel_afw);
                this.mTitlePanelDefault = findViewById(R.id.titlePanel_default);
                if (title == null) {
                    title = getTitleForAction(intent.getAction(), defaultTitleRes);
                }
                if (!TextUtils.isEmpty(title)) {
                    TextView titleTextDefault = (TextView) findViewById(R.id.title_default);
                    this.defaultTextSize = (float) getResources().getDimensionPixelSize(R.dimen.tw_resolver_pagemode_titlepanel_text_size);
                    if (titleTextDefault != null) {
                        titleTextDefault.setTextSize(0, this.defaultTextSize * getFontScale());
                        titleTextDefault.setText(title);
                    }
                    TextView titleTextAFW = (TextView) findViewById(R.id.title_afw);
                    if (titleTextAFW != null) {
                        if (this.mIsManagedProfile) {
                            titleTextAFW.setText(R.string.tw_resolver_afw_personal_title);
                        } else {
                            titleTextAFW.setText(R.string.tw_resolver_afw_title);
                        }
                    }
                    setTitle(title);
                    if (!this.mIsDeviceDefault) {
                        ImageView titleIcon = (ImageView) findViewById(R.id.title_icon);
                        if (titleIcon != null) {
                            ApplicationInfo ai = null;
                            try {
                                if (!TextUtils.isEmpty(referrerPackage)) {
                                    ai = this.mPm.getApplicationInfo(referrerPackage, 0);
                                }
                            } catch (NameNotFoundException e4) {
                                Log.e(TAG, "Could not find referrer package " + referrerPackage);
                            }
                            if (ai != null) {
                                titleIcon.setImageDrawable(ai.loadIcon(this.mPm));
                            }
                        }
                    }
                }
                if (shouldShowSimpleSharing()) {
                    applyRemoteShareResolver(intent);
                    this.mProfileView = findViewById(R.id.profile_button);
                    if (this.mProfileView != null) {
                        this.mProfileView.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                DisplayResolveInfo dri = ResolverActivity.this.mAdapter.getOtherProfile();
                                if (dri != null) {
                                    ResolverActivity.this.mProfileSwitchMessageId = -1;
                                    ResolverActivity.this.onTargetSelected(dri, false);
                                    ResolverActivity.this.finish();
                                }
                            }
                        });
                        bindProfileView();
                        if (this.mTitlePanelDefault != null) {
                            this.mTitlePanelDefault.setVisibility(8);
                        }
                        if (this.mTitlePanelAFW != null && this.mProfileView.getVisibility() == 8) {
                            this.mTitlePanelAFW.setVisibility(8);
                            return;
                        }
                        return;
                    }
                    return;
                }
                this.mTopPanel = findViewById(R.id.topPanel);
                if (this.mTopPanel != null) {
                    this.mTopPanel.setVisibility(8);
                }
                if (this.mTitlePanelAFW != null && this.mAdapter.getOtherProfile() == null) {
                    this.mTitlePanelAFW.setVisibility(8);
                } else if (!(this.mTitlePanelDefault == null || this.mAdapter.getOtherProfile() == null)) {
                    this.mTitlePanelDefault.setVisibility(8);
                }
                ImageView iconView = (ImageView) findViewById(R.id.icon);
                DisplayResolveInfo iconInfo = this.mAdapter.getFilteredItem();
                if (!(iconView == null || iconInfo == null)) {
                    new LoadIconIntoViewTask(iconInfo, iconView).execute(new Void[0]);
                }
                if ((alwaysUseOption || this.mAdapter.hasFilteredItem()) && this.mSupportButtons) {
                    ViewGroup buttonLayout = (ViewGroup) findViewById(R.id.button_bar);
                    if (buttonLayout != null) {
                        buttonLayout.setVisibility(0);
                        this.mAlwaysButton = (Button) buttonLayout.findViewById(R.id.button_always);
                        this.mOnceButton = (Button) buttonLayout.findViewById(R.id.button_once);
                        if (this.mIsDeviceDefault && this.mSupportShowButtonShapes) {
                            this.mAlwaysButton.setBackgroundResource(R.drawable.tw_dialog_btn_show_background_material_light);
                            this.mOnceButton.setBackgroundResource(R.drawable.tw_dialog_btn_show_background_material_light);
                        }
                    } else {
                        this.mAlwaysUseOption = false;
                    }
                }
                if (this.mAdapter.hasFilteredItem() && this.mSupportButtons) {
                    setAlwaysButtonEnabled(true, this.mAdapter.getFilteredPosition(), false);
                    this.mOnceButton.setEnabled(true);
                }
                if (this.mSupportGridResolver) {
                    if (this.mAdapterView instanceof GridView) {
                        ((GridView) this.mAdapterView).setNumColumns(Math.min(this.mAdapter.getCount(), this.mMaxColumns));
                    }
                    if (this.mAdapter.hasFilteredItem() && this.mSupportButtons) {
                        this.mAdapterView.setItemChecked(this.mAdapter.getFilteredPosition(), true);
                        int checkedPos = this.mAdapterView.getCheckedItemPosition();
                        boolean hasValidSelection = checkedPos != -1;
                        if (this.mAlwaysUseOption && !(hasValidSelection && this.mLastSelected == checkedPos)) {
                            setAlwaysButtonEnabled(hasValidSelection, checkedPos, true);
                            this.mOnceButton.setEnabled(hasValidSelection);
                            if (hasValidSelection) {
                                this.mAdapterView.smoothScrollToPosition(checkedPos);
                            }
                            this.mLastSelected = checkedPos;
                        }
                    }
                }
                this.mProfileView = findViewById(R.id.profile_button);
                if (this.mProfileView != null) {
                    this.mProfileView.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            DisplayResolveInfo dri = ResolverActivity.this.mAdapter.getOtherProfile();
                            if (dri != null) {
                                ResolverActivity.this.mProfileSwitchMessageId = -1;
                                ResolverActivity.this.onTargetSelected(dri, false);
                                ResolverActivity.this.finish();
                            }
                        }
                    });
                    bindProfileView();
                }
                if (isVoiceInteraction()) {
                    onSetupVoiceInteraction();
                }
                getWindow().getDecorView().addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                    public void onViewAttachedToWindow(View v) {
                        v.getViewRootImpl().setDrawDuringWindowsAnimating(true);
                    }

                    public void onViewDetachedFromWindow(View v) {
                    }
                });
                return;
            }
            return;
        }
        Log.d(TAG, "onCreate(): ShareList is not allowed");
        finish();
    }

    void onSetupVoiceInteraction() {
        sendVoiceChoicesIfNeeded();
    }

    void sendVoiceChoicesIfNeeded() {
        if (isVoiceInteraction()) {
            Option[] options = new Option[this.mAdapter.getCount()];
            int N = options.length;
            for (int i = 0; i < N; i++) {
                options[i] = optionForChooserTarget(this.mAdapter.getItem(i), i);
            }
            this.mPickOptionRequest = new PickTargetOptionRequest(new Prompt(getTitle()), options, null);
            getVoiceInteractor().submitRequest(this.mPickOptionRequest);
        }
    }

    Option optionForChooserTarget(TargetInfo target, int index) {
        return new Option(target.getDisplayLabel(), index);
    }

    protected final void setAdditionalTargets(Intent[] intents) {
        if (intents != null) {
            for (Intent intent : intents) {
                this.mIntents.add(intent);
            }
        }
    }

    public Intent getTargetIntent() {
        return this.mIntents.isEmpty() ? null : (Intent) this.mIntents.get(0);
    }

    private String getReferrerPackageName() {
        Uri referrer = getReferrer();
        if (referrer == null || !"android-app".equals(referrer.getScheme())) {
            return null;
        }
        return referrer.getHost();
    }

    int getLayoutResource() {
        if (!this.mIsDeviceDefault) {
            return R.layout.resolver_list;
        }
        if (this.mSupportPageMode) {
            return R.layout.tw_resolver_pagemode_list;
        }
        return R.layout.tw_resolver_list;
    }

    void bindProfileView() {
        DisplayResolveInfo dri = this.mAdapter.getOtherProfile();
        if (dri != null) {
            this.mProfileView.setVisibility(0);
            ImageView icon = (ImageView) this.mProfileView.findViewById(R.id.icon);
            TextView text = (TextView) this.mProfileView.findViewById(R.id.text1);
            if (!dri.hasDisplayIcon()) {
                new LoadIconIntoViewTask(dri, icon).execute(new Void[0]);
            }
            icon.setImageDrawable(dri.getDisplayIcon());
            this.defaultTextSize = (float) getResources().getDimensionPixelSize(R.dimen.tw_resolver_item_text_size);
            text.setTextSize(0, this.defaultTextSize * getFontScale());
            text.setText(dri.getDisplayLabel());
            return;
        }
        this.mProfileView.setVisibility(8);
    }

    private void setProfileSwitchMessageId(int contentUserHint) {
        if (contentUserHint != -2 && contentUserHint != UserHandle.myUserId()) {
            UserManager userManager = (UserManager) getSystemService(ImsConferenceState.USER);
            UserInfo originUserInfo = userManager.getUserInfo(contentUserHint);
            UserInfo targetUserInfo = userManager.getUserInfo(userManager.getUserHandle());
            boolean originIsManaged = originUserInfo != null ? originUserInfo.isManagedProfile() && !originUserInfo.isKnoxWorkspace() : false;
            boolean targetIsManaged;
            if (!userManager.isManagedProfile() || targetUserInfo.isKnoxWorkspace()) {
                targetIsManaged = false;
            } else {
                targetIsManaged = true;
            }
            if (originIsManaged && !targetIsManaged) {
                this.mProfileSwitchMessageId = R.string.forward_intent_to_owner;
            } else if (!originIsManaged && targetIsManaged) {
                this.mProfileSwitchMessageId = R.string.forward_intent_to_work;
            }
        }
    }

    public void setSafeForwardingMode(boolean safeForwarding) {
        this.mSafeForwardingMode = safeForwarding;
    }

    protected CharSequence getTitleForAction(String action, int defaultTitleRes) {
        ActionTitle title = this.mResolvingHome ? ActionTitle.HOME : ActionTitle.forAction(action);
        boolean named = this.mSupportGridResolver ? false : this.mAdapter.hasFilteredItem();
        if (title == ActionTitle.DEFAULT && defaultTitleRes != 0) {
            return getString(defaultTitleRes);
        }
        if (!named) {
            return getString(title.titleRes);
        }
        return getString(title.namedTitleRes, new Object[]{this.mAdapter.getFilteredItem().getDisplayLabel()});
    }

    void dismiss() {
        if (!isFinishing()) {
            finish();
        }
    }

    Drawable getIcon(Resources res, int resId) {
        try {
            return res.getDrawableForDensity(resId, this.mIconDpi);
        } catch (NotFoundException e) {
            return null;
        }
    }

    Drawable loadIconForResolveInfo(ResolveInfo ri) {
        try {
            ComponentInfo ci = ri.activityInfo;
            Drawable dr = this.mPm.getCSCPackageItemIcon(ci.icon != 0 ? ci.name : ci.packageName);
            if (dr != null) {
                return dr;
            }
            if (!(ri.resolvePackageName == null || ri.icon == 0)) {
                if (isForKnoxNFC()) {
                    dr = getIcon(this.mPm.getResourcesForApplicationAsUser(ri.resolvePackageName, UserHandle.getUserId(ri.activityInfo.applicationInfo.uid)), ri.icon);
                } else {
                    dr = getIcon(this.mPm.getResourcesForApplication(ri.resolvePackageName), ri.icon);
                }
                if (dr != null) {
                    return dr;
                }
            }
            int iconRes = ri.getIconResource();
            String themePackageName = Settings$System.getString(getContentResolver(), "current_sec_active_themepackage");
            if (iconRes != 0) {
                if (isForKnoxNFC()) {
                    dr = getIcon(this.mPm.getResourcesForApplicationAsUser(ri.activityInfo.packageName, UserHandle.getUserId(ri.activityInfo.applicationInfo.uid)), iconRes);
                } else if (themePackageName != null) {
                    dr = ri.activityInfo.loadIcon(this.mPm);
                } else {
                    dr = getIcon(this.mPm.getResourcesForApplication(ri.activityInfo.packageName), iconRes);
                    if (this.mSquicleUX && this.mPm.shouldPackIntoIconTray(ri.activityInfo.packageName) && (!this.mIsManagedProfile || PersonaManager.isKnoxId(ri.targetUserId))) {
                        PackageManager packageManager = this.mPm;
                        PackageManager packageManager2 = this.mPm;
                        dr = packageManager.getDrawableForIconTray(dr, 1);
                    }
                }
                if (dr != null) {
                    return dr;
                }
            }
            return ri.loadIcon(this.mPm);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Couldn't find resources for package", e);
        }
    }

    protected void onRestart() {
        super.onRestart();
        if (!this.mRegistered) {
            this.mPackageMonitor.register(this, getMainLooper(), false);
            this.mRegistered = true;
        }
        this.mAdapter.handlePackagesChanged();
        if (this.mProfileView != null) {
            bindProfileView();
        }
    }

    protected void onStop() {
        super.onStop();
        if (this.mRegistered) {
            this.mPackageMonitor.unregister();
            this.mRegistered = false;
        }
        if ((getIntent().getFlags() & 268435456) != 0 && !isVoiceInteraction() && !isChangingConfigurations()) {
            Context context = getBaseContext();
            if (this.mLaunchedFromUid == 1001) {
                KeyguardManager km = (KeyguardManager) context.getSystemService("keyguard");
                if (km != null && km.isKeyguardLocked()) {
                    Log.w(TAG, "we don't finish resolver for this exceptional case");
                    return;
                }
            }
            finish();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations() && this.mPickOptionRequest != null) {
            this.mPickOptionRequest.cancel();
        }
    }

    protected void onResume() {
        super.onResume();
        if (this.mSimpleSharing != null) {
            this.mSimpleSharing.registerRecentContactsReceiver();
        }
    }

    protected void onPause() {
        super.onPause();
        if (this.mSimpleSharing != null) {
            this.mSimpleSharing.unregisterRecentContactsReceiver();
        }
    }

    public void finish() {
        if (this.mExitAnimator == null) {
            super.finish();
            overridePendingTransition(R.anim.options_panel_enter, R.anim.options_panel_exit);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + " resultCode=" + resultCode);
        if (requestCode == 1 && resultCode == -1 && this.mSimpleSharing != null) {
            try {
                startActivity(this.mSimpleSharing.getRecentHistoryIntent(0));
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "onActivityResult : startActivity failed!!");
            }
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (this.mAlwaysUseOption && this.mSupportButtons) {
            int checkedPos = this.mAdapterView.getCheckedItemPosition();
            boolean hasValidSelection = checkedPos != -1;
            this.mLastSelected = checkedPos;
            setAlwaysButtonEnabled(hasValidSelection, checkedPos, true);
            this.mOnceButton.setEnabled(hasValidSelection);
            if (hasValidSelection) {
                this.mAdapterView.setSelection(checkedPos);
            }
        }
    }

    private boolean hasManagedProfile() {
        UserManager userManager = (UserManager) getSystemService(ImsConferenceState.USER);
        if (userManager == null) {
            return false;
        }
        try {
            for (UserInfo userInfo : userManager.getProfiles(getUserId())) {
                if (userInfo != null && userInfo.isManagedProfile() && !userInfo.isKnoxWorkspace()) {
                    return true;
                }
            }
            return false;
        } catch (SecurityException e) {
            return false;
        }
    }

    private boolean supportsManagedProfiles(ResolveInfo resolveInfo) {
        try {
            if (getPackageManager().getApplicationInfo(resolveInfo.activityInfo.packageName, 0).targetSdkVersion >= 21) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    private void setAlwaysButtonEnabled(boolean hasValidSelection, int checkedPos, boolean filtered) {
        boolean enabled = false;
        if (hasValidSelection && this.mAdapter.resolveInfoForPosition(checkedPos, filtered).targetUserId == -2) {
            enabled = true;
        }
        this.mAlwaysButton.setEnabled(enabled);
    }

    public void onButtonClick(View v) {
        startSelected(this.mAlwaysUseOption ? this.mAdapterView.getCheckedItemPosition() : this.mAdapter.getFilteredPosition(), v.getId() == R.id.button_always, this.mAlwaysUseOption);
    }

    void startSelected(int which, boolean always, boolean filtered) {
        if (!isFinishing()) {
            ResolveInfo ri = this.mAdapter.resolveInfoForPosition(which, filtered);
            if (this.mResolvingHome && hasManagedProfile() && !supportsManagedProfiles(ri)) {
                Toast.makeText((Context) this, String.format(getResources().getString(R.string.activity_resolver_work_profiles_support), new Object[]{ri.activityInfo.loadLabel(getPackageManager()).toString()}), 1).show();
            } else if (onTargetSelected(this.mAdapter.targetInfoForPosition(which, filtered), always)) {
                finish();
            }
        }
    }

    public Intent getReplacementIntent(ActivityInfo aInfo, Intent defIntent) {
        return defIntent;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected boolean onTargetSelected(com.android.internal.app.ResolverActivity.TargetInfo r45, boolean r46) {
        /*
        r44 = this;
        r40 = r45.getResolveInfo();
        if (r45 == 0) goto L_0x0050;
    L_0x0006:
        r10 = r45.getResolvedIntent();
    L_0x000a:
        if (r10 == 0) goto L_0x01ed;
    L_0x000c:
        r0 = r44;
        r3 = r0.mAlwaysUseOption;
        if (r3 != 0) goto L_0x001c;
    L_0x0012:
        r0 = r44;
        r3 = r0.mAdapter;
        r3 = r3.hasFilteredItem();
        if (r3 == 0) goto L_0x01ed;
    L_0x001c:
        r0 = r44;
        r3 = r0.mAdapter;
        r3 = r3.mOrigResolveList;
        if (r3 == 0) goto L_0x01ed;
    L_0x0024:
        r4 = new android.content.IntentFilter;
        r4.<init>();
        r19 = r10.getAction();
        if (r19 == 0) goto L_0x0034;
    L_0x002f:
        r0 = r19;
        r4.addAction(r0);
    L_0x0034:
        r21 = r10.getCategories();
        if (r21 == 0) goto L_0x0052;
    L_0x003a:
        r28 = r21.iterator();
    L_0x003e:
        r3 = r28.hasNext();
        if (r3 == 0) goto L_0x0052;
    L_0x0044:
        r20 = r28.next();
        r20 = (java.lang.String) r20;
        r0 = r20;
        r4.addCategory(r0);
        goto L_0x003e;
    L_0x0050:
        r10 = 0;
        goto L_0x000a;
    L_0x0052:
        r3 = "android.intent.category.DEFAULT";
        r4.addCategory(r3);
        r0 = r40;
        r3 = r0.match;
        r7 = 268369920; // 0xfff0000 float:2.5144941E-29 double:1.32592358E-315;
        r20 = r3 & r7;
        r23 = r10.getData();
        r3 = 6291456; // 0x600000 float:8.816208E-39 double:3.1083923E-317;
        r0 = r20;
        if (r0 != r3) goto L_0x0076;
    L_0x0069:
        r0 = r44;
        r31 = r10.resolveType(r0);
        if (r31 == 0) goto L_0x0076;
    L_0x0071:
        r0 = r31;
        r4.addDataType(r0);	 Catch:{ MalformedMimeTypeException -> 0x0182 }
    L_0x0076:
        if (r23 == 0) goto L_0x0136;
    L_0x0078:
        r3 = r23.getScheme();
        if (r3 == 0) goto L_0x0136;
    L_0x007e:
        r3 = 6291456; // 0x600000 float:8.816208E-39 double:3.1083923E-317;
        r0 = r20;
        if (r0 != r3) goto L_0x009c;
    L_0x0084:
        r3 = "file";
        r7 = r23.getScheme();
        r3 = r3.equals(r7);
        if (r3 != 0) goto L_0x0136;
    L_0x0090:
        r3 = "content";
        r7 = r23.getScheme();
        r3 = r3.equals(r7);
        if (r3 != 0) goto L_0x0136;
    L_0x009c:
        r3 = r23.getScheme();
        r4.addDataScheme(r3);
        r0 = r40;
        r3 = r0.filter;
        r33 = r3.schemeSpecificPartsIterator();
        if (r33 == 0) goto L_0x00d4;
    L_0x00ad:
        r41 = r23.getSchemeSpecificPart();
    L_0x00b1:
        if (r41 == 0) goto L_0x00d4;
    L_0x00b3:
        r3 = r33.hasNext();
        if (r3 == 0) goto L_0x00d4;
    L_0x00b9:
        r32 = r33.next();
        r32 = (android.os.PatternMatcher) r32;
        r0 = r32;
        r1 = r41;
        r3 = r0.match(r1);
        if (r3 == 0) goto L_0x00b1;
    L_0x00c9:
        r3 = r32.getPath();
        r7 = r32.getType();
        r4.addDataSchemeSpecificPart(r3, r7);
    L_0x00d4:
        r0 = r40;
        r3 = r0.filter;
        r18 = r3.authoritiesIterator();
        if (r18 == 0) goto L_0x0105;
    L_0x00de:
        r3 = r18.hasNext();
        if (r3 == 0) goto L_0x0105;
    L_0x00e4:
        r17 = r18.next();
        r17 = (android.content.IntentFilter.AuthorityEntry) r17;
        r0 = r17;
        r1 = r23;
        r3 = r0.match(r1);
        if (r3 < 0) goto L_0x00de;
    L_0x00f4:
        r37 = r17.getPort();
        r7 = r17.getHost();
        if (r37 < 0) goto L_0x018d;
    L_0x00fe:
        r3 = java.lang.Integer.toString(r37);
    L_0x0102:
        r4.addDataAuthority(r7, r3);
    L_0x0105:
        r0 = r40;
        r3 = r0.filter;
        r33 = r3.pathsIterator();
        if (r33 == 0) goto L_0x0136;
    L_0x010f:
        r35 = r23.getPath();
    L_0x0113:
        if (r35 == 0) goto L_0x0136;
    L_0x0115:
        r3 = r33.hasNext();
        if (r3 == 0) goto L_0x0136;
    L_0x011b:
        r32 = r33.next();
        r32 = (android.os.PatternMatcher) r32;
        r0 = r32;
        r1 = r35;
        r3 = r0.match(r1);
        if (r3 == 0) goto L_0x0113;
    L_0x012b:
        r3 = r32.getPath();
        r7 = r32.getType();
        r4.addDataPath(r3, r7);
    L_0x0136:
        if (r4 == 0) goto L_0x01ed;
    L_0x0138:
        r0 = r44;
        r3 = r0.mAdapter;
        r3 = r3.mOrigResolveList;
        r16 = r3.size();
        r0 = r16;
        r6 = new android.content.ComponentName[r0];
        r5 = 0;
        r27 = 0;
    L_0x0149:
        r0 = r27;
        r1 = r16;
        if (r0 >= r1) goto L_0x0190;
    L_0x014f:
        r0 = r44;
        r3 = r0.mAdapter;
        r3 = r3.mOrigResolveList;
        r0 = r27;
        r3 = r3.get(r0);
        r3 = (com.android.internal.app.ResolverActivity.ResolvedComponentInfo) r3;
        r7 = 0;
        r38 = r3.getResolveInfoAt(r7);
        r3 = new android.content.ComponentName;
        r0 = r38;
        r7 = r0.activityInfo;
        r7 = r7.packageName;
        r0 = r38;
        r9 = r0.activityInfo;
        r9 = r9.name;
        r3.<init>(r7, r9);
        r6[r27] = r3;
        r0 = r38;
        r3 = r0.match;
        if (r3 <= r5) goto L_0x017f;
    L_0x017b:
        r0 = r38;
        r5 = r0.match;
    L_0x017f:
        r27 = r27 + 1;
        goto L_0x0149;
    L_0x0182:
        r25 = move-exception;
        r3 = "ResolverActivity";
        r0 = r25;
        android.util.Log.w(r3, r0);
        r4 = 0;
        goto L_0x0076;
    L_0x018d:
        r3 = 0;
        goto L_0x0102;
    L_0x0190:
        r0 = r40;
        r3 = r0.activityInfo;
        r3 = r3.applicationInfo;
        r3 = r3.uid;
        r8 = android.os.UserHandle.getUserId(r3);
        if (r46 == 0) goto L_0x030a;
    L_0x019e:
        r42 = r44.getUserId();
        r36 = r44.getPackageManager();
        r3 = android.os.PersonaManager.isBBCContainer(r42);
        if (r3 == 0) goto L_0x0266;
    L_0x01ac:
        r3 = "ResolverActivity";
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r9 = "KEA App is selected for user : ";
        r7 = r7.append(r9);
        r0 = r42;
        r7 = r7.append(r0);
        r9 = " Doesn't add as a preferredActivity";
        r7 = r7.append(r9);
        r7 = r7.toString();
        android.util.Log.d(r3, r7);
    L_0x01cc:
        r0 = r40;
        r3 = r0.handleAllWebDataURI;
        if (r3 == 0) goto L_0x02aa;
    L_0x01d2:
        r0 = r36;
        r1 = r42;
        r34 = r0.getDefaultBrowserPackageName(r1);
        r3 = android.text.TextUtils.isEmpty(r34);
        if (r3 == 0) goto L_0x01ed;
    L_0x01e0:
        r0 = r40;
        r3 = r0.activityInfo;
        r3 = r3.packageName;
        r0 = r36;
        r1 = r42;
        r0.setDefaultBrowserPackageName(r3, r1);
    L_0x01ed:
        if (r45 == 0) goto L_0x0395;
    L_0x01ef:
        if (r46 == 0) goto L_0x0200;
    L_0x01f1:
        if (r10 == 0) goto L_0x0200;
    L_0x01f3:
        r0 = r44;
        r3 = r0.mSShareCommon;
        r0 = r44;
        r7 = r0.mSafeForwardingMode;
        r0 = r44;
        r3.setResolverGuideIntent(r0, r10, r7);
    L_0x0200:
        r0 = r44;
        r3 = r0.mSupportLogging;
        if (r3 == 0) goto L_0x021f;
    L_0x0206:
        r0 = r44;
        r3 = r0.mSShareLogging;
        if (r3 == 0) goto L_0x021f;
    L_0x020c:
        r0 = r44;
        r3 = r0.mSShareLogging;
        r0 = r44;
        r7 = r0.mSShareLogging;
        r7 = "APPP";
        r0 = r40;
        r9 = r0.activityInfo;
        r9 = r9.packageName;
        r3.insertLog(r7, r9);
    L_0x021f:
        r3 = r44.isForKnoxNFC();
        if (r3 == 0) goto L_0x0392;
    L_0x0225:
        r0 = r40;
        r3 = r0.activityInfo;
        r3 = r3.applicationInfo;
        r3 = r3.uid;
        r42 = android.os.UserHandle.getUserId(r3);
        r3 = "ResolverActivity";
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r9 = "Launch NFC app ";
        r7 = r7.append(r9);
        r7 = r7.append(r10);
        r9 = " for container ";
        r7 = r7.append(r9);
        r0 = r42;
        r7 = r7.append(r0);
        r7 = r7.toString();
        android.util.Log.d(r3, r7);
        r3 = 0;
        r7 = new android.os.UserHandle;
        r0 = r42;
        r7.<init>(r0);
        r0 = r45;
        r1 = r44;
        r0.startAsUser(r1, r3, r7);
        r3 = 1;
    L_0x0265:
        return r3;
    L_0x0266:
        r3 = r44.isForKnoxNFC();
        if (r3 == 0) goto L_0x029f;
    L_0x026c:
        r3 = "ResolverActivity";
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r9 = "Add NFC default pref ";
        r7 = r7.append(r9);
        r9 = r10.getComponent();
        r7 = r7.append(r9);
        r9 = "for user ";
        r7 = r7.append(r9);
        r7 = r7.append(r8);
        r7 = r7.toString();
        android.util.Log.d(r3, r7);
        r3 = r44.getPackageManager();
        r7 = r10.getComponent();
        r3.addPreferredActivity(r4, r5, r6, r7, r8);
        goto L_0x01cc;
    L_0x029f:
        r3 = r10.getComponent();
        r0 = r36;
        r0.addPreferredActivity(r4, r5, r6, r3);
        goto L_0x01cc;
    L_0x02aa:
        r22 = r10.getComponent();
        r34 = r22.getPackageName();
        if (r23 == 0) goto L_0x02fe;
    L_0x02b4:
        r24 = r23.getScheme();
    L_0x02b8:
        if (r24 == 0) goto L_0x0301;
    L_0x02ba:
        r3 = "http";
        r0 = r24;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x02ce;
    L_0x02c4:
        r3 = "https";
        r0 = r24;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0301;
    L_0x02ce:
        r29 = 1;
    L_0x02d0:
        if (r19 == 0) goto L_0x0304;
    L_0x02d2:
        r3 = "android.intent.action.VIEW";
        r0 = r19;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0304;
    L_0x02dc:
        r30 = 1;
    L_0x02de:
        if (r21 == 0) goto L_0x0307;
    L_0x02e0:
        r3 = "android.intent.category.BROWSABLE";
        r0 = r21;
        r3 = r0.contains(r3);
        if (r3 == 0) goto L_0x0307;
    L_0x02ea:
        r26 = 1;
    L_0x02ec:
        if (r29 == 0) goto L_0x01ed;
    L_0x02ee:
        if (r30 == 0) goto L_0x01ed;
    L_0x02f0:
        if (r26 == 0) goto L_0x01ed;
    L_0x02f2:
        r3 = 2;
        r0 = r36;
        r1 = r34;
        r2 = r42;
        r0.updateIntentVerificationStatus(r1, r3, r2);
        goto L_0x01ed;
    L_0x02fe:
        r24 = 0;
        goto L_0x02b8;
    L_0x0301:
        r29 = 0;
        goto L_0x02d0;
    L_0x0304:
        r30 = 0;
        goto L_0x02de;
    L_0x0307:
        r26 = 0;
        goto L_0x02ec;
    L_0x030a:
        r3 = "ResolverActivity";
        r7 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0375 }
        r7.<init>();	 Catch:{ RemoteException -> 0x0375 }
        r9 = "Set last activity pref for user ";
        r7 = r7.append(r9);	 Catch:{ RemoteException -> 0x0375 }
        r7 = r7.append(r8);	 Catch:{ RemoteException -> 0x0375 }
        r7 = r7.toString();	 Catch:{ RemoteException -> 0x0375 }
        android.util.Log.d(r3, r7);	 Catch:{ RemoteException -> 0x0375 }
        r3 = r44.isForKnoxNFC();	 Catch:{ RemoteException -> 0x0375 }
        if (r3 == 0) goto L_0x035c;
    L_0x0328:
        r3 = "ResolverActivity";
        r7 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0375 }
        r7.<init>();	 Catch:{ RemoteException -> 0x0375 }
        r9 = "Attach user list with only one element ";
        r7 = r7.append(r9);	 Catch:{ RemoteException -> 0x0375 }
        r7 = r7.append(r8);	 Catch:{ RemoteException -> 0x0375 }
        r7 = r7.toString();	 Catch:{ RemoteException -> 0x0375 }
        android.util.Log.d(r3, r7);	 Catch:{ RemoteException -> 0x0375 }
        r43 = new java.util.ArrayList;	 Catch:{ RemoteException -> 0x0375 }
        r3 = 1;
        r3 = new java.lang.Integer[r3];	 Catch:{ RemoteException -> 0x0375 }
        r7 = 0;
        r9 = java.lang.Integer.valueOf(r8);	 Catch:{ RemoteException -> 0x0375 }
        r3[r7] = r9;	 Catch:{ RemoteException -> 0x0375 }
        r3 = java.util.Arrays.asList(r3);	 Catch:{ RemoteException -> 0x0375 }
        r0 = r43;
        r0.<init>(r3);	 Catch:{ RemoteException -> 0x0375 }
        r3 = "com.samsung.sec.knox.EXTRA_KNOX_ARRAY";
        r0 = r43;
        r10.putIntegerArrayListExtra(r3, r0);	 Catch:{ RemoteException -> 0x0375 }
    L_0x035c:
        r9 = android.app.AppGlobals.getPackageManager();	 Catch:{ RemoteException -> 0x0375 }
        r3 = r44.getContentResolver();	 Catch:{ RemoteException -> 0x0375 }
        r11 = r10.resolveTypeIfNeeded(r3);	 Catch:{ RemoteException -> 0x0375 }
        r12 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r15 = r10.getComponent();	 Catch:{ RemoteException -> 0x0375 }
        r13 = r4;
        r14 = r5;
        r9.setLastChosenActivity(r10, r11, r12, r13, r14, r15);	 Catch:{ RemoteException -> 0x0375 }
        goto L_0x01ed;
    L_0x0375:
        r39 = move-exception;
        r3 = "ResolverActivity";
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r9 = "Error calling setLastChosenActivity\n";
        r7 = r7.append(r9);
        r0 = r39;
        r7 = r7.append(r0);
        r7 = r7.toString();
        android.util.Log.d(r3, r7);
        goto L_0x01ed;
    L_0x0392:
        r44.safelyStartActivtyAfterAnimation(r45);
    L_0x0395:
        r3 = 1;
        goto L_0x0265;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.app.ResolverActivity.onTargetSelected(com.android.internal.app.ResolverActivity$TargetInfo, boolean):boolean");
    }

    void safelyStartActivity(TargetInfo cti) {
        if (this.mProfileSwitchMessageId != -1 && (cti == null || cti.getResolveInfo() == null || !PersonaManager.isKnoxId(cti.getResolveInfo().targetUserId))) {
            Toast.makeText((Context) this, getString(this.mProfileSwitchMessageId), 1).show();
        }
        if (this.mSafeForwardingMode) {
            try {
                if (cti.startAsCaller(this, null, DeviceRootKeyServiceManager.ERR_SERVICE_ERROR)) {
                    onActivityStarted(cti);
                }
            } catch (RuntimeException e) {
                String launchedFromPackage;
                try {
                    launchedFromPackage = ActivityManagerNative.getDefault().getLaunchedFromPackage(getActivityToken());
                } catch (RemoteException e2) {
                    launchedFromPackage = "??";
                }
                Slog.wtf(TAG, "Unable to launch as uid " + this.mLaunchedFromUid + " package " + launchedFromPackage + ", while running in " + ActivityThread.currentProcessName(), e);
            }
        } else if (cti.start(this, null)) {
            onActivityStarted(cti);
        }
    }

    void onActivityStarted(TargetInfo cti) {
    }

    boolean shouldGetActivityMetadata() {
        return false;
    }

    boolean shouldAutoLaunchSingleChoice(TargetInfo target) {
        return true;
    }

    void showAppDetails(ResolveInfo ri) {
        startActivity(new Intent().setAction("android.settings.APPLICATION_DETAILS_SETTINGS").setData(Uri.fromParts(AbsCocktailLoadablePanel.PACKAGE_NAME, ri.activityInfo.packageName, null)).addFlags(524288));
    }

    ResolveListAdapter createAdapter(Context context, List<Intent> payloadIntents, Intent[] initialIntents, List<ResolveInfo> rList, int launchedFromUid, boolean filterLastUsed) {
        return new ResolveListAdapter(context, payloadIntents, initialIntents, rList, launchedFromUid, this.mLaunchedFromPackage, filterLastUsed);
    }

    boolean configureContentView(List<Intent> payloadIntents, Intent[] initialIntents, List<ResolveInfo> rList, boolean alwaysUseOption) {
        int layoutId;
        int i = this.mLaunchedFromUid;
        boolean z = alwaysUseOption && !isVoiceInteraction();
        this.mAdapter = createAdapter(this, payloadIntents, initialIntents, rList, i, z);
        this.mSShareCommon = new SShareCommon(getBaseContext(), (Intent) payloadIntents.get(0), this.mIsDeviceDefault, alwaysUseOption, this.mAdapter.hasFilteredItem(), this.mLaunchedFromUid, this.mExtraIntentList, this.mAdapter.getUnfilteredCount());
        this.mMaxColumns = getResources().getInteger(R.integer.config_maxResolverActivityColumns);
        this.mPageItemNum = this.mMaxColumns * 2;
        this.mSupportSimpleSharing = this.mSShareCommon.getSupportSimpleSharing();
        this.mSupportLogging = this.mSShareCommon.getSupportLogging();
        this.mSupportMoreActions = this.mSShareCommon.getSupportMoreActions();
        this.mSupportGridResolver = this.mSShareCommon.getSupportGridResolver();
        this.mSupportPageMode = this.mSShareCommon.getSupportPageMode();
        this.mSupportButtons = this.mSShareCommon.getSupportButtons();
        this.mSupportShowButtonShapes = this.mSShareCommon.getSupportShowButtonShapes();
        if (this.mSupportSimpleSharing) {
            this.mSimpleSharing = new SShareSimpleSharing(this, getBaseContext(), this.mSShareCommon, (Intent) payloadIntents.get(0), this.mLaunchedFromUid, this.mExtraIntentList);
        }
        if (this.mSupportLogging) {
            this.mSShareLogging = new SShareLogging(getBaseContext(), (Intent) payloadIntents.get(0));
            SShareLogging sShareLogging = this.mSShareLogging;
            SShareLogging sShareLogging2 = this.mSShareLogging;
            sShareLogging.insertLog(SShareLogging.SURVEY_FEATURE_START, this.mLaunchedFromPackage);
        }
        if (this.mSupportGridResolver || !this.mAdapter.hasFilteredItem()) {
            layoutId = getLayoutResource();
        } else {
            layoutId = R.layout.resolver_list_with_default;
            alwaysUseOption = false;
        }
        this.mAlwaysUseOption = alwaysUseOption;
        int count = this.mAdapter.getUnfilteredCount();
        if (count == 1 && this.mAdapter.getOtherProfile() == null) {
            TargetInfo target = this.mAdapter.targetInfoForPosition(0, false);
            if (shouldAutoLaunchSingleChoice(target)) {
                safelyStartActivity(target);
                this.mPackageMonitor.unregister();
                this.mRegistered = false;
                finish();
                return true;
            }
        }
        if ((Secure.getInt(getContentResolver(), "user_setup_complete", 0) != 0) || alwaysUseOption || this.mAdapter.hasFilteredItem()) {
            if (count > 0 || shouldShowSimpleSharing() || this.mSupportMoreActions) {
                setContentView(layoutId);
                this.mAdapterView = (AbsListView) findViewById(R.id.resolver_list);
                onPrepareAdapterView(this.mAdapterView, this.mAdapter, alwaysUseOption);
            } else {
                if (this.mIsDeviceDefault) {
                    setContentView(R.layout.tw_resolver_list);
                } else {
                    setContentView(R.layout.resolver_list);
                }
                ((TextView) findViewById(R.id.empty)).setVisibility(0);
                this.mAdapterView = (AbsListView) findViewById(R.id.resolver_list);
                this.mAdapterView.setVisibility(8);
            }
            return false;
        }
        Log.d(TAG, "Blocked for security reason!! Setup is not completed!!");
        if (this.mIsDeviceDefault) {
            setContentView(R.layout.tw_resolver_list);
        } else {
            setContentView(R.layout.resolver_list);
        }
        ((TextView) findViewById(R.id.empty)).setVisibility(0);
        this.mAdapterView = (AbsListView) findViewById(R.id.resolver_list);
        this.mAdapterView.setVisibility(8);
        return false;
    }

    void onPrepareAdapterView(AbsListView adapterView, ResolveListAdapter adapter, boolean alwaysUseOption) {
        if (this.mSupportPageMode) {
            buildUpPagerAdapter();
            preparePageView(adapter.getCount());
            return;
        }
        boolean useHeader = adapter.hasFilteredItem();
        ViewGroup listView = adapterView instanceof ListView ? (ListView) adapterView : null;
        adapterView.setAdapter(this.mAdapter);
        ItemClickListener listener = new ItemClickListener();
        adapterView.setOnItemClickListener(listener);
        adapterView.setOnItemLongClickListener(listener);
        if (!this.mSupportGridResolver) {
            if (alwaysUseOption) {
                listView.setChoiceMode(1);
            }
            if (useHeader && listView != null) {
                listView.addHeaderView(LayoutInflater.from(this).inflate((int) R.layout.resolver_different_item_header, listView, false));
            }
        } else if (alwaysUseOption && this.mSupportButtons) {
            adapterView.setChoiceMode(1);
        }
    }

    static boolean resolveInfoMatch(ResolveInfo lhs, ResolveInfo rhs) {
        return lhs == null ? rhs == null : lhs.activityInfo == null ? rhs.activityInfo == null : Objects.equals(lhs.activityInfo.name, rhs.activityInfo.name) && Objects.equals(lhs.activityInfo.packageName, rhs.activityInfo.packageName);
    }

    static final boolean isSpecificUriMatch(int match) {
        match &= 268369920;
        return match >= 3145728 && match <= TACommandRequest.MAX_BUFFER_SIZE;
    }

    public void setDropComponent(ComponentName comp) {
        this.mDropComponents.add(comp);
    }

    public SShareCommon getSShareCommon() {
        return this.mSShareCommon;
    }

    public boolean shouldShowSimpleSharing() {
        if (this.mSupportSimpleSharing && this.mSimpleSharing != null && this.mSimpleSharing.isRemoteShareServiceEnabled()) {
            return true;
        }
        return false;
    }

    private void applyRemoteShareResolver(Intent intent) {
        if (this.mGridRecentHistory == null) {
            addRecentHistoryGridView(intent);
        }
        if (this.mSimpleSharing != null) {
            this.mSimpleSharing.buildUpSimpleSharingData();
        }
    }

    private void addRecentHistoryGridView(Intent intent) {
        this.mGridRecentHistory = (HorizontalListView) findViewById(R.id.tw_remote_share_recent_history_grid);
        this.mSimpleSharing.setSimpleSharingView(this.mGridRecentHistory, new SShareItemClickListener());
    }

    public ResolverPagerAdapter getPagerAdapter() {
        return this.mPagerAdapter;
    }

    public void updatePagerAdapter(int count) {
        if (this.mSupportPageMode && this.mViewPager != null) {
            clearPagerAdapter();
            buildUpPagerAdapter();
            preparePageView(count);
            this.mPagerAdapter.notifyDataSetChanged();
        }
    }

    private void clearPagerAdapter() {
        if (this.mGridResolveAdapterList != null) {
            this.mGridResolveAdapterList.clear();
        }
        if (this.mResolverListMap != null) {
            this.mResolverListMap.clear();
        }
    }

    Map<String, Integer> getResolverListMap() {
        return this.mResolverListMap;
    }

    List<PageResolverListAdapter> getPageResolverList() {
        return this.mGridResolveAdapterList;
    }

    String getPagerKey() {
        return PAGER_KEY;
    }

    int getPageItemNum() {
        return this.mPageItemNum;
    }

    void preparePageView(int count) {
        this.mTotalCount = count;
        this.mViewPager = (ViewPager) findViewById(R.id.resolver_page);
        if (this.mViewPager != null) {
            ((ResolverDrawerLayout) findViewById(R.id.contentPanel)).forceDisallowInterceptTouchEvent(true);
            ViewGroup.LayoutParams params = this.mViewPager.getLayoutParams();
            if (count > this.mMaxColumns) {
                params.height = getResources().getDimensionPixelSize(R.dimen.tw_resolver_pagemode_page_height);
                if (!this.mSupportSimpleSharing || this.mAlwaysUseOption) {
                    params.height = getResources().getDimensionPixelSize(R.dimen.tw_resolver_pagemode_page_doubleline_app_label_height);
                }
            } else {
                params.height = getResources().getDimensionPixelSize(R.dimen.tw_resolver_pagemode_page_singleline_height);
            }
            this.mViewPager.setLayoutParams(params);
            if (this.mPagerAdapter == null) {
                this.mPagerAdapter = new ResolverPagerAdapter(getBaseContext());
                this.mViewPager.setAdapter(this.mPagerAdapter);
                this.mViewPager.setOnPageChangeListener(new ResolverPageChangeListener());
            }
            this.mViewPagerNavi = (LinearLayout) findViewById(R.id.tw_resolver_page_navi);
            this.mViewPagerBottomSpacing = (LinearLayout) findViewById(R.id.tw_resolver_page_bottom_spacing);
            if (count > this.mPageItemNum) {
                initViewPagerNavi();
                this.mViewPagerNavi.setVisibility(0);
                this.mViewPagerBottomSpacing.setVisibility(8);
                return;
            }
            this.mViewPagerNavi.setVisibility(8);
            this.mViewPagerBottomSpacing.setVisibility(0);
        }
    }

    void buildUpPagerAdapter() {
        int dataCnt = this.mAdapter.getCount();
        int i = 0;
        while (i < dataCnt) {
            int pageIndex = i / this.mPageItemNum;
            int itemIndex = i % this.mPageItemNum;
            if (this.mAdapter.getItem(i) != null) {
                if (itemIndex == 0) {
                    this.mGridResolveAdapterList.add(new PageResolverListAdapter(getBaseContext()));
                }
                String key = String.format(PAGER_KEY, new Object[]{Integer.valueOf(pageIndex), Integer.valueOf(itemIndex)});
                TargetInfo targetInfo = this.mAdapter.getItem(i);
                PageResolverListAdapter pageAdapter = (PageResolverListAdapter) this.mGridResolveAdapterList.get(pageIndex);
                if (pageAdapter != null) {
                    pageAdapter.mList.add(targetInfo);
                    pageAdapter.mKey.add(key);
                }
                this.mResolverListMap.put(key, Integer.valueOf(i));
                i++;
            } else {
                return;
            }
        }
    }

    private int convertPageModePosition(int pos) {
        if (this.mPagerAdapter == null || this.mGridResolveAdapterList == null) {
            return pos;
        }
        String key = String.format(PAGER_KEY, new Object[]{Integer.valueOf(this.mCurrentPageIdx), Integer.valueOf(pos)});
        if (this.mResolverListMap.containsKey(key)) {
            return ((Integer) this.mResolverListMap.get(key)).intValue();
        }
        return pos;
    }

    private boolean isLandscapeMode() {
        return getResources().getConfiguration().orientation == 2;
    }

    private void initViewPagerNavi() {
        boolean isRtlLayout = true;
        if (this.mPagerAdapter != null && this.mPagerAdapter.getCount() != 0) {
            if (getResources().getConfiguration().getLayoutDirection() != 1) {
                isRtlLayout = false;
            }
            int naviSize = getResources().getDimensionPixelSize(R.dimen.tw_resolver_pagemode_navi_size);
            int naviStart = getResources().getDimensionPixelSize(R.dimen.tw_resolver_pagemode_navi_start_padding);
            this.mViewPagerNavi.removeAllViews();
            for (int i = 0; i < this.mPagerAdapter.getCount(); i++) {
                ImageView iv = new ImageView(this);
                iv.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) iv.getLayoutParams();
                if (i == this.mViewPager.getCurrentItem()) {
                    iv.setImageResource(R.drawable.tw_resolver_navigation_dot_focused);
                } else {
                    iv.setImageResource(R.drawable.tw_resolver_navigation_dot_unfocused);
                }
                if (i > 0) {
                    if (isRtlLayout) {
                        lp.rightMargin = naviStart;
                    } else {
                        lp.leftMargin = naviStart;
                    }
                }
                lp.width = naviSize;
                lp.height = naviSize;
                iv.setLayoutParams(lp);
                this.mViewPagerNavi.addView(iv);
                iv.setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        ResolverActivity.this.mViewPager.setCurrentItem(ResolverActivity.this.mViewPagerNavi.indexOfChild(v), true);
                        return true;
                    }
                });
            }
        }
    }

    private float getFontScale() {
        float fontScale = getResources().getConfiguration().fontScale;
        if (fontScale > MAX_FONT_SCALE) {
            return MAX_FONT_SCALE;
        }
        return fontScale;
    }

    private void addMoreActionsView() {
        this.mMoreActions = new SShareMoreActions(this, getBaseContext(), this.mSShareCommon, getWindow(), getReferrerPackageName());
        this.mBottomPanel = (ViewGroup) findViewById(R.id.bottomPanel);
        if (this.mBottomPanel != null) {
            this.mMoreActions.setMoreActionsView(this.mBottomPanel, new MoreActionsItemClickListener());
        }
    }

    protected ComponentName getGuideActivity() {
        return new ComponentName(ZenModeConfig.SYSTEM_AUTHORITY, "com.android.internal.app.ResolverGuideActivity");
    }

    private boolean isForKnoxNFC() {
        if (PersonaManager.isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_0) && this.mLaunchedFromUid == 1027) {
            return true;
        }
        return false;
    }

    private boolean getEnterprisePolicyEnabled(Context context, String edmUri, String projectionArgs, String[] selectionArgs) {
        Cursor cr = context.getContentResolver().query(Uri.parse(edmUri), null, projectionArgs, selectionArgs, null);
        if (cr != null) {
            try {
                cr.moveToFirst();
                if (cr.getString(cr.getColumnIndex(projectionArgs)).equals(SmartFaceManager.TRUE)) {
                    return true;
                }
                cr.close();
                return false;
            } catch (Exception e) {
            } finally {
                cr.close();
            }
        }
        return true;
    }

    private Animator createExitAnimation(View v) {
        int height = getResources().getDisplayMetrics().heightPixels;
        AnimatorSet animator = new AnimatorSet();
        Animator aniY = ObjectAnimator.ofFloat(v, "translationY", new float[]{0.0f, (float) (height / 2)});
        Animator aniAlpha = ObjectAnimator.ofFloat(v, "alpha", new float[]{1.0f, 0.0f});
        animator.playTogether(new Animator[]{aniY, aniAlpha});
        animator.setDuration(300);
        return animator;
    }

    private void safelyStartActivtyAfterAnimation(final TargetInfo cti) {
        View v = findViewById(R.id.contentPanel);
        if (v == null) {
            safelyStartActivity(cti);
            return;
        }
        this.mExitAnimator = createExitAnimation(v);
        this.mExitAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                ResolverActivity.this.mExitAnimator = null;
                ResolverActivity.this.safelyStartActivity(cti);
                ResolverActivity.this.finish();
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        this.mExitAnimator.start();
    }

    boolean checkSquicleUXRequired() {
        return "2016A".equals(SystemProperties.get("ro.build.scafe.version"));
    }
}
