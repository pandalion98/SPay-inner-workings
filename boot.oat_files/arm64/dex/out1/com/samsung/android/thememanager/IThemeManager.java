package com.samsung.android.thememanager;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
import java.util.Map;

public interface IThemeManager extends IInterface {

    public static abstract class Stub extends Binder implements IThemeManager {
        private static final String DESCRIPTOR = "com.samsung.android.thememanager.IThemeManager";
        static final int TRANSACTION_applyEventTheme = 29;
        static final int TRANSACTION_applyThemePackage = 20;
        static final int TRANSACTION_changeThemeState = 8;
        static final int TRANSACTION_deleteThemePackage = 21;
        static final int TRANSACTION_getActiveComponents = 9;
        static final int TRANSACTION_getActiveFestivalPackage = 28;
        static final int TRANSACTION_getActiveMyEvents = 11;
        static final int TRANSACTION_getCategoryList = 12;
        static final int TRANSACTION_getChineseFestivalList = 3;
        static final int TRANSACTION_getComponentPackageMap = 2;
        static final int TRANSACTION_getCoverAttachStatus = 25;
        static final int TRANSACTION_getCurrentThemePackage = 10;
        static final int TRANSACTION_getCustomData = 32;
        static final int TRANSACTION_getListByCategory = 15;
        static final int TRANSACTION_getPreviousToCoverPackage = 26;
        static final int TRANSACTION_getSpecialEditionThemePackage = 30;
        static final int TRANSACTION_getStateThemePackage = 17;
        static final int TRANSACTION_getThemeDetailsList = 1;
        static final int TRANSACTION_getThemeVersionForMasterPackage = 33;
        static final int TRANSACTION_getThemesForComponent = 18;
        static final int TRANSACTION_getVersionForThemeFramework = 5;
        static final int TRANSACTION_installThemePackage = 4;
        static final int TRANSACTION_isOnTrialMode = 6;
        static final int TRANSACTION_isSupportThemePackage = 35;
        static final int TRANSACTION_isSupportThemeVersion = 34;
        static final int TRANSACTION_isThemePackageExist = 24;
        static final int TRANSACTION_registerStatusListener = 22;
        static final int TRANSACTION_removeThemePackage = 7;
        static final int TRANSACTION_setCustomData = 31;
        static final int TRANSACTION_setDeleteMyEvents = 14;
        static final int TRANSACTION_setFestivalPackage = 27;
        static final int TRANSACTION_setStateThemePackage = 16;
        static final int TRANSACTION_setTimeForMyEvent = 13;
        static final int TRANSACTION_stopTrialThemePackage = 19;
        static final int TRANSACTION_unregisterStatusListener = 23;

        private static class Proxy implements IThemeManager {
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

