package android.media.tv;

import android.content.ComponentName;
import android.content.ContentUris;
import android.net.ProxyInfo;
import android.net.Uri;
import android.net.Uri.Builder;
import android.provider.BaseColumns;
import android.util.ArraySet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TvContract {
    public static final String AUTHORITY = "android.media.tv";
    public static final String PARAM_BROWSABLE_ONLY = "browsable_only";
    public static final String PARAM_CANONICAL_GENRE = "canonical_genre";
    public static final String PARAM_CHANNEL = "channel";
    public static final String PARAM_END_TIME = "end_time";
    public static final String PARAM_INPUT = "input";
    public static final String PARAM_START_TIME = "start_time";
    private static final String PATH_CHANNEL = "channel";
    private static final String PATH_PASSTHROUGH = "passthrough";
    private static final String PATH_PROGRAM = "program";

    public interface BaseTvColumns extends BaseColumns {
        public static final String COLUMN_PACKAGE_NAME = "package_name";
    }

    public static final class Channels implements BaseTvColumns {
        public static final String COLUMN_APP_LINK_COLOR = "app_link_color";
        public static final String COLUMN_APP_LINK_ICON_URI = "app_link_icon_uri";
        public static final String COLUMN_APP_LINK_INTENT_URI = "app_link_intent_uri";
        public static final String COLUMN_APP_LINK_POSTER_ART_URI = "app_link_poster_art_uri";
        public static final String COLUMN_APP_LINK_TEXT = "app_link_text";
        public static final String COLUMN_BROWSABLE = "browsable";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DISPLAY_NAME = "display_name";
        public static final String COLUMN_DISPLAY_NUMBER = "display_number";
        public static final String COLUMN_INPUT_ID = "input_id";
        public static final String COLUMN_INTERNAL_PROVIDER_DATA = "internal_provider_data";
        public static final String COLUMN_INTERNAL_PROVIDER_FLAG1 = "internal_provider_flag1";
        public static final String COLUMN_INTERNAL_PROVIDER_FLAG2 = "internal_provider_flag2";
        public static final String COLUMN_INTERNAL_PROVIDER_FLAG3 = "internal_provider_flag3";
        public static final String COLUMN_INTERNAL_PROVIDER_FLAG4 = "internal_provider_flag4";
        public static final String COLUMN_LOCKED = "locked";
        public static final String COLUMN_NETWORK_AFFILIATION = "network_affiliation";
        public static final String COLUMN_ORIGINAL_NETWORK_ID = "original_network_id";
        public static final String COLUMN_SEARCHABLE = "searchable";
        public static final String COLUMN_SERVICE_ID = "service_id";
        public static final String COLUMN_SERVICE_TYPE = "service_type";
        public static final String COLUMN_TRANSPORT_STREAM_ID = "transport_stream_id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_VERSION_NUMBER = "version_number";
        public static final String COLUMN_VIDEO_FORMAT = "video_format";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/channel";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/channel";
        public static final Uri CONTENT_URI = Uri.parse("content://android.media.tv/channel");
        public static final String SERVICE_TYPE_AUDIO = "SERVICE_TYPE_AUDIO";
        public static final String SERVICE_TYPE_AUDIO_VIDEO = "SERVICE_TYPE_AUDIO_VIDEO";
        public static final String SERVICE_TYPE_OTHER = "SERVICE_TYPE_OTHER";
        public static final String TYPE_1SEG = "TYPE_1SEG";
        public static final String TYPE_ATSC_C = "TYPE_ATSC_C";
        public static final String TYPE_ATSC_M_H = "TYPE_ATSC_M_H";
        public static final String TYPE_ATSC_T = "TYPE_ATSC_T";
        public static final String TYPE_CMMB = "TYPE_CMMB";
        public static final String TYPE_DTMB = "TYPE_DTMB";
        public static final String TYPE_DVB_C = "TYPE_DVB_C";
        public static final String TYPE_DVB_C2 = "TYPE_DVB_C2";
        public static final String TYPE_DVB_H = "TYPE_DVB_H";
        public static final String TYPE_DVB_S = "TYPE_DVB_S";
        public static final String TYPE_DVB_S2 = "TYPE_DVB_S2";
        public static final String TYPE_DVB_SH = "TYPE_DVB_SH";
        public static final String TYPE_DVB_T = "TYPE_DVB_T";
        public static final String TYPE_DVB_T2 = "TYPE_DVB_T2";
        public static final String TYPE_ISDB_C = "TYPE_ISDB_C";
        public static final String TYPE_ISDB_S = "TYPE_ISDB_S";
        public static final String TYPE_ISDB_T = "TYPE_ISDB_T";
        public static final String TYPE_ISDB_TB = "TYPE_ISDB_TB";
        public static final String TYPE_NTSC = "TYPE_NTSC";
        public static final String TYPE_OTHER = "TYPE_OTHER";
        public static final String TYPE_PAL = "TYPE_PAL";
        public static final String TYPE_SECAM = "TYPE_SECAM";
        public static final String TYPE_S_DMB = "TYPE_S_DMB";
        public static final String TYPE_T_DMB = "TYPE_T_DMB";
        public static final String VIDEO_FORMAT_1080I = "VIDEO_FORMAT_1080I";
        public static final String VIDEO_FORMAT_1080P = "VIDEO_FORMAT_1080P";
        public static final String VIDEO_FORMAT_2160P = "VIDEO_FORMAT_2160P";
        public static final String VIDEO_FORMAT_240P = "VIDEO_FORMAT_240P";
        public static final String VIDEO_FORMAT_360P = "VIDEO_FORMAT_360P";
        public static final String VIDEO_FORMAT_4320P = "VIDEO_FORMAT_4320P";
        public static final String VIDEO_FORMAT_480I = "VIDEO_FORMAT_480I";
        public static final String VIDEO_FORMAT_480P = "VIDEO_FORMAT_480P";
        public static final String VIDEO_FORMAT_576I = "VIDEO_FORMAT_576I";
        public static final String VIDEO_FORMAT_576P = "VIDEO_FORMAT_576P";
        public static final String VIDEO_FORMAT_720P = "VIDEO_FORMAT_720P";
        private static final Map<String, String> VIDEO_FORMAT_TO_RESOLUTION_MAP = new HashMap();
        public static final String VIDEO_RESOLUTION_ED = "VIDEO_RESOLUTION_ED";
        public static final String VIDEO_RESOLUTION_FHD = "VIDEO_RESOLUTION_FHD";
        public static final String VIDEO_RESOLUTION_HD = "VIDEO_RESOLUTION_HD";
        public static final String VIDEO_RESOLUTION_SD = "VIDEO_RESOLUTION_SD";
        public static final String VIDEO_RESOLUTION_UHD = "VIDEO_RESOLUTION_UHD";

        public static final class Logo {
            public static final String CONTENT_DIRECTORY = "logo";

            private Logo() {
            }
        }

        static {
            VIDEO_FORMAT_TO_RESOLUTION_MAP.put(VIDEO_FORMAT_480I, VIDEO_RESOLUTION_SD);
            VIDEO_FORMAT_TO_RESOLUTION_MAP.put(VIDEO_FORMAT_480P, VIDEO_RESOLUTION_ED);
            VIDEO_FORMAT_TO_RESOLUTION_MAP.put(VIDEO_FORMAT_576I, VIDEO_RESOLUTION_SD);
            VIDEO_FORMAT_TO_RESOLUTION_MAP.put(VIDEO_FORMAT_576P, VIDEO_RESOLUTION_ED);
            VIDEO_FORMAT_TO_RESOLUTION_MAP.put(VIDEO_FORMAT_720P, VIDEO_RESOLUTION_HD);
            VIDEO_FORMAT_TO_RESOLUTION_MAP.put(VIDEO_FORMAT_1080I, VIDEO_RESOLUTION_HD);
            VIDEO_FORMAT_TO_RESOLUTION_MAP.put(VIDEO_FORMAT_1080P, VIDEO_RESOLUTION_FHD);
            VIDEO_FORMAT_TO_RESOLUTION_MAP.put(VIDEO_FORMAT_2160P, VIDEO_RESOLUTION_UHD);
            VIDEO_FORMAT_TO_RESOLUTION_MAP.put(VIDEO_FORMAT_4320P, VIDEO_RESOLUTION_UHD);
        }

        public static final String getVideoResolution(String videoFormat) {
            return (String) VIDEO_FORMAT_TO_RESOLUTION_MAP.get(videoFormat);
        }

        private Channels() {
        }
    }

    public static final class Programs implements BaseTvColumns {
        public static final String COLUMN_AUDIO_LANGUAGE = "audio_language";
        public static final String COLUMN_BROADCAST_GENRE = "broadcast_genre";
        public static final String COLUMN_CANONICAL_GENRE = "canonical_genre";
        public static final String COLUMN_CHANNEL_ID = "channel_id";
        public static final String COLUMN_CONTENT_RATING = "content_rating";
        public static final String COLUMN_END_TIME_UTC_MILLIS = "end_time_utc_millis";
        public static final String COLUMN_EPISODE_NUMBER = "episode_number";
        public static final String COLUMN_EPISODE_TITLE = "episode_title";
        public static final String COLUMN_INTERNAL_PROVIDER_DATA = "internal_provider_data";
        public static final String COLUMN_INTERNAL_PROVIDER_FLAG1 = "internal_provider_flag1";
        public static final String COLUMN_INTERNAL_PROVIDER_FLAG2 = "internal_provider_flag2";
        public static final String COLUMN_INTERNAL_PROVIDER_FLAG3 = "internal_provider_flag3";
        public static final String COLUMN_INTERNAL_PROVIDER_FLAG4 = "internal_provider_flag4";
        public static final String COLUMN_LONG_DESCRIPTION = "long_description";
        public static final String COLUMN_POSTER_ART_URI = "poster_art_uri";
        public static final String COLUMN_SEARCHABLE = "searchable";
        public static final String COLUMN_SEASON_NUMBER = "season_number";
        public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
        public static final String COLUMN_START_TIME_UTC_MILLIS = "start_time_utc_millis";
        public static final String COLUMN_THUMBNAIL_URI = "thumbnail_uri";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VERSION_NUMBER = "version_number";
        public static final String COLUMN_VIDEO_HEIGHT = "video_height";
        public static final String COLUMN_VIDEO_WIDTH = "video_width";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/program";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/program";
        public static final Uri CONTENT_URI = Uri.parse("content://android.media.tv/program");

        public static final class Genres {
            public static final String ANIMAL_WILDLIFE = "ANIMAL_WILDLIFE";
            public static final String ARTS = "ARTS";
            private static final ArraySet<String> CANONICAL_GENRES = new ArraySet();
            public static final String COMEDY = "COMEDY";
            public static final String DRAMA = "DRAMA";
            public static final String EDUCATION = "EDUCATION";
            public static final String ENTERTAINMENT = "ENTERTAINMENT";
            public static final String FAMILY_KIDS = "FAMILY_KIDS";
            public static final String GAMING = "GAMING";
            public static final String LIFE_STYLE = "LIFE_STYLE";
            public static final String MOVIES = "MOVIES";
            public static final String MUSIC = "MUSIC";
            public static final String NEWS = "NEWS";
            public static final String PREMIER = "PREMIER";
            public static final String SHOPPING = "SHOPPING";
            public static final String SPORTS = "SPORTS";
            public static final String TECH_SCIENCE = "TECH_SCIENCE";
            public static final String TRAVEL = "TRAVEL";

            static {
                CANONICAL_GENRES.add(FAMILY_KIDS);
                CANONICAL_GENRES.add(SPORTS);
                CANONICAL_GENRES.add(SHOPPING);
                CANONICAL_GENRES.add(MOVIES);
                CANONICAL_GENRES.add(COMEDY);
                CANONICAL_GENRES.add(TRAVEL);
                CANONICAL_GENRES.add(DRAMA);
                CANONICAL_GENRES.add(EDUCATION);
                CANONICAL_GENRES.add(ANIMAL_WILDLIFE);
                CANONICAL_GENRES.add(NEWS);
                CANONICAL_GENRES.add(GAMING);
                CANONICAL_GENRES.add(ARTS);
                CANONICAL_GENRES.add(ENTERTAINMENT);
                CANONICAL_GENRES.add(LIFE_STYLE);
                CANONICAL_GENRES.add(MUSIC);
                CANONICAL_GENRES.add(PREMIER);
                CANONICAL_GENRES.add(TECH_SCIENCE);
            }

            private Genres() {
            }

            public static String encode(String... genres) {
                StringBuilder sb = new StringBuilder();
                String separator = ProxyInfo.LOCAL_EXCL_LIST;
                for (String genre : genres) {
                    sb.append(separator).append(genre);
                    separator = ",";
                }
                return sb.toString();
            }

            public static String[] decode(String genres) {
                return genres.split("\\s*,\\s*");
            }

            public static boolean isCanonical(String genre) {
                return CANONICAL_GENRES.contains(genre);
            }
        }

        private Programs() {
        }
    }

    public static final class WatchedPrograms implements BaseTvColumns {
        public static final String COLUMN_CHANNEL_ID = "channel_id";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_END_TIME_UTC_MILLIS = "end_time_utc_millis";
        public static final String COLUMN_INTERNAL_SESSION_TOKEN = "session_token";
        public static final String COLUMN_INTERNAL_TUNE_PARAMS = "tune_params";
        public static final String COLUMN_START_TIME_UTC_MILLIS = "start_time_utc_millis";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_WATCH_END_TIME_UTC_MILLIS = "watch_end_time_utc_millis";
        public static final String COLUMN_WATCH_START_TIME_UTC_MILLIS = "watch_start_time_utc_millis";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/watched_program";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/watched_program";
        public static final Uri CONTENT_URI = Uri.parse("content://android.media.tv/watched_program");

        private WatchedPrograms() {
        }
    }

    public static final String buildInputId(ComponentName name) {
        return name.flattenToShortString();
    }

    public static final Uri buildChannelUri(long channelId) {
        return ContentUris.withAppendedId(Channels.CONTENT_URI, channelId);
    }

    public static final Uri buildChannelUriForPassthroughInput(String inputId) {
        return new Builder().scheme("content").authority(AUTHORITY).appendPath(PATH_PASSTHROUGH).appendPath(inputId).build();
    }

    public static final Uri buildChannelLogoUri(long channelId) {
        return buildChannelLogoUri(buildChannelUri(channelId));
    }

    public static final Uri buildChannelLogoUri(Uri channelUri) {
        if (isChannelUriForTunerInput(channelUri)) {
            return Uri.withAppendedPath(channelUri, Logo.CONTENT_DIRECTORY);
        }
        throw new IllegalArgumentException("Not a channel: " + channelUri);
    }

    public static final Uri buildChannelsUriForInput(String inputId) {
        return buildChannelsUriForInput(inputId, false);
    }

    public static final Uri buildChannelsUriForInput(String inputId, boolean browsableOnly) {
        Builder builder = Channels.CONTENT_URI.buildUpon();
        if (inputId != null) {
            builder.appendQueryParameter("input", inputId);
        }
        return builder.appendQueryParameter(PARAM_BROWSABLE_ONLY, String.valueOf(browsableOnly)).build();
    }

    public static final Uri buildChannelsUriForInput(String inputId, String genre, boolean browsableOnly) {
        if (genre == null) {
            return buildChannelsUriForInput(inputId, browsableOnly);
        }
        if (Genres.isCanonical(genre)) {
            return buildChannelsUriForInput(inputId, browsableOnly).buildUpon().appendQueryParameter("canonical_genre", genre).build();
        }
        throw new IllegalArgumentException("Not a canonical genre: '" + genre + "'");
    }

    public static final Uri buildProgramUri(long programId) {
        return ContentUris.withAppendedId(Programs.CONTENT_URI, programId);
    }

    public static final Uri buildProgramsUriForChannel(long channelId) {
        return Programs.CONTENT_URI.buildUpon().appendQueryParameter("channel", String.valueOf(channelId)).build();
    }

    public static final Uri buildProgramsUriForChannel(Uri channelUri) {
        if (isChannelUriForTunerInput(channelUri)) {
            return buildProgramsUriForChannel(ContentUris.parseId(channelUri));
        }
        throw new IllegalArgumentException("Not a channel: " + channelUri);
    }

    public static final Uri buildProgramsUriForChannel(long channelId, long startTime, long endTime) {
        return buildProgramsUriForChannel(channelId).buildUpon().appendQueryParameter(PARAM_START_TIME, String.valueOf(startTime)).appendQueryParameter(PARAM_END_TIME, String.valueOf(endTime)).build();
    }

    public static final Uri buildProgramsUriForChannel(Uri channelUri, long startTime, long endTime) {
        if (isChannelUriForTunerInput(channelUri)) {
            return buildProgramsUriForChannel(ContentUris.parseId(channelUri), startTime, endTime);
        }
        throw new IllegalArgumentException("Not a channel: " + channelUri);
    }

    public static final Uri buildWatchedProgramUri(long watchedProgramId) {
        return ContentUris.withAppendedId(WatchedPrograms.CONTENT_URI, watchedProgramId);
    }

    private static boolean isTvUri(Uri uri) {
        return uri != null && "content".equals(uri.getScheme()) && AUTHORITY.equals(uri.getAuthority());
    }

    private static boolean isTwoSegmentUriStartingWith(Uri uri, String pathSegment) {
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() == 2 && pathSegment.equals(pathSegments.get(0))) {
            return true;
        }
        return false;
    }

    public static final boolean isChannelUri(Uri uri) {
        return isChannelUriForTunerInput(uri) || isChannelUriForPassthroughInput(uri);
    }

    public static final boolean isChannelUriForTunerInput(Uri uri) {
        return isTvUri(uri) && isTwoSegmentUriStartingWith(uri, "channel");
    }

    public static final boolean isChannelUriForPassthroughInput(Uri uri) {
        return isTvUri(uri) && isTwoSegmentUriStartingWith(uri, PATH_PASSTHROUGH);
    }

    public static final boolean isProgramUri(Uri uri) {
        return isTvUri(uri) && isTwoSegmentUriStartingWith(uri, PATH_PROGRAM);
    }

    private TvContract() {
    }
}
