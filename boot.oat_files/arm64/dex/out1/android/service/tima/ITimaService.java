package android.service.tima;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import java.util.List;

public interface ITimaService extends IInterface {

    public static abstract class Stub extends Binder implements ITimaService {
        private static final String DESCRIPTOR = "android.service.tima.ITimaService";
        static final int TRANSACTION_DCMSelfCheck = 45;
        static final int TRANSACTION_FipsKeyStore3_del = 41;
        static final int TRANSACTION_FipsKeyStore3_exist = 38;
        static final int TRANSACTION_FipsKeyStore3_get = 39;
        static final int TRANSACTION_FipsKeyStore3_getmtime = 40;
        static final int TRANSACTION_FipsKeyStore3_init = 36;
        static final int TRANSACTION_FipsKeyStore3_put = 37;
        static final int TRANSACTION_FipsKeyStore3_saw = 42;
        static final int TRANSACTION_KeyStore3_del = 17;
        static final int TRANSACTION_KeyStore3_exist = 14;
        static final int TRANSACTION_KeyStore3_get = 15;
        static final int TRANSACTION_KeyStore3_getmtime = 16;
        static final int TRANSACTION_KeyStore3_init = 12;
        static final int TRANSACTION_KeyStore3_put = 13;
        static final int TRANSACTION_KeyStore3_saw = 18;
        static final int TRANSACTION_attestation = 10;
        static final int TRANSACTION_ccmRegisterForDefaultCertificate = 20;
        static final int TRANSACTION_checkEvent = 1;
        static final int TRANSACTION_checkHistory = 2;
        static final int TRANSACTION_displayEvent = 3;
        static final int TRANSACTION_dumpLog = 21;
        static final int TRANSACTION_getDeviceID = 11;
        static final int TRANSACTION_getEventList = 4;
        static final int TRANSACTION_getTimaVersion = 19;
        static final int TRANSACTION_getTuiVersion = 29;
        static final int TRANSACTION_isKapEnforced = 44;
        static final int TRANSACTION_keystoreInit = 6;
        static final int TRANSACTION_keystoreInstallKey = 7;
        static final int TRANSACTION_keystoreRetrieveKey = 8;
        static final int TRANSACTION_keystoreShutdown = 9;
        static final int TRANSACTION_launchTui = 24;
        static final int TRANSACTION_launchTuiWithSecretId = 25;
        static final int TRANSACTION_loadTui = 22;
        static final int TRANSACTION_setISLCallback = 5;
        static final int TRANSACTION_setKapMode = 43;
        static final int TRANSACTION_tuiDecryptPinHash = 34;
        static final int TRANSACTION_tuiGetCerts = 32;
        static final int TRANSACTION_tuiGetSecretDimension = 33;
        static final int TRANSACTION_tuiInitSecret = 26;
        static final int TRANSACTION_tuiInitSecretFile = 27;
        static final int TRANSACTION_tuiInitSecretMemoryFile = 28;
        static final int TRANSACTION_tuiRegAppImage = 30;
        static final int TRANSACTION_tuiRegAppImageFile = 31;
        static final int TRANSACTION_unloadTui = 23;
        static final int TRANSACTION_verifyCertChain = 35;

        private static class Proxy implements ITimaService {
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

