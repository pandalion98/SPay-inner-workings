package android.media;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.hardware.display.DisplayManager;
import android.net.ProxyInfo;
import android.os.DVFSHelper;
import android.os.SystemProperties;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public final class RemoteDisplayCallback {
    private static final String TAG = "RemoteDisplayCallback";
    private static AudioManager mAudioManager = null;
    private static Context mContext = null;
    private static DVFSHelper mCpuBooster = null;
    private static boolean mCpuLockEnabled = false;
    private static final int mDefaultFreq = 1000000;
    private static String mDongleVer = null;
    private static boolean mHdcpSuspend = false;
    private static boolean mIsVideoCase = false;
    private static Listener mListener;
    private static String mRemoteIP = null;
    private static boolean mRenameCapablity = false;
    private static boolean mUibcAvailable = false;
    private static String mUibcSinkVer = null;
    private static String mUpdateURL = null;
    private static int mWfdMode = 0;
    private BroadcastReceiver mReceiver = null;
    private int mStreamVol = 0;

    public interface Listener {
        void onHeadSetConnected();

        void onQoSLevelChanged(int i);

        void onTerminateRequested();

        void onTransportChanged(int i);

        void onWeakNetwork();
    }

    private class WfdStateReceiver extends BroadcastReceiver {
        private WfdStateReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DisplayManager.WIFIDISPLAY_RESOLUTION_FROM_APP)) {
                String strSessionControl = intent.getStringExtra("Control");
                if (strSessionControl != null && strSessionControl.equals("terminate") && RemoteDisplayCallback.mListener != null) {
                    RemoteDisplayCallback.mListener.onTerminateRequested();
                }
            } else if (action.equals(DisplayManager.ACTION_LAUNCH_WFD_UPDATE)) {
                Log.d(RemoteDisplayCallback.TAG, "send command: UpdateUserInput >> 1");
                RemoteDisplayCallback.this.sendCmd("upgd", Boolean.valueOf(true));
            } else if (action.equals(DisplayManager.WIFIDISPLAY_UPDATE_INPUT_FROM_APP)) {
                Log.d(RemoteDisplayCallback.TAG, "send command: UpdateUserInput >> 0");
                RemoteDisplayCallback.this.sendCmd("upgd", Boolean.valueOf(false));
            } else if (action.equals(DisplayManager.ACTION_WIFI_DISPLAY_TCP_TRANSPORT)) {
                RemoteDisplayCallback.mWfdMode = 1;
                RemoteDisplayCallback.this.sendCmd("tcp", Boolean.valueOf(true));
            } else if (action.equals(DisplayManager.ACTION_WIFI_DISPLAY_UDP_TRANSPORT)) {
                RemoteDisplayCallback.mWfdMode = 0;
                RemoteDisplayCallback.this.sendCmd("udp", Boolean.valueOf(true));
            } else if (action.equals("android.media.VOLUME_CHANGED_ACTION")) {
                int audio_type = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", 0);
                int currVolume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);
                if (audio_type != 3) {
                    return;
                }
                if (RemoteDisplayCallback.this.mStreamVol == currVolume) {
                    Log.d(RemoteDisplayCallback.TAG, "same volume! skip to send command!");
                } else if (RemoteDisplayCallback.mWfdMode == 1) {
                    RemoteDisplayCallback.this.mStreamVol = currVolume;
                    Log.d(RemoteDisplayCallback.TAG, "send command: curr stream vol:" + RemoteDisplayCallback.this.mStreamVol);
                    RemoteDisplayCallback.this.sendCmd("vol", Integer.valueOf(RemoteDisplayCallback.this.mStreamVol));
                }
            } else if (action.equals("android.intent.action.HEADSET_PLUG")) {
                if (intent.hasExtra("state") && intent.getIntExtra("state", 0) != 0 && intent.getIntExtra("state", 0) == 1 && RemoteDisplayCallback.mWfdMode == 1 && RemoteDisplayCallback.mIsVideoCase && RemoteDisplayCallback.mListener != null) {
                    RemoteDisplayCallback.mListener.onHeadSetConnected();
                }
            } else if (action.equals(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, 2);
                Log.d(RemoteDisplayCallback.TAG, "action << WIFIDISPLAY_BLUETOOTH_HEADSET_ACTION, state : " + state + "MODE : " + RemoteDisplayCallback.mWfdMode);
                if (state == 2) {
                    try {
                        Log.d(RemoteDisplayCallback.TAG, "Thead on");
                        Thread.sleep(2000);
                        Log.d(RemoteDisplayCallback.TAG, "mAudioManager.isBluetoothA2dpOn() : " + RemoteDisplayCallback.mAudioManager.isBluetoothA2dpOn());
                        if (RemoteDisplayCallback.mAudioManager.isBluetoothA2dpOn() && RemoteDisplayCallback.mWfdMode == 1 && RemoteDisplayCallback.mIsVideoCase) {
                            Log.d(RemoteDisplayCallback.TAG, "isBluetoothA2dpOn()");
                            if (RemoteDisplayCallback.mListener != null) {
                                RemoteDisplayCallback.mListener.onHeadSetConnected();
                            }
                        }
                    } catch (InterruptedException e) {
                        Log.d(RemoteDisplayCallback.TAG, "Thread exception!!");
                    }
                }
            } else if (action.equals("android.intent.action.MAR_SLAVE_SMB")) {
                int maxBitrate = intent.getIntExtra("maxBitrate", 0);
                if (maxBitrate > 0) {
                    Log.d(RemoteDisplayCallback.TAG, "action << android.intent.action.MAR_SLAVE_SMB : " + maxBitrate);
                    RemoteDisplayCallback.this.sendCmd("smb", Integer.valueOf(maxBitrate));
                }
            } else if (action.equals(AudioManager.STREAM_DEVICES_CHANGED_ACTION)) {
                int stream = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
                int devices = intent.getIntExtra(AudioManager.EXTRA_VOLUME_STREAM_DEVICES, -1);
                Log.d(RemoteDisplayCallback.TAG, stream + " stream use " + devices + " : " + ((String) intent.getExtra(AudioManager.EXTRA_VOLUME_STREAM_MUSIC_ADDRESS, ProxyInfo.LOCAL_EXCL_LIST)));
                if (stream == 3 && devices != 32768 && RemoteDisplayCallback.mWfdMode == 1 && RemoteDisplayCallback.mIsVideoCase && RemoteDisplayCallback.mListener != null) {
                    RemoteDisplayCallback.mListener.onHeadSetConnected();
                }
            }
        }
    }

    public static void setContext(Context context, Listener listener) {
        if (mContext == null) {
            mContext = context;
        }
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        }
        if (mListener == null) {
            mListener = listener;
        }
        mRemoteIP = null;
        mUpdateURL = null;
        mDongleVer = null;
        mWfdMode = 0;
        mHdcpSuspend = false;
        Log.d(TAG, "mContext:" + context + " mAudioManager:" + mAudioManager + " mListener:" + mListener);
    }

    public RemoteDisplayCallback() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DisplayManager.WIFIDISPLAY_RESOLUTION_FROM_APP);
        filter.addAction(DisplayManager.ACTION_LAUNCH_WFD_UPDATE);
        filter.addAction(DisplayManager.WIFIDISPLAY_UPDATE_INPUT_FROM_APP);
        filter.addAction(DisplayManager.ACTION_WIFI_DISPLAY_TCP_TRANSPORT);
        filter.addAction(DisplayManager.ACTION_WIFI_DISPLAY_UDP_TRANSPORT);
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        filter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction("android.intent.action.HEADSET_PLUG");
        filter.addAction("android.intent.action.MAR_SLAVE_SMB");
        filter.addAction(AudioManager.STREAM_DEVICES_CHANGED_ACTION);
        this.mReceiver = new WfdStateReceiver();
        mContext.registerReceiver(this.mReceiver, filter);
    }

    private void lockCPUFreq(int minLockFreq) {
        if (mContext != null) {
            mCpuBooster = new DVFSHelper(mContext, 12);
            if (mCpuBooster != null) {
                int[] supportedCPUFreqTable = mCpuBooster.getSupportedCPUFrequency();
                int minIdx = 0;
                if (supportedCPUFreqTable != null) {
                    int min = Math.abs(minLockFreq - supportedCPUFreqTable[0]);
                    for (int i = 1; i < supportedCPUFreqTable.length; i++) {
                        if (min > Math.abs(minLockFreq - supportedCPUFreqTable[i])) {
                            min = Math.abs(minLockFreq - supportedCPUFreqTable[i]);
                            minIdx = i;
                        }
                    }
                    mCpuBooster.addExtraOption("CPU", supportedCPUFreqTable[minIdx] + ProxyInfo.LOCAL_EXCL_LIST);
                    try {
                        mCpuBooster.acquire();
                    } catch (Exception e) {
                        Log.e(TAG, "cpuBooster.acquire is failed");
                        e.printStackTrace();
                    }
                    Log.d(TAG, "WFD lock DVFS_MIN_LIMIT :" + supportedCPUFreqTable[minIdx]);
                    mCpuLockEnabled = true;
                }
            }
        }
    }

    private void unlockCPUFreq() {
        if (mCpuLockEnabled && mContext != null) {
            try {
                mCpuBooster.release();
            } catch (Exception e) {
                Log.e(TAG, "mDVFSLock.release is failed");
                e.printStackTrace();
            }
            mCpuBooster = null;
            Log.d(TAG, "Wfd release DVFS_MIN_LIMIT");
            mCpuLockEnabled = false;
        }
    }

    private void sendCmd(String key, Object data) {
        JSONObject mParam = new JSONObject();
        try {
            mParam.put(key, data);
        } catch (JSONException e) {
            Log.w(TAG, e.toString());
        }
        RemoteDisplay.setParam(0, mParam.toString());
        mParam.remove(key);
    }

    public boolean isDongleRenameAvailable() {
        return mRenameCapablity;
    }

    public boolean setDeviceName(String deviceName) {
        Log.d(TAG, "setDeviceName : " + deviceName);
        sendCmd("res", deviceName);
        return true;
    }

    private void broadcastDongleVerToFota(String Ver) {
        Log.d(TAG, "broadcastDongleVerToFota << Ver: " + Ver);
        Intent intent = new Intent("com.samsung.wfd.START_WFD");
        if (Ver != null) {
            intent.putExtra("version", Ver);
        }
        intent.addFlags(32);
        mContext.sendBroadcast(intent);
    }

    private void broadcastDongleUpdateUrl(String Url) {
        Log.d(TAG, "broadcastDongleUpdateUrl << Url: " + Url);
        Intent intent = new Intent(DisplayManager.WIFIDISPLAY_UPDATE_URL_FROM_NATIVE);
        intent.addFlags(67108864);
        intent.putExtra("URL", Url);
        mContext.sendBroadcast(intent);
    }

    private void broadcastWfdConnectionType(int mode) {
        Log.d(TAG, "broadcastWfdConnectionType mode : " + mode);
        Intent intent = new Intent(DisplayManager.WIFIDISPLAY_NOTI_CONNECTION_MODE);
        intent.addFlags(67108864);
        intent.putExtra("CONNECTION_MODE", mode);
        mContext.sendBroadcast(intent);
    }

    private void StartHDCPSuspend() {
        Log.d(TAG, "StartHDCPSuspend");
        sendCmd("sus", (String) Resources.getSystem().getText(17041649));
    }

    public void startStopUIBCVirtualSoftkey(boolean isStart) {
        Log.d(TAG, "startStopUIBCVirtualSoftkey() isStart=" + isStart);
        try {
            Intent intent = new Intent("com.samsung.action.UIBCVIRTUALSOFTKEY_SERVICE").setComponent(new ComponentName("com.sec.android.uibcvirtualsoftkey", "com.sec.android.uibcvirtualsoftkey.UIBCVirtualSoftkeyService"));
            intent.putExtra("Start", isStart);
            mContext.startService(intent);
        } catch (Exception e) {
            Log.e(TAG, "Exception showHideUIBCVirtualSoftkey() : ", e);
        }
    }

    public void onNoti(int msg, String data) {
        Log.d(TAG, "onNoti << msg:" + msg + " data:" + data);
        switch (msg) {
            case 1:
                Log.d(TAG, "data:" + data);
                try {
                    JSONObject mNoti = new JSONObject(data);
                    mRemoteIP = mNoti.getString("remoteIP");
                    mDongleVer = mNoti.getString("sink_ver");
                    if (mDongleVer.equals("AA00")) {
                        mDongleVer = null;
                    }
                    SystemProperties.set("wlan.wfd.dongle", mDongleVer);
                    mRenameCapablity = mNoti.getBoolean("renameAvailable");
                } catch (JSONException e) {
                    Log.w(TAG, e.toString());
                }
                broadcastDongleVerToFota(mDongleVer);
                Log.d(TAG, "WFD client connected broadcast sent");
                return;
            case 2:
                Log.d(TAG, "data:" + data);
                mContext.sendBroadcast(new Intent("com.sec.android.smartview.WFD_ENGINE_RESUME"));
                return;
            case 3:
                Log.d(TAG, "data:" + data);
                mContext.sendBroadcast(new Intent("com.sec.android.smartview.WFD_ENGINE_PAUSE"));
                return;
            case 7:
                if (mContext != null) {
                    int prevWfdMode = mWfdMode;
                    mWfdMode = Integer.parseInt(data);
                    if (prevWfdMode == 0 && mWfdMode == 1) {
                        mIsVideoCase = true;
                    } else {
                        mIsVideoCase = false;
                    }
                    Log.d(TAG, "onNoti received : WFD_NOTI_TO_APP_TRANSPORT_MODE, prevWfdMode = " + prevWfdMode + ", mWfdMode = " + mWfdMode + ", mIsVideoCase = " + mIsVideoCase);
                    if (mWfdMode == 1) {
                        String path = mAudioManager.getParameters(AudioManager.OUTDEVICE);
                        boolean isRSubmix = false;
                        if (!(path == null || ProxyInfo.LOCAL_EXCL_LIST.equals(path))) {
                            if ((Integer.valueOf(path).intValue() & 32768) == 0) {
                                isRSubmix = false;
                            } else {
                                isRSubmix = true;
                            }
                            Log.i(TAG, "isRSubmix :" + isRSubmix);
                        }
                        if (!(isRSubmix || !mIsVideoCase || mListener == null)) {
                            mListener.onHeadSetConnected();
                        }
                        if (mAudioManager != null) {
                            this.mStreamVol = mAudioManager.getStreamVolume(3);
                        }
                        Log.d(TAG, "get native STREAM_MUSIC volume :" + this.mStreamVol);
                        Log.d(TAG, "send command: curr stream vol @ tcp start! << " + this.mStreamVol);
                        sendCmd("vol", Integer.valueOf(this.mStreamVol));
                    }
                    broadcastWfdConnectionType(mWfdMode);
                    if (mListener != null) {
                        mListener.onTransportChanged(mWfdMode);
                        return;
                    }
                    return;
                }
                return;
            case 9:
                Log.d(TAG, "Noti2App_UIBC Data: ");
                try {
                    mUibcAvailable = new JSONObject(data).getBoolean("UibcAvailable");
                    Log.d(TAG, "Calling starStopUIBCVirtualSoftkey");
                    startStopUIBCVirtualSoftkey(mUibcAvailable);
                } catch (JSONException e2) {
                    Log.w(TAG, e2.toString());
                }
                Log.d(TAG, "BroadCast UIBC Data");
                return;
            case 20:
                if (mContext != null) {
                    try {
                        mUpdateURL = new JSONObject(data).getString("SinkFwUrl");
                    } catch (JSONException e22) {
                        Log.w(TAG, e22.toString());
                    }
                    broadcastDongleUpdateUrl(mUpdateURL);
                    return;
                }
                return;
            case 30:
                if (mContext != null) {
                    Log.d(TAG, "WFD noti to App - weak network connection..");
                    if (mListener != null) {
                        mListener.onWeakNetwork();
                        return;
                    }
                    return;
                }
                return;
            case 40:
                if (mContext != null) {
                    int level = 0;
                    try {
                        level = new JSONObject(data).getInt("level");
                    } catch (JSONException e222) {
                        Log.w(TAG, e222.toString());
                    }
                    Log.d(TAG, "WFD noti to App - update QoS level : " + level);
                    if (mListener != null) {
                        mListener.onQoSLevelChanged(level);
                        return;
                    }
                    return;
                }
                return;
            case 50:
                if (mContext != null) {
                    Log.d(TAG, "WFD noti to App - WFD_NOTI_TO_APP_HEADSET_CONNECTED");
                    if (mListener != null) {
                        mListener.onHeadSetConnected();
                        return;
                    }
                    return;
                }
                return;
            case 100:
            case 1001:
                return;
            default:
                Log.e(TAG, "check!! << msg:" + msg);
                return;
        }
    }
}
