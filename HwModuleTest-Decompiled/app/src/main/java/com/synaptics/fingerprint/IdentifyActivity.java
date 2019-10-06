package com.synaptics.fingerprint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.widget.LockPatternUtils;
import com.goodix.cap.fingerprint.Constants;
import com.synaptics.fingerprint.ConfigReader.ConfigData;
import com.synaptics.fingerprint.FingerprintCore.EventListener;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;

public class IdentifyActivity extends Activity implements EventListener {
    private static final int CONFIRM_EXISTING_REQUEST = 100;
    private static final boolean DBG = true;
    public static String FINGER_ACTION_LABEL = "Please swipe";
    public static final int REQUEST_CODE = 101;
    private static final String TAG = "IdentifyActivity";
    public static final String USER_ID = "userId";
    public static final String VCS_RESULT = "result";
    /* access modifiers changed from: private */
    public static int mTotalFailedPatternAttempts = 0;
    private View mActivityView;
    private Button mAlternateButton;
    private Button mCancelButton;
    /* access modifiers changed from: private */
    public CountDownTimer mCountdownTimer = null;
    /* access modifiers changed from: private */
    public Fingerprint mFingerprint;
    private Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public Semaphore mLock = new Semaphore(1);
    private LockPatternUtils mLockPatternUtils;
    /* access modifiers changed from: private */
    public PowerManager mPowerMgr;
    private SensorView mSensorView;
    /* access modifiers changed from: private */
    public TextView mStatusView;
    private Thread mThread;
    /* access modifiers changed from: private */
    public TextView mTimeoutText;
    /* access modifiers changed from: private */
    public String mUserId = "";

    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        this.mActivityView = getPrepareView();
        setContentView(this.mActivityView);
        setTitle("Identify");
        this.mPowerMgr = (PowerManager) getSystemService("power");
        this.mLockPatternUtils = new LockPatternUtils(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String userId = extras.getString(USER_ID);
            if (userId != null) {
                this.mUserId = userId;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Log.i(TAG, "onResume()");
        super.onResume();
        ConfigData configData = ConfigReader.getData();
        this.mSensorView.setLocation(configData);
        if (!(configData == null || configData.fingerActionGenericLabel == null)) {
            FINGER_ACTION_LABEL = configData.fingerActionGenericLabel;
        }
        startIdentify();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();
        cancel();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    public void onBackPressed() {
        Log.i(TAG, "onBackPressed()");
        setResult(0, 103);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            setResult(-1, 0);
        } else {
            setResult(resultCode, 103);
        }
    }

    private View getPrepareView() {
        RelativeLayout root = new RelativeLayout(this);
        root.setBackgroundColor(getResources().getColor(17170446));
        LayoutParams trlp = new LayoutParams(-1, -2);
        trlp.setMargins(0, 5, 0, 5);
        RelativeLayout btnLayout = new RelativeLayout(this);
        int vId = 100 + 1;
        btnLayout.setId(100);
        btnLayout.setLayoutParams(trlp);
        root.addView(btnLayout);
        LayoutParams cblp = new LayoutParams(200, -2);
        cblp.addRule(9, 1);
        this.mCancelButton = new Button(this);
        this.mCancelButton.setText("Cancel");
        this.mCancelButton.setLayoutParams(cblp);
        int vId2 = vId + 1;
        this.mCancelButton.setId(vId);
        this.mCancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                IdentifyActivity.this.setResult(0, 103);
            }
        });
        btnLayout.addView(this.mCancelButton);
        LayoutParams ablp = new LayoutParams(200, -2);
        ablp.addRule(11, 1);
        this.mAlternateButton = new Button(this);
        this.mAlternateButton.setText("Alternate");
        this.mAlternateButton.setLayoutParams(ablp);
        int vId3 = vId2 + 1;
        this.mAlternateButton.setId(vId2);
        this.mAlternateButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                IdentifyActivity.this.handleNext();
            }
        });
        btnLayout.addView(this.mAlternateButton);
        View topSpacer = new View(this);
        LayoutParams tslp = new LayoutParams(-1, 2);
        tslp.addRule(3, btnLayout.getId());
        tslp.setMargins(0, 5, 0, 5);
        topSpacer.setLayoutParams(tslp);
        int vId4 = vId3 + 1;
        topSpacer.setId(vId3);
        topSpacer.setBackgroundResource(17301524);
        root.addView(topSpacer);
        this.mTimeoutText = new TextView(this);
        this.mTimeoutText.setText("");
        LayoutParams tolp = new LayoutParams(-2, -2);
        tolp.addRule(14);
        tolp.addRule(3, topSpacer.getId());
        this.mTimeoutText.setLayoutParams(tolp);
        int vId5 = vId4 + 1;
        this.mTimeoutText.setId(vId4);
        root.addView(this.mTimeoutText);
        this.mStatusView = new TextView(this);
        this.mStatusView.setText("");
        LayoutParams lp = new LayoutParams(-2, -2);
        lp.addRule(14);
        lp.addRule(12, 1);
        lp.setMargins(0, 5, 0, 5);
        this.mStatusView.setLayoutParams(lp);
        this.mStatusView.setTextAppearance(this, 16973892);
        int vId6 = vId5 + 1;
        this.mStatusView.setId(vId5);
        root.addView(this.mStatusView);
        View bottomSpacer = new View(this);
        LayoutParams bslp = new LayoutParams(-1, 2);
        bslp.addRule(2, this.mStatusView.getId());
        bslp.setMargins(0, 5, 0, 5);
        bottomSpacer.setLayoutParams(bslp);
        int vId7 = vId6 + 1;
        bottomSpacer.setId(vId6);
        bottomSpacer.setBackgroundResource(17301524);
        root.addView(bottomSpacer);
        InputStream is = IdentifyActivity.class.getClassLoader().getResourceAsStream("res/drawable/fingerprint.png");
        if (is != null) {
            Bitmap bmp = BitmapFactory.decodeStream(is);
            ImageView mImageFP = new ImageView(this);
            LayoutParams layoutParams = cblp;
            LayoutParams layoutParams2 = trlp;
            LayoutParams ilp = new LayoutParams(-2, -2);
            ilp.addRule(14);
            LayoutParams layoutParams3 = bslp;
            ilp.addRule(3, this.mTimeoutText.getId());
            ilp.addRule(2, bottomSpacer.getId());
            ilp.setMargins(10, 10, 10, 10);
            mImageFP.setLayoutParams(ilp);
            int i = vId7 + 1;
            mImageFP.setId(vId7);
            mImageFP.setScaleType(ScaleType.CENTER_INSIDE);
            mImageFP.setImageBitmap(bmp);
            root.addView(mImageFP);
        } else {
            LayoutParams layoutParams4 = trlp;
            LayoutParams layoutParams5 = bslp;
            int i2 = vId7;
        }
        this.mSensorView = new SensorView(this, null);
        root.addView(this.mSensorView);
        return root;
    }

    /* access modifiers changed from: private */
    public void setUiString(String msg) {
        final String s = msg;
        Log.i(TAG, s);
        runOnUiThread(new Runnable() {
            public void run() {
                IdentifyActivity.this.mStatusView.setText(s);
            }
        });
    }

    /* access modifiers changed from: private */
    public void startIdentify() {
        Log.i(TAG, "startIdentify()");
        this.mThread = new Thread(new Runnable() {
            public void run() {
                if (IdentifyActivity.this.mFingerprint == null) {
                    IdentifyActivity.this.mFingerprint = new Fingerprint(IdentifyActivity.this, IdentifyActivity.this);
                }
                int result = IdentifyActivity.this.mFingerprint.identify(IdentifyActivity.this.mUserId);
                if (result == 0) {
                    IdentifyActivity.this.setUiString("Please wait");
                } else {
                    String str = IdentifyActivity.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Identify() failed, result = ");
                    sb.append(result);
                    Log.e(str, sb.toString());
                    IdentifyActivity.this.setUiString("Failed to intialize Identify operation.");
                }
                IdentifyActivity.this.mLock.release();
            }
        });
        try {
            this.mLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.mThread.start();
    }

    private void startIdentifyWithDelay() {
        Log.i(TAG, "startIdentifyWithDelay()");
        this.mHandler.removeCallbacksAndMessages(null);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                IdentifyActivity.this.startIdentify();
            }
        }, 1500);
    }

    private void cancel() {
        Log.i(TAG, "cancel()");
        this.mHandler.removeCallbacksAndMessages(null);
        stopCountdownTimer();
        this.mThread = new Thread(new Runnable() {
            public void run() {
                IdentifyActivity.this.stopCountdownTimer();
                if (IdentifyActivity.this.mFingerprint != null) {
                    IdentifyActivity.this.mFingerprint.cancel();
                    IdentifyActivity.this.mFingerprint.cleanUp();
                    IdentifyActivity.this.mFingerprint = null;
                }
                IdentifyActivity.this.mLock.release();
            }
        });
        try {
            this.mLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.mThread.start();
    }

    public void onEvent(FingerprintEvent fpEvent) {
        if (fpEvent == null) {
            Log.e(TAG, "Invalid event data.");
            return;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onEventsCB(): id = ");
        sb.append(fpEvent.eventId);
        Log.i(str, sb.toString());
        String statusMessage = "";
        int i = fpEvent.eventId;
        if (i == 101) {
            statusMessage = FINGER_ACTION_LABEL;
        } else if (i != 401) {
            switch (i) {
                case 403:
                case 404:
                    Log.i(TAG, "EVT_EIV_STATUS_IDENTIFY/VERIFY_COMPLETED");
                    if (fpEvent.eventData != null) {
                        if (fpEvent.eventData instanceof IdentifyResult) {
                            IdentifyResult identifyResult = (IdentifyResult) fpEvent.eventData;
                            if (identifyResult.result != 0) {
                                Log.i(TAG, "Identify/verify failed");
                                boolean reverify = false;
                                int i2 = identifyResult.result;
                                if (i2 == 2) {
                                    statusMessage = "Sensor not available";
                                    reverify = DBG;
                                } else if (i2 != 103) {
                                    statusMessage = "Identify failed, User not found.";
                                    mTotalFailedPatternAttempts++;
                                    if (mTotalFailedPatternAttempts >= 5) {
                                        handleAttemptLockout(Constants.TEST_TIMEOUT_MS);
                                    } else {
                                        reverify = DBG;
                                    }
                                    String str2 = TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("Fingerprint verify failed, Result: ");
                                    sb2.append(identifyResult.result);
                                    Log.e(str2, sb2.toString());
                                } else {
                                    statusMessage = "Operation has been cancelled";
                                }
                                if (reverify) {
                                    startIdentifyWithDelay();
                                    break;
                                }
                            } else {
                                statusMessage = "Identify success, User found.";
                                mTotalFailedPatternAttempts = 0;
                                this.mHandler.postDelayed(new Runnable() {
                                    public void run() {
                                        IdentifyActivity.this.setResult(-1, 0);
                                    }
                                }, 500);
                                break;
                            }
                        } else {
                            Log.e(TAG, "Invalid event data");
                            return;
                        }
                    } else {
                        Log.e(TAG, "Invalid event data");
                        return;
                    }
                    break;
            }
        } else {
            statusMessage = "Captured fingerprint";
        }
        if (!statusMessage.equalsIgnoreCase("")) {
            setUiString(statusMessage);
        }
    }

    /* access modifiers changed from: private */
    public void setResult(int resultCode, int identifyResult) {
        Intent intent = new Intent();
        intent.putExtra(VCS_RESULT, identifyResult);
        setResult(resultCode, intent);
        finish();
    }

    /* access modifiers changed from: private */
    public void handleNext() {
        Log.i(TAG, "handleNext()");
        int altUnlockMethod = 0;
        try {
            Method methodAltUnlock = LockPatternUtils.class.getMethod("getAltUnlockMethod", new Class[0]);
            if (methodAltUnlock != null) {
                altUnlockMethod = ((Integer) methodAltUnlock.invoke(this.mLockPatternUtils, new Object[0])).intValue();
            }
            if (altUnlockMethod == 0) {
                Toast.makeText(this, "Alternate unlock method was not set!", 1).show();
                return;
            }
            cancel();
            String activityClassName = "";
            if (altUnlockMethod == 65536) {
                activityClassName = "com.android.settings.ConfirmLockPattern";
            } else if (altUnlockMethod == 131072 || altUnlockMethod == 262144 || altUnlockMethod == 327680) {
                activityClassName = "com.android.settings.ConfirmLockPassword";
            }
            if (!activityClassName.equals("")) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.settings");
                intent.setClassName("com.android.settings", activityClassName);
                intent.setFlags(536870912);
                try {
                    startActivityForResult(intent, 100);
                } catch (SecurityException se) {
                    Log.e(TAG, se.getMessage());
                    Toast.makeText(this, "Alternate unlock screen doesn't have permission to launch from external app.", 1).show();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void handleAttemptLockout(long elapsedRealtimeDeadline) {
        this.mHandler.post(new Runnable() {
            public void run() {
                IdentifyActivity identifyActivity = IdentifyActivity.this;
                C03161 r1 = new CountDownTimer(Constants.TEST_TIMEOUT_MS, 1000) {
                    public void onTick(long millisUntilFinished) {
                        if (IdentifyActivity.this.mPowerMgr.isScreenOn()) {
                            IdentifyActivity.this.mStatusView.setText("Please wait");
                            int secondsRemaining = (int) (millisUntilFinished / 1000);
                            TextView access$1100 = IdentifyActivity.this.mTimeoutText;
                            StringBuilder sb = new StringBuilder();
                            sb.append("Try again in ");
                            sb.append(secondsRemaining);
                            sb.append(" seconds.");
                            access$1100.setText(sb.toString());
                        }
                    }

                    public void onFinish() {
                        if (IdentifyActivity.this.mPowerMgr.isScreenOn()) {
                            IdentifyActivity.mTotalFailedPatternAttempts = 0;
                            IdentifyActivity.this.mTimeoutText.setText("");
                            IdentifyActivity.this.startIdentify();
                        }
                    }
                };
                identifyActivity.mCountdownTimer = r1.start();
            }
        });
    }

    /* access modifiers changed from: private */
    public void stopCountdownTimer() {
        if (this.mCountdownTimer != null) {
            this.mCountdownTimer.cancel();
        }
        mTotalFailedPatternAttempts = 0;
    }
}
