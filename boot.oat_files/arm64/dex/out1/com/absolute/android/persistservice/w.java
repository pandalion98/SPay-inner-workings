package com.absolute.android.persistservice;

import java.io.File;

public class w extends ah {
    w(String str, String str2, boolean z, v vVar) {
        super(ABTPersistenceService.a() + str + File.separatorChar + str2, z, vVar);
        File parentFile = this.b.getParentFile();
        if (!parentFile.canRead() || !parentFile.canWrite() || !parentFile.canExecute()) {
            this.a = ABTPersistenceService.a() + str2;
            this.b = new File(this.a);
        }
    }
}
