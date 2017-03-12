package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spaytui.SpayTuiTAController;
import org.bouncycastle.asn1.isismtt.ocsp.RequestedCertificate;
import org.bouncycastle.math.ec.ECCurve;

public class AmexTAException extends Exception {
    private int errorCode;

    public AmexTAException(int i) {
        super(toString(i));
        this.errorCode = -6;
        this.errorCode = i;
    }

    public String toString() {
        return toString(this.errorCode);
    }

    public static String toString(int i) {
        switch (i) {
            case PaymentFramework.RESULT_CODE_FAIL_INVALID_INPUT /*-5*/:
                return "User Authentication Failure";
            case PaymentFramework.RESULT_CODE_FAIL_OPERATION_NOT_ALLOWED /*-4*/:
                return "Invalid Input Param";
            case SpayTuiTAController.ERROR_INVALID_INPUT_PARAMS /*-3*/:
                return "Invalid Input Buffer";
            case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                return "No Such TZ Command Supported";
            case RequestedCertificate.certificate /*-1*/:
                return "TZ Communication Error";
            case ECCurve.COORD_AFFINE /*0*/:
                return "No Error";
            default:
                return "Unknown Error";
        }
    }
}
