package android.provider;

import android.app.backup.FullBackup;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.MiniThumbFile;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.ParcelFileDescriptor;
import android.provider.Contacts.GroupMembership;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public final class MediaStore {
    public static final String ACTION_IMAGE_CAPTURE = "android.media.action.IMAGE_CAPTURE";
    public static final String ACTION_IMAGE_CAPTURE_SECURE = "android.media.action.IMAGE_CAPTURE_SECURE";
    public static final String ACTION_MTP_SESSION_END = "android.provider.action.MTP_SESSION_END";
    public static final String ACTION_VIDEO_CAPTURE = "android.media.action.VIDEO_CAPTURE";
    public static final String AUTHORITY = "media";
    private static final String CONTENT_AUTHORITY_SLASH = "content://media/";
    public static final String EXTRA_DURATION_LIMIT = "android.intent.extra.durationLimit";
    public static final String EXTRA_FINISH_ON_COMPLETION = "android.intent.extra.finishOnCompletion";
    public static final String EXTRA_FULL_SCREEN = "android.intent.extra.fullScreen";
    public static final String EXTRA_MEDIA_ALBUM = "android.intent.extra.album";
    public static final String EXTRA_MEDIA_ARTIST = "android.intent.extra.artist";
    public static final String EXTRA_MEDIA_FOCUS = "android.intent.extra.focus";
    public static final String EXTRA_MEDIA_GENRE = "android.intent.extra.genre";
    public static final String EXTRA_MEDIA_PLAYLIST = "android.intent.extra.playlist";
    public static final String EXTRA_MEDIA_RADIO_CHANNEL = "android.intent.extra.radio_channel";
    public static final String EXTRA_MEDIA_TITLE = "android.intent.extra.title";
    public static final String EXTRA_OUTPUT = "output";
    public static final String EXTRA_SCREEN_ORIENTATION = "android.intent.extra.screenOrientation";
    public static final String EXTRA_SHOW_ACTION_ICONS = "android.intent.extra.showActionIcons";
    public static final String EXTRA_SIZE_LIMIT = "android.intent.extra.sizeLimit";
    public static final String EXTRA_VIDEO_QUALITY = "android.intent.extra.videoQuality";
    public static final String INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH = "android.media.action.MEDIA_PLAY_FROM_SEARCH";
    public static final String INTENT_ACTION_MEDIA_SEARCH = "android.intent.action.MEDIA_SEARCH";
    @Deprecated
    public static final String INTENT_ACTION_MUSIC_PLAYER = "android.intent.action.MUSIC_PLAYER";
    public static final String INTENT_ACTION_STILL_IMAGE_CAMERA = "android.media.action.STILL_IMAGE_CAMERA";
    public static final String INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE = "android.media.action.STILL_IMAGE_CAMERA_SECURE";
    public static final String INTENT_ACTION_TEXT_OPEN_FROM_SEARCH = "android.media.action.TEXT_OPEN_FROM_SEARCH";
    public static final String INTENT_ACTION_VIDEO_CAMERA = "android.media.action.VIDEO_CAMERA";
    public static final String INTENT_ACTION_VIDEO_PLAY_FROM_SEARCH = "android.media.action.VIDEO_PLAY_FROM_SEARCH";
    public static final String MEDIA_IGNORE_FILENAME = ".nomedia";
    public static final String MEDIA_SCANNER_VOLUME = "volume";
    public static final String META_DATA_STILL_IMAGE_CAMERA_PREWARM_SERVICE = "android.media.still_image_camera_preview_service";
    public static final String PARAM_DELETE_DATA = "deletedata";
    private static final String TAG = "MediaStore";
    public static final String UNHIDE_CALL = "unhide";
    public static final String UNKNOWN_STRING = "<unknown>";

    public interface MediaColumns extends BaseColumns {
        public static final String DATA = "_data";
        public static final String DATE_ADDED = "date_added";
        public static final String DATE_MODIFIED = "date_modified";
        public static final String DISPLAY_NAME = "_display_name";
        public static final String HEIGHT = "height";
        public static final String IS_DRM = "is_drm";
        public static final String MEDIA_SCANNER_NEW_OBJECT_ID = "media_scanner_new_object_id";
        public static final String MIME_TYPE = "mime_type";
        public static final String SIZE = "_size";
        public static final String TITLE = "title";
        public static final String WIDTH = "width";
    }

    public static final class Audio {

        public interface AlbumColumns {
            public static final String ALBUM = "album";
            public static final String ALBUM_ART = "album_art";
            public static final String ALBUM_ID = "album_id";
            public static final String ALBUM_KEY = "album_key";
            public static final String ARTIST = "artist";
            public static final String FIRST_YEAR = "minyear";
            public static final String LAST_YEAR = "maxyear";
            public static final String NUMBER_OF_SONGS = "numsongs";
            public static final String NUMBER_OF_SONGS_FOR_ARTIST = "numsongs_by_artist";
        }

        public static final class Albums implements BaseColumns, AlbumColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/albums";
            public static final String DEFAULT_SORT_ORDER = "album_key";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/album";
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");

            public static android.net.Uri getContentUri(java.lang.String r2) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r0 = new java.lang.StringBuilder;
                r0.<init>();
                r1 = "content://media/";
                r0 = r0.append(r1);
                r0 = r0.append(r2);
                r1 = "/audio/albums";
                r0 = r0.append(r1);
                r0 = r0.toString();
                r0 = android.net.Uri.parse(r0);
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.Audio.Albums.getContentUri(java.lang.String):android.net.Uri");
            }
        }

        public interface ArtistColumns {
            public static final String ARTIST = "artist";
            public static final String ARTIST_KEY = "artist_key";
            public static final String NUMBER_OF_ALBUMS = "number_of_albums";
            public static final String NUMBER_OF_TRACKS = "number_of_tracks";
        }

        public static final class Artists implements BaseColumns, ArtistColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/artists";
            public static final String DEFAULT_SORT_ORDER = "artist_key";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/artist";
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");

            public static final class Albums implements AlbumColumns {
                public static final android.net.Uri getContentUri(java.lang.String r3, long r4) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r0 = new java.lang.StringBuilder;
                    r0.<init>();
                    r1 = "content://media/";
                    r0 = r0.append(r1);
                    r0 = r0.append(r3);
                    r1 = "/audio/artists/";
                    r0 = r0.append(r1);
                    r0 = r0.append(r4);
                    r1 = "/albums";
                    r0 = r0.append(r1);
                    r0 = r0.toString();
                    r0 = android.net.Uri.parse(r0);
                    return r0;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.Audio.Artists.Albums.getContentUri(java.lang.String, long):android.net.Uri");
                }
            }

            public static android.net.Uri getContentUri(java.lang.String r2) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r0 = new java.lang.StringBuilder;
                r0.<init>();
                r1 = "content://media/";
                r0 = r0.append(r1);
                r0 = r0.append(r2);
                r1 = "/audio/artists";
                r0 = r0.append(r1);
                r0 = r0.toString();
                r0 = android.net.Uri.parse(r0);
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.Audio.Artists.getContentUri(java.lang.String):android.net.Uri");
            }
        }

        public interface AudioColumns extends MediaColumns {
            public static final String ALBUM = "album";
            public static final String ALBUM_ARTIST = "album_artist";
            public static final String ALBUM_ID = "album_id";
            public static final String ALBUM_KEY = "album_key";
            public static final String ARTIST = "artist";
            public static final String ARTIST_ID = "artist_id";
            public static final String ARTIST_KEY = "artist_key";
            public static final String BOOKMARK = "bookmark";
            public static final String COMPILATION = "compilation";
            public static final String COMPOSER = "composer";
            public static final String DURATION = "duration";
            public static final String GENRE = "genre";
            public static final String IS_ALARM = "is_alarm";
            public static final String IS_ALARM_THEME = "is_alarm_theme";
            public static final String IS_MUSIC = "is_music";
            public static final String IS_NOTIFICATION = "is_notification";
            public static final String IS_NOTIFICATION_THEME = "is_notification_theme";
            public static final String IS_PODCAST = "is_podcast";
            public static final String IS_RINGTONE = "is_ringtone";
            public static final String IS_RINGTONE_THEME = "is_ringtone_theme";
            public static final String TITLE_KEY = "title_key";
            public static final String TRACK = "track";
            public static final String YEAR = "year";
        }

        public interface GenresColumns {
            public static final String NAME = "name";
        }

        public static final class Genres implements BaseColumns, GenresColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/genre";
            public static final String DEFAULT_SORT_ORDER = "name";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/genre";
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");

            public static final class Members implements AudioColumns {
                public static final String AUDIO_ID = "audio_id";
                public static final String CONTENT_DIRECTORY = "members";
                public static final String DEFAULT_SORT_ORDER = "title_key";
                public static final String GENRE_ID = "genre_id";

                public static final android.net.Uri getContentUri(java.lang.String r3, long r4) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r0 = new java.lang.StringBuilder;
                    r0.<init>();
                    r1 = "content://media/";
                    r0 = r0.append(r1);
                    r0 = r0.append(r3);
                    r1 = "/audio/genres/";
                    r0 = r0.append(r1);
                    r0 = r0.append(r4);
                    r1 = "/members";
                    r0 = r0.append(r1);
                    r0 = r0.toString();
                    r0 = android.net.Uri.parse(r0);
                    return r0;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.Audio.Genres.Members.getContentUri(java.lang.String, long):android.net.Uri");
                }
            }

            public static android.net.Uri getContentUri(java.lang.String r2) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r0 = new java.lang.StringBuilder;
                r0.<init>();
                r1 = "content://media/";
                r0 = r0.append(r1);
                r0 = r0.append(r2);
                r1 = "/audio/genres";
                r0 = r0.append(r1);
                r0 = r0.toString();
                r0 = android.net.Uri.parse(r0);
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.Audio.Genres.getContentUri(java.lang.String):android.net.Uri");
            }

            public static android.net.Uri getContentUriForAudioId(java.lang.String r2, int r3) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r0 = new java.lang.StringBuilder;
                r0.<init>();
                r1 = "content://media/";
                r0 = r0.append(r1);
                r0 = r0.append(r2);
                r1 = "/audio/media/";
                r0 = r0.append(r1);
                r0 = r0.append(r3);
                r1 = "/genres";
                r0 = r0.append(r1);
                r0 = r0.toString();
                r0 = android.net.Uri.parse(r0);
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.Audio.Genres.getContentUriForAudioId(java.lang.String, int):android.net.Uri");
            }
        }

        public static final class Media implements AudioColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/audio";
            public static final String DEFAULT_SORT_ORDER = "title_key";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/audio";
            public static final Uri EXTERNAL_CONTENT_URI;
            private static final String[] EXTERNAL_PATHS;
            public static final String EXTRA_MAX_BYTES = "android.provider.MediaStore.extra.MAX_BYTES";
            public static final Uri INTERNAL_CONTENT_URI;
            public static final String RECORD_SOUND_ACTION = "android.provider.MediaStore.RECORD_SOUND";

            static {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = "SECONDARY_STORAGE";
                r0 = java.lang.System.getenv(r1);
                if (r0 == 0) goto L_0x0022;
            L_0x0008:
                r1 = ":";
                r1 = r0.split(r1);
                EXTERNAL_PATHS = r1;
            L_0x0010:
                r1 = "internal";
                r1 = getContentUri(r1);
                INTERNAL_CONTENT_URI = r1;
                r1 = "external";
                r1 = getContentUri(r1);
                EXTERNAL_CONTENT_URI = r1;
                return;
            L_0x0022:
                r1 = 0;
                r1 = new java.lang.String[r1];
                EXTERNAL_PATHS = r1;
                goto L_0x0010;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.Audio.Media.<clinit>():void");
            }

            public static android.net.Uri getContentUri(java.lang.String r2) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r0 = new java.lang.StringBuilder;
                r0.<init>();
                r1 = "content://media/";
                r0 = r0.append(r1);
                r0 = r0.append(r2);
                r1 = "/audio/media";
                r0 = r0.append(r1);
                r0 = r0.toString();
                r0 = android.net.Uri.parse(r0);
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.Audio.Media.getContentUri(java.lang.String):android.net.Uri");
            }

            public static android.net.Uri getContentUriForPath(java.lang.String r6) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r0 = EXTERNAL_PATHS;
                r3 = r0.length;
                r2 = 0;
            L_0x0004:
                if (r2 >= r3) goto L_0x0014;
            L_0x0006:
                r1 = r0[r2];
                r4 = r6.startsWith(r1);
                if (r4 == 0) goto L_0x0011;
            L_0x000e:
                r4 = EXTERNAL_CONTENT_URI;
            L_0x0010:
                return r4;
            L_0x0011:
                r2 = r2 + 1;
                goto L_0x0004;
            L_0x0014:
                r4 = new java.lang.StringBuilder;
                r4.<init>();
                r5 = android.os.Environment.getRootDirectory();
                r5 = r5.getPath();
                r4 = r4.append(r5);
                r5 = "/media";
                r4 = r4.append(r5);
                r4 = r4.toString();
                r4 = r6.startsWith(r4);
                if (r4 != 0) goto L_0x0056;
            L_0x0035:
                r4 = new java.lang.StringBuilder;
                r4.<init>();
                r5 = android.os.Environment.getDataDirectory();
                r5 = r5.getPath();
                r4 = r4.append(r5);
                r5 = "/overlays";
                r4 = r4.append(r5);
                r4 = r4.toString();
                r4 = r6.startsWith(r4);
                if (r4 == 0) goto L_0x0059;
            L_0x0056:
                r4 = INTERNAL_CONTENT_URI;
                goto L_0x0010;
            L_0x0059:
                r4 = EXTERNAL_CONTENT_URI;
                goto L_0x0010;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.Audio.Media.getContentUriForPath(java.lang.String):android.net.Uri");
            }
        }

        public interface PlaylistsColumns {
            public static final String DATA = "_data";
            public static final String DATE_ADDED = "date_added";
            public static final String DATE_MODIFIED = "date_modified";
            public static final String NAME = "name";
        }

        public static final class Playlists implements BaseColumns, PlaylistsColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/playlist";
            public static final String DEFAULT_SORT_ORDER = "name";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/playlist";
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");

            public static final class Members implements AudioColumns {
                public static final String AUDIO_ID = "audio_id";
                public static final String CONTENT_DIRECTORY = "members";
                public static final String DEFAULT_SORT_ORDER = "play_order";
                public static final String PLAYLIST_ID = "playlist_id";
                public static final String PLAY_ORDER = "play_order";
                public static final String _ID = "_id";

                public static final android.net.Uri getContentUri(java.lang.String r3, long r4) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r0 = new java.lang.StringBuilder;
                    r0.<init>();
                    r1 = "content://media/";
                    r0 = r0.append(r1);
                    r0 = r0.append(r3);
                    r1 = "/audio/playlists/";
                    r0 = r0.append(r1);
                    r0 = r0.append(r4);
                    r1 = "/members";
                    r0 = r0.append(r1);
                    r0 = r0.toString();
                    r0 = android.net.Uri.parse(r0);
                    return r0;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.Audio.Playlists.Members.getContentUri(java.lang.String, long):android.net.Uri");
                }

                public static final boolean moveItem(android.content.ContentResolver r7, long r8, int r10, int r11) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r5 = 0;
                    r2 = "external";
                    r2 = getContentUri(r2, r8);
                    r2 = r2.buildUpon();
                    r3 = java.lang.String.valueOf(r10);
                    r2 = r2.appendEncodedPath(r3);
                    r3 = "move";
                    r4 = "true";
                    r2 = r2.appendQueryParameter(r3, r4);
                    r0 = r2.build();
                    r1 = new android.content.ContentValues;
                    r1.<init>();
                    r2 = "play_order";
                    r3 = java.lang.Integer.valueOf(r11);
                    r1.put(r2, r3);
                    r2 = r7.update(r0, r1, r5, r5);
                    if (r2 == 0) goto L_0x0038;
                L_0x0036:
                    r2 = 1;
                L_0x0037:
                    return r2;
                L_0x0038:
                    r2 = 0;
                    goto L_0x0037;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.Audio.Playlists.Members.moveItem(android.content.ContentResolver, long, int, int):boolean");
                }
            }

            public static android.net.Uri getContentUri(java.lang.String r2) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r0 = new java.lang.StringBuilder;
                r0.<init>();
                r1 = "content://media/";
                r0 = r0.append(r1);
                r0 = r0.append(r2);
                r1 = "/audio/playlists";
                r0 = r0.append(r1);
                r0 = r0.toString();
                r0 = android.net.Uri.parse(r0);
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.Audio.Playlists.getContentUri(java.lang.String):android.net.Uri");
            }
        }

        public static final class Radio {
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/radio";

            private Radio() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r0 = this;
                r0.<init>();
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.Audio.Radio.<init>():void");
            }
        }

        public static java.lang.String keyFor(java.lang.String r8) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r7 = 46;
            if (r8 == 0) goto L_0x00d5;
        L_0x0004:
            r4 = 0;
            r5 = "<unknown>";
            r5 = r8.equals(r5);
            if (r5 == 0) goto L_0x0010;
        L_0x000d:
            r2 = "\u0001";
        L_0x000f:
            return r2;
        L_0x0010:
            r5 = "\u0001";
            r5 = r8.startsWith(r5);
            if (r5 == 0) goto L_0x0019;
        L_0x0018:
            r4 = 1;
        L_0x0019:
            r5 = r8.trim();
            r8 = r5.toLowerCase();
            r5 = "the ";
            r5 = r8.startsWith(r5);
            if (r5 == 0) goto L_0x002f;
        L_0x002a:
            r5 = 4;
            r8 = r8.substring(r5);
        L_0x002f:
            r5 = "an ";
            r5 = r8.startsWith(r5);
            if (r5 == 0) goto L_0x003c;
        L_0x0037:
            r5 = 3;
            r8 = r8.substring(r5);
        L_0x003c:
            r5 = "a ";
            r5 = r8.startsWith(r5);
            if (r5 == 0) goto L_0x0049;
        L_0x0044:
            r5 = 2;
            r8 = r8.substring(r5);
        L_0x0049:
            r5 = ", the";
            r5 = r8.endsWith(r5);
            if (r5 != 0) goto L_0x0079;
        L_0x0051:
            r5 = ",the";
            r5 = r8.endsWith(r5);
            if (r5 != 0) goto L_0x0079;
        L_0x0059:
            r5 = ", an";
            r5 = r8.endsWith(r5);
            if (r5 != 0) goto L_0x0079;
        L_0x0061:
            r5 = ",an";
            r5 = r8.endsWith(r5);
            if (r5 != 0) goto L_0x0079;
        L_0x0069:
            r5 = ", a";
            r5 = r8.endsWith(r5);
            if (r5 != 0) goto L_0x0079;
        L_0x0071:
            r5 = ",a";
            r5 = r8.endsWith(r5);
            if (r5 == 0) goto L_0x0084;
        L_0x0079:
            r5 = 0;
            r6 = 44;
            r6 = r8.lastIndexOf(r6);
            r8 = r8.substring(r5, r6);
        L_0x0084:
            r5 = "[\\[\\]\\(\\)\"'.,?!]";
            r6 = "";
            r5 = r8.replaceAll(r5, r6);
            r8 = r5.trim();
            r5 = r8.length();
            if (r5 <= 0) goto L_0x00d1;
        L_0x0096:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r0.append(r7);
            r3 = r8.length();
            r1 = 0;
        L_0x00a3:
            if (r1 >= r3) goto L_0x00b2;
        L_0x00a5:
            r5 = r8.charAt(r1);
            r0.append(r5);
            r0.append(r7);
            r1 = r1 + 1;
            goto L_0x00a3;
        L_0x00b2:
            r8 = r0.toString();
            r2 = android.database.DatabaseUtils.getCollationKey(r8);
            if (r4 == 0) goto L_0x000f;
        L_0x00bc:
            r5 = new java.lang.StringBuilder;
            r5.<init>();
            r6 = "\u0001";
            r5 = r5.append(r6);
            r5 = r5.append(r2);
            r2 = r5.toString();
            goto L_0x000f;
        L_0x00d1:
            r2 = "";
            goto L_0x000f;
        L_0x00d5:
            r2 = 0;
            goto L_0x000f;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.Audio.keyFor(java.lang.String):java.lang.String");
        }
    }

    public static final class Files {

        public interface FileColumns extends MediaColumns {
            public static final String FORMAT = "format";
            public static final String MEDIA_TYPE = "media_type";
            public static final int MEDIA_TYPE_AUDIO = 2;
            public static final int MEDIA_TYPE_IMAGE = 1;
            public static final int MEDIA_TYPE_NONE = 0;
            public static final int MEDIA_TYPE_PLAYLIST = 4;
            public static final int MEDIA_TYPE_VIDEO = 3;
            public static final String MIME_TYPE = "mime_type";
            public static final String PARENT = "parent";
            public static final String STORAGE_ID = "storage_id";
            public static final String TITLE = "title";
        }

        public static Uri getContentUri(String volumeName) {
            return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/file");
        }

        public static final Uri getContentUri(String volumeName, long rowId) {
            return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/file/" + rowId);
        }

        public static Uri getMtpObjectsUri(String volumeName) {
            return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/object");
        }

        public static final Uri getMtpObjectsUri(String volumeName, long fileId) {
            return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/object/" + fileId);
        }

        public static final Uri getMtpReferencesUri(String volumeName, long fileId) {
            return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/object/" + fileId + "/references");
        }
    }

    public static final class Images {

        public interface ImageColumns extends MediaColumns {
            public static final String BUCKET_DISPLAY_NAME = "bucket_display_name";
            public static final String BUCKET_ID = "bucket_id";
            public static final String DATE_TAKEN = "datetaken";
            public static final String DESCRIPTION = "description";
            public static final String IS_PRIVATE = "isprivate";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String MINI_THUMB_MAGIC = "mini_thumb_magic";
            public static final String ORIENTATION = "orientation";
            public static final String PICASA_ID = "picasa_id";
        }

        public static final class Media implements ImageColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/image";
            public static final String DEFAULT_SORT_ORDER = "bucket_display_name";
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");

            public static final Cursor query(ContentResolver cr, Uri uri, String[] projection) {
                return cr.query(uri, projection, null, null, "bucket_display_name");
            }

            public static final Cursor query(ContentResolver cr, Uri uri, String[] projection, String where, String orderBy) {
                return cr.query(uri, projection, where, null, orderBy == null ? "bucket_display_name" : orderBy);
            }

            public static final Cursor query(ContentResolver cr, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
                return cr.query(uri, projection, selection, selectionArgs, orderBy == null ? "bucket_display_name" : orderBy);
            }

            public static final Bitmap getBitmap(ContentResolver cr, Uri url) throws FileNotFoundException, IOException {
                InputStream input = cr.openInputStream(url);
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                input.close();
                return bitmap;
            }

            public static final String insertImage(ContentResolver cr, String imagePath, String name, String description) throws FileNotFoundException {
                FileInputStream stream = new FileInputStream(imagePath);
                try {
                    Bitmap bm = BitmapFactory.decodeFile(imagePath);
                    String ret = insertImage(cr, bm, name, description);
                    bm.recycle();
                    return ret;
                } finally {
                    try {
                        stream.close();
                    } catch (IOException e) {
                    }
                }
            }

            private static final Bitmap StoreThumbnail(ContentResolver cr, Bitmap source, long id, float width, float height, int kind) {
                Matrix matrix = new Matrix();
                matrix.setScale(width / ((float) source.getWidth()), height / ((float) source.getHeight()));
                Bitmap thumb = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
                ContentValues values = new ContentValues(4);
                values.put("kind", Integer.valueOf(kind));
                values.put("image_id", Integer.valueOf((int) id));
                values.put("height", Integer.valueOf(thumb.getHeight()));
                values.put("width", Integer.valueOf(thumb.getWidth()));
                try {
                    OutputStream thumbOut = cr.openOutputStream(cr.insert(Thumbnails.EXTERNAL_CONTENT_URI, values));
                    thumb.compress(CompressFormat.JPEG, 100, thumbOut);
                    thumbOut.close();
                    return thumb;
                } catch (FileNotFoundException e) {
                    return null;
                } catch (IOException e2) {
                    return null;
                }
            }

            public static final String insertImage(ContentResolver cr, Bitmap source, String title, String description) {
                ContentValues values = new ContentValues();
                values.put("title", title);
                values.put("description", description);
                values.put("mime_type", "image/jpeg");
                Uri url = null;
                OutputStream imageOut;
                try {
                    url = cr.insert(EXTERNAL_CONTENT_URI, values);
                    if (source != null) {
                        imageOut = cr.openOutputStream(url);
                        source.compress(CompressFormat.JPEG, 50, imageOut);
                        imageOut.close();
                        long id = ContentUris.parseId(url);
                        StoreThumbnail(cr, Thumbnails.getThumbnail(cr, id, 1, null), id, 50.0f, 50.0f, 3);
                        if (url == null) {
                            return url.toString();
                        }
                        return null;
                    }
                    Log.e(MediaStore.TAG, "Failed to create thumbnail, removing original");
                    cr.delete(url, null, null);
                    url = null;
                    if (url == null) {
                        return null;
                    }
                    return url.toString();
                } catch (Exception e) {
                    Log.e(MediaStore.TAG, "Failed to insert image", e);
                    if (url != null) {
                        cr.delete(url, null, null);
                        url = null;
                    }
                } catch (Throwable th) {
                    imageOut.close();
                }
            }

            public static Uri getContentUri(String volumeName) {
                return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/images/media");
            }
        }

        public static class Thumbnails implements BaseColumns {
            public static final String DATA = "_data";
            public static final String DEFAULT_SORT_ORDER = "image_id ASC";
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
            public static final int FULL_SCREEN_KIND = 2;
            public static final String HEIGHT = "height";
            public static final String IMAGE_ID = "image_id";
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
            public static final String KIND = "kind";
            public static final int MICRO_KIND = 3;
            public static final int MINI_KIND = 1;
            public static final String THUMB_DATA = "thumb_data";
            public static final String WIDTH = "width";

            public static final Cursor query(ContentResolver cr, Uri uri, String[] projection) {
                return cr.query(uri, projection, null, null, DEFAULT_SORT_ORDER);
            }

            public static final Cursor queryMiniThumbnails(ContentResolver cr, Uri uri, int kind, String[] projection) {
                return cr.query(uri, projection, "kind = " + kind, null, DEFAULT_SORT_ORDER);
            }

            public static final Cursor queryMiniThumbnail(ContentResolver cr, long origId, int kind, String[] projection) {
                return cr.query(EXTERNAL_CONTENT_URI, projection, "image_id = " + origId + " AND " + "kind" + " = " + kind, null, null);
            }

            public static void cancelThumbnailRequest(ContentResolver cr, long origId) {
                InternalThumbnails.cancelThumbnailRequest(cr, origId, EXTERNAL_CONTENT_URI, 0);
            }

            public static Bitmap getThumbnail(ContentResolver cr, long origId, int kind, Options options) {
                return InternalThumbnails.getThumbnail(cr, origId, 0, kind, options, EXTERNAL_CONTENT_URI, false);
            }

            public static void cancelThumbnailRequest(ContentResolver cr, long origId, long groupId) {
                InternalThumbnails.cancelThumbnailRequest(cr, origId, EXTERNAL_CONTENT_URI, groupId);
            }

            public static Bitmap getThumbnail(ContentResolver cr, long origId, long groupId, int kind, Options options) {
                return InternalThumbnails.getThumbnail(cr, origId, groupId, kind, options, EXTERNAL_CONTENT_URI, false);
            }

            public static Uri getContentUri(String volumeName) {
                return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/images/thumbnails");
            }
        }
    }

    private static class InternalThumbnails implements BaseColumns {
        static final int DEFAULT_GROUP_ID = 0;
        private static final int FULL_SCREEN_KIND = 2;
        private static final int MICRO_KIND = 3;
        private static final int MINI_KIND = 1;
        private static final String[] PROJECTION = new String[]{"_id", "_data"};
        private static byte[] sThumbBuf;
        private static final Object sThumbBufLock = new Object();

        private InternalThumbnails() {
        }

        private static Bitmap getMiniThumbFromFile(Cursor c, Uri baseUri, ContentResolver cr, Options options) {
            Bitmap bitmap = null;
            Uri thumbUri = null;
            try {
                long thumbId = c.getLong(0);
                String filePath = c.getString(1);
                thumbUri = ContentUris.withAppendedId(baseUri, thumbId);
                ParcelFileDescriptor pfdInput = cr.openFileDescriptor(thumbUri, FullBackup.ROOT_TREE_TOKEN);
                bitmap = BitmapFactory.decodeFileDescriptor(pfdInput.getFileDescriptor(), null, options);
                pfdInput.close();
                return bitmap;
            } catch (FileNotFoundException ex) {
                Log.e(MediaStore.TAG, "couldn't open thumbnail " + thumbUri + "; " + ex);
                return bitmap;
            } catch (IOException ex2) {
                Log.e(MediaStore.TAG, "couldn't open thumbnail " + thumbUri + "; " + ex2);
                return bitmap;
            } catch (OutOfMemoryError ex3) {
                Log.e(MediaStore.TAG, "failed to allocate memory for thumbnail " + thumbUri + "; " + ex3);
                return bitmap;
            }
        }

        static void cancelThumbnailRequest(ContentResolver cr, long origId, Uri baseUri, long groupId) {
            Cursor c = null;
            try {
                c = cr.query(baseUri.buildUpon().appendQueryParameter("cancel", WifiEnterpriseConfig.ENGINE_ENABLE).appendQueryParameter("orig_id", String.valueOf(origId)).appendQueryParameter(GroupMembership.GROUP_ID, String.valueOf(groupId)).build(), PROJECTION, null, null, null);
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }

        static Bitmap getThumbnail(ContentResolver cr, long origId, long groupId, int kind, Options options, Uri baseUri, boolean isVideo) {
            Bitmap bitmap = null;
            Cursor c = null;
            MiniThumbFile miniThumbFile = new MiniThumbFile(isVideo ? Media.EXTERNAL_CONTENT_URI : Media.EXTERNAL_CONTENT_URI);
            Uri uri = Uri.parse(baseUri.buildUpon().appendPath(String.valueOf(origId)).toString().replaceFirst("thumbnails", "media"));
            try {
                long magic = miniThumbFile.getMagic(origId);
                long mini_thumb_magic = 0;
                c = cr.query(uri, new String[]{"mini_thumb_magic"}, null, null, null);
                if (c != null && c.moveToFirst()) {
                    mini_thumb_magic = c.getLong(0);
                }
                if (c != null) {
                    c.close();
                }
                if (magic != 0 && magic == mini_thumb_magic) {
                    if (kind == 3) {
                        synchronized (sThumbBufLock) {
                            if (sThumbBuf == null) {
                                sThumbBuf = new byte[10000];
                            }
                            if (miniThumbFile.getMiniThumbFromFile(origId, sThumbBuf) != null) {
                                bitmap = BitmapFactory.decodeByteArray(sThumbBuf, 0, sThumbBuf.length);
                                if (bitmap == null) {
                                    Log.w(MediaStore.TAG, "couldn't decode byte array.");
                                }
                            }
                        }
                        if (c != null) {
                            c.close();
                        }
                        miniThumbFile.deactivate();
                        return bitmap;
                    } else if (kind == 1) {
                        c = cr.query(baseUri, PROJECTION, (isVideo ? "video_id=" : "image_id=") + origId, null, null);
                        if (c != null && c.moveToFirst()) {
                            bitmap = getMiniThumbFromFile(c, baseUri, cr, options);
                            if (bitmap != null) {
                                if (c != null) {
                                    c.close();
                                }
                                miniThumbFile.deactivate();
                                return bitmap;
                            }
                        }
                    }
                }
                Uri blockingUri = baseUri.buildUpon().appendQueryParameter("blocking", WifiEnterpriseConfig.ENGINE_ENABLE).appendQueryParameter("orig_id", String.valueOf(origId)).appendQueryParameter(GroupMembership.GROUP_ID, String.valueOf(groupId)).build();
                if (c != null) {
                    c.close();
                }
                c = cr.query(blockingUri, PROJECTION, null, null, null);
                if (c == null) {
                    if (c != null) {
                        c.close();
                    }
                    miniThumbFile.deactivate();
                    return null;
                }
                if (kind == 3) {
                    if (c != null) {
                        c.close();
                    }
                    c = cr.query(uri, new String[]{"mini_thumb_magic"}, null, null, null);
                    if (c != null && c.moveToFirst()) {
                        mini_thumb_magic = c.getLong(0);
                    }
                    if (c != null) {
                        c.close();
                    }
                    if (mini_thumb_magic != 0 && miniThumbFile.getMagic(origId) == mini_thumb_magic) {
                        synchronized (sThumbBufLock) {
                            if (sThumbBuf == null) {
                                sThumbBuf = new byte[10000];
                            }
                            Arrays.fill(sThumbBuf, (byte) 0);
                            if (miniThumbFile.getMiniThumbFromFile(origId, sThumbBuf) != null) {
                                bitmap = BitmapFactory.decodeByteArray(sThumbBuf, 0, sThumbBuf.length);
                                if (bitmap == null) {
                                    Log.w(MediaStore.TAG, "couldn't decode byte array.");
                                }
                            }
                        }
                    }
                } else if (kind != 1) {
                    throw new IllegalArgumentException("Unsupported kind: " + kind);
                } else if (c.moveToFirst()) {
                    bitmap = getMiniThumbFromFile(c, baseUri, cr, options);
                    if (c != null) {
                        c.close();
                    }
                }
                if (bitmap == null) {
                    Log.v(MediaStore.TAG, "Create the thumbnail in memory: origId=" + origId + ", kind=" + kind + ", isVideo=" + isVideo);
                    if (c != null) {
                        c.close();
                    }
                    c = cr.query(uri, PROJECTION, null, null, null);
                    if (c == null || !c.moveToFirst()) {
                        if (c != null) {
                            c.close();
                        }
                        miniThumbFile.deactivate();
                        return null;
                    }
                    String filePath = c.getString(1);
                    if (filePath != null) {
                        if (isVideo) {
                            bitmap = ThumbnailUtils.createVideoThumbnail(filePath, kind);
                        } else {
                            bitmap = ThumbnailUtils.createImageThumbnail(filePath, kind);
                        }
                    }
                }
                if (c != null) {
                    c.close();
                }
                miniThumbFile.deactivate();
                return bitmap;
            } catch (Throwable ex) {
                try {
                    Log.w(MediaStore.TAG, ex);
                } finally {
                    if (c != null) {
                        c.close();
                    }
                    miniThumbFile.deactivate();
                }
            }
        }
    }

    public static final class Video {
        public static final String DEFAULT_SORT_ORDER = "_display_name";

        public interface VideoColumns extends MediaColumns {
            public static final String ALBUM = "album";
            public static final String ARTIST = "artist";
            public static final String BOOKMARK = "bookmark";
            public static final String BUCKET_DISPLAY_NAME = "bucket_display_name";
            public static final String BUCKET_ID = "bucket_id";
            public static final String CATEGORY = "category";
            public static final String DATE_TAKEN = "datetaken";
            public static final String DESCRIPTION = "description";
            public static final String DURATION = "duration";
            public static final String IS_PRIVATE = "isprivate";
            public static final String LANGUAGE = "language";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String MINI_THUMB_MAGIC = "mini_thumb_magic";
            public static final String RESOLUTION = "resolution";
            public static final String TAGS = "tags";
        }

        public static final class Media implements VideoColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/video";
            public static final String DEFAULT_SORT_ORDER = "title";
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");

            public static Uri getContentUri(String volumeName) {
                return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/video/media");
            }
        }

        public static class Thumbnails implements BaseColumns {
            public static final String DATA = "_data";
            public static final String DEFAULT_SORT_ORDER = "video_id ASC";
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
            public static final int FULL_SCREEN_KIND = 2;
            public static final String HEIGHT = "height";
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
            public static final String KIND = "kind";
            public static final int MICRO_KIND = 3;
            public static final int MINI_KIND = 1;
            public static final String VIDEO_ID = "video_id";
            public static final String WIDTH = "width";

            public static void cancelThumbnailRequest(ContentResolver cr, long origId) {
                InternalThumbnails.cancelThumbnailRequest(cr, origId, EXTERNAL_CONTENT_URI, 0);
            }

            public static Bitmap getThumbnail(ContentResolver cr, long origId, int kind, Options options) {
                return InternalThumbnails.getThumbnail(cr, origId, 0, kind, options, EXTERNAL_CONTENT_URI, true);
            }

            public static Bitmap getThumbnail(ContentResolver cr, long origId, long groupId, int kind, Options options) {
                return InternalThumbnails.getThumbnail(cr, origId, groupId, kind, options, EXTERNAL_CONTENT_URI, true);
            }

            public static void cancelThumbnailRequest(ContentResolver cr, long origId, long groupId) {
                InternalThumbnails.cancelThumbnailRequest(cr, origId, EXTERNAL_CONTENT_URI, groupId);
            }

            public static Uri getContentUri(String volumeName) {
                return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/video/thumbnails");
            }
        }

        public static final Cursor query(ContentResolver cr, Uri uri, String[] projection) {
            return cr.query(uri, projection, null, null, "_display_name");
        }
    }

    public static Uri getMediaScannerUri() {
        return Uri.parse("content://media/none/media_scanner");
    }

    public static Uri getMediaProviderDbLogUri(String volumeName) {
        return Uri.parse(CONTENT_AUTHORITY_SLASH + volumeName + "/mediadb/log");
    }

    public static Uri getPlaylistsSyncUri(String volumeName) {
        return Uri.parse(CONTENT_AUTHORITY_SLASH + volumeName + "/playlists/sync");
    }

    public static Uri getPlaylistsSaveUri(String volumeName) {
        return Uri.parse(CONTENT_AUTHORITY_SLASH + volumeName + "/playlists/save");
    }

    public static Uri getRestoreSdcardUri(String volumeName) {
        return Uri.parse(CONTENT_AUTHORITY_SLASH + volumeName + "/restore/sdcard");
    }

    public static Uri getChangePathSdcardUri(String volumeName) {
        return Uri.parse(CONTENT_AUTHORITY_SLASH + volumeName + "/changepath/sdcard");
    }

    public static String getVersion(Context context) {
        String str = null;
        Cursor c = context.getContentResolver().query(Uri.parse("content://media/none/version"), null, null, null, null);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    str = c.getString(0);
                } else {
                    c.close();
                }
            } finally {
                c.close();
            }
        }
        return str;
    }
}
