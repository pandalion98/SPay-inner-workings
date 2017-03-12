package android.util;

import android.graphics.Matrix;
import android.os.SystemProperties;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class ResolutionOverride {
    private static final boolean DEBUG = false;
    private static final String RES_OVERRIDE = "persist.debug.app_res_override";
    private static final String TAG = "ResolutionOverride";
    private boolean mIsEnabled = false;
    private int mOverrideXres = 0;
    private int mOverrideYres = 0;

    public ResolutionOverride(SurfaceView view) {
        boolean enable = false;
        if (view.getContext().getApplicationInfo().canOverrideRes() == 1) {
            enable = true;
        }
        int orientation = view.getResources().getConfiguration().orientation;
        if (!enable) {
            return;
        }
        if (orientation == 1 || orientation == 2) {
            String resStr = SystemProperties.get(RES_OVERRIDE, null);
            if (resStr != null && resStr.length() > 0) {
                resStr = resStr.toLowerCase();
                int pos = resStr.indexOf(120);
                if (pos > 0 && resStr.lastIndexOf(120) == pos) {
                    try {
                        this.mOverrideXres = Integer.parseInt(resStr.substring(0, pos));
                        this.mOverrideYres = Integer.parseInt(resStr.substring(pos + 1));
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Error in extracting the overriding xres and yres");
                    }
                }
            }
            if (orientation == 2) {
                int tmp = this.mOverrideXres;
                this.mOverrideXres = this.mOverrideYres;
                this.mOverrideYres = tmp;
            }
            if (this.mOverrideXres > 0 && this.mOverrideYres > 0) {
                this.mIsEnabled = true;
            }
        }
    }

    public void setFixedSize(SurfaceView view) {
        if (this.mIsEnabled) {
            view.getHolder().setFixedSize(this.mOverrideXres, this.mOverrideYres);
        }
    }

    public void handleTouch(SurfaceView view, MotionEvent ev) {
        if (this.mIsEnabled) {
            Matrix matrix = new Matrix();
            matrix.postScale((((float) this.mOverrideXres) * 1.0f) / ((float) view.getWidth()), (((float) this.mOverrideYres) * 1.0f) / ((float) view.getHeight()));
            ev.transform(matrix);
        }
    }

    public void handleResize(final SurfaceView surfaceView) {
        if (this.mIsEnabled) {
            surfaceView.post(new Runnable() {
                public void run() {
                    surfaceView.setVisibility(8);
                }
            });
            surfaceView.postDelayed(new Runnable() {
                public void run() {
                    surfaceView.setVisibility(0);
                }
            }, 100);
        }
    }
}
