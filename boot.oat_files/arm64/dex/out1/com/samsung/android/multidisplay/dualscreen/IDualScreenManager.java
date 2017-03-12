package com.samsung.android.multidisplay.dualscreen;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.samsung.android.dualscreen.DualScreen;
import com.samsung.android.dualscreen.TaskInfo;
import java.util.List;

public interface IDualScreenManager extends IInterface {

    public static abstract class Stub extends Binder implements IDualScreenManager {
        private static final String DESCRIPTOR = "com.samsung.android.multidisplay.dualscreen.IDualScreenManager";
        static final int TRANSACTION_canBeCoupled = 1;
        static final int TRANSACTION_canBeExpanded = 2;
        static final int TRANSACTION_dimScreen = 19;
        static final int TRANSACTION_finishCoupledActivity = 27;
        static final int TRANSACTION_fixTask = 3;
        static final int TRANSACTION_fixTopTask = 5;
        static final int TRANSACTION_focusScreen = 6;
        static final int TRANSACTION_forceFocusScreen = 7;
        static final int TRANSACTION_getFocusedScreen = 8;
        static final int TRANSACTION_getOrientation = 9;
        static final int TRANSACTION_getScreen = 11;
        static final int TRANSACTION_getTaskInfo = 10;
        static final int TRANSACTION_getTasks = 12;
        static final int TRANSACTION_getTopRunningTaskIdWithPermission = 13;
        static final int TRANSACTION_getTopRunningTaskInfo = 14;
        static final int TRANSACTION_isExpandable = 15;
        static final int TRANSACTION_isInFixedScreenMode = 16;
        static final int TRANSACTION_moveTaskToScreen = 17;
        static final int TRANSACTION_moveTaskToScreenWithPermission = 18;
        static final int TRANSACTION_overrideNextAppTransition = 20;
        static final int TRANSACTION_registerDualScreenCallbacks = 21;
        static final int TRANSACTION_registerExpandableActivity = 22;
        static final int TRANSACTION_requestExpandedDisplayOrientation = 24;
        static final int TRANSACTION_requestOppositeDisplayOrientation = 23;
        static final int TRANSACTION_sendExpandRequest = 29;
        static final int TRANSACTION_sendShrinkRequest = 30;
        static final int TRANSACTION_setExpandable = 25;
        static final int TRANSACTION_setFinishWithCoupledTask = 26;
        static final int TRANSACTION_swapTopTask = 28;
        static final int TRANSACTION_unfixTask = 4;
        static final int TRANSACTION_unfixTopTask = 31;
        static final int TRANSACTION_unregisterDualScreenCallbacks = 32;
        static final int TRANSACTION_unregisterExpandableActivity = 33;

        private static class Proxy implements IDualScreenManager {
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

            public boolean canBeCoupled(IBinder token) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(1, _data, _reply, 0);
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

