package android.os;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.IKnoxModeChangeObserver;
import android.content.pm.IKnoxUnlockAction;
import android.content.pm.IPersonaCallback;
import android.content.pm.ISystemPersonaObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PersonaAttribute;
import android.content.pm.PersonaInfo;
import android.content.pm.PersonaNewEvent;
import android.content.pm.PersonaState;
import android.graphics.Bitmap;
import android.net.Uri;
import java.util.List;

public interface IPersonaManager extends IInterface {

    public static abstract class Stub extends Binder implements IPersonaManager {
        private static final String DESCRIPTOR = "android.os.IPersonaManager";
        static final int TRANSACTION_addAppForPersona = 50;
        static final int TRANSACTION_addLockOnImage = 31;
        static final int TRANSACTION_addPackageToInstallWhiteList = 56;
        static final int TRANSACTION_addPackageToNonSecureAppList = 132;
        static final int TRANSACTION_adminLockPersona = 46;
        static final int TRANSACTION_adminUnLockPersona = 47;
        static final int TRANSACTION_canAccess = 105;
        static final int TRANSACTION_clearAppListForPersona = 53;
        static final int TRANSACTION_clearNonSecureAppList = 134;
        static final int TRANSACTION_convertContainerType = 82;
        static final int TRANSACTION_createPersona = 8;
        static final int TRANSACTION_disablePersonaKeyGuard = 66;
        static final int TRANSACTION_doWhenUnlock = 78;
        static final int TRANSACTION_enablePersonaKeyGuard = 67;
        static final int TRANSACTION_exists = 16;
        static final int TRANSACTION_fireEvent = 4;
        static final int TRANSACTION_getAdminUidForPersona = 37;
        static final int TRANSACTION_getAppListForPersona = 52;
        static final int TRANSACTION_getContainerHideUsageStatsApps = 131;
        static final int TRANSACTION_getCustomBadgedResourceIdIconifRequired = 128;
        static final int TRANSACTION_getDefaultQuickSettings = 129;
        static final int TRANSACTION_getDisabledHomeLaunchers = 65;
        static final int TRANSACTION_getFingerCount = 99;
        static final int TRANSACTION_getFingerprintHash = 124;
        static final int TRANSACTION_getFingerprintIndex = 123;
        static final int TRANSACTION_getFocusedUser = 109;
        static final int TRANSACTION_getForegroundUser = 108;
        static final int TRANSACTION_getIsAdminLockedJustBefore = 95;
        static final int TRANSACTION_getIsFingerAsSupplement = 83;
        static final int TRANSACTION_getIsFingerIdentifyFailed = 97;
        static final int TRANSACTION_getIsFingerReset = 93;
        static final int TRANSACTION_getIsFingerTimeout = 91;
        static final int TRANSACTION_getIsQuickAccessUIEnabled = 89;
        static final int TRANSACTION_getIsUnlockedAfterTurnOn = 87;
        static final int TRANSACTION_getKeyguardShowState = 79;
        static final int TRANSACTION_getKnoxIconChanged = 117;
        static final int TRANSACTION_getKnoxIconChangedAsUser = 119;
        static final int TRANSACTION_getKnoxNameChanged = 116;
        static final int TRANSACTION_getKnoxNameChangedAsUser = 118;
        static final int TRANSACTION_getKnoxSecurityTimeout = 106;
        static final int TRANSACTION_getLastKeyguardUnlockTime = 85;
        static final int TRANSACTION_getMoveToKnoxStatus = 24;
        static final int TRANSACTION_getNonSecureAppList = 133;
        static final int TRANSACTION_getNormalizedState = 28;
        static final int TRANSACTION_getPackageInfo = 130;
        static final int TRANSACTION_getPackagesFromInstallWhiteList = 59;
        static final int TRANSACTION_getParentId = 22;
        static final int TRANSACTION_getParentUserForCurrentPersona = 19;
        static final int TRANSACTION_getPasswordHint = 69;
        static final int TRANSACTION_getPersonaBackgroundTime = 32;
        static final int TRANSACTION_getPersonaIcon = 21;
        static final int TRANSACTION_getPersonaIdentification = 36;
        static final int TRANSACTION_getPersonaIds = 48;
        static final int TRANSACTION_getPersonaInfo = 15;
        static final int TRANSACTION_getPersonaSamsungAccount = 40;
        static final int TRANSACTION_getPersonaType = 26;
        static final int TRANSACTION_getPersonas = 14;
        static final int TRANSACTION_getPersonasForCreator = 18;
        static final int TRANSACTION_getPersonasForUser = 17;
        static final int TRANSACTION_getPreviousState = 2;
        static final int TRANSACTION_getScreenOffTime = 61;
        static final int TRANSACTION_getState = 1;
        static final int TRANSACTION_getUserManagedPersonas = 42;
        static final int TRANSACTION_handleHomeShow = 44;
        static final int TRANSACTION_hideScrim = 81;
        static final int TRANSACTION_inState = 3;
        static final int TRANSACTION_installApplications = 29;
        static final int TRANSACTION_isAttribute = 6;
        static final int TRANSACTION_isEnabledFingerprintIndex = 122;
        static final int TRANSACTION_isFOTAUpgrade = 12;
        static final int TRANSACTION_isFingerLockscreenActivated = 102;
        static final int TRANSACTION_isFingerSupplementActivated = 101;
        static final int TRANSACTION_isFotaUpgradeVersionChanged = 135;
        static final int TRANSACTION_isKioskContainerExistOnDevice = 71;
        static final int TRANSACTION_isKioskModeEnabled = 70;
        static final int TRANSACTION_isKnoxKeyguardShown = 80;
        static final int TRANSACTION_isNFCAllowed = 120;
        static final int TRANSACTION_isPackageInInstallWhiteList = 58;
        static final int TRANSACTION_isResetPersonaOnRebootEnabled = 75;
        static final int TRANSACTION_isSessionExpired = 45;
        static final int TRANSACTION_launchPersonaHome = 10;
        static final int TRANSACTION_lockPersona = 43;
        static final int TRANSACTION_markForRemoval = 38;
        static final int TRANSACTION_mountOldContainer = 112;
        static final int TRANSACTION_needToSkipResetOnReboot = 13;
        static final int TRANSACTION_notifyKeyguardShow = 77;
        static final int TRANSACTION_onKeyguardBackPressed = 111;
        static final int TRANSACTION_onWakeLockChange = 64;
        static final int TRANSACTION_refreshTimer = 62;
        static final int TRANSACTION_registerKnoxModeChangeObserver = 33;
        static final int TRANSACTION_registerSystemPersonaObserver = 34;
        static final int TRANSACTION_registerUser = 7;
        static final int TRANSACTION_removeAppForPersona = 51;
        static final int TRANSACTION_removeKnoxAppsinFota = 136;
        static final int TRANSACTION_removePackageFromInstallWhiteList = 57;
        static final int TRANSACTION_removePersona = 11;
        static final int TRANSACTION_resetPassword = 55;
        static final int TRANSACTION_resetPersona = 35;
        static final int TRANSACTION_resetPersonaOnReboot = 73;
        static final int TRANSACTION_resetPersonaPassword = 126;
        static final int TRANSACTION_savePasswordInTima = 54;
        static final int TRANSACTION_setAccessPermission = 104;
        static final int TRANSACTION_setAttribute = 5;
        static final int TRANSACTION_setBackPressed = 72;
        static final int TRANSACTION_setFingerCount = 100;
        static final int TRANSACTION_setFingerprintHash = 125;
        static final int TRANSACTION_setFingerprintIndex = 121;
        static final int TRANSACTION_setFocusedUser = 110;
        static final int TRANSACTION_setFsMountState = 68;
        static final int TRANSACTION_setIsAdminLockedJustBefore = 96;
        static final int TRANSACTION_setIsFingerAsSupplement = 84;
        static final int TRANSACTION_setIsFingerIdentifyFailed = 98;
        static final int TRANSACTION_setIsFingerReset = 94;
        static final int TRANSACTION_setIsFingerTimeout = 92;
        static final int TRANSACTION_setIsQuickAccessUIEnabled = 90;
        static final int TRANSACTION_setIsUnlockedAfterTurnOn = 88;
        static final int TRANSACTION_setKnoxBackupPin = 115;
        static final int TRANSACTION_setKnoxSecurityTimeout = 107;
        static final int TRANSACTION_setLastKeyguardUnlockTime = 86;
        static final int TRANSACTION_setMaximumScreenOffTimeoutFromDeviceAdmin = 60;
        static final int TRANSACTION_setMoveToKnoxStatus = 23;
        static final int TRANSACTION_setPersonaIcon = 20;
        static final int TRANSACTION_setPersonaName = 25;
        static final int TRANSACTION_setPersonaSamsungAccount = 41;
        static final int TRANSACTION_setPersonaType = 27;
        static final int TRANSACTION_setShownHelp = 103;
        static final int TRANSACTION_settingSyncAllowed = 49;
        static final int TRANSACTION_setupComplete = 127;
        static final int TRANSACTION_showKeyguard = 76;
        static final int TRANSACTION_switchPersonaAndLaunch = 9;
        static final int TRANSACTION_unInstallSystemApplications = 30;
        static final int TRANSACTION_unmarkForRemoval = 39;
        static final int TRANSACTION_unmountOldContainer = 113;
        static final int TRANSACTION_updatePersonaInfo = 74;
        static final int TRANSACTION_userActivity = 63;
        static final int TRANSACTION_verifyKnoxBackupPin = 114;