            public Map getThemeDetailsList(String packageName) throws RemoteException {
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

            public Map getComponentPackageMap() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
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
                    this.mRemote.transact(3, _data, _reply, 0);
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
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getVersionForThemeFramework() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
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

            public boolean changeThemeState(String packageName, int order, boolean isTrial) throws RemoteException {
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

            public String[] getActiveComponents() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCurrentThemePackage() throws RemoteException {
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

            public List<String> getActiveMyEvents() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
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
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    List _result = _reply.readArrayList(getClass().getClassLoader());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTimeForMyEvent(String myEventType, String packageName, String startTime, String repeatSchedule) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(myEventType);
                    _data.writeString(packageName);
                    _data.writeString(startTime);
                    _data.writeString(repeatSchedule);
                    this.mRemote.transact(13, _data, _reply, 0);
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
                    this.mRemote.transact(14, _data, _reply, 0);
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
                    this.mRemote.transact(15, _data, _reply, 0);
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
                    this.mRemote.transact(16, _data, _reply, 0);
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
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getThemesForComponent(String compName, int order) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(compName);
                    _data.writeInt(order);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean stopTrialThemePackage() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
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

            public boolean applyThemePackage(String packageName, boolean isTrial) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (isTrial) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
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

            public void deleteThemePackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerStatusListener(IStatusListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterStatusListener(IStatusListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
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
                    this.mRemote.transact(24, _data, _reply, 0);
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

            public boolean getCoverAttachStatus(String packageName) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(25, _data, _reply, 0);
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

            public String getPreviousToCoverPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
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
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
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
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void applyEventTheme(String packageName, int index, String category) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(index);
                    _data.writeString(category);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSpecialEditionThemePackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCustomData(String packageName, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getCustomData(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Bundle _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
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

            public String getThemeVersionForMasterPackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSupportThemeVersion(int versionCode) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(versionCode);
                    this.mRemote.transact(34, _data, _reply, 0);
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

            public boolean isSupportThemePackage(String packageName) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(35, _data, _reply, 0);
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

        public static IThemeManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IThemeManager)) {
                return new Proxy(obj);
            }
            return (IThemeManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            Map _result;
            List _result2;
            boolean _arg1;
            String _result3;
            boolean _result4;
            String _arg0;
            List<String> _result5;
            int _result6;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getThemeDetailsList(data.readString());
                    reply.writeNoException();
                    reply.writeMap(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getComponentPackageMap();
                    reply.writeNoException();
                    reply.writeMap(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getChineseFestivalList();
                    reply.writeNoException();
                    reply.writeList(_result2);
                    return true;
                case 4:
                    Uri _arg02;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (Uri) Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    installThemePackage(_arg02, _arg1);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getVersionForThemeFramework();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isOnTrialMode(data.readString());
                    reply.writeNoException();
                    if (_result4) {
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
                    _arg0 = data.readString();
                    int _arg12 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    } else {
                        _arg2 = false;
                    }
                    _result4 = changeThemeState(_arg0, _arg12, _arg2);
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result7 = getActiveComponents();
                    reply.writeNoException();
                    reply.writeStringArray(_result7);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getCurrentThemePackage();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getActiveMyEvents();
                    reply.writeNoException();
                    reply.writeStringList(_result5);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getCategoryList();
                    reply.writeNoException();
                    reply.writeList(_result2);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    setTimeForMyEvent(data.readString(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    setDeleteMyEvents(data.createStringArrayList(), data.readString());
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getListByCategory(data.readInt());
                    reply.writeNoException();
                    reply.writeList(_result2);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = setStateThemePackage(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getStateThemePackage(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getThemesForComponent(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeStringList(_result5);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = stopTrialThemePackage();
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    _result4 = applyThemePackage(_arg0, _arg1);
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    deleteThemePackage(data.readString());
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    registerStatusListener(com.samsung.android.thememanager.IStatusListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterStatusListener(com.samsung.android.thememanager.IStatusListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isThemePackageExist(data.readString());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getCoverAttachStatus(data.readString());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getPreviousToCoverPackage();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    setFestivalPackage(data.readString());
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getActiveFestivalPackage();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    applyEventTheme(data.readString(), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getSpecialEditionThemePackage();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 31:
                    Bundle _arg13;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg13 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    setCustomData(_arg0, _arg13);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    Bundle _result8 = getCustomData(data.readString());
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getThemeVersionForMasterPackage(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isSupportThemeVersion(data.readInt());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isSupportThemePackage(data.readString());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void applyEventTheme(String str, int i, String str2) throws RemoteException;

    boolean applyThemePackage(String str, boolean z) throws RemoteException;

    boolean changeThemeState(String str, int i, boolean z) throws RemoteException;

    void deleteThemePackage(String str) throws RemoteException;

    String[] getActiveComponents() throws RemoteException;

    String getActiveFestivalPackage() throws RemoteException;

    List<String> getActiveMyEvents() throws RemoteException;

    List getCategoryList() throws RemoteException;

    List getChineseFestivalList() throws RemoteException;

    Map getComponentPackageMap() throws RemoteException;

    boolean getCoverAttachStatus(String str) throws RemoteException;

    String getCurrentThemePackage() throws RemoteException;

    Bundle getCustomData(String str) throws RemoteException;

    List getListByCategory(int i) throws RemoteException;

    String getPreviousToCoverPackage() throws RemoteException;

    String getSpecialEditionThemePackage() throws RemoteException;

    int getStateThemePackage(String str) throws RemoteException;

    Map getThemeDetailsList(String str) throws RemoteException;

    String getThemeVersionForMasterPackage(String str) throws RemoteException;

    List<String> getThemesForComponent(String str, int i) throws RemoteException;

    String getVersionForThemeFramework() throws RemoteException;

    void installThemePackage(Uri uri, boolean z) throws RemoteException;

    boolean isOnTrialMode(String str) throws RemoteException;

    boolean isSupportThemePackage(String str) throws RemoteException;

    boolean isSupportThemeVersion(int i) throws RemoteException;

    boolean isThemePackageExist(String str) throws RemoteException;

    void registerStatusListener(IStatusListener iStatusListener) throws RemoteException;

    void removeThemePackage(String str) throws RemoteException;

    void setCustomData(String str, Bundle bundle) throws RemoteException;

    void setDeleteMyEvents(List<String> list, String str) throws RemoteException;

    void setFestivalPackage(String str) throws RemoteException;

    int setStateThemePackage(String str, int i) throws RemoteException;

    void setTimeForMyEvent(String str, String str2, String str3, String str4) throws RemoteException;

    boolean stopTrialThemePackage() throws RemoteException;

    void unregisterStatusListener(IStatusListener iStatusListener) throws RemoteException;
}
