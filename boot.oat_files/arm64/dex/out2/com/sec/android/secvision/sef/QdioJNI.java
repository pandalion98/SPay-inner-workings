package com.sec.android.secvision.sef;

import android.util.Log;
import com.sec.android.secvision.sef.SEF.AudioJPEGData;
import com.sec.android.secvision.sef.SEF.QdioJPEGData;
import java.io.FileInputStream;
import java.io.IOException;

public class QdioJNI {
    private static final String TAG = "QdioJNI";

    private static native int AddSoundInQdioFile(String str, byte[] bArr, int i, String str2, int i2);

    private static native int DeleteQdioFromFile(String str);

    private static native int[] ParseQdioFile(String str);

    private static native long[] ParseQdioFile64(String str);

    private static native int copyAdioData(String str, String str2);

    private static native int getNativeVersion();

    private static native int isQdioFile(String str);

    static {
        System.loadLibrary("SEF");
    }

    public static boolean checkFileString(String filename) {
        return filename != null && filename.length() > 0;
    }

    public static AudioJPEGData getAudioDataInJPEG(String filename) {
        int[] getParsedData = null;
        if (checkFileString(filename)) {
            getParsedData = ParseQdioFile(filename);
            if (getParsedData == null) {
                return null;
            }
            if (getParsedData.length % 2 != 0) {
                Log.e(TAG, "Some Sound Data is broken");
                return null;
            }
            AudioJPEGData ajData = new AudioJPEGData();
            int i = 0;
            while (i < getParsedData.length / 2) {
                if (getParsedData[i] <= 0 || getParsedData[i + 1] <= 0) {
                    Log.e(TAG, "Some Sound Data stream is broken");
                    return null;
                }
                ajData.startOffset.add(Integer.valueOf(getParsedData[i]));
                ajData.endOffset.add(Integer.valueOf(getParsedData[i + 1]));
                ajData.audio_count++;
                ajData.filename = filename;
                i++;
            }
            return ajData;
        }
        Log.e(TAG, "getAudioDataInJPEG input parameter is null : filename = " + filename);
        return null;
    }

    public static QdioJPEGData checkAudioInJPEG(String filename) {
        int[] getParsedData = null;
        if (checkFileString(filename)) {
            getParsedData = ParseQdioFile(filename);
            if (getParsedData == null) {
                return null;
            }
            if (getParsedData.length % 2 != 0) {
                Log.e(TAG, "Some Sound Data is broken");
                return null;
            }
            QdioJPEGData qjData = new QdioJPEGData();
            int i = 0;
            while (i < getParsedData.length / 2) {
                if (getParsedData[i] <= 0 || getParsedData[i + 1] <= 0) {
                    Log.e(TAG, "Some Sound Data stream is broken");
                    return null;
                }
                qjData.startOffset.add(Integer.valueOf(getParsedData[i]));
                qjData.endOffset.add(Integer.valueOf(getParsedData[i + 1]));
                qjData.audio_count++;
                qjData.filename = filename;
                i++;
            }
            return qjData;
        }
        Log.e(TAG, "checkAudioInJPEG input parameter is null : filename = " + filename);
        return null;
    }

    public static byte[] getAudioStreamBuffer(AudioJPEGData audioJpegData, int index) throws IOException {
        byte[] ret = null;
        if (audioJpegData == null) {
            Log.e(TAG, "qdioJpegData is null");
            return null;
        } else if (audioJpegData.audio_count <= index) {
            Log.e(TAG, "invalid index. file : " + audioJpegData.getFileName() + " has " + audioJpegData.audio_count + " audio streams but index = " + index);
            return null;
        } else {
            FileInputStream fis = new FileInputStream(audioJpegData.getFileName());
            int startOffset = audioJpegData.getStartOffset(index);
            int endOffset = startOffset + audioJpegData.getLength(index);
            if (fis.available() < endOffset) {
                Log.e(TAG, "fis.available is smaller then audio stream end : fileLen = " + fis.available() + ", audio strema end on " + endOffset);
                fis.close();
                return null;
            }
            try {
                Log.i(TAG, "fis.avaliable = " + fis.available());
                Log.i(TAG, "s = " + startOffset + ", " + endOffset);
                ret = new byte[(endOffset - startOffset)];
                if (startOffset < 0) {
                    return null;
                }
                if (fis.skip((long) startOffset) == 0) {
                    fis.close();
                    return null;
                } else if (fis.read(ret) == 0) {
                    fis.close();
                    return null;
                } else {
                    fis.close();
                    return ret;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fis.close();
            }
        }
    }

    public static byte[] getAudioStreamBuffer(QdioJPEGData qdioJpegData, int index) throws IOException {
        byte[] ret = null;
        if (qdioJpegData == null) {
            Log.e(TAG, "qdioJpegData is null");
            return null;
        } else if (qdioJpegData.audio_count <= index) {
            Log.e(TAG, "invalid index. file : " + qdioJpegData.getFileName() + " has " + qdioJpegData.audio_count + " audio streams but index = " + index);
            return null;
        } else {
            FileInputStream fis = new FileInputStream(qdioJpegData.getFileName());
            int startOffset = qdioJpegData.getStartOffset(index);
            int endOffset = startOffset + qdioJpegData.getLength(index);
            if (fis.available() < endOffset) {
                Log.e(TAG, "fis.available is smaller then audio stream end : fileLen = " + fis.available() + ", audio strema end on " + endOffset);
                fis.close();
                return null;
            }
            try {
                Log.i(TAG, "fis.avaliable = " + fis.available());
                Log.i(TAG, "s = " + startOffset + ", " + endOffset);
                ret = new byte[(endOffset - startOffset)];
                if (startOffset < 0) {
                    return null;
                }
                if (fis.skip((long) startOffset) == 0) {
                    fis.close();
                    return null;
                } else if (fis.read(ret) == 0) {
                    fis.close();
                    return null;
                } else {
                    fis.close();
                    return ret;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fis.close();
            }
        }
    }

    public static int isJPEG(String filename) {
        if (!checkFileString(filename)) {
            Log.e(TAG, "isJPEG input parameter is null : filename = " + filename);
            return -1;
        } else if (isQdioFile(filename) != 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public static int copyAdioInJPEGtoPNG(String srcFilename, String dstFilename) {
        if (checkFileString(srcFilename) && checkFileString(dstFilename)) {
            return copyAdioData(srcFilename, dstFilename);
        }
        return 0;
    }

    public static String getVersion() {
        return "1.02_" + getNativeVersion();
    }
}