        private static class Proxy implements IPersonaManager {
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

            public PersonaState getState(int id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    PersonaState _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (PersonaState) PersonaState.CREATOR.createFromParcel(_reply);
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

            public PersonaState getPreviousState(int id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    PersonaState _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (PersonaState) PersonaState.CREATOR.createFromParcel(_reply);
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

            public boolean inState(PersonaState state, int id) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (state != null) {
                        _data.writeInt(1);
                        state.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(id);
                    this.mRemote.transact(3, _data, _reply, 0);
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

            public PersonaState fireEvent(PersonaNewEvent event, int id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    PersonaState _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(id);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (PersonaState) PersonaState.CREATOR.createFromParcel(_reply);
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

            public boolean setAttribute(PersonaAttribute attribute, boolean enabled, int id) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (attribute != null) {
                        _data.writeInt(1);
                        attribute.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeInt(id);
                    this.mRemote.transact(5, _data, _reply, 0);
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

            public boolean isAttribute(PersonaAttribute attribute, int id) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (attribute != null) {
                        _data.writeInt(1);
                        attribute.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(id);
                    this.mRemote.transact(6, _data, _reply, 0);
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

            public boolean registerUser(IPersonaCallback client) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
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

            public int createPersona(String name, String password, long flags, String personaType, String admin, Uri setupWizardApkUri, String mdmSecretKey, int lockType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(password);
                    _data.writeLong(flags);
                    _data.writeString(personaType);
                    _data.writeString(admin);
                    if (setupWizardApkUri != null) {
                        _data.writeInt(1);
                        setupWizardApkUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(mdmSecretKey);
                    _data.writeInt(lockType);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean switchPersonaAndLaunch(int personaId, Intent intent) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, _reply, 0);
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

            public boolean launchPersonaHome(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
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

            public int removePersona(int personaHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaHandle);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isFOTAUpgrade() throws RemoteException {
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

            public boolean needToSkipResetOnReboot() throws RemoteException {
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

            public List<PersonaInfo> getPersonas(boolean excludeDying) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (excludeDying) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    List<PersonaInfo> _result = _reply.createTypedArrayList(PersonaInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PersonaInfo getPersonaInfo(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    PersonaInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (PersonaInfo) PersonaInfo.CREATOR.createFromParcel(_reply);
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

            public boolean exists(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
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

            public List<PersonaInfo> getPersonasForUser(int userId, boolean excludeDying) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (excludeDying) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    List<PersonaInfo> _result = _reply.createTypedArrayList(PersonaInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PersonaInfo> getPersonasForCreator(int creatorUid, boolean excludeDying) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(creatorUid);
                    if (excludeDying) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    List<PersonaInfo> _result = _reply.createTypedArrayList(PersonaInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getParentUserForCurrentPersona() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPersonaIcon(int personaHandle, Bitmap icon) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaHandle);
                    if (icon != null) {
                        _data.writeInt(1);
                        icon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bitmap getPersonaIcon(int personaHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Bitmap _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaHandle);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bitmap) Bitmap.CREATOR.createFromParcel(_reply);
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

            public int getParentId(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMoveToKnoxStatus(boolean isProgressing) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (isProgressing) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getMoveToKnoxStatus() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public void setPersonaName(int personaId, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeString(name);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getPersonaType(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPersonaType(int personaId, String personaType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeString(personaType);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getNormalizedState(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean installApplications(int personaId, List<String> packages) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeStringList(packages);
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

            public int unInstallSystemApplications(int personaId, List<String> packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeStringList(packages);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bitmap addLockOnImage(Bitmap origDrawable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Bitmap _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (origDrawable != null) {
                        _data.writeInt(1);
                        origDrawable.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bitmap) Bitmap.CREATOR.createFromParcel(_reply);
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

            public long getPersonaBackgroundTime(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean registerKnoxModeChangeObserver(IKnoxModeChangeObserver mKnoxModeChangeObserver) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(mKnoxModeChangeObserver != null ? mKnoxModeChangeObserver.asBinder() : null);
                    this.mRemote.transact(33, _data, _reply, 0);
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

            public boolean registerSystemPersonaObserver(ISystemPersonaObserver mISystemPersonaObserver) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(mISystemPersonaObserver != null ? mISystemPersonaObserver.asBinder() : null);
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

            public int resetPersona(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getPersonaIdentification(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAdminUidForPersona(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void markForRemoval(int personaId, ComponentName removalActivity) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (removalActivity != null) {
                        _data.writeInt(1);
                        removalActivity.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unmarkForRemoval(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getPersonaSamsungAccount(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPersonaSamsungAccount(int personaId, String samsungAccount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeString(samsungAccount);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PersonaInfo> getUserManagedPersonas(boolean excludeDying) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (excludeDying) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    List<PersonaInfo> _result = _reply.createTypedArrayList(PersonaInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void lockPersona(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean handleHomeShow() throws RemoteException {
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

            public boolean isSessionExpired(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(45, _data, _reply, 0);
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

            public boolean adminLockPersona(int personaId, String password) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeString(password);
                    this.mRemote.transact(46, _data, _reply, 0);
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

            public boolean adminUnLockPersona(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(47, _data, _reply, 0);
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

            public int[] getPersonaIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean settingSyncAllowed(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(49, _data, _reply, 0);
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

            public void addAppForPersona(String type, String packageName, int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(type);
                    _data.writeString(packageName);
                    _data.writeInt(personaId);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeAppForPersona(String type, String packageName, int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(type);
                    _data.writeString(packageName);
                    _data.writeInt(personaId);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getAppListForPersona(String type, int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(type);
                    _data.writeInt(personaId);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearAppListForPersona(String type, int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(type);
                    _data.writeInt(personaId);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean savePasswordInTima(int personaId, String newPassword) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeString(newPassword);
                    this.mRemote.transact(54, _data, _reply, 0);
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

            public boolean resetPassword(String newPassword) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(newPassword);
                    this.mRemote.transact(55, _data, _reply, 0);
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

            public void addPackageToInstallWhiteList(String packageName, int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(personaId);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removePackageFromInstallWhiteList(String packageName, int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(personaId);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isPackageInInstallWhiteList(String packageName, int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(personaId);
                    this.mRemote.transact(58, _data, _reply, 0);
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

            public List<String> getPackagesFromInstallWhiteList(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMaximumScreenOffTimeoutFromDeviceAdmin(long timeMs, int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(timeMs);
                    _data.writeInt(personaId);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getScreenOffTime(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void refreshTimer(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void userActivity(int event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(event);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onWakeLockChange(boolean isAcquired, int flags, int ownerUid, int ownerPid, String packageName) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (isAcquired) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(flags);
                    _data.writeInt(ownerUid);
                    _data.writeInt(ownerPid);
                    _data.writeString(packageName);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getDisabledHomeLaunchers(int personaId, boolean clearList) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (clearList) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean disablePersonaKeyGuard(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(66, _data, _reply, 0);
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

            public boolean enablePersonaKeyGuard(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(67, _data, _reply, 0);
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

            public void setFsMountState(int personaId, boolean mountState) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (mountState) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getPasswordHint() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isKioskModeEnabled(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(70, _data, _reply, 0);
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

            public boolean isKioskContainerExistOnDevice() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(71, _data, _reply, 0);
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

            public void setBackPressed(int personaId, boolean isBackPressed) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (isBackPressed) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean resetPersonaOnReboot(int personaId, boolean enable) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (enable) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(73, _data, _reply, 0);
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

            public boolean updatePersonaInfo(int personaId, String packageName, int adminUid, int creatorUid) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeString(packageName);
                    _data.writeInt(adminUid);
                    _data.writeInt(creatorUid);
                    this.mRemote.transact(74, _data, _reply, 0);
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

            public boolean isResetPersonaOnRebootEnabled(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(75, _data, _reply, 0);
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

            public void showKeyguard(int personaId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeInt(flags);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyKeyguardShow(int personaId, boolean isShown) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (isShown) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void doWhenUnlock(int userId, IKnoxUnlockAction r) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(r != null ? r.asBinder() : null);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getKeyguardShowState(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(79, _data, _reply, 0);
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

            public boolean isKnoxKeyguardShown(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(80, _data, _reply, 0);
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

            public void hideScrim() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void convertContainerType(int personaId, int containerType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeInt(containerType);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getIsFingerAsSupplement(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(83, _data, _reply, 0);
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

            public void setIsFingerAsSupplement(int personaId, boolean isFingerprintAsSupplement) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (isFingerprintAsSupplement) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getLastKeyguardUnlockTime(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(85, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setLastKeyguardUnlockTime(int personaId, long lastKeyguardUnlockTime) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeLong(lastKeyguardUnlockTime);
                    this.mRemote.transact(86, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getIsUnlockedAfterTurnOn(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(87, _data, _reply, 0);
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

            public void setIsUnlockedAfterTurnOn(int personaId, boolean isUnlockBefore) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (isUnlockBefore) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(88, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getIsQuickAccessUIEnabled(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(89, _data, _reply, 0);
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

            public void setIsQuickAccessUIEnabled(int personaId, boolean isEnabledBefore) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (isEnabledBefore) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(90, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getIsFingerTimeout(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(91, _data, _reply, 0);
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

            public void setIsFingerTimeout(int personaId, boolean isFingerTimeout) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (isFingerTimeout) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(92, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getIsFingerReset(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(93, _data, _reply, 0);
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

            public void setIsFingerReset(int personaId, boolean isFingerReset) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (isFingerReset) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getIsAdminLockedJustBefore(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(95, _data, _reply, 0);
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

            public void setIsAdminLockedJustBefore(int personaId, boolean isAdminLockedJustBefore) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (isAdminLockedJustBefore) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getIsFingerIdentifyFailed(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(97, _data, _reply, 0);
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

            public void setIsFingerIdentifyFailed(int personaId, boolean isFingerIdentifyFailed) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (isFingerIdentifyFailed) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(98, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getFingerCount(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFingerCount(int personaId, int fingerCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeInt(fingerCount);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isFingerSupplementActivated(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(101, _data, _reply, 0);
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

            public boolean isFingerLockscreenActivated(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(102, _data, _reply, 0);
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

            public void setShownHelp(int personaId, int containerType, boolean value) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeInt(containerType);
                    if (value) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAccessPermission(String type, int personaId, boolean canAccess) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(type);
                    _data.writeInt(personaId);
                    if (canAccess) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(104, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean canAccess(String type, int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(type);
                    _data.writeInt(personaId);
                    this.mRemote.transact(105, _data, _reply, 0);
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

            public int getKnoxSecurityTimeout(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(106, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setKnoxSecurityTimeout(int personaId, int timeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeInt(timeout);
                    this.mRemote.transact(107, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getForegroundUser() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(108, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getFocusedUser() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(109, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFocusedUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(110, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onKeyguardBackPressed(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(111, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean mountOldContainer(String password, String srcPath, String dstPath, int excludeMediaTypes, int containerId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(password);
                    _data.writeString(srcPath);
                    _data.writeString(dstPath);
                    _data.writeInt(excludeMediaTypes);
                    _data.writeInt(containerId);
                    this.mRemote.transact(112, _data, _reply, 0);
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

            public boolean unmountOldContainer(String dstPath) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(dstPath);
                    this.mRemote.transact(113, _data, _reply, 0);
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

            public boolean verifyKnoxBackupPin(int personaId, String backupPin) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeString(backupPin);
                    this.mRemote.transact(114, _data, _reply, 0);
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

            public void setKnoxBackupPin(int personaId, String backupPin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeString(backupPin);
                    this.mRemote.transact(115, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getKnoxNameChanged(String component, int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(component);
                    _data.writeInt(personaId);
                    this.mRemote.transact(116, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bitmap getKnoxIconChanged(String packageName, int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Bitmap _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(personaId);
                    this.mRemote.transact(117, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bitmap) Bitmap.CREATOR.createFromParcel(_reply);
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

            public String getKnoxNameChangedAsUser(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(118, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bitmap getKnoxIconChangedAsUser(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Bitmap _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(119, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bitmap) Bitmap.CREATOR.createFromParcel(_reply);
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

            public boolean isNFCAllowed(int userId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(120, _data, _reply, 0);
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

            public void setFingerprintIndex(int personaId, boolean enable, int[] list) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeIntArray(list);
                    this.mRemote.transact(121, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isEnabledFingerprintIndex(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(122, _data, _reply, 0);
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

            public int[] getFingerprintIndex(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(123, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getFingerprintHash(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(124, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFingerprintHash(int personaId, List<String> list) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeStringList(list);
                    this.mRemote.transact(125, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resetPersonaPassword(int personaId, String pwdResetToken, int timeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeString(pwdResetToken);
                    _data.writeInt(timeout);
                    this.mRemote.transact(126, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setupComplete(int personaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(127, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCustomBadgedResourceIdIconifRequired(String pkgName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(userId);
                    this.mRemote.transact(128, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDefaultQuickSettings() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(129, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PackageInfo getPackageInfo(String packageName, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    PackageInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    this.mRemote.transact(130, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (PackageInfo) PackageInfo.CREATOR.createFromParcel(_reply);
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

            public List<String> getContainerHideUsageStatsApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(131, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addPackageToNonSecureAppList(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(132, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getNonSecureAppList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(133, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearNonSecureAppList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(134, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isFotaUpgradeVersionChanged() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(135, _data, _reply, 0);
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

            public void removeKnoxAppsinFota(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(136, _data, _reply, 0);
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

        public static IPersonaManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPersonaManager)) {
                return new Proxy(obj);
            }
            return (IPersonaManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            PersonaState _result;
            boolean _result2;
            PersonaAttribute _arg0;
            int _result3;
            int _arg02;
            List<PersonaInfo> _result4;
            Bitmap _result5;
            String _result6;
            long _result7;
            int[] _result8;
            List<String> _result9;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getState(data.readInt());
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
                    _result = getPreviousState(data.readInt());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 3:
                    PersonaState _arg03;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (PersonaState) PersonaState.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    _result2 = inState(_arg03, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 4:
                    PersonaNewEvent _arg04;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = (PersonaNewEvent) PersonaNewEvent.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    _result = fireEvent(_arg04, data.readInt());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (PersonaAttribute) PersonaAttribute.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result2 = setAttribute(_arg0, data.readInt() != 0, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (PersonaAttribute) PersonaAttribute.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result2 = isAttribute(_arg0, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = registerUser(android.content.pm.IPersonaCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 8:
                    Uri _arg5;
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    String _arg1 = data.readString();
                    long _arg2 = data.readLong();
                    String _arg3 = data.readString();
                    String _arg4 = data.readString();
                    if (data.readInt() != 0) {
                        _arg5 = (Uri) Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg5 = null;
                    }
                    _result3 = createPersona(_arg05, _arg1, _arg2, _arg3, _arg4, _arg5, data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 9:
                    Intent _arg12;
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    _result2 = switchPersonaAndLaunch(_arg02, _arg12);
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = launchPersonaHome(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = removePersona(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isFOTAUpgrade();
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = needToSkipResetOnReboot();
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getPersonas(data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeTypedList(_result4);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    PersonaInfo _result10 = getPersonaInfo(data.readInt());
                    reply.writeNoException();
                    if (_result10 != null) {
                        reply.writeInt(1);
                        _result10.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = exists(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getPersonasForUser(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeTypedList(_result4);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getPersonasForCreator(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeTypedList(_result4);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getParentUserForCurrentPersona();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 20:
                    Bitmap _arg13;
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg13 = (Bitmap) Bitmap.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    setPersonaIcon(_arg02, _arg13);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getPersonaIcon(data.readInt());
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getParentId(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    setMoveToKnoxStatus(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getMoveToKnoxStatus();
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    setPersonaName(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getPersonaType(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    setPersonaType(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getNormalizedState(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = installApplications(data.readInt(), data.createStringArrayList());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = unInstallSystemApplications(data.readInt(), data.createStringArrayList());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 31:
                    Bitmap _arg06;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = (Bitmap) Bitmap.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    _result5 = addLockOnImage(_arg06);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = getPersonaBackgroundTime(data.readInt());
                    reply.writeNoException();
                    reply.writeLong(_result7);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = registerKnoxModeChangeObserver(android.content.pm.IKnoxModeChangeObserver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = registerSystemPersonaObserver(android.content.pm.ISystemPersonaObserver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = resetPersona(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getPersonaIdentification(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getAdminUidForPersona(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 38:
                    ComponentName _arg14;
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg14 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    markForRemoval(_arg02, _arg14);
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    unmarkForRemoval(data.readInt());
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getPersonaSamsungAccount(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    setPersonaSamsungAccount(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getUserManagedPersonas(data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeTypedList(_result4);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    lockPersona(data.readInt());
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = handleHomeShow();
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isSessionExpired(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = adminLockPersona(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = adminUnLockPersona(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    _result8 = getPersonaIds();
                    reply.writeNoException();
                    reply.writeIntArray(_result8);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = settingSyncAllowed(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    addAppForPersona(data.readString(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    removeAppForPersona(data.readString(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    _result9 = getAppListForPersona(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeStringList(_result9);
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    clearAppListForPersona(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = savePasswordInTima(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = resetPassword(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    addPackageToInstallWhiteList(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    removePackageFromInstallWhiteList(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isPackageInInstallWhiteList(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    _result9 = getPackagesFromInstallWhiteList(data.readInt());
                    reply.writeNoException();
                    reply.writeStringList(_result9);
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    setMaximumScreenOffTimeoutFromDeviceAdmin(data.readLong(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = getScreenOffTime(data.readInt());
                    reply.writeNoException();
                    reply.writeLong(_result7);
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    refreshTimer(data.readInt());
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    userActivity(data.readInt());
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    onWakeLockChange(data.readInt() != 0, data.readInt(), data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    _result9 = getDisabledHomeLaunchers(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeStringList(_result9);
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = disablePersonaKeyGuard(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = enablePersonaKeyGuard(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    setFsMountState(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getPasswordHint();
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isKioskModeEnabled(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isKioskContainerExistOnDevice();
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    setBackPressed(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = resetPersonaOnReboot(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = updatePersonaInfo(data.readInt(), data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isResetPersonaOnRebootEnabled(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    showKeyguard(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    notifyKeyguardShow(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    doWhenUnlock(data.readInt(), android.content.pm.IKnoxUnlockAction.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getKeyguardShowState(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isKnoxKeyguardShown(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    hideScrim();
                    reply.writeNoException();
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    convertContainerType(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getIsFingerAsSupplement(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    setIsFingerAsSupplement(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = getLastKeyguardUnlockTime(data.readInt());
                    reply.writeNoException();
                    reply.writeLong(_result7);
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    setLastKeyguardUnlockTime(data.readInt(), data.readLong());
                    reply.writeNoException();
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getIsUnlockedAfterTurnOn(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    setIsUnlockedAfterTurnOn(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getIsQuickAccessUIEnabled(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    setIsQuickAccessUIEnabled(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getIsFingerTimeout(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    setIsFingerTimeout(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getIsFingerReset(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    setIsFingerReset(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getIsAdminLockedJustBefore(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    setIsAdminLockedJustBefore(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getIsFingerIdentifyFailed(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    setIsFingerIdentifyFailed(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getFingerCount(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    setFingerCount(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isFingerSupplementActivated(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isFingerLockscreenActivated(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    setShownHelp(data.readInt(), data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    setAccessPermission(data.readString(), data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = canAccess(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 106:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getKnoxSecurityTimeout(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    setKnoxSecurityTimeout(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getForegroundUser();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getFocusedUser();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    setFocusedUser(data.readInt());
                    reply.writeNoException();
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    onKeyguardBackPressed(data.readInt());
                    reply.writeNoException();
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = mountOldContainer(data.readString(), data.readString(), data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 113:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = unmountOldContainer(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = verifyKnoxBackupPin(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    setKnoxBackupPin(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 116:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getKnoxNameChanged(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getKnoxIconChanged(data.readString(), data.readInt());
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 118:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getKnoxNameChangedAsUser(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 119:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getKnoxIconChangedAsUser(data.readInt());
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 120:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isNFCAllowed(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    setFingerprintIndex(data.readInt(), data.readInt() != 0, data.createIntArray());
                    reply.writeNoException();
                    return true;
                case 122:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isEnabledFingerprintIndex(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 123:
                    data.enforceInterface(DESCRIPTOR);
                    _result8 = getFingerprintIndex(data.readInt());
                    reply.writeNoException();
                    reply.writeIntArray(_result8);
                    return true;
                case 124:
                    data.enforceInterface(DESCRIPTOR);
                    _result9 = getFingerprintHash(data.readInt());
                    reply.writeNoException();
                    reply.writeStringList(_result9);
                    return true;
                case 125:
                    data.enforceInterface(DESCRIPTOR);
                    setFingerprintHash(data.readInt(), data.createStringArrayList());
                    reply.writeNoException();
                    return true;
                case 126:
                    data.enforceInterface(DESCRIPTOR);
                    resetPersonaPassword(data.readInt(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 127:
                    data.enforceInterface(DESCRIPTOR);
                    setupComplete(data.readInt());
                    reply.writeNoException();
                    return true;
                case 128:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getCustomBadgedResourceIdIconifRequired(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 129:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getDefaultQuickSettings();
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 130:
                    data.enforceInterface(DESCRIPTOR);
                    PackageInfo _result11 = getPackageInfo(data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    if (_result11 != null) {
                        reply.writeInt(1);
                        _result11.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 131:
                    data.enforceInterface(DESCRIPTOR);
                    _result9 = getContainerHideUsageStatsApps();
                    reply.writeNoException();
                    reply.writeStringList(_result9);
                    return true;
                case 132:
                    data.enforceInterface(DESCRIPTOR);
                    addPackageToNonSecureAppList(data.readString());
                    reply.writeNoException();
                    return true;
                case 133:
                    data.enforceInterface(DESCRIPTOR);
                    _result9 = getNonSecureAppList();
                    reply.writeNoException();
                    reply.writeStringList(_result9);
                    return true;
                case 134:
                    data.enforceInterface(DESCRIPTOR);
                    clearNonSecureAppList();
                    reply.writeNoException();
                    return true;
                case 135:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isFotaUpgradeVersionChanged();
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 136:
                    data.enforceInterface(DESCRIPTOR);
                    removeKnoxAppsinFota(data.readInt());
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

    void addAppForPersona(String str, String str2, int i) throws RemoteException;

    Bitmap addLockOnImage(Bitmap bitmap) throws RemoteException;

    void addPackageToInstallWhiteList(String str, int i) throws RemoteException;

    void addPackageToNonSecureAppList(String str) throws RemoteException;

    boolean adminLockPersona(int i, String str) throws RemoteException;

    boolean adminUnLockPersona(int i) throws RemoteException;

    boolean canAccess(String str, int i) throws RemoteException;

    void clearAppListForPersona(String str, int i) throws RemoteException;

    void clearNonSecureAppList() throws RemoteException;

    void convertContainerType(int i, int i2) throws RemoteException;

    int createPersona(String str, String str2, long j, String str3, String str4, Uri uri, String str5, int i) throws RemoteException;

    boolean disablePersonaKeyGuard(int i) throws RemoteException;

    void doWhenUnlock(int i, IKnoxUnlockAction iKnoxUnlockAction) throws RemoteException;

    boolean enablePersonaKeyGuard(int i) throws RemoteException;

    boolean exists(int i) throws RemoteException;

    PersonaState fireEvent(PersonaNewEvent personaNewEvent, int i) throws RemoteException;

    int getAdminUidForPersona(int i) throws RemoteException;

    List<String> getAppListForPersona(String str, int i) throws RemoteException;

    List<String> getContainerHideUsageStatsApps() throws RemoteException;

    int getCustomBadgedResourceIdIconifRequired(String str, int i) throws RemoteException;

    String getDefaultQuickSettings() throws RemoteException;

    List<String> getDisabledHomeLaunchers(int i, boolean z) throws RemoteException;

    int getFingerCount(int i) throws RemoteException;

    List<String> getFingerprintHash(int i) throws RemoteException;

    int[] getFingerprintIndex(int i) throws RemoteException;

    int getFocusedUser() throws RemoteException;

    int getForegroundUser() throws RemoteException;

    boolean getIsAdminLockedJustBefore(int i) throws RemoteException;

    boolean getIsFingerAsSupplement(int i) throws RemoteException;

    boolean getIsFingerIdentifyFailed(int i) throws RemoteException;

    boolean getIsFingerReset(int i) throws RemoteException;

    boolean getIsFingerTimeout(int i) throws RemoteException;

    boolean getIsQuickAccessUIEnabled(int i) throws RemoteException;

    boolean getIsUnlockedAfterTurnOn(int i) throws RemoteException;

    boolean getKeyguardShowState(int i) throws RemoteException;

    Bitmap getKnoxIconChanged(String str, int i) throws RemoteException;

    Bitmap getKnoxIconChangedAsUser(int i) throws RemoteException;

    String getKnoxNameChanged(String str, int i) throws RemoteException;

    String getKnoxNameChangedAsUser(int i) throws RemoteException;

    int getKnoxSecurityTimeout(int i) throws RemoteException;

    long getLastKeyguardUnlockTime(int i) throws RemoteException;

    boolean getMoveToKnoxStatus() throws RemoteException;

    List<String> getNonSecureAppList() throws RemoteException;

    int getNormalizedState(int i) throws RemoteException;

    PackageInfo getPackageInfo(String str, int i, int i2) throws RemoteException;

    List<String> getPackagesFromInstallWhiteList(int i) throws RemoteException;

    int getParentId(int i) throws RemoteException;

    int getParentUserForCurrentPersona() throws RemoteException;

    String getPasswordHint() throws RemoteException;

    long getPersonaBackgroundTime(int i) throws RemoteException;

    Bitmap getPersonaIcon(int i) throws RemoteException;

    String getPersonaIdentification(int i) throws RemoteException;

    int[] getPersonaIds() throws RemoteException;

    PersonaInfo getPersonaInfo(int i) throws RemoteException;

    String getPersonaSamsungAccount(int i) throws RemoteException;

    String getPersonaType(int i) throws RemoteException;

    List<PersonaInfo> getPersonas(boolean z) throws RemoteException;

    List<PersonaInfo> getPersonasForCreator(int i, boolean z) throws RemoteException;

    List<PersonaInfo> getPersonasForUser(int i, boolean z) throws RemoteException;

    PersonaState getPreviousState(int i) throws RemoteException;

    long getScreenOffTime(int i) throws RemoteException;

    PersonaState getState(int i) throws RemoteException;

    List<PersonaInfo> getUserManagedPersonas(boolean z) throws RemoteException;

    boolean handleHomeShow() throws RemoteException;

    void hideScrim() throws RemoteException;

    boolean inState(PersonaState personaState, int i) throws RemoteException;

    boolean installApplications(int i, List<String> list) throws RemoteException;

    boolean isAttribute(PersonaAttribute personaAttribute, int i) throws RemoteException;

    boolean isEnabledFingerprintIndex(int i) throws RemoteException;

    boolean isFOTAUpgrade() throws RemoteException;

    boolean isFingerLockscreenActivated(int i) throws RemoteException;

    boolean isFingerSupplementActivated(int i) throws RemoteException;

    boolean isFotaUpgradeVersionChanged() throws RemoteException;

    boolean isKioskContainerExistOnDevice() throws RemoteException;

    boolean isKioskModeEnabled(int i) throws RemoteException;

    boolean isKnoxKeyguardShown(int i) throws RemoteException;

    boolean isNFCAllowed(int i) throws RemoteException;

    boolean isPackageInInstallWhiteList(String str, int i) throws RemoteException;

    boolean isResetPersonaOnRebootEnabled(int i) throws RemoteException;

    boolean isSessionExpired(int i) throws RemoteException;

    boolean launchPersonaHome(int i) throws RemoteException;

    void lockPersona(int i) throws RemoteException;

    void markForRemoval(int i, ComponentName componentName) throws RemoteException;

    boolean mountOldContainer(String str, String str2, String str3, int i, int i2) throws RemoteException;

    boolean needToSkipResetOnReboot() throws RemoteException;

    void notifyKeyguardShow(int i, boolean z) throws RemoteException;

    void onKeyguardBackPressed(int i) throws RemoteException;

    void onWakeLockChange(boolean z, int i, int i2, int i3, String str) throws RemoteException;

    void refreshTimer(int i) throws RemoteException;

    boolean registerKnoxModeChangeObserver(IKnoxModeChangeObserver iKnoxModeChangeObserver) throws RemoteException;

    boolean registerSystemPersonaObserver(ISystemPersonaObserver iSystemPersonaObserver) throws RemoteException;

    boolean registerUser(IPersonaCallback iPersonaCallback) throws RemoteException;

    void removeAppForPersona(String str, String str2, int i) throws RemoteException;

    void removeKnoxAppsinFota(int i) throws RemoteException;

    void removePackageFromInstallWhiteList(String str, int i) throws RemoteException;

    int removePersona(int i) throws RemoteException;

    boolean resetPassword(String str) throws RemoteException;

    int resetPersona(int i) throws RemoteException;

    boolean resetPersonaOnReboot(int i, boolean z) throws RemoteException;

    void resetPersonaPassword(int i, String str, int i2) throws RemoteException;

    boolean savePasswordInTima(int i, String str) throws RemoteException;

    void setAccessPermission(String str, int i, boolean z) throws RemoteException;

    boolean setAttribute(PersonaAttribute personaAttribute, boolean z, int i) throws RemoteException;

    void setBackPressed(int i, boolean z) throws RemoteException;

    void setFingerCount(int i, int i2) throws RemoteException;

    void setFingerprintHash(int i, List<String> list) throws RemoteException;

    void setFingerprintIndex(int i, boolean z, int[] iArr) throws RemoteException;

    void setFocusedUser(int i) throws RemoteException;

    void setFsMountState(int i, boolean z) throws RemoteException;

    void setIsAdminLockedJustBefore(int i, boolean z) throws RemoteException;

    void setIsFingerAsSupplement(int i, boolean z) throws RemoteException;

    void setIsFingerIdentifyFailed(int i, boolean z) throws RemoteException;

    void setIsFingerReset(int i, boolean z) throws RemoteException;

    void setIsFingerTimeout(int i, boolean z) throws RemoteException;

    void setIsQuickAccessUIEnabled(int i, boolean z) throws RemoteException;

    void setIsUnlockedAfterTurnOn(int i, boolean z) throws RemoteException;

    void setKnoxBackupPin(int i, String str) throws RemoteException;

    void setKnoxSecurityTimeout(int i, int i2) throws RemoteException;

    void setLastKeyguardUnlockTime(int i, long j) throws RemoteException;

    void setMaximumScreenOffTimeoutFromDeviceAdmin(long j, int i) throws RemoteException;

    void setMoveToKnoxStatus(boolean z) throws RemoteException;

    void setPersonaIcon(int i, Bitmap bitmap) throws RemoteException;

    void setPersonaName(int i, String str) throws RemoteException;

    void setPersonaSamsungAccount(int i, String str) throws RemoteException;

    void setPersonaType(int i, String str) throws RemoteException;

    void setShownHelp(int i, int i2, boolean z) throws RemoteException;

    boolean settingSyncAllowed(int i) throws RemoteException;

    void setupComplete(int i) throws RemoteException;

    void showKeyguard(int i, int i2) throws RemoteException;

    boolean switchPersonaAndLaunch(int i, Intent intent) throws RemoteException;

    int unInstallSystemApplications(int i, List<String> list) throws RemoteException;

    void unmarkForRemoval(int i) throws RemoteException;

    boolean unmountOldContainer(String str) throws RemoteException;

    boolean updatePersonaInfo(int i, String str, int i2, int i3) throws RemoteException;

    void userActivity(int i) throws RemoteException;

    boolean verifyKnoxBackupPin(int i, String str) throws RemoteException;
}
