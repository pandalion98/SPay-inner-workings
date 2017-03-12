package com.samsung.android.contextaware.manager;

import android.os.Bundle;

public interface IContextObserver {
    void updateContext(String str, Bundle bundle);
}
