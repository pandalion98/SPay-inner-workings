package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class PatchData {
    String op;
    String path;
    Object value;

    public PatchData() {
        this.op = "replace";
    }

    public void setOp(String str) {
        this.op = str;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public void setValue(Object obj) {
        this.value = obj;
    }
}
