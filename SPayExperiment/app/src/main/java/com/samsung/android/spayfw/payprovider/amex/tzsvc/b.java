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
package com.samsung.android.spayfw.payprovider.amex.tzsvc;

import android.content.Context;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import android.util.Log;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAException;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.SPayTAAuthCommands;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import javolution.io.Struct;

public class b
extends TAController {
    protected b(Context context, TAInfo tAInfo) {
        super(context, tAInfo);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean authenticateTransaction(byte[] arrby) {
        TACommandResponse tACommandResponse;
        Log.d((String)"SPayTAAuthController", (String)"Calling authenticateTransaction");
        if (arrby == null || arrby.length > 4096) {
            if (arrby != null) {
                Log.d((String)"SPayTAAuthController", (String)("authenticateTransaction: Error: secureObject length invalid! secureObject.len = " + arrby.length));
                throw new AmexTAException(983042);
            }
            Log.d((String)"SPayTAAuthController", (String)"authenticateTransaction: Error: secureObject is null!");
            throw new AmexTAException(983042);
        }
        if (!this.isTALoaded()) {
            Log.d((String)"SPayTAAuthController", (String)"authenticateTransaction: Error: TA is not loaded, please call loadTA() API first!");
            throw new AmexTAException(983040);
        }
        try {
            tACommandResponse = this.executeNoLoad(new SPayTAAuthCommands.AuthenticateTransaction.Request(arrby));
            if (tACommandResponse == null) {
                Log.e((String)"SPayTAAuthController", (String)"Error: authenticateTransaction executeNoLoad failed");
                throw new AmexTAException(983040);
            }
            if (tACommandResponse.mResponseCode != 0) {
                Log.e((String)"SPayTAAuthController", (String)("authenticateTransaction: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode));
                throw new AmexTAException(983040);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new AmexTAException(983040);
        }
        long l2 = new SPayTAAuthCommands.AuthenticateTransaction.Response((TACommandResponse)tACommandResponse).mRetVal.auth_result.get();
        long l3 = l2 LCMP 0L;
        boolean bl = false;
        if (l3 == false) {
            bl = true;
        }
        Log.d((String)"SPayTAAuthController", (String)("authenticateTransaction: auth_result = " + l2));
        return bl;
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
            throw new AmexTAException(983040);
        }
        SPayTAAuthCommands.GetNonce.Request request = new SPayTAAuthCommands.GetNonce.Request(n2);
        try {
            tACommandResponse = this.executeNoLoad(request);
            if (tACommandResponse == null) {
                Log.e((String)"SPayTAAuthController", (String)"Error:getNonce executeNoLoad failed");
                throw new AmexTAException(983040);
            }
            if (tACommandResponse.mResponseCode != 0) {
                Log.e((String)"SPayTAAuthController", (String)("getNonce: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode));
                throw new AmexTAException(983040);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new AmexTAException(983040);
        }
        byte[] arrby = new SPayTAAuthCommands.GetNonce.Response((TACommandResponse)tACommandResponse).mRetVal.out_data.getData();
        if (arrby != null) {
            this.dumpHex("SPayTAAuthController", "getNonce: ", arrby);
        }
        return arrby;
    }
}

