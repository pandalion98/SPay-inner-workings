package android.service.wallpaper;

import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWallpaperService extends IInterface {

    public static abstract class Stub extends Binder implements IWallpaperService {
        private static final String DESCRIPTOR = "android.service.wallpaper.IWallpaperService";
        static final int TRANSACTION_attach = 1;
        static final int TRANSACTION_attachWithDisplayId = 2;

        private static class Proxy implements IWallpaperService {
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

            public void attach(IWallpaperConnection connection, IBinder windowToken, int windowType, boolean isPreview, int reqWidth, int reqHeight, Rect padding) throws RemoteException {
                IBinder iBinder = null;
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (connection != null) {
                        iBinder = connection.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeStrongBinder(windowToken);
                    _data.writeInt(windowType);
                    if (!isPreview) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(reqWidth);
                    _data.writeInt(reqHeight);
                    if (padding != null) {
                        _data.writeInt(1);
                        padding.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void attachWithDisplayId(IWallpaperConnection connection, IBinder windowToken, int windowType, boolean isPreview, int reqWidth, int reqHeight, Rect padding, int displayId) throws RemoteException {
                IBinder iBinder = null;
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (connection != null) {
                        iBinder = connection.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeStrongBinder(windowToken);
                    _data.writeInt(windowType);
                    if (!isPreview) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(reqWidth);
                    _data.writeInt(reqHeight);
                    if (padding != null) {
                        _data.writeInt(1);
                        padding.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(displayId);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWallpaperService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWallpaperService)) {
                return new Proxy(obj);
            }
            return (IWallpaperService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg3 = false;
            IWallpaperConnection _arg0;
            IBinder _arg1;
            int _arg2;
            int _arg4;
            int _arg5;
            Rect _arg6;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = android.service.wallpaper.IWallpaperConnection.Stub.asInterface(data.readStrongBinder());
                    _arg1 = data.readStrongBinder();
                    _arg2 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = true;
                    }
                    _arg4 = data.readInt();
                    _arg5 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg6 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    attach(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = android.service.wallpaper.IWallpaperConnection.Stub.asInterface(data.readStrongBinder());
                    _arg1 = data.readStrongBinder();
                    _arg2 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = true;
                    }
                    _arg4 = data.readInt();
                    _arg5 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg6 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    attachWithDisplayId(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, data.readInt());
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void attach(IWallpaperConnection iWallpaperConnection, IBinder iBinder, int i, boolean z, int i2, int i3, Rect rect) throws RemoteException;

    void attachWithDisplayId(IWallpaperConnection iWallpaperConnection, IBinder iBinder, int i, boolean z, int i2, int i3, Rect rect, int i4) throws RemoteException;
}
