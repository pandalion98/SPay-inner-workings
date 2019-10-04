/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 */
package com.americanexpress.sdkmodulelib.storage;

import com.americanexpress.sdkmodulelib.storage.StorageManager;

public class StorageFactory {
    private static StorageManager storageManager;

    private StorageFactory() {
    }

    public static void closeStorageConnection() {
        if (storageManager != null) {
            storageManager = null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public static StorageManager getStorageManager() {
        if (storageManager != null) return storageManager;
        Class<StorageFactory> class_ = StorageFactory.class;
        // MONITORENTER : com.americanexpress.sdkmodulelib.storage.StorageFactory.class
        storageManager = new StorageManager();
        // MONITOREXIT : class_
        return storageManager;
    }
}

