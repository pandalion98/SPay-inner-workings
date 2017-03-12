package com.samsung.sensorframework.sdi.p047d;

import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.List;

/* renamed from: com.samsung.sensorframework.sdi.d.b */
public class PoIProximityDataHolder {
    private double LB;
    private List<String> LC;

    public PoIProximityDataHolder(List<String> list, double d) {
        this.LC = list;
        this.LB = d;
    }

    public double hY() {
        return this.LB;
    }

    public List<String> hZ() {
        return this.LC;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("NearbyPoIs size: ");
        stringBuilder.append(this.LC != null ? this.LC.size() + BuildConfig.FLAVOR : "null");
        stringBuilder.append(", distanceToClosestPoI: ");
        stringBuilder.append(this.LB);
        return stringBuilder.toString();
    }
}
