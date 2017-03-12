package com.samsung.android.fingerprint;

import android.content.ComponentName;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFingerprintManager extends IInterface {

    public static abstract class Stub extends Binder implements IFingerprintManager {
        private static final String DESCRIPTOR = "com.samsung.android.fingerprint.IFingerprintManager";
        static final int TRANSACTION_cancel = 21;
        static final int TRANSACTION_closeTransaction = 29;
        static final int TRANSACTION_enroll = 22;
        static final int TRANSACTION_enrollForMultiUser = 23;
        static final int TRANSACTION_getDaemonVersion = 11;
        static final int TRANSACTION_getEnrollRepeatCount = 41;
        static final int TRANSACTION_getEnrolledFingers = 2;
        static final int TRANSACTION_getFingerprintIdByFinger = 40;
        static final int TRANSACTION_getFingerprintIds = 39;
        static final int TRANSACTION_getIndexName = 33;
        static final int TRANSACTION_getLastImageQuality = 36;
        static final int TRANSACTION_getLastImageQualityMessage = 35;
        static final int TRANSACTION_getSensorInfo = 10;
        static final int TRANSACTION_getUserIdList = 12;
        static final int TRANSACTION_getVersion = 1;
        static final int TRANSACTION_hasPendingCommand = 5;
        static final int TRANSACTION_identify = 24;
        static final int TRANSACTION_identifyForMultiUser = 25;
        static final int TRANSACTION_identifyWithDialog = 27;
        static final int TRANSACTION_isEnrollSession = 38;
        static final int TRANSACTION_isSensorReady = 30;
        static final int TRANSACTION_isSupportFingerprintIds = 42;
        static final int TRANSACTION_isVZWPermissionGranted = 37;
        static final int TRANSACTION_notifyAlternativePasswordBegin = 15;
        static final int TRANSACTION_notifyApplicationState = 18;
        static final int TRANSACTION_notifyEnrollBegin = 6;
        static final int TRANSACTION_notifyEnrollEnd = 7;
        static final int TRANSACTION_openTransaction = 28;
        static final int TRANSACTION_pauseEnroll = 8;
        static final int TRANSACTION_process = 32;
        static final int TRANSACTION_processFIDO = 31;
        static final int TRANSACTION_registerClient = 19;
        static final int TRANSACTION_removeAllEnrolledFingers = 4;
        static final int TRANSACTION_removeEnrolledFinger = 3;
        static final int TRANSACTION_request = 14;
        static final int TRANSACTION_resumeEnroll = 9;
        static final int TRANSACTION_setIndexName = 34;
        static final int TRANSACTION_setPassword = 17;
        static final int TRANSACTION_showIdentifyDialog = 26;
        static final int TRANSACTION_unregisterClient = 20;
        static final int TRANSACTION_verifyPassword = 16;
        static final int TRANSACTION_verifySensorState = 13;

        private static class Proxy implements IFingerprintManager {
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

            public int getVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getEnrolledFingers(String appName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(appName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeEnrolledFinger(int fingerIndex, String ownName) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fingerIndex);
                    _data.writeString(ownName);
                    this.mRemote.transact(3, _data, _reply, 0);
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

            public boolean removeAllEnrolledFingers(String ownName) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(ownName);
                    this.mRemote.transact(4, _data, _reply, 0);
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

            public boolean hasPendingCommand() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
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

            public boolean notifyEnrollBegin() throws RemoteException {
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

            public boolean notifyEnrollEnd() throws RemoteException {
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

            public boolean pauseEnroll() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
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

            public boolean resumeEnroll() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public String getSensorInfo() throws RemoteException {
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

            public String getDaemonVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getUserIdList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int verifySensorState(int cmd, int sId, int opt, int logOpt, int uId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmd);
                    _data.writeInt(sId);
                    _data.writeInt(opt);
                    _data.writeInt(logOpt);
                    _data.writeInt(uId);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int request(int command, int data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(command);
                    _data.writeInt(data);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyAlternativePasswordBegin() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean verifyPassword(IBinder token, String password, String ownName) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(password);
                    _data.writeString(ownName);
                    this.mRemote.transact(16, _data, _reply, 0);
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

            public boolean setPassword(String newPassword, String ownName) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(newPassword);
                    _data.writeString(ownName);
                    this.mRemote.transact(17, _data, _reply, 0);
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

            public void notifyApplicationState(int state, Bundle extInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    if (extInfo != null) {
                        _data.writeInt(1);
                        extInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IBinder registerClient(IFingerprintClient client, Bundle clientSpec) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    if (clientSpec != null) {
                        _data.writeInt(1);
                        clientSpec.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean unregisterClient(IBinder token) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
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

            public int cancel(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int enroll(IBinder token, String perimissionName, int fingerIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(perimissionName);
                    _data.writeInt(fingerIndex);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int enrollForMultiUser(IBinder token, int userId, String perimissionName, int fingerIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(userId);
                    _data.writeString(perimissionName);
                    _data.writeInt(fingerIndex);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int identify(IBinder token, String permissionName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(permissionName);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int identifyForMultiUser(IBinder token, int userId, String permissionName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(userId);
                    _data.writeString(permissionName);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int showIdentifyDialog(IBinder token, ComponentName componentName, String permissionName, boolean password) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(permissionName);
                    if (!password) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int identifyWithDialog(String pkgName, ComponentName componentName, IFingerprintClient client, Bundle attribute) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    if (attribute != null) {
                        _data.writeInt(1);
                        attribute.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean openTransaction(IBinder token) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(28, _data, _reply, 0);
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

            public boolean closeTransaction(IBinder token) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(29, _data, _reply, 0);
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

            public boolean isSensorReady() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, _data, _reply, 0);
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

            public byte[] processFIDO(IBinder token, String packageName, String permissionName, byte[] requestData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(packageName);
                    _data.writeString(permissionName);
                    _data.writeByteArray(requestData);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] process(IBinder token, String appId, byte[] requestData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(appId);
                    _data.writeByteArray(requestData);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getIndexName(int index) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(index);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setIndexName(int index, String name) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(index);
                    _data.writeString(name);
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

            public String getLastImageQualityMessage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLastImageQuality(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isVZWPermissionGranted(String ownName) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(ownName);
                    this.mRemote.transact(37, _data, _reply, 0);
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

            public boolean isEnrollSession() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, _data, _reply, 0);
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

            public String[] getFingerprintIds(String ownName, String uniqueStr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(ownName);
                    _data.writeString(uniqueStr);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getFingerprintIdByFinger(int fingerindex, String ownName, String uniqueStr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fingerindex);
                    _data.writeString(ownName);
                    _data.writeString(uniqueStr);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getEnrollRepeatCount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSupportFingerprintIds() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(42, _data, _reply, 0);
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

        public static IFingerprintManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IFingerprintManager)) {
                return new Proxy(obj);
            }
            return (IFingerprintManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            int _result;
            boolean _result2;
            String _result3;
            String[] _result4;
            Bundle _arg1;
            ComponentName _arg12;
            byte[] _result5;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getVersion();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getEnrolledFingers(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = removeEnrolledFinger(data.readInt(), data.readString());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = removeAllEnrolledFingers(data.readString());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = hasPendingCommand();
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = notifyEnrollBegin();
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = notifyEnrollEnd();
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = pauseEnroll();
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = resumeEnroll();
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getSensorInfo();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getDaemonVersion();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getUserIdList();
                    reply.writeNoException();
                    reply.writeStringArray(_result4);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result = verifySensorState(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _result = request(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    notifyAlternativePasswordBegin();
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = verifyPassword(data.readStrongBinder(), data.readString(), data.readString());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setPassword(data.readString(), data.readString());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    notifyApplicationState(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    IFingerprintClient _arg02 = com.samsung.android.fingerprint.IFingerprintClient.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg1 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    IBinder _result6 = registerClient(_arg02, _arg1);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result6);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = unregisterClient(data.readStrongBinder());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _result = cancel(data.readStrongBinder());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _result = enroll(data.readStrongBinder(), data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    _result = enrollForMultiUser(data.readStrongBinder(), data.readInt(), data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _result = identify(data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _result = identifyForMultiUser(data.readStrongBinder(), data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 26:
                    boolean _arg3;
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg03 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg12 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    String _arg2 = data.readString();
                    if (data.readInt() != 0) {
                        _arg3 = true;
                    } else {
                        _arg3 = false;
                    }
                    _result = showIdentifyDialog(_arg03, _arg12, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 27:
                    Bundle _arg32;
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    if (data.readInt() != 0) {
                        _arg12 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    IFingerprintClient _arg22 = com.samsung.android.fingerprint.IFingerprintClient.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg32 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }
                    _result = identifyWithDialog(_arg04, _arg12, _arg22, _arg32);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = openTransaction(data.readStrongBinder());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = closeTransaction(data.readStrongBinder());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isSensorReady();
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = processFIDO(data.readStrongBinder(), data.readString(), data.readString(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeByteArray(_result5);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = process(data.readStrongBinder(), data.readString(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeByteArray(_result5);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getIndexName(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setIndexName(data.readInt(), data.readString());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getLastImageQualityMessage(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getLastImageQuality(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isVZWPermissionGranted(data.readString());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isEnrollSession();
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getFingerprintIds(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeStringArray(_result4);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getFingerprintIdByFinger(data.readInt(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getEnrollRepeatCount();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isSupportFingerprintIds();
                    reply.writeNoException();
                    if (_result2) {
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

    int cancel(IBinder iBinder) throws RemoteException;

    boolean closeTransaction(IBinder iBinder) throws RemoteException;

    int enroll(IBinder iBinder, String str, int i) throws RemoteException;

    int enrollForMultiUser(IBinder iBinder, int i, String str, int i2) throws RemoteException;

    String getDaemonVersion() throws RemoteException;

    int getEnrollRepeatCount() throws RemoteException;

    int getEnrolledFingers(String str) throws RemoteException;

    String getFingerprintIdByFinger(int i, String str, String str2) throws RemoteException;

    String[] getFingerprintIds(String str, String str2) throws RemoteException;

    String getIndexName(int i) throws RemoteException;

    int getLastImageQuality(String str) throws RemoteException;

    String getLastImageQualityMessage(String str) throws RemoteException;

    String getSensorInfo() throws RemoteException;

    String[] getUserIdList() throws RemoteException;

    int getVersion() throws RemoteException;

    boolean hasPendingCommand() throws RemoteException;

    int identify(IBinder iBinder, String str) throws RemoteException;

    int identifyForMultiUser(IBinder iBinder, int i, String str) throws RemoteException;

    int identifyWithDialog(String str, ComponentName componentName, IFingerprintClient iFingerprintClient, Bundle bundle) throws RemoteException;

    boolean isEnrollSession() throws RemoteException;

    boolean isSensorReady() throws RemoteException;

    boolean isSupportFingerprintIds() throws RemoteException;

    boolean isVZWPermissionGranted(String str) throws RemoteException;

    void notifyAlternativePasswordBegin() throws RemoteException;

    void notifyApplicationState(int i, Bundle bundle) throws RemoteException;

    boolean notifyEnrollBegin() throws RemoteException;

    boolean notifyEnrollEnd() throws RemoteException;

    boolean openTransaction(IBinder iBinder) throws RemoteException;

    boolean pauseEnroll() throws RemoteException;

    byte[] process(IBinder iBinder, String str, byte[] bArr) throws RemoteException;

    byte[] processFIDO(IBinder iBinder, String str, String str2, byte[] bArr) throws RemoteException;

    IBinder registerClient(IFingerprintClient iFingerprintClient, Bundle bundle) throws RemoteException;

    boolean removeAllEnrolledFingers(String str) throws RemoteException;

    boolean removeEnrolledFinger(int i, String str) throws RemoteException;

    int request(int i, int i2) throws RemoteException;

    boolean resumeEnroll() throws RemoteException;

    boolean setIndexName(int i, String str) throws RemoteException;

    boolean setPassword(String str, String str2) throws RemoteException;

    int showIdentifyDialog(IBinder iBinder, ComponentName componentName, String str, boolean z) throws RemoteException;

    boolean unregisterClient(IBinder iBinder) throws RemoteException;

    boolean verifyPassword(IBinder iBinder, String str, String str2) throws RemoteException;

    int verifySensorState(int i, int i2, int i3, int i4, int i5) throws RemoteException;
}
