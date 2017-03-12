package android.app.wallpaperbackup;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ProxyInfo;
import android.net.TrafficStats;
import android.os.StatFs;
import android.os.SystemProperties;
import com.samsung.android.telephony.MultiSimManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class Controller {
    public static String ERROR_KEY = "ERR_CODE";
    private static final String IMAGE_FILE_NAME = "wallpaper.png";
    private static final String IMAGE_FILE_NAME_SIM2 = "wallpaper2.png";
    public static final String REQUEST_BACKUP_WALLPAPER = "android.intent.action.REQUEST_BACKUP_WALLPAPER";
    public static final String REQUEST_RESTORE_WALLPAPER = "android.intent.action.REQUEST_RESTORE_WALLPAPER";
    private static long REQUIRED_SIZE = 10485760;
    public static String REQUIRED_SIZE_KEY = "REQ_SIZE";
    public static final String RESPONSE_BACKUP_WALLPAPER = "android.intent.action.RESPONSE_BACKUP_WALLPAPER";
    public static final String RESPONSE_RESTORE_WALLPAPER = "android.intent.action.RESPONSE_RESTORE_WALLPAPER";
    public static String RESULT_KEY = "RESULT";
    public static String SAVE_PATH = "SAVE_PATH";
    public static String SOURCE_KEY = "SOURCE";
    public static final String TAG = "XMl_BNR_APK_REV";
    private static final String WALLPAPER_PATH = "wallpaper";
    private String mComponent = ProxyInfo.LOCAL_EXCL_LIST;
    private ERR_CODE mErrorCode = ERR_CODE.UNKNOWN_ERROR;
    private int mHeight;
    private REQ_SIZE mRequiredSize = REQ_SIZE.MINIMUM_SIZE;
    private RESULT mResult = RESULT.FAIL;
    private int mWidth;
    private String wallpaperImagePath = ProxyInfo.LOCAL_EXCL_LIST;
    private String wallpaperUserFileName = ProxyInfo.LOCAL_EXCL_LIST;

    public enum ERR_CODE {
        SUCCESS(0),
        UNKNOWN_ERROR(1),
        STORAGE_FULL(2),
        INVALID_DATA(3);
        
        private int value;

        private ERR_CODE(int num) {
            this.value = 0;
            this.value = num;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum REQ_SIZE {
        SUCCESS(0),
        MINIMUM_SIZE(10485760);
        
        private int value;

        private REQ_SIZE(int num) {
            this.value = 0;
            this.value = num;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum RESULT {
        OK(0),
        FAIL(1);
        
        private int value;

        private RESULT(int num) {
            this.value = 0;
            this.value = num;
        }

        public int getValue() {
            return this.value;
        }
    }

    public void pushRestoreFiles(android.content.Context r16, java.lang.String r17) throws java.lang.Exception {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(Unknown Source)
	at java.util.HashMap$KeyIterator.next(Unknown Source)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:80)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r15 = this;
        r13 = r15.isWallpaperUserFileExists();
        if (r13 == 0) goto L_0x00bd;
    L_0x0006:
        r3 = new android.app.wallpaperbackup.LiveChange;
        r0 = r16;
        r3.<init>(r0);
        r12 = new android.app.wallpaperbackup.WallpaperXmlParser;
        r13 = r15.wallpaperUserFileName;
        r0 = r16;
        r12.<init>(r0, r13);
        r4 = r12.getObject();
        r11 = android.app.WallpaperManager.getInstance(r16);
        r8 = 0;
        r9 = 0;
        r8 = new java.io.FileInputStream;
        r0 = r17;
        r13 = r15.getRestoreWallpaperImageFilesPath(r0, r4);
        r8.<init>(r13);
        if (r8 == 0) goto L_0x00b5;
    L_0x002d:
        r13 = com.samsung.android.telephony.MultiSimManager.getSimSlotCount();	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r14 = 1;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        if (r13 <= r14) goto L_0x00a7;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
    L_0x0034:
        r13 = "gsm.sim.state";	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r7 = android.os.SystemProperties.get(r13);	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r13 = ",";	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r6 = r7.split(r13);	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r13 = "ril.MSIMM";	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r5 = android.os.SystemProperties.get(r13);	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r13 = 0;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r13 = r6[r13];	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r14 = "ABSENT";	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r13 = r13.equals(r14);	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        if (r13 != 0) goto L_0x005e;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
    L_0x0053:
        r13 = 1;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r13 = r6[r13];	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r14 = "ABSENT";	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r13 = r13.equals(r14);	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        if (r13 == 0) goto L_0x0067;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
    L_0x005e:
        r13 = "1";	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r13 = r13.equals(r5);	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r14 = 1;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        if (r13 != r14) goto L_0x0099;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
    L_0x0067:
        r13 = 0;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r11.setStream(r8, r13);	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r0 = r17;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r2 = r15.getBackupWallpaperImageFilesPathSim2(r0);	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r13 = r15.hasFile(r2);	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        if (r13 == 0) goto L_0x008c;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
    L_0x0077:
        r10 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r10.<init>(r2);	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r9 = r10;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
    L_0x007d:
        if (r9 == 0) goto L_0x0083;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
    L_0x007f:
        r13 = 1;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r11.setStream(r9, r13);	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
    L_0x0083:
        r8.close();
        if (r9 == 0) goto L_0x008b;
    L_0x0088:
        r9.close();
    L_0x008b:
        return;
    L_0x008c:
        r10 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r0 = r17;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r13 = r15.getRestoreWallpaperImageFilesPath(r0, r4);	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r10.<init>(r13);	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        r9 = r10;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        goto L_0x007d;	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
    L_0x0099:
        r11.setStream(r8);	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        goto L_0x0083;
    L_0x009d:
        r13 = move-exception;
        r8.close();
        if (r9 == 0) goto L_0x008b;
    L_0x00a3:
        r9.close();
        goto L_0x008b;
    L_0x00a7:
        r11.setStream(r8);	 Catch:{ Exception -> 0x009d, all -> 0x00ab }
        goto L_0x0083;
    L_0x00ab:
        r13 = move-exception;
        r8.close();
        if (r9 == 0) goto L_0x00b4;
    L_0x00b1:
        r9.close();
    L_0x00b4:
        throw r13;
    L_0x00b5:
        r1 = r4.getComponent();
        r3.set(r1);
        goto L_0x008b;
    L_0x00bd:
        r13 = new java.lang.Exception;
        r13.<init>();
        throw r13;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.wallpaperbackup.Controller.pushRestoreFiles(android.content.Context, java.lang.String):void");
    }

    public void startBackup(Context context, String basePath, String source) {
        long freeSpaceInBytes = TrafficStats.GB_IN_BYTES;
        try {
            StatFs stat = new StatFs(basePath);
            freeSpaceInBytes = ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
        basePath = basePath + "/";
        try {
            if (freeSpaceInBytes < REQUIRED_SIZE) {
                this.mResult = RESULT.FAIL;
                this.mErrorCode = ERR_CODE.STORAGE_FULL;
                this.mRequiredSize = REQ_SIZE.MINIMUM_SIZE;
            } else {
                boolean result;
                if (MultiSimManager.getSimSlotCount() > 1) {
                    result = createBackupFilesForMultiSim(context, basePath);
                } else {
                    result = createBackupFiles(context, basePath);
                }
                if (result) {
                    this.mResult = RESULT.OK;
                    this.mErrorCode = ERR_CODE.SUCCESS;
                } else {
                    this.mResult = RESULT.FAIL;
                    this.mErrorCode = ERR_CODE.INVALID_DATA;
                }
                this.mRequiredSize = REQ_SIZE.SUCCESS;
            }
        } catch (Exception e2) {
            this.mResult = RESULT.FAIL;
            this.mErrorCode = ERR_CODE.UNKNOWN_ERROR;
            this.mRequiredSize = REQ_SIZE.SUCCESS;
        }
        Intent intent = new Intent();
        intent.setAction(RESPONSE_BACKUP_WALLPAPER);
        intent.putExtra(RESULT_KEY, this.mResult.getValue());
        intent.putExtra(ERROR_KEY, this.mErrorCode.getValue());
        intent.putExtra(REQUIRED_SIZE_KEY, this.mRequiredSize.getValue());
        intent.putExtra(SOURCE_KEY, source);
        context.sendBroadcast(intent);
    }

    private void deleteFiles(String path) {
        try {
            File sd = new File(path);
            if (sd.exists()) {
                sd.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasFile(String path) {
        File file = new File(path);
        if (file == null || !file.isFile()) {
            return false;
        }
        return true;
    }

    private boolean createBackupFiles(Context context, String basePath) throws FileNotFoundException, IOException {
        this.wallpaperUserFileName = basePath + GenerateXMLForWallpaperUser.WALLPAPER_XML_NAME;
        String imageNameWithPath = getBackupWallpaperImageFilesPath(basePath);
        String imageName = getBackupWallpaperImageName();
        deleteFiles(this.wallpaperUserFileName);
        deleteFiles(imageNameWithPath);
        boolean wallpaperType = getWallpaperDimensions(context, imageNameWithPath);
        if (wallpaperType) {
            createBackupWallpaperXmlFiles(imageName, basePath);
        }
        return wallpaperType;
    }

    private boolean createBackupFilesForMultiSim(Context context, String basePath) throws FileNotFoundException, IOException {
        boolean wallpaperType;
        this.wallpaperUserFileName = basePath + GenerateXMLForWallpaperUser.WALLPAPER_XML_NAME;
        String imageNameWithPath = getBackupWallpaperImageFilesPath(basePath);
        String imageNameWithPathSim2 = getBackupWallpaperImageFilesPathSim2(basePath);
        String imageName = getBackupWallpaperImageName();
        deleteFiles(this.wallpaperUserFileName);
        deleteFiles(imageNameWithPath);
        deleteFiles(imageNameWithPathSim2);
        String[] simState = SystemProperties.get("gsm.sim.state").split(",");
        if (simState[0].equals("ABSENT") || simState[1].equals("ABSENT")) {
            wallpaperType = getWallpaperDimensions(context, imageNameWithPath);
        } else {
            wallpaperType = getWallpaperDimensionsForMultiSim(context, imageNameWithPath, 0);
            if (wallpaperType) {
                wallpaperType = getWallpaperDimensionsForMultiSim(context, imageNameWithPathSim2, 1);
            }
        }
        if (wallpaperType) {
            createBackupWallpaperXmlFiles(imageName, basePath);
        }
        return wallpaperType;
    }

    private String getBackupWallpaperImageName() {
        return "wallpaper/wallpaper.png";
    }

    public void startRestore(Context context, String basePath, String source) {
        basePath = basePath + "/";
        this.wallpaperUserFileName = basePath + GenerateXMLForWallpaperUser.WALLPAPER_XML_NAME;
        try {
            pushRestoreFiles(context, basePath);
            this.mResult = RESULT.OK;
            this.mErrorCode = ERR_CODE.SUCCESS;
            this.mRequiredSize = REQ_SIZE.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            this.mResult = RESULT.FAIL;
            this.mErrorCode = ERR_CODE.UNKNOWN_ERROR;
            this.mRequiredSize = REQ_SIZE.SUCCESS;
        }
        Intent intent = new Intent();
        intent.setAction(RESPONSE_RESTORE_WALLPAPER);
        intent.putExtra(RESULT_KEY, this.mResult.getValue());
        intent.putExtra(ERROR_KEY, this.mErrorCode.getValue());
        intent.putExtra(REQUIRED_SIZE_KEY, this.mRequiredSize.getValue());
        intent.putExtra(SOURCE_KEY, source);
        context.sendBroadcast(intent);
    }

    private boolean isWallpaperUserFileExists() {
        return new File(this.wallpaperUserFileName).exists();
    }

    private void createBackupWallpaperXmlFiles(String imageName, String basePath) throws IOException {
        ArrayList<WallpaperUserPOJO> userList = new ArrayList();
        userList.add(new WallpaperUserPOJO(this.mWidth, this.mHeight, imageName, this.mComponent, 101));
        GenerateXMLForWallpaperUser generateXMLForWallpaperUser = new GenerateXMLForWallpaperUser(userList, basePath);
    }

    private String getBackupWallpaperImageFilesPath(String basePath) {
        this.wallpaperImagePath = basePath + "wallpaper" + "/" + IMAGE_FILE_NAME;
        return this.wallpaperImagePath;
    }

    private String getBackupWallpaperImageFilesPathSim2(String basePath) {
        this.wallpaperImagePath = basePath + "wallpaper" + "/" + IMAGE_FILE_NAME_SIM2;
        return this.wallpaperImagePath;
    }

    private String getRestoreWallpaperImageFilesPath(String basePath, WallpaperUserPOJO recItem) {
        this.wallpaperImagePath = basePath + recItem.getName();
        return this.wallpaperImagePath;
    }

    public boolean getWallpaperDimensions(Context context, String imageNamePath) throws FileNotFoundException, IOException {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        Bitmap image = null;
        if (wallpaperDrawable != null) {
            image = drawableToBitmap(wallpaperDrawable);
        }
        if (wallpaperManager.getWallpaperInfo() != null) {
            return false;
        }
        if (image != null) {
            saveBitmap(imageNamePath, image);
            this.mWidth = image.getWidth();
            this.mHeight = image.getHeight();
        }
        return true;
    }

    public boolean getWallpaperDimensionsForMultiSim(Context context, String imageNamePath, int simSlot) throws FileNotFoundException, IOException {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        Bitmap image = wallpaperManager.getBitmapForMultiSim(simSlot);
        if (wallpaperManager.getWallpaperInfo() != null) {
            return false;
        }
        saveBitmap(imageNamePath, image);
        if (simSlot == 0) {
            this.mWidth = image.getWidth();
            this.mHeight = image.getHeight();
        }
        return true;
    }

    public static void saveBitmap(String nameOfImageAlongWithPath, Bitmap bmp) throws FileNotFoundException, IOException {
        File sd = new File(nameOfImageAlongWithPath);
        if (!sd.exists()) {
            sd.getParentFile().mkdirs();
            if (sd.exists()) {
                sd.delete();
            }
        }
        OutputStream fos = new FileOutputStream(sd);
        bmp.compress(CompressFormat.PNG, 100, fos);
        fos.close();
        fos.flush();
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
