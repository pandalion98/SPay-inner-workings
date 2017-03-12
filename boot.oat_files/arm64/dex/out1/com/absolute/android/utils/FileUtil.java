package com.absolute.android.utils;

import java.io.File;

public class FileUtil {
    public static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            for (File deleteFile : file.listFiles()) {
                deleteFile(deleteFile);
            }
        }
        return file.delete();
    }

    public static String getFilename(String str) {
        String str2 = "";
        int lastIndexOf = str.lastIndexOf(File.separatorChar);
        if (lastIndexOf < 0 || lastIndexOf >= str.length()) {
            return str2;
        }
        return str.substring(lastIndexOf + 1, str.length());
    }
}
