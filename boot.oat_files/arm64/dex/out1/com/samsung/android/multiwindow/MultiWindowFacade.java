package com.samsung.android.multiwindow;

import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.sec.multiwindow.impl.IMultiWindowStyleChangedCallback;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DisplayInfo;
import java.util.List;

public final class MultiWindowFacade {
    public static final int ARRANGE_SPLITED = 1;
    public static final int ARRANGE_SPLITED3L = 2;
    public static final int ARRANGE_SPLITED3R = 4;
    public static final int ARRANGE_SPLITED4 = 8;
    public static final int ARRANGE_UNKNOWN = 0;
    public static final int GET_TASK_ALLSTATE = 0;
    public static final int GET_TASK_CURRENT_USER_ONLY = 2;
    public static final int GET_TASK_EXCLUDE_MOVETASKTOBACK = 4;
    public static final int GET_TASK_RESUMED_ONLY = 1;
    public static final int GET_TASK_WITH_CURRENT_PROFILE = 16;
    public static final int GET_TOP_ACTIVITY_MULTIWINDOW_STYLE = 8;
    public static final int MULTIWINDOW_ABSOLUTE_TOPACTIVITY = 2;
    public static final int MULTIWINDOW_EXCLUDED_CASCADE_TYPE = 1;
    public static final float SPLIT_MAX_WEIGHT = 0.8f;
    public static final float SPLIT_MIN_WEIGHT = 0.2f;
    public static int TASK_CONTROLLER_TYPE_RECENT = 1;
    public static int TASK_CONTROLLER_TYPE_RUNNING = 0;
    private IMultiWindowFacade mService = null;

    public static class TaskInfo {
        public ComponentName baseActivity;
        public Intent baseIntent;
        public int id;
        public ComponentName topActivity;
    }

    public MultiWindowFacade(IMultiWindowFacade service) {
        this.mService = service;
    }

