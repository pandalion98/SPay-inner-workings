package android.support.multidex;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.util.Log;
import dalvik.system.DexFile;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;
import org.bouncycastle.jce.X509KeyUsage;

public final class MultiDex {
    private static final boolean IS_VM_MULTIDEX_CAPABLE;
    private static final int MAX_SUPPORTED_SDK_VERSION = 20;
    private static final int MIN_SDK_VERSION = 4;
    private static final String OLD_SECONDARY_FOLDER_NAME = "secondary-dexes";
    private static final String SECONDARY_FOLDER_NAME;
    static final String TAG = "MultiDex";
    private static final int VM_WITH_MULTIDEX_VERSION_MAJOR = 2;
    private static final int VM_WITH_MULTIDEX_VERSION_MINOR = 1;
    private static final Set<String> installedApk;

    private static final class V14 {
        private V14() {
        }

        private static void install(ClassLoader classLoader, List<File> list, File file) {
            Object obj = MultiDex.findField(classLoader, "pathList").get(classLoader);
            MultiDex.expandFieldArray(obj, "dexElements", makeDexElements(obj, new ArrayList(list), file));
        }

        private static Object[] makeDexElements(Object obj, ArrayList<File> arrayList, File file) {
            Class[] clsArr = new Class[MultiDex.VM_WITH_MULTIDEX_VERSION_MAJOR];
            clsArr[0] = ArrayList.class;
            clsArr[MultiDex.VM_WITH_MULTIDEX_VERSION_MINOR] = File.class;
            Method access$500 = MultiDex.findMethod(obj, "makeDexElements", clsArr);
            Object[] objArr = new Object[MultiDex.VM_WITH_MULTIDEX_VERSION_MAJOR];
            objArr[0] = arrayList;
            objArr[MultiDex.VM_WITH_MULTIDEX_VERSION_MINOR] = file;
            return (Object[]) access$500.invoke(obj, objArr);
        }
    }

    private static final class V19 {
        private V19() {
        }

