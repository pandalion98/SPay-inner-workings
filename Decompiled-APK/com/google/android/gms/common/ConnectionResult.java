package com.google.android.gms.common;

import android.app.Activity;
import android.app.PendingIntent;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;

public final class ConnectionResult implements SafeParcelable {
    public static final int API_UNAVAILABLE = 16;
    public static final int CANCELED = 13;
    public static final ConnectionResultCreator CREATOR;
    public static final int DEVELOPER_ERROR = 10;
    @Deprecated
    public static final int DRIVE_EXTERNAL_STORAGE_REQUIRED = 1500;
    public static final int INTERNAL_ERROR = 8;
    public static final int INTERRUPTED = 15;
    public static final int INVALID_ACCOUNT = 5;
    public static final int LICENSE_CHECK_FAILED = 11;
    public static final int NETWORK_ERROR = 7;
    public static final int RESOLUTION_REQUIRED = 6;
    public static final int SERVICE_DISABLED = 3;
    public static final int SERVICE_INVALID = 9;
    public static final int SERVICE_MISSING = 1;
    public static final int SERVICE_VERSION_UPDATE_REQUIRED = 2;
    public static final int SIGN_IN_FAILED = 17;
    public static final int SIGN_IN_REQUIRED = 4;
    public static final int SUCCESS = 0;
    public static final int TIMEOUT = 14;
    public static final ConnectionResult zzLr;
    private final PendingIntent mPendingIntent;
    final int zzFG;
    private final int zzLs;

    static {
        zzLr = new ConnectionResult(SUCCESS, null);
        CREATOR = new ConnectionResultCreator();
    }

    ConnectionResult(int i, int i2, PendingIntent pendingIntent) {
        this.zzFG = i;
        this.zzLs = i2;
        this.mPendingIntent = pendingIntent;
    }

    public ConnectionResult(int i, PendingIntent pendingIntent) {
        this(SERVICE_MISSING, i, pendingIntent);
    }

    private String zzhK() {
        switch (this.zzLs) {
            case SUCCESS /*0*/:
                return "SUCCESS";
            case SERVICE_MISSING /*1*/:
                return "SERVICE_MISSING";
            case SERVICE_VERSION_UPDATE_REQUIRED /*2*/:
                return "SERVICE_VERSION_UPDATE_REQUIRED";
            case SERVICE_DISABLED /*3*/:
                return "SERVICE_DISABLED";
            case SIGN_IN_REQUIRED /*4*/:
                return "SIGN_IN_REQUIRED";
            case INVALID_ACCOUNT /*5*/:
                return "INVALID_ACCOUNT";
            case RESOLUTION_REQUIRED /*6*/:
                return "RESOLUTION_REQUIRED";
            case NETWORK_ERROR /*7*/:
                return "NETWORK_ERROR";
            case INTERNAL_ERROR /*8*/:
                return "INTERNAL_ERROR";
            case SERVICE_INVALID /*9*/:
                return "SERVICE_INVALID";
            case DEVELOPER_ERROR /*10*/:
                return "DEVELOPER_ERROR";
            case LICENSE_CHECK_FAILED /*11*/:
                return "LICENSE_CHECK_FAILED";
            case CANCELED /*13*/:
                return "CANCELED";
            case TIMEOUT /*14*/:
                return "TIMEOUT";
            case INTERRUPTED /*15*/:
                return "INTERRUPTED";
            case API_UNAVAILABLE /*16*/:
                return "API_UNAVAILABLE";
            case SIGN_IN_FAILED /*17*/:
                return "SIGN_IN_FAILED";
            default:
                return "unknown status code " + this.zzLs;
        }
    }

    public int describeContents() {
        return SUCCESS;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ConnectionResult)) {
            return false;
        }
        ConnectionResult connectionResult = (ConnectionResult) obj;
        return this.zzLs == connectionResult.zzLs && zzw.equal(this.mPendingIntent, connectionResult.mPendingIntent);
    }

    public int getErrorCode() {
        return this.zzLs;
    }

    public PendingIntent getResolution() {
        return this.mPendingIntent;
    }

    public boolean hasResolution() {
        return (this.zzLs == 0 || this.mPendingIntent == null) ? false : true;
    }

    public int hashCode() {
        Object[] objArr = new Object[SERVICE_VERSION_UPDATE_REQUIRED];
        objArr[SUCCESS] = Integer.valueOf(this.zzLs);
        objArr[SERVICE_MISSING] = this.mPendingIntent;
        return zzw.hashCode(objArr);
    }

    public boolean isSuccess() {
        return this.zzLs == 0;
    }

    public void startResolutionForResult(Activity activity, int i) {
        if (hasResolution()) {
            activity.startIntentSenderForResult(this.mPendingIntent.getIntentSender(), i, null, SUCCESS, SUCCESS, SUCCESS);
        }
    }

    public String toString() {
        return zzw.zzk(this).zza("statusCode", zzhK()).zza("resolution", this.mPendingIntent).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        ConnectionResultCreator.zza(this, parcel, i);
    }
}
