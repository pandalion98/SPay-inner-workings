package com.samsung.cpp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class CPPSrvCommunicator {
    public static final int CELLDB_REQUEST_MAX_COUNT = 3;
    public static final int COMM_TYPE_GET_POLICY = 90000;
    public static final int COMM_TYPE_POST_REQ_CELLDB = 20000;
    public static final int COMM_TYPE_POST_REQ_WIFI_LOC = 30000;
    public static final int FAIL_IO_EXCEPTION = 5;
    public static final int FAIL_NO_SEVER_URL = 6;
    public static final String HTTP_RESPONSE_CODE = "http_response_code";
    public static final String HTTP_RESPONSE_MSG = "http_response_msg";
    public static final String MODE = "mode";
    public static final int POLICYID_UPDATE_REQUIRED = 4;
    public static final String REQ_BODY = "body";
    public static final String REQ_ID = "req_id";
    public static final String RESULT_CODE = "result_code";
    public static final String RESULT_MSG = "result_msg";
    public static final String RESULT_SIZE = "size";
    public static final int SUCCESS = 0;
    private static final String TAG = "CPPSrvCommunicator";
    private Context mContext;
    private Handler mHandler;

    private String getUrlInfo(int type, String data) {
        String fullUrl = CPPPolicyHandler.getPolicyServerURL(type);
        if (data != null && "".equals(fullUrl) && type == COMM_TYPE_GET_POLICY) {
            if (data.startsWith("&mcc=460")) {
                fullUrl = CPPPolicyHandler.DEFAULT_CHN_SERVER_URL;
                Log.d(TAG, "getUrlInfo() chn default");
            } else {
                fullUrl = "https://prod-celltw.secb2b.com/";
                Log.d(TAG, "getUrlInfo() : default");
            }
        }
        Log.d(TAG, "Final URL : " + fullUrl);
        return fullUrl;
    }

    private void executeRequest(String body, String url, int type, long req_id, int rat, int mode, int retryCount) {
        final String str = url;
        final int i = type;
        final long j = req_id;
        final int i2 = rat;
        final int i3 = retryCount;
        final String str2 = body;
        final int i4 = mode;
        new Thread(new Runnable() {
            public void run() {
                IOException e;
                Throwable th;
                Message msg = Message.obtain();
                Bundle result = new Bundle();
                HttpURLConnection conn = null;
                DataOutputStream out = null;
                boolean needCellDBRetry = false;
                Log.d(CPPSrvCommunicator.TAG, "executeRequest() url : " + str + ", type : " + i + ", req_id : " + j + ", rat : " + i2 + ", retryCount : " + i3);
                Log.d(CPPSrvCommunicator.TAG, "executeRequest() body : " + str2);
                try {
                    String newUrl = "";
                    if (i == CPPSrvCommunicator.COMM_TYPE_GET_POLICY) {
                        newUrl = i2 == 32 ? str + "api/v1/policy/wifi?_method=GET" + str2 : str + "api/v1/policy?_method=GET" + str2;
                    } else if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB) {
                        String ratName;
                        switch (i2) {
                            case 1:
                                ratName = "lte";
                                break;
                            case 2:
                                ratName = "wcdma";
                                break;
                            case 4:
                                ratName = "gsm";
                                break;
                            case 8:
                                ratName = "cdma1x";
                                break;
                            case 16:
                                ratName = "tdscdma";
                                break;
                            case 32:
                                ratName = "wifi";
                                break;
                            default:
                                ratName = "lte";
                                break;
                        }
                        newUrl = str + "api/v9/cells/cust/" + ratName + "?_method=POST";
                    } else if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_WIFI_LOC) {
                    }
                    Log.d(CPPSrvCommunicator.TAG, "New url : " + newUrl);
                    if (new URL(str).getProtocol().toLowerCase().equals("https")) {
                        HttpURLConnection conn1 = (HttpsURLConnection) new URL(newUrl).openConnection();
                        conn1.setHostnameVerifier(new HostnameVerifier() {
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }
                        });
                        try {
                            SSLContext context = SSLContext.getInstance(SSLSocketFactory.TLS);
                            context.init(null, CPPPinning.getInstance(CPPSrvCommunicator.this.mContext).getTrustManagers(newUrl), null);
                            conn1.setSSLSocketFactory(context.getSocketFactory());
                        } catch (NoSuchAlgorithmException e2) {
                            e2.printStackTrace();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                        conn = conn1;
                    } else {
                        conn = (HttpURLConnection) new URL(newUrl).openConnection();
                    }
                    conn.setConnectTimeout(6000);
                    if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB) {
                        Log.d(CPPSrvCommunicator.TAG, "executeRequest() POST TYPE");
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setRequestProperty("Content-Type", "application/json");
                    } else if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_WIFI_LOC) {
                        Log.d(CPPSrvCommunicator.TAG, "executeRequest() GET TYPE");
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/json");
                    } else {
                        Log.d(CPPSrvCommunicator.TAG, "executeRequest() GET TYPE");
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/octet-stream");
                    }
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.setUseCaches(false);
                    conn.connect();
                    if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB || i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_WIFI_LOC) {
                        DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
                        try {
                            byte[] message = str2.getBytes();
                            Log.d(CPPSrvCommunicator.TAG, "executeRequest() send message : " + Arrays.toString(message));
                            dataOutputStream.write(message);
                            Log.d(CPPSrvCommunicator.TAG, "executeRequest() sizeOfSendData : " + dataOutputStream.size());
                            dataOutputStream.flush();
                            out = dataOutputStream;
                        } catch (IOException e4) {
                            e3 = e4;
                            out = dataOutputStream;
                            try {
                                Log.d(CPPSrvCommunicator.TAG, "executeRequest() executeRequest.Error: " + e3);
                                result.putInt(CPPSrvCommunicator.HTTP_RESPONSE_CODE, 5);
                                result.putString(CPPSrvCommunicator.HTTP_RESPONSE_MSG, "IOException");
                                if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB) {
                                    needCellDBRetry = true;
                                    Log.d(CPPSrvCommunicator.TAG, "executeRequest() CellDBRequest Failure(IOException) --> retry flag set");
                                }
                                if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB || !needCellDBRetry || i3 >= 3) {
                                    Log.d(CPPSrvCommunicator.TAG, "executeRequest() Send result to CPPProvider ! ");
                                    msg.what = 6;
                                    msg.arg1 = i;
                                    msg.arg2 = i2;
                                    if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB) {
                                        result.putLong("req_id", j);
                                    }
                                    msg.setData(result);
                                    CPPSrvCommunicator.this.mHandler.sendMessage(msg);
                                } else {
                                    Log.d(CPPSrvCommunicator.TAG, "executeRequest() CellDBRequest Retry --> call executeRequest / req_id(" + j + "), json(" + str2 + ")");
                                    CPPSrvCommunicator.this.executeRequest(str2, str, i, j, i2, i4, i3 + 1);
                                }
                                if (conn != null) {
                                    conn.disconnect();
                                }
                                if (out != null) {
                                    try {
                                        out.close();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                        return;
                                    }
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB) {
                                }
                                Log.d(CPPSrvCommunicator.TAG, "executeRequest() Send result to CPPProvider ! ");
                                msg.what = 6;
                                msg.arg1 = i;
                                msg.arg2 = i2;
                                if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB) {
                                    result.putLong("req_id", j);
                                }
                                msg.setData(result);
                                CPPSrvCommunicator.this.mHandler.sendMessage(msg);
                                if (conn != null) {
                                    conn.disconnect();
                                }
                                if (out != null) {
                                    try {
                                        out.close();
                                    } catch (IOException ex2) {
                                        ex2.printStackTrace();
                                    }
                                }
                                throw th;
                            }
                        } catch (Throwable th22) {
                            th = th22;
                            out = dataOutputStream;
                            if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB || null == null || i3 >= 3) {
                                Log.d(CPPSrvCommunicator.TAG, "executeRequest() Send result to CPPProvider ! ");
                                msg.what = 6;
                                msg.arg1 = i;
                                msg.arg2 = i2;
                                if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB) {
                                    result.putLong("req_id", j);
                                }
                                msg.setData(result);
                                CPPSrvCommunicator.this.mHandler.sendMessage(msg);
                            } else {
                                Log.d(CPPSrvCommunicator.TAG, "executeRequest() CellDBRequest Retry --> call executeRequest / req_id(" + j + "), json(" + str2 + ")");
                                CPPSrvCommunicator.this.executeRequest(str2, str, i, j, i2, i4, i3 + 1);
                            }
                            if (conn != null) {
                                conn.disconnect();
                            }
                            if (out != null) {
                                out.close();
                            }
                            throw th;
                        }
                    }
                    int httpResponseCode = conn.getResponseCode();
                    Log.d(CPPSrvCommunicator.TAG, "executeRequest() Http Response Code : " + httpResponseCode);
                    String httpResponseMsg = conn.getResponseMessage();
                    Log.d(CPPSrvCommunicator.TAG, "executeRequest() Http Response Msg : " + httpResponseMsg);
                    result.putInt(CPPSrvCommunicator.HTTP_RESPONSE_CODE, httpResponseCode);
                    result.putString(CPPSrvCommunicator.HTTP_RESPONSE_MSG, httpResponseMsg);
                    if (httpResponseCode == 200) {
                        InputStream is = conn.getInputStream();
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        byte[] byteBuffer = new byte[1024];
                        while (true) {
                            int nLength = is.read(byteBuffer);
                            if (nLength > 0) {
                                os.write(byteBuffer, 0, nLength);
                            } else {
                                is.close();
                                byte[] byteData = os.toByteArray();
                                int code = (Arrays.copyOfRange(byteData, 14, 15)[0] & 255) - 48;
                                result.putInt(CPPSrvCommunicator.RESULT_CODE, code);
                                if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB) {
                                    if (code == 0) {
                                        Log.d(CPPSrvCommunicator.TAG, "executeRequest() Result Code : Success");
                                        byte[] parsedByte = Arrays.copyOfRange(byteData, 62, byteData.length - 1);
                                        result.putByteArray(CPPSrvCommunicator.RESULT_MSG, parsedByte);
                                        result.putInt(CPPSrvCommunicator.RESULT_SIZE, parsedByte.length);
                                    } else if (code == 4) {
                                        Log.d(CPPSrvCommunicator.TAG, "executeRequest() Result Code : Policy Update is Required");
                                        result.putByteArray(CPPSrvCommunicator.RESULT_MSG, byteData);
                                        result.putInt(CPPSrvCommunicator.RESULT_SIZE, byteData.length);
                                    } else {
                                        Log.d(CPPSrvCommunicator.TAG, "executeRequest() CellDBRequest Failure(HTTP_OK && NOT SUCCESS)");
                                        result.putByteArray(CPPSrvCommunicator.RESULT_MSG, byteData);
                                        result.putInt(CPPSrvCommunicator.RESULT_SIZE, byteData.length);
                                        needCellDBRetry = false;
                                    }
                                    Log.d(CPPSrvCommunicator.TAG, "executeRequest() Type : " + i + ", RESULT_CODE : " + code + ", RESULT_SIZE : " + os.size() + ", RESULT_REQ_ID : " + j + ", RESULT_MSG (byteData) : " + Arrays.toString(byteData));
                                } else if (i == CPPSrvCommunicator.COMM_TYPE_GET_POLICY) {
                                    String str = new String(byteData);
                                    result.putString(CPPSrvCommunicator.RESULT_MSG, str);
                                    result.putString(CPPSrvCommunicator.REQ_BODY, str2);
                                    Log.d(CPPSrvCommunicator.TAG, "executeRequest() Type : " + i + ", RESULT_CODE : " + code + ", PLMN : " + str2 + ", RESULT_MSG (response) : " + str);
                                } else if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_WIFI_LOC) {
                                    result.putLong("req_id", j);
                                    result.putInt(CPPSrvCommunicator.MODE, i4);
                                    result.putByteArray(CPPSrvCommunicator.RESULT_MSG, byteData);
                                }
                            }
                        }
                    } else {
                        Log.d(CPPSrvCommunicator.TAG, "executeRequest() Http Response : Fail !! ");
                        if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB) {
                            needCellDBRetry = true;
                            Log.d(CPPSrvCommunicator.TAG, "executeRequest() CellDBRequest Failure(HTTP_FAIL) --> retry flag set");
                        }
                    }
                    if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB && needCellDBRetry && i3 < 3) {
                        Log.d(CPPSrvCommunicator.TAG, "executeRequest() CellDBRequest Retry --> call executeRequest / req_id(" + j + "), json(" + str2 + ")");
                        CPPSrvCommunicator.this.executeRequest(str2, str, i, j, i2, i4, i3 + 1);
                    } else {
                        Log.d(CPPSrvCommunicator.TAG, "executeRequest() Send result to CPPProvider ! ");
                        msg.what = 6;
                        msg.arg1 = i;
                        msg.arg2 = i2;
                        if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB) {
                            result.putLong("req_id", j);
                        }
                        msg.setData(result);
                        CPPSrvCommunicator.this.mHandler.sendMessage(msg);
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex22) {
                            ex22.printStackTrace();
                        }
                    }
                } catch (IOException e5) {
                    e3 = e5;
                    Log.d(CPPSrvCommunicator.TAG, "executeRequest() executeRequest.Error: " + e3);
                    result.putInt(CPPSrvCommunicator.HTTP_RESPONSE_CODE, 5);
                    result.putString(CPPSrvCommunicator.HTTP_RESPONSE_MSG, "IOException");
                    if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB) {
                        needCellDBRetry = true;
                        Log.d(CPPSrvCommunicator.TAG, "executeRequest() CellDBRequest Failure(IOException) --> retry flag set");
                    }
                    if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB) {
                    }
                    Log.d(CPPSrvCommunicator.TAG, "executeRequest() Send result to CPPProvider ! ");
                    msg.what = 6;
                    msg.arg1 = i;
                    msg.arg2 = i2;
                    if (i == CPPSrvCommunicator.COMM_TYPE_POST_REQ_CELLDB) {
                        result.putLong("req_id", j);
                    }
                    msg.setData(result);
                    CPPSrvCommunicator.this.mHandler.sendMessage(msg);
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (out != null) {
                        out.close();
                    }
                }
            }
        }).start();
    }

    public CPPSrvCommunicator(Context context, Handler handler) {
        this.mHandler = handler;
        this.mContext = context;
    }

    public void sendToSrv(int type, String data, long req_id, int rat, int mode) {
        Log.d(TAG, "sendToSrv() type : " + type + ", req_id : " + req_id + ", mode : " + mode + ", rat : " + rat);
        String url = getUrlInfo(type, data);
        if ("".equals(url)) {
            Log.d(TAG, "sendToSrv() NO URL");
            Message msg = Message.obtain();
            Bundle result = new Bundle();
            result.putInt(RESULT_CODE, 6);
            result.putString(RESULT_MSG, "NO URL");
            result.putLong("req_id", req_id);
            msg.what = 6;
            msg.arg1 = type;
            msg.arg2 = rat;
            msg.setData(result);
            this.mHandler.sendMessage(msg);
            return;
        }
        executeRequest(data, url, type, req_id, rat, mode, 0);
    }

    public void sendToSrvForLoc(int clientId, byte geoMode, String data) {
        String url = "https://prod-celltw.secb2b.com/";
        if ("".equals(url)) {
            Log.d(TAG, "sendToSrvForLoc() NO URL");
            Message msg = Message.obtain();
            Bundle result = new Bundle();
            result.putInt(RESULT_CODE, 6);
            result.putString(RESULT_MSG, "NO URL");
            msg.what = 6;
            msg.setData(result);
            this.mHandler.sendMessage(msg);
            return;
        }
        executeRequest(data, url, COMM_TYPE_POST_REQ_WIFI_LOC, (long) clientId, 0, geoMode, 0);
    }
}
