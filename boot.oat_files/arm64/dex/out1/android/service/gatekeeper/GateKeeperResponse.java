package android.service.gatekeeper;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class GateKeeperResponse implements Parcelable {
    public static final Creator<GateKeeperResponse> CREATOR = new Creator<GateKeeperResponse>() {
        public GateKeeperResponse createFromParcel(Parcel source) {
            boolean z = true;
            int responseCode = source.readInt();
            GateKeeperResponse response = new GateKeeperResponse(responseCode);
            if (responseCode == 1) {
                response.setTimeout(source.readInt());
            } else if (responseCode == 0) {
                if (source.readInt() != 1) {
                    z = false;
                }
                response.setShouldReEnroll(z);
                int size = source.readInt();
                if (size > 0) {
                    byte[] payload = new byte[size];
                    source.readByteArray(payload);
                    response.setPayload(payload);
                }
            }
            return response;
        }

        public GateKeeperResponse[] newArray(int size) {
            return new GateKeeperResponse[size];
        }
    };
    public static final int RESPONSE_ERROR = -1;
    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_RETRY = 1;
    private byte[] mPayload;
    private final int mResponseCode;
    private boolean mShouldReEnroll;
    private int mTimeout;

    private GateKeeperResponse(int responseCode) {
        this.mResponseCode = responseCode;
    }

    private GateKeeperResponse(int responseCode, int timeout) {
        this.mResponseCode = responseCode;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i = 1;
        dest.writeInt(this.mResponseCode);
        if (this.mResponseCode == 1) {
            dest.writeInt(this.mTimeout);
        } else if (this.mResponseCode == 0) {
            if (!this.mShouldReEnroll) {
                i = 0;
            }
            dest.writeInt(i);
            if (this.mPayload != null) {
                dest.writeInt(this.mPayload.length);
                dest.writeByteArray(this.mPayload);
            }
        }
    }

    public byte[] getPayload() {
        return this.mPayload;
    }

    public int getTimeout() {
        return this.mTimeout;
    }

    public boolean getShouldReEnroll() {
        return this.mShouldReEnroll;
    }

    public int getResponseCode() {
        return this.mResponseCode;
    }

    private void setTimeout(int timeout) {
        this.mTimeout = timeout;
    }

    private void setShouldReEnroll(boolean shouldReEnroll) {
        this.mShouldReEnroll = shouldReEnroll;
    }

    private void setPayload(byte[] payload) {
        this.mPayload = payload;
    }
}
