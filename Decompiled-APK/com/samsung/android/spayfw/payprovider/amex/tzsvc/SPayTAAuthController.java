package com.samsung.android.spayfw.payprovider.amex.tzsvc;

import android.content.Context;
import android.spay.TACommandResponse;
import android.util.Log;
import com.samsung.android.spayfw.cncc.CNCCTAException;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.SPayTAAuthCommands.AuthenticateTransaction;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.SPayTAAuthCommands.GetNonce.Request;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.SPayTAAuthCommands.GetNonce.Response;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;

/* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.b */
public class SPayTAAuthController extends TAController {
    protected SPayTAAuthController(Context context, TAInfo tAInfo) {
        super(context, tAInfo);
    }

    public byte[] getNonce(int i) {
        Log.d("SPayTAAuthController", "Calling getNonce");
        if (isTALoaded()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new Request(i));
                if (executeNoLoad == null) {
                    Log.e("SPayTAAuthController", "Error:getNonce executeNoLoad failed");
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.e("SPayTAAuthController", "getNonce: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                } else {
                    byte[] data = new Response(executeNoLoad).mRetVal.out_data.getData();
                    if (data != null) {
                        dumpHex("SPayTAAuthController", "getNonce: ", data);
                    }
                    return data;
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
            }
        }
        Log.d("SPayTAAuthController", "getNonce: Error: TA is not loaded, please call loadTA() API first!");
        throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
    }

    public boolean authenticateTransaction(byte[] bArr) {
        Log.d("SPayTAAuthController", "Calling authenticateTransaction");
        if (bArr == null || bArr.length > PKIFailureInfo.certConfirmed) {
            if (bArr != null) {
                Log.d("SPayTAAuthController", "authenticateTransaction: Error: secureObject length invalid! secureObject.len = " + bArr.length);
            } else {
                Log.d("SPayTAAuthController", "authenticateTransaction: Error: secureObject is null!");
            }
            throw new AmexTAException(CNCCTAException.CNCC_INVALID_INPUT_BUFFER);
        } else if (isTALoaded()) {
            boolean z = false;
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new AuthenticateTransaction.Request(bArr));
                if (executeNoLoad == null) {
                    Log.e("SPayTAAuthController", "Error: authenticateTransaction executeNoLoad failed");
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.e("SPayTAAuthController", "authenticateTransaction: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                } else {
                    long j = new AuthenticateTransaction.Response(executeNoLoad).mRetVal.auth_result.get();
                    if (j == 0) {
                        z = true;
                    }
                    Log.d("SPayTAAuthController", "authenticateTransaction: auth_result = " + j);
                    return z;
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
            }
        } else {
            Log.d("SPayTAAuthController", "authenticateTransaction: Error: TA is not loaded, please call loadTA() API first!");
            throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
        }
    }
}
