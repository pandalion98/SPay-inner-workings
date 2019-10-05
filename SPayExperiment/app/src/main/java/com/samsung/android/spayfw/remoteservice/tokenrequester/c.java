/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EnrollmentRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EnrollmentResponseData;

public class c
extends m<EnrollmentRequestData, EnrollmentResponseData, com.samsung.android.spayfw.remoteservice.c<EnrollmentResponseData>, c> {
    protected c(l l2, EnrollmentRequestData enrollmentRequestData) {
        super(l2, Client.HttpRequest.RequestMethod.Ah, enrollmentRequestData);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected com.samsung.android.spayfw.remoteservice.c<EnrollmentResponseData> b(int n2, String string) {
        EnrollmentResponseData enrollmentResponseData = this.Al.fromJson(string, EnrollmentResponseData.class);
        Log.d("EnrollRequest", "EnrollmentResponseData : " + enrollmentResponseData);
        if (enrollmentResponseData != null) {
            this.b(enrollmentResponseData.getEulas());
            do {
                return new com.samsung.android.spayfw.remoteservice.c<EnrollmentResponseData>(null, enrollmentResponseData, n2);
                break;
            } while (true);
        }
        Log.e("EnrollRequest", "Enrollment Response Data is Empty");
        return new com.samsung.android.spayfw.remoteservice.c<EnrollmentResponseData>(null, enrollmentResponseData, n2);
    }

    @Override
    protected String cG() {
        return "/enrollments";
    }

    @Override
    protected String getRequestType() {
        return "EnrollRequest";
    }
}

