package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import android.content.Context;
import android.spay.TACommandResponse;
import android.util.Log;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.SPayTAAuthCommands.GetNonce.Request;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.SPayTAAuthCommands.GetNonce.Response;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;

/* renamed from: com.samsung.android.spayfw.payprovider.amexv2.tzsvc.h */
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
                    throw new AmexTAException(-1);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.e("SPayTAAuthController", "getNonce: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new AmexTAException(-1);
                } else {
                    byte[] data = new Response(executeNoLoad).rV.out_data.getData();
                    if (data != null) {
                        dumpHex("SPayTAAuthController", "getNonce: ", data);
                    }
                    return data;
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(-1);
            }
        }
        Log.d("SPayTAAuthController", "getNonce: Error: TA is not loaded, please call loadTA() API first!");
        throw new AmexTAException(-1);
    }
}
