package android.os;

import android.bluetooth.BluetoothClass.Device;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PersonaInfo;
import android.net.ProxyInfo;
import android.os.CustomFrequencyManager.FrequencyRequest;
import android.os.ICustomFrequencyManager.Stub;
import android.os.StrictMode.ThreadPolicy;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class DVFSHelper {
    public static final String ACTION_AMS_RESUME = "ActivityManager_resume";
    public static final String ACTION_APP_LAUNCH = "Application_launch";
    public static final String ACTION_BROWSER_FLING = "Browser_fling";
    public static final String ACTION_BROWSER_TOUCH = "Browser_touch";
    private static final String ACTION_DEVICE_WAKEUP = "Device_wakeup";
    public static final String ACTION_GALLERY_TOUCH = "Gallery_touch";
    public static final String ACTION_GALLERY_TOUCH_TAIL = "Gallery_touch_tail";
    public static final String ACTION_LAUNCHER_HOMEMENU = "Launcher_homemenu";
    public static final String ACTION_LAUNCHER_TOUCH = "Launcher_touch";
    public static final String ACTION_LISTVIEW_SCROLL = "ListView_scroll";
    public static final String ACTION_PWM_ROTATION = "PhoneWindowManager_rotation";
    public static final String ACTION_SHAREMUSIC_GROUPPLAY = "ShareMusic_groupPlay";
    public static final int AMS_RESUME_BOOST_TYPE_ACQUIRE = 1;
    public static final int AMS_RESUME_BOOST_TYPE_RELEASE = 2;
    public static final int AMS_RESUME_BOOST_TYPE_TAIL = 3;
    public static volatile int AMS_RESUME_TAIL_BOOST_TIMEOUT = 0;
    private static final String BASE_MODEL = "zl";
    private static final String BOARD_PLATFORM = SystemProperties.get("ro.board.platform");
    @Deprecated
    public static final int BOOST_TYPE_LCD_FRAME_RATE = 4;
    @Deprecated
    public static final int BOOST_TYPE_TOUCH = 1;
    @Deprecated
    public static final long BO_BUS_MAX = 8;
    @Deprecated
    public static final long BO_CPU_MAX = 2;
    @Deprecated
    public static final long BO_CUSTOM_VALUE = 32;
    private static final String CHIP_NAME = SystemProperties.get("ro.chipname");
    private static final String DEVICE_TYPE = SystemProperties.get("ro.build.characteristics");
    private static final String HARDWARE_NAME = SystemProperties.get("ro.hardware");
    public static volatile int LIST_SCROLL_BOOSTER_CORE_NUM = 0;
    private static final String LOG_LEVEL_PROP = "ro.debug_level";
    private static final String LOG_LEVEL_PROP_HIGH = "0x4948";
    private static final String LOG_LEVEL_PROP_LOW = "0x4f4c";
    private static final String LOG_LEVEL_PROP_MID = "0x494d";
    private static final String LOG_TAG = "DVFSHelper";
    public static volatile int PWM_ROTATION_BOOST_TIMEOUT = 0;
    private static final String SIOP_MODEL = "ssrm_zlf_xx";
    public static final int TYPE_BUS_MAX = 20;
    public static final int TYPE_BUS_MIN = 19;
    public static final int TYPE_CPU_CORE_NUM_MAX = 15;
    public static final int TYPE_CPU_CORE_NUM_MIN = 14;
    public static final int TYPE_CPU_DISABLE_CSTATE = 22;
    public static final int TYPE_CPU_HOTPLUG_DISABLE = 24;
    public static final int TYPE_CPU_LEGACY_SCHED = 23;
    public static final int TYPE_CPU_MAX = 13;
    public static final int TYPE_CPU_MIN = 12;
    public static final int TYPE_EMMC_BURST_MODE = 18;
    public static final int TYPE_FPS_MAX = 21;
    public static final int TYPE_GPU_MAX = 17;
    public static final int TYPE_GPU_MIN = 16;
    public static final int TYPE_MAX = 26;
    public static final int TYPE_NONE = 11;
    public static final int TYPE_PCIE_PSM_DISABLE = 25;
    private static final boolean isEngBinary = "eng".equals(Build.TYPE);
    private static volatile DVFSHelper mAMSCState = null;
    private static volatile DVFSHelper mAMSCStateTail = null;
    private static int mToken = 0;
    static volatile ICustomFrequencyManager sCfmsService = null;
    private static boolean sIsDebugLevelHigh = LOG_LEVEL_PROP_HIGH.equals(SystemProperties.get(LOG_LEVEL_PROP, LOG_LEVEL_PROP_LOW));
    final int APP_LAUNCH_BOOSTING_TIMEOUT_L;
    final int APP_LAUNCH_BOOSTING_TIMEOUT_LL;
    final int APP_LAUNCH_BOOSTING_TIMEOUT_M;
    final int APP_LAUNCH_BOOSTING_TIMEOUT_S;
    private final boolean REGION_JPN;
    final int ROTATION_BOOSTING_TIMEOUT;
    final int ROTATION_GPU_BOOSTING_TIMEOUT;
    FrequencyRequest busRequest;
    FrequencyRequest cpuDisCStateRequest;
    FrequencyRequest cpuHotplugDisableRequest;
    FrequencyRequest cpuLegacySchedulerRequest;
    FrequencyRequest cpuNumRequest;
    FrequencyRequest cpuRequest;
    FrequencyRequest fpsRequest;
    FrequencyRequest gpuRequest;
    DVFSHelper mAppLaunchBUSBooster;
    int mAppLaunchBoostTime;
    DVFSHelper mAppLaunchCPUBooster;
    DVFSHelper mAppLaunchCPUCoreNumBooster;
    DVFSHelper mAppLaunchCState;
    DVFSHelper mAppLaunchGPUBooster;
    private String[] mAppLaunchPackages;
    int[] mBUSFrequencyTable;
    int[] mCPUCoreTable;
    int[] mCPUFrequencyTable;
    private Context mContext;
    private CustomFrequencyManager mCustomFreqManager;
    int[] mGPUFrequencyTable;
    private ArrayList<DVFSHelper> mHintList;
    private int mHintTimeout;
    private Intent mIntentExtra;
    private volatile boolean mIsAcquired;
    private boolean mIsHintNotifier;
    private Model mModel;
    private String mPkgName;
    DVFSHelper mRotationBUSBooster;
    DVFSHelper mRotationCPUCoreNumBooster;
    DVFSHelper mRotationGPUBooster;
    private int[] mSupportedBUSFrequency;
    int[] mSupportedCPUCoreNum;
    private int[] mSupportedCPUCoreNumForSSRM;
    private int[] mSupportedCPUFrequency;
    int[] mSupportedCPUFrequencyForSSRM;
    private int[] mSupportedGPUFrequency;
    private int[] mSupportedGPUFrequencyForSSRM;
    private int mType;
    FrequencyRequest mmcRequest;
    FrequencyRequest pciePsmDisableRequest;

    private class Model {
        protected int AMS_RESUME_ARM_FREQ;
        protected int AMS_RESUME_BUS_FREQ;
        protected int AMS_RESUME_CPU_CORE;
        protected int AMS_RESUME_GPU_FREQ;
        protected int APP_LAUNCH_ARM_FREQ;
        protected int APP_LAUNCH_BUS_FREQ;
        protected int APP_LAUNCH_CPU_CORE;
        protected int APP_LAUNCH_GPU_FREQ;
        protected int BROWSER_FLING_ARM_FREQ;
        protected int BROWSER_TOUCH_ARM_FREQ;
        protected int BROWSER_TOUCH_BOOST_TIMEOUT;
        protected int BROWSER_TOUCH_BUS_FREQ;
        protected int DEVICE_WAKEUP_ARM_FREQ;
        protected int GALLERY_TOUCH_ARM_FREQ;
        protected int GALLERY_TOUCH_BOOST_TIMEOUT;
        protected int GALLERY_TOUCH_BUS_FREQ;
        protected int GALLERY_TOUCH_TAIL_BOOST_TIMEOUT;
        protected int GROUP_PLAY_ARM_FREQ;
        protected int LAUNCHER_TOUCH_ARM_FREQ;
        protected int LAUNCHER_TOUCH_BOOST_TIMEOUT;
        protected int LAUNCHER_TOUCH_BUS_FREQ;
        protected int LAUNCHER_TOUCH_CPU_CORE;
        protected int LAUNCHER_TOUCH_GPU_FREQ;
        protected int LIST_SCROLL_ARM_FREQ;
        protected int LIST_SCROLL_BUS_FREQ;
        protected int LIST_SCROLL_GPU_FREQ;
        protected int ROTATION_ARM_FREQ;

        private Model() {
        }

        public int getTimeoutForAction(String action) {
            if (DVFSHelper.ACTION_LAUNCHER_TOUCH.equals(action)) {
                return this.LAUNCHER_TOUCH_BOOST_TIMEOUT;
            }
            if (DVFSHelper.ACTION_GALLERY_TOUCH.equals(action)) {
                return this.GALLERY_TOUCH_BOOST_TIMEOUT;
            }
            if (DVFSHelper.ACTION_GALLERY_TOUCH_TAIL.equals(action)) {
                return this.GALLERY_TOUCH_TAIL_BOOST_TIMEOUT;
            }
            if (DVFSHelper.ACTION_BROWSER_TOUCH.equals(action)) {
                return this.BROWSER_TOUCH_BOOST_TIMEOUT;
            }
            return -1;
        }

        public int getGalleryTouchCPUFreq() {
            return this.GALLERY_TOUCH_ARM_FREQ;
        }

        public int getGalleryTouchBUSFreq() {
            return this.GALLERY_TOUCH_BUS_FREQ;
        }

        public int getLauncherTouchCPUFreq() {
            return this.LAUNCHER_TOUCH_ARM_FREQ;
        }

        public int getLauncherTouchGPUFreq() {
            return this.LAUNCHER_TOUCH_GPU_FREQ;
        }

        public int getLauncherTouchBUSFreq() {
            return this.LAUNCHER_TOUCH_BUS_FREQ;
        }

        public int getLauncherTouchCPUCore() {
            if (DVFSHelper.this.mSupportedCPUCoreNum == null || DVFSHelper.this.mSupportedCPUCoreNum.length < 1) {
                return -1;
            }
            if (this.LAUNCHER_TOUCH_CPU_CORE > DVFSHelper.this.mSupportedCPUCoreNum[0]) {
                return DVFSHelper.this.mSupportedCPUCoreNum[0];
            }
            return this.LAUNCHER_TOUCH_CPU_CORE;
        }

        public int getBrowserTouchCPUFreq() {
            return this.BROWSER_TOUCH_ARM_FREQ;
        }

        public int getBrowserTouchBUSFreq() {
            return this.BROWSER_TOUCH_BUS_FREQ;
        }

        public int getAMSResumeGPUFreq() {
            return this.AMS_RESUME_GPU_FREQ;
        }

        public int getAMSResumeBUSFreq() {
            return this.AMS_RESUME_BUS_FREQ;
        }

        public int getAMSResumeCPUCore() {
            if (DVFSHelper.this.mSupportedCPUCoreNum == null || DVFSHelper.this.mSupportedCPUCoreNum.length < 1) {
                return -1;
            }
            if (this.AMS_RESUME_CPU_CORE > DVFSHelper.this.mSupportedCPUCoreNum[0]) {
                return DVFSHelper.this.mSupportedCPUCoreNum[0];
            }
            return this.AMS_RESUME_CPU_CORE;
        }

        public int getAppLaunchCPUFreq() {
            if (DVFSHelper.this.mSupportedCPUFrequencyForSSRM == null || DVFSHelper.this.mSupportedCPUFrequencyForSSRM.length < 1 || this.APP_LAUNCH_ARM_FREQ < 0) {
                return -1;
            }
            return this.APP_LAUNCH_ARM_FREQ == 0 ? DVFSHelper.this.mSupportedCPUFrequencyForSSRM[0] : DVFSHelper.this.getApproximateCPUFrequency(this.APP_LAUNCH_ARM_FREQ);
        }

        public int getAppLaunchGPUFreq() {
            return this.APP_LAUNCH_GPU_FREQ;
        }

        public int getAppLaunchBUSFreq() {
            return this.APP_LAUNCH_BUS_FREQ;
        }

        public int getAppLaunchCPUCore() {
            if (DVFSHelper.this.mSupportedCPUCoreNumForSSRM == null || DVFSHelper.this.mSupportedCPUCoreNumForSSRM.length < 1) {
                return 0;
            }
            if (this.APP_LAUNCH_CPU_CORE == 0) {
                return DVFSHelper.this.mSupportedCPUCoreNumForSSRM[0];
            }
            if (this.APP_LAUNCH_CPU_CORE > DVFSHelper.this.mSupportedCPUCoreNumForSSRM[0]) {
                return DVFSHelper.this.mSupportedCPUCoreNumForSSRM[0];
            }
            return this.APP_LAUNCH_CPU_CORE;
        }

        public int getListScrollCPUFreq() {
            return this.LIST_SCROLL_ARM_FREQ;
        }

        public int getListScrollGPUFreq() {
            return this.LIST_SCROLL_GPU_FREQ;
        }

        public int getListScrollBUSFreq() {
            return this.LIST_SCROLL_BUS_FREQ;
        }

        public int getShareMusicCPUFreq() {
            return this.GROUP_PLAY_ARM_FREQ;
        }

        public int getBrowserFlingCpuFreq() {
            return this.BROWSER_FLING_ARM_FREQ;
        }

        public int getAMSResumeCPUFreq() {
            if (DVFSHelper.this.REGION_JPN && "tf".equals(DVFSHelper.BASE_MODEL)) {
                return DVFSHelper.this.getApproximateCPUFrequencyForSSRM(this.AMS_RESUME_ARM_FREQ);
            }
            if ("MSM8939".equals(DVFSHelper.CHIP_NAME) || "MSM8929".equals(DVFSHelper.CHIP_NAME) || "msm8992".equals(DVFSHelper.BOARD_PLATFORM)) {
                if (this.AMS_RESUME_ARM_FREQ <= 0) {
                    return -1;
                }
                return DVFSHelper.this.getApproximateCPUFrequencyForSSRM(this.AMS_RESUME_ARM_FREQ);
            } else if (this.AMS_RESUME_ARM_FREQ < 0) {
                return -1;
            } else {
                int freq = DVFSHelper.this.getApproximateCPUFrequencyByPercentOfMaximumForSSRM(0.7d);
                if (freq <= 0) {
                    return -1;
                }
                return freq <= this.AMS_RESUME_ARM_FREQ ? DVFSHelper.this.getApproximateCPUFrequencyForSSRM(this.AMS_RESUME_ARM_FREQ) : freq;
            }
        }

        public int getRotationCPUFreq() {
            if (DVFSHelper.this.mSupportedCPUFrequencyForSSRM == null || DVFSHelper.this.mSupportedCPUFrequencyForSSRM.length < 1 || this.ROTATION_ARM_FREQ < 0) {
                return -1;
            }
            return this.ROTATION_ARM_FREQ == 0 ? DVFSHelper.this.mSupportedCPUFrequencyForSSRM[0] : DVFSHelper.this.getApproximateCPUFrequency(this.ROTATION_ARM_FREQ);
        }

        public int getDeviceWakeupCPUFreq() {
            if (DVFSHelper.this.mSupportedCPUFrequency == null || this.DEVICE_WAKEUP_ARM_FREQ < 0) {
                return -1;
            }
            if (this.DEVICE_WAKEUP_ARM_FREQ == 0 || this.DEVICE_WAKEUP_ARM_FREQ > DVFSHelper.this.mSupportedCPUFrequency[0]) {
                return getAMSResumeCPUFreq();
            }
            return DVFSHelper.this.getApproximateCPUFrequency(this.DEVICE_WAKEUP_ARM_FREQ);
        }
    }

    private class ModelJBP extends Model {
        ModelJBP() {
            super();
            this.BROWSER_TOUCH_BOOST_TIMEOUT = 1000;
            this.BROWSER_TOUCH_ARM_FREQ = -1;
            this.BROWSER_TOUCH_BUS_FREQ = -1;
            this.GALLERY_TOUCH_BOOST_TIMEOUT = 500;
            this.GALLERY_TOUCH_TAIL_BOOST_TIMEOUT = 500;
            this.GALLERY_TOUCH_ARM_FREQ = 1500000;
            this.GALLERY_TOUCH_BUS_FREQ = -1;
            this.LAUNCHER_TOUCH_BOOST_TIMEOUT = 1000;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1500000;
            this.LAUNCHER_TOUCH_BUS_FREQ = -1;
            this.LAUNCHER_TOUCH_GPU_FREQ = -1;
            this.LAUNCHER_TOUCH_CPU_CORE = 0;
            this.GROUP_PLAY_ARM_FREQ = 1200000;
            this.LIST_SCROLL_ARM_FREQ = 800000;
            this.LIST_SCROLL_GPU_FREQ = -1;
            this.LIST_SCROLL_BUS_FREQ = -1;
            DVFSHelper.LIST_SCROLL_BOOSTER_CORE_NUM = 2;
            this.AMS_RESUME_ARM_FREQ = 1150000;
            this.AMS_RESUME_GPU_FREQ = -1;
            this.AMS_RESUME_BUS_FREQ = -1;
            this.AMS_RESUME_CPU_CORE = 0;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = 300;
            this.APP_LAUNCH_ARM_FREQ = 0;
            this.APP_LAUNCH_GPU_FREQ = -1;
            this.APP_LAUNCH_BUS_FREQ = -1;
            this.APP_LAUNCH_CPU_CORE = 0;
            this.BROWSER_FLING_ARM_FREQ = -1;
            DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT = 2000;
            this.DEVICE_WAKEUP_ARM_FREQ = 0;
            this.ROTATION_ARM_FREQ = 0;
        }
    }

    private class Model8930AB extends ModelJBP {
        Model8930AB() {
            super();
            this.LAUNCHER_TOUCH_BOOST_TIMEOUT = 200;
            this.GALLERY_TOUCH_ARM_FREQ = 1400000;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1400000;
            this.LIST_SCROLL_ARM_FREQ = 1000000;
        }
    }

    private class ModelA8E extends ModelJBP {
        ModelA8E() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1600000;
            this.LIST_SCROLL_ARM_FREQ = 800000;
            this.GALLERY_TOUCH_ARM_FREQ = -1;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1200000;
            this.LAUNCHER_TOUCH_BOOST_TIMEOUT = 100;
            DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT = 1000;
            this.GROUP_PLAY_ARM_FREQ = 650000;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = 500;
        }
    }

    private class ModelCARMEN2 extends ModelJBP {
        ModelCARMEN2() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1500000;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = CalendarColumns.CAL_ACCESS_EDITOR;
            this.LIST_SCROLL_ARM_FREQ = 1000000;
            this.GALLERY_TOUCH_ARM_FREQ = 1100000;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1100000;
            DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT = 1500;
            this.DEVICE_WAKEUP_ARM_FREQ = 1100000;
        }
    }

    private class ModelCORE33G extends ModelJBP {
        ModelCORE33G() {
            super();
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = 3000;
        }
    }

    private class ModelD2 extends ModelJBP {
        ModelD2() {
            super();
            this.AMS_RESUME_CPU_CORE = 2;
        }
    }

    private class ModelKMINI extends ModelJBP {
        ModelKMINI() {
            super();
            this.LAUNCHER_TOUCH_ARM_FREQ = -1;
            this.LIST_SCROLL_ARM_FREQ = 1000000;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = 1000;
            this.AMS_RESUME_ARM_FREQ = 1400000;
            this.AMS_RESUME_BUS_FREQ = 400000;
            this.APP_LAUNCH_BUS_FREQ = 400000;
        }
    }

    private class ModelDegasLTE extends ModelKMINI {
        ModelDegasLTE() {
            super();
            this.LAUNCHER_TOUCH_ARM_FREQ = 1400000;
            this.AMS_RESUME_ARM_FREQ = 1400000;
            this.LIST_SCROLL_ARM_FREQ = 1000000;
        }
    }

    private class ModelExynos4 extends ModelJBP {
        ModelExynos4() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1400000;
            this.AMS_RESUME_GPU_FREQ = 1;
            this.AMS_RESUME_CPU_CORE = 3;
        }
    }

    private class ModelHA extends ModelJBP {
        ModelHA() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1800000;
            this.GALLERY_TOUCH_ARM_FREQ = -1;
            this.LAUNCHER_TOUCH_ARM_FREQ = -1;
        }
    }

    private class ModelHF extends ModelJBP {
        ModelHF() {
            super();
            this.LIST_SCROLL_ARM_FREQ = 1200000;
            this.AMS_RESUME_ARM_FREQ = 1800000;
            this.AMS_RESUME_CPU_CORE = 4;
            this.GALLERY_TOUCH_ARM_FREQ = -1;
            this.LAUNCHER_TOUCH_ARM_FREQ = -1;
            DVFSHelper.LIST_SCROLL_BOOSTER_CORE_NUM = 2;
        }
    }

    private class ModelHRL extends ModelJBP {
        ModelHRL() {
            super();
            this.GALLERY_TOUCH_ARM_FREQ = -1;
            this.LAUNCHER_TOUCH_ARM_FREQ = -1;
            this.LIST_SCROLL_ARM_FREQ = 728000;
            this.AMS_RESUME_ARM_FREQ = 1768000;
            this.DEVICE_WAKEUP_ARM_FREQ = 1144000;
            DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT = 1500;
        }
    }

    private class ModelHRQ extends ModelJBP {
        ModelHRQ() {
            super();
            this.APP_LAUNCH_BUS_FREQ = Device.WEARABLE_JACKET;
            this.AMS_RESUME_BUS_FREQ = 1555;
        }
    }

    private class ModelHawaii extends ModelJBP {
        ModelHawaii() {
            super();
            this.LIST_SCROLL_ARM_FREQ = 1200000;
        }
    }

    private class ModelISLA extends ModelJBP {
        ModelISLA() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1300000;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = CalendarColumns.CAL_ACCESS_EDITOR;
            this.LIST_SCROLL_ARM_FREQ = 900000;
            this.GALLERY_TOUCH_ARM_FREQ = 1000000;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1000000;
            DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT = 1500;
            this.DEVICE_WAKEUP_ARM_FREQ = 1000000;
        }
    }

    private class ModelISLAND extends ModelJBP {
        ModelISLAND() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1105000;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = 1000;
            this.LIST_SCROLL_ARM_FREQ = 806000;
            this.GALLERY_TOUCH_ARM_FREQ = 1105000;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1105000;
            DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT = 2000;
            this.DEVICE_WAKEUP_ARM_FREQ = 1105000;
        }
    }

    private class ModelISLAQUAD extends ModelJBP {
        ModelISLAQUAD() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1300000;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = CalendarColumns.CAL_ACCESS_EDITOR;
            this.LIST_SCROLL_ARM_FREQ = 900000;
            this.GALLERY_TOUCH_ARM_FREQ = 1000000;
            this.GALLERY_TOUCH_ARM_FREQ = 1000000;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1000000;
            DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT = 1500;
            this.DEVICE_WAKEUP_ARM_FREQ = 1000000;
        }
    }

    private class ModelJA extends ModelJBP {
        ModelJA() {
            super();
            this.LAUNCHER_TOUCH_BOOST_TIMEOUT = 200;
            this.GALLERY_TOUCH_ARM_FREQ = 1300000;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1300000;
            this.LAUNCHER_TOUCH_GPU_FREQ = 266;
            this.GROUP_PLAY_ARM_FREQ = PersonaInfo.KNOX_SECURITY_TIMEOUT_DEFAULT;
            this.AMS_RESUME_GPU_FREQ = 266;
        }
    }

    private class ModelJAKOR extends ModelJA {
        ModelJAKOR() {
            super();
            this.ROTATION_ARM_FREQ = 1300000;
        }
    }

    private class ModelJF extends ModelJBP {
        ModelJF() {
            super();
            this.LAUNCHER_TOUCH_BOOST_TIMEOUT = 200;
            this.GALLERY_TOUCH_ARM_FREQ = 1400000;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1400000;
            this.GROUP_PLAY_ARM_FREQ = 1200000;
            this.LIST_SCROLL_ARM_FREQ = 1500000;
            DVFSHelper.LIST_SCROLL_BOOSTER_CORE_NUM = 2;
        }
    }

    private class ModelKA extends ModelJBP {
        ModelKA() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1800000;
            this.GALLERY_TOUCH_ARM_FREQ = -1;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1900000;
            this.LAUNCHER_TOUCH_BOOST_TIMEOUT = 100;
        }
    }

    private class ModelKAM extends ModelJBP {
        ModelKAM() {
            super();
            this.LAUNCHER_TOUCH_BOOST_TIMEOUT = 200;
            this.GALLERY_TOUCH_BOOST_TIMEOUT = 200;
            this.GALLERY_TOUCH_TAIL_BOOST_TIMEOUT = CalendarColumns.CAL_ACCESS_EDITOR;
            this.AMS_RESUME_ARM_FREQ = 1600000;
        }
    }

    private class ModelKF extends ModelJBP {
        ModelKF() {
            super();
            this.LIST_SCROLL_ARM_FREQ = 1200000;
            this.AMS_RESUME_ARM_FREQ = 1800000;
            this.AMS_RESUME_CPU_CORE = 4;
            this.GALLERY_TOUCH_ARM_FREQ = -1;
            this.LAUNCHER_TOUCH_ARM_FREQ = 2457600;
            this.LAUNCHER_TOUCH_BOOST_TIMEOUT = 100;
            this.APP_LAUNCH_BUS_FREQ = 1525;
            this.AMS_RESUME_BUS_FREQ = 1525;
            DVFSHelper.LIST_SCROLL_BOOSTER_CORE_NUM = 2;
        }
    }

    private class ModelKQ extends ModelJBP {
        ModelKQ() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1800000;
            this.GALLERY_TOUCH_ARM_FREQ = -1;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1900000;
            this.LAUNCHER_TOUCH_BOOST_TIMEOUT = 100;
        }
    }

    private class ModelM0 extends ModelJBP {
        ModelM0() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1200000;
            this.AMS_RESUME_GPU_FREQ = 1;
            this.AMS_RESUME_CPU_CORE = 3;
        }
    }

    private class ModelMSM8916 extends ModelJBP {
        ModelMSM8916() {
            super();
            this.GALLERY_TOUCH_ARM_FREQ = 1190400;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1190400;
            this.LAUNCHER_TOUCH_CPU_CORE = 4;
            this.AMS_RESUME_CPU_CORE = 3;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = CalendarColumns.CAL_ACCESS_EDITOR;
        }
    }

    private class ModelMSM8916_A3 extends ModelJBP {
        ModelMSM8916_A3() {
            super();
            this.GALLERY_TOUCH_ARM_FREQ = 1190400;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1094400;
            this.LAUNCHER_TOUCH_CPU_CORE = 4;
            this.AMS_RESUME_CPU_CORE = 3;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = CalendarColumns.CAL_ACCESS_EDITOR;
        }
    }

    private class ModelMSM8929 extends ModelMSM8916 {
        ModelMSM8929() {
            super();
            this.LIST_SCROLL_ARM_FREQ = 400000;
            this.AMS_RESUME_ARM_FREQ = 1267200;
            this.AMS_RESUME_BUS_FREQ = 662;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = CalendarColumns.CAL_ACCESS_EDITOR;
            this.DEVICE_WAKEUP_ARM_FREQ = 499200;
            this.GALLERY_TOUCH_ARM_FREQ = 499200;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1036800;
            DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT = 1500;
        }
    }

    private class ModelMSM8939 extends ModelMSM8916 {
        ModelMSM8939() {
            super();
            this.LIST_SCROLL_ARM_FREQ = 400000;
            this.AMS_RESUME_ARM_FREQ = 1267200;
            this.AMS_RESUME_BUS_FREQ = 796;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = CalendarColumns.CAL_ACCESS_EDITOR;
            this.DEVICE_WAKEUP_ARM_FREQ = 499200;
            this.GALLERY_TOUCH_ARM_FREQ = 556800;
            this.LAUNCHER_TOUCH_ARM_FREQ = 556800;
            DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT = 1500;
        }
    }

    private class ModelMSM8952 extends ModelJBP {
        ModelMSM8952() {
            super();
            this.LIST_SCROLL_ARM_FREQ = 499200;
            this.AMS_RESUME_ARM_FREQ = 1344000;
            this.AMS_RESUME_BUS_FREQ = 806;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = CalendarColumns.CAL_ACCESS_EDITOR;
            this.APP_LAUNCH_BUS_FREQ = 931;
            this.DEVICE_WAKEUP_ARM_FREQ = 499200;
            this.GALLERY_TOUCH_ARM_FREQ = 547200;
            this.LAUNCHER_TOUCH_ARM_FREQ = 547200;
            DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT = 1500;
        }
    }

    private class ModelMSM8976 extends ModelJBP {
        ModelMSM8976() {
            super();
            this.LAUNCHER_TOUCH_ARM_FREQ = 1612800;
            this.LAUNCHER_TOUCH_BOOST_TIMEOUT = 1000;
            this.GALLERY_TOUCH_ARM_FREQ = 1612800;
            this.GALLERY_TOUCH_BOOST_TIMEOUT = 500;
            this.LIST_SCROLL_ARM_FREQ = 883200;
            this.AMS_RESUME_ARM_FREQ = 1305600;
            this.AMS_RESUME_BUS_FREQ = 931;
            this.AMS_RESUME_CPU_CORE = 4;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = CalendarColumns.CAL_ACCESS_EDITOR;
            this.ROTATION_ARM_FREQ = 1804800;
            DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT = 2000;
            this.APP_LAUNCH_ARM_FREQ = 1804800;
            this.APP_LAUNCH_BUS_FREQ = 931;
            this.APP_LAUNCH_CPU_CORE = 4;
            this.DEVICE_WAKEUP_ARM_FREQ = 1305600;
        }
    }

    private class ModelMSM8992 extends ModelJBP {
        ModelMSM8992() {
            super();
            this.GALLERY_TOUCH_ARM_FREQ = 624000;
            this.LAUNCHER_TOUCH_ARM_FREQ = 624000;
            this.LIST_SCROLL_ARM_FREQ = 480000;
            this.DEVICE_WAKEUP_ARM_FREQ = 624000;
            this.AMS_RESUME_ARM_FREQ = 1248000;
            this.AMS_RESUME_CPU_CORE = 2;
            this.AMS_RESUME_BUS_FREQ = 931;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = CalendarColumns.CAL_ACCESS_EDITOR;
            this.ROTATION_ARM_FREQ = 624000;
            DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT = 1500;
            this.APP_LAUNCH_ARM_FREQ = 1248000;
            this.APP_LAUNCH_CPU_CORE = 2;
        }
    }

    private class ModelMSM8x26 extends ModelJBP {
        ModelMSM8x26() {
            super();
            this.LAUNCHER_TOUCH_ARM_FREQ = 1094400;
            this.AMS_RESUME_CPU_CORE = 4;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = 1000;
        }
    }

    private class ModelNOVEL extends ModelISLAND {
        ModelNOVEL() {
            super();
            this.ROTATION_ARM_FREQ = 1196000;
        }
    }

    private class ModelPP extends ModelJBP {
        ModelPP() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1400000;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1300000;
        }
    }

    private class ModelPXA1088 extends ModelJBP {
        ModelPXA1088() {
            super();
            this.AMS_RESUME_GPU_FREQ = 624000;
            this.AMS_RESUME_BUS_FREQ = 533000;
            this.APP_LAUNCH_GPU_FREQ = 624000;
            this.APP_LAUNCH_BUS_FREQ = 533000;
            this.LIST_SCROLL_ARM_FREQ = 1066000;
            this.LIST_SCROLL_GPU_FREQ = 624000;
            this.LIST_SCROLL_BUS_FREQ = 312000;
        }
    }

    private class ModelPXA1908 extends ModelJBP {
        ModelPXA1908() {
            super();
            this.AMS_RESUME_GPU_FREQ = 624000;
            this.AMS_RESUME_BUS_FREQ = 528000;
            this.APP_LAUNCH_GPU_FREQ = 624000;
            this.APP_LAUNCH_BUS_FREQ = 528000;
            this.LIST_SCROLL_ARM_FREQ = 1057000;
            this.LIST_SCROLL_GPU_FREQ = 624000;
            this.LIST_SCROLL_BUS_FREQ = 312000;
            this.LAUNCHER_TOUCH_ARM_FREQ = -1;
        }
    }

    private class ModelPXA1936 extends ModelJBP {
        ModelPXA1936() {
            super();
            this.AMS_RESUME_GPU_FREQ = 624000;
            this.AMS_RESUME_BUS_FREQ = 528000;
            this.APP_LAUNCH_GPU_FREQ = 624000;
            this.APP_LAUNCH_BUS_FREQ = 528000;
            this.LIST_SCROLL_ARM_FREQ = 1057000;
            this.LIST_SCROLL_GPU_FREQ = 624000;
            this.LIST_SCROLL_BUS_FREQ = 312000;
            this.LAUNCHER_TOUCH_ARM_FREQ = -1;
        }
    }

    private class ModelPicasso3GWIFI extends ModelHA {
        ModelPicasso3GWIFI() {
            super();
            this.BROWSER_FLING_ARM_FREQ = 1000000;
        }
    }

    private class ModelSA extends ModelJBP {
        ModelSA() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1600000;
            this.LIST_SCROLL_ARM_FREQ = 650000;
            this.GALLERY_TOUCH_ARM_FREQ = -1;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1200000;
            this.LAUNCHER_TOUCH_BOOST_TIMEOUT = 100;
            DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT = 1000;
            this.GROUP_PLAY_ARM_FREQ = 650000;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = 500;
        }
    }

    private class ModelSF extends ModelJBP {
        ModelSF() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1700000;
            this.AMS_RESUME_CPU_CORE = 4;
            this.GALLERY_TOUCH_ARM_FREQ = -1;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1190400;
            this.LAUNCHER_TOUCH_BOOST_TIMEOUT = 5000;
            DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT = 1000;
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = 500;
        }
    }

    private class ModelSantos10 extends ModelJBP {
        ModelSantos10() {
            super();
            this.GALLERY_TOUCH_ARM_FREQ = 1300000;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1300000;
            this.GROUP_PLAY_ARM_FREQ = -1;
        }
    }

    private class ModelTA extends ModelJBP {
        ModelTA() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1800000;
            this.GALLERY_TOUCH_ARM_FREQ = -1;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1900000;
            this.LAUNCHER_TOUCH_GPU_FREQ = 420;
            this.LAUNCHER_TOUCH_BOOST_TIMEOUT = 100;
            this.DEVICE_WAKEUP_ARM_FREQ = 1200000;
        }
    }

    private class ModelTF extends ModelJBP {
        ModelTF() {
            super();
            this.LIST_SCROLL_ARM_FREQ = 1200000;
            this.AMS_RESUME_ARM_FREQ = 1800000;
            this.AMS_RESUME_CPU_CORE = 4;
            this.AMS_RESUME_BUS_FREQ = 460;
            this.APP_LAUNCH_BUS_FREQ = 662;
            this.GALLERY_TOUCH_BOOST_TIMEOUT = 5000;
            this.GALLERY_TOUCH_ARM_FREQ = 2457600;
            this.GALLERY_TOUCH_BUS_FREQ = Device.PHONE_MODEM_OR_GATEWAY;
            this.LAUNCHER_TOUCH_BOOST_TIMEOUT = 5000;
            this.LAUNCHER_TOUCH_ARM_FREQ = 2649600;
            this.LAUNCHER_TOUCH_BUS_FREQ = Device.PHONE_MODEM_OR_GATEWAY;
            DVFSHelper.LIST_SCROLL_BOOSTER_CORE_NUM = 2;
        }
    }

    private class ModelTFJpn extends ModelTF {
        ModelTFJpn() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1497600;
            this.GALLERY_TOUCH_ARM_FREQ = -1;
            this.GALLERY_TOUCH_BUS_FREQ = -1;
            this.DEVICE_WAKEUP_ARM_FREQ = 1497600;
        }
    }

    private class ModelTR3CA extends ModelJBP {
        ModelTR3CA() {
            super();
            this.AMS_RESUME_ARM_FREQ = 1248000;
            this.GALLERY_TOUCH_ARM_FREQ = -1;
            this.LAUNCHER_TOUCH_ARM_FREQ = 1248000;
            this.LAUNCHER_TOUCH_BOOST_TIMEOUT = 100;
            this.DEVICE_WAKEUP_ARM_FREQ = 1248000;
            this.ROTATION_ARM_FREQ = 1248000;
        }
    }

    private class ModelV13GWIFI extends ModelHA {
        ModelV13GWIFI() {
            super();
            this.BROWSER_FLING_ARM_FREQ = 1000000;
        }
    }

    private class ModelV23GWIFI extends ModelHA {
        ModelV23GWIFI() {
            super();
            this.BROWSER_FLING_ARM_FREQ = 1000000;
        }
    }

    private class ModelVIVALTO3MVE extends ModelJBP {
        ModelVIVALTO3MVE() {
            super();
            DVFSHelper.AMS_RESUME_TAIL_BOOST_TIMEOUT = 3000;
        }
    }

    private class ModelZL extends ModelJBP {
        ModelZL() {
            super();
            this.GALLERY_TOUCH_ARM_FREQ = -1;
            this.LAUNCHER_TOUCH_ARM_FREQ = -1;
            this.AMS_RESUME_ARM_FREQ = 1800000;
            this.DEVICE_WAKEUP_ARM_FREQ = 1200000;
            DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT = 1500;
        }
    }

    public DVFSHelper(Context context, int type) {
        this(context, null, type, 0);
    }

    public DVFSHelper(Context context, int type, long option) {
        this(context, null, type, option);
    }

    public DVFSHelper(Context context, String packageName, int type, long option) {
        boolean z;
        this.mContext = null;
        this.mPkgName = null;
        this.mIntentExtra = null;
        this.mType = 11;
        this.mCustomFreqManager = null;
        this.mSupportedCPUFrequency = null;
        this.mSupportedCPUFrequencyForSSRM = null;
        this.mSupportedCPUCoreNum = null;
        this.mSupportedCPUCoreNumForSSRM = null;
        this.mSupportedGPUFrequency = null;
        this.mSupportedGPUFrequencyForSSRM = null;
        this.mSupportedBUSFrequency = null;
        this.cpuRequest = null;
        this.cpuNumRequest = null;
        this.gpuRequest = null;
        this.busRequest = null;
        this.mmcRequest = null;
        this.fpsRequest = null;
        this.cpuDisCStateRequest = null;
        this.cpuLegacySchedulerRequest = null;
        this.cpuHotplugDisableRequest = null;
        this.pciePsmDisableRequest = null;
        this.mIsAcquired = false;
        if ((SIOP_MODEL != null && SIOP_MODEL.contains("jpn")) || "DCM".equals(SystemProperties.get("ro.csc.sales_code")) || "KDI".equals(SystemProperties.get("ro.csc.sales_code"))) {
            z = true;
        } else {
            z = false;
        }
        this.REGION_JPN = z;
        this.mAppLaunchCPUBooster = null;
        this.mAppLaunchGPUBooster = null;
        this.mAppLaunchBUSBooster = null;
        this.mAppLaunchCPUCoreNumBooster = null;
        this.mAppLaunchCState = null;
        this.mCPUFrequencyTable = null;
        this.mCPUCoreTable = null;
        this.mGPUFrequencyTable = null;
        this.mBUSFrequencyTable = null;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_LL = 4000;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_L = 2000;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_S = 500;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_M = 1000;
        this.mAppLaunchBoostTime = 2000;
        this.mAppLaunchPackages = new String[]{x(new int[]{25, 21, 23, 84, 9, 31, 25, 84, 27, 20, 30, 8, 21, 19, 30, 84, 27, 10, 10, 84, 9, 24, 8, 21, 13, 9, 31, 8}), x(new int[]{25, 21, 23, 84, 29, 21, 21, 29, 22, 31, 84, 27, 20, 30, 8, 21, 19, 30, 84, 27, 10, 10, 9, 84, 25, 18, 8, 21, 23, 31}), x(new int[]{25, 21, 23, 84, 27, 20, 30, 8, 21, 19, 30, 84, 24, 8, 21, 13, 9, 31, 8}), x(new int[]{25, 21, 23, 84, 29, 21, 21, 29, 22, 31, 84, 27, 20, 30, 8, 21, 19, 30, 84, 29, 23}), x(new int[]{25, 21, 23, 84, 9, 27, 23, 9, 15, 20, 29, 84, 27, 20, 30, 8, 21, 19, 30, 84, 31, 23, 27, 19, 22, 84, 15, 19}), x(new int[]{25, 21, 23, 84, 28, 27, 25, 31, 24, 21, 21, 17, 84, 17, 27, 14, 27, 20, 27}), x(new int[]{25, 21, 23, 84, 27, 20, 30, 8, 21, 19, 30, 84, 12, 31, 20, 30, 19, 20, 29}), x(new int[]{25, 21, 23, 84, 9, 27, 23, 9, 15, 20, 29, 84, 31, 12, 31, 8, 29, 22, 27, 30, 31, 9, 84, 12, 19, 30, 31, 21}), x(new int[]{25, 21, 23, 84, 9, 27, 23, 9, 15, 20, 29, 84, 27, 20, 30, 8, 21, 19, 30, 84, 12, 19, 30, 31, 21}), x(new int[]{25, 21, 23, 84, 9, 31, 25, 84, 27, 20, 30, 8, 21, 19, 30, 84, 29, 27, 22, 22, 31, 8, 3, 73, 30}), x(new int[]{25, 21, 23, 84, 29, 21, 21, 29, 22, 31, 84, 27, 20, 30, 8, 21, 19, 30, 84, 23, 27, 10, 9}), x(new int[]{25, 21, 23, 84, 24, 27, 19, 30, 15, 84, 27, 10, 10, 9, 31, 27, 8, 25, 18}), x(new int[]{25, 21, 23, 84, 9, 19, 20, 27, 84, 13, 31, 19, 24, 21}), x(new int[]{25, 21, 23, 84, 24, 27, 19, 30, 15, 84, 56, 27, 19, 30, 15, 55, 27, 10}), x(new int[]{25, 21, 23, 84, 14, 13, 19, 14, 14, 31, 8, 84, 27, 20, 30, 8, 21, 19, 30})};
        this.ROTATION_BOOSTING_TIMEOUT = 500;
        this.ROTATION_GPU_BOOSTING_TIMEOUT = 2000;
        this.mRotationCPUCoreNumBooster = null;
        this.mRotationGPUBooster = null;
        this.mRotationBUSBooster = null;
        this.mHintList = null;
        this.mIsHintNotifier = false;
        this.mHintTimeout = -1;
        if (context != null) {
            this.mContext = context;
            this.mModel = createModel();
            this.mCustomFreqManager = (CustomFrequencyManager) this.mContext.getSystemService(Context.CFMS_SERVICE);
            if (this.mCustomFreqManager == null) {
                Log.i(LOG_TAG, "DVFSHelper:: failed to load CFMS");
                return;
            }
            logOnEng(LOG_TAG, "DVFSHelper:: New instance is created for " + packageName);
            this.mSupportedCPUFrequency = this.mCustomFreqManager.getSupportedCPUFrequency();
            this.mSupportedCPUFrequencyForSSRM = this.mCustomFreqManager.getSupportedCPUFrequency();
            adjustCPUFreqTable();
            this.mSupportedCPUCoreNum = this.mCustomFreqManager.getSupportedCPUCoreNum();
            this.mSupportedCPUCoreNumForSSRM = this.mCustomFreqManager.getSupportedCPUCoreNum();
            adjustCPUCoreTable();
            this.mSupportedGPUFrequency = this.mCustomFreqManager.getSupportedGPUFrequency();
            this.mSupportedGPUFrequencyForSSRM = this.mCustomFreqManager.getSupportedGPUFrequency();
            adjustGPUFreqTable();
            this.mSupportedBUSFrequency = this.mCustomFreqManager.getSupportedSysBusFrequency();
            mToken++;
            if (packageName != null) {
                this.mPkgName = packageName + "@" + mToken;
            } else {
                this.mPkgName = context.getPackageName() + "@" + mToken;
            }
            this.mType = type;
        }
    }

    public boolean isValidDVFSParam(int type) {
        if (type <= 11 || type >= 26) {
            return false;
        }
        return true;
    }

    public boolean isValidDVFSParam(int type, long option) {
        if (!isValidDVFSParam(type) || option < 0) {
            return false;
        }
        return true;
    }

    private void adjustCPUFreqTable() {
        if (this.mSupportedCPUFrequency != null) {
            int SHIFT_STEPS = 0;
            if ("hf".equals(BASE_MODEL)) {
                SHIFT_STEPS = 1;
            } else if ("hrl".equals(BASE_MODEL)) {
                SHIFT_STEPS = 6;
            } else if ("island".equals(BASE_MODEL)) {
                if (SIOP_MODEL.contains("novel")) {
                    SHIFT_STEPS = 1;
                }
            } else if ("hrq".equals(BASE_MODEL) || "kf".equals(BASE_MODEL) || "ka".equals(BASE_MODEL) || "tr3ca".equals(BASE_MODEL) || BASE_MODEL.equals(BASE_MODEL) || "zq".equals(BASE_MODEL)) {
                SHIFT_STEPS = 2;
            } else if ("tf".equals(BASE_MODEL)) {
                SHIFT_STEPS = this.mSupportedCPUFrequency[0] == 2649600 ? 5 : 2;
            }
            if (SIOP_MODEL.contains("lentis") || SIOP_MODEL.contains("kcat6") || "ta".equals(BASE_MODEL)) {
                SHIFT_STEPS = 2;
            } else if (SIOP_MODEL.contains("ja_kor")) {
                SHIFT_STEPS = 3;
            }
            if (SHIFT_STEPS > 0 && this.mSupportedCPUFrequency.length > SHIFT_STEPS) {
                int[] newCPUFreqTable = new int[(this.mSupportedCPUFrequency.length - SHIFT_STEPS)];
                for (int i = 0; i < this.mSupportedCPUFrequency.length - SHIFT_STEPS; i++) {
                    newCPUFreqTable[i] = this.mSupportedCPUFrequency[i + SHIFT_STEPS];
                }
                this.mSupportedCPUFrequency = newCPUFreqTable;
            }
        }
    }

    private void adjustCPUCoreTable() {
        if (this.mSupportedCPUCoreNum != null) {
            int SHIFT_STEPS = 0;
            if ("isla".equals(BASE_MODEL) || "carmen2".equals(BASE_MODEL)) {
                SHIFT_STEPS = 1;
            }
            if (SHIFT_STEPS > 0 && this.mSupportedCPUCoreNum.length > SHIFT_STEPS) {
                int[] newCPUCoreTable = new int[(this.mSupportedCPUCoreNum.length - SHIFT_STEPS)];
                for (int i = 0; i < this.mSupportedCPUCoreNum.length - SHIFT_STEPS; i++) {
                    newCPUCoreTable[i] = this.mSupportedCPUCoreNum[i + SHIFT_STEPS];
                }
                this.mSupportedCPUCoreNum = newCPUCoreTable;
            }
        }
    }

    private void adjustGPUFreqTable() {
        if (this.mSupportedGPUFrequency != null) {
            int SHIFT_STEPS = 0;
            if ("ha".equals(BASE_MODEL) || "ka".equals(BASE_MODEL) || "sa".equals(BASE_MODEL) || "ta".equals(BASE_MODEL) || "hrl".equals(BASE_MODEL) || "hrq".equals(BASE_MODEL)) {
                SHIFT_STEPS = 2;
            } else if (BASE_MODEL.equals(BASE_MODEL)) {
                SHIFT_STEPS = 3;
            }
            if (SHIFT_STEPS > 0 && this.mSupportedGPUFrequency.length > SHIFT_STEPS) {
                int[] newGPUFreqTable = new int[(this.mSupportedGPUFrequency.length - SHIFT_STEPS)];
                for (int i = 0; i < this.mSupportedGPUFrequency.length - SHIFT_STEPS; i++) {
                    newGPUFreqTable[i] = this.mSupportedGPUFrequency[i + SHIFT_STEPS];
                }
                this.mSupportedGPUFrequency = newGPUFreqTable;
            }
        }
    }

    public int[] getSupportedCPUFrequency() {
        return this.mSupportedCPUFrequency;
    }

    public int[] getSupportedCPUFrequencyForSSRM() {
        return this.mSupportedCPUFrequencyForSSRM;
    }

    public int getApproximateCPUFrequency(int freq) {
        if (this.mSupportedCPUFrequency == null || freq < 0) {
            return -1;
        }
        int length = this.mSupportedCPUFrequency.length;
        if (length <= 0) {
            return -1;
        }
        int realFreq = this.mSupportedCPUFrequency[0];
        while (length > 0) {
            if (this.mSupportedCPUFrequency[length - 1] >= freq) {
                return this.mSupportedCPUFrequency[length - 1];
            }
            length--;
        }
        return realFreq;
    }

    public int getApproximateCPUFrequencyByPercentOfMaximum(double percent) {
        if (this.mSupportedCPUFrequency == null || percent < 0.0d || percent > 1.0d || this.mSupportedCPUFrequency.length <= 0) {
            return -1;
        }
        return getApproximateCPUFrequency((int) (((double) this.mSupportedCPUFrequency[0]) * percent));
    }

    public int getApproximateCPUFrequencyForSSRM(int freq) {
        if (this.mSupportedCPUFrequencyForSSRM == null || freq < 0) {
            return -1;
        }
        int length = this.mSupportedCPUFrequencyForSSRM.length;
        if (length <= 0) {
            return -1;
        }
        int realFreq = this.mSupportedCPUFrequencyForSSRM[0];
        while (length > 0) {
            if (this.mSupportedCPUFrequencyForSSRM[length - 1] >= freq) {
                return this.mSupportedCPUFrequencyForSSRM[length - 1];
            }
            length--;
        }
        return realFreq;
    }

    public int getApproximateCPUFrequencyByPercentOfMaximumForSSRM(double percent) {
        if (this.mSupportedCPUFrequencyForSSRM != null && this.mSupportedCPUFrequencyForSSRM.length > 0) {
            return getApproximateCPUFrequencyForSSRM((int) (((double) this.mSupportedCPUFrequencyForSSRM[0]) * percent));
        }
        return -1;
    }

    public int[] getSupportedCPUCoreNum() {
        return this.mSupportedCPUCoreNum;
    }

    public int[] getSupportedCPUCoreNumForSSRM() {
        return this.mSupportedCPUCoreNumForSSRM;
    }

    public int[] getSupportedGPUFrequency() {
        return this.mSupportedGPUFrequency;
    }

    public int getApproximateGPUFrequency(int freq) {
        if (this.mSupportedGPUFrequency == null || freq < 0) {
            return -1;
        }
        int length = this.mSupportedGPUFrequency.length;
        if (length <= 0) {
            return -1;
        }
        int realFreq = this.mSupportedGPUFrequency[0];
        while (length > 0) {
            if (this.mSupportedGPUFrequency[length - 1] >= freq) {
                return this.mSupportedGPUFrequency[length - 1];
            }
            length--;
        }
        return realFreq;
    }

    public int getApproximateGPUFrequencyByPercentOfMaximum(double percent) {
        if (this.mSupportedGPUFrequency == null || percent < 0.0d || percent > 1.0d || this.mSupportedGPUFrequency.length <= 0) {
            return -1;
        }
        return getApproximateGPUFrequency((int) (((double) this.mSupportedGPUFrequency[0]) * percent));
    }

    public int[] getSupportedGPUFrequencyForSSRM() {
        return this.mSupportedGPUFrequencyForSSRM;
    }

    public int[] getSupportedBUSFrequency() {
        return this.mSupportedBUSFrequency;
    }

    public int getApproximateBUSFrequency(int freq) {
        if (this.mSupportedBUSFrequency == null || freq < 0) {
            return -1;
        }
        int length = this.mSupportedBUSFrequency.length;
        if (length <= 0) {
            return -1;
        }
        int realFreq = this.mSupportedBUSFrequency[0];
        while (length > 0) {
            if (this.mSupportedBUSFrequency[length - 1] >= freq) {
                return this.mSupportedBUSFrequency[length - 1];
            }
            length--;
        }
        return realFreq;
    }

    public int getApproximateBUSFrequencyByPercentOfMaximum(double percent) {
        if (this.mSupportedBUSFrequency == null || percent < 0.0d || percent > 1.0d || this.mSupportedBUSFrequency.length <= 0) {
            return -1;
        }
        return getApproximateBUSFrequency((int) (((double) this.mSupportedBUSFrequency[0]) * percent));
    }

    public void acquire(String actionName) {
        int timeout = this.mModel.getTimeoutForAction(actionName);
        if (timeout > 0) {
            acquire(timeout);
        }
    }

    public void acquire() {
        acquire(-1);
    }

    public void acquire(int timeout) {
        if (!this.mIsHintNotifier) {
            acquireImpl(timeout);
        } else if (timeout > 0 || this.mHintTimeout > 0) {
            Iterator i$ = this.mHintList.iterator();
            while (i$.hasNext()) {
                DVFSHelper helper = (DVFSHelper) i$.next();
                if (timeout > 0) {
                    helper.acquireImpl(timeout);
                } else {
                    helper.acquireImpl(this.mHintTimeout);
                }
            }
        } else {
            logOnEng(LOG_TAG, "acquire:: Either timeout or mHintTimeout should have the proper number greater than zero");
        }
    }

    private void acquireImpl(int timeout) {
        if (this.mCustomFreqManager != null) {
            ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
            try {
                logOnEng(LOG_TAG, "acquire:: timeout = " + timeout + ", mIsAcquired = " + this.mIsAcquired);
                if (this.mIsAcquired) {
                    logOnEng(LOG_TAG, "acquire:: DVFS lock is already acquired. Previous lock will be released first.");
                    release();
                }
                Bundle bundle = null;
                if (this.mIntentExtra != null) {
                    bundle = this.mIntentExtra.getExtras();
                }
                int freq;
                String cpuOption;
                int coreNum;
                String stringCoreNum;
                String gpuOption;
                String busOption;
                switch (this.mType) {
                    case 12:
                        if (this.mSupportedCPUFrequency != null) {
                            freq = -1;
                            if (bundle != null) {
                                cpuOption = bundle.getString("CPU", null);
                                if (cpuOption != null) {
                                    freq = Integer.parseInt(cpuOption);
                                }
                            }
                            if ("ja".equals(BASE_MODEL) && freq > 1600000) {
                                freq = getApproximateCPUFrequency(1600000);
                            }
                            if (freq != -1) {
                                this.cpuRequest = this.mCustomFreqManager.newFrequencyRequest(6, freq, (long) timeout, this.mPkgName, this.mContext);
                                break;
                            }
                        }
                        break;
                    case 13:
                        if (this.mSupportedCPUFrequency != null) {
                            freq = -1;
                            if (bundle != null) {
                                cpuOption = bundle.getString("CPU", null);
                                if (cpuOption != null) {
                                    freq = Integer.parseInt(cpuOption);
                                }
                            }
                            if (freq != -1) {
                                this.cpuRequest = this.mCustomFreqManager.newFrequencyRequest(7, freq, (long) timeout, this.mPkgName, this.mContext);
                                break;
                            }
                        }
                        break;
                    case 14:
                        if (this.mSupportedCPUCoreNum != null) {
                            coreNum = 0;
                            stringCoreNum = null;
                            if (bundle != null) {
                                stringCoreNum = bundle.getString("CORE_NUM", null);
                            }
                            if (stringCoreNum != null) {
                                coreNum = Integer.parseInt(stringCoreNum);
                            }
                            if (coreNum > 0) {
                                this.cpuNumRequest = this.mCustomFreqManager.newFrequencyRequest(4, coreNum, (long) timeout, this.mPkgName, this.mContext);
                                break;
                            }
                        }
                        break;
                    case 15:
                        if (this.mSupportedCPUCoreNum != null) {
                            coreNum = 0;
                            stringCoreNum = null;
                            if (bundle != null) {
                                try {
                                    stringCoreNum = bundle.getString("CORE_NUM", null);
                                } catch (NullPointerException e2) {
                                    e2.printStackTrace();
                                }
                            }
                            if (stringCoreNum != null) {
                                coreNum = Integer.parseInt(stringCoreNum);
                            }
                            if (coreNum > 0 || "msm8992".equals(BOARD_PLATFORM)) {
                                this.cpuNumRequest = this.mCustomFreqManager.newFrequencyRequest(5, coreNum, (long) timeout, this.mPkgName, this.mContext);
                                break;
                            }
                        }
                        break;
                    case 16:
                        if (this.mSupportedGPUFrequency != null) {
                            freq = -1;
                            if (bundle != null) {
                                gpuOption = bundle.getString("GPU", null);
                                if (gpuOption != null) {
                                    freq = Integer.parseInt(gpuOption);
                                }
                            }
                            if (freq != -1) {
                                this.gpuRequest = this.mCustomFreqManager.newFrequencyRequest(1, freq, (long) timeout, this.mPkgName, this.mContext);
                                break;
                            }
                        }
                        break;
                    case 17:
                        if (this.mSupportedGPUFrequency != null) {
                            freq = -1;
                            if (bundle != null) {
                                gpuOption = bundle.getString("GPU", null);
                                if (gpuOption != null) {
                                    freq = Integer.parseInt(gpuOption);
                                }
                            }
                            if (freq != -1) {
                                this.gpuRequest = this.mCustomFreqManager.newFrequencyRequest(9, freq, (long) timeout, this.mPkgName, this.mContext);
                                break;
                            }
                        }
                        break;
                    case 18:
                        if (this.mmcRequest != null) {
                            this.mmcRequest.cancelFrequencyRequest();
                            this.mmcRequest = null;
                        }
                        this.mmcRequest = this.mCustomFreqManager.newFrequencyRequest(8, -1, (long) timeout, this.mPkgName, this.mContext);
                        if (this.mmcRequest != null) {
                            this.mmcRequest.doFrequencyRequest();
                            break;
                        }
                        break;
                    case 19:
                        if (this.mSupportedBUSFrequency != null) {
                            int busMinfreq = -1;
                            if (bundle != null) {
                                busOption = bundle.getString("BUS", null);
                                if (busOption != null) {
                                    busMinfreq = Integer.parseInt(busOption);
                                }
                            }
                            if (busMinfreq != -1) {
                                this.busRequest = this.mCustomFreqManager.newFrequencyRequest(10, busMinfreq, (long) timeout, this.mPkgName, this.mContext);
                                break;
                            }
                        }
                        break;
                    case 20:
                        if (this.mSupportedBUSFrequency != null) {
                            int busMaxfreq = -1;
                            if (bundle != null) {
                                busOption = bundle.getString("BUS", null);
                                if (busOption != null) {
                                    busMaxfreq = Integer.parseInt(busOption);
                                }
                            }
                            if (busMaxfreq != -1) {
                                this.busRequest = this.mCustomFreqManager.newFrequencyRequest(11, busMaxfreq, (long) timeout, this.mPkgName, this.mContext);
                                break;
                            }
                        }
                        break;
                    case 21:
                        int fpsMax = 99;
                        if (bundle != null) {
                            String fpsOption = bundle.getString("FPS", null);
                            if (fpsOption != null) {
                                fpsMax = Integer.parseInt(fpsOption);
                            }
                        }
                        if (fpsMax >= 0 && fpsMax < 99) {
                            this.fpsRequest = this.mCustomFreqManager.newFrequencyRequest(3, fpsMax, (long) timeout, this.mPkgName, this.mContext);
                            break;
                        }
                    case 22:
                        this.cpuDisCStateRequest = this.mCustomFreqManager.newFrequencyRequest(12, 0, (long) timeout, this.mPkgName, this.mContext);
                        break;
                    case 23:
                        this.cpuLegacySchedulerRequest = this.mCustomFreqManager.newFrequencyRequest(13, 0, (long) timeout, this.mPkgName, this.mContext);
                        break;
                    case 24:
                        this.cpuHotplugDisableRequest = this.mCustomFreqManager.newFrequencyRequest(14, 0, (long) timeout, this.mPkgName, this.mContext);
                        break;
                    case 25:
                        this.pciePsmDisableRequest = this.mCustomFreqManager.newFrequencyRequest(15, 0, (long) timeout, this.mPkgName, this.mContext);
                        break;
                    default:
                        logOnEng(LOG_TAG, "acquire:: the request type doesn't implemented yet. mType = " + this.mType);
                        break;
                }
            } catch (NullPointerException e1) {
                e1.printStackTrace();
            } catch (Throwable th) {
                StrictMode.setThreadPolicy(oldPolicy);
            }
            if (this.cpuRequest != null) {
                this.cpuRequest.doFrequencyRequest();
            }
            if (this.cpuNumRequest != null) {
                this.cpuNumRequest.doFrequencyRequest();
            }
            if (this.gpuRequest != null) {
                this.gpuRequest.doFrequencyRequest();
            }
            if (this.busRequest != null) {
                this.busRequest.doFrequencyRequest();
            }
            if (this.fpsRequest != null) {
                this.fpsRequest.doFrequencyRequest();
            }
            if (this.cpuDisCStateRequest != null) {
                this.cpuDisCStateRequest.doFrequencyRequest();
            }
            if (this.cpuLegacySchedulerRequest != null) {
                this.cpuLegacySchedulerRequest.doFrequencyRequest();
            }
            if (this.cpuHotplugDisableRequest != null) {
                this.cpuHotplugDisableRequest.doFrequencyRequest();
            }
            if (this.pciePsmDisableRequest != null) {
                this.pciePsmDisableRequest.doFrequencyRequest();
            }
            this.mIsAcquired = true;
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    public void release() {
        if (this.mIsHintNotifier) {
            Iterator i$ = this.mHintList.iterator();
            while (i$.hasNext()) {
                ((DVFSHelper) i$.next()).releaseImpl();
            }
            return;
        }
        releaseImpl();
    }

    private void releaseImpl() {
        if (this.mCustomFreqManager != null) {
            ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
            try {
                logOnEng(LOG_TAG, "release:: mIsAcquired = " + this.mIsAcquired);
                if (this.mIsAcquired) {
                    if (this.cpuRequest != null) {
                        this.cpuRequest.cancelFrequencyRequest();
                        logOnEng(LOG_TAG, "release:: cpuRequest is released.");
                        this.cpuRequest = null;
                    }
                    if (this.cpuNumRequest != null) {
                        this.cpuNumRequest.cancelFrequencyRequest();
                        logOnEng(LOG_TAG, "release:: cpuNumRequest is released.");
                        this.cpuNumRequest = null;
                    }
                    if (this.gpuRequest != null) {
                        this.gpuRequest.cancelFrequencyRequest();
                        logOnEng(LOG_TAG, "release:: gpuRequest is released.");
                        this.gpuRequest = null;
                    }
                    if (this.busRequest != null) {
                        this.busRequest.cancelFrequencyRequest();
                        logOnEng(LOG_TAG, "release:: busRequest is released.");
                        this.busRequest = null;
                    }
                    if (this.mmcRequest != null) {
                        this.mmcRequest.cancelFrequencyRequest();
                        logOnEng(LOG_TAG, "release:: mmcRequest is released.");
                        this.mmcRequest = null;
                    }
                    if (this.fpsRequest != null) {
                        this.fpsRequest.cancelFrequencyRequest();
                        logOnEng(LOG_TAG, "release:: fpsRequest is released.");
                        this.fpsRequest = null;
                    }
                    if (this.cpuDisCStateRequest != null) {
                        this.cpuDisCStateRequest.cancelFrequencyRequest();
                        logOnEng(LOG_TAG, "release:: cpuDisCStateRequest is released.");
                        this.cpuDisCStateRequest = null;
                    }
                    if (this.cpuLegacySchedulerRequest != null) {
                        this.cpuLegacySchedulerRequest.cancelFrequencyRequest();
                        logOnEng(LOG_TAG, "release:: cpuLegacySchedulerRequest is released.");
                        this.cpuLegacySchedulerRequest = null;
                    }
                    if (this.cpuHotplugDisableRequest != null) {
                        this.cpuHotplugDisableRequest.cancelFrequencyRequest();
                        logOnEng(LOG_TAG, "release:: cpuHotplugDisableRequest is released.");
                        this.cpuHotplugDisableRequest = null;
                    }
                    if (this.pciePsmDisableRequest != null) {
                        this.pciePsmDisableRequest.cancelFrequencyRequest();
                        logOnEng(LOG_TAG, "release:: pciePsmDisableRequest is released.");
                        this.pciePsmDisableRequest = null;
                    }
                    this.mIsAcquired = false;
                    StrictMode.setThreadPolicy(oldPolicy);
                }
            } finally {
                StrictMode.setThreadPolicy(oldPolicy);
            }
        }
    }

    public void addExtraOption(String name, String value) {
        if (this.mIntentExtra == null) {
            this.mIntentExtra = new Intent();
        }
        this.mIntentExtra.putExtra(name, value);
    }

    public void addExtraOption(String name, long value) {
        if (this.mIntentExtra == null) {
            this.mIntentExtra = new Intent();
        }
        this.mIntentExtra.putExtra(name, value + ProxyInfo.LOCAL_EXCL_LIST);
    }

    public void cancelExtraOptions() {
        this.mIntentExtra = null;
    }

    public void addExtraOptionsByDefaultPolicy(String actionName) {
        if (this.mCustomFreqManager != null) {
            int freq;
            int coreNum;
            if (ACTION_AMS_RESUME.equals(actionName)) {
                if (this.mType == 12) {
                    freq = this.mModel.getAMSResumeCPUFreq();
                    if (freq > 0) {
                        addExtraOption("CPU", (long) getApproximateCPUFrequencyForSSRM(freq));
                    }
                } else if (this.mType == 16) {
                    freq = this.mModel.getAMSResumeGPUFreq();
                    if (freq > 0) {
                        addExtraOption("GPU", (long) freq);
                    }
                } else if (this.mType == 19) {
                    freq = this.mModel.getAMSResumeBUSFreq();
                    if (freq > 0) {
                        addExtraOption("BUS", (long) freq);
                    }
                } else if (this.mType == 14) {
                    coreNum = this.mModel.getAMSResumeCPUCore();
                    if (coreNum > 0) {
                        addExtraOption("CORE_NUM", (long) coreNum);
                    }
                }
            } else if (ACTION_GALLERY_TOUCH.equals(actionName)) {
                if (this.mType == 12) {
                    freq = this.mModel.getGalleryTouchCPUFreq();
                    if (freq > 0) {
                        addExtraOption("CPU", (long) getApproximateCPUFrequencyForSSRM(freq));
                    }
                } else if (this.mType == 19) {
                    freq = this.mModel.getGalleryTouchBUSFreq();
                    if (freq > 0) {
                        addExtraOption("BUS", (long) freq);
                    }
                }
            } else if (ACTION_BROWSER_TOUCH.equals(actionName)) {
                if (this.mType == 12) {
                    freq = this.mModel.getBrowserTouchCPUFreq();
                    if (freq > 0) {
                        addExtraOption("CPU", (long) getApproximateCPUFrequencyForSSRM(freq));
                    }
                } else if (this.mType == 19) {
                    freq = this.mModel.getBrowserTouchBUSFreq();
                    if (freq > 0) {
                        addExtraOption("BUS", (long) freq);
                    }
                }
            } else if (ACTION_LAUNCHER_TOUCH.equals(actionName)) {
                if (this.mType == 12) {
                    freq = this.mModel.getLauncherTouchCPUFreq();
                    if (freq > 0) {
                        addExtraOption("CPU", (long) getApproximateCPUFrequencyForSSRM(freq));
                    }
                } else if (this.mType == 19) {
                    freq = this.mModel.getLauncherTouchBUSFreq();
                    if (freq > 0) {
                        addExtraOption("BUS", (long) freq);
                    }
                } else if (this.mType == 16) {
                    freq = this.mModel.getLauncherTouchGPUFreq();
                    if (freq > 0) {
                        addExtraOption("GPU", (long) freq);
                    }
                } else if (this.mType == 14) {
                    coreNum = this.mModel.getLauncherTouchCPUCore();
                    if (coreNum > 0) {
                        addExtraOption("CORE_NUM", (long) coreNum);
                    }
                }
            } else if (ACTION_LISTVIEW_SCROLL.equals(actionName)) {
                if (this.mType == 12) {
                    freq = this.mModel.getListScrollCPUFreq();
                    if (freq > 0) {
                        addExtraOption("CPU", (long) getApproximateCPUFrequencyForSSRM(freq));
                    }
                } else if (this.mType == 16) {
                    freq = this.mModel.getListScrollGPUFreq();
                    if (freq > 0) {
                        addExtraOption("GPU", (long) freq);
                    }
                } else if (this.mType == 19) {
                    freq = this.mModel.getListScrollBUSFreq();
                    if (freq > 0) {
                        addExtraOption("BUS", (long) freq);
                    }
                }
            } else if (ACTION_PWM_ROTATION.equals(actionName)) {
                freq = this.mModel.getRotationCPUFreq();
                if (freq > 0) {
                    addExtraOption("CPU", (long) getApproximateCPUFrequencyForSSRM(freq));
                }
            } else if (ACTION_LAUNCHER_HOMEMENU.equals(actionName)) {
                if (this.mType == 16) {
                    freq = this.mModel.getLauncherTouchGPUFreq();
                    if (freq > 0) {
                        addExtraOption("GPU", (long) freq);
                    }
                }
            } else if (ACTION_SHAREMUSIC_GROUPPLAY.equals(actionName)) {
                freq = this.mModel.getShareMusicCPUFreq();
                if (freq > 0) {
                    addExtraOption("CPU", (long) getApproximateCPUFrequencyForSSRM(freq));
                }
            } else if (ACTION_BROWSER_FLING.equals(actionName)) {
                freq = this.mModel.getBrowserFlingCpuFreq();
                if (freq > 0) {
                    addExtraOption("CPU", (long) getApproximateCPUFrequencyForSSRM(freq));
                }
            } else if (ACTION_APP_LAUNCH.equals(actionName)) {
                if (this.mType == 12) {
                    freq = this.mModel.getAppLaunchCPUFreq();
                    if (freq > 0) {
                        addExtraOption("CPU", (long) getApproximateCPUFrequencyForSSRM(freq));
                    }
                } else if (this.mType == 16) {
                    freq = this.mModel.getAppLaunchGPUFreq();
                    if (freq > 0) {
                        addExtraOption("GPU", (long) freq);
                    }
                } else if (this.mType == 19) {
                    freq = this.mModel.getAppLaunchBUSFreq();
                    if (freq > 0) {
                        addExtraOption("BUS", (long) freq);
                    }
                } else if (this.mType == 14) {
                    coreNum = this.mModel.getAppLaunchCPUCore();
                    if (coreNum > 0) {
                        addExtraOption("CORE_NUM", (long) coreNum);
                    }
                }
            } else if (ACTION_DEVICE_WAKEUP.equals(actionName) && this.mType == 12) {
                freq = this.mModel.getDeviceWakeupCPUFreq();
                if (freq > 0) {
                    addExtraOption("CPU", (long) getApproximateCPUFrequencyForSSRM(freq));
                }
            }
        }
    }

    public static void logOnEng(String tag, String msg) {
        if (sIsDebugLevelHigh) {
            Log.i(tag, msg);
        }
    }

    private boolean isPackageExistInAppLaunch(String pkg) {
        for (String s : this.mAppLaunchPackages) {
            if (pkg.contains(s)) {
                return true;
            }
        }
        return false;
    }

    private String x(int[] e) {
        StringBuilder sb = new StringBuilder();
        for (int i : e) {
            sb.append((char) (i ^ 122));
        }
        return sb.toString();
    }

    public void onAppLaunchEvent(Context context, String packageName) {
        if (packageName != null) {
            if (this.mAppLaunchCPUBooster == null) {
                this.mAppLaunchCPUBooster = new DVFSHelper(context, "LAUNCHER_APP_BOOSTER_CPU", 12, 0);
                this.mCPUFrequencyTable = this.mAppLaunchCPUBooster.getSupportedCPUFrequency();
                if (this.mCPUFrequencyTable != null) {
                    this.mAppLaunchCPUBooster.addExtraOptionsByDefaultPolicy(ACTION_APP_LAUNCH);
                } else {
                    logOnEng(LOG_TAG, "onAppLaunchEvent:: mCPUFrequencyTable is null");
                }
            }
            if (this.mAppLaunchCPUCoreNumBooster == null) {
                this.mAppLaunchCPUCoreNumBooster = new DVFSHelper(context, "LAUNCHER_APP_BOOSTER_CORE", 14, 0);
                this.mCPUCoreTable = this.mAppLaunchCPUCoreNumBooster.getSupportedCPUCoreNum();
                if (this.mCPUCoreTable != null) {
                    this.mAppLaunchCPUCoreNumBooster.addExtraOptionsByDefaultPolicy(ACTION_APP_LAUNCH);
                } else {
                    logOnEng(LOG_TAG, "onAppLaunchEvent:: mCPUCoreTable is null");
                }
            }
            if (this.mAppLaunchGPUBooster == null) {
                this.mAppLaunchGPUBooster = new DVFSHelper(context, "LAUNCHER_APP_BOOSTER_GPU", 16, 0);
                this.mGPUFrequencyTable = this.mAppLaunchGPUBooster.getSupportedGPUFrequency();
                if (this.mGPUFrequencyTable != null) {
                    this.mAppLaunchGPUBooster.addExtraOptionsByDefaultPolicy(ACTION_APP_LAUNCH);
                } else {
                    logOnEng(LOG_TAG, "onAppLaunchEvent:: mGPUFrequencyTable is null");
                }
            }
            if (this.mAppLaunchBUSBooster == null) {
                this.mAppLaunchBUSBooster = new DVFSHelper(context, "LAUNCHER_APP_BOOSTER_BUS", 19, 0);
                this.mBUSFrequencyTable = this.mAppLaunchBUSBooster.getSupportedBUSFrequency();
                if (this.mBUSFrequencyTable != null) {
                    this.mAppLaunchBUSBooster.addExtraOptionsByDefaultPolicy(ACTION_APP_LAUNCH);
                } else {
                    logOnEng(LOG_TAG, "onAppLaunchEvent:: mBUSFrequencyTable is null");
                }
            }
            if (this.mAppLaunchCState == null) {
                this.mAppLaunchCState = new DVFSHelper(context, "LAUNCHER_APP_BOOSTER_CSTATE", 22, 0);
            }
            if ("hf".equals(BASE_MODEL) || "ha".equals(BASE_MODEL) || "kam".equals(BASE_MODEL)) {
                this.mAppLaunchBoostTime = 500;
            } else if ("hrl".equals(BASE_MODEL) || "hrq".equals(BASE_MODEL) || BASE_MODEL.equals(BASE_MODEL) || "kf".equals(BASE_MODEL) || "ka".equals(BASE_MODEL) || "kq".equals(BASE_MODEL) || "tf".equals(BASE_MODEL) || "ta".equals(BASE_MODEL) || "sf".equals(BASE_MODEL) || "sa".equals(BASE_MODEL) || "zq".equals(BASE_MODEL) || "tr3ca".equals(BASE_MODEL)) {
                if (packageName.contains("com.sec.android.app.camera")) {
                    this.mAppLaunchBoostTime = 2000;
                } else if (isPackageExistInAppLaunch(packageName)) {
                    this.mAppLaunchBoostTime = 4000;
                } else if (!"sf".equals(BASE_MODEL) && !"sa".equals(BASE_MODEL)) {
                    this.mAppLaunchBoostTime = 1000;
                } else if (SIOP_MODEL.contains("_a8e_") || SIOP_MODEL.contains("_a8hp_")) {
                    this.mAppLaunchBoostTime = 2000;
                } else {
                    this.mAppLaunchBoostTime = 500;
                }
            } else if (!"msm8916".equals(BOARD_PLATFORM) && !"island".equals(BASE_MODEL) && !"isla".equals(BASE_MODEL) && !"carmen2".equals(BASE_MODEL) && !"islaquad".equals(BASE_MODEL) && !BOARD_PLATFORM.startsWith("mrvl") && !BOARD_PLATFORM.startsWith("sc") && !"msm8992".equals(BOARD_PLATFORM)) {
                this.mAppLaunchBoostTime = 2000;
            } else if (isPackageExistInAppLaunch(packageName)) {
                this.mAppLaunchBoostTime = 4000;
            } else {
                this.mAppLaunchBoostTime = 2000;
            }
            if (this.mCPUFrequencyTable != null) {
                this.mAppLaunchCPUBooster.acquire(this.mAppLaunchBoostTime);
            }
            if (this.mCPUCoreTable != null) {
                this.mAppLaunchCPUCoreNumBooster.acquire(this.mAppLaunchBoostTime);
            }
            if (this.mGPUFrequencyTable != null && ("pxa1088".equals(BASE_MODEL) || "pxa1908".equals(BASE_MODEL) || "pxa1936".equals(HARDWARE_NAME))) {
                this.mAppLaunchGPUBooster.acquire(this.mAppLaunchBoostTime);
            }
            if (this.mBUSFrequencyTable != null && ("hrq".equals(BASE_MODEL) || "tf".equals(BASE_MODEL) || "kf".equals(BASE_MODEL) || "pxa1088".equals(BASE_MODEL) || "pxa1908".equals(BASE_MODEL) || "kmini".equals(BASE_MODEL) || "pxa1936".equals(HARDWARE_NAME) || "MSM8976".equals(CHIP_NAME))) {
                this.mAppLaunchBUSBooster.acquire(this.mAppLaunchBoostTime);
            }
            this.mAppLaunchCState.acquire(this.mAppLaunchBoostTime);
        }
    }

    public void onWindowRotationEvent(Context context, String packageName) {
        if (this.mRotationCPUCoreNumBooster == null) {
            this.mRotationCPUCoreNumBooster = new DVFSHelper(context, 14);
            int[] coreTable = this.mRotationCPUCoreNumBooster.getSupportedCPUCoreNum();
            if (coreTable != null) {
                this.mRotationCPUCoreNumBooster.addExtraOption("CORE_NUM", (long) coreTable[0]);
            } else {
                logOnEng(LOG_TAG, "onWindowRotationEvent:: coreTable is null");
            }
        }
        if (this.mRotationCPUCoreNumBooster != null) {
            this.mRotationCPUCoreNumBooster.acquire(500);
        }
        if ("exynos4".equals(BOARD_PLATFORM) || "exynos5".equals(BOARD_PLATFORM) || (("hf".equals(BASE_MODEL) && "tablet".equals(DEVICE_TYPE)) || "pxa1088".equals(BASE_MODEL) || "pxa1908".equals(BASE_MODEL) || "msm8226".equals(BOARD_PLATFORM) || "pxa1936".equals(HARDWARE_NAME))) {
            if (this.mRotationGPUBooster == null) {
                this.mRotationGPUBooster = new DVFSHelper(context, 16);
                int[] gpuTable = this.mRotationGPUBooster.getSupportedGPUFrequency();
                if (gpuTable == null) {
                    logOnEng(LOG_TAG, "onWindowRotationEvent:: gpuTable is null");
                } else if (SIOP_MODEL.contains(BASE_MODEL) || "exynos4".equals(BOARD_PLATFORM)) {
                    this.mRotationGPUBooster.addExtraOption("GPU", (long) gpuTable[gpuTable.length - 2]);
                } else if ("pxa1088".equals(BASE_MODEL) || "pxa1908".equals(BASE_MODEL) || "msm8226".equals(BOARD_PLATFORM) || "pxa1936".equals(HARDWARE_NAME)) {
                    this.mRotationGPUBooster.addExtraOption("GPU", (long) gpuTable[0]);
                } else {
                    this.mRotationGPUBooster.addExtraOption("GPU", (long) gpuTable[1]);
                }
            }
            if (this.mRotationGPUBooster != null) {
                this.mRotationGPUBooster.acquire(2000);
            }
        }
        if ("pxa1088".equals(BASE_MODEL) || "pxa1908".equals(BASE_MODEL) || "pxa1936".equals(HARDWARE_NAME)) {
            if (this.mRotationBUSBooster == null) {
                this.mRotationBUSBooster = new DVFSHelper(context, 19);
                int[] busTable = this.mRotationBUSBooster.getSupportedBUSFrequency();
                if (busTable != null) {
                    this.mRotationBUSBooster.addExtraOption("BUS", (long) busTable[0]);
                } else {
                    logOnEng(LOG_TAG, "onWindowRotationEvent:: busTable is null");
                }
            }
            if (this.mRotationBUSBooster != null) {
                this.mRotationBUSBooster.acquire(2000);
            }
        }
    }

    public void onActivityResumeEvent(Context context, String packageName, int type) {
        if (mAMSCState == null) {
            mAMSCState = new DVFSHelper(context, packageName, 22, 0);
        }
        if (mAMSCStateTail == null) {
            mAMSCStateTail = new DVFSHelper(context, packageName, 22, 0);
        }
        switch (type) {
            case 1:
                if (mAMSCState != null) {
                    mAMSCState.acquire();
                    return;
                }
                return;
            case 2:
                if (mAMSCState != null) {
                    mAMSCState.release();
                    return;
                }
                return;
            case 3:
                if (mAMSCStateTail != null) {
                    mAMSCStateTail.acquire(AMS_RESUME_TAIL_BOOST_TIMEOUT);
                    return;
                }
                return;
            default:
                try {
                    logOnEng(LOG_TAG, "onActivityResumeEvent:: type is not defined");
                    return;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
        }
        e.printStackTrace();
    }

    private Model createModel() {
        if ("hrq".equals(BASE_MODEL)) {
            return new ModelHRQ();
        }
        if ("hrl".equals(BASE_MODEL)) {
            return new ModelHRL();
        }
        if (BASE_MODEL.equals(BASE_MODEL)) {
            return new ModelZL();
        }
        if ("msm8992".equals(BOARD_PLATFORM)) {
            return new ModelMSM8992();
        }
        if ("jf".equals(BASE_MODEL)) {
            return new ModelJF();
        }
        if ("ja".equals(BASE_MODEL)) {
            if (SIOP_MODEL.contains("ja_kor")) {
                return new ModelJAKOR();
            }
            return new ModelJA();
        } else if ("hf".equals(BASE_MODEL)) {
            return new ModelHF();
        } else {
            if ("ha".equals(BASE_MODEL)) {
                if (SIOP_MODEL.contains("vienna")) {
                    return new ModelV13GWIFI();
                }
                if (SIOP_MODEL.contains("v2")) {
                    return new ModelV23GWIFI();
                }
                if (SIOP_MODEL.contains("picasso")) {
                    return new ModelPicasso3GWIFI();
                }
                return new ModelHA();
            } else if ("kf".equals(BASE_MODEL)) {
                return new ModelKF();
            } else {
                if ("ka".equals(BASE_MODEL)) {
                    return new ModelKA();
                }
                if ("kq".equals(BASE_MODEL)) {
                    return new ModelKQ();
                }
                if ("tf".equals(BASE_MODEL)) {
                    if (this.REGION_JPN) {
                        return new ModelTFJpn();
                    }
                    return new ModelTF();
                } else if ("ta".equals(BASE_MODEL)) {
                    return new ModelTA();
                } else {
                    if ("sf".equals(BASE_MODEL)) {
                        return new ModelSF();
                    }
                    if ("sa".equals(BASE_MODEL)) {
                        if (SIOP_MODEL.contains("_a8e_")) {
                            return new ModelA8E();
                        }
                        return new ModelSA();
                    } else if ("clovertrail".equals(BOARD_PLATFORM)) {
                        return new ModelSantos10();
                    } else {
                        if ("island".equals(BASE_MODEL)) {
                            if (SIOP_MODEL.contains("novel")) {
                                return new ModelNOVEL();
                            }
                            return new ModelISLAND();
                        } else if ("isla".equals(BASE_MODEL)) {
                            return new ModelISLA();
                        } else {
                            if ("carmen2".equals(BASE_MODEL)) {
                                return new ModelCARMEN2();
                            }
                            if ("islaquad".equals(BASE_MODEL)) {
                                return new ModelISLAQUAD();
                            }
                            if ("exynos4".equals(BOARD_PLATFORM)) {
                                if ("pp".equals(BASE_MODEL)) {
                                    return new ModelPP();
                                }
                                if ("m0".equals(BASE_MODEL)) {
                                    return new ModelM0();
                                }
                                return new ModelExynos4();
                            } else if ("kam".equals(BASE_MODEL)) {
                                return new ModelKAM();
                            } else {
                                if (SIOP_MODEL.contains("d2")) {
                                    return new ModelD2();
                                }
                                if ("hawaii".equals(BOARD_PLATFORM)) {
                                    return new ModelHawaii();
                                }
                                if ("msm8226".equals(BOARD_PLATFORM)) {
                                    return new ModelMSM8x26();
                                }
                                if ("pxa1936".equals(HARDWARE_NAME)) {
                                    return new ModelPXA1936();
                                }
                                if ("pxa1088".equals(BASE_MODEL)) {
                                    return new ModelPXA1088();
                                }
                                if ("pxa1908".equals(BASE_MODEL)) {
                                    return new ModelPXA1908();
                                }
                                if ("kmini".equals(BASE_MODEL)) {
                                    if (SIOP_MODEL.contains("degaslte")) {
                                        return new ModelDegasLTE();
                                    }
                                    return new ModelKMINI();
                                } else if ("MSM8930AB".equals(CHIP_NAME)) {
                                    return new Model8930AB();
                                } else {
                                    if ("msm8952".equals(BOARD_PLATFORM)) {
                                        if ("MSM8976".equals(CHIP_NAME)) {
                                            return new ModelMSM8976();
                                        }
                                        return new ModelMSM8952();
                                    } else if ("msm8916".equals(BOARD_PLATFORM)) {
                                        if (SIOP_MODEL.contains("a3")) {
                                            return new ModelMSM8916_A3();
                                        }
                                        if ("MSM8939".equals(CHIP_NAME)) {
                                            return new ModelMSM8939();
                                        }
                                        if ("MSM8929".equals(CHIP_NAME)) {
                                            return new ModelMSM8929();
                                        }
                                        return new ModelMSM8916();
                                    } else if ("tr3ca".equals(BASE_MODEL)) {
                                        return new ModelTR3CA();
                                    } else {
                                        if ("core33g".equals(BASE_MODEL)) {
                                            return new ModelCORE33G();
                                        }
                                        if ("vivalto3mve".equals(BASE_MODEL)) {
                                            return new ModelVIVALTO3MVE();
                                        }
                                        return new ModelJBP();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void onScrollEvent(boolean isScroll) {
        try {
            if (sCfmsService == null) {
                IBinder b = ServiceManager.getService(Context.CFMS_SERVICE);
                if (b != null) {
                    sCfmsService = Stub.asInterface(b);
                }
            }
            if (sCfmsService != null) {
                sCfmsService.sendCommandToSSRM("TYPE_SCROLL", isScroll ? "TRUE" : "FALSE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onSmoothScrollEvent(boolean isScroll) {
        try {
            if (sCfmsService == null) {
                IBinder b = ServiceManager.getService(Context.CFMS_SERVICE);
                if (b != null) {
                    sCfmsService = Stub.asInterface(b);
                }
            }
            if (sCfmsService != null) {
                sCfmsService.sendCommandToSSRM("SMOOTH_SCROLL", isScroll ? "TRUE" : "FALSE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isAquired() {
        return this.mIsAcquired;
    }

    public static DVFSHelper createCpuBooster(Context context, String hint) {
        DVFSHelper instance = new DVFSHelper(context, hint, 12, 0);
        int[] table = instance.getSupportedCPUFrequency();
        if (table != null) {
            instance.addExtraOption("CPU", (long) table[0]);
        }
        return instance;
    }

    public static DVFSHelper createCpuLimiter(Context context, String hint) {
        return new DVFSHelper(context, hint, 13, 0);
    }

    public static DVFSHelper createGpuBooster(Context context, String hint) {
        DVFSHelper instance = new DVFSHelper(context, hint, 16, 0);
        int[] table = instance.getSupportedGPUFrequency();
        if (table != null) {
            instance.addExtraOption("GPU", (long) table[0]);
        }
        return instance;
    }

    public static DVFSHelper createGpuLimiter(Context context, String hint) {
        return new DVFSHelper(context, hint, 17, 0);
    }

    public static DVFSHelper createCpuCoreBooster(Context context, String hint) {
        DVFSHelper instance = new DVFSHelper(context, hint, 14, 0);
        int[] table = instance.getSupportedCPUCoreNum();
        if (table != null) {
            instance.addExtraOption("CORE_NUM", (long) table[0]);
        }
        return instance;
    }

    public static DVFSHelper createCpuCoreLimiter(Context context, String hint) {
        return new DVFSHelper(context, hint, 14, 0);
    }

    public static DVFSHelper createBusBooster(Context context, String hint) {
        DVFSHelper instance = new DVFSHelper(context, hint, 19, 0);
        int[] table = instance.getSupportedBUSFrequency();
        if (table != null) {
            instance.addExtraOption("BUS", (long) table[0]);
        }
        return instance;
    }

    public static DVFSHelper createBusLimiter(Context context, String hint) {
        return new DVFSHelper(context, hint, 20, 0);
    }

    public DVFSHelper setFrequency(long freq) {
        switch (this.mType) {
            case 12:
            case 13:
                addExtraOption("CPU", freq);
                break;
            case 14:
            case 15:
                addExtraOption("CORE_NUM", freq);
                break;
            case 16:
            case 17:
                addExtraOption("GPU", freq);
                break;
            case 19:
            case 20:
                addExtraOption("BUS", freq);
                break;
        }
        return this;
    }

    public DVFSHelper setFrequencyByPercent(int percent) {
        switch (this.mType) {
            case 12:
            case 13:
                addExtraOption("CPU", (long) null);
                break;
            case 14:
            case 15:
                addExtraOption("CORE_NUM", (long) null);
                break;
            case 16:
            case 17:
                addExtraOption("GPU", (long) null);
                break;
            case 19:
            case 20:
                addExtraOption("BUS", (long) null);
                break;
        }
        return this;
    }

    public static int getStandbyTimeInUltraPowerSavingMode() {
        try {
            if (sCfmsService == null) {
                IBinder b = ServiceManager.getService(Context.CFMS_SERVICE);
                if (b != null) {
                    sCfmsService = Stub.asInterface(b);
                }
            }
            if (sCfmsService != null) {
                return sCfmsService.getStandbyTimeInUltraPowerSavingMode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void sendCommandToSsrm(String type, String value) {
        try {
            if (sCfmsService == null) {
                IBinder b = ServiceManager.getService(Context.CFMS_SERVICE);
                if (b != null) {
                    sCfmsService = Stub.asInterface(b);
                }
            }
            if (sCfmsService != null) {
                sCfmsService.sendCommandToSSRM(type, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DVFSHelper() {
        boolean z;
        this.mContext = null;
        this.mPkgName = null;
        this.mIntentExtra = null;
        this.mType = 11;
        this.mCustomFreqManager = null;
        this.mSupportedCPUFrequency = null;
        this.mSupportedCPUFrequencyForSSRM = null;
        this.mSupportedCPUCoreNum = null;
        this.mSupportedCPUCoreNumForSSRM = null;
        this.mSupportedGPUFrequency = null;
        this.mSupportedGPUFrequencyForSSRM = null;
        this.mSupportedBUSFrequency = null;
        this.cpuRequest = null;
        this.cpuNumRequest = null;
        this.gpuRequest = null;
        this.busRequest = null;
        this.mmcRequest = null;
        this.fpsRequest = null;
        this.cpuDisCStateRequest = null;
        this.cpuLegacySchedulerRequest = null;
        this.cpuHotplugDisableRequest = null;
        this.pciePsmDisableRequest = null;
        this.mIsAcquired = false;
        if ((SIOP_MODEL != null && SIOP_MODEL.contains("jpn")) || "DCM".equals(SystemProperties.get("ro.csc.sales_code")) || "KDI".equals(SystemProperties.get("ro.csc.sales_code"))) {
            z = true;
        } else {
            z = false;
        }
        this.REGION_JPN = z;
        this.mAppLaunchCPUBooster = null;
        this.mAppLaunchGPUBooster = null;
        this.mAppLaunchBUSBooster = null;
        this.mAppLaunchCPUCoreNumBooster = null;
        this.mAppLaunchCState = null;
        this.mCPUFrequencyTable = null;
        this.mCPUCoreTable = null;
        this.mGPUFrequencyTable = null;
        this.mBUSFrequencyTable = null;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_LL = 4000;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_L = 2000;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_S = 500;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_M = 1000;
        this.mAppLaunchBoostTime = 2000;
        this.mAppLaunchPackages = new String[]{x(new int[]{25, 21, 23, 84, 9, 31, 25, 84, 27, 20, 30, 8, 21, 19, 30, 84, 27, 10, 10, 84, 9, 24, 8, 21, 13, 9, 31, 8}), x(new int[]{25, 21, 23, 84, 29, 21, 21, 29, 22, 31, 84, 27, 20, 30, 8, 21, 19, 30, 84, 27, 10, 10, 9, 84, 25, 18, 8, 21, 23, 31}), x(new int[]{25, 21, 23, 84, 27, 20, 30, 8, 21, 19, 30, 84, 24, 8, 21, 13, 9, 31, 8}), x(new int[]{25, 21, 23, 84, 29, 21, 21, 29, 22, 31, 84, 27, 20, 30, 8, 21, 19, 30, 84, 29, 23}), x(new int[]{25, 21, 23, 84, 9, 27, 23, 9, 15, 20, 29, 84, 27, 20, 30, 8, 21, 19, 30, 84, 31, 23, 27, 19, 22, 84, 15, 19}), x(new int[]{25, 21, 23, 84, 28, 27, 25, 31, 24, 21, 21, 17, 84, 17, 27, 14, 27, 20, 27}), x(new int[]{25, 21, 23, 84, 27, 20, 30, 8, 21, 19, 30, 84, 12, 31, 20, 30, 19, 20, 29}), x(new int[]{25, 21, 23, 84, 9, 27, 23, 9, 15, 20, 29, 84, 31, 12, 31, 8, 29, 22, 27, 30, 31, 9, 84, 12, 19, 30, 31, 21}), x(new int[]{25, 21, 23, 84, 9, 27, 23, 9, 15, 20, 29, 84, 27, 20, 30, 8, 21, 19, 30, 84, 12, 19, 30, 31, 21}), x(new int[]{25, 21, 23, 84, 9, 31, 25, 84, 27, 20, 30, 8, 21, 19, 30, 84, 29, 27, 22, 22, 31, 8, 3, 73, 30}), x(new int[]{25, 21, 23, 84, 29, 21, 21, 29, 22, 31, 84, 27, 20, 30, 8, 21, 19, 30, 84, 23, 27, 10, 9}), x(new int[]{25, 21, 23, 84, 24, 27, 19, 30, 15, 84, 27, 10, 10, 9, 31, 27, 8, 25, 18}), x(new int[]{25, 21, 23, 84, 9, 19, 20, 27, 84, 13, 31, 19, 24, 21}), x(new int[]{25, 21, 23, 84, 24, 27, 19, 30, 15, 84, 56, 27, 19, 30, 15, 55, 27, 10}), x(new int[]{25, 21, 23, 84, 14, 13, 19, 14, 14, 31, 8, 84, 27, 20, 30, 8, 21, 19, 30})};
        this.ROTATION_BOOSTING_TIMEOUT = 500;
        this.ROTATION_GPU_BOOSTING_TIMEOUT = 2000;
        this.mRotationCPUCoreNumBooster = null;
        this.mRotationGPUBooster = null;
        this.mRotationBUSBooster = null;
        this.mHintList = null;
        this.mIsHintNotifier = false;
        this.mHintTimeout = -1;
        this.mHintList = new ArrayList();
        this.mIsHintNotifier = true;
    }

    private void addHelper(DVFSHelper helper) {
        this.mHintList.add(helper);
    }

    public static DVFSHelper createHintNotifier(Context context, String hint) {
        if (sCfmsService == null) {
            try {
                sCfmsService = Stub.asInterface(ServiceManager.getService(Context.CFMS_SERVICE));
                if (sCfmsService == null) {
                    return null;
                }
            } catch (Exception e) {
                if (sIsDebugLevelHigh) {
                    Log.e(LOG_TAG, "createHintNotifier:: failed to get cfms service.");
                }
                if (sCfmsService == null) {
                    return null;
                }
            } catch (Throwable th) {
                if (sCfmsService == null) {
                    return null;
                }
            }
        }
        DVFSHelper notifier = new DVFSHelper();
        Intent policyIntent = null;
        try {
            policyIntent = sCfmsService.getDvfsPolicyByHint(hint);
        } catch (Exception e2) {
            Log.e(LOG_TAG, "createHintNotifier:: failed to call getDvfsPolicyByHint.");
        }
        if (policyIntent == null) {
            return notifier;
        }
        Bundle bundle = policyIntent.getExtras();
        for (String key : bundle.keySet()) {
            String value = bundle.getString(key);
            DVFSHelper newHelper = null;
            String moduleName = ProxyInfo.LOCAL_EXCL_LIST;
            int[] freqTable = new int[]{0};
            if ("CPU_MIN".equalsIgnoreCase(key)) {
                newHelper = new DVFSHelper(context, hint + "@CPU_MIN", 12, 0);
                moduleName = "CPU";
                freqTable = newHelper.getSupportedCPUFrequencyForSSRM();
            } else if ("GPU_MIN".equalsIgnoreCase(key)) {
                newHelper = new DVFSHelper(context, hint + "@GPU_MIN", 16, 0);
                moduleName = "GPU";
                freqTable = newHelper.getSupportedGPUFrequencyForSSRM();
            } else if ("BUS_MIN".equalsIgnoreCase(key)) {
                moduleName = "BUS";
                newHelper = new DVFSHelper(context, hint + "@BUS_MIN", 19, 0);
                freqTable = newHelper.getSupportedBUSFrequency();
            } else if ("CORE_NUM_MIN".equalsIgnoreCase(key)) {
                newHelper = new DVFSHelper(context, hint + "@CORE_NUM_MIN", 14, 0);
                moduleName = "CORE_NUM";
                freqTable = newHelper.getSupportedCPUCoreNum();
            } else if ("timeout".equalsIgnoreCase(key)) {
                notifier.mHintTimeout = Integer.parseInt(value);
            }
            if (newHelper != null) {
                if ("max".equalsIgnoreCase(value)) {
                    newHelper.addExtraOption(moduleName, (long) freqTable[0]);
                    if (sIsDebugLevelHigh) {
                        Log.e(LOG_TAG, "hint : " + hint + ", moduleName = " + moduleName + ", freq = " + freqTable[0]);
                    }
                } else {
                    if (!value.endsWith("%")) {
                        newHelper.addExtraOption(moduleName, (long) Integer.parseInt(value));
                        if (sIsDebugLevelHigh) {
                            Log.e(LOG_TAG, "hint : " + hint + ", moduleName = " + moduleName + ", freq = " + Integer.parseInt(value));
                        }
                    } else if ("CPU".equals(moduleName)) {
                        newHelper.addExtraOption(moduleName, (long) newHelper.getApproximateCPUFrequencyByPercentOfMaximum(Double.parseDouble(value.substring(0, value.indexOf("%"))) / 100.0d));
                        if (sIsDebugLevelHigh) {
                            Log.e(LOG_TAG, "hint : " + hint + ", moduleName = " + moduleName + ", freq = " + newHelper.getApproximateCPUFrequencyByPercentOfMaximum(Double.parseDouble(value.substring(0, value.indexOf("%"))) / 100.0d));
                        }
                    } else if ("GPU".equals(moduleName)) {
                        newHelper.addExtraOption(moduleName, (long) newHelper.getApproximateGPUFrequencyByPercentOfMaximum(Double.parseDouble(value.substring(0, value.indexOf("%"))) / 100.0d));
                        if (sIsDebugLevelHigh) {
                            Log.e(LOG_TAG, "hint : " + hint + ", moduleName = " + moduleName + ", freq = " + newHelper.getApproximateGPUFrequencyByPercentOfMaximum(Double.parseDouble(value.substring(0, value.indexOf("%"))) / 100.0d));
                        }
                    } else if ("BUS".equals(moduleName)) {
                        newHelper.addExtraOption(moduleName, (long) newHelper.getApproximateBUSFrequencyByPercentOfMaximum(Double.parseDouble(value.substring(0, value.indexOf("%"))) / 100.0d));
                        if (sIsDebugLevelHigh) {
                            Log.e(LOG_TAG, "hint : " + hint + ", moduleName = " + moduleName + ", freq = " + newHelper.getApproximateBUSFrequencyByPercentOfMaximum(Double.parseDouble(value.substring(0, value.indexOf("%"))) / 100.0d));
                        }
                    }
                }
                notifier.addHelper(newHelper);
            }
        }
        return notifier;
    }
}
