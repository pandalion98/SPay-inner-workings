package com.samsung.android.spaytui;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Build.VERSION;
import android.spay.TACommandResponse;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytui.SpayTuiTACommands.CheckTUISession;
import com.samsung.android.spaytui.SpayTuiTACommands.ClearState;
import com.samsung.android.spaytui.SpayTuiTACommands.CloseTui;
import com.samsung.android.spaytui.SpayTuiTACommands.GenerateRandom;
import com.samsung.android.spaytui.SpayTuiTACommands.GetAuthResult;
import com.samsung.android.spaytui.SpayTuiTACommands.GetNonce;
import com.samsung.android.spaytui.SpayTuiTACommands.GetPinRandom;
import com.samsung.android.spaytui.SpayTuiTACommands.GetPinSO;
import com.samsung.android.spaytui.SpayTuiTACommands.GetSecureResult;
import com.samsung.android.spaytui.SpayTuiTACommands.InAppConfirm;
import com.samsung.android.spaytui.SpayTuiTACommands.LoadPin.Request;
import com.samsung.android.spaytui.SpayTuiTACommands.LoadPin.Response;
import com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureDisplay;
import com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureInit;
import com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureTouch;
import com.samsung.android.spaytui.SpayTuiTACommands.MerchantSendSecureImg;
import com.samsung.android.spaytui.SpayTuiTACommands.PreloadFpSecureResult;
import com.samsung.android.spaytui.SpayTuiTACommands.Resume;
import com.samsung.android.spaytui.SpayTuiTACommands.SetActionBarText;
import com.samsung.android.spaytui.SpayTuiTACommands.SetBackgroundImg;
import com.samsung.android.spaytui.SpayTuiTACommands.SetCancelBtn;
import com.samsung.android.spaytui.SpayTuiTACommands.SetPinBox;
import com.samsung.android.spaytui.SpayTuiTACommands.SetPrompt;
import com.samsung.android.spaytui.SpayTuiTACommands.SetRtl;
import com.samsung.android.spaytui.SpayTuiTACommands.SetSecureModeText;
import com.samsung.android.spaytui.SpayTuiTACommands.SetupBio;
import com.samsung.android.spaytui.SpayTuiTACommands.SetupPin;
import com.samsung.android.spaytui.SpayTuiTACommands.StartSecureUI;
import com.samsung.android.spaytui.SpayTuiTACommands.UpdateFP;
import com.samsung.android.spaytui.SpayTuiTACommands.Verify;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class SpayTuiTAController extends TAController {
    public static final int ERROR_EXECUTE_FAIL = -2;
    public static final int ERROR_INVALID_INPUT_PARAMS = -3;
    public static final int ERROR_NULL_PAYMENT_HANDLE = -1;
    public static final int SPAY_AUTH_FAIL = 8191;
    public static final int SPAY_AUTH_FAIL_FP_NOT_FOR_PAYMENT = 8190;
    public static final int SPAY_TPP_ERROR_3_SAME_DIGITS = 393226;
    public static final int SPAY_TPP_ERROR_CANCELED = 393225;
    public static final int SPAY_TPP_ERROR_CONSECUTIVE_DIGITS = 393227;
    public static final int SPAY_TPP_ERROR_GENERIC = 401408;
    public static final int SPAY_TPP_ERROR_INVALID_PIN_NUMBER = 397312;
    public static final int SPAY_TPP_ERROR_INVALID_PIN_SO = 393217;
    public static final int SPAY_TPP_ERROR_INVALID_STATE = 397313;
    public static final int SPAY_TPP_ERROR_NO_FLOW_TO_RESUME = 393219;
    public static final int SPAY_TPP_ERROR_NO_PIN = 393216;
    public static final int SPAY_TPP_ERROR_PIN_EXIST = 393218;
    public static final int SPAY_TPP_ERROR_PIN_MISMATCH = 393224;
    public static final int SPAY_TPP_ERROR_SETUP_PIN_MISMATCH = 393222;
    public static final int SPAY_TPP_ERROR_TUI_FP_REJECT = 393228;
    public static final int SPAY_TPP_ERROR_TUI_VERIFIED_TIMEOUT = 393229;
    public static final int SPAY_TPP_INAPP_CONFIRMED = 409600;
    public static final int SPAY_TPP_PIN_VERIFYED = 393223;
    public static final int SPAY_TPP_SETUP_PIN_ENTERED = 393220;
    public static final int SPAY_TPP_SETUP_PIN_VERIFIED = 393221;
    public static final int SPAY_TPP_TUI_ERROR_REWRAP_PIN_SO = 413697;
    public static final int SPAY_TPP_TUI_SESSION_OFF = 405505;
    public static final int SPAY_TPP_TUI_SESSION_ON = 405504;
    public static final int SPAY_TPP_TUI_UPGRADE_PIN_SO_VERSION = 413696;
    private static final String TAG = "SpayTuiTAController";
    public static TAInfo TA_INFO = null;
    public static final int TIMA_ERROR_TUI_CANCELLED = 20480;
    private static final String TUIPINSECUREOBJECTFILE = "mpt.dat";
    private static final String TUI_DATA_DIR;
    private static final boolean bQC;
    private static Context mContext;
    private static SpayTuiTAController mInstance;
    private static boolean mPinExist;
    private static boolean mResetPending;
    private static byte[] mScreenPNGBuf;
    private final int DEFAULT_SW360;
    private int actionBarTextHeight;
    private int actionBarTextWidth;
    private final float actionbar_text;
    private byte[] mNonce;
    private List<TAController> mSupportTAs;
    private final float securemode_text;

    /* renamed from: com.samsung.android.spaytui.SpayTuiTAController.1 */
    class C05901 extends TextPaint {
        final /* synthetic */ float val$fConstTextSize;

        C05901(float f) {
            this.val$fConstTextSize = f;
            setARGB(CipherSuite.TLS_DHE_DSS_WITH_SEED_CBC_SHA, 56, 56, 56);
            setTextAlign(Align.LEFT);
            setTextSize(this.val$fConstTextSize);
            setAntiAlias(true);
            setTypeface(Typeface.create("Roboto-Medium", 0));
        }
    }

    /* renamed from: com.samsung.android.spaytui.SpayTuiTAController.2 */
    class C05912 extends TextPaint {
        final /* synthetic */ float val$fConstTextSize;

        C05912(float f) {
            this.val$fConstTextSize = f;
            setARGB(204, 37, 37, 37);
            setTextAlign(Align.LEFT);
            setTextSize(this.val$fConstTextSize);
            setAntiAlias(true);
            setTypeface(Typeface.create("RobotoCondensed-Bold", 1));
        }
    }

    /* renamed from: com.samsung.android.spaytui.SpayTuiTAController.3 */
    class C05923 extends TextPaint {
        final /* synthetic */ float val$fConstTextSize;

        C05923(float f) {
            this.val$fConstTextSize = f;
            setARGB(GF2Field.MASK, CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384);
            setTextAlign(Align.LEFT);
            setTextSize(this.val$fConstTextSize);
            setAntiAlias(true);
            setTypeface(Typeface.create("Roboto", 0));
        }
    }

    /* renamed from: com.samsung.android.spaytui.SpayTuiTAController.4 */
    class C05934 extends TextPaint {
        C05934() {
            setARGB(GF2Field.MASK, CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384);
            setTextAlign(Align.LEFT);
            setTextSize(SpayTuiTAController.this.getDensity() * 20.0f);
            setAntiAlias(true);
            setTypeface(Typeface.create("Roboto", 0));
        }
    }

    /* renamed from: com.samsung.android.spaytui.SpayTuiTAController.5 */
    class C05945 extends TextPaint {
        C05945() {
            setARGB(GF2Field.MASK, 0, 0, 0);
            setTextAlign(Align.LEFT);
            setTextSize(SpayTuiTAController.this.getDensity() * 16.0f);
            setAntiAlias(true);
            setTypeface(Typeface.create("Roboto", 0));
        }
    }

    /* renamed from: com.samsung.android.spaytui.SpayTuiTAController.6 */
    class C05956 extends TextPaint {
        C05956() {
            setARGB(GF2Field.MASK, 57, 87, CipherSuite.TLS_RSA_PSK_WITH_AES_128_GCM_SHA256);
            setTextAlign(Align.LEFT);
            setTextSize(SpayTuiTAController.this.getDensity() * 14.0f);
            setAntiAlias(true);
            setTypeface(Typeface.create("Roboto", 0));
        }
    }

    /* renamed from: com.samsung.android.spaytui.SpayTuiTAController.7 */
    class C05967 extends TextPaint {
        C05967() {
            setARGB(GF2Field.MASK, 0, 0, 0);
            setTextAlign(Align.LEFT);
            setTextSize(SpayTuiTAController.this.getDensity() * 14.0f);
            setAntiAlias(true);
            setTypeface(Typeface.create("Roboto", 0));
        }
    }

    /* renamed from: com.samsung.android.spaytui.SpayTuiTAController.8 */
    class C05978 extends TextPaint {
        C05978() {
            setARGB(GF2Field.MASK, 57, 87, CipherSuite.TLS_RSA_PSK_WITH_AES_128_GCM_SHA256);
            setTextAlign(Align.LEFT);
            setTextSize(SpayTuiTAController.this.getDensity() * 17.0f);
            setAntiAlias(true);
            setTypeface(Typeface.create("Roboto", 0));
        }
    }

    /* renamed from: com.samsung.android.spaytui.SpayTuiTAController.9 */
    class C05989 extends TextPaint {
        C05989() {
            setARGB(GF2Field.MASK, 250, 250, 250);
            setTextAlign(Align.LEFT);
            setTextSize(SpayTuiTAController.this.getDensity() * 17.0f);
            setAntiAlias(true);
            setTypeface(Typeface.create("Roboto", 0));
        }
    }

    static {
        TA_INFO = new SpayTuiTAInfo();
        TUI_DATA_DIR = TAController.getEfsDirectory();
        mPinExist = false;
        bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
        mResetPending = false;
    }

    private SpayTuiTAController(Context context) {
        super(context, TA_INFO);
        this.DEFAULT_SW360 = 360;
        this.securemode_text = 16.0f;
        this.actionbar_text = 19.0f;
        this.actionBarTextWidth = 0;
        this.actionBarTextHeight = 0;
        this.mNonce = null;
    }

    public static synchronized SpayTuiTAController createOnlyInstance(Context context) {
        SpayTuiTAController spayTuiTAController;
        synchronized (SpayTuiTAController.class) {
            mContext = context;
            if (mInstance == null) {
                mInstance = new SpayTuiTAController(context);
            }
            spayTuiTAController = mInstance;
        }
        return spayTuiTAController;
    }

    public static synchronized SpayTuiTAController getInstance() {
        SpayTuiTAController spayTuiTAController;
        synchronized (SpayTuiTAController.class) {
            spayTuiTAController = mInstance;
        }
        return spayTuiTAController;
    }

    public boolean pinExist() {
        return mPinExist;
    }

    public boolean isResetPeding() {
        return mResetPending;
    }

    public void setResetStatus(boolean z) {
        Log.m285d(TAG, "setResetStatus:  " + z);
        mResetPending = z;
    }

    public void setTAs(List<TAController> list) {
        this.mSupportTAs = list;
    }

    public String getLocale() {
        String language = mContext.getResources().getConfiguration().locale.getLanguage();
        Log.m285d(TAG, "locale.getLanguage: " + language);
        return language;
    }

    private static final boolean isAndroidN() {
        return VERSION.SDK_INT == 24;
    }

    public String getScreenDensity() {
        String str;
        String str2 = BuildConfig.FLAVOR;
        Configuration configuration = mContext.getResources().getConfiguration();
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        String str3 = BuildConfig.FLAVOR;
        int i = (int) (((float) configuration.smallestScreenWidthDp) * displayMetrics.density);
        Log.m285d(TAG, "disp.density: " + displayMetrics.density + ", conf.smallestScreenWidthDp: " + configuration.smallestScreenWidthDp);
        Log.m285d(TAG, "swidth: " + i);
        if (i < 800) {
            str = "-xhdpi";
        } else if (i < 1100) {
            str = "-xxhdpi";
        } else {
            str = "-xxxhdpi";
        }
        if (bQC) {
            String str4 = Build.MODEL;
            if (str4.indexOf("A") > 0) {
                str2 = str4.substring(str4.indexOf("A"));
            }
            if ("A7100".equals(str2) || "A5100".equals(str2)) {
                return str2;
            }
            return String.format(Locale.US, "%d%s", new Object[]{Integer.valueOf(360), str});
        }
        return String.format(Locale.US, "%d%s", new Object[]{Integer.valueOf(360), str});
    }

    protected String findTAByDeviceModel(AssetManager assetManager) {
        String screenDensity;
        Exception e;
        String str = BuildConfig.FLAVOR;
        try {
            screenDensity = getScreenDensity();
            try {
                str = TAInfo.getTARootDir() + File.separator + screenDensity + File.separator + this.mTAInfo.getTAFileName();
                String[] list = assetManager.list(TAInfo.getTARootDir() + File.separator + screenDensity);
                if (list != null) {
                    int length = list.length;
                    int i = 0;
                    while (i < length) {
                        String str2 = list[i];
                        if (str2 == null || !str2.equals(this.mTAInfo.getTAFileName())) {
                            i++;
                        } else {
                            Log.m285d(TAG, "Found TA file: " + str);
                            return str;
                        }
                    }
                }
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                Log.m285d(TAG, "Load default TUI for res: " + screenDensity);
                return TAInfo.getTARootDir() + File.separator + this.mTAInfo.getTAFileName();
            }
        } catch (Exception e3) {
            Exception exception = e3;
            screenDensity = str;
            e = exception;
            e.printStackTrace();
            Log.m285d(TAG, "Load default TUI for res: " + screenDensity);
            return TAInfo.getTARootDir() + File.separator + this.mTAInfo.getTAFileName();
        }
        Log.m285d(TAG, "Load default TUI for res: " + screenDensity);
        return TAInfo.getTARootDir() + File.separator + this.mTAInfo.getTAFileName();
    }

    public synchronized int loadPin(byte[] bArr) {
        int i;
        boolean z = true;
        synchronized (this) {
            this.actionBarTextWidth = 0;
            this.actionBarTextHeight = 0;
            if (DEBUG) {
                Log.m285d(TAG, "Calling loadPin()");
            }
            if (this.mPaymentHandle == null) {
                Log.m286e(TAG, "Error: Payment Handle is null");
                i = ERROR_NULL_PAYMENT_HANDLE;
            } else {
                try {
                    TACommandResponse executeNoLoad = executeNoLoad(new Request(bArr));
                    if (executeNoLoad == null) {
                        Log.m286e(TAG, "Error: load_and_execute failed");
                        i = ERROR_EXECUTE_FAIL;
                    } else {
                        Response response = new Response(executeNoLoad);
                        i = (int) response.mRetVal.retCode.get();
                        if (i == 0 || i == SPAY_TPP_TUI_UPGRADE_PIN_SO_VERSION) {
                            if (response.mRetVal.existPinLen.get() <= 0) {
                                z = false;
                            }
                            mPinExist = z;
                            Log.m287i(TAG, "loadPin: Existing PIN len=" + response.mRetVal.existPinLen.get());
                            if (i == SPAY_TPP_TUI_UPGRADE_PIN_SO_VERSION) {
                                Log.m287i(TAG, "loadPin: Saving upgraded pin so file");
                                savePinSo(response.mRetVal.updated_pin_so.getData());
                                writePinRandomFiles(true);
                            }
                            if (DEBUG) {
                                Log.m285d(TAG, "Existing PIN len: " + response.mRetVal.existPinLen.get() + "  pin exist: " + mPinExist);
                            }
                            i = 0;
                        }
                    }
                } catch (IllegalArgumentException e) {
                    Log.m286e(TAG, e.toString());
                    e.printStackTrace();
                    i = ERROR_INVALID_INPUT_PARAMS;
                }
            }
        }
        return i;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean deletePin() {
        /*
        r5 = this;
        r1 = 1;
        monitor-enter(r5);
        r0 = "SpayTuiTAController";
        r2 = "Delete PIN data";
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r2);	 Catch:{ all -> 0x0025 }
        r0 = r5.mSupportTAs;	 Catch:{ all -> 0x0025 }
        r2 = r0.iterator();	 Catch:{ all -> 0x0025 }
    L_0x000f:
        r0 = r2.hasNext();	 Catch:{ all -> 0x0025 }
        if (r0 == 0) goto L_0x0049;
    L_0x0015:
        r0 = r2.next();	 Catch:{ all -> 0x0025 }
        r0 = (com.samsung.android.spaytzsvc.api.TAController) r0;	 Catch:{ all -> 0x0025 }
        if (r0 != 0) goto L_0x0028;
    L_0x001d:
        r0 = "SpayTuiTAController";
        r3 = "Error: TAController is null. Not created yet";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r3);	 Catch:{ all -> 0x0025 }
        goto L_0x000f;
    L_0x0025:
        r0 = move-exception;
        monitor-exit(r5);
        throw r0;
    L_0x0028:
        r3 = r0.usesPinRandom();	 Catch:{ all -> 0x0025 }
        if (r3 != 0) goto L_0x0036;
    L_0x002e:
        r0 = "SpayTuiTAController";
        r3 = "This TA do not use Pin Random";
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r3);	 Catch:{ all -> 0x0025 }
        goto L_0x000f;
    L_0x0036:
        r0 = r0.getTAInfo();	 Catch:{ all -> 0x0025 }
        r0 = r0.getPinRandomFileName();	 Catch:{ all -> 0x0025 }
        r3 = new java.io.File;	 Catch:{ all -> 0x0025 }
        r4 = TUI_DATA_DIR;	 Catch:{ all -> 0x0025 }
        r3.<init>(r4, r0);	 Catch:{ all -> 0x0025 }
        r3.delete();	 Catch:{ all -> 0x0025 }
        goto L_0x000f;
    L_0x0049:
        r0 = new java.io.File;	 Catch:{ all -> 0x0025 }
        r2 = TUI_DATA_DIR;	 Catch:{ all -> 0x0025 }
        r3 = "mpt.dat";
        r0.<init>(r2, r3);	 Catch:{ all -> 0x0025 }
        r2 = r0.exists();	 Catch:{ all -> 0x0025 }
        if (r2 == 0) goto L_0x0070;
    L_0x0058:
        r2 = r0.isDirectory();	 Catch:{ all -> 0x0025 }
        if (r2 != 0) goto L_0x0070;
    L_0x005e:
        r0 = r0.delete();	 Catch:{ all -> 0x0025 }
        if (r0 == 0) goto L_0x0067;
    L_0x0064:
        r0 = r1;
    L_0x0065:
        monitor-exit(r5);
        return r0;
    L_0x0067:
        r0 = "SpayTuiTAController";
        r1 = "Unable to delete PIN SO!";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ all -> 0x0025 }
        r0 = 0;
        goto L_0x0065;
    L_0x0070:
        r0 = r1;
        goto L_0x0065;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTAController.deletePin():boolean");
    }

    public synchronized boolean loadTA() {
        boolean z = true;
        boolean z2 = false;
        synchronized (this) {
            mPinExist = false;
            if (mResetPending) {
                Log.m286e(TAG, "PF resetting... ");
            } else if (super.loadTA()) {
                Object readFile = readFile(new File(TUI_DATA_DIR, TUIPINSECUREOBJECTFILE));
                if (readFile == null || readFile.length <= 0) {
                    if (DEBUG) {
                        Log.m285d(TAG, "No PIN file found, generate the file only containing random");
                    }
                    if (savePinSecureObjects(true) != 0) {
                        z = false;
                    }
                    z2 = z;
                } else {
                    if (DEBUG) {
                        Log.m285d(TAG, "pin so: " + readFile.toString());
                    }
                    int loadPin = loadPin(Arrays.copyOfRange(readFile, 1, readFile.length));
                    if (loadPin == 0) {
                        if (DEBUG) {
                            Log.m285d(TAG, "Existing Pin SO loaded, PIN exist: " + mPinExist + "  pin[0] = " + readFile[0]);
                        }
                        writePinRandomFiles(false);
                        z2 = true;
                    } else {
                        Log.m286e(TAG, "Load PIN failed with ret = " + loadPin + ": possibly invalid PIN secure object!");
                    }
                }
            }
        }
        return z2;
    }

    private int savePinSo(byte[] bArr) {
        File file = new File(TUI_DATA_DIR, TUIPINSECUREOBJECTFILE);
        try {
            Object obj = new byte[(bArr.length + 1)];
            if (mPinExist) {
                obj[0] = (byte) 1;
            } else {
                obj[0] = (byte) 0;
            }
            System.arraycopy(bArr, 0, obj, 1, bArr.length);
            writeFile(obj, file);
            return 0;
        } catch (Exception e) {
            Log.m286e(TAG, "Failed to save PIN SO!");
            e.printStackTrace();
            return SPAY_TPP_ERROR_INVALID_PIN_SO;
        }
    }

    private int savePinSecureObjects(boolean z) {
        byte[] generateRandom;
        mPinExist = false;
        if (z) {
            generateRandom = generateRandom();
        } else {
            generateRandom = getPinSO();
        }
        if (generateRandom == null) {
            Log.m286e(TAG, "Failed to get PIN/random secure object!");
            return SPAY_TPP_ERROR_INVALID_PIN_SO;
        }
        Log.m285d(TAG, "PIN/random secure object size " + generateRandom.length);
        Log.m287i(TAG, "savePinSecureObjects: savePinSo");
        int savePinSo = savePinSo(generateRandom);
        if (savePinSo == 0) {
            return writePinRandomFiles(true);
        }
        Log.m286e(TAG, "Failed to save PIN SO for PIN!");
        return savePinSo;
    }

    private int writePinRandomFiles(boolean z) {
        for (TAController tAController : this.mSupportTAs) {
            if (tAController == null) {
                Log.m286e(TAG, "Error: TAController is null. Not created yet");
            } else if (tAController.usesPinRandom()) {
                TAInfo tAInfo = tAController.getTAInfo();
                if (!z) {
                    File file = new File(TUI_DATA_DIR, tAInfo.getPinRandomFileName());
                    if (file.exists() && !file.isDirectory()) {
                        Log.m285d(TAG, "Pin Random already exist for this TA");
                    }
                }
                if (bQC) {
                    Log.m285d(TAG, "For QC only, trying to load TA: " + tAController.getTAInfo().getTAFileName());
                    if (!tAController.loadTA()) {
                        Log.m285d(TAG, "Failed to load TA " + tAController.getTAInfo().getTAFileName());
                    }
                }
                String tAId = tAInfo.getTAId();
                byte[] pinRandom = getPinRandom(tAId);
                if (pinRandom != null) {
                    if (DEBUG) {
                        Log.m285d(TAG, "Get SO (len=" + pinRandom.length + ")for " + tAId);
                    }
                    if (bQC) {
                        Log.m285d(TAG, "Now send the encapsulated buffer to TA " + tAId);
                        tAController.setupPinRandom(pinRandom);
                        tAController.unloadTA();
                    } else {
                        String pinRandomFileName = tAInfo.getPinRandomFileName();
                        Log.m285d(TAG, "For tbase, file name is " + pinRandomFileName);
                        try {
                            writeFile(pinRandom, new File(TUI_DATA_DIR, pinRandomFileName));
                        } catch (Exception e) {
                            Log.m286e(TAG, "Failed to save SO for " + tAId);
                            e.printStackTrace();
                            deletePinSo();
                            return SPAY_TPP_ERROR_INVALID_PIN_SO;
                        }
                    }
                }
                Log.m286e(TAG, "Failed to get SO for " + tAId);
                deletePinSo();
                return SPAY_TPP_ERROR_INVALID_PIN_SO;
            } else {
                Log.m285d(TAG, "This TA do not use Pin Random");
            }
        }
        return 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] readFile(java.io.File r9) {
        /*
        r1 = 0;
        r2 = r9.length();
        r0 = (int) r2;
        r0 = new byte[r0];
        r4 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x002a }
        r4.<init>(r9);	 Catch:{ Exception -> 0x002a }
        r5 = 0;
        r3 = 0;
        r6 = r9.length();	 Catch:{ Throwable -> 0x0043, all -> 0x005a }
        r2 = (int) r6;	 Catch:{ Throwable -> 0x0043, all -> 0x005a }
    L_0x0014:
        if (r2 <= 0) goto L_0x001d;
    L_0x0016:
        r6 = r4.read(r0, r3, r2);	 Catch:{ Throwable -> 0x0043, all -> 0x005a }
        r3 = r3 + r6;
        r2 = r2 - r6;
        goto L_0x0014;
    L_0x001d:
        if (r4 == 0) goto L_0x0024;
    L_0x001f:
        if (r1 == 0) goto L_0x003f;
    L_0x0021:
        r4.close();	 Catch:{ Throwable -> 0x0025 }
    L_0x0024:
        return r0;
    L_0x0025:
        r2 = move-exception;
        r5.addSuppressed(r2);	 Catch:{ Exception -> 0x002a }
        goto L_0x0024;
    L_0x002a:
        r0 = move-exception;
        r2 = DEBUG;
        if (r2 == 0) goto L_0x0032;
    L_0x002f:
        r0.printStackTrace();
    L_0x0032:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x003d;
    L_0x0036:
        r0 = "SpayTuiTAController";
        r2 = "No Pin set up yet";
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r2);
    L_0x003d:
        r0 = r1;
        goto L_0x0024;
    L_0x003f:
        r4.close();	 Catch:{ Exception -> 0x002a }
        goto L_0x0024;
    L_0x0043:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0045 }
    L_0x0045:
        r2 = move-exception;
        r8 = r2;
        r2 = r0;
        r0 = r8;
    L_0x0049:
        if (r4 == 0) goto L_0x0050;
    L_0x004b:
        if (r2 == 0) goto L_0x0056;
    L_0x004d:
        r4.close();	 Catch:{ Throwable -> 0x0051 }
    L_0x0050:
        throw r0;	 Catch:{ Exception -> 0x002a }
    L_0x0051:
        r3 = move-exception;
        r2.addSuppressed(r3);	 Catch:{ Exception -> 0x002a }
        goto L_0x0050;
    L_0x0056:
        r4.close();	 Catch:{ Exception -> 0x002a }
        goto L_0x0050;
    L_0x005a:
        r0 = move-exception;
        r2 = r1;
        goto L_0x0049;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTAController.readFile(java.io.File):byte[]");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean writeFile(byte[] r6, java.io.File r7) {
        /*
        r2 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x003e }
        r2.<init>(r7);	 Catch:{ Exception -> 0x003e }
        r1 = 0;
        r0 = DEBUG;	 Catch:{ Throwable -> 0x0053, all -> 0x006a }
        if (r0 == 0) goto L_0x0023;
    L_0x000a:
        r0 = "SpayTuiTAController";
        r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0053, all -> 0x006a }
        r3.<init>();	 Catch:{ Throwable -> 0x0053, all -> 0x006a }
        r4 = "File Write - Length = ";
        r3 = r3.append(r4);	 Catch:{ Throwable -> 0x0053, all -> 0x006a }
        r4 = r6.length;	 Catch:{ Throwable -> 0x0053, all -> 0x006a }
        r3 = r3.append(r4);	 Catch:{ Throwable -> 0x0053, all -> 0x006a }
        r3 = r3.toString();	 Catch:{ Throwable -> 0x0053, all -> 0x006a }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r3);	 Catch:{ Throwable -> 0x0053, all -> 0x006a }
    L_0x0023:
        r2.write(r6);	 Catch:{ Throwable -> 0x0053, all -> 0x006a }
        r0 = r2.getFD();	 Catch:{ Throwable -> 0x0053, all -> 0x006a }
        r0.sync();	 Catch:{ Throwable -> 0x0053, all -> 0x006a }
        r2.close();	 Catch:{ Throwable -> 0x0053, all -> 0x006a }
        r0 = 1;
        if (r2 == 0) goto L_0x0038;
    L_0x0033:
        if (r1 == 0) goto L_0x004f;
    L_0x0035:
        r2.close();	 Catch:{ Throwable -> 0x0039 }
    L_0x0038:
        return r0;
    L_0x0039:
        r2 = move-exception;
        r1.addSuppressed(r2);	 Catch:{ Exception -> 0x003e }
        goto L_0x0038;
    L_0x003e:
        r0 = move-exception;
        r1 = DEBUG;
        if (r1 == 0) goto L_0x0046;
    L_0x0043:
        r0.printStackTrace();
    L_0x0046:
        r0 = "SpayTuiTAController";
        r1 = "writeFile failed";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = 0;
        goto L_0x0038;
    L_0x004f:
        r2.close();	 Catch:{ Exception -> 0x003e }
        goto L_0x0038;
    L_0x0053:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0055 }
    L_0x0055:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
    L_0x0059:
        if (r2 == 0) goto L_0x0060;
    L_0x005b:
        if (r1 == 0) goto L_0x0066;
    L_0x005d:
        r2.close();	 Catch:{ Throwable -> 0x0061 }
    L_0x0060:
        throw r0;	 Catch:{ Exception -> 0x003e }
    L_0x0061:
        r2 = move-exception;
        r1.addSuppressed(r2);	 Catch:{ Exception -> 0x003e }
        goto L_0x0060;
    L_0x0066:
        r2.close();	 Catch:{ Exception -> 0x003e }
        goto L_0x0060;
    L_0x006a:
        r0 = move-exception;
        goto L_0x0059;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTAController.writeFile(byte[], java.io.File):boolean");
    }

    private void deletePinSo() {
        new File(TUI_DATA_DIR, TUIPINSECUREOBJECTFILE).delete();
    }

    private float getDensity() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) mContext.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        Log.m285d(TAG, "getDensity " + displayMetrics.density);
        return displayMetrics.density;
    }

    private int getDensityDpi() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) mContext.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        Log.m285d(TAG, "getDensityDpi " + displayMetrics.densityDpi);
        return displayMetrics.densityDpi;
    }

    public synchronized int setSecureModeText(String[] strArr) {
        int i = ERROR_INVALID_INPUT_PARAMS;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Set secure mode text");
            }
            if (this.mPaymentHandle == null) {
                Log.m286e(TAG, "Error: Payment Handle is null");
                i = ERROR_NULL_PAYMENT_HANDLE;
            } else {
                float applyDimension;
                DisplayMetrics displayMetrics = new DisplayMetrics();
                try {
                    Configuration configuration = mContext.getResources().getConfiguration();
                    DisplayMetrics displayMetrics2 = mContext.getResources().getDisplayMetrics();
                    float f = ((float) configuration.smallestScreenWidthDp) * displayMetrics2.density;
                    displayMetrics.setTo(displayMetrics2);
                    displayMetrics.density = f / 360.0f;
                    displayMetrics.densityDpi = (int) ((((float) displayMetrics2.densityDpi) / displayMetrics2.density) * displayMetrics.density);
                    displayMetrics.scaledDensity = displayMetrics.density;
                    applyDimension = TypedValue.applyDimension(1, 16.0f, displayMetrics);
                } catch (Exception e) {
                    e.printStackTrace();
                    applyDimension = TypedValue.applyDimension(1, 16.0f, mContext.getResources().getDisplayMetrics());
                }
                byte[] text2png = text2png(strArr, new C05901(applyDimension), 0, false);
                if (text2png != null) {
                    try {
                        TACommandResponse executeNoLoad = executeNoLoad(new SetSecureModeText.Request(text2png));
                        if (executeNoLoad == null) {
                            Log.m286e(TAG, "Error: load_and_execute failed");
                            i = ERROR_EXECUTE_FAIL;
                        } else {
                            SetSecureModeText.Response response = new SetSecureModeText.Response(executeNoLoad);
                            if (DEBUG) {
                                Log.m285d(TAG, "Set Secure Mode Text return " + response.mRetVal.retCode.get());
                            }
                            i = (int) response.mRetVal.retCode.get();
                        }
                    } catch (IllegalArgumentException e2) {
                        Log.m286e(TAG, e2.toString());
                        e2.printStackTrace();
                    }
                }
            }
        }
        return i;
    }

    public synchronized int setActionBarText(String[] strArr) {
        int i = ERROR_INVALID_INPUT_PARAMS;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Set action bar text");
            }
            if (this.mPaymentHandle == null) {
                Log.m286e(TAG, "Error: Payment Handle is null");
                i = ERROR_NULL_PAYMENT_HANDLE;
            } else {
                float f;
                TextPaint c05912;
                DisplayMetrics displayMetrics = new DisplayMetrics();
                try {
                    Configuration configuration = mContext.getResources().getConfiguration();
                    DisplayMetrics displayMetrics2 = mContext.getResources().getDisplayMetrics();
                    f = ((float) configuration.smallestScreenWidthDp) * displayMetrics2.density;
                    displayMetrics.setTo(displayMetrics2);
                    displayMetrics.density = f / 360.0f;
                    displayMetrics.densityDpi = (int) ((((float) displayMetrics2.densityDpi) / displayMetrics2.density) * displayMetrics.density);
                    displayMetrics.scaledDensity = displayMetrics.density;
                    f = TypedValue.applyDimension(1, 19.0f, displayMetrics);
                } catch (Exception e) {
                    e.printStackTrace();
                    f = TypedValue.applyDimension(1, 19.0f, mContext.getResources().getDisplayMetrics());
                }
                if (isAndroidN()) {
                    c05912 = new C05912(f);
                } else {
                    c05912 = new C05923(f);
                }
                byte[] text2png = text2png(strArr, c05912, Color.argb(GF2Field.MASK, 250, 250, 250), true);
                if (text2png != null) {
                    try {
                        TACommandResponse executeNoLoad = executeNoLoad(new SetActionBarText.Request(text2png));
                        if (executeNoLoad == null) {
                            Log.m286e(TAG, "Error: load_and_execute failed");
                            i = ERROR_EXECUTE_FAIL;
                        } else {
                            SetActionBarText.Response response = new SetActionBarText.Response(executeNoLoad);
                            if (DEBUG) {
                                Log.m285d(TAG, "Set Action Bar Text return " + response.mRetVal.retCode.get());
                            }
                            i = (int) response.mRetVal.retCode.get();
                        }
                    } catch (IllegalArgumentException e2) {
                        Log.m286e(TAG, e2.toString());
                        e2.printStackTrace();
                    }
                }
            }
        }
        return i;
    }

    public synchronized int setPinBox(byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4, int i5) {
        int i6;
        if (DEBUG) {
            Log.m285d(TAG, "Calling Set Pinbox");
        }
        if (this.mPaymentHandle == null) {
            Log.m286e(TAG, "Error: Payment Handle is null");
            i6 = ERROR_NULL_PAYMENT_HANDLE;
        } else {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new SetPinBox.Request(bArr, bArr2, i, i2, i3, i4, i5));
                if (executeNoLoad == null) {
                    Log.m286e(TAG, "Error: load_and_execute failed");
                    i6 = ERROR_EXECUTE_FAIL;
                } else {
                    SetPinBox.Response response = new SetPinBox.Response(executeNoLoad);
                    if (DEBUG) {
                        Log.m285d(TAG, "Set Pin Box return " + response.mRetVal.retCode.get());
                    }
                    i6 = (int) response.mRetVal.retCode.get();
                }
            } catch (IllegalArgumentException e) {
                Log.m286e(TAG, e.toString());
                e.printStackTrace();
                i6 = ERROR_INVALID_INPUT_PARAMS;
            }
        }
        return i6;
    }

    public synchronized int setPrompt(String[] strArr, TextPaint textPaint, int i, int i2) {
        int i3 = ERROR_INVALID_INPUT_PARAMS;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling Set prompt with string");
            }
            if (this.mPaymentHandle == null) {
                Log.m286e(TAG, "Error: Payment Handle is null");
                i3 = ERROR_NULL_PAYMENT_HANDLE;
            } else {
                byte[] text2png = text2png(strArr, textPaint, 0, false);
                if (text2png != null) {
                    try {
                        TACommandResponse executeNoLoad = executeNoLoad(new SetPrompt.Request(text2png, i, i2));
                        if (executeNoLoad == null) {
                            Log.m286e(TAG, "Error: load_and_execute failed");
                            i3 = ERROR_EXECUTE_FAIL;
                        } else {
                            SetPrompt.Response response = new SetPrompt.Response(executeNoLoad);
                            if (DEBUG) {
                                Log.m285d(TAG, "SetPrompt return " + response.mRetVal.retCode.get());
                            }
                            i3 = (int) response.mRetVal.retCode.get();
                        }
                    } catch (IllegalArgumentException e) {
                        Log.m286e(TAG, e.toString());
                        e.printStackTrace();
                    }
                }
            }
        }
        return i3;
    }

    public synchronized int setPrompt(byte[] bArr, int i, int i2) {
        int i3;
        if (DEBUG) {
            Log.m285d(TAG, "Calling Set prompt with img");
        }
        if (this.mPaymentHandle == null) {
            Log.m286e(TAG, "Error: Payment Handle is null");
            i3 = ERROR_NULL_PAYMENT_HANDLE;
        } else {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new SetPrompt.Request(bArr, i, i2));
                if (executeNoLoad == null) {
                    Log.m286e(TAG, "Error: load_and_execute failed");
                    i3 = ERROR_EXECUTE_FAIL;
                } else {
                    SetPrompt.Response response = new SetPrompt.Response(executeNoLoad);
                    if (DEBUG) {
                        Log.m285d(TAG, "SetPrompt return " + response.mRetVal.retCode.get());
                    }
                    i3 = (int) response.mRetVal.retCode.get();
                }
            } catch (IllegalArgumentException e) {
                Log.m286e(TAG, e.toString());
                e.printStackTrace();
                i3 = ERROR_INVALID_INPUT_PARAMS;
            }
        }
        return i3;
    }

    public int setRtl(boolean z) {
        Log.m285d(TAG, "setRtl=" + z);
        TACommandResponse executeNoLoad = executeNoLoad(new SetRtl.Request(z));
        if (executeNoLoad == null) {
            Log.m286e(TAG, "Error: load_and_execute failed");
            return ERROR_EXECUTE_FAIL;
        }
        int i = (int) new SetRtl.Response(executeNoLoad).mRetVal.retCode.get();
        Log.m285d(TAG, "ret=" + i);
        return i;
    }

    public synchronized int setupBio(byte[] bArr, byte[] bArr2) {
        int i = ERROR_NULL_PAYMENT_HANDLE;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling setup BIO");
            }
            if (this.mPaymentHandle == null) {
                Log.m286e(TAG, "Error: Payment Handle is null");
            } else {
                try {
                    TACommandResponse executeNoLoad = executeNoLoad(new SetupBio.Request(bArr, bArr2));
                    if (executeNoLoad == null) {
                        Log.m286e(TAG, "Error: load_and_execute setup bio failed");
                        i = ERROR_EXECUTE_FAIL;
                    } else {
                        SetupBio.Response response = new SetupBio.Response(executeNoLoad);
                        if (response.mRetVal.retCode.get() != 0) {
                            Log.m286e(TAG, "Error: SetupBio return " + response.mRetVal.retCode.get());
                        }
                        i = (response.mRetVal.retCode.get() == 0 && bQC) ? start_secure_touch() : (int) response.mRetVal.retCode.get();
                    }
                } catch (IllegalArgumentException e) {
                    Log.m286e(TAG, e.toString());
                    e.printStackTrace();
                }
            }
        }
        return i;
    }

    public synchronized int setupPin() {
        int i;
        if (DEBUG) {
            Log.m285d(TAG, "Calling Setup Pin");
        }
        if (this.mPaymentHandle == null) {
            Log.m286e(TAG, "Error: Payment Handle is null");
            i = ERROR_NULL_PAYMENT_HANDLE;
        } else {
            TACommandResponse executeNoLoad = executeNoLoad(new SetupPin.Request());
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: load_and_execute failed");
                i = ERROR_EXECUTE_FAIL;
            } else {
                SetupPin.Response response = new SetupPin.Response(executeNoLoad);
                if (DEBUG) {
                    Log.m285d(TAG, "Setup Pin return " + response.mRetVal.retCode.get());
                }
                this.mNonce = null;
                if (response.mRetVal.retCode.get() == 0 && bQC) {
                    i = start_secure_touch();
                } else {
                    i = (int) response.mRetVal.retCode.get();
                }
            }
        }
        return i;
    }

    public synchronized int setupPin(byte[] bArr) {
        int i;
        if (DEBUG) {
            Log.m285d(TAG, "Calling Setup Pin with secure object");
        }
        if (this.mPaymentHandle == null) {
            Log.m286e(TAG, "Error: Payment Handle is null");
            i = ERROR_NULL_PAYMENT_HANDLE;
        } else {
            TACommandResponse executeNoLoad = executeNoLoad(new SetupPin.Request(bArr));
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: load_and_execute failed");
                i = ERROR_EXECUTE_FAIL;
            } else {
                SetupPin.Response response = new SetupPin.Response(executeNoLoad);
                if (DEBUG) {
                    Log.m285d(TAG, "Setup Pin return " + response.mRetVal.retCode.get());
                }
                this.mNonce = null;
                if (response.mRetVal.retCode.get() == 0 && bQC) {
                    i = start_secure_touch();
                } else {
                    i = (int) response.mRetVal.retCode.get();
                }
            }
        }
        return i;
    }

    public synchronized byte[] getPinSO() {
        byte[] bArr = null;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling get Pin SO");
            }
            if (this.mPaymentHandle == null) {
                Log.m286e(TAG, "Error: Payment Handle is null");
            } else {
                TACommandResponse executeNoLoad = executeNoLoad(new GetPinSO.Request());
                if (executeNoLoad == null) {
                    Log.m286e(TAG, "Error: load_and_execute failed");
                } else {
                    GetPinSO.Response response = new GetPinSO.Response(executeNoLoad);
                    if (response.mRetVal.retCode.get() != 0) {
                        Log.m286e(TAG, "Error: get PIN SO return " + response.mRetVal.retCode.get());
                    } else {
                        if (DEBUG) {
                            Log.m285d(TAG, "Get PIN SO size: " + response.mRetVal.pinSo.size());
                        }
                        mPinExist = response.mRetVal.existPinLen.get() > 0;
                        Log.m287i(TAG, "getPinSO: Existing Pin len=" + response.mRetVal.existPinLen.get());
                        if (DEBUG) {
                            Log.m285d(TAG, "Pin len: " + response.mRetVal.existPinLen.get() + "  pin exist: " + mPinExist);
                        }
                        bArr = response.mRetVal.pinSo.getData();
                    }
                }
            }
        }
        return bArr;
    }

    public synchronized byte[] generateRandom() {
        byte[] bArr = null;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling generate Random");
            }
            if (this.mPaymentHandle == null) {
                Log.m286e(TAG, "Error: Payment Handle is null");
            } else {
                TACommandResponse executeNoLoad = executeNoLoad(new GenerateRandom.Request());
                if (executeNoLoad == null) {
                    Log.m286e(TAG, "Error: load_and_execute failed");
                } else {
                    GenerateRandom.Response response = new GenerateRandom.Response(executeNoLoad);
                    if (response.mRetVal.retCode.get() != 0) {
                        Log.m286e(TAG, "Error: get random return " + response.mRetVal.retCode.get());
                    } else {
                        Log.m287i(TAG, "generateRandom: Existing Pin length=" + response.mRetVal.existPinLen.get());
                        if (DEBUG) {
                            Log.m285d(TAG, "Get random/PIN so size: " + response.mRetVal.pinSo.size());
                            Log.m285d(TAG, "Pin length: " + response.mRetVal.existPinLen.get() + "  It should be 0");
                        }
                        bArr = response.mRetVal.pinSo.getData();
                    }
                }
            }
        }
        return bArr;
    }

    private int start_secure_touch() {
        if (!bQC) {
            return 0;
        }
        TACommandResponse executeNoLoad = executeNoLoad(new StartSecureUI.Request());
        if (executeNoLoad == null) {
            Log.m286e(TAG, "Error: start secure UI failed");
            return ERROR_EXECUTE_FAIL;
        }
        StartSecureUI.Response response = new StartSecureUI.Response(executeNoLoad);
        if (DEBUG) {
            Log.m285d(TAG, "Secure UI return " + response.mRetVal.retCode.get());
        }
        return (int) response.mRetVal.retCode.get();
    }

    public synchronized int resume() {
        int i;
        if (DEBUG) {
            Log.m285d(TAG, "Calling resume");
        }
        if (this.mPaymentHandle == null) {
            Log.m286e(TAG, "Error: Payment Handle is null");
            i = ERROR_NULL_PAYMENT_HANDLE;
        } else {
            TACommandResponse executeNoLoad = executeNoLoad(new Resume.Request(false));
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: load_and_execute failed");
                i = ERROR_EXECUTE_FAIL;
            } else {
                Resume.Response response = new Resume.Response(executeNoLoad);
                if (DEBUG) {
                    Log.m285d(TAG, "Resume return " + response.mRetVal.retCode.get());
                }
                i = (int) response.mRetVal.retCode.get();
                if (i == 0 && bQC) {
                    i = start_secure_touch();
                }
                if (SPAY_TPP_SETUP_PIN_VERIFIED == i) {
                    i = savePinSecureObjects(false);
                }
            }
        }
        return i;
    }

    public synchronized int resume(boolean z) {
        int i;
        if (DEBUG) {
            Log.m285d(TAG, "Calling resume, update_display_only = " + z);
        }
        if (this.mPaymentHandle == null) {
            Log.m286e(TAG, "Error: Payment Handle is null");
            i = ERROR_NULL_PAYMENT_HANDLE;
        } else {
            TACommandResponse executeNoLoad = executeNoLoad(new Resume.Request(z));
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: load_and_execute failed");
                i = ERROR_EXECUTE_FAIL;
            } else {
                Resume.Response response = new Resume.Response(executeNoLoad);
                if (DEBUG) {
                    Log.m285d(TAG, "Resume return " + response.mRetVal.retCode.get());
                }
                i = (int) response.mRetVal.retCode.get();
                if (i == 0 && bQC && !z) {
                    i = start_secure_touch();
                }
                if (SPAY_TPP_SETUP_PIN_VERIFIED == i) {
                    i = savePinSecureObjects(false);
                }
            }
        }
        return i;
    }

    public synchronized int verify() {
        int i = ERROR_NULL_PAYMENT_HANDLE;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling verify (unclose TUI)");
            }
            if (this.mPaymentHandle == null) {
                Log.m286e(TAG, "Error: Payment Handle is null");
            } else {
                TACommandResponse executeNoLoad = executeNoLoad(new Verify.Request(false, false));
                if (executeNoLoad == null) {
                    Log.m286e(TAG, "Error: load_and_execute failed");
                } else {
                    Verify.Response response = new Verify.Response(executeNoLoad);
                    if (DEBUG) {
                        Log.m285d(TAG, "Verify return " + response.mRetVal.retCode.get());
                    }
                    i = (int) response.mRetVal.retCode.get();
                    if (i == 0 && bQC) {
                        i = start_secure_touch();
                    }
                }
            }
        }
        return i;
    }

    public synchronized int verify(boolean z, boolean z2) {
        int i;
        if (DEBUG) {
            Log.m285d(TAG, "Calling verify: close_tui = " + z + " display_only = " + z2);
        }
        if (this.mPaymentHandle == null) {
            Log.m286e(TAG, "Error: PaymFent Handle is null");
            i = ERROR_NULL_PAYMENT_HANDLE;
        } else {
            TACommandResponse executeNoLoad = executeNoLoad(new Verify.Request(z, z2));
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: load_and_execute failed");
                i = ERROR_EXECUTE_FAIL;
            } else {
                Verify.Response response = new Verify.Response(executeNoLoad);
                if (DEBUG) {
                    Log.m285d(TAG, "Verify return " + response.mRetVal.retCode.get());
                }
                i = (int) response.mRetVal.retCode.get();
                if (i == 0 && bQC && !z2) {
                    i = start_secure_touch();
                }
            }
        }
        return i;
    }

    public synchronized byte[] getSecureResult(byte[] bArr, String str) {
        byte[] bArr2 = null;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling get secure result");
            }
            if (this.mPaymentHandle == null) {
                Log.m286e(TAG, "Error: Payment Handle is null");
            } else {
                try {
                    TACommandResponse executeNoLoad = executeNoLoad(new GetSecureResult.Request(bArr, str));
                    if (executeNoLoad == null) {
                        Log.m286e(TAG, "Error: load_and_execute failed");
                    } else {
                        GetSecureResult.Response response = new GetSecureResult.Response(executeNoLoad);
                        if (DEBUG) {
                            Log.m285d(TAG, "getSecureResult return " + response.mRetVal.retCode.get());
                        }
                        if (0 == response.mRetVal.retCode.get()) {
                            bArr2 = response.mRetVal.result_so.getData();
                        }
                    }
                } catch (IllegalArgumentException e) {
                    Log.m286e(TAG, e.toString());
                    e.printStackTrace();
                }
            }
        }
        return bArr2;
    }

    public synchronized int preloadFpSecureResult(byte[] bArr) {
        int i = ERROR_EXECUTE_FAIL;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling preloadFpSecureResult");
            }
            if (this.mPaymentHandle == null) {
                Log.m286e(TAG, "Error: Payment Handle is null");
                i = ERROR_NULL_PAYMENT_HANDLE;
            } else {
                try {
                    TACommandResponse executeNoLoad = executeNoLoad(new PreloadFpSecureResult.Request(bArr));
                    if (executeNoLoad == null) {
                        Log.m286e(TAG, "Error: load_and_execute failed");
                    } else {
                        PreloadFpSecureResult.Response response = new PreloadFpSecureResult.Response(executeNoLoad);
                        if (DEBUG) {
                            Log.m285d(TAG, "PreloadFpSecureResult return " + response.mRetVal.retCode.get());
                        }
                        if (0 == response.mRetVal.retCode.get()) {
                            i = (int) response.mRetVal.retCode.get();
                        }
                    }
                } catch (IllegalArgumentException e) {
                    Log.m286e(TAG, e.toString());
                    e.printStackTrace();
                }
            }
        }
        return i;
    }

    public synchronized int setBackgroundImg(byte[] bArr, int i, int i2) {
        int i3;
        if (DEBUG) {
            Log.m285d(TAG, "Calling setup background image");
        }
        if (this.mPaymentHandle == null) {
            Log.m286e(TAG, "Error: Payment Handle is null");
            i3 = ERROR_NULL_PAYMENT_HANDLE;
        } else {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new SetBackgroundImg.Request(bArr, i, i2));
                if (executeNoLoad == null) {
                    Log.m286e(TAG, "Error: load_and_execute failed");
                    i3 = ERROR_EXECUTE_FAIL;
                } else {
                    SetBackgroundImg.Response response = new SetBackgroundImg.Response(executeNoLoad);
                    if (DEBUG) {
                        Log.m285d(TAG, "Set background image return " + response.mRetVal.retCode.get());
                    }
                    i3 = (int) response.mRetVal.retCode.get();
                }
            } catch (IllegalArgumentException e) {
                Log.m286e(TAG, e.toString());
                e.printStackTrace();
                i3 = ERROR_INVALID_INPUT_PARAMS;
            }
        }
        return i3;
    }

    public synchronized int setCancelBtnImg(byte[] bArr, byte[] bArr2, int i, int i2) {
        int i3;
        if (DEBUG) {
            Log.m285d(TAG, "Calling set cancel");
        }
        if (this.mPaymentHandle == null) {
            Log.m286e(TAG, "Error: Payment Handle is null");
            i3 = ERROR_NULL_PAYMENT_HANDLE;
        } else {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new SetCancelBtn.Request(bArr, bArr2, i, i2));
                if (executeNoLoad == null) {
                    Log.m286e(TAG, "Error: load_and_execute failed");
                    i3 = ERROR_EXECUTE_FAIL;
                } else {
                    SetCancelBtn.Response response = new SetCancelBtn.Response(executeNoLoad);
                    if (DEBUG) {
                        Log.m285d(TAG, "Set cancel return " + response.mRetVal.retCode.get());
                    }
                    i3 = (int) response.mRetVal.retCode.get();
                }
            } catch (IllegalArgumentException e) {
                Log.m286e(TAG, e.toString());
                e.printStackTrace();
                i3 = ERROR_INVALID_INPUT_PARAMS;
            }
        }
        return i3;
    }

    public synchronized AuthNonce getCachedNonce(int i, boolean z) {
        AuthNonce authNonce = null;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling getCachedNonce");
            }
            if (this.mPaymentHandle == null) {
                Log.m286e(TAG, "Error: Payment Handle is null");
            } else if (this.mNonce != null && this.mNonce.length == i) {
                Log.m285d(TAG, "return existing nonce");
                authNonce = new AuthNonce(this.mNonce, true);
            } else if (z) {
                this.mNonce = getNonce(i);
                authNonce = new AuthNonce(this.mNonce, false);
            }
        }
        return authNonce;
    }

    public synchronized byte[] getNonce(int i) {
        byte[] bArr = null;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling get nonce");
            }
            this.mNonce = null;
            if (this.mPaymentHandle == null) {
                Log.m286e(TAG, "Error: Payment Handle is null");
            } else {
                TACommandResponse executeNoLoad = executeNoLoad(new GetNonce.Request(i));
                if (executeNoLoad == null) {
                    Log.m286e(TAG, "Error: load_and_execute failed");
                } else {
                    GetNonce.Response response = new GetNonce.Response(executeNoLoad);
                    if (DEBUG) {
                        Log.m285d(TAG, "get Nonce return " + response.mRetVal.retCode.get());
                    }
                    if (response.mRetVal.retCode.get() == 0) {
                        bArr = response.mRetVal.nonce.getData();
                    }
                }
            }
        }
        return bArr;
    }

    public synchronized AuthResult getAuthResult(byte[] bArr, String str, byte[] bArr2) {
        AuthResult authResult = null;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling get auth result");
            }
            this.mNonce = null;
            if (this.mPaymentHandle == null) {
                Log.m286e(TAG, "Error: Payment Handle is null");
            } else if (bArr == null || str == null || bArr2 == null) {
                Log.m286e(TAG, "invalid input to getAuthResult()");
            } else {
                try {
                    TACommandResponse executeNoLoad = executeNoLoad(new GetAuthResult.Request(bArr, str, bArr2));
                    if (executeNoLoad == null) {
                        Log.m286e(TAG, "Error: load_and_execute failed");
                    } else {
                        String str2;
                        GetAuthResult.Response response = new GetAuthResult.Response(executeNoLoad);
                        if (DEBUG) {
                            Log.m285d(TAG, "get auth result return " + response.mRetVal.retCode.get());
                        }
                        if (response.mRetVal.authType.get() == 0) {
                            str2 = PaymentNetworkProvider.AUTHTYPE_TRUSTED_PIN;
                        } else if (response.mRetVal.authType.get() == 1) {
                            str2 = PaymentNetworkProvider.AUTHTYPE_FP;
                        } else if (response.mRetVal.authType.get() == 2) {
                            str2 = PaymentNetworkProvider.AUTHTYPE_BACKUPPASSWORD;
                        } else if (response.mRetVal.authType.get() == 3) {
                            str2 = PaymentNetworkProvider.AUTHTYPE_IRIS;
                        } else {
                            str2 = "NONE";
                        }
                        if (response.mRetVal.retCode.get() == 0) {
                            if (response.mRetVal.update_pin_so.get() != (short) 0) {
                                Log.m287i(TAG, "getAuthResult: Existing PIN len=" + response.mRetVal.existPinLen.get());
                                mPinExist = response.mRetVal.existPinLen.get() > 0;
                                if (DEBUG) {
                                    Log.m285d(TAG, "Need to update PIN SO! PIN exist: " + mPinExist + " pin len: " + response.mRetVal.existPinLen.get());
                                }
                                Log.m287i(TAG, "getAuthResult: savePinSo");
                                savePinSo(response.mRetVal.pin_so.getData());
                            } else if (DEBUG) {
                                Log.m285d(TAG, "No need to update PIN SO");
                            }
                            authResult = new AuthResult(response.mRetVal.so.getData(), str2);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    Log.m286e(TAG, e.toString());
                    e.printStackTrace();
                }
            }
        }
        return authResult;
    }

    private int closeTui() {
        if (DEBUG) {
            Log.m285d(TAG, "Calling closeTui");
        }
        if (this.mPaymentHandle == null) {
            Log.m286e(TAG, "Error: Payment Handle is null");
            return ERROR_NULL_PAYMENT_HANDLE;
        }
        TACommandResponse executeNoLoad = executeNoLoad(new CloseTui.Request());
        if (executeNoLoad == null) {
            Log.m286e(TAG, "Error: load_and_execute failed");
            return ERROR_EXECUTE_FAIL;
        }
        CloseTui.Response response = new CloseTui.Response(executeNoLoad);
        if (DEBUG) {
            Log.m285d(TAG, "closeTui return " + response.mRetVal.retCode.get());
        }
        return (int) response.mRetVal.retCode.get();
    }

    public synchronized void unloadTA() {
        this.mNonce = null;
        if (this.mPaymentHandle != null) {
            closeTui();
            super.unloadTA();
        } else if (DEBUG) {
            Log.m285d(TAG, "unloadTA: mPaymentHandle is null");
        }
    }

    public synchronized byte[] getPinRandom(String str) {
        byte[] bArr = null;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling get PIN random");
            }
            if (this.mPaymentHandle == null) {
                Log.m286e(TAG, "Error: Payment Handle is null");
            } else {
                TACommandResponse executeNoLoad = executeNoLoad(new GetPinRandom.Request(str));
                if (executeNoLoad == null) {
                    Log.m286e(TAG, "Error: load_and_execute failed");
                } else {
                    GetPinRandom.Response response = new GetPinRandom.Response(executeNoLoad);
                    if (DEBUG) {
                        Log.m285d(TAG, "getPinRandom return " + response.mRetVal.retCode.get() + "auth_type = " + response.mRetVal.authType.get());
                    }
                    bArr = response.mRetVal.result_so.getData();
                }
            }
        }
        return bArr;
    }

    public synchronized int checkTuiSession() {
        int i;
        if (DEBUG) {
            Log.m285d(TAG, "Calling checkTuiSession");
        }
        if (this.mPaymentHandle == null) {
            Log.m286e(TAG, "Error: Payment Handle is null");
            i = ERROR_NULL_PAYMENT_HANDLE;
        } else {
            TACommandResponse executeNoLoad = executeNoLoad(new CheckTUISession.Request());
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: load_and_execute failed");
                i = ERROR_EXECUTE_FAIL;
            } else {
                CheckTUISession.Response response = new CheckTUISession.Response(executeNoLoad);
                if (DEBUG) {
                    Log.m285d(TAG, "CheckTUISession return " + response.mRetVal.retCode.get());
                }
                i = (int) response.mRetVal.retCode.get();
            }
        }
        return i;
    }

    public synchronized int clearTuiState() {
        int i;
        if (DEBUG) {
            Log.m285d(TAG, "Calling clearTuiState");
        }
        if (this.mPaymentHandle == null) {
            Log.m286e(TAG, "Error: Payment Handle is null");
            i = ERROR_NULL_PAYMENT_HANDLE;
        } else {
            TACommandResponse executeNoLoad = executeNoLoad(new ClearState.Request());
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: load_    and_execute failed");
                i = ERROR_EXECUTE_FAIL;
            } else {
                ClearState.Response response = new ClearState.Response(executeNoLoad);
                if (DEBUG) {
                    Log.m285d(TAG, "clearTuiState return " + response.mRetVal.retCode.get());
                }
                i = (int) response.mRetVal.retCode.get();
            }
        }
        return i;
    }

    public synchronized int updateFP(byte[] bArr) {
        int i;
        if (DEBUG) {
            Log.m285d(TAG, "Calling UpateFP with secure object");
        }
        if (this.mPaymentHandle == null) {
            Log.m286e(TAG, "Error: Payment Handle is null");
            i = ERROR_NULL_PAYMENT_HANDLE;
        } else {
            TACommandResponse executeNoLoad = executeNoLoad(new UpdateFP.Request(bArr));
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: load_and_execute failed");
                i = ERROR_EXECUTE_FAIL;
            } else {
                UpdateFP.Response response = new UpdateFP.Response(executeNoLoad);
                if (DEBUG) {
                    Log.m285d(TAG, "UpdateFP return " + response.mRetVal.retCode.get());
                }
                if (response.mRetVal.retCode.get() == 0) {
                    if (response.mRetVal.update_pin_so.get() != (short) 0) {
                        Log.m287i(TAG, "updateFP: Existing Pin len=" + response.mRetVal.existPinLen.get());
                        mPinExist = response.mRetVal.existPinLen.get() > 0;
                        if (DEBUG) {
                            Log.m285d(TAG, "Need to update PIN SO! PIN exist: " + mPinExist + " pin len: " + response.mRetVal.existPinLen.get());
                        }
                        Log.m287i(TAG, "updateFP: savePinSo");
                        savePinSo(response.mRetVal.pin_so.getData());
                    } else if (DEBUG) {
                        Log.m285d(TAG, "No need to update PIN SO");
                    }
                }
                i = (int) response.mRetVal.retCode.get();
            }
        }
        return i;
    }

    public int inAppConfirm(String str, String str2, String str3, String str4, String str5, String str6, byte[] bArr) {
        if (str == null || str2 == null || str3 == null || str4 == null || str5 == null || str6 == null || bArr == null || bArr.length == 0) {
            Log.m286e(TAG, "inAppConfirm input is null");
            return ERROR_EXECUTE_FAIL;
        }
        TextPaint c05934 = new C05934();
        TextPaint c05945 = new C05945();
        Rect rect = new Rect();
        c05945.getTextBounds(str2, 0, str2.length(), rect);
        Log.m285d(TAG, "store length=" + ((128.0f * getDensity()) + ((float) rect.width())));
        if (((float) rect.width()) + (128.0f * getDensity()) > 360.0f * getDensity()) {
            str2 = str2.substring(0, str2.length() / 2) + "...";
        }
        TextPaint c05956 = new C05956();
        TextPaint c05967 = new C05967();
        TextPaint c05978 = new C05978();
        TextPaint c05989 = new C05989();
        byte[] text2png = text2png(str, c05934, Color.argb(0, 246, 246, 246), true);
        byte[] text2png2 = text2png(str2, c05945, Color.argb(GF2Field.MASK, 250, 250, 250), false);
        byte[] text2png3 = text2png(str3, c05956, Color.argb(GF2Field.MASK, 250, 250, 250), false);
        byte[] text2png4 = text2png(str4, c05967, Color.argb(GF2Field.MASK, 250, 250, 250), false);
        byte[] text2png5 = text2png(str5, c05978, Color.argb(0, 250, 250, 250), false);
        byte[] text2png6 = text2png(str6, c05989, Color.argb(0, 250, 250, 250), false);
        if (text2png == null || text2png2 == null || text2png3 == null || text2png4 == null || text2png5 == null || text2png6 == null) {
            Log.m286e(TAG, "inAppConfirm text2png is null");
            return ERROR_EXECUTE_FAIL;
        }
        Log.m285d(TAG, "Lengthes: " + text2png.length + " " + text2png2.length + " " + text2png3.length + " " + text2png4.length + " " + text2png5.length + " " + text2png6.length + " " + bArr.length);
        if (((((((text2png.length + text2png2.length) + text2png3.length) + text2png4.length) + text2png5.length) + text2png6.length) + bArr.length) + 14 > SpayTuiTACommands.RESOURCE_BUFFER_SIZE) {
            Log.m286e(TAG, "Out of buffer");
            return ERROR_EXECUTE_FAIL;
        }
        TACommandResponse executeNoLoad = executeNoLoad(new InAppConfirm.Request(text2png, text2png2, text2png3, text2png4, text2png5, text2png6, bArr));
        if (executeNoLoad == null) {
            Log.m286e(TAG, "Error: load_and_execute failed");
            return ERROR_EXECUTE_FAIL;
        }
        InAppConfirm.Response response = new InAppConfirm.Response(executeNoLoad);
        if (DEBUG) {
            Log.m285d(TAG, "InAppConfirm return " + response.mRetVal.retCode.get());
        }
        int i = (int) response.mRetVal.retCode.get();
        if (i == 0 && bQC) {
            return start_secure_touch();
        }
        return i;
    }

    public int merchantSecureDisplay(byte[] bArr, int i, int i2, int[] iArr) {
        if (bArr == null || bArr.length == 0 || iArr == null || iArr.length == 0) {
            Log.m286e(TAG, "merchantSecureDisplay input is null");
            return ERROR_EXECUTE_FAIL;
        }
        Log.m285d(TAG, "merchantSecureDisplay: totalSize=" + i + " index=" + i2 + " screenPNG.length=" + bArr.length + " coordinates.length=" + iArr.length);
        if (i2 == 0) {
            mScreenPNGBuf = new byte[i];
        }
        System.arraycopy(bArr, 0, mScreenPNGBuf, i2, bArr.length);
        if (bArr.length + i2 < i) {
            Log.m285d(TAG, "merchantSecureDisplay: transfer not finished!");
            return 0;
        }
        int i3;
        Log.m285d(TAG, "merchantSecureDisplay: transfer finished!");
        Log.m285d(TAG, "merchantSecureDisplay before computation");
        byte[] bArr2 = new byte[((iArr.length * 3) + 5)];
        bArr2[0] = (byte) ((mScreenPNGBuf.length >> 16) & GF2Field.MASK);
        bArr2[1] = (byte) ((mScreenPNGBuf.length >> 8) & GF2Field.MASK);
        bArr2[2] = (byte) (mScreenPNGBuf.length & GF2Field.MASK);
        bArr2[3] = (byte) ((iArr.length >> 8) & GF2Field.MASK);
        bArr2[4] = (byte) (iArr.length & GF2Field.MASK);
        int i4 = 5;
        for (i3 = 0; i3 < iArr.length; i3 += 4) {
            int i5 = 0;
            while (i5 < 4) {
                int i6 = i4 + 1;
                bArr2[i4] = (byte) ((iArr[i3 + i5] >> 16) & GF2Field.MASK);
                i4 = i6 + 1;
                bArr2[i6] = (byte) ((iArr[i3 + i5] >> 8) & GF2Field.MASK);
                i6 = i4 + 1;
                bArr2[i4] = (byte) (iArr[i3 + i5] & GF2Field.MASK);
                i5++;
                i4 = i6;
            }
        }
        Log.m285d(TAG, "merchantSecureDisplay after computation");
        TACommandResponse executeNoLoad = executeNoLoad(new MerchantSecureDisplay.Request(bArr2));
        if (executeNoLoad == null) {
            Log.m286e(TAG, "Error: load_and_execute failed");
            return ERROR_EXECUTE_FAIL;
        }
        i3 = (int) new MerchantSecureDisplay.Response(executeNoLoad).mRetVal.retCode.get();
        if (i3 != 0 && i3 != 3) {
            return i3;
        }
        Log.m285d(TAG, "Initial transfer size=" + mScreenPNGBuf.length);
        i4 = mScreenPNGBuf.length;
        i5 = 0;
        int i7 = i3;
        while (i4 > 0) {
            if (i4 + 3 > SpayTuiTACommands.RESOURCE_BUFFER_SIZE) {
                i3 = 255997;
                i4 -= 255997;
            } else {
                i3 = i4;
                i4 = 0;
            }
            Object obj = new byte[i3];
            Log.m285d(TAG, "size=" + i3 + " left=" + i4 + " tmpBuf=" + obj.length);
            System.arraycopy(mScreenPNGBuf, i5, obj, 0, i3);
            i3 += i5;
            TACommandResponse executeNoLoad2 = executeNoLoad(new MerchantSecureDisplay.Request(obj));
            if (executeNoLoad2 == null) {
                Log.m286e(TAG, "Error: load_and_execute failed");
                return i7;
            }
            i7 = (int) new MerchantSecureDisplay.Response(executeNoLoad2).mRetVal.retCode.get();
            Log.m285d(TAG, "Response.mRetVal.retCode.get()=" + i7);
            i5 = i3;
        }
        return i7;
    }

    public int[] merchantSecureTouch() {
        int[] iArr = new int[]{ERROR_EXECUTE_FAIL, 0};
        Log.m285d(TAG, "Before start_secure_touch()");
        if (bQC) {
            iArr[0] = start_secure_touch();
        } else {
            TACommandResponse executeNoLoad = executeNoLoad(new MerchantSecureTouch.Request());
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: load_and_execute failed");
                return iArr;
            }
            iArr[0] = (int) new MerchantSecureTouch.Response(executeNoLoad).mRetVal.retCode.get();
        }
        if ((iArr[0] & 1044480) == 417792) {
            iArr[1] = iArr[0] & 4095;
            iArr[0] = 0;
        }
        Log.m285d(TAG, "After start_secure_touch(): ret[0]=" + iArr[0] + ", ret[1]=" + iArr[1]);
        return iArr;
    }

    public int merchantSecureInit(int i, int[] iArr) {
        if (iArr == null || iArr.length == 0) {
            Log.m286e(TAG, "merchantSecureInit input is null");
            return ERROR_EXECUTE_FAIL;
        }
        Log.m285d(TAG, "merchantSecureInit before computation totalSize=" + i + " coordinates.len=" + iArr.length);
        byte[] bArr = new byte[((iArr.length * 3) + 5)];
        bArr[0] = (byte) ((i >> 16) & GF2Field.MASK);
        bArr[1] = (byte) ((i >> 8) & GF2Field.MASK);
        bArr[2] = (byte) (i & GF2Field.MASK);
        bArr[3] = (byte) ((iArr.length >> 8) & GF2Field.MASK);
        bArr[4] = (byte) (iArr.length & GF2Field.MASK);
        int i2 = 5;
        for (int i3 = 0; i3 < iArr.length; i3 += 4) {
            int i4 = 0;
            while (i4 < 4) {
                int i5 = i2 + 1;
                bArr[i2] = (byte) ((iArr[i3 + i4] >> 16) & GF2Field.MASK);
                i2 = i5 + 1;
                bArr[i5] = (byte) ((iArr[i3 + i4] >> 8) & GF2Field.MASK);
                i5 = i2 + 1;
                bArr[i2] = (byte) (iArr[i3 + i4] & GF2Field.MASK);
                i4++;
                i2 = i5;
            }
        }
        Log.m285d(TAG, "merchantSecureInit after computation");
        TACommandResponse executeNoLoad = executeNoLoad(new MerchantSecureInit.Request(bArr));
        if (executeNoLoad != null) {
            return (int) new MerchantSecureInit.Response(executeNoLoad).mRetVal.retCode.get();
        }
        Log.m286e(TAG, "Error: load_and_execute failed");
        return ERROR_EXECUTE_FAIL;
    }

    public int merchantSendSecureImg(byte[] bArr, int i, int i2, int i3, int i4) {
        if (bArr == null || bArr.length == 0) {
            Log.m286e(TAG, "merchantSendSecureImg input is null");
            return ERROR_EXECUTE_FAIL;
        }
        Log.m285d(TAG, "merchantSendSecureImg: totalSize=" + i + " index=" + i2 + " screenPNG.length=" + bArr.length + " x=" + i3 + " y=" + i4);
        if (i2 == 0) {
            mScreenPNGBuf = new byte[i];
        }
        System.arraycopy(bArr, 0, mScreenPNGBuf, i2, bArr.length);
        if (bArr.length + i2 < i) {
            Log.m285d(TAG, "merchantSendSecureImg: transfer not finished!");
            return 0;
        }
        Log.m285d(TAG, "merchantSendSecureImg: transfer finished!");
        Log.m285d(TAG, "merchantSendSecureImg before computation");
        byte[] bArr2 = new byte[]{(byte) ((mScreenPNGBuf.length >> 16) & GF2Field.MASK), (byte) ((mScreenPNGBuf.length >> 8) & GF2Field.MASK), (byte) (mScreenPNGBuf.length & GF2Field.MASK), (byte) ((i3 >> 16) & GF2Field.MASK), (byte) ((i3 >> 8) & GF2Field.MASK), (byte) (i3 & GF2Field.MASK), (byte) ((i4 >> 16) & GF2Field.MASK), (byte) ((i4 >> 8) & GF2Field.MASK), (byte) (i4 & GF2Field.MASK)};
        Log.m285d(TAG, "merchantSendSecureImg after computation");
        TACommandResponse executeNoLoad = executeNoLoad(new MerchantSendSecureImg.Request(bArr2));
        if (executeNoLoad == null) {
            Log.m286e(TAG, "Error: load_and_execute failed");
            return ERROR_EXECUTE_FAIL;
        }
        int i5 = (int) new MerchantSendSecureImg.Response(executeNoLoad).mRetVal.retCode.get();
        if (i5 != 0 && i5 != 3) {
            return i5;
        }
        Log.m285d(TAG, "Initial transfer size=" + mScreenPNGBuf.length);
        int length = mScreenPNGBuf.length;
        int i6 = 0;
        int i7 = i5;
        while (length > 0) {
            if (length + 3 > SpayTuiTACommands.RESOURCE_BUFFER_SIZE) {
                i5 = 255997;
                length -= 255997;
            } else {
                i5 = length;
                length = 0;
            }
            Object obj = new byte[i5];
            Log.m285d(TAG, "size=" + i5 + " left=" + length + " tmpBuf=" + obj.length);
            System.arraycopy(mScreenPNGBuf, i6, obj, 0, i5);
            i5 += i6;
            TACommandResponse executeNoLoad2 = executeNoLoad(new MerchantSendSecureImg.Request(obj));
            if (executeNoLoad2 == null) {
                Log.m286e(TAG, "Error: load_and_execute failed");
                return i7;
            }
            i7 = (int) new MerchantSendSecureImg.Response(executeNoLoad2).mRetVal.retCode.get();
            Log.m285d(TAG, " =" + i7);
            i6 = i5;
        }
        return i7;
    }

    private byte[] text2png(String[] strArr, TextPaint textPaint, int i, boolean z) {
        Bitmap createBitmap;
        Rect rect = new Rect();
        int i2 = ERROR_NULL_PAYMENT_HANDLE;
        int i3 = 0;
        float f = getContext().getResources().getDisplayMetrics().density;
        int fontSpacing = (int) (((double) textPaint.getFontSpacing()) * 0.4d);
        for (int i4 = 0; i4 < strArr.length; i4++) {
            textPaint.getTextBounds(strArr[i4], 0, strArr[i4].length(), rect);
            if (DEBUG) {
                Log.m285d(TAG, "bounds.width = " + rect.width() + "  bounds.height = " + rect.height());
            }
            if (i2 < rect.width()) {
                i2 = rect.width();
            }
            i3 += rect.height();
        }
        if (DEBUG) {
            Log.m285d(TAG, "maxWidth = " + i2 + "  totalHeight = " + i3);
        }
        if (z) {
            if (DEBUG) {
                Log.m285d(TAG, "actionBarTextWidth = " + this.actionBarTextWidth + "  actionBarTextWidth = " + this.actionBarTextWidth);
            }
            if (i2 > this.actionBarTextWidth) {
                if (DEBUG) {
                    Log.m285d(TAG, "Change action bar text width from " + this.actionBarTextWidth + " to " + i2);
                }
                this.actionBarTextWidth = i2;
                if (DEBUG) {
                    Log.m285d(TAG, "actionBarTextWidth = " + this.actionBarTextWidth + "  actionBarTextWidth = " + this.actionBarTextWidth);
                }
            }
            if (((strArr.length + ERROR_NULL_PAYMENT_HANDLE) * fontSpacing) + i3 > this.actionBarTextHeight) {
                this.actionBarTextHeight = ((strArr.length + ERROR_NULL_PAYMENT_HANDLE) * fontSpacing) + i3;
                if (DEBUG) {
                    Log.m285d(TAG, "actionBarTextHeight = " + this.actionBarTextHeight + "  totalHeight = " + i3);
                    Log.m285d(TAG, "spacing = " + fontSpacing + "  lines.length = " + strArr.length);
                }
            }
            if (this.actionBarTextWidth + 2 <= 0 || this.actionBarTextHeight <= 0) {
                Log.m286e(TAG, "Create Bitmap - Illegal Argument : check input");
                return null;
            }
            createBitmap = Bitmap.createBitmap(this.actionBarTextWidth + 2, this.actionBarTextHeight, Config.ARGB_8888);
        } else if (i2 + 2 <= 0 || ((strArr.length + ERROR_NULL_PAYMENT_HANDLE) * fontSpacing) + i3 <= 0) {
            Log.m286e(TAG, "Create Bitmap - Illegal Argument : check input");
            return null;
        } else {
            createBitmap = Bitmap.createBitmap(i2 + 2, i3 + ((strArr.length + ERROR_NULL_PAYMENT_HANDLE) * fontSpacing), Config.ARGB_8888);
        }
        createBitmap.eraseColor(i);
        Canvas canvas = new Canvas(createBitmap);
        int i5 = 0;
        for (i3 = 0; i3 < strArr.length; i3++) {
            int width;
            textPaint.getTextBounds(strArr[i3], 0, strArr[i3].length(), rect);
            if (DEBUG) {
                Log.m285d(TAG, "Draw line " + i3 + " width: " + rect.width() + "  at ( " + ((-rect.left) + ((i2 - rect.width()) / 2)) + " , " + (-rect.top) + i5 + " )");
            }
            if ((z && Utils.isRTL()) || Utils.aq(getContext())) {
                Log.m289v(TAG, "action bar info, current language is RTL, so text2png uses Align.RIGHT and uses RTL version xPos.");
                textPaint.setTextAlign(Align.RIGHT);
                width = canvas.getWidth();
            } else {
                Log.m289v(TAG, "text2png uses LTR version xPos.");
                width = (-rect.left) + ((i2 - rect.width()) / 2);
            }
            canvas.drawText(strArr[i3], (float) width, (float) ((-rect.top) + i5), textPaint);
            i5 += rect.height() + fontSpacing;
        }
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        createBitmap.compress(CompressFormat.PNG, 0, byteArrayOutputStream);
        createBitmap.recycle();
        return byteArrayOutputStream.toByteArray();
    }

    private byte[] text2png(String str, TextPaint textPaint, int i, boolean z) {
        return text2png(new String[]{str}, textPaint, i, z);
    }
}
