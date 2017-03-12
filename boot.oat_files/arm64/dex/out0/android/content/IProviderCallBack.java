package android.content;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IProviderCallBack extends IInterface {

    public static abstract class Stub extends Binder implements IProviderCallBack {
        private static final String DESCRIPTOR = "android.content.IProviderCallBack";
        static final int TRANSACTION_getCallerInfoDetails = 2;
        static final int TRANSACTION_handleShortcut = 3;
        static final int TRANSACTION_queryProvider = 1;

        private static class Proxy implements IProviderCallBack {
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

            public CustomCursor queryProvider(String providerName, String resourceName, int containerId, String[] projection, String selection, String[] selectionArgs, String sortOrder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    CustomCursor _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(providerName);
                    _data.writeString(resourceName);
                    _data.writeInt(containerId);
                    _data.writeStringArray(projection);
                    _data.writeString(selection);
                    _data.writeStringArray(selectionArgs);
                    _data.writeString(sortOrder);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (CustomCursor) CustomCursor.CREATOR.createFromParcel(_reply);
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

            public CustomCursor getCallerInfoDetails(String contactRefUriAsString) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    CustomCursor _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(contactRefUriAsString);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (CustomCursor) CustomCursor.CREATOR.createFromParcel(_reply);
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

            public void handleShortcut(int userId, String packageName, String packageLabel, Bitmap shortcutIcon, String shortcutIntentUri, String createOrRemove) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    _data.writeString(packageLabel);
                    if (shortcutIcon != null) {
                        _data.writeInt(1);
                        shortcutIcon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(shortcutIntentUri);
                    _data.writeString(createOrRemove);
                    this.mRemote.transact(3, _data, _reply, 0);
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

        public static IProviderCallBack asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IProviderCallBack)) {
                return new Proxy(obj);
            }
            return (IProviderCallBack) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            CustomCursor _result;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = queryProvider(data.readString(), data.readString(), data.readInt(), data.createStringArray(), data.readString(), data.createStringArray(), data.readString());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getCallerInfoDetails(data.readString());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 3:
                    Bitmap _arg3;
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    if (data.readInt() != 0) {
                        _arg3 = (Bitmap) Bitmap.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    handleShortcut(_arg0, _arg1, _arg2, _arg3, data.readString(), data.readString());
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

    CustomCursor getCallerInfoDetails(String str) throws RemoteException;

    void handleShortcut(int i, String str, String str2, Bitmap bitmap, String str3, String str4) throws RemoteException;

    CustomCursor queryProvider(String str, String str2, int i, String[] strArr, String str3, String[] strArr2, String str4) throws RemoteException;
}
