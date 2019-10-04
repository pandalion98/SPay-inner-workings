/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Type
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spayfw.remoteservice;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public abstract class Client {
    private final String Ac;
    private final a Ad;
    private final b Ae;
    private String Af = "";

    protected Client(String string, a a2, b b2) {
        this.Ac = string;
        this.Ad = a2;
        this.Ae = b2;
    }

    public abstract File aU(String var1);

    public abstract String bc(String var1);

    public abstract HttpRequest bd(String var1);

    public final String eN() {
        String string = this.Ae.eN();
        if (string == null) {
            return null;
        }
        return string + this.Ac;
    }

    public final a eO() {
        return this.Ad;
    }

    public final b eP() {
        return this.Ae;
    }

    protected boolean eQ() {
        return true;
    }

    public abstract String getRequestId();

    public static interface HttpRequest {
        public void a(RequestMethod var1, String var2, String var3, a var4, boolean var5);

        public void be(String var1);

        public void setHeader(String var1, String var2);

        public static final class RequestMethod
        extends Enum<RequestMethod> {
            public static final /* enum */ RequestMethod Ag = new RequestMethod();
            public static final /* enum */ RequestMethod Ah = new RequestMethod();
            public static final /* enum */ RequestMethod Ai = new RequestMethod();
            public static final /* enum */ RequestMethod Aj = new RequestMethod();
            private static final /* synthetic */ RequestMethod[] Ak;

            static {
                RequestMethod[] arrrequestMethod = new RequestMethod[]{Ag, Ah, Ai, Aj};
                Ak = arrrequestMethod;
            }

            public static RequestMethod valueOf(String string) {
                return (RequestMethod)Enum.valueOf(RequestMethod.class, (String)string);
            }

            public static RequestMethod[] values() {
                return (RequestMethod[])Ak.clone();
            }
        }

        public static interface a {
            public void a(int var1, Map<String, List<String>> var2, byte[] var3);

            public void a(IOException var1);
        }

    }

    public static interface a {
        public <Z> Z fromJson(String var1, Class<Z> var2);

        public <Z> Z fromJson(String var1, Type var2);

        public String toJson(Object var1);
    }

    public static interface b {
        public String eN();

        public void eR();
    }

}

