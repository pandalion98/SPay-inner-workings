package com.samsung.cpp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.samsung.android.telephony.MultiSimManager;
import com.samsung.cpp.CPPositioningService.RequestCPGeoFenceRegister;
import com.samsung.cpp.CPPositioningService.RequestLocationInput;
import com.sec.android.emergencymode.EmergencyConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CPPProvider {
    private static double DEGREE_RESOLUTION = 20000.0d;
    public static final String GEO_CB = "geo_cb";
    private static final String INTENT_ACTION_SUBINFO_RECORD_UPDATED = "android.intent.action.ACTION_SUBINFO_RECORD_UPDATED";
    private static final String INTENT_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    private static final String INTENT_CLM_TT_GEO_FENCE_UPDATE = "android.intent.action.ACTION_CLM_TT_GEO_FENCE_UPDATE";
    private static final String INTENT_CLM_TT_START_BY_APP = "android.intent.action.ACTION_CLM_TT_START_BY_APP";
    private static final String INTENT_CLM_TT_STOP_BY_APP = "android.intent.action.ACTION_CLM_TT_STOP_BY_APP";
    private static final String INTENT_DIAGNOSTIC_REPORT_CHANGED = "android.settings.DIAGNOSTIC_INFO_CHANGED";
    private static final String INTENT_DIAGNOSTIC_REPORT_DIALOG = "sec.intent.action.DIAGNOSTIC_INFO_DIALOG";
    private static final String INTENT_UPDATE_POLICY = "sec.intent.action.UPDATE_POLICY";
    public static final int MODE_CPP_ONLY = 0;
    public static final int MODE_CPP_PLUS_WIFI = 1;
    private static final int MSG_DEINIT = 2;
    public static final int MSG_DEREGISTER_CP_GEO_FENCE = 10;
    public static final int MSG_GEO_CALLBACK = 11;
    private static final int MSG_INIT = 1;
    public static final int MSG_POLICY_UPDATED = 8;
    public static final int MSG_REGISTER_CP_GEO_FENCE = 9;
    private static final int MSG_REPORT_CP_LOCATION = 5;
    public static final int MSG_RESPONSE_FROM_SERVER = 6;
    private static final int MSG_START_CP_LOCATION = 3;
    private static final int MSG_STOP_CP_LOCATION = 4;
    public static final int MSG_UPDATE_POLICY = 7;
    private static final String TAG = "CPProvider";
    private static boolean cellular = false;
    private static boolean mMobileConnected;
    private static boolean mWifiConnected;
    private IntentFilter filter;
    private boolean flagEnableCLM;
    private boolean flagEnableCPP;
    private boolean flag_MSG_UPDATE_POLICY;
    private BroadcastReceiver mCPPInfoReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(CPPProvider.TAG, "External Intent Received " + intent.getAction());
            Message reqMsg;
            Message reqMsg1;
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                boolean wifiConnected;
                boolean mobileConnected;
                NetworkInfo currentActiveNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                if (currentActiveNetworkInfo == null || !currentActiveNetworkInfo.isConnected()) {
                    wifiConnected = false;
                    mobileConnected = false;
                } else {
                    wifiConnected = currentActiveNetworkInfo.getType() == 1;
                    mobileConnected = currentActiveNetworkInfo.getType() == 0;
                }
                Log.d(CPPProvider.TAG, "CONNECTIVITY_ACTION = { WIFI : " + wifiConnected + ", Mobile Data : " + mobileConnected + " }");
                if (wifiConnected && !CPPProvider.mWifiConnected) {
                    Log.d(CPPProvider.TAG, "CONNECTIVITY_ACTION : WIFI ON");
                    Log.d(CPPProvider.TAG, "CONNECTIVITY_ACTION : flagEnableCLM = " + CPPProvider.this.flagEnableCLM + ", mNeedUpdatePolicy = " + CPPProvider.this.mNeedUpdatePolicy + ", mCurrentPolicyFileExists = " + CPPProvider.this.mPolicyHandler.mCurrentPolicyFileExists);
                    CPPProvider.mWifiConnected = true;
                    if (CPPProvider.this.flagEnableCLM) {
                        Log.d(CPPProvider.TAG, "WIFI connected + flag_enableCPP --> sendRequest directly");
                        CPPProvider.this.sendLargeRequest();
                    }
                    if ((CPPProvider.this.mNeedUpdatePolicy & 1) == 1 || !CPPProvider.this.mPolicyHandler.mCurrentPolicyFileExists) {
                        Log.d(CPPProvider.TAG, "sendMessage(MSG_UPDATE_POLICY)/BroadcastReceiver.CONNECTIVITY_ACTION(wifi)");
                        reqMsg = Message.obtain();
                        reqMsg.what = 7;
                        reqMsg.arg1 = 1;
                        CPPProvider.this.mHandler.sendMessage(reqMsg);
                    }
                } else if (!wifiConnected && CPPProvider.mWifiConnected) {
                    Log.d(CPPProvider.TAG, "CONNECTIVITY_ACTION : WIFI OFF");
                    CPPProvider.mWifiConnected = false;
                }
                if (mobileConnected && !CPPProvider.mMobileConnected) {
                    Log.d(CPPProvider.TAG, "CONNECTIVITY_ACTION : Mobile Data ON");
                    Log.d(CPPProvider.TAG, "CONNECTIVITY_ACTION : mNeedUpdatePolicy = " + CPPProvider.this.mNeedUpdatePolicy + ", mCurrentPolicyFileExists = " + CPPProvider.this.mPolicyHandler.mCurrentPolicyFileExists);
                    CPPProvider.mMobileConnected = true;
                    if ((CPPProvider.this.mNeedUpdatePolicy & 1) == 1 || !CPPProvider.this.mPolicyHandler.mCurrentPolicyFileExists) {
                        Log.d(CPPProvider.TAG, "sendMessage(MSG_UPDATE_POLICY)/BroadcastReceiver.CONNECTIVITY_ACTION(mobile)");
                        reqMsg1 = Message.obtain();
                        reqMsg1.what = 7;
                        reqMsg1.arg1 = 1;
                        CPPProvider.this.mHandler.sendMessage(reqMsg1);
                    }
                } else if (!mobileConnected && CPPProvider.mMobileConnected) {
                    Log.d(CPPProvider.TAG, "CONNECTIVITY_ACTION : Mobile Data OFF");
                    CPPProvider.mMobileConnected = false;
                }
            } else if ("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
                Log.d(CPPProvider.TAG, "received : android.intent.action.ACTION_SHUTDOWN");
                Message stopMsg = Message.obtain();
                stopMsg.what = 4;
                CPPProvider.this.mHandler.sendMessage(stopMsg);
            } else if (CPPProvider.INTENT_CLM_TT_START_BY_APP.equals(intent.getAction())) {
                Log.d(CPPProvider.TAG, "INTENT_CLM_TT_START_BY_APP : flagEnableCPP = " + CPPProvider.this.flagEnableCPP);
                if (CPPProvider.this.flagEnableCPP) {
                    Log.d(CPPProvider.TAG, "INTENT_CLM_TT_START_BY_APP : CP Positioning Service is already enabled.");
                    return;
                }
                CPPProvider.this.flagEnableCPP = true;
                CPPProvider.this.mHandler.sendEmptyMessage(1);
            } else if (CPPProvider.INTENT_CLM_TT_STOP_BY_APP.equals(intent.getAction())) {
                Log.d(CPPProvider.TAG, "INTENT_CLM_TT_STOP_BY_APP : flagEnableCPP = " + CPPProvider.this.flagEnableCPP);
                if (CPPProvider.this.flagEnableCPP) {
                    CPPProvider.this.flagEnableCPP = false;
                    CPPProvider.this.mHandler.sendEmptyMessage(2);
                    return;
                }
                Log.d(CPPProvider.TAG, "INTENT_CLM_TT_STOP_BY_APP : CP Positioning Service is already disabled.");
            } else if (CPPProvider.INTENT_UPDATE_POLICY.equals(intent.getAction())) {
                if (CPPProvider.this.mPolicyHandler.isPolicyExpiration()) {
                    reqMsg = Message.obtain();
                    reqMsg.what = 7;
                    reqMsg.arg1 = 1;
                    CPPProvider.this.mHandler.sendMessage(reqMsg);
                }
                Log.d(CPPProvider.TAG, "Update required by sec.intent.action.UPDATE_POLICY");
            } else if (CPPProvider.INTENT_BOOT_COMPLETED.equals(intent.getAction())) {
                Log.d(CPPProvider.TAG, "Boot Completed, Clean Cell DB Req Table");
                CPPProvider.this.mDbAdapter.deleteAllReq();
            } else if (!"android.net.wifi.STATE_CHANGE".equals(intent.getAction())) {
                if ("android.net.wifi.SCAN_RESULTS".equals(intent.getAction())) {
                    if (CPPProvider.this.mWifiScanning) {
                        CPPProvider.this.wifiResults = CPPProvider.this.wifi.getScanResults();
                        for (ScanResult result : CPPProvider.this.wifiResults) {
                            Log.d(CPPProvider.TAG, "SSID : " + result.SSID + ", BSSID : " + result.BSSID + ", RSSI : " + result.level);
                        }
                    }
                } else if (CPPProvider.INTENT_DIAGNOSTIC_REPORT_CHANGED.equals(intent.getAction())) {
                    String version = CPPProvider.this.mPolicyHandler.getPolicyVersion();
                    if (version != null && version.equals(CPPPolicyHandler.DEFAULT_VERSION)) {
                        CPPProvider.this.mPolicyHandler.setPolicyVersion(new SimpleDateFormat("yyyyMMdd-HH:mm").format(new Date(System.currentTimeMillis())));
                    }
                } else if (CPPProvider.INTENT_ACTION_SUBINFO_RECORD_UPDATED.equals(intent.getAction())) {
                    Log.d(CPPProvider.TAG, "INTENT_ACTION_SUBINFO_RECORD_UPDATED : mNeedUpdatePolicy = " + CPPProvider.this.mNeedUpdatePolicy + ", mCurrentPolicyFileExists = " + CPPProvider.this.mPolicyHandler.mCurrentPolicyFileExists);
                    if ((CPPProvider.this.mNeedUpdatePolicy & 1) == 1 || !CPPProvider.this.mPolicyHandler.mCurrentPolicyFileExists) {
                        Log.d(CPPProvider.TAG, "sendMessageDelayed(MSG_UPDATE_POLICY)/INTENT_ACTION_SUBINFO_RECORD_UPDATED");
                        reqMsg1 = Message.obtain();
                        reqMsg1.what = 7;
                        reqMsg1.arg1 = 1;
                        CPPProvider.this.mHandler.sendMessageDelayed(reqMsg1, 3000);
                    } else {
                        CPPProvider.this.mPolicyHandler.notifyPolicyUpdate();
                    }
                    Log.d(CPPProvider.TAG, "SIM Slot Count : " + CPPProvider.this.mSimSlotCount);
                    if (CPPProvider.this.mSimSlotCount > 1) {
                        if (SubscriptionManager.getSubId(0) != null) {
                            CPPProvider.this.mServiceStatelistener1 = new PhoneStateListener(0) {
                                public void onServiceStateChanged(ServiceState serviceState) {
                                    int currentState = serviceState.getState();
                                }
                            };
                        } else {
                            Log.e(CPPProvider.TAG, "SIM 1 error");
                        }
                        if (SubscriptionManager.getSubId(1) != null) {
                            CPPProvider.this.mServiceStatelistener2 = new PhoneStateListener(1) {
                                public void onServiceStateChanged(ServiceState serviceState) {
                                    int currentState = serviceState.getState();
                                }
                            };
                        } else {
                            Log.e(CPPProvider.TAG, "SIM 2 error");
                        }
                    } else {
                        CPPProvider.this.mServiceStatelistener1 = new PhoneStateListener() {
                            public void onServiceStateChanged(ServiceState serviceState) {
                                Log.d(CPPProvider.TAG, "currentState: " + serviceState.getState());
                            }
                        };
                    }
                    TelephonyManager teleMan = (TelephonyManager) CPPProvider.this.mContext.getSystemService("phone");
                    if (CPPProvider.this.mServiceStatelistener1 != null) {
                        teleMan.listen(CPPProvider.this.mServiceStatelistener1, 1);
                    }
                    if (CPPProvider.this.mServiceStatelistener2 != null) {
                        teleMan.listen(CPPProvider.this.mServiceStatelistener2, 1);
                    }
                }
            }
        }
    };
    private final Context mContext;
    private ICPPLocationListener mCpLocListener;
    private int mCurrentReqMode = -1;
    private CPPDbAdapter mDbAdapter;
    private CPPEventHandler mHandler;
    protected PendingIntent mMobileAlarmIntent = null;
    private int mMobilePolicyRetryCount;
    private int mNeedUpdatePolicy;
    private CPPPolicyHandler mPolicyHandler;
    private boolean mReportLocationStatus = false;
    private PhoneStateListener mServiceStatelistener1;
    private PhoneStateListener mServiceStatelistener2;
    private int mSimSlotConnected;
    private int mSimSlotCount;
    private CPPSrvCommunicator mSrvComm;
    private boolean mWifiScanning;
    private WifiManager wifi;
    private List<ScanResult> wifiResults;

    private class CPPEventHandler extends Handler {
        public CPPEventHandler(Looper looper) {
            super(looper, null);
        }

        public void handleMessage(Message msg) {
            Log.d(CPPProvider.TAG, "handleMessage(): " + msg.what);
            Bundle bundle;
            AlarmManager am;
            Date date;
            switch (msg.what) {
                case 1:
                    CPPProvider.this.initialiseCPLocationProvider();
                    return;
                case 2:
                    CPPProvider.this.deInitialiseCPLocationProvider();
                    return;
                case 3:
                    CPPProvider.this.startReportLocation(msg.arg1, msg.arg2);
                    if (CPPProvider.this.mCurrentReqMode == 1) {
                        if (CPPProvider.mWifiConnected) {
                            CPPProvider.this.startWifiScan();
                        }
                        CPPProvider.this.requestCellHistory((byte) 2, (byte) 0, (byte) 1, (byte) 1);
                    }
                    CPPProvider.this.flagEnableCLM = true;
                    return;
                case 4:
                    CPPProvider.this.stopReportLocation();
                    CPPProvider.this.flagEnableCLM = false;
                    return;
                case 5:
                    Location location = msg.obj;
                    if (CPPProvider.this.mCpLocListener != null) {
                        try {
                            CPPProvider.this.mCpLocListener.onLocationChanged(location);
                            return;
                        } catch (RemoteException ex) {
                            Log.e(CPPProvider.TAG, "MSG_REPORT_CP_LOCATION -onLocationChanged: RemoteException " + ex.toString());
                            return;
                        }
                    }
                    Log.d(CPPProvider.TAG, "MSG_REPORT_CP_LOCATION-No Location listener registered. ");
                    return;
                case 6:
                    Log.d(CPPProvider.TAG, "Received from Server, type : " + msg.arg1 + ", rat :" + msg.arg2);
                    ConnectivityManager connectivityManager = (ConnectivityManager) CPPProvider.this.mContext.getSystemService("connectivity");
                    int _type = msg.arg1;
                    final int _rat = msg.arg2;
                    bundle = msg.getData();
                    if (bundle != null) {
                        int httpResponseCode = bundle.getInt(CPPSrvCommunicator.HTTP_RESPONSE_CODE);
                        String httpResponseMsg = bundle.getString(CPPSrvCommunicator.HTTP_RESPONSE_MSG);
                        int resultCode = bundle.getInt(CPPSrvCommunicator.RESULT_CODE);
                        Log.d(CPPProvider.TAG, "HTTP Response Code : " + httpResponseCode + ", HTTP Response Message : " + httpResponseMsg + ", ResultCode : " + resultCode);
                        if (_type == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB) {
                            int resultSize = bundle.getInt(CPPSrvCommunicator.RESULT_SIZE);
                            final long requestedId = bundle.getLong("req_id");
                            final byte[] resultMsg = bundle.getByteArray(CPPSrvCommunicator.RESULT_MSG);
                            Log.d(CPPProvider.TAG, "RequestedId : " + requestedId + ", resultSize : " + resultSize + ", ResultMsg : " + Arrays.toString(resultMsg));
                            if (httpResponseCode == 200 && resultCode == 4) {
                                Log.d(CPPProvider.TAG, "COMM_TYPE_POST_REQ_CELLDB - HTTP_OK - POLICYID_UPDATE_REQUIRED");
                                CPPPolicyHandler.mInvalidPolicyID = true;
                                CPPProvider.this.mDbAdapter.updateSending(requestedId, false);
                                Message requestPolicyUpdateMsg = Message.obtain();
                                requestPolicyUpdateMsg.what = 7;
                                requestPolicyUpdateMsg.arg1 = _rat;
                                CPPProvider.this.mHandler.sendMessage(requestPolicyUpdateMsg);
                                Log.d(CPPProvider.TAG, "sendMessage(MSG_UPDATE_POLICY)/POLICYID_UPDATE_REQUIRED");
                            } else if (httpResponseCode == 200) {
                                Log.d(CPPProvider.TAG, "COMM_TYPE_POST_REQ_CELLDB - HTTP_OK");
                                Log.d(CPPProvider.TAG, "Cellular : " + CPPProvider.cellular);
                                if (!CPPProvider.this.mDbAdapter.isLargeReq(requestedId)) {
                                    Log.d(CPPProvider.TAG, "!isLargeReq");
                                    new Thread(new Runnable() {
                                        public void run() {
                                            CPPProvider.this.sendRespToCpDirect(resultMsg, requestedId, _rat);
                                        }
                                    }).start();
                                }
                                if (resultCode == 0) {
                                    Log.d(CPPProvider.TAG, "COMM_TYPE_POST_REQ_CELLDB - HTTP_OK - SUCCESS");
                                    CPPProvider.this.mDbAdapter.insertResp(resultMsg, resultSize, _rat, requestedId);
                                }
                            } else {
                                Log.d(CPPProvider.TAG, "CPPEventHandler.handleMessage() -- FAIL from SERVER / not HTTP_OK");
                            }
                            CPPProvider.this.mDbAdapter.deleteReqById(requestedId);
                            return;
                        } else if (_type == CPPSrvCommunicator.COMM_TYPE_GET_POLICY) {
                            String resultMsg2 = bundle.getString(CPPSrvCommunicator.RESULT_MSG);
                            Log.d(CPPProvider.TAG, "MSG_RESPONSE_FROM_SERVER (COMM_TYPE_GET_POLICY) : result message = " + resultMsg2);
                            Message respMsg;
                            if (httpResponseCode == 200 && resultCode == 0) {
                                Log.d(CPPProvider.TAG, "MSG_RESPONSE_FROM_SERVER (COMM_TYPE_GET_POLICY) : SUCCESS");
                                CPPProvider.this.mPolicyHandler.updatePolicy(resultMsg2, bundle.getString(CPPSrvCommunicator.REQ_BODY));
                                Log.d(CPPProvider.TAG, "MSG_RESPONSE_FROM_SERVER (COMM_TYPE_GET_POLICY) : SUCCESS --> mNeedUpdatePolicy = " + CPPProvider.this.mNeedUpdatePolicy + " reset to 0");
                                if ((CPPProvider.this.mNeedUpdatePolicy & 1) == 1) {
                                    CPPProvider.this.mNeedUpdatePolicy = CPPProvider.this.mNeedUpdatePolicy & 14;
                                }
                                Log.d(CPPProvider.TAG, "For Periodic Update Purpose : send MSG_UPDATE_POLICY one more time -> handler:MSG_UPDATE_POLICY");
                                respMsg = Message.obtain();
                                respMsg.what = 7;
                                respMsg.arg1 = msg.arg2;
                                CPPProvider.this.mHandler.sendMessage(respMsg);
                            } else {
                                Log.d(CPPProvider.TAG, "MSG_RESPONSE_FROM_SERVER (COMM_TYPE_GET_POLICY) : FAIL, mMobilePolicyRetryCount = " + CPPProvider.this.mMobilePolicyRetryCount);
                                if (CPPProvider.this.mMobilePolicyRetryCount < 3) {
                                    CPPProvider.this.mMobilePolicyRetryCount = CPPProvider.this.mMobilePolicyRetryCount + 1;
                                    int time = new Random().nextInt(4) + 1;
                                    respMsg = Message.obtain();
                                    respMsg.what = 7;
                                    respMsg.arg1 = msg.arg2;
                                    CPPProvider.this.mHandler.sendMessageDelayed(respMsg, (long) (60000 * time));
                                    Log.d(CPPProvider.TAG, "MSG_RESPONSE_FROM_SERVER (COMM_TYPE_GET_POLICY) : FAIL, retry after (" + time + "min)");
                                } else {
                                    if (CPPProvider.this.mMobilePolicyRetryCount >= 3) {
                                        CPPProvider.this.mPolicyHandler.setDefaultPolicy(true);
                                    }
                                    CPPProvider.this.mMobilePolicyRetryCount = 0;
                                    am = (AlarmManager) CPPProvider.this.mContext.getSystemService(EmergencyConstants.TABLE_ALARM);
                                    date = new Date(System.currentTimeMillis() + ((long) CPPProvider.this.mPolicyHandler.getPolicyRetryPeriod()));
                                    CPPProvider.this.mPolicyHandler.setPolicyExpirationDate(date, false);
                                    CPPProvider.this.mMobileAlarmIntent = PendingIntent.getBroadcast(CPPProvider.this.mContext, 0, new Intent(CPPProvider.INTENT_UPDATE_POLICY), 134217728);
                                    am.set(0, date.getTime(), CPPProvider.this.mMobileAlarmIntent);
                                    Log.d(CPPProvider.TAG, "MSG_RESPONSE_FROM_SERVER (COMM_TYPE_GET_POLICY) : FAIL, register ALARM at (" + date.toString() + "ms)");
                                }
                            }
                            if (CPPProvider.this.flag_MSG_UPDATE_POLICY) {
                                Log.d(CPPProvider.TAG, "MSG_RESPONSE_FROM_SERVER (COMM_TYPE_GET_POLICY) : flag_MSG_UPDATE_POLICY reset");
                                CPPProvider.this.flag_MSG_UPDATE_POLICY = false;
                                return;
                            }
                            return;
                        } else if (_type == CPPSrvCommunicator.COMM_TYPE_POST_REQ_WIFI_LOC) {
                            Log.d(CPPProvider.TAG, "MSG_RESPONSE_FROM_SERVER - COMM_TYPE_POST_REQ_WIFI_LOC");
                            return;
                        } else {
                            return;
                        }
                    }
                    return;
                case 7:
                    Log.d(CPPProvider.TAG, "MSG_UPDATE_POLICY. msg.arg1(RAT) : " + msg.arg1);
                    am = (AlarmManager) CPPProvider.this.mContext.getSystemService(EmergencyConstants.TABLE_ALARM);
                    if (CPPProvider.this.mPolicyHandler == null) {
                        return;
                    }
                    String cellinfo;
                    if (CPPPolicyHandler.mInvalidPolicyID) {
                        cellinfo = CPPProvider.this.mPolicyHandler.getPLMN();
                        if (cellinfo == null) {
                            cellinfo = CPPPolicyHandler.getCurrentMccMnc(CPPProvider.this.mContext);
                        }
                        Log.d(CPPProvider.TAG, "cellinfo : " + cellinfo);
                        if (CPPProvider.this.flag_MSG_UPDATE_POLICY || cellinfo == null) {
                            Log.d(CPPProvider.TAG, "MSG_UPDATE_POLICY : [invalidPolicyID] flag_MSG_UPDATE_POLICY(" + CPPProvider.this.flag_MSG_UPDATE_POLICY + "), cellinfo(" + cellinfo + ") --> NOT sendToSrv");
                            return;
                        }
                        Log.d(CPPProvider.TAG, "MSG_UPDATE_POLICY : [invalidPolicyID] flag_MSG_UPDATE_POLICY(" + CPPProvider.this.flag_MSG_UPDATE_POLICY + "), cellinfo(" + cellinfo + ") --> sendToSrv");
                        CPPProvider.this.flag_MSG_UPDATE_POLICY = true;
                        CPPProvider.this.mSrvComm.sendToSrv(CPPSrvCommunicator.COMM_TYPE_GET_POLICY, cellinfo, 0, msg.arg1, 3);
                        return;
                    }
                    boolean bIsPolicyExpiration = CPPProvider.this.mPolicyHandler.isPolicyExpiration();
                    if (bIsPolicyExpiration) {
                        Log.d(CPPProvider.TAG, "MSG_UPDATE_POLICY(policy expired) : (bIsPolicyExpiration = " + bIsPolicyExpiration + "), expired");
                        Log.d(CPPProvider.TAG, "MSG_UPDATE_POLICY(policy expired) : connection = { mMobileConnected : " + CPPProvider.mMobileConnected + ", mWifiConnected : " + CPPProvider.mWifiConnected + " }");
                        if (CPPProvider.this.mMobileAlarmIntent != null) {
                            am.cancel(CPPProvider.this.mMobileAlarmIntent);
                            Log.d(CPPProvider.TAG, "MSG_UPDATE_POLICY(policy expired) : Previous Alarm is canceled");
                        }
                        CPPProvider.this.mMobileAlarmIntent = null;
                        if (CPPProvider.mWifiConnected || CPPProvider.mMobileConnected) {
                            cellinfo = CPPProvider.this.mPolicyHandler.getPLMN();
                            if (cellinfo == null) {
                                cellinfo = CPPPolicyHandler.getCurrentMccMnc(CPPProvider.this.mContext);
                            }
                            Log.d(CPPProvider.TAG, "cellinfo : " + cellinfo);
                            if (CPPProvider.this.flag_MSG_UPDATE_POLICY || cellinfo == null) {
                                Log.d(CPPProvider.TAG, "MSG_UPDATE_POLICY : [networkConnected] flag_MSG_UPDATE_POLICY(" + CPPProvider.this.flag_MSG_UPDATE_POLICY + "), cellinfo(" + cellinfo + ") --> NOT sendToSrv");
                                return;
                            }
                            Log.d(CPPProvider.TAG, "MSG_UPDATE_POLICY : [networkConnected] flag_MSG_UPDATE_POLICY(" + CPPProvider.this.flag_MSG_UPDATE_POLICY + "), cellinfo(" + cellinfo + ") --> sendToSrv");
                            CPPProvider.this.flag_MSG_UPDATE_POLICY = true;
                            CPPProvider.this.mSrvComm.sendToSrv(CPPSrvCommunicator.COMM_TYPE_GET_POLICY, cellinfo, 0, msg.arg1, 3);
                            return;
                        }
                        CPPProvider.this.mNeedUpdatePolicy = CPPProvider.this.mNeedUpdatePolicy | 1;
                        Log.d(CPPProvider.TAG, "MSG_UPDATE_POLICY(policy expired) : network not connected --> register alarm with PendingIntent");
                        Log.d(CPPProvider.TAG, "MSG_UPDATE_POLICY(policy expired) : network not connected --> set mNeedUpdatePolicy (" + CPPProvider.this.mNeedUpdatePolicy + "), if CONNECTIVITY is changed -> try again");
                        date = new Date(System.currentTimeMillis() + ((long) CPPProvider.this.mPolicyHandler.getPolicyRetryPeriod()));
                        CPPProvider.this.mPolicyHandler.setPolicyExpirationDate(date, false);
                        CPPProvider.this.mMobileAlarmIntent = PendingIntent.getBroadcast(CPPProvider.this.mContext, 0, new Intent(CPPProvider.INTENT_UPDATE_POLICY), 134217728);
                        am.set(0, date.getTime(), CPPProvider.this.mMobileAlarmIntent);
                        Log.d(CPPProvider.TAG, "MSG_UPDATE_POLICY(policy expired) : network not connected --> register ALARM at (" + date.toString() + "ms)");
                        return;
                    } else if (CPPProvider.this.mMobileAlarmIntent == null) {
                        Log.d(CPPProvider.TAG, "MSG_UPDATE_POLICY(policy is not expired) : --> Send intent for next alarm period");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
                        try {
                            String expireDate = CPPProvider.this.mPolicyHandler.getPolicyExpirationDate();
                            if (expireDate != null) {
                                Date date2 = simpleDateFormat.parse(expireDate);
                                CPPProvider.this.mMobileAlarmIntent = PendingIntent.getBroadcast(CPPProvider.this.mContext, 0, new Intent(CPPProvider.INTENT_UPDATE_POLICY), 0);
                                am.set(0, date2.getTime(), CPPProvider.this.mMobileAlarmIntent);
                                Log.d(CPPProvider.TAG, "MSG_UPDATE_POLICY(policy is not expired) : register alarm for retryPeriod = { current : " + System.currentTimeMillis() + ", registeredTime : " + date2.getTime());
                                return;
                            }
                            return;
                        } catch (NullPointerException e) {
                            Log.e(CPPProvider.TAG, "MSG_UPDATE_POLICY(policy is not expired) : NullPointerException : " + e);
                            return;
                        } catch (ParseException e2) {
                            Log.e(CPPProvider.TAG, "MSG_UPDATE_POLICY(policy is not expired) : ParseException : " + e2);
                            return;
                        }
                    } else {
                        return;
                    }
                case 8:
                    int ttAct = msg.arg1;
                    Message sendMsg;
                    if (ttAct == 1 && !CPPProvider.this.flagEnableCLM) {
                        sendMsg = Message.obtain();
                        sendMsg.what = 3;
                        sendMsg.arg1 = 0;
                        sendMsg.arg2 = 0;
                        CPPProvider.this.mHandler.sendMessageDelayed(sendMsg, 1000);
                    } else if (ttAct == 0 && CPPProvider.this.flagEnableCLM) {
                        sendMsg = Message.obtain();
                        sendMsg.what = 4;
                        CPPProvider.this.mHandler.sendMessage(sendMsg);
                    }
                    if (CPPProvider.this.mPolicyHandler.mCurrentPolicyFileExists) {
                        JSONObject receivedPolicy = CPPProvider.this.mPolicyHandler.getTTJSONObject();
                        String currentPolicy = receivedPolicy != null ? receivedPolicy.toString() : null;
                        if (currentPolicy != null) {
                            CPPProvider.this.sendBytesToCp(currentPolicy.getBytes(), currentPolicy.length(), 3);
                            Log.d(CPPProvider.TAG, "policy to CP = " + currentPolicy);
                            return;
                        }
                        Log.d(CPPProvider.TAG, "policy to CP does not have TT info.");
                        return;
                    }
                    return;
                case 9:
                    RequestCPGeoFenceRegister input = msg.obj;
                    int err = CPPProvider.this.registerGeoFence(Double.valueOf(Double.valueOf(input.mLatitude).doubleValue() * CPPProvider.DEGREE_RESOLUTION).intValue(), Double.valueOf(Double.valueOf(input.mLongitude).doubleValue() * CPPProvider.DEGREE_RESOLUTION).intValue(), input.mGeoMode, input.mRadius, input.mPeriod, input.mKey);
                    if (err != 0) {
                        Log.d(CPPProvider.TAG, "MSG_REGISTER_CP_GEO_FENCE failed to send to RIL, err " + err);
                        return;
                    } else {
                        Log.d(CPPProvider.TAG, "MSG_REGISTER_CP_GEO_FENCE success to send to RIL, err " + err);
                        return;
                    }
                case 10:
                    Log.d(CPPProvider.TAG, "MSG_DEREGISTER_CP_GEO_FENCE " + msg.arg1);
                    CPPProvider.this.deRegisterGeoFence(msg.arg1, CPPProvider.this.mDbAdapter.getGeoLat((long) msg.arg1).intValue(), CPPProvider.this.mDbAdapter.getGeoLon((long) msg.arg1).intValue());
                    CPPProvider.this.mDbAdapter.deleteGeoById(msg.arg1);
                    return;
                case 11:
                    bundle = msg.getData();
                    if (!CPPProvider.this.mWifiScanning) {
                        CPPProvider.this.startWifiScan();
                        CPPProvider.this.mHandler.sendMessageDelayed(msg, 4000);
                        return;
                    } else if (CPPProvider.this.wifiResults == null) {
                        CPPProvider.this.mHandler.sendMessageDelayed(msg, 4000);
                        return;
                    } else if (bundle != null) {
                        CPPProvider.this.makeJsonForGeo(true, bundle.getByteArray(CPPProvider.GEO_CB));
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private native void createJNIObject();

    private native void deInitSamsungLocationEngine();

    private native boolean deRegisterGeoFence(int i, int i2, int i3);

    private native void initSamsungLocationEngine();

    private native int registerGeoFence(int i, int i2, int i3, int i4, int i5, int i6);

    private native boolean requestCellHistory(byte b, byte b2, byte b3, byte b4);

    private native boolean sendBytesToCp(byte[] bArr, int i, int i2);

    private native boolean startReportLocation(int i, int i2);

    private native boolean stopReportLocation();

    public CPPProvider(Context context) {
        Log.d(TAG, "CPPProvider Constructor");
        this.mContext = context;
        this.mNeedUpdatePolicy = 0;
        this.mMobilePolicyRetryCount = 0;
        this.mWifiScanning = false;
        this.flag_MSG_UPDATE_POLICY = false;
        mWifiConnected = false;
        mMobileConnected = false;
        this.flagEnableCPP = false;
        this.flagEnableCLM = false;
        HandlerThread hThread = new HandlerThread("CPPEventHandler");
        hThread.start();
        this.mHandler = new CPPEventHandler(hThread.getLooper());
        this.filter = new IntentFilter();
        this.filter.addAction(INTENT_CLM_TT_START_BY_APP);
        this.filter.addAction(INTENT_CLM_TT_STOP_BY_APP);
        this.mContext.registerReceiver(this.mCPPInfoReceiver, this.filter);
    }

    public void initialiseCPLocationProvider() {
        Log.d(TAG, "initialiseCPLocationProvider..");
        loadNativeLibrary();
        createJNIObject();
        this.mSrvComm = new CPPSrvCommunicator(this.mContext, this.mHandler);
        this.mPolicyHandler = new CPPPolicyHandler(this.mContext, this.mHandler);
        this.mDbAdapter = new CPPDbAdapter(this.mContext);
        this.mSimSlotCount = MultiSimManager.getSimSlotCount();
        this.mDbAdapter.open();
        initSamsungLocationEngine();
        this.mPolicyHandler.init();
        this.filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.filter.addAction("android.net.wifi.STATE_CHANGE");
        this.filter.addAction("android.net.wifi.SCAN_RESULTS");
        this.filter.addAction(INTENT_UPDATE_POLICY);
        this.filter.addAction(INTENT_BOOT_COMPLETED);
        this.filter.addAction(INTENT_DIAGNOSTIC_REPORT_CHANGED);
        this.filter.addAction(INTENT_ACTION_SUBINFO_RECORD_UPDATED);
        this.filter.addAction("android.intent.action.ACTION_SHUTDOWN");
        this.mContext.registerReceiver(this.mCPPInfoReceiver, this.filter);
    }

    public void deInitialiseCPLocationProvider() {
        Log.d(TAG, "deInitialiseCPLocationProvider..");
        if (this.mReportLocationStatus || this.flagEnableCLM) {
            Log.d(TAG, "disable(): called before cp_stop so calling cp_stop first");
            this.mHandler.sendEmptyMessage(4);
            this.mReportLocationStatus = false;
        }
        deInitSamsungLocationEngine();
        this.mDbAdapter.close();
        this.mContext.unregisterReceiver(this.mCPPInfoReceiver);
        this.filter = new IntentFilter();
        this.filter.addAction(INTENT_CLM_TT_START_BY_APP);
        this.filter.addAction(INTENT_CLM_TT_STOP_BY_APP);
        this.mContext.registerReceiver(this.mCPPInfoReceiver, this.filter);
    }

    public boolean isEnabled() {
        return this.flagEnableCPP;
    }

    public void requestCPLocationUpdates(RequestLocationInput input) {
        Log.d(TAG, "requestCPLocationUpdate:  Interval is " + input.mInterval + "msec / Displacement is : " + input.mMinDist + "mtrs / mode :" + input.mMode);
        if (input.mCppLocListener == null) {
            Log.e(TAG, "parameters are not valid.Listener is NULL");
        }
        if (!this.mReportLocationStatus || this.mCurrentReqMode == input.mMode) {
            this.mReportLocationStatus = true;
            this.mCpLocListener = input.mCppLocListener;
            this.mCurrentReqMode = input.mMode;
            if (input.mInterval < 5000) {
                input.mInterval = 5000;
            }
            this.mHandler.obtainMessage(3, input.mInterval, input.mMinDist).sendToTarget();
            return;
        }
        Log.e(TAG, "CP Location session all ready running");
    }

    public void stopCPLocationUpdates(ICPPLocationListener cppLocListener) {
        Log.d(TAG, "stopCPLocationUpdates()..");
        if (this.mReportLocationStatus) {
            this.mHandler.obtainMessage(4, cppLocListener).sendToTarget();
            this.mReportLocationStatus = false;
            this.mCpLocListener = null;
            return;
        }
        Log.d(TAG, "stopCPLocationUpdates()- No Location updates ongoing.");
    }

    public void registerCPGeoFence(RequestCPGeoFenceRegister input) {
        Log.e(TAG, "registerCPGeoFence() latitude : " + input.mLatitude + ", longitude : " + input.mLongitude + ", geoMode : " + input.mGeoMode + ", radius : " + input.mRadius + ", period : " + input.mPeriod);
        this.mHandler.obtainMessage(9, input).sendToTarget();
    }

    public void deRegisterCPGeoFence(int clientID) {
        Log.d(TAG, "deRegisterCPGeoFence()..");
        Message msg = this.mHandler.obtainMessage(10);
        msg.arg1 = clientID;
        msg.sendToTarget();
    }

    private void reportLocation(int flags, int latitude, int longitude, int altitude, int speed, int bearing, int accuracy, long timestamp) {
        double convLatitude = ((double) latitude) / DEGREE_RESOLUTION;
        double convLongitude = ((double) longitude) / DEGREE_RESOLUTION;
        float convAccuracy = (float) accuracy;
        Log.d(TAG, "ReportLocation by native : " + convLatitude + ", " + convLongitude + ", " + altitude);
        Log.d(TAG, "ReportLocation by native : " + speed + ", " + bearing + ", " + convAccuracy + ", " + timestamp);
        Location location = new Location("CPPLocationProvider");
        location.setLatitude(convLatitude);
        location.setLongitude(convLongitude);
        location.setAccuracy(convAccuracy);
        location.setTime(System.currentTimeMillis());
        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        Bundle extras = location.getExtras();
        if (extras == null) {
            extras = new Bundle();
        }
        extras.putBoolean("CPP:source-CpLocation", true);
        location.setExtras(extras);
        this.mHandler.obtainMessage(5, location).sendToTarget();
    }

    private void updateServiceState(int slotId, int currentState) {
        Log.d(TAG, "Slot : " + slotId + " Service State Changed : " + currentState);
        if (slotId <= 0) {
            return;
        }
        if (currentState == 0) {
            this.mSimSlotConnected |= slotId;
            Log.d(TAG, "Current SIM connected " + this.mSimSlotConnected);
            return;
        }
        this.mSimSlotConnected &= slotId ^ -1;
        Log.d(TAG, "Current SIM connected " + this.mSimSlotConnected);
    }

    public int insertGeoFencePOI(RequestCPGeoFenceRegister input) {
        return this.mDbAdapter.insertGeoReq(input);
    }

    private void cellHistoryCallBack(byte[] rawData) {
        Log.d(TAG, "cellHistoryCallBack() length : " + rawData.length);
        makeJsonForGeo(false, rawData);
    }

    private void geoFenceCallBack(byte[] rawData) {
        Log.d(TAG, "geoFenceCallBack() length : " + rawData.length);
        new int[1][0] = 0;
        new short[1][0] = (short) 0;
        startWifiScan();
        try {
            ByteBuffer bb = ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN);
            Log.d(TAG, "geoFenceCallBack() ByteBuffer : " + bb.toString());
            byte type = bb.get();
            int clientID;
            if (type == (byte) 0) {
                clientID = bb.getInt();
                byte result = bb.get();
                Log.d(TAG, "geoFenceCallBack() type : " + type + ", clientID : " + clientID + ", result : " + result + ", errorCode : " + bb.get());
                if (result != (byte) 0) {
                    Log.e(TAG, "geoFenceCallBack onError cb null");
                }
            } else if (type == (byte) 1) {
                clientID = bb.getInt();
                geoMode = bb.get();
                Log.d(TAG, "geoFenceCallBack() type : " + type + ", clientID : " + clientID + ", geoMode : " + geoMode + ", cellId : " + bb.getInt());
                intent = new Intent("android.intent.action.ACTION_CLM_TT_GEO_FENCE_UPDATE");
                intent.putExtra(CPPositioningManager.GEOFENCE_ID, clientID);
                intent.putExtra("geoMode", geoMode);
                intent.addFlags(32);
                this.mContext.sendBroadcast(intent);
            } else {
                clientID = bb.getInt();
                geoMode = bb.get();
                int lat = bb.getInt();
                int lon = bb.getInt();
                short accuracy = bb.getShort();
                Log.d(TAG, "geoFenceCallBack() type : " + type + ", clientID : " + clientID + ", geoMode : " + geoMode);
                if (accuracy < (short) 8) {
                    startWifiScan();
                    Message msg = Message.obtain();
                    Bundle cb = new Bundle();
                    cb.putByteArray(GEO_CB, rawData);
                    msg.what = 11;
                    msg.setData(cb);
                    this.mHandler.sendMessageDelayed(msg, 4000);
                    return;
                }
                intent = new Intent("android.intent.action.ACTION_CLM_TT_GEO_FENCE_UPDATE");
                intent.putExtra(CPPositioningManager.GEOFENCE_ID, clientID);
                intent.putExtra("geoMode", geoMode);
                intent.addFlags(32);
                this.mContext.sendBroadcast(intent);
            }
        } catch (Exception i) {
            i.printStackTrace();
        }
    }

    private void requestCellDBDownload(byte[] rawData) {
        Log.d(TAG, "requestCellDBDownload() length : " + rawData.length);
        long req_id = 0;
        if (rawData.length > 0) {
            req_id = this.mDbAdapter.insertReq(rawData);
        } else {
            Log.d(TAG, "requestCellDBDownload() Data received from Native layer is NULL or Empty ");
        }
        if (req_id != 0) {
            sendRequest(req_id);
        }
    }

    private void loadNativeLibrary() {
        Log.d(TAG, "loadNativeLibrary..");
        try {
            System.loadLibrary("cppjni");
        } catch (Throwable e) {
            RuntimeException runtimeException = new RuntimeException("Failed to load lib cppjni.so:" + e.getMessage());
        }
    }

    public void sendRequest(long reqId) {
        Cursor mC1 = this.mDbAdapter.getReq(CPPDbAdapter.KEY_ID_REQ, Long.toString(reqId));
        if (!(mC1 == null || !mC1.moveToFirst() || mC1.isClosed())) {
            Log.d(TAG, "sendRequest() Request Count in Req DB : " + mC1.getCount());
            int rat = mC1.getInt(mC1.getColumnIndex(CPPDbAdapter.KEY_RAT));
            int mode = mC1.getInt(mC1.getColumnIndex(CPPDbAdapter.KEY_MODE));
            int isSending = mC1.getInt(mC1.getColumnIndex(CPPDbAdapter.KEY_IS_SENDING));
            Log.d(TAG, "sendRequest() reqId : " + reqId);
            if (isSending == 0 && (mode != 0 || mWifiConnected)) {
                String jsonString = makeJsonForDbRequest(mC1, mode);
                if (1 == mode || 3 == mode) {
                    sendRespToCp(reqId);
                }
                if (jsonString != null) {
                    if (3 == mode || jsonString.equalsIgnoreCase("exist")) {
                        this.mDbAdapter.deleteReqById(reqId);
                    } else {
                        Log.d(TAG, "sendRequest() Not exist in local DB");
                        this.mDbAdapter.updateSending(reqId, true);
                        this.mSrvComm.sendToSrv(CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB, jsonString, reqId, rat, mode);
                    }
                }
            }
        }
        if (mC1 != null && !mC1.isClosed()) {
            mC1.close();
        }
    }

    public void sendLargeRequest() {
        Cursor mC1 = this.mDbAdapter.getReq(CPPDbAdapter.KEY_MODE, Integer.toString(0));
        if (mWifiConnected && mC1 != null && mC1.moveToFirst() && !mC1.isClosed()) {
            Log.d(TAG, "sendLargeRequest() Large Scale Request Count in Req DB : " + mC1.getCount());
            long req_id = mC1.getLong(mC1.getColumnIndex(CPPDbAdapter.KEY_ID_REQ));
            int rat = mC1.getInt(mC1.getColumnIndex(CPPDbAdapter.KEY_RAT));
            int isSending = mC1.getInt(mC1.getColumnIndex(CPPDbAdapter.KEY_IS_SENDING));
            Log.d(TAG, "sendLargeRequest() Large Scale req_id : " + req_id);
            if (isSending == 0) {
                String jsonString = makeJsonForDbRequest(mC1, 0);
                if (!(jsonString == null || jsonString.equalsIgnoreCase("exist"))) {
                    Log.d(TAG, "sendLargeRequest() Send Large Request to Server");
                    this.mDbAdapter.updateSending(req_id, true);
                    this.mSrvComm.sendToSrv(CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB, jsonString, req_id, rat, 0);
                }
            }
        }
        if (mC1 != null && !mC1.isClosed()) {
            mC1.close();
        }
    }

    public String makeJsonForDbRequest(Cursor mCursor, int mode) {
        int existCount = 0;
        JSONObject mJson = new JSONObject();
        JSONArray mJsonPatches = new JSONArray();
        try {
            long reqId = mCursor.getLong(mCursor.getColumnIndex(CPPDbAdapter.KEY_ID_REQ));
            int rat = mCursor.getInt(mCursor.getColumnIndex(CPPDbAdapter.KEY_RAT));
            int sN = mCursor.getInt(mCursor.getColumnIndex(CPPDbAdapter.KEY_SN));
            mJson.put("sN", sN);
            JSONObject jSONObject = mJson;
            jSONObject.put("d0", mCursor.getInt(mCursor.getColumnIndex(CPPDbAdapter.KEY_REQ_TYPE)));
            jSONObject = mJson;
            jSONObject.put("d1", mCursor.getInt(mCursor.getColumnIndex(CPPDbAdapter.KEY_DB_TYPE)));
            jSONObject = mJson;
            jSONObject.put("d2", mCursor.getInt(mCursor.getColumnIndex(CPPDbAdapter.KEY_PATCH_CLASS)));
            int mcc = mCursor.getInt(mCursor.getColumnIndex("mcc"));
            int mnc = mCursor.getInt(mCursor.getColumnIndex(CPPDbAdapter.KEY_MNC));
            mJson.put("d3", mcc);
            mJson.put("d4", mnc);
            String plmn = this.mPolicyHandler.setPLMN(mcc, mnc);
            int tac = mCursor.getInt(mCursor.getColumnIndex(CPPDbAdapter.KEY_TAC));
            mJson.put("d5", tac);
            long gci = mCursor.getLong(mCursor.getColumnIndex(CPPDbAdapter.KEY_GCI));
            if (1 == mode && gci > 0) {
                int pci = mCursor.getInt(mCursor.getColumnIndex(CPPDbAdapter.KEY_PCI));
                int fcn = mCursor.getInt(mCursor.getColumnIndex(CPPDbAdapter.KEY_FCN));
                mJson.put("d6", gci);
                mJson.put("d16", pci);
                mJson.put("d17", fcn);
            }
            long utc = getUtc();
            long encKey = getEncKey(tac, utc, this.mPolicyHandler.getPLMN(plmn));
            Log.d(TAG, "makeJsonForDbRequest() : encKey = " + encKey);
            if (encKey < 0) {
                Log.d(TAG, "makeJsonForDbRequest() : encKey < 0 --> JSON return null + sendMessage(MSG_UPDATE_POLICY)");
                CPPPolicyHandler.mInvalidPolicyID = true;
                Message msg = Message.obtain();
                msg.what = 7;
                msg.arg1 = 1;
                this.mHandler.sendMessage(msg);
                this.mDbAdapter.deleteReqById(reqId);
                return null;
            }
            int utcKey = getUtcKey(utc);
            mJson.put("d9", encKey);
            this.mDbAdapter.arrayUtc.put(reqId, Long.valueOf(utc));
            this.mDbAdapter.arrayUtcKey.put(reqId, Integer.valueOf(utcKey));
            Cursor mC1 = this.mDbAdapter.getPatch(reqId);
            if (!(mC1 == null || !mC1.moveToFirst() || mC1.isClosed())) {
                int numPatch = mC1.getCount();
                Log.d(TAG, "makeJsonForDbRequest() reqId : " + reqId + ", numPatch = " + numPatch);
                if (numPatch > 0) {
                    int i = 0;
                    int patchCount = 0;
                    while (i < numPatch) {
                        long newVersion;
                        int patchCount2;
                        int latIndex = mC1.getInt(mC1.getColumnIndex(CPPDbAdapter.KEY_LATINDEX));
                        int lonIndex = mC1.getInt(mC1.getColumnIndex(CPPDbAdapter.KEY_LONINDEX));
                        int patch_fcn = mC1.getInt(mC1.getColumnIndex(CPPDbAdapter.KEY_PATCH_FCN));
                        long version = mC1.getLong(mC1.getColumnIndex(CPPDbAdapter.KEY_VERSION));
                        if (1 == mode || 3 == mode) {
                            newVersion = this.mDbAdapter.checkExistedPatchResp(reqId, rat, sN, latIndex, lonIndex, patch_fcn, version);
                        } else {
                            newVersion = version;
                        }
                        if (newVersion > 0) {
                            Log.d(TAG, "makeJsonForDbRequest() patch[" + i + "] is NOT EXIST in DB, version : " + version + ", latIndex : " + latIndex + ", lonIndex : " + lonIndex + ", patch_fcn : " + patch_fcn);
                            JSONObject _mJsonPatch = new JSONObject();
                            _mJsonPatch.put("d11", newVersion);
                            _mJsonPatch.put("d12", latIndex ^ utcKey);
                            _mJsonPatch.put("d13", lonIndex ^ utcKey);
                            _mJsonPatch.put("d14", patch_fcn);
                            patchCount2 = patchCount + 1;
                            mJsonPatches.put(patchCount, _mJsonPatch);
                        } else {
                            existCount++;
                            Log.d(TAG, "makeJsonForDbRequest() patch[" + i + "] is EXIST in DB, version : " + version + ", latIndex : " + latIndex + ", lonIndex : " + lonIndex + ", patch_fcn : " + patch_fcn);
                            if (numPatch == existCount) {
                                Log.d(TAG, "makeJsonForDbRequest() Every Patches are in local DB");
                                mC1.close();
                                return "exist";
                            }
                            patchCount2 = patchCount;
                        }
                        mC1.moveToNext();
                        i++;
                        patchCount = patchCount2;
                    }
                    mJson.put("d10", mJsonPatches);
                }
            }
            if (!(mC1 == null || mC1.isClosed())) {
                mC1.close();
            }
            Log.d(TAG, "makeJsonForDbRequest() FINAL JSON : " + mJson.toString());
            return mJson.toString();
        } catch (Throwable jsonE) {
            Log.d(TAG, "makeJsonForDbRequest() JSONException : " + jsonE);
            throw new RuntimeException(jsonE);
        }
    }

    private void makeJsonForGeo(boolean isGeo, byte[] data) {
        byte geoMode = (byte) 3;
        int clientID = -1;
        int tac = 0;
        JSONObject mJson = new JSONObject();
        try {
            ByteBuffer bb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
            Log.d(TAG, "makeJsonForGeo() ByteBuffer " + bb.toString());
            if (isGeo) {
                byte type = bb.get();
                clientID = bb.getInt();
                geoMode = bb.get();
            } else {
                long j = (long) bb.getInt();
            }
            int lat = bb.getInt();
            int lon = bb.getInt();
            short accuracy = bb.getShort();
            long timestamp = (long) bb.getInt();
            short mcc = bb.getShort();
            short mnc = bb.getShort();
            byte rat = bb.get();
            short measuredCellNum = (short) bb.get();
            if (isGeo) {
                tac = bb.getShort() & 65535;
            }
            int fcn = bb.getShort() & 65535;
            long gci = ((long) bb.getInt()) & 4294967295L;
            int pci = bb.getShort();
            mJson.put("type", 1);
            mJson.put("mcc", mcc);
            mJson.put(CPPDbAdapter.KEY_MNC, mnc);
            if (isGeo) {
                mJson.put(CPPDbAdapter.KEY_TAC, tac);
            }
            mJson.put("cid", gci);
            mJson.put(CPPDbAdapter.KEY_PCI, pci);
            mJson.put(CPPDbAdapter.KEY_FCN, fcn);
            mJson.put("longitude", lon);
            mJson.put("latitude", lat);
            if (this.wifiResults != null && this.mWifiScanning) {
                JSONArray mJsonWifiArray = new JSONArray();
                int wifiCount = 0;
                for (ScanResult result : this.wifiResults) {
                    JSONObject mJsonWifi = new JSONObject();
                    mJsonWifi.put("macAddress", result.BSSID);
                    mJsonWifi.put("rssi", result.level);
                    int wifiCount2 = wifiCount + 1;
                    mJsonWifiArray.put(wifiCount, mJsonWifi);
                    wifiCount = wifiCount2;
                }
                mJson.put("arrMacAddress", mJsonWifiArray);
                this.mWifiScanning = false;
            }
        } catch (JSONException jsonE) {
            Log.d(TAG, "makeJsonForCellHistory() JSONException : " + jsonE);
            throw new RuntimeException(jsonE);
        } catch (Exception e) {
            Log.d(TAG, "makeJsonForCellHistory() Exception : " + e);
            e.printStackTrace();
        }
        if (mJson.toString() != null) {
            Log.d(TAG, "makeJsonForGeo() JSON : " + mJson.toString());
            this.mSrvComm.sendToSrvForLoc(clientID, geoMode, mJson.toString());
        }
    }

    public void sendRespToCp(long req_id) {
        Log.d(TAG, "sendRespToCp() req_id : " + req_id);
        try {
            byte[] result = this.mDbAdapter.getRespForCp(req_id);
            if (result != null) {
                Log.d(TAG, "sendRespToCp() result : " + Arrays.toString(result) + ", result.length : " + result.length);
                sendBytesToCp(result, result.length, 2);
                this.mDbAdapter.deleteReqId(req_id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRespToCpDirect(byte[] result, long req_id, int rat) {
        Log.d(TAG, "sendRespToCpDirect() req_id : " + req_id);
        try {
            Log.d(TAG, "sendRespToCpDirect() result length : " + result.length);
            byte[] init = new byte[]{(byte) 34, (byte) 86, (byte) 97, (byte) 108, (byte) 117, (byte) 101, (byte) 34, (byte) 58};
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(init);
            bos.write(0);
            Integer utcKey = (Integer) this.mDbAdapter.arrayUtcKey.get(req_id);
            if (utcKey != null) {
                bos.write(CPPDbAdapter.intToReverseByte(utcKey.intValue()));
                bos.write((byte) rat);
                bos.write(Arrays.copyOfRange(result, 0, result.length));
                Log.d(TAG, "sendRespToCpDirect() - bos length : " + bos.toByteArray().length);
                sendBytesToCp(bos.toByteArray(), bos.toByteArray().length, 2);
                this.mDbAdapter.deleteReqId(req_id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long getUtc() {
        return System.currentTimeMillis() / 1000;
    }

    private long getEncKey(int tac, long utc) {
        long policyId = this.mPolicyHandler.getPolicyId().longValue();
        if (policyId < 0) {
            return -1;
        }
        long encKey = 4294967295L & ((((long) (tac << 16)) ^ utc) ^ policyId);
        Log.d(TAG, "getEncKey() tac: " + tac + "(" + Integer.toBinaryString(tac) + "), utc : " + utc + ", PolicyId : " + policyId + ", EncKey : " + encKey);
        return encKey;
    }

    private long getEncKey(int tac, long utc, String plmn) {
        long policyId = this.mPolicyHandler.getPolicyId(plmn).longValue();
        if (policyId < 0) {
            return -1;
        }
        long encKey = 4294967295L & ((((long) (tac << 16)) ^ utc) ^ policyId);
        Log.d(TAG, "getEncKey() tac: " + tac + "(" + Integer.toBinaryString(tac) + "), utc : " + utc + ", PolicyId : " + policyId + ", EncKey : " + encKey);
        return encKey;
    }

    private int getUtcKey(long utc) {
        return (int) (65535 & utc);
    }

    private void startWifiScan() {
        if (!this.mWifiScanning) {
            this.mWifiScanning = true;
            this.wifi = (WifiManager) this.mContext.getSystemService("wifi");
            this.wifiResults = null;
            this.wifi.startScan();
        }
    }
}
