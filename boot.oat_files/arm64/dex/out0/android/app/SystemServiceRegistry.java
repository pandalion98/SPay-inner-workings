package android.app;

import android.R;
import android.accounts.AccountManager;
import android.accounts.IAccountManager.Stub;
import android.app.admin.DevicePolicyManager;
import android.app.job.IJobScheduler;
import android.app.job.JobScheduler;
import android.app.trust.TrustManager;
import android.app.usage.IUsageStatsManager;
import android.app.usage.NetworkStatsManager;
import android.app.usage.UsageStatsManager;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.IRestrictionsManager;
import android.content.RestrictionsManager;
import android.content.pm.ILauncherApps;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.ConsumerIrManager;
import android.hardware.ISerialManager;
import android.hardware.SensorManager;
import android.hardware.SerialManager;
import android.hardware.SystemSensorManager;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.IFingerprintService;
import android.hardware.hdmi.HdmiControlManager;
import android.hardware.hdmi.IHdmiControlService;
import android.hardware.input.InputManager;
import android.hardware.motion.MotionRecognitionManager;
import android.hardware.radio.RadioManager;
import android.hardware.scontext.SContextManager;
import android.hardware.usb.IUsbManager;
import android.hardware.usb.UsbManager;
import android.ktuca.IKtUcaIF;
import android.ktuca.KtUcaManager;
import android.location.CountryDetector;
import android.location.ICountryDetector;
import android.location.ILocationManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.media.midi.IMidiManager;
import android.media.midi.MidiManager;
import android.media.projection.MediaProjectionManager;
import android.media.session.MediaSessionManager;
import android.media.tv.ITvInputManager;
import android.media.tv.TvInputManager;
import android.net.ConnectivityManager;
import android.net.EthernetManager;
import android.net.IConnectivityManager;
import android.net.IEthernetManager;
import android.net.INetworkPolicyManager;
import android.net.NetworkPolicyManager;
import android.net.NetworkScoreManager;
import android.net.nsd.INsdManager;
import android.net.nsd.NsdManager;
import android.net.wifi.IRttManager;
import android.net.wifi.IWifiManager;
import android.net.wifi.IWifiOffloadManager;
import android.net.wifi.IWifiScanner;
import android.net.wifi.RttManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiOffloadManager;
import android.net.wifi.WifiScanner;
import android.net.wifi.hs20.IWifiHs20Manager;
import android.net.wifi.hs20.WifiHs20Manager;
import android.net.wifi.p2p.IWifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.CustomFrequencyManager;
import android.os.DropBoxManager;
import android.os.IBinder;
import android.os.ICustomFrequencyManager;
import android.os.IPersonaManager;
import android.os.IPowerManager;
import android.os.IRCPManager;
import android.os.IUserManager;
import android.os.PersonaManager;
import android.os.PowerManager;
import android.os.Process;
import android.os.RCPManager;
import android.os.ServiceManager;
import android.os.SystemVibrator;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.Vibrator;
import android.os.storage.StorageManager;
import android.print.IPrintManager;
import android.print.PrintManager;
import android.sec.clipboard.ClipboardExManager;
import android.service.iccc.IIcccManager;
import android.service.iccc.IcccManager;
import android.service.persistentdata.IPersistentDataBlockService;
import android.service.persistentdata.PersistentDataBlockManager;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.TextServicesManager;
import com.absolute.android.persistence.ABTPersistenceManager;
import com.absolute.android.persistence.IABTPersistence;
import com.android.internal.app.IAppOpsService;
import com.android.internal.appwidget.IAppWidgetService;
import com.android.internal.os.IDropBoxManagerService;
import com.android.internal.policy.PhoneLayoutInflater;
import com.samsung.android.allaroundsensing.AASManager;
import com.samsung.android.allaroundsensing.IAASManager;
import com.samsung.android.app.ExecuteManager;
import com.samsung.android.contextaware.ContextAwareManager;
import com.samsung.android.mdnie.IMdnieManager;
import com.samsung.android.mdnie.MdnieManager;
import com.samsung.android.mscs.IMSCSManager;
import com.samsung.android.mscs.MSCSManager;
import com.samsung.android.multiwindow.IMultiWindowFacade;
import com.samsung.android.multiwindow.MultiWindowFacade;
import com.samsung.android.sensorhub.SensorHubManager;
import com.samsung.android.smartclip.SpenGestureManager;
import com.samsung.location.SLocationManager;
import com.samsung.media.fmradio.FMPlayer;
import com.sec.android.app.CscFeature;
import com.sec.epdg.EpdgManager;
import com.sec.epdg.IEpdgManager;
import java.lang.reflect.Method;
import java.util.HashMap;

final class SystemServiceRegistry {
    private static final HashMap<String, ServiceFetcher<?>> SYSTEM_SERVICE_FETCHERS = new HashMap();
    private static final HashMap<Class<?>, String> SYSTEM_SERVICE_NAMES = new HashMap();
    private static final String TAG = "SystemServiceRegistry";
    private static int sServiceCacheSize;
    private static Object sVzwLocationManager;

