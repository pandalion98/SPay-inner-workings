package android.support.multidex;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

final class MultiDexExtractor {
    private static final int BUFFER_SIZE = 16384;
    private static final String DEX_PREFIX = "classes";
    private static final String DEX_SUFFIX = ".dex";
    private static final String EXTRACTED_NAME_EXT = ".classes";
    private static final String EXTRACTED_SUFFIX = ".zip";
    private static final String KEY_CRC = "crc";
    private static final String KEY_DEX_NUMBER = "dex.number";
    private static final String KEY_TIME_STAMP = "timestamp";
    private static final int MAX_EXTRACT_ATTEMPTS = 3;
    private static final long NO_VALUE = -1;
    private static final String PREFS_FILE = "multidex.version";
    private static final String TAG = "MultiDex";
    private static Method sApplyMethod;

    /* renamed from: android.support.multidex.MultiDexExtractor.1 */
    static class C00001 implements FileFilter {
        final /* synthetic */ String val$extractedFilePrefix;

        C00001(String str) {
            this.val$extractedFilePrefix = str;
        }

        public boolean accept(File file) {
            return !file.getName().startsWith(this.val$extractedFilePrefix);
        }
    }

    MultiDexExtractor() {
    }

    static List<File> load(Context context, ApplicationInfo applicationInfo, File file, boolean z) {
        List<File> performExtractions;
        Log.i(TAG, "MultiDexExtractor.load(" + applicationInfo.sourceDir + ", " + z + ")");
        File file2 = new File(applicationInfo.sourceDir);
        long zipCrc = getZipCrc(file2);
        if (z || isModified(context, file2, zipCrc)) {
            Log.i(TAG, "Detected that extraction must be performed.");
            performExtractions = performExtractions(file2, file);
            putStoredApkInfo(context, getTimeStamp(file2), zipCrc, performExtractions.size() + 1);
        } else {
            try {
                performExtractions = loadExistingExtractions(context, file2, file);
            } catch (Throwable e) {
                Log.w(TAG, "Failed to reload existing extracted secondary dex files, falling back to fresh extraction", e);
                performExtractions = performExtractions(file2, file);
                putStoredApkInfo(context, getTimeStamp(file2), zipCrc, performExtractions.size() + 1);
            }
        }
        Log.i(TAG, "load found " + performExtractions.size() + " secondary dex files");
        return performExtractions;
    }

    private static List<File> loadExistingExtractions(Context context, File file, File file2) {
        Log.i(TAG, "loading existing secondary dex files");
        String str = file.getName() + EXTRACTED_NAME_EXT;
        int i = getMultiDexPreferences(context).getInt(KEY_DEX_NUMBER, 1);
        List<File> arrayList = new ArrayList(i);
        int i2 = 2;
        while (i2 <= i) {
            File file3 = new File(file2, str + i2 + EXTRACTED_SUFFIX);
            if (file3.isFile()) {
                arrayList.add(file3);
                if (verifyZipFile(file3)) {
                    i2++;
                } else {
                    Log.i(TAG, "Invalid zip file: " + file3);
                    throw new IOException("Invalid ZIP file.");
                }
            }
            throw new IOException("Missing extracted secondary dex file '" + file3.getPath() + "'");
        }
        return arrayList;
    }

    private static boolean isModified(Context context, File file, long j) {
        SharedPreferences multiDexPreferences = getMultiDexPreferences(context);
        return (multiDexPreferences.getLong(KEY_TIME_STAMP, NO_VALUE) == getTimeStamp(file) && multiDexPreferences.getLong(KEY_CRC, NO_VALUE) == j) ? false : true;
    }

    private static long getTimeStamp(File file) {
        long lastModified = file.lastModified();
        if (lastModified == NO_VALUE) {
            return lastModified - 1;
        }
        return lastModified;
    }

    private static long getZipCrc(File file) {
        long zipCrc = ZipUtil.getZipCrc(file);
        if (zipCrc == NO_VALUE) {
            return zipCrc - 1;
        }
        return zipCrc;
    }

    private static List<File> performExtractions(File file, File file2) {
        String str = file.getName() + EXTRACTED_NAME_EXT;
        prepareDexDir(file2, str);
        List<File> arrayList = new ArrayList();
        ZipFile zipFile = new ZipFile(file);
        try {
            ZipEntry entry = zipFile.getEntry(DEX_PREFIX + 2 + DEX_SUFFIX);
            int i = 2;
            while (entry != null) {
                File file3 = new File(file2, str + i + EXTRACTED_SUFFIX);
                arrayList.add(file3);
                Log.i(TAG, "Extraction is needed for file " + file3);
                boolean z = false;
                int i2 = 0;
                while (i2 < MAX_EXTRACT_ATTEMPTS && !z) {
                    int i3 = i2 + 1;
                    extract(zipFile, entry, file3, str);
                    boolean verifyZipFile = verifyZipFile(file3);
                    Log.i(TAG, "Extraction " + (verifyZipFile ? "success" : "failed") + " - length " + file3.getAbsolutePath() + ": " + file3.length());
                    if (!verifyZipFile) {
                        file3.delete();
                        if (file3.exists()) {
                            Log.w(TAG, "Failed to delete corrupted secondary dex '" + file3.getPath() + "'");
                            z = verifyZipFile;
                            i2 = i3;
                        }
                    }
                    z = verifyZipFile;
                    i2 = i3;
                }
                if (z) {
                    i2 = i + 1;
                    entry = zipFile.getEntry(DEX_PREFIX + i2 + DEX_SUFFIX);
                    i = i2;
                } else {
                    throw new IOException("Could not create zip file " + file3.getAbsolutePath() + " for secondary dex (" + i + ")");
                }
            }
            return arrayList;
        } finally {
            try {
                zipFile.close();
            } catch (Throwable e) {
                Log.w(TAG, "Failed to close resource", e);
            }
        }
    }

