package android.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.input.IInputManager;
import android.hardware.input.IInputManager.Stub;
import android.media.SoundPool;
import android.os.Binder;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.Settings$System;
import android.util.Log;
import com.android.internal.R;
import com.android.internal.util.XmlUtils;
import com.samsung.android.contextaware.ContextAwareConstants;

public final class PointerIcon implements Parcelable {
    public static final Creator<PointerIcon> CREATOR = new Creator<PointerIcon>() {
        public PointerIcon createFromParcel(Parcel in) {
            int style = in.readInt();
            if (style == 0) {
                return PointerIcon.getNullIcon();
            }
            int systemIconResourceId = in.readInt();
            if (systemIconResourceId == 0) {
                return PointerIcon.createCustomIcon((Bitmap) Bitmap.CREATOR.createFromParcel(in), in.readFloat(), in.readFloat());
            }
            PointerIcon icon = new PointerIcon(style);
            icon.mSystemIconResourceId = systemIconResourceId;
            return icon;
        }

        public PointerIcon[] newArray(int size) {
            return new PointerIcon[size];
        }
    };
    public static final int CUSTOM_DEFAULT_ICON_ID = 255;
    private static final int HOVERING_CUSTOM_ICON_MAX_COUNT = 5;
    private static final int HOVERING_CUSTOM_ICON_MAX_SIZE = 40000;
    private static final int HOVERING_CUSTOM_ICON_RESIZE_X = 100;
    public static final int HOVERING_FLAG_ALWAYSSHOW = 1;
    public static final int HOVERING_PENSELECT_POINTER_01 = 21;
    public static final int HOVERING_SCROLLICON_POINTER_01 = 11;
    public static final int HOVERING_SCROLLICON_POINTER_02 = 12;
    public static final int HOVERING_SCROLLICON_POINTER_03 = 13;
    public static final int HOVERING_SCROLLICON_POINTER_04 = 14;
    public static final int HOVERING_SCROLLICON_POINTER_05 = 15;
    public static final int HOVERING_SCROLLICON_POINTER_06 = 16;
    public static final int HOVERING_SCROLLICON_POINTER_07 = 17;
    public static final int HOVERING_SCROLLICON_POINTER_08 = 18;
    public static final int HOVERING_SPENICON_CURSOR = 2;
    public static final int HOVERING_SPENICON_CUSTOM = 0;
    public static final int HOVERING_SPENICON_DEFAULT = 1;
    public static final int HOVERING_SPENICON_DEFAULT_CUSTOM = 22;
    public static final int HOVERING_SPENICON_DISABLE_DEFAULT_CUSTOM = 23;
    public static final int HOVERING_SPENICON_HIDE = 19;
    public static final int HOVERING_SPENICON_HOVERPOPUP_DEFAULT = 20;
    public static final int HOVERING_SPENICON_MORE = 10;
    public static final int HOVERING_SPENICON_MOVE = 5;
    public static final int HOVERING_SPENICON_RESIZE_01 = 6;
    public static final int HOVERING_SPENICON_RESIZE_02 = 7;
    public static final int HOVERING_SPENICON_RESIZE_03 = 8;
    public static final int HOVERING_SPENICON_RESIZE_04 = 9;
    public static final int HOVERING_SPENICON_SPLIT_01 = 3;
    public static final int HOVERING_SPENICON_SPLIT_02 = 4;
    public static final int MOUSEICON_CURSOR = 102;
    public static final int MOUSEICON_CUSTOM = 100;
    public static final int MOUSEICON_DEFAULT = 101;
    public static final int MOUSEICON_DRAWING = 120;
    public static final int MOUSEICON_MORE = 110;
    public static final int MOUSEICON_MOVE = 105;
    public static final int MOUSEICON_POINTER_01 = 111;
    public static final int MOUSEICON_POINTER_02 = 112;
    public static final int MOUSEICON_POINTER_03 = 113;
    public static final int MOUSEICON_POINTER_04 = 114;
    public static final int MOUSEICON_POINTER_05 = 115;
    public static final int MOUSEICON_POINTER_06 = 116;
    public static final int MOUSEICON_POINTER_07 = 117;
    public static final int MOUSEICON_POINTER_08 = 118;
    public static final int MOUSEICON_RESIZE_01 = 106;
    public static final int MOUSEICON_RESIZE_02 = 107;
    public static final int MOUSEICON_RESIZE_03 = 108;
    public static final int MOUSEICON_RESIZE_04 = 109;
    public static final int MOUSEICON_SPLIT_01 = 103;
    public static final int MOUSEICON_SPLIT_02 = 104;
    public static final int MOUSEICON_TRANSPARENT = 119;
    private static final int MOUSE_CUSTOM_ICON_MAX_COUNT = 5;
    private static final int MOUSE_CUSTOM_ICON_MAX_SIZE = 40000;
    private static final int MOUSE_CUSTOM_ICON_RESIZE_X = 100;
    public static final int STYLE_ARROW = 1000;
    public static final int STYLE_ARROW_BIG = 1001;
    public static final int STYLE_CUSTOM = -1;
    private static final int STYLE_DEFAULT = 1000;
    public static final int STYLE_NULL = 0;
    private static final int STYLE_OEM_FIRST = 10000;
    public static final int STYLE_SPOT_ANCHOR = 2002;
    public static final int STYLE_SPOT_FINGERHOVER = 10002;
    public static final int STYLE_SPOT_HOVER = 2000;
    public static final int STYLE_SPOT_HOVERING_SPEN = 10001;
    public static final int STYLE_SPOT_TOUCH = 2001;
    private static final String TAG = "PointerIcon";
    private static final PointerIcon gNullIcon = new PointerIcon(0);
    private static Context mContext;
    private static Point mCustomHotSpotPoint = null;
    private static int mCustomIconCurrentId = 0;
    private static int mCustomIconId = -1;
    private static Bitmap[] mHoverBitmap;
    private static HoverEffect mHoverEffect;
    private static int mIconType = -1;
    private static volatile Object mLock = new Object();
    private static volatile Object mLockforMouse = new Object();
    private static Bitmap[] mMouseBitmap;
    private static Point mMouseCustomHotSpotPoint = null;
    private static int mMouseCustomIconCurrentId = -1;
    private static int mMouseCustomIconId;
    private static int mMouseIconType = -1;
    public static boolean mThemeApplied = false;
    private static IInputManager sInputManager;
    private static final Object sStaticInitInput = new Object();
    private static final Object sStaticInitWindow = new Object();
    private Bitmap mBitmap;
    private float mHotSpotX;
    private float mHotSpotY;
    private final int mStyle;
    private int mSystemIconResourceId;

