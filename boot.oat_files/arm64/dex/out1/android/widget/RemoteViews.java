package android.widget;

import android.app.ActivityManagerNative;
import android.app.ActivityThread;
import android.app.Application;
import android.app.PendingIntent;
import android.app.StatusBarManager;
import android.appwidget.AppWidgetHostView;
import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.ImageFilter;
import android.graphics.ImageFilter.DropShadowFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Filter;
import android.view.MotionEvent;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import com.android.internal.R;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import libcore.util.Objects;

public class RemoteViews implements Parcelable, Filter {
    public static final Creator<RemoteViews> CREATOR = new Creator<RemoteViews>() {
        public RemoteViews createFromParcel(Parcel parcel) {
            return new RemoteViews(parcel);
        }

        public RemoteViews[] newArray(int size) {
            return new RemoteViews[size];
        }
    };
    private static final OnClickHandler DEFAULT_ON_CLICK_HANDLER = new OnClickHandler();
    static final String EXTRA_REMOTEADAPTER_APPWIDGET_ID = "remoteAdapterAppWidgetId";
    static final String EXTRA_REMOTEADAPTER_COCKTAIL = "remoteAdapterCocktail";
    private static final String LOG_TAG = "RemoteViews";
    private static final int MODE_HAS_LANDSCAPE_AND_PORTRAIT = 1;
    private static final int MODE_NORMAL = 0;
    private static final int SFE_STARTING_TAG = 21;
    private static HashMap<IBinder, Boolean> mIsLongClick = new HashMap();
    private static final ThreadLocal<Object[]> sInvokeArgsTls = new ThreadLocal<Object[]>() {
        protected Object[] initialValue() {
            return new Object[1];
        }
    };
    private static final ArrayMap<Class<? extends View>, ArrayMap<MutablePair<String, Class<?>>, Method>> sMethods = new ArrayMap();
    private static final Object[] sMethodsLock = new Object[0];
    private boolean isNotification;
    private ArrayList<Action> mActions;
    private final Object mActionsLock;
    private ApplicationInfo mApplication;
    private BitmapCache mBitmapCache;
    private boolean mHas12key;
    private boolean mIsRoot;
    private boolean mIsWidgetCollectionChild;
    private RemoteViews mLandscape;
    private final int mLayoutId;
    private MemoryUsageCounter mMemoryUsageCounter;
    private final MutablePair<String, Class<?>> mPair;
    private RemoteViews mPortrait;
    private Map<CharSequence, CharSequence> stringNamesMap;

    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Config.values().length];

        static {
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ALPHA_8.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.RGB_565.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_4444.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_8888.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private static abstract class Action implements Parcelable {
        public static final int MERGE_APPEND = 1;
        public static final int MERGE_IGNORE = 2;
        public static final int MERGE_REPLACE = 0;
        int viewId;

        public abstract void apply(View view, ViewGroup viewGroup, OnClickHandler onClickHandler) throws ActionException;

        public abstract String getActionName();

        private Action() {
        }

        public int describeContents() {
            return 0;
        }

        public void updateMemoryUsageEstimate(MemoryUsageCounter counter) {
        }

        public void setBitmapCache(BitmapCache bitmapCache) {
        }

        public int mergeBehavior() {
            return 0;
        }

        public String getUniqueKey() {
            return getActionName() + this.viewId;
        }
    }

    public static class ActionException extends RuntimeException {
        public ActionException(Exception ex) {
            super(ex);
        }

        public ActionException(String message) {
            super(message);
        }
    }

    private class AddInnerShadowAction extends Action {
        public static final int TAG = 23;
        float angle;
        float blendingOpacity;
        int color;
        final String methodName = "addInnerShadowTextEffect";
        float offset;
        float softness;

        public AddInnerShadowAction(int viewId, float angle, float offset, float softness, int color, float blendingOpacity) {
            super();
            this.viewId = viewId;
            this.angle = angle;
            this.offset = offset;
            this.softness = softness;
            this.color = color;
            this.blendingOpacity = blendingOpacity;
        }

        public AddInnerShadowAction(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.angle = parcel.readFloat();
            this.offset = parcel.readFloat();
            this.softness = parcel.readFloat();
            this.color = parcel.readInt();
            this.blendingOpacity = parcel.readFloat();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(23);
            dest.writeInt(this.viewId);
            dest.writeFloat(this.angle);
            dest.writeFloat(this.offset);
            dest.writeFloat(this.softness);
            dest.writeInt(this.color);
            dest.writeFloat(this.blendingOpacity);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            TextView target = (TextView) root.findViewById(this.viewId);
            if (target != null) {
                target.addInnerShadowTextEffect(this.angle, this.offset, this.softness, this.color, this.blendingOpacity);
                target.getPaint().setFilterBitmap(true);
            }
        }

        public String getActionName() {
            return "AddInnerShadowAction";
        }
    }

    private class AddLinearGradientAction extends Action {
        public static final int TAG = 25;
        float[] alphas;
        float angle;
        float blendingOpacity;
        int[] colors;
        final String methodName = "addLinearGradientTextEffect";
        float[] positions;
        float scale;

        public AddLinearGradientAction(int viewId, float angle, float scale, int[] colors, float[] alphas, float[] positions, float blendingOpacity) {
            super();
            this.viewId = viewId;
            this.angle = angle;
            this.scale = scale;
            this.colors = colors;
            this.alphas = alphas;
            this.positions = positions;
            this.blendingOpacity = blendingOpacity;
        }

        public AddLinearGradientAction(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.angle = parcel.readFloat();
            this.scale = parcel.readFloat();
            this.colors = parcel.createIntArray();
            this.alphas = parcel.createFloatArray();
            this.positions = parcel.createFloatArray();
            this.blendingOpacity = parcel.readFloat();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(25);
            dest.writeInt(this.viewId);
            dest.writeFloat(this.angle);
            dest.writeFloat(this.scale);
            dest.writeIntArray(this.colors);
            dest.writeFloatArray(this.alphas);
            dest.writeFloatArray(this.positions);
            dest.writeFloat(this.blendingOpacity);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            TextView target = (TextView) root.findViewById(this.viewId);
            if (target != null) {
                target.addLinearGradientTextEffect(this.angle, this.scale, this.colors, this.alphas, this.positions, this.blendingOpacity);
            }
        }

        public String getActionName() {
            return "AddLinearGradientAction";
        }
    }

    private class AddOuterGlowAction extends Action {
        public static final int TAG = 26;
        float blendingOpacity;
        int color;
        final String methodName = "addOuterGlowTextEffect";
        float size;

        public AddOuterGlowAction(int viewId, float size, int color, float blendingOpacity) {
            super();
            this.viewId = viewId;
            this.size = size;
            this.color = color;
            this.blendingOpacity = blendingOpacity;
        }

        public AddOuterGlowAction(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.size = parcel.readFloat();
            this.color = parcel.readInt();
            this.blendingOpacity = parcel.readFloat();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(26);
            dest.writeInt(this.viewId);
            dest.writeFloat(this.size);
            dest.writeInt(this.color);
            dest.writeFloat(this.blendingOpacity);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            TextView target = (TextView) root.findViewById(this.viewId);
            if (target != null) {
                target.addOuterGlowTextEffect(this.size, this.color, this.blendingOpacity);
            }
        }

        public String getActionName() {
            return "AddOuterGlowAction";
        }
    }

    private class AddOuterShadowAction extends Action {
        public static final int TAG = 22;
        float angle;
        float blendingOpacity;
        int color;
        final String methodName = "addOuterShadowTextEffect";
        float offset;
        float softness;

        public AddOuterShadowAction(int viewId, float angle, float offset, float softness, int color, float blendingOpacity) {
            super();
            this.viewId = viewId;
            this.angle = angle;
            this.offset = offset;
            this.softness = softness;
            this.color = color;
            this.blendingOpacity = blendingOpacity;
        }

        public AddOuterShadowAction(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.angle = parcel.readFloat();
            this.offset = parcel.readFloat();
            this.softness = parcel.readFloat();
            this.color = parcel.readInt();
            this.blendingOpacity = parcel.readFloat();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(22);
            dest.writeInt(this.viewId);
            dest.writeFloat(this.angle);
            dest.writeFloat(this.offset);
            dest.writeFloat(this.softness);
            dest.writeInt(this.color);
            dest.writeFloat(this.blendingOpacity);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            TextView target = (TextView) root.findViewById(this.viewId);
            if (target != null) {
                target.addOuterShadowTextEffect(this.angle, this.offset, this.softness, this.color, this.blendingOpacity);
                target.getPaint().setFilterBitmap(true);
            }
        }

        public String getActionName() {
            return "AddOuterShadowAction";
        }
    }

    private class AddStrokeAction extends Action {
        public static final int TAG = 24;
        float blendingOpacity;
        int color;
        final String methodName = "addStrokeTextEffect";
        float size;

        public AddStrokeAction(int viewId, float size, int color, float blendingOpacity) {
            super();
            this.viewId = viewId;
            this.size = size;
            this.color = color;
            this.blendingOpacity = blendingOpacity;
        }

        public AddStrokeAction(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.size = parcel.readFloat();
            this.color = parcel.readInt();
            this.blendingOpacity = parcel.readFloat();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(24);
            dest.writeInt(this.viewId);
            dest.writeFloat(this.size);
            dest.writeInt(this.color);
            dest.writeFloat(this.blendingOpacity);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            TextView target = (TextView) root.findViewById(this.viewId);
            if (target != null) {
                target.addStrokeTextEffect(this.size, this.color, this.blendingOpacity);
                target.getPaint().setFilterBitmap(true);
            }
        }

        public String getActionName() {
            return "AddStrokeAction";
        }
    }

    private static class BitmapCache {
        ArrayList<Bitmap> mBitmaps = new ArrayList();

        public BitmapCache(Parcel source) {
            int count = source.readInt();
            for (int i = 0; i < count; i++) {
                this.mBitmaps.add((Bitmap) Bitmap.CREATOR.createFromParcel(source));
            }
        }

        public int getBitmapId(Bitmap b) {
            if (b == null) {
                return -1;
            }
            if (this.mBitmaps.contains(b)) {
                return this.mBitmaps.indexOf(b);
            }
            this.mBitmaps.add(b);
            return this.mBitmaps.size() - 1;
        }

        public Bitmap getBitmapForId(int id) {
            if (id == -1 || id >= this.mBitmaps.size()) {
                return null;
            }
            return (Bitmap) this.mBitmaps.get(id);
        }

        public void writeBitmapsToParcel(Parcel dest, int flags) {
            int count = this.mBitmaps.size();
            dest.writeInt(count);
            for (int i = 0; i < count; i++) {
                ((Bitmap) this.mBitmaps.get(i)).writeToParcel(dest, flags);
            }
        }

        public void assimilate(BitmapCache bitmapCache) {
            ArrayList<Bitmap> bitmapsToBeAdded = bitmapCache.mBitmaps;
            int count = bitmapsToBeAdded.size();
            for (int i = 0; i < count; i++) {
                Bitmap b = (Bitmap) bitmapsToBeAdded.get(i);
                if (!this.mBitmaps.contains(b)) {
                    this.mBitmaps.add(b);
                }
            }
        }

        public void addBitmapMemory(MemoryUsageCounter memoryCounter) {
            for (int i = 0; i < this.mBitmaps.size(); i++) {
                memoryCounter.addBitmapMemory((Bitmap) this.mBitmaps.get(i));
            }
        }
    }

    private class BitmapReflectionAction extends Action {
        public static final int TAG = 12;
        Bitmap bitmap;
        int bitmapId;
        String methodName;

        BitmapReflectionAction(int viewId, String methodName, Bitmap bitmap) {
            super();
            this.bitmap = bitmap;
            this.viewId = viewId;
            this.methodName = methodName;
            this.bitmapId = RemoteViews.this.mBitmapCache.getBitmapId(bitmap);
        }

        BitmapReflectionAction(Parcel in) {
            super();
            this.viewId = in.readInt();
            this.methodName = in.readString();
            this.bitmapId = in.readInt();
            this.bitmap = RemoteViews.this.mBitmapCache.getBitmapForId(this.bitmapId);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(12);
            dest.writeInt(this.viewId);
            dest.writeString(this.methodName);
            dest.writeInt(this.bitmapId);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) throws ActionException {
            new ReflectionAction(this.viewId, this.methodName, 12, this.bitmap).apply(root, rootParent, handler);
        }

        public void setBitmapCache(BitmapCache bitmapCache) {
            this.bitmapId = bitmapCache.getBitmapId(this.bitmap);
        }

        public String getActionName() {
            return "BitmapReflectionAction";
        }
    }

    private class ClearAllTextEffectAction extends Action {
        public static final int TAG = 21;
        final String methodName = "clearAllTextEffect";

        public ClearAllTextEffectAction(int viewId) {
            super();
            this.viewId = viewId;
        }

        public ClearAllTextEffectAction(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(21);
            dest.writeInt(this.viewId);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            TextView target = (TextView) root.findViewById(this.viewId);
            if (target != null) {
                target.clearAllTextEffect();
            }
        }

        public String getActionName() {
            return "ClearAllTextEffectAction";
        }
    }

    private class ImageFilterAction extends Action {
        public static final int TAG = 31;
        float arg1;
        float arg2;
        float arg3;
        float arg4;
        float arg5;
        float arg6;
        int type;

        public ImageFilterAction(int viewId, int type, float arg1, float arg2, float arg3, float arg4, float arg5, float arg6) {
            super();
            this.viewId = viewId;
            this.type = type;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
            this.arg4 = arg4;
            this.arg5 = arg5;
            this.arg6 = arg6;
        }

        public ImageFilterAction(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.type = parcel.readInt();
            this.arg1 = parcel.readFloat();
            this.arg2 = parcel.readFloat();
            this.arg3 = parcel.readFloat();
            this.arg4 = parcel.readFloat();
            this.arg5 = parcel.readFloat();
            this.arg6 = parcel.readFloat();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(31);
            dest.writeInt(this.viewId);
            dest.writeInt(this.type);
            dest.writeFloat(this.arg1);
            dest.writeFloat(this.arg2);
            dest.writeFloat(this.arg3);
            dest.writeFloat(this.arg4);
            dest.writeFloat(this.arg5);
            dest.writeFloat(this.arg6);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) throws ActionException {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                switch (this.type) {
                    case 55:
                        target.setLayerType(2, null);
                        target.setBackgroundColor(0);
                        DropShadowFilter filter = (DropShadowFilter) ImageFilter.createImageFilter(this.type);
                        filter.setDistance(this.arg1);
                        filter.setAngle(this.arg2);
                        filter.setShadowColor(this.arg3, this.arg4, this.arg5, this.arg6);
                        target.setImageFilter(filter);
                        return;
                    default:
                        return;
                }
            }
        }

        public String getActionName() {
            return "ImageFilterAction";
        }
    }

    private class MemoryUsageCounter {
        int mMemoryUsage;

        private MemoryUsageCounter() {
        }

        public void clear() {
            this.mMemoryUsage = 0;
        }

        public void increment(int numBytes) {
            this.mMemoryUsage += numBytes;
        }

        public int getMemoryUsage() {
            return this.mMemoryUsage;
        }

        public void addBitmapMemory(Bitmap b) {
            Config c = b.getConfig();
            int bpp = 4;
            if (c != null) {
                switch (AnonymousClass4.$SwitchMap$android$graphics$Bitmap$Config[c.ordinal()]) {
                    case 1:
                        bpp = 1;
                        break;
                    case 2:
                    case 3:
                        bpp = 2;
                        break;
                    case 4:
                        bpp = 4;
                        break;
                }
            }
            increment((b.getWidth() * b.getHeight()) * bpp);
        }
    }

    static class MutablePair<F, S> {
        F first;
        S second;

        MutablePair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        public boolean equals(Object o) {
            if (!(o instanceof MutablePair)) {
                return false;
            }
            MutablePair<?, ?> p = (MutablePair) o;
            if (Objects.equal(p.first, this.first) && Objects.equal(p.second, this.second)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int i = 0;
            int hashCode = this.first == null ? 0 : this.first.hashCode();
            if (this.second != null) {
                i = this.second.hashCode();
            }
            return hashCode ^ i;
        }
    }

    public static class OnClickHandler {
        public boolean onClickHandler(View view, PendingIntent pendingIntent, Intent fillInIntent) {
            try {
                view.getContext().startIntentSender(pendingIntent.getIntentSender(), fillInIntent, 268435456, 268435456, 0);
                return true;
            } catch (SendIntentException e) {
                Log.e(RemoteViews.LOG_TAG, "Cannot send pending intent: ", e);
                return false;
            } catch (Exception e2) {
                Log.e(RemoteViews.LOG_TAG, "Cannot send pending intent due to unknown exception: ", e2);
                return false;
            }
        }
    }

    private final class ReflectionAction extends Action {
        static final int BITMAP = 12;
        static final int BOOLEAN = 1;
        static final int BUNDLE = 13;
        static final int BYTE = 2;
        static final int CHAR = 8;
        static final int CHAR_SEQUENCE = 10;
        static final int COLOR_STATE_LIST = 15;
        static final int DOUBLE = 7;
        static final int FLOAT = 6;
        static final int ICON = 16;
        static final int INT = 4;
        static final int INTENT = 14;
        static final int LONG = 5;
        static final int SHORT = 3;
        static final int STRING = 9;
        static final int TAG = 2;
        static final int URI = 11;
        String methodName;
        int type;
        Object value;

        ReflectionAction(int viewId, String methodName, int type, Object value) {
            super();
            this.viewId = viewId;
            this.methodName = methodName;
            this.type = type;
            this.value = value;
        }

        ReflectionAction(Parcel in) {
            super();
            this.viewId = in.readInt();
            this.methodName = in.readString();
            this.type = in.readInt();
            switch (this.type) {
                case 1:
                    this.value = Boolean.valueOf(in.readInt() != 0);
                    return;
                case 2:
                    this.value = Byte.valueOf(in.readByte());
                    return;
                case 3:
                    this.value = Short.valueOf((short) in.readInt());
                    return;
                case 4:
                    this.value = Integer.valueOf(in.readInt());
                    return;
                case 5:
                    this.value = Long.valueOf(in.readLong());
                    return;
                case 6:
                    this.value = Float.valueOf(in.readFloat());
                    return;
                case 7:
                    this.value = Double.valueOf(in.readDouble());
                    return;
                case 8:
                    this.value = Character.valueOf((char) in.readInt());
                    return;
                case 9:
                    this.value = in.readString();
                    return;
                case 10:
                    this.value = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
                    return;
                case 11:
                    if (in.readInt() != 0) {
                        this.value = Uri.CREATOR.createFromParcel(in);
                        return;
                    }
                    return;
                case 12:
                    if (in.readInt() != 0) {
                        this.value = Bitmap.CREATOR.createFromParcel(in);
                        return;
                    }
                    return;
                case 13:
                    this.value = in.readBundle();
                    return;
                case 14:
                    if (in.readInt() != 0) {
                        this.value = Intent.CREATOR.createFromParcel(in);
                        return;
                    }
                    return;
                case 15:
                    if (in.readInt() != 0) {
                        this.value = ColorStateList.CREATOR.createFromParcel(in);
                        return;
                    }
                    return;
                case 16:
                    if (in.readInt() != 0) {
                        this.value = Icon.CREATOR.createFromParcel(in);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        public void writeToParcel(Parcel out, int flags) {
            int i = 1;
            out.writeInt(2);
            out.writeInt(this.viewId);
            out.writeString(this.methodName);
            out.writeInt(this.type);
            switch (this.type) {
                case 1:
                    out.writeInt(((Boolean) this.value).booleanValue() ? 1 : 0);
                    return;
                case 2:
                    out.writeByte(((Byte) this.value).byteValue());
                    return;
                case 3:
                    out.writeInt(((Short) this.value).shortValue());
                    return;
                case 4:
                    out.writeInt(((Integer) this.value).intValue());
                    return;
                case 5:
                    out.writeLong(((Long) this.value).longValue());
                    return;
                case 6:
                    out.writeFloat(((Float) this.value).floatValue());
                    return;
                case 7:
                    out.writeDouble(((Double) this.value).doubleValue());
                    return;
                case 8:
                    out.writeInt(((Character) this.value).charValue());
                    return;
                case 9:
                    out.writeString((String) this.value);
                    return;
                case 10:
                    TextUtils.writeToParcel((CharSequence) this.value, out, flags);
                    return;
                case 11:
                    if (this.value == null) {
                        i = 0;
                    }
                    out.writeInt(i);
                    if (this.value != null) {
                        ((Uri) this.value).writeToParcel(out, flags);
                        return;
                    }
                    return;
                case 12:
                    if (this.value == null) {
                        i = 0;
                    }
                    out.writeInt(i);
                    if (this.value != null) {
                        ((Bitmap) this.value).writeToParcel(out, flags);
                        return;
                    }
                    return;
                case 13:
                    out.writeBundle((Bundle) this.value);
                    return;
                case 14:
                    if (this.value == null) {
                        i = 0;
                    }
                    out.writeInt(i);
                    if (this.value != null) {
                        ((Intent) this.value).writeToParcel(out, flags);
                        return;
                    }
                    return;
                case 15:
                    if (this.value == null) {
                        i = 0;
                    }
                    out.writeInt(i);
                    if (this.value != null) {
                        ((ColorStateList) this.value).writeToParcel(out, flags);
                        return;
                    }
                    return;
                case 16:
                    if (this.value == null) {
                        i = 0;
                    }
                    out.writeInt(i);
                    if (this.value != null) {
                        ((Icon) this.value).writeToParcel(out, flags);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        private Class<?> getParameterType() {
            switch (this.type) {
                case 1:
                    return Boolean.TYPE;
                case 2:
                    return Byte.TYPE;
                case 3:
                    return Short.TYPE;
                case 4:
                    return Integer.TYPE;
                case 5:
                    return Long.TYPE;
                case 6:
                    return Float.TYPE;
                case 7:
                    return Double.TYPE;
                case 8:
                    return Character.TYPE;
                case 9:
                    return String.class;
                case 10:
                    return CharSequence.class;
                case 11:
                    return Uri.class;
                case 12:
                    return Bitmap.class;
                case 13:
                    return Bundle.class;
                case 14:
                    return Intent.class;
                case 15:
                    return ColorStateList.class;
                case 16:
                    return Icon.class;
                default:
                    return null;
            }
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View view = root.findViewById(this.viewId);
            if (view != null) {
                Class<?> param = getParameterType();
                if (param == null) {
                    throw new ActionException("bad type: " + this.type);
                }
                try {
                    RemoteViews.this.getMethod(view, this.methodName, param).invoke(view, RemoteViews.wrapArg(this.value));
                } catch (ActionException e) {
                    throw e;
                } catch (Exception ex) {
                    throw new ActionException(ex);
                }
            }
        }

        public int mergeBehavior() {
            if (this.methodName.equals("smoothScrollBy")) {
                return 1;
            }
            return 0;
        }

        public String getActionName() {
            return "ReflectionAction" + this.methodName + this.type;
        }
    }

    private final class ReflectionActionWithoutParams extends Action {
        public static final int TAG = 5;
        final String methodName;

        ReflectionActionWithoutParams(int viewId, String methodName) {
            super();
            this.viewId = viewId;
            this.methodName = methodName;
        }

        ReflectionActionWithoutParams(Parcel in) {
            super();
            this.viewId = in.readInt();
            this.methodName = in.readString();
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(5);
            out.writeInt(this.viewId);
            out.writeString(this.methodName);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View view = root.findViewById(this.viewId);
            if (view != null) {
                try {
                    RemoteViews.this.getMethod(view, this.methodName, null).invoke(view, new Object[0]);
                } catch (ActionException e) {
                    throw e;
                } catch (Exception ex) {
                    throw new ActionException(ex);
                }
            }
        }

        public int mergeBehavior() {
            if (this.methodName.equals("showNext") || this.methodName.equals("showPrevious")) {
                return 2;
            }
            return 0;
        }

        public String getActionName() {
            return "ReflectionActionWithoutParams";
        }
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RemoteView {
    }

    private class SetDrawableParameters extends Action {
        public static final int TAG = 3;
        int alpha;
        int colorFilter;
        Mode filterMode;
        int level;
        boolean targetBackground;

        public SetDrawableParameters(int id, boolean targetBackground, int alpha, int colorFilter, Mode mode, int level) {
            super();
            this.viewId = id;
            this.targetBackground = targetBackground;
            this.alpha = alpha;
            this.colorFilter = colorFilter;
            this.filterMode = mode;
            this.level = level;
        }

        public SetDrawableParameters(Parcel parcel) {
            boolean z;
            boolean hasMode;
            super();
            this.viewId = parcel.readInt();
            if (parcel.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            this.targetBackground = z;
            this.alpha = parcel.readInt();
            this.colorFilter = parcel.readInt();
            if (parcel.readInt() != 0) {
                hasMode = true;
            } else {
                hasMode = false;
            }
            if (hasMode) {
                this.filterMode = Mode.valueOf(parcel.readString());
            } else {
                this.filterMode = null;
            }
            this.level = parcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(3);
            dest.writeInt(this.viewId);
            dest.writeInt(this.targetBackground ? 1 : 0);
            dest.writeInt(this.alpha);
            dest.writeInt(this.colorFilter);
            if (this.filterMode != null) {
                dest.writeInt(1);
                dest.writeString(this.filterMode.toString());
            } else {
                dest.writeInt(0);
            }
            dest.writeInt(this.level);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                Drawable targetDrawable = null;
                if (this.targetBackground) {
                    targetDrawable = target.getBackground();
                } else if (target instanceof ImageView) {
                    targetDrawable = ((ImageView) target).getDrawable();
                }
                if (targetDrawable != null) {
                    if (this.alpha != -1) {
                        targetDrawable.mutate().setAlpha(this.alpha);
                    }
                    if (this.filterMode != null) {
                        targetDrawable.mutate().setColorFilter(this.colorFilter, this.filterMode);
                    }
                    if (this.level != -1) {
                        targetDrawable.mutate().setLevel(this.level);
                    }
                }
            }
        }

        public String getActionName() {
            return "SetDrawableParameters";
        }
    }

    private class SetEmptyView extends Action {
        public static final int TAG = 6;
        int emptyViewId;
        int viewId;

        SetEmptyView(int viewId, int emptyViewId) {
            super();
            this.viewId = viewId;
            this.emptyViewId = emptyViewId;
        }

        SetEmptyView(Parcel in) {
            super();
            this.viewId = in.readInt();
            this.emptyViewId = in.readInt();
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(6);
            out.writeInt(this.viewId);
            out.writeInt(this.emptyViewId);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View view = root.findViewById(this.viewId);
            if (view instanceof AdapterView) {
                AdapterView<?> adapterView = (AdapterView) view;
                View emptyView = root.findViewById(this.emptyViewId);
                if (emptyView != null) {
                    adapterView.setEmptyView(emptyView);
                }
            }
        }

        public String getActionName() {
            return "SetEmptyView";
        }
    }

    private class SetLaunchPendingIntent extends Action {
        public static final int TAG = 20;
        PendingIntent pendingIntent;
        int viewId;

        public SetLaunchPendingIntent(int id, PendingIntent pendingIntent) {
            super();
            this.viewId = id;
            this.pendingIntent = pendingIntent;
        }

        public SetLaunchPendingIntent(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.pendingIntent = PendingIntent.readPendingIntentOrNullFromParcel(parcel);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(20);
            dest.writeInt(this.viewId);
            this.pendingIntent.writeToParcel(dest, 0);
        }

        public void apply(View root, ViewGroup rootParent, final OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null && this.pendingIntent != null) {
                target.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        try {
                            ActivityManagerNative.getDefault().resumeAppSwitches();
                        } catch (RemoteException e) {
                        }
                        float appScale = v.getContext().getResources().getCompatibilityInfo().applicationScale;
                        int[] pos = new int[2];
                        v.getLocationOnScreen(pos);
                        Rect rect = new Rect();
                        rect.left = (int) ((((float) pos[0]) * appScale) + 0.5f);
                        rect.top = (int) ((((float) pos[1]) * appScale) + 0.5f);
                        rect.right = (int) ((((float) (pos[0] + v.getWidth())) * appScale) + 0.5f);
                        rect.bottom = (int) ((((float) (pos[1] + v.getHeight())) * appScale) + 0.5f);
                        Intent intent = new Intent();
                        intent.setSourceBounds(rect);
                        handler.onClickHandler(v, SetLaunchPendingIntent.this.pendingIntent, intent);
                        ((StatusBarManager) v.getContext().getSystemService("statusbar")).collapsePanels();
                    }
                });
            }
        }

        public String getActionName() {
            return "SetLaunchPendingIntent";
        }
    }

    private class SetOnClickFillInIntent extends Action {
        public static final int TAG = 9;
        Intent fillInIntent;

        public SetOnClickFillInIntent(int id, Intent fillInIntent) {
            super();
            this.viewId = id;
            this.fillInIntent = fillInIntent;
        }

        public SetOnClickFillInIntent(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.fillInIntent = (Intent) Intent.CREATOR.createFromParcel(parcel);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(9);
            dest.writeInt(this.viewId);
            this.fillInIntent.writeToParcel(dest, 0);
        }

        public void apply(View root, ViewGroup rootParent, final OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                if (!RemoteViews.this.mIsWidgetCollectionChild) {
                    Log.e(RemoteViews.LOG_TAG, "The method setOnClickFillInIntent is available only from RemoteViewsFactory (ie. on collection items).");
                } else if (target == root) {
                    target.setTagInternal(R.id.fillInIntent, this.fillInIntent);
                } else if (this.fillInIntent != null) {
                    target.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            View parent = (View) v.getParent();
                            while (parent != null && !(parent instanceof AdapterView) && !(parent instanceof AppWidgetHostView)) {
                                parent = (View) parent.getParent();
                            }
                            if ((parent instanceof AppWidgetHostView) || parent == null) {
                                Log.e(RemoteViews.LOG_TAG, "Collection item doesn't have AdapterView parent");
                            } else if (parent.getTag() instanceof PendingIntent) {
                                PendingIntent pendingIntent = (PendingIntent) parent.getTag();
                                SetOnClickFillInIntent.this.fillInIntent.setSourceBounds(RemoteViews.getSourceBounds(v));
                                handler.onClickHandler(v, pendingIntent, SetOnClickFillInIntent.this.fillInIntent);
                            } else {
                                Log.e(RemoteViews.LOG_TAG, "Attempting setOnClickFillInIntent without calling setPendingIntentTemplate on parent.");
                            }
                        }
                    });
                }
            }
        }

        public String getActionName() {
            return "SetOnClickFillInIntent";
        }
    }

    private class SetOnClickPendingIntent extends Action {
        public static final int TAG = 1;
        PendingIntent pendingIntent;

        public SetOnClickPendingIntent(int id, PendingIntent pendingIntent) {
            super();
            this.viewId = id;
            this.pendingIntent = pendingIntent;
        }

        public SetOnClickPendingIntent(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            if (parcel.readInt() != 0) {
                this.pendingIntent = PendingIntent.readPendingIntentOrNullFromParcel(parcel);
            }
        }

        public void writeToParcel(Parcel dest, int flags) {
            int i = 1;
            dest.writeInt(1);
            dest.writeInt(this.viewId);
            if (this.pendingIntent == null) {
                i = 0;
            }
            dest.writeInt(i);
            if (this.pendingIntent != null) {
                this.pendingIntent.writeToParcel(dest, 0);
            }
        }

        public void apply(View root, ViewGroup rootParent, final OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                if (RemoteViews.this.mIsWidgetCollectionChild) {
                    Log.w(RemoteViews.LOG_TAG, "Cannot setOnClickPendingIntent for collection item (id: " + this.viewId + ")");
                    ApplicationInfo appInfo = root.getContext().getApplicationInfo();
                    if (appInfo != null && appInfo.targetSdkVersion >= 16) {
                        return;
                    }
                }
                OnClickListener listener = null;
                if (this.pendingIntent != null) {
                    listener = new OnClickListener() {
                        public void onClick(View v) {
                            Rect rect = RemoteViews.getSourceBounds(v);
                            Intent intent = new Intent();
                            intent.setSourceBounds(rect);
                            handler.onClickHandler(v, SetOnClickPendingIntent.this.pendingIntent, intent);
                        }
                    };
                }
                target.setOnClickListener(listener);
            }
        }

        public String getActionName() {
            return "SetOnClickPendingIntent";
        }
    }

    private class SetOnLongClickDragable extends Action {
        public static final int TAG = 19;
        ClipData clipData;
        PendingIntent dragEnterNotiIntent;
        PendingIntent dragExitNotiIntent;
        PendingIntent dragStartIntent;
        boolean isNeedToRemove;
        int viewId;

        public SetOnLongClickDragable(int id, ClipData clipData, PendingIntent dragStartNotiIntent, PendingIntent dragEnterNotiIntent, PendingIntent dragExitNotiIntent, boolean isNeedToRemove) {
            super();
            this.viewId = id;
            this.isNeedToRemove = isNeedToRemove;
            this.clipData = clipData;
            this.dragStartIntent = dragStartNotiIntent;
            this.dragEnterNotiIntent = dragEnterNotiIntent;
            this.dragExitNotiIntent = dragExitNotiIntent;
        }

        public SetOnLongClickDragable(Parcel parcel) {
            super();
            Log.e(RemoteViews.LOG_TAG, "SetOnLongClickDragable - read:" + parcel.toString());
            this.viewId = parcel.readInt();
            this.isNeedToRemove = parcel.readByte() != (byte) 0;
            this.clipData = (ClipData) parcel.readParcelable(ClipData.class.getClassLoader());
            if (parcel.readInt() != 0) {
                this.dragStartIntent = PendingIntent.readPendingIntentOrNullFromParcel(parcel);
            }
            if (parcel.readInt() != 0) {
                this.dragEnterNotiIntent = PendingIntent.readPendingIntentOrNullFromParcel(parcel);
            }
            if (parcel.readInt() != 0) {
                this.dragExitNotiIntent = PendingIntent.readPendingIntentOrNullFromParcel(parcel);
            }
        }

        public void writeToParcel(Parcel dest, int flag) {
            Log.e(RemoteViews.LOG_TAG, "SetOnLongClickDragable - writeToParcel:" + dest.toString());
            dest.writeInt(19);
            dest.writeInt(this.viewId);
            dest.writeByte((byte) (this.isNeedToRemove ? 1 : 0));
            dest.writeParcelable(this.clipData, 0);
            if (this.dragStartIntent != null) {
                dest.writeInt(1);
                this.dragStartIntent.writeToParcel(dest, 0);
            } else {
                dest.writeInt(0);
            }
            if (this.dragEnterNotiIntent != null) {
                dest.writeInt(1);
                this.dragEnterNotiIntent.writeToParcel(dest, 0);
            } else {
                dest.writeInt(0);
            }
            if (this.dragExitNotiIntent != null) {
                dest.writeInt(1);
                this.dragExitNotiIntent.writeToParcel(dest, 0);
                return;
            }
            dest.writeInt(0);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) throws ActionException {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                target.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
                        Log.e(RemoteViews.LOG_TAG, "Drag info: " + SetOnLongClickDragable.this.clipData + SetOnLongClickDragable.this.dragStartIntent + SetOnLongClickDragable.this.isNeedToRemove);
                        v.startDrag(SetOnLongClickDragable.this.clipData, shadowBuilder, null, 0);
                        if (SetOnLongClickDragable.this.dragStartIntent != null) {
                            try {
                                v.getContext().startIntentSender(SetOnLongClickDragable.this.dragStartIntent.getIntentSender(), null, 268435456, 268435456, 0);
                            } catch (SendIntentException e) {
                                Log.e(RemoteViews.LOG_TAG, "Cannot send pending intent: ", e);
                            }
                        }
                        if (SetOnLongClickDragable.this.isNeedToRemove) {
                            ViewGroup parent = (ViewGroup) v.getParent();
                            if (parent != null) {
                                parent.removeView(v);
                            }
                        }
                        return true;
                    }
                });
                if (this.dragEnterNotiIntent != null || this.dragExitNotiIntent != null) {
                    target.setOnDragListener(new OnDragListener() {
                        public boolean onDrag(View v, DragEvent dragEvent) {
                            switch (dragEvent.getAction()) {
                                case 5:
                                    if (SetOnLongClickDragable.this.dragEnterNotiIntent != null) {
                                        try {
                                            v.getContext().startIntentSender(SetOnLongClickDragable.this.dragEnterNotiIntent.getIntentSender(), null, 268435456, 268435456, 0);
                                            break;
                                        } catch (SendIntentException e) {
                                            Log.e(RemoteViews.LOG_TAG, "Cannot send pending intent: ", e);
                                            break;
                                        }
                                    }
                                    break;
                                case 6:
                                    if (SetOnLongClickDragable.this.dragExitNotiIntent != null) {
                                        try {
                                            v.getContext().startIntentSender(SetOnLongClickDragable.this.dragExitNotiIntent.getIntentSender(), null, 268435456, 268435456, 0);
                                            break;
                                        } catch (SendIntentException e2) {
                                            Log.e(RemoteViews.LOG_TAG, "Cannot send pending intent: ", e2);
                                            break;
                                        }
                                    }
                                    break;
                            }
                            return true;
                        }
                    });
                }
            }
        }

        public String getActionName() {
            return "SetOnLongClickDragable";
        }
    }

    private class SetOnLongClickPendingIntent extends Action {
        public static final int TAG = 16;
        PendingIntent downPendingIntent;
        IBinder key;
        PendingIntent upPendingIntent;
        int viewId;

        public SetOnLongClickPendingIntent(IBinder key, int id, PendingIntent downPendingIntent, PendingIntent upPendingIntent) {
            super();
            this.key = key;
            this.viewId = id;
            this.downPendingIntent = downPendingIntent;
            this.upPendingIntent = upPendingIntent;
        }

        public SetOnLongClickPendingIntent(Parcel parcel) {
            super();
            this.key = parcel.readStrongBinder();
            this.viewId = parcel.readInt();
            this.downPendingIntent = PendingIntent.readPendingIntentOrNullFromParcel(parcel);
            this.upPendingIntent = PendingIntent.readPendingIntentOrNullFromParcel(parcel);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(16);
            dest.writeStrongBinder(this.key);
            dest.writeInt(this.viewId);
            this.downPendingIntent.writeToParcel(dest, 0);
            this.upPendingIntent.writeToParcel(dest, 0);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null && this.downPendingIntent != null && this.upPendingIntent != null) {
                target.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        if (RemoteViews.mIsLongClick != null) {
                            RemoteViews.mIsLongClick.put(SetOnLongClickPendingIntent.this.key, Boolean.valueOf(true));
                        }
                        try {
                            v.getContext().startIntentSender(SetOnLongClickPendingIntent.this.downPendingIntent.getIntentSender(), null, 268435456, 268435456, 0);
                        } catch (SendIntentException e) {
                            Log.e(RemoteViews.LOG_TAG, "Cannot send pending intent: ", e);
                        }
                        return true;
                    }
                });
                target.setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        Boolean isLongClick;
                        switch (event.getAction()) {
                            case 0:
                                if (RemoteViews.mIsLongClick != null) {
                                    RemoteViews.mIsLongClick.put(SetOnLongClickPendingIntent.this.key, Boolean.valueOf(false));
                                    break;
                                }
                                break;
                            case 1:
                            case 3:
                                if (RemoteViews.mIsLongClick != null) {
                                    isLongClick = (Boolean) RemoteViews.mIsLongClick.get(SetOnLongClickPendingIntent.this.key);
                                    if (isLongClick != null && isLongClick.booleanValue()) {
                                        RemoteViews.mIsLongClick.put(SetOnLongClickPendingIntent.this.key, Boolean.valueOf(false));
                                        try {
                                            v.getContext().startIntentSender(SetOnLongClickPendingIntent.this.upPendingIntent.getIntentSender(), null, 268435456, 268435456, 0);
                                            break;
                                        } catch (SendIntentException e) {
                                            Log.e(RemoteViews.LOG_TAG, "Cannot send pending intent: ", e);
                                            break;
                                        }
                                    }
                                }
                                break;
                            case 2:
                                if (!(v.isPressed() || RemoteViews.mIsLongClick == null)) {
                                    isLongClick = (Boolean) RemoteViews.mIsLongClick.get(SetOnLongClickPendingIntent.this.key);
                                    if (isLongClick != null && isLongClick.booleanValue()) {
                                        RemoteViews.mIsLongClick.put(SetOnLongClickPendingIntent.this.key, Boolean.valueOf(false));
                                        try {
                                            v.getContext().startIntentSender(SetOnLongClickPendingIntent.this.upPendingIntent.getIntentSender(), null, 268435456, 268435456, 0);
                                            break;
                                        } catch (SendIntentException e2) {
                                            Log.e(RemoteViews.LOG_TAG, "Cannot send pending intent: ", e2);
                                            break;
                                        }
                                    }
                                }
                        }
                        return false;
                    }
                });
                target.setOnKeyListener(new OnKeyListener() {
                    public boolean onKey(View view, int keyCode, KeyEvent event) {
                        RemoteViews.this.mHas12key = view.getContext().getResources().getConfiguration().keyboard == 3;
                        if (keyCode == 66 || (RemoteViews.this.mHas12key && keyCode == 23)) {
                            switch (event.getAction()) {
                                case 0:
                                    if (event.getRepeatCount() == 0 && RemoteViews.mIsLongClick != null) {
                                        RemoteViews.mIsLongClick.put(SetOnLongClickPendingIntent.this.key, Boolean.valueOf(false));
                                        break;
                                    }
                                case 1:
                                    if (RemoteViews.mIsLongClick != null) {
                                        Boolean isLongClick = (Boolean) RemoteViews.mIsLongClick.get(SetOnLongClickPendingIntent.this.key);
                                        if (isLongClick != null && isLongClick.booleanValue()) {
                                            RemoteViews.mIsLongClick.put(SetOnLongClickPendingIntent.this.key, Boolean.valueOf(false));
                                            try {
                                                view.getContext().startIntentSender(SetOnLongClickPendingIntent.this.upPendingIntent.getIntentSender(), null, 268435456, 268435456, 0);
                                                break;
                                            } catch (SendIntentException e) {
                                                Log.e(RemoteViews.LOG_TAG, "Cannot send pending intent: ", e);
                                                break;
                                            }
                                        }
                                    }
                                    break;
                            }
                        }
                        return false;
                    }
                });
            }
        }

        public String getActionName() {
            return "SetOnLongClickPendingIntent";
        }
    }

    private class SetOnTouchPendingIntent extends Action {
        public static final int TAG = 32;
        PendingIntent pendingIntent;
        int viewId;

        public SetOnTouchPendingIntent(int id, PendingIntent pendingIntent) {
            super();
            this.viewId = id;
            this.pendingIntent = pendingIntent;
        }

        public SetOnTouchPendingIntent(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.pendingIntent = PendingIntent.readPendingIntentOrNullFromParcel(parcel);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(32);
            dest.writeInt(this.viewId);
            this.pendingIntent.writeToParcel(dest, 0);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null && this.pendingIntent != null) {
                target.setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        float x = event.getX();
                        float y = event.getY();
                        Intent intent;
                        switch (event.getAction()) {
                            case 0:
                                try {
                                    intent = new Intent();
                                    intent.putExtra("x_position", x);
                                    intent.putExtra("y_position", y);
                                    intent.putExtra("isUp", false);
                                    v.getContext().startIntentSender(SetOnTouchPendingIntent.this.pendingIntent.getIntentSender(), intent, 268435456, 268435456, 0);
                                    break;
                                } catch (SendIntentException e) {
                                    Log.e(RemoteViews.LOG_TAG, "Cannot send pending intent: ", e);
                                    break;
                                }
                            case 1:
                                try {
                                    intent = new Intent();
                                    intent.putExtra("x_position", x);
                                    intent.putExtra("y_position", y);
                                    intent.putExtra("isUp", true);
                                    v.getContext().startIntentSender(SetOnTouchPendingIntent.this.pendingIntent.getIntentSender(), intent, 268435456, 268435456, 0);
                                    break;
                                } catch (SendIntentException e2) {
                                    Log.e(RemoteViews.LOG_TAG, "Cannot send pending intent: ", e2);
                                    break;
                                }
                        }
                        return false;
                    }
                });
            }
        }

        public String getActionName() {
            return "SetOnTouchPendingIntent";
        }
    }

    private class SetPendingIntentTemplate extends Action {
        public static final int TAG = 8;
        PendingIntent pendingIntentTemplate;

        public SetPendingIntentTemplate(int id, PendingIntent pendingIntentTemplate) {
            super();
            this.viewId = id;
            this.pendingIntentTemplate = pendingIntentTemplate;
        }

        public SetPendingIntentTemplate(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.pendingIntentTemplate = PendingIntent.readPendingIntentOrNullFromParcel(parcel);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(8);
            dest.writeInt(this.viewId);
            this.pendingIntentTemplate.writeToParcel(dest, 0);
        }

        public void apply(View root, ViewGroup rootParent, final OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                if (target instanceof AdapterView) {
                    AdapterView<?> av = (AdapterView) target;
                    av.setOnItemClickListener(new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (view instanceof ViewGroup) {
                                ViewGroup vg = (ViewGroup) view;
                                if (parent instanceof AdapterViewAnimator) {
                                    vg = (ViewGroup) vg.getChildAt(0);
                                }
                                if (vg != null) {
                                    Intent fillInIntent = null;
                                    int childCount = vg.getChildCount();
                                    for (int i = 0; i < childCount; i++) {
                                        Intent tag = vg.getChildAt(i).getTag(R.id.fillInIntent);
                                        if (tag instanceof Intent) {
                                            fillInIntent = tag;
                                            break;
                                        }
                                    }
                                    if (fillInIntent != null) {
                                        new Intent().setSourceBounds(RemoteViews.getSourceBounds(view));
                                        handler.onClickHandler(view, SetPendingIntentTemplate.this.pendingIntentTemplate, fillInIntent);
                                    }
                                }
                            }
                        }
                    });
                    av.setTag(this.pendingIntentTemplate);
                    return;
                }
                Log.e(RemoteViews.LOG_TAG, "Cannot setPendingIntentTemplate on a view which is notan AdapterView (id: " + this.viewId + ")");
            }
        }

        public String getActionName() {
            return "SetPendingIntentTemplate";
        }
    }

    private class SetRemoteViewsAdapterIntent extends Action {
        public static final int TAG = 10;
        Intent intent;

        public SetRemoteViewsAdapterIntent(int id, Intent intent) {
            super();
            this.viewId = id;
            this.intent = intent;
        }

        public SetRemoteViewsAdapterIntent(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.intent = (Intent) Intent.CREATOR.createFromParcel(parcel);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(10);
            dest.writeInt(this.viewId);
            this.intent.writeToParcel(dest, flags);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                if (!(rootParent instanceof AppWidgetHostView)) {
                    Log.e(RemoteViews.LOG_TAG, "SetRemoteViewsAdapterIntent action can only be used for AppWidgets (root id: " + this.viewId + ")");
                } else if ((target instanceof AbsListView) || (target instanceof AdapterViewAnimator)) {
                    this.intent.putExtra(RemoteViews.EXTRA_REMOTEADAPTER_APPWIDGET_ID, ((AppWidgetHostView) rootParent).getAppWidgetId());
                    if (target instanceof AbsListView) {
                        AbsListView v = (AbsListView) target;
                        v.setRemoteViewsAdapter(this.intent);
                        v.setRemoteViewsOnClickHandler(handler);
                    } else if (target instanceof AdapterViewAnimator) {
                        AdapterViewAnimator v2 = (AdapterViewAnimator) target;
                        v2.setRemoteViewsAdapter(this.intent);
                        v2.setRemoteViewsOnClickHandler(handler);
                    }
                } else {
                    Log.e(RemoteViews.LOG_TAG, "Cannot setRemoteViewsAdapter on a view which is not an AbsListView or AdapterViewAnimator (id: " + this.viewId + ")");
                }
            }
        }

        public String getActionName() {
            return "SetRemoteViewsAdapterIntent";
        }
    }

    private class SetRemoteViewsAdapterList extends Action {
        public static final int TAG = 15;
        ArrayList<RemoteViews> list;
        int viewTypeCount;

        public SetRemoteViewsAdapterList(int id, ArrayList<RemoteViews> list, int viewTypeCount) {
            super();
            this.viewId = id;
            this.list = list;
            this.viewTypeCount = viewTypeCount;
        }

        public SetRemoteViewsAdapterList(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.viewTypeCount = parcel.readInt();
            int count = parcel.readInt();
            this.list = new ArrayList();
            for (int i = 0; i < count; i++) {
                this.list.add((RemoteViews) RemoteViews.CREATOR.createFromParcel(parcel));
            }
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(15);
            dest.writeInt(this.viewId);
            dest.writeInt(this.viewTypeCount);
            if (this.list == null || this.list.size() == 0) {
                dest.writeInt(0);
                return;
            }
            int count = this.list.size();
            dest.writeInt(count);
            for (int i = 0; i < count; i++) {
                ((RemoteViews) this.list.get(i)).writeToParcel(dest, flags);
            }
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                if (!(rootParent instanceof AppWidgetHostView)) {
                    Log.e(RemoteViews.LOG_TAG, "SetRemoteViewsAdapterIntent action can only be used for AppWidgets (root id: " + this.viewId + ")");
                } else if (!(target instanceof AbsListView) && !(target instanceof AdapterViewAnimator)) {
                    Log.e(RemoteViews.LOG_TAG, "Cannot setRemoteViewsAdapter on a view which is not an AbsListView or AdapterViewAnimator (id: " + this.viewId + ")");
                } else if (target instanceof AbsListView) {
                    AbsListView v = (AbsListView) target;
                    a = v.getAdapter();
                    if (!(a instanceof RemoteViewsListAdapter) || this.viewTypeCount > a.getViewTypeCount()) {
                        v.setAdapter(new RemoteViewsListAdapter(v.getContext(), this.list, this.viewTypeCount));
                    } else {
                        ((RemoteViewsListAdapter) a).setViewsList(this.list);
                    }
                } else if (target instanceof AdapterViewAnimator) {
                    AdapterViewAnimator v2 = (AdapterViewAnimator) target;
                    a = v2.getAdapter();
                    if (!(a instanceof RemoteViewsListAdapter) || this.viewTypeCount > a.getViewTypeCount()) {
                        v2.setAdapter(new RemoteViewsListAdapter(v2.getContext(), this.list, this.viewTypeCount));
                    } else {
                        ((RemoteViewsListAdapter) a).setViewsList(this.list);
                    }
                }
            }
        }

        public String getActionName() {
            return "SetRemoteViewsAdapterList";
        }
    }

    private class TextViewDrawableAction extends Action {
        public static final int TAG = 11;
        int d1;
        int d2;
        int d3;
        int d4;
        Icon i1;
        Icon i2;
        Icon i3;
        Icon i4;
        boolean isRelative = false;
        boolean useIcons = false;

        public TextViewDrawableAction(int viewId, boolean isRelative, int d1, int d2, int d3, int d4) {
            super();
            this.viewId = viewId;
            this.isRelative = isRelative;
            this.useIcons = false;
            this.d1 = d1;
            this.d2 = d2;
            this.d3 = d3;
            this.d4 = d4;
        }

        public TextViewDrawableAction(int viewId, boolean isRelative, Icon i1, Icon i2, Icon i3, Icon i4) {
            super();
            this.viewId = viewId;
            this.isRelative = isRelative;
            this.useIcons = true;
            this.i1 = i1;
            this.i2 = i2;
            this.i3 = i3;
            this.i4 = i4;
        }

        public TextViewDrawableAction(RemoteViews remoteViews, Parcel parcel) {
            boolean z = true;
            RemoteViews.this = remoteViews;
            super();
            this.viewId = parcel.readInt();
            this.isRelative = parcel.readInt() != 0;
            if (parcel.readInt() == 0) {
                z = false;
            }
            this.useIcons = z;
            if (this.useIcons) {
                if (parcel.readInt() != 0) {
                    this.i1 = (Icon) Icon.CREATOR.createFromParcel(parcel);
                }
                if (parcel.readInt() != 0) {
                    this.i2 = (Icon) Icon.CREATOR.createFromParcel(parcel);
                }
                if (parcel.readInt() != 0) {
                    this.i3 = (Icon) Icon.CREATOR.createFromParcel(parcel);
                }
                if (parcel.readInt() != 0) {
                    this.i4 = (Icon) Icon.CREATOR.createFromParcel(parcel);
                    return;
                }
                return;
            }
            this.d1 = parcel.readInt();
            this.d2 = parcel.readInt();
            this.d3 = parcel.readInt();
            this.d4 = parcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            int i;
            dest.writeInt(11);
            dest.writeInt(this.viewId);
            dest.writeInt(this.isRelative ? 1 : 0);
            if (this.useIcons) {
                i = 1;
            } else {
                i = 0;
            }
            dest.writeInt(i);
            if (this.useIcons) {
                if (this.i1 != null) {
                    dest.writeInt(1);
                    this.i1.writeToParcel(dest, 0);
                } else {
                    dest.writeInt(0);
                }
                if (this.i2 != null) {
                    dest.writeInt(1);
                    this.i2.writeToParcel(dest, 0);
                } else {
                    dest.writeInt(0);
                }
                if (this.i3 != null) {
                    dest.writeInt(1);
                    this.i3.writeToParcel(dest, 0);
                } else {
                    dest.writeInt(0);
                }
                if (this.i4 != null) {
                    dest.writeInt(1);
                    this.i4.writeToParcel(dest, 0);
                    return;
                }
                dest.writeInt(0);
                return;
            }
            dest.writeInt(this.d1);
            dest.writeInt(this.d2);
            dest.writeInt(this.d3);
            dest.writeInt(this.d4);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            TextView target = (TextView) root.findViewById(this.viewId);
            if (target != null) {
                if (this.useIcons) {
                    Context ctx = target.getContext();
                    Drawable id1 = this.i1 == null ? null : this.i1.loadDrawable(ctx);
                    Drawable id2 = this.i2 == null ? null : this.i2.loadDrawable(ctx);
                    Drawable id3 = this.i3 == null ? null : this.i3.loadDrawable(ctx);
                    Drawable id4 = this.i4 == null ? null : this.i4.loadDrawable(ctx);
                    if (this.isRelative) {
                        target.setCompoundDrawablesRelativeWithIntrinsicBounds(id1, id2, id3, id4);
                    } else {
                        target.setCompoundDrawablesWithIntrinsicBounds(id1, id2, id3, id4);
                    }
                } else if (this.isRelative) {
                    target.setCompoundDrawablesRelativeWithIntrinsicBounds(this.d1, this.d2, this.d3, this.d4);
                } else {
                    target.setCompoundDrawablesWithIntrinsicBounds(this.d1, this.d2, this.d3, this.d4);
                }
            }
        }

        public String getActionName() {
            return "TextViewDrawableAction";
        }
    }

    private class TextViewDrawableColorFilterAction extends Action {
        public static final int TAG = 17;
        final int color;
        final int index;
        final boolean isRelative;
        final Mode mode;

        public TextViewDrawableColorFilterAction(int viewId, boolean isRelative, int index, int color, Mode mode) {
            super();
            this.viewId = viewId;
            this.isRelative = isRelative;
            this.index = index;
            this.color = color;
            this.mode = mode;
        }

        public TextViewDrawableColorFilterAction(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.isRelative = parcel.readInt() != 0;
            this.index = parcel.readInt();
            this.color = parcel.readInt();
            this.mode = readPorterDuffMode(parcel);
        }

        private Mode readPorterDuffMode(Parcel parcel) {
            int mode = parcel.readInt();
            if (mode < 0 || mode >= Mode.values().length) {
                return Mode.CLEAR;
            }
            return Mode.values()[mode];
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(17);
            dest.writeInt(this.viewId);
            dest.writeInt(this.isRelative ? 1 : 0);
            dest.writeInt(this.index);
            dest.writeInt(this.color);
            dest.writeInt(this.mode.ordinal());
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            TextView target = (TextView) root.findViewById(this.viewId);
            if (target != null) {
                Drawable[] drawables = this.isRelative ? target.getCompoundDrawablesRelative() : target.getCompoundDrawables();
                if (this.index < 0 || this.index >= 4) {
                    throw new IllegalStateException("index must be in range [0, 3].");
                }
                Drawable d = drawables[this.index];
                if (d != null) {
                    d.mutate();
                    d.setColorFilter(this.color, this.mode);
                }
            }
        }

        public String getActionName() {
            return "TextViewDrawableColorFilterAction";
        }
    }

    private class TextViewSizeAction extends Action {
        public static final int TAG = 13;
        float size;
        int units;

        public TextViewSizeAction(int viewId, int units, float size) {
            super();
            this.viewId = viewId;
            this.units = units;
            this.size = size;
        }

        public TextViewSizeAction(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.units = parcel.readInt();
            this.size = parcel.readFloat();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(13);
            dest.writeInt(this.viewId);
            dest.writeInt(this.units);
            dest.writeFloat(this.size);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            TextView target = (TextView) root.findViewById(this.viewId);
            if (target != null) {
                target.setTextSize(this.units, this.size);
            }
        }

        public String getActionName() {
            return "TextViewSizeAction";
        }
    }

    private class ViewGroupAction extends Action {
        public static final int TAG = 4;
        RemoteViews nestedViews;

        public ViewGroupAction(int viewId, RemoteViews nestedViews) {
            super();
            this.viewId = viewId;
            this.nestedViews = nestedViews;
            if (nestedViews != null) {
                RemoteViews.this.configureRemoteViewsAsChild(nestedViews);
            }
        }

        public ViewGroupAction(Parcel parcel, BitmapCache bitmapCache) {
            super();
            this.viewId = parcel.readInt();
            if (parcel.readInt() == 0) {
                this.nestedViews = null;
            } else {
                this.nestedViews = new RemoteViews(parcel, bitmapCache);
            }
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(4);
            dest.writeInt(this.viewId);
            if (this.nestedViews != null) {
                dest.writeInt(1);
                this.nestedViews.writeToParcel(dest, flags);
                return;
            }
            dest.writeInt(0);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            Context context = root.getContext();
            ViewGroup target = (ViewGroup) root.findViewById(this.viewId);
            if (target != null) {
                if (this.nestedViews != null) {
                    target.addView(this.nestedViews.apply(context, target, handler));
                } else {
                    target.removeAllViews();
                }
            }
        }

        public void updateMemoryUsageEstimate(MemoryUsageCounter counter) {
            if (this.nestedViews != null) {
                counter.increment(this.nestedViews.estimateMemoryUsage());
            }
        }

        public void setBitmapCache(BitmapCache bitmapCache) {
            if (this.nestedViews != null) {
                this.nestedViews.setBitmapCache(bitmapCache);
            }
        }

        public String getActionName() {
            return "ViewGroupAction" + (this.nestedViews == null ? "Remove" : "Add");
        }

        public int mergeBehavior() {
            return 1;
        }
    }

    private class ViewPaddingAction extends Action {
        public static final int TAG = 14;
        int bottom;
        int left;
        int right;
        int top;

        public ViewPaddingAction(int viewId, int left, int top, int right, int bottom) {
            super();
            this.viewId = viewId;
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public ViewPaddingAction(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.left = parcel.readInt();
            this.top = parcel.readInt();
            this.right = parcel.readInt();
            this.bottom = parcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(14);
            dest.writeInt(this.viewId);
            dest.writeInt(this.left);
            dest.writeInt(this.top);
            dest.writeInt(this.right);
            dest.writeInt(this.bottom);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                target.setPadding(this.left, this.top, this.right, this.bottom);
            }
        }

        public String getActionName() {
            return "ViewPaddingAction";
        }
    }

    public void setStringMap(Map<CharSequence, CharSequence> inputMap) {
        if (Build.IS_SYSTEM_SECURE && inputMap.size() > 0) {
            this.isNotification = true;
            this.stringNamesMap = inputMap;
        }
    }

    private void updateResourceMap(Context context) {
        for (CharSequence name : this.stringNamesMap.keySet()) {
            context.getResources().addNotificationString(name, (CharSequence) this.stringNamesMap.get(name));
        }
    }

    public void mergeRemoteViews(RemoteViews newRv) {
        if (newRv != null) {
            RemoteViews copy = newRv.clone();
            HashMap<String, Action> map = new HashMap();
            if (this.mActions == null) {
                this.mActions = new ArrayList();
            }
            synchronized (this.mActionsLock) {
                int i;
                Action a;
                int count = this.mActions.size();
                for (i = 0; i < count; i++) {
                    a = (Action) this.mActions.get(i);
                    map.put(a.getUniqueKey(), a);
                }
                ArrayList<Action> newActions = copy.mActions;
                if (newActions == null) {
                    return;
                }
                count = newActions.size();
                for (i = 0; i < count; i++) {
                    a = (Action) newActions.get(i);
                    String key = ((Action) newActions.get(i)).getUniqueKey();
                    int mergeBehavior = ((Action) newActions.get(i)).mergeBehavior();
                    if (map.containsKey(key) && mergeBehavior == 0) {
                        this.mActions.remove(map.get(key));
                        map.remove(key);
                    }
                    if (mergeBehavior == 0 || mergeBehavior == 1) {
                        this.mActions.add(a);
                    }
                }
                this.mBitmapCache = new BitmapCache();
                setBitmapCache(this.mBitmapCache);
            }
        }
    }

    private static Rect getSourceBounds(View v) {
        float appScale = v.getContext().getResources().getCompatibilityInfo().applicationScale;
        int[] pos = new int[2];
        v.getLocationOnScreen(pos);
        Rect rect = new Rect();
        rect.left = (int) ((((float) pos[0]) * appScale) + 0.5f);
        rect.top = (int) ((((float) pos[1]) * appScale) + 0.5f);
        rect.right = (int) ((((float) (pos[0] + v.getWidth())) * appScale) + 0.5f);
        rect.bottom = (int) ((((float) (pos[1] + v.getHeight())) * appScale) + 0.5f);
        return rect;
    }

    private Method getMethod(View view, String methodName, Class<?> paramType) {
        Method method;
        Class<? extends View> klass = view.getClass();
        synchronized (sMethodsLock) {
            ArrayMap<MutablePair<String, Class<?>>, Method> methods = (ArrayMap) sMethods.get(klass);
            if (methods == null) {
                methods = new ArrayMap();
                sMethods.put(klass, methods);
            }
            this.mPair.first = methodName;
            this.mPair.second = paramType;
            method = (Method) methods.get(this.mPair);
            if (method == null) {
                if (paramType == null) {
                    try {
                        method = klass.getMethod(methodName, new Class[0]);
                    } catch (NoSuchMethodException e) {
                        throw new ActionException("view: " + klass.getName() + " doesn't have method: " + methodName + getParameters(paramType));
                    }
                }
                method = klass.getMethod(methodName, new Class[]{paramType});
                if (method.isAnnotationPresent(RemotableViewMethod.class)) {
                    methods.put(new MutablePair(methodName, paramType), method);
                } else {
                    throw new ActionException("view: " + klass.getName() + " can't use method with RemoteViews: " + methodName + getParameters(paramType));
                }
            }
        }
        return method;
    }

    private static String getParameters(Class<?> paramType) {
        if (paramType == null) {
            return "()";
        }
        return "(" + paramType + ")";
    }

    private static Object[] wrapArg(Object value) {
        Object[] args = (Object[]) sInvokeArgsTls.get();
        args[0] = value;
        return args;
    }

    private void configureRemoteViewsAsChild(RemoteViews rv) {
        this.mBitmapCache.assimilate(rv.mBitmapCache);
        rv.setBitmapCache(this.mBitmapCache);
        rv.setNotRoot();
    }

    void setNotRoot() {
        this.mIsRoot = false;
    }

    public RemoteViews(String packageName, int layoutId) {
        this(getApplicationInfo(packageName, UserHandle.myUserId()), layoutId);
    }

    public RemoteViews(String packageName, int userId, int layoutId) {
        this(getApplicationInfo(packageName, userId), layoutId);
    }

    protected RemoteViews(ApplicationInfo application, int layoutId) {
        Map hashMap;
        this.mIsRoot = true;
        this.mLandscape = null;
        this.mPortrait = null;
        this.mIsWidgetCollectionChild = false;
        this.mActionsLock = new Object();
        if (Build.IS_SYSTEM_SECURE) {
            hashMap = new HashMap();
        } else {
            hashMap = null;
        }
        this.stringNamesMap = hashMap;
        this.mHas12key = false;
        this.isNotification = false;
        this.mPair = new MutablePair(null, null);
        this.mApplication = application;
        this.mLayoutId = layoutId;
        this.mBitmapCache = new BitmapCache();
        this.mMemoryUsageCounter = new MemoryUsageCounter();
        recalculateMemoryUsage();
    }

    private boolean hasLandscapeAndPortraitLayouts() {
        return (this.mLandscape == null || this.mPortrait == null) ? false : true;
    }

    public RemoteViews(RemoteViews landscape, RemoteViews portrait) {
        Map hashMap;
        this.mIsRoot = true;
        this.mLandscape = null;
        this.mPortrait = null;
        this.mIsWidgetCollectionChild = false;
        this.mActionsLock = new Object();
        if (Build.IS_SYSTEM_SECURE) {
            hashMap = new HashMap();
        } else {
            hashMap = null;
        }
        this.stringNamesMap = hashMap;
        this.mHas12key = false;
        this.isNotification = false;
        this.mPair = new MutablePair(null, null);
        if (landscape == null || portrait == null) {
            throw new RuntimeException("Both RemoteViews must be non-null");
        } else if (landscape.mApplication.uid == portrait.mApplication.uid && landscape.mApplication.packageName.equals(portrait.mApplication.packageName)) {
            this.mApplication = portrait.mApplication;
            this.mLayoutId = portrait.getLayoutId();
            this.mLandscape = landscape;
            this.mPortrait = portrait;
            this.mMemoryUsageCounter = new MemoryUsageCounter();
            this.mBitmapCache = new BitmapCache();
            configureRemoteViewsAsChild(landscape);
            configureRemoteViewsAsChild(portrait);
            recalculateMemoryUsage();
        } else {
            throw new RuntimeException("Both RemoteViews must share the same package and user");
        }
    }

    public RemoteViews(Parcel parcel) {
        this(parcel, null);
    }

    private RemoteViews(Parcel parcel, BitmapCache bitmapCache) {
        Map hashMap;
        int i;
        this.mIsRoot = true;
        this.mLandscape = null;
        this.mPortrait = null;
        this.mIsWidgetCollectionChild = false;
        this.mActionsLock = new Object();
        if (Build.IS_SYSTEM_SECURE) {
            hashMap = new HashMap();
        } else {
            hashMap = null;
        }
        this.stringNamesMap = hashMap;
        this.mHas12key = false;
        this.isNotification = false;
        this.mPair = new MutablePair(null, null);
        int mode = parcel.readInt();
        if (bitmapCache == null) {
            this.mBitmapCache = new BitmapCache(parcel);
        } else {
            setBitmapCache(bitmapCache);
            setNotRoot();
        }
        if (mode == 0) {
            boolean z;
            this.mApplication = (ApplicationInfo) parcel.readParcelable(null);
            this.mLayoutId = parcel.readInt();
            if (parcel.readInt() == 1) {
                z = true;
            } else {
                z = false;
            }
            this.mIsWidgetCollectionChild = z;
            int count = parcel.readInt();
            if (count > 0) {
                this.mActions = new ArrayList(count);
                for (i = 0; i < count; i++) {
                    int tag = parcel.readInt();
                    switch (tag) {
                        case 1:
                            this.mActions.add(new SetOnClickPendingIntent(parcel));
                            break;
                        case 2:
                            this.mActions.add(new ReflectionAction(parcel));
                            break;
                        case 3:
                            this.mActions.add(new SetDrawableParameters(parcel));
                            break;
                        case 4:
                            this.mActions.add(new ViewGroupAction(parcel, this.mBitmapCache));
                            break;
                        case 5:
                            this.mActions.add(new ReflectionActionWithoutParams(parcel));
                            break;
                        case 6:
                            this.mActions.add(new SetEmptyView(parcel));
                            break;
                        case 8:
                            this.mActions.add(new SetPendingIntentTemplate(parcel));
                            break;
                        case 9:
                            this.mActions.add(new SetOnClickFillInIntent(parcel));
                            break;
                        case 10:
                            this.mActions.add(new SetRemoteViewsAdapterIntent(parcel));
                            break;
                        case 11:
                            this.mActions.add(new TextViewDrawableAction(this, parcel));
                            break;
                        case 12:
                            this.mActions.add(new BitmapReflectionAction(parcel));
                            break;
                        case 13:
                            this.mActions.add(new TextViewSizeAction(parcel));
                            break;
                        case 14:
                            this.mActions.add(new ViewPaddingAction(parcel));
                            break;
                        case 15:
                            this.mActions.add(new SetRemoteViewsAdapterList(parcel));
                            break;
                        case 16:
                            this.mActions.add(new SetOnLongClickPendingIntent(parcel));
                            break;
                        case 17:
                            this.mActions.add(new TextViewDrawableColorFilterAction(parcel));
                            break;
                        case 19:
                            this.mActions.add(new SetOnLongClickDragable(parcel));
                            break;
                        case 20:
                            this.mActions.add(new SetLaunchPendingIntent(parcel));
                            break;
                        case 21:
                            this.mActions.add(new ClearAllTextEffectAction(parcel));
                            break;
                        case 22:
                            this.mActions.add(new AddOuterShadowAction(parcel));
                            break;
                        case 23:
                            this.mActions.add(new AddInnerShadowAction(parcel));
                            break;
                        case 24:
                            this.mActions.add(new AddStrokeAction(parcel));
                            break;
                        case 25:
                            this.mActions.add(new AddLinearGradientAction(parcel));
                            break;
                        case 26:
                            this.mActions.add(new AddOuterGlowAction(parcel));
                            break;
                        case 31:
                            this.mActions.add(new ImageFilterAction(parcel));
                            break;
                        case 32:
                            this.mActions.add(new SetOnTouchPendingIntent(parcel));
                            break;
                        default:
                            throw new ActionException("Tag " + tag + " not found");
                    }
                }
            }
        } else {
            this.mLandscape = new RemoteViews(parcel, this.mBitmapCache);
            this.mPortrait = new RemoteViews(parcel, this.mBitmapCache);
            this.mApplication = this.mPortrait.mApplication;
            this.mLayoutId = this.mPortrait.getLayoutId();
        }
        if (Build.IS_SYSTEM_SECURE) {
            this.isNotification = ((Boolean) parcel.readValue(null)).booleanValue();
            int size = parcel.readInt();
            if (size > 0) {
                this.stringNamesMap.clear();
                for (i = 0; i < size; i++) {
                    this.stringNamesMap.put(parcel.readCharSequence(), parcel.readCharSequence());
                }
            }
        }
        this.mMemoryUsageCounter = new MemoryUsageCounter();
        recalculateMemoryUsage();
    }

    public RemoteViews clone() {
        Parcel p = Parcel.obtain();
        writeToParcel(p, 0);
        p.setDataPosition(0);
        RemoteViews rv = new RemoteViews(p);
        p.recycle();
        return rv;
    }

    public String getPackage() {
        return this.mApplication != null ? this.mApplication.packageName : null;
    }

    public int getLayoutId() {
        return this.mLayoutId;
    }

    void setIsWidgetCollectionChild(boolean isWidgetCollectionChild) {
        this.mIsWidgetCollectionChild = isWidgetCollectionChild;
    }

    private void recalculateMemoryUsage() {
        this.mMemoryUsageCounter.clear();
        if (hasLandscapeAndPortraitLayouts()) {
            this.mMemoryUsageCounter.increment(this.mLandscape.estimateMemoryUsage());
            this.mMemoryUsageCounter.increment(this.mPortrait.estimateMemoryUsage());
            this.mBitmapCache.addBitmapMemory(this.mMemoryUsageCounter);
            return;
        }
        if (this.mActions != null) {
            int count = this.mActions.size();
            for (int i = 0; i < count; i++) {
                ((Action) this.mActions.get(i)).updateMemoryUsageEstimate(this.mMemoryUsageCounter);
            }
        }
        if (this.mIsRoot) {
            this.mBitmapCache.addBitmapMemory(this.mMemoryUsageCounter);
        }
    }

    private void setBitmapCache(BitmapCache bitmapCache) {
        this.mBitmapCache = bitmapCache;
        if (hasLandscapeAndPortraitLayouts()) {
            this.mLandscape.setBitmapCache(bitmapCache);
            this.mPortrait.setBitmapCache(bitmapCache);
        } else if (this.mActions != null) {
            int count = this.mActions.size();
            for (int i = 0; i < count; i++) {
                ((Action) this.mActions.get(i)).setBitmapCache(bitmapCache);
            }
        }
    }

    public int estimateMemoryUsage() {
        return this.mMemoryUsageCounter.getMemoryUsage();
    }

    private void addAction(Action a) {
        if (hasLandscapeAndPortraitLayouts()) {
            throw new RuntimeException("RemoteViews specifying separate landscape and portrait layouts cannot be modified. Instead, fully configure the landscape and portrait layouts individually before constructing the combined layout.");
        }
        if (this.mActions == null) {
            this.mActions = new ArrayList();
        }
        synchronized (this.mActionsLock) {
            this.mActions.add(a);
        }
        a.updateMemoryUsageEstimate(this.mMemoryUsageCounter);
    }

    public void addView(int viewId, RemoteViews nestedView) {
        addAction(new ViewGroupAction(viewId, nestedView));
    }

    public void removeAllViews(int viewId) {
        addAction(new ViewGroupAction(viewId, null));
    }

    public void showNext(int viewId) {
        addAction(new ReflectionActionWithoutParams(viewId, "showNext"));
    }

    public void showPrevious(int viewId) {
        addAction(new ReflectionActionWithoutParams(viewId, "showPrevious"));
    }

    public void setDisplayedChild(int viewId, int childIndex) {
        setInt(viewId, "setDisplayedChild", childIndex);
    }

    public void setViewVisibility(int viewId, int visibility) {
        setInt(viewId, "setVisibility", visibility);
    }

    public void setTextViewText(int viewId, CharSequence text) {
        setCharSequence(viewId, "setText", text);
    }

    public void setViewFingerHovered(int viewId, boolean enabled) {
        setBoolean(viewId, "setFingerHoveredInAppWidget", enabled);
    }

    public void setTextViewTextSize(int viewId, int units, float size) {
        addAction(new TextViewSizeAction(viewId, units, size));
    }

    public void setTextViewCompoundDrawables(int viewId, int left, int top, int right, int bottom) {
        addAction(new TextViewDrawableAction(viewId, false, left, top, right, bottom));
    }

    public void setTextViewCompoundDrawablesRelative(int viewId, int start, int top, int end, int bottom) {
        addAction(new TextViewDrawableAction(viewId, true, start, top, end, bottom));
    }

    public void setTextViewCompoundDrawablesRelativeColorFilter(int viewId, int index, int color, Mode mode) {
        if (index < 0 || index >= 4) {
            throw new IllegalArgumentException("index must be in range [0, 3].");
        }
        addAction(new TextViewDrawableColorFilterAction(viewId, true, index, color, mode));
    }

    public void clearAllTextEffect(int viewId) {
        addAction(new ClearAllTextEffectAction(viewId));
    }

    public void addOuterShadowTextEffect(int viewId, float angle, float offset, float softness, int color, float blendingOpacity) {
        addAction(new AddOuterShadowAction(viewId, angle, offset, softness, color, blendingOpacity));
    }

    public void addInnerShadowTextEffect(int viewId, float angle, float offset, float softness, int color, float blendingOpacity) {
        addAction(new AddInnerShadowAction(viewId, angle, offset, softness, color, blendingOpacity));
    }

    public void addStrokeTextEffect(int viewId, float size, int color, float blendingOpacity) {
        addAction(new AddStrokeAction(viewId, size, color, blendingOpacity));
    }

    public void addOuterGlowTextEffect(int viewId, float size, int color, float blendingOpacity) {
        addAction(new AddOuterGlowAction(viewId, size, color, blendingOpacity));
    }

    public void addLinearGradientTextEffect(int viewId, float angle, float scale, int[] colors, float[] alphas, float[] positions, float blendingOpacity) {
        addAction(new AddLinearGradientAction(viewId, angle, scale, colors, alphas, positions, blendingOpacity));
    }

    public void setTextViewCompoundDrawables(int viewId, Icon left, Icon top, Icon right, Icon bottom) {
        addAction(new TextViewDrawableAction(viewId, false, left, top, right, bottom));
    }

    public void setTextViewCompoundDrawablesRelative(int viewId, Icon start, Icon top, Icon end, Icon bottom) {
        addAction(new TextViewDrawableAction(viewId, true, start, top, end, bottom));
    }

    public void setImageViewResource(int viewId, int srcId) {
        setInt(viewId, "setImageResource", srcId);
    }

    public void setImageViewUri(int viewId, Uri uri) {
        setUri(viewId, "setImageURI", uri);
    }

    public void setImageViewBitmap(int viewId, Bitmap bitmap) {
        setBitmap(viewId, "setImageBitmap", bitmap);
    }

    public void setImageViewIcon(int viewId, Icon icon) {
        setIcon(viewId, "setImageIcon", icon);
    }

    public void setEmptyView(int viewId, int emptyViewId) {
        addAction(new SetEmptyView(viewId, emptyViewId));
    }

    public void setChronometer(int viewId, long base, String format, boolean started) {
        setLong(viewId, "setBase", base);
        setString(viewId, "setFormat", format);
        setBoolean(viewId, "setStarted", started);
    }

    public void setProgressBar(int viewId, int max, int progress, boolean indeterminate) {
        setBoolean(viewId, "setIndeterminate", indeterminate);
        if (!indeterminate) {
            setInt(viewId, "setMax", max);
            setInt(viewId, "setProgress", progress);
        }
    }

    public void setOnClickPendingIntent(int viewId, PendingIntent pendingIntent) {
        addAction(new SetOnClickPendingIntent(viewId, pendingIntent));
    }

    public void setPendingIntentTemplate(int viewId, PendingIntent pendingIntentTemplate) {
        addAction(new SetPendingIntentTemplate(viewId, pendingIntentTemplate));
    }

    public void setOnClickFillInIntent(int viewId, Intent fillInIntent) {
        addAction(new SetOnClickFillInIntent(viewId, fillInIntent));
    }

    public void setDrawableParameters(int viewId, boolean targetBackground, int alpha, int colorFilter, Mode mode, int level) {
        addAction(new SetDrawableParameters(viewId, targetBackground, alpha, colorFilter, mode, level));
    }

    public void setProgressTintList(int viewId, ColorStateList tint) {
        addAction(new ReflectionAction(viewId, "setProgressTintList", 15, tint));
    }

    public void setProgressBackgroundTintList(int viewId, ColorStateList tint) {
        addAction(new ReflectionAction(viewId, "setProgressBackgroundTintList", 15, tint));
    }

    public void setProgressIndeterminateTintList(int viewId, ColorStateList tint) {
        addAction(new ReflectionAction(viewId, "setIndeterminateTintList", 15, tint));
    }

    public void setTextColor(int viewId, int color) {
        setInt(viewId, "setTextColor", color);
    }

    @Deprecated
    public void setRemoteAdapter(int appWidgetId, int viewId, Intent intent) {
        setRemoteAdapter(viewId, intent);
    }

    public void setRemoteAdapter(int viewId, Intent intent) {
        addAction(new SetRemoteViewsAdapterIntent(viewId, intent));
    }

    public void setRemoteAdapter(int viewId, ArrayList<RemoteViews> list, int viewTypeCount) {
        addAction(new SetRemoteViewsAdapterList(viewId, list, viewTypeCount));
    }

    public void setScrollPosition(int viewId, int position) {
        setInt(viewId, "smoothScrollToPosition", position);
    }

    public void setRelativeScrollPosition(int viewId, int offset) {
        setInt(viewId, "smoothScrollByOffset", offset);
    }

    public void setViewPadding(int viewId, int left, int top, int right, int bottom) {
        addAction(new ViewPaddingAction(viewId, left, top, right, bottom));
    }

    public void setBoolean(int viewId, String methodName, boolean value) {
        addAction(new ReflectionAction(viewId, methodName, 1, Boolean.valueOf(value)));
    }

    public void setByte(int viewId, String methodName, byte value) {
        addAction(new ReflectionAction(viewId, methodName, 2, Byte.valueOf(value)));
    }

    public void setShort(int viewId, String methodName, short value) {
        addAction(new ReflectionAction(viewId, methodName, 3, Short.valueOf(value)));
    }

    public void setInt(int viewId, String methodName, int value) {
        addAction(new ReflectionAction(viewId, methodName, 4, Integer.valueOf(value)));
    }

    public void setLong(int viewId, String methodName, long value) {
        addAction(new ReflectionAction(viewId, methodName, 5, Long.valueOf(value)));
    }

    public void setFloat(int viewId, String methodName, float value) {
        addAction(new ReflectionAction(viewId, methodName, 6, Float.valueOf(value)));
    }

    public void setDouble(int viewId, String methodName, double value) {
        addAction(new ReflectionAction(viewId, methodName, 7, Double.valueOf(value)));
    }

    public void setChar(int viewId, String methodName, char value) {
        addAction(new ReflectionAction(viewId, methodName, 8, Character.valueOf(value)));
    }

    public void setString(int viewId, String methodName, String value) {
        addAction(new ReflectionAction(viewId, methodName, 9, value));
    }

    public void setCharSequence(int viewId, String methodName, CharSequence value) {
        addAction(new ReflectionAction(viewId, methodName, 10, value));
    }

    public void setUri(int viewId, String methodName, Uri value) {
        if (value != null) {
            value = value.getCanonicalUri();
            if (StrictMode.vmFileUriExposureEnabled()) {
                value.checkFileUriExposed("RemoteViews.setUri()");
            }
        }
        addAction(new ReflectionAction(viewId, methodName, 11, value));
    }

    public void setBitmap(int viewId, String methodName, Bitmap value) {
        addAction(new BitmapReflectionAction(viewId, methodName, value));
    }

    public void setBundle(int viewId, String methodName, Bundle value) {
        addAction(new ReflectionAction(viewId, methodName, 13, value));
    }

    public void setIntent(int viewId, String methodName, Intent value) {
        addAction(new ReflectionAction(viewId, methodName, 14, value));
    }

    public void setIcon(int viewId, String methodName, Icon value) {
        addAction(new ReflectionAction(viewId, methodName, 16, value));
    }

    public void setContentDescription(int viewId, CharSequence contentDescription) {
        setCharSequence(viewId, "setContentDescription", contentDescription);
    }

    public void setAccessibilityTraversalBefore(int viewId, int nextId) {
        setInt(viewId, "setAccessibilityTraversalBefore", nextId);
    }

    public void setAccessibilityTraversalAfter(int viewId, int nextId) {
        setInt(viewId, "setAccessibilityTraversalAfter", nextId);
    }

    public void setLabelFor(int viewId, int labeledId) {
        setInt(viewId, "setLabelFor", labeledId);
    }

    private RemoteViews getRemoteViewsToApply(Context context) {
        if (!hasLandscapeAndPortraitLayouts()) {
            return this;
        }
        if (context.getResources().getConfiguration().orientation == 2) {
            return this.mLandscape;
        }
        return this.mPortrait;
    }

    public View apply(Context context, ViewGroup parent) {
        return apply(context, parent, null);
    }

    public View apply(Context context, ViewGroup parent, OnClickHandler handler) {
        RemoteViews rvToApply = getRemoteViewsToApply(context);
        if (Build.IS_SYSTEM_SECURE && this.isNotification) {
            updateResourceMap(context);
        }
        final Context contextForResources = getContextForResources(context);
        LayoutInflater inflater = ((LayoutInflater) context.getSystemService("layout_inflater")).cloneInContext(new ContextWrapper(context) {
            public Resources getResources() {
                return contextForResources.getResources();
            }

            public Theme getTheme() {
                return contextForResources.getTheme();
            }

            public String getPackageName() {
                return contextForResources.getPackageName();
            }
        });
        inflater.setFilter(this);
        View result = inflater.inflate(rvToApply.getLayoutId(), parent, false);
        rvToApply.performApply(result, parent, handler);
        return result;
    }

    public void reapply(Context context, View v) {
        reapply(context, v, null);
    }

    public void reapply(Context context, View v, OnClickHandler handler) {
        RemoteViews rvToApply = getRemoteViewsToApply(context);
        if (!hasLandscapeAndPortraitLayouts() || v.getId() == rvToApply.getLayoutId()) {
            rvToApply.performApply(v, (ViewGroup) v.getParent(), handler);
            return;
        }
        throw new RuntimeException("Attempting to re-apply RemoteViews to a view that that does not share the same root layout id.");
    }

    private void performApply(View v, ViewGroup parent, OnClickHandler handler) {
        if (this.mActions != null) {
            if (handler == null) {
                handler = DEFAULT_ON_CLICK_HANDLER;
            }
            int count = this.mActions.size();
            for (int i = 0; i < count; i++) {
                ((Action) this.mActions.get(i)).apply(v, parent, handler);
            }
        }
    }

    private Context getContextForResources(Context context) {
        if (!(this.mApplication == null || (context.getUserId() == UserHandle.getUserId(this.mApplication.uid) && context.getPackageName().equals(this.mApplication.packageName)))) {
            try {
                context = context.createApplicationContext(this.mApplication, 4);
            } catch (NameNotFoundException e) {
                Log.e(LOG_TAG, "Package name " + this.mApplication.packageName + " not found");
            }
        }
        return context;
    }

    public int getSequenceNumber() {
        return this.mActions == null ? 0 : this.mActions.size();
    }

    public boolean onLoadClass(Class clazz) {
        return clazz.isAnnotationPresent(RemoteView.class);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i = 1;
        if (hasLandscapeAndPortraitLayouts()) {
            dest.writeInt(1);
            if (this.mIsRoot) {
                this.mBitmapCache.writeBitmapsToParcel(dest, flags);
            }
            this.mLandscape.writeToParcel(dest, flags);
            this.mPortrait.writeToParcel(dest, flags);
        } else {
            int count;
            dest.writeInt(0);
            if (this.mIsRoot) {
                this.mBitmapCache.writeBitmapsToParcel(dest, flags);
            }
            dest.writeParcelable(this.mApplication, flags);
            dest.writeInt(this.mLayoutId);
            if (!this.mIsWidgetCollectionChild) {
                i = 0;
            }
            dest.writeInt(i);
            if (this.mActions != null) {
                count = this.mActions.size();
            } else {
                count = 0;
            }
            dest.writeInt(count);
            for (int i2 = 0; i2 < count; i2++) {
                ((Action) this.mActions.get(i2)).writeToParcel(dest, 0);
            }
        }
        if (Build.IS_SYSTEM_SECURE) {
            dest.writeValue(Boolean.valueOf(this.isNotification));
            dest.writeInt(this.stringNamesMap.size());
            for (CharSequence key : this.stringNamesMap.keySet()) {
                dest.writeCharSequence(key);
                dest.writeCharSequence((CharSequence) this.stringNamesMap.get(key));
            }
        }
    }

    private static ApplicationInfo getApplicationInfo(String packageName, int userId) {
        if (packageName == null) {
            return null;
        }
        Application application = ActivityThread.currentApplication();
        if (application == null) {
            throw new IllegalStateException("Cannot create remote views out of an aplication.");
        }
        ApplicationInfo applicationInfo = application.getApplicationInfo();
        if (UserHandle.getUserId(applicationInfo.uid) == userId && applicationInfo.packageName.equals(packageName)) {
            return applicationInfo;
        }
        try {
            return application.getBaseContext().createPackageContextAsUser(packageName, 0, new UserHandle(userId)).getApplicationInfo();
        } catch (NameNotFoundException e) {
            throw new IllegalArgumentException("No such package " + packageName);
        }
    }

    public void setLaunchPendingIntent(int viewId, PendingIntent pendingIntent) {
        addAction(new SetLaunchPendingIntent(viewId, pendingIntent));
    }

    public void setOnLongClickPendingIntent(IBinder key, int viewId, PendingIntent downPendingIntent, PendingIntent upPendingIntent) {
        addAction(new SetOnLongClickPendingIntent(key, viewId, downPendingIntent, upPendingIntent));
    }

    public void SetOnLongClickDragable(int viewId, ClipData clipData, PendingIntent dragStartNotiIntent, PendingIntent dragEnterNotiIntent, PendingIntent dragExitNotiIntent, boolean isNeedToRemove) {
        addAction(new SetOnLongClickDragable(viewId, clipData, dragStartNotiIntent, dragEnterNotiIntent, dragExitNotiIntent, isNeedToRemove));
    }

    public void setImageFilter(int viewId, int type, float arg1, float arg2, float arg3, float arg4, float arg5, float arg6) {
        addAction(new ImageFilterAction(viewId, type, arg1, arg2, arg3, arg4, arg5, arg6));
    }

    public void setOnTouchPendingIntent(int viewId, PendingIntent pendingIntent) {
        addAction(new SetOnTouchPendingIntent(viewId, pendingIntent));
    }
}
