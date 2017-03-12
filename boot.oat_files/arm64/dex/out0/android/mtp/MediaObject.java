package android.mtp;

import android.net.ProxyInfo;
import android.util.Log;

/* compiled from: MTPJNIInterface */
class MediaObject {
    static final String TAG = "MTPJNIInterface";
    public String album = ProxyInfo.LOCAL_EXCL_LIST;
    public String artist = ProxyInfo.LOCAL_EXCL_LIST;
    public String composer = ProxyInfo.LOCAL_EXCL_LIST;
    public String creationDate = ProxyInfo.LOCAL_EXCL_LIST;
    public String dateTaken = ProxyInfo.LOCAL_EXCL_LIST;
    public String description = ProxyInfo.LOCAL_EXCL_LIST;
    public long duration = 0;
    public String filename = ProxyInfo.LOCAL_EXCL_LIST;
    public String genreName = ProxyInfo.LOCAL_EXCL_LIST;
    public long height = 0;
    public String id = ProxyInfo.LOCAL_EXCL_LIST;
    public String language = ProxyInfo.LOCAL_EXCL_LIST;
    public String latitude = ProxyInfo.LOCAL_EXCL_LIST;
    public String longitude = ProxyInfo.LOCAL_EXCL_LIST;
    public int mimeTypeEnum = 3;
    public String modificationDate = ProxyInfo.LOCAL_EXCL_LIST;
    public String path = ProxyInfo.LOCAL_EXCL_LIST;
    public String size = ProxyInfo.LOCAL_EXCL_LIST;
    public String title = ProxyInfo.LOCAL_EXCL_LIST;
    public long width = 0;
    public int year = 0;

    public MediaObject() {
        Log.e(TAG, "Inside MediaObject Constructor");
    }

    public MediaObject(String filename, String album, String artist, String composer, String creationDate, String description, long duration, String id, String language, String latitude, String longitude, int mimeTypeEnum, String modificationDate, String path, String size, String title, int year, String genreName, long width, long height, String dateTaken) {
        this.filename = filename;
        this.album = album;
        this.artist = artist;
        this.composer = composer;
        this.creationDate = creationDate;
        this.description = description;
        this.duration = duration;
        this.id = id;
        this.language = language;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mimeTypeEnum = mimeTypeEnum;
        this.modificationDate = modificationDate;
        this.path = path;
        this.size = size;
        this.title = title;
        this.year = year;
        this.genreName = genreName;
        this.width = width;
        this.height = height;
        this.dateTaken = dateTaken;
    }
}
