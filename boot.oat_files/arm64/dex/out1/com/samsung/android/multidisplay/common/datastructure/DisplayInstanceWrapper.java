package com.samsung.android.multidisplay.common.datastructure;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.graphics.Point;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Debug;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import com.samsung.android.multidisplay.common.ContextRelationManager;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

class DisplayInstanceWrapper implements ContextRelatedInstance {
    private static final String CALLSTACK_DUMP_PREFIX = "                         ";
    public static final boolean DEBUG = DisplayInstanceHandler.DEBUG;
    public static final boolean DUMP_DETAIL = ContextRelationManager.DUMP_DETAIL;
    public static boolean PROPAGATION_ENABLED = false;
    public static final String TAG = "DisplayInstanceHandler";
    WeakReference<Context> contextRef;
    private String mContextCallStack = "";
    private long mContextTimeStamp = 0;
    private ArrayList<String> mDisplayCallStacks;
    private LinkedList<WeakReference<Display>> mDisplayRefList;
    private ArrayList<Long> mDisplayTimeStamps;

    public DisplayInstanceWrapper(Context _itemRef) {
        this.contextRef = new WeakReference(_itemRef);
        this.mDisplayRefList = new LinkedList();
        if (DUMP_DETAIL) {
            this.mDisplayCallStacks = new ArrayList();
            this.mDisplayTimeStamps = new ArrayList();
            this.mContextTimeStamp = System.currentTimeMillis();
            this.mContextCallStack = Debug.getCallers(25, CALLSTACK_DUMP_PREFIX);
            this.mContextCallStack = ContextRelationManager.trimCallStack(this.mContextCallStack);
        }
    }

    public String getCallStack() {
        return this.mContextCallStack;
    }

    public void addDisplayRef(Display display) {
        if (display != null) {
            int refSize = 0;
            if (DEBUG) {
                refSize = this.mDisplayRefList.size();
            }
            Iterator i$ = this.mDisplayRefList.iterator();
            while (i$.hasNext()) {
                Display tempDisplay = (Display) ((WeakReference) i$.next()).get();
                if (tempDisplay != null && tempDisplay == display) {
                    return;
                }
            }
            this.mDisplayRefList.add(new WeakReference(display));
            if (DUMP_DETAIL) {
                this.mDisplayCallStacks.add(ContextRelationManager.trimCallStack(Debug.getCallers(25, CALLSTACK_DUMP_PREFIX)));
                this.mDisplayTimeStamps.add(Long.valueOf(System.currentTimeMillis()));
            }
            if (DEBUG) {
                Log.d(TAG, "addDisplayRef() : display= " + display + " refSize=" + refSize + " final size=" + this.mDisplayRefList.size());
            }
        }
    }

    public void updateDisplay(int displayId) {
        Context context = (Context) this.contextRef.get();
        if (context == null) {
            return;
        }
        if (PROPAGATION_ENABLED) {
            if (DEBUG) {
                Log.d(TAG, "update display of " + context.getOuterContext());
            }
            if (context instanceof Activity) {
                IBinder token = ((Activity) context).getActivityToken();
            }
            Display display = DisplayManagerGlobal.getInstance().getCompatibleDisplay(displayId, context.getDisplayAdjustments(displayId));
            if (display != null) {
                Iterator i$ = this.mDisplayRefList.iterator();
                while (i$.hasNext()) {
                    WeakReference<Display> displayRef = (WeakReference) i$.next();
                    Display targetDisplay = (Display) displayRef.get();
                    if (!(targetDisplay == null || targetDisplay.getDisplayId() == displayId)) {
                        if (DEBUG) {
                            StringBuilder sb = new StringBuilder(128);
                            sb.append("{d" + display.getDisplayId());
                            sb.append(" " + display.getClass().getSimpleName() + "@0x");
                            sb.append(Integer.toHexString(System.identityHashCode(display)));
                            sb.append(" layerStack=" + display.getLayerStack());
                            Point size = new Point();
                            display.getSize(size);
                            sb.append(" size=" + size);
                            int id = this.mDisplayRefList.indexOf(displayRef);
                            if (DUMP_DETAIL) {
                                Date time = new Date(((Long) this.mDisplayTimeStamps.get(id)).longValue());
                                synchronized (ContextRelationManager.TIME_STAMP_FORMAT) {
                                    sb.append(" " + ContextRelationManager.TIME_STAMP_FORMAT.format(time));
                                }
                            }
                            sb.append("}");
                            Log.d(TAG, "update display of " + sb.toString() + " to d" + displayId);
                        }
                        targetDisplay.setTo(displayId);
                    }
                }
                return;
            }
            return;
        }
        Log.d(TAG, "BLOCKED :: updating display of " + context.getOuterContext());
    }

