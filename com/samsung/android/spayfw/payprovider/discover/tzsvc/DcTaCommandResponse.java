package com.samsung.android.spayfw.payprovider.discover.tzsvc;

import android.spay.TACommandResponse;

public abstract class DcTaCommandResponse extends TACommandResponse {
    public DcTaCommandResponseData yX;

    public DcTaCommandResponse(TACommandResponse tACommandResponse) {
        super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
    }

    public boolean validate() {
        return this.yX.validate();
    }

    public long getReturnCode() {
        return this.yX.getReturnCode();
    }

    public String ez() {
        return this.yX.eA();
    }
}
