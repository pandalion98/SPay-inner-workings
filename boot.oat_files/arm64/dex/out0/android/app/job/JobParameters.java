package android.app.job;

import android.app.job.IJobCallback.Stub;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;

public class JobParameters implements Parcelable {
    public static final Creator<JobParameters> CREATOR = new Creator<JobParameters>() {
        public JobParameters createFromParcel(Parcel in) {
            return new JobParameters(in);
        }

        public JobParameters[] newArray(int size) {
            return new JobParameters[size];
        }
    };
    private final IBinder callback;
    private final PersistableBundle extras;
    private final int jobId;
    private final boolean overrideDeadlineExpired;

    public JobParameters(IBinder callback, int jobId, PersistableBundle extras, boolean overrideDeadlineExpired) {
        this.jobId = jobId;
        this.extras = extras;
        this.callback = callback;
        this.overrideDeadlineExpired = overrideDeadlineExpired;
    }

    public int getJobId() {
        return this.jobId;
    }

    public PersistableBundle getExtras() {
        return this.extras;
    }

    public boolean isOverrideDeadlineExpired() {
        return this.overrideDeadlineExpired;
    }

    public IJobCallback getCallback() {
        return Stub.asInterface(this.callback);
    }

    private JobParameters(Parcel in) {
        boolean z = true;
        this.jobId = in.readInt();
        this.extras = in.readPersistableBundle();
        this.callback = in.readStrongBinder();
        if (in.readInt() != 1) {
            z = false;
        }
        this.overrideDeadlineExpired = z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.jobId);
        dest.writePersistableBundle(this.extras);
        dest.writeStrongBinder(this.callback);
        dest.writeInt(this.overrideDeadlineExpired ? 1 : 0);
    }
}
