package android.bluetooth.le;

import android.hardware.scontext.SContextConstants;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class ScanSettings implements Parcelable {
    public static final int CALLBACK_TYPE_ALL_MATCHES = 1;
    public static final int CALLBACK_TYPE_FIRST_MATCH = 2;
    public static final int CALLBACK_TYPE_MATCH_LOST = 4;
    public static final Creator<ScanSettings> CREATOR = new Creator<ScanSettings>() {
        public ScanSettings[] newArray(int size) {
            return new ScanSettings[size];
        }

        public ScanSettings createFromParcel(Parcel in) {
            return new ScanSettings(in);
        }
    };
    public static final int MATCH_MODE_AGGRESSIVE = 1;
    public static final int MATCH_MODE_CUSTOM = 101;
    public static final int MATCH_MODE_STICKY = 2;
    public static final int MATCH_NUM_FEW_ADVERTISEMENT = 2;
    public static final int MATCH_NUM_MAX_ADVERTISEMENT = 3;
    public static final int MATCH_NUM_ONE_ADVERTISEMENT = 1;
    public static final int SCAN_MODE_BALANCED = 1;
    public static final int SCAN_MODE_CUSTOM = 100;
    public static final int SCAN_MODE_LOW_LATENCY = 2;
    public static final int SCAN_MODE_LOW_POWER = 0;
    public static final int SCAN_MODE_OPPORTUNISTIC = -1;
    public static final int SCAN_RESULT_TYPE_ABBREVIATED = 1;
    public static final int SCAN_RESULT_TYPE_FULL = 0;
    private int mCallbackType;
    private int mCustomScanHighRssiFilterValue;
    private int mCustomScanInterval;
    private int mCustomScanLowRssiFilterValue;
    private int mCustomScanWindow;
    private int mMatchMode;
    private int mNumOfMatchesPerFilter;
    private long mReportDelayMillis;
    private int mScanMode;
    private int mScanResultType;

    public static final class Builder {
        private int mCallbackType = 1;
        private int mCustomScanHighRssiFilterValue = SContextConstants.TEMPERATURE_ALERT_MINUS_INFINITY;
        private int mCustomScanInterval = 0;
        private int mCustomScanLowRssiFilterValue = SContextConstants.TEMPERATURE_ALERT_MINUS_INFINITY;
        private int mCustomScanWindow = 0;
        private int mMatchMode = 1;
        private int mNumOfMatchesPerFilter = 3;
        private long mReportDelayMillis = 0;
        private int mScanMode = 0;
        private int mScanResultType = 0;

        public Builder setScanMode(int scanMode) {
            if (scanMode < -1 || scanMode > 100) {
                throw new IllegalArgumentException("invalid scan mode " + scanMode);
            }
            this.mScanMode = scanMode;
            return this;
        }

        public Builder setCallbackType(int callbackType) {
            if (isValidCallbackType(callbackType)) {
                this.mCallbackType = callbackType;
                return this;
            }
            throw new IllegalArgumentException("invalid callback type - " + callbackType);
        }

        private boolean isValidCallbackType(int callbackType) {
            if (callbackType == 1 || callbackType == 2 || callbackType == 4 || callbackType == 6) {
                return true;
            }
            return false;
        }

        public Builder setScanResultType(int scanResultType) {
            if (scanResultType < 0 || scanResultType > 1) {
                throw new IllegalArgumentException("invalid scanResultType - " + scanResultType);
            }
            this.mScanResultType = scanResultType;
            return this;
        }

        public Builder setReportDelay(long reportDelayMillis) {
            if (reportDelayMillis < 0) {
                throw new IllegalArgumentException("reportDelay must be > 0");
            }
            this.mReportDelayMillis = reportDelayMillis;
            return this;
        }

        public Builder setNumOfMatches(int numOfMatches) {
            if (numOfMatches < 1 || numOfMatches > 3) {
                throw new IllegalArgumentException("invalid numOfMatches " + numOfMatches);
            }
            this.mNumOfMatchesPerFilter = numOfMatches;
            return this;
        }

        public Builder setMatchMode(int matchMode) {
            if (matchMode < 1 || matchMode > 101) {
                throw new IllegalArgumentException("invalid matchMode " + matchMode);
            }
            this.mMatchMode = matchMode;
            return this;
        }

        public Builder setCustomScanParams(int scanInterval, int scanWindow) {
            this.mCustomScanInterval = scanInterval;
            this.mCustomScanWindow = scanWindow;
            return this;
        }

        public Builder setCustomScanRssiFilterValue(int highRssiThreshold, int lowRssiThreshold) {
            this.mCustomScanHighRssiFilterValue = highRssiThreshold;
            this.mCustomScanLowRssiFilterValue = lowRssiThreshold;
            return this;
        }

        public ScanSettings build() {
            return new ScanSettings(this.mScanMode, this.mCallbackType, this.mScanResultType, this.mReportDelayMillis, this.mMatchMode, this.mNumOfMatchesPerFilter, this.mCustomScanInterval, this.mCustomScanWindow, this.mCustomScanHighRssiFilterValue, this.mCustomScanLowRssiFilterValue);
        }
    }

    public int getScanMode() {
        return this.mScanMode;
    }

    public int getCallbackType() {
        return this.mCallbackType;
    }

    public int getScanResultType() {
        return this.mScanResultType;
    }

    public int getMatchMode() {
        return this.mMatchMode;
    }

    public int getNumOfMatches() {
        return this.mNumOfMatchesPerFilter;
    }

    public int getCustomScanInterval() {
        return this.mCustomScanInterval;
    }

    public int getCustomScanWindow() {
        return this.mCustomScanWindow;
    }

    public int getCustomScanHighRssiFilterValue() {
        return this.mCustomScanHighRssiFilterValue;
    }

    public int getCustomScanLowRssiFilterValue() {
        return this.mCustomScanLowRssiFilterValue;
    }

    public long getReportDelayMillis() {
        return this.mReportDelayMillis;
    }

    private ScanSettings(int scanMode, int callbackType, int scanResultType, long reportDelayMillis, int matchMode, int numOfMatchesPerFilter, int scanInterval, int scanWindow, int scanHighRssiFilterValue, int scanLowRssiFilterValue) {
        this.mScanMode = scanMode;
        this.mCallbackType = callbackType;
        this.mScanResultType = scanResultType;
        this.mReportDelayMillis = reportDelayMillis;
        this.mNumOfMatchesPerFilter = numOfMatchesPerFilter;
        this.mMatchMode = matchMode;
        this.mCustomScanInterval = scanInterval;
        this.mCustomScanWindow = scanWindow;
        this.mCustomScanHighRssiFilterValue = scanHighRssiFilterValue;
        this.mCustomScanLowRssiFilterValue = scanLowRssiFilterValue;
    }

    private ScanSettings(Parcel in) {
        this.mScanMode = in.readInt();
        this.mCallbackType = in.readInt();
        this.mScanResultType = in.readInt();
        this.mReportDelayMillis = in.readLong();
        this.mMatchMode = in.readInt();
        this.mNumOfMatchesPerFilter = in.readInt();
        this.mCustomScanInterval = in.readInt();
        this.mCustomScanWindow = in.readInt();
        this.mCustomScanHighRssiFilterValue = in.readInt();
        this.mCustomScanLowRssiFilterValue = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mScanMode);
        dest.writeInt(this.mCallbackType);
        dest.writeInt(this.mScanResultType);
        dest.writeLong(this.mReportDelayMillis);
        dest.writeInt(this.mMatchMode);
        dest.writeInt(this.mNumOfMatchesPerFilter);
        dest.writeInt(this.mCustomScanInterval);
        dest.writeInt(this.mCustomScanWindow);
        dest.writeInt(this.mCustomScanHighRssiFilterValue);
        dest.writeInt(this.mCustomScanLowRssiFilterValue);
    }

    public int describeContents() {
        return 0;
    }
}
