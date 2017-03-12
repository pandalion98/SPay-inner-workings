package com.google.android.gms.location;

import android.content.Intent;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import java.util.Collections;
import java.util.List;

public class ActivityRecognitionResult implements SafeParcelable {
    public static final ActivityRecognitionResultCreator CREATOR;
    public static final String EXTRA_ACTIVITY_RESULT = "com.google.android.location.internal.EXTRA_ACTIVITY_RESULT";
    private final int zzFG;
    List<DetectedActivity> zzalH;
    long zzalI;
    long zzalJ;

    static {
        CREATOR = new ActivityRecognitionResultCreator();
    }

    public ActivityRecognitionResult(int i, List<DetectedActivity> list, long j, long j2) {
        this.zzFG = 1;
        this.zzalH = list;
        this.zzalI = j;
        this.zzalJ = j2;
    }

    public ActivityRecognitionResult(DetectedActivity detectedActivity, long j, long j2) {
        this(Collections.singletonList(detectedActivity), j, j2);
    }

    public ActivityRecognitionResult(List<DetectedActivity> list, long j, long j2) {
        boolean z = false;
        boolean z2 = list != null && list.size() > 0;
        zzx.zzb(z2, (Object) "Must have at least 1 detected activity");
        if (j > 0 && j2 > 0) {
            z = true;
        }
        zzx.zzb(z, (Object) "Must set times");
        this.zzFG = 1;
        this.zzalH = list;
        this.zzalI = j;
        this.zzalJ = j2;
    }

    public static ActivityRecognitionResult extractResult(Intent intent) {
        if (!hasResult(intent)) {
            return null;
        }
        Object obj = intent.getExtras().get(EXTRA_ACTIVITY_RESULT);
        if (!(obj instanceof byte[])) {
            return obj instanceof ActivityRecognitionResult ? (ActivityRecognitionResult) obj : null;
        } else {
            Parcel obtain = Parcel.obtain();
            obtain.unmarshall((byte[]) obj, 0, ((byte[]) obj).length);
            obtain.setDataPosition(0);
            return CREATOR.createFromParcel(obtain);
        }
    }

    public static boolean hasResult(Intent intent) {
        return intent == null ? false : intent.hasExtra(EXTRA_ACTIVITY_RESULT);
    }

    public int describeContents() {
        return 0;
    }

    public int getActivityConfidence(int i) {
        for (DetectedActivity detectedActivity : this.zzalH) {
            if (detectedActivity.getType() == i) {
                return detectedActivity.getConfidence();
            }
        }
        return 0;
    }

    public long getElapsedRealtimeMillis() {
        return this.zzalJ;
    }

    public DetectedActivity getMostProbableActivity() {
        return (DetectedActivity) this.zzalH.get(0);
    }

    public List<DetectedActivity> getProbableActivities() {
        return this.zzalH;
    }

    public long getTime() {
        return this.zzalI;
    }

    public int getVersionCode() {
        return this.zzFG;
    }

    public String toString() {
        return "ActivityRecognitionResult [probableActivities=" + this.zzalH + ", timeMillis=" + this.zzalI + ", elapsedRealtimeMillis=" + this.zzalJ + "]";
    }

    public void writeToParcel(Parcel parcel, int i) {
        ActivityRecognitionResultCreator.zza(this, parcel, i);
    }
}
