package com.samsung.android.spayfw.remoteservice;

import android.os.Bundle;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.spayfw.remoteservice.b */
public class GenericServerRequest extends Request<String, String, ServerResponse, GenericServerRequest> {
    private Bundle headers;
    private String url;

    protected /* synthetic */ Response m1165b(int i, String str) {
        return m1166c(i, str);
    }

    protected /* synthetic */ Response m1168e(int i, String str) {
        return m1167d(i, str);
    }

    private static RequestMethod m1164S(int i) {
        switch (i) {
            case ECCurve.COORD_AFFINE /*0*/:
                return RequestMethod.GET;
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return RequestMethod.POST;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return RequestMethod.DELETE;
            default:
                return RequestMethod.GET;
        }
    }

    protected GenericServerRequest(CommonClient commonClient, int i, String str, Bundle bundle, String str2) {
        super(commonClient, GenericServerRequest.m1164S(i), str2);
        this.url = str;
        this.headers = bundle;
    }

    protected String cG() {
        return this.url;
    }

    protected String getRequestType() {
        return "GenericServerRequest";
    }

    protected ServerResponse m1166c(int i, String str) {
        String str2 = null;
        if (str != null) {
            Log.m285d("GenericServerRequest", "Response Bytes = " + str.getBytes());
            Log.m285d("GenericServerRequest", "Response Bytes Length= " + str.getBytes().length);
            if (str.getBytes().length > 200000) {
                str2 = aT(str);
            }
        }
        return new ServerResponse(str2, str, i);
    }

    protected void init() {
        if (this.headers != null) {
            for (String str : this.headers.keySet()) {
                Object obj = this.headers.get(str);
                if (obj == null || !(obj instanceof String)) {
                    Log.m290w("GenericServerRequest", "Cannot add header : " + str);
                } else {
                    Log.m285d("GenericServerRequest", "add header : " + str + ", value: " + ((String) obj));
                    addHeader(str, (String) obj);
                }
            }
        }
    }

    protected String m1169j(Object obj) {
        Log.m285d("GenericServerRequest", "Entered getRequestDataString: ");
        try {
            obj = (String) obj;
        } catch (Throwable e) {
            Log.m284c("GenericServerRequest", e.getMessage(), e);
            obj = null;
        }
        Log.m285d("GenericServerRequest", "requestDataString = " + obj);
        return obj;
    }

    protected ServerResponse m1167d(int i, String str) {
        return new ServerResponse(null, str, i);
    }
}
