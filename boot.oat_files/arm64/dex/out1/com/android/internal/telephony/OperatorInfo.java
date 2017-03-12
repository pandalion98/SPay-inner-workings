package com.android.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.sec.android.app.CscFeature;

public class OperatorInfo implements Parcelable {
    public static final Creator<OperatorInfo> CREATOR = new Creator<OperatorInfo>() {
        public OperatorInfo createFromParcel(Parcel in) {
            return new OperatorInfo(in.readString(), in.readString(), in.readString(), (State) in.readSerializable(), in.readInt());
        }

        public OperatorInfo[] newArray(int size) {
            return new OperatorInfo[size];
        }
    };
    int lac;
    private String mOperatorAlphaLong;
    private String mOperatorAlphaShort;
    private String mOperatorNumeric;
    private State mState;
    String operatorRat;

    public enum State {
        UNKNOWN,
        AVAILABLE,
        CURRENT,
        FORBIDDEN
    }

    public String getOperatorAlphaLong() {
        return this.mOperatorAlphaLong;
    }

    public String getOperatorAlphaShort() {
        return this.mOperatorAlphaShort;
    }

    public String getOperatorNumeric() {
        return this.mOperatorNumeric;
    }

    public State getState() {
        return this.mState;
    }

    public int getLac() {
        return this.lac;
    }

    public String getOperatorRat() {
        return this.operatorRat;
    }

    OperatorInfo(String operatorAlphaLong, String operatorAlphaShort, String operatorNumeric, State state) {
        this.mState = State.UNKNOWN;
        this.operatorRat = SmsConstants.FORMAT_UNKNOWN;
        this.mOperatorAlphaLong = operatorAlphaLong;
        this.mOperatorAlphaShort = operatorAlphaShort;
        this.mOperatorNumeric = operatorNumeric;
        this.mState = state;
        this.lac = 255;
    }

    public OperatorInfo(String operatorAlphaLong, String operatorAlphaShort, String operatorNumeric, String stateString) {
        this(operatorAlphaLong, operatorAlphaShort, operatorNumeric, rilStateToState(stateString));
    }

    public OperatorInfo(String operatorAlphaLong, String operatorAlphaShort, String operatorNumeric) {
        this(operatorAlphaLong, operatorAlphaShort, operatorNumeric, State.UNKNOWN);
    }

    public OperatorInfo(String operatorAlphaLong, String operatorAlphaShort, String operatorNumeric, String stateString, String lacString) {
        this(operatorAlphaLong, operatorAlphaShort, operatorNumeric, rilStateToState(stateString), Integer.parseInt(lacString));
    }

    public OperatorInfo(String operatorAlphaLong, String operatorAlphaShort, String operatorNumeric, State state, int lac) {
        this.mState = State.UNKNOWN;
        this.operatorRat = SmsConstants.FORMAT_UNKNOWN;
        this.mOperatorAlphaLong = operatorAlphaLong;
        this.mOperatorAlphaShort = operatorAlphaShort;
        this.mOperatorNumeric = operatorNumeric;
        this.mState = state;
        if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_NetworkInfoRat") || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_UseRatInfoDuringPlmnSelection")) {
            this.operatorRat = ratToString(lac);
        } else {
            this.lac = lac;
        }
    }

    public OperatorInfo(String operatorAlphaLong, String operatorAlphaShort, String operatorNumeric, String stateString, String ratString, String lacString) {
        this(operatorAlphaLong, operatorAlphaShort, operatorNumeric, rilStateToState(stateString));
        this.lac = Integer.parseInt(lacString);
        if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_DisplayRatInfoInManualNetSearchList")) {
            this.operatorRat = ratInfoToStr(ratString);
        } else {
            this.operatorRat = ratString;
        }
    }

    public OperatorInfo(String operatorAlphaLong, String operatorAlphaShort, String operatorNumeric, State state, String ratString) {
        this.mState = State.UNKNOWN;
        this.operatorRat = SmsConstants.FORMAT_UNKNOWN;
        this.mOperatorAlphaLong = operatorAlphaLong;
        this.mOperatorAlphaShort = operatorAlphaShort;
        this.mOperatorNumeric = operatorNumeric;
        this.mState = state;
        this.operatorRat = ratString;
    }

    private String ratToString(int rat) {
        if (rat == 1) {
            return "GERAN";
        }
        if (rat == 33) {
            return "LTE";
        }
        if (rat == 4) {
            return "UMTS";
        }
        return SmsConstants.FORMAT_UNKNOWN;
    }

    private String actByteToStr(int act) {
        if (act == 1) {
            return "2G";
        }
        if (act == 33) {
            return "4G";
        }
        if (act == 4) {
            return "3G";
        }
        return SmsConstants.FORMAT_UNKNOWN;
    }

    private String ratInfoToStr(String RATinfo) {
        if ("GERAN".equals(RATinfo)) {
            return "2G";
        }
        if ("LTE".equals(RATinfo)) {
            return "4G";
        }
        if ("UMTS".equals(RATinfo)) {
            return "3G";
        }
        return SmsConstants.FORMAT_UNKNOWN;
    }

    private static State rilStateToState(String s) {
        if (s.equals(SmsConstants.FORMAT_UNKNOWN)) {
            return State.UNKNOWN;
        }
        if (s.equals("available")) {
            return State.AVAILABLE;
        }
        if (s.equals("current")) {
            return State.CURRENT;
        }
        if (s.equals("forbidden")) {
            return State.FORBIDDEN;
        }
        throw new RuntimeException("RIL impl error: Invalid network state '" + s + "'");
    }

    public String toString() {
        if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_NetworkInfoRat") || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_UseRatInfoDuringPlmnSelection") || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_DisplayRatInfoInManualNetSearchList")) {
            return "OperatorInfo " + this.mOperatorAlphaLong + "/" + this.mOperatorAlphaShort + "/" + this.mOperatorNumeric + "/" + this.mState + "/" + this.operatorRat;
        }
        return "OperatorInfo " + this.mOperatorAlphaLong + "/" + this.mOperatorAlphaShort + "/" + this.mOperatorNumeric + "/" + this.mState + "/" + this.lac;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mOperatorAlphaLong);
        dest.writeString(this.mOperatorAlphaShort);
        dest.writeString(this.mOperatorNumeric);
        dest.writeSerializable(this.mState);
        dest.writeInt(this.lac);
    }
}
