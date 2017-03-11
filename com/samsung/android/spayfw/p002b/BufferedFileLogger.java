package com.samsung.android.spayfw.p002b;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import com.samsung.android.spayfw.utils.IOExceptionHandler;
import com.samsung.android.spayfw.utils.IOExceptionHandler.C0403a;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

@SuppressLint({"LongLogTag"})
/* renamed from: com.samsung.android.spayfw.b.a */
public final class BufferedFileLogger extends Logger {
    private static SimpleDateFormat oA;
    private static String oB;
    private String oC;
    private BufferedFileLogger oD;

    /* renamed from: com.samsung.android.spayfw.b.a.1 */
    class BufferedFileLogger implements Comparator<File> {
        final /* synthetic */ BufferedFileLogger oE;

        BufferedFileLogger(BufferedFileLogger bufferedFileLogger) {
            this.oE = bufferedFileLogger;
        }

        public int compare(File file, File file2) {
            long parseLong = Long.parseLong(file.getName());
            long parseLong2 = Long.parseLong(file2.getName());
            if (parseLong > parseLong2) {
                return 1;
            }
            return parseLong2 > parseLong ? -1 : 0;
        }
    }

    /* renamed from: com.samsung.android.spayfw.b.a.2 */
    class BufferedFileLogger extends C0403a<FileInputStream> {
        final /* synthetic */ BufferedFileLogger oE;
        final /* synthetic */ FileOutputStream oF;
        final /* synthetic */ File val$file;

        BufferedFileLogger(BufferedFileLogger bufferedFileLogger, File file, FileOutputStream fileOutputStream) {
            this.oE = bufferedFileLogger;
            this.val$file = file;
            this.oF = fileOutputStream;
        }

        public /* synthetic */ Object aJ() {
            return bZ();
        }

        public /* synthetic */ void m269c(Object obj) {
            m268b((FileInputStream) obj);
        }

        public /* synthetic */ void m270f(Object obj) {
            m267a((FileInputStream) obj);
        }

        public FileInputStream bZ() {
            return new FileInputStream(this.val$file);
        }

        public void m267a(FileInputStream fileInputStream) {
            byte[] bArr = new byte[PKIFailureInfo.certConfirmed];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read >= 0) {
                    this.oF.write(bArr, 0, read);
                } else {
                    return;
                }
            }
        }

        public void m268b(FileInputStream fileInputStream) {
            fileInputStream.close();
        }
    }

    /* renamed from: com.samsung.android.spayfw.b.a.3 */
    class BufferedFileLogger implements FilenameFilter {
        final /* synthetic */ BufferedFileLogger oE;

        BufferedFileLogger(BufferedFileLogger bufferedFileLogger) {
            this.oE = bufferedFileLogger;
        }

        public boolean accept(File file, String str) {
            if (BufferedFileLogger.m274c(System.currentTimeMillis()) - Long.parseLong(str) > 86400) {
                return true;
            }
            return false;
        }
    }

    /* renamed from: com.samsung.android.spayfw.b.a.a */
    private class BufferedFileLogger extends Handler {
        final /* synthetic */ BufferedFileLogger oE;
        private int oG;
        private BufferedWriter oH;
        private String oI;

        public BufferedFileLogger(BufferedFileLogger bufferedFileLogger, Looper looper) {
            this.oE = bufferedFileLogger;
            super(looper);
            this.oG = 1;
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    if (this.oG > 100) {
                        this.oG = 0;
                        this.oE.bX();
                    }
                    this.oG++;
                    Date date = new Date();
                    StringBuilder stringBuilder = (StringBuilder) message.obj;
                    stringBuilder.insert(0, BufferedFileLogger.oA.format(date));
                    String str = this.oE.oC + "/" + BufferedFileLogger.m274c(date.getTime());
                    if (this.oI == null || !this.oI.equals(str)) {
                        if (this.oH != null) {
                            Log.d("BufferedFileLogger", "Close File");
                            try {
                                this.oH.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d("BufferedFileLogger", "Create File");
                        this.oI = str;
                        File file = new File(this.oI);
                        if (!(file.exists() || file.getParentFile() == null)) {
                            file.getParentFile().mkdirs();
                        }
                        try {
                            this.oH = new BufferedWriter(new FileWriter(this.oI, true));
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            this.oI = null;
                            return;
                        }
                    }
                    try {
                        this.oH.write(stringBuilder.toString());
                        this.oH.newLine();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    Log.d("BufferedFileLogger", "Flush File");
                    if (this.oH != null) {
                        try {
                            this.oH.flush();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    if (message.obj != null && (message.obj instanceof CountDownLatch)) {
                        ((CountDownLatch) message.obj).countDown();
                    }
                default:
                    Log.e("BufferedFileLogger", "Invalid File Log Handler Message Type" + message.what);
            }
        }
    }

    static {
        oA = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZZ");
        oB = " | ";
    }

    private static final long m274c(long j) {
        long j2 = j / 1000;
        return j2 - (j2 % 86400);
    }

    public BufferedFileLogger(Context context, String str) {
        super(str);
        this.oC = context.getFilesDir().getAbsolutePath() + "/logs";
        HandlerThread handlerThread = new HandlerThread("BufferedLogFileWriterThread", 10);
        bX();
        handlerThread.start();
        this.oD = new BufferedFileLogger(this, handlerThread.getLooper());
    }

    public void m277a(FileOutputStream fileOutputStream) {
        File file = new File(this.oC);
        if (file.exists()) {
            flush();
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                Arrays.sort(listFiles, new BufferedFileLogger(this));
                for (File bufferedFileLogger : listFiles) {
                    IOExceptionHandler.m1262a(new BufferedFileLogger(this, bufferedFileLogger, fileOutputStream), false);
                }
            }
        }
    }

    public void flush() {
        Log.i("BufferedFileLogger", "flush start");
        bX();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        this.oD.sendMessageAtFrontOfQueue(this.oD.obtainMessage(2, countDownLatch));
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("BufferedFileLogger", "flush complete");
    }

    @SuppressLint({"LogTagMismatch"})
    public void m276a(int i, String str, String str2) {
        if (isLoggable(i)) {
            StringBuilder stringBuilder = new StringBuilder();
            if (str2.length() > 100001) {
                Log.w("BufferedFileLoggerAVOIDOOM", "trimming msg to avoid OOM");
                str2 = str2.substring(0, 100000);
            }
            stringBuilder.append(oB).append(Process.myPid()).append(oB).append(Process.myTid()).append(oB).append(Log.m280I(i)).append(oB).append("SpayFw_" + str).append(oB).append(str2);
            this.oD.obtainMessage(1, stringBuilder).sendToTarget();
        }
    }

    private synchronized void bX() {
        Log.d("BufferedFileLogger", "cleanupLogs");
        File file = new File(this.oC);
        if (file.exists()) {
            File[] listFiles = file.listFiles(new BufferedFileLogger(this));
            if (listFiles != null) {
                for (File delete : listFiles) {
                    delete.delete();
                }
            }
        }
    }
}
