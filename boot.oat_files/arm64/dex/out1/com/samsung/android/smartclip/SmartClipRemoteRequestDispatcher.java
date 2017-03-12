package com.samsung.android.smartclip;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewRootImpl;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.VideoView;
import com.samsung.android.multiwindow.MultiWindowFacade;
import java.util.ArrayList;
import java.util.Iterator;

public class SmartClipRemoteRequestDispatcher {
    private static final String KEY_AIR_COMMAND_HIT_TEST_RESULT = "result";
    private static final String KEY_EVENT_INJECTION_EVENTS = "events";
    private static final String KEY_EVENT_INJECTION_WAIT_UNTIL_CONSUME = "waitUntilConsume";
    private static final String KEY_SCROLLABLE_AREA_INFO_ACTIVITY_NAME = "activityName";
    private static final String KEY_SCROLLABLE_AREA_INFO_PACKAGE_NAME = "packageName";
    private static final String KEY_SCROLLABLE_AREA_INFO_SCROLLABLE_VIEWS = "scrollableViews";
    private static final String KEY_SCROLLABLE_AREA_INFO_UNSCROLLABLE_VIEWS = "unscrollableViews";
    private static final String KEY_SCROLLABLE_AREA_INFO_WINDOW_LAYER = "windowLayer";
    private static final String KEY_SCROLLABLE_AREA_INFO_WINDOW_RECT = "windowRect";
    private static final String KEY_SCROLLABLE_VIEW_INFO_CHILD_VIEWS = "childViews";
    private static final String KEY_SCROLLABLE_VIEW_INFO_TARGET_VIEW = "targetView";
    private static final String KEY_VIEW_INFO_HASHCODE = "hashCode";
    private static final String KEY_VIEW_INFO_HIERARCHY = "hierarchy";
    private static final String KEY_VIEW_INFO_SCREEN_RECT = "screenRect";
    public static final String PERMISSION_EXTRACT_SMARTCLIP_DATA = "com.samsung.android.permission.EXTRACT_SMARTCLIP_DATA";
    public static final String PERMISSION_INJECT_INPUT_EVENT = "android.permission.INJECT_EVENTS";
    public static final String TAG = "SmartClipRemoteRequestDispatcher";
    private boolean DEBUG = false;
    private Context mContext;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ViewRootImplGateway mViewRootImplGateway;

    public interface ViewRootImplGateway {
        void enqueueInputEvent(InputEvent inputEvent, InputEventReceiver inputEventReceiver, int i, boolean z);

        View getRootView();

        PointF getScaleFactor();

        ViewRootImpl getViewRootImpl();
    }

    public SmartClipRemoteRequestDispatcher(Context context, ViewRootImplGateway viewRootImplGateway) {
        this.mContext = context;
        this.mViewRootImplGateway = viewRootImplGateway;
        PackageManager pm = context.getPackageManager();
        if (pm != null && pm.hasSystemFeature("com.samsung.android.smartclip.DEBUG")) {
            this.DEBUG = true;
        }
    }

    public boolean isDebugMode() {
        return this.DEBUG;
    }

    public void checkPermission(String permission, int pid, int uid) {
        if (!(this.mContext.checkPermission(permission, pid, uid) == 0)) {
            String errStr = "Requires " + permission + " permission";
            Log.e(TAG, "checkPermission : " + errStr);
            throw new SecurityException(errStr);
        }
    }

    public void dispatchSmartClipRemoteRequest(final SmartClipRemoteRequestInfo request) {
        switch (request.mRequestType) {
            case 2:
                checkPermission(PERMISSION_EXTRACT_SMARTCLIP_DATA, request.mCallerPid, request.mCallerUid);
                this.mHandler.post(new Runnable() {
                    public void run() {
                        SmartClipRemoteRequestDispatcher.this.dispatchAirCommandHitTest(request);
                    }
                });
                return;
            case 3:
                checkPermission(PERMISSION_INJECT_INPUT_EVENT, request.mCallerPid, request.mCallerUid);
                this.mHandler.post(new Runnable() {
                    public void run() {
                        SmartClipRemoteRequestDispatcher.this.dispatchInputEventInjection(request);
                    }
                });
                return;
            case 4:
                checkPermission(PERMISSION_EXTRACT_SMARTCLIP_DATA, request.mCallerPid, request.mCallerUid);
                this.mHandler.post(new Runnable() {
                    public void run() {
                        SmartClipRemoteRequestDispatcher.this.dispatchScrollableAreaInfo(request);
                    }
                });
                return;
            case 5:
                checkPermission(PERMISSION_EXTRACT_SMARTCLIP_DATA, request.mCallerPid, request.mCallerUid);
                this.mHandler.post(new Runnable() {
                    public void run() {
                        SmartClipRemoteRequestDispatcher.this.dispatchScrollableViewInfo(request);
                    }
                });
                return;
            default:
                Log.e(TAG, "dispatchSmartClipRemoteRequest : Unknown request type(" + request.mRequestType + ")");
                return;
        }
    }

