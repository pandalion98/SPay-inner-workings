package com.sec.knox.container.util;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.LocalSocketAddress.Namespace;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.collect.Lists;
import com.sec.android.secvision.sef.SEF.Options;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class DaemonConnector implements Runnable, Callback {
    private static final int DEFAULT_TIMEOUT = 90000;
    private static final boolean LOGD = true;
    private static final long WARN_EXECUTE_DELAY_MS = 500;
    private final int BUFFER_SIZE = Options.OVERWRITE_IF_EXISTS_MP4;
    private final String TAG;
    private Handler mCallbackHandler;
    private IDaemonConnectorCallbacks mCallbacks;
    private final Object mDaemonLock = new Object();
    private OutputStream mOutputStream;
    private final ResponseQueue mResponseQueue;
    private AtomicInteger mSequenceNumber;
    private String mSocket;

    public static class Command {
        private ArrayList<Object> mArguments = Lists.newArrayList();
        private String mCmd;

        public Command(String cmd, Object... args) {
            this.mCmd = cmd;
            for (Object arg : args) {
                appendArg(arg);
            }
        }

        public Command appendArg(Object arg) {
            this.mArguments.add(arg);
            return this;
        }
    }

    private static class DaemonArgumentException extends DaemonConnectorException {
        public DaemonArgumentException(String command, DaemonEvent event) {
            super(command, event);
        }

        public IllegalArgumentException rethrowAsParcelableException() {
            throw new IllegalArgumentException(getMessage(), this);
        }
    }

    private static class DaemonFailureException extends DaemonConnectorException {
        public DaemonFailureException(String command, DaemonEvent event) {
            super(command, event);
        }
    }

    private static class ResponseQueue {
        private int mMaxCount;
        private final LinkedList<PendingCmd> mPendingCmds = new LinkedList();

        private static class PendingCmd {
            public int availableResponseCount;
            public int cmdNum;
            public String request;
            public BlockingQueue<DaemonEvent> responses = new ArrayBlockingQueue(10);

            public PendingCmd(int c, String r) {
                this.cmdNum = c;
                this.request = r;
            }
        }

        ResponseQueue(int maxCount) {
            this.mMaxCount = maxCount;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void add(int r9, com.sec.knox.container.util.DaemonEvent r10) {
            /*
            r8 = this;
            r0 = 0;
            r5 = r8.mPendingCmds;
            monitor-enter(r5);
            r4 = r8.mPendingCmds;	 Catch:{ all -> 0x00ae }
            r2 = r4.iterator();	 Catch:{ all -> 0x00ae }
        L_0x000a:
            r4 = r2.hasNext();	 Catch:{ all -> 0x00ae }
            if (r4 == 0) goto L_0x00b2;
        L_0x0010:
            r3 = r2.next();	 Catch:{ all -> 0x00ae }
            r3 = (com.sec.knox.container.util.DaemonConnector.ResponseQueue.PendingCmd) r3;	 Catch:{ all -> 0x00ae }
            r4 = r3.cmdNum;	 Catch:{ all -> 0x00ae }
            if (r4 != r9) goto L_0x000a;
        L_0x001a:
            r0 = r3;
            r1 = r0;
        L_0x001c:
            if (r1 != 0) goto L_0x00b0;
        L_0x001e:
            r4 = r8.mPendingCmds;	 Catch:{ all -> 0x0087 }
            r4 = r4.size();	 Catch:{ all -> 0x0087 }
            r6 = r8.mMaxCount;	 Catch:{ all -> 0x0087 }
            if (r4 < r6) goto L_0x008b;
        L_0x0028:
            r4 = "DaemonConnector.ResponseQueue";
            r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0087 }
            r6.<init>();	 Catch:{ all -> 0x0087 }
            r7 = "more buffered than allowed: ";
            r6 = r6.append(r7);	 Catch:{ all -> 0x0087 }
            r7 = r8.mPendingCmds;	 Catch:{ all -> 0x0087 }
            r7 = r7.size();	 Catch:{ all -> 0x0087 }
            r6 = r6.append(r7);	 Catch:{ all -> 0x0087 }
            r7 = " >= ";
            r6 = r6.append(r7);	 Catch:{ all -> 0x0087 }
            r7 = r8.mMaxCount;	 Catch:{ all -> 0x0087 }
            r6 = r6.append(r7);	 Catch:{ all -> 0x0087 }
            r6 = r6.toString();	 Catch:{ all -> 0x0087 }
            android.util.Log.e(r4, r6);	 Catch:{ all -> 0x0087 }
            r4 = r8.mPendingCmds;	 Catch:{ all -> 0x0087 }
            r3 = r4.remove();	 Catch:{ all -> 0x0087 }
            r3 = (com.sec.knox.container.util.DaemonConnector.ResponseQueue.PendingCmd) r3;	 Catch:{ all -> 0x0087 }
            r4 = "DaemonConnector.ResponseQueue";
            r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0087 }
            r6.<init>();	 Catch:{ all -> 0x0087 }
            r7 = "Removing request: ";
            r6 = r6.append(r7);	 Catch:{ all -> 0x0087 }
            r7 = r3.request;	 Catch:{ all -> 0x0087 }
            r6 = r6.append(r7);	 Catch:{ all -> 0x0087 }
            r7 = " (";
            r6 = r6.append(r7);	 Catch:{ all -> 0x0087 }
            r7 = r3.cmdNum;	 Catch:{ all -> 0x0087 }
            r6 = r6.append(r7);	 Catch:{ all -> 0x0087 }
            r7 = ")";
            r6 = r6.append(r7);	 Catch:{ all -> 0x0087 }
            r6 = r6.toString();	 Catch:{ all -> 0x0087 }
            android.util.Log.e(r4, r6);	 Catch:{ all -> 0x0087 }
            goto L_0x001e;
        L_0x0087:
            r4 = move-exception;
            r0 = r1;
        L_0x0089:
            monitor-exit(r5);	 Catch:{ all -> 0x00ae }
            throw r4;
        L_0x008b:
            r0 = new com.sec.knox.container.util.DaemonConnector$ResponseQueue$PendingCmd;	 Catch:{ all -> 0x0087 }
            r4 = 0;
            r0.<init>(r9, r4);	 Catch:{ all -> 0x0087 }
            r4 = r8.mPendingCmds;	 Catch:{ all -> 0x00ae }
            r4.add(r0);	 Catch:{ all -> 0x00ae }
        L_0x0096:
            r4 = r0.availableResponseCount;	 Catch:{ all -> 0x00ae }
            r4 = r4 + 1;
            r0.availableResponseCount = r4;	 Catch:{ all -> 0x00ae }
            r4 = r0.availableResponseCount;	 Catch:{ all -> 0x00ae }
            if (r4 != 0) goto L_0x00a5;
        L_0x00a0:
            r4 = r8.mPendingCmds;	 Catch:{ all -> 0x00ae }
            r4.remove(r0);	 Catch:{ all -> 0x00ae }
        L_0x00a5:
            monitor-exit(r5);	 Catch:{ all -> 0x00ae }
            r4 = r0.responses;	 Catch:{ InterruptedException -> 0x00ac }
            r4.put(r10);	 Catch:{ InterruptedException -> 0x00ac }
        L_0x00ab:
            return;
        L_0x00ac:
            r4 = move-exception;
            goto L_0x00ab;
        L_0x00ae:
            r4 = move-exception;
            goto L_0x0089;
        L_0x00b0:
            r0 = r1;
            goto L_0x0096;
        L_0x00b2:
            r1 = r0;
            goto L_0x001c;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sec.knox.container.util.DaemonConnector.ResponseQueue.add(int, com.sec.knox.container.util.DaemonEvent):void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.sec.knox.container.util.DaemonEvent remove(int r12, int r13, java.lang.String r14) {
            /*
            r11 = this;
            r2 = 0;
            r8 = r11.mPendingCmds;
            monitor-enter(r8);
            r7 = r11.mPendingCmds;	 Catch:{ all -> 0x0050 }
            r4 = r7.iterator();	 Catch:{ all -> 0x0050 }
        L_0x000a:
            r7 = r4.hasNext();	 Catch:{ all -> 0x0050 }
            if (r7 == 0) goto L_0x005a;
        L_0x0010:
            r5 = r4.next();	 Catch:{ all -> 0x0050 }
            r5 = (com.sec.knox.container.util.DaemonConnector.ResponseQueue.PendingCmd) r5;	 Catch:{ all -> 0x0050 }
            r7 = r5.cmdNum;	 Catch:{ all -> 0x0050 }
            if (r7 != r12) goto L_0x000a;
        L_0x001a:
            r2 = r5;
            r3 = r2;
        L_0x001c:
            if (r3 != 0) goto L_0x0058;
        L_0x001e:
            r2 = new com.sec.knox.container.util.DaemonConnector$ResponseQueue$PendingCmd;	 Catch:{ all -> 0x0055 }
            r2.<init>(r12, r14);	 Catch:{ all -> 0x0055 }
            r7 = r11.mPendingCmds;	 Catch:{ all -> 0x0050 }
            r7.add(r2);	 Catch:{ all -> 0x0050 }
        L_0x0028:
            r7 = r2.availableResponseCount;	 Catch:{ all -> 0x0050 }
            r7 = r7 + -1;
            r2.availableResponseCount = r7;	 Catch:{ all -> 0x0050 }
            r7 = r2.availableResponseCount;	 Catch:{ all -> 0x0050 }
            if (r7 != 0) goto L_0x0037;
        L_0x0032:
            r7 = r11.mPendingCmds;	 Catch:{ all -> 0x0050 }
            r7.remove(r2);	 Catch:{ all -> 0x0050 }
        L_0x0037:
            monitor-exit(r8);	 Catch:{ all -> 0x0050 }
            r6 = 0;
            r7 = r2.responses;	 Catch:{ InterruptedException -> 0x0053 }
            r8 = (long) r13;	 Catch:{ InterruptedException -> 0x0053 }
            r10 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ InterruptedException -> 0x0053 }
            r7 = r7.poll(r8, r10);	 Catch:{ InterruptedException -> 0x0053 }
            r0 = r7;
            r0 = (com.sec.knox.container.util.DaemonEvent) r0;	 Catch:{ InterruptedException -> 0x0053 }
            r6 = r0;
        L_0x0046:
            if (r6 != 0) goto L_0x004f;
        L_0x0048:
            r7 = "DaemonConnector.ResponseQueue";
            r8 = "Timeout waiting for response";
            android.util.Log.e(r7, r8);
        L_0x004f:
            return r6;
        L_0x0050:
            r7 = move-exception;
        L_0x0051:
            monitor-exit(r8);	 Catch:{ all -> 0x0050 }
            throw r7;
        L_0x0053:
            r7 = move-exception;
            goto L_0x0046;
        L_0x0055:
            r7 = move-exception;
            r2 = r3;
            goto L_0x0051;
        L_0x0058:
            r2 = r3;
            goto L_0x0028;
        L_0x005a:
            r3 = r2;
            goto L_0x001c;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sec.knox.container.util.DaemonConnector.ResponseQueue.remove(int, int, java.lang.String):com.sec.knox.container.util.DaemonEvent");
        }

        public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
            pw.println("Pending requests:");
            synchronized (this.mPendingCmds) {
                Iterator i$ = this.mPendingCmds.iterator();
                while (i$.hasNext()) {
                    PendingCmd pendingCmd = (PendingCmd) i$.next();
                    pw.println("  Cmd " + pendingCmd.cmdNum + " - " + pendingCmd.request);
                }
            }
        }
    }

    public DaemonConnector(IDaemonConnectorCallbacks callbacks, String socket, int responseQueueSize, String logTag, int maxLogSize) {
        this.mCallbacks = callbacks;
        this.mSocket = socket;
        this.mResponseQueue = new ResponseQueue(responseQueueSize);
        this.mSequenceNumber = new AtomicInteger(0);
        if (logTag == null) {
            logTag = "ECS_DaemonConnector";
        }
        this.TAG = logTag;
    }

    public void run() {
        HandlerThread thread = new HandlerThread(this.TAG + ".CallbackHandler");
        thread.start();
        this.mCallbackHandler = new Handler(thread.getLooper(), this);
        while (true) {
            try {
                listenToSocket();
            } catch (Exception e) {
                loge("Error in DaemonConnector: " + e);
                SystemClock.sleep(5000);
            }
        }
    }

    public boolean handleMessage(Message msg) {
        String event = msg.obj;
        try {
            if (!this.mCallbacks.onEvent(msg.what, event, DaemonEvent.unescapeArgs(event))) {
                log(String.format("Unhandled event '%s'", new Object[]{event}));
            }
        } catch (Exception e) {
            loge("Error handling '" + event + "': " + e);
        }
        return true;
    }

    private void listenToSocket() throws IOException {
        IOException ex;
        Throwable th;
        LocalSocket localSocket = null;
        byte[] buffer = new byte[Options.OVERWRITE_IF_EXISTS_MP4];
        int start = 0;
        try {
            LocalSocket socket = new LocalSocket();
            try {
                int count;
                socket.connect(new LocalSocketAddress(this.mSocket, Namespace.RESERVED));
                InputStream inputStream = socket.getInputStream();
                synchronized (this.mDaemonLock) {
                    this.mOutputStream = socket.getOutputStream();
                }
                this.mCallbacks.onDaemonConnected();
                while (true) {
                    count = inputStream.read(buffer, start, 4096 - start);
                    if (count < 0) {
                        break;
                    }
                    String rawEvent;
                    count += start;
                    start = 0;
                    for (int i = 0; i < count; i++) {
                        if (buffer[i] == (byte) 0) {
                            rawEvent = new String(buffer, start, i - start, Charset.forName("UTF-8"));
                            log("RCV <-");
                            try {
                                DaemonEvent event = DaemonEvent.parseRawEvent(rawEvent);
                                if (event.isClassUnsolicited()) {
                                    this.mCallbackHandler.sendMessage(this.mCallbackHandler.obtainMessage(event.getCode(), event.getRawEvent()));
                                } else {
                                    this.mResponseQueue.add(event.getCmdNumber(), event);
                                }
                            } catch (IllegalArgumentException e) {
                                log("Problem parsing message: " + rawEvent + " - " + e);
                            }
                            start = i + 1;
                        }
                    }
                    if (start == 0) {
                        rawEvent = new String(buffer, start, count, Charset.forName("UTF-8"));
                        log("RCV incomplete <-");
                    }
                    if (start != count) {
                        int remaining = 4096 - start;
                        System.arraycopy(buffer, start, buffer, 0, remaining);
                        start = remaining;
                    } else {
                        start = 0;
                        if (buffer != null) {
                            Arrays.fill(buffer, 0, buffer.length, (byte) 0);
                        }
                    }
                }
                loge("got " + count + " reading with start = " + start);
                synchronized (this.mDaemonLock) {
                    if (this.mOutputStream != null) {
                        try {
                            loge("closing stream for " + this.mSocket);
                            this.mOutputStream.close();
                        } catch (IOException e2) {
                            loge("Failed closing output stream: " + e2);
                        }
                        this.mOutputStream = null;
                    }
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ex2) {
                        loge("Failed closing socket: " + ex2);
                    }
                }
                if (buffer != null) {
                    Arrays.fill(buffer, 0, buffer.length, (byte) 0);
                }
            } catch (IOException e3) {
                ex2 = e3;
                localSocket = socket;
            } catch (Throwable th2) {
                th = th2;
                localSocket = socket;
            }
        } catch (IOException e4) {
            ex2 = e4;
            try {
                loge("Communications error: " + ex2);
                ex2.printStackTrace();
                throw ex2;
            } catch (Throwable th3) {
                th = th3;
                synchronized (this.mDaemonLock) {
                    if (this.mOutputStream != null) {
                        try {
                            loge("closing stream for " + this.mSocket);
                            this.mOutputStream.close();
                        } catch (IOException e22) {
                            loge("Failed closing output stream: " + e22);
                        }
                        this.mOutputStream = null;
                    }
                }
                if (localSocket != null) {
                    try {
                        localSocket.close();
                    } catch (IOException ex22) {
                        loge("Failed closing socket: " + ex22);
                    }
                }
                throw th;
            }
        }
    }

    private void makeCommand(StringBuilder builder, String cmd, Object... args) throws DaemonConnectorException {
        if (cmd.indexOf(0) >= 0) {
            throw new IllegalArgumentException("unexpected command: " + cmd);
        }
        builder.append(cmd);
        for (Object arg : args) {
            String argString = String.valueOf(arg);
            if (argString.indexOf(0) >= 0) {
                throw new IllegalArgumentException("unexpected argument: " + arg);
            }
            builder.append(' ');
            appendEscaped(builder, argString);
        }
    }

    public DaemonEvent execute(Command cmd) throws DaemonConnectorException {
        return execute(cmd.mCmd, cmd.mArguments.toArray());
    }

    public DaemonEvent execute(String cmd, Object... args) throws DaemonConnectorException {
        DaemonEvent[] events = executeForList(cmd, args);
        if (events.length == 1) {
            return events[0];
        }
        throw new DaemonConnectorException("Expected exactly one response, but received " + events.length);
    }

    public DaemonEvent[] executeForList(Command cmd) throws DaemonConnectorException {
        return executeForList(cmd.mCmd, cmd.mArguments.toArray());
    }

    public DaemonEvent[] executeForList(String cmd, Object... args) throws DaemonConnectorException {
        return execute(90000, cmd, args);
    }

    public DaemonEvent[] execute(int timeout, String cmd, Object... args) throws DaemonConnectorException {
        ArrayList<DaemonEvent> events = Lists.newArrayList();
        int sequenceNumber = this.mSequenceNumber.incrementAndGet();
        StringBuilder cmdBuilder = new StringBuilder(Integer.toString(sequenceNumber)).append(' ');
        long startTime = SystemClock.elapsedRealtime();
        makeCommand(cmdBuilder, cmd, args);
        String logCmd = cmdBuilder.toString();
        if (args.length > 0) {
            log("SND -> {" + cmd + " " + args[0].toString() + "}");
        }
        cmdBuilder.append('\u0000');
        String sentCmd = cmdBuilder.toString();
        synchronized (this.mDaemonLock) {
            if (this.mOutputStream == null) {
                throw new DaemonConnectorException("missing output stream");
            }
            try {
                this.mOutputStream.write(sentCmd.getBytes(Charset.forName("UTF-8")));
            } catch (Throwable e) {
                throw new DaemonConnectorException("problem sending command", e);
            }
        }
        DaemonEvent event;
        do {
            event = this.mResponseQueue.remove(sequenceNumber, timeout, sentCmd);
            if (event == null) {
                loge("timed-out waiting for response");
                throw new DaemonFailureException(cmd, event);
            }
            log("RMV <-");
            events.add(event);
        } while (event.isClassContinue());
        if (SystemClock.elapsedRealtime() - startTime > WARN_EXECUTE_DELAY_MS) {
        }
        if (event.isClassClientError()) {
            throw new DaemonArgumentException(cmd, event);
        } else if (!event.isClassServerError()) {
            return (DaemonEvent[]) events.toArray(new DaemonEvent[events.size()]);
        } else {
            throw new DaemonFailureException(cmd, event);
        }
    }

    @Deprecated
    public ArrayList<String> doCommand(String cmd) throws DaemonConnectorException {
        ArrayList<String> rawEvents = Lists.newArrayList();
        for (DaemonEvent event : executeForList(cmd, new Object[0])) {
            rawEvents.add(event.getRawEvent());
        }
        return rawEvents;
    }

    @Deprecated
    public String[] doListCommand(String cmd, int expectedCode) throws DaemonConnectorException {
        ArrayList<String> list = Lists.newArrayList();
        DaemonEvent[] events = executeForList(cmd, new Object[0]);
        int i = 0;
        while (i < events.length - 1) {
            DaemonEvent event = events[i];
            int code = event.getCode();
            if (code == expectedCode) {
                list.add(event.getMessage());
                i++;
            } else {
                throw new DaemonConnectorException("unexpected list response " + code + " instead of " + expectedCode);
            }
        }
        DaemonEvent finalEvent = events[events.length - 1];
        if (finalEvent.isClassOk()) {
            return (String[]) list.toArray(new String[list.size()]);
        }
        throw new DaemonConnectorException("unexpected final event: " + finalEvent);
    }

    static void appendEscaped(StringBuilder builder, String arg) {
        boolean hasSpaces = arg.indexOf(32) >= 0;
        if (hasSpaces) {
            builder.append('\"');
        }
        int length = arg.length();
        for (int i = 0; i < length; i++) {
            char c = arg.charAt(i);
            if (c == '\"') {
                builder.append("\\\"");
            } else if (c == '\\') {
                builder.append("\\\\");
            } else {
                builder.append(c);
            }
        }
        if (hasSpaces) {
            builder.append('\"');
        }
    }

    public void monitor() {
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println();
        this.mResponseQueue.dump(fd, pw, args);
    }

    private void log(String logstring) {
        Log.d(this.TAG, logstring);
    }

    private void loge(String logstring) {
        Log.e(this.TAG, logstring);
    }
}
