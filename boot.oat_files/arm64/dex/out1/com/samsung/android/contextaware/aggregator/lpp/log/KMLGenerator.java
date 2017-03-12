package com.samsung.android.contextaware.aggregator.lpp.log;

import android.location.Location;
import android.util.Log;
import com.samsung.android.fingerprint.FingerprintManager;
import java.util.ArrayList;

public class KMLGenerator {
    private static String TAG = "KMLGenerator";
    String mCoordinates = "";
    ArrayList<Location> mGPSCoordinates = new ArrayList();
    String mLineStyle = "ffffffff";
    String mPlaceID = "temp";
    String mPolyStyleColor = "00ffffff";
    int mWidth = 10;

    public KMLGenerator(String ID) {
        SetPlaceID(ID);
    }

    public void SetLineStyle(String style) {
        this.mLineStyle = style;
    }

    void SetPolyStyleColor(String style) {
        this.mPolyStyleColor = style;
    }

    void SetLineWidth(int width) {
        this.mWidth = width;
    }

    void SetPlaceID(String ID) {
        this.mPlaceID = ID;
    }

    public String GenerateLineString() {
        return "";
    }

    public void AddCoordinate(ArrayList<Location> mLocation) {
        Log.d(TAG, "[KMLGen]LppLocation size : " + mLocation.size());
        for (int inx = 0; inx < mLocation.size(); inx++) {
            this.mCoordinates += ((Location) mLocation.get(inx)).getLongitude() + FingerprintManager.FINGER_PERMISSION_DELIMITER + ((Location) mLocation.get(inx)).getLatitude() + FingerprintManager.FINGER_PERMISSION_DELIMITER + ((Location) mLocation.get(inx)).getAltitude() + " ";
        }
    }

    public void AddCoordinateRT(Location loc) {
        this.mCoordinates += loc.getLongitude() + FingerprintManager.FINGER_PERMISSION_DELIMITER + loc.getLatitude() + FingerprintManager.FINGER_PERMISSION_DELIMITER + loc.getAltitude() + " ";
    }

    public void AddGPSCoordinate(ArrayList<Location> mLocation) {
        Log.d(TAG, "[KMLGen]LppLocation size : " + mLocation.size());
        for (int inx = 0; inx < mLocation.size(); inx++) {
            this.mGPSCoordinates.add(new Location((Location) mLocation.get(inx)));
        }
    }
}
