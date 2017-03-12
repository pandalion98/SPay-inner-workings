package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class BluetoothActivityEnergyInfo implements Parcelable {
    public static final int BT_STACK_STATE_INVALID = 0;
    public static final int BT_STACK_STATE_STATE_ACTIVE = 1;
    public static final int BT_STACK_STATE_STATE_IDLE = 3;
    public static final int BT_STACK_STATE_STATE_SCANNING = 2;
    public static final Creator<BluetoothActivityEnergyInfo> CREATOR = new Creator<BluetoothActivityEnergyInfo>() {
        public BluetoothActivityEnergyInfo createFromParcel(Parcel in) {
            return new BluetoothActivityEnergyInfo(in.readLong(), in.readInt(), in.readLong(), in.readLong(), in.readLong(), in.readLong());
        }

        public BluetoothActivityEnergyInfo[] newArray(int size) {
            return new BluetoothActivityEnergyInfo[size];
        }
    };
    private final int mBluetoothStackState;
    private final long mControllerEnergyUsed;
    private final long mControllerIdleTimeMs;
    private final long mControllerRxTimeMs;
    private final long mControllerTxTimeMs;
    private final long mTimestamp;

    public BluetoothActivityEnergyInfo(long timestamp, int stackState, long txTime, long rxTime, long idleTime, long energyUsed) {
        this.mTimestamp = timestamp;
        this.mBluetoothStackState = stackState;
        this.mControllerTxTimeMs = txTime;
        this.mControllerRxTimeMs = rxTime;
        this.mControllerIdleTimeMs = idleTime;
        this.mControllerEnergyUsed = energyUsed;
    }

    public String toString() {
        return "BluetoothActivityEnergyInfo{ mTimestamp=" + this.mTimestamp + " mBluetoothStackState=" + this.mBluetoothStackState + " mControllerTxTimeMs=" + this.mControllerTxTimeMs + " mControllerRxTimeMs=" + this.mControllerRxTimeMs + " mControllerIdleTimeMs=" + this.mControllerIdleTimeMs + " mControllerEnergyUsed=" + this.mControllerEnergyUsed + " }";
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.mTimestamp);
        out.writeInt(this.mBluetoothStackState);
        out.writeLong(this.mControllerTxTimeMs);
        out.writeLong(this.mControllerRxTimeMs);
        out.writeLong(this.mControllerIdleTimeMs);
        out.writeLong(this.mControllerEnergyUsed);
    }

    public int describeContents() {
        return 0;
    }

    public int getBluetoothStackState() {
        return this.mBluetoothStackState;
    }

    public long getControllerTxTimeMillis() {
        return this.mControllerTxTimeMs;
    }

    public long getControllerRxTimeMillis() {
        return this.mControllerRxTimeMs;
    }

    public long getControllerIdleTimeMillis() {
        return this.mControllerIdleTimeMs;
    }

    public long getControllerEnergyUsed() {
        return this.mControllerEnergyUsed;
    }

    public long getTimeStamp() {
        return this.mTimestamp;
    }

    public boolean isValid() {
        return (this.mControllerTxTimeMs == 0 && this.mControllerRxTimeMs == 0 && this.mControllerIdleTimeMs == 0) ? false : true;
    }
}
