package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class ScoredNetwork implements Parcelable {
    public static final Creator<ScoredNetwork> CREATOR = new Creator<ScoredNetwork>() {
        public ScoredNetwork createFromParcel(Parcel in) {
            return new ScoredNetwork(in);
        }

        public ScoredNetwork[] newArray(int size) {
            return new ScoredNetwork[size];
        }
    };
    public final NetworkKey networkKey;
    public final RssiCurve rssiCurve;

    public ScoredNetwork(NetworkKey networkKey, RssiCurve rssiCurve) {
        this.networkKey = networkKey;
        this.rssiCurve = rssiCurve;
    }

    private ScoredNetwork(Parcel in) {
        this.networkKey = (NetworkKey) NetworkKey.CREATOR.createFromParcel(in);
        if (in.readByte() == (byte) 1) {
            this.rssiCurve = (RssiCurve) RssiCurve.CREATOR.createFromParcel(in);
        } else {
            this.rssiCurve = null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        this.networkKey.writeToParcel(out, flags);
        if (this.rssiCurve != null) {
            out.writeByte((byte) 1);
            this.rssiCurve.writeToParcel(out, flags);
            return;
        }
        out.writeByte((byte) 0);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScoredNetwork that = (ScoredNetwork) o;
        if (Objects.equals(this.networkKey, that.networkKey) && Objects.equals(this.rssiCurve, that.rssiCurve)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.networkKey, this.rssiCurve});
    }

    public String toString() {
        return "ScoredNetwork[key=" + this.networkKey + ",score=" + this.rssiCurve + "]";
    }
}
