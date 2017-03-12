package android.telephony.cdma;

import android.os.Bundle;
import android.telephony.CellLocation;
import com.samsung.android.fingerprint.FingerprintManager;

public class CdmaCellLocation extends CellLocation {
    public static final int INVALID_LAT_LONG = Integer.MAX_VALUE;
    private int mBaseStationId;
    private int mBaseStationLatitude;
    private int mBaseStationLongitude;
    private int mLteCellId;
    private int mNetworkId;
    private int mSystemId;
    private int mTac;

    public CdmaCellLocation() {
        this.mBaseStationId = -1;
        this.mBaseStationLatitude = Integer.MAX_VALUE;
        this.mBaseStationLongitude = Integer.MAX_VALUE;
        this.mSystemId = -1;
        this.mNetworkId = -1;
        this.mTac = -1;
        this.mLteCellId = -1;
        this.mBaseStationId = -1;
        this.mBaseStationLatitude = Integer.MAX_VALUE;
        this.mBaseStationLongitude = Integer.MAX_VALUE;
        this.mSystemId = -1;
        this.mNetworkId = -1;
        this.mTac = -1;
        this.mLteCellId = -1;
    }

    public CdmaCellLocation(Bundle bundle) {
        this.mBaseStationId = -1;
        this.mBaseStationLatitude = Integer.MAX_VALUE;
        this.mBaseStationLongitude = Integer.MAX_VALUE;
        this.mSystemId = -1;
        this.mNetworkId = -1;
        this.mTac = -1;
        this.mLteCellId = -1;
        this.mBaseStationId = bundle.getInt("baseStationId", this.mBaseStationId);
        this.mBaseStationLatitude = bundle.getInt("baseStationLatitude", this.mBaseStationLatitude);
        this.mBaseStationLongitude = bundle.getInt("baseStationLongitude", this.mBaseStationLongitude);
        this.mSystemId = bundle.getInt("systemId", this.mSystemId);
        this.mNetworkId = bundle.getInt("networkId", this.mNetworkId);
        this.mTac = bundle.getInt("mTac", this.mTac);
        this.mLteCellId = bundle.getInt("mLteCellId", this.mLteCellId);
    }

    public int getBaseStationId() {
        return this.mBaseStationId;
    }

    public int getBaseStationLatitude() {
        return this.mBaseStationLatitude;
    }

    public int getBaseStationLongitude() {
        return this.mBaseStationLongitude;
    }

    public int getSystemId() {
        return this.mSystemId;
    }

    public int getNetworkId() {
        return this.mNetworkId;
    }

    public int getLteTac() {
        return this.mTac;
    }

    public int getLteCellId() {
        return this.mLteCellId;
    }

    public void setStateInvalid() {
        this.mBaseStationId = -1;
        this.mBaseStationLatitude = Integer.MAX_VALUE;
        this.mBaseStationLongitude = Integer.MAX_VALUE;
        this.mSystemId = -1;
        this.mNetworkId = -1;
        this.mTac = -1;
        this.mLteCellId = -1;
    }

    public void setCellLocationData(int baseStationId, int baseStationLatitude, int baseStationLongitude) {
        this.mBaseStationId = baseStationId;
        this.mBaseStationLatitude = baseStationLatitude;
        this.mBaseStationLongitude = baseStationLongitude;
    }

    public void setCellLocationData(int baseStationId, int baseStationLatitude, int baseStationLongitude, int systemId, int networkId) {
        this.mBaseStationId = baseStationId;
        this.mBaseStationLatitude = baseStationLatitude;
        this.mBaseStationLongitude = baseStationLongitude;
        this.mSystemId = systemId;
        this.mNetworkId = networkId;
    }

    public void setLteCellLocationData(int tac, int lteCellId) {
        this.mTac = tac;
        this.mLteCellId = lteCellId;
    }

    public int hashCode() {
        return (((this.mBaseStationId ^ this.mBaseStationLatitude) ^ this.mBaseStationLongitude) ^ this.mSystemId) ^ this.mNetworkId;
    }

    public boolean equals(Object o) {
        try {
            CdmaCellLocation s = (CdmaCellLocation) o;
            if (o != null && equalsHandlesNulls(Integer.valueOf(this.mBaseStationId), Integer.valueOf(s.mBaseStationId)) && equalsHandlesNulls(Integer.valueOf(this.mBaseStationLatitude), Integer.valueOf(s.mBaseStationLatitude)) && equalsHandlesNulls(Integer.valueOf(this.mBaseStationLongitude), Integer.valueOf(s.mBaseStationLongitude)) && equalsHandlesNulls(Integer.valueOf(this.mSystemId), Integer.valueOf(s.mSystemId)) && equalsHandlesNulls(Integer.valueOf(this.mNetworkId), Integer.valueOf(s.mNetworkId)) && equalsHandlesNulls(Integer.valueOf(this.mTac), Integer.valueOf(s.mTac)) && equalsHandlesNulls(Integer.valueOf(this.mLteCellId), Integer.valueOf(s.mLteCellId))) {
                return true;
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        return "[" + this.mBaseStationId + FingerprintManager.FINGER_PERMISSION_DELIMITER + this.mBaseStationLatitude + FingerprintManager.FINGER_PERMISSION_DELIMITER + this.mBaseStationLongitude + FingerprintManager.FINGER_PERMISSION_DELIMITER + this.mSystemId + FingerprintManager.FINGER_PERMISSION_DELIMITER + this.mNetworkId + FingerprintManager.FINGER_PERMISSION_DELIMITER + this.mTac + FingerprintManager.FINGER_PERMISSION_DELIMITER + this.mLteCellId + "]";
    }

    private static boolean equalsHandlesNulls(Object a, Object b) {
        if (a == null) {
            return b == null;
        } else {
            return a.equals(b);
        }
    }

    public void fillInNotifierBundle(Bundle bundleToFill) {
        bundleToFill.putInt("baseStationId", this.mBaseStationId);
        bundleToFill.putInt("baseStationLatitude", this.mBaseStationLatitude);
        bundleToFill.putInt("baseStationLongitude", this.mBaseStationLongitude);
        bundleToFill.putInt("systemId", this.mSystemId);
        bundleToFill.putInt("networkId", this.mNetworkId);
        bundleToFill.putInt("mTac", this.mTac);
        bundleToFill.putInt("mLteCellId", this.mLteCellId);
    }

    public boolean isEmpty() {
        return this.mBaseStationId == -1 && this.mBaseStationLatitude == Integer.MAX_VALUE && this.mBaseStationLongitude == Integer.MAX_VALUE && this.mSystemId == -1 && this.mTac == -1 && this.mLteCellId == -1 && this.mNetworkId == -1;
    }

    public static double convertQuartSecToDecDegrees(int quartSec) {
        if (!Double.isNaN((double) quartSec) && quartSec >= -2592000 && quartSec <= 2592000) {
            return ((double) quartSec) / 14400.0d;
        }
        throw new IllegalArgumentException("Invalid coordiante value:" + quartSec);
    }
}
