package android.view;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemProperties;
import android.util.DisplayMetrics;
import android.util.Log;

public abstract class OrientationEventListener {
    private static final boolean DEBUG = false;
    public static final int ORIENTATION_UNKNOWN = -1;
    private static final String TAG = "OrientationEventListener";
    private static final boolean localLOGV = false;
    private static int mRotationMode = 0;
    private float fSumRotateAngle_Y;
    private float fSumRotateAngle_Z;
    private boolean mAccStuckCheckMode;
    private boolean mDataIndex;
    private boolean mEnabled;
    private boolean mEnabledGyro;
    private Sensor mGyro;
    private OrientationListener mOldListener;
    private int mOrientation;
    private int mPreOrientation;
    private float[] mPredata;
    private int mRate;
    private Sensor mSensor;
    private SensorEventListener mSensorEventListener;
    private SensorManager mSensorManager;
    private int mX_StuckCnt;
    private int mY_StuckCnt;
    private int mZ_StuckCnt;
    private int m_StableCnt;
    private int m_StcukDurationTHD;

    class SensorEventListenerImpl implements SensorEventListener {
        private static final float OneEightyOverPi = 57.29578f;
        private static final double STANDARD_GRAVITY = 9.80665d;
        private static final float StuckCheckTHDValue = 14.4f;
        private static final int _DATA_X = 0;
        private static final int _DATA_Y = 1;
        private static final int _DATA_Z = 2;

        SensorEventListenerImpl() {
        }

