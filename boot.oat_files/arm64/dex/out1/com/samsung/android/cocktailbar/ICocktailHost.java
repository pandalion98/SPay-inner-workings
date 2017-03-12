package com.samsung.android.cocktailbar;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.RemoteViews;

public interface ICocktailHost extends IInterface {

    public static abstract class Stub extends Binder implements ICocktailHost {
        private static final String DESCRIPTOR = "com.samsung.android.cocktailbar.ICocktailHost";
        static final int TRANSACTION_changeVisibleEdgeService = 13;
        static final int TRANSACTION_closeContextualCocktail = 5;
        static final int TRANSACTION_notifyKeyguardState = 8;
        static final int TRANSACTION_notifyWakeUpState = 9;
        static final int TRANSACTION_partiallyUpdateCocktail = 2;
        static final int TRANSACTION_removeCocktail = 3;
        static final int TRANSACTION_sendExtraData = 11;
        static final int TRANSACTION_setDisableTickerView = 12;
        static final int TRANSACTION_showCocktail = 4;
        static final int TRANSACTION_switchDefaultCocktail = 10;
        static final int TRANSACTION_updateCocktail = 1;
        static final int TRANSACTION_updateToolLauncher = 7;
        static final int TRANSACTION_viewDataChanged = 6;

        private static class Proxy implements ICocktailHost {
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

            public void updateCocktail(int cocktailId, Cocktail cocktail, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cocktailId);
                    if (cocktail != null) {
                        _data.writeInt(1);
                        cocktail.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void partiallyUpdateCocktail(int cocktailId, RemoteViews contentView, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cocktailId);
                    if (contentView != null) {
                        _data.writeInt(1);
                        contentView.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeCocktail(int cocktailId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cocktailId);
                    _data.writeInt(userId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showCocktail(int cocktailId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cocktailId);
                    _data.writeInt(userId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void closeContextualCocktail(int cocktailId, int category, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cocktailId);
                    _data.writeInt(category);
                    _data.writeInt(userId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void viewDataChanged(int cocktailId, int viewId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cocktailId);
                    _data.writeInt(viewId);
                    _data.writeInt(userId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateToolLauncher(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyKeyguardState(boolean enable, int userId) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(userId);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyWakeUpState(boolean bEnable, int keyCode, int userId) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bEnable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(keyCode);
                    _data.writeInt(userId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void switchDefaultCocktail(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendExtraData(int userId, Bundle extraData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (extraData != null) {
                        _data.writeInt(1);
                        extraData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDisableTickerView(int state, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    _data.writeInt(userId);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void changeVisibleEdgeService(boolean visible, int userId) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (visible) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(userId);
                    this.mRemote.transact(13, _data, _reply, 0);
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

        public static ICocktailHost asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICocktailHost)) {
                return new Proxy(obj);
            }
            return (ICocktailHost) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0 = false;
            int _arg02;
            switch (code) {
                case 1:
                    Cocktail _arg1;
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (Cocktail) Cocktail.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    updateCocktail(_arg02, _arg1, data.readInt());
                    reply.writeNoException();
                    return true;
                case 2:
                    RemoteViews _arg12;
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = (RemoteViews) RemoteViews.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    partiallyUpdateCocktail(_arg02, _arg12, data.readInt());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    removeCocktail(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    showCocktail(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    closeContextualCocktail(data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    viewDataChanged(data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    updateToolLauncher(data.readInt());
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    }
                    notifyKeyguardState(_arg0, data.readInt());
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    }
                    notifyWakeUpState(_arg0, data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    switchDefaultCocktail(data.readInt());
                    reply.writeNoException();
                    return true;
                case 11:
                    Bundle _arg13;
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg13 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    sendExtraData(_arg02, _arg13);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    setDisableTickerView(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    }
                    changeVisibleEdgeService(_arg0, data.readInt());
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

    void changeVisibleEdgeService(boolean z, int i) throws RemoteException;

    void closeContextualCocktail(int i, int i2, int i3) throws RemoteException;

    void notifyKeyguardState(boolean z, int i) throws RemoteException;

    void notifyWakeUpState(boolean z, int i, int i2) throws RemoteException;

    void partiallyUpdateCocktail(int i, RemoteViews remoteViews, int i2) throws RemoteException;

    void removeCocktail(int i, int i2) throws RemoteException;

    void sendExtraData(int i, Bundle bundle) throws RemoteException;

    void setDisableTickerView(int i, int i2) throws RemoteException;

    void showCocktail(int i, int i2) throws RemoteException;

    void switchDefaultCocktail(int i) throws RemoteException;

    void updateCocktail(int i, Cocktail cocktail, int i2) throws RemoteException;

    void updateToolLauncher(int i) throws RemoteException;

    void viewDataChanged(int i, int i2, int i3) throws RemoteException;
}
