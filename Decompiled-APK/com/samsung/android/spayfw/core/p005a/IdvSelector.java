package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import com.samsung.android.spayfw.appinterface.ISelectIdvCallback;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.SelectIdvResponse;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.RequestDataBuilder;
import com.samsung.android.spayfw.core.ResponseDataBuilder;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.SelectIdvRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvSelectionResponseData;
import com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn;
import com.samsung.android.spaytui.SpayTuiTAController;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.core.a.j */
public class IdvSelector extends Processor {
    protected ISelectIdvCallback lo;
    protected IdvMethod lp;
    protected String mEnrollmentId;

    /* renamed from: com.samsung.android.spayfw.core.a.j.a */
    private class IdvSelector extends C0413a<Response<IdvSelectionResponseData>, SelectIdvRequest> {
        ISelectIdvCallback lo;
        final /* synthetic */ IdvSelector lq;
        String mEnrollmentId;

        public IdvSelector(IdvSelector idvSelector, String str, ISelectIdvCallback iSelectIdvCallback) {
            this.lq = idvSelector;
            this.mEnrollmentId = str;
            this.lo = iSelectIdvCallback;
        }

        public void m425a(int i, Response<IdvSelectionResponseData> response) {
            int i2 = -1;
            int i3 = -17;
            Log.m285d("IdvSelector", "SelectIdv : onRequestComplete:  " + i);
            if (this.lq.iJ.m558q(this.mEnrollmentId) == null) {
                Log.m286e("IdvSelector", "selectIdv Failed - unable to find the card in memory. ");
                try {
                    this.lo.onFail(this.mEnrollmentId, -6);
                    return;
                } catch (Throwable e) {
                    Log.m284c("IdvSelector", e.getMessage(), e);
                    return;
                }
            }
            SelectIdvResponse selectIdvResponse;
            switch (i) {
                case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                    i2 = PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID;
                    selectIdvResponse = null;
                    break;
                case ECCurve.COORD_AFFINE /*0*/:
                case 503:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE;
                    selectIdvResponse = null;
                    break;
                case 202:
                    if (response == null) {
                        Log.m286e("IdvSelector", "IdvSelectionResponseData is null");
                        i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_OUTPUT_INVALID;
                        selectIdvResponse = null;
                        break;
                    }
                    selectIdvResponse = ResponseDataBuilder.m631a((IdvSelectionResponseData) response.getResult());
                    i2 = 0;
                    break;
                case 205:
                    this.lq.iJ.m560s(this.mEnrollmentId);
                    this.lq.jJ.m1229d(TokenColumn.ENROLLMENT_ID, this.mEnrollmentId);
                    i2 = -8;
                    selectIdvResponse = null;
                    break;
                case 400:
                    ErrorResponseData fa = response.fa();
                    if (fa != null && fa.getCode() != null) {
                        if (ErrorResponseData.ERROR_CODE_PERMISSION_REQUIRED.equals(fa.getCode())) {
                            i3 = PaymentFramework.RESULT_CODE_FAIL_SERVER_REJECT;
                        } else {
                            i3 = -1;
                        }
                        i2 = i3;
                        selectIdvResponse = null;
                        break;
                    }
                    selectIdvResponse = null;
                    break;
                    break;
                case 403:
                case 407:
                    ErrorResponseData fa2 = response.fa();
                    if (fa2 != null && fa2.getCode() != null) {
                        String code = fa2.getCode();
                        if (!(ErrorResponseData.ERROR_CODE_INVALID_ACTIVATION_DATA.equals(code) || ErrorResponseData.ERROR_CODE_UNKNOWN_ACTIVATION_DATA.equals(code))) {
                            if (ErrorResponseData.ERROR_CODE_MISSING_ACTIVATION_DATA.equals(code)) {
                                i3 = -16;
                            } else if (ErrorResponseData.ERROR_CODE_ACTIVATION_EXPIRED.equals(code)) {
                                i3 = -18;
                            } else if (ErrorResponseData.ERROR_CODE_ACTIVATION_EXCEEDED.equals(code)) {
                                i3 = -19;
                            } else if (ErrorResponseData.ERROR_CODE_ACTIVATION_SELECTION_EXCEEDED.equals(code)) {
                                i3 = -20;
                            } else if (ErrorResponseData.ERROR_CODE_ACTIVATION_EXPIRED_AND_SELECTION_EXCEEDED.equals(code)) {
                                i3 = -21;
                            }
                        }
                        i2 = i3;
                        selectIdvResponse = null;
                        break;
                    }
                    i2 = -17;
                    selectIdvResponse = null;
                    break;
                case 404:
                case 500:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL;
                    selectIdvResponse = null;
                    break;
                default:
                    selectIdvResponse = null;
                    break;
            }
            if (i2 == 0) {
                try {
                    this.lo.onSuccess(this.mEnrollmentId, selectIdvResponse);
                    return;
                } catch (Throwable e2) {
                    Log.m284c("IdvSelector", e2.getMessage(), e2);
                    return;
                }
            }
            this.lo.onFail(this.mEnrollmentId, i2);
        }
    }

    public IdvSelector(Context context, String str, IdvMethod idvMethod, ISelectIdvCallback iSelectIdvCallback) {
        super(context);
        this.mEnrollmentId = str;
        this.lp = idvMethod;
        this.lo = iSelectIdvCallback;
    }

    public void process() {
        Log.m285d("IdvSelector", "selectIdv()");
        if (this.mEnrollmentId == null || this.lo == null || this.iJ == null || this.lp == null || this.lp.getId() == null) {
            int i = -5;
            if (this.iJ == null) {
                Log.m286e("IdvSelector", "selectIdv  - Failed to initialize account");
                i = -1;
            } else {
                Log.m286e("IdvSelector", "selectIdv Failed - Invalid inputs");
            }
            if (this.lo != null) {
                try {
                    this.lo.onFail(this.mEnrollmentId, i);
                    return;
                } catch (Throwable e) {
                    Log.m284c("IdvSelector", e.getMessage(), e);
                    return;
                }
            }
            return;
        }
        Card q = this.iJ.m558q(this.mEnrollmentId);
        if (q == null) {
            Log.m286e("IdvSelector", "selectIdv Failed - unable to find the card in memory. ");
            try {
                this.lo.onFail(this.mEnrollmentId, -6);
            } catch (Throwable e2) {
                Log.m284c("IdvSelector", e2.getMessage(), e2);
            }
        } else if (q.ac() == null || q.ac().getTokenStatus() == null || !TokenStatus.PENDING_PROVISION.equals(q.ac().getTokenStatus())) {
            Log.m286e("IdvSelector", "selectIdv Failed - token is null or token staus is not correct. ");
            if (!(q.ac() == null || q.ac().getTokenStatus() == null)) {
                Log.m286e("IdvSelector", "selectIdv Failed - token status:  " + q.ac().getTokenStatus());
            }
            try {
                this.lo.onFail(this.mEnrollmentId, -4);
            } catch (Throwable e22) {
                Log.m284c("IdvSelector", e22.getMessage(), e22);
            }
        } else {
            this.lQ.m1136a(Card.m574y(q.getCardBrand()), RequestDataBuilder.m625a(System.currentTimeMillis(), this.mEnrollmentId, null, null), this.lp).m836a(new IdvSelector(this, this.mEnrollmentId, this.lo));
        }
    }
}