        public void onSensorChanged(SensorEvent event) {
            int orientation;
            float X;
            float Y;
            float Z;
            if (OrientationEventListener.this.mAccStuckCheckMode) {
                switch (event.sensor.getType()) {
                    case 1:
                        float[] values = event.values;
                        orientation = -1;
                        X = -values[0];
                        Y = -values[1];
                        Z = -values[2];
                        if (OrientationEventListener.mRotationMode == 0) {
                            if (Math.abs(X) > StuckCheckTHDValue) {
                                OrientationEventListener.this.mX_StuckCnt = OrientationEventListener.this.mX_StuckCnt + 1;
                            } else {
                                OrientationEventListener.this.mX_StuckCnt = 0;
                            }
                            if (Math.abs(Y) > StuckCheckTHDValue) {
                                OrientationEventListener.this.mY_StuckCnt = OrientationEventListener.this.mY_StuckCnt + 1;
                            } else {
                                OrientationEventListener.this.mY_StuckCnt = 0;
                            }
                            if (Math.abs(Z) > StuckCheckTHDValue) {
                                OrientationEventListener.this.mZ_StuckCnt = OrientationEventListener.this.mZ_StuckCnt + 1;
                            } else {
                                OrientationEventListener.this.mZ_StuckCnt = 0;
                            }
                            if (OrientationEventListener.this.mX_StuckCnt > OrientationEventListener.this.m_StcukDurationTHD) {
                                OrientationEventListener.mRotationMode = 1;
                            }
                            if (OrientationEventListener.this.mY_StuckCnt > OrientationEventListener.this.m_StcukDurationTHD) {
                                OrientationEventListener.mRotationMode = 2;
                            }
                            if (OrientationEventListener.this.mZ_StuckCnt > OrientationEventListener.this.m_StcukDurationTHD) {
                                OrientationEventListener.mRotationMode = 3;
                            }
                        } else {
                            if (Math.abs(X) > StuckCheckTHDValue || Math.abs(Y) > StuckCheckTHDValue || Math.abs(Z) > StuckCheckTHDValue) {
                                OrientationEventListener.this.m_StableCnt = 0;
                            } else {
                                OrientationEventListener.this.m_StableCnt = OrientationEventListener.this.m_StableCnt + 1;
                            }
                            if (OrientationEventListener.this.m_StableCnt > 0) {
                                OrientationEventListener.mRotationMode = 0;
                                OrientationEventListener.this.m_StableCnt = 0;
                                OrientationEventListener.this.mX_StuckCnt = 0;
                                OrientationEventListener.this.mY_StuckCnt = 0;
                                OrientationEventListener.this.mZ_StuckCnt = 0;
                                if (OrientationEventListener.this.mEnabledGyro) {
                                    OrientationEventListener.this.mSensorManager.unregisterListener(this, OrientationEventListener.this.mGyro);
                                    OrientationEventListener.this.mEnabledGyro = false;
                                }
                            }
                        }
                        if (OrientationEventListener.mRotationMode > 0 && !OrientationEventListener.this.mEnabledGyro) {
                            OrientationEventListener.this.mSensorManager.registerListener(this, OrientationEventListener.this.mGyro, 1);
                            OrientationEventListener.this.mEnabledGyro = true;
                        }
                        double tmp;
                        switch (OrientationEventListener.mRotationMode) {
                            case 1:
                                tmp = (96.17038422249999d - ((double) (Y * Y))) - ((double) (Z * Z));
                                if (tmp <= 0.0d) {
                                    X = 0.0f;
                                    break;
                                } else {
                                    X = -((float) Math.sqrt(tmp));
                                    break;
                                }
                            case 2:
                                tmp = (96.17038422249999d - ((double) (X * X))) - ((double) (Z * Z));
                                if (tmp <= 0.0d) {
                                    Y = 0.0f;
                                    break;
                                } else {
                                    Y = -((float) Math.sqrt(tmp));
                                    break;
                                }
                            case 3:
                                tmp = (96.17038422249999d - ((double) (X * X))) - ((double) (Y * Y));
                                if (tmp <= 0.0d) {
                                    Z = 0.0f;
                                    break;
                                } else {
                                    Z = -((float) Math.sqrt(tmp));
                                    break;
                                }
                        }
                        float magnitude = (X * X) + (Y * Y);
                        if (OrientationEventListener.mRotationMode != 0) {
                            if (OrientationEventListener.this.mDataIndex) {
                                X = (OrientationEventListener.this.mPredata[0] + X) / 2.0f;
                                Y = (OrientationEventListener.this.mPredata[1] + Y) / 2.0f;
                                Z = (OrientationEventListener.this.mPredata[2] + Z) / 2.0f;
                                OrientationEventListener.this.mDataIndex = false;
                            } else {
                                OrientationEventListener.this.mPredata[0] = X;
                                OrientationEventListener.this.mPredata[1] = Y;
                                OrientationEventListener.this.mPredata[2] = Z;
                                OrientationEventListener.this.mDataIndex = true;
                            }
                            if (OrientationEventListener.this.mDataIndex) {
                                orientation = OrientationEventListener.this.mOrientation;
                            } else {
                                if (Math.abs(((float) Math.asin(((double) Z) / Math.sqrt((double) (((X * X) + (Y * Y)) + (Z * Z))))) * OneEightyOverPi) < 80.0f) {
                                    float OrientationAngle = 90.0f - (((float) Math.atan2((double) (-Y), (double) X)) * OneEightyOverPi);
                                    while (OrientationAngle >= 360.0f) {
                                        OrientationAngle -= 360.0f;
                                    }
                                    while (OrientationAngle < 0.0f) {
                                        OrientationAngle += 360.0f;
                                    }
                                    int nearestRotation = (Math.round(OrientationAngle) + 45) / 90;
                                    if (nearestRotation == 4) {
                                        nearestRotation = 0;
                                    }
                                    orientation = nearestRotation * 90;
                                }
                            }
                        } else if (4.0f * magnitude >= Z * Z) {
                            orientation = 90 - Math.round(((float) Math.atan2((double) (-Y), (double) X)) * OneEightyOverPi);
                            while (orientation >= 360) {
                                orientation -= 360;
                            }
                            while (orientation < 0) {
                                orientation += DisplayMetrics.DENSITY_360;
                            }
                        }
                        if (OrientationEventListener.this.mOldListener != null) {
                            OrientationEventListener.this.mOldListener.onSensorChanged(1, event.values);
                        }
                        if (orientation != OrientationEventListener.this.mOrientation) {
                            OrientationEventListener.this.mOrientation = orientation;
                            if (OrientationEventListener.mRotationMode == 1 && orientation == 270) {
                                if (OrientationEventListener.this.mPreOrientation == -1) {
                                    if (OrientationEventListener.this.fSumRotateAngle_Y > 5.0f) {
                                        orientation = 90;
                                    }
                                } else if (OrientationEventListener.this.mPreOrientation == 0) {
                                    if (OrientationEventListener.this.fSumRotateAngle_Z < -5.0f) {
                                        orientation = 90;
                                    }
                                } else if (OrientationEventListener.this.mPreOrientation == 180 && OrientationEventListener.this.fSumRotateAngle_Z > 5.0f) {
                                    orientation = 90;
                                }
                            } else if (OrientationEventListener.mRotationMode == 1 && OrientationEventListener.this.mPreOrientation == 0 && orientation == 180 && OrientationEventListener.this.fSumRotateAngle_Z > -150.0f) {
                                orientation = 90;
                            }
                            OrientationEventListener.this.mPreOrientation = orientation;
                            OrientationEventListener.this.fSumRotateAngle_Y = 0.0f;
                            OrientationEventListener.this.fSumRotateAngle_Z = 0.0f;
                            OrientationEventListener.this.onOrientationChanged(orientation);
                            return;
                        } else if (orientation != OrientationEventListener.this.mOrientation) {
                            return;
                        } else {
                            if ((OrientationEventListener.this.mOrientation == 90 || OrientationEventListener.this.mOrientation == 270) && Math.abs(OrientationEventListener.this.fSumRotateAngle_Z) > 150.0f) {
                                orientation = (((OrientationEventListener.this.mPreOrientation / 90) + 2) % 4) * 90;
                                OrientationEventListener.this.mPreOrientation = orientation;
                                OrientationEventListener.this.fSumRotateAngle_Y = 0.0f;
                                OrientationEventListener.this.fSumRotateAngle_Z = 0.0f;
                                OrientationEventListener.this.onOrientationChanged(orientation);
                                return;
                            }
                            return;
                        }
                    case 4:
                        OrientationEventListener.access$1518(OrientationEventListener.this, (((double) event.values[1]) * 0.02d) * 57.295780181884766d);
                        OrientationEventListener.access$1618(OrientationEventListener.this, (((double) event.values[2]) * 0.02d) * 57.295780181884766d);
                        return;
                    default:
                        return;
                }
            }
            values = event.values;
            orientation = -1;
            X = -values[0];
            Y = -values[1];
            Z = -values[2];
            if (4.0f * ((X * X) + (Y * Y)) >= Z * Z) {
                orientation = 90 - Math.round(((float) Math.atan2((double) (-Y), (double) X)) * OneEightyOverPi);
                while (orientation >= 360) {
                    orientation -= 360;
                }
                while (orientation < 0) {
                    orientation += DisplayMetrics.DENSITY_360;
                }
            }
            if (OrientationEventListener.this.mOldListener != null) {
                OrientationEventListener.this.mOldListener.onSensorChanged(1, event.values);
            }
            if (orientation != OrientationEventListener.this.mOrientation) {
                OrientationEventListener.this.mOrientation = orientation;
                OrientationEventListener.this.onOrientationChanged(orientation);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    public abstract void onOrientationChanged(int i);

    static /* synthetic */ float access$1518(OrientationEventListener x0, double x1) {
        float f = (float) (((double) x0.fSumRotateAngle_Y) + x1);
        x0.fSumRotateAngle_Y = f;
        return f;
    }

    static /* synthetic */ float access$1618(OrientationEventListener x0, double x1) {
        float f = (float) (((double) x0.fSumRotateAngle_Z) + x1);
        x0.fSumRotateAngle_Z = f;
        return f;
    }

    public OrientationEventListener(Context context) {
        this(context, 3);
    }

    public OrientationEventListener(Context context, int rate) {
        this.mOrientation = -1;
        this.mPreOrientation = -1;
        this.mEnabled = false;
        this.mEnabledGyro = false;
        this.mAccStuckCheckMode = false;
        this.mX_StuckCnt = 0;
        this.mY_StuckCnt = 0;
        this.mZ_StuckCnt = 0;
        this.m_StableCnt = 0;
        this.fSumRotateAngle_Y = 0.0f;
        this.fSumRotateAngle_Z = 0.0f;
        this.mDataIndex = false;
        this.mPredata = new float[3];
        String DEVICE_NAME = SystemProperties.get("ro.product.name");
        if (DEVICE_NAME == null) {
            this.mAccStuckCheckMode = false;
        } else if (DEVICE_NAME.contains("zerolte") || DEVICE_NAME.contains("zeroflte")) {
            this.mAccStuckCheckMode = true;
            Log.d(TAG, "mAccStuckCheckMode" + this.mAccStuckCheckMode);
        }
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        this.mRate = rate;
        this.mSensor = this.mSensorManager.getDefaultSensor(1);
        if (this.mSensor != null) {
            this.mSensorEventListener = new SensorEventListenerImpl();
        }
        if (this.mAccStuckCheckMode) {
            this.mGyro = this.mSensorManager.getDefaultSensor(4);
            switch (this.mRate) {
                case 0:
                    this.m_StcukDurationTHD = 144;
                    break;
                case 1:
                    this.m_StcukDurationTHD = 36;
                    break;
                case 2:
                    this.m_StcukDurationTHD = 12;
                    break;
                case 3:
                    this.m_StcukDurationTHD = 6;
                    break;
                default:
                    this.m_StcukDurationTHD = 10;
                    break;
            }
            Log.d(TAG, "mRate" + this.mRate + this.m_StcukDurationTHD);
        }
    }

    void registerListener(OrientationListener lis) {
        this.mOldListener = lis;
    }

    public void enable() {
        if (this.mSensor == null) {
            Log.w(TAG, "Cannot detect sensors. Not enabled");
        } else if (!this.mEnabled) {
            this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensor, this.mRate);
            this.mEnabled = true;
        }
    }

    public void disable() {
        if (this.mSensor == null) {
            Log.w(TAG, "Cannot detect sensors. Invalid disable");
            return;
        }
        if (this.mEnabled) {
            this.mSensorManager.unregisterListener(this.mSensorEventListener);
            this.mEnabled = false;
        }
        if (this.mAccStuckCheckMode) {
            mRotationMode = 0;
            this.mX_StuckCnt = 0;
            this.mY_StuckCnt = 0;
            this.mZ_StuckCnt = 0;
        }
    }

    public boolean canDetectOrientation() {
        return this.mSensor != null;
    }
}
