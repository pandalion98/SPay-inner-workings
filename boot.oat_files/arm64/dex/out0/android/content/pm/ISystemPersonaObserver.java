package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISystemPersonaObserver extends IInterface {

    public static abstract class Stub extends Binder implements ISystemPersonaObserver {
        private static final String DESCRIPTOR = "android.content.pm.ISystemPersonaObserver";
        static final int TRANSACTION_onKnoxContainerLaunch = 4;
        static final int TRANSACTION_onPersonaActive = 1;
        static final int TRANSACTION_onRemovePersona = 2;
        static final int TRANSACTION_onResetPersona = 3;
        static final int TRANSACTION_onStateChange = 5;

        private static class Proxy implements ISystemPersonaObserver {
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

            public void onPersonaActive(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onRemovePersona(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onResetPersona(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onKnoxContainerLaunch(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onStateChange(int personaId, PersonaState oldState, PersonaState newState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (oldState != null) {
                        _data.writeInt(1);
                        oldState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (newState != null) {
                        _data.writeInt(1);
                        newState.writeToParcel(_data, 0);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISystemPersonaObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISystemPersonaObserver)) {
                return new Proxy(obj);
            }
            return (ISystemPersonaObserver) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onPersonaActive(data.readInt());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onRemovePersona(data.readInt());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    onResetPersona(data.readInt());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    onKnoxContainerLaunch(data.readInt());
                    reply.writeNoException();
                    return true;
                case 5:
                    PersonaState _arg1;
                    PersonaState _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (PersonaState) PersonaState.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = (PersonaState) PersonaState.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    onStateChange(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onKnoxContainerLaunch(int i) throws RemoteException;

    void onPersonaActive(int i) throws RemoteException;

    void onRemovePersona(int i) throws RemoteException;

    void onResetPersona(int i) throws RemoteException;

    void onStateChange(int i, PersonaState personaState, PersonaState personaState2) throws RemoteException;
}