            public String checkEvent(int operation, int subject) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(operation);
                    _data.writeInt(subject);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String checkHistory(int operation, int subject) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(operation);
                    _data.writeInt(subject);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void displayEvent(String event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(event);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getEventList(int subject) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subject);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setISLCallback(ITimaISLCallback timaISLClbk) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(timaISLClbk != null ? timaISLClbk.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int keystoreInit() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int keystoreInstallKey(int ID, byte[] key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ID);
                    _data.writeByteArray(key);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] keystoreRetrieveKey(int ID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ID);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int keystoreShutdown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] attestation(byte[] nonce, int callerUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(nonce);
                    _data.writeInt(callerUid);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getDeviceID() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int KeyStore3_init() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int KeyStore3_put(String alias, byte[] key, int uid, char[] password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeByteArray(key);
                    _data.writeInt(uid);
                    _data.writeCharArray(password);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean KeyStore3_exist(String alias, int uid) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(uid);
                    this.mRemote.transact(14, _data, _reply, 0);
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

            public byte[] KeyStore3_get(String alias, char[] password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeCharArray(password);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KeyStore3_getmtime(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean KeyStore3_del(String alias, int uid) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(uid);
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

            public String[] KeyStore3_saw(String prefix, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(prefix);
                    _data.writeInt(uid);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getTimaVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int ccmRegisterForDefaultCertificate(int uid, String regPassword, String oldPassword, boolean isTrustedBootRequired) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(regPassword);
                    _data.writeString(oldPassword);
                    if (isTrustedBootRequired) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] dumpLog() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int loadTui() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int unloadTui() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] launchTui(String app_name, byte[] nonce, boolean verify, int min_pin_length) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(app_name);
                    _data.writeByteArray(nonce);
                    if (verify) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(min_pin_length);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] launchTuiWithSecretId(String app_name, String secret_id, byte[] nonce, boolean verify, int min_pin_length) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(app_name);
                    _data.writeString(secret_id);
                    _data.writeByteArray(nonce);
                    if (verify) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(min_pin_length);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int tuiInitSecret(byte[] buffer, int width, int height, String secret_id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(buffer);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeString(secret_id);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int tuiInitSecretFile(String file_name, int width, int height, String secret_id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(file_name);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeString(secret_id);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int tuiInitSecretMemoryFile(ParcelFileDescriptor pfd, int size, int width, int height, String secret_id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (pfd != null) {
                        _data.writeInt(1);
                        pfd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(size);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeString(secret_id);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getTuiVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] tuiRegAppImage(String app_name, byte[] png_buffer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(app_name);
                    _data.writeByteArray(png_buffer);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] tuiRegAppImageFile(String app_name, String png_file) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(app_name);
                    _data.writeString(png_file);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] tuiGetCerts() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] tuiGetSecretDimension() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] tuiDecryptPinHash(String app_name, byte[] buffer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(app_name);
                    _data.writeByteArray(buffer);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] verifyCertChain(byte[] buffer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(buffer);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int FipsKeyStore3_init(boolean selfTestOnly) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (selfTestOnly) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int FipsKeyStore3_put(String alias, byte[] key, int uid, char[] password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeByteArray(key);
                    _data.writeInt(uid);
                    _data.writeCharArray(password);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean FipsKeyStore3_exist(String alias, int uid) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(uid);
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

            public byte[] FipsKeyStore3_get(String alias, char[] password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeCharArray(password);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long FipsKeyStore3_getmtime(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean FipsKeyStore3_del(String alias, int uid) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(uid);
                    this.mRemote.transact(41, _data, _reply, 0);
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

            public String[] FipsKeyStore3_saw(String prefix, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(prefix);
                    _data.writeInt(uid);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setKapMode(boolean isEnabled, boolean notifyKapState) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isEnabled ? 1 : 0);
                    if (!notifyKapState) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isKapEnforced() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, _data, _reply, 0);
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

            public void DCMSelfCheck() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(45, _data, _reply, 0);
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

        public static ITimaService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITimaService)) {
                return new Proxy(obj);
            }
            return (ITimaService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            String _result;
            int _result2;
            byte[] _result3;
            boolean _result4;
            long _result5;
            String[] _result6;
            String _arg1;
            boolean _arg3;
            String _arg0;
            int[] _result7;
            boolean _arg02;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = checkEvent(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = checkHistory(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    displayEvent(data.readString());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result8 = getEventList(data.readInt());
                    reply.writeNoException();
                    reply.writeStringList(_result8);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    setISLCallback(android.service.tima.ITimaISLCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = keystoreInit();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = keystoreInstallKey(data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = keystoreRetrieveKey(data.readInt());
                    reply.writeNoException();
                    reply.writeByteArray(_result3);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = keystoreShutdown();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = attestation(data.createByteArray(), data.readInt());
                    reply.writeNoException();
                    reply.writeByteArray(_result3);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getDeviceID();
                    reply.writeNoException();
                    reply.writeByteArray(_result3);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = KeyStore3_init();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = KeyStore3_put(data.readString(), data.createByteArray(), data.readInt(), data.createCharArray());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = KeyStore3_exist(data.readString(), data.readInt());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = KeyStore3_get(data.readString(), data.createCharArray());
                    reply.writeNoException();
                    reply.writeByteArray(_result3);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = KeyStore3_getmtime(data.readString());
                    reply.writeNoException();
                    reply.writeLong(_result5);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = KeyStore3_del(data.readString(), data.readInt());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = KeyStore3_saw(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeStringArray(_result6);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getTimaVersion();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    _arg1 = data.readString();
                    String _arg2 = data.readString();
                    if (data.readInt() != 0) {
                        _arg3 = true;
                    } else {
                        _arg3 = false;
                    }
                    _result2 = ccmRegisterForDefaultCertificate(_arg03, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = dumpLog();
                    reply.writeNoException();
                    reply.writeByteArray(_result3);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = loadTui();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = unloadTui();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 24:
                    boolean _arg22;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    byte[] _arg12 = data.createByteArray();
                    if (data.readInt() != 0) {
                        _arg22 = true;
                    } else {
                        _arg22 = false;
                    }
                    _result3 = launchTui(_arg0, _arg12, _arg22, data.readInt());
                    reply.writeNoException();
                    reply.writeByteArray(_result3);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    _arg1 = data.readString();
                    byte[] _arg23 = data.createByteArray();
                    if (data.readInt() != 0) {
                        _arg3 = true;
                    } else {
                        _arg3 = false;
                    }
                    _result3 = launchTuiWithSecretId(_arg0, _arg1, _arg23, _arg3, data.readInt());
                    reply.writeNoException();
                    reply.writeByteArray(_result3);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = tuiInitSecret(data.createByteArray(), data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = tuiInitSecretFile(data.readString(), data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 28:
                    ParcelFileDescriptor _arg04;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    _result2 = tuiInitSecretMemoryFile(_arg04, data.readInt(), data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getTuiVersion();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = tuiRegAppImage(data.readString(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeIntArray(_result7);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = tuiRegAppImageFile(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeIntArray(_result7);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = tuiGetCerts();
                    reply.writeNoException();
                    reply.writeByteArray(_result3);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = tuiGetSecretDimension();
                    reply.writeNoException();
                    reply.writeIntArray(_result7);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = tuiDecryptPinHash(data.readString(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeByteArray(_result3);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = verifyCertChain(data.createByteArray());
                    reply.writeNoException();
                    reply.writeByteArray(_result3);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    _result2 = FipsKeyStore3_init(_arg02);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = FipsKeyStore3_put(data.readString(), data.createByteArray(), data.readInt(), data.createCharArray());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = FipsKeyStore3_exist(data.readString(), data.readInt());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = FipsKeyStore3_get(data.readString(), data.createCharArray());
                    reply.writeNoException();
                    reply.writeByteArray(_result3);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = FipsKeyStore3_getmtime(data.readString());
                    reply.writeNoException();
                    reply.writeLong(_result5);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = FipsKeyStore3_del(data.readString(), data.readInt());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = FipsKeyStore3_saw(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeStringArray(_result6);
                    return true;
                case 43:
                    boolean _arg13;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    if (data.readInt() != 0) {
                        _arg13 = true;
                    } else {
                        _arg13 = false;
                    }
                    setKapMode(_arg02, _arg13);
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isKapEnforced();
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    DCMSelfCheck();
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

    void DCMSelfCheck() throws RemoteException;

    boolean FipsKeyStore3_del(String str, int i) throws RemoteException;

    boolean FipsKeyStore3_exist(String str, int i) throws RemoteException;

    byte[] FipsKeyStore3_get(String str, char[] cArr) throws RemoteException;

    long FipsKeyStore3_getmtime(String str) throws RemoteException;

    int FipsKeyStore3_init(boolean z) throws RemoteException;

    int FipsKeyStore3_put(String str, byte[] bArr, int i, char[] cArr) throws RemoteException;

    String[] FipsKeyStore3_saw(String str, int i) throws RemoteException;

    boolean KeyStore3_del(String str, int i) throws RemoteException;

    boolean KeyStore3_exist(String str, int i) throws RemoteException;

    byte[] KeyStore3_get(String str, char[] cArr) throws RemoteException;

    long KeyStore3_getmtime(String str) throws RemoteException;

    int KeyStore3_init() throws RemoteException;

    int KeyStore3_put(String str, byte[] bArr, int i, char[] cArr) throws RemoteException;

    String[] KeyStore3_saw(String str, int i) throws RemoteException;

    byte[] attestation(byte[] bArr, int i) throws RemoteException;

    int ccmRegisterForDefaultCertificate(int i, String str, String str2, boolean z) throws RemoteException;

    String checkEvent(int i, int i2) throws RemoteException;

    String checkHistory(int i, int i2) throws RemoteException;

    void displayEvent(String str) throws RemoteException;

    byte[] dumpLog() throws RemoteException;

    byte[] getDeviceID() throws RemoteException;

    List<String> getEventList(int i) throws RemoteException;

    String getTimaVersion() throws RemoteException;

    String getTuiVersion() throws RemoteException;

    boolean isKapEnforced() throws RemoteException;

    int keystoreInit() throws RemoteException;

    int keystoreInstallKey(int i, byte[] bArr) throws RemoteException;

    byte[] keystoreRetrieveKey(int i) throws RemoteException;

    int keystoreShutdown() throws RemoteException;

    byte[] launchTui(String str, byte[] bArr, boolean z, int i) throws RemoteException;

    byte[] launchTuiWithSecretId(String str, String str2, byte[] bArr, boolean z, int i) throws RemoteException;

    int loadTui() throws RemoteException;

    void setISLCallback(ITimaISLCallback iTimaISLCallback) throws RemoteException;

    void setKapMode(boolean z, boolean z2) throws RemoteException;

    byte[] tuiDecryptPinHash(String str, byte[] bArr) throws RemoteException;

    byte[] tuiGetCerts() throws RemoteException;

    int[] tuiGetSecretDimension() throws RemoteException;

    int tuiInitSecret(byte[] bArr, int i, int i2, String str) throws RemoteException;

    int tuiInitSecretFile(String str, int i, int i2, String str2) throws RemoteException;

    int tuiInitSecretMemoryFile(ParcelFileDescriptor parcelFileDescriptor, int i, int i2, int i3, String str) throws RemoteException;

    int[] tuiRegAppImage(String str, byte[] bArr) throws RemoteException;

    int[] tuiRegAppImageFile(String str, String str2) throws RemoteException;

    int unloadTui() throws RemoteException;

    byte[] verifyCertChain(byte[] bArr) throws RemoteException;
}
