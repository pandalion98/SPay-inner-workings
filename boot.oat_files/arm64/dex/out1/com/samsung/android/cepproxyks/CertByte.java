package com.samsung.android.cepproxyks;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CertByte implements Parcelable {
    public static final Creator<CertByte> CREATOR = new Creator<CertByte>() {
        public CertByte createFromParcel(Parcel source) {
            return new CertByte(source);
        }

        public CertByte[] newArray(int size) {
            return null;
        }
    };
    public byte[] caCertBytes;
    public int caSize;
    public byte[] certBytes;
    public int certsize;

    public CertByte(Parcel source) {
        this.certsize = source.readInt();
        this.certBytes = new byte[this.certsize];
        source.readByteArray(this.certBytes);
        this.caSize = source.readInt();
        this.caCertBytes = new byte[this.caSize];
        source.readByteArray(this.caCertBytes);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.certsize);
        dest.writeByteArray(this.certBytes);
        dest.writeInt(this.caSize);
        dest.writeByteArray(this.caCertBytes);
    }
}
