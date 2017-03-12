package com.samsung.android.writingbuddy;

import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;

public interface IWritingBuddyManager extends IInterface {

    public static abstract class Stub extends Binder implements IWritingBuddyManager {
        private static final String DESCRIPTOR = "com.samsung.android.writingbuddy.IWritingBuddyManager";
        static final int TRANSACTION_dismiss = 3;
        static final int TRANSACTION_getImage = 6;
        static final int TRANSACTION_isShowing = 7;
        static final int TRANSACTION_show = 1;
        static final int TRANSACTION_showPopup = 8;
        static final int TRANSACTION_showTemplate = 2;
        static final int TRANSACTION_updateDialog = 5;
        static final int TRANSACTION_updatePosition = 4;

        private static class Proxy implements IWritingBuddyManager {
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

            public void show(int clientId, IBinder client, IBinder appToken, IBinder windowToken, Rect windowRect, Rect screenRect, Rect aniInitRect, ExtractedText et, EditorInfo ei, int flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeStrongBinder(client);
                    _data.writeStrongBinder(appToken);
                    _data.writeStrongBinder(windowToken);
                    if (windowRect != null) {
                        _data.writeInt(1);
                        windowRect.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (screenRect != null) {
                        _data.writeInt(1);
                        screenRect.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (aniInitRect != null) {
                        _data.writeInt(1);
                        aniInitRect.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (et != null) {
                        _data.writeInt(1);
                        et.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (ei != null) {
                        _data.writeInt(1);
                        ei.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flag);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showTemplate(int clientId, IBinder client, IBinder appToken, IBinder windowToken, Rect windowRect, Rect screenRect, Rect aniInitRect, int template, ExtractedText et, EditorInfo ei, int flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeStrongBinder(client);
                    _data.writeStrongBinder(appToken);
                    _data.writeStrongBinder(windowToken);
                    if (windowRect != null) {
                        _data.writeInt(1);
                        windowRect.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (screenRect != null) {
                        _data.writeInt(1);
                        screenRect.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (aniInitRect != null) {
                        _data.writeInt(1);
                        aniInitRect.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(template);
                    if (et != null) {
                        _data.writeInt(1);
                        et.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (ei != null) {
                        _data.writeInt(1);
                        ei.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flag);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dismiss(int clientId, boolean immediate) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    if (immediate) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updatePosition(int clientId, Rect windowRect, Rect screenRect) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    if (windowRect != null) {
                        _data.writeInt(1);
                        windowRect.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (screenRect != null) {
                        _data.writeInt(1);
                        screenRect.writeToParcel(_data, 0);
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

            public void updateDialog(int clientId, Rect windowRect, Rect screenRect) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    if (windowRect != null) {
                        _data.writeInt(1);
                        windowRect.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (screenRect != null) {
                        _data.writeInt(1);
                        screenRect.writeToParcel(_data, 0);
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

            public ParcelFileDescriptor getImage(int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    ParcelFileDescriptor _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
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

            public boolean isShowing() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
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

            public void showPopup(int clientId, int style) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeInt(style);
                    this.mRemote.transact(8, _data, _reply, 0);
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

        public static IWritingBuddyManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWritingBuddyManager)) {
                return new Proxy(obj);
            }
            return (IWritingBuddyManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _arg0;
            IBinder _arg1;
            IBinder _arg2;
            IBinder _arg3;
            Rect _arg4;
            Rect _arg5;
            Rect _arg6;
            Rect _arg12;
            Rect _arg22;
            switch (code) {
                case 1:
                    ExtractedText _arg7;
                    EditorInfo _arg8;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readStrongBinder();
                    _arg2 = data.readStrongBinder();
                    _arg3 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg4 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg5 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg5 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg6 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg7 = (ExtractedText) ExtractedText.CREATOR.createFromParcel(data);
                    } else {
                        _arg7 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg8 = (EditorInfo) EditorInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg8 = null;
                    }
                    show(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, data.readInt());
                    reply.writeNoException();
                    return true;
                case 2:
                    ExtractedText _arg82;
                    EditorInfo _arg9;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readStrongBinder();
                    _arg2 = data.readStrongBinder();
                    _arg3 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg4 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg5 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg5 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg6 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    int _arg72 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg82 = (ExtractedText) ExtractedText.CREATOR.createFromParcel(data);
                    } else {
                        _arg82 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg9 = (EditorInfo) EditorInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg9 = null;
                    }
                    showTemplate(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg72, _arg82, _arg9, data.readInt());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    dismiss(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg22 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    updatePosition(_arg0, _arg12, _arg22);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg22 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    updateDialog(_arg0, _arg12, _arg22);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelFileDescriptor _result = getImage(data.readInt());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result2 = isShowing();
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    showPopup(data.readInt(), data.readInt());
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

    void dismiss(int i, boolean z) throws RemoteException;

    ParcelFileDescriptor getImage(int i) throws RemoteException;

    boolean isShowing() throws RemoteException;

    void show(int i, IBinder iBinder, IBinder iBinder2, IBinder iBinder3, Rect rect, Rect rect2, Rect rect3, ExtractedText extractedText, EditorInfo editorInfo, int i2) throws RemoteException;

    void showPopup(int i, int i2) throws RemoteException;

    void showTemplate(int i, IBinder iBinder, IBinder iBinder2, IBinder iBinder3, Rect rect, Rect rect2, Rect rect3, int i2, ExtractedText extractedText, EditorInfo editorInfo, int i3) throws RemoteException;

    void updateDialog(int i, Rect rect, Rect rect2) throws RemoteException;

    void updatePosition(int i, Rect rect, Rect rect2) throws RemoteException;
}
