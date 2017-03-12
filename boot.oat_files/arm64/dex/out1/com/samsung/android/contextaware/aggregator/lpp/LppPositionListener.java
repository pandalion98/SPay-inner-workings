package com.samsung.android.contextaware.aggregator.lpp;

import android.location.Location;
import java.util.ArrayList;

public interface LppPositionListener {
    void LPPStatus(String str);

    void LPPUpdate(ArrayList<Location> arrayList);

    void onLocationChanged(Location location);
}
