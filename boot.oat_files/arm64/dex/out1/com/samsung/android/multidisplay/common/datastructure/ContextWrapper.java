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
import com.samsung.android.multidisplay.common.ContextRelationManager;
import java.lang.ref.WeakReference;
import java.util.Date;

class ContextWrapper implements RelationObject {
    private static final String CALLSTACK_DUMP_PREFIX = "                         ";
    public static final boolean DEBUG = ContextRelationManager.DEBUG;
    public static final boolean DUMP_DETAIL = ContextRelationManager.DUMP_DETAIL;
    public static final String TAG = "ContextRelationGraph";
    String callStack = "";
    WeakReference<Context> itemRef;
    long timeStamp = 0;

    public ContextWrapper(Context _itemRef) {
        this.itemRef = new WeakReference(_itemRef);
        this.timeStamp = System.currentTimeMillis();
        if (DUMP_DETAIL) {
            this.callStack = Debug.getCallers(25, CALLSTACK_DUMP_PREFIX);
            this.callStack = ContextRelationManager.trimCallStack(this.callStack);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("{");
        Context context = (Context) this.itemRef.get();
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
            Date time = new Date(this.timeStamp);
            synchronized (ContextRelationManager.TIME_STAMP_FORMAT) {
                sb.append(" " + ContextRelationManager.TIME_STAMP_FORMAT.format(time));
            }
        } else {
            sb.append("null");
        }
        sb.append("}");
        return sb.toString();
    }

    public String getCallStack() {
        return this.callStack;
    }

    public boolean equals(Object o) {
        if (o instanceof ContextWrapper) {
            return ((ContextWrapper) o).getObject() == getObject();
        } else {
            throw new ClassCastException();
        }
    }

    public void updateRelatedInfo(Object... args) {
        try {
            if (args.length > 0) {
                Context context = (Context) this.itemRef.get();
                if (context != null) {
                    if (DEBUG) {
                        Log.d(TAG, "update context of " + context.getOuterContext());
                    }
                    int displayId = ((Integer) args[0]).intValue();
                    if (context instanceof Activity) {
                        IBinder token = ((Activity) context).getActivityToken();
                    }
                    Display display = DisplayManagerGlobal.getInstance().getCompatibleDisplay(displayId, context.getDisplayAdjustments(displayId));
                    if (display != null) {
                        context.setDisplay(display);
                    }
                }
            }
        } catch (ClassCastException e) {
        }
    }

    public Context getObject() {
        return this.itemRef != null ? (Context) this.itemRef.get() : null;
    }
}
