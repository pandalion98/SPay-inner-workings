package org.bouncycastle.x509;

import java.util.Date;

class CertStatus {
    public static final int UNDETERMINED = 12;
    public static final int UNREVOKED = 11;
    int certStatus;
    Date revocationDate;

    CertStatus() {
        this.certStatus = UNREVOKED;
        this.revocationDate = null;
    }

    public int getCertStatus() {
        return this.certStatus;
    }

    public Date getRevocationDate() {
        return this.revocationDate;
    }

    public void setCertStatus(int i) {
        this.certStatus = i;
    }

    public void setRevocationDate(Date date) {
        this.revocationDate = date;
    }
}
