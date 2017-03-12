package android.bluetooth;

import android.net.ProxyInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class BluetoothHeadsetClientCall implements Parcelable {
    public static final int CALL_STATE_ACTIVE = 0;
    public static final int CALL_STATE_ALERTING = 3;
    public static final int CALL_STATE_DIALING = 2;
    public static final int CALL_STATE_HELD = 1;
    public static final int CALL_STATE_HELD_BY_RESPONSE_AND_HOLD = 6;
    public static final int CALL_STATE_INCOMING = 4;
    public static final int CALL_STATE_TERMINATED = 7;
    public static final int CALL_STATE_WAITING = 5;
    public static final Creator<BluetoothHeadsetClientCall> CREATOR = new Creator<BluetoothHeadsetClientCall>() {
        public BluetoothHeadsetClientCall createFromParcel(Parcel in) {
            boolean z = true;
            BluetoothDevice bluetoothDevice = (BluetoothDevice) in.readParcelable(null);
            int readInt = in.readInt();
            int readInt2 = in.readInt();
            String readString = in.readString();
            boolean z2 = in.readInt() == 1;
            if (in.readInt() != 1) {
                z = false;
            }
            return new BluetoothHeadsetClientCall(bluetoothDevice, readInt, readInt2, readString, z2, z);
        }

        public BluetoothHeadsetClientCall[] newArray(int size) {
            return new BluetoothHeadsetClientCall[size];
        }
    };
    private final BluetoothDevice mDevice;
    private final int mId;
    private boolean mMultiParty;
    private String mNumber;
    private final boolean mOutgoing;
    private int mState;

    public BluetoothHeadsetClientCall(BluetoothDevice device, int id, int state, String number, boolean multiParty, boolean outgoing) {
        this.mDevice = device;
        this.mId = id;
        this.mState = state;
        if (number == null) {
            number = ProxyInfo.LOCAL_EXCL_LIST;
        }
        this.mNumber = number;
        this.mMultiParty = multiParty;
        this.mOutgoing = outgoing;
    }

    public void setState(int state) {
        this.mState = state;
    }

    public void setNumber(String number) {
        this.mNumber = number;
    }

    public void setMultiParty(boolean multiParty) {
        this.mMultiParty = multiParty;
    }

    public BluetoothDevice getDevice() {
        return this.mDevice;
    }

    public int getId() {
        return this.mId;
    }

    public int getState() {
        return this.mState;
    }

    public String getNumber() {
        return this.mNumber;
    }

    public boolean isMultiParty() {
        return this.mMultiParty;
    }

    public boolean isOutgoing() {
        return this.mOutgoing;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("BluetoothHeadsetClientCall{mDevice: ");
        builder.append(this.mDevice);
        builder.append(", mId: ");
        builder.append(this.mId);
        builder.append(", mState: ");
        switch (this.mState) {
            case 0:
                builder.append("ACTIVE");
                break;
            case 1:
                builder.append("HELD");
                break;
            case 2:
                builder.append("DIALING");
                break;
            case 3:
                builder.append("ALERTING");
                break;
            case 4:
                builder.append("INCOMING");
                break;
            case 5:
                builder.append("WAITING");
                break;
            case 6:
                builder.append("HELD_BY_RESPONSE_AND_HOLD");
                break;
            case 7:
                builder.append("TERMINATED");
                break;
            default:
                builder.append(this.mState);
                break;
        }
        builder.append(", mNumber: ");
        builder.append(this.mNumber);
        builder.append(", mMultiParty: ");
        builder.append(this.mMultiParty);
        builder.append(", mOutgoing: ");
        builder.append(this.mOutgoing);
        builder.append("}");
        return builder.toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        int i;
        int i2 = 1;
        out.writeParcelable(this.mDevice, 0);
        out.writeInt(this.mId);
        out.writeInt(this.mState);
        out.writeString(this.mNumber);
        if (this.mMultiParty) {
            i = 1;
        } else {
            i = 0;
        }
        out.writeInt(i);
        if (!this.mOutgoing) {
            i2 = 0;
        }
        out.writeInt(i2);
    }

    public int describeContents() {
        return 0;
    }
}
