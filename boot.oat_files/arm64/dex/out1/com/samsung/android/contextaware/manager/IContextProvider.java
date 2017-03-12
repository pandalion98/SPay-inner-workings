package com.samsung.android.contextaware.manager;

import com.samsung.android.contextaware.manager.ContextAwareService.Listener;

public interface IContextProvider {
    void start(Listener listener, int i);

    void stop(Listener listener, int i);
}
