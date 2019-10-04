/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.Closeable
 *  java.io.File
 *  java.io.FileNotFoundException
 *  java.io.IOException
 *  java.lang.AssertionError
 *  java.lang.CharSequence
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.LinkedHashMap
 *  java.util.NoSuchElementException
 *  java.util.concurrent.BlockingQueue
 *  java.util.concurrent.Executor
 *  java.util.concurrent.LinkedBlockingQueue
 *  java.util.concurrent.ThreadFactory
 *  java.util.concurrent.ThreadPoolExecutor
 *  java.util.concurrent.TimeUnit
 *  java.util.regex.Matcher
 *  java.util.regex.Pattern
 */
package com.squareup.okhttp.internal;

import com.squareup.okhttp.internal.FaultHidingSink;
import com.squareup.okhttp.internal.Platform;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.io.FileSystem;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class DiskLruCache
implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final long ANY_SEQUENCE_NUMBER = -1L;
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
    private final Runnable cleanupRunnable = new Runnable(){

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void run() {
            DiskLruCache diskLruCache;
            DiskLruCache diskLruCache2 = diskLruCache = DiskLruCache.this;
            synchronized (diskLruCache2) {
                boolean bl = DiskLruCache.this.initialized;
                boolean bl2 = false;
                if (!bl) {
                    bl2 = true;
                }
                if (bl2 | DiskLruCache.this.closed) {
                    return;
                }
                try {
                    DiskLruCache.this.trimToSize();
                    if (DiskLruCache.this.journalRebuildRequired()) {
                        DiskLruCache.this.rebuildJournal();
                        DiskLruCache.this.redundantOpCount = 0;
                    }
                    return;
                }
                catch (IOException iOException) {
                    throw new RuntimeException((Throwable)iOException);
                }
            }
        }
    };
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
    private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap(0, 0.75f, true);
    private long maxSize;
    private long nextSequenceNumber = 0L;
    private int redundantOpCount;
    private long size = 0L;
    private final int valueCount;

    /*
     * Enabled aggressive block sorting
     */
    static {
        boolean bl = !DiskLruCache.class.desiredAssertionStatus();
        $assertionsDisabled = bl;
        LEGAL_KEY_PATTERN = Pattern.compile((String)"[a-z0-9_-]{1,120}");
        NULL_SINK = new Sink(){

            @Override
            public void close() {
            }

            @Override
            public void flush() {
            }

            @Override
            public Timeout timeout() {
                return Timeout.NONE;
            }

            @Override
            public void write(Buffer buffer, long l2) {
                buffer.skip(l2);
            }
        };
    }

    DiskLruCache(FileSystem fileSystem, File file, int n2, int n3, long l2, Executor executor) {
        this.fileSystem = fileSystem;
        this.directory = file;
        this.appVersion = n2;
        this.journalFile = new File(file, JOURNAL_FILE);
        this.journalFileTmp = new File(file, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(file, JOURNAL_FILE_BACKUP);
        this.valueCount = n3;
        this.maxSize = l2;
        this.executor = executor;
    }

    private void checkNotClosed() {
        DiskLruCache diskLruCache = this;
        synchronized (diskLruCache) {
            if (this.isClosed()) {
                throw new IllegalStateException("cache is closed");
            }
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private void completeEdit(Editor var1_1, boolean var2_2) {
        block20 : {
            var28_3 = this;
            // MONITORENTER : var28_3
            var4_4 = Editor.access$1700(var1_1);
            if (Entry.access$900(var4_4) != var1_1) {
                throw new IllegalStateException();
            }
            var5_5 = 0;
            if (var2_2) {
                var25_6 = Entry.access$800(var4_4);
                var5_5 = 0;
                if (!var25_6) {
                    var26_7 = 0;
                    do {
                        var27_8 = this.valueCount;
                        var5_5 = 0;
                        if (var26_7 >= var27_8) break block20;
                        if (!Editor.access$1800(var1_1)[var26_7]) {
                            var1_1.abort();
                            throw new IllegalStateException("Newly created entry didn't create value for index " + var26_7);
                        }
                        if (!this.fileSystem.exists(Entry.access$1400(var4_4)[var26_7])) {
                            var1_1.abort();
                            return;
                        }
                        ++var26_7;
                    } while (true);
                    do {
                        // MONITOREXIT : var28_3
                        return;
                        break;
                    } while (true);
                }
            }
        }
        do {
            if (var5_5 < this.valueCount) {
                var19_9 = Entry.access$1400(var4_4)[var5_5];
                if (var2_2) {
                    if (this.fileSystem.exists(var19_9)) {
                        var20_10 = Entry.access$1300(var4_4)[var5_5];
                        this.fileSystem.rename(var19_9, var20_10);
                        var21_11 = Entry.access$1200(var4_4)[var5_5];
                        Entry.access$1200((Entry)var4_4)[var5_5] = var23_12 = this.fileSystem.size(var20_10);
                        this.size = var23_12 + (this.size - var21_11);
                    }
                } else {
                    this.fileSystem.delete(var19_9);
                }
            } else {
                this.redundantOpCount = 1 + this.redundantOpCount;
                Entry.access$902(var4_4, null);
                if (var2_2 | Entry.access$800(var4_4)) {
                    Entry.access$802(var4_4, true);
                    this.journalWriter.writeUtf8("CLEAN").writeByte(32);
                    this.journalWriter.writeUtf8(Entry.access$1500(var4_4));
                    var4_4.writeLengths(this.journalWriter);
                    this.journalWriter.writeByte(10);
                    if (var2_2) {
                        var15_13 = this.nextSequenceNumber;
                        this.nextSequenceNumber = 1L + var15_13;
                        Entry.access$1602(var4_4, var15_13);
                    }
                } else {
                    this.lruEntries.remove((Object)Entry.access$1500(var4_4));
                    this.journalWriter.writeUtf8("REMOVE").writeByte(32);
                    this.journalWriter.writeUtf8(Entry.access$1500(var4_4));
                    this.journalWriter.writeByte(10);
                }
                this.journalWriter.flush();
                if (this.size <= this.maxSize && !this.journalRebuildRequired()) ** continue;
                this.executor.execute(this.cleanupRunnable);
                return;
            }
            ++var5_5;
        } while (true);
    }

    public static DiskLruCache create(FileSystem fileSystem, File file, int n2, int n3, long l2) {
        if (l2 <= 0L) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (n3 <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        }
        return new DiskLruCache(fileSystem, file, n2, n3, l2, (Executor)new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, (BlockingQueue)new LinkedBlockingQueue(), Util.threadFactory("OkHttp DiskLruCache", true)));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Editor edit(String string, long l2) {
        DiskLruCache diskLruCache = this;
        synchronized (diskLruCache) {
            Entry entry;
            this.initialize();
            this.checkNotClosed();
            this.validateKey(string);
            Entry entry2 = (Entry)this.lruEntries.get((Object)string);
            if (l2 != -1L) {
                if (entry2 == null) return null;
                long l3 = entry2.sequenceNumber;
                if (l3 != l2) {
                    return null;
                }
            }
            if (entry2 != null && entry2.currentEditor != null) {
                return null;
            }
            this.journalWriter.writeUtf8(DIRTY).writeByte(32).writeUtf8(string).writeByte(10);
            this.journalWriter.flush();
            if (this.hasJournalErrors) {
                return null;
            }
            if (entry2 == null) {
                Entry entry3 = new Entry(string);
                this.lruEntries.put((Object)string, (Object)entry3);
                entry = entry3;
            } else {
                entry = entry2;
            }
            Editor editor = new Editor(entry);
            entry.currentEditor = editor;
            return editor;
        }
    }

    private boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }

    private BufferedSink newJournalWriter() {
        return Okio.buffer(new FaultHidingSink(this.fileSystem.appendingSink(this.journalFile)){
            static final /* synthetic */ boolean $assertionsDisabled;

            /*
             * Enabled aggressive block sorting
             */
            static {
                boolean bl = !DiskLruCache.class.desiredAssertionStatus();
                $assertionsDisabled = bl;
            }

            @Override
            protected void onException(IOException iOException) {
                if (!$assertionsDisabled && !Thread.holdsLock((Object)DiskLruCache.this)) {
                    throw new AssertionError();
                }
                DiskLruCache.this.hasJournalErrors = true;
            }
        });
    }

    private void processJournal() {
        this.fileSystem.delete(this.journalFileTmp);
        Iterator iterator = this.lruEntries.values().iterator();
        while (iterator.hasNext()) {
            Entry entry = (Entry)iterator.next();
            if (entry.currentEditor == null) {
                for (int i2 = 0; i2 < this.valueCount; ++i2) {
                    this.size += entry.lengths[i2];
                }
                continue;
            }
            entry.currentEditor = null;
            for (int i3 = 0; i3 < this.valueCount; ++i3) {
                this.fileSystem.delete(entry.cleanFiles[i3]);
                this.fileSystem.delete(entry.dirtyFiles[i3]);
            }
            iterator.remove();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void readJournal() {
        BufferedSource bufferedSource;
        block9 : {
            bufferedSource = Okio.buffer(this.fileSystem.source(this.journalFile));
            String string = bufferedSource.readUtf8LineStrict();
            String string2 = bufferedSource.readUtf8LineStrict();
            String string3 = bufferedSource.readUtf8LineStrict();
            String string4 = bufferedSource.readUtf8LineStrict();
            String string5 = bufferedSource.readUtf8LineStrict();
            if (!(MAGIC.equals((Object)string) && VERSION_1.equals((Object)string2) && Integer.toString((int)this.appVersion).equals((Object)string3) && Integer.toString((int)this.valueCount).equals((Object)string4) && "".equals((Object)string5))) {
                throw new IOException("unexpected journal header: [" + string + ", " + string2 + ", " + string4 + ", " + string5 + "]");
            }
            break block9;
            finally {
                Util.closeQuietly(bufferedSource);
            }
        }
        int n2 = 0;
        do {
            this.readJournalLine(bufferedSource.readUtf8LineStrict());
            ++n2;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void readJournalLine(String string) {
        String string2;
        Entry entry;
        int n2 = string.indexOf(32);
        if (n2 == -1) {
            throw new IOException("unexpected journal line: " + string);
        }
        int n3 = n2 + 1;
        int n4 = string.indexOf(32, n3);
        if (n4 != -1) {
            string2 = string.substring(n3, n4);
        } else {
            String string3 = string.substring(n3);
            if (n2 == REMOVE.length() && string.startsWith(REMOVE)) {
                this.lruEntries.remove((Object)string3);
                return;
            }
            string2 = string3;
        }
        if ((entry = (Entry)this.lruEntries.get((Object)string2)) == null) {
            entry = new Entry(string2);
            this.lruEntries.put((Object)string2, (Object)entry);
        }
        if (n4 != -1 && n2 == CLEAN.length() && string.startsWith(CLEAN)) {
            String[] arrstring = string.substring(n4 + 1).split(" ");
            entry.readable = true;
            entry.currentEditor = null;
            entry.setLengths(arrstring);
            return;
        }
        if (n4 == -1 && n2 == DIRTY.length() && string.startsWith(DIRTY)) {
            entry.currentEditor = new Editor(entry);
            return;
        }
        if (n4 == -1 && n2 == READ.length() && string.startsWith(READ)) return;
        {
            throw new IOException("unexpected journal line: " + string);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void rebuildJournal() {
        DiskLruCache diskLruCache = this;
        synchronized (diskLruCache) {
            if (this.journalWriter != null) {
                this.journalWriter.close();
            }
            BufferedSink bufferedSink = Okio.buffer(this.fileSystem.sink(this.journalFileTmp));
            try {
                bufferedSink.writeUtf8(MAGIC).writeByte(10);
                bufferedSink.writeUtf8(VERSION_1).writeByte(10);
                bufferedSink.writeDecimalLong(this.appVersion).writeByte(10);
                bufferedSink.writeDecimalLong(this.valueCount).writeByte(10);
                bufferedSink.writeByte(10);
                for (Entry entry : this.lruEntries.values()) {
                    if (entry.currentEditor != null) {
                        bufferedSink.writeUtf8(DIRTY).writeByte(32);
                        bufferedSink.writeUtf8(entry.key);
                        bufferedSink.writeByte(10);
                        continue;
                    }
                    bufferedSink.writeUtf8(CLEAN).writeByte(32);
                    bufferedSink.writeUtf8(entry.key);
                    entry.writeLengths(bufferedSink);
                    bufferedSink.writeByte(10);
                }
            }
            finally {
                bufferedSink.close();
            }
            if (this.fileSystem.exists(this.journalFile)) {
                this.fileSystem.rename(this.journalFile, this.journalFileBackup);
            }
            this.fileSystem.rename(this.journalFileTmp, this.journalFile);
            this.fileSystem.delete(this.journalFileBackup);
            this.journalWriter = this.newJournalWriter();
            this.hasJournalErrors = false;
            return;
        }
    }

    private boolean removeEntry(Entry entry) {
        if (entry.currentEditor != null) {
            entry.currentEditor.hasErrors = true;
        }
        for (int i2 = 0; i2 < this.valueCount; ++i2) {
            this.fileSystem.delete(entry.cleanFiles[i2]);
            this.size -= entry.lengths[i2];
            Entry.access$1200((Entry)entry)[i2] = 0L;
        }
        this.redundantOpCount = 1 + this.redundantOpCount;
        this.journalWriter.writeUtf8(REMOVE).writeByte(32).writeUtf8(entry.key).writeByte(10);
        this.lruEntries.remove((Object)entry.key);
        if (this.journalRebuildRequired()) {
            this.executor.execute(this.cleanupRunnable);
        }
        return true;
    }

    private void trimToSize() {
        while (this.size > this.maxSize) {
            this.removeEntry((Entry)this.lruEntries.values().iterator().next());
        }
    }

    private void validateKey(String string) {
        if (!LEGAL_KEY_PATTERN.matcher((CharSequence)string).matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + string + "\"");
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void close() {
        DiskLruCache diskLruCache = this;
        // MONITORENTER : diskLruCache
        if (!this.initialized || this.closed) {
            this.closed = true;
            // MONITOREXIT : diskLruCache
            return;
        }
        Entry[] arrentry = (Entry[])this.lruEntries.values().toArray((Object[])new Entry[this.lruEntries.size()]);
        int n2 = arrentry.length;
        int n3 = 0;
        do {
            if (n3 >= n2) {
                this.trimToSize();
                this.journalWriter.close();
                this.journalWriter = null;
                this.closed = true;
                return;
            }
            Entry entry = arrentry[n3];
            if (entry.currentEditor != null) {
                entry.currentEditor.abort();
            }
            ++n3;
        } while (true);
    }

    public void delete() {
        this.close();
        this.fileSystem.deleteContents(this.directory);
    }

    public Editor edit(String string) {
        return this.edit(string, -1L);
    }

    public void evictAll() {
        DiskLruCache diskLruCache = this;
        synchronized (diskLruCache) {
            this.initialize();
            Entry[] arrentry = (Entry[])this.lruEntries.values().toArray((Object[])new Entry[this.lruEntries.size()]);
            int n2 = arrentry.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                this.removeEntry(arrentry[i2]);
            }
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void flush() {
        DiskLruCache diskLruCache = this;
        synchronized (diskLruCache) {
            block6 : {
                boolean bl = this.initialized;
                if (bl) break block6;
                do {
                    return;
                    break;
                } while (true);
            }
            this.checkNotClosed();
            this.trimToSize();
            this.journalWriter.flush();
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public Snapshot get(String string) {
        DiskLruCache diskLruCache = this;
        synchronized (diskLruCache) {
            Snapshot snapshot;
            block9 : {
                Entry entry;
                block8 : {
                    this.initialize();
                    this.checkNotClosed();
                    this.validateKey(string);
                    entry = (Entry)this.lruEntries.get((Object)string);
                    if (entry == null) return null;
                    boolean bl = entry.readable;
                    if (bl) break block8;
                    return null;
                }
                snapshot = entry.snapshot();
                if (snapshot != null) break block9;
                return null;
            }
            this.redundantOpCount = 1 + this.redundantOpCount;
            this.journalWriter.writeUtf8(READ).writeByte(32).writeUtf8(string).writeByte(10);
            if (!this.journalRebuildRequired()) return snapshot;
            this.executor.execute(this.cleanupRunnable);
            return snapshot;
        }
    }

    public File getDirectory() {
        return this.directory;
    }

    public long getMaxSize() {
        DiskLruCache diskLruCache = this;
        synchronized (diskLruCache) {
            long l2 = this.maxSize;
            return l2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void initialize() {
        if (!$assertionsDisabled && !Thread.holdsLock((Object)this)) {
            throw new AssertionError();
        }
        if (this.initialized) {
            return;
        }
        if (this.fileSystem.exists(this.journalFileBackup)) {
            if (this.fileSystem.exists(this.journalFile)) {
                this.fileSystem.delete(this.journalFileBackup);
            } else {
                this.fileSystem.rename(this.journalFileBackup, this.journalFile);
            }
        }
        if (this.fileSystem.exists(this.journalFile)) {
            try {
                this.readJournal();
                this.processJournal();
                this.initialized = true;
                return;
            }
            catch (IOException iOException) {
                Platform.get().logW("DiskLruCache " + (Object)this.directory + " is corrupt: " + iOException.getMessage() + ", removing");
                this.delete();
                this.closed = false;
            }
        }
        this.rebuildJournal();
        this.initialized = true;
    }

    public boolean isClosed() {
        DiskLruCache diskLruCache = this;
        synchronized (diskLruCache) {
            boolean bl = this.closed;
            return bl;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean remove(String string) {
        DiskLruCache diskLruCache = this;
        synchronized (diskLruCache) {
            Entry entry;
            block4 : {
                this.initialize();
                this.checkNotClosed();
                this.validateKey(string);
                entry = (Entry)this.lruEntries.get((Object)string);
                if (entry != null) break block4;
                return false;
            }
            boolean bl = this.removeEntry(entry);
            return bl;
        }
    }

    public void setMaxSize(long l2) {
        DiskLruCache diskLruCache = this;
        synchronized (diskLruCache) {
            this.maxSize = l2;
            if (this.initialized) {
                this.executor.execute(this.cleanupRunnable);
            }
            return;
        }
    }

    public long size() {
        DiskLruCache diskLruCache = this;
        synchronized (diskLruCache) {
            this.initialize();
            long l2 = this.size;
            return l2;
        }
    }

    public Iterator<Snapshot> snapshots() {
        DiskLruCache diskLruCache = this;
        synchronized (diskLruCache) {
            this.initialize();
            Iterator<Snapshot> iterator = new Iterator<Snapshot>(){
                final Iterator<Entry> delegate;
                Snapshot nextSnapshot;
                Snapshot removeSnapshot;
                {
                    this.delegate = new ArrayList(DiskLruCache.this.lruEntries.values()).iterator();
                }

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                public boolean hasNext() {
                    DiskLruCache diskLruCache;
                    if (this.nextSnapshot != null) {
                        return true;
                    }
                    DiskLruCache diskLruCache2 = diskLruCache = DiskLruCache.this;
                    synchronized (diskLruCache2) {
                        Snapshot snapshot;
                        if (DiskLruCache.this.closed) {
                            return false;
                        }
                        do {
                            if (this.delegate.hasNext()) continue;
                            return false;
                        } while ((snapshot = ((Entry)this.delegate.next()).snapshot()) == null);
                        this.nextSnapshot = snapshot;
                        return true;
                    }
                }

                public Snapshot next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.removeSnapshot = this.nextSnapshot;
                    this.nextSnapshot = null;
                    return this.removeSnapshot;
                }

                public void remove() {
                    if (this.removeSnapshot == null) {
                        throw new IllegalStateException("remove() before next()");
                    }
                    try {
                        DiskLruCache.this.remove(this.removeSnapshot.key);
                        return;
                    }
                    catch (IOException iOException) {
                        return;
                    }
                    finally {
                        this.removeSnapshot = null;
                    }
                }
            };
            return iterator;
        }
    }

    public final class Editor {
        private boolean committed;
        private final Entry entry;
        private boolean hasErrors;
        private final boolean[] written;

        /*
         * Enabled aggressive block sorting
         */
        private Editor(Entry entry) {
            this.entry = entry;
            boolean[] arrbl = entry.readable ? null : new boolean[DiskLruCache.this.valueCount];
            this.written = arrbl;
        }

        static /* synthetic */ Entry access$1700(Editor editor) {
            return editor.entry;
        }

        static /* synthetic */ boolean[] access$1800(Editor editor) {
            return editor.written;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void abort() {
            DiskLruCache diskLruCache;
            DiskLruCache diskLruCache2 = diskLruCache = DiskLruCache.this;
            synchronized (diskLruCache2) {
                DiskLruCache.this.completeEdit(this, false);
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void abortUnlessCommitted() {
            DiskLruCache diskLruCache;
            DiskLruCache diskLruCache2 = diskLruCache = DiskLruCache.this;
            synchronized (diskLruCache2) {
                boolean bl = this.committed;
                if (!bl) {
                    try {
                        DiskLruCache.this.completeEdit(this, false);
                    }
                    catch (IOException iOException) {}
                }
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void commit() {
            DiskLruCache diskLruCache;
            DiskLruCache diskLruCache2 = diskLruCache = DiskLruCache.this;
            synchronized (diskLruCache2) {
                if (this.hasErrors) {
                    DiskLruCache.this.completeEdit(this, false);
                    DiskLruCache.this.removeEntry(this.entry);
                } else {
                    DiskLruCache.this.completeEdit(this, true);
                }
                this.committed = true;
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public Sink newSink(int n2) {
            DiskLruCache diskLruCache;
            DiskLruCache diskLruCache2 = diskLruCache = DiskLruCache.this;
            synchronized (diskLruCache2) {
                Sink sink;
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!this.entry.readable) {
                    this.written[n2] = true;
                }
                File file = this.entry.dirtyFiles[n2];
                try {
                    sink = DiskLruCache.this.fileSystem.sink(file);
                }
                catch (FileNotFoundException fileNotFoundException) {
                    return NULL_SINK;
                }
                return new FaultHidingSink(sink){

                    /*
                     * Enabled aggressive block sorting
                     * Enabled unnecessary exception pruning
                     * Enabled aggressive exception aggregation
                     */
                    @Override
                    protected void onException(IOException iOException) {
                        DiskLruCache diskLruCache;
                        DiskLruCache diskLruCache2 = diskLruCache = DiskLruCache.this;
                        synchronized (diskLruCache2) {
                            Editor.this.hasErrors = true;
                            return;
                        }
                    }
                };
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public Source newSource(int n2) {
            DiskLruCache diskLruCache;
            DiskLruCache diskLruCache2 = diskLruCache = DiskLruCache.this;
            synchronized (diskLruCache2) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!this.entry.readable) {
                    return null;
                }
                try {
                    return DiskLruCache.this.fileSystem.source(this.entry.cleanFiles[n2]);
                }
                catch (FileNotFoundException fileNotFoundException) {
                    return null;
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

        private Entry(String string) {
            this.key = string;
            this.lengths = new long[DiskLruCache.this.valueCount];
            this.cleanFiles = new File[DiskLruCache.this.valueCount];
            this.dirtyFiles = new File[DiskLruCache.this.valueCount];
            StringBuilder stringBuilder = new StringBuilder(string).append('.');
            int n2 = stringBuilder.length();
            for (int i2 = 0; i2 < DiskLruCache.this.valueCount; ++i2) {
                stringBuilder.append(i2);
                this.cleanFiles[i2] = new File(DiskLruCache.this.directory, stringBuilder.toString());
                stringBuilder.append(".tmp");
                this.dirtyFiles[i2] = new File(DiskLruCache.this.directory, stringBuilder.toString());
                stringBuilder.setLength(n2);
            }
        }

        static /* synthetic */ long access$1602(Entry entry, long l2) {
            entry.sequenceNumber = l2;
            return l2;
        }

        private IOException invalidLengths(String[] arrstring) {
            throw new IOException("unexpected journal line: " + Arrays.toString((Object[])arrstring));
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void setLengths(String[] arrstring) {
            if (arrstring.length != DiskLruCache.this.valueCount) {
                throw this.invalidLengths(arrstring);
            }
            try {
                for (int i2 = 0; i2 < arrstring.length; ++i2) {
                    this.lengths[i2] = Long.parseLong((String)arrstring[i2]);
                }
                return;
            }
            catch (NumberFormatException numberFormatException) {
                throw this.invalidLengths(arrstring);
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        Snapshot snapshot() {
            int n2;
            if (!Thread.holdsLock((Object)DiskLruCache.this)) {
                throw new AssertionError();
            }
            Source[] arrsource = new Source[DiskLruCache.this.valueCount];
            long[] arrl = (long[])this.lengths.clone();
            int n3 = 0;
            try {
                while (n3 < DiskLruCache.this.valueCount) {
                    arrsource[n3] = DiskLruCache.this.fileSystem.source(this.cleanFiles[n3]);
                    ++n3;
                }
                return new Snapshot(this.key, this.sequenceNumber, arrsource, arrl);
            }
            catch (FileNotFoundException fileNotFoundException) {
                n2 = 0;
            }
            while (n2 < DiskLruCache.this.valueCount) {
                if (arrsource[n2] == null) return null;
                Util.closeQuietly(arrsource[n2]);
                ++n2;
            }
            return null;
        }

        void writeLengths(BufferedSink bufferedSink) {
            for (long l2 : this.lengths) {
                bufferedSink.writeByte(32).writeDecimalLong(l2);
            }
        }
    }

    public final class Snapshot
    implements Closeable {
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        private final Source[] sources;

        private Snapshot(String string, long l2, Source[] arrsource, long[] arrl) {
            this.key = string;
            this.sequenceNumber = l2;
            this.sources = arrsource;
            this.lengths = arrl;
        }

        public void close() {
            Source[] arrsource = this.sources;
            int n2 = arrsource.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                Util.closeQuietly(arrsource[i2]);
            }
        }

        public Editor edit() {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        public long getLength(int n2) {
            return this.lengths[n2];
        }

        public Source getSource(int n2) {
            return this.sources[n2];
        }

        public String key() {
            return this.key;
        }
    }

}

