package com.squareup.okhttp.internal;

import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.squareup.okhttp.internal.io.FileSystem;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class DiskLruCache implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled;
    static final long ANY_SEQUENCE_NUMBER = -1;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final Pattern LEGAL_KEY_PATTERN;
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final Sink NULL_SINK;
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    private final Runnable cleanupRunnable;
    private boolean closed;
    private final File directory;
    private final Executor executor;
    private final FileSystem fileSystem;
    private boolean hasJournalErrors;
    private boolean initialized;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    private BufferedSink journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries;
    private long maxSize;
    private long nextSequenceNumber;
    private int redundantOpCount;
    private long size;
    private final int valueCount;

    /* renamed from: com.squareup.okhttp.internal.DiskLruCache.1 */
    class C06391 implements Runnable {
        C06391() {
        }

        public void run() {
            int i = 0;
            synchronized (DiskLruCache.this) {
                if (!DiskLruCache.this.initialized) {
                    i = 1;
                }
                if ((i | DiskLruCache.this.closed) != 0) {
                    return;
                }
                try {
                    DiskLruCache.this.trimToSize();
                    if (DiskLruCache.this.journalRebuildRequired()) {
                        DiskLruCache.this.rebuildJournal();
                        DiskLruCache.this.redundantOpCount = 0;
                    }
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /* renamed from: com.squareup.okhttp.internal.DiskLruCache.2 */
    class C06402 extends FaultHidingSink {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DiskLruCache.class.desiredAssertionStatus() ? true : DiskLruCache.$assertionsDisabled;
        }

        C06402(Sink sink) {
            super(sink);
        }

        protected void onException(IOException iOException) {
            if ($assertionsDisabled || Thread.holdsLock(DiskLruCache.this)) {
                DiskLruCache.this.hasJournalErrors = true;
                return;
            }
            throw new AssertionError();
        }
    }

    /* renamed from: com.squareup.okhttp.internal.DiskLruCache.3 */
    class C06413 implements Iterator<Snapshot> {
        final Iterator<Entry> delegate;
        Snapshot nextSnapshot;
        Snapshot removeSnapshot;

        C06413() {
            this.delegate = new ArrayList(DiskLruCache.this.lruEntries.values()).iterator();
        }

        public boolean hasNext() {
            if (this.nextSnapshot != null) {
                return true;
            }
            synchronized (DiskLruCache.this) {
                if (DiskLruCache.this.closed) {
                    return DiskLruCache.$assertionsDisabled;
                }
                while (this.delegate.hasNext()) {
                    Snapshot snapshot = ((Entry) this.delegate.next()).snapshot();
                    if (snapshot != null) {
                        this.nextSnapshot = snapshot;
                        return true;
                    }
                }
                return DiskLruCache.$assertionsDisabled;
            }
        }

        public Snapshot next() {
            if (hasNext()) {
                this.removeSnapshot = this.nextSnapshot;
                this.nextSnapshot = null;
                return this.removeSnapshot;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            if (this.removeSnapshot == null) {
                throw new IllegalStateException("remove() before next()");
            }
            try {
                DiskLruCache.this.remove(this.removeSnapshot.key);
            } catch (IOException e) {
            } finally {
                this.removeSnapshot = null;
            }
        }
    }

    /* renamed from: com.squareup.okhttp.internal.DiskLruCache.4 */
    static class C06424 implements Sink {
        C06424() {
        }

        public void write(Buffer buffer, long j) {
            buffer.skip(j);
        }

        public void flush() {
        }

        public Timeout timeout() {
            return Timeout.NONE;
        }

        public void close() {
        }
    }

    public final class Editor {
        private boolean committed;
        private final Entry entry;
        private boolean hasErrors;
        private final boolean[] written;

        /* renamed from: com.squareup.okhttp.internal.DiskLruCache.Editor.1 */
        class C06431 extends FaultHidingSink {
            C06431(Sink sink) {
                super(sink);
            }

            protected void onException(IOException iOException) {
                synchronized (DiskLruCache.this) {
                    Editor.this.hasErrors = true;
                }
            }
        }

        private Editor(Entry entry) {
            this.entry = entry;
            this.written = entry.readable ? null : new boolean[DiskLruCache.this.valueCount];
        }

        public Source newSource(int i) {
            Source source = null;
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                } else if (this.entry.readable) {
                    try {
                        source = DiskLruCache.this.fileSystem.source(this.entry.cleanFiles[i]);
                    } catch (FileNotFoundException e) {
                    }
                }
            }
            return source;
        }

        public Sink newSink(int i) {
            Sink c06431;
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!this.entry.readable) {
                    this.written[i] = true;
                }
                try {
                    c06431 = new C06431(DiskLruCache.this.fileSystem.sink(this.entry.dirtyFiles[i]));
                } catch (FileNotFoundException e) {
                    c06431 = DiskLruCache.NULL_SINK;
                }
            }
            return c06431;
        }

        public void commit() {
            synchronized (DiskLruCache.this) {
                if (this.hasErrors) {
                    DiskLruCache.this.completeEdit(this, DiskLruCache.$assertionsDisabled);
                    DiskLruCache.this.removeEntry(this.entry);
                } else {
                    DiskLruCache.this.completeEdit(this, true);
                }
                this.committed = true;
            }
        }

        public void abort() {
            synchronized (DiskLruCache.this) {
                DiskLruCache.this.completeEdit(this, DiskLruCache.$assertionsDisabled);
            }
        }

        public void abortUnlessCommitted() {
            synchronized (DiskLruCache.this) {
                if (!this.committed) {
                    try {
                        DiskLruCache.this.completeEdit(this, DiskLruCache.$assertionsDisabled);
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    private final class Entry {
        private final File[] cleanFiles;
        private Editor currentEditor;
        private final File[] dirtyFiles;
        private final String key;
        private final long[] lengths;
        private boolean readable;
        private long sequenceNumber;

        private Entry(String str) {
            this.key = str;
            this.lengths = new long[DiskLruCache.this.valueCount];
            this.cleanFiles = new File[DiskLruCache.this.valueCount];
            this.dirtyFiles = new File[DiskLruCache.this.valueCount];
            StringBuilder append = new StringBuilder(str).append('.');
            int length = append.length();
            for (int i = 0; i < DiskLruCache.this.valueCount; i++) {
                append.append(i);
                this.cleanFiles[i] = new File(DiskLruCache.this.directory, append.toString());
                append.append(".tmp");
                this.dirtyFiles[i] = new File(DiskLruCache.this.directory, append.toString());
                append.setLength(length);
            }
        }

        private void setLengths(String[] strArr) {
            if (strArr.length != DiskLruCache.this.valueCount) {
                throw invalidLengths(strArr);
            }
            int i = 0;
            while (i < strArr.length) {
                try {
                    this.lengths[i] = Long.parseLong(strArr[i]);
                    i++;
                } catch (NumberFormatException e) {
                    throw invalidLengths(strArr);
                }
            }
        }

        void writeLengths(BufferedSink bufferedSink) {
            for (long writeDecimalLong : this.lengths) {
                bufferedSink.writeByte(32).writeDecimalLong(writeDecimalLong);
            }
        }

        private IOException invalidLengths(String[] strArr) {
            throw new IOException("unexpected journal line: " + Arrays.toString(strArr));
        }

        Snapshot snapshot() {
            int i = 0;
            if (Thread.holdsLock(DiskLruCache.this)) {
                Source[] sourceArr = new Source[DiskLruCache.this.valueCount];
                long[] jArr = (long[]) this.lengths.clone();
                int i2 = 0;
                while (i2 < DiskLruCache.this.valueCount) {
                    try {
                        sourceArr[i2] = DiskLruCache.this.fileSystem.source(this.cleanFiles[i2]);
                        i2++;
                    } catch (FileNotFoundException e) {
                        while (i < DiskLruCache.this.valueCount && sourceArr[i] != null) {
                            Util.closeQuietly(sourceArr[i]);
                            i++;
                        }
                        return null;
                    }
                }
                return new Snapshot(this.key, this.sequenceNumber, sourceArr, jArr, null);
            }
            throw new AssertionError();
        }
    }

    public final class Snapshot implements Closeable {
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        private final Source[] sources;

        private Snapshot(String str, long j, Source[] sourceArr, long[] jArr) {
            this.key = str;
            this.sequenceNumber = j;
            this.sources = sourceArr;
            this.lengths = jArr;
        }

        public String key() {
            return this.key;
        }

        public Editor edit() {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        public Source getSource(int i) {
            return this.sources[i];
        }

        public long getLength(int i) {
            return this.lengths[i];
        }

        public void close() {
            for (Closeable closeQuietly : this.sources) {
                Util.closeQuietly(closeQuietly);
            }
        }
    }

    static {
        $assertionsDisabled = !DiskLruCache.class.desiredAssertionStatus() ? true : $assertionsDisabled;
        LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,120}");
        NULL_SINK = new C06424();
    }

    DiskLruCache(FileSystem fileSystem, File file, int i, int i2, long j, Executor executor) {
        this.size = 0;
        this.lruEntries = new LinkedHashMap(0, 0.75f, true);
        this.nextSequenceNumber = 0;
        this.cleanupRunnable = new C06391();
        this.fileSystem = fileSystem;
        this.directory = file;
        this.appVersion = i;
        this.journalFile = new File(file, JOURNAL_FILE);
        this.journalFileTmp = new File(file, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(file, JOURNAL_FILE_BACKUP);
        this.valueCount = i2;
        this.maxSize = j;
        this.executor = executor;
    }

    void initialize() {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        } else if (!this.initialized) {
            if (this.fileSystem.exists(this.journalFileBackup)) {
                if (this.fileSystem.exists(this.journalFile)) {
                    this.fileSystem.delete(this.journalFileBackup);
                } else {
                    this.fileSystem.rename(this.journalFileBackup, this.journalFile);
                }
            }
            if (this.fileSystem.exists(this.journalFile)) {
                try {
                    readJournal();
                    processJournal();
                    this.initialized = true;
                    return;
                } catch (IOException e) {
                    Platform.get().logW("DiskLruCache " + this.directory + " is corrupt: " + e.getMessage() + ", removing");
                    delete();
                    this.closed = $assertionsDisabled;
                }
            }
            rebuildJournal();
            this.initialized = true;
        }
    }

    public static DiskLruCache create(FileSystem fileSystem, File file, int i, int i2, long j) {
        if (j <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        } else if (i2 <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        } else {
            return new DiskLruCache(fileSystem, file, i, i2, j, new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory("OkHttp DiskLruCache", true)));
        }
    }

    private void readJournal() {
        int i;
        Closeable buffer = Okio.buffer(this.fileSystem.source(this.journalFile));
        try {
            String readUtf8LineStrict = buffer.readUtf8LineStrict();
            String readUtf8LineStrict2 = buffer.readUtf8LineStrict();
            String readUtf8LineStrict3 = buffer.readUtf8LineStrict();
            String readUtf8LineStrict4 = buffer.readUtf8LineStrict();
            String readUtf8LineStrict5 = buffer.readUtf8LineStrict();
            if (MAGIC.equals(readUtf8LineStrict) && VERSION_1.equals(readUtf8LineStrict2) && Integer.toString(this.appVersion).equals(readUtf8LineStrict3) && Integer.toString(this.valueCount).equals(readUtf8LineStrict4) && BuildConfig.FLAVOR.equals(readUtf8LineStrict5)) {
                i = 0;
                while (true) {
                    readJournalLine(buffer.readUtf8LineStrict());
                    i++;
                }
            } else {
                throw new IOException("unexpected journal header: [" + readUtf8LineStrict + ", " + readUtf8LineStrict2 + ", " + readUtf8LineStrict4 + ", " + readUtf8LineStrict5 + "]");
            }
        } catch (EOFException e) {
            this.redundantOpCount = i - this.lruEntries.size();
            if (buffer.exhausted()) {
                this.journalWriter = newJournalWriter();
            } else {
                rebuildJournal();
            }
            Util.closeQuietly(buffer);
        } catch (Throwable th) {
            Util.closeQuietly(buffer);
        }
    }

    private BufferedSink newJournalWriter() {
        return Okio.buffer(new C06402(this.fileSystem.appendingSink(this.journalFile)));
    }

    private void readJournalLine(String str) {
        int indexOf = str.indexOf(32);
        if (indexOf == -1) {
            throw new IOException("unexpected journal line: " + str);
        }
        String str2;
        int i = indexOf + 1;
        int indexOf2 = str.indexOf(32, i);
        if (indexOf2 == -1) {
            String substring = str.substring(i);
            if (indexOf == REMOVE.length() && str.startsWith(REMOVE)) {
                this.lruEntries.remove(substring);
                return;
            }
            str2 = substring;
        } else {
            str2 = str.substring(i, indexOf2);
        }
        Entry entry = (Entry) this.lruEntries.get(str2);
        if (entry == null) {
            entry = new Entry(str2, null);
            this.lruEntries.put(str2, entry);
        }
        if (indexOf2 != -1 && indexOf == CLEAN.length() && str.startsWith(CLEAN)) {
            String[] split = str.substring(indexOf2 + 1).split(" ");
            entry.readable = true;
            entry.currentEditor = null;
            entry.setLengths(split);
        } else if (indexOf2 == -1 && indexOf == DIRTY.length() && str.startsWith(DIRTY)) {
            entry.currentEditor = new Editor(entry, null);
        } else if (indexOf2 != -1 || indexOf != READ.length() || !str.startsWith(READ)) {
            throw new IOException("unexpected journal line: " + str);
        }
    }

    private void processJournal() {
        this.fileSystem.delete(this.journalFileTmp);
        Iterator it = this.lruEntries.values().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            int i;
            if (entry.currentEditor == null) {
                for (i = 0; i < this.valueCount; i++) {
                    this.size += entry.lengths[i];
                }
            } else {
                entry.currentEditor = null;
                for (i = 0; i < this.valueCount; i++) {
                    this.fileSystem.delete(entry.cleanFiles[i]);
                    this.fileSystem.delete(entry.dirtyFiles[i]);
                }
                it.remove();
            }
        }
    }

    private synchronized void rebuildJournal() {
        if (this.journalWriter != null) {
            this.journalWriter.close();
        }
        BufferedSink buffer = Okio.buffer(this.fileSystem.sink(this.journalFileTmp));
        try {
            buffer.writeUtf8(MAGIC).writeByte(10);
            buffer.writeUtf8(VERSION_1).writeByte(10);
            buffer.writeDecimalLong((long) this.appVersion).writeByte(10);
            buffer.writeDecimalLong((long) this.valueCount).writeByte(10);
            buffer.writeByte(10);
            for (Entry entry : this.lruEntries.values()) {
                if (entry.currentEditor != null) {
                    buffer.writeUtf8(DIRTY).writeByte(32);
                    buffer.writeUtf8(entry.key);
                    buffer.writeByte(10);
                } else {
                    buffer.writeUtf8(CLEAN).writeByte(32);
                    buffer.writeUtf8(entry.key);
                    entry.writeLengths(buffer);
                    buffer.writeByte(10);
                }
            }
            buffer.close();
            if (this.fileSystem.exists(this.journalFile)) {
                this.fileSystem.rename(this.journalFile, this.journalFileBackup);
            }
            this.fileSystem.rename(this.journalFileTmp, this.journalFile);
            this.fileSystem.delete(this.journalFileBackup);
            this.journalWriter = newJournalWriter();
            this.hasJournalErrors = $assertionsDisabled;
        } catch (Throwable th) {
            buffer.close();
        }
    }

    public synchronized Snapshot get(String str) {
        Snapshot snapshot;
        initialize();
        checkNotClosed();
        validateKey(str);
        Entry entry = (Entry) this.lruEntries.get(str);
        if (entry == null || !entry.readable) {
            snapshot = null;
        } else {
            snapshot = entry.snapshot();
            if (snapshot == null) {
                snapshot = null;
            } else {
                this.redundantOpCount++;
                this.journalWriter.writeUtf8(READ).writeByte(32).writeUtf8(str).writeByte(10);
                if (journalRebuildRequired()) {
                    this.executor.execute(this.cleanupRunnable);
                }
            }
        }
        return snapshot;
    }

    public Editor edit(String str) {
        return edit(str, ANY_SEQUENCE_NUMBER);
    }

    private synchronized Editor edit(String str, long j) {
        Editor editor;
        initialize();
        checkNotClosed();
        validateKey(str);
        Entry entry = (Entry) this.lruEntries.get(str);
        if (j == ANY_SEQUENCE_NUMBER || (entry != null && entry.sequenceNumber == j)) {
            if (entry != null) {
                if (entry.currentEditor != null) {
                    editor = null;
                }
            }
            this.journalWriter.writeUtf8(DIRTY).writeByte(32).writeUtf8(str).writeByte(10);
            this.journalWriter.flush();
            if (this.hasJournalErrors) {
                editor = null;
            } else {
                Entry entry2;
                if (entry == null) {
                    entry = new Entry(str, null);
                    this.lruEntries.put(str, entry);
                    entry2 = entry;
                } else {
                    entry2 = entry;
                }
                editor = new Editor(entry2, null);
                entry2.currentEditor = editor;
            }
        } else {
            editor = null;
        }
        return editor;
    }

    public File getDirectory() {
        return this.directory;
    }

    public synchronized long getMaxSize() {
        return this.maxSize;
    }

    public synchronized void setMaxSize(long j) {
        this.maxSize = j;
        if (this.initialized) {
            this.executor.execute(this.cleanupRunnable);
        }
    }

    public synchronized long size() {
        initialize();
        return this.size;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void completeEdit(com.squareup.okhttp.internal.DiskLruCache.Editor r11, boolean r12) {
        /*
        r10 = this;
        r0 = 0;
        monitor-enter(r10);
        r2 = r11.entry;	 Catch:{ all -> 0x0012 }
        r1 = r2.currentEditor;	 Catch:{ all -> 0x0012 }
        if (r1 == r11) goto L_0x0015;
    L_0x000c:
        r0 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0012 }
        r0.<init>();	 Catch:{ all -> 0x0012 }
        throw r0;	 Catch:{ all -> 0x0012 }
    L_0x0012:
        r0 = move-exception;
        monitor-exit(r10);
        throw r0;
    L_0x0015:
        if (r12 == 0) goto L_0x005c;
    L_0x0017:
        r1 = r2.readable;	 Catch:{ all -> 0x0012 }
        if (r1 != 0) goto L_0x005c;
    L_0x001d:
        r1 = r0;
    L_0x001e:
        r3 = r10.valueCount;	 Catch:{ all -> 0x0012 }
        if (r1 >= r3) goto L_0x005c;
    L_0x0022:
        r3 = r11.written;	 Catch:{ all -> 0x0012 }
        r3 = r3[r1];	 Catch:{ all -> 0x0012 }
        if (r3 != 0) goto L_0x0046;
    L_0x002a:
        r11.abort();	 Catch:{ all -> 0x0012 }
        r0 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0012 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0012 }
        r2.<init>();	 Catch:{ all -> 0x0012 }
        r3 = "Newly created entry didn't create value for index ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0012 }
        r1 = r2.append(r1);	 Catch:{ all -> 0x0012 }
        r1 = r1.toString();	 Catch:{ all -> 0x0012 }
        r0.<init>(r1);	 Catch:{ all -> 0x0012 }
        throw r0;	 Catch:{ all -> 0x0012 }
    L_0x0046:
        r3 = r10.fileSystem;	 Catch:{ all -> 0x0012 }
        r4 = r2.dirtyFiles;	 Catch:{ all -> 0x0012 }
        r4 = r4[r1];	 Catch:{ all -> 0x0012 }
        r3 = r3.exists(r4);	 Catch:{ all -> 0x0012 }
        if (r3 != 0) goto L_0x0059;
    L_0x0054:
        r11.abort();	 Catch:{ all -> 0x0012 }
    L_0x0057:
        monitor-exit(r10);
        return;
    L_0x0059:
        r1 = r1 + 1;
        goto L_0x001e;
    L_0x005c:
        r1 = r10.valueCount;	 Catch:{ all -> 0x0012 }
        if (r0 >= r1) goto L_0x009d;
    L_0x0060:
        r1 = r2.dirtyFiles;	 Catch:{ all -> 0x0012 }
        r1 = r1[r0];	 Catch:{ all -> 0x0012 }
        if (r12 == 0) goto L_0x0097;
    L_0x0068:
        r3 = r10.fileSystem;	 Catch:{ all -> 0x0012 }
        r3 = r3.exists(r1);	 Catch:{ all -> 0x0012 }
        if (r3 == 0) goto L_0x0094;
    L_0x0070:
        r3 = r2.cleanFiles;	 Catch:{ all -> 0x0012 }
        r3 = r3[r0];	 Catch:{ all -> 0x0012 }
        r4 = r10.fileSystem;	 Catch:{ all -> 0x0012 }
        r4.rename(r1, r3);	 Catch:{ all -> 0x0012 }
        r1 = r2.lengths;	 Catch:{ all -> 0x0012 }
        r4 = r1[r0];	 Catch:{ all -> 0x0012 }
        r1 = r10.fileSystem;	 Catch:{ all -> 0x0012 }
        r6 = r1.size(r3);	 Catch:{ all -> 0x0012 }
        r1 = r2.lengths;	 Catch:{ all -> 0x0012 }
        r1[r0] = r6;	 Catch:{ all -> 0x0012 }
        r8 = r10.size;	 Catch:{ all -> 0x0012 }
        r4 = r8 - r4;
        r4 = r4 + r6;
        r10.size = r4;	 Catch:{ all -> 0x0012 }
    L_0x0094:
        r0 = r0 + 1;
        goto L_0x005c;
    L_0x0097:
        r3 = r10.fileSystem;	 Catch:{ all -> 0x0012 }
        r3.delete(r1);	 Catch:{ all -> 0x0012 }
        goto L_0x0094;
    L_0x009d:
        r0 = r10.redundantOpCount;	 Catch:{ all -> 0x0012 }
        r0 = r0 + 1;
        r10.redundantOpCount = r0;	 Catch:{ all -> 0x0012 }
        r0 = 0;
        r2.currentEditor = r0;	 Catch:{ all -> 0x0012 }
        r0 = r2.readable;	 Catch:{ all -> 0x0012 }
        r0 = r0 | r12;
        if (r0 == 0) goto L_0x00fc;
    L_0x00ae:
        r0 = 1;
        r2.readable = r0;	 Catch:{ all -> 0x0012 }
        r0 = r10.journalWriter;	 Catch:{ all -> 0x0012 }
        r1 = "CLEAN";
        r0 = r0.writeUtf8(r1);	 Catch:{ all -> 0x0012 }
        r1 = 32;
        r0.writeByte(r1);	 Catch:{ all -> 0x0012 }
        r0 = r10.journalWriter;	 Catch:{ all -> 0x0012 }
        r1 = r2.key;	 Catch:{ all -> 0x0012 }
        r0.writeUtf8(r1);	 Catch:{ all -> 0x0012 }
        r0 = r10.journalWriter;	 Catch:{ all -> 0x0012 }
        r2.writeLengths(r0);	 Catch:{ all -> 0x0012 }
        r0 = r10.journalWriter;	 Catch:{ all -> 0x0012 }
        r1 = 10;
        r0.writeByte(r1);	 Catch:{ all -> 0x0012 }
        if (r12 == 0) goto L_0x00e0;
    L_0x00d6:
        r0 = r10.nextSequenceNumber;	 Catch:{ all -> 0x0012 }
        r4 = 1;
        r4 = r4 + r0;
        r10.nextSequenceNumber = r4;	 Catch:{ all -> 0x0012 }
        r2.sequenceNumber = r0;	 Catch:{ all -> 0x0012 }
    L_0x00e0:
        r0 = r10.journalWriter;	 Catch:{ all -> 0x0012 }
        r0.flush();	 Catch:{ all -> 0x0012 }
        r0 = r10.size;	 Catch:{ all -> 0x0012 }
        r2 = r10.maxSize;	 Catch:{ all -> 0x0012 }
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 > 0) goto L_0x00f3;
    L_0x00ed:
        r0 = r10.journalRebuildRequired();	 Catch:{ all -> 0x0012 }
        if (r0 == 0) goto L_0x0057;
    L_0x00f3:
        r0 = r10.executor;	 Catch:{ all -> 0x0012 }
        r1 = r10.cleanupRunnable;	 Catch:{ all -> 0x0012 }
        r0.execute(r1);	 Catch:{ all -> 0x0012 }
        goto L_0x0057;
    L_0x00fc:
        r0 = r10.lruEntries;	 Catch:{ all -> 0x0012 }
        r1 = r2.key;	 Catch:{ all -> 0x0012 }
        r0.remove(r1);	 Catch:{ all -> 0x0012 }
        r0 = r10.journalWriter;	 Catch:{ all -> 0x0012 }
        r1 = "REMOVE";
        r0 = r0.writeUtf8(r1);	 Catch:{ all -> 0x0012 }
        r1 = 32;
        r0.writeByte(r1);	 Catch:{ all -> 0x0012 }
        r0 = r10.journalWriter;	 Catch:{ all -> 0x0012 }
        r1 = r2.key;	 Catch:{ all -> 0x0012 }
        r0.writeUtf8(r1);	 Catch:{ all -> 0x0012 }
        r0 = r10.journalWriter;	 Catch:{ all -> 0x0012 }
        r1 = 10;
        r0.writeByte(r1);	 Catch:{ all -> 0x0012 }
        goto L_0x00e0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.internal.DiskLruCache.completeEdit(com.squareup.okhttp.internal.DiskLruCache$Editor, boolean):void");
    }

    private boolean journalRebuildRequired() {
        return (this.redundantOpCount < 2000 || this.redundantOpCount < this.lruEntries.size()) ? $assertionsDisabled : true;
    }

    public synchronized boolean remove(String str) {
        boolean z;
        initialize();
        checkNotClosed();
        validateKey(str);
        Entry entry = (Entry) this.lruEntries.get(str);
        if (entry == null) {
            z = $assertionsDisabled;
        } else {
            z = removeEntry(entry);
        }
        return z;
    }

    private boolean removeEntry(Entry entry) {
        if (entry.currentEditor != null) {
            entry.currentEditor.hasErrors = true;
        }
        for (int i = 0; i < this.valueCount; i++) {
            this.fileSystem.delete(entry.cleanFiles[i]);
            this.size -= entry.lengths[i];
            entry.lengths[i] = 0;
        }
        this.redundantOpCount++;
        this.journalWriter.writeUtf8(REMOVE).writeByte(32).writeUtf8(entry.key).writeByte(10);
        this.lruEntries.remove(entry.key);
        if (journalRebuildRequired()) {
            this.executor.execute(this.cleanupRunnable);
        }
        return true;
    }

    public synchronized boolean isClosed() {
        return this.closed;
    }

    private synchronized void checkNotClosed() {
        if (isClosed()) {
            throw new IllegalStateException("cache is closed");
        }
    }

    public synchronized void flush() {
        if (this.initialized) {
            checkNotClosed();
            trimToSize();
            this.journalWriter.flush();
        }
    }

    public synchronized void close() {
        if (!this.initialized || this.closed) {
            this.closed = true;
        } else {
            for (Entry entry : (Entry[]) this.lruEntries.values().toArray(new Entry[this.lruEntries.size()])) {
                if (entry.currentEditor != null) {
                    entry.currentEditor.abort();
                }
            }
            trimToSize();
            this.journalWriter.close();
            this.journalWriter = null;
            this.closed = true;
        }
    }

    private void trimToSize() {
        while (this.size > this.maxSize) {
            removeEntry((Entry) this.lruEntries.values().iterator().next());
        }
    }

    public void delete() {
        close();
        this.fileSystem.deleteContents(this.directory);
    }

    public synchronized void evictAll() {
        initialize();
        for (Entry removeEntry : (Entry[]) this.lruEntries.values().toArray(new Entry[this.lruEntries.size()])) {
            removeEntry(removeEntry);
        }
    }

    private void validateKey(String str) {
        if (!LEGAL_KEY_PATTERN.matcher(str).matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + str + "\"");
        }
    }

    public synchronized Iterator<Snapshot> snapshots() {
        initialize();
        return new C06413();
    }
}
