/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.os.Looper
 *  android.os.Message
 *  android.os.Process
 *  android.util.Log
 *  java.io.BufferedWriter
 *  java.io.File
 *  java.io.FileInputStream
 *  java.io.FileOutputStream
 *  java.io.FileWriter
 *  java.io.FilenameFilter
 *  java.io.IOException
 *  java.io.Writer
 *  java.lang.InterruptedException
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.text.SimpleDateFormat
 *  java.util.Arrays
 *  java.util.Comparator
 *  java.util.Date
 *  java.util.concurrent.CountDownLatch
 */
package com.samsung.android.spayfw.b;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.samsung.android.spayfw.utils.IOExceptionHandler;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

@SuppressLint(value={"LongLogTag"})
public final class a
extends d {
    private static SimpleDateFormat oA = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZZ");
    private static String oB = " | ";
    private String oC;
    private a oD;

    public a(Context context, String string) {
        super(string);
        this.oC = context.getFilesDir().getAbsolutePath() + "/logs";
        HandlerThread handlerThread = new HandlerThread("BufferedLogFileWriterThread", 10);
        this.bX();
        handlerThread.start();
        this.oD = new a(handlerThread.getLooper());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void bX() {
        a a2 = this;
        synchronized (a2) {
            File[] arrfile;
            android.util.Log.d((String)"BufferedFileLogger", (String)"cleanupLogs");
            File file = new File(this.oC);
            boolean bl = file.exists();
            if (bl && (arrfile = file.listFiles(new FilenameFilter(){

                public boolean accept(File file, String string) {
                    long l2 = Long.parseLong((String)string);
                    return a.c(System.currentTimeMillis()) - l2 > 86400L;
                }
            })) != null) {
                int n2 = arrfile.length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    arrfile[i2].delete();
                }
            }
            return;
        }
    }

    private static final long c(long l2) {
        long l3 = l2 / 1000L;
        return l3 - l3 % 86400L;
    }

    @SuppressLint(value={"LogTagMismatch"})
    @Override
    public void a(int n2, String string, String string2) {
        if (this.isLoggable(n2)) {
            StringBuilder stringBuilder = new StringBuilder();
            if (string2.length() > 100001) {
                android.util.Log.w((String)"BufferedFileLoggerAVOIDOOM", (String)"trimming msg to avoid OOM");
                string2 = string2.substring(0, 100000);
            }
            stringBuilder.append(oB).append(Process.myPid()).append(oB).append(Process.myTid()).append(oB).append(Log.I(n2)).append(oB).append("SpayFw_" + string).append(oB).append(string2);
            this.oD.obtainMessage(1, (Object)stringBuilder).sendToTarget();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void a(FileOutputStream fileOutputStream) {
        File file = new File(this.oC);
        if (file.exists()) {
            this.flush();
            Object[] arrobject = file.listFiles();
            if (arrobject != null) {
                Arrays.sort((Object[])arrobject, (Comparator)new Comparator<File>(){

                    public int compare(File file, File file2) {
                        long l2;
                        long l3 = Long.parseLong((String)file.getName());
                        if (l3 > (l2 = Long.parseLong((String)file2.getName()))) {
                            return 1;
                        }
                        if (l2 > l3) {
                            return -1;
                        }
                        return 0;
                    }
                });
                int n2 = arrobject.length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    IOExceptionHandler.a(new IOExceptionHandler.a<FileInputStream>((File)arrobject[i2], fileOutputStream){
                        final /* synthetic */ FileOutputStream oF;
                        final /* synthetic */ File val$file;
                        {
                            this.val$file = file;
                            this.oF = fileOutputStream;
                        }

                        public void a(FileInputStream fileInputStream) {
                            int n2;
                            byte[] arrby = new byte[4096];
                            while ((n2 = fileInputStream.read(arrby)) >= 0) {
                                this.oF.write(arrby, 0, n2);
                            }
                        }

                        @Override
                        public /* synthetic */ Object aJ() {
                            return this.bZ();
                        }

                        public void b(FileInputStream fileInputStream) {
                            fileInputStream.close();
                        }

                        public FileInputStream bZ() {
                            return new FileInputStream(this.val$file);
                        }

                        @Override
                        public /* synthetic */ void c(Object object) {
                            this.b((FileInputStream)object);
                        }

                        @Override
                        public /* synthetic */ void f(Object object) {
                            this.a((FileInputStream)object);
                        }
                    }, false);
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void flush() {
        android.util.Log.i((String)"BufferedFileLogger", (String)"flush start");
        this.bX();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Message message = this.oD.obtainMessage(2, (Object)countDownLatch);
        this.oD.sendMessageAtFrontOfQueue(message);
        try {
            countDownLatch.await();
        }
        catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        android.util.Log.i((String)"BufferedFileLogger", (String)"flush complete");
    }

    private class a
    extends Handler {
        private int oG;
        private BufferedWriter oH;
        private String oI;

        public a(Looper looper) {
            super(looper);
            this.oG = 1;
        }

        /*
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        public void handleMessage(Message message) {
            switch (message.what) {
                default: {
                    android.util.Log.e((String)"BufferedFileLogger", (String)("Invalid File Log Handler Message Type" + message.what));
                    return;
                }
                case 1: {
                    if (this.oG > 100) {
                        this.oG = 0;
                        a.this.bX();
                    }
                    this.oG = 1 + this.oG;
                    Date date = new Date();
                    String string = oA.format(date);
                    StringBuilder stringBuilder = (StringBuilder)message.obj;
                    stringBuilder.insert(0, string);
                    String string2 = a.this.oC + "/" + a.c(date.getTime());
                    if (this.oI == null || !this.oI.equals((Object)string2)) {
                        if (this.oH != null) {
                            android.util.Log.d((String)"BufferedFileLogger", (String)"Close File");
                            try {
                                this.oH.close();
                            }
                            catch (IOException iOException) {
                                iOException.printStackTrace();
                            }
                        }
                        android.util.Log.d((String)"BufferedFileLogger", (String)"Create File");
                        this.oI = string2;
                        File file = new File(this.oI);
                        if (!file.exists() && file.getParentFile() != null) {
                            file.getParentFile().mkdirs();
                        }
                        this.oH = new BufferedWriter((Writer)new FileWriter(this.oI, true));
                    }
                    try {
                        this.oH.write(stringBuilder.toString());
                        this.oH.newLine();
                        return;
                    }
                    catch (IOException iOException) {
                        iOException.printStackTrace();
                        return;
                    }
                    catch (IOException iOException) {
                        iOException.printStackTrace();
                        this.oI = null;
                        return;
                    }
                }
                case 2: 
            }
            android.util.Log.d((String)"BufferedFileLogger", (String)"Flush File");
            if (this.oH != null) {
                try {
                    this.oH.flush();
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }
            if (message.obj == null) return;
            if (!(message.obj instanceof CountDownLatch)) return;
            ((CountDownLatch)message.obj).countDown();
        }
    }

}

