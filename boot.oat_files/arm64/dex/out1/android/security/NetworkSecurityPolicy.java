package android.security;

public class NetworkSecurityPolicy {
    private static final NetworkSecurityPolicy INSTANCE = new NetworkSecurityPolicy();

    private NetworkSecurityPolicy() {
    }

    public static NetworkSecurityPolicy getInstance() {
        return INSTANCE;
    }

    public boolean isCleartextTrafficPermitted() {
        return libcore.net.NetworkSecurityPolicy.isCleartextTrafficPermitted();
    }

    public void setCleartextTrafficPermitted(boolean permitted) {
        libcore.net.NetworkSecurityPolicy.setCleartextTrafficPermitted(permitted);
    }
}
