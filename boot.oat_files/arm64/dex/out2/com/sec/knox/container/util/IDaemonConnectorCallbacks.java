package com.sec.knox.container.util;

public interface IDaemonConnectorCallbacks {
    void onDaemonConnected();

    boolean onEvent(int i, String str, String[] strArr);
}
