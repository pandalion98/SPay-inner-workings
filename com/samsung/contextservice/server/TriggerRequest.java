package com.samsung.contextservice.server;

import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.contextservice.server.models.TriggerRequestData;
import com.samsung.contextservice.server.models.TriggerResponseData;

/* renamed from: com.samsung.contextservice.server.k */
public class TriggerRequest extends Request<TriggerRequestData, TriggerResponseData, Response<TriggerResponseData>, TriggerRequest> {
    protected TriggerRequest(ContextRequesterClient contextRequesterClient, TriggerRequestData triggerRequestData) {
        super(contextRequesterClient, RequestMethod.POST, triggerRequestData);
    }

    protected String cG() {
        return "/triggers";
    }

    protected String getRequestType() {
        return "TriggerRequest";
    }

    protected Response<TriggerResponseData> m1459b(int i, String str) {
        return new Response(null, (TriggerResponseData) this.Al.fromJson(str, TriggerResponseData.class), i);
    }
}
