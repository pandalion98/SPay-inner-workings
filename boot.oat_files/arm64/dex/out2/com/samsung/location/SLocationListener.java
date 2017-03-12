package com.samsung.location;

import android.location.Address;
import android.location.Location;

public interface SLocationListener {
    void onLocationAvailable(Location[] locationArr);

    void onLocationChanged(Location location, Address address, String[] strArr);

    void onLocationChanged(Location location, String str, String[] strArr);
}
