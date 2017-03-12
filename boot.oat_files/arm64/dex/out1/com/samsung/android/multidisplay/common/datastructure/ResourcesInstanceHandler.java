package com.samsung.android.multidisplay.common.datastructure;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class ResourcesInstanceHandler extends ContextRelatedInstanceHandler {
    private static final ArrayList<ResourcesInstanceWrapper> mResourcesWrapperList = new ArrayList();

    public ResourcesInstanceHandler() {
        super(mResourcesWrapperList, "ResourcesInstanceWrapper");
        this.TAG = ResourcesInstanceWrapper.TAG;
    }

    public boolean createContextRelatedInstance(Context creator, Object object) {
        if (DEBUG) {
            Log.d(this.TAG, "createContextRelatedInstance() : context=" + creator.getOuterContext());
        }
        if (object == null || !(object instanceof Resources)) {
            return false;
        }
        ResourcesInstanceWrapper creatorResourcesInstanceWrapper;
        ContextRelatedInstance relatedInstance = findContextRelatedInstance(creator);
        if (relatedInstance == null || !(relatedInstance instanceof ResourcesInstanceWrapper)) {
            creatorResourcesInstanceWrapper = new ResourcesInstanceWrapper(creator);
            if (DEBUG) {
                Log.w(this.TAG, "createContextRelatedInstance() : created instances wrapper for creator!");
            }
            mResourcesWrapperList.add(creatorResourcesInstanceWrapper);
        } else {
            creatorResourcesInstanceWrapper = (ResourcesInstanceWrapper) relatedInstance;
        }
        creatorResourcesInstanceWrapper.addResourcesRef((Resources) object);
        garbageCollect();
        return true;
    }

    public void dump(PrintWriter pw, String prefix, boolean dumpDetail, boolean dumpCallStack) {
        if (DEBUG) {
            Log.d(this.TAG, "dump()");
        }
        String innerPrefix = prefix + "  ";
        pw.print(prefix);
        pw.println("Resources instance handler : ");
        pw.print(innerPrefix);
        pw.print(ResourcesInstanceWrapper.class.getSimpleName() + " size=");
        pw.println(mResourcesWrapperList.size());
        if (mResourcesWrapperList.size() > 0) {
            pw.print(innerPrefix);
            pw.println("resources wrappers = {");
            Iterator i$ = mResourcesWrapperList.iterator();
            while (i$.hasNext()) {
                ContextRelatedInstance relatedInstance = (ContextRelatedInstance) i$.next();
                if (relatedInstance instanceof ResourcesInstanceWrapper) {
                    ResourcesInstanceWrapper resourcesWrapper = (ResourcesInstanceWrapper) relatedInstance;
                    if (DEBUG) {
                        Log.d(this.TAG, "dump() : " + resourcesWrapper);
                    }
                    if (dumpDetail) {
                        resourcesWrapper.dump(pw, innerPrefix + "  ", dumpCallStack);
                    } else {
                        pw.print(innerPrefix + "  ");
                        pw.println(resourcesWrapper);
                    }
                }
            }
            pw.print(innerPrefix);
            pw.println("}");
        }
    }
}
