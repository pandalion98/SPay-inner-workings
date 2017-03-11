package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.models.Eula;
import java.util.List;

public class EnrollmentResponseData extends Id {
    private Activation activation;
    private CardEnrollmentInfo card;
    private List<Eula> eulas;
    private String href;
    private Url token;

    public EnrollmentResponseData(String str) {
        super(str);
    }

    CardEnrollmentInfo getCard() {
        return this.card;
    }

    public Activation getActivation() {
        return this.activation;
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
