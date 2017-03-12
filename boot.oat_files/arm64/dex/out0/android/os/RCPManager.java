package android.os;

import android.app.Command;
import android.content.Context;
import android.content.CustomCursor;
import android.content.ICommandExeCallBack;
import android.content.IProviderCallBack;
import android.content.IRCPGlobalContactsDir;
import android.content.IRCPInterface;
import android.content.ISyncCallBack;
import android.graphics.Bitmap;
import android.util.Log;
import java.util.List;

public class RCPManager {
    public static final int ERROR = -333;
    private static final String TAG = "RCPManager";
    IRCPManager mService;

    public CustomCursor queryProvider(String providerName, String resource, int containerId, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "My Context is " + this);
                return this.mService.queryProvider(providerName, resource, containerId, projection, selection, selectionArgs, sortOrder);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException trying to query provider  queryProvider", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<CustomCursor> queryAllProviders(String providerName, String resource, int containerId, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "My Context is " + this);
                return this.mService.queryAllProviders(providerName, resource, containerId, projection, selection, selectionArgs, sortOrder);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException trying to query provider  queryAllProviders()", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    public void registerProvider(String providerName, IProviderCallBack mProvider, int userId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "My Context is " + this);
                this.mService.registerProvider(providerName, mProvider, userId);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException trying to register provider callback registerProvider", e);
                e.printStackTrace();
            }
        }
    }

    public void registerSync(ISyncCallBack callback, int userId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "My Context is " + this);
                this.mService.registerSync(callback, userId);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException trying to register sync callback registerSync", e);
                e.printStackTrace();
            }
        }
    }

    public void registerCommandExe(ICommandExeCallBack callback, int userId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "My Context is " + this);
                if (callback != null) {
                    this.mService.registerCommandExe(callback, userId);
                } else {
                    Log.d(TAG, "registerCommandExe callback object is null!");
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException trying to register command executor callback registerSync", e);
                e.printStackTrace();
            }
        }
    }

    public void registerRCPGlobalContactsDir(IRCPGlobalContactsDir globalContactsDir, int userId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "My Context is " + this);
                this.mService.registerRCPGlobalContactsDir(globalContactsDir, userId);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException trying to register globalContactsDir", e);
                e.printStackTrace();
            }
        }
    }

    public void registerRCPInterface(IRCPInterface rcpInterface, int userId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "registerRCPInterface(): My Context is " + this);
                this.mService.registerRCPInterface(rcpInterface, userId);
            } catch (RemoteException e) {
                Log.e(TAG, "registerRCPInterface: RemoteException trying to register rcpInterface", e);
                e.printStackTrace();
            }
        }
    }

    public RCPManager(IRCPManager service) {
        this.mService = service;
    }

    public void switchPersona(int id) {
        if (this.mService != null) {
            try {
                this.mService.switchPersona(id);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException trying to switch persona.", e);
                e.printStackTrace();
            }
        }
    }

    public void executeCommandForPersona(Command command) {
        if (this.mService != null) {
            try {
                this.mService.executeCommandForPersona(command);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException trying to switch persona.", e);
                e.printStackTrace();
            }
        }
    }

    public IRCPGlobalContactsDir getRCPProxy() {
        if (this.mService != null) {
            try {
                return this.mService.getRCPProxy();
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException trying to get IRCPGlobalContactsDir from getRCPProxy().", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    public IRCPInterface getRCPInterface() {
        if (this.mService != null) {
            try {
                return this.mService.getRCPInterface();
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException trying to get RCPInterface from getRCPInterface().", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    public CustomCursor getCallerInfo(String contactRefUriAsString) {
        if (this.mService != null) {
            try {
                return this.mService.getCallerInfo(contactRefUriAsString);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException trying to get getCallerInfo(). ", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    public int copyFileInternal(int srcContainerId, String srcFilePath, int destContainerId, String destFilePath) throws RemoteException {
        if (this.mService != null) {
            return this.mService.copyFileInternal(srcContainerId, srcFilePath, destContainerId, destFilePath);
        }
        return -1;
    }

    public int moveFile(int srcContainerId, String srcFilePath, int destContainerId, String destFilePath) throws RemoteException {
        if (this.mService != null) {
            return this.mService.moveFile(srcContainerId, srcFilePath, destContainerId, destFilePath);
        }
        return -1;
    }

    public int changePermissionMigration(String path, int uid, int gid, int mode) throws RemoteException {
        if (this.mService != null) {
            return this.mService.changePermissionMigration(path, uid, gid, mode);
        }
        return -1;
    }

    public boolean isFileExist(String path, int containerId) throws RemoteException {
        if (this.mService != null) {
            return this.mService.isFileExist(path, containerId);
        }
        return false;
    }

    public List<String> getFiles(String path, int containerId) throws RemoteException {
        if (this.mService != null) {
            return this.mService.getFiles(path, containerId);
        }
        return null;
    }

    public Bundle getFileInfo(String path, int containerId) throws RemoteException {
        if (this.mService != null) {
            return this.mService.getFileInfo(path, containerId);
        }
        return null;
    }

    public int copyChunks(int srcContainerId, String srcFilePath, int destContainerId, String destFilePath, long offset, int length, long sessionId, boolean deleteSrc) throws RemoteException {
        if (this.mService != null) {
            return this.mService.copyChunks(srcContainerId, srcFilePath, destContainerId, destFilePath, offset, length, sessionId, deleteSrc);
        }
        return ERROR;
    }

    public void cancelCopyChunks(long sessionId) throws RemoteException {
        if (this.mService != null) {
            this.mService.cancelCopyChunks(sessionId);
        }
    }

    public boolean deleteFile(String path, int containerId) throws RemoteException {
        if (this.mService != null) {
            return this.mService.deleteFile(path, containerId);
        }
        return false;
    }

    public boolean registerExchangeData(Context ctx, IRunnableCallback cb, int userId) throws RemoteException {
        if (this.mService == null) {
            return false;
        }
        return this.mService.registerExchangeData(ctx.getPackageName(), cb, userId);
    }

    public Bundle exchangeData(Context ctx, int userId, Bundle bundle) throws RemoteException {
        if (this.mService == null) {
            return null;
        }
        return this.mService.exchangeData(ctx.getPackageName(), userId, bundle);
    }

    public boolean registerMonitorCb(Context ctx, IRunnableCallback cb) throws RemoteException {
        if (this.mService == null) {
            return true;
        }
        return this.mService.registerMonitorCb(ctx.getPackageName(), cb);
    }

    public void handleShortcut(int userId, String packageName, String packageLabel, Bitmap shortcutIcon, String shortcutIntentUri, String createOrRemove) {
        Log.d(TAG, " in createShortcut() for packageName: " + packageName + " userId" + userId);
        if (this.mService != null) {
            try {
                this.mService.handleShortcut(userId, packageName, packageLabel, shortcutIcon, shortcutIntentUri, createOrRemove);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException trying to get createShortcut(). ", e);
                e.printStackTrace();
            }
        }
    }

    public int copyFile(int srcContainerId, String srcFilePath, int destContainerId, String destFilePath) throws RemoteException {
        if (this.mService == null) {
            return -1;
        }
        Log.d(TAG, "copyFile: srcContainerId" + srcContainerId + " srcFilePath" + srcFilePath + " destContainerId" + destContainerId + " destFilePath" + destFilePath);
        return this.mService.copyFile(srcContainerId, srcFilePath, destContainerId, destFilePath);
    }

    public long moveFilesForApp(int requestApp, List<String> srcFilePaths, List<String> destFilePaths) throws RemoteException {
        if (this.mService != null) {
            return this.mService.moveFilesForApp(requestApp, srcFilePaths, destFilePaths);
        }
        return -1;
    }

    public void doSyncForSyncer(String mSyncerName, int mUserId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "doSyncForSyncer, SyncerName " + mSyncerName + " , providerID :" + mUserId);
                this.mService.doSyncForSyncer(mSyncerName, mUserId);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException trying to register globalContactsDir", e);
                e.printStackTrace();
            }
        }
    }

    public void registerObserver(String mSyncerName, int mUserId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "registerObserver, SyncerName " + mSyncerName + " ,userId :" + mUserId);
                this.mService.registerObserver(mSyncerName, mUserId);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException trying to register globalContactsDir", e);
                e.printStackTrace();
            }
        }
    }

    public void unRegisterObserver(String mSyncerName, int mUserId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "registerObserver, SyncerName " + mSyncerName + " ,userId :" + mUserId);
                this.mService.unRegisterObserver(mSyncerName, mUserId);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException trying to register globalContactsDir", e);
                e.printStackTrace();
            }
        }
    }
}
