/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  android.util.Log
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 */
package com.samsung.android.spayfw.payprovider.krcc.tzsvc;

import android.content.Context;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import android.util.Log;
import com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands;
import com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccTAException;
import com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccTAInfo;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.util.Arrays;
import javolution.io.Struct;

public class KrccTAController
extends TAController {
    public static final int SUCCESS_RET_VAL = 0;
    private static final String TAG = "Spay:KrccTAController";
    private static KrccTAInfo TA_INFO;
    private static KrccTAController mInstance;
    private byte[] krcc_cert_sign = null;
    private byte[] krcc_cert_sub = null;

    static {
        mInstance = null;
        TA_INFO = new KrccTAInfo();
    }

    private KrccTAController(Context context) {
        super(context, TA_INFO);
    }

    public static KrccTAController createOnlyInstance(Context context) {
        Class<KrccTAController> class_ = KrccTAController.class;
        synchronized (KrccTAController.class) {
            if (mInstance == null) {
                mInstance = new KrccTAController(context);
            }
            KrccTAController krccTAController = mInstance;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return krccTAController;
        }
    }

    public static KrccTAController getInstance() {
        Class<KrccTAController> class_ = KrccTAController.class;
        synchronized (KrccTAController.class) {
            KrccTAController krccTAController = mInstance;
            // ** MonitorExit[var2] (shouldn't be in output)
            return krccTAController;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void clearMstData() {
        if (DEBUG) {
            Log.d((String)TAG, (String)"Calling clearMstData");
        }
        if (!this.isTALoaded()) {
            Log.e((String)TAG, (String)"clearMstData: Error: TA is not loaded, please call loadTA() API first!");
            throw new KrccTAException("TA not loaded", 1003);
        }
        try {
            TACommandResponse tACommandResponse;
            block12 : {
                try {
                    tACommandResponse = this.executeNoLoad(new KrccCommands.ClearMstData.Request(0));
                    if (tACommandResponse == null) {
                        Log.e((String)TAG, (String)"Error: clearMstData executeNoLoad failed");
                        throw new KrccTAException("Error communicating with the TA", 1001);
                    }
                    if (tACommandResponse.mResponseCode == 0) break block12;
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    if (!(exception instanceof IllegalArgumentException)) throw new KrccTAException("Error communicating with the TA", 1001);
                    {
                        throw new KrccTAException("Invalid Input", 1004);
                    }
                }
                Log.e((String)TAG, (String)("clearMstData: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode));
                throw new KrccTAException("TA command returned error", 1001);
            }
            KrccCommands.ClearMstData.Response response = new KrccCommands.ClearMstData.Response(tACommandResponse);
            if (DEBUG) {
                Log.d((String)TAG, (String)"clearMstData: success ");
            }
            if (response.mRetVal.return_code.get() == 0L) return;
            Log.e((String)TAG, (String)"clearMstData: Fail ");
            return;
        }
        finally {
            this.moveSecOsToDefaultCore();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean loadCert() {
        block7 : {
            if (DEBUG) {
                Log.d((String)TAG, (String)"Calling loadCert");
            }
            if (!this.isTALoaded()) {
                Log.e((String)TAG, (String)"loadCert: Error: TA is not loaded, please call loadTA() API first!");
                return false;
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new KrccCommands.LoadCert.Request(this.krcc_cert_sign, this.krcc_cert_sub));
                if (tACommandResponse == null) {
                    Log.e((String)TAG, (String)"loadCert: Error: executeNoLoad failed");
                    return false;
                }
                KrccCommands.LoadCert.Response response = new KrccCommands.LoadCert.Response(tACommandResponse);
                if (DEBUG) {
                    Log.d((String)TAG, (String)("err_msg= " + Arrays.toString((Object[])response.mRetVal.error_msg)));
                    Log.d((String)TAG, (String)("ret_code= " + response.mRetVal.return_code));
                    Log.d((String)TAG, (String)"loadCert called Successfully");
                }
                return true;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                if (!(exception instanceof IllegalArgumentException)) break block7;
                throw new KrccTAException("Invalid Input", 1004);
            }
        }
        throw new KrccTAException("Error communicating with the TA", 1001);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean prepareMstData(byte[] arrby, byte[] arrby2) {
        TACommandResponse tACommandResponse;
        if (DEBUG) {
            Log.d((String)TAG, (String)"Calling prepareMstData");
        }
        if (arrby == null || arrby.length > 128) {
            if (arrby != null) {
                Log.e((String)TAG, (String)("prepareMstData: Error: invalid track length - " + arrby.length));
                throw new KrccTAException("Invalid Input", 1004);
            }
            Log.e((String)TAG, (String)"prepareMstData: Error: _track is null!");
            throw new KrccTAException("Invalid Input", 1004);
        }
        if (arrby2 == null || arrby2.length > 256) {
            if (arrby2 != null) {
                Log.e((String)TAG, (String)("prepareMstData: Error: invalid track signature length - " + arrby2.length));
                throw new KrccTAException("Invalid Input", 1004);
            }
            Log.e((String)TAG, (String)"prepareMstData: Error: _signedTrackHash is null!");
            throw new KrccTAException("Invalid Input", 1004);
        }
        if (!this.isTALoaded()) {
            Log.e((String)TAG, (String)"prepareMstData: Error: TA is not loaded, please call loadTA() API first!");
            throw new KrccTAException("TA not loaded", 1003);
        }
        try {
            this.loadCert();
            tACommandResponse = this.executeNoLoad(new KrccCommands.PrepareMstData.Request(arrby, arrby2));
            if (tACommandResponse == null) {
                Log.e((String)TAG, (String)"prepareMstData: Error: executeNoLoad failed");
                throw new KrccTAException("Error communicating with the TA", 1001);
            }
            if (tACommandResponse.mResponseCode != 0) {
                Log.e((String)TAG, (String)("prepareMstData: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode));
                throw new KrccTAException("TA command returned error", 1001);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            this.moveSecOsToDefaultCore();
            if (!(exception instanceof IllegalArgumentException)) throw new KrccTAException("Error communicating with the TA", 1001);
            throw new KrccTAException("Invalid Input", 1004);
        }
        KrccCommands.PrepareMstData.Response response = new KrccCommands.PrepareMstData.Response(tACommandResponse);
        if (DEBUG) {
            Log.d((String)TAG, (String)("prepareMstData: response.mRetVal.error_msg = " + Arrays.toString((Object[])response.mRetVal.error_msg)));
        }
        this.moveSecOsToCore4();
        long l2 = response.mRetVal.return_code.get();
        if (l2 != 0L) return false;
        return true;
    }

    public void setKrccServerCerts(byte[] arrby, byte[] arrby2) {
        this.krcc_cert_sign = arrby;
        this.krcc_cert_sub = arrby2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void transmitMstData(int n2, byte[] arrby) {
        TACommandResponse tACommandResponse;
        if (DEBUG) {
            Log.d((String)TAG, (String)"Calling transmitMstData");
        }
        if (arrby == null || arrby.length > 16) {
            if (arrby != null) {
                Log.e((String)TAG, (String)("transmitMstData: Error: config length invalid! config.len = " + arrby.length));
                throw new KrccTAException("Invalid Input", 1004);
            }
            Log.e((String)TAG, (String)"transmitMstData: Error: config is null!");
            throw new KrccTAException("Invalid Input", 1004);
        }
        if (!this.isTALoaded()) {
            Log.e((String)TAG, (String)"transmitMstData: Error: TA is not loaded, please call loadTA() API first!");
            throw new KrccTAException("TA not loaded", 1003);
        }
        if (!this.makeSystemCall(1)) {
            Log.e((String)TAG, (String)"transmitMstData: Error: Failed to turn MST Driver on");
            throw new KrccTAException("Failed to turn MST ON", 9001);
        }
        try {
            tACommandResponse = this.executeNoLoad(new KrccCommands.TransmitMstData.Request(n2, arrby));
            if (!this.makeSystemCall(2)) {
                Log.w((String)TAG, (String)"transmitMstData: Error: Failed to turn MST Driver off");
            }
            if (tACommandResponse == null) {
                Log.e((String)TAG, (String)"Error: transmitMstData executeNoLoad failed");
                throw new KrccTAException("Error communicating with the TA", 1001);
            }
            if (tACommandResponse.mResponseCode != 0) {
                Log.e((String)TAG, (String)("transmitMstData: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode));
                throw new KrccTAException("TA command returned error", 1001);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new KrccTAException("Error communicating with the TA", 1001);
            throw new KrccTAException("Invalid Input", 1004);
        }
        if (new KrccCommands.TransmitMstData.Response((TACommandResponse)tACommandResponse).mRetVal.return_code.get() == 0L) return;
        Log.e((String)TAG, (String)"transmitMstData: fail ");
    }
}

