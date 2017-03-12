package android.os.storage.sensitive;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SensitiveDataParcel implements Parcelable {
    public static final Creator<SensitiveDataParcel> CREATOR = new Creator<SensitiveDataParcel>() {
        public SensitiveDataParcel[] newArray(int size) {
            return new SensitiveDataParcel[size];
        }

        public SensitiveDataParcel createFromParcel(Parcel source) {
            return new SensitiveDataParcel(source);
        }
    };
    public static final int SD_DECRYPT = 2;
    public static final int SD_ENCRYPT = 1;
    private String client;
    private byte[] data;
    private int op;

    public SensitiveDataParcel(String client, byte[] data) {
        this.op = 0;
        this.client = client;
        this.data = data;
    }

    private SensitiveDataParcel(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.op);
        dest.writeString(this.client);
        dest.writeInt(this.data.length);
        dest.writeByteArray(this.data);
    }

    public void readFromParcel(Parcel in) {
        this.op = in.readInt();
        this.client = in.readString();
        this.data = new byte[in.readInt()];
        in.readByteArray(this.data);
    }

    public void setOperation(int op) {
        this.op = op;
    }

    public int getOperation() {
        return this.op;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getDataLength() {
        return this.data.length;
    }

    public String getClient() {
        return this.client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
