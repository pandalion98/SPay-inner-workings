package com.samsung.android.multidisplay.common.datastructure;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.util.Slog;
import android.view.ContextThemeWrapper;
import com.samsung.android.multidisplay.common.ContextRelationManager;
import com.samsung.android.multidisplay.common.datastructure.graph.ItemWrapper;
import com.samsung.android.multidisplay.common.datastructure.graph.Vertex;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class ContextRelationGraph {
    static final boolean DEBUG = ContextRelationManager.DEBUG;
    static final boolean DUMP_DETAIL = ContextRelationManager.DUMP_DETAIL;
    static final String TAG = "ContextRelationGraph";
    private final RelationGraph mRelationGraph = new RelationGraph("ContextRelationGraph");

    public boolean createContext(Context parentContext, Context childContext) {
        if (DEBUG) {
            Slog.d("ContextRelationGraph", "createContext() : context=" + childContext);
        }
        if (childContext == null) {
            return false;
        }
        if (findContextWrapper(childContext) == null) {
            ContextWrapper childContextWrapper = new ContextWrapper(childContext);
            if (this.mRelationGraph.addRelation(findContextWrapper(parentContext), childContextWrapper)) {
                return true;
            }
            return false;
        } else if (!DEBUG) {
            return false;
        } else {
            Slog.w("ContextRelationGraph", "createContext() : same context already exist in graph!");
            return false;
        }
    }

    public boolean removeContext(Context context) {
        ContextWrapper contextWrapper = findContextWrapper(context);
        if (contextWrapper != null) {
            if (this.mRelationGraph.removeRelation(contextWrapper)) {
                return true;
            }
        } else if (DEBUG) {
            Slog.d("ContextRelationGraph", "no ContextWrapper for context=" + context);
        }
        return false;
    }

    public Context getLeafContext(Context context) {
        ContextWrapper contextWrapper = findContextWrapper(context);
        if (contextWrapper != null) {
            RelationObject leafRelation = this.mRelationGraph.getLeafRelation(contextWrapper);
            if (leafRelation != null) {
                return (Context) leafRelation.getObject();
            }
            return null;
        } else if (!DEBUG) {
            return null;
        } else {
            Slog.d("ContextRelationGraph", "no ContextWrapper for context=" + context);
            return null;
        }
    }

    public Context getRootContext(Context context) {
        ContextWrapper contextWrapper = findContextWrapper(context);
        if (contextWrapper != null) {
            RelationObject rootRelation = this.mRelationGraph.getRootRelation(contextWrapper);
            if (rootRelation != null) {
                return (Context) rootRelation.getObject();
            }
            return null;
        } else if (!DEBUG) {
            return null;
        } else {
            Slog.d("ContextRelationGraph", "no ContextWrapper for context=" + context);
            return null;
        }
    }

    public void propagateChangedDisplay(Context context, int displayId) {
        if (DEBUG) {
            Slog.d("ContextRelationGraph", "propagateChangedDisplay() : for outerContext=" + context.getOuterContext() + " display=" + displayId);
        }
        ContextWrapper contextWrapper = findContextWrapper(context);
        if (contextWrapper != null) {
            this.mRelationGraph.propagateChangedRelationInfo(contextWrapper, Integer.valueOf(displayId));
        } else if (DEBUG) {
            Slog.w("ContextRelationGraph", "no ContextWrapper for context=" + context);
        }
    }

    public ArrayList<Context> getDescendantVertices(Context context) {
        ContextWrapper contextWrapper = findContextWrapper(context);
        if (contextWrapper == null) {
            if (DEBUG) {
                Slog.w("ContextRelationGraph", "no ContextWrapper for context=" + context);
            }
            return null;
        }
        ArrayList<RelationObject> descendantRelationList = this.mRelationGraph.getDescendantRelation(contextWrapper);
        ArrayList<Context> descendantContextList = new ArrayList();
        Iterator i$ = descendantRelationList.iterator();
        while (i$.hasNext()) {
            RelationObject r = (RelationObject) i$.next();
            if (r != null) {
                descendantContextList.add((Context) r.getObject());
            }
        }
        return descendantContextList;
    }

    public boolean detachContext(Context context) {
        Slog.d("ContextRelationGraph", "detachContext() : context=" + context);
        ContextWrapper contextWrapper = findContextWrapper(context);
        if (contextWrapper == null) {
            if (DEBUG) {
                Slog.w("ContextRelationGraph", "no ContextWrapper for context=" + context);
            }
            return false;
        }
        this.mRelationGraph.detachRelation(contextWrapper);
        return true;
    }

    public boolean attachContext(Context candidateParentContext, Context childContext) {
        Slog.d("ContextRelationGraph", "attachContext() : parentContext=" + candidateParentContext + " childContext=" + childContext);
        ContextWrapper candidateParentContextWrapper = findContextWrapper(candidateParentContext);
        ContextWrapper childContextWrapper = findContextWrapper(childContext);
        if (candidateParentContextWrapper == null || childContextWrapper == null) {
            if (DEBUG) {
                Slog.w("ContextRelationGraph", "no ContextWrapper for context=" + candidateParentContext + " OR context=" + childContext);
            }
            return false;
        }
        this.mRelationGraph.attachRelation(candidateParentContextWrapper, childContextWrapper);
        return true;
    }

    public String toString() {
        return "Graph \n " + this.mRelationGraph;
    }

    public RelationGraph getRelationGraph() {
        return this.mRelationGraph;
    }

    ContextWrapper findContextWrapper(Context context) {
        if (context == null) {
            return null;
        }
        Iterator i$ = this.mRelationGraph.getGraph().vertices().iterator();
        while (i$.hasNext()) {
            ItemWrapper<?> itemWrapper = ((Vertex) i$.next()).getElement();
            ContextWrapper item = itemWrapper.getWrappedItem();
            if (item instanceof ContextWrapper) {
                ContextWrapper tmpContextWrapper = item;
                if (((Context) tmpContextWrapper.itemRef.get()) == context && !itemWrapper.isDummyWrappedItem()) {
                    return tmpContextWrapper;
                }
            }
        }
        return null;
    }

    public String toString(Context context) {
        String ret = "";
        if (context == null) {
            return "null";
        }
        ContextWrapper contextWrapper = findContextWrapper(context);
        if (contextWrapper != null) {
            ret = contextWrapper.toString();
        } else {
            StringBuilder sb = new StringBuilder(128);
            sb.append("{");
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
            sb.append("}");
            ret = sb.toString();
        }
        return ret;
    }

    public void dump(PrintWriter pw, String prefix, boolean dumpDetail, boolean dumpCallStack) {
        String innerPrefix = prefix + "  ";
        pw.print(prefix);
        pw.println("Context relation policy : ");
        pw.print(innerPrefix);
        pw.print("mGraphName=");
        pw.println(this.mRelationGraph.getGraph().getGraphName());
        pw.print(innerPrefix);
        pw.print("Vertex size=");
        pw.println(this.mRelationGraph.getGraph().numVertices());
        pw.print(innerPrefix);
        pw.print("Edge size=");
        pw.println(this.mRelationGraph.getGraph().numEdges());
        pw.print(innerPrefix);
        pw.print("rootVertices= {");
        Iterator i$ = this.mRelationGraph.getGraph().getRootVertices().iterator();
        while (i$.hasNext()) {
            pw.print(" v" + ((Vertex) i$.next()).id);
        }
        pw.println(" }");
        if (dumpDetail) {
            pw.print(innerPrefix);
            pw.println("DFS Traversed Vertices= {");
            i$ = this.mRelationGraph.getGraph().doDfsForAllVertices().iterator();
            while (i$.hasNext()) {
                Vertex<RelationObject> v = (Vertex) i$.next();
                pw.print(innerPrefix + "  ");
                pw.println(v);
                if (DUMP_DETAIL && dumpCallStack) {
                    RelationObject ro = (RelationObject) v.getElement().getWrappedItem();
                    if (ro instanceof ContextWrapper) {
                        pw.println(((ContextWrapper) ro).getCallStack());
                    }
                }
            }
            pw.print(innerPrefix);
            pw.println("}");
        }
    }

    public boolean isRootContext(Context context) {
        if (context == null || getRootContext(context) != context) {
            return false;
        }
        return true;
    }
}
