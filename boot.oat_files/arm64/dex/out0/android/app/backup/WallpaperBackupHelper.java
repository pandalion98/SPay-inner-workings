package android.app.backup;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Slog;
import android.view.Display;
import android.view.WindowManager;
import java.io.File;

public class WallpaperBackupHelper extends FileBackupHelperBase implements BackupHelper {
    private static final boolean DEBUG = false;
    private static final double MAX_HEIGHT_RATIO = 1.35d;
    private static final double MIN_HEIGHT_RATIO = 0.0d;
    private static final boolean REJECT_OUTSIZED_RESTORE = true;
    private static final String STAGE_FILE = new File(Environment.getUserSystemDirectory(0), "wallpaper-tmp").getAbsolutePath();
    private static final String TAG = "WallpaperBackupHelper";
    public static final String WALLPAPER_IMAGE = new File(Environment.getUserSystemDirectory(0), Context.WALLPAPER_SERVICE).getAbsolutePath();
    public static final String WALLPAPER_IMAGE_KEY = "/data/data/com.android.settings/files/wallpaper";
    public static final String WALLPAPER_INFO = new File(Environment.getUserSystemDirectory(0), "wallpaper_info.xml").getAbsolutePath();
    public static final String WALLPAPER_INFO_KEY = "/data/system/wallpaper_info.xml";
    Context mContext;
    double mDesiredMinHeight;
    double mDesiredMinWidth;
    String[] mFiles;
    String[] mKeys;

    public /* bridge */ /* synthetic */ void writeNewStateDescription(ParcelFileDescriptor x0) {
        super.writeNewStateDescription(x0);
    }

    public WallpaperBackupHelper(Context context, String[] files, String[] keys) {
        super(context);
        this.mContext = context;
        this.mFiles = files;
        this.mKeys = keys;
        WallpaperManager wpm = (WallpaperManager) context.getSystemService(Context.WALLPAPER_SERVICE);
        Display d = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        d.getSize(size);
        this.mDesiredMinWidth = (double) Math.min(size.x, size.y);
        this.mDesiredMinHeight = (double) wpm.getDesiredMinimumHeight();
        if (this.mDesiredMinHeight <= 0.0d) {
            this.mDesiredMinHeight = (double) size.y;
        }
    }

    public void performBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) {
        FileBackupHelperBase.performBackup_checked(oldState, data, newState, this.mFiles, this.mKeys);
    }

    public void restoreEntity(BackupDataInputStream data) {
        String key = data.getKey();
        if (!isKeyInList(key, this.mKeys)) {
            return;
        }
        if (key.equals(WALLPAPER_IMAGE_KEY)) {
            File f = new File(STAGE_FILE);
            if (writeFile(f, data)) {
                Options options = new Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(STAGE_FILE, options);
                double heightRatio = this.mDesiredMinHeight / ((double) options.outHeight);
                if (((double) options.outWidth) < this.mDesiredMinWidth || ((double) options.outHeight) < this.mDesiredMinWidth || heightRatio >= MAX_HEIGHT_RATIO || heightRatio <= 0.0d) {
                    Slog.i(TAG, "Restored image dimensions (w=" + options.outWidth + ", h=" + options.outHeight + ") too far off target (tw=" + this.mDesiredMinWidth + ", th=" + this.mDesiredMinHeight + "); falling back to default wallpaper.");
                    f.delete();
                    return;
                }
                Slog.d(TAG, "Applying restored wallpaper image.");
                f.renameTo(new File(WALLPAPER_IMAGE));
            }
        } else if (key.equals(WALLPAPER_INFO_KEY)) {
            writeFile(new File(WALLPAPER_INFO), data);
        }
    }
}
