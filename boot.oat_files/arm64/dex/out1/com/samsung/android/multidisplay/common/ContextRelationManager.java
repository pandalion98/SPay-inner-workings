package com.samsung.android.multidisplay.common;

import android.app.ActivityThread;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.util.Singleton;
import android.util.Slog;
import android.view.Display;
import android.view.WindowManagerImpl;
import com.samsung.android.dualscreen.DualScreenManager;
import com.samsung.android.multidisplay.common.datastructure.ContextRelatedInstanceHandler;
import com.samsung.android.multidisplay.common.datastructure.ContextRelationGraph;
import com.samsung.android.multidisplay.common.datastructure.DisplayInstanceHandler;
import com.samsung.android.multidisplay.common.datastructure.ResourcesInstanceHandler;
import com.samsung.android.multidisplay.common.datastructure.WindowManagerImplInstanceHandler;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class ContextRelationManager {
    static final String BOTTOM_OF_CALL_STACK_MESSAGE = "<bottom of call stack>";
    public static final boolean DEBUG = false;
    public static final boolean DEBUG_VERBOSE;
    static final String DISPATCH_MESSAGE_METHOD = "android.os.Handler.dispatchMessage";
    public static final boolean DUMP_DETAIL = (DEBUG);
    public static boolean FEATURE_ENABLED = true;
    public static final String TAG = "ContextRelationManager";
    public static final SimpleDateFormat TIME_STAMP_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    private static ContextRelationGraph mContextRelationGraph;
    private static final Singleton<ContextRelationManager> sInstance = new Singleton<ContextRelationManager>() {
        protected ContextRelationManager create() {
            return new ContextRelationManager();
        }
    };
    private final HashMap<String, ContextRelatedInstanceHandler> mContextRelatedInstanceHandlers;

    static {
        boolean z = false;
        if (DualScreenManager.DEBUG) {
        }
        if (DEBUG) {
            z = true;
        }
        DEBUG_VERBOSE = z;
    }

    public static ContextRelationManager getInstance() {
        return (ContextRelationManager) sInstance.get();
    }

    private ContextRelationManager() {
        this.mContextRelatedInstanceHandlers = new HashMap();
        mContextRelationGraph = new ContextRelationGraph();
        this.mContextRelatedInstanceHandlers.put(WindowManagerImpl.class.getName(), new WindowManagerImplInstanceHandler());
        this.mContextRelatedInstanceHandlers.put(Display.class.getName(), new DisplayInstanceHandler());
        this.mContextRelatedInstanceHandlers.put(Resources.class.getName(), new ResourcesInstanceHandler());
        ActivityThread at = ActivityThread.currentActivityThread();
        if (at != null && at.isSystemThread()) {
            FEATURE_ENABLED = false;
        }
        Slog.d(TAG, "ContextRelationManager() : FEATURE_ENABLED=" + FEATURE_ENABLED);
    }

    public void createContext(Context creator, Context context) {
        if (!FEATURE_ENABLED) {
            return;
        }
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        Context parentContext = null;
        int creatorDisplayId = creator != null ? creator.getDisplayId() : -1;
        int contextDisplayId = context.getDisplayId();
        if (creatorDisplayId >= 0 && creatorDisplayId == contextDisplayId) {
            parentContext = creator;
        }
        Context parentImpl = getImpl(parentContext);
        Context contextImpl = getImpl(context);
        synchronized (mContextRelationGraph) {
            mContextRelationGraph.createContext(parentImpl, contextImpl);
        }
    }

    public void removeContext(Context context) {
        if (!FEATURE_ENABLED) {
            return;
        }
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        Context contextImpl = getImpl(context);
        if (contextImpl != null) {
            synchronized (mContextRelationGraph) {
                mContextRelationGraph.removeContext(contextImpl);
            }
        }
    }

    public void createWindowManager(Context creator, WindowManagerImpl wm) {
        if (!FEATURE_ENABLED) {
            return;
        }
        if (creator == null) {
            throw new NullPointerException("creator is null");
        } else if (wm == null) {
            throw new NullPointerException("wm is null");
        } else {
            Context creatorImpl = getImpl(creator);
            if (creatorImpl != null) {
                ContextRelatedInstanceHandler instanceHandler = (ContextRelatedInstanceHandler) this.mContextRelatedInstanceHandlers.get(WindowManagerImpl.class.getName());
                if (instanceHandler != null) {
                    synchronized (instanceHandler) {
                        instanceHandler.createContextRelatedInstance(creatorImpl, wm);
                    }
                }
            }
        }
    }

    public void createDisplay(Context creator, Display display) {
        if (!FEATURE_ENABLED) {
            return;
        }
        if (creator == null) {
            throw new NullPointerException("creator is null");
        } else if (display == null) {
            throw new NullPointerException("display is null");
        } else {
            Context creatorImpl = getImpl(creator);
            if (creatorImpl != null) {
                ContextRelatedInstanceHandler instanceHandler = (ContextRelatedInstanceHandler) this.mContextRelatedInstanceHandlers.get(Display.class.getName());
                if (instanceHandler != null) {
                    synchronized (instanceHandler) {
                        instanceHandler.createContextRelatedInstance(creatorImpl, display);
                    }
                }
            }
        }
    }

    public void createResources(Context creator, Resources res) {
        if (!FEATURE_ENABLED) {
            return;
        }
        if (creator == null) {
            throw new NullPointerException("creator is null");
        } else if (res == null) {
            throw new NullPointerException("res is null");
        } else {
            Context creatorImpl = getImpl(creator);
            if (creatorImpl != null) {
                ContextRelatedInstanceHandler instanceHandler = (ContextRelatedInstanceHandler) this.mContextRelatedInstanceHandlers.get(Resources.class.getName());
                if (instanceHandler != null) {
                    synchronized (instanceHandler) {
                        instanceHandler.createContextRelatedInstance(creatorImpl, res);
                    }
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void propagateChangedContextDisplay(android.content.Context r8, int r9) {
        /*
        r7 = this;
        r3 = FEATURE_ENABLED;
        if (r3 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        if (r8 == 0) goto L_0x0004;
    L_0x0007:
        r4 = mContextRelationGraph;
        monitor-enter(r4);
        r3 = DEBUG_VERBOSE;	 Catch:{ all -> 0x005a }
        if (r3 == 0) goto L_0x0046;
    L_0x000e:
        r3 = "ContextRelationManager";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x005a }
        r5.<init>();	 Catch:{ all -> 0x005a }
        r6 = "propagate new display to ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x005a }
        r6 = mContextRelationGraph;	 Catch:{ all -> 0x005a }
        r6 = r6.toString(r8);	 Catch:{ all -> 0x005a }
        r5 = r5.append(r6);	 Catch:{ all -> 0x005a }
        r6 = ". new displayId=";
        r5 = r5.append(r6);	 Catch:{ all -> 0x005a }
        r5 = r5.append(r9);	 Catch:{ all -> 0x005a }
        r6 = " callers=";
        r5 = r5.append(r6);	 Catch:{ all -> 0x005a }
        r6 = 2;
        r6 = android.os.Debug.getCallers(r6);	 Catch:{ all -> 0x005a }
        r5 = r5.append(r6);	 Catch:{ all -> 0x005a }
        r5 = r5.toString();	 Catch:{ all -> 0x005a }
        android.util.Slog.d(r3, r5);	 Catch:{ all -> 0x005a }
    L_0x0046:
        r3 = r8.getDisplayId();	 Catch:{ all -> 0x005a }
        if (r3 != r9) goto L_0x005d;
    L_0x004c:
        r3 = DEBUG;	 Catch:{ all -> 0x005a }
        if (r3 == 0) goto L_0x0058;
    L_0x0050:
        r3 = "ContextRelationManager";
        r5 = "stop propagating display to a Context has same displayId";
        android.util.Slog.d(r3, r5);	 Catch:{ all -> 0x005a }
    L_0x0058:
        monitor-exit(r4);	 Catch:{ all -> 0x005a }
        goto L_0x0004;
    L_0x005a:
        r3 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x005a }
        throw r3;
    L_0x005d:
        r3 = mContextRelationGraph;	 Catch:{ all -> 0x005a }
        r3.propagateChangedDisplay(r8, r9);	 Catch:{ all -> 0x005a }
        r3 = mContextRelationGraph;	 Catch:{ all -> 0x005a }
        r0 = r3.getDescendantVertices(r8);	 Catch:{ all -> 0x005a }
        r5 = r7.mContextRelatedInstanceHandlers;	 Catch:{ all -> 0x005a }
        monitor-enter(r5);	 Catch:{ all -> 0x005a }
        r3 = r7.mContextRelatedInstanceHandlers;	 Catch:{ all -> 0x0092 }
        r3 = r3.keySet();	 Catch:{ all -> 0x0092 }
        r2 = r3.iterator();	 Catch:{ all -> 0x0092 }
    L_0x0075:
        r3 = r2.hasNext();	 Catch:{ all -> 0x0092 }
        if (r3 == 0) goto L_0x0095;
    L_0x007b:
        r3 = r7.mContextRelatedInstanceHandlers;	 Catch:{ all -> 0x0092 }
        r6 = r2.next();	 Catch:{ all -> 0x0092 }
        r1 = r3.get(r6);	 Catch:{ all -> 0x0092 }
        r1 = (com.samsung.android.multidisplay.common.datastructure.ContextRelatedInstanceHandler) r1;	 Catch:{ all -> 0x0092 }
        if (r1 == 0) goto L_0x0075;
    L_0x0089:
        monitor-enter(r1);	 Catch:{ all -> 0x0092 }
        r1.propagateChangedRelationInfo(r0, r9);	 Catch:{ all -> 0x008f }
        monitor-exit(r1);	 Catch:{ all -> 0x008f }
        goto L_0x0075;
    L_0x008f:
        r3 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x008f }
        throw r3;	 Catch:{ all -> 0x0092 }
    L_0x0092:
        r3 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0092 }
        throw r3;	 Catch:{ all -> 0x005a }
    L_0x0095:
        monitor-exit(r5);	 Catch:{ all -> 0x0092 }
        monitor-exit(r4);	 Catch:{ all -> 0x005a }
        goto L_0x0004;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.multidisplay.common.ContextRelationManager.propagateChangedContextDisplay(android.content.Context, int):void");
    }

    public int getContextRefSize() {
        int numVertices;
        synchronized (mContextRelationGraph) {
            numVertices = mContextRelationGraph.getRelationGraph().getGraph().numVertices();
        }
        return numVertices;
    }

    public void dump(PrintWriter pw, String prefix, boolean dumpDetail, boolean dumpCallStack) {
        pw.print(prefix);
        pw.println(ContextRelationManager.class.getSimpleName() + " : ");
        String innerPrefix = prefix + "  ";
        pw.print(innerPrefix);
        pw.print("Context Ref. size=");
        pw.println(getContextRefSize());
        synchronized (mContextRelationGraph) {
            mContextRelationGraph.dump(pw, innerPrefix, dumpDetail, dumpCallStack);
        }
        synchronized (this.mContextRelatedInstanceHandlers) {
            for (Object obj : this.mContextRelatedInstanceHandlers.keySet()) {
                ContextRelatedInstanceHandler instanceHandler = (ContextRelatedInstanceHandler) this.mContextRelatedInstanceHandlers.get(obj);
                if (instanceHandler != null) {
                    synchronized (instanceHandler) {
                        pw.println();
                        instanceHandler.dump(pw, innerPrefix, dumpDetail, dumpCallStack);
                    }
                }
            }
        }
    }

    public static String trimCallStack(String callStack) {
        String ret = "";
        if (callStack == null) {
            return ret;
        }
        int keywordIndex = callStack.indexOf(DISPATCH_MESSAGE_METHOD);
        if (keywordIndex < 0) {
            keywordIndex = callStack.indexOf(BOTTOM_OF_CALL_STACK_MESSAGE);
            if (keywordIndex < 0) {
                keywordIndex = callStack.length() - 1;
            }
        }
        String keywordTrimedString = callStack.substring(0, keywordIndex);
        int newlineIndex = keywordTrimedString.lastIndexOf(10);
        if (newlineIndex >= 0) {
            ret = keywordTrimedString.substring(0, newlineIndex + 1);
        } else {
            ret = keywordTrimedString.substring(0, keywordTrimedString.length() - 1);
        }
        return ret;
    }

    static Context getImpl(Context context) {
        int repeatCount = 0;
        while (context instanceof ContextWrapper) {
            Context nextContext = ((ContextWrapper) context).getBaseContext();
            if (nextContext == null) {
                return context;
            }
            repeatCount++;
            if (context == nextContext) {
                if (DEBUG) {
                    Slog.w(TAG, "cannot get ContextImpl. (context has itself as a base context)");
                }
                if (DEBUG_VERBOSE) {
                    Slog.w(TAG, "getImpl() " + context.getClass());
                    Slog.w(TAG, "getImpl() super " + context.getClass().getSuperclass());
                    Slog.w(TAG, "getImpl() nextContext : " + nextContext);
                }
                return null;
            } else if (repeatCount > 5) {
                if (DEBUG) {
                    Slog.w(TAG, "getImpl() cannot get ContextImpl. (too many repeat)");
                }
                return null;
            } else {
                context = nextContext;
            }
        }
        return context;
    }
}
