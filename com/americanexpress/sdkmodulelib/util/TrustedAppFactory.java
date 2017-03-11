package com.americanexpress.sdkmodulelib.util;

public class TrustedAppFactory {
    private static boolean mockTrustedApp;
    private static TrustedApp trustedApp;

    static {
        mockTrustedApp = false;
        trustedApp = null;
    }

    public static TrustedApp getTrustedApp() {
        if (mockTrustedApp) {
            if (trustedApp == null) {
                trustedApp = (TrustedApp) Class.forName("com.americanexpress.sdkmodule.test.MockTrustedAppImpl").newInstance();
            }
        } else if (trustedApp == null) {
            synchronized (TrustedAppFactory.class) {
                trustedApp = new TrustedAppImpl();
            }
        }
        return trustedApp;
    }

    public static boolean isMockTrustedApp() {
        return mockTrustedApp;
    }
}
