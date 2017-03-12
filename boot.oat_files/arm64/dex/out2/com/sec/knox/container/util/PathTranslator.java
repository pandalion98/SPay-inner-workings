package com.sec.knox.container.util;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PathTranslator {
    private static final String PACKAGE_DATA_PATH_PREFIX = "/data/user";
    private static final String PATH_MNT_EXTSD = "/mnt/extSdCard";
    private static final String PATH_MNT_KNOX = "/mnt/knox/default/emulated";
    private static final String PATH_MNT_RUNTIME = "/mnt/runtime/default/emulated";
    private static final String PATH_MNT_SD = "/mnt/sdcard";
    private static final String PATH_STORAGE_EMULATED = "/storage/emulated";
    private static final String PATH_STORAGE_EMULATED_EXP = "^/storage/emulated/([0-9]+)";
    private static final String PATH_STORAGE_EMULATED_LEGACY = "/storage/emulated/legacy";
    private static final String PATH_STORAGE_EXTSD = "/storage/extSdCard";
    private static final String PATH_STORAGE_SELF_PRIMARY = "/storage/self/primary";
    private static final Map<String, String> mFilePathMap = new HashMap();

    static {
        mFilePathMap.put("^/data/data", "/data/user/?");
        mFilePathMap.put("^/storage/enc_emulated/legacy", "/mnt/shell/enc_emulated/?");
        mFilePathMap.put("^/storage/enc_emulated/([0-9]+)", "/mnt/shell/enc_emulated/?");
        mFilePathMap.put("^/data/clipboard", "/data/clipboard");
        mFilePathMap.put("^/data/user", PACKAGE_DATA_PATH_PREFIX);
        mFilePathMap.put("^/data/system/container/", "/data/system/container/");
    }

    public static String getRealPath(String path, int containerId) {
        String realPath = "";
        Log.d("epmf", "path=" + path + " cid=" + containerId);
        if (path == null || path.length() < 1) {
            return null;
        }
        if (path.startsWith(PATH_MNT_EXTSD) || path.startsWith(PATH_STORAGE_EXTSD)) {
            return path;
        }
        if (!path.startsWith(PATH_MNT_SD) && !path.startsWith(PATH_STORAGE_SELF_PRIMARY) && !path.startsWith(PATH_STORAGE_EMULATED_LEGACY) && !path.startsWith(PATH_STORAGE_EMULATED)) {
            for (Entry<String, String> entry : mFilePathMap.entrySet()) {
                if (path.matches(((String) entry.getKey()) + ".*")) {
                    realPath = ((String) entry.getValue()).replace("?", String.valueOf(containerId));
                    path = path.replaceFirst((String) entry.getKey(), realPath);
                    Log.d("epmf", "match key=" + ((String) entry.getKey()) + " val=" + ((String) entry.getValue()) + " real=" + realPath);
                    break;
                }
            }
        }
        String leadPath;
        String srcPath;
        if (containerId >= 100) {
            leadPath = PATH_MNT_KNOX;
        } else {
            leadPath = PATH_MNT_RUNTIME;
        }
        leadPath = leadPath + "/" + String.valueOf(containerId);
        if (path.startsWith(PATH_MNT_SD)) {
            srcPath = PATH_MNT_SD;
        } else if (path.startsWith(PATH_STORAGE_SELF_PRIMARY)) {
            srcPath = PATH_STORAGE_SELF_PRIMARY;
        } else if (path.startsWith(PATH_STORAGE_EMULATED_LEGACY)) {
            srcPath = PATH_STORAGE_EMULATED_LEGACY;
        } else if (path.matches("^/storage/emulated/([0-9]+).*")) {
            srcPath = PATH_STORAGE_EMULATED_EXP;
        } else {
            srcPath = PATH_STORAGE_EMULATED;
        }
        path = path.replaceFirst(srcPath, leadPath);
        Log.d("epmf", "match key=" + srcPath + " real=" + leadPath);
        Log.d("epmf", "pathout=" + path);
        return path;
    }

    public static boolean isPackageDataRelatedPath(String path, int containerId) {
        String translatedPath = getRealPath(path, containerId);
        boolean retVal = translatedPath.startsWith(PACKAGE_DATA_PATH_PREFIX);
        if (retVal) {
            Log.d("epmf", "package path detected: " + translatedPath);
        } else {
            Log.d("epmf", "not a package path: " + translatedPath);
        }
        return retVal;
    }
}
