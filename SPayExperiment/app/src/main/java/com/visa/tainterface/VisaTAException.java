/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 */
package com.visa.tainterface;

public class VisaTAException
extends Exception {
    private int errorCode;

    public VisaTAException(String string, int n2) {
        super(string);
        this.errorCode = n2;
    }
}