    interface ServiceFetcher<T> {
        T getService(ContextImpl contextImpl);
    }

    static abstract class StaticServiceFetcher<T> implements ServiceFetcher<T> {
        private T mCachedInstance;

        public abstract T createService();

        StaticServiceFetcher() {
        }

        public final T getService(ContextImpl unused) {
            T t;
            synchronized (this) {
                if (this.mCachedInstance == null) {
                    this.mCachedInstance = createService();
                }
                t = this.mCachedInstance;
            }
            return t;
        }
    }

    static abstract class CachedServiceFetcher<T> implements ServiceFetcher<T> {
        private final int mCacheIndex = SystemServiceRegistry.access$108();

        public abstract T createService(ContextImpl contextImpl);

        public final T getService(ContextImpl ctx) {
            Object service;
            Object[] cache = ctx.mServiceCache;
            synchronized (cache) {
                service = cache[this.mCacheIndex];
                if (service == null) {
                    service = createService(ctx);
                    cache[this.mCacheIndex] = service;
                }
            }
            return service;
        }
    }

    static abstract class StaticOuterContextServiceFetcher<T> implements ServiceFetcher<T> {
        private T mCachedInstance;

        public abstract T createService(Context context);

        StaticOuterContextServiceFetcher() {
        }

        public final T getService(ContextImpl ctx) {
            T t;
            synchronized (this) {
                if (this.mCachedInstance == null) {
                    this.mCachedInstance = createService(ctx.getOuterContext());
                }
                t = this.mCachedInstance;
            }
            return t;
        }
    }

    static /* synthetic */ int access$108() {
        int i = sServiceCacheSize;
        sServiceCacheSize = i + 1;
        return i;
    }

