/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.ServiceConnection
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.ParcelFileDescriptor
 *  android.util.Log
 *  java.io.File
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map$Entry
 *  java.util.Random
 *  java.util.Set
 *  java.util.Timer
 *  java.util.TimerTask
 */
package com.samsung.android.spayfw.appinterface;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.CardAttributeCallback;
import com.samsung.android.spayfw.appinterface.CardDataCallback;
import com.samsung.android.spayfw.appinterface.CardState;
import com.samsung.android.spayfw.appinterface.CommonCallback;
import com.samsung.android.spayfw.appinterface.CommonSpayResponse;
import com.samsung.android.spayfw.appinterface.ConnectionCallback;
import com.samsung.android.spayfw.appinterface.DeleteCardCallback;
import com.samsung.android.spayfw.appinterface.EnrollCardCallback;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardResult;
import com.samsung.android.spayfw.appinterface.ExtractGiftCardDetailRequest;
import com.samsung.android.spayfw.appinterface.ExtractGlobalMembershipCardDetailRequest;
import com.samsung.android.spayfw.appinterface.ExtractLoyaltyCardDetailRequest;
import com.samsung.android.spayfw.appinterface.ExtractLoyaltyCardDetailResponseCallback;
import com.samsung.android.spayfw.appinterface.GiftCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.GiftCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.GiftCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.GiftCardRegisterResponseData;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterResponseData;
import com.samsung.android.spayfw.appinterface.ICardAttributeCallback;
import com.samsung.android.spayfw.appinterface.ICardDataCallback;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.IDeleteCardCallback;
import com.samsung.android.spayfw.appinterface.IEnrollCardCallback;
import com.samsung.android.spayfw.appinterface.IExtractLoyaltyCardDetailResponseCallback;
import com.samsung.android.spayfw.appinterface.IGiftCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.IGiftCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.IInAppPayCallback;
import com.samsung.android.spayfw.appinterface.IPayAppDeathDetector;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.IPaymentFramework;
import com.samsung.android.spayfw.appinterface.IProvisionTokenCallback;
import com.samsung.android.spayfw.appinterface.IPushMessageCallback;
import com.samsung.android.spayfw.appinterface.IRefreshIdvCallback;
import com.samsung.android.spayfw.appinterface.ISelectCardCallback;
import com.samsung.android.spayfw.appinterface.ISelectIdvCallback;
import com.samsung.android.spayfw.appinterface.IServerResponseCallback;
import com.samsung.android.spayfw.appinterface.ISynchronizeCardsCallback;
import com.samsung.android.spayfw.appinterface.ITransactionDetailsCallback;
import com.samsung.android.spayfw.appinterface.IUpdateLoyaltyCardCallback;
import com.samsung.android.spayfw.appinterface.IUserSignatureCallback;
import com.samsung.android.spayfw.appinterface.IVerifyIdvCallback;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.InAppPayCallback;
import com.samsung.android.spayfw.appinterface.InAppTransactionInfo;
import com.samsung.android.spayfw.appinterface.LoyaltyCardShowRequest;
import com.samsung.android.spayfw.appinterface.PayCallback;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;
import com.samsung.android.spayfw.appinterface.ProvisionTokenCallback;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.ProvisionTokenResult;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.PushMessageCallback;
import com.samsung.android.spayfw.appinterface.RefreshIdvResponseCallback;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardCallback;
import com.samsung.android.spayfw.appinterface.SelectIdvResponseCallback;
import com.samsung.android.spayfw.appinterface.ServerRequest;
import com.samsung.android.spayfw.appinterface.ServerResponseCallback;
import com.samsung.android.spayfw.appinterface.SynchronizeCardsCallback;
import com.samsung.android.spayfw.appinterface.Token;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionDetailsCallback;
import com.samsung.android.spayfw.appinterface.UpdateLoyaltyCardCallback;
import com.samsung.android.spayfw.appinterface.UpdateLoyaltyCardInfo;
import com.samsung.android.spayfw.appinterface.UserSignatureCallback;
import com.samsung.android.spayfw.appinterface.VerifyIdvInfo;
import com.samsung.android.spayfw.appinterface.VerifyIdvResponseCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class PaymentFramework {
    public static final String ACTION_PF_NOTIFICATION = "com.samsung.android.spayfw.action.notification";
    private static final int BIND_COUNTER_MAX = 4;
    public static final String CARD_BRAND_AMEX = "AX";
    public static final String CARD_BRAND_DISCOVER = "DS";
    public static final String CARD_BRAND_GIFT = "GI";
    public static final String CARD_BRAND_GLOBAL_MEMBERSHIP = "GM";
    public static final String CARD_BRAND_LOYALTY = "LO";
    public static final String CARD_BRAND_MASTERCARD = "MC";
    public static final String CARD_BRAND_PLCC = "PL";
    public static final String CARD_BRAND_UNKNOWN = "UNKNOWN";
    public static final String CARD_BRAND_VISA = "VI";
    public static final int CARD_PRESENT_MODE_ALL = 15;
    public static final int CARD_PRESENT_MODE_APP = 8;
    public static final int CARD_PRESENT_MODE_ECM = 4;
    public static final int CARD_PRESENT_MODE_MST = 2;
    public static final int CARD_PRESENT_MODE_NFC = 1;
    public static final String CARD_TYPE_CREDIT = "CREDIT";
    public static final String CARD_TYPE_DEBIT = "DEBIT";
    public static final String CARD_TYPE_GIFT = "GIFT";
    public static final String CARD_TYPE_GLOBAL_MEMBERSHIP = "GLOBAL_MEMBERSHIP";
    public static final String CARD_TYPE_LOYALTY = "LOYALTY";
    public static final String CARD_TYPE_SAMSUNG_REWARD = "SAMSUNG_REWARD";
    public static final String CARD_TYPE_UNKNOWN = "UNKNOWN";
    public static final String CONFIG_DEFAULT_CARD = "CONFIG_DEFAULT_CARD";
    public static final String CONFIG_DEVICE_ID = "CONFIG_DEVICE_ID";
    public static final String CONFIG_ENABLE_TAP_N_GO = "CONFIG_ENABLE_TAP_N_GO";
    public static final String CONFIG_JWT_TOKEN = "CONFIG_JWT_TOKEN";
    public static final String CONFIG_PF_INSTANCE_ID = "CONFIG_PF_INSTANCE_ID";
    public static final String CONFIG_RESET_REASON = "CONFIG_RESET_REASON";
    public static final String CONFIG_USER_ID = "CONFIG_USER_ID";
    public static final String CONFIG_VALUE_FALSE = "FALSE";
    public static final String CONFIG_VALUE_TRUE = "TRUE";
    public static final String CONFIG_WALLET_ID = "CONFIG_WALLET_ID";
    public static final String DEVELOPMENT = "DEVELOPMENT";
    public static final String EXTRA_CARD_TYPE = "cardType";
    public static final String EXTRA_DELETE_CARD_DATA = "deleteCardData";
    public static final String EXTRA_INTENT_ACTION = "intentAction";
    public static final String EXTRA_NOTIFICATION_TYPE = "notiType";
    public static final String EXTRA_OPERATION_TYPE = "operation";
    public static final String EXTRA_PACKAGENAME = "packageName";
    public static final String EXTRA_PAYLOAD_DATA = "payloadData";
    public static final String EXTRA_PDOL_TAG_MERCHANT_NAME = "9F4E";
    public static final String EXTRA_PDOL_VALUES = "pdolValues";
    public static final String EXTRA_STATUS_CODE = "status";
    public static final String EXTRA_TOKEN_ID = "tokenId";
    public static final String EXTRA_TOKEN_METADATA_ISSUER_COUNTRY_CODE = "tokenMetadataIssuerCountryCode";
    public static final String EXTRA_TRANSACTION_DETAILS = "transactionDetails";
    public static final String EXTRA_TRANSACTION_TYPE = "transactionType";
    public static final String GIFT_CARD_TOKEN_ID = "GIFT";
    private static final int INTERFACE_VERSION = 84;
    private static final String LOG_TAG = "PaymentFramework";
    public static final String NOTIFICATION_TYPE_PAY_FW_CRASHED = "payFwCrashed";
    public static final String NOTIFICATION_TYPE_SYNC_ALL_CARDS = "syncAllCards";
    public static final String NOTIFICATION_TYPE_TAP_N_GO_STATE = "tapNGoState";
    public static final String NOTIFICATION_TYPE_TRANSACTION_DETAILS_RECEIVED = "transactionDetailsReceived";
    public static final String NOTIFICATION_TYPE_UPDATE_JWT_TOKEN = "updateJwtToken";
    public static final String NOTIFICATION_TYPE_UPDATE_PAYREADY_STATE = "payReadyStateUpdate";
    public static final String OPERATION_TYPE_RESTORE = "restore";
    private static final int PAYFW_OPT_CLEAR_CARD = 11;
    private static final int PAYFW_OPT_CLEAR_ENROLLED_CARD = 16;
    private static final int PAYFW_OPT_DELETE_CARD = 19;
    private static final int PAYFW_OPT_ENROLL = 1;
    private static final int PAYFW_OPT_EXTRACT_GIFTCARD_DETAIL = 27;
    private static final int PAYFW_OPT_EXTRACT_GLOBALMEMBERSHIP_CARD_DETAIL = 44;
    private static final int PAYFW_OPT_EXTRACT_LOYALTY_CARD_DETAIL = 36;
    private static final int PAYFW_OPT_GET_ALL_TOKEN_STATE = 15;
    private static final int PAYFW_OPT_GET_CARD_ATTRIBUTE = 13;
    private static final int PAYFW_OPT_GET_CONFIG = 33;
    private static final int PAYFW_OPT_GET_ENVIRONMENT = 23;
    private static final int PAYFW_OPT_GET_GLOBALMEMBERSHIP_CARD_REGISTERDATA = 41;
    private static final int PAYFW_OPT_GET_GLOBALMEMBERSHIP_CARD_TZENCDATA = 42;
    private static final int PAYFW_OPT_GET_LOGS = 22;
    private static final int PAYFW_OPT_GET_PAY_STATE = 17;
    private static final int PAYFW_OPT_GET_TOKEN_DATA = 18;
    private static final int PAYFW_OPT_GET_TOKEN_STATUS = 10;
    private static final int PAYFW_OPT_GET_TRANSACTION_DETAILS = 38;
    private static final int PAYFW_OPT_GET_USER_SIGNATURE = 31;
    private static final int PAYFW_OPT_IS_DSRP_BLOB_MISSING = 28;
    private static final int PAYFW_OPT_IS_DSRP_BLOB_MISSING_FOR_TOKENID = 29;
    private static final int PAYFW_OPT_NOTIFY_DEVICE_RESET = 12;
    private static final int PAYFW_OPT_PROCESS_SERVER_REQUEST = 34;
    private static final int PAYFW_OPT_PROVISION = 3;
    private static final int PAYFW_OPT_PUSH_HANDLE = 9;
    private static final int PAYFW_OPT_REFRESH_IDV = 39;
    private static final int PAYFW_OPT_SELECT_CARD = 6;
    private static final int PAYFW_OPT_SELECT_IDV = 4;
    private static final int PAYFW_OPT_SET_CONFIG = 32;
    private static final int PAYFW_OPT_SET_JWT_TOKEN = 14;
    private static final int PAYFW_OPT_SET_PIN = 40;
    private static final int PAYFW_OPT_START_GET_GIFTCARD_REGISTERDATA = 24;
    private static final int PAYFW_OPT_START_GET_GIFTCARD_TZENCDATA = 25;
    private static final int PAYFW_OPT_START_GIFTCARD_PAY = 26;
    private static final int PAYFW_OPT_START_GLOBALMEMBERSHIP_CARD_PAY = 43;
    private static final int PAYFW_OPT_START_IN_APP_PAY = 21;
    private static final int PAYFW_OPT_START_LOYALTY_CARD_PAY = 37;
    private static final int PAYFW_OPT_START_PAY = 7;
    private static final int PAYFW_OPT_STOP_PAY = 8;
    private static final int PAYFW_OPT_STORE_USER_SIGNATURE = 30;
    private static final int PAYFW_OPT_SYNCHRONIZ_CARDS = 20;
    private static final int PAYFW_OPT_TNC = 2;
    private static final int PAYFW_OPT_UPDATE_BIN_ATTRIBUTE = 45;
    private static final int PAYFW_OPT_UPDATE_LOYALTY_CARD = 35;
    private static final int PAYFW_OPT_VERIFY_IDV = 5;
    private static final String PAYMENT_FRAMEWORK_DIRECTORY = "pf";
    public static final String PAYMENT_FRAMEWORK_HCE_SERVICE_NAME = "com.samsung.android.spayfw.core.hce.SPayHCEService";
    private static final String PAYMENT_FRAMEWORK_LOG_FILE = "pf.log";
    public static final String PAYMENT_FRAMEWORK_PACKAGE_NAME = "com.samsung.android.spayfw";
    public static final int PAY_TYPE_ECM = 4;
    public static final int PAY_TYPE_MST = 2;
    public static final int PAY_TYPE_NFC = 1;
    private static final long PFW_BIND_TIMEOUT = 30000L;
    public static final String PF_STUB_APP_VERSION = "1.0.00";
    public static final String PF_STUB_APP_VERSION_CSC = "0.0.10";
    public static final int PF_STUB_INTERFACE_VERSION = 1;
    public static final String PRE_PRODUCTION = "PRE-PRODUCTION";
    public static final String PRODUCTION = "PRODUCTION";
    public static final String RESET_REASON_CODE_CLEAR_DATA_PF = "CLEAR_DATA_PF";
    public static final String RESET_REASON_CODE_DEVICE_INTEGRITY_COMPROMISED = "DEVICE_INTEGRITY_COMPROMISED";
    public static final String RESET_REASON_CODE_FACTORY_RESET = "FACTORY_RESET";
    public static final String RESET_REASON_CODE_FMM_WIPEOUT = "FMM_WIPEOUT";
    public static final String RESET_REASON_CODE_SAMSUNG_ACCOUNT_LOGOUT = "SAMSUNG_ACCOUNT_LOGOUT";
    public static final String RESET_REASON_CODE_SPAY_DATA_CLEARED = "SPAY_DATA_CLEARED";
    public static final int RESULT_CODE_CONTENT_DELETED = -8;
    public static final int RESULT_CODE_FAIL_ACCESS_DENIED = -40;
    public static final int RESULT_CODE_FAIL_ALREADY_DONE = -3;
    public static final int RESULT_CODE_FAIL_CARD_NOT_SUPPORTED = -207;
    public static final int RESULT_CODE_FAIL_CASD_UPDATE_FAILED = -43;
    public static final int RESULT_CODE_FAIL_COULDNT_VERIFY_DEVICE_INTEGRITY = -41;
    public static final int RESULT_CODE_FAIL_DEVICE_INTEGRITY_COMPROMISED = -42;
    public static final int RESULT_CODE_FAIL_DEVICE_TOKENS_MAX_LIMIT_REACHED = -209;
    public static final int RESULT_CODE_FAIL_INVALID_CARD = -10;
    public static final int RESULT_CODE_FAIL_INVALID_CARDINPUT = -11;
    public static final int RESULT_CODE_FAIL_INVALID_INPUT = -5;
    public static final int RESULT_CODE_FAIL_INVALID_TRANSACTION_AMOUNT = -101;
    public static final int RESULT_CODE_FAIL_LIMIT_EXCEEDED = -13;
    public static final int RESULT_CODE_FAIL_MC_MISSING_DSRP_BLOB = -45;
    public static final int RESULT_CODE_FAIL_NFC_TIMEOUT_EXPIRED = -46;
    public static final int RESULT_CODE_FAIL_NFC_TRANSACTION_AUTHENTICATION_FAILURE = -33;
    public static final int RESULT_CODE_FAIL_NFC_TRANSACTION_CARD_NOT_SUPPORT = -31;
    public static final int RESULT_CODE_FAIL_NFC_TRANSACTION_DATA_ERROR = -32;
    public static final int RESULT_CODE_FAIL_NFC_TRANSACTION_UNKOWN_ERROR = -34;
    public static final int RESULT_CODE_FAIL_NOT_FOUND = -6;
    public static final int RESULT_CODE_FAIL_NO_NETWORK = -9;
    public static final int RESULT_CODE_FAIL_OPERATION_NOT_ALLOWED = -4;
    public static final int RESULT_CODE_FAIL_PAY_AUTHFAIL = -35;
    public static final int RESULT_CODE_FAIL_PAY_INVALID_APP_STATE = -106;
    public static final int RESULT_CODE_FAIL_PAY_INVALID_TRANSACTION_AID = -105;
    public static final int RESULT_CODE_FAIL_PAY_INVALID_TRANSACTION_CATEGORY = -104;
    public static final int RESULT_CODE_FAIL_PAY_INVALID_TRANSACTION_CURRENCY = -102;
    public static final int RESULT_CODE_FAIL_PAY_INVALID_TRANSACTION_TYPE = -103;
    public static final int RESULT_CODE_FAIL_PAY_PROVIDER = -36;
    public static final int RESULT_CODE_FAIL_PAY_RETRY_TIMEOUT_EXPIRED = -44;
    public static final int RESULT_CODE_FAIL_PAY_TRANSMISSION = -37;
    public static final int RESULT_CODE_FAIL_PFW_BIND = -2;
    public static final int RESULT_CODE_FAIL_PFW_INTERNAL = -1;
    public static final int RESULT_CODE_FAIL_RESET_IN_PROGRESS = -12;
    public static final int RESULT_CODE_FAIL_SERVER_BUSY = -203;
    public static final int RESULT_CODE_FAIL_SERVER_INTERNAL = -205;
    public static final int RESULT_CODE_FAIL_SERVER_NO_RESPONSE = -201;
    public static final int RESULT_CODE_FAIL_SERVER_OUTPUT_INVALID = -204;
    public static final int RESULT_CODE_FAIL_SERVER_REJECT = -202;
    public static final int RESULT_CODE_FAIL_STUB_PF = -47;
    public static final int RESULT_CODE_FAIL_UNSUPPORTED_VERSION = -1000;
    public static final int RESULT_CODE_FAIL_USER_CANCEL = -7;
    public static final int RESULT_CODE_FAIL_WALLET_ID_MISMATCH = -208;
    public static final int RESULT_CODE_IDV_DATA_EXPIRED = -18;
    public static final int RESULT_CODE_IDV_DATA_EXPIRED_AND_SELECTION_EXCEEDED = -21;
    public static final int RESULT_CODE_IDV_DATA_INVALID = -17;
    public static final int RESULT_CODE_IDV_DATA_MISSING = -16;
    public static final int RESULT_CODE_IDV_DATA_RETRY_EXCEEDED = -19;
    public static final int RESULT_CODE_IDV_DATA_SELECTION_EXCEEDED = -20;
    public static final int RESULT_CODE_JWT_TOKEN_INVALID = -206;
    public static final int RESULT_CODE_JWT_TOKEN_MISSING = -15;
    public static final int RESULT_CODE_SUCCESS = 0;
    public static final int RESULT_CODE_WAIT_INIT_FRAMEWORK = 1;
    public static final int RESULT_CODE_WAIT_UPDATE_FRAMEWORK = 2;
    public static final String STAGING = "STAGING";
    private static PaymentFramework mPaymentFramework;
    private static HashMap<Object, Integer> mTrackOpsHash;
    private int mBindCounter = 4;
    private TimerTask mBindTimerTask;
    private boolean mBound;
    private final ServiceConnection mConn = new ServiceConnection(){

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i((String)PaymentFramework.LOG_TAG, (String)("Connected! Name: " + componentName.getClassName()));
            try {
                PaymentFramework.this.resetBindTimer(true);
                PaymentFramework.this.mBound = true;
                PaymentFramework.this.mIPaymentFramework = IPaymentFramework.Stub.asInterface(iBinder);
                if (PaymentFramework.this.mIPaymentFramework != null) {
                    PaymentFramework.this.notifyAndDeleteRequests();
                    return;
                }
                Log.e((String)PaymentFramework.LOG_TAG, (String)"unable to bind to Payment framework");
                PaymentFramework.this.notifyApp();
                return;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return;
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.i((String)PaymentFramework.LOG_TAG, (String)("Disconnected! Name: " + componentName.getClassName()));
            PaymentFramework.this.mBound = false;
            PaymentFramework.this.mIPaymentFramework = null;
            PaymentFramework.this.notifyApp();
        }
    };
    private Context mContext;
    private IPaymentFramework mIPaymentFramework;
    private List<PaymentFrameworkRequest> mPaymentFrameworkRequests;

    static {
        mTrackOpsHash = null;
    }

    private PaymentFramework(Context context) {
        this.mContext = context.getApplicationContext();
    }

    private void addRequest(PaymentFrameworkRequest paymentFrameworkRequest) {
        PaymentFramework paymentFramework = this;
        synchronized (paymentFramework) {
            if (this.mPaymentFrameworkRequests == null) {
                this.mPaymentFrameworkRequests = new ArrayList();
            }
            this.mPaymentFrameworkRequests.add((Object)paymentFrameworkRequest);
            return;
        }
    }

    private static void addToTrackMap(Object object, int n2) {
        Class<PaymentFramework> class_ = PaymentFramework.class;
        synchronized (PaymentFramework.class) {
            if (mTrackOpsHash == null) {
                mTrackOpsHash = new HashMap();
            }
            mTrackOpsHash.put(object, (Object)n2);
            // ** MonitorExit[var4_2] (shouldn't be in output)
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean bindService() {
        PaymentFramework paymentFramework = this;
        synchronized (paymentFramework) {
            if (this.mBindTimerTask != null) {
                Log.d((String)LOG_TAG, (String)"bindService already attempted, waiting.");
                return true;
            }
            IPaymentFramework iPaymentFramework = this.mIPaymentFramework;
            boolean bl = false;
            if (iPaymentFramework != null) return bl;
            Intent intent = new Intent();
            intent.setClassName(PAYMENT_FRAMEWORK_PACKAGE_NAME, "com.samsung.android.spayfw.core.PaymentFrameworkService");
            IPayAppDeathDetector.Stub stub = new IPayAppDeathDetector.Stub(){};
            Bundle bundle = new Bundle();
            bundle.putBinder("deathDetectorBinder", (IBinder)stub);
            intent.putExtras(bundle);
            bl = this.mContext.bindService(intent, this.mConn, 1);
            if (bl) {
                Log.d((String)LOG_TAG, (String)"Service Bind Attempted");
                this.scheduleBindTimer();
            } else {
                Log.e((String)LOG_TAG, (String)"Service Not Bound");
            }
            return bl;
        }
    }

    private static void clearTrackMap() {
        Class<PaymentFramework> class_ = PaymentFramework.class;
        synchronized (PaymentFramework.class) {
            if (mTrackOpsHash != null) {
                mTrackOpsHash.clear();
            }
            // ** MonitorExit[var1] (shouldn't be in output)
            return;
        }
    }

    private boolean createService() {
        PaymentFramework paymentFramework = this;
        synchronized (paymentFramework) {
            boolean bl;
            block3 : {
                boolean bl2;
                bl = true;
                if (this.mIPaymentFramework != null) break block3;
                bl = bl2 = this.bindService();
            }
            return bl;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static PaymentFramework getInstance(Context context) {
        Class<PaymentFramework> class_ = PaymentFramework.class;
        synchronized (PaymentFramework.class) {
            if (context == null) {
                Log.e((String)LOG_TAG, (String)"context is null");
                return null;
            }
            try {
                if (mPaymentFramework == null) {
                    mPaymentFramework = new PaymentFramework(context);
                }
                PaymentFramework paymentFramework = mPaymentFramework;
                return paymentFramework;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
            finally {
                // ** MonitorExit[var4_1] (shouldn't be in output)
            }
        }
    }

    private File getLocalFile(String string) {
        File file = new File(this.mContext.getFilesDir(), PAYMENT_FRAMEWORK_DIRECTORY);
        file.mkdirs();
        int n2 = new Random().nextInt();
        return new File(file, string + System.currentTimeMillis() + String.valueOf((int)n2));
    }

    private static final String getPackageVersion(Context context, String string) {
        try {
            String string2 = context.getPackageManager().getPackageInfo((String)string, (int)0).versionName;
            return string2;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            Log.e((String)"getPackageVersion", (String)("Exception = " + (Object)((Object)nameNotFoundException)));
            return "";
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static int getTrackMapSize() {
        Class<PaymentFramework> class_ = PaymentFramework.class;
        synchronized (PaymentFramework.class) {
            if (mTrackOpsHash == null) return 0;
            int n2 = mTrackOpsHash.size();
            return n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void notifyAndDeleteRequests() {
        PaymentFramework paymentFramework = this;
        synchronized (paymentFramework) {
            Log.d((String)LOG_TAG, (String)"notifyAndDeleteRequests: ");
            List<PaymentFrameworkRequest> list = this.mPaymentFrameworkRequests;
            if (list != null) {
                int n2 = 0;
                do {
                    if (n2 < this.mPaymentFrameworkRequests.size()) {
                        PaymentFrameworkRequest paymentFrameworkRequest = (PaymentFrameworkRequest)this.mPaymentFrameworkRequests.get(n2);
                        Log.d((String)LOG_TAG, (String)("notifyAndDeleteRequests: opt: " + paymentFrameworkRequest.operation));
                        if (paymentFrameworkRequest.callback != null) {
                            paymentFrameworkRequest.callback.onReady();
                        } else {
                            Log.e((String)LOG_TAG, (String)"notifyAndDeleteRequests: callback is null");
                        }
                    } else {
                        this.mPaymentFrameworkRequests.clear();
                        break;
                    }
                    ++n2;
                } while (true);
            }
            return;
        }
    }

    private void notifyApp() {
        if (PaymentFramework.getTrackMapSize() == 0) {
            return;
        }
        block32 : for (Map.Entry entry : mTrackOpsHash.entrySet()) {
            Object object = entry.getKey();
            Integer n2 = (Integer)entry.getValue();
            Log.d((String)LOG_TAG, (String)("notifyApp: cb = " + object + "; key = " + (Object)n2));
            switch ((Integer)entry.getValue()) {
                default: {
                    continue block32;
                }
                case 1: {
                    ((EnrollCardCallback)object).onFail(-2, null);
                    continue block32;
                }
                case 2: {
                    ((CommonCallback)object).onFail(null, -2);
                    continue block32;
                }
                case 3: {
                    ((ProvisionTokenCallback)object).onFail(null, -2, null);
                    continue block32;
                }
                case 4: {
                    ((SelectIdvResponseCallback)object).onFail(null, -2);
                    continue block32;
                }
                case 5: {
                    ((VerifyIdvResponseCallback)object).onFail(null, -2, null);
                    continue block32;
                }
                case 6: {
                    ((SelectCardCallback)object).onFail(null, -2);
                    continue block32;
                }
                case 7: {
                    ((PayCallback)object).onFail(null, -2);
                    continue block32;
                }
                case 8: {
                    ((CommonCallback)object).onFail(null, -2);
                    continue block32;
                }
                case 9: {
                    ((PushMessageCallback)object).onFail(null, -2);
                    continue block32;
                }
                case 14: 
                case 23: 
                case 32: 
                case 33: {
                    ((ConnectionCallback)object).onError(null, -2);
                    continue block32;
                }
                case 21: {
                    ((InAppPayCallback)object).onFail(null, -2);
                    continue block32;
                }
                case 12: 
                case 40: {
                    ((CommonCallback)object).onFail(null, -2);
                    continue block32;
                }
                case 22: {
                    ((CommonCallback)object).onFail(null, -2);
                    continue block32;
                }
                case 24: {
                    ((GiftCardRegisterCallback)object).onFail(-2, null);
                    continue block32;
                }
                case 25: {
                    ((GiftCardRegisterCallback)object).onFail(-2, null);
                    continue block32;
                }
                case 26: {
                    ((PayCallback)object).onFail("GIFT", -2);
                    continue block32;
                }
                case 27: {
                    ((GiftCardExtractDetailCallback)object).onFail(-2);
                    continue block32;
                }
                case 30: {
                    ((CommonCallback)object).onFail(null, -2);
                    continue block32;
                }
                case 31: {
                    ((UserSignatureCallback)object).onFail(-2);
                    continue block32;
                }
                case 34: {
                    ((ServerResponseCallback)object).onFail(-2);
                    continue block32;
                }
                case 35: {
                    ((UpdateLoyaltyCardCallback)object).onFail(-2);
                    continue block32;
                }
                case 36: {
                    ((ExtractLoyaltyCardDetailResponseCallback)object).onFail(-2);
                    continue block32;
                }
                case 37: {
                    ((PayCallback)object).onFail(null, -2);
                    continue block32;
                }
                case 38: {
                    ((TransactionDetailsCallback)object).onFail(null, -2);
                    continue block32;
                }
                case 39: {
                    ((RefreshIdvResponseCallback)object).onFail(null, -2);
                    continue block32;
                }
                case 41: {
                    ((GlobalMembershipCardRegisterCallback)object).onFail(-2, null);
                    continue block32;
                }
                case 42: {
                    ((GlobalMembershipCardRegisterCallback)object).onFail(-2, null);
                    continue block32;
                }
                case 43: {
                    ((PayCallback)object).onFail("GIFT", -2);
                    continue block32;
                }
                case 44: {
                    ((GlobalMembershipCardExtractDetailCallback)object).onFail(-2);
                    continue block32;
                }
                case 45: 
            }
            ((CommonCallback)object).onFail(null, -2);
        }
        PaymentFramework.clearTrackMap();
    }

    public static void removeFromTrackMap(Object object) {
        Class<PaymentFramework> class_ = PaymentFramework.class;
        synchronized (PaymentFramework.class) {
            if (mTrackOpsHash != null) {
                mTrackOpsHash.remove(object);
            }
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void resetBindTimer(boolean bl) {
        PaymentFramework paymentFramework = this;
        synchronized (paymentFramework) {
            try {
                if (this.mBindTimerTask != null) {
                    if (this.mBindTimerTask.cancel()) {
                        Log.d((String)LOG_TAG, (String)"resetBindTimer: timerTask cancel return true ");
                    } else {
                        Log.d((String)LOG_TAG, (String)"resetBindTimer: timerTask cancel return false ");
                    }
                    this.mBindTimerTask = null;
                }
                if (bl) {
                    Log.d((String)LOG_TAG, (String)"resetBindTimer: resetCount ");
                    this.mBindCounter = 4;
                }
            }
            catch (Exception exception) {
                Log.d((String)LOG_TAG, (String)"scheduleBindTimer: Exception in canceling bind timer ");
                exception.printStackTrace();
            }
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void scheduleBindTimer() {
        PaymentFramework paymentFramework = this;
        synchronized (paymentFramework) {
            Log.d((String)LOG_TAG, (String)"scheduleBindTimer: scheduling bind timer ");
            try {
                this.mBindCounter = -1 + this.mBindCounter;
                this.mBindTimerTask = new BindRetryTimerTask();
                new Timer().schedule(this.mBindTimerTask, 30000L);
                do {
                    return;
                    break;
                } while (true);
            }
            catch (Exception exception) {
                try {
                    Log.d((String)LOG_TAG, (String)"scheduleBindTimer: Exception in scheduling bind timer ");
                    exception.printStackTrace();
                    return;
                }
                catch (Throwable throwable) {
                    throw throwable;
                }
                finally {
                }
            }
        }
    }

    /*
     * Exception decompiling
     */
    private File writeStringToFile(String var1_1, String var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
        // org.benf.cfr.reader.b.a.a.j.b(Op04StructuredStatement.java:409)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:487)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    public int acceptTnC(String string, boolean bl, CommonCallback commonCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"acceptTnC(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.acceptTnC(string, bl, commonCallback.getICallback());
                    PaymentFramework.addToTrackMap(commonCallback, 2);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(2, commonCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"acceptTnC Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int clearCard(ConnectionCallback connectionCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"clearCard(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            if (this.mIPaymentFramework == null) break block5;
            this.mIPaymentFramework.clearCard();
            n2 = 0;
        }
        try {
            Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
            this.addRequest(new PaymentFrameworkRequest(11, connectionCallback));
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"clearCard Exception");
            exception.printStackTrace();
            n2 = -2;
        }
        return n2;
    }

    public int clearEnrolledCard(String string, ConnectionCallback connectionCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"clearEnrolledCard(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            if (this.mIPaymentFramework == null) break block5;
            this.mIPaymentFramework.clearEnrolledCard(string);
            n2 = 0;
        }
        try {
            Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
            this.addRequest(new PaymentFrameworkRequest(16, connectionCallback));
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"clearEnrolledCard Exception");
            exception.printStackTrace();
            n2 = -2;
        }
        return n2;
    }

    public int deleteCard(String string, Bundle bundle, DeleteCardCallback deleteCardCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)("deleteCard(): enrollId " + string));
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.deleteCard(string, bundle, deleteCardCallback.getPFDeleteCardCb());
                    PaymentFramework.addToTrackMap(deleteCardCallback, 19);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(19, deleteCardCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"deleteCard Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int deleteCard(String string, DeleteCardCallback deleteCardCallback) {
        return this.deleteCard(string, null, deleteCardCallback);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    public int enrollCard(EnrollCardInfo var1_1, BillingInfo var2_2, EnrollCardCallback var3_3) {
        block24 : {
            block22 : {
                block21 : {
                    block19 : {
                        block20 : {
                            var4_4 = -2;
                            Log.d((String)"PaymentFramework", (String)"enrollCard(): ");
                            if (this.createService()) break block19;
                            Log.e((String)"PaymentFramework", (String)"unable to bind to Payment framework");
                            if (var1_1 == null) break block20;
                            var1_1.decrementRefCount();
lbl8: // 2 sources:
                            do {
                                return var4_4;
                                break;
                            } while (true);
                        }
                        Log.d((String)"PaymentFramework", (String)"enrollCard(): enrollInfo is null ");
                        return var4_4;
                    }
                    if (this.mIPaymentFramework == null) break block21;
                    var7_6 = var15_5 = this.mIPaymentFramework.enrollCard(var1_1, var2_2, var3_3.getPFEnrollCb());
                    PaymentFramework.addToTrackMap(var3_3, 1);
                    var4_4 = var7_6;
lbl20: // 2 sources:
                    while (var1_1 != null) {
                        if (var4_4 != 1 && var4_4 != 0) {
                            var1_1.decrementRefCount();
                            return var4_4;
                        }
                        break block22;
                    }
                    break block24;
                }
                try {
                    Log.w((String)"PaymentFramework", (String)"PaymentFramework not ready yet");
                    this.addRequest(new PaymentFrameworkRequest(1, var3_3));
                    var4_4 = 1;
                    ** GOTO lbl20
                }
                catch (Exception var9_7) {
                    var7_6 = 1;
lbl41: // 2 sources:
                    do {
                        block23 : {
                            Log.e((String)"PaymentFramework", (String)"enollCard Exception");
                            var9_8.printStackTrace();
                            if (var1_1 == null) break block23;
                            var1_1.decrementRefCount();
                            return var4_4;
                        }
                        Log.d((String)"PaymentFramework", (String)"enrollCard(): enrollInfo is null ");
                        return var4_4;
                        break;
                    } while (true);
                }
                catch (Throwable var6_10) {
                    var7_6 = 1;
lbl54: // 3 sources:
                    do {
                        block25 : {
                            if (var1_1 == null) break block25;
                            if (var7_6 != 1 && var7_6 != 0) {
                                var1_1.decrementRefCount();
lbl58: // 3 sources:
                                do {
                                    throw var6_11;
                                    break;
                                } while (true);
                            }
                            if (var3_3 != null) {
                                var3_3.setEnrollCardInfo(var1_1);
                            }
                            ** GOTO lbl58
                        }
                        Log.d((String)"PaymentFramework", (String)"enrollCard(): enrollInfo is null ");
                        ** continue;
                        break;
                    } while (true);
                }
            }
            ** while (var3_3 == null)
lbl34: // 1 sources:
            var3_3.setEnrollCardInfo(var1_1);
            return var4_4;
        }
        Log.d((String)"PaymentFramework", (String)"enrollCard(): enrollInfo is null ");
        return var4_4;
        {
            catch (Throwable var6_12) {
                ** GOTO lbl54
            }
            catch (Throwable var11_13) {
                var7_6 = var4_4;
                var6_11 = var11_13;
                ** continue;
            }
        }
        catch (Exception var9_9) {
            ** continue;
        }
    }

    public int extractGiftCardDetail(ExtractGiftCardDetailRequest extractGiftCardDetailRequest, SecuredObject securedObject, GiftCardExtractDetailCallback giftCardExtractDetailCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"extractGiftCardDetail(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.extractGiftCardDetail(extractGiftCardDetailRequest, securedObject, giftCardExtractDetailCallback.getPFGiftCardExtractDetailCb());
                    PaymentFramework.addToTrackMap(giftCardExtractDetailCallback, 27);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(27, giftCardExtractDetailCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"extractGiftCardDetail() Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int extractGlobalMembershipCardDetail(List<ExtractGlobalMembershipCardDetailRequest> list, GlobalMembershipCardExtractDetailCallback globalMembershipCardExtractDetailCallback) {
        int n2;
        block6 : {
            block5 : {
                Log.d((String)LOG_TAG, (String)"extractGlobalMembershipCardDetail(): ");
                n2 = 1;
                if (this.createService()) break block5;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.extractGlobalMembershipCardDetail(list, globalMembershipCardExtractDetailCallback.getPFGlobalMembershipCardExtractDetailCb());
                    if (mTrackOpsHash == null) {
                        mTrackOpsHash = new HashMap();
                    }
                    mTrackOpsHash.put((Object)globalMembershipCardExtractDetailCallback, (Object)44);
                    break block6;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(44, globalMembershipCardExtractDetailCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"extractGlobalMembershipCardDetail() Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int extractLoyaltyCardDetails(ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest, ExtractLoyaltyCardDetailResponseCallback extractLoyaltyCardDetailResponseCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.i((String)LOG_TAG, (String)"extractLoyaltyCardDetails(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.extractLoyaltyCardDetails(extractLoyaltyCardDetailRequest, extractLoyaltyCardDetailResponseCallback.getPFExtractLoyaltyCardDetailCallback());
                    PaymentFramework.addToTrackMap(extractLoyaltyCardDetailResponseCallback, 36);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(36, extractLoyaltyCardDetailResponseCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"extractLoyaltyCardDetails Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public List<CardState> getAllCardState(Bundle bundle, ConnectionCallback connectionCallback) {
        block6 : {
            if (this.createService()) break block6;
            Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
            if (connectionCallback == null) return null;
            connectionCallback.onError(null, -2);
            return null;
        }
        if (this.mIPaymentFramework != null) {
            return this.mIPaymentFramework.getAllCardState(bundle);
        }
        Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
        this.addRequest(new PaymentFrameworkRequest(15, connectionCallback));
        if (connectionCallback == null) return null;
        try {
            connectionCallback.onError(null, 1);
            return null;
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"getTokenState Exception");
        }
        return null;
    }

    public List<CardState> getAllCardState(ConnectionCallback connectionCallback) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(EXTRA_CARD_TYPE, new String[]{CARD_TYPE_CREDIT, CARD_TYPE_DEBIT, CARD_TYPE_SAMSUNG_REWARD, CARD_TYPE_GLOBAL_MEMBERSHIP});
        return this.getAllCardState(bundle, connectionCallback);
    }

    public int getCardAttribute(String string, boolean bl, CardAttributeCallback cardAttributeCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"getCardAttribute(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.getCardAttributes(string, bl, cardAttributeCallback.getICallback());
                    PaymentFramework.addToTrackMap(cardAttributeCallback, 13);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(13, cardAttributeCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"notifyDeviceReset Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int getCardData(String string, CardDataCallback cardDataCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)("getTokenData(): " + string));
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.getCardData(string, cardDataCallback.getPFCardDataCb());
                    PaymentFramework.addToTrackMap(cardDataCallback, 18);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(18, cardDataCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"getTokenData Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String getConfig(String string, ConnectionCallback connectionCallback) {
        block7 : {
            Log.d((String)LOG_TAG, (String)"getConfig(): ");
            if (this.createService()) break block7;
            Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
            if (connectionCallback == null) return null;
            connectionCallback.onError(null, -2);
            return null;
        }
        if (this.mIPaymentFramework != null) {
            return this.mIPaymentFramework.getConfig(string);
        }
        Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
        this.addRequest(new PaymentFrameworkRequest(33, connectionCallback));
        if (connectionCallback == null) return null;
        try {
            connectionCallback.onError(null, 1);
            return null;
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"getConfig Exception");
            if (connectionCallback != null) {
                connectionCallback.onError(null, -2);
            }
            exception.printStackTrace();
        }
        return null;
    }

    public int getEnvironment(CommonCallback commonCallback) {
        block4 : {
            Log.d((String)LOG_TAG, (String)"getEnvironment(): ");
            try {
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"getEnvironment Exception");
                exception.printStackTrace();
                return 1;
            }
        }
        if (this.mIPaymentFramework != null) {
            return this.mIPaymentFramework.getEnvironment(commonCallback.getICallback());
        }
        Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
        this.addRequest(new PaymentFrameworkRequest(23, commonCallback));
        return 1;
    }

    public int getGiftCardRegisterData(GiftCardRegisterRequestData giftCardRegisterRequestData, GiftCardRegisterCallback giftCardRegisterCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.i((String)LOG_TAG, (String)"getGiftCardRegisterData(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.getGiftCardRegisterData(giftCardRegisterRequestData, giftCardRegisterCallback.getPFGiftCardRegisterCb());
                    PaymentFramework.addToTrackMap(giftCardRegisterCallback, 24);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(24, giftCardRegisterCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"getGiftCardRegisterData Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int getGiftCardTzEncData(GiftCardRegisterRequestData giftCardRegisterRequestData, GiftCardRegisterCallback giftCardRegisterCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.i((String)LOG_TAG, (String)"getGiftCardTZEncData(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.getGiftCardTzEncData(giftCardRegisterRequestData, giftCardRegisterCallback.getPFGiftCardRegisterCb());
                    PaymentFramework.addToTrackMap(giftCardRegisterCallback, 25);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(25, giftCardRegisterCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"getGiftCardTZEncData(): Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int getGlobalMembershipCardRegisterData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, GlobalMembershipCardRegisterCallback globalMembershipCardRegisterCallback) {
        int n2;
        block6 : {
            block5 : {
                Log.i((String)LOG_TAG, (String)"getGlobalMembershipCardRegisterData(): ");
                n2 = 1;
                if (this.createService()) break block5;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.getGlobalMembershipCardRegisterData(globalMembershipCardRegisterRequestData, globalMembershipCardRegisterCallback.getPFGlobalMembershipCardRegisterCb());
                    if (mTrackOpsHash == null) {
                        mTrackOpsHash = new HashMap();
                    }
                    mTrackOpsHash.put((Object)globalMembershipCardRegisterCallback, (Object)41);
                    break block6;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(41, globalMembershipCardRegisterCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"getGlobalMembershipCardRegisterData Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int getGlobalMembershipCardTzEncData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, GlobalMembershipCardRegisterCallback globalMembershipCardRegisterCallback) {
        int n2;
        block6 : {
            block5 : {
                Log.i((String)LOG_TAG, (String)"getGlobalMembershipCardTZEncData(): ");
                n2 = 1;
                if (this.createService()) break block5;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.getGlobalMembershipCardTzEncData(globalMembershipCardRegisterRequestData, globalMembershipCardRegisterCallback.getPFGlobalMembershipCardRegisterCb());
                    if (mTrackOpsHash == null) {
                        mTrackOpsHash = new HashMap();
                    }
                    mTrackOpsHash.put((Object)globalMembershipCardRegisterCallback, (Object)42);
                    break block6;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(42, globalMembershipCardRegisterCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"getGlobalMembershipCardTZEncData(): Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int getInterfaceVersion() {
        return 84;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public int getLogs(CommonCallback commonCallback) {
        block15 : {
            block14 : {
                n2 = -2;
                Log.d((String)"PaymentFramework", (String)"getLogs()");
                parcelFileDescriptor = null;
                if (this.createService()) break block14;
                Log.e((String)"PaymentFramework", (String)"unable to bind to Payment framework");
                if (false == false) return n2;
                try {
                    null.close();
                    return n2;
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    return n2;
                }
            }
            iPaymentFramework = this.mIPaymentFramework;
            parcelFileDescriptor = null;
            if (iPaymentFramework == null) break block15;
            file = new File(this.mContext.getFilesDir(), "pf");
            file.mkdirs();
            file2 = new File(file, "pf.log");
            parcelFileDescriptor = ParcelFileDescriptor.open((File)file2, (int)738197504);
            n3 = this.mIPaymentFramework.getLogs(parcelFileDescriptor, file2.getAbsolutePath(), commonCallback.getICallback());
            PaymentFramework.addToTrackMap(commonCallback, 22);
            n2 = n3;
            ** GOTO lbl44
        }
        try {
            try {
                Log.w((String)"PaymentFramework", (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(22, commonCallback));
                n2 = 1;
                parcelFileDescriptor = null;
            }
            catch (Exception exception) {
                Log.e((String)"PaymentFramework", (String)"getLogs Exception");
                exception.printStackTrace();
                if (parcelFileDescriptor == null) return n2;
                try {
                    parcelFileDescriptor.close();
                    return n2;
                }
                catch (Exception exception3) {
                    exception3.printStackTrace();
                    return n2;
                }
            }
lbl44: // 2 sources:
            if (parcelFileDescriptor == null) return n2;
            try {
                parcelFileDescriptor.close();
                return n2;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return n2;
            }
        }
        catch (Throwable throwable) {
            if (parcelFileDescriptor == null) throw throwable;
            try {
                parcelFileDescriptor.close();
            }
            catch (Exception exception2) {
                exception2.printStackTrace();
                throw throwable;
            }
            throw throwable;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public int getPaymentFrameworkInterfaceVersion() {
        int n2;
        int n3 = n2 = this.mContext.getPackageManager().getApplicationInfo((String)"com.samsung.android.spayfw", (int)128).metaData.getInt("com.samsung.android.spayfw.interface_version", -1);
        if (n3 != -1) return n3;
        String string = PaymentFramework.getPackageVersion(this.mContext, PAYMENT_FRAMEWORK_PACKAGE_NAME);
        if (string == null) return n3;
        if (string.isEmpty()) return n3;
        String[] arrstring = string.split("\\.");
        if (arrstring == null) return n3;
        if (arrstring.length <= 2) return n3;
        int n4 = Integer.parseInt((String)arrstring[1]);
        return n4;
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            PackageManager.NameNotFoundException nameNotFoundException2;
            block8 : {
                n3 = -1;
                nameNotFoundException2 = nameNotFoundException;
                break block8;
                catch (NullPointerException nullPointerException) {
                    NullPointerException nullPointerException2;
                    block9 : {
                        n3 = -1;
                        nullPointerException2 = nullPointerException;
                        break block9;
                        catch (Exception exception) {
                            Exception exception2;
                            block10 : {
                                n3 = -1;
                                exception2 = exception;
                                break block10;
                                catch (Exception exception3) {}
                            }
                            Log.e((String)LOG_TAG, (String)exception2.getMessage());
                            return n3;
                        }
                        catch (NullPointerException nullPointerException3) {}
                    }
                    Log.e((String)LOG_TAG, (String)("Failed to load meta-data, NullPointer: " + nullPointerException2.getMessage()));
                    return n3;
                }
                catch (PackageManager.NameNotFoundException nameNotFoundException3) {}
            }
            Log.e((String)LOG_TAG, (String)("Failed to load meta-data, NameNotFound: " + nameNotFoundException2.getMessage()));
            return n3;
        }
    }

    public int getPaymentFrameworkInterfaceVersionFromMetadata() {
        try {
            int n2 = this.mContext.getPackageManager().getApplicationInfo((String)"com.samsung.android.spayfw", (int)128).metaData.getInt("com.samsung.android.spayfw.interface_version", -1);
            return n2;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            Log.e((String)LOG_TAG, (String)("Failed to load meta-data, NameNotFound: " + nameNotFoundException.getMessage()));
            return -1;
        }
        catch (NullPointerException nullPointerException) {
            Log.e((String)LOG_TAG, (String)("Failed to load meta-data, NullPointer: " + nullPointerException.getMessage()));
            return -1;
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)exception.getMessage());
            return -1;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public List<String> getPaymentReadyState(String string, ConnectionCallback connectionCallback) {
        block5 : {
            Log.d((String)LOG_TAG, (String)"getPayAvailableTokens(): ");
            if (this.createService()) break block5;
            Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
            if (connectionCallback == null) return null;
            connectionCallback.onError(null, -2);
            return null;
        }
        try {
            if (this.mIPaymentFramework != null) {
                return this.mIPaymentFramework.getPaymentReadyState(string);
            }
            Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
            this.addRequest(new PaymentFrameworkRequest(17, connectionCallback));
            return null;
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"clearEnrolledCard Exception");
            exception.printStackTrace();
        }
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public TokenStatus getTokenStatus(String string, ConnectionCallback connectionCallback) {
        block5 : {
            Log.d((String)LOG_TAG, (String)("mBound =  " + this.mBound));
            if (this.createService()) break block5;
            Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
            if (connectionCallback == null) return null;
            connectionCallback.onError(string, -2);
            return null;
        }
        try {
            if (this.mIPaymentFramework != null) {
                return this.mIPaymentFramework.getTokenStatus(string);
            }
            Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
            this.addRequest(new PaymentFrameworkRequest(10, connectionCallback));
            return null;
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"getTokenStatus Exception");
            exception.printStackTrace();
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getTransactionDetails(String string, long l2, long l3, int n2, TransactionDetailsCallback transactionDetailsCallback) {
        Log.d((String)LOG_TAG, (String)"getTransactionDetails(): ");
        try {
            if (!this.createService()) {
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            if (this.mIPaymentFramework != null) {
                int n3 = this.mIPaymentFramework.getTransactionDetails(string, l2, l3, n2, transactionDetailsCallback.getPFTransactionDetailsCb());
                PaymentFramework.addToTrackMap(transactionDetailsCallback, 38);
                return n3;
            }
            Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
            this.addRequest(new PaymentFrameworkRequest(38, transactionDetailsCallback));
            return 1;
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"getTransactionDetails Exception");
            exception.printStackTrace();
            return -2;
        }
    }

    public int getUserSignature(String string, UserSignatureCallback userSignatureCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"getUserSignature():");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.getUserSignature(string, userSignatureCallback.getICallback());
                    PaymentFramework.addToTrackMap(userSignatureCallback, 31);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(31, userSignatureCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"getUserSignature Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public List<String> isDsrpBlobMissing(ConnectionCallback connectionCallback) {
        ArrayList arrayList;
        block6 : {
            arrayList = new ArrayList();
            if (this.createService()) break block6;
            Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
            if (connectionCallback == null) return arrayList;
            connectionCallback.onError(null, -2);
            return arrayList;
        }
        if (this.mIPaymentFramework != null) {
            return this.mIPaymentFramework.isDsrpBlobMissing();
        }
        Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
        this.addRequest(new PaymentFrameworkRequest(28, connectionCallback));
        if (connectionCallback == null) return arrayList;
        try {
            connectionCallback.onError(null, 1);
            return arrayList;
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"isDsrpBlobMissing Exception");
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int isDsrpBlobMissingForTokenId(String string, ConnectionCallback connectionCallback) {
        try {
            if (!this.createService()) {
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                if (connectionCallback == null) return -2;
                connectionCallback.onError(null, -2);
                return -2;
            }
            if (this.mIPaymentFramework != null) {
                return this.mIPaymentFramework.isDsrpBlobMissingForTokenId(string);
            }
            Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
            this.addRequest(new PaymentFrameworkRequest(29, connectionCallback));
            int n2 = 0;
            if (connectionCallback == null) return n2;
            connectionCallback.onError(null, 1);
            return 0;
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"isDsrpBlobMissingForTokenId Exception");
            return 0;
        }
    }

    public int processPushMessage(PushMessage pushMessage, PushMessageCallback pushMessageCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"processPushMessage(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.processPushMessage(pushMessage, pushMessageCallback.getPFPushMsgCb());
                    PaymentFramework.addToTrackMap(pushMessageCallback, 9);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(9, pushMessageCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"processPushMessage Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int processServerRequest(ServerRequest serverRequest, ServerResponseCallback serverResponseCallback) {
        File file;
        ParcelFileDescriptor parcelFileDescriptor;
        int n2;
        block9 : {
            block8 : {
                Log.i((String)LOG_TAG, (String)"processServerRequest(): ");
                n2 = 1;
                try {
                    if (!this.createService()) {
                        Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                        return -2;
                    }
                    if (this.mIPaymentFramework == null) {
                        Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                        this.addRequest(new PaymentFrameworkRequest(34, serverResponseCallback));
                        return n2;
                    }
                    if (serverRequest == null || serverRequest.getBody() == null) break block8;
                    File file2 = this.writeStringToFile(serverRequest.getBody(), "serverRequest_");
                    if (file2 != null) {
                        ParcelFileDescriptor parcelFileDescriptor2 = ParcelFileDescriptor.open((File)file2, (int)268435456);
                        serverRequest.setFd(parcelFileDescriptor2);
                        parcelFileDescriptor = parcelFileDescriptor2;
                        file = file2;
                    } else {
                        file = file2;
                        parcelFileDescriptor = null;
                    }
                    break block9;
                }
                catch (Exception exception) {
                    Log.e((String)LOG_TAG, (String)"processServerRequest Exception");
                    exception.printStackTrace();
                    return -2;
                }
            }
            file = null;
            parcelFileDescriptor = null;
        }
        n2 = this.mIPaymentFramework.processServerRequest(serverRequest, serverResponseCallback.getPFServerCallback());
        if (parcelFileDescriptor != null) {
            parcelFileDescriptor.close();
        }
        if (file != null) {
            file.delete();
        }
        PaymentFramework.addToTrackMap(serverResponseCallback, 34);
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public CommonSpayResponse processSpayApdu(byte[] arrby, Bundle bundle) {
        CommonSpayResponse commonSpayResponse;
        Log.d((String)LOG_TAG, (String)"processSpayApdu(): ");
        int n2 = 1;
        try {
            if (this.mIPaymentFramework != null) {
                CommonSpayResponse commonSpayResponse2;
                commonSpayResponse = commonSpayResponse2 = this.mIPaymentFramework.processSpayApdu(arrby, bundle);
            } else {
                Log.e((String)LOG_TAG, (String)"PaymentFramework not ready yet, should not happen.");
                commonSpayResponse = null;
            }
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"processSpayApdu Exception");
            exception.printStackTrace();
            n2 = -2;
            commonSpayResponse = null;
        }
        if (commonSpayResponse == null) {
            commonSpayResponse = new CommonSpayResponse();
            commonSpayResponse.setStatus(n2);
        }
        return commonSpayResponse;
    }

    public int provisionToken(String string, ProvisionTokenInfo provisionTokenInfo, ProvisionTokenCallback provisionTokenCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"provisionToken(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.provisionToken(string, provisionTokenInfo, provisionTokenCallback.getPFProvisionCb());
                    PaymentFramework.addToTrackMap(provisionTokenCallback, 3);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(3, provisionTokenCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"provisionToken Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int refreshIdv(String string, RefreshIdvResponseCallback refreshIdvResponseCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"refreshIdv(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.refreshIdv(string, refreshIdvResponseCallback.getPFRefreshIdvCallback());
                    PaymentFramework.addToTrackMap(refreshIdvResponseCallback, 39);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(39, refreshIdvResponseCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"selectIDV Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int reset(String string, CommonCallback commonCallback) {
        int n2;
        block11 : {
            block10 : {
                block9 : {
                    block8 : {
                        n2 = 1;
                        Log.d((String)LOG_TAG, (String)("reset(): " + string));
                        if (this.createService()) break block8;
                        Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                        return -2;
                    }
                    String string2 = PaymentFramework.getPackageVersion(this.mContext, PAYMENT_FRAMEWORK_PACKAGE_NAME);
                    Log.d((String)LOG_TAG, (String)("pf_version = " + string2));
                    if (string2 == null) break block9;
                    if (!string2.equals((Object)PF_STUB_APP_VERSION) && !string2.equals((Object)PF_STUB_APP_VERSION_CSC)) break block9;
                    Log.d((String)LOG_TAG, (String)"Device has Stub PF. Data already cleared");
                    return -47;
                }
                int n3 = this.getPaymentFrameworkInterfaceVersionFromMetadata();
                Log.d((String)LOG_TAG, (String)("Interface Version = " + n3));
                if (n3 != n2) break block10;
                Log.d((String)LOG_TAG, (String)"Device has Stub PF. Data already cleared");
                return -47;
            }
            try {
                Log.d((String)LOG_TAG, (String)"Device does not have stub PF");
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.reset(string, commonCallback.getICallback());
                    PaymentFramework.addToTrackMap(commonCallback, 12);
                    break block11;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(12, commonCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"notifyDeviceReset Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int retryPay(PayConfig payConfig) {
        Log.d((String)LOG_TAG, (String)"retryPay(): ");
        try {
            if (this.mIPaymentFramework != null) {
                return this.mIPaymentFramework.retryPay(payConfig);
            }
            Log.e((String)LOG_TAG, (String)"PaymentFramework not ready yet, should not happen.");
            return 1;
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"startPay Exception");
            exception.printStackTrace();
            return -2;
        }
    }

    public int selectCard(String string, SelectCardCallback selectCardCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"selectCard(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.selectCard(string, selectCardCallback.getPFSelectCardCb());
                    PaymentFramework.addToTrackMap(selectCardCallback, 6);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(6, selectCardCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"selectCard Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int selectIdv(String string, IdvMethod idvMethod, SelectIdvResponseCallback selectIdvResponseCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"selectIDV(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.selectIdv(string, idvMethod, selectIdvResponseCallback.getPFSelectIdvCb());
                    PaymentFramework.addToTrackMap(selectIdvResponseCallback, 4);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(4, selectIdvResponseCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"selectIDV Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int setConfig(String string, String string2, ConnectionCallback connectionCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"setConfig(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.setConfig(string, string2);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(32, connectionCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"setConfig Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int setJwtToken(String string, ConnectionCallback connectionCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"setJwtToken(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.setJwtToken(string);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(14, connectionCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"setJwtToken Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int setPin(String string, char[] arrc, CommonCallback commonCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"setPin(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.setPin(string, arrc, commonCallback.getICallback());
                    PaymentFramework.addToTrackMap(commonCallback, 40);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(40, commonCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"setPin Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int startGiftCardPay(byte[] arrby, byte[] arrby2, SecuredObject securedObject, PayConfig payConfig, PayCallback payCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"startGiftCardPay(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.startGiftCardPay(arrby, arrby2, securedObject, payConfig, payCallback.getPFPayCb());
                    PaymentFramework.addToTrackMap(payCallback, 26);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(26, payCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"startGiftCardPay Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int startGlobalMembershipCardPay(String string, byte[] arrby, SecuredObject securedObject, PayConfig payConfig, PayCallback payCallback) {
        int n2;
        block6 : {
            block5 : {
                Log.d((String)LOG_TAG, (String)"startGlobalMembershipCardPay(): ");
                n2 = 1;
                if (this.createService()) break block5;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.startGlobalMembershipCardPay(string, arrby, securedObject, payConfig, payCallback.getPFPayCb());
                    if (mTrackOpsHash == null) {
                        mTrackOpsHash = new HashMap();
                    }
                    mTrackOpsHash.put((Object)payCallback, (Object)43);
                    break block6;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(43, payCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"startGlobalMembershipCardPay Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int startInAppPay(SecuredObject securedObject, InAppTransactionInfo inAppTransactionInfo, InAppPayCallback inAppPayCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.i((String)LOG_TAG, (String)"startInAppPay(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.startInAppPay(securedObject, inAppTransactionInfo, inAppPayCallback.getPFInAppPayCb());
                    PaymentFramework.addToTrackMap(inAppPayCallback, 21);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(21, inAppPayCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"startInAppPay Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int startLoyaltyCardPay(LoyaltyCardShowRequest loyaltyCardShowRequest, PayCallback payCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.i((String)LOG_TAG, (String)"startLoyaltyCardPay(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.startLoyaltyCardPay(loyaltyCardShowRequest, payCallback.getPFPayCb());
                    PaymentFramework.addToTrackMap(payCallback, 37);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(37, payCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"startLoyaltyCardPay Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int startPay(SecuredObject securedObject, PayConfig payConfig, PayCallback payCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"startPay(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.startPay(securedObject, payConfig, payCallback.getPFPayCb());
                    PaymentFramework.addToTrackMap(payCallback, 7);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(7, payCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"startPay Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int stopPay(CommonCallback commonCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"stopPay(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.stopPay(commonCallback.getICallback());
                    PaymentFramework.addToTrackMap(commonCallback, 8);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(8, commonCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"stopPay Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int storeUserSignature(String string, byte[] arrby, CommonCallback commonCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"storeUserSignature():");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.storeUserSignature(string, arrby, commonCallback.getICallback());
                    PaymentFramework.addToTrackMap(commonCallback, 30);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(30, commonCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"storeUserSignature Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int synchronizeCards(String string, SynchronizeCardsCallback synchronizeCardsCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)("synchronizeCards(): " + string));
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.synchronizeCards(string, synchronizeCardsCallback.getPFSynchronizeCardsCb());
                    PaymentFramework.addToTrackMap(synchronizeCardsCallback, 20);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(20, synchronizeCardsCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"synchronizeCards Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int updateBinAttribute(String string, String string2, CommonCallback commonCallback) {
        Log.d((String)LOG_TAG, (String)"updateBinAttribute(): ");
        int n2 = 1;
        try {
            if (!this.createService()) {
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            if (commonCallback == null) {
                Log.e((String)LOG_TAG, (String)"callback is null. stop to connect PF");
                return -5;
            }
            if (this.mIPaymentFramework != null) {
                n2 = this.mIPaymentFramework.updateBinAttribute(string, string2, commonCallback.getICallback());
                if (n2 != 0) return n2;
                PaymentFramework.addToTrackMap(commonCallback, 45);
                return n2;
            }
            Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
            this.addRequest(new PaymentFrameworkRequest(45, commonCallback));
            return n2;
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"updateBinAttribute Exception");
            exception.printStackTrace();
            return -2;
        }
    }

    public int updateLoyaltyCard(UpdateLoyaltyCardInfo updateLoyaltyCardInfo, UpdateLoyaltyCardCallback updateLoyaltyCardCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.i((String)LOG_TAG, (String)"updateLoyaltyCard(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.updateLoyaltyCard(updateLoyaltyCardInfo, updateLoyaltyCardCallback.getPFUpdateLoyaltyCallback());
                    PaymentFramework.addToTrackMap(updateLoyaltyCardCallback, 35);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(35, updateLoyaltyCardCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"updateLoyaltyCard Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    public int verifyIdv(String string, VerifyIdvInfo verifyIdvInfo, VerifyIdvResponseCallback verifyIdvResponseCallback) {
        int n2;
        block5 : {
            block4 : {
                Log.d((String)LOG_TAG, (String)"verifyIdv(): ");
                n2 = 1;
                if (this.createService()) break block4;
                Log.e((String)LOG_TAG, (String)"unable to bind to Payment framework");
                return -2;
            }
            try {
                if (this.mIPaymentFramework != null) {
                    n2 = this.mIPaymentFramework.verifyIdv(string, verifyIdvInfo, verifyIdvResponseCallback.getPFVerifyIdvCb());
                    PaymentFramework.addToTrackMap(verifyIdvResponseCallback, 5);
                    break block5;
                }
                Log.w((String)LOG_TAG, (String)"PaymentFramework not ready yet");
                this.addRequest(new PaymentFrameworkRequest(5, verifyIdvResponseCallback));
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"validateIDV Exception");
                exception.printStackTrace();
                n2 = -2;
            }
        }
        return n2;
    }

    private class BindRetryTimerTask
    extends TimerTask {
        private BindRetryTimerTask() {
        }

        /*
         * Enabled aggressive block sorting
         */
        public void run() {
            Log.d((String)PaymentFramework.LOG_TAG, (String)"run : BindRetryTimerTask");
            if (PaymentFramework.this.mIPaymentFramework == null && PaymentFramework.this.mBindCounter >= 0) {
                Log.e((String)PaymentFramework.LOG_TAG, (String)"PFW Bind Timeout. Binder not available. Ping PFW App.");
                Intent intent = new Intent();
                intent.setClassName(PaymentFramework.PAYMENT_FRAMEWORK_PACKAGE_NAME, "com.samsung.android.spayfw.payprovider.TokenReplenishReceiver");
                if (PaymentFramework.this.mContext != null) {
                    PaymentFramework.this.mContext.sendBroadcast(intent);
                }
                PaymentFramework.this.resetBindTimer(false);
                if (!PaymentFramework.this.bindService()) {
                    Log.e((String)PaymentFramework.LOG_TAG, (String)"try retry bind failed.");
                    PaymentFramework.this.notifyApp();
                }
                return;
            }
            if (PaymentFramework.this.mIPaymentFramework != null) {
                Log.i((String)PaymentFramework.LOG_TAG, (String)"PFW Bind Timeout. But Binder Obtained.");
            } else {
                Log.e((String)PaymentFramework.LOG_TAG, (String)"PFW Bind Timeout and counter expired. Notify App");
                PaymentFramework.this.notifyApp();
            }
            PaymentFramework.this.resetBindTimer(true);
        }
    }

    private class PaymentFrameworkRequest {
        PaymentFrameworkConnection callback;
        int operation;

        public PaymentFrameworkRequest(int n2, PaymentFrameworkConnection paymentFrameworkConnection) {
            this.operation = n2;
            this.callback = paymentFrameworkConnection;
        }
    }

}

