package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class WifiChannel implements Parcelable {
    public static final Creator<WifiChannel> CREATOR = new Creator<WifiChannel>() {
        public WifiChannel createFromParcel(Parcel in) {
            boolean z = true;
            WifiChannel channel = new WifiChannel();
            channel.freqMHz = in.readInt();
            channel.channelNum = in.readInt();
            channel.isDFS = in.readInt() != 0;
            channel.frequency = in.readInt();
            channel.channel = in.readInt();
            if (in.readInt() == 0) {
                z = false;
            }
            channel.isNoIbss = z;
            return channel;
        }

        public WifiChannel[] newArray(int size) {
            return new WifiChannel[size];
        }
    };
    private static final int MAX_CHANNEL_NUM = 196;
    private static final int MAX_FREQ_MHZ = 5825;
    private static final int MIN_CHANNEL_NUM = 1;
    private static final int MIN_FREQ_MHZ = 2412;
    public int channel;
    public int channelNum;
    public int freqMHz;
    public int frequency;
    public boolean isDFS;
    public boolean isNoIbss;

    public boolean isValid() {
        if (this.freqMHz < MIN_FREQ_MHZ || this.freqMHz > MAX_FREQ_MHZ) {
            return false;
        }
        if (this.channelNum < 1 || this.channelNum > 196) {
            return false;
        }
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        int i = 1;
        out.writeInt(this.freqMHz);
        out.writeInt(this.channelNum);
        out.writeInt(this.isDFS ? 1 : 0);
        out.writeInt(this.frequency);
        out.writeInt(this.channel);
        if (!this.isNoIbss) {
            i = 0;
        }
        out.writeInt(i);
    }
}
