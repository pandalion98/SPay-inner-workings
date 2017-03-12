package com.americanexpress.sdkmodulelib.util;

import com.samsung.android.spayfw.cncc.CNCCTAException;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAException;

public class TAErrorUtils {
    public static boolean isTrustedAppCommunicationError(Exception exception) {
        if (isTAError(exception) && ((AmexTAException) exception).getErrorCode() == CNCCTAException.ERR_TZ_COM_ERR) {
            return true;
        }
        return false;
    }

    public static boolean isTAError(Exception exception) {
        if (exception instanceof AmexTAException) {
            return true;
        }
        return false;
    }

    public static int getErrorCode(Exception exception) {
        if (isTAError(exception)) {
            return ((AmexTAException) exception).getErrorCode();
        }
        return 0;
    }
}
