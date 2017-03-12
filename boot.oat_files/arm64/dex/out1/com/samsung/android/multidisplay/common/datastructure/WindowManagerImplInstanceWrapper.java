package com.samsung.android.multidisplay.common.datastructure;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Debug;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.WindowManagerImpl;
import com.samsung.android.multidisplay.common.ContextRelationManager;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

class WindowManagerImplInstanceWrapper implements ContextRelatedInstance {
    private static final String CALLSTACK_DUMP_PREFIX = "                         ";
    public static final boolean DEBUG = WindowManagerImplInstanceHandler.DEBUG;
    public static final boolean DUMP_DETAIL = ContextRelationManager.DUMP_DETAIL;
    public static final String TAG = "WindowManagerImplInstanceHandler";
    WeakReference<Context> contextRef;
    private String mContextCallStack = "";
    private long mContextTimeStamp = 0;
    private ArrayList<String> mWindowManagerImplCallStacks;
    private LinkedList<WeakReference<WindowManagerImpl>> mWindowManagerImplRefList;
    private ArrayList<Long> mWindowManagerImplTimeStamps;

    public WindowManagerImplInstanceWrapper(Context _itemRef) {
        this.contextRef = new WeakReference(_itemRef);
        this.mWindowManagerImplRefList = new LinkedList();
        if (DUMP_DETAIL) {
            this.mWindowManagerImplCallStacks = new ArrayList();
            this.mWindowManagerImplTimeStamps = new ArrayList();
            this.mContextTimeStamp = System.currentTimeMillis();
            this.mContextCallStack = Debug.getCallers(25, CALLSTACK_DUMP_PREFIX);
            this.mContextCallStack = ContextRelationManager.trimCallStack(this.mContextCallStack);
        }
    }

    public String getCallStack() {
        return this.mContextCallStack;
    }

    public void addWindowManagerImplRef(WindowManagerImpl wm) {
        if (wm != null) {
            int refSize = 0;
            if (DEBUG) {
                refSize = this.mWindowManagerImplRefList.size();
            }
            Iterator i$ = this.mWindowManagerImplRefList.iterator();
            while (i$.hasNext()) {
                WindowManagerImpl tempWindowManagerImpl = (WindowManagerImpl) ((WeakReference) i$.next()).get();
                if (tempWindowManagerImpl != null && tempWindowManagerImpl == wm) {
                    return;
                }
            }
            this.mWindowManagerImplRefList.add(new WeakReference(wm));
            if (DUMP_DETAIL) {
                this.mWindowManagerImplCallStacks.add(ContextRelationManager.trimCallStack(Debug.getCallers(25, CALLSTACK_DUMP_PREFIX)));
                this.mWindowManagerImplTimeStamps.add(Long.valueOf(System.currentTimeMillis()));
            }
            if (DEBUG) {
                Log.d(TAG, "addDisplayRef() : wm= " + wm + " refSize=" + refSize + " final size=" + this.mWindowManagerImplRefList.size());
            }
        }
    }

    public void updateDisplay(int displayId) {
        Context context = (Context) this.contextRef.get();
        if (context != null) {
            if (DEBUG) {
                Log.d(TAG, "update window manager of " + context.getOuterContext());
            }
            IBinder token = null;
            if (context instanceof Activity) {
                token = ((Activity) context).getActivityToken();
            }
            Display display = DisplayManagerGlobal.getInstance().getCompatibleDisplay(displayId, context.getDisplayAdjustments(displayId));
            if (DEBUG) {
                Log.d(TAG, "updateDisplay() : displayId=" + displayId + " display=" + display + " token=" + token + " mWindowManagerImplRefList size=" + this.mWindowManagerImplRefList.size());
            }
            if (display != null) {
                Iterator i$ = this.mWindowManagerImplRefList.iterator();
                while (i$.hasNext()) {
                    WeakReference<WindowManagerImpl> wmRef = (WeakReference) i$.next();
                    WindowManagerImpl wm = (WindowManagerImpl) wmRef.get();
                    if (!(wm == null || wm.getDefaultDisplay().getDisplayId() == displayId)) {
                        if (DEBUG) {
                            StringBuilder sb = new StringBuilder(128);
                            sb.append("{d" + wm.getDefaultDisplay().getDisplayId() + " ");
                            sb.append(wm.getClass().getSimpleName() + "@0x");
                            sb.append(Integer.toHexString(System.identityHashCode(wm)));
                            int id = this.mWindowManagerImplRefList.indexOf(wmRef);
                            if (DUMP_DETAIL) {
                                Date time = new Date(((Long) this.mWindowManagerImplTimeStamps.get(id)).longValue());
                                synchronized (ContextRelationManager.TIME_STAMP_FORMAT) {
                                    sb.append(" " + ContextRelationManager.TIME_STAMP_FORMAT.format(time));
                                }
                            }
                            sb.append("}");
                            Log.d(TAG, "update display of " + sb.toString() + " to d" + displayId);
                        }
                        wm.setDisplay(display);
                    }
                }
            }
        }
    }

