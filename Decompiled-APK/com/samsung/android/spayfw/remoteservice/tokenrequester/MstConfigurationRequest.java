package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.MstConfigurationRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.MstConfigurationResponseData;

/* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.d */
public class MstConfigurationRequest extends TokenRequesterRequest<MstConfigurationRequestData, MstConfigurationResponseData, Response<MstConfigurationResponseData>, MstConfigurationRequest> {
    public MstConfigurationRequest(TokenRequesterClient tokenRequesterClient, MstConfigurationRequestData mstConfigurationRequestData) {
        super(tokenRequesterClient, RequestMethod.POST, mstConfigurationRequestData);
    }

    protected String cG() {
        return "/attempts";
    }

    protected String getRequestType() {
        return "MstConfigurationRequest";
    }

    protected Response<MstConfigurationResponseData> m1199b(int i, String str) {
        MstConfigurationResponseData mstConfigurationResponseData = (MstConfigurationResponseData) this.Al.fromJson(str, MstConfigurationResponseData.class);
        Log.m285d("MstConfigurationRequest", "MstConfigurationResponseData : " + mstConfigurationResponseData);
        return new Response(null, mstConfigurationResponseData, i);
    }
}
