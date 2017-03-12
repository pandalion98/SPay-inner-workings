package com.samsung.android.multidisplay.common.datastructure;

interface ContextRelatedInstance extends RelationObject {
    void garbageCollect();

    void release();
}
