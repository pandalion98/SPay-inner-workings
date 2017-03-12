package android.os;

import android.app.Command;
import android.content.CustomCursor;
import android.content.ICommandExeCallBack;
import android.content.IProviderCallBack;
import android.content.IRCPGlobalContactsDir;
import android.content.IRCPInterface;
import android.content.ISyncCallBack;
import android.graphics.Bitmap;
import java.util.List;

public interface IRCPManager extends IInterface {

    public static abstract class Stub extends Binder implements IRCPManager {
        private static final String DESCRIPTOR = "android.os.IRCPManager";
        static final int TRANSACTION_cancelCopyChunks = 23;
        static final int TRANSACTION_changePermissionMigration = 17;
        static final int TRANSACTION_copyChunks = 22;
        static final int TRANSACTION_copyFile = 15;
        static final int TRANSACTION_copyFileInternal = 14;
        static final int TRANSACTION_deleteFile = 20;
        static final int TRANSACTION_doSyncForSyncer = 28;
        static final int TRANSACTION_exchangeData = 25;
        static final int TRANSACTION_executeCommandForPersona = 7;
        static final int TRANSACTION_getCallerInfo = 8;
        static final int TRANSACTION_getFileInfo = 21;
        static final int TRANSACTION_getFiles = 19;
        static final int TRANSACTION_getRCPInterface = 12;
        static final int TRANSACTION_getRCPProxy = 9;
        static final int TRANSACTION_handleShortcut = 27;
        static final int TRANSACTION_isFileExist = 18;
        static final int TRANSACTION_moveFile = 13;
        static final int TRANSACTION_moveFilesForApp = 16;
        static final int TRANSACTION_queryAllProviders = 2;
        static final int TRANSACTION_queryProvider = 1;
        static final int TRANSACTION_registerCommandExe = 5;
        static final int TRANSACTION_registerExchangeData = 24;
        static final int TRANSACTION_registerMonitorCb = 26;
        static final int TRANSACTION_registerObserver = 29;
        static final int TRANSACTION_registerProvider = 3;
        static final int TRANSACTION_registerRCPGlobalContactsDir = 10;
        static final int TRANSACTION_registerRCPInterface = 11;
        static final int TRANSACTION_registerSync = 4;
        static final int TRANSACTION_switchPersona = 6;
        static final int TRANSACTION_unRegisterObserver = 30;

        private static class Proxy implements IRCPManager {
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

