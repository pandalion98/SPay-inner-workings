package com.sec.android.app.hwmoduletest;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.FactoryTest;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings.System;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import com.goodix.cap.fingerprint.Constants;
import com.samsung.android.sdk.dualscreen.SDualScreenActivity;
import com.samsung.android.sdk.dualscreen.SDualScreenActivity.DualScreen;
import com.sec.android.app.hwmoduletest.modules.ModuleDevice;
import com.sec.android.app.hwmoduletest.modules.ModulePower;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.android.app.hwmoduletest.support.LtUtil.Sleep;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.HwTestMenu;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Properties;
import com.sec.xmldata.support.Support.Values;
import com.sec.xmldata.support.XMLDataStorage;
import egis.client.api.EgisFingerprint;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

public class HwModuleTest extends BaseActivity implements OnClickListener {
    private static final String ACTION_OQCSBFTT = "com.sec.android.app.hwmoduletest.OQCSBFTT";
    private static final String ACTION_OQCSBFTT_READ_DATA = "com.sec.factory.OQCSBFTT.READ_DATA";
    private static final String ACTION_OQCSBFTT_RESPONSE = "com.sec.factory.OQCSBFTT.RESPONSE";
    private static final int BOTH = 3;
    private static final int DOWN = 4;
    public static final byte ID_2ND_LCD = 33;
    public static final byte ID_BARCODE_EMUL = 22;
    public static final byte ID_BLACK = 24;
    public static final byte ID_BLUE = 2;
    public static final byte ID_DIMMING = 3;
    public static final byte ID_FRONT_CAM = 6;
    public static final byte ID_GREEN = 1;
    public static final byte ID_GRIP = 15;
    public static final byte ID_GRIP_TOUCH = 44;
    public static final byte ID_HALL_IC = 25;
    public static final byte ID_HDMI = 18;
    public static final byte ID_IRIS_CAM = 41;
    public static final byte ID_IRIS_ENROLL = 43;
    public static final byte ID_IRIS_LED = 42;
    public static final byte ID_IR_LED = 17;
    public static final byte ID_LED = 13;
    public static final byte ID_LOOPBACK = 45;
    public static final byte ID_LOW_FREQUENCY = 19;
    public static final byte ID_MEGA_CAM = 5;
    public static final byte ID_MLC = 31;
    public static final byte ID_MOOD_LED = 36;
    public static final byte ID_MST_TEST = 34;
    public static final byte ID_RECEIVER = 7;
    public static final byte ID_RED = 0;
    public static final byte ID_SENSOR = 9;
    public static final byte ID_SENSORHUB = 23;
    public static final byte ID_SLEEP = 11;
    public static final byte ID_SPEAKER = 8;
    public static final byte ID_SPEAKER_R = 16;
    public static final byte ID_SPEN = 35;
    public static final byte ID_SPEN_HOVERING = 29;
    public static final byte ID_SUB_KEY = 12;
    public static final byte ID_SVC_LED = 20;
    public static final byte ID_TOF_CAM = 47;
    public static final byte ID_TOUCH = 4;
    public static final byte ID_TOUCHSELF = 104;
    public static final byte ID_TOUCH_2 = 27;
    public static final byte ID_TSP_DATA = 21;
    public static final byte ID_TSP_HOVERING = 26;
    public static final byte ID_VERSION = 46;
    public static final byte ID_VIBRATION = 10;
    public static final byte ID_VIBRATION_WITH_HALLIC = 40;
    public static final byte ID_WACOM = 14;
    private static final String KEY_RUNNING_FTCLIENT = "sys.factory.runningFtClient";
    private static final int LEFT = 2;
    private static final int RIGHT = 1;
    private static final int SHOW_AFC_TOAST_MSG = 1002;
    private static final int START_SAVE_TSP_DATA = 1001;

