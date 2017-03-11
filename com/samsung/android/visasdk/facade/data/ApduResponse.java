package com.samsung.android.visasdk.facade.data;

import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spaytui.SpayTuiTAController;
import com.samsung.android.visasdk.p025c.Log;
import com.samsung.android.visasdk.paywave.Constant;
import com.samsung.android.visasdk.paywave.data.ApduError;
import org.bouncycastle.asn1.isismtt.ocsp.RequestedCertificate;
import org.bouncycastle.math.ec.ECCurve;

public class ApduResponse {
    private static final String TAG = "ApduResponse";
    byte[] apduData;
    ApduError apduError;

    public ApduResponse(int i) {
        this.apduData = constructErrorResponseApdu(i);
        this.apduError = new ApduError(i);
    }

    public ApduResponse(byte[] bArr) {
        this.apduData = bArr;
        this.apduError = new ApduError(0);
    }

    public ApduResponse(byte[] bArr, ApduError apduError) {
        this.apduData = bArr;
        this.apduError = apduError;
    }

    public byte[] getApduData() {
        return this.apduData;
    }

    public ApduError getApduError() {
        return this.apduError;
    }

    public void setApduData(byte[] bArr) {
        this.apduData = bArr;
    }

    public void setApduError(ApduError apduError) {
        this.apduError = apduError;
    }

    byte[] constructErrorResponseApdu(int i) {
        Log.m1300d(TAG, "constructErrorResponseApdu(), errorIndex=" + i);
        switch (i) {
            case PaymentFramework.RESULT_CODE_FAIL_LIMIT_EXCEEDED /*-13*/:
                return Constant.DI;
            case PaymentFramework.RESULT_CODE_FAIL_RESET_IN_PROGRESS /*-12*/:
                return Constant.DH;
            case PaymentFramework.RESULT_CODE_FAIL_INVALID_CARDINPUT /*-11*/:
                return Constant.DG;
            case PaymentFramework.RESULT_CODE_FAIL_INVALID_CARD /*-10*/:
                return Constant.DF;
            case PaymentFramework.RESULT_CODE_FAIL_NO_NETWORK /*-9*/:
                return Constant.DE;
            case PaymentFramework.RESULT_CODE_CONTENT_DELETED /*-8*/:
                return Constant.DD;
            case PaymentFramework.RESULT_CODE_FAIL_USER_CANCEL /*-7*/:
                return Constant.DB;
            case PaymentFramework.RESULT_CODE_FAIL_NOT_FOUND /*-6*/:
                return Constant.DA;
            case PaymentFramework.RESULT_CODE_FAIL_INVALID_INPUT /*-5*/:
                return Constant.Dz;
            case PaymentFramework.RESULT_CODE_FAIL_OPERATION_NOT_ALLOWED /*-4*/:
                return Constant.Dy;
            case SpayTuiTAController.ERROR_INVALID_INPUT_PARAMS /*-3*/:
                return Constant.Dx;
            case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                return Constant.Dw;
            case RequestedCertificate.certificate /*-1*/:
                return Constant.Dv;
            case ECCurve.COORD_AFFINE /*0*/:
                return Constant.Du;
            default:
                return Constant.DJ;
        }
    }
}
