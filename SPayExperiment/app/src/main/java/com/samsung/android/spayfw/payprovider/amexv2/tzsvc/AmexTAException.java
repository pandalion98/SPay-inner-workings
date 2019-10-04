/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

public class AmexTAException
extends Exception {
    private int errorCode = -6;

    public AmexTAException(int n2) {
        super(AmexTAException.toString(n2));
        this.errorCode = n2;
    }

    public static String toString(int n2) {
        switch (n2) {
            default: {
                return "Unknown Error";
            }
            case 0: {
                return "No Error";
            }
            case -1: {
                return "TZ Communication Error";
            }
            case -2: {
                return "No Such TZ Command Supported";
            }
            case -3: {
                return "Invalid Input Buffer";
            }
            case -4: {
                return "Invalid Input Param";
            }
            case -5: 
        }
        return "User Authentication Failure";
    }

    public String toString() {
        return AmexTAException.toString(this.errorCode);
    }
}

