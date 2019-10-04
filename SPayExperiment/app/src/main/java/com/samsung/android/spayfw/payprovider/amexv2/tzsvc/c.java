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
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import android.content.Context;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import android.util.Log;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexTAException;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.e;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.f;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.g;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.h;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.i;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.util.List;
import java.util.Map;
import javolution.io.Struct;

public class c
extends h
implements f {
    private static c rO;
    private static String rP;
    private static e rT;
    private com.samsung.android.spayfw.payprovider.amexv2.tzsvc.a rQ = null;
    private a rR = null;
    private com.samsung.android.spayfw.payprovider.amexv2.tzsvc.b rS = null;

    static {
        rP = null;
        rT = new e();
    }

    private c(Context context) {
        super(context, rT);
    }

    public static c D(Context context) {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            if (rO == null) {
                rO = new c(context);
            }
            c c2 = rO;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return c2;
        }
    }

    public static c cz() {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            c c2 = rO;
            // ** MonitorExit[var2] (shouldn't be in output)
            return c2;
        }
    }

    private boolean isSecuritySetupInitialized() {
        if (this.rR != null) {
            if (DEBUG) {
                Log.d((String)"SPAY:AmexTAController", (String)"Device Certs already loaded)");
            }
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int a(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        int n2 = -1;
        c c2 = this;
        synchronized (c2) {
            block15 : {
                AmexCommands.Open.Response response;
                String string;
                block16 : {
                    try {
                        TACommandResponse tACommandResponse;
                        byte[][] arrby4 = g.llVarToBytes(arrby);
                        if (arrby4 != null && arrby4[0] != null) {
                            string = new String(arrby4[0]);
                        } else {
                            Log.d((String)"SPAY:AmexTAController", (String)"byteArray or byteArray[0] is null");
                            string = null;
                        }
                        if (!this.initializeSecuritySetup()) {
                            Log.e((String)"SPAY:AmexTAController", (String)"initializeSecuritySetup failed");
                            break block15;
                        }
                        String string2 = this.rS.aD(string);
                        byte[] arrby5 = null;
                        if (string2 != null) {
                            arrby5 = string2.getBytes();
                        }
                        if ((tACommandResponse = this.executeNoLoad(new AmexCommands.Open.Request(arrby5, arrby2))) == null || tACommandResponse.mResponseCode != 0) {
                            Log.e((String)"SPAY:AmexTAController", (String)"Open: Error: executeNoLoad failed");
                        }
                        response = new AmexCommands.Open.Response(tACommandResponse);
                        int n3 = (int)response.rg.return_code.get();
                        if (n3 != 0) {
                            Log.e((String)"SPAY:AmexTAController", (String)"Open Call Failed");
                            return n3;
                        }
                        break block16;
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    break block15;
                }
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)"Open called Successfully");
                }
                rP = string;
                if (arrby2 == null) return 0;
                byte[] arrby6 = response.rg.qN.getData();
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)("unwrappedBuffer.length  = " + arrby3.length));
                    Log.d((String)"SPAY:AmexTAController", (String)("tmpUnwrappedBuffer.length  = " + arrby6.length));
                }
                if (arrby6 == null || arrby3 == null) {
                    Log.e((String)"SPAY:AmexTAController", (String)"Error: tmpUnwrappedBuffer == null || unwrappedBuffer == null");
                    return -6;
                }
                if (arrby3.length < arrby6.length) {
                    if (!DEBUG) return -32768 + arrby6.length;
                    Log.d((String)"SPAY:AmexTAController", (String)("Require new buffer: size = " + (-32768 + arrby6.length)));
                    return -32768 + arrby6.length;
                }
                System.arraycopy((Object)arrby6, (int)0, (Object)arrby3, (int)0, (int)arrby6.length);
                return arrby6.length;
            }
            return n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public String a(String string, boolean bl) {
        Object object = bl ? (string == null ? null : string.getBytes()) : i.fromBase64(string);
        byte[] arrby = this.a((byte[])object, bl);
        if (bl) {
            return i.toBase64(arrby);
        }
        return new String(arrby);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean a(int n2, byte[] arrby) {
        TACommandResponse tACommandResponse;
        boolean bl;
        if (DEBUG) {
            Log.d((String)"SPAY:AmexTAController", (String)"Calling transmitMstData");
        }
        if (!(bl = this.initializeSecuritySetup())) {
            Log.e((String)"SPAY:AmexTAController", (String)"initializeSecuritySetup failed");
            throw new AmexTAException(-1);
        }
        try {
            tACommandResponse = this.executeNoLoad(new AmexCommands.TransmitMstData.Request(n2, arrby));
            if (tACommandResponse == null) {
                Log.e((String)"SPAY:AmexTAController", (String)"Error: transmitMstData executeNoLoad failed");
                throw new AmexTAException(-1);
            }
            if (tACommandResponse.mResponseCode != 0) {
                throw new AmexTAException(tACommandResponse.mResponseCode);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new AmexTAException(-1);
        }
        long l2 = new AmexCommands.TransmitMstData.Response((TACommandResponse)tACommandResponse).rB.return_code.get();
        if (l2 == 0L) {
            bl = true;
        }
        if (DEBUG) {
            Log.d((String)"SPAY:AmexTAController", (String)("TransmitMstData: ret = " + bl));
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] a(AmexCommands.ProcessDataJwsJwe.JsonOperation jsonOperation, byte[] arrby, byte[] arrby2, String string) {
        c c2 = this;
        synchronized (c2) {
            AmexCommands.ProcessData.Response response;
            if (DEBUG) {
                Log.d((String)"SPAY:AmexTAController", (String)"Calling processDataJwsJwe");
            }
            if (!this.initializeSecuritySetup()) {
                Log.e((String)"SPAY:AmexTAController", (String)"initializeSecuritySetup failed");
                throw new AmexTAException(-1);
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.ProcessDataJwsJwe.Request(jsonOperation, arrby, arrby2, i.getDerChain(string)));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"processData: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                response = new AmexCommands.ProcessData.Response(tACommandResponse);
                int n2 = (int)response.rl.return_code.get();
                if (n2 != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"ProcessDataJwsJwe Call Failed");
                    throw new AmexTAException(n2);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new AmexTAException(-1);
            }
            if (!DEBUG) return response.rl.data.getData();
            Log.d((String)"SPAY:AmexTAController", (String)"ProcessDataJwsJwe called Successfully");
            return response.rl.data.getData();
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public byte[] a(byte[] var1_1, boolean var2_2) {
        block12 : {
            block13 : {
                var17_3 = this;
                // MONITORENTER : var17_3
                if (c.DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)"Calling processData");
                }
                if (!this.initializeSecuritySetup()) {
                    Log.e((String)"SPAY:AmexTAController", (String)"initializeSecuritySetup failed");
                    throw new AmexTAException(-1);
                }
                if (!var2_2) ** GOTO lbl12
                var4_4 = new AmexCommands.ProcessData.Request(var1_1, true);
                break block13;
lbl12: // 1 sources:
                var4_4 = new AmexCommands.ProcessData.Request(var1_1, false);
            }
            if ((var6_5 = this.executeNoLoad(var4_4)) == null || var6_5.mResponseCode != 0) {
                Log.e((String)"SPAY:AmexTAController", (String)"processData: Error: executeNoLoad failed");
                throw new AmexTAException(-1);
            }
            var8_7 = new AmexCommands.ProcessData.Response(var6_5);
            var9_8 = (int)var8_7.rl.return_code.get();
            if (var9_8 != 0) {
                Log.e((String)"SPAY:AmexTAController", (String)"ProcessData Call Failed");
                throw new AmexTAException(var9_8);
            }
            if (c.DEBUG) {
                Log.d((String)"SPAY:AmexTAController", (String)"processData called Successfully");
            }
            if (!var2_2) break block12;
            var12_10 = var11_9 = var8_7.rl.data.getData();
            // MONITOREXIT : var17_3
            return var12_10;
        }
        try {
            var13_11 = var8_7.rl.data.getData();
            return var13_11;
        }
        catch (Exception var5_6) {
            var5_6.printStackTrace();
            throw new AmexTAException(-1);
        }
    }

    public void aC(String string) {
        c c2 = this;
        synchronized (c2) {
            this.rS.aC(string);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int b(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        int n2 = -4;
        c c2 = this;
        synchronized (c2) {
            block16 : {
                AmexCommands.Close.Response response;
                try {
                    byte[][] arrby4 = g.llVarToBytes(arrby);
                    if (arrby4 != null && arrby4[0] != null) {
                        String string = new String(arrby4[0]);
                        if (!string.equalsIgnoreCase(rP)) {
                            Log.e((String)"SPAY:AmexTAController", (String)("Error: Token being closed " + string + " do not match with the active token " + rP));
                            break block16;
                        }
                    } else {
                        Log.d((String)"SPAY:AmexTAController", (String)"byteArray[0] is null");
                    }
                    if (rP == null) {
                        Log.e((String)"SPAY:AmexTAController", (String)"Error: No Active Token to be closed. This must never happen");
                        return -6;
                    }
                    TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.Close.Request(arrby2));
                    if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                        Log.e((String)"SPAY:AmexTAController", (String)"Close: Error: executeNoLoad failed");
                        throw new AmexTAException(-1);
                    }
                    response = new AmexCommands.Close.Response(tACommandResponse);
                    int n3 = (int)response.qO.return_code.get();
                    if (n3 != 0) {
                        Log.e((String)"SPAY:AmexTAController", (String)"Close Call Failed");
                        throw new AmexTAException(n3);
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    return -1;
                }
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)"Close called Successfully");
                }
                this.rS.p(rP, new String(response.qO.qP.getData()));
                rP = null;
                if (arrby2 == null) return 0;
                byte[] arrby5 = response.qO.qQ.getData();
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)("wrappedBuffer.length  = " + arrby3.length));
                    Log.d((String)"SPAY:AmexTAController", (String)("tmpWrappedBuffer.length  = " + arrby5.length));
                }
                if (arrby5 == null || arrby3 == null) {
                    Log.e((String)"SPAY:AmexTAController", (String)"Error: tmpWrappedBuffer == null || wrappedBuffer == null");
                    return -6;
                }
                if (arrby3.length < arrby5.length) {
                    if (!DEBUG) return -32768 + arrby5.length;
                    Log.d((String)"SPAY:AmexTAController", (String)("Require new buffer: size = " + (-32768 + arrby5.length)));
                    return -32768 + arrby5.length;
                }
                System.arraycopy((Object)arrby5, (int)0, (Object)arrby3, (int)0, (int)arrby5.length);
                return arrby5.length;
            }
            return n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public c c(String string, String string2, String string3) {
        c c2 = this;
        synchronized (c2) {
            List<byte[]> list;
            byte[] arrby;
            if (DEBUG) {
                Log.d((String)"SPAY:AmexTAController", (String)"Calling processRequestData");
            }
            if (!this.initializeSecuritySetup()) {
                Log.e((String)"SPAY:AmexTAController", (String)"initializeSecuritySetup failed");
                throw new AmexTAException(-1);
            }
            try {
                list = i.getDerChain(string);
                arrby = string2 == null ? null : string2.getBytes();
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new AmexTAException(-1);
            }
            byte[] arrby2 = string3 == null ? null : string3.getBytes();
            TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.ProcessRequestData.Request(list, arrby, arrby2));
            if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                Log.e((String)"SPAY:AmexTAController", (String)"processRequestData: Error: executeNoLoad failed");
                throw new AmexTAException(-1);
            }
            AmexCommands.ProcessRequestData.Response response = new AmexCommands.ProcessRequestData.Response(tACommandResponse);
            int n2 = (int)response.rs.return_code.get();
            if (n2 != 0) {
                Log.e((String)"SPAY:AmexTAController", (String)"ProcessRequestData Call Failed");
                throw new AmexTAException(n2);
            }
            if (DEBUG) {
                Log.d((String)"SPAY:AmexTAController", (String)"processRequestData called Successfully");
            }
            c c3 = new c();
            c3.encryptedRequestData = i.toBase64(response.rs.pW.getData());
            c3.encryptionParams = i.toBase64(response.rs.pX.getData());
            c3.requestDataSignature = i.toBase64(response.rs.pY.getData());
            return c3;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public a cA() {
        c c2 = this;
        synchronized (c2) {
            if (this.initializeSecuritySetup()) return this.rR;
            Log.e((String)"SPAY:AmexTAController", (String)"initializeSecuritySetup failed");
            throw new AmexTAException(-1);
        }
    }

    @Override
    public int close(byte[] arrby) {
        c c2 = this;
        synchronized (c2) {
            int n2 = this.b(arrby, null, null);
            return n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int computeAC(byte[] arrby, byte[] arrby2) {
        int n2 = -1;
        c c2 = this;
        synchronized (c2) {
            try {
                TACommandResponse tACommandResponse = c.cz().executeNoLoad(new AmexCommands.ComputeAC.Request(arrby));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"computeAC: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                AmexCommands.ComputeAC.Response response = new AmexCommands.ComputeAC.Response(tACommandResponse);
                int n3 = (int)response.qS.return_code.get();
                if (n3 != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"Compute AC call failed");
                    throw new AmexTAException(n3);
                }
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)"computeAC called Successfully");
                    Log.d((String)"SPAY:AmexTAController", (String)("respValue = " + n3));
                }
                byte[] arrby3 = response.qS.pP.getData();
                System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)0, (int)arrby3.length);
                int n4 = response.qS.qT.get();
                return n4;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return n2;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int getSignatureData(byte[] arrby, byte[] arrby2) {
        c c2 = this;
        synchronized (c2) {
            int n2;
            byte[] arrby3;
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.GetSignatureData.Request(arrby));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"GetSignatureData: Error: executeNoLoad failed");
                    return -1;
                }
                AmexCommands.GetSignatureData.Response response = new AmexCommands.GetSignatureData.Response(tACommandResponse);
                n2 = (int)response.qW.return_code.get();
                if (n2 != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"GetSignatureData Call Failed");
                }
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)"GetSignatureData called Successfully");
                }
                arrby3 = response.qW.qX.getData();
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)("sigBuffer.length  = " + arrby2.length));
                    Log.d((String)"SPAY:AmexTAController", (String)("tmpSigBuffer.length  = " + arrby3.length));
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return -1;
            }
            if (arrby2 == null || arrby3 == null) {
                Log.e((String)"SPAY:AmexTAController", (String)"Error: sigBuffer == null || tmpSigBuffer == null");
                return -6;
            }
            if (arrby2.length < arrby3.length) {
                if (!DEBUG) return -32768 + arrby3.length;
                Log.d((String)"SPAY:AmexTAController", (String)("Require new buffer: size = " + (-32768 + arrby3.length)));
                return -32768 + arrby3.length;
            }
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)0, (int)arrby3.length);
            return arrby3.length;
            return n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int init(byte[] arrby) {
        int n2 = -1;
        c c2 = this;
        synchronized (c2) {
            try {
                TACommandResponse tACommandResponse = c.cz().executeNoLoad(new AmexCommands.Init.Request(arrby));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"Init: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                int n3 = (int)new AmexCommands.Init.Response((TACommandResponse)tACommandResponse).qZ.return_code.get();
                if (n3 != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"Init Call Failed");
                    throw new AmexTAException(n3);
                }
                if (!DEBUG) return 0;
                Log.d((String)"SPAY:AmexTAController", (String)"Init called Successfully");
                return 0;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return n2;
            }
        }
    }

    @Override
    protected boolean init() {
        if (!super.init()) {
            Log.e((String)"SPAY:AmexTAController", (String)"Error: init failed");
            return false;
        }
        this.rQ = new com.samsung.android.spayfw.payprovider.amexv2.tzsvc.a(this);
        this.rS = new com.samsung.android.spayfw.payprovider.amexv2.tzsvc.b(this);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int initializeSecureChannel(byte[] arrby, byte[] arrby2) {
        c c2 = this;
        synchronized (c2) {
            int n2;
            byte[] arrby3;
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.InitializeSecureChannel.Request(arrby));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"initializeSecureChannel: Error: executeNoLoad failed");
                    return -1;
                }
                AmexCommands.InitializeSecureChannel.Response response = new AmexCommands.InitializeSecureChannel.Response(tACommandResponse);
                n2 = (int)response.rb.return_code.get();
                if (n2 != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"initializeSecureChannel Call Failed");
                }
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)"initializeSecureChannel called Successfully");
                }
                arrby3 = response.rb.rc.getData();
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)("ephemeralBuffer.length  = " + arrby2.length));
                    Log.d((String)"SPAY:AmexTAController", (String)("tmpEphemeralBuffer.length  = " + arrby3.length));
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return -1;
            }
            if (arrby3 == null || arrby2 == null) {
                Log.e((String)"SPAY:AmexTAController", (String)"Error: tmpEphemeralBuffer == null || ephemeralBuffer == null");
                return -6;
            }
            if (arrby2.length < arrby3.length) {
                if (!DEBUG) return -32768 + arrby3.length;
                Log.d((String)"SPAY:AmexTAController", (String)("Require new buffer: size = " + (-32768 + arrby3.length)));
                return -32768 + arrby3.length;
            }
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)0, (int)arrby3.length);
            return arrby3.length;
            return n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean initializeSecuritySetup() {
        block10 : {
            block9 : {
                if (!this.isSecuritySetupInitialized()) {
                    if (DEBUG) {
                        Log.d((String)"SPAY:AmexTAController", (String)"Calling initializeSecuritySetup");
                    }
                    if (!this.isTALoaded()) {
                        Log.e((String)"SPAY:AmexTAController", (String)"initializeSecuritySetup: Error: TA is not loaded, please call loadTA() API first!");
                        return false;
                    }
                    if (!this.rQ.isLoaded()) {
                        Log.d((String)"SPAY:AmexTAController", (String)"mAmexDeviceCerts is not loaded");
                        if (!this.rQ.load()) {
                            Log.e((String)"SPAY:AmexTAController", (String)"Error: Amex Device Certs Load failed");
                            return false;
                        }
                    }
                    byte[] arrby = this.rQ.getDevicePrivateSignCert();
                    byte[] arrby2 = this.rQ.getDevicePrivateEncryptionCert();
                    try {
                        TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.LoadCerts.Request(arrby, arrby2));
                        if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                            Log.e((String)"SPAY:AmexTAController", (String)"loadAllCerts: Error: executeNoLoad failed");
                            throw new AmexTAException(-1);
                        }
                        AmexCommands.LoadCerts.Response response = new AmexCommands.LoadCerts.Response(tACommandResponse);
                        this.rR = new a();
                        this.rR.deviceCertificate = i.convertToPem(response.rf.deviceRootRSA2048PubCert.getData(), false);
                        this.rR.deviceSigningCertificate = i.convertToPem(response.rf.deviceSignRSA2048PubCert.getData(), false);
                        this.rR.deviceEncryptionCertificate = i.convertToPem(response.rf.pR.getData(), false);
                        if (!DEBUG) break block9;
                        break block10;
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        return false;
                    }
                }
            }
            return true;
        }
        Log.d((String)"SPAY:AmexTAController", (String)"initializeSecuritySetup called Successfully");
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int lcm(int n2) {
        int n3 = -1;
        c c2 = this;
        synchronized (c2) {
            try {
                TACommandResponse tACommandResponse = c.cz().executeNoLoad(new AmexCommands.LCM.Request(n2));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"lcm: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                int n4 = (int)new AmexCommands.LCM.Response((TACommandResponse)tACommandResponse).re.return_code.get();
                if (n4 != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"lcm Call Failed");
                    throw new AmexTAException(n4);
                }
                if (!DEBUG) return 0;
                Log.d((String)"SPAY:AmexTAController", (String)"lcm called Successfully");
                return 0;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return n3;
            }
        }
    }

    @Override
    public boolean loadTA() {
        c c2 = this;
        synchronized (c2) {
            boolean bl = super.loadTA();
            return bl;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String o(String string, String string2) {
        c c2 = this;
        synchronized (c2) {
            AmexCommands.ProcessResponseData.Response response;
            if (DEBUG) {
                Log.d((String)"SPAY:AmexTAController", (String)"Calling processRequestData");
            }
            if (!this.initializeSecuritySetup()) {
                Log.e((String)"SPAY:AmexTAController", (String)"initializeSecuritySetup failed");
                throw new AmexTAException(-1);
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.ProcessResponseData.Request(i.fromBase64(string), i.fromBase64(string2)));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"processRequestData: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                response = new AmexCommands.ProcessResponseData.Response(tACommandResponse);
                int n2 = (int)response.rt.return_code.get();
                if (n2 != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"ProcessResponseData Call Failed");
                    throw new AmexTAException(n2);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new AmexTAException(-1);
            }
            if (!DEBUG) return i.toBase64(response.rt.qb.getData());
            Log.d((String)"SPAY:AmexTAController", (String)"processRequestData called Successfully");
            return i.toBase64(response.rt.qb.getData());
        }
    }

    @Override
    public int open(byte[] arrby) {
        c c2 = this;
        synchronized (c2) {
            int n2 = this.a(arrby, null, null);
            return n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int perso(int n2, byte[] arrby, byte[] arrby2) {
        int n3 = -1;
        c c2 = this;
        synchronized (c2) {
            try {
                TACommandResponse tACommandResponse;
                c c3 = c.cz();
                AmexCommands.PersoToken.Request request = new AmexCommands.PersoToken.Request(n2, arrby, arrby2);
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)("lockcode = " + i.encodeHex(arrby2)));
                }
                if ((tACommandResponse = c3.executeNoLoad(request)) == null || tACommandResponse.mResponseCode != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"perso: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                int n4 = (int)new AmexCommands.PersoToken.Response((TACommandResponse)tACommandResponse).rk.return_code.get();
                if (n4 != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"perso Call Failed");
                    throw new AmexTAException(n4);
                }
                if (!DEBUG) return 0;
                Log.d((String)"SPAY:AmexTAController", (String)"perso called Successfully");
                return 0;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return n3;
            }
        }
    }

    public void q(String string, String string2) {
        c c2 = this;
        synchronized (c2) {
            this.rS.q(string, string2);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int reqMST(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        int n2 = -1;
        c c2 = this;
        synchronized (c2) {
            c c3;
            byte[] arrby4;
            block11 : {
                try {
                    c3 = c.cz();
                    if (arrby == null || arrby.length == 0 || arrby2 == null || arrby2.length == 0) {
                        Log.e((String)"SPAY:AmexTAController", (String)"reqMST: Error: Invalid track data");
                        throw new AmexTAException(-4);
                    }
                    if (arrby3 != null && arrby3.length != 0) break block11;
                    Log.e((String)"SPAY:AmexTAController", (String)"reqMST: Error: Invalid tid buffer");
                    throw new AmexTAException(-3);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    return n2;
                }
            }
            TACommandResponse tACommandResponse = c3.executeNoLoad(new AmexCommands.ReqMST.Request(arrby, arrby2));
            if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                Log.e((String)"SPAY:AmexTAController", (String)"reqMST: Error: executeNoLoad failed");
                throw new AmexTAException(-1);
            }
            AmexCommands.ReqMST.Response response = new AmexCommands.ReqMST.Response(tACommandResponse);
            int n3 = (int)response.ru.return_code.get();
            if (n3 != 0) {
                Log.e((String)"SPAY:AmexTAController", (String)"ReqMST call failed");
                throw new AmexTAException(n3);
            }
            if (DEBUG) {
                Log.d((String)"SPAY:AmexTAController", (String)"reqMST called Successfully");
            }
            if ((arrby4 = response.ru.rv.getData()).length > arrby3.length) {
                Log.e((String)"SPAY:AmexTAController", (String)"reqMST: Error: TID buffer too big");
                throw new AmexTAException(-3);
            }
            System.arraycopy((Object)arrby4, (int)0, (Object)arrby3, (int)0, (int)arrby4.length);
            return response.ru.qT.get();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int sign(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        int n2 = -1;
        c c2 = this;
        synchronized (c2) {
            try {
                TACommandResponse tACommandResponse;
                c c3 = c.cz();
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)("lockcode = " + i.encodeHex(arrby3)));
                }
                if ((tACommandResponse = c3.executeNoLoad(new AmexCommands.Sign.Request(arrby, arrby3))) == null || tACommandResponse.mResponseCode != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"sign: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                AmexCommands.Sign.Response response = new AmexCommands.Sign.Response(tACommandResponse);
                int n3 = (int)response.ry.return_code.get();
                if (n3 != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"sign Call Failed");
                    throw new AmexTAException(n3);
                }
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)"sign called Successfully");
                }
                byte[] arrby4 = response.ry.rz.getData();
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)("signatureData = " + i.encodeHex(arrby4)));
                }
                System.arraycopy((Object)arrby4, (int)0, (Object)arrby2, (int)0, (int)arrby4.length);
                short s2 = response.ry.rA.get();
                return s2;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return n2;
            }
        }
    }

    @Override
    public void unloadTA() {
        c c2 = this;
        synchronized (c2) {
            this.rR = null;
            super.unloadTA();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int unwrap(int n2, byte[] arrby, int n3, byte[] arrby2, int n4) {
        c c2 = this;
        synchronized (c2) {
            int n5;
            byte[] arrby3;
            if (arrby == null || arrby2 == null) {
                Log.e((String)"SPAY:AmexTAController", (String)"Error: Invalid Params");
                return -4;
            }
            Log.e((String)"SPAY:AmexTAController", (String)("UnWrap: mode : " + n2));
            boolean bl = false;
            if (n2 == 0) {
                bl = true;
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.UnWrap.Request(arrby, bl));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"UnWrap: Error: executeNoLoad failed");
                    return -1;
                }
                AmexCommands.UnWrap.Response response = new AmexCommands.UnWrap.Response(tACommandResponse);
                n5 = (int)response.rD.return_code.get();
                if (n5 != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"UnWrap Call Failed");
                }
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)"UnWrap called Successfully");
                }
                arrby3 = response.rD.qN.getData();
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)("unwrappedBuffer.length  = " + arrby2.length));
                    Log.d((String)"SPAY:AmexTAController", (String)("tmpUnwrappedBuffer.length  = " + arrby3.length));
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return -1;
            }
            if (arrby3 == null || arrby2 == null) {
                Log.e((String)"SPAY:AmexTAController", (String)"Error: tmpUnwrappedBuffer == null || unwrappedBuffer == null");
                return -6;
            }
            if (arrby2.length < arrby3.length) {
                if (!DEBUG) return -32768 + arrby3.length;
                Log.d((String)"SPAY:AmexTAController", (String)("Require new buffer: size = " + (-32768 + arrby3.length)));
                return -32768 + arrby3.length;
            }
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)0, (int)arrby3.length);
            return arrby3.length;
            return n5;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int update(byte[] arrby) {
        int n2 = -1;
        c c2 = this;
        synchronized (c2) {
            try {
                TACommandResponse tACommandResponse = c.cz().executeNoLoad(new AmexCommands.Update.Request(arrby));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"update: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                if ((int)new AmexCommands.Update.Response((TACommandResponse)tACommandResponse).rF.return_code.get() != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"update Call Failed");
                    return -6;
                }
                if (!DEBUG) return 0;
                Log.d((String)"SPAY:AmexTAController", (String)"update called Successfully");
                return 0;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return n2;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int updateSecureChannel(byte[] arrby, byte[] arrby2) {
        c c2 = this;
        synchronized (c2) {
            try {
                TACommandResponse tACommandResponse = c.cz().executeNoLoad(new AmexCommands.UpdateSecureChannel.Request(arrby));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"updateSecureChannel: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                AmexCommands.UpdateSecureChannel.Response response = new AmexCommands.UpdateSecureChannel.Response(tACommandResponse);
                int n2 = (int)response.rH.return_code.get();
                if (n2 != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"updateSecureChannel Call Failed");
                    throw new AmexTAException(n2);
                }
                if (!DEBUG) return 0;
                Log.d((String)"SPAY:AmexTAController", (String)"updateSecureChannel called Successfully");
                return 0;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return -1;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public int verify(byte[] arrby, byte[] arrby2) {
        int n2;
        c c2 = this;
        // MONITORENTER : c2
        try {
            TACommandResponse tACommandResponse = c.cz().executeNoLoad(new AmexCommands.Verify.Request(arrby));
            if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                Log.e((String)"SPAY:AmexTAController", (String)"Verify: Error: executeNoLoad failed");
                throw new AmexTAException(-1);
            }
            AmexCommands.Verify.Response response = new AmexCommands.Verify.Response(tACommandResponse);
            n2 = (int)response.rJ.return_code.get();
            if (DEBUG) {
                Log.d((String)"SPAY:AmexTAController", (String)"Verify called Successfully");
                Log.d((String)"SPAY:AmexTAController", (String)("respValue = " + n2));
            }
            arrby2[0] = response.rJ.rK.getData()[0];
            return n2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            n2 = -1;
            // MONITOREXIT : c2
            return n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int wrap(byte[] arrby, int n2, byte[] arrby2, int n3) {
        c c2 = this;
        synchronized (c2) {
            byte[] arrby3;
            int n4;
            if (arrby == null || arrby2 == null) {
                Log.e((String)"SPAY:AmexTAController", (String)"Error: Invalid Params");
                return -4;
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.Wrap.Request(arrby));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"Wrap: Error: executeNoLoad failed");
                    return -1;
                }
                AmexCommands.Wrap.Response response = new AmexCommands.Wrap.Response(tACommandResponse);
                n4 = (int)response.rL.return_code.get();
                if (n4 != 0) {
                    Log.e((String)"SPAY:AmexTAController", (String)"Wrap Call Failed");
                }
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)"Wrap called Successfully");
                }
                arrby3 = response.rL.qQ.getData();
                if (DEBUG) {
                    Log.d((String)"SPAY:AmexTAController", (String)("wrappedBuffer.length  = " + arrby2.length));
                    Log.d((String)"SPAY:AmexTAController", (String)("tmpWrappedBuffer.length  = " + arrby3.length));
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return -1;
            }
            if (arrby3 == null || arrby2 == null) {
                Log.e((String)"SPAY:AmexTAController", (String)"Error: tmpUnwrappedBuffer == null || unwrappedBuffer == null");
                return -6;
            }
            if (arrby2.length < arrby3.length) {
                if (!DEBUG) return -32768 + arrby3.length;
                Log.d((String)"SPAY:AmexTAController", (String)("Require new buffer: size = " + (-32768 + arrby3.length)));
                return -32768 + arrby3.length;
            }
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)0, (int)arrby3.length);
            return arrby3.length;
            return n4;
        }
    }

    public static class a {
        public String deviceCertificate;
        public String deviceEncryptionCertificate;
        public String deviceSigningCertificate;
    }

    public static class b {
        public String merchantCertificate;
        public Map<String, String> txnAttributes;
    }

    public static class c {
        public String encryptedRequestData;
        public String encryptionParams;
        public String requestDataSignature;
    }

}

