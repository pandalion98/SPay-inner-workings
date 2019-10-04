/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 */
package com.samsung.android.spayfw.core;

import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import java.util.HashMap;
import java.util.Map;

public class p {
    private static Map<String, String> kb = new HashMap();
    private static Map<Integer, String> kc = new HashMap();

    static {
        kc.put((Object)-2, (Object)"ERROR-20000");
        kc.put((Object)-9, (Object)"ERROR-21000");
        kc.put((Object)-7, (Object)"ERROR-40000");
        kc.put((Object)-8, (Object)"ERROR-41000");
        kc.put((Object)-6, (Object)"ERROR-30000");
        kb.put((Object)"PENDING", (Object)"PENDING");
        kb.put((Object)"ENROLLED", (Object)"PENDING");
        kb.put((Object)"PENDING_PROVISION", (Object)"PENDING");
        kb.put((Object)"ACTIVE", (Object)"ACTIVE");
        kb.put((Object)"SUSPENDED", (Object)"SUSPENDED");
        kb.put((Object)"DISPOSED", (Object)"DISPOSED");
    }

    public static String G(String string) {
        return (String)kb.get((Object)string);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int a(int n2, ErrorResponseData errorResponseData) {
        int n3 = -3;
        int n4 = -17;
        switch (n2) {
            default: {
                return -1;
            }
            case 200: 
            case 201: 
            case 202: {
                return 0;
            }
            case 204: 
            case 205: {
                return -202;
            }
            case 400: {
                n4 = -5;
                if (errorResponseData == null) return n4;
                if (errorResponseData.getCode() == null) return n4;
                String string = errorResponseData.getCode();
                if ("400.5".equals((Object)string)) {
                    return -11;
                }
                if ("400.4".equals((Object)string)) return n3;
                return n4;
            }
            case 401: 
            case 403: {
                return -4;
            }
            case 407: {
                if (errorResponseData == null) return n4;
                if (errorResponseData.getCode() == null) return n4;
                String string = errorResponseData.getCode();
                if ("407.3".equals((Object)string)) return n4;
                if ("407.2".equals((Object)string)) {
                    return n4;
                }
                if ("407.1".equals((Object)string)) {
                    return -16;
                }
                if ("403.2".equals((Object)string)) {
                    return -18;
                }
                if ("403.3".equals((Object)string)) {
                    return -19;
                }
                if ("403.6".equals((Object)string)) {
                    return -20;
                }
                if (!"403.7".equals((Object)string)) return n4;
                return -21;
            }
            case 406: 
            case 410: {
                return -202;
            }
            case 409: {
                return n3;
            }
            case 404: 
            case 500: {
                return -205;
            }
            case 0: 
            case 408: 
            case 503: {
                return -201;
            }
            case -2: {
                return -206;
            }
        }
    }

    public static String t(int n2) {
        return (String)kc.get((Object)n2);
    }
}