    private static class HoverEffect {
        private SoundPool mHoverSound;
        private int mSoundId;
        private final String soundURIs;

        private HoverEffect() {
            this.soundURIs = "/media/audio/ui/HoverPointer.ogg";
            this.mSoundId = -1;
        }

        void playSound(int type) {
            if (this.mHoverSound == null) {
                this.mHoverSound = new SoundPool(1, 1, 0);
            }
            if (this.mSoundId == -1) {
                this.mSoundId = this.mHoverSound.load(Environment.getRootDirectory() + "/media/audio/ui/HoverPointer.ogg", 1);
            }
            int ret = this.mHoverSound.play(this.mSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    private PointerIcon(int style) {
        this.mStyle = style;
    }

    public static PointerIcon getNullIcon() {
        return gNullIcon;
    }

    public static PointerIcon getDefaultIcon(Context context) {
        return getSystemIcon(context, 1000);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.view.PointerIcon getSystemIcon(android.content.Context r12, int r13) {
        /*
        r11 = 1001; // 0x3e9 float:1.403E-42 double:4.946E-321;
        r10 = -1;
        r8 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r7 = 0;
        if (r12 != 0) goto L_0x0012;
    L_0x000a:
        r4 = new java.lang.IllegalArgumentException;
        r5 = "context must not be null";
        r4.<init>(r5);
        throw r4;
    L_0x0012:
        if (r13 != 0) goto L_0x0017;
    L_0x0014:
        r4 = gNullIcon;
    L_0x0016:
        return r4;
    L_0x0017:
        r4 = mContext;
        if (r4 != 0) goto L_0x001d;
    L_0x001b:
        mContext = r12;
    L_0x001d:
        r5 = mLock;
        monitor-enter(r5);
        if (r13 != r8) goto L_0x006a;
    L_0x0022:
        r4 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        if (r4 == 0) goto L_0x006a;
    L_0x0026:
        r4 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        r6 = 0;
        r4 = r4[r6];	 Catch:{ all -> 0x0052 }
        if (r4 == 0) goto L_0x006a;
    L_0x002d:
        r4 = mCustomHotSpotPoint;	 Catch:{ all -> 0x0052 }
        if (r4 != 0) goto L_0x0055;
    L_0x0031:
        r4 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        r6 = 0;
        r4 = r4[r6];	 Catch:{ all -> 0x0052 }
        r6 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        r7 = 0;
        r6 = r6[r7];	 Catch:{ all -> 0x0052 }
        r6 = r6.getWidth();	 Catch:{ all -> 0x0052 }
        r6 = (float) r6;	 Catch:{ all -> 0x0052 }
        r6 = r6 / r9;
        r7 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        r8 = 0;
        r7 = r7[r8];	 Catch:{ all -> 0x0052 }
        r7 = r7.getHeight();	 Catch:{ all -> 0x0052 }
        r7 = (float) r7;	 Catch:{ all -> 0x0052 }
        r7 = r7 / r9;
        r4 = createCustomIcon(r4, r6, r7);	 Catch:{ all -> 0x0052 }
        monitor-exit(r5);	 Catch:{ all -> 0x0052 }
        goto L_0x0016;
    L_0x0052:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0052 }
        throw r4;
    L_0x0055:
        r4 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        r6 = 0;
        r4 = r4[r6];	 Catch:{ all -> 0x0052 }
        r6 = mCustomHotSpotPoint;	 Catch:{ all -> 0x0052 }
        r6 = r6.x;	 Catch:{ all -> 0x0052 }
        r6 = (float) r6;	 Catch:{ all -> 0x0052 }
        r7 = mCustomHotSpotPoint;	 Catch:{ all -> 0x0052 }
        r7 = r7.y;	 Catch:{ all -> 0x0052 }
        r7 = (float) r7;	 Catch:{ all -> 0x0052 }
        r4 = createCustomIcon(r4, r6, r7);	 Catch:{ all -> 0x0052 }
        monitor-exit(r5);	 Catch:{ all -> 0x0052 }
        goto L_0x0016;
    L_0x006a:
        r4 = 10001; // 0x2711 float:1.4014E-41 double:4.941E-320;
        if (r13 != r4) goto L_0x0130;
    L_0x006e:
        r4 = mIconType;	 Catch:{ all -> 0x0052 }
        if (r4 != 0) goto L_0x00de;
    L_0x0072:
        r4 = mCustomIconId;	 Catch:{ all -> 0x0052 }
        if (r4 < 0) goto L_0x007b;
    L_0x0076:
        r4 = mCustomIconId;	 Catch:{ all -> 0x0052 }
        r6 = 5;
        if (r4 < r6) goto L_0x0086;
    L_0x007b:
        r4 = 1;
        mCustomIconId = r4;	 Catch:{ all -> 0x0052 }
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r4 = getSystemIcon(r12, r4);	 Catch:{ all -> 0x0052 }
        monitor-exit(r5);	 Catch:{ all -> 0x0052 }
        goto L_0x0016;
    L_0x0086:
        r4 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        r6 = mCustomIconId;	 Catch:{ all -> 0x0052 }
        r4 = r4[r6];	 Catch:{ all -> 0x0052 }
        if (r4 != 0) goto L_0x009e;
    L_0x008e:
        r4 = "PointerIcon";
        r6 = "getSystemIcon mHoverBitmap is null";
        android.util.Log.e(r4, r6);	 Catch:{ all -> 0x0052 }
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r4 = getSystemIcon(r12, r4);	 Catch:{ all -> 0x0052 }
        monitor-exit(r5);	 Catch:{ all -> 0x0052 }
        goto L_0x0016;
    L_0x009e:
        r4 = mCustomHotSpotPoint;	 Catch:{ all -> 0x0052 }
        if (r4 != 0) goto L_0x00c7;
    L_0x00a2:
        r4 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        r6 = mCustomIconId;	 Catch:{ all -> 0x0052 }
        r4 = r4[r6];	 Catch:{ all -> 0x0052 }
        r6 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        r7 = mCustomIconId;	 Catch:{ all -> 0x0052 }
        r6 = r6[r7];	 Catch:{ all -> 0x0052 }
        r6 = r6.getWidth();	 Catch:{ all -> 0x0052 }
        r6 = (float) r6;	 Catch:{ all -> 0x0052 }
        r6 = r6 / r9;
        r7 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        r8 = mCustomIconId;	 Catch:{ all -> 0x0052 }
        r7 = r7[r8];	 Catch:{ all -> 0x0052 }
        r7 = r7.getHeight();	 Catch:{ all -> 0x0052 }
        r7 = (float) r7;	 Catch:{ all -> 0x0052 }
        r7 = r7 / r9;
        r4 = createCustomIcon(r4, r6, r7);	 Catch:{ all -> 0x0052 }
        monitor-exit(r5);	 Catch:{ all -> 0x0052 }
        goto L_0x0016;
    L_0x00c7:
        r4 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        r6 = mCustomIconId;	 Catch:{ all -> 0x0052 }
        r4 = r4[r6];	 Catch:{ all -> 0x0052 }
        r6 = mCustomHotSpotPoint;	 Catch:{ all -> 0x0052 }
        r6 = r6.x;	 Catch:{ all -> 0x0052 }
        r6 = (float) r6;	 Catch:{ all -> 0x0052 }
        r7 = mCustomHotSpotPoint;	 Catch:{ all -> 0x0052 }
        r7 = r7.y;	 Catch:{ all -> 0x0052 }
        r7 = (float) r7;	 Catch:{ all -> 0x0052 }
        r4 = createCustomIcon(r4, r6, r7);	 Catch:{ all -> 0x0052 }
        monitor-exit(r5);	 Catch:{ all -> 0x0052 }
        goto L_0x0016;
    L_0x00de:
        r4 = mIconType;	 Catch:{ all -> 0x0052 }
        r6 = 22;
        if (r4 == r6) goto L_0x00e9;
    L_0x00e4:
        r4 = mIconType;	 Catch:{ all -> 0x0052 }
        r6 = 1;
        if (r4 != r6) goto L_0x0130;
    L_0x00e9:
        r4 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        if (r4 == 0) goto L_0x0130;
    L_0x00ed:
        r4 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        r6 = 0;
        r4 = r4[r6];	 Catch:{ all -> 0x0052 }
        if (r4 == 0) goto L_0x0130;
    L_0x00f4:
        r4 = mCustomHotSpotPoint;	 Catch:{ all -> 0x0052 }
        if (r4 != 0) goto L_0x011a;
    L_0x00f8:
        r4 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        r6 = 0;
        r4 = r4[r6];	 Catch:{ all -> 0x0052 }
        r6 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        r7 = 0;
        r6 = r6[r7];	 Catch:{ all -> 0x0052 }
        r6 = r6.getWidth();	 Catch:{ all -> 0x0052 }
        r6 = (float) r6;	 Catch:{ all -> 0x0052 }
        r6 = r6 / r9;
        r7 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        r8 = 0;
        r7 = r7[r8];	 Catch:{ all -> 0x0052 }
        r7 = r7.getHeight();	 Catch:{ all -> 0x0052 }
        r7 = (float) r7;	 Catch:{ all -> 0x0052 }
        r7 = r7 / r9;
        r4 = createCustomIcon(r4, r6, r7);	 Catch:{ all -> 0x0052 }
        monitor-exit(r5);	 Catch:{ all -> 0x0052 }
        goto L_0x0016;
    L_0x011a:
        r4 = mHoverBitmap;	 Catch:{ all -> 0x0052 }
        r6 = 0;
        r4 = r4[r6];	 Catch:{ all -> 0x0052 }
        r6 = mCustomHotSpotPoint;	 Catch:{ all -> 0x0052 }
        r6 = r6.x;	 Catch:{ all -> 0x0052 }
        r6 = (float) r6;	 Catch:{ all -> 0x0052 }
        r7 = mCustomHotSpotPoint;	 Catch:{ all -> 0x0052 }
        r7 = r7.y;	 Catch:{ all -> 0x0052 }
        r7 = (float) r7;	 Catch:{ all -> 0x0052 }
        r4 = createCustomIcon(r4, r6, r7);	 Catch:{ all -> 0x0052 }
        monitor-exit(r5);	 Catch:{ all -> 0x0052 }
        goto L_0x0016;
    L_0x0130:
        monitor-exit(r5);	 Catch:{ all -> 0x0052 }
        r5 = mLockforMouse;
        monitor-enter(r5);
        if (r13 != r11) goto L_0x01a5;
    L_0x0136:
        r4 = mMouseIconType;	 Catch:{ all -> 0x0151 }
        r6 = 100;
        if (r4 != r6) goto L_0x01a5;
    L_0x013c:
        r4 = mMouseCustomIconId;	 Catch:{ all -> 0x0151 }
        if (r4 < 0) goto L_0x0145;
    L_0x0140:
        r4 = mMouseCustomIconId;	 Catch:{ all -> 0x0151 }
        r6 = 5;
        if (r4 < r6) goto L_0x0154;
    L_0x0145:
        r4 = 0;
        mMouseCustomIconId = r4;	 Catch:{ all -> 0x0151 }
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r4 = getSystemIcon(r12, r4);	 Catch:{ all -> 0x0151 }
        monitor-exit(r5);	 Catch:{ all -> 0x0151 }
        goto L_0x0016;
    L_0x0151:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0151 }
        throw r4;
    L_0x0154:
        r4 = mMouseBitmap;	 Catch:{ all -> 0x0151 }
        r6 = mMouseCustomIconId;	 Catch:{ all -> 0x0151 }
        r4 = r4[r6];	 Catch:{ all -> 0x0151 }
        if (r4 != 0) goto L_0x0165;
    L_0x015c:
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r4 = getSystemIcon(r12, r4);	 Catch:{ all -> 0x0151 }
        monitor-exit(r5);	 Catch:{ all -> 0x0151 }
        goto L_0x0016;
    L_0x0165:
        r4 = mMouseCustomHotSpotPoint;	 Catch:{ all -> 0x0151 }
        if (r4 != 0) goto L_0x018e;
    L_0x0169:
        r4 = mMouseBitmap;	 Catch:{ all -> 0x0151 }
        r6 = mMouseCustomIconId;	 Catch:{ all -> 0x0151 }
        r4 = r4[r6];	 Catch:{ all -> 0x0151 }
        r6 = mMouseBitmap;	 Catch:{ all -> 0x0151 }
        r7 = mMouseCustomIconId;	 Catch:{ all -> 0x0151 }
        r6 = r6[r7];	 Catch:{ all -> 0x0151 }
        r6 = r6.getWidth();	 Catch:{ all -> 0x0151 }
        r6 = (float) r6;	 Catch:{ all -> 0x0151 }
        r6 = r6 / r9;
        r7 = mMouseBitmap;	 Catch:{ all -> 0x0151 }
        r8 = mMouseCustomIconId;	 Catch:{ all -> 0x0151 }
        r7 = r7[r8];	 Catch:{ all -> 0x0151 }
        r7 = r7.getHeight();	 Catch:{ all -> 0x0151 }
        r7 = (float) r7;	 Catch:{ all -> 0x0151 }
        r7 = r7 / r9;
        r4 = createCustomIcon(r4, r6, r7);	 Catch:{ all -> 0x0151 }
        monitor-exit(r5);	 Catch:{ all -> 0x0151 }
        goto L_0x0016;
    L_0x018e:
        r4 = mMouseBitmap;	 Catch:{ all -> 0x0151 }
        r6 = mMouseCustomIconId;	 Catch:{ all -> 0x0151 }
        r4 = r4[r6];	 Catch:{ all -> 0x0151 }
        r6 = mMouseCustomHotSpotPoint;	 Catch:{ all -> 0x0151 }
        r6 = r6.x;	 Catch:{ all -> 0x0151 }
        r6 = (float) r6;	 Catch:{ all -> 0x0151 }
        r7 = mMouseCustomHotSpotPoint;	 Catch:{ all -> 0x0151 }
        r7 = r7.y;	 Catch:{ all -> 0x0151 }
        r7 = (float) r7;	 Catch:{ all -> 0x0151 }
        r4 = createCustomIcon(r4, r6, r7);	 Catch:{ all -> 0x0151 }
        monitor-exit(r5);	 Catch:{ all -> 0x0151 }
        goto L_0x0016;
    L_0x01a5:
        monitor-exit(r5);	 Catch:{ all -> 0x0151 }
        r3 = getSystemIconStyleIndex(r13);
        if (r3 != 0) goto L_0x01b6;
    L_0x01ac:
        r4 = 10001; // 0x2711 float:1.4014E-41 double:4.941E-320;
        if (r13 == r4) goto L_0x01b6;
    L_0x01b0:
        if (r13 == r11) goto L_0x01b6;
    L_0x01b2:
        r3 = getSystemIconStyleIndex(r8);
    L_0x01b6:
        r4 = 10001; // 0x2711 float:1.4014E-41 double:4.941E-320;
        if (r13 == r4) goto L_0x01bc;
    L_0x01ba:
        if (r13 != r11) goto L_0x01ed;
    L_0x01bc:
        r4 = 0;
        r5 = com.android.internal.R.styleable.DeviceDefault_Pointer;
        r6 = 18219195; // 0x11600bb float:2.7551173E-38 double:9.0014783E-317;
        r0 = r12.obtainStyledAttributes(r4, r5, r6, r7);
        r2 = r0.getResourceId(r3, r10);
        r0.recycle();
    L_0x01cd:
        if (r2 != r10) goto L_0x0205;
    L_0x01cf:
        r4 = "PointerIcon";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Missing theme resources for pointer icon style ";
        r5 = r5.append(r6);
        r5 = r5.append(r13);
        r5 = r5.toString();
        android.util.Log.w(r4, r5);
        if (r13 != r8) goto L_0x01ff;
    L_0x01e9:
        r4 = gNullIcon;
        goto L_0x0016;
    L_0x01ed:
        r4 = 0;
        r5 = com.android.internal.R.styleable.Pointer;
        r6 = 18219056; // 0x1160030 float:2.7550783E-38 double:9.0014097E-317;
        r0 = r12.obtainStyledAttributes(r4, r5, r6, r7);
        r2 = r0.getResourceId(r3, r10);
        r0.recycle();
        goto L_0x01cd;
    L_0x01ff:
        r4 = getSystemIcon(r12, r8);
        goto L_0x0016;
    L_0x0205:
        r1 = new android.view.PointerIcon;
        r1.<init>(r13);
        r4 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r4 = r4 & r2;
        r5 = 16777216; // 0x1000000 float:2.3509887E-38 double:8.289046E-317;
        if (r4 != r5) goto L_0x0216;
    L_0x0211:
        r1.mSystemIconResourceId = r2;
    L_0x0213:
        r4 = r1;
        goto L_0x0016;
    L_0x0216:
        r4 = r12.getResources();
        r1.loadResource(r12, r4, r2);
        goto L_0x0213;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.PointerIcon.getSystemIcon(android.content.Context, int):android.view.PointerIcon");
    }

    public static PointerIcon createCustomIcon(Bitmap bitmap, float hotSpotX, float hotSpotY) {
        if (bitmap == null) {
            throw new IllegalArgumentException("bitmap must not be null");
        }
        validateHotSpot(bitmap, hotSpotX, hotSpotY);
        PointerIcon icon = new PointerIcon(-1);
        icon.mBitmap = bitmap;
        icon.mHotSpotX = hotSpotX;
        icon.mHotSpotY = hotSpotY;
        return icon;
    }

    public static PointerIcon loadCustomIcon(Resources resources, int resourceId) {
        if (resources == null) {
            throw new IllegalArgumentException("resources must not be null");
        }
        PointerIcon icon = new PointerIcon(-1);
        icon.loadResource(null, resources, resourceId);
        return icon;
    }

    public PointerIcon load(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        } else if (this.mSystemIconResourceId == 0 || this.mBitmap != null) {
            return this;
        } else {
            PointerIcon result = new PointerIcon(this.mStyle);
            result.mSystemIconResourceId = this.mSystemIconResourceId;
            result.loadResource(context, context.getResources(), this.mSystemIconResourceId);
            return result;
        }
    }

    public boolean isNullIcon() {
        return this.mStyle == 0;
    }

    public boolean isLoaded() {
        return this.mBitmap != null || this.mStyle == 0;
    }

    public int getStyle() {
        return this.mStyle;
    }

    public Bitmap getBitmap() {
        throwIfIconIsNotLoaded();
        return this.mBitmap;
    }

    public float getHotSpotX() {
        throwIfIconIsNotLoaded();
        return this.mHotSpotX;
    }

    public float getHotSpotY() {
        throwIfIconIsNotLoaded();
        return this.mHotSpotY;
    }

    private void throwIfIconIsNotLoaded() {
        if (!isLoaded()) {
            throw new IllegalStateException("The icon is not loaded.");
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mStyle);
        if (this.mStyle != 0) {
            out.writeInt(this.mSystemIconResourceId);
            if (this.mSystemIconResourceId == 0) {
                this.mBitmap.writeToParcel(out, flags);
                out.writeFloat(this.mHotSpotX);
                out.writeFloat(this.mHotSpotY);
            }
        }
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof PointerIcon)) {
            return false;
        }
        PointerIcon otherIcon = (PointerIcon) other;
        if (this.mStyle != otherIcon.mStyle || this.mSystemIconResourceId != otherIcon.mSystemIconResourceId) {
            return false;
        }
        if (this.mSystemIconResourceId != 0) {
            return true;
        }
        if (this.mBitmap == otherIcon.mBitmap && this.mHotSpotX == otherIcon.mHotSpotX && this.mHotSpotY == otherIcon.mHotSpotY) {
            return true;
        }
        return false;
    }

    private void loadResource(Context context, Resources resources, int resourceId) {
        XmlResourceParser parser = resources.getXml(resourceId);
        try {
            XmlUtils.beginDocument(parser, "pointer-icon");
            TypedArray a = resources.obtainAttributes(parser, R.styleable.PointerIcon);
            int bitmapRes = a.getResourceId(0, 0);
            float hotSpotX = a.getDimension(1, 0.0f);
            float hotSpotY = a.getDimension(2, 0.0f);
            a.recycle();
            parser.close();
            if (bitmapRes == 0) {
                throw new IllegalArgumentException("<pointer-icon> is missing bitmap attribute.");
            }
            Drawable drawable;
            if (context == null) {
                drawable = resources.getDrawable(bitmapRes);
            } else {
                drawable = context.getDrawable(bitmapRes);
            }
            if (drawable instanceof BitmapDrawable) {
                this.mBitmap = ((BitmapDrawable) drawable).getBitmap();
                this.mHotSpotX = hotSpotX;
                this.mHotSpotY = hotSpotY;
                return;
            }
            throw new IllegalArgumentException("<pointer-icon> bitmap attribute must refer to a bitmap drawable.");
        } catch (Exception ex) {
            throw new IllegalArgumentException("Exception parsing pointer icon resource.", ex);
        } catch (Throwable th) {
            parser.close();
        }
    }

    private static void validateHotSpot(Bitmap bitmap, float hotSpotX, float hotSpotY) {
        if (hotSpotX < 0.0f || hotSpotX >= ((float) bitmap.getWidth())) {
            throw new IllegalArgumentException("x hotspot lies outside of the bitmap area");
        } else if (hotSpotY < 0.0f || hotSpotY >= ((float) bitmap.getHeight())) {
            throw new IllegalArgumentException("y hotspot lies outside of the bitmap area");
        }
    }

    private static int getSystemIconStyleIndex(int style) {
        switch (style) {
            case 1001:
                switch (mMouseIconType) {
                    case 101:
                        return 19;
                    case 102:
                        return 20;
                    case 103:
                        return 21;
                    case 104:
                        return 22;
                    case 105:
                        return 23;
                    case 106:
                        return 24;
                    case 107:
                        return 25;
                    case 108:
                        return 26;
                    case 109:
                        return 27;
                    case 110:
                        return 28;
                    case 111:
                        return 29;
                    case 112:
                        return 30;
                    case 113:
                        return 31;
                    case 114:
                        return 32;
                    case 115:
                        return 33;
                    case 116:
                        return 34;
                    case 117:
                        return 35;
                    case 118:
                        return 36;
                    case 119:
                        return 37;
                    case 120:
                        return 38;
                    default:
                        return 19;
                }
            case 2000:
                return 1;
            case 2001:
                return 2;
            case 2002:
                return 3;
            case 10001:
                Log.d(TAG, "getSystemIconStyleIndex style: " + style);
                switch (mIconType) {
                    case 2:
                        return 11;
                    case 3:
                        return 12;
                    case 4:
                        return 13;
                    case 5:
                        return 14;
                    case 6:
                        return 15;
                    case 7:
                        return 16;
                    case 8:
                        return 17;
                    case 9:
                        return 18;
                    case 10:
                        return 10;
                    case 11:
                        return 0;
                    case 12:
                        return 1;
                    case 13:
                        return 2;
                    case 14:
                        return 3;
                    case 15:
                        return 4;
                    case 16:
                        return 5;
                    case 17:
                        return 6;
                    case 18:
                        return 7;
                    case 19:
                        return 8;
                    case 21:
                        return 39;
                    default:
                        return 9;
                }
            default:
                return 0;
        }
    }

    public static void setCustomDefaultIcon(int deviceType, Drawable customIcon) throws RemoteException {
        Log.d(TAG, "setCustomDefaultIcon(), deviceType: " + deviceType + ", iconType is CUSTOM");
        if (customIcon == null) {
            Log.d(TAG, "setCustomDefaultIcon(), invalid customIcon.");
        } else if (deviceType == 2) {
            setHoveringSpenIcon(22, customIcon);
        } else if (deviceType == 3) {
            setMouseIcon(100, customIcon);
        } else {
            Log.d(TAG, "setCustomDefaultIcon(), invalid device type.");
        }
    }

    public static void setDisableCustomDefaultIcon() throws RemoteException {
        Log.d(TAG, "setDisableCustomDefaultIcon()");
        setHoveringSpenIcon(23, null);
    }

    public static void setIcon(int deviceType, int iconType) throws RemoteException {
        Log.d(TAG, "setIcon(), deviceType: " + deviceType + ", iconType: " + iconType);
        if (deviceType == 2) {
            setHoveringSpenIcon(iconType, -1);
        } else if (deviceType == 3) {
            setMouseIcon(iconType, -1);
        } else {
            Log.d(TAG, "setIcon(), invalid device type.");
        }
    }

    public static void setIcon(int deviceType, Drawable customIcon) throws RemoteException {
        Log.d(TAG, "setIcon(), deviceType: " + deviceType + ", iconType is CUSTOM");
        if (deviceType == 2) {
            setHoveringSpenIcon(0, customIcon);
        } else if (deviceType == 3) {
            setMouseIcon(100, customIcon);
        } else {
            Log.d(TAG, "setIcon(), invalid device type.");
        }
    }

    public static void setIcon(int deviceType, Drawable customIcon, Point hotSpotPoint) throws RemoteException {
        Log.d(TAG, "setIcon(), deviceType: " + deviceType + ", iconType is CUSTOM, " + hotSpotPoint);
        if (deviceType == 2) {
            setHoveringSpenIcon(0, customIcon, hotSpotPoint);
        } else if (deviceType == 3) {
            setMouseIcon(100, customIcon, hotSpotPoint);
        } else {
            Log.d(TAG, "setIcon(), invalid device type.");
        }
    }

    public static int registerMouseCustomIcon(Drawable d) throws RemoteException {
        Log.d(TAG, "registerMouseCustomIcon");
        if (d == null) {
            Log.e(TAG, "registerMouseCustomIcon Drawable is null");
            return -1;
        }
        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        d.draw(canvas);
        if ((bitmap.getWidth() * bitmap.getHeight()) * 4 > ContextAwareConstants.SENSOR_STATUS_CHECK_ACC_DATA_DEFAULT) {
            Log.d(TAG, "registerMouseCustomIcon size too big width: " + bitmap.getWidth() + "height: " + bitmap.getHeight());
            float scale = 100.0f / ((float) bitmap.getWidth());
            Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, (int) (((float) bitmap.getWidth()) * scale), (int) (((float) bitmap.getHeight()) * scale), false);
            bitmap.recycle();
            bitmap = resizeBitmap;
        }
        return getInputManager().registerMouseCustomIcon(bitmap);
    }

    public static void removeMouseCustomIcon(int customIconId) throws RemoteException {
        Log.d(TAG, "removeMouseCustomIcon" + customIconId);
        getInputManager().removeMouseCustomIcon(customIconId);
    }

    public static void setMouseIcon(int iconType, int customIconId) throws RemoteException {
        setMouseIcon(iconType, customIconId, 0);
    }

    public static void setMouseIcon(int iconType, int customIconId, int flag) throws RemoteException {
        getInputManager().reloadMousePointerIcon(1001, iconType, customIconId, null, flag);
    }

    public static void setMouseIcon(int iconType, int customIconId, Point hotSpotPoint) throws RemoteException {
        setMouseIcon(iconType, customIconId, hotSpotPoint, 0);
    }

    public static void setMouseIcon(int iconType, int customIconId, Point hotSpotPoint, int flag) throws RemoteException {
        getInputManager().reloadMousePointerIcon(1001, iconType, customIconId, hotSpotPoint, flag);
    }

    public static int setMouseIcon(int iconType, Drawable customIcon) throws RemoteException {
        return setMouseIcon(iconType, customIcon, 0);
    }

    public static int setMouseIcon(int iconType, Drawable customIcon, int flag) throws RemoteException {
        Log.d(TAG, "setMouseIcon2 iconType: " + iconType);
        Bitmap bitmap = Bitmap.createBitmap(customIcon.getIntrinsicWidth(), customIcon.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        customIcon.setBounds(0, 0, customIcon.getIntrinsicWidth(), customIcon.getIntrinsicHeight());
        customIcon.draw(canvas);
        if ((bitmap.getWidth() * bitmap.getHeight()) * 4 > ContextAwareConstants.SENSOR_STATUS_CHECK_ACC_DATA_DEFAULT) {
            Log.d(TAG, "registerMouseCustomIcon size too big width: " + bitmap.getWidth() + "height: " + bitmap.getHeight());
            Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            bitmap.recycle();
            bitmap = resizeBitmap;
        }
        return getInputManager().reloadMousePointerIconForBitmap(1001, iconType, bitmap, null, flag);
    }

    public static int setMouseIcon(int iconType, Drawable customIcon, Point hotSpotPoint) throws RemoteException {
        return setMouseIcon(iconType, customIcon, hotSpotPoint, 0);
    }

    public static int setMouseIcon(int iconType, Drawable customIcon, Point hotSpotPoint, int flag) throws RemoteException {
        Log.d(TAG, "setMouseIcon with hotSpotPoint iconType: " + iconType);
        Bitmap bitmap = Bitmap.createBitmap(customIcon.getIntrinsicWidth(), customIcon.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        customIcon.setBounds(0, 0, customIcon.getIntrinsicWidth(), customIcon.getIntrinsicHeight());
        customIcon.draw(canvas);
        if ((bitmap.getWidth() * bitmap.getHeight()) * 4 > ContextAwareConstants.SENSOR_STATUS_CHECK_ACC_DATA_DEFAULT) {
            Log.d(TAG, "registerMouseCustomIcon size too big width: " + bitmap.getWidth() + "height: " + bitmap.getHeight());
            Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            bitmap.recycle();
            bitmap = resizeBitmap;
        }
        return getInputManager().reloadMousePointerIconForBitmap(1001, iconType, bitmap, hotSpotPoint, flag);
    }

    public static boolean setMousePointerIcon(int iconType, int customIconId, Point hotSpotPoint) {
        if (mMouseIconType == 100 || mMouseIconType != iconType) {
            synchronized (mLockforMouse) {
                mMouseIconType = iconType;
                mMouseCustomIconId = customIconId;
                mMouseCustomHotSpotPoint = hotSpotPoint;
            }
            return true;
        }
        Log.d(TAG, "setMouseCustomIcon IconType is same." + mMouseIconType);
        return false;
    }

    public static int setMouseCustomIcon(Bitmap bitmap) {
        int i;
        synchronized (mLockforMouse) {
            if (mMouseCustomIconCurrentId >= 5) {
                mMouseCustomIconCurrentId = 0;
            } else {
                mMouseCustomIconCurrentId++;
            }
            if (mMouseBitmap == null) {
                mMouseBitmap = new Bitmap[5];
            } else {
                Log.d(TAG, "MouseBitmap instance already exist");
            }
            if (mMouseBitmap[mMouseCustomIconCurrentId] != null) {
                mMouseBitmap[mMouseCustomIconCurrentId].recycle();
            }
            mMouseBitmap[mMouseCustomIconCurrentId] = bitmap;
            i = mMouseCustomIconCurrentId;
        }
        return i;
    }

    public static int setMouseCustomIcon(int IconId, Bitmap bitmap) {
        int iReaIconId;
        synchronized (mLockforMouse) {
            iReaIconId = IconId;
            if (iReaIconId < 0) {
                iReaIconId = mMouseCustomIconId;
            }
            if (iReaIconId < 0) {
                iReaIconId = 0;
            }
            if (mMouseBitmap == null) {
                mMouseBitmap = new Bitmap[5];
            } else {
                Log.d(TAG, "MouseBitmap instance already exist");
            }
            if (mMouseBitmap[iReaIconId] != null) {
                mMouseBitmap[iReaIconId].recycle();
            }
            mMouseBitmap[iReaIconId] = bitmap;
        }
        return iReaIconId;
    }

    public static int registerHoveringSpenCustomIcon(Drawable d) throws RemoteException {
        Log.d(TAG, "registerHoveringSpenCustomIcon");
        if (d == null) {
            Log.e(TAG, "registerHoveringSpenCustomIcon Drawable is null");
            return -1;
        }
        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        d.draw(canvas);
        if ((bitmap.getWidth() * bitmap.getHeight()) * 4 > ContextAwareConstants.SENSOR_STATUS_CHECK_ACC_DATA_DEFAULT) {
            Log.d(TAG, "registerHoveringSpenCustomIcon size too big width: " + bitmap.getWidth() + "height:" + bitmap.getHeight());
            float scale = 100.0f / ((float) bitmap.getWidth());
            Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, (int) (((float) bitmap.getWidth()) * scale), (int) (((float) bitmap.getHeight()) * scale), false);
            bitmap.recycle();
            bitmap = resizeBitmap;
        }
        return getInputManager().registerHoveringSpenCustomIcon(bitmap);
    }

    public static void removeHoveringSpenCustomIcon(int customIconId) throws RemoteException {
        Log.d(TAG, "removeHoveringSpenCustomIcon" + customIconId);
        getInputManager().removeHoveringSpenCustomIcon(customIconId);
    }

    public static void setHoveringSpenIcon(int iconType, int customIconId) throws RemoteException {
        setHoveringSpenIcon(iconType, customIconId, 0);
    }

    public static void setHoveringSpenIcon(int iconType, int customIconId, int flag) throws RemoteException {
        getInputManager().reloadPointerIcon(10001, iconType, customIconId, null, flag);
    }

    public static void setHoveringSpenIcon(int iconType, int customIconId, Point hotSpotPoint) throws RemoteException {
        setHoveringSpenIcon(iconType, customIconId, hotSpotPoint, 0);
    }

    public static void setHoveringSpenIcon(int iconType, int customIconId, Point hotSpotPoint, int flag) throws RemoteException {
        getInputManager().reloadPointerIcon(10001, iconType, customIconId, hotSpotPoint, flag);
    }

    public static int setHoveringSpenIcon(int iconType, Drawable customIcon) throws RemoteException {
        return setHoveringSpenIcon(iconType, customIcon, 0);
    }

    public static int setHoveringSpenIcon(int iconType, Drawable customIcon, int flag) throws RemoteException {
        Log.d(TAG, "setHoveringSpenIcon2 iconType: " + iconType);
        if (iconType == 23) {
            return getInputManager().reloadPointerIconForBitmap(10001, iconType, null, null, flag);
        }
        Bitmap bitmap = Bitmap.createBitmap(customIcon.getIntrinsicWidth(), customIcon.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        customIcon.setBounds(0, 0, customIcon.getIntrinsicWidth(), customIcon.getIntrinsicHeight());
        customIcon.draw(canvas);
        if ((bitmap.getWidth() * bitmap.getHeight()) * 4 > ContextAwareConstants.SENSOR_STATUS_CHECK_ACC_DATA_DEFAULT) {
            Log.d(TAG, "registerHoveringSpenCustomIcon size too big width: " + bitmap.getWidth() + "height:" + bitmap.getHeight());
            Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            bitmap.recycle();
            bitmap = resizeBitmap;
        }
        return getInputManager().reloadPointerIconForBitmap(10001, iconType, bitmap, null, flag);
    }

    public static int setHoveringSpenIcon(int iconType, Drawable customIcon, Point hotSpotPoint) throws RemoteException {
        return setHoveringSpenIcon(iconType, customIcon, hotSpotPoint, 0);
    }

    public static int setHoveringSpenIcon(int iconType, Drawable customIcon, Point hotSpotPoint, int flag) throws RemoteException {
        Log.d(TAG, "setHoveringSpenIcon with hotSpotPoint iconType: " + iconType);
        Bitmap bitmap = Bitmap.createBitmap(customIcon.getIntrinsicWidth(), customIcon.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        customIcon.setBounds(0, 0, customIcon.getIntrinsicWidth(), customIcon.getIntrinsicHeight());
        customIcon.draw(canvas);
        if ((bitmap.getWidth() * bitmap.getHeight()) * 4 > ContextAwareConstants.SENSOR_STATUS_CHECK_ACC_DATA_DEFAULT) {
            Log.d(TAG, "registerHoveringSpenCustomIcon size too big width: " + bitmap.getWidth() + "height:" + bitmap.getHeight());
            Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            bitmap.recycle();
            bitmap = resizeBitmap;
        }
        return getInputManager().reloadPointerIconForBitmap(10001, iconType, bitmap, hotSpotPoint, flag);
    }

    public static boolean setPointerIcon(int iconType, int customIconId, Point hotSpotPoint) {
        if (iconType == 20 && mCustomIconId != -1) {
            return false;
        }
        if (iconType == 20) {
            iconType = 1;
        }
        if (iconType == 22 || iconType == 23) {
            Log.d(TAG, "setHoveringSpenCustomIcon mIconType is " + mIconType + ", iconType is " + iconType);
            return true;
        }
        if (mIconType == 1 && mThemeApplied) {
            Log.d(TAG, "setHoveringSpenCustomIcon : HOVERING_SPENICON_DEFAULT. Theme changed");
            mThemeApplied = false;
        } else if (mIconType != 0 && mIconType == iconType) {
            Log.d(TAG, "setHoveringSpenCustomIcon IconType is same." + mIconType);
            return false;
        }
        boolean bNeedPlaySound = false;
        synchronized (mLock) {
            mIconType = iconType;
            mCustomIconId = customIconId;
            mCustomHotSpotPoint = hotSpotPoint;
        }
        if (iconType == 10) {
            if (!(mContext == null || Settings$System.getInt(mContext.getContentResolver(), Settings$System.SPEN_FEEDBACK_SOUND, 0) == 0 || Settings$System.getInt(mContext.getContentResolver(), Settings$System.SPEN_FEEDBACK_SOUND_AIR_VIEW, 0) == 0)) {
                bNeedPlaySound = true;
            }
            if (!(mContext == null || Settings$System.getInt(mContext.getContentResolver(), Settings$System.PEN_HOVERING_SOUND, 0) == 0)) {
                bNeedPlaySound = true;
            }
            if (!(mContext == null || mContext.getPackageManager() == null || "2015A".equals(SystemProperties.get("ro.build.scafe.version")) || mContext.getPackageManager().getSystemFeatureLevel("com.sec.feature.spen_usp") <= 3)) {
                bNeedPlaySound = false;
            }
            if (bNeedPlaySound) {
                synchronized (mLock) {
                    if (mHoverEffect == null) {
                        mHoverEffect = new HoverEffect();
                    } else {
                        Log.d(TAG, "HoverEffect instance already exist");
                    }
                }
                long token = Binder.clearCallingIdentity();
                try {
                    mHoverEffect.playSound(iconType);
                } catch (Exception e) {
                    Log.w(TAG, "setPointerIcon , Exception is occurred during playSound");
                } finally {
                    Binder.restoreCallingIdentity(token);
                }
            }
        }
        return true;
    }

    public static int setHoveringSpenCustomIcon(Bitmap bitmap) {
        int i;
        synchronized (mLock) {
            if (mCustomIconCurrentId >= 5) {
                mCustomIconCurrentId = 1;
            } else {
                mCustomIconCurrentId++;
            }
            if (mHoverBitmap == null) {
                mHoverBitmap = new Bitmap[5];
            } else {
                Log.d(TAG, "HoverBitmap instance already exist.");
            }
            if (mHoverBitmap[mCustomIconCurrentId] != null) {
                mHoverBitmap[mCustomIconCurrentId].recycle();
            }
            mHoverBitmap[mCustomIconCurrentId] = bitmap;
            i = mCustomIconCurrentId;
        }
        return i;
    }

    public static int setHoveringSpenCustomIcon(int IconId, Bitmap bitmap) {
        int iReaIconId;
        synchronized (mLock) {
            iReaIconId = IconId;
            if (iReaIconId < 0) {
                iReaIconId = mCustomIconId;
            }
            if (iReaIconId < 1) {
                iReaIconId = 1;
            }
            if (mHoverBitmap == null) {
                mHoverBitmap = new Bitmap[5];
            } else {
                Log.d(TAG, "HoverBitmap instance already exist.");
            }
            if (IconId == 255) {
                iReaIconId = 0;
            }
            mHoverBitmap[iReaIconId] = bitmap;
        }
        return iReaIconId;
    }

    static IInputManager getInputManager() {
        IInputManager iInputManager;
        synchronized (sStaticInitInput) {
            if (sInputManager == null) {
                sInputManager = Stub.asInterface(ServiceManager.getService("input"));
            }
            iInputManager = sInputManager;
        }
        return iInputManager;
    }
}
