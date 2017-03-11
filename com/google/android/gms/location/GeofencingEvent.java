package com.google.android.gms.location;

import android.content.Intent;
import android.location.Location;
import com.google.android.gms.internal.zzpk;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeofencingEvent {
    private final int zzalV;
    private final List<Geofence> zzalW;
    private final Location zzalX;
    private final int zzvR;

    private GeofencingEvent(int i, int i2, List<Geofence> list, Location location) {
        this.zzvR = i;
        this.zzalV = i2;
        this.zzalW = list;
        this.zzalX = location;
    }

    public static GeofencingEvent fromIntent(Intent intent) {
        return intent == null ? null : new GeofencingEvent(intent.getIntExtra("gms_error_code", -1), zzl(intent), zzm(intent), (Location) intent.getParcelableExtra("com.google.android.location.intent.extra.triggering_location"));
    }

    private static int zzl(Intent intent) {
        int intExtra = intent.getIntExtra("com.google.android.location.intent.extra.transition", -1);
        return intExtra == -1 ? -1 : (intExtra == 1 || intExtra == 2 || intExtra == 4) ? intExtra : -1;
    }

    private static List<Geofence> zzm(Intent intent) {
        ArrayList arrayList = (ArrayList) intent.getSerializableExtra("com.google.android.location.intent.extra.geofence_list");
        if (arrayList == null) {
            return null;
        }
        ArrayList arrayList2 = new ArrayList(arrayList.size());
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(zzpk.zzi((byte[]) it.next()));
        }
        return arrayList2;
    }

    public int getErrorCode() {
        return this.zzvR;
    }

    public int getGeofenceTransition() {
        return this.zzalV;
    }

    public List<Geofence> getTriggeringGeofences() {
        return this.zzalW;
    }

    public Location getTriggeringLocation() {
        return this.zzalX;
    }

    public boolean hasError() {
        return this.zzvR != -1;
    }
}
