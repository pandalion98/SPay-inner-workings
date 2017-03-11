package com.mastercard.mobile_api.utils;

import java.util.List;

public abstract class ListFactory {
    static ListFactory INSTANCE;

    public abstract List getList();

    public static ListFactory getInstance() {
        return INSTANCE;
    }

    public static void setInstance(ListFactory listFactory) {
        INSTANCE = listFactory;
    }
}
