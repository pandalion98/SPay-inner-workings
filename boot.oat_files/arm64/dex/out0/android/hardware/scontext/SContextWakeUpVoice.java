package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextWakeUpVoice extends SContextEventContext {
    public static final Creator<SContextWakeUpVoice> CREATOR = new Creator<SContextWakeUpVoice>() {
        public SContextWakeUpVoice createFromParcel(Parcel in) {
            return new SContextWakeUpVoice(in);
        }

        public SContextWakeUpVoice[] newArray(int size) {
            return new SContextWakeUpVoice[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextWakeUpVoice() {
        this.mContext = new Bundle();
    }

    SContextWakeUpVoice(Parcel src) {
        readFromParcel(src);
    }

    public int getMode() {
        return this.mContext.getInt("Mode");
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
