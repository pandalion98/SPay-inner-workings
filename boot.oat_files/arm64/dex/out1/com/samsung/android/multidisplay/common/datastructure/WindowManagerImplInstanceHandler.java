package com.samsung.android.multidisplay.common.datastructure;

import android.content.Context;
import android.util.Log;
import android.view.WindowManagerImpl;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class WindowManagerImplInstanceHandler extends ContextRelatedInstanceHandler {
    private static final ArrayList<WindowManagerImplInstanceWrapper> mWindowManagerImplWrapperList = new ArrayList();

    public WindowManagerImplInstanceHandler() {
        super(mWindowManagerImplWrapperList, WindowManagerImplInstanceWrapper.class.getSimpleName());
        this.TAG = WindowManagerImplInstanceHandler.class.getSimpleName();
    }

    public boolean createContextRelatedInstance(Context creator, Object object) {
        if (DEBUG) {
            Log.d(this.TAG, "createContextRelatedInstance() : context=" + creator.getOuterContext());
        }
        if (object == null || !(object instanceof WindowManagerImpl)) {
            return false;
        }
        WindowManagerImplInstanceWrapper creatorWindowManagerImplWrapper;
        ContextRelatedInstance relatedInstance = findContextRelatedInstance(creator);
        if (relatedInstance == null || !(relatedInstance instanceof WindowManagerImplInstanceWrapper)) {
            creatorWindowManagerImplWrapper = new WindowManagerImplInstanceWrapper(creator);
            mWindowManagerImplWrapperList.add(creatorWindowManagerImplWrapper);
        } else {
            if (DEBUG) {
                Log.w(this.TAG, "createContextRelatedInstance() : instance already exist for creator!");
            }
            creatorWindowManagerImplWrapper = (WindowManagerImplInstanceWrapper) relatedInstance;
        }
        creatorWindowManagerImplWrapper.addWindowManagerImplRef((WindowManagerImpl) object);
        if (DEBUG) {
            Log.d(this.TAG, "createContextRelatedInstance() : add referce (size=" + mWindowManagerImplWrapperList.size() + " for creator " + creator.getOuterContext());
        }
        garbageCollect();
        return true;
    }

    public void dump(PrintWriter pw, String prefix, boolean dumpDetail, boolean dumpCallStack) {
        if (DEBUG) {
            Log.d(this.TAG, "dump()");
        }
        String innerPrefix = prefix + "  ";
        pw.print(prefix);
        pw.println("WindowManagerImpl instance handler : ");
        pw.print(innerPrefix);
        pw.print(WindowManagerImplInstanceWrapper.class.getSimpleName() + " size=");
        pw.println(mWindowManagerImplWrapperList.size());
        if (mWindowManagerImplWrapperList.size() > 0) {
            pw.print(innerPrefix);
            pw.println("window manager wrappers = {");
            Iterator i$ = mWindowManagerImplWrapperList.iterator();
            while (i$.hasNext()) {
                ContextRelatedInstance relatedInstance = (ContextRelatedInstance) i$.next();
                if (relatedInstance instanceof WindowManagerImplInstanceWrapper) {
                    WindowManagerImplInstanceWrapper windowManagerImplWrapper = (WindowManagerImplInstanceWrapper) relatedInstance;
                    if (DEBUG) {
                        Log.d(this.TAG, "dump() : " + windowManagerImplWrapper);
                    }
                    if (dumpDetail) {
                        windowManagerImplWrapper.dump(pw, innerPrefix + "  ", dumpCallStack);
                    } else {
                        pw.print(innerPrefix + "  ");
                        pw.println(windowManagerImplWrapper);
                    }
                }
            }
            pw.print(innerPrefix);
            pw.println("}");
        }
    }
}
