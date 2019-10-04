/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.spay.TACommandResponse
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.tzsvc;

import android.spay.TACommandResponse;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.d;

public abstract class DcTaCommandResponse
extends TACommandResponse {
    public d yX;

    public DcTaCommandResponse(TACommandResponse tACommandResponse) {
        super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
    }

    public String ez() {
        return this.yX.eA();
    }

    public long getReturnCode() {
        return this.yX.getReturnCode();
    }

    public boolean validate() {
        return this.yX.validate();
    }
}

