package android.sec.clipboard;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.sec.clipboard.data.ClipboardData;
import android.text.TextUtils;
import java.util.List;

public interface IClipboardService extends IInterface {

    public static abstract class Stub extends Binder implements IClipboardService {
        private static final String DESCRIPTOR = "android.sec.clipboard.IClipboardService";
        static final int TRANSACTION_AddClipboardFormatListener = 1;
        static final int TRANSACTION_GetClipboardData = 4;
        static final int TRANSACTION_GetClipboardFormatName = 3;
        static final int TRANSACTION_IsShowUIClipboardData = 6;
        static final int TRANSACTION_ObserverUpdate = 7;
        static final int TRANSACTION_RegistClipboardWorkingFormUiInterface = 26;
        static final int TRANSACTION_RegistClipboardWorkingFormUiInterfaces = 11;
        static final int TRANSACTION_RegisterClipboardFormat = 8;
        static final int TRANSACTION_RemoveClipboardFormatListener = 9;
        static final int TRANSACTION_SetClipboardData = 13;
        static final int TRANSACTION_SetClipboardDataOriginalToEx = 14;
        static final int TRANSACTION_SetClipboardDataWithoutSendingOrginalClipboard = 20;
        static final int TRANSACTION_SetSyncClipboardData = 23;
        static final int TRANSACTION_ShowUIClipboardData = 15;
        static final int TRANSACTION_UpdateClipboardDB = 24;
        static final int TRANSACTION_UpdateUIClipboardData = 16;
        static final int TRANSACTION_callPasteMenuFromApp = 19;
        static final int TRANSACTION_dismissUIDataDialog = 2;
        static final int TRANSACTION_getClipedStrings = 17;
        static final int TRANSACTION_getDataSize = 5;
        static final int TRANSACTION_isEnabled = 21;
        static final int TRANSACTION_iscalledPasteMenuFromApp = 18;
        static final int TRANSACTION_setData = 22;
        static final int TRANSACTION_showUIDataDialog = 10;
        static final int TRANSACTION_unRegistClipboardWorkingFormUiInterface = 25;
        static final int TRANSACTION_unRegistClipboardWorkingFormUiInterfaces = 12;

        private static class Proxy implements IClipboardService {
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

            public boolean AddClipboardFormatListener(IClipboardFormatListener listener) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
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

            public void dismissUIDataDialog() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String GetClipboardFormatName(int format) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(format);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ClipboardData GetClipboardData(int format) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    ClipboardData _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(format);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (ClipboardData) ClipboardData.CREATOR.createFromParcel(_reply);
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

            public int getDataSize() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean IsShowUIClipboardData() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
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

