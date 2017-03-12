package android.app;

import android.os.Bundle;

public class BroadcastOptions {
    public static final String KEY_TEMPORARY_APP_WHITELIST_DURATION = "android:broadcast.temporaryAppWhitelistDuration";
    private long mTemporaryAppWhitelistDuration;

    public static BroadcastOptions makeBasic() {
        return new BroadcastOptions();
    }

    private BroadcastOptions() {
    }

    public BroadcastOptions(Bundle opts) {
        this.mTemporaryAppWhitelistDuration = opts.getLong(KEY_TEMPORARY_APP_WHITELIST_DURATION);
    }

    public void setTemporaryAppWhitelistDuration(long duration) {
        this.mTemporaryAppWhitelistDuration = duration;
    }

    public long getTemporaryAppWhitelistDuration() {
        return this.mTemporaryAppWhitelistDuration;
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        if (this.mTemporaryAppWhitelistDuration > 0) {
            b.putLong(KEY_TEMPORARY_APP_WHITELIST_DURATION, this.mTemporaryAppWhitelistDuration);
        }
        return b.isEmpty() ? null : b;
    }
}
