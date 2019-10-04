/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.utils.stash;

public interface DataStash {
    public static final String HCECLIENTSC_DATA_CONTEXT = "HCECLIENTSC_DATA_CONTEXT";
    public static final String HCECLIENT_DATA_CONTEXT = "HCECLIENT_DATA_CONTEXT";
    public static final String HCECLIENT_META_DATA = "HCECLIENT_META_DATA";
    public static final String TAG = "DataStash";

    public void deleteDataFromStorage(String var1, String var2);

    public void deleteStorage(String var1);

    public String getDataFromStorage(String var1, String var2);

    public boolean isDataPresent(String var1, String var2);

    public void putDataIntoStorage(String var1, String var2, String var3);
}