    private void dispatchInputEventInjection(SmartClipRemoteRequestInfo request) {
        if (request.mRequestData == null) {
            Log.e(TAG, "dispatchInputEventInjection : Empty input event!");
        } else if (request.mRequestData instanceof MotionEvent) {
            MotionEvent motionEvent = request.mRequestData;
            transformTouchPosition(motionEvent);
            int action = motionEvent.getAction();
            if (this.DEBUG || action == 0 || action == 1 || action == 3) {
                Log.d(TAG, "dispatchInputEventInjection : Touch event action=" + MotionEvent.actionToString(action) + " x=" + ((int) motionEvent.getRawX()) + " y=" + ((int) motionEvent.getRawY()));
            }
            enqueueInputEvent(request.mRequestData, true);
        } else if (request.mRequestData instanceof Bundle) {
            Bundle reqData = (Bundle) request.mRequestData;
            final Parcelable[] events = reqData.getParcelableArray(KEY_EVENT_INJECTION_EVENTS);
            if (events != null) {
                final boolean waitUntilConsume = reqData.getBoolean(KEY_EVENT_INJECTION_WAIT_UNTIL_CONSUME);
                long firstEventTime = events.length > 0 ? ((InputEvent) events[0]).getEventTime() : -1;
                if (this.DEBUG) {
                    Log.d(TAG, "dispatchInputEventInjection : wait = " + waitUntilConsume + "  eventCount=" + events.length);
                }
                for (final InputEvent event : events) {
                    if (event != null) {
                        if (event instanceof MotionEvent) {
                            transformTouchPosition((MotionEvent) event);
                        }
                        final SmartClipRemoteRequestInfo smartClipRemoteRequestInfo = request;
                        Runnable runnable = new Runnable() {
                            public void run() {
                                long startTime = System.currentTimeMillis();
                                if (SmartClipRemoteRequestDispatcher.this.DEBUG) {
                                    Log.d(SmartClipRemoteRequestDispatcher.TAG, "dispatchInputEventInjection : injecting.. " + event);
                                }
                                SmartClipRemoteRequestDispatcher.this.enqueueInputEvent(event, true);
                                if (event == events[events.length - 1]) {
                                    if (waitUntilConsume) {
                                        SmartClipRemoteRequestDispatcher.this.sendResult(smartClipRemoteRequestInfo, null);
                                    }
                                    Log.d(SmartClipRemoteRequestDispatcher.TAG, "dispatchInputEventInjection : injection finished. Elapsed = " + (System.currentTimeMillis() - startTime));
                                }
                            }
                        };
                        long delay = event.getEventTime() - firstEventTime;
                        if (delay > 0) {
                            this.mHandler.postDelayed(runnable, delay);
                        } else {
                            runnable.run();
                        }
                    }
                }
                return;
            }
            Log.e(TAG, "dispatchInputEventInjection : Event is null!");
        }
    }

