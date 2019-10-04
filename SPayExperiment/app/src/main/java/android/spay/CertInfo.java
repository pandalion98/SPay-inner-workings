package android.spay;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CertInfo implements Parcelable {
    public static final Creator<CertInfo> CREATOR = new Creator<CertInfo>() {
        public CertInfo createFromParcel(Parcel in) {
            return new CertInfo(in);
        }

        public CertInfo[] newArray(int size) {
            return new CertInfo[size];
        }
    };
    private static final String TAG = "CertInfo";
    public Map<String, byte[]> mCerts;

    public CertInfo() {
        this.mCerts = new HashMap();
    }

    private CertInfo(Parcel in) {
        this.mCerts = new HashMap();
        readFromParcel(in);
    }

    public void writeToParcel(Parcel out, int flag) {
        Log.d(TAG, "Writing Certificates = " + this.mCerts.size());
        out.writeInt(this.mCerts.size());
        for (String s : this.mCerts.keySet()) {
            Log.d(TAG, "Certificate = " + s);
            out.writeString(s);
            byte[] certdata = (byte[]) this.mCerts.get(s);
            Log.d(TAG, "certdata = " + Arrays.toString(certdata));
            if (certdata == null || certdata.length == 0) {
                out.writeInt(0);
            } else {
                out.writeInt(certdata.length);
                out.writeByteArray(certdata);
            }
        }
    }

    public void readFromParcel(Parcel in) {
        int count = in.readInt();
        Log.d(TAG, "Reading Certificates = " + count);
        for (int i = 0; i < count; i++) {
            String name = in.readString();
            Log.d(TAG, "Reading Certificate = " + name);
            int certdatalen = in.readInt();
            Log.d(TAG, "Reading Certificate Len = " + certdatalen);
            if (certdatalen == 0) {
                this.mCerts.put(name, null);
            } else {
                byte[] certdata = new byte[certdatalen];
                in.readByteArray(certdata);
                this.mCerts.put(name, certdata);
                Log.d(TAG, "certdata = " + Arrays.toString(certdata));
            }
        }
    }

    public int describeContents() {
        return 0;
    }
}
