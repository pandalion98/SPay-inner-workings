package android.mtp;

import android.content.IContentProvider;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.util.Log;
import java.util.ArrayList;

class MtpPropertyGroup {
    private static final String FORMAT_WHERE = "format=?";
    private static final String ID_FORMAT_WHERE = "_id=? AND format=?";
    private static final String ID_WHERE = "_id=?";
    private static final String PARENT_FORMAT_WHERE = "parent=? AND format=?";
    private static final String PARENT_WHERE = "parent=?";
    private static final String TAG = "MtpPropertyGroup";
    private String[] mColumns;
    private final MtpDatabase mDatabase;
    private final String mPackageName;
    private final Property[] mProperties;
    private final IContentProvider mProvider;
    private final Uri mUri;
    private final String mVolumeName;

    private class Property {
        int code;
        int column;
        int type;

        Property(int code, int type, int column) {
            this.code = code;
            this.type = type;
            this.column = column;
        }
    }

    private native String format_date_time(long j);

    private java.lang.String queryAudio(int r12, java.lang.String r13) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x003b in list [B:9:0x0038]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r11 = this;
        r10 = 0;
        r8 = 0;
        r0 = r11.mProvider;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r1 = r11.mPackageName;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r2 = r11.mVolumeName;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r2 = android.provider.MediaStore.Audio.Media.getContentUri(r2);	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r3 = 2;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r3 = new java.lang.String[r3];	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r4 = 0;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r5 = "_id";	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r3[r4] = r5;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r4 = 1;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r3[r4] = r13;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r4 = "_id=?";	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r5 = 1;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r5 = new java.lang.String[r5];	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r6 = 0;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r7 = java.lang.Integer.toString(r12);	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r5[r6] = r7;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r6 = 0;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r7 = 0;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        if (r8 == 0) goto L_0x003c;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
    L_0x002b:
        r0 = r8.moveToNext();	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        if (r0 == 0) goto L_0x003c;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
    L_0x0031:
        r0 = 1;	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        r0 = r8.getString(r0);	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        if (r8 == 0) goto L_0x003b;
    L_0x0038:
        r8.close();
    L_0x003b:
        return r0;
    L_0x003c:
        r0 = "";	 Catch:{ Exception -> 0x0044, all -> 0x004c }
        if (r8 == 0) goto L_0x003b;
    L_0x0040:
        r8.close();
        goto L_0x003b;
    L_0x0044:
        r9 = move-exception;
        if (r8 == 0) goto L_0x004a;
    L_0x0047:
        r8.close();
    L_0x004a:
        r0 = r10;
        goto L_0x003b;
    L_0x004c:
        r0 = move-exception;
        if (r8 == 0) goto L_0x0052;
    L_0x004f:
        r8.close();
    L_0x0052:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpPropertyGroup.queryAudio(int, java.lang.String):java.lang.String");
    }

    private java.lang.String queryGenre(int r12) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0034 in list [B:9:0x0031]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r11 = this;
        r10 = 0;
        r8 = 0;
        r0 = r11.mVolumeName;	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r2 = android.provider.MediaStore.Audio.Genres.getContentUriForAudioId(r0, r12);	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r0 = r11.mProvider;	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r1 = r11.mPackageName;	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r3 = 2;	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r3 = new java.lang.String[r3];	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r4 = 0;	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r5 = "_id";	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r3[r4] = r5;	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r4 = 1;	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r5 = "name";	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r3[r4] = r5;	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r4 = 0;	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r5 = 0;	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r6 = 0;	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r7 = 0;	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        if (r8 == 0) goto L_0x0035;	 Catch:{ Exception -> 0x003d, all -> 0x004d }
    L_0x0024:
        r0 = r8.moveToNext();	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        if (r0 == 0) goto L_0x0035;	 Catch:{ Exception -> 0x003d, all -> 0x004d }
    L_0x002a:
        r0 = 1;	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r0 = r8.getString(r0);	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        if (r8 == 0) goto L_0x0034;
    L_0x0031:
        r8.close();
    L_0x0034:
        return r0;
    L_0x0035:
        r0 = "";	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        if (r8 == 0) goto L_0x0034;
    L_0x0039:
        r8.close();
        goto L_0x0034;
    L_0x003d:
        r9 = move-exception;
        r0 = "MtpPropertyGroup";	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        r1 = "queryGenre exception";	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        android.util.Log.e(r0, r1, r9);	 Catch:{ Exception -> 0x003d, all -> 0x004d }
        if (r8 == 0) goto L_0x004b;
    L_0x0048:
        r8.close();
    L_0x004b:
        r0 = r10;
        goto L_0x0034;
    L_0x004d:
        r0 = move-exception;
        if (r8 == 0) goto L_0x0053;
    L_0x0050:
        r8.close();
    L_0x0053:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpPropertyGroup.queryGenre(int):java.lang.String");
    }

    private java.lang.String queryString(int r12, java.lang.String r13) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0037 in list [B:9:0x0034]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r11 = this;
        r10 = 0;
        r8 = 0;
        r0 = r11.mProvider;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r1 = r11.mPackageName;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r2 = r11.mUri;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r3 = 2;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r3 = new java.lang.String[r3];	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r4 = 0;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r5 = "_id";	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r3[r4] = r5;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r4 = 1;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r3[r4] = r13;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r4 = "_id=?";	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r5 = 1;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r5 = new java.lang.String[r5];	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r6 = 0;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r7 = java.lang.Integer.toString(r12);	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r5[r6] = r7;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r6 = 0;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r7 = 0;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        if (r8 == 0) goto L_0x0038;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
    L_0x0027:
        r0 = r8.moveToNext();	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        if (r0 == 0) goto L_0x0038;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
    L_0x002d:
        r0 = 1;	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        r0 = r8.getString(r0);	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        if (r8 == 0) goto L_0x0037;
    L_0x0034:
        r8.close();
    L_0x0037:
        return r0;
    L_0x0038:
        r0 = "";	 Catch:{ Exception -> 0x0040, all -> 0x0048 }
        if (r8 == 0) goto L_0x0037;
    L_0x003c:
        r8.close();
        goto L_0x0037;
    L_0x0040:
        r9 = move-exception;
        if (r8 == 0) goto L_0x0046;
    L_0x0043:
        r8.close();
    L_0x0046:
        r0 = r10;
        goto L_0x0037;
    L_0x0048:
        r0 = move-exception;
        if (r8 == 0) goto L_0x004e;
    L_0x004b:
        r8.close();
    L_0x004e:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpPropertyGroup.queryString(int, java.lang.String):java.lang.String");
    }

    android.mtp.MtpPropertyList getPropertyList(int r33, int r34, int r35) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(Unknown Source)
	at java.util.HashMap$KeyIterator.next(Unknown Source)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:80)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r32 = this;
        r2 = 1;
        r0 = r35;
        if (r0 <= r2) goto L_0x000f;
    L_0x0005:
        r8 = new android.mtp.MtpPropertyList;
        r2 = 0;
        r3 = 43016; // 0xa808 float:6.0278E-41 double:2.12527E-319;
        r8.<init>(r2, r3);
    L_0x000e:
        return r8;
    L_0x000f:
        if (r34 != 0) goto L_0x0065;
    L_0x0011:
        r2 = -1;
        r0 = r33;
        if (r0 != r2) goto L_0x004f;
    L_0x0016:
        r6 = 0;
        r7 = 0;
    L_0x0018:
        r20 = 0;
        if (r35 > 0) goto L_0x0029;
    L_0x001c:
        r2 = -1;
        r0 = r33;
        if (r0 == r2) goto L_0x0029;
    L_0x0021:
        r0 = r32;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r0.mColumns;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r2.length;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r3 = 1;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        if (r2 <= r3) goto L_0x0094;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x0029:
        r0 = r32;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r0.mProvider;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r32;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r3 = r0.mPackageName;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r32;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r4 = r0.mUri;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r32;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r5 = r0.mColumns;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8 = 0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r9 = 0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r20 = r2.query(r3, r4, r5, r6, r7, r8, r9);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        if (r20 != 0) goto L_0x0094;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x0041:
        r8 = new android.mtp.MtpPropertyList;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = 0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r3 = 8201; // 0x2009 float:1.1492E-41 double:4.052E-320;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8.<init>(r2, r3);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        if (r20 == 0) goto L_0x000e;
    L_0x004b:
        r20.close();
        goto L_0x000e;
    L_0x004f:
        r2 = 1;
        r7 = new java.lang.String[r2];
        r2 = 0;
        r3 = java.lang.Integer.toString(r33);
        r7[r2] = r3;
        r2 = 1;
        r0 = r35;
        if (r0 != r2) goto L_0x0062;
    L_0x005e:
        r6 = "parent=?";
        goto L_0x0018;
    L_0x0062:
        r6 = "_id=?";
        goto L_0x0018;
    L_0x0065:
        r2 = -1;
        r0 = r33;
        if (r0 != r2) goto L_0x0077;
    L_0x006a:
        r6 = "format=?";
        r2 = 1;
        r7 = new java.lang.String[r2];
        r2 = 0;
        r3 = java.lang.Integer.toString(r34);
        r7[r2] = r3;
        goto L_0x0018;
    L_0x0077:
        r2 = 2;
        r7 = new java.lang.String[r2];
        r2 = 0;
        r3 = java.lang.Integer.toString(r33);
        r7[r2] = r3;
        r2 = 1;
        r3 = java.lang.Integer.toString(r34);
        r7[r2] = r3;
        r2 = 1;
        r0 = r35;
        if (r0 != r2) goto L_0x0091;
    L_0x008d:
        r6 = "parent=? AND format=?";
        goto L_0x0018;
    L_0x0091:
        r6 = "_id=? AND format=?";
        goto L_0x0018;
    L_0x0094:
        if (r20 != 0) goto L_0x00f0;
    L_0x0096:
        r22 = 1;
    L_0x0098:
        r8 = new android.mtp.MtpPropertyList;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r32;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r0.mProperties;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r2.length;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r2 * r22;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r3 = 8193; // 0x2001 float:1.1481E-41 double:4.048E-320;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8.<init>(r2, r3);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r27 = 0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x00a8:
        r0 = r27;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r1 = r22;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        if (r0 >= r1) goto L_0x0241;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x00ae:
        if (r20 == 0) goto L_0x00bd;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x00b0:
        r20.moveToNext();	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = 0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r20;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r0.getLong(r2);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = (int) r2;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r33 = r0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x00bd:
        r29 = 0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x00bf:
        r0 = r32;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r0.mProperties;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r2.length;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r29;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        if (r0 >= r2) goto L_0x023d;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x00c8:
        r0 = r32;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r0.mProperties;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r28 = r2[r29];	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r28;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r10 = r0.code;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r28;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r0.column;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r21 = r0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        switch(r10) {
            case 56323: goto L_0x00f5;
            case 56327: goto L_0x010e;
            case 56329: goto L_0x015e;
            case 56385: goto L_0x0194;
            case 56388: goto L_0x012b;
            case 56390: goto L_0x01bd;
            case 56398: goto L_0x015e;
            case 56459: goto L_0x01a8;
            case 56460: goto L_0x01df;
            case 56473: goto L_0x0170;
            case 56474: goto L_0x01ce;
            case 56978: goto L_0x0203;
            case 56979: goto L_0x01f5;
            case 56980: goto L_0x0203;
            case 56985: goto L_0x01f5;
            case 56986: goto L_0x01f5;
            default: goto L_0x00db;
        };	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x00db:
        r0 = r28;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r0.type;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r3 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        if (r2 != r3) goto L_0x0211;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x00e4:
        r2 = r20.getString(r21);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8.append(r0, r10, r2);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x00ed:
        r29 = r29 + 1;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00bf;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x00f0:
        r22 = r20.getCount();	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x0098;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x00f5:
        r11 = 4;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r12 = 0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r9 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8.append(r9, r10, r11, r12);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;
    L_0x00fe:
        r24 = move-exception;
        r8 = new android.mtp.MtpPropertyList;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = 0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r3 = 8194; // 0x2002 float:1.1482E-41 double:4.0484E-320;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8.<init>(r2, r3);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        if (r20 == 0) goto L_0x000e;
    L_0x0109:
        r20.close();
        goto L_0x000e;
    L_0x010e:
        r30 = r20.getString(r21);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        if (r30 == 0) goto L_0x0125;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x0114:
        r2 = nameFromPath(r30);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8.append(r0, r10, r2);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;
    L_0x011e:
        r2 = move-exception;
        if (r20 == 0) goto L_0x0124;
    L_0x0121:
        r20.close();
    L_0x0124:
        throw r2;
    L_0x0125:
        r2 = 8201; // 0x2009 float:1.1492E-41 double:4.052E-320;
        r8.setResult(r2);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x012b:
        r26 = r20.getString(r21);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        if (r26 != 0) goto L_0x013c;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x0131:
        r2 = "name";	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r32;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r1 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r26 = r0.queryString(r1, r2);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x013c:
        if (r26 != 0) goto L_0x014e;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x013e:
        r2 = "_data";	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r32;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r1 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r26 = r0.queryString(r1, r2);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        if (r26 == 0) goto L_0x014e;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x014a:
        r26 = nameFromPath(r26);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x014e:
        if (r26 == 0) goto L_0x0158;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x0150:
        r0 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r1 = r26;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8.append(r0, r10, r1);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x0158:
        r2 = 8201; // 0x2009 float:1.1492E-41 double:4.052E-320;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8.setResult(r2);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x015e:
        r2 = r20.getInt(r21);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = (long) r2;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r32;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r0.format_date_time(r2);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8.append(r0, r10, r2);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x0170:
        r31 = r20.getInt(r21);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2.<init>();	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r3 = java.lang.Integer.toString(r31);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r3 = "0101T000000";	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r23 = r2.toString();	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r1 = r23;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8.append(r0, r10, r1);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x0194:
        r12 = r20.getLong(r21);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = 32;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r12 = r12 << r2;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = (long) r0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r12 = r12 + r2;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r11 = 10;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r9 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8.append(r9, r10, r11, r12);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x01a8:
        r17 = 4;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r20.getInt(r21);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r2 % 1000;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = (long) r2;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r18 = r0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r14 = r8;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r15 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r16 = r10;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r14.append(r15, r16, r17, r18);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x01bd:
        r2 = "artist";	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r32;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r1 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r0.queryAudio(r1, r2);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8.append(r0, r10, r2);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x01ce:
        r2 = "album";	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r32;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r1 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r0.queryAudio(r1, r2);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8.append(r0, r10, r2);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x01df:
        r25 = r32.queryGenre(r33);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        if (r25 == 0) goto L_0x01ee;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x01e5:
        r0 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r1 = r25;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8.append(r0, r10, r1);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x01ee:
        r2 = 8201; // 0x2009 float:1.1492E-41 double:4.052E-320;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r8.setResult(r2);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x01f5:
        r17 = 6;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r18 = 0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r14 = r8;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r15 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r16 = r10;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r14.append(r15, r16, r17, r18);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x0203:
        r17 = 4;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r18 = 0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r14 = r8;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r15 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r16 = r10;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r14.append(r15, r16, r17, r18);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x0211:
        r0 = r28;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r2 = r0.type;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        if (r2 != 0) goto L_0x0229;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x0217:
        r0 = r28;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r0.type;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r17 = r0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r18 = 0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r14 = r8;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r15 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r16 = r10;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r14.append(r15, r16, r17, r18);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
    L_0x0229:
        r0 = r28;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r0 = r0.type;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r17 = r0;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r18 = r20.getLong(r21);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r14 = r8;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r15 = r33;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r16 = r10;	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        r14.append(r15, r16, r17, r18);	 Catch:{ RemoteException -> 0x00fe, all -> 0x011e }
        goto L_0x00ed;
    L_0x023d:
        r27 = r27 + 1;
        goto L_0x00a8;
    L_0x0241:
        if (r20 == 0) goto L_0x000e;
    L_0x0243:
        r20.close();
        goto L_0x000e;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpPropertyGroup.getPropertyList(int, int, int):android.mtp.MtpPropertyList");
    }

    public MtpPropertyGroup(MtpDatabase database, IContentProvider provider, String packageName, String volume, int[] properties) {
        int i;
        this.mDatabase = database;
        this.mProvider = provider;
        this.mPackageName = packageName;
        this.mVolumeName = volume;
        this.mUri = Files.getMtpObjectsUri(volume);
        int count = properties.length;
        ArrayList<String> columns = new ArrayList(count);
        columns.add("_id");
        this.mProperties = new Property[count];
        for (i = 0; i < count; i++) {
            this.mProperties[i] = createProperty(properties[i], columns);
        }
        count = columns.size();
        this.mColumns = new String[count];
        for (i = 0; i < count; i++) {
            this.mColumns[i] = (String) columns.get(i);
        }
    }

    private Property createProperty(int code, ArrayList<String> columns) {
        int type;
        String column = null;
        switch (code) {
            case MtpConstants.PROPERTY_STORAGE_ID /*56321*/:
                column = FileColumns.STORAGE_ID;
                type = 6;
                break;
            case MtpConstants.PROPERTY_OBJECT_FORMAT /*56322*/:
                column = FileColumns.FORMAT;
                type = 4;
                break;
            case MtpConstants.PROPERTY_PROTECTION_STATUS /*56323*/:
                type = 4;
                break;
            case MtpConstants.PROPERTY_OBJECT_SIZE /*56324*/:
                column = "_size";
                type = 8;
                break;
            case MtpConstants.PROPERTY_OBJECT_FILE_NAME /*56327*/:
                column = "_data";
                type = 65535;
                break;
            case MtpConstants.PROPERTY_DATE_MODIFIED /*56329*/:
                column = "date_modified";
                type = 65535;
                break;
            case MtpConstants.PROPERTY_PARENT_OBJECT /*56331*/:
                column = "parent";
                type = 6;
                break;
            case MtpConstants.PROPERTY_PERSISTENT_UID /*56385*/:
                column = FileColumns.STORAGE_ID;
                type = 10;
                break;
            case MtpConstants.PROPERTY_NAME /*56388*/:
                column = "title";
                type = 65535;
                break;
            case MtpConstants.PROPERTY_ARTIST /*56390*/:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_DESCRIPTION /*56392*/:
                column = "description";
                type = 65535;
                break;
            case MtpConstants.PROPERTY_DATE_ADDED /*56398*/:
                column = "date_added";
                type = 65535;
                break;
            case MtpConstants.PROPERTY_DURATION /*56457*/:
                column = "duration";
                type = 6;
                break;
            case MtpConstants.PROPERTY_TRACK /*56459*/:
                column = AudioColumns.TRACK;
                type = 4;
                break;
            case MtpConstants.PROPERTY_GENRE /*56460*/:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_COMPOSER /*56470*/:
                column = AudioColumns.COMPOSER;
                type = 65535;
                break;
            case MtpConstants.PROPERTY_ORIGINAL_RELEASE_DATE /*56473*/:
                column = "year";
                type = 65535;
                break;
            case MtpConstants.PROPERTY_ALBUM_NAME /*56474*/:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_ALBUM_ARTIST /*56475*/:
                column = AudioColumns.ALBUM_ARTIST;
                type = 65535;
                break;
            case MtpConstants.PROPERTY_DISPLAY_NAME /*56544*/:
                column = "_display_name";
                type = 65535;
                break;
            case MtpConstants.PROPERTY_BITRATE_TYPE /*56978*/:
            case MtpConstants.PROPERTY_NUMBER_OF_CHANNELS /*56980*/:
                type = 4;
                break;
            case MtpConstants.PROPERTY_SAMPLE_RATE /*56979*/:
            case MtpConstants.PROPERTY_AUDIO_WAVE_CODEC /*56985*/:
            case MtpConstants.PROPERTY_AUDIO_BITRATE /*56986*/:
                type = 6;
                break;
            default:
                type = 0;
                Log.e(TAG, "unsupported property " + code);
                break;
        }
        if (column == null) {
            return new Property(code, type, -1);
        }
        columns.add(column);
        return new Property(code, type, columns.size() - 1);
    }

    private Long queryLong(int id, String column) {
        Cursor c = null;
        try {
            c = this.mProvider.query(this.mPackageName, this.mUri, new String[]{"_id", column}, ID_WHERE, new String[]{Integer.toString(id)}, null, null);
            if (c == null || !c.moveToNext()) {
                if (c != null) {
                    c.close();
                }
                return null;
            }
            Long l = new Long(c.getLong(1));
            if (c == null) {
                return l;
            }
            c.close();
            return l;
        } catch (Exception e) {
            if (c != null) {
                c.close();
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
    }

    private static String nameFromPath(String path) {
        int start = 0;
        int lastSlash = path.lastIndexOf(47);
        if (lastSlash >= 0) {
            start = lastSlash + 1;
        }
        int end = path.length();
        if (end - start > 255) {
            end = start + 255;
        }
        return path.substring(start, end);
    }
}
