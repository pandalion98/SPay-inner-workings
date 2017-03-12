package com.android.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.Log;
import android.view.WindowManagerPolicy;
import com.android.server.SecExternalDisplayOrientation.EDS_CameraStatus;
import com.android.server.SecExternalDisplayService.EDSFlagStatus;
import java.util.HashMap;

public class SecExternalDisplayIntents extends SecExternalDisplayOrientation {
    private static final String ALLSHARE_CAST_EXTRA_STATE = "state";
    private static final String ALLSHARE_CAST_GETSTATE = "android.intent.action.WIFI_DISPLAY";
    public static final String ANDROID_HDMI_SET_FORCE_MIRROR_MODE = "samsung.intent.action.ANDROID_HDMI_SET_FORCE_MIRROR_MODE";
    public static final String ANDROID_HDMI_START_VFB = "samsung.intent.action.ANDROID_HDMI_START_VFB";
    public static final String ANDROID_HDMI_STOP_VFB = "samsung.intent.action.ANDROID_HDMI_STOP_VFB";
    private static final String CameraFrontStart = "com.sec.android.app.camera.ACTION_START_FRONT_CAMERA";
    private static final String CameraPosition = "CameraPosition";
    private static final String CameraRearStart = "com.sec.android.app.camera.ACTION_START_BACK_CAMERA";
    private static final String CameraStop = "com.sec.android.app.camera.ACTION_STOP_CAMERA";
    public static final String EXTRA_IS_FORCE_MIRROR = "isForceMirror";
    private static final String KDDI_auSharelink_SOURCE_CONNECT = "com.kddi.android.sptab_source.SUCCESS_CONNECT_SOURCE";
    private static final String KDDI_auSharelink_SOURCE_DISCONNECT = "com.kddi.android.sptab_source.SUCCESS_DISCONNECT_SOURCE";
    private static final boolean LOG = true;
    private static final String PresentationStart = "com.samsung.intent.action.SEC_PRESENTATION_START";
    private static final String PresentationStateInfo = "android.app.presentation.ACTION_START_PRESENTATION_STATE";
    private static final String PresentationStop = "com.samsung.intent.action.SEC_PRESENTATION_STOP";
    private static final String RemoteViewFinder = "com.samsung.android.app.camera.RVF";
    private static final String SCREENRECORDER_NOTIFY_EVENT = "android.intent.action.SCREENRECORDER_INFORMATION";
    private static final String SCREENRECORDER_STOP_EVENT = "android.intent.action.SCREENRECORDER_HDMI";
    private static final String SideSyncConnected = "com.sec.android.sidesync.source.SIDESYNC_CONNECTED";
    private static final String SideSyncDestroyed = "com.sec.android.sidesync.source.SERVICE_DESTROY";
    private static final String TAG = "SecExternalDisplayIntents_Java";
    private static final String VIDEO_PRESENTATION_START_MODE = "com.sec.android.app.videoplayer.REMOVE_BLACK_SCREEN";
    private static final String VIDEO_PRESENTATION_STOP_MODE = "com.sec.android.app.videoplayer.SHOW_BLACK_SCREEN";
    public final String[] ALARM_STARTED = new String[]{"com.samsung.sec.android.clockpackage.alarm.ALARM_STARTED_IN_ALERT", "com.android.deskclock.ALARM_ALERT", "com.samsung.sec.android.clockpackage.alarm.ALARM_ALERT"};
    public final String[] ALARM_STOPPED = new String[]{"com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT", "com.android.deskclock.ALARM_DONE", "com.samsung.sec.android.clockpackage.alarm.ALARM_STOP"};
    private HashMap<String, Integer> BroadcastMap;
    private final BroadcastReceiver mReceiver2 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            try {
                int IntentInfo = ((Integer) SecExternalDisplayIntents.this.BroadcastMap.get(action)).intValue();
                Log.i(SecExternalDisplayIntents.TAG, "Intent Recieved ..  -" + action + "BroadCast Map value - " + IntentInfo);
                if (EDSRecievedIntent.toStatus(IntentInfo) != null) {
                    switch (AnonymousClass2.$SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.toStatus(IntentInfo).ordinal()]) {
                        case 1:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered HDMI Case");
                            }
                            SecExternalDisplayIntents.this.mEDSServiceObj.handleHDMIConnection(intent.getBooleanExtra("state", false));
                            return;
                        case 2:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered rear camera Case");
                            }
                            SecExternalDisplayIntents.this.handleCameraConnection(EDS_CameraStatus.REAR.ordinal());
                            return;
                        case 3:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered Front Case");
                            }
                            SecExternalDisplayIntents.this.handleCameraConnection(EDS_CameraStatus.FRONT.ordinal());
                            return;
                        case 4:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered Camera Stop Case");
                            }
                            SecExternalDisplayIntents.this.handleCameraConnection(EDS_CameraStatus.STOP.ordinal());
                            return;
                        case 5:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered RVF Case");
                            }
                            SecExternalDisplayIntents.this.handleRVFConnection(intent.getBooleanExtra("running", false));
                            return;
                        case 6:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered WFD Case");
                            }
                            SecExternalDisplayIntents.this.mEDSServiceObj.handleWFDConnection(intent.getIntExtra("state", 0));
                            return;
                        case 7:
                        case 8:
                        case 9:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered Camera Start Case");
                            }
                            SecExternalDisplayIntents.this.mEDSServiceObj.handleAlarm(true);
                            return;
                        case 10:
                        case 11:
                        case 12:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered Camera Stop Case");
                            }
                            SecExternalDisplayIntents.this.mEDSServiceObj.handleAlarm(false);
                            return;
                        case 13:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered SideSync Connected Case");
                            }
                            SecExternalDisplayIntents.this.handleSideSyncConnection(true);
                            return;
                        case 14:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered SideSync DC Case");
                            }
                            SecExternalDisplayIntents.this.handleSideSyncConnection(false);
                            return;
                        case 15:
                        case 16:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered ScreenRecorder Case");
                            }
                            String SRState = intent.getStringExtra("IsRunning");
                            Log.i(SecExternalDisplayIntents.TAG, "ScreenRecorder State is: " + SRState);
                            if ("run".equals(SRState)) {
                                SecExternalDisplayIntents.this.mEDSServiceObj.SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SCREENRECORDING.ordinal(), true);
                                return;
                            } else if ("stop".equals(SRState)) {
                                SecExternalDisplayIntents.this.mEDSServiceObj.SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SCREENRECORDING.ordinal(), false);
                                return;
                            } else {
                                return;
                            }
                        case 17:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered SmartDock Case");
                            }
                            int bMouseDockedFlag = intent.getIntExtra("android.intent.extra.device_state", 0);
                            boolean bHMTConnection = SystemProperties.getBoolean("sys.hmt.connected", false);
                            Log.d(SecExternalDisplayIntents.TAG, "Smart Dock Event Received !!!!!  Dock Status :  " + bMouseDockedFlag + " HMT Status :" + bHMTConnection);
                            if (!bHMTConnection) {
                                SecExternalDisplayIntents.this.mEDSServiceObj.handleSmartDockConnection(bMouseDockedFlag);
                                return;
                            }
                            return;
                        case 18:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered ScreenLock Case");
                                return;
                            }
                            return;
                        case 19:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered Screen Unlocked Case");
                                return;
                            }
                            return;
                        case 20:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "Entered Video Start Case");
                            }
                            SecExternalDisplayIntents.this.mEDSServiceObj.handleVideo(true);
                            return;
                        case 21:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "Entered Video Stop Case");
                            }
                            SecExternalDisplayIntents.this.mEDSServiceObj.handleVideo(false);
                            return;
                        case 22:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered Presentation Start Case");
                            }
                            SecExternalDisplayIntents.this.mEDSServiceObj.handlePresentation(true);
                            return;
                        case 23:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "entered Presentation Stop Case");
                            }
                            SecExternalDisplayIntents.this.mEDSServiceObj.handlePresentation(false);
                            return;
                        case 24:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "Entered Kddi auShareLink connect case");
                            }
                            SecExternalDisplayIntents.this.handleKddiAuShareLink(true);
                            return;
                        case 25:
                            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                                Log.i(SecExternalDisplayIntents.TAG, "Entered Kddi auShareLink disconnect case");
                            }
                            SecExternalDisplayIntents.this.handleKddiAuShareLink(false);
                            return;
                        default:
                            return;
                    }
                    Log.e(SecExternalDisplayIntents.TAG, "Exception at :: " + e);
                }
            } catch (Exception e) {
                Log.e(SecExternalDisplayIntents.TAG, "Exception at :: " + e);
            }
        }
    };

    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent = new int[EDSRecievedIntent.values().length];

        static {
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_HDMI.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_RearCamera.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_FrontCamera.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_CameraStop.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_RVF.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_WFD.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_AlarmStart.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_AlarmStart2.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_AlarmStart3.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_AlarmStop.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_AlarmStop2.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_AlarmStop3.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_SideSync.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_SideSyncDC.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_ScreenRecorder.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_ScreenRecorder2.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_SmartDock.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_ScreenLocked.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_ScreenUnlocked.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_VideoStart.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_VideoStop.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_PresentationStart.ordinal()] = 22;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_PresentationStop.ordinal()] = 23;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_KddiAuShareLinkConnect.ordinal()] = 24;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayIntents$EDSRecievedIntent[EDSRecievedIntent.EDS_INTENT_KddiAuShareLinkDisconnect.ordinal()] = 25;
            } catch (NoSuchFieldError e25) {
            }
        }
    }

    private enum EDSRecievedIntent {
        EDS_INTENT_HDMI(1),
        EDS_INTENT_RearCamera(2),
        EDS_INTENT_FrontCamera(3),
        EDS_INTENT_CameraStop(4),
        EDS_INTENT_RVF(5),
        EDS_INTENT_WFD(6),
        EDS_INTENT_AlarmStart(7),
        EDS_INTENT_AlarmStart2(8),
        EDS_INTENT_AlarmStart3(9),
        EDS_INTENT_AlarmStop(10),
        EDS_INTENT_AlarmStop2(11),
        EDS_INTENT_AlarmStop3(12),
        EDS_INTENT_SideSync(13),
        EDS_INTENT_SideSyncDC(14),
        EDS_INTENT_ScreenRecorder(15),
        EDS_INTENT_ScreenRecorder2(16),
        EDS_INTENT_SmartDock(17),
        EDS_INTENT_ScreenLocked(18),
        EDS_INTENT_ScreenUnlocked(19),
        EDS_INTENT_VideoStart(20),
        EDS_INTENT_VideoStop(21),
        EDS_INTENT_KddiAuShareLinkConnect(22),
        EDS_INTENT_KddiAuShareLinkDisconnect(23),
        EDS_INTENT_PresentationStart(24),
        EDS_INTENT_PresentationStop(25);
        
        private final int value;

        private EDSRecievedIntent(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static EDSRecievedIntent toStatus(int value) {
            for (EDSRecievedIntent tempVal : values()) {
                if (tempVal.getValue() == value) {
                    return tempVal;
                }
            }
            return null;
        }
    }

    public static void StartEDSIntents(SecExternalDisplayService edsObj) {
        SecExternalDisplayIntents intentObj = new SecExternalDisplayIntents(edsObj);
    }

    public SecExternalDisplayIntents(SecExternalDisplayService _obj) {
        super(_obj);
        this.mEDSServiceObj = _obj;
        Log.i(TAG, "SecExternalDisplayIntents +");
        SecExternalDisplayRegistIntentReceiver();
    }

    public void SecExternalDisplayRegistIntentReceiver() {
        int i;
        if (bIsLogEnabled) {
            Log.i(TAG, "entered SecExternalDisplayRegistIntentReceiver");
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(WindowManagerPolicy.ACTION_HDMI_PLUGGED);
        filter.addAction(CameraRearStart);
        filter.addAction(CameraFrontStart);
        filter.addAction(CameraStop);
        filter.addAction(ALLSHARE_CAST_GETSTATE);
        filter.addAction("android.intent.action.USBHID_MOUSE_EVENT");
        filter.addAction(SCREENRECORDER_NOTIFY_EVENT);
        filter.addAction(RemoteViewFinder);
        filter.addAction(SideSyncConnected);
        filter.addAction(SideSyncDestroyed);
        for (i = 0; i < 3; i++) {
            filter.addAction(this.ALARM_STARTED[i]);
        }
        for (i = 0; i < 3; i++) {
            filter.addAction(this.ALARM_STOPPED[i]);
        }
        filter.addAction(PresentationStart);
        filter.addAction(PresentationStop);
        filter.addAction(VIDEO_PRESENTATION_START_MODE);
        filter.addAction(VIDEO_PRESENTATION_STOP_MODE);
        filter.addAction(KDDI_auSharelink_SOURCE_CONNECT);
        filter.addAction(KDDI_auSharelink_SOURCE_DISCONNECT);
        this.mEDSServiceObj.getEDSContext().registerReceiverAsUser(this.mReceiver2, UserHandle.ALL, filter, null, null);
        this.BroadcastMap = new HashMap();
        this.BroadcastMap.put(WindowManagerPolicy.ACTION_HDMI_PLUGGED, Integer.valueOf(1));
        this.BroadcastMap.put(CameraRearStart, Integer.valueOf(2));
        this.BroadcastMap.put(CameraFrontStart, Integer.valueOf(3));
        this.BroadcastMap.put(CameraStop, Integer.valueOf(4));
        this.BroadcastMap.put(RemoteViewFinder, Integer.valueOf(5));
        this.BroadcastMap.put(ALLSHARE_CAST_GETSTATE, Integer.valueOf(6));
        this.BroadcastMap.put(this.ALARM_STARTED[0], Integer.valueOf(7));
        this.BroadcastMap.put(this.ALARM_STARTED[1], Integer.valueOf(8));
        this.BroadcastMap.put(this.ALARM_STARTED[2], Integer.valueOf(9));
        this.BroadcastMap.put(this.ALARM_STOPPED[0], Integer.valueOf(10));
        this.BroadcastMap.put(this.ALARM_STOPPED[1], Integer.valueOf(11));
        this.BroadcastMap.put(this.ALARM_STOPPED[2], Integer.valueOf(12));
        this.BroadcastMap.put(SideSyncConnected, Integer.valueOf(13));
        this.BroadcastMap.put(SideSyncDestroyed, Integer.valueOf(14));
        this.BroadcastMap.put(SCREENRECORDER_NOTIFY_EVENT, Integer.valueOf(16));
        this.BroadcastMap.put("android.intent.action.USBHID_MOUSE_EVENT", Integer.valueOf(17));
        this.BroadcastMap.put("android.intent.action.SCREEN_OFF", Integer.valueOf(18));
        this.BroadcastMap.put("android.intent.action.USER_PRESENT", Integer.valueOf(19));
        this.BroadcastMap.put(VIDEO_PRESENTATION_START_MODE, Integer.valueOf(20));
        this.BroadcastMap.put(VIDEO_PRESENTATION_STOP_MODE, Integer.valueOf(21));
        this.BroadcastMap.put(KDDI_auSharelink_SOURCE_CONNECT, Integer.valueOf(22));
        this.BroadcastMap.put(KDDI_auSharelink_SOURCE_DISCONNECT, Integer.valueOf(23));
        this.BroadcastMap.put(PresentationStart, Integer.valueOf(24));
        this.BroadcastMap.put(PresentationStop, Integer.valueOf(25));
    }
}
