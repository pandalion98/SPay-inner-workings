package com.samsung.android.spayfw.appinterface;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.samsung.android.spayfw.appinterface.IPaymentFramework.Stub;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.bouncycastle.jce.X509KeyUsage;

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
    private static final long PFW_BIND_TIMEOUT = 30000;
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
    private int mBindCounter;
    private TimerTask mBindTimerTask;
    private boolean mBound;
    private final ServiceConnection mConn;
    private Context mContext;
    private IPaymentFramework mIPaymentFramework;
    private List<PaymentFrameworkRequest> mPaymentFrameworkRequests;

    /* renamed from: com.samsung.android.spayfw.appinterface.PaymentFramework.1 */
    class C03741 implements ServiceConnection {
        C03741() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(PaymentFramework.LOG_TAG, "Connected! Name: " + componentName.getClassName());
            try {
                PaymentFramework.this.resetBindTimer(true);
                PaymentFramework.this.mBound = true;
                PaymentFramework.this.mIPaymentFramework = Stub.asInterface(iBinder);
                if (PaymentFramework.this.mIPaymentFramework != null) {
                    PaymentFramework.this.notifyAndDeleteRequests();
                    return;
                }
                Log.e(PaymentFramework.LOG_TAG, "unable to bind to Payment framework");
                PaymentFramework.this.notifyApp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(PaymentFramework.LOG_TAG, "Disconnected! Name: " + componentName.getClassName());
            PaymentFramework.this.mBound = false;
            PaymentFramework.this.mIPaymentFramework = null;
            PaymentFramework.this.notifyApp();
        }
    }

    /* renamed from: com.samsung.android.spayfw.appinterface.PaymentFramework.2 */
    class C03752 extends IPayAppDeathDetector.Stub {
        C03752() {
        }
    }

    private class BindRetryTimerTask extends TimerTask {
        private BindRetryTimerTask() {
        }

        public void run() {
            Log.d(PaymentFramework.LOG_TAG, "run : BindRetryTimerTask");
            if (PaymentFramework.this.mIPaymentFramework != null || PaymentFramework.this.mBindCounter < 0) {
                if (PaymentFramework.this.mIPaymentFramework != null) {
                    Log.i(PaymentFramework.LOG_TAG, "PFW Bind Timeout. But Binder Obtained.");
                } else {
                    Log.e(PaymentFramework.LOG_TAG, "PFW Bind Timeout and counter expired. Notify App");
                    PaymentFramework.this.notifyApp();
                }
                PaymentFramework.this.resetBindTimer(true);
                return;
            }
            Log.e(PaymentFramework.LOG_TAG, "PFW Bind Timeout. Binder not available. Ping PFW App.");
            Intent intent = new Intent();
            intent.setClassName(PaymentFramework.PAYMENT_FRAMEWORK_PACKAGE_NAME, "com.samsung.android.spayfw.payprovider.TokenReplenishReceiver");
            if (PaymentFramework.this.mContext != null) {
                PaymentFramework.this.mContext.sendBroadcast(intent);
            }
            PaymentFramework.this.resetBindTimer(false);
            if (!PaymentFramework.this.bindService()) {
                Log.e(PaymentFramework.LOG_TAG, "try retry bind failed.");
                PaymentFramework.this.notifyApp();
            }
        }
    }

    private class PaymentFrameworkRequest {
        PaymentFrameworkConnection callback;
        int operation;

        public PaymentFrameworkRequest(int i, PaymentFrameworkConnection paymentFrameworkConnection) {
            this.operation = i;
            this.callback = paymentFrameworkConnection;
        }
    }

    static {
        mTrackOpsHash = null;
    }

    public static synchronized PaymentFramework getInstance(Context context) {
        PaymentFramework paymentFramework;
        synchronized (PaymentFramework.class) {
            if (context == null) {
                Log.e(LOG_TAG, "context is null");
                paymentFramework = null;
            } else {
                if (mPaymentFramework == null) {
                    mPaymentFramework = new PaymentFramework(context);
                }
                paymentFramework = mPaymentFramework;
            }
        }
        return paymentFramework;
    }

    private static final String getPackageVersion(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, RESULT_CODE_SUCCESS).versionName;
        } catch (NameNotFoundException e) {
            Log.e("getPackageVersion", "Exception = " + e);
            return BuildConfig.FLAVOR;
        }
    }

    private PaymentFramework(Context context) {
        this.mBindCounter = PAY_TYPE_ECM;
        this.mConn = new C03741();
        this.mContext = context.getApplicationContext();
    }

    public int enrollCard(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo, EnrollCardCallback enrollCardCallback) {
        int enrollCard;
        Exception e;
        Throwable th;
        int i = RESULT_CODE_FAIL_PFW_BIND;
        Log.d(LOG_TAG, "enrollCard(): ");
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    enrollCard = this.mIPaymentFramework.enrollCard(enrollCardInfo, billingInfo, enrollCardCallback.getPFEnrollCb());
                    try {
                        addToTrackMap(enrollCardCallback, RESULT_CODE_WAIT_INIT_FRAMEWORK);
                        i = enrollCard;
                    } catch (Exception e2) {
                        e = e2;
                        try {
                            Log.e(LOG_TAG, "enollCard Exception");
                        } catch (Throwable th2) {
                            th = th2;
                            if (enrollCardInfo == null) {
                                Log.d(LOG_TAG, "enrollCard(): enrollInfo is null ");
                            } else if (enrollCard == RESULT_CODE_WAIT_INIT_FRAMEWORK && enrollCard != 0) {
                                enrollCardInfo.decrementRefCount();
                            } else if (enrollCardCallback != null) {
                                enrollCardCallback.setEnrollCardInfo(enrollCardInfo);
                            }
                            throw th;
                        }
                        try {
                            e.printStackTrace();
                            if (enrollCardInfo == null) {
                                Log.d(LOG_TAG, "enrollCard(): enrollInfo is null ");
                            } else {
                                enrollCardInfo.decrementRefCount();
                            }
                            return i;
                        } catch (Throwable th3) {
                            enrollCard = RESULT_CODE_FAIL_PFW_BIND;
                            th = th3;
                            if (enrollCardInfo == null) {
                                Log.d(LOG_TAG, "enrollCard(): enrollInfo is null ");
                            } else {
                                if (enrollCard == RESULT_CODE_WAIT_INIT_FRAMEWORK) {
                                }
                                if (enrollCardCallback != null) {
                                    enrollCardCallback.setEnrollCardInfo(enrollCardInfo);
                                }
                            }
                            throw th;
                        }
                    }
                }
                Log.w(LOG_TAG, "PaymentFramework not ready yet");
                addRequest(new PaymentFrameworkRequest(RESULT_CODE_WAIT_INIT_FRAMEWORK, enrollCardCallback));
                i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
                if (enrollCardInfo == null) {
                    Log.d(LOG_TAG, "enrollCard(): enrollInfo is null ");
                } else if (i != RESULT_CODE_WAIT_INIT_FRAMEWORK && i != 0) {
                    enrollCardInfo.decrementRefCount();
                } else if (enrollCardCallback != null) {
                    enrollCardCallback.setEnrollCardInfo(enrollCardInfo);
                }
            } else {
                Log.e(LOG_TAG, "unable to bind to Payment framework");
                if (enrollCardInfo != null) {
                    enrollCardInfo.decrementRefCount();
                } else {
                    Log.d(LOG_TAG, "enrollCard(): enrollInfo is null ");
                }
            }
        } catch (Exception e3) {
            e = e3;
            enrollCard = RESULT_CODE_WAIT_INIT_FRAMEWORK;
            Log.e(LOG_TAG, "enollCard Exception");
            e.printStackTrace();
            if (enrollCardInfo == null) {
                enrollCardInfo.decrementRefCount();
            } else {
                Log.d(LOG_TAG, "enrollCard(): enrollInfo is null ");
            }
            return i;
        } catch (Throwable th4) {
            th = th4;
            enrollCard = RESULT_CODE_WAIT_INIT_FRAMEWORK;
            if (enrollCardInfo == null) {
                if (enrollCard == RESULT_CODE_WAIT_INIT_FRAMEWORK) {
                }
                if (enrollCardCallback != null) {
                    enrollCardCallback.setEnrollCardInfo(enrollCardInfo);
                }
            } else {
                Log.d(LOG_TAG, "enrollCard(): enrollInfo is null ");
            }
            throw th;
        }
        return i;
    }

    public int acceptTnC(String str, boolean z, CommonCallback commonCallback) {
        Log.d(LOG_TAG, "acceptTnC(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.acceptTnC(str, z, commonCallback.getICallback());
                    addToTrackMap(commonCallback, RESULT_CODE_WAIT_UPDATE_FRAMEWORK);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(RESULT_CODE_WAIT_UPDATE_FRAMEWORK, commonCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "acceptTnC Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int provisionToken(String str, ProvisionTokenInfo provisionTokenInfo, ProvisionTokenCallback provisionTokenCallback) {
        Log.d(LOG_TAG, "provisionToken(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.provisionToken(str, provisionTokenInfo, provisionTokenCallback.getPFProvisionCb());
                    addToTrackMap(provisionTokenCallback, PAYFW_OPT_PROVISION);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_PROVISION, provisionTokenCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "provisionToken Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public TokenStatus getTokenStatus(String str, ConnectionCallback connectionCallback) {
        try {
            Log.d(LOG_TAG, "mBound =  " + this.mBound);
            if (!createService()) {
                Log.e(LOG_TAG, "unable to bind to Payment framework");
                if (connectionCallback == null) {
                    return null;
                }
                connectionCallback.onError(str, RESULT_CODE_FAIL_PFW_BIND);
                return null;
            } else if (this.mIPaymentFramework != null) {
                return this.mIPaymentFramework.getTokenStatus(str);
            } else {
                Log.w(LOG_TAG, "PaymentFramework not ready yet");
                addRequest(new PaymentFrameworkRequest(PAYFW_OPT_GET_TOKEN_STATUS, connectionCallback));
                return null;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "getTokenStatus Exception");
            e.printStackTrace();
            return null;
        }
    }

    public int selectIdv(String str, IdvMethod idvMethod, SelectIdvResponseCallback selectIdvResponseCallback) {
        Log.d(LOG_TAG, "selectIDV(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.selectIdv(str, idvMethod, selectIdvResponseCallback.getPFSelectIdvCb());
                    addToTrackMap(selectIdvResponseCallback, PAY_TYPE_ECM);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAY_TYPE_ECM, selectIdvResponseCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "selectIDV Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int verifyIdv(String str, VerifyIdvInfo verifyIdvInfo, VerifyIdvResponseCallback verifyIdvResponseCallback) {
        Log.d(LOG_TAG, "verifyIdv(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.verifyIdv(str, verifyIdvInfo, verifyIdvResponseCallback.getPFVerifyIdvCb());
                    addToTrackMap(verifyIdvResponseCallback, PAYFW_OPT_VERIFY_IDV);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_VERIFY_IDV, verifyIdvResponseCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "validateIDV Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int clearCard(ConnectionCallback connectionCallback) {
        Log.d(LOG_TAG, "clearCard(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    this.mIPaymentFramework.clearCard();
                    i = RESULT_CODE_SUCCESS;
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_CLEAR_CARD, connectionCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "clearCard Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int selectCard(String str, SelectCardCallback selectCardCallback) {
        Log.d(LOG_TAG, "selectCard(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.selectCard(str, selectCardCallback.getPFSelectCardCb());
                    addToTrackMap(selectCardCallback, PAYFW_OPT_SELECT_CARD);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_SELECT_CARD, selectCardCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "selectCard Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int startPay(SecuredObject securedObject, PayConfig payConfig, PayCallback payCallback) {
        Log.d(LOG_TAG, "startPay(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.startPay(securedObject, payConfig, payCallback.getPFPayCb());
                    addToTrackMap(payCallback, PAYFW_OPT_START_PAY);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_START_PAY, payCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "startPay Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int retryPay(PayConfig payConfig) {
        Log.d(LOG_TAG, "retryPay(): ");
        try {
            if (this.mIPaymentFramework != null) {
                return this.mIPaymentFramework.retryPay(payConfig);
            }
            Log.e(LOG_TAG, "PaymentFramework not ready yet, should not happen.");
            return RESULT_CODE_WAIT_INIT_FRAMEWORK;
        } catch (Exception e) {
            Exception exception = e;
            Log.e(LOG_TAG, "startPay Exception");
            exception.printStackTrace();
            return RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int stopPay(CommonCallback commonCallback) {
        Log.d(LOG_TAG, "stopPay(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.stopPay(commonCallback.getICallback());
                    addToTrackMap(commonCallback, PAYFW_OPT_STOP_PAY);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_STOP_PAY, commonCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "stopPay Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int processPushMessage(PushMessage pushMessage, PushMessageCallback pushMessageCallback) {
        Log.d(LOG_TAG, "processPushMessage(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.processPushMessage(pushMessage, pushMessageCallback.getPFPushMsgCb());
                    addToTrackMap(pushMessageCallback, PAYFW_OPT_PUSH_HANDLE);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_PUSH_HANDLE, pushMessageCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "processPushMessage Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int getPaymentFrameworkInterfaceVersion() {
        int i;
        NameNotFoundException e;
        NullPointerException e2;
        Exception e3;
        try {
            i = this.mContext.getPackageManager().getApplicationInfo(PAYMENT_FRAMEWORK_PACKAGE_NAME, X509KeyUsage.digitalSignature).metaData.getInt("com.samsung.android.spayfw.interface_version", RESULT_CODE_FAIL_PFW_INTERNAL);
            if (i == RESULT_CODE_FAIL_PFW_INTERNAL) {
                try {
                    String packageVersion = getPackageVersion(this.mContext, PAYMENT_FRAMEWORK_PACKAGE_NAME);
                    if (!(packageVersion == null || packageVersion.isEmpty())) {
                        String[] split = packageVersion.split("\\.");
                        if (split != null && split.length > RESULT_CODE_WAIT_UPDATE_FRAMEWORK) {
                            i = Integer.parseInt(split[RESULT_CODE_WAIT_INIT_FRAMEWORK]);
                        }
                    }
                } catch (NameNotFoundException e4) {
                    e = e4;
                    Log.e(LOG_TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
                    return i;
                } catch (NullPointerException e5) {
                    e2 = e5;
                    Log.e(LOG_TAG, "Failed to load meta-data, NullPointer: " + e2.getMessage());
                    return i;
                } catch (Exception e6) {
                    e3 = e6;
                    Log.e(LOG_TAG, e3.getMessage());
                    return i;
                }
            }
        } catch (NameNotFoundException e7) {
            NameNotFoundException nameNotFoundException = e7;
            i = RESULT_CODE_FAIL_PFW_INTERNAL;
            e = nameNotFoundException;
            Log.e(LOG_TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
            return i;
        } catch (NullPointerException e8) {
            NullPointerException nullPointerException = e8;
            i = RESULT_CODE_FAIL_PFW_INTERNAL;
            e2 = nullPointerException;
            Log.e(LOG_TAG, "Failed to load meta-data, NullPointer: " + e2.getMessage());
            return i;
        } catch (Exception e9) {
            Exception exception = e9;
            i = RESULT_CODE_FAIL_PFW_INTERNAL;
            e3 = exception;
            Log.e(LOG_TAG, e3.getMessage());
            return i;
        }
        return i;
    }

    public int getPaymentFrameworkInterfaceVersionFromMetadata() {
        int i = RESULT_CODE_FAIL_PFW_INTERNAL;
        try {
            i = this.mContext.getPackageManager().getApplicationInfo(PAYMENT_FRAMEWORK_PACKAGE_NAME, X509KeyUsage.digitalSignature).metaData.getInt("com.samsung.android.spayfw.interface_version", RESULT_CODE_FAIL_PFW_INTERNAL);
        } catch (NameNotFoundException e) {
            Log.e(LOG_TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e2) {
            Log.e(LOG_TAG, "Failed to load meta-data, NullPointer: " + e2.getMessage());
        } catch (Exception e3) {
            Log.e(LOG_TAG, e3.getMessage());
        }
        return i;
    }

    public int getInterfaceVersion() {
        return INTERFACE_VERSION;
    }

    public int getCardAttribute(String str, boolean z, CardAttributeCallback cardAttributeCallback) {
        Log.d(LOG_TAG, "getCardAttribute(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.getCardAttributes(str, z, cardAttributeCallback.getICallback());
                    addToTrackMap(cardAttributeCallback, PAYFW_OPT_GET_CARD_ATTRIBUTE);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_GET_CARD_ATTRIBUTE, cardAttributeCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "notifyDeviceReset Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int setJwtToken(String str, ConnectionCallback connectionCallback) {
        Log.d(LOG_TAG, "setJwtToken(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.setJwtToken(str);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_SET_JWT_TOKEN, connectionCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "setJwtToken Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int reset(String str, CommonCallback commonCallback) {
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        Log.d(LOG_TAG, "reset(): " + str);
        try {
            if (createService()) {
                String packageVersion = getPackageVersion(this.mContext, PAYMENT_FRAMEWORK_PACKAGE_NAME);
                Log.d(LOG_TAG, "pf_version = " + packageVersion);
                if (packageVersion == null || !(packageVersion.equals(PF_STUB_APP_VERSION) || packageVersion.equals(PF_STUB_APP_VERSION_CSC))) {
                    int paymentFrameworkInterfaceVersionFromMetadata = getPaymentFrameworkInterfaceVersionFromMetadata();
                    Log.d(LOG_TAG, "Interface Version = " + paymentFrameworkInterfaceVersionFromMetadata);
                    if (paymentFrameworkInterfaceVersionFromMetadata == RESULT_CODE_WAIT_INIT_FRAMEWORK) {
                        Log.d(LOG_TAG, "Device has Stub PF. Data already cleared");
                        return RESULT_CODE_FAIL_STUB_PF;
                    }
                    Log.d(LOG_TAG, "Device does not have stub PF");
                    if (this.mIPaymentFramework != null) {
                        i = this.mIPaymentFramework.reset(str, commonCallback.getICallback());
                        addToTrackMap(commonCallback, PAYFW_OPT_NOTIFY_DEVICE_RESET);
                    } else {
                        Log.w(LOG_TAG, "PaymentFramework not ready yet");
                        addRequest(new PaymentFrameworkRequest(PAYFW_OPT_NOTIFY_DEVICE_RESET, commonCallback));
                    }
                    return i;
                }
                Log.d(LOG_TAG, "Device has Stub PF. Data already cleared");
                return RESULT_CODE_FAIL_STUB_PF;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "notifyDeviceReset Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public List<CardState> getAllCardState(ConnectionCallback connectionCallback) {
        Bundle bundle = new Bundle();
        String[] strArr = new String[PAY_TYPE_ECM];
        strArr[RESULT_CODE_SUCCESS] = CARD_TYPE_CREDIT;
        strArr[RESULT_CODE_WAIT_INIT_FRAMEWORK] = CARD_TYPE_DEBIT;
        strArr[RESULT_CODE_WAIT_UPDATE_FRAMEWORK] = CARD_TYPE_SAMSUNG_REWARD;
        strArr[PAYFW_OPT_PROVISION] = CARD_TYPE_GLOBAL_MEMBERSHIP;
        bundle.putStringArray(EXTRA_CARD_TYPE, strArr);
        return getAllCardState(bundle, connectionCallback);
    }

    public List<CardState> getAllCardState(Bundle bundle, ConnectionCallback connectionCallback) {
        try {
            if (!createService()) {
                Log.e(LOG_TAG, "unable to bind to Payment framework");
                if (connectionCallback == null) {
                    return null;
                }
                connectionCallback.onError(null, RESULT_CODE_FAIL_PFW_BIND);
                return null;
            } else if (this.mIPaymentFramework != null) {
                return this.mIPaymentFramework.getAllCardState(bundle);
            } else {
                Log.w(LOG_TAG, "PaymentFramework not ready yet");
                addRequest(new PaymentFrameworkRequest(PAYFW_OPT_GET_ALL_TOKEN_STATE, connectionCallback));
                if (connectionCallback == null) {
                    return null;
                }
                connectionCallback.onError(null, RESULT_CODE_WAIT_INIT_FRAMEWORK);
                return null;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "getTokenState Exception");
            return null;
        }
    }

    public int clearEnrolledCard(String str, ConnectionCallback connectionCallback) {
        Log.d(LOG_TAG, "clearEnrolledCard(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    this.mIPaymentFramework.clearEnrolledCard(str);
                    i = RESULT_CODE_SUCCESS;
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_CLEAR_ENROLLED_CARD, connectionCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "clearEnrolledCard Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public List<String> getPaymentReadyState(String str, ConnectionCallback connectionCallback) {
        Log.d(LOG_TAG, "getPayAvailableTokens(): ");
        try {
            if (!createService()) {
                Log.e(LOG_TAG, "unable to bind to Payment framework");
                if (connectionCallback == null) {
                    return null;
                }
                connectionCallback.onError(null, RESULT_CODE_FAIL_PFW_BIND);
                return null;
            } else if (this.mIPaymentFramework != null) {
                return this.mIPaymentFramework.getPaymentReadyState(str);
            } else {
                Log.w(LOG_TAG, "PaymentFramework not ready yet");
                addRequest(new PaymentFrameworkRequest(PAYFW_OPT_GET_PAY_STATE, connectionCallback));
                return null;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "clearEnrolledCard Exception");
            e.printStackTrace();
            return null;
        }
    }

    public int getCardData(String str, CardDataCallback cardDataCallback) {
        Log.d(LOG_TAG, "getTokenData(): " + str);
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.getCardData(str, cardDataCallback.getPFCardDataCb());
                    addToTrackMap(cardDataCallback, PAYFW_OPT_GET_TOKEN_DATA);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_GET_TOKEN_DATA, cardDataCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "getTokenData Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int deleteCard(String str, DeleteCardCallback deleteCardCallback) {
        return deleteCard(str, null, deleteCardCallback);
    }

    public int deleteCard(String str, Bundle bundle, DeleteCardCallback deleteCardCallback) {
        Log.d(LOG_TAG, "deleteCard(): enrollId " + str);
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.deleteCard(str, bundle, deleteCardCallback.getPFDeleteCardCb());
                    addToTrackMap(deleteCardCallback, PAYFW_OPT_DELETE_CARD);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_DELETE_CARD, deleteCardCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "deleteCard Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int synchronizeCards(String str, SynchronizeCardsCallback synchronizeCardsCallback) {
        Log.d(LOG_TAG, "synchronizeCards(): " + str);
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.synchronizeCards(str, synchronizeCardsCallback.getPFSynchronizeCardsCb());
                    addToTrackMap(synchronizeCardsCallback, PAYFW_OPT_SYNCHRONIZ_CARDS);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_SYNCHRONIZ_CARDS, synchronizeCardsCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "synchronizeCards Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int startInAppPay(SecuredObject securedObject, InAppTransactionInfo inAppTransactionInfo, InAppPayCallback inAppPayCallback) {
        Log.i(LOG_TAG, "startInAppPay(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.startInAppPay(securedObject, inAppTransactionInfo, inAppPayCallback.getPFInAppPayCb());
                    addToTrackMap(inAppPayCallback, PAYFW_OPT_START_IN_APP_PAY);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_START_IN_APP_PAY, inAppPayCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "startInAppPay Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int getLogs(CommonCallback commonCallback) {
        int i = RESULT_CODE_FAIL_PFW_BIND;
        Log.d(LOG_TAG, "getLogs()");
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    File file = new File(this.mContext.getFilesDir(), PAYMENT_FRAMEWORK_DIRECTORY);
                    file.mkdirs();
                    File file2 = new File(file, PAYMENT_FRAMEWORK_LOG_FILE);
                    parcelFileDescriptor = ParcelFileDescriptor.open(file2, 738197504);
                    int logs = this.mIPaymentFramework.getLogs(parcelFileDescriptor, file2.getAbsolutePath(), commonCallback.getICallback());
                    addToTrackMap(commonCallback, PAYFW_OPT_GET_LOGS);
                    i = logs;
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_GET_LOGS, commonCallback));
                    i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
                }
                if (parcelFileDescriptor != null) {
                    try {
                        parcelFileDescriptor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Log.e(LOG_TAG, "unable to bind to Payment framework");
                if (parcelFileDescriptor != null) {
                    try {
                        parcelFileDescriptor.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        } catch (Exception e22) {
            Log.e(LOG_TAG, "getLogs Exception");
            e22.printStackTrace();
            if (parcelFileDescriptor != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (Exception e222) {
                    e222.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (parcelFileDescriptor != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (Exception e2222) {
                    e2222.printStackTrace();
                }
            }
        }
        return i;
    }

    public int getEnvironment(CommonCallback commonCallback) {
        Log.d(LOG_TAG, "getEnvironment(): ");
        try {
            if (!createService()) {
                Log.e(LOG_TAG, "unable to bind to Payment framework");
                return RESULT_CODE_FAIL_PFW_BIND;
            } else if (this.mIPaymentFramework != null) {
                return this.mIPaymentFramework.getEnvironment(commonCallback.getICallback());
            } else {
                Log.w(LOG_TAG, "PaymentFramework not ready yet");
                addRequest(new PaymentFrameworkRequest(PAYFW_OPT_GET_ENVIRONMENT, commonCallback));
                return RESULT_CODE_WAIT_INIT_FRAMEWORK;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "getEnvironment Exception");
            e.printStackTrace();
            return RESULT_CODE_WAIT_INIT_FRAMEWORK;
        }
    }

    public int getGiftCardRegisterData(GiftCardRegisterRequestData giftCardRegisterRequestData, GiftCardRegisterCallback giftCardRegisterCallback) {
        Log.i(LOG_TAG, "getGiftCardRegisterData(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.getGiftCardRegisterData(giftCardRegisterRequestData, giftCardRegisterCallback.getPFGiftCardRegisterCb());
                    addToTrackMap(giftCardRegisterCallback, PAYFW_OPT_START_GET_GIFTCARD_REGISTERDATA);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_START_GET_GIFTCARD_REGISTERDATA, giftCardRegisterCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "getGiftCardRegisterData Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int getGiftCardTzEncData(GiftCardRegisterRequestData giftCardRegisterRequestData, GiftCardRegisterCallback giftCardRegisterCallback) {
        Log.i(LOG_TAG, "getGiftCardTZEncData(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.getGiftCardTzEncData(giftCardRegisterRequestData, giftCardRegisterCallback.getPFGiftCardRegisterCb());
                    addToTrackMap(giftCardRegisterCallback, PAYFW_OPT_START_GET_GIFTCARD_TZENCDATA);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_START_GET_GIFTCARD_TZENCDATA, giftCardRegisterCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "getGiftCardTZEncData(): Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int startGiftCardPay(byte[] bArr, byte[] bArr2, SecuredObject securedObject, PayConfig payConfig, PayCallback payCallback) {
        Log.d(LOG_TAG, "startGiftCardPay(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.startGiftCardPay(bArr, bArr2, securedObject, payConfig, payCallback.getPFPayCb());
                    addToTrackMap(payCallback, PAYFW_OPT_START_GIFTCARD_PAY);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_START_GIFTCARD_PAY, payCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "startGiftCardPay Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int extractGiftCardDetail(ExtractGiftCardDetailRequest extractGiftCardDetailRequest, SecuredObject securedObject, GiftCardExtractDetailCallback giftCardExtractDetailCallback) {
        Log.d(LOG_TAG, "extractGiftCardDetail(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.extractGiftCardDetail(extractGiftCardDetailRequest, securedObject, giftCardExtractDetailCallback.getPFGiftCardExtractDetailCb());
                    addToTrackMap(giftCardExtractDetailCallback, PAYFW_OPT_EXTRACT_GIFTCARD_DETAIL);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_EXTRACT_GIFTCARD_DETAIL, giftCardExtractDetailCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "extractGiftCardDetail() Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public List<String> isDsrpBlobMissing(ConnectionCallback connectionCallback) {
        List<String> arrayList = new ArrayList();
        try {
            if (!createService()) {
                Log.e(LOG_TAG, "unable to bind to Payment framework");
                if (connectionCallback == null) {
                    return arrayList;
                }
                connectionCallback.onError(null, RESULT_CODE_FAIL_PFW_BIND);
                return arrayList;
            } else if (this.mIPaymentFramework != null) {
                return this.mIPaymentFramework.isDsrpBlobMissing();
            } else {
                Log.w(LOG_TAG, "PaymentFramework not ready yet");
                addRequest(new PaymentFrameworkRequest(PAYFW_OPT_IS_DSRP_BLOB_MISSING, connectionCallback));
                if (connectionCallback == null) {
                    return arrayList;
                }
                connectionCallback.onError(null, RESULT_CODE_WAIT_INIT_FRAMEWORK);
                return arrayList;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "isDsrpBlobMissing Exception");
            return arrayList;
        }
    }

    public int isDsrpBlobMissingForTokenId(String str, ConnectionCallback connectionCallback) {
        try {
            if (!createService()) {
                Log.e(LOG_TAG, "unable to bind to Payment framework");
                if (connectionCallback != null) {
                    connectionCallback.onError(null, RESULT_CODE_FAIL_PFW_BIND);
                }
                return RESULT_CODE_FAIL_PFW_BIND;
            } else if (this.mIPaymentFramework != null) {
                return this.mIPaymentFramework.isDsrpBlobMissingForTokenId(str);
            } else {
                Log.w(LOG_TAG, "PaymentFramework not ready yet");
                addRequest(new PaymentFrameworkRequest(PAYFW_OPT_IS_DSRP_BLOB_MISSING_FOR_TOKENID, connectionCallback));
                if (connectionCallback == null) {
                    return RESULT_CODE_SUCCESS;
                }
                connectionCallback.onError(null, RESULT_CODE_WAIT_INIT_FRAMEWORK);
                return RESULT_CODE_SUCCESS;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "isDsrpBlobMissingForTokenId Exception");
            return RESULT_CODE_SUCCESS;
        }
    }

    public int storeUserSignature(String str, byte[] bArr, CommonCallback commonCallback) {
        Log.d(LOG_TAG, "storeUserSignature():");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.storeUserSignature(str, bArr, commonCallback.getICallback());
                    addToTrackMap(commonCallback, PAYFW_OPT_STORE_USER_SIGNATURE);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_STORE_USER_SIGNATURE, commonCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "storeUserSignature Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int getUserSignature(String str, UserSignatureCallback userSignatureCallback) {
        Log.d(LOG_TAG, "getUserSignature():");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.getUserSignature(str, userSignatureCallback.getICallback());
                    addToTrackMap(userSignatureCallback, PAYFW_OPT_GET_USER_SIGNATURE);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_GET_USER_SIGNATURE, userSignatureCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "getUserSignature Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int setConfig(String str, String str2, ConnectionCallback connectionCallback) {
        Log.d(LOG_TAG, "setConfig(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.setConfig(str, str2);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_SET_CONFIG, connectionCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "setConfig Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public String getConfig(String str, ConnectionCallback connectionCallback) {
        Log.d(LOG_TAG, "getConfig(): ");
        try {
            if (!createService()) {
                Log.e(LOG_TAG, "unable to bind to Payment framework");
                if (connectionCallback == null) {
                    return null;
                }
                connectionCallback.onError(null, RESULT_CODE_FAIL_PFW_BIND);
                return null;
            } else if (this.mIPaymentFramework != null) {
                return this.mIPaymentFramework.getConfig(str);
            } else {
                Log.w(LOG_TAG, "PaymentFramework not ready yet");
                addRequest(new PaymentFrameworkRequest(PAYFW_OPT_GET_CONFIG, connectionCallback));
                if (connectionCallback == null) {
                    return null;
                }
                connectionCallback.onError(null, RESULT_CODE_WAIT_INIT_FRAMEWORK);
                return null;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "getConfig Exception");
            if (connectionCallback != null) {
                connectionCallback.onError(null, RESULT_CODE_FAIL_PFW_BIND);
            }
            e.printStackTrace();
            return null;
        }
    }

    public int processServerRequest(ServerRequest serverRequest, ServerResponseCallback serverResponseCallback) {
        File file = null;
        Log.i(LOG_TAG, "processServerRequest(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    ParcelFileDescriptor parcelFileDescriptor;
                    if (serverRequest == null || serverRequest.getBody() == null) {
                        parcelFileDescriptor = null;
                    } else {
                        File writeStringToFile = writeStringToFile(serverRequest.getBody(), "serverRequest_");
                        if (writeStringToFile != null) {
                            ParcelFileDescriptor open = ParcelFileDescriptor.open(writeStringToFile, 268435456);
                            serverRequest.setFd(open);
                            parcelFileDescriptor = open;
                            file = writeStringToFile;
                        } else {
                            parcelFileDescriptor = null;
                            file = writeStringToFile;
                        }
                    }
                    i = this.mIPaymentFramework.processServerRequest(serverRequest, serverResponseCallback.getPFServerCallback());
                    if (parcelFileDescriptor != null) {
                        parcelFileDescriptor.close();
                    }
                    if (file != null) {
                        file.delete();
                    }
                    addToTrackMap(serverResponseCallback, PAYFW_OPT_PROCESS_SERVER_REQUEST);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_PROCESS_SERVER_REQUEST, serverResponseCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "processServerRequest Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int updateLoyaltyCard(UpdateLoyaltyCardInfo updateLoyaltyCardInfo, UpdateLoyaltyCardCallback updateLoyaltyCardCallback) {
        Log.i(LOG_TAG, "updateLoyaltyCard(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.updateLoyaltyCard(updateLoyaltyCardInfo, updateLoyaltyCardCallback.getPFUpdateLoyaltyCallback());
                    addToTrackMap(updateLoyaltyCardCallback, PAYFW_OPT_UPDATE_LOYALTY_CARD);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_UPDATE_LOYALTY_CARD, updateLoyaltyCardCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "updateLoyaltyCard Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int extractLoyaltyCardDetails(ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest, ExtractLoyaltyCardDetailResponseCallback extractLoyaltyCardDetailResponseCallback) {
        Log.i(LOG_TAG, "extractLoyaltyCardDetails(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.extractLoyaltyCardDetails(extractLoyaltyCardDetailRequest, extractLoyaltyCardDetailResponseCallback.getPFExtractLoyaltyCardDetailCallback());
                    addToTrackMap(extractLoyaltyCardDetailResponseCallback, PAYFW_OPT_EXTRACT_LOYALTY_CARD_DETAIL);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_EXTRACT_LOYALTY_CARD_DETAIL, extractLoyaltyCardDetailResponseCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "extractLoyaltyCardDetails Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int startLoyaltyCardPay(LoyaltyCardShowRequest loyaltyCardShowRequest, PayCallback payCallback) {
        Log.i(LOG_TAG, "startLoyaltyCardPay(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.startLoyaltyCardPay(loyaltyCardShowRequest, payCallback.getPFPayCb());
                    addToTrackMap(payCallback, PAYFW_OPT_START_LOYALTY_CARD_PAY);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_START_LOYALTY_CARD_PAY, payCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "startLoyaltyCardPay Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int getTransactionDetails(String str, long j, long j2, int i, TransactionDetailsCallback transactionDetailsCallback) {
        Log.d(LOG_TAG, "getTransactionDetails(): ");
        try {
            if (!createService()) {
                Log.e(LOG_TAG, "unable to bind to Payment framework");
                return RESULT_CODE_FAIL_PFW_BIND;
            } else if (this.mIPaymentFramework != null) {
                int transactionDetails = this.mIPaymentFramework.getTransactionDetails(str, j, j2, i, transactionDetailsCallback.getPFTransactionDetailsCb());
                addToTrackMap(transactionDetailsCallback, PAYFW_OPT_GET_TRANSACTION_DETAILS);
                return transactionDetails;
            } else {
                Log.w(LOG_TAG, "PaymentFramework not ready yet");
                addRequest(new PaymentFrameworkRequest(PAYFW_OPT_GET_TRANSACTION_DETAILS, transactionDetailsCallback));
                return RESULT_CODE_WAIT_INIT_FRAMEWORK;
            }
        } catch (Exception e) {
            Exception exception = e;
            Log.e(LOG_TAG, "getTransactionDetails Exception");
            exception.printStackTrace();
            return RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int refreshIdv(String str, RefreshIdvResponseCallback refreshIdvResponseCallback) {
        Log.d(LOG_TAG, "refreshIdv(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.refreshIdv(str, refreshIdvResponseCallback.getPFRefreshIdvCallback());
                    addToTrackMap(refreshIdvResponseCallback, PAYFW_OPT_REFRESH_IDV);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_REFRESH_IDV, refreshIdvResponseCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "selectIDV Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int setPin(String str, char[] cArr, CommonCallback commonCallback) {
        Log.d(LOG_TAG, "setPin(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.setPin(str, cArr, commonCallback.getICallback());
                    addToTrackMap(commonCallback, PAYFW_OPT_SET_PIN);
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_SET_PIN, commonCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "setPin Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int getGlobalMembershipCardRegisterData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, GlobalMembershipCardRegisterCallback globalMembershipCardRegisterCallback) {
        Log.i(LOG_TAG, "getGlobalMembershipCardRegisterData(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.getGlobalMembershipCardRegisterData(globalMembershipCardRegisterRequestData, globalMembershipCardRegisterCallback.getPFGlobalMembershipCardRegisterCb());
                    if (mTrackOpsHash == null) {
                        mTrackOpsHash = new HashMap();
                    }
                    mTrackOpsHash.put(globalMembershipCardRegisterCallback, Integer.valueOf(PAYFW_OPT_GET_GLOBALMEMBERSHIP_CARD_REGISTERDATA));
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_GET_GLOBALMEMBERSHIP_CARD_REGISTERDATA, globalMembershipCardRegisterCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "getGlobalMembershipCardRegisterData Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int getGlobalMembershipCardTzEncData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, GlobalMembershipCardRegisterCallback globalMembershipCardRegisterCallback) {
        Log.i(LOG_TAG, "getGlobalMembershipCardTZEncData(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.getGlobalMembershipCardTzEncData(globalMembershipCardRegisterRequestData, globalMembershipCardRegisterCallback.getPFGlobalMembershipCardRegisterCb());
                    if (mTrackOpsHash == null) {
                        mTrackOpsHash = new HashMap();
                    }
                    mTrackOpsHash.put(globalMembershipCardRegisterCallback, Integer.valueOf(PAYFW_OPT_GET_GLOBALMEMBERSHIP_CARD_TZENCDATA));
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_GET_GLOBALMEMBERSHIP_CARD_TZENCDATA, globalMembershipCardRegisterCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "getGlobalMembershipCardTZEncData(): Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int startGlobalMembershipCardPay(String str, byte[] bArr, SecuredObject securedObject, PayConfig payConfig, PayCallback payCallback) {
        Log.d(LOG_TAG, "startGlobalMembershipCardPay(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.startGlobalMembershipCardPay(str, bArr, securedObject, payConfig, payCallback.getPFPayCb());
                    if (mTrackOpsHash == null) {
                        mTrackOpsHash = new HashMap();
                    }
                    mTrackOpsHash.put(payCallback, Integer.valueOf(PAYFW_OPT_START_GLOBALMEMBERSHIP_CARD_PAY));
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_START_GLOBALMEMBERSHIP_CARD_PAY, payCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "startGlobalMembershipCardPay Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int extractGlobalMembershipCardDetail(List<ExtractGlobalMembershipCardDetailRequest> list, GlobalMembershipCardExtractDetailCallback globalMembershipCardExtractDetailCallback) {
        Log.d(LOG_TAG, "extractGlobalMembershipCardDetail(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (createService()) {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.extractGlobalMembershipCardDetail(list, globalMembershipCardExtractDetailCallback.getPFGlobalMembershipCardExtractDetailCb());
                    if (mTrackOpsHash == null) {
                        mTrackOpsHash = new HashMap();
                    }
                    mTrackOpsHash.put(globalMembershipCardExtractDetailCallback, Integer.valueOf(PAYFW_OPT_EXTRACT_GLOBALMEMBERSHIP_CARD_DETAIL));
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_EXTRACT_GLOBALMEMBERSHIP_CARD_DETAIL, globalMembershipCardExtractDetailCallback));
                }
                return i;
            }
            Log.e(LOG_TAG, "unable to bind to Payment framework");
            return RESULT_CODE_FAIL_PFW_BIND;
        } catch (Exception e) {
            Log.e(LOG_TAG, "extractGlobalMembershipCardDetail() Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public int updateBinAttribute(String str, String str2, CommonCallback commonCallback) {
        Log.d(LOG_TAG, "updateBinAttribute(): ");
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (!createService()) {
                Log.e(LOG_TAG, "unable to bind to Payment framework");
                return RESULT_CODE_FAIL_PFW_BIND;
            } else if (commonCallback == null) {
                Log.e(LOG_TAG, "callback is null. stop to connect PF");
                return RESULT_CODE_FAIL_INVALID_INPUT;
            } else {
                if (this.mIPaymentFramework != null) {
                    i = this.mIPaymentFramework.updateBinAttribute(str, str2, commonCallback.getICallback());
                    if (i == 0) {
                        addToTrackMap(commonCallback, PAYFW_OPT_UPDATE_BIN_ATTRIBUTE);
                    }
                } else {
                    Log.w(LOG_TAG, "PaymentFramework not ready yet");
                    addRequest(new PaymentFrameworkRequest(PAYFW_OPT_UPDATE_BIN_ATTRIBUTE, commonCallback));
                }
                return i;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "updateBinAttribute Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
    }

    public CommonSpayResponse processSpayApdu(byte[] bArr, Bundle bundle) {
        Log.d(LOG_TAG, "processSpayApdu(): ");
        CommonSpayResponse commonSpayResponse = null;
        int i = RESULT_CODE_WAIT_INIT_FRAMEWORK;
        try {
            if (this.mIPaymentFramework != null) {
                commonSpayResponse = this.mIPaymentFramework.processSpayApdu(bArr, bundle);
            } else {
                Log.e(LOG_TAG, "PaymentFramework not ready yet, should not happen.");
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "processSpayApdu Exception");
            e.printStackTrace();
            i = RESULT_CODE_FAIL_PFW_BIND;
        }
        if (commonSpayResponse != null) {
            return commonSpayResponse;
        }
        commonSpayResponse = new CommonSpayResponse();
        commonSpayResponse.setStatus(i);
        return commonSpayResponse;
    }

    private synchronized void addRequest(PaymentFrameworkRequest paymentFrameworkRequest) {
        if (this.mPaymentFrameworkRequests == null) {
            this.mPaymentFrameworkRequests = new ArrayList();
        }
        this.mPaymentFrameworkRequests.add(paymentFrameworkRequest);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void notifyAndDeleteRequests() {
        /*
        r5 = this;
        monitor-enter(r5);
        r0 = "PaymentFramework";
        r1 = "notifyAndDeleteRequests: ";
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x004f }
        r0 = r5.mPaymentFrameworkRequests;	 Catch:{ all -> 0x004f }
        if (r0 != 0) goto L_0x000e;
    L_0x000c:
        monitor-exit(r5);
        return;
    L_0x000e:
        r0 = 0;
        r1 = r0;
    L_0x0010:
        r0 = r5.mPaymentFrameworkRequests;	 Catch:{ all -> 0x004f }
        r0 = r0.size();	 Catch:{ all -> 0x004f }
        if (r1 >= r0) goto L_0x0052;
    L_0x0018:
        r0 = r5.mPaymentFrameworkRequests;	 Catch:{ all -> 0x004f }
        r0 = r0.get(r1);	 Catch:{ all -> 0x004f }
        r0 = (com.samsung.android.spayfw.appinterface.PaymentFramework.PaymentFrameworkRequest) r0;	 Catch:{ all -> 0x004f }
        r2 = "PaymentFramework";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004f }
        r3.<init>();	 Catch:{ all -> 0x004f }
        r4 = "notifyAndDeleteRequests: opt: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x004f }
        r4 = r0.operation;	 Catch:{ all -> 0x004f }
        r3 = r3.append(r4);	 Catch:{ all -> 0x004f }
        r3 = r3.toString();	 Catch:{ all -> 0x004f }
        android.util.Log.d(r2, r3);	 Catch:{ all -> 0x004f }
        r2 = r0.callback;	 Catch:{ all -> 0x004f }
        if (r2 == 0) goto L_0x0047;
    L_0x003e:
        r0 = r0.callback;	 Catch:{ all -> 0x004f }
        r0.onReady();	 Catch:{ all -> 0x004f }
    L_0x0043:
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x0010;
    L_0x0047:
        r0 = "PaymentFramework";
        r2 = "notifyAndDeleteRequests: callback is null";
        android.util.Log.e(r0, r2);	 Catch:{ all -> 0x004f }
        goto L_0x0043;
    L_0x004f:
        r0 = move-exception;
        monitor-exit(r5);
        throw r0;
    L_0x0052:
        r0 = r5.mPaymentFrameworkRequests;	 Catch:{ all -> 0x004f }
        r0.clear();	 Catch:{ all -> 0x004f }
        goto L_0x000c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.appinterface.PaymentFramework.notifyAndDeleteRequests():void");
    }

    private synchronized boolean createService() {
        boolean z;
        z = true;
        if (this.mIPaymentFramework == null) {
            z = bindService();
        }
        return z;
    }

    private synchronized void scheduleBindTimer() {
        Log.d(LOG_TAG, "scheduleBindTimer: scheduling bind timer ");
        try {
            this.mBindCounter += RESULT_CODE_FAIL_PFW_INTERNAL;
            this.mBindTimerTask = new BindRetryTimerTask();
            new Timer().schedule(this.mBindTimerTask, PFW_BIND_TIMEOUT);
        } catch (Exception e) {
            Log.d(LOG_TAG, "scheduleBindTimer: Exception in scheduling bind timer ");
            e.printStackTrace();
        }
    }

    private synchronized void resetBindTimer(boolean z) {
        try {
            if (this.mBindTimerTask != null) {
                if (this.mBindTimerTask.cancel()) {
                    Log.d(LOG_TAG, "resetBindTimer: timerTask cancel return true ");
                } else {
                    Log.d(LOG_TAG, "resetBindTimer: timerTask cancel return false ");
                }
                this.mBindTimerTask = null;
            }
            if (z) {
                Log.d(LOG_TAG, "resetBindTimer: resetCount ");
                this.mBindCounter = PAY_TYPE_ECM;
            }
        } catch (Exception e) {
            Log.d(LOG_TAG, "scheduleBindTimer: Exception in canceling bind timer ");
            e.printStackTrace();
        }
    }

    private synchronized boolean bindService() {
        boolean z;
        z = false;
        if (this.mBindTimerTask != null) {
            Log.d(LOG_TAG, "bindService already attempted, waiting.");
            z = true;
        } else if (this.mIPaymentFramework == null) {
            Intent intent = new Intent();
            intent.setClassName(PAYMENT_FRAMEWORK_PACKAGE_NAME, "com.samsung.android.spayfw.core.PaymentFrameworkService");
            IBinder c03752 = new C03752();
            Bundle bundle = new Bundle();
            bundle.putBinder("deathDetectorBinder", c03752);
            intent.putExtras(bundle);
            z = this.mContext.bindService(intent, this.mConn, RESULT_CODE_WAIT_INIT_FRAMEWORK);
            if (z) {
                Log.d(LOG_TAG, "Service Bind Attempted");
                scheduleBindTimer();
            } else {
                Log.e(LOG_TAG, "Service Not Bound");
            }
        }
        return z;
    }

    private void notifyApp() {
        if (getTrackMapSize() != 0) {
            for (Entry entry : mTrackOpsHash.entrySet()) {
                Object key = entry.getKey();
                Log.d(LOG_TAG, "notifyApp: cb = " + key + "; key = " + ((Integer) entry.getValue()));
                switch (((Integer) entry.getValue()).intValue()) {
                    case RESULT_CODE_WAIT_INIT_FRAMEWORK /*1*/:
                        ((EnrollCardCallback) key).onFail(RESULT_CODE_FAIL_PFW_BIND, null);
                        break;
                    case RESULT_CODE_WAIT_UPDATE_FRAMEWORK /*2*/:
                        ((CommonCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_PROVISION /*3*/:
                        ((ProvisionTokenCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND, null);
                        break;
                    case PAY_TYPE_ECM /*4*/:
                        ((SelectIdvResponseCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_VERIFY_IDV /*5*/:
                        ((VerifyIdvResponseCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND, null);
                        break;
                    case PAYFW_OPT_SELECT_CARD /*6*/:
                        ((SelectCardCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_START_PAY /*7*/:
                        ((PayCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_STOP_PAY /*8*/:
                        ((CommonCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_PUSH_HANDLE /*9*/:
                        ((PushMessageCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_NOTIFY_DEVICE_RESET /*12*/:
                    case PAYFW_OPT_SET_PIN /*40*/:
                        ((CommonCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_SET_JWT_TOKEN /*14*/:
                    case PAYFW_OPT_GET_ENVIRONMENT /*23*/:
                    case PAYFW_OPT_SET_CONFIG /*32*/:
                    case PAYFW_OPT_GET_CONFIG /*33*/:
                        ((ConnectionCallback) key).onError(null, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_START_IN_APP_PAY /*21*/:
                        ((InAppPayCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_GET_LOGS /*22*/:
                        ((CommonCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_START_GET_GIFTCARD_REGISTERDATA /*24*/:
                        ((GiftCardRegisterCallback) key).onFail(RESULT_CODE_FAIL_PFW_BIND, null);
                        break;
                    case PAYFW_OPT_START_GET_GIFTCARD_TZENCDATA /*25*/:
                        ((GiftCardRegisterCallback) key).onFail(RESULT_CODE_FAIL_PFW_BIND, null);
                        break;
                    case PAYFW_OPT_START_GIFTCARD_PAY /*26*/:
                        ((PayCallback) key).onFail(GIFT_CARD_TOKEN_ID, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_EXTRACT_GIFTCARD_DETAIL /*27*/:
                        ((GiftCardExtractDetailCallback) key).onFail(RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_STORE_USER_SIGNATURE /*30*/:
                        ((CommonCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_GET_USER_SIGNATURE /*31*/:
                        ((UserSignatureCallback) key).onFail(RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_PROCESS_SERVER_REQUEST /*34*/:
                        ((ServerResponseCallback) key).onFail(RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_UPDATE_LOYALTY_CARD /*35*/:
                        ((UpdateLoyaltyCardCallback) key).onFail(RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_EXTRACT_LOYALTY_CARD_DETAIL /*36*/:
                        ((ExtractLoyaltyCardDetailResponseCallback) key).onFail(RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_START_LOYALTY_CARD_PAY /*37*/:
                        ((PayCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_GET_TRANSACTION_DETAILS /*38*/:
                        ((TransactionDetailsCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_REFRESH_IDV /*39*/:
                        ((RefreshIdvResponseCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_GET_GLOBALMEMBERSHIP_CARD_REGISTERDATA /*41*/:
                        ((GlobalMembershipCardRegisterCallback) key).onFail(RESULT_CODE_FAIL_PFW_BIND, null);
                        break;
                    case PAYFW_OPT_GET_GLOBALMEMBERSHIP_CARD_TZENCDATA /*42*/:
                        ((GlobalMembershipCardRegisterCallback) key).onFail(RESULT_CODE_FAIL_PFW_BIND, null);
                        break;
                    case PAYFW_OPT_START_GLOBALMEMBERSHIP_CARD_PAY /*43*/:
                        ((PayCallback) key).onFail(GIFT_CARD_TOKEN_ID, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_EXTRACT_GLOBALMEMBERSHIP_CARD_DETAIL /*44*/:
                        ((GlobalMembershipCardExtractDetailCallback) key).onFail(RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    case PAYFW_OPT_UPDATE_BIN_ATTRIBUTE /*45*/:
                        ((CommonCallback) key).onFail(null, RESULT_CODE_FAIL_PFW_BIND);
                        break;
                    default:
                        break;
                }
            }
            clearTrackMap();
        }
    }

    private static synchronized void addToTrackMap(Object obj, int i) {
        synchronized (PaymentFramework.class) {
            if (mTrackOpsHash == null) {
                mTrackOpsHash = new HashMap();
            }
            mTrackOpsHash.put(obj, Integer.valueOf(i));
        }
    }

    public static synchronized void removeFromTrackMap(Object obj) {
        synchronized (PaymentFramework.class) {
            if (mTrackOpsHash != null) {
                mTrackOpsHash.remove(obj);
            }
        }
    }

    private static synchronized void clearTrackMap() {
        synchronized (PaymentFramework.class) {
            if (mTrackOpsHash != null) {
                mTrackOpsHash.clear();
            }
        }
    }

    private static synchronized int getTrackMapSize() {
        int size;
        synchronized (PaymentFramework.class) {
            if (mTrackOpsHash != null) {
                size = mTrackOpsHash.size();
            } else {
                size = RESULT_CODE_SUCCESS;
            }
        }
        return size;
    }

    private File getLocalFile(String str) {
        File file = new File(this.mContext.getFilesDir(), PAYMENT_FRAMEWORK_DIRECTORY);
        file.mkdirs();
        return new File(file, str + System.currentTimeMillis() + String.valueOf(new Random().nextInt()));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.io.File writeStringToFile(java.lang.String r6, java.lang.String r7) {
        /*
        r5 = this;
        r1 = 0;
        if (r6 == 0) goto L_0x0005;
    L_0x0003:
        if (r7 != 0) goto L_0x0007;
    L_0x0005:
        r0 = r1;
    L_0x0006:
        return r0;
    L_0x0007:
        r0 = r5.getLocalFile(r7);
        r3 = new java.io.BufferedWriter;	 Catch:{ Exception -> 0x0026 }
        r2 = new java.io.FileWriter;	 Catch:{ Exception -> 0x0026 }
        r2.<init>(r0);	 Catch:{ Exception -> 0x0026 }
        r3.<init>(r2);	 Catch:{ Exception -> 0x0026 }
        r2 = 0;
        r3.write(r6);	 Catch:{ Throwable -> 0x002f, all -> 0x0043 }
        if (r3 == 0) goto L_0x0006;
    L_0x001b:
        if (r1 == 0) goto L_0x002b;
    L_0x001d:
        r3.close();	 Catch:{ Throwable -> 0x0021 }
        goto L_0x0006;
    L_0x0021:
        r1 = move-exception;
        r2.addSuppressed(r1);	 Catch:{ Exception -> 0x0026 }
        goto L_0x0006;
    L_0x0026:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0006;
    L_0x002b:
        r3.close();	 Catch:{ Exception -> 0x0026 }
        goto L_0x0006;
    L_0x002f:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x0031 }
    L_0x0031:
        r1 = move-exception;
    L_0x0032:
        if (r3 == 0) goto L_0x0039;
    L_0x0034:
        if (r2 == 0) goto L_0x003f;
    L_0x0036:
        r3.close();	 Catch:{ Throwable -> 0x003a }
    L_0x0039:
        throw r1;	 Catch:{ Exception -> 0x0026 }
    L_0x003a:
        r3 = move-exception;
        r2.addSuppressed(r3);	 Catch:{ Exception -> 0x0026 }
        goto L_0x0039;
    L_0x003f:
        r3.close();	 Catch:{ Exception -> 0x0026 }
        goto L_0x0039;
    L_0x0043:
        r2 = move-exception;
        r4 = r2;
        r2 = r1;
        r1 = r4;
        goto L_0x0032;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.appinterface.PaymentFramework.writeStringToFile(java.lang.String, java.lang.String):java.io.File");
    }
}
