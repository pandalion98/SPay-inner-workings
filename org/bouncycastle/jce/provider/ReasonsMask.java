package org.bouncycastle.jce.provider;

import org.bouncycastle.asn1.x509.ReasonFlags;

class ReasonsMask {
    static final ReasonsMask allReasons;
    private int _reasons;

    static {
        allReasons = new ReasonsMask(33023);
    }

    ReasonsMask() {
        this(0);
    }

    private ReasonsMask(int i) {
        this._reasons = i;
    }

    ReasonsMask(ReasonFlags reasonFlags) {
        this._reasons = reasonFlags.intValue();
    }

    void addReasons(ReasonsMask reasonsMask) {
        this._reasons |= reasonsMask.getReasons();
    }

    int getReasons() {
        return this._reasons;
    }

    boolean hasNewReasons(ReasonsMask reasonsMask) {
        return (this._reasons | (reasonsMask.getReasons() ^ this._reasons)) != 0;
    }

    ReasonsMask intersect(ReasonsMask reasonsMask) {
        ReasonsMask reasonsMask2 = new ReasonsMask();
        reasonsMask2.addReasons(new ReasonsMask(this._reasons & reasonsMask.getReasons()));
        return reasonsMask2;
    }

    boolean isAllReasons() {
        return this._reasons == allReasons._reasons;
    }
}
