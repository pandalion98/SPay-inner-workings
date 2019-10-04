/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.utils.stash;

import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStash;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStashCoreImpl;

public class DataStashImpl
implements DataStash {
    DataStash dataStash = new DataStashCoreImpl();

    @Override
    public void deleteDataFromStorage(String string, String string2) {
        this.dataStash.deleteDataFromStorage(string, string2);
    }

    @Override
    public void deleteStorage(String string) {
        this.dataStash.deleteStorage(string);
    }

    @Override
    public String getDataFromStorage(String string, String string2) {
        return this.dataStash.getDataFromStorage(string, string2);
    }

    @Override
    public boolean isDataPresent(String string, String string2) {
        return this.dataStash.isDataPresent(string, string2);
    }

    @Override
    public void putDataIntoStorage(String string, String string2, String string3) {
        this.dataStash.putDataIntoStorage(string, string2, string3);
    }
}

