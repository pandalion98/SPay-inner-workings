package com.android.internal.widget;

import android.os.AsyncTask;
import com.android.internal.widget.LockPatternUtils.RequestThrottledException;
import com.android.internal.widget.LockPatternView.Cell;
import java.util.List;

public final class LockPatternChecker {

    public interface OnCheckCallback {
        void onChecked(boolean z, int i);
    }

    public interface OnVerifyCallback {
        void onVerified(byte[] bArr, int i);
    }

    public static AsyncTask<?, ?, ?> verifyPattern(LockPatternUtils utils, List<Cell> pattern, long challenge, int userId, OnVerifyCallback callback) {
        final LockPatternUtils lockPatternUtils = utils;
        final List<Cell> list = pattern;
        final long j = challenge;
        final int i = userId;
        final OnVerifyCallback onVerifyCallback = callback;
        AsyncTask<Void, Void, byte[]> task = new AsyncTask<Void, Void, byte[]>() {
            private int mThrottleTimeout;

            protected byte[] doInBackground(Void... args) {
                try {
                    return lockPatternUtils.verifyPattern(list, j, i);
                } catch (RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return null;
                }
            }

            protected void onPostExecute(byte[] result) {
                onVerifyCallback.onVerified(result, this.mThrottleTimeout);
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    public static AsyncTask<?, ?, ?> checkPattern(final LockPatternUtils utils, final List<Cell> pattern, final int userId, final OnCheckCallback callback) {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            private int mThrottleTimeout;

            protected Boolean doInBackground(Void... args) {
                try {
                    return Boolean.valueOf(utils.checkPattern(pattern, userId));
                } catch (RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return Boolean.valueOf(false);
                }
            }

            protected void onPostExecute(Boolean result) {
                callback.onChecked(result.booleanValue(), this.mThrottleTimeout);
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    public static AsyncTask<?, ?, ?> verifyPassword(LockPatternUtils utils, String password, long challenge, int userId, OnVerifyCallback callback) {
        final LockPatternUtils lockPatternUtils = utils;
        final String str = password;
        final long j = challenge;
        final int i = userId;
        final OnVerifyCallback onVerifyCallback = callback;
        AsyncTask<Void, Void, byte[]> task = new AsyncTask<Void, Void, byte[]>() {
            private int mThrottleTimeout;

            protected byte[] doInBackground(Void... args) {
                try {
                    return lockPatternUtils.verifyPassword(str, j, i);
                } catch (RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return null;
                }
            }

            protected void onPostExecute(byte[] result) {
                onVerifyCallback.onVerified(result, this.mThrottleTimeout);
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    public static AsyncTask<?, ?, ?> checkPassword(final LockPatternUtils utils, final String password, final int userId, final OnCheckCallback callback) {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            private int mThrottleTimeout;

            protected Boolean doInBackground(Void... args) {
                try {
                    return Boolean.valueOf(utils.checkPassword(password, userId));
                } catch (RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return Boolean.valueOf(false);
                }
            }

            protected void onPostExecute(Boolean result) {
                callback.onChecked(result.booleanValue(), this.mThrottleTimeout);
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    public static AsyncTask<?, ?, ?> checkRemoteLockPassword(LockPatternUtils utils, int whichlock, String password, int userId, OnCheckCallback callback) {
        final LockPatternUtils lockPatternUtils = utils;
        final int i = whichlock;
        final String str = password;
        final int i2 = userId;
        final OnCheckCallback onCheckCallback = callback;
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            private int mThrottleTimeout;

            protected Boolean doInBackground(Void... args) {
                return Boolean.valueOf(lockPatternUtils.checkRemoteLockPassword(i, str, i2));
            }

            protected void onPostExecute(Boolean result) {
                onCheckCallback.onChecked(result.booleanValue(), this.mThrottleTimeout);
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    public static AsyncTask<?, ?, ?> checkBackupPin(final LockPatternUtils utils, final String password, final int userId, final OnCheckCallback callback) {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            private int mThrottleTimeout;

            protected Boolean doInBackground(Void... args) {
                try {
                    return Boolean.valueOf(utils.checkBackupPin(password, userId));
                } catch (RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return Boolean.valueOf(false);
                }
            }

            protected void onPostExecute(Boolean result) {
                callback.onChecked(result.booleanValue(), this.mThrottleTimeout);
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    public static AsyncTask<?, ?, ?> verifyBackupPin(LockPatternUtils utils, String password, long challenge, int userId, OnVerifyCallback callback) {
        final LockPatternUtils lockPatternUtils = utils;
        final String str = password;
        final long j = challenge;
        final int i = userId;
        final OnVerifyCallback onVerifyCallback = callback;
        AsyncTask<Void, Void, byte[]> task = new AsyncTask<Void, Void, byte[]>() {
            private int mThrottleTimeout;

            protected byte[] doInBackground(Void... args) {
                try {
                    return lockPatternUtils.verifyBackupPin(str, j, i);
                } catch (RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return null;
                }
            }

            protected void onPostExecute(byte[] result) {
                onVerifyCallback.onVerified(result, this.mThrottleTimeout);
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    public static AsyncTask<?, ?, ?> checkBackupPassword(LockPatternUtils utils, String password, int userId, boolean useKeystore, OnCheckCallback callback) {
        final LockPatternUtils lockPatternUtils = utils;
        final String str = password;
        final int i = userId;
        final boolean z = useKeystore;
        final OnCheckCallback onCheckCallback = callback;
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            private int mThrottleTimeout;

            protected Boolean doInBackground(Void... args) {
                try {
                    return Boolean.valueOf(lockPatternUtils.checkBackupPassword(str, i, z));
                } catch (RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return Boolean.valueOf(false);
                }
            }

            protected void onPostExecute(Boolean result) {
                onCheckCallback.onChecked(result.booleanValue(), this.mThrottleTimeout);
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    public static AsyncTask<?, ?, ?> verifyBackupPassword(LockPatternUtils utils, String password, long challenge, int userId, boolean useKeystore, OnVerifyCallback callback) {
        final LockPatternUtils lockPatternUtils = utils;
        final String str = password;
        final long j = challenge;
        final int i = userId;
        final boolean z = useKeystore;
        final OnVerifyCallback onVerifyCallback = callback;
        AsyncTask<Void, Void, byte[]> task = new AsyncTask<Void, Void, byte[]>() {
            private int mThrottleTimeout;

            protected byte[] doInBackground(Void... args) {
                try {
                    return lockPatternUtils.verifyBackupPassword(str, j, i, z);
                } catch (RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return null;
                }
            }

            protected void onPostExecute(byte[] result) {
                onVerifyCallback.onVerified(result, this.mThrottleTimeout);
            }
        };
        task.execute(new Void[0]);
        return task;
    }
}
