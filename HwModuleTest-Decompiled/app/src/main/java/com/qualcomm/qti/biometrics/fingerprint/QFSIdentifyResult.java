package com.qualcomm.qti.biometrics.fingerprint;

public class QFSIdentifyResult {
    private int index;
    private int[] indexes;
    private int result;
    private String userId;

    public QFSIdentifyResult(int index2, int[] indexes2, int result2, String userId2) {
        this.index = index2;
        this.indexes = indexes2;
        this.result = result2;
        this.userId = userId2;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index2) {
        this.index = index2;
    }

    public int[] getIndexes() {
        return this.indexes;
    }

    public void setIndexes(int[] indexes2) {
        this.indexes = indexes2;
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result2) {
        this.result = result2;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId2) {
        this.userId = userId2;
    }
}
