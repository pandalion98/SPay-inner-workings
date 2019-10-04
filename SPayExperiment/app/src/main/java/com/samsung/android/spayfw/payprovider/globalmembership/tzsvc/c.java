/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.spay.CertInfo
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  android.util.Log
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.text.SimpleDateFormat
 *  java.util.Arrays
 *  java.util.Date
 *  java.util.HashMap
 *  java.util.Map
 *  java.util.TimeZone
 */
package com.samsung.android.spayfw.payprovider.globalmembership.tzsvc;

import android.content.Context;
import android.spay.CertInfo;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import android.util.Log;
import com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands;
import com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipTAException;
import com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.d;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import javolution.io.Struct;

public class c
extends TAController {
    private static final TimeZone GMT = TimeZone.getTimeZone((String)"GMT");
    public static TAInfo TA_INFO;
    private static Map<String, a> mServerCertsHash;
    private static c zw;
    private CertInfo certsInfoCache = null;
    private b zx = null;
    private com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.b zy = null;

    static {
        zw = null;
        TA_INFO = new d();
        mServerCertsHash = new HashMap();
    }

    private c(Context context) {
        super(context, TA_INFO);
    }

    public static c F(Context context) {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            if (zw == null) {
                zw = new c(context);
            }
            c c2 = zw;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return c2;
        }
    }

    private static byte charToByte(char c2) {
        return (byte)"0123456789ABCDEF".indexOf((int)c2);
    }

    public static c eB() {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            c c2 = zw;
            // ** MonitorExit[var2] (shouldn't be in output)
            return c2;
        }
    }

    private static byte[] getCurrentTimestamp(long l2) {
        String string = c.gmtTimestamp(new Date(l2));
        if (DEBUG) {
            Log.d((String)"SpayFw_GMTAController", (String)("Network timestamp in sdf  =" + string));
        }
        return c.hexStringToBytes(string);
    }

    private static String gmtTimestamp(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        simpleDateFormat.setTimeZone(GMT);
        return simpleDateFormat.format(date);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static byte[] hexStringToBytes(String string) {
        if (string == null) return null;
        if (string.equals((Object)"")) {
            return null;
        }
        String string2 = string.toUpperCase();
        int n2 = string2.length() / 2;
        char[] arrc = string2.toCharArray();
        byte[] arrby = new byte[n2];
        int n3 = 0;
        while (n3 < n2) {
            int n4 = n3 * 2;
            arrby[n3] = (byte)(c.charToByte(arrc[n4]) << 4 | c.charToByte(arrc[n4 + 1]));
            ++n3;
        }
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean loadAllCerts(String string) {
        block10 : {
            if (DEBUG) {
                Log.d((String)"SpayFw_GMTAController", (String)"Calling loadAllCerts");
            }
            if (!this.isTALoaded()) {
                Log.e((String)"SpayFw_GMTAController", (String)"loadAllCerts: Error: TA is not loaded, please call loadTA() API first!");
                return false;
            }
            if (!this.zy.isLoaded()) {
                Log.d((String)"SpayFw_GMTAController", (String)"mGlobalMembershipDeviceCerts is not loaded");
                if (!this.zy.load()) {
                    Log.e((String)"SpayFw_GMTAController", (String)"Error: GlobalMembership Device Certs Load failed");
                    return false;
                }
            }
            byte[] arrby = this.zy.getDevicePrivateSignCert();
            byte[] arrby2 = this.zy.getDevicePrivateEncryptionCert();
            if (arrby == null || arrby2 == null) {
                Log.e((String)"SpayFw_GMTAController", (String)"loadAllCerts: Error: Certificate Data is NULL");
                this.certsInfoCache = null;
                return false;
            }
            try {
                a a2 = (a)mServerCertsHash.get((Object)string);
                GlobalMembershipCommands.LoadCerts.Request request = a2 != null ? new GlobalMembershipCommands.LoadCerts.Request(arrby, arrby2, a2.zz, a2.zA, a2.zB) : new GlobalMembershipCommands.LoadCerts.Request(arrby, arrby2, null, null, null);
                TACommandResponse tACommandResponse = this.executeNoLoad(request);
                if (tACommandResponse == null) {
                    Log.e((String)"SpayFw_GMTAController", (String)"loadAllCerts: Error: executeNoLoad failed");
                    return false;
                }
                GlobalMembershipCommands.LoadCerts.Response response = new GlobalMembershipCommands.LoadCerts.Response(tACommandResponse);
                this.zx = new b(response.zu.cert_drk.getData(), response.zu.cert_sign.getData(), response.zu.cert_encrypt.getData());
                if (DEBUG) {
                    Log.d((String)"SpayFw_GMTAController", (String)("enc_cert= " + Arrays.toString((byte[])response.zu.cert_encrypt.getData())));
                    Log.d((String)"SpayFw_GMTAController", (String)("sign_cert= " + Arrays.toString((byte[])response.zu.cert_sign.getData())));
                    Log.d((String)"SpayFw_GMTAController", (String)("drk= " + Arrays.toString((byte[])response.zu.cert_drk.getData())));
                    Log.d((String)"SpayFw_GMTAController", (String)"loadAllCerts called Successfully");
                }
                return true;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                if (!(exception instanceof IllegalArgumentException)) break block10;
                throw new GlobalMembershipTAException("Invalid Input", 1004);
            }
        }
        throw new GlobalMembershipTAException("Error communicating with the TA", 1001);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] a(String string, String string2, byte[] arrby) {
        if (DEBUG) {
            Log.d((String)"SpayFw_GMTAController", (String)"Calling Global Membership add card");
        }
        if (!this.isTALoaded()) {
            Log.e((String)"SpayFw_GMTAController", (String)"addCard: Error: TA is not loaded, please call loadTA() API first!");
            return null;
        }
        if (!this.loadAllCerts(string)) {
            Log.e((String)"SpayFw_GMTAController", (String)"addCard: Error: loadAllCerts returned false!");
            return null;
        }
        try {
            TACommandResponse tACommandResponse = this.executeNoLoad(new GlobalMembershipCommands.AddCard.Request(string2.getBytes(), arrby));
            if (tACommandResponse == null) {
                Log.e((String)"SpayFw_GMTAController", (String)"add Card: Error: executeNoLoad failed");
                return null;
            }
            GlobalMembershipCommands.AddCard.Response response = new GlobalMembershipCommands.AddCard.Response(tACommandResponse);
            if (!DEBUG) return response.zm.zn.getData();
            Log.d((String)"SpayFw_GMTAController", (String)("tzEnc data  = " + Arrays.toString((byte[])response.zm.zn.getData())));
            return response.zm.zn.getData();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new GlobalMembershipTAException("Error communicating with the TA", 1001);
            throw new GlobalMembershipTAException("Invalid Input", 1004);
        }
    }

    public b aR(String string) {
        if (this.zx == null && !this.loadAllCerts(string)) {
            Log.e((String)"SpayFw_GMTAController", (String)"getCerts: Error: loadAllCerts returned false!");
        }
        return this.zx;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean authenticateTransaction(byte[] arrby) {
        boolean bl = false;
        if (DEBUG) {
            Log.d((String)"SpayFw_GMTAController", (String)"Calling authenticateTransaction");
        }
        if (!this.isTALoaded()) {
            Log.e((String)"SpayFw_GMTAController", (String)"authenticateTransaction: Error: TA is not loaded, please call loadTA() API first!");
            return bl;
        }
        try {
            TACommandResponse tACommandResponse = this.executeNoLoad(new GlobalMembershipCommands.AuthenticateTransaction.Request(arrby));
            if (tACommandResponse == null) {
                Log.e((String)"SpayFw_GMTAController", (String)"Error: authenticateTransaction executeNoLoad failed");
                return false;
            }
            long l2 = new GlobalMembershipCommands.AuthenticateTransaction.Response((TACommandResponse)tACommandResponse).zo.auth_result.get();
            long l3 = l2 LCMP 0L;
            bl = false;
            if (l3 == false) {
                bl = true;
            }
            if (!DEBUG) return bl;
            Log.d((String)"SpayFw_GMTAController", (String)("authenticateTransaction: auth_result = " + l2));
            return bl;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new GlobalMembershipTAException("Error communicating with the TA", 1001);
            throw new GlobalMembershipTAException("Invalid Input", 1004);
        }
    }

    public void b(String string, byte[] arrby, byte[] arrby2, byte[] arrby3) {
        a a2 = new a();
        a2.zz = arrby;
        a2.zA = arrby2;
        a2.zB = arrby3;
        mServerCertsHash.put((Object)string, (Object)a2);
    }

    public boolean clearMstData() {
        if (DEBUG) {
            Log.d((String)"SpayFw_GMTAController", (String)"Calling clearMstData");
        }
        this.resetMstFlag();
        if (!this.isTALoaded()) {
            Log.e((String)"SpayFw_GMTAController", (String)"clearMstData: Error: TA is not loaded, please call loadTA() API first!");
            throw new GlobalMembershipTAException("Global Membership TA not loaded", 1003);
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.a f(byte[] arrby, byte[] arrby2) {
        c c2 = this;
        synchronized (c2) {
            block18 : {
                if (DEBUG) {
                    Log.d((String)"SpayFw_GMTAController", (String)"Calling extractGlobalMembershipCardDetail");
                }
                com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.a a2 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.a();
                a2.setErrorCode(-1);
                long l2 = -1L;
                try {
                    TACommandResponse tACommandResponse = this.executeNoLoad(new GlobalMembershipCommands.ExtractGlobalMembershipCardDetail.Request(arrby, arrby2));
                    if (tACommandResponse == null) {
                        Log.e((String)"SpayFw_GMTAController", (String)"Error: extractGlobalMembershipCardDetail executeNoLoad failed");
                    } else {
                        GlobalMembershipCommands.ExtractGlobalMembershipCardDetail.Response response = new GlobalMembershipCommands.ExtractGlobalMembershipCardDetail.Response(tACommandResponse);
                        if (response != null && response.zq != null && response.zq.return_code != null) {
                            l2 = response.zq.return_code.get();
                        }
                        if (l2 == 0L) {
                            if (DEBUG) {
                                Log.d((String)"SpayFw_GMTAController", (String)"extractGlobalMembershipCardDetail Success: ");
                            }
                            if (response.zq.cardnumber != null) {
                                a2.setCardnumber(new String(response.zq.cardnumber.getData(), "UTF-8"));
                            }
                            if (response.zq.pin != null) {
                                a2.setPin(new String(response.zq.pin.getData(), "UTF-8"));
                            }
                            if (response.zq.barcodecontent != null) {
                                a2.setBarcodeContent(new String(response.zq.barcodecontent.getData(), "UTF-8"));
                            }
                            if (response.zq.zr != null) {
                                a2.setBarcodeType(new String(response.zq.zr.getData(), "UTF-8"));
                            }
                            if (response.zq.zt != null) {
                                a2.setNumericValue(new String(response.zq.zt.getData(), "UTF-8"));
                            }
                            a2.setErrorCode(0);
                        } else {
                            Log.e((String)"SpayFw_GMTAController", (String)("extractGlobalMembershipCardDetail Fail: respValue = " + l2));
                        }
                    }
                    return a2;
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    if (!(exception instanceof IllegalArgumentException)) break block18;
                    throw new GlobalMembershipTAException("Invalid Input", 1004);
                }
            }
            throw new GlobalMembershipTAException("Error communicating with the TA", 1001);
        }
    }

    public byte[] getNonce(int n2) {
        if (DEBUG) {
            Log.d((String)"SpayFw_GMTAController", (String)"Calling getNonce");
        }
        if (!this.isTALoaded()) {
            Log.e((String)"SpayFw_GMTAController", (String)"getNonce: Error: TA is not loaded, please call loadTA() API first!");
        }
        return null;
    }

    @Override
    protected boolean init() {
        if (!super.init()) {
            Log.e((String)"SpayFw_GMTAController", (String)"Error: init failed");
            return false;
        }
        this.zy = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.b(this);
        return true;
    }

    public boolean mstTransmit(byte[] arrby, int n2, byte[] arrby2) {
        c c2 = this;
        synchronized (c2) {
            if (DEBUG) {
                com.samsung.android.spayfw.b.c.d("SpayFw_GMTAController", "Calling mstTransmit");
            }
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] utility_enc4Server_Transport(String string, byte[] arrby, long l2) {
        byte[] arrby2 = null;
        if (DEBUG) {
            Log.d((String)"SpayFw_GMTAController", (String)"Calling utility_enc4Server_Transport");
        }
        if (!this.isTALoaded()) {
            Log.e((String)"SpayFw_GMTAController", (String)"utility_enc4Server_Transport: Error: TA is not loaded, please call loadTA() API first!");
            return arrby2;
        }
        if (!this.loadAllCerts(string)) {
            Log.e((String)"SpayFw_GMTAController", (String)"utility_enc4Server_Transport: Error: loadAllCerts returned false!");
            return null;
        }
        byte[] arrby3 = c.getCurrentTimestamp(l2);
        if (arrby3 != null && DEBUG) {
            Log.d((String)"SpayFw_GMTAController", (String)("Utility_enc4Server_Transport: timestamp_bytes = " + Arrays.toString((byte[])arrby3)));
        }
        try {
            TACommandResponse tACommandResponse = this.executeNoLoad(new GlobalMembershipCommands.Utility_enc4Server_Transport.Request(arrby, arrby3));
            if (tACommandResponse == null) {
                Log.e((String)"SpayFw_GMTAController", (String)"Utility_enc4Server_Transport: Error: executeNoLoad failed");
                return null;
            }
            GlobalMembershipCommands.Utility_enc4Server_Transport.Response response = new GlobalMembershipCommands.Utility_enc4Server_Transport.Response(tACommandResponse);
            if (DEBUG) {
                Log.d((String)"SpayFw_GMTAController", (String)"Utility_enc4Server_Transport called Successfully");
            }
            if ((arrby2 = response.zv.resp.getData()) == null || !DEBUG) return arrby2;
            Log.d((String)"SpayFw_GMTAController", (String)("utility_enc4Server_Transport: encryptedData length = " + arrby2.length));
            return arrby2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new GlobalMembershipTAException("Error communicating with the TA", 1001);
            throw new GlobalMembershipTAException("Invalid Input", 1004);
        }
    }

    private static class a {
        private byte[] zA = null;
        private byte[] zB = null;
        private byte[] zz = null;

        private a() {
        }
    }

    public static class b {
        public byte[] drk;
        public byte[] encryptcert;
        public byte[] signcert;

        b(byte[] arrby, byte[] arrby2, byte[] arrby3) {
            this.drk = arrby;
            this.signcert = arrby2;
            this.encryptcert = arrby3;
        }
    }

}

