package android.os;

import android.content.Intent;

public interface ICustomFrequencyManager extends IInterface {

    public static abstract class Stub extends Binder implements ICustomFrequencyManager {
        private static final String DESCRIPTOR = "android.os.ICustomFrequencyManager";
        static final int TRANSACTION_acquireDVFSLock = 29;
        static final int TRANSACTION_acquireGovernorParameter = 57;
        static final int TRANSACTION_acquirePersistentDVFSLock = 31;
        static final int TRANSACTION_checkCPUBoostRange = 27;
        static final int TRANSACTION_checkFrameRateRange = 10;
        static final int TRANSACTION_checkGPUFrequencyRange = 2;
        static final int TRANSACTION_checkSysBusFrequencyRange = 6;
        static final int TRANSACTION_deleteBatteryStatsDatabase = 43;
        static final int TRANSACTION_disableCPUCState = 19;
        static final int TRANSACTION_disableHotplugDisable = 24;
        static final int TRANSACTION_disableLegacyScheduler = 22;
        static final int TRANSACTION_disablePCIePSMDisable = 26;
        static final int TRANSACTION_enableCPUCState = 20;
        static final int TRANSACTION_enableHotplugDisable = 23;
        static final int TRANSACTION_enableLegacyScheduler = 21;
        static final int TRANSACTION_enablePCIePSMDisable = 25;
        static final int TRANSACTION_getAbusiveAppList = 47;
        static final int TRANSACTION_getBatteryDeltaSum = 44;
        static final int TRANSACTION_getBatteryRemainingUsageTime = 41;
        static final int TRANSACTION_getBatteryStatistics = 42;
        static final int TRANSACTION_getBatteryTotalCapacity = 45;
        static final int TRANSACTION_getDvfsPolicyByHint = 48;
        static final int TRANSACTION_getFrequentlyUsedAppListByLocation = 55;
        static final int TRANSACTION_getGameThrottlingLevel = 51;
        static final int TRANSACTION_getLeastRecentlyUsedAppList = 56;
        static final int TRANSACTION_getSavedTimeAfterKillingApp = 46;
        static final int TRANSACTION_getStandbyTimeInPowerSavingMode = 39;
        static final int TRANSACTION_getStandbyTimeInUltraPowerSavingMode = 38;
        static final int TRANSACTION_getSupportedCPUCoreNum = 16;
        static final int TRANSACTION_getSupportedCPUFrequency = 28;
        static final int TRANSACTION_getSupportedGPUFrequency = 1;
        static final int TRANSACTION_getSupportedLCDFrequency = 9;
        static final int TRANSACTION_getSupportedSysBusFrequency = 5;
        static final int TRANSACTION_mpdUpdate = 15;
        static final int TRANSACTION_notifyWmAniationState = 35;
        static final int TRANSACTION_releaseCPUCore = 18;
        static final int TRANSACTION_releaseDVFSLock = 30;
        static final int TRANSACTION_releaseGPU = 4;
        static final int TRANSACTION_releaseGovernorParameter = 58;
        static final int TRANSACTION_releasePersistentDVFSLock = 32;
        static final int TRANSACTION_releaseSysBus = 8;
        static final int TRANSACTION_requestCPUCore = 17;
        static final int TRANSACTION_requestCPUUpdate = 14;
        static final int TRANSACTION_requestGPU = 3;
        static final int TRANSACTION_requestLCDFrameRate = 11;
        static final int TRANSACTION_requestMMCBurstRate = 33;
        static final int TRANSACTION_requestMpParameterUpdate = 13;
        static final int TRANSACTION_requestSysBus = 7;
        static final int TRANSACTION_restoreLCDFrameRate = 12;
        static final int TRANSACTION_restoreMMCBurstRate = 34;
        static final int TRANSACTION_reviewPackage = 37;
        static final int TRANSACTION_sendCommandToSSRM = 36;
        static final int TRANSACTION_setGameFps = 50;
        static final int TRANSACTION_setGamePowerSaving = 49;
        static final int TRANSACTION_setGameTouchParam = 53;
        static final int TRANSACTION_setGameTurboMode = 52;
        static final int TRANSACTION_setHglPolicy = 40;
        static final int TRANSACTION_unsetGameTouchParam = 54;

