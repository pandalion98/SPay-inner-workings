package com.samsung.android.contextaware.aggregator.lpp;

import android.location.Location;
import java.util.ArrayList;

public interface ILppDataProvider {
    void gpsAvailable();

    void gpsBatchStarted();

    void gpsOffBatchStopped();

    void gpsOnBatchStopped();

    void gpsUnavailable();

    void lppStatus(String str);

    void lppUpdate(ArrayList<Location> arrayList);

    void onLocationChanged(Location location);
}
