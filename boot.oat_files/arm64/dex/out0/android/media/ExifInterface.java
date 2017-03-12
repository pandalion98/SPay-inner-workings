package android.media;

import android.R;
import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothClass.Device;
import android.net.ConnectivityManager.PacketKeepalive;
import android.net.ProxyInfo;
import android.opengl.GLES30;
import android.opengl.GLES31Ext;
import android.util.Log;
import android.util.Pair;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import libcore.io.IoUtils;
import libcore.io.Streams;

public class ExifInterface {
    private static final Charset ASCII = Charset.forName("US-ASCII");
    private static final short BYTE_ALIGN_II = (short) 18761;
    private static final short BYTE_ALIGN_MM = (short) 19789;
    private static final boolean DEBUG = false;
    private static final byte[] EXIF_ASCII_PREFIX = new byte[]{(byte) 65, (byte) 83, (byte) 67, (byte) 73, (byte) 73, (byte) 0, (byte) 0, (byte) 0};
    private static final ExifTag[][] EXIF_TAGS = new ExifTag[][]{IFD_TIFF_TAGS, IFD_EXIF_TAGS, IFD_GPS_TAGS, IFD_INTEROPERABILITY_TAGS, IFD_THUMBNAIL_TAGS};
    private static final byte[] IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes(ASCII);
    private static final int IFD_EXIF_HINT = 1;
    private static final ExifTag[] IFD_EXIF_TAGS = new ExifTag[]{new ExifTag(TAG_EXPOSURE_TIME, 33434, 5), new ExifTag(TAG_APERTURE, 33437, 5), new ExifTag(TAG_EXPOSURE_PROGRAM, 34850, 3), new ExifTag(TAG_SPECTRAL_SENSITIVITY, (int) GLES30.GL_MAX_DRAW_BUFFERS, 2), new ExifTag(TAG_ISO, (int) GLES30.GL_DRAW_BUFFER2, 3), new ExifTag(TAG_OECF, (int) GLES30.GL_DRAW_BUFFER3, 7), new ExifTag(TAG_EXIF_VERSION, (int) DevicePolicyManager.PASSWORD_QUALITY_SIGNATURE, 2), new ExifTag(TAG_DATETIME_ORIGINAL, 36867, 2), new ExifTag(TAG_DATETIME_DIGITIZED, 36868, 2), new ExifTag(TAG_COMPONENTS_CONFIGURATION, 37121, 7), new ExifTag(TAG_COMPRESSED_BITS_PER_PIXEL, (int) GLES31Ext.GL_TEXTURE_2D_MULTISAMPLE_ARRAY_OES, 5), new ExifTag(TAG_SHUTTER_SPEED_VALUE, 37377, 10), new ExifTag(TAG_APERTURE_VALUE, 37378, 5), new ExifTag(TAG_BRIGHTNESS_VALUE, 37379, 10), new ExifTag(TAG_EXPOSURE_BIAS_VALUE, 37380, 10), new ExifTag(TAG_MAX_APERTURE_VALUE, 37381, 5), new ExifTag(TAG_SUBJECT_DISTANCE, 37382, 5), new ExifTag(TAG_METERING_MODE, 37383, 3), new ExifTag(TAG_LIGHT_SOURCE, 37384, 3), new ExifTag(TAG_FLASH, 37385, 3), new ExifTag(TAG_FOCAL_LENGTH, 37386, 5), new ExifTag(TAG_SUBJECT_AREA, 37396, 3), new ExifTag(TAG_MAKER_NOTE, 37500, 7), new ExifTag(TAG_USER_COMMENT, 37510, 7), new ExifTag(TAG_SUBSEC_TIME, 37520, 2), new ExifTag(TAG_SUBSEC_TIME_ORIG, 37521, 2), new ExifTag(TAG_SUBSEC_TIME_DIG, 37522, 2), new ExifTag(TAG_FLASHPIX_VERSION, 40960, 7), new ExifTag(TAG_COLOR_SPACE, 40961, 3), new ExifTag(TAG_PIXEL_X_DIMENSION, 40962, 3, 4), new ExifTag(TAG_PIXEL_Y_DIMENSION, 40963, 3, 4), new ExifTag(TAG_RELATED_SOUND_FILE, 40964, 2), new ExifTag(TAG_INTEROPERABILITY_IFD_POINTER, 40965, 4), new ExifTag(TAG_FLASH_ENERGY, 41483, 5), new ExifTag(TAG_SPATIAL_FREQUENCY_RESPONSE, 41484, 7), new ExifTag(TAG_FOCAL_PLANE_X_RESOLUTION, 41486, 5), new ExifTag(TAG_FOCAL_PLANE_Y_RESOLUTION, 41487, 5), new ExifTag(TAG_FOCAL_PLANE_RESOLUTION_UNIT, 41488, 3), new ExifTag(TAG_SUBJECT_LOCATION, 41492, 3), new ExifTag(TAG_EXPOSURE_INDEX, 41493, 5), new ExifTag(TAG_SENSING_METHOD, 41495, 3), new ExifTag(TAG_FILE_SOURCE, 41728, 7), new ExifTag(TAG_SCENE_TYPE, 41729, 7), new ExifTag(TAG_CFA_PATTERN, 41730, 7), new ExifTag(TAG_CUSTOM_RENDERED, 41985, 3), new ExifTag(TAG_EXPOSURE_MODE, 41986, 3), new ExifTag(TAG_WHITE_BALANCE, 41987, 3), new ExifTag(TAG_DIGITAL_ZOOM_RATIO, 41988, 5), new ExifTag(TAG_FOCAL_LENGTH_IN_35MM_FILM, 41989, 3), new ExifTag(TAG_SCENE_CAPTURE_TYPE, 41990, 3), new ExifTag(TAG_GAIN_CONTROL, 41991, 3), new ExifTag(TAG_CONTRAST, 41992, 3), new ExifTag(TAG_SATURATION, 41993, 3), new ExifTag(TAG_SHARPNESS, 41994, 3), new ExifTag(TAG_DEVICE_SETTING_DESCRIPTION, 41995, 7), new ExifTag(TAG_SUBJECT_DISTANCE_RANGE, 41996, 3), new ExifTag(TAG_IMAGE_UNIQUE_ID, 42016, 2)};
    private static final int IFD_FORMAT_BYTE = 1;
    private static final int[] IFD_FORMAT_BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8};
    private static final int IFD_FORMAT_DOUBLE = 12;
    private static final String[] IFD_FORMAT_NAMES = new String[]{ProxyInfo.LOCAL_EXCL_LIST, "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE"};
    private static final int IFD_FORMAT_SBYTE = 6;
    private static final int IFD_FORMAT_SINGLE = 11;
    private static final int IFD_FORMAT_SLONG = 9;
    private static final int IFD_FORMAT_SRATIONAL = 10;
    private static final int IFD_FORMAT_SSHORT = 8;
    private static final int IFD_FORMAT_STRING = 2;
    private static final int IFD_FORMAT_ULONG = 4;
    private static final int IFD_FORMAT_UNDEFINED = 7;
    private static final int IFD_FORMAT_URATIONAL = 5;
    private static final int IFD_FORMAT_USHORT = 3;
    private static final int IFD_GPS_HINT = 2;
    private static final ExifTag[] IFD_GPS_TAGS = new ExifTag[]{new ExifTag(TAG_GPS_VERSION_ID, 0, 1), new ExifTag(TAG_GPS_LATITUDE_REF, 1, 2), new ExifTag(TAG_GPS_LATITUDE, 2, 5), new ExifTag(TAG_GPS_LONGITUDE_REF, 3, 2), new ExifTag(TAG_GPS_LONGITUDE, 4, 5), new ExifTag(TAG_GPS_ALTITUDE_REF, 5, 1), new ExifTag(TAG_GPS_ALTITUDE, 6, 5), new ExifTag(TAG_GPS_TIMESTAMP, 7, 5), new ExifTag(TAG_GPS_SATELLITES, 8, 2), new ExifTag(TAG_GPS_STATUS, 9, 2), new ExifTag(TAG_GPS_MEASURE_MODE, 10, 2), new ExifTag(TAG_GPS_DOP, 11, 5), new ExifTag(TAG_GPS_SPEED_REF, 12, 2), new ExifTag(TAG_GPS_SPEED, 13, 5), new ExifTag(TAG_GPS_TRACK_REF, 14, 2), new ExifTag(TAG_GPS_TRACK, 15, 5), new ExifTag(TAG_GPS_IMG_DIRECTION_REF, 16, 2), new ExifTag(TAG_GPS_IMG_DIRECTION, 17, 5), new ExifTag(TAG_GPS_MAP_DATUM, 18, 2), new ExifTag(TAG_GPS_DEST_LATITUDE_REF, 19, 2), new ExifTag(TAG_GPS_DEST_LATITUDE, 20, 5), new ExifTag(TAG_GPS_DEST_LONGITUDE_REF, 21, 2), new ExifTag(TAG_GPS_DEST_LONGITUDE, 22, 5), new ExifTag(TAG_GPS_DEST_BEARING_REF, 23, 2), new ExifTag(TAG_GPS_DEST_BEARING, 24, 5), new ExifTag(TAG_GPS_DEST_DISTANCE_REF, 25, 2), new ExifTag(TAG_GPS_DEST_DISTANCE, 26, 5), new ExifTag(TAG_GPS_PROCESSING_METHOD, 27, 7), new ExifTag(TAG_GPS_AREA_INFORMATION, 28, 7), new ExifTag(TAG_GPS_DATESTAMP, 29, 2), new ExifTag(TAG_GPS_DIFFERENTIAL, 30, 3)};
    private static final int IFD_INTEROPERABILITY_HINT = 3;
    private static final ExifTag[] IFD_INTEROPERABILITY_TAGS = new ExifTag[]{new ExifTag(TAG_INTEROPERABILITY_INDEX, 1, 2)};
    private static final ExifTag[] IFD_POINTER_TAGS = new ExifTag[]{new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4), new ExifTag(TAG_GPS_INFO_IFD_POINTER, (int) GLES30.GL_DRAW_BUFFER0, 4), new ExifTag(TAG_INTEROPERABILITY_IFD_POINTER, 40965, 4)};
    private static final int[] IFD_POINTER_TAG_HINTS = new int[]{1, 2, 3};
    private static final int IFD_THUMBNAIL_HINT = 4;
    private static final ExifTag[] IFD_THUMBNAIL_TAGS = new ExifTag[]{new ExifTag(TAG_THUMBNAIL_IMAGE_WIDTH, 256, 3, 4), new ExifTag(TAG_THUMBNAIL_IMAGE_LENGTH, 257, 3, 4), new ExifTag(TAG_BITS_PER_SAMPLE, 258, 3), new ExifTag(TAG_COMPRESSION, 259, 3), new ExifTag(TAG_PHOTOMETRIC_INTERPRETATION, (int) R.styleable.Theme_dialogPreferredPadding, 3), new ExifTag(TAG_IMAGE_DESCRIPTION, 270, 2), new ExifTag(TAG_MAKE, 271, 2), new ExifTag(TAG_MODEL, (int) Device.COMPUTER_HANDHELD_PC_PDA, 2), new ExifTag(TAG_STRIP_OFFSETS, 3, 4), new ExifTag(TAG_ORIENTATION, 274, 3), new ExifTag(TAG_SAMPLES_PER_PIXEL, 277, 3), new ExifTag(TAG_ROWS_PER_STRIP, 278, 3, 4), new ExifTag(TAG_STRIP_BYTE_COUNTS, 279, 3, 4), new ExifTag(TAG_X_RESOLUTION, (int) IActivityManager.CREATE_STACK_ON_DISPLAY, 5), new ExifTag(TAG_Y_RESOLUTION, (int) IActivityManager.GET_FOCUSED_STACK_ID_TRANSACTION, 5), new ExifTag(TAG_PLANAR_CONFIGURATION, 284, 3), new ExifTag(TAG_RESOLUTION_UNIT, (int) IActivityManager.UPDATE_DEVICE_OWNER_TRANSACTION, 3), new ExifTag(TAG_TRANSFER_FUNCTION, 301, 3), new ExifTag(TAG_SOFTWARE, 305, 2), new ExifTag(TAG_DATETIME, 306, 2), new ExifTag(TAG_ARTIST, 315, 2), new ExifTag(TAG_WHITE_POINT, 318, 5), new ExifTag(TAG_PRIMARY_CHROMATICITIES, 319, 5), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, 513, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4), new ExifTag(TAG_Y_CB_CR_COEFFICIENTS, 529, 5), new ExifTag(TAG_Y_CB_CR_SUB_SAMPLING, 530, 3), new ExifTag(TAG_Y_CB_CR_POSITIONING, 531, 3), new ExifTag(TAG_REFERENCE_BLACK_WHITE, (int) Device.PHONE_ISDN, 5), new ExifTag(TAG_COPYRIGHT, 33432, 2), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4), new ExifTag(TAG_GPS_INFO_IFD_POINTER, (int) GLES30.GL_DRAW_BUFFER0, 4)};
    private static final int IFD_TIFF_HINT = 0;
    private static final ExifTag[] IFD_TIFF_TAGS = new ExifTag[]{new ExifTag(TAG_IMAGE_WIDTH, 256, 3, 4), new ExifTag(TAG_IMAGE_LENGTH, 257, 3, 4), new ExifTag(TAG_BITS_PER_SAMPLE, 258, 3), new ExifTag(TAG_COMPRESSION, 259, 3), new ExifTag(TAG_PHOTOMETRIC_INTERPRETATION, (int) R.styleable.Theme_dialogPreferredPadding, 3), new ExifTag(TAG_IMAGE_DESCRIPTION, 270, 2), new ExifTag(TAG_MAKE, 271, 2), new ExifTag(TAG_MODEL, (int) Device.COMPUTER_HANDHELD_PC_PDA, 2), new ExifTag(TAG_STRIP_OFFSETS, 273, 3, 4), new ExifTag(TAG_ORIENTATION, 274, 3), new ExifTag(TAG_SAMPLES_PER_PIXEL, 277, 3), new ExifTag(TAG_ROWS_PER_STRIP, 278, 3, 4), new ExifTag(TAG_STRIP_BYTE_COUNTS, 279, 3, 4), new ExifTag(TAG_X_RESOLUTION, (int) IActivityManager.CREATE_STACK_ON_DISPLAY, 5), new ExifTag(TAG_Y_RESOLUTION, (int) IActivityManager.GET_FOCUSED_STACK_ID_TRANSACTION, 5), new ExifTag(TAG_PLANAR_CONFIGURATION, 284, 3), new ExifTag(TAG_RESOLUTION_UNIT, (int) IActivityManager.UPDATE_DEVICE_OWNER_TRANSACTION, 3), new ExifTag(TAG_TRANSFER_FUNCTION, 301, 3), new ExifTag(TAG_SOFTWARE, 305, 2), new ExifTag(TAG_DATETIME, 306, 2), new ExifTag(TAG_ARTIST, 315, 2), new ExifTag(TAG_WHITE_POINT, 318, 5), new ExifTag(TAG_PRIMARY_CHROMATICITIES, 319, 5), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, 513, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4), new ExifTag(TAG_Y_CB_CR_COEFFICIENTS, 529, 5), new ExifTag(TAG_Y_CB_CR_SUB_SAMPLING, 530, 3), new ExifTag(TAG_Y_CB_CR_POSITIONING, 531, 3), new ExifTag(TAG_REFERENCE_BLACK_WHITE, (int) Device.PHONE_ISDN, 5), new ExifTag(TAG_COPYRIGHT, 33432, 2), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4), new ExifTag(TAG_GPS_INFO_IFD_POINTER, (int) GLES30.GL_DRAW_BUFFER0, 4)};
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_LENGTH_TAG = new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4);
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_TAG = new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, 513, 4);
    private static final byte MARKER = (byte) -1;
    private static final byte MARKER_APP1 = (byte) -31;
    private static final byte MARKER_COM = (byte) -2;
    private static final byte MARKER_EOI = (byte) -39;
    private static final byte MARKER_SOF0 = (byte) -64;
    private static final byte MARKER_SOF1 = (byte) -63;
    private static final byte MARKER_SOF10 = (byte) -54;
    private static final byte MARKER_SOF11 = (byte) -53;
    private static final byte MARKER_SOF13 = (byte) -51;
    private static final byte MARKER_SOF14 = (byte) -50;
    private static final byte MARKER_SOF15 = (byte) -49;
    private static final byte MARKER_SOF2 = (byte) -62;
    private static final byte MARKER_SOF3 = (byte) -61;
    private static final byte MARKER_SOF5 = (byte) -59;
    private static final byte MARKER_SOF6 = (byte) -58;
    private static final byte MARKER_SOF7 = (byte) -57;
    private static final byte MARKER_SOF9 = (byte) -55;
    private static final byte MARKER_SOI = (byte) -40;
    private static final byte MARKER_SOS = (byte) -38;
    public static final int ORIENTATION_FLIP_HORIZONTAL = 2;
    public static final int ORIENTATION_FLIP_VERTICAL = 4;
    public static final int ORIENTATION_NORMAL = 1;
    public static final int ORIENTATION_ROTATE_180 = 3;
    public static final int ORIENTATION_ROTATE_270 = 8;
    public static final int ORIENTATION_ROTATE_90 = 6;
    public static final int ORIENTATION_TRANSPOSE = 5;
    public static final int ORIENTATION_TRANSVERSE = 7;
    public static final int ORIENTATION_UNDEFINED = 0;
    private static final String TAG = "ExifInterface";
    public static final String TAG_APERTURE = "FNumber";
    public static final String TAG_APERTURE_VALUE = "ApertureValue";
    public static final String TAG_ARTIST = "Artist";
    public static final String TAG_BITS_PER_SAMPLE = "BitsPerSample";
    public static final String TAG_BRIGHTNESS_VALUE = "BrightnessValue";
    public static final String TAG_CFA_PATTERN = "CFAPattern";
    public static final String TAG_COLOR_SPACE = "ColorSpace";
    public static final String TAG_COMPONENTS_CONFIGURATION = "ComponentsConfiguration";
    public static final String TAG_COMPRESSED_BITS_PER_PIXEL = "CompressedBitsPerPixel";
    public static final String TAG_COMPRESSION = "Compression";
    public static final String TAG_CONTRAST = "Contrast";
    public static final String TAG_COPYRIGHT = "Copyright";
    public static final String TAG_CUSTOM_RENDERED = "CustomRendered";
    public static final String TAG_DATETIME = "DateTime";
    public static final String TAG_DATETIME_DIGITIZED = "DateTimeDigitized";
    public static final String TAG_DATETIME_ORIGINAL = "DateTimeOriginal";
    public static final String TAG_DEVICE_SETTING_DESCRIPTION = "DeviceSettingDescription";
    public static final String TAG_DIGITAL_ZOOM_RATIO = "DigitalZoomRatio";
    private static final String TAG_EXIF_IFD_POINTER = "ExifIFDPointer";
    public static final String TAG_EXIF_VERSION = "ExifVersion";
    public static final String TAG_EXPOSURE_BIAS_VALUE = "ExposureBiasValue";
    public static final String TAG_EXPOSURE_INDEX = "ExposureIndex";
    public static final String TAG_EXPOSURE_MODE = "ExposureMode";
    public static final String TAG_EXPOSURE_PROGRAM = "ExposureProgram";
    public static final String TAG_EXPOSURE_TIME = "ExposureTime";
    public static final String TAG_FILE_SOURCE = "FileSource";
    public static final String TAG_FLASH = "Flash";
    public static final String TAG_FLASHPIX_VERSION = "FlashpixVersion";
    public static final String TAG_FLASH_ENERGY = "FlashEnergy";
    public static final String TAG_FOCAL_LENGTH = "FocalLength";
    public static final String TAG_FOCAL_LENGTH_IN_35MM_FILM = "FocalLengthIn35mmFilm";
    public static final String TAG_FOCAL_PLANE_RESOLUTION_UNIT = "FocalPlaneResolutionUnit";
    public static final String TAG_FOCAL_PLANE_X_RESOLUTION = "FocalPlaneXResolution";
    public static final String TAG_FOCAL_PLANE_Y_RESOLUTION = "FocalPlaneYResolution";
    public static final String TAG_GAIN_CONTROL = "GainControl";
    public static final String TAG_GPS_ALTITUDE = "GPSAltitude";
    public static final String TAG_GPS_ALTITUDE_REF = "GPSAltitudeRef";
    public static final String TAG_GPS_AREA_INFORMATION = "GPSAreaInformation";
    public static final String TAG_GPS_DATESTAMP = "GPSDateStamp";
    public static final String TAG_GPS_DEST_BEARING = "GPSDestBearing";
    public static final String TAG_GPS_DEST_BEARING_REF = "GPSDestBearingRef";
    public static final String TAG_GPS_DEST_DISTANCE = "GPSDestDistance";
    public static final String TAG_GPS_DEST_DISTANCE_REF = "GPSDestDistanceRef";
    public static final String TAG_GPS_DEST_LATITUDE = "GPSDestLatitude";
    public static final String TAG_GPS_DEST_LATITUDE_REF = "GPSDestLatitudeRef";
    public static final String TAG_GPS_DEST_LONGITUDE = "GPSDestLongitude";
    public static final String TAG_GPS_DEST_LONGITUDE_REF = "GPSDestLongitudeRef";
    public static final String TAG_GPS_DIFFERENTIAL = "GPSDifferential";
    public static final String TAG_GPS_DOP = "GPSDOP";
    public static final String TAG_GPS_IMG_DIRECTION = "GPSImgDirection";
    public static final String TAG_GPS_IMG_DIRECTION_REF = "GPSImgDirectionRef";
    private static final String TAG_GPS_INFO_IFD_POINTER = "GPSInfoIFDPointer";
    public static final String TAG_GPS_LATITUDE = "GPSLatitude";
    public static final String TAG_GPS_LATITUDE_REF = "GPSLatitudeRef";
    public static final String TAG_GPS_LONGITUDE = "GPSLongitude";
    public static final String TAG_GPS_LONGITUDE_REF = "GPSLongitudeRef";
    public static final String TAG_GPS_MAP_DATUM = "GPSMapDatum";
    public static final String TAG_GPS_MEASURE_MODE = "GPSMeasureMode";
    public static final String TAG_GPS_PROCESSING_METHOD = "GPSProcessingMethod";
    public static final String TAG_GPS_SATELLITES = "GPSSatellites";
    public static final String TAG_GPS_SPEED = "GPSSpeed";
    public static final String TAG_GPS_SPEED_REF = "GPSSpeedRef";
    public static final String TAG_GPS_STATUS = "GPSStatus";
    public static final String TAG_GPS_TIMESTAMP = "GPSTimeStamp";
    public static final String TAG_GPS_TRACK = "GPSTrack";
    public static final String TAG_GPS_TRACK_REF = "GPSTrackRef";
    public static final String TAG_GPS_VERSION_ID = "GPSVersionID";
    private static final String TAG_HAS_THUMBNAIL = "HasThumbnail";
    public static final String TAG_IMAGE_DESCRIPTION = "ImageDescription";
    public static final String TAG_IMAGE_LENGTH = "ImageLength";
    public static final String TAG_IMAGE_UNIQUE_ID = "ImageUniqueID";
    public static final String TAG_IMAGE_WIDTH = "ImageWidth";
    private static final String TAG_INTEROPERABILITY_IFD_POINTER = "InteroperabilityIFDPointer";
    public static final String TAG_INTEROPERABILITY_INDEX = "InteroperabilityIndex";
    public static final String TAG_ISO = "ISOSpeedRatings";
    public static final String TAG_JPEG_INTERCHANGE_FORMAT = "JPEGInterchangeFormat";
    public static final String TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = "JPEGInterchangeFormatLength";
    public static final String TAG_LIGHT_SOURCE = "LightSource";
    public static final String TAG_MAKE = "Make";
    public static final String TAG_MAKER_NOTE = "MakerNote";
    public static final String TAG_MAX_APERTURE_VALUE = "MaxApertureValue";
    public static final String TAG_METERING_MODE = "MeteringMode";
    public static final String TAG_MODEL = "Model";
    public static final String TAG_OECF = "OECF";
    public static final String TAG_ORIENTATION = "Orientation";
    public static final String TAG_PHOTOMETRIC_INTERPRETATION = "PhotometricInterpretation";
    public static final String TAG_PIXEL_X_DIMENSION = "PixelXDimension";
    public static final String TAG_PIXEL_Y_DIMENSION = "PixelYDimension";
    public static final String TAG_PLANAR_CONFIGURATION = "PlanarConfiguration";
    public static final String TAG_PRIMARY_CHROMATICITIES = "PrimaryChromaticities";
    public static final String TAG_REFERENCE_BLACK_WHITE = "ReferenceBlackWhite";
    public static final String TAG_RELATED_SOUND_FILE = "RelatedSoundFile";
    public static final String TAG_RESOLUTION_UNIT = "ResolutionUnit";
    public static final String TAG_ROWS_PER_STRIP = "RowsPerStrip";
    public static final String TAG_SAMPLES_PER_PIXEL = "SamplesPerPixel";
    public static final String TAG_SATURATION = "Saturation";
    public static final String TAG_SCENE_CAPTURE_TYPE = "SceneCaptureType";
    public static final String TAG_SCENE_TYPE = "SceneType";
    public static final String TAG_SENSING_METHOD = "SensingMethod";
    public static final String TAG_SHARPNESS = "Sharpness";
    public static final String TAG_SHUTTER_SPEED_VALUE = "ShutterSpeedValue";
    public static final String TAG_SOFTWARE = "Software";
    public static final String TAG_SPATIAL_FREQUENCY_RESPONSE = "SpatialFrequencyResponse";
    public static final String TAG_SPECTRAL_SENSITIVITY = "SpectralSensitivity";
    public static final String TAG_STRIP_BYTE_COUNTS = "StripByteCounts";
    public static final String TAG_STRIP_OFFSETS = "StripOffsets";
    public static final String TAG_SUBJECT_AREA = "SubjectArea";
    public static final String TAG_SUBJECT_DISTANCE = "SubjectDistance";
    public static final String TAG_SUBJECT_DISTANCE_RANGE = "SubjectDistanceRange";
    public static final String TAG_SUBJECT_LOCATION = "SubjectLocation";
    public static final String TAG_SUBSEC_TIME = "SubSecTime";
    public static final String TAG_SUBSEC_TIME_DIG = "SubSecTimeDigitized";
    public static final String TAG_SUBSEC_TIME_ORIG = "SubSecTimeOriginal";
    private static final String TAG_THUMBNAIL_DATA = "ThumbnailData";
    public static final String TAG_THUMBNAIL_IMAGE_LENGTH = "ThumbnailImageLength";
    public static final String TAG_THUMBNAIL_IMAGE_WIDTH = "ThumbnailImageWidth";
    private static final String TAG_THUMBNAIL_LENGTH = "ThumbnailLength";
    private static final String TAG_THUMBNAIL_OFFSET = "ThumbnailOffset";
    public static final String TAG_TRANSFER_FUNCTION = "TransferFunction";
    public static final String TAG_USER_COMMENT = "UserComment";
    public static final String TAG_WHITE_BALANCE = "WhiteBalance";
    public static final String TAG_WHITE_POINT = "WhitePoint";
    public static final String TAG_X_RESOLUTION = "XResolution";
    public static final String TAG_Y_CB_CR_COEFFICIENTS = "YCbCrCoefficients";
    public static final String TAG_Y_CB_CR_POSITIONING = "YCbCrPositioning";
    public static final String TAG_Y_CB_CR_SUB_SAMPLING = "YCbCrSubSampling";
    public static final String TAG_Y_RESOLUTION = "YResolution";
    public static final int WHITEBALANCE_AUTO = 0;
    public static final int WHITEBALANCE_MANUAL = 1;
    private static final HashMap[] sExifTagMapsForReading = new HashMap[EXIF_TAGS.length];
    private static final HashMap[] sExifTagMapsForWriting = new HashMap[EXIF_TAGS.length];
    private static SimpleDateFormat sFormatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    private static final Pattern sGpsTimestampPattern = Pattern.compile("^([0-9][0-9]):([0-9][0-9]):([0-9][0-9])$");
    private static final Pattern sNonZeroTimePattern = Pattern.compile(".*[1-9].*");
    private static final HashSet<String> sTagSetForCompatibility = new HashSet(Arrays.asList(new String[]{TAG_APERTURE, TAG_DIGITAL_ZOOM_RATIO, TAG_EXPOSURE_TIME, TAG_SUBJECT_DISTANCE, TAG_GPS_TIMESTAMP}));
    private final HashMap[] mAttributes = new HashMap[EXIF_TAGS.length];
    private ByteOrder mExifByteOrder = ByteOrder.BIG_ENDIAN;
    private final String mFilename;
    private boolean mHasThumbnail;
    private boolean mIsSupportedFile;
    private byte[] mThumbnailBytes;
    private int mThumbnailLength;
    private int mThumbnailOffset;

    private static class ByteOrderAwarenessDataInputStream extends ByteArrayInputStream {
        private static final ByteOrder BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
        private static final ByteOrder LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
        private ByteOrder mByteOrder = ByteOrder.BIG_ENDIAN;
        private final long mLength;
        private long mPosition;

        public ByteOrderAwarenessDataInputStream(byte[] bytes) {
            super(bytes);
            this.mLength = (long) bytes.length;
            this.mPosition = 0;
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        public void seek(long byteCount) throws IOException {
            this.mPosition = 0;
            reset();
            if (skip(byteCount) != byteCount) {
                throw new IOException("Couldn't seek up to the byteCount");
            }
        }

        public long peek() {
            return this.mPosition;
        }

        public void readFully(byte[] buffer) throws IOException {
            this.mPosition += (long) buffer.length;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            } else if (super.read(buffer, 0, buffer.length) != buffer.length) {
                throw new IOException("Couldn't read up to the length of buffer");
            }
        }

        public byte readByte() throws IOException {
            this.mPosition++;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            int ch = super.read();
            if (ch >= 0) {
                return (byte) ch;
            }
            throw new EOFException();
        }

        public short readShort() throws IOException {
            this.mPosition += 2;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            int ch1 = super.read();
            int ch2 = super.read();
            if ((ch1 | ch2) < 0) {
                throw new EOFException();
            } else if (this.mByteOrder == LITTLE_ENDIAN) {
                return (short) ((ch2 << 8) + ch1);
            } else {
                if (this.mByteOrder == BIG_ENDIAN) {
                    return (short) ((ch1 << 8) + ch2);
                }
                throw new IOException("Invalid byte order: " + this.mByteOrder);
            }
        }

        public int readInt() throws IOException {
            this.mPosition += 4;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            int ch1 = super.read();
            int ch2 = super.read();
            int ch3 = super.read();
            int ch4 = super.read();
            if ((((ch1 | ch2) | ch3) | ch4) < 0) {
                throw new EOFException();
            } else if (this.mByteOrder == LITTLE_ENDIAN) {
                return (((ch4 << 24) + (ch3 << 16)) + (ch2 << 8)) + ch1;
            } else {
                if (this.mByteOrder == BIG_ENDIAN) {
                    return (((ch1 << 24) + (ch2 << 16)) + (ch3 << 8)) + ch4;
                }
                throw new IOException("Invalid byte order: " + this.mByteOrder);
            }
        }

        public long skip(long byteCount) {
            long skipped = super.skip(Math.min(byteCount, this.mLength - this.mPosition));
            this.mPosition += skipped;
            return skipped;
        }

        public int readUnsignedShort() throws IOException {
            this.mPosition += 2;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            int ch1 = super.read();
            int ch2 = super.read();
            if ((ch1 | ch2) < 0) {
                throw new EOFException();
            } else if (this.mByteOrder == LITTLE_ENDIAN) {
                return (ch2 << 8) + ch1;
            } else {
                if (this.mByteOrder == BIG_ENDIAN) {
                    return (ch1 << 8) + ch2;
                }
                throw new IOException("Invalid byte order: " + this.mByteOrder);
            }
        }

        public long readUnsignedInt() throws IOException {
            return ((long) readInt()) & 4294967295L;
        }

        public long readLong() throws IOException {
            this.mPosition += 8;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            int ch1 = super.read();
            int ch2 = super.read();
            int ch3 = super.read();
            int ch4 = super.read();
            int ch5 = super.read();
            int ch6 = super.read();
            int ch7 = super.read();
            int ch8 = super.read();
            if ((((((((ch1 | ch2) | ch3) | ch4) | ch5) | ch6) | ch7) | ch8) < 0) {
                throw new EOFException();
            } else if (this.mByteOrder == LITTLE_ENDIAN) {
                return (((((((((long) ch8) << 56) + (((long) ch7) << 48)) + (((long) ch6) << 40)) + (((long) ch5) << 32)) + (((long) ch4) << 24)) + (((long) ch3) << 16)) + (((long) ch2) << 8)) + ((long) ch1);
            } else {
                if (this.mByteOrder == BIG_ENDIAN) {
                    return (((((((((long) ch1) << 56) + (((long) ch2) << 48)) + (((long) ch3) << 40)) + (((long) ch4) << 32)) + (((long) ch5) << 24)) + (((long) ch6) << 16)) + (((long) ch7) << 8)) + ((long) ch8);
                }
                throw new IOException("Invalid byte order: " + this.mByteOrder);
            }
        }

        public float readFloat() throws IOException {
            return Float.intBitsToFloat(readInt());
        }

        public double readDouble() throws IOException {
            return Double.longBitsToDouble(readLong());
        }
    }

    private static class ByteOrderAwarenessDataOutputStream extends FilterOutputStream {
        private ByteOrder mByteOrder;
        private final OutputStream mOutputStream;

        public ByteOrderAwarenessDataOutputStream(OutputStream out, ByteOrder byteOrder) {
            super(out);
            this.mOutputStream = out;
            this.mByteOrder = byteOrder;
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        public void write(byte[] bytes) throws IOException {
            this.mOutputStream.write(bytes);
        }

        public void write(byte[] bytes, int offset, int length) throws IOException {
            this.mOutputStream.write(bytes, offset, length);
        }

        public void writeByte(int val) throws IOException {
            this.mOutputStream.write(val);
        }

        public void writeShort(short val) throws IOException {
            if (this.mByteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write((val >>> 0) & 255);
                this.mOutputStream.write((val >>> 8) & 255);
            } else if (this.mByteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write((val >>> 8) & 255);
                this.mOutputStream.write((val >>> 0) & 255);
            }
        }

        public void writeInt(int val) throws IOException {
            if (this.mByteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write((val >>> 0) & 255);
                this.mOutputStream.write((val >>> 8) & 255);
                this.mOutputStream.write((val >>> 16) & 255);
                this.mOutputStream.write((val >>> 24) & 255);
            } else if (this.mByteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write((val >>> 24) & 255);
                this.mOutputStream.write((val >>> 16) & 255);
                this.mOutputStream.write((val >>> 8) & 255);
                this.mOutputStream.write((val >>> 0) & 255);
            }
        }

        public void writeUnsignedShort(int val) throws IOException {
            writeShort((short) val);
        }

        public void writeUnsignedInt(long val) throws IOException {
            writeInt((int) val);
        }
    }

    private static class ExifAttribute {
        public final byte[] bytes;
        public final int format;
        public final int numberOfComponents;

        private ExifAttribute(int format, int numberOfComponents, byte[] bytes) {
            this.format = format;
            this.numberOfComponents = numberOfComponents;
            this.bytes = bytes;
        }

        public static ExifAttribute createUShort(int[] values, ByteOrder byteOrder) {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * values.length)]);
            buffer.order(byteOrder);
            for (int value : values) {
                buffer.putShort((short) value);
            }
            return new ExifAttribute(3, values.length, buffer.array());
        }

        public static ExifAttribute createUShort(int value, ByteOrder byteOrder) {
            return createUShort(new int[]{value}, byteOrder);
        }

        public static ExifAttribute createULong(long[] values, ByteOrder byteOrder) {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * values.length)]);
            buffer.order(byteOrder);
            for (long value : values) {
                buffer.putInt((int) value);
            }
            return new ExifAttribute(4, values.length, buffer.array());
        }

        public static ExifAttribute createULong(long value, ByteOrder byteOrder) {
            return createULong(new long[]{value}, byteOrder);
        }

        public static ExifAttribute createSLong(int[] values, ByteOrder byteOrder) {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[9] * values.length)]);
            buffer.order(byteOrder);
            for (int value : values) {
                buffer.putInt(value);
            }
            return new ExifAttribute(9, values.length, buffer.array());
        }

        public static ExifAttribute createSLong(int value, ByteOrder byteOrder) {
            return createSLong(new int[]{value}, byteOrder);
        }

        public static ExifAttribute createByte(String value) {
            if (value.length() != 1 || value.charAt(0) < '0' || value.charAt(0) > '1') {
                byte[] ascii = value.getBytes(ExifInterface.ASCII);
                return new ExifAttribute(1, ascii.length, ascii);
            }
            byte[] bytes = new byte[]{(byte) (value.charAt(0) - 48)};
            return new ExifAttribute(1, bytes.length, bytes);
        }

        public static ExifAttribute createString(String value) {
            byte[] ascii = (value + '\u0000').getBytes(ExifInterface.ASCII);
            return new ExifAttribute(2, ascii.length, ascii);
        }

        public static ExifAttribute createURational(Rational[] values, ByteOrder byteOrder) {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * values.length)]);
            buffer.order(byteOrder);
            for (Rational value : values) {
                buffer.putInt((int) value.numerator);
                buffer.putInt((int) value.denominator);
            }
            return new ExifAttribute(5, values.length, buffer.array());
        }

        public static ExifAttribute createURational(Rational value, ByteOrder byteOrder) {
            return createURational(new Rational[]{value}, byteOrder);
        }

        public static ExifAttribute createSRational(Rational[] values, ByteOrder byteOrder) {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[10] * values.length)]);
            buffer.order(byteOrder);
            for (Rational value : values) {
                buffer.putInt((int) value.numerator);
                buffer.putInt((int) value.denominator);
            }
            return new ExifAttribute(10, values.length, buffer.array());
        }

        public static ExifAttribute createSRational(Rational value, ByteOrder byteOrder) {
            return createSRational(new Rational[]{value}, byteOrder);
        }

        public static ExifAttribute createDouble(double[] values, ByteOrder byteOrder) {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[12] * values.length)]);
            buffer.order(byteOrder);
            for (double value : values) {
                buffer.putDouble(value);
            }
            return new ExifAttribute(12, values.length, buffer.array());
        }

        public static ExifAttribute createDouble(double value, ByteOrder byteOrder) {
            return createDouble(new double[]{value}, byteOrder);
        }

        public String toString() {
            return "(" + ExifInterface.IFD_FORMAT_NAMES[this.format] + ", data length:" + this.bytes.length + ")";
        }

        private Object getValue(ByteOrder byteOrder) {
            try {
                ByteOrderAwarenessDataInputStream inputStream = new ByteOrderAwarenessDataInputStream(this.bytes);
                inputStream.setByteOrder(byteOrder);
                int i;
                Object values;
                switch (this.format) {
                    case 1:
                    case 6:
                        if (this.bytes.length != 1 || this.bytes[0] < (byte) 0 || this.bytes[0] > (byte) 1) {
                            return new String(this.bytes, ExifInterface.ASCII);
                        }
                        return new String(new char[]{(char) (this.bytes[0] + 48)});
                    case 2:
                    case 7:
                        int index = 0;
                        if (this.numberOfComponents >= ExifInterface.EXIF_ASCII_PREFIX.length) {
                            boolean same = true;
                            i = 0;
                            while (i < ExifInterface.EXIF_ASCII_PREFIX.length) {
                                if (this.bytes[i] != ExifInterface.EXIF_ASCII_PREFIX[i]) {
                                    same = false;
                                    if (same) {
                                        index = ExifInterface.EXIF_ASCII_PREFIX.length;
                                    }
                                } else {
                                    i++;
                                }
                            }
                            if (same) {
                                index = ExifInterface.EXIF_ASCII_PREFIX.length;
                            }
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        for (index = 
/*
Method generation error in method: android.media.ExifInterface.ExifAttribute.getValue(java.nio.ByteOrder):java.lang.Object
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r11_2 'index' int) = (r11_0 'index' int), (r11_0 'index' int), (r11_1 'index' int) binds: {(r11_0 'index' int)=B:24:0x008b, (r11_0 'index' int)=B:17:0x0071, (r11_1 'index' int)=B:25:0x008d} in method: android.media.ExifInterface.ExifAttribute.getValue(java.nio.ByteOrder):java.lang.Object
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeSwitch(RegionGen.java:264)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:277)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:183)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:328)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:265)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:228)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:118)
	at jadx.core.codegen.ClassGen.addInnerClasses(ClassGen.java:241)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:118)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:83)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:19)
	at jadx.core.ProcessClass.process(ProcessClass.java:43)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:530)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:514)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 28 more

*/

                        public double getDoubleValue(ByteOrder byteOrder) {
                            Object value = getValue(byteOrder);
                            if (value == null) {
                                throw new NumberFormatException("NULL can't be converted to a double value");
                            } else if (value instanceof String) {
                                return Double.parseDouble((String) value);
                            } else {
                                if (value instanceof long[]) {
                                    long[] array = (long[]) value;
                                    if (array.length == 1) {
                                        return (double) array[0];
                                    }
                                    throw new NumberFormatException("There are more than one component");
                                } else if (value instanceof int[]) {
                                    int[] array2 = (int[]) value;
                                    if (array2.length == 1) {
                                        return (double) array2[0];
                                    }
                                    throw new NumberFormatException("There are more than one component");
                                } else if (value instanceof double[]) {
                                    double[] array3 = (double[]) value;
                                    if (array3.length == 1) {
                                        return array3[0];
                                    }
                                    throw new NumberFormatException("There are more than one component");
                                } else if (value instanceof Rational[]) {
                                    Rational[] array4 = (Rational[]) value;
                                    if (array4.length == 1) {
                                        return array4[0].calculate();
                                    }
                                    throw new NumberFormatException("There are more than one component");
                                } else {
                                    throw new NumberFormatException("Couldn't find a double value");
                                }
                            }
                        }

                        public int getIntValue(ByteOrder byteOrder) {
                            Object value = getValue(byteOrder);
                            if (value == null) {
                                throw new NumberFormatException("NULL can't be converted to a integer value");
                            } else if (value instanceof String) {
                                return Integer.parseInt((String) value);
                            } else {
                                if (value instanceof long[]) {
                                    long[] array = (long[]) value;
                                    if (array.length == 1) {
                                        return (int) array[0];
                                    }
                                    throw new NumberFormatException("There are more than one component");
                                } else if (value instanceof int[]) {
                                    int[] array2 = (int[]) value;
                                    if (array2.length == 1) {
                                        return array2[0];
                                    }
                                    throw new NumberFormatException("There are more than one component");
                                } else {
                                    throw new NumberFormatException("Couldn't find a integer value");
                                }
                            }
                        }

                        public String getStringValue(ByteOrder byteOrder) {
                            Object value = getValue(byteOrder);
                            if (value == null) {
                                return null;
                            }
                            if (value instanceof String) {
                                return (String) value;
                            }
                            StringBuilder stringBuilder = new StringBuilder();
                            int i;
                            if (value instanceof long[]) {
                                long[] array = (long[]) value;
                                for (i = 0; i < array.length; i++) {
                                    stringBuilder.append(array[i]);
                                    if (i + 1 != array.length) {
                                        stringBuilder.append(",");
                                    }
                                }
                                return stringBuilder.toString();
                            } else if (value instanceof int[]) {
                                int[] array2 = (int[]) value;
                                for (i = 0; i < array2.length; i++) {
                                    stringBuilder.append(array2[i]);
                                    if (i + 1 != array2.length) {
                                        stringBuilder.append(",");
                                    }
                                }
                                return stringBuilder.toString();
                            } else if (value instanceof double[]) {
                                double[] array3 = (double[]) value;
                                for (i = 0; i < array3.length; i++) {
                                    stringBuilder.append(array3[i]);
                                    if (i + 1 != array3.length) {
                                        stringBuilder.append(",");
                                    }
                                }
                                return stringBuilder.toString();
                            } else if (!(value instanceof Rational[])) {
                                return null;
                            } else {
                                Rational[] array4 = (Rational[]) value;
                                for (i = 0; i < array4.length; i++) {
                                    stringBuilder.append(array4[i].numerator);
                                    stringBuilder.append('/');
                                    stringBuilder.append(array4[i].denominator);
                                    if (i + 1 != array4.length) {
                                        stringBuilder.append(",");
                                    }
                                }
                                return stringBuilder.toString();
                            }
                        }

                        public int size() {
                            return ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[this.format] * this.numberOfComponents;
                        }
                    }

                    private static class ExifTag {
                        public final String name;
                        public final int number;
                        public final int primaryFormat;
                        public final int secondaryFormat;

                        private ExifTag(String name, int number, int format) {
                            this.name = name;
                            this.number = number;
                            this.primaryFormat = format;
                            this.secondaryFormat = -1;
                        }

                        private ExifTag(String name, int number, int primaryFormat, int secondaryFormat) {
                            this.name = name;
                            this.number = number;
                            this.primaryFormat = primaryFormat;
                            this.secondaryFormat = secondaryFormat;
                        }
                    }

                    private static class Rational {
                        public final long denominator;
                        public final long numerator;

                        private Rational(long numerator, long denominator) {
                            if (denominator == 0) {
                                this.numerator = 0;
                                this.denominator = 1;
                                return;
                            }
                            this.numerator = numerator;
                            this.denominator = denominator;
                        }

                        public String toString() {
                            return this.numerator + "/" + this.denominator;
                        }

                        public double calculate() {
                            return ((double) this.numerator) / ((double) this.denominator);
                        }
                    }

                    static {
                        sFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                        for (int hint = 0; hint < EXIF_TAGS.length; hint++) {
                            sExifTagMapsForReading[hint] = new HashMap();
                            sExifTagMapsForWriting[hint] = new HashMap();
                            for (ExifTag tag : EXIF_TAGS[hint]) {
                                sExifTagMapsForReading[hint].put(Integer.valueOf(tag.number), tag);
                                sExifTagMapsForWriting[hint].put(tag.name, tag);
                            }
                        }
                    }

                    public ExifInterface(String filename) throws IOException {
                        if (filename == null) {
                            throw new IllegalArgumentException("filename cannot be null");
                        }
                        this.mFilename = filename;
                        loadAttributes();
                    }

                    private ExifAttribute getExifAttribute(String tag) {
                        for (int i = 0; i < EXIF_TAGS.length; i++) {
                            Object value = this.mAttributes[i].get(tag);
                            if (value != null) {
                                return (ExifAttribute) value;
                            }
                        }
                        return null;
                    }

                    public String getAttribute(String tag) {
                        ExifAttribute attribute = getExifAttribute(tag);
                        if (attribute == null) {
                            return null;
                        }
                        if (!sTagSetForCompatibility.contains(tag)) {
                            return attribute.getStringValue(this.mExifByteOrder);
                        }
                        if (!tag.equals(TAG_GPS_TIMESTAMP)) {
                            try {
                                return Double.toString(attribute.getDoubleValue(this.mExifByteOrder));
                            } catch (NumberFormatException e) {
                                return null;
                            }
                        } else if (attribute.format != 5 && attribute.format != 10) {
                            return null;
                        } else {
                            if (((Rational[]) attribute.getValue(this.mExifByteOrder)).length != 3) {
                                return null;
                            }
                            return String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf((int) (((float) ((Rational[]) attribute.getValue(this.mExifByteOrder))[0].numerator) / ((float) ((Rational[]) attribute.getValue(this.mExifByteOrder))[0].denominator))), Integer.valueOf((int) (((float) ((Rational[]) attribute.getValue(this.mExifByteOrder))[1].numerator) / ((float) ((Rational[]) attribute.getValue(this.mExifByteOrder))[1].denominator))), Integer.valueOf((int) (((float) ((Rational[]) attribute.getValue(this.mExifByteOrder))[2].numerator) / ((float) ((Rational[]) attribute.getValue(this.mExifByteOrder))[2].denominator)))});
                        }
                    }

                    public int getAttributeInt(String tag, int defaultValue) {
                        ExifAttribute exifAttribute = getExifAttribute(tag);
                        if (exifAttribute != null) {
                            try {
                                defaultValue = exifAttribute.getIntValue(this.mExifByteOrder);
                            } catch (NumberFormatException e) {
                            }
                        }
                        return defaultValue;
                    }

                    public double getAttributeDouble(String tag, double defaultValue) {
                        ExifAttribute exifAttribute = getExifAttribute(tag);
                        if (exifAttribute != null) {
                            try {
                                defaultValue = exifAttribute.getDoubleValue(this.mExifByteOrder);
                            } catch (NumberFormatException e) {
                            }
                        }
                        return defaultValue;
                    }

                    public void setAttribute(String tag, String value) {
                        if (value != null && sTagSetForCompatibility.contains(tag)) {
                            if (tag.equals(TAG_GPS_TIMESTAMP)) {
                                Matcher m = sGpsTimestampPattern.matcher(value);
                                if (m.find()) {
                                    value = Integer.parseInt(m.group(1)) + "/1," + Integer.parseInt(m.group(2)) + "/1," + Integer.parseInt(m.group(3)) + "/1";
                                } else {
                                    Log.w(TAG, "Invalid value for " + tag + " : " + value);
                                    return;
                                }
                            }
                            try {
                                value = ((long) (10000.0d * Double.parseDouble(value))) + "/10000";
                            } catch (NumberFormatException e) {
                                Log.w(TAG, "Invalid value for " + tag + " : " + value);
                                return;
                            }
                        }
                        for (int i = 0; i < EXIF_TAGS.length; i++) {
                            if (i != 4 || this.mHasThumbnail) {
                                ExifTag obj = sExifTagMapsForWriting[i].get(tag);
                                if (obj != null) {
                                    if (value != null) {
                                        int dataFormat;
                                        ExifTag exifTag = obj;
                                        Pair<Integer, Integer> guess = guessDataFormat(value);
                                        if (exifTag.primaryFormat == ((Integer) guess.first).intValue() || exifTag.primaryFormat == ((Integer) guess.second).intValue()) {
                                            dataFormat = exifTag.primaryFormat;
                                        } else if (exifTag.secondaryFormat != -1 && (exifTag.secondaryFormat == ((Integer) guess.first).intValue() || exifTag.secondaryFormat == ((Integer) guess.second).intValue())) {
                                            dataFormat = exifTag.secondaryFormat;
                                        } else if (exifTag.primaryFormat == 1 || exifTag.primaryFormat == 7 || exifTag.primaryFormat == 2) {
                                            dataFormat = exifTag.primaryFormat;
                                        } else {
                                            Log.w(TAG, "Given tag (" + tag + ") value didn't match with one of expected " + "formats: " + IFD_FORMAT_NAMES[exifTag.primaryFormat] + (exifTag.secondaryFormat == -1 ? ProxyInfo.LOCAL_EXCL_LIST : ", " + IFD_FORMAT_NAMES[exifTag.secondaryFormat]) + " (guess: " + IFD_FORMAT_NAMES[((Integer) guess.first).intValue()] + (((Integer) guess.second).intValue() == -1 ? ProxyInfo.LOCAL_EXCL_LIST : ", " + IFD_FORMAT_NAMES[((Integer) guess.second).intValue()]) + ")");
                                        }
                                        String[] values;
                                        int[] intArray;
                                        int j;
                                        Rational[] rationalArray;
                                        String[] numbers;
                                        switch (dataFormat) {
                                            case 1:
                                                this.mAttributes[i].put(tag, ExifAttribute.createByte(value));
                                                break;
                                            case 2:
                                            case 7:
                                                this.mAttributes[i].put(tag, ExifAttribute.createString(value));
                                                break;
                                            case 3:
                                                values = value.split(",");
                                                intArray = new int[values.length];
                                                for (j = 0; j < values.length; j++) {
                                                    intArray[j] = Integer.parseInt(values[j]);
                                                }
                                                this.mAttributes[i].put(tag, ExifAttribute.createUShort(intArray, this.mExifByteOrder));
                                                break;
                                            case 4:
                                                values = value.split(",");
                                                long[] longArray = new long[values.length];
                                                for (j = 0; j < values.length; j++) {
                                                    longArray[j] = Long.parseLong(values[j]);
                                                }
                                                this.mAttributes[i].put(tag, ExifAttribute.createULong(longArray, this.mExifByteOrder));
                                                break;
                                            case 5:
                                                values = value.split(",");
                                                rationalArray = new Rational[values.length];
                                                for (j = 0; j < values.length; j++) {
                                                    numbers = values[j].split("/");
                                                    rationalArray[j] = new Rational(Long.parseLong(numbers[0]), Long.parseLong(numbers[1]));
                                                }
                                                this.mAttributes[i].put(tag, ExifAttribute.createURational(rationalArray, this.mExifByteOrder));
                                                break;
                                            case 9:
                                                values = value.split(",");
                                                intArray = new int[values.length];
                                                for (j = 0; j < values.length; j++) {
                                                    intArray[j] = Integer.parseInt(values[j]);
                                                }
                                                this.mAttributes[i].put(tag, ExifAttribute.createSLong(intArray, this.mExifByteOrder));
                                                break;
                                            case 10:
                                                values = value.split(",");
                                                rationalArray = new Rational[values.length];
                                                for (j = 0; j < values.length; j++) {
                                                    numbers = values[j].split("/");
                                                    rationalArray[j] = new Rational(Long.parseLong(numbers[0]), Long.parseLong(numbers[1]));
                                                }
                                                this.mAttributes[i].put(tag, ExifAttribute.createSRational(rationalArray, this.mExifByteOrder));
                                                break;
                                            case 12:
                                                values = value.split(",");
                                                double[] doubleArray = new double[values.length];
                                                for (j = 0; j < values.length; j++) {
                                                    doubleArray[j] = Double.parseDouble(values[j]);
                                                }
                                                this.mAttributes[i].put(tag, ExifAttribute.createDouble(doubleArray, this.mExifByteOrder));
                                                break;
                                            default:
                                                Log.w(TAG, "Data format isn't one of expected formats: " + dataFormat);
                                                break;
                                        }
                                    }
                                    this.mAttributes[i].remove(tag);
                                }
                            }
                        }
                    }

                    private boolean updateAttribute(String tag, ExifAttribute value) {
                        boolean updated = false;
                        for (int i = 0; i < EXIF_TAGS.length; i++) {
                            if (this.mAttributes[i].containsKey(tag)) {
                                this.mAttributes[i].put(tag, value);
                                updated = true;
                            }
                        }
                        return updated;
                    }

                    private void removeAttribute(String tag) {
                        for (int i = 0; i < EXIF_TAGS.length; i++) {
                            this.mAttributes[i].remove(tag);
                        }
                    }

                    private void loadAttributes() throws IOException {
                        IOException e;
                        Throwable th;
                        for (int i = 0; i < EXIF_TAGS.length; i++) {
                            this.mAttributes[i] = new HashMap();
                        }
                        InputStream in = null;
                        try {
                            InputStream in2 = new FileInputStream(this.mFilename);
                            try {
                                getJpegAttributes(in2);
                                this.mIsSupportedFile = true;
                                addDefaultValuesForCompatibility();
                                IoUtils.closeQuietly(in2);
                                in = in2;
                            } catch (IOException e2) {
                                e = e2;
                                in = in2;
                                try {
                                    this.mIsSupportedFile = false;
                                    Log.w(TAG, "Invalid image.", e);
                                    addDefaultValuesForCompatibility();
                                    IoUtils.closeQuietly(in);
                                } catch (Throwable th2) {
                                    th = th2;
                                    addDefaultValuesForCompatibility();
                                    IoUtils.closeQuietly(in);
                                    throw th;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                in = in2;
                                addDefaultValuesForCompatibility();
                                IoUtils.closeQuietly(in);
                                throw th;
                            }
                        } catch (IOException e3) {
                            e = e3;
                            this.mIsSupportedFile = false;
                            Log.w(TAG, "Invalid image.", e);
                            addDefaultValuesForCompatibility();
                            IoUtils.closeQuietly(in);
                        }
                    }

                    private void printAttributes() {
                        for (int i = 0; i < this.mAttributes.length; i++) {
                            Log.d(TAG, "The size of tag group[" + i + "]: " + this.mAttributes[i].size());
                            for (Entry entry : this.mAttributes[i].entrySet()) {
                                ExifAttribute tagValue = (ExifAttribute) entry.getValue();
                                Log.d(TAG, "tagName: " + entry.getKey() + ", tagType: " + tagValue.toString() + ", tagValue: '" + tagValue.getStringValue(this.mExifByteOrder) + "'");
                            }
                        }
                    }

                    public void saveAttributes() throws IOException {
                        Throwable th;
                        if (this.mIsSupportedFile) {
                            this.mThumbnailBytes = getThumbnail();
                            File tempFile = new File(this.mFilename + ".tmp");
                            if (new File(this.mFilename).renameTo(tempFile)) {
                                FileInputStream in = null;
                                FileOutputStream out = null;
                                try {
                                    FileOutputStream out2;
                                    FileInputStream in2 = new FileInputStream(tempFile);
                                    try {
                                        out2 = new FileOutputStream(this.mFilename);
                                    } catch (Throwable th2) {
                                        th = th2;
                                        in = in2;
                                        IoUtils.closeQuietly(in);
                                        IoUtils.closeQuietly(out);
                                        tempFile.delete();
                                        throw th;
                                    }
                                    try {
                                        saveJpegAttributes(in2, out2);
                                        IoUtils.closeQuietly(in2);
                                        IoUtils.closeQuietly(out2);
                                        tempFile.delete();
                                        this.mThumbnailBytes = null;
                                        return;
                                    } catch (Throwable th3) {
                                        th = th3;
                                        out = out2;
                                        in = in2;
                                        IoUtils.closeQuietly(in);
                                        IoUtils.closeQuietly(out);
                                        tempFile.delete();
                                        throw th;
                                    }
                                } catch (Throwable th4) {
                                    th = th4;
                                    IoUtils.closeQuietly(in);
                                    IoUtils.closeQuietly(out);
                                    tempFile.delete();
                                    throw th;
                                }
                            }
                            throw new IOException("Could'nt rename to " + tempFile.getAbsolutePath());
                        }
                        throw new IOException("ExifInterface only supports saving attributes on JPEG formats.");
                    }

                    public boolean hasThumbnail() {
                        return this.mHasThumbnail;
                    }

                    public byte[] getThumbnail() {
                        Throwable th;
                        if (!this.mHasThumbnail) {
                            return null;
                        }
                        if (this.mThumbnailBytes != null) {
                            return this.mThumbnailBytes;
                        }
                        FileInputStream in = null;
                        try {
                            FileInputStream in2 = new FileInputStream(this.mFilename);
                            try {
                                if (in2.skip((long) this.mThumbnailOffset) != ((long) this.mThumbnailOffset)) {
                                    throw new IOException("Corrupted image");
                                }
                                byte[] buffer = new byte[this.mThumbnailLength];
                                if (in2.read(buffer) != this.mThumbnailLength) {
                                    throw new IOException("Corrupted image");
                                }
                                IoUtils.closeQuietly(in2);
                                return buffer;
                            } catch (IOException e) {
                                in = in2;
                                IoUtils.closeQuietly(in);
                                return null;
                            } catch (Throwable th2) {
                                th = th2;
                                in = in2;
                                IoUtils.closeQuietly(in);
                                throw th;
                            }
                        } catch (IOException e2) {
                            IoUtils.closeQuietly(in);
                            return null;
                        } catch (Throwable th3) {
                            th = th3;
                            IoUtils.closeQuietly(in);
                            throw th;
                        }
                    }

                    public long[] getThumbnailRange() {
                        if (!this.mHasThumbnail) {
                            return null;
                        }
                        return new long[]{(long) this.mThumbnailOffset, (long) this.mThumbnailLength};
                    }

                    public boolean getLatLong(float[] output) {
                        String latValue = getAttribute(TAG_GPS_LATITUDE);
                        String latRef = getAttribute(TAG_GPS_LATITUDE_REF);
                        String lngValue = getAttribute(TAG_GPS_LONGITUDE);
                        String lngRef = getAttribute(TAG_GPS_LONGITUDE_REF);
                        if (!(latValue == null || latRef == null || lngValue == null || lngRef == null)) {
                            try {
                                output[0] = convertRationalLatLonToFloat(latValue, latRef);
                                output[1] = convertRationalLatLonToFloat(lngValue, lngRef);
                                return true;
                            } catch (IllegalArgumentException e) {
                            }
                        }
                        return false;
                    }

                    public double getAltitude(double defaultValue) {
                        int i = -1;
                        double altitude = getAttributeDouble(TAG_GPS_ALTITUDE, -1.0d);
                        int ref = getAttributeInt(TAG_GPS_ALTITUDE_REF, -1);
                        if (altitude < 0.0d || ref < 0) {
                            return defaultValue;
                        }
                        if (ref != 1) {
                            i = 1;
                        }
                        return altitude * ((double) i);
                    }

                    public long getDateTime() {
                        String dateTimeString = getAttribute(TAG_DATETIME);
                        if (dateTimeString == null || !sNonZeroTimePattern.matcher(dateTimeString).matches()) {
                            return -1;
                        }
                        try {
                            Date datetime = sFormatter.parse(dateTimeString, new ParsePosition(0));
                            if (datetime == null) {
                                return -1;
                            }
                            long msecs = datetime.getTime();
                            String subSecs = getAttribute(TAG_SUBSEC_TIME);
                            if (subSecs == null) {
                                return msecs;
                            }
                            try {
                                long sub = Long.valueOf(subSecs).longValue();
                                while (sub > 1000) {
                                    sub /= 10;
                                }
                                return msecs + sub;
                            } catch (NumberFormatException e) {
                                return msecs;
                            }
                        } catch (IllegalArgumentException e2) {
                            return -1;
                        }
                    }

                    public long getGpsDateTime() {
                        long j = -1;
                        String date = getAttribute(TAG_GPS_DATESTAMP);
                        String time = getAttribute(TAG_GPS_TIMESTAMP);
                        if (!(date == null || time == null || (!sNonZeroTimePattern.matcher(date).matches() && !sNonZeroTimePattern.matcher(time).matches()))) {
                            try {
                                Date datetime = sFormatter.parse(date + ' ' + time, new ParsePosition(0));
                                if (datetime != null) {
                                    j = datetime.getTime();
                                }
                            } catch (IllegalArgumentException e) {
                            }
                        }
                        return j;
                    }

                    private static float convertRationalLatLonToFloat(String rationalString, String ref) {
                        try {
                            String[] parts = rationalString.split(",");
                            String[] pair = parts[0].split("/");
                            double degrees = Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim());
                            pair = parts[1].split("/");
                            double minutes = Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim());
                            pair = parts[2].split("/");
                            double result = ((minutes / 60.0d) + degrees) + ((Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim())) / 3600.0d);
                            if (!ref.equals("S")) {
                                if (!ref.equals("W")) {
                                    return (float) result;
                                }
                            }
                            return (float) (-result);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException();
                        } catch (ArrayIndexOutOfBoundsException e2) {
                            throw new IllegalArgumentException();
                        }
                    }

                    private void getJpegAttributes(InputStream inputStream) throws IOException {
                        DataInputStream dataInputStream = new DataInputStream(inputStream);
                        byte marker = dataInputStream.readByte();
                        if (marker != (byte) -1) {
                            throw new IOException("Invalid marker: " + Integer.toHexString(marker & 255));
                        }
                        int bytesRead = 0 + 1;
                        if (dataInputStream.readByte() != MARKER_SOI) {
                            throw new IOException("Invalid marker: " + Integer.toHexString(marker & 255));
                        }
                        bytesRead++;
                        while (true) {
                            marker = dataInputStream.readByte();
                            if (marker != (byte) -1) {
                                throw new IOException("Invalid marker:" + Integer.toHexString(marker & 255));
                            }
                            bytesRead++;
                            marker = dataInputStream.readByte();
                            bytesRead++;
                            if (marker != MARKER_EOI && marker != MARKER_SOS) {
                                int length = dataInputStream.readUnsignedShort() - 2;
                                bytesRead += 2;
                                if (length < 0) {
                                    throw new IOException("Invalid length");
                                }
                                byte[] bytes;
                                switch (marker) {
                                    case MediaPlayer.MEDIA_ErrDrmServerDeviceLimitReached /*-64*/:
                                    case MediaPlayer.MEDIA_ErrDrmServerProtocolVersionMismatch /*-63*/:
                                    case MediaPlayer.MEDIA_ErrDrmServerUnknownAccountId /*-62*/:
                                    case MediaPlayer.MEDIA_ErrDrmServerNotAMember /*-61*/:
                                    case MediaPlayer.MEDIA_ErrDrmDevCertRevoked /*-59*/:
                                    case MediaPlayer.MEDIA_ErrDrmServerInternalError /*-58*/:
                                    case (byte) -57:
                                    case (byte) -55:
                                    case (byte) -54:
                                    case (byte) -53:
                                    case (byte) -51:
                                    case (byte) -50:
                                    case MediaPlayer.MEDIA_ErrDrmRightsAcquisitionFailed /*-49*/:
                                        if (dataInputStream.skipBytes(1) == 1) {
                                            this.mAttributes[0].put(TAG_IMAGE_LENGTH, ExifAttribute.createULong((long) dataInputStream.readUnsignedShort(), this.mExifByteOrder));
                                            this.mAttributes[0].put(TAG_IMAGE_WIDTH, ExifAttribute.createULong((long) dataInputStream.readUnsignedShort(), this.mExifByteOrder));
                                            length -= 5;
                                            break;
                                        }
                                        throw new IOException("Invalid SOFx");
                                    case PacketKeepalive.ERROR_HARDWARE_ERROR /*-31*/:
                                        if (length >= 6) {
                                            byte[] identifier = new byte[6];
                                            if (inputStream.read(identifier) == 6) {
                                                bytesRead += 6;
                                                length -= 6;
                                                if (Arrays.equals(identifier, IDENTIFIER_EXIF_APP1)) {
                                                    if (length > 0) {
                                                        bytes = new byte[length];
                                                        if (dataInputStream.read(bytes) == length) {
                                                            readExifSegment(bytes, bytesRead);
                                                            bytesRead += length;
                                                            length = 0;
                                                            break;
                                                        }
                                                        throw new IOException("Invalid exif");
                                                    }
                                                    throw new IOException("Invalid exif");
                                                }
                                            }
                                            throw new IOException("Invalid exif");
                                        }
                                        break;
                                    case (byte) -2:
                                        bytes = new byte[length];
                                        if (dataInputStream.read(bytes) == length) {
                                            length = 0;
                                            if (getAttribute(TAG_USER_COMMENT) == null) {
                                                this.mAttributes[1].put(TAG_USER_COMMENT, ExifAttribute.createString(new String(bytes, ASCII)));
                                                break;
                                            }
                                        }
                                        throw new IOException("Invalid exif");
                                        break;
                                }
                                if (length < 0) {
                                    throw new IOException("Invalid length");
                                } else if (dataInputStream.skipBytes(length) != length) {
                                    throw new IOException("Invalid JPEG segment");
                                } else {
                                    bytesRead += length;
                                }
                            } else {
                                return;
                            }
                        }
                    }

                    private void saveJpegAttributes(InputStream inputStream, OutputStream outputStream) throws IOException {
                        DataInputStream dataInputStream = new DataInputStream(inputStream);
                        ByteOrderAwarenessDataOutputStream dataOutputStream = new ByteOrderAwarenessDataOutputStream(outputStream, ByteOrder.BIG_ENDIAN);
                        if (dataInputStream.readByte() != (byte) -1) {
                            throw new IOException("Invalid marker");
                        }
                        dataOutputStream.writeByte(-1);
                        if (dataInputStream.readByte() != MARKER_SOI) {
                            throw new IOException("Invalid marker");
                        }
                        dataOutputStream.writeByte(-40);
                        dataOutputStream.writeByte(-1);
                        dataOutputStream.writeByte(-31);
                        writeExifSegment(dataOutputStream, 6);
                        byte[] bytes = new byte[4096];
                        while (dataInputStream.readByte() == (byte) -1) {
                            byte marker = dataInputStream.readByte();
                            int length;
                            int read;
                            switch (marker) {
                                case (byte) -39:
                                case (byte) -38:
                                    dataOutputStream.writeByte(-1);
                                    dataOutputStream.writeByte(marker);
                                    Streams.copy(dataInputStream, dataOutputStream);
                                    return;
                                case PacketKeepalive.ERROR_HARDWARE_ERROR /*-31*/:
                                    length = dataInputStream.readUnsignedShort() - 2;
                                    if (length >= 0) {
                                        byte[] identifier = new byte[6];
                                        if (length >= 6) {
                                            if (dataInputStream.read(identifier) == 6) {
                                                if (Arrays.equals(identifier, IDENTIFIER_EXIF_APP1)) {
                                                    if (dataInputStream.skip((long) (length - 6)) == ((long) (length - 6))) {
                                                        break;
                                                    }
                                                    throw new IOException("Invalid length");
                                                }
                                            }
                                            throw new IOException("Invalid exif");
                                        }
                                        dataOutputStream.writeByte(-1);
                                        dataOutputStream.writeByte(marker);
                                        dataOutputStream.writeUnsignedShort(length + 2);
                                        if (length >= 6) {
                                            length -= 6;
                                            dataOutputStream.write(identifier);
                                        }
                                        while (length > 0) {
                                            read = dataInputStream.read(bytes, 0, Math.min(length, bytes.length));
                                            if (read < 0) {
                                                break;
                                            }
                                            dataOutputStream.write(bytes, 0, read);
                                            length -= read;
                                        }
                                        break;
                                    }
                                    throw new IOException("Invalid length");
                                default:
                                    dataOutputStream.writeByte(-1);
                                    dataOutputStream.writeByte(marker);
                                    length = dataInputStream.readUnsignedShort();
                                    dataOutputStream.writeUnsignedShort(length);
                                    length -= 2;
                                    if (length >= 0) {
                                        while (length > 0) {
                                            read = dataInputStream.read(bytes, 0, Math.min(length, bytes.length));
                                            if (read < 0) {
                                                break;
                                            }
                                            dataOutputStream.write(bytes, 0, read);
                                            length -= read;
                                        }
                                        break;
                                    }
                                    throw new IOException("Invalid length");
                            }
                        }
                        throw new IOException("Invalid marker");
                    }

                    private void readExifSegment(byte[] exifBytes, int exifOffsetFromBeginning) throws IOException {
                        ByteOrderAwarenessDataInputStream dataInputStream = new ByteOrderAwarenessDataInputStream(exifBytes);
                        short byteOrder = dataInputStream.readShort();
                        switch (byteOrder) {
                            case (short) 18761:
                                this.mExifByteOrder = ByteOrder.LITTLE_ENDIAN;
                                break;
                            case (short) 19789:
                                this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
                                break;
                            default:
                                throw new IOException("Invalid byte order: " + Integer.toHexString(byteOrder));
                        }
                        dataInputStream.setByteOrder(this.mExifByteOrder);
                        int startCode = dataInputStream.readUnsignedShort();
                        if (startCode != 42) {
                            throw new IOException("Invalid exif start: " + Integer.toHexString(startCode));
                        }
                        long firstIfdOffset = dataInputStream.readUnsignedInt();
                        if (firstIfdOffset < 8 || firstIfdOffset >= ((long) exifBytes.length)) {
                            throw new IOException("Invalid first Ifd offset: " + firstIfdOffset);
                        }
                        firstIfdOffset -= 8;
                        if (firstIfdOffset <= 0 || dataInputStream.skip(firstIfdOffset) == firstIfdOffset) {
                            readImageFileDirectory(dataInputStream, 0);
                            String jpegInterchangeFormatString = getAttribute(JPEG_INTERCHANGE_FORMAT_TAG.name);
                            String jpegInterchangeFormatLengthString = getAttribute(JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name);
                            if (jpegInterchangeFormatString != null && jpegInterchangeFormatLengthString != null) {
                                try {
                                    int jpegInterchangeFormat = Integer.parseInt(jpegInterchangeFormatString);
                                    int jpegInterchangeFormatLength = Math.min(jpegInterchangeFormat + Integer.parseInt(jpegInterchangeFormatLengthString), exifBytes.length) - jpegInterchangeFormat;
                                    if (jpegInterchangeFormat > 0 && jpegInterchangeFormatLength > 0) {
                                        this.mHasThumbnail = true;
                                        this.mThumbnailOffset = exifOffsetFromBeginning + jpegInterchangeFormat;
                                        this.mThumbnailLength = jpegInterchangeFormatLength;
                                        return;
                                    }
                                    return;
                                } catch (NumberFormatException e) {
                                    return;
                                }
                            }
                            return;
                        }
                        throw new IOException("Couldn't jump to first Ifd: " + firstIfdOffset);
                    }

                    private void addDefaultValuesForCompatibility() {
                        String valueOfDateTimeOriginal = getAttribute(TAG_DATETIME_ORIGINAL);
                        if (valueOfDateTimeOriginal != null) {
                            this.mAttributes[0].put(TAG_DATETIME, ExifAttribute.createString(valueOfDateTimeOriginal));
                        }
                        if (getAttribute(TAG_IMAGE_WIDTH) == null) {
                            this.mAttributes[0].put(TAG_IMAGE_WIDTH, ExifAttribute.createULong(0, this.mExifByteOrder));
                        }
                        if (getAttribute(TAG_IMAGE_LENGTH) == null) {
                            this.mAttributes[0].put(TAG_IMAGE_LENGTH, ExifAttribute.createULong(0, this.mExifByteOrder));
                        }
                        if (getAttribute(TAG_ORIENTATION) == null) {
                            this.mAttributes[0].put(TAG_ORIENTATION, ExifAttribute.createULong(0, this.mExifByteOrder));
                        }
                        if (getAttribute(TAG_LIGHT_SOURCE) == null) {
                            this.mAttributes[1].put(TAG_LIGHT_SOURCE, ExifAttribute.createULong(0, this.mExifByteOrder));
                        }
                    }

                    private void readImageFileDirectory(ByteOrderAwarenessDataInputStream dataInputStream, int hint) throws IOException {
                        if (dataInputStream.peek() + 2 <= dataInputStream.mLength) {
                            short numberOfDirectoryEntry = dataInputStream.readShort();
                            if (dataInputStream.peek() + ((long) (numberOfDirectoryEntry * 12)) <= dataInputStream.mLength) {
                                for (short i = (short) 0; i < numberOfDirectoryEntry; i = (short) (i + 1)) {
                                    int tagNumber = dataInputStream.readUnsignedShort();
                                    int dataFormat = dataInputStream.readUnsignedShort();
                                    int numberOfComponents = dataInputStream.readInt();
                                    long nextEntryOffset = dataInputStream.peek() + 4;
                                    ExifTag tag = (ExifTag) sExifTagMapsForReading[hint].get(Integer.valueOf(tagNumber));
                                    if (tag == null || dataFormat <= 0 || dataFormat >= IFD_FORMAT_BYTES_PER_FORMAT.length) {
                                        if (tag == null) {
                                            Log.w(TAG, "Skip the tag entry since tag number is not defined: " + tagNumber);
                                        } else {
                                            Log.w(TAG, "Skip the tag entry since data format is invalid: " + dataFormat);
                                        }
                                        dataInputStream.seek(nextEntryOffset);
                                    } else {
                                        long offset;
                                        int byteCount = numberOfComponents * IFD_FORMAT_BYTES_PER_FORMAT[dataFormat];
                                        if (byteCount > 4) {
                                            offset = dataInputStream.readUnsignedInt();
                                            if (((long) byteCount) + offset <= dataInputStream.mLength) {
                                                dataInputStream.seek(offset);
                                            } else {
                                                Log.w(TAG, "Skip the tag entry since data offset is invalid: " + offset);
                                                dataInputStream.seek(nextEntryOffset);
                                            }
                                        }
                                        int innerIfdHint = getIfdHintFromTagNumber(tagNumber);
                                        if (innerIfdHint >= 0) {
                                            offset = -1;
                                            switch (dataFormat) {
                                                case 3:
                                                    offset = (long) dataInputStream.readUnsignedShort();
                                                    break;
                                                case 4:
                                                    offset = dataInputStream.readUnsignedInt();
                                                    break;
                                                case 8:
                                                    offset = (long) dataInputStream.readShort();
                                                    break;
                                                case 9:
                                                    offset = (long) dataInputStream.readInt();
                                                    break;
                                            }
                                            if (offset <= 0 || offset >= dataInputStream.mLength) {
                                                Log.w(TAG, "Skip jump into the IFD since its offset is invalid: " + offset);
                                            } else {
                                                dataInputStream.seek(offset);
                                                readImageFileDirectory(dataInputStream, innerIfdHint);
                                            }
                                            dataInputStream.seek(nextEntryOffset);
                                        } else {
                                            byte[] bytes = new byte[(IFD_FORMAT_BYTES_PER_FORMAT[dataFormat] * numberOfComponents)];
                                            dataInputStream.readFully(bytes);
                                            this.mAttributes[hint].put(tag.name, new ExifAttribute(dataFormat, numberOfComponents, bytes));
                                            if (dataInputStream.peek() != nextEntryOffset) {
                                                dataInputStream.seek(nextEntryOffset);
                                            }
                                        }
                                    }
                                }
                                if (dataInputStream.peek() + 4 <= dataInputStream.mLength) {
                                    long nextIfdOffset = dataInputStream.readUnsignedInt();
                                    if (nextIfdOffset > 8 && nextIfdOffset < dataInputStream.mLength) {
                                        dataInputStream.seek(nextIfdOffset);
                                        readImageFileDirectory(dataInputStream, 4);
                                    }
                                }
                            }
                        }
                    }

                    private static int getIfdHintFromTagNumber(int tagNumber) {
                        for (int i = 0; i < IFD_POINTER_TAG_HINTS.length; i++) {
                            if (IFD_POINTER_TAGS[i].number == tagNumber) {
                                return IFD_POINTER_TAG_HINTS[i];
                            }
                        }
                        return -1;
                    }

                    private int writeExifSegment(ByteOrderAwarenessDataOutputStream dataOutputStream, int exifOffsetFromBeginning) throws IOException {
                        int hint;
                        int i;
                        int[] ifdOffsets = new int[EXIF_TAGS.length];
                        int[] ifdDataSizes = new int[EXIF_TAGS.length];
                        for (ExifTag tag : IFD_POINTER_TAGS) {
                            removeAttribute(tag.name);
                        }
                        removeAttribute(JPEG_INTERCHANGE_FORMAT_TAG.name);
                        removeAttribute(JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name);
                        for (hint = 0; hint < EXIF_TAGS.length; hint++) {
                            for (Entry entry : this.mAttributes[hint].entrySet().toArray()) {
                                if (entry.getValue() == null) {
                                    this.mAttributes[hint].remove(entry.getKey());
                                }
                            }
                        }
                        if (!this.mAttributes[3].isEmpty()) {
                            this.mAttributes[1].put(IFD_POINTER_TAGS[2].name, ExifAttribute.createULong(0, this.mExifByteOrder));
                        }
                        if (!this.mAttributes[1].isEmpty()) {
                            this.mAttributes[0].put(IFD_POINTER_TAGS[0].name, ExifAttribute.createULong(0, this.mExifByteOrder));
                        }
                        if (!this.mAttributes[2].isEmpty()) {
                            this.mAttributes[0].put(IFD_POINTER_TAGS[1].name, ExifAttribute.createULong(0, this.mExifByteOrder));
                        }
                        if (this.mHasThumbnail) {
                            this.mAttributes[0].put(JPEG_INTERCHANGE_FORMAT_TAG.name, ExifAttribute.createULong(0, this.mExifByteOrder));
                            this.mAttributes[0].put(JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name, ExifAttribute.createULong((long) this.mThumbnailLength, this.mExifByteOrder));
                        }
                        for (i = 0; i < EXIF_TAGS.length; i++) {
                            int sum = 0;
                            for (Entry entry2 : this.mAttributes[i].entrySet()) {
                                int size = ((ExifAttribute) entry2.getValue()).size();
                                if (size > 4) {
                                    sum += size;
                                }
                            }
                            ifdDataSizes[i] = ifdDataSizes[i] + sum;
                        }
                        int position = 8;
                        for (hint = 0; hint < EXIF_TAGS.length; hint++) {
                            if (!this.mAttributes[hint].isEmpty()) {
                                ifdOffsets[hint] = position;
                                position += (((this.mAttributes[hint].size() * 12) + 2) + 4) + ifdDataSizes[hint];
                            }
                        }
                        if (this.mHasThumbnail) {
                            int thumbnailOffset = position;
                            this.mAttributes[0].put(JPEG_INTERCHANGE_FORMAT_TAG.name, ExifAttribute.createULong((long) thumbnailOffset, this.mExifByteOrder));
                            this.mThumbnailOffset = exifOffsetFromBeginning + thumbnailOffset;
                            position += this.mThumbnailLength;
                        }
                        int totalSize = position + 8;
                        if (!this.mAttributes[1].isEmpty()) {
                            this.mAttributes[0].put(IFD_POINTER_TAGS[0].name, ExifAttribute.createULong((long) ifdOffsets[1], this.mExifByteOrder));
                        }
                        if (!this.mAttributes[2].isEmpty()) {
                            this.mAttributes[0].put(IFD_POINTER_TAGS[1].name, ExifAttribute.createULong((long) ifdOffsets[2], this.mExifByteOrder));
                        }
                        if (!this.mAttributes[3].isEmpty()) {
                            this.mAttributes[1].put(IFD_POINTER_TAGS[2].name, ExifAttribute.createULong((long) ifdOffsets[3], this.mExifByteOrder));
                        }
                        dataOutputStream.writeUnsignedShort(totalSize);
                        dataOutputStream.write(IDENTIFIER_EXIF_APP1);
                        dataOutputStream.writeShort(this.mExifByteOrder == ByteOrder.BIG_ENDIAN ? BYTE_ALIGN_MM : BYTE_ALIGN_II);
                        dataOutputStream.setByteOrder(this.mExifByteOrder);
                        dataOutputStream.writeUnsignedShort(42);
                        dataOutputStream.writeUnsignedInt(8);
                        for (hint = 0; hint < EXIF_TAGS.length; hint++) {
                            if (!this.mAttributes[hint].isEmpty()) {
                                ExifAttribute attribute;
                                dataOutputStream.writeUnsignedShort(this.mAttributes[hint].size());
                                int dataOffset = ((ifdOffsets[hint] + 2) + (this.mAttributes[hint].size() * 12)) + 4;
                                for (Entry entry22 : this.mAttributes[hint].entrySet()) {
                                    int tagNumber = ((ExifTag) sExifTagMapsForWriting[hint].get(entry22.getKey())).number;
                                    attribute = (ExifAttribute) entry22.getValue();
                                    size = attribute.size();
                                    dataOutputStream.writeUnsignedShort(tagNumber);
                                    dataOutputStream.writeUnsignedShort(attribute.format);
                                    dataOutputStream.writeInt(attribute.numberOfComponents);
                                    if (size > 4) {
                                        dataOutputStream.writeUnsignedInt((long) dataOffset);
                                        dataOffset += size;
                                    } else {
                                        dataOutputStream.write(attribute.bytes);
                                        if (size < 4) {
                                            for (i = size; i < 4; i++) {
                                                dataOutputStream.writeByte(0);
                                            }
                                        }
                                    }
                                }
                                if (hint != 0 || this.mAttributes[4].isEmpty()) {
                                    dataOutputStream.writeUnsignedInt(0);
                                } else {
                                    dataOutputStream.writeUnsignedInt((long) ifdOffsets[4]);
                                }
                                for (Entry entry222 : this.mAttributes[hint].entrySet()) {
                                    attribute = (ExifAttribute) entry222.getValue();
                                    if (attribute.bytes.length > 4) {
                                        dataOutputStream.write(attribute.bytes, 0, attribute.bytes.length);
                                    }
                                }
                            }
                        }
                        if (this.mHasThumbnail) {
                            dataOutputStream.write(getThumbnail());
                        }
                        dataOutputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
                        return totalSize;
                    }

                    private static Pair<Integer, Integer> guessDataFormat(String entryValue) {
                        if (entryValue.contains(",")) {
                            String[] entryValues = entryValue.split(",");
                            Pair<Integer, Integer> guessDataFormat = guessDataFormat(entryValues[0]);
                            if (((Integer) guessDataFormat.first).intValue() == 2) {
                                return guessDataFormat;
                            }
                            for (int i = 1; i < entryValues.length; i++) {
                                Pair<Integer, Integer> guessDataFormat2 = guessDataFormat(entryValues[i]);
                                int first = -1;
                                int second = -1;
                                if (guessDataFormat2.first == guessDataFormat.first || guessDataFormat2.second == guessDataFormat.first) {
                                    first = ((Integer) guessDataFormat.first).intValue();
                                }
                                if (((Integer) guessDataFormat.second).intValue() != -1 && (guessDataFormat2.first == guessDataFormat.second || guessDataFormat2.second == guessDataFormat.second)) {
                                    second = ((Integer) guessDataFormat.second).intValue();
                                }
                                if (first == -1 && second == -1) {
                                    return new Pair(Integer.valueOf(2), Integer.valueOf(-1));
                                }
                                if (first == -1) {
                                    guessDataFormat = new Pair(Integer.valueOf(second), Integer.valueOf(-1));
                                } else if (second == -1) {
                                    guessDataFormat = new Pair(Integer.valueOf(first), Integer.valueOf(-1));
                                }
                            }
                            return guessDataFormat;
                        }
                        if (entryValue.contains("/")) {
                            String[] rationalNumber = entryValue.split("/");
                            if (rationalNumber.length == 2) {
                                try {
                                    long numerator = Long.parseLong(rationalNumber[0]);
                                    long denominator = Long.parseLong(rationalNumber[1]);
                                    if (numerator < 0 || denominator < 0) {
                                        return new Pair(Integer.valueOf(10), Integer.valueOf(-1));
                                    }
                                    if (numerator > 2147483647L || denominator > 2147483647L) {
                                        return new Pair(Integer.valueOf(5), Integer.valueOf(-1));
                                    }
                                    return new Pair(Integer.valueOf(10), Integer.valueOf(5));
                                } catch (NumberFormatException e) {
                                }
                            }
                            return new Pair(Integer.valueOf(2), Integer.valueOf(-1));
                        }
                        try {
                            Long longValue = Long.valueOf(Long.parseLong(entryValue));
                            if (longValue.longValue() >= 0 && longValue.longValue() <= 65535) {
                                return new Pair(Integer.valueOf(3), Integer.valueOf(4));
                            }
                            if (longValue.longValue() < 0) {
                                return new Pair(Integer.valueOf(9), Integer.valueOf(-1));
                            }
                            return new Pair(Integer.valueOf(4), Integer.valueOf(-1));
                        } catch (NumberFormatException e2) {
                            try {
                                Double.parseDouble(entryValue);
                                return new Pair(Integer.valueOf(12), Integer.valueOf(-1));
                            } catch (NumberFormatException e3) {
                                return new Pair(Integer.valueOf(2), Integer.valueOf(-1));
                            }
                        }
                    }
                }
