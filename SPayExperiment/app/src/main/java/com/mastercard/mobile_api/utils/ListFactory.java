/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.List
 */
package com.mastercard.mobile_api.utils;

import java.util.List;

public abstract class ListFactory {
    static ListFactory INSTANCE;

    public static ListFactory getInstance() {
        return INSTANCE;
    }

    public static void setInstance(ListFactory listFactory) {
        INSTANCE = listFactory;
    }

    public abstract List getList();
}

