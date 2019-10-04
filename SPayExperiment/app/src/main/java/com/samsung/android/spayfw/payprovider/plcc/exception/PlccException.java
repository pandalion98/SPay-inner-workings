/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.plcc.exception;

public class PlccException
extends Exception {
    public static final String ERROR_INVALID_SEQUENCE = "Invalid Sequence Format";
    public static final String ERROR_NOT_IMPLEMENT = "Not Implement";
    public static final String ERROR_NOT_SELECT_CARD = "Not Select Card";

    public PlccException(String string) {
        super(string);
    }
}

