package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import android.content.Context;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.Close;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.ComputeAC;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.GetSignatureData;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.Init;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.InitializeSecureChannel;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.LCM;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.LoadCerts.Request;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.LoadCerts.Response;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.Open;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.PersoToken;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.ProcessData;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.ProcessDataJwsJwe;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.ProcessDataJwsJwe.JsonOperation;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.ProcessRequestData;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.ProcessResponseData;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.ReqMST;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.Sign;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.TransmitMstData;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.UnWrap;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.Update;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.UpdateSecureChannel;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.Verify;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.Wrap;
import java.util.List;
import java.util.Map;

/* renamed from: com.samsung.android.spayfw.payprovider.amexv2.tzsvc.c */
public class AmexTAController extends SPayTAAuthController implements IAmexTAController {
    private static AmexTAController rO;
    private static String rP;
    private static AmexTAInfo rT;
    private AmexDeviceCerts rQ;
    private AmexTAController rR;
    private AmexTAContextManager rS;

    /* renamed from: com.samsung.android.spayfw.payprovider.amexv2.tzsvc.c.a */
    public static class AmexTAController {
        public String deviceCertificate;
        public String deviceEncryptionCertificate;
        public String deviceSigningCertificate;
    }

    /* renamed from: com.samsung.android.spayfw.payprovider.amexv2.tzsvc.c.b */
    public static class AmexTAController {
        public String merchantCertificate;
        public Map<String, String> txnAttributes;
    }

    /* renamed from: com.samsung.android.spayfw.payprovider.amexv2.tzsvc.c.c */
    public static class AmexTAController {
        public String encryptedRequestData;
        public String encryptionParams;
        public String requestDataSignature;
    }

    private AmexTAController(Context context) {
        super(context, rT);
        this.rQ = null;
        this.rR = null;
        this.rS = null;
    }

    protected boolean init() {
        if (super.init()) {
            this.rQ = new AmexDeviceCerts(this);
            this.rS = new AmexTAContextManager(this);
            return true;
        }
        Log.e("SPAY:AmexTAController", "Error: init failed");
        return false;
    }

    public static synchronized AmexTAController m810D(Context context) {
        AmexTAController amexTAController;
        synchronized (AmexTAController.class) {
            if (rO == null) {
                rO = new AmexTAController(context);
            }
            amexTAController = rO;
        }
        return amexTAController;
    }

    public static synchronized AmexTAController cz() {
        AmexTAController amexTAController;
        synchronized (AmexTAController.class) {
            amexTAController = rO;
        }
        return amexTAController;
    }

    static {
        rP = null;
        rT = new AmexTAInfo();
    }

    public synchronized boolean loadTA() {
        return super.loadTA();
    }

    public synchronized void unloadTA() {
        this.rR = null;
        super.unloadTA();
    }

    private boolean isSecuritySetupInitialized() {
        if (this.rR == null) {
            return false;
        }
        if (DEBUG) {
            Log.d("SPAY:AmexTAController", "Device Certs already loaded)");
        }
        return true;
    }

