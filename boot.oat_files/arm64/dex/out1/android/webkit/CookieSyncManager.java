package android.webkit;

import android.content.Context;

@Deprecated
public final class CookieSyncManager extends WebSyncManager {
    private static boolean sGetInstanceAllowed = false;
    private static CookieSyncManager sRef;

    public /* bridge */ /* synthetic */ void run() {
        super.run();
    }

    private CookieSyncManager() {
        super(null, null);
    }

    public static synchronized CookieSyncManager getInstance() {
        CookieSyncManager cookieSyncManager;
        synchronized (CookieSyncManager.class) {
            checkInstanceIsAllowed();
            if (sRef == null) {
                sRef = new CookieSyncManager();
            }
            cookieSyncManager = sRef;
        }
        return cookieSyncManager;
    }

    public static synchronized CookieSyncManager createInstance(Context context) {
        CookieSyncManager instance;
        synchronized (CookieSyncManager.class) {
            if (context == null) {
                throw new IllegalArgumentException("Invalid context argument");
            }
            setGetInstanceIsAllowed();
            instance = getInstance();
        }
        return instance;
    }

    @Deprecated
    public void sync() {
        CookieManager.getInstance().flush();
    }

    @Deprecated
    protected void syncFromRamToFlash() {
        CookieManager.getInstance().flush();
    }

    @Deprecated
    public void resetSync() {
    }

    @Deprecated
    public void startSync() {
    }

    @Deprecated
    public void stopSync() {
    }

    static void setGetInstanceIsAllowed() {
        sGetInstanceAllowed = true;
    }

    private static void checkInstanceIsAllowed() {
        if (!sGetInstanceAllowed) {
            throw new IllegalStateException("CookieSyncManager::createInstance() needs to be called before CookieSyncManager::getInstance()");
        }
    }
}
