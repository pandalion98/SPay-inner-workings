package android.media;

import android.R;
import android.app.AlarmManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.IContentProvider;
import android.database.Cursor;
import android.drm.DrmManagerClient;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.MediaFile.MediaFileType;
import android.mtp.MtpConstants;
import android.net.ProxyInfo;
import android.net.Uri;
import android.net.Uri.Builder;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Environment;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.Contacts.GroupMembership;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Audio.Playlists;
import android.provider.MediaStore.Audio.Playlists.Members;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Video.VideoColumns;
import android.provider.Settings.System;
import android.sax.ElementListener;
import android.sax.RootElement;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.util.Xml;
import com.samsung.android.privatemode.PrivateModeManager;
import com.samsung.android.telephony.MultiSimManager;
import com.sec.android.app.CscFeature;
import com.sec.android.secvision.sef.SEF;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class MediaScanner {
    private static final String ALARMS_DIR = "/alarms/";
    private static final int DATE_MODIFIED_PLAYLISTS_COLUMN_INDEX = 2;
    private static final int DB_FILES_NUMBER_CACHING_LIMITATION = 7000;
    private static final String DEFAULT_RINGTONE_PROPERTY_PREFIX = "ro.config.";
    private static final boolean ENABLE_BULK_INSERTS = true;
    private static final int FILES_PRESCAN_DATE_MODIFIED_COLUMN_INDEX = 3;
    private static final int FILES_PRESCAN_FORMAT_COLUMN_INDEX = 2;
    private static final int FILES_PRESCAN_ID_COLUMN_INDEX = 0;
    private static final int FILES_PRESCAN_PATH_COLUMN_INDEX = 1;
    private static final String[] FILES_PRESCAN_PROJECTION = new String[]{"_id", "_data", FileColumns.FORMAT, "date_modified"};
    private static final String[] ID3_GENRES = new String[]{"Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "A capella", "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "Britpop", null, "Polsk Punk", "Beat", "Christian Gangsta", "Heavy Metal", "Black Metal", "Crossover", "Contemporary Christian", "Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "JPop", "Synthpop"};
    private static final int ID_PLAYLISTS_COLUMN_INDEX = 0;
    private static final String[] ID_PROJECTION = new String[]{"_id"};
    private static final String[] ID_PROJECTION_COUNT;
    private static final String MUSIC_DIR = "/music/";
    private static final String NOTIFICATIONS_DIR = "/notifications/";
    private static final int PATH_PLAYLISTS_COLUMN_INDEX = 1;
    private static final String[] PLAYLIST_MEMBERS_PROJECTION = new String[]{Members.PLAYLIST_ID};
    private static final String PODCAST_DIR = "/podcasts/";
    private static final String RINGTONES_DIR = "/ringtones/";
    private static final String TAG = "MediaScanner";
    private static String ejectedPath = null;
    private static HashMap<String, String> mMediaPaths = new HashMap();
    private static HashMap<String, String> mNoMediaPaths = new HashMap();
    private static Object syncEjectedPath = new Object();
    private int beforePrescanCount;
    private int contentsCount = 0;
    private Uri mAudioUri;
    private final Options mBitmapOptions = new Options();
    private boolean mCaseInsensitivePaths;
    private final MyMediaScannerClient mClient = new MyMediaScannerClient();
    private Context mContext;
    private String mDefaultAlarmAlertFilename;
    private boolean mDefaultAlarmSet;
    private String mDefaultNotificationFilename;
    private String mDefaultNotificationFilename_2;
    private boolean mDefaultNotificationSet;
    private boolean mDefaultNotificationSet_2;
    private String mDefaultRingtoneFilename;
    private String mDefaultRingtoneFilename_2;
    private boolean mDefaultRingtoneSet;
    private boolean mDefaultRingtoneSet_2;
    private DrmManagerClient mDrmManagerClient = null;
    private final boolean mExternalIsEmulated;
    private String mExternalStoragePath = null;
    private HashMap<String, FileEntry> mFileCache;
    private HashMap<String, FileParsingTime> mFileParsingStat;
    private Uri mFilesUri;
    private Uri mFilesUriNoNotify;
    private Uri mImagesUri;
    private String mInternalStoragePath = null;
    private MediaInserter mMediaInserter;
    private IContentProvider mMediaProvider;
    private int mMtpObjectHandle;
    private long mNativeContext;
    private int mOriginalCount;
    private String mPackageName;
    private ArrayList<FileEntry> mPlayLists;
    private ArrayList<PlaylistEntry> mPlaylistEntries = new ArrayList();
    private Uri mPlaylistsUri;
    private boolean mPrivateStorageIsMounted;
    private String mPrivateStoragePath = null;
    private boolean mProcessGenres;
    private boolean mProcessPlaylists;
    private Uri mThumbsUri;
    private long mTotalBitmapDecodingTime = 0;
    private long mTotalBulkInserterTime = 0;
    private long mTotalCheckedDirectories = 0;
    private long mTotalCheckedFiles = 0;
    private long mTotalDeadThumbnailTime = 0;
    private long mTotalExifExtractingTime = 0;
    private long mTotalInserted = 0;
    private long mTotalMakeEntryTime = 0;
    private long mTotalParsingTime = 0;
    private long mTotalSefExtractingTime = 0;
    private long mTotalUpdated = 0;
    private Uri mVideoUri;
    private boolean mWasEmptyPriorToScan = false;
    private boolean useHashMap = false;

    private static class FileEntry {
        long mLastModified;
        boolean mLastModifiedChanged = false;
        String mPath;
        long mRowId;

        FileEntry(long rowId, String path, long lastModified) {
            this.mRowId = rowId;
            this.mPath = path;
            this.mLastModified = lastModified;
        }

        public String toString() {
            return this.mPath + " mRowId: " + this.mRowId;
        }
    }

    private static class FileParsingTime {
        String mExtention;
        long mNumber = 1;
        long mTotalTime;

        FileParsingTime(String extention, long parsingTime) {
            this.mTotalTime = parsingTime;
            this.mExtention = extention;
        }

        public void addParsingTime(long parsingTime) {
            this.mNumber++;
            this.mTotalTime += parsingTime;
        }

        public String toString() {
            long averageTime = 0;
            if (this.mNumber != 0) {
                averageTime = this.mTotalTime / this.mNumber;
            }
            return this.mExtention + "_" + this.mNumber + "_" + this.mTotalTime + "_" + averageTime + " ";
        }
    }

    static class MediaBulkDeleter {
        int deletedNumber;
        final Uri mBaseUri;
        final String mPackageName;
        final IContentProvider mProvider;
        String path;
        ArrayList<String> whereArgs = new ArrayList(100);
        StringBuilder whereClause = new StringBuilder();

        public MediaBulkDeleter(IContentProvider provider, String packageName, Uri baseUri, String path) {
            this.mProvider = provider;
            this.mPackageName = packageName;
            this.mBaseUri = baseUri;
            this.deletedNumber = 0;
            this.path = path;
        }

        public int getTotalDeletedNumber() {
            return this.deletedNumber;
        }

        public void delete(long id) throws RemoteException {
            if (this.whereClause.length() != 0) {
                this.whereClause.append(",");
            }
            this.whereClause.append("?");
            this.whereArgs.add(ProxyInfo.LOCAL_EXCL_LIST + id);
            if (this.whereArgs.size() > 100) {
                flush();
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void flush() throws android.os.RemoteException {
            /*
            r8 = this;
            r4 = android.media.MediaScanner.syncEjectedPath;
            monitor-enter(r4);
            r3 = android.media.MediaScanner.ejectedPath;	 Catch:{ all -> 0x00ba }
            if (r3 == 0) goto L_0x0038;
        L_0x000b:
            r3 = android.media.MediaScanner.ejectedPath;	 Catch:{ all -> 0x00ba }
            r5 = r8.path;	 Catch:{ all -> 0x00ba }
            r3 = r3.startsWith(r5);	 Catch:{ all -> 0x00ba }
            if (r3 == 0) goto L_0x0038;
        L_0x0017:
            r3 = "MediaScanner";
            r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ba }
            r5.<init>();	 Catch:{ all -> 0x00ba }
            r6 = "BulkDeleter detects ejection for path : ";
            r5 = r5.append(r6);	 Catch:{ all -> 0x00ba }
            r6 = r8.path;	 Catch:{ all -> 0x00ba }
            r5 = r5.append(r6);	 Catch:{ all -> 0x00ba }
            r5 = r5.toString();	 Catch:{ all -> 0x00ba }
            android.util.Log.e(r3, r5);	 Catch:{ all -> 0x00ba }
            r3 = r8.whereArgs;	 Catch:{ all -> 0x00ba }
            r3.clear();	 Catch:{ all -> 0x00ba }
            monitor-exit(r4);	 Catch:{ all -> 0x00ba }
        L_0x0037:
            return;
        L_0x0038:
            monitor-exit(r4);	 Catch:{ all -> 0x00ba }
            r3 = r8.whereArgs;
            r2 = r3.size();
            if (r2 <= 0) goto L_0x0037;
        L_0x0041:
            r0 = new java.lang.String[r2];
            r3 = r8.whereArgs;
            r0 = r3.toArray(r0);
            r0 = (java.lang.String[]) r0;
            r3 = r8.mProvider;
            r4 = r8.mPackageName;
            r5 = r8.mBaseUri;
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r7 = "_id IN (";
            r6 = r6.append(r7);
            r7 = r8.whereClause;
            r7 = r7.toString();
            r6 = r6.append(r7);
            r7 = ")";
            r6 = r6.append(r7);
            r6 = r6.toString();
            r1 = r3.delete(r4, r5, r6, r0);
            r3 = r8.deletedNumber;
            r3 = r3 + r1;
            r8.deletedNumber = r3;
            r3 = "MediaScanner";
            r4 = new java.lang.StringBuilder;
            r4.<init>();
            r5 = "BulkDeleter flush number : ";
            r4 = r4.append(r5);
            r4 = r4.append(r1);
            r4 = r4.toString();
            android.util.Slog.d(r3, r4);
            r3 = "MediaScanner";
            r4 = new java.lang.StringBuilder;
            r4.<init>();
            r5 = "BulkDeleter ID-info : ";
            r4 = r4.append(r5);
            r5 = java.util.Arrays.toString(r0);
            r4 = r4.append(r5);
            r4 = r4.toString();
            android.util.Slog.d(r3, r4);
            r3 = r8.whereClause;
            r4 = 0;
            r3.setLength(r4);
            r3 = r8.whereArgs;
            r3.clear();
            goto L_0x0037;
        L_0x00ba:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x00ba }
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.MediaBulkDeleter.flush():void");
        }
    }

    private class MyMediaScannerClient implements MediaScannerClient {
        private String m3dvideotype;
        private String mAlbum;
        private String mAlbumArtist;
        private String mArtist;
        private String mAudioCodecInfo;
        private int mBitDepth;
        private long mCityID;
        private int mCompilation;
        private String mComposer;
        private long mCreationTime;
        private int mDuration;
        private long mFileSize;
        private int mFileType;
        private int mFormat;
        private String mGenre;
        private int mHeight;
        private int mIs360video;
        private boolean mIsDrm;
        private long mLastModified;
        private String mMimeType;
        private boolean mNoMedia;
        private String mPath;
        private int mPrivateStorage;
        private int mRecordingMode;
        private int mRecordingType;
        private int mSamplingRate;
        private String mTitle;
        private int mTrack;
        private String mVideoCodecInfo;
        private float mVideoLatitude;
        private float mVideoLongitude;
        private float mVoiceLatitude;
        private float mVoiceLongitude;
        private int mWeatherID;
        private int mWidth;
        private String mWriter;
        private int mYear;

        private MyMediaScannerClient() {
            this.mFormat = 0;
            this.mRecordingType = -1;
            this.mRecordingMode = 0;
            this.mWeatherID = 0;
            this.mCityID = 0;
            this.mVideoLatitude = Float.NEGATIVE_INFINITY;
            this.mVideoLongitude = Float.NEGATIVE_INFINITY;
            this.mVoiceLatitude = Float.NEGATIVE_INFINITY;
            this.mVoiceLongitude = Float.NEGATIVE_INFINITY;
            this.mBitDepth = 0;
            this.mSamplingRate = 0;
            this.mIs360video = 0;
            this.mPrivateStorage = 0;
            this.mCreationTime = -1;
        }

        public FileEntry beginFile(String path, String mimeType, long lastModified, long fileSize, boolean isDirectory, boolean noMedia) {
            FileEntry entry;
            this.mMimeType = mimeType;
            this.mFileType = 0;
            this.mFileSize = fileSize;
            this.mIsDrm = false;
            if (!isDirectory) {
                if (!noMedia && MediaScanner.isNoMediaFile(path)) {
                    noMedia = true;
                }
                this.mNoMedia = noMedia;
                if (mimeType != null) {
                    this.mFileType = MediaFile.getFileTypeForMimeType(mimeType);
                }
                if (this.mFileType == 0) {
                    MediaFileType mediaFileType = MediaFile.getFileType(path);
                    if (mediaFileType != null) {
                        this.mFileType = mediaFileType.fileType;
                        if (this.mMimeType == null) {
                            this.mMimeType = mediaFileType.mimeType;
                        }
                    }
                }
                if (MediaScanner.this.isDrmEnabled() && MediaFile.isDrmFileType(this.mFileType)) {
                    this.mFileType = getFileTypeFromDrm(path);
                }
            }
            long beforeMakeEntryTime = System.currentTimeMillis();
            if (!MediaScanner.this.useHashMap || this.mMimeType == null || this.mMimeType.equals("audio/mp4") || this.mMimeType.equals("audio/amr")) {
                entry = MediaScanner.this.makeEntryFor(path);
            } else {
                String key = path;
                if (MediaScanner.this.mCaseInsensitivePaths) {
                    key = path.toLowerCase();
                }
                entry = (FileEntry) MediaScanner.this.mFileCache.get(key);
            }
            MediaScanner.access$614(MediaScanner.this, System.currentTimeMillis() - beforeMakeEntryTime);
            long delta = entry != null ? lastModified - entry.mLastModified : 0;
            boolean wasModified = delta > 1 || delta < -1;
            if (entry == null || wasModified) {
                if (wasModified) {
                    entry.mLastModified = lastModified;
                } else {
                    entry = new FileEntry(0, path, lastModified);
                    this.mFormat = isDirectory ? 12289 : 0;
                }
                entry.mLastModifiedChanged = true;
            }
            if (MediaScanner.this.mProcessPlaylists && MediaFile.isPlayListFileType(this.mFileType)) {
                MediaScanner.this.mPlayLists.add(entry);
                return null;
            }
            this.mArtist = null;
            this.mAlbumArtist = null;
            this.mAlbum = null;
            this.mTitle = null;
            this.mComposer = null;
            this.mGenre = null;
            this.mTrack = 0;
            this.mYear = 0;
            this.mDuration = 0;
            this.mPath = path;
            this.mLastModified = lastModified;
            this.mWriter = null;
            this.mCompilation = 0;
            this.mWidth = 0;
            this.mHeight = 0;
            this.mVideoLatitude = Float.NEGATIVE_INFINITY;
            this.mVideoLongitude = Float.NEGATIVE_INFINITY;
            this.mVoiceLatitude = Float.NEGATIVE_INFINITY;
            this.mVoiceLongitude = Float.NEGATIVE_INFINITY;
            this.mSamplingRate = 0;
            this.mBitDepth = 0;
            this.mRecordingType = -1;
            this.mRecordingMode = 0;
            this.mWeatherID = 0;
            this.mCityID = 0;
            this.mIs360video = 0;
            this.mPrivateStorage = 0;
            this.m3dvideotype = null;
            this.mVideoCodecInfo = null;
            this.mAudioCodecInfo = null;
            this.mCreationTime = -1;
            return entry;
        }

        public void scanFile(String path, long lastModified, long fileSize, boolean isDirectory, boolean noMedia) {
            if (isDirectory) {
                MediaScanner.this.mTotalCheckedDirectories = 1 + MediaScanner.this.mTotalCheckedDirectories;
            } else {
                MediaScanner.this.mTotalCheckedFiles = 1 + MediaScanner.this.mTotalCheckedFiles;
            }
            doScanFile(path, null, lastModified, fileSize, isDirectory, false, noMedia);
        }

        public void clearBuffer() {
            if (MediaScanner.this.mMediaInserter != null) {
                MediaScanner.this.mMediaInserter.clear();
            }
        }

        public Uri doScanFile(String path, String mimeType, long lastModified, long fileSize, boolean isDirectory, boolean scanAlways, boolean noMedia) {
            try {
                FileEntry entry = beginFile(path, mimeType, lastModified, fileSize, isDirectory, noMedia);
                if (MediaScanner.this.mMtpObjectHandle != 0) {
                    entry.mRowId = 0;
                }
                if (entry == null) {
                    return null;
                }
                if (!entry.mLastModifiedChanged && !scanAlways) {
                    return null;
                }
                if (noMedia) {
                    return endFile(entry, false, false, false, false, false, false);
                }
                boolean dcfRingtones;
                boolean notifications;
                boolean alarms;
                boolean podcasts;
                boolean music;
                long beforeMetaParsingTime;
                long metaParsingTime;
                boolean isaudio;
                boolean isvideo;
                boolean isimage;
                int lastDot;
                String extention;
                FileParsingTime parsingTime;
                String lowpath = path.toLowerCase(Locale.ROOT);
                boolean ringtones = lowpath.indexOf(MediaScanner.RINGTONES_DIR) > 0;
                if (CscFeature.getInstance().getEnableStatus("CscFeature_Common_EnableSprintExtension")) {
                    if (lowpath.indexOf("/download/") > 0) {
                        if (lowpath.endsWith(".dcf")) {
                            dcfRingtones = true;
                            notifications = lowpath.indexOf(MediaScanner.NOTIFICATIONS_DIR) <= 0;
                            alarms = lowpath.indexOf(MediaScanner.ALARMS_DIR) <= 0;
                            podcasts = lowpath.indexOf(MediaScanner.PODCAST_DIR) <= 0;
                            music = lowpath.indexOf(MediaScanner.MUSIC_DIR) <= 0 || !(ringtones || notifications || alarms || podcasts);
                            beforeMetaParsingTime = 0;
                            metaParsingTime = 0;
                            isaudio = MediaFile.isAudioFileType(this.mFileType);
                            isvideo = MediaFile.isVideoFileType(this.mFileType);
                            isimage = MediaFile.isImageFileType(this.mFileType);
                            if (isaudio || isvideo || isimage) {
                                path = Environment.maybeTranslateEmulatedPathToInternal(new File(path)).getAbsolutePath();
                            }
                            if (isaudio || isvideo) {
                                beforeMetaParsingTime = System.currentTimeMillis();
                                MediaScanner.this.processFile(path, mimeType, this);
                                metaParsingTime = System.currentTimeMillis() - beforeMetaParsingTime;
                                MediaScanner.access$1414(MediaScanner.this, metaParsingTime);
                            }
                            if (isimage && this.mFileType != 31) {
                                beforeMetaParsingTime = System.currentTimeMillis();
                                processImageFile(path);
                                metaParsingTime = System.currentTimeMillis() - beforeMetaParsingTime;
                                MediaScanner.access$1514(MediaScanner.this, metaParsingTime);
                            }
                            lastDot = path.lastIndexOf(".");
                            if (!(lastDot <= 0 || beforeMetaParsingTime == 0 || metaParsingTime == 0)) {
                                extention = path.substring(lastDot + 1).toUpperCase();
                                parsingTime = (FileParsingTime) MediaScanner.this.mFileParsingStat.get(extention);
                                if (parsingTime == null) {
                                    parsingTime.addParsingTime(metaParsingTime);
                                    MediaScanner.this.mFileParsingStat.put(extention, parsingTime);
                                } else {
                                    MediaScanner.this.mFileParsingStat.put(extention, new FileParsingTime(extention, metaParsingTime));
                                }
                            }
                            return endFile(entry, ringtones, dcfRingtones, notifications, alarms, music, podcasts);
                        }
                    }
                }
                dcfRingtones = false;
                if (lowpath.indexOf(MediaScanner.NOTIFICATIONS_DIR) <= 0) {
                }
                if (lowpath.indexOf(MediaScanner.ALARMS_DIR) <= 0) {
                }
                if (lowpath.indexOf(MediaScanner.PODCAST_DIR) <= 0) {
                }
                if (lowpath.indexOf(MediaScanner.MUSIC_DIR) <= 0) {
                }
                beforeMetaParsingTime = 0;
                metaParsingTime = 0;
                isaudio = MediaFile.isAudioFileType(this.mFileType);
                isvideo = MediaFile.isVideoFileType(this.mFileType);
                isimage = MediaFile.isImageFileType(this.mFileType);
                path = Environment.maybeTranslateEmulatedPathToInternal(new File(path)).getAbsolutePath();
                beforeMetaParsingTime = System.currentTimeMillis();
                MediaScanner.this.processFile(path, mimeType, this);
                metaParsingTime = System.currentTimeMillis() - beforeMetaParsingTime;
                MediaScanner.access$1414(MediaScanner.this, metaParsingTime);
                beforeMetaParsingTime = System.currentTimeMillis();
                processImageFile(path);
                metaParsingTime = System.currentTimeMillis() - beforeMetaParsingTime;
                MediaScanner.access$1514(MediaScanner.this, metaParsingTime);
                lastDot = path.lastIndexOf(".");
                extention = path.substring(lastDot + 1).toUpperCase();
                parsingTime = (FileParsingTime) MediaScanner.this.mFileParsingStat.get(extention);
                if (parsingTime == null) {
                    MediaScanner.this.mFileParsingStat.put(extention, new FileParsingTime(extention, metaParsingTime));
                } else {
                    parsingTime.addParsingTime(metaParsingTime);
                    MediaScanner.this.mFileParsingStat.put(extention, parsingTime);
                }
                return endFile(entry, ringtones, dcfRingtones, notifications, alarms, music, podcasts);
            } catch (Throwable e) {
                Log.e(MediaScanner.TAG, "RemoteException in MediaScanner.scanFile()", e);
                return null;
            }
        }

        private int parseSubstring(String s, int start, int defaultValue) {
            int length = s.length();
            if (start == length) {
                return defaultValue;
            }
            int start2 = start + 1;
            char ch = s.charAt(start);
            if (ch < '0' || ch > '9') {
                start = start2;
                return defaultValue;
            }
            int result = ch - 48;
            while (start2 < length) {
                start = start2 + 1;
                ch = s.charAt(start2);
                if (ch < '0' || ch > '9') {
                    return result;
                }
                result = (result * 10) + (ch - 48);
                start2 = start;
            }
            start = start2;
            return result;
        }

        public void handleStringTag(String name, String value) {
            boolean z = true;
            if (name.equalsIgnoreCase("title") || name.startsWith("title;")) {
                this.mTitle = value;
            } else if (name.equalsIgnoreCase("artist") || name.startsWith("artist;")) {
                this.mArtist = value.trim();
            } else if (name.equalsIgnoreCase("albumartist") || name.startsWith("albumartist;") || name.equalsIgnoreCase("band") || name.startsWith("band;")) {
                this.mAlbumArtist = value.trim();
            } else if (name.equalsIgnoreCase("album") || name.startsWith("album;")) {
                this.mAlbum = value.trim();
            } else if (name.equalsIgnoreCase(AudioColumns.COMPOSER) || name.startsWith("composer;")) {
                this.mComposer = value.trim();
            } else if (MediaScanner.this.mProcessGenres && (name.equalsIgnoreCase(AudioColumns.GENRE) || name.startsWith("genre;"))) {
                this.mGenre = getGenreName(value);
            } else if (name.equalsIgnoreCase("year") || name.startsWith("year;")) {
                this.mYear = parseSubstring(value, 0, 0);
            } else if (name.equalsIgnoreCase("tracknumber") || name.startsWith("tracknumber;")) {
                this.mTrack = ((this.mTrack / 1000) * 1000) + parseSubstring(value, 0, 0);
            } else if (name.equalsIgnoreCase("discnumber") || name.equals("set") || name.startsWith("set;")) {
                this.mTrack = (parseSubstring(value, 0, 0) * 1000) + (this.mTrack % 1000);
            } else if (name.equalsIgnoreCase("duration")) {
                this.mDuration = parseSubstring(value, 0, 0);
            } else if (name.equalsIgnoreCase("writer") || name.startsWith("writer;")) {
                this.mWriter = value.trim();
            } else if (name.equalsIgnoreCase(AudioColumns.COMPILATION)) {
                this.mCompilation = parseSubstring(value, 0, 0);
            } else if (name.equalsIgnoreCase("isdrm")) {
                if (parseSubstring(value, 0, 0) != 1) {
                    z = false;
                }
                this.mIsDrm = z;
            } else if (name.equalsIgnoreCase("width")) {
                this.mWidth = parseSubstring(value, 0, 0);
            } else if (name.equalsIgnoreCase("height")) {
                this.mHeight = parseSubstring(value, 0, 0);
            } else if (name.equalsIgnoreCase("recordingtype")) {
                this.mRecordingType = Integer.parseInt(value);
            } else if (name.equalsIgnoreCase("recordingmode")) {
                this.mRecordingMode = Integer.parseInt(value);
            } else if (name.equalsIgnoreCase("weather")) {
                this.mWeatherID = Integer.parseInt(value);
            } else if (name.equalsIgnoreCase("cityid")) {
                this.mCityID = Long.parseLong(value);
            } else if (name.equalsIgnoreCase("location")) {
                int index = value.lastIndexOf(45);
                if (index == -1) {
                    index = value.lastIndexOf(43);
                }
                this.mVideoLatitude = Float.parseFloat(value.substring(0, index - 1));
                this.mVideoLongitude = Float.parseFloat(value.substring(index));
                this.mVoiceLatitude = this.mVideoLatitude;
                this.mVoiceLongitude = this.mVideoLongitude;
            } else if (name.equalsIgnoreCase("samplingrate")) {
                this.mSamplingRate = Integer.parseInt(value);
            } else if (name.equalsIgnoreCase("bitspersample")) {
                this.mBitDepth = Integer.parseInt(value);
            } else if (name.equalsIgnoreCase("is360video")) {
                this.mIs360video = Integer.parseInt(value);
            } else if (name.equalsIgnoreCase("3dvideotype")) {
                this.m3dvideotype = value.trim();
            } else if (name.equalsIgnoreCase("videocodecinfo")) {
                this.mVideoCodecInfo = value.trim();
            } else if (name.equalsIgnoreCase("audiocodecinfo")) {
                this.mAudioCodecInfo = value.trim();
            } else if (name.equalsIgnoreCase("creationtime") && value != null) {
                this.mCreationTime = Long.parseLong(value);
            }
        }

        private boolean convertGenreCode(String input, String expected) {
            String output = getGenreName(input);
            if (output.equals(expected)) {
                return true;
            }
            Log.d(MediaScanner.TAG, "'" + input + "' -> '" + output + "', expected '" + expected + "'");
            return false;
        }

        private void testGenreNameConverter() {
            convertGenreCode("2", "Country");
            convertGenreCode("(2)", "Country");
            convertGenreCode("(2", "(2");
            convertGenreCode("2 Foo", "Country");
            convertGenreCode("(2) Foo", "Country");
            convertGenreCode("(2 Foo", "(2 Foo");
            convertGenreCode("2Foo", "2Foo");
            convertGenreCode("(2)Foo", "Country");
            convertGenreCode("200 Foo", "Foo");
            convertGenreCode("(200) Foo", "Foo");
            convertGenreCode("200Foo", "200Foo");
            convertGenreCode("(200)Foo", "Foo");
            convertGenreCode("200)Foo", "200)Foo");
            convertGenreCode("200) Foo", "200) Foo");
        }

        public String getGenreName(String genreTagValue) {
            if (genreTagValue == null) {
                return null;
            }
            int length = genreTagValue.length();
            if (length > 0) {
                boolean parenthesized = false;
                StringBuffer number = new StringBuffer();
                int i = 0;
                while (i < length) {
                    char c = genreTagValue.charAt(i);
                    if (i != 0 || c != '(') {
                        if (!Character.isDigit(c)) {
                            break;
                        }
                        number.append(c);
                    } else {
                        parenthesized = true;
                    }
                    i++;
                }
                char charAfterNumber = i < length ? genreTagValue.charAt(i) : ' ';
                if ((parenthesized && charAfterNumber == ')') || (!parenthesized && Character.isWhitespace(charAfterNumber))) {
                    try {
                        short genreIndex = Short.parseShort(number.toString());
                        if (genreIndex >= (short) 0) {
                            if (genreIndex < MediaScanner.ID3_GENRES.length && MediaScanner.ID3_GENRES[genreIndex] != null) {
                                return MediaScanner.ID3_GENRES[genreIndex];
                            }
                            if (genreIndex == (short) 255) {
                                return null;
                            }
                            if (genreIndex >= (short) 255 || i + 1 >= length) {
                                return number.toString();
                            }
                            if (parenthesized && charAfterNumber == ')') {
                                i++;
                            }
                            String ret = genreTagValue.substring(i).trim();
                            if (ret.length() != 0) {
                                return ret;
                            }
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
            return genreTagValue;
        }

        private void processImageFile(String path) {
            try {
                MediaScanner.this.mBitmapOptions.outWidth = 0;
                MediaScanner.this.mBitmapOptions.outHeight = 0;
                BitmapFactory.decodeFile(path, MediaScanner.this.mBitmapOptions);
                this.mWidth = MediaScanner.this.mBitmapOptions.outWidth;
                this.mHeight = MediaScanner.this.mBitmapOptions.outHeight;
            } catch (Throwable th) {
            }
        }

        public void setMimeType(String mimeType) {
            if (!"audio/mp4".equals(this.mMimeType) || !mimeType.startsWith("video")) {
                this.mMimeType = mimeType;
                this.mFileType = MediaFile.getFileTypeForMimeType(mimeType);
            }
        }

        private ContentValues toValues() {
            ContentValues map = new ContentValues();
            map.put("_data", this.mPath);
            map.put("title", this.mTitle);
            map.put("date_modified", Long.valueOf(this.mLastModified));
            map.put("_size", Long.valueOf(this.mFileSize));
            map.put("mime_type", this.mMimeType);
            map.put(MediaColumns.IS_DRM, Boolean.valueOf(this.mIsDrm));
            String resolution = null;
            if (this.mWidth > 0 && this.mHeight > 0) {
                map.put("width", Integer.valueOf(this.mWidth));
                map.put("height", Integer.valueOf(this.mHeight));
                resolution = this.mWidth + "x" + this.mHeight;
            }
            if (!this.mNoMedia) {
                String str;
                String str2;
                if (MediaFile.isVideoFileType(this.mFileType)) {
                    str = "artist";
                    str2 = (this.mArtist == null || this.mArtist.length() <= 0) ? MediaStore.UNKNOWN_STRING : this.mArtist;
                    map.put(str, str2);
                    str = "album";
                    str2 = (this.mAlbum == null || this.mAlbum.length() <= 0) ? MediaStore.UNKNOWN_STRING : this.mAlbum;
                    map.put(str, str2);
                    map.put("duration", Integer.valueOf(this.mDuration));
                    if (resolution != null) {
                        map.put(VideoColumns.RESOLUTION, resolution);
                    }
                    map.put("weather_ID", Integer.valueOf(this.mWeatherID));
                    map.put("city_ID", Long.valueOf(this.mCityID));
                    if (this.mVideoLatitude != Float.NEGATIVE_INFINITY) {
                        map.put("latitude", Float.valueOf(this.mVideoLatitude));
                        map.put("longitude", Float.valueOf(this.mVideoLongitude));
                    }
                    map.put("is_360_video", Integer.valueOf(this.mIs360video));
                    map.put("recordingtype", Integer.valueOf(this.mRecordingType));
                    map.put("recording_mode", Integer.valueOf(this.mRecordingMode));
                    if (this.m3dvideotype != null) {
                        map.put("type3dvideo", this.m3dvideotype);
                    }
                    if (this.mVideoCodecInfo != null) {
                        map.put("video_codec_info", this.mVideoCodecInfo);
                    }
                    if (this.mAudioCodecInfo != null) {
                        map.put("audio_codec_info", this.mAudioCodecInfo);
                    }
                    if (this.mCreationTime != -1) {
                        map.put("datetaken", Long.valueOf(this.mCreationTime));
                    }
                } else if (!MediaFile.isImageFileType(this.mFileType) && MediaFile.isAudioFileType(this.mFileType)) {
                    str = "artist";
                    str2 = (this.mArtist == null || this.mArtist.length() <= 0) ? MediaStore.UNKNOWN_STRING : this.mArtist;
                    map.put(str, str2);
                    str = AudioColumns.ALBUM_ARTIST;
                    str2 = (this.mAlbumArtist == null || this.mAlbumArtist.length() <= 0) ? null : this.mAlbumArtist;
                    map.put(str, str2);
                    str = "album";
                    str2 = (this.mAlbum == null || this.mAlbum.length() <= 0) ? MediaStore.UNKNOWN_STRING : this.mAlbum;
                    map.put(str, str2);
                    map.put(AudioColumns.COMPOSER, this.mComposer);
                    map.put(AudioColumns.GENRE, this.mGenre);
                    if (this.mYear != 0) {
                        map.put("year", Integer.valueOf(this.mYear));
                    }
                    map.put(AudioColumns.TRACK, Integer.valueOf(this.mTrack));
                    map.put("duration", Integer.valueOf(this.mDuration));
                    map.put(AudioColumns.COMPILATION, Integer.valueOf(this.mCompilation));
                    map.put("recordingtype", Integer.valueOf(this.mRecordingType));
                    map.put("recording_mode", Integer.valueOf(this.mRecordingMode));
                    map.put("bit_depth", Integer.valueOf(this.mBitDepth));
                    map.put("sampling_rate", Integer.valueOf(this.mSamplingRate));
                    if (this.mVoiceLatitude != Float.NEGATIVE_INFINITY) {
                        map.put("latitude", Float.valueOf(this.mVoiceLatitude));
                        map.put("longitude", Float.valueOf(this.mVoiceLongitude));
                    }
                    if (this.mCreationTime != -1) {
                        map.put("datetaken", Long.valueOf(this.mCreationTime));
                    }
                }
            }
            if (MediaScanner.this.mPrivateStorageIsMounted && MediaScanner.this.mPrivateStoragePath != null && this.mPath.startsWith(MediaScanner.this.mPrivateStoragePath)) {
                this.mPrivateStorage = 1;
            }
            map.put("is_secretbox", Integer.valueOf(this.mPrivateStorage));
            return map;
        }

        private void computeDisplayName(String data, ContentValues values) {
            String s = data == null ? ProxyInfo.LOCAL_EXCL_LIST : data.toString();
            int idx = s.lastIndexOf(47);
            if (idx >= 0) {
                s = s.substring(idx + 1);
            }
            values.put("_display_name", s);
        }

        private Uri endFile(FileEntry entry, boolean ringtones, boolean dcfRingtones, boolean notifications, boolean alarms, boolean music, boolean podcasts) throws RemoteException {
            int i;
            if (this.mArtist == null || this.mArtist.length() == 0) {
                this.mArtist = this.mAlbumArtist;
            }
            ContentValues values = toValues();
            String title = values.getAsString("title");
            if (title == null || TextUtils.isEmpty(title.trim())) {
                values.put("title", MediaFile.getFileTitle(values.getAsString("_data")));
            }
            if (MediaStore.UNKNOWN_STRING.equals(values.getAsString("album"))) {
                String album = values.getAsString("_data");
                int lastSlash = album.lastIndexOf(47);
                if (lastSlash >= 0) {
                    int previousSlash = 0;
                    while (true) {
                        int idx = album.indexOf(47, previousSlash + 1);
                        if (idx >= 0 && idx < lastSlash) {
                            previousSlash = idx;
                        } else if (previousSlash != 0) {
                            values.put("album", album.substring(previousSlash + 1, lastSlash));
                        }
                    }
                    if (previousSlash != 0) {
                        values.put("album", album.substring(previousSlash + 1, lastSlash));
                    }
                }
            }
            long rowId = entry.mRowId;
            long exifGpsDateTime = -1;
            if (MediaFile.isAudioFileType(this.mFileType) && (rowId == 0 || MediaScanner.this.mMtpObjectHandle != 0)) {
                String str = AudioColumns.IS_RINGTONE;
                boolean z = ringtones || dcfRingtones;
                values.put(str, Boolean.valueOf(z));
                values.put(AudioColumns.IS_NOTIFICATION, Boolean.valueOf(notifications));
                values.put(AudioColumns.IS_ALARM, Boolean.valueOf(alarms));
                Integer recordingtype = values.getAsInteger("recordingtype");
                if (recordingtype == null || recordingtype.intValue() <= 0) {
                    values.put(AudioColumns.IS_MUSIC, Boolean.valueOf(music));
                } else {
                    values.put(AudioColumns.IS_MUSIC, Integer.valueOf(0));
                }
                values.put(AudioColumns.IS_PODCAST, Boolean.valueOf(podcasts));
            } else if (this.mFileType == 31 && !this.mNoMedia) {
                int width = -1;
                int height = -1;
                long beforeExifExtractingTime = System.currentTimeMillis();
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(entry.mPath);
                } catch (IOException e) {
                }
                if (exif != null) {
                    float[] latlng = new float[2];
                    if (exif.getLatLong(latlng)) {
                        values.put("latitude", Float.valueOf(latlng[0]));
                        values.put("longitude", Float.valueOf(latlng[1]));
                    }
                    long time = exif.getGpsDateTime();
                    if (time != -1) {
                        exifGpsDateTime = time;
                        values.put("datetaken", Long.valueOf(time));
                    } else {
                        time = exif.getDateTime();
                        if (time != -1 && Math.abs((this.mLastModified * 1000) - time) >= AlarmManager.INTERVAL_DAY) {
                            values.put("datetaken", Long.valueOf(time));
                        }
                    }
                    width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1);
                    height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
                    if (orientation != -1) {
                        int degree;
                        switch (orientation) {
                            case 3:
                                degree = 180;
                                break;
                            case 6:
                                degree = 90;
                                break;
                            case 8:
                                degree = 270;
                                break;
                            default:
                                degree = 0;
                                break;
                        }
                        values.put(ImageColumns.ORIENTATION, Integer.valueOf(degree));
                    }
                }
                if (width == -1 || height == -1) {
                    processImageFile(entry.mPath);
                } else {
                    this.mWidth = width;
                    this.mHeight = height;
                }
                if (this.mWidth > 0 && this.mHeight > 0) {
                    values.put("width", Integer.valueOf(this.mWidth));
                    values.put("height", Integer.valueOf(this.mHeight));
                }
                long exifExtractingTime = System.currentTimeMillis() - beforeExifExtractingTime;
                MediaScanner.access$2214(MediaScanner.this, exifExtractingTime);
                int lastDot = entry.mPath.lastIndexOf(".");
                if (!(lastDot <= 0 || beforeExifExtractingTime == 0 || exifExtractingTime == 0)) {
                    String extention = entry.mPath.substring(lastDot + 1).toUpperCase();
                    FileParsingTime parsingTime = (FileParsingTime) MediaScanner.this.mFileParsingStat.get(extention);
                    if (parsingTime != null) {
                        parsingTime.addParsingTime(exifExtractingTime);
                        MediaScanner.this.mFileParsingStat.put(extention, parsingTime);
                    } else {
                        MediaScanner.this.mFileParsingStat.put(extention, new FileParsingTime(extention, exifExtractingTime));
                    }
                }
            }
            long start = System.currentTimeMillis();
            if ((this.mFileType == 31 || this.mFileType == 32) && SEF.isSEFFile(entry.mPath)) {
                int[] SEFDataTypes = SEF.listSEFDataTypes(entry.mPath);
                i = SEFDataTypes.length - 1;
                while (i > -1) {
                    if (SEFDataTypes[i] <= 2047 || SEFDataTypes[i] % 16 != 0) {
                        i--;
                    } else {
                        values.put("sef_file_type", Integer.valueOf(SEFDataTypes[i]));
                        Log.d(MediaScanner.TAG, "sef_file_type set to " + SEFDataTypes[i]);
                        if (SEFDataTypes.length > 2) {
                            int dataSubType = 0;
                            for (int j = 0; j < SEFDataTypes.length; j++) {
                                int diffOfDataType = SEFDataTypes[j] - SEFDataTypes[i];
                                if (i != j && diffOfDataType < 16 && diffOfDataType > 0) {
                                    dataSubType |= 1 << (diffOfDataType - 1);
                                }
                            }
                            values.put("sef_file_sub_type", Integer.valueOf(dataSubType));
                        }
                    }
                }
                try {
                    byte[] data;
                    File file = new File(entry.mPath);
                    if (exifGpsDateTime == -1) {
                        if (SEF.hasSEFData(file, "Image_UTC_Data")) {
                            data = SEF.getSEFData(file, "Image_UTC_Data");
                            if (data != null) {
                                values.put("datetaken", Long.valueOf(Long.parseLong(new String(data))));
                            }
                        }
                    }
                    if (SEF.hasSEFData(file, "Burst_Shot_Info")) {
                        data = SEF.getSEFData(file, "Burst_Shot_Info");
                        if (data != null) {
                            values.put(GroupMembership.GROUP_ID, Integer.valueOf(Integer.parseInt(new String(data))));
                        }
                    }
                } catch (Throwable e2) {
                    Log.e(MediaScanner.TAG, "IOException in MediaScanner.endFile()", e2);
                }
            }
            MediaScanner.access$2314(MediaScanner.this, System.currentTimeMillis() - start);
            Uri tableUri = MediaScanner.this.mFilesUri;
            MediaInserter inserter = MediaScanner.this.mMediaInserter;
            if (!this.mNoMedia) {
                if (MediaFile.isVideoFileType(this.mFileType)) {
                    tableUri = MediaScanner.this.mVideoUri;
                } else if (MediaFile.isImageFileType(this.mFileType)) {
                    tableUri = MediaScanner.this.mImagesUri;
                } else if (MediaFile.isAudioFileType(this.mFileType)) {
                    tableUri = MediaScanner.this.mAudioUri;
                }
            }
            Uri result = null;
            boolean needToSetSettings = false;
            boolean multiSimSettings = false;
            if (rowId == 0) {
                if (MediaScanner.this.mMtpObjectHandle != 0) {
                    values.put(MediaColumns.MEDIA_SCANNER_NEW_OBJECT_ID, Integer.valueOf(MediaScanner.this.mMtpObjectHandle));
                }
                if (tableUri == MediaScanner.this.mFilesUri) {
                    int format = this.mFormat;
                    if (format == 0) {
                        format = MediaFile.getFormatCode(entry.mPath, this.mMimeType);
                    }
                    values.put(FileColumns.FORMAT, Integer.valueOf(format));
                }
                if (MediaScanner.this.mWasEmptyPriorToScan) {
                    if (notifications) {
                        if (!MediaScanner.this.mDefaultNotificationSet && (TextUtils.isEmpty(MediaScanner.this.mDefaultNotificationFilename) || doesPathHaveFilename(entry.mPath, MediaScanner.this.mDefaultNotificationFilename))) {
                            needToSetSettings = true;
                        }
                        if (MultiSimManager.getSimSlotCount() > 1 && !MediaScanner.this.mDefaultNotificationSet_2 && (TextUtils.isEmpty(MediaScanner.this.mDefaultNotificationFilename_2) || doesPathHaveFilename(entry.mPath, MediaScanner.this.mDefaultNotificationFilename_2))) {
                            needToSetSettings = true;
                            multiSimSettings = true;
                        }
                    } else if (ringtones) {
                        if (!MediaScanner.this.mDefaultRingtoneSet && (TextUtils.isEmpty(MediaScanner.this.mDefaultRingtoneFilename) || doesPathHaveFilename(entry.mPath, MediaScanner.this.mDefaultRingtoneFilename))) {
                            needToSetSettings = true;
                        }
                        if (MultiSimManager.getSimSlotCount() > 1 && !MediaScanner.this.mDefaultRingtoneSet_2 && (TextUtils.isEmpty(MediaScanner.this.mDefaultRingtoneFilename_2) || doesPathHaveFilename(entry.mPath, MediaScanner.this.mDefaultRingtoneFilename_2))) {
                            needToSetSettings = true;
                            multiSimSettings = true;
                        }
                    } else if (alarms && !MediaScanner.this.mDefaultAlarmSet && (TextUtils.isEmpty(MediaScanner.this.mDefaultAlarmAlertFilename) || doesPathHaveFilename(entry.mPath, MediaScanner.this.mDefaultAlarmAlertFilename))) {
                        needToSetSettings = true;
                    }
                }
                long bulkInserterTime = System.currentTimeMillis();
                if (inserter == null || needToSetSettings) {
                    if (inserter != null) {
                        inserter.flushAll();
                    }
                    result = MediaScanner.this.mMediaProvider.insert(MediaScanner.this.mPackageName, tableUri, values);
                } else if (this.mFormat == 12289) {
                    inserter.insertwithPriority(tableUri, values);
                } else {
                    inserter.insert(tableUri, values);
                }
                MediaScanner.this.mTotalInserted = 1 + MediaScanner.this.mTotalInserted;
                MediaScanner.access$4214(MediaScanner.this, System.currentTimeMillis() - bulkInserterTime);
                if (result != null) {
                    rowId = ContentUris.parseId(result);
                    entry.mRowId = rowId;
                }
            } else {
                result = ContentUris.withAppendedId(tableUri, rowId);
                if (MediaFile.isAudioFileType(this.mFileType) || MediaFile.isImageFileType(this.mFileType) || MediaFile.isVideoFileType(this.mFileType)) {
                    computeDisplayName(values.getAsString("_data"), values);
                }
                if (MediaFile.isImageFileType(this.mFileType) || MediaFile.isVideoFileType(this.mFileType)) {
                    values.put("mini_thumb_magic", Integer.valueOf(0));
                }
                values.remove("_data");
                int mediaType = 0;
                if (!MediaScanner.isNoMediaPath(entry.mPath)) {
                    int fileType = MediaFile.getFileTypeForMimeType(this.mMimeType);
                    if (MediaFile.isAudioFileType(fileType)) {
                        mediaType = 2;
                    } else if (MediaFile.isVideoFileType(fileType)) {
                        mediaType = 3;
                    } else if (MediaFile.isImageFileType(fileType)) {
                        mediaType = 1;
                    } else if (MediaFile.isPlayListFileType(fileType)) {
                        mediaType = 4;
                    }
                    values.put("media_type", Integer.valueOf(mediaType));
                }
                MediaScanner.this.mTotalUpdated = 1 + MediaScanner.this.mTotalUpdated;
                MediaScanner.this.mMediaProvider.update(MediaScanner.this.mPackageName, result, values, null, null);
            }
            if (needToSetSettings) {
                if (notifications) {
                    if (!(MediaScanner.this.mDefaultNotificationSet || multiSimSettings)) {
                        setSettingIfNotSet("notification_sound", tableUri, rowId);
                        MediaScanner.this.mDefaultNotificationSet = true;
                    }
                    if (MultiSimManager.getSimSlotCount() > 1 && !MediaScanner.this.mDefaultNotificationSet_2 && multiSimSettings) {
                        setSettingIfNotSet("notification_sound_2", tableUri, rowId);
                        MediaScanner.this.mDefaultNotificationSet_2 = true;
                        Log.i(MediaScanner.TAG, "SIM2 default notification is set :  notification_sound_2");
                    }
                } else if (ringtones) {
                    if (!(MediaScanner.this.mDefaultRingtoneSet || multiSimSettings)) {
                        setSettingIfNotSet("ringtone_default", tableUri, rowId);
                        setSettingIfNotSet("ringtone", tableUri, rowId);
                        if (TelephonyManager.getDefault().isMultiSimEnabled()) {
                            int phoneCount = TelephonyManager.getDefault().getPhoneCount();
                            for (i = 1; i < phoneCount; i++) {
                                setSettingIfNotSet("ringtone_" + (i + 1), tableUri, rowId);
                            }
                        }
                        MediaScanner.this.mDefaultRingtoneSet = true;
                    }
                    if (MultiSimManager.getSimSlotCount() > 1 && !MediaScanner.this.mDefaultRingtoneSet_2 && multiSimSettings) {
                        setSettingIfNotSet("ringtone_2", tableUri, rowId);
                        MediaScanner.this.mDefaultRingtoneSet_2 = true;
                        Log.i(MediaScanner.TAG, "SIM2 default notification is set :  ringtone_2");
                    }
                } else if (alarms) {
                    setSettingIfNotSet("alarm_alert", tableUri, rowId);
                    MediaScanner.this.mDefaultAlarmSet = true;
                }
            }
            return result;
        }

        private boolean doesPathHaveFilename(String path, String filename) {
            int pathFilenameStart = path.lastIndexOf(File.separatorChar) + 1;
            int filenameLength = filename.length();
            if (path.regionMatches(pathFilenameStart, filename, 0, filenameLength) && pathFilenameStart + filenameLength == path.length()) {
                return true;
            }
            return false;
        }

        private void setSettingIfNotSet(String settingName, Uri uri, long rowId) {
            if (TextUtils.isEmpty(System.getString(MediaScanner.this.mContext.getContentResolver(), settingName))) {
                System.putString(MediaScanner.this.mContext.getContentResolver(), settingName, ContentUris.withAppendedId(uri, rowId).toString());
            }
        }

        private int getFileTypeFromDrm(String path) {
            if (!MediaScanner.this.isDrmEnabled()) {
                return 0;
            }
            if (MediaScanner.this.mDrmManagerClient == null) {
                MediaScanner.this.mDrmManagerClient = new DrmManagerClient(MediaScanner.this.mContext);
            }
            if (!MediaScanner.this.mDrmManagerClient.canHandle(path, null)) {
                return 0;
            }
            this.mIsDrm = true;
            String drmMimetype = MediaScanner.this.mDrmManagerClient.getOriginalMimeType(path);
            if (drmMimetype == null) {
                return 0;
            }
            this.mMimeType = drmMimetype;
            return MediaFile.getFileTypeForMimeType(drmMimetype);
        }
    }

    private static class PlaylistEntry {
        long bestmatchid;
        int bestmatchlevel;
        String path;

        private PlaylistEntry() {
        }
    }

    class WplHandler implements ElementListener {
        final ContentHandler handler;
        String playListDirectory;

        public WplHandler(String playListDirectory, Uri uri, Cursor fileList) {
            this.playListDirectory = playListDirectory;
            RootElement root = new RootElement("smil");
            root.getChild(TtmlUtils.TAG_BODY).getChild("seq").getChild("media").setElementListener(this);
            this.handler = root.getContentHandler();
        }

        public void start(Attributes attributes) {
            String path = attributes.getValue(ProxyInfo.LOCAL_EXCL_LIST, "src");
            if (path != null) {
                MediaScanner.this.cachePlaylistEntry(path, this.playListDirectory);
            }
        }

        public void end() {
        }

        ContentHandler getContentHandler() {
            return this.handler;
        }
    }

    public static native void initMutex();

    private final native void native_finalize();

    private static final native void native_init();

    private final native void native_setup();

    public static native void notifyUnmount(String str);

    private native void processDirectory(String str, MediaScannerClient mediaScannerClient);

    private native void processFile(String str, String str2, MediaScannerClient mediaScannerClient);

    private void processPlayLists() throws android.os.RemoteException {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(Unknown Source)
	at java.util.HashMap$KeyIterator.next(Unknown Source)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:80)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r11 = this;
        r0 = r11.mPlayLists;
        r10 = r0.iterator();
        r9 = 0;
        r0 = r11.mMediaProvider;	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
        r1 = r11.mPackageName;	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
        r2 = r11.mFilesUri;	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
        r3 = FILES_PRESCAN_PROJECTION;	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
        r4 = "media_type=2";	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
        r5 = 0;	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
        r6 = 0;	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
        r7 = 0;	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
        r9 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
    L_0x0019:
        r0 = r10.hasNext();	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
        if (r0 == 0) goto L_0x0036;	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
    L_0x001f:
        r8 = r10.next();	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
        r8 = (android.media.MediaScanner.FileEntry) r8;	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
        r0 = r8.mLastModifiedChanged;	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
        if (r0 == 0) goto L_0x0019;	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
    L_0x0029:
        if (r9 == 0) goto L_0x0019;	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
    L_0x002b:
        r11.processPlayList(r8, r9);	 Catch:{ RemoteException -> 0x002f, all -> 0x003c }
        goto L_0x0019;
    L_0x002f:
        r0 = move-exception;
        if (r9 == 0) goto L_0x0035;
    L_0x0032:
        r9.close();
    L_0x0035:
        return;
    L_0x0036:
        if (r9 == 0) goto L_0x0035;
    L_0x0038:
        r9.close();
        goto L_0x0035;
    L_0x003c:
        r0 = move-exception;
        if (r9 == 0) goto L_0x0042;
    L_0x003f:
        r9.close();
    L_0x0042:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.processPlayLists():void");
    }

    private void pruneDeadThumbnailFiles() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0127 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r18 = this;
        r12 = new java.util.HashSet;
        r12.<init>();
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r0 = r18;
        r2 = r0.mExternalStoragePath;
        r1 = r1.append(r2);
        r2 = "/DCIM/.thumbnails";
        r1 = r1.append(r2);
        r10 = r1.toString();
        r1 = new java.io.File;
        r1.<init>(r10);
        r14 = r1.list();
        r9 = 0;
        if (r14 != 0) goto L_0x002b;
    L_0x0028:
        r1 = 0;
        r14 = new java.lang.String[r1];
    L_0x002b:
        r16 = 0;
    L_0x002d:
        r1 = r14.length;
        r0 = r16;
        if (r0 >= r1) goto L_0x0051;
    L_0x0032:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1 = r1.append(r10);
        r2 = "/";
        r1 = r1.append(r2);
        r2 = r14[r16];
        r1 = r1.append(r2);
        r15 = r1.toString();
        r12.add(r15);
        r16 = r16 + 1;
        goto L_0x002d;
    L_0x0051:
        r0 = r18;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r1 = r0.mMediaProvider;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r0 = r18;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2 = r0.mPackageName;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r0 = r18;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r3 = r0.mThumbsUri;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r4 = 1;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r4 = new java.lang.String[r4];	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r5 = 0;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r6 = "_data";	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r4[r5] = r6;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r5 = 0;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r6 = 0;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r7 = 0;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r8 = 0;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r9 = r1.query(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r1 = "MediaScanner";	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2.<init>();	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r3 = "images pruneDeadThumbnailFiles... ";	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2 = r2.append(r9);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2 = r2.toString();	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        android.util.Log.v(r1, r2);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        if (r9 == 0) goto L_0x009c;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
    L_0x0088:
        r1 = r9.moveToFirst();	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        if (r1 == 0) goto L_0x009c;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
    L_0x008e:
        r1 = 0;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r15 = r9.getString(r1);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r12.remove(r15);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r1 = r9.moveToNext();	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        if (r1 != 0) goto L_0x008e;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
    L_0x009c:
        if (r9 == 0) goto L_0x00a1;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
    L_0x009e:
        r9.close();	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
    L_0x00a1:
        r0 = r18;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r1 = r0.mMediaProvider;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r0 = r18;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2 = r0.mPackageName;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r3 = "external";	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r3 = android.provider.MediaStore.Video.Thumbnails.getContentUri(r3);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r4 = 1;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r4 = new java.lang.String[r4];	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r5 = 0;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r6 = "_data";	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r4[r5] = r6;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r5 = 0;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r6 = 0;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r7 = 0;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r8 = 0;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r9 = r1.query(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r1 = "MediaScanner";	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2.<init>();	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r3 = "video pruneDeadThumbnailFiles... ";	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2 = r2.append(r9);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2 = r2.toString();	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        android.util.Log.v(r1, r2);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        if (r9 == 0) goto L_0x00ee;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
    L_0x00da:
        r1 = r9.moveToFirst();	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        if (r1 == 0) goto L_0x00ee;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
    L_0x00e0:
        r1 = 0;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r15 = r9.getString(r1);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r12.remove(r15);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r1 = r9.moveToNext();	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        if (r1 != 0) goto L_0x00e0;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
    L_0x00ee:
        r17 = r12.iterator();	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
    L_0x00f2:
        r1 = r17.hasNext();	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        if (r1 == 0) goto L_0x0128;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
    L_0x00f8:
        r13 = r17.next();	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r13 = (java.lang.String) r13;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r1 = new java.io.File;	 Catch:{ SecurityException -> 0x0107 }
        r1.<init>(r13);	 Catch:{ SecurityException -> 0x0107 }
        r1.delete();	 Catch:{ SecurityException -> 0x0107 }
        goto L_0x00f2;
    L_0x0107:
        r11 = move-exception;
        r1 = "MediaScanner";	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2.<init>();	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r3 = "SeecurityException occured during delete : ";	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2 = r2.append(r13);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        r2 = r2.toString();	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        android.util.Log.e(r1, r2);	 Catch:{ RemoteException -> 0x0121, all -> 0x012e }
        goto L_0x00f2;
    L_0x0121:
        r1 = move-exception;
        if (r9 == 0) goto L_0x0127;
    L_0x0124:
        r9.close();
    L_0x0127:
        return;
    L_0x0128:
        if (r9 == 0) goto L_0x0127;
    L_0x012a:
        r9.close();
        goto L_0x0127;
    L_0x012e:
        r1 = move-exception;
        if (r9 == 0) goto L_0x0134;
    L_0x0131:
        r9.close();
    L_0x0134:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.pruneDeadThumbnailFiles():void");
    }

    public native byte[] extractAlbumArt(FileDescriptor fileDescriptor);

    public void scanMtpFile(java.lang.String r28, java.lang.String r29, int r30, int r31) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0123 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r27 = this;
        r0 = r27;
        r1 = r29;
        r0.initialize(r1);
        r26 = android.media.MediaFile.getFileType(r28);
        if (r26 != 0) goto L_0x0075;
    L_0x000d:
        r25 = 0;
    L_0x000f:
        r23 = new java.io.File;
        r0 = r23;
        r1 = r28;
        r0.<init>(r1);
        r4 = r23.lastModified();
        r10 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r14 = r4 / r10;
        r4 = android.media.MediaFile.isAudioFileType(r25);
        if (r4 != 0) goto L_0x0087;
    L_0x0026:
        r4 = android.media.MediaFile.isVideoFileType(r25);
        if (r4 != 0) goto L_0x0087;
    L_0x002c:
        r4 = android.media.MediaFile.isImageFileType(r25);
        if (r4 != 0) goto L_0x0087;
    L_0x0032:
        r4 = android.media.MediaFile.isPlayListFileType(r25);
        if (r4 != 0) goto L_0x0087;
    L_0x0038:
        r4 = android.media.MediaFile.isDrmFileType(r25);
        if (r4 != 0) goto L_0x0087;
    L_0x003e:
        r7 = new android.content.ContentValues;
        r7.<init>();
        r4 = "_size";
        r10 = r23.length();
        r5 = java.lang.Long.valueOf(r10);
        r7.put(r4, r5);
        r4 = "date_modified";
        r5 = java.lang.Long.valueOf(r14);
        r7.put(r4, r5);
        r4 = 1;
        r9 = new java.lang.String[r4];	 Catch:{ RemoteException -> 0x007c }
        r4 = 0;	 Catch:{ RemoteException -> 0x007c }
        r5 = java.lang.Integer.toString(r30);	 Catch:{ RemoteException -> 0x007c }
        r9[r4] = r5;	 Catch:{ RemoteException -> 0x007c }
        r0 = r27;	 Catch:{ RemoteException -> 0x007c }
        r4 = r0.mMediaProvider;	 Catch:{ RemoteException -> 0x007c }
        r0 = r27;	 Catch:{ RemoteException -> 0x007c }
        r5 = r0.mPackageName;	 Catch:{ RemoteException -> 0x007c }
        r6 = android.provider.MediaStore.Files.getMtpObjectsUri(r29);	 Catch:{ RemoteException -> 0x007c }
        r8 = "_id=?";	 Catch:{ RemoteException -> 0x007c }
        r4.update(r5, r6, r7, r8, r9);	 Catch:{ RemoteException -> 0x007c }
    L_0x0074:
        return;
    L_0x0075:
        r0 = r26;
        r0 = r0.fileType;
        r25 = r0;
        goto L_0x000f;
    L_0x007c:
        r21 = move-exception;
        r4 = "MediaScanner";
        r5 = "RemoteException in scanMtpFile";
        r0 = r21;
        android.util.Log.e(r4, r5, r0);
        goto L_0x0074;
    L_0x0087:
        r0 = r30;
        r1 = r27;
        r1.mMtpObjectHandle = r0;
        r24 = 0;
        r4 = android.media.MediaFile.isPlayListFileType(r25);	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        if (r4 == 0) goto L_0x00dd;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
    L_0x0095:
        r4 = 0;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r5 = 1;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r0 = r27;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r6 = r0.mContext;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r0 = r27;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r6 = r0.getExternalStorageSdPath(r6);	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r0 = r27;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r0.prescan(r4, r5, r6);	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r22 = r27.makeEntryFor(r28);	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        if (r22 == 0) goto L_0x00cf;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
    L_0x00ac:
        r0 = r27;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r10 = r0.mMediaProvider;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r0 = r27;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r11 = r0.mPackageName;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r0 = r27;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r12 = r0.mFilesUri;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r13 = FILES_PRESCAN_PROJECTION;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r14 = 0;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r15 = 0;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r16 = 0;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r17 = 0;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r24 = r10.query(r11, r12, r13, r14, r15, r16, r17);	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        if (r24 == 0) goto L_0x00cf;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
    L_0x00c6:
        r0 = r27;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r1 = r22;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r2 = r24;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r0.processPlayList(r1, r2);	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
    L_0x00cf:
        r4 = 0;
        r0 = r27;
        r0.mMtpObjectHandle = r4;
        if (r24 == 0) goto L_0x00d9;
    L_0x00d6:
        r24.close();
    L_0x00d9:
        r27.releaseResources();
        goto L_0x0074;
    L_0x00dd:
        r4 = 0;
        r0 = r27;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r5 = r0.mContext;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r0 = r27;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r5 = r0.getExternalStorageSdPath(r5);	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r0 = r27;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r1 = r28;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r0.prescan(r1, r4, r5);	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r0 = r27;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r11 = r0.mClient;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r0 = r26;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r13 = r0.mimeType;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r16 = r23.length();	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r4 = 12289; // 0x3001 float:1.722E-41 double:6.0716E-320;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r0 = r31;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        if (r0 != r4) goto L_0x0128;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
    L_0x0101:
        r18 = 1;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
    L_0x0103:
        r19 = 1;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r20 = isNoMediaPath(r28);	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r12 = r28;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r11.doScanFile(r12, r13, r14, r16, r18, r19, r20);	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        goto L_0x00cf;
    L_0x010f:
        r21 = move-exception;
        r4 = "MediaScanner";	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r5 = "RemoteException in MediaScanner.scanFile()";	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r0 = r21;	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        android.util.Log.e(r4, r5, r0);	 Catch:{ RemoteException -> 0x010f, all -> 0x012b }
        r4 = 0;
        r0 = r27;
        r0.mMtpObjectHandle = r4;
        if (r24 == 0) goto L_0x0123;
    L_0x0120:
        r24.close();
    L_0x0123:
        r27.releaseResources();
        goto L_0x0074;
    L_0x0128:
        r18 = 0;
        goto L_0x0103;
    L_0x012b:
        r4 = move-exception;
        r5 = 0;
        r0 = r27;
        r0.mMtpObjectHandle = r5;
        if (r24 == 0) goto L_0x0136;
    L_0x0133:
        r24.close();
    L_0x0136:
        r27.releaseResources();
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.scanMtpFile(java.lang.String, java.lang.String, int, int):void");
    }

    public native void setLocale(String str);

    static /* synthetic */ long access$1414(MediaScanner x0, long x1) {
        long j = x0.mTotalParsingTime + x1;
        x0.mTotalParsingTime = j;
        return j;
    }

    static /* synthetic */ long access$1514(MediaScanner x0, long x1) {
        long j = x0.mTotalBitmapDecodingTime + x1;
        x0.mTotalBitmapDecodingTime = j;
        return j;
    }

    static /* synthetic */ long access$2214(MediaScanner x0, long x1) {
        long j = x0.mTotalExifExtractingTime + x1;
        x0.mTotalExifExtractingTime = j;
        return j;
    }

    static /* synthetic */ long access$2314(MediaScanner x0, long x1) {
        long j = x0.mTotalSefExtractingTime + x1;
        x0.mTotalSefExtractingTime = j;
        return j;
    }

    static /* synthetic */ long access$4214(MediaScanner x0, long x1) {
        long j = x0.mTotalBulkInserterTime + x1;
        x0.mTotalBulkInserterTime = j;
        return j;
    }

    static /* synthetic */ long access$614(MediaScanner x0, long x1) {
        long j = x0.mTotalMakeEntryTime + x1;
        x0.mTotalMakeEntryTime = j;
        return j;
    }

    static {
        System.loadLibrary("media_jni");
        native_init();
        String[] strArr = new String[1];
        strArr[0] = String.format("COUNT(%s)", new Object[]{"_id"});
        ID_PROJECTION_COUNT = strArr;
    }

    public MediaScanner(Context c) {
        native_setup();
        this.mContext = c;
        this.mPackageName = c.getPackageName();
        this.mBitmapOptions.inSampleSize = 1;
        this.mBitmapOptions.inJustDecodeBounds = true;
        setDefaultRingtoneFileNames();
        this.mExternalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        this.mInternalStoragePath = Environment.getRootDirectory().toString();
        this.mPrivateStoragePath = getPrivateStorageDir(c);
        this.mExternalIsEmulated = Environment.isExternalStorageEmulated();
        this.mPrivateStorageIsMounted = isPrivateStorageMounted(c);
    }

    public static String encodeStringResource(String str) {
        if (str == null) {
            return null;
        }
        byte[] bytes = str.getBytes();
        for (int i = 0; i < str.getBytes().length; i++) {
            int a = bytes[i];
            int b = a;
            if ((a & 2) != 0) {
                b |= 1;
            } else {
                b &= 254;
            }
            if ((a & 1) != 0) {
                b |= 2;
            } else {
                b &= 253;
            }
            if ((a & 16) != 0) {
                b |= 8;
            } else {
                b &= R.styleable.Theme_buttonBarNegativeButtonStyle;
            }
            if ((a & 8) != 0) {
                b |= 16;
            } else {
                b &= 239;
            }
            bytes[i] = (byte) b;
        }
        return new String(bytes);
    }

    private String getExternalStorageSdPath(Context context) {
        StorageVolume[] storageVolumes = ((StorageManager) context.getSystemService(Context.STORAGE_SERVICE)).getVolumeList();
        if (storageVolumes != null) {
            for (StorageVolume volume : storageVolumes) {
                if ("sd".equals(volume.getSubSystem()) && volume.isRemovable()) {
                    Log.d(TAG, "getExternalStorageSdPath :  " + volume.getPath());
                    return volume.getPath();
                }
            }
        } else {
            Log.e(TAG, "The storageVolumes is null.");
        }
        Log.d(TAG, "getExternalStorageSdPath return null");
        return null;
    }

    private void setDefaultRingtoneFileNames() {
        this.mDefaultRingtoneFilename = SystemProperties.get("ro.config.ringtone");
        this.mDefaultNotificationFilename = SystemProperties.get("ro.config.notification_sound");
        this.mDefaultAlarmAlertFilename = SystemProperties.get("ro.config.alarm_alert");
        if (MultiSimManager.getSimSlotCount() > 1) {
            this.mDefaultRingtoneFilename_2 = SystemProperties.get("ro.config.ringtone_2");
            this.mDefaultNotificationFilename_2 = SystemProperties.get("ro.config.notification_sound_2");
        }
    }

    private boolean isDrmEnabled() {
        return true;
    }

    private boolean isPrivateStorageMounted(Context context) {
        try {
            this.mPrivateStorageIsMounted = PrivateModeManager.isPrivateStorageMounted(context);
        } catch (NoClassDefFoundError e) {
            this.mPrivateStorageIsMounted = false;
        } catch (NoSuchMethodError e2) {
            this.mPrivateStorageIsMounted = false;
        }
        return this.mPrivateStorageIsMounted;
    }

    private String getPrivateStorageDir(Context context) {
        try {
            return PrivateModeManager.getPrivateStorageDir(context);
        } catch (NoClassDefFoundError e) {
            return null;
        } catch (NoSuchMethodError e2) {
            return null;
        }
    }

    private int prescan(String filePath, boolean prescanFiles, String externalStorageSdPath) throws RemoteException {
        String where;
        String[] selectionArgs;
        synchronized (syncEjectedPath) {
            ejectedPath = null;
        }
        if (this.mFileCache == null) {
            this.mFileCache = new HashMap();
        } else {
            this.mFileCache.clear();
        }
        if (this.mFileParsingStat == null) {
            this.mFileParsingStat = new HashMap();
        } else {
            this.mFileParsingStat.clear();
        }
        if (this.mPlayLists == null) {
            this.mPlayLists = new ArrayList();
        } else {
            this.mPlayLists.clear();
        }
        if (filePath != null) {
            where = "_id>? AND _data=?";
            selectionArgs = new String[]{ProxyInfo.LOCAL_EXCL_LIST, filePath};
        } else {
            where = "_id>?";
            selectionArgs = new String[]{ProxyInfo.LOCAL_EXCL_LIST};
        }
        this.beforePrescanCount = 0;
        Cursor c = this.mMediaProvider.query(this.mPackageName, this.mImagesUri, ID_PROJECTION_COUNT, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            this.beforePrescanCount = c.getInt(0);
            c.close();
        }
        Builder builder = this.mFilesUri.buildUpon();
        builder.appendQueryParameter(MediaStore.PARAM_DELETE_DATA, AudioParameter.AUDIO_PARAMETER_VALUE_false);
        MediaBulkDeleter mediaBulkDeleter = new MediaBulkDeleter(this.mMediaProvider, this.mPackageName, builder.build(), this.mExternalStoragePath);
        mediaBulkDeleter = new MediaBulkDeleter(this.mMediaProvider, this.mPackageName, builder.build(), externalStorageSdPath);
        if (prescanFiles) {
            c = this.mMediaProvider.query(this.mPackageName, this.mFilesUri, ID_PROJECTION_COUNT, null, null, null, null);
            if (c != null && c.moveToFirst()) {
                this.contentsCount = c.getInt(0);
                if (this.contentsCount < DB_FILES_NUMBER_CACHING_LIMITATION) {
                    this.useHashMap = true;
                } else {
                    this.useHashMap = false;
                }
                Log.d(TAG, "Prescan. DB items number : " + this.contentsCount + " DB_FILES_NUMBER_CACHING_LIMITATION : " + DB_FILES_NUMBER_CACHING_LIMITATION + "  Caching mode : " + this.useHashMap);
                c.close();
                c = null;
            }
            long lastId = Long.MIN_VALUE;
            Uri limitUri = this.mFilesUri.buildUpon().appendQueryParameter("limit", "1000").build();
            this.mWasEmptyPriorToScan = true;
            while (true) {
                selectionArgs[0] = ProxyInfo.LOCAL_EXCL_LIST + lastId;
                if (c != null) {
                    c.close();
                }
                c = this.mMediaProvider.query(this.mPackageName, limitUri, FILES_PRESCAN_PROJECTION, where, selectionArgs, "_id", null);
                if (c == null) {
                    break;
                }
                try {
                    if (c.getCount() == 0) {
                        break;
                    }
                    this.mWasEmptyPriorToScan = false;
                    while (c.moveToNext()) {
                        long rowId = c.getLong(0);
                        String path = c.getString(1);
                        int format = c.getInt(2);
                        long lastModified = c.getLong(3);
                        lastId = rowId;
                        if (path != null && path.startsWith("/")) {
                            boolean exists = false;
                            try {
                                MediaFileType mediaFileType;
                                String key;
                                if (ejectedPath == null || !path.startsWith(ejectedPath)) {
                                    exists = Os.access(path, OsConstants.F_OK);
                                    if (!exists) {
                                        if (!MtpConstants.isAbstractObject(format)) {
                                            mediaFileType = MediaFile.getFileType(path);
                                            if (MediaFile.isPlayListFileType(mediaFileType != null ? 0 : mediaFileType.fileType)) {
                                                if (externalStorageSdPath == null && path.startsWith(externalStorageSdPath)) {
                                                    mediaBulkDeleter.delete(rowId);
                                                } else {
                                                    mediaBulkDeleter.delete(rowId);
                                                }
                                                if (path.toLowerCase(Locale.US).endsWith("/.nomedia")) {
                                                    mediaBulkDeleter.flush();
                                                    mediaBulkDeleter.flush();
                                                    this.mMediaProvider.call(this.mPackageName, MediaStore.UNHIDE_CALL, new File(path).getParent(), null);
                                                }
                                            }
                                        }
                                    }
                                    if (this.useHashMap) {
                                        key = path;
                                        if (this.mCaseInsensitivePaths) {
                                            key = path.toLowerCase();
                                        }
                                        this.mFileCache.put(key, new FileEntry(rowId, path, lastModified));
                                    }
                                } else {
                                    exists = false;
                                    if (exists) {
                                        if (MtpConstants.isAbstractObject(format)) {
                                            mediaFileType = MediaFile.getFileType(path);
                                            if (mediaFileType != null) {
                                            }
                                            if (MediaFile.isPlayListFileType(mediaFileType != null ? 0 : mediaFileType.fileType)) {
                                                if (externalStorageSdPath == null) {
                                                }
                                                mediaBulkDeleter.delete(rowId);
                                                if (path.toLowerCase(Locale.US).endsWith("/.nomedia")) {
                                                    mediaBulkDeleter.flush();
                                                    mediaBulkDeleter.flush();
                                                    this.mMediaProvider.call(this.mPackageName, MediaStore.UNHIDE_CALL, new File(path).getParent(), null);
                                                }
                                            }
                                        }
                                    }
                                    if (this.useHashMap) {
                                        key = path;
                                        if (this.mCaseInsensitivePaths) {
                                            key = path.toLowerCase();
                                        }
                                        this.mFileCache.put(key, new FileEntry(rowId, path, lastModified));
                                    }
                                }
                            } catch (ErrnoException e) {
                            }
                        }
                    }
                } catch (Throwable th) {
                    if (c != null) {
                        c.close();
                    }
                    mediaBulkDeleter.flush();
                    mediaBulkDeleter.flush();
                }
            }
        }
        if (c != null) {
            c.close();
        }
        mediaBulkDeleter.flush();
        mediaBulkDeleter.flush();
        this.mOriginalCount = 0;
        c = this.mMediaProvider.query(this.mPackageName, this.mImagesUri, ID_PROJECTION_COUNT, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            this.mOriginalCount = c.getInt(0);
            c.close();
        }
        int deletedNumber = mediaBulkDeleter.getTotalDeletedNumber() + mediaBulkDeleter.getTotalDeletedNumber();
        synchronized (syncEjectedPath) {
            ejectedPath = null;
        }
        return deletedNumber;
    }

    public static void terminateBulkDeleter(String path) {
        Log.d(TAG, "TerminateBulkDeleter is called for path : " + path);
        synchronized (syncEjectedPath) {
            ejectedPath = path;
        }
    }

    private boolean inScanDirectory(String path, String[] directories) {
        for (String directory : directories) {
            if (path.startsWith(directory)) {
                return true;
            }
        }
        return false;
    }

    private void postscan(String[] directories) throws RemoteException {
        if (this.mProcessPlaylists) {
            processPlayLists();
        }
        long start = System.currentTimeMillis();
        if ((this.mOriginalCount == 0 || this.beforePrescanCount > this.mOriginalCount) && this.mImagesUri.equals(Media.getContentUri("external"))) {
            pruneDeadThumbnailFiles();
        }
        this.mTotalDeadThumbnailTime = System.currentTimeMillis() - start;
        this.mPlayLists = null;
        if (this.mFileCache != null) {
            this.mFileCache.clear();
        }
        this.mFileCache = null;
        this.mMediaProvider = null;
    }

    private void releaseResources() {
        if (this.mDrmManagerClient != null) {
            this.mDrmManagerClient.release();
            this.mDrmManagerClient = null;
        }
    }

    private void initialize(String volumeName) {
        this.mMediaProvider = this.mContext.getContentResolver().acquireProvider("media");
        this.mAudioUri = Audio.Media.getContentUri(volumeName);
        this.mVideoUri = Video.Media.getContentUri(volumeName);
        this.mImagesUri = Media.getContentUri(volumeName);
        this.mThumbsUri = Thumbnails.getContentUri(volumeName);
        this.mFilesUri = Files.getContentUri(volumeName);
        this.mFilesUriNoNotify = this.mFilesUri.buildUpon().appendQueryParameter("nonotify", WifiEnterpriseConfig.ENGINE_ENABLE).build();
        if (!volumeName.equals("internal")) {
            this.mProcessPlaylists = true;
            this.mProcessGenres = true;
            this.mPlaylistsUri = Playlists.getContentUri(volumeName);
            this.mCaseInsensitivePaths = true;
        }
    }

    private void printToMediaProviderDBLog(String message, String volumeName) {
        ContentValues val = new ContentValues();
        val.put("title", message);
        Uri logUri = MediaStore.getMediaProviderDbLogUri(volumeName);
        try {
            this.mMediaProvider = this.mContext.getContentResolver().acquireProvider("media");
            this.mMediaProvider.insert(this.mPackageName, logUri, val);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in printToMediaProviderDBLog()", e);
        } finally {
            this.mMediaProvider = null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void scanDirectories(java.lang.String[] r41, java.lang.String r42) {
        /*
        r40 = this;
        r0 = r40;
        r0 = r0.mContext;
        r35 = r0;
        r0 = r40;
        r1 = r35;
        r11 = r0.getExternalStorageSdPath(r1);
        r32 = java.lang.System.currentTimeMillis();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r1 = r42;
        r0.initialize(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = 0;
        r36 = 1;
        r0 = r40;
        r1 = r35;
        r2 = r36;
        r35 = r0.prescan(r1, r2, r11);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r35;
        r0 = (long) r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r24 = r0;
        r22 = java.lang.System.currentTimeMillis();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = new android.media.MediaInserter;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mMediaProvider;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r0 = r40;
        r0 = r0.mPackageName;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = r0;
        r38 = 100;
        r35.<init>(r36, r37, r38);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r35;
        r1 = r40;
        r1.mMediaInserter = r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r16 = 0;
    L_0x004b:
        r0 = r41;
        r0 = r0.length;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = r0;
        r0 = r16;
        r1 = r35;
        if (r0 >= r1) goto L_0x00cf;
    L_0x0056:
        if (r11 == 0) goto L_0x0062;
    L_0x0058:
        r35 = r41[r16];	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r35;
        r35 = r0.startsWith(r11);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        if (r35 != 0) goto L_0x007e;
    L_0x0062:
        r35 = r41[r16];	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mExternalStoragePath;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r35 = r35.startsWith(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        if (r35 != 0) goto L_0x007e;
    L_0x0070:
        r35 = r41[r16];	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mInternalStoragePath;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r35 = r35.startsWith(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        if (r35 == 0) goto L_0x00cb;
    L_0x007e:
        r35 = "MediaScanner";
        r36 = new java.lang.StringBuilder;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36.<init>();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "processDirectory :  ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = r41[r16];	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r36.toString();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        android.util.Log.v(r35, r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = r41[r16];	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mClient;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r0 = r40;
        r1 = r35;
        r2 = r36;
        r0.processDirectory(r1, r2);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r6 = java.lang.System.currentTimeMillis();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mMediaInserter;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = r0;
        r35.flushAll();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalBulkInserterTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r38 = java.lang.System.currentTimeMillis();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r38 - r6;
        r36 = r36 + r38;
        r0 = r36;
        r2 = r40;
        r2.mTotalBulkInserterTime = r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
    L_0x00cb:
        r16 = r16 + 1;
        goto L_0x004b;
    L_0x00cf:
        r0 = r40;
        r0 = r0.mPrivateStorageIsMounted;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = r0;
        if (r35 == 0) goto L_0x0134;
    L_0x00d7:
        r0 = r40;
        r0 = r0.mPrivateStoragePath;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = r0;
        if (r35 == 0) goto L_0x0134;
    L_0x00df:
        r35 = "MediaScanner";
        r36 = new java.lang.StringBuilder;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36.<init>();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "processDirectory force private storage : ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mPrivateStoragePath;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = r0;
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r36.toString();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        android.util.Log.v(r35, r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mPrivateStoragePath;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = r0;
        r0 = r40;
        r0 = r0.mClient;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r0 = r40;
        r1 = r35;
        r2 = r36;
        r0.processDirectory(r1, r2);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r6 = java.lang.System.currentTimeMillis();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mMediaInserter;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = r0;
        r35.flushAll();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalBulkInserterTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r38 = java.lang.System.currentTimeMillis();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r38 - r6;
        r36 = r36 + r38;
        r0 = r36;
        r2 = r40;
        r2.mTotalBulkInserterTime = r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
    L_0x0134:
        r35 = 0;
        r0 = r35;
        r1 = r40;
        r1.mMediaInserter = r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r28 = java.lang.System.currentTimeMillis();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r40.postscan(r41);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r12 = java.lang.System.currentTimeMillis();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r18 = 0;
        r20 = 0;
        r8 = 0;
        r4 = 0;
        r14 = 0;
        r30 = 0;
        r36 = r28 - r22;
        r38 = 0;
        r35 = (r36 > r38 ? 1 : (r36 == r38 ? 0 : -1));
        if (r35 == 0) goto L_0x01af;
    L_0x015b:
        r0 = r40;
        r0 = r0.mTotalMakeEntryTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r38 = 100;
        r36 = r36 * r38;
        r38 = r28 - r22;
        r18 = r36 / r38;
        r0 = r40;
        r0 = r0.mTotalParsingTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r38 = 100;
        r36 = r36 * r38;
        r38 = r28 - r22;
        r20 = r36 / r38;
        r0 = r40;
        r0 = r0.mTotalBitmapDecodingTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r38 = 100;
        r36 = r36 * r38;
        r38 = r28 - r22;
        r4 = r36 / r38;
        r0 = r40;
        r0 = r0.mTotalExifExtractingTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r38 = 100;
        r36 = r36 * r38;
        r38 = r28 - r22;
        r14 = r36 / r38;
        r0 = r40;
        r0 = r0.mTotalSefExtractingTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r38 = 100;
        r36 = r36 * r38;
        r38 = r28 - r22;
        r30 = r36 / r38;
        r0 = r40;
        r0 = r0.mTotalBulkInserterTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r38 = 100;
        r36 = r36 * r38;
        r38 = r28 - r22;
        r8 = r36 / r38;
    L_0x01af:
        r35 = "MediaScanner";
        r36 = new java.lang.StringBuilder;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36.<init>();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = " prescan time: ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r22 - r32;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "ms (DB items: ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.contentsCount;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = r0;
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = ")\n";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r36.toString();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        android.util.Slog.v(r35, r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = "MediaScanner";
        r36 = new java.lang.StringBuilder;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36.<init>();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "    scan time: ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r28 - r22;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "ms (Caching mode: ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.useHashMap;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = r0;
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "), (makeEntry time: ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalMakeEntryTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r0;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "ms ~";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r36;
        r1 = r18;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "%),  (parsing time: ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalParsingTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r0;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "ms ~";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r36;
        r1 = r20;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "%),  (bitmapDecoding time: ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalBitmapDecodingTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r0;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "ms ~";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r36;
        r36 = r0.append(r4);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "%), (exifExtracting time : ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalExifExtractingTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r0;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "ms ~";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r36;
        r36 = r0.append(r14);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "%), (sefExtracting time : ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalSefExtractingTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r0;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "ms ~";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r36;
        r1 = r30;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "%), (bulkInserter time : ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalBulkInserterTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r0;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "ms ~";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r36;
        r36 = r0.append(r8);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "%)\n";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r36.toString();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        android.util.Slog.v(r35, r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = "MediaScanner";
        r36 = new java.lang.StringBuilder;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36.<init>();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "postscan time: ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r12 - r28;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "ms (bulkDeleter : ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r36;
        r1 = r24;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "), (delete DeadThumbnail time : ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalDeadThumbnailTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r0;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "ms)\n";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r36.toString();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        android.util.Slog.v(r35, r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = "MediaScanner";
        r36 = new java.lang.StringBuilder;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36.<init>();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "   total time: ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r12 - r32;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "ms\n";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r36.toString();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        android.util.Slog.v(r35, r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = "MediaScanner";
        r36 = new java.lang.StringBuilder;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36.<init>();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "checked files: ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalCheckedFiles;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r0;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "  directories : ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalCheckedDirectories;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r0;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = "  (I: ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalInserted;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r0;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = ", U: ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalUpdated;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r38 = r0;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = ")\n";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r36.toString();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        android.util.Slog.v(r35, r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = new java.lang.StringBuilder;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35.<init>();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = "Volume:";
        r35 = r35.append(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r35;
        r1 = r42;
        r35 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = "/Current DB items:";
        r35 = r35.append(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.contentsCount;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r35 = r35.append(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = "/Checked files:";
        r35 = r35.append(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalCheckedFiles;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r35 = r35.append(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = "/Directories:";
        r35 = r35.append(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalCheckedDirectories;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r35 = r35.append(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = " (I:";
        r35 = r35.append(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalInserted;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r35 = r35.append(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = "/U:";
        r35 = r35.append(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mTotalUpdated;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r35 = r35.append(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = "/D:";
        r35 = r35.append(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r35;
        r1 = r24;
        r35 = r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = ")";
        r35 = r35.append(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r26 = r35.toString();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r1 = r26;
        r2 = r42;
        r0.printToMediaProviderDBLog(r1, r2);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r27 = new java.lang.StringBuffer;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r27.<init>();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mFileParsingStat;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = r0;
        r35 = r35.values();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r17 = r35.iterator();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
    L_0x044e:
        r35 = r17.hasNext();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        if (r35 == 0) goto L_0x0476;
    L_0x0454:
        r34 = r17.next();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r34 = (android.media.MediaScanner.FileParsingTime) r34;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = r34.toString();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r27;
        r1 = r35;
        r0.append(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        goto L_0x044e;
    L_0x0466:
        r10 = move-exception;
        r35 = "MediaScanner";
        r36 = "SQLException in MediaScanner.scan()";
        r0 = r35;
        r1 = r36;
        android.util.Log.e(r0, r1, r10);	 Catch:{ all -> 0x0561 }
        r40.releaseResources();
    L_0x0475:
        return;
    L_0x0476:
        if (r27 == 0) goto L_0x048b;
    L_0x0478:
        r35 = r27.length();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        if (r35 <= 0) goto L_0x048b;
    L_0x047e:
        r35 = r27.toString();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r1 = r35;
        r2 = r42;
        r0.printToMediaProviderDBLog(r1, r2);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
    L_0x048b:
        r0 = r40;
        r0 = r0.mTotalParsingTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r38 = 0;
        r35 = (r36 > r38 ? 1 : (r36 == r38 ? 0 : -1));
        if (r35 != 0) goto L_0x04af;
    L_0x0497:
        r0 = r40;
        r0 = r0.mTotalBitmapDecodingTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r38 = 0;
        r35 = (r36 > r38 ? 1 : (r36 == r38 ? 0 : -1));
        if (r35 != 0) goto L_0x04af;
    L_0x04a3:
        r0 = r40;
        r0 = r0.mTotalExifExtractingTime;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r38 = 0;
        r35 = (r36 > r38 ? 1 : (r36 == r38 ? 0 : -1));
        if (r35 == 0) goto L_0x04cb;
    L_0x04af:
        r35 = "MediaScanner";
        r36 = new java.lang.StringBuilder;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36.<init>();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = " parsing stat: ";
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r37 = r27.toString();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r36.append(r37);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r36.toString();	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        android.util.Slog.v(r35, r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
    L_0x04cb:
        r35 = 0;
        r0 = r35;
        r1 = r40;
        r1.mFileParsingStat = r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = 0;
        r0 = r36;
        r2 = r40;
        r2.mTotalParsingTime = r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = 0;
        r0 = r36;
        r2 = r40;
        r2.mTotalBitmapDecodingTime = r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = 0;
        r0 = r36;
        r2 = r40;
        r2.mTotalExifExtractingTime = r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = 0;
        r0 = r36;
        r2 = r40;
        r2.mTotalBulkInserterTime = r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = 0;
        r0 = r36;
        r2 = r40;
        r2.mTotalCheckedFiles = r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = 0;
        r0 = r36;
        r2 = r40;
        r2.mTotalCheckedDirectories = r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = 0;
        r0 = r36;
        r2 = r40;
        r2.mTotalInserted = r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = 0;
        r0 = r36;
        r2 = r40;
        r2.mTotalUpdated = r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = 0;
        r0 = r35;
        r1 = r40;
        r1.useHashMap = r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = 0;
        r0 = r35;
        r1 = r40;
        r1.contentsCount = r0;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r35 = 0;
        r35 = r41[r35];	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r0 = r40;
        r0 = r0.mInternalStoragePath;	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        r36 = r0;
        r35 = r35.startsWith(r36);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
        if (r35 == 0) goto L_0x053a;
    L_0x0533:
        r0 = r40;
        r1 = r42;
        r0.checkDefaultSounds(r1);	 Catch:{ SQLException -> 0x0466, UnsupportedOperationException -> 0x053f, RemoteException -> 0x0550 }
    L_0x053a:
        r40.releaseResources();
        goto L_0x0475;
    L_0x053f:
        r10 = move-exception;
        r35 = "MediaScanner";
        r36 = "UnsupportedOperationException in MediaScanner.scan()";
        r0 = r35;
        r1 = r36;
        android.util.Log.e(r0, r1, r10);	 Catch:{ all -> 0x0561 }
        r40.releaseResources();
        goto L_0x0475;
    L_0x0550:
        r10 = move-exception;
        r35 = "MediaScanner";
        r36 = "RemoteException in MediaScanner.scan()";
        r0 = r35;
        r1 = r36;
        android.util.Log.e(r0, r1, r10);	 Catch:{ all -> 0x0561 }
        r40.releaseResources();
        goto L_0x0475;
    L_0x0561:
        r35 = move-exception;
        r40.releaseResources();
        throw r35;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.scanDirectories(java.lang.String[], java.lang.String):void");
    }

    public Uri scanSingleFile(String path, String volumeName, String mimeType) {
        Uri doScanFile;
        try {
            Log.d(TAG, "scanSingleFile : " + encodeStringResource(path));
            initialize(volumeName);
            prescan(path, true, getExternalStorageSdPath(this.mContext));
            File file = new File(path);
            if (file.exists()) {
                String str = path;
                String str2 = mimeType;
                doScanFile = this.mClient.doScanFile(str, str2, file.lastModified() / 1000, file.length(), false, true, isNoMediaPath(path));
                releaseResources();
                return doScanFile;
            }
            doScanFile = null;
            return doScanFile;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in MediaScanner.scanFile()", e);
            doScanFile = null;
        } finally {
            releaseResources();
        }
    }

    private static boolean isNoMediaFile(String path) {
        if (new File(path).isDirectory()) {
            return false;
        }
        int lastSlash = path.lastIndexOf(47);
        if (lastSlash >= 0 && lastSlash + 2 < path.length()) {
            if (path.regionMatches(lastSlash + 1, "._", 0, 2)) {
                return true;
            }
            if (path.regionMatches(true, path.length() - 4, ".jpg", 0, 4)) {
                if (path.regionMatches(true, lastSlash + 1, "AlbumArt_{", 0, 10)) {
                    return true;
                }
                if (path.regionMatches(true, lastSlash + 1, "AlbumArt.", 0, 9)) {
                    return true;
                }
                int length = (path.length() - lastSlash) - 1;
                if (length == 17) {
                    if (path.regionMatches(true, lastSlash + 1, "AlbumArtSmall", 0, 13)) {
                        return true;
                    }
                }
                if (length == 10) {
                    if (path.regionMatches(true, lastSlash + 1, "Folder", 0, 6)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void clearMediaPathCache(boolean clearMediaPaths, boolean clearNoMediaPaths) {
        synchronized (MediaScanner.class) {
            if (clearMediaPaths) {
                mMediaPaths.clear();
            }
            if (clearNoMediaPaths) {
                mNoMediaPaths.clear();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isNoMediaPath(java.lang.String r9) {
        /*
        r8 = 47;
        r6 = 1;
        r5 = 0;
        if (r9 != 0) goto L_0x0007;
    L_0x0006:
        return r5;
    L_0x0007:
        r7 = "/.";
        r7 = r9.indexOf(r7);
        if (r7 < 0) goto L_0x0011;
    L_0x000f:
        r5 = r6;
        goto L_0x0006;
    L_0x0011:
        r1 = r9.lastIndexOf(r8);
        if (r1 <= 0) goto L_0x0006;
    L_0x0017:
        r3 = r9.substring(r5, r1);
        r7 = android.media.MediaScanner.class;
        monitor-enter(r7);
        r5 = mNoMediaPaths;	 Catch:{ all -> 0x007a }
        r5 = r5.containsKey(r3);	 Catch:{ all -> 0x007a }
        if (r5 == 0) goto L_0x0029;
    L_0x0026:
        monitor-exit(r7);	 Catch:{ all -> 0x007a }
        r5 = r6;
        goto L_0x0006;
    L_0x0029:
        r5 = mMediaPaths;	 Catch:{ all -> 0x007a }
        r5 = r5.containsKey(r3);	 Catch:{ all -> 0x007a }
        if (r5 != 0) goto L_0x0074;
    L_0x0031:
        r2 = 1;
    L_0x0032:
        if (r2 < 0) goto L_0x006d;
    L_0x0034:
        r5 = 47;
        r4 = r9.indexOf(r5, r2);	 Catch:{ all -> 0x007a }
        if (r4 <= r2) goto L_0x006b;
    L_0x003c:
        r4 = r4 + 1;
        r0 = new java.io.File;	 Catch:{ all -> 0x007a }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x007a }
        r5.<init>();	 Catch:{ all -> 0x007a }
        r8 = 0;
        r8 = r9.substring(r8, r4);	 Catch:{ all -> 0x007a }
        r5 = r5.append(r8);	 Catch:{ all -> 0x007a }
        r8 = ".nomedia";
        r5 = r5.append(r8);	 Catch:{ all -> 0x007a }
        r5 = r5.toString();	 Catch:{ all -> 0x007a }
        r0.<init>(r5);	 Catch:{ all -> 0x007a }
        r5 = r0.exists();	 Catch:{ all -> 0x007a }
        if (r5 == 0) goto L_0x006b;
    L_0x0061:
        r5 = mNoMediaPaths;	 Catch:{ all -> 0x007a }
        r8 = "";
        r5.put(r3, r8);	 Catch:{ all -> 0x007a }
        monitor-exit(r7);	 Catch:{ all -> 0x007a }
        r5 = r6;
        goto L_0x0006;
    L_0x006b:
        r2 = r4;
        goto L_0x0032;
    L_0x006d:
        r5 = mMediaPaths;	 Catch:{ all -> 0x007a }
        r6 = "";
        r5.put(r3, r6);	 Catch:{ all -> 0x007a }
    L_0x0074:
        monitor-exit(r7);	 Catch:{ all -> 0x007a }
        r5 = isNoMediaFile(r9);
        goto L_0x0006;
    L_0x007a:
        r5 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x007a }
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.isNoMediaPath(java.lang.String):boolean");
    }

    FileEntry makeEntryFor(String path) {
        Cursor c = null;
        try {
            String[] selectionArgs = new String[]{path};
            c = this.mMediaProvider.query(this.mPackageName, this.mFilesUriNoNotify, FILES_PRESCAN_PROJECTION, "_data=?", selectionArgs, null, null);
            if (c.moveToFirst()) {
                FileEntry fileEntry = new FileEntry(c.getLong(0), path, c.getLong(3));
                if (c == null) {
                    return fileEntry;
                }
                c.close();
                return fileEntry;
            }
            if (c != null) {
                c.close();
            }
            return null;
        } catch (RemoteException e) {
            if (c != null) {
                c.close();
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
    }

    private int matchPaths(String path1, String path2) {
        int result = 0;
        int end1 = path1.length();
        int end2 = path2.length();
        while (end1 > 0 && end2 > 0) {
            int start1;
            int start2;
            int slash1 = path1.lastIndexOf(47, end1 - 1);
            int slash2 = path2.lastIndexOf(47, end2 - 1);
            int backSlash1 = path1.lastIndexOf(92, end1 - 1);
            int backSlash2 = path2.lastIndexOf(92, end2 - 1);
            if (slash1 > backSlash1) {
                start1 = slash1;
            } else {
                start1 = backSlash1;
            }
            if (slash2 > backSlash2) {
                start2 = slash2;
            } else {
                start2 = backSlash2;
            }
            start1 = start1 < 0 ? 0 : start1 + 1;
            start2 = start2 < 0 ? 0 : start2 + 1;
            int length = end1 - start1;
            if (end2 - start2 != length || !path1.regionMatches(true, start1, path2, start2, length)) {
                break;
            }
            result++;
            end1 = start1 - 1;
            end2 = start2 - 1;
        }
        return result;
    }

    private boolean matchEntries(long rowId, String data) {
        int len = this.mPlaylistEntries.size();
        boolean done = true;
        for (int i = 0; i < len; i++) {
            PlaylistEntry entry = (PlaylistEntry) this.mPlaylistEntries.get(i);
            if (entry.bestmatchlevel != Integer.MAX_VALUE) {
                done = false;
                if (data.equalsIgnoreCase(entry.path)) {
                    entry.bestmatchid = rowId;
                    entry.bestmatchlevel = Integer.MAX_VALUE;
                } else {
                    int matchLength = matchPaths(data, entry.path);
                    if (matchLength > entry.bestmatchlevel) {
                        entry.bestmatchid = rowId;
                        entry.bestmatchlevel = matchLength;
                    }
                }
            }
        }
        return done;
    }

    private void cachePlaylistEntry(String line, String playListDirectory) {
        boolean fullPath = false;
        PlaylistEntry entry = new PlaylistEntry();
        int entryLength = line.length();
        while (entryLength > 0 && Character.isWhitespace(line.charAt(entryLength - 1))) {
            entryLength--;
        }
        if (entryLength >= 3) {
            if (entryLength < line.length()) {
                line = line.substring(0, entryLength);
            }
            char ch1 = line.charAt(0);
            if (ch1 == '/' || (Character.isLetter(ch1) && line.charAt(1) == ':' && line.charAt(2) == '\\')) {
                fullPath = true;
            }
            if (!fullPath) {
                line = playListDirectory + line;
            }
            entry.path = line;
            this.mPlaylistEntries.add(entry);
        }
    }

    private void processCachedPlaylist(Cursor fileList, ContentValues values, Uri playlistUri) {
        fileList.moveToPosition(-1);
        while (fileList.moveToNext()) {
            if (matchEntries(fileList.getLong(0), fileList.getString(1))) {
                break;
            }
        }
        int len = this.mPlaylistEntries.size();
        int index = 0;
        for (int i = 0; i < len; i++) {
            PlaylistEntry entry = (PlaylistEntry) this.mPlaylistEntries.get(i);
            if (entry.bestmatchlevel > 0) {
                try {
                    values.clear();
                    values.put("play_order", Integer.valueOf(index));
                    values.put("audio_id", Long.valueOf(entry.bestmatchid));
                    this.mMediaProvider.insert(this.mPackageName, playlistUri, values);
                    index++;
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in MediaScanner.processCachedPlaylist()", e);
                    return;
                }
            }
        }
        this.mPlaylistEntries.clear();
    }

    private void processM3uPlayList(String path, String playListDirectory, Uri uri, ContentValues values, Cursor fileList) {
        IOException e;
        Throwable th;
        BufferedReader reader = null;
        try {
            File f = new File(path);
            if (f.exists()) {
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(f)), 8192);
                try {
                    String line = reader2.readLine();
                    this.mPlaylistEntries.clear();
                    while (line != null) {
                        if (line.length() > 0 && line.charAt(0) != '#') {
                            cachePlaylistEntry(line, playListDirectory);
                        }
                        line = reader2.readLine();
                    }
                    processCachedPlaylist(fileList, values, uri);
                    reader = reader2;
                } catch (IOException e2) {
                    e = e2;
                    reader = reader2;
                    try {
                        Log.e(TAG, "IOException in MediaScanner.processM3uPlayList()", e);
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e3) {
                                Log.e(TAG, "IOException in MediaScanner.processM3uPlayList()", e3);
                                return;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e32) {
                                Log.e(TAG, "IOException in MediaScanner.processM3uPlayList()", e32);
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    reader = reader2;
                    if (reader != null) {
                        reader.close();
                    }
                    throw th;
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e322) {
                    Log.e(TAG, "IOException in MediaScanner.processM3uPlayList()", e322);
                }
            }
        } catch (IOException e4) {
            e322 = e4;
            Log.e(TAG, "IOException in MediaScanner.processM3uPlayList()", e322);
            if (reader != null) {
                reader.close();
            }
        }
    }

    private void processPlsPlayList(String path, String playListDirectory, Uri uri, ContentValues values, Cursor fileList) {
        IOException e;
        Throwable th;
        BufferedReader reader = null;
        try {
            File f = new File(path);
            if (f.exists()) {
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(f)), 8192);
                try {
                    this.mPlaylistEntries.clear();
                    for (String line = reader2.readLine(); line != null; line = reader2.readLine()) {
                        if (line.startsWith("File")) {
                            int equals = line.indexOf(61);
                            if (equals > 0) {
                                cachePlaylistEntry(line.substring(equals + 1), playListDirectory);
                            }
                        }
                    }
                    processCachedPlaylist(fileList, values, uri);
                    reader = reader2;
                } catch (IOException e2) {
                    e = e2;
                    reader = reader2;
                    try {
                        Log.e(TAG, "IOException in MediaScanner.processPlsPlayList()", e);
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e3) {
                                Log.e(TAG, "IOException in MediaScanner.processPlsPlayList()", e3);
                                return;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e32) {
                                Log.e(TAG, "IOException in MediaScanner.processPlsPlayList()", e32);
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    reader = reader2;
                    if (reader != null) {
                        reader.close();
                    }
                    throw th;
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e322) {
                    Log.e(TAG, "IOException in MediaScanner.processPlsPlayList()", e322);
                }
            }
        } catch (IOException e4) {
            e322 = e4;
            Log.e(TAG, "IOException in MediaScanner.processPlsPlayList()", e322);
            if (reader != null) {
                reader.close();
            }
        }
    }

    private void processWplPlayList(String path, String playListDirectory, Uri uri, ContentValues values, Cursor fileList) {
        SAXException e;
        IOException e2;
        Throwable th;
        FileInputStream fis = null;
        try {
            File f = new File(path);
            if (f.exists()) {
                FileInputStream fis2 = new FileInputStream(f);
                try {
                    this.mPlaylistEntries.clear();
                    Xml.parse(fis2, Xml.findEncodingByName("UTF-8"), new WplHandler(playListDirectory, uri, fileList).getContentHandler());
                    processCachedPlaylist(fileList, values, uri);
                    fis = fis2;
                } catch (SAXException e3) {
                    e = e3;
                    fis = fis2;
                    try {
                        e.printStackTrace();
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e22) {
                                Log.e(TAG, "IOException in MediaScanner.processWplPlayList()", e22);
                                return;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e222) {
                                Log.e(TAG, "IOException in MediaScanner.processWplPlayList()", e222);
                            }
                        }
                        throw th;
                    }
                } catch (IOException e4) {
                    e222 = e4;
                    fis = fis2;
                    e222.printStackTrace();
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e2222) {
                            Log.e(TAG, "IOException in MediaScanner.processWplPlayList()", e2222);
                            return;
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fis = fis2;
                    if (fis != null) {
                        fis.close();
                    }
                    throw th;
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e22222) {
                    Log.e(TAG, "IOException in MediaScanner.processWplPlayList()", e22222);
                }
            }
        } catch (SAXException e5) {
            e = e5;
            e.printStackTrace();
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e6) {
            e22222 = e6;
            e22222.printStackTrace();
            if (fis != null) {
                fis.close();
            }
        }
    }

    private void processPlayList(FileEntry entry, Cursor fileList) throws RemoteException {
        String path = entry.mPath;
        ContentValues values = new ContentValues();
        int lastSlash = path.lastIndexOf(47);
        if (lastSlash < 0) {
            throw new IllegalArgumentException("bad path " + path);
        }
        Uri membersUri;
        long rowId = entry.mRowId;
        String name = values.getAsString("name");
        if (name == null) {
            name = values.getAsString("title");
            if (name == null) {
                int lastDot = path.lastIndexOf(46);
                if (lastDot < 0) {
                    name = path.substring(lastSlash + 1);
                } else {
                    name = path.substring(lastSlash + 1, lastDot);
                }
            }
        }
        values.put("name", name);
        values.put("date_modified", Long.valueOf(entry.mLastModified));
        values.put("media_type", Integer.valueOf(4));
        Uri uri;
        if (rowId == 0) {
            values.put("_data", path);
            uri = this.mMediaProvider.insert(this.mPackageName, this.mPlaylistsUri, values);
            rowId = ContentUris.parseId(uri);
            membersUri = Uri.withAppendedPath(uri, "members");
        } else {
            uri = ContentUris.withAppendedId(this.mPlaylistsUri, rowId);
            this.mMediaProvider.update(this.mPackageName, uri, values, null, null);
            membersUri = Uri.withAppendedPath(uri, "members");
            this.mMediaProvider.delete(this.mPackageName, membersUri, null, null);
        }
        String playListDirectory = path.substring(0, lastSlash + 1);
        MediaFileType mediaFileType = MediaFile.getFileType(path);
        int fileType = mediaFileType == null ? 0 : mediaFileType.fileType;
        if (fileType == 41) {
            processM3uPlayList(path, playListDirectory, membersUri, values, fileList);
        } else if (fileType == 42) {
            processPlsPlayList(path, playListDirectory, membersUri, values, fileList);
        } else if (fileType == 43) {
            processWplPlayList(path, playListDirectory, membersUri, values, fileList);
        }
    }

    public void release() {
        native_finalize();
    }

    protected void finalize() {
        this.mContext.getContentResolver().releaseProvider(this.mMediaProvider);
        native_finalize();
    }

    public void checkDefaultSounds(String volumeName) {
        Slog.d(TAG, "checkDefaultSounds");
        Uri URIdefaultSound = Audio.Media.getContentUri(volumeName);
        Uri URIsetting = System.CONTENT_URI;
        ContentResolver cr = this.mContext.getContentResolver();
        if (cr == null) {
            Slog.d(TAG, "Context resolver is null!!!");
            return;
        }
        setDefaultRingtoneFileNames();
        SettingDefaultSoundAgain(cr, URIsetting, URIdefaultSound, "alarm_alert", this.mDefaultAlarmAlertFilename);
        SettingDefaultSoundAgain(cr, URIsetting, URIdefaultSound, "notification_sound", this.mDefaultNotificationFilename);
        SettingDefaultSoundAgain(cr, URIsetting, URIdefaultSound, "ringtone", this.mDefaultRingtoneFilename);
        if (MultiSimManager.getSimSlotCount() > 1) {
            SettingDefaultSoundAgain(cr, URIsetting, URIdefaultSound, "ringtone_2", this.mDefaultRingtoneFilename);
            SettingDefaultSoundAgain(cr, URIsetting, URIdefaultSound, "notification_sound_2", this.mDefaultNotificationFilename);
        }
    }

    private void SettingDefaultSoundAgain(ContentResolver cr, Uri URIsetting, Uri URIInternalMedia, String settingString, String defaultMediaName) {
        Slog.d(TAG, "system " + settingString + "  : " + encodeStringResource(defaultMediaName));
        Cursor resultSettingCursor = cr.query(URIsetting, null, "name = '" + settingString + "'", null, null);
        if (resultSettingCursor != null) {
            if (resultSettingCursor.getCount() == 0) {
                Cursor resultMediaCursor = cr.query(URIInternalMedia, null, "_data like '%" + defaultMediaName + "'", null, null);
                if (resultMediaCursor != null) {
                    if (resultMediaCursor.getCount() >= 1) {
                        resultMediaCursor.moveToFirst();
                        long rowId = resultMediaCursor.getLong(0);
                        Slog.d(TAG, "Write again. Default " + settingString + " is [" + resultMediaCursor.getLong(0) + "]  Result is [" + System.putString(cr, settingString, ContentUris.withAppendedId(URIInternalMedia, rowId).toString()) + "]");
                        System.putString(cr, "DEBUG_MEDIASCANNER_" + settingString + "_AGAIN", "MediaScanner : " + ContentUris.withAppendedId(URIInternalMedia, rowId).toString());
                    } else {
                        Slog.d(TAG, "Warning! getCount() of Cursor for " + settingString + " in internal DB is [" + resultMediaCursor.getCount() + "]");
                    }
                    resultMediaCursor.close();
                } else {
                    Slog.d(TAG, "Warning! Cursor for " + settingString + " in internal DB is null.");
                }
            } else {
                Slog.d(TAG, "OK. " + settingString + " is already exist in setting DB.");
            }
            resultSettingCursor.close();
            return;
        }
        Slog.d(TAG, "Warning! Cursor for " + settingString + " in setting DB is null.");
    }
}