    public boolean initializeSecuritySetup() {
        if (isSecuritySetupInitialized()) {
            return true;
        }
        if (DEBUG) {
            Log.d("SPAY:AmexTAController", "Calling initializeSecuritySetup");
        }
        if (isTALoaded()) {
            if (!this.rQ.isLoaded()) {
                Log.d("SPAY:AmexTAController", "mAmexDeviceCerts is not loaded");
                if (!this.rQ.load()) {
                    Log.e("SPAY:AmexTAController", "Error: Amex Device Certs Load failed");
                    return false;
                }
            }
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new Request(this.rQ.getDevicePrivateSignCert(), this.rQ.getDevicePrivateEncryptionCert()));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.e("SPAY:AmexTAController", "loadAllCerts: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                Response response = new Response(executeNoLoad);
                this.rR = new AmexTAController();
                this.rR.deviceCertificate = Utils.convertToPem(response.rf.deviceRootRSA2048PubCert.getData(), false);
                this.rR.deviceSigningCertificate = Utils.convertToPem(response.rf.deviceSignRSA2048PubCert.getData(), false);
                this.rR.deviceEncryptionCertificate = Utils.convertToPem(response.rf.pR.getData(), false);
                if (!DEBUG) {
                    return true;
                }
                Log.d("SPAY:AmexTAController", "initializeSecuritySetup called Successfully");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        Log.e("SPAY:AmexTAController", "initializeSecuritySetup: Error: TA is not loaded, please call loadTA() API first!");
        return false;
    }

    public synchronized AmexTAController cA() {
        if (initializeSecuritySetup()) {
        } else {
            Log.e("SPAY:AmexTAController", "initializeSecuritySetup failed");
            throw new AmexTAException(-1);
        }
        return this.rR;
    }

    public synchronized AmexTAController m817c(String str, String str2, String str3) {
        AmexTAController amexTAController;
        byte[] bArr = null;
        synchronized (this) {
            if (DEBUG) {
                Log.d("SPAY:AmexTAController", "Calling processRequestData");
            }
            if (initializeSecuritySetup()) {
                try {
                    byte[] bArr2;
                    List derChain = Utils.getDerChain(str);
                    if (str2 == null) {
                        bArr2 = null;
                    } else {
                        bArr2 = str2.getBytes();
                    }
                    if (str3 != null) {
                        bArr = str3.getBytes();
                    }
                    TACommandResponse executeNoLoad = executeNoLoad(new ProcessRequestData.Request(derChain, bArr2, bArr));
                    if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                        Log.e("SPAY:AmexTAController", "processRequestData: Error: executeNoLoad failed");
                        throw new AmexTAException(-1);
                    }
                    ProcessRequestData.Response response = new ProcessRequestData.Response(executeNoLoad);
                    int i = (int) response.rs.return_code.get();
                    if (i != 0) {
                        Log.e("SPAY:AmexTAController", "ProcessRequestData Call Failed");
                        throw new AmexTAException(i);
                    }
                    if (DEBUG) {
                        Log.d("SPAY:AmexTAController", "processRequestData called Successfully");
                    }
                    amexTAController = new AmexTAController();
                    amexTAController.encryptedRequestData = Utils.toBase64(response.rs.pW.getData());
                    amexTAController.encryptionParams = Utils.toBase64(response.rs.pX.getData());
                    amexTAController.requestDataSignature = Utils.toBase64(response.rs.pY.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AmexTAException(-1);
                }
            }
            Log.e("SPAY:AmexTAController", "initializeSecuritySetup failed");
            throw new AmexTAException(-1);
        }
        return amexTAController;
    }

    public String m812a(String str, boolean z) {
        byte[] bytes = z ? str == null ? null : str.getBytes() : Utils.fromBase64(str);
        byte[] a = m815a(bytes, z);
        return z ? Utils.toBase64(a) : new String(a);
    }

    public synchronized byte[] m815a(byte[] bArr, boolean z) {
        byte[] data;
        if (DEBUG) {
            Log.d("SPAY:AmexTAController", "Calling processData");
        }
        if (initializeSecuritySetup()) {
            TACommandRequest request;
            if (z) {
                try {
                    request = new ProcessData.Request(bArr, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AmexTAException(-1);
                }
            }
            request = new ProcessData.Request(bArr, false);
            TACommandResponse executeNoLoad = executeNoLoad(request);
            if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                Log.e("SPAY:AmexTAController", "processData: Error: executeNoLoad failed");
                throw new AmexTAException(-1);
            }
            ProcessData.Response response = new ProcessData.Response(executeNoLoad);
            int i = (int) response.rl.return_code.get();
            if (i != 0) {
                Log.e("SPAY:AmexTAController", "ProcessData Call Failed");
                throw new AmexTAException(i);
            }
            if (DEBUG) {
                Log.d("SPAY:AmexTAController", "processData called Successfully");
            }
            if (z) {
                data = response.rl.data.getData();
            } else {
                data = response.rl.data.getData();
            }
        } else {
            Log.e("SPAY:AmexTAController", "initializeSecuritySetup failed");
            throw new AmexTAException(-1);
        }
        return data;
    }

    public synchronized byte[] m814a(JsonOperation jsonOperation, byte[] bArr, byte[] bArr2, String str) {
        ProcessData.Response response;
        if (DEBUG) {
            Log.d("SPAY:AmexTAController", "Calling processDataJwsJwe");
        }
        if (initializeSecuritySetup()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new ProcessDataJwsJwe.Request(jsonOperation, bArr, bArr2, Utils.getDerChain(str)));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.e("SPAY:AmexTAController", "processData: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                response = new ProcessData.Response(executeNoLoad);
                int i = (int) response.rl.return_code.get();
                if (i != 0) {
                    Log.e("SPAY:AmexTAController", "ProcessDataJwsJwe Call Failed");
                    throw new AmexTAException(i);
                }
                if (DEBUG) {
                    Log.d("SPAY:AmexTAController", "ProcessDataJwsJwe called Successfully");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(-1);
            }
        }
        Log.e("SPAY:AmexTAController", "initializeSecuritySetup failed");
        throw new AmexTAException(-1);
        return response.rl.data.getData();
    }

    public synchronized String m818o(String str, String str2) {
        ProcessResponseData.Response response;
        if (DEBUG) {
            Log.d("SPAY:AmexTAController", "Calling processRequestData");
        }
        if (initializeSecuritySetup()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new ProcessResponseData.Request(Utils.fromBase64(str), Utils.fromBase64(str2)));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.e("SPAY:AmexTAController", "processRequestData: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                response = new ProcessResponseData.Response(executeNoLoad);
                int i = (int) response.rt.return_code.get();
                if (i != 0) {
                    Log.e("SPAY:AmexTAController", "ProcessResponseData Call Failed");
                    throw new AmexTAException(i);
                }
                if (DEBUG) {
                    Log.d("SPAY:AmexTAController", "processRequestData called Successfully");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(-1);
            }
        }
        Log.e("SPAY:AmexTAController", "initializeSecuritySetup failed");
        throw new AmexTAException(-1);
        return Utils.toBase64(response.rt.qb.getData());
    }

    public synchronized int open(byte[] bArr) {
        return m811a(bArr, null, null);
    }

    public synchronized int m811a(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        byte[] bArr4 = null;
        int i = -1;
        synchronized (this) {
            try {
                String str;
                byte[][] llVarToBytes = LLVARByteUtil.llVarToBytes(bArr);
                if (llVarToBytes == null || llVarToBytes[0] == null) {
                    Log.d("SPAY:AmexTAController", "byteArray or byteArray[0] is null");
                    str = null;
                } else {
                    str = new String(llVarToBytes[0]);
                }
                if (initializeSecuritySetup()) {
                    String aD = this.rS.aD(str);
                    if (aD != null) {
                        bArr4 = aD.getBytes();
                    }
                    TACommandResponse executeNoLoad = executeNoLoad(new Open.Request(bArr4, bArr2));
                    if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                        Log.e("SPAY:AmexTAController", "Open: Error: executeNoLoad failed");
                    } else {
                        Open.Response response = new Open.Response(executeNoLoad);
                        int i2 = (int) response.rg.return_code.get();
                        if (i2 != 0) {
                            Log.e("SPAY:AmexTAController", "Open Call Failed");
                            i = i2;
                        } else {
                            if (DEBUG) {
                                Log.d("SPAY:AmexTAController", "Open called Successfully");
                            }
                            rP = str;
                            if (bArr2 != null) {
                                Object data = response.rg.qN.getData();
                                if (DEBUG) {
                                    Log.d("SPAY:AmexTAController", "unwrappedBuffer.length  = " + bArr3.length);
                                    Log.d("SPAY:AmexTAController", "tmpUnwrappedBuffer.length  = " + data.length);
                                }
                                if (data == null || bArr3 == null) {
                                    Log.e("SPAY:AmexTAController", "Error: tmpUnwrappedBuffer == null || unwrappedBuffer == null");
                                    i = -6;
                                } else if (bArr3.length < data.length) {
                                    if (DEBUG) {
                                        Log.d("SPAY:AmexTAController", "Require new buffer: size = " + (data.length + HCEClientConstants.SC_DEST_BUFFER_COMPUTATION_CONSTANT));
                                    }
                                    i = data.length + HCEClientConstants.SC_DEST_BUFFER_COMPUTATION_CONSTANT;
                                } else {
                                    System.arraycopy(data, 0, bArr3, 0, data.length);
                                    i = data.length;
                                }
                            } else {
                                i = 0;
                            }
                        }
                    }
                } else {
                    Log.e("SPAY:AmexTAController", "initializeSecuritySetup failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public synchronized int close(byte[] bArr) {
        return m816b(bArr, null, null);
    }

    public synchronized int m816b(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        int i = -4;
        synchronized (this) {
            try {
                byte[][] llVarToBytes = LLVARByteUtil.llVarToBytes(bArr);
                if (llVarToBytes == null || llVarToBytes[0] == null) {
                    Log.d("SPAY:AmexTAController", "byteArray[0] is null");
                } else {
                    String str = new String(llVarToBytes[0]);
                    if (!str.equalsIgnoreCase(rP)) {
                        Log.e("SPAY:AmexTAController", "Error: Token being closed " + str + " do not match with the active token " + rP);
                    } else if (rP == null) {
                        Log.e("SPAY:AmexTAController", "Error: No Active Token to be closed. This must never happen");
                        i = -6;
                    } else {
                        TACommandResponse executeNoLoad = executeNoLoad(new Close.Request(bArr2));
                        if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                            Log.e("SPAY:AmexTAController", "Close: Error: executeNoLoad failed");
                            throw new AmexTAException(-1);
                        }
                        Close.Response response = new Close.Response(executeNoLoad);
                        i = (int) response.qO.return_code.get();
                        if (i != 0) {
                            Log.e("SPAY:AmexTAController", "Close Call Failed");
                            throw new AmexTAException(i);
                        }
                        if (DEBUG) {
                            Log.d("SPAY:AmexTAController", "Close called Successfully");
                        }
                        this.rS.m808p(rP, new String(response.qO.qP.getData()));
                        rP = null;
                        if (bArr2 != null) {
                            Object data = response.qO.qQ.getData();
                            if (DEBUG) {
                                Log.d("SPAY:AmexTAController", "wrappedBuffer.length  = " + bArr3.length);
                                Log.d("SPAY:AmexTAController", "tmpWrappedBuffer.length  = " + data.length);
                            }
                            if (data == null || bArr3 == null) {
                                Log.e("SPAY:AmexTAController", "Error: tmpWrappedBuffer == null || wrappedBuffer == null");
                                i = -6;
                            } else if (bArr3.length < data.length) {
                                if (DEBUG) {
                                    Log.d("SPAY:AmexTAController", "Require new buffer: size = " + (data.length + HCEClientConstants.SC_DEST_BUFFER_COMPUTATION_CONSTANT));
                                }
                                i = data.length + HCEClientConstants.SC_DEST_BUFFER_COMPUTATION_CONSTANT;
                            } else {
                                System.arraycopy(data, 0, bArr3, 0, data.length);
                                i = data.length;
                            }
                        } else {
                            i = 0;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                i = -1;
            }
        }
        return i;
    }

    public synchronized int unwrap(int i, byte[] bArr, int i2, byte[] bArr2, int i3) {
        int i4;
        boolean z = false;
        synchronized (this) {
            if (bArr == null || bArr2 == null) {
                Log.e("SPAY:AmexTAController", "Error: Invalid Params");
                i4 = -4;
            } else {
                Log.e("SPAY:AmexTAController", "UnWrap: mode : " + i);
                if (i == 0) {
                    z = true;
                }
                try {
                    TACommandResponse executeNoLoad = executeNoLoad(new UnWrap.Request(bArr, z));
                    if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                        Log.e("SPAY:AmexTAController", "UnWrap: Error: executeNoLoad failed");
                        i4 = -1;
                    } else {
                        UnWrap.Response response = new UnWrap.Response(executeNoLoad);
                        i4 = (int) response.rD.return_code.get();
                        if (i4 != 0) {
                            Log.e("SPAY:AmexTAController", "UnWrap Call Failed");
                        } else {
                            if (DEBUG) {
                                Log.d("SPAY:AmexTAController", "UnWrap called Successfully");
                            }
                            Object data = response.rD.qN.getData();
                            if (DEBUG) {
                                Log.d("SPAY:AmexTAController", "unwrappedBuffer.length  = " + bArr2.length);
                                Log.d("SPAY:AmexTAController", "tmpUnwrappedBuffer.length  = " + data.length);
                            }
                            if (data == null || bArr2 == null) {
                                Log.e("SPAY:AmexTAController", "Error: tmpUnwrappedBuffer == null || unwrappedBuffer == null");
                                i4 = -6;
                            } else if (bArr2.length < data.length) {
                                if (DEBUG) {
                                    Log.d("SPAY:AmexTAController", "Require new buffer: size = " + (data.length + HCEClientConstants.SC_DEST_BUFFER_COMPUTATION_CONSTANT));
                                }
                                i4 = data.length + HCEClientConstants.SC_DEST_BUFFER_COMPUTATION_CONSTANT;
                            } else {
                                System.arraycopy(data, 0, bArr2, 0, data.length);
                                i4 = data.length;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    i4 = -1;
                }
            }
        }
        return i4;
    }

    public synchronized int wrap(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3;
        if (bArr == null || bArr2 == null) {
            Log.e("SPAY:AmexTAController", "Error: Invalid Params");
            i3 = -4;
        } else {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new Wrap.Request(bArr));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.e("SPAY:AmexTAController", "Wrap: Error: executeNoLoad failed");
                    i3 = -1;
                } else {
                    Wrap.Response response = new Wrap.Response(executeNoLoad);
                    i3 = (int) response.rL.return_code.get();
                    if (i3 != 0) {
                        Log.e("SPAY:AmexTAController", "Wrap Call Failed");
                    } else {
                        if (DEBUG) {
                            Log.d("SPAY:AmexTAController", "Wrap called Successfully");
                        }
                        Object data = response.rL.qQ.getData();
                        if (DEBUG) {
                            Log.d("SPAY:AmexTAController", "wrappedBuffer.length  = " + bArr2.length);
                            Log.d("SPAY:AmexTAController", "tmpWrappedBuffer.length  = " + data.length);
                        }
                        if (data == null || bArr2 == null) {
                            Log.e("SPAY:AmexTAController", "Error: tmpUnwrappedBuffer == null || unwrappedBuffer == null");
                            i3 = -6;
                        } else if (bArr2.length < data.length) {
                            if (DEBUG) {
                                Log.d("SPAY:AmexTAController", "Require new buffer: size = " + (data.length + HCEClientConstants.SC_DEST_BUFFER_COMPUTATION_CONSTANT));
                            }
                            i3 = data.length + HCEClientConstants.SC_DEST_BUFFER_COMPUTATION_CONSTANT;
                        } else {
                            System.arraycopy(data, 0, bArr2, 0, data.length);
                            i3 = data.length;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                i3 = -1;
            }
        }
        return i3;
    }

    public synchronized int initializeSecureChannel(byte[] bArr, byte[] bArr2) {
        int i;
        try {
            TACommandResponse executeNoLoad = executeNoLoad(new InitializeSecureChannel.Request(bArr));
            if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                Log.e("SPAY:AmexTAController", "initializeSecureChannel: Error: executeNoLoad failed");
                i = -1;
            } else {
                InitializeSecureChannel.Response response = new InitializeSecureChannel.Response(executeNoLoad);
                i = (int) response.rb.return_code.get();
                if (i != 0) {
                    Log.e("SPAY:AmexTAController", "initializeSecureChannel Call Failed");
                } else {
                    if (DEBUG) {
                        Log.d("SPAY:AmexTAController", "initializeSecureChannel called Successfully");
                    }
                    Object data = response.rb.rc.getData();
                    if (DEBUG) {
                        Log.d("SPAY:AmexTAController", "ephemeralBuffer.length  = " + bArr2.length);
                        Log.d("SPAY:AmexTAController", "tmpEphemeralBuffer.length  = " + data.length);
                    }
                    if (data == null || bArr2 == null) {
                        Log.e("SPAY:AmexTAController", "Error: tmpEphemeralBuffer == null || ephemeralBuffer == null");
                        i = -6;
                    } else if (bArr2.length < data.length) {
                        if (DEBUG) {
                            Log.d("SPAY:AmexTAController", "Require new buffer: size = " + (data.length + HCEClientConstants.SC_DEST_BUFFER_COMPUTATION_CONSTANT));
                        }
                        i = data.length + HCEClientConstants.SC_DEST_BUFFER_COMPUTATION_CONSTANT;
                    } else {
                        System.arraycopy(data, 0, bArr2, 0, data.length);
                        i = data.length;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            i = -1;
        }
        return i;
    }

    public synchronized int updateSecureChannel(byte[] bArr, byte[] bArr2) {
        int i = 0;
        synchronized (this) {
            Object obj = null;
            try {
                TACommandResponse executeNoLoad = AmexTAController.cz().executeNoLoad(new UpdateSecureChannel.Request(bArr));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.e("SPAY:AmexTAController", "updateSecureChannel: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                UpdateSecureChannel.Response response = new UpdateSecureChannel.Response(executeNoLoad);
                int i2 = (int) response.rH.return_code.get();
                if (i2 != 0) {
                    Log.e("SPAY:AmexTAController", "updateSecureChannel Call Failed");
                    throw new AmexTAException(i2);
                }
                if (DEBUG) {
                    Log.d("SPAY:AmexTAController", "updateSecureChannel called Successfully");
                }
                if (obj != null) {
                    Log.d("SPAY:AmexTAController", "hmacBuffer != null");
                    Object data = response.rH.rI.getData();
                    if (DEBUG) {
                        Log.d("SPAY:AmexTAController", "hmacBuffer.length  = " + obj.length);
                        Log.d("SPAY:AmexTAController", "tmpHmacBuffer.length  = " + data.length);
                    }
                    if (data == null || obj == null) {
                        Log.e("SPAY:AmexTAController", "Error: tmpHmacBuffer == null || hmacBuffer == null");
                        i = -6;
                    } else if (obj.length < data.length) {
                        if (DEBUG) {
                            Log.d("SPAY:AmexTAController", "Require new buffer: size = " + (data.length + HCEClientConstants.SC_DEST_BUFFER_COMPUTATION_CONSTANT));
                        }
                        i = data.length + HCEClientConstants.SC_DEST_BUFFER_COMPUTATION_CONSTANT;
                    } else {
                        System.arraycopy(data, 0, obj, 0, data.length);
                        i = data.length;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                i = -1;
            }
        }
        return i;
    }

    public synchronized int init(byte[] bArr) {
        int i = -1;
        synchronized (this) {
            try {
                TACommandResponse executeNoLoad = AmexTAController.cz().executeNoLoad(new Init.Request(bArr));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.e("SPAY:AmexTAController", "Init: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                int i2 = (int) new Init.Response(executeNoLoad).qZ.return_code.get();
                if (i2 != 0) {
                    Log.e("SPAY:AmexTAController", "Init Call Failed");
                    throw new AmexTAException(i2);
                }
                if (DEBUG) {
                    Log.d("SPAY:AmexTAController", "Init called Successfully");
                }
                i = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public synchronized int perso(int i, byte[] bArr, byte[] bArr2) {
        int i2 = -1;
        synchronized (this) {
            try {
                AmexTAController cz = AmexTAController.cz();
                PersoToken.Request request = new PersoToken.Request(i, bArr, bArr2);
                if (DEBUG) {
                    Log.d("SPAY:AmexTAController", "lockcode = " + Utils.encodeHex(bArr2));
                }
                TACommandResponse executeNoLoad = cz.executeNoLoad(request);
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.e("SPAY:AmexTAController", "perso: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                int i3 = (int) new PersoToken.Response(executeNoLoad).rk.return_code.get();
                if (i3 != 0) {
                    Log.e("SPAY:AmexTAController", "perso Call Failed");
                    throw new AmexTAException(i3);
                }
                if (DEBUG) {
                    Log.d("SPAY:AmexTAController", "perso called Successfully");
                }
                i2 = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i2;
    }

    public synchronized int update(byte[] bArr) {
        int i = -1;
        synchronized (this) {
            try {
                TACommandResponse executeNoLoad = AmexTAController.cz().executeNoLoad(new Update.Request(bArr));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.e("SPAY:AmexTAController", "update: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                if (((int) new Update.Response(executeNoLoad).rF.return_code.get()) != 0) {
                    Log.e("SPAY:AmexTAController", "update Call Failed");
                    i = -6;
                } else {
                    if (DEBUG) {
                        Log.d("SPAY:AmexTAController", "update called Successfully");
                    }
                    i = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public synchronized int getSignatureData(byte[] bArr, byte[] bArr2) {
        int i;
        try {
            TACommandResponse executeNoLoad = executeNoLoad(new GetSignatureData.Request(bArr));
            if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                Log.e("SPAY:AmexTAController", "GetSignatureData: Error: executeNoLoad failed");
                i = -1;
            } else {
                GetSignatureData.Response response = new GetSignatureData.Response(executeNoLoad);
                i = (int) response.qW.return_code.get();
                if (i != 0) {
                    Log.e("SPAY:AmexTAController", "GetSignatureData Call Failed");
                } else {
                    if (DEBUG) {
                        Log.d("SPAY:AmexTAController", "GetSignatureData called Successfully");
                    }
                    Object data = response.qW.qX.getData();
                    if (DEBUG) {
                        Log.d("SPAY:AmexTAController", "sigBuffer.length  = " + bArr2.length);
                        Log.d("SPAY:AmexTAController", "tmpSigBuffer.length  = " + data.length);
                    }
                    if (bArr2 == null || data == null) {
                        Log.e("SPAY:AmexTAController", "Error: sigBuffer == null || tmpSigBuffer == null");
                        i = -6;
                    } else if (bArr2.length < data.length) {
                        if (DEBUG) {
                            Log.d("SPAY:AmexTAController", "Require new buffer: size = " + (data.length + HCEClientConstants.SC_DEST_BUFFER_COMPUTATION_CONSTANT));
                        }
                        i = data.length + HCEClientConstants.SC_DEST_BUFFER_COMPUTATION_CONSTANT;
                    } else {
                        System.arraycopy(data, 0, bArr2, 0, data.length);
                        i = data.length;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            i = -1;
        }
        return i;
    }

    public synchronized int lcm(int i) {
        int i2 = -1;
        synchronized (this) {
            try {
                TACommandResponse executeNoLoad = AmexTAController.cz().executeNoLoad(new LCM.Request(i));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.e("SPAY:AmexTAController", "lcm: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                int i3 = (int) new LCM.Response(executeNoLoad).re.return_code.get();
                if (i3 != 0) {
                    Log.e("SPAY:AmexTAController", "lcm Call Failed");
                    throw new AmexTAException(i3);
                }
                if (DEBUG) {
                    Log.d("SPAY:AmexTAController", "lcm called Successfully");
                }
                i2 = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i2;
    }

    public synchronized int computeAC(byte[] bArr, byte[] bArr2) {
        int i = -1;
        synchronized (this) {
            try {
                TACommandResponse executeNoLoad = AmexTAController.cz().executeNoLoad(new ComputeAC.Request(bArr));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.e("SPAY:AmexTAController", "computeAC: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                ComputeAC.Response response = new ComputeAC.Response(executeNoLoad);
                int i2 = (int) response.qS.return_code.get();
                if (i2 != 0) {
                    Log.e("SPAY:AmexTAController", "Compute AC call failed");
                    throw new AmexTAException(i2);
                }
                if (DEBUG) {
                    Log.d("SPAY:AmexTAController", "computeAC called Successfully");
                    Log.d("SPAY:AmexTAController", "respValue = " + i2);
                }
                Object data = response.qS.pP.getData();
                System.arraycopy(data, 0, bArr2, 0, data.length);
                i = response.qS.qT.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public synchronized int sign(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        int i = -1;
        synchronized (this) {
            try {
                AmexTAController cz = AmexTAController.cz();
                if (DEBUG) {
                    Log.d("SPAY:AmexTAController", "lockcode = " + Utils.encodeHex(bArr3));
                }
                TACommandResponse executeNoLoad = cz.executeNoLoad(new Sign.Request(bArr, bArr3));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.e("SPAY:AmexTAController", "sign: Error: executeNoLoad failed");
                    throw new AmexTAException(-1);
                }
                Sign.Response response = new Sign.Response(executeNoLoad);
                int i2 = (int) response.ry.return_code.get();
                if (i2 != 0) {
                    Log.e("SPAY:AmexTAController", "sign Call Failed");
                    throw new AmexTAException(i2);
                }
                if (DEBUG) {
                    Log.d("SPAY:AmexTAController", "sign called Successfully");
                }
                Object data = response.ry.rz.getData();
                if (DEBUG) {
                    Log.d("SPAY:AmexTAController", "signatureData = " + Utils.encodeHex(data));
                }
                System.arraycopy(data, 0, bArr2, 0, data.length);
                i = response.ry.rA.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public synchronized int reqMST(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        int i = -1;
        synchronized (this) {
            try {
                AmexTAController cz = AmexTAController.cz();
                if (bArr == null || bArr.length == 0 || bArr2 == null || bArr2.length == 0) {
                    Log.e("SPAY:AmexTAController", "reqMST: Error: Invalid track data");
                    throw new AmexTAException(-4);
                }
                if (bArr3 != null) {
                    if (bArr3.length != 0) {
                        TACommandResponse executeNoLoad = cz.executeNoLoad(new ReqMST.Request(bArr, bArr2));
                        if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                            Log.e("SPAY:AmexTAController", "reqMST: Error: executeNoLoad failed");
                            throw new AmexTAException(-1);
                        }
                        ReqMST.Response response = new ReqMST.Response(executeNoLoad);
                        int i2 = (int) response.ru.return_code.get();
                        if (i2 != 0) {
                            Log.e("SPAY:AmexTAController", "ReqMST call failed");
                            throw new AmexTAException(i2);
                        }
                        if (DEBUG) {
                            Log.d("SPAY:AmexTAController", "reqMST called Successfully");
                        }
                        Object data = response.ru.rv.getData();
                        if (data.length <= bArr3.length) {
                            System.arraycopy(data, 0, bArr3, 0, data.length);
                            i = response.ru.qT.get();
                        } else {
                            Log.e("SPAY:AmexTAController", "reqMST: Error: TID buffer too big");
                            throw new AmexTAException(-3);
                        }
                    }
                }
                Log.e("SPAY:AmexTAController", "reqMST: Error: Invalid tid buffer");
                throw new AmexTAException(-3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public boolean m813a(int i, byte[] bArr) {
        if (DEBUG) {
            Log.d("SPAY:AmexTAController", "Calling transmitMstData");
        }
        boolean initializeSecuritySetup = initializeSecuritySetup();
        if (initializeSecuritySetup) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new TransmitMstData.Request(i, bArr));
                if (executeNoLoad == null) {
                    Log.e("SPAY:AmexTAController", "Error: transmitMstData executeNoLoad failed");
                    throw new AmexTAException(-1);
                } else if (executeNoLoad.mResponseCode != 0) {
                    throw new AmexTAException(executeNoLoad.mResponseCode);
                } else {
                    if (new TransmitMstData.Response(executeNoLoad).rB.return_code.get() == 0) {
                        initializeSecuritySetup = true;
                    }
                    if (DEBUG) {
                        Log.d("SPAY:AmexTAController", "TransmitMstData: ret = " + initializeSecuritySetup);
                    }
                    return initializeSecuritySetup;
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(-1);
            }
        }
        Log.e("SPAY:AmexTAController", "initializeSecuritySetup failed");
        throw new AmexTAException(-1);
    }

    public synchronized int verify(byte[] bArr, byte[] bArr2) {
        int i;
        try {
            TACommandResponse executeNoLoad = AmexTAController.cz().executeNoLoad(new Verify.Request(bArr));
            if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                Log.e("SPAY:AmexTAController", "Verify: Error: executeNoLoad failed");
                throw new AmexTAException(-1);
            }
            Verify.Response response = new Verify.Response(executeNoLoad);
            i = (int) response.rJ.return_code.get();
            if (DEBUG) {
                Log.d("SPAY:AmexTAController", "Verify called Successfully");
                Log.d("SPAY:AmexTAController", "respValue = " + i);
            }
            bArr2[0] = response.rJ.rK.getData()[0];
        } catch (Exception e) {
            e.printStackTrace();
            i = -1;
        }
        return i;
    }

    public synchronized void m819q(String str, String str2) {
        this.rS.m809q(str, str2);
    }

    public synchronized void aC(String str) {
        this.rS.aC(str);
    }
}
