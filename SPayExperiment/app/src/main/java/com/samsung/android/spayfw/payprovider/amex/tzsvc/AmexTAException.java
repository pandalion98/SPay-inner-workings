/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.amex.tzsvc;

public class AmexTAException
extends Exception {
    private int errorCode = 9001;

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
            case 983040: {
                return "TZ Communication Error";
            }
            case 983041: {
                return "No Such TZ Command Supported";
            }
            case 983042: {
                return "Invalid Input Buffer";
            }
            case 1: {
                return "Invalid Input Param";
            }
            case 17: {
                return "HMAC calculation failed/ HMAC did not match/Decryption of token data failed";
            }
            case 18: {
                return "Token Data cannot be parsed";
            }
            case 19: {
                return "Decryption Session key or KCV calculation failed";
            }
            case 20: {
                return "KCV mismatch failure";
            }
            case 21: {
                return "Failed creating blobs";
            }
            case 22: {
                return "Failure encrypting blobs";
            }
            case 23: {
                return "Unable to compute response data signature";
            }
            case 24: {
                return "Encryption of request data failed";
            }
            case 25: {
                return "Signature cal of request data failed";
            }
            case 26: {
                return "Decryption of response data failed";
            }
            case 28: {
                return "Parsing failed";
            }
            case 29: {
                return "ATC cannot be returned";
            }
            case 30: {
                return "TID cal failed";
            }
            case 31: {
                return "Blobs cannot be constructed";
            }
            case 32: {
                return "Parsing failed";
            }
            case 33: {
                return "ATC cannot be returned";
            }
            case 34: {
                return "TID cal failed";
            }
            case 35: {
                return "Failed generating crypto";
            }
            case 39: {
                return "Decryption failed";
            }
            case 40: {
                return "Parsing failed";
            }
            case 41: {
                return "Meta data blob cannot be constructed";
            }
            case 36: {
                return "Decryption failed";
            }
            case 37: {
                return "Parsing failed";
            }
            case 38: {
                return "Meta data blob cannot be constructed";
            }
            case 42: {
                return "Decryption failed";
            }
            case 43: {
                return "Parsing failed";
            }
            case 44: {
                return "Meta data blob cannot be constructed";
            }
            case 45: {
                return "User Authentication Failure";
            }
            case 48: 
        }
        return "MST Turn On Failure";
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String toString() {
        return AmexTAException.toString(this.errorCode);
    }
}

