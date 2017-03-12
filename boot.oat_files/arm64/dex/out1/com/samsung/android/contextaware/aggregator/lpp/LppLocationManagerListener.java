package com.samsung.android.contextaware.aggregator.lpp;

import android.location.Location;
import java.util.ArrayList;

public interface LppLocationManagerListener {
    void batchLocListUpdate(ArrayList<Location> arrayList);

    void batchLocUpdate(Location location);

    void gpsAvailable();

    void gpsBatchStarted();

    void gpsBatchStopped();

    void gpsOffBatchStopped();

    void gpsOnBatchStopped();

    void gpsUnavailable();

    void locPassBatchUpdate(Location location);

    void locPassUpdate(Location location);

    void locUpdate(ArrayList<Location> arrayList);

    void locationNotFound();

    void logData(String str);

    void logNmeaData(String str);

    void status(String str);
}
