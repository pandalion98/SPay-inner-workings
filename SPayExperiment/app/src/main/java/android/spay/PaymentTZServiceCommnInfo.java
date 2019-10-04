package android.spay;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.HashMap;
import java.util.Map;

public class PaymentTZServiceCommnInfo implements Parcelable {
    public static final Creator<PaymentTZServiceCommnInfo> CREATOR = new Creator<PaymentTZServiceCommnInfo>() {
        public PaymentTZServiceCommnInfo createFromParcel(Parcel in) {
            return new PaymentTZServiceCommnInfo(in);
        }

        public PaymentTZServiceCommnInfo[] newArray(int size) {
            return new PaymentTZServiceCommnInfo[size];
        }
    };
    public int mServiceVersion;
    public Map<Integer, IBinder> mTAs;

    public PaymentTZServiceCommnInfo() {
        this.mTAs = new HashMap();
    }

    private PaymentTZServiceCommnInfo(Parcel in) {
        this.mTAs = new HashMap();
        readFromParcel(in);
    }

    public void writeToParcel(Parcel out, int flag) {
        out.writeInt(this.mServiceVersion);
        out.writeInt(this.mTAs.size());
        for (Integer s : this.mTAs.keySet()) {
            out.writeInt(s.intValue());
            out.writeStrongBinder((IBinder) this.mTAs.get(s));
        }
    }

    public void readFromParcel(Parcel in) {
        this.mServiceVersion = in.readInt();
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            this.mTAs.put(Integer.valueOf(in.readInt()), in.readStrongBinder());
        }
    }

    public int describeContents() {
        return 0;
    }
}
