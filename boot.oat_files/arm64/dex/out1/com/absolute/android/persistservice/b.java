package com.absolute.android.persistservice;

import java.io.File;
import java.io.FilenameFilter;

class b implements FilenameFilter {
    final /* synthetic */ ABTPersistenceService a;

    public b(ABTPersistenceService aBTPersistenceService) {
        this.a = aBTPersistenceService;
    }

    public boolean accept(File file, String str) {
        return str.startsWith(ABTPersistenceService.d) && str.endsWith(".apk");
    }
}
