package android.media;

import android.app.backup.FullBackup;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class MediaMetadataRetriever {
    public static final int CONFIG_COLOR_ARGB8888 = 1;
    public static final int CONFIG_COLOR_RGB565 = 0;
    private static final int EMBEDDED_PICTURE_TYPE_ANY = 65535;
    public static final int METADATA_KEY_ALBUM = 1;
    public static final int METADATA_KEY_ALBUMARTIST = 13;
    public static final int METADATA_KEY_ALBUMARTIST_ASCII = 1009;
    public static final int METADATA_KEY_ALBUM_ASCII = 1001;
    public static final int METADATA_KEY_ARTIST = 2;
    public static final int METADATA_KEY_ARTIST_ASCII = 1002;
    public static final int METADATA_KEY_AUTHOR = 3;
    public static final int METADATA_KEY_AUTHOR_ASCII = 1003;
    public static final int METADATA_KEY_BITRATE = 20;
    public static final int METADATA_KEY_BITS_PER_SAMPLE = 1020;
    public static final int METADATA_KEY_CAPTURE_FRAMERATE = 25;
    public static final int METADATA_KEY_CD_TRACK_NUMBER = 0;
    public static final int METADATA_KEY_COMPILATION = 15;
    public static final int METADATA_KEY_COMPOSER = 4;
    public static final int METADATA_KEY_COMPOSER_ASCII = 1004;
    public static final int METADATA_KEY_DATE = 5;
    public static final int METADATA_KEY_DATE_ASCII = 1005;
    public static final int METADATA_KEY_DISC_NUMBER = 14;
    public static final int METADATA_KEY_DURATION = 9;
    public static final int METADATA_KEY_GENRE = 6;
    public static final int METADATA_KEY_GENRE_ASCII = 1006;
    public static final int METADATA_KEY_HAS_AUDIO = 16;
    public static final int METADATA_KEY_HAS_VIDEO = 17;
    public static final int METADATA_KEY_IS_DRM = 22;
    public static final int METADATA_KEY_LOCATION = 23;
    public static final int METADATA_KEY_MIMETYPE = 12;
    public static final int METADATA_KEY_MULTI_AUDIO_CHANNELS = 1012;
    public static final int METADATA_KEY_MULTI_AUDIO_LANGUAGES = 1011;
    public static final int METADATA_KEY_NUM_AUDIO_TRACKS = 1010;
    public static final int METADATA_KEY_NUM_TEXT_TRACKS = 1013;
    public static final int METADATA_KEY_NUM_TRACKS = 10;
    public static final int METADATA_KEY_SAMPLING_RATE = 1019;
    public static final int METADATA_KEY_SEC_360VIDEO = 1021;
    public static final int METADATA_KEY_SEC_3DVIDEOTYPE = 1018;
    public static final int METADATA_KEY_SEC_AUDIOCODECINFO = 1025;
    public static final int METADATA_KEY_SEC_AUTHORIZATION = 1015;
    public static final int METADATA_KEY_SEC_CITYID = 1017;
    public static final int METADATA_KEY_SEC_CREATIONTIME = 1026;
    public static final int METADATA_KEY_SEC_RECORDINGMODE = 1022;
    public static final int METADATA_KEY_SEC_SLOWVIDEOINFO = 1023;
    public static final int METADATA_KEY_SEC_VIDEOCODECINFO = 1024;
    public static final int METADATA_KEY_SEC_VIDEOSNAPSHOT = 1016;
    public static final int METADATA_KEY_SEC_WEATHER = 1014;
    public static final int METADATA_KEY_TIMED_TEXT_LANGUAGES = 21;
    public static final int METADATA_KEY_TITLE = 7;
    public static final int METADATA_KEY_TITLE_ASCII = 1007;
    public static final int METADATA_KEY_VIDEO_HEIGHT = 19;
    public static final int METADATA_KEY_VIDEO_ROTATION = 24;
    public static final int METADATA_KEY_VIDEO_WIDTH = 18;
    public static final int METADATA_KEY_WRITER = 11;
    public static final int METADATA_KEY_WRITER_ASCII = 1008;
    public static final int METADATA_KEY_YEAR = 8;
    public static final int OPTION_CLOSEST = 3;
    public static final int OPTION_CLOSEST_SYNC = 2;
    public static final int OPTION_NEXT_SYNC = 1;
    public static final int OPTION_PREVIOUS_SYNC = 0;
    private static final String TAG = "MediaMetadataRetriever";
    private long mNativeContext;

    private native Bitmap _getFrameAtTime(long j, int i);

    private native void _setDataSource(MediaDataSource mediaDataSource) throws IllegalArgumentException;

    private native void _setDataSource(IBinder iBinder, String str, String[] strArr, String[] strArr2) throws IllegalArgumentException;

    private native byte[] getEmbeddedPicture(int i);

    private final native void native_finalize();

    private static native void native_init();

    private native void native_setup();

    public native String extractMetadata(int i);

    public native void release();

    public native void setDataSource(FileDescriptor fileDescriptor, long j, long j2) throws IllegalArgumentException;

    public native void setVideoSize(int i, int i2, boolean z, boolean z2);

    static {
        System.loadLibrary("media_jni");
        native_init();
    }

    public MediaMetadataRetriever() {
        native_setup();
    }

    public void setDataSource(String path) throws IllegalArgumentException {
        FileInputStream fileInputStream;
        if (path == null) {
            Log.e(TAG, "setDataSource path is null");
            throw new IllegalArgumentException();
        }
        try {
            FileInputStream is = new FileInputStream(path);
            try {
                setDataSource(is.getFD(), 0, 576460752303423487L);
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                    }
                }
            } catch (FileNotFoundException e2) {
                fileInputStream = is;
                Log.e(TAG, "setDataSource - FileNotFoundException");
                throw new IllegalArgumentException();
            } catch (IOException e3) {
                fileInputStream = is;
                Log.e(TAG, "setDataSource - IOException");
                throw new IllegalArgumentException();
            }
        } catch (FileNotFoundException e4) {
            Log.e(TAG, "setDataSource - FileNotFoundException");
            throw new IllegalArgumentException();
        } catch (IOException e5) {
            Log.e(TAG, "setDataSource - IOException");
            throw new IllegalArgumentException();
        }
    }

    public void setDataSource(String uri, Map<String, String> headers) throws IllegalArgumentException {
        int i = 0;
        String[] keys = new String[headers.size()];
        String[] values = new String[headers.size()];
        for (Entry<String, String> entry : headers.entrySet()) {
            keys[i] = (String) entry.getKey();
            values[i] = (String) entry.getValue();
            i++;
        }
        _setDataSource(MediaHTTPService.createHttpServiceBinderIfNecessary(uri), uri, keys, values);
    }

    public void setDataSource(FileDescriptor fd) throws IllegalArgumentException {
        setDataSource(fd, 0, 576460752303423487L);
    }

    public void setDataSource(Context context, Uri uri) throws IllegalArgumentException, SecurityException {
        if (uri == null) {
            Log.e(TAG, "setDataSource - uri is null");
            throw new IllegalArgumentException();
        }
        String scheme = uri.getScheme();
        if (scheme == null || scheme.equals(ContentResolver.SCHEME_FILE)) {
            setDataSource(uri.getPath());
            return;
        }
        AssetFileDescriptor fd = null;
        try {
            fd = context.getContentResolver().openAssetFileDescriptor(uri, FullBackup.ROOT_TREE_TOKEN);
            if (fd == null) {
                Log.e(TAG, "setDataSource - fd is null");
                throw new IllegalArgumentException();
            }
            FileDescriptor descriptor = fd.getFileDescriptor();
            if (descriptor.valid()) {
                if (fd.getDeclaredLength() < 0) {
                    setDataSource(descriptor);
                } else {
                    setDataSource(descriptor, fd.getStartOffset(), fd.getDeclaredLength());
                }
                if (fd != null) {
                    try {
                        fd.close();
                        return;
                    } catch (IOException e) {
                        Log.e(TAG, "setDataSource -descriptor is not valid");
                        return;
                    }
                }
                return;
            }
            Log.e(TAG, "setDataSource -descriptor is not valid");
            throw new IllegalArgumentException();
        } catch (FileNotFoundException e2) {
            Log.e(TAG, "setDataSource - FileNotFoundException");
            throw new IllegalArgumentException();
        } catch (SecurityException e3) {
            if (fd != null) {
                try {
                    fd.close();
                } catch (IOException e4) {
                    Log.e(TAG, "setDataSource -descriptor is not valid");
                }
            }
            setDataSource(uri.toString());
        } catch (Throwable th) {
            if (fd != null) {
                try {
                    fd.close();
                } catch (IOException e5) {
                    Log.e(TAG, "setDataSource -descriptor is not valid");
                }
            }
        }
    }

    public void setDataSource(MediaDataSource dataSource) throws IllegalArgumentException {
        _setDataSource(dataSource);
    }

    public Bitmap getFrameAtTime(long timeUs, int option) {
        if (option >= 0 && option <= 3) {
            return _getFrameAtTime(timeUs, option);
        }
        throw new IllegalArgumentException("Unsupported option: " + option);
    }

    public Bitmap getFrameAtTime(long timeUs, int option, int config) {
        if (option < 0 || option > 3) {
            throw new IllegalArgumentException("Unsupported option: " + option);
        } else if (config < 0 || config > 1) {
            throw new IllegalArgumentException("Unsupported color format: " + config);
        } else {
            if (config == 1) {
                option += 16;
            }
            return _getFrameAtTime(timeUs, option);
        }
    }

    public Bitmap getFrameAtTime(long timeUs) {
        return getFrameAtTime(timeUs, 2);
    }

    public Bitmap getFrameAtTime() {
        return getFrameAtTime(-1, 2);
    }

    public byte[] getEmbeddedPicture() {
        return getEmbeddedPicture(65535);
    }

    protected void finalize() throws Throwable {
        try {
            native_finalize();
        } finally {
            super.finalize();
        }
    }
}
