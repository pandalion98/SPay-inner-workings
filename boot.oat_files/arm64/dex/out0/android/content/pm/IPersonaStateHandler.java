package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPersonaStateHandler extends IInterface {

    public static abstract class Stub extends Binder implements IPersonaStateHandler {
        private static final String DESCRIPTOR = "android.content.pm.IPersonaStateHandler";
        static final int TRANSACTION_mapToNormalizedState = 1;
        static final int TRANSACTION_postEvent = 2;
        static final int TRANSACTION_postEventForPersona = 3;

        private static class Proxy implements IPersonaStateHandler {
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

            public int mapToNormalizedState(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int postEvent(PersonaEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int postEventForPersona(int personaId, PersonaEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPersonaStateHandler asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPersonaStateHandler)) {
                return new Proxy(obj);
            }
            return (IPersonaStateHandler) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _result;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = mapToNormalizedState(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    PersonaEvent _arg0;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (PersonaEvent) PersonaEvent.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result = postEvent(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 3:
                    PersonaEvent _arg1;
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (PersonaEvent) PersonaEvent.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    _result = postEventForPersona(_arg02, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    int mapToNormalizedState(int i) throws RemoteException;

    int postEvent(PersonaEvent personaEvent) throws RemoteException;

    int postEventForPersona(int i, PersonaEvent personaEvent) throws RemoteException;
}
