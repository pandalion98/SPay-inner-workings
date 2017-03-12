package com.samsung.android.spayfw.core;

import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentResponseData.Card;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;
import com.samsung.android.spaytui.SpayTuiTAController;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.core.p */
public class StatusCodeTranslator {
    private static Map<String, String> kb;
    private static Map<Integer, String> kc;

    static {
        kb = new HashMap();
        kc = new HashMap();
        kc.put(Integer.valueOf(-2), TokenReport.ERROR_PAYMENT_FRAMEWORK);
        kc.put(Integer.valueOf(-9), TokenReport.ERROR_PAYMENT_FRAMEWORK_DATA);
        kc.put(Integer.valueOf(-7), TokenReport.ERROR_PAYMENT_NETWORK_LIBRARY);
        kc.put(Integer.valueOf(-8), TokenReport.ERROR_PAYMENT_NETWORK_LIBRARY_AUTHENTICATION);
        kc.put(Integer.valueOf(-6), TokenReport.ERROR_TRUSTED_APP);
        kb.put(Card.STATUS_PENDING, Card.STATUS_PENDING);
        kb.put(TokenStatus.PENDING_ENROLLED, Card.STATUS_PENDING);
        kb.put(TokenStatus.PENDING_PROVISION, Card.STATUS_PENDING);
        kb.put(TokenStatus.ACTIVE, TokenStatus.ACTIVE);
        kb.put(TokenStatus.SUSPENDED, TokenStatus.SUSPENDED);
        kb.put(TokenStatus.DISPOSED, TokenStatus.DISPOSED);
    }

    public static String m659G(String str) {
        return (String) kb.get(str);
    }

    public static String m661t(int i) {
        return (String) kc.get(Integer.valueOf(i));
    }

    public static int m660a(int i, ErrorResponseData errorResponseData) {
        int i2 = -3;
        switch (i) {
            case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                return PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID;
            case ECCurve.COORD_AFFINE /*0*/:
            case 408:
            case 503:
                return PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE;
            case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
            case 201:
            case 202:
                return 0;
            case 204:
            case 205:
                return PaymentFramework.RESULT_CODE_FAIL_SERVER_REJECT;
            case 400:
                if (errorResponseData == null || errorResponseData.getCode() == null) {
                    return -5;
                }
                String code = errorResponseData.getCode();
                if (ErrorResponseData.ERROR_CODE_PAN_NOT_ELIGIBLE.equals(code)) {
                    i2 = -11;
                } else if (!ErrorResponseData.ERROR_CODE_PAN_ALREADY_ENROLLED.equals(code)) {
                    i2 = -5;
                }
                return i2;
            case 401:
            case 403:
                return -4;
            case 404:
            case 500:
                return PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL;
            case 406:
            case 410:
                return PaymentFramework.RESULT_CODE_FAIL_SERVER_REJECT;
            case 407:
                if (errorResponseData == null || errorResponseData.getCode() == null) {
                    return -17;
                }
                String code2 = errorResponseData.getCode();
                if (ErrorResponseData.ERROR_CODE_INVALID_ACTIVATION_DATA.equals(code2) || ErrorResponseData.ERROR_CODE_UNKNOWN_ACTIVATION_DATA.equals(code2)) {
                    i2 = -17;
                } else if (ErrorResponseData.ERROR_CODE_MISSING_ACTIVATION_DATA.equals(code2)) {
                    i2 = -16;
                } else if (ErrorResponseData.ERROR_CODE_ACTIVATION_EXPIRED.equals(code2)) {
                    i2 = -18;
                } else if (ErrorResponseData.ERROR_CODE_ACTIVATION_EXCEEDED.equals(code2)) {
                    i2 = -19;
                } else if (ErrorResponseData.ERROR_CODE_ACTIVATION_SELECTION_EXCEEDED.equals(code2)) {
                    i2 = -20;
                } else if (ErrorResponseData.ERROR_CODE_ACTIVATION_EXPIRED_AND_SELECTION_EXCEEDED.equals(code2)) {
                    i2 = -21;
                } else {
                    i2 = -17;
                }
                return i2;
            case 409:
                return -3;
            default:
                return -1;
        }
    }
}
