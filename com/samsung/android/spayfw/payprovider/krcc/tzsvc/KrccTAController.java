package com.samsung.android.spayfw.payprovider.krcc.tzsvc;

import android.content.Context;
import android.spay.TACommandResponse;
import android.util.Log;
import com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.ClearMstData;
import com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.LoadCert.Request;
import com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.LoadCert.Response;
import com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.PrepareMstData;
import com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.TransmitMstData;
import com.samsung.android.spaytui.SPayTUIException;
import com.samsung.android.spaytzsvc.api.TAController;
import java.util.Arrays;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.jce.X509KeyUsage;

public class KrccTAController extends TAController {
    public static final int SUCCESS_RET_VAL = 0;
    private static final String TAG = "Spay:KrccTAController";
    private static KrccTAInfo TA_INFO;
    private static KrccTAController mInstance;
    private byte[] krcc_cert_sign;
    private byte[] krcc_cert_sub;

    static {
        mInstance = null;
        TA_INFO = new KrccTAInfo();
    }

    private KrccTAController(Context context) {
        super(context, TA_INFO);
        this.krcc_cert_sign = null;
        this.krcc_cert_sub = null;
    }

    public static synchronized KrccTAController createOnlyInstance(Context context) {
        KrccTAController krccTAController;
        synchronized (KrccTAController.class) {
            if (mInstance == null) {
                mInstance = new KrccTAController(context);
            }
            krccTAController = mInstance;
        }
        return krccTAController;
    }

    public static synchronized KrccTAController getInstance() {
        KrccTAController krccTAController;
        synchronized (KrccTAController.class) {
            krccTAController = mInstance;
        }
        return krccTAController;
    }

    public void setKrccServerCerts(byte[] bArr, byte[] bArr2) {
        this.krcc_cert_sign = bArr;
        this.krcc_cert_sub = bArr2;
    }

    public boolean loadCert() {
        if (DEBUG) {
            Log.d(TAG, "Calling loadCert");
        }
        if (isTALoaded()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new Request(this.krcc_cert_sign, this.krcc_cert_sub));
                if (executeNoLoad == null) {
                    Log.e(TAG, "loadCert: Error: executeNoLoad failed");
                    return false;
                }
                Response response = new Response(executeNoLoad);
                if (DEBUG) {
                    Log.d(TAG, "err_msg= " + Arrays.toString(response.mRetVal.error_msg));
                    Log.d(TAG, "ret_code= " + response.mRetVal.return_code);
                    Log.d(TAG, "loadCert called Successfully");
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new KrccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
                }
                throw new KrccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
            }
        }
        Log.e(TAG, "loadCert: Error: TA is not loaded, please call loadTA() API first!");
        return false;
    }

    public boolean prepareMstData(byte[] bArr, byte[] bArr2) {
        if (DEBUG) {
            Log.d(TAG, "Calling prepareMstData");
        }
        if (bArr == null || bArr.length > X509KeyUsage.digitalSignature) {
            if (bArr != null) {
                Log.e(TAG, "prepareMstData: Error: invalid track length - " + bArr.length);
            } else {
                Log.e(TAG, "prepareMstData: Error: _track is null!");
            }
            throw new KrccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
        } else if (bArr2 == null || bArr2.length > SkeinMac.SKEIN_256) {
            if (bArr2 != null) {
                Log.e(TAG, "prepareMstData: Error: invalid track signature length - " + bArr2.length);
            } else {
                Log.e(TAG, "prepareMstData: Error: _signedTrackHash is null!");
            }
            throw new KrccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
        } else if (isTALoaded()) {
            try {
                loadCert();
                TACommandResponse executeNoLoad = executeNoLoad(new PrepareMstData.Request(bArr, bArr2));
                if (executeNoLoad == null) {
                    Log.e(TAG, "prepareMstData: Error: executeNoLoad failed");
                    throw new KrccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.e(TAG, "prepareMstData: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new KrccTAException("TA command returned error", SPayTUIException.ERR_TZ_COM_ERR);
                } else {
                    PrepareMstData.Response response = new PrepareMstData.Response(executeNoLoad);
                    if (DEBUG) {
                        Log.d(TAG, "prepareMstData: response.mRetVal.error_msg = " + Arrays.toString(response.mRetVal.error_msg));
                    }
                    moveSecOsToCore4();
                    if (response.mRetVal.return_code.get() == 0) {
                        return true;
                    }
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                moveSecOsToDefaultCore();
                if (e instanceof IllegalArgumentException) {
                    throw new KrccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
                }
                throw new KrccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
            }
        } else {
            Log.e(TAG, "prepareMstData: Error: TA is not loaded, please call loadTA() API first!");
            throw new KrccTAException("TA not loaded", SPayTUIException.ERR_TZ_TA_NOT_AVAILABLE);
        }
    }

    public void clearMstData() {
        if (DEBUG) {
            Log.d(TAG, "Calling clearMstData");
        }
        if (isTALoaded()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new ClearMstData.Request(SUCCESS_RET_VAL));
                if (executeNoLoad == null) {
                    Log.e(TAG, "Error: clearMstData executeNoLoad failed");
                    throw new KrccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.e(TAG, "clearMstData: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new KrccTAException("TA command returned error", SPayTUIException.ERR_TZ_COM_ERR);
                } else {
                    ClearMstData.Response response = new ClearMstData.Response(executeNoLoad);
                    if (DEBUG) {
                        Log.d(TAG, "clearMstData: success ");
                    }
                    if (response.mRetVal.return_code.get() != 0) {
                        Log.e(TAG, "clearMstData: Fail ");
                    }
                    moveSecOsToDefaultCore();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new KrccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
                }
                throw new KrccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
            } catch (Throwable th) {
                moveSecOsToDefaultCore();
            }
        } else {
            Log.e(TAG, "clearMstData: Error: TA is not loaded, please call loadTA() API first!");
            throw new KrccTAException("TA not loaded", SPayTUIException.ERR_TZ_TA_NOT_AVAILABLE);
        }
    }

    public void transmitMstData(int i, byte[] bArr) {
        if (DEBUG) {
            Log.d(TAG, "Calling transmitMstData");
        }
        if (bArr == null || bArr.length > 16) {
            if (bArr != null) {
                Log.e(TAG, "transmitMstData: Error: config length invalid! config.len = " + bArr.length);
            } else {
                Log.e(TAG, "transmitMstData: Error: config is null!");
            }
            throw new KrccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
        } else if (!isTALoaded()) {
            Log.e(TAG, "transmitMstData: Error: TA is not loaded, please call loadTA() API first!");
            throw new KrccTAException("TA not loaded", SPayTUIException.ERR_TZ_TA_NOT_AVAILABLE);
        } else if (makeSystemCall(1)) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new TransmitMstData.Request(i, bArr));
                if (!makeSystemCall(2)) {
                    Log.w(TAG, "transmitMstData: Error: Failed to turn MST Driver off");
                }
                if (executeNoLoad == null) {
                    Log.e(TAG, "Error: transmitMstData executeNoLoad failed");
                    throw new KrccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.e(TAG, "transmitMstData: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new KrccTAException("TA command returned error", SPayTUIException.ERR_TZ_COM_ERR);
                } else if (new TransmitMstData.Response(executeNoLoad).mRetVal.return_code.get() != 0) {
                    Log.e(TAG, "transmitMstData: fail ");
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new KrccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
                }
                throw new KrccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
            }
        } else {
            Log.e(TAG, "transmitMstData: Error: Failed to turn MST Driver on");
            throw new KrccTAException("Failed to turn MST ON", SPayTUIException.ERR_UNKNOWN);
        }
    }
}
