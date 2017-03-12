package com.samsung.android.multiwindow;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.sec.multiwindow.impl.IMultiWindowStyleChangedCallback;
import android.view.DisplayInfo;
import java.util.List;

public interface IMultiWindowFacade extends IInterface {

    public static abstract class Stub extends Binder implements IMultiWindowFacade {
        private static final String DESCRIPTOR = "com.samsung.android.multiwindow.IMultiWindowFacade";
        static final int TRANSACTION_addTab = 35;
        static final int TRANSACTION_appMinimizingStarted = 45;
        static final int TRANSACTION_changeTaskToCascade = 22;
        static final int TRANSACTION_changeTaskToFull = 21;
        static final int TRANSACTION_closeMultiWindowPanel = 58;
        static final int TRANSACTION_exchangeTopTaskToZone = 12;
        static final int TRANSACTION_exitByCloseBtn = 14;
        static final int TRANSACTION_finishAllTasks = 43;
        static final int TRANSACTION_getArrangeState = 15;
        static final int TRANSACTION_getAvailableMultiInstanceCnt = 24;
        static final int TRANSACTION_getCenterBarPoint = 3;
        static final int TRANSACTION_getCurrentOrientation = 50;
        static final int TRANSACTION_getDragAndDropModeOfStack = 48;
        static final int TRANSACTION_getEnabledFeaturesFlags = 54;
        static final int TRANSACTION_getExpectedOrientation = 10;
        static final int TRANSACTION_getFocusedStackLayer = 16;
        static final int TRANSACTION_getFocusedZone = 17;
        static final int TRANSACTION_getFrontActivityMultiWindowStyle = 11;
        static final int TRANSACTION_getGlobalSystemUiVisibility = 20;
        static final int TRANSACTION_getMultiWindowAppList = 62;
        static final int TRANSACTION_getMultiWindowStyle = 6;
        static final int TRANSACTION_getPreferenceThroughSystemProcess = 56;
        static final int TRANSACTION_getRecentTaskSize = 44;
        static final int TRANSACTION_getRunningPenWindowCnt = 59;
        static final int TRANSACTION_getRunningScaleWindows = 49;
        static final int TRANSACTION_getRunningTasks = 13;
        static final int TRANSACTION_getSplitMaxWeight = 31;
        static final int TRANSACTION_getSplitMinWeight = 30;
        static final int TRANSACTION_getStackBound = 8;
        static final int TRANSACTION_getStackId = 39;
        static final int TRANSACTION_getStackOriginalBound = 9;
        static final int TRANSACTION_getStackPosition = 33;
        static final int TRANSACTION_getSystemDisplayInfo = 63;
        static final int TRANSACTION_getTabs = 37;
        static final int TRANSACTION_getZoneBounds = 18;
        static final int TRANSACTION_isEnableMakePenWindow = 32;
        static final int TRANSACTION_isSplitSupportedForTask = 60;
        static final int TRANSACTION_minimizeTask = 23;
        static final int TRANSACTION_minimizeWindow = 1;
        static final int TRANSACTION_moveOnlySpecificTaskToFront = 28;
        static final int TRANSACTION_needHideTrayBar = 61;
        static final int TRANSACTION_needMoveOnlySpecificTaskToFront = 27;
        static final int TRANSACTION_needToExposureTitleBarMenu = 46;
        static final int TRANSACTION_registerMultiWindowStyleChangedCallback = 64;
        static final int TRANSACTION_registerTaskController = 51;
        static final int TRANSACTION_registerTaskControllerWithType = 52;
        static final int TRANSACTION_removeAllTasks = 42;
        static final int TRANSACTION_removeTab = 36;
        static final int TRANSACTION_setAppVisibility = 41;
        static final int TRANSACTION_setCenterBarPoint = 2;
        static final int TRANSACTION_setDragAndDropModeOfStack = 47;
        static final int TRANSACTION_setFocusAppByZone = 25;
        static final int TRANSACTION_setFocusedStack = 38;
        static final int TRANSACTION_setMultiWindowStyle = 4;
        static final int TRANSACTION_setMultiWindowStyleWithLogging = 5;
        static final int TRANSACTION_setMultiWindowTrayOpenState = 19;
        static final int TRANSACTION_setStackBound = 7;
        static final int TRANSACTION_setStackBoundByStackId = 40;
        static final int TRANSACTION_startActivityFromRecentMultiWindow = 29;
        static final int TRANSACTION_unregisterMultiWindowStyleChangedCallback = 65;
        static final int TRANSACTION_unregisterTaskController = 53;
        static final int TRANSACTION_updateIsolatedCenterPoint = 26;
        static final int TRANSACTION_updateMinimizeSize = 34;
        static final int TRANSACTION_updateMultiWindowSetting = 57;
        static final int TRANSACTION_updatePreferenceThroughSystemProcess = 55;

