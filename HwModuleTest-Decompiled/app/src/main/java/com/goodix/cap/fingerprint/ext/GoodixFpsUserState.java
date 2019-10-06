package com.goodix.cap.fingerprint.ext;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AtomicFile;
import android.util.Log;
import android.util.Xml;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class GoodixFpsUserState {
    private static final String ATTR_FINGER_ID = "fingerId";
    private static final String ATTR_GROUP_ID = "groupId";
    private static final String ATTR_INDEX = "index";
    private static final String ATTR_PASSWORD = "password";
    private static final String ATTR_USER_ID = "userId";
    private static final String FINGERPRINT_FILE = "fingerprint_info.xml";
    private static final String TAG = "GoodixFpsUserState";
    private static final String TAG_FINGERPRINT = "fingerprint";
    private static final String TAG_FINGERPRINTS = "fingerprints";
    private static final String TAG_USER_INFO = "user_info";
    private static final String TAG_USER_INFOS = "user_infos";
    private static final String USER_INFO_FILE = "user_info.xml";
    private final Context mCtx;
    private String mCurUserId = "";
    private File mFingerprintFile;
    private final ArrayList<GoodixFpInfo> mFingerprints = new ArrayList<>();
    private final ArrayList<GoodixUserInfo> mUserInfo = new ArrayList<>();
    private final File mUserInfoFile;
    private final Runnable mWriteFpStateRunnable = new Runnable() {
        public void run() {
            GoodixFpsUserState.this.doWriteFpState();
        }
    };
    private final Runnable mWriteUserInfoRunnable = new Runnable() {
        public void run() {
            GoodixFpsUserState.this.doWriteUserInfo();
        }
    };

    public GoodixFpsUserState(Context ctx) {
        this.mCtx = ctx;
        this.mUserInfoFile = getUserInfoFile();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mCtx = ");
        sb.append(this.mCtx);
        Log.d(str, sb.toString());
        GoodixSharePref.getInstance().init(ctx);
        synchronized (this) {
            readUserInfoSyncLocked();
        }
    }

    public int addUserInfo(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("addUserInfo userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        int groupId = -1;
        synchronized (this) {
            if (!isUserIdExist(userId)) {
                groupId = getUniqueGroupId();
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("addUserInfo groupId = ");
                sb2.append(groupId);
                Log.d(str2, sb2.toString());
                this.mUserInfo.add(new GoodixUserInfo(userId, groupId));
                scheduleWriteUserInfoLocked();
            }
            if (GoodixSharePref.getInstance().getBoolean("enable_mt_flag", false)) {
                groupId = 10000;
            }
        }
        return groupId;
    }

    public boolean setUserPwd(String userId, String pwd) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setUserPwd userId = ");
        sb.append(userId);
        sb.append(", pwd=");
        sb.append(pwd);
        Log.d(str, sb.toString());
        for (int i = 0; i < this.mUserInfo.size(); i++) {
            if (userId.equals(((GoodixUserInfo) this.mUserInfo.get(i)).getUserId().toString())) {
                ((GoodixUserInfo) this.mUserInfo.get(i)).setPwd(pwd);
                scheduleWriteUserInfoLocked();
                this.mCurUserId = userId;
                return true;
            }
        }
        return false;
    }

    public boolean verifyUserPwd(String userId, String pwd) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("verifyUserPwd userId = ");
        sb.append(userId);
        sb.append(", pwd=");
        sb.append(pwd);
        Log.d(str, sb.toString());
        if (this.mUserInfo != null) {
            for (int i = 0; i < this.mUserInfo.size(); i++) {
                if (this.mUserInfo.get(i) != null && userId.equals(((GoodixUserInfo) this.mUserInfo.get(i)).getUserId().toString()) && pwd.equals(((GoodixUserInfo) this.mUserInfo.get(i)).getPwd().toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getIndexByFingerID(int fingerID) {
        for (int i = 0; i < this.mFingerprints.size(); i++) {
            if (fingerID == ((GoodixFpInfo) this.mFingerprints.get(i)).getFingerId()) {
                return ((GoodixFpInfo) this.mFingerprints.get(i)).getFingerIndex();
            }
        }
        return -1;
    }

    public boolean isUserIdExist(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isUserIdExist userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        for (int i = 0; i < this.mUserInfo.size(); i++) {
            if (userId.equals(((GoodixUserInfo) this.mUserInfo.get(i)).getUserId().toString())) {
                return true;
            }
        }
        return false;
    }

    private boolean isGroupIdExist(int groupId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isGroupIdExist groupId = ");
        sb.append(groupId);
        Log.d(str, sb.toString());
        for (int i = 0; i < this.mUserInfo.size(); i++) {
            if (groupId == ((GoodixUserInfo) this.mUserInfo.get(i)).getGroupId()) {
                return true;
            }
        }
        return false;
    }

    private int getUniqueGroupId() {
        int groupId = -1;
        while (true) {
            if (!GoodixSharePref.getInstance().getBoolean("enable_mt_flag", false)) {
                groupId++;
                if (!isGroupIdExist(groupId)) {
                    break;
                }
            } else {
                groupId = 10000;
                break;
            }
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getUniqueGroupId groupId = ");
        sb.append(groupId);
        Log.d(str, sb.toString());
        return groupId;
    }

    public void addFingerprint(String userId, int index, int fingerId, int groupId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("addFingerprint userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("addFingerprint mCurUserId = ");
        sb2.append(this.mCurUserId);
        Log.d(str2, sb2.toString());
        String str3 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("addFingerprint index = ");
        sb3.append(index);
        Log.d(str3, sb3.toString());
        String str4 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("addFingerprint fingerId = ");
        sb4.append(fingerId);
        Log.d(str4, sb4.toString());
        String str5 = TAG;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("addFingerprint groupId = ");
        sb5.append(groupId);
        Log.d(str5, sb5.toString());
        synchronized (this) {
            if (!this.mCurUserId.equals(userId)) {
                this.mCurUserId = userId;
                Log.d(TAG, "addFingerprint loadFingerprintsForUser ");
                loadFingerprintsForUser(userId);
            }
            this.mFingerprints.add(new GoodixFpInfo(index, groupId, fingerId));
            scheduleWriteFpLocked(userId);
        }
    }

    public void removeFingerprint(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("removeFingerprint userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("removeFingerprint index = ");
        sb2.append(index);
        Log.d(str2, sb2.toString());
        synchronized (this) {
            if (this.mCurUserId != null && !this.mCurUserId.equals(userId)) {
                this.mCurUserId = userId;
                loadFingerprintsForUser(userId);
            }
            int j = 0;
            int i = 0;
            while (true) {
                if (i >= this.mFingerprints.size()) {
                    break;
                } else if (index == ((GoodixFpInfo) this.mFingerprints.get(i)).getFingerIndex()) {
                    this.mFingerprints.remove(i);
                    scheduleWriteFpLocked(userId);
                    if (this.mFingerprints.size() == 0) {
                        getFingerprintFile(userId).delete();
                        while (true) {
                            if (j >= this.mUserInfo.size()) {
                                break;
                            } else if (userId.equals(((GoodixUserInfo) this.mUserInfo.get(j)).getUserId())) {
                                this.mUserInfo.remove(j);
                                scheduleWriteUserInfoLocked();
                                break;
                            } else {
                                j++;
                            }
                        }
                    }
                } else {
                    i++;
                }
            }
        }
    }

    public List<GoodixUserInfo> getUserInfo() {
        ArrayList userInfoCopy;
        synchronized (this) {
            userInfoCopy = getUserInfoCopy(this.mUserInfo);
        }
        return userInfoCopy;
    }

    public void loadFingerprintsForUser(String userId) {
        synchronized (this) {
            this.mFingerprintFile = getFingerprintFile(userId);
            readFpSyncLocked();
        }
    }

    public List<GoodixFpInfo> getFingerprints(String userId) {
        ArrayList fpInfoCopy;
        synchronized (this) {
            this.mFingerprintFile = getFingerprintFile(userId);
            readFpSyncLocked();
            fpInfoCopy = getFpInfoCopy(this.mFingerprints);
        }
        return fpInfoCopy;
    }

    private File getUserInfoFile() {
        return new File(this.mCtx.getFilesDir(), USER_INFO_FILE);
    }

    private File getFingerprintFile(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getFingerprintFile  = ");
        sb.append(this.mCtx.getFilesDir());
        sb.append("/");
        sb.append(String.valueOf(getGroupIdForUser(userId)));
        sb.append("/");
        sb.append(FINGERPRINT_FILE);
        Log.d(str, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mCtx.getFilesDir());
        sb2.append("/");
        sb2.append(String.valueOf(getGroupIdForUser(userId)));
        sb2.append("/");
        return new File(sb2.toString(), FINGERPRINT_FILE);
    }

    private void createFpInfoDir(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("createFpInfoDir userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mCtx.getFilesDir());
        sb2.append("/");
        sb2.append(String.valueOf(getGroupIdForUser(userId)));
        File dir = new File(sb2.toString());
        if (!dir.exists()) {
            dir.mkdirs();
            loadFingerprintsForUser(userId);
        }
    }

    public int getGroupIdForUser(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getGroupIdForUser userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        for (int i = 0; i < this.mUserInfo.size(); i++) {
            if (userId.equals(((GoodixUserInfo) this.mUserInfo.get(i)).getUserId())) {
                return ((GoodixUserInfo) this.mUserInfo.get(i)).getGroupId();
            }
        }
        return -1;
    }

    private void scheduleWriteUserInfoLocked() {
        AsyncTask.execute(this.mWriteUserInfoRunnable);
    }

    private ArrayList<GoodixUserInfo> getUserInfoCopy(ArrayList<GoodixUserInfo> array) {
        ArrayList<GoodixUserInfo> result = new ArrayList<>(array.size());
        for (int i = 0; i < array.size(); i++) {
            GoodixUserInfo ui = (GoodixUserInfo) array.get(i);
            result.add(new GoodixUserInfo(ui.getUserId(), ui.getGroupId(), ui.getPwd()));
        }
        return result;
    }

    /* access modifiers changed from: private */
    public void doWriteUserInfo() {
        ArrayList<GoodixUserInfo> userInfo;
        AtomicFile destination = new AtomicFile(this.mUserInfoFile);
        synchronized (this) {
            userInfo = getUserInfoCopy(this.mUserInfo);
        }
        try {
            FileOutputStream out = destination.startWrite();
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(out, "utf-8");
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.startTag(null, TAG_USER_INFOS);
            int count = userInfo.size();
            for (int i = 0; i < count; i++) {
                GoodixUserInfo ui = (GoodixUserInfo) userInfo.get(i);
                serializer.startTag(null, TAG_USER_INFO);
                serializer.attribute(null, "userId", ui.getUserId().toString());
                serializer.attribute(null, ATTR_GROUP_ID, Integer.toString(ui.getGroupId()));
                if (ui.getPwd() != null) {
                    serializer.attribute(null, ATTR_PASSWORD, ui.getPwd().toString());
                }
                serializer.endTag(null, TAG_USER_INFO);
            }
            serializer.endTag(null, TAG_USER_INFOS);
            serializer.endDocument();
            destination.finishWrite(out);
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    null.close();
                } catch (IOException e2) {
                }
            }
            throw th;
        }
    }

    private void readUserInfoSyncLocked() {
        Log.d(TAG, "readUserInfoSyncLocked");
        if (this.mUserInfoFile.exists()) {
            try {
                FileInputStream in = new FileInputStream(this.mUserInfoFile);
                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(in, null);
                    parseUserInfoLocked(parser);
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                } catch (Exception e2) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failed parsing user info file: ");
                    sb.append(this.mUserInfoFile);
                    throw new IllegalStateException(sb.toString(), e2);
                } catch (Throwable th) {
                    try {
                        in.close();
                    } catch (IOException e3) {
                    }
                    throw th;
                }
            } catch (FileNotFoundException e4) {
                Log.i(TAG, "No fingerprint state");
            }
        }
    }

    private void parseUserInfoLocked(XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.d(TAG, "parseUserInfoLocked");
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == 1) {
                return;
            }
            if (type == 3 && parser.getDepth() <= outerDepth) {
                return;
            }
            if (!(type == 3 || type == 4 || !parser.getName().equals(TAG_USER_INFOS))) {
                parseUserInformationLocked(parser);
            }
        }
    }

    private void parseUserInformationLocked(XmlPullParser parser) throws IOException, XmlPullParserException {
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == 1) {
                return;
            }
            if (type == 3 && parser.getDepth() <= outerDepth) {
                return;
            }
            if (!(type == 3 || type == 4 || !parser.getName().equals(TAG_USER_INFO))) {
                String userId = parser.getAttributeValue(null, "userId");
                String groupId = parser.getAttributeValue(null, ATTR_GROUP_ID);
                String password = parser.getAttributeValue(null, ATTR_PASSWORD);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("parseUserInformationLocked userId=");
                sb.append(userId);
                sb.append(",groupId=");
                sb.append(groupId);
                sb.append(",password=");
                sb.append(password);
                Log.d(str, sb.toString());
                this.mUserInfo.add(new GoodixUserInfo(userId, Integer.parseInt(groupId), password));
            }
        }
    }

    private void scheduleWriteFpLocked(String userId) {
        Log.e(TAG, "scheduleWriteFpLocked");
        createFpInfoDir(userId);
        this.mFingerprintFile = getFingerprintFile(userId);
        doWriteFpState();
    }

    private ArrayList<GoodixFpInfo> getFpInfoCopy(ArrayList<GoodixFpInfo> array) {
        ArrayList<GoodixFpInfo> result = new ArrayList<>(array.size());
        for (int i = 0; i < array.size(); i++) {
            GoodixFpInfo fp = (GoodixFpInfo) array.get(i);
            result.add(new GoodixFpInfo(fp.getFingerIndex(), fp.getGroupId(), fp.getFingerId()));
        }
        return result;
    }

    /* access modifiers changed from: private */
    public void doWriteFpState() {
        ArrayList<GoodixFpInfo> fingerprints;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("doWriteFpState mFingerprintFile = ");
        sb.append(this.mFingerprintFile.getAbsolutePath());
        Log.e(str, sb.toString());
        AtomicFile destination = new AtomicFile(this.mFingerprintFile);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("doWriteFpState mFingerprints getGroupIdForUser= ");
        sb2.append(getGroupIdForUser(this.mCurUserId));
        Log.e(str2, sb2.toString());
        String str3 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("doWriteFpState mFingerprints size= ");
        sb3.append(this.mFingerprints.size());
        Log.e(str3, sb3.toString());
        for (int i = 0; i < this.mFingerprints.size(); i++) {
            String str4 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("doWriteFpState mFingerprints getGroupId= ");
            sb4.append(((GoodixFpInfo) this.mFingerprints.get(i)).getGroupId());
            Log.e(str4, sb4.toString());
            String str5 = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("doWriteFpState mFingerprints getFingerId= ");
            sb5.append(((GoodixFpInfo) this.mFingerprints.get(i)).getFingerId());
            Log.e(str5, sb5.toString());
            String str6 = TAG;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("doWriteFpState mFingerprints getFingerIndex= ");
            sb6.append(((GoodixFpInfo) this.mFingerprints.get(i)).getFingerIndex());
            Log.e(str6, sb6.toString());
        }
        synchronized (this) {
            fingerprints = getFpInfoCopy(this.mFingerprints);
        }
        try {
            FileOutputStream out = destination.startWrite();
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(out, "utf-8");
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.startTag(null, TAG_FINGERPRINTS);
            int count = fingerprints.size();
            int currGroupId = getGroupIdForUser(this.mCurUserId);
            for (int i2 = 0; i2 < count; i2++) {
                GoodixFpInfo fp = (GoodixFpInfo) fingerprints.get(i2);
                if (currGroupId == fp.getGroupId()) {
                    serializer.startTag(null, TAG_FINGERPRINT);
                    serializer.attribute(null, ATTR_INDEX, Integer.toString(fp.getFingerIndex()));
                    serializer.attribute(null, ATTR_GROUP_ID, Integer.toString(fp.getGroupId()));
                    serializer.attribute(null, ATTR_FINGER_ID, Integer.toString(fp.getFingerId()));
                    serializer.endTag(null, TAG_FINGERPRINT);
                }
            }
            serializer.endTag(null, TAG_FINGERPRINTS);
            serializer.endDocument();
            destination.finishWrite(out);
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    null.close();
                } catch (IOException e2) {
                }
            }
            throw th;
        }
    }

    private void readFpSyncLocked() {
        if (!this.mFingerprintFile.exists()) {
            Log.i(TAG, "readFpSyncLocked No mFingerprintFile");
            return;
        }
        String str = TAG;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("FingerprintFile = ");
            sb.append(this.mFingerprintFile.getAbsolutePath());
            Log.i(str, sb.toString());
            FileInputStream in = new FileInputStream(this.mFingerprintFile);
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(in, null);
                parseFpLocked(parser);
                try {
                    in.close();
                } catch (IOException e) {
                }
            } catch (Exception e2) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed parsing fingerprint file: ");
                sb2.append(this.mFingerprintFile);
                throw new IllegalStateException(sb2.toString(), e2);
            } catch (Throwable th) {
                try {
                    in.close();
                } catch (IOException e3) {
                }
                throw th;
            }
        } catch (FileNotFoundException e4) {
            Log.i(TAG, "No fingerprint state");
        }
    }

    private void parseFpLocked(XmlPullParser parser) throws IOException, XmlPullParserException {
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == 1) {
                return;
            }
            if (type == 3 && parser.getDepth() <= outerDepth) {
                return;
            }
            if (!(type == 3 || type == 4 || !parser.getName().equals(TAG_FINGERPRINTS))) {
                parseFingerprintsLocked(parser);
            }
        }
    }

    private void parseFingerprintsLocked(XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.i(TAG, "parseFingerprintsLocked");
        int outerDepth = parser.getDepth();
        this.mFingerprints.clear();
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == 1) {
                return;
            }
            if (type == 3 && parser.getDepth() <= outerDepth) {
                return;
            }
            if (!(type == 3 || type == 4 || !parser.getName().equals(TAG_FINGERPRINT))) {
                this.mFingerprints.add(new GoodixFpInfo(Integer.parseInt(parser.getAttributeValue(null, ATTR_INDEX)), Integer.parseInt(parser.getAttributeValue(null, ATTR_GROUP_ID)), Integer.parseInt(parser.getAttributeValue(null, ATTR_FINGER_ID))));
            }
        }
    }
}
