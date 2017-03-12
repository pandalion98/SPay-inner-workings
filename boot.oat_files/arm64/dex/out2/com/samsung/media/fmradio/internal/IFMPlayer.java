package com.samsung.media.fmradio.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFMPlayer extends IInterface {

    public static abstract class Stub extends Binder implements IFMPlayer {
        private static final String DESCRIPTOR = "com.samsung.media.fmradio.internal.IFMPlayer";
        static final int TRANSACTION_SkipTuning_Value = 35;
        static final int TRANSACTION_cancelAFSwitching = 30;
        static final int TRANSACTION_cancelScan = 12;
        static final int TRANSACTION_cancelSeek = 9;
        static final int TRANSACTION_disableAF = 24;
        static final int TRANSACTION_disableDNS = 21;
        static final int TRANSACTION_disableRDS = 19;
        static final int TRANSACTION_enableAF = 23;
        static final int TRANSACTION_enableDNS = 20;
        static final int TRANSACTION_enableRDS = 18;
        static final int TRANSACTION_getAFRMSSISamples = 90;
        static final int TRANSACTION_getAFRMSSIThreshold = 88;
        static final int TRANSACTION_getAFValid_th = 66;
        static final int TRANSACTION_getAF_th = 64;
        static final int TRANSACTION_getCFOTh12 = 82;
        static final int TRANSACTION_getCnt_th = 57;
        static final int TRANSACTION_getCnt_th_2 = 60;
        static final int TRANSACTION_getCurrentChannel = 10;
        static final int TRANSACTION_getCurrentRSSI = 45;
        static final int TRANSACTION_getCurrentSNR = 46;
        static final int TRANSACTION_getGoodChannelRMSSIThreshold = 92;
        static final int TRANSACTION_getHybridSearch = 94;
        static final int TRANSACTION_getLastScanResult = 31;
        static final int TRANSACTION_getMaxVolume = 41;
        static final int TRANSACTION_getOffChannelThreshold = 78;
        static final int TRANSACTION_getOnChannelThreshold = 76;
        static final int TRANSACTION_getRMSSIFirstStage = 84;
        static final int TRANSACTION_getRSSI_th = 55;
        static final int TRANSACTION_getRSSI_th_2 = 58;
        static final int TRANSACTION_getSINRFirstStage = 86;
        static final int TRANSACTION_getSINRSamples = 74;
        static final int TRANSACTION_getSINRThreshold = 80;
        static final int TRANSACTION_getSNR_th = 56;
        static final int TRANSACTION_getSNR_th_2 = 59;
        static final int TRANSACTION_getSearchAlgoType = 72;
        static final int TRANSACTION_getSeekDC = 96;
        static final int TRANSACTION_getSeekQA = 98;
        static final int TRANSACTION_getSoftMuteMode = 69;
        static final int TRANSACTION_getVolume = 36;
        static final int TRANSACTION_isAFEnable = 29;
        static final int TRANSACTION_isAirPlaneMode = 42;
        static final int TRANSACTION_isBatteryLow = 62;
        static final int TRANSACTION_isBusy = 27;
        static final int TRANSACTION_isDNSEnable = 22;
        static final int TRANSACTION_isHeadsetPlugged = 37;
        static final int TRANSACTION_isOn = 6;
        static final int TRANSACTION_isRDSEnable = 28;
        static final int TRANSACTION_isScanning = 13;
        static final int TRANSACTION_isSeeking = 14;
        static final int TRANSACTION_isTvOutPlugged = 38;
        static final int TRANSACTION_mute = 43;
        static final int TRANSACTION_off = 5;
        static final int TRANSACTION_on = 4;
        static final int TRANSACTION_on_in_testmode = 61;
        static final int TRANSACTION_removeListener = 2;
        static final int TRANSACTION_scan = 11;
        static final int TRANSACTION_searchAll = 17;
        static final int TRANSACTION_searchDown = 15;
        static final int TRANSACTION_searchUp = 16;
        static final int TRANSACTION_seekDown = 8;
        static final int TRANSACTION_seekUp = 7;
        static final int TRANSACTION_setAFRMSSISamples = 89;
        static final int TRANSACTION_setAFRMSSIThreshold = 87;
        static final int TRANSACTION_setAFValid_th = 65;
        static final int TRANSACTION_setAF_th = 63;
        static final int TRANSACTION_setBand = 25;
        static final int TRANSACTION_setCFOTh12 = 81;
        static final int TRANSACTION_setChannelSpacing = 26;
        static final int TRANSACTION_setCnt_th = 51;
        static final int TRANSACTION_setCnt_th_2 = 54;
        static final int TRANSACTION_setDEConstant = 44;
        static final int TRANSACTION_setFMIntenna = 67;
        static final int TRANSACTION_setGoodChannelRMSSIThreshold = 91;
        static final int TRANSACTION_setHybridSearch = 93;
        static final int TRANSACTION_setInternetStreamingMode = 99;
        static final int TRANSACTION_setListener = 1;
        static final int TRANSACTION_setMono = 33;
        static final int TRANSACTION_setOffChannelThreshold = 77;
        static final int TRANSACTION_setOnChannelThreshold = 75;
        static final int TRANSACTION_setRMSSIFirstStage = 83;
        static final int TRANSACTION_setRSSI_th = 49;
        static final int TRANSACTION_setRSSI_th_2 = 52;
        static final int TRANSACTION_setRecordMode = 40;
        static final int TRANSACTION_setSINRFirstStage = 85;
        static final int TRANSACTION_setSINRSamples = 73;
        static final int TRANSACTION_setSINRThreshold = 79;
        static final int TRANSACTION_setSNR_th = 50;
        static final int TRANSACTION_setSNR_th_2 = 53;
        static final int TRANSACTION_setSearchAlgoType = 71;
        static final int TRANSACTION_setSeekDC = 95;
        static final int TRANSACTION_setSeekQA = 97;
        static final int TRANSACTION_setSeekRSSI = 47;
        static final int TRANSACTION_setSeekSNR = 48;
        static final int TRANSACTION_setSoftMuteControl = 70;
        static final int TRANSACTION_setSoftmute = 68;
        static final int TRANSACTION_setSpeakerOn = 39;
        static final int TRANSACTION_setStereo = 32;
        static final int TRANSACTION_setVolume = 34;
        static final int TRANSACTION_tune = 3;

        private static class Proxy implements IFMPlayer {
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

            public void setListener(IFMEventListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeListener(IFMEventListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void tune(long freq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(freq);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean on() throws RemoteException {
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

            public boolean off() throws RemoteException {
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

            public boolean isOn() throws RemoteException {
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

            public long seekUp() throws RemoteException {
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

            public long seekDown() throws RemoteException {
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

            public void cancelSeek() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getCurrentChannel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void scan() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean cancelScan() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
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

            public boolean isScanning() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public boolean isSeeking() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public long searchDown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long searchUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long searchAll() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableRDS() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableRDS() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableDNS() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableDNS() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDNSEnable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
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

            public void enableAF() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_enableAF, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableAF() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_disableAF, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBand(int band) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(band);
                    this.mRemote.transact(Stub.TRANSACTION_setBand, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setChannelSpacing(int spacing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(spacing);
                    this.mRemote.transact(Stub.TRANSACTION_setChannelSpacing, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int isBusy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isBusy, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRDSEnable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isRDSEnable, _data, _reply, 0);
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

            public boolean isAFEnable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isAFEnable, _data, _reply, 0);
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

            public void cancelAFSwitching() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_cancelAFSwitching, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long[] getLastScanResult() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    long[] _result = _reply.createLongArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setStereo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMono() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_setMono, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVolume(long val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(val);
                    this.mRemote.transact(Stub.TRANSACTION_setVolume, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void SkipTuning_Value() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_SkipTuning_Value, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getVolume() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getVolume, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isHeadsetPlugged() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isHeadsetPlugged, _data, _reply, 0);
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

            public boolean isTvOutPlugged() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isTvOutPlugged, _data, _reply, 0);
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

            public void setSpeakerOn(boolean bSpeakerOn) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bSpeakerOn) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_setSpeakerOn, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRecordMode(int is_record) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(is_record);
                    this.mRemote.transact(Stub.TRANSACTION_setRecordMode, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getMaxVolume() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getMaxVolume, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAirPlaneMode() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isAirPlaneMode, _data, _reply, 0);
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

            public void mute(boolean value) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (value) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_mute, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDEConstant(long val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(val);
                    this.mRemote.transact(Stub.TRANSACTION_setDEConstant, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getCurrentRSSI() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getCurrentRSSI, _data, _reply, 0);
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
                    this.mRemote.transact(Stub.TRANSACTION_getCurrentSNR, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSeekRSSI(long val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(val);
                    this.mRemote.transact(Stub.TRANSACTION_setSeekRSSI, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSeekSNR(long val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(val);
                    this.mRemote.transact(Stub.TRANSACTION_setSeekSNR, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRSSI_th(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setRSSI_th, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSNR_th(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setSNR_th, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCnt_th(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setCnt_th, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRSSI_th_2(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setRSSI_th_2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSNR_th_2(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setSNR_th_2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCnt_th_2(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setCnt_th_2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRSSI_th() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getRSSI_th, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSNR_th() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getSNR_th, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCnt_th() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getCnt_th, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRSSI_th_2() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getRSSI_th_2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSNR_th_2() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getSNR_th_2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCnt_th_2() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getCnt_th_2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean on_in_testmode() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_on_in_testmode, _data, _reply, 0);
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

            public boolean isBatteryLow() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isBatteryLow, _data, _reply, 0);
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

            public void setAF_th(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAF_th() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAFValid_th(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setAFValid_th, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAFValid_th() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getAFValid_th, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFMIntenna(boolean setFMIntenna) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (setFMIntenna) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_setFMIntenna, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSoftmute(boolean state) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (state) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_setSoftmute, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getSoftMuteMode() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getSoftMuteMode, _data, _reply, 0);
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

            public void setSoftMuteControl(int min_RSSI, int max_RSSI, int max_attenuation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(min_RSSI);
                    _data.writeInt(max_RSSI);
                    _data.writeInt(max_attenuation);
                    this.mRemote.transact(Stub.TRANSACTION_setSoftMuteControl, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSearchAlgoType(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setSearchAlgoType, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSearchAlgoType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getSearchAlgoType, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSINRSamples(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setSINRSamples, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSINRSamples() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getSINRSamples, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setOnChannelThreshold(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setOnChannelThreshold, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getOnChannelThreshold() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getOnChannelThreshold, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setOffChannelThreshold(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setOffChannelThreshold, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getOffChannelThreshold() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getOffChannelThreshold, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSINRThreshold(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setSINRThreshold, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSINRThreshold() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getSINRThreshold, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCFOTh12(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setCFOTh12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCFOTh12() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getCFOTh12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRMSSIFirstStage(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setRMSSIFirstStage, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRMSSIFirstStage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getRMSSIFirstStage, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSINRFirstStage(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setSINRFirstStage, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSINRFirstStage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getSINRFirstStage, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAFRMSSIThreshold(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setAFRMSSIThreshold, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAFRMSSIThreshold() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getAFRMSSIThreshold, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAFRMSSISamples(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setAFRMSSISamples, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAFRMSSISamples() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getAFRMSSISamples, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGoodChannelRMSSIThreshold(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setGoodChannelRMSSIThreshold, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getGoodChannelRMSSIThreshold() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getGoodChannelRMSSIThreshold, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setHybridSearch(String val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(val);
                    this.mRemote.transact(Stub.TRANSACTION_setHybridSearch, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getHybridSearch() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getHybridSearch, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSeekDC(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setSeekDC, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSeekDC() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getSeekDC, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSeekQA(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    this.mRemote.transact(Stub.TRANSACTION_setSeekQA, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSeekQA() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getSeekQA, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInternetStreamingMode(boolean mode) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (mode) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_setInternetStreamingMode, _data, _reply, 0);
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

        public static IFMPlayer asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IFMPlayer)) {
                return new Proxy(obj);
            }
            return (IFMPlayer) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            boolean _result;
            long _result2;
            int _result3;
            boolean _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    setListener(com.samsung.media.fmradio.internal.IFMEventListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    removeListener(com.samsung.media.fmradio.internal.IFMEventListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    tune(data.readLong());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result = on();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result = off();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isOn();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = seekUp();
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = seekDown();
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    cancelSeek();
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getCurrentChannel();
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    scan();
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result = cancelScan();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isScanning();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isSeeking();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = searchDown();
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = searchUp();
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = searchAll();
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    enableRDS();
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    disableRDS();
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    enableDNS();
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    disableDNS();
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isDNSEnable();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case TRANSACTION_enableAF /*23*/:
                    data.enforceInterface(DESCRIPTOR);
                    enableAF();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_disableAF /*24*/:
                    data.enforceInterface(DESCRIPTOR);
                    disableAF();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setBand /*25*/:
                    data.enforceInterface(DESCRIPTOR);
                    setBand(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setChannelSpacing /*26*/:
                    data.enforceInterface(DESCRIPTOR);
                    setChannelSpacing(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_isBusy /*27*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isBusy();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_isRDSEnable /*28*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isRDSEnable();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case TRANSACTION_isAFEnable /*29*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isAFEnable();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case TRANSACTION_cancelAFSwitching /*30*/:
                    data.enforceInterface(DESCRIPTOR);
                    cancelAFSwitching();
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    long[] _result4 = getLastScanResult();
                    reply.writeNoException();
                    reply.writeLongArray(_result4);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    setStereo();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setMono /*33*/:
                    data.enforceInterface(DESCRIPTOR);
                    setMono();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setVolume /*34*/:
                    data.enforceInterface(DESCRIPTOR);
                    setVolume(data.readLong());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_SkipTuning_Value /*35*/:
                    data.enforceInterface(DESCRIPTOR);
                    SkipTuning_Value();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getVolume /*36*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getVolume();
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case TRANSACTION_isHeadsetPlugged /*37*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isHeadsetPlugged();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case TRANSACTION_isTvOutPlugged /*38*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isTvOutPlugged();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case TRANSACTION_setSpeakerOn /*39*/:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    setSpeakerOn(_arg0);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setRecordMode /*40*/:
                    data.enforceInterface(DESCRIPTOR);
                    setRecordMode(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getMaxVolume /*41*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getMaxVolume();
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case TRANSACTION_isAirPlaneMode /*42*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isAirPlaneMode();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case TRANSACTION_mute /*43*/:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    mute(_arg0);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setDEConstant /*44*/:
                    data.enforceInterface(DESCRIPTOR);
                    setDEConstant(data.readLong());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getCurrentRSSI /*45*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getCurrentRSSI();
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case TRANSACTION_getCurrentSNR /*46*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getCurrentSNR();
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case TRANSACTION_setSeekRSSI /*47*/:
                    data.enforceInterface(DESCRIPTOR);
                    setSeekRSSI(data.readLong());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setSeekSNR /*48*/:
                    data.enforceInterface(DESCRIPTOR);
                    setSeekSNR(data.readLong());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setRSSI_th /*49*/:
                    data.enforceInterface(DESCRIPTOR);
                    setRSSI_th(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setSNR_th /*50*/:
                    data.enforceInterface(DESCRIPTOR);
                    setSNR_th(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setCnt_th /*51*/:
                    data.enforceInterface(DESCRIPTOR);
                    setCnt_th(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setRSSI_th_2 /*52*/:
                    data.enforceInterface(DESCRIPTOR);
                    setRSSI_th_2(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setSNR_th_2 /*53*/:
                    data.enforceInterface(DESCRIPTOR);
                    setSNR_th_2(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setCnt_th_2 /*54*/:
                    data.enforceInterface(DESCRIPTOR);
                    setCnt_th_2(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getRSSI_th /*55*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getRSSI_th();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_getSNR_th /*56*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getSNR_th();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_getCnt_th /*57*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getCnt_th();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_getRSSI_th_2 /*58*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getRSSI_th_2();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_getSNR_th_2 /*59*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getSNR_th_2();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_getCnt_th_2 /*60*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getCnt_th_2();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_on_in_testmode /*61*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result = on_in_testmode();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case TRANSACTION_isBatteryLow /*62*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isBatteryLow();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    setAF_th(data.readInt());
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getAF_th();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_setAFValid_th /*65*/:
                    data.enforceInterface(DESCRIPTOR);
                    setAFValid_th(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getAFValid_th /*66*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getAFValid_th();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_setFMIntenna /*67*/:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    setFMIntenna(_arg0);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setSoftmute /*68*/:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    setSoftmute(_arg0);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getSoftMuteMode /*69*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSoftMuteMode();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case TRANSACTION_setSoftMuteControl /*70*/:
                    data.enforceInterface(DESCRIPTOR);
                    setSoftMuteControl(data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setSearchAlgoType /*71*/:
                    data.enforceInterface(DESCRIPTOR);
                    setSearchAlgoType(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getSearchAlgoType /*72*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getSearchAlgoType();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_setSINRSamples /*73*/:
                    data.enforceInterface(DESCRIPTOR);
                    setSINRSamples(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getSINRSamples /*74*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getSINRSamples();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_setOnChannelThreshold /*75*/:
                    data.enforceInterface(DESCRIPTOR);
                    setOnChannelThreshold(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getOnChannelThreshold /*76*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getOnChannelThreshold();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_setOffChannelThreshold /*77*/:
                    data.enforceInterface(DESCRIPTOR);
                    setOffChannelThreshold(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getOffChannelThreshold /*78*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getOffChannelThreshold();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_setSINRThreshold /*79*/:
                    data.enforceInterface(DESCRIPTOR);
                    setSINRThreshold(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getSINRThreshold /*80*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getSINRThreshold();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_setCFOTh12 /*81*/:
                    data.enforceInterface(DESCRIPTOR);
                    setCFOTh12(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getCFOTh12 /*82*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getCFOTh12();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_setRMSSIFirstStage /*83*/:
                    data.enforceInterface(DESCRIPTOR);
                    setRMSSIFirstStage(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getRMSSIFirstStage /*84*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getRMSSIFirstStage();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_setSINRFirstStage /*85*/:
                    data.enforceInterface(DESCRIPTOR);
                    setSINRFirstStage(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getSINRFirstStage /*86*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getSINRFirstStage();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_setAFRMSSIThreshold /*87*/:
                    data.enforceInterface(DESCRIPTOR);
                    setAFRMSSIThreshold(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getAFRMSSIThreshold /*88*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getAFRMSSIThreshold();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_setAFRMSSISamples /*89*/:
                    data.enforceInterface(DESCRIPTOR);
                    setAFRMSSISamples(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getAFRMSSISamples /*90*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getAFRMSSISamples();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_setGoodChannelRMSSIThreshold /*91*/:
                    data.enforceInterface(DESCRIPTOR);
                    setGoodChannelRMSSIThreshold(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getGoodChannelRMSSIThreshold /*92*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getGoodChannelRMSSIThreshold();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_setHybridSearch /*93*/:
                    data.enforceInterface(DESCRIPTOR);
                    setHybridSearch(data.readString());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getHybridSearch /*94*/:
                    data.enforceInterface(DESCRIPTOR);
                    String _result5 = getHybridSearch();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case TRANSACTION_setSeekDC /*95*/:
                    data.enforceInterface(DESCRIPTOR);
                    setSeekDC(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getSeekDC /*96*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getSeekDC();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_setSeekQA /*97*/:
                    data.enforceInterface(DESCRIPTOR);
                    setSeekQA(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getSeekQA /*98*/:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getSeekQA();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case TRANSACTION_setInternetStreamingMode /*99*/:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    setInternetStreamingMode(_arg0);
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

    void SkipTuning_Value() throws RemoteException;

    void cancelAFSwitching() throws RemoteException;

    boolean cancelScan() throws RemoteException;

    void cancelSeek() throws RemoteException;

    void disableAF() throws RemoteException;

    void disableDNS() throws RemoteException;

    void disableRDS() throws RemoteException;

    void enableAF() throws RemoteException;

    void enableDNS() throws RemoteException;

    void enableRDS() throws RemoteException;

    int getAFRMSSISamples() throws RemoteException;

    int getAFRMSSIThreshold() throws RemoteException;

    int getAFValid_th() throws RemoteException;

    int getAF_th() throws RemoteException;

    int getCFOTh12() throws RemoteException;

    int getCnt_th() throws RemoteException;

    int getCnt_th_2() throws RemoteException;

    long getCurrentChannel() throws RemoteException;

    long getCurrentRSSI() throws RemoteException;

    long getCurrentSNR() throws RemoteException;

    int getGoodChannelRMSSIThreshold() throws RemoteException;

    String getHybridSearch() throws RemoteException;

    long[] getLastScanResult() throws RemoteException;

    long getMaxVolume() throws RemoteException;

    int getOffChannelThreshold() throws RemoteException;

    int getOnChannelThreshold() throws RemoteException;

    int getRMSSIFirstStage() throws RemoteException;

    int getRSSI_th() throws RemoteException;

    int getRSSI_th_2() throws RemoteException;

    int getSINRFirstStage() throws RemoteException;

    int getSINRSamples() throws RemoteException;

    int getSINRThreshold() throws RemoteException;

    int getSNR_th() throws RemoteException;

    int getSNR_th_2() throws RemoteException;

    int getSearchAlgoType() throws RemoteException;

    int getSeekDC() throws RemoteException;

    int getSeekQA() throws RemoteException;

    boolean getSoftMuteMode() throws RemoteException;

    long getVolume() throws RemoteException;

    boolean isAFEnable() throws RemoteException;

    boolean isAirPlaneMode() throws RemoteException;

    boolean isBatteryLow() throws RemoteException;

    int isBusy() throws RemoteException;

    boolean isDNSEnable() throws RemoteException;

    boolean isHeadsetPlugged() throws RemoteException;

    boolean isOn() throws RemoteException;

    boolean isRDSEnable() throws RemoteException;

    boolean isScanning() throws RemoteException;

    boolean isSeeking() throws RemoteException;

    boolean isTvOutPlugged() throws RemoteException;

    void mute(boolean z) throws RemoteException;

    boolean off() throws RemoteException;

    boolean on() throws RemoteException;

    boolean on_in_testmode() throws RemoteException;

    void removeListener(IFMEventListener iFMEventListener) throws RemoteException;

    void scan() throws RemoteException;

    long searchAll() throws RemoteException;

    long searchDown() throws RemoteException;

    long searchUp() throws RemoteException;

    long seekDown() throws RemoteException;

    long seekUp() throws RemoteException;

    void setAFRMSSISamples(int i) throws RemoteException;

    void setAFRMSSIThreshold(int i) throws RemoteException;

    void setAFValid_th(int i) throws RemoteException;

    void setAF_th(int i) throws RemoteException;

    void setBand(int i) throws RemoteException;

    void setCFOTh12(int i) throws RemoteException;

    void setChannelSpacing(int i) throws RemoteException;

    void setCnt_th(int i) throws RemoteException;

    void setCnt_th_2(int i) throws RemoteException;

    void setDEConstant(long j) throws RemoteException;

    void setFMIntenna(boolean z) throws RemoteException;

    void setGoodChannelRMSSIThreshold(int i) throws RemoteException;

    void setHybridSearch(String str) throws RemoteException;

    void setInternetStreamingMode(boolean z) throws RemoteException;

    void setListener(IFMEventListener iFMEventListener) throws RemoteException;

    void setMono() throws RemoteException;

    void setOffChannelThreshold(int i) throws RemoteException;

    void setOnChannelThreshold(int i) throws RemoteException;

    void setRMSSIFirstStage(int i) throws RemoteException;

    void setRSSI_th(int i) throws RemoteException;

    void setRSSI_th_2(int i) throws RemoteException;

    void setRecordMode(int i) throws RemoteException;

    void setSINRFirstStage(int i) throws RemoteException;

    void setSINRSamples(int i) throws RemoteException;

    void setSINRThreshold(int i) throws RemoteException;

    void setSNR_th(int i) throws RemoteException;

    void setSNR_th_2(int i) throws RemoteException;

    void setSearchAlgoType(int i) throws RemoteException;

    void setSeekDC(int i) throws RemoteException;

    void setSeekQA(int i) throws RemoteException;

    void setSeekRSSI(long j) throws RemoteException;

    void setSeekSNR(long j) throws RemoteException;

    void setSoftMuteControl(int i, int i2, int i3) throws RemoteException;

    void setSoftmute(boolean z) throws RemoteException;

    void setSpeakerOn(boolean z) throws RemoteException;

    void setStereo() throws RemoteException;

    void setVolume(long j) throws RemoteException;

    void tune(long j) throws RemoteException;
}
