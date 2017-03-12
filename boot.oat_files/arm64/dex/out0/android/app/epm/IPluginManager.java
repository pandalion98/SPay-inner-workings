package android.app.epm;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
import java.util.Map;

public interface IPluginManager extends IInterface {

    public static abstract class Stub extends Binder implements IPluginManager {
        private static final String DESCRIPTOR = "android.app.epm.IPluginManager";
        static final int TRANSACTION_changePluginState = 8;
        static final int TRANSACTION_getActiveComponents = 11;
        static final int TRANSACTION_getActiveFestivalPackage = 12;
        static final int TRANSACTION_getActiveMyEvents = 15;
        static final int TRANSACTION_getCategoryList = 17;
        static final int TRANSACTION_getChineseFestivalList = 4;
        static final int TRANSACTION_getComponentPackageMap = 3;
        static final int TRANSACTION_getCoverAttachStatus = 13;
        static final int TRANSACTION_getCurrentThemePackage = 14;
        static final int TRANSACTION_getLanguagePackList = 2;
        static final int TRANSACTION_getListByCategory = 20;
        static final int TRANSACTION_getPluginDetailsList = 1;
        static final int TRANSACTION_getPreviousToCoverPackage = 10;
        static final int TRANSACTION_getStateThemePackage = 22;
        static final int TRANSACTION_installThemePackage = 5;
        static final int TRANSACTION_isOnTrialMode = 6;
        static final int TRANSACTION_isThemePackageExist = 23;
        static final int TRANSACTION_registerCallback = 16;
        static final int TRANSACTION_removeThemePackage = 7;
        static final int TRANSACTION_setDeleteMyEvents = 19;
        static final int TRANSACTION_setFestivalPackage = 9;
        static final int TRANSACTION_setStateThemePackage = 21;
        static final int TRANSACTION_setTimeForMyEvent = 18;

        private static class Proxy implements IPluginManager {
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

            public Map getPluginDetailsList(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    Map _result = _reply.readHashMap(getClass().getClassLoader());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Map getLanguagePackList(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    Map _result = _reply.readHashMap(getClass().getClassLoader());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Map getComponentPackageMap() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    Map _result = _reply.readHashMap(getClass().getClassLoader());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List getChineseFestivalList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    List _result = _reply.readArrayList(getClass().getClassLoader());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void installThemePackage(Uri path, boolean isTrial) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (path != null) {
                        _data.writeInt(1);
                        path.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!isTrial) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isOnTrialMode(String packageName) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
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

            public void removeThemePackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean changePluginState(String packageName, int order, boolean isTrial) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(order);
                    if (isTrial) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(8, _data, _reply, 0);
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

            public void setFestivalPackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getPreviousToCoverPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getActiveComponents() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getActiveFestivalPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getCoverAttachStatus(String packageName) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(13, _data, _reply, 0);
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

            public String getCurrentThemePackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getActiveMyEvents() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerCallback(IPluginManagerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List getCategoryList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    List _result = _reply.readArrayList(getClass().getClassLoader());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTimeForMyEvent(String myEventType, String packageName, String startTime, String repeatSchedule, String myEventTitle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(myEventType);
                    _data.writeString(packageName);
                    _data.writeString(startTime);
                    _data.writeString(repeatSchedule);
                    _data.writeString(myEventTitle);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDeleteMyEvents(List<String> myEventType, String action) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(myEventType);
                    _data.writeString(action);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List getListByCategory(int category) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(category);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    List _result = _reply.readArrayList(getClass().getClassLoader());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setStateThemePackage(String packageName, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(state);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getStateThemePackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isThemePackageExist(String packageName) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(23, _data, _reply, 0);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPluginManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPluginManager)) {
                return new Proxy(obj);
            }
            return (IPluginManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            Map _result;
            List _result2;
            boolean _result3;
            String _result4;
            int _result5;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getPluginDetailsList(data.readString());
                    reply.writeNoException();
                    reply.writeMap(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getLanguagePackList(data.readString());
                    reply.writeNoException();
                    reply.writeMap(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getComponentPackageMap();
                    reply.writeNoException();
                    reply.writeMap(_result);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getChineseFestivalList();
                    reply.writeNoException();
                    reply.writeList(_result2);
                    return true;
                case 5:
                    Uri _arg0;
                    boolean _arg1;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Uri) Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    installThemePackage(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isOnTrialMode(data.readString());
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    removeThemePackage(data.readString());
                    reply.writeNoException();
                    return true;
                case 8:
                    boolean _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    int _arg12 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    } else {
                        _arg2 = false;
                    }
                    _result3 = changePluginState(_arg02, _arg12, _arg2);
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    setFestivalPackage(data.readString());
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getPreviousToCoverPackage();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result6 = getActiveComponents();
                    reply.writeNoException();
                    reply.writeStringArray(_result6);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getActiveFestivalPackage();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getCoverAttachStatus(data.readString());
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getCurrentThemePackage();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result7 = getActiveMyEvents();
                    reply.writeNoException();
                    reply.writeStringList(_result7);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    registerCallback(android.app.epm.IPluginManagerCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getCategoryList();
                    reply.writeNoException();
                    reply.writeList(_result2);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    setTimeForMyEvent(data.readString(), data.readString(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    setDeleteMyEvents(data.createStringArrayList(), data.readString());
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getListByCategory(data.readInt());
                    reply.writeNoException();
                    reply.writeList(_result2);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = setStateThemePackage(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getStateThemePackage(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isThemePackageExist(data.readString());
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    boolean changePluginState(String str, int i, boolean z) throws RemoteException;

    String[] getActiveComponents() throws RemoteException;

    String getActiveFestivalPackage() throws RemoteException;

    List<String> getActiveMyEvents() throws RemoteException;

    List getCategoryList() throws RemoteException;

    List getChineseFestivalList() throws RemoteException;

    Map getComponentPackageMap() throws RemoteException;

    boolean getCoverAttachStatus(String str) throws RemoteException;

    String getCurrentThemePackage() throws RemoteException;

    Map getLanguagePackList(String str) throws RemoteException;

    List getListByCategory(int i) throws RemoteException;

    Map getPluginDetailsList(String str) throws RemoteException;

    String getPreviousToCoverPackage() throws RemoteException;

    int getStateThemePackage(String str) throws RemoteException;

    void installThemePackage(Uri uri, boolean z) throws RemoteException;

    boolean isOnTrialMode(String str) throws RemoteException;

    boolean isThemePackageExist(String str) throws RemoteException;

    void registerCallback(IPluginManagerCallback iPluginManagerCallback) throws RemoteException;

    void removeThemePackage(String str) throws RemoteException;

    void setDeleteMyEvents(List<String> list, String str) throws RemoteException;

    void setFestivalPackage(String str) throws RemoteException;

    int setStateThemePackage(String str, int i) throws RemoteException;

    void setTimeForMyEvent(String str, String str2, String str3, String str4, String str5) throws RemoteException;
}
