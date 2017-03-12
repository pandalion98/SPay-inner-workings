package android.sec.clipboard.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.data.ClipboardData;
import android.sec.clipboard.data.list.ClipboardDataHtml;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import com.android.internal.R;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.nio.channels.FileChannel;

public class FileHelper {
    private static final String BASE_64_ENCODING = ";base64";
    private static final int LENGTH_CONTENT_URI = "content://".length();
    private static final int LENGTH_HTTPS_URL = PREFIX_HTTPS_URL.length();
    private static final int LENGTH_HTTP_URL = PREFIX_HTTP_URL.length();
    private static final String PREFIX_CONTENT_URI = "content://";
    private static final String PREFIX_DATA = "data:";
    private static final String PREFIX_HTTPS_URL = "https://";
    private static final String PREFIX_HTTP_URL = "http://";
    private static FileHelper instance = new FileHelper();
    private File NullFile = new File("_TEMP_FILE");
    private final String TAG = "FileHelper";

    public static FileHelper getInstance() {
        return instance;
    }

    public boolean fileCopy(File src, File dest) {
        FileInputStream inputStream = null;
        try {
            dest.createNewFile();
            FileUtils.setPermissions(dest.getAbsolutePath(), 509, -1, -1);
            inputStream = new FileInputStream(src);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(dest);
            FileOutputStream fileOutputStream;
            if (inputStream == null || outputStream == null) {
                if (ClipboardConstants.DEBUG) {
                    Log.e("FileHelper", "break fileCopy()...because of inputStream :" + inputStream + ", or outputStream :" + outputStream);
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                fileOutputStream = outputStream;
                return 0;
            }
            boolean Result;
            FileChannel fcin = inputStream.getChannel();
            FileChannel fcout = outputStream.getChannel();
            try {
                fcin.transferTo(0, fcin.size(), fcout);
                fcout.close();
                fcin.close();
                outputStream.close();
                inputStream.close();
                Result = true;
                if (fcin != null) {
                    try {
                        fcin.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                    }
                }
                if (fcout != null) {
                    fcout.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e2222) {
                e2222.printStackTrace();
                Result = false;
                if (fcin != null) {
                    try {
                        fcin.close();
                    } catch (IOException e22222) {
                        e22222.printStackTrace();
                    }
                }
                if (fcout != null) {
                    fcout.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Throwable th) {
                if (fcin != null) {
                    try {
                        fcin.close();
                    } catch (IOException e222222) {
                        e222222.printStackTrace();
                    }
                }
                if (fcout != null) {
                    fcout.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            fileOutputStream = outputStream;
            return Result;
        } catch (FileNotFoundException e3) {
            e3.printStackTrace();
            return 0;
        }
    }

    public boolean fileCopy(ParcelFileDescriptor pfd, File dest) {
        IOException e;
        FileNotFoundException e2;
        Throwable th;
        boolean Result = false;
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        FileChannel fcin = null;
        FileChannel fcout = null;
        FileDescriptor fd = pfd.getFileDescriptor();
        try {
            dest.createNewFile();
            FileUtils.setPermissions(dest.getAbsolutePath(), 509, -1, -1);
            FileInputStream inputStream2 = new FileInputStream(fd);
            try {
                FileOutputStream outputStream2 = new FileOutputStream(dest);
                try {
                    fcin = inputStream2.getChannel();
                    fcout = outputStream2.getChannel();
                    fcin.transferTo(0, fcin.size(), fcout);
                    Result = true;
                    if (fcin != null) {
                        try {
                            fcin.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                            outputStream = outputStream2;
                            inputStream = inputStream2;
                        }
                    }
                    if (fcout != null) {
                        fcout.close();
                    }
                    if (inputStream2 != null) {
                        inputStream2.close();
                    }
                    if (outputStream2 != null) {
                        outputStream2.close();
                    }
                    pfd.close();
                    outputStream = outputStream2;
                    inputStream = inputStream2;
                } catch (FileNotFoundException e4) {
                    e2 = e4;
                    outputStream = outputStream2;
                    inputStream = inputStream2;
                    try {
                        e2.printStackTrace();
                        if (fcin != null) {
                            try {
                                fcin.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                                return 0;
                            }
                        }
                        if (fcout != null) {
                            fcout.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        pfd.close();
                        return 0;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fcin != null) {
                            try {
                                fcin.close();
                            } catch (IOException e322) {
                                e322.printStackTrace();
                                throw th;
                            }
                        }
                        if (fcout != null) {
                            fcout.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        pfd.close();
                        throw th;
                    }
                } catch (IOException e5) {
                    e322 = e5;
                    outputStream = outputStream2;
                    inputStream = inputStream2;
                    e322.printStackTrace();
                    if (fcin != null) {
                        try {
                            fcin.close();
                        } catch (IOException e3222) {
                            e3222.printStackTrace();
                        }
                    }
                    if (fcout != null) {
                        fcout.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    pfd.close();
                    return Result;
                } catch (Throwable th3) {
                    th = th3;
                    outputStream = outputStream2;
                    inputStream = inputStream2;
                    if (fcin != null) {
                        fcin.close();
                    }
                    if (fcout != null) {
                        fcout.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    pfd.close();
                    throw th;
                }
            } catch (FileNotFoundException e6) {
                e2 = e6;
                inputStream = inputStream2;
                e2.printStackTrace();
                if (fcin != null) {
                    fcin.close();
                }
                if (fcout != null) {
                    fcout.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                pfd.close();
                return 0;
            } catch (IOException e7) {
                e3222 = e7;
                inputStream = inputStream2;
                e3222.printStackTrace();
                if (fcin != null) {
                    fcin.close();
                }
                if (fcout != null) {
                    fcout.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                pfd.close();
                return Result;
            } catch (Throwable th4) {
                th = th4;
                inputStream = inputStream2;
                if (fcin != null) {
                    fcin.close();
                }
                if (fcout != null) {
                    fcout.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                pfd.close();
                throw th;
            }
        } catch (FileNotFoundException e8) {
            e2 = e8;
            e2.printStackTrace();
            if (fcin != null) {
                fcin.close();
            }
            if (fcout != null) {
                fcout.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            pfd.close();
            return 0;
        } catch (IOException e9) {
            e3222 = e9;
            e3222.printStackTrace();
            if (fcin != null) {
                fcin.close();
            }
            if (fcout != null) {
                fcout.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            pfd.close();
            return Result;
        }
        return Result;
    }

    public boolean saveObjectFile(String file, Object obj) {
        IOException e;
        Throwable th;
        if (obj == null) {
            if (ClipboardConstants.DEBUG) {
                Log.i("FileHelper", "obj == null");
            }
            return false;
        }
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos2 = new FileOutputStream(file);
            try {
                ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
                try {
                    oos2.writeObject(obj);
                    if (oos2 != null) {
                        try {
                            oos2.close();
                        } catch (IOException e2) {
                            if (ClipboardConstants.DEBUG) {
                                Log.d("FileHelper", "close : " + e2.getMessage());
                            }
                            e2.printStackTrace();
                            oos = oos2;
                            fos = fos2;
                            return true;
                        }
                    }
                    if (fos2 != null) {
                        fos2.close();
                    }
                    oos = oos2;
                    fos = fos2;
                    return true;
                } catch (IOException e3) {
                    e2 = e3;
                    oos = oos2;
                    fos = fos2;
                    try {
                        if (ClipboardConstants.DEBUG) {
                            Log.d("FileHelper", "saveObjectFile~IOException :" + e2.getMessage());
                        }
                        e2.printStackTrace();
                        if (oos != null) {
                            try {
                                oos.close();
                            } catch (IOException e22) {
                                if (ClipboardConstants.DEBUG) {
                                    Log.d("FileHelper", "close : " + e22.getMessage());
                                }
                                e22.printStackTrace();
                                return false;
                            }
                        }
                        if (fos != null) {
                            return false;
                        }
                        fos.close();
                        return false;
                    } catch (Throwable th2) {
                        th = th2;
                        if (oos != null) {
                            try {
                                oos.close();
                            } catch (IOException e222) {
                                if (ClipboardConstants.DEBUG) {
                                    Log.d("FileHelper", "close : " + e222.getMessage());
                                }
                                e222.printStackTrace();
                                throw th;
                            }
                        }
                        if (fos != null) {
                            fos.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    oos = oos2;
                    fos = fos2;
                    if (oos != null) {
                        oos.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                    throw th;
                }
            } catch (IOException e4) {
                e222 = e4;
                fos = fos2;
                if (ClipboardConstants.DEBUG) {
                    Log.d("FileHelper", "saveObjectFile~IOException :" + e222.getMessage());
                }
                e222.printStackTrace();
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    return false;
                }
                fos.close();
                return false;
            } catch (Throwable th4) {
                th = th4;
                fos = fos2;
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                throw th;
            }
        } catch (IOException e5) {
            e222 = e5;
            if (ClipboardConstants.DEBUG) {
                Log.d("FileHelper", "saveObjectFile~IOException :" + e222.getMessage());
            }
            e222.printStackTrace();
            if (oos != null) {
                oos.close();
            }
            if (fos != null) {
                return false;
            }
            fos.close();
            return false;
        }
    }

    public boolean createThumnailImage(String filePath) {
        Exception e;
        Throwable th;
        String thumFullPath = filePath + ClipboardConstants.THUMBNAIL_SUFFIX;
        boolean Result = false;
        Bitmap bm = getBitmap(filePath, 153, 86);
        if (bm == null) {
            if (ClipboardConstants.DEBUG) {
                Log.i("FileHelper", "createThumnailImage Bitmap is null");
            }
            return 0;
        }
        FileOutputStream fos = null;
        try {
            FileOutputStream fos2 = new FileOutputStream(thumFullPath);
            if (fos2 != null) {
                try {
                    bm.compress(CompressFormat.PNG, 50, fos2);
                    Result = true;
                } catch (Exception e2) {
                    e = e2;
                    fos = fos2;
                    try {
                        e.printStackTrace();
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        bm.recycle();
                        return Result;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fos = fos2;
                    if (fos != null) {
                        fos.close();
                    }
                    throw th;
                }
            }
            if (fos2 != null) {
                try {
                    fos2.close();
                } catch (IOException e322) {
                    e322.printStackTrace();
                    fos = fos2;
                }
            }
            fos = fos2;
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (fos != null) {
                fos.close();
            }
            bm.recycle();
            return Result;
        }
        bm.recycle();
        return Result;
    }

    public boolean createThumnailImage(String filePath, int width, int height) {
        Exception e;
        Throwable th;
        String thumFullPath = filePath + ClipboardConstants.THUMBNAIL_SUFFIX;
        Bitmap bm = getBitmap(filePath, width, height);
        boolean Result = false;
        if (bm == null) {
            if (ClipboardConstants.DEBUG) {
                Log.i("FileHelper", "createThumnailImage Bitmap is null");
            }
            return 0;
        }
        FileOutputStream fos = null;
        try {
            FileOutputStream fos2 = new FileOutputStream(thumFullPath);
            if (fos2 != null) {
                try {
                    bm.compress(CompressFormat.PNG, 50, fos2);
                    Result = true;
                } catch (Exception e2) {
                    e = e2;
                    fos = fos2;
                    try {
                        e.printStackTrace();
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        bm.recycle();
                        return Result;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fos = fos2;
                    if (fos != null) {
                        fos.close();
                    }
                    throw th;
                }
            }
            if (fos2 != null) {
                try {
                    fos2.close();
                } catch (IOException e322) {
                    e322.printStackTrace();
                    fos = fos2;
                }
            }
            fos = fos2;
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (fos != null) {
                fos.close();
            }
            bm.recycle();
            return Result;
        }
        bm.recycle();
        return Result;
    }

    public boolean createTemporaryThumnailImage(String filePath) {
        Exception e;
        Throwable th;
        String thumFullPath = filePath + ClipboardConstants.THUMBNAIL_SUFFIX;
        boolean Result = false;
        Bitmap bm = getBitmap(filePath, R.styleable.Theme_fragmentBreadCrumbItemLayout, 240);
        if (bm == null) {
            if (ClipboardConstants.DEBUG) {
                Log.i("FileHelper", "createThumnailImage Bitmap is null");
            }
            return 0;
        }
        FileOutputStream fos = null;
        try {
            FileOutputStream fos2 = new FileOutputStream(thumFullPath);
            if (fos2 != null) {
                try {
                    bm.compress(CompressFormat.PNG, 50, fos2);
                    Result = true;
                } catch (Exception e2) {
                    e = e2;
                    fos = fos2;
                    try {
                        e.printStackTrace();
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        bm.recycle();
                        return Result;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fos = fos2;
                    if (fos != null) {
                        fos.close();
                    }
                    throw th;
                }
            }
            if (fos2 != null) {
                try {
                    fos2.close();
                } catch (IOException e322) {
                    e322.printStackTrace();
                    fos = fos2;
                }
            }
            fos = fos2;
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (fos != null) {
                fos.close();
            }
            bm.recycle();
            return Result;
        }
        bm.recycle();
        return Result;
    }

    public String createThumnailFromData(Context context, ClipboardData clip) {
        Throwable th;
        ContentResolver contentResolver = context.getContentResolver();
        int thumbImageWidth = 384;
        int thumbImageHeight = 384;
        try {
            thumbImageWidth = (int) context.getResources().getDimension(R.dimen.tw_clipboard_thumnail_image_width);
            thumbImageHeight = (int) context.getResources().getDimension(R.dimen.tw_clipboard_thumnail_image_height);
        } catch (Exception e) {
            Exception e2;
            e2.printStackTrace();
        }
        if (clip == null) {
            if (ClipboardConstants.DEBUG) {
                Log.i("FileHelper", "createThumnailFromData() is false because clip is invalid data. clip :" + clip);
            }
            return null;
        } else if (clip.getFormat() == 4) {
            ClipboardDataHtml htmlClip = (ClipboardDataHtml) clip;
            if (ClipboardConstants.DEBUG) {
                Log.i("FileHelper", "Create preview image for html data in createThumnailFromData()");
            }
            Bitmap bm = null;
            String sFileName = "";
            try {
                sFileName = Html.fromHtml(Uri.decode(ClipboardProcText.getImgFileNameFormHtml(htmlClip.getHtml().toString()))).toString();
            } catch (Exception e22) {
                e22.printStackTrace();
            }
            if (sFileName == null || (sFileName != null && sFileName.length() < 1)) {
                if (ClipboardConstants.DEBUG) {
                    Log.w("FileHelper", "getFirstImage : FileName is empty.");
                }
                return null;
            }
            android.util.secutil.Log.d("FileHelper", "name = " + sFileName);
            int length = sFileName.length();
            if (sFileName.startsWith(PREFIX_DATA)) {
                int index = sFileName.indexOf(44);
                if (index > 0 && index < length && sFileName.substring(PREFIX_DATA.length(), index).contains(BASE_64_ENCODING)) {
                    bm = ClipboardDataBitmapUrl.getResizeBitmap(Base64.decode(sFileName.substring(index + 1).getBytes(), 4), thumbImageWidth, thumbImageHeight);
                }
            } else if ((length > LENGTH_HTTP_URL && sFileName.substring(0, LENGTH_HTTP_URL).compareTo(PREFIX_HTTP_URL) == 0) || (length > LENGTH_HTTPS_URL && sFileName.substring(0, LENGTH_HTTPS_URL).compareTo(PREFIX_HTTPS_URL) == 0)) {
                Log.i("FileHelper", "downloadSimpleBitmap");
                try {
                    android.util.secutil.Log.d("FileHelper", "html : " + htmlClip.getHtml().toString());
                    bm = ClipboardDataBitmapUrl.downloadSimpleBitmap(sFileName, thumbImageWidth, thumbImageHeight);
                } catch (Exception e222) {
                    e222.printStackTrace();
                    return null;
                }
            } else if (contentResolver == null || length <= LENGTH_CONTENT_URI || sFileName.substring(0, LENGTH_CONTENT_URI).compareTo("content://") != 0) {
                Log.d("FileHelper", "invalid data");
            } else {
                Log.i("FileHelper", "getUriPathBitmap...");
                bm = ClipboardDataBitmapUrl.getUriPathBitmap(contentResolver, Uri.parse(sFileName), thumbImageWidth, thumbImageHeight);
            }
            if (bm == null) {
                return null;
            }
            getInstance().makeDir(new File(ClipboardConstants.CLIPBOARD_ROOT_PATH_TEMP));
            String thumFullPath = new File(ClipboardConstants.CLIPBOARD_ROOT_PATH_TEMP, "previewhtemlclipboarditem") + ClipboardConstants.THUMBNAIL_SUFFIX + ClipboardConstants.THUMBNAIL_SUFFIX;
            FileOutputStream fos = null;
            try {
                FileOutputStream fos2 = new FileOutputStream(thumFullPath);
                if (fos2 != null) {
                    try {
                        bm.compress(CompressFormat.PNG, 50, fos2);
                    } catch (Exception e3) {
                        e222 = e3;
                        fos = fos2;
                        try {
                            e222.printStackTrace();
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e4) {
                                    e4.printStackTrace();
                                }
                            }
                            bm.recycle();
                            return thumFullPath;
                        } catch (Throwable th2) {
                            th = th2;
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e42) {
                                    e42.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        fos = fos2;
                        if (fos != null) {
                            fos.close();
                        }
                        throw th;
                    }
                }
                if (fos2 != null) {
                    try {
                        fos2.close();
                    } catch (IOException e422) {
                        e422.printStackTrace();
                        fos = fos2;
                    }
                }
                fos = fos2;
            } catch (Exception e5) {
                e222 = e5;
                e222.printStackTrace();
                if (fos != null) {
                    fos.close();
                }
                bm.recycle();
                return thumFullPath;
            }
            bm.recycle();
            return thumFullPath;
        } else {
            if (ClipboardConstants.DEBUG) {
                Log.i("FileHelper", "createThumnailFromData() is false because clip is not html type. clip.GetFomat() :" + clip.getFormat());
            }
            return null;
        }
    }

    private Bitmap getBitmap(String bitmapPath, int reqWidth, int reqHeight) {
        int sampleSize = 2;
        Bitmap bm = null;
        Options bitmapOption = new Options();
        bitmapOption.inJustDecodeBounds = true;
        bitmapOption.inPurgeable = true;
        try {
            if (ClipboardConstants.DEBUG) {
                Log.i("FileHelper", "BitmapFactory.decodeFile(bitmapPath, bitmapOption");
            }
            bm = BitmapFactory.decodeFile(bitmapPath, bitmapOption);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ClipboardConstants.DEBUG) {
            Log.i("FileHelper", "bitmapOption.outWidth:" + bitmapOption.outWidth + " bitmapOption.outHieght:" + bitmapOption.outHeight);
            Log.i("FileHelper", "mGridItemWidth:" + reqWidth + " mGridItemHeight:" + reqHeight);
        }
        while (bitmapOption.outWidth / sampleSize >= reqWidth && bitmapOption.outHeight / sampleSize >= reqHeight) {
            sampleSize++;
        }
        bitmapOption.inSampleSize = sampleSize - 1;
        try {
            bitmapOption.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(bitmapPath, bitmapOption);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return bm;
    }

    public Object loadObjectFile(File file) {
        IOException e;
        FileNotFoundException e2;
        Throwable th;
        StreamCorruptedException e3;
        ClassNotFoundException e4;
        ClassCastException e5;
        Object result = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            if (ClipboardConstants.DEBUG) {
                Log.d("FileHelper", "load object file");
            }
            FileInputStream fis2 = new FileInputStream(file);
            try {
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                try {
                    result = ois2.readObject();
                    if (ois2 != null) {
                        try {
                            ois2.close();
                        } catch (IOException e6) {
                            e6.printStackTrace();
                            ois = ois2;
                            fis = fis2;
                        }
                    }
                    if (fis2 != null) {
                        fis2.close();
                    }
                    ois = ois2;
                    fis = fis2;
                } catch (FileNotFoundException e7) {
                    e2 = e7;
                    ois = ois2;
                    fis = fis2;
                    try {
                        e2.printStackTrace();
                        if (ois != null) {
                            try {
                                ois.close();
                            } catch (IOException e62) {
                                e62.printStackTrace();
                            }
                        }
                        if (fis != null) {
                            fis.close();
                        }
                        return result;
                    } catch (Throwable th2) {
                        th = th2;
                        if (ois != null) {
                            try {
                                ois.close();
                            } catch (IOException e622) {
                                e622.printStackTrace();
                                throw th;
                            }
                        }
                        if (fis != null) {
                            fis.close();
                        }
                        throw th;
                    }
                } catch (StreamCorruptedException e8) {
                    e3 = e8;
                    ois = ois2;
                    fis = fis2;
                    e3.printStackTrace();
                    if (ois != null) {
                        try {
                            ois.close();
                        } catch (IOException e6222) {
                            e6222.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    return result;
                } catch (ClassNotFoundException e9) {
                    e4 = e9;
                    ois = ois2;
                    fis = fis2;
                    e4.printStackTrace();
                    if (ois != null) {
                        try {
                            ois.close();
                        } catch (IOException e62222) {
                            e62222.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    return result;
                } catch (IOException e10) {
                    e62222 = e10;
                    ois = ois2;
                    fis = fis2;
                    e62222.printStackTrace();
                    if (ois != null) {
                        try {
                            ois.close();
                        } catch (IOException e622222) {
                            e622222.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    return result;
                } catch (ClassCastException e11) {
                    e5 = e11;
                    ois = ois2;
                    fis = fis2;
                    e5.printStackTrace();
                    if (ois != null) {
                        try {
                            ois.close();
                        } catch (IOException e6222222) {
                            e6222222.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    return result;
                } catch (Throwable th3) {
                    th = th3;
                    ois = ois2;
                    fis = fis2;
                    if (ois != null) {
                        ois.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e12) {
                e2 = e12;
                fis = fis2;
                e2.printStackTrace();
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
                return result;
            } catch (StreamCorruptedException e13) {
                e3 = e13;
                fis = fis2;
                e3.printStackTrace();
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
                return result;
            } catch (ClassNotFoundException e14) {
                e4 = e14;
                fis = fis2;
                e4.printStackTrace();
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
                return result;
            } catch (IOException e15) {
                e6222222 = e15;
                fis = fis2;
                e6222222.printStackTrace();
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
                return result;
            } catch (ClassCastException e16) {
                e5 = e16;
                fis = fis2;
                e5.printStackTrace();
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
                return result;
            } catch (Throwable th4) {
                th = th4;
                fis = fis2;
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e17) {
            e2 = e17;
            e2.printStackTrace();
            if (ois != null) {
                ois.close();
            }
            if (fis != null) {
                fis.close();
            }
            return result;
        } catch (StreamCorruptedException e18) {
            e3 = e18;
            e3.printStackTrace();
            if (ois != null) {
                ois.close();
            }
            if (fis != null) {
                fis.close();
            }
            return result;
        } catch (ClassNotFoundException e19) {
            e4 = e19;
            e4.printStackTrace();
            if (ois != null) {
                ois.close();
            }
            if (fis != null) {
                fis.close();
            }
            return result;
        } catch (IOException e20) {
            e6222222 = e20;
            e6222222.printStackTrace();
            if (ois != null) {
                ois.close();
            }
            if (fis != null) {
                fis.close();
            }
            return result;
        } catch (ClassCastException e21) {
            e5 = e21;
            e5.printStackTrace();
            if (ois != null) {
                ois.close();
            }
            if (fis != null) {
                fis.close();
            }
            return result;
        }
        return result;
    }

    public String getSDCardPath() {
        String path = "";
        if (Environment.getExternalStorageState().equals("mounted")) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return "unmounted";
    }

    public boolean checkDir(File file) {
        return file.isDirectory();
    }

    public void makeDir(File file) {
        if (!file.exists()) {
            file.mkdirs();
            FileUtils.setPermissions(file.getAbsolutePath(), 509, -1, -1);
        }
    }

    public boolean checkFile(File file) {
        return file.isFile();
    }

    public File[] getList(File file) {
        return file.listFiles();
    }

    public void delete(File file) {
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] list = file.listFiles();
            if (list != null) {
                for (File f : list) {
                    delete(f);
                }
                file.delete();
            }
        }
    }

    public File getNullFile() {
        return this.NullFile;
    }

    public String getAbsoluteNullFile() {
        return this.NullFile.getAbsolutePath();
    }
}
