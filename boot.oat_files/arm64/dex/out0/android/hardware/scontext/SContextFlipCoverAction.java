package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextFlipCoverAction extends SContextEventContext {
    public static final Creator<SContextFlipCoverAction> CREATOR = new Creator<SContextFlipCoverAction>() {
        public SContextFlipCoverAction createFromParcel(Parcel in) {
            return new SContextFlipCoverAction(in);
        }

        public SContextFlipCoverAction[] newArray(int size) {
            return new SContextFlipCoverAction[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextFlipCoverAction() {
        this.mContext = new Bundle();
    }

    SContextFlipCoverAction(Parcel src) {
        readFromParcel(src);
    }

    public int getAction() {
        return this.mContext.getInt("Action");
    }

    void setValues(Bundle context) {
        this.mContext = context;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.mContext);
    }

    private void readFromParcel(Parcel src) {
        this.mContext = src.readBundle();
    }
}
