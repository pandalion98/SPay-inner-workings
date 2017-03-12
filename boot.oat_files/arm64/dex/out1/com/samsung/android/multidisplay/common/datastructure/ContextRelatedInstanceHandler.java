package com.samsung.android.multidisplay.common.datastructure;

import android.content.Context;
import android.util.Log;
import com.samsung.android.multidisplay.common.ContextRelationManager;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class ContextRelatedInstanceHandler {
    static final boolean DEBUG = ContextRelationManager.DEBUG;
    static final boolean DUMP_DETAIL = ContextRelationManager.DUMP_DETAIL;
    protected String TAG;
    protected final ArrayList<? extends ContextRelatedInstance> mContextRelatedInstanceList;
    final String mInstanceType;

    public abstract boolean createContextRelatedInstance(Context context, Object obj);

    public abstract void dump(PrintWriter printWriter, String str, boolean z, boolean z2);

    public ContextRelatedInstanceHandler(ArrayList<? extends ContextRelatedInstance> list, String instanceType) {
        if (list == null) {
            throw new NullPointerException("list is null");
        }
        this.mContextRelatedInstanceList = list;
        this.mInstanceType = instanceType;
    }

    public void propagateChangedRelationInfo(ArrayList<Context> contextList, int displayId) {
        if (contextList != null && contextList.size() > 0) {
            Iterator it = contextList.iterator();
            while (it.hasNext()) {
                Context relationContext = (Context) it.next();
                if (DEBUG) {
                    Log.d(this.TAG, " propagate to " + relationContext);
                }
                if (relationContext != null && (relationContext instanceof Context)) {
                    Iterator i$ = this.mContextRelatedInstanceList.iterator();
                    while (i$.hasNext()) {
                        ContextRelatedInstance relatedInstance = (ContextRelatedInstance) i$.next();
                        if ((relatedInstance.getObject() instanceof Context) && relationContext.equals((Context) relatedInstance.getObject())) {
                            relatedInstance.updateRelatedInfo(Integer.valueOf(displayId));
                        }
                    }
                }
            }
        }
        garbageCollect();
    }

    ContextRelatedInstance findContextRelatedInstance(Context context) {
        if (context == null) {
            return null;
        }
        Iterator i$ = this.mContextRelatedInstanceList.iterator();
        while (i$.hasNext()) {
            ContextRelatedInstance relatedInstance = (ContextRelatedInstance) i$.next();
            if (relatedInstance.getObject() instanceof Context) {
                Context tmpContext = (Context) relatedInstance.getObject();
                if (tmpContext != null && tmpContext.equals(context)) {
                    return relatedInstance;
                }
            }
        }
        return null;
    }

    public boolean removeContext(Context context) {
        ContextRelatedInstance relation = findContextRelatedInstance(context);
        if (relation != null) {
            relation.release();
            if (this.mContextRelatedInstanceList.remove(relation)) {
                garbageCollect();
                return true;
            }
        }
        garbageCollect();
        return false;
    }

    public void garbageCollect() {
        if (DEBUG) {
            int instanceSize = this.mContextRelatedInstanceList.size();
        }
        LinkedList<ContextRelatedInstance> removingContextRelatedInstanceList = new LinkedList();
        Iterator i$ = this.mContextRelatedInstanceList.iterator();
        while (i$.hasNext()) {
            ContextRelatedInstance relation = (ContextRelatedInstance) i$.next();
            if (relation.getObject() == null) {
                relation.release();
                removingContextRelatedInstanceList.add(relation);
            } else {
                relation.garbageCollect();
            }
        }
        this.mContextRelatedInstanceList.removeAll(removingContextRelatedInstanceList);
        removingContextRelatedInstanceList.clear();
        if (!DEBUG) {
        }
    }
}
