package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import java.io.File;

public class ContextCompat {
    private static final String DIR_ANDROID = "Android";
    private static final String DIR_CACHE = "cache";
    private static final String DIR_DATA = "data";
    private static final String DIR_FILES = "files";
    private static final String DIR_OBB = "obb";

    public static boolean startActivities(Context context, Intent[] intentArr) {
        return startActivities(context, intentArr, null);
    }

    public static boolean startActivities(Context context, Intent[] intentArr, Bundle bundle) {
        int i = VERSION.SDK_INT;
        if (i >= 16) {
            ContextCompatJellybean.startActivities(context, intentArr, bundle);
            return true;
        } else if (i < 11) {
            return false;
        } else {
            ContextCompatHoneycomb.startActivities(context, intentArr);
            return true;
        }
    }

    public static File[] getObbDirs(Context context) {
        int i = VERSION.SDK_INT;
        if (i >= 19) {
            return ContextCompatKitKat.getObbDirs(context);
        }
        File obbDir;
        if (i >= 11) {
            obbDir = ContextCompatHoneycomb.getObbDir(context);
        } else {
            obbDir = buildPath(Environment.getExternalStorageDirectory(), DIR_ANDROID, DIR_OBB, context.getPackageName());
        }
        return new File[]{obbDir};
    }

    public static File[] getExternalFilesDirs(Context context, String str) {
        int i = VERSION.SDK_INT;
        if (i >= 19) {
            return ContextCompatKitKat.getExternalFilesDirs(context, str);
        }
        File externalFilesDir;
        if (i >= 8) {
            externalFilesDir = ContextCompatFroyo.getExternalFilesDir(context, str);
        } else {
            externalFilesDir = buildPath(Environment.getExternalStorageDirectory(), DIR_ANDROID, DIR_DATA, context.getPackageName(), DIR_FILES, str);
        }
        return new File[]{externalFilesDir};
    }

    public static File[] getExternalCacheDirs(Context context) {
        int i = VERSION.SDK_INT;
        if (i >= 19) {
            return ContextCompatKitKat.getExternalCacheDirs(context);
        }
        File externalCacheDir;
        if (i >= 8) {
            externalCacheDir = ContextCompatFroyo.getExternalCacheDir(context);
        } else {
            externalCacheDir = buildPath(Environment.getExternalStorageDirectory(), DIR_ANDROID, DIR_DATA, context.getPackageName(), DIR_CACHE);
        }
        return new File[]{externalCacheDir};
    }

    private static File buildPath(File file, String... strArr) {
        int length = strArr.length;
        int i = 0;
        File file2 = file;
        while (i < length) {
            File file3;
            String str = strArr[i];
            if (file2 == null) {
                file3 = new File(str);
            } else if (str != null) {
                file3 = new File(file2, str);
            } else {
                file3 = file2;
            }
            i++;
            file2 = file3;
        }
        return file2;
    }
}
