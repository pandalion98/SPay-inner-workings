package com.samsung.android.telephony;

import android.app.ActivityThread;
import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.provider.Settings$System;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.TelephonyProperties;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.smartface.SmartFaceManager;
import java.util.List;

public class MultiSimManager {
    public static final int ICC_LOCKED = 1;
    public static final int ICC_NOT_SUPPORT = -1;
    public static final int ICC_UNLOCKED = 0;
    static final String LOG_TAG = "MultiSimManager";
    private static final int PHONE_ID_TYPE_FOREGROUND_CALL = 0;
    private static final int PHONE_ID_TYPE_REJECT_CALL = 1;
    public static final int SIMSLOT1 = 0;
    public static final int SIMSLOT2 = 1;
    public static final int SIMSLOT3 = 2;
    public static final int SIMSLOT4 = 3;
    public static final int SIMSLOT5 = 4;
    private static final int SIMSLOT_COUNT = getTelephonyManager().getPhoneCount();
    private static SubscriptionManager SM = new SubscriptionManager(null);
    public static final int TYPE_DATA = 3;
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_SMS = 2;
    public static final int TYPE_VOICE = 1;
    private static final String[] mPhoneOnKey = new String[]{Settings$System.PHONE1_ON, Settings$System.PHONE2_ON, "phone3_on", "phone4_on", "phone5_on"};
    private static final String[] mSimIconKey = new String[]{Settings$System.SELECT_ICON_1, Settings$System.SELECT_ICON_2, "select_icon_3", "select_icon_4", "select_icon_5"};
    private static final String[] mSimNameKey = new String[]{Settings$System.SELECT_NAME_1, Settings$System.SELECT_NAME_2, "select_name_3", "select_name_4", "select_name_5"};

    private static SubscriptionManager getSubscriptionManager() {
        return SubscriptionManager.from(ActivityThread.currentApplication().getApplicationContext());
    }

