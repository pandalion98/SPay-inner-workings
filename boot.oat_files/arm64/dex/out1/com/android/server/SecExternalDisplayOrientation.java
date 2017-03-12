package com.android.server;

import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.provider.Settings$System;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import com.android.internal.R;
import com.android.server.SecExternalDisplayService.EDSBlank;
import com.android.server.SecExternalDisplayService.EDSFlagStatus;

public class SecExternalDisplayOrientation {
    public static final String EXTRA_IS_FORCE_MIRROR = "isForceMirror";
    private static final boolean LOG = true;
    private static final String TAG = "SecExternalDisplayOrientation_Java";
    private static boolean bIsCameraOn = false;
    protected static boolean bIsLogEnabled = false;
    private static boolean bIsTabletConnected = false;
    private static int newOrientation = 0;
    private final DisplayListener mDisplayListener = new DisplayListener() {
        public void onDisplayAdded(int displayId) {
            Display tempDisplay = SecExternalDisplayOrientation.this.mEDSServiceObj.mDisplayManager.getDisplay(displayId);
            if (tempDisplay != null) {
                Point mPoint = new Point();
                tempDisplay.getSize(mPoint);
                if (SecExternalDisplayOrientation.bIsLogEnabled) {
                    Log.i(SecExternalDisplayOrientation.TAG, "Display #" + displayId + " added.");
                    Log.i(SecExternalDisplayOrientation.TAG, "name=" + tempDisplay.getName() + ", rot = " + tempDisplay.getRotation() + "state= " + tempDisplay.getState() + "size = " + mPoint.x + "x" + mPoint.y + "isValid =" + tempDisplay.isValid() + "type= " + Display.typeToString(tempDisplay.getType()));
                }
                if (SecExternalDisplayOrientation.this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SIDESYNC.ordinal()) && tempDisplay.getType() == 3 && tempDisplay.getName().equals("wfdservice")) {
                    SecExternalDisplayOrientation.this.mSidesyncDisplayId = displayId;
                    Log.i(SecExternalDisplayOrientation.TAG, "Sidesync display added, #" + SecExternalDisplayOrientation.this.mSidesyncDisplayId);
                }
            }
        }

