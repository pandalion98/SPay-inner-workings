package com.samsung.contextservice.server;

import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.contextservice.server.models.PolicyResponseData;

/* renamed from: com.samsung.contextservice.server.g */
public class PolicyRequest extends Request<String, PolicyResponseData, Response<PolicyResponseData>, PolicyRequest> {
    private String mId;

    protected PolicyRequest(ContextRequesterClient contextRequesterClient, String str) {
        super(contextRequesterClient, RequestMethod.GET, str);
        this.mId = str;
    }

    protected String cG() {
        if (this.mId == null) {
            return "/policies";
        }
        return "/policies/" + this.mId;
    }

    protected String getRequestType() {
        return "PolicyRequest";
    }

    protected Response<PolicyResponseData> m1456b(int i, String str) {
        return new Response(null, (PolicyResponseData) this.Al.fromJson(str, PolicyResponseData.class), i);
    }
}
