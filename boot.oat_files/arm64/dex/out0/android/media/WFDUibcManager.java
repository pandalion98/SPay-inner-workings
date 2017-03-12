package android.media;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManagerGlobal;
import android.hardware.input.InputManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.ContactsContract.Directory;
import android.provider.Settings.System;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;
import android.view.OrientationEventListener;
import android.view.WindowManager;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class WFDUibcManager {
    private static boolean DEBUG = false;
    private static HashMap<Integer, Integer> DISPLAY_MAP = new HashMap();
    private static final int MAX_EVENTS = 10;
    private static final int MAX_KEY_EVENTS = 15;
    private static final String TAG = "WFDUibcManager";
    private Context mContext;
    private boolean mCoupleShot = false;
    private EventDispatcher mEventDispatcher = null;
    private Thread mEventDispatcherThread = null;
    private final IntentFilter mIntentFilter = new IntentFilter();
    private float mNegRs_X = 1920.0f;
    private float mNegRs_Y = 1080.0f;
    private int mOrientation = -1;
    private OrientationEventListener mOrientationListener;
    private int mPresentDisplayID;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WFDUibcManager.DEBUG) {
                Log.v(WFDUibcManager.TAG, "intent received " + action);
            }
            int name;
            if (action.equals("com.samsung.intent.action.SEC_PRESENTATION_START")) {
                name = intent.getIntExtra(Directory.DISPLAY_NAME, 111);
                WFDUibcManager.this.mPresentDisplayID = intent.getIntExtra("displayID", -1);
                if (WFDUibcManager.DEBUG) {
                    Log.i(WFDUibcManager.TAG, "Presentation Display: " + name + " id: " + WFDUibcManager.this.mPresentDisplayID);
                }
            } else if (action.equals("com.samsung.intent.action.SEC_PRESENTATION_STOP")) {
                name = intent.getIntExtra(Directory.DISPLAY_NAME, 111);
                if (WFDUibcManager.DEBUG) {
                    Log.i(WFDUibcManager.TAG, "Presentation Display: " + name + " id: " + WFDUibcManager.this.mPresentDisplayID);
                }
            }
        }
    };

    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$android$media$WFDUibcManager$UIBC_Mode = new int[UIBC_Mode.values().length];

        static {
            try {
                $SwitchMap$android$media$WFDUibcManager$UIBC_Mode[UIBC_Mode.CAMERA.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$media$WFDUibcManager$UIBC_Mode[UIBC_Mode.PRESENTATION.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$media$WFDUibcManager$UIBC_Mode[UIBC_Mode.FULL_SCREEN.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$media$WFDUibcManager$UIBC_Mode[UIBC_Mode.FORCE_VALUE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private class EventQueue {
        private BlockingQueue<InputEvent> queuedEvents;
        private BlockingQueue<Integer> rotateEvents;

        private EventQueue() {
            this.queuedEvents = new LinkedBlockingQueue();
            this.rotateEvents = new LinkedBlockingQueue();
        }

        public InputEvent getNextEvent() {
            try {
                InputEvent queuedEvent = (InputEvent) this.queuedEvents.poll(3, TimeUnit.MILLISECONDS);
                return queuedEvent != null ? queuedEvent : null;
            } catch (InterruptedException e) {
                Log.e(WFDUibcManager.TAG, "Interrupted when waiting to read from queue", e);
                return null;
            }
        }

        public Integer getRotateEvent() {
            Integer event = (Integer) this.rotateEvents.poll();
            return event != null ? event : null;
        }

        public void addEvent(InputEvent ev) {
            try {
                this.queuedEvents.put(ev);
            } catch (InterruptedException e) {
                Log.e(WFDUibcManager.TAG, "Interrupted when waiting to insert to queue", e);
            } catch (NullPointerException e2) {
                Log.e(WFDUibcManager.TAG, "Null pointer exception", e2);
            }
        }

        public void rotate(int radians, int fraction) {
            if (radians < 0) {
                fraction *= -1;
            }
            float degrees = ((((float) radians) + (((float) fraction) * 0.0039f)) * 180.0f) / 3.14f;
            if (WFDUibcManager.DEBUG) {
                Log.i(WFDUibcManager.TAG, "Rotate to " + degrees);
            }
            try {
                this.rotateEvents.put(Integer.valueOf(Math.round(degrees)));
            } catch (InterruptedException e) {
                Log.e(WFDUibcManager.TAG, "Interrupted when waiting to insert to queue", e);
            } catch (NullPointerException e2) {
                Log.e(WFDUibcManager.TAG, "Null pointer exception", e2);
            }
        }
    }

    class EventDispatcher extends EventQueue implements Runnable {
        private final int CAMERA_0 = 0;
        private final int CAMERA_180 = 2;
        private final int CAMERA_270 = 3;
        private final int CAMERA_90 = 1;
        private float Me_X;
        private float Me_Y;
        private UIBC_Mode Mode = UIBC_Mode.NORMAL;
        private PointerProperties[] aPointerProp = null;
        private PointerCoords[] aPtrCoords = null;
        private ActivityManager activityManager;
        private boolean bCap = false;
        private boolean bNum = false;
        private boolean bScroll = false;
        private boolean bSim = false;
        private Configuration configuration = null;
        private DisplayMetrics displayMetrics = null;
        private InputEvent ev = null;
        private boolean foregroundCamera = false;
        private float kH;
        private float kW;
        private float kX;
        private float kY;
        private KeyEvent ke = null;
        private KeyEvent last_Ke = null;
        private long mBroadTime = 0;
        private int mFlag = 0;
        private long mKeyDownTime = 0;
        private HashMap<Integer, Keyevnt_arrC> mKeyEvnt_Arr = new HashMap(15);
        private long mKeyUpTime = 0;
        private int mMetaFlag = 0;
        private int mRepeatCnt = -1;
        private long mTouchDownTime = 0;
        private MotionEvent me = null;
        private Integer rEv = null;
        public volatile boolean running = true;
        private float screenRatio;
        private float wfdRatio;
        private WindowManager wm = null;

        EventDispatcher() {
            super();
        }

        private void handleCameraTouch(int i) {
            int mCameraOrientation;
            int rotation = this.wm.getDefaultDisplay().getRotation();
            if (WFDUibcManager.DEBUG) {
                Log.i(WFDUibcManager.TAG, "rotation: " + rotation + ", mOrientation: " + WFDUibcManager.this.mOrientation);
            }
            if ((WFDUibcManager.this.mOrientation > 0 && WFDUibcManager.this.mOrientation < 45) || WFDUibcManager.this.mOrientation > 315) {
                mCameraOrientation = 0;
            } else if (WFDUibcManager.this.mOrientation >= 45 && WFDUibcManager.this.mOrientation < 115) {
                mCameraOrientation = 1;
            } else if (WFDUibcManager.this.mOrientation < 115 || WFDUibcManager.this.mOrientation >= 225) {
                mCameraOrientation = 3;
            } else {
                mCameraOrientation = 2;
            }
            mCameraOrientation = (mCameraOrientation + rotation) % 4;
            float newWidth;
            float normX;
            float pad;
            if (mCameraOrientation == 0 || mCameraOrientation == 2) {
                this.screenRatio = ((float) this.displayMetrics.widthPixels) / ((float) this.displayMetrics.heightPixels);
                this.wfdRatio = WFDUibcManager.this.mNegRs_X / WFDUibcManager.this.mNegRs_Y;
                if (WFDUibcManager.DEBUG) {
                    Log.v(WFDUibcManager.TAG, "screenRatio: " + this.screenRatio + " wfdRatio: " + this.wfdRatio);
                }
                if (this.screenRatio < this.wfdRatio) {
                    newWidth = (this.kW * WFDUibcManager.this.mNegRs_Y) / this.kH;
                    normX = (1.0E-4f * WFDUibcManager.this.mNegRs_X) * this.aPtrCoords[i].x;
                    this.Me_X = ((float) this.displayMetrics.widthPixels) * ((normX - ((WFDUibcManager.this.mNegRs_X - newWidth) / 2.0f)) / newWidth);
                    this.Me_Y = this.kY * this.aPtrCoords[i].y;
                } else if (this.screenRatio > this.wfdRatio) {
                    float newHeight = (this.kH * WFDUibcManager.this.mNegRs_X) / this.kW;
                    pad = (WFDUibcManager.this.mNegRs_Y - newHeight) / 2.0f;
                    float normY = (1.0E-4f * WFDUibcManager.this.mNegRs_Y) * this.aPtrCoords[i].y;
                    this.Me_X = this.kX * this.aPtrCoords[i].x;
                    this.Me_Y = ((float) this.displayMetrics.heightPixels) * ((normY - pad) / newHeight);
                } else {
                    this.Me_X = this.kX * this.aPtrCoords[i].x;
                    this.Me_Y = this.kY * this.aPtrCoords[i].y;
                }
                if (mCameraOrientation == 0) {
                    this.aPtrCoords[i].setAxisValue(0, this.Me_X);
                    this.aPtrCoords[i].setAxisValue(1, this.Me_Y);
                    if (WFDUibcManager.DEBUG) {
                        Log.i(WFDUibcManager.TAG, "Orientation is 0~45 or more than 315, X: " + this.Me_X + " Y: " + this.Me_Y);
                    }
                } else {
                    this.Me_X = ((float) this.displayMetrics.widthPixels) - this.Me_X;
                    this.Me_Y = ((float) this.displayMetrics.heightPixels) - this.Me_Y;
                    this.aPtrCoords[i].setAxisValue(0, this.Me_X);
                    this.aPtrCoords[i].setAxisValue(1, this.Me_Y);
                    if (WFDUibcManager.DEBUG) {
                        Log.i(WFDUibcManager.TAG, "Orientation is 225 ~ 315, X: " + this.Me_X + " Y: " + this.Me_Y);
                    }
                }
            } else if (mCameraOrientation == 1 || mCameraOrientation == 3) {
                newWidth = (this.kH * WFDUibcManager.this.mNegRs_Y) / this.kW;
                pad = (WFDUibcManager.this.mNegRs_X - newWidth) / 2.0f;
                normX = (1.0E-4f * WFDUibcManager.this.mNegRs_X) * this.aPtrCoords[i].x;
                if (mCameraOrientation == 1) {
                    this.Me_Y = ((float) this.displayMetrics.heightPixels) - (((float) this.displayMetrics.heightPixels) * ((normX - pad) / newWidth));
                    this.Me_X = this.kX * this.aPtrCoords[i].y;
                    if (WFDUibcManager.DEBUG) {
                        Log.i(WFDUibcManager.TAG, "Orientation is 45 ~ 114, X: " + this.Me_X + " Y: " + this.Me_Y);
                    }
                } else {
                    this.Me_Y = ((float) this.displayMetrics.heightPixels) * ((normX - pad) / newWidth);
                    this.Me_X = ((float) this.displayMetrics.widthPixels) - (this.kX * this.aPtrCoords[i].y);
                    if (WFDUibcManager.DEBUG) {
                        Log.i(WFDUibcManager.TAG, "Orientation is 115 ~ 224, X: " + this.Me_X + " Y: " + this.Me_Y);
                    }
                }
                this.aPtrCoords[i].setAxisValue(0, this.Me_X);
                this.aPtrCoords[i].setAxisValue(1, this.Me_Y);
            }
            if (WFDUibcManager.DEBUG) {
                Log.v(WFDUibcManager.TAG, "Executing Camera App mode is " + mCameraOrientation);
            }
        }

        private void handleMotionEvent() {
            MotionEvent newEv = (MotionEvent) this.ev;
            this.Mode = UIBC_Mode.NORMAL;
            this.wm.getDefaultDisplay().getRealMetrics(this.displayMetrics);
            if (WFDUibcManager.DEBUG) {
                Log.v(WFDUibcManager.TAG, "displayMetrics widthPixels: " + this.displayMetrics.widthPixels + " heightPixels: " + this.displayMetrics.heightPixels);
            }
            if (WFDUibcManager.DEBUG) {
                Log.v(WFDUibcManager.TAG, "mNegRs_X: " + WFDUibcManager.this.mNegRs_X + " mNegRs_Y: " + WFDUibcManager.this.mNegRs_Y);
            }
            int displayId = newEv.getDisplayId();
            if (WFDUibcManager.DEBUG) {
                Log.v(WFDUibcManager.TAG, "before motion displayId: " + displayId);
            }
            if (displayId < 0) {
                DisplayManagerGlobal dm = DisplayManagerGlobal.getInstance();
                for (int id : dm.getDisplayIds()) {
                    if (dm.getDisplayInfo(id).type == 3) {
                        displayId = id;
                        break;
                    }
                }
            }
            if (WFDUibcManager.DEBUG) {
                Log.v(WFDUibcManager.TAG, "after motion displayId: " + displayId);
            }
            if (displayId < 0) {
                Log.i(WFDUibcManager.TAG, "displayId not found");
                return;
            }
            int i;
            Context access$600 = WFDUibcManager.this.mContext;
            WFDUibcManager.this.mContext;
            this.activityManager = (ActivityManager) access$600.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningTaskInfo> info = this.activityManager.getRunningTasks(20);
            if (!info.isEmpty()) {
                this.foregroundCamera = ((RunningTaskInfo) info.get(0)).topActivity.getPackageName().equals("com.sec.android.app.camera");
                if (WFDUibcManager.DEBUG) {
                    Log.i(WFDUibcManager.TAG, "Is camera running: " + this.foregroundCamera);
                }
                if (this.foregroundCamera) {
                    this.Mode = UIBC_Mode.CAMERA;
                }
            }
            DisplayManager mDm = (DisplayManager) WFDUibcManager.this.mContext.getSystemService(Context.DISPLAY_SERVICE);
            if (System.getIntForUser(WFDUibcManager.this.mContext.getContentResolver(), "sidesync_source_connect", 0, -2) != 0 || WFDUibcManager.this.mCoupleShot) {
                this.Mode = UIBC_Mode.FORCE_VALUE;
                if (WFDUibcManager.DEBUG) {
                    Log.i(WFDUibcManager.TAG, "FORCE_VALUE will be set!");
                }
            } else if (mDm.isAuSLServiceRunning()) {
                this.Mode = UIBC_Mode.FULL_SCREEN;
                if (WFDUibcManager.DEBUG) {
                    Log.i(WFDUibcManager.TAG, "AuSLService Running setting FULL_SCREEN Mode!");
                }
            } else {
                Display[] presentationDisplays = mDm.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
                if (WFDUibcManager.DEBUG) {
                    Log.v(WFDUibcManager.TAG, "Received presentationDisplays amount: " + presentationDisplays.length);
                }
                Display[] allDisplays = mDm.getDisplays();
                if (WFDUibcManager.DEBUG) {
                    Log.v(WFDUibcManager.TAG, "Received alldisplays amount: " + allDisplays.length);
                }
                i = 0;
                while (i < presentationDisplays.length) {
                    Display display = presentationDisplays[i];
                    if (displayId == display.getDisplayId()) {
                        if (WFDUibcManager.DEBUG) {
                            Log.v(WFDUibcManager.TAG, "presentation display (" + displayId + ") found");
                        }
                        if (mDm.hasContent(WFDUibcManager.this.mPresentDisplayID) && displayId == WFDUibcManager.this.mPresentDisplayID) {
                            if (WFDUibcManager.DEBUG) {
                                Log.v(WFDUibcManager.TAG, "presentation( " + WFDUibcManager.this.mPresentDisplayID + " ) has content");
                            }
                            display.getRealMetrics(this.displayMetrics);
                            this.Mode = UIBC_Mode.PRESENTATION;
                        } else if (WFDUibcManager.DEBUG) {
                            Log.v(WFDUibcManager.TAG, "presentation( " + WFDUibcManager.this.mPresentDisplayID + " ) doesn't have content");
                        }
                    } else {
                        i++;
                    }
                }
            }
            if (newEv.getAction() == 8) {
                this.mTouchDownTime = SystemClock.uptimeMillis();
                if (WFDUibcManager.DEBUG) {
                    Log.i(WFDUibcManager.TAG, "ACTION SCROLL TIME : " + this.mTouchDownTime);
                }
                this.aPointerProp[0].clear();
                this.aPtrCoords[0].clear();
                newEv.getPointerProperties(0, this.aPointerProp[0]);
                newEv.getPointerCoords(0, this.aPtrCoords[0]);
                this.aPointerProp[0].id = 0;
                this.aPointerProp[0].toolType = 3;
                this.aPtrCoords[0].setAxisValue(9, this.aPtrCoords[0].y);
                this.aPtrCoords[0].setAxisValue(10, this.aPtrCoords[0].x);
                this.aPtrCoords[0].y = (float) (this.displayMetrics.heightPixels >> 1);
                this.aPtrCoords[0].x = (float) (this.displayMetrics.widthPixels >> 1);
                this.me = MotionEvent.obtain(this.mTouchDownTime - 10, this.mTouchDownTime - 10, 8, 1, this.aPointerProp, this.aPtrCoords, 0, 0, displayId, 1.0f, 1.0f, 10, 0, 8194, 0);
                this.me.setSource(8194);
                if (WFDUibcManager.DEBUG) {
                    Log.i(WFDUibcManager.TAG, "Scroll Inject: " + this.me);
                }
                InputManager.getInstance().injectInputEvent(this.me, 0);
                this.me = null;
                return;
            }
            this.kX = 1.0E-4f * ((float) this.displayMetrics.widthPixels);
            this.kY = 1.0E-4f * ((float) this.displayMetrics.heightPixels);
            this.kH = 1.0f * ((float) this.displayMetrics.heightPixels);
            this.kW = 1.0f * ((float) this.displayMetrics.widthPixels);
            this.Me_X = 1.0f;
            this.Me_Y = 1.0f;
            int count = newEv.getPointerCount();
            if (WFDUibcManager.DEBUG) {
                Log.v(WFDUibcManager.TAG, "UIBC mode: " + this.Mode);
            }
            for (i = 0; i < count; i++) {
                this.aPointerProp[i].clear();
                this.aPtrCoords[i].clear();
                newEv.getPointerProperties(i, this.aPointerProp[i]);
                newEv.getPointerCoords(i, this.aPtrCoords[i]);
                if (WFDUibcManager.DEBUG) {
                    Log.i(WFDUibcManager.TAG, "aPtrCoords[" + i + "].x: " + this.aPtrCoords[i].x + " aPtrCoords[" + i + "].y: " + this.aPtrCoords[i].y);
                }
                switch (AnonymousClass3.$SwitchMap$android$media$WFDUibcManager$UIBC_Mode[this.Mode.ordinal()]) {
                    case 1:
                        handleCameraTouch(i);
                        break;
                    case 2:
                        if (WFDUibcManager.DEBUG) {
                            Log.i(WFDUibcManager.TAG, "UIBC Mode is PRESENTATION");
                        }
                        this.Me_X = this.kX * this.aPtrCoords[i].x;
                        this.Me_Y = this.kY * this.aPtrCoords[i].y;
                        this.aPtrCoords[i].setAxisValue(0, this.Me_X);
                        this.aPtrCoords[i].setAxisValue(1, this.Me_Y);
                        break;
                    case 3:
                        int rotation = this.wm.getDefaultDisplay().getRotation();
                        if (WFDUibcManager.DEBUG) {
                            Log.i(WFDUibcManager.TAG, "UIBC Mode is Full Screen: rotation=" + rotation);
                        }
                        if (rotation == 0) {
                            this.Me_X = ((float) this.displayMetrics.widthPixels) - (this.kX * this.aPtrCoords[i].y);
                            this.Me_Y = this.kY * this.aPtrCoords[i].x;
                        } else if (rotation == 3) {
                            this.Me_X = ((float) this.displayMetrics.widthPixels) - (this.kX * this.aPtrCoords[i].x);
                            this.Me_Y = ((float) this.displayMetrics.heightPixels) - (this.kY * this.aPtrCoords[i].y);
                        } else if (rotation == 2) {
                            this.Me_X = this.kX * this.aPtrCoords[i].y;
                            this.Me_Y = ((float) this.displayMetrics.heightPixels) - (this.kY * this.aPtrCoords[i].x);
                        } else {
                            this.Me_X = this.kX * this.aPtrCoords[i].x;
                            this.Me_Y = this.kY * this.aPtrCoords[i].y;
                        }
                        this.aPtrCoords[i].setAxisValue(0, this.Me_X);
                        this.aPtrCoords[i].setAxisValue(1, this.Me_Y);
                        break;
                    case 4:
                        if (WFDUibcManager.DEBUG) {
                            Log.i(WFDUibcManager.TAG, "UIBC Mode is foreceValue");
                        }
                        this.Me_X = this.kX * this.aPtrCoords[i].x;
                        this.Me_Y = this.kY * this.aPtrCoords[i].y;
                        this.aPtrCoords[i].setAxisValue(0, this.Me_X);
                        this.aPtrCoords[i].setAxisValue(1, this.Me_Y);
                        break;
                    default:
                        this.configuration = WFDUibcManager.this.mContext.getResources().getConfiguration();
                        this.screenRatio = ((float) this.displayMetrics.widthPixels) / ((float) this.displayMetrics.heightPixels);
                        this.wfdRatio = WFDUibcManager.this.mNegRs_X / WFDUibcManager.this.mNegRs_Y;
                        if (WFDUibcManager.DEBUG) {
                            Log.i(WFDUibcManager.TAG, "UIBC Mode is default configuration: " + this.configuration);
                        }
                        if (WFDUibcManager.DEBUG) {
                            Log.v(WFDUibcManager.TAG, "screenRatio: " + this.screenRatio + " wfdRatio: " + this.wfdRatio);
                        }
                        float newWidth;
                        if (this.configuration.orientation != 2) {
                            if (this.screenRatio >= this.wfdRatio) {
                                if (Math.abs(this.screenRatio - this.wfdRatio) >= SensorManager.LIGHT_NO_MOON) {
                                    Log.i(WFDUibcManager.TAG, "Skip event, ratio wrong");
                                    break;
                                }
                                this.Me_X = this.kX * this.aPtrCoords[i].x;
                                this.Me_Y = this.kY * this.aPtrCoords[i].y;
                                this.aPtrCoords[i].setAxisValue(0, this.Me_X);
                                this.aPtrCoords[i].setAxisValue(1, this.Me_Y);
                                break;
                            }
                            newWidth = (this.kW * WFDUibcManager.this.mNegRs_Y) / this.kH;
                            this.Me_X = (((float) this.displayMetrics.widthPixels) * (((1.0E-4f * WFDUibcManager.this.mNegRs_X) * this.aPtrCoords[i].x) - ((WFDUibcManager.this.mNegRs_X - newWidth) / 2.0f))) / newWidth;
                            this.Me_Y = this.kY * this.aPtrCoords[i].y;
                            this.aPtrCoords[i].setAxisValue(0, this.Me_X);
                            this.aPtrCoords[i].setAxisValue(1, this.Me_Y);
                            break;
                        } else if (this.screenRatio >= this.wfdRatio) {
                            if (this.screenRatio <= this.wfdRatio) {
                                this.Me_X = this.kX * this.aPtrCoords[i].x;
                                this.Me_Y = this.kY * this.aPtrCoords[i].y;
                                this.aPtrCoords[i].setAxisValue(0, this.Me_X);
                                this.aPtrCoords[i].setAxisValue(1, this.Me_Y);
                                break;
                            }
                            float newHeight = (this.kH * WFDUibcManager.this.mNegRs_X) / this.kW;
                            float pad = (WFDUibcManager.this.mNegRs_Y - newHeight) / 2.0f;
                            float normY = (1.0E-4f * WFDUibcManager.this.mNegRs_Y) * this.aPtrCoords[i].y;
                            this.Me_X = this.kX * this.aPtrCoords[i].x;
                            this.Me_Y = ((float) this.displayMetrics.heightPixels) * ((normY - pad) / newHeight);
                            this.aPtrCoords[i].setAxisValue(0, this.Me_X);
                            this.aPtrCoords[i].setAxisValue(1, this.Me_Y);
                            break;
                        } else {
                            newWidth = (this.kW * WFDUibcManager.this.mNegRs_Y) / this.kH;
                            this.Me_X = ((float) this.displayMetrics.widthPixels) * ((((1.0E-4f * WFDUibcManager.this.mNegRs_X) * this.aPtrCoords[i].x) - ((WFDUibcManager.this.mNegRs_X - newWidth) / 2.0f)) / newWidth);
                            this.Me_Y = this.kY * this.aPtrCoords[i].y;
                            this.aPtrCoords[i].setAxisValue(0, this.Me_X);
                            this.aPtrCoords[i].setAxisValue(1, this.Me_Y);
                            break;
                        }
                }
                if (WFDUibcManager.DEBUG) {
                    Log.i(WFDUibcManager.TAG, "Value Me_X=" + this.Me_X + " Me_Y=" + this.Me_Y);
                }
            }
            if (WFDUibcManager.DEBUG) {
                Log.i(WFDUibcManager.TAG, "Device id : " + newEv.getDeviceId());
            }
            if (newEv.getAction() == 0) {
                this.mTouchDownTime = SystemClock.uptimeMillis();
            }
            this.me = MotionEvent.obtain(this.mTouchDownTime, SystemClock.uptimeMillis(), newEv.getAction(), newEv.getPointerCount(), this.aPointerProp, this.aPtrCoords, 0, 0, displayId, 1.0f, 1.0f, newEv.getDeviceId(), 0, newEv.getSource(), 0);
            if (WFDUibcManager.DEBUG) {
                Log.i(WFDUibcManager.TAG, "InjectTouchEvent: " + this.me);
            }
            InputManager.getInstance().injectInputEvent(this.me, 0);
            this.me = null;
            if (this.Mode != UIBC_Mode.FORCE_VALUE && this.Mode != UIBC_Mode.PRESENTATION) {
                long mlTempTime = SystemClock.uptimeMillis();
                if (WFDUibcManager.DEBUG) {
                    Log.v(WFDUibcManager.TAG, "TimeGap: " + (mlTempTime - this.mBroadTime));
                }
                if (mlTempTime - this.mBroadTime > 1000) {
                    if (WFDUibcManager.DEBUG) {
                        Log.v(WFDUibcManager.TAG, "Send intent (android.intent.action.SEC_UIBC_GET_FOCUS)");
                    }
                    Intent intent = new Intent("android.intent.action.SEC_UIBC_GET_FOCUS");
                    intent.putExtra("UIBC_X", (int) this.Me_X);
                    intent.putExtra("UIBC_Y", (int) this.Me_Y);
                    intent.putExtra("DISPLAY_ID", displayId);
                    WFDUibcManager.this.mContext.sendBroadcast(intent);
                    this.mBroadTime = mlTempTime;
                }
            }
        }

        private void getMetaFlag(int keycode, boolean isDown) {
            if (isDown) {
                if (keycode == 59 || keycode == 60) {
                    this.mMetaFlag |= 1;
                    if (keycode == 59) {
                        this.mMetaFlag |= 64;
                    }
                    if (keycode == 60) {
                        this.mMetaFlag |= 128;
                    }
                }
                if (keycode == 57 || keycode == 58) {
                    this.mMetaFlag |= 2;
                    if (keycode == 57) {
                        this.mMetaFlag |= 16;
                    }
                    if (keycode == 58) {
                        this.mMetaFlag |= 32;
                    }
                }
                if (keycode == 113 || keycode == 114) {
                    this.mMetaFlag |= 4096;
                    if (keycode == 113) {
                        this.mMetaFlag |= 8192;
                    }
                    if (keycode == 114) {
                        this.mMetaFlag |= 16384;
                    }
                }
                if (keycode == 119) {
                    this.mMetaFlag |= 8;
                    return;
                }
                return;
            }
            if (keycode == 59 || keycode == 60) {
                this.mMetaFlag &= -2;
                if (keycode == 59) {
                    this.mMetaFlag &= -65;
                }
                if (keycode == 60) {
                    this.mMetaFlag &= -129;
                }
            }
            if (keycode == 57 || keycode == 58) {
                this.mMetaFlag &= -3;
                if (keycode == 57) {
                    this.mMetaFlag &= -17;
                }
                if (keycode == 58) {
                    this.mMetaFlag &= -33;
                }
            }
            if (keycode == 113 || keycode == 114) {
                this.mMetaFlag &= -4097;
                if (keycode == 113) {
                    this.mMetaFlag &= -8193;
                }
                if (keycode == 114) {
                    this.mMetaFlag &= -16385;
                }
            }
            if (keycode == 119) {
                this.mMetaFlag &= -9;
            }
            if (keycode != 115) {
                return;
            }
            if (this.bCap) {
                this.mMetaFlag &= -1048577;
                this.bCap = false;
                return;
            }
            this.mMetaFlag |= 1048576;
            this.bCap = true;
        }

        private void handleKeyEvent() {
            KeyEvent NewKE = (KeyEvent) this.ev;
            this.mFlag = 0;
            this.mKeyDownTime = 0;
            this.mRepeatCnt = 0;
            if (WFDUibcManager.DEBUG) {
                Log.i(WFDUibcManager.TAG, "Key Device id : " + this.ev.getDeviceId());
            }
            int displayId = NewKE.getDisplayId();
            if (WFDUibcManager.DEBUG) {
                Log.v(WFDUibcManager.TAG, "Before Key display id : " + displayId);
            }
            if (displayId < 0) {
                DisplayManagerGlobal dm = DisplayManagerGlobal.getInstance();
                for (int id : dm.getDisplayIds()) {
                    if (dm.getDisplayInfo(id).type == 3) {
                        displayId = id;
                        break;
                    }
                }
            }
            if (WFDUibcManager.DEBUG) {
                Log.v(WFDUibcManager.TAG, "After Key Display id : " + displayId);
            }
            if (!WFDUibcManager.this.mCoupleShot || NewKE.getKeyCode() != 4) {
                Keyevnt_arrC kev;
                if (NewKE.getAction() == 0) {
                    if (WFDUibcManager.DEBUG) {
                        Log.i(WFDUibcManager.TAG, "KeyEvent ACTION_DOWN");
                    }
                    if (this.mKeyEvnt_Arr.containsKey(Integer.valueOf(NewKE.getKeyCode()))) {
                        kev = (Keyevnt_arrC) this.mKeyEvnt_Arr.get(Integer.valueOf(NewKE.getKeyCode()));
                        kev.mKyFlag |= NewKE.getFlags();
                        kev.mRepCnt++;
                        if (kev.mRepCnt >= 1) {
                            kev.mKyFlag |= 128;
                        }
                        this.mKeyDownTime = kev.mKyDwnTime;
                        this.mFlag |= kev.mKyFlag;
                        this.mRepeatCnt = kev.mRepCnt;
                        if (WFDUibcManager.DEBUG) {
                            Log.v(WFDUibcManager.TAG, "Long press detected");
                        }
                    } else {
                        if (this.mKeyEvnt_Arr.size() < 15) {
                            Keyevnt_arrC keyevnt_arrC = new Keyevnt_arrC();
                            keyevnt_arrC.mKyDwnTime = NewKE.getDownTime();
                            keyevnt_arrC.mKyFlag = 0;
                            keyevnt_arrC.mRepCnt = 0;
                            this.mKeyEvnt_Arr.put(Integer.valueOf(NewKE.getKeyCode()), keyevnt_arrC);
                        }
                        this.mKeyDownTime = NewKE.getDownTime();
                        this.mFlag |= NewKE.getFlags();
                        this.mRepeatCnt = NewKE.getRepeatCount();
                    }
                    getMetaFlag(NewKE.getKeyCode(), true);
                } else if (NewKE.getAction() == 1) {
                    if (WFDUibcManager.DEBUG) {
                        Log.i(WFDUibcManager.TAG, "KeyEvent ACTION_UP");
                    }
                    if (this.mKeyEvnt_Arr.containsKey(Integer.valueOf(NewKE.getKeyCode()))) {
                        kev = (Keyevnt_arrC) this.mKeyEvnt_Arr.get(Integer.valueOf(NewKE.getKeyCode()));
                        this.mKeyDownTime = kev.mKyDwnTime;
                        this.mFlag |= kev.mKyFlag;
                        this.mRepeatCnt = 0;
                        this.mKeyEvnt_Arr.remove(Integer.valueOf(NewKE.getKeyCode()));
                    } else {
                        this.mKeyDownTime = NewKE.getDownTime();
                        this.mFlag |= NewKE.getFlags();
                        this.mRepeatCnt = NewKE.getRepeatCount();
                    }
                    getMetaFlag(NewKE.getKeyCode(), false);
                }
                if (WFDUibcManager.DEBUG) {
                    Log.i(WFDUibcManager.TAG, "KeyCode: " + NewKE.getKeyCode() + " DownTime: " + this.mKeyDownTime + " Flag: " + this.mFlag + " RepeatCnt: " + this.mRepeatCnt + "mMetaFlag: " + this.mMetaFlag);
                }
                int metakey = NewKE.getMetaState();
                if (metakey == 0) {
                    metakey = this.mMetaFlag;
                }
                this.ke = KeyEvent.obtain(this.mKeyDownTime, NewKE.getEventTime(), NewKE.getAction(), NewKE.getKeyCode(), this.mRepeatCnt, metakey, NewKE.getDeviceId(), NewKE.getScanCode(), this.mFlag, displayId, NewKE.getSource(), NewKE.getCharacters());
                if (this.ke.getAction() == 0) {
                    this.last_Ke = this.ke;
                }
                InputManager.getInstance().injectInputEvent(this.ke, 0);
                if (WFDUibcManager.DEBUG) {
                    Log.i(WFDUibcManager.TAG, "InjectKeyEvent: " + this.ke);
                }
                if (this.ke.getAction() == 1 && (this.ke.getKeyCode() == 3 || this.ke.getKeyCode() == 4)) {
                    if (WFDUibcManager.DEBUG) {
                        Log.i(WFDUibcManager.TAG, "send broadcast : Terminate External Display");
                    }
                    Intent intent = new Intent("com.sec.android.app.PRESENTATION_FOCUS_CHANGED");
                    intent.putExtra("app_name", "finish");
                    if (this.ke.getKeyCode() == 3) {
                        intent.putExtra("launch_home", true);
                    } else if (this.ke.getKeyCode() == 4) {
                        intent.putExtra("launch_home", false);
                    } else if (WFDUibcManager.DEBUG) {
                        Log.i(WFDUibcManager.TAG, "putExtra : no parameter");
                    }
                    WFDUibcManager.this.mContext.sendBroadcast(intent);
                }
                this.ke = null;
            } else if (WFDUibcManager.DEBUG) {
                Log.i(WFDUibcManager.TAG, "Ignore Back key event");
            }
        }

        public void run() {
            this.aPointerProp = new PointerProperties[10];
            this.aPtrCoords = new PointerCoords[10];
            for (int i = 0; i < 10; i++) {
                this.aPointerProp[i] = new PointerProperties();
                this.aPtrCoords[i] = new PointerCoords();
            }
            this.displayMetrics = new DisplayMetrics();
            this.wm = (WindowManager) WFDUibcManager.this.mContext.getSystemService(Context.WINDOW_SERVICE);
            while (this.running) {
                this.ev = getNextEvent();
                this.rEv = getRotateEvent();
                if (this.ev != null || this.rEv != null) {
                    if (WFDUibcManager.DEBUG) {
                        Log.i(WFDUibcManager.TAG, "RecvdEvent: " + this.ev);
                    }
                    if (WFDUibcManager.DEBUG) {
                        Log.i(WFDUibcManager.TAG, "RecvdEvent: " + this.rEv);
                    }
                    if (this.ev instanceof MotionEvent) {
                        handleMotionEvent();
                    } else if (this.ev instanceof KeyEvent) {
                        handleKeyEvent();
                    }
                    if (this.rEv != null) {
                        int accel = System.getInt(WFDUibcManager.this.mContext.getContentResolver(), "accelerometer_rotation", 0);
                        System.putInt(WFDUibcManager.this.mContext.getContentResolver(), "accelerometer_rotation", 0);
                        int rotation = System.getInt(WFDUibcManager.this.mContext.getContentResolver(), "user_rotation", 0);
                        if (WFDUibcManager.DEBUG) {
                            Log.i(WFDUibcManager.TAG, "Current rotation " + rotation);
                        }
                        if (((DisplayManager) WFDUibcManager.this.mContext.getSystemService(Context.DISPLAY_SERVICE)).isAuSLServiceRunning()) {
                            if (WFDUibcManager.DEBUG) {
                                Log.v(WFDUibcManager.TAG, "Setting SPECIAL rotation!");
                            }
                            this.rEv = this.rEv;
                        } else {
                            this.rEv = Integer.valueOf((rotation * 90) + this.rEv.intValue());
                        }
                        this.rEv = Integer.valueOf(this.rEv.intValue() % 360);
                        if (this.rEv.intValue() < 0) {
                            this.rEv = Integer.valueOf(this.rEv.intValue() + 360);
                        }
                        if ((this.rEv.intValue() > 315 && this.rEv.intValue() <= 360) || (this.rEv.intValue() >= 0 && this.rEv.intValue() <= 45)) {
                            rotation = 0;
                        }
                        if (this.rEv.intValue() > 45 && this.rEv.intValue() <= 135) {
                            rotation = 1;
                        }
                        if (this.rEv.intValue() > 135 && this.rEv.intValue() <= 225) {
                            rotation = 2;
                        }
                        if (this.rEv.intValue() > 225 && this.rEv.intValue() <= 315) {
                            rotation = 3;
                        }
                        if (WFDUibcManager.DEBUG) {
                            Log.v(WFDUibcManager.TAG, "Rotation set to " + rotation);
                        }
                        System.putInt(WFDUibcManager.this.mContext.getContentResolver(), "user_rotation", rotation);
                        System.putInt(WFDUibcManager.this.mContext.getContentResolver(), "accelerometer_rotation", accel);
                    }
                }
            }
        }
    }

    private class Keyevnt_arrC {
        public long mKyDwnTime;
        public int mKyFlag;
        public int mRepCnt;

        private Keyevnt_arrC() {
        }
    }

    private enum UIBC_Mode {
        NORMAL,
        FORCE_VALUE,
        CAMERA,
        FULL_SCREEN,
        PRESENTATION
    }

    static {
        boolean z;
        if ("eng".equals(Build.TYPE)) {
            z = true;
        } else {
            z = false;
        }
        DEBUG = z;
    }

    public WFDUibcManager(Context context) {
        this.mContext = context;
        this.mIntentFilter.addAction("com.samsung.intent.action.SEC_PRESENTATION_STOP");
        this.mIntentFilter.addAction("com.samsung.intent.action.SEC_PRESENTATION_START");
        this.mContext.registerReceiver(this.mReceiver, this.mIntentFilter);
        Log.v(TAG, TAG);
        this.mOrientationListener = new OrientationEventListener(this.mContext) {
            public void onOrientationChanged(int orientation) {
                if (orientation != -1) {
                    WFDUibcManager.this.mOrientation = orientation;
                }
            }
        };
    }

    public boolean start(int mSessionId) {
        this.mEventDispatcher = new EventDispatcher();
        this.mEventDispatcherThread = new Thread(this.mEventDispatcher);
        this.mEventDispatcherThread.start();
        this.mOrientationListener.enable();
        Log.v(TAG, "Uibc Manager start with sessionId");
        return true;
    }

    public boolean stop(int mSessionId) {
        this.mOrientationListener.disable();
        if (this.mEventDispatcher != null) {
            this.mEventDispatcher.running = false;
            Log.v(TAG, "Going to stop Uibc manager");
            try {
                this.mEventDispatcherThread.join();
            } catch (InterruptedException e) {
                Log.e(TAG, "Error joining event dispatcher thread", e);
            }
            Log.v(TAG, "Uibc manager stopped");
            this.mEventDispatcher = null;
            this.mEventDispatcherThread = null;
        }
        return true;
    }

    public boolean start() {
        this.mEventDispatcher = new EventDispatcher();
        this.mEventDispatcherThread = new Thread(this.mEventDispatcher);
        RemoteDisplay.nativeStartUIBC(this.mEventDispatcher);
        this.mEventDispatcherThread.start();
        this.mOrientationListener.enable();
        Log.v(TAG, "Uibc Manager started");
        return true;
    }

    public boolean stop() {
        if (((DisplayManager) this.mContext.getSystemService(Context.DISPLAY_SERVICE)).isAuSLServiceRunning() && System.getInt(this.mContext.getContentResolver(), "accelerometer_rotation", 0) == 0) {
            System.putInt(this.mContext.getContentResolver(), "user_rotation", 0);
        }
        if (this.mEventDispatcher != null) {
            this.mEventDispatcher.running = false;
            RemoteDisplay.nativeStopUIBC();
            Log.v(TAG, "Going to stop Uibc manager");
            try {
                this.mEventDispatcherThread.join();
            } catch (InterruptedException e) {
                Log.e(TAG, "Error joining event dispatcher thread", e);
            }
            Log.v(TAG, "Uibc manager stopped");
            this.mEventDispatcher = null;
            this.mEventDispatcherThread = null;
        }
        return true;
    }

    public void setUIBCNegotiagedResolution(float negRs_X, float negRs_Y) {
        this.mNegRs_X = negRs_X;
        this.mNegRs_Y = negRs_Y;
    }

    public void setCoupleShotMode(boolean isCoupleShot) {
        this.mCoupleShot = isCoupleShot;
    }
}