        public void onDisplayRemoved(int displayId) {
            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                Log.i(SecExternalDisplayOrientation.TAG, "Display #" + displayId + " removed.");
            }
            if (SecExternalDisplayOrientation.this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SIDESYNC.ordinal()) && displayId == SecExternalDisplayOrientation.this.mSidesyncDisplayId) {
                Log.i(SecExternalDisplayOrientation.TAG, "Sidesync display removed, #" + SecExternalDisplayOrientation.this.mSidesyncDisplayId);
                SecExternalDisplayOrientation.this.mEDSServiceObj.SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_ON.ordinal());
                SecExternalDisplayOrientation.this.mSidesyncDisplayId = -1;
            }
        }

        public void onDisplayChanged(int displayId) {
            Display tempDisplay = SecExternalDisplayOrientation.this.mEDSServiceObj.mDisplayManager.getDisplay(displayId);
            if (tempDisplay != null) {
                Point mPoint = new Point();
                tempDisplay.getSize(mPoint);
                if (SecExternalDisplayOrientation.bIsLogEnabled) {
                    Log.i(SecExternalDisplayOrientation.TAG, "Display #" + displayId + " changed.");
                    Log.i(SecExternalDisplayOrientation.TAG, "name=" + tempDisplay.getName() + ", rot = " + tempDisplay.getRotation() + "state= " + tempDisplay.getState() + "size = " + mPoint.x + "x" + mPoint.y + "isValid =" + tempDisplay.isValid());
                }
            }
        }
    };
    private int mDockSurfaceParameter = 0;
    protected SecExternalDisplayService mEDSServiceObj;
    OrientationEventListener mOrientationListenerForCamera = null;
    private final ContentObserver mSidesyncContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
        public void onChange(boolean selfChange) {
            if (SecExternalDisplayOrientation.bIsLogEnabled) {
                Log.i(SecExternalDisplayOrientation.TAG, "mSidesyncContentObserver onChange + selfChange=" + selfChange);
            }
            SecExternalDisplayOrientation.this.handleBlankDispDuringSidesync();
        }
    };
    private int mSidesyncDisplayId = -1;
    private int setOrientation = 0;

    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$android$server$SecExternalDisplayOrientation$EDS_CameraStatus = new int[EDS_CameraStatus.values().length];

        static {
            try {
                $SwitchMap$com$android$server$SecExternalDisplayOrientation$EDS_CameraStatus[EDS_CameraStatus.REAR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayOrientation$EDS_CameraStatus[EDS_CameraStatus.FRONT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$server$SecExternalDisplayOrientation$EDS_CameraStatus[EDS_CameraStatus.STOP.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public enum EDS_CameraStatus {
        REAR(0),
        FRONT(1),
        STOP(2);
        
        private final int value;

        private EDS_CameraStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static EDS_CameraStatus toStatus(int value) {
            for (EDS_CameraStatus tempVal : values()) {
                if (tempVal.getValue() == value) {
                    return tempVal;
                }
            }
            return null;
        }
    }

    private enum EDS_Transform {
        Potrait(0),
        Landscape(1),
        Inverse_Potrait(2),
        Inverse_Landscape(3),
        BufMirrorMode_Start(4),
        BufMirrorMode_Stop(5);
        
        private final int value;

        private EDS_Transform(int value) {
            this.value = value;
        }
    }

    public void SecExternalDisplaySetOrientationMode(boolean bMode) {
        if (bMode) {
            if (this.mOrientationListenerForCamera != null) {
                this.mOrientationListenerForCamera.enable();
            }
        } else if (this.mOrientationListenerForCamera != null) {
            this.mOrientationListenerForCamera.disable();
        }
    }

    private void setOrientationListenerForCamera(Context context) {
        if (this.mOrientationListenerForCamera == null) {
            this.mOrientationListenerForCamera = new OrientationEventListener(context) {
                public void onOrientationChanged(int orientation) {
                    if (orientation != -1 && SecExternalDisplayOrientation.bIsCameraOn) {
                        SecExternalDisplayOrientation.newOrientation = EDS_Transform.Potrait.ordinal();
                        orientation %= DisplayMetrics.DENSITY_360;
                        if (orientation < 45) {
                            SecExternalDisplayOrientation.newOrientation = EDS_Transform.Landscape.ordinal();
                        } else if (orientation < 135) {
                            SecExternalDisplayOrientation.newOrientation = EDS_Transform.Inverse_Potrait.ordinal();
                        } else if (orientation < 225) {
                            SecExternalDisplayOrientation.newOrientation = EDS_Transform.Inverse_Landscape.ordinal();
                        } else if (orientation < R.styleable.Theme_findOnPageNextDrawable) {
                            SecExternalDisplayOrientation.newOrientation = EDS_Transform.Potrait.ordinal();
                        } else if (orientation < DisplayMetrics.DENSITY_360) {
                            SecExternalDisplayOrientation.newOrientation = EDS_Transform.Landscape.ordinal();
                        }
                        if (SecExternalDisplayOrientation.bIsLogEnabled) {
                            Log.i(SecExternalDisplayOrientation.TAG, " Checking Orientation Value :  " + SecExternalDisplayOrientation.newOrientation + " bIsCameraOn = " + SecExternalDisplayOrientation.bIsCameraOn);
                        }
                        if (SecExternalDisplayOrientation.this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_HDMI.ordinal()) || !(SecExternalDisplayOrientation.this.mEDSServiceObj.mDisplayManager.getDisplays().length <= 1 || SecExternalDisplayOrientation.this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SIDESYNC.ordinal()) || SecExternalDisplayOrientation.this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_KDDI_AUSHARELINK.ordinal()) || SecExternalDisplayOrientation.this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_RVF.ordinal()))) {
                            SecExternalDisplayOrientation.this.mEDSServiceObj.SecExternalDisplaySetExternalUITransform(SecExternalDisplayOrientation.newOrientation);
                        }
                    } else if (SecExternalDisplayOrientation.bIsLogEnabled) {
                        Log.i(SecExternalDisplayOrientation.TAG, " Checking orientation :" + orientation + " bIsCameraOn = " + SecExternalDisplayOrientation.bIsCameraOn + " return");
                    }
                }
            };
        }
    }

    public SecExternalDisplayOrientation(SecExternalDisplayService _obj) {
        this.mEDSServiceObj = _obj;
        Log.i(TAG, "SecExternalDisplayService constructor");
        setOrientationListenerForCamera(this.mEDSServiceObj.getEDSContext());
        bIsLogEnabled = SystemProperties.getBoolean("persist.sys.SecEDS.Logs", false);
        Display display = ((DisplayManager) this.mEDSServiceObj.getEDSContext().getSystemService("display")).getDisplays()[0];
        int width = display.getWidth();
        int height = display.getHeight();
        int orientation = display.getRotation();
        if ((width <= height || !(orientation == 0 || orientation == 2)) && (width >= height || !(orientation == 1 || orientation == 3))) {
            bIsTabletConnected = false;
        } else {
            bIsTabletConnected = true;
        }
        this.mEDSServiceObj.SecExternalDisplayType(bIsTabletConnected);
    }

    public boolean handleCameraConnection(int status) {
        if (bIsLogEnabled) {
            Log.i(TAG, "handle Camera Connection = " + status);
        }
        if (EDS_CameraStatus.toStatus(status) == null) {
            return false;
        }
        switch (AnonymousClass4.$SwitchMap$com$android$server$SecExternalDisplayOrientation$EDS_CameraStatus[EDS_CameraStatus.toStatus(status).ordinal()]) {
            case 1:
            case 2:
                this.mEDSServiceObj.SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_CAMERA_START.ordinal(), true);
                if (bIsLogEnabled) {
                    Log.i(TAG, "entered Camera Rear Case");
                }
                if (!(this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_RVF.ordinal()) || this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SIDESYNC.ordinal()) || this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_KDDI_AUSHARELINK.ordinal()))) {
                    if (this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_HDMI.ordinal()) && this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal())) {
                        if (bIsLogEnabled) {
                            Log.i(TAG, "Destroy SmartDock Surface");
                        }
                        this.mEDSServiceObj.SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_ON.ordinal());
                    }
                    if (this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_STATUS_CAMERA_ROTATION_SUPPORT.ordinal())) {
                        if (bIsCameraOn) {
                            this.setOrientation = (newOrientation + 180) % DisplayMetrics.DENSITY_360;
                            if (bIsLogEnabled) {
                                Log.i(TAG, " Calling Camera with setOrientation Value :  " + this.setOrientation);
                            }
                            if (this.setOrientation == EDS_Transform.Potrait.ordinal() || this.setOrientation == EDS_Transform.Inverse_Landscape.ordinal()) {
                                this.mEDSServiceObj.SecExternalDisplaySetExternalUITransform(this.setOrientation);
                            }
                        } else {
                            this.mEDSServiceObj.SecExternalDisplaySetExternalUITransform(newOrientation);
                        }
                    }
                    bIsCameraOn = true;
                    if (this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_STATUS_CAMERA_ROTATION_SUPPORT.ordinal())) {
                        SecExternalDisplaySetOrientationMode(true);
                        break;
                    }
                }
                break;
            case 3:
                this.mEDSServiceObj.SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_CAMERA_START.ordinal(), false);
                if (bIsLogEnabled) {
                    Log.i(TAG, "Camera Stop Case");
                }
                if (this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_HDMI.ordinal()) && this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SMARTDOCK.ordinal())) {
                    if (bIsLogEnabled) {
                        Log.i(TAG, "Create SmartDock Surface");
                    }
                    if (!this.mEDSServiceObj.CALL_CONNECT) {
                        this.mEDSServiceObj.SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_OFF.ordinal());
                    }
                }
                bIsCameraOn = false;
                if (this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_STATUS_CAMERA_ROTATION_SUPPORT.ordinal())) {
                    SecExternalDisplaySetOrientationMode(false);
                    if (!bIsTabletConnected) {
                        this.mEDSServiceObj.SecExternalDisplaySetExternalUITransform(EDS_Transform.Potrait.ordinal());
                        break;
                    }
                    this.mEDSServiceObj.SecExternalDisplaySetExternalUITransform(EDS_Transform.Landscape.ordinal());
                    break;
                }
                break;
        }
        return true;
    }

    private void handleBlankDispDuringSidesync() {
        try {
            boolean blankDisp = Settings$System.getIntForUser(this.mEDSServiceObj.getEDSContext().getContentResolver(), "sidesync_source_presentation", 0, -2) == 1;
            if (blankDisp) {
                Log.i(TAG, "handleBlankDispDuringSidesync, MODE_OFF, blankDisp=" + blankDisp);
                this.mEDSServiceObj.SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_OFF.ordinal());
                return;
            }
            Log.i(TAG, "handleBlankDispDuringSidesync, MODE_ON, blankDisp=" + blankDisp);
            this.mEDSServiceObj.SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_ON.ordinal());
        } catch (Exception e) {
            Log.e(TAG, "Exception getSideSyncSetting '" + false + "'", e);
        }
    }

    private void registerSideSync_SettingObserver(boolean register) {
        if (register) {
            if (bIsLogEnabled) {
                Log.d(TAG, "Registering ContentObserver for sidesync_source_presentation");
            }
            this.mEDSServiceObj.getEDSContext().getContentResolver().registerContentObserver(Settings$System.getUriFor("sidesync_source_presentation"), true, this.mSidesyncContentObserver, -1);
            return;
        }
        if (bIsLogEnabled) {
            Log.d(TAG, "Unregister ContentObserver for sidesync_source_presentation");
        }
        this.mEDSServiceObj.getEDSContext().getContentResolver().unregisterContentObserver(this.mSidesyncContentObserver);
    }

    public boolean handleSideSyncConnection(boolean status) {
        if (bIsLogEnabled) {
            Log.i(TAG, "entered handleSideSyncConnection with status :" + status);
        }
        if (status) {
            this.mEDSServiceObj.SecExternalDisplaySetExternalUITransform(EDS_Transform.BufMirrorMode_Start.ordinal());
            this.mEDSServiceObj.SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SIDESYNC.ordinal(), status);
            if (((double) this.mEDSServiceObj.getCameraAppVersionNumber()) < 3.0d) {
                this.mEDSServiceObj.SecExternalDisplaySetStatus(EDSFlagStatus.EDS_STATUS_CAMERA_ROTATION_SUPPORT.ordinal(), false);
            }
            if (!(this.mEDSServiceObj.mDisplayManager == null || this.mDisplayListener == null)) {
                if (bIsLogEnabled) {
                    Log.d(TAG, "Registering display listener for sidesync");
                }
                this.mEDSServiceObj.mDisplayManager.registerDisplayListener(this.mDisplayListener, null);
            }
            registerSideSync_SettingObserver(true);
        } else {
            if (!(this.mEDSServiceObj.mDisplayManager == null || this.mDisplayListener == null)) {
                if (bIsLogEnabled) {
                    Log.d(TAG, "Unregister display listener for sidesync");
                }
                this.mEDSServiceObj.mDisplayManager.unregisterDisplayListener(this.mDisplayListener);
            }
            registerSideSync_SettingObserver(false);
            this.mEDSServiceObj.SecExternalDisplayBlankDisplay(EDSBlank.EDS_POWER_MODE_ON.ordinal());
            this.mEDSServiceObj.SecExternalDisplaySetExternalUITransform(EDS_Transform.BufMirrorMode_Stop.ordinal());
            this.mEDSServiceObj.SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SIDESYNC.ordinal(), status);
            if (((double) this.mEDSServiceObj.getCameraAppVersionNumber()) < 3.0d) {
                this.mEDSServiceObj.SecExternalDisplaySetStatus(EDSFlagStatus.EDS_STATUS_CAMERA_ROTATION_SUPPORT.ordinal(), true);
            }
        }
        return true;
    }

    public boolean handleKddiAuShareLink(boolean status) {
        if (bIsLogEnabled) {
            Log.i(TAG, "entered handleKddiAuShareLink with status :" + status);
        }
        if (status) {
            this.mEDSServiceObj.SecExternalDisplaySetExternalUITransform(EDS_Transform.BufMirrorMode_Start.ordinal());
            this.mEDSServiceObj.SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_KDDI_AUSHARELINK.ordinal(), status);
            if (((double) this.mEDSServiceObj.getCameraAppVersionNumber()) < 3.0d) {
                this.mEDSServiceObj.SecExternalDisplaySetStatus(EDSFlagStatus.EDS_STATUS_CAMERA_ROTATION_SUPPORT.ordinal(), false);
            }
        } else {
            this.mEDSServiceObj.SecExternalDisplaySetExternalUITransform(EDS_Transform.BufMirrorMode_Stop.ordinal());
            this.mEDSServiceObj.SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_KDDI_AUSHARELINK.ordinal(), status);
            if (((double) this.mEDSServiceObj.getCameraAppVersionNumber()) < 3.0d) {
                this.mEDSServiceObj.SecExternalDisplaySetStatus(EDSFlagStatus.EDS_STATUS_CAMERA_ROTATION_SUPPORT.ordinal(), true);
            }
        }
        return true;
    }

    public boolean handleRVFConnection(boolean status) {
        if (this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SIDESYNC.ordinal())) {
            this.mEDSServiceObj.SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_SIDESYNC.ordinal(), false);
        }
        if (this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_KDDI_AUSHARELINK.ordinal())) {
            this.mEDSServiceObj.SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_KDDI_AUSHARELINK.ordinal(), false);
        }
        if (bIsLogEnabled) {
            Log.i(TAG, "entered handleRVFConnection with status :" + status);
        }
        if (status != this.mEDSServiceObj.SecExternalDisplayGetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_RVF.ordinal())) {
            this.mEDSServiceObj.SecExternalDisplaySetExternalUITransform(EDS_Transform.Potrait.ordinal());
        }
        this.mEDSServiceObj.SecExternalDisplaySetStatus(EDSFlagStatus.EDS_CONNECTION_STATUS_RVF.ordinal(), status);
        return true;
    }
}
