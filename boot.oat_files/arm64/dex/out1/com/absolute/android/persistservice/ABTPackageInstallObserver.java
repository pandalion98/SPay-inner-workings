package com.absolute.android.persistservice;

import android.content.pm.IPackageInstallObserver.Stub;
import android.util.Log;

public class ABTPackageInstallObserver extends Stub {
    boolean finished;
    int result;

    public void packageInstalled(String packageName, int status) {
        Log.d("Absolute", "ABTPackageInstallObserver: " + packageName + " status: " + status);
        synchronized (this) {
            this.finished = true;
            this.result = status;
            notifyAll();
        }
    }

    public boolean getFinished() {
        return this.finished;
    }

    public int getResult() {
        return this.result;
    }
}
