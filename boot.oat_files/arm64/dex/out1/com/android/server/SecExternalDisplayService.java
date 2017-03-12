package com.android.server;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.hardware.SecExternalDisplayJNIInterface;
import android.hardware.display.DisplayManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.ISecExternalDisplayService.Stub;
import android.os.Message;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UEventObserver;
import android.os.UEventObserver.UEvent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.CheckBox;
import android.widget.Toast;
import com.android.internal.R;
import com.android.internal.os.SMProviderContract;
import com.android.internal.telephony.PhoneConstants;
import com.samsung.android.smartface.SmartFaceManager;

public class SecExternalDisplayService extends Stub {
    private static final int HORIZONTAL_ALIGNMENT = 80;
    private static final boolean LOG = true;
    private static final String TAG = "SecExternalDisplayService_Java";
    private static boolean bIsLogEnabled = false;
    private static Context mContext;
    private static SecExternalDisplayJNIInterface mEDSInterface;
    public boolean CALL_CONNECT = false;
    private boolean bDreamsEnabledByDefaultConfig;
    private boolean bIsAlarmRecieved = false;
    private boolean bIsDayDreamEnabled;
    private boolean bIsHomeTheatreConnected = false;
    private boolean bPresentation = false;
    private AlertDialog mAlertDialog = null;
    private CheckBox mDialogCheckBox;
    public DisplayManager mDisplayManager;
    private int mDockSurfaceParameter = 0;
    private final Handler mMHLHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.arg1 != 0) {
                SecExternalDisplayService.this.SecExternalDisplayAlertMsg(message.arg1);
            } else if (SecExternalDisplayService.this.mAlertDialog != null) {
                SecExternalDisplayService.this.mAlertDialog.dismiss();
            }
        }
    };
    private UEventObserver mMHLObserver = new UEventObserver() {
        public void onUEvent(UEvent event) {
            int EventState = Integer.parseInt(event.get("SWITCH_STATE"));
            if (SecExternalDisplayService.bIsLogEnabled) {
                Log.i(SecExternalDisplayService.TAG, "Received Kernel Event with EventState : " + EventState);
            }
            Message msg = new Message();
            msg.arg1 = EventState;
            SecExternalDisplayService.this.mMHLHandler.sendMessage(msg);
        }
    };
    public PhoneStateListener mPhoneStateListener = null;
    public TelephonyManager mTelephonyManager = null;
    private WakeLock mWakeLock = null;
    WindowManager wm;

    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$android$server$SecExternalDisplayService$EDSToast = new int[EDSToast.values().length];

        static {
            try {
                $SwitchMap$com$android$server$SecExternalDisplayService$EDSToast[EDSToast.EDS_TOAST_HDMI_CONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayService$EDSToast[EDSToast.EDS_TOAST_HDMI_DISCONNECTED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayService$EDSToast[EDSToast.EDS_TOAST_WFD_DISCONNECTED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public enum EDSBlank {
        EDS_POWER_MODE_OFF(0),
        EDS_POWER_MODE_ON(2);
        
        private final int value;

        private EDSBlank(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static EDSBlank toStatus(int value) {
            for (EDSBlank tempVal : values()) {
                if (tempVal.getValue() == value) {
                    return tempVal;
                }
            }
            return null;
        }
    }

    public enum EDSFlagStatus {
        EDS_CONNECTION_STATUS_HDMI(0),
        EDS_CONNECTION_STATUS_WFD(1),
        EDS_CONNECTION_STATUS_SMARTDOCK(2),
        EDS_CONNECTION_STATUS_RVF(3),
        EDS_CONNECTION_STATUS_SIDESYNC(4),
        EDS_CONNECTION_STATUS_SCREENRECORDING(5),
        EDS_CONNECTION_STATUS_HOMETHEATRE(6),
        EDS_CONNECTION_STATUS_EVF(7),
        EDS_CONNECTION_STATUS_CAMERA_START(8),
        EDS_STATUS_CAMERA_ROTATION_SUPPORT(9),
        EDS_CONNECTION_STATUS_VIDEO(10),
        EDS_CONNECTION_STATUS_KDDI_AUSHARELINK(11);
        
        private final int value;

        private EDSFlagStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static EDSFlagStatus toStatus(int value) {
            for (EDSFlagStatus tempVal : values()) {
                if (tempVal.getValue() == value) {
                    return tempVal;
                }
            }
            return null;
        }
    }

    private enum EDSToast {
        EDS_TOAST_HDMI_CONNECTED(0),
        EDS_TOAST_HDMI_DISCONNECTED(1),
        EDS_TOAST_WFD_DISCONNECTED(2);
        
        private final int value;

        private EDSToast(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static EDSToast toStatus(int value) {
            for (EDSToast tempVal : values()) {
                if (tempVal.getValue() == value) {
                    return tempVal;
                }
            }
            return null;
        }
    }

    public Context getEDSContext() {
        return mContext;
    }

    public SecExternalDisplayService(Context context) {
        bIsLogEnabled = SystemProperties.getBoolean("persist.sys.SecEDS.Logs", false);
        Log.i(TAG, "SecExternalDisplayService +");
        mContext = context;
        mEDSInterface = new SecExternalDisplayJNIInterface();
        this.mDisplayManager = (DisplayManager) context.getSystemService("display");
        this.wm = (WindowManager) context.getSystemService("window");
        this.mMHLObserver.startObserving("DEVPATH=/devices/virtual/switch/mhl_event_switch");
        SecExternalDisplayInitWakeMode(mContext, 6);
        SecExternalDisplayIntents.StartEDSIntents(this);
        if (((double) getCameraAppVersionNumber()) < 3.0d) {
            SecExternalDisplaySetStatus(EDSFlagStatus.EDS_STATUS_CAMERA_ROTATION_SUPPORT.ordinal(), true);
        } else {
            SecExternalDisplaySetStatus(EDSFlagStatus.EDS_STATUS_CAMERA_ROTATION_SUPPORT.ordinal(), false);
        }
    }

    public float getCameraAppVersionNumber() {
        float versionNumber = 0.0f;
        try {
            PackageInfo pi = getEDSContext().getPackageManager().getPackageInfo("com.sec.android.app.camera", 0);
            String packageName = pi.packageName;
            versionNumber = Float.parseFloat(pi.versionName);
            Log.i(TAG, "getCameraAppVersionNumber. Package: " + packageName + " , versionNumber=" + versionNumber);
            return versionNumber;
        } catch (NameNotFoundException e) {
            Log.i(TAG, "com.sec.android.app.camera Package name not found");
            e.printStackTrace();
            return versionNumber;
        }
    }

    private void SecExternalDisplayInitWakeMode(Context context, int nMode) {
        boolean wakelockHeld = false;
        if (this.mWakeLock != null) {
            if (bIsLogEnabled) {
                Log.i(TAG, "mWakeLock is not null");
            }
            if (this.mWakeLock.isHeld()) {
                wakelockHeld = true;
                this.mWakeLock.release();
            }
            this.mWakeLock = null;
        }
        if (bIsLogEnabled) {
            Log.i(TAG, "SecExternalDisplayInitWakeMode is called");
        }
        this.mWakeLock = ((PowerManager) context.getSystemService(SMProviderContract.KEY_POWER)).newWakeLock(536870912 | nMode, TAG);
        if (bIsLogEnabled) {
            Log.i(TAG, "SecExternalDisplayInitWakeMode set the mode : " + nMode);
        }
        this.mWakeLock.setReferenceCounted(false);
        if (wakelockHeld) {
            this.mWakeLock.acquire();
        }
    }

    private void SecExternalDisplaySetWakeLock(boolean bStatus) {
        if (this.mWakeLock != null) {
            if (bStatus && !this.mWakeLock.isHeld()) {
                if (bIsLogEnabled) {
                    Log.i(TAG, "Acquire the lock for Wake status");
                }
                this.mWakeLock.acquire();
            } else if (!bStatus && this.mWakeLock.isHeld()) {
                if (bIsLogEnabled) {
                    Log.i(TAG, "release the lock for Wake status");
                }
                this.mWakeLock.release();
            }
        } else if (bIsLogEnabled) {
            Log.e(TAG, "mWakeLock is null");
        }
    }

    public void SecExternalDisplayToast(int nIndex) {
        if (bIsLogEnabled) {
            Log.i(TAG, " SecExternalDisplayToast : " + nIndex);
        }
        CharSequence strDefault = null;
        if (EDSToast.toStatus(nIndex) != null) {
            switch (AnonymousClass5.$SwitchMap$com$android$server$SecExternalDisplayService$EDSToast[EDSToast.toStatus(nIndex).ordinal()]) {
                case 1:
                    strDefault = mContext.getResources().getString(R.string.hdmi_connect);
                    break;
                case 2:
                    strDefault = mContext.getResources().getString(R.string.hdmi_disconnect);
                    break;
                case 3:
                    strDefault = mContext.getResources().getString(R.string.hdmi_wfd_terminate);
                    break;
            }
            if (strDefault != null) {
                Toast toast = Toast.makeText(mContext, strDefault, 0);
                LayoutParams windowParams = toast.getWindowParams();
                windowParams.privateFlags |= 16;
                toast.show();
            }
        }
    }

    public void handlePresentation(boolean status) {
        if (bIsLogEnabled) {
            Log.i(TAG, " handlePresentation Condition : " + status);
        }
        if (status) {
            this.bPresentation = true;
        } else {
            this.bPresentation = false;
        }
        if (!SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_HDMI.ordinal()) || !SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal())) {
            return;
        }
        if (status) {
            SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_ON.ordinal());
        } else {
            SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_OFF.ordinal());
        }
    }

    public boolean handleHDMIConnection(boolean status) {
        if (bIsLogEnabled) {
            Log.i(TAG, " handleHDMIConnection : " + status);
        }
        boolean cable_status = SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_HDMI.ordinal());
        SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_HDMI.ordinal(), status);
        SecExternalDisplaySetWakeLock(status);
        if (status) {
            if (SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SIDESYNC.ordinal())) {
                SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SIDESYNC.ordinal(), false);
            }
            if (SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_KDDI_AUSHARELINK.ordinal())) {
                SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_KDDI_AUSHARELINK.ordinal(), false);
            }
            if (SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_WFD.ordinal())) {
                SecExternalDisplayToast(EDSToast.EDS_TOAST_WFD_DISCONNECTED.ordinal());
            } else {
                Log.i(TAG, " HDMI Connected : ");
                SecExternalDisplayToast(EDSToast.EDS_TOAST_HDMI_CONNECTED.ordinal());
            }
            if (SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal())) {
                SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_OFF.ordinal());
            }
        } else {
            Log.i(TAG, " HDMI DisConnected : ");
            if (SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal())) {
                SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_ON.ordinal());
            }
            if (cable_status) {
                SecExternalDisplayToast(EDSToast.EDS_TOAST_HDMI_DISCONNECTED.ordinal());
            }
        }
        return true;
    }

    public boolean handleWFDConnection(int status) {
        Log.i(TAG, "AllShare Cast Connected Status : !! " + status);
        if (status == 1) {
            return SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_WFD.ordinal(), true);
        }
        return SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_WFD.ordinal(), false);
    }

    public boolean handleAlarm(boolean status) {
        if (bIsLogEnabled) {
            Log.i(TAG, "Handle Alarm : !! " + status);
        }
        if (status) {
            this.bIsAlarmRecieved = true;
            if (this.bIsAlarmRecieved && SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal()) && SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_HDMI.ordinal()) && !this.bPresentation) {
                if (bIsLogEnabled) {
                    Log.i(TAG, "Destroy SmartDock Surface !!");
                }
                SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_ON.ordinal());
            }
        } else if (this.bIsAlarmRecieved && SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal()) && SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_HDMI.ordinal()) && !this.bPresentation) {
            if (bIsLogEnabled) {
                Log.i(TAG, "Create SmartDock Surface !!");
            }
            SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_OFF.ordinal());
            this.bIsAlarmRecieved = false;
        }
        return true;
    }

    public boolean handleVideo(boolean status) {
        if (bIsLogEnabled) {
            Log.i(TAG, "Handle Video : !! " + status);
        }
        SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_VIDEO.ordinal(), status);
        if (status) {
            if (SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_KDDI_AUSHARELINK.ordinal()) && SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_HDMI.ordinal())) {
                SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_KDDI_AUSHARELINK.ordinal(), false);
            }
            if (SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal())) {
                if (bIsLogEnabled) {
                    Log.i(TAG, "Destroy SmartDock Surface during videoplay  !!");
                }
                SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_ON.ordinal());
            }
        } else if (SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal())) {
            if (bIsLogEnabled) {
                Log.i(TAG, "Create SmartDock Surface after video is stopped!!");
            }
            SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_OFF.ordinal());
        }
        return true;
    }

    public boolean handleSmartDockConnection(int status) {
        Log.i(TAG, " handleSmartDockConnection status = " + status + "Intent.EXTRA_USB_HID_STATE_ATTACHED value is : " + 1);
        if (1 == status) {
            Log.d(TAG, "Mouse  Connected  Smart Dock!!!!!");
            SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal(), true);
            if (!SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_VIDEO.ordinal())) {
                if (SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_HDMI.ordinal()) && SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal()) && !this.bPresentation) {
                    SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_OFF.ordinal());
                }
                if (SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal())) {
                    if (this.mTelephonyManager == null) {
                        this.mTelephonyManager = (TelephonyManager) mContext.getSystemService(PhoneConstants.PHONE_KEY);
                    }
                    if (this.mTelephonyManager != null) {
                        try {
                            if (this.mPhoneStateListener == null) {
                                this.mPhoneStateListener = new PhoneStateListener() {
                                    public void onCallStateChanged(int state, String incomingNumber) {
                                        if (state == 1 || state == 2) {
                                            if (SecExternalDisplayService.this.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal())) {
                                                SecExternalDisplayService.this.CALL_CONNECT = true;
                                                SecExternalDisplayService.this.SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_ON.ordinal());
                                            }
                                        } else if (SecExternalDisplayService.this.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_HDMI.ordinal()) && SecExternalDisplayService.this.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal()) && !SecExternalDisplayService.this.bPresentation) {
                                            SecExternalDisplayService.this.CALL_CONNECT = false;
                                            SecExternalDisplayService.this.SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_OFF.ordinal());
                                        }
                                    }
                                };
                            }
                            this.mTelephonyManager.listen(this.mPhoneStateListener, 32);
                        } catch (SecurityException e) {
                            Log.w(TAG, "Phone window manager doesn't have the permssion READ_PHONE_STATE. please defines it via <uses-permssion> in AndroidManifest.xml.");
                        }
                    }
                } else if (this.mTelephonyManager != null) {
                    this.mTelephonyManager.listen(this.mPhoneStateListener, 0);
                }
            }
        } else {
            if (status == 0) {
                Log.d(TAG, "Mouse Disconnected from Smart Dock!!!!!");
                if (SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal())) {
                    SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_ON.ordinal());
                    SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal(), false);
                }
            }
            if (SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal())) {
                if (this.mTelephonyManager == null) {
                    this.mTelephonyManager = (TelephonyManager) mContext.getSystemService(PhoneConstants.PHONE_KEY);
                }
                if (this.mTelephonyManager != null) {
                    if (this.mPhoneStateListener == null) {
                        this.mPhoneStateListener = /* anonymous class already generated */;
                    }
                    this.mTelephonyManager.listen(this.mPhoneStateListener, 32);
                }
            } else if (this.mTelephonyManager != null) {
                this.mTelephonyManager.listen(this.mPhoneStateListener, 0);
            }
        }
        return true;
    }

    public boolean SecExternalDisplaySetFPS(int FPS) {
        boolean status = false;
        try {
            IBinder SFservice = ServiceManager.getService("SurfaceFlinger");
            if (SFservice != null) {
                Parcel data = Parcel.obtain();
                data.writeInterfaceToken("android.ui.ISurfaceComposer");
                data.writeInt(FPS);
                status = SFservice.transact(1022, data, null, 0);
                data.recycle();
            }
            return status;
        } catch (RemoteException e) {
            return false;
        }
    }

    public int SecExternalDisplayGetFPS() {
        int fps = -1;
        try {
            IBinder SFservice = ServiceManager.getService("SurfaceFlinger");
            if (SFservice != null) {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeInterfaceToken("android.ui.ISurfaceComposer");
                SFservice.transact(1024, data, reply, 0);
                fps = reply.readInt();
                reply.recycle();
            }
            return fps;
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean SecExternalDisplaySetResolution(int nResolution) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("SecExternalDisplayService available only from system UID.");
        } else if (nResolution == 0) {
            return true;
        } else {
            return mEDSInterface.SecExternalDisplaySetResolution(nResolution);
        }
    }

    public int SecExternalDisplayGetResolution() {
        if (Binder.getCallingUid() == 1000) {
            return mEDSInterface.SecExternalDisplayGetResolution();
        }
        throw new SecurityException("SecExternalDisplayService available only from system UID.");
    }

    public boolean SecExternalDisplaySetOutputMode(int nOutputMode) {
        if (Binder.getCallingUid() == 1000) {
            return mEDSInterface.SecExternalDisplaySetOutputMode(nOutputMode);
        }
        throw new SecurityException("SecExternalDisplayService available only from system UID.");
    }

    public int SecExternalDisplayGet3DMode() {
        if (Binder.getCallingUid() == 1000) {
            return mEDSInterface.SecExternalDisplayGet3DMode();
        }
        throw new SecurityException("SecExternalDisplayService available only from system UID.");
    }

    public int SecExternalDisplaySet3DMode(int nS3DMode) {
        if (Binder.getCallingUid() == 1000) {
            return mEDSInterface.SecExternalDisplaySet3DMode(nS3DMode);
        }
        throw new SecurityException("SecExternalDisplayService available only from system UID.");
    }

    public boolean SecExternalDisplayCreateSurface(String surface, int width, int height) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("SecExternalDisplayService available only from system UID.");
        } else if (surface == null || width == 0 || height == 0) {
            return true;
        } else {
            return mEDSInterface.SecExternalDisplayCreateSurface(surface, width, height);
        }
    }

    public boolean SecExternalDisplayDestroySurface(String surface, int width, int height) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("SecExternalDisplayService available only from system UID.");
        } else if (surface == null || width == 0 || height == 0) {
            return true;
        } else {
            return mEDSInterface.SecExternalDisplayDestroySurface(surface, width, height);
        }
    }

    public boolean SecExternalDisplayBlankDisplay(int mode) {
        if (Binder.getCallingUid() == 1000) {
            return mEDSInterface.SecExternalDisplayBlankDisplay(mode);
        }
        throw new SecurityException("SecExternalDisplayService available only from system UID.");
    }

    public int SecExternalDisplayRegisterEVF(boolean bStatus) {
        if (Binder.getCallingUid() == 1000) {
            return mEDSInterface.SecExternalDisplayRegisterEVF(bStatus);
        }
        throw new SecurityException("SecExternalDisplayService available only from system UID.");
    }

    public boolean SecExternalDisplaySetPause(boolean bStatus) {
        if (Binder.getCallingUid() == 1000) {
            return mEDSInterface.SecExternalDisplaySetPause(bStatus);
        }
        throw new SecurityException("SecExternalDisplayService available only from system UID.");
    }

    public boolean SecExternalDisplaySetStatus(int param, boolean status) {
        if (Binder.getCallingUid() == 1000) {
            return mEDSInterface.SecExternalDisplaySetStatus(param, status);
        }
        throw new SecurityException("SecExternalDisplayService available only from system UID.");
    }

    public boolean SecExternalDisplayGetStatus(int param) {
        if (Binder.getCallingUid() == 1000) {
            return mEDSInterface.SecExternalDisplayGetStatus(param);
        }
        throw new SecurityException("SecExternalDisplayService available only from system UID.");
    }

    public boolean SecExternalDisplayLEDInit() {
        return false;
    }

    public boolean SecExternalDisplayLEDDestroy() {
        return false;
    }

    public boolean SecExternalDisplayLEDRefresh() {
        return false;
    }

    public boolean SecExternalDisplayLEDOff() {
        return false;
    }

    public boolean SecExternalDisplayLEDSetLoop(int enable, int time, int count, int start, int end) {
        return false;
    }

    public boolean SecExternalDisplayLEDSetScroll(int enable, int time, int count, int start, int end) {
        return false;
    }

    public boolean SecExternalDisplayLEDSetframe(byte[] data, int numOfFrames, int repeatCount) {
        return false;
    }

    public boolean SecExternalDisplaySetForceMirrorMode(boolean bEnable) {
        if (Binder.getCallingUid() == 1000) {
            return mEDSInterface.SecExternalDisplaySetForceMirrorMode(bEnable);
        }
        throw new SecurityException("SecExternalDisplayService available only from system UID.");
    }

    public boolean SecExternalDisplaySetExternalUITransform(int transform) {
        if (Binder.getCallingUid() == 1000) {
            return mEDSInterface.SecExternalDisplaySetExternalUITransform(transform);
        }
        throw new SecurityException("SecExternalDisplayService available only from system UID.");
    }

    public boolean SecExternalDisplayType(boolean bIsTablet) {
        if (Binder.getCallingUid() == 1000) {
            return mEDSInterface.SecExternalDisplayType(bIsTablet);
        }
        throw new SecurityException("SecExternalDisplayService available only from system UID.");
    }

    public boolean SecExternalDisplaySetGpuLock(String filePath, int value) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("SecExternalDisplayService available only from system UID.");
        } else if (filePath == null || value == 0) {
            return true;
        } else {
            return mEDSInterface.SecExternalDisplaySetGpuLock(filePath, value);
        }
    }

    public boolean SecExternalDisplayIsTablet() {
        if (this.mDisplayManager == null || this.mDisplayManager.getDisplays() == null || this.wm == null) {
            return false;
        }
        Display display = this.wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int orientation = display.getRotation();
        if (width > height && (orientation == 0 || orientation == 2)) {
            return true;
        }
        if (width >= height || (orientation != 1 && orientation != 3)) {
            return false;
        }
        return true;
    }

    public void SecExternalDisplayAlertMsg(int value) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("SecExternalDisplayService available only from system UID.");
        } else if (!SystemProperties.getBoolean("persist.sys.SecEDS.Dialog", false)) {
            String strPopUp;
            switch (value) {
                case 1:
                    strPopUp = mContext.getResources().getString(R.string.hdmi_connect_indirectly);
                    break;
                case 2:
                    strPopUp = mContext.getResources().getString(R.string.hdmi_recommend_charging_device);
                    break;
                default:
                    Log.e(TAG, " Received Wrong value from kernel ");
                    return;
            }
            boolean isLightTheme = false;
            String mThemeType = SystemProperties.get("ro.build.scafe.cream");
            if (mThemeType != null && mThemeType.contains("white")) {
                isLightTheme = true;
            }
            Log.d(TAG, "isLightTheme => " + isLightTheme);
            int mAlertTheme = isLightTheme ? R.style.Theme_DeviceDefault_Light_Dialog_Alert : R.style.Theme_DeviceDefault_Dialog_Alert;
            int mCheckBoxColor = isLightTheme ? -16777216 : -1;
            this.mDialogCheckBox = new CheckBox(mContext);
            this.mDialogCheckBox.setText(mContext.getResources().getString(R.string.do_not_show_again));
            this.mDialogCheckBox.setTextColor(mCheckBoxColor);
            Builder builder = new Builder(mContext, mAlertTheme);
            builder.setTitle(mContext.getResources().getString(R.string.hdmi_connect_charger));
            builder.setMessage(strPopUp);
            builder.setView(this.mDialogCheckBox, this.mDialogCheckBox.getLeft() + 80, this.mDialogCheckBox.getTop(), this.mDialogCheckBox.getRight(), this.mDialogCheckBox.getBottom());
            builder.setPositiveButton(mContext.getResources().getString(R.string.dlg_ok), new OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (SecExternalDisplayService.this.mDialogCheckBox.isChecked()) {
                        SystemProperties.set("persist.sys.SecEDS.Dialog", SmartFaceManager.TRUE);
                    } else {
                        SystemProperties.set("persist.sys.SecEDS.Dialog", SmartFaceManager.FALSE);
                    }
                    dialog.dismiss();
                }
            });
            this.mAlertDialog = builder.create();
            this.mAlertDialog.getWindow().setType(LayoutParams.TYPE_SYSTEM_ALERT);
            this.mAlertDialog.show();
        }
    }
}
