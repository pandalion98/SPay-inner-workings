package android.content;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IRCPInterface extends IInterface {

    public static abstract class Stub extends Binder implements IRCPInterface {
        private static final String DESCRIPTOR = "android.content.IRCPInterface";
        static final int TRANSACTION_cancel = 12;
        static final int TRANSACTION_cancelCopyChunks = 11;
        static final int TRANSACTION_copyChunks = 10;
        static final int TRANSACTION_copyFile = 4;
        static final int TRANSACTION_copyFiles = 1;
        static final int TRANSACTION_getErrorMessage = 6;
        static final int TRANSACTION_getFileInfo = 9;
        static final int TRANSACTION_getFiles = 8;
        static final int TRANSACTION_isFileExist = 7;
        static final int TRANSACTION_moveFile = 5;
        static final int TRANSACTION_moveFiles = 2;
        static final int TRANSACTION_moveFilesForApp = 3;

        private static class Proxy implements IRCPInterface {
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

            public long copyFiles(int srcContainerId, List<String> srcFilePaths, int destContainerId, List<String> destFilePaths, IRCPInterfaceCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(srcContainerId);
                    _data.writeStringList(srcFilePaths);
                    _data.writeInt(destContainerId);
                    _data.writeStringList(destFilePaths);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long moveFiles(int srcContainerId, List<String> srcFilePaths, int destContainerId, List<String> destFilePaths, IRCPInterfaceCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(srcContainerId);
                    _data.writeStringList(srcFilePaths);
                    _data.writeInt(destContainerId);
                    _data.writeStringList(destFilePaths);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
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
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
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
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
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
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getErrorMessage(int errorId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(errorId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
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

            public List<String> getFiles(String path, int containerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeInt(containerId);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
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
                    this.mRemote.transact(9, _data, _reply, 0);
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
                    this.mRemote.transact(10, _data, _reply, 0);
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
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancel(long threadId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(threadId);
                    this.mRemote.transact(12, _data, _reply, 0);
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

        public static IRCPInterface asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRCPInterface)) {
                return new Proxy(obj);
            }
            return (IRCPInterface) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            long _result;
            int _result2;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = copyFiles(data.readInt(), data.createStringArrayList(), data.readInt(), data.createStringArrayList(), android.content.IRCPInterfaceCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeLong(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = moveFiles(data.readInt(), data.createStringArrayList(), data.readInt(), data.createStringArrayList(), android.content.IRCPInterfaceCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeLong(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result = moveFilesForApp(data.readInt(), data.createStringArrayList(), data.createStringArrayList());
                    reply.writeNoException();
                    reply.writeLong(_result);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = copyFile(data.readInt(), data.readString(), data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = moveFile(data.readInt(), data.readString(), data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _result3 = getErrorMessage(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result4 = isFileExist(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result5 = getFiles(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeStringList(_result5);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    Bundle _result6 = getFileInfo(data.readString(), data.readInt());
                    reply.writeNoException();
                    if (_result6 != null) {
                        reply.writeInt(1);
                        _result6.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = copyChunks(data.readInt(), data.readString(), data.readInt(), data.readString(), data.readLong(), data.readInt(), data.readLong(), data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    cancelCopyChunks(data.readLong());
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    cancel(data.readLong());
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

    void cancel(long j) throws RemoteException;

    void cancelCopyChunks(long j) throws RemoteException;

    int copyChunks(int i, String str, int i2, String str2, long j, int i3, long j2, boolean z) throws RemoteException;

    int copyFile(int i, String str, int i2, String str2) throws RemoteException;

    long copyFiles(int i, List<String> list, int i2, List<String> list2, IRCPInterfaceCallback iRCPInterfaceCallback) throws RemoteException;

    String getErrorMessage(int i) throws RemoteException;

    Bundle getFileInfo(String str, int i) throws RemoteException;

    List<String> getFiles(String str, int i) throws RemoteException;

    boolean isFileExist(String str, int i) throws RemoteException;

    int moveFile(int i, String str, int i2, String str2) throws RemoteException;

    long moveFiles(int i, List<String> list, int i2, List<String> list2, IRCPInterfaceCallback iRCPInterfaceCallback) throws RemoteException;

    long moveFilesForApp(int i, List<String> list, List<String> list2) throws RemoteException;
}
