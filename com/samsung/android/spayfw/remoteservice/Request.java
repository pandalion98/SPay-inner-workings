package com.samsung.android.spayfw.remoteservice;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client.C0575a;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.C0574a;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.models.ServerCertificates;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.bouncycastle.asn1.x509.DisplayText;

public abstract class Request<U, V, W extends Response<V>, T extends Request<U, V, W, T>> {
    private static final Object AC;
    private static boolean AD;
    private static List<Request> AE;
    private String AA;
    private String AB;
    protected C0575a Al;
    protected Client Am;
    private HttpRequest An;
    private C0574a Ao;
    private String Ap;
    private C0413a<W, T> Aq;
    private RequestMethod Ar;
    private U As;
    private W At;
    private boolean Au;
    private String Av;
    private int Aw;
    private Map<String, String> Ax;
    private String Ay;
    private String Az;
    private String TAG;

    /* renamed from: com.samsung.android.spayfw.remoteservice.Request.a */
    public static abstract class C0413a<X, Y> {
        public abstract void m363a(int i, X x);

        public boolean m364a(int i, String str) {
            throw new IllegalStateException("Request.Callback.onCasdUpdate() method not overridden but the CASD Parameters are set. EITHER Do not set the CASD Parameters in request OR Override Request.Callback.onCasdUpdate() method");
        }

        public void m362a(int i, ServerCertificates serverCertificates, Y y) {
            throw new IllegalStateException("Request.Callback.onCertsReceived() method not overridden but the server cert hash is set. EITHER Do not set server cert hash in request OR Override Request.Callback.onCertsReceived() method");
        }

        public void m365f(int i, String str) {
        }
    }

    /* renamed from: com.samsung.android.spayfw.remoteservice.Request.1 */
    class C05771 implements C0574a {
        final /* synthetic */ Request AF;

        C05771(Request request) {
            this.AF = request;
        }

        public void m1157a(int i, Map<String, List<String>> map, byte[] bArr) {
            try {
                Log.m287i(this.AF.TAG, "TR Response : " + i);
                if (i == 401 && bArr != null) {
                    try {
                        String str = new String(bArr);
                        Log.m286e(this.AF.TAG, "TR Response : " + str);
                        CifResponse cifResponse = (CifResponse) this.AF.Al.fromJson(str, CifResponse.class);
                        if (!(cifResponse == null || cifResponse.resultCode == null || !cifResponse.resultCode.equals("CIF1N1017"))) {
                            i = -2;
                        }
                        this.AF.At = this.AF.m841e(i, str);
                    } catch (Exception e) {
                        Log.m286e(this.AF.TAG, e.getMessage());
                    }
                }
                if (i == 412) {
                    Log.m286e(this.AF.TAG, "TR Response : HEADER_PF_VERSION : " + Request.m829b((Map) map, "PF-Version"));
                } else if (i == 421) {
                    this.AF.m830b(i, map, bArr);
                    return;
                } else if (i == 409) {
                    if (bArr != null) {
                        r0 = new String(bArr);
                        Log.m286e(this.AF.TAG, "TR Response : " + r0);
                        this.AF.At = this.AF.m838b(i, r0);
                    }
                } else if (i == 503) {
                    r0 = Request.m829b((Map) map, "Retry-After");
                    Log.m286e(this.AF.TAG, "TR Response ::onFailure : retry-after = " + r0);
                    if (this.AF.Aq != null) {
                        this.AF.Aq.m365f(i, r0);
                    }
                } else if (i >= DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE && i <= 300) {
                    Log.m285d(this.AF.TAG, "TR Response : HEADER_PF_VERSION : " + Request.m829b((Map) map, "PF-Version"));
                    if (bArr != null) {
                        r0 = new String(bArr);
                        Log.m285d(this.AF.TAG, "TR Response : " + r0);
                        this.AF.At = this.AF.m838b(i, r0);
                        r0 = Request.m829b((Map) map, "Server-Tokens");
                        Log.m285d(this.AF.TAG, "TR Response : HEADER_SERVER_TOKENS : " + r0);
                        if (r0 != null) {
                            this.AF.At.bh(r0);
                        }
                    }
                } else if (bArr != null) {
                    r0 = new String(bArr);
                    Log.m286e(this.AF.TAG, "TR Response Error: " + r0);
                    this.AF.At = this.AF.m841e(i, r0);
                }
                Log.m285d(this.AF.TAG, "mCallback : " + this.AF.Aq);
                if (this.AF.Aq != null) {
                    this.AF.Aq.m363a(i, this.AF.At);
                }
            } catch (Throwable e2) {
                Log.m284c(this.AF.TAG, "onResponse", e2);
                this.AF.At = this.AF.m841e(0, null);
                if (this.AF.Aq != null) {
                    this.AF.Aq.m363a(0, this.AF.At);
                }
            }
        }

