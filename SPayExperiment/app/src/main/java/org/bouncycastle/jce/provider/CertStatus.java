/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Date
 */
package org.bouncycastle.jce.provider;

import java.util.Date;

class CertStatus {
    public static final int UNDETERMINED = 12;
    public static final int UNREVOKED = 11;
    int certStatus = 11;
    Date revocationDate = null;

    CertStatus() {
    }

    public int getCertStatus() {
        return this.certStatus;
    }

    public Date getRevocationDate() {
        return this.revocationDate;
    }

    public void setCertStatus(int n) {
        this.certStatus = n;
    }

    public void setRevocationDate(Date date) {
        this.revocationDate = date;
    }
}

