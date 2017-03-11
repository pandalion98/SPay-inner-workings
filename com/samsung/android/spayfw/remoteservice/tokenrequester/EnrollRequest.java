package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EnrollmentRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EnrollmentResponseData;

/* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.c */
public class EnrollRequest extends TokenRequesterRequest<EnrollmentRequestData, EnrollmentResponseData, Response<EnrollmentResponseData>, EnrollRequest> {
    protected EnrollRequest(TokenRequesterClient tokenRequesterClient, EnrollmentRequestData enrollmentRequestData) {
        super(tokenRequesterClient, RequestMethod.POST, enrollmentRequestData);
    }

    protected String cG() {
        return "/enrollments";
    }

    protected String getRequestType() {
        return "EnrollRequest";
    }

    protected Response<EnrollmentResponseData> m1198b(int i, String str) {
        EnrollmentResponseData enrollmentResponseData = (EnrollmentResponseData) this.Al.fromJson(str, EnrollmentResponseData.class);
        Log.m285d("EnrollRequest", "EnrollmentResponseData : " + enrollmentResponseData);
        if (enrollmentResponseData != null) {
            m840b(enrollmentResponseData.getEulas());
        } else {
            Log.m286e("EnrollRequest", "Enrollment Response Data is Empty");
        }
        return new Response(null, enrollmentResponseData, i);
    }
}
