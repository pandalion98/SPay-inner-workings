package android.renderscript;

import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import android.content.res.Resources;
import java.io.File;
import java.io.InputStream;

public class FileA3D extends BaseObj {
    IndexEntry[] mFileEntries;
    InputStream mInputStream;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$renderscript$FileA3D$EntryType = new int[EntryType.values().length];

        static {
            try {
                $SwitchMap$android$renderscript$FileA3D$EntryType[EntryType.MESH.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
        }
    }

    public enum EntryType {
        UNKNOWN(0),
        MESH(1);
        
        int mID;

        private EntryType(int id) {
            this.mID = id;
        }

        static EntryType toEntryType(int intID) {
            return values()[intID];
        }
    }

    public static class IndexEntry {
        EntryType mEntryType;
        long mID;
        int mIndex;
        BaseObj mLoadedObj = null;
        String mName;
        RenderScript mRS;

        public String getName() {
            return this.mName;
        }

        public EntryType getEntryType() {
            return this.mEntryType;
        }

        public BaseObj getObject() {
            this.mRS.validate();
            return internalCreate(this.mRS, this);
        }

        public Mesh getMesh() {
            return (Mesh) getObject();
        }

        static synchronized BaseObj internalCreate(RenderScript rs, IndexEntry entry) {
            BaseObj baseObj = null;
            synchronized (IndexEntry.class) {
                if (entry.mLoadedObj != null) {
                    baseObj = entry.mLoadedObj;
                } else if (entry.mEntryType != EntryType.UNKNOWN) {
                    long objectID = rs.nFileA3DGetEntryByIndex(entry.mID, entry.mIndex);
                    if (objectID != 0) {
                        switch (AnonymousClass1.$SwitchMap$android$renderscript$FileA3D$EntryType[entry.mEntryType.ordinal()]) {
                            case 1:
                                entry.mLoadedObj = new Mesh(objectID, rs);
                                entry.mLoadedObj.updateFromNative();
                                baseObj = entry.mLoadedObj;
                                break;
                            default:
                                throw new RSRuntimeException("Unrecognized object type in file.");
                        }
                    }
                }
            }
            return baseObj;
        }

        IndexEntry(RenderScript rs, int index, long id, String name, EntryType type) {
            this.mRS = rs;
            this.mIndex = index;
            this.mID = id;
            this.mName = name;
            this.mEntryType = type;
        }
    }

    FileA3D(long id, RenderScript rs, InputStream stream) {
        super(id, rs);
        this.mInputStream = stream;
    }

    private void initEntries() {
        int numFileEntries = this.mRS.nFileA3DGetNumIndexEntries(getID(this.mRS));
        if (numFileEntries > 0) {
            this.mFileEntries = new IndexEntry[numFileEntries];
            int[] ids = new int[numFileEntries];
            String[] names = new String[numFileEntries];
            this.mRS.nFileA3DGetIndexEntries(getID(this.mRS), numFileEntries, ids, names);
            for (int i = 0; i < numFileEntries; i++) {
                this.mFileEntries[i] = new IndexEntry(this.mRS, i, getID(this.mRS), names[i], EntryType.toEntryType(ids[i]));
            }
        }
    }

    public int getIndexEntryCount() {
        if (this.mFileEntries == null) {
            return 0;
        }
        return this.mFileEntries.length;
    }

    public IndexEntry getIndexEntry(int index) {
        if (getIndexEntryCount() == 0 || index < 0 || index >= this.mFileEntries.length) {
            return null;
        }
        return this.mFileEntries[index];
    }

    public static FileA3D createFromAsset(RenderScript rs, AssetManager mgr, String path) {
        rs.validate();
        long fileId = rs.nFileA3DCreateFromAsset(mgr, path);
        if (fileId == 0) {
            throw new RSRuntimeException("Unable to create a3d file from asset " + path);
        }
        FileA3D fa3d = new FileA3D(fileId, rs, null);
        fa3d.initEntries();
        return fa3d;
    }

    public static FileA3D createFromFile(RenderScript rs, String path) {
        long fileId = rs.nFileA3DCreateFromFile(path);
        if (fileId == 0) {
            throw new RSRuntimeException("Unable to create a3d file from " + path);
        }
        FileA3D fa3d = new FileA3D(fileId, rs, null);
        fa3d.initEntries();
        return fa3d;
    }

    public static FileA3D createFromFile(RenderScript rs, File path) {
        return createFromFile(rs, path.getAbsolutePath());
    }

    public static FileA3D createFromResource(RenderScript rs, Resources res, int id) {
        rs.validate();
        try {
            InputStream is = res.openRawResource(id);
            if (is instanceof AssetInputStream) {
                long fileId = rs.nFileA3DCreateFromAssetStream(((AssetInputStream) is).getNativeAsset());
                if (fileId == 0) {
                    throw new RSRuntimeException("Unable to create a3d file from resource " + id);
                }
                FileA3D fa3d = new FileA3D(fileId, rs, is);
                fa3d.initEntries();
                return fa3d;
            }
            throw new RSRuntimeException("Unsupported asset stream");
        } catch (Exception e) {
            throw new RSRuntimeException("Unable to open resource " + id);
        }
    }
}
