package android.security.keystore;

import java.util.Date;

abstract class Utils {
    private Utils() {
    }

    static Date cloneIfNotNull(Date value) {
        return value != null ? (Date) value.clone() : null;
    }
}
