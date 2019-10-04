/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.FileNotFoundException
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 */
package com.squareup.okhttp.internal.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import okio.Okio;
import okio.Sink;
import okio.Source;

public interface FileSystem {
    public static final FileSystem SYSTEM = new FileSystem(){

        @Override
        public Sink appendingSink(File file) {
            try {
                Sink sink = Okio.appendingSink(file);
                return sink;
            }
            catch (FileNotFoundException fileNotFoundException) {
                file.getParentFile().mkdirs();
                return Okio.appendingSink(file);
            }
        }

        @Override
        public void delete(File file) {
            if (!file.delete() && file.exists()) {
                throw new IOException("failed to delete " + (Object)file);
            }
        }

        @Override
        public void deleteContents(File file) {
            File[] arrfile = file.listFiles();
            if (arrfile == null) {
                throw new IOException("not a readable directory: " + (Object)file);
            }
            for (File file2 : arrfile) {
                if (file2.isDirectory()) {
                    this.deleteContents(file2);
                }
                if (file2.delete()) continue;
                throw new IOException("failed to delete " + (Object)file2);
            }
        }

        @Override
        public boolean exists(File file) {
            return file.exists();
        }

        @Override
        public void rename(File file, File file2) {
            this.delete(file2);
            if (!file.renameTo(file2)) {
                throw new IOException("failed to rename " + (Object)file + " to " + (Object)file2);
            }
        }

        @Override
        public Sink sink(File file) {
            try {
                Sink sink = Okio.sink(file);
                return sink;
            }
            catch (FileNotFoundException fileNotFoundException) {
                file.getParentFile().mkdirs();
                return Okio.sink(file);
            }
        }

        @Override
        public long size(File file) {
            return file.length();
        }

        @Override
        public Source source(File file) {
            return Okio.source(file);
        }
    };

    public Sink appendingSink(File var1);

    public void delete(File var1);

    public void deleteContents(File var1);

    public boolean exists(File var1);

    public void rename(File var1, File var2);

    public Sink sink(File var1);

    public long size(File var1);

    public Source source(File var1);

}

