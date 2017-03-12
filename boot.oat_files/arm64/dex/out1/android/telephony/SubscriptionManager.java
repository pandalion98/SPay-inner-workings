package android.telephony;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.DisplayMetrics;
import com.android.internal.telephony.IOnSubscriptionsChangedListener;
import com.android.internal.telephony.IOnSubscriptionsChangedListener.Stub;
import com.android.internal.telephony.ISub;
import com.android.internal.telephony.ITelephonyRegistry;
import com.android.internal.telephony.PhoneConstants;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionManager {
    public static final int ACTIVE = 1;
    public static final String CARRIER_NAME = "carrier_name";
    public static final String CB_ALERT_REMINDER_INTERVAL = "alert_reminder_interval";
    public static final String CB_ALERT_SOUND_DURATION = "alert_sound_duration";
    public static final String CB_ALERT_SPEECH = "enable_alert_speech";
    public static final String CB_ALERT_VIBRATE = "enable_alert_vibrate";
    public static final String CB_AMBER_ALERT = "enable_cmas_amber_alerts";
    public static final String CB_CHANNEL_50_ALERT = "enable_channel_50_alerts";
    public static final String CB_CHANNEL_60_ALERT = "enable_channel_60_alerts";
    public static final String CB_CMAS_TEST_ALERT = "enable_cmas_test_alerts";
    public static final String CB_EMERGENCY_ALERT = "enable_emergency_alerts";
    public static final String CB_ETWS_TEST_ALERT = "enable_etws_test_alerts";
    public static final String CB_EXTREME_THREAT_ALERT = "enable_cmas_extreme_threat_alerts";
    public static final String CB_OPT_OUT_DIALOG = "show_cmas_opt_out_dialog";
    public static final String CB_SEVERE_THREAT_ALERT = "enable_cmas_severe_threat_alerts";
    public static final String COLOR = "color";
    public static final int COLOR_1 = 0;
    public static final int COLOR_2 = 1;
    public static final int COLOR_3 = 2;
    public static final int COLOR_4 = 3;
    public static final int COLOR_DEFAULT = 0;
    public static final Uri CONTENT_URI = Uri.parse("content://telephony/siminfo");
    public static final String DATA_ROAMING = "data_roaming";
    public static final int DATA_ROAMING_DEFAULT = 0;
    public static final int DATA_ROAMING_DISABLE = 0;
    public static final int DATA_ROAMING_ENABLE = 1;
    private static final boolean DBG = false;
    public static final int DEFAULT_NAME_RES = 17039374;
    public static final int DEFAULT_NW_MODE = -1;
    public static final int DEFAULT_PHONE_INDEX = 0;
    public static final int DEFAULT_SIM_SLOT_INDEX = Integer.MAX_VALUE;
    public static final int DEFAULT_SUBSCRIPTION_ID = Integer.MAX_VALUE;
    public static final String DISPLAY_NAME = "display_name";
    public static final int DISPLAY_NUMBER_DEFAULT = 1;
    public static final int DISPLAY_NUMBER_FIRST = 1;
    public static final String DISPLAY_NUMBER_FORMAT = "display_number_format";
    public static final int DISPLAY_NUMBER_LAST = 2;
    public static final int DISPLAY_NUMBER_NONE = 0;
    public static final int DUMMY_SUBSCRIPTION_ID_BASE = 2147483643;
    public static final int EXTRA_VALUE_NEW_SIM = 1;
    public static final int EXTRA_VALUE_NOCHANGE = 4;
    public static final int EXTRA_VALUE_REMOVE_SIM = 2;
    public static final int EXTRA_VALUE_REPOSITION_SIM = 3;
    public static final String ICC_ID = "icc_id";
    public static final int INACTIVE = 0;
    public static final String INTENT_KEY_DETECT_STATUS = "simDetectStatus";
    public static final String INTENT_KEY_NEW_SIM_SLOT = "newSIMSlot";
    public static final String INTENT_KEY_NEW_SIM_STATUS = "newSIMStatus";
    public static final String INTENT_KEY_SIM_COUNT = "simCount";
    public static final int INVALID_PHONE_INDEX = -1;
    public static final int INVALID_SIM_SLOT_INDEX = -1;
    public static final int INVALID_SUBSCRIPTION_ID = -1;
    public static final long INVALID_SUB_ID = -1000;
    private static final String LOG_TAG = "SubscriptionManager";
    public static final int MAX_SUBSCRIPTION_ID_VALUE = 2147483646;
    public static final String MCC = "mcc";
    public static final int MIN_SUBSCRIPTION_ID_VALUE = 0;
    public static final String MNC = "mnc";
    public static final String NAME_SOURCE = "name_source";
    public static final int NAME_SOURCE_DEFAULT_SOURCE = 0;
    public static final int NAME_SOURCE_SIM_SOURCE = 1;
    public static final int NAME_SOURCE_UNDEFINDED = -1;
    public static final int NAME_SOURCE_USER_INPUT = 2;
    public static final String NETWORK_MODE = "network_mode";
    public static final String NUMBER = "number";
    public static final int SIM_NOT_INSERTED = -1;
    public static final String SIM_SLOT_INDEX = "sim_id";
    public static final int SUB_CONFIGURATION_IN_PROGRESS = 2;
    public static final String SUB_DEFAULT_CHANGED_ACTION = "android.intent.action.SUB_DEFAULT_CHANGED";
    public static final String SUB_STATE = "sub_state";
    public static final String UNIQUE_KEY_SUBSCRIPTION_ID = "_id";
    private static final boolean VDBG = false;
    private final Context mContext;

    public static class OnSubscriptionsChangedListener {
        IOnSubscriptionsChangedListener callback = new Stub() {
            public void onSubscriptionsChanged() {
                OnSubscriptionsChangedListener.this.mHandler.sendEmptyMessage(0);
            }
        };
        private final Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                OnSubscriptionsChangedListener.this.onSubscriptionsChanged();
            }
        };

        public void onSubscriptionsChanged() {
        }

        private void log(String s) {
            Rlog.d(SubscriptionManager.LOG_TAG, s);
        }
    }

    public SubscriptionManager(Context context) {
        this.mContext = context;
    }

    public static SubscriptionManager from(Context context) {
        return (SubscriptionManager) context.getSystemService("telephony_subscription_service");
    }

    public void addOnSubscriptionsChangedListener(OnSubscriptionsChangedListener listener) {
        String pkgForDebug = this.mContext != null ? this.mContext.getOpPackageName() : "<unknown>";
        try {
            ITelephonyRegistry tr = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
            if (tr != null) {
                tr.addOnSubscriptionsChangedListener(pkgForDebug, listener.callback);
            }
        } catch (RemoteException e) {
        }
    }

    public void removeOnSubscriptionsChangedListener(OnSubscriptionsChangedListener listener) {
        String pkgForDebug = this.mContext != null ? this.mContext.getOpPackageName() : "<unknown>";
        try {
            ITelephonyRegistry tr = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
            if (tr != null) {
                tr.removeOnSubscriptionsChangedListener(pkgForDebug, listener.callback);
            }
        } catch (RemoteException e) {
        }
    }

    public SubscriptionInfo getActiveSubscriptionInfo(int subId) {
        if (!isValidSubscriptionId(subId)) {
            return null;
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getActiveSubscriptionInfo(subId, this.mContext.getOpPackageName());
            }
            return null;
        } catch (RemoteException e) {
            return null;
        }
    }

    public SubscriptionInfo getActiveSubscriptionInfoForIccIndex(String iccId) {
        if (iccId == null) {
            logd("[getActiveSubscriptionInfoForIccIndex]- null iccid");
            return null;
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getActiveSubscriptionInfoForIccId(iccId, this.mContext.getOpPackageName());
            }
            return null;
        } catch (RemoteException e) {
            return null;
        }
    }

    public SubscriptionInfo getActiveSubscriptionInfoForSimSlotIndex(int slotIdx) {
        if (isValidSlotId(slotIdx)) {
            try {
                ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
                if (iSub != null) {
                    return iSub.getActiveSubscriptionInfoForSimSlotIndex(slotIdx, this.mContext.getOpPackageName());
                }
                return null;
            } catch (RemoteException e) {
                return null;
            }
        }
        logd("[getActiveSubscriptionInfoForSimSlotIndex]- invalid slotIdx");
        return null;
    }

    public List<SubscriptionInfo> getAllSubscriptionInfoList() {
        List<SubscriptionInfo> result = null;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getAllSubInfoList(this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        }
        if (result == null) {
            return new ArrayList();
        }
        return result;
    }

    public List<SubscriptionInfo> getActiveSubscriptionInfoList() {
        List<SubscriptionInfo> result = null;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getActiveSubscriptionInfoList(this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        }
        return result;
    }

    public int getAllSubscriptionInfoCount() {
        int result = 0;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getAllSubInfoCount(this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        }
        return result;
    }

    public int getActiveSubscriptionInfoCount() {
        int result = 0;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getActiveSubInfoCount(this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        }
        return result;
    }

    public int getActiveSubscriptionInfoCountMax() {
        int result = 0;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getActiveSubInfoCountMax();
            }
        } catch (RemoteException e) {
        }
        return result;
    }

    public Uri addSubscriptionInfoRecord(String iccId, int slotId) {
        if (iccId == null) {
            logd("[addSubscriptionInfoRecord]- null iccId");
        }
        if (!isValidSlotId(slotId)) {
            logd("[addSubscriptionInfoRecord]- invalid slotId");
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.addSubInfoRecord(iccId, slotId);
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    public int setIconTint(int tint, int subId) {
        if (isValidSubscriptionId(subId)) {
            try {
                ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
                if (iSub != null) {
                    return iSub.setIconTint(tint, subId);
                }
                return 0;
            } catch (RemoteException e) {
                return 0;
            }
        }
        logd("[setIconTint]- fail");
        return -1;
    }

    public int setDisplayName(String displayName, int subId) {
        return setDisplayName(displayName, subId, -1);
    }

    public int setDisplayName(String displayName, int subId, long nameSource) {
        if (isValidSubscriptionId(subId)) {
            try {
                ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
                if (iSub != null) {
                    return iSub.setDisplayNameUsingSrc(displayName, subId, nameSource);
                }
                return 0;
            } catch (RemoteException e) {
                return 0;
            }
        }
        logd("[setDisplayName]- fail");
        return -1;
    }

    public int setDisplayNumber(String number, int subId) {
        if (number == null || !isValidSubscriptionId(subId)) {
            logd("[setDisplayNumber]- fail");
            return -1;
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.setDisplayNumber(number, subId);
            }
            return 0;
        } catch (RemoteException e) {
            return 0;
        }
    }

    public int setDataRoaming(int roaming, int subId) {
        if (roaming < 0 || !isValidSubscriptionId(subId)) {
            logd("[setDataRoaming]- fail");
            return -1;
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.setDataRoaming(roaming, subId);
            }
            return 0;
        } catch (RemoteException e) {
            return 0;
        }
    }

    public static int getSlotId(int subId) {
        int result = !isValidSubscriptionId(subId) ? -1 : -1;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getSlotId(subId);
            }
        } catch (RemoteException e) {
        }
        return result;
    }

    public static int[] getSubId(int slotId) {
        if (isValidSlotId(slotId)) {
            try {
                ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
                if (iSub != null) {
                    return iSub.getSubId(slotId);
                }
                return null;
            } catch (RemoteException e) {
                return null;
            }
        }
        logd("[getSubId]- fail, slotId:" + slotId + ", getSimCount():" + TelephonyManager.getDefault().getSimCount());
        return null;
    }

    public static int getPhoneId(int subId) {
        if (!isValidSubscriptionId(subId)) {
            return -1;
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getPhoneId(subId);
            }
            return -1;
        } catch (RemoteException e) {
            return -1;
        }
    }

    private static void logd(String msg) {
        Rlog.d(LOG_TAG, msg);
    }

    public static int getDefaultSubId() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getDefaultSubId();
            }
            logd("getDefaultSubId(): Fail! iSub is null");
            return -1;
        } catch (RemoteException ex) {
            logd("getDefaultSubId(): Exception " + ex);
            return -1;
        }
    }

    public static int getDefaultVoiceSubId() {
        int subId = -1;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                subId = iSub.getDefaultVoiceSubId();
            }
        } catch (RemoteException e) {
        }
        return subId;
    }

    public void setDefaultVoiceSubId(int subId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setDefaultVoiceSubId(subId);
            }
        } catch (RemoteException e) {
        }
    }

    public SubscriptionInfo getDefaultVoiceSubscriptionInfo() {
        return getActiveSubscriptionInfo(getDefaultVoiceSubId());
    }

    public static int getDefaultVoicePhoneId() {
        return getPhoneId(getDefaultVoiceSubId());
    }

    public static boolean isSMSPromptEnabled() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.isSMSPromptEnabled();
            }
        } catch (RemoteException e) {
        }
        return false;
    }

    public static void setSMSPromptEnabled(boolean enabled) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setSMSPromptEnabled(enabled);
            }
        } catch (RemoteException e) {
        }
    }

    public static int getDefaultSmsSubId() {
        int subId = -1;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                subId = iSub.getDefaultSmsSubId();
            }
        } catch (RemoteException e) {
        }
        return subId;
    }

    public void setDefaultSmsSubId(int subId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setDefaultSmsSubId(subId);
            }
        } catch (RemoteException e) {
        }
    }

    public SubscriptionInfo getDefaultSmsSubscriptionInfo() {
        return getActiveSubscriptionInfo(getDefaultSmsSubId());
    }

    public int getDefaultSmsPhoneId() {
        return getPhoneId(getDefaultSmsSubId());
    }

    public static int getDefaultDataSubId() {
        int subId = -1;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                subId = iSub.getDefaultDataSubId();
            }
        } catch (RemoteException e) {
        }
        return subId;
    }

    public void setDefaultDataSubId(int subId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setDefaultDataSubId(subId);
            }
        } catch (RemoteException e) {
        }
    }

    public static void setDefaultDataSubIdForMMS(int subId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setDefaultDataSubIdForMMS(subId);
            }
        } catch (RemoteException e) {
        }
    }

    public SubscriptionInfo getDefaultDataSubscriptionInfo() {
        return getActiveSubscriptionInfo(getDefaultDataSubId());
    }

    public int getDefaultDataPhoneId() {
        return getPhoneId(getDefaultDataSubId());
    }

    public void clearSubscriptionInfo() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.clearSubInfo();
            }
        } catch (RemoteException e) {
        }
    }

    public boolean allDefaultsSelected() {
        if (isValidSubscriptionId(getDefaultDataSubId()) && isValidSubscriptionId(getDefaultSmsSubId()) && isValidSubscriptionId(getDefaultVoiceSubId())) {
            return true;
        }
        return false;
    }

    public void clearDefaultsForInactiveSubIds() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.clearDefaultsForInactiveSubIds();
            }
        } catch (RemoteException e) {
        }
    }

    public static boolean isValidSubscriptionId(int subId) {
        return subId > -1;
    }

    public static boolean isUsableSubIdValue(int subId) {
        return subId >= 0 && subId <= MAX_SUBSCRIPTION_ID_VALUE;
    }

    public static boolean isValidSlotId(int slotId) {
        return slotId >= 0 && slotId < TelephonyManager.getDefault().getSimCount();
    }

    public static boolean isValidPhoneId(int phoneId) {
        return phoneId >= 0 && phoneId < TelephonyManager.getDefault().getPhoneCount();
    }

    public static void putPhoneIdAndSubIdExtra(Intent intent, int phoneId) {
        int[] subIds = getSubId(phoneId);
        if (subIds == null || subIds.length <= 0) {
            logd("putPhoneIdAndSubIdExtra: no valid subs");
        } else {
            putPhoneIdAndSubIdExtra(intent, phoneId, subIds[0]);
        }
    }

    public static void putPhoneIdAndSubIdExtra(Intent intent, int phoneId, int subId) {
        intent.putExtra(PhoneConstants.SUBSCRIPTION_KEY, subId);
        intent.putExtra(PhoneConstants.PHONE_KEY, phoneId);
        intent.putExtra(PhoneConstants.SLOT_KEY, phoneId);
    }

    public int[] getActiveSubscriptionIdList() {
        int[] subId = null;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                subId = iSub.getActiveSubIdList();
            }
        } catch (RemoteException e) {
        }
        if (subId == null) {
            return new int[0];
        }
        return subId;
    }

    public boolean isNetworkRoaming(int subId) {
        if (getPhoneId(subId) < 0) {
            return false;
        }
        return TelephonyManager.getDefault().isNetworkRoaming(subId);
    }

    public static int getSimStateForSlotIdx(int slotIdx) {
        int simState = 0;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                simState = iSub.getSimStateForSlotIdx(slotIdx);
            }
        } catch (RemoteException e) {
        }
        logd("getSimStateForSlotIdx: simState=" + simState + " slotIdx=" + slotIdx);
        return simState;
    }

    public static void setSubscriptionProperty(int subId, String propKey, String propValue) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setSubscriptionProperty(subId, propKey, propValue);
            }
        } catch (RemoteException e) {
        }
    }

    private static String getSubscriptionProperty(int subId, String propKey, Context context) {
        String resultValue = null;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                resultValue = iSub.getSubscriptionProperty(subId, propKey, context.getOpPackageName());
            }
        } catch (RemoteException e) {
        }
        return resultValue;
    }

    public static boolean getBooleanSubscriptionProperty(int subId, String propKey, boolean defValue, Context context) {
        String result = getSubscriptionProperty(subId, propKey, context);
        if (result != null) {
            try {
                if (Integer.parseInt(result) == 1) {
                    return true;
                }
                return false;
            } catch (NumberFormatException e) {
                logd("getBooleanSubscriptionProperty NumberFormat exception");
            }
        }
        return defValue;
    }

    public static int getIntegerSubscriptionProperty(int subId, String propKey, int defValue, Context context) {
        String result = getSubscriptionProperty(subId, propKey, context);
        if (result != null) {
            try {
                defValue = Integer.parseInt(result);
            } catch (NumberFormatException e) {
                logd("getBooleanSubscriptionProperty NumberFormat exception");
            }
        }
        return defValue;
    }

    public static Resources getResourcesForSubId(Context context, int subId) {
        SubscriptionInfo subInfo = from(context).getActiveSubscriptionInfo(subId);
        Configuration config = context.getResources().getConfiguration();
        Configuration newConfig = new Configuration();
        newConfig.setTo(config);
        if (subInfo != null) {
            newConfig.mcc = subInfo.getMcc();
            newConfig.mnc = subInfo.getMnc();
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        DisplayMetrics newMetrics = new DisplayMetrics();
        newMetrics.setTo(metrics);
        return new Resources(context.getResources().getAssets(), newMetrics, newConfig);
    }

    public boolean isActiveSubId(int subId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.isActiveSubId(subId);
            }
        } catch (RemoteException e) {
        }
        return false;
    }

    public static void activateSubId(int subId) {
        logd("activateSubId sub id = " + subId);
        try {
            getISubInfo().activateSubId(subId);
        } catch (RemoteException e) {
        }
    }

    public static void deactivateSubId(int subId) {
        logd("deactivateSubId sub id = " + subId);
        try {
            getISubInfo().deactivateSubId(subId);
        } catch (RemoteException e) {
        }
    }

    public static int getSubState(int subId) {
        logd("getSubState sub id = " + subId);
        try {
            return getISubInfo().getSubState(subId);
        } catch (RemoteException e) {
            return 0;
        }
    }

    public static int setSubState(int subId, int subState) {
        logd("setSubState sub id = " + subId + " state = " + subState);
        try {
            return getISubInfo().setSubState(subId, subState);
        } catch (RemoteException e) {
            return 0;
        }
    }

    public static int getSubState(long subId) {
        return getSubState((int) subId);
    }

    public static int setSubState(long subId, int subState) {
        return setSubState((int) subId, subState);
    }

    private static ISub getISubInfo() {
        return ISub.Stub.asInterface(ServiceManager.getService("isub"));
    }

    public static boolean isVoicePromptEnabled() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.isVoicePromptEnabled();
            }
        } catch (RemoteException e) {
        }
        return false;
    }

    public static void setVoicePromptEnabled(boolean enabled) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setVoicePromptEnabled(enabled);
            }
        } catch (RemoteException e) {
        }
    }
}
