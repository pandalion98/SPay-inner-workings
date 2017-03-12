package android.sec.clipboard.data.file;

import android.content.Context;
import android.os.Handler;
import android.os.SELinux;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.data.ClipboardData;
import android.sec.clipboard.data.list.ClipboardDataHTMLFragment;
import android.sec.clipboard.data.list.ClipboardDataHtml;
import android.sec.clipboard.util.FileHelper;
import android.sec.clipboard.util.StringHelper;
import android.text.format.DateUtils;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class FileManager {
    private static final int CLEAR_DELETE_LIST_TIMER = 60000;
    private static final String TAG = "Clipboard.FileManager";
    private ConnectFileSystem mConnectFileSystem = null;
    private Context mContext;
    private ArrayList<WrapFileClipData> mDataList;
    private ArrayList<File> mDeleteFileList = new ArrayList();
    public int mHandleID;

    class ConnectFileSystem {
        ArrayList<WrapFileClipData> dataList;
        private FileHelper fileHelper;
        private int gcLifeCount = 25;
        private File infoFile;
        private File infoTempFile;
        private Handler mHandler = new Handler();
        private Runnable mRunnable = new Runnable() {
            public void run() {
                if (ConnectFileSystem.this.saveInfoFile()) {
                    if (ConnectFileSystem.this.fileHelper.getList(ConnectFileSystem.this.rootPath) != null && ConnectFileSystem.this.gcLifeCount < ConnectFileSystem.this.fileHelper.getList(ConnectFileSystem.this.rootPath).length) {
                        ConnectFileSystem.this.gc();
                    }
                    ConnectFileSystem.this.saveTempInfoFile();
                    FileManager.this.updateCategory();
                }
            }
        };
        private File rootPath;

        public ConnectFileSystem(File infoFile) {
            this.infoFile = infoFile;
            this.infoTempFile = new File(infoFile.getAbsolutePath() + "_temp");
            this.rootPath = infoFile.getParentFile();
            this.fileHelper = FileHelper.getInstance();
            if (infoFile.getParentFile() != null && !this.fileHelper.checkDir(infoFile.getParentFile())) {
                this.fileHelper.makeDir(infoFile.getParentFile());
            }
        }

        public WrapFileClipData createData(ClipboardData element) {
            return new WrapFileClipData(element);
        }

        public ArrayList<WrapFileClipData> createDataList() {
            return new ArrayList(20);
        }

        public void update() {
            this.mHandler.removeCallbacks(this.mRunnable);
            this.mHandler.post(this.mRunnable);
        }

        public void updateForced() {
            Log.d(FileManager.TAG, "updateForced()");
            if (saveInfoFile()) {
                if (this.fileHelper.getList(this.rootPath) != null && this.gcLifeCount < this.fileHelper.getList(this.rootPath).length) {
                    gc();
                }
                saveTempInfoFile();
                FileManager.this.updateCategory();
            }
        }

        private boolean saveInfoFile() {
            return this.fileHelper.saveObjectFile(this.infoFile.getAbsolutePath(), this.dataList);
        }

        private boolean saveTempInfoFile() {
            return this.fileHelper.saveObjectFile(this.infoTempFile.getAbsolutePath(), this.dataList);
        }

        private File[] deleteEquals(ArrayList<WrapFileClipData> base, File[] _result) {
            File[] result = (File[]) _result.clone();
            int size = result.length;
            Iterator i$ = base.iterator();
            while (i$.hasNext()) {
                File base_path = ((WrapFileClipData) i$.next()).getFile().getParentFile();
                int i = 0;
                while (i < size) {
                    if (this.infoFile.compareTo(result[i]) == 0) {
                        result[i] = this.fileHelper.getNullFile();
                    } else if (this.infoTempFile.compareTo(result[i]) == 0) {
                        result[i] = this.fileHelper.getNullFile();
                    } else if (base_path != null && base_path.compareTo(result[i]) == 0) {
                        result[i] = this.fileHelper.getNullFile();
                    }
                    i++;
                }
            }
            return result;
        }

        public ArrayList<WrapFileClipData> load() {
            ArrayList<WrapFileClipData> createDataList = createDataList();
            if (this.fileHelper.checkFile(this.infoFile)) {
                if (ClipboardConstants.DEBUG) {
                    Log.d(FileManager.TAG, "load ...info file");
                }
                try {
                    createDataList = (ArrayList) this.fileHelper.loadObjectFile(this.infoFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    createDataList = null;
                }
            }
            if (createDataList == null) {
                if (ClipboardConstants.DEBUG) {
                    Log.w(FileManager.TAG, "failed load ...clips.info file");
                }
                if (this.fileHelper.checkFile(this.infoTempFile)) {
                    if (ClipboardConstants.DEBUG) {
                        Log.d(FileManager.TAG, "load ...clips.info_temp file");
                    }
                    try {
                        createDataList = (ArrayList) this.fileHelper.loadObjectFile(this.infoTempFile);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        createDataList = null;
                    }
                }
            }
            if (createDataList == null) {
                if (ClipboardConstants.DEBUG) {
                    Log.w(FileManager.TAG, "failed to load info file. create new list.");
                }
                createDataList = createDataList();
            }
            try {
                this.dataList = loadDataList(createDataList);
            } catch (Exception e22) {
                this.dataList = createDataList();
                e22.printStackTrace();
            }
            return this.dataList;
        }

        private ArrayList<WrapFileClipData> loadDataList(ArrayList<WrapFileClipData> list) {
            ArrayList<WrapFileClipData> temp_list = createDataList();
            Iterator i$ = list.iterator();
            while (i$.hasNext()) {
                WrapFileClipData data = (WrapFileClipData) i$.next();
                if (data.load()) {
                    temp_list.add(data);
                }
            }
            return temp_list;
        }

        public void allDelete() {
        }

        public synchronized boolean save(WrapFileClipData wfClip) {
            return save(wfClip, false);
        }

        public synchronized boolean save(WrapFileClipData wfClip, boolean isForced) {
            boolean z = false;
            synchronized (this) {
                FileHelper fh = FileHelper.getInstance();
                StringBuffer sb = new StringBuffer();
                sb.append(this.rootPath);
                sb.append("/");
                sb.append(Integer.toString(wfClip.hashCode()));
                sb.append(StringHelper.getUniqueString());
                File dir = new File(sb.toString());
                if (isForced || !dir.exists()) {
                    if (!fh.checkDir(dir)) {
                        fh.makeDir(dir);
                    }
                    if (makeDataValue(dir, wfClip)) {
                        sb.append("/clip");
                        File cfile = new File(sb.toString());
                        if (fh.saveObjectFile(cfile.getAbsolutePath(), wfClip.getClipData())) {
                            if (ClipboardConstants.DEBUG) {
                                Log.d(FileManager.TAG, "ok Wrap saveData");
                            }
                            wfClip.setDir(dir);
                            wfClip.setFile(cfile);
                            z = true;
                        } else {
                            if (ClipboardConstants.DEBUG) {
                                Log.d(FileManager.TAG, "Can't Save File, Delete Directory");
                            }
                            fh.delete(dir);
                        }
                    }
                } else if (ClipboardConstants.DEBUG) {
                    Log.e(FileManager.TAG, "Saving WrapFileClipData, folder already exists");
                }
            }
            return z;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean makeDataValue(java.io.File r23, android.sec.clipboard.data.file.WrapFileClipData r24) {
            /*
            r22 = this;
            r14 = r24.getClipData();
            if (r14 == 0) goto L_0x02af;
        L_0x0006:
            r17 = r14.getFormat();
            switch(r17) {
                case 2: goto L_0x0010;
                case 3: goto L_0x0019;
                case 4: goto L_0x0170;
                case 5: goto L_0x0217;
                default: goto L_0x000d;
            };
        L_0x000d:
            r17 = 1;
        L_0x000f:
            return r17;
        L_0x0010:
            r15 = r14;
            r15 = (android.sec.clipboard.data.list.ClipboardDataText) r15;
            r15.toSave();
            r17 = 1;
            goto L_0x000f;
        L_0x0019:
            r3 = r14;
            r3 = (android.sec.clipboard.data.list.ClipboardDataBitmap) r3;
            r11 = r3.getBitmapPath();
            r17 = java.io.File.separator;
            r0 = r17;
            r12 = r11.lastIndexOf(r0);
            if (r12 >= 0) goto L_0x0143;
        L_0x002a:
            r8 = r11;
        L_0x002b:
            r4 = new java.io.File;
            r0 = r23;
            r4.<init>(r0, r8);
            r7 = r3.getParcelFileDescriptor();
            if (r7 == 0) goto L_0x0044;
        L_0x0038:
            r17 = android.sec.clipboard.util.FileHelper.getInstance();
            r0 = r17;
            r17 = r0.fileCopy(r7, r4);
            if (r17 != 0) goto L_0x0059;
        L_0x0044:
            r17 = android.sec.clipboard.util.FileHelper.getInstance();
            r18 = new java.io.File;
            r0 = r18;
            r0.<init>(r11);
            r0 = r17;
            r1 = r18;
            r17 = r0.fileCopy(r1, r4);
            if (r17 == 0) goto L_0x02af;
        L_0x0059:
            r17 = android.sec.clipboard.data.ClipboardConstants.DEBUG;
            if (r17 == 0) goto L_0x0065;
        L_0x005d:
            r17 = "Clipboard.FileManager";
            r18 = "ok path change..";
            android.util.Log.d(r17, r18);
        L_0x0065:
            r17 = 0;
            r0 = r17;
            r3.setParcelFileDescriptor(r0);
            r17 = r4.getAbsolutePath();
            r0 = r17;
            r3.setBitmapPath(r0);
            r17 = r4.getAbsolutePath();
            r18 = 509; // 0x1fd float:7.13E-43 double:2.515E-321;
            r19 = -1;
            r20 = -1;
            android.os.FileUtils.setPermissions(r17, r18, r19, r20);
            r0 = r22;
            r0 = android.sec.clipboard.data.file.FileManager.this;
            r17 = r0;
            r17 = r17.mContext;
            if (r17 == 0) goto L_0x0153;
        L_0x008e:
            r17 = android.sec.clipboard.util.FileHelper.getInstance();
            r18 = r4.getAbsolutePath();
            r0 = r22;
            r0 = android.sec.clipboard.data.file.FileManager.this;
            r19 = r0;
            r19 = r19.mContext;
            r19 = r19.getResources();
            r20 = 17105305; // 0x1050199 float:2.4429388E-38 double:8.4511436E-317;
            r19 = r19.getDimension(r20);
            r0 = r19;
            r0 = (int) r0;
            r19 = r0;
            r0 = r22;
            r0 = android.sec.clipboard.data.file.FileManager.this;
            r20 = r0;
            r20 = r20.mContext;
            r20 = r20.getResources();
            r21 = 17105304; // 0x1050198 float:2.4429385E-38 double:8.451143E-317;
            r20 = r20.getDimension(r21);
            r0 = r20;
            r0 = (int) r0;
            r20 = r0;
            r17.createThumnailImage(r18, r19, r20);
        L_0x00cd:
            r17 = r3.HasExtraData();
            if (r17 == 0) goto L_0x013f;
        L_0x00d3:
            r2 = r3.getExtraDataPath();
            r17 = java.io.File.separator;
            r0 = r17;
            r13 = r2.lastIndexOf(r0);
            if (r13 >= 0) goto L_0x0160;
        L_0x00e1:
            r9 = r2;
        L_0x00e2:
            r5 = new java.io.File;
            r0 = r23;
            r5.<init>(r0, r9);
            r17 = r24.getClipData();
            r17 = (android.sec.clipboard.data.list.ClipboardDataBitmap) r17;
            r6 = r17.getExtraParcelFileDescriptor();
            if (r6 == 0) goto L_0x0101;
        L_0x00f5:
            r17 = android.sec.clipboard.util.FileHelper.getInstance();
            r0 = r17;
            r17 = r0.fileCopy(r6, r5);
            if (r17 != 0) goto L_0x0116;
        L_0x0101:
            r17 = android.sec.clipboard.util.FileHelper.getInstance();
            r18 = new java.io.File;
            r0 = r18;
            r0.<init>(r2);
            r0 = r17;
            r1 = r18;
            r17 = r0.fileCopy(r1, r5);
            if (r17 == 0) goto L_0x013f;
        L_0x0116:
            r17 = android.sec.clipboard.data.ClipboardConstants.DEBUG;
            if (r17 == 0) goto L_0x0122;
        L_0x011a:
            r17 = "Clipboard.FileManager";
            r18 = "ok ExtraDataPath change..";
            android.util.Log.d(r17, r18);
        L_0x0122:
            r17 = 0;
            r0 = r17;
            r3.setExtraParcelFileDescriptor(r0);
            r17 = r5.getAbsolutePath();
            r0 = r17;
            r3.SetExtraDataPath(r0);
            r17 = r5.getAbsolutePath();
            r18 = 509; // 0x1fd float:7.13E-43 double:2.515E-321;
            r19 = -1;
            r20 = -1;
            android.os.FileUtils.setPermissions(r17, r18, r19, r20);
        L_0x013f:
            r17 = 1;
            goto L_0x000f;
        L_0x0143:
            r17 = r12 + 1;
            r18 = r11.length();
            r0 = r17;
            r1 = r18;
            r8 = r11.substring(r0, r1);
            goto L_0x002b;
        L_0x0153:
            r17 = android.sec.clipboard.util.FileHelper.getInstance();
            r18 = r4.getAbsolutePath();
            r17.createThumnailImage(r18);
            goto L_0x00cd;
        L_0x0160:
            r17 = r13 + 1;
            r18 = r2.length();
            r0 = r17;
            r1 = r18;
            r9 = r2.substring(r0, r1);
            goto L_0x00e2;
        L_0x0170:
            r10 = r14;
            r10 = (android.sec.clipboard.data.list.ClipboardDataHtml) r10;
            r11 = r10.getFirstImgPath();
            r17 = "";
            r0 = r17;
            if (r11 == r0) goto L_0x0213;
        L_0x017d:
            r17 = r11.length();
            if (r17 <= 0) goto L_0x0213;
        L_0x0183:
            r17 = java.io.File.separator;
            r0 = r17;
            r12 = r11.lastIndexOf(r0);
            if (r12 >= 0) goto L_0x0203;
        L_0x018d:
            r8 = r11;
        L_0x018e:
            r4 = new java.io.File;
            r0 = r23;
            r4.<init>(r0, r8);
            r17 = r24.getClipData();
            r7 = r17.getParcelFileDescriptor();
            if (r7 == 0) goto L_0x01ab;
        L_0x019f:
            r17 = android.sec.clipboard.util.FileHelper.getInstance();
            r0 = r17;
            r17 = r0.fileCopy(r7, r4);
            if (r17 != 0) goto L_0x01c0;
        L_0x01ab:
            r17 = android.sec.clipboard.util.FileHelper.getInstance();
            r18 = new java.io.File;
            r0 = r18;
            r0.<init>(r11);
            r0 = r17;
            r1 = r18;
            r17 = r0.fileCopy(r1, r4);
            if (r17 == 0) goto L_0x02af;
        L_0x01c0:
            r17 = android.sec.clipboard.data.ClipboardConstants.DEBUG;
            if (r17 == 0) goto L_0x01cc;
        L_0x01c4:
            r17 = "Clipboard.FileManager";
            r18 = "ok path change..";
            android.util.Log.d(r17, r18);
        L_0x01cc:
            r17 = 0;
            r0 = r17;
            r10.setParcelFileDescriptor(r0);
            r17 = r4.getAbsolutePath();
            r0 = r17;
            r10.setFirstImgPath(r0);
            r17 = r4.getAbsolutePath();
            r18 = 509; // 0x1fd float:7.13E-43 double:2.515E-321;
            r19 = -1;
            r20 = -1;
            android.os.FileUtils.setPermissions(r17, r18, r19, r20);
            r17 = "previewhtemlclipboarditem";
            r0 = r17;
            r17 = r11.contains(r0);
            if (r17 != 0) goto L_0x01ff;
        L_0x01f4:
            r17 = android.sec.clipboard.util.FileHelper.getInstance();
            r18 = r4.getAbsolutePath();
            r17.createThumnailImage(r18);
        L_0x01ff:
            r17 = 1;
            goto L_0x000f;
        L_0x0203:
            r17 = r12 + 1;
            r18 = r11.length();
            r0 = r17;
            r1 = r18;
            r8 = r11.substring(r0, r1);
            goto L_0x018e;
        L_0x0213:
            r17 = 1;
            goto L_0x000f;
        L_0x0217:
            r16 = r14;
            r16 = (android.sec.clipboard.data.list.ClipboardDataUri) r16;
            r11 = r16.getPreviewImgPath();
            r17 = "";
            r0 = r17;
            if (r11 == r0) goto L_0x02ab;
        L_0x0225:
            r17 = r11.length();
            if (r17 <= 0) goto L_0x02ab;
        L_0x022b:
            r17 = java.io.File.separator;
            r0 = r17;
            r12 = r11.lastIndexOf(r0);
            if (r12 >= 0) goto L_0x029c;
        L_0x0235:
            r8 = r11;
        L_0x0236:
            r4 = new java.io.File;
            r0 = r23;
            r4.<init>(r0, r8);
            r17 = r24.getClipData();
            r7 = r17.getParcelFileDescriptor();
            if (r7 == 0) goto L_0x0253;
        L_0x0247:
            r17 = android.sec.clipboard.util.FileHelper.getInstance();
            r0 = r17;
            r17 = r0.fileCopy(r7, r4);
            if (r17 != 0) goto L_0x0268;
        L_0x0253:
            r17 = android.sec.clipboard.util.FileHelper.getInstance();
            r18 = new java.io.File;
            r0 = r18;
            r0.<init>(r11);
            r0 = r17;
            r1 = r18;
            r17 = r0.fileCopy(r1, r4);
            if (r17 == 0) goto L_0x02af;
        L_0x0268:
            r17 = android.sec.clipboard.data.ClipboardConstants.DEBUG;
            if (r17 == 0) goto L_0x0274;
        L_0x026c:
            r17 = "Clipboard.FileManager";
            r18 = "ok path change..";
            android.util.Log.d(r17, r18);
        L_0x0274:
            r17 = 0;
            r16.setParcelFileDescriptor(r17);
            r17 = r4.getAbsolutePath();
            r16.setPreviewImgPath(r17);
            r17 = r4.getAbsolutePath();
            r18 = 509; // 0x1fd float:7.13E-43 double:2.515E-321;
            r19 = -1;
            r20 = -1;
            android.os.FileUtils.setPermissions(r17, r18, r19, r20);
            r17 = android.sec.clipboard.util.FileHelper.getInstance();
            r18 = r4.getAbsolutePath();
            r17.createThumnailImage(r18);
            r17 = 1;
            goto L_0x000f;
        L_0x029c:
            r17 = r12 + 1;
            r18 = r11.length();
            r0 = r17;
            r1 = r18;
            r8 = r11.substring(r0, r1);
            goto L_0x0236;
        L_0x02ab:
            r17 = 1;
            goto L_0x000f;
        L_0x02af:
            r17 = 0;
            goto L_0x000f;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.sec.clipboard.data.file.FileManager.ConnectFileSystem.makeDataValue(java.io.File, android.sec.clipboard.data.file.WrapFileClipData):boolean");
        }

        public void dump() {
            if (ClipboardConstants.DEBUG) {
                Log.d(FileManager.TAG, "===========================================================");
            }
            Iterator i$ = this.dataList.iterator();
            while (i$.hasNext()) {
                WrapFileClipData wrap = (WrapFileClipData) i$.next();
                if (ClipboardConstants.INFO_DEBUG && wrap.getClipData() != null) {
                    Log.d(FileManager.TAG, wrap.getClipData().toString());
                    Log.d(FileManager.TAG, "file : " + wrap.getFile().getAbsolutePath());
                }
            }
            if (ClipboardConstants.DEBUG) {
                Log.d(FileManager.TAG, "===========================================================");
            }
        }

        public void gc() {
            if (ClipboardConstants.DEBUG) {
                Log.d(FileManager.TAG, "run gc()1");
            }
            File[] list = this.fileHelper.getList(this.rootPath);
            if (list != null && this.dataList != null) {
                list = deleteEquals(this.dataList, list);
                int size = list.length;
                for (int i = 0; i < size; i++) {
                    if (list[i].compareTo(this.fileHelper.getNullFile()) != 0) {
                        if (ClipboardConstants.INFO_DEBUG) {
                            Log.d(FileManager.TAG, "list [ " + i + " ] : " + list[i].getAbsolutePath());
                        }
                        this.fileHelper.delete(list[i]);
                    }
                }
            }
        }

        public void removeDB() {
            if (ClipboardConstants.DEBUG) {
                Log.i(FileManager.TAG, "removeDB()_rootPath :" + this.rootPath);
            }
            this.fileHelper.delete(this.rootPath);
        }

        public void removeKNOXClipFilder() {
            if (ClipboardConstants.DEBUG) {
                Log.d(FileManager.TAG, "run removeKNOXClipFilder()");
            }
            File[] list = this.fileHelper.getList(this.rootPath);
            if (list != null && this.dataList != null) {
                list = deleteEquals(this.dataList, list);
                int size = list.length;
                int i = 0;
                while (i < size) {
                    if (list[i].compareTo(this.fileHelper.getNullFile()) != 0 && (list[i].getAbsolutePath() == null || !list[i].getAbsolutePath().contains("clips.info"))) {
                        this.fileHelper.delete(list[i]);
                    }
                    i++;
                }
            }
        }
    }

    public FileManager(File infoFile, int id, Context context) {
        this.mContext = context;
        this.mConnectFileSystem = new ConnectFileSystem(infoFile);
        this.mDataList = this.mConnectFileSystem.load();
        this.mHandleID = id;
        updateList();
    }

    public int getHandleId() {
        return this.mHandleID;
    }

    public void refresh() {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "refresh() - reload the mDataList ");
        }
        if (this.mConnectFileSystem != null) {
            this.mDataList = this.mConnectFileSystem.load();
        }
    }

    public void removeClipboardDB() {
        if (this.mConnectFileSystem != null) {
            this.mConnectFileSystem.removeDB();
            this.mConnectFileSystem = null;
        }
    }

    public ClipboardData set(int index, ClipboardData element) {
        WrapFileClipData wrap = (WrapFileClipData) this.mDataList.get(index);
        wrap.setClipData(element);
        wrap.setProtectState(element.GetProtectState());
        wrap = (WrapFileClipData) this.mDataList.set(index, wrap);
        if (wrap != null) {
            this.mConnectFileSystem.update();
        }
        if (wrap != null) {
            return wrap.getClipData();
        }
        return null;
    }

    private void updateCategory() {
        if (this.mHandleID >= 1100 && this.mHandleID <= ClipboardConstants.PERSONA_CATEGORY_END) {
            if (ClipboardConstants.DEBUG) {
                Log.i(TAG, "RS: FileManager, add: knox case, user: " + (this.mHandleID - 1000));
            }
            SELinux.restorecon_with_category("/data/clipboard" + String.valueOf(this.mHandleID - 1000), this.mHandleID - 1000);
        } else if (this.mHandleID == 102) {
            if (ClipboardConstants.DEBUG) {
                Log.i(TAG, "FileManager, add: GOOD CATEGORY, adding clip, cat value:" + this.mHandleID);
            }
            SELinux.restoreconRecursive(new File("/data/clipboard/secontainer/" + String.valueOf(this.mHandleID)));
        } else if (this.mHandleID >= 201 && this.mHandleID <= 500) {
            if (ClipboardConstants.DEBUG) {
                Log.i(TAG, "FileManager, add: GENERIC CONTAINER CATEGORY, mHandleID" + this.mHandleID);
            }
            SELinux.restorecon_with_category("/data/clipboard/secontainer/" + String.valueOf(this.mHandleID), this.mHandleID);
        }
    }

    public boolean add(ClipboardData element) {
        if (!this.mDataList.add(this.mConnectFileSystem.createData(element))) {
            return false;
        }
        this.mConnectFileSystem.update();
        return true;
    }

    public boolean add(int index, ClipboardData element) {
        if (ClipboardConstants.DEBUG) {
            Log.d(TAG, "add data..file system, handleid:" + this.mHandleID);
        }
        WrapFileClipData wrap = this.mConnectFileSystem.createData(element);
        if (!this.mConnectFileSystem.save(wrap)) {
            return false;
        }
        this.mDataList.add(index, wrap);
        this.mConnectFileSystem.update();
        return true;
    }

    private boolean deleteDirectoryContent(File dir) {
        if (!dir.isDirectory()) {
            return false;
        }
        String[] children = dir.list();
        for (String file : children) {
            new File(dir, file).delete();
        }
        return dir.delete();
    }

    public boolean removeAll(boolean isOwner, int userId) {
        boolean bRes = false;
        if (isOwner) {
            for (int i = this.mDataList.size() - 1; i >= 0; i--) {
                WrapFileClipData wrap = (WrapFileClipData) this.mDataList.get(i);
                ClipboardData clipboardData = wrap.getClipData();
                if (!(clipboardData == null || clipboardData.GetProtectState() || !this.mDataList.remove(wrap))) {
                    File dir = wrap.getDir();
                    if (dir == null) {
                        dir = wrap.getFile().getParentFile();
                    }
                    if (dir != null) {
                        FileHelper.getInstance().delete(dir);
                        bRes = true;
                    }
                }
            }
        } else {
            Iterator i$ = this.mDataList.iterator();
            while (i$.hasNext()) {
                WrapFileClipData data = (WrapFileClipData) i$.next();
                if (!data.getClipData().GetProtectState()) {
                    data.addDeletedUserList(userId);
                    bRes = true;
                }
            }
        }
        if (bRes) {
            this.mConnectFileSystem.update();
        }
        return bRes;
    }

    public void resetOwnerClips(int userId) {
        Iterator i$ = this.mDataList.iterator();
        while (i$.hasNext()) {
            ((WrapFileClipData) i$.next()).resetOwnerClips(userId);
        }
    }

    public void reAddForKnox(int index) {
        ((WrapFileClipData) this.mDataList.get(index)).reAddForKnox();
    }

    public ArrayList<ClipboardData> getNonDeletedClips(int userId) {
        ArrayList<ClipboardData> realDataList = new ArrayList();
        Iterator i$ = this.mDataList.iterator();
        while (i$.hasNext()) {
            WrapFileClipData data = (WrapFileClipData) i$.next();
            if (!data.isDeletedUser(userId)) {
                realDataList.add(data.getClipData());
            }
        }
        return realDataList;
    }

    public ArrayList<WrapFileClipData> getNonDeletedClipsFromKnox(int userId) {
        ArrayList<WrapFileClipData> realDataList = new ArrayList();
        Iterator i$ = this.mDataList.iterator();
        while (i$.hasNext()) {
            WrapFileClipData data = (WrapFileClipData) i$.next();
            if (!data.isDeletedUser(userId)) {
                realDataList.add(data);
            }
        }
        return realDataList;
    }

    public ClipboardData remove(boolean isOwner, int userId, int index) {
        if (isOwner) {
            return remove(index);
        }
        int realIndex = 0;
        Iterator i$ = this.mDataList.iterator();
        while (i$.hasNext()) {
            WrapFileClipData wrap = (WrapFileClipData) i$.next();
            if (!wrap.isDeletedUser(userId)) {
                int realIndex2 = realIndex + 1;
                if (index == realIndex) {
                    Log.d(TAG, "index is " + realIndex2);
                    wrap.addDeletedUserList(userId);
                    ClipboardData retData = wrap.getClipData();
                    this.mConnectFileSystem.update();
                    realIndex = realIndex2;
                    return retData;
                }
                realIndex = realIndex2;
            }
        }
        return null;
    }

    public ClipboardData remove(int index) {
        WrapFileClipData wrap = (WrapFileClipData) this.mDataList.remove(index);
        if (wrap != null) {
            File dir = wrap.getDir();
            if (!(dir == null || deleteDirectoryContent(dir))) {
                Log.d(TAG, "failed removing data : " + dir);
            }
        }
        this.mConnectFileSystem.update();
        if (wrap == null) {
            return null;
        }
        return wrap.getClipData();
    }

    public boolean remove(ClipboardData clip) {
        if (!this.mDataList.remove(clip)) {
            return false;
        }
        this.mConnectFileSystem.update();
        return true;
    }

    public void clear() {
        this.mConnectFileSystem.allDelete();
        this.mDataList.clear();
    }

    public int size() {
        return this.mDataList.size();
    }

    public int sharedSize(int userId) {
        int sharedSize = 0;
        Iterator i$ = this.mDataList.iterator();
        while (i$.hasNext()) {
            if (!((WrapFileClipData) i$.next()).isDeletedUser(userId)) {
                sharedSize++;
            }
        }
        return sharedSize;
    }

    public ClipboardData get(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }
        return ((WrapFileClipData) this.mDataList.get(index)).getClipData();
    }

    public WrapFileClipData getWrap(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }
        return (WrapFileClipData) this.mDataList.get(index);
    }

    public void updateList() {
        boolean updated = false;
        this.mDeleteFileList.clear();
        long timestamp = 0;
        try {
            int listSize = this.mDataList.size();
            int i = 0;
            while (i < listSize) {
                WrapFileClipData data = (WrapFileClipData) this.mDataList.get(i);
                if (data != null) {
                    ClipboardData clipboard = data.getClipData();
                    if (clipboard != null) {
                        File dir = data.getDir();
                        if (clipboard instanceof ClipboardDataHTMLFragment) {
                            ClipboardData html = new ClipboardDataHtml((ClipboardDataHTMLFragment) clipboard);
                            if (timestamp == 0) {
                                timestamp = clipboard.getTimestamp();
                            }
                            html.setTimestamp(timestamp);
                            data.setClipData(html);
                            this.mDeleteFileList.add(dir);
                            this.mConnectFileSystem.save(data, true);
                            timestamp -= ClipboardConstants.TIMESTAMP_GAP;
                            clipboard = html;
                            updated = true;
                        }
                        if (!clipboard.isValidData()) {
                            this.mDeleteFileList.add(dir);
                            this.mDataList.remove(i);
                            i--;
                            listSize--;
                            Log.d(TAG, "updateList invalid data : " + clipboard.getFormat() + ", " + clipboard.getTimestamp());
                            updated = true;
                        } else if (clipboard.getStateToSave()) {
                            if (timestamp == 0) {
                                timestamp = clipboard.getTimestamp();
                            }
                            clipboard.setTimestamp(timestamp);
                            timestamp -= ClipboardConstants.TIMESTAMP_GAP;
                            this.mDeleteFileList.add(dir);
                            Log.d(TAG, "updateList timestamp : " + clipboard.getTimestamp());
                            this.mConnectFileSystem.save(data, true);
                            updated = true;
                        }
                    } else {
                        this.mDataList.remove(i);
                        i--;
                        listSize--;
                        Log.d(TAG, "updateList null");
                        updated = true;
                    }
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            updated = true;
        }
        if (updated) {
            Log.d(TAG, "updateList update!");
            this.mConnectFileSystem.updateForced();
            clearDeleteList();
        }
    }

    private void clearDeleteList() {
        Log.d(TAG, "clearDeleteList");
        new Handler().postDelayed(new Runnable() {
            public void run() {
                int size = FileManager.this.mDeleteFileList.size();
                int i = 0;
                while (i < size) {
                    File f = (File) FileManager.this.mDeleteFileList.get(i);
                    if (f != null) {
                        if (FileManager.this.deleteDirectoryContent(f)) {
                            Log.d(FileManager.TAG, "successed remove in clearDeleteList : " + f);
                            FileManager.this.mDeleteFileList.remove(i);
                            i--;
                            size--;
                        } else {
                            Log.d(FileManager.TAG, "failed remove in clearDeleteList : " + f);
                        }
                    }
                    i++;
                }
                Log.d(FileManager.TAG, "finish clearDeleteList");
            }
        }, DateUtils.MINUTE_IN_MILLIS);
    }

    public FileManager(File infoFile, File infoFileKNOX, int containerID, int id) {
    }

    public FileManager(File infoFile, File infoFileKNOX, int containerID, int id, Context context) {
    }

    public void UpdateClipboardDB(int format) {
    }

    public FileManager(File infoFile, int id) {
    }
}