    public int getDisplayRefSize() {
        int displayRefSize = 0;
        Iterator i$ = this.mDisplayRefList.iterator();
        while (i$.hasNext()) {
            if (((Display) ((WeakReference) i$.next()).get()) != null) {
                displayRefSize++;
            }
        }
        return displayRefSize;
    }

    public void release() {
        this.mDisplayRefList.clear();
        if (DUMP_DETAIL) {
            this.mDisplayCallStacks.clear();
            this.mDisplayTimeStamps.clear();
        }
    }

    public void garbageCollect() {
        LinkedList<WeakReference<Display>> removingDisplayRefList = new LinkedList();
        Iterator i$ = this.mDisplayRefList.iterator();
        while (i$.hasNext()) {
            WeakReference<Display> tempRef = (WeakReference) i$.next();
            if (((Display) tempRef.get()) == null) {
                removingDisplayRefList.add(tempRef);
            }
        }
        i$ = removingDisplayRefList.iterator();
        while (i$.hasNext()) {
            tempRef = (WeakReference) i$.next();
            if (DEBUG) {
                Log.w(TAG, "garbageCollect() : removing " + tempRef);
            }
            int index = this.mDisplayRefList.indexOf(tempRef);
            this.mDisplayRefList.remove(index);
            if (DUMP_DETAIL) {
                this.mDisplayCallStacks.remove(index);
                this.mDisplayTimeStamps.remove(index);
            }
        }
        removingDisplayRefList.clear();
        if (DEBUG) {
            Log.d(TAG, "garbageCollect() : " + toString());
        }
    }

    public Context getObject() {
        return this.contextRef != null ? (Context) this.contextRef.get() : null;
    }

    public void updateRelatedInfo(Object... args) {
        try {
            if (args.length > 0) {
                updateDisplay(((Integer) args[0]).intValue());
            }
        } catch (ClassCastException e) {
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("Context {");
        Context context = (Context) this.contextRef.get();
        if (context != null) {
            sb.append("d" + context.getDisplayId() + " ");
            Context outerContext = context.getOuterContext();
            if (outerContext instanceof Activity) {
                sb.append("a ");
            } else if (outerContext instanceof Service) {
                sb.append("s ");
            } else if (outerContext instanceof Application) {
                sb.append("p ");
            } else if (outerContext instanceof ContextThemeWrapper) {
                sb.append("t ");
            } else {
                sb.append("c ");
            }
            sb.append(outerContext.getClass().getSimpleName());
            sb.append("@0x" + Integer.toHexString(System.identityHashCode(outerContext)));
            if (DUMP_DETAIL) {
                Date time = new Date(this.mContextTimeStamp);
                synchronized (ContextRelationManager.TIME_STAMP_FORMAT) {
                    sb.append(" " + ContextRelationManager.TIME_STAMP_FORMAT.format(time));
                }
            }
            sb.append(" displaySize=" + getDisplayRefSize());
        } else {
            sb.append("null");
        }
        sb.append("}");
        return sb.toString();
    }

    public void dump(PrintWriter pw, String prefix, boolean dumpCallStack) {
        String innerPrefix = prefix + "   ";
        pw.print(prefix);
        pw.println(toString());
        Iterator i$ = this.mDisplayRefList.iterator();
        while (i$.hasNext()) {
            WeakReference<Display> displayRef = (WeakReference) i$.next();
            Display display = (Display) displayRef.get();
            int id = this.mDisplayRefList.indexOf(displayRef);
            pw.print(innerPrefix);
            pw.print(Display.class.getSimpleName() + " {");
            if (display != null) {
                pw.print("d" + display.getDisplayId());
                pw.print(" " + display.getClass().getSimpleName() + "@0x");
                pw.print(Integer.toHexString(System.identityHashCode(display)));
                pw.print(" layerStack=" + display.getLayerStack());
                Point size = new Point();
                display.getSize(size);
                pw.print(" size=" + size);
                if (DUMP_DETAIL) {
                    Date time = new Date(((Long) this.mDisplayTimeStamps.get(id)).longValue());
                    synchronized (ContextRelationManager.TIME_STAMP_FORMAT) {
                        pw.print(" " + ContextRelationManager.TIME_STAMP_FORMAT.format(time));
                    }
                }
            } else {
                pw.print("null");
            }
            pw.println("}");
            if (DUMP_DETAIL && dumpCallStack) {
                pw.print((String) this.mDisplayCallStacks.get(id));
            }
        }
        if (this.mDisplayRefList.size() == 0) {
            pw.print(innerPrefix);
            pw.print("no reference");
        }
    }
}
