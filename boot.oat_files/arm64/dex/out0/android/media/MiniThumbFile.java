package android.media;

import android.app.backup.FullBackup;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Hashtable;

public class MiniThumbFile {
    public static final int BYTES_PER_MINTHUMB = 10000;
    private static final int HEADER_SIZE = 13;
    private static final int MINI_THUMB_DATA_FILE_VERSION = 3;
    private static final String TAG = "MiniThumbFile";
    private static final Hashtable<String, MiniThumbFile> sThumbFiles = new Hashtable();
    private ByteBuffer mBuffer = ByteBuffer.allocateDirect(10000);
    private FileChannel mChannel;
    private RandomAccessFile mMiniThumbFile;
    private Uri mUri;

    public static synchronized void reset() {
        synchronized (MiniThumbFile.class) {
            for (MiniThumbFile file : sThumbFiles.values()) {
                file.deactivate();
            }
            sThumbFiles.clear();
        }
    }

    public static synchronized MiniThumbFile instance(Uri uri) {
        MiniThumbFile file;
        synchronized (MiniThumbFile.class) {
            String type = (String) uri.getPathSegments().get(1);
            file = (MiniThumbFile) sThumbFiles.get(type);
            if (file == null) {
                file = new MiniThumbFile(Uri.parse("content://media/external/" + type + "/media"));
                sThumbFiles.put(type, file);
            }
        }
        return file;
    }

    private String randomAccessFilePath(int version) {
        return (Environment.getExternalStorageDirectory().toString() + "/DCIM/.thumbnails") + "/.thumbdata" + version + "-" + this.mUri.hashCode();
    }

    private void removeOldFile() {
        File oldFile = new File(randomAccessFilePath(2));
        if (oldFile.exists()) {
            try {
                oldFile.delete();
            } catch (SecurityException e) {
            }
        }
    }

    private RandomAccessFile miniThumbDataFile() {
        if (this.mMiniThumbFile == null) {
            removeOldFile();
            String path = randomAccessFilePath(3);
            File directory = new File(path).getParentFile();
            if (!(directory.isDirectory() || directory.mkdirs())) {
                Log.e(TAG, "Unable to create .thumbnails directory " + directory.toString());
            }
            File f = new File(path);
            try {
                this.mMiniThumbFile = new RandomAccessFile(f, "rw");
            } catch (IOException e) {
                try {
                    this.mMiniThumbFile = new RandomAccessFile(f, FullBackup.ROOT_TREE_TOKEN);
                } catch (IOException e2) {
                }
            }
            if (this.mMiniThumbFile != null) {
                this.mChannel = this.mMiniThumbFile.getChannel();
            }
        }
        return this.mMiniThumbFile;
    }

    public MiniThumbFile(Uri uri) {
        this.mUri = uri;
    }

    public synchronized void deactivate() {
        if (this.mMiniThumbFile != null) {
            try {
                this.mMiniThumbFile.close();
                this.mMiniThumbFile = null;
            } catch (IOException e) {
            }
        }
    }

    public synchronized long getMagic(long id) {
        long j;
        if (miniThumbDataFile() != null) {
            long pos = id * 10000;
            FileLock lock = null;
            try {
                this.mBuffer.clear();
                this.mBuffer.limit(9);
                lock = this.mChannel.lock(pos, 9, true);
                if (this.mChannel.read(this.mBuffer, pos) == 9) {
                    this.mBuffer.position(0);
                    if (this.mBuffer.get() == (byte) 1) {
                        j = this.mBuffer.getLong();
                        if (lock != null) {
                            try {
                                lock.release();
                            } catch (IOException e) {
                            }
                        }
                    }
                }
                if (lock != null) {
                    try {
                        lock.release();
                    } catch (IOException e2) {
                    }
                }
            } catch (IOException ex) {
                Log.v(TAG, "Got exception checking file magic: ", ex);
                if (lock != null) {
                    try {
                        lock.release();
                    } catch (IOException e3) {
                    }
                }
            } catch (RuntimeException ex2) {
                Log.e(TAG, "Got exception when reading magic, id = " + id + ", disk full or mount read-only? " + ex2.getClass());
                if (lock != null) {
                    try {
                        lock.release();
                    } catch (IOException e4) {
                    }
                }
            } catch (Throwable th) {
                if (lock != null) {
                    try {
                        lock.release();
                    } catch (IOException e5) {
                    }
                }
            }
        }
        j = 0;
        return j;
    }

    public synchronized void saveMiniThumbToFile(byte[] data, long id, long magic) throws IOException {
        if (miniThumbDataFile() != null) {
            long pos = id * 10000;
            FileLock lock = null;
            if (data != null) {
                try {
                    if (data.length <= 9987) {
                        this.mBuffer.clear();
                        this.mBuffer.put((byte) 1);
                        this.mBuffer.putLong(magic);
                        this.mBuffer.putInt(data.length);
                        this.mBuffer.put(data);
                        this.mBuffer.flip();
                        lock = this.mChannel.lock(pos, 10000, false);
                        this.mChannel.write(this.mBuffer, pos);
                    } else if (lock != null) {
                        try {
                            lock.release();
                        } catch (IOException e) {
                        }
                    }
                } catch (IOException ex) {
                    Log.e(TAG, "couldn't save mini thumbnail data for " + id + "; ", ex);
                    throw ex;
                } catch (RuntimeException ex2) {
                    Log.e(TAG, "couldn't save mini thumbnail data for " + id + "; disk full or mount read-only? " + ex2.getClass());
                    if (lock != null) {
                        try {
                            lock.release();
                        } catch (IOException e2) {
                        }
                    }
                } catch (Throwable th) {
                    if (lock != null) {
                        try {
                            lock.release();
                        } catch (IOException e3) {
                        }
                    }
                }
            }
            if (lock != null) {
                try {
                    lock.release();
                } catch (IOException e4) {
                }
            }
        }
        return;
    }

    public synchronized byte[] getMiniThumbFromFile(long id, byte[] data) {
        if (miniThumbDataFile() == null) {
            data = null;
        } else {
            long pos = id * 10000;
            FileLock lock = null;
            try {
                this.mBuffer.clear();
                lock = this.mChannel.lock(pos, 10000, true);
                int size = this.mChannel.read(this.mBuffer, pos);
                if (size > 13) {
                    this.mBuffer.position(0);
                    byte flag = this.mBuffer.get();
                    long magic = this.mBuffer.getLong();
                    int length = this.mBuffer.getInt();
                    if (size >= length + 13 && length != 0 && magic != 0 && flag == (byte) 1 && data.length >= length) {
                        this.mBuffer.get(data, 0, length);
                        if (lock != null) {
                            try {
                                lock.release();
                            } catch (IOException e) {
                            }
                        }
                    }
                }
                if (lock != null) {
                    try {
                        lock.release();
                    } catch (IOException e2) {
                    }
                }
            } catch (IOException ex) {
                Log.w(TAG, "got exception when reading thumbnail id=" + id + ", exception: " + ex);
                if (lock != null) {
                    try {
                        lock.release();
                    } catch (IOException e3) {
                    }
                }
            } catch (RuntimeException ex2) {
                Log.e(TAG, "Got exception when reading thumbnail, id = " + id + ", disk full or mount read-only? " + ex2.getClass());
                if (lock != null) {
                    try {
                        lock.release();
                    } catch (IOException e4) {
                    }
                }
            } catch (Throwable th) {
                if (lock != null) {
                    try {
                        lock.release();
                    } catch (IOException e5) {
                    }
                }
            }
            data = null;
        }
        return data;
    }
}
