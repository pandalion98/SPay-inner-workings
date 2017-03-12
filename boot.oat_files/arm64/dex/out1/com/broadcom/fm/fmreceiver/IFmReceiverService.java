package com.broadcom.fm.fmreceiver;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFmReceiverService extends IInterface {

    public static abstract class Stub extends Binder implements IFmReceiverService {
        private static final String DESCRIPTOR = "com.broadcom.fm.fmreceiver.IFmReceiverService";
        static final int TRANSACTION_cleanupFmService = 33;
        static final int TRANSACTION_estimateNoiseFloorLevel = 26;
        static final int TRANSACTION_getCurrentRssi = 7;
        static final int TRANSACTION_getCurrentSNR = 8;
        static final int TRANSACTION_getIsMute = 9;
        static final int TRANSACTION_getMonoStereoMode = 5;
        static final int TRANSACTION_getRadioIsOn = 4;
        static final int TRANSACTION_getStatus = 13;
        static final int TRANSACTION_getTunedFrequency = 6;
        static final int TRANSACTION_init = 1;
        static final int TRANSACTION_muteAudio = 14;
        static final int TRANSACTION_offFMService = 34;
        static final int TRANSACTION_registerCallback = 2;
        static final int TRANSACTION_seekRdsStation = 17;
        static final int TRANSACTION_seekStation = 15;
        static final int TRANSACTION_seekStationAbort = 18;
        static final int TRANSACTION_seekStationCombo = 16;
        static final int TRANSACTION_setAudioMode = 21;
        static final int TRANSACTION_setAudioPath = 22;
        static final int TRANSACTION_setCOS = 30;
        static final int TRANSACTION_setDeemPhasis = 25;
        static final int TRANSACTION_setFMIntenna = 32;
        static final int TRANSACTION_setFMVolume = 28;
        static final int TRANSACTION_setLiveAudioPolling = 27;
        static final int TRANSACTION_setPiEccMode = 20;
        static final int TRANSACTION_setRdsMode = 19;
        static final int TRANSACTION_setRfMute = 31;
        static final int TRANSACTION_setSnrThreshold = 29;
        static final int TRANSACTION_setStepSize = 23;
        static final int TRANSACTION_setWorldRegion = 24;
        static final int TRANSACTION_tuneRadio = 12;
        static final int TRANSACTION_turnOffRadio = 10;
        static final int TRANSACTION_turnOnRadio = 11;
        static final int TRANSACTION_unregisterCallback = 3;

        private static class Proxy implements IFmReceiverService {
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

            public void init() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerCallback(IFmReceiverCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterCallback(IFmReceiverCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getRadioIsOn() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public int getMonoStereoMode() throws RemoteException {
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

            public int getTunedFrequency() throws RemoteException {
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

            public long getCurrentRssi() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getCurrentSNR() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getIsMute() throws RemoteException {
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

            public int turnOffRadio(boolean bKeepChipOn) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bKeepChipOn) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int turnOnRadio(int functionalityMask, char[] clientPackagename) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(functionalityMask);
                    _data.writeCharArray(clientPackagename);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readCharArray(clientPackagename);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int tuneRadio(int freq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(freq);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int muteAudio(boolean mute) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (mute) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long seekStation(int scanDirection, int minSignalStrength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(scanDirection);
                    _data.writeInt(minSignalStrength);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int seekStationCombo(int startFreq, int endFreq, int minSignalStrength, int dir, int scanMethod, boolean multiChannel, int rdsType, int rdsTypeValue) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(startFreq);
                    _data.writeInt(endFreq);
                    _data.writeInt(minSignalStrength);
                    _data.writeInt(dir);
                    _data.writeInt(scanMethod);
                    if (multiChannel) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(rdsType);
                    _data.writeInt(rdsTypeValue);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long seekRdsStation(int scanDirection, int minSignalStrength, int rdsCondition, int rdsValue) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(scanDirection);
                    _data.writeInt(minSignalStrength);
                    _data.writeInt(rdsCondition);
                    _data.writeInt(rdsValue);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int seekStationAbort() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setRdsMode(int rdsMode, int rdsFeatures, int afMode, int afThreshold) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rdsMode);
                    _data.writeInt(rdsFeatures);
                    _data.writeInt(afMode);
                    _data.writeInt(afThreshold);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setPiEccMode(int piEccOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piEccOn);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setAudioMode(int audioMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(audioMode);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setAudioPath(int audioPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(audioPath);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setStepSize(int stepSize) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stepSize);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setWorldRegion(int worldRegion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(worldRegion);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setDeemPhasis(int deemphasisTime) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deemphasisTime);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int estimateNoiseFloorLevel(int nflLevel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(nflLevel);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setLiveAudioPolling(boolean liveAudioPolling, int signalPollInterval) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (liveAudioPolling) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(signalPollInterval);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setFMVolume(int volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(volume);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setSnrThreshold(int snrThreshold) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(snrThreshold);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setCOS(int cos_value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cos_value);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setRfMute(boolean rf_mute) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rf_mute) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setFMIntenna(boolean setFMIntenna) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (setFMIntenna) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int cleanupFmService() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int offFMService() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IFmReceiverService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IFmReceiverService)) {
                return new Proxy(obj);
            }
            return (IFmReceiverService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _result;
            int _result2;
            long _result3;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    init();
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    registerCallback(com.broadcom.fm.fmreceiver.IFmReceiverCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterCallback(com.broadcom.fm.fmreceiver.IFmReceiverCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getRadioIsOn();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getMonoStereoMode();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getTunedFrequency();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getCurrentRssi();
                    reply.writeNoException();
                    reply.writeLong(_result3);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getCurrentSNR();
                    reply.writeNoException();
                    reply.writeLong(_result3);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getIsMute();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = turnOffRadio(data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    char[] _arg1 = data.createCharArray();
                    _result2 = turnOnRadio(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    reply.writeCharArray(_arg1);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = tuneRadio(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getStatus();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = muteAudio(data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = seekStation(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeLong(_result3);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = seekStationCombo(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt() != 0, data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = seekRdsStation(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeLong(_result3);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = seekStationAbort();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setRdsMode(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setPiEccMode(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setAudioMode(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setAudioPath(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setStepSize(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setWorldRegion(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setDeemPhasis(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = estimateNoiseFloorLevel(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setLiveAudioPolling(data.readInt() != 0, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setFMVolume(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setSnrThreshold(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setCOS(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setRfMute(data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setFMIntenna(data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = cleanupFmService();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = offFMService();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    int cleanupFmService() throws RemoteException;

    int estimateNoiseFloorLevel(int i) throws RemoteException;

    long getCurrentRssi() throws RemoteException;

    long getCurrentSNR() throws RemoteException;

    boolean getIsMute() throws RemoteException;

    int getMonoStereoMode() throws RemoteException;

    boolean getRadioIsOn() throws RemoteException;

    int getStatus() throws RemoteException;

    int getTunedFrequency() throws RemoteException;

    void init() throws RemoteException;

    int muteAudio(boolean z) throws RemoteException;

    int offFMService() throws RemoteException;

    void registerCallback(IFmReceiverCallback iFmReceiverCallback) throws RemoteException;

    long seekRdsStation(int i, int i2, int i3, int i4) throws RemoteException;

    long seekStation(int i, int i2) throws RemoteException;

    int seekStationAbort() throws RemoteException;

    int seekStationCombo(int i, int i2, int i3, int i4, int i5, boolean z, int i6, int i7) throws RemoteException;

    int setAudioMode(int i) throws RemoteException;

    int setAudioPath(int i) throws RemoteException;

    int setCOS(int i) throws RemoteException;

    int setDeemPhasis(int i) throws RemoteException;

    int setFMIntenna(boolean z) throws RemoteException;

    int setFMVolume(int i) throws RemoteException;

    int setLiveAudioPolling(boolean z, int i) throws RemoteException;

    int setPiEccMode(int i) throws RemoteException;

    int setRdsMode(int i, int i2, int i3, int i4) throws RemoteException;

    int setRfMute(boolean z) throws RemoteException;

    int setSnrThreshold(int i) throws RemoteException;

    int setStepSize(int i) throws RemoteException;

    int setWorldRegion(int i) throws RemoteException;

    int tuneRadio(int i) throws RemoteException;

    int turnOffRadio(boolean z) throws RemoteException;

    int turnOnRadio(int i, char[] cArr) throws RemoteException;

    void unregisterCallback(IFmReceiverCallback iFmReceiverCallback) throws RemoteException;
}
