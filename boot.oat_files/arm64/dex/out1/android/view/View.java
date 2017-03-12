package android.view;

import android.animation.StateListAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Canvas.EdgeType;
import android.graphics.ImageFilter;
import android.graphics.Insets;
import android.graphics.Interpolator;
import android.graphics.Interpolator.Result;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.provider.Settings$System;
import android.provider.Settings.Secure;
import android.service.notification.ZenModeConfig;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatProperty;
import android.util.Log;
import android.util.LongSparseLongArray;
import android.util.Pools.SynchronizedPool;
import android.util.Property;
import android.util.SparseArray;
import android.util.StateSet;
import android.util.SuperNotCalledException;
import android.util.TypedValue;
import android.view.AccessibilityIterators.TextSegmentIterator;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent.DispatcherState;
import android.view.ViewDebug.CapturedViewProperty;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewDebug.FlagToString;
import android.view.ViewDebug.IntToString;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.InternalInsetsInfo;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityEventSource;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HoverPopupWindow;
import android.widget.NumberPicker;
import android.widget.ScrollBarDrawable;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.util.Predicate;
import com.android.internal.view.menu.MenuBuilder;
import com.google.android.collect.Lists;
import com.google.android.collect.Maps;
import com.samsung.android.airbutton.AirButtonImpl;
import com.samsung.android.multiwindow.MultiWindowFacade;
import com.samsung.android.multiwindow.MultiWindowStyle;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipCroppedArea;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipDataElement;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipMetaTag;
import com.samsung.android.smartclip.SmartClipDataCropperImpl;
import com.samsung.android.smartclip.SmartClipDataElementImpl;
import com.samsung.android.smartclip.SmartClipDataExtractionListener;
import com.samsung.android.smartclip.SmartClipDataRepositoryImpl;
import com.samsung.android.smartclip.SmartClipMetaTagArrayImpl;
import com.samsung.android.smartface.SmartFaceManager;
import com.samsung.android.writingbuddy.WritingBuddyImpl;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class View implements Callback, KeyEvent.Callback, AccessibilityEventSource {
    public static final int ACCESSIBILITY_CURSOR_POSITION_UNDEFINED = -1;
    public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
    static final int ACCESSIBILITY_LIVE_REGION_DEFAULT = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
    static final int ALL_RTL_PROPERTIES_RESOLVED = 1610678816;
    public static final Property<View, Float> ALPHA = new FloatProperty<View>("alpha") {
        public void setValue(View object, float value) {
            object.setAlpha(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getAlpha());
        }
    };
    static final int CLICKABLE = 16384;
    static final int CONTEXT_CLICKABLE = 8388608;
    private static final boolean DBG = false;
    private static final boolean DEBUG_DCS = false;
    public static final String DEBUG_LAYOUT_PROPERTY = "debug.layout";
    static final int DISABLED = 32;
    public static final int DRAG_FLAG_GLOBAL = 1;
    static final int DRAG_MASK = 3;
    static final int DRAWING_CACHE_ENABLED = 32768;
    public static final int DRAWING_CACHE_QUALITY_AUTO = 0;
    private static final int[] DRAWING_CACHE_QUALITY_FLAGS = new int[]{0, 524288, 1048576};
    public static final int DRAWING_CACHE_QUALITY_HIGH = 1048576;
    public static final int DRAWING_CACHE_QUALITY_LOW = 524288;
    static final int DRAWING_CACHE_QUALITY_MASK = 1572864;
    static final int DRAW_MASK = 128;
    static final int DUPLICATE_PARENT_STATE = 4194304;
    protected static final int[] EMPTY_STATE_SET = StateSet.get(0);
    static final int ENABLED = 0;
    protected static final int[] ENABLED_FOCUSED_SELECTED_STATE_SET = StateSet.get(14);
    protected static final int[] ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(15);
    protected static final int[] ENABLED_FOCUSED_STATE_SET = StateSet.get(12);
    protected static final int[] ENABLED_FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(13);
    static final int ENABLED_MASK = 32;
    protected static final int[] ENABLED_SELECTED_STATE_SET = StateSet.get(10);
    protected static final int[] ENABLED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(11);
    protected static final int[] ENABLED_STATE_SET = StateSet.get(8);
    protected static final int[] ENABLED_WINDOW_FOCUSED_STATE_SET = StateSet.get(9);
    static final int FADING_EDGE_HORIZONTAL = 4096;
    static final int FADING_EDGE_MASK = 12288;
    static final int FADING_EDGE_NONE = 0;
    static final int FADING_EDGE_VERTICAL = 8192;
    static final int FILTER_TOUCHES_WHEN_OBSCURED = 1024;
    public static final int FIND_VIEWS_WITH_ACCESSIBILITY_NODE_PROVIDERS = 4;
    public static final int FIND_VIEWS_WITH_CONTENT_DESCRIPTION = 2;
    public static final int FIND_VIEWS_WITH_TEXT = 1;
    static final int FINGER_HOVERED = 1;
    private static final int FITS_SYSTEM_WINDOWS = 2;
    private static final int FOCUSABLE = 1;
    public static final int FOCUSABLES_ALL = 0;
    public static final int FOCUSABLES_TOUCH_MODE = 1;
    static final int FOCUSABLE_IN_TOUCH_MODE = 262144;
    private static final int FOCUSABLE_MASK = 1;
    protected static final int[] FOCUSED_SELECTED_STATE_SET = StateSet.get(6);
    protected static final int[] FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(7);
    protected static final int[] FOCUSED_STATE_SET = StateSet.get(4);
    protected static final int[] FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(5);
    public static final int FOCUS_BACKWARD = 1;
    public static final int FOCUS_DOWN = 130;
    public static final int FOCUS_FORWARD = 2;
    public static final int FOCUS_LEFT = 17;
    public static final int FOCUS_RIGHT = 66;
    public static final int FOCUS_UP = 33;
    public static final int GONE = 8;
    public static final int HAPTIC_FEEDBACK_ENABLED = 268435456;
    private static final int HOVERING_UI_DISABLED = 2;
    private static final int HOVERING_UI_ENABLED = 1;
    private static final int HOVERING_UI_MASK = 15;
    private static final int HOVERING_UI_NOT_DECIDED = 0;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
    static final int IMPORTANT_FOR_ACCESSIBILITY_DEFAULT = 0;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
    public static final int INVISIBLE = 4;
    private static final boolean IS_DYNAMIC_COLOR_SCALING_ENABLED = true;
    public static final int KEEP_SCREEN_ON = 67108864;
    public static final int LAYER_TYPE_HARDWARE = 2;
    public static final int LAYER_TYPE_NONE = 0;
    public static final int LAYER_TYPE_SOFTWARE = 1;
    private static final int LAYOUT_DIRECTION_DEFAULT = 2;
    private static final int[] LAYOUT_DIRECTION_FLAGS = new int[]{0, 1, 2, 3};
    public static final int LAYOUT_DIRECTION_INHERIT = 2;
    public static final int LAYOUT_DIRECTION_LOCALE = 3;
    public static final int LAYOUT_DIRECTION_LTR = 0;
    static final int LAYOUT_DIRECTION_RESOLVED_DEFAULT = 0;
    public static final int LAYOUT_DIRECTION_RTL = 1;
    public static final int LAYOUT_DIRECTION_UNDEFINED = -1;
    static final int LONG_CLICKABLE = 2097152;
    public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
    public static final int MEASURED_SIZE_MASK = 16777215;
    public static final int MEASURED_STATE_MASK = -16777216;
    public static final int MEASURED_STATE_TOO_SMALL = 16777216;
    public static final int NAVIGATION_BAR_TRANSIENT = 134217728;
    public static final int NAVIGATION_BAR_TRANSLUCENT = Integer.MIN_VALUE;
    public static final int NAVIGATION_BAR_UNHIDE = 536870912;
    private static final int NOT_FOCUSABLE = 0;
    public static final int NO_ID = -1;
    static final int OPTIONAL_FITS_SYSTEM_WINDOWS = 2048;
    public static final int OVER_SCROLL_ALWAYS = 0;
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    public static final int OVER_SCROLL_NEVER = 2;
    static final int PARENT_SAVE_DISABLED = 536870912;
    static final int PARENT_SAVE_DISABLED_MASK = 536870912;
    static final int PFLAG2_ACCESSIBILITY_FOCUSED = 67108864;
    static final int PFLAG2_ACCESSIBILITY_LIVE_REGION_MASK = 25165824;
    static final int PFLAG2_ACCESSIBILITY_LIVE_REGION_SHIFT = 23;
    static final int PFLAG2_DRAG_CAN_ACCEPT = 1;
    static final int PFLAG2_DRAG_HOVERED = 2;
    static final int PFLAG2_DRAWABLE_RESOLVED = 1073741824;
    static final int PFLAG2_HAS_TRANSIENT_STATE = Integer.MIN_VALUE;
    static final int PFLAG2_IMPORTANT_FOR_ACCESSIBILITY_MASK = 7340032;
    static final int PFLAG2_IMPORTANT_FOR_ACCESSIBILITY_SHIFT = 20;
    static final int PFLAG2_LAYOUT_DIRECTION_MASK = 12;
    static final int PFLAG2_LAYOUT_DIRECTION_MASK_SHIFT = 2;
    static final int PFLAG2_LAYOUT_DIRECTION_RESOLVED = 32;
    static final int PFLAG2_LAYOUT_DIRECTION_RESOLVED_MASK = 48;
    static final int PFLAG2_LAYOUT_DIRECTION_RESOLVED_RTL = 16;
    static final int PFLAG2_PADDING_RESOLVED = 536870912;
    static final int PFLAG2_SUBTREE_ACCESSIBILITY_STATE_CHANGED = 134217728;
    private static final int[] PFLAG2_TEXT_ALIGNMENT_FLAGS = new int[]{0, 8192, 16384, 24576, 32768, 40960, 49152};
    static final int PFLAG2_TEXT_ALIGNMENT_MASK = 57344;
    static final int PFLAG2_TEXT_ALIGNMENT_MASK_SHIFT = 13;
    static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED = 65536;
    private static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED_DEFAULT = 131072;
    static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED_MASK = 917504;
    static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED_MASK_SHIFT = 17;
    private static final int[] PFLAG2_TEXT_DIRECTION_FLAGS = new int[]{0, 64, 128, 192, 256, 320, 384, PFLAG2_TEXT_DIRECTION_MASK};
    static final int PFLAG2_TEXT_DIRECTION_MASK = 448;
    static final int PFLAG2_TEXT_DIRECTION_MASK_SHIFT = 6;
    static final int PFLAG2_TEXT_DIRECTION_RESOLVED = 512;
    static final int PFLAG2_TEXT_DIRECTION_RESOLVED_DEFAULT = 1024;
    static final int PFLAG2_TEXT_DIRECTION_RESOLVED_MASK = 7168;
    static final int PFLAG2_TEXT_DIRECTION_RESOLVED_MASK_SHIFT = 10;
    static final int PFLAG2_VIEW_QUICK_REJECTED = 268435456;
    static final int PFLAG3_APPLYING_INSETS = 32;
    static final int PFLAG3_ASSIST_BLOCKED = 16384;
    static final int PFLAG3_CALLED_SUPER = 16;
    static final int PFLAG3_FITTING_SYSTEM_WINDOWS = 64;
    static final int PFLAG3_IS_LAID_OUT = 4;
    static final int PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT = 8;
    static final int PFLAG3_NESTED_SCROLLING_ENABLED = 128;
    static final int PFLAG3_SCROLL_INDICATOR_BOTTOM = 512;
    static final int PFLAG3_SCROLL_INDICATOR_END = 8192;
    static final int PFLAG3_SCROLL_INDICATOR_LEFT = 1024;
    static final int PFLAG3_SCROLL_INDICATOR_RIGHT = 2048;
    static final int PFLAG3_SCROLL_INDICATOR_START = 4096;
    static final int PFLAG3_SCROLL_INDICATOR_TOP = 256;
    static final int PFLAG3_VIEW_IS_ANIMATING_ALPHA = 2;
    static final int PFLAG3_VIEW_IS_ANIMATING_TRANSFORM = 1;
    static final int PFLAG_ACTIVATED = 1073741824;
    static final int PFLAG_ALPHA_SET = 262144;
    static final int PFLAG_ANIMATION_STARTED = 65536;
    private static final int PFLAG_AWAKEN_SCROLL_BARS_ON_ATTACH = 134217728;
    static final int PFLAG_CANCEL_NEXT_UP_EVENT = 67108864;
    static final int PFLAG_DIRTY = 2097152;
    static final int PFLAG_DIRTY_MASK = 6291456;
    static final int PFLAG_DIRTY_OPAQUE = 4194304;
    private static final int PFLAG_DOES_NOTHING_REUSE_PLEASE = 536870912;
    static final int PFLAG_DRAWABLE_STATE_DIRTY = 1024;
    static final int PFLAG_DRAWING_CACHE_VALID = 32768;
    static final int PFLAG_DRAWN = 32;
    static final int PFLAG_DRAW_ANIMATION = 64;
    static final int PFLAG_FOCUSED = 2;
    static final int PFLAG_FORCE_LAYOUT = 4096;
    static final int PFLAG_HAS_BOUNDS = 16;
    private static final int PFLAG_HOVERED = 268435456;
    static final int PFLAG_INVALIDATED = Integer.MIN_VALUE;
    static final int PFLAG_IS_ROOT_NAMESPACE = 8;
    static final int PFLAG_LAYOUT_REQUIRED = 8192;
    static final int PFLAG_MEASURED_DIMENSION_SET = 2048;
    static final int PFLAG_OPAQUE_BACKGROUND = 8388608;
    static final int PFLAG_OPAQUE_MASK = 25165824;
    static final int PFLAG_OPAQUE_SCROLLBARS = 16777216;
    private static final int PFLAG_PREPRESSED = 33554432;
    private static final int PFLAG_PRESSED = 16384;
    static final int PFLAG_REQUEST_TRANSPARENT_REGIONS = 512;
    private static final int PFLAG_SAVE_STATE_CALLED = 131072;
    static final int PFLAG_SCROLL_CONTAINER = 524288;
    static final int PFLAG_SCROLL_CONTAINER_ADDED = 1048576;
    static final int PFLAG_SELECTED = 4;
    static final int PFLAG_SKIP_DRAW = 128;
    static final int PFLAG_WANTS_FOCUS = 1;
    private static final int POPULATING_ACCESSIBILITY_EVENT_TYPES = 172479;
    protected static final int[] PRESSED_ENABLED_FOCUSED_SELECTED_STATE_SET = StateSet.get(30);
    protected static final int[] PRESSED_ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(31);
    protected static final int[] PRESSED_ENABLED_FOCUSED_STATE_SET = StateSet.get(28);
    protected static final int[] PRESSED_ENABLED_FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(29);
    protected static final int[] PRESSED_ENABLED_SELECTED_STATE_SET = StateSet.get(26);
    protected static final int[] PRESSED_ENABLED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(27);
    protected static final int[] PRESSED_ENABLED_STATE_SET = StateSet.get(24);
    protected static final int[] PRESSED_ENABLED_WINDOW_FOCUSED_STATE_SET = StateSet.get(25);
    protected static final int[] PRESSED_FOCUSED_SELECTED_STATE_SET = StateSet.get(22);
    protected static final int[] PRESSED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(23);
    protected static final int[] PRESSED_FOCUSED_STATE_SET = StateSet.get(20);
    protected static final int[] PRESSED_FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(21);
    protected static final int[] PRESSED_SELECTED_STATE_SET = StateSet.get(18);
    protected static final int[] PRESSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(19);
    protected static final int[] PRESSED_STATE_SET = StateSet.get(16);
    protected static final int[] PRESSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(17);
    public static final String PRODUCT_NAME = SystemProperties.get("ro.product.name");
    private static final int PROVIDER_BACKGROUND = 0;
    private static final int PROVIDER_BOUNDS = 2;
    private static final int PROVIDER_NONE = 1;
    private static final int PROVIDER_PADDED_BOUNDS = 3;
    public static final int PUBLIC_STATUS_BAR_VISIBILITY_MASK = 16383;
    public static final int RECENT_APPS_VISIBLE = 16384;
    public static final Property<View, Float> ROTATION = new FloatProperty<View>("rotation") {
        public void setValue(View object, float value) {
            object.setRotation(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getRotation());
        }
    };
    public static final Property<View, Float> ROTATION_X = new FloatProperty<View>("rotationX") {
        public void setValue(View object, float value) {
            object.setRotationX(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getRotationX());
        }
    };
    public static final Property<View, Float> ROTATION_Y = new FloatProperty<View>("rotationY") {
        public void setValue(View object, float value) {
            object.setRotationY(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getRotationY());
        }
    };
    static final int SAVE_DISABLED = 65536;
    static final int SAVE_DISABLED_MASK = 65536;
    public static final Property<View, Float> SCALE_X = new FloatProperty<View>("scaleX") {
        public void setValue(View object, float value) {
            object.setScaleX(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getScaleX());
        }
    };
    public static final Property<View, Float> SCALE_Y = new FloatProperty<View>("scaleY") {
        public void setValue(View object, float value) {
            object.setScaleY(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getScaleY());
        }
    };
    public static final int SCREEN_STATE_OFF = 0;
    public static final int SCREEN_STATE_ON = 1;
    static final int SCROLLBARS_HORIZONTAL = 256;
    static final int SCROLLBARS_INSET_MASK = 16777216;
    public static final int SCROLLBARS_INSIDE_INSET = 16777216;
    public static final int SCROLLBARS_INSIDE_OVERLAY = 0;
    static final int SCROLLBARS_MASK = 768;
    static final int SCROLLBARS_NONE = 0;
    public static final int SCROLLBARS_OUTSIDE_INSET = 50331648;
    static final int SCROLLBARS_OUTSIDE_MASK = 33554432;
    public static final int SCROLLBARS_OUTSIDE_OVERLAY = 33554432;
    static final int SCROLLBARS_STYLE_MASK = 50331648;
    static final int SCROLLBARS_VERTICAL = 512;
    public static final int SCROLLBAR_POSITION_DEFAULT = 0;
    public static final int SCROLLBAR_POSITION_LEFT = 1;
    public static final int SCROLLBAR_POSITION_RIGHT = 2;
    public static final int SCROLL_AXIS_HORIZONTAL = 1;
    public static final int SCROLL_AXIS_NONE = 0;
    public static final int SCROLL_AXIS_VERTICAL = 2;
    static final int SCROLL_INDICATORS_NONE = 0;
    static final int SCROLL_INDICATORS_PFLAG3_MASK = 16128;
    static final int SCROLL_INDICATORS_TO_PFLAGS3_LSHIFT = 8;
    public static final int SCROLL_INDICATOR_BOTTOM = 2;
    public static final int SCROLL_INDICATOR_END = 32;
    public static final int SCROLL_INDICATOR_LEFT = 4;
    public static final int SCROLL_INDICATOR_RIGHT = 8;
    public static final int SCROLL_INDICATOR_START = 16;
    public static final int SCROLL_INDICATOR_TOP = 1;
    protected static final int[] SELECTED_STATE_SET = StateSet.get(2);
    protected static final int[] SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(3);
    public static final int SMARTCLIP_EXTRACTION_FAILED = 0;
    public static final int SMARTCLIP_EXTRACTION_PENDING = 2;
    public static final int SMARTCLIP_EXTRACTION_SKIP_CHILD_EXTRACTION = 256;
    public static final int SMARTCLIP_EXTRACTION_SUCCESS = 1;
    public static final int SOUND_EFFECTS_ENABLED = 134217728;
    public static final int STATUS_BAR_DISABLE_BACK = 4194304;
    public static final int STATUS_BAR_DISABLE_CLOCK = 8388608;
    public static final int STATUS_BAR_DISABLE_EXPAND = 65536;
    public static final int STATUS_BAR_DISABLE_HOME = 2097152;
    public static final int STATUS_BAR_DISABLE_NOTIFICATION_ALERTS = 262144;
    public static final int STATUS_BAR_DISABLE_NOTIFICATION_ICONS = 131072;
    public static final int STATUS_BAR_DISABLE_NOTIFICATION_TICKER = 524288;
    public static final int STATUS_BAR_DISABLE_RECENT = 16777216;
    public static final int STATUS_BAR_DISABLE_SEARCH = 33554432;
    public static final int STATUS_BAR_DISABLE_SYSTEM_INFO = 1048576;
    public static final int STATUS_BAR_HIDDEN = 1;
    public static final int STATUS_BAR_TRANSIENT = 67108864;
    public static final int STATUS_BAR_TRANSLUCENT = 1073741824;
    public static final int STATUS_BAR_UNHIDE = 268435456;
    public static final int STATUS_BAR_VISIBLE = 0;
    public static final int SYSTEM_UI_CLEARABLE_FLAGS = 7;
    public static final int SYSTEM_UI_FLAG_FULLSCREEN = 4;
    public static final int SYSTEM_UI_FLAG_HIDE_NAVIGATION = 2;
    public static final int SYSTEM_UI_FLAG_IMMERSIVE = 2048;
    public static final int SYSTEM_UI_FLAG_IMMERSIVE_STICKY = 4096;
    public static final int SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN = 1024;
    public static final int SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION = 512;
    public static final int SYSTEM_UI_FLAG_LAYOUT_STABLE = 256;
    public static final int SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = 8192;
    public static final int SYSTEM_UI_FLAG_LOW_PROFILE = 1;
    public static final int SYSTEM_UI_FLAG_VISIBLE = 0;
    public static final int SYSTEM_UI_LAYOUT_FLAGS = 1536;
    public static final int SYSTEM_UI_TRANSPARENT = 32768;
    private static final int TEXTSELECTION_DISABLED = 32;
    private static final int TEXTSELECTION_ENABLED = 16;
    private static final int TEXTSELECTION_MASK = 240;
    private static final int TEXTSELECTION_NOT_DECIDED = 0;
    public static final int TEXT_ALIGNMENT_CENTER = 4;
    private static final int TEXT_ALIGNMENT_DEFAULT = 1;
    public static final int TEXT_ALIGNMENT_GRAVITY = 1;
    public static final int TEXT_ALIGNMENT_INHERIT = 0;
    static final int TEXT_ALIGNMENT_RESOLVED_DEFAULT = 1;
    public static final int TEXT_ALIGNMENT_TEXT_END = 3;
    public static final int TEXT_ALIGNMENT_TEXT_START = 2;
    public static final int TEXT_ALIGNMENT_VIEW_END = 6;
    public static final int TEXT_ALIGNMENT_VIEW_START = 5;
    public static final int TEXT_DIRECTION_ANY_RTL = 2;
    private static final int TEXT_DIRECTION_DEFAULT = 0;
    public static final int TEXT_DIRECTION_FIRST_STRONG = 1;
    public static final int TEXT_DIRECTION_FIRST_STRONG_LTR = 6;
    public static final int TEXT_DIRECTION_FIRST_STRONG_RTL = 7;
    public static final int TEXT_DIRECTION_INHERIT = 0;
    public static final int TEXT_DIRECTION_LOCALE = 5;
    public static final int TEXT_DIRECTION_LTR = 3;
    static final int TEXT_DIRECTION_RESOLVED_DEFAULT = 1;
    public static final int TEXT_DIRECTION_RTL = 4;
    public static final Property<View, Float> TRANSLATION_X = new FloatProperty<View>("translationX") {
        public void setValue(View object, float value) {
            object.setTranslationX(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getTranslationX());
        }
    };
    public static final Property<View, Float> TRANSLATION_Y = new FloatProperty<View>("translationY") {
        public void setValue(View object, float value) {
            object.setTranslationY(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getTranslationY());
        }
    };
    public static final Property<View, Float> TRANSLATION_Z = new FloatProperty<View>("translationZ") {
        public void setValue(View object, float value) {
            object.setTranslationZ(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getTranslationZ());
        }
    };
    public static final boolean TW_SCAFE_2016A = "2016A".equals(SystemProperties.get("ro.build.scafe.version"));
    public static final boolean TW_SCAFE_AMERICANO = "americano".equals(SystemProperties.get("ro.build.scafe"));
    public static final boolean TW_SCAFE_CAPUCCINO = "capuccino".equals(SystemProperties.get("ro.build.scafe"));
    public static final boolean TW_SCAFE_LATTE = "latte".equals(SystemProperties.get("ro.build.scafe"));
    public static final boolean TW_SCAFE_MOCHA = "mocha".equals(SystemProperties.get("ro.build.scafe"));
    private static final int UNDEFINED_PADDING = Integer.MIN_VALUE;
    protected static final String VIEW_LOG_TAG = "View";
    private static final int[] VISIBILITY_FLAGS = new int[]{0, 4, 8};
    static final int VISIBILITY_MASK = 12;
    public static final int VISIBLE = 0;
    static final int WILL_NOT_CACHE_DRAWING = 131072;
    static final int WILL_NOT_DRAW = 128;
    protected static final int[] WINDOW_FOCUSED_STATE_SET = StateSet.get(1);
    private static final int WRITINGBUDDY_FEATURE_DISABLED = 512;
    private static final int WRITINGBUDDY_FEATURE_ENABLED = 256;
    private static final int WRITINGBUDDY_FEATURE_MASK = 3840;
    private static final int WRITINGBUDDY_FEATURE_NOT_CHECKED = 0;
    public static final Property<View, Float> X = new FloatProperty<View>("x") {
        public void setValue(View object, float value) {
            object.setX(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getX());
        }
    };
    public static final Property<View, Float> Y = new FloatProperty<View>("y") {
        public void setValue(View object, float value) {
            object.setY(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getY());
        }
    };
    public static final Property<View, Float> Z = new FloatProperty<View>("z") {
        public void setValue(View object, float value) {
            object.setZ(value);
        }

        public Float get(View object) {
            return Float.valueOf(object.getZ());
        }
    };
    private static SparseArray<String> mAttributeMap;
    public static boolean mDebugViewAttributes = false;
    protected static int mHoverUIFeatureLevel = 0;
    protected static boolean misHoverUIFeatureLevelChecked = false;
    private static boolean sCompatibilityDone = false;
    private static int sHoverUIEnableFlag = 0;
    private static boolean sIgnoreMeasureCache = false;
    private static boolean sIsEasyClipOnSpenLevel3Enabled = false;
    private static int sNextAccessibilityViewId;
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    protected static int sSpenUspLevel = -1;
    static final ThreadLocal<Rect> sThreadLocal = new ThreadLocal();
    private static boolean sUseBrokenMakeMeasureSpec = false;
    static boolean sUseZeroUnspecifiedMeasureSpec = false;
    private final boolean DEBUG_ELASTIC;
    private final boolean isElasticEnabled;
    boolean isPenSideButton;
    private int mAccessibilityCursorPosition;
    AccessibilityDelegate mAccessibilityDelegate;
    private int mAccessibilityTraversalAfterId;
    private int mAccessibilityTraversalBeforeId;
    int mAccessibilityViewId;
    private AirButtonImpl mAirButtonImpl;
    private ViewPropertyAnimator mAnimator;
    AttachInfo mAttachInfo;
    @ExportedProperty(category = "attributes", hasAdjacentMapping = true)
    public String[] mAttributes;
    @ExportedProperty(deepExport = true, prefix = "bg_")
    private Drawable mBackground;
    @ExportedProperty(category = "paths")
    private CharSequence mBackgroundPath;
    private RenderNode mBackgroundRenderNode;
    private int mBackgroundResource;
    private boolean mBackgroundSizeChanged;
    private TintInfo mBackgroundTint;
    @ExportedProperty(category = "layout")
    protected int mBottom;
    public boolean mCachingFailed;
    Rect mClipBounds;
    private CharSequence mContentDescription;
    @ExportedProperty(deepExport = true)
    protected Context mContext;
    protected Animation mCurrentAnimation;
    private boolean mDisablePenGestureforfactorytest;
    private int[] mDrawableState;
    private Bitmap mDrawingCache;
    private int mDrawingCacheBackgroundColor;
    private boolean mEnablePenGesture;
    private ViewTreeObserver mFloatingTreeObserver;
    @ExportedProperty(deepExport = true, prefix = "fg_")
    private ForegroundInfo mForegroundInfo;
    GhostView mGhostView = this;
    private boolean mHasPerformedLongPress;
    protected HoverPopupWindow mHoverPopup;
    protected int mHoverPopupToolTypeByApp;
    protected int mHoverPopupType;
    @ExportedProperty(resolveId = true)
    int mID;
    private boolean mIgnoreNextUpEvent;
    private boolean mInContextButtonPress;
    protected final InputEventConsistencyVerifier mInputEventConsistencyVerifier;
    protected boolean mIsDetachedFromWindow;
    private boolean mIsInDialog;
    boolean mIsSetContextMenuZOrderToTop;
    private boolean mIsSetFingerHovedInAppWidget;
    protected boolean mIsWritingBuddyEnabled;
    private SparseArray<Object> mKeyedTags;
    private int mLabelForId;
    private boolean mLastIsOpaque;
    Paint mLayerPaint;
    @ExportedProperty(category = "drawing", mapping = {@IntToString(from = 0, to = "NONE"), @IntToString(from = 1, to = "SOFTWARE"), @IntToString(from = 2, to = "HARDWARE")})
    int mLayerType;
    private Insets mLayoutInsets;
    protected LayoutParams mLayoutParams;
    @ExportedProperty(category = "layout")
    protected int mLeft;
    private boolean mLeftPaddingDefined;
    ListenerInfo mListenerInfo;
    private MatchIdPredicate mMatchIdPredicate;
    private MatchLabelForPredicate mMatchLabelForPredicate;
    private LongSparseLongArray mMeasureCache;
    @ExportedProperty(category = "measurement")
    int mMeasuredHeight;
    @ExportedProperty(category = "measurement")
    int mMeasuredWidth;
    @ExportedProperty(category = "measurement")
    private int mMinHeight;
    @ExportedProperty(category = "measurement")
    private int mMinWidth;
    private boolean mNeededToChangedScrollBarPosition;
    private ViewParent mNestedScrollingParent;
    private int mNextFocusDownId;
    int mNextFocusForwardId;
    private int mNextFocusLeftId;
    private int mNextFocusRightId;
    private int mNextFocusUpId;
    int mOldHeightMeasureSpec;
    int mOldWidthMeasureSpec;
    ViewOutlineProvider mOutlineProvider;
    private int mOverScrollMode;
    ViewOverlay mOverlay;
    @ExportedProperty(category = "padding")
    protected int mPaddingBottom;
    @ExportedProperty(category = "padding")
    protected int mPaddingLeft;
    @ExportedProperty(category = "padding")
    protected int mPaddingRight;
    @ExportedProperty(category = "padding")
    protected int mPaddingTop;
    protected ViewParent mParent;
    private CheckForLongPress mPendingCheckForLongPress;
    private CheckForTap mPendingCheckForTap;
    private PerformClick mPerformClick;
    @ExportedProperty(flagMapping = {@FlagToString(equals = 4096, mask = 4096, name = "FORCE_LAYOUT"), @FlagToString(equals = 8192, mask = 8192, name = "LAYOUT_REQUIRED"), @FlagToString(equals = 32768, mask = 32768, name = "DRAWING_CACHE_INVALID", outputIf = false), @FlagToString(equals = 32, mask = 32, name = "DRAWN", outputIf = true), @FlagToString(equals = 32, mask = 32, name = "NOT_DRAWN", outputIf = false), @FlagToString(equals = 4194304, mask = 6291456, name = "DIRTY_OPAQUE"), @FlagToString(equals = 2097152, mask = 6291456, name = "DIRTY")}, formatToHexString = true)
    int mPrivateFlags;
    int mPrivateFlags2;
    int mPrivateFlags3;
    boolean mRecreateDisplayList;
    final RenderNode mRenderNode;
    ImageFilter mRenderNodeImageFilter;
    ArrayList<ClipRect> mRenderNodeImageFilterClipRects;
    private final Resources mResources;
    @ExportedProperty(category = "layout")
    protected int mRight;
    private boolean mRightPaddingDefined;
    private View mRootViewCheckForDialog;
    private int mScrollBarPositionPadding;
    private ScrollabilityCache mScrollCache;
    private Drawable mScrollIndicatorDrawable;
    @ExportedProperty(category = "scrolling")
    protected int mScrollX;
    @ExportedProperty(category = "scrolling")
    protected int mScrollY;
    private SendViewScrolledAccessibilityEvent mSendViewScrolledAccessibilityEvent;
    SendViewStateChangedAccessibilityEvent mSendViewStateChangedAccessibilityEvent;
    private boolean mSendingHoverAccessibilityEvents;
    protected boolean mSkipRtlCheck;
    protected SmartClipDataExtractionListener mSmartClipDataExtractionListener;
    protected SmartClipMetaTagArrayImpl mSmartClipDataTag;
    String mStartActivityRequestWho;
    private StateListAnimator mStateListAnimator;
    @ExportedProperty(flagMapping = {@FlagToString(equals = 1, mask = 1, name = "SYSTEM_UI_FLAG_LOW_PROFILE", outputIf = true), @FlagToString(equals = 2, mask = 2, name = "SYSTEM_UI_FLAG_HIDE_NAVIGATION", outputIf = true), @FlagToString(equals = 0, mask = 16383, name = "SYSTEM_UI_FLAG_VISIBLE", outputIf = true)}, formatToHexString = true)
    int mSystemUiVisibility;
    protected Object mTag;
    private int[] mTempNestedScrollConsumed;
    @ExportedProperty(category = "layout")
    protected int mTop;
    private TouchDelegate mTouchDelegate;
    private int mTouchSlop;
    int mTouchwizFlags;
    TransformationInfo mTransformationInfo;
    int mTransientStateCount;
    private String mTransitionName;
    public int mTwExtraPaddingBottomForPreference;
    private int mTwHorizontalScrollbarPosition;
    public final Rect mTwHorizontalScrollbarRect;
    public boolean mTwScrollingByScrollbar;
    public boolean mTwScrollingVertical;
    public final Rect mTwVerticalScrollbarRect;
    private Bitmap mUnscaledDrawingCache;
    private UnsetPressedState mUnsetPressedState;
    @ExportedProperty(category = "padding")
    protected int mUserPaddingBottom;
    @ExportedProperty(category = "padding")
    int mUserPaddingEnd;
    @ExportedProperty(category = "padding")
    protected int mUserPaddingLeft;
    int mUserPaddingLeftInitial;
    @ExportedProperty(category = "padding")
    protected int mUserPaddingRight;
    int mUserPaddingRightInitial;
    @ExportedProperty(category = "padding")
    int mUserPaddingStart;
    private float mVerticalScrollFactor;
    private int mVerticalScrollbarPosition;
    @ExportedProperty(formatToHexString = true)
    int mViewFlags;
    int mWindowAttachCount;
    private WritingBuddyImpl mWritingBuddy;
    @ExportedProperty(category = "paths")
    private CharSequence mXmlFilePath;

    public interface OnClickListener {
        void onClick(View view);
    }

    public interface OnAttachStateChangeListener {
        void onViewAttachedToWindow(View view);

        void onViewDetachedFromWindow(View view);
    }

    public static class AccessibilityDelegate {
        public void sendAccessibilityEvent(View host, int eventType) {
            host.sendAccessibilityEventInternal(eventType);
        }

        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            return host.performAccessibilityActionInternal(action, args);
        }

        public void sendAccessibilityEventUnchecked(View host, AccessibilityEvent event) {
            host.sendAccessibilityEventUncheckedInternal(event);
        }

        public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            return host.dispatchPopulateAccessibilityEventInternal(event);
        }

        public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            host.onPopulateAccessibilityEventInternal(event);
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            host.onInitializeAccessibilityEventInternal(event);
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
            host.onInitializeAccessibilityNodeInfoInternal(info);
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
            return host.onRequestSendAccessibilityEventInternal(child, event);
        }

        public AccessibilityNodeProvider getAccessibilityNodeProvider(View host) {
            return null;
        }

        public AccessibilityNodeInfo createAccessibilityNodeInfo(View host) {
            return host.createAccessibilityNodeInfoInternal();
        }
    }

    static final class AttachInfo {
        int mAccessibilityFetchFlags;
        Drawable mAccessibilityFocusDrawable;
        int mAccessibilityWindowId = Integer.MAX_VALUE;
        float mApplicationScale;
        Canvas mCanvas;
        final Rect mContentInsets = new Rect();
        boolean mDebugLayout = SystemProperties.getBoolean(View.DEBUG_LAYOUT_PROPERTY, false);
        int mDisabledSystemUiVisibility;
        Display mDisplay;
        int mDisplayState = 0;
        long mDrawingTime;
        boolean mForceReportNewAttributes;
        final InternalInsetsInfo mGivenInternalInsets = new InternalInsetsInfo();
        int mGlobalSystemUiVisibility;
        final Handler mHandler;
        boolean mHardwareAccelerated;
        boolean mHardwareAccelerationRequested;
        HardwareRenderer mHardwareRenderer;
        boolean mHasNonEmptyGivenInternalInsets;
        boolean mHasSystemUiListeners;
        boolean mHasWindowFocus;
        boolean mHighContrastText;
        IWindowId mIWindowId;
        boolean mIgnoreDirtyState;
        boolean mInTouchMode;
        final int[] mInvalidateChildLocation = new int[2];
        boolean mIsOpen = true;
        boolean mKeepScreenOn;
        final DispatcherState mKeyDispatchState = new DispatcherState();
        final Rect mOutsets = new Rect();
        final Rect mOverscanInsets = new Rect();
        boolean mOverscanRequested;
        IBinder mPanelParentWindowToken;
        List<RenderNode> mPendingAnimatingRenderNodes;
        final Point mPoint = new Point();
        boolean mRecomputeGlobalAttributes;
        final Callbacks mRootCallbacks;
        View mRootView;
        boolean mScalingRequired;
        final ArrayList<View> mScrollContainers = new ArrayList();
        final IWindowSession mSession;
        boolean mSetIgnoreDirtyState = false;
        final Rect mStableInsets = new Rect();
        int mSystemUiVisibility;
        final ArrayList<View> mTempArrayList = new ArrayList(24);
        final Rect mTmpInvalRect = new Rect();
        final int[] mTmpLocation = new int[2];
        final Matrix mTmpMatrix = new Matrix();
        final Outline mTmpOutline = new Outline();
        final List<RectF> mTmpRectList = new ArrayList();
        final float[] mTmpTransformLocation = new float[2];
        final RectF mTmpTransformRect = new RectF();
        final RectF mTmpTransformRect1 = new RectF();
        final Transformation mTmpTransformation = new Transformation();
        final int[] mTransparentLocation = new int[2];
        final ViewTreeObserver mTreeObserver = new ViewTreeObserver();
        boolean mTurnOffWindowResizeAnim;
        boolean mUnbufferedDispatchRequested;
        boolean mUse32BitDrawingCache;
        View mViewRequestingLayout;
        final ViewRootImpl mViewRootImpl;
        boolean mViewScrollChanged;
        boolean mViewVisibilityChanged;
        final Rect mVisibleInsets = new Rect();
        final IWindow mWindow;
        WindowId mWindowId;
        int mWindowLeft;
        final IBinder mWindowToken;
        int mWindowTop;
        int mWindowVisibility;

        interface Callbacks {
            boolean performHapticFeedback(int i, boolean z);

            void playSoundEffect(int i);
        }

        static class InvalidateInfo {
            private static final int POOL_LIMIT = 10;
            private static final SynchronizedPool<InvalidateInfo> sPool = new SynchronizedPool(10);
            int bottom;
            int left;
            int right;
            View target;
            int top;

            InvalidateInfo() {
            }

            public static InvalidateInfo obtain() {
                InvalidateInfo instance = (InvalidateInfo) sPool.acquire();
                return instance != null ? instance : new InvalidateInfo();
            }

            public void recycle() {
                this.target = null;
                sPool.release(this);
            }
        }

        AttachInfo(IWindowSession session, IWindow window, Display display, ViewRootImpl viewRootImpl, Handler handler, Callbacks effectPlayer) {
            this.mSession = session;
            this.mWindow = window;
            this.mWindowToken = window.asBinder();
            this.mDisplay = display;
            this.mViewRootImpl = viewRootImpl;
            this.mHandler = handler;
            this.mRootCallbacks = effectPlayer;
            if (this.mSession != null) {
                try {
                    this.mIsOpen = this.mSession.getCoverStateSwitch();
                } catch (RemoteException e) {
                    this.mIsOpen = true;
                }
            }
        }

        public void setDisplay(Display display) {
        }
    }

    public static class BaseSavedState extends AbsSavedState {
        public static final Creator<BaseSavedState> CREATOR = new Creator<BaseSavedState>() {
            public BaseSavedState createFromParcel(Parcel in) {
                return new BaseSavedState(in);
            }

            public BaseSavedState[] newArray(int size) {
                return new BaseSavedState[size];
            }
        };
        String mStartActivityRequestWhoSaved;

        public BaseSavedState(Parcel source) {
            super(source);
            this.mStartActivityRequestWhoSaved = source.readString();
        }

        public BaseSavedState(Parcel source, ClassLoader classloader) {
            super(source, classloader);
            this.mStartActivityRequestWhoSaved = source.readString();
        }

        public BaseSavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(this.mStartActivityRequestWhoSaved);
        }
    }

    private final class CheckForLongPress implements Runnable {
        private int mOriginalWindowAttachCount;

        private CheckForLongPress() {
        }

        public void run() {
            if (View.this.isPressed() && View.this.mParent != null && this.mOriginalWindowAttachCount == View.this.mWindowAttachCount && View.this.performLongClick()) {
                View.this.mHasPerformedLongPress = true;
            }
        }

        public void rememberWindowAttachCount() {
            this.mOriginalWindowAttachCount = View.this.mWindowAttachCount;
        }
    }

    private final class CheckForTap implements Runnable {
        public float x;
        public float y;

        private CheckForTap() {
        }

        public void run() {
            View view = View.this;
            view.mPrivateFlags &= -33554433;
            View.this.setPressed(true, this.x, this.y);
            View.this.checkForLongClick(ViewConfiguration.getTapTimeout());
        }
    }

    public class ClipRect {
        Rect mClipRect;
        float mCornerRadius = 0.0f;

        protected ClipRect() {
        }

        public Rect getRect() {
            return this.mClipRect;
        }

        public void setRectBound(int left, int top, int right, int bottom) {
            if (this.mClipRect == null) {
                this.mClipRect = new Rect();
            }
            this.mClipRect.left = left;
            this.mClipRect.top = top;
            this.mClipRect.right = right;
            this.mClipRect.bottom = bottom;
        }

        public float getCornerRadius() {
            return this.mCornerRadius;
        }

        public void setCornerRadius(float radius) {
            this.mCornerRadius = radius;
        }
    }

    private static class DeclaredOnClickListener implements OnClickListener {
        private final View mHostView;
        private Method mMethod;
        private final String mMethodName;

        public DeclaredOnClickListener(View hostView, String methodName) {
            this.mHostView = hostView;
            this.mMethodName = methodName;
        }

        public void onClick(View v) {
            if (this.mMethod == null) {
                this.mMethod = resolveMethod(this.mHostView.getContext(), this.mMethodName);
            }
            try {
                this.mMethod.invoke(this.mHostView.getContext(), new Object[]{v});
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Could not execute non-public method for android:onClick", e);
            } catch (InvocationTargetException e2) {
                throw new IllegalStateException("Could not execute method for android:onClick", e2);
            }
        }

        private Method resolveMethod(Context context, String name) {
            while (context != null) {
                try {
                    if (!context.isRestricted()) {
                        return context.getClass().getMethod(this.mMethodName, new Class[]{View.class});
                    }
                } catch (NoSuchMethodException e) {
                }
                if (context instanceof ContextWrapper) {
                    context = ((ContextWrapper) context).getBaseContext();
                } else {
                    context = null;
                }
            }
            int id = this.mHostView.getId();
            throw new IllegalStateException("Could not find method " + this.mMethodName + "(View) in a parent or ancestor Context for android:onClick " + "attribute defined on view " + this.mHostView.getClass() + (id == -1 ? "" : " with id '" + this.mHostView.getContext().getResources().getResourceEntryName(id) + "'"));
        }
    }

    public static class DragShadowBuilder {
        private final WeakReference<View> mView;

        public DragShadowBuilder(View view) {
            this.mView = new WeakReference(view);
        }

        public DragShadowBuilder() {
            this.mView = new WeakReference(null);
        }

        public final View getView() {
            return (View) this.mView.get();
        }

        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            View view = (View) this.mView.get();
            if (view != null) {
                shadowSize.set(view.getWidth(), view.getHeight());
                shadowTouchPoint.set(shadowSize.x / 2, shadowSize.y / 2);
                return;
            }
            Log.e(View.VIEW_LOG_TAG, "Asked for drag thumb metrics but no view");
        }

        public void onDrawShadow(Canvas canvas) {
            View view = (View) this.mView.get();
            if (view != null) {
                view.draw(canvas);
            } else {
                Log.e(View.VIEW_LOG_TAG, "Asked to draw drag shadow but no view");
            }
        }
    }

    private static class ForegroundInfo {
        private boolean mBoundsChanged;
        private Drawable mDrawable;
        private int mGravity;
        private boolean mInsidePadding;
        private final Rect mOverlayBounds;
        private final Rect mSelfBounds;
        private TintInfo mTintInfo;

        private ForegroundInfo() {
            this.mGravity = 119;
            this.mInsidePadding = true;
            this.mBoundsChanged = true;
            this.mSelfBounds = new Rect();
            this.mOverlayBounds = new Rect();
        }
    }

    static class ListenerInfo {
        private OnHoverListener mOnAirButtonHoverListener;
        OnApplyWindowInsetsListener mOnApplyWindowInsetsListener;
        private CopyOnWriteArrayList<OnAttachStateChangeListener> mOnAttachStateChangeListeners;
        public OnAttachedDisplayChangeListener mOnAttachedDisplayChangeListener;
        public OnClickListener mOnClickListener;
        protected OnContextClickListener mOnContextClickListener;
        protected OnCreateContextMenuListener mOnCreateContextMenuListener;
        private OnDragListener mOnDragListener;
        protected OnFocusChangeListener mOnFocusChangeListener;
        private OnGenericMotionListener mOnGenericMotionListener;
        private OnHoverListener mOnHoverListener;
        private OnKeyListener mOnKeyListener;
        private ArrayList<OnLayoutChangeListener> mOnLayoutChangeListeners;
        protected OnLongClickListener mOnLongClickListener;
        protected OnScrollChangeListener mOnScrollChangeListener;
        private OnSystemUiVisibilityChangeListener mOnSystemUiVisibilityChangeListener;
        private OnTouchListener mOnTouchListener;

        ListenerInfo() {
        }
    }

    private class MatchIdPredicate implements Predicate<View> {
        public int mId;

        private MatchIdPredicate() {
        }

        public boolean apply(View view) {
            return view.mID == this.mId;
        }
    }

    private class MatchLabelForPredicate implements Predicate<View> {
        private int mLabeledId;

        private MatchLabelForPredicate() {
        }

        public boolean apply(View view) {
            return view.mLabelForId == this.mLabeledId;
        }
    }

    public static class MeasureSpec {
        public static final int AT_MOST = Integer.MIN_VALUE;
        public static final int EXACTLY = 1073741824;
        private static final int MODE_MASK = -1073741824;
        private static final int MODE_SHIFT = 30;
        public static final int UNSPECIFIED = 0;

        public static int makeMeasureSpec(int size, int mode) {
            if (View.sUseBrokenMakeMeasureSpec) {
                return size + mode;
            }
            return (1073741823 & size) | (MODE_MASK & mode);
        }

        public static int makeSafeMeasureSpec(int size, int mode) {
            if (View.sUseZeroUnspecifiedMeasureSpec && mode == 0) {
                return 0;
            }
            return makeMeasureSpec(size, mode);
        }

        public static int getMode(int measureSpec) {
            return MODE_MASK & measureSpec;
        }

        public static int getSize(int measureSpec) {
            return 1073741823 & measureSpec;
        }

        static int adjust(int measureSpec, int delta) {
            int mode = getMode(measureSpec);
            int size = getSize(measureSpec);
            if (mode == 0) {
                return makeMeasureSpec(size, 0);
            }
            size += delta;
            if (size < 0) {
                Log.e(View.VIEW_LOG_TAG, "MeasureSpec.adjust: new size would be negative! (" + size + ") spec: " + toString(measureSpec) + " delta: " + delta);
                size = 0;
            }
            return makeMeasureSpec(size, mode);
        }

        public static String toString(int measureSpec) {
            int mode = getMode(measureSpec);
            int size = getSize(measureSpec);
            StringBuilder sb = new StringBuilder("MeasureSpec: ");
            if (mode == 0) {
                sb.append("UNSPECIFIED ");
            } else if (mode == 1073741824) {
                sb.append("EXACTLY ");
            } else if (mode == Integer.MIN_VALUE) {
                sb.append("AT_MOST ");
            } else {
                sb.append(mode).append(" ");
            }
            sb.append(size);
            return sb.toString();
        }
    }

    private class MoreInfoHPW extends HoverPopupWindow {
        private static final boolean DEBUG = true;
        private static final int ID_INFOVIEW = 117510676;
        private static final String TAG = "MoreInfoHPW_View";
        private int mInitialMaxLine = 7;
        private int mLastOrientation = 0;
        TextView mParentTextView = null;

        public MoreInfoHPW(View parentView, int type) {
            super(parentView, type);
            if (this.mParentView instanceof TextView) {
                this.mParentTextView = (TextView) this.mParentView;
                return;
            }
            Log.e(TAG, "Parent view is not a TextView");
            this.mParentTextView = new TextView(View.this.mContext);
        }

        protected void setInstanceByType(int type) {
            super.setInstanceByType(type);
            if (type == 2) {
                this.mPopupGravity = 12849;
                this.mAnimationStyle = R.style.Animation_HoverPopup;
                this.mHoverDetectTimeMS = 300;
                this.mIsGuideLineEnabled = true;
                this.mGuideLineFadeOffset = convertDPtoPX(6.0f, null);
            }
        }

        public boolean isHoverPopupPossible() {
            boolean ret = View.this.findEllipsizedTextView(this.mParentView);
            if (ret) {
                return ret;
            }
            return super.isHoverPopupPossible();
        }

        protected void makeDefaultContentView() {
            TextView v;
            CharSequence text;
            int orientation = View.this.mContext.getResources().getConfiguration().orientation;
            if (this.mContentView != null && this.mContentView.getId() == ID_INFOVIEW && orientation == this.mLastOrientation) {
                v = this.mContentView;
            } else {
                v = (TextView) LayoutInflater.from(View.this.mContext).inflate((int) R.layout.hover_text_popup, null);
                v.setHoverPopupType(0);
                v.setId(ID_INFOVIEW);
                this.mInitialMaxLine = v.getMaxLines();
                this.mLastOrientation = orientation;
            }
            if (TextUtils.isEmpty(this.mContentText)) {
                text = this.mParentTextView.getText();
            } else {
                text = this.mContentText;
            }
            if (TextUtils.isEmpty(text)) {
                v = null;
            } else {
                v.setText(text.toString());
                v.setEllipsize(TruncateAt.END);
                DisplayMetrics d = View.this.getResources().getDisplayMetrics();
                if (d.scaledDensity > d.density && this.mInitialMaxLine > 2) {
                    v.setMaxLines(this.mInitialMaxLine - 2);
                }
            }
            this.mContentView = v;
        }
    }

    public interface OnApplyWindowInsetsListener {
        WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets);
    }

    public interface OnAttachedDisplayChangeListener {
        void onDisplayChange(int i);
    }

    public interface OnContextClickListener {
        boolean onContextClick(View view);
    }

    public interface OnCreateContextMenuListener {
        void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo);
    }

    public interface OnDragListener {
        boolean onDrag(View view, DragEvent dragEvent);
    }

    public interface OnFocusChangeListener {
        void onFocusChange(View view, boolean z);
    }

    public interface OnGenericMotionListener {
        boolean onGenericMotion(View view, MotionEvent motionEvent);
    }

    public interface OnHoverListener {
        boolean onHover(View view, MotionEvent motionEvent);
    }

    public interface OnKeyListener {
        boolean onKey(View view, int i, KeyEvent keyEvent);
    }

    public interface OnLayoutChangeListener {
        void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);
    }

    public interface OnLongClickListener {
        boolean onLongClick(View view);
    }

    public interface OnScrollChangeListener {
        void onScrollChange(View view, int i, int i2, int i3, int i4);
    }

    public interface OnSystemUiVisibilityChangeListener {
        void onSystemUiVisibilityChange(int i);
    }

    public interface OnTouchListener {
        boolean onTouch(View view, MotionEvent motionEvent);
    }

    private final class PerformClick implements Runnable {
        private PerformClick() {
        }

        public void run() {
            View.this.performClick();
        }
    }

    private static class ScrollabilityCache implements Runnable {
        public static final int FADING = 2;
        public static final int OFF = 0;
        public static final int ON = 1;
        private static final float[] OPAQUE = new float[]{255.0f};
        private static final float[] TRANSPARENT = new float[]{0.0f};
        public boolean fadeScrollBars;
        public long fadeStartTime;
        public int fadingEdgeLength;
        public View host;
        public float[] interpolatorValues;
        private int mLastColor;
        public final Matrix matrix;
        public final Paint paint;
        public ScrollBarDrawable scrollBar;
        public int scrollBarDefaultDelayBeforeFade;
        public int scrollBarFadeDuration;
        public final Interpolator scrollBarInterpolator = new Interpolator(1, 2);
        public int scrollBarSize;
        public Shader shader;
        public int state = 0;

        public ScrollabilityCache(ViewConfiguration configuration, View host) {
            this.fadingEdgeLength = configuration.getScaledFadingEdgeLength();
            this.scrollBarSize = configuration.getScaledScrollBarSize();
            this.scrollBarDefaultDelayBeforeFade = ViewConfiguration.getScrollDefaultDelay();
            this.scrollBarFadeDuration = ViewConfiguration.getScrollBarFadeDuration();
            this.paint = new Paint();
            this.matrix = new Matrix();
            this.shader = new LinearGradient(0.0f, 0.0f, 0.0f, 1.0f, -16777216, 0, TileMode.CLAMP);
            this.paint.setShader(this.shader);
            this.paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
            this.host = host;
        }

        public void setFadeColor(int color) {
            if (color != this.mLastColor) {
                this.mLastColor = color;
                if (color != 0) {
                    this.shader = new LinearGradient(0.0f, 0.0f, 0.0f, 1.0f, -16777216 | color, color & 16777215, TileMode.CLAMP);
                    this.paint.setShader(this.shader);
                    this.paint.setXfermode(null);
                    return;
                }
                this.shader = new LinearGradient(0.0f, 0.0f, 0.0f, 1.0f, -16777216, 0, TileMode.CLAMP);
                this.paint.setShader(this.shader);
                this.paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
            }
        }

        public void run() {
            long now = AnimationUtils.currentAnimationTimeMillis();
            if (now >= this.fadeStartTime) {
                int nextFrame = (int) now;
                Interpolator interpolator = this.scrollBarInterpolator;
                int framesCount = 0 + 1;
                interpolator.setKeyFrame(0, nextFrame, OPAQUE);
                interpolator.setKeyFrame(framesCount, nextFrame + this.scrollBarFadeDuration, TRANSPARENT);
                this.state = 2;
                this.host.invalidate(true);
            }
        }
    }

    private class SendViewScrolledAccessibilityEvent implements Runnable {
        public volatile boolean mIsPending;

        private SendViewScrolledAccessibilityEvent() {
        }

        public void run() {
            View.this.sendAccessibilityEvent(4096);
            this.mIsPending = false;
        }
    }

    private class SendViewStateChangedAccessibilityEvent implements Runnable {
        private int mChangeTypes;
        private long mLastEventTimeMillis;
        private boolean mPosted;
        private boolean mPostedWithDelay;

        private SendViewStateChangedAccessibilityEvent() {
            this.mChangeTypes = 0;
        }

        public void run() {
            this.mPosted = false;
            this.mPostedWithDelay = false;
            this.mLastEventTimeMillis = SystemClock.uptimeMillis();
            if (AccessibilityManager.getInstance(View.this.mContext).isEnabled()) {
                AccessibilityEvent event = AccessibilityEvent.obtain();
                event.setEventType(2048);
                event.setContentChangeTypes(this.mChangeTypes);
                View.this.sendAccessibilityEventUnchecked(event);
            }
            this.mChangeTypes = 0;
        }

        public void runOrPost(int changeType) {
            this.mChangeTypes |= changeType;
            if (View.this.inLiveRegion()) {
                if (this.mPostedWithDelay) {
                    View.this.removeCallbacks(this);
                    this.mPostedWithDelay = false;
                }
                if (!this.mPosted) {
                    View.this.post(this);
                    this.mPosted = true;
                }
            } else if (!this.mPosted) {
                long timeSinceLastMillis = SystemClock.uptimeMillis() - this.mLastEventTimeMillis;
                long minEventIntevalMillis = ViewConfiguration.getSendRecurringAccessibilityEventsInterval();
                if (timeSinceLastMillis >= minEventIntevalMillis) {
                    View.this.removeCallbacks(this);
                    run();
                    return;
                }
                View.this.postDelayed(this, minEventIntevalMillis - timeSinceLastMillis);
                this.mPostedWithDelay = true;
            }
        }
    }

    static class TintInfo {
        boolean mHasTintList;
        boolean mHasTintMode;
        ColorStateList mTintList;
        Mode mTintMode;

        TintInfo() {
        }
    }

    static class TransformationInfo {
        @ExportedProperty
        float mAlpha = 1.0f;
        private Matrix mInverseMatrix;
        private final Matrix mMatrix = new Matrix();
        float mTransitionAlpha = 1.0f;

        TransformationInfo() {
        }
    }

    private final class UnsetPressedState implements Runnable {
        private UnsetPressedState() {
        }

        public void run() {
            View.this.setPressed(false);
        }
    }

    public boolean isHighContrastTextEnabled() {
        return ViewRootImpl.sIsHighContrastTextEnabled;
    }

    public View(Context context) {
        InputEventConsistencyVerifier inputEventConsistencyVerifier;
        Resources resources = null;
        this.DEBUG_ELASTIC = true;
        this.isElasticEnabled = true;
        this.mCurrentAnimation = null;
        this.mRecreateDisplayList = false;
        this.mID = -1;
        this.mAccessibilityViewId = -1;
        this.mAccessibilityCursorPosition = -1;
        this.mTag = null;
        this.mTransientStateCount = 0;
        this.mClipBounds = null;
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mLabelForId = -1;
        this.mAccessibilityTraversalBeforeId = -1;
        this.mAccessibilityTraversalAfterId = -1;
        this.mLeftPaddingDefined = false;
        this.mRightPaddingDefined = false;
        this.mOldWidthMeasureSpec = Integer.MIN_VALUE;
        this.mOldHeightMeasureSpec = Integer.MIN_VALUE;
        this.mDrawableState = null;
        this.mOutlineProvider = ViewOutlineProvider.BACKGROUND;
        this.mNextFocusLeftId = -1;
        this.mNextFocusRightId = -1;
        this.mNextFocusUpId = -1;
        this.mNextFocusDownId = -1;
        this.mNextFocusForwardId = -1;
        this.mPendingCheckForTap = null;
        this.mTouchDelegate = null;
        this.mDrawingCacheBackgroundColor = 0;
        this.mAnimator = null;
        this.mTwExtraPaddingBottomForPreference = 0;
        this.mLayerType = 0;
        this.mEnablePenGesture = false;
        this.mDisablePenGestureforfactorytest = true;
        this.isPenSideButton = false;
        if (InputEventConsistencyVerifier.isInstrumentationEnabled()) {
            inputEventConsistencyVerifier = new InputEventConsistencyVerifier(this, 0);
        } else {
            inputEventConsistencyVerifier = null;
        }
        this.mInputEventConsistencyVerifier = inputEventConsistencyVerifier;
        this.mTwScrollingByScrollbar = false;
        this.mTwScrollingVertical = true;
        this.mTwVerticalScrollbarRect = new Rect();
        this.mTwHorizontalScrollbarRect = new Rect();
        this.mWritingBuddy = null;
        this.mIsWritingBuddyEnabled = false;
        this.mRootViewCheckForDialog = null;
        this.mIsInDialog = false;
        this.mIsSetFingerHovedInAppWidget = true;
        this.mScrollBarPositionPadding = 0;
        this.mNeededToChangedScrollBarPosition = false;
        this.mSkipRtlCheck = false;
        this.mSmartClipDataTag = null;
        this.mSmartClipDataExtractionListener = null;
        this.mHoverPopup = null;
        this.mIsDetachedFromWindow = false;
        this.mHoverPopupType = 0;
        this.mHoverPopupToolTypeByApp = 0;
        this.mIsSetContextMenuZOrderToTop = false;
        this.mContext = context;
        if (context != null) {
            resources = context.getResources();
        }
        this.mResources = resources;
        this.mViewFlags = 402653184;
        this.mPrivateFlags2 = 140296;
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setOverScrollMode(1);
        this.mUserPaddingStart = Integer.MIN_VALUE;
        this.mUserPaddingEnd = Integer.MIN_VALUE;
        this.mRenderNode = RenderNode.create(getClass().getName(), this);
        if (!(sCompatibilityDone || context == null)) {
            boolean z;
            int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
            if (targetSdkVersion <= 17) {
                z = true;
            } else {
                z = false;
            }
            sUseBrokenMakeMeasureSpec = z;
            if (targetSdkVersion < 19) {
                z = true;
            } else {
                z = false;
            }
            sIgnoreMeasureCache = z;
            if (targetSdkVersion < 23) {
                z = true;
            } else {
                z = false;
            }
            Canvas.sCompatibilityRestore = z;
            if (targetSdkVersion < 23) {
                z = true;
            } else {
                z = false;
            }
            sUseZeroUnspecifiedMeasureSpec = z;
            sCompatibilityDone = true;
        }
        if (sSpenUspLevel == -1) {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                sSpenUspLevel = pm.getSystemFeatureLevel("com.sec.feature.spen_usp");
                sIsEasyClipOnSpenLevel3Enabled = pm.hasSystemFeature("com.sec.feature.easyclip_on_spen_usp_level3");
            }
        }
        if (sSpenUspLevel > 0 && Settings$System.getInt(context.getContentResolver(), "disable_pen_gesture", 0) == 0) {
            this.mEnablePenGesture = true;
            this.mDisablePenGestureforfactorytest = false;
        }
        this.mRenderNodeImageFilterClipRects = new ArrayList();
    }

    public View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public View(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public View(android.content.Context r57, android.util.AttributeSet r58, int r59, int r60) {
        /*
        r56 = this;
        r56.<init>(r57);
        r53 = com.android.internal.R.styleable.View;
        r0 = r57;
        r1 = r58;
        r2 = r53;
        r3 = r59;
        r4 = r60;
        r6 = r0.obtainStyledAttributes(r1, r2, r3, r4);
        r53 = mDebugViewAttributes;
        if (r53 == 0) goto L_0x001e;
    L_0x0017:
        r0 = r56;
        r1 = r58;
        r0.saveAttributeData(r1, r6);
    L_0x001e:
        r8 = 0;
        r22 = -1;
        r42 = -1;
        r26 = -1;
        r9 = -1;
        r35 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r12 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r25 = -1;
        r49 = 0;
        r48 = 0;
        r34 = 0;
        r51 = 0;
        r52 = 0;
        r44 = 0;
        r45 = 0;
        r46 = 0;
        r11 = 0;
        r28 = 0;
        r29 = 0;
        r30 = 0;
        r37 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r38 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r43 = 0;
        r32 = 0;
        r0 = r56;
        r0 = r0.mOverScrollMode;
        r24 = r0;
        r20 = 0;
        r19 = 0;
        r36 = 0;
        r13 = 0;
        r23 = 0;
        r27 = 0;
        r53 = r57.getApplicationInfo();
        r0 = r53;
        r0 = r0.targetSdkVersion;
        r39 = r0;
        r5 = r6.getIndexCount();
        r17 = 0;
    L_0x006c:
        r0 = r17;
        if (r0 >= r5) goto L_0x06f5;
    L_0x0070:
        r0 = r17;
        r7 = r6.getIndex(r0);
        switch(r7) {
            case 8: goto L_0x0397;
            case 9: goto L_0x01db;
            case 10: goto L_0x01eb;
            case 11: goto L_0x010c;
            case 12: goto L_0x0116;
            case 13: goto L_0x007c;
            case 14: goto L_0x0096;
            case 15: goto L_0x00af;
            case 16: goto L_0x00c0;
            case 17: goto L_0x00c9;
            case 18: goto L_0x00da;
            case 19: goto L_0x0207;
            case 20: goto L_0x0217;
            case 21: goto L_0x0295;
            case 22: goto L_0x01f7;
            case 23: goto L_0x0360;
            case 24: goto L_0x0376;
            case 25: goto L_0x0079;
            case 26: goto L_0x03f0;
            case 27: goto L_0x0400;
            case 28: goto L_0x0410;
            case 29: goto L_0x0420;
            case 30: goto L_0x022d;
            case 31: goto L_0x0245;
            case 32: goto L_0x026d;
            case 33: goto L_0x02e0;
            case 34: goto L_0x0281;
            case 35: goto L_0x05fe;
            case 36: goto L_0x0440;
            case 37: goto L_0x0450;
            case 38: goto L_0x061b;
            case 39: goto L_0x0336;
            case 40: goto L_0x03c4;
            case 41: goto L_0x03ad;
            case 42: goto L_0x034b;
            case 43: goto L_0x0460;
            case 44: goto L_0x02f6;
            case 45: goto L_0x0079;
            case 46: goto L_0x0079;
            case 47: goto L_0x0079;
            case 48: goto L_0x0486;
            case 49: goto L_0x03d8;
            case 50: goto L_0x0120;
            case 51: goto L_0x0131;
            case 52: goto L_0x0147;
            case 53: goto L_0x015d;
            case 54: goto L_0x016e;
            case 55: goto L_0x01c3;
            case 56: goto L_0x01cf;
            case 57: goto L_0x019f;
            case 58: goto L_0x01ab;
            case 59: goto L_0x01b7;
            case 60: goto L_0x0490;
            case 61: goto L_0x0430;
            case 62: goto L_0x04a0;
            case 63: goto L_0x037e;
            case 64: goto L_0x0518;
            case 65: goto L_0x04b5;
            case 66: goto L_0x04eb;
            case 67: goto L_0x02a9;
            case 68: goto L_0x00e3;
            case 69: goto L_0x00f9;
            case 70: goto L_0x0325;
            case 71: goto L_0x0529;
            case 72: goto L_0x017f;
            case 73: goto L_0x053a;
            case 74: goto L_0x0547;
            case 75: goto L_0x0190;
            case 76: goto L_0x0558;
            case 77: goto L_0x0571;
            case 78: goto L_0x05a8;
            case 79: goto L_0x0663;
            case 80: goto L_0x063c;
            case 81: goto L_0x05e9;
            case 82: goto L_0x0303;
            case 83: goto L_0x0314;
            case 84: goto L_0x06c0;
            case 85: goto L_0x0259;
            case 86: goto L_0x0680;
            case 87: goto L_0x06e4;
            default: goto L_0x0079;
        };
    L_0x0079:
        r17 = r17 + 1;
        goto L_0x006c;
    L_0x007c:
        r8 = r6.getDrawable(r7);
        r53 = android.os.Build.IS_SYSTEM_SECURE;
        if (r53 == 0) goto L_0x0079;
    L_0x0084:
        if (r8 == 0) goto L_0x0079;
    L_0x0086:
        r53 = 13;
        r0 = r53;
        r18 = r6.peekValue(r0);
        if (r18 == 0) goto L_0x0079;
    L_0x0090:
        r0 = r18;
        r8.setImagePath(r0);
        goto L_0x0079;
    L_0x0096:
        r53 = -1;
        r0 = r53;
        r25 = r6.getDimensionPixelSize(r7, r0);
        r0 = r25;
        r1 = r56;
        r1.mUserPaddingLeftInitial = r0;
        r0 = r25;
        r1 = r56;
        r1.mUserPaddingRightInitial = r0;
        r23 = 1;
        r27 = 1;
        goto L_0x0079;
    L_0x00af:
        r53 = -1;
        r0 = r53;
        r22 = r6.getDimensionPixelSize(r7, r0);
        r0 = r22;
        r1 = r56;
        r1.mUserPaddingLeftInitial = r0;
        r23 = 1;
        goto L_0x0079;
    L_0x00c0:
        r53 = -1;
        r0 = r53;
        r42 = r6.getDimensionPixelSize(r7, r0);
        goto L_0x0079;
    L_0x00c9:
        r53 = -1;
        r0 = r53;
        r26 = r6.getDimensionPixelSize(r7, r0);
        r0 = r26;
        r1 = r56;
        r1.mUserPaddingRightInitial = r0;
        r27 = 1;
        goto L_0x0079;
    L_0x00da:
        r53 = -1;
        r0 = r53;
        r9 = r6.getDimensionPixelSize(r7, r0);
        goto L_0x0079;
    L_0x00e3:
        r53 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r0 = r53;
        r35 = r6.getDimensionPixelSize(r7, r0);
        r53 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r0 = r35;
        r1 = r53;
        if (r0 == r1) goto L_0x00f6;
    L_0x00f3:
        r36 = 1;
    L_0x00f5:
        goto L_0x0079;
    L_0x00f6:
        r36 = 0;
        goto L_0x00f5;
    L_0x00f9:
        r53 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r0 = r53;
        r12 = r6.getDimensionPixelSize(r7, r0);
        r53 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r0 = r53;
        if (r12 == r0) goto L_0x010a;
    L_0x0107:
        r13 = 1;
    L_0x0108:
        goto L_0x0079;
    L_0x010a:
        r13 = 0;
        goto L_0x0108;
    L_0x010c:
        r53 = 0;
        r0 = r53;
        r51 = r6.getDimensionPixelOffset(r7, r0);
        goto L_0x0079;
    L_0x0116:
        r53 = 0;
        r0 = r53;
        r52 = r6.getDimensionPixelOffset(r7, r0);
        goto L_0x0079;
    L_0x0120:
        r53 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r53;
        r53 = r6.getFloat(r7, r0);
        r0 = r56;
        r1 = r53;
        r0.setAlpha(r1);
        goto L_0x0079;
    L_0x0131:
        r53 = 0;
        r0 = r53;
        r53 = r6.getDimensionPixelOffset(r7, r0);
        r0 = r53;
        r0 = (float) r0;
        r53 = r0;
        r0 = r56;
        r1 = r53;
        r0.setPivotX(r1);
        goto L_0x0079;
    L_0x0147:
        r53 = 0;
        r0 = r53;
        r53 = r6.getDimensionPixelOffset(r7, r0);
        r0 = r53;
        r0 = (float) r0;
        r53 = r0;
        r0 = r56;
        r1 = r53;
        r0.setPivotY(r1);
        goto L_0x0079;
    L_0x015d:
        r53 = 0;
        r0 = r53;
        r53 = r6.getDimensionPixelOffset(r7, r0);
        r0 = r53;
        r0 = (float) r0;
        r44 = r0;
        r43 = 1;
        goto L_0x0079;
    L_0x016e:
        r53 = 0;
        r0 = r53;
        r53 = r6.getDimensionPixelOffset(r7, r0);
        r0 = r53;
        r0 = (float) r0;
        r45 = r0;
        r43 = 1;
        goto L_0x0079;
    L_0x017f:
        r53 = 0;
        r0 = r53;
        r53 = r6.getDimensionPixelOffset(r7, r0);
        r0 = r53;
        r0 = (float) r0;
        r46 = r0;
        r43 = 1;
        goto L_0x0079;
    L_0x0190:
        r53 = 0;
        r0 = r53;
        r53 = r6.getDimensionPixelOffset(r7, r0);
        r0 = r53;
        r11 = (float) r0;
        r43 = 1;
        goto L_0x0079;
    L_0x019f:
        r53 = 0;
        r0 = r53;
        r28 = r6.getFloat(r7, r0);
        r43 = 1;
        goto L_0x0079;
    L_0x01ab:
        r53 = 0;
        r0 = r53;
        r29 = r6.getFloat(r7, r0);
        r43 = 1;
        goto L_0x0079;
    L_0x01b7:
        r53 = 0;
        r0 = r53;
        r30 = r6.getFloat(r7, r0);
        r43 = 1;
        goto L_0x0079;
    L_0x01c3:
        r53 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r53;
        r37 = r6.getFloat(r7, r0);
        r43 = 1;
        goto L_0x0079;
    L_0x01cf:
        r53 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r53;
        r38 = r6.getFloat(r7, r0);
        r43 = 1;
        goto L_0x0079;
    L_0x01db:
        r53 = -1;
        r0 = r53;
        r53 = r6.getResourceId(r7, r0);
        r0 = r53;
        r1 = r56;
        r1.mID = r0;
        goto L_0x0079;
    L_0x01eb:
        r53 = r6.getText(r7);
        r0 = r53;
        r1 = r56;
        r1.mTag = r0;
        goto L_0x0079;
    L_0x01f7:
        r53 = 0;
        r0 = r53;
        r53 = r6.getBoolean(r7, r0);
        if (r53 == 0) goto L_0x0079;
    L_0x0201:
        r49 = r49 | 2;
        r48 = r48 | 2;
        goto L_0x0079;
    L_0x0207:
        r53 = 0;
        r0 = r53;
        r53 = r6.getBoolean(r7, r0);
        if (r53 == 0) goto L_0x0079;
    L_0x0211:
        r49 = r49 | 1;
        r48 = r48 | 1;
        goto L_0x0079;
    L_0x0217:
        r53 = 0;
        r0 = r53;
        r53 = r6.getBoolean(r7, r0);
        if (r53 == 0) goto L_0x0079;
    L_0x0221:
        r53 = 262145; // 0x40001 float:3.67343E-40 double:1.29517E-318;
        r49 = r49 | r53;
        r53 = 262145; // 0x40001 float:3.67343E-40 double:1.29517E-318;
        r48 = r48 | r53;
        goto L_0x0079;
    L_0x022d:
        r53 = 0;
        r0 = r53;
        r53 = r6.getBoolean(r7, r0);
        if (r53 == 0) goto L_0x0079;
    L_0x0237:
        r0 = r49;
        r0 = r0 | 16384;
        r49 = r0;
        r0 = r48;
        r0 = r0 | 16384;
        r48 = r0;
        goto L_0x0079;
    L_0x0245:
        r53 = 0;
        r0 = r53;
        r53 = r6.getBoolean(r7, r0);
        if (r53 == 0) goto L_0x0079;
    L_0x024f:
        r53 = 2097152; // 0x200000 float:2.938736E-39 double:1.0361308E-317;
        r49 = r49 | r53;
        r53 = 2097152; // 0x200000 float:2.938736E-39 double:1.0361308E-317;
        r48 = r48 | r53;
        goto L_0x0079;
    L_0x0259:
        r53 = 0;
        r0 = r53;
        r53 = r6.getBoolean(r7, r0);
        if (r53 == 0) goto L_0x0079;
    L_0x0263:
        r53 = 8388608; // 0x800000 float:1.17549435E-38 double:4.144523E-317;
        r49 = r49 | r53;
        r53 = 8388608; // 0x800000 float:1.17549435E-38 double:4.144523E-317;
        r48 = r48 | r53;
        goto L_0x0079;
    L_0x026d:
        r53 = 1;
        r0 = r53;
        r53 = r6.getBoolean(r7, r0);
        if (r53 != 0) goto L_0x0079;
    L_0x0277:
        r53 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r49 = r49 | r53;
        r53 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r48 = r48 | r53;
        goto L_0x0079;
    L_0x0281:
        r53 = 0;
        r0 = r53;
        r53 = r6.getBoolean(r7, r0);
        if (r53 == 0) goto L_0x0079;
    L_0x028b:
        r53 = 4194304; // 0x400000 float:5.877472E-39 double:2.0722615E-317;
        r49 = r49 | r53;
        r53 = 4194304; // 0x400000 float:5.877472E-39 double:2.0722615E-317;
        r48 = r48 | r53;
        goto L_0x0079;
    L_0x0295:
        r53 = 0;
        r0 = r53;
        r50 = r6.getInt(r7, r0);
        if (r50 == 0) goto L_0x0079;
    L_0x029f:
        r53 = VISIBILITY_FLAGS;
        r53 = r53[r50];
        r49 = r49 | r53;
        r48 = r48 | 12;
        goto L_0x0079;
    L_0x02a9:
        r0 = r56;
        r0 = r0.mPrivateFlags2;
        r53 = r0;
        r53 = r53 & -61;
        r0 = r53;
        r1 = r56;
        r1.mPrivateFlags2 = r0;
        r53 = -1;
        r0 = r53;
        r21 = r6.getInt(r7, r0);
        r53 = -1;
        r0 = r21;
        r1 = r53;
        if (r0 == r1) goto L_0x02dd;
    L_0x02c7:
        r53 = LAYOUT_DIRECTION_FLAGS;
        r47 = r53[r21];
    L_0x02cb:
        r0 = r56;
        r0 = r0.mPrivateFlags2;
        r53 = r0;
        r54 = r47 << 2;
        r53 = r53 | r54;
        r0 = r53;
        r1 = r56;
        r1.mPrivateFlags2 = r0;
        goto L_0x0079;
    L_0x02dd:
        r47 = 2;
        goto L_0x02cb;
    L_0x02e0:
        r53 = 0;
        r0 = r53;
        r10 = r6.getInt(r7, r0);
        if (r10 == 0) goto L_0x0079;
    L_0x02ea:
        r53 = DRAWING_CACHE_QUALITY_FLAGS;
        r53 = r53[r10];
        r49 = r49 | r53;
        r53 = 1572864; // 0x180000 float:2.204052E-39 double:7.77098E-318;
        r48 = r48 | r53;
        goto L_0x0079;
    L_0x02f6:
        r53 = r6.getString(r7);
        r0 = r56;
        r1 = r53;
        r0.setContentDescription(r1);
        goto L_0x0079;
    L_0x0303:
        r53 = -1;
        r0 = r53;
        r53 = r6.getResourceId(r7, r0);
        r0 = r56;
        r1 = r53;
        r0.setAccessibilityTraversalBefore(r1);
        goto L_0x0079;
    L_0x0314:
        r53 = -1;
        r0 = r53;
        r53 = r6.getResourceId(r7, r0);
        r0 = r56;
        r1 = r53;
        r0.setAccessibilityTraversalAfter(r1);
        goto L_0x0079;
    L_0x0325:
        r53 = -1;
        r0 = r53;
        r53 = r6.getResourceId(r7, r0);
        r0 = r56;
        r1 = r53;
        r0.setLabelFor(r1);
        goto L_0x0079;
    L_0x0336:
        r53 = 1;
        r0 = r53;
        r53 = r6.getBoolean(r7, r0);
        if (r53 != 0) goto L_0x0079;
    L_0x0340:
        r53 = -134217729; // 0xfffffffff7ffffff float:-1.0384593E34 double:NaN;
        r49 = r49 & r53;
        r53 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r48 = r48 | r53;
        goto L_0x0079;
    L_0x034b:
        r53 = 1;
        r0 = r53;
        r53 = r6.getBoolean(r7, r0);
        if (r53 != 0) goto L_0x0079;
    L_0x0355:
        r53 = -268435457; // 0xffffffffefffffff float:-1.5845632E29 double:NaN;
        r49 = r49 & r53;
        r53 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r48 = r48 | r53;
        goto L_0x0079;
    L_0x0360:
        r53 = 0;
        r0 = r53;
        r33 = r6.getInt(r7, r0);
        if (r33 == 0) goto L_0x0079;
    L_0x036a:
        r49 = r49 | r33;
        r0 = r48;
        r0 = r0 | 768;
        r48 = r0;
        r20 = 1;
        goto L_0x0079;
    L_0x0376:
        r53 = 14;
        r0 = r39;
        r1 = r53;
        if (r0 >= r1) goto L_0x0079;
    L_0x037e:
        r53 = 0;
        r0 = r53;
        r14 = r6.getInt(r7, r0);
        if (r14 == 0) goto L_0x0079;
    L_0x0388:
        r49 = r49 | r14;
        r0 = r48;
        r0 = r0 | 12288;
        r48 = r0;
        r0 = r56;
        r0.initializeFadingEdgeInternal(r6);
        goto L_0x0079;
    L_0x0397:
        r53 = 0;
        r0 = r53;
        r32 = r6.getInt(r7, r0);
        if (r32 == 0) goto L_0x0079;
    L_0x03a1:
        r53 = 50331648; // 0x3000000 float:3.761582E-37 double:2.4867138E-316;
        r53 = r53 & r32;
        r49 = r49 | r53;
        r53 = 50331648; // 0x3000000 float:3.761582E-37 double:2.4867138E-316;
        r48 = r48 | r53;
        goto L_0x0079;
    L_0x03ad:
        r34 = 1;
        r53 = 0;
        r0 = r53;
        r53 = r6.getBoolean(r7, r0);
        if (r53 == 0) goto L_0x0079;
    L_0x03b9:
        r53 = 1;
        r0 = r56;
        r1 = r53;
        r0.setScrollContainer(r1);
        goto L_0x0079;
    L_0x03c4:
        r53 = 0;
        r0 = r53;
        r53 = r6.getBoolean(r7, r0);
        if (r53 == 0) goto L_0x0079;
    L_0x03ce:
        r53 = 67108864; // 0x4000000 float:1.5046328E-36 double:3.31561842E-316;
        r49 = r49 | r53;
        r53 = 67108864; // 0x4000000 float:1.5046328E-36 double:3.31561842E-316;
        r48 = r48 | r53;
        goto L_0x0079;
    L_0x03d8:
        r53 = 0;
        r0 = r53;
        r53 = r6.getBoolean(r7, r0);
        if (r53 == 0) goto L_0x0079;
    L_0x03e2:
        r0 = r49;
        r0 = r0 | 1024;
        r49 = r0;
        r0 = r48;
        r0 = r0 | 1024;
        r48 = r0;
        goto L_0x0079;
    L_0x03f0:
        r53 = -1;
        r0 = r53;
        r53 = r6.getResourceId(r7, r0);
        r0 = r53;
        r1 = r56;
        r1.mNextFocusLeftId = r0;
        goto L_0x0079;
    L_0x0400:
        r53 = -1;
        r0 = r53;
        r53 = r6.getResourceId(r7, r0);
        r0 = r53;
        r1 = r56;
        r1.mNextFocusRightId = r0;
        goto L_0x0079;
    L_0x0410:
        r53 = -1;
        r0 = r53;
        r53 = r6.getResourceId(r7, r0);
        r0 = r53;
        r1 = r56;
        r1.mNextFocusUpId = r0;
        goto L_0x0079;
    L_0x0420:
        r53 = -1;
        r0 = r53;
        r53 = r6.getResourceId(r7, r0);
        r0 = r53;
        r1 = r56;
        r1.mNextFocusDownId = r0;
        goto L_0x0079;
    L_0x0430:
        r53 = -1;
        r0 = r53;
        r53 = r6.getResourceId(r7, r0);
        r0 = r53;
        r1 = r56;
        r1.mNextFocusForwardId = r0;
        goto L_0x0079;
    L_0x0440:
        r53 = 0;
        r0 = r53;
        r53 = r6.getDimensionPixelSize(r7, r0);
        r0 = r53;
        r1 = r56;
        r1.mMinWidth = r0;
        goto L_0x0079;
    L_0x0450:
        r53 = 0;
        r0 = r53;
        r53 = r6.getDimensionPixelSize(r7, r0);
        r0 = r53;
        r1 = r56;
        r1.mMinHeight = r0;
        goto L_0x0079;
    L_0x0460:
        r53 = r57.isRestricted();
        if (r53 == 0) goto L_0x046e;
    L_0x0466:
        r53 = new java.lang.IllegalStateException;
        r54 = "The android:onClick attribute cannot be used within a restricted context";
        r53.<init>(r54);
        throw r53;
    L_0x046e:
        r15 = r6.getString(r7);
        if (r15 == 0) goto L_0x0079;
    L_0x0474:
        r53 = new android.view.View$DeclaredOnClickListener;
        r0 = r53;
        r1 = r56;
        r0.<init>(r1, r15);
        r0 = r56;
        r1 = r53;
        r0.setOnClickListener(r1);
        goto L_0x0079;
    L_0x0486:
        r53 = 1;
        r0 = r53;
        r24 = r6.getInt(r7, r0);
        goto L_0x0079;
    L_0x0490:
        r53 = 0;
        r0 = r53;
        r53 = r6.getInt(r7, r0);
        r0 = r53;
        r1 = r56;
        r1.mVerticalScrollbarPosition = r0;
        goto L_0x0079;
    L_0x04a0:
        r53 = 0;
        r0 = r53;
        r53 = r6.getInt(r7, r0);
        r54 = 0;
        r0 = r56;
        r1 = r53;
        r2 = r54;
        r0.setLayerType(r1, r2);
        goto L_0x0079;
    L_0x04b5:
        r0 = r56;
        r0 = r0.mPrivateFlags2;
        r53 = r0;
        r0 = r53;
        r0 = r0 & -449;
        r53 = r0;
        r0 = r53;
        r1 = r56;
        r1.mPrivateFlags2 = r0;
        r53 = -1;
        r0 = r53;
        r41 = r6.getInt(r7, r0);
        r53 = -1;
        r0 = r41;
        r1 = r53;
        if (r0 == r1) goto L_0x0079;
    L_0x04d7:
        r0 = r56;
        r0 = r0.mPrivateFlags2;
        r53 = r0;
        r54 = PFLAG2_TEXT_DIRECTION_FLAGS;
        r54 = r54[r41];
        r53 = r53 | r54;
        r0 = r53;
        r1 = r56;
        r1.mPrivateFlags2 = r0;
        goto L_0x0079;
    L_0x04eb:
        r0 = r56;
        r0 = r0.mPrivateFlags2;
        r53 = r0;
        r54 = -57345; // 0xffffffffffff1fff float:NaN double:NaN;
        r53 = r53 & r54;
        r0 = r53;
        r1 = r56;
        r1.mPrivateFlags2 = r0;
        r53 = 1;
        r0 = r53;
        r40 = r6.getInt(r7, r0);
        r0 = r56;
        r0 = r0.mPrivateFlags2;
        r53 = r0;
        r54 = PFLAG2_TEXT_ALIGNMENT_FLAGS;
        r54 = r54[r40];
        r53 = r53 | r54;
        r0 = r53;
        r1 = r56;
        r1.mPrivateFlags2 = r0;
        goto L_0x0079;
    L_0x0518:
        r53 = 0;
        r0 = r53;
        r53 = r6.getInt(r7, r0);
        r0 = r56;
        r1 = r53;
        r0.setImportantForAccessibility(r1);
        goto L_0x0079;
    L_0x0529:
        r53 = 0;
        r0 = r53;
        r53 = r6.getInt(r7, r0);
        r0 = r56;
        r1 = r53;
        r0.setAccessibilityLiveRegion(r1);
        goto L_0x0079;
    L_0x053a:
        r53 = r6.getString(r7);
        r0 = r56;
        r1 = r53;
        r0.setTransitionName(r1);
        goto L_0x0079;
    L_0x0547:
        r53 = 0;
        r0 = r53;
        r53 = r6.getBoolean(r7, r0);
        r0 = r56;
        r1 = r53;
        r0.setNestedScrollingEnabled(r1);
        goto L_0x0079;
    L_0x0558:
        r53 = 0;
        r0 = r53;
        r53 = r6.getResourceId(r7, r0);
        r0 = r57;
        r1 = r53;
        r53 = android.animation.AnimatorInflater.loadStateListAnimator(r0, r1);
        r0 = r56;
        r1 = r53;
        r0.setStateListAnimator(r1);
        goto L_0x0079;
    L_0x0571:
        r0 = r56;
        r0 = r0.mBackgroundTint;
        r53 = r0;
        if (r53 != 0) goto L_0x0584;
    L_0x0579:
        r53 = new android.view.View$TintInfo;
        r53.<init>();
        r0 = r53;
        r1 = r56;
        r1.mBackgroundTint = r0;
    L_0x0584:
        r0 = r56;
        r0 = r0.mBackgroundTint;
        r53 = r0;
        r54 = 77;
        r0 = r54;
        r54 = r6.getColorStateList(r0);
        r0 = r54;
        r1 = r53;
        r1.mTintList = r0;
        r0 = r56;
        r0 = r0.mBackgroundTint;
        r53 = r0;
        r54 = 1;
        r0 = r54;
        r1 = r53;
        r1.mHasTintList = r0;
        goto L_0x0079;
    L_0x05a8:
        r0 = r56;
        r0 = r0.mBackgroundTint;
        r53 = r0;
        if (r53 != 0) goto L_0x05bb;
    L_0x05b0:
        r53 = new android.view.View$TintInfo;
        r53.<init>();
        r0 = r53;
        r1 = r56;
        r1.mBackgroundTint = r0;
    L_0x05bb:
        r0 = r56;
        r0 = r0.mBackgroundTint;
        r53 = r0;
        r54 = 78;
        r55 = -1;
        r0 = r54;
        r1 = r55;
        r54 = r6.getInt(r0, r1);
        r55 = 0;
        r54 = android.graphics.drawable.Drawable.parseTintMode(r54, r55);
        r0 = r54;
        r1 = r53;
        r1.mTintMode = r0;
        r0 = r56;
        r0 = r0.mBackgroundTint;
        r53 = r0;
        r54 = 1;
        r0 = r54;
        r1 = r53;
        r1.mHasTintMode = r0;
        goto L_0x0079;
    L_0x05e9:
        r53 = 81;
        r54 = 0;
        r0 = r53;
        r1 = r54;
        r53 = r6.getInt(r0, r1);
        r0 = r56;
        r1 = r53;
        r0.setOutlineProviderFromAttribute(r1);
        goto L_0x0079;
    L_0x05fe:
        r53 = 23;
        r0 = r39;
        r1 = r53;
        if (r0 >= r1) goto L_0x060e;
    L_0x0606:
        r0 = r56;
        r0 = r0 instanceof android.widget.FrameLayout;
        r53 = r0;
        if (r53 == 0) goto L_0x0079;
    L_0x060e:
        r53 = r6.getDrawable(r7);
        r0 = r56;
        r1 = r53;
        r0.setForeground(r1);
        goto L_0x0079;
    L_0x061b:
        r53 = 23;
        r0 = r39;
        r1 = r53;
        if (r0 >= r1) goto L_0x062b;
    L_0x0623:
        r0 = r56;
        r0 = r0 instanceof android.widget.FrameLayout;
        r53 = r0;
        if (r53 == 0) goto L_0x0079;
    L_0x062b:
        r53 = 0;
        r0 = r53;
        r53 = r6.getInt(r7, r0);
        r0 = r56;
        r1 = r53;
        r0.setForegroundGravity(r1);
        goto L_0x0079;
    L_0x063c:
        r53 = 23;
        r0 = r39;
        r1 = r53;
        if (r0 >= r1) goto L_0x064c;
    L_0x0644:
        r0 = r56;
        r0 = r0 instanceof android.widget.FrameLayout;
        r53 = r0;
        if (r53 == 0) goto L_0x0079;
    L_0x064c:
        r53 = -1;
        r0 = r53;
        r53 = r6.getInt(r7, r0);
        r54 = 0;
        r53 = android.graphics.drawable.Drawable.parseTintMode(r53, r54);
        r0 = r56;
        r1 = r53;
        r0.setForegroundTintMode(r1);
        goto L_0x0079;
    L_0x0663:
        r53 = 23;
        r0 = r39;
        r1 = r53;
        if (r0 >= r1) goto L_0x0673;
    L_0x066b:
        r0 = r56;
        r0 = r0 instanceof android.widget.FrameLayout;
        r53 = r0;
        if (r53 == 0) goto L_0x0079;
    L_0x0673:
        r53 = r6.getColorStateList(r7);
        r0 = r56;
        r1 = r53;
        r0.setForegroundTintList(r1);
        goto L_0x0079;
    L_0x0680:
        r53 = 23;
        r0 = r39;
        r1 = r53;
        if (r0 >= r1) goto L_0x0690;
    L_0x0688:
        r0 = r56;
        r0 = r0 instanceof android.widget.FrameLayout;
        r53 = r0;
        if (r53 == 0) goto L_0x0079;
    L_0x0690:
        r0 = r56;
        r0 = r0.mForegroundInfo;
        r53 = r0;
        if (r53 != 0) goto L_0x06a5;
    L_0x0698:
        r53 = new android.view.View$ForegroundInfo;
        r54 = 0;
        r53.<init>();
        r0 = r53;
        r1 = r56;
        r1.mForegroundInfo = r0;
    L_0x06a5:
        r0 = r56;
        r0 = r0.mForegroundInfo;
        r53 = r0;
        r0 = r56;
        r0 = r0.mForegroundInfo;
        r54 = r0;
        r54 = r54.mInsidePadding;
        r0 = r54;
        r54 = r6.getBoolean(r7, r0);
        r53.mInsidePadding = r54;
        goto L_0x0079;
    L_0x06c0:
        r53 = 0;
        r0 = r53;
        r53 = r6.getInt(r7, r0);
        r53 = r53 << 8;
        r0 = r53;
        r0 = r0 & 16128;
        r31 = r0;
        if (r31 == 0) goto L_0x0079;
    L_0x06d2:
        r0 = r56;
        r0 = r0.mPrivateFlags3;
        r53 = r0;
        r53 = r53 | r31;
        r0 = r53;
        r1 = r56;
        r1.mPrivateFlags3 = r0;
        r19 = 1;
        goto L_0x0079;
    L_0x06e4:
        r53 = 0;
        r0 = r53;
        r53 = r6.getInt(r7, r0);
        r0 = r56;
        r1 = r53;
        r0.setHoverPopupType(r1);
        goto L_0x0079;
    L_0x06f5:
        r0 = r56;
        r1 = r24;
        r0.setOverScrollMode(r1);
        r0 = r35;
        r1 = r56;
        r1.mUserPaddingStart = r0;
        r0 = r56;
        r0.mUserPaddingEnd = r12;
        if (r8 == 0) goto L_0x070d;
    L_0x0708:
        r0 = r56;
        r0.setBackground(r8);
    L_0x070d:
        r0 = r23;
        r1 = r56;
        r1.mLeftPaddingDefined = r0;
        r0 = r27;
        r1 = r56;
        r1.mRightPaddingDefined = r0;
        if (r25 < 0) goto L_0x072f;
    L_0x071b:
        r22 = r25;
        r42 = r25;
        r26 = r25;
        r9 = r25;
        r0 = r25;
        r1 = r56;
        r1.mUserPaddingLeftInitial = r0;
        r0 = r25;
        r1 = r56;
        r1.mUserPaddingRightInitial = r0;
    L_0x072f:
        r53 = r56.isRtlCompatibilityMode();
        if (r53 == 0) goto L_0x080e;
    L_0x0735:
        r0 = r56;
        r0 = r0.mLeftPaddingDefined;
        r53 = r0;
        if (r53 != 0) goto L_0x0741;
    L_0x073d:
        if (r36 == 0) goto L_0x0741;
    L_0x073f:
        r22 = r35;
    L_0x0741:
        if (r22 < 0) goto L_0x07fe;
    L_0x0743:
        r53 = r22;
    L_0x0745:
        r0 = r53;
        r1 = r56;
        r1.mUserPaddingLeftInitial = r0;
        r0 = r56;
        r0 = r0.mRightPaddingDefined;
        r53 = r0;
        if (r53 != 0) goto L_0x0757;
    L_0x0753:
        if (r13 == 0) goto L_0x0757;
    L_0x0755:
        r26 = r12;
    L_0x0757:
        if (r26 < 0) goto L_0x0806;
    L_0x0759:
        r53 = r26;
    L_0x075b:
        r0 = r53;
        r1 = r56;
        r1.mUserPaddingRightInitial = r0;
    L_0x0761:
        r0 = r56;
        r0 = r0.mUserPaddingLeftInitial;
        r53 = r0;
        if (r42 < 0) goto L_0x0839;
    L_0x0769:
        r0 = r56;
        r0 = r0.mUserPaddingRightInitial;
        r54 = r0;
        if (r9 < 0) goto L_0x0841;
    L_0x0771:
        r0 = r56;
        r1 = r53;
        r2 = r42;
        r3 = r54;
        r0.internalSetPadding(r1, r2, r3, r9);
        if (r48 == 0) goto L_0x0787;
    L_0x077e:
        r0 = r56;
        r1 = r49;
        r2 = r48;
        r0.setFlags(r1, r2);
    L_0x0787:
        if (r20 == 0) goto L_0x078e;
    L_0x0789:
        r0 = r56;
        r0.initializeScrollbarsInternal(r6);
    L_0x078e:
        if (r19 == 0) goto L_0x0793;
    L_0x0790:
        r56.initializeScrollIndicatorsInternal();
    L_0x0793:
        r6.recycle();
        if (r32 == 0) goto L_0x079b;
    L_0x0798:
        r56.recomputePadding();
    L_0x079b:
        if (r51 != 0) goto L_0x079f;
    L_0x079d:
        if (r52 == 0) goto L_0x07a8;
    L_0x079f:
        r0 = r56;
        r1 = r51;
        r2 = r52;
        r0.scrollTo(r1, r2);
    L_0x07a8:
        if (r43 == 0) goto L_0x07e7;
    L_0x07aa:
        r0 = r56;
        r1 = r44;
        r0.setTranslationX(r1);
        r0 = r56;
        r1 = r45;
        r0.setTranslationY(r1);
        r0 = r56;
        r1 = r46;
        r0.setTranslationZ(r1);
        r0 = r56;
        r0.setElevation(r11);
        r0 = r56;
        r1 = r28;
        r0.setRotation(r1);
        r0 = r56;
        r1 = r29;
        r0.setRotationX(r1);
        r0 = r56;
        r1 = r30;
        r0.setRotationY(r1);
        r0 = r56;
        r1 = r37;
        r0.setScaleX(r1);
        r0 = r56;
        r1 = r38;
        r0.setScaleY(r1);
    L_0x07e7:
        if (r34 != 0) goto L_0x07fa;
    L_0x07e9:
        r0 = r49;
        r0 = r0 & 512;
        r53 = r0;
        if (r53 == 0) goto L_0x07fa;
    L_0x07f1:
        r53 = 1;
        r0 = r56;
        r1 = r53;
        r0.setScrollContainer(r1);
    L_0x07fa:
        r56.computeOpaqueFlags();
        return;
    L_0x07fe:
        r0 = r56;
        r0 = r0.mUserPaddingLeftInitial;
        r53 = r0;
        goto L_0x0745;
    L_0x0806:
        r0 = r56;
        r0 = r0.mUserPaddingRightInitial;
        r53 = r0;
        goto L_0x075b;
    L_0x080e:
        if (r36 != 0) goto L_0x0812;
    L_0x0810:
        if (r13 == 0) goto L_0x0836;
    L_0x0812:
        r16 = 1;
    L_0x0814:
        r0 = r56;
        r0 = r0.mLeftPaddingDefined;
        r53 = r0;
        if (r53 == 0) goto L_0x0824;
    L_0x081c:
        if (r16 != 0) goto L_0x0824;
    L_0x081e:
        r0 = r22;
        r1 = r56;
        r1.mUserPaddingLeftInitial = r0;
    L_0x0824:
        r0 = r56;
        r0 = r0.mRightPaddingDefined;
        r53 = r0;
        if (r53 == 0) goto L_0x0761;
    L_0x082c:
        if (r16 != 0) goto L_0x0761;
    L_0x082e:
        r0 = r26;
        r1 = r56;
        r1.mUserPaddingRightInitial = r0;
        goto L_0x0761;
    L_0x0836:
        r16 = 0;
        goto L_0x0814;
    L_0x0839:
        r0 = r56;
        r0 = r0.mPaddingTop;
        r42 = r0;
        goto L_0x0769;
    L_0x0841:
        r0 = r56;
        r9 = r0.mPaddingBottom;
        goto L_0x0771;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.View.<init>(android.content.Context, android.util.AttributeSet, int, int):void");
    }

    View() {
        InputEventConsistencyVerifier inputEventConsistencyVerifier;
        this.DEBUG_ELASTIC = true;
        this.isElasticEnabled = true;
        this.mCurrentAnimation = null;
        this.mRecreateDisplayList = false;
        this.mID = -1;
        this.mAccessibilityViewId = -1;
        this.mAccessibilityCursorPosition = -1;
        this.mTag = null;
        this.mTransientStateCount = 0;
        this.mClipBounds = null;
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mLabelForId = -1;
        this.mAccessibilityTraversalBeforeId = -1;
        this.mAccessibilityTraversalAfterId = -1;
        this.mLeftPaddingDefined = false;
        this.mRightPaddingDefined = false;
        this.mOldWidthMeasureSpec = Integer.MIN_VALUE;
        this.mOldHeightMeasureSpec = Integer.MIN_VALUE;
        this.mDrawableState = null;
        this.mOutlineProvider = ViewOutlineProvider.BACKGROUND;
        this.mNextFocusLeftId = -1;
        this.mNextFocusRightId = -1;
        this.mNextFocusUpId = -1;
        this.mNextFocusDownId = -1;
        this.mNextFocusForwardId = -1;
        this.mPendingCheckForTap = null;
        this.mTouchDelegate = null;
        this.mDrawingCacheBackgroundColor = 0;
        this.mAnimator = null;
        this.mTwExtraPaddingBottomForPreference = 0;
        this.mLayerType = 0;
        this.mEnablePenGesture = false;
        this.mDisablePenGestureforfactorytest = true;
        this.isPenSideButton = false;
        if (InputEventConsistencyVerifier.isInstrumentationEnabled()) {
            inputEventConsistencyVerifier = new InputEventConsistencyVerifier(this, 0);
        } else {
            inputEventConsistencyVerifier = null;
        }
        this.mInputEventConsistencyVerifier = inputEventConsistencyVerifier;
        this.mTwScrollingByScrollbar = false;
        this.mTwScrollingVertical = true;
        this.mTwVerticalScrollbarRect = new Rect();
        this.mTwHorizontalScrollbarRect = new Rect();
        this.mWritingBuddy = null;
        this.mIsWritingBuddyEnabled = false;
        this.mRootViewCheckForDialog = null;
        this.mIsInDialog = false;
        this.mIsSetFingerHovedInAppWidget = true;
        this.mScrollBarPositionPadding = 0;
        this.mNeededToChangedScrollBarPosition = false;
        this.mSkipRtlCheck = false;
        this.mSmartClipDataTag = null;
        this.mSmartClipDataExtractionListener = null;
        this.mHoverPopup = null;
        this.mIsDetachedFromWindow = false;
        this.mHoverPopupType = 0;
        this.mHoverPopupToolTypeByApp = 0;
        this.mIsSetContextMenuZOrderToTop = false;
        this.mResources = null;
        this.mRenderNode = RenderNode.create(getClass().getName(), this);
    }

    public boolean setImageFilter(ImageFilter filter) {
        int i = 0;
        if (filter != null) {
            this.mRenderNodeImageFilter = filter;
        } else {
            this.mRenderNodeImageFilter = null;
        }
        if (!SystemProperties.getBoolean("debug.hwui.imagefilter.enable", true)) {
            Log.e("HWUI_IMAGE_FILTER", "Should enable HWUI_IMAGE_FILTER: setprop debug.hwui.imagefilter.enable true.");
            this.mRenderNode.setImageFilter(null);
            invalidate(true);
            return false;
        } else if (this.mLayerType != 2) {
            Log.e("HWUI_IMAGE_FILTER", "Should set LayerType to LAYER_TYPE_HARDWARE.");
            setLayerType(2, null);
            Log.d("HWUI_IMAGE_FILTER", "LAYER_TYPE_HARDWARE setted automaticly.");
            return false;
        } else {
            if (SystemProperties.getBoolean("debug.hwui.imagefilter.log", false)) {
                String str = "HWUI_IMAGE_FILTER";
                String str2 = "{0x%x}->View.setImageFilter(0x%x)";
                Object[] objArr = new Object[2];
                objArr[0] = Integer.valueOf(hashCode());
                if (filter != null) {
                    i = filter.hashCode();
                }
                objArr[1] = Integer.valueOf(i);
                Log.d(str, String.format(str2, objArr));
            }
            this.mRenderNode.setImageFilter(this.mRenderNodeImageFilter);
            syncImageFilterClipRects();
            invalidate(true);
            return true;
        }
    }

    public void invalidateFilterClipRect() {
        syncImageFilterClipRects();
        invalidate(true);
    }

    public void clearImageFilterClipRect() {
        this.mRenderNodeImageFilterClipRects.clear();
        syncImageFilterClipRects();
        invalidate(true);
    }

    public int getImageFilterClipRectCount() {
        return this.mRenderNodeImageFilterClipRects.size();
    }

    public ClipRect getImageFilterClipRectAt(int index) {
        if (index >= this.mRenderNodeImageFilterClipRects.size()) {
            return null;
        }
        return (ClipRect) this.mRenderNodeImageFilterClipRects.get(index);
    }

    public void addImageFilterClipRect(Rect r, float cornerRadius) {
        if (r != null) {
            ClipRect c = new ClipRect();
            c.mClipRect = new Rect(r);
            c.mCornerRadius = cornerRadius;
            this.mRenderNodeImageFilterClipRects.add(c);
            syncImageFilterClipRects();
            invalidate(true);
        }
    }

    public void removeImageFilterClipRect(ClipRect clipRect) {
        this.mRenderNodeImageFilterClipRects.remove(clipRect);
        syncImageFilterClipRects();
        invalidate(true);
    }

    protected void syncImageFilterClipRects() {
        this.mRenderNode.clearImageFilterClipRects();
        Iterator i$ = this.mRenderNodeImageFilterClipRects.iterator();
        while (i$.hasNext()) {
            ClipRect i = (ClipRect) i$.next();
            this.mRenderNode.addImageFilterClipRect(i.mClipRect.left, i.mClipRect.top, i.mClipRect.width(), i.mClipRect.height(), i.mCornerRadius);
        }
    }

    private static SparseArray<String> getAttributeMap() {
        if (mAttributeMap == null) {
            mAttributeMap = new SparseArray();
        }
        return mAttributeMap;
    }

    private void saveAttributeData(AttributeSet attrs, TypedArray t) {
        int j;
        int attrsCount = attrs == null ? 0 : attrs.getAttributeCount();
        int indexCount = t.getIndexCount();
        String[] attributes = new String[((attrsCount + indexCount) * 2)];
        int i = 0;
        for (j = 0; j < attrsCount; j++) {
            attributes[i] = attrs.getAttributeName(j);
            attributes[i + 1] = attrs.getAttributeValue(j);
            i += 2;
        }
        Resources res = t.getResources();
        SparseArray<String> attributeMap = getAttributeMap();
        for (j = 0; j < indexCount; j++) {
            int index = t.getIndex(j);
            if (t.hasValueOrEmpty(index)) {
                int resourceId = t.getResourceId(index, 0);
                if (resourceId != 0) {
                    String resourceName = (String) attributeMap.get(resourceId);
                    if (resourceName == null) {
                        try {
                            resourceName = res.getResourceName(resourceId);
                        } catch (NotFoundException e) {
                            resourceName = "0x" + Integer.toHexString(resourceId);
                        }
                        attributeMap.put(resourceId, resourceName);
                    }
                    attributes[i] = resourceName;
                    attributes[i + 1] = t.getString(index);
                    i += 2;
                }
            }
        }
        String[] trimmed = new String[i];
        System.arraycopy(attributes, 0, trimmed, 0, i);
        this.mAttributes = trimmed;
    }

    protected void twEnableHorizontalScrollbar() {
        this.mViewFlags = (this.mViewFlags & -769) | 256;
    }

    public String toString() {
        char c;
        char c2 = 'F';
        char c3 = 'D';
        StringBuilder out = new StringBuilder(128);
        out.append(getClass().getName());
        out.append('{');
        out.append(Integer.toHexString(System.identityHashCode(this)));
        out.append(' ');
        switch (this.mViewFlags & 12) {
            case 0:
                out.append('V');
                break;
            case 4:
                out.append('I');
                break;
            case 8:
                out.append('G');
                break;
            default:
                out.append('.');
                break;
        }
        if ((this.mViewFlags & 1) == 1) {
            c = 'F';
        } else {
            c = '.';
        }
        out.append(c);
        if ((this.mViewFlags & 32) == 0) {
            c = DateFormat.DAY;
        } else {
            c = '.';
        }
        out.append(c);
        if ((this.mViewFlags & 128) == 128) {
            c = '.';
        } else {
            c = 'D';
        }
        out.append(c);
        if ((this.mViewFlags & 256) != 0) {
            c = 'H';
        } else {
            c = '.';
        }
        out.append(c);
        if ((this.mViewFlags & 512) != 0) {
            c = 'V';
        } else {
            c = '.';
        }
        out.append(c);
        if ((this.mViewFlags & 16384) != 0) {
            c = 'C';
        } else {
            c = '.';
        }
        out.append(c);
        if ((this.mViewFlags & 2097152) != 0) {
            c = DateFormat.STANDALONE_MONTH;
        } else {
            c = '.';
        }
        out.append(c);
        if ((this.mViewFlags & 8388608) != 0) {
            c = 'X';
        } else {
            c = '.';
        }
        out.append(c);
        out.append(' ');
        if ((this.mPrivateFlags & 8) != 0) {
            c = 'R';
        } else {
            c = '.';
        }
        out.append(c);
        if ((this.mPrivateFlags & 2) == 0) {
            c2 = '.';
        }
        out.append(c2);
        if ((this.mPrivateFlags & 4) != 0) {
            c = 'S';
        } else {
            c = '.';
        }
        out.append(c);
        if ((this.mPrivateFlags & 33554432) != 0) {
            out.append('p');
        } else {
            out.append((this.mPrivateFlags & 16384) != 0 ? 'P' : '.');
        }
        if ((this.mPrivateFlags & 268435456) != 0) {
            c = 'H';
        } else {
            c = '.';
        }
        out.append(c);
        if ((this.mPrivateFlags & 1073741824) != 0) {
            c = DateFormat.CAPITAL_AM_PM;
        } else {
            c = '.';
        }
        out.append(c);
        if ((this.mPrivateFlags & Integer.MIN_VALUE) != 0) {
            c = 'I';
        } else {
            c = '.';
        }
        out.append(c);
        if ((this.mPrivateFlags & PFLAG_DIRTY_MASK) == 0) {
            c3 = '.';
        }
        out.append(c3);
        out.append(' ');
        out.append(this.mLeft);
        out.append(',');
        out.append(this.mTop);
        out.append('-');
        out.append(this.mRight);
        out.append(',');
        out.append(this.mBottom);
        int id = getId();
        if (id != -1) {
            out.append(" #");
            out.append(Integer.toHexString(id));
            Resources r = this.mResources;
            if (Resources.resourceHasPackage(id) && r != null) {
                String pkgname;
                switch (-16777216 & id) {
                    case 16777216:
                        pkgname = ZenModeConfig.SYSTEM_AUTHORITY;
                        break;
                    case 2130706432:
                        pkgname = "app";
                        break;
                    default:
                        try {
                            pkgname = r.getResourcePackageName(id);
                            break;
                        } catch (NotFoundException e) {
                            break;
                        }
                }
                String typename = r.getResourceTypeName(id);
                String entryname = r.getResourceEntryName(id);
                out.append(" ");
                out.append(pkgname);
                out.append(":");
                out.append(typename);
                out.append("/");
                out.append(entryname);
            }
        }
        out.append("}");
        return out.toString();
    }

    public void setXmlFilePath(CharSequence path) {
        if (Build.IS_SYSTEM_SECURE) {
            this.mXmlFilePath = path;
        }
    }

    protected void initializeFadingEdge(TypedArray a) {
        TypedArray arr = this.mContext.obtainStyledAttributes(R.styleable.View);
        initializeFadingEdgeInternal(arr);
        arr.recycle();
    }

    protected void initializeFadingEdgeInternal(TypedArray a) {
        initScrollCache();
        this.mScrollCache.fadingEdgeLength = a.getDimensionPixelSize(25, ViewConfiguration.get(this.mContext).getScaledFadingEdgeLength());
    }

    public int getVerticalFadingEdgeLength() {
        if (isVerticalFadingEdgeEnabled()) {
            ScrollabilityCache cache = this.mScrollCache;
            if (cache != null) {
                return cache.fadingEdgeLength;
            }
        }
        return 0;
    }

    public void setFadingEdgeLength(int length) {
        initScrollCache();
        this.mScrollCache.fadingEdgeLength = length;
    }

    public int getHorizontalFadingEdgeLength() {
        if (isHorizontalFadingEdgeEnabled()) {
            ScrollabilityCache cache = this.mScrollCache;
            if (cache != null) {
                return cache.fadingEdgeLength;
            }
        }
        return 0;
    }

    public int getVerticalScrollbarWidth() {
        ScrollabilityCache cache = this.mScrollCache;
        if (cache == null) {
            return 0;
        }
        ScrollBarDrawable scrollBar = cache.scrollBar;
        if (scrollBar == null) {
            return 0;
        }
        int size = scrollBar.getSize(true);
        if (size <= 0) {
            return cache.scrollBarSize;
        }
        return size;
    }

    protected int getHorizontalScrollbarHeight() {
        ScrollabilityCache cache = this.mScrollCache;
        if (cache == null) {
            return 0;
        }
        ScrollBarDrawable scrollBar = cache.scrollBar;
        if (scrollBar == null) {
            return 0;
        }
        int size = scrollBar.getSize(false);
        if (size <= 0) {
            return cache.scrollBarSize;
        }
        return size;
    }

    protected void initializeScrollbars(TypedArray a) {
        TypedArray arr = this.mContext.obtainStyledAttributes(R.styleable.View);
        initializeScrollbarsInternal(arr);
        arr.recycle();
    }

    protected void initializeScrollbarsInternal(TypedArray a) {
        initScrollCache();
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        if (scrollabilityCache.scrollBar == null) {
            scrollabilityCache.scrollBar = new ScrollBarDrawable(this);
            scrollabilityCache.scrollBar.setCallback(this);
            scrollabilityCache.scrollBar.setState(getDrawableState());
        }
        boolean fadeScrollbars = a.getBoolean(47, true);
        if (!fadeScrollbars) {
            scrollabilityCache.state = 1;
        }
        scrollabilityCache.fadeScrollBars = fadeScrollbars;
        scrollabilityCache.scrollBarFadeDuration = a.getInt(45, ViewConfiguration.getScrollBarFadeDuration());
        scrollabilityCache.scrollBarDefaultDelayBeforeFade = a.getInt(46, ViewConfiguration.getScrollDefaultDelay());
        scrollabilityCache.scrollBarSize = a.getDimensionPixelSize(1, ViewConfiguration.get(this.mContext).getScaledScrollBarSize());
        scrollabilityCache.scrollBar.setHorizontalTrackDrawable(a.getDrawable(4));
        Drawable thumb = a.getDrawable(2);
        if (thumb != null) {
            scrollabilityCache.scrollBar.setHorizontalThumbDrawable(thumb);
        }
        if (a.getBoolean(6, false)) {
            scrollabilityCache.scrollBar.setAlwaysDrawHorizontalTrack(true);
        }
        Drawable track = a.getDrawable(5);
        scrollabilityCache.scrollBar.setVerticalTrackDrawable(track);
        thumb = a.getDrawable(3);
        if (thumb != null) {
            scrollabilityCache.scrollBar.setVerticalThumbDrawable(thumb);
        }
        if (a.getBoolean(7, false)) {
            scrollabilityCache.scrollBar.setAlwaysDrawVerticalTrack(true);
        }
        int layoutDirection = getLayoutDirection();
        if (track != null) {
            track.setLayoutDirection(layoutDirection);
        }
        if (thumb != null) {
            thumb.setLayoutDirection(layoutDirection);
        }
        resolvePadding();
    }

    private void initializeScrollIndicatorsInternal() {
        boolean mIsDeviceDefault = false;
        boolean mIsDeviceDefaultDark = true;
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, true);
        if (outValue.data != 0) {
            mIsDeviceDefault = true;
        }
        TypedValue outValueDarkTheme = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.parentIsDeviceDefaultDark, outValueDarkTheme, true);
        if (outValueDarkTheme.data == 0) {
            mIsDeviceDefaultDark = false;
        }
        if (this.mScrollIndicatorDrawable != null) {
            return;
        }
        if (!mIsDeviceDefault || mIsDeviceDefaultDark) {
            this.mScrollIndicatorDrawable = this.mContext.getDrawable(R.drawable.scroll_indicator_material);
        } else {
            this.mScrollIndicatorDrawable = this.mContext.getDrawable(R.drawable.tw_scroll_indicator_material);
        }
    }

    private void initScrollCache() {
        if (this.mScrollCache == null) {
            this.mScrollCache = new ScrollabilityCache(ViewConfiguration.get(this.mContext), this);
        }
    }

    private ScrollabilityCache getScrollCache() {
        initScrollCache();
        return this.mScrollCache;
    }

    public void setVerticalScrollbarPosition(int position) {
        if (this.mVerticalScrollbarPosition != position) {
            this.mVerticalScrollbarPosition = position;
            computeOpaqueFlags();
            resolvePadding();
        }
    }

    public int getVerticalScrollbarPosition() {
        return this.mVerticalScrollbarPosition;
    }

    public void setScrollIndicators(int indicators) {
        setScrollIndicators(indicators, 63);
    }

    public void setScrollIndicators(int indicators, int mask) {
        mask = (mask << 8) & SCROLL_INDICATORS_PFLAG3_MASK;
        indicators = (indicators << 8) & mask;
        int updatedFlags = indicators | (this.mPrivateFlags3 & (mask ^ -1));
        if (this.mPrivateFlags3 != updatedFlags) {
            this.mPrivateFlags3 = updatedFlags;
            if (indicators != 0) {
                initializeScrollIndicatorsInternal();
            }
            invalidate();
        }
    }

    public int getScrollIndicators() {
        return (this.mPrivateFlags3 & SCROLL_INDICATORS_PFLAG3_MASK) >>> 8;
    }

    public void setTwHorizontalScrollbarPosition(int position) {
        if (this.mTwHorizontalScrollbarPosition != position) {
            this.mTwHorizontalScrollbarPosition = position;
            computeOpaqueFlags();
            resolvePadding();
        }
    }

    public int getTwHorizontalScrollbarPosition() {
        return this.mTwHorizontalScrollbarPosition;
    }

    ListenerInfo getListenerInfo() {
        if (this.mListenerInfo != null) {
            return this.mListenerInfo;
        }
        this.mListenerInfo = new ListenerInfo();
        return this.mListenerInfo;
    }

    public void setOnScrollChangeListener(OnScrollChangeListener l) {
        getListenerInfo().mOnScrollChangeListener = l;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        getListenerInfo().mOnFocusChangeListener = l;
    }

    public void addOnLayoutChangeListener(OnLayoutChangeListener listener) {
        ListenerInfo li = getListenerInfo();
        if (li.mOnLayoutChangeListeners == null) {
            li.mOnLayoutChangeListeners = new ArrayList();
        }
        if (!li.mOnLayoutChangeListeners.contains(listener)) {
            li.mOnLayoutChangeListeners.add(listener);
        }
    }

    public void removeOnLayoutChangeListener(OnLayoutChangeListener listener) {
        ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnLayoutChangeListeners != null) {
            li.mOnLayoutChangeListeners.remove(listener);
        }
    }

    public void addOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
        ListenerInfo li = getListenerInfo();
        if (li.mOnAttachStateChangeListeners == null) {
            li.mOnAttachStateChangeListeners = new CopyOnWriteArrayList();
        }
        li.mOnAttachStateChangeListeners.add(listener);
    }

    public void removeOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
        ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnAttachStateChangeListeners != null) {
            li.mOnAttachStateChangeListeners.remove(listener);
        }
    }

    public OnFocusChangeListener getOnFocusChangeListener() {
        ListenerInfo li = this.mListenerInfo;
        return li != null ? li.mOnFocusChangeListener : null;
    }

    public void setOnClickListener(OnClickListener l) {
        if (!isClickable()) {
            setClickable(true);
        }
        getListenerInfo().mOnClickListener = l;
    }

    public boolean hasOnClickListeners() {
        ListenerInfo li = this.mListenerInfo;
        return (li == null || li.mOnClickListener == null) ? false : true;
    }

    public void setOnLongClickListener(OnLongClickListener l) {
        if (!isLongClickable()) {
            setLongClickable(true);
        }
        getListenerInfo().mOnLongClickListener = l;
    }

    public void setOnContextClickListener(OnContextClickListener l) {
        if (!isContextClickable()) {
            setContextClickable(true);
        }
        getListenerInfo().mOnContextClickListener = l;
    }

    public void setOnCreateContextMenuListener(OnCreateContextMenuListener l) {
        if (!isLongClickable()) {
            setLongClickable(true);
        }
        getListenerInfo().mOnCreateContextMenuListener = l;
    }

    public boolean performClick() {
        boolean result;
        ListenerInfo li = this.mListenerInfo;
        if (li == null || li.mOnClickListener == null) {
            result = false;
        } else {
            playSoundEffect(0);
            li.mOnClickListener.onClick(this);
            result = true;
        }
        sendAccessibilityEvent(1);
        return result;
    }

    public boolean callOnClick() {
        ListenerInfo li = this.mListenerInfo;
        if (li == null || li.mOnClickListener == null) {
            return false;
        }
        li.mOnClickListener.onClick(this);
        return true;
    }

    public boolean performLongClick() {
        sendAccessibilityEvent(2);
        boolean handled = false;
        ListenerInfo li = this.mListenerInfo;
        if (!(li == null || li.mOnLongClickListener == null)) {
            handled = li.mOnLongClickListener.onLongClick(this);
        }
        if (!handled) {
            handled = showContextMenu();
        }
        if (handled) {
            performHapticFeedback(0);
        }
        return handled;
    }

    public boolean performContextClick() {
        sendAccessibilityEvent(8388608);
        boolean handled = false;
        ListenerInfo li = this.mListenerInfo;
        if (!(li == null || li.mOnContextClickListener == null)) {
            handled = li.mOnContextClickListener.onContextClick(this);
        }
        if (handled) {
            performHapticFeedback(6);
        }
        return handled;
    }

    protected boolean performButtonActionOnTouchDown(MotionEvent event) {
        if (event.getToolType(0) != 3 || (event.getButtonState() & 2) == 0) {
            return false;
        }
        showContextMenu(event.getX(), event.getY(), event.getMetaState());
        this.mPrivateFlags |= 67108864;
        return true;
    }

    public boolean showContextMenu() {
        return getParent().showContextMenuForChild(this);
    }

    public boolean showContextMenu(float x, float y, int metaState) {
        return showContextMenu();
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        return startActionMode(callback, 0);
    }

    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        ViewParent parent = getParent();
        if (parent == null) {
            return null;
        }
        try {
            return parent.startActionModeForChild(this, callback, type);
        } catch (AbstractMethodError e) {
            return parent.startActionModeForChild(this, callback);
        }
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        this.mStartActivityRequestWho = "@android:view:" + System.identityHashCode(this);
        getContext().startActivityForResult(this.mStartActivityRequestWho, intent, requestCode, null);
    }

    public boolean dispatchActivityResult(String who, int requestCode, int resultCode, Intent data) {
        if (this.mStartActivityRequestWho == null || !this.mStartActivityRequestWho.equals(who)) {
            return false;
        }
        onActivityResult(requestCode, resultCode, data);
        this.mStartActivityRequestWho = null;
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void setOnKeyListener(OnKeyListener l) {
        getListenerInfo().mOnKeyListener = l;
    }

    public void setOnTouchListener(OnTouchListener l) {
        getListenerInfo().mOnTouchListener = l;
    }

    public void setOnGenericMotionListener(OnGenericMotionListener l) {
        getListenerInfo().mOnGenericMotionListener = l;
    }

    public void setOnHoverListener(OnHoverListener l) {
        getListenerInfo().mOnHoverListener = l;
    }

    public void setOnAirButtonHoverListener(OnHoverListener l) {
        getListenerInfo().mOnAirButtonHoverListener = l;
    }

    public void setOnDragListener(OnDragListener l) {
        getListenerInfo().mOnDragListener = l;
    }

    void handleFocusGainInternal(int direction, Rect previouslyFocusedRect) {
        if ((this.mPrivateFlags & 2) == 0) {
            this.mPrivateFlags |= 2;
            View oldFocus = this.mAttachInfo != null ? getRootView().findFocus() : null;
            if (this.mParent != null) {
                this.mParent.requestChildFocus(this, this);
            }
            if (this.mAttachInfo != null) {
                this.mAttachInfo.mTreeObserver.dispatchOnGlobalFocusChange(oldFocus, this);
            }
            onFocusChanged(true, direction, previouslyFocusedRect);
            refreshDrawableState();
        }
    }

    public void getHotspotBounds(Rect outRect) {
        Drawable background = getBackground();
        if (background != null) {
            background.getHotspotBounds(outRect);
        } else {
            getBoundsOnScreen(outRect);
        }
    }

    public void twSetDrawDuringWindowsAnimating(boolean value) {
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl != null) {
            viewRootImpl.mTwDrawDuringWindowsAnimating = value;
            Log.d(VIEW_LOG_TAG, "mTwDrawDuringWindowsAnimating=" + value);
            return;
        }
        Log.e(VIEW_LOG_TAG, "There is no ViewRootImpl.");
    }

    public boolean requestRectangleOnScreen(Rect rectangle) {
        return requestRectangleOnScreen(rectangle, false);
    }

    public boolean requestRectangleOnScreen(Rect rectangle, boolean immediate) {
        if (this.mParent == null) {
            return false;
        }
        View child = this;
        RectF position = this.mAttachInfo != null ? this.mAttachInfo.mTmpTransformRect : new RectF();
        position.set(rectangle);
        position.offset((float) (-getScrollX()), (float) (-getScrollY()));
        ViewParent parent = this.mParent;
        boolean scrolled = false;
        while (parent != null) {
            rectangle.set((int) position.left, (int) position.top, (int) position.right, (int) position.bottom);
            scrolled |= parent.requestChildRectangleOnScreen(child, rectangle, immediate);
            if (!child.hasIdentityMatrix()) {
                child.getMatrix().mapRect(position);
            }
            position.offset((float) child.mLeft, (float) child.mTop);
            if (!(parent instanceof View)) {
                return scrolled;
            }
            View parentView = (View) parent;
            position.offset((float) (-parentView.getScrollX()), (float) (-parentView.getScrollY()));
            child = parentView;
            parent = child.getParent();
        }
        return scrolled;
    }

    public void clearFocus() {
        clearFocusInternal(null, true, true);
    }

    void clearFocusInternal(View focused, boolean propagate, boolean refocus) {
        if ((this.mPrivateFlags & 2) != 0) {
            this.mPrivateFlags &= -3;
            if (propagate && this.mParent != null) {
                this.mParent.clearChildFocus(this);
            }
            onFocusChanged(false, 0, null);
            refreshDrawableState();
            boolean flag = false;
            if (this.mContext.getPackageManager().hasSystemFeature("com.sec.feature.folder_type") && this.mParent != null && (this instanceof EditText) && (this.mParent instanceof NumberPicker)) {
                flag = true;
            }
            if (!propagate) {
                return;
            }
            if (!refocus || (!flag && !rootViewRequestFocus())) {
                notifyGlobalFocusCleared(this);
            }
        }
    }

    void notifyGlobalFocusCleared(View oldFocus) {
        if (oldFocus != null && this.mAttachInfo != null) {
            this.mAttachInfo.mTreeObserver.dispatchOnGlobalFocusChange(oldFocus, null);
        }
    }

    boolean rootViewRequestFocus() {
        View root = getRootView();
        return root != null && root.requestFocus();
    }

    void unFocus(View focused) {
        clearFocusInternal(focused, false, false);
    }

    @ExportedProperty(category = "focus")
    public boolean hasFocus() {
        return (this.mPrivateFlags & 2) != 0;
    }

    public boolean hasFocusable() {
        if (!isFocusableInTouchMode()) {
            for (ViewParent p = this.mParent; p instanceof ViewGroup; p = p.getParent()) {
                if (((ViewGroup) p).shouldBlockFocusForTouchscreen()) {
                    return false;
                }
            }
        }
        if ((this.mViewFlags & 12) == 0 && isFocusable()) {
            return true;
        }
        return false;
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (gainFocus) {
            sendAccessibilityEvent(8);
        } else {
            notifyViewAccessibilityStateChangedIfNeeded(0);
        }
        InputMethodManager imm = InputMethodManager.peekInstance();
        if (!gainFocus) {
            if (isPressed()) {
                setPressed(false);
            }
            if (!(imm == null || this.mAttachInfo == null || !this.mAttachInfo.mHasWindowFocus)) {
                imm.focusOut(this);
            }
            onFocusLost();
        } else if (!(imm == null || this.mAttachInfo == null || !this.mAttachInfo.mHasWindowFocus)) {
            imm.focusIn(this);
        }
        invalidate(true);
        ListenerInfo li = this.mListenerInfo;
        if (!(li == null || li.mOnFocusChangeListener == null)) {
            li.mOnFocusChangeListener.onFocusChange(this, gainFocus);
        }
        if (this.mAttachInfo != null) {
            this.mAttachInfo.mKeyDispatchState.reset(this);
        }
    }

    public void sendAccessibilityEvent(int eventType) {
        if (this.mAccessibilityDelegate != null) {
            this.mAccessibilityDelegate.sendAccessibilityEvent(this, eventType);
        } else {
            sendAccessibilityEventInternal(eventType);
        }
    }

    public void announceForAccessibility(CharSequence text) {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled() && this.mParent != null) {
            AccessibilityEvent event = AccessibilityEvent.obtain(16384);
            onInitializeAccessibilityEvent(event);
            event.getText().add(text);
            event.setContentDescription(null);
            this.mParent.requestSendAccessibilityEvent(this, event);
        }
    }

    public void sendAccessibilityEventInternal(int eventType) {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            sendAccessibilityEventUnchecked(AccessibilityEvent.obtain(eventType));
        }
    }

    public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
        if (this.mAccessibilityDelegate != null) {
            this.mAccessibilityDelegate.sendAccessibilityEventUnchecked(this, event);
        } else {
            sendAccessibilityEventUncheckedInternal(event);
        }
    }

    public void sendAccessibilityEventUncheckedInternal(AccessibilityEvent event) {
        if (isShown()) {
            onInitializeAccessibilityEvent(event);
            if ((event.getEventType() & POPULATING_ACCESSIBILITY_EVENT_TYPES) != 0) {
                dispatchPopulateAccessibilityEvent(event);
            }
            ViewParent parent = getParent();
            if (parent != null) {
                parent.requestSendAccessibilityEvent(this, event);
            }
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        if (this.mAccessibilityDelegate != null) {
            return this.mAccessibilityDelegate.dispatchPopulateAccessibilityEvent(this, event);
        }
        return dispatchPopulateAccessibilityEventInternal(event);
    }

    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return false;
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        if (this.mAccessibilityDelegate != null) {
            this.mAccessibilityDelegate.onPopulateAccessibilityEvent(this, event);
        } else {
            onPopulateAccessibilityEventInternal(event);
        }
    }

    public void onPopulateAccessibilityEventInternal(AccessibilityEvent event) {
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        if (this.mAccessibilityDelegate != null) {
            this.mAccessibilityDelegate.onInitializeAccessibilityEvent(this, event);
        } else {
            onInitializeAccessibilityEventInternal(event);
        }
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        event.setSource(this);
        event.setClassName(getAccessibilityClassName());
        event.setPackageName(getContext().getPackageName());
        event.setEnabled(isEnabled());
        event.setContentDescription(this.mContentDescription);
        switch (event.getEventType()) {
            case 8:
                ArrayList<View> focusablesTempList = this.mAttachInfo != null ? this.mAttachInfo.mTempArrayList : new ArrayList();
                getRootView().addFocusables(focusablesTempList, 2, 0);
                event.setItemCount(focusablesTempList.size());
                event.setCurrentItemIndex(focusablesTempList.indexOf(this));
                if (this.mAttachInfo != null) {
                    focusablesTempList.clear();
                    return;
                }
                return;
            case 8192:
                CharSequence text = getIterableTextForAccessibility();
                if (text != null && text.length() > 0) {
                    event.setFromIndex(getAccessibilitySelectionStart());
                    event.setToIndex(getAccessibilitySelectionEnd());
                    event.setItemCount(text.length());
                    return;
                }
                return;
            default:
                return;
        }
    }

    public AccessibilityNodeInfo createAccessibilityNodeInfo() {
        if (this.mAccessibilityDelegate != null) {
            return this.mAccessibilityDelegate.createAccessibilityNodeInfo(this);
        }
        return createAccessibilityNodeInfoInternal();
    }

    public AccessibilityNodeInfo createAccessibilityNodeInfoInternal() {
        AccessibilityNodeProvider provider = getAccessibilityNodeProvider();
        if (provider != null) {
            return provider.createAccessibilityNodeInfo(-1);
        }
        AccessibilityNodeInfo info = AccessibilityNodeInfo.obtain(this);
        onInitializeAccessibilityNodeInfo(info);
        return info;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        if (this.mAccessibilityDelegate != null) {
            this.mAccessibilityDelegate.onInitializeAccessibilityNodeInfo(this, info);
        } else {
            onInitializeAccessibilityNodeInfoInternal(info);
        }
    }

    public void getBoundsOnScreen(Rect outRect) {
        getBoundsOnScreen(outRect, false);
    }

    public void getBoundsOnScreen(Rect outRect, boolean clipToParent) {
        if (this.mAttachInfo != null) {
            RectF position = this.mAttachInfo.mTmpTransformRect;
            position.set(0.0f, 0.0f, (float) (this.mRight - this.mLeft), (float) (this.mBottom - this.mTop));
            if (!hasIdentityMatrix()) {
                getMatrix().mapRect(position);
            }
            position.offset((float) this.mLeft, (float) this.mTop);
            ViewParent parent = this.mParent;
            while (parent instanceof View) {
                View parentView = (View) parent;
                position.offset((float) (-parentView.mScrollX), (float) (-parentView.mScrollY));
                if (clipToParent) {
                    position.left = Math.max(position.left, 0.0f);
                    position.top = Math.max(position.top, 0.0f);
                    position.right = Math.min(position.right, (float) parentView.getWidth());
                    position.bottom = Math.min(position.bottom, (float) parentView.getHeight());
                }
                if (!parentView.hasIdentityMatrix()) {
                    parentView.getMatrix().mapRect(position);
                }
                position.offset((float) parentView.mLeft, (float) parentView.mTop);
                parent = parentView.mParent;
            }
            if (parent instanceof ViewRootImpl) {
                position.offset(0.0f, (float) (-((ViewRootImpl) parent).mCurScrollY));
            }
            ViewRootImpl viewRoot = getViewRootImpl();
            if (!(viewRoot == null || (viewRoot.mScaleFactor.x == 1.0f && viewRoot.mScaleFactor.y == 1.0f))) {
                position.left *= viewRoot.mScaleFactor.x;
                position.right *= viewRoot.mScaleFactor.x;
                position.top *= viewRoot.mScaleFactor.y;
                position.bottom *= viewRoot.mScaleFactor.y;
            }
            position.offset((float) this.mAttachInfo.mWindowLeft, (float) this.mAttachInfo.mWindowTop);
            outRect.set((int) (position.left + 0.5f), (int) (position.top + 0.5f), (int) (position.right + 0.5f), (int) (position.bottom + 0.5f));
        }
    }

    public CharSequence getAccessibilityClassName() {
        return View.class.getName();
    }

    public void onProvideStructure(ViewStructure structure) {
        int id = this.mID;
        if (id <= 0 || (-16777216 & id) == 0 || (Spanned.SPAN_PRIORITY & id) == 0 || (65535 & id) == 0) {
            structure.setId(id, null, null, null);
        } else {
            String entry;
            String type;
            String pkg;
            try {
                Resources res = getResources();
                entry = res.getResourceEntryName(id);
                type = res.getResourceTypeName(id);
                pkg = res.getResourcePackageName(id);
            } catch (NotFoundException e) {
                pkg = null;
                type = null;
                entry = null;
            }
            structure.setId(id, pkg, type, entry);
        }
        structure.setDimens(this.mLeft, this.mTop, this.mScrollX, this.mScrollY, this.mRight - this.mLeft, this.mBottom - this.mTop);
        if (!hasIdentityMatrix()) {
            structure.setTransformation(getMatrix());
        }
        structure.setElevation(getZ());
        structure.setVisibility(getVisibility());
        structure.setEnabled(isEnabled());
        if (isClickable()) {
            structure.setClickable(true);
        }
        if (isFocusable()) {
            structure.setFocusable(true);
        }
        if (isFocused()) {
            structure.setFocused(true);
        }
        if (isAccessibilityFocused()) {
            structure.setAccessibilityFocused(true);
        }
        if (isSelected()) {
            structure.setSelected(true);
        }
        if (isActivated()) {
            structure.setActivated(true);
        }
        if (isLongClickable()) {
            structure.setLongClickable(true);
        }
        if (this instanceof Checkable) {
            structure.setCheckable(true);
            if (((Checkable) this).isChecked()) {
                structure.setChecked(true);
            }
        }
        if (isContextClickable()) {
            structure.setContextClickable(true);
        }
        structure.setClassName(getAccessibilityClassName().toString());
        structure.setContentDescription(getContentDescription());
    }

    public void onProvideVirtualStructure(ViewStructure structure) {
        AccessibilityNodeProvider provider = getAccessibilityNodeProvider();
        if (provider != null) {
            AccessibilityNodeInfo info = createAccessibilityNodeInfo();
            structure.setChildCount(1);
            populateVirtualStructure(structure.newChild(0), provider, info);
            info.recycle();
        }
    }

    private void populateVirtualStructure(ViewStructure structure, AccessibilityNodeProvider provider, AccessibilityNodeInfo info) {
        if (info == null) {
            Object obj;
            String str = VIEW_LOG_TAG;
            StringBuilder append = new StringBuilder().append("AccessibilityNodeInfo is NULL of populateVirtualStructure() provider=");
            if (provider == null) {
                obj = "NULL";
            } else {
                AccessibilityNodeProvider accessibilityNodeProvider = provider;
            }
            append = append.append(obj).append(" accessibilityDelegate=");
            if (this.mAccessibilityDelegate == null) {
                obj = "NULL";
            } else {
                obj = this.mAccessibilityDelegate;
            }
            Log.e(str, append.append(obj).append(" this=").append(this).toString());
        }
        structure.setId(AccessibilityNodeInfo.getVirtualDescendantId(info.getSourceNodeId()), null, null, null);
        Rect rect = structure.getTempRect();
        info.getBoundsInParent(rect);
        structure.setDimens(rect.left, rect.top, 0, 0, rect.width(), rect.height());
        structure.setVisibility(0);
        structure.setEnabled(info.isEnabled());
        if (info.isClickable()) {
            structure.setClickable(true);
        }
        if (info.isFocusable()) {
            structure.setFocusable(true);
        }
        if (info.isFocused()) {
            structure.setFocused(true);
        }
        if (info.isAccessibilityFocused()) {
            structure.setAccessibilityFocused(true);
        }
        if (info.isSelected()) {
            structure.setSelected(true);
        }
        if (info.isLongClickable()) {
            structure.setLongClickable(true);
        }
        if (info.isCheckable()) {
            structure.setCheckable(true);
            if (info.isChecked()) {
                structure.setChecked(true);
            }
        }
        if (info.isContextClickable()) {
            structure.setContextClickable(true);
        }
        CharSequence cname = info.getClassName();
        structure.setClassName(cname != null ? cname.toString() : null);
        structure.setContentDescription(info.getContentDescription());
        if (!(info.getText() == null && info.getError() == null)) {
            structure.setText(info.getText(), info.getTextSelectionStart(), info.getTextSelectionEnd());
        }
        int NCHILDREN = info.getChildCount();
        if (NCHILDREN > 0) {
            structure.setChildCount(NCHILDREN);
            for (int i = 0; i < NCHILDREN; i++) {
                AccessibilityNodeInfo cinfo = provider.createAccessibilityNodeInfo(AccessibilityNodeInfo.getVirtualDescendantId(info.getChildId(i)));
                populateVirtualStructure(structure.newChild(i), provider, cinfo);
                cinfo.recycle();
            }
        }
    }

    public void dispatchProvideStructure(ViewStructure structure) {
        if (isAssistBlocked()) {
            structure.setClassName(getAccessibilityClassName().toString());
            structure.setAssistBlocked(true);
            return;
        }
        onProvideStructure(structure);
        onProvideVirtualStructure(structure);
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        if (this.mAttachInfo != null) {
            View rootView;
            View next;
            Rect bounds = this.mAttachInfo.mTmpInvalRect;
            getDrawingRect(bounds);
            info.setBoundsInParent(bounds);
            getBoundsOnScreen(bounds, true);
            info.setBoundsInScreen(bounds);
            ViewParent parent = getParentForAccessibility();
            if (parent instanceof View) {
                info.setParent((View) parent);
            }
            if (this.mID != -1) {
                rootView = getRootView();
                if (rootView == null) {
                    rootView = this;
                }
                View label = rootView.findLabelForView(this, this.mID);
                if (label != null) {
                    info.setLabeledBy(label);
                }
                if ((this.mAttachInfo.mAccessibilityFetchFlags & 16) != 0 && Resources.resourceHasPackage(this.mID)) {
                    try {
                        info.setViewIdResourceName(getResources().getResourceName(this.mID));
                    } catch (NotFoundException e) {
                    }
                }
            }
            if (this.mLabelForId != -1) {
                rootView = getRootView();
                if (rootView == null) {
                    rootView = this;
                }
                View labeled = rootView.findViewInsideOutShouldExist(this, this.mLabelForId);
                if (labeled != null) {
                    info.setLabelFor(labeled);
                }
            }
            if (this.mAccessibilityTraversalBeforeId != -1) {
                rootView = getRootView();
                if (rootView == null) {
                    rootView = this;
                }
                next = rootView.findViewInsideOutShouldExist(this, this.mAccessibilityTraversalBeforeId);
                if (next != null && next.includeForAccessibility()) {
                    info.setTraversalBefore(next);
                }
            }
            if (this.mAccessibilityTraversalAfterId != -1) {
                rootView = getRootView();
                if (rootView == null) {
                    rootView = this;
                }
                next = rootView.findViewInsideOutShouldExist(this, this.mAccessibilityTraversalAfterId);
                if (next != null && next.includeForAccessibility()) {
                    info.setTraversalAfter(next);
                }
            }
            info.setVisibleToUser(isVisibleToUser());
            info.setPackageName(this.mContext.getPackageName());
            info.setClassName(getAccessibilityClassName());
            info.setContentDescription(getContentDescription());
            info.setEnabled(isEnabled());
            info.setClickable(isClickable());
            info.setFocusable(isFocusable());
            info.setFocused(isFocused());
            info.setAccessibilityFocused(isAccessibilityFocused());
            info.setSelected(isSelected());
            info.setLongClickable(isLongClickable());
            info.setContextClickable(isContextClickable());
            info.setLiveRegion(getAccessibilityLiveRegion());
            info.addAction(4);
            info.addAction(8);
            if (isFocusable()) {
                if (isFocused()) {
                    info.addAction(2);
                } else {
                    info.addAction(1);
                }
            }
            if (isAccessibilityFocused()) {
                info.addAction(128);
            } else {
                info.addAction(64);
            }
            if (isClickable() && isEnabled()) {
                info.addAction(16);
            }
            if (isLongClickable() && isEnabled()) {
                info.addAction(32);
            }
            if (isContextClickable() && isEnabled()) {
                info.addAction(AccessibilityAction.ACTION_CONTEXT_CLICK);
            }
            CharSequence text = getIterableTextForAccessibility();
            if (text != null && text.length() > 0) {
                info.setTextSelection(getAccessibilitySelectionStart(), getAccessibilitySelectionEnd());
                info.addAction(131072);
                info.addAction(256);
                info.addAction(512);
                info.setMovementGranularities(11);
            }
            info.addAction(AccessibilityAction.ACTION_SHOW_ON_SCREEN);
        }
    }

    private View findLabelForView(View view, int labeledId) {
        if (this.mMatchLabelForPredicate == null) {
            this.mMatchLabelForPredicate = new MatchLabelForPredicate();
        }
        this.mMatchLabelForPredicate.mLabeledId = labeledId;
        return findViewByPredicateInsideOut(view, this.mMatchLabelForPredicate);
    }

    protected boolean isVisibleToUser() {
        return isVisibleToUser(null);
    }

    protected boolean isVisibleToUser(Rect boundInView) {
        if (this.mAttachInfo == null || this.mAttachInfo.mWindowVisibility != 0) {
            return false;
        }
        ViewParent viewParent = this;
        while (viewParent instanceof View) {
            View view = (View) viewParent;
            if (view.getAlpha() <= 0.0f || view.getTransitionAlpha() <= 0.0f || view.getVisibility() != 0) {
                return false;
            }
            viewParent = view.mParent;
        }
        Rect visibleRect = this.mAttachInfo.mTmpInvalRect;
        Point offset = this.mAttachInfo.mPoint;
        if (!getGlobalVisibleRect(visibleRect, offset)) {
            return false;
        }
        if (boundInView == null) {
            return true;
        }
        visibleRect.offset(-offset.x, -offset.y);
        return boundInView.intersect(visibleRect);
    }

    public AccessibilityDelegate getAccessibilityDelegate() {
        return this.mAccessibilityDelegate;
    }

    public void setAccessibilityDelegate(AccessibilityDelegate delegate) {
        this.mAccessibilityDelegate = delegate;
    }

    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        if (this.mAccessibilityDelegate != null) {
            return this.mAccessibilityDelegate.getAccessibilityNodeProvider(this);
        }
        return null;
    }

    public int getAccessibilityViewId() {
        if (this.mAccessibilityViewId == -1) {
            int i = sNextAccessibilityViewId;
            sNextAccessibilityViewId = i + 1;
            this.mAccessibilityViewId = i;
        }
        return this.mAccessibilityViewId;
    }

    public int getAccessibilityWindowId() {
        return this.mAttachInfo != null ? this.mAttachInfo.mAccessibilityWindowId : Integer.MAX_VALUE;
    }

    @ExportedProperty(category = "accessibility")
    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    @RemotableViewMethod
    public void setContentDescription(CharSequence contentDescription) {
        if (this.mContentDescription == null) {
            if (contentDescription == null) {
                return;
            }
        } else if (this.mContentDescription.equals(contentDescription)) {
            return;
        }
        this.mContentDescription = contentDescription;
        boolean nonEmptyDesc = contentDescription != null && contentDescription.length() > 0;
        if (nonEmptyDesc && getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
            notifySubtreeAccessibilityStateChangedIfNeeded();
            return;
        }
        notifyViewAccessibilityStateChangedIfNeeded(4);
    }

    @RemotableViewMethod
    public void setAccessibilityTraversalBefore(int beforeId) {
        if (this.mAccessibilityTraversalBeforeId != beforeId) {
            this.mAccessibilityTraversalBeforeId = beforeId;
            notifyViewAccessibilityStateChangedIfNeeded(0);
        }
    }

    public int getAccessibilityTraversalBefore() {
        return this.mAccessibilityTraversalBeforeId;
    }

    @RemotableViewMethod
    public void setAccessibilityTraversalAfter(int afterId) {
        if (this.mAccessibilityTraversalAfterId != afterId) {
            this.mAccessibilityTraversalAfterId = afterId;
            notifyViewAccessibilityStateChangedIfNeeded(0);
        }
    }

    public int getAccessibilityTraversalAfter() {
        return this.mAccessibilityTraversalAfterId;
    }

    @ExportedProperty(category = "accessibility")
    public int getLabelFor() {
        return this.mLabelForId;
    }

    @RemotableViewMethod
    public void setLabelFor(int id) {
        if (this.mLabelForId != id) {
            this.mLabelForId = id;
            if (this.mLabelForId != -1 && this.mID == -1) {
                this.mID = generateViewId();
            }
            notifyViewAccessibilityStateChangedIfNeeded(0);
        }
    }

    protected void onFocusLost() {
        resetPressedState();
    }

    private void resetPressedState() {
        if ((this.mViewFlags & 32) != 32 && isPressed()) {
            setPressed(false);
            if (!this.mHasPerformedLongPress) {
                removeLongPressCallback();
            }
        }
    }

    @ExportedProperty(category = "focus")
    public boolean isFocused() {
        return (this.mPrivateFlags & 2) != 0;
    }

    public View findFocus() {
        return (this.mPrivateFlags & 2) != 0 ? this : null;
    }

    public boolean isScrollContainer() {
        return (this.mPrivateFlags & 1048576) != 0;
    }

    public void setScrollContainer(boolean isScrollContainer) {
        if (isScrollContainer) {
            if (this.mAttachInfo != null && (this.mPrivateFlags & 1048576) == 0) {
                this.mAttachInfo.mScrollContainers.add(this);
                this.mPrivateFlags |= 1048576;
            }
            this.mPrivateFlags |= 524288;
            return;
        }
        if ((this.mPrivateFlags & 1048576) != 0) {
            this.mAttachInfo.mScrollContainers.remove(this);
        }
        this.mPrivateFlags &= -1572865;
    }

    public int getDrawingCacheQuality() {
        return this.mViewFlags & DRAWING_CACHE_QUALITY_MASK;
    }

    public void setDrawingCacheQuality(int quality) {
        setFlags(quality, DRAWING_CACHE_QUALITY_MASK);
    }

    public boolean getKeepScreenOn() {
        return (this.mViewFlags & 67108864) != 0;
    }

    public void setKeepScreenOn(boolean keepScreenOn) {
        setFlags(keepScreenOn ? 67108864 : 0, 67108864);
    }

    public int getNextFocusLeftId() {
        return this.mNextFocusLeftId;
    }

    public void setNextFocusLeftId(int nextFocusLeftId) {
        this.mNextFocusLeftId = nextFocusLeftId;
    }

    public int getNextFocusRightId() {
        return this.mNextFocusRightId;
    }

    public void setNextFocusRightId(int nextFocusRightId) {
        this.mNextFocusRightId = nextFocusRightId;
    }

    public int getNextFocusUpId() {
        return this.mNextFocusUpId;
    }

    public void setNextFocusUpId(int nextFocusUpId) {
        this.mNextFocusUpId = nextFocusUpId;
    }

    public int getNextFocusDownId() {
        return this.mNextFocusDownId;
    }

    public void setNextFocusDownId(int nextFocusDownId) {
        this.mNextFocusDownId = nextFocusDownId;
    }

    public int getNextFocusForwardId() {
        return this.mNextFocusForwardId;
    }

    public void setNextFocusForwardId(int nextFocusForwardId) {
        this.mNextFocusForwardId = nextFocusForwardId;
    }

    public boolean isShown() {
        View current = this;
        while ((current.mViewFlags & 12) == 0) {
            ViewParent parent = current.mParent;
            if (parent == null) {
                return false;
            }
            if (!(parent instanceof View)) {
                return true;
            }
            current = (View) parent;
            if (current == null) {
                return false;
            }
        }
        return false;
    }

    protected boolean fitSystemWindows(Rect insets) {
        if ((this.mPrivateFlags3 & 32) != 0) {
            return fitSystemWindowsInt(insets);
        }
        if (insets == null) {
            return false;
        }
        try {
            this.mPrivateFlags3 |= 64;
            boolean isConsumed = dispatchApplyWindowInsets(new WindowInsets(insets)).isConsumed();
            return isConsumed;
        } finally {
            this.mPrivateFlags3 &= -65;
        }
    }

    private boolean fitSystemWindowsInt(Rect insets) {
        if ((this.mViewFlags & 2) != 2) {
            return false;
        }
        this.mUserPaddingStart = Integer.MIN_VALUE;
        this.mUserPaddingEnd = Integer.MIN_VALUE;
        Rect localInsets = (Rect) sThreadLocal.get();
        if (localInsets == null) {
            localInsets = new Rect();
            sThreadLocal.set(localInsets);
        }
        boolean res = computeFitSystemWindows(insets, localInsets);
        this.mUserPaddingLeftInitial = localInsets.left;
        this.mUserPaddingRightInitial = localInsets.right;
        internalSetPadding(localInsets.left, localInsets.top, localInsets.right, localInsets.bottom);
        return res;
    }

    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if ((this.mPrivateFlags3 & 64) == 0) {
            if (fitSystemWindows(insets.getSystemWindowInsets())) {
                return insets.consumeSystemWindowInsets();
            }
            return insets;
        } else if (fitSystemWindowsInt(insets.getSystemWindowInsets())) {
            return insets.consumeSystemWindowInsets();
        } else {
            return insets;
        }
    }

    public void setOnApplyWindowInsetsListener(OnApplyWindowInsetsListener listener) {
        getListenerInfo().mOnApplyWindowInsetsListener = listener;
    }

    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        try {
            this.mPrivateFlags3 |= 32;
            WindowInsets onApplyWindowInsets;
            if (this.mListenerInfo == null || this.mListenerInfo.mOnApplyWindowInsetsListener == null) {
                onApplyWindowInsets = onApplyWindowInsets(insets);
                this.mPrivateFlags3 &= -33;
                return onApplyWindowInsets;
            }
            onApplyWindowInsets = this.mListenerInfo.mOnApplyWindowInsetsListener.onApplyWindowInsets(this, insets);
            return onApplyWindowInsets;
        } finally {
            this.mPrivateFlags3 &= -33;
        }
    }

    public WindowInsets getRootWindowInsets() {
        if (this.mAttachInfo != null) {
            return this.mAttachInfo.mViewRootImpl.getWindowInsets(false);
        }
        return null;
    }

    protected boolean computeFitSystemWindows(Rect inoutInsets, Rect outLocalInsets) {
        if ((this.mViewFlags & 2048) == 0 || this.mAttachInfo == null || ((this.mAttachInfo.mSystemUiVisibility & SYSTEM_UI_LAYOUT_FLAGS) == 0 && !this.mAttachInfo.mOverscanRequested)) {
            outLocalInsets.set(inoutInsets);
            inoutInsets.set(0, 0, 0, 0);
            return true;
        }
        Rect overscan = this.mAttachInfo.mOverscanInsets;
        outLocalInsets.set(overscan);
        inoutInsets.left -= overscan.left;
        inoutInsets.top -= overscan.top;
        inoutInsets.right -= overscan.right;
        inoutInsets.bottom -= overscan.bottom;
        return false;
    }

    public WindowInsets computeSystemWindowInsets(WindowInsets in, Rect outLocalInsets) {
        if ((this.mViewFlags & 2048) == 0 || this.mAttachInfo == null || (this.mAttachInfo.mSystemUiVisibility & SYSTEM_UI_LAYOUT_FLAGS) == 0) {
            outLocalInsets.set(in.getSystemWindowInsets());
            return in.consumeSystemWindowInsets();
        }
        outLocalInsets.set(0, 0, 0, 0);
        return in;
    }

    public void setFitsSystemWindows(boolean fitSystemWindows) {
        setFlags(fitSystemWindows ? 2 : 0, 2);
    }

    @ExportedProperty
    public boolean getFitsSystemWindows() {
        return (this.mViewFlags & 2) == 2;
    }

    public boolean fitsSystemWindows() {
        return getFitsSystemWindows();
    }

    public void requestFitSystemWindows() {
        if (this.mParent != null) {
            this.mParent.requestFitSystemWindows();
        }
    }

    public void requestApplyInsets() {
        requestFitSystemWindows();
    }

    public void makeOptionalFitsSystemWindows() {
        setFlags(2048, 2048);
    }

    public void getOutsets(Rect outOutsetRect) {
        if (this.mAttachInfo != null) {
            outOutsetRect.set(this.mAttachInfo.mOutsets);
        } else {
            outOutsetRect.setEmpty();
        }
    }

    @ExportedProperty(mapping = {@IntToString(from = 0, to = "VISIBLE"), @IntToString(from = 4, to = "INVISIBLE"), @IntToString(from = 8, to = "GONE")})
    public int getVisibility() {
        return this.mViewFlags & 12;
    }

    @RemotableViewMethod
    public void setVisibility(int visibility) {
        setFlags(visibility, 12);
    }

    @ExportedProperty
    public boolean isEnabled() {
        return (this.mViewFlags & 32) == 0;
    }

    @RemotableViewMethod
    public void setEnabled(boolean enabled) {
        if (enabled != isEnabled()) {
            setFlags(enabled ? 0 : 32, 32);
            refreshDrawableState();
            invalidate(true);
            if (!enabled) {
                cancelPendingInputEvents();
            }
        }
    }

    public void setFocusable(boolean focusable) {
        int i = 0;
        if (!focusable) {
            setFlags(0, 262144);
        }
        if (focusable) {
            i = 1;
        }
        setFlags(i, 1);
    }

    public void setFocusableInTouchMode(boolean focusableInTouchMode) {
        setFlags(focusableInTouchMode ? 262144 : 0, 262144);
        if (focusableInTouchMode) {
            setFlags(1, 1);
        }
    }

    public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
        setFlags(soundEffectsEnabled ? 134217728 : 0, 134217728);
    }

    @ExportedProperty
    public boolean isSoundEffectsEnabled() {
        return 134217728 == (this.mViewFlags & 134217728);
    }

    public void setHapticFeedbackEnabled(boolean hapticFeedbackEnabled) {
        setFlags(hapticFeedbackEnabled ? 268435456 : 0, 268435456);
    }

    @ExportedProperty
    public boolean isHapticFeedbackEnabled() {
        return 268435456 == (this.mViewFlags & 268435456);
    }

    @ExportedProperty(category = "layout", mapping = {@IntToString(from = 0, to = "LTR"), @IntToString(from = 1, to = "RTL"), @IntToString(from = 2, to = "INHERIT"), @IntToString(from = 3, to = "LOCALE")})
    public int getRawLayoutDirection() {
        return (this.mPrivateFlags2 & 12) >> 2;
    }

    @RemotableViewMethod
    public void setLayoutDirection(int layoutDirection) {
        if (getRawLayoutDirection() != layoutDirection) {
            this.mPrivateFlags2 &= -13;
            resetRtlProperties();
            this.mPrivateFlags2 |= (layoutDirection << 2) & 12;
            if (!this.mSkipRtlCheck) {
                resolveRtlPropertiesIfNeeded();
            }
            requestLayout();
            invalidate(true);
        }
    }

    @ExportedProperty(category = "layout", mapping = {@IntToString(from = 0, to = "RESOLVED_DIRECTION_LTR"), @IntToString(from = 1, to = "RESOLVED_DIRECTION_RTL")})
    public int getLayoutDirection() {
        if (getContext().getApplicationInfo().targetSdkVersion < 17 && !isSystemApp()) {
            this.mPrivateFlags2 |= 32;
            return 0;
        } else if ((this.mPrivateFlags2 & 16) == 16) {
            return 1;
        } else {
            return 0;
        }
    }

    @ExportedProperty(category = "layout")
    public boolean isLayoutRtl() {
        return getLayoutDirection() == 1;
    }

    @ExportedProperty(category = "layout")
    public boolean hasTransientState() {
        return (this.mPrivateFlags2 & Integer.MIN_VALUE) == Integer.MIN_VALUE;
    }

    public void setHasTransientState(boolean hasTransientState) {
        this.mTransientStateCount = hasTransientState ? this.mTransientStateCount + 1 : this.mTransientStateCount - 1;
        if (this.mTransientStateCount < 0) {
            this.mTransientStateCount = 0;
            Log.e(VIEW_LOG_TAG, "hasTransientState decremented below 0: unmatched pair of setHasTransientState calls");
        } else if ((hasTransientState && this.mTransientStateCount == 1) || (!hasTransientState && this.mTransientStateCount == 0)) {
            int i;
            int i2 = Integer.MAX_VALUE & this.mPrivateFlags2;
            if (hasTransientState) {
                i = Integer.MIN_VALUE;
            } else {
                i = 0;
            }
            this.mPrivateFlags2 = i | i2;
            if (this.mParent != null) {
                try {
                    this.mParent.childHasTransientStateChanged(this, hasTransientState);
                } catch (AbstractMethodError e) {
                    Log.e(VIEW_LOG_TAG, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", e);
                }
            }
        }
    }

    public boolean isAttachedToWindow() {
        return this.mAttachInfo != null;
    }

    public boolean isLaidOut() {
        return (this.mPrivateFlags3 & 4) == 4;
    }

    public void setWillNotDraw(boolean willNotDraw) {
        setFlags(willNotDraw ? 128 : 0, 128);
    }

    @ExportedProperty(category = "drawing")
    public boolean willNotDraw() {
        return (this.mViewFlags & 128) == 128;
    }

    public void setWillNotCacheDrawing(boolean willNotCacheDrawing) {
        setFlags(willNotCacheDrawing ? 131072 : 0, 131072);
    }

    @ExportedProperty(category = "drawing")
    public boolean willNotCacheDrawing() {
        return (this.mViewFlags & 131072) == 131072;
    }

    @ExportedProperty
    public boolean isClickable() {
        return (this.mViewFlags & 16384) == 16384;
    }

    public void setClickable(boolean clickable) {
        setFlags(clickable ? 16384 : 0, 16384);
    }

    public boolean isLongClickable() {
        return (this.mViewFlags & 2097152) == 2097152;
    }

    public void setLongClickable(boolean longClickable) {
        setFlags(longClickable ? 2097152 : 0, 2097152);
    }

    public boolean isContextClickable() {
        return (this.mViewFlags & 8388608) == 8388608;
    }

    public void setContextClickable(boolean contextClickable) {
        setFlags(contextClickable ? 8388608 : 0, 8388608);
    }

    private void setPressed(boolean pressed, float x, float y) {
        if (pressed) {
            drawableHotspotChanged(x, y);
        }
        setPressed(pressed);
    }

    public void setPressed(boolean pressed) {
        boolean z;
        boolean needsRefresh = true;
        if ((this.mPrivateFlags & 16384) == 16384) {
            z = true;
        } else {
            z = false;
        }
        if (pressed == z) {
            needsRefresh = false;
        }
        if (pressed) {
            this.mPrivateFlags |= 16384;
        } else {
            this.mPrivateFlags &= -16385;
        }
        if (needsRefresh) {
            refreshDrawableState();
        }
        dispatchSetPressed(pressed);
    }

    protected void dispatchSetPressed(boolean pressed) {
    }

    @ExportedProperty
    public boolean isPressed() {
        return (this.mPrivateFlags & 16384) == 16384;
    }

    public boolean isAssistBlocked() {
        return (this.mPrivateFlags3 & 16384) != 0;
    }

    public void setAssistBlocked(boolean enabled) {
        if (enabled) {
            this.mPrivateFlags3 |= 16384;
        } else {
            this.mPrivateFlags3 &= -16385;
        }
    }

    public boolean isSaveEnabled() {
        return (this.mViewFlags & 65536) != 65536;
    }

    public void setSaveEnabled(boolean enabled) {
        int i;
        if (enabled) {
            i = 0;
        } else {
            i = 65536;
        }
        setFlags(i, 65536);
    }

    @ExportedProperty
    public boolean getFilterTouchesWhenObscured() {
        return (this.mViewFlags & 1024) != 0;
    }

    public void setFilterTouchesWhenObscured(boolean enabled) {
        setFlags(enabled ? 1024 : 0, 1024);
    }

    public boolean isSaveFromParentEnabled() {
        return (this.mViewFlags & 536870912) != 536870912;
    }

    public void setSaveFromParentEnabled(boolean enabled) {
        int i;
        if (enabled) {
            i = 0;
        } else {
            i = 536870912;
        }
        setFlags(i, 536870912);
    }

    @ExportedProperty(category = "focus")
    public final boolean isFocusable() {
        return 1 == (this.mViewFlags & 1);
    }

    @ExportedProperty
    public final boolean isFocusableInTouchMode() {
        return 262144 == (this.mViewFlags & 262144);
    }

    public View focusSearch(int direction) {
        if (this.mParent != null) {
            return this.mParent.focusSearch(this, direction);
        }
        return null;
    }

    public boolean dispatchUnhandledMove(View focused, int direction) {
        return false;
    }

    View findUserSetNextFocus(View root, int direction) {
        switch (direction) {
            case 1:
                if (this.mID == -1) {
                    return null;
                }
                final int id = this.mID;
                return root.findViewByPredicateInsideOut(this, new Predicate<View>() {
                    public boolean apply(View t) {
                        return t.mNextFocusForwardId == id;
                    }
                });
            case 2:
                if (this.mNextFocusForwardId != -1) {
                    return findViewInsideOutShouldExist(root, this.mNextFocusForwardId);
                }
                return null;
            case 17:
                if (this.mNextFocusLeftId != -1) {
                    return findViewInsideOutShouldExist(root, this.mNextFocusLeftId);
                }
                return null;
            case 33:
                if (this.mNextFocusUpId != -1) {
                    return findViewInsideOutShouldExist(root, this.mNextFocusUpId);
                }
                return null;
            case 66:
                if (this.mNextFocusRightId != -1) {
                    return findViewInsideOutShouldExist(root, this.mNextFocusRightId);
                }
                return null;
            case 130:
                if (this.mNextFocusDownId != -1) {
                    return findViewInsideOutShouldExist(root, this.mNextFocusDownId);
                }
                return null;
            default:
                return null;
        }
    }

    private View findViewInsideOutShouldExist(View root, int id) {
        if (this.mMatchIdPredicate == null) {
            this.mMatchIdPredicate = new MatchIdPredicate();
        }
        this.mMatchIdPredicate.mId = id;
        View result = root.findViewByPredicateInsideOut(this, this.mMatchIdPredicate);
        if (result == null) {
            Log.w(VIEW_LOG_TAG, "couldn't find view with id " + id);
        }
        return result;
    }

    public ArrayList<View> getFocusables(int direction) {
        ArrayList<View> result = new ArrayList(24);
        addFocusables(result, direction);
        return result;
    }

    public void addFocusables(ArrayList<View> views, int direction) {
        addFocusables(views, direction, 1);
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (views == null || !isFocusable()) {
            return;
        }
        if ((focusableMode & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) {
            views.add(this);
        }
    }

    public void findViewsWithText(ArrayList<View> outViews, CharSequence searched, int flags) {
        if (getAccessibilityNodeProvider() != null) {
            if ((flags & 4) != 0) {
                outViews.add(this);
            }
        } else if ((flags & 2) != 0 && searched != null && searched.length() > 0 && this.mContentDescription != null && this.mContentDescription.length() > 0) {
            if (this.mContentDescription.toString().toLowerCase().contains(searched.toString().toLowerCase())) {
                outViews.add(this);
            }
        }
    }

    public ArrayList<View> getTouchables() {
        ArrayList<View> result = new ArrayList();
        addTouchables(result);
        return result;
    }

    public void addTouchables(ArrayList<View> views) {
        int viewFlags = this.mViewFlags;
        if (((viewFlags & 16384) == 16384 || (viewFlags & 2097152) == 2097152 || (viewFlags & 8388608) == 8388608) && (viewFlags & 32) == 0) {
            views.add(this);
        }
    }

    @ExportedProperty(category = "accessibility")
    public boolean isAccessibilityFocused() {
        return (this.mPrivateFlags2 & 67108864) != 0;
    }

    private boolean isScreenReaderEnabled() {
        return Secure.getInt(this.mContext.getContentResolver(), "enabled_accessibility_samsung_screen_reader", 0) != 0;
    }

    public boolean requestAccessibilityFocus() {
        AccessibilityManager manager = AccessibilityManager.getInstance(this.mContext);
        if (!manager.isEnabled()) {
            return false;
        }
        boolean universalSwitchEnabled;
        if (Secure.getInt(this.mContext.getContentResolver(), "universal_switch_enabled", 0) == 1) {
            universalSwitchEnabled = true;
        } else {
            universalSwitchEnabled = false;
        }
        if ((!universalSwitchEnabled && !isScreenReaderEnabled() && !manager.isTouchExplorationEnabled()) || (this.mViewFlags & 12) != 0 || (this.mPrivateFlags2 & 67108864) != 0) {
            return false;
        }
        this.mPrivateFlags2 |= 67108864;
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl != null) {
            viewRootImpl.setAccessibilityFocus(this, null);
        }
        invalidate();
        sendAccessibilityEvent(32768);
        return true;
    }

    public void clearAccessibilityFocus() {
        clearAccessibilityFocusNoCallbacks();
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl != null) {
            View focusHost = viewRootImpl.getAccessibilityFocusedHost();
            if (focusHost != null && ViewRootImpl.isViewDescendantOf(focusHost, this)) {
                viewRootImpl.setAccessibilityFocus(null, null);
            }
        }
    }

    private void sendAccessibilityHoverEvent(int eventType) {
        View source = this;
        while (!source.includeForAccessibility()) {
            ViewParent parent = source.getParent();
            if (parent instanceof View) {
                source = (View) parent;
            } else {
                return;
            }
        }
        source.sendAccessibilityEvent(eventType);
    }

    void clearAccessibilityFocusNoCallbacks() {
        if ((this.mPrivateFlags2 & 67108864) != 0) {
            this.mPrivateFlags2 &= -67108865;
            invalidate();
            sendAccessibilityEvent(65536);
        }
    }

    public final boolean requestFocus() {
        return requestFocus(130);
    }

    public final boolean requestFocus(int direction) {
        return requestFocus(direction, null);
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return requestFocusNoSearch(direction, previouslyFocusedRect);
    }

    private boolean requestFocusNoSearch(int direction, Rect previouslyFocusedRect) {
        if ((this.mViewFlags & 1) != 1 || (this.mViewFlags & 12) != 0) {
            return false;
        }
        if ((isInTouchMode() && 262144 != (this.mViewFlags & 262144)) || hasAncestorThatBlocksDescendantFocus()) {
            return false;
        }
        handleFocusGainInternal(direction, previouslyFocusedRect);
        return true;
    }

    public final boolean requestFocusFromTouch() {
        if (isInTouchMode()) {
            ViewRootImpl viewRoot = getViewRootImpl();
            if (viewRoot != null) {
                viewRoot.ensureTouchMode(false);
            }
        }
        return requestFocus(130);
    }

    private boolean hasAncestorThatBlocksDescendantFocus() {
        boolean focusableInTouchMode = isFocusableInTouchMode();
        ViewParent ancestor = this.mParent;
        while (ancestor instanceof ViewGroup) {
            ViewGroup vgAncestor = (ViewGroup) ancestor;
            if (vgAncestor.getDescendantFocusability() == 393216 || (!focusableInTouchMode && vgAncestor.shouldBlockFocusForTouchscreen())) {
                return true;
            }
            ancestor = vgAncestor.getParent();
        }
        return false;
    }

    @ExportedProperty(category = "accessibility", mapping = {@IntToString(from = 0, to = "auto"), @IntToString(from = 1, to = "yes"), @IntToString(from = 2, to = "no"), @IntToString(from = 4, to = "noHideDescendants")})
    public int getImportantForAccessibility() {
        return (this.mPrivateFlags2 & PFLAG2_IMPORTANT_FOR_ACCESSIBILITY_MASK) >> 20;
    }

    public void setAccessibilityLiveRegion(int mode) {
        if (mode != getAccessibilityLiveRegion()) {
            this.mPrivateFlags2 &= -25165825;
            this.mPrivateFlags2 |= (mode << 23) & 25165824;
            notifyViewAccessibilityStateChangedIfNeeded(0);
        }
    }

    public int getAccessibilityLiveRegion() {
        return (this.mPrivateFlags2 & 25165824) >> 23;
    }

    public void setImportantForAccessibility(int mode) {
        boolean oldIncludeForAccessibility = true;
        int oldMode = getImportantForAccessibility();
        if (mode != oldMode) {
            boolean maySkipNotify = oldMode == 0 || mode == 0;
            if (!(maySkipNotify && includeForAccessibility())) {
                oldIncludeForAccessibility = false;
            }
            this.mPrivateFlags2 &= -7340033;
            this.mPrivateFlags2 |= (mode << 20) & PFLAG2_IMPORTANT_FOR_ACCESSIBILITY_MASK;
            if (maySkipNotify && oldIncludeForAccessibility == includeForAccessibility()) {
                notifyViewAccessibilityStateChangedIfNeeded(0);
            } else {
                notifySubtreeAccessibilityStateChangedIfNeeded();
            }
        }
    }

    public boolean isImportantForAccessibility() {
        int mode = (this.mPrivateFlags2 & PFLAG2_IMPORTANT_FOR_ACCESSIBILITY_MASK) >> 20;
        if (mode == 2 || mode == 4) {
            return false;
        }
        for (ViewParent parent = this.mParent; parent instanceof View; parent = parent.getParent()) {
            if (((View) parent).getImportantForAccessibility() == 4) {
                return false;
            }
        }
        boolean z = mode == 1 || isActionableForAccessibility() || hasListenersForAccessibility() || getAccessibilityNodeProvider() != null || getAccessibilityLiveRegion() != 0;
        return z;
    }

    public ViewParent getParentForAccessibility() {
        if (!(this.mParent instanceof View)) {
            return null;
        }
        if (this.mParent.includeForAccessibility()) {
            return this.mParent;
        }
        return this.mParent.getParentForAccessibility();
    }

    public void addChildrenForAccessibility(ArrayList<View> arrayList) {
    }

    public boolean includeForAccessibility() {
        if (this.mAttachInfo == null) {
            return false;
        }
        if ((this.mAttachInfo.mAccessibilityFetchFlags & 8) != 0 || isImportantForAccessibility()) {
            return true;
        }
        return false;
    }

    public boolean isActionableForAccessibility() {
        return isClickable() || isLongClickable() || isFocusable();
    }

    private boolean hasListenersForAccessibility() {
        ListenerInfo info = getListenerInfo();
        return (this.mTouchDelegate == null && info.mOnKeyListener == null && info.mOnTouchListener == null && info.mOnGenericMotionListener == null && info.mOnHoverListener == null && info.mOnDragListener == null) ? false : true;
    }

    public void notifyViewAccessibilityStateChangedIfNeeded(int changeType) {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled() && this.mAttachInfo != null && Thread.currentThread().getId() == 1) {
            if (this.mSendViewStateChangedAccessibilityEvent == null) {
                this.mSendViewStateChangedAccessibilityEvent = new SendViewStateChangedAccessibilityEvent();
            }
            this.mSendViewStateChangedAccessibilityEvent.runOrPost(changeType);
        }
    }

    public void notifySubtreeAccessibilityStateChangedIfNeeded() {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled() && this.mAttachInfo != null && (this.mPrivateFlags2 & 134217728) == 0) {
            this.mPrivateFlags2 |= 134217728;
            if (this.mParent != null) {
                try {
                    this.mParent.notifySubtreeAccessibilityStateChanged(this, this, 1);
                } catch (AbstractMethodError e) {
                    Log.e(VIEW_LOG_TAG, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", e);
                }
            }
        }
    }

    public void setTransitionVisibility(int visibility) {
        this.mViewFlags = (this.mViewFlags & -13) | visibility;
    }

    void resetSubtreeAccessibilityStateChanged() {
        this.mPrivateFlags2 &= -134217729;
    }

    public boolean dispatchNestedPrePerformAccessibilityAction(int action, Bundle arguments) {
        for (ViewParent p = getParent(); p != null; p = p.getParent()) {
            if (p.onNestedPrePerformAccessibilityAction(this, action, arguments)) {
                return true;
            }
        }
        return false;
    }

    public boolean performAccessibilityAction(int action, Bundle arguments) {
        if (this.mAccessibilityDelegate != null) {
            return this.mAccessibilityDelegate.performAccessibilityAction(this, action, arguments);
        }
        return performAccessibilityActionInternal(action, arguments);
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        int end = -1;
        if (isNestedScrollingEnabled() && ((action == 8192 || action == 4096 || action == R.id.accessibilityActionScrollUp || action == R.id.accessibilityActionScrollLeft || action == R.id.accessibilityActionScrollDown || action == R.id.accessibilityActionScrollRight) && dispatchNestedPrePerformAccessibilityAction(action, arguments))) {
            return true;
        }
        switch (action) {
            case 1:
                if (!hasFocus()) {
                    getViewRootImpl().ensureTouchMode(false);
                    return requestFocus();
                }
                break;
            case 2:
                if (hasFocus()) {
                    clearFocus();
                    if (isFocused()) {
                        return false;
                    }
                    return true;
                }
                break;
            case 4:
                if (!isSelected()) {
                    setSelected(true);
                    return isSelected();
                }
                break;
            case 8:
                if (isSelected()) {
                    setSelected(false);
                    if (isSelected()) {
                        return false;
                    }
                    return true;
                }
                break;
            case 16:
                if (isClickable()) {
                    performClick();
                    return true;
                }
                break;
            case 32:
                if (isLongClickable()) {
                    performLongClick();
                    return true;
                }
                break;
            case 64:
                if (!isAccessibilityFocused()) {
                    return requestAccessibilityFocus();
                }
                break;
            case 128:
                if (isAccessibilityFocused()) {
                    clearAccessibilityFocus();
                    return true;
                }
                break;
            case 256:
                if (arguments != null) {
                    return traverseAtGranularity(arguments.getInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT), true, arguments.getBoolean(AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN));
                }
                break;
            case 512:
                if (arguments != null) {
                    return traverseAtGranularity(arguments.getInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT), false, arguments.getBoolean(AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN));
                }
                break;
            case 131072:
                if (getIterableTextForAccessibility() == null) {
                    return false;
                }
                int start;
                if (arguments != null) {
                    start = arguments.getInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, -1);
                } else {
                    start = -1;
                }
                if (arguments != null) {
                    end = arguments.getInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, -1);
                }
                if (!(getAccessibilitySelectionStart() == start && getAccessibilitySelectionEnd() == end) && start == end) {
                    setAccessibilitySelection(start, end);
                    notifyViewAccessibilityStateChangedIfNeeded(0);
                    return true;
                }
            case R.id.accessibilityActionShowOnScreen /*16908342*/:
                if (this.mAttachInfo != null) {
                    Rect r = this.mAttachInfo.mTmpInvalRect;
                    getDrawingRect(r);
                    return requestRectangleOnScreen(r, true);
                }
                break;
            case R.id.accessibilityActionContextClick /*16908348*/:
                if (isContextClickable()) {
                    performContextClick();
                    return true;
                }
                break;
        }
        return false;
    }

    private boolean traverseAtGranularity(int granularity, boolean forward, boolean extendSelection) {
        CharSequence text = getIterableTextForAccessibility();
        if (text == null || text.length() == 0) {
            return false;
        }
        TextSegmentIterator iterator = getIteratorForGranularity(granularity);
        if (iterator == null) {
            return false;
        }
        int current = getAccessibilitySelectionEnd();
        if (current == -1) {
            current = forward ? 0 : text.length();
        }
        int[] range = forward ? iterator.following(current) : iterator.preceding(current);
        if (range == null) {
            return false;
        }
        int selectionStart;
        int selectionEnd;
        int segmentStart = range[0];
        int segmentEnd = range[1];
        if (extendSelection && isAccessibilitySelectionExtendable()) {
            selectionStart = getAccessibilitySelectionStart();
            if (selectionStart == -1) {
                if (forward) {
                    selectionStart = segmentStart;
                } else {
                    selectionStart = segmentEnd;
                }
            }
            if (forward) {
                selectionEnd = segmentEnd;
            } else {
                selectionEnd = segmentStart;
            }
        } else {
            selectionEnd = forward ? segmentEnd : segmentStart;
            selectionStart = selectionEnd;
        }
        setAccessibilitySelection(selectionStart, selectionEnd);
        sendViewTextTraversedAtGranularityEvent(forward ? 256 : 512, granularity, segmentStart, segmentEnd);
        return true;
    }

    public CharSequence getIterableTextForAccessibility() {
        return getContentDescription();
    }

    public boolean isAccessibilitySelectionExtendable() {
        return false;
    }

    public int getAccessibilitySelectionStart() {
        return this.mAccessibilityCursorPosition;
    }

    public int getAccessibilitySelectionEnd() {
        return getAccessibilitySelectionStart();
    }

    public void setAccessibilitySelection(int start, int end) {
        if (start != end || end != this.mAccessibilityCursorPosition) {
            if (start < 0 || start != end || end > getIterableTextForAccessibility().length()) {
                this.mAccessibilityCursorPosition = -1;
            } else {
                this.mAccessibilityCursorPosition = start;
            }
            sendAccessibilityEvent(8192);
        }
    }

    private void sendViewTextTraversedAtGranularityEvent(int action, int granularity, int fromIndex, int toIndex) {
        if (this.mParent != null) {
            AccessibilityEvent event = AccessibilityEvent.obtain(131072);
            onInitializeAccessibilityEvent(event);
            onPopulateAccessibilityEvent(event);
            event.setFromIndex(fromIndex);
            event.setToIndex(toIndex);
            event.setAction(action);
            event.setMovementGranularity(granularity);
            this.mParent.requestSendAccessibilityEvent(this, event);
        }
    }

    public TextSegmentIterator getIteratorForGranularity(int granularity) {
        CharSequence text;
        TextSegmentIterator iterator;
        switch (granularity) {
            case 1:
                text = getIterableTextForAccessibility();
                if (text != null && text.length() > 0) {
                    iterator = CharacterTextSegmentIterator.getInstance(this.mContext.getResources().getConfiguration().locale);
                    iterator.initialize(text.toString());
                    return iterator;
                }
            case 2:
                text = getIterableTextForAccessibility();
                if (text != null && text.length() > 0) {
                    iterator = WordTextSegmentIterator.getInstance(this.mContext.getResources().getConfiguration().locale);
                    iterator.initialize(text.toString());
                    return iterator;
                }
            case 8:
                text = getIterableTextForAccessibility();
                if (text != null && text.length() > 0) {
                    iterator = ParagraphTextSegmentIterator.getInstance();
                    iterator.initialize(text.toString());
                    return iterator;
                }
        }
        return null;
    }

    public void dispatchStartTemporaryDetach() {
        onStartTemporaryDetach();
    }

    public void onStartTemporaryDetach() {
        removeUnsetPressCallback();
        this.mPrivateFlags |= 67108864;
    }

    public void dispatchFinishTemporaryDetach() {
        onFinishTemporaryDetach();
    }

    public void onFinishTemporaryDetach() {
    }

    public boolean dispatchKeyEventTextMultiSelection(KeyEvent event) {
        if (sSpenUspLevel > 0) {
            return onKeyTextMultiSelection(event.getKeyCode(), event);
        }
        return false;
    }

    public DispatcherState getKeyDispatcherState() {
        return this.mAttachInfo != null ? this.mAttachInfo.mKeyDispatchState : null;
    }

    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        return onKeyPreIme(event.getKeyCode(), event);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onKeyEvent(event, 0);
        }
        ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnKeyListener != null && (this.mViewFlags & 32) == 0 && li.mOnKeyListener.onKey(this, event.getKeyCode(), event)) {
            return true;
        }
        if (event.dispatch(this, this.mAttachInfo != null ? this.mAttachInfo.mKeyDispatchState : null, this)) {
            return true;
        }
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onUnhandledEvent(event, 0);
        }
        return false;
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return onKeyShortcut(event.getKeyCode(), event);
    }

    protected boolean isTwShowingScrollbar() {
        return (this.mScrollCache == null || this.mScrollCache.state == 0) ? false : true;
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.isTargetAccessibilityFocus()) {
            if (!isAccessibilityFocusedViewOrHost()) {
                return false;
            }
            event.setTargetAccessibilityFocus(false);
        }
        boolean result = false;
        if (this.mEnablePenGesture && !this.mDisablePenGestureforfactorytest) {
            boolean isPenEvent = false;
            int action = event.getAction();
            if (event.getToolType(0) == 2 && (event.getButtonState() & 32) != 0) {
                switch (action) {
                    case 0:
                        this.isPenSideButton = true;
                        isPenEvent = true;
                        break;
                    case 1:
                    case 3:
                        if (this.isPenSideButton) {
                            this.isPenSideButton = false;
                            isPenEvent = true;
                            break;
                        }
                        break;
                    case 2:
                        if (this.isPenSideButton) {
                            isPenEvent = true;
                            break;
                        }
                        break;
                    case 211:
                    case 212:
                    case 213:
                    case 214:
                        isPenEvent = true;
                        break;
                }
            }
            switch (action) {
                case 0:
                case 1:
                case 3:
                    this.isPenSideButton = false;
                    break;
                case 2:
                    if (this.isPenSideButton) {
                        isPenEvent = true;
                        break;
                    }
                    break;
                case 212:
                case 213:
                    isPenEvent = true;
                    break;
                default:
                    break;
            }
            if (isPenEvent) {
                if (isTextSelectionEnabled() && getParent() != null) {
                    getParent().requestOnStylusButtonEvent(event);
                }
                if (!isMultiPenSelectionEnabled()) {
                    return true;
                }
            }
        }
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onTouchEvent(event, 0);
        }
        int actionMasked = event.getActionMasked();
        if (actionMasked == 0) {
            stopNestedScroll();
        }
        if (onFilterTouchEventForSecurity(event)) {
            ListenerInfo li = this.mListenerInfo;
            if (li != null && li.mOnTouchListener != null && (this.mViewFlags & 32) == 0 && li.mOnTouchListener.onTouch(this, event)) {
                result = true;
            }
            if (!result && onTouchEvent(event)) {
                result = true;
            }
        }
        if (!(result || this.mInputEventConsistencyVerifier == null)) {
            this.mInputEventConsistencyVerifier.onUnhandledEvent(event, 0);
        }
        if (actionMasked != 1 && actionMasked != 3 && (actionMasked != 0 || result)) {
            return result;
        }
        stopNestedScroll();
        return result;
    }

    boolean isAccessibilityFocusedViewOrHost() {
        return isAccessibilityFocused() || (getViewRootImpl() != null && getViewRootImpl().getAccessibilityFocusedHost() == this);
    }

    public boolean onFilterTouchEventForSecurity(MotionEvent event) {
        if ((this.mViewFlags & 1024) == 0 || (event.getFlags() & 1) == 0) {
            return true;
        }
        return false;
    }

    public boolean dispatchTrackballEvent(MotionEvent event) {
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onTrackballEvent(event, 0);
        }
        return onTrackballEvent(event);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onGenericMotionEvent(event, 0);
        }
        if ((event.getSource() & 2) != 0) {
            int action = event.getAction();
            if (action == 9 || action == 7 || action == 10) {
                if (dispatchHoverEvent(event)) {
                    return true;
                }
            } else if (dispatchGenericPointerEvent(event)) {
                return true;
            }
        } else if (dispatchGenericFocusedEvent(event)) {
            return true;
        }
        if (dispatchGenericMotionEventInternal(event)) {
            return true;
        }
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onUnhandledEvent(event, 0);
        }
        return false;
    }

    private boolean dispatchGenericMotionEventInternal(MotionEvent event) {
        ListenerInfo li = this.mListenerInfo;
        if ((li != null && li.mOnGenericMotionListener != null && (this.mViewFlags & 32) == 0 && li.mOnGenericMotionListener.onGenericMotion(this, event)) || onGenericMotionEvent(event)) {
            return true;
        }
        int actionButton = event.getActionButton();
        switch (event.getActionMasked()) {
            case 11:
                if (isContextClickable() && !this.mInContextButtonPress && !this.mHasPerformedLongPress && ((actionButton == 32 || actionButton == 2) && performContextClick())) {
                    this.mInContextButtonPress = true;
                    setPressed(true, event.getX(), event.getY());
                    removeTapCallback();
                    removeLongPressCallback();
                    return true;
                }
            case 12:
                if (this.mInContextButtonPress && (actionButton == 32 || actionButton == 2)) {
                    this.mInContextButtonPress = false;
                    this.mIgnoreNextUpEvent = true;
                    break;
                }
        }
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onUnhandledEvent(event, 0);
        }
        return false;
    }

    protected boolean dispatchHoverEvent(MotionEvent event) {
        ListenerInfo li = this.mListenerInfo;
        if (!(li == null || li.mOnAirButtonHoverListener == null || (this.mViewFlags & 32) != 0)) {
            li.mOnAirButtonHoverListener.onHover(this, event);
        }
        if (li == null || li.mOnHoverListener == null || (this.mViewFlags & 32) != 0 || !li.mOnHoverListener.onHover(this, event)) {
            return onHoverEvent(event);
        }
        return true;
    }

    protected boolean hasHoveredChild() {
        return false;
    }

    protected boolean dispatchGenericPointerEvent(MotionEvent event) {
        return false;
    }

    protected boolean dispatchGenericFocusedEvent(MotionEvent event) {
        return false;
    }

    public final boolean dispatchPointerEvent(MotionEvent event) {
        if (event.isTouchEvent()) {
            return dispatchTouchEvent(event);
        }
        return dispatchGenericMotionEvent(event);
    }

    public void dispatchWindowFocusChanged(boolean hasFocus) {
        onWindowFocusChanged(hasFocus);
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        InputMethodManager imm = InputMethodManager.peekInstance();
        if (!hasWindowFocus) {
            if (isPressed()) {
                setPressed(false);
            }
            if (!(imm == null || (this.mPrivateFlags & 2) == 0)) {
                imm.focusOut(this);
            }
            removeLongPressCallback();
            removeTapCallback();
            onFocusLost();
            if ((this.mPrivateFlags & 268435456) != 0) {
                this.mPrivateFlags &= -268435457;
            }
            if ((this.mTouchwizFlags & 1) != 0) {
                this.mTouchwizFlags &= -2;
            }
        } else if (!(imm == null || (this.mPrivateFlags & 2) == 0)) {
            imm.focusIn(this);
        }
        refreshDrawableState();
    }

    public boolean hasWindowFocus() {
        return this.mAttachInfo != null && this.mAttachInfo.mHasWindowFocus;
    }

    public void setOnAttachedDisplayChangeListener(OnAttachedDisplayChangeListener listener) {
    }

    public void dispatchAttachedDisplayChanged(int displayId) {
    }

    public void onAttachedDisplayIdChanged(int displayId) {
    }

    public void twSetEnabledThreeFingerGesture(boolean enabled) {
    }

    protected void dispatchVisibilityChanged(View changedView, int visibility) {
        onVisibilityChanged(changedView, visibility);
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        boolean visible;
        if (visibility == 0 && getVisibility() == 0) {
            visible = true;
        } else {
            visible = false;
        }
        if (visible && this.mAttachInfo != null) {
            initialAwakenScrollBars();
        }
        Drawable dr = this.mBackground;
        if (!(dr == null || visible == dr.isVisible())) {
            dr.setVisible(visible, false);
        }
        Drawable fg = this.mForegroundInfo != null ? this.mForegroundInfo.mDrawable : null;
        if (fg != null && visible != fg.isVisible()) {
            fg.setVisible(visible, false);
        }
    }

    public void dispatchDisplayHint(int hint) {
        onDisplayHint(hint);
    }

    protected void onDisplayHint(int hint) {
    }

    public void dispatchWindowVisibilityChanged(int visibility) {
        onWindowVisibilityChanged(visibility);
    }

    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility == 0) {
            initialAwakenScrollBars();
        }
    }

    public int getWindowVisibility() {
        return this.mAttachInfo != null ? this.mAttachInfo.mWindowVisibility : 8;
    }

    public void getWindowVisibleDisplayFrame(Rect outRect) {
        if (this.mAttachInfo != null) {
            try {
                this.mAttachInfo.mSession.getDisplayFrame(this.mAttachInfo.mWindow, outRect);
                Rect insets = this.mAttachInfo.mVisibleInsets;
                outRect.left += insets.left;
                outRect.top += insets.top;
                outRect.right -= insets.right;
                outRect.bottom -= insets.bottom;
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        DisplayManagerGlobal.getInstance().getRealDisplay(0).getRectSize(outRect);
    }

    public void getWindowVisibleContentFrame(Rect outRect) {
        if (this.mAttachInfo != null) {
            try {
                this.mAttachInfo.mSession.getContentFrame(this.mAttachInfo.mWindow, outRect);
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        DisplayManagerGlobal.getInstance().getRealDisplay(0).getRectSize(outRect);
    }

    public void dispatchConfigurationChanged(Configuration newConfig) {
        onConfigurationChanged(newConfig);
    }

    protected void onConfigurationChanged(Configuration newConfig) {
    }

    void dispatchCollectViewAttributes(AttachInfo attachInfo, int visibility) {
        performCollectViewAttributes(attachInfo, visibility);
    }

    void performCollectViewAttributes(AttachInfo attachInfo, int visibility) {
        if ((visibility & 12) == 0) {
            if ((this.mViewFlags & 67108864) == 67108864) {
                attachInfo.mKeepScreenOn = true;
            }
            attachInfo.mSystemUiVisibility |= this.mSystemUiVisibility;
            ListenerInfo li = this.mListenerInfo;
            if (li != null && li.mOnSystemUiVisibilityChangeListener != null) {
                attachInfo.mHasSystemUiListeners = true;
            }
        }
    }

    void needGlobalAttributesUpdate(boolean force) {
        AttachInfo ai = this.mAttachInfo;
        if (ai != null && !ai.mRecomputeGlobalAttributes) {
            if (force || ai.mKeepScreenOn || ai.mSystemUiVisibility != 0 || ai.mHasSystemUiListeners) {
                ai.mRecomputeGlobalAttributes = true;
            }
        }
    }

    @ExportedProperty
    public boolean isInTouchMode() {
        if (this.mAttachInfo != null) {
            return this.mAttachInfo.mInTouchMode;
        }
        return ViewRootImpl.isInTouchMode();
    }

    @CapturedViewProperty
    public final Context getContext() {
        return this.mContext;
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyTextMultiSelection(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!KeyEvent.isConfirmKey(keyCode)) {
            return false;
        }
        if ((this.mViewFlags & 32) == 32) {
            return true;
        }
        if (((this.mViewFlags & 16384) != 16384 && (this.mViewFlags & 2097152) != 2097152) || event.getRepeatCount() != 0) {
            return false;
        }
        setPressed(true);
        checkForLongClick(0);
        return true;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!KeyEvent.isConfirmKey(keyCode)) {
            return false;
        }
        if ((this.mViewFlags & 32) == 32) {
            return true;
        }
        if ((this.mViewFlags & 16384) != 16384 || !isPressed()) {
            return false;
        }
        setPressed(false);
        if (this.mHasPerformedLongPress) {
            return false;
        }
        removeLongPressCallback();
        return performClick();
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onCheckIsTextEditor() {
        return false;
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return null;
    }

    public boolean isWritingBuddyEnabled() {
        return this.mIsWritingBuddyEnabled;
    }

    public void setWritingBuddyEnabled(boolean enabled) {
        this.mIsWritingBuddyEnabled = enabled;
    }

    public void setWritingBuddy(WritingBuddyImpl wb) {
        boolean isCarModeOn;
        if (Settings$System.getIntForUser(this.mContext.getContentResolver(), "car_mode_on", 0, -3) == 1) {
            isCarModeOn = true;
        } else {
            isCarModeOn = false;
        }
        if (wb == null) {
            this.mIsWritingBuddyEnabled = false;
            this.mWritingBuddy = null;
        } else if (isCarModeOn) {
            this.mIsWritingBuddyEnabled = false;
            this.mWritingBuddy = null;
        } else {
            if (!(this.mWritingBuddy == null || this.mWritingBuddy.equals(wb))) {
                this.mWritingBuddy.setParentView(null);
            }
            this.mIsWritingBuddyEnabled = true;
            this.mWritingBuddy = wb;
        }
    }

    public WritingBuddyImpl getWritingBuddy(boolean createIfNull) {
        if (this.mWritingBuddy != null) {
            return this.mWritingBuddy;
        }
        if (!createIfNull) {
            return null;
        }
        this.mWritingBuddy = new WritingBuddyImpl(this);
        return this.mWritingBuddy;
    }

    protected boolean onWritingBuddyMotionEvent(MotionEvent event) {
        return getWritingBuddy(true).handleMotionEvent(this, event);
    }

    public void reportCurrentWritingBuddyView(View view) {
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl != null) {
            viewRootImpl.setCurrentWritingBuddyView(view);
        }
    }

    public View getCurrentWritingBuddyView() {
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl != null) {
            return viewRootImpl.getCurrentWritingBuddyView();
        }
        return null;
    }

    public void setAirButton(AirButtonImpl airbutton) {
        this.mAirButtonImpl = airbutton;
    }

    public AirButtonImpl getAirButton() {
        return this.mAirButtonImpl;
    }

    public boolean checkInputConnectionProxy(View view) {
        return false;
    }

    public void createContextMenu(ContextMenu menu) {
        ContextMenuInfo menuInfo = getContextMenuInfo();
        ((MenuBuilder) menu).setCurrentMenuInfo(menuInfo);
        onCreateContextMenu(menu);
        ListenerInfo li = this.mListenerInfo;
        if (!(li == null || li.mOnCreateContextMenuListener == null)) {
            li.mOnCreateContextMenuListener.onCreateContextMenu(menu, this, menuInfo);
        }
        ((MenuBuilder) menu).setCurrentMenuInfo(null);
        if (this.mParent != null) {
            this.mParent.createContextMenu(menu);
        }
    }

    protected ContextMenuInfo getContextMenuInfo() {
        return null;
    }

    protected void onCreateContextMenu(ContextMenu menu) {
    }

    public boolean onTrackballEvent(MotionEvent event) {
        return false;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        return false;
    }

    public boolean onHoverEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (this.mSendingHoverAccessibilityEvents) {
            if (action == 10 || (action == 2 && !pointInView(event.getX(), event.getY()))) {
                this.mSendingHoverAccessibilityEvents = false;
                sendAccessibilityHoverEvent(256);
            }
        } else if ((action == 9 || action == 7) && !hasHoveredChild() && pointInView(event.getX(), event.getY())) {
            sendAccessibilityHoverEvent(128);
            this.mSendingHoverAccessibilityEvents = true;
        }
        int tooltype = event.getToolType(0);
        boolean compareTooltype;
        if (tooltype == 2 || tooltype == 1) {
            compareTooltype = true;
        } else {
            compareTooltype = false;
        }
        boolean compareTooltypeByApp = true;
        if (!(this.mHoverPopupToolTypeByApp == 0 || this.mHoverPopupToolTypeByApp == tooltype)) {
            compareTooltypeByApp = false;
        }
        if (!(!compareTooltypeByApp || !compareTooltype || this.mHoverPopupType == 0 || this.mIsDetachedFromWindow || getHoverPopupWindow(tooltype, false) == null || getHoverPopupWindow(tooltype, false).onHoverEvent(event) || !isFingerHoveredInAppWidget())) {
            if (action == 9) {
                boolean isBtnPressed;
                if ((event.getButtonState() & 32) != 0) {
                    isBtnPressed = true;
                } else {
                    isBtnPressed = false;
                }
                if (!((this.mParent instanceof Spinner) || isBtnPressed)) {
                    getHoverPopupWindow(tooltype, false).setHoveringPoint((int) event.getRawXForScaledWindow(), (int) event.getRawYForScaledWindow());
                    getHoverPopupWindow(tooltype, false).show(this.mHoverPopupType);
                }
            } else if (!(action == 7 || action != 10 || (this.mParent instanceof Spinner))) {
                getHoverPopupWindow(tooltype, false).dismiss();
            }
        }
        if (!isHoverable()) {
            return false;
        }
        switch (action) {
            case 9:
                if (event.getToolType(0) == 1) {
                    setFingerHovered(true);
                }
                setHovered(true);
                break;
            case 10:
                if (event.getToolType(0) == 1) {
                    setFingerHovered(false);
                }
                setHovered(false);
                break;
        }
        dispatchGenericMotionEventInternal(event);
        return true;
    }

    private boolean isHoverable() {
        int viewFlags = this.mViewFlags;
        if ((viewFlags & 32) == 32) {
            return false;
        }
        if ((viewFlags & 16384) == 16384 || (viewFlags & 2097152) == 2097152 || (viewFlags & 8388608) == 8388608) {
            return true;
        }
        return false;
    }

    @ExportedProperty
    public boolean isHovered() {
        return (this.mPrivateFlags & 268435456) != 0;
    }

    public void setHovered(boolean hovered) {
        if (hovered) {
            if ((this.mPrivateFlags & 268435456) == 0) {
                this.mPrivateFlags |= 268435456;
                refreshDrawableState();
                onHoverChanged(true);
            }
        } else if ((this.mPrivateFlags & 268435456) != 0) {
            this.mPrivateFlags &= -268435457;
            refreshDrawableState();
            onHoverChanged(false);
        }
    }

    public void onHoverChanged(boolean hovered) {
    }

    public boolean isInDialog() {
        if (this.mRootViewCheckForDialog == null) {
            this.mRootViewCheckForDialog = getRootView();
            if (this.mRootViewCheckForDialog == null) {
                return false;
            }
            Context context = this.mRootViewCheckForDialog.getContext();
            if ((context instanceof Activity) && ((Activity) context).getWindow().getAttributes().type == 1) {
                this.mIsInDialog = false;
            } else {
                this.mIsInDialog = true;
            }
        }
        return this.mIsInDialog;
    }

    protected boolean findEllipsizedTextView(View view) {
        if (view instanceof ViewGroup) {
            if (view instanceof GridView) {
                return false;
            }
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (findEllipsizedTextView(viewGroup.getChildAt(i))) {
                    return true;
                }
            }
            return false;
        } else if (!(view instanceof TextView)) {
            return false;
        } else {
            TextView textView = (TextView) view;
            if (textView != null && textView.getVisibility() == 0 && textView.isEllipsis()) {
                return true;
            }
            return false;
        }
    }

    public boolean hideCursorControllers(View view) {
        if (view instanceof ViewGroup) {
            if (view instanceof GridView) {
                return false;
            }
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (hideCursorControllers(viewGroup.getChildAt(i))) {
                    return true;
                }
            }
            return false;
        } else if (!(view instanceof EditText)) {
            return false;
        } else {
            EditText editText = (EditText) view;
            if (editText == null || !editText.isCursorControllersShowing()) {
                return false;
            }
            editText.hideCursorControllers();
            return true;
        }
    }

    protected boolean findSetFingerHovedInAppWidget(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (!findSetFingerHovedInAppWidget(viewGroup.getChildAt(i))) {
                    return false;
                }
            }
        } else if ((view instanceof TextView) && !((TextView) view).isFingerHoveredInAppWidget()) {
            return false;
        }
        return true;
    }

    public boolean isFingerHoveredInAppWidget() {
        return this.mIsSetFingerHovedInAppWidget;
    }

    @RemotableViewMethod
    public void setFingerHoveredInAppWidget(boolean fingerHovered) {
        this.mIsSetFingerHovedInAppWidget = fingerHovered;
    }

    @ExportedProperty
    public boolean isFingerHovered() {
        return (this.mTouchwizFlags & 1) != 0;
    }

    public void setFingerHovered(boolean fingerHovered) {
        boolean isFingerAirViewPreview = true;
        if (Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.FINGER_AIR_VIEW_INFORMATION_PREVIEW, 0, -3) != 1) {
            isFingerAirViewPreview = false;
        }
        if (isFingerAirViewPreview && fingerHovered) {
            if ((this.mTouchwizFlags & 1) == 0) {
                this.mTouchwizFlags |= 1;
            }
        } else if ((this.mTouchwizFlags & 1) != 0) {
            this.mTouchwizFlags &= -2;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int viewFlags = this.mViewFlags;
        int action = event.getAction();
        if ((viewFlags & 32) == 32) {
            if (action == 1 && (this.mPrivateFlags & 16384) != 0) {
                setPressed(false);
            }
            if ((viewFlags & 16384) == 16384 || (viewFlags & 2097152) == 2097152 || (viewFlags & 8388608) == 8388608) {
                return true;
            }
            return false;
        } else if (this.mTouchDelegate != null && this.mTouchDelegate.onTouchEvent(event)) {
            return true;
        } else {
            if ((viewFlags & 16384) != 16384 && (viewFlags & 2097152) != 2097152 && (viewFlags & 8388608) != 8388608) {
                return false;
            }
            switch (action) {
                case 0:
                    this.mHasPerformedLongPress = false;
                    if (!performButtonActionOnTouchDown(event)) {
                        if (!isInScrollingContainer()) {
                            setPressed(true, x, y);
                            checkForLongClick(0);
                            break;
                        }
                        this.mPrivateFlags |= 33554432;
                        if (this.mPendingCheckForTap == null) {
                            this.mPendingCheckForTap = new CheckForTap();
                        }
                        this.mPendingCheckForTap.x = event.getX();
                        this.mPendingCheckForTap.y = event.getY();
                        postDelayed(this.mPendingCheckForTap, (long) ViewConfiguration.getTapTimeout());
                        break;
                    }
                    break;
                case 1:
                    boolean prepressed;
                    if ((this.mPrivateFlags & 33554432) != 0) {
                        prepressed = true;
                    } else {
                        prepressed = false;
                    }
                    if ((this.mPrivateFlags & 16384) != 0 || prepressed) {
                        boolean focusTaken = false;
                        if (isFocusable() && isFocusableInTouchMode() && !isFocused()) {
                            focusTaken = requestFocus();
                        }
                        if (prepressed) {
                            setPressed(true, x, y);
                        }
                        if (!(this.mHasPerformedLongPress || this.mIgnoreNextUpEvent)) {
                            removeLongPressCallback();
                            if (!focusTaken) {
                                if (this.mPerformClick == null) {
                                    this.mPerformClick = new PerformClick();
                                }
                                if (!post(this.mPerformClick)) {
                                    performClick();
                                }
                            }
                        }
                        if (this.mUnsetPressedState == null) {
                            this.mUnsetPressedState = new UnsetPressedState();
                        }
                        if (prepressed) {
                            postDelayed(this.mUnsetPressedState, (long) ViewConfiguration.getPressedStateDuration());
                        } else if (!post(this.mUnsetPressedState)) {
                            this.mUnsetPressedState.run();
                        }
                        removeTapCallback();
                    }
                    this.mIgnoreNextUpEvent = false;
                    break;
                case 2:
                    drawableHotspotChanged(x, y);
                    if (!pointInView(x, y, (float) this.mTouchSlop)) {
                        removeTapCallback();
                        if ((this.mPrivateFlags & 16384) != 0) {
                            removeLongPressCallback();
                            setPressed(false);
                            break;
                        }
                    }
                    break;
                case 3:
                    setPressed(false);
                    removeTapCallback();
                    removeLongPressCallback();
                    this.mInContextButtonPress = false;
                    this.mHasPerformedLongPress = false;
                    this.mIgnoreNextUpEvent = false;
                    break;
            }
            return true;
        }
    }

    public boolean isInScrollingContainer() {
        ViewParent p = getParent();
        while (p != null && (p instanceof ViewGroup)) {
            if (((ViewGroup) p).shouldDelayChildPressedState()) {
                return true;
            }
            p = p.getParent();
        }
        return false;
    }

    private void removeLongPressCallback() {
        if (this.mPendingCheckForLongPress != null) {
            removeCallbacks(this.mPendingCheckForLongPress);
        }
    }

    private void removePerformClickCallback() {
        if (this.mPerformClick != null) {
            removeCallbacks(this.mPerformClick);
        }
    }

    private void removeUnsetPressCallback() {
        if ((this.mPrivateFlags & 16384) != 0 && this.mUnsetPressedState != null) {
            setPressed(false);
            removeCallbacks(this.mUnsetPressedState);
        }
    }

    private void removeTapCallback() {
        if (this.mPendingCheckForTap != null) {
            this.mPrivateFlags &= -33554433;
            removeCallbacks(this.mPendingCheckForTap);
        }
    }

    public void cancelLongPress() {
        removeLongPressCallback();
        removeTapCallback();
    }

    private void removeSendViewScrolledAccessibilityEventCallback() {
        if (this.mSendViewScrolledAccessibilityEvent != null) {
            removeCallbacks(this.mSendViewScrolledAccessibilityEvent);
            this.mSendViewScrolledAccessibilityEvent.mIsPending = false;
        }
    }

    public void setTouchDelegate(TouchDelegate delegate) {
        this.mTouchDelegate = delegate;
    }

    public TouchDelegate getTouchDelegate() {
        return this.mTouchDelegate;
    }

    public final void requestUnbufferedDispatch(MotionEvent event) {
        int action = event.getAction();
        if (this.mAttachInfo == null) {
            return;
        }
        if ((action == 0 || action == 2) && event.isTouchEvent()) {
            this.mAttachInfo.mUnbufferedDispatchRequested = true;
        }
    }

    void setFlags(int flags, int mask) {
        boolean oldIncludeForAccessibility;
        boolean accessibilityEnabled = AccessibilityManager.getInstance(this.mContext).isEnabled();
        if (accessibilityEnabled && includeForAccessibility()) {
            oldIncludeForAccessibility = true;
        } else {
            oldIncludeForAccessibility = false;
        }
        int old = this.mViewFlags;
        this.mViewFlags = (this.mViewFlags & (mask ^ -1)) | (flags & mask);
        int changed = this.mViewFlags ^ old;
        if (changed != 0) {
            int privateFlags = this.mPrivateFlags;
            if (!((changed & 1) == 0 || (privateFlags & 16) == 0)) {
                if ((old & 1) == 1 && (privateFlags & 2) != 0) {
                    clearFocus();
                } else if ((old & 1) == 0 && (privateFlags & 2) == 0 && this.mParent != null) {
                    this.mParent.focusableViewAvailable(this);
                }
            }
            int newVisibility = flags & 12;
            if (newVisibility == 0 && (changed & 12) != 0) {
                this.mPrivateFlags |= 32;
                invalidate(true);
                needGlobalAttributesUpdate(true);
                if (this.mParent != null && this.mBottom > this.mTop && this.mRight > this.mLeft) {
                    this.mParent.focusableViewAvailable(this);
                }
            }
            if ((changed & 8) != 0) {
                needGlobalAttributesUpdate(false);
                requestLayout();
                if ((this.mViewFlags & 12) == 8) {
                    if (hasFocus()) {
                        clearFocus();
                    }
                    clearAccessibilityFocus();
                    destroyDrawingCache();
                    if (this.mParent instanceof View) {
                        ((View) this.mParent).invalidate(true);
                    }
                    this.mPrivateFlags |= 32;
                }
                if (this.mAttachInfo != null) {
                    this.mAttachInfo.mViewVisibilityChanged = true;
                }
            }
            if ((changed & 4) != 0) {
                needGlobalAttributesUpdate(false);
                this.mPrivateFlags |= 32;
                if ((this.mViewFlags & 12) == 4 && getRootView() != this) {
                    if (hasFocus()) {
                        clearFocus();
                    }
                    clearAccessibilityFocus();
                }
                if (this.mAttachInfo != null) {
                    this.mAttachInfo.mViewVisibilityChanged = true;
                }
            }
            if ((changed & 12) != 0) {
                if (!(newVisibility == 0 || this.mAttachInfo == null)) {
                    cleanupDraw();
                }
                if (this.mParent instanceof ViewGroup) {
                    ((ViewGroup) this.mParent).onChildVisibilityChanged(this, changed & 12, newVisibility);
                    ((View) this.mParent).invalidate(true);
                } else if (this.mParent != null) {
                    this.mParent.invalidateChild(this, null);
                }
                if (this.mAttachInfo != null) {
                    dispatchVisibilityChanged(this, newVisibility);
                    notifySubtreeAccessibilityStateChangedIfNeeded();
                }
            }
            if ((131072 & changed) != 0) {
                destroyDrawingCache();
            }
            if ((32768 & changed) != 0) {
                destroyDrawingCache();
                this.mPrivateFlags &= -32769;
                invalidateParentCaches();
            }
            if ((DRAWING_CACHE_QUALITY_MASK & changed) != 0) {
                destroyDrawingCache();
                this.mPrivateFlags &= -32769;
            }
            if ((changed & 128) != 0) {
                if ((this.mViewFlags & 128) == 0) {
                    this.mPrivateFlags &= -129;
                } else if (this.mBackground == null && (this.mForegroundInfo == null || this.mForegroundInfo.mDrawable == null)) {
                    this.mPrivateFlags |= 128;
                } else {
                    this.mPrivateFlags &= -129;
                }
                requestLayout();
                invalidate(true);
            }
            if (!((67108864 & changed) == 0 || this.mParent == null || this.mAttachInfo == null || this.mAttachInfo.mRecomputeGlobalAttributes)) {
                this.mParent.recomputeViewAttributes(this);
            }
            if (!accessibilityEnabled) {
                return;
            }
            if ((changed & 1) == 0 && (changed & 12) == 0 && (changed & 16384) == 0 && (2097152 & changed) == 0 && (8388608 & changed) == 0) {
                if ((changed & 32) != 0) {
                    notifyViewAccessibilityStateChangedIfNeeded(0);
                }
            } else if (oldIncludeForAccessibility != includeForAccessibility()) {
                notifySubtreeAccessibilityStateChangedIfNeeded();
            } else {
                notifyViewAccessibilityStateChangedIfNeeded(0);
            }
        }
    }

    public void bringToFront() {
        if (this.mParent != null) {
            this.mParent.bringChildToFront(this);
        }
    }

    public void demoteRenderer() {
        Log.d("HWUI", "View:demoteRenderer");
        ViewRootImpl.sRendererDemoted = true;
    }

    public void setBufferCount(int count) {
        Log.d("SRIB_DBQ", "View:setBufferCount  count=" + count);
        ViewRootImpl.sBufferCount = count;
    }

    public int getBufferCount() {
        return ViewRootImpl.sBufferCount;
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        notifySubtreeAccessibilityStateChangedIfNeeded();
        if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            postSendViewScrolledAccessibilityEventCallback();
        }
        this.mBackgroundSizeChanged = true;
        if (this.mForegroundInfo != null) {
            this.mForegroundInfo.mBoundsChanged = true;
        }
        AttachInfo ai = this.mAttachInfo;
        if (ai != null) {
            ai.mViewScrollChanged = true;
        }
        if (this.mListenerInfo != null && this.mListenerInfo.mOnScrollChangeListener != null) {
            this.mListenerInfo.mOnScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    }

    protected void dispatchDraw(Canvas canvas) {
    }

    public final ViewParent getParent() {
        return this.mParent;
    }

    public void setScrollX(int value) {
        scrollTo(value, this.mScrollY);
    }

    public void setScrollY(int value) {
        scrollTo(this.mScrollX, value);
    }

    public final int getScrollX() {
        return this.mScrollX;
    }

    public final int getScrollY() {
        return this.mScrollY;
    }

    @ExportedProperty(category = "layout")
    public final int getWidth() {
        return this.mRight - this.mLeft;
    }

    @ExportedProperty(category = "layout")
    public final int getHeight() {
        return this.mBottom - this.mTop;
    }

    public void getDrawingRect(Rect outRect) {
        outRect.left = this.mScrollX;
        outRect.top = this.mScrollY;
        outRect.right = this.mScrollX + (this.mRight - this.mLeft);
        outRect.bottom = this.mScrollY + (this.mBottom - this.mTop);
    }

    public final int getMeasuredWidth() {
        return this.mMeasuredWidth & 16777215;
    }

    @ExportedProperty(category = "measurement", flagMapping = {@FlagToString(equals = 16777216, mask = -16777216, name = "MEASURED_STATE_TOO_SMALL")})
    public final int getMeasuredWidthAndState() {
        return this.mMeasuredWidth;
    }

    public final int getMeasuredHeight() {
        return this.mMeasuredHeight & 16777215;
    }

    @ExportedProperty(category = "measurement", flagMapping = {@FlagToString(equals = 16777216, mask = -16777216, name = "MEASURED_STATE_TOO_SMALL")})
    public final int getMeasuredHeightAndState() {
        return this.mMeasuredHeight;
    }

    public final int getMeasuredState() {
        return (this.mMeasuredWidth & -16777216) | ((this.mMeasuredHeight >> 16) & InputDevice.SOURCE_ANY);
    }

    public Matrix getMatrix() {
        ensureTransformationInfo();
        Matrix matrix = this.mTransformationInfo.mMatrix;
        this.mRenderNode.getMatrix(matrix);
        return matrix;
    }

    final boolean hasIdentityMatrix() {
        return this.mRenderNode.hasIdentityMatrix();
    }

    void ensureTransformationInfo() {
        if (this.mTransformationInfo == null) {
            this.mTransformationInfo = new TransformationInfo();
        }
    }

    public final Matrix getInverseMatrix() {
        ensureTransformationInfo();
        if (this.mTransformationInfo.mInverseMatrix == null) {
            this.mTransformationInfo.mInverseMatrix = new Matrix();
        }
        Matrix matrix = this.mTransformationInfo.mInverseMatrix;
        this.mRenderNode.getInverseMatrix(matrix);
        return matrix;
    }

    public float getCameraDistance() {
        return -(this.mRenderNode.getCameraDistance() * ((float) this.mResources.getDisplayMetrics().densityDpi));
    }

    public void setCameraDistance(float distance) {
        float dpi = (float) this.mResources.getDisplayMetrics().densityDpi;
        invalidateViewProperty(true, false);
        this.mRenderNode.setCameraDistance((-Math.abs(distance)) / dpi);
        invalidateViewProperty(false, false);
        invalidateParentIfNeededAndWasQuickRejected();
    }

    @ExportedProperty(category = "drawing")
    public float getRotation() {
        return this.mRenderNode.getRotation();
    }

    public void setRotation(float rotation) {
        if (rotation != getRotation()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setRotation(rotation);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getRotationY() {
        return this.mRenderNode.getRotationY();
    }

    public void setRotationY(float rotationY) {
        if (rotationY != getRotationY()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setRotationY(rotationY);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getRotationX() {
        return this.mRenderNode.getRotationX();
    }

    public void setRotationX(float rotationX) {
        if (rotationX != getRotationX()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setRotationX(rotationX);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getScaleX() {
        return this.mRenderNode.getScaleX();
    }

    public void setScaleX(float scaleX) {
        if (scaleX != getScaleX()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setScaleX(scaleX);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getScaleY() {
        return this.mRenderNode.getScaleY();
    }

    public void setScaleY(float scaleY) {
        if (scaleY != getScaleY()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setScaleY(scaleY);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getPivotX() {
        return this.mRenderNode.getPivotX();
    }

    public void setPivotX(float pivotX) {
        if (!this.mRenderNode.isPivotExplicitlySet() || pivotX != getPivotX()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setPivotX(pivotX);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getPivotY() {
        return this.mRenderNode.getPivotY();
    }

    public void setPivotY(float pivotY) {
        if (!this.mRenderNode.isPivotExplicitlySet() || pivotY != getPivotY()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setPivotY(pivotY);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getAlpha() {
        return this.mTransformationInfo != null ? this.mTransformationInfo.mAlpha : 1.0f;
    }

    @ExportedProperty(category = "drawing")
    public boolean hasOverlappingRendering() {
        return true;
    }

    public void setAlpha(float alpha) {
        ensureTransformationInfo();
        if (this.mTransformationInfo.mAlpha != alpha) {
            this.mTransformationInfo.mAlpha = alpha;
            if (onSetAlpha((int) (255.0f * alpha))) {
                this.mPrivateFlags |= 262144;
                invalidateParentCaches();
                invalidate(true);
                return;
            }
            this.mPrivateFlags &= -262145;
            invalidateViewProperty(true, false);
            this.mRenderNode.setAlpha(getFinalAlpha());
            notifyViewAccessibilityStateChangedIfNeeded(0);
        }
    }

    boolean setAlphaNoInvalidation(float alpha) {
        ensureTransformationInfo();
        if (this.mTransformationInfo.mAlpha != alpha) {
            this.mTransformationInfo.mAlpha = alpha;
            if (onSetAlpha((int) (255.0f * alpha))) {
                this.mPrivateFlags |= 262144;
                return true;
            }
            this.mPrivateFlags &= -262145;
            this.mRenderNode.setAlpha(getFinalAlpha());
        }
        return false;
    }

    public void setTransitionAlpha(float alpha) {
        ensureTransformationInfo();
        if (this.mTransformationInfo.mTransitionAlpha != alpha) {
            this.mTransformationInfo.mTransitionAlpha = alpha;
            this.mPrivateFlags &= -262145;
            invalidateViewProperty(true, false);
            this.mRenderNode.setAlpha(getFinalAlpha());
        }
    }

    private float getFinalAlpha() {
        if (this.mTransformationInfo != null) {
            return this.mTransformationInfo.mAlpha * this.mTransformationInfo.mTransitionAlpha;
        }
        return 1.0f;
    }

    @ExportedProperty(category = "drawing")
    public float getTransitionAlpha() {
        return this.mTransformationInfo != null ? this.mTransformationInfo.mTransitionAlpha : 1.0f;
    }

    @CapturedViewProperty
    public final int getTop() {
        return this.mTop;
    }

    public final void setTop(int top) {
        if (top != this.mTop) {
            boolean matrixIsIdentity = hasIdentityMatrix();
            if (!matrixIsIdentity) {
                invalidate(true);
            } else if (this.mAttachInfo != null) {
                int minTop;
                int yLoc;
                if (top < this.mTop) {
                    minTop = top;
                    yLoc = top - this.mTop;
                } else {
                    minTop = this.mTop;
                    yLoc = 0;
                }
                invalidate(0, yLoc, this.mRight - this.mLeft, this.mBottom - minTop);
            }
            int width = this.mRight - this.mLeft;
            int oldHeight = this.mBottom - this.mTop;
            this.mTop = top;
            this.mRenderNode.setTop(this.mTop);
            sizeChange(width, this.mBottom - this.mTop, width, oldHeight);
            if (!matrixIsIdentity) {
                this.mPrivateFlags |= 32;
                invalidate(true);
            }
            this.mBackgroundSizeChanged = true;
            if (this.mForegroundInfo != null) {
                this.mForegroundInfo.mBoundsChanged = true;
            }
            invalidateParentIfNeeded();
            if ((this.mPrivateFlags2 & 268435456) == 268435456) {
                invalidateParentIfNeeded();
            }
        }
    }

    @CapturedViewProperty
    public final int getBottom() {
        return this.mBottom;
    }

    public boolean isDirty() {
        return (this.mPrivateFlags & PFLAG_DIRTY_MASK) != 0;
    }

    public final void setBottom(int bottom) {
        if (bottom != this.mBottom) {
            boolean matrixIsIdentity = hasIdentityMatrix();
            if (!matrixIsIdentity) {
                invalidate(true);
            } else if (this.mAttachInfo != null) {
                int maxBottom;
                if (bottom < this.mBottom) {
                    maxBottom = this.mBottom;
                } else {
                    maxBottom = bottom;
                }
                invalidate(0, 0, this.mRight - this.mLeft, maxBottom - this.mTop);
            }
            int width = this.mRight - this.mLeft;
            int oldHeight = this.mBottom - this.mTop;
            this.mBottom = bottom;
            this.mRenderNode.setBottom(this.mBottom);
            sizeChange(width, this.mBottom - this.mTop, width, oldHeight);
            if (!matrixIsIdentity) {
                this.mPrivateFlags |= 32;
                invalidate(true);
            }
            this.mBackgroundSizeChanged = true;
            if (this.mForegroundInfo != null) {
                this.mForegroundInfo.mBoundsChanged = true;
            }
            invalidateParentIfNeeded();
            if ((this.mPrivateFlags2 & 268435456) == 268435456) {
                invalidateParentIfNeeded();
            }
        }
    }

    @CapturedViewProperty
    public final int getLeft() {
        return this.mLeft;
    }

    public final void setLeft(int left) {
        if (left != this.mLeft) {
            boolean matrixIsIdentity = hasIdentityMatrix();
            if (!matrixIsIdentity) {
                invalidate(true);
            } else if (this.mAttachInfo != null) {
                int minLeft;
                int xLoc;
                if (left < this.mLeft) {
                    minLeft = left;
                    xLoc = left - this.mLeft;
                } else {
                    minLeft = this.mLeft;
                    xLoc = 0;
                }
                invalidate(xLoc, 0, this.mRight - minLeft, this.mBottom - this.mTop);
            }
            int oldWidth = this.mRight - this.mLeft;
            int height = this.mBottom - this.mTop;
            this.mLeft = left;
            this.mRenderNode.setLeft(left);
            sizeChange(this.mRight - this.mLeft, height, oldWidth, height);
            if (!matrixIsIdentity) {
                this.mPrivateFlags |= 32;
                invalidate(true);
            }
            this.mBackgroundSizeChanged = true;
            if (this.mForegroundInfo != null) {
                this.mForegroundInfo.mBoundsChanged = true;
            }
            invalidateParentIfNeeded();
            if ((this.mPrivateFlags2 & 268435456) == 268435456) {
                invalidateParentIfNeeded();
            }
        }
    }

    @CapturedViewProperty
    public final int getRight() {
        return this.mRight;
    }

    public final void setRight(int right) {
        if (right != this.mRight) {
            boolean matrixIsIdentity = hasIdentityMatrix();
            if (!matrixIsIdentity) {
                invalidate(true);
            } else if (this.mAttachInfo != null) {
                int maxRight;
                if (right < this.mRight) {
                    maxRight = this.mRight;
                } else {
                    maxRight = right;
                }
                invalidate(0, 0, maxRight - this.mLeft, this.mBottom - this.mTop);
            }
            int oldWidth = this.mRight - this.mLeft;
            int height = this.mBottom - this.mTop;
            this.mRight = right;
            this.mRenderNode.setRight(this.mRight);
            sizeChange(this.mRight - this.mLeft, height, oldWidth, height);
            if (!matrixIsIdentity) {
                this.mPrivateFlags |= 32;
                invalidate(true);
            }
            this.mBackgroundSizeChanged = true;
            if (this.mForegroundInfo != null) {
                this.mForegroundInfo.mBoundsChanged = true;
            }
            invalidateParentIfNeeded();
            if ((this.mPrivateFlags2 & 268435456) == 268435456) {
                invalidateParentIfNeeded();
            }
        }
    }

    @ExportedProperty(category = "drawing")
    public float getX() {
        return ((float) this.mLeft) + getTranslationX();
    }

    public void setX(float x) {
        setTranslationX(x - ((float) this.mLeft));
    }

    @ExportedProperty(category = "drawing")
    public float getY() {
        return ((float) this.mTop) + getTranslationY();
    }

    public void setY(float y) {
        setTranslationY(y - ((float) this.mTop));
    }

    @ExportedProperty(category = "drawing")
    public float getZ() {
        return getElevation() + getTranslationZ();
    }

    public void setZ(float z) {
        setTranslationZ(z - getElevation());
    }

    @ExportedProperty(category = "drawing")
    public float getElevation() {
        return this.mRenderNode.getElevation();
    }

    public void setElevation(float elevation) {
        if (elevation != getElevation()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setElevation(elevation);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getTranslationX() {
        return this.mRenderNode.getTranslationX();
    }

    public void setTranslationX(float translationX) {
        if (translationX != getTranslationX()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setTranslationX(translationX);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getTranslationY() {
        return this.mRenderNode.getTranslationY();
    }

    public void setTranslationY(float translationY) {
        if (translationY != getTranslationY()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setTranslationY(translationY);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public float getTranslationZ() {
        return this.mRenderNode.getTranslationZ();
    }

    public void setTranslationZ(float translationZ) {
        if (translationZ != getTranslationZ()) {
            invalidateViewProperty(true, false);
            this.mRenderNode.setTranslationZ(translationZ);
            invalidateViewProperty(false, true);
            invalidateParentIfNeededAndWasQuickRejected();
        }
    }

    public void setAnimationMatrix(Matrix matrix) {
        invalidateViewProperty(true, false);
        this.mRenderNode.setAnimationMatrix(matrix);
        invalidateViewProperty(false, true);
        invalidateParentIfNeededAndWasQuickRejected();
    }

    public StateListAnimator getStateListAnimator() {
        return this.mStateListAnimator;
    }

    public void setStateListAnimator(StateListAnimator stateListAnimator) {
        if (this.mStateListAnimator != stateListAnimator) {
            if (this.mStateListAnimator != null) {
                this.mStateListAnimator.setTarget(null);
            }
            this.mStateListAnimator = stateListAnimator;
            if (stateListAnimator != null) {
                stateListAnimator.setTarget(this);
                if (isAttachedToWindow()) {
                    stateListAnimator.setState(getDrawableState());
                }
            }
        }
    }

    public final boolean getClipToOutline() {
        return this.mRenderNode.getClipToOutline();
    }

    public void setClipToOutline(boolean clipToOutline) {
        damageInParent();
        if (getClipToOutline() != clipToOutline) {
            this.mRenderNode.setClipToOutline(clipToOutline);
        }
    }

    private void setOutlineProviderFromAttribute(int providerInt) {
        switch (providerInt) {
            case 0:
                setOutlineProvider(ViewOutlineProvider.BACKGROUND);
                return;
            case 1:
                setOutlineProvider(null);
                return;
            case 2:
                setOutlineProvider(ViewOutlineProvider.BOUNDS);
                return;
            case 3:
                setOutlineProvider(ViewOutlineProvider.PADDED_BOUNDS);
                return;
            default:
                return;
        }
    }

    public void setOutlineProvider(ViewOutlineProvider provider) {
        this.mOutlineProvider = provider;
        invalidateOutline();
    }

    public ViewOutlineProvider getOutlineProvider() {
        return this.mOutlineProvider;
    }

    public void invalidateOutline() {
        rebuildOutline();
        notifySubtreeAccessibilityStateChangedIfNeeded();
        invalidateViewProperty(false, false);
    }

    private void rebuildOutline() {
        if (this.mAttachInfo != null) {
            if (this.mOutlineProvider == null) {
                this.mRenderNode.setOutline(null);
                return;
            }
            Outline outline = this.mAttachInfo.mTmpOutline;
            outline.setEmpty();
            outline.setAlpha(1.0f);
            this.mOutlineProvider.getOutline(this, outline);
            this.mRenderNode.setOutline(outline);
        }
    }

    @ExportedProperty(category = "drawing")
    public boolean hasShadow() {
        return this.mRenderNode.hasShadow();
    }

    public void setRevealClip(boolean shouldClip, float x, float y, float radius) {
        this.mRenderNode.setRevealClip(shouldClip, x, y, radius);
        invalidateViewProperty(false, false);
    }

    public void getHitRect(Rect outRect) {
        if (hasIdentityMatrix() || this.mAttachInfo == null) {
            outRect.set(this.mLeft, this.mTop, this.mRight, this.mBottom);
            return;
        }
        RectF tmpRect = this.mAttachInfo.mTmpTransformRect;
        tmpRect.set(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
        getMatrix().mapRect(tmpRect);
        outRect.set(((int) tmpRect.left) + this.mLeft, ((int) tmpRect.top) + this.mTop, ((int) tmpRect.right) + this.mLeft, ((int) tmpRect.bottom) + this.mTop);
    }

    final boolean pointInView(float localX, float localY) {
        return localX >= 0.0f && localX < ((float) (this.mRight - this.mLeft)) && localY >= 0.0f && localY < ((float) (this.mBottom - this.mTop));
    }

    public boolean pointInView(float localX, float localY, float slop) {
        return localX >= (-slop) && localY >= (-slop) && localX < ((float) (this.mRight - this.mLeft)) + slop && localY < ((float) (this.mBottom - this.mTop)) + slop;
    }

    public void getFocusedRect(Rect r) {
        getDrawingRect(r);
    }

    public boolean getGlobalVisibleRect(Rect r, Point globalOffset) {
        int width = this.mRight - this.mLeft;
        int height = this.mBottom - this.mTop;
        if (width <= 0 || height <= 0) {
            return false;
        }
        r.set(0, 0, width, height);
        if (globalOffset != null) {
            globalOffset.set(-this.mScrollX, -this.mScrollY);
        }
        if (this.mParent == null || this.mParent.getChildVisibleRect(this, r, globalOffset)) {
            return true;
        }
        return false;
    }

    public final boolean getGlobalVisibleRect(Rect r) {
        return getGlobalVisibleRect(r, null);
    }

    public final boolean getLocalVisibleRect(Rect r) {
        Point offset = this.mAttachInfo != null ? this.mAttachInfo.mPoint : new Point();
        if (!getGlobalVisibleRect(r, offset)) {
            return false;
        }
        r.offset(-offset.x, -offset.y);
        return true;
    }

    public void offsetTopAndBottom(int offset) {
        if (offset != 0) {
            boolean matrixIsIdentity = hasIdentityMatrix();
            if (!matrixIsIdentity) {
                invalidateViewProperty(false, false);
            } else if (isHardwareAccelerated()) {
                invalidateViewProperty(false, false);
            } else {
                ViewParent p = this.mParent;
                if (!(p == null || this.mAttachInfo == null)) {
                    int minTop;
                    int maxBottom;
                    int yLoc;
                    Rect r = this.mAttachInfo.mTmpInvalRect;
                    if (offset < 0) {
                        minTop = this.mTop + offset;
                        maxBottom = this.mBottom;
                        yLoc = offset;
                    } else {
                        minTop = this.mTop;
                        maxBottom = this.mBottom + offset;
                        yLoc = 0;
                    }
                    r.set(0, yLoc, this.mRight - this.mLeft, maxBottom - minTop);
                    p.invalidateChild(this, r);
                }
            }
            this.mTop += offset;
            this.mBottom += offset;
            this.mRenderNode.offsetTopAndBottom(offset);
            if (isHardwareAccelerated()) {
                invalidateViewProperty(false, false);
                invalidateParentIfNeededAndWasQuickRejected();
            } else {
                if (!matrixIsIdentity) {
                    invalidateViewProperty(false, true);
                }
                invalidateParentIfNeeded();
            }
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    public void offsetLeftAndRight(int offset) {
        if (offset != 0) {
            boolean matrixIsIdentity = hasIdentityMatrix();
            if (!matrixIsIdentity) {
                invalidateViewProperty(false, false);
            } else if (isHardwareAccelerated()) {
                invalidateViewProperty(false, false);
            } else {
                ViewParent p = this.mParent;
                if (!(p == null || this.mAttachInfo == null)) {
                    int minLeft;
                    int maxRight;
                    Rect r = this.mAttachInfo.mTmpInvalRect;
                    if (offset < 0) {
                        minLeft = this.mLeft + offset;
                        maxRight = this.mRight;
                    } else {
                        minLeft = this.mLeft;
                        maxRight = this.mRight + offset;
                    }
                    r.set(0, 0, maxRight - minLeft, this.mBottom - this.mTop);
                    p.invalidateChild(this, r);
                }
            }
            this.mLeft += offset;
            this.mRight += offset;
            this.mRenderNode.offsetLeftAndRight(offset);
            if (isHardwareAccelerated()) {
                invalidateViewProperty(false, false);
                invalidateParentIfNeededAndWasQuickRejected();
            } else {
                if (!matrixIsIdentity) {
                    invalidateViewProperty(false, true);
                }
                invalidateParentIfNeeded();
            }
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
    }

    @ExportedProperty(deepExport = true, prefix = "layout_")
    public LayoutParams getLayoutParams() {
        return this.mLayoutParams;
    }

    public void setLayoutParams(LayoutParams params) {
        if (params == null) {
            throw new NullPointerException("Layout parameters cannot be null");
        }
        this.mLayoutParams = params;
        resolveLayoutParams();
        if (this.mParent instanceof ViewGroup) {
            ((ViewGroup) this.mParent).onSetLayoutParams(this, params);
        }
        requestLayout();
    }

    public void resolveLayoutParams() {
        if (this.mLayoutParams != null) {
            this.mLayoutParams.resolveLayoutDirection(getLayoutDirection());
        }
    }

    public void scrollTo(int x, int y) {
        if (this.mScrollX != x || this.mScrollY != y) {
            int oldX = this.mScrollX;
            int oldY = this.mScrollY;
            this.mScrollX = x;
            this.mScrollY = y;
            invalidateParentCaches();
            onScrollChanged(this.mScrollX, this.mScrollY, oldX, oldY);
            if (!awakenScrollBars()) {
                postInvalidateOnAnimation();
            }
        }
    }

    public void scrollBy(int x, int y) {
        scrollTo(this.mScrollX + x, this.mScrollY + y);
    }

    protected boolean awakenScrollBars() {
        return this.mScrollCache != null && awakenScrollBars(this.mScrollCache.scrollBarDefaultDelayBeforeFade, true);
    }

    private boolean initialAwakenScrollBars() {
        return this.mScrollCache != null && awakenScrollBars(this.mScrollCache.scrollBarDefaultDelayBeforeFade * 4, true);
    }

    protected boolean awakenScrollBars(int startDelay) {
        return awakenScrollBars(startDelay, true);
    }

    protected boolean awakenScrollBars(int startDelay, boolean invalidate) {
        ScrollabilityCache scrollCache = this.mScrollCache;
        if (scrollCache == null || !scrollCache.fadeScrollBars) {
            return false;
        }
        if (scrollCache.scrollBar == null) {
            scrollCache.scrollBar = new ScrollBarDrawable(this);
            scrollCache.scrollBar.setCallback(this);
            scrollCache.scrollBar.setState(getDrawableState());
        }
        if (!isHorizontalScrollBarEnabled() && !isVerticalScrollBarEnabled()) {
            return false;
        }
        if (invalidate) {
            postInvalidateOnAnimation();
        }
        if (scrollCache.state == 0) {
            startDelay = Math.max(750, startDelay);
        }
        long fadeStartTime = AnimationUtils.currentAnimationTimeMillis() + ((long) startDelay);
        scrollCache.fadeStartTime = fadeStartTime;
        scrollCache.state = 1;
        if (this.mAttachInfo != null) {
            this.mAttachInfo.mHandler.removeCallbacks(scrollCache);
            if (!this.mTwScrollingByScrollbar) {
                this.mAttachInfo.mHandler.postAtTime(scrollCache, fadeStartTime);
            }
        }
        return true;
    }

    private boolean skipInvalidate() {
        return ((this.mViewFlags & 12) == 0 || this.mCurrentAnimation != null || ((this.mParent instanceof ViewGroup) && ((ViewGroup) this.mParent).isViewTransitioning(this))) ? false : true;
    }

    public void invalidate(Rect dirty) {
        int scrollX = this.mScrollX;
        int scrollY = this.mScrollY;
        invalidateInternal(dirty.left - scrollX, dirty.top - scrollY, dirty.right - scrollX, dirty.bottom - scrollY, true, false);
    }

    public void invalidate(int l, int t, int r, int b) {
        int scrollX = this.mScrollX;
        int scrollY = this.mScrollY;
        invalidateInternal(l - scrollX, t - scrollY, r - scrollX, b - scrollY, true, false);
    }

    public void invalidate() {
        invalidate(true);
    }

    void invalidate(boolean invalidateCache) {
        invalidateInternal(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop, invalidateCache, true);
    }

    void invalidateInternal(int l, int t, int r, int b, boolean invalidateCache, boolean fullInvalidate) {
        if (this.mGhostView != null) {
            this.mGhostView.invalidate(true);
        } else if (!skipInvalidate()) {
            if ((this.mPrivateFlags & 48) == 48 || ((invalidateCache && (this.mPrivateFlags & 32768) == 32768) || (this.mPrivateFlags & Integer.MIN_VALUE) != Integer.MIN_VALUE || (fullInvalidate && isOpaque() != this.mLastIsOpaque))) {
                if (fullInvalidate) {
                    this.mLastIsOpaque = isOpaque();
                    this.mPrivateFlags &= -33;
                }
                this.mPrivateFlags |= 2097152;
                if (invalidateCache) {
                    this.mPrivateFlags |= Integer.MIN_VALUE;
                    this.mPrivateFlags &= -32769;
                }
                AttachInfo ai = this.mAttachInfo;
                ViewParent p = this.mParent;
                if (p != null && ai != null && l < r && t < b) {
                    Rect damage = ai.mTmpInvalRect;
                    damage.set(l, t, r, b);
                    p.invalidateChild(this, damage);
                }
                if (this.mBackground != null && this.mBackground.isProjected()) {
                    View receiver = getProjectionReceiver();
                    if (receiver != null) {
                        receiver.damageInParent();
                    }
                }
                if (isHardwareAccelerated() && getZ() != 0.0f) {
                    damageShadowReceiver();
                }
            }
        }
    }

    private View getProjectionReceiver() {
        ViewParent p = getParent();
        while (p != null && (p instanceof View)) {
            View v = (View) p;
            if (v.isProjectionReceiver()) {
                return v;
            }
            p = p.getParent();
        }
        return null;
    }

    private boolean isProjectionReceiver() {
        return this.mBackground != null;
    }

    private void damageShadowReceiver() {
        if (this.mAttachInfo != null) {
            ViewParent p = getParent();
            if (p != null && (p instanceof ViewGroup)) {
                ((ViewGroup) p).damageInParent();
            }
        }
    }

    void invalidateViewProperty(boolean invalidateParent, boolean forceRedraw) {
        if (isHardwareAccelerated() && this.mRenderNode.isValid() && (this.mPrivateFlags & 64) == 0) {
            damageInParent();
        } else {
            if (invalidateParent) {
                invalidateParentCaches();
            }
            if (forceRedraw) {
                this.mPrivateFlags |= 32;
            }
            invalidate(false);
        }
        if (isHardwareAccelerated() && invalidateParent && getZ() != 0.0f) {
            damageShadowReceiver();
        }
    }

    protected void damageInParent() {
        AttachInfo ai = this.mAttachInfo;
        if (this.mParent != null && ai != null) {
            Rect r = ai.mTmpInvalRect;
            r.set(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
            if (this.mParent instanceof ViewGroup) {
                ((ViewGroup) this.mParent).damageChild(this, r);
            } else {
                this.mParent.invalidateChild(this, r);
            }
        }
    }

    void transformRect(Rect rect) {
        if (!getMatrix().isIdentity()) {
            RectF boundingRect = this.mAttachInfo.mTmpTransformRect;
            boundingRect.set(rect);
            getMatrix().mapRect(boundingRect);
            rect.set((int) Math.floor((double) boundingRect.left), (int) Math.floor((double) boundingRect.top), (int) Math.ceil((double) boundingRect.right), (int) Math.ceil((double) boundingRect.bottom));
        }
    }

    protected void invalidateParentCaches() {
        if (this.mParent instanceof View) {
            View view = (View) this.mParent;
            view.mPrivateFlags |= Integer.MIN_VALUE;
        }
    }

    protected void invalidateParentIfNeeded() {
        if (isHardwareAccelerated() && (this.mParent instanceof View)) {
            ((View) this.mParent).invalidate(true);
        }
    }

    protected void invalidateParentIfNeededAndWasQuickRejected() {
        if ((this.mPrivateFlags2 & 268435456) != 0) {
            invalidateParentIfNeeded();
        }
    }

    @ExportedProperty(category = "drawing")
    public boolean isOpaque() {
        return (this.mPrivateFlags & 25165824) == 25165824 && getFinalAlpha() >= 1.0f;
    }

    protected void computeOpaqueFlags() {
        if (this.mBackground == null || this.mBackground.getOpacity() != -1) {
            this.mPrivateFlags &= -8388609;
        } else {
            this.mPrivateFlags |= 8388608;
        }
        int flags = this.mViewFlags;
        if (((flags & 512) == 0 && (flags & 256) == 0) || (flags & 50331648) == 0 || (flags & 50331648) == 33554432) {
            this.mPrivateFlags |= 16777216;
        } else {
            this.mPrivateFlags &= -16777217;
        }
    }

    protected boolean hasOpaqueScrollbars() {
        return (this.mPrivateFlags & 16777216) == 16777216;
    }

    public Handler getHandler() {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mHandler;
        }
        return null;
    }

    public ViewRootImpl getViewRootImpl() {
        if (this.mAttachInfo != null) {
            return this.mAttachInfo.mViewRootImpl;
        }
        return null;
    }

    public HardwareRenderer getHardwareRenderer() {
        return this.mAttachInfo != null ? this.mAttachInfo.mHardwareRenderer : null;
    }

    public boolean post(Runnable action) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mHandler.post(action);
        }
        ViewRootImpl.getRunQueue().post(action);
        return true;
    }

    public boolean postDelayed(Runnable action, long delayMillis) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mHandler.postDelayed(action, delayMillis);
        }
        ViewRootImpl.getRunQueue().postDelayed(action, delayMillis);
        return true;
    }

    public void postOnAnimation(Runnable action) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            attachInfo.mViewRootImpl.mChoreographer.postCallback(1, action, null);
        } else {
            ViewRootImpl.getRunQueue().post(action);
        }
    }

    public void postOnAnimationDelayed(Runnable action, long delayMillis) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            attachInfo.mViewRootImpl.mChoreographer.postCallbackDelayed(1, action, null, delayMillis);
        } else {
            ViewRootImpl.getRunQueue().postDelayed(action, delayMillis);
        }
    }

    public boolean removeCallbacks(Runnable action) {
        if (action != null) {
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null) {
                attachInfo.mHandler.removeCallbacks(action);
                attachInfo.mViewRootImpl.mChoreographer.removeCallbacks(1, action, null);
            }
            ViewRootImpl.getRunQueue().removeCallbacks(action);
        }
        return true;
    }

    public void postInvalidate() {
        postInvalidateDelayed(0);
    }

    public void postInvalidate(int left, int top, int right, int bottom) {
        postInvalidateDelayed(0, left, top, right, bottom);
    }

    public void postInvalidateDelayed(long delayMilliseconds) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            attachInfo.mViewRootImpl.dispatchInvalidateDelayed(this, delayMilliseconds);
        }
    }

    public void postInvalidateDelayed(long delayMilliseconds, int left, int top, int right, int bottom) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            InvalidateInfo info = InvalidateInfo.obtain();
            info.target = this;
            info.left = left;
            info.top = top;
            info.right = right;
            info.bottom = bottom;
            attachInfo.mViewRootImpl.dispatchInvalidateRectDelayed(info, delayMilliseconds);
        }
    }

    public void postInvalidateOnAnimation() {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            attachInfo.mViewRootImpl.dispatchInvalidateOnAnimation(this);
        }
    }

    public void postInvalidateOnAnimation(int left, int top, int right, int bottom) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            InvalidateInfo info = InvalidateInfo.obtain();
            info.target = this;
            info.left = left;
            info.top = top;
            info.right = right;
            info.bottom = bottom;
            attachInfo.mViewRootImpl.dispatchInvalidateRectOnAnimation(info);
        }
    }

    private void postSendViewScrolledAccessibilityEventCallback() {
        if (this.mSendViewScrolledAccessibilityEvent == null) {
            this.mSendViewScrolledAccessibilityEvent = new SendViewScrolledAccessibilityEvent();
        }
        if (!this.mSendViewScrolledAccessibilityEvent.mIsPending) {
            this.mSendViewScrolledAccessibilityEvent.mIsPending = true;
            postDelayed(this.mSendViewScrolledAccessibilityEvent, ViewConfiguration.getSendRecurringAccessibilityEventsInterval());
        }
    }

    public void computeScroll() {
    }

    public boolean isHorizontalFadingEdgeEnabled() {
        return (this.mViewFlags & 4096) == 4096;
    }

    public void setHorizontalFadingEdgeEnabled(boolean horizontalFadingEdgeEnabled) {
        if (isHorizontalFadingEdgeEnabled() != horizontalFadingEdgeEnabled) {
            if (horizontalFadingEdgeEnabled) {
                initScrollCache();
            }
            this.mViewFlags ^= 4096;
        }
    }

    public boolean isVerticalFadingEdgeEnabled() {
        return (this.mViewFlags & 8192) == 8192;
    }

    public void setVerticalFadingEdgeEnabled(boolean verticalFadingEdgeEnabled) {
        if (isVerticalFadingEdgeEnabled() != verticalFadingEdgeEnabled) {
            if (verticalFadingEdgeEnabled) {
                initScrollCache();
            }
            this.mViewFlags ^= 8192;
        }
    }

    protected float getTopFadingEdgeStrength() {
        return computeVerticalScrollOffset() > 0 ? 1.0f : 0.0f;
    }

    protected float getBottomFadingEdgeStrength() {
        return computeVerticalScrollOffset() + computeVerticalScrollExtent() < computeVerticalScrollRange() ? 1.0f : 0.0f;
    }

    protected float getLeftFadingEdgeStrength() {
        return computeHorizontalScrollOffset() > 0 ? 1.0f : 0.0f;
    }

    protected float getRightFadingEdgeStrength() {
        return computeHorizontalScrollOffset() + computeHorizontalScrollExtent() < computeHorizontalScrollRange() ? 1.0f : 0.0f;
    }

    public boolean isHorizontalScrollBarEnabled() {
        return (this.mViewFlags & 256) == 256;
    }

    public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
        if (isHorizontalScrollBarEnabled() != horizontalScrollBarEnabled) {
            this.mViewFlags ^= 256;
            computeOpaqueFlags();
            resolvePadding();
        }
    }

    public boolean isVerticalScrollBarEnabled() {
        return (this.mViewFlags & 512) == 512;
    }

    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        if (isVerticalScrollBarEnabled() != verticalScrollBarEnabled) {
            this.mViewFlags ^= 512;
            computeOpaqueFlags();
            resolvePadding();
        }
    }

    protected void recomputePadding() {
        internalSetPadding(this.mUserPaddingLeft, this.mPaddingTop, this.mUserPaddingRight, this.mUserPaddingBottom);
    }

    public void setScrollbarFadingEnabled(boolean fadeScrollbars) {
        initScrollCache();
        ScrollabilityCache scrollabilityCache = this.mScrollCache;
        scrollabilityCache.fadeScrollBars = fadeScrollbars;
        if (fadeScrollbars) {
            scrollabilityCache.state = 0;
        } else {
            scrollabilityCache.state = 1;
        }
    }

    public boolean isScrollbarFadingEnabled() {
        return this.mScrollCache != null && this.mScrollCache.fadeScrollBars;
    }

    public int getScrollBarDefaultDelayBeforeFade() {
        return this.mScrollCache == null ? ViewConfiguration.getScrollDefaultDelay() : this.mScrollCache.scrollBarDefaultDelayBeforeFade;
    }

    public void setScrollBarDefaultDelayBeforeFade(int scrollBarDefaultDelayBeforeFade) {
        getScrollCache().scrollBarDefaultDelayBeforeFade = scrollBarDefaultDelayBeforeFade;
    }

    public int getScrollBarFadeDuration() {
        return this.mScrollCache == null ? ViewConfiguration.getScrollBarFadeDuration() : this.mScrollCache.scrollBarFadeDuration;
    }

    public void setScrollBarFadeDuration(int scrollBarFadeDuration) {
        getScrollCache().scrollBarFadeDuration = scrollBarFadeDuration;
    }

    public int getScrollBarSize() {
        return this.mScrollCache == null ? ViewConfiguration.get(this.mContext).getScaledScrollBarSize() : this.mScrollCache.scrollBarSize;
    }

    public void setScrollBarSize(int scrollBarSize) {
        getScrollCache().scrollBarSize = scrollBarSize;
    }

    public void setScrollBarStyle(int style) {
        if (style != (this.mViewFlags & 50331648)) {
            this.mViewFlags = (this.mViewFlags & -50331649) | (style & 50331648);
            computeOpaqueFlags();
            resolvePadding();
        }
    }

    @ExportedProperty(mapping = {@IntToString(from = 0, to = "INSIDE_OVERLAY"), @IntToString(from = 16777216, to = "INSIDE_INSET"), @IntToString(from = 33554432, to = "OUTSIDE_OVERLAY"), @IntToString(from = 50331648, to = "OUTSIDE_INSET")})
    public int getScrollBarStyle() {
        return this.mViewFlags & 50331648;
    }

    protected int computeHorizontalScrollRange() {
        return getWidth();
    }

    protected int computeHorizontalScrollOffset() {
        return this.mScrollX;
    }

    protected int computeHorizontalScrollExtent() {
        return getWidth();
    }

    protected int computeVerticalScrollRange() {
        return getHeight();
    }

    protected int computeVerticalScrollOffset() {
        return this.mScrollY;
    }

    protected int computeVerticalScrollExtent() {
        return getHeight();
    }

    public boolean canScrollHorizontally(int direction) {
        int offset = computeHorizontalScrollOffset();
        int range = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
        if (range == 0) {
            return false;
        }
        if (direction < 0) {
            if (offset <= 0) {
                return false;
            }
            return true;
        } else if (offset >= range - 1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean canScrollVertically(int direction) {
        int offset = computeVerticalScrollOffset();
        int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
        if (range == 0) {
            return false;
        }
        if (direction < 0) {
            if (offset <= 0) {
                return false;
            }
            return true;
        } else if (offset >= range - 1) {
            return false;
        } else {
            return true;
        }
    }

    void getScrollIndicatorBounds(Rect out) {
        out.left = this.mScrollX;
        out.right = (this.mScrollX + this.mRight) - this.mLeft;
        out.top = this.mScrollY;
        out.bottom = (this.mScrollY + this.mBottom) - this.mTop;
    }

    private void onDrawScrollIndicators(Canvas c) {
        if ((this.mPrivateFlags3 & SCROLL_INDICATORS_PFLAG3_MASK) != 0) {
            Drawable dr = this.mScrollIndicatorDrawable;
            if (dr != null) {
                int leftRtl;
                int rightRtl;
                int h = dr.getIntrinsicHeight();
                int w = dr.getIntrinsicWidth();
                Rect rect = this.mAttachInfo.mTmpInvalRect;
                getScrollIndicatorBounds(rect);
                if ((this.mPrivateFlags3 & 256) != 0 && canScrollVertically(-1)) {
                    dr.setBounds(rect.left, rect.top, rect.right, rect.top + h);
                    dr.draw(c);
                }
                if ((this.mPrivateFlags3 & 512) != 0 && canScrollVertically(1)) {
                    dr.setBounds(rect.left, rect.bottom - h, rect.right, rect.bottom);
                    dr.draw(c);
                }
                if (getLayoutDirection() == 1) {
                    leftRtl = 8192;
                    rightRtl = 4096;
                } else {
                    leftRtl = 4096;
                    rightRtl = 8192;
                }
                if ((this.mPrivateFlags3 & (leftRtl | 1024)) != 0 && canScrollHorizontally(-1)) {
                    dr.setBounds(rect.left, rect.top, rect.left + w, rect.bottom);
                    dr.draw(c);
                }
                if ((this.mPrivateFlags3 & (rightRtl | 2048)) != 0 && canScrollHorizontally(1)) {
                    dr.setBounds(rect.right - w, rect.top, rect.right, rect.bottom);
                    dr.draw(c);
                }
            }
        }
    }

    protected final void onDrawScrollBars(Canvas canvas) {
        ScrollabilityCache cache = this.mScrollCache;
        if (cache != null) {
            int state = cache.state;
            if (state != 0) {
                boolean invalidate = false;
                if (state == 2) {
                    if (cache.interpolatorValues == null) {
                        cache.interpolatorValues = new float[1];
                    }
                    float[] values = cache.interpolatorValues;
                    if (cache.scrollBarInterpolator.timeToValues(values) == Result.FREEZE_END) {
                        cache.state = 0;
                    } else {
                        cache.scrollBar.mutate().setAlpha(Math.round(values[0]));
                    }
                    invalidate = true;
                } else {
                    cache.scrollBar.mutate().setAlpha(255);
                }
                int viewFlags = this.mViewFlags;
                boolean drawHorizontalScrollBar = (viewFlags & 256) == 256 && !isTwHorizontalScrollBarHidden();
                boolean drawVerticalScrollBar = (viewFlags & 512) == 512 && !isVerticalScrollBarHidden();
                if (drawVerticalScrollBar || drawHorizontalScrollBar) {
                    int size;
                    int top;
                    int left;
                    int right;
                    int bottom;
                    int width = this.mRight - this.mLeft;
                    int height = this.mBottom - this.mTop;
                    ScrollBarDrawable scrollBar = cache.scrollBar;
                    int scrollX = this.mScrollX;
                    int scrollY = this.mScrollY;
                    int inside = (33554432 & viewFlags) == 0 ? -1 : 0;
                    if (drawHorizontalScrollBar) {
                        size = scrollBar.getSize(false);
                        if (size <= 0) {
                            size = cache.scrollBarSize;
                        }
                        scrollBar.setParameters(computeHorizontalScrollRange(), computeHorizontalScrollOffset(), computeHorizontalScrollExtent(), false);
                        top = ((scrollY + height) - size) - (this.mUserPaddingBottom & inside);
                        left = scrollX + (this.mPaddingLeft & inside);
                        right = ((scrollX + width) - (this.mUserPaddingRight & inside)) - (drawVerticalScrollBar ? getVerticalScrollbarWidth() : 0);
                        bottom = top + size;
                        onDrawHorizontalScrollBar(canvas, scrollBar, left, top, right, bottom);
                        if (invalidate) {
                            invalidate(left, top, right, bottom);
                        }
                    }
                    if (drawVerticalScrollBar) {
                        size = scrollBar.getSize(true);
                        if (size <= 0) {
                            size = cache.scrollBarSize;
                        }
                        scrollBar.setParameters(computeVerticalScrollRange(), computeVerticalScrollOffset(), computeVerticalScrollExtent(), true);
                        int verticalScrollbarPosition = this.mVerticalScrollbarPosition;
                        if (verticalScrollbarPosition == 0) {
                            verticalScrollbarPosition = isLayoutRtl() ? 1 : 2;
                        }
                        switch (verticalScrollbarPosition) {
                            case 1:
                                left = scrollX + (this.mUserPaddingLeft & inside);
                                break;
                            default:
                                left = ((scrollX + width) - size) - (this.mUserPaddingRight & inside);
                                break;
                        }
                        top = scrollY + (this.mPaddingTop & inside);
                        right = left + size;
                        bottom = (scrollY + height) - (this.mUserPaddingBottom & inside);
                        if (this.mNeededToChangedScrollBarPosition) {
                            left -= this.mScrollBarPositionPadding;
                            right -= this.mScrollBarPositionPadding;
                            cache.scrollBar.setBounds(left, top, right, bottom);
                        }
                        onDrawVerticalScrollBar(canvas, scrollBar, left, top, right, bottom);
                        if (invalidate) {
                            invalidate(left, top, right, bottom);
                        }
                    }
                }
            }
        }
    }

    public void setVerticalScrollBarPadding(boolean flag) {
        this.mNeededToChangedScrollBarPosition = flag;
    }

    public void setVerticalScrollBarPaddingPosition(int paddingValue) {
        this.mScrollBarPositionPadding = paddingValue;
    }

    protected boolean isVerticalScrollBarHidden() {
        return false;
    }

    protected boolean isTwHorizontalScrollBarHidden() {
        return false;
    }

    protected void onDrawHorizontalScrollBar(Canvas canvas, Drawable scrollBar, int l, int t, int r, int b) {
        scrollBar.setBounds(l, t, r, b);
        scrollBar.draw(canvas);
    }

    protected void onDrawVerticalScrollBar(Canvas canvas, Drawable scrollBar, int l, int t, int r, int b) {
        scrollBar.setBounds(l, t, r, b);
        scrollBar.draw(canvas);
    }

    protected void onDraw(Canvas canvas) {
    }

    void assignParent(ViewParent parent) {
        if (this.mParent == null) {
            this.mParent = parent;
        } else if (parent == null) {
            this.mParent = null;
        } else {
            throw new RuntimeException("view " + this + " being added, but" + " it already has a parent");
        }
    }

    protected void onAttachedToWindow() {
        if (!(this.mParent == null || (this.mPrivateFlags & 512) == 0)) {
            this.mParent.requestTransparentRegion(this);
        }
        this.mPrivateFlags3 &= -5;
        jumpDrawablesToCurrentState();
        resetSubtreeAccessibilityStateChanged();
        rebuildOutline();
        if (isFocused()) {
            InputMethodManager imm = InputMethodManager.peekInstance();
            if (imm != null) {
                imm.focusIn(this);
            }
        }
        this.mIsDetachedFromWindow = false;
    }

    public boolean resolveRtlPropertiesIfNeeded() {
        if (!needRtlPropertiesResolution()) {
            return false;
        }
        if (!isLayoutDirectionResolved()) {
            resolveLayoutDirection();
            resolveLayoutParams();
        }
        if (!isTextDirectionResolved()) {
            resolveTextDirection();
        }
        if (!isTextAlignmentResolved()) {
            resolveTextAlignment();
        }
        if (!areDrawablesResolved()) {
            resolveDrawables();
        }
        if (!isPaddingResolved()) {
            resolvePadding();
        }
        onRtlPropertiesChanged(getLayoutDirection());
        return true;
    }

    public void resetRtlProperties() {
        resetResolvedLayoutDirection();
        resetResolvedTextDirection();
        resetResolvedTextAlignment();
        resetResolvedPadding();
        resetResolvedDrawables();
    }

    void dispatchScreenStateChanged(int screenState) {
        onScreenStateChanged(screenState);
    }

    public void onScreenStateChanged(int screenState) {
    }

    private boolean hasRtlSupport() {
        return this.mContext.getApplicationInfo().hasRtlSupport();
    }

    private boolean isRtlCompatibilityMode() {
        return (getContext().getApplicationInfo().targetSdkVersion < 17 && !isSystemApp()) || !hasRtlSupport();
    }

    private boolean isSystemApp() {
        return (getContext().getApplicationInfo().flags & 1) != 0;
    }

    private boolean needRtlPropertiesResolution() {
        return (this.mPrivateFlags2 & ALL_RTL_PROPERTIES_RESOLVED) != ALL_RTL_PROPERTIES_RESOLVED;
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
    }

    public boolean resolveLayoutDirection() {
        this.mPrivateFlags2 &= -49;
        if (hasRtlSupport()) {
            switch ((this.mPrivateFlags2 & 12) >> 2) {
                case 1:
                    this.mPrivateFlags2 |= 16;
                    break;
                case 2:
                    if (canResolveLayoutDirection()) {
                        try {
                            if (this.mParent != null && this.mParent.isLayoutDirectionResolved()) {
                                if (this.mParent.getLayoutDirection() == 1) {
                                    this.mPrivateFlags2 |= 16;
                                    break;
                                }
                            }
                            return false;
                        } catch (AbstractMethodError e) {
                            Log.e(VIEW_LOG_TAG, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", e);
                            break;
                        }
                    }
                    return false;
                    break;
                case 3:
                    if (1 == TextUtils.getLayoutDirectionFromLocale(Locale.getDefault())) {
                        this.mPrivateFlags2 |= 16;
                        break;
                    }
                    break;
            }
        }
        this.mPrivateFlags2 |= 32;
        return true;
    }

    public boolean canResolveLayoutDirection() {
        switch (getRawLayoutDirection()) {
            case 2:
                if (this.mParent != null) {
                    try {
                        return this.mParent.canResolveLayoutDirection();
                    } catch (AbstractMethodError e) {
                        Log.e(VIEW_LOG_TAG, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", e);
                    }
                }
                return false;
            default:
                return true;
        }
    }

    public void resetResolvedLayoutDirection() {
        this.mPrivateFlags2 &= -49;
    }

    public boolean isLayoutDirectionInherited() {
        return getRawLayoutDirection() == 2;
    }

    public boolean isLayoutDirectionResolved() {
        return (this.mPrivateFlags2 & 32) == 32;
    }

    boolean isPaddingResolved() {
        return (this.mPrivateFlags2 & 536870912) == 536870912;
    }

    public void resolvePadding() {
        int resolvedLayoutDirection = getLayoutDirection();
        if (!isRtlCompatibilityMode()) {
            if (!(this.mBackground == null || (this.mLeftPaddingDefined && this.mRightPaddingDefined))) {
                Rect padding = (Rect) sThreadLocal.get();
                if (padding == null) {
                    padding = new Rect();
                    sThreadLocal.set(padding);
                }
                this.mBackground.getPadding(padding);
                if (!this.mLeftPaddingDefined) {
                    this.mUserPaddingLeftInitial = padding.left;
                }
                if (!this.mRightPaddingDefined) {
                    this.mUserPaddingRightInitial = padding.right;
                }
            }
            switch (resolvedLayoutDirection) {
                case 1:
                    if (this.mUserPaddingStart != Integer.MIN_VALUE) {
                        this.mUserPaddingRight = this.mUserPaddingStart;
                    } else {
                        this.mUserPaddingRight = this.mUserPaddingRightInitial;
                    }
                    if (this.mUserPaddingEnd == Integer.MIN_VALUE) {
                        this.mUserPaddingLeft = this.mUserPaddingLeftInitial;
                        break;
                    } else {
                        this.mUserPaddingLeft = this.mUserPaddingEnd;
                        break;
                    }
                default:
                    if (this.mUserPaddingStart != Integer.MIN_VALUE) {
                        this.mUserPaddingLeft = this.mUserPaddingStart;
                    } else {
                        this.mUserPaddingLeft = this.mUserPaddingLeftInitial;
                    }
                    if (this.mUserPaddingEnd == Integer.MIN_VALUE) {
                        this.mUserPaddingRight = this.mUserPaddingRightInitial;
                        break;
                    } else {
                        this.mUserPaddingRight = this.mUserPaddingEnd;
                        break;
                    }
            }
            this.mUserPaddingBottom = this.mUserPaddingBottom >= 0 ? this.mUserPaddingBottom : this.mPaddingBottom;
        }
        internalSetPadding(this.mUserPaddingLeft, this.mPaddingTop, this.mUserPaddingRight, this.mUserPaddingBottom);
        onRtlPropertiesChanged(resolvedLayoutDirection);
        this.mPrivateFlags2 |= 536870912;
    }

    public void resetResolvedPadding() {
        resetResolvedPaddingInternal();
    }

    void resetResolvedPaddingInternal() {
        this.mPrivateFlags2 &= -536870913;
    }

    protected void onDetachedFromWindow() {
    }

    protected void onDetachedFromWindowInternal() {
        if (!(this.mHoverPopupType == 0 || this.mHoverPopup == null)) {
            this.mHoverPopup.dismiss();
            this.mHoverPopup = null;
        }
        this.mIsDetachedFromWindow = true;
        this.mRootViewCheckForDialog = null;
        this.mPrivateFlags &= -67108865;
        this.mPrivateFlags3 &= -5;
        removeUnsetPressCallback();
        removeLongPressCallback();
        removePerformClickCallback();
        removeSendViewScrolledAccessibilityEventCallback();
        stopNestedScroll();
        jumpDrawablesToCurrentState();
        destroyDrawingCache();
        cleanupDraw();
        this.mCurrentAnimation = null;
    }

    private void cleanupDraw() {
        resetDisplayList();
        if (this.mAttachInfo != null) {
            this.mAttachInfo.mViewRootImpl.cancelInvalidate(this);
        }
    }

    void invalidateInheritedLayoutMode(int layoutModeOfRoot) {
    }

    protected int getWindowAttachCount() {
        return this.mWindowAttachCount;
    }

    public IBinder getWindowToken() {
        return this.mAttachInfo != null ? this.mAttachInfo.mWindowToken : null;
    }

    public WindowId getWindowId() {
        if (this.mAttachInfo == null) {
            return null;
        }
        if (this.mAttachInfo.mWindowId == null) {
            try {
                this.mAttachInfo.mIWindowId = this.mAttachInfo.mSession.getWindowId(this.mAttachInfo.mWindowToken);
                this.mAttachInfo.mWindowId = new WindowId(this.mAttachInfo.mIWindowId);
            } catch (RemoteException e) {
            }
        }
        return this.mAttachInfo.mWindowId;
    }

    public IBinder getApplicationWindowToken() {
        AttachInfo ai = this.mAttachInfo;
        if (ai == null) {
            return null;
        }
        IBinder appWindowToken = ai.mPanelParentWindowToken;
        if (appWindowToken == null) {
            return ai.mWindowToken;
        }
        return appWindowToken;
    }

    public Display getDisplay() {
        return this.mAttachInfo != null ? this.mAttachInfo.mDisplay : null;
    }

    IWindowSession getWindowSession() {
        return this.mAttachInfo != null ? this.mAttachInfo.mSession : null;
    }

    int combineVisibility(int vis1, int vis2) {
        return Math.max(vis1, vis2);
    }

    void dispatchAttachedToWindow(AttachInfo info, int visibility) {
        CopyOnWriteArrayList<OnAttachStateChangeListener> listeners = null;
        this.mAttachInfo = info;
        if (this.mOverlay != null) {
            this.mOverlay.getOverlayView().dispatchAttachedToWindow(info, visibility);
        }
        this.mWindowAttachCount++;
        this.mPrivateFlags |= 1024;
        if (this.mFloatingTreeObserver != null) {
            info.mTreeObserver.merge(this.mFloatingTreeObserver);
            this.mFloatingTreeObserver = null;
        }
        if ((this.mPrivateFlags & 524288) != 0) {
            this.mAttachInfo.mScrollContainers.add(this);
            this.mPrivateFlags |= 1048576;
        }
        performCollectViewAttributes(this.mAttachInfo, visibility);
        onAttachedToWindow();
        ListenerInfo li = this.mListenerInfo;
        if (li != null) {
            listeners = li.mOnAttachStateChangeListeners;
        }
        if (listeners != null && listeners.size() > 0) {
            Iterator i$ = listeners.iterator();
            while (i$.hasNext()) {
                ((OnAttachStateChangeListener) i$.next()).onViewAttachedToWindow(this);
            }
        }
        int vis = info.mWindowVisibility;
        if (vis != 8) {
            onWindowVisibilityChanged(vis);
        }
        onVisibilityChanged(this, visibility);
        if ((this.mPrivateFlags & 1024) != 0) {
            refreshDrawableState();
        }
        needGlobalAttributesUpdate(false);
    }

    void dispatchDetachedFromWindow() {
        CopyOnWriteArrayList<OnAttachStateChangeListener> listeners;
        AttachInfo info = this.mAttachInfo;
        if (!(info == null || info.mWindowVisibility == 8)) {
            onWindowVisibilityChanged(8);
        }
        onDetachedFromWindow();
        onDetachedFromWindowInternal();
        InputMethodManager imm = InputMethodManager.peekInstance();
        if (imm != null) {
            imm.onViewDetachedFromWindow(this);
        }
        ListenerInfo li = this.mListenerInfo;
        if (li != null) {
            listeners = li.mOnAttachStateChangeListeners;
        } else {
            listeners = null;
        }
        if (listeners != null && listeners.size() > 0) {
            Iterator i$ = listeners.iterator();
            while (i$.hasNext()) {
                ((OnAttachStateChangeListener) i$.next()).onViewDetachedFromWindow(this);
            }
        }
        if ((this.mPrivateFlags & 1048576) != 0) {
            this.mAttachInfo.mScrollContainers.remove(this);
            this.mPrivateFlags &= -1048577;
        }
        this.mAttachInfo = null;
        if (this.mOverlay != null) {
            this.mOverlay.getOverlayView().dispatchDetachedFromWindow();
        }
    }

    public final void cancelPendingInputEvents() {
        dispatchCancelPendingInputEvents();
    }

    void dispatchCancelPendingInputEvents() {
        this.mPrivateFlags3 &= -17;
        onCancelPendingInputEvents();
        if ((this.mPrivateFlags3 & 16) != 16) {
            throw new SuperNotCalledException("View " + getClass().getSimpleName() + " did not call through to super.onCancelPendingInputEvents()");
        }
    }

    public void onCancelPendingInputEvents() {
        removePerformClickCallback();
        cancelLongPress();
        this.mPrivateFlags3 |= 16;
    }

    public void saveHierarchyState(SparseArray<Parcelable> container) {
        dispatchSaveInstanceState(container);
    }

    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        if (this.mID != -1 && (this.mViewFlags & 65536) == 0) {
            this.mPrivateFlags &= -131073;
            Parcelable state = onSaveInstanceState();
            if ((this.mPrivateFlags & 131072) == 0) {
                throw new IllegalStateException("Derived class did not call super.onSaveInstanceState()");
            } else if (state != null) {
                container.put(this.mID, state);
            }
        }
    }

    protected Parcelable onSaveInstanceState() {
        this.mPrivateFlags |= 131072;
        if (this.mStartActivityRequestWho == null) {
            return BaseSavedState.EMPTY_STATE;
        }
        BaseSavedState state = new BaseSavedState(AbsSavedState.EMPTY_STATE);
        state.mStartActivityRequestWhoSaved = this.mStartActivityRequestWho;
        return state;
    }

    public void restoreHierarchyState(SparseArray<Parcelable> container) {
        dispatchRestoreInstanceState(container);
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        if (this.mID != -1) {
            Parcelable state = (Parcelable) container.get(this.mID);
            if (state != null) {
                this.mPrivateFlags &= -131073;
                onRestoreInstanceState(state);
                if ((this.mPrivateFlags & 131072) == 0) {
                    throw new IllegalStateException("Derived class did not call super.onRestoreInstanceState()");
                }
            }
        }
    }

    protected void onRestoreInstanceState(Parcelable state) {
        this.mPrivateFlags |= 131072;
        if (state != null && !(state instanceof AbsSavedState)) {
            throw new IllegalArgumentException("Wrong state class, expecting View State but received " + state.getClass().toString() + " instead. This usually happens " + "when two views of different type have the same id in the same hierarchy. " + "This view's id is " + ViewDebug.resolveId(this.mContext, getId()) + ". Make sure " + "other views do not use the same id.");
        } else if (state != null && (state instanceof BaseSavedState)) {
            this.mStartActivityRequestWho = ((BaseSavedState) state).mStartActivityRequestWhoSaved;
        }
    }

    public long getDrawingTime() {
        return this.mAttachInfo != null ? this.mAttachInfo.mDrawingTime : 0;
    }

    public void setDuplicateParentStateEnabled(boolean enabled) {
        setFlags(enabled ? 4194304 : 0, 4194304);
    }

    public boolean isDuplicateParentStateEnabled() {
        return (this.mViewFlags & 4194304) == 4194304;
    }

    public void setLayerType(int layerType, Paint paint) {
        if (layerType < 0 || layerType > 2) {
            throw new IllegalArgumentException("Layer type can only be one of: LAYER_TYPE_NONE, LAYER_TYPE_SOFTWARE or LAYER_TYPE_HARDWARE");
        } else if (this.mRenderNode.setLayerType(layerType)) {
            if (this.mLayerType == 1) {
                destroyDrawingCache();
            }
            this.mLayerType = layerType;
            if (this.mLayerType == 0) {
                paint = null;
            } else if (paint == null) {
                paint = new Paint();
            }
            this.mLayerPaint = paint;
            this.mRenderNode.setLayerPaint(this.mLayerPaint);
            invalidateParentCaches();
            invalidate(true);
        } else {
            setLayerPaint(paint);
        }
    }

    public void setLayerPaint(Paint paint) {
        int layerType = getLayerType();
        if (layerType != 0) {
            if (paint == null) {
                paint = new Paint();
            }
            this.mLayerPaint = paint;
            if (layerType != 2) {
                invalidate();
            } else if (this.mRenderNode.setLayerPaint(this.mLayerPaint)) {
                invalidateViewProperty(false, false);
            }
        }
    }

    public int getLayerType() {
        return this.mLayerType;
    }

    public void buildLayer() {
        if (this.mLayerType != 0) {
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo == null) {
                throw new IllegalStateException("This view must be attached to a window first");
            } else if (getWidth() != 0 && getHeight() != 0) {
                switch (this.mLayerType) {
                    case 1:
                        buildDrawingCache(true);
                        return;
                    case 2:
                        updateDisplayListIfDirty();
                        if (attachInfo.mHardwareRenderer != null && this.mRenderNode.isValid()) {
                            attachInfo.mHardwareRenderer.buildLayer(this.mRenderNode);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    HardwareLayer getHardwareLayer() {
        return null;
    }

    protected void destroyHardwareResources() {
        resetDisplayList();
    }

    public void setDrawingCacheEnabled(boolean enabled) {
        int i = 0;
        this.mCachingFailed = false;
        if (enabled) {
            i = 32768;
        }
        setFlags(i, 32768);
    }

    @ExportedProperty(category = "drawing")
    public boolean isDrawingCacheEnabled() {
        return (this.mViewFlags & 32768) == 32768;
    }

    public void outputDirtyFlags(String indent, boolean clear, int clearMask) {
        Log.d(VIEW_LOG_TAG, indent + this + "             DIRTY(" + (this.mPrivateFlags & PFLAG_DIRTY_MASK) + ") DRAWN(" + (this.mPrivateFlags & 32) + ")" + " CACHE_VALID(" + (this.mPrivateFlags & 32768) + ") INVALIDATED(" + (this.mPrivateFlags & Integer.MIN_VALUE) + ")");
        if (clear) {
            this.mPrivateFlags &= clearMask;
        }
        if (this instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) this;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                parent.getChildAt(i).outputDirtyFlags(indent + "  ", clear, clearMask);
            }
        }
    }

    protected void dispatchGetDisplayList() {
    }

    public boolean canHaveDisplayList() {
        return (this.mAttachInfo == null || this.mAttachInfo.mHardwareRenderer == null) ? false : true;
    }

    public RenderNode updateDisplayListIfDirty() {
        RenderNode renderNode = this.mRenderNode;
        if (canHaveDisplayList()) {
            if ((this.mPrivateFlags & 32768) != 0 && renderNode.isValid() && !this.mRecreateDisplayList) {
                this.mPrivateFlags |= 32800;
                this.mPrivateFlags &= -6291457;
            } else if (!renderNode.isValid() || this.mRecreateDisplayList) {
                this.mRecreateDisplayList = true;
                int width = this.mRight - this.mLeft;
                int height = this.mBottom - this.mTop;
                int layerType = getLayerType();
                DisplayListCanvas canvas = renderNode.start(width, height);
                canvas.setHighContrastText(this.mAttachInfo.mHighContrastText);
                try {
                    HardwareLayer layer = getHardwareLayer();
                    if (layer != null && layer.isValid()) {
                        canvas.drawHardwareLayer(layer, 0.0f, 0.0f, this.mLayerPaint);
                    } else if (layerType == 1) {
                        buildDrawingCache(true);
                        Bitmap cache = getDrawingCache(true);
                        if (cache != null) {
                            canvas.drawBitmap(cache, 0.0f, 0.0f, this.mLayerPaint);
                        }
                    } else {
                        computeScroll();
                        canvas.translate((float) (-this.mScrollX), (float) (-this.mScrollY));
                        this.mPrivateFlags |= 32800;
                        this.mPrivateFlags &= -6291457;
                        if ((this.mPrivateFlags & 128) == 128) {
                            dispatchDraw(canvas);
                            if (!(this.mOverlay == null || this.mOverlay.isEmpty())) {
                                this.mOverlay.getOverlayView().draw(canvas);
                            }
                        } else {
                            draw(canvas);
                        }
                    }
                    renderNode.end(canvas);
                    setDisplayListProperties(renderNode);
                } catch (Throwable th) {
                    renderNode.end(canvas);
                    setDisplayListProperties(renderNode);
                }
            } else {
                this.mPrivateFlags |= 32800;
                this.mPrivateFlags &= -6291457;
                dispatchGetDisplayList();
            }
        }
        return renderNode;
    }

    private void resetDisplayList() {
        if (this.mRenderNode.isValid()) {
            this.mRenderNode.destroyDisplayListData();
        }
        if (this.mBackgroundRenderNode != null && this.mBackgroundRenderNode.isValid()) {
            this.mBackgroundRenderNode.destroyDisplayListData();
        }
    }

    public Bitmap getDrawingCache() {
        return getDrawingCache(false);
    }

    public Bitmap getDrawingCache(boolean autoScale) {
        if ((this.mViewFlags & 131072) == 131072) {
            return null;
        }
        if ((this.mViewFlags & 32768) == 32768) {
            buildDrawingCache(autoScale);
        }
        return autoScale ? this.mDrawingCache : this.mUnscaledDrawingCache;
    }

    public void destroyDrawingCache() {
        if (this.mDrawingCache != null) {
            this.mDrawingCache.recycle();
            this.mDrawingCache = null;
        }
        if (this.mUnscaledDrawingCache != null) {
            this.mUnscaledDrawingCache.recycle();
            this.mUnscaledDrawingCache = null;
        }
    }

    public void setDrawingCacheBackgroundColor(int color) {
        if (color != this.mDrawingCacheBackgroundColor) {
            this.mDrawingCacheBackgroundColor = color;
            this.mPrivateFlags &= -32769;
        }
    }

    public int getDrawingCacheBackgroundColor() {
        return this.mDrawingCacheBackgroundColor;
    }

    public void buildDrawingCache() {
        buildDrawingCache(false);
    }

    public void buildDrawingCache(boolean autoScale) {
        if ((this.mPrivateFlags & 32768) != 0) {
            if (autoScale) {
                if (this.mDrawingCache != null) {
                    return;
                }
            } else if (this.mUnscaledDrawingCache != null) {
                return;
            }
        }
        if (Trace.isTagEnabled(8)) {
            Trace.traceBegin(8, "buildDrawingCache/SW Layer for " + getClass().getSimpleName());
        }
        try {
            buildDrawingCacheImpl(autoScale);
        } finally {
            Trace.traceEnd(8);
        }
    }

    private void buildDrawingCacheImpl(boolean autoScale) {
        this.mCachingFailed = false;
        int width = this.mRight - this.mLeft;
        int height = this.mBottom - this.mTop;
        AttachInfo attachInfo = this.mAttachInfo;
        boolean scalingRequired = attachInfo != null && attachInfo.mScalingRequired;
        if (autoScale && scalingRequired) {
            width = (int) ((((float) width) * attachInfo.mApplicationScale) + 0.5f);
            height = (int) ((((float) height) * attachInfo.mApplicationScale) + 0.5f);
        }
        int drawingCacheBackgroundColor = this.mDrawingCacheBackgroundColor;
        boolean opaque = drawingCacheBackgroundColor != 0 || isOpaque();
        boolean use32BitCache = attachInfo != null && attachInfo.mUse32BitDrawingCache;
        int i = width * height;
        int i2 = (!opaque || use32BitCache) ? 4 : 2;
        long projectedBitmapSize = (long) (i2 * i);
        long drawingCacheSize = (long) ViewConfiguration.get(this.mContext).getScaledMaximumDrawingCacheSize();
        if (width <= 0 || height <= 0 || projectedBitmapSize > drawingCacheSize) {
            if (width > 0 && height > 0) {
                Log.w(VIEW_LOG_TAG, getClass().getSimpleName() + " not displayed because it is" + " too large to fit into a software layer (or drawing cache), needs " + projectedBitmapSize + " bytes, only " + drawingCacheSize + " available");
            }
            destroyDrawingCache();
            this.mCachingFailed = true;
            return;
        }
        Bitmap bitmap;
        Canvas canvas;
        boolean clear = true;
        if (autoScale) {
            bitmap = this.mDrawingCache;
        } else {
            bitmap = this.mUnscaledDrawingCache;
        }
        if (!(bitmap != null && bitmap.getWidth() == width && bitmap.getHeight() == height)) {
            Config quality;
            if (opaque) {
                quality = use32BitCache ? Config.ARGB_8888 : Config.RGB_565;
            } else {
                i2 = this.mViewFlags & DRAWING_CACHE_QUALITY_MASK;
                quality = Config.ARGB_8888;
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
            try {
                bitmap = Bitmap.createBitmap(this.mResources.getDisplayMetrics(), width, height, quality);
                bitmap.setDensity(getResources().getDisplayMetrics().densityDpi);
                if (autoScale) {
                    this.mDrawingCache = bitmap;
                } else {
                    this.mUnscaledDrawingCache = bitmap;
                }
                if (opaque && use32BitCache) {
                    bitmap.setHasAlpha(false);
                }
                clear = drawingCacheBackgroundColor != 0;
            } catch (OutOfMemoryError e) {
                if (autoScale) {
                    this.mDrawingCache = null;
                } else {
                    this.mUnscaledDrawingCache = null;
                }
                this.mCachingFailed = true;
                return;
            }
        }
        if (attachInfo != null) {
            canvas = attachInfo.mCanvas;
            if (canvas == null) {
                canvas = new Canvas();
            }
            canvas.setBitmap(bitmap);
            canvas.setHighContrastText(attachInfo.mHighContrastText);
            attachInfo.mCanvas = null;
        } else {
            canvas = new Canvas(bitmap);
        }
        if (clear) {
            bitmap.eraseColor(drawingCacheBackgroundColor);
        }
        computeScroll();
        int restoreCount = canvas.save();
        if (autoScale && scalingRequired) {
            float scale = attachInfo.mApplicationScale;
            canvas.scale(scale, scale);
        }
        canvas.translate((float) (-this.mScrollX), (float) (-this.mScrollY));
        this.mPrivateFlags |= 32;
        if (!(this.mAttachInfo != null && this.mAttachInfo.mHardwareAccelerated && this.mLayerType == 0)) {
            this.mPrivateFlags |= 32768;
        }
        if ((this.mPrivateFlags & 128) == 128) {
            this.mPrivateFlags &= -6291457;
            dispatchDraw(canvas);
            if (!(this.mOverlay == null || this.mOverlay.isEmpty())) {
                this.mOverlay.getOverlayView().draw(canvas);
            }
        } else {
            draw(canvas);
        }
        canvas.restoreToCount(restoreCount);
        canvas.setBitmap(null);
        if (attachInfo != null) {
            attachInfo.mCanvas = canvas;
        }
    }

    Bitmap createSnapshot(Config quality, int backgroundColor, boolean skipChildren) {
        int width = this.mRight - this.mLeft;
        int height = this.mBottom - this.mTop;
        AttachInfo attachInfo = this.mAttachInfo;
        float scale = attachInfo != null ? attachInfo.mApplicationScale : 1.0f;
        width = (int) ((((float) width) * scale) + 0.5f);
        height = (int) ((((float) height) * scale) + 0.5f);
        DisplayMetrics displayMetrics = this.mResources.getDisplayMetrics();
        if (width <= 0) {
            width = 1;
        }
        if (height <= 0) {
            height = 1;
        }
        Bitmap bitmap = Bitmap.createBitmap(displayMetrics, width, height, quality);
        if (bitmap == null) {
            throw new OutOfMemoryError();
        }
        Canvas canvas;
        Resources resources = getResources();
        if (resources != null) {
            bitmap.setDensity(resources.getDisplayMetrics().densityDpi);
        }
        if (attachInfo != null) {
            canvas = attachInfo.mCanvas;
            if (canvas == null) {
                canvas = new Canvas();
            }
            canvas.setBitmap(bitmap);
            attachInfo.mCanvas = null;
        } else {
            canvas = new Canvas(bitmap);
        }
        if ((-16777216 & backgroundColor) != 0) {
            bitmap.eraseColor(backgroundColor);
        }
        computeScroll();
        int restoreCount = canvas.save();
        canvas.scale(scale, scale);
        canvas.translate((float) (-this.mScrollX), (float) (-this.mScrollY));
        int flags = this.mPrivateFlags;
        this.mPrivateFlags &= -6291457;
        if ((this.mPrivateFlags & 128) == 128) {
            dispatchDraw(canvas);
            if (!(this.mOverlay == null || this.mOverlay.isEmpty())) {
                this.mOverlay.getOverlayView().draw(canvas);
            }
        } else {
            draw(canvas);
        }
        this.mPrivateFlags = flags;
        canvas.restoreToCount(restoreCount);
        canvas.setBitmap(null);
        if (attachInfo != null) {
            attachInfo.mCanvas = canvas;
        }
        return bitmap;
    }

    public boolean isInEditMode() {
        return false;
    }

    protected boolean isPaddingOffsetRequired() {
        return false;
    }

    protected int getLeftPaddingOffset() {
        return 0;
    }

    protected int getRightPaddingOffset() {
        return 0;
    }

    protected int getTopPaddingOffset() {
        return 0;
    }

    protected int getBottomPaddingOffset() {
        return 0;
    }

    protected int getFadeTop(boolean offsetRequired) {
        int top = this.mPaddingTop;
        if (offsetRequired) {
            return top + getTopPaddingOffset();
        }
        return top;
    }

    protected int getFadeHeight(boolean offsetRequired) {
        int padding = this.mPaddingTop;
        if (offsetRequired) {
            padding += getTopPaddingOffset();
        }
        return ((this.mBottom - this.mTop) - this.mPaddingBottom) - padding;
    }

    @ExportedProperty(category = "drawing")
    public boolean isHardwareAccelerated() {
        return this.mAttachInfo != null && this.mAttachInfo.mHardwareAccelerated;
    }

    public void setClipBounds(Rect clipBounds) {
        if (clipBounds == this.mClipBounds) {
            return;
        }
        if (clipBounds == null || !clipBounds.equals(this.mClipBounds)) {
            if (clipBounds == null) {
                this.mClipBounds = null;
            } else if (this.mClipBounds == null) {
                this.mClipBounds = new Rect(clipBounds);
            } else {
                this.mClipBounds.set(clipBounds);
            }
            this.mRenderNode.setClipBounds(this.mClipBounds);
            invalidateViewProperty(false, false);
        }
    }

    public Rect getClipBounds() {
        return this.mClipBounds != null ? new Rect(this.mClipBounds) : null;
    }

    public boolean getClipBounds(Rect outRect) {
        if (this.mClipBounds == null) {
            return false;
        }
        outRect.set(this.mClipBounds);
        return true;
    }

    private boolean applyLegacyAnimation(ViewGroup parent, long drawingTime, Animation a, boolean scalingRequired) {
        Transformation invalidationTransform;
        int flags = parent.mGroupFlags;
        if (!a.isInitialized()) {
            a.initialize(this.mRight - this.mLeft, this.mBottom - this.mTop, parent.getWidth(), parent.getHeight());
            a.initializeInvalidateRegion(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
            if (this.mAttachInfo != null) {
                a.setListenerHandler(this.mAttachInfo.mHandler);
            }
            onAnimationStart();
        }
        Transformation t = parent.getChildTransformation();
        boolean more = a.getTransformation(drawingTime, t, 1.0f);
        if (!scalingRequired || this.mAttachInfo.mApplicationScale == 1.0f) {
            invalidationTransform = t;
        } else {
            if (parent.mInvalidationTransformation == null) {
                parent.mInvalidationTransformation = new Transformation();
            }
            invalidationTransform = parent.mInvalidationTransformation;
            a.getTransformation(drawingTime, invalidationTransform, 1.0f);
        }
        if (more) {
            if (a.willChangeBounds()) {
                if (parent.mInvalidateRegion == null) {
                    parent.mInvalidateRegion = new RectF();
                }
                RectF region = parent.mInvalidateRegion;
                a.getInvalidateRegion(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop, region, invalidationTransform);
                parent.mPrivateFlags |= 64;
                int left = this.mLeft + ((int) region.left);
                int top = this.mTop + ((int) region.top);
                parent.invalidate(left, top, ((int) (region.width() + 0.5f)) + left, ((int) (region.height() + 0.5f)) + top);
            } else if ((flags & 144) == 128) {
                parent.mGroupFlags |= 4;
            } else if ((flags & 4) == 0) {
                parent.mPrivateFlags |= 64;
                parent.invalidate(this.mLeft, this.mTop, this.mRight, this.mBottom);
            }
        }
        return more;
    }

    void setDisplayListProperties(RenderNode renderNode) {
        if (renderNode != null) {
            renderNode.setHasOverlappingRendering(hasOverlappingRendering());
            boolean z = (this.mParent instanceof ViewGroup) && ((ViewGroup) this.mParent).getClipChildren();
            renderNode.setClipToBounds(z);
            float alpha = 1.0f;
            if ((this.mParent instanceof ViewGroup) && (((ViewGroup) this.mParent).mGroupFlags & 2048) != 0) {
                ViewGroup parentVG = this.mParent;
                Transformation t = parentVG.getChildTransformation();
                if (parentVG.getChildStaticTransformation(this, t)) {
                    int transformType = t.getTransformationType();
                    if (transformType != 0) {
                        if ((transformType & 1) != 0) {
                            alpha = t.getAlpha();
                        }
                        if ((transformType & 2) != 0) {
                            renderNode.setStaticMatrix(t.getMatrix());
                        }
                    }
                }
            }
            if (this.mTransformationInfo != null) {
                alpha *= getFinalAlpha();
                if (alpha < 1.0f && onSetAlpha((int) (255.0f * alpha))) {
                    alpha = 1.0f;
                }
                renderNode.setAlpha(alpha);
            } else if (alpha < 1.0f) {
                renderNode.setAlpha(alpha);
            }
        }
    }

    boolean draw(Canvas canvas, ViewGroup parent, long drawingTime) {
        float alpha;
        boolean hardwareAcceleratedCanvas = canvas.isHardwareAccelerated();
        boolean drawingWithRenderNode = this.mAttachInfo != null && this.mAttachInfo.mHardwareAccelerated && hardwareAcceleratedCanvas;
        boolean more = false;
        boolean childHasIdentityMatrix = hasIdentityMatrix();
        int parentFlags = parent.mGroupFlags;
        if ((parentFlags & 256) != 0) {
            parent.getChildTransformation().clear();
            parent.mGroupFlags &= -257;
        }
        Transformation transformToApply = null;
        boolean concatMatrix = false;
        boolean scalingRequired = this.mAttachInfo != null && this.mAttachInfo.mScalingRequired;
        Animation a = getAnimation();
        if (a != null) {
            more = applyLegacyAnimation(parent, drawingTime, a, scalingRequired);
            concatMatrix = a.willChangeTransformationMatrix();
            if (concatMatrix) {
                this.mPrivateFlags3 |= 1;
            }
            transformToApply = parent.getChildTransformation();
        } else {
            if ((this.mPrivateFlags3 & 1) != 0) {
                this.mRenderNode.setAnimationMatrix(null);
                this.mPrivateFlags3 &= -2;
            }
            if (!(drawingWithRenderNode || (parentFlags & 2048) == 0)) {
                Transformation t = parent.getChildTransformation();
                if (parent.getChildStaticTransformation(this, t)) {
                    int transformType = t.getTransformationType();
                    transformToApply = transformType != 0 ? t : null;
                    concatMatrix = (transformType & 2) != 0;
                }
            }
        }
        concatMatrix |= !childHasIdentityMatrix ? 1 : 0;
        this.mPrivateFlags |= 32;
        if (!concatMatrix && (parentFlags & 2049) == 1) {
            if (canvas.quickReject((float) this.mLeft, (float) this.mTop, (float) this.mRight, (float) this.mBottom, EdgeType.BW) && (this.mPrivateFlags & 64) == 0) {
                this.mPrivateFlags2 |= 268435456;
                return more;
            }
        }
        this.mPrivateFlags2 &= -268435457;
        if (hardwareAcceleratedCanvas) {
            this.mRecreateDisplayList = (this.mPrivateFlags & Integer.MIN_VALUE) != 0;
            this.mPrivateFlags &= Integer.MAX_VALUE;
        }
        RenderNode renderNode = null;
        Bitmap cache = null;
        int layerType = getLayerType();
        if (layerType == 1 || !(drawingWithRenderNode || layerType == 0)) {
            layerType = 1;
            buildDrawingCache(true);
            cache = getDrawingCache(true);
        }
        if (drawingWithRenderNode) {
            renderNode = updateDisplayListIfDirty();
            if (!renderNode.isValid()) {
                Log.e("ViewSystem", "View #3 displayList is not valid.");
                renderNode = null;
                drawingWithRenderNode = false;
            }
        }
        int sx = 0;
        int sy = 0;
        if (!drawingWithRenderNode) {
            computeScroll();
            sx = this.mScrollX;
            sy = this.mScrollY;
        }
        boolean drawingWithDrawingCache = (cache == null || drawingWithRenderNode) ? false : true;
        boolean offsetForScroll = cache == null && !drawingWithRenderNode;
        int restoreTo = -1;
        if (!(drawingWithRenderNode && transformToApply == null)) {
            restoreTo = canvas.save();
        }
        if (offsetForScroll) {
            canvas.translate((float) (this.mLeft - sx), (float) (this.mTop - sy));
        } else {
            if (!drawingWithRenderNode) {
                canvas.translate((float) this.mLeft, (float) this.mTop);
            }
            if (scalingRequired) {
                if (drawingWithRenderNode) {
                    restoreTo = canvas.save();
                }
                float scale = 1.0f / this.mAttachInfo.mApplicationScale;
                canvas.scale(scale, scale);
            }
        }
        if (drawingWithRenderNode) {
            alpha = 1.0f;
        } else {
            alpha = getAlpha() * getTransitionAlpha();
        }
        if (transformToApply != null || alpha < 1.0f || !hasIdentityMatrix() || (this.mPrivateFlags3 & 2) != 0) {
            if (!(transformToApply == null && childHasIdentityMatrix)) {
                int transX = 0;
                int transY = 0;
                if (offsetForScroll) {
                    transX = -sx;
                    transY = -sy;
                }
                if (transformToApply != null) {
                    if (concatMatrix) {
                        if (drawingWithRenderNode) {
                            renderNode.setAnimationMatrix(transformToApply.getMatrix());
                        } else {
                            canvas.translate((float) (-transX), (float) (-transY));
                            canvas.concat(transformToApply.getMatrix());
                            canvas.translate((float) transX, (float) transY);
                        }
                        parent.mGroupFlags |= 256;
                    }
                    float transformAlpha = transformToApply.getAlpha();
                    if (transformAlpha < 1.0f) {
                        alpha *= transformAlpha;
                        parent.mGroupFlags |= 256;
                    }
                }
                if (!(childHasIdentityMatrix || drawingWithRenderNode)) {
                    canvas.translate((float) (-transX), (float) (-transY));
                    canvas.concat(getMatrix());
                    canvas.translate((float) transX, (float) transY);
                }
            }
            if (alpha < 1.0f || (this.mPrivateFlags3 & 2) != 0) {
                if (alpha < 1.0f) {
                    this.mPrivateFlags3 |= 2;
                } else {
                    this.mPrivateFlags3 &= -3;
                }
                parent.mGroupFlags |= 256;
                if (!drawingWithDrawingCache) {
                    int multipliedAlpha = (int) (255.0f * alpha);
                    if (onSetAlpha(multipliedAlpha)) {
                        this.mPrivateFlags |= 262144;
                    } else if (drawingWithRenderNode) {
                        renderNode.setAlpha((getAlpha() * alpha) * getTransitionAlpha());
                    } else if (layerType == 0) {
                        canvas.saveLayerAlpha((float) sx, (float) sy, (float) (getWidth() + sx), (float) (getHeight() + sy), multipliedAlpha);
                    }
                }
            }
        } else if ((this.mPrivateFlags & 262144) == 262144) {
            onSetAlpha(255);
            this.mPrivateFlags &= -262145;
        }
        if (!drawingWithRenderNode) {
            if ((parentFlags & 1) != 0 && cache == null) {
                if (offsetForScroll) {
                    canvas.clipRect(sx, sy, getWidth() + sx, getHeight() + sy);
                } else if (!scalingRequired || cache == null) {
                    canvas.clipRect(0, 0, getWidth(), getHeight());
                } else {
                    canvas.clipRect(0, 0, cache.getWidth(), cache.getHeight());
                }
            }
            if (this.mClipBounds != null) {
                canvas.clipRect(this.mClipBounds);
            }
        }
        if (drawingWithDrawingCache) {
            if (cache != null) {
                this.mPrivateFlags &= -6291457;
                if (layerType == 0) {
                    Paint cachePaint = parent.mCachePaint;
                    if (cachePaint == null) {
                        cachePaint = new Paint();
                        cachePaint.setDither(false);
                        parent.mCachePaint = cachePaint;
                    }
                    cachePaint.setAlpha((int) (255.0f * alpha));
                    canvas.drawBitmap(cache, 0.0f, 0.0f, cachePaint);
                } else {
                    int layerPaintAlpha = this.mLayerPaint.getAlpha();
                    this.mLayerPaint.setAlpha((int) (((float) layerPaintAlpha) * alpha));
                    canvas.drawBitmap(cache, 0.0f, 0.0f, this.mLayerPaint);
                    this.mLayerPaint.setAlpha(layerPaintAlpha);
                }
            }
        } else if (drawingWithRenderNode) {
            this.mPrivateFlags &= -6291457;
            ((DisplayListCanvas) canvas).drawRenderNode(renderNode);
        } else if ((this.mPrivateFlags & 128) == 128) {
            this.mPrivateFlags &= -6291457;
            dispatchDraw(canvas);
        } else {
            draw(canvas);
        }
        if (restoreTo >= 0) {
            canvas.restoreToCount(restoreTo);
        }
        if (!(a == null || more)) {
            if (!(hardwareAcceleratedCanvas || a.getFillAfter())) {
                onSetAlpha(255);
            }
            parent.finishAnimatingView(this, a);
        }
        if (more && hardwareAcceleratedCanvas && a.hasAlpha() && (this.mPrivateFlags & 262144) == 262144) {
            invalidate(true);
        }
        this.mRecreateDisplayList = false;
        return more;
    }

    public void draw(Canvas canvas) {
        int privateFlags = this.mPrivateFlags;
        boolean dirtyOpaque = (PFLAG_DIRTY_MASK & privateFlags) == 4194304 && (this.mAttachInfo == null || !this.mAttachInfo.mIgnoreDirtyState);
        this.mPrivateFlags = (-6291457 & privateFlags) | 32;
        if (!dirtyOpaque) {
            drawBackground(canvas);
        }
        int viewFlags = this.mViewFlags;
        boolean horizontalEdges = (viewFlags & 4096) != 0;
        boolean verticalEdges = (viewFlags & 8192) != 0;
        if (verticalEdges || horizontalEdges) {
            boolean drawTop = false;
            boolean drawBottom = false;
            boolean drawLeft = false;
            boolean drawRight = false;
            float topFadeStrength = 0.0f;
            float bottomFadeStrength = 0.0f;
            float leftFadeStrength = 0.0f;
            float rightFadeStrength = 0.0f;
            int paddingLeft = this.mPaddingLeft;
            boolean offsetRequired = isPaddingOffsetRequired();
            if (offsetRequired) {
                paddingLeft += getLeftPaddingOffset();
            }
            int left = this.mScrollX + paddingLeft;
            int right = (((this.mRight + left) - this.mLeft) - this.mPaddingRight) - paddingLeft;
            int top = this.mScrollY + getFadeTop(offsetRequired);
            int bottom = top + getFadeHeight(offsetRequired);
            if (offsetRequired) {
                right += getRightPaddingOffset();
                bottom += getBottomPaddingOffset();
            }
            ScrollabilityCache scrollabilityCache = this.mScrollCache;
            float fadeHeight = (float) scrollabilityCache.fadingEdgeLength;
            int length = (int) fadeHeight;
            if (verticalEdges && top + length > bottom - length) {
                length = (bottom - top) / 2;
            }
            if (horizontalEdges && left + length > right - length) {
                length = (right - left) / 2;
            }
            if (verticalEdges) {
                topFadeStrength = Math.max(0.0f, Math.min(1.0f, getTopFadingEdgeStrength()));
                drawTop = topFadeStrength * fadeHeight > 1.0f;
                bottomFadeStrength = Math.max(0.0f, Math.min(1.0f, getBottomFadingEdgeStrength()));
                drawBottom = bottomFadeStrength * fadeHeight > 1.0f;
            }
            if (horizontalEdges) {
                leftFadeStrength = Math.max(0.0f, Math.min(1.0f, getLeftFadingEdgeStrength()));
                drawLeft = leftFadeStrength * fadeHeight > 1.0f;
                rightFadeStrength = Math.max(0.0f, Math.min(1.0f, getRightFadingEdgeStrength()));
                drawRight = rightFadeStrength * fadeHeight > 1.0f;
            }
            int saveCount = canvas.getSaveCount();
            int solidColor = getSolidColor();
            if (solidColor == 0) {
                if (drawTop) {
                    canvas.saveLayer((float) left, (float) top, (float) right, (float) (top + length), null, 4);
                }
                if (drawBottom) {
                    canvas.saveLayer((float) left, (float) (bottom - length), (float) right, (float) bottom, null, 4);
                }
                if (drawLeft) {
                    canvas.saveLayer((float) left, (float) top, (float) (left + length), (float) bottom, null, 4);
                }
                if (drawRight) {
                    canvas.saveLayer((float) (right - length), (float) top, (float) right, (float) bottom, null, 4);
                }
            } else {
                scrollabilityCache.setFadeColor(solidColor);
            }
            if (!dirtyOpaque) {
                onDraw(canvas);
            }
            dispatchDraw(canvas);
            Paint p = scrollabilityCache.paint;
            Matrix matrix = scrollabilityCache.matrix;
            Shader fade = scrollabilityCache.shader;
            if (drawTop) {
                matrix.setScale(1.0f, fadeHeight * topFadeStrength);
                matrix.postTranslate((float) left, (float) top);
                fade.setLocalMatrix(matrix);
                p.setShader(fade);
                canvas.drawRect((float) left, (float) top, (float) right, (float) (top + length), p);
            }
            if (drawBottom) {
                matrix.setScale(1.0f, fadeHeight * bottomFadeStrength);
                matrix.postRotate(180.0f);
                matrix.postTranslate((float) left, (float) bottom);
                fade.setLocalMatrix(matrix);
                p.setShader(fade);
                canvas.drawRect((float) left, (float) (bottom - length), (float) right, (float) bottom, p);
            }
            if (drawLeft) {
                matrix.setScale(1.0f, fadeHeight * leftFadeStrength);
                matrix.postRotate(-90.0f);
                matrix.postTranslate((float) left, (float) top);
                fade.setLocalMatrix(matrix);
                p.setShader(fade);
                canvas.drawRect((float) left, (float) top, (float) (left + length), (float) bottom, p);
            }
            if (drawRight) {
                matrix.setScale(1.0f, fadeHeight * rightFadeStrength);
                matrix.postRotate(90.0f);
                matrix.postTranslate((float) right, (float) top);
                fade.setLocalMatrix(matrix);
                p.setShader(fade);
                canvas.drawRect((float) (right - length), (float) top, (float) right, (float) bottom, p);
            }
            canvas.restoreToCount(saveCount);
            if (!(this.mOverlay == null || this.mOverlay.isEmpty())) {
                this.mOverlay.getOverlayView().dispatchDraw(canvas);
            }
            onDrawForeground(canvas);
            return;
        }
        if (!dirtyOpaque) {
            onDraw(canvas);
        }
        dispatchDraw(canvas);
        if (!(this.mOverlay == null || this.mOverlay.isEmpty())) {
            this.mOverlay.getOverlayView().dispatchDraw(canvas);
        }
        onDrawForeground(canvas);
    }

    protected void twUpdateBackgroundBounds() {
        this.mBackground.setBounds(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
    }

    private void drawBackground(Canvas canvas) {
        Drawable background = this.mBackground;
        if (background != null) {
            setBackgroundBounds();
            if (!(!canvas.isHardwareAccelerated() || this.mAttachInfo == null || this.mAttachInfo.mHardwareRenderer == null)) {
                this.mBackgroundRenderNode = getDrawableRenderNode(background, this.mBackgroundRenderNode);
                RenderNode renderNode = this.mBackgroundRenderNode;
                if (renderNode != null && renderNode.isValid()) {
                    setBackgroundRenderNodeProperties(renderNode);
                    ((DisplayListCanvas) canvas).drawRenderNode(renderNode);
                    return;
                }
            }
            if (getViewRootImpl() != null && ViewRootImpl.sIsDcsEnabledApp && ViewRootImpl.sDcsFormat == 4) {
                Drawable.DCS_FLAG_APPLY_SHADOW = true;
            }
            int scrollX = this.mScrollX;
            int scrollY = this.mScrollY;
            if ((scrollX | scrollY) == 0) {
                background.draw(canvas);
                return;
            }
            canvas.translate((float) scrollX, (float) scrollY);
            background.draw(canvas);
            canvas.translate((float) (-scrollX), (float) (-scrollY));
        }
    }

    void setBackgroundBounds() {
        if (this.mBackgroundSizeChanged && this.mBackground != null) {
            this.mBackground.setBounds(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
            this.mBackgroundSizeChanged = false;
            rebuildOutline();
        }
    }

    private void setBackgroundRenderNodeProperties(RenderNode renderNode) {
        renderNode.setTranslationX((float) this.mScrollX);
        renderNode.setTranslationY((float) this.mScrollY);
    }

    private RenderNode getDrawableRenderNode(Drawable drawable, RenderNode renderNode) {
        if (renderNode == null) {
            renderNode = RenderNode.create(drawable.getClass().getName(), this);
        }
        Rect bounds = drawable.getBounds();
        DisplayListCanvas canvas = renderNode.start(bounds.width(), bounds.height());
        canvas.translate((float) (-bounds.left), (float) (-bounds.top));
        try {
            drawable.draw(canvas);
            renderNode.setLeftTopRightBottom(bounds.left, bounds.top, bounds.right, bounds.bottom);
            renderNode.setProjectBackwards(drawable.isProjected());
            renderNode.setProjectionReceiver(true);
            renderNode.setClipToBounds(false);
            return renderNode;
        } finally {
            renderNode.end(canvas);
        }
    }

    public ViewOverlay getOverlay() {
        if (this.mOverlay == null) {
            this.mOverlay = new ViewOverlay(this.mContext, this);
        }
        return this.mOverlay;
    }

    @ExportedProperty(category = "drawing")
    public int getSolidColor() {
        return 0;
    }

    private static String printFlags(int flags) {
        String output = "";
        int numFlags = 0;
        if ((flags & 1) == 1) {
            output = output + "TAKES_FOCUS";
            numFlags = 0 + 1;
        }
        switch (flags & 12) {
            case 4:
                if (numFlags > 0) {
                    output = output + " ";
                }
                return output + "INVISIBLE";
            case 8:
                if (numFlags > 0) {
                    output = output + " ";
                }
                return output + "GONE";
            default:
                return output;
        }
    }

    private static String printPrivateFlags(int privateFlags) {
        String output = "";
        int numFlags = 0;
        if ((privateFlags & 1) == 1) {
            output = output + "WANTS_FOCUS";
            numFlags = 0 + 1;
        }
        if ((privateFlags & 2) == 2) {
            if (numFlags > 0) {
                output = output + " ";
            }
            output = output + "FOCUSED";
            numFlags++;
        }
        if ((privateFlags & 4) == 4) {
            if (numFlags > 0) {
                output = output + " ";
            }
            output = output + "SELECTED";
            numFlags++;
        }
        if ((privateFlags & 8) == 8) {
            if (numFlags > 0) {
                output = output + " ";
            }
            output = output + "IS_ROOT_NAMESPACE";
            numFlags++;
        }
        if ((privateFlags & 16) == 16) {
            if (numFlags > 0) {
                output = output + " ";
            }
            output = output + "HAS_BOUNDS";
            numFlags++;
        }
        if ((privateFlags & 32) != 32) {
            return output;
        }
        if (numFlags > 0) {
            output = output + " ";
        }
        return output + "DRAWN";
    }

    public boolean isLayoutRequested() {
        return (this.mPrivateFlags & 4096) == 4096;
    }

    public static boolean isLayoutModeOptical(Object o) {
        return (o instanceof ViewGroup) && ((ViewGroup) o).isLayoutModeOptical();
    }

    private boolean setOpticalFrame(int left, int top, int right, int bottom) {
        Insets parentInsets = this.mParent instanceof View ? ((View) this.mParent).getOpticalInsets() : Insets.NONE;
        Insets childInsets = getOpticalInsets();
        return setFrame((parentInsets.left + left) - childInsets.left, (parentInsets.top + top) - childInsets.top, (parentInsets.left + right) + childInsets.right, (parentInsets.top + bottom) + childInsets.bottom);
    }

    public void layout(int l, int t, int r, int b) {
        if ((this.mPrivateFlags3 & 8) != 0) {
            onMeasure(this.mOldWidthMeasureSpec, this.mOldHeightMeasureSpec);
            this.mPrivateFlags3 &= -9;
        }
        int oldL = this.mLeft;
        int oldT = this.mTop;
        int oldB = this.mBottom;
        int oldR = this.mRight;
        boolean changed = isLayoutModeOptical(this.mParent) ? setOpticalFrame(l, t, r, b) : setFrame(l, t, r, b);
        if (changed || (this.mPrivateFlags & 8192) == 8192) {
            onLayout(changed, l, t, r, b);
            this.mPrivateFlags &= -8193;
            ListenerInfo li = this.mListenerInfo;
            if (!(li == null || li.mOnLayoutChangeListeners == null)) {
                ArrayList<OnLayoutChangeListener> listenersCopy = (ArrayList) li.mOnLayoutChangeListeners.clone();
                int numListeners = listenersCopy.size();
                for (int i = 0; i < numListeners; i++) {
                    ((OnLayoutChangeListener) listenersCopy.get(i)).onLayoutChange(this, l, t, r, b, oldL, oldT, oldR, oldB);
                }
            }
        }
        this.mPrivateFlags &= -4097;
        this.mPrivateFlags3 |= 4;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    protected boolean setFrame(int left, int top, int right, int bottom) {
        boolean changed = false;
        if (!(this.mLeft == left && this.mRight == right && this.mTop == top && this.mBottom == bottom)) {
            changed = true;
            int drawn = this.mPrivateFlags & 32;
            int oldWidth = this.mRight - this.mLeft;
            int oldHeight = this.mBottom - this.mTop;
            int newWidth = right - left;
            int newHeight = bottom - top;
            boolean sizeChanged = (newWidth == oldWidth && newHeight == oldHeight) ? false : true;
            invalidate(sizeChanged);
            this.mLeft = left;
            this.mTop = top;
            this.mRight = right;
            this.mBottom = bottom;
            this.mRenderNode.setLeftTopRightBottom(this.mLeft, this.mTop, this.mRight, this.mBottom);
            this.mPrivateFlags |= 16;
            if (sizeChanged) {
                sizeChange(newWidth, newHeight, oldWidth, oldHeight);
            }
            if ((this.mViewFlags & 12) == 0 || this.mGhostView != null) {
                this.mPrivateFlags |= 32;
                invalidate(sizeChanged);
                invalidateParentCaches();
            }
            this.mPrivateFlags |= drawn;
            this.mBackgroundSizeChanged = true;
            if (this.mForegroundInfo != null) {
                this.mForegroundInfo.mBoundsChanged = true;
            }
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
        return changed;
    }

    public void setLeftTopRightBottom(int left, int top, int right, int bottom) {
        setFrame(left, top, right, bottom);
    }

    private void sizeChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        if (this.mOverlay != null) {
            this.mOverlay.getOverlayView().setRight(newWidth);
            this.mOverlay.getOverlayView().setBottom(newHeight);
        }
        rebuildOutline();
    }

    protected void onFinishInflate() {
    }

    public Resources getResources() {
        return this.mResources;
    }

    public void invalidateDrawable(Drawable drawable) {
        if (verifyDrawable(drawable)) {
            Rect dirty = drawable.getDirtyBounds();
            int scrollX = this.mScrollX;
            int scrollY = this.mScrollY;
            invalidate(dirty.left + scrollX, dirty.top + scrollY, dirty.right + scrollX, dirty.bottom + scrollY);
            rebuildOutline();
        }
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (verifyDrawable(who) && what != null) {
            long delay = when - SystemClock.uptimeMillis();
            if (this.mAttachInfo != null) {
                this.mAttachInfo.mViewRootImpl.mChoreographer.postCallbackDelayed(1, what, who, Choreographer.subtractFrameDelay(delay));
                return;
            }
            ViewRootImpl.getRunQueue().postDelayed(what, delay);
        }
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (verifyDrawable(who) && what != null) {
            if (this.mAttachInfo != null) {
                this.mAttachInfo.mViewRootImpl.mChoreographer.removeCallbacks(1, what, who);
            }
            ViewRootImpl.getRunQueue().removeCallbacks(what);
        }
    }

    public void unscheduleDrawable(Drawable who) {
        if (this.mAttachInfo != null && who != null) {
            this.mAttachInfo.mViewRootImpl.mChoreographer.removeCallbacks(1, null, who);
        }
    }

    protected void resolveDrawables() {
        if (isLayoutDirectionResolved() || getRawLayoutDirection() != 2) {
            int layoutDirection = isLayoutDirectionResolved() ? getLayoutDirection() : getRawLayoutDirection();
            if (this.mBackground != null) {
                this.mBackground.setLayoutDirection(layoutDirection);
            }
            if (!(this.mForegroundInfo == null || this.mForegroundInfo.mDrawable == null)) {
                this.mForegroundInfo.mDrawable.setLayoutDirection(layoutDirection);
            }
            this.mPrivateFlags2 |= 1073741824;
            onResolveDrawables(layoutDirection);
        }
    }

    boolean areDrawablesResolved() {
        return (this.mPrivateFlags2 & 1073741824) == 1073741824;
    }

    public void onResolveDrawables(int layoutDirection) {
    }

    protected void resetResolvedDrawables() {
        resetResolvedDrawablesInternal();
    }

    void resetResolvedDrawablesInternal() {
        this.mPrivateFlags2 &= -1073741825;
    }

    protected boolean verifyDrawable(Drawable who) {
        return who == this.mBackground || (this.mForegroundInfo != null && this.mForegroundInfo.mDrawable == who);
    }

    protected void drawableStateChanged() {
        int[] state = getDrawableState();
        boolean changed = false;
        Drawable bg = this.mBackground;
        if (bg != null && bg.isStateful()) {
            changed = false | bg.setState(state);
        }
        Drawable fg = this.mForegroundInfo != null ? this.mForegroundInfo.mDrawable : null;
        if (fg != null && fg.isStateful()) {
            changed |= fg.setState(state);
        }
        if (this.mScrollCache != null) {
            Drawable scrollBar = this.mScrollCache.scrollBar;
            if (scrollBar != null && scrollBar.isStateful()) {
                int i = (!scrollBar.setState(state) || this.mScrollCache.state == 0) ? 0 : 1;
                changed |= i;
            }
        }
        if (this.mStateListAnimator != null) {
            this.mStateListAnimator.setState(state);
        }
        if (changed) {
            invalidate();
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        if (this.mBackground != null) {
            this.mBackground.setHotspot(x, y);
        }
        if (!(this.mForegroundInfo == null || this.mForegroundInfo.mDrawable == null)) {
            this.mForegroundInfo.mDrawable.setHotspot(x, y);
        }
        dispatchDrawableHotspotChanged(x, y);
    }

    public void dispatchDrawableHotspotChanged(float x, float y) {
    }

    public void refreshDrawableState() {
        this.mPrivateFlags |= 1024;
        drawableStateChanged();
        ViewParent parent = this.mParent;
        if (parent != null) {
            parent.childDrawableStateChanged(this);
        }
    }

    public final int[] getDrawableState() {
        if (this.mDrawableState != null && (this.mPrivateFlags & 1024) == 0) {
            return this.mDrawableState;
        }
        this.mDrawableState = onCreateDrawableState(0);
        this.mPrivateFlags &= -1025;
        return this.mDrawableState;
    }

    protected int[] onCreateDrawableState(int extraSpace) {
        if ((this.mViewFlags & 4194304) == 4194304 && (this.mParent instanceof View)) {
            return ((View) this.mParent).onCreateDrawableState(extraSpace);
        }
        int privateFlags = this.mPrivateFlags;
        int viewStateIndex = 0;
        if ((privateFlags & 16384) != 0) {
            viewStateIndex = 0 | 16;
        }
        if ((this.mViewFlags & 32) == 0) {
            viewStateIndex |= 8;
        }
        if (isFocused()) {
            viewStateIndex |= 4;
        }
        if ((privateFlags & 4) != 0) {
            viewStateIndex |= 2;
        }
        if (hasWindowFocus()) {
            viewStateIndex |= 1;
        }
        if ((1073741824 & privateFlags) != 0) {
            viewStateIndex |= 32;
        }
        if (this.mAttachInfo != null && this.mAttachInfo.mHardwareAccelerationRequested && HardwareRenderer.isAvailable()) {
            viewStateIndex |= 64;
        }
        if ((268435456 & privateFlags) != 0) {
            viewStateIndex |= 128;
        }
        int privateFlags2 = this.mPrivateFlags2;
        if ((privateFlags2 & 1) != 0) {
            viewStateIndex |= 256;
        }
        if ((privateFlags2 & 2) != 0) {
            viewStateIndex |= 512;
        }
        if ((this.mTouchwizFlags & 1) != 0) {
            viewStateIndex |= 1024;
        }
        int[] drawableState = StateSet.get(viewStateIndex);
        if (extraSpace == 0) {
            return drawableState;
        }
        int[] fullState;
        if (drawableState != null) {
            fullState = new int[(drawableState.length + extraSpace)];
            System.arraycopy(drawableState, 0, fullState, 0, drawableState.length);
        } else {
            fullState = new int[extraSpace];
        }
        return fullState;
    }

    protected static int[] mergeDrawableStates(int[] baseState, int[] additionalState) {
        int i = baseState.length - 1;
        while (i >= 0 && baseState[i] == 0) {
            i--;
        }
        System.arraycopy(additionalState, 0, baseState, i + 1, additionalState.length);
        return baseState;
    }

    public void jumpDrawablesToCurrentState() {
        if (this.mBackground != null) {
            this.mBackground.jumpToCurrentState();
        }
        if (this.mStateListAnimator != null) {
            this.mStateListAnimator.jumpToCurrentState();
        }
        if (this.mForegroundInfo != null && this.mForegroundInfo.mDrawable != null) {
            this.mForegroundInfo.mDrawable.jumpToCurrentState();
        }
    }

    @RemotableViewMethod
    public void setBackgroundColor(int color) {
        if (this.mBackground instanceof ColorDrawable) {
            ((ColorDrawable) this.mBackground.mutate()).setColor(color);
            computeOpaqueFlags();
            this.mBackgroundResource = 0;
            return;
        }
        setBackground(new ColorDrawable(color));
    }

    @RemotableViewMethod
    public void setBackgroundResource(int resid) {
        if (resid == 0 || resid != this.mBackgroundResource) {
            Drawable d = null;
            if (resid != 0) {
                d = this.mContext.getDrawable(resid);
            }
            setBackground(d);
            this.mBackgroundResource = resid;
        }
    }

    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    @Deprecated
    public void setBackgroundDrawable(Drawable background) {
        computeOpaqueFlags();
        if (background != this.mBackground) {
            boolean requestLayout = false;
            this.mBackgroundResource = 0;
            if (this.mBackground != null) {
                this.mBackground.setCallback(null);
                unscheduleDrawable(this.mBackground);
            }
            if (background != null) {
                Rect padding = (Rect) sThreadLocal.get();
                if (padding == null) {
                    padding = new Rect();
                    sThreadLocal.set(padding);
                }
                resetResolvedDrawablesInternal();
                background.setLayoutDirection(getLayoutDirection());
                if (background.getPadding(padding)) {
                    resetResolvedPaddingInternal();
                    switch (background.getLayoutDirection()) {
                        case 1:
                            this.mUserPaddingLeftInitial = padding.right;
                            this.mUserPaddingRightInitial = padding.left;
                            internalSetPadding(padding.right, padding.top, padding.left, padding.bottom);
                            break;
                        default:
                            this.mUserPaddingLeftInitial = padding.left;
                            this.mUserPaddingRightInitial = padding.right;
                            internalSetPadding(padding.left, padding.top, padding.right, padding.bottom);
                            break;
                    }
                    this.mLeftPaddingDefined = false;
                    this.mRightPaddingDefined = false;
                }
                if (!(this.mBackground != null && this.mBackground.getMinimumHeight() == background.getMinimumHeight() && this.mBackground.getMinimumWidth() == background.getMinimumWidth())) {
                    requestLayout = true;
                }
                background.setCallback(this);
                if (background.isStateful()) {
                    background.setState(getDrawableState());
                }
                background.setVisible(getVisibility() == 0, false);
                this.mBackground = background;
                if (Build.IS_SYSTEM_SECURE && this.mBackground.isImagePath()) {
                    this.mBackgroundPath = this.mBackground.getImagePath();
                }
                applyBackgroundTint();
                if ((this.mPrivateFlags & 128) != 0) {
                    this.mPrivateFlags &= -129;
                    requestLayout = true;
                }
            } else {
                this.mBackground = null;
                if ((this.mViewFlags & 128) != 0 && (this.mForegroundInfo == null || this.mForegroundInfo.mDrawable == null)) {
                    this.mPrivateFlags |= 128;
                }
                requestLayout = true;
            }
            computeOpaqueFlags();
            if (requestLayout) {
                requestLayout();
            }
            this.mBackgroundSizeChanged = true;
            invalidate(true);
        }
    }

    public Drawable getBackground() {
        return this.mBackground;
    }

    public void setBackgroundTintList(ColorStateList tint) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        this.mBackgroundTint.mTintList = tint;
        this.mBackgroundTint.mHasTintList = true;
        applyBackgroundTint();
    }

    public ColorStateList getBackgroundTintList() {
        return this.mBackgroundTint != null ? this.mBackgroundTint.mTintList : null;
    }

    public void setBackgroundTintMode(Mode tintMode) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        this.mBackgroundTint.mTintMode = tintMode;
        this.mBackgroundTint.mHasTintMode = true;
        applyBackgroundTint();
    }

    public Mode getBackgroundTintMode() {
        return this.mBackgroundTint != null ? this.mBackgroundTint.mTintMode : null;
    }

    private void applyBackgroundTint() {
        if (this.mBackground != null && this.mBackgroundTint != null) {
            TintInfo tintInfo = this.mBackgroundTint;
            if (tintInfo.mHasTintList || tintInfo.mHasTintMode) {
                this.mBackground = this.mBackground.mutate();
                if (tintInfo.mHasTintList) {
                    this.mBackground.setTintList(tintInfo.mTintList);
                }
                if (tintInfo.mHasTintMode) {
                    this.mBackground.setTintMode(tintInfo.mTintMode);
                }
                if (this.mBackground.isStateful()) {
                    this.mBackground.setState(getDrawableState());
                }
            }
        }
    }

    public Drawable getForeground() {
        return this.mForegroundInfo != null ? this.mForegroundInfo.mDrawable : null;
    }

    public void setForeground(Drawable foreground) {
        if (this.mForegroundInfo == null) {
            if (foreground != null) {
                this.mForegroundInfo = new ForegroundInfo();
            } else {
                return;
            }
        }
        if (foreground != this.mForegroundInfo.mDrawable) {
            if (this.mForegroundInfo.mDrawable != null) {
                this.mForegroundInfo.mDrawable.setCallback(null);
                unscheduleDrawable(this.mForegroundInfo.mDrawable);
            }
            this.mForegroundInfo.mDrawable = foreground;
            this.mForegroundInfo.mBoundsChanged = true;
            if (foreground != null) {
                if ((this.mPrivateFlags & 128) != 0) {
                    this.mPrivateFlags &= -129;
                }
                foreground.setCallback(this);
                foreground.setLayoutDirection(getLayoutDirection());
                if (foreground.isStateful()) {
                    foreground.setState(getDrawableState());
                }
                applyForegroundTint();
            } else if ((this.mViewFlags & 128) != 0 && this.mBackground == null) {
                this.mPrivateFlags |= 128;
            }
            requestLayout();
            invalidate();
        }
    }

    public boolean isForegroundInsidePadding() {
        return this.mForegroundInfo != null ? this.mForegroundInfo.mInsidePadding : true;
    }

    public int getForegroundGravity() {
        return this.mForegroundInfo != null ? this.mForegroundInfo.mGravity : 8388659;
    }

    public void setForegroundGravity(int gravity) {
        if (this.mForegroundInfo == null) {
            this.mForegroundInfo = new ForegroundInfo();
        }
        if (this.mForegroundInfo.mGravity != gravity) {
            if ((Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK & gravity) == 0) {
                gravity |= Gravity.START;
            }
            if ((gravity & 112) == 0) {
                gravity |= 48;
            }
            this.mForegroundInfo.mGravity = gravity;
            requestLayout();
        }
    }

    public void setForegroundTintList(ColorStateList tint) {
        if (this.mForegroundInfo == null) {
            this.mForegroundInfo = new ForegroundInfo();
        }
        if (this.mForegroundInfo.mTintInfo == null) {
            this.mForegroundInfo.mTintInfo = new TintInfo();
        }
        this.mForegroundInfo.mTintInfo.mTintList = tint;
        this.mForegroundInfo.mTintInfo.mHasTintList = true;
        applyForegroundTint();
    }

    public ColorStateList getForegroundTintList() {
        return (this.mForegroundInfo == null || this.mForegroundInfo.mTintInfo == null) ? null : this.mForegroundInfo.mTintInfo.mTintList;
    }

    public void setForegroundTintMode(Mode tintMode) {
        if (this.mForegroundInfo == null) {
            this.mForegroundInfo = new ForegroundInfo();
        }
        if (this.mForegroundInfo.mTintInfo == null) {
            this.mForegroundInfo.mTintInfo = new TintInfo();
        }
        this.mForegroundInfo.mTintInfo.mTintMode = tintMode;
        this.mForegroundInfo.mTintInfo.mHasTintMode = true;
        applyForegroundTint();
    }

    public Mode getForegroundTintMode() {
        return (this.mForegroundInfo == null || this.mForegroundInfo.mTintInfo == null) ? null : this.mForegroundInfo.mTintInfo.mTintMode;
    }

    private void applyForegroundTint() {
        if (this.mForegroundInfo != null && this.mForegroundInfo.mDrawable != null && this.mForegroundInfo.mTintInfo != null) {
            TintInfo tintInfo = this.mForegroundInfo.mTintInfo;
            if (tintInfo.mHasTintList || tintInfo.mHasTintMode) {
                this.mForegroundInfo.mDrawable = this.mForegroundInfo.mDrawable.mutate();
                if (tintInfo.mHasTintList) {
                    this.mForegroundInfo.mDrawable.setTintList(tintInfo.mTintList);
                }
                if (tintInfo.mHasTintMode) {
                    this.mForegroundInfo.mDrawable.setTintMode(tintInfo.mTintMode);
                }
                if (this.mForegroundInfo.mDrawable.isStateful()) {
                    this.mForegroundInfo.mDrawable.setState(getDrawableState());
                }
            }
        }
    }

    public void onDrawForeground(Canvas canvas) {
        onDrawScrollIndicators(canvas);
        onDrawScrollBars(canvas);
        Drawable foreground = this.mForegroundInfo != null ? this.mForegroundInfo.mDrawable : null;
        if (foreground != null) {
            if (this.mForegroundInfo.mBoundsChanged) {
                this.mForegroundInfo.mBoundsChanged = false;
                Rect selfBounds = this.mForegroundInfo.mSelfBounds;
                Rect overlayBounds = this.mForegroundInfo.mOverlayBounds;
                if (this.mForegroundInfo.mInsidePadding) {
                    selfBounds.set(0, 0, getWidth(), getHeight());
                } else {
                    selfBounds.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
                }
                Gravity.apply(this.mForegroundInfo.mGravity, foreground.getIntrinsicWidth(), foreground.getIntrinsicHeight(), selfBounds, overlayBounds, getLayoutDirection());
                foreground.setBounds(overlayBounds);
            }
            foreground.draw(canvas);
        }
    }

    public void setPadding(int left, int top, int right, int bottom) {
        resetResolvedPaddingInternal();
        this.mUserPaddingStart = Integer.MIN_VALUE;
        this.mUserPaddingEnd = Integer.MIN_VALUE;
        this.mUserPaddingLeftInitial = left;
        this.mUserPaddingRightInitial = right;
        this.mLeftPaddingDefined = true;
        this.mRightPaddingDefined = true;
        internalSetPadding(left, top, right, bottom);
    }

    protected void internalSetPadding(int left, int top, int right, int bottom) {
        int i = 0;
        this.mUserPaddingLeft = left;
        this.mUserPaddingRight = right;
        this.mUserPaddingBottom = bottom;
        int viewFlags = this.mViewFlags;
        boolean changed = false;
        if ((viewFlags & 768) != 0) {
            if ((viewFlags & 512) != 0) {
                int offset = (viewFlags & 16777216) == 0 ? 0 : getVerticalScrollbarWidth();
                switch (this.mVerticalScrollbarPosition) {
                    case 0:
                        if (!isLayoutRtl()) {
                            right += offset;
                            break;
                        } else {
                            left += offset;
                            break;
                        }
                    case 1:
                        left += offset;
                        break;
                    case 2:
                        right += offset;
                        break;
                }
            }
            if ((viewFlags & 256) != 0) {
                if ((viewFlags & 16777216) != 0) {
                    i = getHorizontalScrollbarHeight();
                }
                bottom += i;
            }
        }
        if (this.mPaddingLeft != left) {
            changed = true;
            this.mPaddingLeft = left;
        }
        if (this.mPaddingTop != top) {
            changed = true;
            this.mPaddingTop = top;
        }
        if (this.mPaddingRight != right) {
            changed = true;
            this.mPaddingRight = right;
        }
        if (this.mPaddingBottom != bottom) {
            changed = true;
            this.mPaddingBottom = bottom;
        }
        if (changed) {
            requestLayout();
            invalidateOutline();
        }
    }

    public void setPaddingRelative(int start, int top, int end, int bottom) {
        resetResolvedPaddingInternal();
        this.mUserPaddingStart = start;
        this.mUserPaddingEnd = end;
        this.mLeftPaddingDefined = true;
        this.mRightPaddingDefined = true;
        switch (getLayoutDirection()) {
            case 1:
                this.mUserPaddingLeftInitial = end;
                this.mUserPaddingRightInitial = start;
                internalSetPadding(end, top, start, bottom);
                return;
            default:
                this.mUserPaddingLeftInitial = start;
                this.mUserPaddingRightInitial = end;
                internalSetPadding(start, top, end, bottom);
                return;
        }
    }

    public int getPaddingTop() {
        return this.mPaddingTop;
    }

    public int getPaddingBottom() {
        return this.mPaddingBottom;
    }

    public int getPaddingLeft() {
        if (!isPaddingResolved()) {
            resolvePadding();
        }
        return this.mPaddingLeft;
    }

    public int getPaddingStart() {
        if (!isPaddingResolved()) {
            resolvePadding();
        }
        return getLayoutDirection() == 1 ? this.mPaddingRight : this.mPaddingLeft;
    }

    public int getPaddingRight() {
        if (!isPaddingResolved()) {
            resolvePadding();
        }
        return this.mPaddingRight;
    }

    public int getPaddingEnd() {
        if (!isPaddingResolved()) {
            resolvePadding();
        }
        return getLayoutDirection() == 1 ? this.mPaddingLeft : this.mPaddingRight;
    }

    public boolean isPaddingRelative() {
        return (this.mUserPaddingStart == Integer.MIN_VALUE && this.mUserPaddingEnd == Integer.MIN_VALUE) ? false : true;
    }

    Insets computeOpticalInsets() {
        return this.mBackground == null ? Insets.NONE : this.mBackground.getOpticalInsets();
    }

    public void resetPaddingToInitialValues() {
        if (isRtlCompatibilityMode()) {
            this.mPaddingLeft = this.mUserPaddingLeftInitial;
            this.mPaddingRight = this.mUserPaddingRightInitial;
        } else if (isLayoutRtl()) {
            this.mPaddingLeft = this.mUserPaddingEnd >= 0 ? this.mUserPaddingEnd : this.mUserPaddingLeftInitial;
            this.mPaddingRight = this.mUserPaddingStart >= 0 ? this.mUserPaddingStart : this.mUserPaddingRightInitial;
        } else {
            this.mPaddingLeft = this.mUserPaddingStart >= 0 ? this.mUserPaddingStart : this.mUserPaddingLeftInitial;
            this.mPaddingRight = this.mUserPaddingEnd >= 0 ? this.mUserPaddingEnd : this.mUserPaddingRightInitial;
        }
    }

    public Insets getOpticalInsets() {
        if (this.mLayoutInsets == null) {
            this.mLayoutInsets = computeOpticalInsets();
        }
        return this.mLayoutInsets;
    }

    public void setSkipRtlCheck(boolean skip) {
        this.mSkipRtlCheck = skip;
    }

    public void setOpticalInsets(Insets insets) {
        this.mLayoutInsets = insets;
    }

    public void setSelected(boolean selected) {
        if (((this.mPrivateFlags & 4) != 0) != selected) {
            int i;
            int i2 = this.mPrivateFlags & -5;
            if (selected) {
                i = 4;
            } else {
                i = 0;
            }
            this.mPrivateFlags = i | i2;
            if (!selected) {
                resetPressedState();
            }
            invalidate(true);
            refreshDrawableState();
            dispatchSetSelected(selected);
            if (selected) {
                sendAccessibilityEvent(4);
            } else {
                notifyViewAccessibilityStateChangedIfNeeded(0);
            }
        }
    }

    protected void dispatchSetSelected(boolean selected) {
    }

    @ExportedProperty
    public boolean isSelected() {
        return (this.mPrivateFlags & 4) != 0;
    }

    public void setActivated(boolean activated) {
        int i = 1073741824;
        if (((this.mPrivateFlags & 1073741824) != 0) != activated) {
            int i2 = this.mPrivateFlags & -1073741825;
            if (!activated) {
                i = 0;
            }
            this.mPrivateFlags = i | i2;
            invalidate(true);
            refreshDrawableState();
            dispatchSetActivated(activated);
        }
    }

    protected void dispatchSetActivated(boolean activated) {
    }

    @ExportedProperty
    public boolean isActivated() {
        return (this.mPrivateFlags & 1073741824) != 0;
    }

    public ViewTreeObserver getViewTreeObserver() {
        if (this.mAttachInfo != null) {
            return this.mAttachInfo.mTreeObserver;
        }
        if (this.mFloatingTreeObserver == null) {
            this.mFloatingTreeObserver = new ViewTreeObserver();
        }
        return this.mFloatingTreeObserver;
    }

    public View getRootView() {
        if (this.mAttachInfo != null) {
            View v = this.mAttachInfo.mRootView;
            if (v != null) {
                return v;
            }
        }
        View parent = this;
        while (parent.mParent != null && (parent.mParent instanceof View)) {
            parent = parent.mParent;
        }
        return parent;
    }

    public boolean toGlobalMotionEvent(MotionEvent ev) {
        AttachInfo info = this.mAttachInfo;
        if (info == null) {
            return false;
        }
        Matrix m = info.mTmpMatrix;
        m.set(Matrix.IDENTITY_MATRIX);
        transformMatrixToGlobal(m);
        ev.transform(m);
        return true;
    }

    public boolean toLocalMotionEvent(MotionEvent ev) {
        AttachInfo info = this.mAttachInfo;
        if (info == null) {
            return false;
        }
        Matrix m = info.mTmpMatrix;
        m.set(Matrix.IDENTITY_MATRIX);
        transformMatrixToLocal(m);
        ev.transform(m);
        return true;
    }

    public void transformMatrixToGlobal(Matrix m) {
        ViewParent parent = this.mParent;
        if (parent instanceof View) {
            View vp = (View) parent;
            vp.transformMatrixToGlobal(m);
            m.preTranslate((float) (-vp.mScrollX), (float) (-vp.mScrollY));
        } else if (parent instanceof ViewRootImpl) {
            ViewRootImpl vr = (ViewRootImpl) parent;
            vr.transformMatrixToGlobal(m);
            m.preTranslate(0.0f, (float) (-vr.mCurScrollY));
        }
        m.preTranslate((float) this.mLeft, (float) this.mTop);
        if (!hasIdentityMatrix()) {
            m.preConcat(getMatrix());
        }
    }

    public void transformMatrixToLocal(Matrix m) {
        ViewParent parent = this.mParent;
        if (parent instanceof View) {
            View vp = (View) parent;
            vp.transformMatrixToLocal(m);
            m.postTranslate((float) vp.mScrollX, (float) vp.mScrollY);
        } else if (parent instanceof ViewRootImpl) {
            ViewRootImpl vr = (ViewRootImpl) parent;
            vr.transformMatrixToLocal(m);
            m.postTranslate(0.0f, (float) vr.mCurScrollY);
        }
        m.postTranslate((float) (-this.mLeft), (float) (-this.mTop));
        if (!hasIdentityMatrix()) {
            m.postConcat(getInverseMatrix());
        }
    }

    @ExportedProperty(category = "layout", indexMapping = {@IntToString(from = 0, to = "x"), @IntToString(from = 1, to = "y")})
    public int[] getLocationOnScreen() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        return location;
    }

    public void getLocationOnScreen(int[] location) {
        getLocationInWindow(location);
        AttachInfo info = this.mAttachInfo;
        ViewRootImpl viewRoot = getViewRootImpl();
        if (viewRoot != null && (viewRoot.mScaleFactor.x != 1.0f || viewRoot.mScaleFactor.y != 1.0f)) {
            IBinder atoken = this.mContext.getBaseActivityToken();
            if (info != null && atoken != null) {
                Point pos = ((MultiWindowFacade) this.mContext.getSystemService("multiwindow_facade")).getStackPosition(atoken);
                if (pos != null) {
                    if (isScaleWindow() && viewRoot.mWindowAttributes.type == 1) {
                        pos.x = info.mWindowLeft;
                        pos.y = info.mWindowTop;
                    }
                    location[0] = (int) (((float) location[0]) + (((float) (info.mWindowLeft - pos.x)) / viewRoot.mScaleFactor.x));
                    location[1] = (int) (((float) location[1]) + (((float) (info.mWindowTop - pos.y)) / viewRoot.mScaleFactor.y));
                }
            }
        } else if (info != null) {
            location[0] = location[0] + info.mWindowLeft;
            location[1] = location[1] + info.mWindowTop;
        }
    }

    public void getLocationInWindow(int[] location) {
        if (location == null || location.length < 2) {
            throw new IllegalArgumentException("location must be an array of two integers");
        } else if (this.mAttachInfo == null) {
            location[1] = 0;
            location[0] = 0;
        } else {
            float[] position = this.mAttachInfo.mTmpTransformLocation;
            position[1] = 0.0f;
            position[0] = 0.0f;
            if (!hasIdentityMatrix()) {
                getMatrix().mapPoints(position);
            }
            position[0] = position[0] + ((float) this.mLeft);
            position[1] = position[1] + ((float) this.mTop);
            ViewParent viewParent = this.mParent;
            while (viewParent instanceof View) {
                View view = (View) viewParent;
                position[0] = position[0] - ((float) view.mScrollX);
                position[1] = position[1] - ((float) view.mScrollY);
                if (!view.hasIdentityMatrix()) {
                    view.getMatrix().mapPoints(position);
                }
                position[0] = position[0] + ((float) view.mLeft);
                position[1] = position[1] + ((float) view.mTop);
                viewParent = view.mParent;
            }
            if (viewParent instanceof ViewRootImpl) {
                position[1] = position[1] - ((float) ((ViewRootImpl) viewParent).mCurScrollY);
            }
            location[0] = (int) (position[0] + 0.5f);
            location[1] = (int) (position[1] + 0.5f);
        }
    }

    protected View findViewTraversal(int id) {
        return id == this.mID ? this : null;
    }

    protected View findViewWithTagTraversal(Object tag) {
        return (tag == null || !tag.equals(this.mTag)) ? null : this;
    }

    protected View findViewByPredicateTraversal(Predicate<View> predicate, View childToSkip) {
        return predicate.apply(this) ? this : null;
    }

    public final View findViewById(int id) {
        if (id < 0) {
            return null;
        }
        return findViewTraversal(id);
    }

    final View findViewByAccessibilityId(int accessibilityId) {
        if (accessibilityId < 0) {
            return null;
        }
        View view = findViewByAccessibilityIdTraversal(accessibilityId);
        if (view == null) {
            return null;
        }
        if (!view.includeForAccessibility()) {
            view = null;
        }
        return view;
    }

    public View findViewByAccessibilityIdTraversal(int accessibilityId) {
        return getAccessibilityViewId() == accessibilityId ? this : null;
    }

    public final View findViewWithTag(Object tag) {
        if (tag == null) {
            return null;
        }
        return findViewWithTagTraversal(tag);
    }

    public final View findViewByPredicate(Predicate<View> predicate) {
        return findViewByPredicateTraversal(predicate, null);
    }

    public final View findViewByPredicateInsideOut(View start, Predicate<View> predicate) {
        View childToSkip = null;
        while (true) {
            View view = start.findViewByPredicateTraversal(predicate, childToSkip);
            if (view != null || start == this) {
                return view;
            }
            ViewParent parent = start.getParent();
            if (parent != null && (parent instanceof View)) {
                childToSkip = start;
                start = (View) parent;
            }
        }
        return null;
    }

    public void setId(int id) {
        this.mID = id;
        if (this.mID == -1 && this.mLabelForId != -1) {
            this.mID = generateViewId();
        }
    }

    public void setIsRootNamespace(boolean isRoot) {
        if (isRoot) {
            this.mPrivateFlags |= 8;
        } else {
            this.mPrivateFlags &= -9;
        }
    }

    public boolean isRootNamespace() {
        return (this.mPrivateFlags & 8) != 0;
    }

    @CapturedViewProperty
    public int getId() {
        return this.mID;
    }

    @ExportedProperty
    public Object getTag() {
        return this.mTag;
    }

    public void setTag(Object tag) {
        this.mTag = tag;
    }

    public Object getTag(int key) {
        if (this.mKeyedTags != null) {
            return this.mKeyedTags.get(key);
        }
        return null;
    }

    public void setTag(int key, Object tag) {
        if ((key >>> 24) < 2) {
            throw new IllegalArgumentException("The key must be an application-specific resource id.");
        }
        setKeyedTag(key, tag);
    }

    public void setTagInternal(int key, Object tag) {
        if ((key >>> 24) != 1) {
            throw new IllegalArgumentException("The key must be a framework-specific resource id.");
        }
        setKeyedTag(key, tag);
    }

    private void setKeyedTag(int key, Object tag) {
        if (this.mKeyedTags == null) {
            this.mKeyedTags = new SparseArray(2);
        }
        this.mKeyedTags.put(key, tag);
    }

    public void debug() {
        debug(0);
    }

    protected void debug(int depth) {
        String output = debugIndent(depth - 1) + "+ " + this;
        int id = getId();
        if (id != -1) {
            output = output + " (id=" + id + ")";
        }
        Object tag = getTag();
        if (tag != null) {
            output = output + " (tag=" + tag + ")";
        }
        Log.d(VIEW_LOG_TAG, output);
        if ((this.mPrivateFlags & 2) != 0) {
            Log.d(VIEW_LOG_TAG, debugIndent(depth) + " FOCUSED");
        }
        Log.d(VIEW_LOG_TAG, debugIndent(depth) + "frame={" + this.mLeft + ", " + this.mTop + ", " + this.mRight + ", " + this.mBottom + "} scroll={" + this.mScrollX + ", " + this.mScrollY + "} ");
        if (!(this.mPaddingLeft == 0 && this.mPaddingTop == 0 && this.mPaddingRight == 0 && this.mPaddingBottom == 0)) {
            Log.d(VIEW_LOG_TAG, debugIndent(depth) + "padding={" + this.mPaddingLeft + ", " + this.mPaddingTop + ", " + this.mPaddingRight + ", " + this.mPaddingBottom + "}");
        }
        Log.d(VIEW_LOG_TAG, debugIndent(depth) + "mMeasureWidth=" + this.mMeasuredWidth + " mMeasureHeight=" + this.mMeasuredHeight);
        output = debugIndent(depth);
        if (this.mLayoutParams == null) {
            output = output + "BAD! no layout params";
        } else {
            output = this.mLayoutParams.debug(output);
        }
        Log.d(VIEW_LOG_TAG, output);
        Log.d(VIEW_LOG_TAG, ((debugIndent(depth) + "flags={") + printFlags(this.mViewFlags)) + "}");
        Log.d(VIEW_LOG_TAG, ((debugIndent(depth) + "privateFlags={") + printPrivateFlags(this.mPrivateFlags)) + "}");
    }

    protected static String debugIndent(int depth) {
        StringBuilder spaces = new StringBuilder(((depth * 2) + 3) * 2);
        for (int i = 0; i < (depth * 2) + 3; i++) {
            spaces.append(' ').append(' ');
        }
        return spaces.toString();
    }

    @ExportedProperty(category = "layout")
    public int getBaseline() {
        return -1;
    }

    public boolean isInLayout() {
        ViewRootImpl viewRoot = getViewRootImpl();
        return viewRoot != null && viewRoot.isInLayout();
    }

    public void requestLayout() {
        if (this.mMeasureCache != null) {
            this.mMeasureCache.clear();
        }
        if (this.mAttachInfo != null && this.mAttachInfo.mViewRequestingLayout == null) {
            ViewRootImpl viewRoot = getViewRootImpl();
            if (viewRoot == null || !viewRoot.isInLayout() || viewRoot.requestLayoutDuringLayout(this)) {
                this.mAttachInfo.mViewRequestingLayout = this;
            } else {
                return;
            }
        }
        this.mPrivateFlags |= 4096;
        this.mPrivateFlags |= Integer.MIN_VALUE;
        if (!(this.mParent == null || this.mParent.isLayoutRequested() || this.mParent == null)) {
            this.mParent.requestLayout();
        }
        if (this.mAttachInfo != null && this.mAttachInfo.mViewRequestingLayout == this) {
            this.mAttachInfo.mViewRequestingLayout = null;
        }
    }

    public void forceLayout() {
        if (this.mMeasureCache != null) {
            this.mMeasureCache.clear();
        }
        this.mPrivateFlags |= 4096;
        this.mPrivateFlags |= Integer.MIN_VALUE;
    }

    public final void measure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean optical = isLayoutModeOptical(this);
        if (optical != isLayoutModeOptical(this.mParent)) {
            Insets insets = getOpticalInsets();
            int oWidth = insets.left + insets.right;
            int oHeight = insets.top + insets.bottom;
            if (optical) {
                oWidth = -oWidth;
            }
            widthMeasureSpec = MeasureSpec.adjust(widthMeasureSpec, oWidth);
            if (optical) {
                oHeight = -oHeight;
            }
            heightMeasureSpec = MeasureSpec.adjust(heightMeasureSpec, oHeight);
        }
        long key = (((long) widthMeasureSpec) << 32) | (((long) heightMeasureSpec) & 4294967295L);
        if (this.mMeasureCache == null) {
            this.mMeasureCache = new LongSparseLongArray(2);
        }
        if (!((this.mPrivateFlags & 4096) != 4096 && widthMeasureSpec == this.mOldWidthMeasureSpec && heightMeasureSpec == this.mOldHeightMeasureSpec)) {
            this.mPrivateFlags &= -2049;
            if (!this.mSkipRtlCheck) {
                resolveRtlPropertiesIfNeeded();
            }
            int cacheIndex = (this.mPrivateFlags & 4096) == 4096 ? -1 : this.mMeasureCache.indexOfKey(key);
            if (cacheIndex < 0 || sIgnoreMeasureCache) {
                onMeasure(widthMeasureSpec, heightMeasureSpec);
                this.mPrivateFlags3 &= -9;
            } else {
                long value = this.mMeasureCache.valueAt(cacheIndex);
                setMeasuredDimensionRaw((int) (value >> 32), (int) value);
                this.mPrivateFlags3 |= 8;
            }
            if ((this.mPrivateFlags & 2048) == 2048 || SmartFaceManager.PAGE_BOTTOM.equals(SystemProperties.get("sys.multiwindow.running"))) {
                this.mPrivateFlags |= 8192;
            } else {
                throw new IllegalStateException("View with id " + getId() + ": " + getClass().getName() + "#onMeasure() did not set the" + " measured dimension by calling" + " setMeasuredDimension()");
            }
        }
        this.mOldWidthMeasureSpec = widthMeasureSpec;
        this.mOldHeightMeasureSpec = heightMeasureSpec;
        this.mMeasureCache.put(key, (((long) this.mMeasuredWidth) << 32) | (((long) this.mMeasuredHeight) & 4294967295L));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    protected final void setMeasuredDimension(int measuredWidth, int measuredHeight) {
        boolean optical = isLayoutModeOptical(this);
        if (optical != isLayoutModeOptical(this.mParent)) {
            Insets insets = getOpticalInsets();
            int opticalWidth = insets.left + insets.right;
            int opticalHeight = insets.top + insets.bottom;
            if (!optical) {
                opticalWidth = -opticalWidth;
            }
            measuredWidth += opticalWidth;
            if (!optical) {
                opticalHeight = -opticalHeight;
            }
            measuredHeight += opticalHeight;
        }
        setMeasuredDimensionRaw(measuredWidth, measuredHeight);
    }

    private void setMeasuredDimensionRaw(int measuredWidth, int measuredHeight) {
        this.mMeasuredWidth = measuredWidth;
        this.mMeasuredHeight = measuredHeight;
        this.mPrivateFlags |= 2048;
    }

    public static int combineMeasuredStates(int curState, int newState) {
        return curState | newState;
    }

    public static int resolveSize(int size, int measureSpec) {
        return resolveSizeAndState(size, measureSpec, 0) & 16777215;
    }

    public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case Integer.MIN_VALUE:
                if (specSize >= size) {
                    result = size;
                    break;
                }
                result = specSize | 16777216;
                break;
            case 1073741824:
                result = specSize;
                break;
            default:
                result = size;
                break;
        }
        return (-16777216 & childMeasuredState) | result;
    }

    public static int getDefaultSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case Integer.MIN_VALUE:
            case 1073741824:
                return specSize;
            case 0:
                return size;
            default:
                return result;
        }
    }

    protected int getSuggestedMinimumHeight() {
        return this.mBackground == null ? this.mMinHeight : Math.max(this.mMinHeight, this.mBackground.getMinimumHeight());
    }

    protected int getSuggestedMinimumWidth() {
        return this.mBackground == null ? this.mMinWidth : Math.max(this.mMinWidth, this.mBackground.getMinimumWidth());
    }

    public int getMinimumHeight() {
        return this.mMinHeight;
    }

    public void setMinimumHeight(int minHeight) {
        this.mMinHeight = minHeight;
        requestLayout();
    }

    public int getMinimumWidth() {
        return this.mMinWidth;
    }

    public void setMinimumWidth(int minWidth) {
        this.mMinWidth = minWidth;
        requestLayout();
    }

    public Animation getAnimation() {
        return this.mCurrentAnimation;
    }

    public void startAnimation(Animation animation) {
        animation.setStartTime(-1);
        setAnimation(animation);
        invalidateParentCaches();
        invalidate(true);
    }

    public void clearAnimation() {
        if (this.mCurrentAnimation != null) {
            this.mCurrentAnimation.detach();
        }
        this.mCurrentAnimation = null;
        invalidateParentIfNeeded();
    }

    public void setAnimation(Animation animation) {
        this.mCurrentAnimation = animation;
        if (animation != null) {
            if (this.mAttachInfo != null && this.mAttachInfo.mDisplayState == 1 && animation.getStartTime() == -1) {
                animation.setStartTime(AnimationUtils.currentAnimationTimeMillis());
            }
            animation.reset();
        }
    }

    protected void onAnimationStart() {
        this.mPrivateFlags |= 65536;
    }

    protected void onAnimationEnd() {
        this.mPrivateFlags &= -65537;
    }

    protected boolean onSetAlpha(int alpha) {
        return false;
    }

    public boolean gatherTransparentRegion(Region region) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (!(region == null || attachInfo == null)) {
            if ((this.mPrivateFlags & 128) == 0) {
                int[] location = attachInfo.mTransparentLocation;
                getLocationInWindow(location);
                region.op(location[0], location[1], (location[0] + this.mRight) - this.mLeft, (location[1] + this.mBottom) - this.mTop, Op.DIFFERENCE);
            } else {
                if (!(this.mBackground == null || this.mBackground.getOpacity() == -2)) {
                    applyDrawableToTransparentRegion(this.mBackground, region);
                }
                if (!(this.mForegroundInfo == null || this.mForegroundInfo.mDrawable == null || this.mForegroundInfo.mDrawable.getOpacity() == -2)) {
                    applyDrawableToTransparentRegion(this.mForegroundInfo.mDrawable, region);
                }
            }
        }
        return true;
    }

    public void playSoundEffect(int soundConstant) {
        if (this.mAttachInfo != null && this.mAttachInfo.mRootCallbacks != null && isSoundEffectsEnabled()) {
            this.mAttachInfo.mRootCallbacks.playSoundEffect(soundConstant);
        }
    }

    public boolean performHapticFeedback(int feedbackConstant) {
        return performHapticFeedback(feedbackConstant, 0);
    }

    public boolean performHapticFeedback(int feedbackConstant, int flags) {
        boolean z = false;
        if (this.mAttachInfo == null) {
            return false;
        }
        if ((flags & 1) == 0 && !isHapticFeedbackEnabled()) {
            return false;
        }
        Callbacks callbacks = this.mAttachInfo.mRootCallbacks;
        if ((flags & 2) != 0) {
            z = true;
        }
        return callbacks.performHapticFeedback(feedbackConstant, z);
    }

    public void dispatchCoverStateChanged(boolean isOpen) {
    }

    protected void dispatchMultiWindowStateChanged(int state) {
    }

    public void setSystemUiVisibility(int visibility) {
        if (visibility != this.mSystemUiVisibility) {
            this.mSystemUiVisibility = visibility;
            if (this.mParent != null && this.mAttachInfo != null && !this.mAttachInfo.mRecomputeGlobalAttributes) {
                this.mParent.recomputeViewAttributes(this);
            }
        }
    }

    public int getSystemUiVisibility() {
        return this.mSystemUiVisibility;
    }

    public int getWindowSystemUiVisibility() {
        return this.mAttachInfo != null ? this.mAttachInfo.mSystemUiVisibility : 0;
    }

    public void onWindowSystemUiVisibilityChanged(int visible) {
    }

    public void dispatchWindowSystemUiVisiblityChanged(int visible) {
        onWindowSystemUiVisibilityChanged(visible);
    }

    public void setOnSystemUiVisibilityChangeListener(OnSystemUiVisibilityChangeListener l) {
        getListenerInfo().mOnSystemUiVisibilityChangeListener = l;
        if (this.mParent != null && this.mAttachInfo != null && !this.mAttachInfo.mRecomputeGlobalAttributes) {
            this.mParent.recomputeViewAttributes(this);
        }
    }

    public void dispatchSystemUiVisibilityChanged(int visibility) {
        ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnSystemUiVisibilityChangeListener != null) {
            li.mOnSystemUiVisibilityChangeListener.onSystemUiVisibilityChange(visibility & PUBLIC_STATUS_BAR_VISIBILITY_MASK);
        }
    }

    boolean updateLocalSystemUiVisibility(int localValue, int localChanges) {
        int val = (this.mSystemUiVisibility & (localChanges ^ -1)) | (localValue & localChanges);
        if (val == this.mSystemUiVisibility) {
            return false;
        }
        setSystemUiVisibility(val);
        return true;
    }

    public void setDisabledSystemUiVisibility(int flags) {
        if (this.mAttachInfo != null && this.mAttachInfo.mDisabledSystemUiVisibility != flags) {
            this.mAttachInfo.mDisabledSystemUiVisibility = flags;
            if (this.mParent != null) {
                this.mParent.recomputeViewAttributes(this);
            }
        }
    }

    public final boolean startDrag(ClipData data, DragShadowBuilder shadowBuilder, Object myLocalState, int flags) {
        Canvas canvas;
        Point shadowSize = new Point();
        Point shadowTouchPoint = new Point();
        shadowBuilder.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
        if (shadowSize.x < 0 || shadowSize.y < 0 || shadowTouchPoint.x < 0 || shadowTouchPoint.y < 0) {
            throw new IllegalStateException("Drag shadow dimensions must not be negative");
        }
        Surface surface = new Surface();
        try {
            IBinder token = this.mAttachInfo.mSession.prepareDrag(this.mAttachInfo.mWindow, flags, shadowSize.x, shadowSize.y, surface);
            if (token == null) {
                return false;
            }
            canvas = surface.lockCanvas(null);
            canvas.drawColor(0, Mode.CLEAR);
            shadowBuilder.onDrawShadow(canvas);
            surface.unlockCanvasAndPost(canvas);
            ViewRootImpl root = getViewRootImpl();
            root.setLocalDragState(myLocalState);
            root.getLastTouchPoint(shadowSize);
            boolean okay = this.mAttachInfo.mSession.performDrag(this.mAttachInfo.mWindow, token, (float) shadowSize.x, (float) shadowSize.y, (float) shadowTouchPoint.x, (float) shadowTouchPoint.y, data);
            surface.release();
            return okay;
        } catch (Throwable e) {
            Log.e(VIEW_LOG_TAG, "Unable to initiate drag", e);
            surface.destroy();
            return false;
        } catch (Throwable th) {
            surface.unlockCanvasAndPost(canvas);
        }
    }

    public boolean onDragEvent(DragEvent event) {
        return false;
    }

    public boolean dispatchDragEvent(DragEvent event) {
        ListenerInfo li = this.mListenerInfo;
        if (li == null || li.mOnDragListener == null || (this.mViewFlags & 32) != 0 || !li.mOnDragListener.onDrag(this, event)) {
            return onDragEvent(event);
        }
        return true;
    }

    boolean canAcceptDrag() {
        return (this.mPrivateFlags2 & 1) != 0;
    }

    public void onCloseSystemDialogs(String reason) {
    }

    public void applyDrawableToTransparentRegion(Drawable dr, Region region) {
        Region r = dr.getTransparentRegion();
        Rect db = dr.getBounds();
        AttachInfo attachInfo = this.mAttachInfo;
        if (r == null || attachInfo == null) {
            region.op(db, Op.DIFFERENCE);
            return;
        }
        int w = getRight() - getLeft();
        int h = getBottom() - getTop();
        if (db.left > 0) {
            r.op(0, 0, db.left, h, Op.UNION);
        }
        if (db.right < w) {
            r.op(db.right, 0, w, h, Op.UNION);
        }
        if (db.top > 0) {
            r.op(0, 0, w, db.top, Op.UNION);
        }
        if (db.bottom < h) {
            r.op(0, db.bottom, w, h, Op.UNION);
        }
        int[] location = attachInfo.mTransparentLocation;
        getLocationInWindow(location);
        r.translate(location[0], location[1]);
        region.op(r, Op.INTERSECT);
    }

    private void checkForLongClick(int delayOffset) {
        if ((this.mViewFlags & 2097152) == 2097152) {
            this.mHasPerformedLongPress = false;
            if (this.mPendingCheckForLongPress == null) {
                this.mPendingCheckForLongPress = new CheckForLongPress();
            }
            this.mPendingCheckForLongPress.rememberWindowAttachCount();
            postDelayed(this.mPendingCheckForLongPress, (long) (ViewConfiguration.getLongPressTimeout() - delayOffset));
        }
    }

    public static View inflate(Context context, int resource, ViewGroup root) {
        return LayoutInflater.from(context).inflate(resource, root);
    }

    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        int overScrollMode = this.mOverScrollMode;
        boolean canScrollHorizontal = computeHorizontalScrollRange() > computeHorizontalScrollExtent();
        boolean canScrollVertical = computeVerticalScrollRange() > computeVerticalScrollExtent();
        boolean overScrollHorizontal = overScrollMode == 0 || (overScrollMode == 1 && canScrollHorizontal);
        boolean overScrollVertical = overScrollMode == 0 || (overScrollMode == 1 && canScrollVertical);
        int newScrollX = scrollX + deltaX;
        if (!overScrollHorizontal) {
            maxOverScrollX = 0;
        }
        int newScrollY = scrollY + deltaY;
        if (!overScrollVertical) {
            maxOverScrollY = 0;
        }
        int left = -maxOverScrollX;
        int right = maxOverScrollX + scrollRangeX;
        int top = -maxOverScrollY;
        int bottom = maxOverScrollY + scrollRangeY;
        boolean clampedX = false;
        if (newScrollX > right) {
            newScrollX = right;
            clampedX = true;
        } else if (newScrollX < left) {
            newScrollX = left;
            clampedX = true;
        }
        boolean clampedY = false;
        if (newScrollY > bottom) {
            newScrollY = bottom;
            clampedY = true;
        } else if (newScrollY < top) {
            newScrollY = top;
            clampedY = true;
        }
        onOverScrolled(newScrollX, newScrollY, clampedX, clampedY);
        if (clampedX || clampedY) {
            return true;
        }
        return false;
    }

    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
    }

    public int getOverScrollMode() {
        return this.mOverScrollMode;
    }

    public void setOverScrollMode(int overScrollMode) {
        if (overScrollMode == 0 || overScrollMode == 1 || overScrollMode == 2) {
            this.mOverScrollMode = overScrollMode;
            return;
        }
        throw new IllegalArgumentException("Invalid overscroll mode " + overScrollMode);
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        if (enabled) {
            this.mPrivateFlags3 |= 128;
            return;
        }
        stopNestedScroll();
        this.mPrivateFlags3 &= -129;
    }

    public boolean isNestedScrollingEnabled() {
        return (this.mPrivateFlags3 & 128) == 128;
    }

    public boolean startNestedScroll(int axes) {
        if (hasNestedScrollingParent()) {
            return true;
        }
        if (isNestedScrollingEnabled()) {
            View child = this;
            for (ViewParent p = getParent(); p != null; p = p.getParent()) {
                try {
                    if (p.onStartNestedScroll(child, this, axes)) {
                        this.mNestedScrollingParent = p;
                        p.onNestedScrollAccepted(child, this, axes);
                        return true;
                    }
                } catch (AbstractMethodError e) {
                    Log.e(VIEW_LOG_TAG, "ViewParent " + p + " does not implement interface " + "method onStartNestedScroll", e);
                }
                if (p instanceof View) {
                    child = (View) p;
                }
            }
        }
        return false;
    }

    public void stopNestedScroll() {
        if (this.mNestedScrollingParent != null) {
            this.mNestedScrollingParent.onStopNestedScroll(this);
            this.mNestedScrollingParent = null;
        }
    }

    public boolean hasNestedScrollingParent() {
        return this.mNestedScrollingParent != null;
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        if (isNestedScrollingEnabled() && this.mNestedScrollingParent != null) {
            if (dxConsumed != 0 || dyConsumed != 0 || dxUnconsumed != 0 || dyUnconsumed != 0) {
                int startX = 0;
                int startY = 0;
                if (offsetInWindow != null) {
                    getLocationInWindow(offsetInWindow);
                    startX = offsetInWindow[0];
                    startY = offsetInWindow[1];
                }
                this.mNestedScrollingParent.onNestedScroll(this, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
                if (offsetInWindow != null) {
                    getLocationInWindow(offsetInWindow);
                    offsetInWindow[0] = offsetInWindow[0] - startX;
                    offsetInWindow[1] = offsetInWindow[1] - startY;
                }
                return true;
            } else if (offsetInWindow != null) {
                offsetInWindow[0] = 0;
                offsetInWindow[1] = 0;
            }
        }
        return false;
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        if (!isNestedScrollingEnabled() || this.mNestedScrollingParent == null) {
            return false;
        }
        if (dx != 0 || dy != 0) {
            int startX = 0;
            int startY = 0;
            if (offsetInWindow != null) {
                getLocationInWindow(offsetInWindow);
                startX = offsetInWindow[0];
                startY = offsetInWindow[1];
            }
            if (consumed == null) {
                if (this.mTempNestedScrollConsumed == null) {
                    this.mTempNestedScrollConsumed = new int[2];
                }
                consumed = this.mTempNestedScrollConsumed;
            }
            consumed[0] = 0;
            consumed[1] = 0;
            this.mNestedScrollingParent.onNestedPreScroll(this, dx, dy, consumed);
            if (offsetInWindow != null) {
                getLocationInWindow(offsetInWindow);
                offsetInWindow[0] = offsetInWindow[0] - startX;
                offsetInWindow[1] = offsetInWindow[1] - startY;
            }
            if (consumed[0] == 0 && consumed[1] == 0) {
                return false;
            }
            return true;
        } else if (offsetInWindow == null) {
            return false;
        } else {
            offsetInWindow[0] = 0;
            offsetInWindow[1] = 0;
            return false;
        }
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        if (!isNestedScrollingEnabled() || this.mNestedScrollingParent == null) {
            return false;
        }
        return this.mNestedScrollingParent.onNestedFling(this, velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        if (!isNestedScrollingEnabled() || this.mNestedScrollingParent == null) {
            return false;
        }
        return this.mNestedScrollingParent.onNestedPreFling(this, velocityX, velocityY);
    }

    protected float getVerticalScrollFactor() {
        if (this.mVerticalScrollFactor == 0.0f) {
            TypedValue outValue = new TypedValue();
            if (this.mContext.getTheme().resolveAttribute(R.attr.listPreferredItemHeight, outValue, true)) {
                this.mVerticalScrollFactor = outValue.getDimension(this.mContext.getResources().getDisplayMetrics());
            } else {
                throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
            }
        }
        return this.mVerticalScrollFactor;
    }

    protected float getHorizontalScrollFactor() {
        return getVerticalScrollFactor();
    }

    @ExportedProperty(category = "text", mapping = {@IntToString(from = 0, to = "INHERIT"), @IntToString(from = 1, to = "FIRST_STRONG"), @IntToString(from = 2, to = "ANY_RTL"), @IntToString(from = 3, to = "LTR"), @IntToString(from = 4, to = "RTL"), @IntToString(from = 5, to = "LOCALE"), @IntToString(from = 6, to = "FIRST_STRONG_LTR"), @IntToString(from = 7, to = "FIRST_STRONG_RTL")})
    public int getRawTextDirection() {
        return (this.mPrivateFlags2 & PFLAG2_TEXT_DIRECTION_MASK) >> 6;
    }

    public void setTextDirection(int textDirection) {
        if (getRawTextDirection() != textDirection) {
            this.mPrivateFlags2 &= -449;
            resetResolvedTextDirection();
            this.mPrivateFlags2 |= (textDirection << 6) & PFLAG2_TEXT_DIRECTION_MASK;
            resolveTextDirection();
            onRtlPropertiesChanged(getLayoutDirection());
            requestLayout();
            invalidate(true);
        }
    }

    @ExportedProperty(category = "text", mapping = {@IntToString(from = 0, to = "INHERIT"), @IntToString(from = 1, to = "FIRST_STRONG"), @IntToString(from = 2, to = "ANY_RTL"), @IntToString(from = 3, to = "LTR"), @IntToString(from = 4, to = "RTL"), @IntToString(from = 5, to = "LOCALE"), @IntToString(from = 6, to = "FIRST_STRONG_LTR"), @IntToString(from = 7, to = "FIRST_STRONG_RTL")})
    public int getTextDirection() {
        return (this.mPrivateFlags2 & PFLAG2_TEXT_DIRECTION_RESOLVED_MASK) >> 10;
    }

    public boolean resolveTextDirection() {
        this.mPrivateFlags2 &= -7681;
        if (hasRtlSupport()) {
            int textDirection = getRawTextDirection();
            switch (textDirection) {
                case 0:
                    if (canResolveTextDirection()) {
                        try {
                            if (this.mParent != null && this.mParent.isTextDirectionResolved()) {
                                int parentResolvedDirection = 3;
                                try {
                                    if (this.mParent != null) {
                                        parentResolvedDirection = this.mParent.getTextDirection();
                                    }
                                } catch (AbstractMethodError e) {
                                    Log.e(VIEW_LOG_TAG, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", e);
                                    parentResolvedDirection = 3;
                                }
                                switch (parentResolvedDirection) {
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                    case 6:
                                    case 7:
                                        this.mPrivateFlags2 |= parentResolvedDirection << 10;
                                        break;
                                    default:
                                        this.mPrivateFlags2 |= 1024;
                                        break;
                                }
                            }
                            this.mPrivateFlags2 |= 1024;
                            return false;
                        } catch (AbstractMethodError e2) {
                            Log.e(VIEW_LOG_TAG, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", e2);
                            this.mPrivateFlags2 |= SYSTEM_UI_LAYOUT_FLAGS;
                            return true;
                        }
                    }
                    this.mPrivateFlags2 |= 1024;
                    return false;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    this.mPrivateFlags2 |= textDirection << 10;
                    break;
                default:
                    this.mPrivateFlags2 |= 1024;
                    break;
            }
        }
        this.mPrivateFlags2 |= 1024;
        this.mPrivateFlags2 |= 512;
        return true;
    }

    public boolean canResolveTextDirection() {
        switch (getRawTextDirection()) {
            case 0:
                if (this.mParent != null) {
                    try {
                        return this.mParent.canResolveTextDirection();
                    } catch (AbstractMethodError e) {
                        Log.e(VIEW_LOG_TAG, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", e);
                    }
                }
                return false;
            default:
                return true;
        }
    }

    public void resetResolvedTextDirection() {
        this.mPrivateFlags2 &= -7681;
        this.mPrivateFlags2 |= 1024;
    }

    public boolean isTextDirectionInherited() {
        return getRawTextDirection() == 0;
    }

    public boolean isTextDirectionResolved() {
        return (this.mPrivateFlags2 & 512) == 512;
    }

    @ExportedProperty(category = "text", mapping = {@IntToString(from = 0, to = "INHERIT"), @IntToString(from = 1, to = "GRAVITY"), @IntToString(from = 2, to = "TEXT_START"), @IntToString(from = 3, to = "TEXT_END"), @IntToString(from = 4, to = "CENTER"), @IntToString(from = 5, to = "VIEW_START"), @IntToString(from = 6, to = "VIEW_END")})
    public int getRawTextAlignment() {
        return (this.mPrivateFlags2 & PFLAG2_TEXT_ALIGNMENT_MASK) >> 13;
    }

    public void setTextAlignment(int textAlignment) {
        if (textAlignment != getRawTextAlignment()) {
            this.mPrivateFlags2 &= -57345;
            resetResolvedTextAlignment();
            this.mPrivateFlags2 |= (textAlignment << 13) & PFLAG2_TEXT_ALIGNMENT_MASK;
            resolveTextAlignment();
            onRtlPropertiesChanged(getLayoutDirection());
            requestLayout();
            invalidate(true);
        }
    }

    @ExportedProperty(category = "text", mapping = {@IntToString(from = 0, to = "INHERIT"), @IntToString(from = 1, to = "GRAVITY"), @IntToString(from = 2, to = "TEXT_START"), @IntToString(from = 3, to = "TEXT_END"), @IntToString(from = 4, to = "CENTER"), @IntToString(from = 5, to = "VIEW_START"), @IntToString(from = 6, to = "VIEW_END")})
    public int getTextAlignment() {
        return (this.mPrivateFlags2 & PFLAG2_TEXT_ALIGNMENT_RESOLVED_MASK) >> 17;
    }

    public boolean resolveTextAlignment() {
        this.mPrivateFlags2 &= -983041;
        if (hasRtlSupport()) {
            int textAlignment = getRawTextAlignment();
            switch (textAlignment) {
                case 0:
                    if (canResolveTextAlignment()) {
                        try {
                            if (this.mParent != null && this.mParent.isTextAlignmentResolved()) {
                                int parentResolvedTextAlignment;
                                try {
                                    parentResolvedTextAlignment = this.mParent.getTextAlignment();
                                } catch (AbstractMethodError e) {
                                    Log.e(VIEW_LOG_TAG, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", e);
                                    parentResolvedTextAlignment = 1;
                                }
                                switch (parentResolvedTextAlignment) {
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                    case 6:
                                        this.mPrivateFlags2 |= parentResolvedTextAlignment << 17;
                                        break;
                                    default:
                                        this.mPrivateFlags2 |= 131072;
                                        break;
                                }
                            }
                            this.mPrivateFlags2 |= 131072;
                            return false;
                        } catch (AbstractMethodError e2) {
                            Log.e(VIEW_LOG_TAG, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", e2);
                            this.mPrivateFlags2 |= 196608;
                            return true;
                        }
                    }
                    this.mPrivateFlags2 |= 131072;
                    return false;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    this.mPrivateFlags2 |= textAlignment << 17;
                    break;
                default:
                    this.mPrivateFlags2 |= 131072;
                    break;
            }
        }
        this.mPrivateFlags2 |= 131072;
        this.mPrivateFlags2 |= 65536;
        return true;
    }

    public boolean canResolveTextAlignment() {
        switch (getRawTextAlignment()) {
            case 0:
                if (this.mParent != null) {
                    try {
                        return this.mParent.canResolveTextAlignment();
                    } catch (AbstractMethodError e) {
                        Log.e(VIEW_LOG_TAG, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", e);
                    }
                }
                return false;
            default:
                return true;
        }
    }

    public void resetResolvedTextAlignment() {
        this.mPrivateFlags2 &= -983041;
        this.mPrivateFlags2 |= 131072;
    }

    public boolean isTextAlignmentInherited() {
        return getRawTextAlignment() == 0;
    }

    public boolean isTextAlignmentResolved() {
        return (this.mPrivateFlags2 & 65536) == 65536;
    }

    public static int generateViewId() {
        int result;
        int newValue;
        do {
            result = sNextGeneratedId.get();
            newValue = result + 1;
            if (newValue > 16777215) {
                newValue = 1;
            }
        } while (!sNextGeneratedId.compareAndSet(result, newValue));
        return result;
    }

    public void captureTransitioningViews(List<View> transitioningViews) {
        if (getVisibility() == 0) {
            transitioningViews.add(this);
        }
    }

    public void findNamedViews(Map<String, View> namedElements) {
        if (getVisibility() == 0 || this.mGhostView != null) {
            String transitionName = getTransitionName();
            if (transitionName != null) {
                namedElements.put(transitionName, this);
            }
        }
    }

    public void hackTurnOffWindowResizeAnim(boolean off) {
        if (this.mAttachInfo != null) {
            this.mAttachInfo.mTurnOffWindowResizeAnim = off;
        }
    }

    public ViewPropertyAnimator animate() {
        if (this.mAnimator == null) {
            this.mAnimator = new ViewPropertyAnimator(this);
        }
        return this.mAnimator;
    }

    public final void setTransitionName(String transitionName) {
        this.mTransitionName = transitionName;
    }

    @ExportedProperty
    public String getTransitionName() {
        return this.mTransitionName;
    }

    private boolean inLiveRegion() {
        if (getAccessibilityLiveRegion() != 0) {
            return true;
        }
        for (ViewParent parent = getParent(); parent instanceof View; parent = parent.getParent()) {
            if (((View) parent).getAccessibilityLiveRegion() != 0) {
                return true;
            }
        }
        return false;
    }

    private static void dumpFlags() {
        HashMap<String, String> found = Maps.newHashMap();
        try {
            for (Field field : View.class.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                    if (field.getType().equals(Integer.TYPE)) {
                        dumpFlag(found, field.getName(), field.getInt(null));
                    } else if (field.getType().equals(int[].class)) {
                        int[] values = (int[]) field.get(null);
                        for (int i = 0; i < values.length; i++) {
                            dumpFlag(found, field.getName() + "[" + i + "]", values[i]);
                        }
                    }
                }
            }
            ArrayList<String> keys = Lists.newArrayList();
            keys.addAll(found.keySet());
            Collections.sort(keys);
            Iterator i$ = keys.iterator();
            while (i$.hasNext()) {
                Log.d(VIEW_LOG_TAG, (String) found.get((String) i$.next()));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void dumpFlag(HashMap<String, String> found, String name, int value) {
        String substring;
        String bits = String.format("%32s", new Object[]{Integer.toBinaryString(value)}).replace('0', ' ');
        int prefix = name.indexOf(95);
        StringBuilder stringBuilder = new StringBuilder();
        if (prefix > 0) {
            substring = name.substring(0, prefix);
        } else {
            substring = name;
        }
        found.put(stringBuilder.append(substring).append(bits).append(name).toString(), bits + " " + name);
    }

    public void encode(ViewHierarchyEncoder stream) {
        stream.beginObject(this);
        encodeProperties(stream);
        stream.endObject();
    }

    protected void encodeProperties(ViewHierarchyEncoder stream) {
        Object resolveId = ViewDebug.resolveId(getContext(), this.mID);
        if (resolveId instanceof String) {
            stream.addProperty("id", (String) resolveId);
        } else {
            stream.addProperty("id", this.mID);
        }
        stream.addProperty("misc:transformation.alpha", this.mTransformationInfo != null ? this.mTransformationInfo.mAlpha : 0.0f);
        stream.addProperty("misc:transitionName", getTransitionName());
        stream.addProperty("layout:left", this.mLeft);
        stream.addProperty("layout:right", this.mRight);
        stream.addProperty("layout:top", this.mTop);
        stream.addProperty("layout:bottom", this.mBottom);
        stream.addProperty("layout:width", getWidth());
        stream.addProperty("layout:height", getHeight());
        stream.addProperty("layout:layoutDirection", getLayoutDirection());
        stream.addProperty("layout:layoutRtl", isLayoutRtl());
        stream.addProperty("layout:hasTransientState", hasTransientState());
        stream.addProperty("layout:baseline", getBaseline());
        LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            stream.addPropertyKey("layoutParams");
            layoutParams.encode(stream);
        }
        stream.addProperty("scrolling:scrollX", this.mScrollX);
        stream.addProperty("scrolling:scrollY", this.mScrollY);
        stream.addProperty("padding:paddingLeft", this.mPaddingLeft);
        stream.addProperty("padding:paddingRight", this.mPaddingRight);
        stream.addProperty("padding:paddingTop", this.mPaddingTop);
        stream.addProperty("padding:paddingBottom", this.mPaddingBottom);
        stream.addProperty("padding:userPaddingRight", this.mUserPaddingRight);
        stream.addProperty("padding:userPaddingLeft", this.mUserPaddingLeft);
        stream.addProperty("padding:userPaddingBottom", this.mUserPaddingBottom);
        stream.addProperty("padding:userPaddingStart", this.mUserPaddingStart);
        stream.addProperty("padding:userPaddingEnd", this.mUserPaddingEnd);
        stream.addProperty("measurement:minHeight", this.mMinHeight);
        stream.addProperty("measurement:minWidth", this.mMinWidth);
        stream.addProperty("measurement:measuredWidth", this.mMeasuredWidth);
        stream.addProperty("measurement:measuredHeight", this.mMeasuredHeight);
        stream.addProperty("drawing:elevation", getElevation());
        stream.addProperty("drawing:translationX", getTranslationX());
        stream.addProperty("drawing:translationY", getTranslationY());
        stream.addProperty("drawing:translationZ", getTranslationZ());
        stream.addProperty("drawing:rotation", getRotation());
        stream.addProperty("drawing:rotationX", getRotationX());
        stream.addProperty("drawing:rotationY", getRotationY());
        stream.addProperty("drawing:scaleX", getScaleX());
        stream.addProperty("drawing:scaleY", getScaleY());
        stream.addProperty("drawing:pivotX", getPivotX());
        stream.addProperty("drawing:pivotY", getPivotY());
        stream.addProperty("drawing:opaque", isOpaque());
        stream.addProperty("drawing:alpha", getAlpha());
        stream.addProperty("drawing:transitionAlpha", getTransitionAlpha());
        stream.addProperty("drawing:shadow", hasShadow());
        stream.addProperty("drawing:solidColor", getSolidColor());
        stream.addProperty("drawing:layerType", this.mLayerType);
        stream.addProperty("drawing:willNotDraw", willNotDraw());
        stream.addProperty("drawing:hardwareAccelerated", isHardwareAccelerated());
        stream.addProperty("drawing:willNotCacheDrawing", willNotCacheDrawing());
        stream.addProperty("drawing:drawingCacheEnabled", isDrawingCacheEnabled());
        stream.addProperty("drawing:overlappingRendering", hasOverlappingRendering());
        stream.addProperty("focus:hasFocus", hasFocus());
        stream.addProperty("focus:isFocused", isFocused());
        stream.addProperty("focus:isFocusable", isFocusable());
        stream.addProperty("focus:isFocusableInTouchMode", isFocusableInTouchMode());
        stream.addProperty("misc:clickable", isClickable());
        stream.addProperty("misc:pressed", isPressed());
        stream.addProperty("misc:selected", isSelected());
        stream.addProperty("misc:touchMode", isInTouchMode());
        stream.addProperty("misc:hovered", isHovered());
        stream.addProperty("misc:activated", isActivated());
        stream.addProperty("misc:visibility", getVisibility());
        stream.addProperty("misc:fitsSystemWindows", getFitsSystemWindows());
        stream.addProperty("misc:filterTouchesWhenObscured", getFilterTouchesWhenObscured());
        stream.addProperty("misc:enabled", isEnabled());
        stream.addProperty("misc:soundEffectsEnabled", isSoundEffectsEnabled());
        stream.addProperty("misc:hapticFeedbackEnabled", isHapticFeedbackEnabled());
        Theme theme = getContext().getTheme();
        if (theme != null) {
            stream.addPropertyKey("theme");
            theme.encode(stream);
        }
        int n = this.mAttributes != null ? this.mAttributes.length : 0;
        stream.addProperty("meta:__attrCount__", n / 2);
        for (int i = 0; i < n; i += 2) {
            stream.addProperty("meta:__attr__" + this.mAttributes[i], this.mAttributes[i + 1]);
        }
        stream.addProperty("misc:scrollBarStyle", getScrollBarStyle());
        stream.addProperty("text:textDirection", getTextDirection());
        stream.addProperty("text:textAlignment", getTextAlignment());
        CharSequence contentDescription = getContentDescription();
        stream.addProperty("accessibility:contentDescription", contentDescription == null ? "" : contentDescription.toString());
        stream.addProperty("accessibility:labelFor", getLabelFor());
        stream.addProperty("accessibility:importantForAccessibility", getImportantForAccessibility());
    }

    public SmartClipDataExtractionListener getSmartClipDataExtractionListener() {
        return this.mSmartClipDataExtractionListener;
    }

    public boolean setSmartClipDataExtractionListener(SmartClipDataExtractionListener listener) {
        this.mSmartClipDataExtractionListener = listener;
        return true;
    }

    public SmartClipMetaTagArrayImpl getSmartClipTags() {
        return this.mSmartClipDataTag;
    }

    public boolean addSmartClipTag(SlookSmartClipMetaTag metaTag) {
        if (metaTag == null) {
            return false;
        }
        if (this.mSmartClipDataTag == null) {
            this.mSmartClipDataTag = new SmartClipMetaTagArrayImpl();
        }
        this.mSmartClipDataTag.add(metaTag);
        return true;
    }

    public boolean removeSmartClipTag(String tagType) {
        if (this.mSmartClipDataTag == null || tagType == null) {
            return false;
        }
        this.mSmartClipDataTag.removeTag(tagType);
        return true;
    }

    public boolean clearAllSmartClipTag() {
        this.mSmartClipDataTag = null;
        return true;
    }

    public int extractSmartClipData(SlookSmartClipDataElement resultElement, SlookSmartClipCroppedArea croppedArea) {
        SmartClipDataElementImpl elementImpl = (SmartClipDataElementImpl) resultElement;
        if (elementImpl == null) {
            return 0;
        }
        SmartClipDataRepositoryImpl repository = elementImpl.getDataRepository();
        SmartClipDataCropperImpl cropper = repository != null ? (SmartClipDataCropperImpl) repository.getSmartClipDataCropper() : null;
        if (cropper != null) {
            return cropper.extractDefaultSmartClipData(this, elementImpl, croppedArea);
        }
        return 0;
    }

    @RemotableViewMethod
    public void setHoverPopupType(int type) {
        if (isHoveringUIEnabled()) {
            this.mHoverPopupType = type;
        }
    }

    public HoverPopupWindow getHoverPopup() {
        return this.mHoverPopup;
    }

    public void setHoverPopup(HoverPopupWindow hoverPopup) {
        this.mHoverPopup = hoverPopup;
    }

    public HoverPopupWindow getHoverPopupWindow() {
        if (!isHoveringUIEnabled()) {
            return null;
        }
        if (this.mHoverPopup == null) {
            this.mHoverPopup = new MoreInfoHPW(this, this.mHoverPopupType);
        }
        setHoverPopupWindowSettings(2);
        return this.mHoverPopup;
    }

    public HoverPopupWindow getHoverPopupWindow(int tooltype) {
        if (!isHoveringUIEnabled()) {
            return null;
        }
        if (this.mHoverPopup == null) {
            if (tooltype != 2) {
                if (tooltype == 1) {
                    switch (this.mHoverPopupType) {
                        case 1:
                            break;
                        case 2:
                            this.mHoverPopup = new MoreInfoHPW(this, this.mHoverPopupType);
                            break;
                        case 3:
                            this.mHoverPopup = new MoreInfoHPW(this, this.mHoverPopupType);
                            break;
                        default:
                            break;
                    }
                }
            }
            this.mHoverPopup = new MoreInfoHPW(this, this.mHoverPopupType);
        }
        setHoverPopupWindowSettings(tooltype);
        this.mHoverPopupToolTypeByApp = tooltype;
        if (tooltype == 1 && this.mHoverPopupType == 1 && this.mHoverPopup != null) {
            this.mHoverPopup.dismiss();
            this.mHoverPopup = null;
        }
        return this.mHoverPopup;
    }

    public HoverPopupWindow getHoverPopupWindow(int tooltype, boolean calledByApp) {
        int backup_HoverPopupToolTypeByApp = this.mHoverPopupToolTypeByApp;
        getHoverPopupWindow(tooltype);
        if (!calledByApp) {
            this.mHoverPopupToolTypeByApp = backup_HoverPopupToolTypeByApp;
        }
        return this.mHoverPopup;
    }

    protected boolean setHoverPopupWindowSettings(int tooltype) {
        if (this.mHoverPopup == null) {
            return false;
        }
        if (tooltype == 2) {
            this.mHoverPopup.setHoverPopupToolType(2);
            this.mHoverPopup.setFHAnimationEnabledByApp(false, false);
            switch (this.mHoverPopupType) {
                case 1:
                    this.mHoverPopup.setInfoPickerMoveEabledByApp(false, false);
                    this.mHoverPopup.setFHGuideLineEnabledByApp(false, false);
                    return true;
                case 2:
                    this.mHoverPopup.setInfoPickerMoveEabledByApp(true, false);
                    this.mHoverPopup.setFHGuideLineEnabledByApp(true, false);
                    return true;
                default:
                    return true;
            }
        } else if (tooltype != 1) {
            return true;
        } else {
            this.mHoverPopup.setHoverPopupToolType(1);
            switch (this.mHoverPopupType) {
                case 1:
                    return true;
                case 2:
                    this.mHoverPopup.setFHAnimationEnabledByApp(true, false);
                    this.mHoverPopup.setInfoPickerMoveEabledByApp(true, false);
                    this.mHoverPopup.setFHGuideLineEnabledByApp(true, false);
                    return true;
                case 3:
                    this.mHoverPopup.setFHAnimationEnabledByApp(true, false);
                    return true;
                default:
                    return true;
            }
        }
    }

    protected boolean isHoveringUIEnabled() {
        if ((sHoverUIEnableFlag & 15) == 0) {
            sHoverUIEnableFlag &= -16;
            sHoverUIEnableFlag = (this.mContext.getPackageManager().hasSystemFeature("com.sec.feature.hovering_ui") ? 1 : 2) | sHoverUIEnableFlag;
        }
        if ((sHoverUIEnableFlag & 15) == 1) {
            return true;
        }
        return false;
    }

    protected boolean isTextSelectionEnabled() {
        if ((sHoverUIEnableFlag & 240) == 0) {
            sHoverUIEnableFlag &= -241;
            sHoverUIEnableFlag = (this.mContext.getPackageManager().hasSystemFeature("com.sec.feature.spen_usp") ? 16 : 32) | sHoverUIEnableFlag;
        }
        return (sHoverUIEnableFlag & 240) == 16;
    }

    protected boolean isWritingBuddyFeatureEnabled() {
        return false;
    }

    protected int getHoverUIFeatureLevel() {
        if (!misHoverUIFeatureLevelChecked) {
            mHoverUIFeatureLevel = this.mContext.getPackageManager().getSystemFeatureLevel("com.sec.feature.hovering_ui");
            misHoverUIFeatureLevelChecked = true;
        }
        return mHoverUIFeatureLevel;
    }

    public boolean getIsDetachedFromWindow() {
        return this.mIsDetachedFromWindow;
    }

    public void twSetContextMenuZOrderToTop(boolean isTop) {
        this.mIsSetContextMenuZOrderToTop = isTop;
    }

    public boolean twGetContextMenuZOrderToTop() {
        return this.mIsSetContextMenuZOrderToTop;
    }

    public boolean isMultiWindow() {
        MultiWindowStyle style = getMultiWindowStyle();
        if (style == null || style.getType() == 0) {
            return false;
        }
        return true;
    }

    public boolean isScaleWindow() {
        MultiWindowStyle style = getMultiWindowStyle();
        if (style == null || style.getType() == 0 || !style.isEnabled(2048) || style.isEnabled(4)) {
            return false;
        }
        return true;
    }

    public boolean usingInputMethodInCascade() {
        MultiWindowStyle style = getMultiWindowStyle();
        if (style == null || !style.isCascade() || style.isEnabled(4) || this.mAttachInfo.mWindowTop >= style.getBounds().top) {
            return false;
        }
        return true;
    }

    protected MultiWindowStyle getMultiWindowStyle() {
        if (this.mContext != null) {
            if (this.mContext instanceof Activity) {
                return ((Activity) this.mContext).getMultiWindowStyle();
            }
            ViewRootImpl viewRoot = getViewRootImpl();
            if (viewRoot != null && viewRoot.mWindowAttributes.type == WindowManager.LayoutParams.TYPE_MINI_APP) {
                return MultiWindowStyle.sConstDefaultMultiWindowStyle;
            }
            MultiWindowStyle style = this.mContext.getAppMultiWindowStyle();
            if (style != null) {
                return style;
            }
        }
        return MultiWindowStyle.sConstDefaultMultiWindowStyle;
    }

    public boolean isAttachedToFullWindow() {
        ViewRootImpl viewRoot = getViewRootImpl();
        if (viewRoot != null && viewRoot.mWindowAttributes.x == 0 && viewRoot.mWindowAttributes.y == 0 && viewRoot.mWindowAttributes.width == -1 && viewRoot.mWindowAttributes.height == -1) {
            return true;
        }
        return false;
    }

    public void setUseGestureDetectorExtension(boolean enableflag) {
        ViewRootImpl viewroot = getViewRootImpl();
        if (viewroot != null) {
            viewroot.setUseGestureDetectorEx(enableflag);
        }
    }

    protected boolean isMultiPenSelectionEnabled() {
        if (sSpenUspLevel >= 3) {
            return true;
        }
        return false;
    }

    protected boolean dispatchSipResizeAnimationState(boolean isStart) {
        return false;
    }
}
