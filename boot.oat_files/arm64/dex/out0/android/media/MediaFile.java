package android.media;

import android.content.ClipDescription;
import android.media.DecoderCapabilities.AudioDecoder;
import android.media.DecoderCapabilities.VideoDecoder;
import android.mtp.MtpConstants;
import android.os.SystemProperties;
import com.sec.android.app.CscFeature;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MediaFile {
    public static final int FILE_TYPE_3GA = 10;
    public static final int FILE_TYPE_3GPP = 23;
    public static final int FILE_TYPE_3GPP2 = 24;
    public static final int FILE_TYPE_AAC = 8;
    public static final int FILE_TYPE_AK3G = 311;
    public static final int FILE_TYPE_AK3GV = 343;
    public static final int FILE_TYPE_AMR = 4;
    public static final int FILE_TYPE_ASF = 26;
    public static final int FILE_TYPE_AVI = 29;
    public static final int FILE_TYPE_AWB = 5;
    public static final int FILE_TYPE_BMP = 34;
    public static final int FILE_TYPE_CER = 125;
    public static final int FILE_TYPE_CRT = 122;
    public static final int FILE_TYPE_DASH = 45;
    public static final int FILE_TYPE_DCF = 53;
    public static final int FILE_TYPE_DCF_SKT = 300;
    public static final int FILE_TYPE_DER = 123;
    public static final int FILE_TYPE_DIVX = 202;
    public static final int FILE_TYPE_DM = 52;
    public static final int FILE_TYPE_EVC = 17;
    public static final int FILE_TYPE_FL = 51;
    public static final int FILE_TYPE_FLAC = 13;
    public static final int FILE_TYPE_FLV = 205;
    public static final int FILE_TYPE_GIF = 32;
    public static final int FILE_TYPE_GOLF = 37;
    public static final int FILE_TYPE_HTML = 101;
    public static final int FILE_TYPE_HTTPLIVE = 44;
    public static final int FILE_TYPE_IMY = 16;
    public static final int FILE_TYPE_ISMA = 12;
    public static final int FILE_TYPE_ISMV = 203;
    public static final int FILE_TYPE_JPEG = 31;
    public static final int FILE_TYPE_K3G = 340;
    public static final int FILE_TYPE_K3GA = 322;
    public static final int FILE_TYPE_M3U = 41;
    public static final int FILE_TYPE_M4A = 2;
    public static final int FILE_TYPE_M4V = 22;
    public static final int FILE_TYPE_MID = 14;
    public static final int FILE_TYPE_MKA = 9;
    public static final int FILE_TYPE_MKV = 27;
    public static final int FILE_TYPE_MP2PS = 200;
    public static final int FILE_TYPE_MP2TS = 28;
    public static final int FILE_TYPE_MP3 = 1;
    public static final int FILE_TYPE_MP4 = 21;
    public static final int FILE_TYPE_MS_EXCEL = 105;
    public static final int FILE_TYPE_MS_POWERPOINT = 106;
    public static final int FILE_TYPE_MS_WORD = 104;
    public static final int FILE_TYPE_ODF_LGU = 321;
    public static final int FILE_TYPE_OGG = 7;
    public static final int FILE_TYPE_P12 = 120;
    public static final int FILE_TYPE_PDF = 102;
    public static final int FILE_TYPE_PEM = 124;
    public static final int FILE_TYPE_PFX = 121;
    public static final int FILE_TYPE_PLS = 42;
    public static final int FILE_TYPE_PNG = 33;
    public static final int FILE_TYPE_PYA = 11;
    public static final int FILE_TYPE_PYV = 201;
    public static final int FILE_TYPE_QCP = 18;
    public static final int FILE_TYPE_RMF = 204;
    public static final int FILE_TYPE_SKA = 301;
    public static final int FILE_TYPE_SKM = 341;
    public static final int FILE_TYPE_SKV = 342;
    public static final int FILE_TYPE_SMF = 15;
    public static final int FILE_TYPE_TEXT = 100;
    public static final int FILE_TYPE_WAV = 3;
    public static final int FILE_TYPE_WBMP = 35;
    public static final int FILE_TYPE_WEBM = 30;
    public static final int FILE_TYPE_WEBP = 36;
    public static final int FILE_TYPE_WMA = 6;
    public static final int FILE_TYPE_WMV = 25;
    public static final int FILE_TYPE_WPL = 43;
    public static final int FILE_TYPE_XML = 103;
    public static final int FILE_TYPE_ZIP = 107;
    private static final int FIRST_AUDIO_FILE_TYPE = 1;
    private static final int FIRST_DRM_FILE_TYPE = 51;
    private static final int FIRST_IMAGE_FILE_TYPE = 31;
    private static final int FIRST_KOR_AUDIO_FILE_TYPE = 300;
    private static final int FIRST_KOR_VIDEO_FILE_TYPE = 340;
    private static final int FIRST_MIDI_FILE_TYPE = 14;
    private static final int FIRST_PLAYLIST_FILE_TYPE = 41;
    private static final int FIRST_USA_SPEECH_FILE_TYPE = 17;
    private static final int FIRST_VIDEO_FILE_TYPE = 21;
    private static final int FIRST_VIDEO_FILE_TYPE2 = 200;
    private static final int LAST_AUDIO_FILE_TYPE = 13;
    private static final int LAST_DRM_FILE_TYPE = 53;
    private static final int LAST_IMAGE_FILE_TYPE = 37;
    private static final int LAST_KOR_AUDIO_FILE_TYPE = 322;
    private static final int LAST_KOR_VIDEO_FILE_TYPE = 343;
    private static final int LAST_MIDI_FILE_TYPE = 16;
    private static final int LAST_PLAYLIST_FILE_TYPE = 45;
    private static final int LAST_USA_SPEECH_FILE_TYPE = 18;
    private static final int LAST_VIDEO_FILE_TYPE = 30;
    private static final int LAST_VIDEO_FILE_TYPE2 = 205;
    private static final HashMap<String, MediaFileType> sFileTypeMap = new HashMap();
    private static final HashMap<String, Integer> sFileTypeToFormatMap = new HashMap();
    private static final HashMap<Integer, String> sFormatToMimeTypeMap = new HashMap();
    private static final HashMap<String, Integer> sMimeTypeMap = new HashMap();
    private static final HashMap<String, Integer> sMimeTypeToFormatMap = new HashMap();

    public static class MediaFileType {
        public final int fileType;
        public final String mimeType;

        MediaFileType(int fileType, String mimeType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }
    }

    static {
        addFileType("MP3", 1, MediaFormat.MIMETYPE_AUDIO_MPEG, 12297);
        addFileType("MPGA", 1, MediaFormat.MIMETYPE_AUDIO_MPEG, 12297);
        addFileType("M4A", 2, "audio/mp4", 12299);
        addFileType("WAV", 3, "audio/x-wav", 12296);
        addFileType("AMR", 4, "audio/amr");
        addFileType("AWB", 5, MediaFormat.MIMETYPE_AUDIO_AMR_WB);
        addFileType("3GP", 10, MediaFormat.MIMETYPE_AUDIO_AMR_NB);
        addFileType("3GA", 10, MediaFormat.MIMETYPE_AUDIO_AMR_NB);
        addFileType("ASF", 6, "audio/x-ms-wma");
        addFileType("WMA", 6, "audio/x-ms-wma", MtpConstants.FORMAT_WMA);
        if (isQCPEnabled()) {
            addFileType("QCP", 18, MediaFormat.MIMETYPE_AUDIO_QCELP);
            addFileType("EVC", 17, "audio/evrc");
        }
        addFileType("PYA", 11, "audio/vnd.ms-playready.media.pya");
        addFileType("AAC", 8, MediaFormat.MIMETYPE_AUDIO_AAC, MtpConstants.FORMAT_AAC);
        addFileType("AAC", 8, "audio/m4a", MtpConstants.FORMAT_AAC);
        addFileType("MID", 14, "audio/mid");
        addFileType("MIDI", 14, "audio/mid");
        addFileType("PYV", 201, "video/vnd.ms-playready.media.pyv");
        addFileType("DM", 52, "application/vnd.oma.drm.content");
        addFileType("DCF", 53, "application/vnd.oma.drm.content");
        addFileType("JPG", 31, "image/jpg", MtpConstants.FORMAT_EXIF_JPEG);
        addFileType("JPEG", 31, "image/jpg", MtpConstants.FORMAT_EXIF_JPEG);
        addFileType("BMP", 34, "image/bmp", MtpConstants.FORMAT_BMP);
        addFileType("OGG", 7, "audio/ogg", MtpConstants.FORMAT_OGG);
        addFileType("OGG", 7, "application/ogg", MtpConstants.FORMAT_OGG);
        addFileType("OGA", 7, "application/ogg", MtpConstants.FORMAT_OGG);
        addFileType("AAC", 8, "audio/aac", MtpConstants.FORMAT_AAC);
        addFileType("AAC", 8, "audio/aac-adts", MtpConstants.FORMAT_AAC);
        addFileType("MKA", 9, "audio/x-matroska");
        addFileType("MID", 14, "audio/midi");
        addFileType("MIDI", 14, "audio/midi");
        addFileType("XMF", 14, "audio/midi");
        addFileType("RTTTL", 14, "audio/midi");
        addFileType("SMF", 15, "audio/sp-midi");
        addFileType("IMY", 16, "audio/imelody");
        addFileType("RTX", 14, "audio/midi");
        addFileType("OTA", 14, "audio/midi");
        addFileType("MXMF", 14, "audio/midi");
        addFileType("MPEG", 21, "video/mpeg", 12299);
        addFileType("MPG", 21, "video/mpeg", 12299);
        addFileType("MP4", 21, "video/mp4", 12299);
        addFileType("M4V", 22, "video/mp4", 12299);
        addFileType("3GP", 23, MediaFormat.MIMETYPE_VIDEO_H263, MtpConstants.FORMAT_3GP_CONTAINER);
        addFileType("3GPP", 23, MediaFormat.MIMETYPE_VIDEO_H263, MtpConstants.FORMAT_3GP_CONTAINER);
        addFileType("3G2", 24, "video/3gpp2", MtpConstants.FORMAT_3GP_CONTAINER);
        addFileType("3GPP2", 24, "video/3gpp2", MtpConstants.FORMAT_3GP_CONTAINER);
        addFileType("MKV", 27, "video/x-matroska");
        addFileType("WEBM", 30, "video/webm");
        addFileType("TS", 28, "video/mp2ts");
        addFileType("AVI", 29, "video/avi");
        if (CscFeature.getInstance().getEnableStatus("CscFeature_MyFiles_SupportRmvbFileFormat")) {
            addFileType("RAM", 204, "video/rmf");
            addFileType("RM", 204, "video/rmf");
            addFileType("RMVB", 204, "video/rmf");
        }
        addFileType("WMV", 25, "video/x-ms-wmv", MtpConstants.FORMAT_WMV);
        addFileType("ASF", 26, "video/x-ms-asf");
        addFileType("JPG", 31, "image/jpeg", MtpConstants.FORMAT_EXIF_JPEG);
        addFileType("JPEG", 31, "image/jpeg", MtpConstants.FORMAT_EXIF_JPEG);
        addFileType("GIF", 32, "image/gif", MtpConstants.FORMAT_GIF);
        addFileType("PNG", 33, "image/png", MtpConstants.FORMAT_PNG);
        addFileType("BMP", 34, "image/x-ms-bmp", MtpConstants.FORMAT_BMP);
        addFileType("WBMP", 35, "image/vnd.wap.wbmp");
        addFileType("WEBP", 36, "image/webp");
        addFileType("GOLF", 37, "image/golf");
        addFileType("M3U", 41, "audio/x-mpegurl", MtpConstants.FORMAT_M3U_PLAYLIST);
        addFileType("M3U", 41, "application/x-mpegurl", MtpConstants.FORMAT_M3U_PLAYLIST);
        addFileType("PLS", 42, "audio/x-scpls", MtpConstants.FORMAT_PLS_PLAYLIST);
        addFileType("WPL", 43, "application/vnd.ms-wpl", MtpConstants.FORMAT_WPL_PLAYLIST);
        addFileType("M3U8", 44, "application/vnd.apple.mpegurl");
        addFileType("M3U8", 44, "audio/mpegurl");
        addFileType("M3U8", 44, "audio/x-mpegurl");
        addFileType("FL", 51, "application/x-android-drm-fl");
        addFileType("TXT", 100, ClipDescription.MIMETYPE_TEXT_PLAIN, 12292);
        addFileType("HTM", 101, ClipDescription.MIMETYPE_TEXT_HTML, 12293);
        addFileType("HTML", 101, ClipDescription.MIMETYPE_TEXT_HTML, 12293);
        addFileType("PDF", 102, "application/pdf");
        addFileType("DOC", 104, "application/msword", MtpConstants.FORMAT_MS_WORD_DOCUMENT);
        addFileType("XLS", 105, "application/vnd.ms-excel", MtpConstants.FORMAT_MS_EXCEL_SPREADSHEET);
        addFileType("PPT", 106, "application/mspowerpoint", MtpConstants.FORMAT_MS_POWERPOINT_PRESENTATION);
        addFileType("FLAC", 13, MediaFormat.MIMETYPE_AUDIO_FLAC, MtpConstants.FORMAT_FLAC);
        addFileType("ZIP", 107, "application/zip");
        addFileType("MPG", 200, "video/mp2p");
        addFileType("MPEG", 200, "video/mp2p");
        addFileType("DIVX", 202, "video/divx");
        addFileType("FLV", 205, "video/flv");
        addFileType("TP", 28, "video/mp2ts");
        addFileType("TRP", 28, "video/mp2ts");
        addFileType("M2TS", 28, "video/mp2ts");
        addFileType("MTS", 28, "video/mp2ts");
        addFileType("M2T", 28, "video/mp2ts");
        addFileType("P12", 120, "application/x-pkcs12");
        addFileType("PFX", 121, "application/x-pkcs12");
        addFileType("CRT", 122, "application/x-x509-ca-cert");
        addFileType("DER", 123, "application/x-x509-ca-cert");
        addFileType("PEM", 124, "application/x-pem-file");
        addFileType("CER", 125, "application/pkix-cert");
        if (CscFeature.getInstance().getString("CscFeature_MMFW_ConfigFileExtension").contains("PIFF")) {
            addFileType("ISMA", 12, "audio/isma");
            addFileType("ISMV", 203, "video/ismv");
        }
        if (_checkCountryCode("KOREA")) {
            addFileType("SKM", FILE_TYPE_SKM, "video/skm");
            addFileType("SKV", FILE_TYPE_SKV, "video/skm");
            addFileType("SKA", 301, "audio/skm");
            addFileType("K3G", 340, "video/k3g");
            addFileType("K3G", 340, "video/kr3g");
            addFileType("K3G", 322, "audio/kr3g");
            addFileType("K3G", 322, "audio/k3g");
            addFileType("AK3G", FILE_TYPE_AK3G, "audio/ak3g");
            addFileType("AK3G", 343, "video/ak3g");
            if (CscFeature.getInstance().getString("CscFeature_MMFW_ConfigFileExtension").contains("DCF")) {
                addFileType("DCF", 300, "audio/x-mp3");
            }
        }
        if ("Combination".equals("Strawberry")) {
            addFileType("MPD", 45, "application/dash+xml");
        }
    }

    static void addFileType(String extension, int fileType, String mimeType) {
        sFileTypeMap.put(extension, new MediaFileType(fileType, mimeType));
        sMimeTypeMap.put(mimeType, Integer.valueOf(fileType));
    }

    static void addFileType(String extension, int fileType, String mimeType, int mtpFormatCode) {
        addFileType(extension, fileType, mimeType);
        sFileTypeToFormatMap.put(extension, Integer.valueOf(mtpFormatCode));
        sMimeTypeToFormatMap.put(mimeType, Integer.valueOf(mtpFormatCode));
        sFormatToMimeTypeMap.put(Integer.valueOf(mtpFormatCode), mimeType);
    }

    private static boolean isWMAEnabled() {
        List<AudioDecoder> decoders = DecoderCapabilities.getAudioDecoders();
        int count = decoders.size();
        for (int i = 0; i < count; i++) {
            if (((AudioDecoder) decoders.get(i)) == AudioDecoder.AUDIO_DECODER_WMA) {
                return true;
            }
        }
        return false;
    }

    private static boolean isWMVEnabled() {
        List<VideoDecoder> decoders = DecoderCapabilities.getVideoDecoders();
        int count = decoders.size();
        for (int i = 0; i < count; i++) {
            if (((VideoDecoder) decoders.get(i)) == VideoDecoder.VIDEO_DECODER_WMV) {
                return true;
            }
        }
        return false;
    }

    private static boolean isQCPEnabled() {
        if (CscFeature.getInstance().getString("CscFeature_MMFW_ConfigFileExtension").contains("QCP")) {
            return true;
        }
        return false;
    }

    private static boolean _checkCountryCode(String code) {
        if (code == null || !code.equals(SystemProperties.get("ro.csc.country_code"))) {
            return false;
        }
        return true;
    }

    private static boolean _isAudioFileType(int fileType) {
        if (fileType >= 1 && fileType <= 13) {
            return true;
        }
        if (fileType < 14 || fileType > 16) {
            return fileType >= 17 && fileType <= 18;
        } else {
            return true;
        }
    }

    public static boolean isAudioFileType(int fileType) {
        if (_isAudioFileType(fileType)) {
            return true;
        }
        if (!_checkCountryCode("KOREA")) {
            return false;
        }
        if (fileType < 300 || fileType > 322) {
            return false;
        }
        return true;
    }

    private static boolean _isVideoFileType(int fileType) {
        return (fileType >= 21 && fileType <= 30) || (fileType >= 200 && fileType <= 205);
    }

    public static boolean isVideoFileType(int fileType) {
        if (_isVideoFileType(fileType)) {
            return true;
        }
        if (!_checkCountryCode("KOREA")) {
            return false;
        }
        if (fileType < 340 || fileType > 343) {
            return false;
        }
        return true;
    }

    public static boolean isImageFileType(int fileType) {
        return fileType >= 31 && fileType <= 37;
    }

    public static boolean isPlayListFileType(int fileType) {
        return fileType >= 41 && fileType <= 45;
    }

    public static boolean isDrmFileType(int fileType) {
        return fileType >= 51 && fileType <= 53;
    }

    public static MediaFileType getFileType(String path) {
        int lastDot = path.lastIndexOf(46);
        if (lastDot < 0) {
            return null;
        }
        return (MediaFileType) sFileTypeMap.get(path.substring(lastDot + 1).toUpperCase(Locale.ROOT));
    }

    public static boolean isMimeTypeMedia(String mimeType) {
        int fileType = getFileTypeForMimeType(mimeType);
        return isAudioFileType(fileType) || isVideoFileType(fileType) || isImageFileType(fileType) || isPlayListFileType(fileType);
    }

    public static String getFileTitle(String path) {
        int lastSlash = path.lastIndexOf(47);
        if (lastSlash >= 0) {
            lastSlash++;
            if (lastSlash < path.length()) {
                path = path.substring(lastSlash);
            }
        }
        int lastDot = path.lastIndexOf(46);
        if (lastDot > 0) {
            return path.substring(0, lastDot);
        }
        return path;
    }

    public static int getFileTypeForMimeType(String mimeType) {
        Integer value = (Integer) sMimeTypeMap.get(mimeType);
        return value == null ? 0 : value.intValue();
    }

    public static String getMimeTypeForFile(String path) {
        MediaFileType mediaFileType = getFileType(path);
        return mediaFileType == null ? null : mediaFileType.mimeType;
    }

    public static int getFormatCode(String fileName, String mimeType) {
        Integer value;
        if (mimeType != null) {
            value = (Integer) sMimeTypeToFormatMap.get(mimeType);
            if (value != null) {
                return value.intValue();
            }
        }
        int lastDot = fileName.lastIndexOf(46);
        if (lastDot > 0) {
            value = (Integer) sFileTypeToFormatMap.get(fileName.substring(lastDot + 1).toUpperCase(Locale.ROOT));
            if (value != null) {
                return value.intValue();
            }
        }
        return 12288;
    }

    public static String getMimeTypeForFormatCode(int formatCode) {
        return (String) sFormatToMimeTypeMap.get(Integer.valueOf(formatCode));
    }
}
