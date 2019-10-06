package com.sec.android.app.hwmoduletest;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import java.io.File;

public class CameraImageView extends BaseActivity {
    private static Bitmap mRotatedBitmap;
    private static Bitmap vgaDispBitmap;
    private boolean Isfrontcam = false;
    private int SCREEN_HEIGHT;
    private int SCREEN_WIDTH;
    private ImageView image;

    public CameraImageView() {
        super("CameraImageView");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.camera_image_view);
        this.image = (ImageView) findViewById(C0268R.C0269id.camera_image_preview);
        String filePath = getIntent().getStringExtra("bg_filepath");
        if (filePath == null) {
            LtUtil.log_e(this.CLASS_NAME, "onCreate", "File path is null");
            finish();
        }
        Display display = ((WindowManager) getSystemService("window")).getDefaultDisplay();
        Point outpoint = new Point();
        display.getRealSize(outpoint);
        this.SCREEN_WIDTH = outpoint.x;
        this.SCREEN_HEIGHT = outpoint.y;
        StringBuilder sb = new StringBuilder();
        sb.append("SCREEN_WIDTH X SCREEN_HEIGHT = ");
        sb.append(this.SCREEN_WIDTH);
        sb.append(" X ");
        sb.append(this.SCREEN_HEIGHT);
        LtUtil.log_d(this.CLASS_NAME, "onCreate", sb.toString());
        Bundle myExtras = getIntent().getExtras();
        if (myExtras != null) {
            this.Isfrontcam = myExtras.getBoolean("frontcam", false);
        }
        Bitmap bgPic = getBitmapFromFile(filePath);
        if (bgPic != null) {
            mRotatedBitmap = rotateBitmap(bgPic);
            vgaDispBitmap = FlipVerticalBitmap(mRotatedBitmap);
            if (this.Isfrontcam) {
                LtUtil.log_d(this.CLASS_NAME, "onCreate", "Front Camera");
                this.image.setImageDrawable(new BitmapDrawable(getResources(), vgaDispBitmap));
            } else {
                LtUtil.log_d(this.CLASS_NAME, "onCreate", "Mega Camera");
                this.image.setImageDrawable(new BitmapDrawable(getResources(), mRotatedBitmap));
            }
        }
        deleteCameraFile(filePath);
    }

    public boolean onTouchEvent(MotionEvent event) {
        setResult(-1);
        finish();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (vgaDispBitmap != null) {
            LtUtil.log_i(this.CLASS_NAME, "onDestroy", "Remove vgaDispBitmap");
            vgaDispBitmap = null;
        }
        if (mRotatedBitmap != null) {
            LtUtil.log_i(this.CLASS_NAME, "onDestroy", "Remove mRotatedBitmap");
            mRotatedBitmap = null;
        }
    }

    private Bitmap getBitmapFromFile(String path) {
        if (path == null) {
            LtUtil.log_e(this.CLASS_NAME, "getBitmapFromFile", "path=null");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("path=");
        sb.append(path);
        LtUtil.log_d(this.CLASS_NAME, "getBitmapFromFile", sb.toString());
        if (!new File(path).exists()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Filt is not exist. / ");
            sb2.append(path);
            LtUtil.log_e(this.CLASS_NAME, "getBitmapFromFile", sb2.toString());
            return null;
        }
        Options options = new Options();
        options.inPreferredConfig = Config.ARGB_8888;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("options.outWidth=");
        sb3.append(options.outWidth);
        sb3.append(" , options.outHeight=");
        sb3.append(options.outHeight);
        LtUtil.log_d(this.CLASS_NAME, "getBitmapFromFile", sb3.toString());
        int widthScale = options.outWidth / this.SCREEN_WIDTH;
        int heightScale = options.outHeight / this.SCREEN_HEIGHT;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("widthScale=");
        sb4.append(widthScale);
        sb4.append(" , heightScale=");
        sb4.append(heightScale);
        LtUtil.log_d(this.CLASS_NAME, "getBitmapFromFile", sb4.toString());
        int scale = widthScale > heightScale ? widthScale : heightScale;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("scale=");
        sb5.append(scale);
        LtUtil.log_d(this.CLASS_NAME, "getBitmapFromFile", sb5.toString());
        options.inSampleSize = calculateSampleSize(scale);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private int calculateSampleSize(int scale) {
        int scale2;
        if (scale >= 2) {
            scale2 = (scale / 2) * 2;
        } else {
            scale2 = 1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("scale = ");
        sb.append(scale2);
        LtUtil.log_d(this.CLASS_NAME, "calculateSampleSize", sb.toString());
        return scale2;
    }

    private void deleteCameraFile(String path) {
        boolean result;
        if (path == null) {
            LtUtil.log_e(this.CLASS_NAME, "deleteCameraFile", "path=null");
            return;
        }
        File delFile = new File(path);
        if (delFile.exists()) {
            result = delFile.delete();
        } else {
            result = true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Delete result=");
        sb.append(result);
        LtUtil.log_d(this.CLASS_NAME, "deleteCameraFile", sb.toString());
    }

    private Bitmap rotateBitmap(Bitmap originalBitmap) {
        if (originalBitmap == null) {
            LtUtil.log_e(this.CLASS_NAME, "rotateBitmap", "originalBitmap is null");
            return null;
        }
        Matrix matrix = new Matrix();
        if (this.Isfrontcam) {
            matrix.postRotate(270.0f);
        } else {
            matrix.postRotate(90.0f);
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        StringBuilder sb = new StringBuilder();
        sb.append("width=");
        sb.append(rotatedBitmap.getWidth());
        sb.append(", height=");
        sb.append(rotatedBitmap.getHeight());
        LtUtil.log_i(this.CLASS_NAME, "rotateBitmap", sb.toString());
        return rotatedBitmap;
    }

    private Bitmap FlipVerticalBitmap(Bitmap originalBitmap) {
        if (originalBitmap == null) {
            LtUtil.log_e(this.CLASS_NAME, "FlipVerticalBitmap", "originalBitmap is null");
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
    }
}
