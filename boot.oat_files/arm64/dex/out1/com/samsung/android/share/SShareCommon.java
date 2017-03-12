package com.samsung.android.share;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings$System;
import android.util.GeneralUtil;
import android.util.Log;
import com.samsung.android.coreapps.sdk.easysignup.EasySignUpManager;
import com.samsung.android.feature.FloatingFeature;
import com.samsung.android.multiwindow.MultiWindowStyle;
import com.sec.android.app.CscFeature;
import com.sec.android.emergencymode.EmergencyManager;
import java.util.ArrayList;
import java.util.List;

public class SShareCommon {
    private static final boolean DEBUG = false;
    private static final int DISABLE = 0;
    private static final int ENABLE = 1;
    private static final boolean ENABLE_SURVEY_MODE = FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_CONTEXTSERVICE_ENABLE_SURVEY_MODE");
    private static final String EXTRA_SAFE_FORWARD = "extra_safe_forward";
    private static final int LIMITED_ENABLE = 2;
    private static final String MORE_ACTIONS_CHANGE_PLAYER = "more_actions_change_player";
    private static final String MORE_ACTIONS_PRINT = "more_actions_print";
    private static final String MORE_ACTIONS_QUICK_CONNECT = "more_actions_quick_connect";
    private static final String MORE_ACTIONS_SCREEN_MIRRORING = "more_actions_screen_mirroring";
    private static final String MORE_ACTIONS_SCREEN_SHARING = "more_actions_screen_sharing";
    private static final String RESOLVER_GUIDE_ACTIVITY_CLASS = "com.android.internal.app.ResolverGuideActivity";
    private static final String RESOLVER_GUIDE_ACTIVITY_PKG = "android";
    private static final String SIMPLE_SHARING_FORCE_DISABLE = "simple_sharing_force_disable";
    private static final String TAG = "SShareCommon";
    private static boolean mIsSupportButtons = false;
    private static boolean mIsSupportGridResolver = false;
    private static boolean mIsSupportLogging = false;
    private static boolean mIsSupportMoreActions = false;
    private static boolean mIsSupportNearby = false;
    private static boolean mIsSupportPageMode = false;
    private static boolean mIsSupportRecentSort = false;
    private static boolean mIsSupportResolverGuide = false;
    private static boolean mIsSupportShowButtonShapes = false;
    private static boolean mIsSupportSimpleSharing = false;
    private final int REMOTE_SHARE_SERVICE_ID;
    private Context mContext;
    private boolean mDeviceDefault;
    private boolean mEasySignUpAlreadyChecked;
    private boolean mEasySignUpEnabled;
    private List<Intent> mExtraIntentList;
    private int mIconChangePlayer;
    private int mIconPrint;
    private int mIconQuickConnect;
    private int mIconScreenMirroring;
    private int mIconScreenSharing;
    private int mItemCount;
    private int mLaunchedFromUid;
    private Intent mResolverGuideIntent;

    public SShareCommon(Context context, Intent intent, boolean isDeviceDefault, int launchedFromUid, List<Intent> extraIntentList, int itemCount) {
        this(context, intent, isDeviceDefault, false, false, launchedFromUid, extraIntentList, itemCount);
    }

    public SShareCommon(Context context, Intent intent, boolean isDeviceDefault, boolean alwaysUseOption, boolean hasFilteredItem, int launchedFromUid, List<Intent> extraIntentList, int itemCount) {
        this.mItemCount = 0;
        this.mEasySignUpAlreadyChecked = false;
        this.mEasySignUpEnabled = false;
        this.mIconChangePlayer = 0;
        this.mIconScreenMirroring = 0;
        this.mIconScreenSharing = 0;
        this.mIconQuickConnect = 0;
        this.mIconPrint = 0;
        this.REMOTE_SHARE_SERVICE_ID = 2;
        this.mContext = context;
        this.mItemCount = itemCount;
        this.mLaunchedFromUid = launchedFromUid;
        this.mExtraIntentList = extraIntentList;
        this.mDeviceDefault = isDeviceDefault;
        if (!(!isDeviceDefault || alwaysUseOption || hasFilteredItem || launchedFromUid < 0 || UserHandle.isIsolated(launchedFromUid))) {
            setSimpleSharingFeature(intent);
            setMoreActionsFeature(intent);
            setRecentSortFeature();
            setLoggingFeature();
        }
        if (alwaysUseOption || hasFilteredItem) {
            setGridResolverFeature();
            setButtonsFeature();
            setResolverGuideFeature();
        }
        setPageModeFeature();
    }

