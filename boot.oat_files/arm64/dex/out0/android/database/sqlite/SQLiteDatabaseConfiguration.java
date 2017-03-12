package android.database.sqlite;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SQLiteDatabaseConfiguration {
    private static final Pattern EMAIL_IN_DB_PATTERN = Pattern.compile("[\\w\\.\\-]+@[\\w\\.\\-]+");
    public static final String MEMORY_DB_PATH = ":memory:";
    public final ArrayList<SQLiteCustomFunction> customFunctions = new ArrayList();
    public boolean foreignKeyConstraintsEnabled;
    public final String label;
    public Locale locale;
    public int maxSqlCacheSize;
    public int openFlags;
    public final String path;

    public SQLiteDatabaseConfiguration(String path, int openFlags) {
        if (path == null) {
            throw new IllegalArgumentException("path must not be null.");
        }
        this.path = path;
        this.label = stripPathForLogs(path);
        this.openFlags = openFlags;
        this.maxSqlCacheSize = 25;
        this.locale = Locale.getDefault();
    }

    public SQLiteDatabaseConfiguration(SQLiteDatabaseConfiguration other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null.");
        }
        this.path = other.path;
        this.label = other.label;
        updateParametersFrom(other);
    }

    public void updateParametersFrom(SQLiteDatabaseConfiguration other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null.");
        } else if (this.path.equals(other.path)) {
            this.openFlags = other.openFlags;
            this.maxSqlCacheSize = other.maxSqlCacheSize;
            this.locale = other.locale;
            this.foreignKeyConstraintsEnabled = other.foreignKeyConstraintsEnabled;
            this.customFunctions.clear();
            this.customFunctions.addAll(other.customFunctions);
        } else {
            throw new IllegalArgumentException("other configuration must refer to the same database.");
        }
    }

    public boolean isInMemoryDb() {
        return this.path.equalsIgnoreCase(MEMORY_DB_PATH);
    }

    private static String stripPathForLogs(String path) {
        return path.indexOf(64) == -1 ? path : EMAIL_IN_DB_PATTERN.matcher(path).replaceAll("XX@YY");
    }
}
