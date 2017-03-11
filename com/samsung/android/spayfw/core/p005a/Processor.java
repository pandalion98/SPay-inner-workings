package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import com.samsung.android.spayfw.core.Account;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.StatusCodeTranslator;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.remoteservice.models.ServerCertificates;
import com.samsung.android.spayfw.remoteservice.tokenrequester.TokenRequesterClient;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;
import com.samsung.android.spayfw.storage.ServerCertsStorage;
import com.samsung.android.spayfw.storage.ServerCertsStorage.ServerCertsDb.ServerCertsColumn;
import com.samsung.android.spayfw.storage.TokenRecordStorage;
import com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.core.a.o */
public abstract class Processor {
    protected Account iJ;
    protected TokenRecordStorage jJ;
    protected TokenRequesterClient lQ;
    protected ServerCertsStorage lh;
    protected final Context mContext;

    /* renamed from: com.samsung.android.spayfw.core.a.o.1 */
    class Processor implements Comparator<CertificateInfo> {
        final /* synthetic */ Processor lR;

        Processor(Processor processor) {
            this.lR = processor;
        }

        public /* synthetic */ int compare(Object obj, Object obj2) {
            return m477a((CertificateInfo) obj, (CertificateInfo) obj2);
        }

        public int m477a(CertificateInfo certificateInfo, CertificateInfo certificateInfo2) {
            return certificateInfo.getContent().compareTo(certificateInfo2.getContent());
        }
    }

    protected Processor(Context context) {
        this.mContext = context;
        this.iJ = Account.m551a(this.mContext, null);
        try {
            this.jJ = TokenRecordStorage.ae(this.mContext);
        } catch (Throwable e) {
            Log.m284c("Processor", e.getMessage(), e);
            Log.m286e("Processor", "Exception when initializing Dao");
            this.jJ = null;
        }
        this.lQ = TokenRequesterClient.m1126Q(this.mContext);
        this.lh = ServerCertsStorage.ad(this.mContext);
    }

    public void m331a(String str, String str2, String str3, String str4, String str5, ProviderResponseData providerResponseData, boolean z) {
        TokenReport tokenReport = new TokenReport(str, str2, StatusCodeTranslator.m659G(str3));
        if (str4 != null) {
            tokenReport.setEvent(str4);
        }
        if (!(providerResponseData == null || providerResponseData.cl() == null)) {
            tokenReport.setData(providerResponseData.cl());
        }
        if (z) {
            this.lQ.m1135a(str5, tokenReport).ff();
        } else {
            this.lQ.m1135a(str5, tokenReport).fe();
        }
    }

    public void m335b(String str, String str2, String str3, String str4, String str5, ProviderResponseData providerResponseData, boolean z) {
        TokenReport tokenReport = new TokenReport(str, str2, StatusCodeTranslator.m659G(str3));
        if (str4 != null) {
            tokenReport.setEvent(str4);
        }
        ErrorReport errorReport = new ErrorReport();
        if (providerResponseData != null) {
            String t = StatusCodeTranslator.m661t(providerResponseData.getErrorCode());
            if (t == null) {
                t = TokenReport.ERROR_PAYMENT_FRAMEWORK;
            }
            errorReport.setCode(t);
            if (providerResponseData.cl() != null) {
                tokenReport.setData(providerResponseData.cl());
            }
            if (providerResponseData.getErrorMessage() != null) {
                errorReport.setDescription(providerResponseData.getErrorMessage());
            }
        } else {
            errorReport.setCode(TokenReport.ERROR_PAYMENT_FRAMEWORK);
        }
        tokenReport.setError(errorReport);
        if (z) {
            this.lQ.m1135a(str5, tokenReport).ff();
        } else {
            this.lQ.m1135a(str5, tokenReport).fe();
        }
    }

    private final String m328a(List<CertificateInfo> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        Collections.sort(list, new Processor(this));
        StringBuilder stringBuilder = new StringBuilder();
        for (CertificateInfo content : list) {
            stringBuilder.append(content.getContent());
        }
        return Utils.bC(stringBuilder.toString());
    }

    protected final String m329P(String str) {
        if (this.lh == null) {
            Log.m286e("Processor", "Server Certs Db obj is null!");
            return BuildConfig.FLAVOR;
        }
        String a = m328a(this.lh.m1220a(ServerCertsColumn.CARD_TYPE, str));
        return a == null ? BuildConfig.FLAVOR : a;
    }

