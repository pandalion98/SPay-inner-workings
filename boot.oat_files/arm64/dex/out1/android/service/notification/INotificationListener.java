package android.service.notification;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INotificationListener extends IInterface {

    public static abstract class Stub extends Binder implements INotificationListener {
        private static final String DESCRIPTOR = "android.service.notification.INotificationListener";
        static final int TRANSACTION_onEdgeNotificationPosted = 7;
        static final int TRANSACTION_onEdgeNotificationRemoved = 8;
        static final int TRANSACTION_onInterruptionFilterChanged = 6;
        static final int TRANSACTION_onListenerConnected = 1;
        static final int TRANSACTION_onListenerHintsChanged = 5;
        static final int TRANSACTION_onNotificationPosted = 2;
        static final int TRANSACTION_onNotificationRankingUpdate = 4;
        static final int TRANSACTION_onNotificationRemoved = 3;

        private static class Proxy implements INotificationListener {
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

            public void onListenerConnected(NotificationRankingUpdate update) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (update != null) {
                        _data.writeInt(1);
                        update.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onNotificationPosted(IStatusBarNotificationHolder notificationHolder, NotificationRankingUpdate update) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (notificationHolder != null) {
                        iBinder = notificationHolder.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    if (update != null) {
                        _data.writeInt(1);
                        update.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onNotificationRemoved(IStatusBarNotificationHolder notificationHolder, NotificationRankingUpdate update) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (notificationHolder != null) {
                        iBinder = notificationHolder.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    if (update != null) {
                        _data.writeInt(1);
                        update.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onNotificationRankingUpdate(NotificationRankingUpdate update) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (update != null) {
                        _data.writeInt(1);
                        update.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onListenerHintsChanged(int hints) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(hints);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onInterruptionFilterChanged(int interruptionFilter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(interruptionFilter);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onEdgeNotificationPosted(String pkg, int id, Bundle extra) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(id);
                    if (extra != null) {
                        _data.writeInt(1);
                        extra.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onEdgeNotificationRemoved(String pkg, int id, Bundle extra) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(id);
                    if (extra != null) {
                        _data.writeInt(1);
                        extra.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INotificationListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INotificationListener)) {
                return new Proxy(obj);
            }
            return (INotificationListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            NotificationRankingUpdate _arg0;
            IStatusBarNotificationHolder _arg02;
            NotificationRankingUpdate _arg1;
            String _arg03;
            int _arg12;
            Bundle _arg2;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (NotificationRankingUpdate) NotificationRankingUpdate.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    onListenerConnected(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = android.service.notification.IStatusBarNotificationHolder.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg1 = (NotificationRankingUpdate) NotificationRankingUpdate.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    onNotificationPosted(_arg02, _arg1);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = android.service.notification.IStatusBarNotificationHolder.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg1 = (NotificationRankingUpdate) NotificationRankingUpdate.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    onNotificationRemoved(_arg02, _arg1);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (NotificationRankingUpdate) NotificationRankingUpdate.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    onNotificationRankingUpdate(_arg0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    onListenerHintsChanged(data.readInt());
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    onInterruptionFilterChanged(data.readInt());
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg03 = data.readString();
                    _arg12 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    onEdgeNotificationPosted(_arg03, _arg12, _arg2);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _arg03 = data.readString();
                    _arg12 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    onEdgeNotificationRemoved(_arg03, _arg12, _arg2);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onEdgeNotificationPosted(String str, int i, Bundle bundle) throws RemoteException;

    void onEdgeNotificationRemoved(String str, int i, Bundle bundle) throws RemoteException;

    void onInterruptionFilterChanged(int i) throws RemoteException;

    void onListenerConnected(NotificationRankingUpdate notificationRankingUpdate) throws RemoteException;

    void onListenerHintsChanged(int i) throws RemoteException;

    void onNotificationPosted(IStatusBarNotificationHolder iStatusBarNotificationHolder, NotificationRankingUpdate notificationRankingUpdate) throws RemoteException;

    void onNotificationRankingUpdate(NotificationRankingUpdate notificationRankingUpdate) throws RemoteException;

    void onNotificationRemoved(IStatusBarNotificationHolder iStatusBarNotificationHolder, NotificationRankingUpdate notificationRankingUpdate) throws RemoteException;
}