    static {
        registerService(Context.ACCESSIBILITY_SERVICE, AccessibilityManager.class, new CachedServiceFetcher<AccessibilityManager>() {
            public AccessibilityManager createService(ContextImpl ctx) {
                return AccessibilityManager.getInstance(ctx);
            }
        });
        registerService(Context.CAPTIONING_SERVICE, CaptioningManager.class, new CachedServiceFetcher<CaptioningManager>() {
            public CaptioningManager createService(ContextImpl ctx) {
                return new CaptioningManager(ctx);
            }
        });
        registerService("account", AccountManager.class, new CachedServiceFetcher<AccountManager>() {
            public AccountManager createService(ContextImpl ctx) {
                return new AccountManager(ctx, Stub.asInterface(ServiceManager.getService("account")));
            }
        });
        registerService(Context.ACTIVITY_SERVICE, ActivityManager.class, new CachedServiceFetcher<ActivityManager>() {
            public ActivityManager createService(ContextImpl ctx) {
                return new ActivityManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        registerService(Context.ICCC_SERVICE, IcccManager.class, new CachedServiceFetcher<IcccManager>() {
            public IcccManager createService(ContextImpl ctx) {
                return new IcccManager(IIcccManager.Stub.asInterface(ServiceManager.getService(Context.ICCC_SERVICE)));
            }
        });
        registerService("alarm", AlarmManager.class, new CachedServiceFetcher<AlarmManager>() {
            public AlarmManager createService(ContextImpl ctx) {
                return new AlarmManager(IAlarmManager.Stub.asInterface(ServiceManager.getService("alarm")), ctx);
            }
        });
        registerService(Context.AUDIO_SERVICE, AudioManager.class, new CachedServiceFetcher<AudioManager>() {
            public AudioManager createService(ContextImpl ctx) {
                return new AudioManager(ctx);
            }
        });
        registerService(Context.MEDIA_ROUTER_SERVICE, MediaRouter.class, new CachedServiceFetcher<MediaRouter>() {
            public MediaRouter createService(ContextImpl ctx) {
                return new MediaRouter(ctx);
            }
        });
        registerService("bluetooth", BluetoothManager.class, new CachedServiceFetcher<BluetoothManager>() {
            public BluetoothManager createService(ContextImpl ctx) {
                return new BluetoothManager(ctx);
            }
        });
        registerService(Context.HDMI_CONTROL_SERVICE, HdmiControlManager.class, new StaticServiceFetcher<HdmiControlManager>() {
            public HdmiControlManager createService() {
                return new HdmiControlManager(IHdmiControlService.Stub.asInterface(ServiceManager.getService(Context.HDMI_CONTROL_SERVICE)));
            }
        });
        registerService(Context.CLIPBOARD_SERVICE, ClipboardManager.class, new CachedServiceFetcher<ClipboardManager>() {
            public ClipboardManager createService(ContextImpl ctx) {
                return new ClipboardManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        SYSTEM_SERVICE_NAMES.put(android.text.ClipboardManager.class, Context.CLIPBOARD_SERVICE);
        registerService(Context.CLIPBOARDEX_SERVICE, ClipboardExManager.class, new CachedServiceFetcher<ClipboardExManager>() {
            public ClipboardExManager createService(ContextImpl ctx) {
                return new ClipboardExManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        registerService(Context.CONNECTIVITY_SERVICE, ConnectivityManager.class, new StaticOuterContextServiceFetcher<ConnectivityManager>() {
            public ConnectivityManager createService(Context context) {
                return new ConnectivityManager(context, IConnectivityManager.Stub.asInterface(ServiceManager.getService(Context.CONNECTIVITY_SERVICE)));
            }
        });
        registerService(Context.COUNTRY_DETECTOR, CountryDetector.class, new StaticServiceFetcher<CountryDetector>() {
            public CountryDetector createService() {
                return new CountryDetector(ICountryDetector.Stub.asInterface(ServiceManager.getService(Context.COUNTRY_DETECTOR)));
            }
        });
        registerService(Context.DEVICE_POLICY_SERVICE, DevicePolicyManager.class, new CachedServiceFetcher<DevicePolicyManager>() {
            public DevicePolicyManager createService(ContextImpl ctx) {
                return DevicePolicyManager.create(ctx, ctx.mMainThread.getHandler());
            }
        });
        registerService(Context.DOWNLOAD_SERVICE, DownloadManager.class, new CachedServiceFetcher<DownloadManager>() {
            public DownloadManager createService(ContextImpl ctx) {
                return new DownloadManager(ctx.getContentResolver(), ctx.getPackageName());
            }
        });
        registerService(Context.BATTERY_SERVICE, BatteryManager.class, new StaticServiceFetcher<BatteryManager>() {
            public BatteryManager createService() {
                return new BatteryManager();
            }
        });
        registerService("nfc", NfcManager.class, new CachedServiceFetcher<NfcManager>() {
            public NfcManager createService(ContextImpl ctx) {
                return new NfcManager(ctx);
            }
        });
        registerService(Context.DROPBOX_SERVICE, DropBoxManager.class, new StaticServiceFetcher<DropBoxManager>() {
            public DropBoxManager createService() {
                IDropBoxManagerService service = IDropBoxManagerService.Stub.asInterface(ServiceManager.getService(Context.DROPBOX_SERVICE));
                if (service == null) {
                    return null;
                }
                return new DropBoxManager(service);
            }
        });
        registerService("input", InputManager.class, new StaticServiceFetcher<InputManager>() {
            public InputManager createService() {
                return InputManager.getInstance();
            }
        });
        registerService(Context.DISPLAY_SERVICE, DisplayManager.class, new CachedServiceFetcher<DisplayManager>() {
            public DisplayManager createService(ContextImpl ctx) {
                return new DisplayManager(ctx.getOuterContext());
            }
        });
        registerService(Context.INPUT_METHOD_SERVICE, InputMethodManager.class, new StaticServiceFetcher<InputMethodManager>() {
            public InputMethodManager createService() {
                return InputMethodManager.getInstance();
            }
        });
        registerService(Context.TEXT_SERVICES_MANAGER_SERVICE, TextServicesManager.class, new StaticServiceFetcher<TextServicesManager>() {
            public TextServicesManager createService() {
                return TextServicesManager.getInstance();
            }
        });
        registerService(Context.KEYGUARD_SERVICE, KeyguardManager.class, new StaticServiceFetcher<KeyguardManager>() {
            public KeyguardManager createService() {
                return new KeyguardManager();
            }
        });
        registerService(Context.LAYOUT_INFLATER_SERVICE, LayoutInflater.class, new CachedServiceFetcher<LayoutInflater>() {
            public LayoutInflater createService(ContextImpl ctx) {
                return new PhoneLayoutInflater(ctx.getOuterContext());
            }
        });
        registerService("location", LocationManager.class, new CachedServiceFetcher<LocationManager>() {
            public LocationManager createService(ContextImpl ctx) {
                return new LocationManager(ctx, ILocationManager.Stub.asInterface(ServiceManager.getService("location")));
            }
        });
        registerService(Context.SEC_LOCATION_SERVICE, SLocationManager.class, new StaticServiceFetcher<SLocationManager>() {
            public SLocationManager createService() {
                IBinder b = ServiceManager.getService(Context.SEC_LOCATION_SERVICE);
                SLocationManager sLocationManager = null;
                try {
                    return (SLocationManager) Class.forName("com.samsung.location.SLocationLoader").getDeclaredMethod("getSLocationManager", new Class[]{IBinder.class}).invoke(null, new Object[]{b});
                } catch (Throwable th) {
                    Log.e(SystemServiceRegistry.TAG, "Getting SLocation has been failed, error or not support");
                    return sLocationManager;
                }
            }
        });
        registerService(Context.NETWORK_POLICY_SERVICE, NetworkPolicyManager.class, new CachedServiceFetcher<NetworkPolicyManager>() {
            public NetworkPolicyManager createService(ContextImpl ctx) {
                return new NetworkPolicyManager(ctx, INetworkPolicyManager.Stub.asInterface(ServiceManager.getService(Context.NETWORK_POLICY_SERVICE)));
            }
        });
        registerService(Context.NOTIFICATION_SERVICE, NotificationManager.class, new CachedServiceFetcher<NotificationManager>() {
            public NotificationManager createService(ContextImpl ctx) {
                Context outerContext = ctx.getOuterContext();
                return new NotificationManager(new ContextThemeWrapper(outerContext, Resources.selectSystemTheme(0, outerContext.getApplicationInfo().targetSdkVersion, R.style.Theme_Dialog, R.style.Theme_Holo_Dialog, R.style.Theme_DeviceDefault_Dialog, R.style.Theme_DeviceDefault_Light_Dialog)), ctx.mMainThread.getHandler());
            }
        });
        if (CscFeature.getInstance().getEnableStatus("CscFeature_Common_SupportHuxWiFiPromptDataOveruse")) {
            registerService(Context.WIFI_OFFLOAD_SERVICE, WifiOffloadManager.class, new CachedServiceFetcher<WifiOffloadManager>() {
                public WifiOffloadManager createService(ContextImpl ctx) {
                    IWifiOffloadManager service = IWifiOffloadManager.Stub.asInterface(ServiceManager.getService(Context.WIFI_OFFLOAD_SERVICE));
                    Log.i(SystemServiceRegistry.TAG, "return wifioffload service");
                    return new WifiOffloadManager(service);
                }
            });
        }
        registerService(Context.NSD_SERVICE, NsdManager.class, new CachedServiceFetcher<NsdManager>() {
            public NsdManager createService(ContextImpl ctx) {
                return new NsdManager(ctx.getOuterContext(), INsdManager.Stub.asInterface(ServiceManager.getService(Context.NSD_SERVICE)));
            }
        });
        registerService(Context.POWER_SERVICE, PowerManager.class, new CachedServiceFetcher<PowerManager>() {
            public PowerManager createService(ContextImpl ctx) {
                IPowerManager service = IPowerManager.Stub.asInterface(ServiceManager.getService(Context.POWER_SERVICE));
                if (service == null) {
                    Log.wtf(SystemServiceRegistry.TAG, "Failed to get power manager service.");
                }
                return new PowerManager(ctx.getOuterContext(), service, ctx.mMainThread.getHandler());
            }
        });
        registerService(Context.CFMS_SERVICE, CustomFrequencyManager.class, new CachedServiceFetcher<CustomFrequencyManager>() {
            public CustomFrequencyManager createService(ContextImpl ctx) {
                ICustomFrequencyManager service = ICustomFrequencyManager.Stub.asInterface(ServiceManager.getService(Context.CFMS_SERVICE));
                if (service == null) {
                    Log.wtf(SystemServiceRegistry.TAG, "Failed to get custom frequency manager service.");
                }
                return new CustomFrequencyManager(service, ctx.mMainThread.getHandler());
            }
        });
        registerService("search", SearchManager.class, new CachedServiceFetcher<SearchManager>() {
            public SearchManager createService(ContextImpl ctx) {
                return new SearchManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        registerService(Context.SENSOR_SERVICE, SensorManager.class, new CachedServiceFetcher<SensorManager>() {
            public SensorManager createService(ContextImpl ctx) {
                return new SystemSensorManager(ctx.getOuterContext(), ctx.mMainThread.getHandler().getLooper());
            }
        });
        registerService(Context.SENSORHUB_SERVICE, SensorHubManager.class, new CachedServiceFetcher<SensorHubManager>() {
            public SensorHubManager createService(ContextImpl ctx) {
                return new SensorHubManager(ctx.getOuterContext(), ctx.mMainThread.getHandler().getLooper());
            }
        });
        registerService(Context.STATUS_BAR_SERVICE, StatusBarManager.class, new CachedServiceFetcher<StatusBarManager>() {
            public StatusBarManager createService(ContextImpl ctx) {
                return new StatusBarManager(ctx.getOuterContext());
            }
        });
        registerService(Context.STORAGE_SERVICE, StorageManager.class, new CachedServiceFetcher<StorageManager>() {
            public StorageManager createService(ContextImpl ctx) {
                return new StorageManager(ctx, ctx.mMainThread.getHandler().getLooper());
            }
        });
        registerService("phone", TelephonyManager.class, new CachedServiceFetcher<TelephonyManager>() {
            public TelephonyManager createService(ContextImpl ctx) {
                return new TelephonyManager(ctx.getOuterContext());
            }
        });
        registerService(Context.TELEPHONY_SUBSCRIPTION_SERVICE, SubscriptionManager.class, new CachedServiceFetcher<SubscriptionManager>() {
            public SubscriptionManager createService(ContextImpl ctx) {
                return new SubscriptionManager(ctx.getOuterContext());
            }
        });
        registerService(Context.CARRIER_CONFIG_SERVICE, CarrierConfigManager.class, new CachedServiceFetcher<CarrierConfigManager>() {
            public CarrierConfigManager createService(ContextImpl ctx) {
                return new CarrierConfigManager();
            }
        });
        registerService(Context.TELECOM_SERVICE, TelecomManager.class, new CachedServiceFetcher<TelecomManager>() {
            public TelecomManager createService(ContextImpl ctx) {
                return new TelecomManager(ctx.getOuterContext());
            }
        });
        registerService(Context.UI_MODE_SERVICE, UiModeManager.class, new CachedServiceFetcher<UiModeManager>() {
            public UiModeManager createService(ContextImpl ctx) {
                return new UiModeManager();
            }
        });
        registerService(Context.USB_SERVICE, UsbManager.class, new CachedServiceFetcher<UsbManager>() {
            public UsbManager createService(ContextImpl ctx) {
                return new UsbManager(ctx, IUsbManager.Stub.asInterface(ServiceManager.getService(Context.USB_SERVICE)));
            }
        });
        registerService(Context.SERIAL_SERVICE, SerialManager.class, new CachedServiceFetcher<SerialManager>() {
            public SerialManager createService(ContextImpl ctx) {
                return new SerialManager(ctx, ISerialManager.Stub.asInterface(ServiceManager.getService(Context.SERIAL_SERVICE)));
            }
        });
        registerService(Context.VIBRATOR_SERVICE, Vibrator.class, new CachedServiceFetcher<Vibrator>() {
            public Vibrator createService(ContextImpl ctx) {
                return new SystemVibrator(ctx);
            }
        });
        registerService(Context.WALLPAPER_SERVICE, WallpaperManager.class, new CachedServiceFetcher<WallpaperManager>() {
            public WallpaperManager createService(ContextImpl ctx) {
                return new WallpaperManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        registerService(Context.CONTEXT_AWARE_SERVICE, ContextAwareManager.class, new CachedServiceFetcher<ContextAwareManager>() {
            public ContextAwareManager createService(ContextImpl ctx) {
                return new ContextAwareManager(ctx.mMainThread.getHandler().getLooper());
            }
        });
        registerService(Context.SCONTEXT_SERVICE, SContextManager.class, new CachedServiceFetcher<SContextManager>() {
            public SContextManager createService(ContextImpl ctx) {
                return new SContextManager(ctx.mMainThread.getHandler().getLooper());
            }
        });
        registerService("wifi", WifiManager.class, new CachedServiceFetcher<WifiManager>() {
            public WifiManager createService(ContextImpl ctx) {
                return new WifiManager(ctx.getOuterContext(), IWifiManager.Stub.asInterface(ServiceManager.getService("wifi")));
            }
        });
        registerService(Context.WIFI_HS20_SERVICE, WifiHs20Manager.class, new CachedServiceFetcher<WifiHs20Manager>() {
            public WifiHs20Manager createService(ContextImpl ctx) {
                return new WifiHs20Manager(ctx.getOuterContext(), IWifiHs20Manager.Stub.asInterface(ServiceManager.getService(Context.WIFI_HS20_SERVICE)));
            }
        });
        Log.i(TAG, "VZWLBS flag: CSC=" + CscFeature.getInstance().getEnableStatus("CscFeature_Common_SupportHuxGpsEnableVzwLbsStack"));
        if (CscFeature.getInstance().getEnableStatus("CscFeature_Common_SupportHuxGpsEnableVzwLbsStack")) {
            registerService(Context.VZW_LOCATION_SERVICE, Object.class, new CachedServiceFetcher<Object>() {
                public Object createService(ContextImpl ctx) {
                    return SystemServiceRegistry.getVzwLocationManager(ctx);
                }
            });
        }
        registerService(Context.WIFI_P2P_SERVICE, WifiP2pManager.class, new StaticServiceFetcher<WifiP2pManager>() {
            public WifiP2pManager createService() {
                return new WifiP2pManager(IWifiP2pManager.Stub.asInterface(ServiceManager.getService(Context.WIFI_P2P_SERVICE)));
            }
        });
        registerService(Context.WIFI_SCANNING_SERVICE, WifiScanner.class, new CachedServiceFetcher<WifiScanner>() {
            public WifiScanner createService(ContextImpl ctx) {
                return new WifiScanner(ctx.getOuterContext(), IWifiScanner.Stub.asInterface(ServiceManager.getService(Context.WIFI_SCANNING_SERVICE)));
            }
        });
        registerService(Context.WIFI_RTT_SERVICE, RttManager.class, new CachedServiceFetcher<RttManager>() {
            public RttManager createService(ContextImpl ctx) {
                return new RttManager(ctx.getOuterContext(), IRttManager.Stub.asInterface(ServiceManager.getService(Context.WIFI_RTT_SERVICE)));
            }
        });
        registerService(Context.ETHERNET_SERVICE, EthernetManager.class, new CachedServiceFetcher<EthernetManager>() {
            public EthernetManager createService(ContextImpl ctx) {
                return new EthernetManager(ctx.getOuterContext(), IEthernetManager.Stub.asInterface(ServiceManager.getService(Context.ETHERNET_SERVICE)));
            }
        });
        registerService(Context.WINDOW_SERVICE, WindowManager.class, new CachedServiceFetcher<WindowManager>() {
            public WindowManager createService(ContextImpl ctx) {
                return new WindowManagerImpl(ctx.getDisplay());
            }
        });
        registerService("ktuca", KtUcaManager.class, new StaticServiceFetcher<KtUcaManager>() {
            public KtUcaManager createService() {
                return new KtUcaManager(IKtUcaIF.Stub.asInterface(ServiceManager.getService("ktuca")));
            }
        });
        registerService(Context.USER_SERVICE, UserManager.class, new CachedServiceFetcher<UserManager>() {
            public UserManager createService(ContextImpl ctx) {
                return new UserManager(ctx, IUserManager.Stub.asInterface(ServiceManager.getService(Context.USER_SERVICE)));
            }
        });
        registerService(Context.APP_OPS_SERVICE, AppOpsManager.class, new CachedServiceFetcher<AppOpsManager>() {
            public AppOpsManager createService(ContextImpl ctx) {
                return new AppOpsManager(ctx, IAppOpsService.Stub.asInterface(ServiceManager.getService(Context.APP_OPS_SERVICE)));
            }
        });
        registerService(Context.CAMERA_SERVICE, CameraManager.class, new CachedServiceFetcher<CameraManager>() {
            public CameraManager createService(ContextImpl ctx) {
                return new CameraManager(ctx);
            }
        });
        registerService(Context.LAUNCHER_APPS_SERVICE, LauncherApps.class, new CachedServiceFetcher<LauncherApps>() {
            public LauncherApps createService(ContextImpl ctx) {
                return new LauncherApps(ctx, ILauncherApps.Stub.asInterface(ServiceManager.getService(Context.LAUNCHER_APPS_SERVICE)));
            }
        });
        registerService(Context.RESTRICTIONS_SERVICE, RestrictionsManager.class, new CachedServiceFetcher<RestrictionsManager>() {
            public RestrictionsManager createService(ContextImpl ctx) {
                return new RestrictionsManager(ctx, IRestrictionsManager.Stub.asInterface(ServiceManager.getService(Context.RESTRICTIONS_SERVICE)));
            }
        });
        registerService("print", PrintManager.class, new CachedServiceFetcher<PrintManager>() {
            public PrintManager createService(ContextImpl ctx) {
                return new PrintManager(ctx.getOuterContext(), IPrintManager.Stub.asInterface(ServiceManager.getService("print")), UserHandle.myUserId(), UserHandle.getAppId(Process.myUid()));
            }
        });
        registerService(Context.CONSUMER_IR_SERVICE, ConsumerIrManager.class, new CachedServiceFetcher<ConsumerIrManager>() {
            public ConsumerIrManager createService(ContextImpl ctx) {
                return new ConsumerIrManager(ctx);
            }
        });
        registerService(Context.MEDIA_SESSION_SERVICE, MediaSessionManager.class, new CachedServiceFetcher<MediaSessionManager>() {
            public MediaSessionManager createService(ContextImpl ctx) {
                return new MediaSessionManager(ctx);
            }
        });
        registerService(Context.TRUST_SERVICE, TrustManager.class, new StaticServiceFetcher<TrustManager>() {
            public TrustManager createService() {
                return new TrustManager(ServiceManager.getService(Context.TRUST_SERVICE));
            }
        });
        registerService(Context.FINGERPRINT_SERVICE, FingerprintManager.class, new CachedServiceFetcher<FingerprintManager>() {
            public FingerprintManager createService(ContextImpl ctx) {
                return new FingerprintManager(ctx.getOuterContext(), IFingerprintService.Stub.asInterface(ServiceManager.getService(Context.FINGERPRINT_SERVICE)));
            }
        });
        registerService(Context.TV_INPUT_SERVICE, TvInputManager.class, new StaticServiceFetcher<TvInputManager>() {
            public TvInputManager createService() {
                return new TvInputManager(ITvInputManager.Stub.asInterface(ServiceManager.getService(Context.TV_INPUT_SERVICE)), UserHandle.myUserId());
            }
        });
        registerService(Context.NETWORK_SCORE_SERVICE, NetworkScoreManager.class, new CachedServiceFetcher<NetworkScoreManager>() {
            public NetworkScoreManager createService(ContextImpl ctx) {
                return new NetworkScoreManager(ctx);
            }
        });
        registerService(Context.USAGE_STATS_SERVICE, UsageStatsManager.class, new CachedServiceFetcher<UsageStatsManager>() {
            public UsageStatsManager createService(ContextImpl ctx) {
                return new UsageStatsManager(ctx.getOuterContext(), IUsageStatsManager.Stub.asInterface(ServiceManager.getService(Context.USAGE_STATS_SERVICE)));
            }
        });
        registerService(Context.NETWORK_STATS_SERVICE, NetworkStatsManager.class, new CachedServiceFetcher<NetworkStatsManager>() {
            public NetworkStatsManager createService(ContextImpl ctx) {
                return new NetworkStatsManager(ctx.getOuterContext());
            }
        });
        registerService(Context.JOB_SCHEDULER_SERVICE, JobScheduler.class, new StaticServiceFetcher<JobScheduler>() {
            public JobScheduler createService() {
                return new JobSchedulerImpl(IJobScheduler.Stub.asInterface(ServiceManager.getService(Context.JOB_SCHEDULER_SERVICE)));
            }
        });
        registerService(Context.PERSISTENT_DATA_BLOCK_SERVICE, PersistentDataBlockManager.class, new StaticServiceFetcher<PersistentDataBlockManager>() {
            public PersistentDataBlockManager createService() {
                IPersistentDataBlockService persistentDataBlockService = IPersistentDataBlockService.Stub.asInterface(ServiceManager.getService(Context.PERSISTENT_DATA_BLOCK_SERVICE));
                if (persistentDataBlockService != null) {
                    return new PersistentDataBlockManager(persistentDataBlockService);
                }
                return null;
            }
        });
        registerService(Context.BARBEAM_SERVICE, BarBeamCommandImpl.class, new CachedServiceFetcher<BarBeamCommandImpl>() {
            public BarBeamCommandImpl createService(ContextImpl ctx) {
                if (ctx.getOuterContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BARCODE_EMULATOR)) {
                    return new BarBeamCommandImpl(IBarBeamService.Stub.asInterface(ServiceManager.getService(Context.BARBEAM_SERVICE)));
                }
                return null;
            }
        });
        registerService(Context.MEDIA_PROJECTION_SERVICE, MediaProjectionManager.class, new CachedServiceFetcher<MediaProjectionManager>() {
            public MediaProjectionManager createService(ContextImpl ctx) {
                return new MediaProjectionManager(ctx);
            }
        });
        registerService(Context.APPWIDGET_SERVICE, AppWidgetManager.class, new CachedServiceFetcher<AppWidgetManager>() {
            public AppWidgetManager createService(ContextImpl ctx) {
                return new AppWidgetManager(ctx, IAppWidgetService.Stub.asInterface(ServiceManager.getService(Context.APPWIDGET_SERVICE)));
            }
        });
        registerService(Context.MOTION_RECOGNITION_SERVICE, MotionRecognitionManager.class, new CachedServiceFetcher<MotionRecognitionManager>() {
            public MotionRecognitionManager createService(ContextImpl ctx) {
                return new MotionRecognitionManager(ctx.mMainThread.getHandler().getLooper());
            }
        });
        registerService("midi", MidiManager.class, new CachedServiceFetcher<MidiManager>() {
            public MidiManager createService(ContextImpl ctx) {
                IBinder b = ServiceManager.getService("midi");
                if (b == null) {
                    return null;
                }
                return new MidiManager(IMidiManager.Stub.asInterface(b));
            }
        });
        registerService(Context.RADIO_SERVICE, RadioManager.class, new CachedServiceFetcher<RadioManager>() {
            public RadioManager createService(ContextImpl ctx) {
                return new RadioManager(ctx);
            }
        });
        registerService("ABTPersistenceService", ABTPersistenceManager.class, new StaticServiceFetcher<ABTPersistenceManager>() {
            public ABTPersistenceManager createService() {
                return new ABTPersistenceManager(IABTPersistence.Stub.asInterface(ServiceManager.getService("ABTPersistenceService")));
            }
        });
        registerService(Context.MULTIWINDOW_FACADE_SERVICE, MultiWindowFacade.class, new CachedServiceFetcher<MultiWindowFacade>() {
            public MultiWindowFacade createService(ContextImpl ctx) {
                return new MultiWindowFacade(IMultiWindowFacade.Stub.asInterface(ServiceManager.getService(Context.MULTIWINDOW_FACADE_SERVICE)));
            }
        });
        registerService(Context.SPENGESTURE_SERVICE, SpenGestureManager.class, new CachedServiceFetcher<SpenGestureManager>() {
            public SpenGestureManager createService(ContextImpl ctx) {
                return new SpenGestureManager(ctx);
            }
        });
        registerService("vr", VRManager.class, new CachedServiceFetcher<VRManager>() {
            public VRManager createService(ContextImpl ctx) {
                return new VRManager(ctx);
            }
        });
        registerService(Context.PERSONA_SERVICE, PersonaManager.class, new CachedServiceFetcher<PersonaManager>() {
            public PersonaManager createService(ContextImpl ctx) {
                return new PersonaManager(ctx, IPersonaManager.Stub.asInterface(ServiceManager.getService(Context.PERSONA_SERVICE)));
            }
        });
        registerService(Context.RCP_SERVICE, RCPManager.class, new CachedServiceFetcher<RCPManager>() {
            public RCPManager createService(ContextImpl ctx) {
                return new RCPManager(IRCPManager.Stub.asInterface(ServiceManager.getService(Context.RCP_SERVICE)));
            }
        });
        registerService(Context.EXECUTE_SERVICE, ExecuteManager.class, new CachedServiceFetcher<ExecuteManager>() {
            public ExecuteManager createService(ContextImpl ctx) {
                return new ExecuteManager(ctx.getOuterContext());
            }
        });
        if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportEpdg")) {
            registerService(Context.EPDG_SERVICE, EpdgManager.class, new StaticServiceFetcher<EpdgManager>() {
                public EpdgManager createService() {
                    return new EpdgManager(IEpdgManager.Stub.asInterface(ServiceManager.getService(Context.EPDG_SERVICE)));
                }
            });
        }
        if (Integer.parseInt("0") > 0) {
            registerService(Context.FM_RADIO_SERVICE, FMPlayer.class, new CachedServiceFetcher<FMPlayer>() {
                public FMPlayer createService(ContextImpl ctx) {
                    return new FMPlayer(ctx);
                }
            });
        }
        registerService("mDNIe", MdnieManager.class, new CachedServiceFetcher<MdnieManager>() {
            public MdnieManager createService(ContextImpl ctx) {
                return new MdnieManager(IMdnieManager.Stub.asInterface(ServiceManager.getService("mDNIe")));
            }
        });
        registerService("AAS", AASManager.class, new CachedServiceFetcher<AASManager>() {
            public AASManager createService(ContextImpl ctx) {
                return new AASManager(IAASManager.Stub.asInterface(ServiceManager.getService("AAS")));
            }
        });
        registerService("MSCS", MSCSManager.class, new CachedServiceFetcher<MSCSManager>() {
            public MSCSManager createService(ContextImpl ctx) {
                return new MSCSManager(IMSCSManager.Stub.asInterface(ServiceManager.getService("MSCS")));
            }
        });
    }

    private SystemServiceRegistry() {
    }

    private static Object getVzwLocationManager(ContextImpl ctx) {
        if (CscFeature.getInstance().getEnableStatus("CscFeature_Common_SupportHuxGpsEnableVzwLbsStack") && sVzwLocationManager == null) {
            try {
                sVzwLocationManager = loadVzwLbsFromFramework(ctx);
            } catch (Exception e) {
                Log.e(TAG, "Exception in loading vzwlbs == " + e);
            }
        }
        return sVzwLocationManager;
    }

    private static Object loadVzwLbsFromFramework(ContextImpl ctx) {
        Method method = null;
        try {
            Context apkServiceContext = ctx.createPackageContext("com.samsung.vzwlbs", 3);
            Log.d(TAG, "Loading LoadVzwLbs from the APK");
            Class cls = null;
            try {
                cls = apkServiceContext.getClassLoader().loadClass("android.app.LoadVzwLbs");
                Log.d(TAG, "Loaded VzwLocationManagerService");
            } catch (Exception e) {
                Log.e(TAG, "Exception while loading the class " + e);
            }
            Log.i("VZWLBS", "Loaded LoadVzwLbs SUPL2.0");
            if (cls != null) {
                Log.d(TAG, "android.app.LoadVzwLbs exists, not null");
                method = cls.getMethod("loadVzwLbsFromFramework", null);
                if (method != null) {
                    return method.invoke(method, null);
                }
            }
        } catch (Exception e2) {
            Log.e(TAG, "Exception in getting class android.app.LoadVzwLbs " + e2);
        }
        return method;
    }

    public static Object[] createServiceCache() {
        return new Object[sServiceCacheSize];
    }

    public static Object getSystemService(ContextImpl ctx, String name) {
        ServiceFetcher<?> fetcher = (ServiceFetcher) SYSTEM_SERVICE_FETCHERS.get(name);
        return fetcher != null ? fetcher.getService(ctx) : null;
    }

    public static String getSystemServiceName(Class<?> serviceClass) {
        return (String) SYSTEM_SERVICE_NAMES.get(serviceClass);
    }

    private static <T> void registerService(String serviceName, Class<T> serviceClass, ServiceFetcher<T> serviceFetcher) {
        SYSTEM_SERVICE_NAMES.put(serviceClass, serviceName);
        SYSTEM_SERVICE_FETCHERS.put(serviceName, serviceFetcher);
    }
}