        public void m1158a(IOException iOException) {
            Log.m286e(this.AF.TAG, "TR Response : " + iOException.getClass());
            Log.m286e(this.AF.TAG, "TR Response : " + iOException.getMessage());
            if (!(iOException instanceof UnknownHostException) || this.AF.Aw >= 4) {
                this.AF.Aw = 0;
                this.AF.At = this.AF.m841e(0, null);
                if (this.AF.Aq != null) {
                    this.AF.Aq.m363a(0, this.AF.At);
                    return;
                }
                return;
            }
            this.AF.Am.eP().eR();
            try {
                this.AF.eW();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.samsung.android.spayfw.remoteservice.Request.2 */
    class C05782 extends TimerTask {
        final /* synthetic */ Request AF;

        C05782(Request request) {
            this.AF = request;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r6 = this;
            r1 = com.samsung.android.spayfw.remoteservice.Request.AC;
            monitor-enter(r1);
            r0 = r6.AF;	 Catch:{ all -> 0x0051 }
            r0 = r0.TAG;	 Catch:{ all -> 0x0051 }
            r2 = "runUnattestedFlow: dispatching req queue";
            com.samsung.android.spayfw.p002b.Log.m287i(r0, r2);	 Catch:{ all -> 0x0051 }
            r0 = 0;
            com.samsung.android.spayfw.remoteservice.Request.AD = r0;	 Catch:{ all -> 0x0051 }
            r0 = com.samsung.android.spayfw.remoteservice.Request.AE;	 Catch:{ all -> 0x0051 }
            r2 = r0.iterator();	 Catch:{ all -> 0x0051 }
        L_0x001c:
            r0 = r2.hasNext();	 Catch:{ all -> 0x0051 }
            if (r0 == 0) goto L_0x0054;
        L_0x0022:
            r0 = r2.next();	 Catch:{ all -> 0x0051 }
            r0 = (com.samsung.android.spayfw.remoteservice.Request) r0;	 Catch:{ all -> 0x0051 }
            r3 = r6.AF;	 Catch:{ all -> 0x0051 }
            r3 = r3.TAG;	 Catch:{ all -> 0x0051 }
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0051 }
            r4.<init>();	 Catch:{ all -> 0x0051 }
            r5 = "runUnattestedFlow: dispatch req : ";
            r4 = r4.append(r5);	 Catch:{ all -> 0x0051 }
            r5 = r0.getRequestType();	 Catch:{ all -> 0x0051 }
            r4 = r4.append(r5);	 Catch:{ all -> 0x0051 }
            r4 = r4.toString();	 Catch:{ all -> 0x0051 }
            com.samsung.android.spayfw.p002b.Log.m287i(r3, r4);	 Catch:{ all -> 0x0051 }
            r0.eW();	 Catch:{ Exception -> 0x004c }
            goto L_0x001c;
        L_0x004c:
            r0 = move-exception;
            r0.printStackTrace();	 Catch:{ all -> 0x0051 }
            goto L_0x001c;
        L_0x0051:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0051 }
            throw r0;
        L_0x0054:
            r0 = com.samsung.android.spayfw.remoteservice.Request.AE;	 Catch:{ all -> 0x0051 }
            r0.clear();	 Catch:{ all -> 0x0051 }
            monitor-exit(r1);	 Catch:{ all -> 0x0051 }
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.remoteservice.Request.2.run():void");
        }
    }

    private static class CifResponse {
        String resultCode;

        private CifResponse() {
        }
    }

    protected abstract W m838b(int i, String str);

    protected abstract String cG();

    protected abstract String getRequestType();

