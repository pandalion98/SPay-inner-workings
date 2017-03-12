package android.telephony;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.Settings$System;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;
import android.security.keystore.KeyProperties;
import android.telecom.PhoneAccount;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.GeneralUtil;
import android.util.Log;
import com.android.internal.R;
import com.android.internal.content.NativeLibraryHelper;
import com.android.internal.os.PowerProfile;
import com.android.internal.telecom.ITelecomService;
import com.android.internal.telephony.CellNetworkScanResult;
import com.android.internal.telephony.IPhoneSubInfo;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephonyRegistry;
import com.android.internal.telephony.ITelephonyRegistry.Stub;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneConstants.State;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.TelephonyProperties;
import com.broadcom.fm.fmreceiver.FmProxy;
import com.kddi.android.internal.pdg.PdgUimAccessChecker;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.smartface.SmartFaceManager;
import com.sec.android.app.CscFeature;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class TelephonyManager {
    public static final String ACTION_CONFIGURE_VOICEMAIL = "android.telephony.action.CONFIGURE_VOICEMAIL";
    public static final String ACTION_EMERGENCY_ASSISTANCE = "android.telephony.action.EMERGENCY_ASSISTANCE";
    public static final String ACTION_PHONE_STATE_CHANGED = "android.intent.action.PHONE_STATE";
    public static final String ACTION_PRECISE_CALL_STATE_CHANGED = "android.intent.action.PRECISE_CALL_STATE";
    public static final String ACTION_PRECISE_DATA_CONNECTION_STATE_CHANGED = "android.intent.action.PRECISE_DATA_CONNECTION_STATE_CHANGED";
    public static final String ACTION_RESPOND_VIA_MESSAGE = "android.intent.action.RESPOND_VIA_MESSAGE";
    public static final int CALL_STATE_IDLE = 0;
    public static final int CALL_STATE_OFFHOOK = 2;
    public static final int CALL_STATE_RINGING = 1;
    public static final int CAPABLE_TYPE_DOWNLOADBOOSTER = 1;
    public static final int CARRIER_PRIVILEGE_STATUS_ERROR_LOADING_RULES = -2;
    public static final int CARRIER_PRIVILEGE_STATUS_HAS_ACCESS = 1;
    public static final int CARRIER_PRIVILEGE_STATUS_NO_ACCESS = 0;
    public static final int CARRIER_PRIVILEGE_STATUS_RULES_NOT_LOADED = -1;
    public static final int DATA_ACTIVITY_DORMANT = 4;
    public static final int DATA_ACTIVITY_IN = 1;
    public static final int DATA_ACTIVITY_INOUT = 3;
    public static final int DATA_ACTIVITY_NONE = 0;
    public static final int DATA_ACTIVITY_OUT = 2;
    public static final int DATA_CONNECTED = 2;
    public static final int DATA_CONNECTING = 1;
    public static final int DATA_DISCONNECTED = 0;
    public static final int DATA_SUSPENDED = 3;
    public static final int DATA_UNKNOWN = -1;
    private static final boolean DBG = (SystemProperties.getInt("ro.debuggable", 0) == 1);
    public static final int DM_CMD_DELETE_LOG = 8;
    public static final int DM_CMD_EVENT_SET = 2;
    public static final int DM_CMD_HDV_ALARM_EVENT = 7;
    public static final int DM_CMD_LOG_SET = 1;
    public static final int DM_CMD_MEM_CHECK = 5;
    public static final int DM_CMD_MEM_SET = 4;
    public static final int DM_CMD_SAVE_LOG = 6;
    public static final int DM_CMD_STATE_CHANGE_SET = 3;
    private static final String DOD_SKT_APP_SIGNATURE = "3082019b30820104a00302010202044c6b473a300d06092a864886f70d010105050030123110300e06035504031307616e64726f6964301e170d3130303831383032333634325a170d3430303831303032333634325a30123110300e06035504031307616e64726f696430819f300d06092a864886f70d010101050003818d00308189028181008e22b5c794e4621f5acf64431605f6f03301e8af027353d1952f3cd6acb5ce50a02bbc85822bf21ee5ee84410ed5c847233de58790d3309799e6e3e91eff8cb8db56ae7b64f691e3a522f78ec869b093720236152410bce1242bbe567fa9c2e1e4efdeb8feabe027d264501fe0ea65777b49b0bed6b806bd888c195394fd2a230203010001300d06092a864886f70d010105050003818100760b171ab6383e2b4170136ebb253e8226d2af2d31c3196c4914c92cea6e91072827b581a639a427fd4302842c5e2be9418d5226745d6ed6cef06904505c7a6ef51897368251a46fc9aae61fc4778ccb85432c801d64cd818f436e686753cccd4aa76e3bcfe3355a73c4bc1e5b239e453fc739b52959cd7de0e617e4072017b0";
    private static final String DOD_SKT_APP_SIGNATURE2 = "3082033b30820223a00302010202046949927c300d06092a864886f70d01010b0500304d310b3009060355040613024b52310e300c0603550407130553656f756c31123010060355040a1309736b74656c65636f6d311a3018060355040b1311536d61727420446576696365204c61622e3020170d3133303731373034303731315a180f32313133303632333034303731315a304d310b3009060355040613024b52310e300c0603550407130553656f756c31123010060355040a1309736b74656c65636f6d311a3018060355040b1311536d61727420446576696365204c61622e30820122300d06092a864886f70d01010105000382010f003082010a02820101009054ef68216a1db045aa95d5b71120701ae32b55f692ba4a033e4fd8531d7614ce0a8dc058cf2d11857f68138a3579f5f81eb7fcf6abc721e215868fd2866fae01f69967340267497410520a2cfffca58585cad43dfc5ece54de5c253a2d1e7391a09abeaca1cd2e2db7fb4d045f43ab1c4df0621b1e914322d2f1743d465021d540715b1ea7dafb9d3f25b77812f5998ad3a6befa48bb3ef46acfc50b8f093e2c42611ce1084221a28c6a26c96cb2a78c779e2edc41f859b8638b5c060ec608d92564e1ee355b6cf400888c7bd3fc6b3bd38bf4512d23153cb90a9c1b3c7c221cd15ffbd84abea143f4665bdab5fb969d1332e29499b487810c3324cf1927b10203010001a321301f301d0603551d0e041604149f6ccd79ff1fc86191fd86973cbe8ad3498752a6300d06092a864886f70d01010b05000382010100465ded5885849010ba16c05af54a55a4783db87fe46d3a2411866ca9819f7e734132c513ba370bec8bc657f5b507e8a6632e3cdcdd750b04c059f3e8ef999cd35c8ebc467351e7f16093672c267f4688640f0700d8bbf6f3340aeb447714267e8a4adb91773df43975e62b5ad24065719f3aec825b955442db1b88d6bde48ebed00431915f23991b58c1b24e7033f26f752ddd0c3a16dd4c5a2764055d5e09ae839e2c21404fdd5e90163f4e06305755700f883f11fd50a3277775b76373f6acac68a94faf3a29a798cfc8e869e786d5790e363adbd87037de537ffb2870591e3b5672bf7adaa0c86491041878d8277296fcf6089b5e8b1188c64d0d0bbd6db7";
    public static final boolean EMERGENCY_ASSISTANCE_ENABLED = false;
    public static final String EXTRA_BACKGROUND_CALL_STATE = "background_state";
    public static final String EXTRA_DATA_APN = "apn";
    public static final String EXTRA_DATA_APN_TYPE = "apnType";
    public static final String EXTRA_DATA_CHANGE_REASON = "reason";
    public static final String EXTRA_DATA_FAILURE_CAUSE = "failCause";
    public static final String EXTRA_DATA_LINK_PROPERTIES_KEY = "linkProperties";
    public static final String EXTRA_DATA_NETWORK_TYPE = "networkType";
    public static final String EXTRA_DATA_STATE = "state";
    public static final String EXTRA_DISCONNECT_CAUSE = "disconnect_cause";
    public static final String EXTRA_FOREGROUND_CALL_STATE = "foreground_state";
    public static final String EXTRA_INCOMING_NUMBER = "incoming_number";
    public static final String EXTRA_PRECISE_DISCONNECT_CAUSE = "precise_disconnect_cause";
    public static final String EXTRA_RINGING_CALL_STATE = "ringing_state";
    public static final String EXTRA_STATE = "state";
    public static final String EXTRA_STATE_IDLE = State.IDLE.toString();
    public static final String EXTRA_STATE_OFFHOOK = State.OFFHOOK.toString();
    public static final String EXTRA_STATE_RINGING = State.RINGING.toString();
    public static final String EXTRA_VOIP_CALLSTATE = "is_voip_callstate";
    private static final String KNIGHT_LOG_FILE_NAME = "/data/log/knightBuff.tmp";
    private static final String LTE_ON_CDMA_FILE_PATH = "/efs/carrier/CdmaOnly";
    public static final int NETWORK_CLASS_2_G = 1;
    public static final int NETWORK_CLASS_3_G = 2;
    public static final int NETWORK_CLASS_4_G = 3;
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    public static final int NETWORK_TYPE_1xRTT = 7;
    public static final int NETWORK_TYPE_CDMA = 4;
    public static final int NETWORK_TYPE_DC = 30;
    public static final int NETWORK_TYPE_EDGE = 2;
    public static final int NETWORK_TYPE_EHRPD = 14;
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    public static final int NETWORK_TYPE_EVDO_A = 6;
    public static final int NETWORK_TYPE_EVDO_B = 12;
    public static final int NETWORK_TYPE_GPRS = 1;
    public static final int NETWORK_TYPE_GSM = 16;
    public static final int NETWORK_TYPE_HSDPA = 8;
    public static final int NETWORK_TYPE_HSPA = 10;
    public static final int NETWORK_TYPE_HSPAP = 15;
    public static final int NETWORK_TYPE_HSUPA = 9;
    public static final int NETWORK_TYPE_IDEN = 11;
    public static final int NETWORK_TYPE_IWLAN = 18;
    public static final int NETWORK_TYPE_LTE = 13;
    public static final int NETWORK_TYPE_TDLTE = 31;
    public static final int NETWORK_TYPE_TD_SCDMA = 17;
    public static final int NETWORK_TYPE_UMTS = 3;
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    public static final int PHONE_TYPE_CDMA = 2;
    public static final int PHONE_TYPE_GSM = 1;
    public static final int PHONE_TYPE_NONE = 0;
    public static final int PHONE_TYPE_SIP = 3;
    private static final String[] PREFIX_TABLE = new String[]{"010", "SKT", "010", "010", "010", "010", "KTF", "STI", "HSP", "LGT"};
    private static final boolean SHIP_BUILD = SmartFaceManager.TRUE.equals(SystemProperties.get("ro.product_ship", SmartFaceManager.FALSE));
    public static final int SIM_ACTIVATION_RESULT_CANCELED = 4;
    public static final int SIM_ACTIVATION_RESULT_COMPLETE = 0;
    public static final int SIM_ACTIVATION_RESULT_FAILED = 3;
    public static final int SIM_ACTIVATION_RESULT_IN_PROGRESS = 2;
    public static final int SIM_ACTIVATION_RESULT_NOT_SUPPORTED = 1;
    public static final int SIM_STATE_ABSENT = 1;
    public static final int SIM_STATE_CARD_IO_ERROR = 8;
    public static final int SIM_STATE_NETWORK_LOCKED = 4;
    public static final int SIM_STATE_NOT_READY = 6;
    public static final int SIM_STATE_PERM_DISABLED = 7;
    public static final int SIM_STATE_PIN_REQUIRED = 2;
    public static final int SIM_STATE_PUK_REQUIRED = 3;
    public static final int SIM_STATE_READY = 5;
    public static final int SIM_STATE_UNKNOWN = 0;
    private static final String TAG = "TelephonyManager";
    private static final String TAG_DM_LOGGING = "DmLoggingService";
    private static final String UKNIGHT_LGT_APP_SIGNATURE = "3082019d30820106a00302010202044f3193c6300d06092a864886f70d010105050030133111300f060355040a0c084c475f55706c7573301e170d3132303230373231313233385a170d3432303133303231313233385a30133111300f060355040a0c084c475f55706c757330819f300d06092a864886f70d010101050003818d0030818902818100872b7051b6c30272b6c200b809a90a4f7fa148bdb554a4b29df536018f256c624c6781006655a30eef98152781353b48da3aa739d8e0bdc2fcee10789438454bce9dcf081a3a8757ecb6f2985bcdec0b83e7ed46dc35ac36e3820442740b0b6c6e05ac17d49502708070e1137914eb26d2e63c9235efbb6d930a353c004228490203010001300d06092a864886f70d0101050500038181004157e820571e50c367497ab98c05375a7d8e40ea67ff3df8858226322faf91e5c12521266402ce9d2e946d25b0833cc7c4b39a2b28cae46e184b16f973a885fd2f607decafcb814ad326739a35d3703c140ac5bdbb18f1598f997e1ae52fcefeee88f3419db99379e63caa981a632d41a23549a0a03e843bf285ad6cdbcaa6f4";
    private static final String UKNIGHT_PACKAGE_NAME = "com.lguplus.uknight2";
    private static final int UKNIGHT_TOKEN = 6012;
    public static final int VOIPCALL_STATE_IDLE = 3;
    public static final int VOIPCALL_STATE_OFFHOOK = 5;
    public static final int VOIPCALL_STATE_RINGING = 4;
    public static final String VVM_TYPE_CVVM = "vvm_type_cvvm";
    public static final String VVM_TYPE_OMTP = "vvm_type_omtp";
    public static boolean isCDMAMessage = false;
    private static boolean isRtreFileRead = false;
    public static boolean isSelecttelecomDF = false;
    private static String mConfigNetworkTypeCapability = CscFeature.getInstance().getString("CscFeature_RIL_ConfigNetworkTypeCapability");
    private static final String mDmLoggingPidFile = "/data/log/dmlog_pid";
    public static String mImsLineNumber = null;
    private static String mRtreVal = null;
    private static String multiSimConfig = SystemProperties.get(TelephonyProperties.PROPERTY_MULTI_SIM_CONFIG);
    private static TelephonyManager sInstance = new TelephonyManager();
    private static final String sKernelCmdLine = getProcCmdLine();
    private static final String sLteOnCdmaProductType = SystemProperties.get(TelephonyProperties.PROPERTY_LTE_ON_CDMA_PRODUCT_TYPE, "");
    private static final Pattern sProductTypePattern = Pattern.compile("\\sproduct_type\\s*=\\s*(\\w+)");
    private static ITelephonyRegistry sRegistry;
    private final Context mContext;
    private SubscriptionManager mSubscriptionManager;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$telephony$TelephonyManager$MultiSimVariants = new int[MultiSimVariants.values().length];

        static {
            try {
                $SwitchMap$android$telephony$TelephonyManager$MultiSimVariants[MultiSimVariants.UNKNOWN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$telephony$TelephonyManager$MultiSimVariants[MultiSimVariants.DSDS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$telephony$TelephonyManager$MultiSimVariants[MultiSimVariants.DSDA.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$telephony$TelephonyManager$MultiSimVariants[MultiSimVariants.TSTS.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public class GbaBootstrappingResponse {
        public byte[] auts;
        public byte[] res;

        public GbaBootstrappingResponse(Bundle bundle) {
            this.res = bundle.getByteArray("res");
            this.auts = bundle.getByteArray("auts");
        }
    }

    public enum MultiSimVariants {
        DSDS,
        DSDA,
        TSTS,
        UNKNOWN
    }

    public interface WifiCallingChoices {
        public static final int ALWAYS_USE = 0;
        public static final int ASK_EVERY_TIME = 1;
        public static final int NEVER_USE = 2;
    }

    public TelephonyManager(Context context) {
        Context appContext = context.getApplicationContext();
        if (appContext != null) {
            this.mContext = appContext;
        } else {
            this.mContext = context;
        }
        this.mSubscriptionManager = SubscriptionManager.from(this.mContext);
        if (sRegistry == null) {
            sRegistry = Stub.asInterface(ServiceManager.getService("telephony.registry"));
        }
    }

    private TelephonyManager() {
        this.mContext = null;
    }

    public static TelephonyManager getDefault() {
        return sInstance;
    }

    private String getOpPackageName() {
        if (this.mContext != null) {
            return this.mContext.getOpPackageName();
        }
        return ActivityThread.currentOpPackageName();
    }

    public static TelephonyManager getFirst() {
        return null;
    }

    public static TelephonyManager getSecondary() {
        return null;
    }

    public MultiSimVariants getMultiSimConfiguration() {
        String mSimConfig = SystemProperties.get(TelephonyProperties.PROPERTY_MULTI_SIM_CONFIG);
        if (mSimConfig.equals("dsds")) {
            return MultiSimVariants.DSDS;
        }
        if (mSimConfig.equals("dsda")) {
            return MultiSimVariants.DSDA;
        }
        if (mSimConfig.equals("tsts")) {
            return MultiSimVariants.TSTS;
        }
        return MultiSimVariants.UNKNOWN;
    }

    public int getPhoneCount() {
        switch (AnonymousClass1.$SwitchMap$android$telephony$TelephonyManager$MultiSimVariants[getMultiSimConfiguration().ordinal()]) {
            case 1:
                return 1;
            case 2:
            case 3:
                return 2;
            case 4:
                return 3;
            default:
                return 1;
        }
    }

    public static TelephonyManager from(Context context) {
        return (TelephonyManager) context.getSystemService(PhoneConstants.PHONE_KEY);
    }

    public boolean isMultiSimEnabled() {
        return multiSimConfig.equals("dsds") || multiSimConfig.equals("dsda") || multiSimConfig.equals("tsts");
    }

    public String getDeviceSoftwareVersion() {
        return getDeviceSoftwareVersion(getDefaultPhone());
    }

    public String getDeviceSoftwareVersion(int slotId) {
        int[] subId = SubscriptionManager.getSubId(slotId);
        if (subId == null || subId.length == 0) {
            return null;
        }
        if (mConfigNetworkTypeCapability != null && mConfigNetworkTypeCapability.startsWith("SPR-") && getLteOnCdmaModeStatic() != 1) {
            return SystemProperties.get(TelephonyProperties.PROPERTY_BASEBAND_VERSION);
        }
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                return info.getDeviceSvnUsingSubId(subId[0], this.mContext.getOpPackageName());
            }
            return null;
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public String getDeviceId() {
        String str = null;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                str = telephony.getDeviceId(this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getDeviceId(int slotId) {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getDeviceIdForPhone(slotId, this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getImei() {
        return getImei(getDefaultSim());
    }

    public String getImei(int slotId) {
        String str = null;
        int[] subId = SubscriptionManager.getSubId(slotId);
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getImeiForSubscriber(subId[0], this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getNai() {
        return getNai(getDefaultSim());
    }

    public String getNai(int slotId) {
        int[] subId = SubscriptionManager.getSubId(slotId);
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            String nai = info.getNaiForSubscriber(subId[0], this.mContext.getOpPackageName());
            if (!Log.isLoggable(TAG, 2)) {
                return nai;
            }
            Rlog.v(TAG, "Nai = " + nai);
            return nai;
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public String getMeid() {
        return getMeid(getDefaultSim());
    }

    public String getMeid(int slotId) {
        String str = null;
        try {
            str = getSubscriberInfo().getMeidForSubscriber(SubscriptionManager.getSubId(slotId)[0]);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public CellLocation getCellLocation() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                Rlog.d(TAG, "getCellLocation returning null because telephony is null");
                return null;
            }
            Bundle bundle = telephony.getCellLocation(this.mContext.getOpPackageName());
            if (bundle.isEmpty()) {
                Rlog.d(TAG, "getCellLocation returning null because bundle is empty");
                return null;
            }
            CellLocation cl = CellLocation.newFromBundle(bundle);
            if (!cl.isEmpty()) {
                return cl;
            }
            Rlog.d(TAG, "getCellLocation returning null because CellLocation is empty");
            return null;
        } catch (RemoteException ex) {
            Rlog.d(TAG, "getCellLocation returning null due to RemoteException " + ex);
            return null;
        } catch (NullPointerException ex2) {
            Rlog.d(TAG, "getCellLocation returning null due to NullPointerException " + ex2);
            return null;
        }
    }

    public void enableLocationUpdates() {
        enableLocationUpdates(getDefaultSubscription());
    }

    public void enableLocationUpdates(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.enableLocationUpdatesForSubscriber(subId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
    }

    public void disableLocationUpdates() {
        disableLocationUpdates(getDefaultSubscription());
    }

    public void disableLocationUpdates(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.disableLocationUpdatesForSubscriber(subId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
    }

    @Deprecated
    public List<NeighboringCellInfo> getNeighboringCellInfo() {
        List<NeighboringCellInfo> list = null;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                list = telephony.getNeighboringCellInfo(this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return list;
    }

    public int getCurrentPhoneType() {
        return getCurrentPhoneType(getDefaultSubscription());
    }

    public int getCurrentPhoneType(int subId) {
        int phoneId;
        if (subId == -1) {
            phoneId = 0;
        } else {
            phoneId = SubscriptionManager.getPhoneId(subId);
        }
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null || subId == -1) {
                return getPhoneTypeFromProperty(phoneId);
            }
            return telephony.getActivePhoneTypeForSubscriber(subId);
        } catch (RemoteException e) {
            return getPhoneTypeFromProperty(phoneId);
        } catch (NullPointerException e2) {
            return getPhoneTypeFromProperty(phoneId);
        }
    }

    public int getPhoneType() {
        if (isVoiceCapable() && !SystemProperties.get("ro.product.name", "").matches(".*ldu.*")) {
            return getCurrentPhoneType();
        }
        return 0;
    }

    private int getPhoneTypeFromProperty() {
        return getPhoneTypeFromProperty(getDefaultPhone());
    }

    private int getPhoneTypeFromProperty(int phoneId) {
        String type = getTelephonyProperty(phoneId, TelephonyProperties.CURRENT_ACTIVE_PHONE, null);
        if (type == null || type.equals("")) {
            return getPhoneTypeFromNetworkType(phoneId);
        }
        return Integer.parseInt(type);
    }

    private int getPhoneTypeFromNetworkType() {
        return getPhoneTypeFromNetworkType(getDefaultPhone());
    }

    private int getPhoneTypeFromNetworkType(int phoneId) {
        String mode = getTelephonyProperty(phoneId, "ro.telephony.default_network", null);
        if (mode != null) {
            return getPhoneType(Integer.parseInt(mode));
        }
        return 0;
    }

    public static int getPhoneType(int networkMode) {
        if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_CdmalteTelephonyCommon") && networkMode == 10) {
            return 2;
        }
        switch (networkMode) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 9:
            case 10:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
                return 1;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 21:
            case 22:
                return 2;
            case 11:
                if (getLteOnCdmaModeStatic() != 1) {
                    return 1;
                }
                return 2;
            default:
                return 1;
        }
    }

    private static String getProcCmdLine() {
        IOException e;
        Throwable th;
        String cmdline = "";
        FileInputStream is = null;
        try {
            FileInputStream is2 = new FileInputStream("/proc/cmdline");
            try {
                byte[] buffer = new byte[2048];
                int count = is2.read(buffer);
                if (count > 0) {
                    cmdline = new String(buffer, 0, count);
                }
                if (is2 != null) {
                    try {
                        is2.close();
                        is = is2;
                    } catch (IOException e2) {
                        is = is2;
                    }
                }
            } catch (IOException e3) {
                e = e3;
                is = is2;
                try {
                    Rlog.d(TAG, "No /proc/cmdline exception=" + e);
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e4) {
                        }
                    }
                    Rlog.d(TAG, "/proc/cmdline=" + cmdline);
                    return cmdline;
                } catch (Throwable th2) {
                    th = th2;
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e5) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                is = is2;
                if (is != null) {
                    is.close();
                }
                throw th;
            }
        } catch (IOException e6) {
            e = e6;
            Rlog.d(TAG, "No /proc/cmdline exception=" + e);
            if (is != null) {
                is.close();
            }
            Rlog.d(TAG, "/proc/cmdline=" + cmdline);
            return cmdline;
        }
        Rlog.d(TAG, "/proc/cmdline=" + cmdline);
        return cmdline;
    }

    public static int getLteOnCdmaModeStatic() {
        int retVal;
        Matcher matcher;
        Throwable th;
        String productType = "";
        int curVal = SystemProperties.getInt(TelephonyProperties.PROPERTY_LTE_ON_CDMA_DEVICE, -1);
        if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_ConfigNvSim")) {
            BufferedReader in = null;
            if (!isRtreFileRead) {
                try {
                    BufferedReader in2 = new BufferedReader(new FileReader(LTE_ON_CDMA_FILE_PATH));
                    try {
                        mRtreVal = in2.readLine();
                        isRtreFileRead = true;
                        Rlog.d(TAG, "LTE_ON_CDMA_FILE_PATH, mRtreVal = /efs/carrier/CdmaOnly" + mRtreVal);
                        if (in2 != null) {
                            try {
                                in2.close();
                            } catch (IOException e) {
                                Rlog.d(TAG, "Error in Buffer reader");
                                in = in2;
                            }
                        }
                        in = in2;
                    } catch (IOException e2) {
                        in = in2;
                        try {
                            Rlog.d(TAG, "RTRE File doesnt exist");
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e3) {
                                    Rlog.d(TAG, "Error in Buffer reader");
                                }
                            }
                            if (mRtreVal != null) {
                                if (!SmartFaceManager.PAGE_MIDDLE.equals(mRtreVal)) {
                                    Rlog.d(TAG, "RTRE True");
                                    return 1;
                                } else if (SmartFaceManager.PAGE_BOTTOM.equals(mRtreVal)) {
                                    return -1;
                                } else {
                                    Rlog.d(TAG, "RTRE False");
                                    return 0;
                                }
                            }
                            retVal = curVal;
                            if (retVal == -1) {
                                matcher = sProductTypePattern.matcher(sKernelCmdLine);
                                if (matcher.find()) {
                                    productType = matcher.group(1);
                                    if (sLteOnCdmaProductType.equals(productType)) {
                                        retVal = 1;
                                    } else {
                                        retVal = 0;
                                    }
                                } else {
                                    retVal = 0;
                                }
                            }
                            Rlog.d(TAG, "getLteOnCdmaMode=" + retVal + " curVal=" + curVal + " product_type='" + productType + "' lteOnCdmaProductType='" + sLteOnCdmaProductType + "'");
                            return retVal;
                        } catch (Throwable th2) {
                            th = th2;
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e4) {
                                    Rlog.d(TAG, "Error in Buffer reader");
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        in = in2;
                        if (in != null) {
                            in.close();
                        }
                        throw th;
                    }
                } catch (IOException e5) {
                    Rlog.d(TAG, "RTRE File doesnt exist");
                    if (in != null) {
                        in.close();
                    }
                    if (mRtreVal != null) {
                        if (!SmartFaceManager.PAGE_MIDDLE.equals(mRtreVal)) {
                            Rlog.d(TAG, "RTRE True");
                            return 1;
                        } else if (SmartFaceManager.PAGE_BOTTOM.equals(mRtreVal)) {
                            return -1;
                        } else {
                            Rlog.d(TAG, "RTRE False");
                            return 0;
                        }
                    }
                    retVal = curVal;
                    if (retVal == -1) {
                        matcher = sProductTypePattern.matcher(sKernelCmdLine);
                        if (matcher.find()) {
                            retVal = 0;
                        } else {
                            productType = matcher.group(1);
                            if (sLteOnCdmaProductType.equals(productType)) {
                                retVal = 0;
                            } else {
                                retVal = 1;
                            }
                        }
                    }
                    Rlog.d(TAG, "getLteOnCdmaMode=" + retVal + " curVal=" + curVal + " product_type='" + productType + "' lteOnCdmaProductType='" + sLteOnCdmaProductType + "'");
                    return retVal;
                }
            }
            if (mRtreVal != null) {
                if (!SmartFaceManager.PAGE_MIDDLE.equals(mRtreVal)) {
                    Rlog.d(TAG, "RTRE True");
                    return 1;
                } else if (SmartFaceManager.PAGE_BOTTOM.equals(mRtreVal)) {
                    return -1;
                } else {
                    Rlog.d(TAG, "RTRE False");
                    return 0;
                }
            }
        }
        retVal = curVal;
        if (retVal == -1) {
            matcher = sProductTypePattern.matcher(sKernelCmdLine);
            if (matcher.find()) {
                productType = matcher.group(1);
                if (sLteOnCdmaProductType.equals(productType)) {
                    retVal = 1;
                } else {
                    retVal = 0;
                }
            } else {
                retVal = 0;
            }
        }
        Rlog.d(TAG, "getLteOnCdmaMode=" + retVal + " curVal=" + curVal + " product_type='" + productType + "' lteOnCdmaProductType='" + sLteOnCdmaProductType + "'");
        return retVal;
    }

    public String getNetworkOperatorName() {
        return getNetworkOperatorName(getDefaultSubscription());
    }

    public String getNetworkOperatorName(int subId) {
        int phoneId = SubscriptionManager.getPhoneId(subId);
        String operatorNumeric = getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OPERATOR_NUMERIC, "");
        String simOperatorNumeric = SystemProperties.get(TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC);
        String salesCode = SystemProperties.get("ro.csc.sales_code", KeyProperties.DIGEST_NONE);
        if (mConfigNetworkTypeCapability != null && mConfigNetworkTypeCapability.startsWith("SPR-")) {
            if (getCurrentPhoneType() == 2) {
                String homeOperatorAlpha = getTelephonyProperty(phoneId, "ro.cdma.home.operator.alpha", "");
                if (homeOperatorAlpha != null && homeOperatorAlpha.length() >= 2 && "Chameleon".equals(homeOperatorAlpha)) {
                    return "Samsung";
                }
            }
            operatorNumeric = getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OPERATOR_NUMERIC_REAL, operatorNumeric);
            if ("44020".equals(operatorNumeric)) {
                return "SoftBank";
            }
        }
        if ("IUS".equals(salesCode) && "334090".equals(simOperatorNumeric) && "334090".equals(operatorNumeric)) {
            return "Iusacell";
        }
        return getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OPERATOR_ALPHA, "");
    }

    public String getNetworkOperator() {
        return getNetworkOperatorForPhone(getDefaultPhone());
    }

    public String getNetworkOperatorForSubscription(int subId) {
        return getNetworkOperatorForPhone(SubscriptionManager.getPhoneId(subId));
    }

    public String getNetworkOperatorForPhone(int phoneId) {
        if (mConfigNetworkTypeCapability != null && mConfigNetworkTypeCapability.startsWith("SPR-") && getCurrentPhoneType() == 2) {
            if (checkIsSprintSimCard() && isCdmaOrLteRat(getNetworkType()) && !isNetworkRoaming()) {
                String homeOperatorNumeric = SystemProperties.get("ro.cdma.home.operator.numeric");
                if (homeOperatorNumeric != null && homeOperatorNumeric.length() >= 4) {
                    Rlog.d(TAG, "getNetworkOperator sprint sim + cdma home/lte home = " + homeOperatorNumeric);
                    return homeOperatorNumeric;
                }
            }
            Rlog.d(TAG, "getNetworkOperator network value");
            return getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OPERATOR_NUMERIC_REAL, "");
        }
        return getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OPERATOR_NUMERIC, "");
    }

    private boolean isCdmaOrLteRat(int radioTech) {
        switch (radioTech) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 12:
            case 13:
            case 14:
                return true;
            default:
                return false;
        }
    }

    private boolean checkIsSprintSimCard() {
        String simoperator = getTelephonyProperty(getDefaultPhone(), TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC, "");
        if ("310120".equals(simoperator) || "310000".equals(simoperator) || "311490".equals(simoperator) || "311870".equals(simoperator) || "312530".equals(simoperator)) {
            return true;
        }
        return false;
    }

    public boolean isNetworkRoaming() {
        return isNetworkRoaming(getDefaultSubscription());
    }

    public boolean isNetworkRoaming(int subId) {
        return Boolean.parseBoolean(getTelephonyProperty(SubscriptionManager.getPhoneId(subId), TelephonyProperties.PROPERTY_OPERATOR_ISROAMING, null));
    }

    public String getNetworkCountryIso() {
        return getNetworkCountryIsoForPhone(getDefaultPhone());
    }

    public String getNetworkCountryIsoForSubscription(int subId) {
        return getNetworkCountryIsoForPhone(SubscriptionManager.getPhoneId(subId));
    }

    public String getNetworkCountryIsoForPhone(int phoneId) {
        if (phoneId == -1) {
            Rlog.d(TAG, "INVALID_PHONE_INDEX ==> DEFAULT_PHONE_INDEX");
            phoneId = 0;
        }
        return getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OPERATOR_ISO_COUNTRY, "");
    }

    public int getNetworkType() {
        int i = 0;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                i = telephony.getNetworkType();
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    public int getNetworkType(int subId) {
        int i = 0;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                i = telephony.getNetworkTypeForSubscriber(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    public int getDataNetworkType() {
        return getDataNetworkType(getDefaultSubscription());
    }

    public int getDataNetworkType(int subId) {
        int i = 0;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                i = telephony.getDataNetworkTypeForSubscriber(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    public int getVoiceNetworkType() {
        return getVoiceNetworkType(getDefaultSubscription());
    }

    public int getVoiceNetworkType(int subId) {
        int i = 0;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                i = telephony.getVoiceNetworkTypeForSubscriber(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    public static int getNetworkClass(int networkType) {
        switch (networkType) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
            case 16:
                return 1;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
            case 17:
                return 2;
            case 13:
            case 18:
                return 3;
            default:
                return 0;
        }
    }

    public String getNetworkTypeName() {
        return getNetworkTypeName(getNetworkType());
    }

    public static String getNetworkTypeName(int type) {
        switch (type) {
            case 1:
                return "GPRS";
            case 2:
                return "EDGE";
            case 3:
                return "UMTS";
            case 4:
                return "CDMA";
            case 5:
                return "CDMA - EvDo rev. 0";
            case 6:
                return "CDMA - EvDo rev. A";
            case 7:
                return "CDMA - 1xRTT";
            case 8:
                return "HSDPA";
            case 9:
                return "HSUPA";
            case 10:
                return "HSPA";
            case 11:
                return "iDEN";
            case 12:
                return "CDMA - EvDo rev. B";
            case 13:
                if ("KDI".equals("EUR")) {
                    return "4G";
                }
                if ("SBM".equals("EUR")) {
                    return "4G";
                }
                return "LTE";
            case 14:
                return "CDMA - eHRPD";
            case 15:
                return "HSPA+";
            case 16:
                return "GSM";
            case 17:
                return "TD_SCDMA";
            case 18:
                return "IWLAN";
            case 30:
                return "DC";
            default:
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
    }

    public boolean hasIccCard() {
        return hasIccCard(getDefaultSim());
    }

    public boolean hasIccCard(int slotId) {
        boolean z = false;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                z = telephony.hasIccCardUsingSlotId(slotId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    public int getSimState() {
        int slotIdx = getDefaultSim();
        if (slotIdx >= 0) {
            return getSimState(slotIdx);
        }
        for (int i = 0; i < getPhoneCount(); i++) {
            int simState = getSimState(i);
            if (simState != 1) {
                Rlog.d(TAG, "getSimState: default sim:" + slotIdx + ", sim state for " + "slotIdx=" + i + " is " + simState + ", return state as unknown");
                return 0;
            }
        }
        Rlog.d(TAG, "getSimState: default sim:" + slotIdx + ", all SIMs absent, return " + "state as absent");
        return 1;
    }

    public int getSimState(int slotIdx) {
        return SubscriptionManager.getSimStateForSlotIdx(slotIdx);
    }

    public String getSimOperator() {
        return getSimOperatorNumeric();
    }

    public String getSimOperator(int subId) {
        return getSimOperatorNumericForSubscription(subId);
    }

    public String getSimOperatorNumeric() {
        int subId = SubscriptionManager.getDefaultDataSubId();
        if (!SubscriptionManager.isUsableSubIdValue(subId)) {
            subId = SubscriptionManager.getDefaultSmsSubId();
            if (!SubscriptionManager.isUsableSubIdValue(subId)) {
                subId = SubscriptionManager.getDefaultVoiceSubId();
                if (!SubscriptionManager.isUsableSubIdValue(subId)) {
                    subId = SubscriptionManager.getDefaultSubId();
                }
            }
        }
        return getSimOperatorNumericForSubscription(subId);
    }

    public String getSimOperatorNumericForSubscription(int subId) {
        return getSimOperatorNumericForPhone(SubscriptionManager.getPhoneId(subId));
    }

    public String getSimOperatorNumericForPhone(int phoneId) {
        return getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC, "");
    }

    public String getSimOperatorName() {
        if (mConfigNetworkTypeCapability == null || !mConfigNetworkTypeCapability.startsWith("VZW-")) {
            return getSimOperatorNameForPhone(getDefaultPhone());
        }
        return "Verizon";
    }

    public String getSimOperatorNameForSubscription(int subId) {
        return getSimOperatorNameForPhone(SubscriptionManager.getPhoneId(subId));
    }

    public String getSimOperatorNameForPhone(int phoneId) {
        return getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_ICC_OPERATOR_ALPHA, "");
    }

    public String getSimCountryIso() {
        return getSimCountryIsoForPhone(getDefaultPhone());
    }

    public String getSimCountryIso(int subId) {
        return getSimCountryIsoForSubscription(subId);
    }

    public String getSimCountryIsoForSubscription(int subId) {
        return getSimCountryIsoForPhone(SubscriptionManager.getPhoneId(subId));
    }

    public String getSimCountryIsoForPhone(int phoneId) {
        return getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_ICC_OPERATOR_ISO_COUNTRY, "");
    }

    public String getSimSerialNumber() {
        return getSimSerialNumber(getDefaultSubscription());
    }

    public String getSimSerialNumber(int subId) {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getIccSerialNumberForSubscriber(subId, this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public int getLteOnCdmaMode() {
        return getLteOnCdmaMode(getDefaultSubscription());
    }

    public int getLteOnCdmaMode(int subId) {
        int i = -1;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                i = telephony.getLteOnCdmaModeForSubscriber(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    public String getSubscriberId() {
        return getSubscriberId(getDefaultSubscription());
    }

    public String getSubscriberId(int subId) {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getSubscriberIdForSubscriber(subId, this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getSubscriberIdDm(int networkType) {
        return getSubscriberIdDm(getDefaultSubscription(), networkType);
    }

    public String getSubscriberIdDm(int subId, int networkType) {
        String str = null;
        try {
            str = getSubscriberInfo().getSubscriberIdDm(subId, networkType);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getGroupIdLevel1() {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getGroupIdLevel1(this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getGroupIdLevel1(int subId) {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getGroupIdLevel1ForSubscriber(subId, this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getLine1Number() {
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_Common_EnablePrivacyDataGuard") || PdgUimAccessChecker.checkPrivacy(this.mContext)) {
            return getLine1NumberForSubscriber(getDefaultSubscription());
        }
        return null;
    }

    public String getLine1NumberForSubscriber(int subId) {
        String number = null;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                number = telephony.getLine1NumberForDisplay(subId, this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        if (number != null) {
            return number;
        }
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getLine1NumberForSubscriber(subId, this.mContext.getOpPackageName());
        } catch (RemoteException e3) {
            return null;
        } catch (NullPointerException e4) {
            return null;
        }
    }

    public boolean setLine1NumberForDisplay(String alphaTag, String number) {
        return setLine1NumberForDisplayForSubscriber(getDefaultSubscription(), alphaTag, number);
    }

    public boolean setLine1NumberForDisplayForSubscriber(int subId, String alphaTag, String number) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setLine1NumberForDisplayForSubscriber(subId, alphaTag, number);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return false;
    }

    public String getLine1AlphaTag() {
        return getLine1AlphaTagForSubscriber(getDefaultSubscription());
    }

    public String getLine1AlphaTagForSubscriber(int subId) {
        String alphaTag = null;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                alphaTag = telephony.getLine1AlphaTagForDisplay(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        if (alphaTag != null) {
            return alphaTag;
        }
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getLine1AlphaTagForSubscriber(subId, getOpPackageName());
        } catch (RemoteException e3) {
            return null;
        } catch (NullPointerException e4) {
            return null;
        }
    }

    public String[] getMergedSubscriberIds() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getMergedSubscriberIds(getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return null;
    }

    public String getMsisdn() {
        return getMsisdn(getDefaultSubscription());
    }

    public String getMsisdn(int subId) {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getMsisdnForSubscriber(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getVoiceMailNumber() {
        return getVoiceMailNumber(getDefaultSubscription());
    }

    public String getVoiceMailNumber(int subId) {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getVoiceMailNumberForSubscriber(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getCompleteVoiceMailNumber() {
        return getCompleteVoiceMailNumber(getDefaultSubscription());
    }

    public String getCompleteVoiceMailNumber(int subId) {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getCompleteVoiceMailNumberForSubscriber(subId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public boolean setVoiceMailNumber(String alphaTag, String number) {
        return setVoiceMailNumber(getDefaultSubscription(), alphaTag, number);
    }

    public boolean setVoiceMailNumber(int subId, String alphaTag, String number) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setVoiceMailNumber(subId, alphaTag, number);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return false;
    }

    public int getVoiceMessageCount() {
        return getVoiceMessageCount(getDefaultSubscription());
    }

    public int getVoiceMessageCount(int subId) {
        int i = 0;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                i = telephony.getVoiceMessageCountForSubscriber(subId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    public String getVoiceMailAlphaTag() {
        return getVoiceMailAlphaTag(getDefaultSubscription());
    }

    public String getVoiceMailAlphaTag(int subId) {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getVoiceMailAlphaTagForSubscriber(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getIsimImpi() {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getIsimImpi();
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getIsimImpi(int subId) {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getIsimImpiForSubscriber(subId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getIsimDomain() {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getIsimDomain();
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getIsimDomain(int subId) {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getIsimDomainForSubscriber(subId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String[] getIsimImpu() {
        String[] strArr = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                strArr = info.getIsimImpu();
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return strArr;
    }

    public String[] getIsimImpu(int subId) {
        String[] strArr = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                strArr = info.getIsimImpuForSubscriber(subId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return strArr;
    }

    private IPhoneSubInfo getSubscriberInfo() {
        return IPhoneSubInfo.Stub.asInterface(ServiceManager.getService("iphonesubinfo"));
    }

    public int getCallState() {
        try {
            ITelecomService telecom = getTelecomService();
            if (telecom != null) {
                return telecom.getCallState();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelecomService#getCallState", e);
        }
        return 0;
    }

    public int getCallState(int subId) {
        int i = 0;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                i = telephony.getCallStateForSubscriber(subId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    public int getDataActivity() {
        int i = 0;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                i = telephony.getDataActivity();
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    public int getDataState() {
        int i = 0;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                i = telephony.getDataState();
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    public int getDataState(int subId) {
        int i = 0;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                i = telephony.getDataStateSimSlot(subId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    private ITelephony getITelephony() {
        return ITelephony.Stub.asInterface(ServiceManager.getService(PhoneConstants.PHONE_KEY));
    }

    private ITelecomService getTelecomService() {
        return ITelecomService.Stub.asInterface(ServiceManager.getService("telecom"));
    }

    public void listen(PhoneStateListener listener, int events) {
        if (this.mContext != null) {
            try {
                sRegistry.listenForSubscriber(listener.mSubId, getOpPackageName(), listener.callback, events, Boolean.valueOf(getITelephony() != null).booleanValue());
            } catch (RemoteException e) {
            } catch (NullPointerException e2) {
            }
        }
    }

    public int getCdmaEriIconIndex() {
        return getCdmaEriIconIndex(getDefaultSubscription());
    }

    public int getCdmaEriIconIndex(int subId) {
        int i = -1;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                i = telephony.getCdmaEriIconIndexForSubscriber(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    public int getCdmaEriIconMode() {
        return getCdmaEriIconMode(getDefaultSubscription());
    }

    public int getCdmaEriIconMode(int subId) {
        int i = -1;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                i = telephony.getCdmaEriIconModeForSubscriber(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    public String getCdmaEriText() {
        return getCdmaEriText(getDefaultSubscription());
    }

    public String getCdmaEriText(int subId) {
        String str = null;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                str = telephony.getCdmaEriTextForSubscriber(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public boolean isVoiceCapable() {
        if (this.mContext == null) {
            return true;
        }
        return this.mContext.getResources().getBoolean(R.bool.config_voice_capable);
    }

    public boolean isSmsCapable() {
        if (this.mContext == null) {
            return true;
        }
        boolean isCdmaTablet = false;
        boolean isAtt = false;
        if (mConfigNetworkTypeCapability != null && (mConfigNetworkTypeCapability.indexOf("-GLB-USA") >= 0 || mConfigNetworkTypeCapability.indexOf("-CDM-USA") >= 0)) {
            isCdmaTablet = !GeneralUtil.isPhone();
        }
        if (mConfigNetworkTypeCapability.contains("ATT")) {
            isAtt = true;
        }
        if (isCdmaTablet) {
            Rlog.d(TAG, "US CDMA Tablet Model");
            return false;
        } else if (!GeneralUtil.isTablet() || !isAtt) {
            return this.mContext.getResources().getBoolean(R.bool.config_sms_capable);
        } else {
            ActivityManager am = (ActivityManager) this.mContext.getSystemService("activity");
            if (am == null) {
                return false;
            }
            List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
            if (runningTasks.isEmpty()) {
                return false;
            }
            ComponentName topActivity = ((RunningTaskInfo) runningTasks.get(0)).topActivity;
            if (topActivity == null) {
                return false;
            }
            Rlog.d(TAG, "getTopPackageName = " + topActivity.getPackageName());
            if ("com.android.vending".equals(topActivity.getPackageName())) {
                return true;
            }
            return false;
        }
    }

    public boolean isExtraCapable(int capableType) {
        if (capableType != 1) {
            return false;
        }
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_RIL_DisableSmartBonding")) {
            String salesCode = SystemProperties.get("ro.csc.sales_code", KeyProperties.DIGEST_NONE);
            Rlog.d(TAG, "isExtraCapable(Download Booster) : all features are enabled");
            if (!salesCode.equalsIgnoreCase("XAS")) {
                return true;
            }
            if ("312530".equals(getSimOperator())) {
                Rlog.d(TAG, "isExtraCapable(Download Booster) : blocked for Sprint Prepaid");
                return false;
            }
        }
        return true;
    }

    public List<CellInfo> getAllCellInfo() {
        List<CellInfo> list = null;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                list = telephony.getAllCellInfo(getOpPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return list;
    }

    public void setCellInfoListRate(int rateInMillis) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.setCellInfoListRate(rateInMillis);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
    }

    public String getMmsUserAgent() {
        String sUserAgent = "SAMSUNG-ANDROID-MMS/uaprof";
        String mmsUa = getValuefromCSC("/system/csc/customer.xml", "MessageUserAgent");
        if (mmsUa != null) {
            sUserAgent = mmsUa;
        }
        CscFeature cscFeature = CscFeature.getInstance();
        String cscUa = cscFeature.getString("CscFeature_Message_UserAgent");
        if (!(cscUa == null || cscUa.length() == 0)) {
            sUserAgent = cscUa;
        }
        if (cscFeature.getEnableStatus("CscFeature_RIL_MmsUapBuildid", false)) {
            String sellerID = SystemProperties.get("ro.cdma.home.operator.alpha");
            String version = SystemProperties.get("ro.build.version.incremental");
            String model = Build.MODEL;
            if (sUserAgent.equalsIgnoreCase("USAATTUserAgent")) {
                sUserAgent = model + "/" + version + " Mozilla/5.0 SMM-MMS/1.2.0";
            } else {
                if (sellerID.equalsIgnoreCase("Sprint")) {
                    sellerID = "SPRINT";
                    sUserAgent = "SAMSUNG_" + Build.MODEL + sUserAgent;
                } else {
                    String sellerPrefix = sellerID;
                    if (sellerID.equalsIgnoreCase("Virgin") || sellerID.equalsIgnoreCase("Virgin Mobile")) {
                        sellerPrefix = "VMUB";
                        sellerID = "VIRGIN";
                    } else if (sellerID.equalsIgnoreCase("Boost") || sellerID.equalsIgnoreCase("Boost Mobile")) {
                        sellerPrefix = "BST";
                        sellerID = "BOOST";
                    } else if (sellerID.equalsIgnoreCase("Samsung")) {
                        sellerPrefix = "Wholesale";
                        sellerID = "MVNO";
                    }
                    sUserAgent = Build.MODEL + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + sellerPrefix;
                }
                if (mConfigNetworkTypeCapability != null && mConfigNetworkTypeCapability.startsWith("SPR-")) {
                    sUserAgent = Build.MODEL + " " + sellerID.toUpperCase();
                }
            }
        }
        if (sUserAgent != null) {
            return sUserAgent;
        }
        if (this.mContext == null) {
            return null;
        }
        return this.mContext.getResources().getString(R.string.config_mms_user_agent);
    }

    public String getMmsUAProfUrl() {
        String sUaProfUrl = "http://wap.samsungmobile.com/uaprof/uaprof.rdf";
        CscFeature cscFeature = CscFeature.getInstance();
        String mmsUap_url = getValuefromCSC("/system/csc/customer.xml", "MessageUaProfUrl");
        if (mmsUap_url != null) {
            sUaProfUrl = mmsUap_url;
        }
        String cscUapUrl = cscFeature.getString("CscFeature_Message_UaProfUrl");
        if (!(cscUapUrl == null || cscUapUrl.length() == 0)) {
            sUaProfUrl = cscUapUrl;
        }
        if (cscFeature.getEnableStatus("CscFeature_RIL_MmsUapBuildid", false) && !SystemProperties.get("ro.csc.sales_code", KeyProperties.DIGEST_NONE).equalsIgnoreCase("ATT")) {
            String sellerID = SystemProperties.get("ro.cdma.home.operator.alpha");
            String version = SystemProperties.get("ro.build.version.incremental");
            if (sellerID.equalsIgnoreCase("Sprint")) {
                sellerID = "SPRINT";
            } else if (sellerID.equalsIgnoreCase("Virgin") || sellerID.equalsIgnoreCase("Virgin Mobile")) {
                sellerID = "VIRGIN";
            } else if (sellerID.equalsIgnoreCase("Boost") || sellerID.equalsIgnoreCase("Boost Mobile")) {
                sellerID = "BOOST";
            } else if (sellerID.equalsIgnoreCase("Samsung")) {
                sellerID = "MVNO";
            }
            if (version.length() > 3) {
                version = version.substring(version.length() - 3);
            }
            if (mConfigNetworkTypeCapability == null || !mConfigNetworkTypeCapability.startsWith("SPR-")) {
                sUaProfUrl = sUaProfUrl + Build.MODEL + "/" + version + ".rdf";
            } else {
                sUaProfUrl = sUaProfUrl + Build.MODEL + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + sellerID.toUpperCase() + "/" + version + ".rdf";
            }
        }
        if (sUaProfUrl != null) {
            return sUaProfUrl;
        }
        if (this.mContext == null) {
            return null;
        }
        return this.mContext.getResources().getString(R.string.config_mms_user_agent_profile_url);
    }

    public IccOpenLogicalChannelResponse iccOpenLogicalChannel(String AID) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccOpenLogicalChannel(AID);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return null;
    }

    public IccOpenLogicalChannelResponse iccOpenLogicalChannel(String AID, int slotId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccOpenLogicalChannelUsingSlotId(AID, slotId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return null;
    }

    public boolean iccCloseLogicalChannel(int channel) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccCloseLogicalChannel(channel);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return false;
    }

    public boolean iccCloseLogicalChannel(int channel, int slotId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccCloseLogicalChannelUsingSlotId(channel, slotId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return false;
    }

    public String iccTransmitApduLogicalChannel(int channel, int cla, int instruction, int p1, int p2, int p3, String data) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccTransmitApduLogicalChannel(channel, cla, instruction, p1, p2, p3, data);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return "";
    }

    public String iccTransmitApduLogicalChannel(int channel, int cla, int instruction, int p1, int p2, int p3, String data, int slotId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccTransmitApduLogicalChannelUsingSlotId(channel, cla, instruction, p1, p2, p3, data, slotId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return "";
    }

    public String iccTransmitApduBasicChannel(int cla, int instruction, int p1, int p2, int p3, String data) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccTransmitApduBasicChannel(cla, instruction, p1, p2, p3, data);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return "";
    }

    public String iccTransmitApduBasicChannel(int cla, int instruction, int p1, int p2, int p3, String data, int slotId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccTransmitApduBasicChannelUsingSlotId(cla, instruction, p1, p2, p3, data, slotId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return "";
    }

    public byte[] iccExchangeSimIO(int fileID, int command, int p1, int p2, int p3, String filePath) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccExchangeSimIO(fileID, command, p1, p2, p3, filePath);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return null;
    }

    public byte[] iccExchangeSimIO(int fileID, int command, int p1, int p2, int p3, String filePath, int slotId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccExchangeSimIOUsingSlotId(fileID, command, p1, p2, p3, filePath, slotId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return null;
    }

    public String sendEnvelopeWithStatus(String content) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.sendEnvelopeWithStatus(content);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return "";
    }

    public String nvReadItem(int itemID) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.nvReadItem(itemID);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "nvReadItem RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "nvReadItem NPE", ex2);
        }
        return "";
    }

    public boolean nvWriteItem(int itemID, String itemValue) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.nvWriteItem(itemID, itemValue);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "nvWriteItem RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "nvWriteItem NPE", ex2);
        }
        return false;
    }

    public boolean nvWriteCdmaPrl(byte[] preferredRoamingList) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.nvWriteCdmaPrl(preferredRoamingList);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "nvWriteCdmaPrl RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "nvWriteCdmaPrl NPE", ex2);
        }
        return false;
    }

    public boolean nvResetConfig(int resetType) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.nvResetConfig(resetType);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "nvResetConfig RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "nvResetConfig NPE", ex2);
        }
        return false;
    }

    private static int getDefaultSubscription() {
        return SubscriptionManager.getDefaultSubId();
    }

    private static int getDefaultPhone() {
        return SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultSubId());
    }

    public int getDefaultSim() {
        return SubscriptionManager.getSlotId(SubscriptionManager.getDefaultSubId());
    }

    public static void setTelephonyProperty(int phoneId, String property, String value) {
        String propVal = "";
        String[] p = null;
        String prop = SystemProperties.get(property);
        if (value == null) {
            value = "";
        }
        if (prop != null) {
            p = prop.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
        }
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            int i = 0;
            while (i < phoneId) {
                String str = "";
                if (p != null && i < p.length) {
                    str = p[i];
                }
                propVal = propVal + str + FingerprintManager.FINGER_PERMISSION_DELIMITER;
                i++;
            }
            propVal = propVal + value;
            if (p != null) {
                for (i = phoneId + 1; i < p.length; i++) {
                    propVal = propVal + FingerprintManager.FINGER_PERMISSION_DELIMITER + p[i];
                }
            }
            if (property.length() > 31 || propVal.length() > 91) {
                Rlog.d(TAG, "setTelephonyProperty: property to long phoneId=" + phoneId + " property=" + property + " value: " + value + " propVal=" + propVal);
                return;
            }
            Rlog.d(TAG, "setTelephonyProperty: success phoneId=" + phoneId + " property=" + property + " value: " + value + " propVal=" + propVal);
            SystemProperties.set(property, propVal);
            return;
        }
        Rlog.d(TAG, "setTelephonyProperty: invalid phoneId=" + phoneId + " property=" + property + " value: " + value + " prop=" + prop);
    }

    public static int getIntAtIndex(ContentResolver cr, String name, int index) throws SettingNotFoundException {
        String v = Global.getString(cr, name);
        if (v != null) {
            String[] valArray = v.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
            if (index >= 0 && index < valArray.length && valArray[index] != null) {
                try {
                    return Integer.parseInt(valArray[index]);
                } catch (NumberFormatException e) {
                }
            }
        }
        throw new SettingNotFoundException(name);
    }

    public static boolean putIntAtIndex(ContentResolver cr, String name, int index, int value) {
        String data = "";
        String[] valArray = null;
        String v = Global.getString(cr, name);
        if (index == Integer.MAX_VALUE) {
            throw new RuntimeException("putIntAtIndex index == MAX_VALUE index=" + index);
        } else if (index < 0) {
            throw new RuntimeException("putIntAtIndex index < 0 index=" + index);
        } else {
            if (v != null) {
                valArray = v.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
            }
            int i = 0;
            while (i < index) {
                String str = "";
                if (valArray != null && i < valArray.length) {
                    str = valArray[i];
                }
                data = data + str + FingerprintManager.FINGER_PERMISSION_DELIMITER;
                i++;
            }
            data = data + value;
            if (valArray != null) {
                for (i = index + 1; i < valArray.length; i++) {
                    data = data + FingerprintManager.FINGER_PERMISSION_DELIMITER + valArray[i];
                }
            }
            return Global.putString(cr, name, data);
        }
    }

    public static String getTelephonyProperty(int phoneId, String property, String defaultVal) {
        String propVal = null;
        String prop = SystemProperties.get(property);
        if (prop != null && prop.length() > 0) {
            String[] values = prop.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
            if (phoneId >= 0 && phoneId < values.length && values[phoneId] != null) {
                propVal = values[phoneId];
            }
        }
        return propVal == null ? defaultVal : propVal;
    }

    public int getSimCount() {
        if (isMultiSimEnabled()) {
            return 2;
        }
        if (getDefault().getPhoneCount() <= 1) {
            return 1;
        }
        Rlog.d(TAG, "getSimCount(): persist.radio.multisim.config:" + SystemProperties.get(TelephonyProperties.PROPERTY_MULTI_SIM_CONFIG, "NULL"));
        return 1;
    }

    public int getSimTrayCount() {
        try {
            return (SystemProperties.getInt("ro.revision", 0) < 8 || SystemProperties.getInt("ro.multisim.simslotcount", 1) > 1) ? 1 : 1;
        } catch (NumberFormatException e) {
            Rlog.e(TAG, "getSimTrayCount() Exception " + 0);
        }
    }

    public String getIsimIst() {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getIsimIst();
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String[] getIsimPcscf() {
        String[] strArr = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                strArr = info.getIsimPcscf();
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return strArr;
    }

    public String getIsimChallengeResponse(String nonce) {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getIsimChallengeResponse(nonce);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getIsimChallengeResponse(int subId, String nonce) {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getIsimChallengeResponseForSubscriber(subId, nonce);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getIccSimChallengeResponse(int subId, int appType, String data) {
        String str = null;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                str = info.getIccSimChallengeResponse(subId, appType, data);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getIccSimChallengeResponse(int appType, String data) {
        return getIccSimChallengeResponse(getDefaultSubscription(), appType, data);
    }

    public String[] getPcscfAddress(String apnType) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return new String[0];
            }
            return telephony.getPcscfAddress(apnType, getOpPackageName());
        } catch (RemoteException e) {
            return new String[0];
        }
    }

    public void setImsRegistrationState(boolean registered) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.setImsRegistrationState(registered);
            }
        } catch (RemoteException e) {
        }
    }

    public int getPreferredNetworkType(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getPreferredNetworkType(subId);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "getPreferredNetworkType RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "getPreferredNetworkType NPE", ex2);
        }
        return -1;
    }

    public void setNetworkSelectionModeAutomatic(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.setNetworkSelectionModeAutomatic(subId);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setNetworkSelectionModeAutomatic RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "setNetworkSelectionModeAutomatic NPE", ex2);
        }
    }

    public CellNetworkScanResult getCellNetworkScanResults(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getCellNetworkScanResults(subId);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "getCellNetworkScanResults RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "getCellNetworkScanResults NPE", ex2);
        }
        return null;
    }

    public boolean setNetworkSelectionModeManual(int subId, OperatorInfo operator, boolean persistSelection) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setNetworkSelectionModeManual(subId, operator, persistSelection);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setNetworkSelectionModeManual RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "setNetworkSelectionModeManual NPE", ex2);
        }
        return false;
    }

    public boolean setPreferredNetworkType(int subId, int networkType) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setPreferredNetworkType(subId, networkType);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setPreferredNetworkType RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "setPreferredNetworkType NPE", ex2);
        }
        return false;
    }

    public boolean setPreferredNetworkTypeToGlobal() {
        return setPreferredNetworkType(getDefaultSubscription(), 10);
    }

    public int getTetherApnRequired() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getTetherApnRequired();
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "hasMatchedTetherApnSetting RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "hasMatchedTetherApnSetting NPE", ex2);
        }
        return 2;
    }

    public boolean hasCarrierPrivileges() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                if (telephony.getCarrierPrivilegeStatus() == 1) {
                    return true;
                }
                return false;
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "hasCarrierPrivileges RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "hasCarrierPrivileges NPE", ex2);
        }
        return false;
    }

    public boolean setOperatorBrandOverride(String brand) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setOperatorBrandOverride(brand);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setOperatorBrandOverride RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "setOperatorBrandOverride NPE", ex2);
        }
        return false;
    }

    public boolean setRoamingOverride(List<String> gsmRoamingList, List<String> gsmNonRoamingList, List<String> cdmaRoamingList, List<String> cdmaNonRoamingList) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setRoamingOverride(gsmRoamingList, gsmNonRoamingList, cdmaRoamingList, cdmaNonRoamingList);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setRoamingOverride RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "setRoamingOverride NPE", ex2);
        }
        return false;
    }

    public String getCdmaMdn() {
        return getCdmaMdn(getDefaultSubscription());
    }

    public String getCdmaMdn(int subId) {
        String str = null;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                str = telephony.getCdmaMdn(subId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getCdmaMin() {
        return getCdmaMin(getDefaultSubscription());
    }

    public String getCdmaMin(int subId) {
        String str = null;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                str = telephony.getCdmaMin(subId);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public int checkCarrierPrivilegesForPackage(String pkgName) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.checkCarrierPrivilegesForPackage(pkgName);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "checkCarrierPrivilegesForPackage RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "checkCarrierPrivilegesForPackage NPE", ex2);
        }
        return 0;
    }

    public int checkCarrierPrivilegesForPackageAnyPhone(String pkgName) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.checkCarrierPrivilegesForPackageAnyPhone(pkgName);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "checkCarrierPrivilegesForPackageAnyPhone RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "checkCarrierPrivilegesForPackageAnyPhone NPE", ex2);
        }
        return 0;
    }

    public List<String> getCarrierPackageNamesForIntent(Intent intent) {
        return getCarrierPackageNamesForIntentAndPhone(intent, getDefaultPhone());
    }

    public List<String> getCarrierPackageNamesForIntentAndPhone(Intent intent, int phoneId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getCarrierPackageNamesForIntentAndPhone(intent, phoneId);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "getCarrierPackageNamesForIntentAndPhone RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "getCarrierPackageNamesForIntentAndPhone NPE", ex2);
        }
        return null;
    }

    public void dial(String number) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.dial(number);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#dial", e);
        }
    }

    public void dial(int subId, String number) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.dialForSubscriber(subId, number);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#dial", e);
        }
    }

    public void call(String callingPackage, String number) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.call(callingPackage, number);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#call", e);
        }
    }

    public void call(int subId, String callingPackage, String number) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.callForSubscriber(subId, callingPackage, number);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#call", e);
        }
    }

    public boolean endCall() {
        if (!SHIP_BUILD) {
            Rlog.dumpCallStack(TAG, "endCall", 2);
        }
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.endCall();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#endCall", e);
        }
        return false;
    }

    public boolean endCall(int subId) {
        if (!SHIP_BUILD) {
            Rlog.dumpCallStack(TAG, "endCall(subId)", 2);
        }
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.endCallForSubscriber(subId);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#endCall", e);
        }
        return false;
    }

    public void answerRingingCall() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.answerRingingCall();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#answerRingingCall", e);
        }
    }

    public void answerRingingCall(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.answerRingingCallForSubscriber(subId);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#answerRingingCall", e);
        }
    }

    public void silenceRinger() {
        try {
            getTelecomService().silenceRinger(getOpPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelecomService#silenceRinger", e);
        }
    }

    public boolean isOffhook() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isOffhook(getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isOffhook", e);
        }
        return false;
    }

    public boolean isOffhook(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isOffhookForSubscriber(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isOffhook", e);
        }
        return false;
    }

    public boolean isRinging() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isRinging(getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isRinging", e);
        }
        return false;
    }

    public boolean isRinging(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isRingingForSubscriber(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isRinging", e);
        }
        return false;
    }

    public boolean isIdle() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isIdle(getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isIdle", e);
        }
        return true;
    }

    public boolean isIdle(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isIdleForSubscriber(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isIdle", e);
        }
        return true;
    }

    public boolean isRadioOn() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isRadioOn(getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isRadioOn", e);
        }
        return false;
    }

    public boolean isRadioOn(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isRadioOnForSubscriber(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isRadioOn", e);
        }
        return false;
    }

    public boolean isSimPinEnabled() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isSimPinEnabled(getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isSimPinEnabled", e);
        }
        return false;
    }

    public boolean supplyPin(String pin) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.supplyPin(pin);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#supplyPin", e);
        }
        return false;
    }

    public boolean supplyPin(int subId, String pin) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.supplyPinForSubscriber(subId, pin);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#supplyPin", e);
        }
        return false;
    }

    public boolean supplyPuk(String puk, String pin) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.supplyPuk(puk, pin);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#supplyPuk", e);
        }
        return false;
    }

    public boolean supplyPuk(int subId, String puk, String pin) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.supplyPukForSubscriber(subId, puk, pin);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#supplyPuk", e);
        }
        return false;
    }

    public int[] supplyPinReportResult(String pin) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.supplyPinReportResult(pin);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#supplyPinReportResult", e);
        }
        return new int[0];
    }

    public int[] supplyPinReportResult(int subId, String pin) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.supplyPinReportResultForSubscriber(subId, pin);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#supplyPinReportResult", e);
        }
        return new int[0];
    }

    public int[] supplyPukReportResult(String puk, String pin) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.supplyPukReportResult(puk, pin);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#]", e);
        }
        return new int[0];
    }

    public int[] supplyPukReportResult(int subId, String puk, String pin) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.supplyPukReportResultForSubscriber(subId, puk, pin);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#]", e);
        }
        return new int[0];
    }

    public boolean handlePinMmi(String dialString) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.handlePinMmi(dialString);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#handlePinMmi", e);
        }
        return false;
    }

    public boolean handlePinMmiForSubscriber(int subId, String dialString) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.handlePinMmiForSubscriber(subId, dialString);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#handlePinMmi", e);
        }
        return false;
    }

    public void toggleRadioOnOff() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.toggleRadioOnOff();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#toggleRadioOnOff", e);
        }
    }

    public void toggleRadioOnOff(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.toggleRadioOnOffForSubscriber(subId);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#toggleRadioOnOff", e);
        }
    }

    public boolean setRadio(boolean turnOn) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setRadio(turnOn);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#setRadio", e);
        }
        return false;
    }

    public boolean setRadio(int subId, boolean turnOn) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setRadioForSubscriber(subId, turnOn);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#setRadio", e);
        }
        return false;
    }

    public boolean setRadioPower(boolean turnOn) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setRadioPower(turnOn);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#setRadioPower", e);
        }
        return false;
    }

    public void updateServiceLocation() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.updateServiceLocation();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#updateServiceLocation", e);
        }
    }

    public boolean enableDataConnectivity() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.enableDataConnectivity();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#enableDataConnectivity", e);
        }
        return false;
    }

    public boolean disableDataConnectivity() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.disableDataConnectivity();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#disableDataConnectivity", e);
        }
        return false;
    }

    public boolean isDataConnectivityPossible() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isDataConnectivityPossible();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isDataConnectivityPossible", e);
        }
        return false;
    }

    public boolean needsOtaServiceProvisioning() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.needsOtaServiceProvisioning();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#needsOtaServiceProvisioning", e);
        }
        return false;
    }

    public void setDataEnabled(boolean enable) {
        setDataEnabled(SubscriptionManager.getDefaultDataSubId(), enable);
    }

    public void setDataEnabled(int subId, boolean enable) {
        try {
            Log.d(TAG, "setDataEnabled: enabled=" + enable + " CallingPid=" + Binder.getCallingPid());
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.setDataEnabled(subId, enable);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#setDataEnabled", e);
        }
    }

    public boolean getDataEnabled() {
        return getDataEnabled(SubscriptionManager.getDefaultDataSubId());
    }

    public boolean getDataEnabled(int subId) {
        boolean retVal = false;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                retVal = telephony.getDataEnabled(subId);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#getDataEnabled", e);
        } catch (NullPointerException e2) {
        }
        Log.d(TAG, "getDataEnabled: retVal=" + retVal);
        return retVal;
    }

    public static String appendId(String text, long id) {
        if (getDefault().getPhoneCount() < 2) {
            return text;
        }
        StringBuilder str = new StringBuilder(text);
        if (id < 0 || id >= ((long) getDefault().getPhoneCount())) {
            Log.e(TAG, "Id is error (text : " + text + ", id : " + id + ")");
            try {
                throw new Exception("appendId() exception");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (id != 0) {
                str.append(id);
            }
            return str.toString();
        }
    }

    public int invokeOemRilRequestRaw(byte[] oemReq, byte[] oemResp) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.invokeOemRilRequestRaw(oemReq, oemResp);
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return -1;
    }

    public void enableVideoCalling(boolean enable) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.enableVideoCalling(enable);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#enableVideoCalling", e);
        }
    }

    public boolean isVideoCallingEnabled() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isVideoCallingEnabled(getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isVideoCallingEnabled", e);
        }
        return false;
    }

    public boolean canChangeDtmfToneLength() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.canChangeDtmfToneLength();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#canChangeDtmfToneLength", e);
        } catch (SecurityException e2) {
            Log.e(TAG, "Permission error calling ITelephony#canChangeDtmfToneLength", e2);
        }
        return false;
    }

    public boolean isWorldPhone() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isWorldPhone();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isWorldPhone", e);
        } catch (SecurityException e2) {
            Log.e(TAG, "Permission error calling ITelephony#isWorldPhone", e2);
        }
        return false;
    }

    public boolean isTtyModeSupported() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isTtyModeSupported();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isTtyModeSupported", e);
        } catch (SecurityException e2) {
            Log.e(TAG, "Permission error calling ITelephony#isTtyModeSupported", e2);
        }
        return false;
    }

    public boolean isHearingAidCompatibilitySupported() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isHearingAidCompatibilitySupported();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isHearingAidCompatibilitySupported", e);
        } catch (SecurityException e2) {
            Log.e(TAG, "Permission error calling ITelephony#isHearingAidCompatibilitySupported", e2);
        }
        return false;
    }

    public static int getIntWithSubId(ContentResolver cr, String name, int subId) throws SettingNotFoundException {
        int i;
        try {
            i = Global.getInt(cr, name + subId);
        } catch (SettingNotFoundException e) {
            try {
                i = Global.getInt(cr, name);
                Global.putInt(cr, name + subId, i);
                int default_val = i;
                if (name.equals("mobile_data")) {
                    if (SmartFaceManager.TRUE.equalsIgnoreCase(SystemProperties.get("ro.com.android.mobiledata", SmartFaceManager.TRUE))) {
                        default_val = 1;
                    } else {
                        default_val = 0;
                    }
                } else if (name.equals("data_roaming")) {
                    default_val = SmartFaceManager.TRUE.equalsIgnoreCase(SystemProperties.get("ro.com.android.dataroaming", SmartFaceManager.FALSE)) ? 1 : 0;
                }
                if (default_val != i) {
                    Global.putInt(cr, name, default_val);
                }
            } catch (SettingNotFoundException e2) {
                throw new SettingNotFoundException(name);
            }
        }
        return i;
    }

    public boolean isImsRegistered() {
        boolean z = false;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                z = telephony.isImsRegistered();
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    public boolean isVolteAvailable() {
        boolean z = false;
        try {
            z = getITelephony().isVolteAvailable();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    public boolean isVideoTelephonyAvailable() {
        boolean z = false;
        try {
            z = getITelephony().isVideoTelephonyAvailable();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    public boolean isWifiCallingAvailable() {
        boolean z = false;
        try {
            z = getITelephony().isWifiCallingAvailable();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    public void setSimOperatorNumeric(String numeric) {
        setSimOperatorNumericForPhone(getDefaultPhone(), numeric);
    }

    public void setSimOperatorNumericForPhone(int phoneId, String numeric) {
        setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC, numeric);
    }

    public void setSimOperatorName(String name) {
        setSimOperatorNameForPhone(getDefaultPhone(), name);
    }

    public void setSimOperatorNameForPhone(int phoneId, String name) {
        setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_ICC_OPERATOR_ALPHA, name);
    }

    public void setSimCountryIso(String iso) {
        setSimCountryIsoForPhone(getDefaultPhone(), iso);
    }

    public void setSimCountryIsoForPhone(int phoneId, String iso) {
        setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_ICC_OPERATOR_ISO_COUNTRY, iso);
    }

    public void setSimState(String state) {
        setSimStateForPhone(getDefaultPhone(), state);
    }

    public void setSimStateForPhone(int phoneId, String state) {
        setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_SIM_STATE, state);
    }

    public void setBasebandVersion(String version) {
        setBasebandVersionForPhone(getDefaultPhone(), version);
    }

    public void setBasebandVersionForPhone(int phoneId, String version) {
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            SystemProperties.set(TelephonyProperties.PROPERTY_BASEBAND_VERSION + (phoneId == 0 ? "" : Integer.toString(phoneId)), version);
        }
    }

    public void setPhoneType(int type) {
        setPhoneType(getDefaultPhone(), type);
    }

    public void setPhoneType(int phoneId, int type) {
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            setTelephonyProperty(phoneId, TelephonyProperties.CURRENT_ACTIVE_PHONE, String.valueOf(type));
        }
    }

    public String getOtaSpNumberSchema(String defaultValue) {
        return getOtaSpNumberSchemaForPhone(getDefaultPhone(), defaultValue);
    }

    public String getOtaSpNumberSchemaForPhone(int phoneId, String defaultValue) {
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            return getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OTASP_NUM_SCHEMA, defaultValue);
        }
        return defaultValue;
    }

    public boolean getSmsReceiveCapable(boolean defaultValue) {
        return getSmsReceiveCapableForPhone(getDefaultPhone(), defaultValue);
    }

    public boolean getSmsReceiveCapableForPhone(int phoneId, boolean defaultValue) {
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            return Boolean.valueOf(getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_SMS_RECEIVE, String.valueOf(defaultValue))).booleanValue();
        }
        return defaultValue;
    }

    public boolean getSmsSendCapable(boolean defaultValue) {
        return getSmsSendCapableForPhone(getDefaultPhone(), defaultValue);
    }

    public boolean getSmsSendCapableForPhone(int phoneId, boolean defaultValue) {
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            return Boolean.valueOf(getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_SMS_SEND, String.valueOf(defaultValue))).booleanValue();
        }
        return defaultValue;
    }

    public void setNetworkOperatorName(String name) {
        setNetworkOperatorNameForPhone(getDefaultPhone(), name);
    }

    public void setNetworkOperatorNameForPhone(int phoneId, String name) {
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OPERATOR_ALPHA, name);
        }
    }

    public void setNetworkOperatorNumeric(String numeric) {
        setNetworkOperatorNumericForPhone(getDefaultPhone(), numeric);
    }

    public void setNetworkOperatorNumericForPhone(int phoneId, String numeric) {
        setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OPERATOR_NUMERIC, numeric);
    }

    public void setNetworkRoaming(boolean isRoaming) {
        setNetworkRoamingForPhone(getDefaultPhone(), isRoaming);
    }

    public void setNetworkRoamingForPhone(int phoneId, boolean isRoaming) {
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OPERATOR_ISROAMING, isRoaming ? SmartFaceManager.TRUE : SmartFaceManager.FALSE);
        }
    }

    public void setNetworkCountryIso(String iso) {
        setNetworkCountryIsoForPhone(getDefaultPhone(), iso);
    }

    public void setNetworkCountryIsoForPhone(int phoneId, String iso) {
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OPERATOR_ISO_COUNTRY, iso);
        }
    }

    public void setDataNetworkType(int type) {
        setDataNetworkTypeForPhone(getDefaultPhone(), type);
    }

    public void setDataNetworkTypeForPhone(int phoneId, int type) {
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_DATA_NETWORK_TYPE, ServiceState.rilRadioTechnologyToString(type));
        }
    }

    public int getSubIdForPhoneAccount(PhoneAccount phoneAccount) {
        int retval = -1;
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                retval = service.getSubIdForPhoneAccount(phoneAccount);
            }
        } catch (RemoteException e) {
        }
        return retval;
    }

    public void factoryReset(int subId) {
        try {
            Log.d(TAG, "factoryReset: subId=" + subId);
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.factoryReset(subId);
            }
        } catch (RemoteException e) {
        }
    }

    public String getLocaleFromDefaultSim() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getLocaleFromDefaultSim();
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    public ModemActivityInfo getModemActivityInfo() {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getModemActivityInfo();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#getModemActivityInfo", e);
        }
        return null;
    }

    public String[] getSponImsi() {
        String[] strArr = null;
        try {
            strArr = getSubscriberInfo().getSponImsi();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return strArr;
    }

    public boolean isVolteRegistered() {
        boolean z = false;
        try {
            z = getSubscriberInfo().isVolteRegistered();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    public boolean isWfcRegistered() {
        boolean z = false;
        try {
            z = getSubscriberInfo().isWfcRegistered();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    public int getImsRegisteredFeature() {
        int i = 0;
        try {
            i = getSubscriberInfo().getImsRegisteredFeature();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    public boolean hasCall(String callType) {
        boolean z = false;
        try {
            z = getSubscriberInfo().hasCall(callType);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    public boolean getDataRoamingEnabled() {
        boolean z = false;
        try {
            z = getITelephony().getDataRoamingEnabled();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    public boolean getDataRoamingEnabled(int lSubId) {
        boolean z = false;
        try {
            z = getITelephony().getDataRoamingEnabledUsingSubID(lSubId);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    public void setDataRoamingEnabled(boolean enable) {
        try {
            getITelephony().setDataRoamingEnabled(enable);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
    }

    public String getSelectedApn() {
        return null;
    }

    public void setSelectedApn() {
    }

    public int getServiceState() {
        int state = 0;
        try {
            state = getITelephony().getServiceState();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return state;
    }

    public int getServiceState(int subId) {
        int state = 0;
        try {
            state = getITelephony().getServiceStateUsingSubId(subId);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return state;
    }

    public int getDataServiceState() {
        int state = 0;
        try {
            state = getITelephony().getDataServiceState();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return state;
    }

    public int getDataServiceState(int subId) {
        int state = 0;
        try {
            state = getITelephony().getDataServiceStateUsingSubId(subId);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return state;
    }

    public int getDataServiceState(long subId) {
        return getDataServiceState((int) subId);
    }

    public static boolean IsCDMAmessage() {
        return isCDMAMessage;
    }

    public static boolean isSelectTelecomDF() {
        return isSelecttelecomDF;
    }

    public boolean isSimFDNEnabled() {
        return isSimFDNEnabledForSubscriber(getDefaultSubscription());
    }

    public boolean isSimFDNEnabledForSubscriber(int subId) {
        try {
            getITelephony().isSimFDNEnabledForSubscriber(subId);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return false;
    }

    private static String getRoamingUserAgent(String ua, String mdn) {
        if (!SystemProperties.get("ril.currentplmn", "domestic").equals("oversea")) {
            return ua;
        }
        if (mdn.length() == 11) {
            return String.format("I%c%c%s", new Object[]{Character.valueOf(mdn.charAt(1)), Character.valueOf(mdn.charAt(2)), ua.substring(3)});
        }
        return String.format("I%c%c%s", new Object[]{Character.valueOf(mdn.charAt(0)), Character.valueOf(mdn.charAt(1)), ua.substring(3)});
    }

    private static boolean isWIFIConnected() {
        String wifiConnected = SystemProperties.get("gsm.wifiConnected.active");
        if (TextUtils.isEmpty(wifiConnected) || !SmartFaceManager.TRUE.equals(wifiConnected)) {
            return false;
        }
        return true;
    }

    private static char getServiceUserAgent() {
        if (isWIFIConnected()) {
            return 'D';
        }
        switch (getDefault().getNetworkType()) {
            case 1:
                return 'B';
            case 2:
                return 'C';
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 12:
                return '6';
            case 8:
                return '8';
            case 9:
                return '9';
            case 13:
                return 'F';
            default:
                return '6';
        }
    }

    private String getSktImsiM() {
        String str = null;
        try {
            str = getSubscriberInfo().getSktImsiM();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    private String getSktIrm() {
        String str = null;
        try {
            str = getSubscriberInfo().getSktIrm();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public static String getUAField() {
        Exception e;
        boolean is_roaming;
        String numPrefix;
        String mnc;
        String mcc;
        Throwable th;
        String UserAgent = "";
        String min8 = "";
        String mdn = "";
        StringBuffer sbDeviceInfo = null;
        FileReader fileReader = null;
        BufferedReader reader = null;
        char networkType;
        String networkOperator;
        GsmCellLocation cell;
        int cid;
        byte nodeB;
        byte rnc;
        byte msd;
        String networkInfo;
        try {
            BufferedReader bufferedReader;
            FileReader fileReader2 = new FileReader(new File("/system/skt/ua/uafield.dat"));
            try {
                bufferedReader = new BufferedReader(fileReader2);
            } catch (Exception e2) {
                e = e2;
                fileReader = fileReader2;
                try {
                    e.printStackTrace();
                    try {
                        reader.close();
                        fileReader.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (sbDeviceInfo == null) {
                        return null;
                    }
                    is_roaming = "oversea".equals(SystemProperties.get("ril.currentplmn"));
                    networkType = getServiceUserAgent();
                    sbDeviceInfo.setCharAt(3, networkType);
                    networkOperator = SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_NUMERIC);
                    cell = (GsmCellLocation) getDefault().getCellLocation();
                    if (cell == null) {
                        try {
                            cid = cell.getCid();
                            nodeB = (byte) ((65280 & cid) >> 8);
                            rnc = (byte) ((cid >> 16) & 31);
                            msd = (byte) ((cid >> 21) & 127);
                            if (is_roaming) {
                                mdn = getDefault().getSktImsiM();
                            } else {
                                mdn = getDefault().getSktIrm();
                            }
                            numPrefix = PREFIX_TABLE[mdn.charAt(2) - 48];
                            if (mdn.length() == 11) {
                                min8 = mdn.substring(mdn.length() - 8, mdn.length());
                            } else if (mdn.length() != 10) {
                                min8 = mdn.substring(mdn.length() - 8, mdn.length());
                            } else if (is_roaming) {
                                min8 = SmartFaceManager.PAGE_MIDDLE + mdn.substring(mdn.length() - 7, mdn.length());
                            } else {
                                min8 = mdn.substring(mdn.length() - 8, mdn.length());
                            }
                            mnc = networkOperator.substring(3);
                            mcc = SmartFaceManager.PAGE_MIDDLE + networkOperator.substring(0, 3);
                            if (networkType == 'F') {
                                networkInfo = ";" + ((nodeB / 10) + "" + (nodeB % 10)) + ";" + rnc + ";" + msd + ";" + mnc + ";" + mcc;
                            } else {
                                networkInfo = ";ECI;" + mnc + ";" + mcc;
                            }
                            UserAgent = sbDeviceInfo.append(min8).append(networkInfo).toString();
                            if (numPrefix != null) {
                                UserAgent = numPrefix + UserAgent.substring(3, UserAgent.length());
                            }
                            UserAgent = getRoamingUserAgent(UserAgent, mdn);
                            Rlog.i(TAG, "getUAField():" + UserAgent);
                            return UserAgent;
                        } catch (Exception e3) {
                            Rlog.e(TAG, "No Sim or No MSISDN -" + e3);
                            e3.printStackTrace();
                            return null;
                        }
                    }
                    Rlog.e(TAG, "No Sim or Flight mode");
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        reader.close();
                        fileReader.close();
                    } catch (Exception ex2) {
                        ex2.printStackTrace();
                    }
                    if (sbDeviceInfo != null) {
                        return null;
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fileReader = fileReader2;
                reader.close();
                fileReader.close();
                if (sbDeviceInfo != null) {
                    return null;
                }
                throw th;
            }
            try {
                StringBuffer sbDeviceInfo2 = new StringBuffer(bufferedReader.readLine());
                try {
                    Rlog.e(TAG, "ua -" + sbDeviceInfo2);
                    bufferedReader.close();
                    try {
                        bufferedReader.close();
                        fileReader2.close();
                    } catch (Exception ex22) {
                        ex22.printStackTrace();
                    }
                    if (sbDeviceInfo2 == null) {
                        reader = bufferedReader;
                        fileReader = fileReader2;
                        sbDeviceInfo = sbDeviceInfo2;
                        return null;
                    }
                    reader = bufferedReader;
                    fileReader = fileReader2;
                    sbDeviceInfo = sbDeviceInfo2;
                    is_roaming = "oversea".equals(SystemProperties.get("ril.currentplmn"));
                    networkType = getServiceUserAgent();
                    sbDeviceInfo.setCharAt(3, networkType);
                    networkOperator = SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_NUMERIC);
                    cell = (GsmCellLocation) getDefault().getCellLocation();
                    if (cell == null) {
                        Rlog.e(TAG, "No Sim or Flight mode");
                        return null;
                    }
                    cid = cell.getCid();
                    nodeB = (byte) ((65280 & cid) >> 8);
                    rnc = (byte) ((cid >> 16) & 31);
                    msd = (byte) ((cid >> 21) & 127);
                    if (is_roaming) {
                        mdn = getDefault().getSktImsiM();
                    } else {
                        mdn = getDefault().getSktIrm();
                    }
                    numPrefix = PREFIX_TABLE[mdn.charAt(2) - 48];
                    if (mdn.length() == 11) {
                        min8 = mdn.substring(mdn.length() - 8, mdn.length());
                    } else if (mdn.length() != 10) {
                        min8 = mdn.substring(mdn.length() - 8, mdn.length());
                    } else if (is_roaming) {
                        min8 = SmartFaceManager.PAGE_MIDDLE + mdn.substring(mdn.length() - 7, mdn.length());
                    } else {
                        min8 = mdn.substring(mdn.length() - 8, mdn.length());
                    }
                    mnc = networkOperator.substring(3);
                    mcc = SmartFaceManager.PAGE_MIDDLE + networkOperator.substring(0, 3);
                    if (networkType == 'F') {
                        networkInfo = ";ECI;" + mnc + ";" + mcc;
                    } else {
                        networkInfo = ";" + ((nodeB / 10) + "" + (nodeB % 10)) + ";" + rnc + ";" + msd + ";" + mnc + ";" + mcc;
                    }
                    UserAgent = sbDeviceInfo.append(min8).append(networkInfo).toString();
                    if (numPrefix != null) {
                        UserAgent = numPrefix + UserAgent.substring(3, UserAgent.length());
                    }
                    UserAgent = getRoamingUserAgent(UserAgent, mdn);
                    Rlog.i(TAG, "getUAField():" + UserAgent);
                    return UserAgent;
                } catch (Exception e4) {
                    e3 = e4;
                    reader = bufferedReader;
                    fileReader = fileReader2;
                    sbDeviceInfo = sbDeviceInfo2;
                    e3.printStackTrace();
                    reader.close();
                    fileReader.close();
                    if (sbDeviceInfo == null) {
                        return null;
                    }
                    is_roaming = "oversea".equals(SystemProperties.get("ril.currentplmn"));
                    networkType = getServiceUserAgent();
                    sbDeviceInfo.setCharAt(3, networkType);
                    networkOperator = SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_NUMERIC);
                    cell = (GsmCellLocation) getDefault().getCellLocation();
                    if (cell == null) {
                        cid = cell.getCid();
                        nodeB = (byte) ((65280 & cid) >> 8);
                        rnc = (byte) ((cid >> 16) & 31);
                        msd = (byte) ((cid >> 21) & 127);
                        if (is_roaming) {
                            mdn = getDefault().getSktIrm();
                        } else {
                            mdn = getDefault().getSktImsiM();
                        }
                        numPrefix = PREFIX_TABLE[mdn.charAt(2) - 48];
                        if (mdn.length() == 11) {
                            min8 = mdn.substring(mdn.length() - 8, mdn.length());
                        } else if (mdn.length() != 10) {
                            min8 = mdn.substring(mdn.length() - 8, mdn.length());
                        } else if (is_roaming) {
                            min8 = mdn.substring(mdn.length() - 8, mdn.length());
                        } else {
                            min8 = SmartFaceManager.PAGE_MIDDLE + mdn.substring(mdn.length() - 7, mdn.length());
                        }
                        mnc = networkOperator.substring(3);
                        mcc = SmartFaceManager.PAGE_MIDDLE + networkOperator.substring(0, 3);
                        if (networkType == 'F') {
                            networkInfo = ";" + ((nodeB / 10) + "" + (nodeB % 10)) + ";" + rnc + ";" + msd + ";" + mnc + ";" + mcc;
                        } else {
                            networkInfo = ";ECI;" + mnc + ";" + mcc;
                        }
                        UserAgent = sbDeviceInfo.append(min8).append(networkInfo).toString();
                        if (numPrefix != null) {
                            UserAgent = numPrefix + UserAgent.substring(3, UserAgent.length());
                        }
                        UserAgent = getRoamingUserAgent(UserAgent, mdn);
                        Rlog.i(TAG, "getUAField():" + UserAgent);
                        return UserAgent;
                    }
                    Rlog.e(TAG, "No Sim or Flight mode");
                    return null;
                } catch (Throwable th4) {
                    th = th4;
                    reader = bufferedReader;
                    fileReader = fileReader2;
                    sbDeviceInfo = sbDeviceInfo2;
                    reader.close();
                    fileReader.close();
                    if (sbDeviceInfo != null) {
                        return null;
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e3 = e5;
                reader = bufferedReader;
                fileReader = fileReader2;
                e3.printStackTrace();
                reader.close();
                fileReader.close();
                if (sbDeviceInfo == null) {
                    return null;
                }
                is_roaming = "oversea".equals(SystemProperties.get("ril.currentplmn"));
                networkType = getServiceUserAgent();
                sbDeviceInfo.setCharAt(3, networkType);
                networkOperator = SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_NUMERIC);
                cell = (GsmCellLocation) getDefault().getCellLocation();
                if (cell == null) {
                    Rlog.e(TAG, "No Sim or Flight mode");
                    return null;
                }
                cid = cell.getCid();
                nodeB = (byte) ((65280 & cid) >> 8);
                rnc = (byte) ((cid >> 16) & 31);
                msd = (byte) ((cid >> 21) & 127);
                if (is_roaming) {
                    mdn = getDefault().getSktImsiM();
                } else {
                    mdn = getDefault().getSktIrm();
                }
                numPrefix = PREFIX_TABLE[mdn.charAt(2) - 48];
                if (mdn.length() == 11) {
                    min8 = mdn.substring(mdn.length() - 8, mdn.length());
                } else if (mdn.length() != 10) {
                    min8 = mdn.substring(mdn.length() - 8, mdn.length());
                } else if (is_roaming) {
                    min8 = SmartFaceManager.PAGE_MIDDLE + mdn.substring(mdn.length() - 7, mdn.length());
                } else {
                    min8 = mdn.substring(mdn.length() - 8, mdn.length());
                }
                mnc = networkOperator.substring(3);
                mcc = SmartFaceManager.PAGE_MIDDLE + networkOperator.substring(0, 3);
                if (networkType == 'F') {
                    networkInfo = ";ECI;" + mnc + ";" + mcc;
                } else {
                    networkInfo = ";" + ((nodeB / 10) + "" + (nodeB % 10)) + ";" + rnc + ";" + msd + ";" + mnc + ";" + mcc;
                }
                UserAgent = sbDeviceInfo.append(min8).append(networkInfo).toString();
                if (numPrefix != null) {
                    UserAgent = numPrefix + UserAgent.substring(3, UserAgent.length());
                }
                UserAgent = getRoamingUserAgent(UserAgent, mdn);
                Rlog.i(TAG, "getUAField():" + UserAgent);
                return UserAgent;
            } catch (Throwable th5) {
                th = th5;
                reader = bufferedReader;
                fileReader = fileReader2;
                reader.close();
                fileReader.close();
                if (sbDeviceInfo != null) {
                    return null;
                }
                throw th;
            }
        } catch (Exception e6) {
            e3 = e6;
            e3.printStackTrace();
            reader.close();
            fileReader.close();
            if (sbDeviceInfo == null) {
                return null;
            }
            is_roaming = "oversea".equals(SystemProperties.get("ril.currentplmn"));
            networkType = getServiceUserAgent();
            sbDeviceInfo.setCharAt(3, networkType);
            networkOperator = SystemProperties.get(TelephonyProperties.PROPERTY_OPERATOR_NUMERIC);
            cell = (GsmCellLocation) getDefault().getCellLocation();
            if (cell == null) {
                Rlog.e(TAG, "No Sim or Flight mode");
                return null;
            }
            cid = cell.getCid();
            nodeB = (byte) ((65280 & cid) >> 8);
            rnc = (byte) ((cid >> 16) & 31);
            msd = (byte) ((cid >> 21) & 127);
            if (is_roaming) {
                mdn = getDefault().getSktImsiM();
            } else {
                mdn = getDefault().getSktIrm();
            }
            numPrefix = PREFIX_TABLE[mdn.charAt(2) - 48];
            if (mdn.length() == 11) {
                min8 = mdn.substring(mdn.length() - 8, mdn.length());
            } else if (mdn.length() != 10) {
                min8 = mdn.substring(mdn.length() - 8, mdn.length());
            } else if (is_roaming) {
                min8 = SmartFaceManager.PAGE_MIDDLE + mdn.substring(mdn.length() - 7, mdn.length());
            } else {
                min8 = mdn.substring(mdn.length() - 8, mdn.length());
            }
            mnc = networkOperator.substring(3);
            mcc = SmartFaceManager.PAGE_MIDDLE + networkOperator.substring(0, 3);
            if (networkType == 'F') {
                networkInfo = ";ECI;" + mnc + ";" + mcc;
            } else {
                networkInfo = ";" + ((nodeB / 10) + "" + (nodeB % 10)) + ";" + rnc + ";" + msd + ";" + mnc + ";" + mcc;
            }
            UserAgent = sbDeviceInfo.append(min8).append(networkInfo).toString();
            if (numPrefix != null) {
                UserAgent = numPrefix + UserAgent.substring(3, UserAgent.length());
            }
            UserAgent = getRoamingUserAgent(UserAgent, mdn);
            Rlog.i(TAG, "getUAField():" + UserAgent);
            return UserAgent;
        }
    }

    public int setNetworkBand(String passcode, String mode, int band) {
        try {
            int ret_val = getITelephony().setNetworkBand(passcode, mode, band);
            Rlog.d(TAG, "setNetworkBand ret_val:" + ret_val);
            return ret_val;
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setNetworkBand RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "setNetworkBand NPE", ex2);
        }
        return -1;
    }

    public int setAirplaneMode(String passcode, boolean mode) {
        try {
            int ret_val = getITelephony().setAirplaneMode(passcode, mode);
            Rlog.d(TAG, "setAirplaneMode ret_val:" + ret_val);
            return ret_val;
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setAirplaneMode RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "setAirplaneMode NPE", ex2);
        }
        return -1;
    }

    public HashMap getMobileQualityInformation() {
        Rlog.d(TAG, "getMobileQualityInformation");
        return getMobileQualityInfo();
    }

    public HashMap getMobileQualityInformation2() {
        Rlog.d(TAG, "getMobileQualityInformation2");
        return getMobileQualityInfo();
    }

    public HashMap getMobileQualityInfo() {
        if (!"SKT".equals("EUR") && !"KTT".equals("EUR")) {
            return null;
        }
        HashMap map = new HashMap();
        try {
            String mMobileInfo = getITelephony().getMobileQualityInformation();
            String[] Values = mMobileInfo.split(";");
            try {
                String mMobileIP = getITelephony().getIpAddressFromLinkProp("MOBILE");
                if (!SHIP_BUILD) {
                    Rlog.d(TAG, "mMobileInfo[" + mMobileInfo + "] length(" + Values.length + ")");
                    Rlog.d(TAG, "mMobileIP[" + mMobileIP + "]");
                }
                if ("KTT".equals("EUR")) {
                    for (int i = 0; i < Values.length; i++) {
                        String[] mElements = Values[i].split(":");
                        Rlog.d(TAG, "getMobileQualityInfo mElements[" + mElements[0] + ":" + mElements[1] + ":" + mElements[2] + "]");
                        if (mElements[1].equals("--")) {
                            map.put(mElements[0], null);
                        } else if (SmartFaceManager.PAGE_BOTTOM.equals(mElements[2])) {
                            map.put(mElements[0], Integer.valueOf(Integer.parseInt(mElements[1])));
                        } else if ("2".equals(mElements[2])) {
                            map.put(mElements[0], mElements[1]);
                        } else if ("3".equals(mElements[2])) {
                            map.put(mElements[0], Float.valueOf(Float.parseFloat(mElements[1])));
                        } else {
                            Rlog.d(TAG, "getMobileQualityInfo Wrong Type[" + mElements[2] + "]");
                        }
                        if (i == 0 && "LTE".equals(mElements[1])) {
                            int callType;
                            Rlog.d(TAG, "getMobileQualityInfo callType VOLTE");
                            if (Settings$System.getInt(this.mContext.getContentResolver(), "voicecall_type", 1) == 0) {
                                callType = 1;
                            } else {
                                callType = 0;
                            }
                            Rlog.d(TAG, "getMobileQualityInfo callType[" + callType + "]");
                            map.put("VOLTE", new Integer(callType));
                        }
                    }
                    return map;
                } else if (Values[0].equals("WCDMA")) {
                    map.put(SubscriptionManager.NETWORK_MODE, Values[0]);
                    map.put(SubscriptionManager.MCC, Integer.valueOf(Integer.parseInt(Values[1])));
                    map.put(SubscriptionManager.MNC, Integer.valueOf(Integer.parseInt(Values[2])));
                    map.put("downlink_channel", Integer.valueOf(Integer.parseInt(Values[3])));
                    map.put("uplink_channel", Integer.valueOf(Integer.parseInt(Values[4])));
                    if ("KTT".equals("EUR")) {
                        map.put("cell_id", Values[5]);
                    } else {
                        map.put("cell_id", Integer.valueOf(Integer.parseInt(Values[5])));
                    }
                    map.put("rssi", Integer.valueOf(Integer.parseInt(Values[6])));
                    if (Values[7].equals("--")) {
                        map.put("tx_power", null);
                    } else {
                        map.put("tx_power", Integer.valueOf(Integer.parseInt(Values[7])));
                    }
                    map.put("ul_interference", Integer.valueOf(Integer.parseInt(Values[8])));
                    map.put("activeset_psc", Integer.valueOf(Integer.parseInt(Values[9])));
                    map.put("activeset_rscp", Integer.valueOf(Integer.parseInt(Values[10])));
                    map.put("activeset_ecio", Integer.valueOf(Integer.parseInt(Values[11])));
                    if (Values[12].equals("--")) {
                        map.put("neighborset_psc", null);
                    } else {
                        map.put("neighborset_psc", Integer.valueOf(Integer.parseInt(Values[12])));
                    }
                    if (Values[13].equals("--")) {
                        map.put("neighborset_rscp", null);
                    } else {
                        map.put("neighborset_rscp", Integer.valueOf(Integer.parseInt(Values[13])));
                    }
                    if (Values[14].equals("--")) {
                        map.put("neighborset_ecio", null);
                    } else {
                        map.put("neighborset_ecio", Integer.valueOf(Integer.parseInt(Values[14])));
                    }
                    map.put("cqi", Integer.valueOf(Integer.parseInt(Values[15])));
                    if (Values[16].equals("--")) {
                        map.put("bler", null);
                    } else {
                        map.put("bler", Float.valueOf(Float.parseFloat(Values[16])));
                    }
                    if (!"KTT".equals("EUR") || Values.length <= 17) {
                        return map;
                    }
                    map.put("etc", Values[17]);
                    return map;
                } else {
                    map.put(SubscriptionManager.NETWORK_MODE, Values[0]);
                    map.put(SubscriptionManager.MCC, Integer.valueOf(Integer.parseInt(Values[1])));
                    map.put(SubscriptionManager.MNC, Integer.valueOf(Integer.parseInt(Values[2])));
                    map.put("tac", Integer.valueOf(Integer.parseInt(Values[3])));
                    map.put("earfcn_downlink", Integer.valueOf(Integer.parseInt(Values[4])));
                    map.put("earfcn_uplink", Integer.valueOf(Integer.parseInt(Values[5])));
                    map.put("band", Integer.valueOf(Integer.parseInt(Values[6])));
                    map.put("bandwidth", Integer.valueOf(Integer.parseInt(Values[7])));
                    if ("KTT".equals("EUR")) {
                        map.put("cell_id", Values[8]);
                    } else {
                        map.put("cell_id", Integer.valueOf(Integer.parseInt(Values[8])));
                    }
                    map.put("pci", Integer.valueOf(Integer.parseInt(Values[9])));
                    map.put("rssi", Integer.valueOf(Integer.parseInt(Values[10])));
                    map.put("rsrp", Integer.valueOf(Integer.parseInt(Values[11])));
                    map.put("rsrq", Integer.valueOf(Integer.parseInt(Values[12])));
                    if (Values[13].equals("--")) {
                        map.put("tx_power", null);
                    } else {
                        map.put("tx_power", Integer.valueOf(Integer.parseInt(Values[13])));
                    }
                    map.put("sinr", Float.valueOf(Float.parseFloat(Values[14])));
                    map.put("rrc", Values[15]);
                    map.put("ip", mMobileIP);
                    map.put("cqi", Integer.valueOf(Integer.parseInt(Values[17])));
                    if ("SKT".equals("EUR") && Values.length >= 33) {
                        map.put("ri", Integer.valueOf(Integer.parseInt(Values[18])));
                        map.put("ca", Integer.valueOf(Integer.parseInt(Values[19])));
                        map.put("s_pci", Integer.valueOf(Integer.parseInt(Values[20])));
                        map.put("s_freq", Integer.valueOf(Integer.parseInt(Values[21])));
                        map.put("s_bandwidth", Integer.valueOf(Integer.parseInt(Values[22])));
                        map.put("s_rsrp", Integer.valueOf(Integer.parseInt(Values[23])));
                        map.put("s_rsrq", Integer.valueOf(Integer.parseInt(Values[24])));
                        map.put("s_sinr", Integer.valueOf(Integer.parseInt(Values[25])));
                        map.put("s2_ca", Integer.valueOf(Integer.parseInt(Values[26])));
                        map.put("s2_pci", Integer.valueOf(Integer.parseInt(Values[27])));
                        map.put("s2_freq", Integer.valueOf(Integer.parseInt(Values[28])));
                        map.put("s2_bandwidth", Integer.valueOf(Integer.parseInt(Values[29])));
                        map.put("s2_rsrp", Integer.valueOf(Integer.parseInt(Values[30])));
                        map.put("s2_rsrq", Integer.valueOf(Integer.parseInt(Values[31])));
                        map.put("s2_sinr", Integer.valueOf(Integer.parseInt(Values[32])));
                    } else if (Values[0].equals("LTE-A")) {
                        map.put(SubscriptionManager.NETWORK_MODE, "LTE");
                        map.put("ri", Integer.valueOf(Integer.parseInt(Values[18])));
                        map.put("ca", Integer.valueOf(Integer.parseInt(Values[19])));
                        map.put("s_pci", Integer.valueOf(Integer.parseInt(Values[20])));
                        map.put("s_freq", Integer.valueOf(Integer.parseInt(Values[21])));
                        if ("KTT".equals("EUR") && Values.length > 22) {
                            map.put("etc", Values[22]);
                        }
                    } else {
                        map.put("ri", new Integer(0));
                        map.put("ca", new Integer(0));
                        map.put("s_pci", new Integer(-1));
                        map.put("s_freq", new Integer(-1));
                        if ("KTT".equals("EUR") && Values.length > 18) {
                            map.put("etc", Values[18]);
                        }
                    }
                    if (!"SKT".equals("EUR") || Values.length != 42) {
                        return map;
                    }
                    if (Values[33].equals("--")) {
                        map.put("neighborset_pci", null);
                    } else {
                        map.put("neighborset_pci", Integer.valueOf(Integer.parseInt(Values[33])));
                    }
                    if (Values[34].equals("--")) {
                        map.put("neighborset_rsrp", null);
                    } else {
                        map.put("neighborset_rsrp", Integer.valueOf(Integer.parseInt(Values[34])));
                    }
                    if (Values[35].equals("--")) {
                        map.put("neighborset_rsrq", null);
                    } else {
                        map.put("neighborset_rsrq", Integer.valueOf(Integer.parseInt(Values[35])));
                    }
                    if (Values[36].equals("--")) {
                        map.put("neighborset_pci_2", null);
                    } else {
                        map.put("neighborset_pci_2", Integer.valueOf(Integer.parseInt(Values[36])));
                    }
                    if (Values[37].equals("--")) {
                        map.put("neighborset_rsrp_2", null);
                    } else {
                        map.put("neighborset_rsrp_2", Integer.valueOf(Integer.parseInt(Values[37])));
                    }
                    if (Values[38].equals("--")) {
                        map.put("neighborset_rsrq_2", null);
                    } else {
                        map.put("neighborset_rsrq_2", Integer.valueOf(Integer.parseInt(Values[38])));
                    }
                    if (Values[39].equals("--")) {
                        map.put("neighborset_pci_3", null);
                    } else {
                        map.put("neighborset_pci_3", Integer.valueOf(Integer.parseInt(Values[39])));
                    }
                    if (Values[40].equals("--")) {
                        map.put("neighborset_rsrp_3", null);
                    } else {
                        map.put("neighborset_rsrp_3", Integer.valueOf(Integer.parseInt(Values[40])));
                    }
                    if (Values[41].equals("--")) {
                        map.put("neighborset_rsrq_3", null);
                        return map;
                    }
                    map.put("neighborset_rsrq_3", Integer.valueOf(Integer.parseInt(Values[41])));
                    return map;
                }
            } catch (RemoteException ex) {
                Rlog.e(TAG, "getIpAddressFromLinkProp() - RemoteException occured : " + ex);
                return null;
            }
        } catch (RemoteException ex2) {
            Rlog.e(TAG, "getMobileQualityInformation() - RemoteException occured : " + ex2);
            return null;
        }
    }

    public void startMobileQualityInformation() {
        Rlog.d(TAG, "block startMobileQualityInformation");
    }

    public void stopMobileQualityInformation() {
        Rlog.d(TAG, "block stopMobileQualityInformation");
    }

    public HashMap getLGUplusKnightInfo() {
        if (!"LGT".equals("EUR") || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_DmLoggingService")) {
            return null;
        }
        HashMap map = new HashMap();
        try {
            try {
                String[] Values = getITelephony().getMobileQualityInformation().split(";");
                Rlog.d("getLGUplusKnightInfo", "length : " + Values.length);
                map.put(IccCardConstants.INTENT_VALUE_ICC_IMSI, Values[0]);
                map.put("GUTI", Values[1]);
                map.put(RILConstants.SETUP_DATA_PROTOCOL_IP, Values[2]);
                map.put("Antenna Bar", Values[3]);
                map.put("NV Mode", Values[4]);
                map.put("Service State", Values[5]);
                map.put("LAC", Values[6]);
                map.put("TAC", Values[7]);
                map.put("Band", Values[8]);
                map.put("Bandwidth", Values[9]);
                map.put("PCI", Values[10]);
                map.put("TX", Values[11]);
                map.put(FmProxy.EXTRA_RSSI, Values[12]);
                map.put("RSRP", Values[13]);
                map.put("RSRQ", Values[14]);
                map.put("SINR", Values[15]);
                map.put("EARFCN downlink", Values[16]);
                map.put("EARFCN uplink", Values[17]);
                map.put("EMM cause", Values[18]);
                map.put("EMM state", Values[19]);
                map.put("EMM connection state", Values[20]);
                map.put("Default Bearer count", Values[21]);
                map.put("Dedicated Bearer count", Values[22]);
                return map;
            } catch (Exception e) {
                Rlog.e(TAG, "getLGUplusKnightInfo() - Values is not valid");
                return null;
            }
        } catch (RemoteException ex) {
            Rlog.d(TAG, "getMobileQualityInformation() - Exception occured : " + ex);
            return null;
        }
    }

    private boolean isDmLoggingPID() {
        String dmDaemonState = SystemProperties.get("ril.dmlog.completed", PowerProfile.POWER_NONE);
        return ("portError".equals(dmDaemonState) || PowerProfile.POWER_NONE.equals(dmDaemonState)) ? false : true;
    }

    private boolean validateAppSignatureForPackage(Context context, String packageName) {
        try {
            for (Signature signature : context.getPackageManager().getPackageInfo(packageName, 64).signatures) {
                if ("SKT".equals("EUR")) {
                    if (DBG) {
                        Rlog.d(TAG_DM_LOGGING, " -" + signature.toCharsString().length());
                        Rlog.d(TAG_DM_LOGGING, " -" + signature.toCharsString());
                    }
                    if (DOD_SKT_APP_SIGNATURE.equals(signature.toCharsString()) || DOD_SKT_APP_SIGNATURE2.equals(signature.toCharsString())) {
                        if (!DBG) {
                            return true;
                        }
                        Rlog.d(TAG_DM_LOGGING, "validateAppSignatureForPackage(), - DOD SIGNATURE !!");
                        return true;
                    }
                } else if (UKNIGHT_LGT_APP_SIGNATURE.equals(signature.toCharsString())) {
                    if (!DBG) {
                        return true;
                    }
                    Rlog.d(TAG_DM_LOGGING, "validateAppSignatureForPackage(), -KNIGHT SIGNATURE !!");
                    return true;
                }
            }
            if (DBG) {
                Rlog.d(TAG_DM_LOGGING, "validateAppSignatureForPackage(), - Not SIGNATURE .");
            }
            return false;
        } catch (NameNotFoundException ex) {
            if (DBG) {
                Rlog.d(TAG_DM_LOGGING, "validateAppSignatureForPackage(), exception - " + ex);
            }
            return false;
        }
    }

    private byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) ((value >>> (((b.length - 1) - i) * 8)) & 255);
        }
        return b;
    }

    private int ByteToInt(byte[] b) {
        return (((b[0] << 24) | ((b[1] & 255) << 16)) | ((b[2] & 255) << 8)) | (b[3] & 255);
    }

    public byte[] uknight_log_set(byte[] setCmd) {
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_RIL_DmLoggingService")) {
            return null;
        }
        if (isDmLoggingPID()) {
            String client = this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid());
            if (DBG && client != null) {
                Rlog.d(TAG_DM_LOGGING, "log_set(), client -" + client);
            }
            byte[] retByte = new byte[2];
            if (client == null || !validateAppSignatureForPackage(this.mContext, client)) {
                return null;
            }
            if (setCmd == null) {
                setCmd = new byte[1];
                if (SystemProperties.get("ril.modem.board").contains("SHANNON") || SystemProperties.get("ril.modem.board").contains("SS")) {
                    setCmd[0] = (byte) 0;
                } else {
                    setCmd[0] = (byte) -1;
                }
            }
            try {
                int[] ret = getITelephony().setDmCmd(1, setCmd);
                if (ret != null) {
                    if (DBG) {
                        Rlog.d(TAG_DM_LOGGING, "log_set(), ret = " + ret[0] + " , " + ret[1]);
                    }
                    if (ret[0] == 0) {
                        retByte[0] = (byte) 0;
                        return retByte;
                    }
                    retByte[0] = (byte) 1;
                    return retByte;
                }
                Rlog.d(TAG_DM_LOGGING, "log_set(), ret = null");
                return null;
            } catch (RemoteException e) {
                return null;
            } catch (NullPointerException e2) {
                return null;
            }
        }
        Rlog.d(TAG_DM_LOGGING, "ps not initialized!");
        return null;
    }

    public byte[] uknight_event_set(byte[] setCmd) {
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_RIL_DmLoggingService")) {
            return null;
        }
        if (isDmLoggingPID()) {
            String client = this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid());
            if (DBG && client != null) {
                Rlog.d(TAG_DM_LOGGING, "event_set(), client -" + client);
            }
            byte[] retByte = new byte[2];
            if (client == null || !validateAppSignatureForPackage(this.mContext, client)) {
                return null;
            }
            if (setCmd == null) {
                setCmd = new byte[1];
                if (SystemProperties.get("ril.modem.board").contains("SHANNON") || SystemProperties.get("ril.modem.board").contains("SS")) {
                    setCmd[0] = (byte) 0;
                } else {
                    setCmd[0] = (byte) -1;
                }
            }
            try {
                int[] ret = getITelephony().setDmCmd(2, setCmd);
                if (ret != null) {
                    if (DBG) {
                        Rlog.d(TAG_DM_LOGGING, "event_set(), ret = " + ret[0] + " , " + ret[1]);
                    }
                    if (ret[0] == 0) {
                        retByte[0] = (byte) 0;
                        return retByte;
                    }
                    retByte[0] = (byte) 1;
                    return retByte;
                }
                Rlog.d(TAG_DM_LOGGING, "event_set(), ret = null");
                return null;
            } catch (RemoteException e) {
                return null;
            } catch (NullPointerException e2) {
                return null;
            }
        }
        Rlog.d(TAG_DM_LOGGING, "ps not initialized!");
        return null;
    }

    public boolean uknight_state_change_set(int eventCode) {
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_RIL_DmLoggingService")) {
            return false;
        }
        if (isDmLoggingPID()) {
            String client = this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid());
            if (DBG && client != null) {
                Rlog.d(TAG_DM_LOGGING, "state_change_set(), client -" + client);
            }
            if (client == null || !validateAppSignatureForPackage(this.mContext, client)) {
                return false;
            }
            if (DBG) {
                Rlog.d(TAG_DM_LOGGING, "state_change_set(), eventCode = " + eventCode);
            }
            try {
                int[] ret = getITelephony().setDmCmd(3, intToByteArray(eventCode));
                if (ret == null) {
                    Rlog.d(TAG_DM_LOGGING, "state_change_set(), ret = null!");
                    return false;
                }
                if (DBG) {
                    Rlog.d(TAG_DM_LOGGING, "state_change_set(), ret = " + ret[0] + " , " + ret[1]);
                }
                if (ret[0] == 0) {
                    return true;
                }
                return false;
            } catch (RemoteException e) {
                return false;
            } catch (NullPointerException e2) {
                return false;
            }
        }
        Rlog.d(TAG_DM_LOGGING, "ps not initialized!");
        return false;
    }

    public boolean uknight_mem_set(int percent) {
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_RIL_DmLoggingService")) {
            return false;
        }
        if (isDmLoggingPID()) {
            String client = this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid());
            if (DBG && client != null) {
                Rlog.d(TAG_DM_LOGGING, "uknight_mem_set(), client -" + client);
            }
            if (client == null || !validateAppSignatureForPackage(this.mContext, client)) {
                return false;
            }
            byte[] setCmd = new byte[1];
            if (DBG) {
                Rlog.d(TAG_DM_LOGGING, "uknight_mem_set(), percent = " + percent);
            }
            setCmd[0] = (byte) percent;
            try {
                int[] ret = getITelephony().setDmCmd(4, setCmd);
                if (ret == null) {
                    Rlog.d(TAG_DM_LOGGING, "uknight_mem_set(), ret = null!");
                    return false;
                }
                if (DBG) {
                    Rlog.d(TAG_DM_LOGGING, "uknight_mem_set(), ret = " + ret[0] + " , " + ret[1]);
                }
                if (ret[0] == 0) {
                    return true;
                }
                return false;
            } catch (RemoteException e) {
                return false;
            } catch (NullPointerException e2) {
                return false;
            }
        }
        Rlog.d(TAG_DM_LOGGING, "ps not initialized!");
        return false;
    }

    public byte[] uknight_get_data() {
        FileNotFoundException e;
        IOException e2;
        Throwable th;
        Exception e3;
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_RIL_DmLoggingService")) {
            return null;
        }
        if (isDmLoggingPID()) {
            String client = this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid());
            if (DBG && client != null) {
                Rlog.d(TAG_DM_LOGGING, "uknight_get_data(), client -" + client);
            }
            if (client == null || !validateAppSignatureForPackage(this.mContext, client)) {
                return null;
            }
            try {
                getITelephony().setDmCmd(6, null);
                FileInputStream fileIS = null;
                byte[] bArr = null;
                try {
                    File file = new File(KNIGHT_LOG_FILE_NAME);
                    if (file.exists()) {
                        int bufSize = (int) file.length();
                        if (bufSize > 0) {
                            FileInputStream fileIS2 = new FileInputStream(KNIGHT_LOG_FILE_NAME);
                            try {
                                bArr = new byte[bufSize];
                                fileIS2.read(bArr);
                                if (DBG) {
                                    Rlog.d(TAG_DM_LOGGING, "uknight_get_data(), buff size = " + bufSize);
                                }
                                fileIS = fileIS2;
                            } catch (FileNotFoundException e4) {
                                e = e4;
                                fileIS = fileIS2;
                                try {
                                    e.printStackTrace();
                                    Rlog.d(TAG_DM_LOGGING, "FileNotFoundException");
                                    bArr = null;
                                    if (fileIS != null) {
                                        try {
                                            fileIS.close();
                                        } catch (IOException e22) {
                                            e22.printStackTrace();
                                            bArr = null;
                                        }
                                    }
                                    getITelephony().setDmCmd(8, null);
                                    return bArr;
                                } catch (Throwable th2) {
                                    th = th2;
                                    if (fileIS != null) {
                                        try {
                                            fileIS.close();
                                        } catch (IOException e222) {
                                            e222.printStackTrace();
                                        }
                                    }
                                    throw th;
                                }
                            } catch (IOException e5) {
                                e222 = e5;
                                fileIS = fileIS2;
                                e222.printStackTrace();
                                Rlog.d(TAG_DM_LOGGING, "IOException");
                                bArr = null;
                                if (fileIS != null) {
                                    try {
                                        fileIS.close();
                                    } catch (IOException e2222) {
                                        e2222.printStackTrace();
                                        bArr = null;
                                    }
                                }
                                getITelephony().setDmCmd(8, null);
                                return bArr;
                            } catch (Exception e6) {
                                e3 = e6;
                                fileIS = fileIS2;
                                e3.printStackTrace();
                                Rlog.d(TAG_DM_LOGGING, "Exception");
                                bArr = null;
                                if (fileIS != null) {
                                    try {
                                        fileIS.close();
                                    } catch (IOException e22222) {
                                        e22222.printStackTrace();
                                        bArr = null;
                                    }
                                }
                                getITelephony().setDmCmd(8, null);
                                return bArr;
                            } catch (Throwable th3) {
                                th = th3;
                                fileIS = fileIS2;
                                if (fileIS != null) {
                                    fileIS.close();
                                }
                                throw th;
                            }
                        }
                        if (fileIS != null) {
                            try {
                                fileIS.close();
                            } catch (IOException e222222) {
                                e222222.printStackTrace();
                                bArr = null;
                            }
                        }
                        try {
                            getITelephony().setDmCmd(8, null);
                            return bArr;
                        } catch (RemoteException e7) {
                            return bArr;
                        } catch (NullPointerException e8) {
                            return bArr;
                        }
                    }
                    Rlog.d(TAG_DM_LOGGING, "uknight_get_data(), log not exist! ");
                    if (fileIS != null) {
                        try {
                            fileIS.close();
                        } catch (IOException e2222222) {
                            e2222222.printStackTrace();
                        }
                    }
                    return null;
                } catch (FileNotFoundException e9) {
                    e = e9;
                    e.printStackTrace();
                    Rlog.d(TAG_DM_LOGGING, "FileNotFoundException");
                    bArr = null;
                    if (fileIS != null) {
                        fileIS.close();
                    }
                    getITelephony().setDmCmd(8, null);
                    return bArr;
                } catch (IOException e10) {
                    e2222222 = e10;
                    e2222222.printStackTrace();
                    Rlog.d(TAG_DM_LOGGING, "IOException");
                    bArr = null;
                    if (fileIS != null) {
                        fileIS.close();
                    }
                    getITelephony().setDmCmd(8, null);
                    return bArr;
                } catch (Exception e11) {
                    e3 = e11;
                    e3.printStackTrace();
                    Rlog.d(TAG_DM_LOGGING, "Exception");
                    bArr = null;
                    if (fileIS != null) {
                        fileIS.close();
                    }
                    getITelephony().setDmCmd(8, null);
                    return bArr;
                }
            } catch (RemoteException e12) {
                return null;
            } catch (NullPointerException e13) {
                return null;
            }
        }
        Rlog.d(TAG_DM_LOGGING, "ps not initialized!");
        return null;
    }

    public int[] uknight_mem_check() {
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_RIL_DmLoggingService")) {
            return null;
        }
        if (isDmLoggingPID()) {
            String client = this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid());
            if (DBG && client != null) {
                Rlog.d(TAG_DM_LOGGING, "uknight_mem_check(), client -" + client);
            }
            if (client == null || !validateAppSignatureForPackage(this.mContext, client)) {
                return null;
            }
            try {
                int[] ret = getITelephony().setDmCmd(5, null);
                if (!DBG) {
                    return ret;
                }
                if (ret != null) {
                    Rlog.d(TAG_DM_LOGGING, "uknight_mem_check(), ret = " + ret[0] + " , " + ret[1]);
                    return ret;
                }
                Rlog.d(TAG_DM_LOGGING, "uknight_mem_check(), ret = null");
                return ret;
            } catch (RemoteException e) {
                return null;
            } catch (NullPointerException e2) {
                return null;
            }
        }
        Rlog.d(TAG_DM_LOGGING, "ps not initialized!");
        return null;
    }

    public byte[] oem_ssa_set_log(byte[] startCode, byte[] maskCode) {
        if (SystemProperties.get("ril.modem.board").contains("SHANNON") || SystemProperties.get("ril.modem.board").contains("SS")) {
            Rlog.d(TAG_DM_LOGGING, "oem_ssa_set_log( , ) - NULL!! ");
            return null;
        }
        byte[] setCmd = null;
        if (!(startCode == null || maskCode == null)) {
            setCmd = new byte[(startCode.length + maskCode.length)];
            System.arraycopy(startCode, 0, setCmd, 0, startCode.length);
            System.arraycopy(maskCode, 0, setCmd, startCode.length, maskCode.length);
        }
        return uknight_log_set(setCmd);
    }

    public byte[] oem_ssa_set_log(byte[] maskCode) {
        if (SystemProperties.get("ril.modem.board").contains("SHANNON") || SystemProperties.get("ril.modem.board").contains("SS")) {
            return uknight_log_set(maskCode);
        }
        Rlog.d(TAG_DM_LOGGING, "oem_ssa_set_log( ) - NULL!! ");
        return null;
    }

    public byte[] oem_ssa_set_event(byte[] setCmd) {
        if (DBG) {
            Rlog.d(TAG_DM_LOGGING, "oem_ssa_set_event() +");
        }
        return uknight_event_set(setCmd);
    }

    public byte[] oem_ssa_alarm_event(byte[] setCmd) {
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_RIL_DmLoggingService")) {
            return null;
        }
        if (isDmLoggingPID()) {
            String client = this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid());
            if (DBG && client != null) {
                Rlog.d(TAG_DM_LOGGING, "oem_ssa_alarm_event(), client -" + client);
            }
            if (client == null || !validateAppSignatureForPackage(this.mContext, client)) {
                return null;
            }
            if (setCmd == null) {
                setCmd = new byte[1];
                if (SystemProperties.get("ril.modem.board").contains("SHANNON") || SystemProperties.get("ril.modem.board").contains("SS")) {
                    setCmd[0] = (byte) 0;
                } else {
                    setCmd[0] = (byte) -1;
                }
            }
            if (DBG) {
                Rlog.d(TAG_DM_LOGGING, "oem_ssa_alarm_event(), [0] = " + setCmd[0] + ", len :" + setCmd.length);
            }
            try {
                int[] ret = getITelephony().setDmCmd(3, setCmd);
                if (ret != null) {
                    if (DBG) {
                        Rlog.d(TAG_DM_LOGGING, "oem_ssa_alarm_event(), ret = " + ret[0] + " , " + ret[1]);
                    }
                    byte[] retByte = new byte[2];
                    if (ret[0] == 0) {
                        retByte[0] = (byte) 0;
                        return retByte;
                    }
                    retByte[0] = (byte) 1;
                    return retByte;
                }
                Rlog.d(TAG_DM_LOGGING, "oem_ssa_alarm_event(), ret = null");
                return null;
            } catch (RemoteException e) {
                return null;
            } catch (NullPointerException e2) {
                return null;
            }
        }
        Rlog.d(TAG_DM_LOGGING, "ps not initialized!");
        return null;
    }

    public byte[] oem_ssa_get_data() {
        return uknight_get_data();
    }

    public boolean oem_ssa_set_mem(int percent) {
        return uknight_mem_set(percent);
    }

    public int[] oem_ssa_check_mem() {
        return uknight_mem_check();
    }

    public String feliCaUimLock(int changestate, int[] cmd, String data) {
        int mEFLockUser = 0;
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_NFC_EnableFelica")) {
            return null;
        }
        String result;
        try {
            int resType;
            int resAPDU;
            Rlog.d(TAG, "feliCaUimLock(), start");
            result = getITelephony().iccTransmitApduBasicChannel(cmd[0], cmd[1], cmd[2], cmd[3], cmd[4], data);
            int sw1 = Integer.parseInt(result.substring(0, 2), 16);
            int sw2 = Integer.parseInt(result.substring(2, 4), 16);
            Rlog.d(TAG, "feliCaUimLock(), return : sw1: " + sw1 + "/ sw2: " + sw2);
            if (changestate == 1) {
                resType = 1;
            } else {
                resType = 2;
            }
            if (sw1 == 144 && sw2 == 0) {
                resAPDU = 36864;
                mEFLockUser = changestate == 1 ? 1 : 0;
            } else {
                resAPDU = (sw1 == 105 && sw2 == 130) ? 27010 : (sw1 == 105 && sw2 == 131) ? 27011 : (sw1 == 105 && sw2 == 133) ? 27013 : (sw1 == 109 && sw2 == 0) ? 27904 : 65535;
            }
            Intent intent = new Intent("android.intent.action.ACTION_EF_LOCK_UPDATED");
            intent.putExtra("responseType", resType);
            intent.putExtra("responseApdu", resAPDU);
            intent.putExtra("efLockUser", mEFLockUser);
            intent.putExtra("efLockRemote", 0);
            if (DBG) {
                Rlog.d(TAG, "notify   [ " + resType + " ]");
                Rlog.d(TAG, "APDU res [ " + resAPDU + " ]");
                Rlog.d(TAG, "UserLock [ " + mEFLockUser + " ]");
                Rlog.d(TAG, "RemoteLock [ " + 0 + " ]");
            }
            this.mContext.sendBroadcast(intent);
            getFeliCaUimLockStatus(0);
        } catch (RemoteException e) {
            Rlog.d(TAG, "feliCaUimLock(), RemoteException");
            result = null;
        } catch (NullPointerException e2) {
            Rlog.d(TAG, "feliCaUimLock(), NullPointerException");
            result = null;
        }
        return result;
    }

    public int getFeliCaUimLockStatus(int option) {
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_NFC_EnableFelica")) {
            return 0;
        }
        try {
            Rlog.d(TAG, "getFeliCaUimLockStatus(), start");
            int result = getITelephony().getFeliCaUimLockStatus(option);
            Rlog.d(TAG, "getFeliCaUimLockStatus(), result: " + result);
            return result;
        } catch (RemoteException e) {
            Rlog.d(TAG, "getFeliCaUimLockStatus(), RemoteException");
            return -1;
        } catch (NullPointerException e2) {
            Rlog.d(TAG, "getFeliCaUimLockStatus(), NullPointerException");
            return -2;
        }
    }

    public int setUimRemoteLockStatus(int option) {
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_NFC_EnableFelica")) {
            return 0;
        }
        try {
            Rlog.d(TAG, "setUimRemoteLockStatus(), start");
            int result = getITelephony().setUimRemoteLockStatus(option);
            Rlog.d(TAG, "setUimRemoteLockStatus(), result: " + result);
            return result;
        } catch (RemoteException e) {
            Rlog.d(TAG, "setUimRemoteLockStatus(), RemoteException");
            return -1;
        } catch (NullPointerException e2) {
            Rlog.d(TAG, "setUimRemoteLockStatus(), NullPointerException");
            return -2;
        }
    }

    public int openLockChannel(String AID) {
        int channel = -1;
        try {
            IccOpenLogicalChannelResponse mOpen = getITelephony().iccOpenLogicalChannel(AID);
            if (mOpen != null) {
                channel = mOpen.getChannel();
            }
        } catch (RemoteException e) {
            Rlog.d(TAG, "openLockChannel(), RemoteException");
        } catch (NullPointerException e2) {
            Rlog.d(TAG, "openLockChannel(), NullPointerException");
        }
        return channel;
    }

    public boolean closeLockChannel(int channel) {
        boolean result = false;
        try {
            result = getITelephony().iccCloseLogicalChannel(channel);
        } catch (RemoteException e) {
            Rlog.d(TAG, "closeLockChannel(), RemoteException");
        } catch (NullPointerException e2) {
            Rlog.d(TAG, "closeLockChannel(), NullPointerException");
        }
        return result;
    }

    public String transmitLockChannel(int cla, int command, int channel, int p1, int p2, int p3, String data) {
        String response = null;
        try {
            response = getITelephony().iccTransmitApduLogicalChannel(channel, cla, command, p1, p2, p3, data);
        } catch (RemoteException e) {
            Rlog.d(TAG, "transmitLockChannel(), RemoteException");
        } catch (NullPointerException e2) {
            Rlog.d(TAG, "transmitLockChannel(), NullPointerException");
        }
        return response;
    }

    public byte[] oem_ssa_hdv_alarm_event(byte[] setCmd) {
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_RIL_DmLoggingService")) {
            return null;
        }
        if (isDmLoggingPID()) {
            String client = this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid());
            if (DBG && client != null) {
                Rlog.d(TAG_DM_LOGGING, "oem_ssa_hdv_alarm_event(), client -" + client);
            }
            if (client == null || !validateAppSignatureForPackage(this.mContext, client)) {
                return null;
            }
            if (setCmd == null) {
                setCmd = new byte[]{(byte) -1};
            }
            if (DBG) {
                Rlog.d(TAG_DM_LOGGING, "oem_ssa_hdv_alarm_event(), [0] = " + setCmd[0] + ", len :" + setCmd.length);
            }
            try {
                int[] ret = getITelephony().setDmCmd(7, setCmd);
                if (ret != null) {
                    if (DBG) {
                        Rlog.d(TAG_DM_LOGGING, "oem_ssa_hdv_alarm_event(), ret = " + ret[0] + " , " + ret[1]);
                    }
                    byte[] retByte = new byte[2];
                    if (ret[0] == 0) {
                        retByte[0] = (byte) 0;
                        return retByte;
                    }
                    retByte[0] = (byte) 1;
                    return retByte;
                }
                Rlog.d(TAG_DM_LOGGING, "oem_ssa_hdv_alarm_event(), ret = null");
                return null;
            } catch (RemoteException e) {
                return null;
            } catch (NullPointerException e2) {
                return null;
            }
        }
        Rlog.d(TAG_DM_LOGGING, "ps not initialized!");
        return null;
    }

    public boolean getSdnAvailable() {
        boolean z = true;
        try {
            z = getITelephony().getSdnAvailable();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    public boolean isGbaSupported() {
        boolean z = false;
        try {
            z = getSubscriberInfo().isGbaSupported();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    public String calculateAkaResponse(byte[] rand, byte[] autn) {
        String str = null;
        try {
            str = getITelephony().calculateAkaResponse(rand, autn);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public byte[] getRand() {
        byte[] bArr = null;
        try {
            bArr = getSubscriberInfo().getRand();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return bArr;
    }

    public String getBtid() {
        String str = null;
        try {
            str = getSubscriberInfo().getBtid();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getKeyLifetime() {
        String str = null;
        try {
            str = getSubscriberInfo().getKeyLifetime();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getIsimAid() {
        String str = null;
        try {
            str = getSubscriberInfo().getIsimAid();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public void setGbaBootstrappingParams(byte[] rand, String btid, String keyLifetime) {
        try {
            getITelephony().setGbaBootstrappingParams(rand, btid, keyLifetime);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
    }

    public GbaBootstrappingResponse calculateGbaBootstrappingResponse(byte[] rand, byte[] autn) {
        try {
            Bundle bundle = getITelephony().calculateGbaBootstrappingResponse(rand, autn);
            if (bundle == null) {
                return null;
            }
            return new GbaBootstrappingResponse(bundle);
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public byte[] calculateNafExternalKey(byte[] nafId) {
        byte[] bArr = null;
        try {
            bArr = getITelephony().calculateNafExternalKey(nafId);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return bArr;
    }

    public void setMultiSimLastRejectIncomingCallPhoneId(int phoneId) {
        try {
            getITelephony().setMultiSimLastRejectIncomingCallPhoneId(phoneId);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
    }

    public int getMultiSimLastRejectIncomingCallPhoneId() {
        int i = 0;
        try {
            i = getITelephony().getMultiSimLastRejectIncomingCallPhoneId();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    public void setMultiSimForegroundPhoneId(int phoneId) {
        try {
            getITelephony().setMultiSimForegroundPhoneId(phoneId);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
    }

    public int getMultiSimForegroundPhoneId() {
        int i = 0;
        try {
            i = getITelephony().getMultiSimForegroundPhoneId();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    public void setEPSLOCI(byte[] newEpsloci) {
        try {
            getITelephony().setEPSLOCI(newEpsloci);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
    }

    public String getLine1NumberType(int SimType) {
        String str = null;
        try {
            str = getSubscriberInfo().getLine1NumberType(SimType);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public String getSubscriberIdType(int SimType) {
        String str = null;
        try {
            str = getSubscriberInfo().getSubscriberIdType(SimType);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return str;
    }

    public boolean hasIsim() {
        boolean z = false;
        try {
            z = getSubscriberInfo().hasIsim();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    public byte[] getPsismsc() {
        byte[] bArr = null;
        try {
            bArr = getSubscriberInfo().getPsismsc();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return bArr;
    }

    public boolean isSmoveripSupported() {
        boolean z = false;
        try {
            z = getSubscriberInfo().isSmoveripSupported();
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return z;
    }

    public boolean isVideoCall() {
        boolean retVal = false;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                retVal = telephony.isVideoCall();
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "Error calling ITelephony#isVideoCall", e);
        } catch (NullPointerException e2) {
        }
        Rlog.d(TAG, "isVideoCall: retVal=" + retVal);
        return retVal;
    }

    public static void setImsLine1Number(String number) {
        if (SHIP_BUILD) {
            Rlog.d(TAG, "setImsLine1Number");
        } else {
            Rlog.d(TAG, "setImsLine1Number is " + number);
        }
        mImsLineNumber = number;
    }

    public static String getImsLine1Number() {
        if (SHIP_BUILD) {
            Rlog.d(TAG, "getImsLine1Number");
        } else {
            Rlog.d(TAG, "getImsLine1Number is " + mImsLineNumber);
        }
        return mImsLineNumber;
    }

    public void setPcoValue(int newPco) {
        try {
            getSubscriberInfo().setPcoValue(newPco);
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
    }

    public boolean isApnTypeAvailable(String apnType) {
        try {
            return getITelephony().isApnTypeAvailable(apnType);
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isApnTypeAvailable", e);
            return false;
        }
    }

    public String getEuimid() {
        try {
            return getITelephony().getEuimid();
        } catch (RemoteException e) {
            Rlog.e(TAG, "error calling itelephony#getEuimid");
            return null;
        }
    }

    public int validateMsisdn() {
        int i = -1;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                i = telephony.validateMsisdn();
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        return i;
    }

    private String getValuefromCSC(String path, String field) {
        String Tag = field;
        String retVal = null;
        InputStream fis = null;
        try {
            fis = new FileInputStream(new File(path));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        if (fis == null) {
            return null;
        }
        InputStream stream = fis;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(stream, "UTF-8");
            int eventType = xpp.getEventType();
            while (eventType != 1) {
                if (eventType == 2 && xpp.getName().equals(Tag)) {
                    xpp.next();
                    eventType = 1;
                    retVal = xpp.getText();
                } else {
                    eventType = xpp.next();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e22) {
                e22.printStackTrace();
            }
        }
        return retVal;
    }

    public int getDisable2g() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getDisable2g();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#getDisable2g", e);
        }
        return -1;
    }

    public boolean setDisable2g(int state) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setDisable2g(state);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setDisable2g RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "setDisable2g NPE", ex2);
        }
        return false;
    }
}
