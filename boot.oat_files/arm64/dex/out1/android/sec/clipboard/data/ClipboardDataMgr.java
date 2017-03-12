package android.sec.clipboard.data;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.UserInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PersonaManager;
import android.os.PersonaPolicyManager;
import android.os.RemoteException;
import android.os.SELinux;
import android.os.UserHandle;
import android.os.UserManager;
import android.sec.clipboard.data.IClipboardDataList.Stub;
import android.sec.clipboard.data.file.FileManager;
import android.sec.clipboard.data.file.WrapFileClipData;
import android.sec.clipboard.data.list.ClipboardDataText;
import android.sec.enterprise.content.SecContentProviderURI;
import android.util.Log;
import com.android.ims.ImsConferenceState;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClipboardDataMgr implements IClipboardDataList {
    private static final String TAG = "ClipboardDataMgr";
    private final Stub mBinder = new Stub() {
        public int size() throws RemoteException {
            return ClipboardDataMgr.this.size();
        }

        public ClipboardData getItem(int index) throws RemoteException {
            return ClipboardDataMgr.this.getItem(index);
        }

        public boolean removeData(int index) throws RemoteException {
            return ClipboardDataMgr.this.removeData(index);
        }

        public boolean updateData(int index, ClipboardData clipData) throws RemoteException {
            return ClipboardDataMgr.this.updateData(index, clipData);
        }

        public boolean updateScrapData(int index, ClipboardData clipData) throws RemoteException {
            return ClipboardDataMgr.this.updateScrapData(index, clipData);
        }

        public boolean removeScrapData(int index) throws RemoteException {
            return ClipboardDataMgr.this.removeScrapData(index);
        }

        public ClipboardData getScrapItem(int index) throws RemoteException {
            return ClipboardDataMgr.this.getScrapItem(index);
        }

        public int scrapSize() throws RemoteException {
            return ClipboardDataMgr.this.scrapSize();
        }
    };
    private Context mContext;
    private int mCurrentListIndex;
    private int mCurrentListOwnerUid = 0;
    private ArrayList<FileManager> mDataArrayList = new ArrayList();
    private FileManager mDataList;
    private boolean mIsShared = false;
    private final int mMaxSize;
    private PersonaManager mPersonaManager = null;
    private PersonaPolicyManager mPersonaPolicyManager = null;
    private UserManager mUserManager = null;
    private ClipboardData tempDeleteItem = null;

    public boolean updateScrapData(int index, ClipboardData clipData) {
        return false;
    }

    public boolean removeScrapData(int index) {
        return false;
    }

    public ClipboardData getScrapItem(int index) {
        return null;
    }

    public int scrapSize() {
        return 0;
    }

    public ClipboardDataMgr(int size, Context context, boolean shared) {
        this.mMaxSize = size;
        this.mContext = context;
        this.mIsShared = shared;
        this.mPersonaManager = (PersonaManager) this.mContext.getSystemService("persona");
        this.mUserManager = (UserManager) this.mContext.getSystemService(ImsConferenceState.USER);
        initializeClipboardDataMgr(size);
    }

    public ArrayList<FileManager> getFileManagerList() {
        return this.mDataArrayList;
    }

    private boolean getPersonaManager() {
        if (this.mPersonaManager != null) {
            return true;
        }
        if (ClipboardConstants.HAS_KNOX_FEATURE) {
            Log.e(TAG, "personaManager is null!");
            this.mPersonaManager = (PersonaManager) this.mContext.getSystemService("persona");
        }
        return this.mPersonaManager != null;
    }

    private int getUserId() {
        int userId = UserHandle.getUserId(Binder.getCallingUid());
        if (!PersonaManager.isBBCContainer(userId)) {
            return userId;
        }
        Log.d(TAG, "getUserId is BBC");
        return 0;
    }

    public int getPersonaId() {
        if (!ClipboardConstants.HAS_KNOX_FEATURE) {
            return getUserId();
        }
        if (getPersonaManager()) {
            return this.mPersonaManager.getFocusedUser();
        }
        return ActivityManager.getCurrentUser();
    }

    public int getCurrentListOwnerUid() {
        return this.mCurrentListOwnerUid;
    }

    public boolean isManagedProfile(int userId) {
        boolean isManagedProfile;
        long origId = -1;
        try {
            origId = Binder.clearCallingIdentity();
            isManagedProfile = this.mUserManager.getUserInfo(userId).isManagedProfile();
            Log.e(TAG, "isManagedProfile for userId: " + userId + " is value: " + isManagedProfile);
            if (origId != -1) {
                Binder.restoreCallingIdentity(origId);
            }
        } catch (Exception ex) {
            Log.e(TAG, "isManagedProfile() Exception!");
            ex.printStackTrace();
            isManagedProfile = false;
            if (origId != -1) {
                Binder.restoreCallingIdentity(origId);
            }
        } catch (Throwable th) {
            if (origId != -1) {
                Binder.restoreCallingIdentity(origId);
            }
        }
        Log.e(TAG, "isManagedProfile for userId: " + userId + " is value: " + isManagedProfile);
        return isManagedProfile;
    }

    private void initializeClipboardDataMgr(int size) {
        this.mCurrentListIndex = 0;
        File rootPath = new File("/data/clipboard");
        Log.w(TAG, "ClipboardDefine.SUPPORT_KNOX :" + ClipboardConstants.SUPPORT_KNOX);
        this.mDataList = new FileManager(new File(rootPath, "clips.info"), 0, this.mContext);
        this.mDataArrayList.add(this.mDataList);
        this.mCurrentListOwnerUid = 0;
        if (this.mUserManager != null) {
            List<UserInfo> userlist = this.mUserManager.getUsers();
            if (userlist != null && userlist.size() > 1) {
                int i = 0;
                while (i < userlist.size()) {
                    if (!(userlist.get(i) == null || ((UserInfo) userlist.get(i)).isAdmin())) {
                        int id = ((UserInfo) userlist.get(i)).id;
                        Log.i(TAG, "add multi user db...id :" + id);
                        if (id >= 100 && id <= 194) {
                            int cat = id + 1000;
                            if (ClipboardConstants.DEBUG) {
                                Log.w(TAG, "RS:add multi user db...cat :" + cat);
                            }
                            createUserDBAndConnect(cat);
                        } else if (!(id == 0 || id >= 100 || isManagedProfile(id) || PersonaManager.isBBCContainer(id))) {
                            if (ClipboardConstants.DEBUG) {
                                Log.w(TAG, "secondary user usecase :" + 0);
                            }
                            createUserDBAndConnect(id + 1000);
                        }
                    }
                    i++;
                }
            }
        }
        List<Integer> category = getAllSEContainerCategory();
        if (category != null && category.size() > 0) {
            for (Integer intValue : category) {
                createUserDBAndConnect(intValue.intValue());
            }
        }
    }

    public int findFileManagerIndex(int id) {
        Log.i(TAG, "findFileManagerIndex. id :" + id);
        if (this.mDataArrayList != null) {
            int i = 0;
            while (i < this.mDataArrayList.size()) {
                FileManager mgr = (FileManager) this.mDataArrayList.get(i);
                if (mgr == null || mgr.getHandleId() != id) {
                    i++;
                } else {
                    Log.i(TAG, "findFileManagerIndex. find DB... id :" + id + ", i :" + i);
                    return i;
                }
            }
        }
        Log.w(TAG, "Can not find file manager. id : " + id);
        return -1;
    }

    public void replaceWithTargetForUser(ClipboardDataMgr dataList, int userId) {
        int cat;
        Log.d(TAG, "replaceWithTargetForUser : userId - " + userId);
        if (userId >= 100 && userId <= 194) {
            cat = userId + 1000;
        } else if (userId == 0 || userId >= 100 || isManagedProfile(userId) || PersonaManager.isBBCContainer(userId)) {
            cat = userId;
        } else {
            cat = userId + 1000;
        }
        int targetId = findFileManagerIndex(cat);
        if (targetId != -1) {
            Log.d(TAG, "replaceWithTargetForUser : targetId - " + targetId);
            int sourceId = dataList.findFileManagerIndex(cat);
            if (sourceId != -1) {
                FileManager sourceDataList = (FileManager) dataList.getFileManagerList().get(sourceId);
                Log.d(TAG, sourceId + ", " + sourceDataList.size());
                this.mDataArrayList.set(targetId, sourceDataList);
                this.mDataList = sourceDataList;
                this.mCurrentListIndex = targetId;
            }
        }
    }

    public void refresh() {
        if (this.mDataList != null) {
            this.mDataList.refresh();
        }
    }

    public void multiUserMode(int id, String mode) {
        int cat;
        Log.i(TAG, "multiUserMode_Called mode :" + mode + ", id :" + id + ", mCurrentListIndex :" + this.mCurrentListIndex);
        this.mCurrentListOwnerUid = id;
        if (id >= 100 && id <= 194) {
            cat = id + 1000;
        } else if (id == 0 || id >= 100 || isManagedProfile(id) || PersonaManager.isBBCContainer(id)) {
            cat = id;
        } else {
            cat = id + 1000;
        }
        if (mode != null && this.mDataArrayList != null) {
            if (mode.equals("ADDED")) {
                createUserDBAndConnect(cat);
            } else if (mode.equals("REMOVED")) {
                int deleteIndex = findFileManagerIndex(cat);
                if (deleteIndex != -1) {
                    FileManager mgr = (FileManager) this.mDataArrayList.get(deleteIndex);
                    if (mgr != null) {
                        mgr.removeClipboardDB();
                        this.mDataArrayList.remove(deleteIndex);
                        this.mDataList.resetOwnerClips(cat);
                        return;
                    }
                    return;
                }
                Log.w(TAG, "findFileManagerIndex return an invalid index. id :" + cat + ", return value :" + deleteIndex);
            } else if (!mode.equals("SWITCHED")) {
                Log.w(TAG, "multiUserMode_Called wrong mode : " + mode + ", id : " + cat);
            } else if (cat == this.mCurrentListIndex) {
                Log.w(TAG, "USER_SWITCHED, but same user : " + id + ", category: " + cat + ", mCurrentListIndex: " + this.mCurrentListIndex);
            } else {
                this.mCurrentListIndex = findFileManagerIndex(cat);
                if (this.mCurrentListIndex != -1) {
                    this.mDataList = (FileManager) this.mDataArrayList.get(this.mCurrentListIndex);
                    Log.w(TAG, "RS, multiUserMode, findFileManagerIndex returned index:" + this.mCurrentListIndex + ", now getting relevant datalist with handleid:" + this.mDataList.getHandleId());
                    return;
                }
                Log.w(TAG, "findFileManagerIndex return an invalid index. id :" + cat + ", return value :" + this.mCurrentListIndex);
                this.mDataList = createUserDBAndConnect(cat);
            }
        }
    }

    private FileManager createUserDBAndConnect(int cat) {
        int index;
        if (PersonaManager.isBBCContainer(cat - 1000)) {
            index = findFileManagerIndex(0);
            if (index >= 0) {
                return (FileManager) this.mDataArrayList.get(index);
            }
            FileManager fm = new FileManager(new File(new File("/data/clipboard"), "clips.info"), 0, this.mContext);
            this.mDataArrayList.add(fm);
            return fm;
        }
        index = findFileManagerIndex(cat);
        if (index >= 0) {
            return (FileManager) this.mDataArrayList.get(index);
        }
        if (cat == 102) {
            File rootPath = new File("/data/clipboard/secontainer/" + String.valueOf(cat));
            fm = new FileManager(new File(rootPath, "clips.info"), cat, this.mContext);
            this.mDataArrayList.add(fm);
            if (ClipboardConstants.DEBUG) {
                Log.w(TAG, "createUserDBAndConnect, good container, cat:" + cat);
            }
            SELinux.restoreconRecursive(rootPath);
            return fm;
        } else if (cat < 201 || cat > 500) {
            fm = new FileManager(new File(new File("/data/clipboard" + String.valueOf(cat - 1000)), "clips.info"), cat, this.mContext);
            this.mDataArrayList.add(fm);
            if (!ClipboardConstants.SUPPORT_KNOX && 1100 <= cat && cat <= ClipboardConstants.PERSONA_CATEGORY_END) {
                SELinux.restorecon_with_category("/data/clipboard" + String.valueOf(cat - 1000), cat - 1000);
            }
            if (!(cat == 0 || isManagedProfile(cat - 1000))) {
                SELinux.restorecon_with_category("/data/clipboard" + String.valueOf(cat - 1000), cat - 1000);
            }
            return fm;
        } else {
            fm = new FileManager(new File(new File("/data/clipboard/secontainer/" + String.valueOf(cat)), "clips.info"), cat, this.mContext);
            this.mDataArrayList.add(fm);
            if (ClipboardConstants.DEBUG) {
                Log.w(TAG, "createUserDBAndConnect, third party container, cat:" + cat);
            }
            SELinux.restorecon_with_category("/data/clipboard/secontainer/" + String.valueOf(cat), cat);
            return fm;
        }
    }

    public boolean getKnoxPolicy(int userId) {
        try {
            if (getPersonaManager()) {
                if (this.mPersonaPolicyManager == null) {
                    this.mPersonaPolicyManager = (PersonaPolicyManager) this.mPersonaManager.getPersonaService("persona_policy");
                }
                if (this.mPersonaPolicyManager != null) {
                    return this.mPersonaPolicyManager.isShareClipboardDataToOwnerAllowed(userId);
                }
                Log.e(TAG, "mPersonaPolicyManager is null!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void loadSEContainerDB(int category) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "loadSEContainerDB, category:" + category + ", previous category:" + this.mDataList.getHandleId());
        }
        if (this.mDataList.getHandleId() != category) {
            int index = findFileManagerIndex(category);
            if (index != -1) {
                this.mDataList = (FileManager) this.mDataArrayList.get(index);
                refresh();
                return;
            }
            if (ClipboardConstants.DEBUG) {
                Log.w(TAG, "loadSEContainerDB, findFileManagerIndex returned -1, create db, category :" + category);
            }
            this.mDataList = createUserDBAndConnect(category);
        }
    }

    private List<Integer> getAllSEContainerCategory() {
        List<Integer> res = new ArrayList();
        if (this.mContext != null) {
            Cursor cr = this.mContext.getContentResolver().query(Uri.parse(SecContentProviderURI.SEAMS_URI), null, SecContentProviderURI.SEAMSPOLICY_SECONTAINERCATEGORY_METHOD, null, null);
            if (cr == null || cr.getCount() <= 0) {
                Log.i(TAG, "getAllSEContainerCategory query error, return -1");
                if (cr != null) {
                    cr.close();
                }
            } else {
                try {
                    cr.moveToFirst();
                    while (true) {
                        Log.i(TAG, "getAllSEContainerCategory, cat result from query:" + cr.getInt(cr.getColumnIndex(SecContentProviderURI.SEAMSPOLICY_SECONTAINERCATEGORY_METHOD)));
                        res.add(Integer.valueOf(cr.getInt(cr.getColumnIndex(SecContentProviderURI.SEAMSPOLICY_SECONTAINERCATEGORY_METHOD))));
                        if (!cr.moveToNext()) {
                            break;
                        }
                    }
                    Log.i(TAG, "getAllSEContainerCategory success, return res");
                } finally {
                    cr.close();
                }
            }
        }
        return res;
    }

    public void secontainerClipboardMode(String mode, int category) {
        if (mode.equals(ClipboardConstants.ACTION_SECONTAINER_REMOVED)) {
            int deleteIndex = findFileManagerIndex(category);
            if (category == this.mDataList.getHandleId()) {
                this.mDataList = (FileManager) this.mDataArrayList.get(0);
            }
            if (deleteIndex != -1) {
                FileManager mgr = (FileManager) this.mDataArrayList.get(deleteIndex);
                if (mgr != null) {
                    mgr.removeClipboardDB();
                    this.mDataArrayList.remove(deleteIndex);
                    this.mDataList.resetOwnerClips(category);
                    return;
                }
                return;
            }
            Log.w(TAG, "findFileManagerIndex return an invalid index. id:" + category + ", return value :" + deleteIndex);
        } else if (mode.equals(ClipboardConstants.ACTION_SECONTAINER_ADDED)) {
            Log.w(TAG, "action secontainer added for category:" + category + ", creating clipboard db");
            createUserDBAndConnect(category);
        }
    }

    public int size() {
        int finalSize = 0;
        if (getPersonaManager() && getPersonaId() == 0) {
            int[] ids = this.mPersonaManager.getPersonaIds();
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {
                    boolean isAllowed = getKnoxPolicy(ids[i]);
                    Log.d(TAG, "policy is " + isAllowed + ", " + ids[i]);
                    if (isAllowed) {
                        if (this.mDataArrayList.size() > i) {
                            FileManager fm = (FileManager) this.mDataArrayList.get(i + 1);
                            if (fm != null) {
                                finalSize += fm.sharedSize(0);
                            }
                        } else {
                            Log.d(TAG, "data list is less than " + i);
                        }
                    }
                }
            }
        }
        if (this.mDataList != null) {
            return finalSize + this.mDataList.size();
        }
        return finalSize;
    }

    public int sharedSize() {
        if (this.mDataList != null) {
            return this.mDataList.sharedSize(getPersonaId());
        }
        return 0;
    }

    public void updateFotaClips(int userId, ArrayList<String> list) {
        FileManager fmg = (FileManager) this.mDataArrayList.get(0);
        if (fmg != null) {
            int size = fmg.size();
            String path = "";
            for (int i = 0; i < size; i++) {
                WrapFileClipData wrap = fmg.getWrap(i);
                if (wrap != null) {
                    path = wrap.getDir().toString();
                    if (list.contains(path.substring(path.lastIndexOf("/") + 1))) {
                        wrap.addDeletedUserList(userId);
                    }
                }
            }
        }
    }

    public ClipboardData getItem(int index) {
        int myListSize = this.mDataList.size();
        ClipboardData Result = null;
        if (index < myListSize) {
            Result = this.mDataList.get(index);
        } else if (getPersonaManager()) {
            int arrayListSize = this.mDataArrayList.size();
            int[] ids = this.mPersonaManager.getPersonaIds();
            if (ids != null && ids.length > 0) {
                int i = 1;
                int totalSize = myListSize;
                int idsIndex = 0;
                while (i < arrayListSize) {
                    if (idsIndex >= ids.length) {
                        Log.e(TAG, "overflow! " + idsIndex + ", " + ids.length);
                        break;
                    }
                    if (getKnoxPolicy(ids[idsIndex])) {
                        ArrayList<ClipboardData> list = ((FileManager) this.mDataArrayList.get(i)).getNonDeletedClips(0);
                        int listSize = list.size();
                        if (index < totalSize + listSize) {
                            Log.d(TAG, "return : " + i + ", " + (index - totalSize));
                            return (ClipboardData) list.get(index - totalSize);
                        }
                        totalSize += listSize;
                        Log.d(TAG, "next : " + i + ", " + ids[idsIndex] + ", " + listSize);
                    } else {
                        Log.d(TAG, "not allow! " + ids[idsIndex]);
                    }
                    i++;
                    idsIndex++;
                }
            }
        }
        return Result;
    }

    public ClipboardData getSharedItem(int index) {
        ClipboardData Result = null;
        try {
            int userId = getPersonaId();
            ArrayList<ClipboardData> nonDeletedlist = new ArrayList();
            return (ClipboardData) this.mDataList.getNonDeletedClips(userId).get(index);
        } catch (Exception e) {
            e.printStackTrace();
            return Result;
        }
    }

    public synchronized boolean addData(ClipboardData data) {
        boolean z;
        boolean Result = true;
        int iSize = this.mDataList.size();
        if (data.GetFomat() == 2 && ((ClipboardDataText) data).GetText().toString().equals("")) {
            z = false;
        } else {
            int index = -1;
            try {
                if (iSize >= this.mMaxSize) {
                    for (int i = iSize - 1; i >= 0; i--) {
                        ClipboardData deleteData = this.mDataList.get(i);
                        if (deleteData != null) {
                            if (ClipboardConstants.DEBUG) {
                                Log.d(TAG, "[addData] deleteData.GetProtectState() : " + deleteData.GetProtectState());
                            }
                            if (!deleteData.GetProtectState()) {
                                index = i;
                                break;
                            }
                        }
                    }
                    if (index >= 0) {
                        this.tempDeleteItem = this.mDataList.get(index);
                        removeData(index);
                        Result = this.mDataList.add(0, data);
                    }
                } else {
                    Result = this.mDataList.add(0, data);
                }
            } catch (Exception e) {
                Result = false;
            }
            z = Result;
        }
        return z;
    }

    public boolean removeAll() {
        boolean isOwner;
        boolean z = true;
        Log.d(TAG, "inside remove all method in data mgr mshared is " + this.mIsShared);
        boolean bRes = false;
        boolean ownRes = false;
        if (getPersonaId() == 0) {
            isOwner = true;
        } else {
            isOwner = false;
        }
        try {
            if (getPersonaManager() && isOwner) {
                int[] ids = this.mPersonaManager.getPersonaIds();
                if (ids != null) {
                    int i = 1;
                    for (int idsIndex = 0; idsIndex < ids.length; idsIndex++) {
                        ArrayList<WrapFileClipData> list = ((FileManager) this.mDataArrayList.get(i)).getNonDeletedClipsFromKnox(0);
                        if (list != null && list.size() > 0) {
                            int listSize = list.size();
                            for (int index = 0; index < listSize; index++) {
                                if (!((WrapFileClipData) list.get(index)).getClipData().GetProtectState()) {
                                    ((WrapFileClipData) list.get(index)).addDeletedUserList(0);
                                    ownRes = true;
                                }
                            }
                        }
                        i++;
                    }
                }
            }
            FileManager fileManager = this.mDataList;
            if (this.mIsShared) {
                z = false;
            }
            bRes = fileManager.removeAll(z, getPersonaId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isOwner && ownRes) {
            return true;
        }
        return bRes;
    }

    public boolean removeData(int index) {
        Log.d(TAG, "inside removeData method" + index);
        try {
            int size = this.mDataList.size();
            if (index < size) {
                if (this.mDataList.remove(!this.mIsShared, getPersonaId(), index) != null) {
                    return true;
                }
                return false;
            }
            if (getPersonaManager()) {
                int arrayListSize = this.mDataArrayList.size();
                int[] ids = this.mPersonaManager.getPersonaIds();
                if (ids != null && ids.length > 0) {
                    int totalSize = size;
                    for (int i = 1; i < arrayListSize; i++) {
                        ArrayList<WrapFileClipData> list = ((FileManager) this.mDataArrayList.get(i)).getNonDeletedClipsFromKnox(0);
                        int listSize = list.size();
                        if (index < totalSize + listSize) {
                            Log.d(TAG, "return : " + i + ", " + (index - totalSize));
                            ((WrapFileClipData) list.get(index - totalSize)).addDeletedUserList(0);
                            return true;
                        }
                        totalSize += listSize;
                        Log.d(TAG, "next : " + i + ", " + ids[i] + ", " + listSize);
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reAddForKnox(ClipboardData data) {
        Log.d(TAG, "reAddForKnox");
        int dataListSize = this.mDataList.size();
        for (int i = 0; i < dataListSize; i++) {
            if (data.GetFomat() == this.mDataList.get(i).GetFomat()) {
                this.mDataList.reAddForKnox(i);
                Log.d(TAG, "call : " + i);
                return;
            }
        }
    }

    public ClipboardData getTopItem(int index) {
        ClipboardData latestData = null;
        long timestamp = 0;
        if (this.mDataList != null && this.mDataList.size() > 0) {
            latestData = this.mDataList.get(0);
            timestamp = this.mDataList.get(0).getTimestamp();
            Log.e(TAG, "in own condtn" + latestData + " " + timestamp);
        }
        int[] ids = this.mPersonaManager.getPersonaIds();
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                if (getKnoxPolicy(ids[i])) {
                    ArrayList<ClipboardData> list = ((FileManager) this.mDataArrayList.get(i + 1)).getNonDeletedClips(0);
                    if (list != null && list.size() > 0) {
                        ClipboardData knoxData = (ClipboardData) list.get(index);
                        Log.e(TAG, "knoxdata" + knoxData);
                        if (knoxData != null && knoxData.getTimestamp() > timestamp) {
                            timestamp = knoxData.getTimestamp();
                            latestData = knoxData;
                            Log.e(TAG, "in knox condtn" + latestData + " " + timestamp);
                        }
                    }
                }
            }
        }
        Log.e(TAG, "before return latestdatal" + latestData);
        return latestData;
    }

    public boolean updateData(int index, ClipboardData clipData) {
        if (clipData != null) {
            try {
                if (this.mDataList.set(index, clipData) != null) {
                    return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else if (!ClipboardConstants.DEBUG) {
            return false;
        } else {
            Log.e(TAG, "updateData-ClipboardData is null");
            return false;
        }
    }

    public IBinder asBinder() {
        return this.mBinder;
    }

    public ClipboardData getdeletedItem() {
        return this.tempDeleteItem;
    }

    public void setdeletedItem(ClipboardData data) {
        this.tempDeleteItem = data;
    }
}
