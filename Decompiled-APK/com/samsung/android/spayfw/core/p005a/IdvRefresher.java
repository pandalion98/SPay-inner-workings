package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import com.samsung.android.spayfw.appinterface.IRefreshIdvCallback;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.ResponseDataBuilder;
import com.samsung.android.spayfw.core.StatusCodeTranslator;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.RefreshIdvRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvOptionsData;
import com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn;
import java.util.List;
import org.bouncycastle.asn1.x509.DisplayText;

/* renamed from: com.samsung.android.spayfw.core.a.i */
public class IdvRefresher extends Processor {
    protected IRefreshIdvCallback lm;
    protected String mEnrollmentId;

    /* renamed from: com.samsung.android.spayfw.core.a.i.a */
    private class IdvRefresher extends C0413a<Response<IdvOptionsData>, RefreshIdvRequest> {
        final /* synthetic */ IdvRefresher ln;

        private IdvRefresher(IdvRefresher idvRefresher) {
            this.ln = idvRefresher;
        }

        public void m423a(int i, Response<IdvOptionsData> response) {
            int i2;
            List list = null;
            Log.m285d("IdvRefresher", "RefreshIdv : onRequestComplete:  " + i);
            int ba = this.ln.ba();
            if (!(ba == 0 || this.ln.lm == null)) {
                try {
                    this.ln.lm.onFail(this.ln.mEnrollmentId, ba);
                } catch (Throwable e) {
                    Log.m284c("IdvRefresher", e.getMessage(), e);
                }
            }
            Card q = this.ln.iJ.m558q(this.ln.mEnrollmentId);
            switch (i) {
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    if (response == null) {
                        Log.m286e("IdvRefresher", "IdvMethod is null");
                        i2 = -3;
                        break;
                    }
                    list = ResponseDataBuilder.m636a(q, (IdvOptionsData) response.getResult());
                    if (list != null) {
                        i2 = ba;
                        break;
                    }
                    Log.m286e("IdvRefresher", "IdvMethod is null");
                    i2 = -3;
                    break;
                case 205:
                    i2 = -8;
                    this.ln.iJ.m560s(this.ln.mEnrollmentId);
                    this.ln.jJ.m1229d(TokenColumn.ENROLLMENT_ID, this.ln.mEnrollmentId);
                    break;
                default:
                    ErrorResponseData fa;
                    if (response != null) {
                        fa = response.fa();
                    } else {
                        fa = null;
                    }
                    i2 = StatusCodeTranslator.m660a(i, fa);
                    break;
            }
            try {
                if (this.ln.lm == null) {
                    return;
                }
                if (i2 == 0) {
                    this.ln.lm.onSuccess(this.ln.mEnrollmentId, list);
                } else {
                    this.ln.lm.onFail(this.ln.mEnrollmentId, i2);
                }
            } catch (Throwable e2) {
                Log.m284c("IdvRefresher", e2.getMessage(), e2);
            }
        }
    }

    public IdvRefresher(Context context, String str, IRefreshIdvCallback iRefreshIdvCallback) {
        super(context);
        this.mEnrollmentId = str;
        this.lm = iRefreshIdvCallback;
    }

    public void process() {
        Log.m285d("IdvRefresher", "refreshIdv()");
        int ba = ba();
        if (ba != 0) {
            Log.m286e("IdvRefresher", "refreshIdv validation failed");
            if (this.lm != null) {
                try {
                    this.lm.onFail(this.mEnrollmentId, ba);
                    return;
                } catch (Throwable e) {
                    Log.m284c("IdvRefresher", e.getMessage(), e);
                    return;
                }
            }
            return;
        }
        Card q = this.iJ.m558q(this.mEnrollmentId);
        if (q == null || q.getCardBrand() == null || q.ac() == null || q.ac().getTokenId() == null) {
            Log.m286e("IdvRefresher", "invalid cardInfo found in the PF");
            if (this.lm != null) {
                try {
                    this.lm.onFail(this.mEnrollmentId, -1);
                    return;
                } catch (Throwable e2) {
                    Log.m284c("IdvRefresher", e2.getMessage(), e2);
                    return;
                }
            }
            return;
        }
        this.lQ.m1142y(Card.m574y(q.getCardBrand()), q.ac().getTokenId()).m836a(new IdvRefresher());
    }

    protected int ba() {
        if (this.mEnrollmentId == null || this.mEnrollmentId.isEmpty() || this.lm == null) {
            Log.m286e("IdvRefresher", "refreshIdv Failed - Invalid inputs");
            return -5;
        }
        int a = super.m330a(true, this.mEnrollmentId, null, true);
        if (a != 0) {
            Log.m286e("IdvRefresher", "refreshIdv Failed - validate failed: " + a);
            return a;
        } else if (this.iJ == null) {
            Log.m286e("IdvRefresher", "refreshIdv Failed - mAccount is null: ");
            return -1;
        } else if (super.m332a(this.iJ.m558q(this.mEnrollmentId), TokenStatus.PENDING_PROVISION)) {
            return 0;
        } else {
            Log.m290w("IdvRefresher", "refreshIdv Failed - Not valid token status");
            return -4;
        }
    }
}
