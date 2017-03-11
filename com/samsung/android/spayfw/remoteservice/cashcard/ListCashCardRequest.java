package com.samsung.android.spayfw.remoteservice.cashcard;

import com.google.gson.reflect.TypeToken;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.cashcard.models.CashCardInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Collection;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;

/* renamed from: com.samsung.android.spayfw.remoteservice.cashcard.b */
public class ListCashCardRequest extends Request<String, Collection<CashCardInfo>, Response<Collection<CashCardInfo>>, ListCashCardRequest> {
    private String AL;
    private boolean AM;
    private String bN;

    /* renamed from: com.samsung.android.spayfw.remoteservice.cashcard.b.1 */
    class ListCashCardRequest extends TypeToken<Collection<CashCardInfo>> {
        final /* synthetic */ ListCashCardRequest AN;

        ListCashCardRequest(ListCashCardRequest listCashCardRequest) {
            this.AN = listCashCardRequest;
        }
    }

    protected ListCashCardRequest(CashCardClient cashCardClient, String str) {
        super(cashCardClient, RequestMethod.GET, BuildConfig.FLAVOR);
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Card Id is null or empty in QueryCashCardRequest");
        }
        this.bN = str;
    }

    public void m1179g(boolean z) {
        this.AM = z;
    }

    protected String cG() {
        StringBuilder append = new StringBuilder("/cards").append("?user.id=").append(this.bN);
        if (this.AM) {
            append.append("&isAutoImport=true");
        }
        return append.toString();
    }

    protected String getRequestType() {
        return "ListCashCardRequest";
    }

    protected Response<Collection<CashCardInfo>> m1178b(int i, String str) {
        return new Response(null, (Collection) this.Al.fromJson(str, new ListCashCardRequest(this).getType()), i);
    }

    protected void init() {
        if (this.AL != null && !this.AL.isEmpty()) {
            addHeader("OTP", this.AL);
        }
    }
}
