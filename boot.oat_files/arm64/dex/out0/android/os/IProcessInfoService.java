package android.os;

public interface IProcessInfoService extends IInterface {

    public static abstract class Stub extends Binder implements IProcessInfoService {
        private static final String DESCRIPTOR = "android.os.IProcessInfoService";
        static final int TRANSACTION_getProcessStatesFromPids = 1;

        private static class Proxy implements IProcessInfoService {
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

            public void getProcessStatesFromPids(int[] pids, int[] states) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    if (states == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(states.length);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    _reply.readIntArray(states);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IProcessInfoService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IProcessInfoService)) {
                return new Proxy(obj);
            }
            return (IProcessInfoService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    int[] _arg1;
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg0 = data.createIntArray();
                    int _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new int[_arg1_length];
                    }
                    getProcessStatesFromPids(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeIntArray(_arg1);
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void getProcessStatesFromPids(int[] iArr, int[] iArr2) throws RemoteException;
}
