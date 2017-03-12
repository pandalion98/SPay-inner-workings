package com.google.android.gms.common.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;

public final class Status implements Result, SafeParcelable {
    public static final StatusCreator CREATOR;
    public static final Status zzNo;
    public static final Status zzNp;
    public static final Status zzNq;
    public static final Status zzNr;
    public static final Status zzNs;
    private final PendingIntent mPendingIntent;
    private final int zzFG;
    private final int zzLs;
    private final String zzNt;

    static {
        zzNo = new Status(0);
        zzNp = new Status(14);
        zzNq = new Status(8);
        zzNr = new Status(15);
        zzNs = new Status(16);
        CREATOR = new StatusCreator();
    }

    public Status(int i) {
        this(i, null);
    }

    Status(int i, int i2, String str, PendingIntent pendingIntent) {
        this.zzFG = i;
        this.zzLs = i2;
        this.zzNt = str;
        this.mPendingIntent = pendingIntent;
    }

    public Status(int i, String str) {
        this(1, i, str, null);
    }

    public Status(int i, String str, PendingIntent pendingIntent) {
        this(1, i, str, pendingIntent);
    }

    private String zzhK() {
        return this.zzNt != null ? this.zzNt : CommonStatusCodes.getStatusCodeString(this.zzLs);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Status)) {
            return false;
        }
        Status status = (Status) obj;
        return this.zzFG == status.zzFG && this.zzLs == status.zzLs && zzw.equal(this.zzNt, status.zzNt) && zzw.equal(this.mPendingIntent, status.mPendingIntent);
    }

    public PendingIntent getResolution() {
        return this.mPendingIntent;
    }

    public Status getStatus() {
        return this;
    }

    public int getStatusCode() {
        return this.zzLs;
    }

    public String getStatusMessage() {
        return this.zzNt;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public boolean hasResolution() {
        return this.mPendingIntent != null;
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.zzFG), Integer.valueOf(this.zzLs), this.zzNt, this.mPendingIntent);
    }

    public boolean isCanceled() {
        return this.zzLs == 16;
    }

    public boolean isInterrupted() {
        return this.zzLs == 14;
    }

    public boolean isSuccess() {
        return this.zzLs <= 0;
    }

    public void startResolutionForResult(Activity activity, int i) {
        if (hasResolution()) {
            activity.startIntentSenderForResult(this.mPendingIntent.getIntentSender(), i, null, 0, 0, 0);
        }
    }

    public String toString() {
        return zzw.zzk(this).zza("statusCode", zzhK()).zza("resolution", this.mPendingIntent).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        StatusCreator.zza(this, parcel, i);
    }

    PendingIntent zzip() {
        return this.mPendingIntent;
    }
}
