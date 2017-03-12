package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class UpdateEnrollmentRequestData {
    private String op;
    private String path;
    private String value;

    private UpdateEnrollmentRequestData(String str, String str2) {
        this.op = "replace";
        this.path = str;
        this.value = str2;
    }
}
