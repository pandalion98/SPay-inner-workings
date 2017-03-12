package com.lguplus.common_api.permission;

public interface Permissions {
    void afterAdd(String str, int i);

    String beforeAdd(String str, String str2, String str3);

    boolean checkByPkgName(String str, String str2);

    boolean checkByUid(int i, String str);

    boolean del(String str);

    int[] getGids(String str);

    String getUserNameInTempPerms(String str);

    boolean isContainsInTempPerms(String str);

    boolean isSystemUid(String str);
}
