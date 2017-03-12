package com.samsung.android.contextaware.creator;

import com.samsung.android.contextaware.manager.ContextComponent;

public interface IListObjectCreator {
    ContextComponent getObject();

    ContextComponent getObject(Object... objArr);

    ContextComponent getObjectForSubCollection();

    ContextComponent getObjectForSubCollection(Object... objArr);

    void removeObject(String str);
}
