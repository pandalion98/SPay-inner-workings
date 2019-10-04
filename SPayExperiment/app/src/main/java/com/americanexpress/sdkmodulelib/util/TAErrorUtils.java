/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 */
package com.americanexpress.sdkmodulelib.util;

import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAException;

public class TAErrorUtils {
    public static int getErrorCode(Exception exception) {
        if (TAErrorUtils.isTAError(exception)) {
            return ((AmexTAException)exception).getErrorCode();
        }
        return 0;
    }

    public static boolean isTAError(Exception exception) {
        return exception instanceof AmexTAException;
    }

    public static boolean isTrustedAppCommunicationError(Exception exception) {
        return TAErrorUtils.isTAError(exception) && ((AmexTAException)exception).getErrorCode() == 983040;
    }
}