    private static void putStoredApkInfo(Context context, long j, long j2, int i) {
        Editor edit = getMultiDexPreferences(context).edit();
        edit.putLong(KEY_TIME_STAMP, j);
        edit.putLong(KEY_CRC, j2);
        edit.putInt(KEY_DEX_NUMBER, i);
        apply(edit);
    }

    private static SharedPreferences getMultiDexPreferences(Context context) {
        return context.getSharedPreferences(PREFS_FILE, VERSION.SDK_INT < 11 ? 0 : 4);
    }

    private static void prepareDexDir(File file, String str) {
        file.mkdirs();
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles(new C00001(str));
            if (listFiles == null) {
                Log.w(TAG, "Failed to list secondary dex dir content (" + file.getPath() + ").");
                return;
            }
            for (File file2 : listFiles) {
                Log.i(TAG, "Trying to delete old file " + file2.getPath() + " of size " + file2.length());
                if (file2.delete()) {
                    Log.i(TAG, "Deleted old file " + file2.getPath());
                } else {
                    Log.w(TAG, "Failed to delete old file " + file2.getPath());
                }
            }
            return;
        }
        throw new IOException("Failed to create dex directory " + file.getPath());
    }

    private static void extract(ZipFile zipFile, ZipEntry zipEntry, File file, String str) {
        Closeable inputStream = zipFile.getInputStream(zipEntry);
        File createTempFile = File.createTempFile(str, EXTRACTED_SUFFIX, file.getParentFile());
        Log.i(TAG, "Extracting " + createTempFile.getPath());
        ZipOutputStream zipOutputStream;
        try {
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(createTempFile)));
            ZipEntry zipEntry2 = new ZipEntry("classes.dex");
            zipEntry2.setTime(zipEntry.getTime());
            zipOutputStream.putNextEntry(zipEntry2);
            byte[] bArr = new byte[BUFFER_SIZE];
            for (int read = inputStream.read(bArr); read != -1; read = inputStream.read(bArr)) {
                zipOutputStream.write(bArr, 0, read);
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            Log.i(TAG, "Renaming to " + file.getPath());
            if (createTempFile.renameTo(file)) {
                closeQuietly(inputStream);
                createTempFile.delete();
                return;
            }
            throw new IOException("Failed to rename \"" + createTempFile.getAbsolutePath() + "\" to \"" + file.getAbsolutePath() + "\"");
        } catch (Throwable th) {
            closeQuietly(inputStream);
            createTempFile.delete();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean verifyZipFile(java.io.File r4) {
        /*
        r0 = new java.util.zip.ZipFile;	 Catch:{ ZipException -> 0x0029, IOException -> 0x004d }
        r0.<init>(r4);	 Catch:{ ZipException -> 0x0029, IOException -> 0x004d }
        r0.close();	 Catch:{ IOException -> 0x000a, ZipException -> 0x0029 }
        r0 = 1;
    L_0x0009:
        return r0;
    L_0x000a:
        r0 = move-exception;
        r0 = "MultiDex";
        r1 = new java.lang.StringBuilder;	 Catch:{ ZipException -> 0x0029, IOException -> 0x004d }
        r1.<init>();	 Catch:{ ZipException -> 0x0029, IOException -> 0x004d }
        r2 = "Failed to close zip file: ";
        r1 = r1.append(r2);	 Catch:{ ZipException -> 0x0029, IOException -> 0x004d }
        r2 = r4.getAbsolutePath();	 Catch:{ ZipException -> 0x0029, IOException -> 0x004d }
        r1 = r1.append(r2);	 Catch:{ ZipException -> 0x0029, IOException -> 0x004d }
        r1 = r1.toString();	 Catch:{ ZipException -> 0x0029, IOException -> 0x004d }
        android.util.Log.w(r0, r1);	 Catch:{ ZipException -> 0x0029, IOException -> 0x004d }
    L_0x0027:
        r0 = 0;
        goto L_0x0009;
    L_0x0029:
        r0 = move-exception;
        r1 = "MultiDex";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "File ";
        r2 = r2.append(r3);
        r3 = r4.getAbsolutePath();
        r2 = r2.append(r3);
        r3 = " is not a valid zip file.";
        r2 = r2.append(r3);
        r2 = r2.toString();
        android.util.Log.w(r1, r2, r0);
        goto L_0x0027;
    L_0x004d:
        r0 = move-exception;
        r1 = "MultiDex";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Got an IOException trying to open zip file: ";
        r2 = r2.append(r3);
        r3 = r4.getAbsolutePath();
        r2 = r2.append(r3);
        r2 = r2.toString();
        android.util.Log.w(r1, r2, r0);
        goto L_0x0027;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.multidex.MultiDexExtractor.verifyZipFile(java.io.File):boolean");
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (Throwable e) {
            Log.w(TAG, "Failed to close resource", e);
        }
    }

    static {
        try {
            sApplyMethod = Editor.class.getMethod("apply", new Class[0]);
        } catch (NoSuchMethodException e) {
            sApplyMethod = null;
        }
    }

    private static void apply(Editor editor) {
        if (sApplyMethod != null) {
            try {
                sApplyMethod.invoke(editor, new Object[0]);
                return;
            } catch (InvocationTargetException e) {
            } catch (IllegalAccessException e2) {
            }
        }
        editor.commit();
    }
}
