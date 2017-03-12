package com.samsung.android.contextaware.aggregator.lpp;

import android.location.Location;
import java.util.ArrayList;

public interface LppAlgoListener {
    void logData(int i, String str);

    void onUpdate(Location location);

    void onUpdateLPPtraj(ArrayList<LppLocation> arrayList);

    void requestLoc();

    void status(String str);
}