    private void setGridResolverFeature() {
        mIsSupportGridResolver = true;
    }

    private void setButtonsFeature() {
        mIsSupportButtons = getButtonsSupportState();
        if (mIsSupportButtons) {
            mIsSupportShowButtonShapes = getButtonShapeSupportState();
        }
    }

    private void setResolverGuideFeature() {
        mIsSupportResolverGuide = getResolverGuideSupportState();
    }

    private void setSimpleSharingFeature(Intent intent) {
        mIsSupportSimpleSharing = getSimpleSharingSupportState(intent);
    }

    private void setLoggingFeature() {
        mIsSupportLogging = ENABLE_SURVEY_MODE;
    }

    private void setQuickConnectFeature() {
        mIsSupportNearby = getQuickConnectSupportState();
    }

    private void setMoreActionsFeature(Intent intent) {
        mIsSupportMoreActions = getMoreActionsSupportState(intent);
    }

    private void setPageModeFeature() {
        mIsSupportPageMode = getPageModeSupportState();
    }

    private void setRecentSortFeature() {
        mIsSupportRecentSort = mIsSupportPageMode;
    }

    public void setResolverGuideIntent(Activity activity, Intent intent, boolean safeForward) {
        if (mIsSupportResolverGuide) {
            try {
                Intent newIntent = new Intent(intent);
                newIntent.setComponent(new ComponentName("android", RESOLVER_GUIDE_ACTIVITY_CLASS));
                newIntent.putExtra("android.intent.extra.INTENT", intent);
                newIntent.putExtra(EXTRA_SAFE_FORWARD, safeForward);
                newIntent.addFlags(8388608);
                MultiWindowStyle mws = activity.getMultiWindowStyle();
                if (mws == null) {
                    mws = new MultiWindowStyle();
                }
                newIntent.setMultiWindowStyle(mws);
                MultiWindowStyle.skipMultiWindowLaunch(newIntent);
                this.mResolverGuideIntent = newIntent;
            } catch (ActivityNotFoundException e) {
                Log.w(TAG, "Activity Not Found");
            } catch (Exception e2) {
                Log.w(TAG, "Class Not Found");
            }
        }
    }

    public Intent getResolverGuideIntent() {
        return this.mResolverGuideIntent;
    }

    public boolean isDeviceDefaultTheme() {
        return this.mDeviceDefault;
    }

    public boolean getSupportGridResolver() {
        return mIsSupportGridResolver;
    }

    public boolean getSupportButtons() {
        return mIsSupportButtons;
    }

    public boolean getSupportShowButtonShapes() {
        return mIsSupportShowButtonShapes;
    }

    public boolean getSupportResolverGuide() {
        return mIsSupportResolverGuide;
    }

    public boolean getSupportSimpleSharing() {
        return mIsSupportSimpleSharing;
    }

    public boolean getSupportLogging() {
        return mIsSupportLogging;
    }

    public boolean getSupportNearby() {
        return mIsSupportNearby;
    }

    public boolean getSupportMoreActions() {
        return mIsSupportMoreActions;
    }

    public boolean getSupportPageMode() {
        return mIsSupportPageMode;
    }

    public boolean getSupportRecentSort() {
        return mIsSupportRecentSort;
    }

    public int getChangePlayerEnable() {
        return this.mIconChangePlayer;
    }

    public int getScreenMirroringEnable() {
        return this.mIconScreenMirroring;
    }

    public int getScreenSharingEnable() {
        return this.mIconScreenSharing;
    }