            public void ObserverUpdate(int format, ClipboardData data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(format);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
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

            public int RegisterClipboardFormat(String formatName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(formatName);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean RemoveClipboardFormatListener(IClipboardFormatListener listener) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(9, _data, _reply, 0);
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

            public void showUIDataDialog() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void RegistClipboardWorkingFormUiInterfaces(IClipboardWorkingFormUiInterface iRegInterface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(iRegInterface != null ? iRegInterface.asBinder() : null);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unRegistClipboardWorkingFormUiInterfaces(IClipboardWorkingFormUiInterface iRegInterface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(iRegInterface != null ? iRegInterface.asBinder() : null);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean SetClipboardData(int format, ClipboardData data, String callingPackage) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(format);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    this.mRemote.transact(13, _data, _reply, 0);
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

            public boolean SetClipboardDataOriginalToEx(int format, ClipboardData data) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(format);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, _reply, 0);
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

            public void ShowUIClipboardData(int format, IClipboardDataPasteEvent clPasteEvent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(format);
                    _data.writeStrongBinder(clPasteEvent != null ? clPasteEvent.asBinder() : null);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void UpdateUIClipboardData(int format, IClipboardDataPasteEvent clPasteEvent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(format);
                    _data.writeStrongBinder(clPasteEvent != null ? clPasteEvent.asBinder() : null);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getClipedStrings(int metaType, int maxCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(metaType);
                    _data.writeInt(maxCount);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean iscalledPasteMenuFromApp() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
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

            public void callPasteMenuFromApp(int enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean SetClipboardDataWithoutSendingOrginalClipboard(int format, ClipboardData data, String callingPackage) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(format);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    this.mRemote.transact(20, _data, _reply, 0);
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

            public boolean isEnabled() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
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

            public boolean setData(int format, ClipboardData data, boolean isCallFromGED) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(format);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(isCallFromGED ? 1 : 0);
                    this.mRemote.transact(22, _data, _reply, 0);
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

            public boolean SetSyncClipboardData(CharSequence text) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (text != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(text, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(23, _data, _reply, 0);
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

            public void UpdateClipboardDB(int format) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(format);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unRegistClipboardWorkingFormUiInterface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void RegistClipboardWorkingFormUiInterface(IClipboardWorkingFormUiInterface iRegInterface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(iRegInterface != null ? iRegInterface.asBinder() : null);
                    this.mRemote.transact(26, _data, _reply, 0);
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

        public static IClipboardService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IClipboardService)) {
                return new Proxy(obj);
            }
            return (IClipboardService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            boolean _result;
            int _result2;
            int _arg0;
            ClipboardData _arg1;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = AddClipboardFormatListener(android.sec.clipboard.IClipboardFormatListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    dismissUIDataDialog();
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _result3 = GetClipboardFormatName(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    ClipboardData _result4 = GetClipboardData(data.readInt());
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getDataSize();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result = IsShowUIClipboardData();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (ClipboardData) ClipboardData.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    ObserverUpdate(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = RegisterClipboardFormat(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _result = RemoveClipboardFormatListener(android.sec.clipboard.IClipboardFormatListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    showUIDataDialog();
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    RegistClipboardWorkingFormUiInterfaces(android.sec.clipboard.IClipboardWorkingFormUiInterface.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    unRegistClipboardWorkingFormUiInterfaces(android.sec.clipboard.IClipboardWorkingFormUiInterface.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (ClipboardData) ClipboardData.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    _result = SetClipboardData(_arg0, _arg1, data.readString());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (ClipboardData) ClipboardData.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    _result = SetClipboardDataOriginalToEx(_arg0, _arg1);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    ShowUIClipboardData(data.readInt(), android.sec.clipboard.IClipboardDataPasteEvent.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    UpdateUIClipboardData(data.readInt(), android.sec.clipboard.IClipboardDataPasteEvent.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result5 = getClipedStrings(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeStringList(_result5);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _result = iscalledPasteMenuFromApp();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    callPasteMenuFromApp(data.readInt());
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (ClipboardData) ClipboardData.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    _result = SetClipboardDataWithoutSendingOrginalClipboard(_arg0, _arg1, data.readString());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isEnabled();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 22:
                    boolean _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (ClipboardData) ClipboardData.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    } else {
                        _arg2 = false;
                    }
                    _result = setData(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 23:
                    CharSequence _arg02;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    _result = SetSyncClipboardData(_arg02);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    UpdateClipboardDB(data.readInt());
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    unRegistClipboardWorkingFormUiInterface();
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    RegistClipboardWorkingFormUiInterface(android.sec.clipboard.IClipboardWorkingFormUiInterface.Stub.asInterface(data.readStrongBinder()));
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

    boolean AddClipboardFormatListener(IClipboardFormatListener iClipboardFormatListener) throws RemoteException;

    ClipboardData GetClipboardData(int i) throws RemoteException;

    String GetClipboardFormatName(int i) throws RemoteException;

    boolean IsShowUIClipboardData() throws RemoteException;

    void ObserverUpdate(int i, ClipboardData clipboardData) throws RemoteException;

    void RegistClipboardWorkingFormUiInterface(IClipboardWorkingFormUiInterface iClipboardWorkingFormUiInterface) throws RemoteException;

    void RegistClipboardWorkingFormUiInterfaces(IClipboardWorkingFormUiInterface iClipboardWorkingFormUiInterface) throws RemoteException;

    int RegisterClipboardFormat(String str) throws RemoteException;

    boolean RemoveClipboardFormatListener(IClipboardFormatListener iClipboardFormatListener) throws RemoteException;

    boolean SetClipboardData(int i, ClipboardData clipboardData, String str) throws RemoteException;

    boolean SetClipboardDataOriginalToEx(int i, ClipboardData clipboardData) throws RemoteException;

    boolean SetClipboardDataWithoutSendingOrginalClipboard(int i, ClipboardData clipboardData, String str) throws RemoteException;

    boolean SetSyncClipboardData(CharSequence charSequence) throws RemoteException;

    void ShowUIClipboardData(int i, IClipboardDataPasteEvent iClipboardDataPasteEvent) throws RemoteException;

    void UpdateClipboardDB(int i) throws RemoteException;

    void UpdateUIClipboardData(int i, IClipboardDataPasteEvent iClipboardDataPasteEvent) throws RemoteException;

    void callPasteMenuFromApp(int i) throws RemoteException;

    void dismissUIDataDialog() throws RemoteException;

    List<String> getClipedStrings(int i, int i2) throws RemoteException;

    int getDataSize() throws RemoteException;

    boolean isEnabled() throws RemoteException;

    boolean iscalledPasteMenuFromApp() throws RemoteException;

    boolean setData(int i, ClipboardData clipboardData, boolean z) throws RemoteException;

    void showUIDataDialog() throws RemoteException;

    void unRegistClipboardWorkingFormUiInterface() throws RemoteException;

    void unRegistClipboardWorkingFormUiInterfaces(IClipboardWorkingFormUiInterface iClipboardWorkingFormUiInterface) throws RemoteException;
}
