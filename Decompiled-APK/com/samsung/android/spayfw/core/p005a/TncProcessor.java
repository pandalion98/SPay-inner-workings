package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.RequestDataBuilder;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.tokenrequester.ProvisionRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.ProvisionResponse;
import com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import com.samsung.android.spaytui.SpayTuiTAController;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.core.a.u */
public class TncProcessor extends Processor {
    protected String mEnrollmentId;
    protected ICommonCallback ml;
    protected boolean mm;

    /* renamed from: com.samsung.android.spayfw.core.a.u.a */
    private class TncProcessor extends C0413a<ProvisionResponse, ProvisionRequest> {
        String mEnrollmentId;
        ICommonCallback ml;
        final /* synthetic */ TncProcessor mn;

        public TncProcessor(TncProcessor tncProcessor, String str, ICommonCallback iCommonCallback) {
            this.mn = tncProcessor;
            this.mEnrollmentId = str;
            this.ml = iCommonCallback;
        }

        public void m517a(int i, ProvisionResponse provisionResponse) {
            int i2 = 0;
            Log.m287i("TncProcessor", "Tnc reject to TR server: onRequestComplete:  " + i);
            switch (i) {
                case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                    i2 = PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID;
                    break;
                case ECCurve.COORD_AFFINE /*0*/:
                case 503:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE;
                    break;
                case 202:
                case 205:
                    break;
                case 500:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL;
                    break;
                default:
                    i2 = -1;
                    break;
            }
            if (i2 == 0) {
                try {
                    if (this.mn.jJ.bp(this.mEnrollmentId) != null) {
                        this.mn.jJ.m1229d(TokenColumn.ENROLLMENT_ID, this.mEnrollmentId);
                        Log.m285d("TncProcessor", "TncCallback: db recored deleted for : " + this.mEnrollmentId);
                    }
                    this.mn.iJ.m560s(this.mEnrollmentId);
                    this.ml.onSuccess(this.mEnrollmentId);
                    return;
                } catch (Throwable e) {
                    Log.m284c("TncProcessor", e.getMessage(), e);
                    return;
                }
            }
            this.ml.onFail(this.mEnrollmentId, i2);
        }
    }

    public TncProcessor(Context context, String str, boolean z, ICommonCallback iCommonCallback) {
        super(context);
        this.mEnrollmentId = str;
        this.mm = z;
        this.ml = iCommonCallback;
    }

    public void process() {
        if (this.mEnrollmentId != null && this.ml != null && this.iJ != null) {
            Card q = this.iJ.m558q(this.mEnrollmentId);
            TokenRecord bp = this.jJ.bp(this.mEnrollmentId);
            if (q == null || bp == null) {
                if (q == null) {
                    Log.m286e("TncProcessor", "acceptTnc Failed - Invalid Enrollment Id");
                } else {
                    Log.m286e("TncProcessor", "acceptTnc Failed - unable to find Enrollment Id from db");
                }
                try {
                    this.ml.onFail(this.mEnrollmentId, -6);
                } catch (Throwable e) {
                    Log.m284c("TncProcessor", e.getMessage(), e);
                }
            } else if (this.mm) {
                bp.m1252b(System.currentTimeMillis());
                this.jJ.m1230d(bp);
                try {
                    this.ml.onSuccess(this.mEnrollmentId);
                } catch (Throwable e2) {
                    Log.m284c("TncProcessor", e2.getMessage(), e2);
                }
            } else {
                this.lQ.m1130a(Card.m574y(q.getCardBrand()), RequestDataBuilder.m625a(-1, this.mEnrollmentId, null, null)).m836a(new TncProcessor(this, this.mEnrollmentId, this.ml));
            }
        } else if (this.ml != null) {
            try {
                Log.m286e("TncProcessor", "acceptTnc Failed - Invalid inputs");
                this.ml.onFail(this.mEnrollmentId, -5);
            } catch (Throwable e22) {
                Log.m284c("TncProcessor", e22.getMessage(), e22);
            }
        }
    }
}
