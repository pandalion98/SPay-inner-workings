/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.AssetManager
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$CompressFormat
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
 *  android.graphics.Color
 *  android.graphics.Paint
 *  android.graphics.Paint$Align
 *  android.graphics.Rect
 *  android.graphics.Typeface
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.spay.ITAController
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  android.text.TextPaint
 *  android.util.DisplayMetrics
 *  android.util.TypedValue
 *  android.view.Display
 *  android.view.WindowManager
 *  java.io.ByteArrayOutputStream
 *  java.io.File
 *  java.io.FileInputStream
 *  java.io.OutputStream
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.Arrays
 *  java.util.List
 *  java.util.Locale
 */
package com.samsung.android.spaytui;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.spay.TACommandResponse;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.utils.h;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SpayTuiTAController
extends TAController {
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
    public static TAInfo TA_INFO = new SpayTuiTAInfo();
    public static final int TIMA_ERROR_TUI_CANCELLED = 20480;
    private static final String TUIPINSECUREOBJECTFILE = "mpt.dat";
    private static final String TUI_DATA_DIR = TAController.getEfsDirectory();
    private static final boolean bQC;
    private static Context mContext;
    private static SpayTuiTAController mInstance;
    private static boolean mPinExist;
    private static boolean mResetPending;
    private static byte[] mScreenPNGBuf;
    private final int DEFAULT_SW360 = 360;
    private int actionBarTextHeight = 0;
    private int actionBarTextWidth = 0;
    private final float actionbar_text = 19.0f;
    private byte[] mNonce = null;
    private List<TAController> mSupportTAs;
    private final float securemode_text = 16.0f;

    static {
        mPinExist = false;
        bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
        mResetPending = false;
    }

    private SpayTuiTAController(Context context) {
        super(context, TA_INFO);
    }

    private int closeTui() {
        if (DEBUG) {
            Log.d(TAG, "Calling closeTui");
        }
        if (this.mPaymentHandle == null) {
            Log.e(TAG, "Error: Payment Handle is null");
            return -1;
        }
        TACommandResponse tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.CloseTui.Request());
        if (tACommandResponse == null) {
            Log.e(TAG, "Error: load_and_execute failed");
            return -2;
        }
        SpayTuiTACommands.CloseTui.Response response = new SpayTuiTACommands.CloseTui.Response(tACommandResponse);
        if (DEBUG) {
            Log.d(TAG, "closeTui return " + response.mRetVal.retCode.get());
        }
        return (int)response.mRetVal.retCode.get();
    }

    public static SpayTuiTAController createOnlyInstance(Context context) {
        Class<SpayTuiTAController> class_ = SpayTuiTAController.class;
        synchronized (SpayTuiTAController.class) {
            mContext = context;
            if (mInstance == null) {
                mInstance = new SpayTuiTAController(context);
            }
            SpayTuiTAController spayTuiTAController = mInstance;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return spayTuiTAController;
        }
    }

    private void deletePinSo() {
        new File(TUI_DATA_DIR, TUIPINSECUREOBJECTFILE).delete();
    }

    private float getDensity() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager)mContext.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        Log.d(TAG, "getDensity " + displayMetrics.density);
        return displayMetrics.density;
    }

    private int getDensityDpi() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager)mContext.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        Log.d(TAG, "getDensityDpi " + displayMetrics.densityDpi);
        return displayMetrics.densityDpi;
    }

    public static SpayTuiTAController getInstance() {
        Class<SpayTuiTAController> class_ = SpayTuiTAController.class;
        synchronized (SpayTuiTAController.class) {
            SpayTuiTAController spayTuiTAController = mInstance;
            // ** MonitorExit[var2] (shouldn't be in output)
            return spayTuiTAController;
        }
    }

    private static final boolean isAndroidN() {
        return Build.VERSION.SDK_INT == 24;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static byte[] readFile(File var0) {
        var1_1 = new byte[(int)var0.length()];
        var2_2 = new FileInputStream(var0);
        var3_3 = 0;
        try {
            ** GOTO lbl28
        }
        catch (Throwable var8_8) {
            try {
                throw var8_8;
            }
            catch (Throwable var9_9) {
                block17 : {
                    var5_10 = var8_8;
                    var4_11 = var9_9;
                    break block17;
                    catch (Throwable var4_12) {
                        var5_10 = null;
                    }
                }
                if (var2_2 == null) throw var4_11;
                if (var5_10 == null) ** GOTO lbl26
                try {
                    var2_2.close();
                }
                catch (Throwable var7_13) {
                    var5_10.addSuppressed(var7_13);
                    throw var4_11;
                }
                throw var4_11;
lbl26: // 1 sources:
                var2_2.close();
                throw var4_11;
lbl28: // 2 sources:
                for (var10_4 = (int)var0.length(); var10_4 > 0; var3_3 += var11_5, var10_4 -= var11_5) {
                    var11_5 = var2_2.read(var1_1, var3_3, var10_4);
                }
                if (var2_2 == null) return var1_1;
                if (false) {
                    try {
                        var2_2.close();
                        return var1_1;
                    }
                    catch (Throwable var12_6) {
                        null.addSuppressed(var12_6);
                        return var1_1;
                    }
                }
                try {
                    var2_2.close();
                    return var1_1;
                }
                catch (Exception var6_7) {
                    if (SpayTuiTAController.DEBUG) {
                        var6_7.printStackTrace();
                    }
                    if (SpayTuiTAController.DEBUG == false) return null;
                    Log.d("SpayTuiTAController", "No Pin set up yet");
                    return null;
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private int savePinSecureObjects(boolean bl) {
        mPinExist = false;
        byte[] arrby = bl ? this.generateRandom() : this.getPinSO();
        if (arrby == null) {
            Log.e(TAG, "Failed to get PIN/random secure object!");
            return 393217;
        }
        Log.d(TAG, "PIN/random secure object size " + arrby.length);
        Log.i(TAG, "savePinSecureObjects: savePinSo");
        int n2 = this.savePinSo(arrby);
        if (n2 != 0) {
            Log.e(TAG, "Failed to save PIN SO for PIN!");
            return n2;
        }
        return this.writePinRandomFiles(true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private int savePinSo(byte[] arrby) {
        File file = new File(TUI_DATA_DIR, TUIPINSECUREOBJECTFILE);
        try {
            byte[] arrby2 = new byte[1 + arrby.length];
            arrby2[0] = !mPinExist ? (byte)0 : 1;
            System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)1, (int)arrby.length);
            SpayTuiTAController.writeFile(arrby2, file);
            return 0;
        }
        catch (Exception exception) {
            Log.e(TAG, "Failed to save PIN SO!");
            exception.printStackTrace();
            return 393217;
        }
    }

    private int start_secure_touch() {
        if (bQC) {
            TACommandResponse tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.StartSecureUI.Request());
            if (tACommandResponse == null) {
                Log.e(TAG, "Error: start secure UI failed");
                return -2;
            }
            SpayTuiTACommands.StartSecureUI.Response response = new SpayTuiTACommands.StartSecureUI.Response(tACommandResponse);
            if (DEBUG) {
                Log.d(TAG, "Secure UI return " + response.mRetVal.retCode.get());
            }
            return (int)response.mRetVal.retCode.get();
        }
        return 0;
    }

    private byte[] text2png(String string, TextPaint textPaint, int n2, boolean bl) {
        return this.text2png(new String[]{string}, textPaint, n2, bl);
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] text2png(String[] arrstring, TextPaint textPaint, int n2, boolean bl) {
        Bitmap bitmap;
        Rect rect = new Rect();
        int n3 = -1;
        int n4 = 0;
        int n5 = (int)(0.4 * (double)textPaint.getFontSpacing());
        for (int i2 = 0; i2 < arrstring.length; n4 += rect.height(), ++i2) {
            textPaint.getTextBounds(arrstring[i2], 0, arrstring[i2].length(), rect);
            if (DEBUG) {
                Log.d(TAG, "bounds.width = " + rect.width() + "  bounds.height = " + rect.height());
            }
            if (n3 >= rect.width()) continue;
            n3 = rect.width();
        }
        if (DEBUG) {
            Log.d(TAG, "maxWidth = " + n3 + "  totalHeight = " + n4);
        }
        if (bl) {
            if (DEBUG) {
                Log.d(TAG, "actionBarTextWidth = " + this.actionBarTextWidth + "  actionBarTextWidth = " + this.actionBarTextWidth);
            }
            if (n3 > this.actionBarTextWidth) {
                if (DEBUG) {
                    Log.d(TAG, "Change action bar text width from " + this.actionBarTextWidth + " to " + n3);
                }
                this.actionBarTextWidth = n3;
                if (DEBUG) {
                    Log.d(TAG, "actionBarTextWidth = " + this.actionBarTextWidth + "  actionBarTextWidth = " + this.actionBarTextWidth);
                }
            }
            if (n4 + n5 * (-1 + arrstring.length) > this.actionBarTextHeight) {
                this.actionBarTextHeight = n4 + n5 * (-1 + arrstring.length);
                if (DEBUG) {
                    Log.d(TAG, "actionBarTextHeight = " + this.actionBarTextHeight + "  totalHeight = " + n4);
                    Log.d(TAG, "spacing = " + n5 + "  lines.length = " + arrstring.length);
                }
            }
            if (2 + this.actionBarTextWidth <= 0 || this.actionBarTextHeight <= 0) {
                Log.e(TAG, "Create Bitmap - Illegal Argument : check input");
                return null;
            }
            bitmap = Bitmap.createBitmap((int)(2 + this.actionBarTextWidth), (int)this.actionBarTextHeight, (Bitmap.Config)Bitmap.Config.ARGB_8888);
        } else {
            if (n3 + 2 <= 0 || n4 + n5 * (-1 + arrstring.length) <= 0) {
                Log.e(TAG, "Create Bitmap - Illegal Argument : check input");
                return null;
            }
            bitmap = Bitmap.createBitmap((int)(n3 + 2), (int)(n4 + n5 * (-1 + arrstring.length)), (Bitmap.Config)Bitmap.Config.ARGB_8888);
        }
        bitmap.eraseColor(n2);
        Canvas canvas = new Canvas(bitmap);
        int n6 = 0;
        int n7 = 0;
        do {
            int n8;
            if (n7 >= arrstring.length) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, (OutputStream)byteArrayOutputStream);
                bitmap.recycle();
                return byteArrayOutputStream.toByteArray();
            }
            textPaint.getTextBounds(arrstring[n7], 0, arrstring[n7].length(), rect);
            if (DEBUG) {
                Log.d(TAG, "Draw line " + n7 + " width: " + rect.width() + "  at ( " + (-rect.left + (n3 - rect.width()) / 2) + " , " + -rect.top + n6 + " )");
            }
            if (bl && h.isRTL() || h.aq(this.getContext())) {
                Log.v(TAG, "action bar info, current language is RTL, so text2png uses Align.RIGHT and uses RTL version xPos.");
                textPaint.setTextAlign(Paint.Align.RIGHT);
                n8 = canvas.getWidth();
            } else {
                Log.v(TAG, "text2png uses LTR version xPos.");
                n8 = -rect.left + (n3 - rect.width()) / 2;
            }
            canvas.drawText(arrstring[n7], (float)n8, (float)(n6 + -rect.top), (Paint)textPaint);
            n6 += n5 + rect.height();
            ++n7;
        } while (true);
    }

    /*
     * Exception decompiling
     */
    private static boolean writeFile(byte[] var0, File var1_1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
        // org.benf.cfr.reader.b.a.a.j.b(Op04StructuredStatement.java:409)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:487)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    private int writePinRandomFiles(boolean bl) {
        for (TAController tAController : this.mSupportTAs) {
            String string;
            File file;
            byte[] arrby;
            if (tAController == null) {
                Log.e(TAG, "Error: TAController is null. Not created yet");
                continue;
            }
            if (!tAController.usesPinRandom()) {
                Log.d(TAG, "This TA do not use Pin Random");
                continue;
            }
            TAInfo tAInfo = tAController.getTAInfo();
            if (!bl && (file = new File(TUI_DATA_DIR, tAInfo.getPinRandomFileName())).exists() && !file.isDirectory()) {
                Log.d(TAG, "Pin Random already exist for this TA");
                continue;
            }
            if (bQC) {
                Log.d(TAG, "For QC only, trying to load TA: " + tAController.getTAInfo().getTAFileName());
                if (!tAController.loadTA()) {
                    Log.d(TAG, "Failed to load TA " + tAController.getTAInfo().getTAFileName());
                    continue;
                }
            }
            if ((arrby = this.getPinRandom(string = tAInfo.getTAId())) != null) {
                if (DEBUG) {
                    Log.d(TAG, "Get SO (len=" + arrby.length + ")for " + string);
                }
                if (bQC) {
                    Log.d(TAG, "Now send the encapsulated buffer to TA " + string);
                    tAController.setupPinRandom(arrby);
                    tAController.unloadTA();
                    continue;
                }
                String string2 = tAInfo.getPinRandomFileName();
                Log.d(TAG, "For tbase, file name is " + string2);
                File file2 = new File(TUI_DATA_DIR, string2);
                try {
                    SpayTuiTAController.writeFile(arrby, file2);
                    continue;
                }
                catch (Exception exception) {
                    Log.e(TAG, "Failed to save SO for " + string);
                    exception.printStackTrace();
                    this.deletePinSo();
                    return 393217;
                }
            }
            Log.e(TAG, "Failed to get SO for " + string);
            this.deletePinSo();
            return 393217;
        }
        return 0;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int checkTuiSession() {
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            TACommandResponse tACommandResponse;
            block9 : {
                block8 : {
                    if (DEBUG) {
                        Log.d(TAG, "Calling checkTuiSession");
                    }
                    if (this.mPaymentHandle != null) break block8;
                    Log.e(TAG, "Error: Payment Handle is null");
                    return -1;
                }
                tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.CheckTUISession.Request());
                if (tACommandResponse != null) break block9;
                Log.e(TAG, "Error: load_and_execute failed");
                return -2;
            }
            SpayTuiTACommands.CheckTUISession.Response response = new SpayTuiTACommands.CheckTUISession.Response(tACommandResponse);
            if (DEBUG) {
                Log.d(TAG, "CheckTUISession return " + response.mRetVal.retCode.get());
            }
            long l2 = response.mRetVal.retCode.get();
            return (int)l2;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int clearTuiState() {
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            TACommandResponse tACommandResponse;
            block9 : {
                block8 : {
                    if (DEBUG) {
                        Log.d(TAG, "Calling clearTuiState");
                    }
                    if (this.mPaymentHandle != null) break block8;
                    Log.e(TAG, "Error: Payment Handle is null");
                    return -1;
                }
                tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.ClearState.Request());
                if (tACommandResponse != null) break block9;
                Log.e(TAG, "Error: load_    and_execute failed");
                return -2;
            }
            SpayTuiTACommands.ClearState.Response response = new SpayTuiTACommands.ClearState.Response(tACommandResponse);
            if (DEBUG) {
                Log.d(TAG, "clearTuiState return " + response.mRetVal.retCode.get());
            }
            long l2 = response.mRetVal.retCode.get();
            return (int)l2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean deletePin() {
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            Log.i(TAG, "Delete PIN data");
            for (TAController tAController : this.mSupportTAs) {
                if (tAController == null) {
                    Log.e(TAG, "Error: TAController is null. Not created yet");
                    continue;
                }
                if (!tAController.usesPinRandom()) {
                    Log.d(TAG, "This TA do not use Pin Random");
                    continue;
                }
                String string = tAController.getTAInfo().getPinRandomFileName();
                new File(TUI_DATA_DIR, string).delete();
            }
            File file = new File(TUI_DATA_DIR, TUIPINSECUREOBJECTFILE);
            if (!file.exists()) return true;
            if (file.isDirectory()) return true;
            boolean bl = file.delete();
            if (bl) {
                return true;
            }
            Log.e(TAG, "Unable to delete PIN SO!");
            return false;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    protected String findTAByDeviceModel(AssetManager assetManager) {
        String string;
        block5 : {
            int n2;
            String[] arrstring;
            String string2;
            block6 : {
                try {
                    String string3;
                    string = string3 = this.getScreenDensity();
                }
                catch (Exception exception) {
                    string = "";
                    Exception exception2 = exception;
                    exception2.printStackTrace();
                    break block5;
                }
                string2 = TAInfo.getTARootDir() + File.separator + string + File.separator + this.mTAInfo.getTAFileName();
                arrstring = assetManager.list(TAInfo.getTARootDir() + File.separator + string);
                if (arrstring == null) break block5;
                n2 = arrstring.length;
                break block6;
                {
                    catch (Exception exception) {}
                }
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                String string4 = arrstring[i2];
                if (string4 == null || !string4.equals((Object)this.mTAInfo.getTAFileName())) continue;
                Log.d(TAG, "Found TA file: " + string2);
                return string2;
            }
        }
        Log.d(TAG, "Load default TUI for res: " + string);
        return TAInfo.getTARootDir() + File.separator + this.mTAInfo.getTAFileName();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] generateRandom() {
        byte[] arrby = null;
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            if (DEBUG) {
                Log.d(TAG, "Calling generate Random");
            }
            if (this.mPaymentHandle != null) {
                TACommandResponse tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.GenerateRandom.Request());
                if (tACommandResponse == null) {
                    Log.e(TAG, "Error: load_and_execute failed");
                    return null;
                }
                SpayTuiTACommands.GenerateRandom.Response response = new SpayTuiTACommands.GenerateRandom.Response(tACommandResponse);
                if (response.mRetVal.retCode.get() != 0L) {
                    Log.e(TAG, "Error: get random return " + response.mRetVal.retCode.get());
                    return null;
                }
                Log.i(TAG, "generateRandom: Existing Pin length=" + response.mRetVal.existPinLen.get());
                byte[] arrby2 = response.mRetVal.pinSo.getData();
                if (!DEBUG) return arrby2;
                Log.d(TAG, "Get random/PIN so size: " + response.mRetVal.pinSo.size());
                Log.d(TAG, "Pin length: " + response.mRetVal.existPinLen.get() + "  It should be 0");
                return arrby2;
            }
            Log.e(TAG, "Error: Payment Handle is null");
            return arrby;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public AuthResult getAuthResult(byte[] arrby, String string, byte[] arrby2) {
        AuthResult authResult = null;
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            block12 : {
                SpayTuiTACommands.GetAuthResult.Request request;
                if (DEBUG) {
                    Log.d(TAG, "Calling get auth result");
                }
                this.mNonce = null;
                if (this.mPaymentHandle == null) break block12;
                if (arrby == null || string == null || arrby2 == null) {
                    Log.e(TAG, "invalid input to getAuthResult()");
                    return null;
                }
                try {
                    request = new SpayTuiTACommands.GetAuthResult.Request(arrby, string, arrby2);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    Log.e(TAG, illegalArgumentException.toString());
                    illegalArgumentException.printStackTrace();
                    return null;
                }
                TACommandResponse tACommandResponse = this.executeNoLoad(request);
                if (tACommandResponse == null) {
                    Log.e(TAG, "Error: load_and_execute failed");
                    return null;
                }
                SpayTuiTACommands.GetAuthResult.Response response = new SpayTuiTACommands.GetAuthResult.Response(tACommandResponse);
                if (DEBUG) {
                    Log.d(TAG, "get auth result return " + response.mRetVal.retCode.get());
                }
                String string2 = response.mRetVal.authType.get() == 0L ? "PIN" : (response.mRetVal.authType.get() == 1L ? "FP" : (response.mRetVal.authType.get() == 2L ? "BACKUP PASSWORD" : (response.mRetVal.authType.get() == 3L ? "IRIS" : "NONE")));
                long l2 = response.mRetVal.retCode.get() LCMP 0L;
                authResult = null;
                if (l2 != false) return authResult;
                if (response.mRetVal.update_pin_so.get() != 0) {
                    Log.i(TAG, "getAuthResult: Existing PIN len=" + response.mRetVal.existPinLen.get());
                    boolean bl = response.mRetVal.existPinLen.get() > 0L;
                    mPinExist = bl;
                    if (DEBUG) {
                        Log.d(TAG, "Need to update PIN SO! PIN exist: " + mPinExist + " pin len: " + response.mRetVal.existPinLen.get());
                    }
                    Log.i(TAG, "getAuthResult: savePinSo");
                    this.savePinSo(response.mRetVal.pin_so.getData());
                    return new AuthResult(response.mRetVal.so.getData(), string2);
                } else {
                    if (!DEBUG) return new AuthResult(response.mRetVal.so.getData(), string2);
                    Log.d(TAG, "No need to update PIN SO");
                }
                return new AuthResult(response.mRetVal.so.getData(), string2);
            }
            Log.e(TAG, "Error: Payment Handle is null");
            return authResult;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public AuthNonce getCachedNonce(int n2, boolean bl) {
        AuthNonce authNonce = null;
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            if (DEBUG) {
                Log.d(TAG, "Calling getCachedNonce");
            }
            if (this.mPaymentHandle != null) {
                if (this.mNonce != null && this.mNonce.length == n2) {
                    Log.d(TAG, "return existing nonce");
                    return new AuthNonce(this.mNonce, true);
                }
                authNonce = null;
                if (!bl) return authNonce;
                this.mNonce = this.getNonce(n2);
                return new AuthNonce(this.mNonce, false);
            }
            Log.e(TAG, "Error: Payment Handle is null");
            return authNonce;
        }
    }

    public String getLocale() {
        String string = SpayTuiTAController.mContext.getResources().getConfiguration().locale.getLanguage();
        Log.d(TAG, "locale.getLanguage: " + string);
        return string;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getNonce(int n2) {
        byte[] arrby = null;
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            if (DEBUG) {
                Log.d(TAG, "Calling get nonce");
            }
            this.mNonce = null;
            if (this.mPaymentHandle != null) {
                TACommandResponse tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.GetNonce.Request(n2));
                if (tACommandResponse == null) {
                    Log.e(TAG, "Error: load_and_execute failed");
                    return null;
                }
                SpayTuiTACommands.GetNonce.Response response = new SpayTuiTACommands.GetNonce.Response(tACommandResponse);
                if (DEBUG) {
                    Log.d(TAG, "get Nonce return " + response.mRetVal.retCode.get());
                }
                long l2 = response.mRetVal.retCode.get() LCMP 0L;
                arrby = null;
                if (l2 != false) return arrby;
                byte[] arrby2 = response.mRetVal.nonce.getData();
                return arrby2;
            }
            Log.e(TAG, "Error: Payment Handle is null");
            return arrby;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getPinRandom(String string) {
        byte[] arrby = null;
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            if (DEBUG) {
                Log.d(TAG, "Calling get PIN random");
            }
            if (this.mPaymentHandle != null) {
                TACommandResponse tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.GetPinRandom.Request(string));
                if (tACommandResponse == null) {
                    Log.e(TAG, "Error: load_and_execute failed");
                    return null;
                }
                SpayTuiTACommands.GetPinRandom.Response response = new SpayTuiTACommands.GetPinRandom.Response(tACommandResponse);
                byte[] arrby2 = response.mRetVal.result_so.getData();
                if (!DEBUG) return arrby2;
                Log.d(TAG, "getPinRandom return " + response.mRetVal.retCode.get() + "auth_type = " + response.mRetVal.authType.get());
                return arrby2;
            }
            Log.e(TAG, "Error: Payment Handle is null");
            return arrby;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getPinSO() {
        byte[] arrby = null;
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            if (DEBUG) {
                Log.d(TAG, "Calling get Pin SO");
            }
            if (this.mPaymentHandle != null) {
                TACommandResponse tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.GetPinSO.Request());
                if (tACommandResponse == null) {
                    Log.e(TAG, "Error: load_and_execute failed");
                    return null;
                }
                SpayTuiTACommands.GetPinSO.Response response = new SpayTuiTACommands.GetPinSO.Response(tACommandResponse);
                if (response.mRetVal.retCode.get() != 0L) {
                    Log.e(TAG, "Error: get PIN SO return " + response.mRetVal.retCode.get());
                    return null;
                }
                if (DEBUG) {
                    Log.d(TAG, "Get PIN SO size: " + response.mRetVal.pinSo.size());
                }
                boolean bl = response.mRetVal.existPinLen.get() > 0L;
                mPinExist = bl;
                Log.i(TAG, "getPinSO: Existing Pin len=" + response.mRetVal.existPinLen.get());
                byte[] arrby2 = response.mRetVal.pinSo.getData();
                if (!DEBUG) return arrby2;
                Log.d(TAG, "Pin len: " + response.mRetVal.existPinLen.get() + "  pin exist: " + mPinExist);
                return arrby2;
            }
            Log.e(TAG, "Error: Payment Handle is null");
            return arrby;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public String getScreenDensity() {
        String string = "";
        Configuration configuration = mContext.getResources().getConfiguration();
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int n2 = (int)((float)configuration.smallestScreenWidthDp * displayMetrics.density);
        Log.d(TAG, "disp.density: " + displayMetrics.density + ", conf.smallestScreenWidthDp: " + configuration.smallestScreenWidthDp);
        Log.d(TAG, "swidth: " + n2);
        String string2 = n2 < 800 ? "-xhdpi" : (n2 < 1100 ? "-xxhdpi" : "-xxxhdpi");
        if (!bQC) {
            Locale locale = Locale.US;
            Object[] arrobject = new Object[]{360, string2};
            return String.format((Locale)locale, (String)"%d%s", (Object[])arrobject);
        }
        String string3 = Build.MODEL;
        if (string3.indexOf("A") > 0) {
            string = string3.substring(string3.indexOf("A"));
        }
        if ("A7100".equals((Object)string)) return string;
        if ("A5100".equals((Object)string)) return string;
        Locale locale = Locale.US;
        Object[] arrobject = new Object[]{360, string2};
        return String.format((Locale)locale, (String)"%d%s", (Object[])arrobject);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getSecureResult(byte[] arrby, String string) {
        byte[] arrby2 = null;
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            block8 : {
                SpayTuiTACommands.GetSecureResult.Request request;
                if (DEBUG) {
                    Log.d(TAG, "Calling get secure result");
                }
                if (this.mPaymentHandle == null) break block8;
                try {
                    request = new SpayTuiTACommands.GetSecureResult.Request(arrby, string);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    Log.e(TAG, illegalArgumentException.toString());
                    illegalArgumentException.printStackTrace();
                    return null;
                }
                TACommandResponse tACommandResponse = this.executeNoLoad(request);
                if (tACommandResponse == null) {
                    Log.e(TAG, "Error: load_and_execute failed");
                    return null;
                }
                SpayTuiTACommands.GetSecureResult.Response response = new SpayTuiTACommands.GetSecureResult.Response(tACommandResponse);
                if (DEBUG) {
                    Log.d(TAG, "getSecureResult return " + response.mRetVal.retCode.get());
                }
                long l2 = 0L LCMP response.mRetVal.retCode.get();
                arrby2 = null;
                if (l2 != false) return arrby2;
                byte[] arrby3 = response.mRetVal.result_so.getData();
                return arrby3;
            }
            Log.e(TAG, "Error: Payment Handle is null");
            return arrby2;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int inAppConfirm(String string, String string2, String string3, String string4, String string5, String string6, byte[] arrby) {
        int n2;
        if (string == null || string2 == null || string3 == null || string4 == null || string5 == null || string6 == null || arrby == null || arrby.length == 0) {
            Log.e(TAG, "inAppConfirm input is null");
            return -2;
        }
        TextPaint textPaint = new TextPaint(){
            {
                this.setARGB(255, 159, 159, 159);
                this.setTextAlign(Paint.Align.LEFT);
                this.setTextSize(20.0f * SpayTuiTAController.this.getDensity());
                this.setAntiAlias(true);
                this.setTypeface(Typeface.create((String)"Roboto", (int)0));
            }
        };
        TextPaint textPaint2 = new TextPaint(){
            {
                this.setARGB(255, 0, 0, 0);
                this.setTextAlign(Paint.Align.LEFT);
                this.setTextSize(16.0f * SpayTuiTAController.this.getDensity());
                this.setAntiAlias(true);
                this.setTypeface(Typeface.create((String)"Roboto", (int)0));
            }
        };
        Rect rect = new Rect();
        textPaint2.getTextBounds(string2, 0, string2.length(), rect);
        Log.d(TAG, "store length=" + (128.0f * this.getDensity() + (float)rect.width()));
        if (128.0f * this.getDensity() + (float)rect.width() > 360.0f * this.getDensity()) {
            String string7 = string2.substring(0, string2.length() / 2);
            string2 = string7 + "...";
        }
        TextPaint textPaint3 = new TextPaint(){
            {
                this.setARGB(255, 57, 87, 172);
                this.setTextAlign(Paint.Align.LEFT);
                this.setTextSize(14.0f * SpayTuiTAController.this.getDensity());
                this.setAntiAlias(true);
                this.setTypeface(Typeface.create((String)"Roboto", (int)0));
            }
        };
        TextPaint textPaint4 = new TextPaint(){
            {
                this.setARGB(255, 0, 0, 0);
                this.setTextAlign(Paint.Align.LEFT);
                this.setTextSize(14.0f * SpayTuiTAController.this.getDensity());
                this.setAntiAlias(true);
                this.setTypeface(Typeface.create((String)"Roboto", (int)0));
            }
        };
        TextPaint textPaint5 = new TextPaint(){
            {
                this.setARGB(255, 57, 87, 172);
                this.setTextAlign(Paint.Align.LEFT);
                this.setTextSize(17.0f * SpayTuiTAController.this.getDensity());
                this.setAntiAlias(true);
                this.setTypeface(Typeface.create((String)"Roboto", (int)0));
            }
        };
        TextPaint textPaint6 = new TextPaint(){
            {
                this.setARGB(255, 250, 250, 250);
                this.setTextAlign(Paint.Align.LEFT);
                this.setTextSize(17.0f * SpayTuiTAController.this.getDensity());
                this.setAntiAlias(true);
                this.setTypeface(Typeface.create((String)"Roboto", (int)0));
            }
        };
        byte[] arrby2 = this.text2png(string, textPaint, Color.argb((int)0, (int)246, (int)246, (int)246), true);
        byte[] arrby3 = this.text2png(string2, textPaint2, Color.argb((int)255, (int)250, (int)250, (int)250), false);
        byte[] arrby4 = this.text2png(string3, textPaint3, Color.argb((int)255, (int)250, (int)250, (int)250), false);
        byte[] arrby5 = this.text2png(string4, textPaint4, Color.argb((int)255, (int)250, (int)250, (int)250), false);
        byte[] arrby6 = this.text2png(string5, textPaint5, Color.argb((int)0, (int)250, (int)250, (int)250), false);
        byte[] arrby7 = this.text2png(string6, textPaint6, Color.argb((int)0, (int)250, (int)250, (int)250), false);
        if (arrby2 == null || arrby3 == null || arrby4 == null || arrby5 == null || arrby6 == null || arrby7 == null) {
            Log.e(TAG, "inAppConfirm text2png is null");
            return -2;
        }
        Log.d(TAG, "Lengthes: " + arrby2.length + " " + arrby3.length + " " + arrby4.length + " " + arrby5.length + " " + arrby6.length + " " + arrby7.length + " " + arrby.length);
        if (14 + (arrby2.length + arrby3.length + arrby4.length + arrby5.length + arrby6.length + arrby7.length + arrby.length) > 256000) {
            Log.e(TAG, "Out of buffer");
            return -2;
        }
        TACommandResponse tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.InAppConfirm.Request(arrby2, arrby3, arrby4, arrby5, arrby6, arrby7, arrby));
        if (tACommandResponse == null) {
            Log.e(TAG, "Error: load_and_execute failed");
            return -2;
        }
        SpayTuiTACommands.InAppConfirm.Response response = new SpayTuiTACommands.InAppConfirm.Response(tACommandResponse);
        if (DEBUG) {
            Log.d(TAG, "InAppConfirm return " + response.mRetVal.retCode.get());
        }
        if ((n2 = (int)response.mRetVal.retCode.get()) != 0) return n2;
        if (!bQC) return n2;
        return this.start_secure_touch();
    }

    public boolean isResetPeding() {
        return mResetPending;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int loadPin(byte[] arrby) {
        boolean bl = true;
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            SpayTuiTACommands.LoadPin.Request request;
            this.actionBarTextWidth = 0;
            this.actionBarTextHeight = 0;
            if (DEBUG) {
                Log.d(TAG, "Calling loadPin()");
            }
            if (this.mPaymentHandle == null) {
                Log.e(TAG, "Error: Payment Handle is null");
                return -1;
            }
            try {
                request = new SpayTuiTACommands.LoadPin.Request(arrby);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                Log.e(TAG, illegalArgumentException.toString());
                illegalArgumentException.printStackTrace();
                return -3;
            }
            TACommandResponse tACommandResponse = this.executeNoLoad(request);
            if (tACommandResponse == null) {
                Log.e(TAG, "Error: load_and_execute failed");
                return -2;
            }
            SpayTuiTACommands.LoadPin.Response response = new SpayTuiTACommands.LoadPin.Response(tACommandResponse);
            int n2 = (int)response.mRetVal.retCode.get();
            if (n2 != 0) {
                if (n2 != 413696) return n2;
            }
            if (response.mRetVal.existPinLen.get() <= 0L) {
                bl = false;
            }
            mPinExist = bl;
            Log.i(TAG, "loadPin: Existing PIN len=" + response.mRetVal.existPinLen.get());
            if (n2 == 413696) {
                Log.i(TAG, "loadPin: Saving upgraded pin so file");
                this.savePinSo(response.mRetVal.updated_pin_so.getData());
                this.writePinRandomFiles(true);
            }
            if (!DEBUG) return 0;
            Log.d(TAG, "Existing PIN len: " + response.mRetVal.existPinLen.get() + "  pin exist: " + mPinExist);
            return 0;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean loadTA() {
        boolean bl = true;
        boolean bl2 = false;
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            mPinExist = false;
            if (!mResetPending) {
                int n2;
                boolean bl3 = super.loadTA();
                bl2 = false;
                if (!bl3) return bl2;
                byte[] arrby = SpayTuiTAController.readFile(new File(TUI_DATA_DIR, TUIPINSECUREOBJECTFILE));
                if (arrby != null && arrby.length > 0) {
                    int n3;
                    if (DEBUG) {
                        Log.d(TAG, "pin so: " + arrby.toString());
                    }
                    if ((n3 = this.loadPin(Arrays.copyOfRange((byte[])arrby, (int)1, (int)arrby.length))) == 0) {
                        if (DEBUG) {
                            Log.d(TAG, "Existing Pin SO loaded, PIN exist: " + mPinExist + "  pin[0] = " + arrby[0]);
                        }
                        this.writePinRandomFiles(false);
                        return bl;
                    }
                    Log.e(TAG, "Load PIN failed with ret = " + n3 + ": possibly invalid PIN secure object!");
                    return false;
                }
                if (DEBUG) {
                    Log.d(TAG, "No PIN file found, generate the file only containing random");
                }
                if ((n2 = this.savePinSecureObjects(true)) == 0) return bl;
                return false;
            }
            Log.e(TAG, "PF resetting... ");
            return bl2;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public int merchantSecureDisplay(byte[] arrby, int n2, int n3, int[] arrn) {
        if (arrby == null || arrby.length == 0 || arrn == null || arrn.length == 0) {
            Log.e(TAG, "merchantSecureDisplay input is null");
            return -2;
        }
        Log.d(TAG, "merchantSecureDisplay: totalSize=" + n2 + " index=" + n3 + " screenPNG.length=" + arrby.length + " coordinates.length=" + arrn.length);
        if (n3 == 0) {
            mScreenPNGBuf = new byte[n2];
        }
        System.arraycopy((Object)arrby, (int)0, (Object)mScreenPNGBuf, (int)n3, (int)arrby.length);
        if (n3 + arrby.length < n2) {
            Log.d(TAG, "merchantSecureDisplay: transfer not finished!");
            return 0;
        }
        Log.d(TAG, "merchantSecureDisplay: transfer finished!");
        Log.d(TAG, "merchantSecureDisplay before computation");
        byte[] arrby2 = new byte[5 + 3 * arrn.length];
        arrby2[0] = (byte)(255 & mScreenPNGBuf.length >> 16);
        arrby2[1] = (byte)(255 & mScreenPNGBuf.length >> 8);
        arrby2[2] = (byte)(255 & mScreenPNGBuf.length);
        arrby2[3] = (byte)(255 & arrn.length >> 8);
        arrby2[4] = (byte)(255 & arrn.length);
        int n4 = 5;
        for (int i2 = 0; i2 < arrn.length; i2 += 4) {
            for (int i3 = 0; i3 < 4; ++i3) {
                int n5 = n4 + 1;
                arrby2[n4] = (byte)(255 & arrn[i2 + i3] >> 16);
                int n6 = n5 + 1;
                arrby2[n5] = (byte)(255 & arrn[i2 + i3] >> 8);
                int n7 = n6 + 1;
                arrby2[n6] = (byte)(255 & arrn[i2 + i3]);
                n4 = n7;
            }
        }
        Log.d(TAG, "merchantSecureDisplay after computation");
        TACommandResponse tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.MerchantSecureDisplay.Request(arrby2));
        if (tACommandResponse == null) {
            Log.e(TAG, "Error: load_and_execute failed");
            return -2;
        }
        int n8 = (int)new SpayTuiTACommands.MerchantSecureDisplay.Response((TACommandResponse)tACommandResponse).mRetVal.retCode.get();
        if (n8 != 0 && n8 != 3) {
            return n8;
        }
        Log.d(TAG, "Initial transfer size=" + mScreenPNGBuf.length);
        int n9 = mScreenPNGBuf.length;
        int n10 = 0;
        int n11 = n8;
        while (n9 > 0) {
            int n12;
            if (n9 + 3 > 256000) {
                n12 = 255997;
                n9 -= n12;
            } else {
                n12 = n9;
                n9 = 0;
            }
            byte[] arrby3 = new byte[n12];
            Log.d(TAG, "size=" + n12 + " left=" + n9 + " tmpBuf=" + arrby3.length);
            System.arraycopy((Object)mScreenPNGBuf, (int)n10, (Object)arrby3, (int)0, (int)n12);
            int n13 = n12 + n10;
            TACommandResponse tACommandResponse2 = this.executeNoLoad(new SpayTuiTACommands.MerchantSecureDisplay.Request(arrby3));
            if (tACommandResponse2 == null) {
                Log.e(TAG, "Error: load_and_execute failed");
                return n11;
            }
            n11 = (int)new SpayTuiTACommands.MerchantSecureDisplay.Response((TACommandResponse)tACommandResponse2).mRetVal.retCode.get();
            Log.d(TAG, "Response.mRetVal.retCode.get()=" + n11);
            n10 = n13;
        }
        return n11;
    }

    public int merchantSecureInit(int n2, int[] arrn) {
        if (arrn == null || arrn.length == 0) {
            Log.e(TAG, "merchantSecureInit input is null");
            return -2;
        }
        Log.d(TAG, "merchantSecureInit before computation totalSize=" + n2 + " coordinates.len=" + arrn.length);
        byte[] arrby = new byte[5 + 3 * arrn.length];
        arrby[0] = (byte)(255 & n2 >> 16);
        arrby[1] = (byte)(255 & n2 >> 8);
        arrby[2] = (byte)(n2 & 255);
        arrby[3] = (byte)(255 & arrn.length >> 8);
        arrby[4] = (byte)(255 & arrn.length);
        int n3 = 5;
        for (int i2 = 0; i2 < arrn.length; i2 += 4) {
            for (int i3 = 0; i3 < 4; ++i3) {
                int n4 = n3 + 1;
                arrby[n3] = (byte)(255 & arrn[i2 + i3] >> 16);
                int n5 = n4 + 1;
                arrby[n4] = (byte)(255 & arrn[i2 + i3] >> 8);
                int n6 = n5 + 1;
                arrby[n5] = (byte)(255 & arrn[i2 + i3]);
                n3 = n6;
            }
        }
        Log.d(TAG, "merchantSecureInit after computation");
        TACommandResponse tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.MerchantSecureInit.Request(arrby));
        if (tACommandResponse == null) {
            Log.e(TAG, "Error: load_and_execute failed");
            return -2;
        }
        return (int)new SpayTuiTACommands.MerchantSecureInit.Response((TACommandResponse)tACommandResponse).mRetVal.retCode.get();
    }

    /*
     * Enabled aggressive block sorting
     */
    public int[] merchantSecureTouch() {
        int[] arrn = new int[]{-2, 0};
        Log.d(TAG, "Before start_secure_touch()");
        if (bQC) {
            arrn[0] = this.start_secure_touch();
        } else {
            TACommandResponse tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.MerchantSecureTouch.Request());
            if (tACommandResponse == null) {
                Log.e(TAG, "Error: load_and_execute failed");
                return arrn;
            }
            arrn[0] = (int)new SpayTuiTACommands.MerchantSecureTouch.Response((TACommandResponse)tACommandResponse).mRetVal.retCode.get();
        }
        if ((1044480 & arrn[0]) == 417792) {
            arrn[1] = 4095 & arrn[0];
            arrn[0] = 0;
        }
        Log.d(TAG, "After start_secure_touch(): ret[0]=" + arrn[0] + ", ret[1]=" + arrn[1]);
        return arrn;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int merchantSendSecureImg(byte[] arrby, int n2, int n3, int n4, int n5) {
        if (arrby == null || arrby.length == 0) {
            Log.e(TAG, "merchantSendSecureImg input is null");
            return -2;
        }
        Log.d(TAG, "merchantSendSecureImg: totalSize=" + n2 + " index=" + n3 + " screenPNG.length=" + arrby.length + " x=" + n4 + " y=" + n5);
        if (n3 == 0) {
            mScreenPNGBuf = new byte[n2];
        }
        System.arraycopy((Object)arrby, (int)0, (Object)mScreenPNGBuf, (int)n3, (int)arrby.length);
        if (n3 + arrby.length < n2) {
            Log.d(TAG, "merchantSendSecureImg: transfer not finished!");
            return 0;
        }
        Log.d(TAG, "merchantSendSecureImg: transfer finished!");
        Log.d(TAG, "merchantSendSecureImg before computation");
        byte[] arrby2 = new byte[]{(byte)(255 & mScreenPNGBuf.length >> 16), (byte)(255 & mScreenPNGBuf.length >> 8), (byte)(255 & mScreenPNGBuf.length), (byte)(255 & n4 >> 16), (byte)(255 & n4 >> 8), (byte)(n4 & 255), (byte)(255 & n5 >> 16), (byte)(255 & n5 >> 8), (byte)(n5 & 255)};
        Log.d(TAG, "merchantSendSecureImg after computation");
        TACommandResponse tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.MerchantSendSecureImg.Request(arrby2));
        if (tACommandResponse == null) {
            Log.e(TAG, "Error: load_and_execute failed");
            return -2;
        }
        int n6 = (int)new SpayTuiTACommands.MerchantSendSecureImg.Response((TACommandResponse)tACommandResponse).mRetVal.retCode.get();
        if (n6 != 0 && n6 != 3) {
            return n6;
        }
        Log.d(TAG, "Initial transfer size=" + mScreenPNGBuf.length);
        int n7 = mScreenPNGBuf.length;
        int n8 = 0;
        int n9 = n6;
        while (n7 > 0) {
            int n10;
            if (n7 + 3 > 256000) {
                n10 = 255997;
                n7 -= n10;
            } else {
                n10 = n7;
                n7 = 0;
            }
            byte[] arrby3 = new byte[n10];
            Log.d(TAG, "size=" + n10 + " left=" + n7 + " tmpBuf=" + arrby3.length);
            System.arraycopy((Object)mScreenPNGBuf, (int)n8, (Object)arrby3, (int)0, (int)n10);
            int n11 = n10 + n8;
            TACommandResponse tACommandResponse2 = this.executeNoLoad(new SpayTuiTACommands.MerchantSendSecureImg.Request(arrby3));
            if (tACommandResponse2 == null) {
                Log.e(TAG, "Error: load_and_execute failed");
                return n9;
            }
            n9 = (int)new SpayTuiTACommands.MerchantSendSecureImg.Response((TACommandResponse)tACommandResponse2).mRetVal.retCode.get();
            Log.d(TAG, " =" + n9);
            n8 = n11;
        }
        return n9;
    }

    public boolean pinExist() {
        return mPinExist;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int preloadFpSecureResult(byte[] arrby) {
        int n2 = -2;
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            block9 : {
                TACommandResponse tACommandResponse;
                block10 : {
                    SpayTuiTACommands.PreloadFpSecureResult.Request request;
                    if (DEBUG) {
                        Log.d(TAG, "Calling preloadFpSecureResult");
                    }
                    if (this.mPaymentHandle == null) {
                        Log.e(TAG, "Error: Payment Handle is null");
                        return -1;
                    }
                    try {
                        request = new SpayTuiTACommands.PreloadFpSecureResult.Request(arrby);
                    }
                    catch (IllegalArgumentException illegalArgumentException) {
                        Log.e(TAG, illegalArgumentException.toString());
                        illegalArgumentException.printStackTrace();
                        break block9;
                    }
                    tACommandResponse = this.executeNoLoad(request);
                    if (tACommandResponse != null) break block10;
                    Log.e(TAG, "Error: load_and_execute failed");
                    break block9;
                }
                SpayTuiTACommands.PreloadFpSecureResult.Response response = new SpayTuiTACommands.PreloadFpSecureResult.Response(tACommandResponse);
                if (DEBUG) {
                    Log.d(TAG, "PreloadFpSecureResult return " + response.mRetVal.retCode.get());
                }
                if (0L != response.mRetVal.retCode.get()) return n2;
                long l2 = response.mRetVal.retCode.get();
                return (int)l2;
            }
            return n2;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int resume() {
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            int n2;
            block12 : {
                TACommandResponse tACommandResponse;
                block11 : {
                    block10 : {
                        if (DEBUG) {
                            Log.d(TAG, "Calling resume");
                        }
                        if (this.mPaymentHandle != null) break block10;
                        Log.e(TAG, "Error: Payment Handle is null");
                        return -1;
                    }
                    tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.Resume.Request(false));
                    if (tACommandResponse != null) break block11;
                    Log.e(TAG, "Error: load_and_execute failed");
                    return -2;
                }
                SpayTuiTACommands.Resume.Response response = new SpayTuiTACommands.Resume.Response(tACommandResponse);
                if (DEBUG) {
                    Log.d(TAG, "Resume return " + response.mRetVal.retCode.get());
                }
                if ((n2 = (int)response.mRetVal.retCode.get()) != 0) break block12;
                if (!bQC) break block12;
                n2 = this.start_secure_touch();
            }
            if (393221 != n2) return n2;
            int n3 = this.savePinSecureObjects(false);
            return n3;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int resume(boolean bl) {
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            int n2;
            block13 : {
                TACommandResponse tACommandResponse;
                block12 : {
                    block11 : {
                        if (DEBUG) {
                            Log.d(TAG, "Calling resume, update_display_only = " + bl);
                        }
                        if (this.mPaymentHandle != null) break block11;
                        Log.e(TAG, "Error: Payment Handle is null");
                        return -1;
                    }
                    tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.Resume.Request(bl));
                    if (tACommandResponse != null) break block12;
                    Log.e(TAG, "Error: load_and_execute failed");
                    return -2;
                }
                SpayTuiTACommands.Resume.Response response = new SpayTuiTACommands.Resume.Response(tACommandResponse);
                if (DEBUG) {
                    Log.d(TAG, "Resume return " + response.mRetVal.retCode.get());
                }
                if ((n2 = (int)response.mRetVal.retCode.get()) != 0) break block13;
                if (!bQC || bl) break block13;
                n2 = this.start_secure_touch();
            }
            if (393221 != n2) return n2;
            int n3 = this.savePinSecureObjects(false);
            return n3;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int setActionBarText(String[] arrstring) {
        int n2 = -3;
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            block12 : {
                float f2;
                SpayTuiTACommands.SetActionBarText.Request request;
                if (DEBUG) {
                    Log.d(TAG, "Set action bar text");
                }
                if (this.mPaymentHandle == null) {
                    Log.e(TAG, "Error: Payment Handle is null");
                    return -1;
                }
                DisplayMetrics displayMetrics = new DisplayMetrics();
                try {
                    float f3;
                    Configuration configuration = mContext.getResources().getConfiguration();
                    DisplayMetrics displayMetrics2 = mContext.getResources().getDisplayMetrics();
                    float f4 = (float)configuration.smallestScreenWidthDp * displayMetrics2.density;
                    displayMetrics.setTo(displayMetrics2);
                    displayMetrics.density = f4 / 360.0f;
                    displayMetrics.densityDpi = (int)((float)displayMetrics2.densityDpi / displayMetrics2.density * displayMetrics.density);
                    displayMetrics.scaledDensity = displayMetrics.density;
                    f2 = f3 = TypedValue.applyDimension((int)1, (float)19.0f, (DisplayMetrics)displayMetrics);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    f2 = TypedValue.applyDimension((int)1, (float)19.0f, (DisplayMetrics)mContext.getResources().getDisplayMetrics());
                }
                TextPaint textPaint = SpayTuiTAController.isAndroidN() ? new TextPaint(){
                    {
                        this.setARGB(204, 37, 37, 37);
                        this.setTextAlign(Paint.Align.LEFT);
                        this.setTextSize(f2);
                        this.setAntiAlias(true);
                        this.setTypeface(Typeface.create((String)"RobotoCondensed-Bold", (int)1));
                    }
                } : new TextPaint(){
                    {
                        this.setARGB(255, 159, 159, 159);
                        this.setTextAlign(Paint.Align.LEFT);
                        this.setTextSize(f2);
                        this.setAntiAlias(true);
                        this.setTypeface(Typeface.create((String)"Roboto", (int)0));
                    }
                };
                byte[] arrby = this.text2png(arrstring, textPaint, Color.argb((int)255, (int)250, (int)250, (int)250), true);
                if (arrby == null) return n2;
                try {
                    request = new SpayTuiTACommands.SetActionBarText.Request(arrby);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    Log.e(TAG, illegalArgumentException.toString());
                    illegalArgumentException.printStackTrace();
                    break block12;
                }
                TACommandResponse tACommandResponse = this.executeNoLoad(request);
                if (tACommandResponse == null) {
                    Log.e(TAG, "Error: load_and_execute failed");
                    return -2;
                }
                SpayTuiTACommands.SetActionBarText.Response response = new SpayTuiTACommands.SetActionBarText.Response(tACommandResponse);
                if (DEBUG) {
                    Log.d(TAG, "Set Action Bar Text return " + response.mRetVal.retCode.get());
                }
                long l2 = response.mRetVal.retCode.get();
                return (int)l2;
            }
            return n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int setBackgroundImg(byte[] arrby, int n2, int n3) {
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            SpayTuiTACommands.SetBackgroundImg.Request request;
            if (DEBUG) {
                Log.d(TAG, "Calling setup background image");
            }
            if (this.mPaymentHandle == null) {
                Log.e(TAG, "Error: Payment Handle is null");
                return -1;
            }
            try {
                request = new SpayTuiTACommands.SetBackgroundImg.Request(arrby, n2, n3);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                Log.e(TAG, illegalArgumentException.toString());
                illegalArgumentException.printStackTrace();
                return -3;
            }
            TACommandResponse tACommandResponse = this.executeNoLoad(request);
            if (tACommandResponse == null) {
                Log.e(TAG, "Error: load_and_execute failed");
                return -2;
            }
            SpayTuiTACommands.SetBackgroundImg.Response response = new SpayTuiTACommands.SetBackgroundImg.Response(tACommandResponse);
            if (DEBUG) {
                Log.d(TAG, "Set background image return " + response.mRetVal.retCode.get());
            }
            long l2 = response.mRetVal.retCode.get();
            return (int)l2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int setCancelBtnImg(byte[] arrby, byte[] arrby2, int n2, int n3) {
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            SpayTuiTACommands.SetCancelBtn.Request request;
            if (DEBUG) {
                Log.d(TAG, "Calling set cancel");
            }
            if (this.mPaymentHandle == null) {
                Log.e(TAG, "Error: Payment Handle is null");
                return -1;
            }
            try {
                request = new SpayTuiTACommands.SetCancelBtn.Request(arrby, arrby2, n2, n3);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                Log.e(TAG, illegalArgumentException.toString());
                illegalArgumentException.printStackTrace();
                return -3;
            }
            TACommandResponse tACommandResponse = this.executeNoLoad(request);
            if (tACommandResponse == null) {
                Log.e(TAG, "Error: load_and_execute failed");
                return -2;
            }
            SpayTuiTACommands.SetCancelBtn.Response response = new SpayTuiTACommands.SetCancelBtn.Response(tACommandResponse);
            if (DEBUG) {
                Log.d(TAG, "Set cancel return " + response.mRetVal.retCode.get());
            }
            long l2 = response.mRetVal.retCode.get();
            return (int)l2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int setPinBox(byte[] arrby, byte[] arrby2, int n2, int n3, int n4, int n5, int n6) {
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            SpayTuiTACommands.SetPinBox.Request request;
            if (DEBUG) {
                Log.d(TAG, "Calling Set Pinbox");
            }
            if (this.mPaymentHandle == null) {
                Log.e(TAG, "Error: Payment Handle is null");
                return -1;
            }
            try {
                request = new SpayTuiTACommands.SetPinBox.Request(arrby, arrby2, n2, n3, n4, n5, n6);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                Log.e(TAG, illegalArgumentException.toString());
                illegalArgumentException.printStackTrace();
                return -3;
            }
            TACommandResponse tACommandResponse = this.executeNoLoad(request);
            if (tACommandResponse == null) {
                Log.e(TAG, "Error: load_and_execute failed");
                return -2;
            }
            SpayTuiTACommands.SetPinBox.Response response = new SpayTuiTACommands.SetPinBox.Response(tACommandResponse);
            if (DEBUG) {
                Log.d(TAG, "Set Pin Box return " + response.mRetVal.retCode.get());
            }
            long l2 = response.mRetVal.retCode.get();
            return (int)l2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int setPrompt(byte[] arrby, int n2, int n3) {
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            SpayTuiTACommands.SetPrompt.Request request;
            if (DEBUG) {
                Log.d(TAG, "Calling Set prompt with img");
            }
            if (this.mPaymentHandle == null) {
                Log.e(TAG, "Error: Payment Handle is null");
                return -1;
            }
            try {
                request = new SpayTuiTACommands.SetPrompt.Request(arrby, n2, n3);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                Log.e(TAG, illegalArgumentException.toString());
                illegalArgumentException.printStackTrace();
                return -3;
            }
            TACommandResponse tACommandResponse = this.executeNoLoad(request);
            if (tACommandResponse == null) {
                Log.e(TAG, "Error: load_and_execute failed");
                return -2;
            }
            SpayTuiTACommands.SetPrompt.Response response = new SpayTuiTACommands.SetPrompt.Response(tACommandResponse);
            if (DEBUG) {
                Log.d(TAG, "SetPrompt return " + response.mRetVal.retCode.get());
            }
            long l2 = response.mRetVal.retCode.get();
            return (int)l2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int setPrompt(String[] arrstring, TextPaint textPaint, int n2, int n3) {
        int n4 = -3;
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            block10 : {
                SpayTuiTACommands.SetPrompt.Request request;
                if (DEBUG) {
                    Log.d(TAG, "Calling Set prompt with string");
                }
                if (this.mPaymentHandle == null) {
                    Log.e(TAG, "Error: Payment Handle is null");
                    return -1;
                }
                byte[] arrby = this.text2png(arrstring, textPaint, 0, false);
                if (arrby == null) return n4;
                try {
                    request = new SpayTuiTACommands.SetPrompt.Request(arrby, n2, n3);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    Log.e(TAG, illegalArgumentException.toString());
                    illegalArgumentException.printStackTrace();
                    break block10;
                }
                TACommandResponse tACommandResponse = this.executeNoLoad(request);
                if (tACommandResponse == null) {
                    Log.e(TAG, "Error: load_and_execute failed");
                    return -2;
                }
                SpayTuiTACommands.SetPrompt.Response response = new SpayTuiTACommands.SetPrompt.Response(tACommandResponse);
                if (DEBUG) {
                    Log.d(TAG, "SetPrompt return " + response.mRetVal.retCode.get());
                }
                long l2 = response.mRetVal.retCode.get();
                return (int)l2;
            }
            return n4;
        }
    }

    public void setResetStatus(boolean bl) {
        Log.d(TAG, "setResetStatus:  " + bl);
        mResetPending = bl;
    }

    public int setRtl(boolean bl) {
        Log.d(TAG, "setRtl=" + bl);
        TACommandResponse tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.SetRtl.Request(bl));
        if (tACommandResponse == null) {
            Log.e(TAG, "Error: load_and_execute failed");
            return -2;
        }
        int n2 = (int)new SpayTuiTACommands.SetRtl.Response((TACommandResponse)tACommandResponse).mRetVal.retCode.get();
        Log.d(TAG, "ret=" + n2);
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int setSecureModeText(String[] arrstring) {
        int n2 = -3;
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            block12 : {
                byte[] arrby;
                float f2;
                SpayTuiTACommands.SetSecureModeText.Request request;
                if (DEBUG) {
                    Log.d(TAG, "Set secure mode text");
                }
                if (this.mPaymentHandle == null) {
                    Log.e(TAG, "Error: Payment Handle is null");
                    return -1;
                }
                DisplayMetrics displayMetrics = new DisplayMetrics();
                try {
                    float f3;
                    Configuration configuration = mContext.getResources().getConfiguration();
                    DisplayMetrics displayMetrics2 = mContext.getResources().getDisplayMetrics();
                    float f4 = (float)configuration.smallestScreenWidthDp * displayMetrics2.density;
                    displayMetrics.setTo(displayMetrics2);
                    displayMetrics.density = f4 / 360.0f;
                    displayMetrics.densityDpi = (int)((float)displayMetrics2.densityDpi / displayMetrics2.density * displayMetrics.density);
                    displayMetrics.scaledDensity = displayMetrics.density;
                    f2 = f3 = TypedValue.applyDimension((int)1, (float)16.0f, (DisplayMetrics)displayMetrics);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    f2 = TypedValue.applyDimension((int)1, (float)16.0f, (DisplayMetrics)mContext.getResources().getDisplayMetrics());
                }
                if ((arrby = this.text2png(arrstring, new TextPaint(){
                    {
                        this.setARGB(153, 56, 56, 56);
                        this.setTextAlign(Paint.Align.LEFT);
                        this.setTextSize(f2);
                        this.setAntiAlias(true);
                        this.setTypeface(Typeface.create((String)"Roboto-Medium", (int)0));
                    }
                }, 0, false)) == null) return n2;
                try {
                    request = new SpayTuiTACommands.SetSecureModeText.Request(arrby);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    Log.e(TAG, illegalArgumentException.toString());
                    illegalArgumentException.printStackTrace();
                    break block12;
                }
                TACommandResponse tACommandResponse = this.executeNoLoad(request);
                if (tACommandResponse == null) {
                    Log.e(TAG, "Error: load_and_execute failed");
                    return -2;
                }
                SpayTuiTACommands.SetSecureModeText.Response response = new SpayTuiTACommands.SetSecureModeText.Response(tACommandResponse);
                if (DEBUG) {
                    Log.d(TAG, "Set Secure Mode Text return " + response.mRetVal.retCode.get());
                }
                long l2 = response.mRetVal.retCode.get();
                return (int)l2;
            }
            return n2;
        }
    }

    public void setTAs(List<TAController> list) {
        this.mSupportTAs = list;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int setupBio(byte[] arrby, byte[] arrby2) {
        int n2 = -1;
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            block9 : {
                SpayTuiTACommands.SetupBio.Request request;
                block10 : {
                    if (DEBUG) {
                        Log.d(TAG, "Calling setup BIO");
                    }
                    if (this.mPaymentHandle != null) break block10;
                    Log.e(TAG, "Error: Payment Handle is null");
                    break block9;
                }
                try {
                    request = new SpayTuiTACommands.SetupBio.Request(arrby, arrby2);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    Log.e(TAG, illegalArgumentException.toString());
                    illegalArgumentException.printStackTrace();
                    break block9;
                }
                TACommandResponse tACommandResponse = this.executeNoLoad(request);
                if (tACommandResponse == null) {
                    Log.e(TAG, "Error: load_and_execute setup bio failed");
                    return -2;
                }
                SpayTuiTACommands.SetupBio.Response response = new SpayTuiTACommands.SetupBio.Response(tACommandResponse);
                if (response.mRetVal.retCode.get() != 0L) {
                    Log.e(TAG, "Error: SetupBio return " + response.mRetVal.retCode.get());
                }
                if (response.mRetVal.retCode.get() == 0L && bQC) {
                    return this.start_secure_touch();
                }
                long l2 = response.mRetVal.retCode.get();
                return (int)l2;
            }
            return n2;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int setupPin() {
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            TACommandResponse tACommandResponse;
            block10 : {
                block9 : {
                    if (DEBUG) {
                        Log.d(TAG, "Calling Setup Pin");
                    }
                    if (this.mPaymentHandle != null) break block9;
                    Log.e(TAG, "Error: Payment Handle is null");
                    return -1;
                }
                tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.SetupPin.Request());
                if (tACommandResponse != null) break block10;
                Log.e(TAG, "Error: load_and_execute failed");
                return -2;
            }
            SpayTuiTACommands.SetupPin.Response response = new SpayTuiTACommands.SetupPin.Response(tACommandResponse);
            if (DEBUG) {
                Log.d(TAG, "Setup Pin return " + response.mRetVal.retCode.get());
            }
            this.mNonce = null;
            if (response.mRetVal.retCode.get() == 0L && bQC) {
                return this.start_secure_touch();
            }
            long l2 = response.mRetVal.retCode.get();
            return (int)l2;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int setupPin(byte[] arrby) {
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            TACommandResponse tACommandResponse;
            block10 : {
                block9 : {
                    if (DEBUG) {
                        Log.d(TAG, "Calling Setup Pin with secure object");
                    }
                    if (this.mPaymentHandle != null) break block9;
                    Log.e(TAG, "Error: Payment Handle is null");
                    return -1;
                }
                tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.SetupPin.Request(arrby));
                if (tACommandResponse != null) break block10;
                Log.e(TAG, "Error: load_and_execute failed");
                return -2;
            }
            SpayTuiTACommands.SetupPin.Response response = new SpayTuiTACommands.SetupPin.Response(tACommandResponse);
            if (DEBUG) {
                Log.d(TAG, "Setup Pin return " + response.mRetVal.retCode.get());
            }
            this.mNonce = null;
            if (response.mRetVal.retCode.get() == 0L && bQC) {
                return this.start_secure_touch();
            }
            long l2 = response.mRetVal.retCode.get();
            return (int)l2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void unloadTA() {
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            this.mNonce = null;
            if (this.mPaymentHandle == null) {
                if (DEBUG) {
                    Log.d(TAG, "unloadTA: mPaymentHandle is null");
                }
            } else {
                this.closeTui();
                super.unloadTA();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int updateFP(byte[] arrby) {
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            if (DEBUG) {
                Log.d(TAG, "Calling UpateFP with secure object");
            }
            if (this.mPaymentHandle == null) {
                Log.e(TAG, "Error: Payment Handle is null");
                return -1;
            }
            TACommandResponse tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.UpdateFP.Request(arrby));
            if (tACommandResponse == null) {
                Log.e(TAG, "Error: load_and_execute failed");
                return -2;
            }
            SpayTuiTACommands.UpdateFP.Response response = new SpayTuiTACommands.UpdateFP.Response(tACommandResponse);
            if (DEBUG) {
                Log.d(TAG, "UpdateFP return " + response.mRetVal.retCode.get());
            }
            if (response.mRetVal.retCode.get() != 0L) return (int)response.mRetVal.retCode.get();
            if (response.mRetVal.update_pin_so.get() != 0) {
                Log.i(TAG, "updateFP: Existing Pin len=" + response.mRetVal.existPinLen.get());
                boolean bl = response.mRetVal.existPinLen.get() > 0L;
                mPinExist = bl;
                if (DEBUG) {
                    Log.d(TAG, "Need to update PIN SO! PIN exist: " + mPinExist + " pin len: " + response.mRetVal.existPinLen.get());
                }
                Log.i(TAG, "updateFP: savePinSo");
                this.savePinSo(response.mRetVal.pin_so.getData());
                return (int)response.mRetVal.retCode.get();
            } else {
                if (!DEBUG) return (int)response.mRetVal.retCode.get();
                Log.d(TAG, "No need to update PIN SO");
            }
            return (int)response.mRetVal.retCode.get();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int verify() {
        int n2 = -1;
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            if (DEBUG) {
                Log.d(TAG, "Calling verify (unclose TUI)");
            }
            if (this.mPaymentHandle == null) {
                Log.e(TAG, "Error: Payment Handle is null");
            } else {
                TACommandResponse tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.Verify.Request(false, false));
                if (tACommandResponse == null) {
                    Log.e(TAG, "Error: load_and_execute failed");
                } else {
                    SpayTuiTACommands.Verify.Response response = new SpayTuiTACommands.Verify.Response(tACommandResponse);
                    if (DEBUG) {
                        Log.d(TAG, "Verify return " + response.mRetVal.retCode.get());
                    }
                    if ((n2 = (int)response.mRetVal.retCode.get()) != 0) return n2;
                    if (!bQC) return n2;
                    int n3 = this.start_secure_touch();
                    return n3;
                }
            }
            return n2;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int verify(boolean bl, boolean bl2) {
        SpayTuiTAController spayTuiTAController = this;
        synchronized (spayTuiTAController) {
            int n2;
            TACommandResponse tACommandResponse;
            block11 : {
                block10 : {
                    if (DEBUG) {
                        Log.d(TAG, "Calling verify: close_tui = " + bl + " display_only = " + bl2);
                    }
                    if (this.mPaymentHandle != null) break block10;
                    Log.e(TAG, "Error: PaymFent Handle is null");
                    return -1;
                }
                tACommandResponse = this.executeNoLoad(new SpayTuiTACommands.Verify.Request(bl, bl2));
                if (tACommandResponse != null) break block11;
                Log.e(TAG, "Error: load_and_execute failed");
                return -2;
            }
            SpayTuiTACommands.Verify.Response response = new SpayTuiTACommands.Verify.Response(tACommandResponse);
            if (DEBUG) {
                Log.d(TAG, "Verify return " + response.mRetVal.retCode.get());
            }
            if ((n2 = (int)response.mRetVal.retCode.get()) != 0) return n2;
            if (!bQC) return n2;
            if (bl2) return n2;
            int n3 = this.start_secure_touch();
            return n3;
        }
    }

}

