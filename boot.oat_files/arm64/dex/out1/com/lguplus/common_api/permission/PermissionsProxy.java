package com.lguplus.common_api.permission;

import android.content.pm.IPackageManager.Stub;
import android.content.pm.PackageParser.Package;
import android.content.res.AssetManager;
import android.util.Log;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PermissionsProxy {
    static final String PERMS_CLS_NAME = "com.lguplus.common_api_impl.permission.PermissionsImpl";
    static final String PERMS_PKG_NAME = "com.lguplus.common_api_impl";
    Permissions mPerms = null;
    Stub mPkgMgr;

    static class PermsClassLoader extends PathClassLoader {
        public PermsClassLoader(String path, ClassLoader parent) {
            super(path, parent);
        }

        public Class<?> loadClass(String clsName) throws ClassNotFoundException {
            if (clsName.equals(PermissionsProxy.PERMS_CLS_NAME)) {
                return findClass(clsName);
            }
            return super.loadClass(clsName);
        }
    }

    public PermissionsProxy(Stub pkgMgr) {
        this.mPkgMgr = pkgMgr;
    }

    private void getPerms() {
        try {
            this.mPerms = (Permissions) new PermsClassLoader(this.mPkgMgr.getApplicationInfo(PERMS_PKG_NAME, 0, 0).sourceDir, ClassLoader.getSystemClassLoader().getParent()).loadClass(PERMS_CLS_NAME).getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception e) {
            Log.d("uplus_common_api", "getPerms:" + e.getMessage());
        }
    }

    public boolean checkByUid(int uid, String permName) {
        if (this.mPerms == null) {
            getPerms();
        }
        return this.mPerms != null && this.mPerms.checkByUid(uid, permName);
    }

    public boolean checkByPkgName(String pkgName, String permName) {
        if (this.mPerms == null) {
            getPerms();
        }
        return this.mPerms != null && this.mPerms.checkByPkgName(pkgName, permName);
    }

    public int[] getGids(String pkgName) {
        return this.mPerms != null ? this.mPerms.getGids(pkgName) : null;
    }

    public boolean isSystemUid(String pkgName) {
        return this.mPerms != null ? this.mPerms.isSystemUid(pkgName) : false;
    }

    public boolean preverifyPkg(Package pkg) {
        String userName;
        Exception e;
        Throwable th;
        try {
            if (this.mPerms != null) {
                String token = null;
                AssetManager am = null;
                InputStream is = null;
                try {
                    AssetManager am2 = new AssetManager();
                    try {
                        int cookie = am2.addAssetPath(pkg.baseCodePath);
                        if (cookie != 0) {
                            is = am2.openNonAsset(cookie, "assets/uplus_common_api_perm_token.txt");
                            byte[] bytes = new byte[4096];
                            token = new String(bytes, 0, is.read(bytes));
                        }
                        if (is != null) {
                            try {
                                is.close();
                                am = am2;
                            } catch (IOException e2) {
                                Log.d("uplus_common_api", "preverifyPkg(finally):" + e2.getMessage());
                                am = am2;
                            }
                        } else {
                            am = am2;
                        }
                    } catch (FileNotFoundException e3) {
                        am = am2;
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e22) {
                                Log.d("uplus_common_api", "preverifyPkg(finally):" + e22.getMessage());
                            }
                        }
                        if (am != null) {
                            am.close();
                        }
                        userName = this.mPerms.beforeAdd(pkg.packageName, token, pkg.mSignatures[0].toCharsString());
                        if (userName == null) {
                        }
                    } catch (Exception e4) {
                        e = e4;
                        am = am2;
                        try {
                            Log.d("uplus_common_api", "preverifyPkg:" + e.getMessage());
                            if (is != null) {
                                try {
                                    is.close();
                                } catch (IOException e222) {
                                    Log.d("uplus_common_api", "preverifyPkg(finally):" + e222.getMessage());
                                }
                            }
                            if (am != null) {
                                am.close();
                            }
                            userName = this.mPerms.beforeAdd(pkg.packageName, token, pkg.mSignatures[0].toCharsString());
                            if (userName == null) {
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (is != null) {
                                try {
                                    is.close();
                                } catch (IOException e2222) {
                                    Log.d("uplus_common_api", "preverifyPkg(finally):" + e2222.getMessage());
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        am = am2;
                        if (is != null) {
                            is.close();
                        }
                        throw th;
                    }
                } catch (FileNotFoundException e5) {
                    if (is != null) {
                        is.close();
                    }
                    if (am != null) {
                        am.close();
                    }
                    userName = this.mPerms.beforeAdd(pkg.packageName, token, pkg.mSignatures[0].toCharsString());
                    if (userName == null) {
                    }
                } catch (Exception e6) {
                    e = e6;
                    Log.d("uplus_common_api", "preverifyPkg:" + e.getMessage());
                    if (is != null) {
                        is.close();
                    }
                    if (am != null) {
                        am.close();
                    }
                    userName = this.mPerms.beforeAdd(pkg.packageName, token, pkg.mSignatures[0].toCharsString());
                    if (userName == null) {
                    }
                }
                if (am != null) {
                    am.close();
                }
                userName = this.mPerms.beforeAdd(pkg.packageName, token, pkg.mSignatures[0].toCharsString());
                return (userName == null && userName.equals("INVALID-TOKEN")) ? false : true;
            }
        } catch (Exception e7) {
            Log.d("uplus_common_api", "preverifyPkg:" + e7.getMessage());
        }
        return false;
    }

    public void beforeAddPkg(Package pkg) {
        try {
            if (this.mPerms != null) {
                boolean isValidToken;
                String pkgName = pkg.packageName;
                String userName = null;
                if (this.mPerms.isContainsInTempPerms(pkgName)) {
                    isValidToken = true;
                } else {
                    isValidToken = preverifyPkg(pkg);
                }
                if (isValidToken) {
                    userName = this.mPerms.getUserNameInTempPerms(pkgName);
                }
                if (pkg.mSharedUserId != null) {
                    if (!isValidToken) {
                        pkg.mSharedUserId = pkg.mSharedUserId.replaceFirst(".uplus_common_api$", "");
                    } else if (!pkg.mSharedUserId.equals(userName)) {
                        pkg.mSharedUserId += ".uplus_common_api";
                    }
                } else if (isValidToken) {
                    pkg.mSharedUserId = userName;
                }
            }
        } catch (Exception e) {
            Log.d("uplus_common_api", "beforeAddPkg:" + e.getMessage());
        }
    }

    public void afterAddPkg(Package pkg) {
        if (pkg.packageName.equals(PERMS_PKG_NAME)) {
            getPerms();
        }
        if (this.mPerms != null) {
            this.mPerms.afterAdd(pkg.packageName, pkg.applicationInfo.uid);
        }
    }

    public void onDelPkg(Package pkg) {
        if (this.mPerms != null) {
            this.mPerms.del(pkg.packageName);
        }
    }

    public void reorderApkFiles(File[] files) {
        for (int i = 1; i < files.length; i++) {
            if (files[i].getName().equals("LGUPlusLinuxCommonApi.apk")) {
                File f = files[i];
                for (int j = i - 1; j >= 0; j--) {
                    files[j + 1] = files[j];
                }
                files[0] = f;
                return;
            }
        }
    }
}
