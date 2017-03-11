package com.americanexpress.sdkmodulelib.storage;

public class StorageFactory {
    private static StorageManager storageManager;

    private StorageFactory() {
    }

    public static StorageManager getStorageManager() {
        if (storageManager == null) {
            synchronized (StorageFactory.class) {
                storageManager = new StorageManager();
            }
        }
        return storageManager;
    }

    public static void closeStorageConnection() {
        if (storageManager != null) {
            storageManager = null;
        }
    }
}
