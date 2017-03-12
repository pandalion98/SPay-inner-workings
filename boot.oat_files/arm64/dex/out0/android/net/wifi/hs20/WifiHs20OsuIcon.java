package android.net.wifi.hs20;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class WifiHs20OsuIcon implements Parcelable {
    public static final Creator<WifiHs20OsuIcon> CREATOR = new Creator<WifiHs20OsuIcon>() {
        public WifiHs20OsuIcon createFromParcel(Parcel in) {
            return new WifiHs20OsuIcon(in.readInt(), in.readInt(), in.readString(), in.readString(), in.readString());
        }

        public WifiHs20OsuIcon[] newArray(int size) {
            return new WifiHs20OsuIcon[size];
        }
    };
    private String fileName;
    int height;
    String iconHash;
    private boolean isDownloaded;
    String language;
    private String type;
    int width;

    public WifiHs20OsuIcon() {
        this.width = 0;
        this.height = 0;
        this.language = null;
        this.type = null;
        setFileName(null);
    }

    public WifiHs20OsuIcon(int width, int height, String language, String type, String fileName) {
        this.width = width;
        this.height = height;
        this.language = language;
        this.type = type;
        setFileName(fileName);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.language);
        dest.writeString(this.type);
        dest.writeString(getFileName());
    }

    public boolean isDownloaded() {
        return this.isDownloaded;
    }

    public void setDownloaded(boolean isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
