package android.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public class AudioRoutesInfo implements Parcelable {
    public static final Creator<AudioRoutesInfo> CREATOR = new Creator<AudioRoutesInfo>() {
        public AudioRoutesInfo createFromParcel(Parcel in) {
            return new AudioRoutesInfo(in);
        }

        public AudioRoutesInfo[] newArray(int size) {
            return new AudioRoutesInfo[size];
        }
    };
    public static final int MAIN_DOCK_SPEAKERS = 4;
    public static final int MAIN_HDMI = 8;
    public static final int MAIN_HEADPHONES = 2;
    public static final int MAIN_HEADSET = 1;
    public static final int MAIN_SPEAKER = 0;
    public static final int MAIN_USB = 16;
    public CharSequence bluetoothName;
    public int mainType = 0;

    public AudioRoutesInfo(AudioRoutesInfo o) {
        this.bluetoothName = o.bluetoothName;
        this.mainType = o.mainType;
    }

    AudioRoutesInfo(Parcel src) {
        this.bluetoothName = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(src);
        this.mainType = src.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        TextUtils.writeToParcel(this.bluetoothName, dest, flags);
        dest.writeInt(this.mainType);
    }
}