    private void dispatchScrollableAreaInfo(SmartClipRemoteRequestInfo request) {
        View rootView = this.mViewRootImplGateway.getRootView();
        if (rootView != null) {
            ArrayList<View> scrollableViews = new ArrayList();
            ArrayList<View> unscrollableViews = new ArrayList();
            Rect windowRect = SmartClipMetaUtils.getScreenRectOfView(rootView);
            Log.d(TAG, "dispatchScrollableAreaInfo : windowRect = " + windowRect);
            findScrollableViews(rootView, windowRect, scrollableViews, unscrollableViews);
            Bundle resultData = new Bundle();
            ArrayList<Bundle> scrollableViewInfo = new ArrayList();
            Iterator i$ = scrollableViews.iterator();
            while (i$.hasNext()) {
                scrollableViewInfo.add(createViewInfoAsBundle((View) i$.next()));
            }
            Log.d(TAG, "dispatchScrollableAreaInfo : Scrollable view count = " + scrollableViewInfo.size());
            resultData.putParcelableArrayList(KEY_SCROLLABLE_AREA_INFO_SCROLLABLE_VIEWS, scrollableViewInfo);
            ArrayList<Bundle> unscrollableViewInfo = new ArrayList();
            i$ = unscrollableViews.iterator();
            while (i$.hasNext()) {
                unscrollableViewInfo.add(createViewInfoAsBundle((View) i$.next()));
            }
            Log.d(TAG, "dispatchScrollableAreaInfo : Unscrollable view count = " + unscrollableViewInfo.size());
            resultData.putParcelableArrayList(KEY_SCROLLABLE_AREA_INFO_UNSCROLLABLE_VIEWS, unscrollableViewInfo);
            resultData.putParcelable(KEY_SCROLLABLE_AREA_INFO_WINDOW_RECT, windowRect);
            resultData.putInt(KEY_SCROLLABLE_AREA_INFO_WINDOW_LAYER, request.mTargetWindowLayer);
            String pkgName = this.mContext.getPackageName();
            String activityName = null;
            resultData.putString("packageName", pkgName);
            if (this.mContext instanceof Activity) {
                activityName = this.mContext.getClass().getName();
                resultData.putString(KEY_SCROLLABLE_AREA_INFO_ACTIVITY_NAME, activityName);
            }
            Log.d(TAG, "dispatchScrollableAreaInfo : Pkg=" + pkgName + " Activity=" + activityName);
            sendResult(request, resultData);
            return;
        }
        Log.e(TAG, "dispatchScrollableAreaInfo : Root view is null!");
    }