    public void minimizeWindow(IBinder activityToken) {
        try {
            this.mService.minimizeWindow(activityToken);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setCenterBarPoint(int displayId, Point point) {
        try {
            this.mService.setCenterBarPoint(displayId, point);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Point getCenterBarPoint(int displayId) {
        try {
            return this.mService.getCenterBarPoint(displayId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setMultiWindowStyleWithLogging(IBinder activityToken, MultiWindowStyle style, int loggingReason) {
        try {
            if (this.mService != null) {
                this.mService.setMultiWindowStyleWithLogging(activityToken, style, loggingReason);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setMultiWindowStyle(IBinder activityToken, MultiWindowStyle style) {
        try {
            if (this.mService != null) {
                this.mService.setMultiWindowStyle(activityToken, style);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public MultiWindowStyle getMultiWindowStyle(IBinder activityToken) {
        try {
            return this.mService.getMultiWindowStyle(activityToken);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setStackBound(IBinder activityToken, Rect bound) {
        try {
            this.mService.setStackBound(activityToken, bound);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Rect getStackBound(IBinder activityToken) {
        try {
            return this.mService.getStackBound(activityToken);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Rect getStackOriginalBound(IBinder activityToken) {
        try {
            return this.mService.getStackOriginalBound(activityToken);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getExpectedOrientation() {
        try {
            return this.mService.getExpectedOrientation();
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public MultiWindowStyle getFrontActivityMultiWindowStyle(int flags) {
        try {
            return this.mService.getFrontActivityMultiWindowStyle(flags);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean exchangeTopTaskToZone(int zone1, int zone2) {
        try {
            return this.mService.exchangeTopTaskToZone(zone1, zone2);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean needHideTrayBar() {
        try {
            return this.mService.needHideTrayBar();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ResolveInfo> getMultiWindowAppList() {
        try {
            return this.mService.getMultiWindowAppList();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<RunningTaskInfo> getRunningTasks(int maxNum, int flag) throws SecurityException {
        try {
            return this.mService.getRunningTasks(maxNum, flag);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void exitByCloseBtn(int taskId) {
        try {
            this.mService.exitByCloseBtn(taskId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int getArrangeState() {
        try {
            return this.mService.getArrangeState();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getFocusedStackLayer() {
        try {
            return this.mService.getFocusedStackLayer();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getFocusedZone() {
        try {
            return this.mService.getFocusedZone();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Rect getZoneBounds(int zone) {
        try {
            return this.mService.getZoneBounds(zone);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setMultiWindowTrayOpenState(boolean open) {
        try {
            this.mService.setMultiWindowTrayOpenState(open);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setDragAndDropModeOfStack(IBinder activityToken, boolean enable) {
        try {
            this.mService.setDragAndDropModeOfStack(activityToken, enable);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean getDragAndDropModeOfStack(IBinder activityToken) {
        try {
            return this.mService.getDragAndDropModeOfStack(activityToken);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean needToExposureTitleBarMenu() {
        try {
            return this.mService.needToExposureTitleBarMenu();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getGlobalSystemUiVisibility() {
        try {
            return this.mService.getGlobalSystemUiVisibility();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void changeTaskToFull(int zone) {
        try {
            this.mService.changeTaskToFull(zone);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean changeTaskToCascade(Point point, int zone, boolean bMinimize) {
        try {
            return this.mService.changeTaskToCascade(point, zone, bMinimize);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean minimizeTask(int taskId, Point iconLocation, boolean stayResumed) {
        try {
            return this.mService.minimizeTask(taskId, iconLocation, stayResumed);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getAvailableMultiInstanceCnt() {
        try {
            return this.mService.getAvailableMultiInstanceCnt();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setFocusAppByZone(int zone) {
        try {
            this.mService.setFocusAppByZone(zone);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void updateIsolatedCenterPoint(Point point) {
        try {
            this.mService.updateIsolatedCenterPoint(point);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean needMoveOnlySpecificTaskToFront(int taskId) {
        try {
            return this.mService.needMoveOnlySpecificTaskToFront(taskId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void moveOnlySpecificTaskToFront(int taskId, Bundle options, int flags, MultiWindowStyle style) {
        try {
            this.mService.moveOnlySpecificTaskToFront(taskId, options, flags, style);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void startActivityFromRecentMultiWindow(int taskId, Bundle options, MultiWindowStyle style) {
        try {
            this.mService.startActivityFromRecentMultiWindow(taskId, options, style);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public float getSplitMinWeight() {
        try {
            return this.mService.getSplitMinWeight();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public float getSplitMaxWeight() {
        try {
            return this.mService.getSplitMaxWeight();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 1.0f;
        }
    }

    public boolean isEnableMakePenWindow() {
        try {
            return this.mService.isEnableMakePenWindow(-1);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEnableMakePenWindow(int notIncludeTaskId) {
        try {
            return this.mService.isEnableMakePenWindow(notIncludeTaskId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Point getStackPosition(IBinder activityToken) {
        try {
            return this.mService.getStackPosition(activityToken);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateMinimizeSize(IBinder activityToken, int[] size) {
        try {
            this.mService.updateMinimizeSize(activityToken, size);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addTab(IBinder activityToken) {
        try {
            this.mService.addTab(activityToken);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean removeTab(int stackId) {
        try {
            return this.mService.removeTab(stackId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeAllTasks(IBinder activityToken, int flags) {
        removeAllTasks(activityToken, flags, false);
    }

    public void removeAllTasks(IBinder activityToken, int flags, boolean logging) {
        try {
            this.mService.removeAllTasks(activityToken, flags, logging);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void finishAllTasks(IBinder activityToken, int flags) {
        try {
            this.mService.finishAllTasks(activityToken, flags);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public DisplayInfo getSystemDisplayInfo() {
        try {
            return this.mService.getSystemDisplayInfo();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MultiWindowTab> getTabs() {
        try {
            return this.mService.getTabs();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setFocusedStack(int stackId, boolean tapOutSide) {
        try {
            this.mService.setFocusedStack(stackId, tapOutSide);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int getStackId(IBinder activityToken) {
        try {
            return this.mService.getStackId(activityToken);
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void setStackBoundByStackId(int stackId, Rect bound) {
        try {
            this.mService.setStackBoundByStackId(stackId, bound);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setAppVisibility(IBinder token, boolean visible) {
        try {
            this.mService.setAppVisibility(token, visible);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void appMinimizingStarted(IBinder token) {
        try {
            this.mService.appMinimizingStarted(token);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int getRecentTaskSize(int userId, int maxCount) {
        try {
            return this.mService.getRecentTaskSize(userId, maxCount);
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<ComponentName> getRunningScaleWindows() {
        try {
            return this.mService.getRunningScaleWindows();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getCurrentOrientation() {
        try {
            if (this.mService != null) {
                return this.mService.getCurrentOrientation();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void registerTaskController(ITaskController taskController) {
        try {
            this.mService.registerTaskController(taskController);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void registerTaskControllerWithType(ITaskController taskController, int type) {
        try {
            this.mService.registerTaskControllerWithType(taskController, type);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unregisterTaskController(ITaskController taskController) {
        try {
            this.mService.unregisterTaskController(taskController);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public long getEnabledFeaturesFlags() {
        try {
            return this.mService.getEnabledFeaturesFlags();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void updatePreferenceThroughSystemProcess(String key, int value) {
        try {
            this.mService.updatePreferenceThroughSystemProcess(key, value);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int getPreferenceThroughSystemProcess(String key) {
        try {
            return this.mService.getPreferenceThroughSystemProcess(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void updateMultiWindowSetting(String key, boolean enable) {
        updateMultiWindowSetting(key, "prev", enable);
    }

    public void updateMultiWindowSetting(String key, String reason, boolean enable) {
        if (key != null) {
            try {
                Log.d("MultiWindowFacade", "UpdateMultiWindowSetting : " + enable);
                this.mService.updateMultiWindowSetting(key, reason, enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean closeMultiWindowPanel() {
        try {
            return this.mService.closeMultiWindowPanel();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getRunningPenWindowCnt() {
        try {
            return this.mService.getRunningPenWindowCnt();
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean isSplitSupportedForTask(IBinder activityToken) {
        try {
            return this.mService.isSplitSupportedForTask(activityToken);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean getRealSize(Rect outSize, DisplayInfo di) {
        Display display = DisplayManagerGlobal.getInstance().getRealDisplay(0);
        DisplayMetrics dm = new DisplayMetrics();
        if (display != null) {
            di.getAppMetrics(dm, display.getDisplayAdjustments());
            outSize.set(0, 0, dm.widthPixels, dm.heightPixels);
        }
        return false;
    }

    public void registerMultiWindowStyleChangedCallback(IMultiWindowStyleChangedCallback observer) {
        try {
            this.mService.registerMultiWindowStyleChangedCallback(observer);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unregisterMultiWindowStyleChangedCallback(IMultiWindowStyleChangedCallback observer) {
        try {
            this.mService.unregisterMultiWindowStyleChangedCallback(observer);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