            public CustomCursor queryProvider(String providerName, String resource, int containerId, String[] projection, String selection, String[] selectionArgs, String sortOrder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    CustomCursor _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(providerName);
                    _data.writeString(resource);
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

            public List<CustomCursor> queryAllProviders(String providerName, String resource, int containerId, String[] projection, String selection, String[] selectionArgs, String sortOrder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(providerName);
                    _data.writeString(resource);
                    _data.writeInt(containerId);
                    _data.writeStringArray(projection);
                    _data.writeString(selection);
                    _data.writeStringArray(selectionArgs);
                    _data.writeString(sortOrder);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    List<CustomCursor> _result = _reply.createTypedArrayList(CustomCursor.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerProvider(String providerName, IProviderCallBack mProvider, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(providerName);
                    _data.writeStrongBinder(mProvider != null ? mProvider.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerSync(ISyncCallBack callback, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerCommandExe(ICommandExeCallBack callback, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void switchPersona(int id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void executeCommandForPersona(Command command) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (command != null) {
                        _data.writeInt(1);
                        command.writeToParcel(_data, 0);
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

            public CustomCursor getCallerInfo(String contactRefUriAsString) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    CustomCursor _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(contactRefUriAsString);
                    this.mRemote.transact(8, _data, _reply, 0);
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

            public IRCPGlobalContactsDir getRCPProxy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    IRCPGlobalContactsDir _result = android.content.IRCPGlobalContactsDir.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerRCPGlobalContactsDir(IRCPGlobalContactsDir globalContactsDir, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(globalContactsDir != null ? globalContactsDir.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerRCPInterface(IRCPInterface rcpInterface, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(rcpInterface != null ? rcpInterface.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IRCPInterface getRCPInterface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    IRCPInterface _result = android.content.IRCPInterface.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int moveFile(int srcContainerId, String srcFilePath, int destContainerId, String destFilePath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(srcContainerId);
                    _data.writeString(srcFilePath);
                    _data.writeInt(destContainerId);
                    _data.writeString(destFilePath);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int copyFileInternal(int srcContainerId, String srcFilePath, int destContainerId, String destFilePath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(srcContainerId);
                    _data.writeString(srcFilePath);
                    _data.writeInt(destContainerId);
                    _data.writeString(destFilePath);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int copyFile(int srcContainerId, String srcFilePath, int destContainerId, String destFilePath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(srcContainerId);
                    _data.writeString(srcFilePath);
                    _data.writeInt(destContainerId);
                    _data.writeString(destFilePath);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long moveFilesForApp(int requestApp, List<String> srcFilePaths, List<String> destFilePaths) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(requestApp);
                    _data.writeStringList(srcFilePaths);
                    _data.writeStringList(destFilePaths);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int changePermissionMigration(String path, int uid, int gid, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeInt(uid);
                    _data.writeInt(gid);
                    _data.writeInt(mode);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isFileExist(String path, int containerId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeInt(containerId);
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

            public List<String> getFiles(String path, int containerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeInt(containerId);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean deleteFile(String path, int containerId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeInt(containerId);
                    this.mRemote.transact(20, _data, _reply, 0);
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

            public Bundle getFileInfo(String path, int containerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Bundle _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeInt(containerId);
                    this.mRemote.transact(21, _data, _reply, 0);
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

            public int copyChunks(int srcContainerId, String srcFilePath, int destContainerId, String destFilePath, long offset, int length, long sessionId, boolean deleteSrc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(srcContainerId);
                    _data.writeString(srcFilePath);
                    _data.writeInt(destContainerId);
                    _data.writeString(destFilePath);
                    _data.writeLong(offset);
                    _data.writeInt(length);
                    _data.writeLong(sessionId);
                    _data.writeInt(deleteSrc ? 1 : 0);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelCopyChunks(long sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(sessionId);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean registerExchangeData(String pkgName, IRunnableCallback cb, int userId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeInt(userId);
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

            public Bundle exchangeData(String pkgName, int userId, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Bundle _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(userId);
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(25, _data, _reply, 0);
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

            public boolean registerMonitorCb(String pkgName, IRunnableCallback cb) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(26, _data, _reply, 0);
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
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void doSyncForSyncer(String mSyncerName, int mUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(mSyncerName);
                    _data.writeInt(mUserId);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerObserver(String mSyncerName, int mUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(mSyncerName);
                    _data.writeInt(mUserId);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unRegisterObserver(String mSyncerName, int mUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(mSyncerName);
                    _data.writeInt(mUserId);
                    this.mRemote.transact(30, _data, _reply, 0);
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

        public static IRCPManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRCPManager)) {
                return new Proxy(obj);
            }
            return (IRCPManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            CustomCursor _result;
            int _result2;
            boolean _result3;
            Bundle _result4;
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
                    List<CustomCursor> _result5 = queryAllProviders(data.readString(), data.readString(), data.readInt(), data.createStringArray(), data.readString(), data.createStringArray(), data.readString());
                    reply.writeNoException();
                    reply.writeTypedList(_result5);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    registerProvider(data.readString(), android.content.IProviderCallBack.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    registerSync(android.content.ISyncCallBack.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    registerCommandExe(android.content.ICommandExeCallBack.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    switchPersona(data.readInt());
                    reply.writeNoException();
                    return true;
                case 7:
                    Command _arg0;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Command) Command.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    executeCommandForPersona(_arg0);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getCallerInfo(data.readString());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    IRCPGlobalContactsDir _result6 = getRCPProxy();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result6 != null ? _result6.asBinder() : null);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    registerRCPGlobalContactsDir(android.content.IRCPGlobalContactsDir.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    registerRCPInterface(android.content.IRCPInterface.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    IRCPInterface _result7 = getRCPInterface();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result7 != null ? _result7.asBinder() : null);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = moveFile(data.readInt(), data.readString(), data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = copyFileInternal(data.readInt(), data.readString(), data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = copyFile(data.readInt(), data.readString(), data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    long _result8 = moveFilesForApp(data.readInt(), data.createStringArrayList(), data.createStringArrayList());
                    reply.writeNoException();
                    reply.writeLong(_result8);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = changePermissionMigration(data.readString(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isFileExist(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result9 = getFiles(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeStringList(_result9);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = deleteFile(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getFileInfo(data.readString(), data.readInt());
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = copyChunks(data.readInt(), data.readString(), data.readInt(), data.readString(), data.readLong(), data.readInt(), data.readLong(), data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    cancelCopyChunks(data.readLong());
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = registerExchangeData(data.readString(), android.os.IRunnableCallback.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 25:
                    Bundle _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    int _arg1 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    _result4 = exchangeData(_arg02, _arg1, _arg2);
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = registerMonitorCb(data.readString(), android.os.IRunnableCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 27:
                    Bitmap _arg3;
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    String _arg12 = data.readString();
                    String _arg22 = data.readString();
                    if (data.readInt() != 0) {
                        _arg3 = (Bitmap) Bitmap.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    handleShortcut(_arg03, _arg12, _arg22, _arg3, data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    doSyncForSyncer(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    registerObserver(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    unRegisterObserver(data.readString(), data.readInt());
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

    void cancelCopyChunks(long j) throws RemoteException;

    int changePermissionMigration(String str, int i, int i2, int i3) throws RemoteException;

    int copyChunks(int i, String str, int i2, String str2, long j, int i3, long j2, boolean z) throws RemoteException;

    int copyFile(int i, String str, int i2, String str2) throws RemoteException;

    int copyFileInternal(int i, String str, int i2, String str2) throws RemoteException;

    boolean deleteFile(String str, int i) throws RemoteException;

    void doSyncForSyncer(String str, int i) throws RemoteException;

    Bundle exchangeData(String str, int i, Bundle bundle) throws RemoteException;

    void executeCommandForPersona(Command command) throws RemoteException;

    CustomCursor getCallerInfo(String str) throws RemoteException;

    Bundle getFileInfo(String str, int i) throws RemoteException;

    List<String> getFiles(String str, int i) throws RemoteException;

    IRCPInterface getRCPInterface() throws RemoteException;

    IRCPGlobalContactsDir getRCPProxy() throws RemoteException;

    void handleShortcut(int i, String str, String str2, Bitmap bitmap, String str3, String str4) throws RemoteException;

    boolean isFileExist(String str, int i) throws RemoteException;

    int moveFile(int i, String str, int i2, String str2) throws RemoteException;

    long moveFilesForApp(int i, List<String> list, List<String> list2) throws RemoteException;

    List<CustomCursor> queryAllProviders(String str, String str2, int i, String[] strArr, String str3, String[] strArr2, String str4) throws RemoteException;

    CustomCursor queryProvider(String str, String str2, int i, String[] strArr, String str3, String[] strArr2, String str4) throws RemoteException;

    void registerCommandExe(ICommandExeCallBack iCommandExeCallBack, int i) throws RemoteException;

    boolean registerExchangeData(String str, IRunnableCallback iRunnableCallback, int i) throws RemoteException;

    boolean registerMonitorCb(String str, IRunnableCallback iRunnableCallback) throws RemoteException;

    void registerObserver(String str, int i) throws RemoteException;

    void registerProvider(String str, IProviderCallBack iProviderCallBack, int i) throws RemoteException;

    void registerRCPGlobalContactsDir(IRCPGlobalContactsDir iRCPGlobalContactsDir, int i) throws RemoteException;

    void registerRCPInterface(IRCPInterface iRCPInterface, int i) throws RemoteException;

    void registerSync(ISyncCallBack iSyncCallBack, int i) throws RemoteException;

    void switchPersona(int i) throws RemoteException;

    void unRegisterObserver(String str, int i) throws RemoteException;
}