    public int getQuickConnectEnable() {
        return this.mIconQuickConnect;
    }

    public int getPrintEnable() {
        return this.mIconPrint;
    }

    private boolean isUSA() {
        return CscFeature.getInstance().getEnableStatus("CscFeature_Framework_SupportResolverActivityGuide");
    }

    private boolean isKnoxModeEnabled() {
        return UserHandle.getUserId(this.mLaunchedFromUid) >= 100;
    }

    private boolean isForceSimpleSharingDisable(Intent intent) {
        if (intent.getIntExtra(SIMPLE_SHARING_FORCE_DISABLE, 0) == 1) {
            return true;
        }
        return false;
    }

    private boolean isEmergencyOrUPSModeEnabled() {
        if (this.mContext == null) {
            return false;
        }
        EmergencyManager em = EmergencyManager.getInstance(this.mContext);
        boolean isEmergencyMode = false;
        boolean isUltraPowerSavingMode = false;
        if (em != null) {
            isEmergencyMode = em.isEmergencyMode() && !em.checkModeType(512);
            if (em.isEmergencyMode() && em.checkModeType(512)) {
                isUltraPowerSavingMode = true;
            } else {
                isUltraPowerSavingMode = false;
            }
            boolean isEmergencyOrUPSM = em.isEmergencyMode();
        }
        if (isEmergencyMode || isUltraPowerSavingMode) {
            return true;
        }
        return false;
    }

    private boolean isIntentTypeSupportRemoteShare(Intent intent) {
        String action = intent.getAction();
        if (("android.intent.action.SEND".equals(action) || "android.intent.action.SEND_MULTIPLE".equals(action)) && isIntentUriDataIValidCheck(intent)) {
            return true;
        }
        return false;
    }

