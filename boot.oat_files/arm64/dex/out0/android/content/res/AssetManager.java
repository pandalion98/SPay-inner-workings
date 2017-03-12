package android.content.res;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public final class AssetManager implements AutoCloseable {
    public static final int ACCESS_BUFFER = 3;
    public static final int ACCESS_RANDOM = 1;
    public static final int ACCESS_STREAMING = 2;
    public static final int ACCESS_UNKNOWN = 0;
    private static final boolean DEBUG_REFS = false;
    static final int STYLE_ASSET_COOKIE = 2;
    static final int STYLE_CHANGING_CONFIGURATIONS = 4;
    static final int STYLE_DATA = 1;
    static final int STYLE_DENSITY = 5;
    static final int STYLE_NUM_ENTRIES = 6;
    static final int STYLE_RESOURCE_ID = 3;
    static final int STYLE_TYPE = 0;
    private static final String TAG = "AssetManager";
    private static final boolean isElasticEnabled = true;
    private static final boolean localLOGV = false;
    private static final Object sSync = new Object();
    static AssetManager sSystem = null;
    public HashMap<Integer, Integer> elasticAppCookieOffset;
    private Object ensuringLock;
    private int mNumRefs;
    private long mObject;
    private final long[] mOffsets;
    private boolean mOpen;
    ArrayList<String> mOverlays;
    private HashMap<Long, RuntimeException> mRefStacks;
    private StringBlock[] mStringBlocks;
    private final TypedValue mValue;

    public final class AssetInputStream extends InputStream {
        private long mAsset;
        private long mLength;
        private long mMarkPos;

        public final int getAssetInt() {
            throw new UnsupportedOperationException();
        }

        public final long getNativeAsset() {
            return this.mAsset;
        }

        private AssetInputStream(long asset) {
            this.mAsset = asset;
            this.mLength = AssetManager.this.getAssetLength(asset);
        }

        public final int read() throws IOException {
            return AssetManager.this.readAssetChar(this.mAsset);
        }

        public final boolean markSupported() {
            return true;
        }

        public final int available() throws IOException {
            long len = AssetManager.this.getAssetRemainingLength(this.mAsset);
            return len > 2147483647L ? Integer.MAX_VALUE : (int) len;
        }

        public final void close() throws IOException {
            synchronized (AssetManager.this) {
                if (this.mAsset != 0) {
                    AssetManager.this.destroyAsset(this.mAsset);
                    this.mAsset = 0;
                    AssetManager.this.decRefsLocked((long) hashCode());
                }
            }
        }

        public final void mark(int readlimit) {
            this.mMarkPos = AssetManager.this.seekAsset(this.mAsset, 0, 0);
        }

        public final void reset() throws IOException {
            AssetManager.this.seekAsset(this.mAsset, this.mMarkPos, -1);
        }

        public final int read(byte[] b) throws IOException {
            return AssetManager.this.readAsset(this.mAsset, b, 0, b.length);
        }

        public final int read(byte[] b, int off, int len) throws IOException {
            return AssetManager.this.readAsset(this.mAsset, b, off, len);
        }

        public final long skip(long n) throws IOException {
            long pos = AssetManager.this.seekAsset(this.mAsset, 0, 0);
            if (pos + n > this.mLength) {
                n = this.mLength - pos;
            }
            if (n > 0) {
                AssetManager.this.seekAsset(this.mAsset, n, 0);
            }
            return n;
        }

        protected void finalize() throws Throwable {
            close();
        }
    }

    private final native int addAssetPathNative(String str, int i);

    static final native boolean applyStyle(long j, int i, int i2, long j2, int[] iArr, int[] iArr2, int[] iArr3);

    static final native void applyThemeStyle(long j, int i, boolean z);

    static final native void clearTheme(long j);

    static final native void copyTheme(long j, long j2);

    private final native void deleteTheme(long j);

    private final native void destroy();

    private final native void destroyAsset(long j);

    static final native void dumpTheme(long j, int i, String str, String str2);

    private final native int[] getArrayStringInfo(int i);

    private final native String[] getArrayStringResource(int i);

    public static final native String getAssetAllocations();

    private final native long getAssetLength(long j);

    private final native long getAssetRemainingLength(long j);

    public static final native int getGlobalAssetCount();

    public static final native int getGlobalAssetManagerCount();

    private final native long getNativeStringBlock(int i);

    private final native int getOverlayIDNative(int i);

    private final native int getStringBlockCount();

    static final native int getThemeChangingConfigurations(long j);

    private final native void init(boolean z);

    private final native int loadResourceBagValue(int i, int i2, TypedValue typedValue, boolean z);

    private final native int loadResourceValue(int i, short s, TypedValue typedValue, boolean z);

    static final native int loadThemeAttributeValue(long j, int i, TypedValue typedValue, boolean z);

    private final native long newTheme();

    private final native long openAsset(String str, int i);

    private final native ParcelFileDescriptor openAssetFd(String str, long[] jArr) throws IOException;

    private native ParcelFileDescriptor openNonAssetFdNative(int i, String str, long[] jArr) throws IOException;

    private final native long openNonAssetNative(int i, String str, int i2);

    private final native long openXmlAssetNative(int i, String str, int i2);

    private final native int readAsset(long j, byte[] bArr, int i, int i2);

    private final native int readAssetChar(long j);

    private final native int removeOverlayPathNative(String str);

    static final native boolean resolveAttrs(long j, int i, int i2, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4);

    private final native long seekAsset(long j, long j2, int i);

    private final native void setConfiguration_(int i, int i2, String str, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16);

    public final native int addOverlayPathNative(String str);

    final native int[] getArrayIntResource(int i);

    final native int getArraySize(int i);

    public final native SparseArray<String> getAssignedPackageIdentifiers();

    public final native String getCookieName(int i);

    public final native String[] getLocales();

    final native String getResourceEntryName(int i);

    final native int getResourceIdentifier(String str, String str2, String str3);

    final native String getResourceName(int i);

    final native String getResourcePackageName(int i);

    final native String getResourceTypeName(int i);

    final native int[] getStyleAttributes(int i);

    public final native boolean isUpToDate();

    public final native String[] list(String str) throws IOException;

    final native int retrieveArray(int i, int[] iArr);

    final native boolean retrieveAttributes(long j, int[] iArr, int[] iArr2, int[] iArr3);

    public final native void setLocale(String str);

    public AssetManager() {
        this.mValue = new TypedValue();
        this.mOffsets = new long[2];
        this.mStringBlocks = null;
        this.mNumRefs = 1;
        this.mOpen = true;
        this.elasticAppCookieOffset = new HashMap();
        this.ensuringLock = new Object();
        this.mOverlays = new ArrayList();
        synchronized (this) {
            init(false);
            ensureSystemAssets();
        }
    }

    private static void ensureSystemAssets() {
        synchronized (sSync) {
            if (sSystem == null) {
                AssetManager system = new AssetManager(true);
                system.makeStringBlocks(null);
                sSystem = system;
            }
        }
    }

    private AssetManager(boolean isSystem) {
        this.mValue = new TypedValue();
        this.mOffsets = new long[2];
        this.mStringBlocks = null;
        this.mNumRefs = 1;
        this.mOpen = true;
        this.elasticAppCookieOffset = new HashMap();
        this.ensuringLock = new Object();
        this.mOverlays = new ArrayList();
        init(true);
    }

    public static AssetManager getSystem() {
        ensureSystemAssets();
        return sSystem;
    }

    public void close() {
        synchronized (this) {
            if (this.mOpen) {
                this.mOpen = false;
                decRefsLocked((long) hashCode());
            }
        }
    }

    final CharSequence getResourceText(int ident) {
        synchronized (this) {
            TypedValue tmpValue = this.mValue;
            int block = loadResourceValue(ident, (short) 0, tmpValue, true);
            if (block < 0) {
                return null;
            } else if (tmpValue.type == 3) {
                r2 = this.mStringBlocks[block].get(tmpValue.data);
                return r2;
            } else {
                r2 = tmpValue.coerceToString();
                return r2;
            }
        }
    }

    final CharSequence getResourceBagText(int ident, int bagEntryId) {
        synchronized (this) {
            TypedValue tmpValue = this.mValue;
            int block = loadResourceBagValue(ident, bagEntryId, tmpValue, true);
            if (block < 0) {
                return null;
            } else if (tmpValue.type == 3) {
                r2 = this.mStringBlocks[block].get(tmpValue.data);
                return r2;
            } else {
                r2 = tmpValue.coerceToString();
                return r2;
            }
        }
    }

    final String[] getResourceStringArray(int id) {
        return getArrayStringResource(id);
    }

    final boolean getResourceValue(int ident, int density, TypedValue outValue, boolean resolveRefs) {
        int block = loadResourceValue(ident, (short) density, outValue, resolveRefs);
        if (block < 0) {
            return false;
        }
        if (outValue.type != 3) {
            return true;
        }
        try {
            outValue.string = getEnsuredStringBlock(block).get(outValue.data);
            return true;
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "### Crash occurred ###");
            Log.e(TAG, "### while trying to find resource value #0x" + Integer.toHexString(ident) + " in StringBlock " + block + ", density = " + density + ", resolveRefs = " + resolveRefs);
            Log.e(TAG, "### " + outValue.toString());
            Log.e(TAG, "### outValue.density = " + outValue.density);
            Log.e(TAG, "### -------------- ###");
            throw e;
        }
    }

    private boolean isStringBlockExists(int block) {
        if (this.mStringBlocks == null) {
            return false;
        }
        try {
            if (this.mStringBlocks[block] != null) {
                return true;
            }
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private StringBlock getEnsuredStringBlock(int block) {
        if (!isStringBlockExists(block)) {
            synchronized (this.ensuringLock) {
                if (!isStringBlockExists(block)) {
                    synchronized (this) {
                        this.mStringBlocks = null;
                        ensureStringBlocks();
                    }
                }
            }
        }
        return this.mStringBlocks[block];
    }

    final CharSequence[] getResourceTextArray(int id) {
        int[] rawInfoArray = getArrayStringInfo(id);
        int rawInfoArrayLen = rawInfoArray.length;
        CharSequence[] retArray = new CharSequence[(rawInfoArrayLen / 2)];
        int i = 0;
        int j = 0;
        while (i < rawInfoArrayLen) {
            int block = rawInfoArray[i];
            int index = rawInfoArray[i + 1];
            retArray[j] = index >= 0 ? getEnsuredStringBlock(block).get(index) : null;
            i += 2;
            j++;
        }
        return retArray;
    }

    final boolean getThemeValue(long theme, int ident, TypedValue outValue, boolean resolveRefs) {
        int block = loadThemeAttributeValue(theme, ident, outValue, resolveRefs);
        if (block < 0) {
            return false;
        }
        if (outValue.type != 3) {
            return true;
        }
        outValue.string = getEnsuredStringBlock(block).get(outValue.data);
        return true;
    }

    final void ensureStringBlocks() {
        synchronized (this.ensuringLock) {
            if (this.mStringBlocks == null) {
                synchronized (this) {
                    if (this.mStringBlocks == null) {
                        makeStringBlocks(sSystem.mStringBlocks);
                    }
                }
            }
        }
    }

    final void makeStringBlocks(StringBlock[] seed) {
        int seedNum = seed != null ? seed.length : 0;
        int num = getStringBlockCount();
        this.mStringBlocks = new StringBlock[num];
        for (int i = 0; i < num; i++) {
            if (i < seedNum) {
                this.mStringBlocks[i] = seed[i];
            } else {
                this.mStringBlocks[i] = new StringBlock(getNativeStringBlock(i), true);
            }
        }
    }

    final CharSequence getPooledStringForCookie(int cookie, int id) {
        return getEnsuredStringBlock(cookie - 1).get(id);
    }

    public final InputStream open(String fileName) throws IOException {
        return open(fileName, 2);
    }

    public final InputStream open(String fileName, int accessMode) throws IOException {
        synchronized (this) {
            if (this.mOpen) {
                long asset = openAsset(fileName, accessMode);
                if (asset != 0) {
                    AssetInputStream res = new AssetInputStream(asset);
                    incRefsLocked((long) res.hashCode());
                    return res;
                }
                throw new FileNotFoundException("Asset file: " + fileName);
            }
            throw new RuntimeException("Assetmanager has been closed");
        }
    }

    public final AssetFileDescriptor openFd(String fileName) throws IOException {
        synchronized (this) {
            if (this.mOpen) {
                ParcelFileDescriptor pfd = openAssetFd(fileName, this.mOffsets);
                if (pfd != null) {
                    AssetFileDescriptor assetFileDescriptor = new AssetFileDescriptor(pfd, this.mOffsets[0], this.mOffsets[1]);
                    return assetFileDescriptor;
                }
                throw new FileNotFoundException("Asset file: " + fileName);
            }
            throw new RuntimeException("Assetmanager has been closed");
        }
    }

    public final InputStream openNonAsset(String fileName) throws IOException {
        return openNonAsset(0, fileName, 2);
    }

    public final InputStream openNonAsset(String fileName, int accessMode) throws IOException {
        return openNonAsset(0, fileName, accessMode);
    }

    public final InputStream openNonAsset(int cookie, String fileName) throws IOException {
        return openNonAsset(cookie, fileName, 2);
    }

    public final InputStream openNonAsset(int cookie, String fileName, int accessMode) throws IOException {
        synchronized (this) {
            if (this.mOpen) {
                long asset = openNonAssetNative(cookie, fileName, accessMode);
                if (asset != 0) {
                    AssetInputStream res = new AssetInputStream(asset);
                    incRefsLocked((long) res.hashCode());
                    return res;
                }
                throw new FileNotFoundException("Asset absolute file: " + fileName);
            }
            throw new RuntimeException("Assetmanager has been closed");
        }
    }

    public final AssetFileDescriptor openNonAssetFd(String fileName) throws IOException {
        return openNonAssetFd(0, fileName);
    }

    public final AssetFileDescriptor openNonAssetFd(int cookie, String fileName) throws IOException {
        synchronized (this) {
            if (this.mOpen) {
                ParcelFileDescriptor pfd = openNonAssetFdNative(cookie, fileName, this.mOffsets);
                if (pfd != null) {
                    AssetFileDescriptor assetFileDescriptor = new AssetFileDescriptor(pfd, this.mOffsets[0], this.mOffsets[1]);
                    return assetFileDescriptor;
                }
                throw new FileNotFoundException("Asset absolute file: " + fileName);
            }
            throw new RuntimeException("Assetmanager has been closed");
        }
    }

    public final XmlResourceParser openXmlResourceParser(String fileName) throws IOException {
        return openXmlResourceParser(0, fileName);
    }

    public final XmlResourceParser openXmlResourceParser(int cookie, String fileName) throws IOException {
        XmlBlock block = openXmlBlockAsset(cookie, fileName);
        XmlResourceParser rp = block.newParser();
        block.close();
        return rp;
    }

    final XmlBlock openXmlBlockAsset(String fileName) throws IOException {
        return openXmlBlockAsset(0, fileName);
    }

    final XmlBlock openXmlBlockAsset(int cookie, String fileName) throws IOException {
        synchronized (this) {
            if (this.mOpen) {
                int mapOffset = 0;
                if (this.elasticAppCookieOffset.get(Integer.valueOf(cookie)) != null) {
                    mapOffset = ((Integer) this.elasticAppCookieOffset.get(Integer.valueOf(cookie))).intValue();
                }
                long xmlBlock = openXmlAssetNative(cookie, fileName, mapOffset);
                if (xmlBlock != 0) {
                    XmlBlock res = new XmlBlock(this, xmlBlock);
                    incRefsLocked((long) res.hashCode());
                    return res;
                }
                throw new FileNotFoundException("Asset XML file: " + fileName);
            }
            throw new RuntimeException("Assetmanager has been closed");
        }
    }

    void xmlBlockGone(int id) {
        synchronized (this) {
            decRefsLocked((long) id);
        }
    }

    final long createTheme() {
        long res;
        synchronized (this) {
            if (this.mOpen) {
                res = newTheme();
                incRefsLocked(res);
            } else {
                throw new RuntimeException("Assetmanager has been closed");
            }
        }
        return res;
    }

    final void releaseTheme(long theme) {
        synchronized (this) {
            deleteTheme(theme);
            decRefsLocked(theme);
        }
    }

    protected void finalize() throws Throwable {
        try {
            destroy();
        } finally {
            super.finalize();
        }
    }

    public final int addAssetPath(String path) {
        return addAssetPath(path, 0);
    }

    public final int addAssetPath(String path, int mapOffset) {
        int res;
        synchronized (this.ensuringLock) {
            synchronized (this) {
                res = addAssetPathNative(path, mapOffset);
                this.elasticAppCookieOffset.put(Integer.valueOf(res), Integer.valueOf(mapOffset));
                makeStringBlocks(this.mStringBlocks);
            }
        }
        return res;
    }

    public final int addOverlayPath(String idmapPath) {
        int res;
        synchronized (this.ensuringLock) {
            synchronized (this) {
                res = -1;
                if (!this.mOverlays.contains(idmapPath)) {
                    this.mOverlays.add(idmapPath);
                    res = addOverlayPathNative(idmapPath);
                    makeStringBlocks(this.mStringBlocks);
                }
            }
        }
        return res;
    }

    public ArrayList<String> getOverlays() {
        return this.mOverlays;
    }

    public final int removeOverlayPath(String idmapPath) {
        int res;
        synchronized (this.ensuringLock) {
            synchronized (this) {
                res = removeOverlayPathNative(idmapPath);
                makeStringBlocks(this.mStringBlocks);
                this.mOverlays.clear();
            }
        }
        return res;
    }

    public final int[] addAssetPaths(String[] paths) {
        if (paths == null) {
            return null;
        }
        int[] cookies = new int[paths.length];
        for (int i = 0; i < paths.length; i++) {
            cookies[i] = addAssetPath(paths[i]);
        }
        return cookies;
    }

    public final void setConfiguration(int mcc, int mnc, String locale, int orientation, int touchscreen, int density, int keyboard, int keyboardHidden, int navigation, int screenWidth, int screenHeight, int smallestScreenWidthDp, int screenWidthDp, int screenHeightDp, int screenLayout, int uiMode, int majorVersion) {
        setConfiguration_(mcc, mnc, locale, orientation, touchscreen, density, keyboard, keyboardHidden, navigation, screenWidth, screenHeight, smallestScreenWidthDp, screenWidthDp, screenHeightDp, screenLayout, uiMode, majorVersion);
    }

    private final void incRefsLocked(long id) {
        this.mNumRefs++;
    }

    private final void decRefsLocked(long id) {
        this.mNumRefs--;
        if (this.mNumRefs == 0) {
            destroy();
        }
    }

    public int getOverlayID(int resID) {
        return getOverlayIDNative(resID);
    }
}
