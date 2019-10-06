package com.sec.android.app.hwmoduletest;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.NVAccessor;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.HwTestMenu;
import com.sec.xmldata.support.Support.Kernel;

import egis.client.api.EgisFingerprint;

import static com.sec.xmldata.support.Support.Kernel.BATTERY_WPC_CTRL;

public class MSTTest extends BaseActivity implements OnClickListener {
    protected static final String CLASS_NAME = "MSTTest";
    /* access modifiers changed from: private */
    public static Boolean mIsThreadWriting = Boolean.valueOf(false);
    private static String mMstDriverStatus;
    /* access modifiers changed from: private */
    public static String mMstSysfsPath;
    private final String MST_DRIVER_ACTIVATING = "activating";
    private final String MST_DRIVER_WAITING = "waiting";
    private final int TRACK_DELAY = 1000;
    private final int WHAT_MST_DRIVER_ACTIVATING_CONTINUOUS_TRACK1 = 3;
    private final int WHAT_MST_DRIVER_ACTIVATING_CONTINUOUS_TRACK1PLUS2 = 5;
    private final int WHAT_MST_DRIVER_ACTIVATING_CONTINUOUS_TRACK2 = 4;
    private final int WHAT_MST_DRIVER_ACTIVATING_ONETIME = 2;
    private final int WHAT_MST_DRIVER_ACTIVATING_SOMETHING = 0;
    private final int WHAT_MST_DRIVER_WAITING = 1;
    private final int WHAT_STOP_WPC = 8;
    private final int WHAT_WPC_CHANGE_OFF_FAIL = 6;
    private final int WHAT_WPC_CHANGE_ON_FAIL = 7;
    private final String WPC_DISABLE = "0";
    private final String WPC_ENABLE = EgisFingerprint.MAJOR_VERSION;
    private int WPC_OFF_DELAY = 1200;
    private String WpcStatus = "";
    /* access modifiers changed from: private */
    public boolean canBeTransferred = true;
    private int count = 0;
    private boolean isEnHigh = false;
    /* access modifiers changed from: private */
    public boolean isThreadOff = false;
    private boolean isUseMFCIC = false;
    /* access modifiers changed from: private */
    public Button mButtonContinuousTrack1;
    /* access modifiers changed from: private */
    public Button mButtonContinuousTrack1plus2;
    /* access modifiers changed from: private */
    public Button mButtonContinuousTrack2;
    /* access modifiers changed from: private */
    public Button mButtonMSTOff;
    /* access modifiers changed from: private */
    public Button mButtonTrack1;
    /* access modifiers changed from: private */
    public Button mButtonTrack2;
    private Thread mContinuousButtonThread = null;
    private final String mDataTrack1 = "%B5424180661705886^*****/******            ^121010100000025901000000383000000?;E?";
    private final String mDataTrack2 = ";6010567836392988=25010005000060073930?=";
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            StringBuilder sb = new StringBuilder();
            sb.append("msg.what : ");
            sb.append(msg.what);
            LtUtil.log_d(MSTTest.CLASS_NAME, "handleMessage", sb.toString());
            switch (msg.what) {
                case 0:
                    MSTTest.this.mButtonTrack1.setEnabled(false);
                    MSTTest.this.mButtonTrack2.setEnabled(false);
                    MSTTest.this.mButtonContinuousTrack1.setEnabled(false);
                    MSTTest.this.mButtonContinuousTrack2.setEnabled(false);
                    MSTTest.this.mButtonContinuousTrack1plus2.setEnabled(false);
                    MSTTest.this.mButtonMSTOff.setEnabled(true);
                    return;
                case 1:
                case 2:
                    MSTTest.this.mButtonTrack1.setEnabled(true);
                    MSTTest.this.mButtonTrack2.setEnabled(true);
                    MSTTest.this.mButtonContinuousTrack1.setEnabled(true);
                    MSTTest.this.mButtonContinuousTrack2.setEnabled(true);
                    MSTTest.this.mButtonContinuousTrack1plus2.setEnabled(true);
                    MSTTest.this.mButtonMSTOff.setEnabled(false);
                    return;
                case 3:
                    MSTTest.this.mButtonTrack1.setEnabled(true);
                    MSTTest.this.mButtonTrack2.setEnabled(true);
                    MSTTest.this.mButtonContinuousTrack1.setEnabled(false);
                    MSTTest.this.mButtonContinuousTrack2.setEnabled(true);
                    MSTTest.this.mButtonContinuousTrack1plus2.setEnabled(true);
                    MSTTest.this.mButtonMSTOff.setEnabled(true);
                    return;
                case 4:
                    MSTTest.this.mButtonTrack1.setEnabled(true);
                    MSTTest.this.mButtonTrack2.setEnabled(true);
                    MSTTest.this.mButtonContinuousTrack1.setEnabled(true);
                    MSTTest.this.mButtonContinuousTrack2.setEnabled(false);
                    MSTTest.this.mButtonContinuousTrack1plus2.setEnabled(true);
                    MSTTest.this.mButtonMSTOff.setEnabled(true);
                    return;
                case 5:
                    MSTTest.this.mButtonTrack1.setEnabled(true);
                    MSTTest.this.mButtonTrack2.setEnabled(true);
                    MSTTest.this.mButtonContinuousTrack1.setEnabled(true);
                    MSTTest.this.mButtonContinuousTrack2.setEnabled(true);
                    MSTTest.this.mButtonContinuousTrack1plus2.setEnabled(false);
                    MSTTest.this.mButtonMSTOff.setEnabled(true);
                    return;
                case 6:
                    Builder builder = new Builder(MSTTest.this);
                    builder.setCancelable(false);
                    builder.setTitle("Warning");
                    builder.setMessage("WPC OFF FAIL");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            LtUtil.log_d(MSTTest.CLASS_NAME, "onClick", "OK");
                            MSTTest.this.finish();
                        }
                    });
                    Dialog mDialog = builder.create();
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    return;
                case 8:
                    Builder builder_stopWPC = new Builder(MSTTest.this);
                    builder_stopWPC.setCancelable(false);
                    builder_stopWPC.setTitle("Warning");
                    builder_stopWPC.setMessage("Please stop wireless charging first.");
                    builder_stopWPC.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            LtUtil.log_d(MSTTest.CLASS_NAME, "onClick", "OK");
                            MSTTest.this.shown_wpcOffDialog = false;
                        }
                    });
                    Dialog mDialog_stopWPC = builder_stopWPC.create();
                    mDialog_stopWPC.setCanceledOnTouchOutside(false);
                    mDialog_stopWPC.show();
                    return;
                default:
                    return;
            }
        }
    };
    private TextView mMstTestTitle;
    private TextView mSampleDataTrack1;
    private TextView mSampleDataTrack2;
    private Boolean mSupportEMCTest = Boolean.valueOf(false);
    private final String mSymbolTrack1 = "Track1";
    private final String mSymbolTrack2 = "Track2";
    /* access modifiers changed from: private */
    public boolean shown_wpcOffDialog = false;
    private boolean wpcOffFirstTime = true;

    public MSTTest() {
        super(CLASS_NAME);
    }

    /* access modifiers changed from: private */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean ChangeWPCStatus(java.lang.String r7) {

        if (r7.equals("0")) {
            LtUtil.log_d("MSTTest", "ChangeWPCStatus ", "0");
            Kernel.write(BATTERY_WPC_CTRL, "0");
            this.WpcStatus = Kernel.read(BATTERY_WPC_CTRL);

            if (this.WpcStatus.equals("0")) {
                boolean r0 = this.wpcOffFirstTime;

                if (r0) {
                    com.sec.android.app.hwmoduletest.support.LtUtil.log_i("MSTTest", "ChangeWPCStatus", "no delay(wpc)");
                    this.wpcOffFirstTime = false;
                    return true;
                } else {
                    LtUtil.Sleep.sleep(this.WPC_OFF_DELAY);

                    String xR4 = "off delay(wpc) : " + WPC_OFF_DELAY;
                    LtUtil.log_i("MSTTest", "ChangeWPCStatus", xR4);
                    this.WPC_OFF_DELAY = 1;
                    this.wpcOffFirstTime = false;
                    return true;
                }
            } else {
                LtUtil.log_i("MSTTest", "ChangeWPCStatus", "fail to turn off WPC ");
                this.mHandler.sendEmptyMessage(6);
                return false;
            }
        } else if (r7.equals("1")) {
            LtUtil.log_d("MSTTest", "ChangeWPCStatus ", "0");
            Kernel.write(BATTERY_WPC_CTRL, "1");
            this.WpcStatus = Kernel.read(BATTERY_WPC_CTRL);

            if (this.WpcStatus.equals("1")) {
                return true;
            } else {
                LtUtil.log_i("MSTTest", "ChangeWPCStatus", "fail to turn on WPC ");
                return false;
            }
        } else {
            return true;
        }


        /*
            r6 = this;
            int r0 = r7.hashCode()
            r1 = 1
            r2 = 0
            switch(r0) {
                case 48: goto L_0x0014;
                case 49: goto L_0x000a;
                default: goto L_0x0009;
            }
        L_0x0009:
            goto L_0x001e
        L_0x000a:
            java.lang.String r0 = "1"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x001e
            r0 = r1
            goto L_0x001f
        L_0x0014:
            java.lang.String r0 = "0"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x001e
            r0 = r2
            goto L_0x001f
        L_0x001e:
            r0 = -1
        L_0x001f:
            r3 = 0
            switch(r0) {
                case 0: goto L_0x004f;
                case 1: goto L_0x0025;
                default: goto L_0x0023;
            }
        L_0x0023:
            goto L_0x00b2
        L_0x0025:
            java.lang.String r0 = "MSTTest"
            java.lang.String r4 = "ChangeWPCStatus "
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r0, r4, r3)
            java.lang.String r0 = "BATTERY_WPC_CTRL"
            java.lang.String r3 = "1"
            com.sec.xmldata.support.Support.Kernel.write(r0, r3)
            java.lang.String r0 = "BATTERY_WPC_CTRL"
            java.lang.String r0 = com.sec.xmldata.support.Support.Kernel.read(r0)
            r6.WpcStatus = r0
            java.lang.String r0 = "1"
            java.lang.String r3 = r6.WpcStatus
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x00b2
            java.lang.String r0 = "MSTTest"
            java.lang.String r1 = "ChangeWPCStatus"
            java.lang.String r3 = "fail to turn on WPC "
            com.sec.android.app.hwmoduletest.support.LtUtil.log_i(r0, r1, r3)
            return r2
        L_0x004f:
            java.lang.String r0 = "MSTTest"
            java.lang.String r4 = "ChangeWPCStatus "
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r0, r4, r3)
            java.lang.String r0 = "BATTERY_WPC_CTRL"
            java.lang.String r3 = "0"
            com.sec.xmldata.support.Support.Kernel.write(r0, r3)
            java.lang.String r0 = "BATTERY_WPC_CTRL"
            java.lang.String r0 = com.sec.xmldata.support.Support.Kernel.read(r0)
            r6.WpcStatus = r0
            java.lang.String r0 = "0"
            java.lang.String r3 = r6.WpcStatus
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x007f
            java.lang.String r0 = "MSTTest"
            java.lang.String r1 = "ChangeWPCStatus"
            java.lang.String r3 = "fail to turn off WPC "
            com.sec.android.app.hwmoduletest.support.LtUtil.log_i(r0, r1, r3)
            android.os.Handler r0 = r6.mHandler
            r1 = 6
            r0.sendEmptyMessage(r1)
            return r2
        L_0x007f:
            boolean r0 = r6.wpcOffFirstTime
            if (r0 != 0) goto L_0x00a6
            int r0 = r6.WPC_OFF_DELAY
            long r3 = (long) r0
            com.sec.android.app.hwmoduletest.support.LtUtil.Sleep.sleep(r3)
            java.lang.String r0 = "MSTTest"
            java.lang.String r3 = "ChangeWPCStatus"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "off delay(wpc) : "
            r4.append(r5)
            int r5 = r6.WPC_OFF_DELAY
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            com.sec.android.app.hwmoduletest.support.LtUtil.log_i(r0, r3, r4)
            r6.WPC_OFF_DELAY = r1
            goto L_0x00af
        L_0x00a6:
            java.lang.String r0 = "MSTTest"
            java.lang.String r3 = "ChangeWPCStatus"
            java.lang.String r4 = "no delay(wpc)"
            com.sec.android.app.hwmoduletest.support.LtUtil.log_i(r0, r3, r4)
        L_0x00af:
            r6.wpcOffFirstTime = r2
        L_0x00b2:
            return r1
        */
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        LtUtil.log_i(CLASS_NAME, "onCreate", null);
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.msttest);
        if (!Feature.getBoolean(Feature.WPC_OFF_WHEN_MST)) {
            this.WPC_OFF_DELAY = 0;
        }
        mMstSysfsPath = Kernel.MST_DATA_TRANSMIT_PATH;
        StringBuilder sb = new StringBuilder();
        sb.append("mMstSysfsPath : ");
        sb.append(mMstSysfsPath);
        LtUtil.log_i(CLASS_NAME, "onCreate", sb.toString());
        String testCase = HwTestMenu.getTestCase(String.valueOf(34));
        if (testCase != null && "EMCTest".equalsIgnoreCase(testCase)) {
            this.mSupportEMCTest = Boolean.valueOf(true);
            this.mMstTestTitle = (TextView) findViewById(C0268R.C0269id.msttest_title);
            this.mMstTestTitle.setText("MST (only using for EMC Test)");
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("mSupportEMCTest : ");
        sb2.append(this.mSupportEMCTest);
        LtUtil.log_d(CLASS_NAME, "onCreate", sb2.toString());
        if (Feature.getBoolean(Feature.WPC_OFF_WHEN_MST)) {
            ChangeWPCStatus("0");
        }
        initUI();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        mMstDriverStatus = Kernel.read(mMstSysfsPath);
        StringBuilder sb = new StringBuilder();
        sb.append("mMstDriverStatus : ");
        sb.append(mMstDriverStatus);
        LtUtil.log_d(CLASS_NAME, "onResume", sb.toString());
        this.isThreadOff = false;
        if (mMstDriverStatus == null || !"waiting".equalsIgnoreCase(mMstDriverStatus)) {
            this.mHandler.sendEmptyMessage(0);
        } else {
            this.mHandler.sendEmptyMessage(1);
        }
        if (Kernel.isExistFileID(Kernel.USE_MST_MFC_IC) && "mfc_charger".equalsIgnoreCase(Kernel.read(Kernel.USE_MST_MFC_IC))) {
            this.isUseMFCIC = true;
        }
        if (isOqcsbftt) {
            NVAccessor.setNV(BaseActivity.NV_O_MST, NVAccessor.NV_VALUE_ENTER);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (!this.mSupportEMCTest.booleanValue()) {
            StringBuilder sb = new StringBuilder();
            sb.append("mSupportEMCTest : ");
            sb.append(this.mSupportEMCTest);
            sb.append(", Stop previous writing thread");
            LtUtil.log_d(CLASS_NAME, "onPause", sb.toString());
            if (this.mContinuousButtonThread != null) {
                this.mContinuousButtonThread.interrupt();
            }
            this.isThreadOff = true;
        }
        if (this.isUseMFCIC) {
            Kernel.write(mMstSysfsPath, "0");
        }
        if (Feature.getBoolean(Feature.WPC_OFF_WHEN_MST)) {
            ChangeWPCStatus(EgisFingerprint.MAJOR_VERSION);
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    private void initUI() {
        LtUtil.log_d(CLASS_NAME, "initUI", null);
        this.mSampleDataTrack1 = (TextView) findViewById(C0268R.C0269id.mst_sample_data_track_1);
        this.mSampleDataTrack2 = (TextView) findViewById(C0268R.C0269id.mst_sample_data_track_2);
        this.mButtonTrack1 = (Button) findViewById(C0268R.C0269id.btn_mst_track_1);
        this.mButtonTrack2 = (Button) findViewById(C0268R.C0269id.btn_mst_track_2);
        this.mButtonContinuousTrack1 = (Button) findViewById(C0268R.C0269id.btn_mst_continuous_track1);
        this.mButtonContinuousTrack2 = (Button) findViewById(C0268R.C0269id.btn_mst_continuous_track2);
        this.mButtonContinuousTrack1plus2 = (Button) findViewById(C0268R.C0269id.btn_mst_continuous_track1plus2);
        this.mButtonMSTOff = (Button) findViewById(C0268R.C0269id.btn_mst_off);
        this.mButtonTrack1.setOnClickListener(this);
        this.mButtonTrack2.setOnClickListener(this);
        this.mButtonContinuousTrack1.setOnClickListener(this);
        this.mButtonContinuousTrack2.setOnClickListener(this);
        this.mButtonContinuousTrack1plus2.setOnClickListener(this);
        this.mButtonMSTOff.setOnClickListener(this);
        this.mSampleDataTrack1.setText("%B5424180661705886^*****/******            ^121010100000025901000000383000000?;E?");
        this.mSampleDataTrack2.setText(";6010567836392988=25010005000060073930?=");
        this.mButtonTrack1.setEnabled(false);
        this.mButtonTrack2.setEnabled(false);
        this.mButtonContinuousTrack1.setEnabled(false);
        this.mButtonContinuousTrack2.setEnabled(false);
        this.mButtonContinuousTrack1plus2.setEnabled(false);
        this.mButtonMSTOff.setEnabled(false);
    }

    private boolean isConnectWirelessCharger() {
        String mstatus = Kernel.read(Kernel.WIRELESS_BATTERY);
        if (mstatus == null || !mstatus.contains(EgisFingerprint.MAJOR_VERSION)) {
            return false;
        }
        return true;
    }

    public void onClick(View v) {
        if (this.mContinuousButtonThread != null) {
            LtUtil.log_d(CLASS_NAME, "onClick", "Stop previous action, interrupt thread");
            this.mContinuousButtonThread.interrupt();
        }
        switch (v.getId()) {
            case C0268R.C0269id.btn_mst_track_1 /*2131296527*/:
                LtUtil.log_d(CLASS_NAME, "onClick", "click Track1 button");
                this.WPC_OFF_DELAY = 1200;
                if (Feature.getBoolean(Feature.WPC_OFF_WHEN_MST)) {
                    this.canBeTransferred = ChangeWPCStatus("0");
                }
                if (this.isUseMFCIC) {
                    if (this.isEnHigh || !isConnectWirelessCharger()) {
                        this.canBeTransferred = true;
                    } else {
                        this.canBeTransferred = false;
                        if (!this.shown_wpcOffDialog) {
                            this.shown_wpcOffDialog = true;
                            this.mHandler.sendEmptyMessage(8);
                        }
                    }
                }
                if (this.canBeTransferred) {
                    this.mHandler.sendEmptyMessage(2);
                    LtUtil.log_d(CLASS_NAME, "onClick", "Transmit OneTime Track1, writeValue : 2");
                    if (this.isUseMFCIC) {
                        Kernel.write(mMstSysfsPath, EgisFingerprint.MAJOR_VERSION);
                        this.isEnHigh = true;
                    }
                    Kernel.write(mMstSysfsPath, egis.optical.client.api.EgisFingerprint.MAJOR_VERSION);
                    if (this.isUseMFCIC) {
                        Kernel.write(mMstSysfsPath, "0");
                        this.isEnHigh = false;
                        return;
                    }
                    return;
                }
                return;
            case C0268R.C0269id.btn_mst_track_2 /*2131296528*/:
                LtUtil.log_d(CLASS_NAME, "onClick", "click Track2 button");
                this.WPC_OFF_DELAY = 1200;
                if (Feature.getBoolean(Feature.WPC_OFF_WHEN_MST)) {
                    this.canBeTransferred = ChangeWPCStatus("0");
                }
                if (this.isUseMFCIC) {
                    if (this.isEnHigh || !isConnectWirelessCharger()) {
                        this.canBeTransferred = true;
                    } else {
                        this.canBeTransferred = false;
                        if (!this.shown_wpcOffDialog) {
                            this.shown_wpcOffDialog = true;
                            this.mHandler.sendEmptyMessage(8);
                        }
                    }
                }
                if (this.canBeTransferred) {
                    this.mHandler.sendEmptyMessage(2);
                    LtUtil.log_d(CLASS_NAME, "onClick", "Transmit OneTime Track2, writeValue : 3");
                    if (this.isUseMFCIC) {
                        Kernel.write(mMstSysfsPath, EgisFingerprint.MAJOR_VERSION);
                        this.isEnHigh = true;
                    }
                    Kernel.write(mMstSysfsPath, "3");
                    if (this.isUseMFCIC) {
                        Kernel.write(mMstSysfsPath, "0");
                        this.isEnHigh = false;
                        return;
                    }
                    return;
                }
                return;
            case C0268R.C0269id.btn_mst_continuous_track1 /*2131296530*/:
                LtUtil.log_d(CLASS_NAME, "onClick", "click Continuous Track1 button");
                if (this.isUseMFCIC) {
                    if (this.isEnHigh || !isConnectWirelessCharger()) {
                        this.canBeTransferred = true;
                        Kernel.write(mMstSysfsPath, EgisFingerprint.MAJOR_VERSION);
                        this.isEnHigh = true;
                    } else {
                        this.canBeTransferred = false;
                        if (!this.shown_wpcOffDialog) {
                            this.shown_wpcOffDialog = true;
                            this.mHandler.sendEmptyMessage(8);
                        }
                    }
                }
                this.WPC_OFF_DELAY = 1200;
                this.mContinuousButtonThread = new Thread(new Runnable() {
                    public void run() {
                        MSTTest.this.mHandler.sendEmptyMessage(3);
                        LtUtil.log_d(MSTTest.CLASS_NAME, "mContinuousButtonThread.run", "Transmit Continuous Track1, writeValue : 2");
                        MSTTest.mIsThreadWriting = Boolean.valueOf(true);
                        while (!Thread.currentThread().isInterrupted() && !MSTTest.this.isThreadOff) {
                            try {
                                if (Feature.getBoolean(Feature.WPC_OFF_WHEN_MST)) {
                                    MSTTest.this.canBeTransferred = MSTTest.this.ChangeWPCStatus("0");
                                }
                                if (MSTTest.this.canBeTransferred) {
                                    Kernel.write(MSTTest.mMstSysfsPath, egis.optical.client.api.EgisFingerprint.MAJOR_VERSION);
                                    Thread.sleep(1000);
                                }
                            } catch (InterruptedException e) {
                            } catch (Throwable th) {
                                MSTTest.mIsThreadWriting = Boolean.valueOf(false);
                                LtUtil.log_d(MSTTest.CLASS_NAME, "mContinuousButtonThread.run", "Continuous Track1 Terminated");
                                throw th;
                            }
                        }
                        MSTTest.mIsThreadWriting = Boolean.valueOf(false);
                        LtUtil.log_d(MSTTest.CLASS_NAME, "mContinuousButtonThread.run", "Continuous Track1 Terminated");
                    }
                });
                if (this.canBeTransferred) {
                    this.mContinuousButtonThread.setDaemon(true);
                    this.mContinuousButtonThread.start();
                    return;
                }
                return;
            case C0268R.C0269id.btn_mst_continuous_track2 /*2131296531*/:
                LtUtil.log_d(CLASS_NAME, "onClick", "click Continuous Track2 button");
                if (this.isUseMFCIC) {
                    if (this.isEnHigh || !isConnectWirelessCharger()) {
                        this.canBeTransferred = true;
                        Kernel.write(mMstSysfsPath, EgisFingerprint.MAJOR_VERSION);
                        this.isEnHigh = true;
                    } else {
                        this.canBeTransferred = false;
                        if (!this.shown_wpcOffDialog) {
                            this.shown_wpcOffDialog = true;
                            this.mHandler.sendEmptyMessage(8);
                        }
                    }
                }
                this.WPC_OFF_DELAY = 1200;
                this.mContinuousButtonThread = new Thread(new Runnable() {
                    public void run() {
                        MSTTest.this.mHandler.sendEmptyMessage(4);
                        LtUtil.log_d(MSTTest.CLASS_NAME, "mContinuousButtonThread.run", "Transmit Continuous Track2, writeValue : 3");
                        MSTTest.mIsThreadWriting = Boolean.valueOf(true);
                        while (!Thread.currentThread().isInterrupted() && !MSTTest.this.isThreadOff) {
                            try {
                                if (Feature.getBoolean(Feature.WPC_OFF_WHEN_MST)) {
                                    MSTTest.this.canBeTransferred = MSTTest.this.ChangeWPCStatus("0");
                                }
                                if (MSTTest.this.canBeTransferred) {
                                    Kernel.write(MSTTest.mMstSysfsPath, "3");
                                    Thread.sleep(1000);
                                }
                            } catch (InterruptedException e) {
                            } catch (Throwable th) {
                                MSTTest.mIsThreadWriting = Boolean.valueOf(false);
                                LtUtil.log_d(MSTTest.CLASS_NAME, "mContinuousButtonThread.run", "Continuous Track2 Terminated");
                                throw th;
                            }
                        }
                        MSTTest.mIsThreadWriting = Boolean.valueOf(false);
                        LtUtil.log_d(MSTTest.CLASS_NAME, "mContinuousButtonThread.run", "Continuous Track2 Terminated");
                    }
                });
                if (this.canBeTransferred) {
                    this.mContinuousButtonThread.setDaemon(true);
                    this.mContinuousButtonThread.start();
                    return;
                }
                return;
            case C0268R.C0269id.btn_mst_continuous_track1plus2 /*2131296532*/:
                LtUtil.log_d(CLASS_NAME, "onClick", "click Continuous Track1+2 button");
                if (this.isUseMFCIC) {
                    if (this.isEnHigh || !isConnectWirelessCharger()) {
                        this.canBeTransferred = true;
                        Kernel.write(mMstSysfsPath, EgisFingerprint.MAJOR_VERSION);
                        this.isEnHigh = true;
                    } else {
                        this.canBeTransferred = false;
                        if (!this.shown_wpcOffDialog) {
                            this.shown_wpcOffDialog = true;
                            this.mHandler.sendEmptyMessage(8);
                        }
                    }
                }
                this.WPC_OFF_DELAY = 1200;
                this.mContinuousButtonThread = new Thread(new Runnable() {
                    public void run() {
                        MSTTest.this.mHandler.sendEmptyMessage(5);
                        LtUtil.log_d(MSTTest.CLASS_NAME, "mContinuousButtonThread.run", "Transmit Continuous Track1+2, writeValue : 2,3");
                        MSTTest.mIsThreadWriting = Boolean.valueOf(true);
                        while (!Thread.currentThread().isInterrupted() && !MSTTest.this.isThreadOff) {
                            try {
                                if (Feature.getBoolean(Feature.WPC_OFF_WHEN_MST)) {
                                    MSTTest.this.canBeTransferred = MSTTest.this.ChangeWPCStatus("0");
                                }
                                if (MSTTest.this.canBeTransferred) {
                                    Kernel.write(MSTTest.mMstSysfsPath, egis.optical.client.api.EgisFingerprint.MAJOR_VERSION);
                                    Thread.sleep(1000);
                                    Kernel.write(MSTTest.mMstSysfsPath, "3");
                                    Thread.sleep(1000);
                                }
                            } catch (InterruptedException e) {
                            } catch (Throwable th) {
                                MSTTest.mIsThreadWriting = Boolean.valueOf(false);
                                LtUtil.log_d(MSTTest.CLASS_NAME, "mContinuousButtonThread.run", "Continuous Track1+2 Terminated");
                                throw th;
                            }
                        }
                        MSTTest.mIsThreadWriting = Boolean.valueOf(false);
                        LtUtil.log_d(MSTTest.CLASS_NAME, "mContinuousButtonThread.run", "Continuous Track1+2 Terminated");
                    }
                });
                if (this.canBeTransferred) {
                    this.mContinuousButtonThread.setDaemon(true);
                    this.mContinuousButtonThread.start();
                    return;
                }
                return;
            case C0268R.C0269id.btn_mst_off /*2131296533*/:
                LtUtil.log_d(CLASS_NAME, "onClick", "click Off button");
                if (this.isUseMFCIC) {
                    Kernel.write(mMstSysfsPath, "0");
                    this.isEnHigh = false;
                }
                this.mHandler.sendEmptyMessage(1);
                return;
            default:
                return;
        }
    }
}