        private static void install(ClassLoader classLoader, List<File> list, File file) {
            Object obj = MultiDex.findField(classLoader, "pathList").get(classLoader);
            ArrayList arrayList = new ArrayList();
            MultiDex.expandFieldArray(obj, "dexElements", makeDexElements(obj, new ArrayList(list), file, arrayList));
            if (arrayList.size() > 0) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    Log.w(MultiDex.TAG, "Exception in makeDexElement", (IOException) it.next());
                }
                Field access$300 = MultiDex.findField(classLoader, "dexElementsSuppressedExceptions");
                IOException[] iOExceptionArr = (IOException[]) access$300.get(classLoader);
                if (iOExceptionArr == null) {
                    obj = (IOException[]) arrayList.toArray(new IOException[arrayList.size()]);
                } else {
                    Object obj2 = new IOException[(arrayList.size() + iOExceptionArr.length)];
                    arrayList.toArray(obj2);
                    System.arraycopy(iOExceptionArr, 0, obj2, arrayList.size(), iOExceptionArr.length);
                    obj = obj2;
                }
                access$300.set(classLoader, obj);
            }
        }

        private static Object[] makeDexElements(Object obj, ArrayList<File> arrayList, File file, ArrayList<IOException> arrayList2) {
            return (Object[]) MultiDex.findMethod(obj, "makeDexElements", ArrayList.class, File.class, ArrayList.class).invoke(obj, new Object[]{arrayList, file, arrayList2});
        }
    }

    private static final class V4 {
        private V4() {
        }

        private static void install(ClassLoader classLoader, List<File> list) {
            int size = list.size();
            Field access$300 = MultiDex.findField(classLoader, "path");
            StringBuilder stringBuilder = new StringBuilder((String) access$300.get(classLoader));
            String[] strArr = new String[size];
            File[] fileArr = new File[size];
            ZipFile[] zipFileArr = new ZipFile[size];
            DexFile[] dexFileArr = new DexFile[size];
            ListIterator listIterator = list.listIterator();
            while (listIterator.hasNext()) {
                File file = (File) listIterator.next();
                String absolutePath = file.getAbsolutePath();
                stringBuilder.append(':').append(absolutePath);
                int previousIndex = listIterator.previousIndex();
                strArr[previousIndex] = absolutePath;
                fileArr[previousIndex] = file;
                zipFileArr[previousIndex] = new ZipFile(file);
                dexFileArr[previousIndex] = DexFile.loadDex(absolutePath, absolutePath + ".dex", 0);
            }
            access$300.set(classLoader, stringBuilder.toString());
            MultiDex.expandFieldArray(classLoader, "mPaths", strArr);
            MultiDex.expandFieldArray(classLoader, "mFiles", fileArr);
            MultiDex.expandFieldArray(classLoader, "mZips", zipFileArr);
            MultiDex.expandFieldArray(classLoader, "mDexs", dexFileArr);
        }
    }

    static {
        SECONDARY_FOLDER_NAME = "code_cache" + File.separator + OLD_SECONDARY_FOLDER_NAME;
        installedApk = new HashSet();
        IS_VM_MULTIDEX_CAPABLE = isVMMultidexCapable(System.getProperty("java.vm.version"));
    }

    private MultiDex() {
    }

    public static void install(Context context) {
        Log.i(TAG, "install");
        if (IS_VM_MULTIDEX_CAPABLE) {
            Log.i(TAG, "VM has multidex support, MultiDex support library is disabled.");
        } else if (VERSION.SDK_INT < MIN_SDK_VERSION) {
            throw new RuntimeException("Multi dex installation failed. SDK " + VERSION.SDK_INT + " is unsupported. Min SDK version is " + MIN_SDK_VERSION + ".");
        } else {
            try {
                ApplicationInfo applicationInfo = getApplicationInfo(context);
                if (applicationInfo != null) {
                    synchronized (installedApk) {
                        String str = applicationInfo.sourceDir;
                        if (installedApk.contains(str)) {
                            return;
                        }
                        installedApk.add(str);
                        if (VERSION.SDK_INT > MAX_SUPPORTED_SDK_VERSION) {
                            Log.w(TAG, "MultiDex is not guaranteed to work in SDK version " + VERSION.SDK_INT + ": SDK version higher than " + MAX_SUPPORTED_SDK_VERSION + " should be backed by " + "runtime with built-in multidex capabilty but it's not the " + "case here: java.vm.version=\"" + System.getProperty("java.vm.version") + "\"");
                        }
                        try {
                            ClassLoader classLoader = context.getClassLoader();
                            if (classLoader == null) {
                                Log.e(TAG, "Context class loader is null. Must be running in test mode. Skip patching.");
                                return;
                            }
                            try {
                                clearOldDexDir(context);
                            } catch (Throwable th) {
                                Log.w(TAG, "Something went wrong when trying to clear old MultiDex extraction, continuing without cleaning.", th);
                            }
                            File file = new File(applicationInfo.dataDir, SECONDARY_FOLDER_NAME);
                            List load = MultiDexExtractor.load(context, applicationInfo, file, IS_VM_MULTIDEX_CAPABLE);
                            if (checkValidZipFiles(load)) {
                                installSecondaryDexes(classLoader, file, load);
                            } else {
                                Log.w(TAG, "Files were not valid zip files.  Forcing a reload.");
                                List load2 = MultiDexExtractor.load(context, applicationInfo, file, true);
                                if (checkValidZipFiles(load2)) {
                                    installSecondaryDexes(classLoader, file, load2);
                                } else {
                                    throw new RuntimeException("Zip files were not valid.");
                                }
                            }
                            Log.i(TAG, "install done");
                            return;
                        } catch (Throwable e) {
                            Log.w(TAG, "Failure while trying to obtain Context class loader. Must be running in test mode. Skip patching.", e);
                        }
                    }
                }
            } catch (Throwable e2) {
                Log.e(TAG, "Multidex installation failure", e2);
                throw new RuntimeException("Multi dex installation failed (" + e2.getMessage() + ").");
            }
        }
    }

    private static ApplicationInfo getApplicationInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            if (packageManager == null || packageName == null) {
                return null;
            }
            return packageManager.getApplicationInfo(packageName, X509KeyUsage.digitalSignature);
        } catch (Throwable e) {
            Log.w(TAG, "Failure while trying to obtain ApplicationInfo from Context. Must be running in test mode. Skip patching.", e);
            return null;
        }
    }

    static boolean isVMMultidexCapable(String str) {
        boolean z = IS_VM_MULTIDEX_CAPABLE;
        if (str != null) {
            Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(str);
            if (matcher.matches()) {
                try {
                    int parseInt = Integer.parseInt(matcher.group(VM_WITH_MULTIDEX_VERSION_MINOR));
                    int parseInt2 = Integer.parseInt(matcher.group(VM_WITH_MULTIDEX_VERSION_MAJOR));
                    if (parseInt > VM_WITH_MULTIDEX_VERSION_MAJOR || (parseInt == VM_WITH_MULTIDEX_VERSION_MAJOR && parseInt2 >= VM_WITH_MULTIDEX_VERSION_MINOR)) {
                        z = true;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        Log.i(TAG, "VM with version " + str + (z ? " has multidex support" : " does not have multidex support"));
        return z;
    }

    private static void installSecondaryDexes(ClassLoader classLoader, File file, List<File> list) {
        if (!list.isEmpty()) {
            if (VERSION.SDK_INT >= 19) {
                V19.install(classLoader, list, file);
            } else if (VERSION.SDK_INT >= 14) {
                V14.install(classLoader, list, file);
            } else {
                V4.install(classLoader, list);
            }
        }
    }

    private static boolean checkValidZipFiles(List<File> list) {
        for (File verifyZipFile : list) {
            if (!MultiDexExtractor.verifyZipFile(verifyZipFile)) {
                return IS_VM_MULTIDEX_CAPABLE;
            }
        }
        return true;
    }

    private static Field findField(Object obj, String str) {
        Class cls = obj.getClass();
        while (cls != null) {
            try {
                Field declaredField = cls.getDeclaredField(str);
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                }
                return declaredField;
            } catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field " + str + " not found in " + obj.getClass());
    }

    private static Method findMethod(Object obj, String str, Class<?>... clsArr) {
        Class cls = obj.getClass();
        while (cls != null) {
            try {
                Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
                if (!declaredMethod.isAccessible()) {
                    declaredMethod.setAccessible(true);
                }
                return declaredMethod;
            } catch (NoSuchMethodException e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchMethodException("Method " + str + " with parameters " + Arrays.asList(clsArr) + " not found in " + obj.getClass());
    }

    private static void expandFieldArray(Object obj, String str, Object[] objArr) {
        Field findField = findField(obj, str);
        Object[] objArr2 = (Object[]) findField.get(obj);
        Object[] objArr3 = (Object[]) Array.newInstance(objArr2.getClass().getComponentType(), objArr2.length + objArr.length);
        System.arraycopy(objArr2, 0, objArr3, 0, objArr2.length);
        System.arraycopy(objArr, 0, objArr3, objArr2.length, objArr.length);
        findField.set(obj, objArr3);
    }

    private static void clearOldDexDir(Context context) {
        File file = new File(context.getFilesDir(), OLD_SECONDARY_FOLDER_NAME);
        if (file.isDirectory()) {
            Log.i(TAG, "Clearing old secondary dex dir (" + file.getPath() + ").");
            File[] listFiles = file.listFiles();
            if (listFiles == null) {
                Log.w(TAG, "Failed to list secondary dex dir content (" + file.getPath() + ").");
                return;
            }
            int length = listFiles.length;
            for (int i = 0; i < length; i += VM_WITH_MULTIDEX_VERSION_MINOR) {
                File file2 = listFiles[i];
                Log.i(TAG, "Trying to delete old file " + file2.getPath() + " of size " + file2.length());
                if (file2.delete()) {
                    Log.i(TAG, "Deleted old file " + file2.getPath());
                } else {
                    Log.w(TAG, "Failed to delete old file " + file2.getPath());
                }
            }
            if (file.delete()) {
                Log.i(TAG, "Deleted old secondary dex dir " + file.getPath());
            } else {
                Log.w(TAG, "Failed to delete secondary dex dir " + file.getPath());
            }
        }
    }
}
