package com.android.internal.os;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.LocalServerSocket;
import android.opengl.EGL14;
import android.os.Debug;
import android.os.Process;
import android.os.Process.ZygoteState;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.service.notification.ZenModeConfig;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructPollfd;
import android.text.Hyphenator;
import android.util.EventLog;
import android.util.Log;
import android.webkit.WebViewFactory;
import com.android.internal.R;
import com.android.internal.telephony.PhoneConstants;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.multiwindow.MultiWindowFacade;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;
import dalvik.system.VMRuntime;
import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import libcore.io.IoUtils;

public class ZygoteInit {
    private static final String ABI_LIST_ARG = "--abi-list=";
    private static final String ANDROID_SOCKET_PREFIX = "ANDROID_SOCKET_";
    private static final int INITIAL_NUMBER_THREAD1 = 700;
    private static final int INITIAL_NUMBER_THREAD2 = 500;
    private static final int LOG_BOOT_PROGRESS_PRELOAD_END = 3030;
    private static final int LOG_BOOT_PROGRESS_PRELOAD_START = 3020;
    private static final boolean PARALLEL_LOAD = false;
    private static final String PRELOADED_CLASSES = "/system/etc/preloaded-classes";
    private static final int PRELOAD_GC_THRESHOLD = (((Integer.parseInt(heapgrowthlimit.substring(0, heapgrowthlimit.length() - 1)) * 1024) * 1024) / 2);
    private static final boolean PRELOAD_RESOURCES = true;
    private static final String PROPERTY_DISABLE_OPENGL_PRELOADING = "ro.zygote.disable_gl_preload";
    private static final String PROPERTY_NC_THREAD1 = "persist.sys.clssprld1";
    private static final String PROPERTY_NC_THREAD2 = "persist.sys.clssprld2";
    private static final int ROOT_GID = 0;
    private static final int ROOT_UID = 0;
    private static final String SOCKET_NAME_ARG = "--socket-name=";
    private static final String TAG = "Zygote";
    private static final int UNPRIVILEGED_GID = 9999;
    private static final int UNPRIVILEGED_UID = 9999;
    private static final boolean USE_DYNAMIC_BALANCE = true;
    private static final String heapgrowthlimit = "64m";
    static boolean isError = false;
    static boolean isParallelThreadRunning = false;
    static boolean isPreloadComplete = false;
    private static Resources mResources;
    private static int numberOfPreloadClasses = 0;
    private static int numberOfPreloadClassesforThread1 = INITIAL_NUMBER_THREAD1;
    private static int numberOfPreloadClassesforThread2 = 500;
    public static boolean parallelPCThread1running = false;
    public static boolean parallelPCThread2running = false;
    public static String[] postLoadClasses = new String[]{"android.database.CursorWindow", "android.database.CursorWindow$1"};
    private static LocalServerSocket sServerSocket;
    private static int thread1time = 0;
    private static int thread2time = 0;
    private static int threadMtime = 0;

    public static class MethodAndArgsCaller extends Exception implements Runnable {
        private final String[] mArgs;
        private final Method mMethod;

        public MethodAndArgsCaller(Method method, String[] args) {
            this.mMethod = method;
            this.mArgs = args;
        }