    /* renamed from: UP */
    private static final int f15UP = 5;
    protected static ModulePower mModulePower = null;
    public static int preVolume = 0;
    private final long BACK_KEY_EVENT_TIMELAG = 2000;
    boolean PassSavingInspection;
    boolean PassSavingReference;
    private String TSPFile;
    private final String TSPFilePath = "mnt/extSdCard/";
    private AlertDialog TspNotiDialog = null;
    private int mAccRotationValue;
    private AlertDialog mAlertDialog;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Code restructure failed: missing block: B:67:0x01ca, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized void onReceive(android.content.Context r9, android.content.Intent r10) {
            /*
                r8 = this;
                monitor-enter(r8)
                java.lang.String r0 = r10.getAction()     // Catch:{ all -> 0x01cb }
                com.sec.android.app.hwmoduletest.HwModuleTest r1 = com.sec.android.app.hwmoduletest.HwModuleTest.this     // Catch:{ all -> 0x01cb }
                java.lang.String r1 = r1.CLASS_NAME     // Catch:{ all -> 0x01cb }
                java.lang.String r2 = "mBroadcastReceiver"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x01cb }
                r3.<init>()     // Catch:{ all -> 0x01cb }
                java.lang.String r4 = "action= = "
                r3.append(r4)     // Catch:{ all -> 0x01cb }
                r3.append(r0)     // Catch:{ all -> 0x01cb }
                java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x01cb }
                com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r1, r2, r3)     // Catch:{ all -> 0x01cb }
                java.lang.String r1 = "android.intent.action.BATTERY_CHANGED"
                boolean r1 = r1.equals(r0)     // Catch:{ all -> 0x01cb }
                r2 = 0
                if (r1 == 0) goto L_0x00f7
                java.lang.String r1 = "plugged"
                int r1 = r10.getIntExtra(r1, r2)     // Catch:{ all -> 0x01cb }
                r3 = r9
                android.app.Activity r3 = (android.app.Activity) r3     // Catch:{ all -> 0x01cb }
                boolean r3 = r3.isResumed()     // Catch:{ all -> 0x01cb }
                if (r3 != 0) goto L_0x0048
                com.sec.android.app.hwmoduletest.HwModuleTest r2 = com.sec.android.app.hwmoduletest.HwModuleTest.this     // Catch:{ all -> 0x01cb }
                java.lang.String r2 = r2.CLASS_NAME     // Catch:{ all -> 0x01cb }
                java.lang.String r3 = "mBroadcastReceiver"
                java.lang.String r4 = "Activity is not resumed"
                com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r2, r3, r4)     // Catch:{ all -> 0x01cb }
                monitor-exit(r8)
                return
            L_0x0048:
                java.lang.String r3 = "SUPPORT_AFCHARGE_TOAST_TEST"
                boolean r3 = com.sec.xmldata.support.Support.Feature.getBoolean(r3, r2)     // Catch:{ all -> 0x01cb }
                if (r3 == 0) goto L_0x009e
                if (r1 != 0) goto L_0x0057
                com.sec.android.app.hwmoduletest.HwModuleTest r3 = com.sec.android.app.hwmoduletest.HwModuleTest.this     // Catch:{ all -> 0x01cb }
                r3.mIsShowingToastAFC = r2     // Catch:{ all -> 0x01cb }
            L_0x0057:
                com.sec.android.app.hwmoduletest.HwModuleTest r3 = com.sec.android.app.hwmoduletest.HwModuleTest.this     // Catch:{ all -> 0x01cb }
                boolean r3 = r3.mIsStartAFC     // Catch:{ all -> 0x01cb }
                if (r3 != 0) goto L_0x008f
                com.sec.android.app.hwmoduletest.HwModuleTest r3 = com.sec.android.app.hwmoduletest.HwModuleTest.this     // Catch:{ all -> 0x01cb }
                boolean r3 = r3.mIsShowingToastAFC     // Catch:{ all -> 0x01cb }
                if (r3 == 0) goto L_0x0068
                goto L_0x008f
            L_0x0068:
                r3 = 1
                if (r1 == r3) goto L_0x007c
                r4 = 4
                if (r1 != r4) goto L_0x009e
                java.lang.String r4 = "1"
                java.lang.String r5 = "WIRELESS_BATTERY"
                java.lang.String r5 = com.sec.xmldata.support.Support.Kernel.read(r5)     // Catch:{ all -> 0x01cb }
                boolean r4 = r4.equals(r5)     // Catch:{ all -> 0x01cb }
                if (r4 == 0) goto L_0x009e
            L_0x007c:
                com.sec.android.app.hwmoduletest.HwModuleTest r4 = com.sec.android.app.hwmoduletest.HwModuleTest.this     // Catch:{ all -> 0x01cb }
                r4.mIsStartAFC = r3     // Catch:{ all -> 0x01cb }
                java.lang.Thread r3 = new java.lang.Thread     // Catch:{ all -> 0x01cb }
                com.sec.android.app.hwmoduletest.HwModuleTest$1$1 r4 = new com.sec.android.app.hwmoduletest.HwModuleTest$1$1     // Catch:{ all -> 0x01cb }
                r4.<init>(r1)     // Catch:{ all -> 0x01cb }
                r3.<init>(r4)     // Catch:{ all -> 0x01cb }
                r3.start()     // Catch:{ all -> 0x01cb }
                goto L_0x009e
            L_0x008f:
                com.sec.android.app.hwmoduletest.HwModuleTest r2 = com.sec.android.app.hwmoduletest.HwModuleTest.this     // Catch:{ all -> 0x01cb }
                java.lang.String r2 = r2.CLASS_NAME     // Catch:{ all -> 0x01cb }
                java.lang.String r3 = "mBroadcastReceiver"
                java.lang.String r4 = "Already start to check AFC"
                com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r2, r3, r4)     // Catch:{ all -> 0x01cb }
                monitor-exit(r8)
                return
            L_0x009e:
                java.lang.String r3 = "factory"
                java.lang.String r4 = "BINARY_TYPE"
                java.lang.String r4 = com.sec.xmldata.support.Support.Properties.get(r4)     // Catch:{ all -> 0x01cb }
                boolean r3 = r3.equalsIgnoreCase(r4)     // Catch:{ all -> 0x01cb }
                if (r3 != 0) goto L_0x00f5
                java.lang.String r3 = "INBATT_SAVE_SOC"
                boolean r2 = com.sec.xmldata.support.Support.Feature.getBoolean(r3, r2)     // Catch:{ all -> 0x01cb }
                if (r2 == 0) goto L_0x00f5
                java.lang.String r2 = "JIG_CONNECTION_CHECK"
                java.lang.String r2 = com.sec.xmldata.support.Support.Kernel.read(r2)     // Catch:{ all -> 0x01cb }
                if (r2 == 0) goto L_0x00f5
                java.lang.String r3 = "JIG"
                boolean r3 = r2.contains(r3)     // Catch:{ all -> 0x01cb }
                if (r3 != 0) goto L_0x00f5
                java.lang.String r3 = "level"
                r4 = -1
                int r3 = r10.getIntExtra(r3, r4)     // Catch:{ all -> 0x01cb }
                com.sec.android.app.hwmoduletest.HwModuleTest r4 = com.sec.android.app.hwmoduletest.HwModuleTest.this     // Catch:{ all -> 0x01cb }
                java.lang.String r4 = r4.CLASS_NAME     // Catch:{ all -> 0x01cb }
                java.lang.String r5 = "mBroadcastReceiver"
                java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x01cb }
                r6.<init>()     // Catch:{ all -> 0x01cb }
                java.lang.String r7 = "battLevel = "
                r6.append(r7)     // Catch:{ all -> 0x01cb }
                r6.append(r3)     // Catch:{ all -> 0x01cb }
                java.lang.String r7 = "%"
                r6.append(r7)     // Catch:{ all -> 0x01cb }
                java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x01cb }
                com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r4, r5, r6)     // Catch:{ all -> 0x01cb }
                com.sec.android.app.hwmoduletest.HwModuleTest r4 = com.sec.android.app.hwmoduletest.HwModuleTest.this     // Catch:{ all -> 0x01cb }
                java.lang.String r5 = java.lang.Integer.toString(r3)     // Catch:{ all -> 0x01cb }
                r4.writeSOC(r5)     // Catch:{ all -> 0x01cb }
            L_0x00f5:
                goto L_0x01c9
            L_0x00f7:
                java.lang.String r1 = "com.sec.android.app.hwmoduletest.OQCSBFTT"
                boolean r1 = r1.equals(r0)     // Catch:{ all -> 0x01cb }
                if (r1 == 0) goto L_0x01ae
                java.lang.String r1 = "start"
                boolean r1 = r10.getBooleanExtra(r1, r2)     // Catch:{ all -> 0x01cb }
                com.sec.android.app.hwmoduletest.support.BaseActivity.isOqcsbftt = r1     // Catch:{ all -> 0x01cb }
                com.sec.android.app.hwmoduletest.HwModuleTest r1 = com.sec.android.app.hwmoduletest.HwModuleTest.this     // Catch:{ all -> 0x01cb }
                java.lang.String r1 = r1.CLASS_NAME     // Catch:{ all -> 0x01cb }
                java.lang.String r2 = "mBroadcastReceiver"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x01cb }
                r3.<init>()     // Catch:{ all -> 0x01cb }
                java.lang.String r4 = "start = "
                r3.append(r4)     // Catch:{ all -> 0x01cb }
                boolean r4 = com.sec.android.app.hwmoduletest.support.BaseActivity.isOqcsbftt     // Catch:{ all -> 0x01cb }
                r3.append(r4)     // Catch:{ all -> 0x01cb }
                java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x01cb }
                com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r1, r2, r3)     // Catch:{ all -> 0x01cb }
                java.lang.String r1 = "SUPPORT_EPEN"
                boolean r1 = com.sec.xmldata.support.Support.Feature.getBoolean(r1)     // Catch:{ all -> 0x01cb }
                if (r1 == 0) goto L_0x0140
                boolean r1 = com.sec.android.app.hwmoduletest.support.BaseActivity.isOqcsbftt     // Catch:{ all -> 0x01cb }
                if (r1 == 0) goto L_0x0139
                java.lang.String r1 = "EPEN_SAVINGMODE"
                java.lang.String r2 = "0"
                com.sec.xmldata.support.Support.Kernel.write(r1, r2)     // Catch:{ all -> 0x01cb }
                goto L_0x0140
            L_0x0139:
                java.lang.String r1 = "EPEN_SAVINGMODE"
                java.lang.String r2 = "1"
                com.sec.xmldata.support.Support.Kernel.write(r1, r2)     // Catch:{ all -> 0x01cb }
            L_0x0140:
                boolean r1 = com.sec.android.app.hwmoduletest.support.BaseActivity.isOqcsbftt     // Catch:{ all -> 0x01cb }
                if (r1 == 0) goto L_0x01a1
                java.lang.String r1 = "Qualcomm"
                java.lang.String r2 = "CHIPSET_MANUFACTURE"
                java.lang.String r2 = com.sec.xmldata.support.Support.Feature.getString(r2)     // Catch:{ all -> 0x01cb }
                boolean r1 = r1.equals(r2)     // Catch:{ all -> 0x01cb }
                if (r1 == 0) goto L_0x01a1
                int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x01cb }
                r2 = 26
                if (r1 > r2) goto L_0x017d
                java.lang.String r1 = "running"
                java.lang.String r2 = "init.svc.diag_uart_log"
                java.lang.String r3 = "Unknown"
                java.lang.String r2 = android.os.SystemProperties.get(r2, r3)     // Catch:{ all -> 0x01cb }
                boolean r1 = r1.equals(r2)     // Catch:{ all -> 0x01cb }
                if (r1 != 0) goto L_0x01a1
                com.sec.android.app.hwmoduletest.HwModuleTest r1 = com.sec.android.app.hwmoduletest.HwModuleTest.this     // Catch:{ all -> 0x01cb }
                java.lang.String r1 = r1.CLASS_NAME     // Catch:{ all -> 0x01cb }
                java.lang.String r2 = "mBroadcastReceiver"
                java.lang.String r3 = "start diag_uart_log"
                com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r1, r2, r3)     // Catch:{ all -> 0x01cb }
                java.lang.String r1 = "ctl.start"
                java.lang.String r2 = "diag_uart_log"
                android.os.SystemProperties.set(r1, r2)     // Catch:{ all -> 0x01cb }
                goto L_0x01a1
            L_0x017d:
                java.lang.String r1 = "running"
                java.lang.String r2 = "init.svc.sec_diag_uart_log"
                java.lang.String r3 = "Unknown"
                java.lang.String r2 = android.os.SystemProperties.get(r2, r3)     // Catch:{ all -> 0x01cb }
                boolean r1 = r1.equals(r2)     // Catch:{ all -> 0x01cb }
                if (r1 != 0) goto L_0x01a1
                com.sec.android.app.hwmoduletest.HwModuleTest r1 = com.sec.android.app.hwmoduletest.HwModuleTest.this     // Catch:{ all -> 0x01cb }
                java.lang.String r1 = r1.CLASS_NAME     // Catch:{ all -> 0x01cb }
                java.lang.String r2 = "mBroadcastReceiver"
                java.lang.String r3 = "start sec_diag_uart_log"
                com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r1, r2, r3)     // Catch:{ all -> 0x01cb }
                java.lang.String r1 = "ctl.start"
                java.lang.String r2 = "sec_diag_uart_log"
                android.os.SystemProperties.set(r1, r2)     // Catch:{ all -> 0x01cb }
            L_0x01a1:
                android.content.Intent r1 = new android.content.Intent     // Catch:{ all -> 0x01cb }
                java.lang.String r2 = "com.sec.factory.OQCSBFTT.RESPONSE"
                r1.<init>(r2)     // Catch:{ all -> 0x01cb }
                com.sec.android.app.hwmoduletest.HwModuleTest r2 = com.sec.android.app.hwmoduletest.HwModuleTest.this     // Catch:{ all -> 0x01cb }
                r2.sendBroadcast(r1)     // Catch:{ all -> 0x01cb }
                goto L_0x01c9
            L_0x01ae:
                java.lang.String r1 = "com.sec.factory.OQCSBFTT.READ_DATA"
                boolean r1 = r1.equals(r0)     // Catch:{ all -> 0x01cb }
                if (r1 == 0) goto L_0x01c9
                android.content.Intent r1 = new android.content.Intent     // Catch:{ all -> 0x01cb }
                java.lang.String r2 = "com.sec.factory.OQCSBFTT.RESPONSE"
                r1.<init>(r2)     // Catch:{ all -> 0x01cb }
                java.lang.String r2 = "OQCSBFTT"
                boolean r3 = com.sec.android.app.hwmoduletest.support.BaseActivity.isOqcsbftt     // Catch:{ all -> 0x01cb }
                r1.putExtra(r2, r3)     // Catch:{ all -> 0x01cb }
                com.sec.android.app.hwmoduletest.HwModuleTest r2 = com.sec.android.app.hwmoduletest.HwModuleTest.this     // Catch:{ all -> 0x01cb }
                r2.sendBroadcast(r1)     // Catch:{ all -> 0x01cb }
            L_0x01c9:
                monitor-exit(r8)
                return
            L_0x01cb:
                r9 = move-exception
                monitor-exit(r8)
                throw r9
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.hwmoduletest.HwModuleTest.C02451.onReceive(android.content.Context, android.content.Intent):void");
        }
    };
    private ViewGroup mContainer;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    LtUtil.log_d(HwModuleTest.this.CLASS_NAME, "handleMessage", "START_SAVE_TSP_DATA");
                    HwModuleTest.this.makeTspDataReference();
                    HwModuleTest.this.makeTspDataInspection();
                    StringBuilder sb = new StringBuilder();
                    sb.append("PassSavingReference -1 : ");
                    sb.append(HwModuleTest.this.PassSavingReference);
                    sb.append(" ,PassSavingInspection : ");
                    sb.append(HwModuleTest.this.PassSavingInspection);
                    LtUtil.log_d(HwModuleTest.this.CLASS_NAME, "StartTspdataTest", sb.toString());
                    if (HwModuleTest.this.PassSavingReference && HwModuleTest.this.PassSavingInspection) {
                        LtUtil.log_d(HwModuleTest.this.CLASS_NAME, "StartTspdataTest", "remove dialog");
                        HwModuleTest.this.mHandler.removeMessages(1001);
                        HwModuleTest.this.hideProgressDialog();
                        return;
                    }
                    return;
                case 1002:
                    Toast.makeText(HwModuleTest.this.getBaseContext(), "AFC", 1).show();
                    return;
                default:
                    return;
            }
        }
    };
    private final boolean mIsLongPress = false;
    private boolean mIsPlayingSound = false;
    /* access modifiers changed from: private */
    public boolean mIsShowingToastAFC = false;
    /* access modifiers changed from: private */
    public boolean mIsStartAFC = false;
    private MediaPlayer mMediaPlayer;
    private ModuleDevice mModuleDevice;
    private int mPlayingSoundSpk = 0;
    private final long mPrevBackKeyEventTime = -1;
    String mProduct = SystemProperties.get("ro.product.model", Properties.PROPERTIES_DEFAULT_STRING);
    private String mSpeakerName;
    private int mTestCount = 0;
    private String mTspManufacture;
    private boolean needToResetUart = false;
    private boolean needToStopFtClient = false;
    String testcase;

    public class EmptyListener implements DialogInterface.OnClickListener {
        public EmptyListener() {
        }

        public void onClick(DialogInterface dialog, int which) {
            HwModuleTest.this.finish();
        }
    }

    public HwModuleTest() {
        super("HwModuleTest");
    }

    /* access modifiers changed from: private */
    public void readChargeData() {
        String str = "";
        int i = this.mTestCount + 1;
        this.mTestCount = i;
        if (i <= 5) {
            String hvChargeStatus = Kernel.read(Kernel.HV_CHARGE_STATUS);
            if (EgisFingerprint.MAJOR_VERSION.equals(hvChargeStatus) || egis.optical.client.api.EgisFingerprint.MAJOR_VERSION.equals(hvChargeStatus)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Success| Charge Status:");
                sb.append(hvChargeStatus);
                LtUtil.log_i(this.CLASS_NAME, "readChargeData", sb.toString());
                this.mHandler.sendEmptyMessage(1002);
                this.mTestCount = 0;
                return;
            }
            Sleep.sleep(1000);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Charge Status:");
            sb2.append(hvChargeStatus);
            sb2.append(" / delay 1 sec - TestCount : ");
            sb2.append(this.mTestCount);
            LtUtil.log_i(this.CLASS_NAME, "readChargeData", sb2.toString());
            readChargeData();
            return;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Fail| Charge Status:");
        sb3.append(Kernel.read(Kernel.HV_CHARGE_STATUS));
        LtUtil.log_i(this.CLASS_NAME, "readChargeData", sb3.toString());
        this.mTestCount = 0;
    }

    /* access modifiers changed from: private */
    public void readWirelessChargeData() {
        int i = this.mTestCount + 1;
        this.mTestCount = i;
        if (i > 5) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fail| Charge Status:");
            sb.append(Kernel.read(Kernel.BATT_HV_WIRELESS_STATUS));
            LtUtil.log_i(this.CLASS_NAME, "readWirelessChargeData", sb.toString());
            this.mTestCount = 0;
        } else if ("3".equals(Kernel.read(Kernel.BATT_HV_WIRELESS_STATUS))) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Success| Charge Status:");
            sb2.append(Kernel.read(Kernel.BATT_HV_WIRELESS_STATUS));
            LtUtil.log_i(this.CLASS_NAME, "readWirelessChargeData", sb2.toString());
            this.mHandler.sendEmptyMessage(1002);
            this.mTestCount = 0;
        } else {
            Sleep.sleep(1000);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Charge Status:");
            sb3.append(Kernel.read(Kernel.BATT_HV_WIRELESS_STATUS));
            sb3.append(" / delay 1 sec - TestCount : ");
            sb3.append(this.mTestCount);
            LtUtil.log_i(this.CLASS_NAME, "readWirelessChargeData", sb3.toString());
            readWirelessChargeData();
        }
    }