            public boolean canBeExpanded(int taskId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(2, _data, _reply, 0);
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

            public void fixTask(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unfixTask(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void fixTopTask(DualScreen screen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (screen != null) {
                        _data.writeInt(1);
                        screen.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void focusScreen(IBinder token, DualScreen screen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (screen != null) {
                        _data.writeInt(1);
                        screen.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forceFocusScreen(DualScreen screen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (screen != null) {
                        _data.writeInt(1);
                        screen.writeToParcel(_data, 0);
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

            public DualScreen getFocusedScreen() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    DualScreen _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (DualScreen) DualScreen.CREATOR.createFromParcel(_reply);
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

            public int getOrientation(DualScreen screen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (screen != null) {
                        _data.writeInt(1);
                        screen.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public TaskInfo getTaskInfo(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    TaskInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (TaskInfo) TaskInfo.CREATOR.createFromParcel(_reply);
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

            public DualScreen getScreen(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    DualScreen _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (DualScreen) DualScreen.CREATOR.createFromParcel(_reply);
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

            public List<TaskInfo> getTasks(int maxNum, int flags, DualScreen screen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(flags);
                    if (screen != null) {
                        _data.writeInt(1);
                        screen.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    List<TaskInfo> _result = _reply.createTypedArrayList(TaskInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getTopRunningTaskIdWithPermission(DualScreen screen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (screen != null) {
                        _data.writeInt(1);
                        screen.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public TaskInfo getTopRunningTaskInfo(DualScreen screen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    TaskInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (screen != null) {
                        _data.writeInt(1);
                        screen.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (TaskInfo) TaskInfo.CREATOR.createFromParcel(_reply);
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

            public boolean isExpandable(int taskId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(15, _data, _reply, 0);
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

            public boolean isInFixedScreenMode(DualScreen screen) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (screen != null) {
                        _data.writeInt(1);
                        screen.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(16, _data, _reply, 0);
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

            public void moveTaskToScreen(IBinder token, DualScreen toScreen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (toScreen != null) {
                        _data.writeInt(1);
                        toScreen.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void moveTaskToScreenWithPermission(int taskId, DualScreen toScreen, int flags, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (toScreen != null) {
                        _data.writeInt(1);
                        toScreen.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dimScreen(IBinder token, DualScreen screen, boolean enable) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (screen != null) {
                        _data.writeInt(1);
                        screen.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enable) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void overrideNextAppTransition(IBinder token, DualScreen screen, int transit) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (screen != null) {
                        _data.writeInt(1);
                        screen.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(transit);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerDualScreenCallbacks(IDualScreenCallbacks callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerExpandableActivity(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestOppositeDisplayOrientation(IBinder token, int requestedOrientation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(requestedOrientation);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestExpandedDisplayOrientation(IBinder token, int requestedOrientation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(requestedOrientation);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setExpandable(IBinder token, boolean expandable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (expandable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFinishWithCoupledTask(IBinder token, boolean finish) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (finish) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void finishCoupledActivity(IBinder token, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(flags);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void swapTopTask() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendExpandRequest(int targetTaskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(targetTaskId);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendShrinkRequest(int targetTaskId, DualScreen toScreen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(targetTaskId);
                    if (toScreen != null) {
                        _data.writeInt(1);
                        toScreen.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unfixTopTask(DualScreen screen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (screen != null) {
                        _data.writeInt(1);
                        screen.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterDualScreenCallbacks(IDualScreenCallbacks callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterExpandableActivity(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(33, _data, _reply, 0);
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

        public static IDualScreenManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDualScreenManager)) {
                return new Proxy(obj);
            }
            return (IDualScreenManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            boolean _result;
            DualScreen _arg0;
            IBinder _arg02;
            DualScreen _arg1;
            DualScreen _result2;
            int _result3;
            TaskInfo _result4;
            int _arg03;
            boolean _arg12;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = canBeCoupled(data.readStrongBinder());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = canBeExpanded(data.readInt());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    fixTask(data.readInt());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    unfixTask(data.readInt());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (DualScreen) DualScreen.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    fixTopTask(_arg0);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg1 = (DualScreen) DualScreen.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    focusScreen(_arg02, _arg1);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (DualScreen) DualScreen.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    forceFocusScreen(_arg0);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getFocusedScreen();
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (DualScreen) DualScreen.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result3 = getOrientation(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getTaskInfo(data.readInt());
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getScreen(data.readInt());
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 12:
                    DualScreen _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    _arg03 = data.readInt();
                    int _arg13 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = (DualScreen) DualScreen.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    List<TaskInfo> _result5 = getTasks(_arg03, _arg13, _arg2);
                    reply.writeNoException();
                    reply.writeTypedList(_result5);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (DualScreen) DualScreen.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result3 = getTopRunningTaskIdWithPermission(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (DualScreen) DualScreen.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result4 = getTopRunningTaskInfo(_arg0);
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isExpandable(data.readInt());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (DualScreen) DualScreen.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result = isInFixedScreenMode(_arg0);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg1 = (DualScreen) DualScreen.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    moveTaskToScreen(_arg02, _arg1);
                    reply.writeNoException();
                    return true;
                case 18:
                    Bundle _arg3;
                    data.enforceInterface(DESCRIPTOR);
                    _arg03 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (DualScreen) DualScreen.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    int _arg22 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    moveTaskToScreenWithPermission(_arg03, _arg1, _arg22, _arg3);
                    reply.writeNoException();
                    return true;
                case 19:
                    boolean _arg23;
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg1 = (DualScreen) DualScreen.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg23 = true;
                    } else {
                        _arg23 = false;
                    }
                    dimScreen(_arg02, _arg1, _arg23);
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg1 = (DualScreen) DualScreen.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    overrideNextAppTransition(_arg02, _arg1, data.readInt());
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    registerDualScreenCallbacks(com.samsung.android.multidisplay.dualscreen.IDualScreenCallbacks.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    registerExpandableActivity(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    requestOppositeDisplayOrientation(data.readStrongBinder(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    requestExpandedDisplayOrientation(data.readStrongBinder(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg12 = true;
                    } else {
                        _arg12 = false;
                    }
                    setExpandable(_arg02, _arg12);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg12 = true;
                    } else {
                        _arg12 = false;
                    }
                    setFinishWithCoupledTask(_arg02, _arg12);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    finishCoupledActivity(data.readStrongBinder(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    swapTopTask();
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    sendExpandRequest(data.readInt());
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    _arg03 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (DualScreen) DualScreen.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    sendShrinkRequest(_arg03, _arg1);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (DualScreen) DualScreen.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    unfixTopTask(_arg0);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterDualScreenCallbacks(com.samsung.android.multidisplay.dualscreen.IDualScreenCallbacks.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterExpandableActivity(data.readStrongBinder());
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

    boolean canBeCoupled(IBinder iBinder) throws RemoteException;

    boolean canBeExpanded(int i) throws RemoteException;

    void dimScreen(IBinder iBinder, DualScreen dualScreen, boolean z) throws RemoteException;

    void finishCoupledActivity(IBinder iBinder, int i) throws RemoteException;

    void fixTask(int i) throws RemoteException;

    void fixTopTask(DualScreen dualScreen) throws RemoteException;

    void focusScreen(IBinder iBinder, DualScreen dualScreen) throws RemoteException;

    void forceFocusScreen(DualScreen dualScreen) throws RemoteException;

    DualScreen getFocusedScreen() throws RemoteException;

    int getOrientation(DualScreen dualScreen) throws RemoteException;

    DualScreen getScreen(int i) throws RemoteException;

    TaskInfo getTaskInfo(int i) throws RemoteException;

    List<TaskInfo> getTasks(int i, int i2, DualScreen dualScreen) throws RemoteException;

    int getTopRunningTaskIdWithPermission(DualScreen dualScreen) throws RemoteException;

    TaskInfo getTopRunningTaskInfo(DualScreen dualScreen) throws RemoteException;

    boolean isExpandable(int i) throws RemoteException;

    boolean isInFixedScreenMode(DualScreen dualScreen) throws RemoteException;

    void moveTaskToScreen(IBinder iBinder, DualScreen dualScreen) throws RemoteException;

    void moveTaskToScreenWithPermission(int i, DualScreen dualScreen, int i2, Bundle bundle) throws RemoteException;

    void overrideNextAppTransition(IBinder iBinder, DualScreen dualScreen, int i) throws RemoteException;

    void registerDualScreenCallbacks(IDualScreenCallbacks iDualScreenCallbacks) throws RemoteException;

    void registerExpandableActivity(IBinder iBinder) throws RemoteException;

    void requestExpandedDisplayOrientation(IBinder iBinder, int i) throws RemoteException;

    void requestOppositeDisplayOrientation(IBinder iBinder, int i) throws RemoteException;

    void sendExpandRequest(int i) throws RemoteException;

    void sendShrinkRequest(int i, DualScreen dualScreen) throws RemoteException;

    void setExpandable(IBinder iBinder, boolean z) throws RemoteException;

    void setFinishWithCoupledTask(IBinder iBinder, boolean z) throws RemoteException;

    void swapTopTask() throws RemoteException;

    void unfixTask(int i) throws RemoteException;

    void unfixTopTask(DualScreen dualScreen) throws RemoteException;

    void unregisterDualScreenCallbacks(IDualScreenCallbacks iDualScreenCallbacks) throws RemoteException;

    void unregisterExpandableActivity(IBinder iBinder) throws RemoteException;
}