        private static class Proxy implements ICustomFrequencyManager {
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

            public int[] getSupportedGPUFrequency() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean checkGPUFrequencyRange(int value) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(value);
                    this.mRemote.transact(2, _data, _reply, 0);
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

            public void requestGPU(int type, int freqLevel, IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(freqLevel);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void releaseGPU(int type, IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getSupportedSysBusFrequency() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean checkSysBusFrequencyRange(int value) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(value);
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

            public void requestSysBus(int type, int freqLevel, IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(freqLevel);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void releaseSysBus(int type, IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getSupportedLCDFrequency() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean checkFrameRateRange(int value) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(value);
                    this.mRemote.transact(10, _data, _reply, 0);
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

            public void requestLCDFrameRate(int frameRate, IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(frameRate);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restoreLCDFrameRate(IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestMpParameterUpdate(String command) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(command);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestCPUUpdate(int cpu, int enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cpu);
                    _data.writeInt(enable);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void mpdUpdate(int mpEnable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mpEnable);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getSupportedCPUCoreNum() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestCPUCore(int type, int coreNum, IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(coreNum);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void releaseCPUCore(int type, IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableCPUCState(IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableCPUCState(IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableLegacyScheduler(IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableLegacyScheduler(IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableHotplugDisable(IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableHotplugDisable(IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enablePCIePSMDisable(IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disablePCIePSMDisable(IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean checkCPUBoostRange(int value) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(value);
                    this.mRemote.transact(27, _data, _reply, 0);
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

            public int[] getSupportedCPUFrequency() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void acquireDVFSLock(int type, int freqLevel, IBinder lock, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(freqLevel);
                    _data.writeStrongBinder(lock);
                    _data.writeString(pkgName);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void releaseDVFSLock(IBinder lock, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lock);
                    _data.writeString(pkgName);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void acquirePersistentDVFSLock(int type, int frequency, int callingId, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(frequency);
                    _data.writeInt(callingId);
                    _data.writeString(pkgName);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void releasePersistentDVFSLock(int callingId, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callingId);
                    _data.writeString(pkgName);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestMMCBurstRate(int burstRate, IBinder binder, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(burstRate);
                    _data.writeStrongBinder(binder);
                    _data.writeString(pkgName);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restoreMMCBurstRate(IBinder binder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyWmAniationState(long currentTime, boolean isStart) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(currentTime);
                    if (isStart) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendCommandToSSRM(String type, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(type);
                    _data.writeString(value);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reviewPackage(String packagePath, String packagename) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packagePath);
                    _data.writeString(packagename);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getStandbyTimeInUltraPowerSavingMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getStandbyTimeInPowerSavingMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setHglPolicy(String xml) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(xml);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getBatteryRemainingUsageTime(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getBatteryStatistics(int what) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(what);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean deleteBatteryStatsDatabase() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(43, _data, _reply, 0);
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

            public int getBatteryDeltaSum(int what) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(what);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getBatteryTotalCapacity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getSavedTimeAfterKillingApp(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getAbusiveAppList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Intent getDvfsPolicyByHint(String hint) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Intent _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(hint);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Intent) Intent.CREATOR.createFromParcel(_reply);
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

            public void setGamePowerSaving(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enabled) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGameFps(int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(level);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getGameThrottlingLevel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGameTurboMode(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enabled) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGameTouchParam(String level, String head, String tail) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(level);
                    _data.writeString(head);
                    _data.writeString(tail);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unsetGameTouchParam() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getFrequentlyUsedAppListByLocation(double latitude, double longitude, int numOfItems) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeDouble(latitude);
                    _data.writeDouble(longitude);
                    _data.writeInt(numOfItems);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getLeastRecentlyUsedAppList(int numOfItems) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(numOfItems);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void acquireGovernorParameter(Intent param) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (param != null) {
                        _data.writeInt(1);
                        param.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void releaseGovernorParameter() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(58, _data, _reply, 0);
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

        public static ICustomFrequencyManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICustomFrequencyManager)) {
                return new Proxy(obj);
            }
            return (ICustomFrequencyManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            int[] _result;
            boolean _result2;
            int _result3;
            String[] _result4;
            boolean _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSupportedGPUFrequency();
                    reply.writeNoException();
                    reply.writeIntArray(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = checkGPUFrequencyRange(data.readInt());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    requestGPU(data.readInt(), data.readInt(), data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    releaseGPU(data.readInt(), data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSupportedSysBusFrequency();
                    reply.writeNoException();
                    reply.writeIntArray(_result);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = checkSysBusFrequencyRange(data.readInt());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    requestSysBus(data.readInt(), data.readInt(), data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    releaseSysBus(data.readInt(), data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSupportedLCDFrequency();
                    reply.writeNoException();
                    reply.writeIntArray(_result);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = checkFrameRateRange(data.readInt());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    requestLCDFrameRate(data.readInt(), data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    restoreLCDFrameRate(data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    requestMpParameterUpdate(data.readString());
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    requestCPUUpdate(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    mpdUpdate(data.readInt());
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSupportedCPUCoreNum();
                    reply.writeNoException();
                    reply.writeIntArray(_result);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    requestCPUCore(data.readInt(), data.readInt(), data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    releaseCPUCore(data.readInt(), data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    disableCPUCState(data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    enableCPUCState(data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    enableLegacyScheduler(data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    disableLegacyScheduler(data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    enableHotplugDisable(data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    disableHotplugDisable(data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    enablePCIePSMDisable(data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    disablePCIePSMDisable(data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = checkCPUBoostRange(data.readInt());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSupportedCPUFrequency();
                    reply.writeNoException();
                    reply.writeIntArray(_result);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    acquireDVFSLock(data.readInt(), data.readInt(), data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    releaseDVFSLock(data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    acquirePersistentDVFSLock(data.readInt(), data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    releasePersistentDVFSLock(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    requestMMCBurstRate(data.readInt(), data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    restoreMMCBurstRate(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 35:
                    boolean _arg1;
                    data.enforceInterface(DESCRIPTOR);
                    long _arg02 = data.readLong();
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    notifyWmAniationState(_arg02, _arg1);
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    sendCommandToSSRM(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    reviewPackage(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getStandbyTimeInUltraPowerSavingMode();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getStandbyTimeInPowerSavingMode();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    setHglPolicy(data.readString());
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getBatteryRemainingUsageTime(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _result5 = getBatteryStatistics(data.readInt());
                    reply.writeNoException();
                    reply.writeByteArray(_result5);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = deleteBatteryStatsDatabase();
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getBatteryDeltaSum(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getBatteryTotalCapacity();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    long _result6 = getSavedTimeAfterKillingApp(data.readString());
                    reply.writeNoException();
                    reply.writeLong(_result6);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getAbusiveAppList();
                    reply.writeNoException();
                    reply.writeStringArray(_result4);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    Intent _result7 = getDvfsPolicyByHint(data.readString());
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    setGamePowerSaving(_arg0);
                    reply.writeNoException();
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    setGameFps(data.readInt());
                    reply.writeNoException();
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getGameThrottlingLevel();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    setGameTurboMode(_arg0);
                    reply.writeNoException();
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    setGameTouchParam(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    unsetGameTouchParam();
                    reply.writeNoException();
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getFrequentlyUsedAppListByLocation(data.readDouble(), data.readDouble(), data.readInt());
                    reply.writeNoException();
                    reply.writeStringArray(_result4);
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getLeastRecentlyUsedAppList(data.readInt());
                    reply.writeNoException();
                    reply.writeStringArray(_result4);
                    return true;
                case 57:
                    Intent _arg03;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    acquireGovernorParameter(_arg03);
                    reply.writeNoException();
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    releaseGovernorParameter();
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

    void acquireDVFSLock(int i, int i2, IBinder iBinder, String str) throws RemoteException;

    void acquireGovernorParameter(Intent intent) throws RemoteException;

    void acquirePersistentDVFSLock(int i, int i2, int i3, String str) throws RemoteException;

    boolean checkCPUBoostRange(int i) throws RemoteException;

    boolean checkFrameRateRange(int i) throws RemoteException;

    boolean checkGPUFrequencyRange(int i) throws RemoteException;

    boolean checkSysBusFrequencyRange(int i) throws RemoteException;

    boolean deleteBatteryStatsDatabase() throws RemoteException;

    void disableCPUCState(IBinder iBinder, String str) throws RemoteException;

    void disableHotplugDisable(IBinder iBinder, String str) throws RemoteException;

    void disableLegacyScheduler(IBinder iBinder, String str) throws RemoteException;

    void disablePCIePSMDisable(IBinder iBinder, String str) throws RemoteException;

    void enableCPUCState(IBinder iBinder, String str) throws RemoteException;

    void enableHotplugDisable(IBinder iBinder, String str) throws RemoteException;

    void enableLegacyScheduler(IBinder iBinder, String str) throws RemoteException;

    void enablePCIePSMDisable(IBinder iBinder, String str) throws RemoteException;

    String[] getAbusiveAppList() throws RemoteException;

    int getBatteryDeltaSum(int i) throws RemoteException;

    int getBatteryRemainingUsageTime(int i) throws RemoteException;

    byte[] getBatteryStatistics(int i) throws RemoteException;

    int getBatteryTotalCapacity() throws RemoteException;

    Intent getDvfsPolicyByHint(String str) throws RemoteException;

    String[] getFrequentlyUsedAppListByLocation(double d, double d2, int i) throws RemoteException;

    int getGameThrottlingLevel() throws RemoteException;

    String[] getLeastRecentlyUsedAppList(int i) throws RemoteException;

    long getSavedTimeAfterKillingApp(String str) throws RemoteException;

    int getStandbyTimeInPowerSavingMode() throws RemoteException;

    int getStandbyTimeInUltraPowerSavingMode() throws RemoteException;

    int[] getSupportedCPUCoreNum() throws RemoteException;

    int[] getSupportedCPUFrequency() throws RemoteException;

    int[] getSupportedGPUFrequency() throws RemoteException;

    int[] getSupportedLCDFrequency() throws RemoteException;

    int[] getSupportedSysBusFrequency() throws RemoteException;

    void mpdUpdate(int i) throws RemoteException;

    void notifyWmAniationState(long j, boolean z) throws RemoteException;

    void releaseCPUCore(int i, IBinder iBinder, String str) throws RemoteException;

    void releaseDVFSLock(IBinder iBinder, String str) throws RemoteException;

    void releaseGPU(int i, IBinder iBinder, String str) throws RemoteException;

    void releaseGovernorParameter() throws RemoteException;

    void releasePersistentDVFSLock(int i, String str) throws RemoteException;

    void releaseSysBus(int i, IBinder iBinder, String str) throws RemoteException;

    void requestCPUCore(int i, int i2, IBinder iBinder, String str) throws RemoteException;

    void requestCPUUpdate(int i, int i2) throws RemoteException;

    void requestGPU(int i, int i2, IBinder iBinder, String str) throws RemoteException;

    void requestLCDFrameRate(int i, IBinder iBinder, String str) throws RemoteException;

    void requestMMCBurstRate(int i, IBinder iBinder, String str) throws RemoteException;

    void requestMpParameterUpdate(String str) throws RemoteException;

    void requestSysBus(int i, int i2, IBinder iBinder, String str) throws RemoteException;

    void restoreLCDFrameRate(IBinder iBinder, String str) throws RemoteException;

    void restoreMMCBurstRate(IBinder iBinder) throws RemoteException;

    void reviewPackage(String str, String str2) throws RemoteException;

    void sendCommandToSSRM(String str, String str2) throws RemoteException;

    void setGameFps(int i) throws RemoteException;

    void setGamePowerSaving(boolean z) throws RemoteException;

    void setGameTouchParam(String str, String str2, String str3) throws RemoteException;

    void setGameTurboMode(boolean z) throws RemoteException;

    void setHglPolicy(String str) throws RemoteException;

    void unsetGameTouchParam() throws RemoteException;
}
