package android.util;

import android.security.Credentials;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class XmlMoreAtomicFile extends MoreAtomicFile {
    public XmlMoreAtomicFile(File baseName) {
        super(baseName);
    }

    public XmlMoreAtomicFile(File baseName, PrintWriter pw) {
        super(baseName, pw);
    }

    protected boolean isDamaged(File file) {
        Exception e;
        Throwable th;
        boolean isDamaged = false;
        if (file != null && file.exists()) {
            RandomAccessFile raf_xmltest = null;
            try {
                RandomAccessFile raf_xmltest2 = new RandomAccessFile(file, "r");
                try {
                    if (raf_xmltest2.length() <= 14) {
                        isDamaged = true;
                        Slog.d(MoreAtomicFile.TAG, file + " is too small, rename it to " + file + Credentials.EXTENSION_CRT);
                    }
                    if (raf_xmltest2 != null) {
                        try {
                            raf_xmltest2.close();
                        } catch (IOException ie) {
                            ie.printStackTrace();
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    raf_xmltest = raf_xmltest2;
                    isDamaged = true;
                    try {
                        Slog.d(MoreAtomicFile.TAG, file + " during check if it is damaged, rename it to " + file + Credentials.EXTENSION_CRT);
                        e.printStackTrace();
                        if (raf_xmltest != null) {
                            try {
                                raf_xmltest.close();
                            } catch (IOException ie2) {
                                ie2.printStackTrace();
                            }
                        }
                        return isDamaged;
                    } catch (Throwable th2) {
                        th = th2;
                        if (raf_xmltest != null) {
                            try {
                                raf_xmltest.close();
                            } catch (IOException ie22) {
                                ie22.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    raf_xmltest = raf_xmltest2;
                    if (raf_xmltest != null) {
                        raf_xmltest.close();
                    }
                    throw th;
                }
            } catch (Exception e3) {
                e = e3;
                isDamaged = true;
                Slog.d(MoreAtomicFile.TAG, file + " during check if it is damaged, rename it to " + file + Credentials.EXTENSION_CRT);
                e.printStackTrace();
                if (raf_xmltest != null) {
                    raf_xmltest.close();
                }
                return isDamaged;
            }
        }
        return isDamaged;
    }
}
