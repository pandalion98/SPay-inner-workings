package com.google.android.gms.location.places;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import java.util.List;

public class zzh implements Result {
    private final Status zzHb;
    private final List<Place> zzanQ;

    public zzh(Status status, List<Place> list) {
        this.zzHb = status;
        this.zzanQ = list;
    }

    public Status getStatus() {
        return this.zzHb;
    }
}
