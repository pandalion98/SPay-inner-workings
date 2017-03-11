package com.americanexpress.mobilepayments.hceclient.utils.stash;

public class DataStashImpl implements DataStash {
    DataStash dataStash;

    public DataStashImpl() {
        this.dataStash = new DataStashCoreImpl();
    }

    public String getDataFromStorage(String str, String str2) {
        return this.dataStash.getDataFromStorage(str, str2);
    }

    public void putDataIntoStorage(String str, String str2, String str3) {
        this.dataStash.putDataIntoStorage(str, str2, str3);
    }

    public boolean isDataPresent(String str, String str2) {
        return this.dataStash.isDataPresent(str, str2);
    }

    public void deleteDataFromStorage(String str, String str2) {
        this.dataStash.deleteDataFromStorage(str, str2);
    }

    public void deleteStorage(String str) {
        this.dataStash.deleteStorage(str);
    }
}
