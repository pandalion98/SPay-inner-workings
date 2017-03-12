package com.absolute.android.persistservice;

import android.content.pm.IPackageDeleteObserver.Stub;
import android.util.Log;

public class ABTPackageDeleteObserver extends Stub {
    boolean finished;
    int result;

    public void packageDeleted(String packageName, int returnCode) {
        Log.d("Absolute", "ABTPackageDeleteObserver: " + packageName + " returnCode: " + returnCode);
        synchronized (this) {
            this.finished = true;
            this.result = returnCode;
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