        public void run() {
            try {
                this.mMethod.invoke(null, new Object[]{this.mArgs});
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex2) {
                Throwable cause = ex2.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                } else if (cause instanceof Error) {
                    throw ((Error) cause);
                } else {
                    throw new RuntimeException(ex2);
                }
            }
        }
    }

    private static void registerZygoteSocket(String socketName) {
        if (sServerSocket == null) {
            String fullSocketName = ANDROID_SOCKET_PREFIX + socketName;
            try {
                int fileDesc = Integer.parseInt(System.getenv(fullSocketName));
                try {
                    FileDescriptor fd = new FileDescriptor();
                    fd.setInt$(fileDesc);
                    sServerSocket = new LocalServerSocket(fd);
                } catch (IOException ex) {
                    throw new RuntimeException("Error binding to local socket '" + fileDesc + "'", ex);
                }
            } catch (RuntimeException ex2) {
                throw new RuntimeException(fullSocketName + " unset or invalid", ex2);
            }
        }
    }

    private static ZygoteConnection acceptCommandPeer(String abiList) {
        try {
            return new ZygoteConnection(sServerSocket.accept(), abiList);
        } catch (IOException ex) {
            throw new RuntimeException("IOException during accept()", ex);
        }
    }

    static void closeServerSocket() {
        try {
            if (sServerSocket != null) {
                FileDescriptor fd = sServerSocket.getFileDescriptor();
                sServerSocket.close();
                if (fd != null) {
                    Os.close(fd);
                }
            }
        } catch (IOException ex) {
            Log.e(TAG, "Zygote:  error closing sockets", ex);
        } catch (ErrnoException ex2) {
            Log.e(TAG, "Zygote:  error closing descriptor", ex2);
        }
        sServerSocket = null;
    }

    static FileDescriptor getServerSocketFileDescriptor() {
        return sServerSocket.getFileDescriptor();
    }

    private static void setEffectiveUser(int uid) {
        try {
            Os.setreuid(0, uid);
        } catch (ErrnoException ex) {
            throw new RuntimeException("Failed sets effective user ID", ex);
        }
    }

    private static void setEffectiveGroup(int gid) {
        try {
            Os.setregid(0, gid);
        } catch (ErrnoException ex) {
            throw new RuntimeException("Failed sets effective group ID", ex);
        }
    }

    static void preload() {
        Log.i(TAG, "!@Boot: Begin of preload()");
        preloadClasses();
        preloadResources();
        preloadOpenGL();
        preloadSharedLibraries();
        preloadTextResources();
        WebViewFactory.prepareWebViewInZygote();
        Log.i(TAG, "!@Boot: End of preload()");
    }

    private static void preloadSharedLibraries() {
        Log.i(TAG, "Preloading shared libraries...");
        System.loadLibrary(ZenModeConfig.SYSTEM_AUTHORITY);
        System.loadLibrary("compiler_rt");
        System.loadLibrary("jnigraphics");
    }

    private static void preloadOpenGL() {
        if (!SystemProperties.getBoolean(PROPERTY_DISABLE_OPENGL_PRELOADING, false)) {
            EGL14.eglGetDisplay(0);
        }
    }

    private static void preloadTextResources() {
        Hyphenator.init();
    }

    private static void adjustClassPreloadBalance() {
        if (thread1time > 0 && thread2time > 0 && threadMtime > 0) {
            int min = Math.min(Math.min(thread1time, thread2time), threadMtime);
            int max = Math.max(Math.max(thread1time, thread2time), threadMtime);
            if (max - min > 100) {
                Log.e(TAG, " adjust " + max + " " + min + " ,1:" + thread1time + " ,2:" + thread2time + " ,M:" + threadMtime);
                if (max == thread1time) {
                    SystemProperties.set(PROPERTY_NC_THREAD1, (numberOfPreloadClassesforThread1 - 5) + "");
                } else if (max == thread2time) {
                    SystemProperties.set(PROPERTY_NC_THREAD2, (numberOfPreloadClassesforThread2 - 5) + "");
                }
                if (min == thread1time) {
                    SystemProperties.set(PROPERTY_NC_THREAD1, (numberOfPreloadClassesforThread1 + 5) + "");
                } else if (min == thread2time) {
                    SystemProperties.set(PROPERTY_NC_THREAD2, (numberOfPreloadClassesforThread2 + 5) + "");
                }
            }
        }
    }

    private static void preloadClasses() {
        VMRuntime runtime = VMRuntime.getRuntime();
        try {
            InputStream is = new FileInputStream(PRELOADED_CLASSES);
            Log.i(TAG, "Preloading classes...");
            long startTime = SystemClock.uptimeMillis();
            int reuid = Os.getuid();
            int regid = Os.getgid();
            boolean droppedPriviliges = false;
            if (reuid == 0 && regid == 0) {
                try {
                    Os.setregid(0, 9999);
                    Os.setreuid(0, 9999);
                    droppedPriviliges = true;
                } catch (ErrnoException ex) {
                    throw new RuntimeException("Failed to drop root", ex);
                }
            }
            float defaultUtilization = runtime.getTargetHeapUtilization();
            runtime.setTargetHeapUtilization(MultiWindowFacade.SPLIT_MAX_WEIGHT);
            BufferedReader br = new BufferedReader(new InputStreamReader(is), 256);
            int count = 0;
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (!(line.startsWith("#") || line.equals(""))) {
                    try {
                        Class.forName(line, true, null);
                        count++;
                    } catch (ClassNotFoundException e) {
                        Log.w(TAG, "Class not found for preloading: " + line);
                    } catch (UnsatisfiedLinkError e2) {
                        Log.w(TAG, "Problem preloading " + line + ": " + e2);
                    } catch (IOException e3) {
                        Log.e(TAG, "Error reading /system/etc/preloaded-classes.", e3);
                        IoUtils.closeQuietly(is);
                        runtime.setTargetHeapUtilization(defaultUtilization);
                        runtime.preloadDexCaches();
                        if (droppedPriviliges) {
                            try {
                                Os.setreuid(0, 0);
                                Os.setregid(0, 0);
                                return;
                            } catch (ErrnoException ex2) {
                                throw new RuntimeException("Failed to restore root", ex2);
                            }
                        }
                        return;
                    } catch (Throwable th) {
                        IoUtils.closeQuietly(is);
                        runtime.setTargetHeapUtilization(defaultUtilization);
                        runtime.preloadDexCaches();
                        if (droppedPriviliges) {
                            try {
                                Os.setreuid(0, 0);
                                Os.setregid(0, 0);
                            } catch (ErrnoException ex22) {
                                throw new RuntimeException("Failed to restore root", ex22);
                            }
                        }
                    }
                }
            }
            Log.i(TAG, "...preloaded " + count + " classes in " + (SystemClock.uptimeMillis() - startTime) + "ms.");
            IoUtils.closeQuietly(is);
            runtime.setTargetHeapUtilization(defaultUtilization);
            runtime.preloadDexCaches();
            if (droppedPriviliges) {
                try {
                    Os.setreuid(0, 0);
                    Os.setregid(0, 0);
                } catch (ErrnoException ex222) {
                    throw new RuntimeException("Failed to restore root", ex222);
                }
            }
        } catch (FileNotFoundException e4) {
            Log.e(TAG, "Couldn't find /system/etc/preloaded-classes.");
        }
    }

    static void parallelpreloadClasses() {
        String line;
        final VMRuntime runtime = VMRuntime.getRuntime();
        final ArrayList<String> classList = new ArrayList(3013);
        try {
            InputStream is = new FileInputStream(PRELOADED_CLASSES);
            Log.i(TAG, "Preloading classes...");
            float defaultUtilization = runtime.getTargetHeapUtilization();
            runtime.setTargetHeapUtilization(MultiWindowFacade.SPLIT_MAX_WEIGHT);
            System.gc();
            runtime.runFinalizationSync();
            Debug.startAllocCounting();
            BufferedReader br = new BufferedReader(new InputStreamReader(is), 256);
            int count = 0;
            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (!(line.startsWith("#") || line.equals(""))) {
                    classList.add(line);
                    count++;
                }
            }
            Log.i(TAG, "Number of total Classes to prelaod: " + count);
            numberOfPreloadClasses = count;
            try {
                numberOfPreloadClassesforThread1 = Integer.parseInt(SystemProperties.get(PROPERTY_NC_THREAD1, "700"));
                numberOfPreloadClassesforThread2 = Integer.parseInt(SystemProperties.get(PROPERTY_NC_THREAD2, "500"));
            } catch (NumberFormatException e) {
            }
            try {
                int j;
                if (numberOfPreloadClassesforThread1 < 1 || numberOfPreloadClassesforThread2 < 1 || numberOfPreloadClassesforThread1 + numberOfPreloadClassesforThread2 >= numberOfPreloadClasses) {
                    Log.i(TAG, "!@set default");
                    numberOfPreloadClassesforThread1 = numberOfPreloadClasses / 3;
                    numberOfPreloadClassesforThread2 = numberOfPreloadClasses / 3;
                    SystemProperties.set(PROPERTY_NC_THREAD1, numberOfPreloadClassesforThread1 + "");
                    SystemProperties.set(PROPERTY_NC_THREAD2, numberOfPreloadClassesforThread2 + "");
                }
                Log.i(TAG, "Classes to load for thread1 0~" + (numberOfPreloadClassesforThread1 - 1));
                Log.i(TAG, "Classes to load for thread2 " + numberOfPreloadClassesforThread1 + "~" + ((numberOfPreloadClassesforThread1 + numberOfPreloadClassesforThread2) - 1));
                Log.i(TAG, "Classes to load for thread3 " + (numberOfPreloadClassesforThread1 + numberOfPreloadClassesforThread2) + "~" + (numberOfPreloadClasses - 1));
                new Thread(new Runnable() {
                    public void run() {
                        ZygoteInit.parallelPCThread1running = true;
                        long startTime = SystemClock.uptimeMillis();
                        Process.setThreadPriority(-16);
                        for (int i = 0; i < ZygoteInit.numberOfPreloadClassesforThread1; i++) {
                            String line = (String) classList.get(i);
                            int j = 0;
                            while (j < ZygoteInit.postLoadClasses.length) {
                                try {
                                    if (line.equals(ZygoteInit.postLoadClasses[j])) {
                                        Log.v(ZygoteInit.TAG, "postpone " + line);
                                        line = null;
                                        break;
                                    }
                                    j++;
                                } catch (ClassNotFoundException e) {
                                    Log.w(ZygoteInit.TAG, "Class not found for preloading: " + line);
                                } catch (Throwable t) {
                                    Log.e(ZygoteInit.TAG, "!@Error preloading " + line + ".", t);
                                    Process.killProcess(Process.myPid());
                                    System.exit(10);
                                    if (t instanceof Error) {
                                        Error t2 = (Error) t;
                                    } else if (t instanceof RuntimeException) {
                                        RuntimeException t3 = (RuntimeException) t;
                                    } else {
                                        RuntimeException runtimeException = new RuntimeException(t);
                                    }
                                }
                            }
                            if (line != null) {
                                Class.forName(line);
                                if (Debug.getGlobalAllocSize() > ZygoteInit.PRELOAD_GC_THRESHOLD) {
                                    Log.e(ZygoteInit.TAG, "!@GC at " + Debug.getGlobalAllocSize());
                                    System.gc();
                                    runtime.runFinalizationSync();
                                    Debug.resetGlobalAllocSize();
                                    Log.e(ZygoteInit.TAG, "!@GC end");
                                }
                            }
                        }
                        Log.i(ZygoteInit.TAG, "!@prldclss1: " + ZygoteInit.numberOfPreloadClassesforThread1 + " classes in " + (SystemClock.uptimeMillis() - startTime) + "ms.");
                        ZygoteInit.parallelPCThread1running = false;
                        ZygoteInit.thread1time = (int) SystemClock.uptimeMillis();
                    }
                }, "prct_1").start();
                new Thread(new Runnable() {
                    public void run() {
                        ZygoteInit.parallelPCThread2running = true;
                        long startTime = SystemClock.uptimeMillis();
                        Process.setThreadPriority(-16);
                        for (int i = ZygoteInit.numberOfPreloadClassesforThread1; i < ZygoteInit.numberOfPreloadClassesforThread1 + ZygoteInit.numberOfPreloadClassesforThread2; i++) {
                            String line = (String) classList.get(i);
                            int j = 0;
                            while (j < ZygoteInit.postLoadClasses.length) {
                                try {
                                    if (line.equals(ZygoteInit.postLoadClasses[j])) {
                                        Log.v(ZygoteInit.TAG, "postpone " + line);
                                        line = null;
                                        break;
                                    }
                                    j++;
                                } catch (ClassNotFoundException e) {
                                    Log.w(ZygoteInit.TAG, "Class not found for preloading: " + line);
                                } catch (Throwable t) {
                                    Log.e(ZygoteInit.TAG, "!@Error preloading " + line + ".", t);
                                    Process.killProcess(Process.myPid());
                                    System.exit(10);
                                    if (t instanceof Error) {
                                        Error t2 = (Error) t;
                                    } else if (t instanceof RuntimeException) {
                                        RuntimeException t3 = (RuntimeException) t;
                                    } else {
                                        RuntimeException runtimeException = new RuntimeException(t);
                                    }
                                }
                            }
                            if (line != null) {
                                Class.forName(line);
                                if (Debug.getGlobalAllocSize() > ZygoteInit.PRELOAD_GC_THRESHOLD) {
                                    Log.e(ZygoteInit.TAG, "!@GC at " + Debug.getGlobalAllocSize());
                                    System.gc();
                                    runtime.runFinalizationSync();
                                    Debug.resetGlobalAllocSize();
                                    Log.e(ZygoteInit.TAG, "!@GC end");
                                }
                            }
                        }
                        Log.i(ZygoteInit.TAG, "!@prldclss2: " + ZygoteInit.numberOfPreloadClassesforThread2 + " classes in " + (SystemClock.uptimeMillis() - startTime) + "ms.");
                        ZygoteInit.parallelPCThread2running = false;
                        ZygoteInit.thread2time = (int) SystemClock.uptimeMillis();
                    }
                }, "prct_2").start();
                long startTime = SystemClock.uptimeMillis();
                for (int i = numberOfPreloadClassesforThread1 + numberOfPreloadClassesforThread2; i < classList.size(); i++) {
                    line = (String) classList.get(i);
                    if (line == null) {
                        break;
                    }
                    for (String equals : postLoadClasses) {
                        if (line.equals(equals)) {
                            Log.v(TAG, "postpone " + line);
                            line = null;
                            break;
                        }
                    }
                    if (line != null) {
                        Class.forName(line);
                        if (Debug.getGlobalAllocSize() > PRELOAD_GC_THRESHOLD) {
                            System.gc();
                            runtime.runFinalizationSync();
                            Debug.resetGlobalAllocSize();
                        }
                    }
                }
                Log.i(TAG, "!@prldclssM: " + ((numberOfPreloadClasses - numberOfPreloadClassesforThread1) - numberOfPreloadClassesforThread2) + " classes in " + (SystemClock.uptimeMillis() - startTime) + "ms.");
                threadMtime = (int) SystemClock.uptimeMillis();
                while (true) {
                    if (!parallelPCThread1running && !parallelPCThread2running) {
                        break;
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    Log.i(TAG, "Wait PC Thread 1=" + parallelPCThread1running + " 2=" + parallelPCThread2running);
                }
                for (j = 0; j < postLoadClasses.length; j++) {
                    try {
                        Class.forName(postLoadClasses[j]);
                        Log.v(TAG, "post pld=" + postLoadClasses[j]);
                    } catch (ClassNotFoundException e3) {
                        Log.w(TAG, "Class not found for preloading: " + line);
                    }
                }
                IoUtils.closeQuietly(is);
                runtime.setTargetHeapUtilization(defaultUtilization);
                runtime.preloadDexCaches();
                Debug.stopAllocCounting();
            } catch (ClassNotFoundException e4) {
                Log.w(TAG, "Class not found for preloading: " + line);
            } catch (IOException e5) {
                Log.e(TAG, "Error reading /system/etc/preloaded-classes.", e5);
                IoUtils.closeQuietly(is);
                runtime.setTargetHeapUtilization(defaultUtilization);
                runtime.preloadDexCaches();
                Debug.stopAllocCounting();
            } catch (Throwable th) {
                IoUtils.closeQuietly(is);
                runtime.setTargetHeapUtilization(defaultUtilization);
                runtime.preloadDexCaches();
                Debug.stopAllocCounting();
            }
        } catch (FileNotFoundException e6) {
            Log.e(TAG, "Couldn't find /system/etc/preloaded-classes.");
        }
    }

    private static void preloadResources() {
        VMRuntime runtime = VMRuntime.getRuntime();
        try {
            mResources = Resources.getSystem();
            mResources.startPreloading();
            Log.i(TAG, "Preloading resources...");
            long startTime = SystemClock.uptimeMillis();
            TypedArray ar = mResources.obtainTypedArray(R.array.preloaded_drawables);
            int N = preloadDrawables(runtime, ar);
            ar.recycle();
            Log.i(TAG, "...preloaded " + N + " resources in " + (SystemClock.uptimeMillis() - startTime) + "ms.");
            startTime = SystemClock.uptimeMillis();
            ar = mResources.obtainTypedArray(R.array.preloaded_color_state_lists);
            N = preloadColorStateLists(runtime, ar);
            ar.recycle();
            Log.i(TAG, "...preloaded " + N + " resources in " + (SystemClock.uptimeMillis() - startTime) + "ms.");
            mResources.finishPreloading();
        } catch (RuntimeException e) {
            Log.w(TAG, "Failure preloading resources", e);
        }
    }

    private static int preloadColorStateLists(VMRuntime runtime, TypedArray ar) {
        int N = ar.length();
        int i = 0;
        while (i < N) {
            int id = ar.getResourceId(i, 0);
            if (id == 0 || mResources.getColorStateList(id, null) != null) {
                i++;
            } else {
                throw new IllegalArgumentException("Unable to find preloaded color resource #0x" + Integer.toHexString(id) + " (" + ar.getString(i) + ")");
            }
        }
        return N;
    }

    private static int preloadDrawables(VMRuntime runtime, TypedArray ar) {
        int N = ar.length();
        int i = 0;
        while (i < N) {
            int id = ar.getResourceId(i, 0);
            if (id == 0 || mResources.getDrawable(id, null) != null) {
                i++;
            } else {
                throw new IllegalArgumentException("Unable to find preloaded drawable resource #0x" + Integer.toHexString(id) + " (" + ar.getString(i) + ")");
            }
        }
        return N;
    }

    static void gcAndFinalize() {
        VMRuntime runtime = VMRuntime.getRuntime();
        System.gc();
        runtime.runFinalizationSync();
        System.gc();
    }

    private static void handleSystemServerProcess(Arguments parsedArgs) throws MethodAndArgsCaller {
        closeServerSocket();
        Os.umask(OsConstants.S_IRWXG | OsConstants.S_IRWXO);
        if (parsedArgs.niceName != null) {
            Process.setArgV0(parsedArgs.niceName);
        }
        String systemServerClasspath = Os.getenv("SYSTEMSERVERCLASSPATH");
        if (systemServerClasspath != null) {
            performSystemServerDexOpt(systemServerClasspath);
        }
        if (parsedArgs.invokeWith != null) {
            String[] args = parsedArgs.remainingArgs;
            if (systemServerClasspath != null) {
                String[] amendedArgs = new String[(args.length + 2)];
                amendedArgs[0] = "-cp";
                amendedArgs[1] = systemServerClasspath;
                System.arraycopy(parsedArgs.remainingArgs, 0, amendedArgs, 2, parsedArgs.remainingArgs.length);
            }
            WrapperInit.execApplication(parsedArgs.invokeWith, parsedArgs.niceName, parsedArgs.targetSdkVersion, VMRuntime.getCurrentInstructionSet(), null, args);
            return;
        }
        ClassLoader cl = null;
        if (systemServerClasspath != null) {
            cl = new PathClassLoader(systemServerClasspath, ClassLoader.getSystemClassLoader());
            Thread.currentThread().setContextClassLoader(cl);
        }
        RuntimeInit.zygoteInit(parsedArgs.targetSdkVersion, parsedArgs.remainingArgs, cl);
    }

    private static void performSystemServerDexOpt(String classPath) {
        String[] classPathElements = classPath.split(":");
        InstallerConnection installer = new InstallerConnection();
        installer.waitForConnection();
        String instructionSet = VMRuntime.getRuntime().vmInstructionSet();
        try {
            for (String classPathElement : classPathElements) {
                int dexoptNeeded = DexFile.getDexOptNeeded(classPathElement, PhoneConstants.APN_TYPE_ALL, instructionSet, false);
                if (dexoptNeeded != 0) {
                    installer.dexopt(classPathElement, 1000, false, instructionSet, dexoptNeeded, false);
                }
            }
            installer.disconnect();
        } catch (IOException ioe) {
            throw new RuntimeException("Error starting system_server", ioe);
        } catch (Throwable th) {
            installer.disconnect();
        }
    }

    private static boolean startSystemServer(String abiList, String socketName) throws MethodAndArgsCaller, RuntimeException {
        IllegalArgumentException ex;
        long capabilities = posixCapabilitiesAsBits(OsConstants.CAP_BLOCK_SUSPEND, OsConstants.CAP_KILL, OsConstants.CAP_NET_ADMIN, OsConstants.CAP_NET_BIND_SERVICE, OsConstants.CAP_NET_BROADCAST, OsConstants.CAP_NET_RAW, OsConstants.CAP_SYS_MODULE, OsConstants.CAP_SYS_NICE, OsConstants.CAP_SYS_RESOURCE, OsConstants.CAP_SYS_TIME, OsConstants.CAP_SYS_TTY_CONFIG);
        try {
            Arguments parsedArgs = new Arguments(new String[]{"--setuid=1000", "--setgid=1000", "--setgroups=1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1018,1021,1032,3001,3002,3003,3004,3005,3006,3007", "--capabilities=" + capabilities + FingerprintManager.FINGER_PERMISSION_DELIMITER + capabilities, "--nice-name=system_server", "--runtime-args", "com.android.server.SystemServer"});
            try {
                ZygoteConnection.applyDebuggerSystemProperty(parsedArgs);
                ZygoteConnection.applyInvokeWithSystemProperty(parsedArgs);
                if (Zygote.forkSystemServer(parsedArgs.uid, parsedArgs.gid, parsedArgs.gids, parsedArgs.debugFlags, (int[][]) null, parsedArgs.permittedCapabilities, parsedArgs.effectiveCapabilities) == 0) {
                    if (hasSecondZygote(abiList)) {
                        waitForSecondaryZygote(socketName);
                    }
                    handleSystemServerProcess(parsedArgs);
                }
                return true;
            } catch (IllegalArgumentException e) {
                ex = e;
                Arguments arguments = parsedArgs;
                throw new RuntimeException(ex);
            }
        } catch (IllegalArgumentException e2) {
            ex = e2;
            throw new RuntimeException(ex);
        }
    }

    private static long posixCapabilitiesAsBits(int... capabilities) {
        long result = 0;
        for (int capability : capabilities) {
            if (capability < 0 || capability > OsConstants.CAP_LAST_CAP) {
                throw new IllegalArgumentException(String.valueOf(capability));
            }
            result |= 1 << capability;
        }
        return result;
    }

    public static void main(String[] argv) {
        try {
            Process.setThreadPriority(-16);
            RuntimeInit.enableDdms();
            SamplingProfilerIntegration.start();
            boolean startSystemServer = false;
            String socketName = "zygote";
            String abiList = null;
            for (int i = 1; i < argv.length; i++) {
                if ("start-system-server".equals(argv[i])) {
                    startSystemServer = true;
                } else if (argv[i].startsWith(ABI_LIST_ARG)) {
                    abiList = argv[i].substring(ABI_LIST_ARG.length());
                } else if (argv[i].startsWith(SOCKET_NAME_ARG)) {
                    socketName = argv[i].substring(SOCKET_NAME_ARG.length());
                } else {
                    throw new RuntimeException("Unknown command line argument: " + argv[i]);
                }
            }
            if (abiList == null) {
                throw new RuntimeException("No ABI list supplied.");
            }
            registerZygoteSocket(socketName);
            EventLog.writeEvent((int) LOG_BOOT_PROGRESS_PRELOAD_START, SystemClock.uptimeMillis());
            preload();
            EventLog.writeEvent((int) LOG_BOOT_PROGRESS_PRELOAD_END, SystemClock.uptimeMillis());
            SamplingProfilerIntegration.writeZygoteSnapshot();
            gcAndFinalize();
            Trace.setTracingEnabled(false);
            Process.setThreadPriority(0);
            if (startSystemServer) {
                startSystemServer(abiList, socketName);
            }
            Log.i(TAG, "Accepting command socket connections");
            runSelectLoop(abiList);
            closeServerSocket();
        } catch (MethodAndArgsCaller caller) {
            caller.run();
        } catch (RuntimeException ex) {
            Log.e(TAG, "Zygote died with exception", ex);
            closeServerSocket();
            throw ex;
        }
    }

    private static boolean hasSecondZygote(String abiList) {
        return !SystemProperties.get("ro.product.cpu.abilist").equals(abiList);
    }

    private static void waitForSecondaryZygote(String socketName) {
        String otherZygoteName = "zygote".equals(socketName) ? "zygote_secondary" : "zygote";
        while (true) {
            try {
                ZygoteState.connect(otherZygoteName).close();
                break;
            } catch (IOException ioe) {
                Log.w(TAG, "Got error connecting to zygote, retrying. msg= " + ioe.getMessage());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private static void runSelectLoop(String abiList) throws MethodAndArgsCaller {
        ArrayList<FileDescriptor> fds = new ArrayList();
        ArrayList<ZygoteConnection> peers = new ArrayList();
        fds.add(sServerSocket.getFileDescriptor());
        peers.add(null);
        while (true) {
            int i;
            StructPollfd[] pollFds = new StructPollfd[fds.size()];
            for (i = 0; i < pollFds.length; i++) {
                pollFds[i] = new StructPollfd();
                pollFds[i].fd = (FileDescriptor) fds.get(i);
                pollFds[i].events = (short) OsConstants.POLLIN;
            }
            try {
                Os.poll(pollFds, -1);
                for (i = pollFds.length - 1; i >= 0; i--) {
                    if ((pollFds[i].revents & OsConstants.POLLIN) != 0) {
                        if (i == 0) {
                            ZygoteConnection newPeer = acceptCommandPeer(abiList);
                            peers.add(newPeer);
                            fds.add(newPeer.getFileDesciptor());
                        } else if (((ZygoteConnection) peers.get(i)).runOnce()) {
                            peers.remove(i);
                            fds.remove(i);
                        }
                    }
                }
            } catch (ErrnoException ex) {
                throw new RuntimeException("poll failed", ex);
            }
        }
    }

    private ZygoteInit() {
    }
}
