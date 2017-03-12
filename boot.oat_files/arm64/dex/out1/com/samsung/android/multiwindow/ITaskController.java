package com.samsung.android.multiwindow;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface ITaskController extends IInterface {

    public static abstract class Stub extends Binder implements ITaskController {
        private static final String DESCRIPTOR = "com.samsung.android.multiwindow.ITaskController";
        static final int TRANSACTION_onTaskRemoved = 3;
        static final int TRANSACTION_onTaskStarted = 1;
        static final int TRANSACTION_onTaskStopped = 2;

        private static class Proxy implements ITaskController {
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

            public void onTaskStarted(List tasks) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeList(tasks);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onTaskStopped(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onTaskRemoved(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITaskController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITaskController)) {
                return new Proxy(obj);
            }
            return (ITaskController) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onTaskStarted(data.readArrayList(getClass().getClassLoader()));
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onTaskStopped(data.readInt());
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    onTaskRemoved(data.readInt());
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onTaskRemoved(int i) throws RemoteException;

    void onTaskStarted(List list) throws RemoteException;

    void onTaskStopped(int i) throws RemoteException;
}
