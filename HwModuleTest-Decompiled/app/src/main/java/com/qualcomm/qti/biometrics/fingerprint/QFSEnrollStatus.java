package com.qualcomm.qti.biometrics.fingerprint;

public class QFSEnrollStatus {
    private int badTrial;
    private int progress;
    private int successTrial;
    private int totalTrial;

    public QFSEnrollStatus(int badTrial2, int progress2, int successTrial2, int totalTrial2) {
        this.badTrial = badTrial2;
        this.progress = progress2;
        this.successTrial = successTrial2;
        this.totalTrial = totalTrial2;
    }

    public int getBadTrial() {
        return this.badTrial;
    }

    public void setBadTrial(int badTrial2) {
        this.badTrial = badTrial2;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress2) {
        this.progress = progress2;
    }

    public int getSuccessTrial() {
        return this.successTrial;
    }

    public void setSuccessTrial(int successTrial2) {
        this.successTrial = successTrial2;
    }

    public int getTotalTrial() {
        return this.totalTrial;
    }

    public void setTotalTrial(int totalTrial2) {
        this.totalTrial = totalTrial2;
    }
}
