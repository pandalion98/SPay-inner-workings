package com.samsung.android.spayfw.remoteservice;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.core.ConfigurationManager;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client.C0575a;
import com.samsung.android.spayfw.remoteservice.Client.C0576b;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest;
import com.samsung.android.spayfw.remoteservice.p019b.HttpRequestAdapter;
import com.samsung.android.spayfw.remoteservice.p022e.SpayFwSSLSocketFactory;
import com.samsung.android.spayfw.remoteservice.p022e.SslUtils;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.utils.AttestationHelper;
import com.samsung.android.spayfw.utils.GLDManager;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.File;
import java.lang.reflect.Type;
import java.util.UUID;

/* renamed from: com.samsung.android.spayfw.remoteservice.a */
public abstract class CommonClient extends Client {
    protected final Context mContext;

    /* renamed from: com.samsung.android.spayfw.remoteservice.a.a */
    private static class CommonClient implements C0576b {
        private Context mContext;

        CommonClient(Context context) {
            this.mContext = context;
        }

        public void eR() {
            GLDManager.af(this.mContext).fG();
        }

        public String eN() {
            return GLDManager.af(this.mContext).by("PROD");
        }
    }

    /* renamed from: com.samsung.android.spayfw.remoteservice.a.b */
    private static class CommonClient implements C0575a {
        final Gson sGson;

        private CommonClient() {
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

    protected CommonClient(Context context, String str) {
        super(str, new CommonClient(), new CommonClient(context));
        this.mContext = context;
    }

    public GenericServerRequest m1124a(int i, String str, Bundle bundle, String str2) {
        Request genericServerRequest = new GenericServerRequest(this, i, str, bundle, str2);
        m1125a(genericServerRequest);
        return genericServerRequest;
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
            Log.m287i("CommonClient", "Request UUID " + randomUUID.toString());
            stringBuilder.append(HCEClientConstants.TAG_KEY_SEPARATOR).append(randomUUID.toString());
        }
        if (l != null) {
            Log.m287i("CommonClient", "Request Timestamp " + l);
            stringBuilder.append(HCEClientConstants.TAG_KEY_SEPARATOR).append(l);
        }
        return stringBuilder.toString();
    }

    public HttpRequest bd(String str) {
        return HttpRequestAdapter.m1162a(str, SpayFwSSLSocketFactory.m1187a(SslUtils.m1190M(this.mContext).getSocketFactory()));
    }

    protected boolean eQ() {
        return Utils.ak(this.mContext);
    }

    public void m1125a(Request request) {
        Log.m287i("CommonClient", "initializeRequest : Environment - PROD");
        Log.m287i("CommonClient", "Build CL : 3392039");
        request.setUserAgent(Utils.ai(this.mContext));
        request.addHeader("Authorization", "Bearer " + ConfigurationManager.m581h(this.mContext).getConfig(PaymentFramework.CONFIG_JWT_TOKEN));
        request.addHeader("x-smps-dmid", ConfigurationManager.m581h(this.mContext).getConfig(PaymentFramework.CONFIG_WALLET_ID));
        request.addHeader("x-smps-mid", ConfigurationManager.m581h(this.mContext).getConfig(PaymentFramework.CONFIG_USER_ID));
        request.addHeader("x-smps-cc2", Utils.fP());
        request.addHeader("x-smps-did", Utils.ah(this.mContext));
        request.addHeader("PF-Version", Utils.getPackageVersion(this.mContext, this.mContext.getPackageName()));
        request.addHeader("PF-Version-Code", Utils.m1277f(this.mContext, this.mContext.getPackageName()) + BuildConfig.FLAVOR);
        request.addHeader("Device-Did", Utils.ah(this.mContext));
        request.addHeader("Spay-Version", Utils.getPackageVersion(this.mContext, "com.samsung.android.spay"));
        request.addHeader("x-smps-model-id", Build.MODEL);
        request.addHeader("x-smps-mcc", DeviceInfo.getMcc(this.mContext));
        request.addHeader("x-smps-mnc", DeviceInfo.getMnc(this.mContext));
        request.addHeader("x-smps-dt", HCEClientConstants.API_INDEX_TOKEN_OPEN);
        request.addHeader("x-smps-lang", this.mContext.getResources().getConfiguration().locale.getLanguage());
        request.addHeader("x-smps-sales-cd", Utils.getSalesCode());
        request.addHeader("PF-Instance-Id", ConfigurationManager.m581h(this.mContext).getConfig(PaymentFramework.CONFIG_PF_INSTANCE_ID));
    }
}