    private void dispatchScrollableViewInfo(SmartClipRemoteRequestInfo request) {
        View rootView = this.mViewRootImplGateway.getRootView();
        if (rootView != null) {
            int viewHash = ((Bundle) request.mRequestData).getInt(KEY_VIEW_INFO_HASHCODE, -1);
            if (viewHash != -1) {
                View view = findViewByHashCode(rootView, viewHash);
                Bundle resultData = new Bundle();
                if (view != null) {
                    resultData.putParcelable(KEY_SCROLLABLE_AREA_INFO_WINDOW_RECT, SmartClipMetaUtils.getScreenRectOfView(rootView));
                    resultData.putParcelable(KEY_SCROLLABLE_VIEW_INFO_TARGET_VIEW, createViewInfoAsBundle(view));
                    ArrayList<Bundle> childInfoArray = new ArrayList();
                    if (view instanceof ViewGroup) {
                        ViewGroup vg = (ViewGroup) view;
                        int childCount = vg.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            childInfoArray.add(createViewInfoAsBundle(vg.getChildAt(i)));
                        }
                    }
                    resultData.putParcelableArrayList(KEY_SCROLLABLE_VIEW_INFO_CHILD_VIEWS, childInfoArray);
                    Log.d(TAG, "dispatchScrollableViewInfo : " + view + "ChildCnt=" + childInfoArray.size());
                } else {
                    Log.e(TAG, "dispatchScrollableViewInfo : Could not found the view! hash=" + viewHash);
                }
                sendResult(request, resultData);
                return;
            }
            Log.e(TAG, "dispatchScrollableViewInfo : There is no hash value in request!");
        }
    }

    private void dispatchAirCommandHitTest(SmartClipRemoteRequestInfo request) {
        PointF mScaleFactor = this.mViewRootImplGateway.getScaleFactor();
        View mView = this.mViewRootImplGateway.getRootView();
        int result = -1;
        Bundle bundle = request.mRequestData;
        int id = bundle.getInt("id", -1);
        int x = bundle.getInt("x", -999999);
        int y = bundle.getInt("y", -999999);
        if (mView != null) {
            if (!(this.mContext == null || (mScaleFactor.x == 1.0f && mScaleFactor.y == 1.0f))) {
                Point pos = ((MultiWindowFacade) this.mContext.getSystemService("multiwindow_facade")).getStackPosition(this.mContext.getBaseActivityToken());
                if (pos != null) {
                    x = (int) ((((float) (x - pos.x)) * (1.0f / mScaleFactor.x)) + 0.5f);
                    y = (int) ((((float) (y - pos.y)) * (1.0f / mScaleFactor.y)) + 0.5f);
                }
            }
            View targetView = findTopmostViewByPosition(mView, x, y);
            if (targetView == null || targetView.getAirButton() == null) {
                result = 0;
            } else {
                result = 1;
            }
        }
        if (this.mContext != null) {
            Bundle resultData = new Bundle();
            resultData.putInt(KEY_AIR_COMMAND_HIT_TEST_RESULT, result);
            sendResult(request, resultData);
        }
    }

    private Bundle createViewInfoAsBundle(View view) {
        Bundle bundle = new Bundle();
        int hashCode = view.hashCode();
        Rect screenRectOfView = SmartClipMetaUtils.getScreenRectOfView(view);
        ArrayList<String> viewHierarchy = getViewHierarchyTable(view);
        bundle.putInt(KEY_VIEW_INFO_HASHCODE, hashCode);
        bundle.putParcelable(KEY_VIEW_INFO_SCREEN_RECT, screenRectOfView);
        bundle.putStringArrayList(KEY_VIEW_INFO_HIERARCHY, viewHierarchy);
        if (this.DEBUG) {
            Log.d(TAG, "createScrollableViewInfo : Scrollable view hash=@" + Integer.toHexString(hashCode).toUpperCase() + " / Rect=" + screenRectOfView);
            Iterator i$ = viewHierarchy.iterator();
            while (i$.hasNext()) {
                Log.d(TAG, "createScrollableViewInfo :   + " + ((String) i$.next()));
            }
        }
        return bundle;
    }

    private ArrayList<String> getViewHierarchyTable(View view) {
        ArrayList<String> hierarchy = new ArrayList();
        for (Class<?> cls = view.getClass(); cls != null; cls = cls.getSuperclass()) {
            String clsName = cls.getName();
            hierarchy.add(clsName);
            if ("android.view.View".equals(clsName)) {
                break;
            }
        }
        return hierarchy;
    }

    private View findTopmostViewByPosition(View view, int x, int y) {
        if (view == null || view.getVisibility() != 0 || view.getWidth() == 0 || view.getHeight() == 0) {
            return null;
        }
        if (!SmartClipMetaUtils.getScreenRectOfView(view).contains(x, y)) {
            return null;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
                View topMostView = findTopmostViewByPosition(viewGroup.getChildAt(i), x, y);
                if (topMostView != null) {
                    return topMostView;
                }
            }
        }
        if (view.getAirButton() != null) {
            return view;
        }
        return null;
    }

    private void findScrollableViews(View view, Rect windowRect, ArrayList<View> scrollableViews, ArrayList<View> unscrollableViews) {
        if (view != null && view.getVisibility() == 0 && view.getWidth() != 0 && view.getHeight() != 0) {
            String viewClassName = view.getClass().getName();
            String parentClassName = view.getClass().getSuperclass().getName();
            Rect screenRectOfView = SmartClipMetaUtils.getScreenRectOfView(view);
            if (Rect.intersects(windowRect, screenRectOfView)) {
                String hashCode = Integer.toHexString(view.hashCode()).toUpperCase();
                if ((view instanceof ScrollView) || (view instanceof AbsListView) || (view instanceof WebView)) {
                    if (this.DEBUG) {
                        Log.d(TAG, "findScrollableViews : Scrollable view = @" + hashCode + " " + viewClassName + "(" + parentClassName + ") / Rect=" + screenRectOfView + " H=" + screenRectOfView.height() + " Rect=" + screenRectOfView);
                    }
                    scrollableViews.add(view);
                    return;
                }
                if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
                        findScrollableViews(viewGroup.getChildAt(i), windowRect, scrollableViews, unscrollableViews);
                    }
                }
                if ((view instanceof VideoView) || (view instanceof HorizontalScrollView)) {
                    if (this.DEBUG) {
                        Log.d(TAG, "findScrollableViews : Unscrollable view = @" + hashCode + " " + viewClassName + "(" + parentClassName + ") / Rect=" + screenRectOfView + " H=" + screenRectOfView.height() + " Rect=" + screenRectOfView);
                    }
                    unscrollableViews.add(view);
                    return;
                }
                boolean haveCustomTouchEventHandler = false;
                boolean haveCustomDrawHandler = false;
                Class<?> cls = view.getClass();
                Class<?>[] paramEvent = new Class[]{MotionEvent.class};
                Class<?>[] paramCanvas = new Class[]{Canvas.class};
                while (cls != null) {
                    String clsName = cls.getName();
                    if (!clsName.startsWith("android.view.") && !clsName.startsWith("android.widget.") && !clsName.startsWith("com.android.internal.")) {
                        if (isMethodDeclared(cls, "dispatchTouchEvent", paramEvent)) {
                            haveCustomTouchEventHandler = true;
                            if (this.DEBUG) {
                                Log.d(TAG, "findScrollableViews : @" + hashCode + " Have dispatchTouchEvent() " + viewClassName + " / " + cls.getName() + " / Rect=" + screenRectOfView);
                            }
                        }
                        if (isMethodDeclared(cls, "onTouchEvent", paramEvent)) {
                            haveCustomTouchEventHandler = true;
                            if (this.DEBUG) {
                                Log.d(TAG, "findScrollableViews : @" + hashCode + " Have onTouchEvent() " + viewClassName + " / " + cls.getName() + " / Rect=" + screenRectOfView);
                            }
                        }
                        if (isMethodDeclared(cls, "onDraw", paramCanvas)) {
                            haveCustomDrawHandler = true;
                            if (this.DEBUG) {
                                Log.d(TAG, "findScrollableViews : @" + hashCode + " Have onDraw() " + viewClassName + " / " + cls.getName() + " / Rect=" + screenRectOfView);
                            }
                        }
                        if (isMethodDeclared(cls, "draw", paramCanvas)) {
                            haveCustomDrawHandler = true;
                            if (this.DEBUG) {
                                Log.d(TAG, "findScrollableViews : @" + hashCode + " Have draw() " + viewClassName + " / " + cls.getName() + " / Rect=" + screenRectOfView);
                            }
                        }
                        if (isMethodDeclared(cls, "dispatchDraw", paramCanvas)) {
                            haveCustomDrawHandler = true;
                            if (this.DEBUG) {
                                Log.d(TAG, "findScrollableViews : @" + hashCode + " Have dispatchDraw() " + viewClassName + " / " + cls.getName() + " / Rect=" + screenRectOfView);
                            }
                        }
                        if (haveCustomTouchEventHandler && haveCustomDrawHandler) {
                            break;
                        }
                        cls = cls.getSuperclass();
                    } else {
                        break;
                    }
                }
                if (haveCustomTouchEventHandler) {
                    scrollableViews.add(view);
                }
            } else if (this.DEBUG) {
                Log.d(TAG, "findScrollableViews : Not in range - " + viewClassName + "(" + parentClassName + ") / Rect=" + screenRectOfView);
            }
        }
    }

    private boolean isMethodDeclared(Class<?> cls, String methodName, Class<?>[] paramTypes) {
        try {
            if (cls.getDeclaredMethod(methodName, paramTypes) != null) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    private View findViewByHashCode(View view, int viewHash) {
        if (view == null) {
            return null;
        }
        if (view.hashCode() == viewHash) {
            return view;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
                View foundView = findViewByHashCode(viewGroup.getChildAt(i), viewHash);
                if (foundView != null) {
                    return foundView;
                }
            }
        }
        return null;
    }

    private void enqueueInputEvent(InputEvent inputEvent, boolean processImmediately) {
        if (this.mViewRootImplGateway == null) {
            Log.e(TAG, "enqueueInputEvent : Gateway is null!");
        } else {
            this.mViewRootImplGateway.enqueueInputEvent(inputEvent, null, 0, processImmediately);
        }
    }

    private void sendResult(SmartClipRemoteRequestInfo request, Parcelable resultData) {
        ((SpenGestureManager) this.mContext.getSystemService("spengestureservice")).sendSmartClipRemoteRequestResult(new SmartClipRemoteRequestResult(request.mRequestId, request.mRequestType, resultData));
    }

    private void transformTouchPosition(MotionEvent event) {
        View rootView = this.mViewRootImplGateway.getRootView();
        if (rootView == null) {
            Log.e(TAG, "transformTouchPosition : Root view is not exists");
            return;
        }
        Rect windowRect = SmartClipMetaUtils.getScreenRectOfView(rootView);
        int windowX = windowRect.left;
        int windowY = windowRect.top;
        float dssScale = this.mContext.getApplicationInfo().dssScale;
        if (windowX != 0 || windowY != 0 || dssScale != 1.0f) {
            float x = (event.getRawX() - ((float) windowX)) * dssScale;
            float y = (event.getRawY() - ((float) windowY)) * dssScale;
            event.setLocation(x, y);
            if (this.DEBUG) {
                Log.d(TAG, "transformMotionEvent : Window offsetX=" + windowX + " offsetY=" + windowY + " dssScale=" + dssScale + " destX=" + x + " destY=" + y);
            }
        }
    }
}
