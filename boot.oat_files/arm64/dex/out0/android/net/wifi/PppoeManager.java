package android.net.wifi;

public class PppoeManager {
    private static final int BASE = 458752;
    public static final int BUSY = 2;
    public static final int CMD_START_PPPOE = 458753;
    public static final int CMD_START_PPPOE_FAILED = 458754;
    public static final int CMD_START_PPPOE_SUCCEDED = 458755;
    public static final int CMD_STOP_PPPOE = 458756;
    public static final int CMD_STOP_PPPOE_FAILED = 458757;
    public static final int CMD_STOP_PPPOE_SUCCEDED = 458758;
    public static final int ERROR = 0;
    public static final String EXTRA_PPPOE_DNS1 = "pppoe_dns1";
    public static final String EXTRA_PPPOE_DNS2 = "pppoe_dns2";
    public static final String EXTRA_PPPOE_IP_STATE = "pppoe_ip_state";
    public static final String EXTRA_PPPOE_RESULT_ERROR_CODE = "pppoe_result_error_code";
    public static final String EXTRA_PPPOE_RESULT_STATUS = "pppoe_result_status";
    public static final String EXTRA_PPPOE_STATE = "pppoe_state";
    public static final String PPPOE_COMPLETED_ACTION = "android.net.wifi.PPPOE_COMPLETED_ACTION";
    public static final int PPPOE_CONNECTING_TIMED_OUT = 458759;
    public static final String PPPOE_IP_STATE_CHANGED_ACTION = "android.net.wifi.PPPOE_IP_STATE_CHANGED";
    public static final String PPPOE_RESULT_STATUS_ALREADY_ONLINE = "ALREADY_ONLINE";
    public static final String PPPOE_RESULT_STATUS_FAILURE = "FAILURE";
    public static final String PPPOE_RESULT_STATUS_SUCCESS = "SUCCESS";
    public static final String PPPOE_STATE_CHANGED_ACTION = "android.net.wifi.PPPOE_STATE_CHANGED";
    public static final String PPPOE_STATE_CONNECTED = "PPPOE_STATE_CONNECTED";
    public static final String PPPOE_STATE_CONNECTING = "PPPOE_STATE_CONNECTING";
    public static final String PPPOE_STATE_DISCONNECTED = "PPPOE_STATE_DISCONNECTED";
    public static final String PPPOE_STATE_DISCONNECTING = "PPPOE_STATE_DISCONNECTING";
    public static final int PPPOE_UNSUPPORTED = 1;
    private static final String TAG = "PppoeManager";
}