        private static class Proxy implements IMultiWindowFacade {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void minimizeWindow(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCenterBarPoint(int displayId, Point point) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (point != null) {
                        _data.writeInt(1);
                        point.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Point getCenterBarPoint(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Point _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Point) Point.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMultiWindowStyle(IBinder activityToken, MultiWindowStyle style) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    if (style != null) {
                        _data.writeInt(1);
                        style.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMultiWindowStyleWithLogging(IBinder activityToken, MultiWindowStyle style, int loggingReason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    if (style != null) {
                        _data.writeInt(1);
                        style.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(loggingReason);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public MultiWindowStyle getMultiWindowStyle(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    MultiWindowStyle _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (MultiWindowStyle) MultiWindowStyle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setStackBound(IBinder activityToken, Rect bound) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    if (bound != null) {
                        _data.writeInt(1);
                        bound.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Rect getStackBound(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Rect _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Rect) Rect.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Rect getStackOriginalBound(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Rect _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Rect) Rect.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getExpectedOrientation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public MultiWindowStyle getFrontActivityMultiWindowStyle(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    MultiWindowStyle _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (MultiWindowStyle) MultiWindowStyle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean exchangeTopTaskToZone(int zone1, int zone2) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(zone1);
                    _data.writeInt(zone2);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List getRunningTasks(int maxNum, int flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(flag);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    List _result = _reply.readArrayList(getClass().getClassLoader());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void exitByCloseBtn(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getArrangeState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getFocusedStackLayer() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getFocusedZone() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Rect getZoneBounds(int zone) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Rect _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(zone);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Rect) Rect.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMultiWindowTrayOpenState(boolean open) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (open) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getGlobalSystemUiVisibility() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void changeTaskToFull(int zone) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(zone);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean changeTaskToCascade(Point point, int zone, boolean bMinimize) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (point != null) {
                        _data.writeInt(1);
                        point.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(zone);
                    _data.writeInt(bMinimize ? 1 : 0);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean minimizeTask(int taskId, Point iconLocation, boolean stayResumed) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (iconLocation != null) {
                        _data.writeInt(1);
                        iconLocation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(stayResumed ? 1 : 0);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAvailableMultiInstanceCnt() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFocusAppByZone(int zone) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(zone);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateIsolatedCenterPoint(Point point) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (point != null) {
                        _data.writeInt(1);
                        point.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean needMoveOnlySpecificTaskToFront(int taskId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void moveOnlySpecificTaskToFront(int taskId, Bundle options, int flags, MultiWindowStyle style) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    if (style != null) {
                        _data.writeInt(1);
                        style.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startActivityFromRecentMultiWindow(int taskId, Bundle options, MultiWindowStyle style) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (style != null) {
                        _data.writeInt(1);
                        style.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public float getSplitMinWeight() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    float _result = _reply.readFloat();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public float getSplitMaxWeight() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    float _result = _reply.readFloat();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isEnableMakePenWindow(int notIncludeTaskId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(notIncludeTaskId);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Point getStackPosition(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Point _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Point) Point.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateMinimizeSize(IBinder activityToken, int[] size) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    _data.writeIntArray(size);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addTab(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeTab(int stackId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List getTabs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    List _result = _reply.readArrayList(getClass().getClassLoader());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFocusedStack(int stackId, boolean tapOutSide) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    if (tapOutSide) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getStackId(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setStackBoundByStackId(int stackId, Rect bound) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    if (bound != null) {
                        _data.writeInt(1);
                        bound.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAppVisibility(IBinder token, boolean visible) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (visible) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeAllTasks(IBinder activityToken, int flags, boolean logging) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    _data.writeInt(flags);
                    if (logging) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void finishAllTasks(IBinder activityToken, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    _data.writeInt(flags);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRecentTaskSize(int userId, int maxCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(maxCount);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void appMinimizingStarted(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean needToExposureTitleBarMenu() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDragAndDropModeOfStack(IBinder activityToken, boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getDragAndDropModeOfStack(IBinder activityToken) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List getRunningScaleWindows() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    List _result = _reply.readArrayList(getClass().getClassLoader());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCurrentOrientation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerTaskController(ITaskController taskController) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(taskController != null ? taskController.asBinder() : null);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerTaskControllerWithType(ITaskController taskController, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(taskController != null ? taskController.asBinder() : null);
                    _data.writeInt(type);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterTaskController(ITaskController taskController) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(taskController != null ? taskController.asBinder() : null);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getEnabledFeaturesFlags() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updatePreferenceThroughSystemProcess(String key, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(value);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPreferenceThroughSystemProcess(String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateMultiWindowSetting(String requester, String reason, boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(requester);
                    _data.writeString(reason);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean closeMultiWindowPanel() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRunningPenWindowCnt() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSplitSupportedForTask(IBinder activityToken) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean needHideTrayBar() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List getMultiWindowAppList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                    List _result = _reply.readArrayList(getClass().getClassLoader());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public DisplayInfo getSystemDisplayInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    DisplayInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (DisplayInfo) DisplayInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerMultiWindowStyleChangedCallback(IMultiWindowStyleChangedCallback observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterMultiWindowStyleChangedCallback(IMultiWindowStyleChangedCallback observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMultiWindowFacade asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMultiWindowFacade)) {
                return new Proxy(obj);
            }
            return (IMultiWindowFacade) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            int _arg0;
            Point _arg1;
            Point _result;
            IBinder _arg02;
            MultiWindowStyle _arg12;
            MultiWindowStyle _result2;
            Rect _arg13;
            Rect _result3;
            int _result4;
            boolean _result5;
            List _result6;
            Point _arg03;
            int _arg14;
            boolean _arg2;
            Bundle _arg15;
            float _result7;
            boolean _arg16;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    minimizeWindow(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (Point) Point.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    setCenterBarPoint(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getCenterBarPoint(data.readInt());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg12 = (MultiWindowStyle) MultiWindowStyle.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    setMultiWindowStyle(_arg02, _arg12);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg12 = (MultiWindowStyle) MultiWindowStyle.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    setMultiWindowStyleWithLogging(_arg02, _arg12, data.readInt());
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getMultiWindowStyle(data.readStrongBinder());
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg13 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    setStackBound(_arg02, _arg13);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getStackBound(data.readStrongBinder());
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getStackOriginalBound(data.readStrongBinder());
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getExpectedOrientation();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getFrontActivityMultiWindowStyle(data.readInt());
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = exchangeTopTaskToZone(data.readInt(), data.readInt());
                    reply.writeNoException();
                    if (_result5) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getRunningTasks(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeList(_result6);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    exitByCloseBtn(data.readInt());
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getArrangeState();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getFocusedStackLayer();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getFocusedZone();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getZoneBounds(data.readInt());
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 19:
                    boolean _arg04;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = true;
                    } else {
                        _arg04 = false;
                    }
                    setMultiWindowTrayOpenState(_arg04);
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getGlobalSystemUiVisibility();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    changeTaskToFull(data.readInt());
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (Point) Point.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    _arg14 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    } else {
                        _arg2 = false;
                    }
                    _result5 = changeTaskToCascade(_arg03, _arg14, _arg2);
                    reply.writeNoException();
                    if (_result5) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (Point) Point.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    } else {
                        _arg2 = false;
                    }
                    _result5 = minimizeTask(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    if (_result5) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getAvailableMultiInstanceCnt();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    setFocusAppByZone(data.readInt());
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (Point) Point.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    updateIsolatedCenterPoint(_arg03);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = needMoveOnlySpecificTaskToFront(data.readInt());
                    reply.writeNoException();
                    if (_result5) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 28:
                    MultiWindowStyle _arg3;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg15 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg15 = null;
                    }
                    int _arg22 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = (MultiWindowStyle) MultiWindowStyle.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    moveOnlySpecificTaskToFront(_arg0, _arg15, _arg22, _arg3);
                    reply.writeNoException();
                    return true;
                case 29:
                    MultiWindowStyle _arg23;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg15 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg15 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg23 = (MultiWindowStyle) MultiWindowStyle.CREATOR.createFromParcel(data);
                    } else {
                        _arg23 = null;
                    }
                    startActivityFromRecentMultiWindow(_arg0, _arg15, _arg23);
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = getSplitMinWeight();
                    reply.writeNoException();
                    reply.writeFloat(_result7);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = getSplitMaxWeight();
                    reply.writeNoException();
                    reply.writeFloat(_result7);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = isEnableMakePenWindow(data.readInt());
                    reply.writeNoException();
                    if (_result5) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getStackPosition(data.readStrongBinder());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    updateMinimizeSize(data.readStrongBinder(), data.createIntArray());
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    addTab(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = removeTab(data.readInt());
                    reply.writeNoException();
                    if (_result5) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getTabs();
                    reply.writeNoException();
                    reply.writeList(_result6);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg16 = true;
                    } else {
                        _arg16 = false;
                    }
                    setFocusedStack(_arg0, _arg16);
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getStackId(data.readStrongBinder());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg13 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    setStackBoundByStackId(_arg0, _arg13);
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg16 = true;
                    } else {
                        _arg16 = false;
                    }
                    setAppVisibility(_arg02, _arg16);
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    _arg14 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    } else {
                        _arg2 = false;
                    }
                    removeAllTasks(_arg02, _arg14, _arg2);
                    reply.writeNoException();
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    finishAllTasks(data.readStrongBinder(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getRecentTaskSize(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    appMinimizingStarted(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = needToExposureTitleBarMenu();
                    reply.writeNoException();
                    if (_result5) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg16 = true;
                    } else {
                        _arg16 = false;
                    }
                    setDragAndDropModeOfStack(_arg02, _arg16);
                    reply.writeNoException();
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getDragAndDropModeOfStack(data.readStrongBinder());
                    reply.writeNoException();
                    if (_result5) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getRunningScaleWindows();
                    reply.writeNoException();
                    reply.writeList(_result6);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getCurrentOrientation();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    registerTaskController(com.samsung.android.multiwindow.ITaskController.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    registerTaskControllerWithType(com.samsung.android.multiwindow.ITaskController.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterTaskController(com.samsung.android.multiwindow.ITaskController.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    long _result8 = getEnabledFeaturesFlags();
                    reply.writeNoException();
                    reply.writeLong(_result8);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    updatePreferenceThroughSystemProcess(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getPreferenceThroughSystemProcess(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    String _arg17 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    } else {
                        _arg2 = false;
                    }
                    updateMultiWindowSetting(_arg05, _arg17, _arg2);
                    reply.writeNoException();
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = closeMultiWindowPanel();
                    reply.writeNoException();
                    if (_result5) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getRunningPenWindowCnt();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = isSplitSupportedForTask(data.readStrongBinder());
                    reply.writeNoException();
                    if (_result5) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = needHideTrayBar();
                    reply.writeNoException();
                    if (_result5) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getMultiWindowAppList();
                    reply.writeNoException();
                    reply.writeList(_result6);
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    DisplayInfo _result9 = getSystemDisplayInfo();
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    registerMultiWindowStyleChangedCallback(android.sec.multiwindow.impl.IMultiWindowStyleChangedCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterMultiWindowStyleChangedCallback(android.sec.multiwindow.impl.IMultiWindowStyleChangedCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void addTab(IBinder iBinder) throws RemoteException;

    void appMinimizingStarted(IBinder iBinder) throws RemoteException;

    boolean changeTaskToCascade(Point point, int i, boolean z) throws RemoteException;

    void changeTaskToFull(int i) throws RemoteException;

    boolean closeMultiWindowPanel() throws RemoteException;

    boolean exchangeTopTaskToZone(int i, int i2) throws RemoteException;

    void exitByCloseBtn(int i) throws RemoteException;

    void finishAllTasks(IBinder iBinder, int i) throws RemoteException;

    int getArrangeState() throws RemoteException;

    int getAvailableMultiInstanceCnt() throws RemoteException;

    Point getCenterBarPoint(int i) throws RemoteException;

    int getCurrentOrientation() throws RemoteException;

    boolean getDragAndDropModeOfStack(IBinder iBinder) throws RemoteException;

    long getEnabledFeaturesFlags() throws RemoteException;

    int getExpectedOrientation() throws RemoteException;

    int getFocusedStackLayer() throws RemoteException;

    int getFocusedZone() throws RemoteException;

    MultiWindowStyle getFrontActivityMultiWindowStyle(int i) throws RemoteException;

    int getGlobalSystemUiVisibility() throws RemoteException;

    List getMultiWindowAppList() throws RemoteException;

    MultiWindowStyle getMultiWindowStyle(IBinder iBinder) throws RemoteException;

    int getPreferenceThroughSystemProcess(String str) throws RemoteException;

    int getRecentTaskSize(int i, int i2) throws RemoteException;

    int getRunningPenWindowCnt() throws RemoteException;

    List getRunningScaleWindows() throws RemoteException;

    List getRunningTasks(int i, int i2) throws RemoteException;

    float getSplitMaxWeight() throws RemoteException;

    float getSplitMinWeight() throws RemoteException;

    Rect getStackBound(IBinder iBinder) throws RemoteException;

    int getStackId(IBinder iBinder) throws RemoteException;

    Rect getStackOriginalBound(IBinder iBinder) throws RemoteException;

    Point getStackPosition(IBinder iBinder) throws RemoteException;

    DisplayInfo getSystemDisplayInfo() throws RemoteException;

    List getTabs() throws RemoteException;

    Rect getZoneBounds(int i) throws RemoteException;

    boolean isEnableMakePenWindow(int i) throws RemoteException;

    boolean isSplitSupportedForTask(IBinder iBinder) throws RemoteException;

    boolean minimizeTask(int i, Point point, boolean z) throws RemoteException;

    void minimizeWindow(IBinder iBinder) throws RemoteException;

    void moveOnlySpecificTaskToFront(int i, Bundle bundle, int i2, MultiWindowStyle multiWindowStyle) throws RemoteException;

    boolean needHideTrayBar() throws RemoteException;

    boolean needMoveOnlySpecificTaskToFront(int i) throws RemoteException;

    boolean needToExposureTitleBarMenu() throws RemoteException;

    void registerMultiWindowStyleChangedCallback(IMultiWindowStyleChangedCallback iMultiWindowStyleChangedCallback) throws RemoteException;

    void registerTaskController(ITaskController iTaskController) throws RemoteException;

    void registerTaskControllerWithType(ITaskController iTaskController, int i) throws RemoteException;

    void removeAllTasks(IBinder iBinder, int i, boolean z) throws RemoteException;

    boolean removeTab(int i) throws RemoteException;

    void setAppVisibility(IBinder iBinder, boolean z) throws RemoteException;

    void setCenterBarPoint(int i, Point point) throws RemoteException;

    void setDragAndDropModeOfStack(IBinder iBinder, boolean z) throws RemoteException;

    void setFocusAppByZone(int i) throws RemoteException;

    void setFocusedStack(int i, boolean z) throws RemoteException;

    void setMultiWindowStyle(IBinder iBinder, MultiWindowStyle multiWindowStyle) throws RemoteException;

    void setMultiWindowStyleWithLogging(IBinder iBinder, MultiWindowStyle multiWindowStyle, int i) throws RemoteException;

    void setMultiWindowTrayOpenState(boolean z) throws RemoteException;

    void setStackBound(IBinder iBinder, Rect rect) throws RemoteException;

    void setStackBoundByStackId(int i, Rect rect) throws RemoteException;

    void startActivityFromRecentMultiWindow(int i, Bundle bundle, MultiWindowStyle multiWindowStyle) throws RemoteException;

    void unregisterMultiWindowStyleChangedCallback(IMultiWindowStyleChangedCallback iMultiWindowStyleChangedCallback) throws RemoteException;

    void unregisterTaskController(ITaskController iTaskController) throws RemoteException;

    void updateIsolatedCenterPoint(Point point) throws RemoteException;

    void updateMinimizeSize(IBinder iBinder, int[] iArr) throws RemoteException;

    void updateMultiWindowSetting(String str, String str2, boolean z) throws RemoteException;

    void updatePreferenceThroughSystemProcess(String str, int i) throws RemoteException;
}
