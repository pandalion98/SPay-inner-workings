package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Instruction implements Parcelable {
    public static final Creator<Instruction> CREATOR;
    private boolean encrypt;
    private String op;
    private String path;
    private String value;

    /* renamed from: com.samsung.android.spayfw.appinterface.Instruction.1 */
    static class C03661 implements Creator<Instruction> {
        C03661() {
        }

        public Instruction createFromParcel(Parcel parcel) {
            return new Instruction(null);
        }

        public Instruction[] newArray(int i) {
            return new Instruction[i];
        }
    }

    static {
        CREATOR = new C03661();
    }

    private Instruction(Parcel parcel) {
        readFromParcel(parcel);
    }

    public Instruction() {
        this.op = null;
        this.path = null;
        this.value = null;
        this.encrypt = false;
    }

    public String getOp() {
        return this.op;
    }

    public void setOp(String str) {
        this.op = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public boolean isEncrypt() {
        return this.encrypt;
    }

    public void setEncrypt(boolean z) {
        this.encrypt = z;
    }

    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        boolean z = true;
        this.op = parcel.readString();
        this.path = parcel.readString();
        this.value = parcel.readString();
        if (parcel.readInt() != 1) {
            z = false;
        }
        this.encrypt = z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.op);
        parcel.writeString(this.path);
        parcel.writeString(this.value);
        parcel.writeInt(this.encrypt ? 1 : 0);
    }

    public String toString() {
        return "Instruction: op: " + this.op + " path: " + this.path + " value: " + this.value + "encrypt: " + this.encrypt;
    }
}
