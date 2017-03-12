package com.samsung.android.spayfw.remoteservice.cashcard;

import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.PatchData;

/* renamed from: com.samsung.android.spayfw.remoteservice.cashcard.d */
public class SetPinRequest extends Request<PatchData[], String, Response<String>, SetPinRequest> {
    private String AO;

    protected SetPinRequest(CashCardClient cashCardClient, String str, PatchData[] patchDataArr) {
        super(cashCardClient, RequestMethod.PATCH, patchDataArr);
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Card Id is null or empty in SetPinRequest");
        }
        this.AO = str;
    }

    protected String cG() {
        return "/cards/" + this.AO;
    }

    protected String getRequestType() {
        return "SetPinRequest";
    }

    protected Response<String> m1181b(int i, String str) {
        return null;
    }
}
