/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  android.util.Log
 *  java.lang.Exception
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import android.content.Context;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import android.util.Log;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexTAException;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.SPayTAAuthCommands;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;

public class h
extends TAController {
    protected h(Context context, TAInfo tAInfo) {
        super(context, tAInfo);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getNonce(int n2) {
        TACommandResponse tACommandResponse;
        Log.d((String)"SPayTAAuthController", (String)"Calling getNonce");
        if (!this.isTALoaded()) {
            Log.d((String)"SPayTAAuthController", (String)"getNonce: Error: TA is not loaded, please call loadTA() API first!");
            throw new AmexTAException(-1);
        }
        SPayTAAuthCommands.GetNonce.Request request = new SPayTAAuthCommands.GetNonce.Request(n2);
        try {
            tACommandResponse = this.executeNoLoad(request);
            if (tACommandResponse == null) {
                Log.e((String)"SPayTAAuthController", (String)"Error:getNonce executeNoLoad failed");
                throw new AmexTAException(-1);
            }
            if (tACommandResponse.mResponseCode != 0) {
                Log.e((String)"SPayTAAuthController", (String)("getNonce: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode));
                throw new AmexTAException(-1);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new AmexTAException(-1);
        }
        byte[] arrby = new SPayTAAuthCommands.GetNonce.Response((TACommandResponse)tACommandResponse).rV.out_data.getData();
        if (arrby != null) {
            this.dumpHex("SPayTAAuthController", "getNonce: ", arrby);
        }
        return arrby;
    }
}

