package android.provider;

import android.net.Uri;

public final class SecurityContract {
    public static final String AUTHORITY = "com.android.security";
    public static final Uri AUTHORITY_URI = Uri.parse("content://com.android.security");

    protected interface DataColumns {
        public static final String NAME = "name";
        public static final String TYPE = "type";
        public static final String VALUE = "value";
    }

    protected interface PasswordColumns {
        public static final String PASSWORD = "password";
        public static final String SET_DATE = "set_date";
    }

    public static final class Tables {

        public static final class PASSWORDS {
            public static final Uri CONTENT_URI = Uri.withAppendedPath(SecurityContract.AUTHORITY_URI, "password");
            public static final String TABLE_NAME = "passwords";

            public static final class Columns implements BaseColumns, PasswordColumns {
            }
        }
    }
}
