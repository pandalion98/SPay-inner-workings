package android.database.sqlite;

public class SQLiteDatabaseCorruptException extends SQLiteException {
    private String[] err_msg;
    private int[] err_num;
    private int mCorrupt_code;

    public SQLiteDatabaseCorruptException() {
        this.mCorrupt_code = 11;
        this.err_num = new int[]{11, 26};
        this.err_msg = new String[]{"database disk image is malformed", "file is encrypted or is not a database"};
    }

    public SQLiteDatabaseCorruptException(String error) {
        super(error);
        this.mCorrupt_code = 11;
        this.err_num = new int[]{11, 26};
        this.err_msg = new String[]{"database disk image is malformed", "file is encrypted or is not a database"};
        for (int i = 0; i < this.err_msg.length; i++) {
            if (error.indexOf(this.err_msg[i]) >= 0) {
                this.mCorrupt_code = this.err_num[i];
                return;
            }
        }
    }

    public int getCorruptCode() {
        return this.mCorrupt_code;
    }
}