    public int getWindowManagerImplRefSize() {
        int wmRefSize = 0;
        Iterator i$ = this.mWindowManagerImplRefList.iterator();
        while (i$.hasNext()) {
            if (((WindowManagerImpl) ((WeakReference) i$.next()).get()) != null) {
                wmRefSize++;
            }
        }
        return wmRefSize;
    }

    public void release() {
        this.mWindowManagerImplRefList.clear();
        if (DUMP_DETAIL) {
            this.mWindowManagerImplCallStacks.clear();
            this.mWindowManagerImplTimeStamps.clear();
        }
    }

    public void garbageCollect() {
        LinkedList<WeakReference<WindowManagerImpl>> removingWindowManagerImplRefList = new LinkedList();
        Iterator i$ = this.mWindowManagerImplRefList.iterator();
        while (i$.hasNext()) {
            WeakReference<WindowManagerImpl> tempRef = (WeakReference) i$.next();
            if (((WindowManagerImpl) tempRef.get()) == null) {
                removingWindowManagerImplRefList.add(tempRef);
            }
        }
        i$ = removingWindowManagerImplRefList.iterator();
        while (i$.hasNext()) {
            tempRef = (WeakReference) i$.next();
            if (DEBUG) {
                Log.w(TAG, "garbageCollect() : removing " + tempRef);
            }
            int index = this.mWindowManagerImplRefList.indexOf(tempRef);
            this.mWindowManagerImplRefList.remove(index);
            if (DUMP_DETAIL) {
                this.mWindowManagerImplCallStacks.remove(index);
                this.mWindowManagerImplTimeStamps.remove(index);
            }
        }
        removingWindowManagerImplRefList.clear();
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
            sb.append(" wmSize=" + getWindowManagerImplRefSize());
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
        Iterator i$ = this.mWindowManagerImplRefList.iterator();
        while (i$.hasNext()) {
            WeakReference<WindowManagerImpl> wmRef = (WeakReference) i$.next();
            WindowManagerImpl wm = (WindowManagerImpl) wmRef.get();
            int id = this.mWindowManagerImplRefList.indexOf(wmRef);
            pw.print(innerPrefix);
            pw.print(WindowManagerImpl.class.getSimpleName() + " {");
            if (wm != null) {
                pw.print("d" + wm.getDefaultDisplay().getDisplayId() + " ");
                pw.print(wm.getClass().getSimpleName() + "@0x");
                pw.print(Integer.toHexString(System.identityHashCode(wm)));
                if (DUMP_DETAIL) {
                    Date time = new Date(((Long) this.mWindowManagerImplTimeStamps.get(id)).longValue());
                    synchronized (ContextRelationManager.TIME_STAMP_FORMAT) {
                        pw.print(" " + ContextRelationManager.TIME_STAMP_FORMAT.format(time));
                    }
                }
            } else {
                pw.print("null");
            }
            pw.println("}");
            if (DUMP_DETAIL && dumpCallStack) {
                pw.print((String) this.mWindowManagerImplCallStacks.get(id));
            }
        }
        if (this.mWindowManagerImplRefList.size() == 0) {
            pw.print(innerPrefix);
            pw.print("no reference");
        }
    }
}