    private static String m829b(Map<String, List<String>> map, String str) {
        if (map == null || map.get(str) == null) {
            return null;
        }
        return (String) ((List) map.get(str)).get(0);
    }

    static {
        AC = new Object();
        AD = false;
        AE = new ArrayList();
    }

    protected Request(Client client, RequestMethod requestMethod, U u) {
        this.TAG = "Request_";
        this.Aw = 0;
        this.Ax = new HashMap();
        this.Am = client;
        this.Ar = requestMethod;
        this.As = u;
        this.Al = client.eO();
        if (getRequestType() != null) {
            this.TAG += getRequestType();
        }
    }

    public final void m836a(C0413a<W, T> c0413a) {
        boolean eQ = this.Am.eQ();
        Log.m285d(this.TAG, "execute: data connection state: " + eQ);
        if (eQ) {
            this.Aq = c0413a;
            this.Au = false;
            try {
                eW();
                return;
            } catch (Throwable e) {
                Log.m284c(this.TAG, e.getMessage(), e);
                if (this.Aq != null) {
                    this.Aq.m363a(0, null);
                    return;
                }
                return;
            }
        }
        Log.m286e(this.TAG, "execute: No data connection found");
        c0413a.m363a(0, null);
    }

    public final void m839b(C0413a<W, T> c0413a) {
        boolean eQ = this.Am.eQ();
        Log.m285d(this.TAG, "executeSync: data connection state: " + eQ);
        if (eQ) {
            this.Aq = c0413a;
            this.Au = true;
            try {
                eW();
                return;
            } catch (Throwable e) {
                Log.m284c(this.TAG, e.getMessage(), e);
                if (this.Aq != null) {
                    this.Aq.m363a(0, null);
                    return;
                }
                return;
            }
        }
        Log.m286e(this.TAG, "executeSync: No data connection found");
        c0413a.m363a(0, null);
    }

    public final Response eS() {
        m839b(null);
        return this.At;
    }

    public final void bf(String str) {
        this.AA = str;
    }

    public final void m844s(String str, String str2) {
        this.Ay = str;
        this.Az = str2;
        this.AA = BuildConfig.FLAVOR;
    }

    public void setUserAgent(String str) {
        this.Av = str;
    }

    public U eT() {
        return this.As;
    }

    public final void m843k(U u) {
        this.As = u;
    }

    public void addHeader(String str, String str2) {
        if (str != null && str2 != null) {
            this.Ax.put(str, str2);
        }
    }

    public String bg(String str) {
        return (String) this.Ax.get(str);
    }

    public String getRequestId() {
        return this.AB;
    }

    protected void init() {
    }

    protected String m842j(Object obj) {
        return this.Al.toJson(obj);
    }

    protected String eU() {
        String eN = this.Am.eN();
        if (eN != null) {
            Log.m285d(this.TAG, "getAbsoluteUrl : baseURL is " + eN);
            return eN + cG();
        }
        Log.m285d(this.TAG, "getAbsoluteUrl : baseURL is null");
        return null;
    }

