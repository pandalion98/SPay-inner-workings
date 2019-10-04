/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.util.List
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.models.Eula;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Activation;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardEnrollmentInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Url;
import java.util.List;

public class EnrollmentResponseData
extends Id {
    private Activation activation;
    private CardEnrollmentInfo card;
    private List<Eula> eulas;
    private String href;
    private Url token;

    public EnrollmentResponseData(String string) {
        super(string);
    }

    public Activation getActivation() {
        return this.activation;
    }

    CardEnrollmentInfo getCard() {
        return this.card;
    }

    public List<Eula> getEulas() {
        return this.eulas;
    }

    public String getHref() {
        return this.href;
    }

    public Url getToken() {
        return this.token;
    }
}