    private static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) ActivityThread.currentApplication().getApplicationContext().getSystemService(PhoneConstants.PHONE_KEY);
    }

    public static int getSimSlotCount() {
        return SIMSLOT_COUNT;
    }

    public static int getInsertedSimCount() {
        return getSubscriptionManager().getActiveSubscriptionInfoCount();
    }

    public static int getEnabledSimCount(Context context) {
        int count = 0;
        List<SubscriptionInfo> activeSubList = getSubscriptionManager().getActiveSubscriptionInfoList();
        if (activeSubList != null) {
            for (SubscriptionInfo SubscriptionInfo : activeSubList) {
                getTelephonyManager();
                if (!SmartFaceManager.PAGE_MIDDLE.equals(TelephonyManager.getTelephonyProperty(SubscriptionInfo.getSimSlotIndex(), TelephonyProperties.PROPERTY_ICC_TYPE, SmartFaceManager.PAGE_MIDDLE)) && isEnabledSim(context, SubscriptionInfo.getSimSlotIndex())) {
                    count++;
                }
            }
        }
        Rlog.i(LOG_TAG, "return getEnabledSimCount count = " + count);
        return count;
    }

    public static boolean isNoSIM() {
        int count = 0;
        for (int simSlotNum = 0; simSlotNum < getSimSlotCount(); simSlotNum++) {
            if (SmartFaceManager.PAGE_MIDDLE.equals(getTelephonyProperty(TelephonyProperties.PROPERTY_ICC_TYPE, simSlotNum, SmartFaceManager.PAGE_MIDDLE))) {
                count++;
            }
        }
        if (count == getSimSlotCount()) {
            return true;
        }
        Rlog.i(LOG_TAG, "return isNoSIM = false");
        return false;
    }

    public static boolean isEnabledSim(Context context, int simSlot) {
        if (Settings$System.getInt(context.getContentResolver(), mPhoneOnKey[simSlot], -1) == 1) {
            return true;
        }
        Rlog.i(LOG_TAG, "return isEnabledSim = false");
        return false;
    }

    public static String getSubscriberId(int slotId) {
        try {
            return getTelephonyManager().getSubscriberId(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static String getDeviceId(int slotId) {
        return getTelephonyManager().getDeviceId(slotId);
    }

    public static String getSimSerialNumber(int slotId) {
        try {
            return getTelephonyManager().getSimSerialNumber(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static String getLine1Number(int slotId) {
        try {
            return getTelephonyManager().getLine1NumberForSubscriber(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static String getImei(int slotId) {
        return getTelephonyManager().getImei(slotId);
    }

    public static int getCurrentPhoneType(int slotId) {
        try {
            return getTelephonyManager().getCurrentPhoneType(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return 1;
        }
    }

    public static String getNetworkOperatorName(int slotId) {
        try {
            return getTelephonyManager().getNetworkOperatorName(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static String getNetworkOperator(int slotId) {
        try {
            return getTelephonyManager().getNetworkOperatorForSubscription(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static boolean isNetworkRoaming(int slotId) {
        try {
            return getTelephonyManager().isNetworkRoaming(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static String getNetworkCountryIso(int slotId) {
        try {
            return getTelephonyManager().getNetworkCountryIsoForSubscription(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static int getNetworkType(int slotId) {
        try {
            return getTelephonyManager().getNetworkType(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static int getDataNetworkType(int slotId) {
        try {
            return getTelephonyManager().getDataNetworkType(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static int getVoiceNetworkType(int slotId) {
        try {
            return getTelephonyManager().getVoiceNetworkType(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static boolean hasIccCard(int slotId) {
        return getTelephonyManager().hasIccCard(slotId);
    }

    public static int getSimState(int slotId) {
        return getTelephonyManager().getSimState(slotId);
    }

    public static String getSimOperator(int slotId) {
        try {
            return getTelephonyManager().getSimOperator(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static String getSimOperatorName(int slotId) {
        try {
            return getTelephonyManager().getSimOperatorNameForSubscription(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static String getSimCountryIso(int slotId) {
        try {
            return getTelephonyManager().getSimCountryIso(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static int getLteOnCdmaMode(int slotId) {
        try {
            return getTelephonyManager().getLteOnCdmaMode(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public static String getGroupIdLevel1(int slotId) {
        try {
            return getTelephonyManager().getGroupIdLevel1(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static String getLine1AlphaTag(int slotId) {
        try {
            return getTelephonyManager().getLine1AlphaTagForSubscriber(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static String getMsisdn(int slotId) {
        try {
            return getTelephonyManager().getMsisdn(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static String getVoiceMailNumber(int slotId) {
        try {
            return getTelephonyManager().getVoiceMailNumber(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public String getCompleteVoiceMailNumber(int slotId) {
        try {
            return getTelephonyManager().getCompleteVoiceMailNumber(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static boolean isSimFDNEnabledSubId(int subId) {
        return getTelephonyManager().isSimFDNEnabledForSubscriber(subId);
    }

    public static int getVoiceMessageCount(int slotId) {
        try {
            return getTelephonyManager().getVoiceMessageCount(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static String getVoiceMailAlphaTag(int slotId) {
        try {
            return getTelephonyManager().getVoiceMailAlphaTag(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static int getCallState(int slotId) {
        try {
            return getTelephonyManager().getCallState(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static int getDataState(int slotId) {
        try {
            return getTelephonyManager().getDataState(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static String getTelephonyProperty(String property, int slotId, String defaultVal) {
        String propVal = null;
        String prop = SystemProperties.get(property);
        if (prop != null && prop.length() > 0) {
            String[] values = prop.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
            if (slotId >= 0 && slotId < values.length && !TextUtils.isEmpty(values[slotId])) {
                propVal = values[slotId];
            }
        }
        return propVal == null ? defaultVal : propVal;
    }

    public static boolean getDataRoamingEnabled(int slotId) {
        try {
            return getTelephonyManager().getDataRoamingEnabled(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static int getServiceState(int slotId) {
        try {
            return getTelephonyManager().getServiceState(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static int getServiceStateUsingSubId(int subId) {
        return getTelephonyManager().getServiceState(subId);
    }

    public static int getDataServiceState(int slotId) {
        try {
            return getTelephonyManager().getDataServiceState(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static void dial(int slotId, String number) {
        try {
            getTelephonyManager().dial(getSubIdUsingProperty(slotId), number);
        } catch (NullPointerException e) {
        }
    }

    public static void call(int slotId, String callingPackage, String number) {
        try {
            getTelephonyManager().call(getSubIdUsingProperty(slotId), callingPackage, number);
        } catch (NullPointerException e) {
        }
    }

    public static void answerRingingCall(int slotId) {
        try {
            getTelephonyManager().answerRingingCall(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
        }
    }

    public static boolean endCall(int slotId) {
        try {
            return getTelephonyManager().endCall(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean isOffhook(int slotId) {
        try {
            return getTelephonyManager().isOffhook(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean isRinging(int slotId) {
        try {
            return getTelephonyManager().isRinging(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean isIdle(int slotId) {
        try {
            return getTelephonyManager().isIdle(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return true;
        }
    }

    public static boolean isRadioOn(int slotId) {
        try {
            return getTelephonyManager().isRadioOn(getSubIdUsingProperty(slotId));
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean supplyPin(int slotId, String pin) {
        try {
            return getTelephonyManager().supplyPin(getSubIdUsingProperty(slotId), pin);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean supplyPuk(int slotId, String puk, String pin) {
        try {
            return getTelephonyManager().supplyPuk(getSubIdUsingProperty(slotId), puk, pin);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static int[] supplyPinReportResult(int slotId, String pin) {
        try {
            return getTelephonyManager().supplyPinReportResult(getSubIdUsingProperty(slotId), pin);
        } catch (NullPointerException e) {
            return new int[0];
        }
    }

    public static int[] supplyPukReportResult(int slotId, String puk, String pin) {
        try {
            return getTelephonyManager().supplyPukReportResult(getSubIdUsingProperty(slotId), puk, pin);
        } catch (NullPointerException e) {
            return new int[0];
        }
    }

    public static boolean handlePinMmi(int slotId, String dialString) {
        try {
            return getTelephonyManager().handlePinMmiForSubscriber(getSubIdUsingProperty(slotId), dialString);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static void setDataEnabled(boolean enable) {
        getTelephonyManager().setDataEnabled(enable);
    }

    public static boolean isEmergencyNumber(int slotId, String number) {
        try {
            return PhoneNumberUtils.isEmergencyNumber(getSubIdUsingProperty(slotId), number);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static int getMultiSimPhoneId(int type) {
        switch (type) {
            case 0:
                return getTelephonyManager().getMultiSimForegroundPhoneId();
            case 1:
                return getTelephonyManager().getMultiSimLastRejectIncomingCallPhoneId();
            default:
                return 0;
        }
    }

    public static SubscriptionInfo getSubInfoForSubscriber(int subId) {
        return getSubscriptionManager().getActiveSubscriptionInfo(subId);
    }

    public static SubscriptionInfo getSubInfoUsingIccId(String iccId) {
        return getSubscriptionManager().getActiveSubscriptionInfoForIccIndex(iccId);
    }

    public static SubscriptionInfo getSubInfoUsingSlotId(int slotId) {
        return getSubscriptionManager().getActiveSubscriptionInfoForSimSlotIndex(slotId);
    }

    public static List<SubscriptionInfo> getAllSubInfoList() {
        return getSubscriptionManager().getAllSubscriptionInfoList();
    }

    public static List<SubscriptionInfo> getActiveSubInfoList() {
        return getSubscriptionManager().getActiveSubscriptionInfoList();
    }

    public static int getAllSubInfoCount() {
        return getSubscriptionManager().getAllSubscriptionInfoCount();
    }

    public static int getActiveSubInfoCount() {
        return getSubscriptionManager().getActiveSubscriptionInfoCount();
    }

    public static int getSlotId(int subId) {
        return SubscriptionManager.getSlotId(subId);
    }

    public static int[] getSubscriptionId(int slotId) {
        return SubscriptionManager.getSubId(slotId);
    }

    public static int getPhoneId(int subId) {
        return SubscriptionManager.getPhoneId(subId);
    }

    public static int getDefaultSubscriptionId(int type) {
        switch (type) {
            case 1:
                return SubscriptionManager.getDefaultVoiceSubId();
            case 2:
                return SubscriptionManager.getDefaultSmsSubId();
            case 3:
                return SubscriptionManager.getDefaultDataSubId();
            default:
                return SubscriptionManager.getDefaultSubId();
        }
    }

    public static void setDefaultSubId(int type, int subId) {
        switch (type) {
            case 1:
                getSubscriptionManager().setDefaultVoiceSubId(subId);
                return;
            case 2:
                getSubscriptionManager().setDefaultSmsSubId(subId);
                return;
            case 3:
                getSubscriptionManager().setDefaultDataSubId(subId);
                return;
            default:
                Rlog.e(LOG_TAG, "[setDefaultSubId] Invalid Type:" + type);
                return;
        }
    }

    public static SubscriptionInfo getDefaultSubInfo(int type) {
        switch (type) {
            case 1:
                return getSubscriptionManager().getDefaultVoiceSubscriptionInfo();
            case 2:
                return getSubscriptionManager().getDefaultSmsSubscriptionInfo();
            case 3:
                return getSubscriptionManager().getDefaultDataSubscriptionInfo();
            default:
                Rlog.e(LOG_TAG, "[getDefaultSubInfo] Invalid Type:" + type);
                return null;
        }
    }

    public static int getDefaultPhoneId(int type) {
        switch (type) {
            case 1:
                return SubscriptionManager.getDefaultVoicePhoneId();
            case 2:
                return getSubscriptionManager().getDefaultSmsPhoneId();
            case 3:
                return getSubscriptionManager().getDefaultDataPhoneId();
            default:
                Rlog.e(LOG_TAG, "[getDefaultPhoneId] Invalid Type:" + type);
                return 0;
        }
    }

    public static boolean allDefaultsSelected() {
        return getSubscriptionManager().allDefaultsSelected();
    }

    public static boolean isValidSubId(int subId) {
        return SubscriptionManager.isValidSubscriptionId(subId);
    }

    public static boolean isValidPhoneId(int phoneId) {
        return SubscriptionManager.isValidPhoneId(phoneId);
    }

    public static void putPhoneIdAndSubIdExtra(Intent intent, int phoneId) {
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, phoneId);
    }

    public static void putPhoneIdAndSubIdExtra(Intent intent, int phoneId, int subId) {
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, phoneId, subId);
    }

    public static int[] getActiveSubscriptionIdList() {
        return getSubscriptionManager().getActiveSubscriptionIdList();
    }

    public static int getIccLockState() {
        String prop = SystemProperties.get("gsm.facilitylock.state", SmartFaceManager.PAGE_TOP);
        if (prop.equals(Boolean.toString(true))) {
            return 1;
        }
        if (prop.equals(Boolean.toString(false))) {
            return 0;
        }
        return -1;
    }

    public static String getSimName(Context context, int simSlot) {
        String simName = Settings$System.getString(context.getContentResolver(), mSimNameKey[simSlot]);
        Rlog.e(LOG_TAG, "getSimName =" + simName + ", simSlot = " + simSlot);
        if (simName != null) {
            return simName;
        }
        switch (simSlot) {
            case 0:
                return "SIM 1";
            case 1:
                return "SIM 2";
            case 2:
                return "SIM 3";
            case 3:
                return "SIM 4";
            case 4:
                return "SIM 5";
            default:
                return "SIM 1";
        }
    }

    public static int getSimIcon(Context context, int simSlot) {
        int simIcon = Settings$System.getInt(context.getContentResolver(), mSimIconKey[simSlot], -1);
        Rlog.e(LOG_TAG, "getSimIcon =" + simIcon + ", simSlot = " + simSlot);
        if (simIcon >= 0) {
            return simIcon;
        }
        switch (simSlot) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            default:
                return 0;
        }
    }

    public static String appendSimSlot(String text, int simSlot) {
        StringBuilder str = new StringBuilder(text);
        if (simSlot < 0 || simSlot >= SIMSLOT_COUNT) {
            Rlog.e(LOG_TAG, "SimSlot value is bigger than SIMSLOT_COUNT or smaller than 0.(text : " + text + ", simSlot : " + simSlot + ")");
            try {
                throw new Exception("appendSimSlot method exception");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (simSlot != 0) {
                str.append(simSlot + 1);
            }
            return str.toString();
        }
    }

    public static int getSlotId(long subId) {
        return getSlotId((int) subId);
    }

    public static long[] getSubId(int slotId) {
        int[] subIds = SubscriptionManager.getSubId(slotId);
        int numSubIds = (subIds == null || subIds.length <= 0) ? 0 : subIds.length;
        long[] subIdArr = new long[numSubIds];
        for (int i = 0; i < numSubIds; i++) {
            subIdArr[i] = (long) subIds[i];
        }
        return subIdArr;
    }

    public static int getServiceStateUsingSubId(long subId) {
        return getServiceStateUsingSubId((int) subId);
    }

    public static SubscriptionInfo getSubInfoForSubscriber(long subId) {
        return getSubInfoForSubscriber((int) subId);
    }

    public static int getPhoneId(long subId) {
        return getPhoneId((int) subId);
    }

    public static long getDefaultSubId(int type) {
        switch (type) {
            case 1:
                return (long) SubscriptionManager.getDefaultVoiceSubId();
            case 2:
                return (long) SubscriptionManager.getDefaultSmsSubId();
            case 3:
                return (long) SubscriptionManager.getDefaultDataSubId();
            default:
                return (long) SubscriptionManager.getDefaultSubId();
        }
    }

    public static void setDefaultSubId(int type, long subId) {
        setDefaultSubId(type, (int) subId);
    }

    public static boolean isValidSubId(long subId) {
        return isValidSubId((int) subId);
    }

    public static void putPhoneIdAndSubIdExtra(Intent intent, int phoneId, long subId) {
        putPhoneIdAndSubIdExtra(intent, phoneId, (int) subId);
    }

    public static long[] getActiveSubIdList() {
        int[] subIds = getSubscriptionManager().getActiveSubscriptionIdList();
        int numSubIds = subIds.length > 0 ? subIds.length : 0;
        long[] subIdArr = new long[numSubIds];
        for (int i = 0; i < numSubIds; i++) {
            subIdArr[i] = (long) subIds[i];
        }
        return subIdArr;
    }

    public static int getDataStateUsingSubId(int subId) {
        return getTelephonyManager().getDataState(subId);
    }

    public static int getInsertedSimNum() {
        int count = 0;
        for (int simSlot = 0; simSlot < getSimSlotCount(); simSlot++) {
            if (!SmartFaceManager.PAGE_MIDDLE.equals(getTelephonyProperty(TelephonyProperties.PROPERTY_ICC_TYPE, simSlot, SmartFaceManager.PAGE_MIDDLE))) {
                count++;
            }
        }
        return count;
    }

    public static String getLine1NumberUsingSubId(int subId) {
        return getTelephonyManager().getLine1NumberForSubscriber(subId);
    }

    public static String getMeid(int slotId) {
        return getTelephonyManager().getMeid(slotId);
    }

    public static int getSubIdUsingProperty(int slotId) {
        String propVal = null;
        int subId = Integer.MAX_VALUE;
        String prop = SystemProperties.get("ril.subinfo", "");
        if (prop != null && prop.length() > 0) {
            String[] values = prop.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
            if (slotId >= 0 && slotId < values.length && !TextUtils.isEmpty(values[slotId])) {
                propVal = values[slotId];
            }
        }
        if (propVal != null && propVal.length() > 0) {
            String[] retStr = propVal.split(":");
            if (slotId >= 0 && slotId < retStr.length && !TextUtils.isEmpty(retStr[slotId])) {
                subId = Integer.parseInt(retStr[1]);
            }
        }
        Rlog.i(LOG_TAG, "return subId: " + subId);
        return subId;
    }

    public static String getTelephonyPropertyUsingSubId(String property, int subId, String defaultVal) {
        return TelephonyManager.getTelephonyProperty(getPhoneId(subId), property, defaultVal);
    }

    public static void setTelephonyProperty(String property, int slotId, String value) {
        try {
            TelephonyManager.setTelephonyProperty(slotId, property, value);
        } catch (NullPointerException e) {
        }
    }
}
