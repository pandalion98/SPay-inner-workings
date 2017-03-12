package android.util;

import android.security.Credentials;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class MoreAtomicFile extends AtomicFile {
    protected static final boolean DEBUG = true;
    public static final String TAG = "MoreAtomicFile";
    private final File mMoreBackupName;
    private PrintWriter mPw;

    protected abstract boolean isDamaged(File file);

    public MoreAtomicFile(File baseName) {
        super(baseName);
        this.mPw = null;
        this.mMoreBackupName = new File(baseName.getPath() + ".mbak");
    }

    public MoreAtomicFile(File baseName, PrintWriter pw) {
        this(baseName);
        this.mPw = pw;
    }

    public void delete() {
        super.delete();
        this.mMoreBackupName.delete();
        File corruptedFile = new File(getBaseFile().getPath() + Credentials.EXTENSION_CRT);
        if (corruptedFile.exists()) {
            corruptedFile.delete();
        }
    }

    public FileOutputStream startWrite() throws IOException {
        if (isDamaged(getBaseFile())) {
            processDamagedFile();
        }
        if (!(getBaseFile().exists() || getBackupFile().exists() || !this.mMoreBackupName.exists())) {
            logToFile(this.mPw, "only more backup, rename " + this.mMoreBackupName + " to " + getBackupFile());
            this.mMoreBackupName.renameTo(getBaseFile());
        }
        return super.startWrite(this.mPw);
    }

    public void finishWrite(FileOutputStream str) {
        this.mMoreBackupName.delete();
        getBackupFile().renameTo(this.mMoreBackupName);
        super.finishWrite(str);
    }

    public FileInputStream openRead() throws FileNotFoundException {
        if (isDamaged(getBaseFile())) {
            processDamagedFile();
        }
        if (!(getBaseFile().exists() || getBackupFile().exists() || !this.mMoreBackupName.exists())) {
            logToFile(this.mPw, "only more backup, rename " + this.mMoreBackupName + " to " + getBackupFile());
            this.mMoreBackupName.renameTo(getBackupFile());
        }
        return super.openRead(this.mPw);
    }

    public void processDamagedFile() {
        saveDamagedFile();
        getBaseFile().delete();
    }

    void saveDamagedFile() {
        if (getBaseFile().exists()) {
            getBaseFile().renameTo(new File(getBaseFile().getPath() + Credentials.EXTENSION_CRT));
        }
    }
}
