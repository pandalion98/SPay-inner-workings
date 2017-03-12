package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextSLocationCore extends SContextEventContext {
    public static final Creator<SContextSLocationCore> CREATOR = new Creator<SContextSLocationCore>() {
        public SContextSLocationCore createFromParcel(Parcel in) {
            return new SContextSLocationCore(in);
        }

        public SContextSLocationCore[] newArray(int size) {
            return new SContextSLocationCore[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextSLocationCore() {
        this.mContext = new Bundle();
    }

    SContextSLocationCore(Parcel src) {
        readFromParcel(src);
    }

    public int getMode() {
        return this.mContext.getInt("Mode");
    }

    public int getAction() {
        return this.mContext.getInt("Action");
    }

    public int getFenceID() {
        return this.mContext.getInt("GeoFenceId");
    }

    public int getStatus() {
        return this.mContext.getInt("GeoFenceStatus");
    }

    public long getTimeStamp() {
        return this.mContext.getLong("Timestamp");
    }

    public double getLatitude() {
        return this.mContext.getDouble("Latitude");
    }

    public double getLongitude() {
        return this.mContext.getDouble("Longitude");
    }

    public int getAccuracy() {
        return this.mContext.getInt("Accuracy");
    }

    public int getTotalGpsCount() {
        return this.mContext.getInt("TotalGpsCount");
    }

    public int getSuccessGpsCount() {
        return this.mContext.getInt("SuccessGpsCount");
    }

    public int getFuncType() {
        return this.mContext.getInt("FunctionType");
    }

    public int getErrorCode() {
        return this.mContext.getInt("ErrorCode");
    }

    public float getDistance() {
        return this.mContext.getFloat("Distance");
    }

    public int getDataSize() {
        return this.mContext.getInt("DataCount");
    }

    public int[] getTypeArray() {
        return this.mContext.getIntArray("EventTypeArray");
    }

    public int[] getStatusArray() {
        return this.mContext.getIntArray("EventStatusArray");
    }

    public long[] getTimeStampArray() {
        return this.mContext.getLongArray("TimeStampArray");
    }

    public int[] getDataArray() {
        return this.mContext.getIntArray("DataArray");
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
