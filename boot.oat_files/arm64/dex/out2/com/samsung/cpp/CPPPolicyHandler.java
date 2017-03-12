package com.samsung.cpp;

import android.content.Context;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.sec.android.emergencymode.EmergencyConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class CPPPolicyHandler {
    public static final int CP_MODE_SEND_CELL_DB = 2;
    public static final int CP_MODE_SEND_POLICY_UPDATE = 3;
    public static final String DEFAULT_CHN_SERVER_URL = "https://cn-prod-celltw.secb2b.com.cn/";
    public static final String DEFAULT_CHN_WIFI_SERVER_URL = "https://cn-prod-wifi-celltw.secb2b.com.cn/";
    public static final String DEFAULT_SERVER_URL = "https://prod-celltw.secb2b.com/";
    public static final String DEFAULT_VERSION = "19700102-00:00";
    public static final String DEFAULT_WIFI_SERVER_URL = "https://prod-celltw.secb2b.com/";
    public static final int RAT_TYPE_CDMA1X = 8;
    public static final int RAT_TYPE_GSM = 4;
    public static final int RAT_TYPE_LTE = 1;
    public static final int RAT_TYPE_TDSCDMA = 16;
    public static final int RAT_TYPE_WCDMA = 2;
    public static final int RAT_TYPE_WIFI = 32;
    private static final String TAG = "CPPPolicyHandler";
    private static final int defaultTimeToNextPolicyUpdateInDay = 30;
    public static boolean mInvalidPolicyID;
    private static HashMap<String, String> mapCurrentPolicy;
    private static HashMap<String, String> mapCurrentPolicyList;
    private static HashMap<String, String> mapDefaultPolicy;
    private static HashMap<String, String> mapPolicyToCp;
    private String dirNamePolicy = "/data/system/";
    private String fileNameToCurrentPolicy = "/data/system/fileCurrentPolicy";
    private String fileNameToCurrentPolicyList = "/data/system/fileCurrentPolicyList";
    private String fileNameToDefaultPolicy = "/data/system/fileDefaultPolicy";
    private Context mContext;
    public boolean mCurrentPolicyFileExists;
    FileObserver mDirObserver = new FileObserver(this.dirNamePolicy, 264) {
        public synchronized void onEvent(int event, String path) {
            String fullPath = CPPPolicyHandler.this.dirNamePolicy + path;
            boolean isCurrentPolicy = fullPath.equalsIgnoreCase(CPPPolicyHandler.this.fileNameToCurrentPolicy);
            boolean isCurrentPolicyList = fullPath.equalsIgnoreCase(CPPPolicyHandler.this.fileNameToCurrentPolicyList);
            switch (event) {
                case 8:
                    if (isCurrentPolicy) {
                        Log.d(CPPPolicyHandler.TAG, "FileObserver.MODIFICATION(" + event + ") detected, filePath : " + fullPath + " --> refresh current policy");
                        CPPPolicyHandler.this.setPolicy();
                        CPPPolicyHandler.this.notifyPolicyUpdate();
                        break;
                    }
                    break;
                case 256:
                    if (isCurrentPolicy) {
                        Log.d(CPPPolicyHandler.TAG, "FileObserver.CREATE(" + event + ") detected, filePath : " + fullPath);
                        break;
                    }
                    break;
            }
        }
    };
    private Handler mHandler;
    int mMobileTotalCount;
    int mSizeofMemory;
    int mWifiTotalCount;
    public String plmn;

    private boolean isDefaultPolicyChanged() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH:mm");
        boolean result = false;
        try {
            Date newVersion = dateFormat.parse(DEFAULT_VERSION);
            Date existedApVersion = dateFormat.parse((String) mapDefaultPolicy.get("defaultVersion"));
            Log.i(TAG, "isDefaultPolicyChanged : { existed_version : " + dateFormat.format(existedApVersion) + ", default_version : " + dateFormat.format(newVersion) + " }");
            switch (newVersion.compareTo(existedApVersion)) {
                case 1:
                    result = true;
                    break;
            }
            Log.i(TAG, "isDefaultPolicyChanged : result : " + result);
            return result;
        } catch (NullPointerException e) {
            Log.e(TAG, "isDefaultPolicyChanged : NullPointerException = " + e);
            return true;
        } catch (ParseException e2) {
            Log.e(TAG, "isDefaultPolicyChanged : ParseException = " + e2);
            return true;
        }
    }

    public boolean isPolicyFileExist(String filename) {
        boolean ret = new File(filename).exists();
        Log.d(TAG, "isPolicyFileExist : ( " + filename + " ) = " + ret);
        return ret;
    }

    private void initialisePolicyFiles() {
        Log.d(TAG, "initialisePolicyFiles : setDefaultPolicy + sendMessage(MSG_UPDATE_POLICY)/initialisePolicyFiles");
        setDefaultPolicy(true);
        Message reqMsg = Message.obtain();
        reqMsg.what = 7;
        reqMsg.arg1 = 1;
        this.mHandler.sendMessage(reqMsg);
    }

    private void setPolicy() {
        Log.d(TAG, "setPolicy() ");
        try {
            mapDefaultPolicy = retrievePolicy(this.fileNameToDefaultPolicy, mapDefaultPolicy);
            mapCurrentPolicy = retrievePolicy(this.fileNameToCurrentPolicy, mapCurrentPolicy);
            if (mapCurrentPolicy == null || mapCurrentPolicy.isEmpty()) {
                throw new IOException();
            } else if (isDefaultPolicyChanged()) {
                setDefaultPolicy(true);
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "No Policy files");
            setDefaultPolicy(true);
        } catch (ClassNotFoundException e2) {
            Log.e(TAG, "No Class files");
            setDefaultPolicy(true);
        } catch (IOException e3) {
            Log.e(TAG, "IO Exception");
            setDefaultPolicy(true);
        }
    }

    private void setPolicyList() {
        Log.d(TAG, "setPolicyList() ");
        try {
            if (isPolicyFileExist(this.fileNameToCurrentPolicyList)) {
                mapCurrentPolicyList = retrievePolicy(this.fileNameToCurrentPolicyList, mapCurrentPolicyList);
            } else {
                savePolicy(this.fileNameToCurrentPolicyList, mapCurrentPolicyList);
            }
            if (mapCurrentPolicyList == null) {
                throw new IOException();
            } else if (!mapCurrentPolicyList.isEmpty() || !this.mCurrentPolicyFileExists) {
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "setPolicyList : FileNotFoundException");
        } catch (ClassNotFoundException e2) {
            Log.e(TAG, "setPolicyList : ClassNotFoundException");
        } catch (IOException e3) {
            Log.e(TAG, "setPolicyList : IOException");
        }
    }

    private void savePolicy(String fileName, Map<String, String> mapToSave) {
        Throwable th;
        Log.d(TAG, "savePolicy : " + fileName);
        FileOutputStream fos = null;
        try {
            Log.d(TAG, "savePolicy : " + mapToSave.toString());
            FileOutputStream fos2 = new FileOutputStream(fileName);
            try {
                ObjectOutputStream oos = new ObjectOutputStream(fos2);
                oos.writeObject(mapToSave);
                oos.close();
                if (fos2 != null) {
                    try {
                        fos2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        fos = fos2;
                        return;
                    }
                }
                fos = fos2;
            } catch (FileNotFoundException e2) {
                fos = fos2;
                try {
                    Log.e(TAG, "FileNotFoundException");
                    setDefaultPolicy(true);
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e32) {
                            e32.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (IOException e4) {
                fos = fos2;
                Log.e(TAG, "IOException");
                setDefaultPolicy(true);
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e322) {
                        e322.printStackTrace();
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                fos = fos2;
                if (fos != null) {
                    fos.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e5) {
            Log.e(TAG, "FileNotFoundException");
            setDefaultPolicy(true);
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e6) {
            Log.e(TAG, "IOException");
            setDefaultPolicy(true);
            if (fos != null) {
                fos.close();
            }
        }
    }

    private HashMap<String, String> retrievePolicy(String fileName, HashMap<String, String> _map) throws IOException, ClassNotFoundException {
        Exception ex;
        Throwable th;
        Log.d(TAG, "retrievePolicy. filename :  " + fileName);
        HashMap<String, String> retrievedMap = _map;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        if (fileName != null) {
            try {
                FileInputStream fis2 = new FileInputStream(fileName);
                try {
                    ObjectInputStream ois2 = new ObjectInputStream(fis2);
                    try {
                        retrievedMap = (HashMap) ois2.readObject();
                        if (fis2 != null) {
                            try {
                                fis2.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                ois = ois2;
                                fis = fis2;
                            }
                        }
                        if (ois2 != null) {
                            ois2.close();
                        }
                        ois = ois2;
                        fis = fis2;
                    } catch (Exception e2) {
                        ex = e2;
                        ois = ois2;
                        fis = fis2;
                        try {
                            ex.printStackTrace();
                            if (fis != null) {
                                try {
                                    fis.close();
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                    return _map;
                                }
                            }
                            if (ois != null) {
                                return _map;
                            }
                            ois.close();
                            return _map;
                        } catch (Throwable th2) {
                            th = th2;
                            if (fis != null) {
                                try {
                                    fis.close();
                                } catch (IOException e32) {
                                    e32.printStackTrace();
                                    throw th;
                                }
                            }
                            if (ois != null) {
                                ois.close();
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        ois = ois2;
                        fis = fis2;
                        if (fis != null) {
                            fis.close();
                        }
                        if (ois != null) {
                            ois.close();
                        }
                        throw th;
                    }
                } catch (Exception e4) {
                    ex = e4;
                    fis = fis2;
                    ex.printStackTrace();
                    if (fis != null) {
                        fis.close();
                    }
                    if (ois != null) {
                        return _map;
                    }
                    ois.close();
                    return _map;
                } catch (Throwable th4) {
                    th = th4;
                    fis = fis2;
                    if (fis != null) {
                        fis.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                    throw th;
                }
            } catch (Exception e5) {
                ex = e5;
                ex.printStackTrace();
                if (fis != null) {
                    fis.close();
                }
                if (ois != null) {
                    return _map;
                }
                ois.close();
                return _map;
            }
        }
        return retrievedMap;
    }

    public CPPPolicyHandler(Context context, Handler handler) {
        Log.d(TAG, "CPPPolicyHandler constructor. ");
        this.mContext = context;
        this.mHandler = handler;
        mapPolicyToCp = new HashMap();
        mapDefaultPolicy = new HashMap();
        mapCurrentPolicy = new HashMap();
        mapCurrentPolicyList = new HashMap();
        mInvalidPolicyID = false;
        this.mMobileTotalCount = 0;
        this.mWifiTotalCount = 0;
        this.mSizeofMemory = 0;
        this.plmn = null;
        this.mDirObserver.startWatching();
    }

    public void init() {
        if (isPolicyFileExist(this.fileNameToDefaultPolicy) && isPolicyFileExist(this.fileNameToCurrentPolicy)) {
            this.mCurrentPolicyFileExists = true;
            Log.d(TAG, "init : Policies exist. (mCurrentPolicyFileExists = " + this.mCurrentPolicyFileExists + ")");
            setPolicy();
        } else {
            this.mCurrentPolicyFileExists = false;
            Log.d(TAG, "init : Policy downloading is needed. (mCurrentPolicyFileExists = " + this.mCurrentPolicyFileExists + ")");
            initialisePolicyFiles();
        }
        setPolicyList();
    }

    public void notifyPolicyUpdate() {
        Message reqMsg = Message.obtain();
        reqMsg.what = 8;
        reqMsg.arg1 = getPolicyTtAct();
        this.mHandler.sendMessage(reqMsg);
    }

    public JSONObject getTTJSONObject() {
        try {
            return getTTJSONObject((String) mapCurrentPolicy.get("json"), (String) mapCurrentPolicy.get("updatedPolicyMccMnc"));
        } catch (NullPointerException e) {
            Log.e(TAG, "getTTJSONObject : NullPointerException");
            return null;
        }
    }

    public JSONObject getTTJSONObject(String data, String policyPlmn) {
        JSONException jsonE;
        Exception e;
        Log.d(TAG, "getTTJSONObject");
        JSONObject jsonTT = new JSONObject();
        try {
            JSONObject jsonTT2 = new JSONObject(new JSONObject(data).getJSONObject(EmergencyConstants.VALUE), new String[]{"policyId", "ttAct", "ttItems"});
            try {
                String plmn = getPLMN(policyPlmn);
                if (plmn != null && plmn.length() >= 5) {
                    String mcc = plmn.substring(0, 3);
                    String mnc = plmn.substring(3);
                    jsonTT2.put("mcc", Integer.valueOf(mcc));
                    jsonTT2.put(CPPDbAdapter.KEY_MNC, Integer.valueOf(mnc));
                }
                jsonTT2.put("utcTime", getUtc());
                Log.d(TAG, "getTTJSONObject : TT policy items' only = " + jsonTT2.toString());
                jsonTT = jsonTT2;
                return jsonTT2;
            } catch (JSONException e2) {
                jsonE = e2;
                jsonTT = jsonTT2;
                jsonE.printStackTrace();
                return null;
            } catch (Exception e3) {
                e = e3;
                jsonTT = jsonTT2;
                e.printStackTrace();
                return null;
            }
        } catch (JSONException e4) {
            jsonE = e4;
            jsonE.printStackTrace();
            return null;
        } catch (Exception e5) {
            e = e5;
            e.printStackTrace();
            return null;
        }
    }

    public boolean updatePolicy(String data, String body) {
        Log.d(TAG, "updatePolicy : " + data);
        try {
            Set<String> setFieldToCp = new HashSet();
            Set<String> setFieldToAp = new HashSet();
            Collections.addAll(setFieldToCp, new String[]{"policyId", "ttAct", "ttItems", "ttID", "ttVal"});
            Collections.addAll(setFieldToAp, new String[]{CPPDbAdapter.KEY_VERSION, "nextPolicyUpdate", "dbCollection", "neighborCellInfo", "reportInterval", "valuableThreshold", "sizeOfMemory", "primaryServerAddress", "secondaryServerAddress", "forcedGPS", "targetTrackingArea", "targetCell", "measurementElement", "highMeasureInterval", "midMeasureInterval", "lowMeasureInterval", "gpsAccuracyThreshold", "samplingRAT", "reportingRAT", "retryPeriod", "cellInfo", "policyId", "ttAct", "ttItems", "ttID", "ttVal"});
            JSONObject jsonValue = new JSONObject(new JSONObject(data).getString(EmergencyConstants.VALUE));
            Iterator<String> iterFieldInJsonValue = jsonValue.keys();
            while (iterFieldInJsonValue.hasNext()) {
                String field = (String) iterFieldInJsonValue.next();
                if (setFieldToCp.contains(field)) {
                    mapPolicyToCp.put(field, jsonValue.getString(field));
                }
                if (setFieldToAp.contains(field)) {
                    mapCurrentPolicy.put(field, jsonValue.getString(field));
                    if (field.equalsIgnoreCase("policyId")) {
                        String plmn = getPLMN(body);
                        if (plmn != null) {
                            mapCurrentPolicyList.put(plmn, jsonValue.getString("policyId"));
                            savePolicy(this.fileNameToCurrentPolicyList, mapCurrentPolicyList);
                        }
                    }
                }
            }
            mapCurrentPolicy.put("updatedPolicyMccMnc", body);
            mapCurrentPolicy.put("json", data);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
            Calendar c = Calendar.getInstance();
            c.add(5, Integer.parseInt(jsonValue.getString("nextPolicyUpdate")));
            mapCurrentPolicy.put("dateForNextPolicyUpdate", dateFormat.format(c.getTime()));
            savePolicy(this.fileNameToCurrentPolicy, mapCurrentPolicy);
            if (!this.mCurrentPolicyFileExists) {
                this.mCurrentPolicyFileExists = true;
            }
            if (mInvalidPolicyID) {
                mInvalidPolicyID = false;
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setDefaultPolicy(boolean fullReset) {
        Log.d(TAG, "setDefaultPolicy : fullReset ( " + fullReset + " )");
        mapDefaultPolicy.put("neighborCellInfo", "0");
        mapDefaultPolicy.put("valuableThreshold", "0");
        mapDefaultPolicy.put("forcedGPS", "0");
        mapDefaultPolicy.put("targetTrackingArea", "0");
        mapDefaultPolicy.put("targetCell", "0");
        mapDefaultPolicy.put("measurementElement", "");
        mapDefaultPolicy.put("highMeasureInterval", "60");
        mapDefaultPolicy.put("midMeasureInterval", "30");
        mapDefaultPolicy.put("lowMeasureInterval", "10");
        mapDefaultPolicy.put("gpsAccuracyThreshold", "20");
        mapDefaultPolicy.put("samplingRAT", "31");
        mapDefaultPolicy.put("cellInfo", "0");
        if (fullReset) {
            mapDefaultPolicy.put("defaultVersion", DEFAULT_VERSION);
            mapDefaultPolicy.put(CPPDbAdapter.KEY_VERSION, DEFAULT_VERSION);
            mapDefaultPolicy.put("nextPolicyUpdate", Integer.toString(defaultTimeToNextPolicyUpdateInDay));
        }
        mapDefaultPolicy.put("dateForNextPolicyUpdate", new SimpleDateFormat("yyyyMMddHHmm").format(new Date(System.currentTimeMillis())));
        mapDefaultPolicy.put("dbCollection", "1");
        mapDefaultPolicy.put("reportInterval", "24");
        mapDefaultPolicy.put("sizeOfMemory", "10");
        if (fullReset) {
            mapDefaultPolicy.put("primaryServerAddress", "");
            mapDefaultPolicy.put("secondaryServerAddress", "");
        }
        mapDefaultPolicy.put("reportingRAT", "0");
        mapDefaultPolicy.put("retryPeriod", "24");
        savePolicy(this.fileNameToDefaultPolicy, mapDefaultPolicy);
    }

    public String getPolicyVersion() {
        String policyItem = "";
        Log.d(TAG, "getPolicyVersion ");
        try {
            policyItem = (String) mapCurrentPolicy.get(CPPDbAdapter.KEY_VERSION);
            Log.d(TAG, "Version : " + policyItem);
            return policyItem;
        } catch (NullPointerException e) {
            Log.e(TAG, "getPolicyVersion.Null Pointer Exception : " + e);
            return policyItem;
        }
    }

    public void setPolicyVersion(String version) {
        Log.d(TAG, "setPolicyVersion ");
        if (this.mCurrentPolicyFileExists && mapCurrentPolicy != null) {
            mapCurrentPolicy.put(CPPDbAdapter.KEY_VERSION, version);
            savePolicy(this.fileNameToCurrentPolicy, mapCurrentPolicy);
        }
        Log.d(TAG, "Version : " + version);
    }

    public boolean isPolicyExpiration() {
        Date nowDate = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Log.i(TAG, "isPolicyExpiration ");
        try {
            Date dateForNextPolicyUpdateDate;
            boolean result;
            if (this.mCurrentPolicyFileExists && mapCurrentPolicy != null && mapCurrentPolicy.containsKey("dateForNextPolicyUpdate")) {
                dateForNextPolicyUpdateDate = dateFormat.parse((String) mapCurrentPolicy.get("dateForNextPolicyUpdate"));
            } else {
                dateForNextPolicyUpdateDate = dateFormat.parse((String) mapDefaultPolicy.get("dateForNextPolicyUpdate"));
            }
            Log.i(TAG, "isPolicyExpiration : dateForNextPolicyUpdate { policyFile indicates = " + dateFormat.format(dateForNextPolicyUpdateDate) + ", CurrentTime = " + dateFormat.format(nowDate) + "}");
            switch (nowDate.compareTo(dateForNextPolicyUpdateDate)) {
                case -1:
                    result = false;
                    break;
                case 0:
                case 1:
                    result = true;
                    break;
                default:
                    result = false;
                    break;
            }
            Log.i(TAG, "isPolicyExpiration : result = " + result);
            return result;
        } catch (NullPointerException e) {
            Log.e(TAG, "isPolicyExpiration : NullPointerException = " + e);
            setPolicyExpirationDate(nowDate, false);
            return true;
        } catch (NumberFormatException e2) {
            Log.e(TAG, "isPolicyExpiration : NumberFormatException = " + e2);
            setPolicyExpirationDate(nowDate, false);
            return true;
        } catch (ParseException e3) {
            Log.e(TAG, "isPolicyExpiration : ParseException = " + e3);
            setPolicyExpirationDate(nowDate, false);
            return true;
        }
    }

    public void setPolicyExpirationDate(Date date, boolean erasePolicy) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Log.d(TAG, "setPolicyExpirationDate");
        if (erasePolicy) {
            try {
                setDefaultPolicy(false);
            } catch (NullPointerException e) {
                Log.e(TAG, "setPolicyExpirationDate.Null Pointer Exception : " + e);
            }
        }
        if (this.mCurrentPolicyFileExists && mapCurrentPolicy != null) {
            mapCurrentPolicy.put("dateForNextPolicyUpdate", dateFormat.format(date));
            savePolicy(this.fileNameToCurrentPolicy, mapCurrentPolicy);
        }
        Log.d(TAG, "Renew Expiration Date : " + date);
    }

    public Long getPolicyId(String plmn) {
        long policyItem = -1;
        Log.d(TAG, "getPolicyId(plmn)");
        try {
            if (mapCurrentPolicyList != null && mapCurrentPolicyList.containsKey(plmn)) {
                String temp = (String) mapCurrentPolicyList.get(plmn);
                if (!(temp == null || temp.equalsIgnoreCase(""))) {
                    policyItem = Long.valueOf(temp).longValue();
                }
            }
            Log.d(TAG, "policyId : " + policyItem);
            return Long.valueOf(policyItem);
        } catch (NullPointerException e) {
            Log.e(TAG, "getPolicyId. NullPointerException : " + e.toString());
            return Long.valueOf(-1);
        } catch (NumberFormatException e2) {
            Log.e(TAG, "getPolicyId. NumberFormatException : " + e2.toString());
            return Long.valueOf(-1);
        } catch (Exception e3) {
            Log.e(TAG, "getPolicyId. Exception : " + e3.toString());
            return Long.valueOf(-1);
        }
    }

    public Long getPolicyId() {
        long policyItem = -1;
        Log.d(TAG, "getPolicyId()");
        try {
            String temp = (String) mapCurrentPolicy.get("policyId");
            if (!(temp == null || temp.equalsIgnoreCase(""))) {
                policyItem = Long.valueOf(temp).longValue();
            }
            Log.d(TAG, "policyId : " + policyItem);
            return Long.valueOf(policyItem);
        } catch (NullPointerException e) {
            Log.e(TAG, "getPolicyId. NullPointerException : " + e.toString());
            return Long.valueOf(-1);
        } catch (NumberFormatException e2) {
            Log.e(TAG, "getPolicyId. NumberFormatException : " + e2.toString());
            return Long.valueOf(-1);
        } catch (Exception e3) {
            Log.e(TAG, "getPolicyId. Exception : " + e3.toString());
            return Long.valueOf(-1);
        }
    }

    public int getPolicyTtAct() {
        int policyItem = -1;
        Log.d(TAG, "getPolicyTtAct()");
        try {
            String temp = (String) mapCurrentPolicy.get("ttAct");
            if (!(temp == null || temp.equalsIgnoreCase(""))) {
                policyItem = Integer.valueOf(temp).intValue();
            }
            Log.d(TAG, "ttAct : " + policyItem);
            return policyItem;
        } catch (NullPointerException e) {
            Log.e(TAG, "getPolicyTtAct. Null Pointer Exception : " + e.toString());
            return -1;
        } catch (NumberFormatException e2) {
            Log.e(TAG, "getPolicyTtAct. Null Pointer Exception : " + e2.toString());
            return -1;
        } catch (Exception e3) {
            Log.e(TAG, "getPolicyTtAct. Exception : " + e3.toString());
            return -1;
        }
    }

    public String getPolicyExpirationDate() {
        String expiredDate = null;
        Log.d(TAG, "getPolicyExpirationDate()");
        try {
            if (this.mCurrentPolicyFileExists && mapCurrentPolicy != null) {
                expiredDate = (String) mapCurrentPolicy.get("dateForNextPolicyUpdate");
            }
            if (expiredDate == null) {
                expiredDate = new SimpleDateFormat("yyyyMMddHHmm").format(new Date(System.currentTimeMillis() + ((long) getPolicyRetryPeriod())));
            }
            Log.d(TAG, "getPolicyExpirationDate : expired Date = " + expiredDate);
            return expiredDate;
        } catch (NullPointerException e) {
            Log.e(TAG, "getPolicyExpirationDate : NullPointerException = " + e);
            return null;
        }
    }

    public int getPolicyRetryPeriod() {
        int period = 86400000;
        Log.d(TAG, "getPolicyRetryPeriod");
        try {
            String temp;
            if (mapDefaultPolicy != null) {
                temp = (String) mapDefaultPolicy.get("retryPeriod");
                if (temp != null) {
                    period = ((Integer.parseInt(temp) * 60) * 60) * 1000;
                }
            }
            if (this.mCurrentPolicyFileExists && mapCurrentPolicy != null) {
                temp = (String) mapCurrentPolicy.get("retryPeriod");
                if (temp != null) {
                    Log.d(TAG, "Retry Period (from CurrentPolicy) : " + period + " ms");
                    period = ((Integer.parseInt(temp) * 60) * 60) * 1000;
                }
            }
            Log.d(TAG, "Retry Period : " + period + " ms");
            return period;
        } catch (NullPointerException e) {
            Log.e(TAG, "getPolicyRetryPeriod : NullPointerException. " + e);
            return 86400000;
        } catch (NumberFormatException e2) {
            Log.e(TAG, "getPolicyRetryPeriod : NumberFormatException. " + e2);
            return 86400000;
        }
    }

    public static String getPolicyServerURL(int type) {
        Log.i(TAG, " getPolicyServerURL - type : " + type);
        String serverURL = "";
        if (mapCurrentPolicy != null) {
            if (mapCurrentPolicy.containsKey("primaryServerAddress")) {
                Log.i(TAG, "get primaryServerAddress");
                serverURL = (String) mapCurrentPolicy.get("primaryServerAddress");
            }
            if ("".equals(serverURL) && mapCurrentPolicy.containsKey("secondaryServerAddress")) {
                Log.i(TAG, "get secondaryServerAddress (not used)");
                serverURL = (String) mapCurrentPolicy.get("secondaryServerAddress");
            }
        }
        Log.i(TAG, type + " getPolicyServerURL. serverURL : " + serverURL);
        try {
            if ("".equals(serverURL)) {
                return serverURL;
            }
            if (serverURL.startsWith("http") && serverURL.endsWith("/")) {
                return serverURL;
            }
            Log.i(TAG, type + " getPolicyServerURL. invalid url format (not start" + " with HTTP, not end with / --> delete serverURL : " + serverURL);
            return "";
        } catch (NullPointerException e) {
            Log.e(TAG, "getPolicyServerURL. Null Pointer Exception : " + e);
            return serverURL;
        }
    }

    public static String getCurrentMccMnc(Context mContext) {
        Log.i(TAG, "getCurrentMccMnc : get current registered operator's plmn");
        StringBuilder sb = new StringBuilder();
        String plmn = getCurrentNetworkOperator(mContext);
        if (plmn == null) {
            Log.e(TAG, "getCurrentMccMnc: plmn is null");
            return null;
        }
        Log.i(TAG, "getCurrentMccMnc : TelephonyManager.getNetworkOperator() = " + plmn + " / length(" + plmn.length() + ")");
        if (plmn.length() < 5) {
            return null;
        }
        sb.append("&mcc=");
        sb.append(plmn.substring(0, 3));
        sb.append("&mnc=");
        sb.append(plmn.substring(3));
        Log.i(TAG, "getCurrentMccMnc : " + sb.toString());
        return sb.toString();
    }

    public static String getCurrentNetworkOperator(Context mContext) {
        String plmn = ((TelephonyManager) mContext.getSystemService("phone")).getNetworkOperator();
        return plmn.length() >= 5 ? plmn : null;
    }

    public static String getCurrentPolicyMccMnc() {
        String output = "";
        try {
            output = (String) mapCurrentPolicy.get("updatedPolicyMccMnc");
            Log.i(TAG, "cur pol mccmnc " + output);
            return output;
        } catch (NullPointerException e) {
            Log.e(TAG, "getCurrentPolicyMccMnc : NullPointerException : " + e);
            return "";
        }
    }

    private long getUtc() {
        return System.currentTimeMillis() / 1000;
    }

    public String setPLMN(int mcc, int mnc) {
        StringBuilder sb = new StringBuilder();
        sb.append("&mcc=");
        sb.append(mcc);
        sb.append("&mnc=");
        if (mnc / 10 == 0) {
            sb.append("0");
        }
        sb.append(mnc);
        this.plmn = sb.toString();
        Log.d(TAG, "setPLMN = " + this.plmn);
        return this.plmn;
    }

    public String getPLMN() {
        Log.i(TAG, "getPLMN  : " + this.plmn);
        return this.plmn;
    }

    public String getPLMN(String body) {
        String temp = null;
        if (body != null) {
            temp = body.replace("&mcc=", "").replace("&mnc=", "");
        }
        Log.i(TAG, "getPLMN(" + body + ") : " + temp);
        return temp;
    }
}