    /* access modifiers changed from: private */
    public void writeSOC(String result) {
        File socDataFile = new File(Kernel.getFilePath(Kernel.BATTERY_SAVE_SOC_DATA));
        if (result != null) {
            Kernel.writeToPathNsync(socDataFile.getPath(), result);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!XMLDataStorage.instance().wasCompletedParsing()) {
            XMLDataStorage.instance().parseXML(this);
        }
        setContentView(C0268R.layout.hwmoduletest);
        this.mContainer = (ViewGroup) findViewById(C0268R.C0269id.container);
        mModulePower = mModulePower == null ? ModulePower.instance(this) : mModulePower;
        this.mModuleDevice = ModuleDevice.instance(this);
        ModuleDevice moduleDevice = this.mModuleDevice;
        ModuleDevice moduleDevice2 = this.mModuleDevice;
        this.mTspManufacture = moduleDevice.startTSPTest(ModuleDevice.TSP_CMD_CHIP_VENDOR_READ).toUpperCase(Locale.ENGLISH);
        this.mAccRotationValue = System.getInt(getContentResolver(), "accelerometer_rotation", 0);
        if (Feature.getBoolean(Feature.SET_ACCELEROMETER_ROTATION, false)) {
            System.putInt(getContentResolver(), "accelerometer_rotation", 1);
        } else {
            System.putInt(getContentResolver(), "accelerometer_rotation", 0);
        }
        checkDeviceType();
        enableUart();
    }