    protected final boolean m334a(String str, Card card, ServerCertificates serverCertificates) {
        int i = 0;
        Log.m285d("Processor", "processReceivedCerts: enrollmentId:" + str);
        if (card == null || serverCertificates == null || serverCertificates.getCertificates() == null || serverCertificates.getCertificates().length <= 0 || card.ad() == null) {
            Log.m286e("Processor", "processReceivedCerts: invalid input");
            return false;
        }
        boolean serverCertificates2 = card.ad().setServerCertificates(serverCertificates.getCertificates());
        if (serverCertificates2) {
            CertificateInfo[] certificates = serverCertificates.getCertificates();
            int length = certificates.length;
            while (i < length) {
                CertificateInfo certificateInfo = certificates[i];
                Card q = this.iJ.m558q(str);
                if (q == null) {
                    Log.m290w("Processor", "getCardByEnrollmentId null");
                    return serverCertificates2;
                }
                String cardBrand = q.getCardBrand();
                if (this.lh != null) {
                    this.lh.m1219a(cardBrand, certificateInfo);
                } else {
                    Log.m290w("Processor", "mServerCertsDb null, cant add server certs to Db");
                }
                i++;
            }
            return serverCertificates2;
        }
        Log.m290w("Processor", "getPayNetworkProvider setServerCertificates fail");
        return serverCertificates2;
    }

    protected int m330a(boolean z, String str, String str2, boolean z2) {
        if (this.mContext == null) {
            Log.m286e("Processor", "Context is null");
            return -1;
        } else if (this.lQ == null) {
            Log.m286e("Processor", "Token Requester Client is null");
            return -1;
        } else if (this.jJ == null) {
            Log.m286e("Processor", "Pay Storage is null");
            return -1;
        } else if (this.lh == null) {
            Log.m286e("Processor", "Server Certs Storage is null");
            return -1;
        } else {
            if (z) {
                if (this.iJ == null) {
                    Log.m286e("Processor", "Account is null");
                    return -1;
                }
                Card q;
                if (!(str == null || str.isEmpty())) {
                    q = this.iJ.m558q(str);
                    if (q == null) {
                        Log.m286e("Processor", "Card by Enrollment ID is null");
                        return -6;
                    } else if (z2 && q.ac() == null) {
                        Log.m286e("Processor", "Card Token by Enrollment ID is null");
                        return -6;
                    }
                }
                if (!(str2 == null || str2.isEmpty())) {
                    q = this.iJ.m559r(str2);
                    if (q == null) {
                        Log.m286e("Processor", "Card by Token ID is null");
                        return -6;
                    } else if (z2 && q.ac() == null) {
                        Log.m286e("Processor", "Card Token by Token ID is null");
                        return -6;
                    }
                }
            }
            return 0;
        }
    }

    protected boolean m332a(Card card, String str) {
        if (card == null) {
            Log.m286e("Processor", "Card is null");
            return false;
        } else if (card.ac() == null) {
            Log.m286e("Processor", "Card Token is null");
            return false;
        } else if (card.ac().getTokenStatus() != null && card.ac().getTokenStatus().equals(str)) {
            return true;
        } else {
            Log.m286e("Processor", "Card Token Status does not match. Expected(" + str + ") Actual(" + card.ac().getTokenStatus() + ")");
            return false;
        }
    }

    protected boolean m333a(TokenRecord tokenRecord) {
        if (tokenRecord == null) {
            Log.m286e("Processor", "Record is null");
            return false;
        }
        String trTokenId = tokenRecord.getTrTokenId();
        Log.m285d("Processor", "TR Token Id : " + trTokenId);
        if (trTokenId == null) {
            Log.m286e("Processor", "TR Token ID is null");
            return false;
        }
        String tokenRefId = tokenRecord.getTokenRefId();
        Log.m285d("Processor", "Token Ref Id : " + tokenRefId);
        if (tokenRefId == null) {
            Log.m287i("Processor", "No Record with Token Ref Id");
            return false;
        }
        trTokenId = tokenRecord.getCardBrand();
        Log.m285d("Processor", "Card Brand : " + trTokenId);
        if (trTokenId == null) {
            Log.m287i("Processor", "No Record with Card Brand");
            return false;
        }
        List<TokenRecord> c = this.jJ.m1228c(TokenColumn.CARD_BRAND, trTokenId);
        if (c != null) {
            for (TokenRecord tokenRefId2 : c) {
                if (tokenRefId.equals(tokenRefId2.getTokenRefId())) {
                    Log.m287i("Processor", "Existing Record with Token Ref Id");
                    return true;
                }
            }
        }
        Log.m287i("Processor", "No Existing Record");
        return false;
    }
}
