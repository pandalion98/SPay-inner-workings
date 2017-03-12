package com.sec.android.secvision.sef;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SEF {
    private static final boolean DEBUG = false;
    private static final String SEF_VERSION = "1.03";
    private static final String TAG = "SEF";

    public static class AudioJPEGData {
        public int audio_count = 0;
        public ArrayList endOffset = new ArrayList();
        public String filename;
        public ArrayList startOffset = new ArrayList();

        private void resetAudioJpegData() {
            this.startOffset.clear();
            this.endOffset.clear();
            this.audio_count = 0;
            this.filename = null;
        }

        public String getFileName() {
            return this.filename;
        }

        public int getAudioListSize() {
            return this.audio_count;
        }

        public int getStartOffset(int idx) {
            if (idx >= 0 && idx <= this.startOffset.size()) {
                return ((Integer) this.startOffset.get(idx)).intValue();
            }
            return 0;
        }

        public int getLength(int idx) {
            if (idx >= 0 && idx <= this.endOffset.size()) {
                return ((Integer) this.endOffset.get(idx)).intValue() - ((Integer) this.startOffset.get(idx)).intValue();
            }
            return 0;
        }

        public AudioJPEGData() {
            resetAudioJpegData();
        }
    }

    public static final class DataType {
        public static final int ANIMATED_GIF_INFO = 2400;
        public static final int AUTO_ENHANCE_FOOD_INFO = 2480;
        public static final int AUTO_ENHANCE_INFO = 2240;
        public static final int BACKUP_RESTORE_DATA = 2625;
        public static final int BEAUTY_FACE_INFO = 2368;
        public static final int BURST_SHOT_BEST_PHOTO_INFO = 2529;
        public static final int BURST_SHOT_INFO = 2528;
        public static final int CLIP_MOVIE_INFO = 2496;
        public static final int DUAL_CAMERA_INFO = 2352;
        public static final int DUBBING_SHOT_FACIAL_FEATURE_DATA = 2082;
        public static final int DUBBING_SHOT_FACIAL_FEATURE_NEUTRAL = 2081;
        public static final int DUBBING_SHOT_INFO = 2080;
        public static final int EASY_360_INFO = 2512;
        public static final int EXT_A = 1030;
        public static final int EXT_DLL = 1029;
        public static final int EXT_EXE = 1031;
        public static final int EXT_HTML = 1026;
        public static final int EXT_LIB = 1028;
        public static final int EXT_SO = 1027;
        public static final int EXT_SWF = 1025;
        public static final int EXT_XML = 1024;
        public static final int FACE_DATA = 2177;
        public static final int FACE_DATA_INFO = 2577;
        public static final int FACE_TAG_DATA = 2178;
        public static final int FAST_MOTION_DATA = 2208;
        public static final int FLIP_PHOTO_INFO = 2592;
        public static final int FOCUS_SHOT_INFO = 2112;
        public static final int FOCUS_SHOT_MAP = 2113;
        public static final int FOOD_SHOT_INFO = 2336;
        public static final int FRONT_CAM_SELFIE_INFO = 2320;
        public static final int GOLF_SHOT_INFO = 2064;
        public static final int HIGHLIGHT_VIDEO_DATA = 2224;
        public static final int IMAGE_UTC_DATA = 2561;
        public static final int IMG_BMP = 3;
        public static final int IMG_GIF = 4;
        public static final int IMG_JPEG = 1;
        public static final int IMG_NV21 = 9;
        public static final int IMG_NV22 = 10;
        public static final int IMG_PNG = 2;
        public static final int IMG_RAW_BGRA = 12;
        public static final int IMG_RAW_RGB = 13;
        public static final int IMG_RAW_RGB565 = 14;
        public static final int IMG_RAW_RGBA = 11;
        public static final int IMG_TIFF = 5;
        public static final int IMG_YUV420 = 8;
        public static final int IMG_YUV422 = 7;
        public static final int IMG_YUV444 = 6;
        public static final int INTERACTIVE_PANORAMA_DEBUG_DATA = 2257;
        public static final int INTERACTIVE_PANORAMA_INFO = 2256;
        public static final int INTERVAL_SHOT_INFO = 2432;
        public static final int INVALID_DATA = 32766;
        public static final int INVALID_TYPE = -1;
        public static final int JPEG_360_2D_INFO = 2640;
        public static final int MAGIC_SHOT_BEST_FACE_INFO = 2098;
        public static final int MAGIC_SHOT_BEST_PHOTO_INFO = 2097;
        public static final int MAGIC_SHOT_DRAMA_SHOT_INFO = 2100;
        public static final int MAGIC_SHOT_ERASER_INFO = 2099;
        public static final int MAGIC_SHOT_INFO = 2096;
        public static final int MAGIC_SHOT_PIC_MOTION_INFO = 2101;
        public static final int MOTION_PHOTO_DATA = 2608;
        public static final int MOV_AVI = 512;
        public static final int MOV_MOV = 515;
        public static final int MOV_MP4 = 513;
        public static final int MOV_QUICK_TIME = 514;
        public static final int MOV_QickTime = 514;
        public static final int MULTI_SHOT_REFOCUS_DATA = 2144;
        public static final int PANORAMA_MOTION_DEBUG_DATA = 2274;
        public static final int PANORAMA_MOTION_INFO = 2273;
        public static final int PANORAMA_SHOT_INFO = 2272;
        public static final int PRO_MODE_INFO = 2544;
        public static final int REAR_CAM_SELFIE_INFO = 2304;
        public static final int SAMSUNG_SPECIFIC_DATA_TYPE_END = 16384;
        public static final int SAMSUNG_SPECIFIC_DATA_TYPE_START = 2048;
        public static final int SEQUENCE_SHOT_DATA = 2160;
        public static final int SLOW_MOTION_DATA = 2192;
        public static final int SND_AAC = 260;
        public static final int SND_FLAC = 259;
        public static final int SND_MP3 = 257;
        public static final int SND_OGG = 258;
        public static final int SND_PCM_WAV = 256;
        public static final int SOUND_SHOT_INFO = 2048;
        public static final int SPORTS_SHOT_INFO = 2288;
        public static final int SURROUND_SHOT_INFO = 2384;
        public static final int TAG_SHOT_INFO = 2448;
        public static final int USER_DATA = 0;
        public static final int UserData = 0;
        public static final int VIDEO_VIEW_MODE = 2465;
        public static final int VIRTUAL_TOUR_INFO = 2128;
        public static final int WIDE_SELFIE_INFO = 2416;
    }

    public static final class KeyName {
        public static final String ANIMATED_GIF_INFO = "Animated_Gif_Info";
        public static final String AUTO_ENHANCE_FOOD_INFO = "Auto_Enhance_Food_Info";
        public static final String AUTO_ENHANCE_IMG_PROCESSED = "Auto_Enhance_Processed";
        public static final String AUTO_ENHANCE_IMG_UNPROCESSED = "Auto_Enhance_Unprocessed";
        public static final String AUTO_ENHANCE_INFO = "Auto_Enhance_Info";
        public static final String BACKUP_RESTORE_DATA = "BackupRestore_Data";
        public static final String BEAUTY_FACE_INFO = "Beauty_Face_Info";
        public static final String BURST_SHOT_BEST_PHOTO_INFO = "Burst_Shot_Best_Photo_Info";
        public static final String BURST_SHOT_INFO = "Burst_Shot_Info";
        public static final String CLIP_MOVIE_INFO = "Clip_Movie_Info";
        public static final String DUAL_CAMERA_INFO = "Dual_Camera_Info";
        public static final String EASY_360_INFO = "Easy_360_Info";
        public static final String FACE_DATA = "Face_Data_%03d";
        public static final String FACE_DATA_INFO = "Face_Data_Info";
        public static final String FACE_TAG_DATA = "Face_Tag_Data_%03d";
        public static final String FAST_MOTION_DATA = "FastMotion_Data";
        public static final String FLIP_PHOTO_INFO = "Flip_Photo_Info";
        public static final String FLIP_PHOTO_JPG_TEMPLATE = "FlipPhoto_%03d";
        public static final String FOCUS_SHOT_INFO = "FocusShot_Meta_Info";
        public static final String FOCUS_SHOT_JPG_TEMPLATE = "FocusShot_%d";
        public static final String FOCUS_SHOT_MAP = "FocusShot_Map";
        public static final String FOOD_SHOT_INFO = "Food_Shot_Info";
        public static final String FRONT_CAM_SELFIE_INFO = "Front_Cam_Selfie_Info";
        public static final String HIGHLIGHT_VIDEO_DATA = "HighlightVideo_Data";
        public static final String IMAGE_UTC_DATA = "Image_UTC_Data";
        public static final String INTERACTIVE_PANORAMA_DEBUG_DATA = "Interactive_Panorama_Debug_Data";
        public static final String INTERACTIVE_PANORAMA_INFO = "Interactive_Panorama_Info";
        public static final String INTERACTIVE_PANORAMA_MP4_TEMPLATE = "Interactive_Panorama_%03d";
        public static final String INTERVAL_SHOT_INFO = "Interval_Shot_Info";
        public static final String INVALID_DATA = "Invalid_Data";
        public static final String JPEG_360_2D_INFO = "Jpeg360_2D_Info";
        public static final String MAGIC_SHOT_BEST_FACE_INFO = "MagicShot_Best_Face_Info";
        public static final String MAGIC_SHOT_BEST_FACE_JPG = "MagicShot_Best_Face_JPG";
        public static final String MAGIC_SHOT_BEST_PHOTO_INFO = "MagicShot_Best_Photo_Info";
        public static final String MAGIC_SHOT_BEST_PHOTO_JPG = "MagicShot_Best_Photo_JPG";
        public static final String MAGIC_SHOT_DRAMA_SHOT_INFO = "MagicShot_Drama_Shot_Info";
        public static final String MAGIC_SHOT_DRAMA_SHOT_JPG = "MagicShot_Drama_Shot_JPG";
        public static final String MAGIC_SHOT_ERASER_INFO = "MagicShot_Eraser_Info";
        public static final String MAGIC_SHOT_ERASER_JPG = "MagicShot_Eraser_JPG";
        public static final String MAGIC_SHOT_INFO = "MagicShot_Info";
        public static final String MAGIC_SHOT_JPG_TEMPLATE = "MagicShot_%03d";
        public static final String MAGIC_SHOT_PIC_MOTION_INFO = "MagicShot_Pic_Motion_Info";
        public static final String MAGIC_SHOT_PIC_MOTION_JPG = "MagicShot_Pic_Motion_JPG";
        public static final String MOTION_PHOTO_DATA = "MotionPhoto_Data";
        public static final String PANORAMA_MOTION_DEBUG_DATA = "Motion_Panorama_Debug_Data";
        public static final String PANORAMA_MOTION_INFO = "Motion_Panorama_Info";
        public static final String PANORAMA_MOTION_MP4_TEMPLATE = "Motion_Panorama_MP4_%03d";
        public static final String PANORAMA_MOTION_SOUND_TEMPLATE = "Motion_Panorama_Sound_%03d";
        public static final String PANORAMA_SHOT_INFO = "Panorama_Shot_Info";
        public static final String PRO_MODE_INFO = "Pro_Mode_Info";
        public static final String REAR_CAM_SELFIE_INFO = "Rear_Cam_Selfie_Info";
        public static final String SEQUENCE_SHOT_DATA = "SequenceShot_Data";
        public static final String SLOW_MOTION_DATA = "SlowMotion_Data";
        public static final String SOUND_SHOT_INFO = "SoundShot_Meta_Info";
        public static final String SOUND_SHOT_WAVE = "SoundShot_000";
        public static final String SPORTS_SHOT_INFO = "Sports_Shot_Info";
        public static final String SURROUND_SHOT_INFO = "Surround_Shot_Info";
        public static final String TAG_SHOT_INFO = "Tag_Shot_Info";
        public static final String VIDEO_VIEW_MODE = "Video_View_Mode";
        public static final String VIRTUAL_TOUR_INFO = "VirtualTour_Info";
        public static final String VIRTUAL_TOUR_JPG_TEMPLATE = "VirtualTour_%03d";
        public static final String WIDE_SELFIE_INFO = "Wide_Selfie_Info";
    }

    public static final class Options {
        public static final int OVERWRITE_IF_EXISTS = 1;
        public static final int OVERWRITE_IF_EXISTS_MP4 = 4096;
        public static final int SKIP_IF_EXISTS = 0;
        public static final int SKIP_IF_EXISTS_MP4 = 256;
        public static final int SUBSTITUTE_IF_EXIST = 16;
    }

    public static class QdioJPEGData {
        public int audio_count = 0;
        public ArrayList endOffset = new ArrayList();
        public String filename;
        public ArrayList startOffset = new ArrayList();

        private void resetQdioJpegData() {
            this.startOffset.clear();
            this.endOffset.clear();
            this.audio_count = 0;
            this.filename = null;
        }

        public String getFileName() {
            return this.filename;
        }

        public int getAudioListSize() {
            return this.audio_count;
        }

        public int getStartOffset(int idx) {
            if (idx >= 0 && idx <= this.startOffset.size()) {
                return ((Integer) this.startOffset.get(idx)).intValue();
            }
            return 0;
        }

        public int getLength(int idx) {
            if (idx >= 0 && idx <= this.endOffset.size()) {
                return ((Integer) this.endOffset.get(idx)).intValue() - ((Integer) this.startOffset.get(idx)).intValue();
            }
            return 0;
        }

        public QdioJPEGData() {
            resetQdioJpegData();
        }
    }

    public static class SEFDataPosition64 {
        public long length;
        public long offset;
    }

    public static class SEFDataPosition {
        public int length;
        public int offset;
    }

    public static class SEFSubDataPosition64 {
        public long length;
        public long offset;
    }

    public static class SEFSubDataPosition {
        public int length;
        public int offset;
    }

    public static String getVersion() {
        return "1.03_" + SEFJNI.getNativeVersion();
    }

    public static boolean isSEFFile(File sefFile) throws IOException {
        String fileName = sefFile.getCanonicalPath();
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return false;
        } else if (SEFJNI.isSEFFile(fileName) == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isSEFFile(String fileName) {
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return false;
        } else if (SEFJNI.isSEFFile(fileName) == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean hasSEFData(File sefFile, int dataType) throws IOException {
        String fileName = sefFile.getCanonicalPath();
        if (fileName == null || fileName.length() <= 0 || dataType == -1) {
            Log.e(TAG, "Invalid file name: " + fileName + ", Data Type: " + dataType);
            return false;
        } else if (SEFJNI.isSEFFile(fileName) == 0) {
            return false;
        } else {
            int[] currentTypes = listSEFDataTypes(sefFile);
            if (currentTypes == null) {
                Log.e(TAG, "Invalid file : " + fileName);
                return false;
            }
            for (int i = currentTypes.length - 1; i > -1; i--) {
                if (dataType == currentTypes[i]) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean hasSEFData(File sefFile, String keyName) throws IOException {
        String fileName = sefFile.getCanonicalPath();
        if (fileName == null || fileName.length() <= 0 || keyName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName + ", keyName: " + keyName);
            return false;
        } else if (SEFJNI.isSEFFile(fileName) == 0) {
            return false;
        } else {
            String[] currentTypes = listKeyNames(sefFile);
            if (currentTypes == null) {
                Log.e(TAG, "Invalid file : " + fileName);
                return false;
            } else if (currentTypes.length > 0 || currentTypes == null) {
                for (int i = currentTypes.length - 1; i > -1; i--) {
                    if (keyName.equals(currentTypes[i])) {
                        return true;
                    }
                }
                return false;
            } else {
                Log.e(TAG, "Invalid file : " + fileName);
                return false;
            }
        }
    }

    public static boolean hasDataType(String fileName, int dataType) {
        if (fileName == null || fileName.length() <= 0 || dataType == -1) {
            Log.e(TAG, "Invalid file name: " + fileName + ", Data Type: " + dataType);
            return false;
        } else if (SEFJNI.isSEFFile(fileName) == 0) {
            return false;
        } else {
            int[] currentTypes = listSEFDataTypes(fileName);
            if (currentTypes == null) {
                Log.e(TAG, "Invalid file : " + fileName);
                return false;
            }
            for (int i = currentTypes.length - 1; i > -1; i--) {
                if (dataType == currentTypes[i]) {
                    return true;
                }
            }
            return false;
        }
    }

    public static int addSEFData(File sefFile, String keyName, byte[] data, int dataType, int option) throws IOException {
        return addSEFData(sefFile, keyName, data, null, dataType, option);
    }

    public static int addSEFData(File sefFile, String keyName, byte[] data, byte[] subdataInfo, int dataType, int option) throws IOException {
        String fileName = sefFile.getCanonicalPath();
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return 0;
        } else if (keyName == null || keyName.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + keyName);
            return 0;
        } else if (data == null || data.length <= 0) {
            Log.e(TAG, "Invalid data");
            return 0;
        } else if (option == 16) {
            return SEFJNI.addFastSEFData(fileName, keyName, keyName.length(), subdataInfo, subdataInfo == null ? 0 : subdataInfo.length, data, data.length, dataType, option);
        } else if (option == 256) {
            return SEFJNI.addSEFDataToMP4(fileName, keyName, keyName.length(), subdataInfo, subdataInfo == null ? 0 : subdataInfo.length, data, data.length, dataType, 0);
        } else if (option == Options.OVERWRITE_IF_EXISTS_MP4) {
            return SEFJNI.addSEFDataToMP4(fileName, keyName, keyName.length(), subdataInfo, subdataInfo == null ? 0 : subdataInfo.length, data, data.length, dataType, 1);
        } else {
            return SEFJNI.addSEFData(fileName, keyName, keyName.length(), subdataInfo, subdataInfo == null ? 0 : subdataInfo.length, data, data.length, dataType, option);
        }
    }

    public static int addSEFData(File sefFile, String keyName, File dataFile, int dataType, int option) throws IOException {
        return addSEFData(sefFile, keyName, dataFile, null, dataType, option);
    }

    public static int addSEFData(File sefFile, String keyName, File dataFile, byte[] subdataInfo, int dataType, int option) throws IOException {
        String fileName = sefFile.getCanonicalPath();
        String dataFileName = dataFile.getCanonicalPath();
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return 0;
        } else if (keyName == null || keyName.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + keyName);
            return 0;
        } else if (dataFileName == null || dataFileName.length() <= 0) {
            Log.e(TAG, "Invalid SEF Data File name: " + dataFileName);
            return 0;
        } else if (option == 16) {
            return SEFJNI.addFastSEFDataFile(fileName, keyName, keyName.length(), subdataInfo, subdataInfo == null ? 0 : subdataInfo.length, dataFileName, dataType, option);
        } else if (option == 256) {
            return SEFJNI.addSEFDataFileToMP4(fileName, keyName, keyName.length(), subdataInfo, subdataInfo == null ? 0 : subdataInfo.length, dataFileName, dataType, 0);
        } else if (option == Options.OVERWRITE_IF_EXISTS_MP4) {
            return SEFJNI.addSEFDataFileToMP4(fileName, keyName, keyName.length(), subdataInfo, subdataInfo == null ? 0 : subdataInfo.length, dataFileName, dataType, 1);
        } else {
            return SEFJNI.addSEFDataFile(fileName, keyName, keyName.length(), subdataInfo, subdataInfo == null ? 0 : subdataInfo.length, dataFileName, dataType, option);
        }
    }

    public static int addSEFData(String fileName, String keyName, byte[] data, int dataType, int option) {
        return addSEFData(fileName, keyName, data, null, dataType, option);
    }

    public static int addSEFData(String fileName, String keyName, byte[] data, byte[] subdataInfo, int dataType, int option) {
        int i = 0;
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return 0;
        } else if (keyName == null || keyName.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + keyName);
            return 0;
        } else if (data == null || data.length <= 0) {
            Log.e(TAG, "Invalid data");
            return 0;
        } else if (option == 16) {
            r2 = keyName.length();
            if (subdataInfo != null) {
                i = subdataInfo.length;
            }
            return SEFJNI.addFastSEFData(fileName, keyName, r2, subdataInfo, i, data, data.length, dataType, option);
        } else {
            r2 = keyName.length();
            if (subdataInfo != null) {
                i = subdataInfo.length;
            }
            return SEFJNI.addSEFData(fileName, keyName, r2, subdataInfo, i, data, data.length, dataType, option);
        }
    }

    public static int addSEFDataFile(String fileName, String keyName, String dataFileName, int dataType, int option) {
        return addSEFDataFile(fileName, keyName, dataFileName, null, dataType, option);
    }

    public static int addSEFDataFile(String fileName, String keyName, String dataFileName, byte[] subdataInfo, int dataType, int option) {
        int i = 0;
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return 0;
        } else if (keyName == null || keyName.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + keyName);
            return 0;
        } else if (dataFileName == null || dataFileName.length() <= 0) {
            Log.e(TAG, "Invalid SEF Data File name: " + dataFileName);
            return 0;
        } else {
            int length = keyName.length();
            if (subdataInfo != null) {
                i = subdataInfo.length;
            }
            return SEFJNI.addSEFDataFile(fileName, keyName, length, subdataInfo, i, dataFileName, dataType, option);
        }
    }

    public static int addFastSEFData(String fileName, String keyName, byte[] data, int dataType, int option) {
        return addFastSEFData(fileName, keyName, data, null, dataType, option);
    }

    public static int addFastSEFData(String fileName, String keyName, byte[] data, byte[] subdataInfo, int dataType, int option) {
        int i = 0;
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return 0;
        } else if (keyName == null || keyName.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + keyName);
            return 0;
        } else if (data == null || data.length <= 0) {
            Log.e(TAG, "Invalid data");
            return 0;
        } else {
            int length = keyName.length();
            if (subdataInfo != null) {
                i = subdataInfo.length;
            }
            return SEFJNI.addFastSEFData(fileName, keyName, length, subdataInfo, i, data, data.length, dataType, option);
        }
    }

    public static int addFastSEFDataFile(String fileName, String keyName, String dataFileName, int dataType, int option) {
        return addFastSEFDataFile(fileName, keyName, dataFileName, null, dataType, option);
    }

    public static int addFastSEFDataFile(String fileName, String keyName, String dataFileName, byte[] subdataInfo, int dataType, int option) {
        int i = 0;
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return 0;
        } else if (keyName == null || keyName.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + keyName);
            return 0;
        } else if (dataFileName == null || dataFileName.length() <= 0) {
            Log.e(TAG, "Invalid SEF Data File name: " + dataFileName);
            return 0;
        } else {
            int length = keyName.length();
            if (subdataInfo != null) {
                i = subdataInfo.length;
            }
            return SEFJNI.addFastSEFDataFile(fileName, keyName, length, subdataInfo, i, dataFileName, dataType, option);
        }
    }

    public static int addSEFDataFiles(String fileName, String[] keyNames, String[] dataFileNames, int[] dataTypes, int option) {
        int dataCount = keyNames.length;
        if (dataCount != dataFileNames.length) {
            Log.e(TAG, "Data Count is different. ( keyNames data count= " + dataCount + ", dataFileNames data count= " + dataFileNames.length + " )");
        } else if (dataCount != dataTypes.length) {
            Log.e(TAG, "Data Count is different. ( keyNames data count= " + dataCount + ", dataTypes data count= " + dataTypes.length + " )");
        }
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return 0;
        }
        int[] keynamesLength = new int[keyNames.length];
        for (int i = 0; i < keyNames.length; i++) {
            keynamesLength[i] = keyNames[i].length();
        }
        return SEFJNI.addSEFDataFiles(fileName, keyNames, keynamesLength, dataFileNames, dataTypes, option, dataCount);
    }

    public static boolean deleteSEFData(File sefFile, String keyName) throws IOException {
        return deleteSEFData(sefFile, keyName, 1);
    }

    public static boolean deleteSEFData(File sefFile, String keyName, int option) throws IOException {
        String fileName = sefFile.getCanonicalPath();
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return false;
        } else if (keyName == null || keyName.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + keyName);
            return false;
        } else if (option == 16) {
            if (SEFJNI.fastDeleteSEFData(fileName, keyName, keyName.length()) != 1) {
                return false;
            }
            return true;
        } else if (SEFJNI.deleteSEFData(fileName, keyName, keyName.length()) != 1) {
            return false;
        } else {
            return true;
        }
    }

    public static int deleteSEFData(String fileName, String keyName) {
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return 0;
        } else if (keyName != null && keyName.length() > 0) {
            return SEFJNI.deleteSEFData(fileName, keyName, keyName.length());
        } else {
            Log.e(TAG, "Invalid key name: " + keyName);
            return 0;
        }
    }

    public static boolean deleteFastSEFData(String fileName, String keyName) {
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return false;
        } else if (keyName == null || keyName.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + keyName);
            return false;
        } else if (SEFJNI.fastDeleteSEFData(fileName, keyName, keyName.length()) != 1) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean deleteAllSEFData(File sefFile) throws IOException {
        return deleteAllSEFData(sefFile, 1);
    }

    public static boolean deleteAllSEFData(File sefFile, int option) throws IOException {
        String fileName = sefFile.getCanonicalPath();
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return false;
        } else if (option == 16) {
            if (SEFJNI.fastClearSEFData(fileName) != 1) {
                return false;
            }
            return true;
        } else if (SEFJNI.clearSEFData(fileName) != 1) {
            return false;
        } else {
            return true;
        }
    }

    public static int clearSEFData(String fileName) {
        if (fileName != null && fileName.length() > 0) {
            return SEFJNI.clearSEFData(fileName);
        }
        Log.e(TAG, "Invalid file name: " + fileName);
        return 0;
    }

    public static int clearFastSEFData(String fileName) {
        if (fileName != null && fileName.length() > 0) {
            return SEFJNI.fastClearSEFData(fileName);
        }
        Log.e(TAG, "Invalid file name: " + fileName);
        return 0;
    }

    public static boolean compact(File sefFile) throws IOException {
        String fileName = sefFile.getCanonicalPath();
        if (deleteSEFData(sefFile, KeyName.INVALID_DATA)) {
            return true;
        }
        return false;
    }

    public static String[] listKeyNames(File sefFile) throws IOException {
        String fileName = sefFile.getCanonicalPath();
        if (fileName != null && fileName.length() > 0) {
            return SEFJNI.listKeyNames(fileName);
        }
        Log.e(TAG, "Invalid file name: " + fileName);
        return null;
    }

    public static String[] listKeyNames(File sefFile, int dataType) throws IOException {
        String fileName = sefFile.getCanonicalPath();
        if (fileName != null && fileName.length() > 0 && dataType != -1) {
            return SEFJNI.listKeyNamesByDataType(fileName, dataType);
        }
        Log.e(TAG, "Invalid file name: " + fileName + ", Data Type: " + dataType);
        return null;
    }

    public static String[] listKeyNames(String fileName) {
        if (fileName != null && fileName.length() > 0) {
            return SEFJNI.listKeyNames(fileName);
        }
        Log.e(TAG, "Invalid file name: " + fileName);
        return null;
    }

    public static String[] listKeyNamesByDataType(String fileName, int dataType) {
        if (fileName != null && fileName.length() > 0 && dataType != -1) {
            return SEFJNI.listKeyNamesByDataType(fileName, dataType);
        }
        Log.e(TAG, "Invalid file name: " + fileName + ", Data Type: " + dataType);
        return null;
    }

    public static SEFDataPosition getSEFDataPosition(String fileName, String keyName) {
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return null;
        } else if (keyName == null || keyName.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + keyName);
            return null;
        } else {
            int[] posArray = SEFJNI.getSEFDataPosition(fileName, keyName);
            if (posArray == null) {
                Log.w(TAG, "No SEF data is found in " + fileName + ".");
                return null;
            }
            SEFDataPosition position = new SEFDataPosition();
            position.offset = posArray[0];
            position.length = posArray[1];
            return position;
        }
    }

    public static int[] getSEFDataPositionArray(String fileName, String keyName) {
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return null;
        } else if (keyName == null || keyName.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + keyName);
            return null;
        } else {
            int[] posArray = SEFJNI.getSEFDataPosition(fileName, keyName);
            if (posArray != null) {
                return posArray;
            }
            Log.w(TAG, "No SEF data is found in " + fileName + ".");
            return null;
        }
    }

    public static SEFDataPosition64 getSEFDataPosition64(String fileName, String keyName) {
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return null;
        } else if (keyName == null || keyName.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + keyName);
            return null;
        } else {
            long[] posArray = SEFJNI.getSEFDataPosition64(fileName, keyName);
            if (posArray == null) {
                Log.w(TAG, "No SEF data is found in " + fileName + ".");
                return null;
            }
            SEFDataPosition64 position = new SEFDataPosition64();
            position.offset = posArray[0];
            position.length = posArray[1];
            return position;
        }
    }

    public static SEFSubDataPosition getSEFSubDataPosition(String fileName, String keyName) {
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return null;
        } else if (keyName == null || keyName.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + keyName);
            return null;
        } else {
            int[] posArray = SEFJNI.getSEFSubDataPosition(fileName, keyName);
            if (posArray == null) {
                Log.w(TAG, "No SEF data is found in " + fileName + ".");
                return null;
            }
            SEFSubDataPosition position = new SEFSubDataPosition();
            position.offset = posArray[0];
            position.length = posArray[1];
            return position;
        }
    }

    public static SEFSubDataPosition64 getSEFSubDataPosition64(String fileName, String keyName) {
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return null;
        } else if (keyName == null || keyName.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + keyName);
            return null;
        } else {
            long[] posArray = SEFJNI.getSEFSubDataPosition64(fileName, keyName);
            if (posArray == null) {
                Log.w(TAG, "No SEF data is found in " + fileName + ".");
                return null;
            }
            SEFSubDataPosition64 position = new SEFSubDataPosition64();
            position.offset = posArray[0];
            position.length = posArray[1];
            return position;
        }
    }

    public static byte[] getSEFData(File sefFile, String keyName) throws IOException {
        Exception e;
        Throwable th;
        String fileName = sefFile.getCanonicalPath();
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return null;
        } else if (keyName == null || keyName.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + keyName);
            return null;
        } else {
            byte[] sefData = null;
            FileInputStream fis = null;
            try {
                FileInputStream fis2 = new FileInputStream(fileName);
                try {
                    SEFDataPosition64 dataPosition = getSEFDataPosition64(fileName, keyName);
                    if (dataPosition == null) {
                        fis2.close();
                        if (fis2 == null) {
                            return null;
                        }
                        fis2.close();
                        return null;
                    }
                    long startOffset = dataPosition.offset;
                    long endOffset = startOffset + dataPosition.length;
                    sefData = new byte[((int) dataPosition.length)];
                    if (startOffset < 0) {
                        if (fis2 == null) {
                            return null;
                        }
                        fis2.close();
                        return null;
                    } else if (fis2.skip(startOffset) == 0) {
                        if (fis2 == null) {
                            return null;
                        }
                        fis2.close();
                        return null;
                    } else if (fis2.read(sefData) == 0) {
                        if (fis2 == null) {
                            return null;
                        }
                        fis2.close();
                        return null;
                    } else if (fis2 != null) {
                        fis2.close();
                        fis = fis2;
                        return sefData;
                    } else {
                        return sefData;
                    }
                } catch (Exception e2) {
                    e = e2;
                    fis = fis2;
                    try {
                        e.printStackTrace();
                        if (fis != null) {
                            return sefData;
                        }
                        fis.close();
                        return sefData;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fis != null) {
                            fis.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fis = fis2;
                    if (fis != null) {
                        fis.close();
                    }
                    throw th;
                }
            } catch (Exception e3) {
                e = e3;
                e.printStackTrace();
                if (fis != null) {
                    return sefData;
                }
                fis.close();
                return sefData;
            }
        }
    }

    public static byte[] getSEFData(String fileName, String keyName) throws IOException {
        Exception e;
        Throwable th;
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return null;
        } else if (keyName == null || keyName.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + keyName);
            return null;
        } else {
            byte[] sefData = null;
            FileInputStream fis = null;
            try {
                FileInputStream fis2 = new FileInputStream(fileName);
                try {
                    SEFDataPosition64 dataPosition = getSEFDataPosition64(fileName, keyName);
                    if (dataPosition == null) {
                        fis2.close();
                        if (fis2 == null) {
                            return null;
                        }
                        fis2.close();
                        return null;
                    }
                    long startOffset = dataPosition.offset;
                    long endOffset = startOffset + dataPosition.length;
                    sefData = new byte[((int) dataPosition.length)];
                    if (startOffset < 0) {
                        if (fis2 == null) {
                            return null;
                        }
                        fis2.close();
                        return null;
                    } else if (fis2.skip(startOffset) == 0) {
                        if (fis2 == null) {
                            return null;
                        }
                        fis2.close();
                        return null;
                    } else if (fis2.read(sefData) == 0) {
                        if (fis2 == null) {
                            return null;
                        }
                        fis2.close();
                        return null;
                    } else if (fis2 != null) {
                        fis2.close();
                        fis = fis2;
                        return sefData;
                    } else {
                        return sefData;
                    }
                } catch (Exception e2) {
                    e = e2;
                    fis = fis2;
                    try {
                        e.printStackTrace();
                        if (fis != null) {
                            return sefData;
                        }
                        fis.close();
                        return sefData;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fis != null) {
                            fis.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fis = fis2;
                    if (fis != null) {
                        fis.close();
                    }
                    throw th;
                }
            } catch (Exception e3) {
                e = e3;
                e.printStackTrace();
                if (fis != null) {
                    return sefData;
                }
                fis.close();
                return sefData;
            }
        }
    }

    public static int getSEFDataCount(File sefFile) throws IOException {
        String fileName = sefFile.getCanonicalPath();
        if (fileName != null && fileName.length() > 0) {
            return SEFJNI.getSEFDataCount(fileName);
        }
        Log.e(TAG, "Invalid file name: " + fileName);
        return -1;
    }

    public static int getSEFDataCount(String fileName) {
        if (fileName != null && fileName.length() > 0) {
            return SEFJNI.getSEFDataCount(fileName);
        }
        Log.e(TAG, "Invalid file name: " + fileName);
        return -1;
    }

    public static int getSEFDataType(File sefFile, String keyName) throws IOException {
        String fileName = sefFile.getCanonicalPath();
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return -1;
        } else if (keyName != null && keyName.length() > 0) {
            return SEFJNI.getSEFDataType(fileName, keyName);
        } else {
            Log.e(TAG, "Invalid key name: " + keyName);
            return -1;
        }
    }

    public static int getSEFDataType(String fileName, String keyName) {
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return -1;
        } else if (keyName != null && keyName.length() > 0) {
            return SEFJNI.getSEFDataType(fileName, keyName);
        } else {
            Log.e(TAG, "Invalid key name: " + keyName);
            return -1;
        }
    }

    public static int[] listSEFDataTypes(File sefFile) throws IOException {
        String fileName = sefFile.getCanonicalPath();
        if (fileName != null && fileName.length() > 0) {
            return SEFJNI.listSEFDataTypes(fileName);
        }
        Log.e(TAG, "Invalid file name: " + fileName);
        return null;
    }

    public static int[] listSEFDataTypes(String fileName) {
        if (fileName != null && fileName.length() > 0) {
            return SEFJNI.listSEFDataTypes(fileName);
        }
        Log.e(TAG, "Invalid file name: " + fileName);
        return null;
    }

    public static int getMajorDataType(String fileName) {
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
            return -1;
        } else if (SEFJNI.isSEFFile(fileName) == 0) {
            return -1;
        } else {
            int[] dataTypes = listSEFDataTypes(fileName);
            if (dataTypes == null) {
                Log.e(TAG, "No data type has been found : " + fileName);
                return -1;
            }
            int i = dataTypes.length - 1;
            while (i > -1) {
                if (dataTypes[i] >= 2048 && dataTypes[i] <= 16384 && (dataTypes[i] & 15) == 0) {
                    return dataTypes[i];
                }
                i--;
            }
            Log.e(TAG, "No major data type has been found : " + fileName);
            return -1;
        }
    }

    public static int copyAllSEFData(File srcFile, File dstFile) throws IOException {
        String srcFileName = srcFile.getCanonicalPath();
        String dstFileName = dstFile.getCanonicalPath();
        if (srcFileName == null || srcFileName.length() <= 0) {
            Log.e(TAG, "Invalid src file name: " + srcFileName);
            return 0;
        } else if (dstFileName != null && dstFileName.length() > 0) {
            return SEFJNI.copyAllSEFData(srcFileName, dstFileName);
        } else {
            Log.e(TAG, "Invalid dst file name: " + dstFileName);
            return 0;
        }
    }

    public static int copyAllSEFData(String srcFileName, String dstFileName) {
        if (srcFileName == null || srcFileName.length() <= 0) {
            Log.e(TAG, "Invalid src file name: " + srcFileName);
            return 0;
        } else if (dstFileName != null && dstFileName.length() > 0) {
            return SEFJNI.copyAllSEFData(srcFileName, dstFileName);
        } else {
            Log.e(TAG, "Invalid dst file name: " + dstFileName);
            return 0;
        }
    }

    public static int saveAudioJPEG(String fileName, byte[] audiostream) {
        return saveAudioJPEG(fileName, KeyName.SOUND_SHOT_WAVE, audiostream);
    }

    public static int saveAudioJPEG(String fileName, String keyName, byte[] audioStream) {
        if (fileName != null && audioStream != null && audioStream.length > 0) {
            return SEFJNI.saveAudioJPEG(fileName, audioStream, audioStream.length, keyName, keyName.length());
        }
        Log.e(TAG, "Invalid file name: " + fileName);
        if (audioStream != null) {
            Log.e(TAG, "saveAudioJPEG input parameter is null :  audioStream.length = " + audioStream.length);
        } else {
            Log.e(TAG, "saveAudioJPEG input parameter is null ");
        }
        return 0;
    }

    public static int deleteAudioData(String fileName) {
        if (fileName != null && fileName.length() > 0) {
            return SEFJNI.deleteQdioData(fileName, KeyName.SOUND_SHOT_WAVE, KeyName.SOUND_SHOT_WAVE.length());
        }
        Log.e(TAG, "Invalid file name: " + fileName);
        return 0;
    }

    public static int deleteQdioData(String fileName) {
        if (fileName != null && fileName.length() > 0) {
            return SEFJNI.deleteQdioData(fileName, KeyName.SOUND_SHOT_WAVE, KeyName.SOUND_SHOT_WAVE.length());
        }
        Log.e(TAG, "Invalid file name: " + fileName);
        return 0;
    }

    public static int clearAudioData(String fileName) {
        if (fileName != null && fileName.length() > 0) {
            return SEFJNI.clearQdioData(fileName);
        }
        Log.e(TAG, "Invalid file name: " + fileName);
        return 0;
    }

    public static int clearQdioData(String fileName) {
        if (fileName != null && fileName.length() > 0) {
            return SEFJNI.clearQdioData(fileName);
        }
        Log.e(TAG, "Invalid file name: " + fileName);
        return 0;
    }

    public static AudioJPEGData getAudioDataInJPEG(String fileName) {
        if (fileName != null && fileName.length() > 0) {
            return QdioJNI.getAudioDataInJPEG(fileName);
        }
        Log.e(TAG, "Invalid file name: " + fileName);
        return null;
    }

    public static QdioJPEGData checkAudioInJPEG(String fileName) {
        if (fileName != null && fileName.length() > 0) {
            return QdioJNI.checkAudioInJPEG(fileName);
        }
        Log.e(TAG, "Invalid file name: " + fileName);
        return null;
    }

    public static byte[] getAudioStreamBuffer(AudioJPEGData qdioJpegData, int index) throws IOException {
        return QdioJNI.getAudioStreamBuffer(qdioJpegData, index);
    }

    public static byte[] getAudioStreamBuffer(QdioJPEGData qdioJpegData, int index) throws IOException {
        return QdioJNI.getAudioStreamBuffer(qdioJpegData, index);
    }

    public static int isAudioJPEG(String fileName) {
        return QdioJNI.isJPEG(fileName);
    }

    public static int isJPEG(String fileName) {
        return QdioJNI.isJPEG(fileName);
    }

    public static int copyAudioData(String srcFileName, String dstFileName) {
        if (srcFileName == null || srcFileName.length() <= 0) {
            Log.e(TAG, "Invalid src file name: " + srcFileName);
            return 0;
        } else if (dstFileName != null && dstFileName.length() > 0) {
            return QdioJNI.copyAdioInJPEGtoPNG(srcFileName, dstFileName);
        } else {
            Log.e(TAG, "Invalid dst file name: " + dstFileName);
            return 0;
        }
    }

    public static int copyAdioInJPEGtoPNG(String srcFileName, String dstFileName) {
        if (srcFileName == null || srcFileName.length() <= 0) {
            Log.e(TAG, "Invalid src file name: " + srcFileName);
            return 0;
        } else if (dstFileName != null && dstFileName.length() > 0) {
            return QdioJNI.copyAdioInJPEGtoPNG(srcFileName, dstFileName);
        } else {
            Log.e(TAG, "Invalid dst file name: " + dstFileName);
            return 0;
        }
    }

    public static void showSEFDataList(String fileName) {
        if (fileName == null || fileName.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + fileName);
        }
        if (SEFJNI.isSEFFile(fileName) != 0) {
        }
    }
}