    protected W m841e(int i, String str) {
        ErrorResponseData errorResponseData;
        try {
            errorResponseData = (ErrorResponseData) this.Al.fromJson(str, ErrorResponseData.class);
        } catch (Throwable e) {
            Log.m284c(this.TAG, e.getMessage(), e);
            errorResponseData = null;
        }
        return new Response(errorResponseData, null, i);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected java.lang.String aT(java.lang.String r7) {
        /*
        r6 = this;
        r2 = 0;
        r0 = r6.TAG;
        r1 = "writeStringToFile";
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);
        r0 = r6.eC();
        r1 = r6.TAG;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "File = ";
        r3 = r3.append(r4);
        r3 = r3.append(r0);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r3);
        r3 = new java.io.BufferedWriter;	 Catch:{ Exception -> 0x005e }
        r1 = new java.io.FileWriter;	 Catch:{ Exception -> 0x005e }
        r1.<init>(r0);	 Catch:{ Exception -> 0x005e }
        r3.<init>(r1);	 Catch:{ Exception -> 0x005e }
        r1 = 0;
        r3.write(r7);	 Catch:{ Throwable -> 0x0050, all -> 0x006a }
        r0 = r0.getAbsolutePath();	 Catch:{ Throwable -> 0x0050, all -> 0x006a }
        if (r3 == 0) goto L_0x003d;
    L_0x0038:
        if (r2 == 0) goto L_0x004c;
    L_0x003a:
        r3.close();	 Catch:{ Throwable -> 0x003e }
    L_0x003d:
        return r0;
    L_0x003e:
        r2 = move-exception;
        r1.addSuppressed(r2);	 Catch:{ Exception -> 0x0043 }
        goto L_0x003d;
    L_0x0043:
        r1 = move-exception;
    L_0x0044:
        r1 = r6.TAG;
        r2 = "Exception when writing string to file";
        com.samsung.android.spayfw.p002b.Log.m286e(r1, r2);
        goto L_0x003d;
    L_0x004c:
        r3.close();	 Catch:{ Exception -> 0x0043 }
        goto L_0x003d;
    L_0x0050:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0052 }
    L_0x0052:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
    L_0x0056:
        if (r3 == 0) goto L_0x005d;
    L_0x0058:
        if (r1 == 0) goto L_0x0066;
    L_0x005a:
        r3.close();	 Catch:{ Throwable -> 0x0061 }
    L_0x005d:
        throw r0;	 Catch:{ Exception -> 0x005e }
    L_0x005e:
        r0 = move-exception;
        r0 = r2;
        goto L_0x0044;
    L_0x0061:
        r3 = move-exception;
        r1.addSuppressed(r3);	 Catch:{ Exception -> 0x005e }
        goto L_0x005d;
    L_0x0066:
        r3.close();	 Catch:{ Exception -> 0x005e }
        goto L_0x005d;
    L_0x006a:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0056;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.remoteservice.Request.aT(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void m837a(com.samsung.android.spayfw.remoteservice.models.Art[] r11) {
        /*
        r10 = this;
        if (r11 != 0) goto L_0x0003;
    L_0x0002:
        return;
    L_0x0003:
        r4 = new com.samsung.android.spayfw.utils.f;
        r4.<init>();
        r0 = r10.Am;
        r1 = "CardArts";
        r5 = r0.aU(r1);
        r6 = r11.length;
        r0 = 0;
        r3 = r0;
    L_0x0013:
        if (r3 >= r6) goto L_0x0002;
    L_0x0015:
        r0 = r11[r3];
        r1 = new java.util.Random;
        r1.<init>();
        r1 = r1.nextInt();
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r7 = "cardArts_";
        r2 = r2.append(r7);
        r8 = java.lang.System.currentTimeMillis();
        r2 = r2.append(r8);
        r1 = java.lang.String.valueOf(r1);
        r1 = r2.append(r1);
        r1 = r1.toString();
        r7 = new java.io.File;
        r7.<init>(r5, r1);
        r1 = r0.getContent();
        if (r1 == 0) goto L_0x0054;
    L_0x004a:
        r1 = r0.getContent();
        r1 = r1.isEmpty();
        if (r1 == 0) goto L_0x0149;
    L_0x0054:
        r1 = r10.TAG;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r8 = "Fetching Card Art for Url: ";
        r2 = r2.append(r8);
        r8 = r0.getUrl();
        r2 = r2.append(r8);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);
        r1 = r0.getUrl();
        if (r1 == 0) goto L_0x00b1;
    L_0x0076:
        r1 = r0.getUrl();
        r1 = r4.m1271b(r1, r7);
        if (r1 == 0) goto L_0x00b6;
    L_0x0080:
        r1 = r10.TAG;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r8 = "Card art Successfully retrieved and stored in ";
        r2 = r2.append(r8);
        r8 = r7.getAbsolutePath();
        r2 = r2.append(r8);
        r8 = " Size: ";
        r2 = r2.append(r8);
        r8 = r7.length();
        r2 = r2.append(r8);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);
        r1 = r7.getAbsolutePath();
        r0.setLocalFilePath(r1);
    L_0x00b1:
        r0 = r3 + 1;
        r3 = r0;
        goto L_0x0013;
    L_0x00b6:
        r1 = r10.TAG;
        r2 = "Trying old method for fetching card arts ";
        com.samsung.android.spayfw.p002b.Log.m286e(r1, r2);
        r1 = r0.getUrl();
        r1 = r4.m1270a(r1, r7);
        if (r1 == 0) goto L_0x00b1;
    L_0x00c7:
        r2 = r1.isSuccessful();
        if (r2 == 0) goto L_0x00ff;
    L_0x00cd:
        r1 = r10.TAG;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r8 = "Card art Successfully retrieved and stored in ";
        r2 = r2.append(r8);
        r8 = r7.getAbsolutePath();
        r2 = r2.append(r8);
        r8 = " Size: ";
        r2 = r2.append(r8);
        r8 = r7.length();
        r2 = r2.append(r8);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);
        r1 = r7.getAbsolutePath();
        r0.setLocalFilePath(r1);
        goto L_0x00b1;
    L_0x00ff:
        r2 = r10.TAG;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Failed retrieving card art data : ";
        r3 = r3.append(r4);
        r4 = r1.code();
        r3 = r3.append(r4);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);
        r7.delete();
        r2 = new java.io.IOException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Failed Retrieving Card Data from ";
        r3 = r3.append(r4);
        r0 = r0.getUrl();
        r0 = r3.append(r0);
        r3 = " : ";
        r0 = r0.append(r3);
        r1 = r1.code();
        r0 = r0.append(r1);
        r0 = r0.toString();
        r2.<init>(r0);
        throw r2;
    L_0x0149:
        r1 = r10.TAG;
        r2 = "Fetching Card Art for Content";
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);
        r1 = r0.getContent();
        if (r1 == 0) goto L_0x00b1;
    L_0x0156:
        r1 = r0.getContent();
        r1 = r1.isEmpty();
        if (r1 != 0) goto L_0x00b1;
    L_0x0160:
        r2 = 0;
        r1 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x018c, all -> 0x01aa }
        r1.<init>(r7);	 Catch:{ IOException -> 0x018c, all -> 0x01aa }
        r2 = r0.getContent();	 Catch:{ IOException -> 0x01bf }
        r8 = 2;
        r2 = android.util.Base64.decode(r2, r8);	 Catch:{ IOException -> 0x01bf }
        r1.write(r2);	 Catch:{ IOException -> 0x01bf }
        r2 = r7.getAbsolutePath();	 Catch:{ IOException -> 0x01bf }
        r0.setLocalFilePath(r2);	 Catch:{ IOException -> 0x01bf }
        if (r1 == 0) goto L_0x00b1;
    L_0x017b:
        r1.close();	 Catch:{ IOException -> 0x0180 }
        goto L_0x00b1;
    L_0x0180:
        r0 = move-exception;
        r1 = r10.TAG;
        r2 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);
        goto L_0x00b1;
    L_0x018c:
        r0 = move-exception;
        r1 = r2;
    L_0x018e:
        r2 = r10.TAG;	 Catch:{ all -> 0x01bd }
        r7 = r0.getMessage();	 Catch:{ all -> 0x01bd }
        com.samsung.android.spayfw.p002b.Log.m284c(r2, r7, r0);	 Catch:{ all -> 0x01bd }
        if (r1 == 0) goto L_0x00b1;
    L_0x0199:
        r1.close();	 Catch:{ IOException -> 0x019e }
        goto L_0x00b1;
    L_0x019e:
        r0 = move-exception;
        r1 = r10.TAG;
        r2 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);
        goto L_0x00b1;
    L_0x01aa:
        r0 = move-exception;
        r1 = r2;
    L_0x01ac:
        if (r1 == 0) goto L_0x01b1;
    L_0x01ae:
        r1.close();	 Catch:{ IOException -> 0x01b2 }
    L_0x01b1:
        throw r0;
    L_0x01b2:
        r1 = move-exception;
        r2 = r10.TAG;
        r3 = r1.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r2, r3, r1);
        goto L_0x01b1;
    L_0x01bd:
        r0 = move-exception;
        goto L_0x01ac;
    L_0x01bf:
        r0 = move-exception;
        goto L_0x018e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.remoteservice.Request.a(com.samsung.android.spayfw.remoteservice.models.Art[]):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void m840b(java.util.List<com.samsung.android.spayfw.remoteservice.models.Eula> r11) {
        /*
        r10 = this;
        if (r11 != 0) goto L_0x000a;
    L_0x0002:
        r0 = r10.TAG;
        r1 = "eulas null";
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);
    L_0x0009:
        return;
    L_0x000a:
        r3 = new com.samsung.android.spayfw.utils.f;
        r3.<init>();
        r0 = r10.Am;
        r1 = "Eula";
        r4 = r0.aU(r1);
        r5 = r11.iterator();
    L_0x001b:
        r0 = r5.hasNext();
        if (r0 == 0) goto L_0x0009;
    L_0x0021:
        r0 = r5.next();
        r0 = (com.samsung.android.spayfw.remoteservice.models.Eula) r0;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tnc_";
        r1 = r1.append(r2);
        r6 = java.lang.System.currentTimeMillis();
        r1 = r1.append(r6);
        r1 = r1.toString();
        r6 = new java.io.File;
        r6.<init>(r4, r1);
        r1 = r0.getContent();
        if (r1 == 0) goto L_0x0053;
    L_0x0049:
        r1 = r0.getContent();
        r1 = r1.isEmpty();
        if (r1 == 0) goto L_0x0119;
    L_0x0053:
        r1 = r10.TAG;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r7 = "Fetching Eula for Url : ";
        r2 = r2.append(r7);
        r7 = r0.getUrl();
        r2 = r2.append(r7);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);
        r1 = r0.getUrl();
        if (r1 == 0) goto L_0x001b;
    L_0x0075:
        r1 = r0.getUrl();
        r1 = r1.isEmpty();
        if (r1 != 0) goto L_0x001b;
    L_0x007f:
        r1 = r0.getUrl();	 Catch:{ Exception -> 0x00c2 }
        r1 = r3.m1270a(r1, r6);	 Catch:{ Exception -> 0x00c2 }
        if (r1 == 0) goto L_0x001b;
    L_0x0089:
        r2 = r1.isSuccessful();	 Catch:{ Exception -> 0x00c2 }
        if (r2 == 0) goto L_0x00ea;
    L_0x008f:
        r1 = r10.TAG;	 Catch:{ Exception -> 0x00c2 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c2 }
        r2.<init>();	 Catch:{ Exception -> 0x00c2 }
        r7 = "Eula Successfully retrieved and stored in ";
        r2 = r2.append(r7);	 Catch:{ Exception -> 0x00c2 }
        r7 = r6.getAbsolutePath();	 Catch:{ Exception -> 0x00c2 }
        r2 = r2.append(r7);	 Catch:{ Exception -> 0x00c2 }
        r7 = " Size: ";
        r2 = r2.append(r7);	 Catch:{ Exception -> 0x00c2 }
        r8 = r6.length();	 Catch:{ Exception -> 0x00c2 }
        r2 = r2.append(r8);	 Catch:{ Exception -> 0x00c2 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c2 }
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);	 Catch:{ Exception -> 0x00c2 }
        r1 = r6.getAbsolutePath();	 Catch:{ Exception -> 0x00c2 }
        r0.setLocalFilePath(r1);	 Catch:{ Exception -> 0x00c2 }
        goto L_0x001b;
    L_0x00c2:
        r1 = move-exception;
        r2 = r10.TAG;
        r6 = r1.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r2, r6, r1);
        r1 = r10.TAG;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r6 = "Failed Retrieving Eula Data from ";
        r2 = r2.append(r6);
        r0 = r0.getUrl();
        r0 = r2.append(r0);
        r0 = r0.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r1, r0);
        goto L_0x001b;
    L_0x00ea:
        r2 = r10.TAG;	 Catch:{ Exception -> 0x00c2 }
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c2 }
        r7.<init>();	 Catch:{ Exception -> 0x00c2 }
        r8 = "Failed Retrieving Eula Data from ";
        r7 = r7.append(r8);	 Catch:{ Exception -> 0x00c2 }
        r8 = r0.getUrl();	 Catch:{ Exception -> 0x00c2 }
        r7 = r7.append(r8);	 Catch:{ Exception -> 0x00c2 }
        r8 = " : ";
        r7 = r7.append(r8);	 Catch:{ Exception -> 0x00c2 }
        r1 = r1.code();	 Catch:{ Exception -> 0x00c2 }
        r1 = r7.append(r1);	 Catch:{ Exception -> 0x00c2 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x00c2 }
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r1);	 Catch:{ Exception -> 0x00c2 }
        r6.delete();	 Catch:{ Exception -> 0x00c2 }
        goto L_0x001b;
    L_0x0119:
        r1 = r10.TAG;
        r2 = "Fetching Eula for Content";
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);
        r2 = 0;
        r1 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x014c, all -> 0x016a }
        r1.<init>(r6);	 Catch:{ IOException -> 0x014c, all -> 0x016a }
        r2 = r0.getContent();	 Catch:{ IOException -> 0x017f }
        r7 = 2;
        r2 = android.util.Base64.decode(r2, r7);	 Catch:{ IOException -> 0x017f }
        r1.write(r2);	 Catch:{ IOException -> 0x017f }
        r2 = r6.getAbsolutePath();	 Catch:{ IOException -> 0x017f }
        r0.setLocalFilePath(r2);	 Catch:{ IOException -> 0x017f }
        if (r1 == 0) goto L_0x001b;
    L_0x013b:
        r1.close();	 Catch:{ IOException -> 0x0140 }
        goto L_0x001b;
    L_0x0140:
        r0 = move-exception;
        r1 = r10.TAG;
        r2 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);
        goto L_0x001b;
    L_0x014c:
        r0 = move-exception;
        r1 = r2;
    L_0x014e:
        r2 = r10.TAG;	 Catch:{ all -> 0x017d }
        r6 = r0.getMessage();	 Catch:{ all -> 0x017d }
        com.samsung.android.spayfw.p002b.Log.m284c(r2, r6, r0);	 Catch:{ all -> 0x017d }
        if (r1 == 0) goto L_0x001b;
    L_0x0159:
        r1.close();	 Catch:{ IOException -> 0x015e }
        goto L_0x001b;
    L_0x015e:
        r0 = move-exception;
        r1 = r10.TAG;
        r2 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);
        goto L_0x001b;
    L_0x016a:
        r0 = move-exception;
        r1 = r2;
    L_0x016c:
        if (r1 == 0) goto L_0x0171;
    L_0x016e:
        r1.close();	 Catch:{ IOException -> 0x0172 }
    L_0x0171:
        throw r0;
    L_0x0172:
        r1 = move-exception;
        r2 = r10.TAG;
        r3 = r1.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r2, r3, r1);
        goto L_0x0171;
    L_0x017d:
        r0 = move-exception;
        goto L_0x016c;
    L_0x017f:
        r0 = move-exception;
        goto L_0x014e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.remoteservice.Request.b(java.util.List):void");
    }

    private void eV() {
        if (this.Ao == null) {
            this.Ao = new C05771(this);
        }
    }

    W eW() {
        synchronized (AC) {
            if (AD && this.Ap == null && !this.Au) {
                AE.add(this);
                Log.m287i(this.TAG, "executeInternal : req added in queue : " + getRequestType());
                W w = this.At;
                return w;
            }
            this.An = this.Am.bd(this.Av);
            String eU = eU();
            Log.m287i(this.TAG, "executeInternal : " + getRequestType());
            Log.m285d(this.TAG, "requestUrl : " + eU);
            if (eU == null) {
                Log.m286e(this.TAG, "The requested url is null");
                if (this.Aq == null) {
                    return null;
                }
                this.Aq.m363a(0, null);
                return null;
            }
            URI.create(eU);
            init();
            eX();
            eV();
            Log.m285d(this.TAG, "TR Request : mRequestData" + this.As);
            String j = m842j(this.As);
            Log.m285d(this.TAG, "TR Request : requestDataString" + j);
            this.Aw++;
            this.An.m1156a(this.Ar, eU, j, this.Ao, this.Au);
            return this.At;
        }
    }

    private void eX() {
        if (this.An != null) {
            Log.m287i(this.TAG, "UserAgent : " + this.Av);
            for (String str : this.Ax.keySet()) {
                this.An.be(str);
                Log.m285d(this.TAG, str + " : " + ((String) this.Ax.get(str)));
                this.An.setHeader(str, (String) this.Ax.get(str));
            }
            this.An.setHeader("Accept", "application/json");
            if (this.Ar == RequestMethod.PATCH) {
                this.An.setHeader("Content-Type", "application/json-patch+json");
            } else {
                this.An.setHeader("Content-Type", "application/json");
            }
            this.An.be("Attestation-Blob");
            Log.m285d(this.TAG, "Attestation-Blob : " + this.Ap);
            if (!(this.Ap == null || this.Ap.isEmpty())) {
                this.An.setHeader("Attestation-Blob", this.Ap);
            }
            Log.m285d(this.TAG, "Server-Cert-Id : " + this.AA);
            if (this.AA != null) {
                this.An.setHeader("Server-Cert-Id", this.AA);
            }
            Log.m285d(this.TAG, "Device-Huid : " + this.Ay);
            if (!(this.Ay == null || this.Ay.isEmpty())) {
                this.An.setHeader("Device-Huid", this.Ay);
            }
            Log.m285d(this.TAG, "Device-Hpk : " + this.Az);
            if (!(this.Az == null || this.Az.isEmpty())) {
                this.An.setHeader("Device-Hpk", this.Az);
            }
            this.AB = this.Am.getRequestId();
            Log.m285d(this.TAG, "Request-Id : " + this.AB);
            if (this.AB != null && !this.AB.isEmpty()) {
                this.An.setHeader("Request-Id", this.AB);
                return;
            }
            return;
        }
        Log.m286e(this.TAG, "HTTP CLIENT IS NULL");
    }

    private void m830b(int i, Map<String, List<String>> map, byte[] bArr) {
        ServerCertificates serverCertificates = null;
        Log.m285d(this.TAG, "runUnattestedFlow()");
        String b = m829b((Map) map, "Attestation-Nonce");
        if (b == null || b.isEmpty()) {
            Log.m286e(this.TAG, "Nonce is empty");
            this.Ap = null;
        } else {
            synchronized (AC) {
                if (!AD || this.Au) {
                    AD = true;
                    Log.m287i(this.TAG, "runUnattestedFlow: generating attestation blob");
                    this.Ap = this.Am.bc(b);
                    Log.m287i(this.TAG, "runUnattestedFlow: attestation blob generated");
                    new Timer().schedule(new C05782(this), 1000);
                } else {
                    AE.add(this);
                    Log.m287i(this.TAG, "runUnattestedFlow: req added in queue : " + getRequestType());
                    return;
                }
            }
        }
        if (!(this.Ay == null || this.Az == null)) {
            b = m829b((Map) map, "Device-Ecasd");
            Log.m285d(this.TAG, "casd : " + b);
            if (b == null || b.isEmpty()) {
                Log.m286e(this.TAG, "CASD Certificate Expected but not received. Inform caller.");
                if (this.Aq != null) {
                    this.Aq.m364a(i, b);
                    return;
                }
                return;
            }
            Log.m285d(this.TAG, "mCallback : " + this.Aq);
            if (!(this.Aq == null || this.Aq.m364a(i, b))) {
                Log.m286e(this.TAG, "CASD Certificate Update Failed. Stop Processing.");
                return;
            }
        }
        this.Ay = null;
        this.Az = null;
        b = m829b((Map) map, "Server-Cert-Id");
        Log.m285d(this.TAG, "serverCertsHash : " + b);
        if (b != null && !b.isEmpty()) {
            if (bArr != null && bArr.length > 0) {
                String str = new String(bArr);
                Log.m285d(this.TAG, "TR Response : " + str);
                serverCertificates = (ServerCertificates) this.Al.fromJson(str, ServerCertificates.class);
            }
            Log.m285d(this.TAG, "mCallback : " + this.Aq);
            if (this.Aq != null) {
                this.Aq.m362a(i, serverCertificates, this);
            }
        } else if (this.Ap != null) {
            try {
                eW();
            } catch (Throwable e) {
                Log.m284c(this.TAG, e.getMessage(), e);
            }
        } else {
            Log.m285d(this.TAG, "mCallback : " + this.Aq);
            if (this.Aq != null) {
                this.Aq.m363a(i, null);
            }
        }
    }

    private File eC() {
        return new File(this.Am.aU("CardArts"), "serverResponse_" + System.currentTimeMillis() + String.valueOf(new Random().nextInt()));
    }
}