    public void onStart() {
        super.onStart();
        if (!Feature.getBoolean(Feature.ATD_RECONNECT_FROM_SSRM) && !LtUtil.isEMATCmd(getBaseContext())) {
            sendRequestReconnectFtClient();
        }
    }

    private void sendRequestReconnectFtClient() {
        LtUtil.log_d(this.CLASS_NAME, "sendRequestReconnectFtClient", "");
        if (!FactoryTest.isFactoryBinary() && VERSION.SDK_INT >= 24 && !LtUtil.isEMATCmd(getBaseContext()) && !isRunningFtClient()) {
            String jigCheck = Kernel.read(Kernel.JIG_CONNECTION_CHECK);
            if (jigCheck != null && jigCheck.contains("JIG")) {
                Intent intent = new Intent("com.sec.atd.request_reconnect");
                intent.addFlags(16777216);
                sendBroadcast(intent);
            }
        }
    }

    private void sendRequestStopFtClient() {
        if (!FactoryTest.isFactoryBinary() && VERSION.SDK_INT >= 24 && !LtUtil.isEMATCmd(getBaseContext()) && isRunningFtClient()) {
            Intent intent = new Intent("com.sec.factory.stop_ftclient");
            intent.addFlags(16777216);
            sendBroadcast(intent);
        }
    }

    public void stopUsbFtClient() {
        LtUtil.log_d(this.CLASS_NAME, "stopUsbFtClient", "");
        Intent intent = new Intent("com.samsung.datadistributor.disconnect");
        intent.addFlags(16777216);
        sendBroadcast(intent);
    }

    public boolean isRunningFtClient() {
        String result = SystemProperties.get(KEY_RUNNING_FTCLIENT, "false");
        StringBuilder sb = new StringBuilder();
        sb.append("RUNNING_FTCLIENT : ");
        sb.append(result);
        LtUtil.log_d(this.CLASS_NAME, "isRunningFtClient", sb.toString());
        return "true".equalsIgnoreCase(result);
    }

    private void enableUart() {
        if ("0".equals(Kernel.read(Kernel.UART_ON_OFF))) {
            Kernel.write(Kernel.UART_ON_OFF, EgisFingerprint.MAJOR_VERSION);
            this.needToResetUart = true;
        }
    }

    private void disableUart() {
        if (this.needToResetUart) {
            Kernel.write(Kernel.UART_ON_OFF, "0");
        }
    }