    private boolean hasExtraIntentUriInfo() {
        if (this.mExtraIntentList != null) {
            for (int i = 0; i < this.mExtraIntentList.size(); i++) {
                Bundle extraBundle = ((Intent) this.mExtraIntentList.get(i)).getExtras();
                if (extraBundle != null && ((Uri) extraBundle.getParcelable("android.intent.extra.STREAM")) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isIntentUriDataIValidCheck(Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.SEND".equals(action)) {
            Uri uri = null;
            Bundle extraBundle = intent.getExtras();
            if (extraBundle != null) {
                uri = (Uri) extraBundle.getParcelable("android.intent.extra.STREAM");
            }
            if (uri != null) {
                return "com.android.contacts".equals(uri.getEncodedAuthority()) ? true : true;
            } else {
                if (hasExtraIntentUriInfo()) {
                    return true;
                }
                return false;
            }
        } else if (!"android.intent.action.SEND_MULTIPLE".equals(action)) {
            return true;
        } else {
            ArrayList<Uri> uriArray = new ArrayList();
            uriArray = intent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            if (uriArray == null) {
                return false;
            }
            int numOfUri = uriArray.size();
            int i = 0;
            while (i < numOfUri) {
                if (uriArray.get(i) == null) {
                    i++;
                } else if ("com.android.contacts".equals(((Uri) uriArray.get(i)).getEncodedAuthority())) {
                    return true;
                } else {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean getResolverGuideSupportState() {
        if (mIsSupportButtons || !isUSA()) {
            return false;
        }
        return true;
    }

    private boolean getPageModeSupportState() {
        boolean bPhoneType = GeneralUtil.isPhone();
        boolean bUpgrade = "2014A".equals(SystemProperties.get("ro.build.scafe.version"));
        boolean bSupportSimpleShare = checkSimpleShareSupport();
        if (!bPhoneType || mIsSupportButtons) {
            return false;
        }
        if (!bUpgrade) {
            return true;
        }
        if (bSupportSimpleShare) {
            return true;
        }
        return false;
    }

    private boolean getButtonsSupportState() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        if (contentResolver == null || Settings$System.getInt(contentResolver, "default_app_selection_option", 0) != 1) {
            return false;
        }
        return true;
    }

    private boolean getButtonShapeSupportState() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        if (contentResolver == null || Settings$System.getInt(contentResolver, Settings$System.SHOW_BUTTON_BACKGROUND, 0) != 1) {
            return false;
        }
        return true;
    }

    private boolean getQuickConnectSupportState() {
        try {
            this.mContext.getPackageManager().getApplicationInfo("com.samsung.android.sconnect", 128);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean getSimpleSharingSupportState(Intent intent) {
        boolean featureEnable = checkSimpleShareSupport();
        boolean intentSupport = isIntentTypeSupportRemoteShare(intent);
        boolean knoxMode = isKnoxModeEnabled();
        boolean emergencyMode = isEmergencyOrUPSModeEnabled();
        boolean forceDisable = isForceSimpleSharingDisable(intent);
        if (featureEnable && intentSupport && !knoxMode && !emergencyMode && !forceDisable) {
            return true;
        }
        Log.d(TAG, "featureEnable = " + featureEnable + " intentSupport = " + intentSupport + " knoxMode = " + knoxMode + " emergencyMode = " + emergencyMode + " forceDisable = " + forceDisable);
        return false;
    }

    private boolean checkSimpleShareSupport() {
        if (this.mEasySignUpAlreadyChecked) {
            return this.mEasySignUpEnabled;
        }
        try {
            int retVal = EasySignUpManager.getSupportedFeatures(this.mContext, 2);
            Log.d(TAG, "checkSimpleShareSupport = " + retVal);
            if (retVal == -1) {
                this.mEasySignUpEnabled = false;
            } else {
                this.mEasySignUpEnabled = true;
            }
            this.mEasySignUpAlreadyChecked = true;
        } catch (Exception e) {
            this.mEasySignUpEnabled = false;
            Log.e(TAG, "EasySignUpManager returns exception !!!", e);
        }
        return this.mEasySignUpEnabled;
    }

    private boolean getMoreActionsSupportState(Intent intent) {
        if (isEmergencyOrUPSModeEnabled()) {
            return false;
        }
        try {
            this.mIconScreenSharing = intent.getIntExtra(MORE_ACTIONS_SCREEN_SHARING, 0);
            this.mIconScreenMirroring = intent.getIntExtra(MORE_ACTIONS_SCREEN_MIRRORING, 0);
            this.mIconChangePlayer = intent.getIntExtra(MORE_ACTIONS_CHANGE_PLAYER, 0);
            this.mIconQuickConnect = intent.getIntExtra(MORE_ACTIONS_QUICK_CONNECT, 0);
            this.mIconPrint = UserHandle.myUserId() >= 100 ? 0 : intent.getIntExtra(MORE_ACTIONS_PRINT, 0);
        } catch (Exception e) {
            Log.e(TAG, "Exception !!! during getting more actions", e);
        } catch (OutOfMemoryError e2) {
            Log.d(TAG, "OutOfMemoryError !!! during getting more actions");
        }
        if (this.mIconChangePlayer == 1) {
            intent.putExtra(MORE_ACTIONS_CHANGE_PLAYER, 0);
        }
        if (this.mIconScreenMirroring == 1) {
            intent.putExtra(MORE_ACTIONS_SCREEN_MIRRORING, 0);
        }
        if (this.mIconScreenSharing == 1 || this.mIconScreenSharing == 2) {
            intent.putExtra(MORE_ACTIONS_SCREEN_SHARING, 0);
        }
        if (this.mIconQuickConnect == 1) {
            intent.putExtra(MORE_ACTIONS_QUICK_CONNECT, 0);
        }
        if (this.mIconPrint == 1) {
            intent.putExtra(MORE_ACTIONS_PRINT, 0);
        }
        if (this.mIconChangePlayer == 1 || this.mIconScreenMirroring == 1 || this.mIconScreenSharing == 1 || this.mIconScreenSharing == 2 || this.mIconQuickConnect == 1 || this.mIconPrint == 1) {
            return true;
        }
        return false;
    }
}
