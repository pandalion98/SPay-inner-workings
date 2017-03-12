package android.sec.clipboard.data.file;

import android.sec.clipboard.data.ClipboardData;
import android.sec.clipboard.util.FileHelper;
import android.util.Log;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class WrapFileClipData implements Serializable {
    private static final String TAG = "WrapFileClipData";
    private static final long serialVersionUID = 1;
    private transient ClipboardData mClip;
    private ArrayList<Integer> mDeletedUserList = new ArrayList();
    private File mDir;
    private boolean mIsProtected;
    private File mPath;

    public WrapFileClipData(ClipboardData clip) {
        if (clip != null) {
            clip.setClipdata(null);
        }
        this.mClip = clip;
        this.mPath = FileHelper.getInstance().getNullFile();
    }

    public ClipboardData getClipData() {
        if (this.mClip == null) {
            this.mClip = loadData();
            if (this.mClip != null) {
                this.mClip.setClipdata(null);
            }
        }
        return this.mClip;
    }

    public void setClipData(ClipboardData clip) {
        clip.setClipdata(null);
        this.mClip = clip;
    }

    public File getFile() {
        return this.mPath;
    }

    public void setFile(File path) {
        this.mPath = path;
    }

    public File getDir() {
        return this.mDir;
    }

    public void setDir(File dir) {
        this.mDir = dir;
    }

    public void setProtectState(boolean isProtected) {
        this.mIsProtected = isProtected;
    }

    public boolean load() {
        this.mClip = loadData();
        if (this.mClip == null) {
            return false;
        }
        this.mClip.SetProtectState(this.mIsProtected);
        switch (this.mClip.getFormat()) {
            case 2:
                this.mClip.toLoad();
                return true;
            case 3:
                if (new File(this.mClip.getBitmapPath()).exists()) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }

    private ClipboardData loadData() {
        return (ClipboardData) FileHelper.getInstance().loadObjectFile(this.mPath);
    }

    public void addDeletedUserList(int userId) {
        Log.d(TAG, "addDeletedUserList : " + userId);
        if (this.mDeletedUserList == null) {
            this.mDeletedUserList = new ArrayList();
        }
        if (!isDeletedUser(userId)) {
            this.mDeletedUserList.add(Integer.valueOf(userId));
        }
    }

    public boolean isDeletedUser(int userId) {
        if (this.mDeletedUserList != null) {
            return this.mDeletedUserList.contains(Integer.valueOf(userId));
        }
        this.mDeletedUserList = new ArrayList();
        return false;
    }

    public void reAddForKnox() {
        if (this.mDeletedUserList != null) {
            Log.d(TAG, "reAddForKnox : " + this.mDeletedUserList);
            this.mDeletedUserList.clear();
        }
    }

    public void resetOwnerClips(int userId) {
        Log.d(TAG, "resetOwnerClips : " + this.mDeletedUserList);
        if (this.mDeletedUserList != null) {
            int index = this.mDeletedUserList.indexOf(Integer.valueOf(userId));
            Log.d(TAG, "index is " + index);
            if (index > -1) {
                Log.d(TAG, "remove : " + index);
                this.mDeletedUserList.remove(index);
            }
        }
    }
}
