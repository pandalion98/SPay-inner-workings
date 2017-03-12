package com.americanexpress.mobilepayments.hceclient.utils.stash;

public interface DataStash {
    public static final String HCECLIENTSC_DATA_CONTEXT = "HCECLIENTSC_DATA_CONTEXT";
    public static final String HCECLIENT_DATA_CONTEXT = "HCECLIENT_DATA_CONTEXT";
    public static final String HCECLIENT_META_DATA = "HCECLIENT_META_DATA";
    public static final String TAG = "DataStash";

    void deleteDataFromStorage(String str, String str2);

    void deleteStorage(String str);

    String getDataFromStorage(String str, String str2);

    boolean isDataPresent(String str, String str2);

    void putDataIntoStorage(String str, String str2, String str3);
}
