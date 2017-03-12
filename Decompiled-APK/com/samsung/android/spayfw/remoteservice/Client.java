package com.samsung.android.spayfw.remoteservice;

import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public abstract class Client {
    private final String Ac;
    private final C0575a Ad;
    private final C0576b Ae;
    private String Af;

    public interface HttpRequest {

        public enum RequestMethod {
            GET,
            POST,
            DELETE,
            PATCH
        }

        /* renamed from: com.samsung.android.spayfw.remoteservice.Client.HttpRequest.a */
        public interface C0574a {
            void m1154a(int i, Map<String, List<String>> map, byte[] bArr);

            void m1155a(IOException iOException);
        }

        void m1156a(RequestMethod requestMethod, String str, String str2, C0574a c0574a, boolean z);

        void be(String str);

        void setHeader(String str, String str2);
    }

    /* renamed from: com.samsung.android.spayfw.remoteservice.Client.a */
    public interface C0575a {
        <Z> Z fromJson(String str, Class<Z> cls);

        <Z> Z fromJson(String str, Type type);

        String toJson(Object obj);
    }

    /* renamed from: com.samsung.android.spayfw.remoteservice.Client.b */
    public interface C0576b {
        String eN();

        void eR();
    }

    public abstract File aU(String str);

    public abstract String bc(String str);

    public abstract HttpRequest bd(String str);

    public abstract String getRequestId();

    protected Client(String str, C0575a c0575a, C0576b c0576b) {
        this.Af = BuildConfig.FLAVOR;
        this.Ac = str;
        this.Ad = c0575a;
        this.Ae = c0576b;
    }

    public final String eN() {
        String eN = this.Ae.eN();
        if (eN == null) {
            return null;
        }
        return eN + this.Ac;
    }

    public final C0575a eO() {
        return this.Ad;
    }

    public final C0576b eP() {
        return this.Ae;
    }

    protected boolean eQ() {
        return true;
    }
}
