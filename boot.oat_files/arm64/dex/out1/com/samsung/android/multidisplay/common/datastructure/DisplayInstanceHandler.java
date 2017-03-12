package com.samsung.android.multidisplay.common.datastructure;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class DisplayInstanceHandler extends ContextRelatedInstanceHandler {
    private static final ArrayList<DisplayInstanceWrapper> mDisplayWrapperList = new ArrayList();

    public DisplayInstanceHandler() {
        super(mDisplayWrapperList, "DisplayInstanceWrapper");
        this.TAG = DisplayInstanceWrapper.TAG;
    }

    public boolean createContextRelatedInstance(Context creator, Object object) {
        if (DEBUG) {
            Log.d(this.TAG, "createContextRelatedInstance() : context=" + creator.getOuterContext());
        }
        if (object == null || !(object instanceof Display)) {
            return false;
        }
        DisplayInstanceWrapper creatorDisplayInstanceWrapper;
        ContextRelatedInstance relatedInstance = findContextRelatedInstance(creator);
        if (relatedInstance == null || !(relatedInstance instanceof DisplayInstanceWrapper)) {
            creatorDisplayInstanceWrapper = new DisplayInstanceWrapper(creator);
            mDisplayWrapperList.add(creatorDisplayInstanceWrapper);
        } else {
            if (DEBUG) {
                Log.w(this.TAG, "createContextRelatedInstance() : instance already exist for creator!");
            }
            creatorDisplayInstanceWrapper = (DisplayInstanceWrapper) relatedInstance;
        }
        creatorDisplayInstanceWrapper.addDisplayRef((Display) object);
        garbageCollect();
        return true;
    }

    public void dump(PrintWriter pw, String prefix, boolean dumpDetail, boolean dumpCallStack) {
        if (DEBUG) {
            Log.d(this.TAG, "dump()");
        }
        String innerPrefix = prefix + "  ";
        pw.print(prefix);
        pw.println("Display instance handler : ");
        pw.print(innerPrefix);
        pw.print(DisplayInstanceWrapper.class.getSimpleName() + " size=");
        pw.println(mDisplayWrapperList.size());
        if (mDisplayWrapperList.size() > 0) {
            pw.print(innerPrefix);
            pw.println("display wrappers = {");
            Iterator i$ = mDisplayWrapperList.iterator();
            while (i$.hasNext()) {
                ContextRelatedInstance relatedInstance = (ContextRelatedInstance) i$.next();
                if (relatedInstance instanceof DisplayInstanceWrapper) {
                    DisplayInstanceWrapper displayWrapper = (DisplayInstanceWrapper) relatedInstance;
                    if (DEBUG) {
                        Log.d(this.TAG, "dump() : " + displayWrapper);
                    }
                    if (dumpDetail) {
                        displayWrapper.dump(pw, innerPrefix + "  ", dumpCallStack);
                    } else {
                        pw.print(innerPrefix + "  ");
                        pw.println(displayWrapper);
                    }
                }
            }
            pw.print(innerPrefix);
            pw.println("}");
        }
    }
}
