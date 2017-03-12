package com.samsung.android.cepproxyks;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.security.cert.Certificate;

public class CertificateAKS implements Parcelable {
    public static final Creator<CertificateAKS> CREATOR = new Creator<CertificateAKS>() {
        public CertificateAKS createFromParcel(Parcel source) {
            return new CertificateAKS(source);
        }

        public CertificateAKS[] newArray(int size) {
            return null;
        }
    };
    public Certificate[] mCertificate;

    public CertificateAKS(Parcel source) {
        this.mCertificate = (Certificate[]) source.readSerializable();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.mCertificate);
    }
}
