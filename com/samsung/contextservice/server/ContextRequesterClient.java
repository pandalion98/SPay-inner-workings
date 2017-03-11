package com.samsung.contextservice.server;

import android.content.Context;
import android.os.Build;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Client.C0575a;
import com.samsung.android.spayfw.remoteservice.Client.C0576b;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.p019b.HttpRequestAdapter;
import com.samsung.android.spayfw.remoteservice.p022e.SpayFwSSLSocketFactory;
import com.samsung.android.spayfw.remoteservice.p022e.SslUtils;
import com.samsung.android.spayfw.utils.AttestationHelper;
import com.samsung.android.spayfw.utils.GLDManager;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.contextclient.data.Location;
import com.samsung.contextservice.p029b.CSlog;
import com.samsung.contextservice.server.models.CacheRequestData;
import com.samsung.contextservice.server.models.DeviceInfo;
import com.samsung.contextservice.server.models.TriggerRequestData;
import java.io.File;
import java.lang.reflect.Type;
import java.util.UUID;

/* renamed from: com.samsung.contextservice.server.d */
public class ContextRequesterClient extends Client {
    private static ContextRequesterClient GG;
    private final Context mContext;

    /* renamed from: com.samsung.contextservice.server.d.a */
    private static class ContextRequesterClient implements C0576b {
        private Context mContext;

        ContextRequesterClient(Context context) {
            this.mContext = context;
        }

        public void eR() {
            GLDManager.af(this.mContext).fG();
        }

        public String eN() {
            return GLDManager.af(this.mContext).by(ServerConfig.gA());
        }
    }

    /* renamed from: com.samsung.contextservice.server.d.b */
    private static class ContextRequesterClient implements C0575a {
        final Gson sGson;

        private ContextRequesterClient() {
            this.sGson = new GsonBuilder().disableHtmlEscaping().create();
        }

        public String toJson(Object obj) {
            return this.sGson.toJson(obj);
        }

        public <Z> Z fromJson(String str, Class<Z> cls) {
            return this.sGson.fromJson(str, (Class) cls);
        }

        public <Z> Z fromJson(String str, Type type) {
            return this.sGson.fromJson(str, type);
        }
    }

    public static synchronized ContextRequesterClient aw(Context context) {
        ContextRequesterClient contextRequesterClient;
        synchronized (ContextRequesterClient.class) {
            if (GG == null) {
                GG = new ContextRequesterClient(context);
            }
            contextRequesterClient = GG;
        }
        return contextRequesterClient;
    }

    public ContextRequesterClient(Context context) {
        super("/cp/v1", new ContextRequesterClient(), new ContextRequesterClient(context));
        this.mContext = context;
    }

    public CacheRequest m1432a(Location location) {
        Request cacheRequest = new CacheRequest(this, new CacheRequestData(location));
        m1434a(cacheRequest);
        return cacheRequest;
    }

    public TriggerRequest m1433a(TriggerRequestData triggerRequestData) {
        Request triggerRequest = new TriggerRequest(this, triggerRequestData);
        m1434a(triggerRequest);
        return triggerRequest;
    }

    public PolicyRequest bL(String str) {
        Request policyRequest = new PolicyRequest(this, str);
        m1434a(policyRequest);
        return policyRequest;
    }

    public File aU(String str) {
        return this.mContext.getDir(str, 0);
    }

    public String bc(String str) {
        return AttestationHelper.bx(str);
    }

    public String getRequestId() {
        String str = Build.SERIAL;
        String deviceImei = DeviceInfo.getDeviceImei(this.mContext);
        UUID randomUUID = UUID.randomUUID();
        String l = Long.toString(System.currentTimeMillis());
        StringBuilder stringBuilder = new StringBuilder();
        if (str != null) {
            stringBuilder.append(str);
        }
        if (deviceImei != null) {
            stringBuilder.append(HCEClientConstants.TAG_KEY_SEPARATOR).append(deviceImei);
        }
        if (randomUUID != null) {
            CSlog.m1410i("CtxRequesterClient", "Request UUID " + randomUUID.toString());
            stringBuilder.append(HCEClientConstants.TAG_KEY_SEPARATOR).append(randomUUID.toString());
        }
        if (l != null) {
            CSlog.m1410i("CtxRequesterClient", "Request Timestamp " + l);
            stringBuilder.append(HCEClientConstants.TAG_KEY_SEPARATOR).append(l);
        }
        return stringBuilder.toString();
    }

    public HttpRequest bd(String str) {
        return HttpRequestAdapter.m1162a(str, SpayFwSSLSocketFactory.m1187a(SslUtils.m1190M(this.mContext).getSocketFactory()));
    }

    public void m1434a(Request request) {
        request.setUserAgent(Utils.ai(this.mContext));
        request.addHeader("Authorization", "Bearer " + com.samsung.contextservice.p029b.Utils.aJ(this.mContext));
        request.addHeader("x-smps-dmid", com.samsung.contextservice.p029b.Utils.aI(this.mContext));
        request.addHeader("x-smps-mid", com.samsung.contextservice.p029b.Utils.aK(this.mContext));
        request.addHeader("x-smps-cc2", com.samsung.contextservice.p029b.Utils.fP());
        request.addHeader("x-smps-did", com.samsung.contextservice.p029b.Utils.ah(this.mContext));
        request.addHeader("PF-Version", com.samsung.contextservice.p029b.Utils.getPackageVersion(this.mContext, this.mContext.getPackageName()));
        request.addHeader("Device-Did", DeviceInfo.getDeviceId(this.mContext));
        request.addHeader("Spay-Version", com.samsung.contextservice.p029b.Utils.getPackageVersion(this.mContext, "com.samsung.android.spay"));
        request.addHeader("CTX-Version", "1.1.00");
        request.addHeader("x-smps-model-id", Build.MODEL);
        request.addHeader("x-smps-mcc", com.samsung.contextservice.p029b.Utils.getMcc(this.mContext));
        request.addHeader("x-smps-mnc", com.samsung.contextservice.p029b.Utils.getMnc(this.mContext));
        request.addHeader("x-smps-dt", HCEClientConstants.API_INDEX_TOKEN_OPEN);
        request.addHeader("x-smps-lang", this.mContext.getResources().getConfiguration().locale.getLanguage());
        request.addHeader("x-smps-sales-cd", Utils.getSalesCode());
    }
}
