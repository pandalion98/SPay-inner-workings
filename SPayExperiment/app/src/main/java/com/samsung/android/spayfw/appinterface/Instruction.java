/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;

public class Instruction
implements Parcelable {
    public static final Parcelable.Creator<Instruction> CREATOR = new Parcelable.Creator<Instruction>(){

        public Instruction createFromParcel(Parcel parcel) {
            return new Instruction(parcel);
        }

        public Instruction[] newArray(int n2) {
            return new Instruction[n2];
        }
    };
    private boolean encrypt;
    private String op;
    private String path;
    private String value;

    public Instruction() {
        this.op = null;
        this.path = null;
        this.value = null;
        this.encrypt = false;
    }

    private Instruction(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public String getOp() {
        return this.op;
    }

    public String getPath() {
        return this.path;
    }

    public String getValue() {
        return this.value;
    }

    public boolean isEncrypt() {
        return this.encrypt;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void readFromParcel(Parcel parcel) {
        int n2 = 1;
        this.op = parcel.readString();
        this.path = parcel.readString();
        this.value = parcel.readString();
        if (parcel.readInt() != n2) {
            n2 = 0;
        }
        this.encrypt = n2;
    }

    public void setEncrypt(boolean bl) {
        this.encrypt = bl;
    }

    public void setOp(String string) {
        this.op = string;
    }

    public void setPath(String string) {
        this.path = string;
    }

    public void setValue(String string) {
        this.value = string;
    }

    public String toString() {
        return "Instruction: op: " + this.op + " path: " + this.path + " value: " + this.value + "encrypt: " + this.encrypt;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.op);
        parcel.writeString(this.path);
        parcel.writeString(this.value);
        int n3 = this.encrypt ? 1 : 0;
        parcel.writeInt(n3);
    }

}

