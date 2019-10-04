/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class UpdateEnrollmentRequestData {
    private String op = "replace";
    private String path;
    private String value;

    private UpdateEnrollmentRequestData(String string, String string2) {
        this.path = string;
        this.value = string2;
    }
}

