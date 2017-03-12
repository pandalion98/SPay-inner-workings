package com.samsung.android.magazinecard;

import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.magazinecard.IMagazineCardManager.Stub;

public class MagazineCardManager {
    private static String KNOX_APP_PACKAGE_NAME_PREFIX = "sec_container_";
    private static String TAG = "MagazineCardManager";
    private static Context mContext = null;
    private static int mCurrentUserID;
    private IMagazineCardManager mService = null;

    public MagazineCardManager(Context context) {
        Log.d(TAG, "MagazineCardManager");
        mContext = context;
        mCurrentUserID = mContext.getUserId();
        getService();
    }

    private synchronized IMagazineCardManager getService() {
        if (this.mService == null) {
            this.mService = Stub.asInterface(ServiceManager.getService("magazinecardservice"));
            if (this.mService == null) {
                Log.e(TAG, "getService : Could not get the service!");
            }
        }
        return this.mService;
    }

    private void setTimeStamp(MagazineCardInfo card) {
        if (card.mTimeStamp == 0) {
            card.mTimeStamp = System.currentTimeMillis();
        }
    }

    private ComponentName getComponentName(String packageName) {
        if (packageName != null) {
            return new ComponentName(packageName, "");
        }
        return new ComponentName(mContext, "");
    }

    private String getAppName(String packageName) {
        if (packageName == null) {
            packageName = mContext.getPackageName();
        }
        String[] split = packageName.split("\\.");
        if (split.length > 0) {
            return split[split.length - 1];
        }
        return null;
    }

    public boolean isServiceEnabled() {
        return (isKnoxApp() || getService() == null) ? false : true;
    }

    private boolean isKnoxApp() {
        String packageName = mContext.getPackageName();
        if (packageName == null || !packageName.startsWith(KNOX_APP_PACKAGE_NAME_PREFIX)) {
            return false;
        }
        return true;
    }

    public boolean addCard(MagazineCardInfo card) {
        Log.d(TAG, "addCard : " + getAppName(null) + " U" + mCurrentUserID + " C" + card.mCardId);
        if (isKnoxApp()) {
            Log.e(TAG, "addCard : KNOX application cannot use magazine card service");
            return false;
        }
        try {
            IMagazineCardManager svc = getService();
            if (svc == null) {
                return true;
            }
            setTimeStamp(card);
            ComponentName component = getComponentName(null);
            card.mUserId = mCurrentUserID;
            svc.addCard(mCurrentUserID, component, card);
            return true;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean updateCard(MagazineCardInfo card) {
        return updateCard(null, card);
    }

    public boolean updateCard(String packageName, MagazineCardInfo card) {
        Log.d(TAG, "updateCard : " + getAppName(packageName) + " U" + mCurrentUserID + " C" + card.mCardId);
        if (isKnoxApp()) {
            Log.e(TAG, "updateCard : KNOX application cannot use magazine card service");
            return false;
        }
        try {
            IMagazineCardManager svc = getService();
            if (svc == null) {
                return true;
            }
            ComponentName component = getComponentName(packageName);
            card.mUserId = mCurrentUserID;
            svc.updateCard(mCurrentUserID, component, card);
            return true;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean removeCard(int cardId) {
        return removeCard(null, cardId);
    }

    public boolean removeCard(String packageName, int cardId) {
        Log.d(TAG, "removeCard : " + getAppName(packageName) + " U" + mCurrentUserID + " C" + cardId);
        if (isKnoxApp()) {
            Log.e(TAG, "removeCard : KNOX application cannot use magazine card service");
            return false;
        }
        try {
            IMagazineCardManager svc = getService();
            if (svc == null) {
                return true;
            }
            svc.removeCard(mCurrentUserID, getComponentName(packageName), cardId);
            return true;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean removeAllCard() {
        return removeAllCard(null);
    }

    public boolean removeAllCard(String packageName) {
        Log.d(TAG, "removeAllCard : " + getAppName(packageName) + " U" + mCurrentUserID);
        if (isKnoxApp()) {
            Log.e(TAG, "removeAllCard : KNOX application cannot use magazine card service");
            return false;
        }
        try {
            IMagazineCardManager svc = getService();
            if (svc == null) {
                return true;
            }
            ComponentName component;
            if (packageName != null) {
                component = new ComponentName(packageName, "");
            } else {
                component = new ComponentName(mContext, "");
            }
            svc.removeAllCard(mCurrentUserID, component);
            return true;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean isCardExist(int cardId) {
        return isCardExist(null, cardId);
    }

    public boolean isCardExist(String packageName, int cardId) {
        boolean z = false;
        Log.d(TAG, "isCardExist : " + getAppName(packageName) + " U" + mCurrentUserID + " C" + cardId);
        if (isKnoxApp()) {
            Log.e(TAG, "isCardExist : KNOX application cannot use magazine card service");
        } else {
            try {
                IMagazineCardManager svc = getService();
                if (svc != null) {
                    z = svc.isCardExist(mCurrentUserID, getComponentName(packageName), cardId);
                }
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
        return z;
    }

    public int[] getCardRecordIdList(int userId, boolean securityMode) {
        int[] iArr = null;
        Log.d(TAG, "getCardRecordIdList : " + getAppName(null) + " U" + mCurrentUserID + " ReqU" + userId + " SM:" + securityMode);
        try {
            IMagazineCardManager svc = getService();
            if (svc != null) {
                iArr = svc.getCardRecordIdList(userId, securityMode);
            }
            return iArr;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public MagazineCardRecord getCard(int cardRecordId) {
        MagazineCardRecord magazineCardRecord = null;
        try {
            Log.d(TAG, "getCard : " + getAppName(null) + " R" + cardRecordId);
            IMagazineCardManager svc = getService();
            if (svc != null) {
                magazineCardRecord = svc.getCard(cardRecordId);
            }
            return magazineCardRecord;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }
}
