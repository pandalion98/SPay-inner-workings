package android.net.wifi.hs20;

import com.android.org.conscrypt.HS20SSLContextImpl;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;

public class WifiHs20OCSPStaplingProvider extends Provider {
    private static final Provider WifiHs20OCSPStaplingProvider = null;

    public WifiHs20OCSPStaplingProvider() {
        super("OCSPStaplingProvider", 1.0d, "Samsung ocsp stapling provider with mandatory certificate revocation check");
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                WifiHs20OCSPStaplingProvider.this.setup();
                return null;
            }
        });
    }

    private void setup() {
        put("SSLContext.HS20", HS20SSLContextImpl.class.getName());
    }
}
