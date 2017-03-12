package com.samsung.android.multidisplay.common.datastructure;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
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

class ResourcesInstanceWrapper implements ContextRelatedInstance {
    private static final String CALLSTACK_DUMP_PREFIX = "                         ";
    public static final boolean DEBUG = ResourcesInstanceHandler.DEBUG;
    public static final boolean DUMP_DETAIL = ContextRelationManager.DUMP_DETAIL;
    public static boolean PROPAGATION_ENABLED = false;
    public static final String TAG = "ResourcesInstanceHandler";
    WeakReference<Context> contextRef;
    private String mContextCallStack = "";
    private long mContextTimeStamp = 0;
    private ArrayList<String> mResourcesCallStacks;
    private LinkedList<WeakReference<Resources>> mResourcesRefList;
    private ArrayList<Long> mResourcesTimeStamps;

    public ResourcesInstanceWrapper(Context _itemRef) {
        this.contextRef = new WeakReference(_itemRef);
        this.mResourcesRefList = new LinkedList();
        if (DUMP_DETAIL) {
            this.mResourcesCallStacks = new ArrayList();
            this.mResourcesTimeStamps = new ArrayList();
            this.mContextTimeStamp = System.currentTimeMillis();
            this.mContextCallStack = Debug.getCallers(25, CALLSTACK_DUMP_PREFIX);
            this.mContextCallStack = ContextRelationManager.trimCallStack(this.mContextCallStack);
        }
    }

    public String getCallStack() {
        return this.mContextCallStack;
    }

    public void addResourcesRef(Resources res) {
        if (res != null) {
            int refSize = 0;
            if (DEBUG) {
                refSize = this.mResourcesRefList.size();
            }
            Iterator i$ = this.mResourcesRefList.iterator();
            while (i$.hasNext()) {
                Resources tempResources = (Resources) ((WeakReference) i$.next()).get();
                if (tempResources != null && tempResources == res) {
                    return;
                }
            }
            this.mResourcesRefList.add(new WeakReference(res));
            if (DUMP_DETAIL) {
                this.mResourcesCallStacks.add(ContextRelationManager.trimCallStack(Debug.getCallers(25, CALLSTACK_DUMP_PREFIX)));
                this.mResourcesTimeStamps.add(Long.valueOf(System.currentTimeMillis()));
            }
            if (DEBUG) {
                Log.d(TAG, "addDisplayRef() : res= " + res + " refSize=" + refSize + " final size=" + this.mResourcesRefList.size());
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
                Iterator i$ = this.mResourcesRefList.iterator();
                while (i$.hasNext()) {
                    WeakReference<Resources> resourcesRef = (WeakReference) i$.next();
                    Resources res = (Resources) resourcesRef.get();
                    if (!(res == null || res.getConfiguration().displayId == displayId)) {
                        if (DEBUG) {
                            StringBuilder sb = new StringBuilder(128);
                            sb.append("{d" + res.getConfiguration().displayId);
                            sb.append(" " + res.getClass().getSimpleName() + "@0x");
                            sb.append(Integer.toHexString(System.identityHashCode(res)));
                            int id = this.mResourcesRefList.indexOf(resourcesRef);
                            if (DUMP_DETAIL) {
                                Date time = new Date(((Long) this.mResourcesTimeStamps.get(id)).longValue());
                                synchronized (ContextRelationManager.TIME_STAMP_FORMAT) {
                                    sb.append(" " + ContextRelationManager.TIME_STAMP_FORMAT.format(time));
                                }
                            }
                            sb.append("}");
                            Log.d(TAG, "update display of " + sb.toString() + " to d" + displayId);
                        }
                        res.updateConfiguration(display);
                    }
                }
                return;
            }
            return;
        }
        Log.d(TAG, "BLOCKED :: updating display of " + context.getOuterContext());
    }

    public int getResourcesRefSize() {
        int resourcesRefSize = 0;
        Iterator i$ = this.mResourcesRefList.iterator();
        while (i$.hasNext()) {
            if (((Resources) ((WeakReference) i$.next()).get()) != null) {
                resourcesRefSize++;
            }
        }
        return resourcesRefSize;
    }

    public void release() {
        this.mResourcesRefList.clear();
        if (DUMP_DETAIL) {
            this.mResourcesCallStacks.clear();
            this.mResourcesTimeStamps.clear();
        }
    }

    public void garbageCollect() {
        LinkedList<WeakReference<Resources>> removingResourcesRefList = new LinkedList();
        Iterator i$ = this.mResourcesRefList.iterator();
        while (i$.hasNext()) {
            WeakReference<Resources> tempRef = (WeakReference) i$.next();
            if (((Resources) tempRef.get()) == null) {
                removingResourcesRefList.add(tempRef);
            }
        }
        i$ = removingResourcesRefList.iterator();
        while (i$.hasNext()) {
            tempRef = (WeakReference) i$.next();
            if (DEBUG) {
                Log.w(TAG, "garbageCollect() : removing " + tempRef);
            }
            int index = this.mResourcesRefList.indexOf(tempRef);
            this.mResourcesRefList.remove(index);
            if (DUMP_DETAIL) {
                this.mResourcesCallStacks.remove(index);
                this.mResourcesTimeStamps.remove(index);
            }
        }
        removingResourcesRefList.clear();
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
            sb.append(" resourcesSize=" + getResourcesRefSize());
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
        Iterator i$ = this.mResourcesRefList.iterator();
        while (i$.hasNext()) {
            WeakReference<Resources> resourcesRef = (WeakReference) i$.next();
            Resources res = (Resources) resourcesRef.get();
            int id = this.mResourcesRefList.indexOf(resourcesRef);
            pw.print(innerPrefix);
            pw.print(Resources.class.getSimpleName() + " {");
            if (res != null) {
                pw.print("d" + res.getConfiguration().displayId);
                pw.print(" " + res.getClass().getSimpleName() + "@0x");
                pw.print(Integer.toHexString(System.identityHashCode(res)));
                if (DUMP_DETAIL) {
                    Date time = new Date(((Long) this.mResourcesTimeStamps.get(id)).longValue());
                    synchronized (ContextRelationManager.TIME_STAMP_FORMAT) {
                        pw.print(" " + ContextRelationManager.TIME_STAMP_FORMAT.format(time));
                    }
                }
            } else {
                pw.print("null");
            }
            pw.println("}");
            if (DUMP_DETAIL && dumpCallStack) {
                pw.print((String) this.mResourcesCallStacks.get(id));
            }
        }
        if (this.mResourcesRefList.size() == 0) {
            pw.print(innerPrefix);
            pw.print("no reference");
        }
    }
}