    private void startOppositeScreenForDualScreen() {
        if (mIsDualScreenFeatureEnabled) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setClass(getBaseContext(), OppositeActivity.class);
            SDualScreenActivity.makeIntent(this, intent, DualScreen.SUB, SDualScreenActivity.FLAG_COUPLED_TASK_EXPAND_MODE);
            startActivityForResult(intent, 1);
        }
    }

    private void checkDeviceType() {
        if ("dongle".equalsIgnoreCase(Feature.getString(Feature.DEVICE_TYPE))) {
            Builder builder = new Builder(this);
            builder.setTitle("Check Device Type");
            builder.setMessage("We don't support devices like a Dongle or Setopbox.");
            builder.setPositiveButton("OK", new EmptyListener());
            builder.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(DialogInterface dInterface, int keyCode, KeyEvent event) {
                    HwModuleTest.this.finish();
                    return true;
                }
            });
            this.mAlertDialog = builder.create();
            this.mAlertDialog.show();
        }
    }

    private void showAlert_SensorReset() {
        Builder builder = new Builder(this);
        builder.setMessage("Waiting for sensor reset.\nPlease try again.");
        builder.setPositiveButton("OK", null);
        this.mAlertDialog = builder.create();
        this.mAlertDialog.show();
    }

    public void onResume() {
        StringBuilder sb = new StringBuilder();
        sb.append("Resume ");
        sb.append(this.CLASS_NAME);
        LtUtil.log_i(this.CLASS_NAME, "onResume", sb.toString());
        super.onResume();
        createUI();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction(ACTION_OQCSBFTT);
        intentFilter.addAction(ACTION_OQCSBFTT_READ_DATA);
        registerReceiver(this.mBroadcastReceiver, intentFilter);
    }

    public void onPause() {
        StringBuilder sb = new StringBuilder();
        sb.append("mIsPlayingSound : ");
        sb.append(this.mIsPlayingSound);
        LtUtil.log_i(this.CLASS_NAME, "onPause", sb.toString());
        if (this.mIsPlayingSound) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("stopSpeakerTest ");
            sb2.append(this.CLASS_NAME);
            LtUtil.log_i(this.CLASS_NAME, "onPause", sb2.toString());
            if (Feature.getBoolean(Feature.SUPPORT_RCV_SPK_COMBINANTION)) {
                this.mPlayingSoundSpk = 5;
            }
            stopSpeakerTest();
        }
        unregisterReceiver(this.mBroadcastReceiver);
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        disableUart();
        sendRequestStopFtClient();
        if (Feature.getBoolean(Feature.SUPPORT_HMT_USB_FTCLIENT) && !LtUtil.isEMATCmd(getBaseContext())) {
            stopUsbFtClient();
        }
        if (!FactoryTest.isFactoryBinary() && "Qualcomm".equals(Feature.getString(Feature.CHIPSET_MANUFACTURE))) {
            if (VERSION.SDK_INT <= 26) {
                if ("running".equals(SystemProperties.get("init.svc.diag_uart_log", Properties.PROPERTIES_DEFAULT_STRING))) {
                    LtUtil.log_d(this.CLASS_NAME, "mBroadcastReceiver", "stop diag_uart_log");
                    SystemProperties.set("ctl.stop", "diag_uart_log");
                }
            } else if ("running".equals(SystemProperties.get("init.svc.sec_diag_uart_log", Properties.PROPERTIES_DEFAULT_STRING))) {
                LtUtil.log_d(this.CLASS_NAME, "mBroadcastReceiver", "stop sec_diag_uart_log");
                SystemProperties.set("ctl.stop", "sec_diag_uart_log");
            }
        }
        System.putInt(getContentResolver(), "accelerometer_rotation", this.mAccRotationValue);
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 104) {
            StringBuilder sb = new StringBuilder();
            sb.append("resultCode = ");
            sb.append(resultCode);
            LtUtil.log_d(this.CLASS_NAME, "onActivityResult", sb.toString());
            Intent i = new Intent(this, TouchTest.class);
            if (resultCode == 0) {
                i.putExtra("TEST_TSP_SELF", false);
            } else {
                i.putExtra("TEST_TSP_SELF", true);
            }
            startActivity(i);
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    public void onClick(View view) {
        int id = view.getId();
        StringBuilder sb = new StringBuilder();
        sb.append("id=");
        sb.append(id);
        LtUtil.log_d(this.CLASS_NAME, "onClick", sb.toString());
        Intent intent = new Intent();
        switch (id) {
            case 0:
                intent.setClass(this, ColorTest.class);
                intent.putExtra("color", -65536);
                startActivity(intent);
                return;
            case 1:
                intent.setClass(this, ColorTest.class);
                intent.putExtra("color", -16711936);
                startActivity(intent);
                return;
            case 2:
                intent.setClass(this, ColorTest.class);
                intent.putExtra("color", -16776961);
                startActivity(intent);
                return;
            case 3:
                intent.setClass(this, DimmingTest.class);
                startActivity(intent);
                return;
            case 4:
            case Constants.CMD_TEST_SET_CONFIG /*27*/:
                if (id == 4) {
                    this.testcase = HwTestMenu.getTestCase(HwTestMenu.LCD_TEST_TOUCH).toUpperCase(Locale.ENGLISH);
                } else if (id == 27) {
                    this.testcase = HwTestMenu.getTestCase(HwTestMenu.LCD_TEST_TOUCH_2).toUpperCase(Locale.ENGLISH);
                }
                intent.putExtra("isHovering", false);
                intent.putExtra("testID", id);
                if (this.testcase.contains("STYLE_X")) {
                    intent.setClass(this, TspPatternStyleX.class);
                    startActivity(intent);
                    return;
                }
                intent.setClass(this, TouchTest.class);
                startActivity(intent);
                return;
            case 5:
                if (isApplicableToCamTest()) {
                    intent.setClassName("com.sec.factory.camera", "com.sec.android.app.camera.CameraTestActivity");
                    intent.putExtra("testtype", "HW");
                    intent.putExtra("arg1", "rear");
                    startActivity(intent);
                    return;
                }
                return;
            case 6:
                intent.setClassName("com.sec.factory.camera", "com.sec.android.app.camera.CameraTestActivity");
                intent.putExtra("testtype", "HW");
                intent.putExtra("arg1", "front");
                startActivity(intent);
                return;
            case 7:
                intent.setClass(this, ReceiverTest.class);
                startActivity(intent);
                return;
            case 8:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("ID_SPEAKER mIsPlayingSound : ");
                sb2.append(this.mIsPlayingSound);
                LtUtil.log_i(this.CLASS_NAME, "onClick", sb2.toString());
                if (Feature.getInt(Feature.SPEAKER_COUNT) == 4) {
                    intent.setClass(this, SpeakerTest.class);
                    startActivity(intent);
                    return;
                } else if (this.mIsPlayingSound) {
                    if (!Feature.getBoolean(Feature.SUPPORT_RCV_SPK_COMBINANTION)) {
                        stopSpeakerTest();
                        return;
                    } else if (this.mPlayingSoundSpk == 5) {
                        Button btn = (Button) this.mContainer.findViewById(8);
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("△");
                        sb3.append(this.mSpeakerName);
                        sb3.append("▽");
                        btn.setText(sb3.toString());
                        stopSpeakerTest();
                        return;
                    } else {
                        stopSpeakerTest();
                        this.mPlayingSoundSpk++;
                        startSpeakerTest(this.mPlayingSoundSpk);
                        return;
                    }
                } else if (HwTestMenu.getEnabled(16) == null || !"true".equals(HwTestMenu.getEnabled(16))) {
                    if (Feature.getBoolean(Feature.SUPPORT_RCV_AS_PSEUDO_SPK)) {
                        Sleep.sleep(300);
                    }
                    if (Feature.getBoolean(Feature.SUPPORT_RCV_SPK_COMBINANTION)) {
                        this.mPlayingSoundSpk = 3;
                        startSpeakerTest(this.mPlayingSoundSpk);
                        return;
                    }
                    startSpeakerTest();
                    return;
                } else {
                    startSpeakerTest(2);
                    return;
                }
            case 9:
                boolean passSensorReset = true;
                String str = "";
                if (Kernel.isExistFileID(Kernel.STATE_SENSOR_RESET)) {
                    if ("0".equals(Kernel.read(Kernel.STATE_SENSOR_RESET))) {
                        passSensorReset = false;
                        showAlert_SensorReset();
                    }
                }
                if (passSensorReset) {
                    intent.setClass(this, SensorTest.class);
                    intent.putExtra("isSensorHubTest", false);
                    startActivity(intent);
                    return;
                }
                return;
            case 10:
                intent.setClass(this, VibrationTest.class);
                startActivity(intent);
                return;
            case 11:
                mModulePower.doWakeLock(false);
                LtUtil.sleep(this);
                return;
            case 12:
                intent.setClass(this, SubKeyTest.class);
                startActivity(intent);
                return;
            case 13:
                intent.setClass(this, LedTest.class);
                startActivity(intent);
                return;
            case 14:
                intent.setClass(this, DigitizerTest.class);
                startActivity(intent);
                return;
            case 15:
                if (Feature.getBoolean(Feature.GRIP_TEST_UI_NEW_CONCEPT)) {
                    intent.setClass(this, GripSensorWorkingTest.class);
                } else if (Feature.getBoolean(Feature.GRIP_TOUCHIC_ENABLE, false)) {
                    intent.setClass(this, GripSensorTestTouchIc.class);
                } else if (!Feature.getString(Feature.GRIPSENSOR_TYPE).equalsIgnoreCase("AP") && !Feature.getString(Feature.GRIPSENSOR_TYPE).equalsIgnoreCase("WIFI")) {
                    intent.setClass(this, GripSensorTest.class);
                } else if (Feature.getBoolean(Feature.GRIP_SENSOR_LIMB_BODY, false)) {
                    intent.setClass(this, GripSensorTestLnB.class);
                } else if (Feature.getBoolean(Feature.GRIP_TEST_UI_OLD_CONCEPT, false)) {
                    intent.setClass(this, GripSensorTest2.class);
                } else {
                    intent.setClass(this, GripSensorWorkingTest.class);
                }
                startActivity(intent);
                return;
            case 16:
                StringBuilder sb4 = new StringBuilder();
                sb4.append("ID_SPEAKER_R mIsPlayingSound : ");
                sb4.append(this.mIsPlayingSound);
                LtUtil.log_i(this.CLASS_NAME, "onClick", sb4.toString());
                if (Feature.getInt(Feature.SPEAKER_COUNT) == 4) {
                    intent.setClass(this, SpeakerTest.class);
                    startActivity(intent);
                    return;
                } else if (this.mIsPlayingSound) {
                    stopSpeakerTest();
                    return;
                } else if (HwTestMenu.getEnabled(16) == null || !"true".equals(HwTestMenu.getEnabled(16))) {
                    startSpeakerTest();
                    return;
                } else {
                    startSpeakerTest(1);
                    return;
                }
            case 17:
                intent.setClass(this, IrLedTest.class);
                startActivity(intent);
                return;
            case 18:
                if ("LAND".equalsIgnoreCase(HwTestMenu.getTestCase(HwTestMenu.HDMI_TYPE))) {
                    intent.setClass(this, HDMI_Landscape.class);
                } else {
                    intent.setClass(this, HDMI_Portrait.class);
                }
                startActivity(intent);
                return;
            case 19:
                intent.setClass(this, LowFrequencyTest.class);
                startActivity(intent);
                return;
            case 20:
                intent.setClass(this, SvcLedTest.class);
                startActivity(intent);
                return;
            case 21:
                StartTspdataTest();
                return;
            case 22:
                intent.setClass(this, BarcodeEmulTest.class);
                startActivity(intent);
                return;
            case 23:
                intent.setClass(this, SensorHubTest.class);
                startActivity(intent);
                return;
            case 24:
                intent.setClass(this, LcdFlameTest.class);
                startActivity(intent);
                return;
            case Constants.CMD_TEST_CANCEL /*25*/:
                if (Feature.getBoolean(Feature.HALLIC_WITH_SENSORHUB, false)) {
                    intent.setClass(this, HallICTest_SensorHub.class);
                } else {
                    intent.setClass(this, HallICTest.class);
                }
                startActivity(intent);
                return;
            case 26:
                LtUtil.log_d(this.CLASS_NAME, "startTSP", "Start TSP Hovering");
                intent.putExtra("isHovering", true);
                if (HwTestMenu.getTestCase(HwTestMenu.LCD_TEST_TOUCH).toUpperCase(Locale.ENGLISH).contains("STYLE_X")) {
                    intent.setClass(this, TspPatternStyleHoverX.class);
                    startActivity(intent);
                    return;
                }
                intent.setClass(this, TouchTest.class);
                startActivity(intent);
                return;
            case Constants.CMD_TEST_DOWNLOAD_CFG /*29*/:
                LtUtil.log_d(this.CLASS_NAME, "startSPEN", "Start SPEN Hovering");
                intent.setClass(this, SpenHoveringDrawTest.class);
                startActivity(intent);
                return;
            case 31:
                intent.setClass(this, BarometerWaterProofTest.class);
                startActivity(intent);
                return;
            case 34:
                if (!Kernel.isExistFile(Kernel.SUPPORT_MST_GPIO)) {
                    intent.setClass(this, MSTTest.class);
                    startActivity(intent);
                    return;
                } else if (EgisFingerprint.MAJOR_VERSION.equals(Kernel.read(Kernel.SUPPORT_MST_GPIO))) {
                    intent.setClass(this, MSTTest.class);
                    startActivity(intent);
                    return;
                } else {
                    Toast.makeText(getBaseContext(), "MST not support", 1).show();
                    return;
                }
            case 35:
                intent.setClass(this, SPen.class);
                startActivity(intent);
                return;
            case 36:
                intent.setClass(this, MoodLedTest.class);
                startActivity(intent);
                return;
            case Constants.CMD_TEST_REAL_TIME_DATA /*41*/:
                intent.setClass(this, IrisCamTest.class);
                startActivity(intent);
                return;
            case 42:
                intent.setClass(this, IrisLedTest.class);
                startActivity(intent);
                return;
            case Constants.CMD_TEST_READ_FW /*43*/:
                intent.setClass(this, IrisEnrollTest.class);
                startActivity(intent);
                return;
            case 44:
                intent.setClass(this, GripSensorTestTouchIc.class);
                startActivity(intent);
                return;
            case 45:
                intent.setClassName("com.sec.android.app.factorykeystring", "com.sec.android.app.status.LoopbackTestNew");
                intent.addFlags(1082130432);
                startActivity(intent);
                return;
            case 46:
                intent.setClassName("com.sec.android.app.factorykeystring", "com.sec.android.app.version.SimpleVersion");
                intent.addFlags(1082130432);
                startActivity(intent);
                return;
            case Constants.CMD_TEST_RAWDATA_SATURATED /*47*/:
                intent.setClassName("com.sec.factory.camera", "com.sec.android.app.camera.CameraTestActivity");
                intent.putExtra("testtype", "HW");
                intent.putExtra("arg1", "tof");
                startActivity(intent);
                return;
            default:
                return;
        }
    }

    private void createUI() {
        int i;
        if (this.mContainer == null) {
            this.mContainer = (ViewGroup) findViewById(C0268R.C0269id.container);
        }
        this.mContainer.removeAllViews();
        ArrayList<String> menuList = new ArrayList<>(Arrays.asList(HwTestMenu.getTestMenu()));
        int i2 = 0;
        if (HwTestMenu.getEnabled(15) != null && Feature.getBoolean(Feature.FEATURE_ENABLE_DYNAMIC_GRIP) && Kernel.read(Kernel.GRIP_SENSOR_NAME) == null) {
            Iterator it = menuList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String item = (String) it.next();
                if (HwTestMenu.LCD_TEST_GRIP.equalsIgnoreCase(item.split(",")[0])) {
                    LtUtil.log_d(this.CLASS_NAME, "createUI", "ID_GRIP is removed.");
                    menuList.remove(item);
                    break;
                }
            }
        }
        int i3 = 0;
        while (i3 < menuList.size()) {
            LinearLayout layout = new LinearLayout(this);
            layout.setLayoutParams(new LayoutParams(-1, -2, 1.0f));
            layout.setOrientation(i2);
            layout.setWeightSum(3.0f);
            if (i3 + 3 >= menuList.size()) {
                i = menuList.size();
            } else {
                i = i3 + 3;
            }
            for (String item2 : menuList.subList(i3, i)) {
                String id = item2.split(",")[i2];
                String text = item2.split(",")[1];
                Button button = new Button(this);
                if (Feature.getBoolean(Feature.SUPPORT_RCV_SPK_COMBINANTION) && Integer.parseInt(id) == 8) {
                    this.mSpeakerName = text;
                    StringBuilder sb = new StringBuilder();
                    sb.append("△");
                    sb.append(this.mSpeakerName);
                    sb.append("▽");
                    text = sb.toString();
                }
                button.setLayoutParams(new LayoutParams(i2, -1, 1.0f));
                button.setId(Integer.parseInt(id));
                button.setText(text);
                button.setOnClickListener(this);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Create Button. id=");
                sb2.append(id);
                sb2.append(", name=");
                sb2.append(text);
                LtUtil.log_d(this.CLASS_NAME, "createUI", sb2.toString());
                layout.addView(button);
                i2 = 0;
            }
            this.mContainer.addView(layout);
            i3 += 3;
            i2 = 0;
        }
    }

    private String SubStringProduct() {
        return this.mProduct.substring(this.mProduct.indexOf(45) + 1, this.mProduct.length());
    }

    /* access modifiers changed from: private */
    public void makeTspDataReference() {
        FileOutputStream fos = null;
        File FileReference = new File(CreateFile("TSP_Reference"));
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("TSPFile");
            sb.append(this.TSPFile);
            LtUtil.log_d(this.CLASS_NAME, "makeFileTspdata", sb.toString());
            fos = new FileOutputStream(FileReference, true);
            fos.write(get_Reference_ReadValue().getBytes());
            this.PassSavingReference = true;
            fos.close();
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            if (fos != null) {
                fos.close();
            }
        } catch (Throwable th) {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            throw th;
        }
    }

    /* access modifiers changed from: private */
    public void makeTspDataInspection() {
        FileOutputStream fos = null;
        File FileInspection = new File(CreateFile("TSP_Delta"));
        try {
            LtUtil.log_d(this.CLASS_NAME, "makeFileTspdata", "");
            fos = new FileOutputStream(FileInspection, true);
            fos.write(get_Inspection_ReadValue().getBytes());
            this.PassSavingInspection = true;
            fos.close();
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            if (fos != null) {
                fos.close();
            }
        } catch (Throwable th) {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            throw th;
        }
    }

    private String get_Reference_ReadValue() {
        StringBuilder sb = new StringBuilder();
        sb.append(" mTspManufacture :");
        sb.append(this.mTspManufacture);
        LtUtil.log_d(this.CLASS_NAME, "get_Reference_ReadValue()", sb.toString());
        String result = "";
        if ("MELFAS".equals(this.mTspManufacture)) {
            result = this.mModuleDevice.startTSPTest(ModuleDevice.TSP_CMD_READ1_RUN_CM_ABS);
        } else if ("SYNAPTICS".equals(this.mTspManufacture)) {
            this.mModuleDevice.startTSPTest("get_x_num");
            result = this.mModuleDevice.startTSPTest(ModuleDevice.TSP_CMD_READ1_RUN_RAWCAP);
        } else if ("STM".equals(this.mTspManufacture)) {
            result = this.mModuleDevice.startTSPTest(ModuleDevice.TSP_CMD_READ1_RUN_REF);
        } else if ("ATMEL".equals(this.mTspManufacture)) {
            result = this.mModuleDevice.startTSPTest(ModuleDevice.TSP_CMD_READ1_RUN_REF);
        }
        if ("MELFAS".equals(this.mTspManufacture)) {
            return this.mModuleDevice.startTSPReadTest(ModuleDevice.TSP_CMD_READ1_CM_ABS, 10);
        }
        if ("SYNAPTICS".equals(this.mTspManufacture)) {
            return this.mModuleDevice.startTSPReadTest(ModuleDevice.TSP_CMD_READ1_RAWCAP, 10);
        }
        if ("STM".equals(this.mTspManufacture)) {
            return this.mModuleDevice.startTSPReadTest(ModuleDevice.TSP_CMD_READ1_GET_REF, 10);
        }
        if ("ATMEL".equals(this.mTspManufacture)) {
            return this.mModuleDevice.startTSPReadTest(ModuleDevice.TSP_CMD_READ1_GET_REF, 10);
        }
        return result;
    }

    private String get_Inspection_ReadValue() {
        StringBuilder sb = new StringBuilder();
        sb.append(" mTspManufacture :");
        sb.append(this.mTspManufacture);
        LtUtil.log_d(this.CLASS_NAME, "get_Inspection_ReadValue()", sb.toString());
        String str = "";
        String result = "";
        if ("MELFAS".equals(this.mTspManufacture)) {
            result = this.mModuleDevice.startTSPTest(ModuleDevice.TSP_CMD_READ1_RUN_CM_ABS);
        } else if ("SYNAPTICS".equals(this.mTspManufacture)) {
            ModuleDevice moduleDevice = this.mModuleDevice;
            result = "NA";
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" result :");
            sb2.append(result);
            LtUtil.log_d(this.CLASS_NAME, "get_Inspection_ReadValue()", sb2.toString());
        } else if ("STM".equals(this.mTspManufacture)) {
            result = this.mModuleDevice.startTSPTest(ModuleDevice.TSP_CMD_READ5_RUN_RAW);
        } else if ("ATMEL".equals(this.mTspManufacture)) {
            result = this.mModuleDevice.startTSPTest("run_delta_read");
        }
        if ("MELFAS".equals(this.mTspManufacture)) {
            return this.mModuleDevice.startTSPReadTest(ModuleDevice.TSP_CMD_READ1_CM_ABS, 10);
        }
        if ("SYNAPTICS".equals(this.mTspManufacture)) {
            ModuleDevice moduleDevice2 = this.mModuleDevice;
            String result2 = "NA";
            StringBuilder sb3 = new StringBuilder();
            sb3.append("SYNAPTICS : GET_INSPECTION  - result: ");
            sb3.append(result2);
            LtUtil.log_d(this.CLASS_NAME, "get_Inspection_ReadValue(), ", sb3.toString());
            return result2;
        } else if ("STM".equals(this.mTspManufacture)) {
            return this.mModuleDevice.startTSPReadTest(ModuleDevice.TSP_CMD_READ2_GET_RAW, 10);
        } else {
            if ("ATMEL".equals(this.mTspManufacture)) {
                return this.mModuleDevice.startTSPReadTest("get_delta", 10);
            }
            return result;
        }
    }

    private String CreateFile(String tsptype) {
        File dir = new File("mnt/extSdCard/");
        String fileName = tsptype;
        String exr = ".txt";
        StringBuilder sb = new StringBuilder();
        sb.append("mnt/extSdCard/");
        sb.append(SubStringProduct());
        sb.append("_");
        sb.append(fileName);
        sb.append(exr);
        File file = new File(sb.toString());
        int count = 0;
        LtUtil.log_d(this.CLASS_NAME, "CreateFile", "");
        if (!dir.exists()) {
            return null;
        }
        String[] arr = dir.list();
        String str = this.CLASS_NAME;
        String str2 = "CreateFile";
        StringBuilder sb2 = new StringBuilder();
        sb2.append("arr : ");
        sb2.append(arr != null ? Arrays.toString(arr) : "null");
        LtUtil.log_d(str, str2, sb2.toString());
        if (file.exists()) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("file : ");
            sb3.append(file);
            LtUtil.log_d(this.CLASS_NAME, "CreateFile", sb3.toString());
            if (arr != null) {
                int count2 = 0;
                for (String str3 : arr) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("str : ");
                    sb4.append(str3);
                    sb4.append(",fileName");
                    sb4.append(fileName);
                    LtUtil.log_d(this.CLASS_NAME, "CreateFile", sb4.toString());
                    if (str3.length() > 5) {
                        String substring = str3.substring(0, str3.length() - 4);
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(SubStringProduct());
                        sb5.append("_");
                        sb5.append(fileName);
                        if (substring.startsWith(sb5.toString())) {
                            count2++;
                        }
                    }
                }
                count = count2;
            }
            StringBuilder sb6 = new StringBuilder();
            sb6.append("mnt/extSdCard/");
            sb6.append(SubStringProduct());
            sb6.append("_");
            sb6.append(fileName);
            sb6.append("_");
            sb6.append(count);
            sb6.append(exr);
            return sb6.toString();
        }
        StringBuilder sb7 = new StringBuilder();
        sb7.append("mnt/extSdCard/");
        sb7.append(SubStringProduct());
        sb7.append("_");
        sb7.append(fileName);
        sb7.append(exr);
        return sb7.toString();
    }

    private void StartTspdataTest() {
        if (!this.mModuleDevice.isExternalMemoryExist() || !this.mModuleDevice.isMountedStorage(1)) {
            Toast.makeText(this, "Please insert SD card.", 0).show();
            LtUtil.log_d(this.CLASS_NAME, "StartTspdataTest", "SD Card is not Detect");
            return;
        }
        this.TspNotiDialog = showProgressDialog("Wait...");
        this.TspNotiDialog.show();
        LtUtil.log_d(this.CLASS_NAME, "StartTspdataTest", "StartTspdata Test!!!");
        this.mHandler.sendEmptyMessageDelayed(1001, 1000);
    }

    private AlertDialog showProgressDialog(String msg) {
        LtUtil.log_d(this.CLASS_NAME, "showProgressDialog", "Display Dialog");
        Builder ab = new Builder(this);
        ab.setTitle(msg);
        ab.setMessage("TSP Node Data entering to file.");
        ab.setCancelable(false);
        return ab.create();
    }

    /* access modifiers changed from: private */
    public void hideProgressDialog() {
        LtUtil.log_d(this.CLASS_NAME, "hideProgressDialog", "hide Dialog");
        if (this.TspNotiDialog.isShowing() && this.TspNotiDialog.getWindow() != null) {
            this.TspNotiDialog.dismiss();
        }
    }

    private void setLoopbackOff() {
        LtUtil.log_d(this.CLASS_NAME, "setLoopbackOff", "Loopback Off");
        ((AudioManager) getSystemService("audio")).setParameters("factory_test_loopback=off");
    }

    private void startSpeakerTest(int direction) {
        setLoopbackOff();
        AudioManager am = (AudioManager) getSystemService("audio");
        preVolume = am.getStreamVolume(3);
        am.setStreamVolume(3, am.getStreamMaxVolume(3), 0);
        StringBuilder sb = new StringBuilder();
        sb.append("direction: ");
        sb.append(direction);
        LtUtil.log_d(this.CLASS_NAME, "startSpeakerTest", sb.toString());
        if (direction == 2) {
            if (Feature.getBoolean(Feature.SEPARATE_SPK_PATH)) {
                am.setParameters("factory_test_route=speaker-l");
            } else {
                am.setParameters("factory_test_route=spk;factory_test_spkpath=spk2");
            }
        } else if (direction == 1) {
            if (Feature.getBoolean(Feature.SEPARATE_SPK_PATH)) {
                am.setParameters("factory_test_route=speaker-r");
            } else {
                am.setParameters("factory_test_route=spk;factory_test_spkpath=spk1");
            }
        } else if (direction == 5) {
            Button btn = (Button) this.mContainer.findViewById(8);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("▲");
            sb2.append(this.mSpeakerName);
            sb2.append("▽");
            btn.setText(sb2.toString());
            am.setParameters("factory_test_route=spk;factory_test_spkpath=spk2");
        } else if (direction == 4) {
            Button btn2 = (Button) this.mContainer.findViewById(8);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("△");
            sb3.append(this.mSpeakerName);
            sb3.append("▼");
            btn2.setText(sb3.toString());
            am.setParameters("factory_test_route=spk;factory_test_spkpath=spk1");
        } else if (direction == 3) {
            Button btn3 = (Button) this.mContainer.findViewById(8);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("▲");
            sb4.append(this.mSpeakerName);
            sb4.append("▼");
            btn3.setText(sb4.toString());
            am.setParameters("factory_test_route=spk;factory_test_spkpath=spk");
        }
        if (new File(Values.OverTheHorizonPath).exists()) {
            this.mMediaPlayer = MediaPlayer.create(this, Uri.parse(Values.OverTheHorizonPath));
        } else {
            this.mMediaPlayer = MediaPlayer.create(this, C0268R.raw.over_the_horizon);
        }
        this.mIsPlayingSound = true;
        am.setStreamVolume(3, (am.getStreamMaxVolume(3) * 100) / 100, 0);
        this.mMediaPlayer.setLooping(true);
        LtUtil.log_i(this.CLASS_NAME, "startSpeakerTest", "Start of calling method");
        this.mMediaPlayer.start();
        LtUtil.log_i(this.CLASS_NAME, "startSpeakerTest", "End of calling method");
    }

    private void startSpeakerTest() {
        this.mIsPlayingSound = true;
        StringBuilder sb = new StringBuilder();
        sb.append("mIsPlayingSound : ");
        sb.append(this.mIsPlayingSound);
        LtUtil.log_i(this.CLASS_NAME, "startSpeakerTest", sb.toString());
        setLoopbackOff();
        AudioManager am = (AudioManager) getSystemService("audio");
        preVolume = am.getStreamVolume(3);
        am.setStreamVolume(3, am.getStreamMaxVolume(3), 0);
        am.setParameters("factory_test_route=spk;factory_test_spkpath=spk");
        LtUtil.log_i(this.CLASS_NAME, "startSpeakerTest SetAudioPath  ", "factory_test_route = spk ");
        if (new File(Values.OverTheHorizonPath).exists()) {
            this.mMediaPlayer = MediaPlayer.create(this, Uri.parse(Values.OverTheHorizonPath));
        } else {
            this.mMediaPlayer = MediaPlayer.create(this, C0268R.raw.over_the_horizon);
        }
        this.mMediaPlayer.setLooping(true);
        this.mMediaPlayer.start();
    }

    private void stopSpeakerTest() {
        this.mMediaPlayer.stop();
        this.mMediaPlayer.release();
        this.mMediaPlayer = null;
        AudioManager am = (AudioManager) getSystemService("audio");
        am.setStreamVolume(3, preVolume, 0);
        if (Feature.getBoolean(Feature.SUPPORT_RCV_SPK_COMBINANTION)) {
            Sleep.sleep(500);
            if (this.mPlayingSoundSpk == 5) {
                am.setParameters("factory_test_route=off");
                Sleep.sleep(400);
            }
        } else {
            am.setParameters("factory_test_route=off");
            LtUtil.log_i(this.CLASS_NAME, "stopSpeakerTest SetAudioPath  ", "factory_test_route = off ");
        }
        this.mIsPlayingSound = false;
        StringBuilder sb = new StringBuilder();
        sb.append("mIsPlayingSound : ");
        sb.append(this.mIsPlayingSound);
        LtUtil.log_i(this.CLASS_NAME, "stopSpeakerTest", sb.toString());
    }

    private boolean isApplicableToCamTest() {
        int battLevel = registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("level", -1);
        StringBuilder sb = new StringBuilder();
        sb.append("result = ");
        sb.append(battLevel);
        sb.append("%");
        LtUtil.log_i(this.CLASS_NAME, "checkCapacity()", sb.toString());
        if (battLevel >= 15) {
            return true;
        }
        Builder builder = new Builder(this);
        builder.setTitle("Battery Low");
        builder.setMessage("Need to Charge 15% Over");
        builder.setPositiveButton("OK", null);
        builder.create().show();
        return false;
    }
}
