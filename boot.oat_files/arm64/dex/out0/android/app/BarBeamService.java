package android.app;

import android.app.IBarBeamService.Stub;
import android.bluetooth.BluetoothHidDevice;
import android.content.Context;
import android.net.ProxyInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class BarBeamService extends Stub {
    static boolean FAIL = false;
    private static final String LED_PATH = "/sys/class/sec/led/led_b";
    private static final int MIN_LED_TIME = 500;
    private static final String PERMISSION_BARCODE_READ = "com.samsung.permission.BARCODE_READ";
    static boolean SUCCESS = true;
    private static final String TAG = "BarBeamService";
    private static boolean bEnabled;
    static boolean result;
    private final byte DATA_COMMAND = (byte) 0;
    private final int LED_NOTIF_ID = 5160;
    private final int MAX_SEQUENCE_REGISTER = 18;
    private final int MSG_START_LED_NOTIFY = 30;
    private final int MSG_STOP_BEAMING = 50;
    private final int MSG_STOP_LED_NOTIFY = 40;
    private final byte NUM_HOPS_VERSION_COMMAND = BluetoothHidDevice.SUBCLASS1_MOUSE;
    private final byte SEQUENCE_REGISTER_LENGTH = (byte) 7;
    private final byte SEQUENCE_REGISTER_START = (byte) -127;
    private final byte START_STOP_COMMAND = (byte) -1;
    private final String SYSFS_BARCODE_CONTROL = "/sys/class/sec/sec_barcode_emul/barcode_send";
    private final String SYSFS_BARCODE_ENABLE = "/sys/class/sensors/proximity_sensor/barcode_emul_en";
    private final String SYSFS_BARCODE_LED_STATUS = "/sys/class/sec/sec_barcode_emul/barcode_led_status";
    private IBarBeamListener callbacks = null;
    private boolean isRunning;
    private byte mBeamLength = (byte) 0;
    private final Context mContext;
    private Timer mLEDTimer = null;
    ArrayList<Listener> mListeners = new ArrayList();
    Handler mMsgHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 30) {
                Log.d(BarBeamService.TAG, "Led in handler : false");
                BarBeamService.this.stopBlinkLED();
                synchronized (BarBeamService.this.mListeners) {
                    Iterator i$ = BarBeamService.this.mListeners.iterator();
                    while (i$.hasNext()) {
                        ((Listener) i$.next()).onBeamingStoppped();
                    }
                    BarBeamService.this.mListeners.notify();
                }
            }
            if (msg.what == 40) {
                Log.d(BarBeamService.TAG, "Led in handler : true");
                BarBeamService.this.blinkLED();
            }
            if (msg.what == 50) {
                Log.d(BarBeamService.TAG, "StopBeaming by no binder");
                BarBeamService.this.stopBeaming();
            }
        }
    };
    CheckStatusThread mcheckstatusThread = null;

    private final class CheckStatusThread extends Thread {
        public Handler mHandler;
        public Handler mMainHandler;

        public CheckStatusThread(Handler handler) {
            this.mMainHandler = handler;
        }

        public void run() {
            Looper.prepare();
            this.mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    if (msg.what == 30) {
                        CheckStatusThread.this.mMainHandler.sendEmptyMessageDelayed(40, 0);
                        Log.d(BarBeamService.TAG, "check status ++");
                        do {
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                            }
                        } while (BarBeamService.this.getCurrentStatus());
                        Log.d(BarBeamService.TAG, "check status --");
                        BarBeamService.this.isRunning = false;
                        CheckStatusThread.this.mMainHandler.sendEmptyMessageDelayed(30, 0);
                    }
                }
            };
            Looper.loop();
        }
    }

    private final class Listener implements DeathRecipient {
        final IBinder mToken;

        Listener(IBinder token) {
            this.mToken = token;
        }

        public void binderDied() {
            synchronized (BarBeamService.this.mListeners) {
                BarBeamService.this.mListeners.remove(this);
                Log.d(BarBeamService.TAG, "  .binderDied : " + BarBeamService.this.mListeners.size());
                this.mToken.unlinkToDeath(this, 0);
                BarBeamService.this.mListeners.notify();
                if (BarBeamService.this.mListeners.size() == 0) {
                    BarBeamService.this.mMsgHandler.sendEmptyMessageDelayed(50, 0);
                }
            }
        }

        public void onBeamingStoppped() {
            try {
                IBarBeamListener.Stub.asInterface(this.mToken).onBeamingStoppped();
            } catch (RemoteException e) {
                Log.e(BarBeamService.TAG, "Failed onBeamingStoppped", e);
            }
        }

        public void onBeamingStarted() {
            try {
                IBarBeamListener.Stub.asInterface(this.mToken).onBeamingStarted();
            } catch (RemoteException e) {
                Log.e(BarBeamService.TAG, "Failed onBeamingStarted", e);
            }
        }
    }

    private void blinkLED() {
        Log.i(TAG, "blinkLED ");
        stopBlinkLED();
        if (swtichLED(true)) {
            this.mLEDTimer = new Timer();
            this.mLEDTimer.schedule(new TimerTask() {
                boolean bSwitch = false;

                public void run() {
                    BarBeamService.this.swtichLED(this.bSwitch);
                    this.bSwitch = !this.bSwitch;
                }
            }, 500, 500);
        }
    }

    private void stopBlinkLED() {
        if (this.mLEDTimer != null) {
            this.mLEDTimer.cancel();
            this.mLEDTimer = null;
        }
        swtichLED(false);
    }

    private synchronized boolean swtichLED(boolean enable) {
        Throwable th;
        boolean ret = false;
        FileWriter fwLED = null;
        try {
            FileWriter fwLED2 = new FileWriter(LED_PATH);
            if (enable) {
                try {
                    fwLED2.write("40");
                } catch (FileNotFoundException e) {
                    fwLED = fwLED2;
                    try {
                        Log.e(TAG, "No SvcLED");
                        if (fwLED != null) {
                            try {
                                fwLED.close();
                            } catch (IOException e2) {
                                Log.e(TAG, "No SvcLED");
                            } catch (Throwable th2) {
                                th = th2;
                                throw th;
                            }
                        }
                        return ret;
                    } catch (Throwable th3) {
                        th = th3;
                        if (fwLED != null) {
                            try {
                                fwLED.close();
                            } catch (IOException e3) {
                                Log.e(TAG, "No SvcLED");
                            }
                        }
                        throw th;
                    }
                } catch (IOException e4) {
                    fwLED = fwLED2;
                    Log.e(TAG, "No SvcLED");
                    if (fwLED != null) {
                        try {
                            fwLED.close();
                        } catch (IOException e5) {
                            Log.e(TAG, "No SvcLED");
                        }
                    }
                    return ret;
                } catch (Throwable th4) {
                    th = th4;
                    fwLED = fwLED2;
                    if (fwLED != null) {
                        fwLED.close();
                    }
                    throw th;
                }
            }
            fwLED2.write("0");
            fwLED2.flush();
            ret = true;
            if (fwLED2 != null) {
                try {
                    fwLED2.close();
                    fwLED = fwLED2;
                } catch (IOException e6) {
                    Log.e(TAG, "No SvcLED");
                    fwLED = fwLED2;
                } catch (Throwable th5) {
                    th = th5;
                    fwLED = fwLED2;
                    throw th;
                }
            }
        } catch (FileNotFoundException e7) {
            Log.e(TAG, "No SvcLED");
            if (fwLED != null) {
                fwLED.close();
            }
            return ret;
        } catch (IOException e8) {
            Log.e(TAG, "No SvcLED");
            if (fwLED != null) {
                fwLED.close();
            }
            return ret;
        }
    }

    public boolean getCurrentStatus() {
        FileNotFoundException ex;
        IOException ex2;
        Exception ex3;
        Throwable th;
        StringBuffer strstatus = new StringBuffer();
        BufferedReader reader = null;
        String ver = ProxyInfo.LOCAL_EXCL_LIST;
        boolean result = SUCCESS;
        synchronized (this) {
            try {
                BufferedReader reader2 = new BufferedReader(new FileReader("/sys/class/sec/sec_barcode_emul/barcode_led_status"), 1024);
                while (true) {
                    try {
                        String temp = reader2.readLine();
                        if (temp == null) {
                            break;
                        }
                        strstatus.append(temp);
                    } catch (FileNotFoundException e) {
                        ex = e;
                        reader = reader2;
                    } catch (IOException e2) {
                        ex2 = e2;
                        reader = reader2;
                    } catch (Exception e3) {
                        ex3 = e3;
                        reader = reader2;
                    } catch (Throwable th2) {
                        th = th2;
                        reader = reader2;
                    }
                }
                if (reader2 != null) {
                    try {
                        ver = strstatus.toString();
                        reader2.close();
                        if (ver.equals(WifiEnterpriseConfig.ENGINE_ENABLE)) {
                            result = SUCCESS;
                        } else {
                            result = FAIL;
                        }
                        reader = reader2;
                    } catch (FileNotFoundException ex4) {
                        ex4.printStackTrace();
                        reader = reader2;
                    } catch (Throwable th3) {
                        th = th3;
                        reader = reader2;
                        throw th;
                    }
                }
            } catch (FileNotFoundException e4) {
                ex4 = e4;
                try {
                    ex4.printStackTrace();
                    result = FAIL;
                    if (reader != null) {
                        try {
                            ver = strstatus.toString();
                            reader.close();
                            result = ver.equals(WifiEnterpriseConfig.ENGINE_ENABLE) ? SUCCESS : FAIL;
                        } catch (IOException ex22) {
                            ex22.printStackTrace();
                        } catch (Throwable th4) {
                            th = th4;
                        }
                    }
                    return result;
                } catch (Throwable th5) {
                    th = th5;
                    if (reader != null) {
                        try {
                            ver = strstatus.toString();
                            reader.close();
                            if (ver.equals(WifiEnterpriseConfig.ENGINE_ENABLE)) {
                                result = SUCCESS;
                            } else {
                                result = FAIL;
                            }
                        } catch (IOException ex222) {
                            ex222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (IOException e5) {
                ex222 = e5;
                ex222.printStackTrace();
                result = FAIL;
                if (reader != null) {
                    try {
                        ver = strstatus.toString();
                        reader.close();
                        result = ver.equals(WifiEnterpriseConfig.ENGINE_ENABLE) ? SUCCESS : FAIL;
                    } catch (IOException ex2222) {
                        ex2222.printStackTrace();
                    }
                }
                return result;
            } catch (Exception e6) {
                ex3 = e6;
                ex3.printStackTrace();
                result = FAIL;
                if (reader != null) {
                    try {
                        ver = strstatus.toString();
                        reader.close();
                        result = ver.equals(WifiEnterpriseConfig.ENGINE_ENABLE) ? SUCCESS : FAIL;
                    } catch (IOException ex22222) {
                        ex22222.printStackTrace();
                    }
                }
                return result;
            }
            return result;
        }
    }

    public BarBeamService(Context context) {
        Log.i(TAG, "BarBeamService : Create");
        this.mContext = context;
        this.isRunning = false;
        this.mcheckstatusThread = new CheckStatusThread(this.mMsgHandler);
        this.mcheckstatusThread.start();
    }

    public void startBeaming() {
        Exception ex;
        Throwable th;
        FileNotFoundException ex2;
        FileOutputStream out;
        Iterator i$;
        IOException ex3;
        Log.d(TAG, "startBeaming");
        this.mContext.enforceCallingOrSelfPermission("com.samsung.permission.BARCODE_READ", null);
        result = SUCCESS;
        FileWriter out_en = null;
        FileOutputStream out2 = null;
        byte[] data = new byte[2];
        char[] flag = new char[1];
        synchronized (this) {
            try {
                FileWriter out_en2 = new FileWriter("/sys/class/sensors/proximity_sensor/barcode_emul_en");
                try {
                    flag[0] = '1';
                    out_en2.write(flag);
                    out_en2.flush();
                    if (out_en2 != null) {
                        try {
                            out_en2.close();
                            out_en = out_en2;
                        } catch (Exception ex4) {
                            ex4.printStackTrace();
                            out_en = out_en2;
                        } catch (Throwable th2) {
                            th = th2;
                            out_en = out_en2;
                            throw th;
                        }
                    }
                } catch (FileNotFoundException e) {
                    ex2 = e;
                    out_en = out_en2;
                    try {
                        ex2.printStackTrace();
                        result = FAIL;
                        if (out_en != null) {
                            try {
                                out_en.close();
                            } catch (Exception ex42) {
                                ex42.printStackTrace();
                            } catch (Throwable th3) {
                                th = th3;
                                throw th;
                            }
                        }
                        data[0] = (byte) -1;
                        data[1] = this.mBeamLength;
                        out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                        out.write(data);
                        out.flush();
                        Log.d(TAG, "startBarBeam : " + this.mBeamLength);
                        if (out != null) {
                            try {
                                out.close();
                                if (!this.isRunning) {
                                    synchronized (this.mListeners) {
                                        i$ = this.mListeners.iterator();
                                        while (i$.hasNext()) {
                                            ((Listener) i$.next()).onBeamingStarted();
                                        }
                                        this.mListeners.notify();
                                    }
                                    this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                                }
                                this.isRunning = true;
                                out2 = out;
                            } catch (Exception ex422) {
                                try {
                                    ex422.printStackTrace();
                                    out2 = out;
                                } catch (Throwable th4) {
                                    th = th4;
                                    out2 = out;
                                    throw th;
                                }
                            }
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        if (out_en != null) {
                            try {
                                out_en.close();
                            } catch (Exception ex4222) {
                                ex4222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e2) {
                    ex3 = e2;
                    out_en = out_en2;
                    ex3.printStackTrace();
                    result = FAIL;
                    if (out_en != null) {
                        try {
                            out_en.close();
                        } catch (Exception ex42222) {
                            ex42222.printStackTrace();
                        }
                    }
                    data[0] = (byte) -1;
                    data[1] = this.mBeamLength;
                    out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                    out.write(data);
                    out.flush();
                    Log.d(TAG, "startBarBeam : " + this.mBeamLength);
                    if (out != null) {
                    } else {
                        out.close();
                        if (this.isRunning) {
                            synchronized (this.mListeners) {
                                i$ = this.mListeners.iterator();
                                while (i$.hasNext()) {
                                    ((Listener) i$.next()).onBeamingStarted();
                                }
                                this.mListeners.notify();
                            }
                            this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                        }
                        this.isRunning = true;
                        out2 = out;
                    }
                } catch (Exception e3) {
                    ex42222 = e3;
                    out_en = out_en2;
                    ex42222.printStackTrace();
                    result = FAIL;
                    if (out_en != null) {
                        try {
                            out_en.close();
                        } catch (Exception ex422222) {
                            ex422222.printStackTrace();
                        }
                    }
                    data[0] = (byte) -1;
                    data[1] = this.mBeamLength;
                    out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                    out.write(data);
                    out.flush();
                    Log.d(TAG, "startBarBeam : " + this.mBeamLength);
                    if (out != null) {
                        out.close();
                        if (this.isRunning) {
                            synchronized (this.mListeners) {
                                i$ = this.mListeners.iterator();
                                while (i$.hasNext()) {
                                    ((Listener) i$.next()).onBeamingStarted();
                                }
                                this.mListeners.notify();
                            }
                            this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                        }
                        this.isRunning = true;
                        out2 = out;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    out_en = out_en2;
                    if (out_en != null) {
                        out_en.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e4) {
                ex2 = e4;
                ex2.printStackTrace();
                result = FAIL;
                if (out_en != null) {
                    out_en.close();
                }
                data[0] = (byte) -1;
                data[1] = this.mBeamLength;
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                out.write(data);
                out.flush();
                Log.d(TAG, "startBarBeam : " + this.mBeamLength);
                if (out != null) {
                } else {
                    out.close();
                    if (this.isRunning) {
                        synchronized (this.mListeners) {
                            i$ = this.mListeners.iterator();
                            while (i$.hasNext()) {
                                ((Listener) i$.next()).onBeamingStarted();
                            }
                            this.mListeners.notify();
                        }
                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                    }
                    this.isRunning = true;
                    out2 = out;
                }
            } catch (IOException e5) {
                ex3 = e5;
                ex3.printStackTrace();
                result = FAIL;
                if (out_en != null) {
                    out_en.close();
                }
                data[0] = (byte) -1;
                data[1] = this.mBeamLength;
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                out.write(data);
                out.flush();
                Log.d(TAG, "startBarBeam : " + this.mBeamLength);
                if (out != null) {
                    out.close();
                    if (this.isRunning) {
                        synchronized (this.mListeners) {
                            i$ = this.mListeners.iterator();
                            while (i$.hasNext()) {
                                ((Listener) i$.next()).onBeamingStarted();
                            }
                            this.mListeners.notify();
                        }
                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                    }
                    this.isRunning = true;
                    out2 = out;
                }
            } catch (Exception e6) {
                ex422222 = e6;
                ex422222.printStackTrace();
                result = FAIL;
                if (out_en != null) {
                    out_en.close();
                }
                data[0] = (byte) -1;
                data[1] = this.mBeamLength;
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                out.write(data);
                out.flush();
                Log.d(TAG, "startBarBeam : " + this.mBeamLength);
                if (out != null) {
                } else {
                    out.close();
                    if (this.isRunning) {
                        synchronized (this.mListeners) {
                            i$ = this.mListeners.iterator();
                            while (i$.hasNext()) {
                                ((Listener) i$.next()).onBeamingStarted();
                            }
                            this.mListeners.notify();
                        }
                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                    }
                    this.isRunning = true;
                    out2 = out;
                }
            }
            data[0] = (byte) -1;
            data[1] = this.mBeamLength;
            try {
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                try {
                    out.write(data);
                    out.flush();
                    Log.d(TAG, "startBarBeam : " + this.mBeamLength);
                    if (out != null) {
                        out.close();
                        if (this.isRunning) {
                            synchronized (this.mListeners) {
                                i$ = this.mListeners.iterator();
                                while (i$.hasNext()) {
                                    ((Listener) i$.next()).onBeamingStarted();
                                }
                                this.mListeners.notify();
                            }
                            this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                        }
                        this.isRunning = true;
                        out2 = out;
                    }
                } catch (FileNotFoundException e7) {
                    ex2 = e7;
                    out2 = out;
                    try {
                        ex2.printStackTrace();
                        result = FAIL;
                        if (out2 != null) {
                            try {
                                out2.close();
                                if (!this.isRunning) {
                                    synchronized (this.mListeners) {
                                        i$ = this.mListeners.iterator();
                                        while (i$.hasNext()) {
                                            ((Listener) i$.next()).onBeamingStarted();
                                        }
                                        this.mListeners.notify();
                                    }
                                    this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                                }
                                this.isRunning = true;
                            } catch (FileNotFoundException ex22) {
                                ex22.printStackTrace();
                            }
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        if (out2 != null) {
                            try {
                                out2.close();
                                if (!this.isRunning) {
                                    synchronized (this.mListeners) {
                                        i$ = this.mListeners.iterator();
                                        while (i$.hasNext()) {
                                            ((Listener) i$.next()).onBeamingStarted();
                                        }
                                        this.mListeners.notify();
                                    }
                                    this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                                }
                                this.isRunning = true;
                            } catch (Exception ex4222222) {
                                ex4222222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e8) {
                    ex3 = e8;
                    out2 = out;
                    ex3.printStackTrace();
                    result = FAIL;
                    if (out2 != null) {
                        try {
                            out2.close();
                            if (!this.isRunning) {
                                synchronized (this.mListeners) {
                                    i$ = this.mListeners.iterator();
                                    while (i$.hasNext()) {
                                        ((Listener) i$.next()).onBeamingStarted();
                                    }
                                    this.mListeners.notify();
                                }
                                this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                            }
                            this.isRunning = true;
                        } catch (IOException ex32) {
                            ex32.printStackTrace();
                        }
                    }
                } catch (Exception e9) {
                    ex4222222 = e9;
                    out2 = out;
                    ex4222222.printStackTrace();
                    result = FAIL;
                    if (out2 != null) {
                        try {
                            out2.close();
                            if (!this.isRunning) {
                                synchronized (this.mListeners) {
                                    i$ = this.mListeners.iterator();
                                    while (i$.hasNext()) {
                                        ((Listener) i$.next()).onBeamingStarted();
                                    }
                                    this.mListeners.notify();
                                }
                                this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                            }
                            this.isRunning = true;
                        } catch (Exception ex42222222) {
                            ex42222222.printStackTrace();
                        }
                    }
                } catch (Throwable th8) {
                    th = th8;
                    out2 = out;
                    if (out2 != null) {
                        out2.close();
                        if (this.isRunning) {
                            synchronized (this.mListeners) {
                                i$ = this.mListeners.iterator();
                                while (i$.hasNext()) {
                                    ((Listener) i$.next()).onBeamingStarted();
                                }
                                this.mListeners.notify();
                            }
                            this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                        }
                        this.isRunning = true;
                    }
                    throw th;
                }
            } catch (FileNotFoundException e10) {
                ex22 = e10;
                ex22.printStackTrace();
                result = FAIL;
                if (out2 != null) {
                    out2.close();
                    if (this.isRunning) {
                        synchronized (this.mListeners) {
                            i$ = this.mListeners.iterator();
                            while (i$.hasNext()) {
                                ((Listener) i$.next()).onBeamingStarted();
                            }
                            this.mListeners.notify();
                        }
                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                    }
                    this.isRunning = true;
                }
            } catch (IOException e11) {
                ex32 = e11;
                ex32.printStackTrace();
                result = FAIL;
                if (out2 != null) {
                    out2.close();
                    if (this.isRunning) {
                        synchronized (this.mListeners) {
                            i$ = this.mListeners.iterator();
                            while (i$.hasNext()) {
                                ((Listener) i$.next()).onBeamingStarted();
                            }
                            this.mListeners.notify();
                        }
                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                    }
                    this.isRunning = true;
                }
            } catch (Exception e12) {
                ex42222222 = e12;
                ex42222222.printStackTrace();
                result = FAIL;
                if (out2 != null) {
                    out2.close();
                    if (this.isRunning) {
                        synchronized (this.mListeners) {
                            i$ = this.mListeners.iterator();
                            while (i$.hasNext()) {
                                ((Listener) i$.next()).onBeamingStarted();
                            }
                            this.mListeners.notify();
                        }
                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                    }
                    this.isRunning = true;
                }
            }
        }
    }

    public void stopBeaming() {
        Exception ex;
        Throwable th;
        FileNotFoundException ex2;
        FileOutputStream out;
        IOException ex3;
        Log.d(TAG, "stopBarBeam");
        this.mContext.enforceCallingOrSelfPermission("com.samsung.permission.BARCODE_READ", null);
        FileWriter out_en = null;
        FileOutputStream out2 = null;
        result = SUCCESS;
        byte[] data = new byte[2];
        char[] flag = new char[1];
        synchronized (this) {
            try {
                FileWriter out_en2 = new FileWriter("/sys/class/sensors/proximity_sensor/barcode_emul_en");
                try {
                    flag[0] = '0';
                    out_en2.write(flag);
                    out_en2.flush();
                    if (out_en2 != null) {
                        try {
                            out_en2.close();
                            out_en = out_en2;
                        } catch (Exception ex4) {
                            ex4.printStackTrace();
                            out_en = out_en2;
                        } catch (Throwable th2) {
                            th = th2;
                            out_en = out_en2;
                            throw th;
                        }
                    }
                } catch (FileNotFoundException e) {
                    ex2 = e;
                    out_en = out_en2;
                    try {
                        ex2.printStackTrace();
                        result = FAIL;
                        if (out_en != null) {
                            try {
                                out_en.close();
                            } catch (Exception ex42) {
                                ex42.printStackTrace();
                            } catch (Throwable th3) {
                                th = th3;
                                throw th;
                            }
                        }
                        data[0] = (byte) -1;
                        data[1] = (byte) 0;
                        out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                        out.write(data);
                        out.flush();
                        if (out != null) {
                            try {
                                out.close();
                                out2 = out;
                            } catch (Exception ex422) {
                                ex422.printStackTrace();
                                out2 = out;
                            } catch (Throwable th4) {
                                th = th4;
                                out2 = out;
                                throw th;
                            }
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        if (out_en != null) {
                            try {
                                out_en.close();
                            } catch (Exception ex4222) {
                                ex4222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e2) {
                    ex3 = e2;
                    out_en = out_en2;
                    ex3.printStackTrace();
                    result = FAIL;
                    if (out_en != null) {
                        try {
                            out_en.close();
                        } catch (Exception ex42222) {
                            ex42222.printStackTrace();
                        }
                    }
                    data[0] = (byte) -1;
                    data[1] = (byte) 0;
                    out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                    out.write(data);
                    out.flush();
                    if (out != null) {
                    } else {
                        out.close();
                        out2 = out;
                    }
                } catch (Exception e3) {
                    ex42222 = e3;
                    out_en = out_en2;
                    ex42222.printStackTrace();
                    result = FAIL;
                    if (out_en != null) {
                        try {
                            out_en.close();
                        } catch (Exception ex422222) {
                            ex422222.printStackTrace();
                        }
                    }
                    data[0] = (byte) -1;
                    data[1] = (byte) 0;
                    out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                    out.write(data);
                    out.flush();
                    if (out != null) {
                        out.close();
                        out2 = out;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    out_en = out_en2;
                    if (out_en != null) {
                        out_en.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e4) {
                ex2 = e4;
                ex2.printStackTrace();
                result = FAIL;
                if (out_en != null) {
                    out_en.close();
                }
                data[0] = (byte) -1;
                data[1] = (byte) 0;
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                out.write(data);
                out.flush();
                if (out != null) {
                } else {
                    out.close();
                    out2 = out;
                }
            } catch (IOException e5) {
                ex3 = e5;
                ex3.printStackTrace();
                result = FAIL;
                if (out_en != null) {
                    out_en.close();
                }
                data[0] = (byte) -1;
                data[1] = (byte) 0;
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                out.write(data);
                out.flush();
                if (out != null) {
                    out.close();
                    out2 = out;
                }
            } catch (Exception e6) {
                ex422222 = e6;
                ex422222.printStackTrace();
                result = FAIL;
                if (out_en != null) {
                    out_en.close();
                }
                data[0] = (byte) -1;
                data[1] = (byte) 0;
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                out.write(data);
                out.flush();
                if (out != null) {
                } else {
                    out.close();
                    out2 = out;
                }
            }
            data[0] = (byte) -1;
            data[1] = (byte) 0;
            try {
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                try {
                    out.write(data);
                    out.flush();
                    if (out != null) {
                        out.close();
                        out2 = out;
                    }
                } catch (FileNotFoundException e7) {
                    ex2 = e7;
                    out2 = out;
                    try {
                        ex2.printStackTrace();
                        result = FAIL;
                        if (out2 != null) {
                            try {
                                out2.close();
                            } catch (Exception ex4222222) {
                                ex4222222.printStackTrace();
                            }
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        if (out2 != null) {
                            try {
                                out2.close();
                            } catch (Exception ex42222222) {
                                ex42222222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e8) {
                    ex3 = e8;
                    out2 = out;
                    ex3.printStackTrace();
                    result = FAIL;
                    if (out2 != null) {
                        try {
                            out2.close();
                        } catch (Exception ex422222222) {
                            ex422222222.printStackTrace();
                        }
                    }
                } catch (Exception e9) {
                    ex422222222 = e9;
                    out2 = out;
                    ex422222222.printStackTrace();
                    result = FAIL;
                    if (out2 != null) {
                        try {
                            out2.close();
                        } catch (Exception ex4222222222) {
                            ex4222222222.printStackTrace();
                        }
                    }
                } catch (Throwable th8) {
                    th = th8;
                    out2 = out;
                    if (out2 != null) {
                        out2.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e10) {
                ex2 = e10;
                ex2.printStackTrace();
                result = FAIL;
                if (out2 != null) {
                    out2.close();
                }
            } catch (IOException e11) {
                ex3 = e11;
                ex3.printStackTrace();
                result = FAIL;
                if (out2 != null) {
                    out2.close();
                }
            } catch (Exception e12) {
                ex4222222222 = e12;
                ex4222222222.printStackTrace();
                result = FAIL;
                if (out2 != null) {
                    out2.close();
                }
            }
        }
    }

    public void setHopSequence(byte[] buffer, int col_size, int row_size) {
        FileOutputStream out;
        Exception ex;
        Throwable th;
        FileNotFoundException ex2;
        int i;
        int i2;
        int j;
        IOException ex3;
        Log.d(TAG, "sendHopSequenceTable " + col_size + " x " + row_size);
        this.mContext.enforceCallingOrSelfPermission("com.samsung.permission.BARCODE_READ", null);
        FileOutputStream out2 = null;
        byte[] data = new byte[8];
        byte[] numPacket = new byte[2];
        int NP = col_size;
        byte BSR = (byte) -127;
        result = SUCCESS;
        numPacket[0] = BluetoothHidDevice.SUBCLASS1_MOUSE;
        numPacket[1] = (byte) (NP & 31);
        synchronized (this) {
            try {
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                try {
                    out.write(numPacket);
                    out.flush();
                    if (out != null) {
                        try {
                            out.close();
                            out2 = out;
                        } catch (Exception ex4) {
                            ex4.printStackTrace();
                            out2 = out;
                        } catch (Throwable th2) {
                            th = th2;
                            out2 = out;
                            throw th;
                        }
                    }
                    out2 = out;
                } catch (FileNotFoundException e) {
                    ex2 = e;
                    out2 = out;
                    try {
                        ex2.printStackTrace();
                        result = FAIL;
                        if (out2 != null) {
                            try {
                                out2.close();
                            } catch (Exception ex42) {
                                ex42.printStackTrace();
                            } catch (Throwable th3) {
                                th = th3;
                                throw th;
                            }
                        }
                        i = 0;
                        out = out2;
                        while (i < col_size) {
                            i2 = 0 + 1;
                            data[0] = BSR;
                            if (buffer[i * row_size] <= (byte) 21) {
                                j = i2 + 1;
                                data[i2] = buffer[i * row_size];
                            } else {
                                Log.d(TAG, "not supported bw ");
                                j = i2 + 1;
                                data[i2] = (byte) 21;
                            }
                            i2 = j + 1;
                            data[j] = buffer[(i * row_size) + 1];
                            j = i2 + 1;
                            data[i2] = buffer[(i * row_size) + 2];
                            i2 = j + 1;
                            data[j] = buffer[(i * row_size) + 3];
                            j = i2 + 1;
                            data[i2] = buffer[(i * row_size) + 4];
                            i2 = j + 1;
                            data[j] = buffer[(i * row_size) + 5];
                            j = i2 + 1;
                            data[i2] = buffer[(i * row_size) + 6];
                            BSR = (byte) (BSR + 7);
                            synchronized (this) {
                                try {
                                    out2 = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                                    try {
                                        out2.write(data);
                                        out2.flush();
                                        if (out2 != null) {
                                            try {
                                                out2.close();
                                            } catch (IOException ex32) {
                                                ex32.printStackTrace();
                                            }
                                        }
                                    } catch (FileNotFoundException e2) {
                                        ex2 = e2;
                                        try {
                                            ex2.printStackTrace();
                                            result = FAIL;
                                            if (out2 != null) {
                                                try {
                                                    out2.close();
                                                } catch (IOException ex322) {
                                                    ex322.printStackTrace();
                                                }
                                            }
                                            i++;
                                            out = out2;
                                        } catch (Throwable th4) {
                                            th = th4;
                                        }
                                    } catch (IOException e3) {
                                        ex322 = e3;
                                        ex322.printStackTrace();
                                        result = FAIL;
                                        if (out2 != null) {
                                            try {
                                                out2.close();
                                            } catch (IOException ex3222) {
                                                ex3222.printStackTrace();
                                            }
                                        }
                                        i++;
                                        out = out2;
                                    } catch (Exception e4) {
                                        ex42 = e4;
                                        ex42.printStackTrace();
                                        result = FAIL;
                                        if (out2 != null) {
                                            try {
                                                out2.close();
                                            } catch (IOException ex32222) {
                                                ex32222.printStackTrace();
                                            }
                                        }
                                        i++;
                                        out = out2;
                                    }
                                } catch (FileNotFoundException e5) {
                                    ex2 = e5;
                                    out2 = out;
                                    ex2.printStackTrace();
                                    result = FAIL;
                                    if (out2 != null) {
                                        out2.close();
                                    }
                                    i++;
                                    out = out2;
                                } catch (IOException e6) {
                                    ex32222 = e6;
                                    out2 = out;
                                    ex32222.printStackTrace();
                                    result = FAIL;
                                    if (out2 != null) {
                                        out2.close();
                                    }
                                    i++;
                                    out = out2;
                                } catch (Exception e7) {
                                    ex42 = e7;
                                    out2 = out;
                                    ex42.printStackTrace();
                                    result = FAIL;
                                    if (out2 != null) {
                                        out2.close();
                                    }
                                    i++;
                                    out = out2;
                                } catch (Throwable th5) {
                                    th = th5;
                                    out2 = out;
                                }
                            }
                            i++;
                            out = out2;
                        }
                        return;
                    } catch (Throwable th6) {
                        th = th6;
                        if (out2 != null) {
                            try {
                                out2.close();
                            } catch (Exception ex422) {
                                ex422.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e8) {
                    ex32222 = e8;
                    out2 = out;
                    ex32222.printStackTrace();
                    result = FAIL;
                    if (out2 != null) {
                        try {
                            out2.close();
                        } catch (Exception ex4222) {
                            ex4222.printStackTrace();
                        }
                    }
                    i = 0;
                    out = out2;
                    while (i < col_size) {
                        i2 = 0 + 1;
                        data[0] = BSR;
                        if (buffer[i * row_size] <= (byte) 21) {
                            Log.d(TAG, "not supported bw ");
                            j = i2 + 1;
                            data[i2] = (byte) 21;
                        } else {
                            j = i2 + 1;
                            data[i2] = buffer[i * row_size];
                        }
                        i2 = j + 1;
                        data[j] = buffer[(i * row_size) + 1];
                        j = i2 + 1;
                        data[i2] = buffer[(i * row_size) + 2];
                        i2 = j + 1;
                        data[j] = buffer[(i * row_size) + 3];
                        j = i2 + 1;
                        data[i2] = buffer[(i * row_size) + 4];
                        i2 = j + 1;
                        data[j] = buffer[(i * row_size) + 5];
                        j = i2 + 1;
                        data[i2] = buffer[(i * row_size) + 6];
                        BSR = (byte) (BSR + 7);
                        synchronized (this) {
                            out2 = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                            out2.write(data);
                            out2.flush();
                            if (out2 != null) {
                                out2.close();
                            }
                        }
                        i++;
                        out = out2;
                    }
                    return;
                } catch (Exception e9) {
                    ex4222 = e9;
                    out2 = out;
                    ex4222.printStackTrace();
                    result = FAIL;
                    if (out2 != null) {
                        try {
                            out2.close();
                        } catch (Exception ex42222) {
                            ex42222.printStackTrace();
                        }
                    }
                    i = 0;
                    out = out2;
                    while (i < col_size) {
                        i2 = 0 + 1;
                        data[0] = BSR;
                        if (buffer[i * row_size] <= (byte) 21) {
                            j = i2 + 1;
                            data[i2] = buffer[i * row_size];
                        } else {
                            Log.d(TAG, "not supported bw ");
                            j = i2 + 1;
                            data[i2] = (byte) 21;
                        }
                        i2 = j + 1;
                        data[j] = buffer[(i * row_size) + 1];
                        j = i2 + 1;
                        data[i2] = buffer[(i * row_size) + 2];
                        i2 = j + 1;
                        data[j] = buffer[(i * row_size) + 3];
                        j = i2 + 1;
                        data[i2] = buffer[(i * row_size) + 4];
                        i2 = j + 1;
                        data[j] = buffer[(i * row_size) + 5];
                        j = i2 + 1;
                        data[i2] = buffer[(i * row_size) + 6];
                        BSR = (byte) (BSR + 7);
                        synchronized (this) {
                            out2 = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                            out2.write(data);
                            out2.flush();
                            if (out2 != null) {
                                out2.close();
                            }
                        }
                        i++;
                        out = out2;
                    }
                    return;
                } catch (Throwable th7) {
                    th = th7;
                    out2 = out;
                    if (out2 != null) {
                        out2.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e10) {
                ex2 = e10;
                ex2.printStackTrace();
                result = FAIL;
                if (out2 != null) {
                    out2.close();
                }
                i = 0;
                out = out2;
                while (i < col_size) {
                    i2 = 0 + 1;
                    data[0] = BSR;
                    if (buffer[i * row_size] <= (byte) 21) {
                        Log.d(TAG, "not supported bw ");
                        j = i2 + 1;
                        data[i2] = (byte) 21;
                    } else {
                        j = i2 + 1;
                        data[i2] = buffer[i * row_size];
                    }
                    i2 = j + 1;
                    data[j] = buffer[(i * row_size) + 1];
                    j = i2 + 1;
                    data[i2] = buffer[(i * row_size) + 2];
                    i2 = j + 1;
                    data[j] = buffer[(i * row_size) + 3];
                    j = i2 + 1;
                    data[i2] = buffer[(i * row_size) + 4];
                    i2 = j + 1;
                    data[j] = buffer[(i * row_size) + 5];
                    j = i2 + 1;
                    data[i2] = buffer[(i * row_size) + 6];
                    BSR = (byte) (BSR + 7);
                    synchronized (this) {
                        out2 = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                        out2.write(data);
                        out2.flush();
                        if (out2 != null) {
                            out2.close();
                        }
                    }
                    i++;
                    out = out2;
                }
                return;
            } catch (IOException e11) {
                ex32222 = e11;
                ex32222.printStackTrace();
                result = FAIL;
                if (out2 != null) {
                    out2.close();
                }
                i = 0;
                out = out2;
                while (i < col_size) {
                    i2 = 0 + 1;
                    data[0] = BSR;
                    if (buffer[i * row_size] <= (byte) 21) {
                        j = i2 + 1;
                        data[i2] = buffer[i * row_size];
                    } else {
                        Log.d(TAG, "not supported bw ");
                        j = i2 + 1;
                        data[i2] = (byte) 21;
                    }
                    i2 = j + 1;
                    data[j] = buffer[(i * row_size) + 1];
                    j = i2 + 1;
                    data[i2] = buffer[(i * row_size) + 2];
                    i2 = j + 1;
                    data[j] = buffer[(i * row_size) + 3];
                    j = i2 + 1;
                    data[i2] = buffer[(i * row_size) + 4];
                    i2 = j + 1;
                    data[j] = buffer[(i * row_size) + 5];
                    j = i2 + 1;
                    data[i2] = buffer[(i * row_size) + 6];
                    BSR = (byte) (BSR + 7);
                    synchronized (this) {
                        out2 = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                        out2.write(data);
                        out2.flush();
                        if (out2 != null) {
                            out2.close();
                        }
                    }
                    i++;
                    out = out2;
                }
                return;
            } catch (Exception e12) {
                ex42222 = e12;
                ex42222.printStackTrace();
                result = FAIL;
                if (out2 != null) {
                    out2.close();
                }
                i = 0;
                out = out2;
                while (i < col_size) {
                    i2 = 0 + 1;
                    data[0] = BSR;
                    if (buffer[i * row_size] <= (byte) 21) {
                        Log.d(TAG, "not supported bw ");
                        j = i2 + 1;
                        data[i2] = (byte) 21;
                    } else {
                        j = i2 + 1;
                        data[i2] = buffer[i * row_size];
                    }
                    i2 = j + 1;
                    data[j] = buffer[(i * row_size) + 1];
                    j = i2 + 1;
                    data[i2] = buffer[(i * row_size) + 2];
                    i2 = j + 1;
                    data[j] = buffer[(i * row_size) + 3];
                    j = i2 + 1;
                    data[i2] = buffer[(i * row_size) + 4];
                    i2 = j + 1;
                    data[j] = buffer[(i * row_size) + 5];
                    j = i2 + 1;
                    data[i2] = buffer[(i * row_size) + 6];
                    BSR = (byte) (BSR + 7);
                    synchronized (this) {
                        out2 = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                        out2.write(data);
                        out2.flush();
                        if (out2 != null) {
                            out2.close();
                        }
                    }
                    i++;
                    out = out2;
                }
                return;
            }
        }
        if (out2 != null) {
            try {
                out2.close();
            } catch (IOException ex322222) {
                ex322222.printStackTrace();
            }
        }
        throw th;
        throw th;
    }

    public void setBarcode(byte[] buffer) {
        IOException ex;
        Throwable th;
        FileNotFoundException ex2;
        Exception ex3;
        Log.d(TAG, "setBarcode");
        this.mContext.enforceCallingOrSelfPermission("com.samsung.permission.BARCODE_READ", null);
        FileOutputStream out = null;
        byte[] data = new byte[(buffer.length + 3)];
        result = SUCCESS;
        this.mBeamLength = (byte) (buffer.length + 2);
        Log.d(TAG, "sendDataTable length : " + this.mBeamLength);
        data[0] = Byte.valueOf((byte) 0).byteValue();
        data[1] = Byte.valueOf((byte) -1).byteValue();
        for (int i = 2; i < buffer.length + 2; i++) {
            data[i] = buffer[i - 2];
        }
        data[buffer.length + 2] = Byte.valueOf((byte) -1).byteValue();
        synchronized (this) {
            try {
                FileOutputStream out2 = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                try {
                    out2.write(data);
                    Log.d(TAG, "setBarcode is Done!");
                    if (out2 != null) {
                        try {
                            out2.close();
                            out = out2;
                        } catch (IOException ex4) {
                            ex4.printStackTrace();
                            out = out2;
                        } catch (Throwable th2) {
                            th = th2;
                            out = out2;
                            throw th;
                        }
                    }
                } catch (FileNotFoundException e) {
                    ex2 = e;
                    out = out2;
                    try {
                        ex2.printStackTrace();
                        result = FAIL;
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException ex42) {
                                ex42.printStackTrace();
                            } catch (Throwable th3) {
                                th = th3;
                                throw th;
                            }
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException ex422) {
                                ex422.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e2) {
                    ex422 = e2;
                    out = out2;
                    ex422.printStackTrace();
                    result = FAIL;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex4222) {
                            ex4222.printStackTrace();
                        }
                    }
                } catch (Exception e3) {
                    ex3 = e3;
                    out = out2;
                    ex3.printStackTrace();
                    result = FAIL;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex42222) {
                            ex42222.printStackTrace();
                        }
                    }
                } catch (Throwable th5) {
                    th = th5;
                    out = out2;
                    if (out != null) {
                        out.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e4) {
                ex2 = e4;
                ex2.printStackTrace();
                result = FAIL;
                if (out != null) {
                    out.close();
                }
            } catch (IOException e5) {
                ex42222 = e5;
                ex42222.printStackTrace();
                result = FAIL;
                if (out != null) {
                    out.close();
                }
            } catch (Exception e6) {
                ex3 = e6;
                ex3.printStackTrace();
                result = FAIL;
                if (out != null) {
                    out.close();
                }
            }
        }
    }

    public void startBeaming_repeat(int repeatCount) {
        IOException ex;
        Throwable th;
        FileNotFoundException ex2;
        FileOutputStream out;
        Iterator i$;
        Exception ex3;
        Log.d(TAG, "startBeaming_repeat");
        if (repeatCount < 1) {
            repeatCount = 1;
        }
        if (repeatCount > 255) {
            repeatCount = 255;
        }
        this.mContext.enforceCallingOrSelfPermission("com.samsung.permission.BARCODE_READ", null);
        result = SUCCESS;
        FileWriter out_en = null;
        FileOutputStream out2 = null;
        byte[] data = new byte[2];
        char[] flag = new char[1];
        synchronized (this) {
            try {
                FileWriter out_en2 = new FileWriter("/sys/class/sensors/proximity_sensor/barcode_emul_en");
                try {
                    flag[0] = '1';
                    out_en2.write(flag);
                    out_en2.flush();
                    if (out_en2 != null) {
                        try {
                            out_en2.close();
                            out_en = out_en2;
                        } catch (IOException ex4) {
                            ex4.printStackTrace();
                            out_en = out_en2;
                        } catch (Throwable th2) {
                            th = th2;
                            out_en = out_en2;
                            throw th;
                        }
                    }
                } catch (FileNotFoundException e) {
                    ex2 = e;
                    out_en = out_en2;
                    try {
                        ex2.printStackTrace();
                        result = FAIL;
                        if (out_en != null) {
                            try {
                                out_en.close();
                            } catch (IOException ex42) {
                                ex42.printStackTrace();
                            } catch (Throwable th3) {
                                th = th3;
                                throw th;
                            }
                        }
                        data[0] = (byte) -1;
                        data[1] = (byte) repeatCount;
                        out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                        try {
                            out.write(data);
                            out.flush();
                            Log.d(TAG, "startBarBeam : " + this.mBeamLength);
                            if (out != null) {
                                try {
                                    out.close();
                                    if (!this.isRunning) {
                                        synchronized (this.mListeners) {
                                            i$ = this.mListeners.iterator();
                                            while (i$.hasNext()) {
                                                ((Listener) i$.next()).onBeamingStarted();
                                            }
                                            this.mListeners.notify();
                                        }
                                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                                    }
                                    this.isRunning = true;
                                    out2 = out;
                                } catch (IOException ex422) {
                                    try {
                                        ex422.printStackTrace();
                                        out2 = out;
                                    } catch (Throwable th4) {
                                        th = th4;
                                        out2 = out;
                                        throw th;
                                    }
                                }
                            }
                        } catch (FileNotFoundException e2) {
                            ex2 = e2;
                            out2 = out;
                            try {
                                ex2.printStackTrace();
                                result = FAIL;
                                if (out2 != null) {
                                    try {
                                        out2.close();
                                        if (!this.isRunning) {
                                            synchronized (this.mListeners) {
                                                i$ = this.mListeners.iterator();
                                                while (i$.hasNext()) {
                                                    ((Listener) i$.next()).onBeamingStarted();
                                                }
                                                this.mListeners.notify();
                                            }
                                            this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                                        }
                                        this.isRunning = true;
                                    } catch (FileNotFoundException ex22) {
                                        ex22.printStackTrace();
                                    }
                                }
                            } catch (Throwable th5) {
                                th = th5;
                                if (out2 != null) {
                                    try {
                                        out2.close();
                                        if (!this.isRunning) {
                                            synchronized (this.mListeners) {
                                                i$ = this.mListeners.iterator();
                                                while (i$.hasNext()) {
                                                    ((Listener) i$.next()).onBeamingStarted();
                                                }
                                                this.mListeners.notify();
                                            }
                                            this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                                        }
                                        this.isRunning = true;
                                    } catch (IOException ex4222) {
                                        ex4222.printStackTrace();
                                    }
                                }
                                throw th;
                            }
                        } catch (IOException e3) {
                            ex4222 = e3;
                            out2 = out;
                            ex4222.printStackTrace();
                            result = FAIL;
                            if (out2 != null) {
                                try {
                                    out2.close();
                                    if (!this.isRunning) {
                                        synchronized (this.mListeners) {
                                            i$ = this.mListeners.iterator();
                                            while (i$.hasNext()) {
                                                ((Listener) i$.next()).onBeamingStarted();
                                            }
                                            this.mListeners.notify();
                                        }
                                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                                    }
                                    this.isRunning = true;
                                } catch (IOException ex42222) {
                                    ex42222.printStackTrace();
                                }
                            }
                        } catch (Exception e4) {
                            ex3 = e4;
                            out2 = out;
                            ex3.printStackTrace();
                            result = FAIL;
                            if (out2 != null) {
                                try {
                                    out2.close();
                                    if (!this.isRunning) {
                                        synchronized (this.mListeners) {
                                            i$ = this.mListeners.iterator();
                                            while (i$.hasNext()) {
                                                ((Listener) i$.next()).onBeamingStarted();
                                            }
                                            this.mListeners.notify();
                                        }
                                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                                    }
                                    this.isRunning = true;
                                } catch (Exception ex32) {
                                    ex32.printStackTrace();
                                }
                            }
                        } catch (Throwable th6) {
                            th = th6;
                            out2 = out;
                            if (out2 != null) {
                                out2.close();
                                if (this.isRunning) {
                                    synchronized (this.mListeners) {
                                        i$ = this.mListeners.iterator();
                                        while (i$.hasNext()) {
                                            ((Listener) i$.next()).onBeamingStarted();
                                        }
                                        this.mListeners.notify();
                                    }
                                    this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                                }
                                this.isRunning = true;
                            }
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        if (out_en != null) {
                            try {
                                out_en.close();
                            } catch (IOException ex422222) {
                                ex422222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e5) {
                    ex422222 = e5;
                    out_en = out_en2;
                    ex422222.printStackTrace();
                    result = FAIL;
                    if (out_en != null) {
                        try {
                            out_en.close();
                        } catch (IOException ex4222222) {
                            ex4222222.printStackTrace();
                        }
                    }
                    data[0] = (byte) -1;
                    data[1] = (byte) repeatCount;
                    out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                    out.write(data);
                    out.flush();
                    Log.d(TAG, "startBarBeam : " + this.mBeamLength);
                    if (out != null) {
                    } else {
                        out.close();
                        if (this.isRunning) {
                            synchronized (this.mListeners) {
                                i$ = this.mListeners.iterator();
                                while (i$.hasNext()) {
                                    ((Listener) i$.next()).onBeamingStarted();
                                }
                                this.mListeners.notify();
                            }
                            this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                        }
                        this.isRunning = true;
                        out2 = out;
                    }
                } catch (Exception e6) {
                    ex32 = e6;
                    out_en = out_en2;
                    ex32.printStackTrace();
                    result = FAIL;
                    if (out_en != null) {
                        try {
                            out_en.close();
                        } catch (IOException ex42222222) {
                            ex42222222.printStackTrace();
                        }
                    }
                    data[0] = (byte) -1;
                    data[1] = (byte) repeatCount;
                    out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                    out.write(data);
                    out.flush();
                    Log.d(TAG, "startBarBeam : " + this.mBeamLength);
                    if (out != null) {
                        out.close();
                        if (this.isRunning) {
                            synchronized (this.mListeners) {
                                i$ = this.mListeners.iterator();
                                while (i$.hasNext()) {
                                    ((Listener) i$.next()).onBeamingStarted();
                                }
                                this.mListeners.notify();
                            }
                            this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                        }
                        this.isRunning = true;
                        out2 = out;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    out_en = out_en2;
                    if (out_en != null) {
                        out_en.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e7) {
                ex22 = e7;
                ex22.printStackTrace();
                result = FAIL;
                if (out_en != null) {
                    out_en.close();
                }
                data[0] = (byte) -1;
                data[1] = (byte) repeatCount;
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                out.write(data);
                out.flush();
                Log.d(TAG, "startBarBeam : " + this.mBeamLength);
                if (out != null) {
                } else {
                    out.close();
                    if (this.isRunning) {
                        synchronized (this.mListeners) {
                            i$ = this.mListeners.iterator();
                            while (i$.hasNext()) {
                                ((Listener) i$.next()).onBeamingStarted();
                            }
                            this.mListeners.notify();
                        }
                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                    }
                    this.isRunning = true;
                    out2 = out;
                }
            } catch (IOException e8) {
                ex42222222 = e8;
                ex42222222.printStackTrace();
                result = FAIL;
                if (out_en != null) {
                    out_en.close();
                }
                data[0] = (byte) -1;
                data[1] = (byte) repeatCount;
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                out.write(data);
                out.flush();
                Log.d(TAG, "startBarBeam : " + this.mBeamLength);
                if (out != null) {
                    out.close();
                    if (this.isRunning) {
                        synchronized (this.mListeners) {
                            i$ = this.mListeners.iterator();
                            while (i$.hasNext()) {
                                ((Listener) i$.next()).onBeamingStarted();
                            }
                            this.mListeners.notify();
                        }
                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                    }
                    this.isRunning = true;
                    out2 = out;
                }
            } catch (Exception e9) {
                ex32 = e9;
                ex32.printStackTrace();
                result = FAIL;
                if (out_en != null) {
                    out_en.close();
                }
                data[0] = (byte) -1;
                data[1] = (byte) repeatCount;
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                out.write(data);
                out.flush();
                Log.d(TAG, "startBarBeam : " + this.mBeamLength);
                if (out != null) {
                } else {
                    out.close();
                    if (this.isRunning) {
                        synchronized (this.mListeners) {
                            i$ = this.mListeners.iterator();
                            while (i$.hasNext()) {
                                ((Listener) i$.next()).onBeamingStarted();
                            }
                            this.mListeners.notify();
                        }
                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                    }
                    this.isRunning = true;
                    out2 = out;
                }
            }
            data[0] = (byte) -1;
            data[1] = (byte) repeatCount;
            try {
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                out.write(data);
                out.flush();
                Log.d(TAG, "startBarBeam : " + this.mBeamLength);
                if (out != null) {
                    out.close();
                    if (this.isRunning) {
                        synchronized (this.mListeners) {
                            i$ = this.mListeners.iterator();
                            while (i$.hasNext()) {
                                ((Listener) i$.next()).onBeamingStarted();
                            }
                            this.mListeners.notify();
                        }
                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                    }
                    this.isRunning = true;
                    out2 = out;
                }
            } catch (FileNotFoundException e10) {
                ex22 = e10;
                ex22.printStackTrace();
                result = FAIL;
                if (out2 != null) {
                    out2.close();
                    if (this.isRunning) {
                        synchronized (this.mListeners) {
                            i$ = this.mListeners.iterator();
                            while (i$.hasNext()) {
                                ((Listener) i$.next()).onBeamingStarted();
                            }
                            this.mListeners.notify();
                        }
                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                    }
                    this.isRunning = true;
                }
            } catch (IOException e11) {
                ex42222222 = e11;
                ex42222222.printStackTrace();
                result = FAIL;
                if (out2 != null) {
                    out2.close();
                    if (this.isRunning) {
                        synchronized (this.mListeners) {
                            i$ = this.mListeners.iterator();
                            while (i$.hasNext()) {
                                ((Listener) i$.next()).onBeamingStarted();
                            }
                            this.mListeners.notify();
                        }
                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                    }
                    this.isRunning = true;
                }
            } catch (Exception e12) {
                ex32 = e12;
                ex32.printStackTrace();
                result = FAIL;
                if (out2 != null) {
                    out2.close();
                    if (this.isRunning) {
                        synchronized (this.mListeners) {
                            i$ = this.mListeners.iterator();
                            while (i$.hasNext()) {
                                ((Listener) i$.next()).onBeamingStarted();
                            }
                            this.mListeners.notify();
                        }
                        this.mcheckstatusThread.mHandler.sendEmptyMessageDelayed(30, 0);
                    }
                    this.isRunning = true;
                }
            }
        }
    }

    public void addListener(IBinder binder) throws RemoteException {
        Listener listener;
        Throwable th;
        synchronized (this.mListeners) {
            try {
                Iterator i$ = this.mListeners.iterator();
                while (i$.hasNext()) {
                    if (binder == ((Listener) i$.next()).mToken) {
                        return;
                    }
                }
                if (null == null) {
                    Listener l = new Listener(binder);
                    try {
                        binder.linkToDeath(l, 0);
                        this.mListeners.add(l);
                        String client = ProxyInfo.LOCAL_EXCL_LIST;
                        try {
                            client = IBarBeamListener.Stub.asInterface(binder).getListenerInfo();
                        } catch (RemoteException e) {
                        }
                        this.mListeners.notify();
                        listener = l;
                    } catch (Throwable th2) {
                        th = th2;
                        listener = l;
                        throw th;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
    }

    public void removeListener(IBinder binder) throws RemoteException {
        Listener l = null;
        String client = ProxyInfo.LOCAL_EXCL_LIST;
        synchronized (this.mListeners) {
            Iterator i$ = this.mListeners.iterator();
            while (i$.hasNext()) {
                Listener listener = (Listener) i$.next();
                if (binder == listener.mToken) {
                    l = listener;
                    break;
                }
            }
            if (l != null) {
                binder.unlinkToDeath(l, 0);
                this.mListeners.remove(l);
                try {
                    client = IBarBeamListener.Stub.asInterface(binder).getListenerInfo();
                } catch (RemoteException e) {
                }
                this.mListeners.notify();
            }
        }
    }

    public boolean isImplementationCompatible() {
        IOException ex;
        Throwable th;
        FileNotFoundException ex2;
        FileOutputStream out;
        Exception ex3;
        Log.d(TAG, "isImplementationCompatible");
        FileWriter out_en = null;
        FileOutputStream out2 = null;
        result = SUCCESS;
        synchronized (this) {
            try {
                FileWriter out_en2 = new FileWriter("/sys/class/sensors/proximity_sensor/barcode_emul_en");
                try {
                    Log.d(TAG, "isImplementationCompatible : Enable barcode_emul_en");
                    if (out_en2 != null) {
                        try {
                            out_en2.close();
                            out_en = out_en2;
                        } catch (IOException ex4) {
                            ex4.printStackTrace();
                            out_en = out_en2;
                        } catch (Throwable th2) {
                            th = th2;
                            out_en = out_en2;
                            throw th;
                        }
                    }
                } catch (FileNotFoundException e) {
                    ex2 = e;
                    out_en = out_en2;
                    try {
                        ex2.printStackTrace();
                        result = FAIL;
                        if (out_en != null) {
                            try {
                                out_en.close();
                            } catch (IOException ex42) {
                                ex42.printStackTrace();
                            } catch (Throwable th3) {
                                th = th3;
                                throw th;
                            }
                        }
                        out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                        try {
                            Log.d(TAG, "isImplementationCompatible : Enable barcode_send");
                            if (out != null) {
                                try {
                                    out.close();
                                    out2 = out;
                                } catch (IOException ex422) {
                                    ex422.printStackTrace();
                                    out2 = out;
                                } catch (Throwable th4) {
                                    th = th4;
                                    out2 = out;
                                    throw th;
                                }
                            }
                        } catch (FileNotFoundException e2) {
                            ex2 = e2;
                            out2 = out;
                            try {
                                ex2.printStackTrace();
                                result = FAIL;
                                if (out2 != null) {
                                    try {
                                        out2.close();
                                    } catch (IOException ex4222) {
                                        ex4222.printStackTrace();
                                    }
                                }
                                return result;
                            } catch (Throwable th5) {
                                th = th5;
                                if (out2 != null) {
                                    try {
                                        out2.close();
                                    } catch (IOException ex42222) {
                                        ex42222.printStackTrace();
                                    }
                                }
                                throw th;
                            }
                        } catch (IOException e3) {
                            ex42222 = e3;
                            out2 = out;
                            ex42222.printStackTrace();
                            result = FAIL;
                            if (out2 != null) {
                                try {
                                    out2.close();
                                } catch (IOException ex422222) {
                                    ex422222.printStackTrace();
                                }
                            }
                            return result;
                        } catch (Exception e4) {
                            ex3 = e4;
                            out2 = out;
                            ex3.printStackTrace();
                            result = FAIL;
                            if (out2 != null) {
                                try {
                                    out2.close();
                                } catch (IOException ex4222222) {
                                    ex4222222.printStackTrace();
                                }
                            }
                            return result;
                        } catch (Throwable th6) {
                            th = th6;
                            out2 = out;
                            if (out2 != null) {
                                out2.close();
                            }
                            throw th;
                        }
                        return result;
                    } catch (Throwable th7) {
                        th = th7;
                        if (out_en != null) {
                            try {
                                out_en.close();
                            } catch (IOException ex42222222) {
                                ex42222222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e5) {
                    ex42222222 = e5;
                    out_en = out_en2;
                    ex42222222.printStackTrace();
                    result = FAIL;
                    if (out_en != null) {
                        try {
                            out_en.close();
                        } catch (IOException ex422222222) {
                            ex422222222.printStackTrace();
                        }
                    }
                    out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                    Log.d(TAG, "isImplementationCompatible : Enable barcode_send");
                    if (out != null) {
                    } else {
                        out.close();
                        out2 = out;
                    }
                    return result;
                } catch (Exception e6) {
                    ex3 = e6;
                    out_en = out_en2;
                    ex3.printStackTrace();
                    result = FAIL;
                    if (out_en != null) {
                        try {
                            out_en.close();
                        } catch (IOException ex4222222222) {
                            ex4222222222.printStackTrace();
                        }
                    }
                    out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                    Log.d(TAG, "isImplementationCompatible : Enable barcode_send");
                    if (out != null) {
                        out.close();
                        out2 = out;
                    }
                    return result;
                } catch (Throwable th8) {
                    th = th8;
                    out_en = out_en2;
                    if (out_en != null) {
                        out_en.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e7) {
                ex2 = e7;
                ex2.printStackTrace();
                result = FAIL;
                if (out_en != null) {
                    out_en.close();
                }
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                Log.d(TAG, "isImplementationCompatible : Enable barcode_send");
                if (out != null) {
                } else {
                    out.close();
                    out2 = out;
                }
                return result;
            } catch (IOException e8) {
                ex4222222222 = e8;
                ex4222222222.printStackTrace();
                result = FAIL;
                if (out_en != null) {
                    out_en.close();
                }
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                Log.d(TAG, "isImplementationCompatible : Enable barcode_send");
                if (out != null) {
                    out.close();
                    out2 = out;
                }
                return result;
            } catch (Exception e9) {
                ex3 = e9;
                ex3.printStackTrace();
                result = FAIL;
                if (out_en != null) {
                    out_en.close();
                }
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                Log.d(TAG, "isImplementationCompatible : Enable barcode_send");
                if (out != null) {
                } else {
                    out.close();
                    out2 = out;
                }
                return result;
            }
            try {
                out = new FileOutputStream("/sys/class/sec/sec_barcode_emul/barcode_send");
                Log.d(TAG, "isImplementationCompatible : Enable barcode_send");
                if (out != null) {
                    out.close();
                    out2 = out;
                }
            } catch (FileNotFoundException e10) {
                ex2 = e10;
                ex2.printStackTrace();
                result = FAIL;
                if (out2 != null) {
                    out2.close();
                }
                return result;
            } catch (IOException e11) {
                ex4222222222 = e11;
                ex4222222222.printStackTrace();
                result = FAIL;
                if (out2 != null) {
                    out2.close();
                }
                return result;
            } catch (Exception e12) {
                ex3 = e12;
                ex3.printStackTrace();
                result = FAIL;
                if (out2 != null) {
                    out2.close();
                }
                return result;
            }
            return result;
        }
    }
}
