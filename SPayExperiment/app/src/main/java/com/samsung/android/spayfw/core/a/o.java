/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  com.google.gson.JsonObject
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.Collections
 *  java.util.Comparator
 *  java.util.Iterator
 *  java.util.List
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.core.a;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.p;
import com.samsung.android.spayfw.core.q;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.remoteservice.models.ServerCertificates;
import com.samsung.android.spayfw.remoteservice.tokenrequester.j;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;
import com.samsung.android.spayfw.storage.ServerCertsStorage;
import com.samsung.android.spayfw.storage.TokenRecordStorage;
import com.samsung.android.spayfw.utils.h;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public abstract class o {
    protected a iJ;
    protected TokenRecordStorage jJ;
    protected l lQ;
    protected ServerCertsStorage lh;
    protected final Context mContext;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected o(Context context) {
        this.mContext = context;
        this.iJ = a.a(this.mContext, null);
        try {
            this.jJ = TokenRecordStorage.ae(this.mContext);
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.c("Processor", exception.getMessage(), exception);
            com.samsung.android.spayfw.b.c.e("Processor", "Exception when initializing Dao");
            this.jJ = null;
        }
        this.lQ = l.Q(this.mContext);
        this.lh = ServerCertsStorage.ad(this.mContext);
    }

    private final String a(List<CertificateInfo> list) {
        if (list != null && !list.isEmpty()) {
            Collections.sort(list, (Comparator)new Comparator<CertificateInfo>(){

                public int a(CertificateInfo certificateInfo, CertificateInfo certificateInfo2) {
                    return certificateInfo.getContent().compareTo(certificateInfo2.getContent());
                }

                public /* synthetic */ int compare(Object object, Object object2) {
                    return this.a((CertificateInfo)object, (CertificateInfo)object2);
                }
            });
            StringBuilder stringBuilder = new StringBuilder();
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                stringBuilder.append(((CertificateInfo)iterator.next()).getContent());
            }
            return h.bC(stringBuilder.toString());
        }
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected final String P(String string) {
        if (this.lh == null) {
            com.samsung.android.spayfw.b.c.e("Processor", "Server Certs Db obj is null!");
            return "";
        }
        String string2 = this.a(this.lh.a(ServerCertsStorage.ServerCertsDb.ServerCertsColumn.Cg, string));
        if (string2 != null) return string2;
        return "";
    }

    protected int a(boolean bl, String string, String string2, boolean bl2) {
        if (this.mContext == null) {
            com.samsung.android.spayfw.b.c.e("Processor", "Context is null");
            return -1;
        }
        if (this.lQ == null) {
            com.samsung.android.spayfw.b.c.e("Processor", "Token Requester Client is null");
            return -1;
        }
        if (this.jJ == null) {
            com.samsung.android.spayfw.b.c.e("Processor", "Pay Storage is null");
            return -1;
        }
        if (this.lh == null) {
            com.samsung.android.spayfw.b.c.e("Processor", "Server Certs Storage is null");
            return -1;
        }
        if (bl) {
            if (this.iJ == null) {
                com.samsung.android.spayfw.b.c.e("Processor", "Account is null");
                return -1;
            }
            if (string != null && !string.isEmpty()) {
                c c2 = this.iJ.q(string);
                if (c2 == null) {
                    com.samsung.android.spayfw.b.c.e("Processor", "Card by Enrollment ID is null");
                    return -6;
                }
                if (bl2 && c2.ac() == null) {
                    com.samsung.android.spayfw.b.c.e("Processor", "Card Token by Enrollment ID is null");
                    return -6;
                }
            }
            if (string2 != null && !string2.isEmpty()) {
                c c3 = this.iJ.r(string2);
                if (c3 == null) {
                    com.samsung.android.spayfw.b.c.e("Processor", "Card by Token ID is null");
                    return -6;
                }
                if (bl2 && c3.ac() == null) {
                    com.samsung.android.spayfw.b.c.e("Processor", "Card Token by Token ID is null");
                    return -6;
                }
            }
        }
        return 0;
    }

    public void a(String string, String string2, String string3, String string4, String string5, e e2, boolean bl) {
        TokenReport tokenReport = new TokenReport(string, string2, p.G(string3));
        if (string4 != null) {
            tokenReport.setEvent(string4);
        }
        if (e2 != null && e2.cl() != null) {
            tokenReport.setData(e2.cl());
        }
        if (bl) {
            this.lQ.a(string5, tokenReport).ff();
            return;
        }
        this.lQ.a(string5, tokenReport).fe();
    }

    protected boolean a(c c2, String string) {
        if (c2 == null) {
            com.samsung.android.spayfw.b.c.e("Processor", "Card is null");
            return false;
        }
        if (c2.ac() == null) {
            com.samsung.android.spayfw.b.c.e("Processor", "Card Token is null");
            return false;
        }
        if (c2.ac().getTokenStatus() == null || !c2.ac().getTokenStatus().equals((Object)string)) {
            com.samsung.android.spayfw.b.c.e("Processor", "Card Token Status does not match. Expected(" + string + ") Actual(" + c2.ac().getTokenStatus() + ")");
            return false;
        }
        return true;
    }

    protected boolean a(com.samsung.android.spayfw.storage.models.a a2) {
        if (a2 == null) {
            com.samsung.android.spayfw.b.c.e("Processor", "Record is null");
            return false;
        }
        String string = a2.getTrTokenId();
        com.samsung.android.spayfw.b.c.d("Processor", "TR Token Id : " + string);
        if (string == null) {
            com.samsung.android.spayfw.b.c.e("Processor", "TR Token ID is null");
            return false;
        }
        String string2 = a2.getTokenRefId();
        com.samsung.android.spayfw.b.c.d("Processor", "Token Ref Id : " + string2);
        if (string2 == null) {
            com.samsung.android.spayfw.b.c.i("Processor", "No Record with Token Ref Id");
            return false;
        }
        String string3 = a2.getCardBrand();
        com.samsung.android.spayfw.b.c.d("Processor", "Card Brand : " + string3);
        if (string3 == null) {
            com.samsung.android.spayfw.b.c.i("Processor", "No Record with Card Brand");
            return false;
        }
        List<com.samsung.android.spayfw.storage.models.a> list = this.jJ.c(TokenRecordStorage.TokenGroup.TokenColumn.Cu, string3);
        if (list != null) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                if (!string2.equals((Object)((com.samsung.android.spayfw.storage.models.a)iterator.next()).getTokenRefId())) continue;
                com.samsung.android.spayfw.b.c.i("Processor", "Existing Record with Token Ref Id");
                return true;
            }
        }
        com.samsung.android.spayfw.b.c.i("Processor", "No Existing Record");
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected final boolean a(String string, c c2, ServerCertificates serverCertificates) {
        int n2 = 0;
        com.samsung.android.spayfw.b.c.d("Processor", "processReceivedCerts: enrollmentId:" + string);
        if (c2 == null || serverCertificates == null || serverCertificates.getCertificates() == null || serverCertificates.getCertificates().length <= 0 || c2.ad() == null) {
            com.samsung.android.spayfw.b.c.e("Processor", "processReceivedCerts: invalid input");
            return false;
        }
        boolean bl = c2.ad().setServerCertificates(serverCertificates.getCertificates());
        if (!bl) {
            com.samsung.android.spayfw.b.c.w("Processor", "getPayNetworkProvider setServerCertificates fail");
            return bl;
        }
        CertificateInfo[] arrcertificateInfo = serverCertificates.getCertificates();
        int n3 = arrcertificateInfo.length;
        while (n2 < n3) {
            CertificateInfo certificateInfo = arrcertificateInfo[n2];
            c c3 = this.iJ.q(string);
            if (c3 == null) {
                com.samsung.android.spayfw.b.c.w("Processor", "getCardByEnrollmentId null");
                return bl;
            }
            String string2 = c3.getCardBrand();
            if (this.lh != null) {
                this.lh.a(string2, certificateInfo);
            } else {
                com.samsung.android.spayfw.b.c.w("Processor", "mServerCertsDb null, cant add server certs to Db");
            }
            ++n2;
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void b(String string, String string2, String string3, String string4, String string5, e e2, boolean bl) {
        TokenReport tokenReport = new TokenReport(string, string2, p.G(string3));
        if (string4 != null) {
            tokenReport.setEvent(string4);
        }
        ErrorReport errorReport = new ErrorReport();
        if (e2 != null) {
            String string6 = p.t(e2.getErrorCode());
            if (string6 == null) {
                string6 = "ERROR-20000";
            }
            errorReport.setCode(string6);
            if (e2.cl() != null) {
                tokenReport.setData(e2.cl());
            }
            if (e2.getErrorMessage() != null) {
                errorReport.setDescription(e2.getErrorMessage());
            }
        } else {
            errorReport.setCode("ERROR-20000");
        }
        tokenReport.setError(errorReport);
        if (bl) {
            this.lQ.a(string5, tokenReport).ff();
            return;
        }
        this.lQ.a(string5, tokenReport).fe();
    }

}

